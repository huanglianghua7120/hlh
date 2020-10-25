package saaf.common.fmw.message.model.dao;

import com.yhg.hibernate.core.dao.ViewObjectImpl;

import saaf.common.fmw.message.model.entities.MessageServerDetailInfoEntity_HI;

import org.springframework.stereotype.Component;

@Component("messageServerDetailInfoDAO_HI")
public class MessageServerDetailInfoDAO_HI extends ViewObjectImpl<MessageServerDetailInfoEntity_HI> {
    public MessageServerDetailInfoDAO_HI() {
        super();
    }

    @Override
    public Object save(MessageServerDetailInfoEntity_HI entity) {
        return super.save(entity);
    }
}
