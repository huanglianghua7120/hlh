package saaf.common.fmw.message.model.entities;

import java.util.List;

public class UserDetailInfoBean {
    private String subscribe;
    private String openid;
    private String nickname;
    private String sex;
    private String language;
    private String city;
    private String province;
    private String country;
    private String headimgurl;
    private Long subscribe_time;
    private String remark;
    private Integer groupId;
    private List<String> tagid_list;

    public UserDetailInfoBean() {
        super();
    }

    public void setSubscribe(String subscribe) {
        this.subscribe = subscribe;
    }

    public String getSubscribe() {
        return subscribe;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvince() {
        return province;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setSubscribe_time(Long subscribe_time) {
        this.subscribe_time = subscribe_time * 1000;
    }

    public Long getSubscribe_time() {
        return subscribe_time;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setTagid_list(List<String> tagid_list) {
        this.tagid_list = tagid_list;
    }

    public List<String> getTagid_list() {
        return tagid_list;
    }
}
