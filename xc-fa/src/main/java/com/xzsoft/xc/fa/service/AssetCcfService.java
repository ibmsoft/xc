package com.xzsoft.xc.fa.service;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.fa.modal.XcAsset;
/**
 * @ClassName: AssetCcfService 
 * @Description: 资产分类接口
 * @author wangwh
 * @date 2016年7月14日  
 *
 */

public interface AssetCcfService {
	/**
	 * 
	 * @methodName  saveAssetCcf
	 * @author      wangwh
	 * @date        2016年7月14日
	 * @describe    资产分类保存
	 * @param map
	 * @变动说明       
	 * @version     1.0
	 */
	public void saveAssetCcf(HashMap<String, String> map);
	/**
	 * 
	 * @methodName  updateIsLeafByUpcode
	 * @author      wangwh
	 * @date        2016年7月14日
	 * @describe    新增资产分类 ,如果存在上级,上级资产的IS_LEAF变成0
	 * @param map
	 * @变动说明       
	 * @version     1.0
	 */
	public void updateIsLeafByUpcode(HashMap<String, String> map );
	/**
	 * 
	 * @methodName  updateAssetCcf
	 * @author      wangwh
	 * @date        2016年7月14日
	 * @describe    更新资产分类
	 * @param map
	 * @变动说明       
	 * @version     1.0
	 */
	public void updateAssetCcf(HashMap<String, String> map );
	/**
	 * 
	 * @methodName  updateDownAssetCcf
	 * @author      wangwh
	 * @date        2016年7月14日
	 * @describe    更新资产分类,如果存在下级，改变下级资产的资产编码和上级编码
	 * @param map
	 * @变动说明       
	 * @version     1.0
	 */
	public void updateDownAssetCcf(HashMap<String, String> map );
	/**
	 * 
	 * @methodName  getOneAssetMaxCode
	 * @author      wangwh
	 * @date        2016年7月14日
	 * @describe    获取一级最大资产编码 
	 * @param property
	 * @变动说明       
	 * @version     1.0
	 */
	public String getOneAssetMaxCode(String property );
	/**
	 * 
	 * @methodName  getTwoAssetMaxCode
	 * @author      wangwh
	 * @date        2016年7月14日
	 * @describe    获取二级最大资产编码
	 * @param property
	 * @变动说明       
	 * @version     1.0
	 */
	public String getTwoAssetMaxCode(String property );
	/**
	 * 
	 * @methodName  getThreeAssetMaxCode
	 * @author      wangwh
	 * @date        2016年7月14日
	 * @describe    获取三级最大资产编码
	 * @param property
	 * @变动说明       
	 * @version     1.0
	 */
	public String getThreeAssetMaxCode(String property );
	/**
	 * 
	 * @methodName  getUpCodeByCatCode
	 * @author      wangwh
	 * @date        2016年7月14日
	 * @describe    根据当前资产编码获取上级资产编码
	 * @param property
	 * @变动说明       
	 * @version     1.0
	 */
	public String getUpCodeByCatCode(String cat_code );
	/**
	 * 
	 * @methodName  getAssetInfoByCatId
	 * @author      wangwh
	 * @date        2016年7月14日
	 * @describe    根据资产ID获取资产信息
	 * @param cat_id
	 * @变动说明       
	 * @version     1.0
	 */
	public XcAsset getAssetInfoByCatId(String cat_id );
	/**
	 * 
	 * @methodName  getDownAssetByCode
	 * @author      wangwh
	 * @date        2016年7月14日
	 * @describe    根据资产编码获取下级资产个数
	 * @param cat_code
	 * @变动说明       
	 * @version     1.0
	 */
	public List<XcAsset>  getDownAssetByCode(String cat_code );
	/**
	 * 
	 * @methodName  getCodeByPropertyName
	 * @author      wangwh
	 * @date        2016年7月17日
	 * @describe    根据资产属性名称获取编码
	 * @param fa_property
	 * @变动说明       
	 * @version     1.0
	 */
	public String getCodeByPropertyName(String fa_property );
	/**
	 * 
	 * @methodName  getAssetByCatName
	 * @author      wangwh
	 * @date        2016年7月17日
	 * @describe    根据资产名称获取资产
	 * @param cat_name
	 * @变动说明       
	 * @version     1.0
	 */
	public XcAsset getAssetByCatName(String cat_name );
	



}
