package saaf.common.fmw.message.model.entities;

public class UserRemarkInfo {
    private String openid;
    private String remark;

    public UserRemarkInfo() {

    }

    public UserRemarkInfo(String openId, String remark) {
        this.openid = openId;
        this.remark = remark;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }
}
