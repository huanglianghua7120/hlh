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
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import saaf.common.fmw.message.model.inter.server.MessageTemplatesNoticeInfoServer;
import saaf.common.fmw.message.utils.SaafToolUtils;
import saaf.common.fmw.services.CommonAbstractServices;

@Path("/messageTemplatesService")
public class MessageTemplatesNoticeInfoService extends CommonAbstractServices{
private static final Logger LOGGER = LoggerFactory.getLogger(MessageTemplatesNoticeInfoService.class);
	@Context
	private HttpServletRequest request;
	@Context
	private HttpServletResponse response;
	public MessageTemplatesNoticeInfoService() {
		super();
	}

	@POST
	@Path("findMessageTemplatesInfo")
	//@Consumes("application/json")
	@Produces("application/json")
	//	/messageRestServer/messageTemplatesService/findMessageTemplatesInfo
	public String findMessageTemplatesNoticeInfoInfo(@FormParam("params") String postParam,
            @FormParam("pageIndex") @DefaultValue("1") Integer curIndex,
            @FormParam("pageRows") @DefaultValue("10") Integer pageSize) {
		LOGGER.info(postParam);
		
		JSONObject paramJSON = JSON.parseObject(postParam);
		MessageTemplatesNoticeInfoServer messageTemplatesNoticeInfoServer = (MessageTemplatesNoticeInfoServer)SaafToolUtils.context.getBean("messageTemplatesNoticeInfoServer");
		String resultStr = messageTemplatesNoticeInfoServer.findMessageTemplatesNoticeInfoInfo(paramJSON,curIndex,pageSize);
		LOGGER.info(resultStr);
		return resultStr;
	}
	@POST
	@Path("saveMessageTemplatesInfo")
	//@Consumes("application/json")
	@Produces("application/json")
	//	/messageRestServer/messageTemplatesService/saveMessageTemplatesInfo
	public String saveMessageTemplatesNoticeInfoInfo(@FormParam("params") String postParam) {
		LOGGER.info(postParam);
		
		JSONObject paramJSON = JSON.parseObject(postParam);
		MessageTemplatesNoticeInfoServer messageTemplatesNoticeInfoServer = (MessageTemplatesNoticeInfoServer)SaafToolUtils.context.getBean("messageTemplatesNoticeInfoServer");
		String resultStr = messageTemplatesNoticeInfoServer.saveMessageTemplatesNoticeInfoInfo(paramJSON);
		LOGGER.info(resultStr);
		return resultStr;
	}
	@POST
	@Path("deleteMessageTemplatesInfo")
//	@Consumes("application/json")
	@Produces("application/json")
	//	/messageRestServer/messageTemplatesService/deleteMessageTemplatesInfo
	public String deleteMessageTemplatesInfo(@FormParam("params") String postParam) {
		LOGGER.info(postParam);
		
		JSONObject paramJSON = JSON.parseObject(postParam);
		MessageTemplatesNoticeInfoServer messageTemplatesNoticeInfoServer = (MessageTemplatesNoticeInfoServer)SaafToolUtils.context.getBean("messageTemplatesNoticeInfoServer");
		String resultStr = messageTemplatesNoticeInfoServer.deleteMessageTemplatesInfo(paramJSON);
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
