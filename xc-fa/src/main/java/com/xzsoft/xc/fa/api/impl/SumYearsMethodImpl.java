package com.xzsoft.xc.fa.api.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.stereotype.Service;

import com.xzsoft.xc.fa.api.DeprMethodService;
import com.xzsoft.xc.fa.modal.FaAddition;
import com.xzsoft.xc.fa.modal.FaDepreciation;
import com.xzsoft.xc.fa.util.XcFaUtil;

/**
 * @ClassName: SumYearsMethodImpl
 * @Description: 折旧方法接口类实现(年度总和法)
 * @author zhaoxin
 * @date 2016年8月17日 下午4:47:35
 *
 */

@Service("sumYearsMethodImpl")
public class SumYearsMethodImpl implements DeprMethodService {
    
    @Override
    public FaDepreciation calcDepr(FaAddition faAddition, String periodCode) throws Exception {
        double deprAmt = 0;
        double zjl = 0;
        // 四舍五入之后的折旧率
        double pZjl = 0;
        FaDepreciation faDepreciation = new FaDepreciation();
        // 计算资产折旧年限
        int months = XcFaUtil.getMonths(faAddition.getKsjtqj(), faAddition.getTyqj());
        int years = months / 12;
        int usedMonths = XcFaUtil.getMonths(faAddition.getKsjtqj(), periodCode);
        if (usedMonths <= years * 12) {
            double sumYears = (double) months / 12;
            // 净值
            double netValue = faAddition.getYz() - faAddition.getYz() * faAddition.getCzl() / 100
                            - faAddition.getJzzb() - faAddition.getYjtje();
            double usedYears = usedMonths / 12;
            zjl = (double) (sumYears - usedYears) / XcFaUtil.getYearSum(sumYears) / 12;
            pZjl = new BigDecimal(zjl).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
            deprAmt = (double) netValue * zjl;
        } else {
            int remain = XcFaUtil.getMonths(periodCode, faAddition.getTyqj());
            zjl = 1 / (months - years * 12);
            pZjl = new BigDecimal(zjl).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
            deprAmt = (faAddition.getYz() - faAddition.getYz() * faAddition.getCzl() / 100 - faAddition.getJzzb() - faAddition.getLjzjLjtx()) /remain;
        }
        BigDecimal bg = new BigDecimal(deprAmt);
        faDepreciation.setDepreciationRate(pZjl);
        faDepreciation.setDepreciationAmount(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        return faDepreciation;
    }
}
