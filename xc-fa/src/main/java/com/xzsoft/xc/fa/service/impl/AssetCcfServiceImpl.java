package com.xzsoft.xc.fa.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.xzsoft.xc.fa.dao.XcAssetDao;
import com.xzsoft.xc.fa.modal.XcAsset;
import com.xzsoft.xc.fa.service.AssetCcfService;
import com.xzsoft.xc.gl.dao.LedgerDAO;
import com.xzsoft.xip.framework.util.AppContext;

/**
 * @ClassName: AssetCcfServiceImpl 
 * @Description: 资产分类实现类
 * @author wangwh
 * @date 2016年7月14日  
 *
 */
@Service("assetCcfService")
public class AssetCcfServiceImpl implements AssetCcfService {
	@Resource
	private XcAssetDao xcAssetDao ;
	@Override
	public void saveAssetCcf(HashMap<String, String> map) {
		ApplicationContext context = AppContext.getApplicationContext();
		xcAssetDao = (XcAssetDao) context.getBean("xcAssetDao");
		xcAssetDao.saveAssetCcf(map);
		
	}

	@Override
	public void updateIsLeafByUpcode(HashMap<String, String> map) {
		ApplicationContext context = AppContext.getApplicationContext();
		xcAssetDao = (XcAssetDao) context.getBean("xcAssetDao");
		xcAssetDao.updateIsLeafByUpcode(map);
		
	}

	@Override
	public void updateAssetCcf(HashMap<String, String> map) {
		ApplicationContext context = AppContext.getApplicationContext();
		xcAssetDao = (XcAssetDao) context.getBean("xcAssetDao");
		xcAssetDao.updateAssetCcf(map);
		
	}

	@Override
	public void updateDownAssetCcf(HashMap<String, String> map) {
		ApplicationContext context = AppContext.getApplicationContext();
		xcAssetDao = (XcAssetDao) context.getBean("xcAssetDao");
		xcAssetDao.updateDownAssetCcf(map);
		
	}

	@Override
	public String getOneAssetMaxCode(String property) {
		ApplicationContext context = AppContext.getApplicationContext();
		xcAssetDao = (XcAssetDao) context.getBean("xcAssetDao");
		return xcAssetDao.getOneAssetMaxCode(property);
	}

	@Override
	public String getTwoAssetMaxCode(String property) {
		ApplicationContext context = AppContext.getApplicationContext();
		xcAssetDao = (XcAssetDao) context.getBean("xcAssetDao");
		return xcAssetDao.getTwoAssetMaxCode(property);
	}

	@Override
	public String getThreeAssetMaxCode(String property) {
		ApplicationContext context = AppContext.getApplicationContext();
		xcAssetDao = (XcAssetDao) context.getBean("xcAssetDao");
		return xcAssetDao.getThreeAssetMaxCode(property);
	}

	@Override
	public String getUpCodeByCatCode(String cat_code) {
		ApplicationContext context = AppContext.getApplicationContext();
		xcAssetDao = (XcAssetDao) context.getBean("xcAssetDao");
		return xcAssetDao.getUpCodeByCatCode(cat_code);
	}

	@Override
	public XcAsset getAssetInfoByCatId(String cat_id) {
		ApplicationContext context = AppContext.getApplicationContext();
		xcAssetDao = (XcAssetDao) context.getBean("xcAssetDao");
		return xcAssetDao.getAssetInfoByCatId(cat_id);
	}

	@Override
	public List<XcAsset>  getDownAssetByCode(String cat_code) {
		ApplicationContext context = AppContext.getApplicationContext();
		xcAssetDao = (XcAssetDao) context.getBean("xcAssetDao");
		return xcAssetDao.getDownAssetByCode(cat_code);
	}

	@Override
	public String getCodeByPropertyName(String fa_property) {
		ApplicationContext context = AppContext.getApplicationContext();
		xcAssetDao = (XcAssetDao) context.getBean("xcAssetDao");
		return xcAssetDao.getCodeByPropertyName(fa_property);
	}

	@Override
	public XcAsset getAssetByCatName(String cat_name) {
		ApplicationContext context = AppContext.getApplicationContext();
		xcAssetDao = (XcAssetDao) context.getBean("xcAssetDao");
		return xcAssetDao.getAssetByCatName(cat_name);
	}

}
