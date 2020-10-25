package saaf.common.fmw.message.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

import javax.persistence.Transient;
import javax.persistence.Version;

/**
 * WxMemberInfoEntity_HI Entity Object
 * Fri Jul 07 17:07:30 CST 2017  Auto Generate
 */
@Entity
@Table(name = "wx_member_info")
public class WxMemberInfoEntity_HI {
    private Integer wxMId;
    private Integer msWxAppId;
    private String nickname;
    private String openid;
    private String country;
    private String province;
    private String city;
    private String headimgurl;
    private Integer groupId;
    private String language;
    private String sex;
    private String remark;
    private String subscribe;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Long subscribe_time;
    private Date subscribeTime;
    private List<String> tagid_list;
    private String tagidList;
    private Integer versionNum;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date creationDate;
    private String createdBy;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdatedBy;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateDate;

    public void setWxMId(Integer wxMId) {
        this.wxMId = wxMId;
    }

    @Id
    @GeneratedValue
    @Column(name = "wx_m_id", nullable = false, length = 11)
    public Integer getWxMId() {
        return wxMId;
    }

    public void setMsWxAppId(Integer msWxAppId) {
        this.msWxAppId = msWxAppId;
    }

    @Column(name = "ms_wx_app_id", nullable = true, length = 100)
    public Integer getMsWxAppId() {
        return msWxAppId;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Column(name = "nickname", nullable = true, length = 80)
    public String getNickname() {
        return nickname;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    @Column(name = "openid", nullable = true, length = 300)
    public String getOpenid() {
        return openid;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Column(name = "country", nullable = true, length = 50)
    public String getCountry() {
        return country;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Column(name = "province", nullable = true, length = 50)
    public String getProvince() {
        return province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name = "city", nullable = true, length = 50)
    public String getCity() {
        return city;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    @Column(name = "headimgurl", nullable = true, length = 3000)
    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setGroupId(Integer groupid) {
        this.groupId = groupid;
    }

    @Column(name = "groupid", nullable = true, length = 11)
    public Integer getGroupId() {
        return groupId;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Column(name = "language", nullable = true, length = 30)
    public String getLanguage() {
        return language;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Column(name = "sex", nullable = true, length = 3)
    public String getSex() {
        return sex;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "remark", nullable = true, length = 500)
    public String getRemark() {
        return remark;
    }

    public void setSubscribe(String subscribe) {
        this.subscribe = subscribe;
    }

    @Column(name = "subscribe", nullable = true, length = 30)
    public String getSubscribe() {
        return subscribe;
    }

    public void setSubscribeTime(Date subscribeTime) {
        this.subscribeTime = subscribeTime;
    }

    @Column(name = "subscribe_time", nullable = true, length = 0)
    public Date getSubscribeTime() {
        return subscribeTime;
    }

    public void setTagidList(String tagidList) {
        this.tagidList = tagidList;
    }

    @Column(name = "tagid_list", nullable = true, length = 4000)
    public String getTagidList() {
        return tagidList;
    }

    public void setVersionNum(Integer versionNum) {
        this.versionNum = versionNum;
    }

    @Version
    @Column(name = "version_num", nullable = true, length = 11)
    public Integer getVersionNum() {
        return versionNum;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Column(name = "creation_date", nullable = true, length = 0)
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Column(name = "created_by", nullable = true, length = 255)
    public String getCreatedBy() {
        return createdBy;
    }

    public void setLastUpdatedBy(Date lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    @Column(name = "last_updated_by", nullable = true, length = 0)
    public Date getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    @Column(name = "last_update_date", nullable = true, length = 0)
    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setSubscribe_time(Long subscribe_time) {
        this.subscribe_time = subscribe_time;
    }
    @Transient
    public Long getSubscribe_time() {
        return subscribe_time;
    }

    public void setTagid_list(List<String> tagid_list) {
        this.tagid_list = tagid_list;
    }
    @Transient
    public List<String> getTagid_list() {
        return tagid_list;
    }
}

