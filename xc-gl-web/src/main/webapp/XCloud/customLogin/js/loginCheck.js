/**
 * use jquery 1.9.0 loginCheck 登录校验
 */
/*
 *设置cookie
 *key --cookie名
 *value -- 值
 *expire --超时时间
 *maxAge --最大时间 
 */
function setCookie(key, value, expire, maxAge) {
			var C = key + "=" + escape(value) + "; ";
			if (typeof (expire) != 'undefined' && expire > 0) {
				var A = new Date();
				A.setTime(A.getTime() + expire * 60 * 1000);
				C += "expires=" + A.toGMTString() + "; ";
				if (typeof (maxAge) != 'undefined') {
					maxAge = expire * 60;
					C += "max-age=" + maxAge;
				}
			}
			document.cookie = C;
		}
/*
 *获取Cookie
 *key --cookie名 
 */
function getCookie(key) {
	var cookies = document.cookie.split("; "), i, l, value;
	for (i = 0, l = cookies.length; i < l; i++) {
		if (cookies[i].split("=")[0] === key) {
			value = unescape(cookies[i].split("=")[1]);
			break;
		}
	}
	return value;
}
/*
 *删除cookie 
 *key --cookie名 
 */
function delCookie(key) {
	var C = new Date();
	C.setTime(C.getTime() - 100000);
	var B = getCookie(key);
	document.cookie = key + "=" + B + "; expires=" + C.toGMTString();
}
$(document).ready(function() {
					$("#loginBtn").bind("click",function() {
										var userName = $.trim($("#CasUsername").val());
										var userPwd = $.trim($("#CasPassword").val());
										var errInfoStr = "";
										if (userName && userName != 'undefined'&& userPwd&& userPwd != 'undefined') {
											var agree = $('#remaindbox')[0].checked;
											var cookieName = 'XC_CLOUD_DEMO_NAME';
											var cookiePwd = 'XC_CLOUD_DEMO_PWD';
											if (agree !== false) {
												setCookie(cookieName,userName,720,1);
												setCookie(cookiePwd,userPwd,720,1);
											} else {
												delCookie(cookieName);
											}
											$.ajax({   type : 'post',
														url : contextPath+'/platformAction.do?method=personalLogin',
														data : {'loginName':userName,'password':userPwd,'isEncryption':'N'},
														dataType:'json',
														success:function(json) {
															var flag = json.flag;
															if(flag == "0"){
																window.location.href = json.url;
															}else{
																$("#errInfo").html(json.msg).show();
																return false;
															}
														}
													});
										} else {
											if (!userName && !userPwd) {
												errInfoStr = "请输入用户名和密码";
											} else if (!userName) {
												errInfoStr = "请输入用户名";
											} else if (!userPwd) {
												errInfoStr = "请输入密码";
											}
											$("#errInfo").html(errInfoStr).show();
											return false;
										}

									});
					$("#CasPassword").bind('keypress',function(event){
			            if(event.keyCode == "13") {
			            	$("#loginBtn").trigger("click");
			            }
					});
				});