package com.xzsoft.xc.fa.api.impl;



import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.xzsoft.xc.fa.api.DeprMethodService;
import com.xzsoft.xc.fa.modal.FaAddition;
import com.xzsoft.xc.fa.modal.FaDepreciation;
import com.xzsoft.xc.fa.util.XcFaUtil;
/**
 * @ClassName: CompositeLifeMethodImpl 
 * @Description: 折旧方法接口类实现(平均年限法)
 * @author zhaoxin
 * @date 2016年8月17日 下午4:47:35 
 *
 */
@Service("compositeLifeMethodImpl")
public class CompositeLifeMethodImpl implements DeprMethodService{

	@Override
	public FaDepreciation calcDepr(FaAddition faAddition, String periodCode)
			throws Exception {
		//本期折旧金额
		double deprAmt = 0;
		//净值
		double jz = (faAddition.getYz() - faAddition.getYz()*faAddition.getCzl()/100 - faAddition.getJzzb() - faAddition.getLjzjLjtx());
		//本期折旧率
		deprAmt = (double)jz /XcFaUtil.getMonths(periodCode, faAddition.getTyqj());
		double zjl = (double)deprAmt/(faAddition.getYz() - faAddition.getYz()*faAddition.getCzl()/100 - faAddition.getJzzb());
		//四舍五入之后的折旧率
		double pZjl = new BigDecimal(zjl).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
		
		
		BigDecimal bg = new BigDecimal(deprAmt);
		
		FaDepreciation faDepreciation = new FaDepreciation();
		faDepreciation.setDepreciationRate(pZjl);
		faDepreciation.setDepreciationAmount(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		
		return faDepreciation;
	}
}
