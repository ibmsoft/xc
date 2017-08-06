/**
 * 
 */
package com.xzsoft.xc.bg.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.bg.dao.BgItemsBatchDao;
import com.xzsoft.xc.bg.modal.BgItemDTO;
import com.xzsoft.xc.bg.service.BgItemsBatchService;

/**
 * @author tangxl
 *
 */
@Service("bgItemsBatchService")
public class BgItemsBatchServiceImpl implements BgItemsBatchService {
	@Resource
	private BgItemsBatchDao bgItemsBatchDao;

	/* (non-Javadoc)
	 * @name     getBgItemsByLedger
	 * @author   tangxl
	 * @date     2016年7月25日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.bg.service.BgItemsBatchService#getBgItemsByLedger(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public List<BgItemDTO> getBgItemsByLedger(String ledgerId, String bgItemName,String exportModel)
			throws Exception {
		return bgItemsBatchDao.getBgItemsByLedger(ledgerId, bgItemName,exportModel);
	}

	/* (non-Javadoc)
	 * @name     updateImportItems
	 * @author   tangxl
	 * @date     2016年7月26日
	 * @注释                   导入数据修改
	 * @version  1.0
	 * @see com.xzsoft.xc.bg.service.BgItemsBatchService#updateImportItems(java.lang.String, java.util.List, java.lang.String)
	 */
	@Override
	public String updateImportItems(String ledgerId,List<HashMap<String, String>> itemList, String importType)
			throws Exception {
		return bgItemsBatchDao.updateImportItems(ledgerId, itemList, importType);
	}

	/* (non-Javadoc)
	 * @name     方法名
	 * @author   tangxl
	 * @date     2016年7月28日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.bg.service.BgItemsBatchService#deleteTmpImport(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void deleteTmpImport(String ledgerId, String sessionId,
			String importType) throws Exception {
		bgItemsBatchDao.deleteTmpImport(ledgerId,sessionId,importType);
	}

}
