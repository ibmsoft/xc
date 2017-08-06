package com.xzsoft.xc.gl.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;
/**
 * @fileName GlTreelDataValidMapper
 * @desc (总账树形基础数据二次校验接口) 
 * @author tangxl
 * @date 2016年5月5日
 */
public interface GlTreelDataValidMapper {
	/**
	 * 
	 * @title:        checkUpProjectIsValid
	 * @description:  校验项目的上级编码是否存在
	 * @author        tangxl
	 * @date          2016年5月13日
	 * @param         sessionId
	 */
	public void checkUpProjectIsValid(@Param(value="sessionId")String sessionId);
	/**
	 * 
	 * @title:        checkUpAccountIsValid
	 * @description:  校验科目的上级编码是否存在
	 * @author        tangxl
	 * @date          2016年5月13日
	 * @param         sessionId
	 */
	public void checkUpAccountIsValid(@Param(value="sessionId")String sessionId);
	/**
	 * 
	 * @title:        checkUpCashIsValid
	 * @description:  校验科目的上级现金流编码是否存在
	 * @author        tangxl
	 * @date          2016年5月13日
	 * @param         sessionId
	 */
	public void checkUpCashIsValid(@Param(value="sessionId")String sessionId);
	/**
	 * 
	 * @title:        checkUpSegmentIsValid
	 * @description:  校验自定义段的上级现金流编码是否存在
	 * @author        tangxl
	 * @date          2016年5月13日
	 * @param         sessionId
	 */
	public void checkUpSegmentIsValid(@Param(value="sessionId")String sessionId,@Param(value="segCode")String segCode);
	/**
	 * 
	 * @title:        updateUpProject
	 * @description:  更新项目上级id
	 * @author        tangxl
	 * @date          2016年5月13日
	 * @param         sessionId
	 */
	public void updateUpProject(@Param(value="sessionId")String sessionId,@Param(value="ledgerId")String ledgerId);
	/**
	 * 
	 * @title:        updateProjectItself
	 * @description:  自更新项目的id
	 * @author        tangxl
	 * @date          2016年5月13日
	 * @param         sessionId
	 */
	public int updateProjectItself(@Param(value="sessionId")String sessionId,@Param(value="ledgerId")String ledgerId);
	/**
	 * 
	 * @title:        updateUpAccount
	 * @description:  更新科目上级层级和是否为叶子节点属性
	 * @author        tangxl
	 * @date          2016年5月13日
	 * @param         sessionId
	 */
	public void updateUpAccount(@Param(value="sessionId")String sessionId);
	/**
	 * 
	 * @title:        updateUpAccountItself
	 * @description:   更新科目上级层级和是否为叶子节点属性
	 * @author        tangxl
	 * @date          2016年5月13日
	 * @param         sessionId
	 */
	public int updateUpAccountItself(@Param(value="sessionId")String sessionId);
	/**
	 * 
	 * @title:        updateUpCashItem
	 * @description:  更新现金流项目上级层级和是否为叶子节点属性
	 * @author        tangxl
	 * @date          2016年5月13日
	 * @param         sessionId
	 */
	public void updateUpCashItem(@Param(value="sessionId")String sessionId);
	/**
	 * 
	 * @title:        updateUpCashItemItself
	 * @description:  更新现金流项目上级层级和是否为叶子节点属性
	 * @author        tangxl
	 * @date          2016年5月13日
	 * @param         sessionId
	 */
	public int updateUpCashItemItself(@Param(value="sessionId")String sessionId);
	/**
	 * 
	 * @title:        updateUpSegment
	 * @description:  更新自定义项目上级层级和是否为叶子节点属性
	 * @author        tangxl
	 * @date          2016年5月13日
	 * @param         sessionId
	 */
	public void updateUpSegment(@Param(value="sessionId")String sessionId,@Param(value="ledgerId")String ledgerId);
	/**
	 * 
	 * @title:        updateUpSegmentItself
	 * @description:  更新自定义项目上级层级和是否为叶子节点属性
	 * @author        tangxl
	 * @date          2016年5月13日
	 * @param         sessionId
	 */
	public int updateUpSegmentItself(@Param(value="sessionId")String sessionId,@Param(value="ledgerId")String ledgerId);
	/**
	 * 
	 * @title:        getAllImportData.java
	 * @description:  返回本次导入的所有有效记录
	 * @author        tangxl
	 * @date          2016年5月16日
	 * @param         sessionId
	 * @return
	 */
	public List<HashMap> getAllImportData(@Param(value="sessionId")String sessionId,@Param(value="tableName")String tableName);
	/**
	 * 
	 * @title:       insertProjectData
	 * @description: 插入项目数据到正式表中
	 * @author       tangxl
	 * @date         2016年5月16日
	 * @param        map
	 */
	public void insertProjectData(HashMap<String, String> map);
	/**
	 * 
	 * @title:       insertAccountData
	 * @description: 插入科目数据到正式表中
	 * @author       tangxl
	 * @date         2016年5月16日
	 * @param        map
	 */
	public void insertAccountData(HashMap<String, String> map);
	/**
	 * 
	 * @title:       insertAccountData
	 * @description: 插入现金流项目数据到正式表中
	 * @author       tangxl
	 * @date         2016年5月16日
	 * @param        map
	 */
	public void insertCashItemData(HashMap<String, String> map);
	/**
	 * @title:       insertSegmentData
	 * @description: 插入辅助段数据到正式表中
	 * @author       tangxl
	 * @date         2016年5月16日
	 * @param        map
	 */
	public void insertSegmentData(HashMap<String, String> map);
	/**
	 * 
	 * @title:       getMaxCashOrder
	 * @description: 获取现金流的最大排序号
	 * @author       tangxl
	 * @date         2016年5月9日
	 * @param        sessionId
	 * @return       
	 * @throws       Exception
	 */
	public int getMaxCashOrder() throws Exception;
	/**
	 * 
	 * @methodName  checkUpBgItemIsValid
	 * @author      tangxl
	 * @date        2016年6月16日
	 * @describe    校验预算项目上级项目是否存在有效
	 * @param sessionId
	 * @变动说明       
	 * @version     1.0
	 */
	public void checkUpBgItemIsValid(@Param(value="sessionId")String sessionId);
	/**
	 * 
	 * @methodName  checkUpExItemIsValid
	 * @author      tangxl
	 * @date        2016年6月16日
	 * @describe    校验费用项目上级项目是否存在有效
	 * @param sessionId
	 * @变动说明       
	 * @version     1.0
	 */
	public void checkUpExItemIsValid(@Param(value="sessionId")String sessionId);
	/**
	 * 
	 * @methodName  updateUpBgItem
	 * @author      tangxl
	 * @date        2016年6月16日
	 * @describe    更新预算项目的上级项目id(上级项目存在于正式表中)
	 * @param sessionId
	 * @param bgHrcyId
	 * @变动说明       
	 * @version     1.0
	 */
	public void updateUpBgItem(@Param(value="sessionId")String sessionId,@Param(value="bgHrcyId")String bgHrcyId);
	/**
	 * 
	 * @methodName  updateBgItemItself
	 * @author      tangxl
	 * @date        2016年6月16日
	 * @describe    更新预算项目的上级项目id(上级项目存在于临时表中)
	 * @param sessionId
	 * @param ledgerId
	 * @return
	 * @变动说明       
	 * @version     1.0
	 */
	public int updateBgItemItself(@Param(value="sessionId")String sessionId,@Param(value="bgHrcyId")String bgHrcyId);
	/**
	 * 
	 * @methodName  updateUpExItem
	 * @author      tangxl
	 * @date        2016年6月16日
	 * @describe    更新费用项目的上级项目id(上级项目存在于正式表中)
	 * @param sessionId
	 * @param exHrcyId
	 * @变动说明       
	 * @version     1.0
	 */
	public void updateUpExItem(@Param(value="sessionId")String sessionId,@Param(value="exHrcyId")String exHrcyId);
	/**
	 * 
	 * @methodName  updateexItemItself
	 * @author      tangxl
	 * @date        2016年6月16日
	 * @describe    更新费用项目的上级项目id(上级项目存在于临时表中)
	 * @param sessionId
	 * @param exHrcyId
	 * @return
	 * @变动说明       
	 * @version     1.0
	 */
	public int updateExItemItself(@Param(value="sessionId")String sessionId,@Param(value="exHrcyId")String exHrcyId);
	/**
	 * 
	 * @methodName  insertBgItemData
	 * @author      tangxl
	 * @date        2016年6月16日
	 * @describe    插入预算项目数据到正式表
	 * @param map
	 * @变动说明       
	 * @version     1.0
	 */
	public void insertBgItemData(HashMap<String, String> map);
	/**
	 * 
	 * @methodName  insertExItemData
	 * @author      tangxl
	 * @date        2016年6月16日
	 * @describe    插入费用项目数据到正式表
	 * @param map
	 * @变动说明       
	 * @version     1.0
	 */
	public void insertExItemData(HashMap<String, String> map);
	
}
