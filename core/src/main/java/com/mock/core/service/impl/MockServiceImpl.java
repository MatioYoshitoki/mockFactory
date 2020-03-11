package com.mock.core.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mock.common.exception.ExceptionPlus;
import com.mock.common.global.CloudCode;
import com.mock.common.global.CloudGlobal;
import com.mock.common.pojo.JsonPublic;
import com.mock.common.pojo.UserPo;
import com.mock.core.dao.ManifestMapper;
import com.mock.core.pojo.*;
import com.mock.core.service.MockService;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by matioyoshitoki on 2020/1/12.
 */
@Service
public class MockServiceImpl implements MockService {

    @Resource
    ManifestMapper manifestMapper;
    @Resource
    JedisPool jedisPool;


//    @Override
//    public String getMock(List<SingleInterfacePo> interfaceList, String manifestID, String port) throws IOException {
//
//        List<SingleInterfaceDetailPo> interfaceDetailList = new ArrayList<>();
//
//
//        String mockPath = null;
//        for (SingleInterfacePo singleInterface: interfaceList) {
//
//            List<ParamsDetailPo> paramsDetailList = new ArrayList<>();
//
//            try {
//                mockAns(singleInterface.getParams(), paramsDetailList);
//            } catch (Exception e) {
//                e.printStackTrace();
//                continue;
//            }
//
//            JSONObject jsonObject = new JSONObject();
//            JSONObject viewContent = new JSONObject();
//            try {
//                mockToJSON(singleInterface.getReturnContent(), jsonObject, viewContent);
//            } catch (Exception e) {
//                e.printStackTrace();
//                continue;
//            }
//
//            InterfacePo interfacePo = new InterfacePo();
//            interfacePo.setInterfaceID(MD5Util.md5(manifestID+singleInterface.getRequestURL()));
//            interfacePo.setManifestID(manifestID);
//            interfacePo.setInterfaceType(singleInterface.getInterfaceType());
//            interfacePo.setInterfaceName(singleInterface.getInterfaceName());
//            interfacePo.setRequestMap(singleInterface.getRequestURL());
//
//            SingleInterfaceDetailPo singleInterfaceDetail = new SingleInterfaceDetailPo(
//                    interfacePo.getInterfaceID(),
//                    paramsDetailList,
//                    jsonObject,
//                    viewContent.toJSONString(),
//                    singleInterface.getRequestURL()
//            );
//            mockPath = StringUtil.createMockFile(interfaceDetailList, manifestID, port);
//            interfacePo.setMockPath(mockPath);
//            try {
//                manifestMapper.addInterface(interfacePo);
//            }catch (Exception e){
//                e.printStackTrace();
//                continue;
//            }
//            interfaceDetailList.add(singleInterfaceDetail);
//        }
//
//        addInterfaceParamAndReturnParam(interfaceDetailList);
//
//        return mockPath;
//    }
//
//    @Override
//    @Transactional
//    public String getMockByFile(String fileBase64, String manifestName, String fileName, String port, HttpServletRequest request) throws Exception {
//
//        String extString = fileName.substring(fileName.lastIndexOf("."));
//
//        UserPo userPo = (UserPo) request.getAttribute("userPo");
//        InputStream is = Base64Util.base64ToInputStream(fileBase64);
//
//        try {
//            Workbook wb;
//            if (".xls".equals(extString)) {
//                wb = new HSSFWorkbook(is);
//            } else if (".xlsx".equals(extString)) {
//                wb = new XSSFWorkbook(is);
//            } else {
//                throw new Exception("文件格式错误");
//            }
//            Sheet sheet = wb.getSheetAt(0);
//            //获取sheet中，有数据的行数
//            int rowNum = sheet.getPhysicalNumberOfRows();
//            //因为模板是在第四行开始读取，那么我们的直接定位到第四行
////            int count = 0;
//            List<SingleInterfacePo> singleInterfacePos = new ArrayList<>();
//            for (int i = 1; i < rowNum; i++) {
//                //获取当前行
//                Row row = sheet.getRow(i);
//                if (row != null) {
//
//                    Cell interfaceName = row.getCell(1);
//                    Cell requestURL = row.getCell(2);
//                    Cell interfaceType = row.getCell(3);
//                    Cell params = row.getCell(4);
//                    Cell returnContent = row.getCell(5);
//                    if (StringUtil.isEmpty(requestURL.getStringCellValue())){
//                        break;
//                    }
//                    SingleInterfacePo singleInterfacePo = new SingleInterfacePo();
//                    singleInterfacePo.setInterfaceName(interfaceName.getStringCellValue());
//                    singleInterfacePo.setParams(Base64.encode(params.getStringCellValue().replace("\u2028", "\n").getBytes()));
//                    singleInterfacePo.setReturnContent(Base64.encode(returnContent.getStringCellValue().replace("\u2028", "\n").getBytes()));
//                    singleInterfacePo.setRequestURL(requestURL.getStringCellValue());
//                    singleInterfacePo.setInterfaceType(interfaceType.getStringCellValue());
//                    singleInterfacePos.add(singleInterfacePo);
//                }
//                //遍历结束
//            }
//            ManifestPo manifestPo = new ManifestPo();
//            manifestPo.setGroupID("123");
//            manifestPo.setManifestName(manifestName+"("+port+")");
//            manifestPo.setPort(port);
//            manifestPo.setManifestID(MD5Util.md5(manifestName+port));
//            manifestPo.setAuthorID(userPo.getUserID());
//            manifestMapper.addManifest(manifestPo);
//
//            String path = getMock(singleInterfacePos, manifestPo.getManifestID(), port);
//            return ReturnUtil.returnSuccess(path);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//        return "";
//    }

    @Override
    public JsonPublic getManifestList(String token) throws ExceptionPlus {
        UserPo userPo = getUserPoFromRedis(token);
        List<ManifestSummaryPo> manifestList = manifestMapper.getManifestList(userPo.getUserID(), null);
        JsonPublic jsonPublic = new JsonPublic();
        jsonPublic.setData(manifestList);
        return jsonPublic;
    }

    @Override
    public JsonPublic getInterfaceList(String manifestID, String token) throws ExceptionPlus {

        UserPo userPo = getUserPoFromRedis(token);

        if (manifestMapper.getManifestList(userPo.getUserID(), null).size()==0){
            return new JsonPublic(CloudCode.NOT_YOUR_MANIFEST, CloudCode.NOT_YOUR_MANIFEST_MESSAGE, null);
        }
        JsonPublic jsonPublic = new JsonPublic();
        jsonPublic.setData(manifestMapper.getInterfaceList(manifestID));
        return jsonPublic;
    }

    @Override
    public JsonPublic getManifestBase(String manifestID, String token) throws ExceptionPlus {
        UserPo userPo = getUserPoFromRedis(token);
        List<ManifestSummaryPo> manifestList = manifestMapper.getManifestList(userPo.getUserID(), manifestID);
        if (manifestList.size()==0){
            return new JsonPublic(CloudCode.NOT_YOUR_MANIFEST, CloudCode.NOT_YOUR_MANIFEST_MESSAGE, null);
        }
        JsonPublic jsonPublic = new JsonPublic();
        jsonPublic.setData(manifestList.get(0));
        return jsonPublic;
    }

    @Override
    public JsonPublic getInterfaceDetail(String interfaceID, String manifestID, String token) throws ExceptionPlus {


        UserPo userPo = getUserPoFromRedis(token);

        if (manifestMapper.getManifestList(userPo.getUserID(), null).size()==0){
            return new JsonPublic(CloudCode.NOT_YOUR_MANIFEST, CloudCode.NOT_YOUR_MANIFEST_MESSAGE, null);
        }

        List<InterfaceParamsPo> interfaceParamsList = manifestMapper.getInterfaceParams(interfaceID);

        String successReturn = manifestMapper.getInterfaceReturnContent(interfaceID);

        List<NullReturnPo> nullReturnList = new ArrayList<>();

        nullReturnList.add(new NullReturnPo("成功", successReturn));
        interfaceParamsList.forEach(one->{
            if (CloudGlobal.YES.equals(one.getCheckNull())){
                nullReturnList.add(new NullReturnPo("当'"+one.getParamName()+"'参数为空时", one.getNullReturn()));
            }
            switch (one.getParamType()){
                case "string":
                    one.setParamType("String");
                    break;
                case "cname":
                    one.setParamType("String");
                    break;
                case "int":
                    one.setParamType("int");
                    break;
                case "float":
                    one.setParamType("float");
                    break;
                case "md5":
                    one.setParamType("String");
                    break;
                default:
                    one.setParamType("String");
                    break;
            }
            one.setNullReturn(null);
        });
        JsonPublic jsonPublic = new JsonPublic();
        jsonPublic.setData(new InterfaceDetail(interfaceParamsList, nullReturnList));
        return jsonPublic;
    }


    private static void mockAns(String returnContentStr, List<ParamsDetailPo> paramsDetailPos) throws Exception {
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


                StringBuffer subMap = new StringBuffer();
                for (int j=i+1;j<returnContentList.length;j++){
                    startFlag = getStartFlag(startFlag, returnContentList[j]);
                    if (returnContentList[j].startsWith(startFlag)){
                        subMap.append(returnContentList[j].substring(startFlag.length())+"\n");
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
                StringBuffer subMap = new StringBuffer();
                for (int j=i+1;j<returnContentList.length;j++){
                    startFlag = getStartFlag(startFlag, returnContentList[j]);
                    if (!returnContentList[j].startsWith(startFlag)){
                        i=j+1;
                        break;
                    }
                    subMap.append(returnContentList[j].substring(startFlag.length())+"\n");
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

    public static void mockToJSON(String returnContentStr, JSONObject jsonObject, JSONObject viewContent) throws Exception {

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
//            System.out.println(contentName);


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
                StringBuffer subMap = new StringBuffer();
                startFlag = getStartFlag(startFlag, returnContentList[i+1]);
                for (int j=i+1;j<returnContentList.length;j++){

                    if (!returnContentList[j].startsWith(startFlag)){
                        break;
                    }
                    i=j;
                    subMap.append(returnContentList[j].substring(startFlag.length())+"\n");
                }

                mockToJSON(subMap.toString() ,tmp, viewTmp);

                jsonObject.put(contentName ,tmp);
                viewContent.put(contentName, viewTmp);
            }else if ("listMap".equals(contentType)){
                JSONObject tmp = new JSONObject();
                JSONObject viewTmp = new JSONObject();
                StringBuffer subMap = new StringBuffer();
                startFlag = getStartFlag(startFlag, returnContentList[i+1]);
                for (int j=i+1;j<returnContentList.length;j++){

                    if (!returnContentList[j].startsWith(startFlag)){
                        break;
                    }
                    i=j;
                    subMap.append(returnContentList[j].substring(startFlag.length())+"\n");
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
                    case "fixed":
                        jsonObject.put(contentName, fixedOrEnumValue);
                        viewContent.put(contentName, fixedOrEnumValue);
                        break;
                    case "ctitle":
                        jsonObject.put(contentName, "@ctitle");
                        viewContent.put(contentName, "");
                        break;
                    default:
                        jsonObject.put(contentName, fixedOrEnumValue);
                        viewContent.put(contentName, fixedOrEnumValue);
                        break;

                }
            }
        }
    }

    public void addInterfaceParamAndReturnParam(List<SingleInterfaceDetailPo> interfaceDetailList){
        new Thread(() -> {
            interfaceDetailList.forEach(one-> {
                addReturnContent(one.getInterfaceID(), one.getReturnJson().toJSONString(), one.getViewContent());
                one.getParamsDetailPoList().forEach(param-> addParam(param.getParamName(), one.getInterfaceID(), param.isCheckNull()?"Y":"N", param.getNullReturn(), param.getParamType(), ""));
            });
        }).start();
    }

    public void addReturnContent(String interfaceID, String mockContent, String viewContent){
        new Thread(() -> {
            manifestMapper.addReturnContent(new ReturnContentPo(SecureUtil.md5(interfaceID+mockContent), interfaceID, mockContent, viewContent));
        }).start();
    }

    public void addParam(String paramName, String interfaceID, String checkNull, String nullReturn, String paramType, String mark){
        InterfaceParamsPo interfaceParamsPo = new InterfaceParamsPo(SecureUtil.md5(interfaceID+paramName), paramName, interfaceID, checkNull, nullReturn, paramType, mark);
        manifestMapper.addInterfaceParams(interfaceParamsPo);
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

    private UserPo getUserPoFromRedis(String token) throws ExceptionPlus {
        Jedis jedis = null;
        UserPo userPo;
        try {
            jedis = jedisPool.getResource();
            if (!jedis.exists(token)){
                throw new ExceptionPlus(CloudCode.WRONG_TOKEN, CloudCode.WRONG_TOKEN_MESSAGE);
            }
            userPo = JSON.parseObject(jedis.get(token), UserPo.class);
        }finally {
            if (jedis!=null){
                jedis.close();
            }
        }
        return userPo;
    }
}
