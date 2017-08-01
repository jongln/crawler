package com.demo.controller;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.JMException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.demo.core.BendiNewsPageProcessor;
import com.demo.core.CrawSitDataTask;
import com.demo.entity.CommonModel;
import com.demo.entity.NewsCommonModel;
import com.demo.service.IndexService;
import com.demo.utils.CommonHttpClient;
import com.demo.utils.CommonUtils;
import com.demo.utils.HttpProxy;
import com.demo.utils.JsoupHtmlResolver;
import com.demo.utils.ListUrlType;
import com.demo.utils.MyFutureCrawSitDataTask;
import com.demo.utils.PublicConstants;

@Controller
@RequestMapping("/index")
public class IndexController {
	@Autowired
	private IndexService indexService;
	
	
/*	*//**
	 * 进入抓取新闻列表界面
	 * @param newsModel
	 * @return
	 *//*
	@RequestMapping(value="/queryBendiNewsList")
	public ModelAndView queryBendiNewsList(NewsCommonModel newsModel){
		ModelAndView modelAndView =new ModelAndView ("/result");
		List<NewsCommonModel> newsList = indexService.queryBendiNewsList(newsModel);
		if(newsList.size() > 0){
			modelAndView.addObject("result", newsList);
		}else{
			modelAndView.addObject("result", "获取信息列表信息失败");
		}
		return modelAndView;
	}*/
	
	
	 /**
	  * 本地头条数据抓取
	  * @param type 类型：1 初始化加载新闻列表url 2手动下拉刷新加载新闻列表url
	  * @param request
	  * @return
	  * @throws JMException
	  * @throws URISyntaxException
	  */
	@RequestMapping(value="/crawBendiNewsData")
	public ModelAndView crawBendiNewsData(@RequestParam(value = "type") Integer type,HttpServletRequest request) throws JMException, URISyntaxException{
		ModelAndView mdelAndView =new ModelAndView("/result");
//		String params = paramsHandle(request.getQueryString());
		String params = request.getParameter("params");
		System.out.println(params);
		String result = "";
		if(org.springframework.util.StringUtils.isEmpty(params)){
			result = PublicConstants.URL_PARAMS_EXCEPTION;
		}else{
		//	String obj =StringEscapeUtils.unescapeJava(params);
			String publicUrl = "";
			if(type == 1){
				publicUrl = PublicConstants.AUTO_NEWS_LIST_URL;
			}else if(type == 2){
				publicUrl = PublicConstants.NEWS_LIST_URL;
			}
			//Pipeline结果输出和持久化接口
			String url = publicUrl+params;
			BendiNewsPageProcessor.crawlData(url);
			System.out.println("path-==-"+BendiNewsPageProcessor.class.getProtectionDomain()) ;
			if(PublicConstants.VERIFY_URL == false){
				result = PublicConstants.URL_FAIL;
			}else{
				List<NewsCommonModel> newsList = BendiNewsPageProcessor.newsList;
				result =PublicConstants.FAIL;
				if(newsList.size()>0){
					int state = indexService.insertNewsRecordList(newsList);
					BendiNewsPageProcessor.newsList.clear();
					if(state > 0){
						result =PublicConstants.SUCCESS;
					}
				}
			}
			PublicConstants.VERIFY_URL = true;
			mdelAndView.addObject("result",result);
		}
		return mdelAndView;
	}
	

	
	
	/**
	 * 上传选择的新闻列表信息
	 * @param newsModel
	 * @return
	 */
/*	@RequestMapping(value="/uploadBendiNewsList")
	public ModelAndView uploadBendiNewsList(List<NewsCommonModel> newsModel){
		ModelAndView modelAndView =new ModelAndView ("/result");
		List<NewsCommonModel> newsList = indexService.uploadBendiNewsList(newsModel);
		if(newsList.size() > 0){
			modelAndView.addObject("result", newsList);
		}else{
			modelAndView.addObject("result", "上传新闻列表信息失败");
		}
		return modelAndView;
	}*/
	
	
	/**
	 * 查询点评店铺列表
	 * @param newsModel
	 * @return
	 */
	@RequestMapping(value="/queryDianpingShopList")
	public ModelAndView queryDianpingShopList(List<CommonModel> newsModel){
		ModelAndView modelAndView =new ModelAndView ("/result");
		List<CommonModel> newsList = indexService.queryDianpingShopList(newsModel);
		if(newsList.size() > 0){
			modelAndView.addObject("result", newsList);
		}else{
			modelAndView.addObject("result", "上传新闻列表信息失败");
		}
		return modelAndView;
	}
	
	
	
	
	/**
	 * 大众点评店铺信息抓取
	 * @param cityName 城市名称（支持全拼）
	 * @param type 抓取类型
	 * @return
	 */
	@RequestMapping(value="/crawDianpingShopsData")
	public ModelAndView crawDianpingShopsData(@RequestParam(value = "cityName") String cityName,
			@RequestParam(value = "type") ListUrlType type){
		ModelAndView modelAndView =new ModelAndView ("/result");
		Pattern p = Pattern.compile("^[A-Za-z]+$");
		Matcher m = p.matcher(cityName);
		boolean isValid = m.matches();
		if(isValid){
			String autoUrl = PublicConstants.DIANPING_URL+"/"+cityName;
			String strHhml =CommonHttpClient.httpClient(autoUrl);
			if(StringUtils.isEmpty(strHhml)){
				strHhml = HttpProxy.doProxy(autoUrl);
				if(StringUtils.isEmpty(strHhml)){
					modelAndView.addObject("result", "获取城市代码信息失败");
					return modelAndView;
				}
			}
			int cityId = JsoupHtmlResolver.dianpingScriptCityCodeHtmlResolver(strHhml);
			if(cityId > 0){//抓取代码城市存在
				String params = ListUrlType.getValue(type);
				String listUrl = "";
				listUrl = PublicConstants.DIANPING_LIST_URL.replace("cityId", String.valueOf(cityId)).replace("params", params);
				if(type == ListUrlType.HOTEL){
					listUrl = (PublicConstants.DIANPING_URL+"/"+params).replace("cityName", cityName);
				}
				//抓取该业务类型下店铺总条数
				int countNum = getTotalCountByUrl(listUrl);
				if(countNum == 0){//该类型下不存在店铺信息
					modelAndView.addObject("result", "该类型下不存在店铺信息");
					return modelAndView;
				}
				List<CommonModel> retList = indexService.crawDianpingShopsInfo(countNum,cityId,listUrl,type);
				modelAndView.addObject("result", retList);
			
		}else{
			modelAndView.addObject("result", "请输入城市英文全拼音");
			}
		}
		return modelAndView;
	}
	
	/**
	 * 大众点评商品信息抓取
	 * @param type 抓取类型
	 * @param ids 对应抓取ID
	 * @return
	 */
	@RequestMapping(value="/crawDianpingGoodsData")
	public ModelAndView crawDianpingGoodsData(@RequestParam(value = "type") ListUrlType type,@RequestParam(value = "shopIds") String ids){
		ModelAndView modelAndView =new ModelAndView ("/result");
		int status = indexService.crawDianpingGoodsInfo(type,ids);
		if(status > 0){
			modelAndView.addObject("result", "抓取数据成功,本次共更新"+status+"条数据，详情数据库查看");
		}else{
			modelAndView.addObject("result", "抓取商品信息失败");
		}
		return modelAndView;
	}
	
	
	/**
	 * 查看大众点评商品列表信息
	 * @param id 根据ID
	 * @return
	 */
	@RequestMapping(value="/queryGoodsListInfo")
	public ModelAndView queryDianpingGoodsListInfo(@RequestParam(value = "id") Integer shopId){
		ModelAndView modelAndView =new ModelAndView ("/result");
		List<CommonModel> commonModel = indexService.queryDianpingGoodsInfo(shopId);
		modelAndView.addObject("result", commonModel);
		return modelAndView;
	}
	/**
	 * 查看大众点评商品列表中，单个商品信息
	 * @param id 根据ID
	 * @return
	 */
/*	@RequestMapping(value="/queryGoodsOneInfo")
	public ModelAndView queryDianpingGoodsOneInfo(@RequestParam(value = "id") Integer shopId){
		ModelAndView modelAndView =new ModelAndView ("/result");
		List<CommonModel> commonModel = indexService.queryDianpingGoodsInfo(shopId);
		modelAndView.addObject("result", commonModel);
		return modelAndView;
	}*/
	
	/**
	 * 选中抓取的店铺信息到店铺列表
	 * @param shopId
	 * @return
	 */
	@RequestMapping(value="/addCheckShops")
	public ModelAndView addCheckShopsToShopList(@RequestParam(value = "shopIds") String shopIds){
		ModelAndView modelAndView =new ModelAndView ("/result");
		String [] idsArry = shopIds.split(",");
		Map<String,Object> params = new HashMap<String, Object>();
        params.put("ids", idsArry);
		int state = indexService.uploadDianpingShopsInfo(params);
		modelAndView.addObject("result", state);
		return modelAndView;
	}
	
	
	/**
	 * 点评店铺商品列表中，选中抓取的商品信息到已认证店铺商品列表中
	 * @param shopId
	 * @return
	 */
	@RequestMapping(value="/addCheckGoods")
	public ModelAndView addCheckGoodsToGoodsList(@RequestParam(value = "shopId") Integer shopId,List<CommonModel> commonModel){
		ModelAndView modelAndView =new ModelAndView ("/result");
		if(0 >= commonModel.size()){
			modelAndView.addObject("result", "请选择要上传店铺的商品信息");
			return modelAndView;
		}
		List<CommonModel> shopList = indexService.queryExistClaimShopsInfo(shopId);
		if(shopList.size() == 0){
			modelAndView.addObject("result", "该商品店铺未认领，所选的商品信息不能上传");
			return modelAndView;
		}
		for (CommonModel _commonModel : commonModel) {
			_commonModel.setDbShopId(shopList.get(0).getDbShopId());
		}
		int state = indexService.uploadDianpingGoodsInfo(commonModel);
		modelAndView.addObject("result", state);
		return modelAndView;
	}
	
	
	
	
	
	/**
	 * 获取类型总条数
	 * @param listUrl
	 * @return
	 */
	private int getTotalCountByUrl(String listUrl){
		String strHtml = CommonHttpClient.httpClient(listUrl.replace("#", "1"));
		int count = 0;
		if(!StringUtils.isEmpty(strHtml)){
			count = JsoupHtmlResolver.totalCountHtmlResolver(strHtml);
		}
		return count*15;
	}
	
}
