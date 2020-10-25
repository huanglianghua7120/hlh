package saaf.common.fmw.message.services;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import saaf.common.fmw.message.model.inter.server.MessageSendMarkInfoServer;
import saaf.common.fmw.message.utils.SaafToolUtils;
import saaf.common.fmw.services.CommonAbstractServices;

@Path("/MessageSendMarkInfoService")
public class MessageSendMarkInfoService extends CommonAbstractServices{
private static final Logger LOGGER = LoggerFactory.getLogger(MessageSendMarkInfoService.class);
	@Context
	private HttpServletRequest request;
	@Context
	private HttpServletResponse response;
	

	
	public MessageSendMarkInfoService() {
		super();
	}
	
    private Map<String,String> getSessionMap(){
    	Map<String, String> sessionMap = new HashMap<String, String>();
       /* sessionMap.put("instId", this.getInstIdData());
        sessionMap.put("platformCode", this.getPlatformCode()!=null?this.getPlatformCode():"SAAF");
        sessionMap.put("userName", this.getUserName());
        sessionMap.put("userId", String.valueOf(this.getSessionUserId()));
        sessionMap.put("memberId", String.valueOf(this.getMemberId()));
        sessionMap.put("isAdmin", this.getIsAdmin());*/
        
    	sessionMap.put("instId", "-9999");
        sessionMap.put("platformCode", "SAAF");
        sessionMap.put("userName", "-9999");
        sessionMap.put("userId", "-1");
        sessionMap.put("memberId", "-9999");
        sessionMap.put("isAdmin", "Y");
        return sessionMap;
    }

	@POST
	@Path("findSendMarkInfo")
	//@Consumes("application/json")
	@Produces("application/json")
	//	/messageRestServer/MessageSendMarkInfoService/findSendMarkInfo
	public String findSendMarkInfo(@FormParam("params") String postParam,
            @FormParam("pageIndex") @DefaultValue("1") Integer curIndex,
            @FormParam("pageRows") @DefaultValue("10") Integer pageSize) {
		LOGGER.info(postParam);
		
		JSONObject paramJSON = JSON.parseObject(postParam);
		MessageSendMarkInfoServer markServer = (MessageSendMarkInfoServer)SaafToolUtils.context.getBean("messageSendMarkInfoServer");
		String resultStr = markServer.findMessageSendMarkInfoInfo(paramJSON,curIndex,pageSize);
		LOGGER.info(resultStr);
		return resultStr;
	}
	@POST
	@Path("saveMultiSendInfo")
//	@Consumes("application/json")
	@Produces("application/json")
	//	/messageRestServer/MessageSendMarkInfoService/saveMultiSendInfo
	public String saveMultiSendMessageInfo(@FormParam("params") String postParam) {
		LOGGER.info(postParam);
	     //ShortMessageSender shortMessageSender = (ShortMessageSender)SaafToolUtils.context.getBean("shortMessageSender");
		MessageSendMarkInfoServer markServer = (MessageSendMarkInfoServer)SaafToolUtils.context.getBean("messageSendMarkInfoServer");
	    JSONObject paramJSON = JSON.parseObject(postParam);
	    Map<String, String> sessionMap = this.getSessionMap();
	    String resultStr=markServer.saveMultiSendMessageInfo(paramJSON,sessionMap); 
	
		return resultStr;
	}
	@POST
	@Path("saveBaiduPushSendInfo")
//	@Consumes("application/json")
	@Produces("application/json")
	//	/messageRestServer/MessageSendMarkInfoService/saveBaiduPushSendInfo
	public String saveBaiduPushSendInfo(@FormParam("params") String postParam) {
		LOGGER.info(postParam);
	     //ShortMessageSender shortMessageSender = (ShortMessageSender)SaafToolUtils.context.getBean("shortMessageSender");
		MessageSendMarkInfoServer markServer = (MessageSendMarkInfoServer)SaafToolUtils.context.getBean("messageSendMarkInfoServer");
	    JSONObject paramJSON = JSON.parseObject(postParam);
	    Map<String, String> sessionMap = this.getSessionMap();
	    String resultStr=markServer.saveBaiduPushSendInfo(paramJSON,sessionMap); 
	
		return resultStr;
	}
	  /**
     * 推送通知 站内消息
     *
     * @param params
     * @return
     */
    @POST
    @Path("pushNoticeToUser")
	public String pushNoticeToUser(@FormParam("params") String postParam) {
		LOGGER.info(postParam);

		MessageSendMarkInfoServer markServer = (MessageSendMarkInfoServer) SaafToolUtils.context
				.getBean("messageSendMarkInfoServer");
		  JSONObject paramJSON = JSON.parseObject(postParam);
		String resultStr=null;
		try {
			Map<String, String> sessionMap = this.getSessionMap();
			resultStr = markServer.savePushOfNoticeInfo(paramJSON,sessionMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return resultStr;

	}
	/*@POST
	@Path("saveSendBaiduPushInfo")
//	@Consumes("application/json")
	@Produces("application/json")
	//	/messageRestServer/MessageSendMarkInfoService/saveMultiSendInfo
	public String saveSendBaiduPushInfo(@FormParam("params") String postParam) {
		LOGGER.info(postParam);
	     //ShortMessageSender shortMessageSender = (ShortMessageSender)SaafToolUtils.context.getBean("shortMessageSender");
		MessageSendMarkInfoServer markServer = (MessageSendMarkInfoServer)SaafToolUtils.context.getBean("messageSendMarkInfoServer");
	    JSONObject paramJSON = JSON.parseObject(postParam);
	    Map<String, String> sessionMap = this.getSessionMap();
	    String resultStr=markServer.saveSendBaiduPushInfo(paramJSON,sessionMap); 
	
		return resultStr;
	}*/
	@GET
	@Path("user")
	@Produces("text/plain")
	//	/messageRestServer/MessageSendMarkInfoService/user
	public String getUser(@QueryParam("name") String name) {
		return "hello " + name;
	}
}
