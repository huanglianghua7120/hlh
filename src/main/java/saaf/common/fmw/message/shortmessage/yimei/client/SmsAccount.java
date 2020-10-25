package saaf.common.fmw.message.shortmessage.yimei.client;

import net.sf.json.JSONObject;

public class SmsAccount {
    private String softwareSerialNo; //序列号
    private String key; //注册序列号时设置的key值
    private String password; //密码
    private String url; //短信接口地址

    public SmsAccount() {

    }

    public SmsAccount(String softwareSerialNo, String key, String password, String url) {
        this.softwareSerialNo = softwareSerialNo;
        this.key = key;
        this.password = password;
        this.url = url;
    }

    public void setSoftwareSerialNo(String softwareSerialNo) {
        this.softwareSerialNo = softwareSerialNo;
    }

    public String getSoftwareSerialNo() {
        return softwareSerialNo;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String toJsonString() {
        return JSONObject.fromObject(this).toString();
    }
}
