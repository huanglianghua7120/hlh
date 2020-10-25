package saaf.common.fmw.message.model.inter.server;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yhg.activemq.framework.queue.ProducerServiceImpl;
import com.yhg.base.utils.SToolUtils;

import saaf.common.fmw.message.model.entities.MessageSendMarkInfoEntity_HI;
import saaf.common.fmw.message.utils.SaafToolUtils;

//import com.yhg.activemq.framework.queue.ProducerServiceImpl;

import java.net.MalformedURLException;

import java.rmi.RemoteException;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.rpc.ServiceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component("shortMessageSender")
public class ShortMessageSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShortMessageSender.class);
    private final static Log logger = LogFactory.getLog(ShortMessageSender.class);
    @Autowired
    private ShortMessageServer shortMessageServer;
    @Autowired
    private MessageSendMarkInfoServer messageSendMarkInfoServer;    
    @Autowired
    private org.apache.activemq.command.ActiveMQQueue shortMessageSendDestination;
    @Autowired
    private ProducerServiceImpl shortMessageSendProducerService;    
    private static Map<String, String> shortMessageServerInfo;

    public ShortMessageSender() {

    }
    private synchronized void shortMessageServerInfo(){
        if(null == shortMessageServerInfo){
            shortMessageServerInfo = shortMessageServer.queryServerInfo();
        }
    }

    public int smsSendSingleMsg(String[] mobiles, String subject,String content) throws MalformedURLException, RemoteException, ServiceException {
        return smsSendSingleMsg(mobiles,subject, content, false);
    }
    
    public int smsSendSingleMsg(String[] mobiles,String subject, String content, boolean useMQSendFlag) throws MalformedURLException, RemoteException, ServiceException {
    	if(null == shortMessageServerInfo){
            shortMessageServerInfo();
        }
        String softwareSerialNo = shortMessageServerInfo.get(ShortMessageServer.SOFTWARE_SERIAL_NO);
        String key = shortMessageServerInfo.get(ShortMessageServer.KEY);
        return smsSendSingleMsg(mobiles, subject,content, softwareSerialNo, key, useMQSendFlag);
    }
    
    public int smsSendSingleMsg(String[] mobiles,String subject, String content, String softwareSerialNo, String key, boolean useMQSendFlag) throws MalformedURLException, RemoteException, ServiceException {
        if(useMQSendFlag){
            JSONObject josnObject = new JSONObject();
            josnObject.put("mobiles", mobiles);
            josnObject.put("content", content);
            shortMessageSendProducerService.sendMessage(shortMessageSendDestination, josnObject.toString());
            return 0;
        }
        int result = 0;
        String msmSendStatus = "";
        if (mobiles == null || content == null) {
            msmSendStatus = "失败";
           
            result = -1;
        }
        double balance = shortMessageServer.getBalance(softwareSerialNo, key);
        if (balance <= 0) {
            logger.info("发送失败！返回标志：" + -1 + "，余额：" + balance);           
            msmSendStatus = "失败";
            
            result = -1;
        }
        if(result != -1){
            result =0;//= shortMessageServer.sendShortMessage(mobiles, content, "", 5, softwareSerialNo, key);//注释掉短信发送
        }
        
        if (result == 0) {
            msmSendStatus = "成功";
            logger.info("本短信第1次发送成功！");
            
        }else{
            msmSendStatus = "失败";    
          
        }
        MessageSendMarkInfoEntity_HI messageSendMarkInfoEntity_HI = new MessageSendMarkInfoEntity_HI();
        messageSendMarkInfoEntity_HI.setMsmReceiverAccount(JSONArray.toJSONString(mobiles));
        messageSendMarkInfoEntity_HI.setMsmMessageTitle(subject);
        messageSendMarkInfoEntity_HI.setMsmMessageContent(content);
        messageSendMarkInfoEntity_HI.setMsmSendDateTime(new Date());
        messageSendMarkInfoEntity_HI.setMsmSendStatus(msmSendStatus);
        messageSendMarkInfoEntity_HI.setMsmSendWayCode("shortMessage");
        messageSendMarkInfoEntity_HI.setMsmSendWayName("亿美短信");
        try {
            messageSendMarkInfoServer.saveMessageSendMarkInfoInfo(messageSendMarkInfoEntity_HI);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            logger.error("短信发送状态是" + result + ", 发送痕迹保存失败", e);
        }
        return result;
    }
    
	

    public static void main(String[] args) throws RemoteException {
        ShortMessageSender shortMessageSender = (ShortMessageSender)SaafToolUtils.context.getBean("shortMessageSender");
//        Map<String, String> info = shortMessageSender.shortMessageServer.queryServerInfo();
//        String softwareSerialNo = info.get(ShortMessageServer.SOFTWARE_SERIAL_NO);
//        String key = info.get(ShortMessageServer.KEY);
//        List<StatusReport> list = shortMessageSender.shortMessageServer.getReport(softwareSerialNo, key);
//        LOGGER.info(list.size());
//        if(list.size() > 100){
//            LOGGER.info(JSONArray.toJSONString(list.subList(0, 100)));    
//        }else{
//            LOGGER.info(JSONArray.toJSONString(list));    
//        }
        if (true) {
            String[] mobiles = { "13422008950" };
            String subect="测试";
            String content = "尊敬的【林先生】:您好！您的账号已经注册成功，欢迎加入金逸影城！测试测试2017年9月3日22:14:35";
            try {
               shortMessageSender.smsSendSingleMsg(mobiles,subect, content, false);
            } catch (Exception e) {
                logger.error("sendSms", e);
            }finally {
				System.out.println("执行了");
			}
        }
    }
    
//    public void messageEnterQueue(Destination queueDestination, ProducerService producer, String empStr) {
//        producer.sendMessage(queueDestination, empStr);
//    }
}