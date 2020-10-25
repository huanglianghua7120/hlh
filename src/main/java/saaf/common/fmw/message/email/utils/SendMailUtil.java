package saaf.common.fmw.message.email.utils;

import com.yhg.base.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import saaf.common.fmw.base.model.entities.SaafEmployeesEntity_HI;
import saaf.common.fmw.utils.ThreadPoolUtils;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * Created by zhy on 2018/3/13.
 */
public class SendMailUtil {
    /**
     * Message对象将存储我们实际发送的电子邮件信息，
     * Message对象被作为一个MimeMessage对象来创建并且需要知道应当选择哪一个JavaMail session。
     */
    private MimeMessage message;
    private MimeMessageHelper messageHelper;
    private JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

    /***
     * 邮件是既可以被发送也可以被受到。JavaMail使用了两个不同的类来完成这两个功能：Transport 和 Store。
     * Transport 是用来发送信息的，而Store用来收信。这里我们只需要用到Transport对象。
     */
    private String mailHost="";
    private String mailSign="";
    private String mailPort="";
    private String sender_username="";
    private String sender_password="";
    private Properties prop  = new Properties();
    private static final Logger LOGGER = LoggerFactory.getLogger(SendMailUtil.class);
    private static final SendMailUtil SEND_MAIL_UTIL = new SendMailUtil(true);
    /*
     * 初始化方法
     */
    public SendMailUtil(boolean debug) {
        InputStream in = SendMailUtil.class.getResourceAsStream("/mailConfig.properties");
        try {
            prop.load(in);
            this.mailHost = prop.getProperty("email_host");
            this.mailSign = prop.getProperty("email_sign");
            this.mailPort = prop.getProperty("email_port");
            this.sender_username = prop.getProperty("email_email");
            this.sender_password = prop.getProperty("email_pass");
            //加入身份验证信息
            Properties properties = new Properties();
            mailSender.setPort(Integer.parseInt(this.mailPort));
            mailSender.setHost(this.mailHost);
            mailSender.setUsername(this.sender_username);
            mailSender.setPassword(this.sender_password);
            mailSender.setDefaultEncoding("UTF-8");
            properties.setProperty("mail.smtp.timeout", "30000");
            properties.setProperty("mail.smtp.auth", "true");
            mailSender.setJavaMailProperties(properties);
        }catch (IOException e) {
            LOGGER.error("未知错误:{}", e);
        }
    }
    /**
     * 发送邮件
     * @param subject
     *            邮件主题
     * @param emailContent
     *            邮件内容
     * @param toEmailAddress
     *            收件人地址
     */
    public void doSendHtmlEmail(final String subject,final String emailContent,final String[] toEmailAddress){
        if(null == toEmailAddress || toEmailAddress.length<=0){
            LOGGER.debug("暂无邮件地址");
            return;
        }
        Set<String> set = new HashSet<>();
        for(String k:toEmailAddress){
            if(null != k && !"".equals(k)){
                set.add(k);
            }
        }
        if(null == set || set.isEmpty()){
            LOGGER.debug("暂无邮件地址");
            return;
        }
        final String[] emailAddress = new String[set.size()];
        Integer count = 0;
        for(String k:set){
            emailAddress[count] = k;
            count++;
        }
        final String userName = this.sender_username;
        final String personal = this.mailSign;
        try{
            ThreadPoolUtils.getThreadPool().submit(new Runnable() {
                @Override
                public void run() {
                    try{
                        message = mailSender.createMimeMessage();
                        messageHelper = new MimeMessageHelper(message,true,"UTF-8");
                        String mailFrom = userName;
                        if(mailFrom.endsWith("@scc.com")){
                            mailFrom = mailFrom + ".cn";
                        }
                        //发送人
                        messageHelper.setFrom(mailFrom,personal);
                        //收件人
                        messageHelper.setTo(emailAddress);
                        //邮件主题
                        messageHelper.setSubject(subject);
                        //邮件内容
                        messageHelper.setText(emailContent,true);
                        //保存邮件
                        message.saveChanges();
                        mailSender.send(message);
                        LOGGER.info("doSendHtmlEmail send email successfully "+subject);
                    }catch (Exception e){
                        LOGGER.error("未知错误:{}", e);
                    }
                }
            });
        }catch (Exception e){
            LOGGER.error("未知错误:{}", e);
        }
    }

    /**
     *
     * @Title: getAddress
     * @Description: 遍历收件人信息
     * @param emilAddress
     * @return
     * @throws Exception
     * @return: Address[]
     */
    private static Address[] getAddress(String[] emilAddress) throws Exception {
        Address[] address = new Address[emilAddress.length];
        for (int i = 0; i < address.length; i++) {
            address[i] = new InternetAddress(emilAddress[i]);
        }
        return address;
    }

    /**
     * 获取邮件工具的实例
     * @return 返回邮件工具的实例
     */
    public static SendMailUtil getInstance() {
        return SEND_MAIL_UTIL;
    }

    /**
     * 审批驳回操作后，给员工发送邮件
     *
     * @param employeeInfo 员工信息
     * @param processName  流程名称
     * @param approval     审批还是驳回
     * @author xuwen
     * @date 2019/4/2
     */
    public static void sendApprovalEmailToEmployee(SaafEmployeesEntity_HI employeeInfo, final String processName,final String processNumber, final boolean approval) {
        if (employeeInfo == null) {
            LOGGER.warn("employeeInfo is null，processName:"+processName+",processNumber:"+processNumber+",approval:"+approval);
            return;
        }
        final String employeeName = employeeInfo.getEmployeeName();
        final String email = employeeInfo.getEmail();
        SEND_MAIL_UTIL.doSendHtmlEmail(processName + "结果", "<p>您好！</p>" + "尊敬的" + employeeName + "，您的" + processName + "流程：" + processNumber + "，已经" + (approval ? "审核通过" : "被驳回") + "，请知悉。", new String[]{email});
    }

    public static void main(String[] args) {
        SendMailUtil se = new SendMailUtil(true);// 开启调试
        se.doSendHtmlEmail("SRM系统代码测试邮箱"+ DateUtil.date2Str(new Date()), "<p>恭喜您，成功续费超级会员服务 (2020-03-13 17:39:00)！</p><br/>"
                        + "<p>您于2020-03-13 8:方法的:00通过预付费成功续费1个月超级会员服务，您的超级会员服务有效期至2021-04-06。</p><br/>"
                        + "<p>开通年费会员即送200点成长值，立享更高QQ等级加速、更快成长加速、千人群等专属特权！ [查看详情]</p>",
                new String[]{"sys@scc.com"});
    }
}