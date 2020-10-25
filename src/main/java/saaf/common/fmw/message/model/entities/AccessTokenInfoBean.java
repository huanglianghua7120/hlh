package saaf.common.fmw.message.model.entities;

/**
 * token信息
 */
public class AccessTokenInfoBean {
    private String access_token;
    private Integer expires_in;

    public AccessTokenInfoBean() {
        super();
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setExpires_in(Integer expires_in) {
        this.expires_in = expires_in;
    }

    public Integer getExpires_in() {
        return expires_in;
    }
}
