package saaf.common.fmw.message.model.inter.server;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.rpc.ServiceException;

import org.codehaus.jettison.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.baidu.yun.push.auth.PushKeyPair;
import com.baidu.yun.push.client.BaiduPushClient;
import com.baidu.yun.push.constants.BaiduPushConstants;
import com.baidu.yun.push.exception.PushClientException;
import com.baidu.yun.push.exception.PushServerException;
import com.baidu.yun.push.model.PushBatchUniMsgRequest;
import com.baidu.yun.push.model.PushBatchUniMsgResponse;
import com.yhg.base.utils.SToolUtils;
import com.yhg.base.utils.StringUtils;
import com.yhg.hibernate.core.paging.Pagination;

import saaf.common.fmw.base.model.dao.SaafMessageHeadDAO_HI;
import saaf.common.fmw.base.model.entities.SaafMessageHeadEntity_HI;
import saaf.common.fmw.base.model.entities.readonly.SaafUserEmployeesEntity_HI_RO;
import saaf.common.fmw.base.model.inter.ISaafMessage;
import saaf.common.fmw.base.model.inter.ISaafResponsibilitys;
import saaf.common.fmw.base.model.inter.ISaafUserResp;
import saaf.common.fmw.base.model.inter.server.SaafUsersServer;
import saaf.common.fmw.message.model.dao.MessageSendMarkInfoDAO_HI;
import saaf.common.fmw.message.model.dao.MessageTemplatesNoticeInfoDAO_HI;
import saaf.common.fmw.message.model.entities.MessageSendMarkInfoEntity_HI;
import saaf.common.fmw.message.model.entities.MessageTemplatesNoticeInfoEntity_HI;
import saaf.common.fmw.message.model.inter.IMessageSendMarkInfo;
import saaf.common.fmw.report.model.inter.server.SaafDynamicQuerySetServer;
import saaf.common.fmw.report.model.inter.server.SaafQueryDynamicReportServer;

@Component("messageSendMarkInfoServer")
public class MessageSendMarkInfoServer implements IMessageSendMarkInfo {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageSendMarkInfoServer.class);
    @Autowired
    private MessageSendMarkInfoDAO_HI messageSendMarkInfoDAO_HI;
    @Autowired
    private MessageTemplatesNoticeInfoDAO_HI messageTemplatesDAO_HI;
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private ShortMessageSender shortMessageSender;
    @Autowired
    private WeiXinSender weiXinSender;
	@Autowired
	private SaafQueryDynamicReportServer saafQueryDynamicReportServer;
	@Autowired
	private SaafDynamicQuerySetServer saafDynamicQuerySetServer;
	@Autowired
	private SaafUsersServer saafUsersServer;
	@Autowired
    private ISaafMessage saafMessageServer;

    @Autowired
    private SaafMessageHeadDAO_HI saafMessageHeadDAO_HI;
    
    @Autowired
    private MessageServerDetailInfoServer msDetailInfoServer;
    
    @Autowired
    private ISaafUserResp baseSaafUserRespServer;
	
 

    public MessageSendMarkInfoServer() {
        super();
    }

    public String findMessageSendMarkInfoInfo(JSONObject queryParamJSON,Integer curIndex,Integer pageSize) {
       // Map<String, Object> queryParamMap = SToolUtils.fastJsonObj2Map(queryParamJSON);
        MessageSendMarkInfoEntity_HI msEntity_HI = JSON.parseObject(queryParamJSON.toString(),
        		MessageSendMarkInfoEntity_HI.class);
        StringBuffer sbhql =new StringBuffer();
        sbhql.append("from MessageSendMarkInfoEntity_HI s where 1=1 ");
 /*       if(null!=queryParamJSON.getString("msmId")) {
        	sbhql.append(" and  s.msmId=:msmId");
        	
        }*/
        Map<String, Object> map =new HashMap<String, Object>();
      //利用java反射机制 
		 Field[] fields = msEntity_HI.getClass().getDeclaredFields();
		 for(Field field:fields) {
			 String fieldName = field.getName();
			 if(StringUtils.isEmpty(queryParamJSON.getString(fieldName)))
				 continue;
			 if("msmId".equals(fieldName))//前端详情查询某一条数据
				 sbhql.append(" and  s."+fieldName+" = "+queryParamJSON.getString(fieldName)+" ");
			 else
				 sbhql.append(" and  s."+fieldName+" like '%"+queryParamJSON.getString(fieldName)+"%' ");
		 }
		 sbhql.append(" order by s.msmId desc  ");
		 Pagination<MessageSendMarkInfoEntity_HI> findListResult = messageSendMarkInfoDAO_HI.findPagination(sbhql, map, curIndex,pageSize);
        JSONObject jsonObject = JSONObject.parseObject( JSON.toJSONString(findListResult));
		jsonObject.put("status", "S");
		String resultData = JSON.toJSONString(jsonObject);
		System.out.println(resultData);
        return resultData.toString();
    }
    
    public String saveMessageSendMarkInfoInfo(MessageSendMarkInfoEntity_HI messageSendMarkInfoEntity_HI) {
        Object resultData = messageSendMarkInfoDAO_HI.save(messageSendMarkInfoEntity_HI);
        JSONObject resultStr = SToolUtils.convertResultJSONObj("S", "", 1, resultData);
        return resultStr.toString();
    }

    public String saveMessageSendMarkInfoInfo(JSONObject queryParamJSON) {
        MessageSendMarkInfoEntity_HI messageSendMarkInfoEntity_HI = JSON.parseObject(queryParamJSON.toString(), MessageSendMarkInfoEntity_HI.class);
        Object resultData = messageSendMarkInfoDAO_HI.save(messageSendMarkInfoEntity_HI);
        JSONObject resultStr = SToolUtils.convertResultJSONObj("S", "", 1, resultData);
        return resultStr.toString();
    }
 
    
    public String replaceContent(String TemplatesContent,String querySetNum
       , /*JSONObject jsonParams,*/String msmReceiverAccounts,
			Map<String, String> sessionBean) {
		// 标题 表体信息
		JSONObject object = new JSONObject();
		object.clear();
		object.put("querySetNum", querySetNum);
		String lineList = saafDynamicQuerySetServer.findLinesList(object, 1, 100);
		JSONObject jsonObjectLine = JSONObject.parseObject(lineList);
		JSONArray lineListResult = JSON.parseArray(jsonObjectLine.getString("data"));// 所有字段名字

		// key-value值
		Map<String, String> map = new HashMap<String, String>();
		if (null != lineListResult && lineListResult.size() > 0)
			for (int i = 0; i < lineListResult.size(); i++) {
				map.put("column" + (i + 1), lineListResult.getJSONObject(i).getString("columnName"));
			}
		else
			return "false";
		// 内容 查最多一条数据
		object.clear();
		object.put("default_var_equal_querysomeparam", msmReceiverAccounts);
		String paramJSONlist = saafQueryDynamicReportServer.findList(querySetNum, object, sessionBean, 1, 100);
		JSONObject jsonObject = JSONObject.parseObject(paramJSONlist);
		JSONArray findListResult = JSON.parseArray(jsonObject.getString("data"));
		if (null == findListResult || findListResult.size() == 0)
			return "false";
		// 遍历行数据 根据key-value值 替换发送内容相关字段
		String key = "";
		Object value = null;
		for (Entry<String, Object> entry : findListResult.getJSONObject(0).entrySet()) {

			value = entry.getValue();
			key = entry.getKey();

			if (TemplatesContent.indexOf(map.get(key)) >= 0) {
				TemplatesContent = TemplatesContent.replaceAll("@#" + map.get(key)+"#",
						findListResult.getJSONObject(0).getString(key));
				TemplatesContent = TemplatesContent.replaceAll("#" + map.get(key)+"#",
						findListResult.getJSONObject(0).getString(key));
				TemplatesContent = TemplatesContent.replaceAll("@" + map.get(key),
						findListResult.getJSONObject(0).getString(key));

			}
		}

		return TemplatesContent;
	}
    
	public String saveMultiSendMessageInfo(JSONObject queryParamJSON,Map<String, String> sessionMap) {
		String[] msmReceiverAccounts = new String[1];

		if (null != queryParamJSON.getJSONArray("pushList") && queryParamJSON.getJSONArray("pushList").size() > 0) {
			MessageTemplatesNoticeInfoEntity_HI templateEntity_HI = JSON.parseObject(queryParamJSON.getString("template"),
					MessageTemplatesNoticeInfoEntity_HI.class);
			//逻辑 根据某个接口获取对应的数据 替换模板中的占位符号 
			//根据templateEntity_HI.getMtValuesetCode()查到对应的数据
			//JSONObject jsonParams = parameters.getJSONObject("queryParams");
            JSONObject parameters =new JSONObject();
			
	        
			JSONArray array = queryParamJSON.getJSONArray("pushList");
			Iterator<Object> it = array.iterator();
			String content=null;
			while (it.hasNext()) {
				try {
					msmReceiverAccounts[0] = JSONObject.parseObject(JSON.toJSONString(it.next())).getString("msmReceiverAccount");
					/*parameters.clear();
					parameters.put("default_var_equal_querysomeparam", msmReceiverAccounts[0]);*/
					
					content=replaceContent(templateEntity_HI.getTemplatesContent(),
							templateEntity_HI.getMtValuesetCode(), msmReceiverAccounts[0], sessionMap);
					
					
					if (isMobileNO(msmReceiverAccounts[0])) {
						if("false".equals(content)){
							//没有会员信息 没办法替换信息 则插入发生失败
							String msmSendStatus = "失败"; 
							saveMessageSendMark(msmReceiverAccounts, templateEntity_HI.getTemplatesTitle(),
									templateEntity_HI.getTemplatesContent(), msmSendStatus,"短信");
							continue;
							
						}
						//过滤html代码
						content=deleteAllHTMLTag(content);
						// 手机
						shortMessageSender.smsSendSingleMsg(msmReceiverAccounts, templateEntity_HI.getTemplatesTitle(),
								content,
								false);

					} else if (isEmail(msmReceiverAccounts[0])) {
						if("false".equals(content)){
							//没有会员信息 没办法替换信息 则插入发生失败
							String msmSendStatus = "失败"; 
							saveMessageSendMark(msmReceiverAccounts, templateEntity_HI.getTemplatesTitle(),
									templateEntity_HI.getTemplatesContent(), msmSendStatus,"邮件");
							continue;
							
						}
						// 邮件
						org.codehaus.jettison.json.JSONObject json = new org.codehaus.jettison.json.JSONObject();

						json.put("SUBJECT", templateEntity_HI.getTemplatesTitle());
						json.put("CONTENT", content);
						org.codehaus.jettison.json.JSONArray jsonArrayTo = new org.codehaus.jettison.json.JSONArray();
						// jsonArrayTo.put("lindezhao@chinasie.com");
						jsonArrayTo.put(msmReceiverAccounts[0]);
						json.put("TO_LIST", jsonArrayTo);

						emailSender.sendEmail(json.toString(), false);

					} else {

						// 微信
					/*	TextMessageBean message = new TextMessageBean();
						message.setTouser("oWyzd1Y1gjNCvX55fmbnYsmfzwbk");
						message.setCreateTime(System.currentTimeMillis());
						message.setMsgtype("text");
						message.setText(new ContentMessage(replaceContent(templateEntity_HI.getTemplatesContent(),
								templateEntity_HI.getMtValuesetCode(), parameters, sessionMap)));
						weiXinSender.sendTextMessage2OpenUser(message);*/

					}
				} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} /*catch (KeyManagementException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} */catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

			}
			}
			
		}

		JSONObject resultStr = SToolUtils.convertResultJSONObj("S", "", 1, "send success");
		return String.valueOf(resultStr);

	}
	
	
	  @Override
	    public String savePushOfNoticeInfo(JSONObject paramJSON,Map<String, String> sessionMap)
 throws Exception {
		String messageCode = new Date().getTime() + "";// SaafToolUtils.getBillNumber("SAAF_MESSAGE_PUSH");
		int userId = -9999; // 待办都是后台发送的消息，一次设置用户为-1
		String url = null;
		JSONObject resultStr = null;
		String content = null;
		StringBuffer hql = new StringBuffer();
		String[] msmReceiverAccounts = new String[1];

		LOGGER.info("savePushOfNoticeInfo=" + paramJSON);
		MessageTemplatesNoticeInfoEntity_HI templateEntity_HI = JSON.parseObject(paramJSON.toString(),
				MessageTemplatesNoticeInfoEntity_HI.class);

		JSONArray array = paramJSON.getJSONArray("dataList");
		if (null == array || array.size() == 0) {

			resultStr = SToolUtils.convertResultJSONObj("F", "会员列表为空，不能发生站内消息", 1, "send fail");
			return resultStr.toString();
		}
		Iterator<Object> it = array.iterator();
		Iterator<Object> ittop = array.iterator();
		JSONObject object = new JSONObject();
	/*	while (ittop.hasNext()) {
			JSONObject obtop = (JSONObject) ittop.next();
			if (object != null)
				object.clear();
			object.put("respId", obtop.getString("responsibilityId"));
			Pagination usersRespList = baseSaafUserRespServer.findRespUserList(object, 1, 500);
			JSONObject jsonObjectLine = JSONObject.parseObject(JSONObject.toJSONString(usersRespList));
			JSONArray lineListResult = JSON.parseArray(jsonObjectLine.getString("data"));// 所有字段名字
			Iterator<Object> it = lineListResult.iterator();*/
			while (it.hasNext()) {
				JSONObject ob = (JSONObject) it.next();
				hql.append(ob.getString("userId"));
				if (object != null)
					object.clear();
				object.put("userId", ob.getString("userId"));
				SaafUserEmployeesEntity_HI_RO entity_HI_RO = saafUsersServer.findSaafUsersById(object);

				if (null != paramJSON.getString("templatesContent")) {
					content = paramJSON.getString("templatesContent");
					content = replaceContent(content, templateEntity_HI.getMtValuesetCode(),
							entity_HI_RO.getMobilePhone(), sessionMap);
					if ("false".equals(content)) {
						// 没有会员信息 没办法替换信息 则插入发生失败
						String msmSendStatus = "失败";
						msmReceiverAccounts[0] = entity_HI_RO.getMobilePhone();
						saveMessageSendMark(msmReceiverAccounts, templateEntity_HI.getTemplatesTitle(),
								templateEntity_HI.getTemplatesContent(), msmSendStatus, "站内信");
						continue;
					}

				}
				SaafMessageHeadEntity_HI saafMessageHeadEntity_HI = new SaafMessageHeadEntity_HI();
				saafMessageHeadEntity_HI.setMessageCode(messageCode);
				saafMessageHeadEntity_HI.setMessageDesc(paramJSON.getString("templatesTitle"));
				saafMessageHeadEntity_HI.setMessageTex(content);
				saafMessageHeadEntity_HI.setMessageType("NOTICE");
				if (url != null && !url.trim().equals("")) {
					saafMessageHeadEntity_HI.setMessageUrl(url);
				} else {
					saafMessageHeadEntity_HI.setMessageUrl("#/home/show_message_List/" + messageCode);
				}

				saafMessageHeadEntity_HI.setLastUpdateDate(new Date());
				saafMessageHeadEntity_HI.setLastUpdatedBy(userId);
				saafMessageHeadEntity_HI.setLastUpdateLogin(userId);
				saafMessageHeadEntity_HI.setCreatedBy(userId);
				saafMessageHeadEntity_HI.setCreationDate(new Date());
				LOGGER.info("saafMessageHeadEntity_HI=" + JSONObject.toJSONString(saafMessageHeadEntity_HI));
				saafMessageHeadDAO_HI.save(saafMessageHeadEntity_HI);
				int MessageHeadId = saafMessageHeadEntity_HI.getMessageId();

				String insertLineSql = " INSERT INTO saaf_message_line ( MESSAGE_ID, TYPE, USERID_OR_GROUP, USER_CODE, USER_NAME, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, LAST_UPDATE_DATE, LAST_UPDATE_LOGIN ) SELECT VAR_MESSAGE_ID, 'USER', user_table.USER_ID, user_table.USER_NAME, user_table.USER_FULL_NAME, now(), VAR_USERID, VAR_USERID, now(), VAR_USERID FROM ( SELECT 1 AS flag, CONCAT(',',group_concat(saaf_users.USER_ID),',') USER_ID, group_concat(saaf_users.USER_NAME) USER_NAME, group_concat( CASE saaf_users.PLATFORM_CODE WHEN 'AUX' THEN ( SELECT saaf_employees.EMPLOYEE_NAME FROM saaf_employees WHERE saaf_employees.EMPLOYEE_ID = saaf_users.EMPLOYEE_ID ) ELSE saaf_users.USER_FULL_NAME END ) USER_FULL_NAME FROM saaf_users WHERE saaf_users.USER_ID IN (VAR_personSql) GROUP BY flag ) user_table  ";
				insertLineSql = insertLineSql.replaceAll("VAR_MESSAGE_ID", Integer.toString(MessageHeadId))
						.replaceAll("VAR_USERID", Integer.toString(userId)).replaceAll("VAR_personSql", hql.toString());

				saafMessageHeadDAO_HI.executeSqlUpdate(insertLineSql);
				saafMessageServer.updateSaafMessage(MessageHeadId, userId, "createPushOfNotice", "createPushOfNotice");
				// 清空
				hql.delete(0, hql.length());

				String msmSendStatus = "成功";
				msmReceiverAccounts[0] = entity_HI_RO.getMobilePhone();
				saveMessageSendMark(msmReceiverAccounts, templateEntity_HI.getTemplatesTitle(), content, msmSendStatus,
						"站内信");
			}
	/*	}*/

		resultStr = SToolUtils.convertResultJSONObj("S", "", 1, "send success");
		return resultStr.toString();
	}
	
	
	
     /**
      * 百度云推送
      */
	public String saveBaiduPushSendInfo(JSONObject queryParamJSON, Map<String, String> sessionMap) {
		MessageTemplatesNoticeInfoEntity_HI templateEntity_HI = JSON.parseObject(queryParamJSON.toString(),
				MessageTemplatesNoticeInfoEntity_HI.class);
		// String[] channelIds = new String[10000];
		LOGGER.info("saveBaiduPushSendInfo:" + queryParamJSON);
		// 1. get apiKey and secretKey from developer console
		String apiKey=null; //= "L1tdqG5iVyc2RAeOmytT8HZZ";
		String secretKey=null; //= "FyHgdmDcqLEryMjEOG8XvbvdreFOq7Q8";
		//动态获取相关key 
		JSONObject jb = new JSONObject();
		jb.put("msType", "YUNPUSH");
		jb.put("isEnabled", "Y");
		Map<String, String> detailInfoInfo = msDetailInfoServer.findServerInfo(jb);
		for (Entry<String, String> entry : detailInfoInfo.entrySet()) {
			if("apiKey".equals(entry.getKey())){
				apiKey=entry.getValue();
			}else if("secretKey".equals(entry.getKey())){
				secretKey=entry.getValue();
			}
		}
	
		
		
		PushKeyPair pair = new PushKeyPair(apiKey, secretKey);
		String[] msmReceiverAccounts = new String[1];

		// 2. build a BaidupushClient object to access released interfaces
		BaiduPushClient pushClient = new BaiduPushClient(pair, BaiduPushConstants.CHANNEL_REST_URL);

		// 3. register a YunLogHandler to get detail interacting information
		// in this request.
		pushClient.setChannelLogHandler(new YunLogHandler() {
			@Override
			public void onHandle(YunLogEvent event) {
				System.out.println(event.getMessage());
			}
		});

		try {

			if (null != queryParamJSON.getJSONArray("dataList") && queryParamJSON.getJSONArray("dataList").size() > 0) {

				JSONObject parameters = new JSONObject();

				JSONArray array = queryParamJSON.getJSONArray("dataList");
				Iterator<Object> it = array.iterator();
				String content = null;
				while (it.hasNext()) {
					JSONObject ob = (JSONObject) it.next();
					/*if(null==ob.getString("channelId"))
						continue;
				    String[] channelIds = { ob.getString("channelId")};*/
					JSONObject object = new JSONObject();
					object.put("userId", ob.getString("userId"));
					SaafUserEmployeesEntity_HI_RO entity_HI_RO = saafUsersServer.findSaafUsersById(object);
					content = templateEntity_HI.getTemplatesContent();
					if (null != content) {
						content = replaceContent(content, templateEntity_HI.getMtValuesetCode(),
								entity_HI_RO.getMobilePhone(), sessionMap);
						if ("false".equals(content)) {
							// 没有会员信息 没办法替换信息 则插入发生失败
							String msmSendStatus = "失败";
							msmReceiverAccounts[0] = entity_HI_RO.getMobilePhone();
							saveMessageSendMark(msmReceiverAccounts, templateEntity_HI.getTemplatesTitle(),
									templateEntity_HI.getTemplatesContent(), msmSendStatus, "云推送");
							continue;
						}
						//云推送 过滤html代码
						content=deleteAllHTMLTag(content);
						// 4. specify request arguments
						// 创建Android通知					
						JSONObject notification = new JSONObject();
						notification.put("title", templateEntity_HI.getTemplatesTitle());
						notification.put("description", content);
						notification.put("notification_builder_id", 0);
						notification.put("notification_basic_style", 4);
						notification.put("open_type", 1);
						notification.put("url", "http://push.baidu.com");
						JSONObject jsonCustormCont = new JSONObject();
						jsonCustormCont.put("key", "value"); // 自定义内容，key-value
						notification.put("custom_content", jsonCustormCont);
						String str = "";
						//	String[] channelIds = { ob.getString("channelId")};
						//暂时写死
						String[] channelIds = { "3558167010460962849" , "4521980073705589872"  };

						PushBatchUniMsgRequest request = new PushBatchUniMsgRequest().addChannelIds(channelIds)
								.addMsgExpires(new Integer(3600)).addMessageType(1).addMessage(notification.toString())
								.addDeviceType(3).addTopicId("BaiduPush");// 设置类别主题
						// 5. http request
						PushBatchUniMsgResponse response = pushClient.pushBatchUniMsg(request);

						String msmSendStatus = "成功";
						msmReceiverAccounts[0] = entity_HI_RO.getMobilePhone();
						saveMessageSendMark(msmReceiverAccounts, templateEntity_HI.getTemplatesTitle(), content,
								msmSendStatus, "云推送");

					}

				}

			}
		} catch (PushClientException e) {
			if (BaiduPushConstants.ERROROPTTYPE) {
				// throw e;
			} else {
				e.printStackTrace();
			}
		} catch (PushServerException e) {
			if (BaiduPushConstants.ERROROPTTYPE) {
				// throw e;
			} else {
				LOGGER.info(String.format("requestId: %d, errorCode: %d, errorMessage: %s", e.getRequestId(),
						e.getErrorCode(), e.getErrorMsg()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

		JSONObject resultStr = SToolUtils.convertResultJSONObj("S", "", 1, "send success");
		return String.valueOf(resultStr);

	}
	
	public void saveMessageSendMark(String[] mobiles,String subject,String content,String msmSendStatus,String wayName) {

		MessageSendMarkInfoEntity_HI messageSendMarkInfoEntity_HI = new MessageSendMarkInfoEntity_HI();
		messageSendMarkInfoEntity_HI.setMsmReceiverAccount(JSONArray.toJSONString(mobiles));
		messageSendMarkInfoEntity_HI.setMsmMessageTitle(subject);
		messageSendMarkInfoEntity_HI.setMsmMessageContent(content);
		messageSendMarkInfoEntity_HI.setMsmSendDateTime(new Date());
		messageSendMarkInfoEntity_HI.setMsmSendStatus(msmSendStatus);
		messageSendMarkInfoEntity_HI.setMsmSendWayCode("shortMessage");
		messageSendMarkInfoEntity_HI.setMsmSendWayName(wayName);
		saveMessageSendMarkInfoInfo(messageSendMarkInfoEntity_HI);

	}
	/**
	 * 验证邮箱地址是否正确
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		boolean flag = false;
		try {
			String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			// LOG.error("验证邮箱地址错误", e);
			flag = false;
		}

		return flag;
	}

	/**
	 * 验证手机号码
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		boolean flag = false;
		try {
			Pattern p = Pattern.compile("^((13[0-9])|(14[5|7])|(17[6|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$");
			Matcher m = p.matcher(mobiles);
			flag = m.matches();
		} catch (Exception e) {
			// LOG.error("验证手机号码错误", e);
			flag = false;
		}
		return flag;
	}
	/**
	  * 删除所有的HTML标签
	  *
	  * @param source 需要进行除HTML的文本
	  * @return
	  */
	 public static String deleteAllHTMLTag(String source) {

	  if(source == null) {
	       return "";
	  }

	  String s = source;
	  s=s.replaceAll("&nbsp;", " ");
	  /** 删除普通标签  */
	  s = s.replaceAll("<(S*?)[^>]*>.*?|<.*? />", "");
	  /** 删除转义字符 */
	  s = s.replaceAll("&.{2,6}?;", "");
	  return s;
	 }

    public static void main(String[] args) {/*
    	Map<String, String> map =new HashMap<String,String>();
    	map.put("column8", "tel_phone");
    	map.put("column9", "mobile_phone");
    	map.put("column6", "position_name");
    	map.put("column7", "postal_address");
    	map.put("column10", "querysomeparam");
    	map.put("column1", "account_number");
    	map.put("column3", "employee_name");
    	map.put("column2", "employee_number");
    	map.put("column5", "sex");
    	map.put("column4","platform_code");
    	{column8=tel_phone,
    			column9=mobile_phone, 
    			column6=position_name, 
    			column7=postal_address,
    			column10=querysomeparam, 
    			column1=account_number, 
    			column3=employee_name,
    			column2=employee_number,
    			column5=sex,
    			column4=platform_code}
    	String TemplatesContent="<p>恭喜你，中了<code class=\"hook-cursor-tip\" style=\"color:#00a0e9; cursor:default\">@employee_name</code>&nbsp;<code class=\"hook-cursor-tip\" style=\"color:#00a0e9; cursor:default\">@employee_number</code>&nbsp;</p><p style=\"display:none;\"><br/></p><p style=\"display:none;\"><br/></p>";
        JSONArray findListResult=JSON.parseArray("[{\"column8\":\"\",\"column9\":\"13498769876\",\"column6\":\"市场部\",\"column7\":\"\",\"column1\":\"sysadmin\","
        		+ "\"column3\":\"系统超级管理员\",\"column2\":\"SAAF001\",\"column5\":\"MALE\",\"column4\":\"SAAF\"}]");
     //   LOGGER.info("555555555555555555555555555="+jsonObject.getString("data"));
        if(null==findListResult||findListResult.size()==0)
        	 TemplatesContent;
        //遍历行数据 根据key-value值 替换发送内容相关字段
      //  Object object =findListResult.getJSONObject(0);
      //  Field[] fields = object.getClass().getDeclaredFields(); // 获取实体类的所有属性，返回Field数组  
        for (Entry<String, Object> entry : findListResult.getJSONObject(0).entrySet()) {
        	String key = "";
            String paramName = "";
            Object value = null;
        	   value = entry.getValue();
               key = entry.getKey();
        	// System.out.println("llllllllllllllllllllllllll="+key);
        	// System.out.println("222222222222222222222222="+map);
        	if(TemplatesContent.indexOf(map.get(key))>=0){
        		TemplatesContent=TemplatesContent.replaceAll("@"+map.get(key)
        		, "系统超级管理员");
        		
        	}
		}
        
        System.out.println(TemplatesContent);  	
    */
   /* 	String str ="<p>恭喜你，中了<code class=\"hook-cursor-tip\" style=\"color:#00a0e9; cursor:default\">lindezhao</code>&nbsp;<code class=\"hook-cursor-tip\" style=\"color:#00a0e9; cursor:default\">YG00000229</code>&nbsp;</p><p style=\"display:none;\"><br/></p><p style=\"display:none;\"><br/></p>";
         System.out.println(deleteAllHTMLTag(str));*/
    }

	
}
