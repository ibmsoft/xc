package com.xzsoft.xsr.core.service.impl.repCheck;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xzsoft.xsr.core.init.XSRFilepath;
import com.xzsoft.xsr.core.modal.CheckFormula;
import com.xzsoft.xsr.core.modal.VerifyErrorModal;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 对运算后的又问题的稽核结果进行整理，
 * 调整为可以在html中展示的格式
 * 同时生成稽核结果的word文件
 * @author ZhouSuRong
 * @date 2016-3-3
 */
@Service
public class QueryCheckResult {
	private static Logger log = Logger.getLogger(QueryCheckResult.class.getName());
	
	/**
	 * 拼接稽核结果json串
	 * @param resultMap
	 * @param isExistRedisMsg
	 * @param docFullPath
	 * @return
	 * @throws Exception
	 */
	public String[] getCheckResultList(Map<String, List<CheckFormula>> resultMap,
			String isExistRedisMsg, String chkFreeMarkerFilePath) throws Exception {	
		String[] returnArray = new String[2];
		String json = "";
		String path = "";
		StringBuffer sb= new StringBuffer();
		int flag = 0; // 是否存在记录的状态位
		List<VerifyErrorModal> verifyErrorModallist = new ArrayList<VerifyErrorModal>();
		VerifyErrorModal errorModal = null;
		VerifyErrorModal errorSubModal = null;
		VerifyErrorModal errorDetail = null;
		
		try {
			Set<String> keySet = resultMap.keySet();
			for(String key : keySet) {
				int resultNum = 0;
				String oldModalSheetId = null;
				String oldEntityId = null;
				String oldM = null;
				List<CheckFormula> resultList = resultMap.get(key);
				for(CheckFormula c : resultList) {
					String v_logic_name = (null == c.getLOGIC_NAME()) ? "" :  c.getLOGIC_NAME();
					StringBuffer sbstr = new StringBuffer();
					StringBuffer sbstr1 = new StringBuffer();
					StringBuffer sbstr2 = new StringBuffer();
					String modalsheet_id = c.getModalsheet_id();
					if( (modalsheet_id.equals(oldModalSheetId))) {
						resultNum++;
					}else{
						sbstr1.append("<hr/>");
						sbstr1.append("<font color=blue>公司名称:&nbsp;&nbsp;[");
					    sbstr1.append(c.getEntityName());
					    sbstr1.append("]</font><p>");
						sbstr1.append("<font color=blue>报表名称:&nbsp;&nbsp;[").append(c.getMODALSHEET_NAME()).append("]</font><p>");
						resultNum = 1;
					}
					oldModalSheetId = modalsheet_id;
					sbstr2.append(resultNum).append(".");
					sbstr.append("<font color=blue>公式说明：</font>").append(c.getDESCRIPTION());
				    if("S".equals(c.getF_SETTYPE())) {
				    	sbstr.append("「单体公式」");
				    } else if("R".equals(c.getF_SETTYPE())) {
				    	sbstr.append("「行公式」");
				    } else if("C".equals(c.getF_SETTYPE())) {
				    	sbstr.append("「列公式」");
				    } else {
				    	sbstr.append("「单体公式」");
				    }

				    if (!"".equals(v_logic_name)) {
				    	if("GETLEN".equals(v_logic_name)) {
				    		sbstr.append("「逻辑稽核：指标长度」"); 
				    	} else if("ISBLANK".equals(v_logic_name)) {
				    		sbstr.append("「逻辑稽核：指标是否空(0-为空,1-非空)」"); 
				    	} else if("IFLOGIC".equals(v_logic_name)) {
				    		sbstr.append("「逻辑稽核：条件函数」");  
				    	} else if("DECILEN".equals(v_logic_name)) {
				    		sbstr.append("「逻辑稽核：小数位数」"); 
				    	}

					}
				    if(-1 != c.getROWNUMDTL()) {
				    	sbstr.append(" 「浮动行:").append(c.getROWNUMDTL()).append("」");
				    }
				    sbstr.append(";<p> &nbsp;&nbsp;<font color=blue>中文描述：</font>");
				    if(!v_logic_name.equals("IFLOGIC")) {
				    	sbstr.append("【").append(c.getMODALSHEET_NAME()).append("】").append(c.getRowitem_desc())
			             .append(c.getColitem_desc()).append(c.getF_EXP());
				    }
				    sbstr.append(c.getF_DESC());
				   if (v_logic_name.equals("IFLOGIC")) {
					   sbstr.append(";<p> &nbsp;&nbsp;<font color=blue>稽核结果：</font>").append(c.getRightValue())
						    .append("(等式不成立);<p>");
				   }else {
					   if (v_logic_name.equals("ISBLANK")) {
						    sbstr.append(";<p> &nbsp;&nbsp;<font color=blue>稽核结果：</font>");
							if (c.getBanlance() == "1") {
								sbstr.append( "当前指标不为空");
							} else {
								sbstr.append( "当前指标为空");
							}
							sbstr.append("(等式不成立);<p>");
						} else {
							sbstr.append(";<p> &nbsp;&nbsp;<font color=blue>稽核结果：</font>").append(c.getLeftValue()).append(c.getF_EXP())
							.append(c.getRightValue()).append("(等式不成立);<p> &nbsp;&nbsp;<font color=blue>稽核差额：</font> [")
							.append(c.getLeftValue())
							.append("]-[").append(c.getRightValue()).append("]=").append(c.getBanlance());
						 }
				   }
				   sb.append(sbstr1).append(sbstr2).append(sbstr).append("<p>");
				   //导出wrod
				   if (c.getEntityId().equals(oldEntityId)) {
					   if (modalsheet_id.equals(oldM)) {
							String cr = replaceSymbol(sbstr.toString());
							String r = replaceFS(cr);
							String[] array = r.split(";");
							errorDetail = new VerifyErrorModal();
							String r1 = replaceFS(array[1]);
							errorDetail.setDesc(r1);
							errorDetail.setDescription(replaceFS(array[3]));
							// by liuw 2015-06-05 start
							String r2 = replaceN(array[5]);
							errorDetail.setResult(r2);
							// End
							if (array.length >= 8) {
								errorDetail.setAmount(array[7]);
							} else {
								errorDetail.setAmount("");
							}
							errorSubModal.getDetail().add(errorDetail);
					   }else{
						    errorSubModal = new VerifyErrorModal();
						    errorSubModal.setName( "报表名称:["+ c.getMODALSHEET_NAME() + "]\n\n");
							String cr = replaceSymbol(sbstr.toString());
							String r = replaceFS(cr);
							String[] array = r.split(";");
							errorDetail = new VerifyErrorModal();
							String r1 = replaceFS(array[1]);
							errorDetail.setDesc(r1);
							errorDetail.setDescription(array[3]);
							// by liuw 2015-06-05 start
							String r2 = replaceN(array[5]);
							errorDetail.setResult(r2);
							// End
							if (array.length >= 8) {
								errorDetail.setAmount(array[7]);
							} else {
								errorDetail.setAmount("");
							}
							errorSubModal.getDetail().add(errorDetail);
							// added by zhousr 2014-7-31 同一家公司的不同模板信息要添加到modal中
							// start
							errorModal.getSubError().add(errorSubModal);
							// added by zhousr 2014-7-31 同一家公司的不同模板信息要添加到modal中
					   }
					} else {
						if (errorModal != null) {
							verifyErrorModallist.add(errorModal);
						}
						errorModal = new VerifyErrorModal();
						errorModal.setName("公司名称:[" + c.getEntityName() + "]\n\n");
						errorSubModal = new VerifyErrorModal();
						errorSubModal.setName( "报表名称:["+ c.getMODALSHEET_NAME() + "]\n\n");
						String cr = replaceSymbol(sbstr.toString());
						String r = replaceFS(cr);
						String[] array = r.split(";");
						errorDetail = new VerifyErrorModal();
						String r1 = replaceFS(array[1]);
						errorDetail.setDesc(r1);
						errorDetail.setDescription(replaceFS(array[3]));
						// by liuw 2015-06-05 start
						String r2 = replaceN(array[5]);
						errorDetail.setResult(r2);
						// End
						if (array.length >= 8) {
							errorDetail.setAmount(array[7]);
						} else {
							errorDetail.setAmount("");
						}
						errorSubModal.getDetail().add(errorDetail);
						errorModal.getSubError().add(errorSubModal);
					}
				    oldEntityId =c.getEntityId();
				    oldM = modalsheet_id;
					flag++;
				}
				verifyErrorModallist.add(errorModal);
			}
			if (flag > 0) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("Errors", verifyErrorModallist);
				path = createDoc(chkFreeMarkerFilePath, map);
			}
			sb.append(isExistRedisMsg).append("<hr/>");
		} catch(Exception e) {
			log.error("运算稽核结果失败！原因：", e);
		}
		// 拼接JSON串
		json = "{'flag':'1','msg':'"+sb.toString()+"'}";
		returnArray[0] = json;
		returnArray[1] = path;
		return returnArray;
	}

	public String replaceSymbol(String content) {
		content = content.replace("<font color=blue>", " ");
		content = content.replace("</font>", " ");
		content = content.replace("&lt;then&gt;", "，然后，");
		content = content.replace("&nbsp;", " ");
		content = content.replace("<p>", "\n");
		content = content.replace("中文描述:", "\n中文描述:");
		content = content.replace("稽核结果:", "\n  稽核结果：");
		content = content.replace("稽核差额:", "\n  稽核差额");
		content = content.replace("<hr/>", "");
		content = content.replace("&gt;", "大于");
		content = content.replace("&lt;", "小于");
		content = content.replace("公式说明：", ";");
		content = content.replace("中文描述：", ";");
		content = content.replace("稽核结果：", ";");
		content = content.replace("稽核差额：", ";");
		return content;
	}

	public String replaceFS(String content) {
		content = content.replace(">", "大于");
		content = content.replace("<", "小于");
		return content;
	}

	public String replaceN(String content) {
		content = content.replace("(等式不成立)", "");
		return content;
	}
	
	/**
	 * 
	 * @param savePath
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String createDoc(String chkFreemarkerPath, Map<String, Object> map) throws Exception {
		String id = UUID.randomUUID().toString();
		//String readpath = savePath;
		String resultFileName = chkFreemarkerPath + File.separator + "temp" + File.separator + "checkResult-" + id + ".doc";

		Configuration cfg = null;
		Template t = null;
		Writer out = null;
		try {
			if (cfg == null) {
				cfg = new Configuration();
				cfg.setDefaultEncoding("utf-8");
			}
			cfg.setDirectoryForTemplateLoading(new File(chkFreemarkerPath));
			cfg.setObjectWrapper(new DefaultObjectWrapper());
			t = cfg.getTemplate(XSRFilepath.chkFreemarkerName);
			
			System.out.println(resultFileName);
			File outFile = new File(resultFileName);
			if (!outFile.exists()) {
				outFile.createNewFile();
			}
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"));
			t.process(map, out);
		} catch (Exception e) {
			log.error("生成word文档失败！原因：", e);
		} finally {
			if (null != out) {
				out.close();
			}
		}
		return resultFileName;
	}
}
