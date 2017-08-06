/**
 * Enterprise Spreadsheet Solutions
 * Copyright(c) FeyaSoft Inc. All right reserved.
 * info@enterpriseSheet.com
 * http://www.enterpriseSheet.com
 * 
 * Licensed under the EnterpriseSheet Commercial License.
 * http://enterprisesheet.com/license.jsp
 * 
 * You need to have a valid license key to access this file.
 */
package com.cubedrive.sheet.service.sheet._export.handler.sheet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.DateUtil;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataValidation;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataValidations;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationOperator;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationType;

import com.cubedrive.base.utils.DateTimeUtil;
import com.cubedrive.sheet.service.sheet._export.SpreadsheetExportHelper;
import com.cubedrive.sheet.service.sheet._export.ctx.XSSFWorksheetExportContext;
import com.google.common.base.Joiner;

public class DataValidationProcessor extends WorksheetProcessor {
	
	private static final NumericValidationExportStrategy numericValidationExportStrategy = new NumericValidationExportStrategy();
	
	private static final DateValidationExportStrategy dateValidationExportStrategy = new DateValidationExportStrategy();
	
	private static final ListValidationExportStrategy listValidationExportStrategy = new ListValidationExportStrategy();
	
	private final Map<String,Object> _dataValidationMap;
	
	private DataValidationProcessor(Map<String,Object> dataValidationMap, XSSFWorksheetExportContext xssfWorksheetExportContext){
		super(xssfWorksheetExportContext);
		_dataValidationMap = dataValidationMap;
	}

	@Override
	public void process() {
		Map<String,Object> optMap = (Map<String,Object>)_dataValidationMap.get("opt");
		int dt = ((Integer)optMap.get("dt")).intValue();
		DataValidationExportStrategy dataValidationExportStrategy = getDataValidationExportStrategy(dt);
		if(dataValidationExportStrategy != null){
			CTWorksheet ctWorksheet = _xssfWorksheetExportContext.sheet().getXSSFSheet().getCTWorksheet();
			CTDataValidations ctDataValidations;
			int count =0;
			if(!ctWorksheet.isSetDataValidations()){
				ctDataValidations = ctWorksheet.addNewDataValidations();
			}else{
				ctDataValidations = ctWorksheet.getDataValidations();
				count = (int)ctDataValidations.getCount();
			}
			CTDataValidation ctDataValidation = ctDataValidations.addNewDataValidation();
			try{
				dataValidationExportStrategy.exportDataValidation(optMap, ctDataValidation);
				Boolean allow = (Boolean)optMap.get("allow");
				if(allow == null || allow){
					ctDataValidation.setAllowBlank(true);
				}else{
					ctDataValidation.setAllowBlank(false);
				}
				String hint = (String)optMap.get("hint");
				if(hint != null){
					ctDataValidation.setShowInputMessage(true);
					ctDataValidation.setPrompt(hint);
				}
				Map<String,Object> spanMap = ((List<Map<String,Object>>)_dataValidationMap.get("rng")).get(0);
				String sqref = SpreadsheetExportHelper.spanToSqref((List<Integer>)spanMap.get("span"), false);
				List<String> sqrefList = new ArrayList<>();
				sqrefList.add(sqref);
				ctDataValidation.setSqref(sqrefList);
				ctDataValidations.setCount(++count);
			}catch(Exception ex){
				ctDataValidations.removeDataValidation(ctDataValidations.sizeOfDataValidationArray() - 1);
			}
		}
		
		
	}
	
	
	private DataValidationExportStrategy getDataValidationExportStrategy(int dt){
		DataValidationExportStrategy _strategy;
		switch(dt){
			case 0: 
				_strategy = numericValidationExportStrategy;
				break;
			case 2: 
				_strategy = dateValidationExportStrategy;
				break;
			case 3: 
				_strategy = listValidationExportStrategy;
				break;
			default:
				_strategy = null;
				break;
		}
		return _strategy;
	}
	
	private static STDataValidationOperator.Enum getOperator(int op){
		STDataValidationOperator.Enum _operator;
		switch(op){
			case 0:
			case 12:
				_operator = STDataValidationOperator.BETWEEN;
				break;
			case 1:
				_operator = STDataValidationOperator.NOT_BETWEEN;
				break;
			case 2:
			case 13:
				_operator = STDataValidationOperator.LESS_THAN;
				break;
			case 3:
			case 14:
				_operator = STDataValidationOperator.LESS_THAN_OR_EQUAL;
				break;
			case 4:
			case 15:
				_operator = STDataValidationOperator.GREATER_THAN;
				break;
			case 5:
			case 16:
				_operator = STDataValidationOperator.GREATER_THAN_OR_EQUAL;
				break;
			case 6:
				_operator = STDataValidationOperator.EQUAL;
				break;
			case 7:
				_operator = STDataValidationOperator.NOT_EQUAL;
				break;
			default:
				throw new IllegalStateException("Can't find operator.op:" + op);
		}
		return _operator;
	}
	
	private static interface DataValidationExportStrategy {
		
		void exportDataValidation(Map<String,Object> optMap, CTDataValidation ctDataValidation);
		
	}
	
	
	private static class NumericValidationExportStrategy implements DataValidationExportStrategy{

		@Override
		public void exportDataValidation(Map<String, Object> optMap, CTDataValidation ctDataValidation) {
			ctDataValidation.setType(STDataValidationType.DECIMAL);
			STDataValidationOperator.Enum operator = getOperator((int)optMap.get("op"));
			Object formulaObject1 = null;
			String formula1 = null;
			Object formulaObject2 = null;
			String formula2 = null;
			if(STDataValidationOperator.BETWEEN.equals(operator) || STDataValidationOperator.NOT_BETWEEN.equals(operator)){
				formulaObject1 = optMap.get("min");
				formulaObject2 = optMap.get("max");
			}else{
				formulaObject1 = optMap.get("num");
			}
			
			if(formulaObject1 instanceof Map ){
				formula1 = SpreadsheetExportHelper.spanToSqref((List<Integer>)(((Map<String,Object>)formulaObject1).get("span")), true);
			}else if(formulaObject1 instanceof Number){
				formula1 = ((Number)formulaObject1).toString();
			}
			
			if(formulaObject2 instanceof Map) {
				formula2 = SpreadsheetExportHelper.spanToSqref((List<Integer>)(((Map<String,Object>)formulaObject2).get("span")), true);
			}else if(formulaObject2 instanceof Number){
				formula2 = ((Number)formulaObject2).toString();
			}
			
			if(formula1 == null && formula2 ==null){
				throw new IllegalStateException("formula1 and formula2 are both null");
			}
			
			ctDataValidation.setOperator(operator);
			ctDataValidation.setFormula1(formula1);
			if(formula2 != null){
				ctDataValidation.setFormula2(formula2);
			}
			
		}
		
		
	}
	
	private static class DateValidationExportStrategy implements DataValidationExportStrategy{

		@Override
		public void exportDataValidation(Map<String, Object> optMap, CTDataValidation ctDataValidation) {
			ctDataValidation.setType(STDataValidationType.DATE);
			STDataValidationOperator.Enum operator = getOperator((int)optMap.get("op"));
			Object formulaObject1 = null;
			String formula1 = null;
			Object formulaObject2 = null;
			String formula2 = null;
			if(STDataValidationOperator.BETWEEN.equals(operator) || STDataValidationOperator.NOT_BETWEEN.equals(operator)){
				formulaObject1 = optMap.get("mind");
				formulaObject2 = optMap.get("maxd");
			}else{
				formulaObject1 = optMap.get("date");
			}
			
			
			if(formulaObject1 instanceof Map ){
				formula1 = SpreadsheetExportHelper.spanToSqref((List<Integer>)(((Map<String,Object>)formulaObject1).get("span")), true);
			}else if(formulaObject1 instanceof String){
				formula1 = String.valueOf(DateUtil.getExcelDate(DateTimeUtil.parseAsYYYYMMdd((String)formulaObject1), false));
			}
			
			if(formulaObject2 instanceof Map) {
				formula2 = SpreadsheetExportHelper.spanToSqref((List<Integer>)(((Map<String,Object>)formulaObject2).get("span")), true);
			}else if(formulaObject2 instanceof String){
				formula2 = String.valueOf(DateUtil.getExcelDate(DateTimeUtil.parseAsYYYYMMdd((String)formulaObject2), false));
			}
			
			if(formula1 == null && formula2 ==null){
				throw new IllegalStateException("formula1 and formula2 are both null");
			}
			
			ctDataValidation.setOperator(operator);
			ctDataValidation.setFormula1(formula1);
			if(formula2 != null){
				ctDataValidation.setFormula2(formula2);
			}
		}
		
	}
	
	private static class ListValidationExportStrategy implements DataValidationExportStrategy{
		private final static Joiner commaJoiner = Joiner.on(',');

		@Override
		public void exportDataValidation(Map<String, Object> optMap, CTDataValidation ctDataValidation) {
			ctDataValidation.setType(STDataValidationType.LIST);
			Object listObject = optMap.get("list");
			String formula1 = null;
			if(listObject instanceof Map){
				formula1 = SpreadsheetExportHelper.spanToSqref((List<Integer>)(((Map<String,Object>)listObject).get("span")), true);
			}else if(listObject instanceof List){
				formula1 =  "\"" + commaJoiner.join((List<Object>)listObject) +"\"";
			}
			if(formula1 != null){
				ctDataValidation.setFormula1(formula1);
			}else{
				throw new IllegalStateException("formula1 is null");
			}
			
		}
		
	}
	
	public static DataValidationProcessor build(Map<String,Object> dataValidationMap,XSSFWorksheetExportContext xssfWorksheetExportContext){
		DataValidationProcessor dataValidationProcessor = new DataValidationProcessor(dataValidationMap,xssfWorksheetExportContext);
		return dataValidationProcessor; 
	}

}
