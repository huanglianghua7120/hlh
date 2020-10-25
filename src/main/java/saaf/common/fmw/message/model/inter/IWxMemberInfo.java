package saaf.common.fmw.message.model.inter;

import com.alibaba.fastjson.JSONObject;

import com.yhg.hibernate.core.paging.Pagination;

import java.util.Set;

public interface IWxMemberInfo {

    Set<String> findWxMemberInfoInfo(JSONObject queryParamJSON);

    String saveWxMemberInfoInfo(JSONObject queryParamJSON);

}
