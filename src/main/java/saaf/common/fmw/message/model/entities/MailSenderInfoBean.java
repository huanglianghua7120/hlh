package saaf.common.fmw.message.model.entities;

import com.alibaba.fastjson.JSONObject;

import com.base.sie.common.utils.EncipherDataUtil;

import saaf.common.fmw.message.model.inter.server.MessageServerDetailInfoServer;

import com.yhg.base.utils.PropertiesUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//import oracle.adf.share.logging.ADFLogger;
/*===========================================================+
  |   Copyright (c) 2012 赛意信息科技有限公司                                         |
+===========================================================+
  |  HISTORY                                                                        |
  | ============ ====== ============  ===========================                   |
  |  Date                     Ver.        Administrator                   Content          |
  | ============ ====== ============  ===========================                   |
  |  Jun 23, 2015            1.0           XXX                      Creation        |
  |  待发送邮件内容的基本信息
 +===========================================================*/
@Component("mailSenderInfoBean")
public class MailSenderInfoBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailSenderInfoBean.class);
    public static final String SERVICE_HOST_NAME = "service_host_name";
    public static final String SERVICE_PORT = "service_port";
    public static final String SERVICE_USER_NAME = "service_user_name";
    public static final String SERVICE_PASSWORD = "service_password";

    private List<String> toList;
    private List<String> csList;
    private List<String> msList;
    private List<String> arrArchiveList;
    private String subject;
    private String content;
    private Date sendDate;
    private boolean validate = true;
    private boolean sendSuccessFlag = false;
    private String errorInfo;
    
    @Autowired
    private MessageServerDetailInfoServer messageServerDetailInfoServer;
    
    public static void main(String[] args) {
        MailSenderInfoBean bean = new MailSenderInfoBean();
        bean.getProperties();
    }

    public Properties getProperties() {
        JSONObject queryParamJSON = new JSONObject();
       //queryParamJSON.put("msdWayCode", "email");
        queryParamJSON.put("msType", "EMAIL");
        queryParamJSON.put("isEnabled", "Y");
        Map<String, String> detailInfoInfo = messageServerDetailInfoServer.findServerInfo(queryParamJSON);
        //TODO 可以优化采用redis等缓存技术将相关信息缓存到redis中 以减轻数据库的压力
        //String filePath = MailSenderInfoBean.class.getName();
        //filePath = filePath.replaceAll("\\.", "/");
        //filePath = filePath.replaceAll(MailSenderInfoBean.class.getSimpleName(), "email_resource.properties");
        //LOGGER.warn("saaf.common.fmw.service.mail.beans.MailSenderInfoBean-->getProperties====>filePath-->" + filePath);
        //Properties loadProperties = PropertiesUtils.loadProperties(filePath);
        String mailServerHost = detailInfoInfo.get(SERVICE_HOST_NAME);// loadProperties.getProperty(SERVICE_HOST_NAME);
        String mailServerPort = detailInfoInfo.get(SERVICE_PORT); //loadProperties.getProperty(SERVICE_PORT);
        String userName = detailInfoInfo.get(SERVICE_USER_NAME); //loadProperties.getProperty(SERVICE_USER_NAME);
        String password = detailInfoInfo.get(SERVICE_PASSWORD); //loadProperties.getProperty(SERVICE_PASSWORD);
        try {
            password = EncipherDataUtil.decryptionStringData(password);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        Properties properties = new Properties();
  /*      properties.put("mail.host", mailServerHost);//smtp.exmail.qq.com
        properties.put("mail.smtp.port", mailServerPort);//465
        properties.put("mail.smtp.auth", validate ? "true" : "false");
        properties.put("mail.smtp.userName", userName);//yuanhaigang@chinasie.com
        properties.put("mail.smtp.password", password);//gavin819038939
*/       
      
         //企业邮箱测试结果 qq邮箱 不要设置端口
        //properties.put("mail.smtp.port", 465);//qq企业邮箱465
        // 发送服务器需要身份验证
        properties.put("mail.smtp.auth", validate ? "true" : "false");
        properties.put("mail.smtp.userName",userName);
       //密码
        properties.put("mail.smtp.password", password);
        // 发送邮件协议名称
        properties.setProperty("mail.transport.protocol", "smtp");
        // 设置邮件服务器主机名
        properties.setProperty("mail.host",mailServerHost);
        
        //126
        /*    
        properties.put("mail.smtp.auth", validate ? "true" : "false");
        properties.put("mail.smtp.port", 25);// 126邮箱端口25
        properties.put("mail.smtp.userName","475438028@126.com");
        properties.put("mail.smtp.password", "*****");// 密码
        // 发送邮件协议名称
        properties.setProperty("mail.transport.protocol", "smtp");
        // 设置邮件服务器主机名
        properties.setProperty("mail.host", "smtp.126.com");*/
        //个人qq邮箱
        /*    
        properties.put("mail.smtp.auth", validate ? "true" : "false");
        properties.put("mail.smtp.userName","475438028@qq.com");
        properties.put("mail.smtp.password", "ennwtglhxrsubjic");// 授权码
        // 发送邮件协议名称
        properties.setProperty("mail.transport.protocol", "smtp");
        // 设置邮件服务器主机名
        properties.setProperty("mail.host", "smtp.qq.com");*/

        return properties;
    }

    public boolean isValidate() {
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    public void setToList(List<String> toList) {
        this.toList = toList;
    }

    public List<String> getToList() {
        return toList;
    }

    public void setCsList(List<String> csList) {
        this.csList = csList;
    }

    public List<String> getCsList() {
        return csList;
    }

    public void setMsList(List<String> msList) {
        this.msList = msList;
    }

    public List<String> getMsList() {
        return msList;
    }

    public void setArrArchiveList(List<String> arrArchiveList) {
        this.arrArchiveList = arrArchiveList;
    }

    public List<String> getArrArchiveList() {
        return arrArchiveList;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendSuccessFlag(boolean sendSuccessFlag) {
        this.sendSuccessFlag = sendSuccessFlag;
    }

    public boolean isSendSuccessFlag() {
        return sendSuccessFlag;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public String getErrorInfo() {
        return errorInfo;
    }
}
