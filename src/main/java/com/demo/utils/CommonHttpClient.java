package com.demo.utils;

import java.io.IOException;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class CommonHttpClient {
	
	/**
	 * 多线程http抓取请求（默认最大200  默认最多路由连接数100）
	 * @param url
	 * @return
	 */
	public static String httpClient(String url) {
	    String result = "";
	    try {
	      result = Request.Post(url)
	              .connectTimeout(1000)
	              .socketTimeout(2000)
	              .execute().returnContent().asString();

	    } catch (Exception e) {
	    	System.out.println("list 请求异常，原因："+e);
	    	result = "";
	    }
	    return result;
	  }
	
	/**
	 * 单个调用 http Post请求
	 * @param url
	 * @return
	 */
	 public static String handleHttpPostInfoSend(String url) {
			String strResp = "";
			CloseableHttpClient httpClient = HttpClients.createDefault();
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			try{
				//第一步：创建HttpClient对象
				httpClient = HttpClients.createDefault();
			 	//第二步：创建httpPost对象
		        HttpPost httpPost = new HttpPost(url);
		        //第三步：给httpPost设置JSON格式的参数
//		    	String signData = signMsg(postData);
		        StringEntity requestEntity = new StringEntity("GBK");
		        requestEntity.setContentEncoding("UTF-8");    	        
		        httpPost.setHeader("Content-type", "application/json");
		        httpPost.setEntity(requestEntity);
		       
		       //第四步：发送HttpPost请求，获取返回值
		        strResp = httpClient.execute(httpPost,responseHandler); //调接口获取返回值时，必须用此方法
		       CloseableHttpResponse httpResonse = httpClient.execute(httpPost);
		       int statusCode = httpResonse.getStatusLine().getStatusCode();
		       if(statusCode!=200){
		        	System.out.println("请求发送失败，失败的返回参数为："+httpResonse.getStatusLine());
		        	strResp = httpResonse.getStatusLine().toString();
		        	return strResp;
		       }
			}catch(Exception e){
				 e.printStackTrace();
			}finally {
		       try {
				httpClient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    }
//			System.out.println(strResp);
			 //第五步：处理返回值
//			log.debug("返回报文：" + strResp);
			return strResp;
		}
	
	

}
