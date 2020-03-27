package com.mock.core.service.impl;

import com.mock.common.exception.ExceptionPlus;
import com.mock.common.global.CloudCode;
import com.mock.common.global.CloudGlobal;
import com.mock.common.pojo.JsonPublic;
import com.mock.common.pojo.UserPo;
import com.mock.common.util.RedisUtil;
import com.mock.core.dao.ManifestMapper;
import com.mock.core.pojo.InterfaceDetail;
import com.mock.core.pojo.InterfaceParamsPo;
import com.mock.core.pojo.ManifestSummaryPo;
import com.mock.core.pojo.NullReturnPo;
import com.mock.core.service.MockService;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.ArrayList;
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


    @Override
    public JsonPublic getManifestList(String token) throws ExceptionPlus {
        UserPo userPo = RedisUtil.getFromRedis(jedisPool, token, UserPo.class);
        if (userPo==null){
            throw new ExceptionPlus(CloudCode.WRONG_TOKEN, CloudCode.WRONG_TOKEN_MESSAGE);
        }
        List<ManifestSummaryPo> manifestList = manifestMapper.getManifestList(userPo.getUserID(), null);
        JsonPublic jsonPublic = new JsonPublic();
        jsonPublic.setData(manifestList);
        return jsonPublic;
    }

    @Override
    public JsonPublic getInterfaceList(String manifestID, String token) throws ExceptionPlus {

        UserPo userPo = RedisUtil.getFromRedis(jedisPool, token, UserPo.class);
        if (userPo==null){
            throw new ExceptionPlus(CloudCode.WRONG_TOKEN, CloudCode.WRONG_TOKEN_MESSAGE);
        }

        if (manifestMapper.getManifestList(userPo.getUserID(), null).size()==0){
            return new JsonPublic(CloudCode.NOT_YOUR_MANIFEST, CloudCode.NOT_YOUR_MANIFEST_MESSAGE, null);
        }
        JsonPublic jsonPublic = new JsonPublic();
        jsonPublic.setData(manifestMapper.getInterfaceList(manifestID));
        return jsonPublic;
    }

    @Override
    public JsonPublic getManifestBase(String manifestID, String token) throws ExceptionPlus {
        UserPo userPo = RedisUtil.getFromRedis(jedisPool, token, UserPo.class);
        if (userPo==null){
            throw new ExceptionPlus(CloudCode.WRONG_TOKEN, CloudCode.WRONG_TOKEN_MESSAGE);
        }
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


        UserPo userPo = RedisUtil.getFromRedis(jedisPool, token, UserPo.class);
        if (userPo==null){
            throw new ExceptionPlus(CloudCode.WRONG_TOKEN, CloudCode.WRONG_TOKEN_MESSAGE);
        }

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
                case "int":
                    one.setParamType("int");
                    break;
                case "float":
                    one.setParamType("float");
                    break;
                case "string":
                case "cname":
                case "md5":
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


//    private static void mockAns(String returnContentStr, List<ParamsDetailPo> paramsDetailPos) throws Exception {
//        String[] returnContentList = returnContentStr.split("\n");
//
//        String startFlag = null;
//
//        for (int i=0;i<returnContentList.length;i++){
//            String line = returnContentList[i];
//            String[] contentParam = line.split("#");
//
//            if (contentParam.length<2){
//                continue;
//            }
//            String contentType ;
//            String contentName = contentParam[0].trim();
//            boolean checkNull = false;
//            String nullReturn = null;
//
//
//
//            if (contentParam[1].contains("checkNull")){
//                checkNull = true;
//                String tmp = contentParam[1].replace("checkNull ", "");
//                contentType = tmp.substring(0, tmp.indexOf(" "));
//                nullReturn = tmp.substring(tmp.indexOf(" ")).replace("#","").replace("'","\"");
//            }else {
//                contentType = contentParam[1];
//            }
//
//            if ("map".equals(contentType)) {
//
//                ParamsDetailPo paramsDetail = new ParamsDetailPo();
//                paramsDetail.setParamName(contentName);
//                paramsDetail.setCheckNull(checkNull);
//                paramsDetail.setNullReturn(nullReturn);
//
//                List<ParamsDetailPo> tmpList = new ArrayList<>();
//
//
//                StringBuilder subMap = new StringBuilder();
//                for (int j=i+1;j<returnContentList.length;j++){
//                    startFlag = getStartFlag(startFlag, returnContentList[j]);
//                    if (returnContentList[j].startsWith(startFlag)){
//                        subMap.append(returnContentList[j].substring(startFlag.length())).append("\n");
//                        i=j+1;
//                        break;
//                    }
//                }
//                mockAns(subMap.toString(), tmpList);
//
//                paramsDetail.setListParam(tmpList);
//                paramsDetail.setParamType(contentType);
//                paramsDetailPos.add(paramsDetail);
//
//            }else if ("listMap".equals(contentType)){
//                ParamsDetailPo paramsDetail = new ParamsDetailPo();
//                paramsDetail.setParamName(contentName+"|1-5");
//                paramsDetail.setCheckNull(checkNull);
//                paramsDetail.setNullReturn(nullReturn);
//                paramsDetail.setParamType(contentType);
//                List<ParamsDetailPo> tmpList = new ArrayList<>();
//                StringBuilder subMap = new StringBuilder();
//                for (int j=i+1;j<returnContentList.length;j++){
//                    startFlag = getStartFlag(startFlag, returnContentList[j]);
//                    if (!returnContentList[j].startsWith(startFlag)){
//                        i=j+1;
//                        break;
//                    }
//                    subMap.append(returnContentList[j].substring(startFlag.length())).append("\n");
//                }
//
//                mockAns(subMap.toString(), tmpList);
//                paramsDetail.setListParam(tmpList);
//                paramsDetail.setParamType(contentType);
//                paramsDetailPos.add(paramsDetail);
//            }else {
//                ParamsDetailPo paramsDetail = new ParamsDetailPo();
//                paramsDetail.setCheckNull(checkNull);
//                paramsDetail.setNullReturn(nullReturn);
//                paramsDetail.setParamName(contentName);
//                paramsDetail.setParamType(contentType);
//                paramsDetailPos.add(paramsDetail);
//
//            }
//        }
//    }

//    public static void mockToJSON(String returnContentStr, JSONObject jsonObject, JSONObject viewContent) throws Exception {
//
//        String[] returnContentList = returnContentStr.split("\n");
//        String startFlag = null;
//        for (int i=0;i<returnContentList.length;i++){
//            String line = returnContentList[i];
//
//            String[] contentParam = line.split("#");
//
//            if (contentParam.length<2){
//                continue;
//            }
//            String contentType ;
//            String contentName = contentParam[0].replace(" ", "").replace("\t", "");
////            System.out.println(contentName);
//
//
//            String fixedOrEnumValue = null;
//            if (contentParam[1].contains("fixed")||contentParam[1].toLowerCase().contains("enum")){
//                String[] tmp = contentParam[1].split(" ");
//                contentType = tmp[0];
//                fixedOrEnumValue = tmp[1];
//            }else {
//                contentType = contentParam[1];
//            }
//
//            if ("map".equals(contentType)) {
//                JSONObject tmp = new JSONObject();
//                JSONObject viewTmp = new JSONObject();
//                StringBuilder subMap = new StringBuilder();
//                startFlag = getStartFlag(startFlag, returnContentList[i+1]);
//                for (int j=i+1;j<returnContentList.length;j++){
//
//                    if (!returnContentList[j].startsWith(startFlag)){
//                        break;
//                    }
//                    i=j;
//                    subMap.append(returnContentList[j].substring(startFlag.length())).append("\n");
//                }
//
//                mockToJSON(subMap.toString() ,tmp, viewTmp);
//
//                jsonObject.put(contentName ,tmp);
//                viewContent.put(contentName, viewTmp);
//            }else if ("listMap".equals(contentType)){
//                JSONObject tmp = new JSONObject();
//                JSONObject viewTmp = new JSONObject();
//                StringBuilder subMap = new StringBuilder();
//                startFlag = getStartFlag(startFlag, returnContentList[i+1]);
//                for (int j=i+1;j<returnContentList.length;j++){
//
//                    if (!returnContentList[j].startsWith(startFlag)){
//                        break;
//                    }
//                    i=j;
//                    subMap.append(returnContentList[j].substring(startFlag.length())).append("\n");
//                }
//                mockToJSON(subMap.toString() ,tmp, viewTmp);
//
//                jsonObject.put(contentName.replace(StrUtil.nullToEmpty(startFlag), "")+"|1-5" , JSONArray.parseArray("["+tmp+"]", HashMap.class));
//                viewContent.put(contentName, JSONArray.parseArray("["+viewTmp+"]", HashMap.class));
//            }else {
//
//                String lowContentType = contentType.toLowerCase();
//                switch (lowContentType) {
//                    case "string":
//                        jsonObject.put(contentName + "|1", "@ctitle");
//                        viewContent.put(contentName, "");
//                        break;
//                    case "List":
//                        jsonObject.put(contentName + "|1-5", JSONArray.parseArray(fixedOrEnumValue, String.class));
//                        viewContent.put(contentName, "['']");
//                        break;
//                    case "cname":
//                        jsonObject.put(contentName, "@cname");
//                        viewContent.put(contentName, "");
//                        break;
//                    case "image":
//                        jsonObject.put(contentName, "@image()");
//                        viewContent.put(contentName, "");
//                        break;
//                    case "stringenum":
//                        jsonObject.put(contentName + "|1", JSONArray.parseArray(fixedOrEnumValue, String.class));
//                        viewContent.put(contentName, "");
//                        break;
//                    case "intenum":
//                        jsonObject.put(contentName + "|1", JSONArray.parseArray(fixedOrEnumValue, Integer.class));
//                        viewContent.put(contentName, 0);
//                        break;
//                    case "floatenum":
//                        jsonObject.put(contentName + "|1", JSONArray.parseArray(fixedOrEnumValue, Float.class));
//                        viewContent.put(contentName, 0.0);
//                        break;
//                    case "int":
//                        jsonObject.put(contentName + "|1-100", 1);
//                        viewContent.put(contentName, 9);
//                        break;
//                    case "md5":
//                        jsonObject.put(contentName+"|32", "A");
//                        viewContent.put(contentName, "");
//                        break;
//                    case "datetime":
//                        jsonObject.put(contentName, "@dateTime");
//                        viewContent.put(contentName, "");
//                        break;
//                    case "ctitle":
//                        jsonObject.put(contentName, "@ctitle");
//                        viewContent.put(contentName, "");
//                        break;
//                    case "fixed":
//                    default:
//                        jsonObject.put(contentName, fixedOrEnumValue);
//                        viewContent.put(contentName, fixedOrEnumValue);
//                        break;
//
//                }
//            }
//        }
//    }

//    public void addInterfaceParamAndReturnParam(List<SingleInterfaceDetailPo> interfaceDetailList){
//        new Thread(() -> interfaceDetailList.forEach(one-> {
//            addReturnContent(one.getInterfaceID(), one.getReturnJson().toJSONString(), one.getViewContent());
//            one.getParamsDetailPoList().forEach(param-> addParam(param.getParamName(), one.getInterfaceID(), param.isCheckNull()?"Y":"N", param.getNullReturn(), param.getParamType(), ""));
//        })).start();
//    }

//    public void addReturnContent(String interfaceID, String mockContent, String viewContent){
//        new Thread(() -> {
//            manifestMapper.addReturnContent(new ReturnContentPo(SecureUtil.md5(interfaceID+mockContent), interfaceID, mockContent, viewContent));
//        }).start();
//    }

//    public void addParam(String paramName, String interfaceID, String checkNull, String nullReturn, String paramType, String mark){
//        InterfaceParamsPo interfaceParamsPo = new InterfaceParamsPo(SecureUtil.md5(interfaceID+paramName), paramName, interfaceID, checkNull, nullReturn, paramType, mark);
//        manifestMapper.addInterfaceParams(interfaceParamsPo);
//    }
//
//    private static String getStartFlag(String startFlag, String line) {
//        if (startFlag == null){
//            if (line.startsWith("\t")) {
//                startFlag = "\t";
//            } else if (line.startsWith("    ")) {
//                startFlag = "    ";
//            }else if (line.startsWith(" ")){
//                startFlag = " ";
//            }
//        }
//        return startFlag;
//    }

}
