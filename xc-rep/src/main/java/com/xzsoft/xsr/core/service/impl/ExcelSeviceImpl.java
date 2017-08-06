package com.xzsoft.xsr.core.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xzsoft.xip.framework.util.SpringDBHelp;
import com.xzsoft.xsr.core.mapper.ExcelMapper;
import com.xzsoft.xsr.core.modal.CellValue;
import com.xzsoft.xsr.core.modal.Colitem;
import com.xzsoft.xsr.core.service.ExcelSevice;
import com.xzsoft.xsr.core.util.XSRUtil;

@Service
public class ExcelSeviceImpl implements ExcelSevice{
	
	@Autowired
	private ExcelMapper excelmapper;
	
	@Override
	public String exportDataWithMoreCorp(String suitId,
			String xlsFullName, String entityCodeList,String entityNameList,String periodCode,
			String currencyCode,String mdlSheetList,String sheetNamelist,String mdlType,
			String repNum, String userId,String mdlshtWithEntity)
			throws Exception {
		Connection conn = SpringDBHelp.getConnection();
		PreparedStatement pst = null;
		StringBuffer sql=new StringBuffer();
		String[] entityNames = entityNameList.split(",");
		String[] entityArray = entityCodeList.split(",");
		String[] modalSheetArray = mdlSheetList.split(",");
		String[] modalSheetNameArray = sheetNamelist.split(",");
		boolean osType = XSRUtil.getOS().toLowerCase().contains("windows");

		// DAT文件
		String xlsName = xlsFullName.substring (xlsFullName.lastIndexOf(File.separator)+1);
		// INI文件引用的DAT文件名
		String datFile = xlsName.toUpperCase().replace(".INI", ".DAT").replace("JI", "JS");

		// Ini头文件
		File f1 = new File(xlsFullName);
		BufferedWriter bw1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f1),"GBK"));
		
		String flagMessage = "";

		bw1.write("[上报说明]");
		if(osType){
			bw1.newLine();
		}else{
			bw1.write("\r");
			bw1.newLine();
		}
		bw1.write("版本=2.1合并2.0/v1.12");
		if(osType){
			bw1.newLine();
		}else{
			bw1.write("\r");
			bw1.newLine();
		}
		bw1.write("上报单位="+entityNames[0]);
		if(osType){
			bw1.newLine();
		}else{
			bw1.write("\r");
			bw1.newLine();
		}
		//添加上报单位统一编号（远光） by 刘文龙     2014-08-21
		bw1.write("上报单位统一编号="+entityCodeList);
		if(osType){
			bw1.newLine();
		}else{
			bw1.write("\r");
			bw1.newLine();
		}
		bw1.write("上报报表单位列表="+entityNameList);
		if(osType){
			bw1.newLine();
		}else{
			bw1.write("\r");
			bw1.newLine();
		}
		bw1.write("报表个数="+repNum);
		if(osType){
			bw1.newLine();
		}else{
			bw1.write("\r");
			bw1.newLine();
		}
		bw1.write("报表时段=月报");
		if(osType){
			bw1.newLine();
		}else{
			bw1.write("\r");
			bw1.newLine();
		}
		bw1.write("报表时间="+periodCode);
		if(osType){
			bw1.newLine();
		}else{
			bw1.write("\r");
			bw1.newLine();
		}
		bw1.write("数据内容=<元角分><折合>");
		if(osType){
			bw1.newLine();
		}else{
			bw1.write("\r");
			bw1.newLine();
		}
		bw1.write("生成上报时间="+new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		if(osType){
			bw1.newLine();
		}else{
			bw1.write("\r");
			bw1.newLine();
		}
		String userName=excelmapper.getUserName(userId);
		bw1.write("上报人="+userName);
		if(osType){
			bw1.newLine();
		}else{
			bw1.write("\r");
			bw1.newLine();
		}
		bw1.write("说明=");
		if(osType){
			bw1.newLine();
		}else{
			bw1.write("\r");
			bw1.newLine();
		}
		bw1.write("报表数据文件名="+datFile);
		if(osType){
			bw1.newLine();
		}else{
			bw1.write("\r");
			bw1.newLine();
		}
		bw1.flush();

		String datFullFile = xlsFullName.replace(".ini", ".DAT").replace(".INI", ".DAT").replace("JI", "JS");
		File f2 = new File(datFullFile);//Dat数据文件
		BufferedWriter bw2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f2),"GBK"));
		String ygMdlCode ="";
		String ygMdlName="";
		String[] argsDat = new String [7];
		try{
			int fixRow = 1;
			int floatRow = 0;
			for (int i=0; i<modalSheetArray.length; i++) {//循环模板列表
				ygMdlCode=modalSheetArray[i];
				ygMdlName=modalSheetNameArray[i];
	//************************************写入.ini报表头文件**************************************
		//固定行数据
		if(osType){
			bw1.newLine();
		}else{
			bw1.write("\r");
			bw1.newLine();
		}
		bw1.write("[报表"+(i+1)+"]");
		if(osType){
			bw1.newLine();
		}else{
			bw1.write("\r");
			bw1.newLine();
		}
		bw1.write("报表名称="+ygMdlName);
		if(osType){
			bw1.newLine();
		}else{
			bw1.write("\r");
			bw1.newLine();
		}
		bw1.write("报表代码="+ygMdlCode);
		if(osType){
			bw1.newLine();
		}else{
			bw1.write("\r");
			bw1.newLine();
		}
		bw1.write("类型=行数固定");
		if(osType){
			bw1.newLine();
		}else{
			bw1.write("\r");
			bw1.newLine();
		}
		bw1.write("本表数据起始行="+fixRow);
		if(osType){
			bw1.newLine();
		}else{
			bw1.write("\r");
			bw1.newLine();
		}
		//*****************************************写入.dat报表数据文件*********************************************
		bw2.write("报表名："+ygMdlName+" (固定报表)");//1
		if(osType){
			bw2.newLine();
		}else{
			bw2.write("\r");
			bw2.newLine();
		}
		String entity_name = "";
		for(int j=0; j<entityArray.length; j++ ) {//循环公司列表
			
			String mdlshtWithEntityTemp = "["+ygMdlCode+","+entityArray[j]+"]";
			int index = mdlshtWithEntity.indexOf(mdlshtWithEntityTemp);
			if(index != -1) {
				entity_name = entityNames[j];
				argsDat[0]=entityArray[j];
				argsDat[1]=mdlType;
				argsDat[2]=modalSheetArray[i];
				//bw2.write("==="+this.getEntityNameArray(entityArray[j]));//2
				bw2.write("==="+entity_name);
				if(osType){
					bw2.newLine();
				}else{
					bw2.write("\r");
					bw2.newLine();
				}
				fixRow+=1;//2
				//获取数据
				List<CellValue> cellValues=excelmapper.expYgRepData(suitId, argsDat[0], periodCode, currencyCode, argsDat[1], argsDat[2]);
				if(cellValues.size()<=0){
					return flagMessage =ygMdlName+"报表数据有问题,导出上报文件出错";
				}
				if(cellValues.size()>0){
					for(int z=0;z<cellValues.size();z++){
//						if(StringUtils.isEmpty(cellValues.get(z).getCELLV()+"") || StringUtils.isBlank(cellValues.get(z).getCELLV()+"")){
//							if(flagMessage.indexOf(ygMdlName) == -1){
//								flagMessage += ygMdlName + ",";
//							}
//							continue;
//						}
						bw2.write("0"+"\t"+cellValues.get(z).getROWNO()+"\t"+cellValues.get(z).getCOLNO()+"\t"+cellValues.get(z).getDATATYPE()+"\t"+XSRUtil.replaceTabEnter(cellValues.get(z).getCELLV()+""));
						if(osType){
							bw2.newLine();
						}else{
							bw2.write("\r");
							bw2.newLine();
						}
						fixRow+=1;//2+entityArray.length
				}
				}
			}
		}//循环公司列表 end
		// 补写.ini报表头文件
		bw1.write("本表数据终止行="+fixRow);
		if(osType){
			bw1.newLine();
		}else{
			bw1.write("\r");
			bw1.newLine();
		}
		//循环写入多家公司的附加行数据	
		floatRow = fixRow;
		floatRow+=1;//2+entityArray.length+1

		bw1.write("附加数据起始行="+floatRow);
		if(osType){
			bw1.newLine();
		}else{
			bw1.write("\r");
			bw1.newLine();
		}
		//开始写附加数据
		bw2.write("附加数据:");//附加数据应该是在floatRow+1的位置
		if(osType){
			bw2.newLine();
		}else{
			bw2.write("\r");
			bw2.newLine();
		}
		for(int j=0; j<entityArray.length; j++ ) {//循环公司列表
			String mdlshtWithEntityTemp = "["+ygMdlCode +","+entityArray[j]+"]";
			int index = mdlshtWithEntity.indexOf(mdlshtWithEntityTemp);
			if(index != -1) {
				entity_name = entityNames[j];
				argsDat[0]=entityArray[j];
				argsDat[1]=mdlType;
				argsDat[2]=modalSheetArray[i];
				//bw2.write("==="+this.getEntityNameArray(entityArray[j]));//2
				bw2.write("==="+entity_name);
				if(osType){
					bw2.newLine();
				}else{
					bw2.write("\r");
					bw2.newLine();
				}
				floatRow+=1;//2
				//开始查询浮动行数据信息
				String datacols="";
				//获取浮动行数据列DATA_COL、数据类型DATATYPE、列次号
				List<Colitem> colitems=excelmapper.getFjColitemList(suitId,modalSheetArray[i],mdlType);
				if(colitems.size()>0){
					for(int z=0;z<colitems.size();z++){
						datacols+=colitems.get(z).getDATA_COL()+",";
					}
					if (datacols.lastIndexOf(",") == datacols.length() - 1) {
						datacols = datacols.substring(0, datacols.length() - 1);
			        }
					sql.append("SELECT c.ROWITEM_ID,r.ROWNO,c.ROWNUMDTL,").append(datacols).append(" FROM xsr_rep_fj_cellvalue c,xsr_rep_rowmodalref r,xsr_rep_modalsheet m "
							+ " WHERE m.SUIT_ID=c.SUIT_ID " 
							+" AND m.SUIT_ID=r.SUIT_ID " 
							+" AND m.MODALSHEET_ID=c.MODALSHEET_ID " 
							+" AND m.MODALSHEET_ID=r.MODALSHEET_ID " 
							+" AND r.MODALTYPE_ID=m.MODALTYPE_ID " 
							+" AND c.ROWITEM_ID=r.ROWITEM_ID "
							+" AND m.SUIT_ID=? AND m.MODALSHEET_CODE=? AND m.MODALTYPE_CODE=?");
					//查询固定行的值相关信息
					List<Map<String, String>> celMapList=new ArrayList<Map<String, String>>();
					List<CellValue> cellValues=excelmapper.getCellValues(suitId, entityArray[j], periodCode, currencyCode);
					for(int z=0;z<cellValues.size();z++){
						Map<String, String> celMap=new HashMap<String, String>();
						celMap.put("ROWITEM_ID", cellValues.get(z).getROWITEM_ID());
						 celMap.put("COLITEM_ID", cellValues.get(z).getCOLITEM_ID());
						 celMap.put("CELLV", cellValues.get(z).getCELLV()+"");
						 celMap.put("CELLTEXTV", cellValues.get(z).getCELLTEXTV()+"");
						 celMapList.add(celMap);
					}
						pst=conn.prepareStatement(sql.toString());
						pst.setString(1,suitId);
						pst.setString(2,modalSheetArray[i]);
						pst.setString(3,mdlType);
						ResultSet rs=pst.executeQuery();
						while(rs.next()){
							String rowId=rs.getString("ROWITEM_ID");
							String rowNo=rs.getString("ROWNO");
							String rowNumDtl=rs.getString("ROWNUMDTL");
							for (Colitem col : colitems) {
								if(rs.getString(col.getDATA_COL())!=null&&!rs.getString(col.getDATA_COL()).equals("")){
									String colId=col.getCOLITEM_ID().toString();
									String colNo=col.getCOLNO().toString();
									String datatype=col.getDATATYPE().toString();
									String cellv="";
									String cellvRId="";
									String cellvCId="";
									for(int z=0;z<celMapList.size();z++){
										cellvRId=celMapList.get(z).get("ROWITEM_ID");
										cellvCId=celMapList.get(z).get("COLITEM_ID");
										if(cellvRId.equals(rowId)&&cellvCId.equals(colId)){
											if(datatype.equals("1")||datatype.equals("5")){
												cellv=celMapList.get(z).get("CELLTEXTV");
												if(cellv.equals(null)||cellv.equals("")){
													cellv="-";
												}
											}else{
												cellv=celMapList.get(z).get("CELLV");
												if(cellv.equals(null)||cellv.equals("")){
													cellv="0";
												}
											}
										}
									}
									bw2.write("0"+"\t"+rowNo+"\t"+"1"+"\t"+colNo+"\t"+rowNumDtl+"\t"+datatype+"\t"+XSRUtil.replaceTabEnter(cellv));
									if(osType){
										bw2.newLine();
									}else{
										bw2.write("\r");
										bw2.newLine();
									}
									floatRow+=1;
								}
									
							}
							
						}
						rs.close();
						pst.close();
				}
				
				}
				}
			//补写.ini报表头文件 附加数据填写完毕
			bw1.write("附加数据终止行="+floatRow);
			if(osType){
				bw1.newLine();
			}else{
				bw1.write("\r");
				bw1.newLine();
			}
		}//循环模板列表 end
		return flagMessage;
	}catch(Exception Ex){
				Ex.printStackTrace();
				throw new Exception();
				}finally{
					bw1.close();
					bw2.close();
					conn.close();
				}
	}
}
		
