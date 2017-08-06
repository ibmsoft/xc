package com.xzsoft.xc.gl.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.gl.dao.VInterfaceDao;
import com.xzsoft.xc.gl.mapper.VInterfaceMapper;
import com.xzsoft.xc.gl.modal.VInterface;

@Repository("vInterfaceDao")
public class VInterfaceDaoImpl implements VInterfaceDao {
	@Resource
	private VInterfaceMapper vInterfaceMapper;

	@Override
	public void insertVInterface(VInterface vInterface) {
		vInterfaceMapper.insertVInterface(vInterface);
	}

	@Override
	public List<VInterface> getVInterfaceBySrcHid(String srcHid) {
		return vInterfaceMapper.getVInterfaceBySrcHid(srcHid);
	}

	@Override
	public void deleteVInterfaceBySrcHid(String srcHid) {
		vInterfaceMapper.deleteVInterfaceBySrcHid(srcHid);
	}

    @Override
    public void deleteVInterfaceBySrc(String srcHId, String srcLId, String transCode) {
        vInterfaceMapper.deleteVInterfaceBySrc(srcHId, srcLId, transCode);
    }

    @Override
    public String getVHeaderId4Interface(String srcHId, String srcLId, String transCode) {
        return vInterfaceMapper.getVHeaderId4Interface(srcHId, srcLId, transCode);
    }

}
