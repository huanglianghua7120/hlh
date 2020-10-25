package saaf.common.fmw.message.model.inter;

import javax.ws.rs.FormParam;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.yhg.hibernate.core.paging.Pagination;

public interface IMessageTemplatesNoticeInfo{

	String findMessageTemplatesNoticeInfoInfo(JSONObject queryParamJSON,Integer curIndex,Integer pageSize);

	String saveMessageTemplatesNoticeInfoInfo(JSONObject queryParamJSON);
	
	 String deleteMessageTemplatesInfo(JSONObject queryParamJSON);
	 

}
