package com.xzsoft.xc.st.modal;

import java.util.Date;

/**
 * 
  * @ClassName XcStMaterialBean
  * @Description 物资信息表
  * @author RenJianJian
  * @date 2016年11月30日 上午11:11:42
 */
public class XcStMaterialBean {
	private String MATERIAL_ID;
	private String MATERIAL_CODE;
	private String MATERIAL_NAME;
	private String MATERIAL_PRO;
	private String MATERIAL_SET_ID;
	private String MATERIAL_TYPE_ID;
	private String FINACE_TYPE_ID;
	private String SPECIFICATION;
	private String DIM_ID;
	private String IS_BATCH;
	private String IS_SERIAL;
	private String IS_PURCHASE;
	private String IS_PURCHASE_EXCESS;
	private String IS_SALE;
	private String IS_SALE_EXCESS;
	private double PURCHASE_PRICE;
	private double SALE_PRICE;
	private String SALE_TYPE_ID;
	private String IMAGES;
	private Date CREATION_DATE;
	private String CREATED_BY;
	private Date LAST_UPDATE_DATE;
	private String LAST_UPDATED_BY;
	public XcStMaterialBean() {
		super();
	}
	public String getMATERIAL_ID() {
		return MATERIAL_ID;
	}
	public void setMATERIAL_ID(String mATERIAL_ID) {
		MATERIAL_ID = mATERIAL_ID;
	}
	public String getMATERIAL_CODE() {
		return MATERIAL_CODE;
	}
	public void setMATERIAL_CODE(String mATERIAL_CODE) {
		MATERIAL_CODE = mATERIAL_CODE;
	}
	public String getMATERIAL_NAME() {
		return MATERIAL_NAME;
	}
	public void setMATERIAL_NAME(String mATERIAL_NAME) {
		MATERIAL_NAME = mATERIAL_NAME;
	}
	public String getMATERIAL_PRO() {
		return MATERIAL_PRO;
	}
	public void setMATERIAL_PRO(String mATERIAL_PRO) {
		MATERIAL_PRO = mATERIAL_PRO;
	}
	public String getMATERIAL_SET_ID() {
		return MATERIAL_SET_ID;
	}
	public void setMATERIAL_SET_ID(String mATERIAL_SET_ID) {
		MATERIAL_SET_ID = mATERIAL_SET_ID;
	}
	public String getMATERIAL_TYPE_ID() {
		return MATERIAL_TYPE_ID;
	}
	public void setMATERIAL_TYPE_ID(String mATERIAL_TYPE_ID) {
		MATERIAL_TYPE_ID = mATERIAL_TYPE_ID;
	}
	public String getFINACE_TYPE_ID() {
		return FINACE_TYPE_ID;
	}
	public void setFINACE_TYPE_ID(String fINACE_TYPE_ID) {
		FINACE_TYPE_ID = fINACE_TYPE_ID;
	}
	public String getSPECIFICATION() {
		return SPECIFICATION;
	}
	public void setSPECIFICATION(String sPECIFICATION) {
		SPECIFICATION = sPECIFICATION;
	}
	public String getDIM_ID() {
		return DIM_ID;
	}
	public void setDIM_ID(String dIM_ID) {
		DIM_ID = dIM_ID;
	}
	public String getIS_BATCH() {
		return IS_BATCH;
	}
	public void setIS_BATCH(String iS_BATCH) {
		IS_BATCH = iS_BATCH;
	}
	public String getIS_SERIAL() {
		return IS_SERIAL;
	}
	public void setIS_SERIAL(String iS_SERIAL) {
		IS_SERIAL = iS_SERIAL;
	}
	public String getIS_PURCHASE() {
		return IS_PURCHASE;
	}
	public void setIS_PURCHASE(String iS_PURCHASE) {
		IS_PURCHASE = iS_PURCHASE;
	}
	public String getIS_PURCHASE_EXCESS() {
		return IS_PURCHASE_EXCESS;
	}
	public void setIS_PURCHASE_EXCESS(String iS_PURCHASE_EXCESS) {
		IS_PURCHASE_EXCESS = iS_PURCHASE_EXCESS;
	}
	public String getIS_SALE() {
		return IS_SALE;
	}
	public void setIS_SALE(String iS_SALE) {
		IS_SALE = iS_SALE;
	}
	public String getIS_SALE_EXCESS() {
		return IS_SALE_EXCESS;
	}
	public void setIS_SALE_EXCESS(String iS_SALE_EXCESS) {
		IS_SALE_EXCESS = iS_SALE_EXCESS;
	}
	public double getPURCHASE_PRICE() {
		return PURCHASE_PRICE;
	}
	public void setPURCHASE_PRICE(double pURCHASE_PRICE) {
		PURCHASE_PRICE = pURCHASE_PRICE;
	}
	public double getSALE_PRICE() {
		return SALE_PRICE;
	}
	public void setSALE_PRICE(double sALE_PRICE) {
		SALE_PRICE = sALE_PRICE;
	}
	public String getSALE_TYPE_ID() {
		return SALE_TYPE_ID;
	}
	public void setSALE_TYPE_ID(String sALE_TYPE_ID) {
		SALE_TYPE_ID = sALE_TYPE_ID;
	}
	public Date getCREATION_DATE() {
		return CREATION_DATE;
	}
	public String getIMAGES() {
		return IMAGES;
	}
	public void setIMAGES(String iMAGES) {
		IMAGES = iMAGES;
	}
	public void setCREATION_DATE(Date cREATION_DATE) {
		CREATION_DATE = cREATION_DATE;
	}
	public String getCREATED_BY() {
		return CREATED_BY;
	}
	public void setCREATED_BY(String cREATED_BY) {
		CREATED_BY = cREATED_BY;
	}
	public Date getLAST_UPDATE_DATE() {
		return LAST_UPDATE_DATE;
	}
	public void setLAST_UPDATE_DATE(Date lAST_UPDATE_DATE) {
		LAST_UPDATE_DATE = lAST_UPDATE_DATE;
	}
	public String getLAST_UPDATED_BY() {
		return LAST_UPDATED_BY;
	}
	public void setLAST_UPDATED_BY(String lAST_UPDATED_BY) {
		LAST_UPDATED_BY = lAST_UPDATED_BY;
	}
}
