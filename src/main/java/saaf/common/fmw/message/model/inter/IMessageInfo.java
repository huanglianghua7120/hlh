package saaf.common.fmw.message.model.inter;

import com.alibaba.fastjson.JSONObject;

import saaf.common.fmw.message.model.entities.MessageServerInfoEntity_HI;

public interface IMessageInfo {

	MessageServerInfoEntity_HI findMessageInfoInfo(JSONObject queryParamJSON);

	String findMessageInfoList(JSONObject queryParamJSON, Integer curIndex, Integer pageSize);

	String saveMessageAllInfo(JSONObject queryParamJSON);

	String saveMessageInfo(JSONObject queryParamJSON);

    String deleteMessageInfo(JSONObject queryParamJSON);

}
