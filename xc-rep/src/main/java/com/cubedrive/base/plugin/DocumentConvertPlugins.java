package com.cubedrive.base.plugin;

import java.io.File;

import com.cubedrive.base.exception.DocumentConvertError;

public class DocumentConvertPlugins {
	
	private static final DocumentConvertPlugins _instance = new DocumentConvertPlugins();
	
	private DocumentConvertPlugin _plugin;
	
	private DocumentConvertPlugins(){
		
	}
	
	public void installPlugin(DocumentConvertPlugin documentConvertPlugin){
		this._plugin = documentConvertPlugin;
	}
	
	public void uninstallPlugin(DocumentConvertPlugin documentConvertPlugin){
		if(this._plugin == documentConvertPlugin){
			this._plugin = null;
		}
	}
	
	public DocumentConvertPlugin getPlugin(){
		if(this._plugin == null)
			return dummyPlugin;
		else
			return _plugin;
		
	}
	
	
	public static interface DocumentConvertPlugin{
		
		void convertFile(File sourceFile, File destFile, String fileType);
		
	}
	
	public static DocumentConvertPlugins instance(){
		return _instance;
	}
	
	
	private static final DummyDocumentConvertPlugin dummyPlugin = new DummyDocumentConvertPlugin();
	
	private static class DummyDocumentConvertPlugin implements DocumentConvertPlugin{

		@Override
		public void convertFile(File sourceFile, File destFile, String fileType) {
			throw new DocumentConvertError(sourceFile,destFile,fileType);
		}
		
	}
	
	

}
