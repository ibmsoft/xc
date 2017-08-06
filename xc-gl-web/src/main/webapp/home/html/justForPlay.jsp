<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page language="java" import="java.util.HashMap" %>
<%@ page language="java" import="com.xzsoft.xip.framework.common.SessionVariable" %>
<%
	String contextPath = request.getContextPath();

	HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
	String userName = sessionVars.get(SessionVariable.userName);

%>
<!doctype html>
<html class="no-js" lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <title></title>
    <link rel="stylesheet" type="text/css" href="../css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="../css/main.css">
    
    <link rel="stylesheet" href="<%=contextPath%>/webbuilder/controls/ext/resources/css/ext-all.css" type="text/css">
	<link rel="stylesheet" href="<%=contextPath%>/webbuilder/css/style.css" type="text/css">
    <script type="text/javascript" src="<%=contextPath%>/webbuilder/script/locale/wb-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=contextPath%>/webbuilder/controls/ext/ext-all.js"></script>
	<script type="text/javascript" src="<%=contextPath%>/webbuilder/controls/ext/locale/ext-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=contextPath%>/webbuilder/script/wb.js"></script>
    
    <style type="text/css">

    </style>
    
    <script type="text/javascript">
	    function logout(){
	      Ext.MessageBox.minWidth = 200 ;
	      Ext.MessageBox.confirm('提示', 'Are you sure to logout?', function (opt) {
	        if (opt == 'yes') {
	          var msg = Ext.MessageBox.show({
	            title 	 : 	'',
	            msg 	 : 	'waiting ',
	            width 	 : 	240,
	            wait	 : 	true,
	            progress : 	true,
	            closable : 	false
	          });	
	
	          Ext.Ajax.request({
	            url : '<%=contextPath%>/frameAction.do?method=logout',
	            method: 'POST',
	            success: function(response,options){
	              msg.hide();
	              var temp = response.responseText;
	              var response = Ext.JSON.decode(temp);
	              if(response.flag=='1'){
	                var logoutUrl = response.logoutUrl ;
	                var callbackUrl = response.callbackUrl ;
	                window.location.href = logoutUrl + callbackUrl ;
	
	              }else{
	                Ext.Msg.alert("提示",response.msg);
	              }
	            },
	            failure: function(response,options){
	              msg.hide();
	              Ext.Msg.alert("提示",'failure');
	            }
	          });
	        }
	      });
	    }  
	    
	    function openWin(type){
	    	if(type=='Asset'){
	    		window.open('<%=contextPath%>/demo/IndonesiaStaticData/Indonesia Asset Diagram.html');
	    	} else if(type=='License'){
	    		window.open('<%=contextPath%>/m?xwl=xc/demo/chart/chart02');
	    	} else if(type=='Immigration'){
	    		//window.open('../../demo/IndonesiaPopulation/indonesia.html');
	    		window.open('<%=contextPath%>/m?xwl=xc/demo/demo/demo_test1');
	    	} else if(type=='access'){
	    		//window.open('');
	    	} else if(type=='workflow'){
	    		window.open('http://192.168.200.73:8080/workspace');
	    	} else if(type=='api'){
	    		window.open('http://192.168.200.73:8080/governor');
	    	} else if(type=='pm'){
	    		//window.open('');
	    	} else if(type=='Budget'){
	    		window.open('<%=contextPath%>/m?xwl=xc/demo/demo/demo_test', 'Budget');
	    	}
	    	
	    }
	    
	    function changeRoleAndForward(type){
	    	var roleId = '';
	    	var roleCode = '';
	    	var roleName = '';
	    	
	    	if(type=='Financial_Accounting'){
	    		roleId = '2ed58941-603c-4bc0-930f-ce689bfbb712';
	    		roleCode = 'DEMO_FINACIAL';
		    	roleName = 'FINACIAL ACCOUTING';
	    	}else if(type=='Asset_Management'){
	    		roleId = 'e4688780-633c-48f9-b885-7e73bda69afe';
	    		roleCode = 'DEMO_ASSET';
		    	roleName = 'ASSET';
	    	}else if(type=='Budget_Cash'){
	    		roleId = 'f1de759c-f70d-4520-ae4c-896c2b0bcaa1';
	    		roleCode = 'DEMO_BUDGET&CASH';
		    	roleName = 'BUDGET & CASH';
	    	}else if(type=='Master_Date'){
	    		window.open('<%=contextPath%>/demo/MateData/MateData.html');
		    	return
	    	}else if(type=='access'){
	    		roleId = 'f4ce9188-3e3f-4780-b20d-ba5a0dac7717';
	    		roleCode = 'DEMO_ACCESS';
		    	roleName = 'ACCESS CONTROL';
	    	}else if(type=='header_right_01'){
	    		roleId = '874f736f-b901-4758-a715-13156bfa3cba';
	    		roleCode = 'DEMO_LICENSE';
		    	roleName = 'LICENSE_MANAGEMENT';
	    	}
	    	
	    	Ext.Ajax.request({
                url : '<%=contextPath%>/frameAction.do?method=switchRole',
                method: 'POST',
                params:{"roleId":roleId,"roleName":roleName,"roleCode":roleCode},
                success: function(response,options){
                  var temp = response.responseText;
                  var resp = Ext.JSON.decode(temp);
                  window.open('<%=contextPath%>/m?xwl=xip/pub/navigator/portalNavigator/portalTreeExplorer');
                },
                failure: function(response,options){
                }
              });
	    	
	    }
    </script>
    
</head>
<body>
<div class="page-header clearfix">
    <div class="page-header-left">
        <img src="../images/logo.png">
        <span>KEPOLISIAN NEGARA REPUBLIK INDONESIA</span>
    </div>
    <div class="page-header-right">
        <img src="../images/login.png">
        <span onclick="logout();">Logout</span>
    </div>
</div>
<div class='mini-content container-fluid'>
    <div class="container">
    <div class="row clearfix">
        <div class="col-md-12 column">
            
            <div class="row clearfix">
                <div class="col-md-6 column">
                    <div class="row clearfix page-header-button-left">
                        <div class="col-md-3 column">
                            <img src="../images/Financial_Accounting.png" onclick="changeRoleAndForward('Financial_Accounting')">
                        </div>
                        <div class="col-md-3 column">
                            <img src="../images/Asset_Management.png" onclick="changeRoleAndForward('Asset_Management')">
                        </div>
                        <div class="col-md-3 column">
                            <img src="../images/Budget_Cash.png" onclick="changeRoleAndForward('Budget_Cash')">
                        </div>
                        <% if("DEMO1".equalsIgnoreCase(userName)){%>
	                        <div class="col-md-3 column" style="display:display">
	                            <img src="../images/Master_Date.png" onclick="changeRoleAndForward('Master_Date')">
	                        </div>
                        <%} %>
                    </div>
                </div>
                <div class="col-md-6 column">
                    <div class="row clearfix  page-header-button-right">
                        <div class="col-md-3 column">
                            <img src="../images/header_right_01.png"  onclick="changeRoleAndForward('header_right_01')">
                        </div>
                        <div class="col-md-3 column">
                            <img src="../images/header_right_02.png">
                        </div>
                        <% if("DEMO1".equalsIgnoreCase(userName)){%>
                        <div class="col-md-3 column">
                            <img src="../images/header_right_03.png">
                        </div>
                        <%} %>
                        <div class="col-md-3 column">
                            <img src="../images/header_right_04.png">
                        </div>
                    </div>
                </div>
            </div>
            <div class="row clearfix home_list">
                <div style="width: 75%;display: inline-block;">
                <img src="../images/home.png">
                </div>
                <!-- 
                <div style="width: 24%;display: inline-block;">
                <img src="../images/caculor.png">
                </div> -->
            </div>
            <div class="row clearfix">
                <div class="col-md-6 column table_my">
                    <div class="table_my_title" onclick="openWin('Budget');">Budget</div>
                    <div class="table_my_body">
                    
                    	<img width="555" height="290" src="../images/10011.png"></img>
                    
                    </div>
                </div>
                <div class="col-md-6 column table_my">
                    <div class="table_my_title" onclick="openWin('Asset');">Asset</div>
                    <div class="table_my_body">
                    
                    	<iframe width="555" height="290" src="<%=contextPath%>/demo/IndonesiaStaticData/Indonesia Asset Diagram small.html"></iframe>
                    
                    </div>
                </div>
            </div>
            
            <% if("DEMO1".equalsIgnoreCase(userName)){%>
            
            <div class="row clearfix row_table_my">
                <div class="col-md-6 column table_my">
                    <div class="table_my_title" onclick="openWin('License');">License</div>
                    <div class="table_my_body">
                    
                    	<iframe width="555" height="290" src="<%=contextPath%>/demo/IndonesiaPopulation/driverLicenseOwnship.html"></iframe>
                    
                    </div>
                </div>
                <div class="col-md-6 column table_my">
                    <div class="table_my_title"  onclick="openWin('Immigration');">Police</div>
                    <div class="table_my_body">
                    
                    	<iframe width="555" height="290" src="../../demo/IndonesiaPopulation/indonesia-small.html"></iframe>
                    
                    </div>
                </div>
            </div>
            <%} %>
            
            <hr class="my_hr">
            <div class="row clearfix">
                <div class="col-md-6 column" style="left: calc(50% - 307px);">
                    <div class="row clearfix  page-footer-button-left">
                        <div class="col-md-3 column">
                            <img src="../images/footer_rel_01.png" onclick="changeRoleAndForward('access');">
                        </div>
                        <div class="col-md-3 column">
                            <img src="../images/footer_rel_02.png" onclick="openWin('workflow');">
                        </div>
                        <% if("DEMO1".equalsIgnoreCase(userName)){%>
                        <div class="col-md-3 column">
                            <img src="../images/footer_rel_03.png" onclick="openWin('api');">
                        </div>
                        <div class="col-md-3 column">
                            <img src="../images/footer_rel_04.png" onclick="openWin('pm');">
                        </div>
                        <%} %>
                    </div>
                </div>
            </div>
            
        </div>
    </div>
</div>
    <!-- æ·»å htmlåå®¹çå°æ¹ -->
</div>
<div class="page-footer">
    <small>WEBSITE RESMI KEPOLISIAN NEGARA REPUBLIK INDONESIA (POLRI) TA.2015</small>
</div>
<script type="text/javascript">
    //éæ·»å çJSï¼æ­¤å¤ä¸å¯ä½¿ç¨require
</script>
</body>
</html>
