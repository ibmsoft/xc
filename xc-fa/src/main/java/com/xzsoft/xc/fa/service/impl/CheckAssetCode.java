package com.xzsoft.xc.fa.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.xzsoft.xc.fa.dao.XcAssetDao;
import com.xzsoft.xc.fa.modal.XcAsset;
import com.xzsoft.xc.fa.service.AssetCcfService;
import com.xzsoft.xc.util.common.XCTransaction;
import com.xzsoft.xip.framework.util.AppContext;
/**
 * 
  * @ClassName: CheckAssetCode
  * @Description: 资产分类导入根据编码规则数据处理实现类
  * @author  wangwh
  * @date 2016年7月12日
 */
@Service("checkAssCode")
public class CheckAssetCode {
	private static final Logger log = Logger.getLogger(CheckAssetCode.class.getName()) ;
	@Resource
	private XcAssetDao xcAssetDao;
	AssetCcfService assetCcfService;
	public String checkAssetCode(String template_code,String tag_id)throws Exception{
		ApplicationContext context = AppContext.getApplicationContext();
		xcAssetDao = (XcAssetDao) context.getBean("xcAssetDao");
		assetCcfService = (AssetCcfService) context.getBean("assetCcfService");
		List<XcAsset> assetList= xcAssetDao.getAssetByTagId(tag_id);
		String result = "<result><flag>0</flag><msg>编码无异常！</msg></result>";
		int errorData = 0;
		String deleteId = "";
		XCTransaction xCTransaction = new XCTransaction();
		xCTransaction.beginTransaction("XC_FA_CATS");
		try{
			
			for(XcAsset xcAsset : assetList){
				String catId = xcAsset.getCatId();
				String catCode = xcAsset.getCatCode();
				String faPro = xcAsset.getFaProperty();
				String catName = xcAsset.getCatName();
				String upCode = xcAsset.getUpCode();
				
				if(catCode==null||catCode.equals("")){
					//新插入的数据，判断是否可插入，不可插入则根据ID删除
					//1.资产名称，数据库里非空唯一，不用校验
					//2.上级资产为空，更新资产属性（名称换成编码），资产编码赋值
					//3.上级资产不为空，上级资产不存在或为3级资产则删除，否则如果上级资产属性与资产属性不一致,删除。
					//String property = assetCcfService.getCodeByPropertyName(faPro);
					if(upCode==null||upCode.equals("")){
						//上级为空
						if(!faPro.equals("FA")&&!faPro.equals("IA")&&!faPro.equals("BA")&&!faPro.equals("LV")){
							//上级资产属性与资产属性不一致,删除
							errorData++;
							deleteId += "'"+xcAsset.getCatId()+"',";
						}
						String oneMaxCode = assetCcfService.getOneAssetMaxCode(new StringBuilder("%").append(faPro).append("%").toString());
						String newCatCode = "";
						String newUpCode = "";
						if(oneMaxCode.equals("0")){
							newCatCode = faPro + "001";
						}else{
							int numCode = Integer.parseInt(oneMaxCode)+1;
							String code = "000"+numCode;
							newCatCode = faPro+code.substring(code.length()-3,code.length());
							
						}
						//上级编码重新赋值
						newUpCode = faPro+"_ROOT";
						//执行更新
						HashMap updateMap = new HashMap();
						updateMap.put("CAT_ID", catId);
						updateMap.put("UP_CODE", newUpCode);
						updateMap.put("CAT_CODE", newCatCode);
						updateMap.put("CAT_NAME", catName);
						updateMap.put("FA_PROPERTY", faPro);
						updateMap.put("LAST_UPDATE_DATE",new Date());
						assetCcfService.updateAssetCcf(updateMap);
						
					}else{
						//判断上级资产是否存在
						XcAsset xcas = assetCcfService.getAssetByCatName(upCode);
						if(xcas!=null){
							String isThreeAsset = xcas.getCatCode();
							
							if(!xcas.getFaProperty().equals(faPro)){
								//上级资产属性与资产属性不一致,删除
								errorData++;
								deleteId += "'"+xcAsset.getCatId()+"',";
							}else{
								if(isThreeAsset.split("_").length==3){
									//上级资产为3级资产，删除
									errorData++;
									deleteId += "'"+xcAsset.getCatId()+"',";
								}else if(isThreeAsset.split("_").length==2){
									//当前更新的资产为3级资产
									//上级资产为1级资产，获取2级资产最大编码
									String twoMaxCode = assetCcfService.getTwoAssetMaxCode(new StringBuilder("%").append(isThreeAsset).append("%").toString());
									String twoCatCode = "";
									if(twoMaxCode.equals("0")){
										twoCatCode = isThreeAsset+"_001"; 
									}else{
										int numCode = Integer.parseInt(twoMaxCode)+1;
										String code = "000"+numCode;
										twoCatCode = isThreeAsset+"_"+code.substring(code.length()-3,code.length());
									}
									//执行更新
									HashMap updateMap = new HashMap();
									updateMap.put("CAT_ID", catId);
									updateMap.put("UP_CODE", isThreeAsset);
									updateMap.put("CAT_CODE", twoCatCode);
									updateMap.put("CAT_NAME", catName);
									updateMap.put("FA_PROPERTY", faPro);
									updateMap.put("LAST_UPDATE_DATE",new Date());
									assetCcfService.updateAssetCcf(updateMap);
								}else if(isThreeAsset.split("_").length==1){
									//当前更新的资产为2级资产
									//上级资产为2级资产，获取3级资产最大编码
									String threeMaxCode = assetCcfService.getThreeAssetMaxCode(new StringBuilder("%").append(isThreeAsset).append("%").toString());
									String threeCatCode ="";
									if(threeMaxCode.equals("0")){
										threeCatCode = isThreeAsset + "_001";
									}else{
										int numCode = Integer.parseInt(threeMaxCode)+1;
										String code = "000"+numCode;
										threeCatCode = isThreeAsset+"_"+code.substring(code.length()-3,code.length());
									}
									//执行更新
									HashMap updateMap = new HashMap();
									updateMap.put("CAT_ID", catId);
									updateMap.put("UP_CODE", isThreeAsset);
									updateMap.put("CAT_CODE", threeCatCode);
									updateMap.put("CAT_NAME", catName);
									updateMap.put("FA_PROPERTY", faPro);
									updateMap.put("LAST_UPDATE_DATE",new Date());
									assetCcfService.updateAssetCcf(updateMap);
								}
								
							}
							
						}else{
						 //不存在上级资产，根据ID删除
							errorData++;
							deleteId += "'"+xcAsset.getCatId()+"',";
						}
					}
				}
				
				
			}
				
			if(deleteId!=null && deleteId!=""){
				deleteId = deleteId.substring(0, deleteId.length()-1);
				//执行删除
				for(int m = 0;m<deleteId.split(",").length;m++){
					xcAssetDao.deleteAsset(deleteId.split(",")[m].substring(1, deleteId.split(",")[m].length()-1));
				}
				result = "<result><flag>0</flag><msg>有"+errorData+"条数据未导入（请检查资产名称唯一，上级资产（填写）属性是否与该资产属性一致）！</msg></result>";
			}


		}catch(Exception e){
			result = "<result><flag>1</flag><msg>"+e.getMessage()+"！</msg></result>";
			log.error(e.getMessage());
		    throw e;
		    
		}finally{
			xCTransaction.endTransaction();
		}
		return result;
		
	}

}
