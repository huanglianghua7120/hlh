package saaf.common.fmw.message.model.dao;

import com.yhg.hibernate.core.dao.ViewObjectImpl;

import saaf.common.fmw.message.model.entities.MessageSendMarkInfoEntity_HI;

import org.springframework.stereotype.Component;

@Component("messageSendMarkInfoDAO_HI")
public class MessageSendMarkInfoDAO_HI extends ViewObjectImpl<MessageSendMarkInfoEntity_HI> {
    public MessageSendMarkInfoDAO_HI() {
        super();
    }

    @Override
    public Object save(MessageSendMarkInfoEntity_HI entity) {
        return super.save(entity);
    }
}
