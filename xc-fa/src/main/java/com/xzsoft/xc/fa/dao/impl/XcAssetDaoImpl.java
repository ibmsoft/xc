package com.xzsoft.xc.fa.dao.impl;

import java.util.HashMap;
import java.util.List;














import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import com.xzsoft.xc.fa.dao.XcAssetDao;
import com.xzsoft.xc.fa.mapper.AssetCcfMapper;
import com.xzsoft.xc.fa.mapper.CheckAssetCodeMapper;
import com.xzsoft.xc.fa.modal.XcAsset;
import com.xzsoft.xip.framework.util.AppContext;
/**
 * 
  * @ClassName: XcAssetDaoImpl
  * @Description: 资产分类Dao层实现类
  * @author wangwh
  * @date 2016年7月12
 */
@Repository("xcAssetDao")
public class XcAssetDaoImpl implements XcAssetDao {
	@Resource
	private CheckAssetCodeMapper checkAssetCodeMapper ;
	@Resource
	private AssetCcfMapper assetCcfMapper ;
	@Override
	public List<XcAsset> getAssetByTagId(String tag_id) {
		ApplicationContext context = AppContext.getApplicationContext();
		checkAssetCodeMapper =(CheckAssetCodeMapper) context.getBean("checkAssetCodeMapper");
		return checkAssetCodeMapper.getAssetByTagId(tag_id);
	}
	@Override
	public void deleteAsset(String ids) {
		ApplicationContext context = AppContext.getApplicationContext();
		checkAssetCodeMapper =(CheckAssetCodeMapper) context.getBean("checkAssetCodeMapper");
		checkAssetCodeMapper.deleteAsset(ids);
	}
	@Override
	public void saveAssetCcf(HashMap<String, String> map) {
		ApplicationContext context = AppContext.getApplicationContext();
		assetCcfMapper =(AssetCcfMapper) context.getBean("assetCcfMapper");
		assetCcfMapper.saveAssetCcf(map);
	}
	@Override
	public void updateIsLeafByUpcode(HashMap<String, String> map) {
		ApplicationContext context = AppContext.getApplicationContext();
		assetCcfMapper =(AssetCcfMapper) context.getBean("assetCcfMapper");
		assetCcfMapper.updateIsLeafByUpcode(map);
	}
	@Override
	public void updateAssetCcf(HashMap<String, String> map) {
		ApplicationContext context = AppContext.getApplicationContext();
		assetCcfMapper =(AssetCcfMapper) context.getBean("assetCcfMapper");
		assetCcfMapper.updateAssetCcf(map);
	}
	@Override
	public void updateDownAssetCcf(HashMap<String, String> map) {
		ApplicationContext context = AppContext.getApplicationContext();
		assetCcfMapper =(AssetCcfMapper) context.getBean("assetCcfMapper");
		assetCcfMapper.updateDownAssetCcf(map);
	}
	@Override
	public String getOneAssetMaxCode(String property) {
		ApplicationContext context = AppContext.getApplicationContext();
		assetCcfMapper =(AssetCcfMapper) context.getBean("assetCcfMapper");
		return assetCcfMapper.getOneAssetMaxCode(property);
	}
	@Override
	public String getTwoAssetMaxCode(String property) {
		ApplicationContext context = AppContext.getApplicationContext();
		assetCcfMapper =(AssetCcfMapper) context.getBean("assetCcfMapper");
		return assetCcfMapper.getTwoAssetMaxCode(property);
	}
	@Override
	public String getThreeAssetMaxCode(String property) {
		ApplicationContext context = AppContext.getApplicationContext();
		assetCcfMapper =(AssetCcfMapper) context.getBean("assetCcfMapper");
		return assetCcfMapper.getThreeAssetMaxCode(property);
	}
	@Override
	public String getUpCodeByCatCode(String cat_code) {
		ApplicationContext context = AppContext.getApplicationContext();
		assetCcfMapper =(AssetCcfMapper) context.getBean("assetCcfMapper");
		return assetCcfMapper.getUpCodeByCatCode(cat_code);
	}
	@Override
	public XcAsset getAssetInfoByCatId(String cat_id) {
		ApplicationContext context = AppContext.getApplicationContext();
		assetCcfMapper =(AssetCcfMapper) context.getBean("assetCcfMapper");
		return assetCcfMapper.getAssetInfoByCatId(cat_id);
	}
	@Override
	public List<XcAsset>  getDownAssetByCode(String cat_code) {
		ApplicationContext context = AppContext.getApplicationContext();
		assetCcfMapper =(AssetCcfMapper) context.getBean("assetCcfMapper");
		return assetCcfMapper.getDownAssetByCode(cat_code);
	}
	@Override
	public String getCodeByPropertyName(String fa_property) {
		ApplicationContext context = AppContext.getApplicationContext();
		assetCcfMapper =(AssetCcfMapper) context.getBean("assetCcfMapper");
		return assetCcfMapper.getCodeByPropertyName(fa_property);
	}
	@Override
	public XcAsset getAssetByCatName(String cat_name) {
		ApplicationContext context = AppContext.getApplicationContext();
		assetCcfMapper =(AssetCcfMapper) context.getBean("assetCcfMapper");
		return assetCcfMapper.getAssetByCatName(cat_name);
	}

}
