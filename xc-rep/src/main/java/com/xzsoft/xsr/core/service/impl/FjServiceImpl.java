package com.xzsoft.xsr.core.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.PreparedStatement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSetMetaData;

import com.google.common.collect.Maps;
import com.xzsoft.xip.framework.util.JSONUtil;
import com.xzsoft.xip.framework.util.SpringDBHelp;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xsr.core.mapper.FjMapper;
import com.xzsoft.xsr.core.mapper.MakeReportMapper;
import com.xzsoft.xsr.core.mapper.ModalsheetMapper;
import com.xzsoft.xsr.core.modal.CellData;
import com.xzsoft.xsr.core.modal.Colitem;
import com.xzsoft.xsr.core.modal.ModalSheet;
import com.xzsoft.xsr.core.modal.ModalSheetElement;
import com.xzsoft.xsr.core.modal.ModalSheetFormat;
import com.xzsoft.xsr.core.service.CellFormulaService;
import com.xzsoft.xsr.core.service.FjService;
import com.xzsoft.xsr.core.util.FormulaTranslate;
import com.xzsoft.xsr.core.util.JsonUtil;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.annotation.Resource;
@Service("fjService")
public class FjServiceImpl implements FjService {
	
	@Resource
	private FjMapper fjmapper;
	@Resource
	private CellFormulaService  cellFormulaService;
	@Resource
	private MakeReportMapper makeReportMapper;
	@Resource
	private ModalsheetMapper modalsheetMapper;
	
	/**
	 * 获取数据源字段
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,noRollbackFor={Exception.class})
	public String getFields(String sql) throws Exception {
		String metaData = "{'fields':[{'name':'COLUMN_NAME','type':'string'},{'name':'COLUMN_TYPE','type':'string'}]}";
		Connection conn = SpringDBHelp.getConnection();
		if ("".equals(sql) || sql == null) {
			return null;
		}
		List<HashMap> fields = new ArrayList<HashMap>();
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(sql);
			ResultSetMetaData rsd = pst.executeQuery().getMetaData();
			
			for (int i = 0; i < rsd.getColumnCount(); i++) {
				HashMap field = new HashMap();
				field.put("COLUMN_NAME", rsd.getColumnLabel(i + 1)); //字段名称
				field.put("COLUMN_TYPE", rsd.getColumnTypeName(i + 1)); //数据库类型
				fields.add(field);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			if(pst!=null){
				pst.close();
			}
			SpringDBHelp.releaseConnection(conn); 
		}
		int size = fields.size();
		String rows = "";
		for(int i=0; i<size; i++){
			rows = rows + "{'COLUMN_NAME':'" + fields.get(i).get("COLUMN_NAME") + "','COLUMN_TYPE':'" + fields.get(i).get("COLUMN_TYPE")+ "'},";
		}
		if (rows.lastIndexOf(",") == rows.length() - 1) {
            rows = rows.substring(0, rows.length() - 1);
        }
		
		String json = JSONUtil.parseStoreJsonForExt4(size, "true", metaData, rows);
		
		return json;
	}

	/**
	 * 加载浮动行模版格式
	 * @param msFormatId
	 * @param modalsheetName
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,noRollbackFor={Exception.class})
	public Map<String, Object> loadFjModalsheetFormat(String userId,String suitId,String modalsheetId,String modaltypeId,String rowItemId,String entity_id,
			String msFormatId,String modalsheetName,String titleMaxRow,String rowItemCode,String mark,String periodId,String currencyId)throws Exception {
		List<ModalSheet> sheetList = new ArrayList<ModalSheet>();
		ModalSheet ms = new ModalSheet(Integer.parseInt(msFormatId),
				modalsheetName);
		sheetList.add(ms);
		List<ModalSheetFormat> newcellList =new ArrayList<ModalSheetFormat>();
//		List<ModalSheetFormat> final_cellList =new ArrayList<ModalSheetFormat>();
		ModalSheetFormat modalsf=null;
//		ModalSheetFormat final_modalsf=null;
		int count=0;
		if("FjData".equals(mark)){
				if(userId!=null&&!"".equals(userId)){
					count=fjmapper.countFjTempData(suitId, entity_id, periodId, currencyId, rowItemId, modalsheetId, userId);  //判断浮动行下的数据记录数
				}else{
					System.out.println("session失效------------");
					return null;
				}
		}else{
			count=fjmapper.countFjFormula(suitId, entity_id, rowItemId, modalsheetId);      //判断浮动行公式设置记录数
		}
		List<ModalSheetFormat> cellList = fjmapper.getFjModalsheetFormatCellList(msFormatId,titleMaxRow,rowItemCode);   //获取表头和浮动行的模版格式
		String rowno_col="";     //行次所在物理列位置
		Set<String> finalset=new HashSet<String>();
		Map<String, String>map1=new HashMap<String, String>();
		String oldrow="";
		String oldformula="";
		//将浮动行复制10行
		for (ModalSheetFormat msf : cellList) {
			if(msf.getCell_comment()!=null&&msf.getCell_comment().equals("rowno")){
				rowno_col=msf.getCol().toString();
			}
			if(msf.getRow()>Integer.parseInt(titleMaxRow)){
				oldrow=msf.getRow().toString();
				msf.setRow(Integer.parseInt(titleMaxRow)+1);
				String content=msf.getJson();
				Map<String, Object> jsonMap = JsonUtil.getJsonObj(content);
				if(jsonMap.get("cal")==null){
					jsonMap.put("data","");
				}else if(jsonMap.get("cal")!=null&&jsonMap.get("cal").toString().equals("true")){ 
					String formula=jsonMap.get("data").toString();
					oldformula=formula;
					String f1=formula.replaceAll("[A-Z]+[1-9]+","dygwz");
					String f2=formula;
					Set<String> set1=new HashSet<String>();
					boolean isline=true;
					String[] s1=f1.split("dygwz");
					for(int i=0;i<s1.length;i++){
						if(s1[i]!=null && s1[i]!=""){
							f2=f2.replace(s1[i], ",");
						}
					}
					f2=f2.replaceAll("[,]{2,}", ",");
					String[] s2=f2.split(",");
					for(int i=0;i<s2.length;i++){
						if(s2[i]!=null && !"".equals(s2[i])){
//							if("".equals(f3)){
//								f3=s2[i].replaceAll("[A-Z]+", "");
//							}
							set1.add(s2[i]);
							finalset.add(s2[i]);
						}
					}
					for (String s : set1) {
						if(!s.replaceAll("[A-Z]+", "").equals(oldrow)){
							isline=false;
							break;
						}else{
							map1.put(s,s.replaceAll("[1-9]+", ""));
						}
					}
					if(isline){
						for (String s : set1) {
							formula=formula.replace(s,map1.get(s)+msf.getRow());
						}
						jsonMap.put("data", formula);
					}else{
						jsonMap.remove("cal");
						jsonMap.remove("value");
						jsonMap.put("data","");
					}
					
				}
			
				msf.setJson(JsonUtil.toJson(jsonMap));
				modalsf=new ModalSheetFormat(msf.getFormatId(),msf.getSheet(),msf.getRow(),msf.getCol(),msf.getJson(),msf.getCal(),msf.getCreationDate(),msf.getCreatedBy(),msf.getCell_comment(),msf.getCell_comment_type());
//				final_cellList.add(modalsf);
				for(int i=1;i<=(count<=10?9:count-1);i++){
					modalsf=new ModalSheetFormat(msf.getFormatId(),msf.getSheet(),msf.getRow(),msf.getCol(),msf.getJson(),msf.getCal(),msf.getCreationDate(),msf.getCreatedBy(),msf.getCell_comment(),msf.getCell_comment_type());
					modalsf.setRow(modalsf.getRow()+i);
					if(jsonMap.get("cal")!=null&&jsonMap.get("cal").toString().equals("true")){
						String formula=oldformula;
						Set<String> tempset=finalset;
						for (String s : tempset) {
							formula=formula.replace(s,map1.get(s)+modalsf.getRow());
						}
						jsonMap.put("data", formula);
						modalsf.setJson(JsonUtil.toJson(jsonMap));
					}
					newcellList.add(modalsf);
				}
			}
		}
		for (ModalSheetFormat nmsf : newcellList) {
			cellList.add(nmsf);
		}
		//加载模版数据
		List<Map<String, String>>recs=new ArrayList<Map<String,String>>();
			/*判断当前为浮动行公式设置还是报表浮动行数据*/
		if("FjData".equals(mark)){
			if(userId!=null&&!"".equals(userId)){
				recs=selRepFjData(userId,suitId, modalsheetId, modaltypeId, rowItemId, entity_id, periodId, currencyId);  //浮动行数据(临时表)
			}else{
				System.out.println("session失效------------");
			}
		}else{
			recs=selFjCalFormula(suitId, modalsheetId, modaltypeId, rowItemId, entity_id);                     //浮动行公式
		}	 
		  //将map数据中的DATA列转换成与之对应的物理列位置y
		List<Colitem> colList=fjmapper.getFjColitemList(suitId, modalsheetId,modaltypeId);
		for (Map<String, String> map : recs) {
			for (Colitem col : colList) {
				map.put(col.getCOL().toString(),map.get(col.getDATA_COL()));
				map.remove(col.getDATA_COL());
			}
		}
		  //
		for (Map<String, String> rec : recs) {
			String rownumdtl=rec.get("ROWNUMDTL");
//			if(Integer.parseInt(rownumdtl)+Integer.parseInt(titleMaxRow)>Integer.parseInt(titleMaxRow)+10){
//				for (ModalSheetFormat msf : final_cellList) {
//					modalsf=new ModalSheetFormat(msf.getFormatId(),msf.getSheet(),msf.getRow(),msf.getCol(),msf.getJson(),msf.getCal(),msf.getCreationDate(),msf.getCreatedBy(),msf.getCell_comment(),msf.getCell_comment_type());
//					modalsf.setRow(Integer.parseInt(rownumdtl)+Integer.parseInt(titleMaxRow));
//					String content=modalsf.getJson();
//					Map<String, Object> jsonMap = JsonUtil.getJsonObj(content);
//					if(modalsf.getCol()==Integer.parseInt(rowno_col)){
//						jsonMap.put("data",rownumdtl);
//					}else if(rec.get(modalsf.getCol().toString())!=null){
//						if(jsonMap.get("cal")!=null&&jsonMap.get("cal").toString().equals("true")){
//							jsonMap.put("value",rec.get(modalsf.getCol().toString()));
//		        		}else{
//		        			jsonMap.put("data",rec.get(modalsf.getCol().toString()));
//		        		}
//					}
//					modalsf.setJson(JsonUtil.toJson(jsonMap));
//					cellList.add(modalsf);
//				}
				
//			}else{
				for (ModalSheetFormat msf : cellList) {
					if(Integer.parseInt(rownumdtl)+Integer.parseInt(titleMaxRow)==msf.getRow()){
						String content=msf.getJson();
						Map<String, Object> jsonMap = JsonUtil.getJsonObj(content);
						if(msf.getCol()==Integer.parseInt(rowno_col)){
							jsonMap.put("data",rownumdtl);
						}else if(rec.get(msf.getCol().toString())!=null){
							if(jsonMap.get("cal")!=null&&jsonMap.get("cal").toString().equals("true")){
								jsonMap.put("value",rec.get(msf.getCol().toString()));
			        		}else{
			        			jsonMap.put("data",rec.get(msf.getCol().toString()));
			        		}
							
						}
						msf.setJson(JsonUtil.toJson(jsonMap));
//						System.out.println("1");
					} 
				}
				
//			}
			
		}
		List<ModalSheetElement> elementList = fjmapper.getFjModalsheetElementList(msFormatId);
		for (int i=0;i<elementList.size();i++) {
			ModalSheetElement mse=elementList.get(i);
			String type=mse.getFtype();
			String content=mse.getJson();
			if("meg".equals(type)){
				content=content.replace("[", "").replace("]", "");
				String[] cont=content.split(",");
				if(Integer.parseInt(cont[0])>Integer.parseInt(titleMaxRow)){
					elementList.remove(i);
				}
			}
			
		}
		Map<String, Object> results = Maps.newHashMap();
		results.put("fileName", modalsheetName);
		results.put("sheets", sheetList);
		results.put("cells", cellList);
		results.put("floatings", elementList);
		
		return results;
	}
	/**
	 * 报表查询时，主从表合并加载
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,noRollbackFor={Exception.class})
	public Map<String, Object> loadRepFormat(String userId, String suitId,String modalsheetId, String modaltypeId,String entity_id, 
			String msFormatId, String modalsheetName,String titleMaxRow, String periodId,String currencyId) throws Exception {
		List<ModalSheet> sheetList = new ArrayList<ModalSheet>();
		ModalSheet ms = new ModalSheet(Integer.parseInt(msFormatId),
				modalsheetName);
		sheetList.add(ms);
		List<Map<String, String>> fjRows=makeReportMapper.getFjRowitemInfo(suitId, modaltypeId, modalsheetId);
		List<ModalSheetFormat> cellList = modalsheetMapper.getModalsheetFormatCellList(msFormatId);    //获取主表格式
		List<ModalSheetFormat> newcellList =new ArrayList<ModalSheetFormat>();
		List<Map<String, String>> recs=selRepFjDataAll(suitId, modalsheetId, modaltypeId, entity_id, periodId, currencyId);  //获取模版下所有的浮动行数据
		//将map数据中的DATA列转换成与之对应的物理列位置y
		List<Colitem> colList=fjmapper.getFjColitemList(suitId, modalsheetId,modaltypeId);
		for (Map<String, String> map : recs) {
			for (Colitem col : colList) {
				map.put(col.getCOL().toString(),map.get(col.getDATA_COL()));
				map.remove(col.getDATA_COL());
			}
		}
		ModalSheetFormat modalsf=null;
		//获取主行数据
		Map<String,String> map=new HashMap<String,String>();
		map.put("suitId", suitId);
		map.put("msformatId", msFormatId);
		map.put("entityId", entity_id);
		map.put("periodId", periodId);
		map.put("cnyId", currencyId);
		map.put("dbType", PlatformUtil.getDbType());
		List<CellData> DataList=makeReportMapper.loadDataValue(map);
		//
		String rowno_col="";
		String rowitem_col="";
		for (ModalSheetFormat msf : cellList) {
			if(msf.getCell_comment()!=null&&msf.getCell_comment().equals("rowno")){
				rowno_col=msf.getCol().toString();
			}
			if(msf.getCell_comment()!=null&&msf.getCell_comment().equals("rowitem")){
				rowitem_col=msf.getCol().toString();
			}
		}
		for (ModalSheetFormat msf : cellList) {
			
			//给主行数据单元格赋值
			for(int i=0;i<DataList.size();i++){
				CellData celldata=DataList.get(i);
				String content=msf.getJson();
				Map<String, Object> jsonMap = JsonUtil.getJsonObj(content);
				if(jsonMap.get("cal")!=null&&jsonMap.get("cal").toString().equals("true")){
					jsonMap.remove("cal");
					jsonMap.remove("value");
					jsonMap.put("data","");
				}
				if(msf.getCol()==Integer.parseInt(celldata.getCol())&&msf.getRow()==Integer.parseInt(celldata.getRow())){
					String data=celldata.getJson();
					data=data.replace("\'","\"");
					data=data.replace("data","\"data\"");
					Map<String, Object> dataMap = JsonUtil.getJsonObj(data);
					jsonMap.put("data",dataMap.get("data"));
					msf.setJson(JsonUtil.toJson(jsonMap));
					msf.setCal(false);
					break;
				}
				msf.setJson(JsonUtil.toJson(jsonMap));
				msf.setCal(false);
			}
			//把浮动行插入到对应主行下
			for(int i=0;i<fjRows.size();i++){
				Map<String, String>fjRow=fjRows.get(i);
				String rowitemId=fjRow.get("ROWITEM_ID");
				String rowitemCode=fjRow.get("ROWITEM_CODE");
				if(msf.getCol()==Integer.parseInt(rowno_col)&&msf.getRow()>Integer.parseInt(titleMaxRow)){
					if(msf.getCell_comment()!=null&&msf.getCell_comment().equals(rowitemCode)){
						int row=msf.getRow();
						int cnt=fjmapper.countFjData(suitId, entity_id, periodId, currencyId, rowitemId, modalsheetId);
//						Pattern p = Pattern.compile("^-?\\d*[.]?\\d*$");
						List<Map<String, String>> temps=new ArrayList<Map<String,String>>(); 
						Map<String, String> temp=null;
						for (ModalSheetFormat msf2 : cellList) {
							if(msf2.getRow()==row){
								String content=msf2.getJson();
								Map<String, Object> jsonMap = JsonUtil.getJsonObj(content);
								if(jsonMap.get("cal")!=null&&jsonMap.get("cal").toString().equals("true")){
									jsonMap.remove("cal");
									jsonMap.remove("value");
									jsonMap.put("data","");
									msf2.setJson(JsonUtil.toJson(jsonMap));
									msf2.setCal(false);
								}
//								if(jsonMap.get("data")==null){
//									jsonMap.put("data","");
//								}
//								Matcher m = p.matcher((CharSequence) jsonMap.get("data"));
								if(msf2.getCol()==Integer.parseInt(rowitem_col)|| msf2.getCol()==Integer.parseInt(rowno_col)){
									temp=new HashMap<String, String>();
									temp.put("col", msf2.getCol().toString());
									temp.put("data", jsonMap.get("data").toString());
									temps.add(temp);
								}
								for(int i2=1;i2<=cnt;i2++){
									modalsf=new ModalSheetFormat(msf2.getFormatId(),msf2.getSheet(),msf2.getRow(),msf2.getCol(),msf2.getJson(),msf2.getCal(),msf2.getCreationDate(),msf2.getCreatedBy(),msf2.getCell_comment(),msf2.getCell_comment_type());
									modalsf.setRow(modalsf.getRow()+i2);
									newcellList.add(modalsf);
								}
							}else if(msf2.getRow()>row){
								msf2.setRow(msf2.getRow()+cnt);
							}
						}
						//给浮动行赋值
						for (ModalSheetFormat nmsf : newcellList) {
							for (Map<String, String> rec : recs) {
								String rownumdtl=rec.get("ROWNUMDTL");
								if(rec.get("ROWITEM_CODE").equals(rowitemCode)&&nmsf.getRow()==row+Integer.parseInt(rownumdtl)){
									String content=nmsf.getJson();
									Map<String, Object> jsonMap = JsonUtil.getJsonObj(content);
									if(nmsf.getCol()==Integer.parseInt(rowno_col)){
										for (Map<String, String> map2 : temps) {
											if(map2.get("col").equals(rowno_col)){
												jsonMap.put("data",map2.get("data")+"-"+rownumdtl);
											}
										}
									}else if(rec.get(nmsf.getCol().toString())!=null){
										if(nmsf.getCol()==Integer.parseInt(rowitem_col)){
											for (Map<String, String> map2 : temps) {
												if(map2.get("col").equals(rowitem_col)){
													jsonMap.put("data",map2.get("data")+"-"+rec.get(nmsf.getCol().toString()));
												}
											}
										}else{
											jsonMap.put("data",rec.get(nmsf.getCol().toString()));
//											jsonMap.put("itms", "{i:'1'}");
											jsonMap.put("itms", "1");
										}
									}
									nmsf.setJson(JsonUtil.toJson(jsonMap));
									break;
								}
							}
						}
						//同时改变主表数据区域的位置
						for(int i2=0;i2<DataList.size();i2++){
							CellData celldata=DataList.get(i2);
							if(Integer.parseInt(celldata.getRow())>row){
								celldata.setRow(((Integer)(Integer.parseInt(celldata.getRow())+cnt)).toString());
							}
						}	
						break;
					}
				}
				
			}
			
		}
		for (ModalSheetFormat nmsf : newcellList) {
			cellList.add(nmsf);
		}
		
		List<ModalSheetElement> elementList = modalsheetMapper.getModalsheetElementList(msFormatId);       //加载合并单元格等信息
		for (int i=0;i<elementList.size();i++) {
			ModalSheetElement mse=elementList.get(i);
			String type=mse.getFtype();
			String content=mse.getJson();
			if("meg".equals(type)){
				content=content.replace("[", "").replace("]", "");
				String[] cont=content.split(",");
				if(Integer.parseInt(cont[0])>Integer.parseInt(titleMaxRow)){
					String tempC="[";
					for(int j=0;j<cont.length;j++){
						if(j==0||j==2){
							cont[j]=(Integer.parseInt(cont[j])+recs.size())+"";
						}
						if(j==cont.length-1){
							tempC=tempC+cont[j]+"]";
						}else{
							tempC=tempC+cont[j]+",";
						}
					}
					mse.setJson(tempC);
				}
			}
			
		}
		Map<String, Object> results = Maps.newHashMap();
		results.put("fileName", modalsheetName);
		results.put("sheets", sheetList);
		results.put("cells", cellList);
		results.put("floatings", elementList);
		
		return results;
		
	}
	
	/**
	 * 查询浮动行公式设置数据
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,noRollbackFor={Exception.class})
	public List<Map<String, String>> selFjCalFormula(String suitId,String modalsheetId,String modaltypeId,
			String rowItemId,String entity_id)throws Exception{
		List<Colitem> colList=fjmapper.getFjColitemList(suitId, modalsheetId,modaltypeId);   //获取DATA与列指标的对应
		Connection conn = SpringDBHelp.getConnection();
		PreparedStatement pst = null;
		StringBuffer sql=new StringBuffer();
		String keys="";
		for(int i=0;i<colList.size();i++){
			Colitem col=colList.get(i);
			keys=keys+col.getDATA_COL()+",";
		}
		if (keys.lastIndexOf(",") == keys.length() - 1) {
			keys = keys.substring(0, keys.length() - 1);
        }
		sql.append("SELECT ROWNUMDTL,").append(keys).append(" FROM xsr_rep_fj_cal_formula_new WHERE SUIT_ID=? AND ENTITY_ID=? AND MODALSHEET_ID=? AND ROWITEM_ID=?");
		List<Map<String, String>> recs=new ArrayList<Map<String, String>>();
		try{
			pst=conn.prepareStatement(sql.toString());
			pst.setString(1,suitId);
			pst.setString(2,entity_id);
			pst.setString(3,modalsheetId);
			pst.setString(4,rowItemId);
			ResultSet rs=pst.executeQuery();
			while(rs.next()){
				Map<String, String> rec=new HashMap<String, String>();
				rec.put("ROWNUMDTL",rs.getString("ROWNUMDTL"));
				for (Colitem col : colList) {
	//				rec.put(col.getCOL().toString(),rs.getString(col.getDATA_COL()));
					rec.put(col.getDATA_COL().toString(),rs.getString(col.getDATA_COL()));
				}
				recs.add(rec);
			}
		}catch(SQLException e){
			throw e;
		}finally{
			if(pst!=null){
				pst.close();
			}
			SpringDBHelp.releaseConnection(conn); 
		}
		
		return recs;
	}
	/**
	 * 查询报表浮动行数据(临时表)
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,noRollbackFor={Exception.class})
	public List<Map<String, String>> selRepFjData(String userId,String suitId,String modalsheetId,String modaltypeId,
			String rowItemId,String entity_id,String periodId,String currencyId)throws Exception{
		List<Colitem> colList=fjmapper.getFjColitemList(suitId, modalsheetId,modaltypeId);   //获取DATA与列指标的对应
		Connection conn = SpringDBHelp.getConnection();
		PreparedStatement pst = null;
		StringBuffer sql=new StringBuffer();
		String keys="";
		for(int i=0;i<colList.size();i++){
			Colitem col=colList.get(i);
			keys=keys+col.getDATA_COL()+",";
		}
		if (keys.lastIndexOf(",") == keys.length() - 1) {
			keys = keys.substring(0, keys.length() - 1);
        }
		sql.append("SELECT ROWNUMDTL,").append(keys).append(" FROM xsr_rep_fj_cellvalue_temp WHERE SUIT_ID=? AND ENTITY_ID=? AND MODALSHEET_ID=? AND ROWITEM_ID=? AND PERIOD_ID=? AND CURRENCY_ID=? AND USER_ID=?");
		List<Map<String, String>> recs=new ArrayList<Map<String, String>>();
		try{
			pst=conn.prepareStatement(sql.toString());
			pst.setString(1,suitId);
			pst.setString(2,entity_id);
			pst.setString(3,modalsheetId);
			pst.setString(4,rowItemId);
			pst.setString(5,periodId);
			pst.setString(6,currencyId);
			pst.setString(7,userId);
			ResultSet rs=pst.executeQuery();
			while(rs.next()){
				Map<String, String> rec=new HashMap<String, String>();
				rec.put("ROWNUMDTL",rs.getString("ROWNUMDTL"));
				for (Colitem col : colList) {
					rec.put(col.getDATA_COL().toString(),rs.getString(col.getDATA_COL()));
				}
				recs.add(rec);
			}
		}catch(SQLException e){
			throw e;
		}finally{
			if(pst!=null){
				pst.close();
			}
			SpringDBHelp.releaseConnection(conn); 
		}
		return recs;
	}
	/**
	 * 浮动行公式设置数据提交
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,noRollbackFor={Exception.class})
	public String saveCellFormula(String modaltypeId,String modalsheetId, String entity_id,
			String titleMaxRow, String rowItemId, String sheetJson,
			String startCol,String suitId, String userId) throws Exception {
		int cnt=fjmapper.delFjCalFormula(suitId, entity_id, modalsheetId, rowItemId);	  //删除原数据
		
		List<Map<String, String>>sqlList=new ArrayList<Map<String,String>>();
		List<Map<String, String>>datasource=sheetJsonToData(modaltypeId,sheetJson, suitId, modalsheetId, titleMaxRow,"");
		int maxRow=0;
		for (Map<String, String> map : datasource) {
			if(Integer.parseInt(map.get("x"))>maxRow){
				maxRow=Integer.parseInt(map.get("x"));
			}
		}
		Pattern p = Pattern.compile("^-?0{1}[.]?0*$");
		for (int i=Integer.parseInt(titleMaxRow)+1;i<=maxRow;i++) {
			boolean flag=false;
			Map<String, String> sqlMap=new HashMap<String, String>();
			for (Map<String, String> map : datasource) {
				if(Integer.parseInt(map.get("x"))==i){
					sqlMap.put(map.get("D"), map.get("data"));
					Matcher m=p.matcher(map.get("data"));
					if(map.get("data")!=null&&!"".equals(map.get("data"))&&!m.matches()){
						flag=true;
					}
				}
			}
			if(flag){
				sqlMap.put("FJ_CAL_ID",UUID.randomUUID().toString());
//				sqlMap.put("ROWNUMDTL", (i-Integer.parseInt(titleMaxRow))+"");
				sqlMap.put("ISFM", "N");
				sqlList.add(sqlMap);
			}
		}
//		保存sqlList中的数据(公式设置保存)
		List<Colitem> colList=fjmapper.getFjColitemList(suitId, modalsheetId,modaltypeId);
		if(colList.size()<=0){
			System.out.println("查询DATA列与列指标映射关系失败-----");
			return null;
		}
//		Map<String, String>row=sqlList.get(0);
//		Set<String>rowKey=row.keySet();
		String keys="";
		String val="";
//		int i=1;
//		for (String s : rowKey) {
//			if(s!=null&&!"".equals(s)&&s.substring(0, 4).equals("DATA")){
//				keys=keys+"DATA"+i+",";
//				i++;
//			}
//		}
		for(int i=0;i<colList.size();i++){
			Colitem col=colList.get(i);
			keys=keys+col.getDATA_COL()+",";
		}
		if (keys.lastIndexOf(",") == keys.length() - 1) {
			keys = keys.substring(0, keys.length() - 1);
        }
		for(int i2=0;i2<keys.split(",").length;i2++){
			val+="?,";
		}
		if (val.lastIndexOf(",") == val.length() - 1) {
			val = val.substring(0, val.length() - 1);
        }
		
		Connection conn = SpringDBHelp.getConnection();
		PreparedStatement pst = null;
		StringBuffer sql=new StringBuffer();
		sql.append("INSERT INTO xsr_rep_fj_cal_formula_new(FJ_CAL_ID,SUIT_ID,ENTITY_ID,MODALSHEET_ID,ROWITEM_ID,ROWNUMDTL,ISFM,LAST_UPDATE_DATE,LAST_UPDATED_BY,CREATION_DATE,CREATED_BY,").
			append(keys).append(")VALUES(?,?,?,?,?,?,?,?,?,?,?,").append(val).append(")");
		try{
			pst=conn.prepareStatement(sql.toString());
			for(int i3=1;i3<=sqlList.size();i3++){
				Map<String, String> rec=sqlList.get(i3-1);
				pst.setString(1, rec.get("FJ_CAL_ID"));
				pst.setString(2, suitId);
				pst.setString(3, entity_id);
				pst.setString(4, modalsheetId);
				pst.setString(5, rowItemId);
//				pst.setInt(6, Integer.parseInt(rec.get("ROWNUMDTL")));
				pst.setInt(6, i3);
				pst.setString(7, rec.get("ISFM"));
				pst.setString(8,new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				pst.setString(9, userId);
				pst.setString(10,new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				pst.setString(11, userId);
				for(int i4=1;i4<=colList.size();i4++){
					pst.setString(i4+11, rec.get(colList.get(i4-1).getDATA_COL()));
				}
				pst.addBatch();
				
				if(i3%1000==0){
					pst.executeBatch();
				}
				
			}
			pst.executeBatch();
		}catch(SQLException e){
			throw e;
		}finally{
			if(pst!=null){
				pst.close();
			}
			SpringDBHelp.releaseConnection(conn); 
		}
		return null;
	}
	/**
	 * 页面sheetJson转化为{"x":"","y":"","data":"","D":""}数据形式
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,noRollbackFor={Exception.class})
	public List<Map<String, String>> sheetJsonToData(String modaltypeId,String sheetJson,String suitId,String modalsheetId,String titleMaxRow,String mark) throws Exception {
		List<Map<String, String>> meteData=new ArrayList<Map<String,String>>();
		//将页面的sheetJson数据转化为map
		 Map<String, Object> jsonObjMap = JsonUtil.getJsonObj(sheetJson);
		 List<Object> cells = (List<Object>)jsonObjMap.get("cells");
		  for (int i = 0; i < cells.size(); i++) {
			  Map<String, String> data=new HashMap<String, String>();
	        	Map<String, Object> cellMap = (Map<String, Object>) cells.get(i);
	        	if(Integer.parseInt(cellMap.get("x").toString())>Integer.parseInt(titleMaxRow)){
	        		data.put("x", cellMap.get("x").toString());
	        		data.put("y", cellMap.get("y").toString());
	        		Map<String, Object> jsonMap = JsonUtil.getJsonObj(cellMap.get("j").toString());
	        		if(cellMap.get("c")!=null&&cellMap.get("c").toString().equals("true")){
	        			data.put("data","FjData".equals(mark)?jsonMap.get("value").toString():"0");     //判断是数据保存还是公式设置
//	        			data.put("data","0");
	        		}else if(jsonMap.get("data")!=null){
	        			data.put("data",jsonMap.get("data").toString());
	        		}
	        		if(data.get("data")==null){
	        			data.put("data","");
	        		}
	        		meteData.add(data);
	        	}
	        }
		  List<Colitem> colList=fjmapper.getFjColitemList(suitId, modalsheetId,modaltypeId);
		  for(int i=0;i<meteData.size();i++){
			  Map<String, String> row=meteData.get(i);
			  for (Colitem col : colList) {
			 	if(Integer.parseInt(row.get("y"))==col.getCOL()){
			 		row.put("D",col.getDATA_COL());
			 	}
			  }
		  }
		return meteData;
	}

	
	/**
	 * 浮动行数据采集
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,noRollbackFor={Exception.class})
	public String collectFjData(Connection conn,String suitId, String modalsheetId,String modaltypeId, String rowItemId, String entity_id,String periodId,
			String currencyId,String rowItemCode,String entity_code,String periodCode,String currencyCode,String userId,
			String ledgerId,String cnyCode)throws Exception {
		String flag="0";
		
		List<Map<String, String>>recs=selFjCalFormula(suitId, modalsheetId, modaltypeId, rowItemId, entity_id); //浮动行单位级公式查询
		List<Colitem> colList=fjmapper.getFjColitemList(suitId, modalsheetId,modaltypeId);   //获取DATA与列指标的对应
		for (Map<String, String> map : recs) {
			for (Colitem col : colList) {
				String col_val=map.get(col.getDATA_COL());
				if(col_val!=null&&!"".equals(col_val)&&!"0".equals(col_val)){
					//调用运算函数，获取运算结果
					String equation=cellFormulaService.getArithmetic(col_val, periodId, entity_id,suitId,currencyId,ledgerId,cnyCode);  
					//将函数运算串，生成最终计算结果值
					String dataV=FormulaTranslate.evalFormula(equation);
					map.put(col.getDATA_COL(), dataV);
					flag="1";
				}
//				map.put(col.getCOL().toString(),map.get(col.getDATA_COL()));
//				map.remove(col.getDATA_COL());
			}
		}
		if("1".equals(flag)){
			int cnt=fjmapper.delFjData(suitId, entity_id, modalsheetId, rowItemId, periodId, currencyId);  //删除原数据
			String keys="";
			String val="";
			for(int i=0;i<colList.size();i++){
				Colitem col=colList.get(i);
				keys=keys+col.getDATA_COL()+",";
			}
			if (keys.lastIndexOf(",") == keys.length() - 1) {
				keys = keys.substring(0, keys.length() - 1);
	        }
			for(int i2=0;i2<keys.split(",").length;i2++){
				val+="?,";
			}
			if (val.lastIndexOf(",") == val.length() - 1) {
				val = val.substring(0, val.length() - 1);
	        }
			
			PreparedStatement pst = null;
			StringBuffer sql=new StringBuffer();
			sql.append("INSERT INTO xsr_rep_fj_cellvalue(FJ_CELLV_ID,SUIT_ID,ENTITY_ID,PERIOD_ID,CURRENCY_ID,MODALSHEET_ID,ROWITEM_ID,ENTITY_CODE,PERIOD_CODE,CURRENCY_CODE,ROWITEM_CODE,ROWNUMDTL,SAVEFLAG,SUBENTITY_ID,LAST_UPDATE_DATE,LAST_UPDATED_BY,CREATION_DATE,CREATED_BY,").
				append(keys).append(")VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,").append(val).append(")");
			try{
				pst=conn.prepareStatement(sql.toString());
				for(int i3=1;i3<=recs.size();i3++){
					Map<String, String> rec=recs.get(i3-1);
					pst.setString(1, UUID.randomUUID().toString());
					pst.setString(2, suitId);
					pst.setString(3, entity_id);
					pst.setString(4, periodId);
					pst.setString(5, currencyId);
					pst.setString(6, modalsheetId);
					pst.setString(7, rowItemId);
					pst.setString(8, entity_code);
					pst.setString(9, periodCode);
					pst.setString(10, currencyCode);
					pst.setString(11, rowItemCode);
					pst.setInt(12, Integer.parseInt(rec.get("ROWNUMDTL")));
					pst.setInt(13, 1);
					pst.setString(14, "");
					pst.setString(15,new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
					pst.setString(16, userId);
					pst.setString(17,new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
					pst.setString(18, userId);
					for(int i4=1;i4<=colList.size();i4++){
						pst.setString(i4+18, rec.get(colList.get(i4-1).getDATA_COL()));
					}
					pst.addBatch();
					
					if(i3%1000==0){
						pst.executeBatch();
					}
					
				}
				pst.executeBatch();
			}catch(SQLException e){
				throw e;
			}finally{
				if(pst!=null){
					pst.close();
				}
//				SpringDBHelp.releaseConnection(conn); 
			}
		}
		return flag;
	}
	/**
	 * 报表浮动行数据保存
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,noRollbackFor={Exception.class})
	public String saveFjCellData(String modaltypeId, String modalsheetId,String entity_id, String titleMaxRow, String rowItemId,
			String sheetJson, String startCol, String suitId, String userId,String periodId,String currencyId,String rowItemCode)throws Exception {
		int cnt=fjmapper.delFjData(suitId, entity_id, modalsheetId, rowItemId, periodId, currencyId);   //删除原数据
		//根据id获取code值
		 String entityCode=null;
		 String periodCode=null;
		 String currencyCode=null;
		 String modalsheetCode=null; 
		HashMap<String, String> params = new HashMap<String, String>();
		 params.put("MODALSHEET_ID", modalsheetId);
		 params.put("ENTITY_ID", entity_id);
		 params.put("PERIOD_ID", periodId);
		 params.put("CURRENCY_ID", currencyId);
		List<HashMap> repCodesMap=makeReportMapper.getRepCodes(params);
			if (repCodesMap.size()>0) {
				 entityCode=repCodesMap.get(0).get("ENTITY_CODE").toString();
				 periodCode = repCodesMap.get(0).get("PERIOD_CODE").toString();
				 currencyCode=repCodesMap.get(0).get("CURRENCY_CODE").toString();
				 modalsheetCode=repCodesMap.get(0).get("MODALSHEET_CODE").toString();
			}
		//构造保存需要的数据记录格式	
		List<Map<String, String>>sqlList=new ArrayList<Map<String,String>>();
		List<Map<String, String>>datasource=sheetJsonToData(modaltypeId,sheetJson, suitId, modalsheetId, titleMaxRow,"FjData");
		int maxRow=0;
		for (Map<String, String> map : datasource) {
			if(Integer.parseInt(map.get("x"))>maxRow){
				maxRow=Integer.parseInt(map.get("x"));
			}
		}
		for (int i=Integer.parseInt(titleMaxRow)+1;i<=maxRow;i++) {
			Map<String, String> sqlMap=new HashMap<String, String>();
			sqlMap.put("FJ_CELLV_ID",UUID.randomUUID().toString());
			sqlMap.put("ROWNUMDTL", (i-Integer.parseInt(titleMaxRow))+"");
//			sqlMap.put("ISFM", "N");
			for (Map<String, String> map : datasource) {
				if(Integer.parseInt(map.get("x"))==i){
					sqlMap.put(map.get("D"), map.get("data"));
				}
			}
			sqlList.add(sqlMap);
		}
		//保存sqlList中的数据（浮动行数据保存）
		List<Colitem> colList=fjmapper.getFjColitemList(suitId, modalsheetId,modaltypeId);
		String keys="";
		String val="";
		for(int i=0;i<colList.size();i++){
			Colitem col=colList.get(i);
			keys=keys+col.getDATA_COL()+",";
		}
		if (keys.lastIndexOf(",") == keys.length() - 1) {
			keys = keys.substring(0, keys.length() - 1);
        }
		for(int i2=0;i2<keys.split(",").length;i2++){
			val+="?,";
		}
		if (val.lastIndexOf(",") == val.length() - 1) {
			val = val.substring(0, val.length() - 1);
        }
		
		Connection conn = SpringDBHelp.getConnection();
		PreparedStatement pst = null;
		StringBuffer sql=new StringBuffer();
		sql.append("INSERT INTO xsr_rep_fj_cellvalue(FJ_CELLV_ID,SUIT_ID,ENTITY_ID,PERIOD_ID,CURRENCY_ID,MODALSHEET_ID,ROWITEM_ID,ENTITY_CODE,PERIOD_CODE,CURRENCY_CODE,ROWITEM_CODE,ROWNUMDTL,SAVEFLAG,SUBENTITY_ID,LAST_UPDATE_DATE,LAST_UPDATED_BY,CREATION_DATE,CREATED_BY,").
			append(keys).append(")VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,").append(val).append(")");
		try{
			pst=conn.prepareStatement(sql.toString());
			for(int i3=1;i3<=sqlList.size();i3++){
				Map<String, String> rec=sqlList.get(i3-1);
				pst.setString(1, rec.get("FJ_CELLV_ID"));
				pst.setString(2, suitId);
				pst.setString(3, entity_id);
				pst.setString(4, periodId);
				pst.setString(5, currencyId);
				pst.setString(6, modalsheetId);
				pst.setString(7, rowItemId);
				pst.setString(8, entityCode);
				pst.setString(9, periodCode);
				pst.setString(10, currencyCode);
				pst.setString(11, rowItemCode);
				pst.setInt(12, Integer.parseInt(rec.get("ROWNUMDTL")));
				pst.setInt(13, 1);
				pst.setString(14, "");
				pst.setString(15,new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				pst.setString(16, userId);
				pst.setString(17,new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				pst.setString(18, userId);
				for(int i4=1;i4<=colList.size();i4++){
					pst.setString(i4+18, rec.get(colList.get(i4-1).getDATA_COL()));
				}
				pst.addBatch();
				
				if(i3%1000==0){
					pst.executeBatch();
				}
				
			}
			pst.executeBatch();
		}catch(SQLException e){
			throw e;
		}finally{
			if(pst!=null){
				pst.close();
			}
			SpringDBHelp.releaseConnection(conn);
		}
		return null;
	}
	/**
	 * 报表浮动行数据保存(临时表)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,noRollbackFor={Exception.class})
	public String saveFjCellDataToTemp(String modaltypeId, String modalsheetId,String entity_id, String titleMaxRow, String rowItemId,
			String sheetJson, String startCol, String suitId, String userId,String periodId,String currencyId,String rowItemCode)throws Exception {
		int cnt=fjmapper.delFjDataTemp(suitId, entity_id, modalsheetId, rowItemId, periodId, currencyId,userId);   //删除临时表中的原数据
		//根据id获取code值
		 String entityCode=null;
		 String periodCode=null;
		 String currencyCode=null;
		 String modalsheetCode=null; 
		HashMap<String, String> params = new HashMap<String, String>();
		 params.put("MODALSHEET_ID", modalsheetId);
		 params.put("ENTITY_ID", entity_id);
		 params.put("PERIOD_ID", periodId);
		 params.put("CURRENCY_ID", currencyId);
		List<HashMap> repCodesMap=makeReportMapper.getRepCodes(params);
			if (repCodesMap.size()>0) {
				 entityCode=repCodesMap.get(0).get("ENTITY_CODE").toString();
				 periodCode = repCodesMap.get(0).get("PERIOD_CODE").toString();
				 currencyCode=repCodesMap.get(0).get("CURRENCY_CODE").toString();
				 modalsheetCode=repCodesMap.get(0).get("MODALSHEET_CODE").toString();
			}
		//构造保存需要的数据记录格式	
		List<Map<String, String>>sqlList=new ArrayList<Map<String,String>>();
		List<Map<String, String>>datasource=sheetJsonToData(modaltypeId,sheetJson, suitId, modalsheetId, titleMaxRow,"FjData");
		int maxRow=0;
		for (Map<String, String> map : datasource) {
			if(Integer.parseInt(map.get("x"))>maxRow){
				maxRow=Integer.parseInt(map.get("x"));
			}
		}
		Pattern p = Pattern.compile("^-?0{1}[.]?0*$");
		for (int i=Integer.parseInt(titleMaxRow)+1;i<=maxRow;i++) {
			boolean flag=false;
			Map<String, String> sqlMap=new HashMap<String, String>();
			for (Map<String, String> map : datasource) {
				if(Integer.parseInt(map.get("x"))==i){
					sqlMap.put(map.get("D"), map.get("data"));
					Matcher m=p.matcher(map.get("data"));
					if(map.get("data")!=null&&!"".equals(map.get("data"))&&!m.matches()){
						flag=true;
					}
				}
			}
			if(flag){
				sqlMap.put("FJ_CELLV_TEMP_ID",UUID.randomUUID().toString());
//				sqlMap.put("ROWNUMDTL", (i-Integer.parseInt(titleMaxRow))+"");
//				sqlMap.put("ISFM", "N");
				sqlList.add(sqlMap);
			}
		}
		//保存sqlList中的数据（浮动行数据保存）
		List<Colitem> colList=fjmapper.getFjColitemList(suitId, modalsheetId,modaltypeId);
		String keys="";
		String val="";
		for(int i=0;i<colList.size();i++){
			Colitem col=colList.get(i);
			keys=keys+col.getDATA_COL()+",";
		}
		if (keys.lastIndexOf(",") == keys.length() - 1) {
			keys = keys.substring(0, keys.length() - 1);
        }
		for(int i2=0;i2<keys.split(",").length;i2++){
			val+="?,";
		}
		if (val.lastIndexOf(",") == val.length() - 1) {
			val = val.substring(0, val.length() - 1);
        }
		
		Connection conn = SpringDBHelp.getConnection();
		PreparedStatement pst = null;
		StringBuffer sql=new StringBuffer();
		sql.append("INSERT INTO xsr_rep_fj_cellvalue_temp(FJ_CELLV_TEMP_ID,USER_ID,SUIT_ID,ENTITY_ID,PERIOD_ID,CURRENCY_ID,MODALSHEET_ID,ROWITEM_ID,ENTITY_CODE,PERIOD_CODE,CURRENCY_CODE,ROWITEM_CODE,ROWNUMDTL,SAVEFLAG,SUBENTITY_ID,LAST_UPDATE_DATE,LAST_UPDATED_BY,CREATION_DATE,CREATED_BY,").
			append(keys).append(")VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,").append(val).append(")");
		try{
			pst=conn.prepareStatement(sql.toString());
			for(int i3=1;i3<=sqlList.size();i3++){
				Map<String, String> rec=sqlList.get(i3-1);
				pst.setString(1, rec.get("FJ_CELLV_TEMP_ID"));
				pst.setString(2, userId);
				pst.setString(3, suitId);
				pst.setString(4, entity_id);
				pst.setString(5, periodId);
				pst.setString(6, currencyId);
				pst.setString(7, modalsheetId);
				pst.setString(8, rowItemId);
				pst.setString(9, entityCode);
				pst.setString(10, periodCode);
				pst.setString(11, currencyCode);
				pst.setString(12, rowItemCode);
//				pst.setInt(13, Integer.parseInt(rec.get("ROWNUMDTL")));
				pst.setInt(13, i3);
				pst.setInt(14, 0);
				pst.setString(15, "");
				pst.setString(16,new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				pst.setString(17, userId);
				pst.setString(18,new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				pst.setString(19, userId);
				for(int i4=1;i4<=colList.size();i4++){
					pst.setString(i4+19, rec.get(colList.get(i4-1).getDATA_COL()));
				}
				pst.addBatch();
				
				if(i3%1000==0){
					pst.executeBatch();
				}
				
			}
			pst.executeBatch();
		}catch(SQLException e){
			throw e;
		}finally{
			if(pst!=null){
				pst.close();
			}
			SpringDBHelp.releaseConnection(conn);
		}
		return null;
	}
	/**
	 * 对浮动行明细数据进行汇总
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,noRollbackFor={Exception.class})
	public List<Map<String, String>> sumFjDataTemp(String userId, String suitId,String modalsheetId, String modaltypeId, String rowItemId, String entityId,
			String periodId, String currencyId) throws Exception {
		List<Colitem> colList=fjmapper.getFjColitemListOnly3(suitId, modalsheetId,modaltypeId);   //获取datatype=3的DATA与列指标的对应
		Connection conn = SpringDBHelp.getConnection();
		PreparedStatement pst = null;
		StringBuffer sql=new StringBuffer();
		String keys="";
		for(int i=0;i<colList.size();i++){
			Colitem col=colList.get(i);
			keys=keys+"sum("+col.getDATA_COL()+") '"+col.getDATA_COL()+"',";
		}
		if (keys.lastIndexOf(",") == keys.length() - 1) {
			keys = keys.substring(0, keys.length() - 1);
        }
		sql.append("SELECT ").append(keys).append(" FROM xsr_rep_fj_cellvalue_temp WHERE SUIT_ID=? AND ENTITY_ID=? AND MODALSHEET_ID=? AND ROWITEM_ID=? AND PERIOD_ID=? AND CURRENCY_ID=? AND USER_ID=?");
		List<Map<String, String>> recs=new ArrayList<Map<String, String>>();
		try{
			pst=conn.prepareStatement(sql.toString());
			pst.setString(1,suitId);
			pst.setString(2,entityId);
			pst.setString(3,modalsheetId);
			pst.setString(4,rowItemId);
			pst.setString(5,periodId);
			pst.setString(6,currencyId);
			pst.setString(7,userId);
			ResultSet rs=pst.executeQuery();
			while(rs.next()){
				for (Colitem col : colList) {
					Map<String, String> rec=new HashMap<String, String>();
					rec.put("DATAV",rs.getString(col.getDATA_COL()));
					rec.put("COL",col.getCOL().toString());
					recs.add(rec);
				}
				
			}
		}catch(SQLException e){
			throw e;
		}finally{
			if(pst!=null){
				pst.close();
			}
			SpringDBHelp.releaseConnection(conn); 
		}
		return recs;
	}
	/**
	 *打开报表，同时将浮动行明细数据插入临时表 
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,noRollbackFor={Exception.class})
	public String instFjDataToTemp(String userId, String suitId,String modalsheetId, String modaltypeId, String entityId,
			String periodId, String currencyId) throws Exception {
		//1、判断当前模版有无浮动行
		int count=fjmapper.isExistFjItem(suitId, modalsheetId, modaltypeId);
		if(count<=0){return null;}
		//2、判断浮动行有无明细数据
		List<Map<String, String>> recs=selRepFjDataAll(suitId, modalsheetId, modaltypeId, entityId, periodId, currencyId);
		if(recs==null || recs.size()<=0){return null;}
		//3、删除临时表原有数据
		fjmapper.delFjDataTempSingle(suitId, entityId, modalsheetId, periodId, currencyId, userId);     
		//3、判断当前模版浮动行数据是否已插入临时表
//		int count2=fjmapper.isExistTempFjData(userId, suitId, modalsheetId, entityId, periodId, currencyId);
//		if(count2>0){return null;}
		//4、向临时表中插入当前模版下的所有浮动行数据
		//根据id获取code值
		 String entityCode=null;
		 String periodCode=null;
		 String currencyCode=null;
		 String modalsheetCode=null; 
		HashMap<String, String> params = new HashMap<String, String>();
		 params.put("MODALSHEET_ID", modalsheetId);
		 params.put("ENTITY_ID", entityId);
		 params.put("PERIOD_ID", periodId);
		 params.put("CURRENCY_ID", currencyId);
		List<HashMap> repCodesMap=makeReportMapper.getRepCodes(params);
			if (repCodesMap.size()>0) {
				 entityCode=repCodesMap.get(0).get("ENTITY_CODE").toString();
				 periodCode = repCodesMap.get(0).get("PERIOD_CODE").toString();
				 currencyCode=repCodesMap.get(0).get("CURRENCY_CODE").toString();
				 modalsheetCode=repCodesMap.get(0).get("MODALSHEET_CODE").toString();
			}
			
			List<Colitem> colList=fjmapper.getFjColitemList(suitId, modalsheetId,modaltypeId);
			String keys="";
			String val="";
			for(int i=0;i<colList.size();i++){
				Colitem col=colList.get(i);
				keys=keys+col.getDATA_COL()+",";
			}
			if (keys.lastIndexOf(",") == keys.length() - 1) {
				keys = keys.substring(0, keys.length() - 1);
	        }
			for(int i2=0;i2<keys.split(",").length;i2++){
				val+="?,";
			}
			if (val.lastIndexOf(",") == val.length() - 1) {
				val = val.substring(0, val.length() - 1);
	        }
			
			Connection conn = SpringDBHelp.getConnection();
			PreparedStatement pst = null;
			StringBuffer sql=new StringBuffer();
			sql.append("INSERT INTO xsr_rep_fj_cellvalue_temp(FJ_CELLV_TEMP_ID,USER_ID,SUIT_ID,ENTITY_ID,PERIOD_ID,CURRENCY_ID,MODALSHEET_ID,ROWITEM_ID,ENTITY_CODE,PERIOD_CODE,CURRENCY_CODE,ROWITEM_CODE,ROWNUMDTL,SAVEFLAG,SUBENTITY_ID,LAST_UPDATE_DATE,LAST_UPDATED_BY,CREATION_DATE,CREATED_BY,").
				append(keys).append(")VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,").append(val).append(")");
			try{
				pst=conn.prepareStatement(sql.toString());
				for(int i3=1;i3<=recs.size();i3++){
					Map<String, String> rec=recs.get(i3-1);
					pst.setString(1, UUID.randomUUID().toString());
					pst.setString(2, userId);
					pst.setString(3, suitId);
					pst.setString(4, entityId);
					pst.setString(5, periodId);
					pst.setString(6, currencyId);
					pst.setString(7, modalsheetId);
					pst.setString(8, rec.get("ROWITEM_ID"));
					pst.setString(9, entityCode);
					pst.setString(10, periodCode);
					pst.setString(11, currencyCode);
					pst.setString(12, rec.get("ROWITEM_CODE"));
					pst.setInt(13, Integer.parseInt(rec.get("ROWNUMDTL")));
					pst.setInt(14, 0);
					pst.setString(15, "");
					pst.setString(16,new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
					pst.setString(17, userId);
					pst.setString(18,new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
					pst.setString(19, userId);
					for(int i4=1;i4<=colList.size();i4++){
						pst.setString(i4+19, rec.get(colList.get(i4-1).getDATA_COL()));
					}
					pst.addBatch();
					
					if(i3%1000==0){
						pst.executeBatch();
					}
					
				}
				pst.executeBatch();
			}catch(SQLException e){
				throw e;
			}finally{
				if(pst!=null){
					pst.close();
				}
				SpringDBHelp.releaseConnection(conn);
			}	
			
		return null;
	}
	/**
	 * 主表数据保存时，将临时表数据同步到浮动行值正式表
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,noRollbackFor={Exception.class})
	public String instTempToFjData(String userId, String suitId,String modalsheetId, String modaltypeId, String entityId,
			String periodId, String currencyId) throws Exception {
		//根据id获取code值
		 String entityCode=null;
		 String periodCode=null;
		 String currencyCode=null;
		 String modalsheetCode=null; 
		HashMap<String, String> params = new HashMap<String, String>();
		 params.put("MODALSHEET_ID", modalsheetId);
		 params.put("ENTITY_ID", entityId);
		 params.put("PERIOD_ID", periodId);
		 params.put("CURRENCY_ID", currencyId);
		List<HashMap> repCodesMap=makeReportMapper.getRepCodes(params);
			if (repCodesMap.size()>0) {
				 entityCode=repCodesMap.get(0).get("ENTITY_CODE").toString();
				 periodCode = repCodesMap.get(0).get("PERIOD_CODE").toString();
				 currencyCode=repCodesMap.get(0).get("CURRENCY_CODE").toString();
				 modalsheetCode=repCodesMap.get(0).get("MODALSHEET_CODE").toString();
			}
		
		//删除浮动行值表原数据
		int cnt=fjmapper.delFjDataAll(suitId, entityId, modalsheetId, periodId, currencyId);
		//再保存数据
		List<Map<String, String>> recs=selRepFjDataTempAll(userId, suitId, modalsheetId, modaltypeId, entityId, periodId, currencyId);  //从临时表中获取数据
		List<Colitem> colList=fjmapper.getFjColitemList(suitId, modalsheetId,modaltypeId);
		if(colList.size()<=0){
			System.out.println("浮动行列映射信息查询失败-----");
			return null;
		}
		String keys="";
		String val="";
		for(int i=0;i<colList.size();i++){
			Colitem col=colList.get(i);
			keys=keys+col.getDATA_COL()+",";
		}
		if (keys.lastIndexOf(",") == keys.length() - 1) {
			keys = keys.substring(0, keys.length() - 1);
        }
		for(int i2=0;i2<keys.split(",").length;i2++){
			val+="?,";
		}
		if (val.lastIndexOf(",") == val.length() - 1) {
			val = val.substring(0, val.length() - 1);
        }
		
		Connection conn = SpringDBHelp.getConnection();
		PreparedStatement pst = null;
		StringBuffer sql=new StringBuffer();
		sql.append("INSERT INTO xsr_rep_fj_cellvalue(FJ_CELLV_ID,SUIT_ID,ENTITY_ID,PERIOD_ID,CURRENCY_ID,MODALSHEET_ID,ROWITEM_ID,ENTITY_CODE,PERIOD_CODE,CURRENCY_CODE,ROWITEM_CODE,ROWNUMDTL,SAVEFLAG,SUBENTITY_ID,LAST_UPDATE_DATE,LAST_UPDATED_BY,CREATION_DATE,CREATED_BY,").
			append(keys).append(")VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,").append(val).append(")");
		try{
			pst=conn.prepareStatement(sql.toString());
			for(int i3=1;i3<=recs.size();i3++){
				Map<String, String> rec=recs.get(i3-1);
				pst.setString(1, UUID.randomUUID().toString());
				pst.setString(2, suitId);
				pst.setString(3, entityId);
				pst.setString(4, periodId);
				pst.setString(5, currencyId);
				pst.setString(6, modalsheetId);
				pst.setString(7, rec.get("ROWITEM_ID"));
				pst.setString(8, entityCode);
				pst.setString(9, periodCode);
				pst.setString(10, currencyCode);
				pst.setString(11, rec.get("ROWITEM_CODE"));
				pst.setInt(12, Integer.parseInt(rec.get("ROWNUMDTL")));
				pst.setInt(13, 1);
				pst.setString(14, "");
				pst.setString(15,new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				pst.setString(16, userId);
				pst.setString(17,new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				pst.setString(18, userId);
				for(int i4=1;i4<=colList.size();i4++){
					pst.setString(i4+18, rec.get(colList.get(i4-1).getDATA_COL()));
				}
				pst.addBatch();
				
				if(i3%1000==0){
					pst.executeBatch();
				}
				
			}
			pst.executeBatch();
		}catch(SQLException e){
			throw e;
		}finally{
			if(pst!=null){
				pst.close();
			}
			SpringDBHelp.releaseConnection(conn);
		}
		return null;
	}
	
	/**
	 * 查询报表下所有浮动行数据
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,noRollbackFor={Exception.class})
	public List<Map<String, String>> selRepFjDataAll(String suitId,String modalsheetId,String modaltypeId,
			String entityId,String periodId,String currencyId)throws Exception{
		List<Colitem> colList=fjmapper.getFjColitemList(suitId, modalsheetId,modaltypeId);   //获取DATA与列指标的对应
		if(colList.size()<=0){
			System.out.println("查询列指标与DATA列的映射失败----");
			return null;
		}
		Connection conn = SpringDBHelp.getConnection();
		PreparedStatement pst = null;
		StringBuffer sql=new StringBuffer();
		String keys="";
		for(int i=0;i<colList.size();i++){
			Colitem col=colList.get(i);
			keys=keys+col.getDATA_COL()+",";
		}
		if (keys.lastIndexOf(",") == keys.length() - 1) {
			keys = keys.substring(0, keys.length() - 1);
        }
		sql.append("SELECT f.ROWITEM_ID,f.ROWITEM_CODE,"
				+ "(SELECT r.ROWNO FROM xsr_rep_rowmodalref r WHERE r.SUIT_ID=f.SUIT_ID AND r.MODALTYPE_ID=? AND r.MODALSHEET_ID=f.MODALSHEET_ID AND r.ROWITEM_ID=f.ROWITEM_ID) ROWNO,"
				+ "ROWNUMDTL,").append(keys).append(" FROM xsr_rep_fj_cellvalue f WHERE f.SUIT_ID=? AND f.ENTITY_ID=? AND f.MODALSHEET_ID=? AND f.PERIOD_ID=? AND f.CURRENCY_ID=?");
		List<Map<String, String>> recs=new ArrayList<Map<String, String>>();
		try{
			pst=conn.prepareStatement(sql.toString());
			pst.setString(1,modaltypeId);
			pst.setString(2,suitId);
			pst.setString(3,entityId);
			pst.setString(4,modalsheetId);
			pst.setString(5,periodId);
			pst.setString(6,currencyId);
			ResultSet rs=pst.executeQuery();
			while(rs.next()){
				Map<String, String> rec=new HashMap<String, String>();
				rec.put("ROWNUMDTL",rs.getString("ROWNUMDTL"));
				rec.put("ROWITEM_ID",rs.getString("ROWITEM_ID"));
				rec.put("ROWITEM_CODE",rs.getString("ROWITEM_CODE"));
				rec.put("ROWNO",rs.getString("ROWNO"));
				for (Colitem col : colList) {
					rec.put(col.getDATA_COL().toString(),rs.getString(col.getDATA_COL()));
				}
				recs.add(rec);
			}
		}catch(SQLException e){
			throw e;
		}finally{
			if(pst!=null){
				pst.close();
			}
			SpringDBHelp.releaseConnection(conn); 
		}
		return recs;
	}
	/**
	 * 查询报表浮动行的所有数据（临时表）
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,noRollbackFor={Exception.class})
	public List<Map<String, String>> selRepFjDataTempAll(String userId,String suitId,String modalsheetId,String modaltypeId,
			String entity_id,String periodId,String currencyId)throws Exception{
		List<Colitem> colList=fjmapper.getFjColitemList(suitId, modalsheetId,modaltypeId);   //获取DATA与列指标的对应
		if(colList.size()<=0){
			System.out.println("查询列指标与DATA列的映射失败----");
			return null;
		}
		Connection conn = SpringDBHelp.getConnection();
		PreparedStatement pst = null;
		StringBuffer sql=new StringBuffer();
		String keys="";
		for(int i=0;i<colList.size();i++){
			Colitem col=colList.get(i);
			keys=keys+col.getDATA_COL()+",";
		}
		if (keys.lastIndexOf(",") == keys.length() - 1) {
			keys = keys.substring(0, keys.length() - 1);
        }
		sql.append("SELECT ROWITEM_ID,ROWITEM_CODE,ROWNUMDTL,").append(keys).append(" FROM xsr_rep_fj_cellvalue_temp WHERE SUIT_ID=? AND ENTITY_ID=? AND MODALSHEET_ID=? AND USER_ID=? AND PERIOD_ID=? AND CURRENCY_ID=?");
		List<Map<String, String>> recs=new ArrayList<Map<String, String>>();
		try{
			pst=conn.prepareStatement(sql.toString());
			pst.setString(1,suitId);
			pst.setString(2,entity_id);
			pst.setString(3,modalsheetId);
			pst.setString(4,userId);
			pst.setString(5,periodId);
			pst.setString(6,currencyId);
			ResultSet rs=pst.executeQuery();
			while(rs.next()){
				Map<String, String> rec=new HashMap<String, String>();
				rec.put("ROWNUMDTL",rs.getString("ROWNUMDTL"));
				rec.put("ROWITEM_ID",rs.getString("ROWITEM_ID"));
				rec.put("ROWITEM_CODE",rs.getString("ROWITEM_CODE"));
				for (Colitem col : colList) {
					rec.put(col.getDATA_COL().toString(),rs.getString(col.getDATA_COL()));
				}
				recs.add(rec);
			}
		}catch(SQLException e){
			throw e;
		}finally{
			if(pst!=null){
				pst.close();
			}
			SpringDBHelp.releaseConnection(conn); 
		}
		return recs;
	}
	/**
	 *通过列编码获取列id 
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,noRollbackFor={Exception.class})
	public Map<String, String> getColitemIdByCode(Map<String, String> param) throws Exception{
		List<Map<String, String>> result=fjmapper.getColitemIdByCode(param);
		return result.get(0);
	}
	/**
	 * 查询报表浮动行数据
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,noRollbackFor={Exception.class})
	public List<Map<String, String>> selFjData(String suitId,String modalsheetId,String modaltypeId,
			String rowItemId,String entity_id,String periodId,String currencyId)throws Exception{
		List<Colitem> colList=fjmapper.getFjColitemList(suitId, modalsheetId,modaltypeId);   //获取DATA与列指标的对应
		Connection conn = SpringDBHelp.getConnection();
		PreparedStatement pst = null;
		StringBuffer sql=new StringBuffer();
		String keys="";
		for(int i=0;i<colList.size();i++){
			Colitem col=colList.get(i);
			keys=keys+col.getDATA_COL()+",";
		}
		if (keys.lastIndexOf(",") == keys.length() - 1) {
			keys = keys.substring(0, keys.length() - 1);
        }
		sql.append("SELECT ROWNUMDTL,").append(keys).append(" FROM xsr_rep_fj_cellvalue WHERE SUIT_ID=? AND ENTITY_ID=? AND MODALSHEET_ID=? AND ROWITEM_ID=? AND PERIOD_ID=? AND CURRENCY_ID=?");
		List<Map<String, String>> recs=new ArrayList<Map<String, String>>();
		try{
			pst=conn.prepareStatement(sql.toString());
			pst.setString(1,suitId);
			pst.setString(2,entity_id);
			pst.setString(3,modalsheetId);
			pst.setString(4,rowItemId);
			pst.setString(5,periodId);
			pst.setString(6,currencyId);
			ResultSet rs=pst.executeQuery();
			while(rs.next()){
				Map<String, String> rec=new HashMap<String, String>();
				rec.put("ROWNUMDTL",rs.getString("ROWNUMDTL"));
				for (Colitem col : colList) {
					rec.put(col.getDATA_COL().toString(),rs.getString(col.getDATA_COL()));
				}
				recs.add(rec);
			}
		}catch(SQLException e){
			throw e;
		}finally{
			if(pst!=null){
				pst.close();
			}
			SpringDBHelp.releaseConnection(conn); 
		}
		return recs;
	}
	/**
	 * 查询报表浮动行数据
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,noRollbackFor={Exception.class})
	public Map<String, Object> repQryFjCellData(String suitId,String modalsheetId,String modaltypeId,String rowItemId,String entity_id,
			String msFormatId,String modalsheetName,String titleMaxRow,String rowItemCode,String periodId,String currencyId)throws Exception {
		List<ModalSheet> sheetList = new ArrayList<ModalSheet>();
		ModalSheet ms = new ModalSheet(Integer.parseInt(msFormatId),modalsheetName);
		sheetList.add(ms);
		List<ModalSheetFormat> newcellList =new ArrayList<ModalSheetFormat>();
		List<ModalSheetFormat> final_cellList =new ArrayList<ModalSheetFormat>();
		ModalSheetFormat modalsf=null;
		List<ModalSheetFormat> cellList = fjmapper.getFjModalsheetFormatCellList(msFormatId,titleMaxRow,rowItemCode);   //获取表头和浮动行的模版格式
		String rowno_col="";     //行次所在物理列位置
		//将浮动行复制10行
		for (ModalSheetFormat msf : cellList) {
			if(msf.getCell_comment()!=null&&msf.getCell_comment().equals("rowno")){
				rowno_col=msf.getCol().toString();
			}
			if(msf.getRow()>Integer.parseInt(titleMaxRow)){
				msf.setRow(Integer.parseInt(titleMaxRow)+1);
				String content=msf.getJson();
				Map<String, Object> jsonMap = JsonUtil.getJsonObj(content);
				if(jsonMap.get("cal")!=null&&jsonMap.get("cal").toString().equals("true")){
					jsonMap.remove("cal");
					jsonMap.remove("value");
				}
				jsonMap.put("data","");
				msf.setJson(JsonUtil.toJson(jsonMap));
				msf.setCal(false);
				modalsf=new ModalSheetFormat(msf.getFormatId(),msf.getSheet(),msf.getRow(),msf.getCol(),msf.getJson(),msf.getCal(),msf.getCreationDate(),msf.getCreatedBy(),msf.getCell_comment(),msf.getCell_comment_type());
				final_cellList.add(modalsf);
				for(int i=1;i<=9;i++){
					modalsf=new ModalSheetFormat(msf.getFormatId(),msf.getSheet(),msf.getRow(),msf.getCol(),msf.getJson(),msf.getCal(),msf.getCreationDate(),msf.getCreatedBy(),msf.getCell_comment(),msf.getCell_comment_type());
					modalsf.setRow(modalsf.getRow()+i);
					newcellList.add(modalsf);
				}
			}
		}
		for (ModalSheetFormat nmsf : newcellList) {
			cellList.add(nmsf);
		}
		//加载模版数据
		List<Map<String, String>>recs=new ArrayList<Map<String,String>>();
		recs=selFjData(suitId, modalsheetId, modaltypeId, rowItemId, entity_id, periodId, currencyId);  //浮动行数据值
		  //将map数据中的DATA列转换成与之对应的物理列位置y
		List<Colitem> colList=fjmapper.getFjColitemList(suitId, modalsheetId,modaltypeId);
		for (Map<String, String> map : recs) {
			for (Colitem col : colList) {
				map.put(col.getCOL().toString(),map.get(col.getDATA_COL()));
				map.remove(col.getDATA_COL());
			}
		}
		  //
		for (Map<String, String> rec : recs) {
			String rownumdtl=rec.get("ROWNUMDTL");
			if(Integer.parseInt(rownumdtl)+Integer.parseInt(titleMaxRow)>Integer.parseInt(titleMaxRow)+10){
				for (ModalSheetFormat msf : final_cellList) {
					modalsf=new ModalSheetFormat(msf.getFormatId(),msf.getSheet(),msf.getRow(),msf.getCol(),msf.getJson(),msf.getCal(),msf.getCreationDate(),msf.getCreatedBy(),msf.getCell_comment(),msf.getCell_comment_type());
					modalsf.setRow(Integer.parseInt(rownumdtl)+Integer.parseInt(titleMaxRow));
					String content=modalsf.getJson();
					Map<String, Object> jsonMap = JsonUtil.getJsonObj(content);
					if(modalsf.getCol()==Integer.parseInt(rowno_col)){
						jsonMap.put("data",rownumdtl);
					}else if(rec.get(modalsf.getCol().toString())!=null){
						if(jsonMap.get("cal")!=null&&jsonMap.get("cal").toString().equals("true")){
							jsonMap.put("value",rec.get(modalsf.getCol().toString()));
		        		}else{
		        			jsonMap.put("data",rec.get(modalsf.getCol().toString()));
		        		}
					}
					modalsf.setJson(JsonUtil.toJson(jsonMap));
					cellList.add(modalsf);
				}
				
			}else{
				for (ModalSheetFormat msf : cellList) {
					if(Integer.parseInt(rownumdtl)+Integer.parseInt(titleMaxRow)==msf.getRow()){
						String content=msf.getJson();
						Map<String, Object> jsonMap = JsonUtil.getJsonObj(content);
						if(msf.getCol()==Integer.parseInt(rowno_col)){
							jsonMap.put("data",rownumdtl);
						}else if(rec.get(msf.getCol().toString())!=null){
							if(jsonMap.get("cal")!=null&&jsonMap.get("cal").toString().equals("true")){
								jsonMap.put("value",rec.get(msf.getCol().toString()));
			        		}else{
			        			jsonMap.put("data",rec.get(msf.getCol().toString()));
			        		}
							
						}
						msf.setJson(JsonUtil.toJson(jsonMap));
//						System.out.println("1");
					} 
				}
				
			}
			
		}
		List<ModalSheetElement> elementList = fjmapper.getFjModalsheetElementList(msFormatId);
		for (int i=0;i<elementList.size();i++) {
			ModalSheetElement mse=elementList.get(i);
			String type=mse.getFtype();
			String content=mse.getJson();
			if("meg".equals(type)){
				content=content.replace("[", "").replace("]", "");
				String[] cont=content.split(",");
				if(Integer.parseInt(cont[0])>Integer.parseInt(titleMaxRow)){
					elementList.remove(i);
				}
			}
			
		}
		Map<String, Object> results = Maps.newHashMap();
		results.put("fileName", modalsheetName);
		results.put("sheets", sheetList);
		results.put("cells", cellList);
		results.put("floatings", elementList);
		
		return results;
	}
	
	/**
	 * 删除临时表数据（多模版）
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,noRollbackFor={Exception.class})
	public String delFjDataTempAll(Map<String, Object> param) throws Exception {
		int cnt=fjmapper.delFjDataTempAll(param);
		return null;
	}
	/**
	 * 报表数据上报（浮动行）
	 */
	@Override
	public List<Map<String, String>> fjDataUpload(String suitId,String modalsheetId, String modaltypeId, String entityId,
			String periodId, String currencyId) throws Exception {
		
		return selRepFjDataAll(suitId, modalsheetId, modaltypeId, entityId, periodId, currencyId);
	}

	
	

	
	
	
	

}
