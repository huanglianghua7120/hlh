package saaf.common.fmw.message.model.entities;

public class ConcernUserCount {
    private String ref_date;
    private Integer user_source;
    private Integer cumulate_user;

    public ConcernUserCount() {
        super();
    }

    public void setRef_date(String ref_date) {
        this.ref_date = ref_date;
    }

    public String getRef_date() {
        return ref_date;
    }

    public void setUser_source(Integer user_source) {
        this.user_source = user_source;
    }

    public Integer getUser_source() {
        return user_source;
    }

    public void setCumulate_user(Integer cumulate_user) {
        this.cumulate_user = cumulate_user;
    }

    public Integer getCumulate_user() {
        return cumulate_user;
    }
}
