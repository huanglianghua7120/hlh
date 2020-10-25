package saaf.common.fmw.message.model.entities;

import javax.persistence.*;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * MessageSendMarkInfoEntity_HI Entity Object
 * Fri Jul 07 17:07:30 CST 2017  Auto Generate
 */
@Entity
@Table(name = "message_send_mark_info")
public class MessageSendMarkInfoEntity_HI {
	private Integer msmId;
private String msmSendWayCode;
private String msmSendWayName;
private String msmReceiverAccount;
private String msmReceiverAccountCc;
private String msmReceiverAccountBcc;
private String msmMessageContent;
private String msmMessageTitle;
private String msmMessageAttachmentUrl;
private String msmMessageAttachmentName;
@JSONField(format = "yyyy-MM-dd HH:mm:ss")
private Date msmSendDateTime;
private String msmSendStatus;
private String msmCustomerName;
private Integer msmPrice;
private Integer versionNum;
@JSONField(format = "yyyy-MM-dd HH:mm:ss")
private Date creationDate;
private Integer createdBy;
private Integer lastUpdatedBy;
@JSONField(format = "yyyy-MM-dd HH:mm:ss")
private Date lastUpdateDate;
private Integer operatorUserId;
private Integer  lastUpdateLogin;

public void setMsmId(Integer msmId) {
	this.msmId = msmId;
}

@Id	
@SequenceGenerator(name = "MESSAGE_SEND_MARK_INFO_S", sequenceName = "MESSAGE_SEND_MARK_INFO_S", allocationSize = 1)
@GeneratedValue(generator = "MESSAGE_SEND_MARK_INFO_S", strategy = GenerationType.SEQUENCE)
@Column(name = "msm_id", nullable = false, length = 11)	
public Integer getMsmId() {
	return msmId;
}

public void setMsmSendWayCode(String msmSendWayCode) {
	this.msmSendWayCode = msmSendWayCode;
}

@Column(name = "msm_send_way_code", nullable = true, length = 100)	
public String getMsmSendWayCode() {
	return msmSendWayCode;
}

public void setMsmSendWayName(String msmSendWayName) {
	this.msmSendWayName = msmSendWayName;
}

@Column(name = "msm_send_way_name", nullable = true, length = 100)	
public String getMsmSendWayName() {
	return msmSendWayName;
}

public void setMsmReceiverAccount(String msmReceiverAccount) {
	this.msmReceiverAccount = msmReceiverAccount;
}

@Column(name = "msm_receiver_account", nullable = true, length = 400)	
public String getMsmReceiverAccount() {
	return msmReceiverAccount;
}

public void setMsmReceiverAccountCc(String msmReceiverAccountCc) {
	this.msmReceiverAccountCc = msmReceiverAccountCc;
}

@Column(name = "msm_receiver_account_cc", nullable = true, length = 400)	
public String getMsmReceiverAccountCc() {
	return msmReceiverAccountCc;
}

public void setMsmReceiverAccountBcc(String msmReceiverAccountBcc) {
	this.msmReceiverAccountBcc = msmReceiverAccountBcc;
}

@Column(name = "msm_receiver_account_bcc", nullable = true, length = 400)	
public String getMsmReceiverAccountBcc() {
	return msmReceiverAccountBcc;
}

public void setMsmMessageContent(String msmMessageContent) {
	this.msmMessageContent = msmMessageContent;
}

@Column(name = "msm_message_content", nullable = true, length = 4000)	
public String getMsmMessageContent() {
	return msmMessageContent;
}

public void setMsmMessageTitle(String msmMessageTitle) {
	this.msmMessageTitle = msmMessageTitle;
}

@Column(name = "msm_message_title", nullable = true, length = 400)	
public String getMsmMessageTitle() {
	return msmMessageTitle;
}

public void setMsmMessageAttachmentUrl(String msmMessageAttachmentUrl) {
	this.msmMessageAttachmentUrl = msmMessageAttachmentUrl;
}

@Column(name = "msm_message_attachment_url", nullable = true, length = 2000)	
public String getMsmMessageAttachmentUrl() {
	return msmMessageAttachmentUrl;
}

public void setMsmMessageAttachmentName(String msmMessageAttachmentName) {
	this.msmMessageAttachmentName = msmMessageAttachmentName;
}

@Column(name = "msm_message_attachment_name", nullable = true, length = 400)	
public String getMsmMessageAttachmentName() {
	return msmMessageAttachmentName;
}

public void setMsmSendDateTime(Date msmSendDateTime) {
	this.msmSendDateTime = msmSendDateTime;
}

@Column(name = "msm_send_date_time", nullable = true, length = 0)	
public Date getMsmSendDateTime() {
	return msmSendDateTime;
}

public void setMsmSendStatus(String msmSendStatus) {
	this.msmSendStatus = msmSendStatus;
}

@Column(name = "msm_send_status", nullable = true, length = 20)	
public String getMsmSendStatus() {
	return msmSendStatus;
}

public void setMsmCustomerName(String msmCustomerName) {
	this.msmCustomerName = msmCustomerName;
}

@Column(name = "msm_customer_name", nullable = true, length = 255)	
public String getMsmCustomerName() {
	return msmCustomerName;
}

public void setMsmPrice(Integer msmPrice) {
	this.msmPrice = msmPrice;
}

@Column(name = "msm_price", nullable = true, length = 11)	
public Integer getMsmPrice() {
	return msmPrice;
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