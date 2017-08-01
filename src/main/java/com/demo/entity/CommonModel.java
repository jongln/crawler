package com.demo.entity;

import com.demo.utils.ListUrlType;

/**
 * 大众点评用到的Model
 * @author zhaojq
 * @since 2017-03-31
 */
public class CommonModel {
	private String listUrl;//列表URL
	private String detailUrl;//详情URL
	private String strHtml;//html内容
	private ListUrlType urlType;//抓取类别
	private String goodsUrl;//商品详细URL
	private String mainCategoryId;//菜单类型（美食分类里）
	private String shopType;//店铺类别code
	
	
	//抓取的需要存库数据
	private Integer id;//ID
	private String businessType;//抓取业务类型
	private String shopId;//店铺Id
	private String type;//店铺类型
	private String title;//店铺名称
	private String picture;//店铺图片
	private String address;//店铺地址
	private String shopGlat;//店铺经度
	private String shopGlng;//店铺维度
	private String telePhone;//店铺联系方式
	
	private String goodsNames;//商品名称
	private String goodsPrices;//商品价格
	private String goodsDetails;//商品详情
	private String goodsPictures;//商品图片
	
	//后面代码合并删掉
	private Integer dbShopId;//对应数据库店铺Id
	private Integer shopName;//店铺名称
	
	public Integer getShopName() {
		return shopName;
	}

	public void setShopName(Integer shopName) {
		this.shopName = shopName;
	}

	public Integer getDbShopId() {
		return dbShopId;
	}

	public void setDbShopId(Integer dbShopId) {
		this.dbShopId = dbShopId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGoodsPrices() {
		return goodsPrices;
	}

	public void setGoodsPrices(String goodsPrices) {
		this.goodsPrices = goodsPrices;
	}

	public String getGoodsDetails() {
		return goodsDetails;
	}

	public void setGoodsDetails(String goodsDetails) {
		this.goodsDetails = goodsDetails;
	}

	public String getShopType() {
		return shopType;
	}

	public void setShopType(String shopType) {
		this.shopType = shopType;
	}

	public String getGoodsUrl() {
		return goodsUrl;
	}

	public void setGoodsUrl(String goodsUrl) {
		this.goodsUrl = goodsUrl;
	}

	public String getGoodsNames() {
		return goodsNames;
	}

	public void setGoodsNames(String goodsNames) {
		this.goodsNames = goodsNames;
	}

	public String getGoodsPictures() {
		return goodsPictures;
	}

	public void setGoodsPictures(String goodsPictures) {
		this.goodsPictures = goodsPictures;
	}

	public String getMainCategoryId() {
		return mainCategoryId;
	}

	public void setMainCategoryId(String mainCategoryId) {
		this.mainCategoryId = mainCategoryId;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public ListUrlType getUrlType() {
		return urlType;
	}

	public void setUrlType(ListUrlType urlType) {
		this.urlType = urlType;
	}

	public String getTelePhone() {
		return telePhone;
	}

	public void setTelePhone(String telePhone) {
		this.telePhone = telePhone;
	}

	public String getShopGlat() {
		return shopGlat;
	}

	public void setShopGlat(String shopGlat) {
		this.shopGlat = shopGlat;
	}

	public String getShopGlng() {
		return shopGlng;
	}

	public void setShopGlng(String shopGlng) {
		this.shopGlng = shopGlng;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetailUrl() {
		return detailUrl;
	}

	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getListUrl() {
		return listUrl;
	}

	public void setListUrl(String listUrl) {
		this.listUrl = listUrl;
	}

	public String getStrHtml() {
		return strHtml;
	}

	public void setStrHtml(String strHtml) {
		this.strHtml = strHtml;
	}
	
}
