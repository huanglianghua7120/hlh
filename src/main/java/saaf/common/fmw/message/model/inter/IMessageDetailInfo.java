package saaf.common.fmw.message.model.inter;

import com.alibaba.fastjson.JSONObject;

import com.yhg.hibernate.core.paging.Pagination;

import saaf.common.fmw.message.model.entities.MessageServerDetailInfoEntity_HI;

import java.util.Map;

public interface IMessageDetailInfo {

	Map<String, String> findMessageDetailInfoInfo(JSONObject queryParamJSON);

	String saveMessageDetailInfoInfo(JSONObject queryParamJSON);

	String findMessageDetailInfoList(JSONObject queryParamJSON, Integer curIndex, Integer pageSize);

	MessageServerDetailInfoEntity_HI findMessageDetailInfo(JSONObject queryParamJSON);

	String deleteMessageDetailInfo(JSONObject queryParamJSON);
}
