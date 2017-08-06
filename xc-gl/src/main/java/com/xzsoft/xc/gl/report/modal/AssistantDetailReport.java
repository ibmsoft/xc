package com.xzsoft.xc.gl.report.modal;


/**
 * @description:辅助明细统计
 * @author tangxl
 * @date 2016年4月01日
 */
public class AssistantDetailReport implements Cloneable{
	private String V_TEMPLATE_TYPE;
	private String CCID;   
	//会计期间
	private String PERIOD_CODE;
	//凭证头id
	private String V_HEAD_ID;
	//会计日期
	private String ACCOUNT_DATE;
	//科目信息
	private String ACC_ID;
	private String ACC_CODE;
	private String ACC_NAME;
	//凭证号
	private String VOUCHER_NUM;
	//摘要
	private String SUMMARY;
	//借方金额
	private double JF_JE;
	//借方未过账金额
	private double T_JF_JE;
	//贷方金额
	private double DF_JE;
	//贷方未过账金额
	private double T_DF_JE;
	//方向
	private String BALANCE_DIRECTION;
	//方向
    private String BALANCE_DIRECTION_NAME;
    
	//余额
	private double QC_YE;
	//余额未过账
	private double T_QC_YE;
    //本期累计发生额<本月合计>借方
    private double BQLJ_JF;
    //本期累计发生额<本月合计>贷方
    private double BQLJ_DF;
    //本月累计发生额<本月合计>借方 未过账
    private double T_BQLJ_JF;
    //本月累计发生额<本月合计>借方 未过账
    private double T_BQLJ_DF;
    //本年累计发生额<本年累计>借方
    private double BNLJ_JF;
    //本年累计发生额<本年累计>贷方
    private double BNLJ_DF;
    //本年累计发生额<本年累计>借方 未过账
    private double T_BNLJ_JF;
    //本年累计发生额<本年累计>贷方 未过账
    private double T_BNLJ_DF;
    //期末余额
    private double QM_YE;
    //期末余额<未过账>
    private double T_QM_YE;
    //是否为计算时添加的记录
    private String isAddFlag;
    
	public String getCCID() {
		return CCID;
	}
	public void setCCID(String cCID) {
		CCID = cCID;
	}
	public String getPERIOD_CODE() {
		return PERIOD_CODE;
	}
	public void setPERIOD_CODE(String pERIOD_CODE) {
		PERIOD_CODE = pERIOD_CODE;
	}
	public String getV_HEAD_ID() {
		return V_HEAD_ID;
	}
	public void setV_HEAD_ID(String v_HEAD_ID) {
		V_HEAD_ID = v_HEAD_ID;
	}
	public String getACCOUNT_DATE() {
		return ACCOUNT_DATE;
	}
	public void setACCOUNT_DATE(String aCCOUNT_DATE) {
		ACCOUNT_DATE = aCCOUNT_DATE;
	}
	public String getACC_ID() {
		return ACC_ID;
	}
	public void setACC_ID(String aCC_ID) {
		ACC_ID = aCC_ID;
	}
	public String getACC_CODE() {
		return ACC_CODE;
	}
	public void setACC_CODE(String aCC_CODE) {
		ACC_CODE = aCC_CODE;
	}
	public String getACC_NAME() {
		return ACC_NAME;
	}
	public void setACC_NAME(String aCC_NAME) {
		ACC_NAME = aCC_NAME;
	}
	public String getVOUCHER_NUM() {
		return VOUCHER_NUM;
	}
	public void setVOUCHER_NUM(String vOUCHER_NUM) {
		VOUCHER_NUM = vOUCHER_NUM;
	}
	public String getSUMMARY() {
		return SUMMARY;
	}
	public void setSUMMARY(String sUMMARY) {
		SUMMARY = sUMMARY;
	}
	public double getJF_JE() {
		return JF_JE;
	}
	public void setJF_JE(double jF_JE) {
		JF_JE = jF_JE;
	}
	public double getT_JF_JE() {
		return T_JF_JE;
	}
	public void setT_JF_JE(double t_JF_JE) {
		T_JF_JE = t_JF_JE;
	}
	public double getDF_JE() {
		return DF_JE;
	}
	public void setDF_JE(double dF_JE) {
		DF_JE = dF_JE;
	}
	public double getT_DF_JE() {
		return T_DF_JE;
	}
	public void setT_DF_JE(double t_DF_JE) {
		T_DF_JE = t_DF_JE;
	}
	public String getBALANCE_DIRECTION() {
		return BALANCE_DIRECTION;
	}
	public void setBALANCE_DIRECTION(String bALANCE_DIRECTION) {
		BALANCE_DIRECTION = bALANCE_DIRECTION;
	}
	public String getBALANCE_DIRECTION_NAME() {
		return BALANCE_DIRECTION_NAME;
	}
	public void setBALANCE_DIRECTION_NAME(String bALANCE_DIRECTION_NAME) {
		BALANCE_DIRECTION_NAME = bALANCE_DIRECTION_NAME;
	}
	public double getQC_YE() {
		return QC_YE;
	}
	public void setQC_YE(double qC_YE) {
		QC_YE = qC_YE;
	}
	public double getT_QC_YE() {
		return T_QC_YE;
	}
	public void setT_QC_YE(double t_QC_YE) {
		T_QC_YE = t_QC_YE;
	}
	public double getBQLJ_JF() {
		return BQLJ_JF;
	}
	public void setBQLJ_JF(double bQLJ_JF) {
		BQLJ_JF = bQLJ_JF;
	}
	public double getBQLJ_DF() {
		return BQLJ_DF;
	}
	public void setBQLJ_DF(double bQLJ_DF) {
		BQLJ_DF = bQLJ_DF;
	}
	public double getT_BQLJ_JF() {
		return T_BQLJ_JF;
	}
	public void setT_BQLJ_JF(double t_BQLJ_JF) {
		T_BQLJ_JF = t_BQLJ_JF;
	}
	public double getT_BQLJ_DF() {
		return T_BQLJ_DF;
	}
	public void setT_BQLJ_DF(double t_BQLJ_DF) {
		T_BQLJ_DF = t_BQLJ_DF;
	}
	public double getBNLJ_JF() {
		return BNLJ_JF;
	}
	public void setBNLJ_JF(double bNLJ_JF) {
		BNLJ_JF = bNLJ_JF;
	}
	public double getBNLJ_DF() {
		return BNLJ_DF;
	}
	public void setBNLJ_DF(double bNLJ_DF) {
		BNLJ_DF = bNLJ_DF;
	}
	public double getT_BNLJ_JF() {
		return T_BNLJ_JF;
	}
	public void setT_BNLJ_JF(double t_BNLJ_JF) {
		T_BNLJ_JF = t_BNLJ_JF;
	}
	public double getT_BNLJ_DF() {
		return T_BNLJ_DF;
	}
	public void setT_BNLJ_DF(double t_BNLJ_DF) {
		T_BNLJ_DF = t_BNLJ_DF;
	}
	public double getQM_YE() {
		return QM_YE;
	}
	public void setQM_YE(double qM_YE) {
		QM_YE = qM_YE;
	}
	public double getT_QM_YE() {
		return T_QM_YE;
	}
	public void setT_QM_YE(double t_QM_YE) {
		T_QM_YE = t_QM_YE;
	}
	public String getIsAddFlag() {
		return isAddFlag;
	}
	public void setIsAddFlag(String isAddFlag) {
		this.isAddFlag = isAddFlag;
	}
	@Override
	public AssistantDetailReport clone(){
		AssistantDetailReport obj = null;
		try {
			obj =(AssistantDetailReport) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return obj;
	}
	public String getV_TEMPLATE_TYPE() {
		return V_TEMPLATE_TYPE;
	}
	public void setV_TEMPLATE_TYPE(String v_TEMPLATE_TYPE) {
		V_TEMPLATE_TYPE = v_TEMPLATE_TYPE;
	} 
}
