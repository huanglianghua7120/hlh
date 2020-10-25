package saaf.common.fmw.message.services;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
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

import saaf.common.fmw.message.model.inter.server.MessageServerInfoServer;
import saaf.common.fmw.message.utils.SaafToolUtils;
import saaf.common.fmw.services.CommonAbstractServices;

@Path("/MessageServerInfoService")
public class MessageServerInfoService extends CommonAbstractServices{
private static final Logger LOGGER = LoggerFactory.getLogger(MessageServerInfoService.class);
	@Context
	private HttpServletRequest request;
	@Context
	private HttpServletResponse response;
	public MessageServerInfoService() {
		super();
	}

	@POST
	@Path("findMessageServerInfo")
//	@Consumes("application/json")
	@Produces("application/json")
	//	/messageRestServer/MessageServerInfoService/findMessageServerInfo
	public String findMessageServerInfo(@FormParam("params") String postParam,
            @FormParam("pageIndex") @DefaultValue("1") Integer curIndex,
            @FormParam("pageRows") @DefaultValue("10") Integer pageSize) {
		LOGGER.info(postParam);	
		JSONObject paramJSON = JSON.parseObject(postParam);
		MessageServerInfoServer messageserverInfoServer = (MessageServerInfoServer)SaafToolUtils.context.getBean("messageServerInfoServer");		
		String resultStr = messageserverInfoServer.findMessageInfoList(paramJSON,curIndex,pageSize);
		LOGGER.info(resultStr);
		return resultStr;
	}
	@POST
	@Path("saveMessageServerInfo")
	//@Consumes("application/json")
	@Produces("application/json")
	//	/messageRestServer/MessageServerInfoService/saveMessageServerInfo
	public String saveMessageServerInfo(@FormParam("params") String postParam) {
		LOGGER.info(postParam);
		
		JSONObject paramJSON = JSON.parseObject(postParam);
		
		//queryParamJSON.getJSONArray("detailList");
		MessageServerInfoServer messageserverInfoServer = (MessageServerInfoServer)SaafToolUtils.context.getBean("messageServerInfoServer");
		String resultStr = messageserverInfoServer.saveMessageAllInfo(paramJSON);
		LOGGER.info(resultStr);
		return resultStr;
	}
	
	@POST
	@Path("delteteMessageServerInfo")
	//@Consumes("application/json")
	@Produces("application/json")
	//	/messageRestServer/MessageServerInfoService/delteteMessageServerInfo
	public String deleteMessageServerInfo(@FormParam("params") String postParam) {
		LOGGER.info(postParam);
		
		JSONObject paramJSON = JSON.parseObject(postParam);
		MessageServerInfoServer messageserverInfoServer = (MessageServerInfoServer)SaafToolUtils.context.getBean("messageServerInfoServer");
		String resultStr = messageserverInfoServer.deleteMessageInfo(paramJSON);
		LOGGER.info(resultStr);
		return resultStr;
	}
	@GET
	@Path("user")
	@Produces("text/plain")
	//	/messageRestServer/messageTemplatesNoticeInfoService/user
	public String getUser(@QueryParam("name") String name) {
		return "hello " + name;
	}
}
