package saaf.common.fmw.message.model.entities;


import java.util.List;

public class TextMessageBean {
    private String touser;
    private Long createTime;
    private String msgtype;
    private ContentMessage text;

    public TextMessageBean() {
        super();
    }

    public void setTouser(String ToUserName) {
        this.touser = ToUserName;
    }

    public String getTouser() {
        return touser;
    }

    public void setCreateTime(Long CreateTime) {
        this.createTime = CreateTime;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setMsgtype(String MsgType) {
        this.msgtype = MsgType;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setText(ContentMessage text) {
        this.text = text;
    }

    public ContentMessage getText() {
        return text;
    }
}
