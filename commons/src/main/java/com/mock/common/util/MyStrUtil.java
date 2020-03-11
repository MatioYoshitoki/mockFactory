package com.mock.common.util;

import com.alibaba.fastjson.JSONObject;
import com.mock.common.pojo.ParamsDetailPo;
import com.mock.common.pojo.SingleInterfaceDetailPo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyStrUtil {

    public static String createMockFile(List<SingleInterfaceDetailPo> interfaceDetailList, String manifestID, String port) throws IOException {
        StringBuffer result = new StringBuffer();
        result.append("let express = require('express');   //引入express\n" +
                "let Mock = require('mockjs');       //引入mock\n" +
                "var bodyParser = require('body-parser');\n" +
                "\n" +
                "\n" +
                "//实例化express\n" +
                "let app = express();\n" +
                "\n" +
                "app.use(bodyParser());\n" +
                "\n" +
                "app.use(function(req, res, next) {\n" +
                "    res.header('Access-Control-Allow-Origin', '*');\n" +
                "    res.header('Access-Control-Allow-Methods', 'PUT, GET, POST, DELETE, OPTIONS');\n" +
                "    res.header('Access-Control-Allow-Headers', 'X-Requested-With');\n" +
                "    res.header('Access-Control-Allow-Headers', 'Content-Type');\n" +
                "    next();\n" +
                "});\n");


        for (SingleInterfaceDetailPo singleInterfaceDetailPo : interfaceDetailList) {

            List<ParamsDetailPo> paramsDetailPos = singleInterfaceDetailPo.getParamsDetailPoList();
            JSONObject returnJson = singleInterfaceDetailPo.getReturnJson();

            result.append("app.use('"+singleInterfaceDetailPo.getUrl()+"',function(req, res){\n");

            paramsDetailPos.stream().filter(p -> p.isCheckNull()).forEach(p -> {
                result.append(
                        "\tlet " + p.getParamName() + " = req.query." + p.getParamName() + ";\n" +
                                "\tif (" + p.getParamName() + "==null){\n" +
                                "\t    res.send('" + p.getNullReturn().replace("'","\"") + "');\n" +
                                "\t    return;\n" +
                                "\t}\n");
            });
            result.append("\tres.json(Mock.mock(\n");
            result.append("\t\t"+returnJson.toJSONString()+"\n");
            result.append("\t))\n" +
                    "})\n\n\n" );
        }

        result.append(
                "\n" +
                        "\n" +
                        "\n" +
                        "app.listen('"+port+"', () => {\n" +
                        "    console.log('监听端口 "+port+"')\n" +
                        "})\n");


        System.out.println(result.toString());

        String path = manifestID+"_node.js";
        Files.write(Paths.get(path), result.toString().getBytes());
        return path;
    }


    public static boolean isPhoneNo(String phoneNo) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(phoneNo);
        return m.matches();
    }

    public static boolean isEmail(String string) {
        if (string == null)
            return false;
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p;
        Matcher m;
        p = Pattern.compile(regEx1);
        m = p.matcher(string);
        if (m.matches())
            return true;
        else
            return false;
    }


}
