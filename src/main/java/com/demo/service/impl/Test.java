package com.demo.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonNode;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import us.codecraft.webmagic.selector.JsonPathSelector;

import com.demo.entity.CommonModel;
import com.demo.utils.CommonHttpClient;
import com.demo.utils.HttpProxy;
import com.demo.utils.JsonUtils;
import com.demo.utils.JsoupHtmlResolver;
import com.demo.utils.ListUrlType;
import com.fasterxml.jackson.core.JsonParseException;  
import com.fasterxml.jackson.databind.DeserializationFeature;  
import com.fasterxml.jackson.databind.JsonMappingException;  
import com.fasterxml.jackson.databind.ObjectMapper;  
public class Test {

	public static String stampToDate(long lt){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
	
	public static String FOOD_DETAIL_URL="http://www.dianping.com/ajax/json/shopDynamic/shopTabs?"
			+ "shopId=SHOP_ID&cityId=CITY_ID&shopName=&power=5&mainCategoryId=MAIN_CATEGORY_ID&shopType=10&shopCityId=CITY_ID";
	
	
	/*public Map<String, String> toMap(Object object) {
	    Map<String, String> data = new HashMap<String, String>();
	    // 将json字符串转换成jsonObject
	    JSONObject jsonObject = JSONObject.(object);
	    Iterator ite = jsonObject.keys();
	    // 遍历jsonObject数据,添加到Map对象
	    while (ite.hasNext()) {
	        String key = ite.next().toString();
	        String value = jsonObject.get(key).toString();
	        data.put(key, value);
	    }
	    // 或者直接将 jsonObject赋值给Map
	    // data = jsonObject;
	    return data;
	}*/
	
	  public static Map<String, Object> dianpingDetailHtmlResolver(ListUrlType urlType,String strHtml) {
		 /* if(urlType == ListUrlType.MARRIED){//结婚团购（动态请求获取）
				Document doc = Jsoup.parse(strHtml);
			  System.out.println(doc);
			  if(doc.getElementsByAttributeValueMatching("id", "J_boxPromo").hasText()){
				  Elements e_detail  = doc.getElementsByClass("J_boxPromo");
  				Elements elements = doc.getElementsByTag("li");
  				for (Element element : elements) {
  					String name = element.getElementsByTag("p").text();
  					Element _element = element.getElementsByTag("span").get(0);
  					String title = element.getElementsByTag("h3").attr("title").replace("\"","").trim();;
  					String details = element.toString();
  					System.out.println(title);
  					System.out.println(details);
  					String detail = "";
  					if(element.toString().contains("img")){
							detail = element.select("img").attr("src");//图片
						}
  				
  				}
				  
			  }
			  
			  
			  
			  
			  
			  
			  
				if(doc.getElementsByAttributeValueMatching("class", "J_table").hasText()){
    				Element e_detail  = doc.getElementsByClass("J_table").get(0);
    				Elements elements = e_detail.getElementsByTag("tr");
    				
    				for (Element element : elements) {
    					Elements _elements = element.getElementsByTag("td");
    					StringBuffer sb_temp = new StringBuffer();
    					for (int i = 1; i < _elements.size()-1; i++) {
							sb_temp.append(_elements.get(i).text()).append(",");
						}
    					if(sb_temp.length() > 0){
    						String names = sb_temp.deleteCharAt(sb_temp.length() -1).substring(0,sb_temp.indexOf(","));
    						if(names.indexOf(" ") > -1){
    							names = names.substring(0, names.indexOf(" "));
    						}
    						String detail = sb_temp.substring(sb_temp.indexOf(",")+1);
    						String price = sb_temp.substring(sb_temp.lastIndexOf(",")+1);
//    						String prices = StringUtils.stripEnd(sb_temp.deleteCharAt(sb_temp.length() -1).toString(),  "/");
    						System.out.println(names);
    						System.out.println(detail);
    						System.out.println(price);
    					
    					}
					}
    			}
			  
			  
			  
			  
			  
			  
		  }*/
		  
		  
		  
		  
		  
		  
		  
		  
		  Document doc = Jsoup.parse(strHtml);
		/*  if(doc.getElementsByAttributeValueMatching("class", "mod sales-promotion clearfix").hasText()){
				Element e_detail  = doc.getElementById("sales");
				Elements elements = e_detail.getElementsByClass("group");
				for (Element element : elements) {
					if(element.toString().contains("item big")){
						Elements _e_detail  = element.getElementsByClass("item");
						for (Element _element : _e_detail) {
							String name = _element.getElementsByTag("p").text();
							String price = _element.getElementsByClass("price").text();
							String detail = _element.select("img").attr("src");//图片
							
							System.out.println("name1=="+name);
							System.out.println("price1=="+price);
							System.out.println("detail1=="+detail);
						}
						
					}else if(element.toString().contains("item small") && !element.getElementsByAttributeValueMatching("id", "vcbooking").hasText()){
						Element s_e_detail  = element.getElementsByTag("div").get(0);
						Elements s_elements = s_e_detail.getElementsByTag("a");
						for (Element _element : s_elements) {
							String name = _element.ownText();
							String price = _element.getElementsByClass("price").text();
							String detail = null;
							if(_element.toString().contains("pic")){
								detail = _element.select("img").attr("src");//图片
							}
							System.out.println("name2=="+name);
							System.out.println("price2=="+price);
							System.out.println("detail2=="+detail);
						}
					}else if(element.getElementsByAttributeValueMatching("class", "group clearfix").hasText()){
						Element s_e_detail  = element.getElementsByTag("div").get(0);
						Elements s_elements = s_e_detail.getElementsByTag("a");
						for (Element _element : s_elements) {
							String name = _element.ownText();
							String price = _element.getElementsByClass("price").text();
							String detail = null;
							if(_element.toString().contains("pic")){
								detail = _element.select("img").attr("src");//图片
							}
							System.out.println("name3=="+name);
							System.out.println("price3=="+price);
							System.out.println("detail3=="+detail);
						}
						
					}else{
						System.out.println("商品类型"+urlType+"发现新的未解析页面");
					}
		  
		  
		  
				}
		  }*/
		  
		  
		  Element element  = doc.getElementsByClass("shop-list").get(0);
  		Elements elementList = element.getElementsByTag("li");
  		for (Element _element : elementList) {
  			String title = _element.select("a").attr("title");//店铺名称
  			String detailUrl = "www.dianping.com"+_element.select("a").attr("href");//详情url
  			
  			String picture = "";
  			if(org.apache.commons.lang.StringUtils.isNotBlank(_element.select("img").attr("src"))){
  				picture = "http:"+_element.select("img").attr("src");//图片
  			}
  			if(org.apache.commons.lang.StringUtils.isNotBlank(_element.select("img").attr("data-lazyload"))){
  				picture = "http:"+_element.select("img").attr("data-lazyload");//图片
  				
  			}
  			String shopId = detailUrl.substring(detailUrl.indexOf("shop")+5);
  			if(shopId.indexOf("?") != -1){
  				shopId = shopId.substring(0, shopId.indexOf("?"));
  			}
  			System.out.println(title);
  			System.out.println(picture);
  			
			}
		  
		  
		return null;
		  
	  }
	
	   public static void main(String[] args) throws IOException {
		 
		   
	    	
//	        String url = "http://www.dianping.com/beijing/hotel/p1n10";
//	        String url = "http://www.dianping.com/search/category/160/70/p1";
//	        String url = "http://www.dianping.com/search/category/160/55/p1";
//	        String url = "http://www.dianping.com/search/category/160/90/g90p1";
//	        String url = "http://www.dianping.com/shop/27112393";
//	        String url = "http://www.dianping.com/shop/67110526";
//	        String url = "http://www.dianping.com/shop/59262703";
//	        String url = "http://www.dianping.com/shop/22071988";
//	        String url = "http://www.dianping.com/shop/59262703";
//	        String url = "http://www.dianping.com/shop/5413997";
//	        String url = "http://www.dianping.com/shop/5424124";
//	        String url = "http://www.dianping.com/search/category/2/55/p1";
//	        String url = "http://www.dianping.com/shop/2899522";
//	        String url = "http://www.dianping.com/newhotel/2533144";
//	        String url = "http://www.dianping.com/shop/38121309";
//	        String url = "http://www.dianping.com/shop/37801367";
//	        String url = "http://www.dianping.com/shop/9340301";
//	        String url = "http://www.dianping.com/shop/16688151";
//	        String url = "http://www.dianping.com/shop/56619441";
//	        String url = "http://www.dianping.com/shop/17884343";
//	        String url = "http://www.dianping.com/shop/66855251";
//	        String url = "http://www.dianping.com/shop/17173183";
//	        String url = "http://www.dianping.com/shop/6297501";
//	        String url = "http://www.dianping.com/shop/64058836";
//	        String url = "http://www.dianping.com/shop/64677951";
//	        String url = "http://www.dianping.com/search/category/1/70";
//	        String url = "http://www.dianping.com/search/category/887/40?pageNo=1";
//	        String url = "http://www.dianping.com/search/category/887/25/p1";
//	        String url = "http://www.dianping.com/shop/49407256";
//	        String url = "http://www.dianping.com/shop/5327835";
//	        String url = "http://www.dianping.com/shop/16688151";
//	        String url = "http://www.dianping.com/shop/27427272";
//	        String url = "http://www.dianping.com/shop/5054668";
//	        String url = "http://www.dianping.com/shop/5383697";
//	        String url = "http://www.dianping.com/shop/27382090";
//	        String url = "http://www.dianping.com/shop/16789967";
//	        String url = "http://www.dianping.com/shop/2901106";
//	        String url = "http://www.dianping.com/shop/68927737";
//	        String url = "http://www.dianping.com/ajax/json/shopDynamic/promoInfo?shopId=69081432&cityId=1&shopName=%E8%BF%AA%E5%A3%AB%E5%B0%BC%E7%99%BE%E8%80%81%E6%B1%87%E3%80%8A%E7%8B%AE%E5%AD%90%E7%8E%8B%E3%80%8B%E9%9F%B3%E4%B9%90%E5%89%A7&power=5&mainCategoryId=33832&shopType=35";
//	       String url = "http://www.dianping.com/shop/8003507"; 
//		   String url = "http://www.dianping.com/hotelproduct/pc/hotelPrepayAndOtaGoodsList?shopId=3566212"; 
//		   String url = "http://www.dianping.com/search/category/1/55/p1"; 
//		   String url = "http://www.dianping.com/shop/8902301?ad_sid=afd99863-039d-4c90-a24d-2c31cc342719&launchid=22148143&_fb_=rmp%3D31MS7fT_n03ktu4bX6J5FxChFgYLjeYfjTMHi5diVN5soViRAqTRRXkqauY%26adshop_id%3D8902301%26entityid%3D8902301%26entityplat%3D1%26target_id%3D4404642%26request_id%3Dafd99863-039d-4c90-a24d-2c31cc342719%26productid%3D74%26ad%3D22148143%26sver%3D2%26slot%3D8%26entitytype%3D1"; 
//		   String url = "http://www.dianping.com/shanghai/hotel/p1n10"; 
//		   String url = "http://www.dianping.com/search/category/1/40?pageNo=1"; 
//		   String url = "http://www.dianping.com/shop/4668211"; 
//		   String url = "http://www.dianping.com/ajax/shop/wedding/promo?actionType=get&shopId=69466337"; 
//		   String url = "http://www.dianping.com/shop/3348505"; 
//		   String url = "http://www.dianping.com/shop/67438505"; 
//		   String url = "http://www.dianping.com/shop/3076987"; 
//		   String url = "http://www.dianping.com/shop/1773421/wedding/product/1016864"; 
//		   String url = "http://www.dianping.com/shop/3076987"; 
//		   String url = "http://www.dianping.com/shop/20917038"; 
//		   String url = "http://www.dianping.com/ajax/json/shopDynamic/shopTabs?shopId=67330730&cityId=1&shopName=LUNETTE+BY+AMANDA&power=5&mainCategoryId=231&shopType=10&shopCityId=1"; 
//		   String url = "http://www.dianping.com/ajax/json/shopDynamic/promoInfo?shopId=67330730&cityId=1&shopName=LUNETTE+BY+AMANDA&power=5&mainCategoryId=231&shopType=10"; 
//		   String url = "http://www.dianping.com/ajax/json/shopDynamic/promoInfo?shopId=22912244&cityId=1&shopName=%E4%B8%87%E5%B2%9B%E6%97%A5%E6%9C%AC%E6%96%99%E7%90%86%E9%93%81%E6%9D%BF%E7%83%A7&power=5&mainCategoryId=225&shopType=10"; //美食
//		   String url = "http://www.dianping.com/ajax/json/shopDynamic/shopTabs?shopId=45831427&cityId=1&shopName=%E5%8D%A2%E7%B1%B3%E5%9F%83%E5%BD%B1%E5%9F%8E&power=5&mainCategoryId=136&shopType=25&shopCityId=1"; //电影
//		   String url = "http://www.dianping.com/ajax/json/shopDynamic/promoInfo?shopId=55834438&cityId=1&shopName=%E9%9B%86%E5%90%88%E7%9F%B3%E5%A4%A7%E5%9E%8B%E7%9C%9F%E4%BA%BA%E6%B8%B8%E6%88%8F%E9%A6%86&power=5&mainCategoryId=2754&shopType=30"; //休闲
//		   String url = "http://www.dianping.com/newhotel/2425123"; //休闲
//		   String url = "http://www.dianping.com/shop/68163551"; //休闲
//		   String url = "http://www.dianping.com/shop/20917038"; //家装
//		   String url = "http://www.dianping.com/shop/73559560"; //家装
//		   String url = "http://www.dianping.com/shop/2901106"; //qinzi
//		   String url = "http://www.dianping.com/shop/67906222"; //qinzi diy
//		   String url = "http://www.dianping.com/shop/67799051"; //peixun
//		   String url = "http://www.dianping.com/shop/67799051"; //peixun
//		   String url = "http://www.dianping.com/ajax/shop/wedding/promo?_nr_force=1495629462249&actionType=get&shopId=11547505"; //peixun
//	       String url = "http://www.dianping.com/shop/17179585";
//	       String url = "http://www.dianping.com/ajax/json/shopDynamic/shopTabs?shopId=45831427&cityId=1&shopName=%E5%8D%A2%E7%B1%B3%E5%9F%83%E5%BD%B1%E5%9F%8E&power=5&mainCategoryId=136&shopType=25&shopCityId=1";
//		   String content = CommonHttpClient.handleHttpPostInfoSend(url);
//		   String url  = "http://www.dianping.com/shop/65555855";
		   String url  = "http://www.dianping.com/search/category/1/70";
		   String content = HttpProxy.doProxy(url);
		   if(StringUtils.isNotBlank(content)){//获取美食菜单信息
			   
			   Map<String, Object> retMap  = dianpingDetailHtmlResolver(ListUrlType.MARRIED,content);
			   
			   
			   
			   /*JsonNode nodes_movie = JsonUtils.parse(content);
				if(nodes_movie.size() > 0){
					for (JsonNode node : nodes_movie.findParents("movieName")) {
						StringBuffer sb_details = new StringBuffer();//商品详情
						String name = node.get("movieName").toString().replace("\"","").trim();
						String ff = node.get("minPrice").toString();
						System.out.println(null == ff || "null".equals(ff));
					if( null != node.get("minPrice") && !"null".equals(node.get("minPrice").toString())){
						String price = node.get("minPrice").toString().replace("\"","").trim();
						System.out.println("price111=="+price);
						}
						//影片内容
						Date date = new Date(Long.valueOf(node.get("showDate").toString()));
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						
						for (JsonNode picture : node.get("movieImage")) {//图片
							System.out.println(null != picture.getTextValue());
							System.out.println(picture.toString().indexOf("{}") > -1);
							if(null != picture.getTextValue()){
								sb_details.append("<dl>").append(picture.toString()).append("</dl>");
							}
						}
						sb_details.append("<dl>")
						.append("<dt>").append("导演：").append("</dt>")
						.append("<dd>").append(node.get("director")).append("</dd>")
						.append("<dt>").append("语言：").append("</dt>")
						.append("<dd>").append(node.get("language")).append("</dd>")
						.append("<dt>").append("类型：").append("</dt>")
						.append("<dd>").append(node.get("sort")).append("</dd>")
						.append("<dt>").append("上映时间：").append("</dt>")
						.append("<dd>").append(sdf.format(date)).append("</dd>")
						.append("<dt>").append("主演：").append("</dt>")
						.append("<dd>").append(node.get("mainPerformer")).append("</dd>")
						.append("<dt>").append("内容简介：").append("</dt>")
						.append("<dd>").append(node.get("movieDes")).append("</dd>")
						.append("</dl>");
					
						System.out.println(name);
						
						System.out.println(sb_details);
					}
				}*/
			   
			   
			   
			  /* JsonNode nodes_goodsList = JsonUtils.parse(content);
			   if(nodes_goodsList.size() > 0){
	  				for (int i = 0; i < nodes_goodsList.findParents("goodsList").size(); i++) {
	  					List<JsonNode> goodsDetailInfo = nodes_goodsList.findParents("goodsList").get(i).findParents("goodsIdL");
	  					for (int j = 0; j < goodsDetailInfo.size(); j++) {
	  						String name = goodsDetailInfo.get(j).get("title").toString().replace("\"","").trim();
	  						String price = "";
	  						if(null != goodsDetailInfo.get(j).get("price")){
	  							price = goodsDetailInfo.get(j).get("price").toString().replace("\"","").trim();
	  						}
//	  						sbPictrues.append(goodsDetailInfo.get(j).get("imageList").toString().replaceAll("[\\[\\]]", "")).append(";");
							List<JsonNode> detailsList =  goodsDetailInfo.get(j).findParents("name");
							String detail = null;
							StringBuffer sb_details = new StringBuffer();
							for (int k = 0; k < detailsList.size(); k++) {
								sb_details.append(detailsList.get(k).get("name")).append(":")
								.append(detailsList.get(k).get("value")).append(",");
								detail = sb_details.toString().replace("\"","").trim();
							}
							System.out.println(name);
							System.out.println(price);
							System.out.println(detail);
						}
	  				}
	  			}*/
			     
			}
		 /*  CloseableHttpClient httpclient = HttpClients.createDefault();  
	          
	        try {  
	        	String cookie ="tencentSig=8408631296; _hc.v=3d1711b2-e26d-8024-dd96-af30d719aadf.1490606400; CNZZDATA1260865305=1390601945-1490766027-http%253A%252F%252Fwww.dianping.com%252F%7C1493786740; CNZZDATA1260869652=196498171-1490766509-http%253A%252F%252Fwww.dianping.com%252F%7C1493786430; CNZZDATA1260952106=1163906161-1490767209-http%253A%252F%252Fwww.dianping.com%252F%7C1493789547; __utma=1.1612998622.1491795497.1493719896.1494301466.5; __utmc=1; __utmz=1.1494301466.5.3.utmcsr=graph.qq.com|utmccn=(referral)|utmcmd=referral|utmcct=/oauth/show; dper=bb15ef57ce2fa98144949fc11b496ed730fcf12d359fde6b3d452cfbfda25225; ua=18621513463; PHOENIX_ID=0a01084a-15bebc4a8a9-5599d27; ll=7fd06e815b796be3df069dec7836c3df; s_ViewType=10; JSESSIONID=E139A67B9DB331F14ACD12F066791C6A; aburl=1; cy=1; cye=shanghai";
	            HttpGet httpget = new HttpGet(url);  
	            httpget.addHeader("Accept", "text/html");  
	        httpget.addHeader("Accept-Charset", "utf-8");  
	            httpget.addHeader("Accept-Encoding", "gzip");  
	        httpget.addHeader("Accept-Language", "en-US,en");  
	        httpget.addHeader("User-Agent",  
	            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.160 Safari/537.22");  
	        httpget.addHeader("Cookie", cookie);  
	            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {  
	   
	                public String handleResponse(  
	                        final HttpResponse response) throws ClientProtocolException, IOException {  
	                    int status = response.getStatusLine().getStatusCode();  
	                    if (status >= 200 && status < 300) {  
	                        HttpEntity entity = response.getEntity();  
	                        System.out.println(status);  
	                        return entity != null ? EntityUtils.toString(entity) : null;  
	                    } else {  
	                        System.out.println(status);  
	                        Date date=new Date();  
	                        System.out.println(date);  
	                        System.exit(0);  
	                        throw new ClientProtocolException("Unexpected response status: " + status);  
	                    }  
	                }  
	            };  
	            String responseBody = httpclient.execute(httpget, responseHandler);  
	            System.out.println(responseBody);
	        } finally {  
	            httpclient.close();  
	        }  */
		   
		   
		   
		   
		  /* System.out.println(content);
//		   String content1 = CommonHttpClient.httpClient(url);
	       int cityId = JsoupHtmlResolver.dianpingScriptCityCodeHtmlResolver(content);
	       System.out.println(cityId);*/
	       
	       
	       
	       
	      /* if(StringUtils.isNotBlank(content)){//获取美食相关团购信息
	    	   StringBuffer sb_names = new StringBuffer();//商品名称
				StringBuffer sb_prices = new StringBuffer();//商品价格
				StringBuffer sb_details = new StringBuffer();//商品详情
				Map<String, Object> maps = JsonUtils.parseMap(content);//JSON解析转map(按节点)
				String parentsNodes = JsonUtils.toJson(maps.get("promoList"));// map转JSON解析
				JsonNode nodes = JsonUtils.parse(parentsNodes);//json解析
				if(nodes.findParents("desc").size() > 0){
					for (JsonNode node : nodes.findParents("desc")) {
						sb_names.append(node.get("desc")).append(";");
						sb_prices.append(node.get("price")).append(";");
						sb_details.append(node.get("extInfo").get("content")).append(";");
					}
				}
				System.out.println(sb_names.toString().replace("\"","").trim());
				System.out.println(sb_prices.toString().replace("\"","").trim());
				System.out.println(sb_details.toString().replace("\"","").trim());
			}*/
	       
	       
	       /*if(StringUtils.isNotBlank(content)){
	    	   JsonNode nodes = JsonUtils.parse(content);
				StringBuffer sb_2 = new StringBuffer();
				System.out.println(nodes.findParents("dealDetails").size());
				if(nodes.findParents("dealDetails").size() > 0){
					for (int i = 0; i < nodes.findParents("productTitle").size(); i++) {
						Object shortTitle = nodes.findParents("productTitle").get(i).get("productTitle");
						Object titleDesc = nodes.findParents("titleDesc").get(i).get("titleDesc");
						sb_2.append(shortTitle.toString().replace("\"","").trim())
						.append("[").append(titleDesc.toString().replace("\"","").trim()).append("]")
						.append(";");
					}
				}
				if(nodes.findParents("dealMoreDetails").size() > 0){//更多套餐信息
					for (int j = 0; j < nodes.findParents("desc").size(); j++) {
						Object titleDesc = nodes.findParents("desc").get(j).get("desc");
						sb_2.append(titleDesc.toString().replace("\"","").trim()).append(";");
					}
				}
				
				System.out.println(sb_2);
			}*/
	       
	       
	       
	       
	       
	      /* if(StringUtils.isNotBlank(content)){
	    	   StringBuffer sb_names = new StringBuffer();
				StringBuffer sb_prices = new StringBuffer();
				StringBuffer sb_details = new StringBuffer();
				StringBuffer sb_pictures = new StringBuffer();
				sb_names.append("[");
				sb_prices.append("[");
				sb_details.append("[");
				
				
//	    	   Map<String, Object> maps = JsonUtils.parseMap(content);//JSON解析转map(按节点)
//		       String allDishes = maps.get("allDishes").toString().replaceAll("[\\[\\]]", "");//获取该节点下所有集合
//		       String parentsNodes = JsonUtils.toJson(maps.get("movielist"));// map转JSON解析
//		       JsonNode _nodes = JsonUtils.parse(parentsNodes);//json解析
				JsonNode nodes = JsonUtils.parse(content);
				System.out.println(nodes.findParents("movieName").size());
				for (JsonNode node : nodes.findParents("movieName")) {
					sb_names.append(node.get("movieName")).append(";");//名称
					sb_prices.append(node.get("minPrice")).append(";");//价格
					//影片内容
					Date date = new Date(Long.valueOf(node.get("showDate").toString()));
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					sb_details.append("导演：").append( node.get("director")).append("|")
					.append("语言：").append(node.get("language")).append("|")
					.append("类型：").append(node.get("sort")).append("|")
					.append("主演：").append(node.get("mainPerformer")).append("|")
					.append("上映时间：").append(sdf.format(date)).append("|")
					.append("内容简介：").append(node.get("movieDes")).append(";");
					
					for (JsonNode picture : node.get("movieImage")) {
						System.out.println(null != picture.getTextValue());
						System.out.println(picture.toString().indexOf("{}") > -1);
						if(null != picture.getTextValue()){
							sb_pictures.append(picture).append("|");
							System.out.println(picture);
						}
					}
					sb_pictures.append(";");
				}
			
				System.out.println(sb_names);
				System.out.println(sb_prices);
				System.out.println(sb_details);
				System.out.println(sb_pictures);
	       }*/
	       
	       
	       
	       
	       
	       
	       
	       
	       
	       
	    /*   Map<String, Object> maps = JsonUtils.parseMap(content);//JSON解析转map(按节点)
//	       String allDishes = maps.get("allDishes").toString().replaceAll("[\\[\\]]", "");//获取该节点下所有集合
	       String parentsNodes = JsonUtils.toJson(maps.get("allDishes"));// map转JSON解析
	       JsonNode nodes = JsonUtils.parse(parentsNodes);//json解析
	       StringBuffer sb_names = new StringBuffer();
			StringBuffer sb_prices = new StringBuffer();
			StringBuffer sb_details = new StringBuffer();
			sb_names.append("[");
			sb_prices.append("[");
			sb_details.append("[");
			for (JsonNode jsonNode : nodes.findParents("dishTagName")) {
				sb_names.append(jsonNode.get("dishTagName").toString().replace("\"","").trim()).append(";");//名称
				if(null == jsonNode.get("finalPrice")){//价格
					sb_prices.append(jsonNode.get("finalPrice")).append(";");
				}else{
					sb_prices.append(jsonNode.get("finalPrice").toString().replace("\"","").trim()).append(";");
				}
				if(null == jsonNode.get("defaultPicURL")){//图片放入详情中
					sb_details.append(jsonNode.get("defaultPicURL")).append(";");
				}else{
					sb_details.append(jsonNode.get("defaultPicURL").toString().replace("\"","").trim()).append(";");
				}
				System.out.println(sb_names);
				System.out.println(sb_prices);
				System.out.println(sb_details);
			}
			sb_names.append("]");
			sb_prices.append("]");
			sb_details.append("]");*/
	       
	       
	       
	       /*if(StringUtils.isNotBlank(content)){
	    	   StringBuffer sb_names = new StringBuffer();
				StringBuffer sb_prices = new StringBuffer();
				StringBuffer sb_details = new StringBuffer();
				sb_names.append("[");
				sb_prices.append("[");
				sb_details.append("[");
	    	   Map<String, Object> maps = JsonUtils.parseMap(content);//JSON解析转map(按节点)
//		       String allDishes = maps.get("allDishes").toString().replaceAll("[\\[\\]]", "");//获取该节点下所有集合
		       String parentsNodes = JsonUtils.toJson(maps.get("dealDetails"));// map转JSON解析
		       JsonNode nodes = JsonUtils.parse(parentsNodes);//json解析
				StringBuffer sb_2 = new StringBuffer();
				System.out.println(nodes.findParents("productTitle").size());
				for (JsonNode node : nodes.findParents("productTitle")) {
					Object title = node.get("productTitle");
					Object prices = node.get("price");
					Object detail = node.get("titleDesc");
					sb_names.append(title).append(";");
					sb_prices.append(prices).append(";");
					sb_details.append(detail).append(";");
				}
				 String moreNodes = JsonUtils.toJson(maps.get("dealMoreDetails"));// map转JSON解析
			     JsonNode nodes2 = JsonUtils.parse(moreNodes);//json解析
			     for (JsonNode jsonNode : nodes2) {
			    	 	sb_names.append("更多团购优惠").append(";");
						sb_prices.append(jsonNode.get("price")).append(";");
						sb_details.append(jsonNode.get("desc")).append(";");
				}
			     sb_names.append("]");
					sb_prices.append("]");
					sb_details.append("]");
	       }*/
	       
	       
	       
	       
	       
	       
	       
	       
	       
//	       JsonUtils.toJson(maps.get("allDishes").toString().replaceAll("[\\[\\]]", ""));
	       
//	       JsonNode nodes = JsonUtils.parse(allDishes);
//	       System.out.println(nodes.size());
	     /*  String []  arryStr = nodes.get("allDishes").toString().split("},");
	       System.out.println(arryStr.length);*/
//			System.out.println(nodes.findParent("allDishes").size());
		
			/*for (int i = 0; i < nodes.findParents("allDishes").size(); i++) {
				Object node = nodes.findParents("allDishes").get(i).get("allDishes");
				sb.append(node.toString().replace("\"","").trim()).append(",");
			}*/
//			System.out.println(sb.toString());
	       /* JsonNode nodes = JsonUtils.parse(content);
	        String details = nodes.get("msg").toString();
	        System.out.println(details);*/
		/*	StringBuffer sb = new StringBuffer();
			for (int i = 0; i < nodes.findParents("msg").size(); i++) {
				Object shortTitle = nodes.findParents("desc").get(i).get("desc");
				sb.append(shortTitle.toString().replace("\"","").trim())
				.append(";");
			}*/
	       
//	       JsoupHtmlResolver.dianpingDetailHtmlResolver(ListUrlType.HOME, content);
	      /* CommonModel commonModel = new CommonModel();
	       commonModel.setUrlType(ListUrlType.MARRIED);
	       commonModel.setStrHtml(content);
	       JsoupHtmlResolver.dianpingHtmlResolver(commonModel);*/
	       
	       /* JsonNode nodes = JsonUtils.parse(content);
  			StringBuffer sb_names = new StringBuffer();
  			StringBuffer sb_prices = new StringBuffer();
  			StringBuffer sb_details = new StringBuffer();
  			StringBuffer sbPictrues = new StringBuffer();
  			System.out.println(nodes.findParents("goodsList").size());
  			if(nodes.findParents("goodsList").size() > 0){
  				for (int i = 0; i < nodes.findParents("goodsList").size(); i++) {
  					System.out.println(nodes.findParents("goodsList").get(i).findParents("goodsIdL").size());
  					List<JsonNode> goodsDetailInfo = nodes.findParents("goodsList").get(i).findParents("goodsIdL");
  					for (int j = 0; j < goodsDetailInfo.size(); j++) {
  						sbPictrues.append(goodsDetailInfo.get(j).get("imageList").toString().replaceAll("[\\[\\]]", "")).append(";");
  						sb_names.append(goodsDetailInfo.get(j).get("title")).append(";");
  						sb_prices.append(goodsDetailInfo.get(j).get("price")).append(";");
						List<JsonNode> listNames =  goodsDetailInfo.get(j).findParents("name");
						for (int k = 0; k < listNames.size(); k++) {
							sb_details.append(listNames.get(k).get("name")).append(":")
							.append(listNames.get(k).get("value")).append(",");
						}
						sb_details.deleteCharAt(sb_details.length() - 1).append(";");
					}
  					
  					System.out.println(sb_names.deleteCharAt(sb_names.length() - 1));
  					System.out.println(sb_prices.deleteCharAt(sb_prices.length() - 1));
  					System.out.println(sb_details.deleteCharAt(sb_details.length() - 1));
  				}
  			}*/
//			System.out.println(sbPictrues.toString());
	       
	       
	       
//	       String content= CommonHttpClient.handleHttpPostInfoSend(url);
	    /*   String content = HttpProxy.doProxy(url);
	       Map retMap = JsoupHtmlResolver.dianpingDetailHtmlResolver(ListUrlType.HOME, content);
	       CommonModel commonModel = new CommonModel();
	       commonModel.setGoodsNames(retMap.containsKey("goodsName")?String.valueOf(retMap.get("goodsName")):null);*/
	       
//	       System.out.println(commonModel.getGoodsNames());
	       /* String content = HttpProxy.doProxy(url);
	        JsoupHtmlResolver.dianpingDetailHtmlResolver(ListUrlType.TRAIN, content);*/
//	        int count = JsoupHtmlResolver.totalCountHtmlResolver(content);
//	        System.out.println(count);
//	       String strUrl = FOOD_DETAIL_URL.replaceAll("SHOP_ID", "18615887").replace("MAIN_CATEGORY_ID", "116").replaceAll("CITY_ID", "1");
	     /*  String strUrl = "http://www.dianping.com/ajax/json/shopDynamic/promoInfo?shopId=6252839&cityId=1&shopName=&power=5&mainCategoryId=2890&shopType=15";
	       String content = CommonHttpClient.httpClient(strUrl);
	       System.out.println(strUrl);*/
//	       String content = CommonHttpClient.httpClient(strUrl);
	       
	      /* JsonNode nodes = JsonUtils.parse(content);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < nodes.findParents("desc").size(); i++) {
				Object shortTitle = nodes.findParents("desc").get(i).get("desc");
				sb.append(shortTitle.toString().replace("\"","").trim())
				.append(";");
			}*/
	       
	       
	     /*  JsonNode nodes = JsonUtils.parse(content);
			StringBuffer sb = new StringBuffer();
			System.out.println(nodes.findParents("dealDetails").size());
			if(nodes.findParents("dealDetails").size() > 0){
				for (int i = 0; i < nodes.findParents("productTitle").size(); i++) {
					Object shortTitle = nodes.findParents("productTitle").get(i).get("productTitle");
					Object titleDesc = nodes.findParents("titleDesc").get(i).get("titleDesc");
					sb.append(shortTitle.toString().replace("\"","").trim())
					.append("[").append(titleDesc.toString().replace("\"","").trim()).append("]")
					.append(";");
				}
			}
			System.out.println(nodes.findParents("dealMoreDetails").size());
			if(nodes.findParents("dealMoreDetails").size() > 0){
				for (int j = 0; j < nodes.findParents("desc").size(); j++) {
					Object titleDesc = nodes.findParents("desc").get(j).get("desc");
					sb.append(titleDesc.toString().replace("\"","").trim()).append(";");
				}
			}
						System.out.println(sb.toString());*/
//	       try {
			
			
			
			
//			String htmls = StringEscapeUtils.escapeJava(jsonNode.get("msg").toString());
//			System.out.println("1111=="+htmls);
			/*strHtml=strHtml.replace("&nbsp;","");
			strHtml=strHtml.replace(".","");
			strHtml=strHtml.replace("/","");
					strHtml=strHtml.replace("'","");
			strHtml=strHtml.replaceAll("//s*|/t|/r|/n","");//去除字符串中的空格,回车,换行符,制表符
//*/			
//			System.out.println(str);
//			String newHtml = "";
			/*if (str!=null) {
				Pattern p = Pattern.compile("\t|\r|\n");
				Matcher m = p.matcher(str);
				newHtml = m.replaceAll("");
			}*/
//			return newHtml;

//			System.out.println(newHtml);
			/*str = str.replaceAll("<[^>]+>", "");
			str = str.replaceAll("\\s*", "");
			str = str.replaceAll("&[^&^;]+;", "");
			str = str.replaceAll("//s*|/\t|/r|/n","");*/
//			System.out.println(str.replace("\\t"," ").replace("\\", "  ").replace("&nbsp;",""));
			
			/*JsonNode jsonNode = JsonUtils.parse(content);
			System.out.println(jsonNode.get("msg"));
			
			String str = jsonNode.get("msg").toString().replace("\"","").trim();
			str =str.replace("\\t"," ")
					.replaceAll("&nbsp;","")
					.replaceAll("\\&quot;", "'")
					.replaceAll("\\&middot;", "·")
					.replaceAll("\\\\","\"");
			str =str.replaceAll("&nbsp;","");
			str =str.replaceAll("\\&quot;", "'");
			str =str.replaceAll("\\&middot;", "·");
			str = str.replaceAll("\\\\","\"");
					
					System.out.println(str);
					
			
			Map map = JsoupHtmlResolver.videoDetailResolver(str);
			System.out.println(map.entrySet());*/
		
//	       String list = new JsonPathSelector("$.results[*]").toString();
//	       JSONObject jobj=new JSONObject(content);
//	       String needStr=jobj.getString("allDishes");
	      /* try {
//	    	   CommonModel commonModel = JsonUtils.fromJson(content, CommonModel);
//	    	   System.out.println(commonModel);
//	    	   Map map = JsonUtils.parseMap(content);
//	    	   System.out.println(map.entrySet());
//	    	   String str = map.get("allDishes").toString().replaceAll("[\\[\\]]", "");  
	    	   
	    	   JsonNode nodes = JsonUtils.parse(content);
	    	   System.out.println(nodes.findParents("dishTagName").size());
	    	   StringBuffer sb = new StringBuffer();
	    	   for (int i = 0; i < nodes.findParents("dishTagName").size(); i++) {
	    		   Object node = nodes.findParents("dishTagName").get(i).get("dishTagName");
	    		   sb.append(node).append(",");
			}
	    	   System.out.println(sb.toString());
	    	   String[] strArray = sb.toString().split(",");
	    	   System.out.println(strArray.length);*/
	    	   
	    	   
	    	   
//	    	   Map<String,String> retMap = strJsonDataHandles(str, "dishTagName");
//			System.out.println(retMap);
/*		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	      /* ObjectMapper mapper = new ObjectMapper();  
	       mapper.*/
//	        System.out.println(needStr);
	 /*       CommonModel commonModel = new CommonModel();
//	        commonModel.setUrlType(ListUrlType.HOTEL);
	        commonModel.setUrlType(ListUrlType.BABY);
	        commonModel.setStrHtml(content);
	        JsoupHtmlResolver.dianpingDetailHtmlResolver(ListUrlType.FOOD,content);*/
//	        JsoupHtmlResolver.dianpingHtmlResolver(commonModel);
//	        System.out.println("Result :===================\n " + content);
//	        dianpingDetailHtmlResolver(content);
	    }
	   
	   
		/**
		  * 
		  * @param str 要处理的json字符串
		  * @param removeKeys 需要移除的参数keys
		  * @return
		  */
		public static Map<String,String> strJsonDataHandles(String str,String removeKeys){
			Map<String,String> retMap = new HashMap<String, String>();
			String [] keys = removeKeys.split(",");
				try {
					JSONObject json = new JSONObject(str);
					for (int i = 0; i < keys.length; i++) {
					String handleParams = json.get(keys[i]).toString();
					json.remove(keys[i]);
					String newStr = json.toString();
					retMap.put("newStr", newStr);
					retMap.put(keys[i], handleParams);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return retMap;
		 }
}
