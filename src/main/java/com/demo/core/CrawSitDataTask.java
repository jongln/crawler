package com.demo.core;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;

import com.demo.entity.CommonModel;
import com.demo.utils.CommonHttpClient;
import com.demo.utils.HttpProxy;
import com.demo.utils.JsonUtils;
import com.demo.utils.JsoupHtmlResolver;
import com.demo.utils.ListUrlType;

/**
 * 开启线程任务，根据url抓取sit数据
 * @author zhaojq
 * @since 2017-03-29
 */
public class CrawSitDataTask implements Callable<List<CommonModel>>{
	  private ListUrlType taskType;//请求类型
	  private CommonModel commonModel;//请求参数
	  /**
	   * 构造函数（任务执行回调）
	   * @param taskType
	   * @param commonModel
	   */
      public CrawSitDataTask(ListUrlType taskType,CommonModel commonModel) {
    	  this.taskType = taskType;
    	  this.commonModel = commonModel;
      }
      
      /**
       * 任务调度处理
       */
      public List<CommonModel> call() throws Exception {
			System.out.println(taskType+"线程开始****");
			List<CommonModel> commonModelList = new ArrayList<CommonModel>();
			if(taskType == ListUrlType.LISTTASK){//店铺列表处理
				System.out.println("list_url==="+commonModel.getListUrl());
		 	    String strHhml = CommonHttpClient.httpClient(commonModel.getListUrl()); //在getOrder中直接调用下面的我封装的http工具类
		 	   if(!org.springframework.util.StringUtils.isEmpty(strHhml)){
		 		   commonModel.setStrHtml(strHhml);
		 		   commonModelList = JsoupHtmlResolver.dianpingHtmlResolver(commonModel);
		 	   }else{
		 		  System.out.println("strml is null");
		 	   }
			}else if(taskType == ListUrlType.DETAILTASK){//店铺详情处理
				if(StringUtils.isNotBlank(commonModel.getDetailUrl())){
					String strDetailHtml = HttpProxy.doProxy(commonModel.getDetailUrl());
					if(StringUtils.isBlank(strDetailHtml)){//代理请求失败，重新http请求尝试（解决培训类型部分post请求验证403问题）
						strDetailHtml= CommonHttpClient.handleHttpPostInfoSend(commonModel.getDetailUrl());
					}
					if(!strDetailHtml.contains("html")){
						System.out.println("title=="+commonModel.getTitle());
						System.out.println("detail html=="+strDetailHtml);
					};
					if(!"".equals(strDetailHtml)){
						Map<String, Object> retMap  = JsoupHtmlResolver.dianpingDetailHtmlResolver(commonModel.getUrlType(),strDetailHtml);
						System.out.println("url=="+commonModel.getDetailUrl());
						System.out.println("retMap=="+retMap.entrySet());
						if(!retMap.isEmpty()){
							System.out.println("url=="+commonModel.getDetailUrl());
							if(commonModel.getUrlType() == ListUrlType.MARRIED || commonModel.getUrlType() == ListUrlType.BABY
									|| commonModel.getUrlType() == ListUrlType.HOTEL || commonModel.getUrlType() == ListUrlType.HOME){
								commonModel.setAddress(String.valueOf(retMap.get("address")));//地址
							}
							commonModel.setShopGlat(StringUtils.isNotBlank(String.valueOf(retMap.get("shopGlat")))?String.valueOf(retMap.get("shopGlat")):null);//经度
							commonModel.setShopGlng(StringUtils.isNotBlank(String.valueOf(retMap.get("shopGlng")))?String.valueOf(retMap.get("shopGlng")):null);//维度
							commonModel.setTelePhone(StringUtils.isNotBlank(String.valueOf(retMap.get("telPhone")))?String.valueOf(retMap.get("telPhone")):null);//店铺联系方式
							commonModelList.add(commonModel);
						}else{
							System.out.println("detail map is null");
						}
					}
				}
			}else if(taskType == ListUrlType.STATIC_GOODS_TASK){
				if(StringUtils.isNotBlank(commonModel.getDetailUrl())){
					String strDetailHtml = HttpProxy.doProxy(commonModel.getDetailUrl());
					if(StringUtils.isBlank(strDetailHtml)){//代理请求失败，重新http请求尝试（解决培训类型部分post请求验证403问题）
						strDetailHtml= CommonHttpClient.handleHttpPostInfoSend(commonModel.getDetailUrl());
					}
					if(!strDetailHtml.contains("html")){
						System.out.println("title=="+commonModel.getTitle());
						System.out.println("detail html=="+strDetailHtml);
					};
					if(!"".equals(strDetailHtml)){
						Map<String, Object> retMap  = JsoupHtmlResolver.dianpingGoodslHtmlResolver(commonModel.getUrlType(),strDetailHtml);
						System.out.println("url=="+commonModel.getDetailUrl());
						System.out.println("retMap=="+retMap.entrySet());
						if(!retMap.isEmpty()){
							System.out.println("url=="+commonModel.getDetailUrl());
							if(commonModel.getUrlType() == ListUrlType.BEAUTY || commonModel.getUrlType() == ListUrlType.BABY
									|| commonModel.getUrlType() == ListUrlType.TRAIN || commonModel.getUrlType() == ListUrlType.HOME
									|| commonModel.getUrlType() == ListUrlType.FEAST || commonModel.getUrlType() == ListUrlType.MARRIED){//解析获取商品信息
								commonModel.setGoodsNames(retMap.containsKey("goodsNames")?String.valueOf(retMap.get("goodsNames")):null);
								commonModel.setGoodsPrices(retMap.containsKey("goodsPrices")?String.valueOf(retMap.get("goodsPrices")):null);
								commonModel.setGoodsDetails(retMap.containsKey("goodsDetals")?String.valueOf(retMap.get("goodsDetals")):null);
							}
							commonModel.setShopType(StringUtils.isNotBlank(String.valueOf(retMap.get("shopType")))?String.valueOf(retMap.get("shopType")):null);//店铺类型代码（作用：不同大类别下存在交集类型店铺，确保每单个店铺数据能抓取到，在这里预获取店铺所属二级类别代码）
							commonModel.setMainCategoryId(retMap.containsKey("mainCategoryId")?String.valueOf(retMap.get("mainCategoryId")):null);//商品请求URL参数代码
							
							commonModelList.add(commonModel);
						}else{
							System.out.println("detail map is null");
						}
					}
				}
			}else if(taskType == ListUrlType.DYNAMIC_GOODS_TASK){//店铺商品信息动态处理
				System.out.println("taskType=="+taskType);
				String content = "";
				String menuContent = "";
				if(StringUtils.isNotBlank(commonModel.getGoodsUrl())){
					if(commonModel.getUrlType() == ListUrlType.FOOD){//美食特殊处理
						String [] strArry = commonModel.getGoodsUrl().split(";");
						menuContent = CommonHttpClient.httpClient(strArry[0]);//团购
						content = CommonHttpClient.httpClient(strArry[1]);//菜单信息
					}else{
						content = CommonHttpClient.httpClient(commonModel.getGoodsUrl());
					}
				}
				JsonNode nodes;
				if(StringUtils.isNotBlank(content)){
					StringBuffer sb_names = new StringBuffer();//商品名称
					StringBuffer sb_prices = new StringBuffer();//商品价格
					StringBuffer sb_details = new StringBuffer();//商品详情
					StringBuffer sb_pictures = new StringBuffer();//商品图片
					String names = "";
					String prices = "";
					String details = "";
					switch (commonModel.getUrlType()) {
					case FOOD:
						if(StringUtils.isNotBlank(content)){//获取美食菜单信息
							 Map<String, Object> maps = JsonUtils.parseMap(content);//JSON解析转map(按节点)
//						     String allDishes = maps.get("allDishes").toString().replaceAll("[\\[\\]]", "");//获取该节点下所有集合
						     String parentsNodes = JsonUtils.toJson(maps.get("allDishes"));// map转JSON解析
						     JsonNode nodesAllDishes = JsonUtils.parse(parentsNodes);//json解析
						     if(nodesAllDishes.size() > 0){
						    	 for (JsonNode node : nodesAllDishes.findParents("dishTagName")) {
						    		 sb_names.append(node.get("dishTagName").toString().replace("\"","").trim()).append(";");//名称
						    		 if(null == node.get("finalPrice")){//价格
						    			 sb_prices.append(node.get("finalPrice")).append(";");
						    		 }else{
						    			 sb_prices.append(node.get("finalPrice").toString().replace("\"","").trim()).append(";");
						    		 }
						    		 if(null == node.get("defaultPicURL")){//图片放入详情中
						    			 sb_details.append(node.get("defaultPicURL")).append(";");
						    		 }else{
						    			 sb_details.append(node.get("defaultPicURL").toString().replace("\"","").trim()).append(";");
						    		 }
						    	 }
						     }
						}
						if(StringUtils.isNotBlank(menuContent)){//获取美食相关团购信息
							Map<String, Object> maps = JsonUtils.parseMap(menuContent);//JSON解析转map(按节点)
						//  String allDishes = maps.get("allDishes").toString().replaceAll("[\\[\\]]", "");//获取该节点下所有集合
							String parentsNodes = JsonUtils.toJson(maps.get("dealDetails"));// map转JSON解析
							JsonNode nodesDealDetails = JsonUtils.parse(parentsNodes);//json解析
							if(nodesDealDetails.size() > 0){//基本团购
								for (JsonNode node : nodesDealDetails.findParents("productTitle")) {
									String title = node.get("productTitle").toString().replace("\"","").trim();
									if(null != node.get("price")){
										
									}
									String price = node.get("price").toString().replace("\"","").trim();
									String detail = node.get("titleDesc").toString().replace("\"","").trim();
									sb_names.append(title).append(";");
									sb_prices.append(price).append(";");
									sb_details.append(detail).append(";");
								}
								
								String moreNodes = JsonUtils.toJson(maps.get("dealMoreDetails"));// 更多团购
								JsonNode nodesMoreDetails = JsonUtils.parse(moreNodes);//json解析
								if(nodesMoreDetails.size() > 0){
									for (JsonNode jsonNode : nodesMoreDetails) {
										sb_names.append("更多团购优惠").append(";");
										sb_prices.append(jsonNode.get("price")).append(";");
										sb_details.append(jsonNode.get("desc")).append(";");
									}
								}
								names =sb_names.toString();
								prices = sb_prices.toString();
								details = sb_details.toString();
							}else{
								names = sb_names.toString().replaceAll("[\\[\\]]", "");
								prices = sb_prices.toString().replaceAll("[\\[\\]]", "");
								details = sb_details.toString().replaceAll("[\\[\\]]", "");
							}
						}
						commonModel.setGoodsNames(names);
						commonModel.setGoodsPrices(prices);
						commonModel.setGoodsDetails(details);
						commonModelList.add(commonModel);
						break;
					case VIDEO:
						System.out.println("抓取任务类别VIDEO");
						/*if(StringUtils.isNotBlank(content)){
							Map<String,String> retMap= JsoupHtmlResolver.videoDetailResolver(content);
							System.out.println(retMap.get("videoNames"));
							System.out.println(retMap.get("videoPictrues"));
							commonModel.setGoodsNames(retMap.get("videoNames"));
							commonModel.setGoodsPictures(retMap.get("videoPictrues"));
							
						}*/
						if(StringUtils.isNotBlank(content)){
							nodes = JsonUtils.parse(content);
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
								
								for (JsonNode picture : node.get("movieImage")) {//图片
									System.out.println(null != picture.getTextValue());
									System.out.println(picture.toString().indexOf("{}") > -1);
									if(null != picture.getTextValue()){
										sb_pictures.append(picture).append("|");
									}
								}
								sb_pictures.append(";");
							}
							commonModel.setGoodsNames(sb_names.toString());
							commonModel.setGoodsPrices(sb_prices.toString());
							commonModel.setGoodsDetails(sb_details.toString());
							commonModel.setGoodsPictures(sb_pictures.toString());
						}
						commonModelList.add(commonModel);
						break;
					case LIFE: case SPORT:case PLAY:case PET:case LIFESERVICE:case CAR:case HEALTH:
						/*if(StringUtils.isNotBlank(content)){
							nodes = JsonUtils.parse(content);
							StringBuffer sb_2 = new StringBuffer();
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
							commonModel.setGoodsNames(sb_2.toString().replaceAll("[\\x{10000}-\\x{10FFFF}]", ""));//去掉特殊字符（说明：因为现在我们的mysql版本低于5.5，字符集为uft8，普通的字符串或者表情都是占位3个字节utf8足够用了，但是查了下移动端的表情符号占位是4个字节。为了防止后续插入时，可能出现java.sql.SQLException: Incorrect string value: '\xF0\x9F\x8C\x9E进行替换）
						}*/
						
						if(StringUtils.isNotBlank(content)){
							Map<String, Object> maps = JsonUtils.parseMap(content);//JSON解析转map(按节点)
							String parentsNodes = JsonUtils.toJson(maps.get("dealDetails"));// map转JSON解析
							nodes = JsonUtils.parse(parentsNodes);//json解析
							if(nodes.size() > 0){
								for (JsonNode node : nodes.findParents("productTitle")) {
									sb_names.append(node.get("productTitle")).append(";");
									sb_prices.append(node.get("price")).append(";");
									sb_details.append(node.get("titleDesc")).append(";");
								}
								String moreNodes = JsonUtils.toJson(maps.get("dealMoreDetails"));// map转JSON解析
								JsonNode nodes2 = JsonUtils.parse(moreNodes);//json解析
								for (JsonNode jsonNode : nodes2) {
									sb_names.append("更多团购优惠").append(";");
									sb_prices.append(jsonNode.get("price")).append(";");
									sb_details.append(jsonNode.get("desc")).append(";");
								}
							}
							names = sb_names.toString().replace("\"","").trim();
							prices = sb_prices.toString().replace("\"","").trim();
							details = sb_details.toString().replace("\"","").trim();
							
							commonModel.setGoodsNames(names.replaceAll("[\\x{10000}-\\x{10FFFF}]", ""));
							commonModel.setGoodsPrices(prices);
							commonModel.setGoodsDetails(details);
						}
						commonModelList.add(commonModel);
						break;
					case KTV:
						if(StringUtils.isNotBlank(content)){//获取美食相关团购信息
							Map<String, Object> maps = JsonUtils.parseMap(content);//JSON解析转map(按节点)
							String parentsNodes = JsonUtils.toJson(maps.get("promoList"));// map转JSON解析
							nodes = JsonUtils.parse(parentsNodes);//json解析
							if(nodes.findParents("desc").size() > 0){
								for (JsonNode node : nodes.findParents("desc")) {
									sb_names.append(node.get("desc")).append(";");
									sb_prices.append(node.get("price")).append(";");
									sb_details.append(node.get("extInfo").get("content")).append(";");
								}
							}
							commonModel.setGoodsNames(sb_names.toString().replace("\"","").trim());
							commonModel.setGoodsPrices(sb_prices.toString().replace("\"","").trim());
							commonModel.setGoodsDetails(sb_details.toString().replace("\"","").trim());
						}
						commonModelList.add(commonModel);
						break;
						
					case HOTEL:
						if(StringUtils.isNotBlank(content)){
							nodes = JsonUtils.parse(content);
					/*		StringBuffer sb_names = new StringBuffer();
				  			StringBuffer sb_prices = new StringBuffer();
				  			StringBuffer sb_details = new StringBuffer();*/
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
//										sb_details.deleteCharAt(sb_details.length() - 1).append(";");
										sb_details.append(";");
									}
				  				}
				  			}
						/*	commonModel.setGoodsNames(sb_names.deleteCharAt(sb_names.length() - 1).toString());
							commonModel.setGoodsPrices(sb_prices.deleteCharAt(sb_prices.length() - 1).toString());
							commonModel.setGoodsDetails(sb_details.deleteCharAt(sb_details.length() - 1).toString());*/
							commonModel.setGoodsNames(sb_names.toString());
							commonModel.setGoodsPrices(sb_prices.toString());
							commonModel.setGoodsDetails(sb_details.toString());
						}
						commonModelList.add(commonModel);
						break;
					case MARRIED:
						nodes = JsonUtils.parse(content);
//						StringBuffer sb_details = new StringBuffer();
				        String detail = nodes.get("msg").toString().replace("\"","").trim();
				        System.out.println(details);
				        if(!org.springframework.util.StringUtils.isEmpty(details) && !org.springframework.util.StringUtils.isEmpty(commonModel.getGoodsDetails())){
				        	sb_details.append("[").append(commonModel.getGoodsDetails()).append("]")
				        	.append("[").append(details).append("]");
				        	commonModel.setGoodsDetails(sb_details.toString());
				        }else if(!org.springframework.util.StringUtils.isEmpty(details) && org.springframework.util.StringUtils.isEmpty(commonModel.getGoodsDetails())){
				        	commonModel.setGoodsDetails(sb_details.toString());
				        }
				        commonModelList.add(commonModel);
					}
				}
			}
		   	System.out.println(taskType+"结束线程****");
		   	return commonModelList;
		}
      
}
