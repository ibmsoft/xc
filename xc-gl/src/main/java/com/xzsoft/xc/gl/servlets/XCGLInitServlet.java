package com.xzsoft.xc.gl.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;

import com.xzsoft.xc.gl.common.XCGLThreadPool;
//import com.xzsoft.xc.gl.common.XCGLThreadPool;
import com.xzsoft.xc.gl.dao.XCGLCommonDAO;
import com.xzsoft.xc.gl.modal.AccHrcy;
import com.xzsoft.xc.gl.modal.LedgerSegment;
import com.xzsoft.xc.gl.util.XCGLCacheUtil;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.common.XConstants;
import com.xzsoft.xip.framework.util.AppContext;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.WFUtil;

/**
 * @ClassName: CacheBaseDataServlet 
 * @Description: 执行总账系统数据库缓存服务
 * @author linp
 * @date 2015年12月30日 上午11:13:42 
 *
 */
public class XCGLInitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// 日志记录器
	private static final Logger log = Logger.getLogger(XCGLInitServlet.class.getName()) ;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public XCGLInitServlet() {
        super();
    }

    /**
     * 系统启动时执行
     */
    public void init() throws ServletException {
		// 系统启动时是否将数据同步到Redis: Y-执行,N-不执行
    	String isCache = this.getInitParameter("isCacheRedis") ;
		if("Y".equals(isCache)){
			// 启动线程执行初始化处理
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						// 判断Redis是否开启
						boolean isUsedRedis = Boolean.parseBoolean(PlatformUtil.getXipConfigVal("xip.useRedis")) ;
						if(!isUsedRedis){
							//throw new Exception("当前系统未开启Redis服务, 请修改config.properties文件, 重启系统生效!") ;
							throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_DQXTWKQRFW", null));
						}else{
							// 判断Redis数据库是否配置正确或是否已经开启服务
							if(PlatformUtil.getRedisTemplate() == null){
								//throw new Exception("未获取到Redis数据库连接, 原因可能为：1.系统中Redis数据库连接信息配置不正确; 2.Redis数据库服务为启动; 3.应用系统与Redis数据库之间网络不通畅。 请检查!") ;
								throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_WHQDRSJKLJ", null));
							}else{
								log.info("基础数据初始化处理开始");
								
								// 执行数据缓存处理
								doCache2Redis(null, "ALL");
								
								log.info("基础数据初始化处理完成");
							}
						}
					} catch (Exception e) {
						log.error(e.getMessage());
					}
				}
			}).start() ;
		}
		
		// 是否初始化线程池
		String isCreateThreadPool = this.getInitParameter("isCreateThreadPool") ;
		if("Y".equals(isCreateThreadPool)){
			try {
				XCGLThreadPool.initExecutors() ;
			} catch (Exception e) {
				log.error("线程池创建失败："+e.getMessage());
			}
		}
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * 执行云ERP总账系统基础数据缓存服务
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// 供外部调用
			String method = request.getParameter("method") ;
			if(method.equals("doCache2Redis")){
				String chkItems = request.getParameter("chkItems") ;
				String ledgerId = request.getParameter("ledgerId") ;
				// 加载系统基础数据到Redis数据库
				String json = this.doCache2Redis(chkItems, ledgerId);
				
				WFUtil.outPrint(response, json);
				
			}else if(method.equals("doCacheAccHrcy2Redis")){
				// 加载会计科目体系信息到Redis
				String hrcyId = request.getParameter("hrcyId") ;
				this.doCacheAccHrcy2Redis(hrcyId);
				
			}else if(method.equals("doCacheSharedSegments2Redis")){
				// 加载共享辅助核算信息到Redis
				String segCode = request.getParameter("segCode") ;
				this.doCacheSharedSegments2Redis(segCode);
				
			}else if(method.equals("doCachePrivateSegments2Redis")){
				// 加载私有辅助核算段信息到Redis
				String ledgerId = request.getParameter("ledgerId") ;
				String segCode = request.getParameter("segCode") ;
				this.doCachePrivateSegments2Redis(ledgerId, segCode);
				
			}else{
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	/**
	 * @Title: doCacheSharedSegments2Redis 
	 * @Description: 将共享辅助段信息同步到Redis数据库
	 * @param segCode
	 * @throws Exception
	 */
	public void doCacheSharedSegments2Redis(String segCode)throws Exception {
		XCGLCacheUtil.syncBaseDataToRedis(null, null, segCode);			
	}
	
	/**
	 * @Title: doCachePrivateSegments2Redis 
	 * @Description: 将私有辅助核算段的信息同步到Redis数据库
	 * @throws Exception
	 */
	public void doCachePrivateSegments2Redis(String ledgerId, String segCode)throws Exception {
		XCGLCacheUtil.syncBaseDataToRedis(ledgerId, null, segCode);		
	}
	
	/**
	 * @Title: doCacheAccHrcy2Redis 
	 * @Description: 缓存会计科体系
	 * @throws Exception
	 */
	public void doCacheAccHrcy2Redis(String hrcyId)throws Exception {
		XCGLCacheUtil.syncBaseDataToRedis(null, hrcyId, XConstants.XC_GL_ACCOUNTS);	
	}
	
	/**
	 * @Title: doCacheCCID2Redis 
	 * @Description: 将CCID缓存到Redis数据库
	 * @throws Exception
	 */
	public void doCacheCCID2Redis(String ledgerId)throws Exception{
		XCGLCacheUtil.syncCCID2Redis(ledgerId);
	}
	
	/**
	 * @Title: doCacheCashItems2Redis 
	 * @Description: 将现金流量项目信息缓存到Redis数据库
	 * @throws Exception    设定文件
	 */
	public void doCacheCashItems2Redis()throws Exception{
		XCGLCacheUtil.syncCashItem2Redis();
	}
	
	/**
	 * @Title: doCacheLedgers 
	 * @Description: 将账簿信息缓存到Redis数据库
	 * @throws Exception    设定文件
	 */
	public void doCacheLedgers(String ledgerId)throws Exception{
		XCGLCacheUtil.syncLedgers(ledgerId);
	}
	
	/**
	 * @Title: doCacheAllVCategory 
	 * @Description: 将凭证分类信息缓存到Redis数据库
	 * @throws Exception    设定文件
	 */
	public void doCacheAllVCategory()throws Exception{
		XCGLCacheUtil.syncVCategory(); 
	}
	
	/**
	 * @Title: doCache2Redis 
	 * @Description: 加载系统基础数据到Redis数据库
	 * @throws Exception
	 */
	public String doCache2Redis(String paramJson,String ledgerId)throws Exception {
		JSONObject returnJo = new JSONObject() ;
		
		try {
			// 按所选项目进行同步
			JSONObject jo = null ;
			if(paramJson != null && !"".equals(paramJson)){
				jo = new JSONObject(paramJson) ; 
			}
			
			// 获取数据接入层接口
			XCGLCommonDAO xcglCommonDAO = (XCGLCommonDAO) AppContext.getApplicationContext().getBean("xcglCommonDAO") ;
			
			// 将会计科目体系信息同步到Redis数据库
			if(jo == null || Boolean.parseBoolean(jo.getString("accounts"))){
				List<AccHrcy> hrcys = xcglCommonDAO.getAllAccHrcys() ;
				if(hrcys != null && hrcys.size()>0){
					for(AccHrcy hrcy : hrcys){
						// 将非预制的科目体系同步到Redis数据库
						if(!"Y".equals(hrcy.getIsPrepared())){
							this.doCacheAccHrcy2Redis(hrcy.getAccHrcyId());
						}
					}
				}
			}
			
			// 将共享辅助段信息同步到Redis数据库
			if(jo == null || Boolean.parseBoolean(jo.getString("vendors"))){
				this.doCacheSharedSegments2Redis(XConstants.XC_AP_VENDORS);		// 供应商
			}
			if(jo == null || Boolean.parseBoolean(jo.getString("customers"))){
				this.doCacheSharedSegments2Redis(XConstants.XC_AR_CUSTOMERS);	// 客户
			}
			if(jo == null || Boolean.parseBoolean(jo.getString("products"))){
				this.doCacheSharedSegments2Redis(XConstants.XC_GL_PRODUCTS);	// 产品
			}
			if(jo == null || Boolean.parseBoolean(jo.getString("orgs"))){
				this.doCacheSharedSegments2Redis(XConstants.XIP_PUB_ORGS);		// 内部往来
			}
			if(jo == null || Boolean.parseBoolean(jo.getString("emps"))){
				this.doCacheSharedSegments2Redis(XConstants.XIP_PUB_EMPS);		// 个人往来
			}
			if(jo == null || Boolean.parseBoolean(jo.getString("cashitem"))){
				this.doCacheCashItems2Redis();									// 现金流量项目
			}
			if(jo == null || Boolean.parseBoolean(jo.getString("category"))){
				this.doCacheAllVCategory();										// 凭证分类
			}
			
			// 将私有辅助段信息同步到Redis数据库
			List<LedgerSegment> segments = xcglCommonDAO.getLedgerSemgments(ledgerId) ;
			if(segments != null && segments.size()>0){
				for(LedgerSegment seg : segments){
					String ldId = seg.getLedgerId() ;
					String segCode = seg.getSegCode() ;
					if(segCode.equals(XConstants.XC_AP_VENDORS)
							|| segCode.equals(XConstants.XC_AR_CUSTOMERS)
							|| segCode.equals(XConstants.XC_GL_PRODUCTS)
							|| segCode.equals(XConstants.XIP_PUB_ORGS)
							|| segCode.equals(XConstants.XIP_PUB_EMPS)){
						// 排除共享辅助核算段
						continue ;
						
					}else{
						if(jo == null){
							this.doCachePrivateSegments2Redis(ldId, segCode);
							
						}else{
							boolean depts = Boolean.parseBoolean(jo.getString("depts")) ;
							boolean projects = Boolean.parseBoolean(jo.getString("projects")) ;
							boolean custom1 = Boolean.parseBoolean(jo.getString("custom1")) ;
							boolean custom2 = Boolean.parseBoolean(jo.getString("custom2")) ;
							boolean custom3 = Boolean.parseBoolean(jo.getString("custom3")) ;
							boolean custom4 = Boolean.parseBoolean(jo.getString("custom4")) ;
							
							if((depts && segCode.equals(XConstants.XIP_PUB_DETPS))
									|| (projects && segCode.equals(XConstants.XC_PM_PROJECTS))
									|| (custom1 && segCode.equals(XConstants.XC_GL_CUSTOM1))
									|| (custom2 && segCode.equals(XConstants.XC_GL_CUSTOM2))
									|| (custom3 && segCode.equals(XConstants.XC_GL_CUSTOM3))
									|| (custom4 && segCode.equals(XConstants.XC_GL_CUSTOM4))){
								
								this.doCachePrivateSegments2Redis(ldId, segCode);
							}
						}
					}
				}
			}
			
			// 将账簿信息缓存到Redis数据库
			if(jo == null || Boolean.parseBoolean(jo.getString("ledgers"))){
				this.doCacheLedgers(ledgerId);
			}
			// 将CCID信息同步到Redis数据库
			if(jo == null || Boolean.parseBoolean(jo.getString("ccid"))){
				this.doCacheCCID2Redis(ledgerId);
			}
			
			// 提示信息
			returnJo.put("flag", "0") ;
			//returnJo.put("msg", "数据同步成功") ;
			returnJo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_GL_SJTBCG", null)) ;
		} catch (Exception e) {
			// 提示信息
			returnJo.put("flag", "1") ;
			//returnJo.put("msg", "数据同步失败："+e.getMessage()) ;
			returnJo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_GL_SJTBSB", null) +e.getMessage()) ;
		}
		
		return returnJo.toString() ;
	}
	
}
