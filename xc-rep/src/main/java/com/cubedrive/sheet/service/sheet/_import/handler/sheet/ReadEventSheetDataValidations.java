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
package com.cubedrive.sheet.service.sheet._import.handler.sheet;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.ss.util.CellReference.NameType;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataValidation;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataValidations;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;

import com.cubedrive.base.utils.DateTimeUtil;
import com.cubedrive.sheet.service.sheet._import.SpreadsheetImportHelper;
import com.cubedrive.sheet.service.sheet._import.ctx.SheetImportContext;
import com.google.common.base.Splitter;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;

public class ReadEventSheetDataValidations {
	
	private static final WholeStrategy wholeStrategy = new WholeStrategy();
	
	private static final DecimalStrategy decimalStrategy = new DecimalStrategy();
	
	private static final DateStrategy dateStrategy = new DateStrategy();
	
	private static final ListStrategy listStrategy = new ListStrategy();
	
	private final SheetImportContext _sheetImportContext;
	
	private final List<DataValidation> _dataValidations = new ArrayList<>();
	
	private ReadEventSheetDataValidations(SheetImportContext sheetImportContext){
		_sheetImportContext = sheetImportContext;
	}
	
	public List<Map<String,Object>> getValidationSettings(){
		List<Map<String,Object>> validationSettings = new ArrayList<>();
		for(DataValidation _dataValidation: _dataValidations){
			validationSettings.add(_dataValidation.getDataValidationSetting());
		}
		return validationSettings;
	}
	
	
	private static class FormulaValue {
		
		private final boolean _isCellRef;
		private final String _value;
		
		private FormulaValue(String value){
			this._isCellRef = isCellRef(value);
			this._value = value;
		}
		
		private boolean isCellRef(String value){
			try{
				NameType nameType = CellReference.classifyCellReference(value, SpreadsheetVersion.EXCEL2007);
				if(nameType == NameType.CELL){
					return true;
				}else if(nameType == NameType.BAD_CELL_OR_NAMED_RANGE && value.contains(":")){
					String[] cellRefArray = value.split(":");
					if(cellRefArray.length == 2 && 
							NameType.CELL == CellReference.classifyCellReference(cellRefArray[0],SpreadsheetVersion.EXCEL2007) &&
							NameType.CELL == CellReference.classifyCellReference(cellRefArray[1],SpreadsheetVersion.EXCEL2007)){
						return true;
					}
				}
				return false;
			}catch(IllegalArgumentException ex){
				return false;
			}
			
		}
		
		public Map<String,Object> valueToCellRef(int tabId){
			return SpreadsheetImportHelper.getSpanMap(tabId, _value);
		}
		
	}

	
	private static class DataValidation{
		
		private final ReadEventSheetDataValidations _readEventSheetDataValidations;
		
		protected final String _operator;
		
		protected final String _sqref;
		
		protected final String _prompt;
		
		protected final FormulaValue _formulaValue1;
		
		protected final FormulaValue _formulaValue2;
		
		protected final DataValidationStrategy _dataValidationStrategy;
		
		
		private DataValidation(ReadEventSheetDataValidations readEventSheetDataValidations, String operator, String sqref, String prompt, 
				FormulaValue formulaValue1, FormulaValue formulaValue2, DataValidationStrategy dataValidationStrategy){
			this._readEventSheetDataValidations = readEventSheetDataValidations;
			this._operator = operator;
			this._sqref = sqref;
			this._prompt = prompt;
			this._formulaValue1 = formulaValue1;
			this._formulaValue2 = formulaValue2;
			this._dataValidationStrategy = dataValidationStrategy;
		}
		
		private Map<String,Object> getDataValidationSetting(){
			Map<String,Object> setting = new HashMap<>();
			setting.put("name", "vd");
			
			List<Map<String,Object>> rng = new ArrayList<>();
			Map<String,Object> spanMap = SpreadsheetImportHelper.getSpanMap(_readEventSheetDataValidations._sheetImportContext.getTabId(), _sqref);
			rng.add(spanMap);
			setting.put("rng", rng);
			
			Map<String,Object> optMap = _dataValidationStrategy.getOpt(this, _readEventSheetDataValidations);
			if(_prompt != null)
				optMap.put("hint", _prompt);
			optMap.put("allow", true);
			
			setting.put("opt", optMap);
			return setting;
		}
		
	}
	
	private static interface DataValidationStrategy {
		
		Map<String,Object> getOpt(DataValidation dataValidation, ReadEventSheetDataValidations readEventSheetDataValidations);
		
		Object getValue(FormulaValue formulaValue,ReadEventSheetDataValidations readEventSheetDataValidations);
	}
	
	private static class WholeStrategy implements DataValidationStrategy{
		
		private int op(String operator){
			String numOperator = operator;
			if(numOperator == null){
				numOperator = "between";
			}
			int _op;
			switch(numOperator){
				case "between": _op = 0; break;
				case "notBetween": _op = 1; break;
				case "lessThan": _op = 2;break;
				case "lessThanOrEqual": _op =3; break;
				case "greaterThan": _op = 4;break;
				case "greaterThanOrEqual": _op=5; break;
				case "equal": _op=6; break;
				case "notEqual": _op=7; break;
				default:
					throw new RuntimeException("numeric data validation error, operator:" + numOperator);
			}
			return _op;
		}
		
		public Map<String,Object> getOpt(DataValidation dataValidation,ReadEventSheetDataValidations readEventSheetDataValidations){
			Map<String,Object> optMap = new HashMap<>();
			optMap.put("dt", 0);
			optMap.put("op", op(dataValidation._operator));
			if(dataValidation._formulaValue2 == null){
				optMap.put("num", getValue(dataValidation._formulaValue1,readEventSheetDataValidations));
			}else{
				optMap.put("min", getValue(dataValidation._formulaValue1,readEventSheetDataValidations));
				optMap.put("max", getValue(dataValidation._formulaValue2,readEventSheetDataValidations));
			}
			
			return optMap;
		}
		
		public Object getValue(FormulaValue formulaValue, ReadEventSheetDataValidations readEventSheetDataValidations){
			Object result;
			if(formulaValue._isCellRef){
				result = formulaValue.valueToCellRef(readEventSheetDataValidations._sheetImportContext.getTabId());
			}else{
				result = Ints.tryParse(formulaValue._value);
			}
			return result;
		}
		
	}
	
	private static class DecimalStrategy extends WholeStrategy {
		
		public Object getValue(FormulaValue formulaValue, ReadEventSheetDataValidations readEventSheetDataValidations){
			Object result;
			if(formulaValue._isCellRef){
				result = formulaValue.valueToCellRef(readEventSheetDataValidations._sheetImportContext.getTabId());
			}else{
				result = Floats.tryParse(formulaValue._value);
			}
			return result;
		}
		
	}
	
	private static class DateStrategy implements DataValidationStrategy{
		
		private int op(String operator){
			int op;
			String dateOperator = operator;
			if(dateOperator == null){
				dateOperator = "between";
			}
			switch(dateOperator){
				case "equal": op = 6;break;
				case "lessThan": op = 13; break;
				case "lessThanOrEqual": op=14; break;
				case "greaterThan": op=15; break;
				case "greaterThanOrEqual": op=16; break;
				case "between": op=0;break;
				case "notBetween": op=1;break;
				default:
					throw new RuntimeException("date data validation error, operator:" + dateOperator);
			}
			return op;
		}
		
		@Override
		public Map<String, Object> getOpt(DataValidation dataValidation,ReadEventSheetDataValidations readEventSheetDataValidations) {
			Map<String,Object> optMap = new HashMap<>();
			optMap.put("dt", 2);
			optMap.put("op", op(dataValidation._operator));
			if(dataValidation._formulaValue2 == null){
				optMap.put("date", getValue(dataValidation._formulaValue1,readEventSheetDataValidations));
			}else{
				optMap.put("mind", getValue(dataValidation._formulaValue1,readEventSheetDataValidations));
				optMap.put("maxd",  getValue(dataValidation._formulaValue2,readEventSheetDataValidations));
			}
			return optMap;
		}
		
		
		public Object getValue(FormulaValue formulaValue, ReadEventSheetDataValidations readEventSheetDataValidations){
			Object result;
			if(formulaValue._isCellRef){
				result = formulaValue.valueToCellRef(readEventSheetDataValidations._sheetImportContext.getTabId());
			}else{
				 Date date = DateUtil.getJavaDate(Double.parseDouble(formulaValue._value), false);
                 result = DateTimeUtil.formatAsYYYYMMdd(date);
			}
			return result;
		}
	}
	
	private static class ListStrategy implements DataValidationStrategy{
		
		private static final Splitter commaSplitter = Splitter.on(",");
		
		@Override
		public Map<String, Object> getOpt(DataValidation dataValidation,
				ReadEventSheetDataValidations readEventSheetDataValidations) {
			Map<String,Object> optMap = new HashMap<>();
			optMap.put("dt", 3);
			optMap.put("op", 12);
			optMap.put("list", getValue(dataValidation._formulaValue1,readEventSheetDataValidations));
			return optMap;
		}
		

		public Object getValue(FormulaValue formulaValue, ReadEventSheetDataValidations readEventSheetDataValidations){
			Object result;
			if(formulaValue._isCellRef){
				result = formulaValue.valueToCellRef(readEventSheetDataValidations._sheetImportContext.getTabId());
			}else{
				result = commaSplitter.splitToList(formulaValue._value.substring(1,formulaValue._value.length()-1));
			}
			return result;
		}
		
		
	}
	
	
	public static ReadEventSheetDataValidations build(SheetImportContext sheetImportContext){
		CTWorksheet ctWorksheet = sheetImportContext.sheet().worksheet();
		ReadEventSheetDataValidations readEventSheetDataValidations = new ReadEventSheetDataValidations(sheetImportContext);
		if(ctWorksheet.isSetDataValidations() && ctWorksheet.getDataValidations().getCount()>0){
			CTDataValidations ctDataValiations= ctWorksheet.getDataValidations();
			for(CTDataValidation ctDataValidation:ctDataValiations.getDataValidationList()){
				String type = ctDataValidation.getType().toString();
				DataValidationStrategy dataValidationStrategy;
				switch(type){
					case "whole": dataValidationStrategy = wholeStrategy; break;
					case "decimal": dataValidationStrategy = decimalStrategy; break;
					case "date": dataValidationStrategy = dateStrategy; break;
					case "list": dataValidationStrategy = listStrategy; break;
					default:
						dataValidationStrategy = null; break;
				}
				if(dataValidationStrategy != null){
					String operator = null;
					if(ctDataValidation.isSetOperator()){
						operator = ctDataValidation.getOperator().toString();
					}
					String sqref = (String)ctDataValidation.getSqref().get(0);
					String prompt = null;
					if(ctDataValidation.isSetPrompt()){
						prompt = ctDataValidation.getPrompt();
					}
					FormulaValue formulaValue1 = null;
					if(ctDataValidation.isSetFormula1()){
						formulaValue1 = new FormulaValue(ctDataValidation.getFormula1());
					}
					FormulaValue formulaValue2 = null;
					if(ctDataValidation.isSetFormula2()){
						formulaValue2 = new FormulaValue(ctDataValidation.getFormula2());
					}
					DataValidation dataValidation = new DataValidation(readEventSheetDataValidations,operator, sqref, prompt,
							formulaValue1, formulaValue2, dataValidationStrategy);
					readEventSheetDataValidations._dataValidations.add(dataValidation);
				}
			}
		}
		return readEventSheetDataValidations;
	}

}
