package com.xzsoft.xc.ex.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

/**
 * @describe 费用项目树处理
 * @author tangxl
 *
 */
public class XCEXItemTreeUtil {
	/**
	 * 
	 * @methodName  合并要处理的树形数据，并去除那些不存在叶子节点费用项目,
	 * @author      tangxl
	 * @date        2016年9月29日
	 * @describe    TODO
	 * @param 		parentItemList
	 * @param 		leafItemList
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public static void mergeExItemData(List<HashMap<String, String>> parentItemList,HashMap<String, String> leafItem,
			HashMap<String, HashMap<String, String>> remainItemMap) throws Exception{
		if(!remainItemMap.containsKey(leafItem.get("EX_ITEM_ID"))){
			remainItemMap.put(leafItem.get("EX_ITEM_ID"), leafItem);
			for(HashMap<String, String> p:parentItemList){
				if(leafItem.get("EX_ITEM_UP_ID").equals(p.get("EX_ITEM_ID"))){
					//递归查询树形数据
					mergeExItemData(parentItemList,p,remainItemMap);
					break;
				}
			}
		}
	}
	/**
	 * 
	 * @methodName  parseListToItemTree
	 * @author      tangxl
	 * @date        2016年9月29日
	 * @describe    解析费用项目树
	 * @param 		itemList
	 * @param 		parentId
	 * @return
	 * @throws Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public static StringBuffer parseListToItemTree(List<HashMap<String, String>> itemList,String parentId) throws Exception{
		StringBuffer buffer = new StringBuffer("[");
		for(HashMap<String, String> t:itemList){
			if(parentId.equalsIgnoreCase(t.get("EX_ITEM_UP_ID"))){
				buffer.append("{");
				Iterator<Entry<String, String>> iterator = t.entrySet().iterator();
				while(iterator.hasNext()){
					Entry<String, String> entry = iterator.next();
					String key = entry.getKey();
					String value = entry.getValue();
					if(value == null){
						value = "";
					}
					buffer.append(key).append(":'").append(value).append("',");
				}
			    //查询子节点
				StringBuffer childBuffer = parseListToItemTree(itemList,t.get("EX_ITEM_ID"));
				//判断子节点是否存在
				if(childBuffer.toString().equals("[]")){
					buffer.append("leaf:true");
				}else{
					buffer.append("children:").append(childBuffer.toString());
				}
				//每一轮循环对应一条记录
				buffer.append("},");
			}
		}
		//去除最后一个逗号
		if(buffer.lastIndexOf(",") == buffer.length()-1){
			buffer.deleteCharAt(buffer.lastIndexOf(","));
		}
		//拼接最后的中括号
		buffer.append("]");
		return buffer;
	}
	
}
