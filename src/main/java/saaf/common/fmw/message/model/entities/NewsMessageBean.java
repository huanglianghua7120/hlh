package saaf.common.fmw.message.model.entities;

public class NewsMessageBean {
    private NewsChildMessageBean news;
    private String msgtype;
    private String touser;

    public NewsMessageBean() {
        super();
    }

    public void setNews(NewsChildMessageBean news) {
        this.news = news;
    }

    public NewsChildMessageBean getNews() {
        return news;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTouser() {
        return touser;
    }
}
