package saaf.common.fmw.message.model.entities;

import java.util.List;

public class UserOpenIdDataInfo {
    private List<String> openid;

    public UserOpenIdDataInfo() {
        super();
    }

    public void setOpenid(List<String> openid) {
        this.openid = openid;
    }

    public List<String> getOpenid() {
        return openid;
    }
}
