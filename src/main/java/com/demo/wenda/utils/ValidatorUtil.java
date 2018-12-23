package com.demo.wenda.utils;


import com.alibaba.druid.util.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.util.HashMap;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtil {
    private static final Logger logger = LoggerFactory.getLogger(ValidatorUtil.class);

    private static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");//以1开始，后面有10个数字

    private static final Pattern email_pattern = Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");//名称@域名

    public static boolean isMobile(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }

        Matcher m = mobile_pattern.matcher(str);
        return m.matches();
    }

    public static boolean isEmail(String str) {
        Matcher m = email_pattern.matcher(str);
        return m.matches();
    }

//    /**
//     * 验证教务处 账号密码
//     * @param txtUserName 账号
//     * @param password 密码
//     * @param txtSecretCode 验证码
//     * @param RadioButtonList1 身份
//     */
//    public static void validatorReal(String txtUserName, String password, String txtSecretCode,String RadioButtonList1){
//        try{
//            String loginUrl = "http://210.36.201.18/default2.aspx";
//
//            String __VIEWSTATE = "dDwxNTMxMDk5Mzc0Ozs+G95mRquIUMsgS3B/fvihjtgJkjM=";
//
//            //创建HTTP客户端
//            OkHttpClient client = new OkHttpClient();
//
//
//            // 模拟表单登录
//            Request request = new Request.Builder()
//                    .url(loginUrl)
//                    .post(new FormBody.Builder()
//                            .add("txtUserName", txtUserName)
//                            .add("TextBox2", password)
//                            .add("txtSecretCode", txtSecretCode)
//                            .add("__VIEWSTATE",__VIEWSTATE)
//                            .add("Textbox1", "")
//                            .add("RadioButtonList1", RadioButtonList1)
//                            .add("Button1", "")
//                            .add("lbLanguage", "")
//                            .add("hidPdrs", "")
//                            .add("hidsc", "")
//                            .build()
//                    ).build();
//
////            dDwxNTMxMDk5Mzc0Ozs+G95mRquIUMsgS3B/fvihjtgJkjM=
////            dDwxNTMxMDk5Mzc0Ozs+G95mRquIUMsgS3B/fvihjtgJkjM=
//
//            // 执行模拟登录请求
//            Response response = client.newCall(request).execute();
//
//            if (response.code() == 302){
//                logger.info("教务处isSuccessful验证成功！");
//            }else {
//                logger.error("code={},message={}",response.code(),response.message());
//            }
//
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//
//    }

    /**
     * 验证教务处 账号密码
     *
     * @param txtUserName      账号
     * @param password         密码
     * @param txtSecretCode    验证码
     * @param RadioButtonList1 身份
     */
    public static boolean validatorReal(String txtUserName, String password, String txtSecretCode, String RadioButtonList1) {
        try {
            String loginUrl = "http://210.36.201.18/default2.aspx";

            String __VIEWSTATE = "dDwxNTMxMDk5Mzc0Ozs+G95mRquIUMsgS3B/fvihjtgJkjM=";

            Connection connection = Jsoup.connect(loginUrl);
            connection.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");
//        connection.header("Cookie","ASP.NET_SessionId=rdlzoq5522m3zm45qdaodcu2; safedog-flow-item=10E36E49A887483FCAC97E88B0095FA5");

            Map<String, String> data = new HashMap<>();
            data.put("txtUserName", txtUserName);
            data.put("TextBox2", password);
            data.put("txtSecretCode", txtSecretCode);
            data.put("__VIEWSTATE", __VIEWSTATE);
            data.put("Textbox1", "");
            data.put("RadioButtonList1", RadioButtonList1);
            data.put("Button1", "");
            data.put("lbLanguage", "");
            data.put("hidPdrs", "");
            data.put("hidsc", "");

            Connection.Response response = connection.data(data).method(Connection.Method.POST).followRedirects(false).execute();

            if (response.statusCode() == 302) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


//    public static void main(String[] args) {
//        System.out.println(isEmail("177qq.com"));
//    }
}
