package com.xzsoft.xc.gl.util;

/**
 * @ClassName: XCGLConstants 
 * @Description: 总账系统常量信息
 * @author linp
 * @date 2015年12月22日 下午6:07:12 
 *
 */
public class XCGLConstants {
	
	/*--------------------------------------------
	 * 针对Redis数据库的缓存处理
	 --------------------------------------------*/
	// 新增
	public static final String REDIS_OP_ADD = "add" ;
	// 清除
	public static final String REDIS_OP_CLEAR = "clear" ;
	
	
	/*--------------------------------------------
	 * 规则分类码
	 --------------------------------------------*/
	public static final String RUEL_TYPE_PZ = "ZZ_PZ" ; // 凭证编号规则
	
	// 建账标志: 1-已建账,0-未建账
	public static final String IS_CREATE_BA = "IS_CREATE_BA" ;
	
	// 建账标志: 1-已建账,0-未建账
	public static final String IS_CREATE_CA = "IS_CREATE_CA" ;
	

}
