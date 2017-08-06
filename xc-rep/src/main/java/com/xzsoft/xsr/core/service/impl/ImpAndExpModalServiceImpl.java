package com.xzsoft.xsr.core.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xsr.core.mapper.CellFormulaMapper;
import com.xzsoft.xsr.core.mapper.CheckFormulaMapper;
import com.xzsoft.xsr.core.mapper.ModalsheetMapper;
import com.xzsoft.xsr.core.modal.CellFormula;
import com.xzsoft.xsr.core.modal.CheckFormula;
import com.xzsoft.xsr.core.modal.Colitem;
import com.xzsoft.xsr.core.modal.Item_Property;
import com.xzsoft.xsr.core.modal.ModalSheet;
import com.xzsoft.xsr.core.modal.ModalSheetElement;
import com.xzsoft.xsr.core.modal.ModalSheetFormat;
import com.xzsoft.xsr.core.modal.ModalType;
import com.xzsoft.xsr.core.modal.Rowitem;
import com.xzsoft.xsr.core.service.ImpAndExpModalService;

/**
 * 模板导出，模板接收操作类
 * 1. 模板接收-接收模板集-通过[表套id，模板集code]判断模板集是否存在。xsr_rep_modaltype表，[表套id, 模板集code]是唯一索引。
 * 2. 接收模板-通过[表套id, 模板集id, 模板code]判断模板是否存在;xsr_rep_modalsheet表上是[表套id, 模板集id, 模板code]唯一;
 * 3. 接收行指标-通过[表套id, 行指标code]判断行指标是否存在;xsr_rep_rowitem表上是[表套id, 行指标code]唯一;
 * 4. 接收列指标-通过[表套id, 列指标code]判断列指标是否存在;xsr_rep_colitem表上是[表套id, 列指标code]唯一;
 * 5. 接收采集公式-通过[表套id, 行指标id, 列指标id, 公司id='-1']判断公式是否存在;xsr_rep_cal_formula表上是[表套id, 行指标id, 列指标id, 公司id]唯一;
 * 6. 接收稽核公式-通过[表套id, 行指标id, 列指标id, 公式右边]判断公式是否存在;xsr_rep_chk_formula表上未建立唯一索引。
 * 涉及到表：
 * 1. xsr_rep_modaltype 判断是否存在；存在，则更新； 不存在，则插入；
 * 2. xsr_rep_modalsheet 判断是否存在；存在，则更新； 不存在，则插入；
 * 3. xsr_rep_modalsheet_format 先删除后插入；
 * 4. xsr_rep_modalsheet_element 先删除后插入；
 * 5. xsr_rep_rowitem 判断是否存在；存在，则更新； 不存在，则插入；
 * 6. xsr_rep_colitem 判断是否存在；存在，则更新； 不存在，则插入；
 * 7. xsr_rep_rowmodalref 先删除后插入；
 * 8. xsr_rep_colmodalref 先删除后插入；
 * 9. xsr_rep_fj_colitem_set 先删除后插入；
 * 10. xsr_rep_item_property 先删除后插入；
 * 11. xsr_rep_cal_formula 判断是否存在；存在，则更新； 不存在，则插入；
 * 12. xsr_rep_chk_formula 判断是否存在；存在，则更新； 不存在，则插入；/ 先删除后插入；
 * @author ZhouSuRong
 * @date 2016-1-11
 */
@Service
public class ImpAndExpModalServiceImpl implements ImpAndExpModalService{
	
	@Autowired
	private ModalsheetMapper modalsheetMapper;
	@Autowired
	private CellFormulaMapper cellFormulaMapper;
	@Autowired
	private CheckFormulaMapper checkFormulaMapper;

	@Override
	public List<String> exportModal(String[] params) throws Exception {
		//存放生成的xml文件路径
		List<String> fileList = new ArrayList<String>();
		String suitId = params[0];
		String modaltypeId = params[1];
		String modalsheetCodeNames = params[2]; 
		String modalsheetIds = params[3];
		String fileDir = params[4];
		/**
		 * 开始生成xml
		 */
		/**
		 * 1. 模板集生成一个单独的文件， 模板的文件名称固定：modaltype.xml
		 */
		String filePath = fileDir+File.separator+"modaltype.xml";
		getModaltypeDom(modaltypeId, filePath, modalsheetCodeNames);
		
		fileList.add(filePath);
		/**
		 * 2. 每个模板生成一个xml文件
		 */
		String[] modalsheetIdArray = modalsheetIds.split(",");
		for(String modalsheetId : modalsheetIdArray) {
			params[3] = modalsheetId;
			filePath = getModalsheetDom(params);
			fileList.add(filePath);
		}
		return fileList;
	}
	/**
	 * 通过模板集id查询到模板集的信息，拼出xml文件
	 * 子节点<modalsheet>包括此次所有的模板编码
	 * @param modaltypeId
	 * @param filepath
	 * @param modalsheetCodes
	 * @throws Exception
	 */
	private void getModaltypeDom(String modaltypeId, String filepath, String modalsheetCodeNames) throws Exception {
		//DocumentHelper提供了创建Document对象的方法  
        Document document = DocumentHelper.createDocument();  
        //添加modaltype根节点 
        Element modaltypeEle = document.addElement("ModalType");  
        ModalType mt = modalsheetMapper.getModaltype(modaltypeId);
        //modaltype根节点增加属性
        //modaltypeEle.addAttribute("id", mt.getMODALTYPE_ID());
        modaltypeEle.addAttribute("code", mt.getMODALTYPE_CODE());
        modaltypeEle.addAttribute("name", mt.getMODALTYPE_NAME());
        modaltypeEle.addAttribute("periodType", mt.getPERIOD_TYPE());
        //稽核方案都会下发，所以可以带上稽核方案ID
        //modaltypeEle.addAttribute("chkSchemeId", mt.getCHK_SCHEME_ID());
        //modaltype根节点增加子节点
        String[] modalsheetCodeNameArray = modalsheetCodeNames.split(",");
        for(String codeName : modalsheetCodeNameArray) {
        	Element modalsheetEle = modaltypeEle.addElement("ModalSheet");
        	modalsheetEle.setText(codeName);
        }
        outputXml(filepath, document);
        //将document文档对象直接转换成字符串输出  
        //System.out.println(document.asXML()); 
	}
	/**
	 * 生成modalsheet xml文件
	 */
	private String getModalsheetDom(String[] params) throws Exception {
		String suitId = params[0];
		String modaltypeId = params[1];
		String modalsheetId = params[3];
		String fileDir = params[4];
		String isExpCalFormula = params[5];
		String isOnlyExpRepFormula = params[6];
		String isExpChkFormula = params[7];
		
		Document document = DocumentHelper.createDocument();  
        //添加modalsheet根节点 
        Element modalsheetEle = document.addElement("ModalSheet");
        ModalSheet ms = modalsheetMapper.getModalsheetById(modalsheetId);
        String filePath = fileDir + File.separator + ms.getCode() + ".xml";
        //表套id，使用接收环境的默认id
        //模板的模板集id,code可以不用导出，接收时使用modaltype.xml中的数据
        //msformatId不需要导出，在接收的系统中查找最大值
        //modalsheetEle.addAttribute("id", ms.getMODALSHEET_ID());
        modalsheetEle.addAttribute("code", ms.getCode());
        modalsheetEle.addAttribute("name", ms.getName());
        modalsheetEle.addAttribute("sortnum", (null == ms.getSORTNUM())? "": ms.getSORTNUM());
        modalsheetEle.addAttribute("type", (null == ms.getTYPE())? "": ms.getTYPE());
        modalsheetEle.addAttribute("fjFlag", (null == ms.getFJ_FLAG())? "": ms.getFJ_FLAG());
        modalsheetEle.addAttribute("fjItem", (null == ms.getFJITEM_NO())? "": ms.getFJITEM_NO());
        modalsheetEle.addAttribute("frCode", (null == ms.getFRAQ_CODE())? "": ms.getFRAQ_CODE());
        modalsheetEle.addAttribute("sortName", (null == ms.getSHORTNAME())? "": ms.getSHORTNAME());
        modalsheetEle.addAttribute("description", (null == ms.getDESCRIPTION())? "": ms.getDESCRIPTION());
        modalsheetEle.addAttribute("cc", (null == ms.getCLASSIFICATION())? "": ms.getCLASSIFICATION());
        modalsheetEle.addAttribute("rowUpcode", (null == ms.getROWUPCODE())? "": ms.getROWUPCODE());
        modalsheetEle.addAttribute("colUpcode", (null == ms.getCOLUPCODE())? "": ms.getCOLUPCODE());
        modalsheetEle.addAttribute("titleMaxRow", (null == ms.getTITLE_MAX_ROW())? "0": ms.getTITLE_MAX_ROW().toString());
        
        String msFormatId = Integer.toString(ms.getId());
        
        //模板格式
        getModalsheetFormat(msFormatId, modalsheetEle);
        //模板元素
        getModalsheetElement(msFormatId, modalsheetEle);
        //行指标
        getRowitem(suitId, modaltypeId, modalsheetId, modalsheetEle);
        //列指标
        getColitem(suitId, modaltypeId, modalsheetId, modalsheetEle);
        //行指标引用
        getRowModalRef(suitId, modaltypeId, modalsheetId, modalsheetEle);
        //列指标引用
        getColModalRef(suitId, modaltypeId, modalsheetId, modalsheetEle);
        //浮动行与列指标映射
        if("Y".equals(ms.getFJ_FLAG())) {
        	getFjColitemSet(suitId, modalsheetId, modalsheetEle);
        }
        //模板属性
        getItemProperty(suitId, modalsheetId, modalsheetEle);
        //采集公式
        if("true".equals(isExpCalFormula)) {
        	getCalFormula(suitId, modalsheetId, modalsheetEle, isOnlyExpRepFormula);
        }
        //稽核公式 
        if("true".equals(isExpChkFormula)) {
        	getCheckFormula(suitId, modalsheetId, modalsheetEle);
        }
        outputXml(filePath, document);
        
        return filePath;
	}
	/**
	 * 模板格式
	 * @param msFormatId
	 * @param modalsheetEle
	 * @throws Exception
	 */
	private void getModalsheetFormat(String msFormatId, Element modalsheetEle) throws Exception {
		Element formatEle = modalsheetEle.addElement("Format");
		List<ModalSheetFormat> formatList = modalsheetMapper.getModalsheetFormatCellList(msFormatId);
		for(ModalSheetFormat format : formatList) {
			Element cellEle = formatEle.addElement("cell");
			//cellEle.addAttribute("id", format.getFormatId());
			cellEle.addAttribute("x", format.getRow().toString());
			cellEle.addAttribute("y", format.getCol().toString());
			cellEle.addAttribute("isCal", (null == format.getCal())? "": format.getCal().toString());
			cellEle.addAttribute("cellCmt", (null == format.getCell_comment())? "": format.getCell_comment());
			cellEle.addAttribute("cellCmttype", (null == format.getCell_comment_type())? "": format.getCell_comment_type());
			cellEle.addText(format.getJson());
		}
	}
	/**
	 * 模板元素
	 * @param msFormatId
	 * @param modalsheetEle
	 * @throws Exception
	 */
	private void getModalsheetElement(String msFormatId, Element modalsheetEle) throws Exception {
		Element eleEle = modalsheetEle.addElement("Element");
		List<ModalSheetElement> elementList = modalsheetMapper.getModalsheetElementList(msFormatId);
		for(ModalSheetElement ele : elementList) {
			Element cellEle = eleEle.addElement("cell");
			//cellEle.addAttribute("id", ele.getElementId());
			cellEle.addAttribute("name", ele.getName());
			cellEle.addAttribute("etype", ele.getFtype());
			cellEle.addText(ele.getJson());
		}
	}
	/**
	 * 行指标
	 */
	private void getRowitem(String suitId, String modaltypeId, String modalsheetId, Element modalsheetEle) throws Exception {
		Element rowitemEle = modalsheetEle.addElement("Rowitem");
		List<Rowitem> rowitemList = modalsheetMapper.getRowitemList(suitId, modaltypeId, modalsheetId);
		for(Rowitem r : rowitemList) {
			/**
			 * rm.ROWITEM_ID, rm.ROWITEM_CODE, rm.ROWITEM_NAME, rm.ISSUM, rm.ISFJITEM, rm.ISICP,
		       rm.ITEMUNIT, rm.ITEMALIAS, rm.UPCODE, rm.DIRECTION, rm.ORDERBY, rm.DESCRIPTION
			 * **/
			Element itemEle = rowitemEle.addElement("item");
			//itemEle.addAttribute("id", r.getROWITEM_ID());
			itemEle.addAttribute("code", r.getROWITEM_CODE());
			itemEle.addAttribute("name", r.getROWITEM_NAME());
			itemEle.addAttribute("isSum", (null == r.getISSUM())? "0": r.getISSUM().toString());
			itemEle.addAttribute("isFjitem", (null == r.getISFJITEM())? "0": r.getISFJITEM().toString());
			itemEle.addAttribute("isIcp", (null == r.getISICP())? "0": r.getISICP().toString());
			itemEle.addAttribute("itemUnit",  (null == r.getITEMUNIT())? "": r.getITEMUNIT());
			itemEle.addAttribute("itemAlias", (null == r.getITEMALIAS())? "": r.getITEMALIAS());
			itemEle.addAttribute("upcode", (null == r.getUPCODE())? "": r.getUPCODE());
			itemEle.addAttribute("direction", (null == r.getDIRECTION())? "": r.getDIRECTION());
			itemEle.addAttribute("orderby", (null == r.getORDERBY())? "": r.getORDERBY());
			itemEle.addAttribute("desc", (null == r.getDESCRIPTION())? "": r.getDESCRIPTION());
		}
	}
	/**
	 * 列指标
	 * @throws Exception
	 */
	private void getColitem(String suitId, String modaltypeId, String modalsheetId, Element modalsheetEle) throws Exception {
		Element colitemEle = modalsheetEle.addElement("Colitem");
		List<Colitem> colitemList = modalsheetMapper.getColitemList(suitId, modaltypeId, modalsheetId);
		for(Colitem c: colitemList) {
			/**
			 * cm.COLITEM_ID, cm.COLITEM_CODE, cm.COLITEM_NAME, cm.ISSUM, cm.ITEMUNIT, 
		       cm.ITEMALIAS, cm.UPCODE, cm.DIRECTION, cm.ORDERBY, cm.DESCRIPTION, cm.DATATYPE
			 * **/
			Element itemEle = colitemEle.addElement("item");
			//itemEle.addAttribute("id", c.getCOLITEM_ID());
			itemEle.addAttribute("code", c.getCOLITEM_CODE() );
			itemEle.addAttribute("name", c.getCOLITEM_NAME());
			itemEle.addAttribute("itemUnit", (null == c.getITEMUNIT())? "": c.getITEMUNIT());
			itemEle.addAttribute("itemAlias", (null == c.getITEMALIAS())? "": c.getITEMALIAS());
			itemEle.addAttribute("upcode", (null == c.getUPCODE())? "": c.getUPCODE());
			itemEle.addAttribute("direction", (null == c.getDIRECTION())? "": c.getDIRECTION());
			itemEle.addAttribute("orderby", (null == c.getORDERBY())? "": c.getORDERBY());
			itemEle.addAttribute("desc", (null == c.getDESCRIPTION())? "": c.getDESCRIPTION());
			itemEle.addAttribute("dataType", (null == c.getDATATYPE())? "3": c.getDATATYPE().toString());
		}
	}
	/**
	 * 模板引用行指标
	 */
	private void getRowModalRef(String suitId, String modaltypeId, String modalsheetId, Element modalsheetEle) throws Exception {
		Element rowRefEle = modalsheetEle.addElement("RowModalRef");
		List<Rowitem> rowitemList = modalsheetMapper.getRowmodalref(suitId, modaltypeId, modalsheetId);
		for(Rowitem r : rowitemList) {
			/**
			 * ROWREF_ID,MODALTYPE_ID,MODALTYPE_CODE,MODALSHEET_ID,MODALSHEET_CODE,
			 * ROWITEM_ID,ROWITEM_CODE,ROWNO,LANNO,REF_FLAG,ROW,COL  
			 * **/
			Element itemEle = rowRefEle.addElement("item");
			/*rowRefEle.addAttribute("rowRefId", r.getROWREF_ID());
			rowRefEle.addAttribute("modaltypeId", r.getMODALTYPE_ID());
			rowRefEle.addAttribute("modaltypeCode", r.getMODALTYPE_CODE());
			rowRefEle.addAttribute("modalsheetId", r.getMODALSHEET_ID());
			rowRefEle.addAttribute("modalsheetCode", r.getMODALSHEET_CODE());
			rowRefEle.addAttribute("rowitemId", r.getROWITEM_ID());*/
			itemEle.addAttribute("rowitemCode", r.getROWITEM_CODE());
			itemEle.addAttribute("rowNo", r.getROWNO().toString());
			itemEle.addAttribute("lanNo", r.getLANNO().toString());
			itemEle.addAttribute("refFlag", r.getREF_FLAG());
			itemEle.addAttribute("row", (null == r.getROW())? "": r.getROW().toString());
			itemEle.addAttribute("col", (null == r.getCOL())? "": r.getCOL().toString());
		}
	}
	/**
	 * 模板引用列指标
	 */
	private void getColModalRef(String suitId, String modaltypeId, String modalsheetId, Element modalsheetEle) throws Exception {
		Element colRefEle = modalsheetEle.addElement("ColModalRef");
		List<Colitem> colitemList = modalsheetMapper.getColmodalref(suitId, modaltypeId, modalsheetId);
		for(Colitem c : colitemList) {
			/**
			 * COLREF_ID,MODALTYPE_ID,MODALTYPE_CODE,MODALSHEET_ID,MODALSHEET_CODE,
		       COLITEM_ID,COLITEM_CODE,COLNO,LANNO,REF_FLAG,DATATYPE 
			 * **/
			Element itemEle = colRefEle.addElement("item");
			/*colRefEle.addAttribute("colRefId", c.getCOLREF_ID());
			colRefEle.addAttribute("modaltypeId", c.getMODALTYPE_ID());
			colRefEle.addAttribute("modaltypeCode", c.getMODALTYPE_CODE());
			colRefEle.addAttribute("modalsheetId", c.getMODALSHEET_ID());
			colRefEle.addAttribute("modalsheetCode", c.getMODALSHEET_CODE());
			colRefEle.addAttribute("colitemId", c.getCOLITEM_ID());*/
			itemEle.addAttribute("colitemCode", c.getCOLITEM_CODE());
			itemEle.addAttribute("colNo", c.getCOLNO().toString());
			itemEle.addAttribute("lanNo", c.getLANNO().toString());
			itemEle.addAttribute("refFlag", c.getREF_FLAG());
			//colRefEle.addAttribute("dataType", c.getDATATYPE().toString());
		}
	}
	/**
	 * 浮动行列与列指标映射
	 */
	private void getFjColitemSet(String suitId, String modalsheetId, Element modalsheetEle) throws Exception {
		Element fjColitemSetEle = modalsheetEle.addElement("FjColitemSet");
		List<Colitem> colitemList = modalsheetMapper.getFjCoitemSetList(suitId, modalsheetId);
		for(Colitem c : colitemList) {
			Element fele = fjColitemSetEle.addElement("item");
			fele.addAttribute("dataCol", c.getDATA_COL());
			fele.addAttribute("colitemCode", c.getCOLITEM_CODE());
			fele.addAttribute("colno", (null == c.getCOLNO())? "": c.getCOLNO().toString());
			fele.addAttribute("row", (null == c.getROW())? "": c.getROW().toString());
			fele.addAttribute("col", (null == c.getCOL())? "": c.getCOL().toString());
		}
	}
	/**
	 * 模板属性
	 * @param suitId
	 * @param modalsheetId
	 * @param modalsheetEle
	 * @throws Exception
	 */
	private void getItemProperty(String suitId, String modalsheetId, Element modalsheetEle) throws Exception {
		Element itemPropertyEle = modalsheetEle.addElement("ItemProperty");
		List<Item_Property> itemList = modalsheetMapper.getItemPropertyList(modalsheetId, suitId);
		//如果返回的list是空的， foreach循环会报错吗？没有报错，模板没有指标属性，生成一个<ItemProperty/>标签
		for(Item_Property item : itemList) {
			Element propertyEle = itemPropertyEle.addElement("item");
			propertyEle.addAttribute("rowitemCode", item.getROWITEM_CODE());
			propertyEle.addAttribute("colitemCode", item.getCOLITEM_CODE());
			propertyEle.addAttribute("proType", item.getPRO_TYPE());
			propertyEle.addAttribute("proValue", item.getPRO_VALUE());
		}
	}
	/**
	 * 采集公式
	 * @param suitId
	 * @param modalsheetId
	 * @param modalsheetEle
	 * @param isOnlyImpRepFormula
	 * @throws Exception
	 */
	private void getCalFormula(String suitId, String modalsheetId,
			Element modalsheetEle, String isOnlyImpRepFormula) throws Exception {
		Element calFormulaEle = modalsheetEle.addElement("CalFormula");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("modalsheetId", modalsheetId);
		params.put("suitId", suitId);
		params.put("isOnlyImpRepFormula", isOnlyImpRepFormula);
		List<CellFormula> calFormulaList = cellFormulaMapper.getCalFormulaList(params);
		
		for(CellFormula formula : calFormulaList) {
			Element formulaEle = calFormulaEle.addElement("formula");
			//rf.ROWITEM_CODE, cf.COLITEM_CODE, t.DATAFORMULA, 
			//t.FORMULA_DESC, t.F_CALTYPE, t.ORDERBY_FLAG
			formulaEle.addAttribute("rowitemCode", formula.getROWITEM_CODE());
			formulaEle.addAttribute("colitemCode", formula.getCOLITEM_CODE());
			formulaEle.addAttribute("dataFormula", formula.getDATAFORMULA());
			formulaEle.addAttribute("formulaDesc", formula.getFORMULA_DESC());
			formulaEle.addAttribute("type", formula.getF_CALTYPE());
			formulaEle.addAttribute("orderBy", Integer.toString(formula.getORDERBY_FLAG()));
		}
	}
	/**
	 * 稽核公式
	 * @param suitId
	 * @param modalsheetId
	 * @param modalsheetEle
	 * @param isOnlyImpRepFormula
	 * @throws Exception
	 */
	private void getCheckFormula(String suitId, String modalsheetId,
			Element modalsheetEle) throws Exception {
		Element chkFormulaEle = modalsheetEle.addElement("ChkFormula");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("modalsheetId", modalsheetId);
		params.put("suitId", suitId);
		List<CheckFormula> checkFormulaList = checkFormulaMapper.getCheckFormulaList(params);
	    //select t.SUIT_ID, t.ROWITEM_ID, rf.ROWITEM_CODE, t.COLITEM_ID, cf.COLITEM_CODE, 
        //t.F_LEFT, t.F_RIGHT, t.F_EXP, t.F_DESC, t.ERR_SCOPE, t.DESCRIPTION, t.F_SETTYPE,
        //t.LOGIC_FLAG, t.LOGIC_NAME, t.ZL_FLAG, t.CHK_TYPE, t.CHK_LEVEL
		for(CheckFormula formula : checkFormulaList) {
			Element formulaEle = chkFormulaEle.addElement("formula");
			formulaEle.addAttribute("rowitemCode", formula.getROWITEM_CODE());
			formulaEle.addAttribute("colitemCode", formula.getCOLITEM_CODE());
			formulaEle.addAttribute("fleft", formula.getF_LEFT());
			formulaEle.addAttribute("fright", formula.getF_RIGHT());
			formulaEle.addAttribute("fexp", formula.getF_EXP());
			formulaEle.addAttribute("fdesc", (null == formula.getF_DESC())? "": formula.getF_DESC());
			formulaEle.addAttribute("errScope", (null == formula.getERR_SCOPE())? "": formula.getERR_SCOPE());
			formulaEle.addAttribute("desc", (null == formula.getDESCRIPTION())? "": formula.getDESCRIPTION());
			formulaEle.addAttribute("fSetType", (null == formula.getF_SETTYPE())? "": formula.getF_SETTYPE());
			formulaEle.addAttribute("logicFlag", (null == formula.getLOGIC_FLAG())? "": formula.getLOGIC_FLAG());
			formulaEle.addAttribute("logicName", (null == formula.getLOGIC_NAME())? "": formula.getLOGIC_NAME());
			formulaEle.addAttribute("zlFlag", (null == formula.getZL_FLAG())? "": formula.getZL_FLAG());
			formulaEle.addAttribute("chkType", (null == formula.getCHK_TYPE())? "": formula.getCHK_TYPE());
			formulaEle.addAttribute("chkLevel", (null == formula.getCHK_LEVEL())? "": formula.getCHK_LEVEL());
		}
	}
	/**
	 * 输出xml数据到文件
	 * @param filepath
	 * @param document
	 * @throws Exception
	 */
	private void outputXml(String filepath, Document document) throws Exception{
		//输出格式
		OutputFormat format = OutputFormat.createPrettyPrint();
        Writer fileWriter = new FileWriter(filepath);  
        //dom4j提供了专门写入文件的对象XMLWriter  
        XMLWriter xmlWriter = new XMLWriter(fileWriter, format);  
        xmlWriter.write(document);  
        xmlWriter.flush();  
        xmlWriter.close(); 
	}
	@Override
	public String getImpModalsheetList(String filePath) throws Exception {
		StringBuilder sheetStr = new StringBuilder();
		sheetStr.append("[");
		File modaltypeFile = new File(filePath);
		//将src下面的xml转换为输入流  
        //InputStream inputStream = new FileInputStream(modaltypeFile);   
        //创建SAXReader读取器，专门用于读取xml  
        SAXReader saxReader = new SAXReader();  
        //根据saxReader的read重写方法可知，既可以通过inputStream输入流来读取，也可以通过file对象来读取   
        //Document document = saxReader.read(inputStream);    
        Document document = saxReader.read(modaltypeFile);//必须指定文件的绝对路径  
          
        //获取根节点对象  
        Element rootElement = document.getRootElement();   
        String modaltypeCode = rootElement.attributeValue("code");
        String modaltypeName = rootElement.attributeValue("name");
          
        //获取子节点  
        //rootElement.element("name");获取某一个子元素  
        //rootElement.elements("name");获取根节点下子元素moudule节点的集合，返回List集合类型  
        List<Element> sheetIterator = rootElement.elements("ModalSheet");  
        for(Element modalsheet : sheetIterator) {
        	String[] sheet = modalsheet.getText().split("&");
        	if("[".equals(sheetStr.toString())) {
        		sheetStr.append("{modaltypeCode:'").append(modaltypeCode)
        		        .append("',modaltypeName:'").append(modaltypeName)
        		        .append("',modalsheetCode:'").append(sheet[0])
        		        .append("',modalsheetName:'").append(sheet[1]).append("'}");
        	} else {
        		sheetStr.append(",{modaltypeCode:'").append(modaltypeCode)
				        .append("',modaltypeName:'").append(modaltypeName)
				        .append("',modalsheetCode:'").append(sheet[0])
				        .append("',modalsheetName:'").append(sheet[1]).append("'}");
        	}
        }
        sheetStr.append("]");
		return sheetStr.toString();
	}
	/**
	 * 接收选中的模板
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void receiveModal(String[] params) throws Exception {
	//params:suitId;userId;fileDir;modalsheetCodeList;isImpCalFormula;isOnlyImpRepFormula;isImpChkFormula;isDeleteFormual;
		String suitId = params[0];
		String userId = params[1];
		String fileDir = params[2];
		String modalsheetCodeList = params[3];
		/**
		 * 1. 接收模板集
		 */
		String modaltypefilePath = fileDir + File.separator + "modaltype.xml";
		String[] modaltypeInfo = receiveModaltype(modaltypefilePath, suitId, userId);
		String modaltypeId = modaltypeInfo[0];
		String modaltypeCode = modaltypeInfo[1];
        /**
         * 2. 循环模板编码，接收模板
         * 通过模板编码，接收模板，判断模板是否存在
         */
		String[] sheetParams = new String[9];
		sheetParams[0] = suitId;
		sheetParams[1] = userId;
		sheetParams[2] = modaltypeId;
		sheetParams[3] = modaltypeCode;
		sheetParams[4] = params[4];
		sheetParams[5] = params[5];
		sheetParams[6] = params[6];
		sheetParams[8] = params[7];
 		String[] modalsheetList = modalsheetCodeList.split(",");
		for(String modalsheetCode : modalsheetList) {
			String modalsheetfilePath = fileDir + File.separator + modalsheetCode + ".xml";
			sheetParams[7] = modalsheetfilePath;
			receiveModalsheet(sheetParams);
		}
	}
	/**
	 * 解析modaltype.xml文件，接收模板集
	 */
	private String[] receiveModaltype(String modaltypefilePath, String suitId, String userId) throws Exception {
		String[] modaltypeInfo = new String[2];
		Timestamp time = new Timestamp(new Date().getTime());
		File modaltypeFile = new File(modaltypefilePath);
		if(!modaltypeFile.exists()) {
			throw new RuntimeException(modaltypefilePath + "模板集文件不存在！");
		}
        //创建SAXReader读取器，专门用于读取xml  
        SAXReader saxReader = new SAXReader();  
        Document document = saxReader.read(modaltypeFile);//必须指定文件的绝对路径  
          
        //获取根节点对象  
        Element rootElement = document.getRootElement(); 
        String modaltypeCode = rootElement.attributeValue("code");
        
        ModalType modaltype = new ModalType();
        modaltype.setSUIT_ID(suitId);
        modaltype.setMODALTYPE_CODE(modaltypeCode);
        modaltype.setMODALTYPE_NAME(rootElement.attributeValue("name"));
        modaltype.setPERIOD_TYPE(rootElement.attributeValue("periodType"));
        modaltype.setLAST_UPDATE_DATE(time);
        modaltype.setLAST_UPDATED_BY(userId);
        /**
         * 通过表套ID，模板集编码判断模板集是否存在
         */
        String modaltypeId = modalsheetMapper.isModaLTypeExist(modaltypeCode, suitId);
        if(null != modaltypeId) {
        	/**
        	 * 模板集存在，则更新模板集
        	 */
        	modalsheetMapper.updateModaltype(modaltype);
        } else {
        	/**
        	 * 模板集不存在，则新增模板集。生成模板集ID
        	 */
        	modaltypeId = UUID.randomUUID().toString();
            modaltype.setMODALTYPE_ID(modaltypeId);
            modaltype.setCREATION_DATE(time);
            modaltype.setCREATED_BY(userId);
            modalsheetMapper.insertModaltype(modaltype);
        }
        modaltypeInfo[0] = modaltypeId;
        modaltypeInfo[1] = modaltypeCode;
        return modaltypeInfo;
	}
	/**
	 * 接收模板
	 * @param modalsheetfilePath
	 * @param modaltypeId
	 * @param suitId
	 * @param userId
	 * @throws Exception
	 */
	private void receiveModalsheet(String[] sheetParams) throws Exception {
		String suitId = sheetParams[0];
		String userId = sheetParams[1];
		String modaltypeId = sheetParams[2];
		String modaltypeCode = sheetParams[3];
		String isImpCalFormula = sheetParams[4];
		String isOnlyImpRepFormula = sheetParams[5];
		String isImpChkFormula = sheetParams[6];
		String modalsheetfilePath = sheetParams[7];
		String isDeleteFormula = sheetParams[8];
		
		Timestamp time = new Timestamp(new Date().getTime());
		File modalsheetFile = new File(modalsheetfilePath);
		if(!modalsheetFile.exists()) {
			throw new RuntimeException(modalsheetfilePath + "模板文件不存在！");
		}
		// 创建SAXReader读取器，专门用于读取xml
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(modalsheetfilePath);// 必须指定文件的绝对路径
		// 获取根节点对象
		Element modalsheetElement = document.getRootElement();
		ModalSheet modalsheet = new ModalSheet();
		String modalsheetCode = modalsheetElement.attributeValue("code");
		modalsheet.setSUIT_ID(suitId);
		modalsheet.setMODALTYPE_ID(modaltypeId);
		modalsheet.setMODALTYPE_CODE(modaltypeCode);
		modalsheet.setCode(modalsheetCode);
		modalsheet.setName(modalsheetElement.attributeValue("name"));
		modalsheet.setSORTNUM(modalsheetElement.attributeValue("sortnum"));
		modalsheet.setTYPE(modalsheetElement.attributeValue("type"));
		modalsheet.setFJ_FLAG(modalsheetElement.attributeValue("fjFlag"));
		modalsheet.setFJITEM_NO(modalsheetElement.attributeValue("fjItem"));
		modalsheet.setFRAQ_CODE(modalsheetElement.attributeValue("frCode"));
		modalsheet.setSHORTNAME(modalsheetElement.attributeValue("sortName"));
		modalsheet.setDESCRIPTION(modalsheetElement.attributeValue("description"));
		modalsheet.setCLASSIFICATION(modalsheetElement.attributeValue("cc"));
		modalsheet.setROWUPCODE(modalsheetElement.attributeValue("rowUpcode"));
		modalsheet.setCOLUPCODE(modalsheetElement.attributeValue("colUpcode"));
		modalsheet.setTITLE_MAX_ROW(Integer.parseInt(modalsheetElement.attributeValue("titleMaxRow")));
		modalsheet.setLAST_UPDATE_DATE(time);
		modalsheet.setLAST_UPDATED_BY(userId);
		String modalsheetId = "";
		String modalFormatId = "";
		/**
		 * 1. 接收模板
		 */
		/**
		 * 通过模板集Id, suitId, 模板code判断模板是否存在
		 */
		Element formatElement = modalsheetElement.element("Format");
		Element eleElement = modalsheetElement.element("Element");
		ModalSheet modalsheetInfo = modalsheetMapper.isModaLSheetExist(modalsheetCode, modaltypeId, suitId);
		if(null != modalsheetInfo) {
			/**
			 * 模板存在，更新模板
			 */
			modalsheetId = modalsheetInfo.getMODALSHEET_ID();
			modalFormatId = Integer.toString(modalsheetInfo.getId());
			modalsheetMapper.updateModalsheet(modalsheet);
			/**
			 * 模板存在，则先删除模板格式和模板元素，再接收
			 */
			//删除格式
			modalsheetMapper.deleleModalsheetFormat(modalFormatId);
			modalsheetMapper.deleleModalsheetElement(modalFormatId);
			//接收格式
			receiveModalFormat(formatElement, modalFormatId, userId);
			//接收元素
			receiveModalElement(eleElement, modalFormatId, userId);
		} else {
			/**
			 * 模板不存在，新增模板， 新增模板需要生成模板ID，格式ID
			 */
			modalsheetId = UUID.randomUUID().toString();
			modalFormatId = modalsheetMapper.getMaxModaLFormat().toString();
			modalsheet.setMODALSHEET_ID(modalsheetId);
			modalsheet.setId(Integer.parseInt(modalFormatId));
			modalsheet.setCREATION_DATE(time);
			modalsheet.setCREATED_BY(userId);
			modalsheetMapper.insertModalsheet(modalsheet);
			/**
			 * 模板不存在，直接接收模板格式和模板元素
			 */
			//接收格式
			receiveModalFormat(formatElement, modalFormatId, userId);
			//接收元素
			receiveModalElement(eleElement, modalFormatId, userId);
		}
		/**
		 * 2. 接收行指标
		 */
		Element rowitemElement = modalsheetElement.element("Rowitem");
		HashMap<String, String> rowitemMap = receiveRowitem(rowitemElement, suitId, userId);
		/**
		 * 3. 接收列指标
		 */
		Element colitemElement = modalsheetElement.element("Colitem");
		HashMap<String, String> colitemMap = receiveColitem(colitemElement, suitId, userId);
		/**
		 * 4. 接收行指标引用
		 */
		Element rowRefElement = modalsheetElement.element("RowModalRef");
		String[] params = new String[6];
		params[0] = modaltypeId;
		params[1] = modaltypeCode;
		params[2] = modalsheetId;
		params[3] = modalsheetCode;
		params[4] = suitId;
		params[5] = userId;
		receiveRowRef(rowRefElement, params, rowitemMap);
		/**
		 * 5. 接收列指标引用
		 */
		Element colRefElement = modalsheetElement.element("ColModalRef");
		receiveColRef(colRefElement, params, colitemMap);
		/**
		 * 6. 接收浮动行列与列指标映射
		 */
		Element fjColitemSet = modalsheetElement.element("FjColitemSet");
		if(null != fjColitemSet) {
			receiveFjColitemSet(fjColitemSet, params, colitemMap);
		}
		/**
		 * 7. 接收指标属性
		 */
		Element itemPropertyEle = modalsheetElement.element("ItemProperty");
		//itemPropertyEle元素不会是空，会是单独一个<ItemProperty/>标签
		receiveItemProperty(itemPropertyEle, params, rowitemMap, colitemMap);
		/**
		 * 8. 接收采集公式
		 */
		if("true".equals(isImpCalFormula)) {
			Element calFormulaEle = modalsheetElement.element("CalFormula");
			receiveCalFormula(calFormulaEle, params, rowitemMap, colitemMap, isOnlyImpRepFormula);
		}
		/**
		 * 9. 接收稽核公式
		 */
		if("true".equals(isImpChkFormula)) {
			Element chkFormulaEle = modalsheetElement.element("ChkFormula");
			if("true".equals(isDeleteFormula)) {
				receiveChkFormulaDeleteAndInsert(chkFormulaEle, params, rowitemMap, colitemMap);
			} else {
				receiveChkFormulaUpdateAndInsert(chkFormulaEle, params, rowitemMap, colitemMap);
			}
		}
	}
	/**
	 * 接收模板格式
	 * @param formatElement
	 * @param modalFormatId
	 * @param userId
	 * @throws Exception
	 */
	private void receiveModalFormat(Element formatElement, String modalFormatId, String userId) throws Exception {
		Timestamp time = new Timestamp(new Date().getTime());
		List<ModalSheetFormat> formatList = new ArrayList<ModalSheetFormat>();
        List<Element> cells = formatElement.elements("cell"); 
        for(Element cell : cells) {
        	ModalSheetFormat mf = new ModalSheetFormat();
        	mf.setFormatId(UUID.randomUUID().toString());
        	//mf.setSheet(modalFormatId);
        	mf.setSheet(Integer.parseInt(modalFormatId));
        	mf.setRow(Integer.parseInt(cell.attributeValue("x")));
        	mf.setCol(Integer.parseInt(cell.attributeValue("y")));
        	mf.setCal(Boolean.parseBoolean(cell.attributeValue("isCal")));
        	mf.setCell_comment(cell.attributeValue("cellCmt"));
        	mf.setCell_comment_type(cell.attributeValue("cellCmttype"));
        	mf.setJson(cell.getText());
        	mf.setCreationDate(time);
        	mf.setCreatedBy(userId);
        	formatList.add(mf);
        }
        if(formatList.size() > 0) {
        	modalsheetMapper.insertBatchFormatCell(formatList);
        }
	}
	/**
	 * 接收模板元素
	 * @param eleElement
	 * @param modalFormatId
	 * @param userId
	 * @throws Exception
	 */
	private void receiveModalElement(Element eleElement, String modalFormatId, String userId) throws Exception {
		Timestamp time = new Timestamp(new Date().getTime());
		List<ModalSheetElement> elementList = new ArrayList<ModalSheetElement>();
        List<Element> cells = eleElement.elements("cell"); 
        for(Element cell : cells) {
        	ModalSheetElement me = new ModalSheetElement();
        	me.setElementId(UUID.randomUUID().toString());
        	//me.setSheet(modalFormatId);
        	me.setSheet(Integer.parseInt(modalFormatId));
        	me.setName(cell.attributeValue("name"));
        	me.setFtype(cell.attributeValue("etype"));
        	me.setJson(cell.getText());
        	me.setCreationDate(time);
        	me.setCreatedBy(userId);
        	elementList.add(me);
        }
        if(elementList.size() > 0) {
        	modalsheetMapper.insertBatchModalSheetElement(elementList);
        }
	}
	/**
	 * 接收行指标
	 * @param rowitemElement
	 * @param suitId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	private HashMap<String, String> receiveRowitem(Element rowitemElement, String suitId, String userId) throws Exception {
		Timestamp time = new Timestamp(new Date().getTime());
		HashMap<String, String> rowitemMap = new HashMap<String, String>();
		List<Rowitem> updateRowitemList = new ArrayList<> ();
		List<Rowitem> insertRowitemList = new ArrayList<>();
		List<Element> rowitems = rowitemElement.elements();
		for(Element rowitem : rowitems) {
			Rowitem row = new Rowitem();
			String rowitemCode = rowitem.attributeValue("code");
			row.setSUIT_ID(suitId);
			row.setROWITEM_CODE(rowitemCode);
			row.setROWITEM_NAME(rowitem.attributeValue("name"));
			row.setISSUM(Boolean.parseBoolean(rowitem.attributeValue("isSum")));
			row.setISFJITEM(Boolean.parseBoolean(rowitem.attributeValue("isFjitem")));
			row.setISICP(Boolean.parseBoolean(rowitem.attributeValue("isIcp")));
			row.setITEMUNIT(rowitem.attributeValue("itemUnit"));
			row.setITEMALIAS(rowitem.attributeValue("itemAlias"));
			row.setUPCODE(rowitem.attributeValue("upcode"));
			row.setDIRECTION(rowitem.attributeValue("direction"));
			row.setORDERBY(rowitem.attributeValue("orderby"));
			row.setDESCRIPTION(rowitem.attributeValue("desc"));
			row.setLAST_UPDATE_DATE(time);
			row.setLAST_UPDATED_BY(userId);
			/**
			 * 用表套id,行指标code 判断行指标是否存在
			 */
			String rowitemId = modalsheetMapper.isRowitemCodeExist(rowitemCode, suitId);
			if(null != rowitemId) {
				/**
				 * 行指标已经存在，则更新，插入到updateRowitemList中
				 */
				row.setROWITEM_ID(rowitemId);
				updateRowitemList.add(row);
			} else {
				/**
				 * 行指标不存在，则新增，插入到insertRowitemList中
				 */
				rowitemId = UUID.randomUUID().toString();
				row.setROWITEM_ID(rowitemId);
				row.setCREATION_DATE(time);
				row.setCREATED_BY(userId);
				insertRowitemList.add(row);
			}
			rowitemMap.put(rowitemCode, rowitemId);
		}
		//批量更新行指标
		if(updateRowitemList.size() > 0) {
			modalsheetMapper.updateBatchRowitemList(updateRowitemList);
		}
		//批量插入行指标
		if(insertRowitemList.size() > 0) {
			modalsheetMapper.insertBatchRowitemList(insertRowitemList);
		}
		
		return rowitemMap;
	}
	/**
	 * 接收列指标
	 * @param rowitemElement
	 * @param suitId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	private HashMap<String, String> receiveColitem(Element colitemElement, String suitId, String userId) throws Exception {
		Timestamp time = new Timestamp(new Date().getTime());
		HashMap<String, String> colitemMap = new HashMap<String, String>();
		List<Colitem> updateColitemList = new ArrayList<> ();
		List<Colitem> insertColitemList = new ArrayList<>();
		List<Element> colitems = colitemElement.elements();
		for(Element colitem : colitems) {
			Colitem col = new Colitem();
			String colitemCode = colitem.attributeValue("code");
			col.setSUIT_ID(suitId);
			col.setCOLITEM_CODE(colitemCode);
			col.setCOLITEM_NAME(colitem.attributeValue("name"));
			col.setITEMUNIT(colitem.attributeValue("itemUnit"));
			col.setITEMALIAS(colitem.attributeValue("itemAlias"));
			col.setUPCODE(colitem.attributeValue("upcode"));
			col.setDIRECTION(colitem.attributeValue("direction"));
			col.setORDERBY(colitem.attributeValue("orderby"));
			col.setDESCRIPTION(colitem.attributeValue("desc"));
			col.setDATATYPE(Integer.parseInt(colitem.attributeValue("dataType")));
			col.setLAST_UPDATE_DATE(time);
			col.setLAST_UPDATED_BY(userId);
			/**
			 * 通过表套id,列指标编码判断列指标是否存在
			 */
			String colitemId = modalsheetMapper.isColitemCodeExist(colitemCode, suitId);
			if(null != colitemId) {
				/**
				 * 列指标已经存在，则更新
				 */
				col.setCOLITEM_ID(colitemId);
				updateColitemList.add(col);
			} else {
				/**
				 * 列指标不存在，则新增
				 */
				colitemId = UUID.randomUUID().toString();
				col.setCOLITEM_ID(colitemId);
				col.setCREATION_DATE(time);
				col.setCREATED_BY(userId);
				insertColitemList.add(col);
			}
			colitemMap.put(colitemCode, colitemId);
		}
		//批量更新列指标
		if(updateColitemList.size() > 0) {
			modalsheetMapper.updateBatchColitemList(updateColitemList);
		}
		//批量插入列指标
		if(insertColitemList.size() > 0) {
			modalsheetMapper.insertBatchColitemList(insertColitemList);
		}
		
		return colitemMap;
	}
	/**
	 * 接收行指标引用
	 * @param rowRefElement
	 * @param params
	 * @param rowitemMap
	 * @throws Exception
	 */
	private void receiveRowRef(Element rowRefElement, String[] params, HashMap<String, String> rowitemMap) throws Exception {
		Timestamp time = new Timestamp(new Date().getTime());
		String modaltypeId = params[0];
		String modaltypeCode = params[1];
		String modalsheetId = params[2];
		String modalsheetCode = params[3];
		String suitId = params[4];
		String  userId = params[5];
		List<Rowitem> rowList = new ArrayList<Rowitem>();
		List<Element> rowRefList = rowRefElement.elements();
		for(Element e : rowRefList) {
			Rowitem r = new Rowitem();
			String rowitemCode = e.attributeValue("rowitemCode");
			r.setROWREF_ID(UUID.randomUUID().toString());
			r.setSUIT_ID(suitId);
			r.setMODALTYPE_ID(modaltypeId);
			r.setMODALTYPE_CODE(modaltypeCode);
			r.setMODALSHEET_ID(modalsheetId);
			r.setMODALSHEET_CODE(modalsheetCode);
			r.setROWITEM_ID(rowitemMap.get(rowitemCode));
			r.setROWITEM_CODE(rowitemCode);
			r.setROWNO(Integer.parseInt(e.attributeValue("rowNo")));
			r.setLANNO(Integer.parseInt(e.attributeValue("lanNo")));
			r.setREF_FLAG(e.attributeValue("refFlag"));
			r.setROW(("".equals(e.attributeValue("row")))? null: Integer.parseInt(e.attributeValue("row")));
			r.setCOL(("".equals(e.attributeValue("col")))? null: Integer.parseInt(e.attributeValue("col")));
			r.setCREATED_BY(userId);
			r.setCREATION_DATE(time);
			r.setLAST_UPDATED_BY(userId);
			r.setLAST_UPDATE_DATE(time);
			rowList.add(r);
		}
		
		//首先删除掉已有的行指标引用
		modalsheetMapper.deleteRowModalRefByMID(modalsheetId);
		//插入行指标引用
		modalsheetMapper.insertBthRowitemRef(rowList);
	}
	/**
	 * 接收列指标引用
	 * @param colRefElement
	 * @param params
	 * @param colitemMap
	 * @throws Exception
	 */
	private void receiveColRef(Element colRefElement, String[] params, HashMap<String, String> colitemMap) throws Exception {
		Timestamp time = new Timestamp(new Date().getTime());
		String modaltypeId = params[0];
		String modaltypeCode = params[1];
		String modalsheetId = params[2];
		String modalsheetCode = params[3];
		String suitId = params[4];
		String  userId = params[5];
		List<Colitem> colList = new ArrayList<Colitem>();
		List<Element> colRefList = colRefElement.elements();
		for(Element e : colRefList) {
			Colitem c = new Colitem();
			String colitemCode = e.attributeValue("colitemCode");
			c.setCOLREF_ID(UUID.randomUUID().toString());
			c.setSUIT_ID(suitId);
			c.setMODALTYPE_ID(modaltypeId);
			c.setMODALTYPE_CODE(modaltypeCode);
			c.setMODALSHEET_ID(modalsheetId);
			c.setMODALSHEET_CODE(modalsheetCode);
			c.setCOLITEM_ID(colitemMap.get(colitemCode));
			c.setCOLITEM_CODE(colitemCode);
			c.setCOLNO(Integer.parseInt(e.attributeValue("colNo")));
			c.setLANNO(Integer.parseInt(e.attributeValue("lanNo")));
			c.setREF_FLAG(e.attributeValue("refFlag"));
			c.setCREATED_BY(userId);
			c.setCREATION_DATE(time);
			c.setLAST_UPDATED_BY(userId);
			c.setLAST_UPDATE_DATE(time);
			colList.add(c);
		}
		
		//首先删除掉已有的行指标引用
		modalsheetMapper.deleteColModalRef(modalsheetId);
		//插入行指标引用
		modalsheetMapper.insertBthColitemRef(colList);
	}
	/**
	 * 接收浮动行列与列指标映射关系
	 * @param fjColitemSet
	 * @param params
	 * @param colitemMap
	 * @throws Exception
	 */
	private void receiveFjColitemSet(Element fjColitemSet, String[] params, HashMap<String, String> colitemMap) throws Exception {
		Timestamp time = new Timestamp(new Date().getTime());
		String modalsheetId = params[2];
		String suitId = params[4];
		String  userId = params[5];
		List<Colitem> colList = new ArrayList<Colitem>();
		List<Element> fjColEle = fjColitemSet.elements();
		for(Element e : fjColEle) {
			Colitem c = new Colitem();
			String colitemCode = e.attributeValue("colitemCode");
			c.setFJITEM_SET_ID(UUID.randomUUID().toString());
			c.setSUIT_ID(suitId);
			c.setMODALSHEET_ID(modalsheetId);
			c.setCOLITEM_ID(colitemMap.get(colitemCode));
			c.setCOLITEM_CODE(colitemCode);
			c.setDATA_COL(e.attributeValue("dataCol"));
			c.setCOLNO(("".equals(e.attributeValue("colno")))? null: Integer.parseInt(e.attributeValue("colno")));
			c.setROW(("".equals(e.attributeValue("row")))? null: Integer.parseInt(e.attributeValue("row")));
			c.setCOL(("".equals(e.attributeValue("col")))? null: Integer.parseInt(e.attributeValue("col")));
			c.setCREATED_BY(userId);
			c.setCREATION_DATE(time);
			c.setLAST_UPDATED_BY(userId);
			c.setLAST_UPDATE_DATE(time);
			colList.add(c);
		}
		if(colList.size() > 0) {
			//首先删除掉已有的列指标映射
			modalsheetMapper.deleteDataColSet(modalsheetId);
			//插入列指标映射
			modalsheetMapper.insertBthDataColSet(colList);
		}
	}
	/**
	 * 接收指标属性
	 * @param itemPropertyEle
	 * @param params
	 * @param rowitemMap
	 * @param colitemMap
	 * @throws Exception
	 */
	private void receiveItemProperty(Element itemPropertyEle, String[] params,
			HashMap<String, String> rowitemMap,
			HashMap<String, String> colitemMap) throws Exception {
		Timestamp time = new Timestamp(new Date().getTime());
		String modaltypeId = params[0];
		String modalsheetId = params[2];
		String suitId = params[4];
		String  userId = params[5];
		List<Item_Property> proList = new ArrayList<Item_Property>();
		List<Element> itemEle = itemPropertyEle.elements();
		for(Element e : itemEle) {
			Item_Property item = new Item_Property();
			item.setITEM_PRO_ID(UUID.randomUUID().toString());
			item.setSUIT_ID(suitId);
			item.setMODALTYPE_ID(modaltypeId);
			item.setMODALSHEET_ID(modalsheetId);
			item.setROWITEM_ID(rowitemMap.get(e.attributeValue("rowitemCode")));
			item.setCOLITEM_ID(colitemMap.get(e.attributeValue("colitemCode")));
			item.setPRO_TYPE(e.attributeValue("proType"));
			item.setPRO_VALUE(e.attributeValue("proValue"));
			item.setCREATED_BY(userId);
			item.setCREATION_DATE(time);
			item.setLAST_UPDATED_BY(userId);
			item.setLAST_UPDATE_DATE(time);
			proList.add(item);
		}
		
		if(proList.size() > 0) {
			//首先删除掉已有的指标属性
			modalsheetMapper.deleleItemPropertyAlltype(modalsheetId, suitId);
			//插入指标属性
			modalsheetMapper.insertBthItemProperty(proList);
		}
	}
	/**
	 * 接收采集公式
	 * @param calFormulaEle
	 * @param params
	 * @param rowitemMap
	 * @param colitemMap
	 * @param isOnlyImpRepFormula
	 * @throws Exception
	 */
	private void receiveCalFormula(Element calFormulaEle, String[] params,
			HashMap<String, String> rowitemMap,
			HashMap<String, String> colitemMap,
			String isOnlyImpRepFormula) throws Exception {
		Timestamp time = new Timestamp(new Date().getTime());
		String modaltypeId = params[0];
		String modaltypeCode = params[1];
		String modalsheetId = params[2];
		String modalsheetCode = params[3];
		String suitId = params[4];
		String  userId = params[5];
		List<CellFormula> insertFormulaList = new ArrayList<CellFormula>();
		List<CellFormula> updateFormulaList = new ArrayList<CellFormula>();
		List<Element> formulaEle = calFormulaEle.elements();
		String rowitemId = "";
		String colitemId = "";
		String entityId = "-1";
		String formulaId = "";
		for(Element e : formulaEle) {
			String fType = e.attributeValue("type");
			CellFormula formula = new CellFormula();
			formula.setSUIT_ID(suitId);
			rowitemId = rowitemMap.get(e.attributeValue("rowitemCode"));
			colitemId = colitemMap.get(e.attributeValue("colitemCode"));
			formula.setROWITEM_ID(rowitemId);
			formula.setCOLITEM_ID(colitemId);
			formula.setENTITY_ID(entityId); //只接收公共级公式
			formula.setDATAFORMULA(e.attributeValue("dataFormula"));
			formula.setFORMULA_DESC(e.attributeValue("formulaDesc"));
			formula.setF_CALTYPE(fType);
			formula.setORDERBY_FLAG(Integer.parseInt(e.attributeValue("orderBy")));
			formula.setLAST_UPDATED_BY(userId);
			formula.setLAST_UPDATE_DATE(time);
			/**
			 * 采集公式通过表套id,行指标id,列指标id,公司id='-1'判断公式是否存在
			 */
			if("true".equals(isOnlyImpRepFormula)) {
				if("REP".equals(fType)) {
					formulaId = cellFormulaMapper.getCalFormulaId(suitId, rowitemId, colitemId, entityId);
//System.out.println("采集公式id: " + formulaId);
					if(!"".equals(formulaId) && null != formulaId) {
						formula.setFORMULA_ID(formulaId);
						updateFormulaList.add(formula);
					} else {
						formula.setFORMULA_ID(UUID.randomUUID().toString());
						formula.setCREATED_BY(userId);
						formula.setCREATION_DATE(time);
						insertFormulaList.add(formula);
					}
				} else {
					continue;
				}
			} else {
				formulaId = cellFormulaMapper.getCalFormulaId(suitId, rowitemId, colitemId, entityId);
//System.out.println("采集公式id: " + formulaId);
				if(!"".equals(formulaId) && null != formulaId) {
					formula.setFORMULA_ID(formulaId);
					updateFormulaList.add(formula);
				} else {
					formula.setFORMULA_ID(UUID.randomUUID().toString());
					formula.setCREATED_BY(userId);
					formula.setCREATION_DATE(time);
					insertFormulaList.add(formula);
				}
			}
		}
		//批量更新采集公式
		if(updateFormulaList.size() > 0) {
			cellFormulaMapper.updateBatchCalFormula(updateFormulaList);
		}
		//批量插入采集公式
		if(insertFormulaList.size() > 0) {
			cellFormulaMapper.insertBatchCellFormat(insertFormulaList);
		}
	}
	/**
	 * 接收稽核公式--先删除公式再插入公式
	 * @param chkFormulaEle
	 * @param params
	 * @param rowitemMap
	 * @param colitemMap
	 * @throws Exception
	 */
	private void receiveChkFormulaDeleteAndInsert(Element chkFormulaEle, String[] params,
			HashMap<String, String> rowitemMap,
			HashMap<String, String> colitemMap) throws Exception {
		Timestamp time = new Timestamp(new Date().getTime());
		String modaltypeId = params[0];
		String modaltypeCode = params[1];
		String modalsheetId = params[2];
		String modalsheetCode = params[3];
		String suitId = params[4];
		String  userId = params[5];
		List<CheckFormula> formulaList = new ArrayList<CheckFormula>();
		List<Element> formulaEle = chkFormulaEle.elements();
		for(Element e : formulaEle) {
			CheckFormula formula = new CheckFormula();
			formula.setCHK_FORMULA_ID(UUID.randomUUID().toString());
			formula.setSUIT_ID(suitId);
			formula.setROWITEM_ID(("<$$>".equals(e.attributeValue("rowitemCode")))? "-1" : rowitemMap.get(e.attributeValue("rowitemCode")));
			formula.setCOLITEM_ID(("<$$>".equals(e.attributeValue("colitemCode")))? "-1" : colitemMap.get(e.attributeValue("colitemCode")));
			formula.setF_LEFT(e.attributeValue("fleft"));
			formula.setF_RIGHT(e.attributeValue("fright"));
			formula.setF_EXP(e.attributeValue("fexp"));
			formula.setF_DESC(e.attributeValue("fdesc"));
			formula.setERR_SCOPE(e.attributeValue("errScope"));
			formula.setDESCRIPTION(e.attributeValue("desc"));
			formula.setF_SETTYPE(e.attributeValue("fSetType"));
			formula.setLOGIC_FLAG(e.attributeValue("logicFlag"));
			formula.setLOGIC_NAME(e.attributeValue("logicName"));
			formula.setZL_FLAG(e.attributeValue("zlFlag"));
			formula.setCHK_TYPE(e.attributeValue("chkType"));
			formula.setCHK_LEVEL(e.attributeValue("chkLevel"));
			formula.setCREATED_BY(userId);
			formula.setCREATION_DATE(time);
			formula.setLAST_UPDATED_BY(userId);
			formula.setLAST_UPDATE_DATE(time);

			formulaList.add(formula);
		}
		//如果接收的文件上没有稽核公式，则以前的公式不删除
		if(formulaList.size() > 0) {
			HashMap<String, String> param = new HashMap<String, String>();
			param.put("modalsheetId", modalsheetId);
			param.put("suitId", suitId);
			/**
			 * 删除稽核公式
			 * 1. 首先删除稽核方案明细表中的公式
			 * 2. 删除稽核公式表
			 */
			checkFormulaMapper.deleteChkSchemeDtl(param);
			checkFormulaMapper.deleteCheckFormula(param);
			//插入指标属性
			checkFormulaMapper.insertBatchCheckFormat(formulaList);
		}
	}
	
	/**
	 * 接收稽核公式--不会删除公式，只是更新和插入公式
	 * @param chkFormulaEle
	 * @param params
	 * @param rowitemMap
	 * @param colitemMap
	 * @throws Exception
	 */
	private void receiveChkFormulaUpdateAndInsert(Element chkFormulaEle, String[] params,
			HashMap<String, String> rowitemMap,
			HashMap<String, String> colitemMap) throws Exception {
		Timestamp time = new Timestamp(new Date().getTime());
		String modaltypeId = params[0];
		String modaltypeCode = params[1];
		String modalsheetId = params[2];
		String modalsheetCode = params[3];
		String suitId = params[4];
		String  userId = params[5];
		List<CheckFormula> insertFormulaList = new ArrayList<CheckFormula>();
		List<CheckFormula> updateFormulaList = new ArrayList<CheckFormula>();
		String formulaId = "";
		String rowitemId = "";
		String colitemId = "";
		String fRight = "";
		List<Element> formulaEle = chkFormulaEle.elements();
		for(Element e : formulaEle) {
			CheckFormula formula = new CheckFormula();
			rowitemId = ("<$$>".equals(e.attributeValue("rowitemCode")))? "-1" : rowitemMap.get(e.attributeValue("rowitemCode"));
			colitemId = ("<$$>".equals(e.attributeValue("colitemCode")))? "-1" : colitemMap.get(e.attributeValue("colitemCode"));
			fRight = e.attributeValue("fright");
			formula.setSUIT_ID(suitId);
			formula.setROWITEM_ID(rowitemId);
			formula.setCOLITEM_ID(colitemId);
			formula.setF_LEFT(e.attributeValue("fleft"));
			formula.setF_RIGHT(fRight);
			formula.setF_EXP(e.attributeValue("fexp"));
			formula.setF_DESC(e.attributeValue("fdesc"));
			formula.setERR_SCOPE(e.attributeValue("errScope"));
			formula.setDESCRIPTION(e.attributeValue("desc"));
			formula.setF_SETTYPE(e.attributeValue("fSetType"));
			formula.setLOGIC_FLAG(e.attributeValue("logicFlag"));
			formula.setLOGIC_NAME(e.attributeValue("logicName"));
			formula.setZL_FLAG(e.attributeValue("zlFlag"));
			formula.setCHK_TYPE(e.attributeValue("chkType"));
			formula.setCHK_LEVEL(e.attributeValue("chkLevel"));
			formula.setLAST_UPDATED_BY(userId);
			formula.setLAST_UPDATE_DATE(time);
			/**
			 * 通过表套id,行指标id,列指标id,公式右边判断公式是否存在
			 */
			formulaId = checkFormulaMapper.getChkFormulaId(suitId, rowitemId, colitemId, fRight);
//System.out.println("稽核公式id: " + formulaId);
			if(!"".equals(formulaId) && null != formulaId) {
				formula.setCHK_FORMULA_ID(formulaId);
				updateFormulaList.add(formula);
			} else {
				formula.setCHK_FORMULA_ID(UUID.randomUUID().toString());
				formula.setCREATED_BY(userId);
				formula.setCREATION_DATE(time);
				insertFormulaList.add(formula);
			}
		}
		//如果接收的文件上没有采集公式，则以前的公式不删除
		if(updateFormulaList.size() > 0) {
			checkFormulaMapper.updateBatchChkFormula(updateFormulaList);
		}
		if(insertFormulaList.size() > 0) {
			checkFormulaMapper.insertBatchCheckFormat(insertFormulaList);
		}
	}
}
