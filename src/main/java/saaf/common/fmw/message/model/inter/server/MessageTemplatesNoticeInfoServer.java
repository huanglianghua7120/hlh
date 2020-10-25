package saaf.common.fmw.message.model.inter.server;

import saaf.common.fmw.message.model.inter.IMessageTemplatesNoticeInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.yhg.base.utils.SToolUtils;
import com.yhg.base.utils.StringUtils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import saaf.common.fmw.message.utils.SaafToolUtils;
import saaf.common.fmw.message.model.entities.MessageServerDetailInfoEntity_HI;
import saaf.common.fmw.message.model.entities.MessageServerInfoEntity_HI;
import saaf.common.fmw.message.model.entities.MessageTemplatesNoticeInfoEntity_HI;
import saaf.common.fmw.message.model.dao.MessageTemplatesNoticeInfoDAO_HI;
import com.yhg.hibernate.core.dao.ViewObject;
import com.yhg.hibernate.core.dao.ViewObjectImpl;
import com.yhg.hibernate.core.paging.Pagination;

@Component("messageTemplatesNoticeInfoServer")
public class MessageTemplatesNoticeInfoServer implements IMessageTemplatesNoticeInfo  {
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageTemplatesNoticeInfoServer.class);
	@Autowired
	private MessageTemplatesNoticeInfoDAO_HI  messageTemplatesNoticeInfoDAO_HI;
	public MessageTemplatesNoticeInfoServer() {
		super();
	}

	public String findMessageTemplatesNoticeInfoInfo(JSONObject queryParamJSON,Integer curIndex,Integer pageSize) {
		System.out.println(queryParamJSON);
		//Map<String, Object> queryParamMap = SToolUtils.fastJsonObj2Map(queryParamJSON);
		MessageTemplatesNoticeInfoEntity_HI mtEntity_HI = JSON.parseObject(queryParamJSON.toString(),
				MessageTemplatesNoticeInfoEntity_HI.class);
		StringBuffer sfhql = new StringBuffer();
		sfhql.append(" from MessageTemplatesNoticeInfoEntity_HI s where 1=1");
		Map<String, Object> map =new HashMap<String, Object>();
		 Field[] fields = mtEntity_HI.getClass().getDeclaredFields();
		 for(Field field:fields) {
			 String fieldName = field.getName();
			 if(StringUtils.isEmpty(queryParamJSON.getString(fieldName)))
				 continue;
			 sfhql.append(" and  s."+fieldName+" like '%"+queryParamJSON.getString(fieldName)+"%' ");
		 }
		Pagination<MessageTemplatesNoticeInfoEntity_HI> findListResult =  messageTemplatesNoticeInfoDAO_HI.findPagination(sfhql, map, curIndex,pageSize);
		JSONObject jsonObject = JSONObject.parseObject( JSON.toJSONString(findListResult));
		jsonObject.put("status", "S");
		String resultData = JSON.toJSONString(jsonObject);
		System.out.println(resultData);
	//	JSONObject resultStr = SToolUtils.convertResultJSONObj("S", "", findListResult.getCount(), resultData);
		return resultData;
	}
	
	  public MessageTemplatesNoticeInfoEntity_HI findTemplatesNoticeInfo(JSONObject queryParamJSON) {
	        Map<String, Object> queryParamMap = SToolUtils.fastJsonObj2Map(queryParamJSON);
	        StringBuffer sbhql =new StringBuffer();
	        sbhql.append(" from MessageTemplatesNoticeInfoEntity_HI s where 1=1");
	        if(null!=queryParamJSON.getString("templatesId")) {
	        	sbhql.append(" and s.templatesId = :templatesId ");
	        }
	        MessageTemplatesNoticeInfoEntity_HI entity_HI = messageTemplatesNoticeInfoDAO_HI.get(sbhql.toString(), queryParamMap);
	        return entity_HI;
	    }
	public String saveMessageTemplatesNoticeInfoInfo(JSONObject queryParamJSON) {
		MessageTemplatesNoticeInfoEntity_HI messageTemplatesNoticeInfoEntity_HI = JSON.parseObject(queryParamJSON.toString(), MessageTemplatesNoticeInfoEntity_HI.class);
		
		JSONObject queryParamJSON_ = new JSONObject();
		queryParamJSON_.put("templatesId",
				null == messageTemplatesNoticeInfoEntity_HI.getTemplatesId() ? 0 : messageTemplatesNoticeInfoEntity_HI.getTemplatesId());
		MessageTemplatesNoticeInfoEntity_HI tempEntity =findTemplatesNoticeInfo(queryParamJSON_);
		LOGGER.info(JSONObject.toJSONString(messageTemplatesNoticeInfoEntity_HI));
		if (null == tempEntity) {
			messageTemplatesNoticeInfoDAO_HI.save(messageTemplatesNoticeInfoEntity_HI);
		} else {

			tempEntity.setTemplatesType(messageTemplatesNoticeInfoEntity_HI.getTemplatesType());
			tempEntity.setTemplatesTitle(messageTemplatesNoticeInfoEntity_HI.getTemplatesTitle());
			tempEntity.setTemplatesContent(messageTemplatesNoticeInfoEntity_HI.getTemplatesContent());
			tempEntity.setStartDateAcitve(messageTemplatesNoticeInfoEntity_HI.getStartDateAcitve());
			tempEntity.setEndDateAcitve(messageTemplatesNoticeInfoEntity_HI.getEndDateAcitve());
			tempEntity.setMtValuesetCode(messageTemplatesNoticeInfoEntity_HI.getMtValuesetCode());
			
			tempEntity.setLastUpdateDate(new Date());
			tempEntity.setLastUpdatedBy(0);
			messageTemplatesNoticeInfoDAO_HI.update(tempEntity);
		}
		queryParamJSON_.clear();
		queryParamJSON_.put("templatesId",
				(null == tempEntity? messageTemplatesNoticeInfoEntity_HI.getTemplatesId():tempEntity.getTemplatesId() ));
		MessageTemplatesNoticeInfoEntity_HI tempEntity_ = findTemplatesNoticeInfo(queryParamJSON_);
		JSONObject resultStr = SToolUtils.convertResultJSONObj("S", "", 1, tempEntity_);
		return resultStr.toString();
	}
	public String deleteMessageTemplatesInfo(JSONObject queryParamJSON) {

		MessageTemplatesNoticeInfoEntity_HI messageTlEntity_HI = JSON.parseObject(queryParamJSON.toString(), MessageTemplatesNoticeInfoEntity_HI.class);
	    

	    messageTemplatesNoticeInfoDAO_HI.delete(messageTlEntity_HI);

		JSONObject resultStr = SToolUtils.convertResultJSONObj("S", "", 1, "delete success");
		return resultStr.toString();
	}
	

    
	public static void main(String[] args) {
		MessageTemplatesNoticeInfoServer messageTemplatesNoticeInfoServer = (MessageTemplatesNoticeInfoServer)SaafToolUtils.context.getBean("messageTemplatesNoticeInfoServer");
		JSONObject paramJSON = new JSONObject();
		paramJSON.put("templatesId", 1);
	/*	paramJSON.put("templatesTitle", "通知");
		paramJSON.put("templatesContent","会议");*/
		MessageTemplatesNoticeInfoEntity_HI entity=messageTemplatesNoticeInfoServer.findTemplatesNoticeInfo(paramJSON);
		//LOGGER.info(resultStr);s
		JSONObject jsonObject = JSONObject.parseObject( JSON.toJSONString(entity));
		//jsonObject.put("status", "S");
		String resultData = JSON.toJSONString(jsonObject);
		System.out.println(JSONObject.parseObject( JSON.toJSONString(entity)).toString());
	}

	
}
