package saaf.common.fmw.message.model.inter.server;


import com.alibaba.fastjson.JSONObject;

import saaf.common.fmw.message.shortmessage.yimei.client.Mo;
import saaf.common.fmw.message.shortmessage.yimei.client.SDKServiceBindingStub;
import saaf.common.fmw.message.shortmessage.yimei.client.StatusReport;

import java.rmi.RemoteException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.xml.rpc.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component("shortMessageServer")
public class ShortMessageServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShortMessageServer.class);
    public static final String SOFTWARE_SERIAL_NO = "softwareSerialNo";
    public static final String KEY = "key";
    public static final String URL = "url";
    
    @Autowired
    private MessageServerDetailInfoServer messageServerDetailInfoServer;
    @Autowired
    private YiMeiSDKLocatorService yiMeiSDKLocatorService;

    public ShortMessageServer() {

    }

    public int chargeUp(String cardNo, String cardPass, String softwareSerialNo, String key) throws RemoteException {
        int value = -1;
        value = getBinding().chargeUp(softwareSerialNo, key, cardNo, cardPass);
        return value;
    }

    public double getBalance(String softwareSerialNo, String key) throws RemoteException {
        double value = 0.0;
        value = getBinding().getBalance(softwareSerialNo, key);
        return value;
    }

    public double getEachFee(String softwareSerialNo, String key) throws RemoteException {
        double value = 0.0;
        value = getBinding().getEachFee(softwareSerialNo, key);
        return value;
    }

    public List<Mo> getMO(String softwareSerialNo, String key) throws RemoteException {
        Mo[] mo = getBinding().getMO(softwareSerialNo, key);
        if (null == mo) {
            return null;
        } else {
            List<Mo> molist = Arrays.asList(mo);
            return molist;
        }
    }

    public List<StatusReport> getReport(String softwareSerialNo, String key) throws RemoteException {
        StatusReport[] sr = getBinding().getReport(softwareSerialNo, key);
        if (null != sr) {
            return Arrays.asList(sr);
        } else {
            return null;
        }
    }

    public int logout(String softwareSerialNo, String key) throws RemoteException {
        int value = -1;
        value = getBinding().logout(softwareSerialNo, key);
        return value;
    }

    public int registDetailInfo(String eName, String linkMan, String phoneNum, String mobile, String email, String fax, String address, String postcode, String softwareSerialNo, String key) throws RemoteException {
        int value = -1;
        value = getBinding().registDetailInfo(softwareSerialNo, key, eName, linkMan, phoneNum, mobile, email, fax, address, postcode);
        return value;
    }

    public int registEx(String password, String softwareSerialNo, String key) throws RemoteException {
        int value = -1;
        value = getBinding().registEx(softwareSerialNo, key, password);
        return value;
    }

    public int sendShortMessage(String[] mobiles, String smsContent, String addSerial, int smsPriority, String softwareSerialNo, String key) throws RemoteException {
        int value = -1;
        value = getBinding().sendSMS(softwareSerialNo, key, "", mobiles, smsContent, addSerial, "gbk", smsPriority, 0);
        return value;
    }

    public int sendScheduledSMSEx(String[] mobiles, String smsContent, String sendTime, String srcCharset, String softwareSerialNo, String key) throws RemoteException {
        int value = -1;
        value = getBinding().sendSMS(softwareSerialNo, key, sendTime, mobiles, smsContent, "", srcCharset, 3, 0);
        return value;
    }

    public int sendSMSEx(String[] mobiles, String smsContent, String addSerial, String srcCharset, int smsPriority, long smsID, String softwareSerialNo, String key) throws RemoteException {
        int value = -1;
        value = getBinding().sendSMS(softwareSerialNo, key, "", mobiles, smsContent, addSerial, srcCharset, smsPriority, smsID);
        return value;
    }

    public String sendVoice(String[] mobiles, String smsContent, String addSerial, String srcCharset, int smsPriority, long smsID, String softwareSerialNo, String key) throws RemoteException {
        String value = null;
        value = getBinding().sendVoice(softwareSerialNo, key, "", mobiles, smsContent, addSerial, srcCharset, smsPriority, smsID);
        return value;
    }

    public int serialPwdUpd(String serialPwd, String serialPwdNew, String softwareSerialNo, String key) throws RemoteException {
        int value = -1;
        value = getBinding().serialPwdUpd(softwareSerialNo, key, serialPwd, serialPwdNew);
        return value;
    }

    public Map<String, String> queryServerInfo() {
        JSONObject queryParamJSON = new JSONObject();
        queryParamJSON.put("msdWayCode", "shortMessage");
        Map<String, String> detailInfoInfo = messageServerDetailInfoServer.findMessageDetailInfoInfo(queryParamJSON);
        return detailInfoInfo;
    }

    public SDKServiceBindingStub getBinding() {
        SDKServiceBindingStub binding = null;
        try {
            binding = (SDKServiceBindingStub)yiMeiSDKLocatorService.getSDKService();
        } catch (ServiceException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return binding;
    }
}
