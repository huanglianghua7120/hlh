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

import saaf.common.fmw.message.model.inter.server.MessageServerDetailInfoServer;
import saaf.common.fmw.message.model.inter.server.MessageTemplatesNoticeInfoServer;
import saaf.common.fmw.message.utils.SaafToolUtils;
import saaf.common.fmw.services.CommonAbstractServices;

@Path("/MessageServerDetailInfoService")
public class MessageServerDetailInfoService extends CommonAbstractServices{
private static final Logger LOGGER = LoggerFactory.getLogger(MessageServerDetailInfoService.class);
	@Context
	private HttpServletRequest request;
	@Context
	private HttpServletResponse response;
	public MessageServerDetailInfoService() {
		super();
	}

	@POST
	@Path("findMessageServerDetailInfo")
	//@Consumes("application/json")
	@Produces("application/json")
	//	/messageRestServer/MessageServerDetailInfoService/findMessageTemplatesNoticeInfoInfo
	public String findMessageTemplatesNoticeInfoInfo(@FormParam("params") String postParam,
            @FormParam("pageIndex") @DefaultValue("1") Integer curIndex,
            @FormParam("pageRows") @DefaultValue("10") Integer pageSize) {
		LOGGER.info(postParam);
	//	System.out.println("getUserSessionBean().getUserId()="+getUserSessionBean().getUserId());
		JSONObject paramJSON = JSON.parseObject(postParam);
		MessageServerDetailInfoServer messageServerDetailInfoServer = (MessageServerDetailInfoServer)SaafToolUtils.context.getBean("messageServerDetailInfoServer");
		String resultStr = messageServerDetailInfoServer.findMessageDetailInfoList(paramJSON,curIndex,pageSize);
		LOGGER.info(resultStr);
		return resultStr;
	}
	@POST
	@Path("saveMessageServerDetailInfo")
	//@Consumes("application/json")
	@Produces("application/json")
	//	/messageRestServer/MessageServerDetailInfoService/saveMessageServerDetailInfo
	public String saveMessageServerDetailInfo(@FormParam("params") String postParam) {
		LOGGER.info(postParam);
		JSONObject paramJSON = JSON.parseObject(postParam);
		MessageServerDetailInfoServer messageServerDetailInfoServer = (MessageServerDetailInfoServer)SaafToolUtils.context.getBean("messageServerDetailInfoServer");
		String resultStr = messageServerDetailInfoServer.saveMessageDetailInfoInfo(paramJSON);
		LOGGER.info(resultStr);
		return resultStr;
	}
	@POST
	@Path("deleteMessageServerDetailInfo")
	//@Consumes("application/json")
	@Produces("application/json")
	//	/messageRestServer/MessageServerDetailInfoService/deleteMessageServerDetailInfo
	public String deleteMessageServerDetailInfo(@FormParam("params") String postParam) {
		LOGGER.info(postParam);
		JSONObject paramJSON = JSON.parseObject(postParam);
		MessageServerDetailInfoServer messageServerDetailInfoServer = (MessageServerDetailInfoServer)SaafToolUtils.context.getBean("messageServerDetailInfoServer");
		String resultStr = messageServerDetailInfoServer.deleteMessageDetailInfo(paramJSON);
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
