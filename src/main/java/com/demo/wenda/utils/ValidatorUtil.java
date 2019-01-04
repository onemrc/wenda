package com.demo.wenda.utils;


import com.alibaba.druid.util.StringUtils;


import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtil {
    private static final Logger logger = LoggerFactory.getLogger(ValidatorUtil.class);

    private static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");//以1开始，后面有10个数字

    private static final Pattern email_pattern = Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");//名称@域名

    private static final String loginUrl = "http://210.36.201.18/default2.aspx";

    private static final String checkCodeUrl = "http://210.36.201.18/CheckCode.aspx";

    private static final String successUrl = "http://210.36.201.18/xs_main.aspx?xh=";

    private static final String path = "E:\\验证码图片";


    private static Map<String, String> cookies;


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
    public static boolean validatorReal(String txtUserName, String password, String txtSecretCode, String RadioButtonList1, Map<String, String> fcookies) {
        try {
            String loginUrl = "http://210.36.201.18/default2.aspx";

            String __VIEWSTATE = getViewState();




            Connection connection = Jsoup.connect(loginUrl);

//            connection.header("Cookie", "ASP.NET_SessionId=rdlzoq5522m3zm45qdaodcu2; safedog-flow-item=10E36E49A887483FCAC97E88B0095FA5");


            Map<String, String> datas = new HashMap<>();
            datas.put("txtUserName", txtUserName);
            datas.put("TextBox2", password);
            datas.put("txtSecretCode", txtSecretCode);
            datas.put("__VIEWSTATE", __VIEWSTATE);
            datas.put("Textbox1", "");
            datas.put("RadioButtonList1", RadioButtonList1);
            datas.put("Button1", "");
            datas.put("lbLanguage", "");
            datas.put("hidPdrs", "");
            datas.put("hidsc", "");
            connection.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");

            Connection.Response response = connection.ignoreContentType(true).data(datas).cookies(fcookies).followRedirects(true).method(Connection.Method.POST).execute();

            logger.info(response.url().toString());
            if (response.url().toString().equals(successUrl + txtUserName)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取__VIEWSTATE
     *
     * @return
     */
    public static String getViewState() {
//        ASP.NET_SessionId=yorlce5524223i454oaowfid; safedog-flow-item=2519E3798185263E44EDEE752C711E83
//        ASP.NET_SessionId=yorlce5524223i454oaowfid; safedog-flow-item=2519E3798185263E44EDEE752C711E83

        Document document = null;
        try {
            Connection connection = Jsoup.connect("http://210.36.201.18/default2.aspx");
            document = connection.get();
            Element ele = document.select("input[name='__VIEWSTATE']").first();

            //随便带上cookie
            Connection.Response response = connection.execute();
            cookies = response.cookies();

            return ele.attr("value");
        } catch (IOException e) {
            logger.error("教务系统网页解析出错");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取图片验证码和设置它的cookie
     */
    public static String getCodeAndCookie() {
        //获取请求相应
        Connection.Response response = null;
        try {
            response = Jsoup.connect(checkCodeUrl).ignoreContentType(true).execute();
            //设置cookie
            cookies = response.cookies();

            //获取验证码保存在本地
            byte[] bytes = response.bodyAsBytes();
//        String pa = "E:\\验证码图片\\1.png";
            String uuid = UUIUtil.uuid();
            String imgPath = path + "\\" + uuid + ".PNG";
            FileUtil.saveFile(imgPath, bytes);
            logger.info("验证码已保存在本地");

            String localPath = uuid + ".PNG";
            return localPath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


//        ITesseract iTesseract = new Tesseract();
//        iTesseract.setDatapath("E:\\Tess4J\\tessdata");
//        iTesseract.setLanguage("eng");
//
//
//
//        try {
//            BufferedImage image = ImageIO.read(new File(imgPath));
//            String res = iTesseract.doOCR(image);
//            logger.info("识别结果：{}",res);
//        } catch (TesseractException e) {
//            e.printStackTrace();
//        }

    }

    public static Map<String, String> getCookies() {
        return cookies;
    }


//    public static void main(String[] args) {
//
//        try {
//            getCodeAndCookie();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
