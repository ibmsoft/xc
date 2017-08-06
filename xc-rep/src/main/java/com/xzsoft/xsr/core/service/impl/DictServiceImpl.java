package com.xzsoft.xsr.core.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xzsoft.xsr.core.mapper.DictMapper;
import com.xzsoft.xsr.core.modal.Dict;
import com.xzsoft.xsr.core.service.DictService;

@Service
public class DictServiceImpl implements DictService {
	
	@Autowired
	private DictMapper dictMapper;
	public Logger log = Logger.getLogger(this.getClass());

	/**
	 * 字典导出
	 */
	@Override
	public void dictExp(String filepath, String dictName) throws Exception {
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("DICTLIST");
		ArrayList<Dict> dictList = new ArrayList<Dict>();
		dictList = dictMapper.selectDictByDictName(dictName);
		
		Element DICT = root.addElement("DICT");
		DICT.addAttribute("DICT_TYPE", dictList.get(0).getDICT_TYPE());
		DICT.addAttribute("DICT_DESC", dictList.get(0).getDICT_DESC());
		DICT.addAttribute("DICT_NAME", dictList.get(0).getDICT_NAME());
		for(Dict o:dictList){
			Element DICTITEM = DICT.addElement("DICTITEM");
			DICTITEM.addAttribute("ITEM_NAME", o.getITEM_NAME());
			DICTITEM.addAttribute("ITEM_DESC", o.getITEM_DESC());
			DICTITEM.addAttribute("ITEM_VAL", o.getITEM_VAL());
			DICTITEM.addAttribute("DICT_TABLE", o.getDICT_TABLE());
			DICTITEM.addAttribute("ITEM_CODE", o.getITEM_CODE());
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
	 * 字典导入
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String dictImpt(File folder,String userId) throws Exception {
		String message = "";
		Timestamp time = new Timestamp(new Date().getTime());
		
		// dom4j读取XML文件；
		SAXReader sax = new SAXReader();
		org.dom4j.Document document = null;
		document = sax.read(folder);
		try{
			// 获取文件根节点；
			org.dom4j.Element root = document.getRootElement();
			// 获取文件DICT节点；
			org.dom4j.Element DICT = root.element("DICT");
			//获取DICT属性
			String DICT_TYPE = DICT.attributeValue("DICT_TYPE");
			String DICT_DESC = DICT.attributeValue("DICT_DESC");
			String DICT_NAME = DICT.attributeValue("DICT_NAME");
			//判断字典是否存在
			int count = dictMapper.isDictExist(DICT_NAME);
			if (count > 0) {
				message = "{flag:1,msg:'字典【" + DICT_NAME + "】已经存在'}";
			} else{
				for(Iterator j = DICT.elementIterator("DICTITEM");j.hasNext();){
					org.dom4j.Element element = (org.dom4j.Element) j.next();
					HashMap params = new HashMap();
					
					params.put("DICT_NAME",DICT_NAME);
					params.put("DICT_DESC",DICT_DESC);
					params.put("DICT_TYPE",DICT_TYPE);
					params.put("DICT_TABLE",element.attributeValue("DICT_TABLE"));
					params.put("ITEM_CODE",element.attributeValue("ITEM_CODE"));
					params.put("ITEM_NAME",element.attributeValue("ITEM_NAME"));
					params.put("ITEM_VAL",element.attributeValue("ITEM_VAL"));
					params.put("ITEM_DESC",element.attributeValue("ITEM_DESC"));
					params.put("LAST_UPDATED_BY",userId);
					params.put("CREATED_BY",userId);
					params.put("LAST_UPDATE_DATE",time);
					params.put("CREATION_DATE",time);
					
					dictMapper.insertDict(params);
				}
			}
			message = "{flag:0,msg:'导入字典成功'}";
		}catch(Exception e){
			message = "{flag:1,msg:'导入字典失败'}";
		}
		
		return message;
	}

}
