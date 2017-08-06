package com.xzsoft.xc.fa.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xzsoft.xc.fa.modal.XcAsset;
import com.xzsoft.xc.fa.service.AssetCcfService;
import com.xzsoft.xc.util.common.XCTransaction;
import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xip.framework.util.AppContext;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xip.platform.util.XipUtil;

/**
 * @fileName AssetCcfAction
 * @describe 资产分类
 * @author   wangwh
 *
 */
@Controller
@RequestMapping("/assetCcfAction.do")

public class AssetCcfAction {
	@Resource
    AssetCcfService assetCcfService;
	// 日志记录器
	private static Logger log = Logger.getLogger(AssetCcfAction.class.getName()) ;
	/**
	 * 
	 *@methodName       saveAsset
	 *@methodDescribe   分类资产保存
	 *@date 2016年7月14日
	 *@author wangwh
	 *@param request
	 *@param response
	 *@return
	 *@throws Exception
	 */
	@RequestMapping(params="method=saveAsset")
	public void saveAsset(HttpServletRequest request, HttpServletResponse response) throws Exception{
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
    	String lang = sessionVars.get(SessionVariable.language);
		
		ApplicationContext context = AppContext.getApplicationContext();
		assetCcfService = (AssetCcfService) context.getBean("assetCcfService");
		JSONObject json = new JSONObject();
		String cat_name = request.getParameter("cat_name");
		String property = request.getParameter("property");
		String up_code = request.getParameter("up_code");
		String user_id = request.getParameter("user_id");
		String cat_code = "";
		XCTransaction xCTransaction = new XCTransaction();
		xCTransaction.beginTransaction("XC_FA_CATS");
		//根据上级资产编码，算当前资产编码
		if(up_code.equals("")||up_code==null){
			//上级资产编码为空 ，当前资产为1级资产
			//获取该资产属性，1级资产最大编码
			String oneMaxCode = assetCcfService.getOneAssetMaxCode(new StringBuilder("%").append(property).append("%").toString());
			if(oneMaxCode.equals("0")){
				cat_code = property + "001";
			}else{
				int numCode = Integer.parseInt(oneMaxCode)+1;
				String code = "000"+numCode;
				cat_code = property+code.substring(code.length()-3,code.length());
				
			}
			
			//1级资产的上级编码重新赋值
			up_code = property+"_ROOT";
		}else{
			//上级资产编码不为空，判断上级资产是几级资产（如果上级资产的上级编码为资产属性+“_ROOT”,则为1级资产，否则为2级资产）
			String upCode = assetCcfService.getUpCodeByCatCode(up_code);
			if(upCode.equals(property+"_ROOT")){
				//上级资产为1级资产，获取2级资产最大编码
				String twoMaxCode = assetCcfService.getTwoAssetMaxCode(new StringBuilder("%").append(up_code).append("%").toString());
				if(twoMaxCode.equals("0")){
					cat_code = up_code+"_001"; 
				}else{
					int numCode = Integer.parseInt(twoMaxCode)+1;
					String code = "000"+numCode;
					cat_code = up_code+"_"+code.substring(code.length()-3,code.length());
				}
				
			}else{
				//上级资产为2级资产，获取3级资产最大编码
				String threeMaxCode = assetCcfService.getThreeAssetMaxCode(new StringBuilder("%").append(up_code).append("%").toString());
				if(threeMaxCode.equals("0")){
					cat_code = up_code + "_001";
				}else{
					int numCode = Integer.parseInt(threeMaxCode)+1;
					String code = "000"+numCode;
					cat_code = up_code+"_"+code.substring(code.length()-3,code.length());
				}
				
			}
		}
		xCTransaction.endTransaction();
		HashMap saveMap = new HashMap();
		saveMap.put("CAT_ID",java.util.UUID.randomUUID().toString());
		saveMap.put("CAT_NAME",cat_name);
		saveMap.put("CAT_CODE",cat_code);
		saveMap.put("FA_PROPERTY",property);
		saveMap.put("UP_CODE",up_code);
		saveMap.put("IS_LEAF",1);
		saveMap.put("CREATION_DATE",new Date());
		saveMap.put("CREATED_BY",user_id);
		saveMap.put("LAST_UPDATE_DATE",new Date());
		saveMap.put("LAST_UPDATED_BY",user_id);
		try{
			assetCcfService.saveAssetCcf(saveMap);
			//如果当前资产不是1级资产，则将该资产的上级资产的IS_LEAF置为0
			if(!up_code.equals(property+"_ROOT")){
				HashMap updateUpmap = new HashMap();
				updateUpmap.put("CAT_CODE", up_code);
				updateUpmap.put("LAST_UPDATE_DATE",new Date());
				updateUpmap.put("LAST_UPDATED_BY",user_id);
				assetCcfService.updateIsLeafByUpcode(updateUpmap);
				
			}
			json.put("flag", "0");
			json.put("msg",XipUtil.getMessage(lang, "XC_FA_SAVE_SUCCESS", null));
		}catch(Exception e){
			log.error(e.getMessage());
			json.put("flag", "1");
			json.put("msg",e.getMessage());
			
		}
		finally{
			PlatformUtil.outPrint(response, json.toString());
		}
	}
	/**
	 * 
	 *@methodName       updateAsset
	 *@methodDescribe   分类资产更新
	 *@date 2016年7月14日
	 *@author wangwh
	 *@param request
	 *@param response
	 *@return
	 *@throws Exception
	 */
	@RequestMapping(params="method=updateAsset")
	public void updateAsset(HttpServletRequest request, HttpServletResponse response) throws Exception{
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
    	String lang = sessionVars.get(SessionVariable.language);
		
		ApplicationContext context = AppContext.getApplicationContext();
		assetCcfService = (AssetCcfService) context.getBean("assetCcfService");
		JSONObject json = new JSONObject();
		String cat_id = request.getParameter("cat_id");
		String cat_name = request.getParameter("cat_name");
		String property = request.getParameter("property");
		String up_code = request.getParameter("up_code");
		String user_id = request.getParameter("user_id");
		String cat_code =request.getParameter("cat_code");
		int is_leaf = 0;
		//根据当前资产ID，获取该资产更新前的信息
		XcAsset asset = assetCcfService.getAssetInfoByCatId(cat_id);
		//根据上级资产编码，算当前资产编码
		XCTransaction xCTransaction = new XCTransaction();
		xCTransaction.beginTransaction("XC_FA_CATS");
		if(up_code.equals("")||up_code==null){
			//上级资产编码为空 ，当前资产为1级资产
			//获取该资产属性，1级资产最大编码
			String oneMaxCode = assetCcfService.getOneAssetMaxCode(new StringBuilder("%").append(property).append("%").toString());
			String code = "";
			if(oneMaxCode.equals("0")){
				code = "001";
			}else{
				int numCode = Integer.parseInt(oneMaxCode)+1;
				code = "000"+numCode;
			}
			
			if(asset.getUpCode().equals(asset.getFaProperty()+"_ROOT")){
				//当前资产更新前是1级资产
				if(property.equals(asset.getFaProperty())){
					//当前资产属性没改
				}else{
					//资产属性变，资产编码变
					cat_code = property+code.substring(code.length()-3,code.length());
				}
			}else{
				//当前资产更新前不是1级资产，资产编码改变
		     	cat_code = property+code.substring(code.length()-3,code.length());
			}
			//1级资产的上级编码重新赋值
			up_code = property+"_ROOT";
		}else{
			//上级资产编码不为空，判断上级资产是几级资产（如果上级资产的上级编码为资产属性+“_ROOT”,则为1级资产，否则为2级资产）
			String upCode = assetCcfService.getUpCodeByCatCode(up_code);
			//获取更新前资产的上级编码跟更新的上级编码对比
			if(asset.getUpCode().equals(up_code)){
				//上级没变，不需要改变编码
			}else{
				//上级已变
				if(upCode.equals(property+"_ROOT")){
					//上级资产为1级资产，获取2级资产最大编码
					String twoMaxCode = assetCcfService.getTwoAssetMaxCode(new StringBuilder("%").append(up_code).append("%").toString());
					if(twoMaxCode.equals("0")){
						cat_code = up_code+"_001";
					}else{
						int numCode = Integer.parseInt(twoMaxCode)+1;
						String code = "000"+numCode;
						cat_code = up_code+"_"+code.substring(code.length()-3,code.length());
					}
					
				}else{
					//上级资产为2级资产，获取3级资产最大编码
					String threeMaxCode = assetCcfService.getThreeAssetMaxCode(new StringBuilder("%").append(up_code).append("%").toString());
					if(threeMaxCode.equals("0")){
						cat_code = up_code+"_001";
					}else{
						int numCode = Integer.parseInt(threeMaxCode)+1;
						String code = "000"+numCode;
						cat_code = up_code+"_"+code.substring(code.length()-3,code.length());
					}
					
				}
				
			}
			
		}
		xCTransaction.endTransaction();
		
		HashMap updateMap = new HashMap();
		updateMap.put("CAT_ID",cat_id);
		updateMap.put("CAT_NAME",cat_name);
		updateMap.put("CAT_CODE",cat_code);
		updateMap.put("FA_PROPERTY",property);
		updateMap.put("UP_CODE",up_code);
		updateMap.put("LAST_UPDATE_DATE",new Date());
		updateMap.put("LAST_UPDATED_BY",user_id);
		try{
			List<XcAsset> upAssetList = assetCcfService.getDownAssetByCode(request.getParameter("cat_code"));
			assetCcfService.updateAssetCcf(updateMap);
			
			//如果当前资产不是1级资产，则将该资产的上级资产的IS_LEAF置为0
			if(!up_code.equals(property+"_ROOT")){
				HashMap updateUpmap = new HashMap();
				updateUpmap.put("CAT_CODE", up_code);
				updateUpmap.put("LAST_UPDATE_DATE",new Date());
				updateUpmap.put("LAST_UPDATED_BY",user_id);
				assetCcfService.updateIsLeafByUpcode(updateUpmap);
				
			}
			//如果当前资产存在下级资产，上级资产改变，则改变下级资产的资产编码和上级编码
			
			for(XcAsset upAsset:upAssetList){
				if(upAsset.getCatId()!=null && !upAsset.getCatId().equals("")){
					if(asset.getUpCode().equals(up_code)){
						//上级没变，不需要改变编码
					}else{
						//上级已变
						//下级资产新的编码为，当前资产新编码加 当前资产下级编码的后4位
						String downCode = upAsset.getCatCode();
                        String newCode = cat_code + downCode.substring(downCode.length()-4,downCode.length());
						HashMap updateDownmap = new HashMap();
						updateDownmap.put("CAT_CODE", newCode);
						updateDownmap.put("LAST_UPDATE_DATE",new Date());
						updateDownmap.put("LAST_UPDATED_BY",user_id);
						updateDownmap.put("UP_CODE",cat_code);
						updateDownmap.put("CAT_ID",upAsset.getCatId());
						
						
						assetCcfService.updateDownAssetCcf(updateDownmap);
						
						
					}
				}
			}
			
			json.put("flag", "0");
			json.put("msg",XipUtil.getMessage(lang, "XC_FA_SAVE_SUCCESS", null));
		}catch(Exception e){
			log.error(e.getMessage());
			json.put("flag", "1");
			json.put("msg",e.getMessage());
			
		}finally{
			PlatformUtil.outPrint(response, json.toString());
		}
		
	}
}
