package saaf.common.fmw.message.model.entities;

import javax.persistence.*;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * MessageServerInfoEntity_HI Entity Object
 * Fri Jul 07 17:07:30 CST 2017  Auto Generate
 */
@Entity
@Table(name = "message_server_info")
public class MessageServerInfoEntity_HI {
    private Integer msId;
    private String msWayCode;
    private String msType;
    private String msWxAppId;
    private String msWayName;
    private String msWayDesc;
    private String msTokenCode;
    private Long msTokenStartTime;
    private String msCustomerName;
    private Integer msPrice;
    private String isEnabled;
    private Integer versionNum;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date creationDate;
    private Integer createdBy;
    private Integer lastUpdatedBy;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateDate;
    private Integer operatorUserId;
    private Integer  lastUpdateLogin;

    public void setMsId(Integer msId) {
        this.msId = msId;
    }

    @Id
    @SequenceGenerator(name = "MESSAGE_SERVER_INFO_S", sequenceName = "MESSAGE_SERVER_INFO_S", allocationSize = 1)
    @GeneratedValue(generator = "MESSAGE_SERVER_INFO_S", strategy = GenerationType.SEQUENCE)
    @Column(name = "ms_id", nullable = false, length = 11)
    public Integer getMsId() {
        return msId;
    }

    public void setMsWayCode(String msWayCode) {
        this.msWayCode = msWayCode;
    }

    @Column(name = "ms_way_code", nullable = true, length = 100)
    public String getMsWayCode() {
        return msWayCode;
    }
    
    public void setMsType(String msType) {
        this.msType = msType;
    }

    @Column(name = "ms_type", nullable = true, length = 40)
    public String getMsType() {
        return msType;
    }

    public void setMsWxAppId(String msWxAppId) {
        this.msWxAppId = msWxAppId;
    }

    @Column(name = "ms_wx_app_id", nullable = true, length = 100)
    public String getMsWxAppId() {
        return msWxAppId;
    }

    public void setMsWayName(String msWayName) {
        this.msWayName = msWayName;
    }

    @Column(name = "ms_way_name", nullable = true, length = 100)
    public String getMsWayName() {
        return msWayName;
    }

    public void setMsWayDesc(String msWayDesc) {
        this.msWayDesc = msWayDesc;
    }

    @Column(name = "ms_way_desc", nullable = true, length = 400)
    public String getMsWayDesc() {
        return msWayDesc;
    }

    public void setMsTokenCode(String msTokenCode) {
        this.msTokenCode = msTokenCode;
    }

    @Column(name = "ms_token_code", nullable = true, length = 400)
    public String getMsTokenCode() {
        return msTokenCode;
    }

    public void setMsTokenStartTime(Long msTokenStartTime) {
        this.msTokenStartTime = msTokenStartTime;
    }

    @Column(name = "ms_token_start_time", nullable = true, length = 20)
    public Long getMsTokenStartTime() {
        return msTokenStartTime;
    }

    public void setVersionNum(Integer versionNum) {
        this.versionNum = versionNum;
    }

    @Version
    @Column(name = "version_num", nullable = true, length = 11)
    public Integer getVersionNum() {
        return versionNum;
    }
    
    
    @Column(name = "is_enabled ", nullable = true, length = 24)
    public String getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
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

    public void setMsCustomerName(String msCustomerName) {
        this.msCustomerName = msCustomerName;
    }
    @Column(name = "ms_customer_name", nullable = true, length = 255)
    public String getMsCustomerName() {
        return msCustomerName;
    }

    public void setMsPrice(Integer msPrice) {
        this.msPrice = msPrice;
    }
    @Column(name = "ms_price", nullable = true, length = 11)
    public Integer getMsPrice() {
        return msPrice;
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
