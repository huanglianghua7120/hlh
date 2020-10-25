package saaf.common.fmw.message.model.dao;

import com.yhg.hibernate.core.dao.ViewObjectImpl;

import saaf.common.fmw.message.model.entities.MessageServerInfoEntity_HI;

import org.springframework.stereotype.Component;

@Component("messageServerInfoDAO_HI")
public class MessageServerInfoDAO_HI extends ViewObjectImpl<MessageServerInfoEntity_HI> {
    public MessageServerInfoDAO_HI() {
        super();
    }

    @Override
    public Object save(MessageServerInfoEntity_HI entity) {
        return super.save(entity);
    }
}
