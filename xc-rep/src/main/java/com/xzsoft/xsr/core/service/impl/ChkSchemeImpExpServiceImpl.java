package com.xzsoft.xsr.core.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xzsoft.xsr.core.modal.Chk_Scheme;
import com.xzsoft.xsr.core.mapper.CheckFormulaMapper;
import com.xzsoft.xsr.core.modal.CheckFormula;
import com.xzsoft.xsr.core.service.ChkSchemeImpExpService;

/**
 * @类名 : ChkSchemeImpExpServiceImpl
 * @author: liuwl
 * @date: 2016年1月12日
 */
@Service
public class ChkSchemeImpExpServiceImpl implements ChkSchemeImpExpService {
	@Autowired
	private CheckFormulaMapper checkFormulaMapper;
	public Logger log = Logger.getLogger(this.getClass());

	/**
	 * 稽核方案导出
	 * 
	 * @throws Exception
	 * 
	 */
	@Override
	public void ChkSchemeExp(String filepath, String chk_scheme_id) throws Exception {
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("EXPCHKSCHEME");
		ArrayList<CheckFormula> chk_FormulaList = new ArrayList<CheckFormula>();
		Chk_Scheme chk_Scheme = new Chk_Scheme();
		// 执行数据导出
		chk_Scheme = checkFormulaMapper.selectChkSchemeById(chk_scheme_id);
		chk_FormulaList = checkFormulaMapper.selectChkFormulaByScheme(chk_scheme_id);

		Element SCHEME = root.addElement("SCHEME");

		Element CHK_SCHEME_CODE = SCHEME.addElement("CHK_SCHEME_CODE");
		CHK_SCHEME_CODE.setText(chk_Scheme.getCHK_SCHEME_CODE());

		Element CHK_SCHEME_NAME = SCHEME.addElement("CHK_SCHEME_NAME");
		CHK_SCHEME_NAME.setText(chk_Scheme.getCHK_SCHEME_NAME());
		if (!chk_FormulaList.isEmpty()) {
			for (CheckFormula cf : chk_FormulaList) {
				Element CHK_FORMULA = SCHEME.addElement("CHK_FORMULA");
				String ROWITEM_CODE = checkFormulaMapper.selectRowitem_Code(cf.getROWITEM_ID());
				String COLITEM_CODE = checkFormulaMapper.selectColitem_Code(cf.getCOLITEM_ID());
				CHK_FORMULA.addAttribute("ROWITEM_CODE", ROWITEM_CODE);
				CHK_FORMULA.addAttribute("COLITEM_CODE", COLITEM_CODE);
				CHK_FORMULA.addAttribute("F_LEFT", cf.getF_LEFT());
				CHK_FORMULA.addAttribute("F_RIGHT", cf.getF_RIGHT());
				CHK_FORMULA.addAttribute("F_EXP", cf.getF_EXP());
				CHK_FORMULA.addAttribute("F_DESC", cf.getF_DESC());
				CHK_FORMULA.addAttribute("ERR_SCOPE", cf.getERR_SCOPE());
				CHK_FORMULA.addAttribute("DESCRIPTION", cf.getDESCRIPTION());
				CHK_FORMULA.addAttribute("ENABL_FLAG", cf.getENABL_FLAG());
				CHK_FORMULA.addAttribute("F_SETTYPE", cf.getF_SETTYPE());
				CHK_FORMULA.addAttribute("LOGIC_FLAG", cf.getLOGIC_FLAG());
				CHK_FORMULA.addAttribute("LOGIC_NAME", cf.getLOGIC_NAME());
				CHK_FORMULA.addAttribute("ZL_FLAG", cf.getZL_FLAG());
				CHK_FORMULA.addAttribute("PERIOD_FLAG", cf.getPERIOD_FLAG());
				CHK_FORMULA.addAttribute("CHK_TYPE", cf.getCHK_TYPE());
				CHK_FORMULA.addAttribute("CHK_LEVEL", cf.getCHK_LEVEL());
			}
		}
		// 实例化输出格式对象
		OutputFormat format = OutputFormat.createPrettyPrint();
		// 设置输出编码
		format.setEncoding("UTF-8");
		// 创建需要写入的File对象
		// 生成XMLWriter对象，构造函数中的参数为需要输出的文件流和格式
		XMLWriter writer = new XMLWriter(new FileOutputStream(filepath), format);
		// 开始写入，write方法中包含上面创建的Document对象
		writer.write(doc);
		writer.close();

	}

	/**
	 * 稽核方案导入
	 * 
	 * @throws Exception
	 * 
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public String ChkSchemeImp(File file, String suitId) throws Exception {
		String message = "{flag:0,msg:'导入稽核公式成功'}";

		// dom4j读取XML文件；
		SAXReader sax = new SAXReader();
		org.dom4j.Document document = null;
		document = sax.read(file);

		// 获取文件根节点；
		org.dom4j.Element root = document.getRootElement();
		// 获取文件SCHEME节点；
		org.dom4j.Element SCHEME = root.element("SCHEME");
		// 得到稽核方案信息；
		Chk_Scheme chk_scheme = new Chk_Scheme();
		org.dom4j.Element CHK_SCHEME_CODE = SCHEME.element("CHK_SCHEME_CODE");
		org.dom4j.Element CHK_SCHEME_NAME = SCHEME.element("CHK_SCHEME_NAME");
		// 判断稽核方案是否存在
		int count = checkFormulaMapper.isChkSchemeExist(CHK_SCHEME_CODE.getText(), suitId);
		if (count > 0) {
			message = "{flag:1,msg:'稽核方案【" + CHK_SCHEME_NAME.getText() + "】已经存在'}";
		} else {
			String SCHEME_ID = UUID.randomUUID().toString();
			chk_scheme.setCHK_SCHEME_ID(SCHEME_ID);
			chk_scheme.setSUIT_ID(suitId);
			chk_scheme.setCHK_SCHEME_CODE(CHK_SCHEME_CODE.getText());
			chk_scheme.setCHK_SCHEME_NAME(CHK_SCHEME_NAME.getText());
			// 插入稽核方案表；
			checkFormulaMapper.insertChkScheme(chk_scheme);

			for (Iterator j = SCHEME.elementIterator("CHK_FORMULA"); j.hasNext();) {
				org.dom4j.Element element = (org.dom4j.Element) j.next();
				HashMap<String, String> params = new HashMap<String, String>();

				String ROWITEM_CODE = element.attributeValue("ROWITEM_CODE");
				String COLITEM_CODE = element.attributeValue("COLITEM_CODE");
				
				params.put("ROWITEM_CODE",ROWITEM_CODE);
				params.put("COLITEM_CODE",COLITEM_CODE);
				params.put("SUIT_ID", suitId);
				params.put("F_LEFT", element.attributeValue("F_LEFT"));
				params.put("F_RIGHT", element.attributeValue("F_RIGHT"));
				params.put("F_EXP", element.attributeValue("F_EXP"));
				params.put("F_DESC", element.attributeValue("F_DESC"));
				params.put("ERR_SCOPE", element.attributeValue("ERR_SCOPE"));
				params.put("DESCRIPTION", element.attributeValue("DESCRIPTION"));
				params.put("ENABL_FLAG", element.attributeValue("ENABL_FLAG"));
				params.put("F_SETTYPE", element.attributeValue("F_SETTYPE"));
				params.put("LOGIC_FLAG", element.attributeValue("LOGIC_FLAG"));
				params.put("LOGIC_NAME", element.attributeValue("LOGIC_NAME"));
				params.put("ZL_FLAG", element.attributeValue("ZL_FLAG"));
				params.put("PERIOD_FLAG", element.attributeValue("PERIOD_FLAG"));
				params.put("CHK_TYPE", element.attributeValue("CHK_TYPE"));
				params.put("CHK_LEVEL", element.attributeValue("CHK_LEVEL"));
				if (isExitItemCode(ROWITEM_CODE, COLITEM_CODE, suitId)) {
					params.put("COLITEM_ID",checkFormulaMapper.selectColitem_Id(COLITEM_CODE, suitId));
					params.put("ROWITEM_ID",checkFormulaMapper.selectRowitem_Id(ROWITEM_CODE, suitId));
					String formulaid=checkFormulaMapper.selectFormulaId(params);
					if (formulaid==""||formulaid==null) {
						String CHK_FORMULA_ID = UUID.randomUUID().toString();
						params.put("CHK_FORMULA_ID",CHK_FORMULA_ID);
						checkFormulaMapper.insertChkFormula(params);
						checkFormulaMapper.insertChkSchemeDtl(SCHEME_ID, CHK_FORMULA_ID);
					} else {
						checkFormulaMapper.insertChkSchemeDtl(SCHEME_ID, formulaid);
					}
				} else {
					message = "{flag:0,msg:'导入稽核方案失败，指标编码不存在'}";
				}

			}
		}
		return message;
	}

	/**
	 * 判断行列指标编码是否存在
	 * 
	 * @throws Exception
	 * @return boolean
	 */
	public boolean isExitItemCode(String ROWITEM_CODE, String COLITEM_CODE, String SUIT_ID) {
		int countRow = 0;
		int countCol = 0;
		boolean isExit = false;
		try {
			if (!ROWITEM_CODE.equals("<$$>") && !COLITEM_CODE.equals("<$$>")) {

				countRow = checkFormulaMapper.isRowItemExit(ROWITEM_CODE, SUIT_ID);
				countCol = checkFormulaMapper.isColItemExit(COLITEM_CODE, SUIT_ID);
				if (countRow > 0 && countCol > 0) {
					isExit = true;
				}
			} else if (ROWITEM_CODE.equals("<$$>")) {
				countCol = checkFormulaMapper.isColItemExit(COLITEM_CODE, SUIT_ID);
				if (countCol > 0) {
					isExit = true;
				}
			} else if (COLITEM_CODE.equals("<$$>")) {
				countRow = checkFormulaMapper.isRowItemExit(ROWITEM_CODE, SUIT_ID);
				if (countRow > 0) {
					isExit = true;
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return isExit;
	}
}
