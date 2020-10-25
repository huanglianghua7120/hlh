package saaf.common.fmw.message.shortmessage.yimei.client;

import net.sf.json.JSONObject;

public class ShortMessageBean {
    private String mobile;
    private String content;

    /**
     * 定时发送时间：yyyy-MM-dd HH:mm:ss
     * 如:2010-05-14 10:30:00
     */
    private String timer = "2012-11-05 20:39:10";

    /**
     *群发短信时 使用
     */
    private String[] mobiles;

    /**
     * 短信请求在队列中执行过的次数，失败一次次数+1
     */
    private Integer runCount = 0;

    public ShortMessageBean() {

    }

    public ShortMessageBean(String mobile) {
        this.mobile = mobile;
    }

    public ShortMessageBean(String mobile, String content) {
        this.mobile = mobile;
        this.content = content;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getRunCount() {
        return runCount;
    }

    public void setRunCount(Integer runCount) {
        this.runCount = runCount;
    }

    public String[] getMobiles() {
        return mobiles;
    }

    public void setMobiles(String[] mobiles) {
        this.mobiles = mobiles;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = JSONObject.fromObject(this);
        return jsonObject.toString();
    }
}
