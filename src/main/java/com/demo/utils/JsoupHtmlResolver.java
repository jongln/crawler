package com.demo.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.demo.entity.CommonModel;
import com.demo.entity.NewsCommonModel;

/**
 * 页面定向解析
 * @author zhaojq
 * @since 2017-04-01
 */
public class JsoupHtmlResolver {
	
	/**
     * 获取大众点评城市代码
     * @param commonModel
     * @return
     */
    public static int dianpingScriptCityCodeHtmlResolver(String strHtml) {
    	Document doc = Jsoup.parse(strHtml);
    	Elements ListDiv  = doc.getElementsByTag("script");
    	int cityId = 0;//城市代码默认0
    	for (Element element : ListDiv) {
    		if(element.toString().contains("_setCityId") || element.toString().contains("setCityId")){
    			String[] data = element.data().toString().split(";");
    			for(String variable : data){
					//过滤variable为空的数据
					if(variable.contains(":")){
						//取到满足条件的JS变量
						
							String[]  strArray = variable.split("],");
							for (int i = 0; i < strArray.length; i++) {
								if(strArray[i].contains("_setCityId") || strArray[i].contains("setCityId")){
									System.out.println(variable);
									cityId = Integer.valueOf(strArray[i].toString().substring(strArray[i].indexOf(",")+1).trim());//value
//									String params = value.substring(value.indexOf(",")+1).replace("\"","").trim();
								}
							}
						}
					}
    		}
    		
    		
    		//首次抓取商品详情时，获取所在城市ID
			if( cityId==0 && element.toString().contains("cityId")){
				String[] data = element.data().toString().split(";");
				for(String variable : data){
					if(variable.contains(":")){
						if(variable.contains("cityId")){
							String[]  strArray = variable.split(",");
							for (int i = 0; i < strArray.length; i++) {
								if(strArray[i].contains("cityId")){
//									String key =strArray[i].toString().substring(0,strArray[i].indexOf(":")).trim();//key
									String value =strArray[i].toString().substring(strArray[i].indexOf(":")+1);//value
									if(value.indexOf("}") > -1){
										value = value.substring(0,value.indexOf("}"));
									}
									cityId = Integer.valueOf(value.replace("\"","").trim());//value
									System.out.println(value);
								}
							}
						}
					}
				}
				
			}
    		
    	}
    	 /* String[] elScriptList =  ListDiv.html().toString().split("=");
    		String string = elScriptList[elScriptList.length-1].toString();
    		String[] arryStr = string.split("],");
    		String lineResult = "";
    		for (int i = 0; i < arryStr.length; i++) {
				String _string = arryStr[i];
				if(_string.indexOf("_setCityId") != -1){
					lineResult = arryStr[i].toString();
					break;
				}
			}
    		System.out.println(lineResult);
//    		int cityId = 0;//城市代码默认0
    		if(org.apache.commons.lang.StringUtils.isNotBlank(lineResult)){
    			String cityLine = lineResult.substring(lineResult.indexOf("setCityId"), lineResult.length()).replace("'", "").replace(" ", "");
    			cityId = Integer.valueOf(cityLine.substring(cityLine.indexOf(",")+1, cityLine.length()));
    			System.out.println("cityId=="+cityId);
    		}*/
		return cityId;
    }
	
    
    /**
     * 获取类型总页数
     * @param strHtml
     * @return
     */
    public static int totalCountHtmlResolver(String strHtml) {
    	
    	int countNum = 0;
    	String num = "";
    	Document doc = Jsoup.parse(strHtml);
    	Element elements = null;
    	if(doc.toString().contains("no-shop-section")){
    		return countNum;
    	}
    	System.out.println(doc.getElementsByAttributeValueMatching("class", "pages-list").hasText());
    	if(doc.getElementsByAttributeValueMatching("class", "shop-wrap").hasText()){
    		Element element = doc.getElementsByClass("shop-wrap").get(0);
    		System.out.println(element.getElementsByClass("page").hasText());
    		if(element.getElementsByClass("page").hasText()){
    			elements  = element.getElementsByClass("page").get(0);
    			Elements elementList  = elements.getElementsByTag("a");
        		num = elementList.get(elementList.size()-2).text();
    		}else if(element.getElementsByClass("pages").hasText()){
    			elements  = element.getElementsByClass("pages").get(0);
    			Elements elementList  = elements.getElementsByTag("a");
        		num = elementList.get(elementList.size()-2).text();
    		}else{
    			//部分县级单页数据，存在找不到page情况默认返回页数1
    			num = "1";
    			System.out.println("获取页数数据异常");
    		}
    	}else if(doc.getElementsByAttributeValueMatching("class", "pages-list").hasText()){
    		Element element = doc.getElementsByClass("pages-list").get(0);
    		System.out.println(element.getElementsByClass("page").hasText());
    		Elements elementList  = element.getElementsByTag("li");
			num = elementList.get(elementList.size()-2).text();
    	}
    	
		if(org.apache.commons.lang.StringUtils.isNotBlank(num)){
			countNum = Integer.valueOf(num);
		}
		return countNum;
    }
    
	
	/**
	 * jsoup 对大众点评列表信息进行数据解析
	 * @param commonModel
	 * @return
	 */
	 public static List<CommonModel> dianpingHtmlResolver(CommonModel commonModel) {
		 List<CommonModel> commonModelList = new ArrayList<CommonModel>();
	    	Document doc = Jsoup.parse(commonModel.getStrHtml());
	    	if(commonModel.getUrlType() == ListUrlType.HOTEL){//酒店
	    		Element element  = doc.getElementsByClass("hotelshop-list").get(0);
	    		Elements elementList = element.getElementsByAttributeValue("class", "hotel-block J_hotel-block");
	    		for (Element _element : elementList) {
	    			StringBuffer sb_imgUrl = new StringBuffer();
	    			CommonModel _commonModel = new CommonModel();
	    			String title = _element.getElementsByAttributeValue("class", "hotel-name-link").html();//酒店名称
	    			/*String mark = _element.getElementsByAttributeValue("class", "place").select("a").html();//地处位置
	    			String address = _element.getElementsByAttributeValue("class", "place").select("span").html();//地址路径
*/	    			Elements pictrues = _element.getElementsByAttributeValue("class", "J_hotel-pics").get(0).getElementsByTag("li");//酒店图片
	    			for (Element _picture : pictrues) {
	    				String picture = _picture.select("img").attr("data-lazyload");
	    				sb_imgUrl.append(picture).append(",");
	    				System.out.println(picture);
					}
	    			String detailUrl = "http://www.dianping.com"+_element.select("a").attr("href");//详情url
	    			String shopId = detailUrl.substring(detailUrl.indexOf("newhotel")+9);
	    			_commonModel.setShopId(shopId);//店铺ID
	    			_commonModel.setUrlType(commonModel.getUrlType());
	    			_commonModel.setDetailUrl(detailUrl);
	    			_commonModel.setTitle(title);
//	    			_commonModel.setType();
	    			_commonModel.setPicture(sb_imgUrl.toString());
//	    			_commonModel.setAddress(mark+address);
	    			commonModelList.add(_commonModel);
				}
	    	}else if(commonModel.getUrlType() == ListUrlType.BABY || commonModel.getUrlType() == ListUrlType.MARRIED
	    			|| commonModel.getUrlType() == ListUrlType.HOME){
	    		Element element  = doc.getElementsByClass("shop-list").get(0);
	    		Elements elementList = element.getElementsByTag("li");
	    		for (Element _element : elementList) {
	    			CommonModel _commonModel = new CommonModel();
	    			String title = _element.select("a").attr("title");//店铺名称
	    			if(commonModel.getUrlType() == ListUrlType.HOME){
	    				title = _element.getElementsByClass("shop-title").select("a").get(0).html();//店铺名称
	    				
	    			}
	    			String detailUrl = "http://www.dianping.com"+_element.select("a").attr("href");//详情url
	    			
	    			String picture = "";
	    			if(org.apache.commons.lang.StringUtils.isNotBlank(_element.select("img").attr("src"))){
	    				picture = "http:"+_element.select("img").attr("src");//图片
	    			}
	    			String shopId = detailUrl.substring(detailUrl.indexOf("shop")+5);
	    			if(shopId.indexOf("?") != -1){
	    				shopId = shopId.substring(0, shopId.indexOf("?"));
	    			}
	    			_commonModel.setShopId(shopId);//店铺ID
	    			_commonModel.setUrlType(commonModel.getUrlType());
	    			_commonModel.setDetailUrl(detailUrl);
	    			_commonModel.setTitle(title);
//	    			_commonModel.setType();
	    			_commonModel.setPicture(picture);
//	    			_commonModel.setAddress();
	    			commonModelList.add(_commonModel);
				}
	    	}else if(commonModel.getUrlType() == ListUrlType.FEAST ){
    			Element element  = doc.getElementsByClass("shop-list").get(0);
    			Elements elementList = element.getElementsByClass("shop-item");
    			for (Element _element : elementList) {
    				CommonModel _commonModel = new CommonModel();
    				String title = _element.getElementsByClass("shop-name").html();//店铺名称
    				String address = _element.getElementsByClass("shop-detail").select("span").html();//店铺名称
    				String detailUrl = "http://www.dianping.com"+_element.select("a").attr("href");//详情url
    				String picture = "";
    				if(org.apache.commons.lang.StringUtils.isNotBlank(_element.getElementsByTag("img").attr("src"))){
    					picture = "http:"+_element.select("img").attr("src");//图片
    				}
    				String shopId = detailUrl.substring(detailUrl.indexOf("shop")+5);
    				if(shopId.indexOf("?") != -1){
	    				shopId = shopId.substring(0, shopId.indexOf("?"));
	    			}
	    			_commonModel.setShopId(shopId);//店铺ID
    				_commonModel.setUrlType(commonModel.getUrlType());
    				_commonModel.setDetailUrl(detailUrl);
    				_commonModel.setTitle(title);
//	    			_commonModel.setType();
    				_commonModel.setPicture(picture);
    				_commonModel.setAddress(address);
    				commonModelList.add(_commonModel);
    			}
	    		
	    	}else{//通用解析
    			Element content  = doc.getElementById("shop-all-list");
    			Elements ListDiv = content.getElementsByTag("li");
    			for (Element element :ListDiv) {
    				CommonModel _commonModel = new CommonModel();
    				Elements links = element.getElementsByAttributeValue("class", "tit");
    				Elements tags = element.getElementsByAttributeValue("class", "tag-addr");
    				String tag = tags.text();
    				Elements pictures = element.getElementsByAttributeValue("class", "pic");
    				String detailUrl = "";
    				if(!org.springframework.util.StringUtils.isEmpty(links.select("a").attr("href"))){
    					detailUrl = "http://www.dianping.com"+links.select("a").attr("href");//店铺详情url
    					_commonModel.setDetailUrl(detailUrl);
    				}
    				String shopId = detailUrl.substring(detailUrl.indexOf("shop")+5);
	    			_commonModel.setShopId(shopId);//店铺ID
    				_commonModel.setUrlType(commonModel.getUrlType());
    				_commonModel.setTitle(links.select("a").attr("title"));
    				_commonModel.setType(tags.text().substring(0, tag.indexOf("|")));
    				_commonModel.setAddress(tags.text().substring(tag.indexOf("|")+1));
    				_commonModel.setPicture(pictures.select("img").attr("data-src"));
    				commonModelList.add(_commonModel);
    			}
	    	}
			return commonModelList;
	    }
	 
	 
	  /**
	     * 店铺详情页面抓取分析（主要获取店铺经、纬度和店铺地址、联系方式）
	     * @param strHtml
	     * @return
	     */
	    public static Map<String, Object> dianpingDetailHtmlResolver(ListUrlType urlType,String strHtml) {
	    	Document doc = Jsoup.parse(strHtml);
	    	Map<String, Object> map = new HashMap<String, Object>();
	    	try {
	    		//step1 获取店铺经纬度(店铺细分类别)
	    		Elements _e_script  = doc.getElementsByTag("script");
	    		/*循环遍历script变量*/
	    		for (Element element : _e_script) {
	    			if(element.toString().contains("shopGlat") || element.toString().contains("poi:")
	    					|| element.toString().contains("lat")){
	    				if(element.toString().contains("poi:")){//解码取值
	    					//取得JS变量数组
	    					String[] data = element.data().toString().split(";");
	    					//取得单个JS变量
	    					for(String variable : data){
	    						//过滤variable为空的数据
	    						if(variable.contains(":")){
	    							//取到满足条件的JS变量
	    							if(variable.contains("poi")){
	    								String[]  strArray = variable.split(",");
	    								for (int i = 0; i < strArray.length; i++) {
	    									if(strArray[i].contains("poi")){//遍历获取店铺经纬度
	    										String key =strArray[i].toString().substring(0,strArray[i].indexOf(":")).trim();//key
	    										String value =strArray[i].toString().substring(strArray[i].indexOf(":")+1).trim();//value
	    										String params = value.substring(value.indexOf(":")+1).replace("\"","").trim();
	    										if(params.indexOf("}") >0 ){
	    											params = params.substring(0,params.indexOf("}")).replaceAll("'", "");
	    										}
	    										Map<String,Object> _map = CommonUtils.javaScriptDatadecode(params);
    											map.put("shopGlat", _map.get("lat"));
    											map.put("shopGlng", _map.get("lng"));
    											if(null !=map.get("shopGlat") && null !=map.get("shopGlng")){
    												break;
    											}
	    									}
	    								}
	    							}
	    						}
	    					}
	    				}else if(element.toString().indexOf("lat") > 0 && element.toString().indexOf("lng")> 0
	    						&& !element.toString().contains("shopGlat")){//部分新版本新界面
	    					String[] data = element.data().toString().split("=");
	    					Map<String,Object> _map = new HashMap<String, Object>();
	    					//取得单个JS变量
	    					for(String variable : data){
	    						//过滤variable为空的数据
	    						if(variable.contains(":")){
	    							//取到满足条件的JS变量
	    							if(variable.contains("lat") && variable.contains("lng")){
	    								String[]  strArray;
	    								if(variable.contains("icon")){
	    									String regex = "(?<=\\{)(\\S+)(?=\\})";
									        Pattern pattern = Pattern.compile (regex);
									        Matcher matcher = pattern.matcher (variable);
									        String str = "";
									        while (matcher.find()){
									        	str = matcher.group();
									            System.out.println (matcher.group());
									        }
	    									strArray = str.split(",");
	    								}else{//通用
	    									strArray = variable.split(",");
	    								}
    									for (int i = 0; i < strArray.length; i++) {
    										if(strArray[i].contains("lat") || strArray[i].contains("lng")){//遍历获取店铺经纬度
    											System.out.println(strArray[i]);
    											String key =strArray[i].toString().substring(0,strArray[i].indexOf(":")).trim();//key
    											String value =strArray[i].toString().substring(strArray[i].indexOf(":")+1);//value
    											_map.put(key.replace("\"","").trim(), value.replace("\"","").trim());
    											System.out.println(null !=_map.get("lat") && null !=_map.get("lng"));
    											map.put("shopGlat", _map.get("lat"));
    											map.put("shopGlng", _map.get("lng"));
    											if(null !=map.get("shopGlat") && null !=map.get("shopGlng")){
    												break;
    											}
    										}
    									}
	    							}
	    						}
	    					}
	    					
	    				}else{//页面常规取值（通用）
	    					//取得JS变量数组
	    					String[] data = element.data().toString().split("=");
	    					//取得单个JS变量
	    					for(String variable : data){
	    						//过滤variable为空的数据
	    						if(variable.contains(":")){
	    							//取到满足条件的JS变量
	    							if(variable.contains("shopGlat") || variable.contains("shopGlng")){
	    								String[]  strArray = variable.split(",");
	    								for (int i = 0; i < strArray.length; i++) {
	    									if(strArray[i].contains("shopGlat") || strArray[i].contains("shopGlng")){//遍历获取店铺经纬度
	    										String key =strArray[i].toString().substring(0,strArray[i].indexOf(":")).trim();//key
	    										String value =strArray[i].toString().substring(strArray[i].indexOf(":")+1);//value
	    										map.put(key, value.replace("\"","").trim());
	    									}
	    								}
	    							}
	    						}
	    					}
	    				}
	    				}
	    			
	    		}
	    		
	    		if(map.size()!= 2){//如果经纬度解析失败，则直接返回，不再继续解析
	    			return map;
	    		}
	    		//step2 获取店铺联系方式
	    		String telPhone = "";
	    		if(doc.getElementsByAttributeValueMatching("id", "basic-info").hasText()){
	    			Elements e_detail  = doc.getElementById("basic-info").getElementsByTag("span");
	    			for (Element _element : e_detail) {
	    				if(_element.toString().contains("tel")){
	    					telPhone = _element.attr("itemprop", "tel").html().trim();
//	    					System.out.println(telPhone);
	    				}
	    			}
	    		}else if(doc.getElementsByAttributeValueMatching("class", "basic-info").hasText()){
	    			Element e_detail  = doc.getElementsByClass("mix-info").get(0);
	    			Elements elementList = e_detail.getElementsByTag("div");
	    			for (Element _element : elementList) {
	    				if(_element.hasClass("phone")){
	    					String str = _element.attr("class", "phone").getElementsByTag("span").html();
	    					telPhone = str;
	    					if(str.indexOf("|")> 0){
	    						telPhone = str.substring(0,str.indexOf("|")).trim();
	    					}
	    					break;
	    				}
	    			}
	    		}else if(doc.getElementsByAttributeValueMatching("class", "icon-phone").hasText()){
//	    		}else if(doc.getElementsByAttributeValueMatching("class", "shop-contact").hasText()){
	    			Element e_detail  = doc.getElementsByClass("icon-phone").get(0);
	    			telPhone = e_detail.html().trim();
	    			/*Elements elementList = e_detail.getElementsByTag("span");
	    			for (Element _element : elementList) {
	    				if(_element.hasClass("icon-phone")){
	    					telPhone = _element.attr("class", "icon-phone").html().trim();
	    					System.out.println("电话====="+urlType+telPhone);
	    				}
	    			}*/
	    		}else if(doc.getElementsByAttributeValueMatching("class", "shopinfor").hasText()){//亲子
	    			Element e_detail  = doc.getElementsByClass("shopinfor").get(0);
	    			Elements elementList = e_detail.getElementsByTag("span");
	    			for (Element _element : elementList) {
	    				if(_element.toString().contains("icon-tel")){
	    					telPhone = _element.attr("class", "icon-tel").text().trim();
	    				}
	    				
	    			}
	    		}else if(doc.getElementsByAttributeValueMatching("class", "list-info").hasText()){//hotel
	    			Element e_detail  = doc.getElementsByClass("list-info").get(0);
	    			Element _element = e_detail.getElementsByTag("li").get(0);
	    			telPhone = _element.getElementsByTag("div").attr("class", "info-value").get(1).html().trim();
	    		}else if(doc.getElementsByAttributeValueMatching("class", "expand-info tel").hasText()){//婚宴
	    			Element e_detail  = doc.getElementsByClass("expand-info").get(1);
	    			Elements _elementList = e_detail.getElementsByTag("span");
	    			for (Element _element : _elementList) {
	    				if(_element.toString().contains("item")){
	    					telPhone = _element.attr("class", "item").text().trim();
	    				}
	    			}
	    		}else if(doc.getElementsByAttributeValueMatching("class", "merchan-phone").hasText()){//家装
	    			Element e_detail  = doc.getElementsByClass("merchan-phone").get(0);
	    			telPhone = e_detail.html();
	    			
	    		}else if(doc.getElementsByAttributeValueMatching("class", "shop-info-content").hasText()){//家装
	    			Elements e_detail  = doc.getElementsByClass("shop-info-content");
	    			for (Element element : e_detail) {
	    				if(element.toString().contains("tel")){
	    					telPhone = element.text();
	    				}
					}
	    		}else if(doc.getElementsByAttributeValueMatching("class", "shop-contact").hasText()){
	    			Elements e_detail  = doc.getElementsByClass("shop-contact");
	    			for (Element element : e_detail) {
	    				if(element.toString().contains("strong")){
	    					telPhone = element.text();
	    				}
					}
	    		}else if(doc.getElementsByAttributeValueMatching("class", "market-detail-other").hasText()){
	    			
	    			Element e_detail  = doc.getElementsByClass("market-detail-other").get(0);
	    			telPhone = e_detail.getElementsByTag("p").get(0).text();
	    			if(telPhone.indexOf("：") > 0){
	    				telPhone = telPhone.substring(telPhone.indexOf("：")+1).trim();
	    			}
	    		}
	    		else{
	    			System.out.println("发现新的未解析详情页面");
	    		}
	    		map.put("telPhone", telPhone);
	    		
	    		//step3 地址（列表未发现）
	    		String address = "";
	    		if(urlType == ListUrlType.MARRIED){
	    			if(doc.getElementsByAttributeValueMatching("class", "shop-info-content").hasText()){
	    				Element e_detail  = doc.getElementsByClass("shop-info-content").get(0);
	    				Elements elements = e_detail.getElementsByTag("span");
	    				for (Element element : elements) {
							if(element.toString().contains("street-address")){
								address = element.text();
							}
						}
	    			}else if(doc.getElementsByAttributeValueMatching("class", "shop-addr").hasText()){
	    				System.out.println(doc.getElementsByClass("fl").hasText());
	    				if(!doc.getElementsByClass("fl").hasText()){
	    					Element e_detail  = doc.getElementsByClass("shop-addr").get(0);
	    					address = e_detail.getElementsByTag("span").attr("title");
	    					System.out.println(address);
	    					
	    				}else{
	    					Element e_detail  = doc.getElementsByClass("fl").get(0);
	    					Elements elementList = e_detail.getElementsByTag("span");
	    					for (Element element : elementList) {
	    						if(element.toString().contains("fl road-addr")){
	    							address = element.attr("class", "fl road-addr").text().trim();
	    							break;
	    						}
	    					}
	    					
	    				}
	    			}else{
	    				System.out.println("地址解析未找到");
	    			}
	    		}
	    		if(urlType == ListUrlType.BABY){
	    			if(doc.getElementsByAttributeValueMatching("class", "shop-info-content").hasText()){
	    				Element e_detail  = doc.getElementsByClass("shop-info-content").get(0);
	    				Elements elements = e_detail.getElementsByTag("span");
	    				for (Element element : elements) {
							if(element.toString().contains("street-address")){
								address = element.text();
							}
						}
	    			}else if(doc.getElementsByAttributeValueMatching("class", "shop-addr").hasText()){
	    				Element e_detail  = doc.getElementsByClass("shop-addr").get(0);
	    				address = e_detail.getElementsByTag("span").attr("title");
	    				
	    			}else{
	    				System.out.println("baby 发现新的未解析页面");
	    			}
	    		}
	    		if(urlType == ListUrlType.HOTEL){
	    			Element e_detail  = doc.getElementsByClass("hotel-address").get(0);
	    			address = e_detail.getElementsByTag("span").html();
	    			if(address.indexOf("：") != -1){
	    				address = address.substring(address.indexOf("：")+1);
	    			}
	    		}
	    		if(urlType == ListUrlType.HOME){
	    			if(doc.getElementsByAttributeValueMatching("class", "shopDeal-Info-address").hasText()){//部分区县页面变动获取
	    				Element element  = doc.getElementsByClass("shop-info-content").get(0);
	    				Elements e_detail  = element.getElementsByTag("span");
	    				for (Element _element : e_detail) {
	    					if(_element.toString().contains("street-address")){
	    						address = element.text();
	    					}
						}
	    			}else if(doc.getElementsByAttributeValueMatching("class", "shop-addr").hasText()){
	    				Element e_detail  = doc.getElementsByClass("shop-addr").get(0);
	    				address = e_detail.getElementsByTag("span").attr("title");
	    			}else{
	    				Element e_detail  = doc.getElementsByClass("shop-contact").get(0);
	    				if(!e_detail.getElementsByTag("span").attr("title").isEmpty()){
	    					address = e_detail.getElementsByTag("span").attr("title");
	    				}else{
	    					String str = e_detail.getElementsByTag("p").text();
	    					if(!"".equals(str)){
	    						address =str.substring(str.indexOf("：")+1);
	    					}
	    				}
	    			}
	    		}
	    		map.put("address", address);
	    		
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("html解析出现异常");
				map.clear();
				return map;
			}
			return map;
	    }
	    
	    
	    
	    /**
	     * 获取店铺详情页面的商品属性信息（丽人、亲子、学习培训、家装、结婚、婚宴）
	     * @param urlType 所属业务类型
	     * @param strHtml 待解析的html页面
	     * @return
	     */
	    public static Map<String, Object> dianpingGoodslHtmlResolver(ListUrlType urlType,String strHtml) {
	    	Document doc = Jsoup.parse(strHtml);
	    	Map<String, Object> map = new HashMap<String, Object>();
	    	try {
	    		
	    		//step1 获取店铺ID,mainCategoryId(店铺细分类别)
	    		Elements _e_script  = doc.getElementsByTag("script");
	    		/*循环遍历script变量*/
	    		for (Element element : _e_script) {
	    			//获取类别mainCategoryId
	    			if(element.toString().contains("mainCategoryId") || element.toString().contains("categoryId")){
	    				String[] data = element.data().toString().split(";");
	    				String value = "";
    					for(String variable : data){
    						if(variable.contains(":")){
    							if(variable.contains("mainCategoryId") || variable.contains("categoryId")){
    								String[]  strArray = variable.split(",");
    								for (int i = 0; i < strArray.length; i++) {
    									if(strArray[i].contains("mainCategoryId") || strArray[i].contains("categoryId") || strArray[i].contains("data-mkt-categoryId")){
//    										String key =strArray[i].toString().substring(0,strArray[i].indexOf(":")).trim();//key
    										String regEx="[^0-9]";  
    										Pattern p = Pattern.compile(regEx);  
    										Matcher m = p.matcher(strArray[i].toString());  
    										System.out.println( m.replaceAll("").trim());
    										value =m.replaceAll("").trim();//value
    										
//    										value =strArray[i].toString().substring(strArray[i].indexOf(":")+1);//value
    										/*if(key.equals("categoryId")){

    											value =strArray[i].toString().substring(strArray[i].indexOf(":")+1,strArray[i].indexOf("}"));//value
    										}else{
    											value =strArray[i].toString().substring(strArray[i].indexOf(":")+1);//value
    										}*/
    										/*if(!map.containsKey("mainCategoryId")){
    											System.out.println("mainCategoryId"+value);
    										}*/
    									}
    								}
    							}
    							
    						}
    					}
    					map.put("mainCategoryId", value);
	    			}
	    			
	    			//获取店铺类别代码
	    			if(element.toString().contains("shopType")){
	    				String[] data = element.data().toString().split(";");
    					for(String variable : data){
    						if(variable.contains(":")){
    							if(variable.contains("shopType")){
    								String[]  strArray = variable.split(",");
    								for (int i = 0; i < strArray.length; i++) {
    									if(strArray[i].contains("shopType")){
//    										String key =strArray[i].toString().substring(0,strArray[i].indexOf(":")).trim();//key
    										String value =strArray[i].toString().substring(strArray[i].indexOf(":")+1);//value
    										map.put("shopType", value.replace("\"","").trim());
    										System.out.println("shop=="+map.get("shopType"));
    										if(null != map.get("shopType")){
    											break;
    										}
//    										System.out.println(key+value);
    									}
    								}
    							}
    						}
    					}
	    			}
	    			
	    			//首次抓取商品详情时，获取所在城市ID
	    			if(null == urlType && element.toString().contains("cityId")){
	    				String[] data = element.data().toString().split(";");
    					for(String variable : data){
    						if(variable.contains(":")){
    							if(variable.contains("cityId")){
    								String[]  strArray = variable.split(",");
    								for (int i = 0; i < strArray.length; i++) {
    									if(strArray[i].contains("cityId")){
//    										String key =strArray[i].toString().substring(0,strArray[i].indexOf(":")).trim();//key
    										String value =strArray[i].toString().substring(strArray[i].indexOf(":")+1);//value
    										if(value.indexOf("}") > -1){
    											value = value.substring(0,value.indexOf("}"));
    										}
    										map.put("cityId", value.replace("\"","").trim());
    										System.out.println(value);
    									}
    								}
    							}
    						}
    					}
	    				
	    			}
	    			
	    			}
	    		if(map.get("mainCategoryId").toString().equals("0")){
	    			if(doc.toString().contains("data-mkt-categoryid")){
	    				Element e_detail  = doc.getElementsByClass("float-layer").get(0);
	    				Elements elements = e_detail.getElementsByClass("J_mkt");
	    				String [] str = elements.toString().split(" ");
	    				for (String _str : str) {
							if(_str.contains("data-mkt-keyword")){
								String value =_str.toString().substring(_str.indexOf("=")+1);//value
								map.put("mainCategoryId", value.replace("\"","").trim());
							}
						}
	    				System.out.println(str);
	    		}
	    		}
	    		
	    		//丽人商品套餐获取
	    		if(urlType == ListUrlType.BEAUTY){
	    			if(doc.getElementsByAttributeValueMatching("class", "mod sales-promotion clearfix").hasText()){
	    				Element e_detail  = doc.getElementById("sales");
	    				Elements elements = e_detail.getElementsByClass("group");
	    				StringBuffer sb_names = new StringBuffer();//商品名称
						StringBuffer sb_prices = new StringBuffer();//商品价格
						StringBuffer sb_details = new StringBuffer();//商品详情
	    				for (Element element : elements) {
	    					if(element.toString().contains("item big")){
	    						Elements _e_detail  = element.getElementsByClass("item");
	    						for (Element _element : _e_detail) {
	    							String title = _element.getElementsByTag("p").text();
	    							String price = _element.getElementsByClass("price").text();
	    							String detail = _element.select("img").attr("src");//图片
	    							sb_names.append(title).append(";");
	    							sb_prices.append(price).append(";");
	    							sb_details.append(detail).append(";");
								}
	    						
	    					}else if(element.toString().contains("item small") && !element.getElementsByAttributeValueMatching("id", "vcbooking").hasText()){
	    						Element s_e_detail  = element.getElementsByTag("div").get(0);
	    						Elements s_elements = s_e_detail.getElementsByTag("a");
	    						for (Element _element : s_elements) {
	    							String title = _element.ownText();
	    							String price = _element.getElementsByClass("price").text();
	    							String detail = null;
	    							if(_element.toString().contains("pic")){
	    								detail = _element.select("img").attr("src");//图片
	    							}
	    							sb_names.append(title).append(";");
	    							sb_prices.append(price).append(";");
	    							sb_details.append(detail).append(";");
	    							System.out.println(sb_names);
	    							System.out.println(sb_prices);
	    							System.out.println(sb_details);
								}
	    					}
						}
	    				map.put("goodsName", sb_names.toString());
	    				map.put("goodsPrices", sb_prices.toString());
	    				map.put("goodsDetals", sb_details.toString());
	    			}
	    		}
	    		
	    		//亲子店铺套餐详情
	    		if(urlType == ListUrlType.BABY){
	    			StringBuffer sb_names = new StringBuffer();//商品名称
					StringBuffer sb_prices = new StringBuffer();//商品价格
					StringBuffer sb_details = new StringBuffer();//商品详情
					
					//团购优惠套餐
	    			if(doc.getElementsByAttributeValueMatching("class", "promo-list").hasText()){
	    				Element e_detail  = doc.getElementsByClass("promo-list").get(0);
	    				Elements elements = e_detail.getElementsByTag("li");
	    				for (Element element : elements) {
	    					String name = element.getElementsByClass("voucher").attr("title");
	    					String price = element.getElementsByClass("cost").text();
	    					String detail = element.getElementsByClass("discount").text();
	    					sb_names.append(name).append(";");
	    					sb_prices.append(price).append(";");
	    					sb_details.append("".equals(detail) ? null : detail).append(";");
						}
	    				
	    			}
	    			//精品套餐
	    			if(doc.getElementsByAttributeValueMatching("id", "J_boxPack").hasText()){
	    				Element p_element  = doc.getElementById("J_boxPack");
	    				Element element  = p_element.getElementsByClass("pack-con").get(0);
	    				
	    				String linkUrl  = p_element.getElementsByClass("hd").select("a").attr("href");
	    				if(!"".equals(linkUrl)){//深度抓取套餐案例
	    					linkUrl = PublicConstants.DIANPING_URL+linkUrl;
	    				}else{
	    					Elements elements = element.getElementsByTag("li");
	    					System.out.println(elements.size());
	    					for (Element _element : elements) {
	    						String name = _element.getElementsByTag("h3").text();
	    						String price = _element.getElementsByClass("newPrice").text();
	    						String detail= _element.getElementsByTag("h5").text();
	    						sb_names.append(name).append(";");
		    					sb_prices.append(price).append(";");
		    					sb_details.append(detail).append(";");
							}
	    				}
	    				System.out.println(PublicConstants.DIANPING_URL+linkUrl);
	    			}
	    			if(doc.getElementsByAttributeValueMatching("id", "J_boxAppreciate").hasText()){//DIY
	    				Element p_element  = doc.getElementById("J_boxAppreciate");
	    				Element element  = p_element.getElementsByClass("pic-tit").get(0);
	    				
	    				Elements elements = element.getElementsByTag("li");
	    				System.out.println(elements.size());
	    				for (Element _element : elements) {
	    					String name = _element.getElementsByClass("pic-name").select("a").attr("title");
	    					String price = _element.getElementsByTag("h5").text().replace(" ", ",");
	    					price = price.substring(0,price.indexOf(","));
	    					String detail  = _element.getElementsByClass("mid-in").select("img").attr("data-lazyload");
	    					sb_names.append(name).append(";");
	    					sb_prices.append(price).append(";");
	    					sb_details.append("".equals(detail)? null : detail).append(";");
	    				}
	    				/*System.out.println(sb_names);
	    				System.out.println(sb_prices);
	    				System.out.println(sb_details);
	    				String linkUrl  = p_element.getElementsByClass("hd").select("a").attr("href");
	    				if(!"".equals(linkUrl)){//深度抓取套餐案例
	    					linkUrl = PublicConstants.DIANPING_URL+linkUrl;
	    				}else{
	    				}
	    				System.out.println(PublicConstants.DIANPING_URL+linkUrl);*/
	    			}
	    			
	    			map.put("goodsName", sb_names.toString());
    				map.put("goodsPrices", sb_prices.toString());
    				map.put("goodsDetals", sb_details.toString());
	    		}
	    		
	    	
	    		//学习培训套餐详情
	    		if(urlType == ListUrlType.TRAIN){
	    			StringBuffer sb_names = new StringBuffer();//商品名称
					StringBuffer sb_prices = new StringBuffer();//商品价格
					StringBuffer sb_details = new StringBuffer();//商品详情
					StringBuffer sb_pictures = new StringBuffer();//商品详情
					
	    			//团购套餐
	    			if(doc.getElementsByAttributeValueMatching("class", "promotion").hasText()){
	    				Element e_detail  = doc.getElementsByClass("promotion").get(0);
	    				Elements elements = e_detail.getElementsByClass("item");
	    				for (Element element : elements) {
	    					String name = element.getElementsByClass("title").html();
	    					String price = element.getElementsByClass("price").text();
	    					String detail = element.select("img").attr("lazy-src");
	    					sb_names.append(name).append(";");
	    					sb_prices.append(price).append(";");
	    					sb_details.append(detail).append(";");
						}
	    			}
	    			
	    			//精品课程
	    			if(doc.getElementsByAttributeValueMatching("id", "course").hasText()){
	    				Element element  = doc.getElementById("course");
	    				Elements elements = element.getElementsByClass("item");
	    				System.out.println(elements.size());
	    				for (Element _element : elements) {
	    					String name = _element.getElementsByClass("title").html();
	    					String price = _element.getElementsByClass("cur").html();
	    					String detail = _element.getElementsByClass("desc").html();
	    					String picture = _element.select("img").attr("lazy-src");
	    					sb_names.append(name).append(";");
	    					sb_prices.append(price).append(";");
	    					sb_details.append(detail).append(";");
	    					sb_pictures.append(picture).append(";");
	    					System.out.println(name);
	    				}
	    			}
	    			map.put("goodsName", sb_names.toString());
    				map.put("goodsPrices", sb_prices.toString());
    				map.put("goodsDetals", sb_details.toString());
	    		}
	    		
	    		//家装套餐详情
	    		if(urlType == ListUrlType.HOME){
	    			StringBuffer sb_names = new StringBuffer();//商品名称
					StringBuffer sb_prices = new StringBuffer();//商品价格
					StringBuffer sb_details = new StringBuffer();//商品详情
					
					//团购套餐
	    			if(doc.getElementsByAttributeValueMatching("class", "profits-list clearfix").hasText()){
	    				Element e_detail  = doc.getElementsByClass("profits-list").get(0);
	    				Elements elements = e_detail.getElementsByTag("li");
	    				for (Element element : elements) {
	    					String name = element.getElementsByTag("p").text();
	    					Element _element = element.getElementsByClass("price").get(0);
	    					String price = _element.getElementsByTag("em").text();
	    					String detail = "";
	    					if(element.toString().contains("img")){
								detail = element.select("img").attr("src");//图片
							}
	    					sb_names.append(name).append(";");
	    					sb_prices.append(price).append(";");
	    					sb_details.append(detail).append(";");
	    				}
//	    				map.put("goodsName", sb_names.toString());
	    			}
	    			
	    			//精品案例
	    			if(doc.getElementsByAttributeValueMatching("id", "J_boxAppreciate").hasText()){
	    				Element p_element  = doc.getElementById("J_boxAppreciate");
	    				Element element =p_element.getElementsByClass("case-list").get(0);
	    				Elements e_detail  = element.getElementsByTag("li");
	    				System.out.println(e_detail.size());
	    				for (Element _element : e_detail) {
	    					String name = _element.getElementsByTag("p").text();
	    					String price = _element.getElementsByClass("price").text();
	    					String detail = _element.getElementsByTag("a").text();
	    					sb_names.append(name).append(";");
	    					sb_prices.append(price).append(";");
	    					sb_details.append(detail).append(";");
						}
	    			
//	    				System.out.println(element);
	    			}
	    			map.put("goodsName", sb_names.toString());
    				map.put("goodsPrices", sb_prices.toString());
    				map.put("goodsDetals", sb_details.toString());
	    		}
	    		
	    		//结婚商品详情
	    		/*if(urlType == ListUrlType.MARRIED){
	    			StringBuffer sb_names = new StringBuffer();
    				StringBuffer sb_prices = new StringBuffer();
    				StringBuffer sb_details = new StringBuffer();
    				StringBuffer sb_pictrues = new StringBuffer();
	    			if(doc.getElementsByAttributeValueMatching("class", "pack-con").hasText()){
	    				Element e_detail  = doc.getElementsByClass("pack-con").get(0);
	    				Elements elements = e_detail.getElementsByTag("li");
//	    				StringBuffer sb = new StringBuffer();
	    				for (Element element : elements) {
	    					String name = element.getElementsByTag("h3").text();
	    					String price = element.getElementsByClass("newPrice").text();
	    					String picture = element.select("img").attr("data-lazyload");//图片
	    					sb_names.append(name).append(";");
	    					sb_prices.append(price).append(";");
	    					sb_pictrues.append(picture).append(";");
	    					Elements _elements = element.getElementsByTag("dd");
	    					for (Element _element : _elements) {
	    						String detail = _element.text();
	    						sb_details.append(_element.text());
	    						System.out.println(detail);
							}
	    					sb_details.append(";");
//	    					sb.append(name).append(";");
	    				}
	    				map.put("goodsNames", sb_names.deleteCharAt(sb_names.length() -1).toString());
	    				map.put("goodsPrices", sb_prices.deleteCharAt(sb_prices.length() -1).toString());
	    				map.put("goodsDetals", sb_details.deleteCharAt(sb_details.length() -1).toString());
	    			}
	    		}*/

	 /*   		if(urlType == ListUrlType.MARRIED){
	    			if(doc.getElementsByAttributeValueMatching("id", "J_boxReserve").hasText()){
	    				Element e_detail  = doc.getElementById("pack-con");
	    				String price = e_detail.getElementsByClass("priceDisplay").text();
	    				String title = e_detail.getElementsByClass("textshow").text();
	    				String detail = e_detail.getElementsByClass("shopinfor").html();
	    				
	    				System.out.println(title);
	    				System.out.println(price);
	    				System.out.println(detail);
	    			}
	    		}*/
	    		
	    		if(urlType == ListUrlType.MARRIED){
	    			System.out.println(doc);
	    		}
	    		
	    		
	    		//婚宴套餐详情
	    		if(urlType == ListUrlType.FEAST || urlType == ListUrlType.MARRIED){
	    			StringBuffer sb_names = new StringBuffer();
    				StringBuffer sb_prices = new StringBuffer();
    				StringBuffer sb_details = new StringBuffer();
    				StringBuffer sb_pictrues = new StringBuffer();
    				if(doc.getElementsByAttributeValueMatching("class", "pack-con").hasText()){
	    				Element e_detail  = doc.getElementsByClass("pack-con").get(0);
	    				Elements elements = e_detail.getElementsByTag("li");
//	    				StringBuffer sb = new StringBuffer();
	    				for (Element element : elements) {
	    					String name = element.getElementsByTag("h3").text();
	    					String price = element.getElementsByClass("newPrice").text();
	    					String picture = element.select("img").attr("data-lazyload");//图片
	    					sb_names.append(name).append(";");
	    					sb_prices.append(price).append(";");
	    					sb_pictrues.append(picture).append(";");
	    					Elements _elements = element.getElementsByTag("dd");
	    					for (Element _element : _elements) {
	    						String detail = _element.text();
	    						sb_details.append(_element.text());
	    						System.out.println(detail);
							}
	    					sb_details.append(";");
//	    					sb.append(name).append(";");
	    				}
	    				map.put("goodsNames", sb_names.deleteCharAt(sb_names.length() -1).toString());
	    				map.put("goodsPrices", sb_prices.deleteCharAt(sb_prices.length() -1).toString());
	    				map.put("goodsDetals", sb_details.deleteCharAt(sb_details.length() -1).toString());
	    			}
    				
	    			if(doc.getElementsByAttributeValueMatching("class", "J_table").hasText()){
	    				Element e_detail  = doc.getElementsByClass("J_table").get(0);
	    				Elements elements = e_detail.getElementsByTag("tr");
	    				sb_names.append("[");
	    				sb_prices.append("[");
	    				sb_details.append("[");
	    				for (Element element : elements) {
	    					Elements _elements = element.getElementsByTag("td");
	    					StringBuffer sb_temp = new StringBuffer();
	    					for (int i = 1; i < _elements.size()-1; i++) {
								sb_temp.append(_elements.get(i).text()).append(",");
							}
//	    					System.out.println(sb_temp);
	    					if(sb_temp.length() > 0){
	    						String name = sb_temp.deleteCharAt(sb_temp.length() -1).substring(0,sb_temp.indexOf(","));
	    						String details = sb_temp.substring(sb_temp.indexOf(",")+1);
	    						String prices = sb_temp.substring(sb_temp.lastIndexOf(",")+1);
//	    						String prices = StringUtils.stripEnd(sb_temp.deleteCharAt(sb_temp.length() -1).toString(),  "/");
	    						sb_names.append(name).append(";");
	    						sb_prices.append(prices).append(";");
	    						sb_details.append(details).append(";");
	    						
	    					}
						}
	    				sb_names.append("]");
	    				sb_prices.append("]");
	    				sb_details.append("]");
	    			}
	    			
	    			if(doc.getElementsByAttributeValueMatching("class", "menu-list slider-box menu-open J_list").hasText()){
	    				Element e_detail  = doc.getElementsByClass("menu-list").get(0);
	    				Elements elements = e_detail.getElementsByClass("m-item");
	    				sb_names.append("[");
	    				sb_prices.append("[");
	    				sb_details.append("[");
	    				for (Element element : elements) {
	    					String title = element.getElementsByClass("m-name").text();
	    					String price = element.getElementsByClass("price").text();
	    					Elements e_details = element.getElementsByClass("m-list");
	    					for (Element _element : e_details) {
	    						String details = _element.getElementsByTag("li").text().replaceAll(" ", ",");
	    						sb_details.append(details).deleteCharAt(sb_details.length()-1).append(";");
							}
	    			        sb_names.append(title).append(";");
	    			        sb_prices.append(price).append(";");
//	    			        System.out.println(sb_details);
	    				}
	    				sb_names.deleteCharAt(sb_names.length() - 1).append("]");
	    				sb_prices.deleteCharAt(sb_prices.length() - 1).append("]");
	    				sb_details.deleteCharAt(sb_details.length() - 1).append("]");
	    				map.put("goodsNames", sb_names.toString());
	    				map.put("goodsPrices", sb_prices.toString());
	    				map.put("goodsDetals", sb_details.toString());
	    				System.out.println("map=="+map.entrySet());
	    			}
	    		}
	    		
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("html解析出现异常");
				map.clear();
				return map;
			}
			return map;
	    }
	    
	    
	    /**
	     * 获取影院电影明细
	     * @param strHtml
	     * @return
	     */
	    public static Map<String,String> videoDetailResolver(String strHtml){
	    	Map<String,String> map = new HashMap<String, String>();
	    	JsonNode jsonNode;
	    	String newStrHtml = "";
			try {
				jsonNode = JsonUtils.parse(strHtml);
				newStrHtml = jsonNode.get("msg").toString().replace("\"","").trim();
				newStrHtml =newStrHtml.replace("\\t"," ")
						.replaceAll("&nbsp;","")
						.replaceAll("\\&quot;", "'")
						.replaceAll("\\&middot;", "·")
						.replaceAll("\\\\","\"");
				Document doc = Jsoup.parse(newStrHtml);
				if(doc.toString().contains("movie-list J-movies")){
					Element e_detail  = doc.getElementsByClass("movie-list").get(0);
					System.out.println(e_detail);
//	    		Element e_detail  = doc.getElementsByClass("movie-list").get(0);
					Elements elements = e_detail.getElementsByTag("li");
					StringBuffer sb_title = new StringBuffer();
					StringBuffer sb_pictureUrl = new StringBuffer();
					for (Element element : elements) {
						String title = element.getElementsByTag("img").attr("title");
						String pictureUrl = element.getElementsByTag("img").attr("src");
						sb_title.append(title).append(",");
						sb_pictureUrl.append(pictureUrl).append(",");
					}
					map.put("videoNames",sb_title.toString().substring(0,sb_title.length()-1));//电影名称
					map.put("videoPictrues",sb_pictureUrl.toString().substring(0,sb_pictureUrl.length()-1));//电影图片URL
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return map;
	    }
	
	
	 /**
     * 根据新闻详情url，解析新闻头条html结构片段
     * @param newsCommonModel
     * @return
     */
    public static NewsCommonModel htmlResolver(NewsCommonModel newsCommonModel) {
//    	Document doc = Jsoup.parse(html);
    	Document doc;
		try {
			System.out.println(newsCommonModel.getLinkUrl());
			doc = Jsoup.connect(newsCommonModel.getLinkUrl()).get();
			Elements ListDiv = doc.getElementsByAttributeValue("class","detail-content");
			StringBuffer sb = new StringBuffer();
			StringBuffer sb_imgUrl = new StringBuffer();
			for (Element element :ListDiv) {
				Elements links = element.getElementsByTag("p");
				for (Element link : links) {
					String linkHref ="";
					linkHref = link.select("img[src~=.(png|jpe?g)]").attr("src");
					if(linkHref == null){
						linkHref = link.select("img[src~=.(png|jpe?g)]").attr("data-src");
						
					}
//					System.out.println("_linkHref=="+linkHref);
					String linkText = link.text().trim();
					if(StringUtils.isNotBlank(linkHref)){
						sb_imgUrl.append(linkHref).append(",");
//						System.out.println("img===="+linkHref);
					}
					sb.append(linkText);
				}
				newsCommonModel.setNewsContent(sb.toString());
				newsCommonModel.setNewsImg(sb_imgUrl.toString());
				
			}
			newsCommonModel.setNewsDetail(ListDiv.toString());
//			newsCommonModel.setNewsDetail(doc.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newsCommonModel;
    }

}
