package com.demo.utils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import com.demo.entity.CommonModel;

/**
 * 公有重写FutureTask方法，对异步线程结果计算进行调度（以后所有task都调用此方法）
 * @author zhaojq
 * @since 2017-04-01
 */
public class MyFutureCrawSitDataTask extends FutureTask<List<CommonModel>> {

	public MyFutureCrawSitDataTask(Callable<List<CommonModel>> callable) {
		super(callable);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void done() {
		//调度完成打印日志输出
		// TODO Auto-generated method stub
		System.out.println("线程全部请求完成>>>>>>>>");
		super.done();
	}

}
