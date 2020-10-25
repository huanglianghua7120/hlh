package saaf.common.fmw.message.model.dao;

import com.yhg.hibernate.core.dao.ViewObjectImpl;
import saaf.common.fmw.message.model.entities.MessageTemplatesNoticeInfoEntity_HI;
import org.springframework.stereotype.Component;

@Component("messageTemplatesNoticeInfoDAO_HI")
public class MessageTemplatesNoticeInfoDAO_HI extends ViewObjectImpl<MessageTemplatesNoticeInfoEntity_HI>  {
	public MessageTemplatesNoticeInfoDAO_HI() {
		super();
	}

	@Override
	public Object save(MessageTemplatesNoticeInfoEntity_HI entity) {
		return super.save(entity);
	}
}
