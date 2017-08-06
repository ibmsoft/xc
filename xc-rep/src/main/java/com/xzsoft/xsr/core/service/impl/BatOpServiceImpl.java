package com.xzsoft.xsr.core.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.print.DocFlavor.STRING;

import org.apache.ibatis.javassist.expr.NewArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fr.report.script.function.SQL;
import com.fr.third.org.apache.poi.hssf.record.formula.functions.Var;
import com.fr.web.core.A.i;
import com.fr.web.core.A.r;
import com.xzsoft.xip.framework.util.SpringDBHelp;
import com.xzsoft.xsr.core.mapper.BatOpMapper;
import com.xzsoft.xsr.core.modal.CellValue;
import com.xzsoft.xsr.core.modal.Colitem;
import com.xzsoft.xsr.core.modal.Fraq;
import com.xzsoft.xsr.core.modal.Heard;
import com.xzsoft.xsr.core.modal.IMPDataBean;
import com.xzsoft.xsr.core.modal.ReceiveBean;
import com.xzsoft.xsr.core.modal.RepSheet;
import com.xzsoft.xsr.core.modal.Rowitem;
import com.xzsoft.xsr.core.service.BatOpService;
import com.xzsoft.xsr.core.util.FileUtil;
import com.xzsoft.xsr.core.util.ReadH;
import com.xzsoft.xsr.core.util.XSRUtil;

@Service
public class BatOpServiceImpl implements BatOpService{

	@Autowired
	private BatOpMapper batOpMapper;
	@Override
	public void exportDataWithMoreCorp(String suitId,
			String xlsFullName, String entityCodeList,String entityNameList,String periodCode,
			String currencyCode,String mdlSheetList,String sheetNamelist,String mdlType,
			String repNum, String userId,String mdlshtWithEntity)
			throws Exception {
		Connection conn = SpringDBHelp.getConnection();
		PreparedStatement pst = null;
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
		String userName=batOpMapper.getUserName(userId);
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
				fixRow = floatRow+1;
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
				List<CellValue> cellValues=batOpMapper.expYgRepData(suitId, argsDat[0], periodCode, currencyCode, argsDat[1], argsDat[2]);
//				if(cellValues.size()<=0){
//					return flagMessage =ygMdlName+"报表数据有问题,导出上报文件出错";
//				}
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
			StringBuffer sql=new StringBuffer();
			
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
				List<Colitem> colitems=batOpMapper.getFjColitemList(suitId,modalSheetArray[i],mdlType);
				//获取映射表中使用的datacol列
				List<Colitem> datacolList=batOpMapper.getFJDatacol();
				if(colitems.size()>0){
					for(int z=0;z<datacolList.size();z++){
						datacols+=datacolList.get(z).getDATA_COL()+",";
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
							+" AND m.SUIT_ID=? AND m.MODALSHEET_CODE=? AND m.MODALTYPE_CODE=? "
							+"AND c.ENTITY_CODE=? AND c.PERIOD_CODE=? AND c.CURRENCY_CODE=? ");
						pst=conn.prepareStatement(sql.toString());
						pst.setString(1,suitId);
						pst.setString(2,modalSheetArray[i]);
						pst.setString(3,mdlType);
						pst.setString(4,entityArray[j]);
						pst.setString(5,periodCode);
						pst.setString(6,currencyCode);
						ResultSet rs=pst.executeQuery();
						while(rs.next()){
							String rowNo=rs.getString("ROWNO");
							String rowNumDtl=rs.getString("ROWNUMDTL");
							 for (Colitem col : colitems) {
								String val=rs.getString(col.getDATA_COL());
								if(val!=null&&!val.equals("")&&!val.equals("0")){
									String colNo=col.getCOLNO().toString();
									String datatype=col.getDATATYPE().toString();
									bw2.write("0"+"\t"+rowNo+"\t"+"1"+"\t"+colNo+"\t"+rowNumDtl+"\t"+datatype+"\t"+val);
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
	}catch(Exception Ex){
				Ex.printStackTrace();
				throw new Exception();
				}finally{
					bw1.close();
					bw2.close();
					conn.close();
	}
}
	/**
	 * 数据解析，并插入数据信息
	 * @param userId
	 * @param suitId
	 * @param type
	 * @param filename
	 * @param bean
	 * @param xlsFullPath
	 * @param xlsFullName
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String parseAndAddImpData(String sessionId,String userId,String suitId,String type, String filename, String modaltypeId,String modaltypeCode, ReceiveBean bean,String xlsFullName)
			throws Exception {
		
		//开始执行解析
		ArrayList<IMPDataBean> dataList = new ArrayList<IMPDataBean>();
		String xlsPath = xlsFullName.substring(0,xlsFullName.lastIndexOf(File.separator));
		String dataFilePath = "";
		String isExistData="1";
		File f1 = new File(xlsFullName);
		BufferedReader iniBr = null;
		BufferedReader datBr = null;
		try {
			iniBr = new BufferedReader(new InputStreamReader(new FileInputStream(f1),"GBK"));
			ReadH rh = new ReadH();

			String line="";
			String row[] = null;

			String RPT_TYPE = "";
			String RPT_PRIOD = "";
			String PUBLISH_TIME = "";
			String PUBLISHER = "";
			while((line=iniBr.readLine())!=null){
				if(line.indexOf("报表数据文件名")!=-1){
					row = line.split("=");
					dataFilePath = xlsPath + File.separator + row[1];
					datBr = new BufferedReader(new InputStreamReader(new FileInputStream(dataFilePath),"GBK"));
					rh.setBufferedReader(datBr);
				}
				if(line.indexOf("报表时段=")!=-1){//报表时段
					row = line.split("=");
					RPT_TYPE = row[1];
				}
				if(line.indexOf("报表时间=")!=-1){//报表时间
					row = line.split("=");
					RPT_PRIOD = row[1];
				}
				if(line.indexOf("生成上报时间=")!=-1){//生成上报时间
					row = line.split("=");
					PUBLISH_TIME = row[1];
				}
				if(line.indexOf("上报人=")!=-1){//上报人
					row = line.split("=");
					PUBLISHER = row[1];
				}

				if(line.indexOf("报表名称=")!=-1){
					 Heard heard = new Heard();

					 row = line.split("=");
					 heard.setRPT_NAME(row[1]);

					 //读取报表代码
					 line = iniBr.readLine();
					 row = line.split("=");
					 heard.setRPT_CODE(row[1]);

					 // 读取报表类型
					 line = iniBr.readLine();

					 // 读取本表数据起始行
					 line = iniBr.readLine();
					 row = line.split("=");
					 heard.setDATASTARTIDX(Integer.parseInt(row[1]));

					 // 读取本表数据终止行
					 line = iniBr.readLine();
					 row = line.split("=");

					 heard.setDATAENDIDX(Integer.parseInt(row[1]));

					 // 读取附加数据起始行
					 line = iniBr.readLine();
					 row = line.split("=");
					 heard.setFJDATASTARTIDX(Integer.parseInt(row[1]));

					 // 读取附加数据终止行
					 line = iniBr.readLine();
					 row = line.split("=");
					 heard.setFJDATAENDIDX(Integer.parseInt(row[1]));

					 String curEntity = null;
					 while((line=rh.readLine())!=null){

						if("".equals(line.trim())){
							continue;
						}
						if(rh.getCurLine()>=heard.getDATASTARTIDX()&&rh.getCurLine()<=heard.getDATAENDIDX()){//读取主表数据
							if(line.indexOf("报表名")!=-1){

							}else if(line.indexOf("===")!=-1){//
								row = line.split("===");
								curEntity = row[1];
							}else{
								row = line.split("\t");
								// 根据远光数据文件格式，如果数组的长度不等于5,则数据不合法。
								if(row.length != 5){
									throw new Exception("【"+curEntity+"】-【"+heard.getRPT_NAME()+"】，第"+rh.getCurLine()+"行数据格式不正确，请确认DAT文件!");
								}
								IMPDataBean dataItem = new IMPDataBean();
								dataItem.setRPT_CODE(heard.getRPT_CODE());
								dataItem.setRPT_NAME(heard.getRPT_NAME());
								dataItem.setENTITY_NAME(curEntity);
								dataItem.setRPT_TYPE(RPT_TYPE);
								dataItem.setRPT_PRIOD(RPT_PRIOD);
								dataItem.setPUBLISHER(PUBLISHER);
								dataItem.setPUBLISH_TIME(PUBLISH_TIME);
								dataItem.setROW_NO(row[1]);
								dataItem.setCOL_NO(row[2]);
								dataItem.setDATA_TYPE(row[3]);
								dataItem.setCELLVALUE(row[4]);
								dataItem.setFLAG(1);
								dataItem.setLINENO(rh.getCurLine());
								
								dataItem.setSESSIONID(sessionId);
								dataItem.setMODALTYPE_ID(modaltypeId);
								dataItem.setMODALTYPE_CODE(modaltypeCode);
								dataItem.setSUIT_ID(suitId);
								dataItem.setUSER_ID(userId);

								dataList.add(dataItem);
							}
						}
						if(rh.getCurLine()>=heard.getFJDATASTARTIDX()&&rh.getCurLine()<=heard.getFJDATAENDIDX()){//读取附加数据
							if(line.indexOf("附加数据") != -1){

							}else if(line.indexOf("===")!=-1){
								 curEntity = line.replaceAll("===", "");
							 } else {
								 row=line.split("\t");
								 // 根据远光数据文件格式，如果数组的长度不等于7,则数据不合法。
								 if(row.length != 7){
									throw new Exception("【"+curEntity+"】-【"+heard.getRPT_NAME()+"】，第"+rh.getCurLine()+"行数据格式不正确，请确认DAT文件!");
								 }
								 row = line.split("\t");
								 IMPDataBean dataItem = new IMPDataBean();
								 dataItem.setRPT_CODE(heard.getRPT_CODE());
								 dataItem.setRPT_NAME(heard.getRPT_NAME());
								 dataItem.setENTITY_NAME(curEntity);
								 dataItem.setRPT_TYPE(RPT_TYPE);
								 dataItem.setRPT_PRIOD(RPT_PRIOD);
								 dataItem.setPUBLISHER(PUBLISHER);
								 dataItem.setPUBLISH_TIME(PUBLISH_TIME);
								 dataItem.setROW_NO(row[1]);
								 dataItem.setCOL_NO(row[3]);
								 dataItem.setNO(row[4]);
								 dataItem.setDATA_TYPE(row[5]);
								 dataItem.setCELLVALUE(row[6]);
								 dataItem.setFLAG(2);
								 dataItem.setLINENO(rh.getCurLine());
								 
								 dataItem.setSESSIONID(sessionId);
								 dataItem.setMODALTYPE_ID(modaltypeId);
								 dataItem.setMODALTYPE_CODE(modaltypeCode);
								 dataItem.setSUIT_ID(suitId);
								 dataItem.setUSER_ID(userId);

								 dataList.add(dataItem);
							}
						}

						if(rh.getCurLine()==heard.getFJDATAENDIDX()){//当前数据表读取完成，接着读下一个
							break;
						}
					 }
				}
			}
			//开始批量插入数据信息
			// 第一步：删除sessionid下的数据信息
			batOpMapper.delTempMsg(userId, sessionId);
			//第二步：批量插入数据信息
			if(dataList.size()>0){
				
				batOpMapper.batchInsertImpData(dataList);
			}else{
				isExistData="0";
			}
			return isExistData;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			if(datBr!=null)
				try {
					datBr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					if(iniBr!=null)
						try {
							iniBr.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
				}

		}
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void updateTempEntityData(ReceiveBean bean,String sessionId) throws Exception {
		//校验单位映射是否唯一
		batOpMapper.IsOneTempEntity(sessionId);
		//校验公司对照关系
		batOpMapper.IsExistTempEntity(sessionId);
		//同时更新公司的id和code
		batOpMapper.updateTempEntityMsg(sessionId);
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void updateTempPeriodData(ReceiveBean bean,String sessionId,String suitId) throws Exception {
		HashMap<String, String> hm = new HashMap<String, String>();
		//获取报表频率
		List<Fraq> selfraqList=batOpMapper.getFraq();
		for(int j=0;j<selfraqList.size();j++){
			String key=selfraqList.get(j).getFRAQ_CODE();
			String value=selfraqList.get(j).getFRAQ_NAME();
			hm.put(key, value);
		}
		List<ReceiveBean> selbeans=batOpMapper.selTempPeriodMsg(sessionId);
		for(int i=0;i<selbeans.size();i++){
			bean.setPERIOD_TYPE(hm.get(selbeans.get(i).getPERIOD_TYPE()));
			bean.setPERIOD_CODE(selbeans.get(i).getPERIOD_CODE());
		}
		// 进行期间转化处理
		if(bean.getPERIOD_CODE() != null && !"".equals(bean.getPERIOD_CODE()) && "MONTH".equals(bean.getPERIOD_TYPE())){
			String period_code = bean.getPERIOD_CODE();
			int i = period_code.indexOf("-");
			if(i != -1){
				String[] arr = period_code.split("-");
				if(arr[1].length() == 1){
					arr[1] = "0".concat(arr[1]);
				}
				period_code = arr[0].concat("-").concat(arr[1]);
			}
			bean.setPERIOD_CODE(period_code);
		}
		//同时更新期间id和报表时间
		batOpMapper.updateTempPeriodMsg(suitId, sessionId, bean.getPERIOD_CODE());
		//查询期间是否在兴竹系统中设置
		batOpMapper.isExistTempPeriod(sessionId, bean.getPERIOD_CODE());
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void updateTempCurrencyData(String suitId,String sessionId) throws Exception {
		batOpMapper.updateTempCurrencyMsg(suitId, sessionId);
	}
	@Override
	public void updateTempModalData(String suitId,String sessionId, String modaltypeId)
			throws Exception {
		//校验模板对照关系
		batOpMapper.IsExistTempModal(sessionId, modaltypeId); 
		//检验一个远光报表与系统中多个报表映射
		batOpMapper.IsOneTempModal(sessionId, modaltypeId);
		//同时更新模板id和code
		batOpMapper.updateTempModalMsg(suitId, sessionId, modaltypeId);
	}
	@Override
	public void updateTempRowitemData(String suitId, String sessionId,
			String modaltypeId) throws Exception {
		//判断行次是否为数值类型
		Pattern pattern=Pattern.compile("[0-9]+");
		String rownoString="";
		List<String> rownoList=new ArrayList<String>();
		List<Rowitem> rowitems=batOpMapper.selRowno(sessionId);
		for(int i=0;i<rowitems.size();i++){
			String rowno=rowitems.get(i).getROWNO().toString();
			if(rownoString.indexOf(rowno+",")==-1){
				rownoString+=rowno+",";
			}
		}
		rownoList =Arrays.asList(rownoString.split(","));
		List<String> errList=new ArrayList<String>();
		for (String rowno : rownoList) {
			if(!pattern.matcher(rowno).matches()){
				errList.add(rowno);
			}
		}
		if(errList.size()>0){
			batOpMapper.isRowNumber(sessionId,errList);
		}
	    //判断行指标不存在
	    batOpMapper.isExistTempRowitem(suitId, sessionId, modaltypeId);
	    //检查行指标引用关系是否正确
	    batOpMapper.IsOneTempRowitem(suitId,sessionId,modaltypeId);
	    //-同时更新行指标的id和code
	    batOpMapper.updateTempRowitemMsg(suitId, sessionId, modaltypeId);
	    
	}
	@Override
	public void updateTempColitemData(String suitId, String sessionId,
			String modaltypeId) throws Exception {
		//判断列次是否为数值类型
		Pattern pattern=Pattern.compile("[0-9]+");
		String colnoString="";
		List<String> colnoList=new ArrayList<String>();
		List<Colitem> colitems=batOpMapper.selColno(sessionId);
		for(int i=0;i<colitems.size();i++){
			String rowno=colitems.get(i).getCOLNO().toString();
			if(colnoString.indexOf(rowno+",")==-1){
				colnoString+=rowno+",";
			}
		}
		colnoList =Arrays.asList(colnoString.split(","));
		List<String> errList=new ArrayList<String>();
		for (String colno : colnoList) {
			if(!pattern.matcher(colno).matches()){
				errList.add(colno);
			}
		}
		if(errList.size()>0){
			batOpMapper.isColitemNumber(sessionId,errList);
		}
	    //列指标不存在
	    batOpMapper.isExistTempColitem(suitId, sessionId, modaltypeId);
	    //检查列指标引用关系是否正确
	    batOpMapper.IsOneTempColitem(suitId, sessionId, modaltypeId);
	    //同时更新列指标的id和code
	    batOpMapper.updateTempColitemMsg(suitId, sessionId, modaltypeId);
	}
	@Override
	public void checkTempRowAndCol(String suitId, String sessionId,
			String modaltypeId) throws Exception {
		//定义校验行列组合不合法的数据
		batOpMapper.checkTempRowAndCol(suitId, sessionId, modaltypeId);
		//查询同一表中固定行数据中重复的行列组合
		batOpMapper.checkTempFixRowAndCol(sessionId);
		//校验行列指标是否合法
		batOpMapper.checkTempFJRowAndCol(sessionId);
	}
	@Override
	public String isLock(String sessionId, String suitId, String cmStr)
			throws Exception {
		String flag = "";
		String[] strArray = cmStr.split("&");
		List<String> modalsheetList=new ArrayList<String>();
		List<String> entityIdList=new ArrayList<String>();
		for (int i = 0; i < strArray.length; i++) {
			String[] s = strArray[i].split(":");
			String entityId = s[0];
			entityIdList.add(entityId);
			String modalsheetId = s[1];
			modalsheetList.add(modalsheetId);
		}
		List<IMPDataBean> tempDataList = batOpMapper.getTempData(suitId, sessionId, modalsheetList, entityIdList);
		for(int i=0;i<tempDataList.size();i++){
			String appStatus = batOpMapper.getAppStatus(suitId, tempDataList.get(i).getENTITY_ID(), tempDataList.get(i).getPERIOD_ID(), tempDataList.get(i).getCURRENCY_ID(), tempDataList.get(i).getMODALTYPE_ID(), tempDataList.get(i).getMODALSHEET_ID());
			if("E".equals(appStatus)){
				flag = "报表接收失败原因，公司：《" + tempDataList.get(i).getENTITY_NAME() + "》==>" +
						"模板：《"+tempDataList.get(i).getMODALSHEET_NAME()+"》 已锁定,请解锁后重新全部导入！";
			}
		}
		
		return flag;
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void receiveData(String suitId,String sessionId,String entityId, String periodId,
			String currencyId, String modaltypeId, String modalsheetId,String userId)
			throws Exception {
		Connection conn = SpringDBHelp.getConnection();
		PreparedStatement pst = null;
		StringBuffer sql=new StringBuffer();
		Timestamp time = new Timestamp(new Date().getTime());
		//删除固定行指标数据
		batOpMapper.delRepData(suitId, entityId, periodId, currencyId, modaltypeId, modalsheetId);
		//删除浮动行指标数据
		batOpMapper.delFJData(suitId, entityId, periodId, currencyId, modaltypeId, modalsheetId);
		//保存固定行数据
		//查询临时导入表中固定行数据
		List<IMPDataBean> impDataBeanList=new ArrayList<IMPDataBean>();
		List<IMPDataBean> impRepMsgList=batOpMapper.getImpMsg(suitId, sessionId, entityId, periodId, currencyId, modaltypeId, modalsheetId);
		for(int i5=0;i5<impRepMsgList.size();i5++){
			IMPDataBean impDataBean=new IMPDataBean();
			IMPDataBean impRepMsg=impRepMsgList.get(i5);
			impDataBean.setCELLV_ID(UUID.randomUUID().toString());
			impDataBean.setSUIT_ID(impRepMsg.getSUIT_ID());
			impDataBean.setENTITY_ID(impRepMsg.getENTITY_ID());
			impDataBean.setPERIOD_ID(impRepMsg.getPERIOD_ID());
			impDataBean.setROWITEM_ID(impRepMsg.getROWITEM_ID());
			impDataBean.setCOLITEM_ID(impRepMsg.getCOLITEM_ID());
			impDataBean.setCURRENCY_ID(impRepMsg.getCURRENCY_ID());
			impDataBean.setENTITY_CODE(impRepMsg.getENTITY_CODE());
			impDataBean.setRPT_PRIOD(impRepMsg.getRPT_PRIOD());
			impDataBean.setROWITEM_CODE(impRepMsg.getROWITEM_CODE());
			impDataBean.setCOLITEM_CODE(impRepMsg.getCOLITEM_CODE());
			impDataBean.setCURRENCY_CODE(impRepMsg.getCURRENCY_CODE());
			impDataBean.setLAST_UPDATE_DATE(time);
			impDataBean.setLAST_UPDATED_BY(userId);
			impDataBean.setCREATION_DATE(time);
			impDataBean.setCREATED_BY(userId);
			if(impRepMsg.getDATA_TYPE().equals("3")){
				impDataBean.setCELLVALUE(impRepMsg.getCELLVALUE());
				impDataBean.setCELLTEXTV(null);
				
			}else{
				impDataBean.setCELLVALUE(null);
				impDataBean.setCELLTEXTV(impRepMsg.getCELLVALUE());
			}
			impDataBeanList.add(impDataBean);
		}
		//开始保存固定行数据
		batOpMapper.insertCellvalue(impDataBeanList);
		//开始保存浮动行数据
		//获取浮动行模版物理列与DATA列的对应--通过code
		List<Colitem> colitems=batOpMapper.getFjColitemListById(suitId, modaltypeId, modalsheetId);
		String keys="";
		String val="";
		for(int i=0;i<colitems.size();i++){
			String datacol=colitems.get(i).getDATA_COL();
			keys=keys+datacol+",";
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
		
		List<Map<String, String>> sqlMapList=new ArrayList<Map<String,String>>();
		//查询临时导入表中浮动行数据
		List<IMPDataBean> FJMsgList=batOpMapper.getFJMsg(suitId, sessionId, entityId, periodId, currencyId, modaltypeId, modalsheetId);
		
		//内外层循环作用：在具有相同行指标id和no时，这是一条浮动行记录，需要拼接成一条
		for(int i=0;i<FJMsgList.size();i++){
			Map<String, String> sqlMap=new HashMap<String, String>();
			//这里使用put(col,val),方便对应后面使用到put(data,val),
			Map<String, String> colAndValMap=new HashMap<String, String>();
			String entityCode=FJMsgList.get(i).getENTITY_CODE();
			String rptPeriod=FJMsgList.get(i).getRPT_PRIOD();
			String currencyCode=FJMsgList.get(i).getCURRENCY_CODE();
			String rowCode=FJMsgList.get(i).getROWITEM_CODE();
			String colCode=FJMsgList.get(i).getCOLITEM_CODE();
			String iColId=FJMsgList.get(i).getCOLITEM_ID();
			String iNo=FJMsgList.get(i).getNO();
			String iRowId=FJMsgList.get(i).getROWITEM_ID(); 
			String icellvalue=FJMsgList.get(i).getCELLVALUE();
			colAndValMap.put(iColId, icellvalue);
			for(int j=i+1;j<FJMsgList.size();j++){
				String jRowId=FJMsgList.get(j).getROWITEM_ID();
				String jColId=FJMsgList.get(j).getCOLITEM_ID();
				String jcellvalue=FJMsgList.get(j).getCELLVALUE();
				String jNo=FJMsgList.get(j).getNO();
				if((iRowId.equals(jRowId))&&(iNo.equals(jNo))){
					colAndValMap.put(jColId, jcellvalue);
					FJMsgList.remove(FJMsgList.get(j));
				}
			}
			sqlMap.put("FJ_CELLV_ID", UUID.randomUUID().toString());
			sqlMap.put("ROWITEM_ID", iRowId);
			sqlMap.put("ROWNUMDTL", iNo);
			sqlMap.put("SAVEFLAG", "0");
			sqlMap.put("ENTITY_CODE", entityCode);
			sqlMap.put("RPT_PRIOD", rptPeriod);
			sqlMap.put("CURRENCY_CODE", currencyCode);
			sqlMap.put("ROWITEM_CODE", rowCode);
			sqlMap.put("COLITEM_CODE", colCode);
			//构造sqlmap，后面insert插入有效列，这里sqlmap也需要构造这些有效列
			String [] dataArray=keys.split(",");
			for (int i2 = 0; i2 < dataArray.length; i2++) {
				sqlMap.put(dataArray[i2], "");
			}
			//把DATA列和val值放入sqlMap中
			Map<String, String> dataAndMap=new HashMap<String, String>();
			for(String colId:colAndValMap.keySet()){
				for (int j1 = 0; j1 < colitems.size(); j1++) {
					if(colId.equals(colitems.get(j1).getCOLITEM_ID())){
						//这里进行了重新赋值，记得测试一下
						sqlMap.put(colitems.get(j1).getDATA_COL(), colAndValMap.get(colId));
					}
				}
			}
			sqlMapList.add(sqlMap);
		}
		sql.append("INSERT INTO xsr_rep_fj_cellvalue(FJ_CELLV_ID,SUIT_ID,ENTITY_ID,PERIOD_ID,CURRENCY_ID,ROWITEM_ID,MODALSHEET_ID,ENTITY_CODE,PERIOD_CODE,CURRENCY_CODE,ROWITEM_CODE,ROWNUMDTL,SAVEFLAG,LAST_UPDATE_DATE,LAST_UPDATED_BY,CREATION_DATE,CREATED_BY,").
			append(keys).append(")VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,").append(val).append(")");
		try{
			//加值得设置
		pst=conn.prepareStatement(sql.toString());
		for(int i3=1;i3<=sqlMapList.size();i3++){
			Map<String, String> rec=sqlMapList.get(i3-1);
			pst.setString(1, rec.get("FJ_CELLV_ID"));
			pst.setString(2, suitId);
			pst.setString(3, entityId);
			pst.setString(4, periodId);
			pst.setString(5, currencyId);
			pst.setString(6, rec.get("ROWITEM_ID"));
			pst.setString(7, modalsheetId);
			pst.setString(8, rec.get("ENTITY_CODE"));
			pst.setString(9, rec.get("RPT_PRIOD"));
			pst.setString(10, rec.get("CURRENCY_CODE"));
			pst.setString(11, rec.get("ROWITEM_CODE"));
			pst.setString(12, rec.get("ROWNUMDTL"));
			pst.setString(13, rec.get("SAVEFLAG"));
			pst.setString(14, new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(time));
			pst.setString(15, userId);
			pst.setString(16,  new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(time));
			pst.setString(17, userId);
			for(int i4=1;i4<=colitems.size();i4++){
				pst.setString(i4+17, rec.get(colitems.get(i4-1).getDATA_COL()));
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
		//删除临时表信息
		batOpMapper.deleteTempImpData(suitId, sessionId, entityId, periodId, currencyId, modaltypeId, modalsheetId);
	}
	
	

}
