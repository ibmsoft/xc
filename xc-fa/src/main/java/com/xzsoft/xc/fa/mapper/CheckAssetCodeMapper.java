package com.xzsoft.xc.fa.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xzsoft.xc.fa.modal.XcAsset;

public interface CheckAssetCodeMapper {
	public List<XcAsset> getAssetByTagId(String tag_id);
	public void deleteAsset(@Param("idss")String idss);

}
