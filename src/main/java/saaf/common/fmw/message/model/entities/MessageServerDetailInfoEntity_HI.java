package saaf.common.fmw.message.model.entities;

import javax.persistence.*;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * MessageServerDetailInfoEntity_HI Entity Object
 * Fri Jul 07 17:07:30 CST 2017  Auto Generate
 */
@Entity
@Table(name = "message_server_detail_info")
public class MessageServerDetailInfoEntity_HI {
    private Integer msdId;
    private String msdWayCode;
    private String msdAttributeKey;
    private String msdAttributeValue;
    private Integer versionNum;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date creationDate;
    private Integer createdBy;
    private Integer lastUpdatedBy;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateDate;
    private Integer operatorUserId;
    private Integer  lastUpdateLogin;

    public void setMsdId(Integer msdId) {
        this.msdId = msdId;
    }

    @Id
    @SequenceGenerator(name = "MESSAGE_SERVER_DETAIL_INFO_S", sequenceName = "MESSAGE_SERVER_DETAIL_INFO_S", allocationSize = 1)
    @GeneratedValue(generator = "MESSAGE_SERVER_DETAIL_INFO_S", strategy = GenerationType.SEQUENCE)
    @Column(name = "msd_id", nullable = false, length = 11)
    public Integer getMsdId() {
        return msdId;
    }

    public void setMsdWayCode(String msdWayCode) {
        this.msdWayCode = msdWayCode;
    }

    @Column(name = "msd_way_code", nullable = true, length = 100)
    public String getMsdWayCode() {
        return msdWayCode;
    }

    public void setMsdAttributeKey(String msdAttributeKey) {
        this.msdAttributeKey = msdAttributeKey;
    }

    @Column(name = "msd_attribute_key", nullable = true, length = 100)
    public String getMsdAttributeKey() {
        return msdAttributeKey;
    }

    public void setMsdAttributeValue(String msdAttributeValue) {
        this.msdAttributeValue = msdAttributeValue;
    }

    @Column(name = "msd_attribute_value", nullable = true, length = 500)
    public String getMsdAttributeValue() {
        return msdAttributeValue;
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

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    @Column(name = "created_by", nullable = true, length = 11)
    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setLastUpdatedBy(Integer lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    @Column(name = "last_updated_by", nullable = true, length = 11)
    public Integer getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    @Column(name = "last_update_date", nullable = true, length = 0)
    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }
    public void setOperatorUserId(Integer operatorUserId) {
    	this.operatorUserId = operatorUserId;
    }

    @Transient	
    public Integer getOperatorUserId() {
    	return operatorUserId;
    }
    public void setLastUpdateLogin(Integer lastUpdateLogin) {
    	this.lastUpdateLogin = lastUpdateLogin;
    }
    @Transient	
    public Integer getLastUpdateLogin() {
    	return lastUpdateLogin;
    }

}

