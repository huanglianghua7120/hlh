package saaf.common.fmw.message.model.inter.server;

import saaf.common.fmw.message.model.inter.IMessageDetailInfo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import com.yhg.base.utils.SToolUtils;
import com.yhg.base.utils.StringUtils;

import org.springframework.stereotype.Component;

import saaf.common.fmw.message.utils.SaafToolUtils;
import saaf.common.fmw.message.model.entities.MessageServerDetailInfoEntity_HI;
import saaf.common.fmw.message.model.entities.MessageServerInfoEntity_HI;
import saaf.common.fmw.message.model.dao.MessageServerDetailInfoDAO_HI;

import com.yhg.hibernate.core.dao.ViewObject;
import com.yhg.hibernate.core.paging.Pagination;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@Component("messageServerDetailInfoServer")
public class MessageServerDetailInfoServer implements IMessageDetailInfo {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageServerDetailInfoServer.class);
    @Autowired
    private MessageServerDetailInfoDAO_HI messageServerDetailInfoDAO_HI;
    
    @Autowired
    private MessageServerInfoServer MessageServerInfoServer;

    public MessageServerDetailInfoServer() {
        super();
    }
    
    public MessageServerDetailInfoEntity_HI findMessageDetailInfo(JSONObject queryParamJSON) {
        Map<String, Object> queryParamMap = SToolUtils.fastJsonObj2Map(queryParamJSON);
        StringBuffer sbhql =new StringBuffer();
        sbhql.append(" from MessageServerDetailInfoEntity_HI l where 1=1 ");
        if(null!=queryParamJSON.getString("msdId")) {
        	sbhql.append(" and l.msdId = :msdId ");
        }
        MessageServerDetailInfoEntity_HI entity_HI = messageServerDetailInfoDAO_HI.get(sbhql.toString(), queryParamMap);
        return entity_HI;
    }

    public Map<String, String> findMessageDetailInfoInfo(JSONObject queryParamJSON) {
        Map<String, Object> queryParamMap = SToolUtils.fastJsonObj2Map(queryParamJSON);
        StringBuffer sbhql =new StringBuffer();
        sbhql.append(" from MessageServerDetailInfoEntity_HI l where 1=1 ");
        if(null!=queryParamJSON.getString("msdWayCode")) {
        	sbhql.append(" and l.msdWayCode = :msdWayCode ");
        	
        }
        List<MessageServerDetailInfoEntity_HI> findListResult = messageServerDetailInfoDAO_HI.findList(sbhql.toString()
        		, queryParamMap);
        Map<String, String> serverAttrInfo = new HashMap<String, String>();
        for(int i=0; i<findListResult.size(); i++){
            MessageServerDetailInfoEntity_HI detailInfoEntity_HI = findListResult.get(i);
            serverAttrInfo.put(detailInfoEntity_HI.getMsdAttributeKey(), detailInfoEntity_HI.getMsdAttributeValue());
        }
        return serverAttrInfo;
    }
    public Map<String, String> findServerInfo(JSONObject queryParamJSON) {
        Map<String, Object> queryParamMap = SToolUtils.fastJsonObj2Map(queryParamJSON);
        MessageServerInfoEntity_HI serverEntity= MessageServerInfoServer.findMessageInfoInfo(queryParamJSON);
     
        queryParamJSON.clear();
        queryParamJSON.put("msdWayCode", serverEntity.getMsWayCode());
        
        Map<String, String> serverAttrInfo =findDetailByServerInfo(queryParamJSON);
        return serverAttrInfo;
    }
    
    
    public Map<String, String> findDetailByServerInfo(JSONObject queryParamJSON) {
        Map<String, Object> queryParamMap = SToolUtils.fastJsonObj2Map(queryParamJSON);
 
        StringBuffer sbhql =new StringBuffer();
        sbhql.append(" from MessageServerDetailInfoEntity_HI l where 1=1 ");
        if(null!=queryParamJSON.getString("msdWayCode")) {
        	sbhql.append(" and l.msdWayCode = :msdWayCode ");
        	
        }
        
        List<MessageServerDetailInfoEntity_HI> findListResult = messageServerDetailInfoDAO_HI.findList(sbhql.toString()
        		, queryParamMap);
        Map<String, String> serverAttrInfo = new HashMap<String, String>();
        for(int i=0; i<findListResult.size(); i++){
            MessageServerDetailInfoEntity_HI detailInfoEntity_HI = findListResult.get(i);
            serverAttrInfo.put(detailInfoEntity_HI.getMsdAttributeKey(), detailInfoEntity_HI.getMsdAttributeValue());
        }
        return serverAttrInfo;
    }
    
    public String findMessageDetailInfoList(JSONObject queryParamJSON,Integer curIndex,Integer pageSize) {
		System.out.println(queryParamJSON);
		Map<String, Object> queryParamMap = SToolUtils.fastJsonObj2Map(queryParamJSON);
		StringBuffer sfhql = new StringBuffer();
		sfhql.append(" from MessageServerDetailInfoEntity_HI s where 1=1 ");
		Map<String, Object> map =new HashMap<String, Object>();
		if (!StringUtils.isEmpty(queryParamJSON.getString("msdWayCode"))) {
			sfhql.append(" and  s.msdWayCode = '"+queryParamJSON.getString("msdWayCode")+"' ");
		}
		Pagination<MessageServerDetailInfoEntity_HI> findListResult =  messageServerDetailInfoDAO_HI.findPagination(sfhql, map, curIndex,pageSize);
		JSONObject jsonObject = JSONObject.parseObject( JSON.toJSONString(findListResult));
		jsonObject.put("status", "S");
		String resultData = JSON.toJSONString(jsonObject);
		System.out.println(resultData);
		return resultData;
	}

    public String saveMessageDetailInfoInfo(JSONObject queryParamJSON) {
		MessageServerDetailInfoEntity_HI messageDetailEntity_HI = JSON.parseObject(queryParamJSON.toString(),
				MessageServerDetailInfoEntity_HI.class);

		JSONObject queryParamJSON_ = new JSONObject();
		queryParamJSON_.put("msdId", null == messageDetailEntity_HI.getMsdId() ? 0 : messageDetailEntity_HI.getMsdId());
		MessageServerDetailInfoEntity_HI tempEntity = findMessageDetailInfo(queryParamJSON_);
		LOGGER.info(JSONObject.toJSONString(messageDetailEntity_HI));
		if (null == tempEntity) {
			messageServerDetailInfoDAO_HI.save(messageDetailEntity_HI);
		} else {

			tempEntity.setMsdWayCode(messageDetailEntity_HI.getMsdWayCode());
			tempEntity.setMsdAttributeKey(messageDetailEntity_HI.getMsdAttributeKey());
			tempEntity.setMsdAttributeValue(messageDetailEntity_HI.getMsdAttributeValue());

			tempEntity.setLastUpdateDate(new Date());
			tempEntity.setLastUpdatedBy(0);
			messageServerDetailInfoDAO_HI.update(tempEntity);
		}
		JSONObject resultStr = SToolUtils.convertResultJSONObj("S", "", 1, "save success");
		return resultStr.toString();
	}
    
	public String deleteMessageDetailInfo(JSONObject queryParamJSON) {

	    MessageServerDetailInfoEntity_HI messageDetailEntity_HI = JSON.parseObject(queryParamJSON.toString(), MessageServerDetailInfoEntity_HI.class);
	    

		messageServerDetailInfoDAO_HI.delete(messageDetailEntity_HI);

		JSONObject resultStr = SToolUtils.convertResultJSONObj("S", "", 1, "delete success");
		return resultStr.toString();
	}
    


    public static void main(String[] args) {
        MessageServerDetailInfoServer messageServerDetailInfoServer = (MessageServerDetailInfoServer)SaafToolUtils.context.getBean("messageServerDetailInfoServer");
        JSONObject paramJSON = new JSONObject();
       paramJSON.put("msd_id", 49);
        //paramJSON.put("ordercode", 1);
        //paramJSON.put("tid", 1);
     /*  List<Map<String, Object>> list =new ArrayList<>();
       for (int i = 0; i < 4; i++) {
       	Map<String, Object> map =new HashMap<String, Object>();
       	map.put("msdId", "0");
       	map.put("msdWayCode", "test1");
       	map.put("msdAttributeKey", "msdAttributeKey"+i);
       	map.put("msdAttributeValue", "msdAttributeValue"+i);
       	list.add(map);
		}
       paramJSON.put("detailList", list);*/
       /* Map<String, String> */String resultStr = messageServerDetailInfoServer.deleteMessageDetailInfo(paramJSON);
        System.out.println(resultStr);
    //   LOGGER.info(resultStr);
    }
}
