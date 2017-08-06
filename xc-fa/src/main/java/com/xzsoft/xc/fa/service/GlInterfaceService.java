package com.xzsoft.xc.fa.service;

public interface GlInterfaceService {
	
	public String saveInterface(String glInterface, String glInterfaceIds, String glInterfaceHead, String userId) throws Exception;
	
	public String submitGlInterface(String vHeadId, String iHeadId) throws Exception;
	
	public String draftGlInterface(String vHeadId, String iHeadId) throws Exception;
	
	public String deleteGlInterface(String vHeadId, String iHeadId, String userId) throws Exception;
}
