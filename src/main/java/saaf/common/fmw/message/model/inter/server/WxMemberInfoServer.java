package saaf.common.fmw.message.model.inter.server;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import saaf.common.fmw.message.model.dao.WxMemberInfoDAO_HI;
import saaf.common.fmw.message.model.entities.WxMemberInfoEntity_HI;
import saaf.common.fmw.message.model.inter.IWxMemberInfo;
import saaf.common.fmw.message.utils.SaafToolUtils;

import com.yhg.base.utils.SToolUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component("wxMemberInfoServer")
public class WxMemberInfoServer implements IWxMemberInfo {
    private static final Logger LOGGER = LoggerFactory.getLogger(WxMemberInfoServer.class);
    @Autowired
    private WxMemberInfoDAO_HI wxMemberInfoDAO_HI;

    public WxMemberInfoServer() {
        super();
    }

    public Set<String> findWxMemberInfoInfo(JSONObject queryParamJSON) {
        Map<String, Object> queryParamMap = SToolUtils.fastJsonObj2Map(queryParamJSON);
        List<WxMemberInfoEntity_HI> findListResult = wxMemberInfoDAO_HI.findList(" from WxMemberInfoEntity_HI ", queryParamMap);
        Set<String> resultSet = new HashSet<String>();
        for(int i=0; i<findListResult.size(); i++){
            resultSet.add(findListResult.get(i).getOpenid());
        }
        //String resultData = JSON.toJSONString(findListResult);
        //JSONObject resultStr = SToolUtils.convertResultJSONObj("S", "", findListResult.size(), resultData);
        return resultSet;
    }

    public String saveWxMemberInfoInfo(JSONObject queryParamJSON) {
        WxMemberInfoEntity_HI wxMemberInfoEntity_HI = JSON.parseObject(queryParamJSON.toString(), WxMemberInfoEntity_HI.class);
        Object resultData = wxMemberInfoDAO_HI.save(wxMemberInfoEntity_HI);
        JSONObject resultStr = SToolUtils.convertResultJSONObj("S", "", 1, resultData);
        return resultStr.toString();
    }

    public static void main(String[] args) {
        WxMemberInfoServer wxMemberInfoServer = (WxMemberInfoServer)SaafToolUtils.context.getBean("wxMemberInfoServer");
        JSONObject paramJSON = new JSONObject();
        //paramJSON.put("ordercode", 1);
        //paramJSON.put("ordercode", 1);
        //paramJSON.put("tid", 1);
        Set<String> resultStr = wxMemberInfoServer.findWxMemberInfoInfo(paramJSON);
        //LOGGER.info(resultStr);
        System.out.println(resultStr);
    }
}
