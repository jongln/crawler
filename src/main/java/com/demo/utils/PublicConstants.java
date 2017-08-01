package com.demo.utils;

public class PublicConstants {
	//本地头条 默认初始化加载新闻列表
	 public static String AUTO_NEWS_LIST_URL = "http://web430.benditoutiao.com:80//channel/autoRefreshNewsList?";
	 
	//本地头条 手动下拉加载新闻列表
	 public static String NEWS_LIST_URL = "http://web430.benditoutiao.com:80//channel/newslist?";
	 
	 /**
	  * 大众点评URL
	  */
	 public static String DIANPING_URL = "http://www.dianping.com";
	 
	 /**
	  * 大众点评店铺URL
	  */
	 public static String DIANPING_SHOP_URL = "http://www.dianping.com/shop/#";
	 
	 /**
	  * 大众点评通用列表URL
	  */
	 public static String DIANPING_LIST_URL = "http://www.dianping.com/search/category/cityId/params";
	 
	 public static String SUCCESS = "数据抓取成功";
	 
	 public static String FAIL = "数据抓取异常";
	 
	 public static String URL_FAIL = "该链接请求已失效，请重新获取";
	 
	 public static String URL_PARAMS_EXCEPTION = "请求链接参数有异常，请重新抓取";
	 
	 public static boolean VERIFY_URL = true;
	 
	 /**
	  * 基本商品详情URL（美食、电影 获取店铺商品信息）
	  */
	public static String GENERAL_GOODS_DETAIL_URL= DIANPING_URL+"/ajax/json/shopDynamic/shopTabs?"
			+ "shopId=SHOP_ID&cityId=CITY_ID&shopName=&power=5&mainCategoryId=MAIN_CATEGORY_ID&shopType=SHOP_TYPE&shopCityId=CITY_ID";
	
	/**
	 * 通用商品团购详情URL（休闲娱乐、KTV、运动健身、宠物）
	 */
	public static String GENERAL_GROUP_DETAIL_URL= DIANPING_URL+"/ajax/json/shopDynamic/promoInfo?"
			+ "shopId=SHOP_ID&cityId=CITY_ID&shopName=&power=5&mainCategoryId=MAIN_CATEGORY_ID&shopType=SHOP_TYPE&shopCityId=CITY_ID";
	
	
	/**
	 * 酒店商品详情URL
	 */
	public static String HOTEL_DETAIL_URL= DIANPING_URL+"/hotelproduct/pc/hotelPrepayAndOtaGoodsList?shopId=SHOP_ID";
	

	/**
	 * 电影演出详情URL（获取电影详情信息）
	 */
	public static String VIDEO_DETAIL_URL= DIANPING_URL+"/ajax/json/shop/movie/showlist?shopId=SHOP_ID&date=DATE";
	
	/**
	 * 结婚团购商品详情URL（获取电影详情信息）
	 */
	public static String MARRIED_DETAIL_URL= DIANPING_URL+"/ajax/shop/wedding/promo?actionType=get&shopId=SHOP_ID";
	

}
