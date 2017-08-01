package com.demo.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 抓取页面时，用到的结构化实体类
 * @author zhaojq
 * @since 2017-03-07
 */

public class NewsCommonModel {
	
    private String newsTitle;//新闻标题
    private String newsFrom;//新闻来源
    private String newsDate;//发布日期
    private String linkUrl;//详情链接
    private String newsDetail;//新闻详情页面
    private String newsContent;//新闻内容
    private String newsImg;//新闻图片
    
    public List<NewsCommonModel> newsList = new ArrayList<NewsCommonModel>();
    
	public List<NewsCommonModel> getNewsList() {
		return newsList;
	}
	public void setNewsList(List<NewsCommonModel> newsList) {
		this.newsList = newsList;
	}
	public String getNewsDetail() {
		return newsDetail;
	}
	public void setNewsDetail(String newsDetail) {
		this.newsDetail = newsDetail;
	}
	public String getNewsImg() {
		return newsImg;
	}
	public void setNewsImg(String newsImg) {
		this.newsImg = newsImg;
	}
	public String getNewsTitle() {
		return newsTitle;
	}
	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}
	public String getNewsFrom() {
		return newsFrom;
	}
	public void setNewsFrom(String newsFrom) {
		this.newsFrom = newsFrom;
	}
	public String getNewsDate() {
		return newsDate;
	}
	public void setNewsDate(String newsDate) {
		this.newsDate = newsDate;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public String getNewsContent() {
		return newsContent;
	}
	public void setNewsContent(String newsContent) {
		this.newsContent = newsContent;
	}
    
	
}
