package com.xzsoft.xc.fa.api;

import com.xzsoft.xc.fa.modal.FaAddition;
import com.xzsoft.xc.fa.modal.FaDepreciation;

/**
 * @ClassName: DeprMethodService 
 * @Description: 折旧方法接口类
 * @author zhaoxin
 * @date 2016年8月17日 下午4:33:35 
 *
 */
public interface DeprMethodService {

	/**
	 * calcDepr:(计算折旧)
	 *
	 * @param faAddition 资产Bean
	 * @param periodCode 期间
	 * @return 折旧金额
	 * @throws Exception
	 * @author GuoXiuFeng
	 * @version Ver 1.0
	 * @return 
	 * @since   Ver 1.0
	*/
	public FaDepreciation calcDepr(FaAddition faAddition,String periodCode) throws Exception ;
	
}
