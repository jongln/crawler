package com.demo.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;

import com.demo.entity.NewsCommonModel;
import com.demo.utils.CommonUtils;
import com.demo.utils.JsoupHtmlResolver;
import com.demo.utils.PublicConstants;

/**
 * 抓取本地头条新闻数据核心类
 * @author zhaojq
 * @since 2017-03-14
 */
public class BendiNewsPageProcessor implements PageProcessor{
	
	
	private static final Logger logger = LoggerFactory.getLogger(BendiNewsPageProcessor.class);

	//对要抓取网站的请求进行相关配置，包括编码、抓取间隔、重试次数等
	private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
	
	//默认初始化加载新闻列表
	 public static final String AUTO_NEWS_LIST_URL = "http://web430.benditoutiao\\.com:80//channel/autoRefreshNewsList.*";
	 
	//手动下拉加载新闻列表
	 public static final String NEWS_LIST_URL = "http://web430.benditoutiao\\.com:80//channel/newslist.*";
	 
	 //保存图片路径地址
	 public static final String IMG_FILE_PATH = "D:\\webmagic\\saveImg\\";
	 
	//消息总列表
	public static List<NewsCommonModel> newsList = new ArrayList<NewsCommonModel>();
	 private BendiNewsPageProcessor(){
	 }
			 
	public Site getSite() {
		return site;
	}
	
	
	
	public static void crawlData(String url){
		logger.info("开始抓取数据******************");
//		try {
			//Spider是爬虫的入口类
			Spider bendiSpider = Spider.create(new BendiNewsPageProcessor()).addUrl(url)
					//Pipeline结果输出和持久化接口
					.addPipeline(new ConsolePipeline());//结果输出到控制台
//				.addPipeline(new JsonFilePipeline("D:\\webmagic\\saveHtml"));//输出保存到本地（文件格式.json）
			//添加到JMT监控中
//			SpiderMonitor.instance().register(bendiSpider);
			//设置线程数
			bendiSpider.thread(2);
			bendiSpider.run();
			bendiSpider.close();
		/*} catch (JMException e) {
			e.printStackTrace();
		}*/
		logger.info("抓取数据结束******************");
	}
	
	/**
	 * 抓取新闻处理（webmagic内置定义方法）
	 */
    public void process(Page page) {
    	System.out.println(page.getUrl().regex(AUTO_NEWS_LIST_URL).match());
    	if(page.getUrl().regex(AUTO_NEWS_LIST_URL).match() 
    			|| page.getUrl().regex(NEWS_LIST_URL).match()){
    		String data = page.getJson().jsonPath("data").toString();
    		JSONObject obj_json = new JSONObject(data);
    		if(obj_json.length() == 0){
    			System.out.println("该链接请求已失效，请重新获取");
    			PublicConstants.VERIFY_URL= false;
    			return;
    		}
    		
    		//step1 抓取新闻列表信息
    		List<String> list = new JsonPathSelector("$.data[*].items").selectList(page.getRawText());
    		System.out.println("list=="+list);
    		for (int i = 0; i < list.size(); i++) {
    			NewsCommonModel newsCommonModel = new NewsCommonModel();
    			JSONObject json = new JSONObject(list.get(i));
    			newsCommonModel.setNewsTitle(json.get("news_title").toString());
    			newsCommonModel.setNewsFrom(json.get("news_from").toString());
    			newsCommonModel.setNewsDate(json.get("news_date").toString());
    			newsCommonModel.setLinkUrl(json.get("link_url").toString());
    			//step 2 根据新闻列表获取新闻详情
    			if(StringUtils.isNotBlank(newsCommonModel.getLinkUrl())){
    				newsCommonModel = JsoupHtmlResolver.htmlResolver(newsCommonModel);
    			}
    			//step3 根据抓取的新闻图片url,保存到本地文件
    			if(StringUtils.isNotBlank(newsCommonModel.getNewsImg())){
    				String saveImgUrl = CommonUtils.saveImgByURL(newsCommonModel.getNewsImg(),IMG_FILE_PATH);
    				newsCommonModel.setNewsImg(saveImgUrl);
    			}
    			System.out.println(newsCommonModel.getNewsTitle());
    			System.out.println(newsCommonModel.getNewsFrom());
    			System.out.println(newsCommonModel.getNewsDetail());
    			newsList.add(newsCommonModel);
    		}
    		System.out.println("size==="+newsList.size());
//    		bendiNewsService.insertNewsRecordList(newsList);
    	}
    }
    

}
