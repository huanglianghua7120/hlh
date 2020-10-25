package saaf.common.fmw.message.model.inter;

import java.util.Map;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSONObject;

import com.yhg.hibernate.core.paging.Pagination;

public interface IMessageSendMarkInfo {

    String findMessageSendMarkInfoInfo(JSONObject queryParamJSON,Integer curIndex,Integer pageSize);

    String saveMessageSendMarkInfoInfo(JSONObject queryParamJSON);
    
    String saveBaiduPushSendInfo(JSONObject queryParamJSON,Map<String, String> sessionMap);
    /**
     * 创建一个推送推送（例如xls导出下载推送，无需创建消息表的信息）
     *
     * @param desc       通知描述
     * @param messageTex 通知内容
     * @param personSql  选择人员的sql
     * @return
     * @throws Exception
     */
    String savePushOfNoticeInfo(JSONObject paramJSON,Map<String, String> sessionMap) throws Exception;
    



}
