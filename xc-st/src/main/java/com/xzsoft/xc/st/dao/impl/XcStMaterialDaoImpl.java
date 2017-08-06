package com.xzsoft.xc.st.dao.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.st.dao.XcStMaterialDao;
import com.xzsoft.xc.st.mapper.XcStMaterialMapper;
import com.xzsoft.xc.st.modal.XcStMaterialBean;
/**
 * 
  * @ClassName XcStMaterialDaoImpl
  * @Description 处理物资信息的dao实现类
  * @author RenJianJian
  * @date 2016年11月30日 下午12:01:31
 */
@Repository("xcStMaterialDao")
public class XcStMaterialDaoImpl implements XcStMaterialDao {
	@Resource
	private XcStMaterialMapper xcStMaterialMapper;
	/*
	 * 
	  * <p>Title insertXcStMaterial</p>
	  * <p>Description 增加操作</p>
	  * @param stMaterial
	  * @throws Exception
	  * @see com.xzsoft.xc.st.dao.XcStMaterialHandelDao#insertXcStMaterial(com.xzsoft.xc.st.modal.XcStMaterialBean)
	 */
	@Override
	public void insertXcStMaterial(XcStMaterialBean stMaterial) throws Exception {
		try {
			xcStMaterialMapper.insertXcStMaterial(stMaterial);
		} catch (Exception e) {
			throw e;
		}
	}
	/*
	 * 
	  * <p>Title updateXcStMaterial</p>
	  * <p>Description 修改操作</p>
	  * @param stMaterial
	  * @throws Exception
	  * @see com.xzsoft.xc.st.dao.XcStMaterialHandelDao#updateXcStMaterial(com.xzsoft.xc.st.modal.XcStMaterialBean)
	 */
	@Override
	public void updateXcStMaterial(XcStMaterialBean stMaterial) throws Exception {
		try {
			xcStMaterialMapper.updateXcStMaterial(stMaterial);
		} catch (Exception e) {
			throw e;
		}
	}
	/*
	 * 
	  * <p>Title deleteXcStMaterial</p>
	  * <p>Description 删除操作</p>
	  * @param materialIds
	  * @throws Exception
	  * @see com.xzsoft.xc.st.dao.XcStMaterialHandelDao#deleteXcStMaterial(java.util.List)
	 */
	@Override
	public void deleteXcStMaterial(List<String> materialIds) throws Exception {
		try {
			xcStMaterialMapper.deleteXcStMaterial(materialIds);
		} catch (Exception e) {
			throw e;
		}
	}
	/*
	 * 
	  * <p>Title xcStMaterialWhetherCited</p>
	  * <p>Description 判断当前操作的物资是否被引用</p>
	  * @param MATERIAL_ID
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.st.dao.XcStMaterialDao#xcStMaterialWhetherCited(java.lang.String)
	 */
	@Override
	public int xcStMaterialWhetherCited(String MATERIAL_ID) throws Exception {
		try {
			return xcStMaterialMapper.xcStMaterialWhetherCited(MATERIAL_ID);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public void deleteXcStMaterialParams(List<String> materialParamsIds) throws Exception {
		try {
			xcStMaterialMapper.deleteXcStMaterialParams(materialParamsIds);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public String xcStMaterialParamsWhetherCited(String MATERIAL_ID, String ORG_ID) throws Exception {
		try {
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("MATERIAL_ID", MATERIAL_ID);
			map.put("ORG_ID", ORG_ID);
			return xcStMaterialMapper.xcStMaterialParamsWhetherCited(map);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public String stMaterialCodeOrNameIsExists(String MATERIAL_CODE,String MATERIAL_NAME, String MAT_TYPE_SET_ID, String MATERIAL_ID) throws Exception {
		try {
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("MATERIAL_CODE", MATERIAL_CODE);
			map.put("MATERIAL_NAME", MATERIAL_NAME);
			map.put("MAT_TYPE_SET_ID", MAT_TYPE_SET_ID);
			map.put("MATERIAL_ID", MATERIAL_ID);
			return xcStMaterialMapper.stMaterialCodeOrNameIsExists(map);
		} catch (Exception e) {
			throw e;
		}
	}
}
