package com.demo.utils;

import java.io.InputStream;
import java.net.Authenticator;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.IOUtils;
import org.springframework.util.StringUtils;


/**
 * http代理类
 * @author zhaojq
 * @since 2017-03-31
 */
public class HttpProxy {
//	private static String proxyHost = "210.22.135.106";
	private static String proxyHost = "xxx.xxxxx.com";
    private static int proxyPort = 8080;
    private static String proxyUser = "user";
    private static String proxyPass = "pass";
    
    /**
     * 通过系统变量方式代理(针对大众点评详情需要权限模拟认证
     * 的单独处理，正常请求会报401权限不足提示)
     * @param url
     * @return
     */
    public static String doProxy(String url) {
        // 设置系统变量（针对http/https开启代理）
        System.setProperty("http.proxySet", "true");
        System.getProperties().setProperty("http.proxyHost", proxyHost);
        System.getProperties().setProperty("http.proxyPort", "" + proxyPort);
        // 设置默认校验器（此处未用到模拟登陆爬取，可忽略）
        setDefaultAuthentication();
        String content = "";
        //请求处理
        try {
//        	URL u = new URL(url);
        	//固定样式，url代理请求时必设，否则会报ssl.HttpsURLConnection异常
            URL u = new URL(null,url,new sun.net.www.protocol.https.Handler());
            URLConnection conn = u.openConnection();
            //添加请求head属性，防止403错误
            String cookie ="tencentSig=8408631296; _hc.v=3d1711b2-e26d-8024-dd96-af30d719aadf.1490606400; CNZZDATA1260865305=1390601945-1490766027-http%253A%252F%252Fwww.dianping.com%252F%7C1493786740; CNZZDATA1260869652=196498171-1490766509-http%253A%252F%252Fwww.dianping.com%252F%7C1493786430; CNZZDATA1260952106=1163906161-1490767209-http%253A%252F%252Fwww.dianping.com%252F%7C1493789547; __utma=1.1612998622.1491795497.1493719896.1494301466.5; __utmc=1; __utmz=1.1494301466.5.3.utmcsr=graph.qq.com|utmccn=(referral)|utmcmd=referral|utmcct=/oauth/show; dper=bb15ef57ce2fa98144949fc11b496ed730fcf12d359fde6b3d452cfbfda25225; ua=18621513463; PHOENIX_ID=0a01084a-15bebc4a8a9-5599d27; ll=7fd06e815b796be3df069dec7836c3df; s_ViewType=10; JSESSIONID=E139A67B9DB331F14ACD12F066791C6A; aburl=1; cy=1; cye=shanghai";
//            conn.setRequestProperty("Accept", "text/html");
//            conn.setRequestProperty("Accept-Charset", "utf-8");
//         	conn.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
//         	conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
         	conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36");
         	conn.setRequestProperty("Cookie", cookie);
//            conn.setRequestProperty("Host", "www.dianping.com");
//            conn.setRequestProperty("User-Agent",
//                    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.160 Safari/537.22");
            
//            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.2)");
//            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
//            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)");
            
//            	conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36");
////            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");

     /*       conn.setDoInput(true);  
            conn.setDoOutput(true); */
//            conn.setUseCaches(false);//POST请求不使用缓存
            HttpsURLConnection httpsCon = (HttpsURLConnection) conn;
            httpsCon.setRequestMethod("GET");
            System.out.println(httpsCon.getResponseCode());
            httpsCon.setFollowRedirects(true);
            if (httpsCon.getResponseCode() == HttpsURLConnection.HTTP_OK ) {
                String encoding = conn.getContentEncoding();
                if (StringUtils.isEmpty(encoding)) {
                	encoding = "UTF-8";
                }
                InputStream is = conn.getInputStream();
                content = IOUtils.toString(is, encoding);
                
         /*       String result = "";
                BufferedReader in = null;
                in = new BufferedReader(new InputStreamReader(
                		conn.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    result += line;
                }
                System.out.println("result =="+result);*/
                
            } 
        } catch (Exception e) {
        	System.out.println("详情请求异常，原因："+e.getMessage());
            e.printStackTrace();
            return "";
        }
		return !StringUtils.isEmpty(content) ? content : "";
    }
 
    /**
     * 设置全局校验器对象
     */
    public static void setDefaultAuthentication() {
        BasicAuthenticator auth = new BasicAuthenticator(proxyUser, proxyPass);
        Authenticator.setDefault(auth);
    }
    
}
