package saaf.common.fmw.message.model.inter.server;

import com.base.sie.common.exception.email.EmailException;

import saaf.common.fmw.message.model.entities.MailSenderInfoBean;
import saaf.common.fmw.message.model.entities.MessageSendMarkInfoEntity_HI;
import saaf.common.fmw.message.utils.SaafToolUtils;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.codehaus.jettison.json.JSONException;

import com.yhg.activemq.framework.queue.ProducerServiceImpl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*===========================================================+
  |   Copyright (c) 2012 赛意信息科技有限公司                                         |
  +===========================================================+
  |  HISTORY                                                                        |
  | ============ ====== ============  ===========================                   |
  |  Date                     Ver.        Administrator                   Content          |
  | ============ ====== ============  ===========================                   |
  |  Jun 23, 2015            1.0           XXX                      Creation        |
  | java发送邮件提供的restful服务
  | 客户端需要传递SUBJECT、CONTENT、TO_LIST、CS_LIST、MS_LIST、ATTACHMENT_LIST等信息封装成JSON对象即可是将邮件发送处出去
 +===========================================================*/
@Component("emailSender")
public class EmailSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSender.class);
    @Autowired
    private MessageSendMarkInfoServer messageSendMarkInfoServer;
    @Autowired
    private org.apache.activemq.command.ActiveMQQueue emailSendDestination;
    @Autowired
    private ProducerServiceImpl emailSendProducerService;   
    @Autowired
    private MailSenderInfoBean mailSenderInfoBean;
    public EmailSender() {
        super();
    }

    public String sendEmail(String emailInfo) {
        return sendEmail(emailInfo, false);
    }

    public String sendEmail(String emailInfo, boolean useMQSendFlag) {
        if(useMQSendFlag){
            emailSendProducerService.sendMessage(emailSendDestination, emailInfo);
            return "已进入消息队列";
        }
        //MailSenderInfoBean mailSenderInfo = (MailSenderInfoBean)SaafToolUtils.context.getBean("mailSenderInfoBean");
        LOGGER.warn("sendEmail==>" + emailInfo);
        JSONObject emailInfoJson = JSONObject.fromObject(emailInfo);
        String subject = emailInfoJson.getString("SUBJECT");
        String content = emailInfoJson.getString("CONTENT");
        JSONArray toList_ = emailInfoJson.getJSONArray("TO_LIST");
        String to = null;
        List<String> toList = new ArrayList<String>();
        for (int i = 0; i < toList_.size(); i++) {
            to = toList_.getString(i);
            toList.add(to);
        }
        if (emailInfoJson.containsKey("CS_LIST")) {
            JSONArray csList_ = emailInfoJson.getJSONArray("CS_LIST");
            List<String> csList = new ArrayList<String>();
            String cs = null;
            for (int i = 0; i < csList_.size(); i++) {
                cs = csList_.getString(i);
                csList.add(cs);
            }
            mailSenderInfoBean.setCsList(csList);
        }
        if (emailInfoJson.containsKey("MS_LIST")) {
            List<String> msList = new ArrayList<String>();
            JSONArray msList_ = emailInfoJson.getJSONArray("MS_LIST");
            String ms = null;
            for (int i = 0; i < msList_.size(); i++) {
                ms = msList_.getString(i);
                msList.add(ms);
            }
            mailSenderInfoBean.setMsList(msList);
        }
        if (emailInfoJson.containsKey("ATTACHMENT_LIST")) {
            JSONArray attachmentList_ = emailInfoJson.getJSONArray("ATTACHMENT_LIST");
            List<String> attachmentList = new ArrayList<String>();
            String attachment = null;
            for (int i = 0; i < toList.size(); i++) {
                attachment = attachmentList_.getString(i);
                attachmentList.add(attachment);
            }
            mailSenderInfoBean.setArrArchiveList(attachmentList);
        }
        //MailSenderInfoBean mailSenderInfo = new MailSenderInfoBean();
        mailSenderInfoBean.setToList(toList);
        mailSenderInfoBean.setSubject(subject);
        mailSenderInfoBean.setContent(content);
        EmailSenderServer emailSender = new EmailSenderServer();
        try {
            emailSender.sendEmail(mailSenderInfoBean);
        } catch (EmailException e) {
            LOGGER.error(e.getMessage(), e);
        }
        String sendEmailResult = JSONObject.fromObject(mailSenderInfoBean).toString();
        MessageSendMarkInfoEntity_HI messageSendMarkInfoEntity_HI = new MessageSendMarkInfoEntity_HI();
        messageSendMarkInfoEntity_HI.setMsmMessageAttachmentName(com.alibaba.fastjson.JSONArray.toJSONString(mailSenderInfoBean.getArrArchiveList()));
        //messageSendMarkInfoEntity_HI.setMsmMessageAttachmentUrl(msmMessageAttachmentUrl);
        messageSendMarkInfoEntity_HI.setMsmMessageContent(mailSenderInfoBean.getContent());
        messageSendMarkInfoEntity_HI.setMsmMessageTitle(mailSenderInfoBean.getSubject());
        messageSendMarkInfoEntity_HI.setMsmReceiverAccount(com.alibaba.fastjson.JSONArray.toJSONString(mailSenderInfoBean.getToList()));
        messageSendMarkInfoEntity_HI.setMsmReceiverAccountBcc(com.alibaba.fastjson.JSONArray.toJSONString(mailSenderInfoBean.getMsList()));
        messageSendMarkInfoEntity_HI.setMsmReceiverAccountCc(com.alibaba.fastjson.JSONArray.toJSONString(mailSenderInfoBean.getCsList()));
        messageSendMarkInfoEntity_HI.setMsmSendDateTime(new Date());
        messageSendMarkInfoEntity_HI.setMsmSendStatus("成功");
        messageSendMarkInfoEntity_HI.setMsmSendWayCode("email");
        messageSendMarkInfoEntity_HI.setMsmSendWayName("邮件");
        messageSendMarkInfoServer.saveMessageSendMarkInfoInfo(messageSendMarkInfoEntity_HI);
        LOGGER.info("sendEmail==>" + sendEmailResult);
        return sendEmailResult;
    }

    public static void main(String[] args) throws JSONException {
        EmailSender emailSender = (EmailSender)SaafToolUtils.context.getBean("emailSender");
        org.codehaus.jettison.json.JSONObject json = new org.codehaus.jettison.json.JSONObject();
        json.put("SUBJECT", "测试 subject12");
        json.put("CONTENT", "我邮件的内容，just do test contentlll");
        org.codehaus.jettison.json.JSONArray jsonArrayTo = new org.codehaus.jettison.json.JSONArray();
       // jsonArrayTo.put("lindezhao@chinasie.com");
        jsonArrayTo.put("498694174@qq.com");
        json.put("TO_LIST", jsonArrayTo);
        //        org.codehaus.jettison.json.JSONArray jsonArrayCS = new org.codehaus.jettison.json.JSONArray();
        //        jsonArrayCS.put("yuanhaigang@chinasie.com");
        //        jsonArrayCS.put("819038993@qq.com");
        //        json.put("CS_LIST", jsonArrayCS);
        //        org.codehaus.jettison.json.JSONArray jsonArrayMS = new org.codehaus.jettison.json.JSONArray();
        //        jsonArrayMS.put("yuanhaigang@test.com");
        //        jsonArrayMS.put("819038993@163.com");
        //        json.put("MS_LIST", jsonArrayMS);

        //        org.codehaus.jettison.json.JSONArray jsonArrayAttam = new org.codehaus.jettison.json.JSONArray();
        //        jsonArrayAttam.put("D:\\MineDocuments\\SIE_Project\\Platform\\ADFBasePlatform\\Source\\SIEADFBasePlatform\\MyCommonBasic\\1.txt");
        //        jsonArrayAttam.put("D:\\MineDocuments\\SIE_Project\\Platform\\ADFBasePlatform\\Source\\SIEADFBasePlatform\\MyCommonBasic\\6.txt");
        //        json.put("ATTACHMENT_LIST", jsonArrayAttam);
        //        LOGGER.warn("main==>" + json.toString());
        emailSender.sendEmail(json.toString(), false);
    }
}
