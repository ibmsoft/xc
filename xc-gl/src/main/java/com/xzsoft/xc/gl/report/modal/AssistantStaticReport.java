package com.xzsoft.xc.gl.report.modal;
/**
 * @description:辅助统计报表
 * @author tangxl
 * @date 2016年3月30日
 */
public class AssistantStaticReport implements Comparable<AssistantStaticReport> {
	private String CCID;   
	//科目
	private String ACC_ID;
	private String ACC_CODE;
	private String ACC_NAME;
	//供应商
	private String VENDOR_ID;
	private String VENDOR_CODE;
	private String VENDOR_NAME;
	//客户
	private String CUSTOMER_ID;
	private String CUSTOMER_CODE;
	private String CUSTOMER_NAME;
	//产品
	private String PRODUCT_ID;
	private String PRODUCT_CODE;
	private String PRODUCT_NAME;
	//项目
	private String PROJECT_ID;
	private String PROJECT_CODE;
	private String PROJECT_NAME;
	//内部往来
	private String ORG_ID;
	private String ORG_CODE;
	private String ORG_NAME;
	//成本中心
	private String DEPT_ID;
	private String DEPT_CODE;
	private String DEPT_NAME;
	//个人往来
	private String EMP_ID;
	private String EMP_CODE;
	private String EMP_NAME;
	//自定义1
	private String CUSTOM1_ID;
	private String CUSTOM1_CODE;
	private String CUSTOM1_NAME;
	//自定义2
	private String CUSTOM2_ID;
	private String CUSTOM2_CODE;
	private String CUSTOM2_NAME;
	//自定义3
	private String CUSTOM3_ID;
	private String CUSTOM3_CODE;
	private String CUSTOM3_NAME;
	//自定义4
	private String CUSTOM4_ID;
	private String CUSTOM4_CODE;
	private String CUSTOM4_NAME;
	//方向
	private String BALANCE_DIRECTION;
	//本位币币种
	private String CURRENCY_CODE;
	//科目币种
	private String DEFAULT_CURRENCY;
	//期初数量、原币、金额
	private double QC_SL;
	private double QCYB_JE;
	private double QC_JE;
	//本期借方数量、原币、金额
	private double BQJF_SL;
	private double BQJF_YB;
	private double BQJF_JE;
	//本期贷方数量、原币、金额
	private double BQDF_SL;
	private double BQDF_YB;
	private double BQDF_JE;
	//本年累计借方数量、原币、金额
	private double BNLJJF_SL;
	private double BNLJJF_YB;
	private double BNLJJF_JE;
	//本年累计贷方数量、原币、金额
	private double BNLJDF_SL;
	private double BNLJDF_YB;
	private double BNLJDF_JE;
	//期末数量、原币、金额
	private double QM_SL;
	private double QMYB_JE;
	private double QM_JE;
	//期初数量、原币、金额<未过账>
	private double T_QC_SL;
	private double T_QCYB_JE;
	private double T_QC_JE;
	//本期借方数量、原币、金额<未过账>
	private double T_BQJF_SL;
	private double T_BQJF_YB;
	private double T_BQJF_JE;
	//本期贷方数量、原币、金额<未过账>
	private double T_BQDF_SL;
	private double T_BQDF_YB;
	private double T_BQDF_JE;
	//本年累计借方数量、原币、金额<未过账>
	private double T_BNLJJF_SL;
	private double T_BNLJJF_YB;
	private double T_BNLJJF_JE;
	//本年累计贷方数量、原币、金额<未过账>
	private double T_BNLJDF_SL;
	private double T_BNLJDF_YB;
	private double T_BNLJDF_JE;
	//期末数量、原币、金额<未过账>
	private double T_QM_SL;
	private double T_QMYB_JE;
	private double T_QM_JE;
	public String getCCID() {
		return CCID;
	}
	public void setCCID(String cCID) {
		CCID = cCID;
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
	public String getVENDOR_ID() {
		return VENDOR_ID;
	}
	public void setVENDOR_ID(String vENDOR_ID) {
		VENDOR_ID = vENDOR_ID;
	}
	public String getVENDOR_CODE() {
		return VENDOR_CODE;
	}
	public void setVENDOR_CODE(String vENDOR_CODE) {
		VENDOR_CODE = vENDOR_CODE;
	}
	public String getVENDOR_NAME() {
		return VENDOR_NAME;
	}
	public void setVENDOR_NAME(String vENDOR_NAME) {
		VENDOR_NAME = vENDOR_NAME;
	}
	public String getCUSTOMER_ID() {
		return CUSTOMER_ID;
	}
	public void setCUSTOMER_ID(String cUSTOMER_ID) {
		CUSTOMER_ID = cUSTOMER_ID;
	}
	public String getCUSTOMER_CODE() {
		return CUSTOMER_CODE;
	}
	public void setCUSTOMER_CODE(String cUSTOMER_CODE) {
		CUSTOMER_CODE = cUSTOMER_CODE;
	}
	public String getCUSTOMER_NAME() {
		return CUSTOMER_NAME;
	}
	public void setCUSTOMER_NAME(String cUSTOMER_NAME) {
		CUSTOMER_NAME = cUSTOMER_NAME;
	}
	public String getPRODUCT_ID() {
		return PRODUCT_ID;
	}
	public void setPRODUCT_ID(String pRODUCT_ID) {
		PRODUCT_ID = pRODUCT_ID;
	}
	public String getPRODUCT_CODE() {
		return PRODUCT_CODE;
	}
	public void setPRODUCT_CODE(String pRODUCT_CODE) {
		PRODUCT_CODE = pRODUCT_CODE;
	}
	public String getPRODUCT_NAME() {
		return PRODUCT_NAME;
	}
	public void setPRODUCT_NAME(String pRODUCT_NAME) {
		PRODUCT_NAME = pRODUCT_NAME;
	}
	public String getPROJECT_ID() {
		return PROJECT_ID;
	}
	public void setPROJECT_ID(String pROJECT_ID) {
		PROJECT_ID = pROJECT_ID;
	}
	public String getPROJECT_CODE() {
		return PROJECT_CODE;
	}
	public void setPROJECT_CODE(String pROJECT_CODE) {
		PROJECT_CODE = pROJECT_CODE;
	}
	public String getPROJECT_NAME() {
		return PROJECT_NAME;
	}
	public void setPROJECT_NAME(String pROJECT_NAME) {
		PROJECT_NAME = pROJECT_NAME;
	}
	public String getORG_ID() {
		return ORG_ID;
	}
	public void setORG_ID(String oRG_ID) {
		ORG_ID = oRG_ID;
	}
	public String getORG_CODE() {
		return ORG_CODE;
	}
	public void setORG_CODE(String oRG_CODE) {
		ORG_CODE = oRG_CODE;
	}
	public String getORG_NAME() {
		return ORG_NAME;
	}
	public void setORG_NAME(String oRG_NAME) {
		ORG_NAME = oRG_NAME;
	}
	public String getDEPT_ID() {
		return DEPT_ID;
	}
	public void setDEPT_ID(String dEPT_ID) {
		DEPT_ID = dEPT_ID;
	}
	public String getDEPT_CODE() {
		return DEPT_CODE;
	}
	public void setDEPT_CODE(String dEPT_CODE) {
		DEPT_CODE = dEPT_CODE;
	}
	public String getDEPT_NAME() {
		return DEPT_NAME;
	}
	public void setDEPT_NAME(String dEPT_NAME) {
		DEPT_NAME = dEPT_NAME;
	}
	public String getEMP_ID() {
		return EMP_ID;
	}
	public void setEMP_ID(String eMP_ID) {
		EMP_ID = eMP_ID;
	}
	public String getEMP_CODE() {
		return EMP_CODE;
	}
	public void setEMP_CODE(String eMP_CODE) {
		EMP_CODE = eMP_CODE;
	}
	public String getEMP_NAME() {
		return EMP_NAME;
	}
	public void setEMP_NAME(String eMP_NAME) {
		EMP_NAME = eMP_NAME;
	}
	public String getCUSTOM1_ID() {
		return CUSTOM1_ID;
	}
	public void setCUSTOM1_ID(String cUSTOM1_ID) {
		CUSTOM1_ID = cUSTOM1_ID;
	}
	public String getCUSTOM1_CODE() {
		return CUSTOM1_CODE;
	}
	public void setCUSTOM1_CODE(String cUSTOM1_CODE) {
		CUSTOM1_CODE = cUSTOM1_CODE;
	}
	public String getCUSTOM1_NAME() {
		return CUSTOM1_NAME;
	}
	public void setCUSTOM1_NAME(String cUSTOM1_NAME) {
		CUSTOM1_NAME = cUSTOM1_NAME;
	}
	public String getCUSTOM2_ID() {
		return CUSTOM2_ID;
	}
	public void setCUSTOM2_ID(String cUSTOM2_ID) {
		CUSTOM2_ID = cUSTOM2_ID;
	}
	public String getCUSTOM2_CODE() {
		return CUSTOM2_CODE;
	}
	public void setCUSTOM2_CODE(String cUSTOM2_CODE) {
		CUSTOM2_CODE = cUSTOM2_CODE;
	}
	public String getCUSTOM2_NAME() {
		return CUSTOM2_NAME;
	}
	public void setCUSTOM2_NAME(String cUSTOM2_NAME) {
		CUSTOM2_NAME = cUSTOM2_NAME;
	}
	public String getCUSTOM3_ID() {
		return CUSTOM3_ID;
	}
	public void setCUSTOM3_ID(String cUSTOM3_ID) {
		CUSTOM3_ID = cUSTOM3_ID;
	}
	public String getCUSTOM3_CODE() {
		return CUSTOM3_CODE;
	}
	public void setCUSTOM3_CODE(String cUSTOM3_CODE) {
		CUSTOM3_CODE = cUSTOM3_CODE;
	}
	public String getCUSTOM3_NAME() {
		return CUSTOM3_NAME;
	}
	public void setCUSTOM3_NAME(String cUSTOM3_NAME) {
		CUSTOM3_NAME = cUSTOM3_NAME;
	}
	public String getCUSTOM4_ID() {
		return CUSTOM4_ID;
	}
	public void setCUSTOM4_ID(String cUSTOM4_ID) {
		CUSTOM4_ID = cUSTOM4_ID;
	}
	public String getCUSTOM4_CODE() {
		return CUSTOM4_CODE;
	}
	public void setCUSTOM4_CODE(String cUSTOM4_CODE) {
		CUSTOM4_CODE = cUSTOM4_CODE;
	}
	public String getCUSTOM4_NAME() {
		return CUSTOM4_NAME;
	}
	public void setCUSTOM4_NAME(String cUSTOM4_NAME) {
		CUSTOM4_NAME = cUSTOM4_NAME;
	}
	public String getBALANCE_DIRECTION() {
		return BALANCE_DIRECTION;
	}
	public void setBALANCE_DIRECTION(String bALANCE_DIRECTION) {
		BALANCE_DIRECTION = bALANCE_DIRECTION;
	}
	public String getCURRENCY_CODE() {
		return CURRENCY_CODE;
	}
	public void setCURRENCY_CODE(String cURRENCY_CODE) {
		CURRENCY_CODE = cURRENCY_CODE;
	}
	public String getDEFAULT_CURRENCY() {
		return DEFAULT_CURRENCY;
	}
	public void setDEFAULT_CURRENCY(String dEFAULT_CURRENCY) {
		DEFAULT_CURRENCY = dEFAULT_CURRENCY;
	}
	public double getQC_SL() {
		return QC_SL;
	}
	public void setQC_SL(double qC_SL) {
		QC_SL = qC_SL;
	}
	public double getQCYB_JE() {
		return QCYB_JE;
	}
	public void setQCYB_JE(double qCYB_JE) {
		QCYB_JE = qCYB_JE;
	}
	public double getQC_JE() {
		return QC_JE;
	}
	public void setQC_JE(double qC_JE) {
		QC_JE = qC_JE;
	}
	public double getBQJF_SL() {
		return BQJF_SL;
	}
	public void setBQJF_SL(double bQJF_SL) {
		BQJF_SL = bQJF_SL;
	}
	public double getBQJF_YB() {
		return BQJF_YB;
	}
	public void setBQJF_YB(double bQJF_YB) {
		BQJF_YB = bQJF_YB;
	}
	public double getBQJF_JE() {
		return BQJF_JE;
	}
	public void setBQJF_JE(double bQJF_JE) {
		BQJF_JE = bQJF_JE;
	}
	public double getBQDF_SL() {
		return BQDF_SL;
	}
	public void setBQDF_SL(double bQDF_SL) {
		BQDF_SL = bQDF_SL;
	}
	public double getBQDF_YB() {
		return BQDF_YB;
	}
	public void setBQDF_YB(double bQDF_YB) {
		BQDF_YB = bQDF_YB;
	}
	public double getBQDF_JE() {
		return BQDF_JE;
	}
	public void setBQDF_JE(double bQDF_JE) {
		BQDF_JE = bQDF_JE;
	}
	public double getBNLJJF_SL() {
		return BNLJJF_SL;
	}
	public void setBNLJJF_SL(double bNLJJF_SL) {
		BNLJJF_SL = bNLJJF_SL;
	}
	public double getBNLJJF_YB() {
		return BNLJJF_YB;
	}
	public void setBNLJJF_YB(double bNLJJF_YB) {
		BNLJJF_YB = bNLJJF_YB;
	}
	public double getBNLJJF_JE() {
		return BNLJJF_JE;
	}
	public void setBNLJJF_JE(double bNLJJF_JE) {
		BNLJJF_JE = bNLJJF_JE;
	}
	public double getBNLJDF_SL() {
		return BNLJDF_SL;
	}
	public void setBNLJDF_SL(double bNLJDF_SL) {
		BNLJDF_SL = bNLJDF_SL;
	}
	public double getBNLJDF_YB() {
		return BNLJDF_YB;
	}
	public void setBNLJDF_YB(double bNLJDF_YB) {
		BNLJDF_YB = bNLJDF_YB;
	}
	public double getBNLJDF_JE() {
		return BNLJDF_JE;
	}
	public void setBNLJDF_JE(double bNLJDF_JE) {
		BNLJDF_JE = bNLJDF_JE;
	}
	public double getQM_SL() {
		return QM_SL;
	}
	public void setQM_SL(double qM_SL) {
		QM_SL = qM_SL;
	}
	public double getQMYB_JE() {
		return QMYB_JE;
	}
	public void setQMYB_JE(double qMYB_JE) {
		QMYB_JE = qMYB_JE;
	}
	public double getQM_JE() {
		return QM_JE;
	}
	public void setQM_JE(double qM_JE) {
		QM_JE = qM_JE;
	}
	public double getT_QC_SL() {
		return T_QC_SL;
	}
	public void setT_QC_SL(double t_QC_SL) {
		T_QC_SL = t_QC_SL;
	}
	public double getT_QCYB_JE() {
		return T_QCYB_JE;
	}
	public void setT_QCYB_JE(double t_QCYB_JE) {
		T_QCYB_JE = t_QCYB_JE;
	}
	public double getT_QC_JE() {
		return T_QC_JE;
	}
	public void setT_QC_JE(double t_QC_JE) {
		T_QC_JE = t_QC_JE;
	}
	public double getT_BQJF_SL() {
		return T_BQJF_SL;
	}
	public void setT_BQJF_SL(double t_BQJF_SL) {
		T_BQJF_SL = t_BQJF_SL;
	}
	public double getT_BQJF_YB() {
		return T_BQJF_YB;
	}
	public void setT_BQJF_YB(double t_BQJF_YB) {
		T_BQJF_YB = t_BQJF_YB;
	}
	public double getT_BQJF_JE() {
		return T_BQJF_JE;
	}
	public void setT_BQJF_JE(double t_BQJF_JE) {
		T_BQJF_JE = t_BQJF_JE;
	}
	public double getT_BQDF_SL() {
		return T_BQDF_SL;
	}
	public void setT_BQDF_SL(double t_BQDF_SL) {
		T_BQDF_SL = t_BQDF_SL;
	}
	public double getT_BQDF_YB() {
		return T_BQDF_YB;
	}
	public void setT_BQDF_YB(double t_BQDF_YB) {
		T_BQDF_YB = t_BQDF_YB;
	}
	public double getT_BQDF_JE() {
		return T_BQDF_JE;
	}
	public void setT_BQDF_JE(double t_BQDF_JE) {
		T_BQDF_JE = t_BQDF_JE;
	}
	public double getT_BNLJJF_SL() {
		return T_BNLJJF_SL;
	}
	public void setT_BNLJJF_SL(double t_BNLJJF_SL) {
		T_BNLJJF_SL = t_BNLJJF_SL;
	}
	public double getT_BNLJJF_YB() {
		return T_BNLJJF_YB;
	}
	public void setT_BNLJJF_YB(double t_BNLJJF_YB) {
		T_BNLJJF_YB = t_BNLJJF_YB;
	}
	public double getT_BNLJJF_JE() {
		return T_BNLJJF_JE;
	}
	public void setT_BNLJJF_JE(double t_BNLJJF_JE) {
		T_BNLJJF_JE = t_BNLJJF_JE;
	}
	public double getT_BNLJDF_SL() {
		return T_BNLJDF_SL;
	}
	public void setT_BNLJDF_SL(double t_BNLJDF_SL) {
		T_BNLJDF_SL = t_BNLJDF_SL;
	}
	public double getT_BNLJDF_YB() {
		return T_BNLJDF_YB;
	}
	public void setT_BNLJDF_YB(double t_BNLJDF_YB) {
		T_BNLJDF_YB = t_BNLJDF_YB;
	}
	public double getT_BNLJDF_JE() {
		return T_BNLJDF_JE;
	}
	public void setT_BNLJDF_JE(double t_BNLJDF_JE) {
		T_BNLJDF_JE = t_BNLJDF_JE;
	}
	public double getT_QM_SL() {
		return T_QM_SL;
	}
	public void setT_QM_SL(double t_QM_SL) {
		T_QM_SL = t_QM_SL;
	}
	public double getT_QMYB_JE() {
		return T_QMYB_JE;
	}
	public void setT_QMYB_JE(double t_QMYB_JE) {
		T_QMYB_JE = t_QMYB_JE;
	}
	public double getT_QM_JE() {
		return T_QM_JE;
	}
	public void setT_QM_JE(double t_QM_JE) {
		T_QM_JE = t_QM_JE;
	}
	@Override
	public int compareTo(AssistantStaticReport o) {
		
		return 0;
	}
}
