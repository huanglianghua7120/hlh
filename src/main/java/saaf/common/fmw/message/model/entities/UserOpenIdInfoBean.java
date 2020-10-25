package saaf.common.fmw.message.model.entities;

import java.util.List;

public class UserOpenIdInfoBean {
    private String total;
    private String count;
    private String next_openid;
    private UserOpenIdDataInfo data;

    public UserOpenIdInfoBean() {
        super();
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotal() {
        return total;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCount() {
        return count;
    }

    public void setNext_openid(String next_openid) {
        this.next_openid = next_openid;
    }

    public String getNext_openid() {
        return next_openid;
    }

    public void setData(UserOpenIdDataInfo data) {
        this.data = data;
    }

    public UserOpenIdDataInfo getData() {
        return data;
    }
}
