package saaf.common.fmw.message.services;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.net.MalformedURLException;

import java.rmi.RemoteException;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import javax.xml.rpc.ServiceException;

import org.codehaus.jettison.json.JSONException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import saaf.common.fmw.message.model.entities.NewsMessageBean;
import saaf.common.fmw.message.model.entities.TextMessageBean;
import saaf.common.fmw.message.model.inter.server.EmailSender;
import saaf.common.fmw.message.model.inter.server.ShortMessageSender;
import saaf.common.fmw.message.model.inter.server.WeiXinSender;


@Component("sendMessageService")
@Path("sendmessage")
public class SendMessageService {
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private ShortMessageSender shortMessageSender;
    @Autowired
    private WeiXinSender weiXinSender;
    @Context
    private HttpServletRequest request;
    @Context
    private HttpServletResponse response;
    private static final Logger LOGGER = LoggerFactory.getLogger(SendMessageService.class);

    public SendMessageService() {
        super();
    }

    public static String getEncoding(String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) { // 判断是不是GB2312
                String s = encode;
                return s; // 是的话，返回GB2312，以下代码同理
            }
        } catch (Exception e) {
            LOGGER.error("getEncoding异常---GB2312", e);
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) { // 判断是不是ISO-8859-1
                String s1 = encode;
                return s1;
            }
        } catch (Exception e) {
            LOGGER.error("getEncoding异常---ISO-8859-1", e);
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) { // 判断是不是UTF-8编码
                String s2 = encode;
                return s2;
            }
        } catch (Exception e) {
            LOGGER.error("getEncoding异常---UTF-8", e);
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) { // 判断是不是GBK
                String s3 = encode;
                return s3;
            }
        } catch (Exception e) {
            LOGGER.error("getEncoding异常---GBK", e);
        }
        return ""; // 到这一步，你就应该检查是不是其他编码啦
    }

    //中文字符处理

    public String charSetConvert(String xmlRequest) {
        String charSet = getEncoding(xmlRequest);
        try {
            byte[] b = xmlRequest.getBytes(charSet);
            xmlRequest = new String(b, "UTF-8");
        } catch (Exception e) {
            LOGGER.error("输入的内容不属于常见的编码格式,请再仔细核实", e);
        }
        return xmlRequest;

    }

    @Path("sendemail")
    @POST
    public String sendEmailService(String params) throws JSONException, UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=UTF-8");
        //        String charSet = NeopServerImpl.getEncoding(xmlRequest);
        //        try {
        //            byte[] b = xmlRequest.getBytes(charSet);
        //            xmlRequest = new String(b, "UTF-8");
        //        } catch (Exception e) {
        //            logger.error("输入的内容不属于常见的编码格式,请再仔细核实", e);
        //        }
        //org.codehaus.jettison.json.JSONObject json = new org.codehaus.jettison.json.JSONObject();
        //        json.put("SUBJECT", "测试 subject");
        //        json.put("CONTENT", "我邮件的内容，just do test content");
        //        org.codehaus.jettison.json.JSONArraparamsy jsonArrayTo = new org.codehaus.jettison.json.JSONArray();
        //        jsonArrayTo.put("yuanhaigang@chinasie.com");
        //        //jsonArrayTo.put("819038939@qq.com");
        //        json.put("TO_LIST", jsonArrayTo);
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
        //        LOGGER.info(params);
        //        params = charSetConvert(params);
        //        LOGGER.info("====" + params);
        JSONObject params_ = JSONObject.parseObject(params);
        Boolean useMQSendFlag = params_.getBoolean("useMQSendFalg");
        params_.remove("useMQSendFalg");
        String sendResult = emailSender.sendEmail(params_.toString(), useMQSendFlag);
        return sendResult;
    }

    @Path("sendShorMessage")
    @POST
    public String sendShortMessage(String params) throws MalformedURLException, RemoteException, ServiceException {
        //        LOGGER.info(params);
        //        params = charSetConvert(params);
        //        LOGGER.info("====" + params);
        JSONObject resultJSON = new JSONObject();
        JSONObject params_ = JSONObject.parseObject(params);
        JSONArray mobiles_ = params_.getJSONArray("mobiles");
        String[] mobiles = mobiles_.toArray(new String[] { });
        String content = params_.getString("content");
        Boolean useMQSendFalg = params_.getBoolean("useMQSendFalg");
        int result = shortMessageSender.smsSendSingleMsg(mobiles,"", content, useMQSendFalg);
        LOGGER.info(result + "");
        resultJSON.put("result", result);
        return resultJSON.toString();
    }

    @Path("sendWeiXinMessage")
    @POST
    public String sendWeiXinMessage(String params) throws NoSuchAlgorithmException, KeyManagementException, IOException, Exception {
        //        LOGGER.info(params);
        //        params = charSetConvert(params);
        //        LOGGER.info("====" + params);
        JSONObject params_ = JSONObject.parseObject(params);
        Boolean useMQSendFalg = params_.getBoolean("useMQSendFalg");
        if (params_.containsKey("newsMessage")) {
            JSONObject newsMessage_ = params_.getJSONObject("newsMessage");
            NewsMessageBean newsMessage = JSONObject.parseObject(newsMessage_.toString(), NewsMessageBean.class);
            return weiXinSender.sendNewsMessage(newsMessage, useMQSendFalg);
        } else if (params_.containsKey("newsMessage")) {
            JSONObject textMessage_ = params_.getJSONObject("textMessage");
            TextMessageBean textMessage = JSONObject.parseObject(textMessage_.toString(), TextMessageBean.class);
            return weiXinSender.sendTextMessage2OpenUser(textMessage);
        }
        return "";
    }
}
