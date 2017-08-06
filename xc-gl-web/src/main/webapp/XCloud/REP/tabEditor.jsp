<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%
	String ctxPath = request.getContextPath() ;
	String accHrcyId = request.getParameter("accHrcyId");
	String tabId = request.getParameter("tabId"); 
%>
<html>
<head>
    <title>格式编制</title>
    
    <meta charset="utf-8">
    <link rel="stylesheet" type="text/css" href="<%=ctxPath%>/xip/es/extjs4.2.1/ext-theme-gray-all.css"></link>
    
    <link rel="stylesheet" type="text/css" href="<%=ctxPath%>/xip/es/EnterpriseSheet/resources/css/common.css"></link>
    <link rel="stylesheet" type="text/css" href="<%=ctxPath%>/xip/es/EnterpriseSheet/resources/css/sheet.css"></link>
    <link rel="stylesheet" type="text/css" href="<%=ctxPath%>/xip/es/EnterpriseSheet/resources/css/toolbar.css"></link>
    <link rel="stylesheet" type="text/css" href="<%=ctxPath%>/xip/es/EnterpriseSheet/resources/css/icon.css"></link>
    
    <script type="text/javascript" src="<%=ctxPath%>/xip/es/extjs4.2.1/ext-all.js"></script> 
    <script type="text/javascript" src="<%=ctxPath%>/xip/es/extjs4.2.1/ext-lang-zh_CN.js"></script>
    
	<script type="text/javascript" src="<%=ctxPath%>/xip/es/EnterpriseSheet/src/EnterpriseSheet/Config.js"></script>
	<script type="text/javascript" src="<%=ctxPath%>/xip/es/EnterpriseSheet/src/language/zh_CN.js"></script>
	<script type="text/javascript" src="<%=ctxPath%>/xip/es/EnterpriseSheet/es.js"></script>
	<script type="text/javascript" src="<%=ctxPath%>/xip/es/EnterpriseSheet/templates/CenterPanel.js"></script>
    
</head>
<body>
 	<script type="text/javascript">
	    Ext.onReady(function(){
	    	//QuickTips的作用是读取标签中的ext:qtip属性，并为它赋予显示提示的动作;
	    	Ext.QuickTips.init();
	    	// 系统默认将第一行隐藏，第一行相应单元格上记录了列次信息
	    	var loadingMask = new Ext.LoadMask(Ext.getBody(), {msg: SLANG['waiting']});
	    	loadingMask.show();
	    	
		    SHEET_API = Ext.create('EnterpriseSheet.api.SheetAPI', {
		        openFileByOnlyLoadDataFlag: true
		    });
		    SHEET_API_HD = SHEET_API.createSheetApp({
		    	withoutTitlebar: false,
	    	    withoutSheetbar: false,//显示页签bar
	    	    withoutToolbar:  false,
	    	    withoutContentbar: false,
	    	    withoutSidebar: true
			});
			//ES panel
		    var centralPanel = Ext.create('enterpriseSheet.templates.CenterPanel', {
		    	listeners : {
		    		'afterrender' : function(){
		    			var processbar = Ext.Msg.wait("正在提交请求...", "提示") ;
		    			Ext.Ajax.request({
		    				url : '<%=ctxPath%>' + '/esAction.do?method=findTemplateById' ,
		    				method: 'POST',
		    				timeout : 3600000,
		    				params : {
		    					'tabId' : '<%=tabId%>'
		    				},
		    				success : function(response, options) {
		    					processbar.hide() ;
		    					var response = Ext.JSON.decode(response.responseText);
		    				    if(response.flag == "0"){ // 加载成功
		    				      	var json = {
		    				        	fileName	: response.fileName,
		    				        	sheets		: response.sheets,
		    				        	cells		: response.cells,
		    				        	floatings 	: response.floatings
		    				      	};
		    				      	SHEET_API.loadData(SHEET_API_HD, json ,function(){},this);
		    				      
		    				    }else if(response.flag == "2"){ // 无模板信息
		    				      	var emptyJson = {
		    				        	fileName: '模板格式设置',
			    				        sheets: [
			    				          {id:0,name:'sheet1',actived:true},
			    				          {id:1,name:'sheet2',actived:false},
			    				          {id:2,name:'sheet3',actived:false}
			    				        ],
			    				        cells:[],
			    				        floatings:[]
		    				      	};
		    				      	SHEET_API.loadData(SHEET_API_HD, emptyJson ,function(){},this);
		    				      	Ext.Msg.alert("提示",response.msg) ;
		    				      
		    				    }else{ // 加载失败
		    				    	Ext.Msg.alert("提示",response.msg) ;
		    				    }
		    				},
		    				failure : function() {
		    					processbar.hide() ;
		    					Ext.Msg.alert("提示","加载报表模板格式失败!");
		    				}
		    			}); 
		    		}
		    	}
		    });

			// 获取信息
			var getItemWindow = function(itemType, _title, _data){
				var store ;
				if(itemType == 'row'){
					store = Ext.create('Ext.data.Store', {
					    storeId:'rowitemStore',
					    fields:['ROWITEM_ID', 'ROWITEM_CODE', 'ROWITEM_NAME','LANNO','ROWNO','X','Y','ROWNO_Y','IS_NEW'],
					    data:{'items':_data},
					    proxy: {
					        type: 'memory',
					        reader: {
					            type: 'json',
					            root: 'items'
					        }
					    }
					});					
				}else{
					store = Ext.create('Ext.data.Store', {
					    storeId:'colitemStore',
					    fields:['COLITEM_ID', 'COLITEM_CODE', 'COLITEM_NAME','LANNO','COLNO','DATATYPE','X','Y','IS_NEW'],
					    data:{'items':_data},
					    proxy: {
					        type: 'memory',
					        reader: {
					            type: 'json',
					            root: 'items'
					        }
					    }
					});					
				}
	 	       	
				var columnArray = [{ header: '序号',  dataIndex: '',xtype:'rownumberer',width:50,align:'center'}] ;
				if(itemType == 'row'){
					columnArray.push({ header: '行指标ID',  dataIndex: 'ROWITEM_ID',hidden:true }) ;
					columnArray.push({ header: '行指标编码', dataIndex: 'ROWITEM_CODE',hidden:true,sortable:false,width:150}) ;
					columnArray.push({ header: '行指标名称', dataIndex: 'ROWITEM_NAME',sortable:false,width:400 ,
						field:{xtype:'textfield',allowBlank:false}
					}) ;
					columnArray.push({ header: '栏次', dataIndex: 'LANNO',width:50,align:'center' }) ;
					columnArray.push({ header: '行次', dataIndex: 'ROWNO',width:50,sortable:false,align:'center' }) ;
					columnArray.push({ header: '横坐标', dataIndex: 'X',hidden:true }) ;
					columnArray.push({ header: '列坐标', dataIndex: 'Y',hidden:true }) ;
					columnArray.push({ header: '行次所有列', dataIndex: 'ROWNO_Y',hidden:true }) ;
					columnArray.push({ header: '新增', dataIndex: 'IS_NEW',align:'center',
						renderer:function(value, metaData, record, rowIndex, colIndex, store, view){
							if(value=='Y'){
							  return '<font color=red>是</font>' ;
							}else if(value == 'N'){
							  return '否' ;
							}else{
							  return value ;
							}						
						}
					}) ;	
					
				}else{
					columnArray.push({ header: '列指标ID',  dataIndex: 'COLITEM_ID',hidden:true }) ;
					columnArray.push({ header: '列指标编码', dataIndex: 'COLITEM_CODE',hidden:true,sortable:false,width:150}) ;
					columnArray.push({ header: '列指标名称', dataIndex: 'COLITEM_NAME',sortable:false,width:350,
						field:{xtype:'textfield',allowBlank:false}	
					}) ;
					columnArray.push({ header: '栏次', dataIndex: 'LANNO',width:50,align:'center' }) ;
					columnArray.push({ header: '列次', dataIndex: 'COLNO',width:50,sortable:false,align:'center' }) ;
					columnArray.push({ header: '数据类型', dataIndex: 'DATATYPE',width:90,align:'center',sortable:false,
						renderer:function(value, metaData, record, rowIndex, colIndex, store, view){
							if(value == 1){
							  return '项目' ;
							}else if(value == 3){
							  return '数值' ;
							}else if(value == 5){
							  return '文本' ;
							}else{
							  return value ;
							}						
						}
					}) ;
					columnArray.push({ header: '横坐标', dataIndex: 'X',hidden:true }) ;
					columnArray.push({ header: '列坐标', dataIndex: 'Y',hidden:true }) ;
					columnArray.push({ header: '新增', dataIndex: 'IS_NEW',align:'center',
						renderer:function(value, metaData, record, rowIndex, colIndex, store, view){
							if(value=='Y'){
							  return '<font color=red>是</font>' ;
							}else if(value == 'N'){
							  return '否' ;
							}else{
							  return value ;
							}						
						}
					}) ;
				}
				
	  	      	var grid = Ext.create('Ext.grid.Panel', {
		  	        store : store,
		  	        border:false,
		  	        forceFit : true,
		  	        multiSelect : false ,
		  	      	simpleSelect : true,
		  	        columns : columnArray,
		           	plugins: [
	  	               Ext.create('Ext.grid.plugin.CellEditing', {
	  	                   clicksToEdit: 1
	  	               })
		  	        ]
		  	   	});	
	  	      	
	  	      	var btnSave = Ext.create('Ext.Button', {
	  	          	text: '保存',
		  	        handler: function() {
	  	        		// 序号
	  	        		var sheet = SHEET_API_HD.sheet;
	  	        		// 表格数据
		  	        	var records = [] ;
	  	        		for(var i=0; i<store.getCount(); i++){
	  	        			var r = store.getAt(i) ;
	  	        			records.push(r.data) ;
	  	        		}
		  	        	// 执行保存处理
		  	        	if(itemType == 'row'){
		  	        		// 行指标数据信息
		  	        		if(records.length === 0){
		  	        		  　Ext.Msg.alert('提示','未发现行指标信息无须执行保存处理') ;
		  	        		  return false ;
		  	        		}
		  	        		var processbar = Ext.Msg.wait("正在保存行指标信息...", "提示") ;
		  	        		// 提交请求
		  	        		Ext.Ajax.request({
		  	        		  	url: '<%=ctxPath%>'+'/esAction.do?method=saveRowItems',
		  	        		  	params: {
		  	        		    	'accHrcyId' : '<%=accHrcyId%>',
		  	        		    	'tabId' : '<%=tabId%>',
		  	        		    	'sheetId'   : sheet.getSheetId(),
		  	        		    	'rowArray'  : Ext.JSON.encode(records) 
		  	        		  	},
		  	        		  	timeout : 3600000,
					    	  	success : function(response, options) {
					    	   	 	processbar.hide() ;
					    	    	var response = Ext.JSON.decode(response.responseText);
			  	        		    if(response.flag == "0"){
			  	        		      // 保存成功后在行次单元格上标注指标编码和ID信息
			  	        		      SHEET_API.updateCells(SHEET_API_HD, response.cells);
			  	        		      win.hide() ;
			  	        		      saveFormat.fireEvent('click') ;
			  	        		      
			  	        		    }else{
			  	        		    	Ext.Msg.alert("提示",response.msg) 
			  	        		    }
					    	  	},
					    	  	failure : function() {
					    		  	processbar.hide() ;
					    		 	Ext.Msg.alert("提示","保存行指标失败!");
					    	  	}
		  	        		});		  	    
		  	        		
		  	        	}else{
		  	        		// 列指标数据信息
		  	        		if(records.length === 0){
		  	        		  	Ext.Msg.alert("提示","未发现列指标信息无须执行保存处理") ;
		  	        		  	return false ;
		  	        		}
		  	        		var processbar = Ext.Msg.wait("正在保存列指标信息...", "提示") ;
		  	        		// 提交请求
		  	        		Ext.Ajax.request({
		  	        		 	url: '<%=ctxPath%>'+'/esAction.do?method=saveColItems',
		  	        		  	params: {
		  	        		    	'accHrcyId' : '<%=accHrcyId%>',
		  	        		    	'tabId' : '<%=tabId%>',
		  	        		    	'sheetId'   : sheet.getSheetId(),
		  	        		    	'colArray'  : Ext.JSON.encode(records) 
		  	        		  	},
		  	        		  	timeout : 3600000,
		  	        			success : function(response, options) {
		  	        		    	processbar.hide() ;
		  	        		    	var response  = Ext.JSON.decode(response.responseText) ;
		  	        		    	if(response.flag == "0"){
		  	        		      		// 保存成功后在第一行单元格上标注批注和值信息
		  	        		      		SHEET_API.updateCells(SHEET_API_HD, response.cells);
		  	        		      		SHEET_API.hideRow(SHEET_API_HD, 1, 1, sheet.getSheetId());
		  	        		      		win.hide() ;  
		  	        		      		saveFormat.fireEvent('click') ;
		  	        		    	}else{
		  	        		    		Ext.Msg.alert("提示",response.msg) ;
		  	        		    	}
		  	        		  	},
					    	  	failure : function() {
					    		  	processbar.hide() ;
					    		 	Ext.Msg.alert("提示","保存列指标失败!");
					    	  	}
		  	        		});
		  	        	}
		  	        }
		  	    });
	  	      	var btnCancel = Ext.create('Ext.Button', {
	  	          	text: '取消',
		  	        handler: function() {
		  	        	win.hide() ;
		  	        }
		  	    });
				
	 	      	var win = Ext.create("Ext.Window", {
		    	    title : _title,
		    	    closeAction : 'hide',
		    	    width  : document.body.clientWidth*(.6),
					height : document.body.clientHeight*(.8),
					layout : 'fit',
					closable : true,
					modal : true,
					maximizable : true,
					maximized : false,
		    	    items : [grid],
		    	    buttons : [btnSave,btnCancel],
		    	    buttonAlign:'right'
		    	});		
	 	       	
	 	       	return win ;
			}
			
			// 保存模板
		    var saveFormat = Ext.create('Ext.Button', {
		        text: '保存模板',
		        style: {
		        	marginTop: '0px',//距顶部高度
		            marginLeft:'2px',//距左边宽度
		            marginRight:'2px'//距右边宽度
		        },
		        width:65,
		        height:20,
		        handler: function(){
		        	// 获取表格数据信息
		        	var dataJson = SHEET_API.getJsonData(SHEET_API_HD,false) ;
		        	var sheet = SHEET_API_HD.sheet;

		        	var processbar = Ext.Msg.wait("正在执行模板保存操作...", "提示") ;
		        	// 提交请求
		        	Ext.Ajax.request({
			    	  	url: '<%=ctxPath%>'+'/esAction.do?method=saveTemplate',
			    	  	params: {
				    	    'accHrcyId' : '<%=accHrcyId%>',
				    	    'sheetId' : sheet.getSheetId(),
				    	    'dataJson':Ext.JSON.encode(dataJson) 
			    	  	},
			    	  	timeout : 3600000,
			    	  	success : function(response, options) {
			    	   	 	processbar.hide() ;
			    	    	var response = Ext.JSON.decode(response.responseText);
			    	    	Ext.Msg.alert("提示",response.msg) ;
			    	  	},
			    	  	failure : function() {
			    		  	processbar.hide() ;
			    		 	Ext.Msg.alert("提示","加载报表模板格式失败!");
			    	  	}
		        	});
		        }
		    });
			// 标定公司
		    var btnRecOrg = Ext.create('Ext.Button', {
		        text: '标定公司',
		        style: {
		        	marginTop: '0px',//距顶部高度
		            marginLeft:'2px',//距左边宽度
		            marginRight:'2px'//距右边宽度
		        },
		        width:65,
		        height:20,
		        handler: function(){
		        	// TODO: 标定公司单元格
		        	var sheet = SHEET_API_HD.sheet;	// 当前活动页签
		        	var sm = sheet.getSelectionModel(); // 选择器对象
		        	var focus = sm.getFocusCell();	// 选中区域
		        	var cells = [];
		        	cells.push({
		        		sheet: sheet.getSheetId(), 
		        		row  : focus.row, 
		        		col  : focus.col, 
		        		json : { comment: "getcorp", commentEdit: "hide" },  
		        		applyWay: 'apply'
		        	});
		        	SHEET_API.updateCells(SHEET_API_HD, cells);		        	
		        }
		    });
			// 标定期间
		    var btnRecPeriod = Ext.create('Ext.Button', {
		        text: '标定期间',
		        style: {
		        	marginTop: '0px',//距顶部高度
		            marginLeft:'2px',//距左边宽度
		            marginRight:'2px'//距右边宽度
		        },
		        width:65,
		        height:20,
		        handler: function(){
		        	// TODO: 标定期间单元格
		        	var sheet = SHEET_API_HD.sheet;	// 当前活动页签
		        	var sm = sheet.getSelectionModel(); // 选择器对象
		        	var focus = sm.getFocusCell();	// 选中区域
		        	var cells = [];
		        	cells.push({
		        		sheet   : sheet.getSheetId(), 
		        		row     : focus.row, 
		        		col     : focus.col, 
		        		json    : { comment: "getperiod", commentEdit: "hide" },  
		        		applyWay: 'apply'
		        	});
		        	SHEET_API.updateCells(SHEET_API_HD, cells);		        	
		        }
		    });
			// 标定行次
		    var btnRecRowNo = Ext.create('Ext.Button', {
		        text: '标定行次',
		        style: {
		        	marginTop: '0px',//距顶部高度
		            marginLeft:'2px',//距左边宽度
		            marginRight:'2px'//距右边宽度
		        },
		        width:65,
		        height:20,
		        handler: function(){
		        	// TODO: 标定行次单元格
		        	var sheet = SHEET_API_HD.sheet;	// 当前活动页签
		        	var sm = sheet.getSelectionModel(); // 选择器对象
		        	var focus = sm.getFocusCell();	// 选中区域
		        	var cells = [];
		        	cells.push({
		        		sheet: sheet.getSheetId(), 
		        		row  : focus.row, 
		        		col  : focus.col, 
		        		json : { comment: "rowno", commentEdit: "hide" },  
		        		applyWay: 'apply'
		        	});
		        	SHEET_API.updateCells(SHEET_API_HD, cells);		        	
		        }
		    });
			// 标定项目
		    var btnRecItem = Ext.create('Ext.Button', {
		        text: '标定项目',
		        style: {
		        	marginTop: '0px',//距顶部高度
		            marginLeft:'2px',//距左边宽度
		            marginRight:'2px'//距右边宽度
		        },
		        width:65,
		        height:20,
		        handler: function(){
		        	// TODO: 标定行指标项目基准单元格
		        	var sheet = SHEET_API_HD.sheet;	// 当前活动页签
		        	var sm = sheet.getSelectionModel(); // 选择器对象
		        	var focus = sm.getFocusCell();	// 选中区域
		        	var cells = [];
		        	cells.push({
		        		sheet:sheet.getSheetId(), 
		        		row:focus.row, 
		        		col:focus.col, 
		        		json: { comment: "rowitem", commentEdit: "hide" },  
		        		applyWay: 'apply'
		        	});
		        	SHEET_API.updateCells(SHEET_API_HD, cells);		        	
		        }
		    });
		    // 行指标
		    var btnRowItem = Ext.create('Ext.Button', {
		        text: '行指标',
		        style: {
		        	marginTop: '0px',//距顶部高度
		            marginLeft:'2px',//距左边宽度
		            marginRight:'2px'//距右边宽度
		        },
		        width:65,
		        height:20,
		        handler: function(){
		        	// 科目体系
		        	var accHrcyId = '<%=accHrcyId%>' ;
		        	// 获取选中区域, 返回值[{row: 2, col: 2, val: 100}, {row: 2, col: 3, val: "test"}]
		        	var selectedRange = SHEET_API.getSelectedRangeData(SHEET_API_HD) ;
		        	if(selectedRange.length === 0){
		        	  Wb.toast('请选择行指标区域') ;
		        	  return false ;
		        	}
		        	// 第一个单元格
		        	var firstCell = selectedRange[0];
		        	var x = firstCell.row ;
		        	var y = firstCell.col ;
		        	// 当前活动页签
		        	var sheet = SHEET_API_HD.sheet;
		        	var cellJson = sheet.getCellValue(sheet.getSheetId(),x, y);

		        	if(cellJson.data === undefined) {
		        	  Ext.Msg.alert("提示", "请选择标定的行指标区域");
		        	  return;
		        	}
		        	if(cellJson.comment != "rowitem") {
		        	  Ext.Msg.alert("提示", "行指标区域应从标定项目单元格开始,您选择的区域不正确!");
		        	  return;
		        	}
		        	// 获取所有带批注的单元格信息:[{sheetId: 1, x: 2, y: 2, comment: "great work"}, ...]
		        	var allCells = SHEET_API.getCellsComment(SHEET_API_HD,sheet.getSheetId());
		        	var rownoArray = [] ;
		        	var rowitemArray = [] ;
		        	Ext.each(allCells,function(r){
		        	  var _comment = r.comment ;
		        	  if(_comment === 'rowno'){
		        	    rownoArray.push(r) ;
		        	  }
		        	  if(_comment === 'rowitem'){
		        	    rowitemArray.push(r) ;
		        	  }
		        	}) ;
		        	if (rowitemArray.length <= 0) {
		        	  	Ext.Msg.alert("提示", "请标定项目之后再复核行指标。");
		        	  	return ;
		        	}else if(rowitemArray.length > 1){
		        	  	// 按列坐标排序
		        	  	rowitemArray.sort(function(a,b){
		        	    	if(a.y<b.y){  
		        	      		return -1;  
		        	    	}else if(a.y>b.y){  
		        	      		return 1;  
		        	    	}  
		        	    	return 0; 
		        	  	}) ;
		        	}
		        	// 插入栏次
		        	var i=1;
		        	Ext.each(rowitemArray, function(r){
		        	  	r.lanno = i ;
		        	  	i++ ;
		        	}) ;
		        	if (rownoArray.length <= 0) {
		        	  	Ext.Msg.alert("提示", "请标定行次之后再复核行指标。");
		        	  	return;
		        	}else if(rownoArray.length > 1){
		        	  	// 按行次列坐标号排序
		        	  	rownoArray.sort(function(a,b){
		        	    	if(a.y<b.y){  
		        	      		return -1;  
		        	    	}else if(a.y>b.y){  
		        	      		return 1;  
		        	   	 	}  
		        	    	return 0; 
		        	  	}) ;
		        	}
		        	// 插入栏次
		        	var i=1 ;
		        	Ext.each(rownoArray,function(r){
		        		r.lanno = i ;
		        		i++ ;
		        	}) ;
		        	// 计算每列行指标项目对应的行次列坐标
		        	for(var i=0; i<rowitemArray.length; i++){
		        	  var r = rowitemArray[i] ;
		        	  for(var j=0; j<rownoArray.length; j++){
		        	    var rr = rownoArray[j] ;
		        	    if(r.lanno == rr.lanno){
		        	      r.rownoy = rr.y ;
		        	    }
		        	  }
		        	}
		        	// 获取行次列坐标
		        	var rowitemCell ;
		        	Ext.each(rowitemArray, function(r){
		        	  if(r.y == y && r.x ==x){
		        	    rowitemCell = r ;
		        	  }
		        	}) ;
		        	// 处理表格记录
		        	var celldata = [] ;
		        	var count= 0 ;
		        	Ext.each(selectedRange, function(r){
		        	  // 判断是否选择了多列
		        	  if(y != r.col){
		        	    count++ ;
		        	  }
		        	  var val = r.val ;
		        	  val = val.replace(/(^\s*)|(\s*$)/g,'');	// 去除前后空格
		        	  if(val != null && val != "" && val != undefined){
			        	  // 行次
			        	  var cell = SHEET_API.getCellValue(SHEET_API_HD, sheet.getSheetId(), r.row, rowitemCell.rownoy);
			        	  var _rowno = cell.data ;
			        	  
			        	  var _rowId = '' ;
			        	  var _rowCode = '' ;
			        	  var _isNew = 'Y' ;
			        	  var cc = cell.comment ;
			        	  if(cc != null && cc != "" && cc != "undefined"){
			        		  var arr = cc.split(">>") ;
			        		  var a = arr[1].split('/') ;
			        	      _rowCode = a[0] ;
			        	      _rowId = a[1] ;
			        	      _isNew = 'N' ;
			        	  }
			        	  // 如果行次不为空且为数值
			        	  if(_rowno != null && _rowno != "" && _rowno != "undefined" && !isNaN(_rowno)){
			        		  var json = {
	        				  	'ROWITEM_ID' : _rowId,
	        				  	'ROWITEM_CODE' : _rowCode,
	        				  	'ROWITEM_NAME' : val,
	        				  	'LANNO' : rowitemCell.lanno ,
	        				  	'ROWNO' : _rowno ,
	        				  	'X' : r.row,
	        				  	'Y' : r.col,
	        				  	'ROWNO_Y' : rowitemCell.rownoy,
	        				  	'IS_NEW': _isNew
	        				  } ;
	        				  celldata.push(json) ;
			        	  }		        		  
		        	  }
		        	}) ;

		        	if(count >0){
		        		Ext.Msg.alert("提示",'提取行指标时只能选择其中一列数据');
		        	  	return false ;
		        	}

		        	// 加载表格
		        	var rowitemWin = getItemWindow('row','行指标',celldata) ;
		        	rowitemWin.show() ;
		        }
		    });
		    // 列指标
		    var btnColItem = Ext.create('Ext.Button', {
		        text: '列指标',
		        style: {
		        	marginTop: '0px',//距顶部高度
		            marginLeft:'2px',//距左边宽度
		            marginRight:'2px'//距右边宽度
		        },
		        width:65,
		        height:20,
		        handler: function(){
		        	// 科目体系
		        	var accHrcyId = '<%=accHrcyId%>' ;
		        	// 获取选中区域, 返回值[{row: 2, col: 2, val: 100}, {row: 2, col: 3, val: "test"}]
		        	var selectedRange = SHEET_API.getSelectedRangeData(SHEET_API_HD) ;
		        	if(selectedRange.length === 0){
		        		Ext.Msg.alert('请选择列指标区域') ;
		        	  	return false ;
		        	}
		        	// 表格序号
		        	var sheet = SHEET_API_HD.sheet;
		        	var sheetTabId = sheet.getSheetId()　;
		        	// 第一个单元格
		        	var firstCell = selectedRange[0];
		        	var x = firstCell.row ;
		        	var y = firstCell.col ;
		        	// 获取所有带批注的单元格信息:[{sheetId: 1, x: 2, y: 2, comment: "great work"}, ...]
		        	var allCells = SHEET_API.getCellsComment(SHEET_API_HD,sheet.getSheetId());
		        	var rownoArray = [] ;
		        	var rowitemArray = [] ;
		        	var rownoYArray = [] ;
		        	var rowitemYArray = [] ;
		        	Ext.each(allCells,function(r){
		        	  var _comment = r.comment ;
		        	  if(_comment === 'rowno'){
		        	    rownoArray.push(r) ;
		        	  }
		        	  if(_comment === 'rowitem'){
		        	    rowitemArray.push(r) ;
		        	  }
		        	}) ;
		        	if (rowitemArray.length <= 0) {
		        	  	Ext.Msg.alert("提示", "请标定项目之后再复核列指标。");
		        	  	return ;
		        	}else if(rowitemArray.length > 1){
		        		// 按列坐标排序
		        		rowitemArray.sort(function(a,b){
		        			if(a.y<b.y){  
		        				return -1;  
		        			}else if(a.y>b.y){  
		        				return 1;  
		        			}  
		        			return 0; 
		        		}) ;
		        	}
		        	// 计算列次数组
		        	var i = 0 ;
		        	Ext.each(rowitemArray, function(r){
		        	  	rowitemYArray[i] = r.y ;
		        	  	i++ ;
		        	}) ;

		        	if(rownoArray.length <= 0) {
		        	  	Ext.Msg.alert("提示", "请标定行次之后再复核列指标。");
		        	  	return;
		        	}else if(rownoArray.length > 1){
		        	 	// 按行次列坐标号排序
		        	  	rownoArray.sort(function(a,b){
		        	    	if(a.y<b.y){  
		        	      		return -1;  
		        	    	}else if(a.y>b.y){  
		        	      		return 1;  
		        	    	}  
		        	    	return 0; 
		        	  	}) ;
		        	}
		        	// 计算列次数组
		        	var i = 0 ;
		        	Ext.each(rownoArray, function(r){
		        	  	rownoYArray[i] = r.y ;
		        	  	i++ ;
		        	}) ;
		        	// 处理表格记录
		        	var celldata = [] ;
		        	var count= 0 ;
		        	var isFirstCreated = true ;	// 第一次创建列指标
		        	Ext.each(selectedRange, function(r){
		        	  // 判断是否选择了多行
		        	  if(x != r.row){
		        		  count++ ;
		        	  }
		        	  var val = r.val ; // 单元格值
		        	  val = val.replace(/(^\s*)|(\s*$)/g,'');	// 去除前后空格
		        	  if(val != null && val != "" && val != "undefined"){
			        	  // 计算栏次
			        	  var _lanno = 0 ;
			        	  for(var i=0; i<rownoYArray.length; i++){
			        		  if(r.col > rownoYArray[i]){
			        			  _lanno++ ;
			        		  }
			        	  }
			        	  if(_lanno === 0) {
			        	    	_lanno = 1 ;
			        	  }
			        	  // 判断当前单元格是否与rowitem单元格在同一列, 如果不考虑浮动行此列可以去除
			        	  var flag = 0 ;
			        	  for(var k=0; k<rowitemYArray.length; k++){
			        	    if(r.col == rowitemYArray[k]){
			        	      flag = 1 ;
			        	    }
			        	  }
			        	  // 计算列指标ID和编码
			        	  var _colId = '' ;
			        	  var _colCode = '' ;
			        	  var _isNew = 'Y' ;
			        	  var colCell = SHEET_API.getCell(SHEET_API_HD, sheetTabId, 1, r.col ) ;
			        	  if((colCell.data != null && colCell.data != "" && colCell.data != "undefined")
			        			  && (colCell.comment != null && colCell.comment != "" && colCell.comment != "undefined")){
			        	    var _cellComment = colCell.comment ;
			        	    var arr = _cellComment.split(">>") ;
			        	    var a = arr[1].split('/') ;
			        	    _colCode = a[0] ;
			        	    _colId = a[1] ;
			        	    _isNew = 'N' ;
			        	    isFirstCreated = false ;
			        	  }
			        	  var cell = SHEET_API.getCell(SHEET_API_HD, sheetTabId, r.row, r.col ) ; // 单元格值和格式信息
			        	  // 如果单元格不为空且不包括批注rowno
			        	  if((val != null && val != "" && val != "undefined") && cell.comment != 'rowno' && flag === 0) {
			        	    var json = {
			        	      'COLITEM_ID' : _colId,
			        	      'COLITEM_CODE' : _colCode,
			        	      'COLITEM_NAME' : val,
			        	      'LANNO' : _lanno ,
			        	      'COLNO' : r.col ,
			        	      'DATATYPE':'3',
			        	      'X' : r.row,
			        	      'Y' : r.col,
			        	      'IS_NEW': _isNew
			        	    } ;
			        	    celldata.push(json) ;
			        	  }		        		  
		        	  }
		        	}) ;

		        	if(count >0){
		        		Ext.Msg.alert("提示",'提取列指标时只能选择其中一行数据');
		        	  	return false ;
		        	}
		        	// 如果第一次创建列指标则在第一行位置创建空行存储列指标信息
		        	if(isFirstCreated){
		        	  	SHEET_API.insertRow(SHEET_API_HD, sheetTabId, 1, 1 ) ;
		        	}

		        	// 加载表格
		        	var colitemWin = getItemWindow('col','列指标',celldata) ;
		        	colitemWin.show() ;		        	
		        }
		    });
		    // 标定数据区
		    var btnRecDataArea = Ext.create('Ext.Button', {
		        text: '标定数据区',
		        style: {
		        	marginTop: '0px',//距顶部高度
		            marginLeft:'2px',//距左边宽度
		            marginRight:'2px'//距右边宽度
		        },
		        width:75,
		        height:20,
		        handler: function(){
		        	// 表格序号
		        	var sheet = SHEET_API_HD.sheet;
		        	var sheetTabId = sheet.getSheetId()　;
		        	// 获取所有带批注的单元格信息:[{sheetId: 1, x: 2, y: 2, comment: "great work"}, ...]
		        	var allCells = SHEET_API.getCellsComment(SHEET_API_HD,sheet.getSheetId());
		        	var rownoArray = [] ;
		        	var rownoYArray = [] ;
		        	Ext.each(allCells,function(r){
		        	  var _comment = r.comment ;
		        	  if(_comment === 'rowno'){
		        	    rownoArray.push(r) ;
		        	  }
		        	}) ;
		        	if (rownoArray.length <= 0) {
		        	  	Ext.Msg.alert("提示", "请标定行次之后再复核列指标。");
		        	  	return ;
		        	}else if(rownoArray.length > 1){
		        		// 按行次列坐标号排序
		        		rownoArray.sort(function(a,b){
			        		if(a.y<b.y){  
			        		  	return -1;  
			        		}else if(a.y>b.y){  
			        		 	return 1;  
			        		}  
			        		return 0; 
		        		}) ;
		        	}
		        	// 计算列次数组
		        	var i = 0 ;
		        	Ext.each(rownoArray, function(r){
		        	  rownoYArray[i] = r.y ;
		        	  i++ ;
		        	}) ;
		        	// 获取选中区域, 返回值[{row: 2, col: 2, val: 100}, {row: 2, col: 3, val: "test"}]
		        	var selectedRange = SHEET_API.getSelectedRangeData(SHEET_API_HD) ;
		        	if(selectedRange.length === 0){
		        		Ext.Msg.alert('请选择列指标区域') ;
		        	  	return false ;
		        	}else{
		        		var cells = [] ;
		        		Ext.each(selectedRange, function(r){
			        		var cell = {} ;
			        		cell.sheet = sheetTabId ;
			        		cell.row = r.row ;
			        		cell.col = r.col ;
			        		var cellJson = SHEET_API.getCell(SHEET_API_HD, sheetTabId, r.row, r.col) ;
			        		// 列指标ID
			        		var colitemId ;
			        		var colCell = SHEET_API.getCell(SHEET_API_HD, sheetTabId, 1, r.col) ;
			        		if(colCell != null && colCell != "" && colCell != "undefined"){
			        		  var _colitem = colCell.comment ;
			        		  if(_colitem !== undefined){
			        			  var arr = _colitem.split(">>") ;
			        			  var a = arr[1].split("/") ;
			        			  colitemId = a[1] ;
			        		  }
			        		}
			        		// 行指标ID
			        		var y ;
			        		var rowitemId ;
			        		Ext.each(rownoYArray, function(ry){
			        		  if(r.col>ry){
			        			y = ry ; 
			        		  }
			        		}) ;
			        		if(y != null && y != "" && y != "undefined"){
			        		  var rowCell = SHEET_API.getCell(SHEET_API_HD, sheetTabId, r.row, y) ;
			        		  var _rowitem = rowCell.comment ;
			        		  if(_rowitem != null && _rowitem != "" && _rowitem != "undefined"){
			        			  var arr = _rowitem.split(">>") ;
			        			  var b = arr[1].split("/") ;
			        			  rowitemId = b[1] ;
			        		  }
			        		}
			        		if((rowitemId != null && rowitemId != "" && rowitemId != "undefined") 
			        				&& (colitemId != null && colitemId != "" && colitemId != "undefined")){
			        			
				        		 cellJson.comment = "data>>"+rowitemId+"/"+colitemId ;
				        		 cellJson.commentEdit = "hide" ;
				        		 cell.json = cellJson ;
				        		 cells.push(cell) ;
			        		}
		        		}) ;

		        		if(cells.length == 0){
		        			Ext.Msg.alert("提示",'在标定数据区域时须先设置行列指标信息!') ;
		        			return false ;
		        		}
		        		SHEET_API.updateCells(SHEET_API_HD, cells, function(){
		        			saveFormat.fireEvent('click') ;	// 执行模板保存处理
		        		},this);
		        	}
		        }
		    });
		    // 清除批注
		    var btnClearComment = Ext.create('Ext.Button', {
		        text: '清除批注',
		        style: {
		        	marginTop: '0px',//距顶部高度
		            marginLeft:'2px',//距左边宽度
		            marginRight:'2px'//距右边宽度
		        },
		        width:65,
		        height:20,
		        handler: function(){
		        	// TODO: 清空选中区域的批注信息
		        	var sheet = SHEET_API_HD.sheet;
		        	var sheetId = sheet.getSheetId();
		        	//选择的区域
		        	var selectedRange = SHEET_API.getSelectedRangeData(SHEET_API_HD);
		        	for (var i=0; i<selectedRange.length; i++) {
		        	  var cell = selectedRange[i];
		        	  SHEET_API.deleteCommentForCoord(SHEET_API_HD, [sheetId, cell.row, cell.col, cell.row, cell.col]);
		        	} 		        	
		        }
		    });		    
		    
		    // 按钮panel
		    var northPanel = Ext.create("Ext.form.Panel", {
		    	id:'north-Panel',
		        height: 30,
		        region: 'north', 
		        layout: 'anchor',
		        frame:true,
		        items: [ 
					saveFormat,
					btnRecOrg,
					btnRecPeriod,
					btnRecRowNo,
					btnRecItem,
					btnRowItem,
					btnColItem,
					btnRecDataArea,
					btnClearComment
				]
		    });
		    
		    //本页面的 viewport
		    Ext.create('Ext.Viewport', {
		    	id:'sheetView',
		        layout: 'border',
		        items: [northPanel, centralPanel],
		        listeners : {
		        	'afterrender' : function(){
		        		loadingMask.hide() ;
		        	}
		        }
		    });
	    });
	</script>
</body>
</html>
