package com.xzsoft.xsr.core.service.impl.repCheck;

import java.util.ArrayList;
import java.util.List;

import com.xzsoft.xsr.core.modal.CheckFormula;

/**
 * 报表稽核-在解析公式时，启动的线程
 * 目前只在使用redis稽核时使用多线程
 * @author ZhouSuRong
 * @date 2016-3-3
 */
public class CheckThread extends Thread{
	//解析稽核公式的类，如果定义为抽象稽核类也可以，那么该稽核线程类为通用类，但考虑到实际，现在常规稽核还没有使用多线程
	private AnalyzeRedisCheckFormula analyze; 
	//线程需要稽核的公式
	private List<CheckFormula> formulaList;
	//解析是需要的参数
	private String[] params;
	
	public List<CheckFormula> resultList = new ArrayList<CheckFormula>();
	public String resultMsg = "ok";
	//运行结束标志
	public boolean flag = false;

	public CheckThread() {

	}

	public CheckThread(AnalyzeRedisCheckFormula analyze, List<CheckFormula> formulaList, String[] params) {
		this.analyze = analyze;
		this.formulaList = formulaList;
		this.params = params;
	}

	public void run() {
		resultList = analyze.analyzeFormula(formulaList, params);
		//运行完成flag赋值为frue
		flag = true;
	}
	
}

