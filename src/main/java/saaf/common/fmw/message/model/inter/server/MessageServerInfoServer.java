package saaf.common.fmw.message.model.inter.server;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import saaf.common.fmw.message.model.dao.MessageServerDetailInfoDAO_HI;
import saaf.common.fmw.message.model.dao.MessageServerInfoDAO_HI;
import saaf.common.fmw.message.model.entities.MessageServerDetailInfoEntity_HI;
import saaf.common.fmw.message.model.entities.MessageServerInfoEntity_HI;
import saaf.common.fmw.message.model.inter.IMessageInfo;
import saaf.common.fmw.message.utils.SaafToolUtils;

import com.yhg.base.utils.SToolUtils;
import com.yhg.base.utils.StringUtils;
import com.yhg.hibernate.core.paging.Pagination;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component("messageServerInfoServer")
public class MessageServerInfoServer  implements IMessageInfo {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageServerInfoServer.class);
    @Autowired
    private MessageServerInfoDAO_HI messageServerInfoDAO_HI;
    
    @Autowired
    private MessageServerDetailInfoDAO_HI messageServerDetailInfoDAO_HI;

    public MessageServerInfoServer() {
        super();
    }

    public MessageServerInfoEntity_HI findMessageInfoInfo(JSONObject queryParamJSON) {
        Map<String, Object> queryParamMap = SToolUtils.fastJsonObj2Map(queryParamJSON);
        StringBuffer sbhql =new StringBuffer();
        sbhql.append(" from MessageServerInfoEntity_HI h where 1=1 ");
       /* if(null!=queryParamJSON.getString("msWxAppId")) {
        	sbhql.append(" and h.msWxAppId = :msWxAppId ");
        }
        if(null!=queryParamJSON.getString("msId")) {
        	sbhql.append(" and h.msId = :msId ");
        }
        if(null!=queryParamJSON.getString("msType")) {
        	sbhql.append(" and h.msType = :msType ");
        }       
        if(null!=queryParamJSON.getString("isEnabled")) {
        	sbhql.append(" and h.isEnabled = :isEnabled ");
        }*/	 
		// 遍历行数据 根据key-value值 替换容相关字段
		String key = "";
		Object value = null;
		for (Entry<String, Object> entry : queryParamMap.entrySet()) {

			value = entry.getValue();
			key = entry.getKey();

			 if(null==queryParamMap.get(key)||"msdWayCode".equals(key.trim()))
				 continue;
			 if("msId".equals(key))//前端详情查询某一条数据
				 sbhql.append(" and  h."+key+" = "+value+" ");
			 else
				 sbhql.append(" and  h."+key+" = '"+value+"' ");
		}

        MessageServerInfoEntity_HI entity_HI = messageServerInfoDAO_HI.get(sbhql.toString(), queryParamMap);
        return entity_HI;
    }
   
    public String findMessageInfoList(JSONObject queryParamJSON,Integer curIndex,Integer pageSize) {
		System.out.println(queryParamJSON);
		//Map<String, Object> queryParamMap = SToolUtils.fastJsonObj2Map(queryParamJSON);
		MessageServerInfoEntity_HI msEntity_HI = JSON.parseObject(queryParamJSON.toString(),
				MessageServerInfoEntity_HI.class);
		StringBuffer sfhql = new StringBuffer();
		sfhql.append(" from MessageServerInfoEntity_HI s where 1=1 ");
		Map<String, Object> map =new HashMap<String, Object>();
		//利用java反射机制 
		 Field[] fields = msEntity_HI.getClass().getDeclaredFields();
		 for(Field field:fields) {
			 String fieldName = field.getName();
			 if(StringUtils.isEmpty(queryParamJSON.getString(fieldName)))
				 continue;
			 if("msId".equals(fieldName))//前端详情查询某一条数据
			 sfhql.append(" and  s."+fieldName+" = "+queryParamJSON.getString(fieldName)+" ");
			 else
			 sfhql.append(" and  s."+fieldName+" like '%"+queryParamJSON.getString(fieldName)+"%' ");
		 }
		Pagination<MessageServerInfoEntity_HI> findListResult =  messageServerInfoDAO_HI.findPagination(sfhql, map, curIndex,pageSize);
		JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(findListResult));
		jsonObject.put("status", "S");		
		String resultData = JSON.toJSONString(jsonObject);
		System.out.println(resultData);
     	return resultData;
	}
    
    /**
     * 新增修改主从表数据
     * @param queryParamJSON
     * @return
     */
	public String saveMessageAllInfo(JSONObject queryParamJSON) {
		MessageServerInfoEntity_HI messageServerInfoEntity_HI = JSON.parseObject(queryParamJSON.toString(),
				MessageServerInfoEntity_HI.class);
		JSONObject queryParamJSON_ = new JSONObject();
		JSONObject resultStr;
		int msId=(null == messageServerInfoEntity_HI.getMsId() ? 0 : messageServerInfoEntity_HI.getMsId());
		queryParamJSON_.put("msId",msId);
		MessageServerInfoEntity_HI tempEntity = findMessageInfoInfo(queryParamJSON_);
		
		StringBuffer sbhql = new StringBuffer();
		sbhql.append(" from MessageServerInfoEntity_HI h where h.isEnabled ='Y' ");
		if (null != messageServerInfoEntity_HI.getMsType()) {
			sbhql.append(" and h.msType = '"+messageServerInfoEntity_HI.getMsType()+"' ");
		}
		if (null != tempEntity) {//修改时
			sbhql.append(" and h.msId <> "+tempEntity.getMsId());
		}
		MessageServerInfoEntity_HI entity_HI = messageServerInfoDAO_HI.get(sbhql.toString(), new HashMap<String, Object>());
		
        if(entity_HI!=null){
        	
        	resultStr = SToolUtils.convertResultJSONObj("F", "", 1, messageServerInfoEntity_HI.getMsType()+"不允许设置多条记录");
        	return String.valueOf(resultStr);
        }
		
		// 头表增删改
		if (null == tempEntity) {
			messageServerInfoEntity_HI.setMsWayCode(messageServerInfoEntity_HI.getMsType()+"_"+Calendar.getInstance().getTimeInMillis());
			messageServerInfoDAO_HI.save(messageServerInfoEntity_HI);
		} else {			
		//	tempEntity.setMsWayCode(messageServerInfoEntity_HI.getMsWayCode());
			tempEntity.setMsWxAppId(messageServerInfoEntity_HI.getMsWxAppId());
			tempEntity.setMsWayName(messageServerInfoEntity_HI.getMsWayName());
			tempEntity.setMsWayDesc(messageServerInfoEntity_HI.getMsWayDesc());
			tempEntity.setMsTokenCode(messageServerInfoEntity_HI.getMsTokenCode());
			tempEntity.setMsTokenStartTime(messageServerInfoEntity_HI.getMsTokenStartTime());
			tempEntity.setMsCustomerName(messageServerInfoEntity_HI.getMsCustomerName());
			tempEntity.setMsPrice(messageServerInfoEntity_HI.getMsPrice());
			tempEntity.setMsType(messageServerInfoEntity_HI.getMsType());
			tempEntity.setVersionNum(messageServerInfoEntity_HI.getVersionNum());
			tempEntity.setLastUpdateDate(new Date());
			tempEntity.setLastUpdatedBy(0);
			messageServerInfoDAO_HI.update(tempEntity);
		
		}
		
		// 插入表体数据
		if (null != queryParamJSON.getJSONArray("dataList")&&queryParamJSON.getJSONArray("dataList").size()>0) {
			// 再新增修改表体信息

			JSONArray array = queryParamJSON.getJSONArray("dataList");
			Iterator<Object> it = array.iterator();
			while (it.hasNext()) {
				saveMessageDetailInfoInfo(JSONObject.parseObject(JSON.toJSONString(it.next())),(null == tempEntity? messageServerInfoEntity_HI.getMsWayCode():tempEntity.getMsWayCode() ));
			}
		}
		queryParamJSON_.clear();
		queryParamJSON_.put("msId",
				(null == tempEntity? messageServerInfoEntity_HI.getMsId():tempEntity.getMsId() ));
		MessageServerInfoEntity_HI tempEntity_ = findMessageInfoInfo(queryParamJSON_);
	   resultStr = SToolUtils.convertResultJSONObj("S", "", 1, tempEntity_);
		return String.valueOf(resultStr);
	}
    /**
     * 
     */
    public String saveMessageInfo(JSONObject queryParamJSON) {
        MessageServerInfoEntity_HI messageServerInfoEntity_HI = JSON.parseObject(queryParamJSON.toString(), MessageServerInfoEntity_HI.class);
        JSONObject queryParamJSON_ = new JSONObject();
        queryParamJSON_.put("msWxAppId", messageServerInfoEntity_HI.getMsWxAppId());
        MessageServerInfoEntity_HI tempEntity = findMessageInfoInfo(queryParamJSON_);
        LOGGER.info(JSONObject.toJSONString(messageServerInfoEntity_HI));
        if(null == tempEntity){
            messageServerInfoDAO_HI.save(messageServerInfoEntity_HI);
        }else{
            tempEntity.setMsTokenCode(messageServerInfoEntity_HI.getMsTokenCode());
            tempEntity.setMsTokenStartTime(messageServerInfoEntity_HI.getMsTokenStartTime());
            messageServerInfoDAO_HI.update(tempEntity);    
        }
        JSONObject resultStr = SToolUtils.convertResultJSONObj("S", "", 1, "save success");
        return resultStr.toString();
    }
    
	public String deleteMessageInfo(JSONObject queryParamJSON) {

		MessageServerInfoEntity_HI entity = JSON.parseObject(queryParamJSON.toString(),
				MessageServerInfoEntity_HI.class);
		if (null != entity) {
		
			StringBuffer sfhql = new StringBuffer();
			sfhql.append(" from MessageServerDetailInfoEntity_HI s where 1=1 ");
			Map<String, Object> map = new HashMap<String, Object>();
			if (!StringUtils.isEmpty(entity.getMsWayCode())) {
				sfhql.append(" and  s.msdWayCode = '" + entity.getMsWayCode()+ "' ");
			}
			List<MessageServerDetailInfoEntity_HI> findListResult = messageServerDetailInfoDAO_HI.findList(sfhql, map);
			if (null != findListResult)
				messageServerDetailInfoDAO_HI.delete(findListResult);
			messageServerInfoDAO_HI.delete(entity);

		}
		JSONObject resultStr = SToolUtils.convertResultJSONObj("S", "", 1, "delete success");
		return resultStr.toString();
	}
    
    
  
    public MessageServerDetailInfoEntity_HI findMessageDetailInfo(JSONObject queryParamJSON) {
        Map<String, Object> queryParamMap = SToolUtils.fastJsonObj2Map(queryParamJSON);
        StringBuffer sbhql =new StringBuffer();
        sbhql.append(" from MessageServerDetailInfoEntity_HI h where 1=1 ");
        if(null!=queryParamJSON.getString("msdId")) {
        	sbhql.append(" and h.msdId = :msdId ");
        }
        MessageServerDetailInfoEntity_HI entity_HI = messageServerDetailInfoDAO_HI.get(sbhql.toString(), queryParamMap);
        return entity_HI;
    }
    
    
	public String saveMessageDetailInfoInfo(JSONObject queryParamJSON,String MsWayCode) {
		MessageServerDetailInfoEntity_HI messageDetailEntity_HI = JSON.parseObject(queryParamJSON.toString(),
				MessageServerDetailInfoEntity_HI.class);

		JSONObject queryParamJSON_ = new JSONObject();
		queryParamJSON_.put("msdId", null == messageDetailEntity_HI.getMsdId() ? 0 : messageDetailEntity_HI.getMsdId());
		MessageServerDetailInfoEntity_HI tempEntity = findMessageDetailInfo(queryParamJSON_);
		LOGGER.info(JSONObject.toJSONString(messageDetailEntity_HI));
		if (null == tempEntity) {
			messageDetailEntity_HI.setMsdWayCode(MsWayCode);
			messageServerDetailInfoDAO_HI.save(messageDetailEntity_HI);
		} else {

			//tempEntity.setMsdWayCode(messageDetailEntity_HI.getMsdWayCode());
			tempEntity.setMsdAttributeKey(messageDetailEntity_HI.getMsdAttributeKey());
			tempEntity.setMsdAttributeValue(messageDetailEntity_HI.getMsdAttributeValue());

			tempEntity.setLastUpdateDate(new Date());
			tempEntity.setLastUpdatedBy(0);
			messageServerDetailInfoDAO_HI.update(tempEntity);
		}
		JSONObject resultStr = SToolUtils.convertResultJSONObj("S", "", 1, "save success");
		return resultStr.toString();
	}

    public static void main(String[] args) {
        MessageServerInfoServer messageServerInfoServer = (MessageServerInfoServer)SaafToolUtils.context.getBean("messageServerInfoServer");
        JSONObject paramJSON = new JSONObject();
        //paramJSON.put("msId", "23");
        paramJSON.put("msWayCode", "test166666666666");
        paramJSON.put("msWayName", "kkdadad");
        paramJSON.put("msTokenCode", "312312");
     
        List<Map<String, Object>> list =new ArrayList<>();
        for (int i = 0; i < 4; i++) {
        	Map<String, Object> map =new HashMap<String, Object>();
        	//map.put("msdId", "65");
        	map.put("msdWayCode", "test166666666666");
        	map.put("msdAttributeKey", "msdAttributeKey"+i);
        	map.put("msdAttributeValue", "msdAttributeValue"+i);
        	list.add(map);
		}
        paramJSON.put("detailList", list);
      //  System.out.println(paramJSON);
       // paramJSON.put("msTokenCode2", "312312");
      String resultStr = messageServerInfoServer.saveMessageAllInfo(paramJSON);

      System.out.println(resultStr);
    
     //     JSONArray array=   paramJSON.getJSONArray("detail");
//      System.out.println(JSONObject.parseObject(JSON.toJSONString(array.get(0))));
    //  LOGGER.info(resultStr);
        
        
    	//   JSONObject resultStr = SToolUtils.convertResultJSONObj("S", "", 1, "save success");
    }


}
