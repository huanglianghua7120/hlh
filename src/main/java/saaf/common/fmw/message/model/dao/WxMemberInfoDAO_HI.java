package saaf.common.fmw.message.model.dao;

import com.yhg.hibernate.core.dao.ViewObjectImpl;

import saaf.common.fmw.message.model.entities.WxMemberInfoEntity_HI;

import org.springframework.stereotype.Component;

@Component("wxMemberInfoDAO_HI")
public class WxMemberInfoDAO_HI extends ViewObjectImpl<WxMemberInfoEntity_HI> {
    public WxMemberInfoDAO_HI() {
        super();
    }

    @Override
    public Object save(WxMemberInfoEntity_HI entity) {
        return super.save(entity);
    }
}
