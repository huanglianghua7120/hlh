package saaf.common.fmw.message.utils;

public class MeetBean {
    private String meetname; // 要点名称
    private String meetid; // 要点编号
    private String meethine; // 要点提示内容
    private String meettime; // 处置时间
    private String meethint;
    private String meetLevel;
    public void setMeetname(String meetname) {
        this.meetname = meetname;
    }

    public String getMeetname() {
        return meetname;
    }

    public void setMeetid(String meetid) {
        this.meetid = meetid;
    }

    public String getMeetid() {
        return meetid;
    }

    public void setMeethine(String meethine) {
        this.meethine = meethine;
    }

    public String getMeethine() {
        return meethine;
    }

    public void setMeettime(String meettime) {
        this.meettime = meettime;
    }

    public String getMeettime() {
        return meettime;
    }

    public void setMeethint(String meethint) {
        this.meethint = meethint;
    }

    public String getMeethint() {
        return meethint;
    }

    public void setMeetLevel(String meetLevel) {
        this.meetLevel = meetLevel;
    }

    public String getMeetLevel() {
        return meetLevel;
    }
}
