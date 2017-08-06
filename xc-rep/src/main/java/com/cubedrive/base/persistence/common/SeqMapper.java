package com.cubedrive.base.persistence.common;

import org.springframework.stereotype.Repository;


/**
 * @Title: SeqMapper.java
 * @Package com.feyasoft.persistence.pe
 * @Description: TODO(添加描述)
 * @author libyenter@gmail.com
 * @date 2014-3-10
 * @version V1.0
 */
@Repository
public interface SeqMapper {
	
	int insertSequence(String seqName);
	
	int getNextvalBySeq(String seqName);
	
	
}