package com.xzsoft.xc.gl.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.xzsoft.xc.gl.report.modal.VDataReport;


public class VoucherA4PrintUtil{
	private final static double AMOUNT_VAL = 0;  //默认数值 
	/**
	 * 
	 * @title: remedyVoucherData
	 * @description:根据查询所得的记录数，动态补全空白数字
	 * @author  tangxl
	 * @date    2016年4月20日
	 * @param voucherList
	 */
	public static void remedyVoucherData(List<VDataReport> voucherList){
		//计算出要补足的空白数，按8的倍数来补全
		//明细行+合计行=8行
		int dataSize = voucherList.size();
		int remedySize = 0;
		remedySize = 8-(dataSize%8)-1; //11%8=3,8-3=5,5-1=4
		VDataReport obj = new VDataReport();
		BeanUtils.copyProperties(voucherList.get(0), obj);
		//显示值部分置成空
		obj.setSUMMARY("");
		obj.setCCID_NAME("");
		obj.setFOREIGN_NAME("");
		obj.setDIMENSION_NAME("");
		//金额值部分置成0
		obj.setACCOUNT_DR(AMOUNT_VAL);
		obj.setACCOUNT_CR(AMOUNT_VAL);
		obj.setT_ACCOUNT_DR(AMOUNT_VAL);
		obj.setT_ACCOUNT_CR(AMOUNT_VAL);
		obj.setAMOUNT(AMOUNT_VAL);
		obj.setT_AMOUNT(AMOUNT_VAL);
		obj.setENTER_AMOUNT(AMOUNT_VAL);
		obj.setT_ENTER_AMOUNT(AMOUNT_VAL);
		obj.setENTER_DR(AMOUNT_VAL);
		obj.setENTER_CR(AMOUNT_VAL);
		obj.setT_ENTER_DR(AMOUNT_VAL);
		obj.setT_ENTER_CR(AMOUNT_VAL);
		obj.setEXCHANGE_RATE(AMOUNT_VAL);
		//开始循环补数
		for(int i=0;i<remedySize;i++){
			VDataReport tmpObj = new VDataReport();
			BeanUtils.copyProperties(obj, tmpObj);
			voucherList.add(tmpObj);
		}
	}
	/**
	 * 
	 * @title:      formatA4Print
	 * @description:根据查询所得的记录数，动态补全空白数字
	 * @author      tangxl
	 * @date        2016年4月20日
	 * @param       voucherList
	 * @param       headerIds
	 */
	public static List<VDataReport> formatA4Print(List<VDataReport> voucherList,String[] headerIds){
		//根据v_header_id 给voucherList分类，得到每一个凭证的明细行
		 List<VDataReport> finalList = new ArrayList<VDataReport>();
		for(int i=0;i<headerIds.length;i++){
			 List<VDataReport> tmpList = new ArrayList<VDataReport>();
			 String headerId = headerIds[i].replaceAll("'", "");
			 for(VDataReport t:voucherList){
				 if(headerId.equals(t.getV_HEAD_ID()))
					 tmpList.add(t);
			 }
		//对每一个凭证的明细行进行补空行
			 remedyVoucherData(tmpList);
	    //添加到返回结果集中
			 for(VDataReport p:tmpList){
				 finalList.add(p);
			 }
		 }
		return finalList;	
	}
}
