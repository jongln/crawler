package com.demo.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.demo.entity.CommonModel;
import com.demo.entity.NewsCommonModel;

public class CommonUtils {
	
    /**
     * 根据图片url保存到本地
     * @param urlString url地址
     * @param filename 图片名称
     * @param savePath 保存本地路径
     */
	public static String saveImgByURL(String urlString,String savePath){
		StringBuffer sb = new StringBuffer();
		try {
			String[] sourceStrArray = urlString.split(",");
			for (String imgUrl : sourceStrArray) {
				// 构造URL打开连接
				URL url = new URL(imgUrl);
				URLConnection con = url.openConnection();
				con.setConnectTimeout(5*1000);
				InputStream is = con.getInputStream();
				byte[] bs = new byte[1024];
				int len;
				File sf=new File(savePath);
				if(!sf.exists()){
					sf.mkdirs();
				}
				String name = getRandom()+imgUrl.substring(imgUrl.length()-4,imgUrl.length());
				OutputStream os;
				os = new FileOutputStream(sf.getPath()+"/"+name);
				while ((len = is.read(bs)) != -1) {
					os.write(bs, 0, len);
				}
				sb.append(savePath+name).append(",");
				// 完毕，关闭所有链接
				os.close();
				is.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
		}
	
	
     /**
      * 随机数生成(作为图片名称)
      * @return
      */
	public static String getRandom(){
		Random r = new Random();
		long num = Math.abs(r.nextLong() % 10000000000L);
		String s = String.valueOf(num);
		for(int i = 0; i < 10-s.length(); i++){
			s = "0" + s;
		}
		return s;
	}
	
	/**
	 * POI结合JS文件解密
	 * @param params
	 * @return
	 */
	public static Map javaScriptDatadecode(String params){
		ScriptEngineManager manager = new ScriptEngineManager();  
        ScriptEngine engine = manager.getEngineByName( "javascript");
        String jsFileName = "src/main/webapp/decode.js";   // 读取js文件   

        FileReader reader;
        Map<String,Object>  retMap = new HashMap<String,Object>();
		try {
			reader = new FileReader(jsFileName);
			engine.eval(reader);
			if(engine instanceof Invocable) {    
				Invocable invoke = (Invocable)engine;    // 调用merge方法，并传入两个参数    
				retMap = (Map)invoke.invokeFunction("decode", params);    
//				retMap = (Map)invoke.invokeFunction("decode", "HHDFJGZVVIHIJG");    
				reader.close();  
			}   
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retMap;         
	}
	
	
	/**
	 * 请求参数进行截取处理
	 * @param str
	 * @return
	 */
	public static String paramsHandle(String str){
		str = str.substring(str.indexOf("&")+1,str.length());
		str = str.substring(str.indexOf("=")+1,str.length());
		str = str.replaceAll("%26", "&");
		return str;
	}
}
