package com.xzsoft.xsr.core.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;


public class FileUtil {
	private static final int BUFFER = 2048;
	
	/**
	 * 将参数文件列表中的文件压缩为指定的文件
	 * @param zipfilePath 压缩文件的全名
	 * @param fileList 被压缩 的文件列表， 每个文件都是文件全名
	 * @throws Exception
	 */
	public static void zipFile(String zipfilePath, List<String> fileList) throws Exception{
		BufferedInputStream original = null;
		FileOutputStream dest = new FileOutputStream(zipfilePath);
	    ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
	    byte data[] = new byte[BUFFER];

	    if(!fileList.isEmpty()){
	    	for(String filePath : fileList) {
	    		File file = new File(filePath);
	    		FileInputStream fis = new FileInputStream(file);
	    		original = new BufferedInputStream(fis, BUFFER);
	    		ZipEntry entry = new ZipEntry(file.getName());
	    		 out.putNextEntry(entry);
                int count;
                while((count = original.read(data, 0, BUFFER)) != -1){
                   out.write(data, 0, count);
                }
                original.close();
	    	}
	    }
	    out.close();
	}
	/**
	 * 删除文件
	 * @param filePathAndName
	 */
	public static void delFile(String filePathAndName) throws Exception {
		String filePath = filePathAndName;
		filePath = filePath.toString();
		File delFile = new java.io.File(filePath);
		delFile.delete();
	}
	/**
	 * 新建目录
	 *
	 * @param folderPath
	 *            String 如 c:/fqf
	 * @return boolean
	 */
	public void newFolder(String folderPath) {
		try {
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			if (!myFilePath.exists()) {
				myFilePath.mkdir();
			}
		} catch (Exception e) {
			System.out.println("新建目录操作出错 ");
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * getXlsName:(得到下发文件的名称)
	 *
	 * @param uuid           :uuid
	 * @param periodCode       :期间编号
	 * @version Ver 1.0
	 * @since   Ver 1.0
	 */
	public String getXlsName(String periodCode,String uuid)
	{
		String xlsName = "";
		String periodName = "";
		if(periodCode.length()==7){//2012-02
			periodName = periodCode.replace("-", "") + "M";
		}else if(periodCode.length()==4){
			periodName = periodCode + "Y";
		}else{
			periodName = periodCode.replace("-", "");
		}
		xlsName="JI"+periodName+"-"+uuid+".INI";
		return xlsName;
	}
	/**
	 * 解压文件
	 * @param argv
	 * @throws Exception
	 */
	public void unZip(String argv[]) throws Exception {
		String fileName =  argv[0];
		String zipPath = argv[0].substring(0, argv[0].lastIndexOf(File.separator)+1);
		ZipFile zipFile;
		try {
			zipFile = new ZipFile(fileName);
	    	Enumeration emu = zipFile.getEntries();
	    	ZipEntry entry;
			while (emu.hasMoreElements()){
				entry = (ZipEntry) emu.nextElement();
				BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
				FileOutputStream fos = new FileOutputStream(zipPath+ entry.getName());
				BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
				byte data[] = new byte[BUFFER];
				int count;
				while ((count = bis.read(data, 0, BUFFER)) != -1) {
					dest.write(data, 0, count);
				}
				dest.flush();
				dest.close();
				bis.close();
			}
			zipFile.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("文件解压失败!");
		}
	}
	/**
	 * 将文件的类型转换成小写
	 * @param filename
	 * @return
	 */
	public String fileSuffixesToLowerCase(String fileOldPath){
		// 获取文件后缀
		String fileSuffixes = this.getFileType(fileOldPath).toLowerCase();
		// 最后一个"."分割符位置
		int endIndex = fileOldPath.lastIndexOf(".");
		// 拼装新的文件路径
		String fileNewPath = fileOldPath.substring(0, endIndex+1)+fileSuffixes;
		// 返回新文件路径
		return fileNewPath;
	}
	/**
	 * 根据文件名称获取文件类型
	 * @param filename
	 * @return
	 */
	public String getFileType(String file){
		// 分隔符位置的开始位置
		int beginIndex = 0;
		beginIndex = file.lastIndexOf(".");

		// 结束位置
		int endIndex = 0;
		endIndex = file.length();

		// 文件类型
		String filetype = "";
		filetype = file.substring(beginIndex+1, endIndex);

		// 返回文件类型
		return filetype;
	}
}
