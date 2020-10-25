package saaf.common.fmw.message.model.entities;

import javax.persistence.*;

import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * MessageTemplatesNoticeInfoEntity_HI Entity Object
 * Wed Aug 23 11:14:18 CST 2017  Auto Generate
 */
@Entity
@Table(name = "message_templates_notice_info")
public class MessageTemplatesNoticeInfoEntity_HI {
    private Integer templatesId;
    private String templatesType;
    private String mtValuesetCode;
    private String templatesTitle;
    private String templatesContent;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date startDateAcitve;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date endDateAcitve;
    private Integer versionNum;
    private Integer createdBy;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date creationDate;
    private Integer lastUpdatedBy;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateDate;
    private Integer lastUpdatedLogin;
    private Integer operatorUserId;
    private Integer  lastUpdateLogin;
    

	public void setTemplatesId(Integer templatesId) {
		this.templatesId = templatesId;
	}

	@Id	
	@SequenceGenerator(name = "MESSAGE_TEMPLATES_NOTICE_INF_S", sequenceName = "MESSAGE_TEMPLATES_NOTICE_INF_S", allocationSize = 1)
    @GeneratedValue(generator = "MESSAGE_TEMPLATES_NOTICE_INF_S", strategy = GenerationType.SEQUENCE)
	@Column(name = "templates_id", nullable = false, length = 10)	
	public Integer getTemplatesId() {
		return templatesId;
	}

	public void setTemplatesType(String templatesType) {
		this.templatesType = templatesType;
	}

	@Column(name = "templates_type", nullable = true, length = 40)	
	public String getTemplatesType() {
		return templatesType;
	}
	@Column(name = "mt_valueset_code", nullable = true, length = 400)	
	public String getMtValuesetCode() {
		return mtValuesetCode;
	}
	
	public void setMtValuesetCode(String mtValuesetCode) {
		this.mtValuesetCode = mtValuesetCode;
	}

	public void setTemplatesTitle(String templatesTitle) {
		this.templatesTitle = templatesTitle;
	}

	@Column(name = "templates_title", nullable = true, length = 40)	
	public String getTemplatesTitle() {
		return templatesTitle;
	}

	public void setTemplatesContent(String templatesContent) {
		this.templatesContent = templatesContent;
	}

	@Column(name = "templates_content", nullable = true, length = 0)	
	public String getTemplatesContent() {
		return templatesContent;
	}

	public void setStartDateAcitve(Date startDateAcitve) {
		this.startDateAcitve = startDateAcitve;
	}

	@Column(name = "start_date_acitve", nullable = true, length = 0)	
	public Date getStartDateAcitve() {
		return startDateAcitve;
	}

	public void setEndDateAcitve(Date endDateAcitve) {
		this.endDateAcitve = endDateAcitve;
	}

	@Column(name = "end_date_acitve", nullable = true, length = 0)	
	public Date getEndDateAcitve() {
		return endDateAcitve;
	}

	public void setVersionNum(Integer versionNum) {
		this.versionNum = versionNum;
	}

	@Version
	@Column(name = "version_num", nullable = true, length = 11)	
	public Integer getVersionNum() {
		return versionNum;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "created_by", nullable = true, length = 11)	
	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreationDate(Date createdDate) {
		this.creationDate = createdDate;
	}

	@Column(name = "creation_date", nullable = true, length = 0)	
	public Date getCreationDate() {
		return creationDate;
	}

	public void setLastUpdatedBy(Integer lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	@Column(name = "last_updated_by", nullable = true, length = 11)	
	public Integer getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdateDate(Date lastUpdationDate) {
		this.lastUpdateDate = lastUpdationDate;
	}

	@Column(name = "last_update_date", nullable = true, length = 0)	
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdatedLogin(Integer lastUpdatedLogin) {
		this.lastUpdatedLogin = lastUpdatedLogin;
	}

	@Column(name = "last_updated_login", nullable = true, length = 11)	
	public Integer getLastUpdatedLogin() {
		return lastUpdatedLogin;
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

