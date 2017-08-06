/**
 * 
 */
package com.xzsoft.xc.bg.dao.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.bg.dao.BgItemsBatchDao;
import com.xzsoft.xc.bg.mapper.BgItemsBatchMapper;
import com.xzsoft.xc.bg.modal.BgItemDTO;
import com.xzsoft.xc.bg.util.XCBGConstants;
import com.xzsoft.xip.platform.util.PlatformUtil;

/**
 * @author tangxl
 *
 */
@Repository("bgItemsBatchDao")
public class BgItemsBatchDaoImpl implements BgItemsBatchDao {
	@Resource
	private BgItemsBatchMapper bgItemsBatchMapper;

	/* (non-Javadoc)
	 * @name     getBgItemsByLedger
	 * @author   tangxl
	 * @date     2016年7月25日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.bg.dao.BgItemsBatchDao#getBgItemsByLedger(java.lang.String, java.lang.String)
	 */
	@Override
	public List<BgItemDTO> getBgItemsByLedger(String ledgerId, String bgItemName,String exportModel)
			throws Exception {
		if (bgItemName == null) {
			bgItemName = "";
		}
		bgItemName = "%".concat(bgItemName).concat("%");
		if(XCBGConstants.BG_COST_TYPE.equalsIgnoreCase(exportModel)){
			return bgItemsBatchMapper.getExItemsByLedger(ledgerId, bgItemName);
		}else{
			return bgItemsBatchMapper.getBgItemsByLedger(ledgerId, bgItemName);
		}
		
	}

	/* (non-Javadoc)
	 * @name     updateImportItems
	 * @author   tangxl
	 * @date     2016年7月26日
	 * @注释                   更新导入的数据
	 * @version  1.0
	 * @see com.xzsoft.xc.bg.dao.BgItemsBatchDao#updateImportItems(java.lang.String, java.util.List, java.lang.String)
	 */
	@Override
	public String updateImportItems(String ledgerId,List<HashMap<String, String>> itemList, String importType)
			throws Exception {
		String resultJson = "{\"success\":true,\"flag\":\"1\",\"msg\":\"导入成功！\"}";
		HashMap<String, Object> map = new HashMap<String, Object>();
		String sessionId = itemList.get(0).get("SESSION_ID");
		map.put("dbType", PlatformUtil.getDbType());
		map.put("list", itemList);
		String sql = "delete from xc_bg_items_tmp where UP_CODE = '"+ledgerId+"' AND SESSION_ID = '"+sessionId+"'";
		if(XCBGConstants.BG_COST_TYPE.equalsIgnoreCase(importType)){
			bgItemsBatchMapper.insertExImportItems(map);
			bgItemsBatchMapper.checkExImportValid(ledgerId,sessionId);
			bgItemsBatchMapper.checkAccImportValid(ledgerId, sessionId);
			bgItemsBatchMapper.checkBgImportValid(ledgerId, sessionId);
			bgItemsBatchMapper.updateNoValidData(ledgerId, sessionId);
			int inValidCount = bgItemsBatchMapper.getInValidBgItem(ledgerId,sessionId,"BG01");
			if(inValidCount>0){
				 return "{\"success\":false,\"flag\":\"2\",\"msg\":\""+sessionId+"\"}";
			}
			bgItemsBatchMapper.modifyFeeItems(ledgerId,sessionId);
			sql = "delete from xc_ex_items_tmp where UP_CODE = '"+ledgerId+"' AND SESSION_ID = '"+sessionId+"'";
		}else{
			bgItemsBatchMapper.insertBgImportItems(map);
			bgItemsBatchMapper.checkBgItemExist(ledgerId, sessionId);
			int inValidCount = bgItemsBatchMapper.getInValidBgItem(ledgerId,sessionId,"BG02");
			if(inValidCount>0){
				 return "{\"success\":false,\"flag\":\"2\",\"msg\":\""+sessionId+"\"}";
			}
			bgItemsBatchMapper.modifyBudgetItems(ledgerId,sessionId);
		}
		bgItemsBatchMapper.deleteTmpData(sql);
		return resultJson;
	}

	/* (non-Javadoc)
	 * @name     方法名
	 * @author   tangxl
	 * @date     2016年7月28日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.bg.dao.BgItemsBatchDao#deleteTmpImport(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteTmpImport(String ledgerId, String sessionId,
			String importType) throws Exception {
		String sql = "delete from xc_bg_items_tmp where UP_CODE = '"+ledgerId+"' AND SESSION_ID = '"+sessionId+"'";
		if(XCBGConstants.BG_COST_TYPE.equalsIgnoreCase(importType)){
			sql = "delete from xc_ex_items_tmp where UP_CODE = '"+ledgerId+"' AND SESSION_ID = '"+sessionId+"'";
		}
		bgItemsBatchMapper.deleteTmpData(sql);
	}
}
