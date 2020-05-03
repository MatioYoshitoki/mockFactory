package com.mock.file.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mock.common.exception.Assert;
import com.mock.common.global.CacheNames;
import com.mock.common.global.CloudCode;
import com.mock.common.pojo.*;
import com.mock.common.util.Base64Util;
import com.mock.common.util.MyStrUtil;
import com.mock.file.dao.ManifestMapper;
import com.mock.file.pojo.*;
import com.mock.file.service.MockFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class MockFileServiceImpl implements MockFileService {

    @Resource
    ManifestMapper manifestMapper;
    @Resource
    RedisCacheManager redisCacheManager;

    @Override
    public String getMock(List<SingleInterfacePo> interfaceList, String manifestID, String port) throws Exception {
        List<SingleInterfaceDetailPo> interfaceDetailList = new ArrayList<>();

        String mockPath = null;
        for (SingleInterfacePo singleInterface: interfaceList) {

            List<ParamsDetailPo> paramsDetailList = new ArrayList<>();

            try {
                mockAns(singleInterface.getParams(), paramsDetailList);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            JSONObject jsonObject = new JSONObject();
            JSONObject viewContent = new JSONObject();
            try {
                mockToJSON(singleInterface.getReturnContent(), jsonObject, viewContent);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            InterfacePo interfacePo = new InterfacePo();
            interfacePo.setInterfaceID(SecureUtil.md5(manifestID+singleInterface.getRequestURL()));
            interfacePo.setManifestID(manifestID);
            interfacePo.setInterfaceType(singleInterface.getInterfaceType());
            interfacePo.setInterfaceName(singleInterface.getInterfaceName());
            interfacePo.setRequestMap(singleInterface.getRequestURL());

            SingleInterfaceDetailPo singleInterfaceDetail = new SingleInterfaceDetailPo(
                    interfacePo.getInterfaceID(),
                    paramsDetailList,
                    jsonObject,
                    viewContent.toJSONString(),
                    singleInterface.getRequestURL()
            );
            mockPath = MyStrUtil.createMockFile(interfaceDetailList, manifestID, port);
            interfacePo.setMockPath(mockPath);
            try {
                manifestMapper.addInterface(interfacePo);
            }catch (Exception e){
                e.printStackTrace();
                continue;
            }
            interfaceDetailList.add(singleInterfaceDetail);
        }

        addInterfaceParamAndReturnParam(interfaceDetailList);

        return mockPath;
    }

    @Override
    public String getMockByFile(MockFilePo mockFilePo, String token) throws Exception {

        String fileBase64 = mockFilePo.getFile();
        String fileName = mockFilePo.getFileName();
        String manifestName = mockFilePo.getManifestName();
        String port = mockFilePo.getPort();

        String extString = fileName.substring(fileName.lastIndexOf("."));

        UserPo userPo = Objects.requireNonNull(redisCacheManager.getCache(CacheNames.USER_CACHE_NAME)).get(token, UserPo.class);
        Assert.isNull(userPo, CloudCode.WRONG_TOKEN, CloudCode.WRONG_TOKEN_MESSAGE);
        InputStream is = Base64Util.base64ToInputStream(fileBase64);

        Workbook wb;
        try {
            if (".xls".equals(extString)) {
                wb = new HSSFWorkbook(is);
            } else if (".xlsx".equals(extString)) {
                wb = new XSSFWorkbook(is);
            } else {
                throw new IllegalArgumentException("文件格式错误");
            }
        }finally {
            is.close();
        }
        Sheet sheet = wb.getSheetAt(0);
        int rowNum = sheet.getPhysicalNumberOfRows();
        List<SingleInterfacePo> singleInterfacePos = new ArrayList<>();
        for (int i = 1; i < rowNum; i++) {
            //获取当前行
            Row row = sheet.getRow(i);
            if (row != null) {

                Cell interfaceName = row.getCell(1);
                Cell requestURL = row.getCell(2);
                Cell interfaceType = row.getCell(3);
                Cell params = row.getCell(4);
                Cell returnContent = row.getCell(5);
                if (StrUtil.isEmpty(requestURL.getStringCellValue())){
                    break;
                }
                if ("接口名称".equals(interfaceName.getStringCellValue())){
                    continue;
                }

                SingleInterfacePo singleInterfacePo = new SingleInterfacePo();
                singleInterfacePo.setInterfaceName(interfaceName.getStringCellValue());
                singleInterfacePo.setParams((params.getStringCellValue().replace("\u2028", "\n")));
                singleInterfacePo.setReturnContent((returnContent.getStringCellValue().replace("\u2028", "\n")));
                singleInterfacePo.setRequestURL(requestURL.getStringCellValue());
                singleInterfacePo.setInterfaceType(interfaceType.getStringCellValue());
                log.info(singleInterfacePo.toString());
                singleInterfacePos.add(singleInterfacePo);
            }
            //遍历结束
        }
        ManifestPo manifestPo = new ManifestPo();
        manifestPo.setGroupID("123");
        manifestPo.setManifestName(manifestName+"("+port+")");
        manifestPo.setPort(port);
        manifestPo.setManifestID(SecureUtil.md5(manifestName+port));
        manifestPo.setAuthorID(userPo.getUserID());
        manifestMapper.addManifest(manifestPo);

        return getMock(singleInterfacePos, manifestPo.getManifestID(), port);

    }



    private static void mockAns(String returnContentStr, List<ParamsDetailPo> paramsDetailPos) {
        String[] returnContentList = returnContentStr.split("\n");

        String startFlag = null;

        for (int i=0;i<returnContentList.length;i++){
            String line = returnContentList[i];
            String[] contentParam = line.split("#");

            if (contentParam.length<2){
                continue;
            }
            String contentType ;
            String contentName = contentParam[0].trim();
            boolean checkNull = false;
            String nullReturn = null;



            if (contentParam[1].contains("checkNull")){
                checkNull = true;
                String tmp = contentParam[1].replace("checkNull ", "");
                contentType = tmp.substring(0, tmp.indexOf(" "));
                nullReturn = tmp.substring(tmp.indexOf(" ")).replace("#","").replace("'","\"");
            }else {
                contentType = contentParam[1];
            }

            if ("map".equals(contentType)) {

                ParamsDetailPo paramsDetail = new ParamsDetailPo();
                paramsDetail.setParamName(contentName);
                paramsDetail.setCheckNull(checkNull);
                paramsDetail.setNullReturn(nullReturn);

                List<ParamsDetailPo> tmpList = new ArrayList<>();


                StringBuilder subMap = new StringBuilder();
                for (int j=i+1;j<returnContentList.length;j++){
                    startFlag = getStartFlag(startFlag, returnContentList[j]);
                    if (returnContentList[j].startsWith(startFlag)){
                        subMap.append(returnContentList[j].substring(startFlag.length())).append("\n");
                        i=j+1;
                        break;
                    }
                }
                mockAns(subMap.toString(), tmpList);

                paramsDetail.setListParam(tmpList);
                paramsDetail.setParamType(contentType);
                paramsDetailPos.add(paramsDetail);

            }else if ("listMap".equals(contentType)){
                ParamsDetailPo paramsDetail = new ParamsDetailPo();
                paramsDetail.setParamName(contentName+"|1-5");
                paramsDetail.setCheckNull(checkNull);
                paramsDetail.setNullReturn(nullReturn);
                paramsDetail.setParamType(contentType);
                List<ParamsDetailPo> tmpList = new ArrayList<>();
                StringBuilder subMap = new StringBuilder();
                for (int j=i+1;j<returnContentList.length;j++){
                    startFlag = getStartFlag(startFlag, returnContentList[j]);
                    if (!returnContentList[j].startsWith(startFlag)){
                        i=j+1;
                        break;
                    }
                    subMap.append(returnContentList[j].substring(startFlag.length())).append("\n");
                }

                mockAns(subMap.toString(), tmpList);
                paramsDetail.setListParam(tmpList);
                paramsDetail.setParamType(contentType);
                paramsDetailPos.add(paramsDetail);
            }else {
                ParamsDetailPo paramsDetail = new ParamsDetailPo();
                paramsDetail.setCheckNull(checkNull);
                paramsDetail.setNullReturn(nullReturn);
                paramsDetail.setParamName(contentName);
                paramsDetail.setParamType(contentType);
                paramsDetailPos.add(paramsDetail);

            }
        }
    }

    public static void mockToJSON(String returnContentStr, JSONObject jsonObject, JSONObject viewContent) {

        String[] returnContentList = returnContentStr.split("\n");
        String startFlag = null;
        for (int i=0;i<returnContentList.length;i++){
            String line = returnContentList[i];

            String[] contentParam = line.split("#");

            if (contentParam.length<2){
                continue;
            }
            String contentType ;
            String contentName = contentParam[0].replace(" ", "").replace("\t", "");

            String fixedOrEnumValue = null;
            if (contentParam[1].contains("fixed")||contentParam[1].toLowerCase().contains("enum")){
                String[] tmp = contentParam[1].split(" ");
                contentType = tmp[0];
                fixedOrEnumValue = tmp[1];
            }else {
                contentType = contentParam[1];
            }

            if ("map".equals(contentType)) {
                JSONObject tmp = new JSONObject();
                JSONObject viewTmp = new JSONObject();
                StringBuilder subMap = new StringBuilder();
                startFlag = getStartFlag(startFlag, returnContentList[i+1]);
                for (int j=i+1;j<returnContentList.length;j++){

                    if (!returnContentList[j].startsWith(startFlag)){
                        break;
                    }
                    i=j;
                    subMap.append(returnContentList[j].substring(startFlag.length())).append("\n");
                }

                mockToJSON(subMap.toString() ,tmp, viewTmp);

                jsonObject.put(contentName ,tmp);
                viewContent.put(contentName, viewTmp);
            }else if ("listMap".equals(contentType)){
                JSONObject tmp = new JSONObject();
                JSONObject viewTmp = new JSONObject();
                StringBuilder subMap = new StringBuilder();
                startFlag = getStartFlag(startFlag, returnContentList[i+1]);
                for (int j=i+1;j<returnContentList.length;j++){

                    if (!returnContentList[j].startsWith(startFlag)){
                        break;
                    }
                    i=j;
                    subMap.append(returnContentList[j].substring(startFlag.length())).append("\n");
                }
                mockToJSON(subMap.toString() ,tmp, viewTmp);

                jsonObject.put(contentName.replace(StrUtil.nullToEmpty(startFlag), "")+"|1-5" , JSONArray.parseArray("["+tmp+"]", HashMap.class));
                viewContent.put(contentName, JSONArray.parseArray("["+viewTmp+"]", HashMap.class));
            }else {

                String lowContentType = contentType.toLowerCase();
                switch (lowContentType) {
                    case "string":
                        jsonObject.put(contentName + "|1", "@ctitle");
                        viewContent.put(contentName, "");
                        break;
                    case "List":
                        jsonObject.put(contentName + "|1-5", JSONArray.parseArray(fixedOrEnumValue, String.class));
                        viewContent.put(contentName, "['']");
                        break;
                    case "cname":
                        jsonObject.put(contentName, "@cname");
                        viewContent.put(contentName, "");
                        break;
                    case "image":
                        jsonObject.put(contentName, "@image()");
                        viewContent.put(contentName, "");
                        break;
                    case "stringenum":
                        jsonObject.put(contentName + "|1", JSONArray.parseArray(fixedOrEnumValue, String.class));
                        viewContent.put(contentName, "");
                        break;
                    case "intenum":
                        jsonObject.put(contentName + "|1", JSONArray.parseArray(fixedOrEnumValue, Integer.class));
                        viewContent.put(contentName, 0);
                        break;
                    case "floatenum":
                        jsonObject.put(contentName + "|1", JSONArray.parseArray(fixedOrEnumValue, Float.class));
                        viewContent.put(contentName, 0.0);
                        break;
                    case "int":
                        jsonObject.put(contentName + "|1-100", 1);
                        viewContent.put(contentName, 9);
                        break;
                    case "md5":
                        jsonObject.put(contentName+"|32", "A");
                        viewContent.put(contentName, "");
                        break;
                    case "datetime":
                        jsonObject.put(contentName, "@dateTime");
                        viewContent.put(contentName, "");
                        break;
                    case "ctitle":
                        jsonObject.put(contentName, "@ctitle");
                        viewContent.put(contentName, "");
                        break;
                    case "fixed":
                    default:
                        jsonObject.put(contentName, fixedOrEnumValue);
                        viewContent.put(contentName, fixedOrEnumValue);
                        break;

                }
            }
        }
    }

    private static String getStartFlag(String startFlag, String line) {
        if (startFlag == null){
            if (line.startsWith("\t")) {
                startFlag = "\t";
            } else if (line.startsWith("    ")) {
                startFlag = "    ";
            }else if (line.startsWith(" ")){
                startFlag = " ";
            }
        }
        return startFlag;
    }

    public void addInterfaceParamAndReturnParam(List<SingleInterfaceDetailPo> interfaceDetailList){
        new Thread(() -> interfaceDetailList.forEach(one-> {
            addReturnContent(one.getInterfaceID(), one.getReturnJson().toJSONString(), one.getViewContent());
            one.getParamsDetailPoList().forEach(param-> addParam(param.getParamName(), one.getInterfaceID(), param.isCheckNull()?"Y":"N", param.getNullReturn(), param.getParamType(), ""));
        })).start();
    }
    public void addReturnContent(String interfaceID, String mockContent, String viewContent){
        new Thread(() -> manifestMapper.addReturnContent(new ReturnContentPo(SecureUtil.md5(interfaceID+mockContent), interfaceID, mockContent, viewContent))).start();
    }
    public void addParam(String paramName, String interfaceID, String checkNull, String nullReturn, String paramType, String mark){
        InterfaceParamsPo interfaceParamsPo = new InterfaceParamsPo(SecureUtil.md5(interfaceID+paramName), paramName, interfaceID, checkNull, nullReturn, paramType, mark);
        manifestMapper.addInterfaceParams(interfaceParamsPo);
    }
}
