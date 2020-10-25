package saaf.common.fmw.message.model.inter.server;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import saaf.common.fmw.message.model.entities.AccessTokenInfoBean;
import saaf.common.fmw.message.model.entities.ArticleBean;
import saaf.common.fmw.message.model.entities.ConcernUserCounts;
import saaf.common.fmw.message.model.entities.ContentMessage;
import saaf.common.fmw.message.model.entities.MessageSendMarkInfoEntity_HI;
import saaf.common.fmw.message.model.entities.MessageServerInfoEntity_HI;
import saaf.common.fmw.message.model.entities.NewsChildMessageBean;
import saaf.common.fmw.message.model.entities.NewsMessageBean;
import saaf.common.fmw.message.model.entities.TagBeanInfo;
import saaf.common.fmw.message.model.entities.TextMessageBean;
import saaf.common.fmw.message.model.entities.UserDetailInfoBean;
import saaf.common.fmw.message.model.entities.UserOpenIdDataInfo;
import saaf.common.fmw.message.model.entities.UserOpenIdInfoBean;
import saaf.common.fmw.message.model.entities.UserRemarkInfo;
import saaf.common.fmw.message.model.entities.WxMemberInfoEntity_HI;
import saaf.common.fmw.message.utils.SaafToolUtils;
import saaf.common.fmw.message.weixin.server.utils.HttpClientUtil;

import com.yhg.activemq.framework.queue.ProducerServiceImpl;
//import com.yhg.activemq.framework.queue.ProducerServiceImpl;
import com.yhg.base.utils.SToolUtils;

import java.io.IOException;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


@Component("weiXinSender")
public class WeiXinSender {
    //    private String appId = null;//"wx9610d16958bcd356";
    //    private String appSecret = null;//"ab1733dd38286c51183dc3b045860850";
    private static final String APP_ID_STR = "app_id";
    private static final String APP_SECRET_STR = "app_secret";
    public static final String CHARSET = "utf-8";
    //获取access_token的请求地址
    public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    //获取微信ip信息的请求地址
    public static final String WX_IP_INFO_URL = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=ACCESS_TOKEN";
    //创建微信公众号菜单的请求地址
    public static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
    //获取用户OpenId的请求地址
    public static final String GET_USER_OPENID_URL = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN";
    //获取用户详细信息的请求地址
    public static final String GET_USR_INFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    //统计关注用户的数量
    public static final String COUNT_USER = "https://api.weixin.qq.com/datacube/getusercumulate?access_token=ACCESS_TOKEN";
    //发送消息给关注的用户
    public static final String SEND_MESSAGE = "https://api.weixin.qq.com/cgi-bin/message/custom/send?&body=0&access_token=ACCESS_TOKEN";
    //创建标签
    public static final String CREATE_TAG = "https://api.weixin.qq.com/cgi-bin/tags/create?access_token=ACCESS_TOKEN";
    //用户备注信息
    public static final String USER_REMARK = "https://api.weixin.qq.com/cgi-bin/user/info/updateremark?access_token=ACCESS_TOKEN";

    private static Map<String, String> serverDetailInfoInfo;

    @Autowired
    private MessageServerDetailInfoServer messageServerDetailInfoServer;
    @Autowired
    private MessageServerInfoServer messageServerInfoServer;
    @Autowired
    private WxMemberInfoServer wxMemberInfoServer;
    @Autowired
    private MessageSendMarkInfoServer messageSendMarkInfoServer;
    @Autowired
    private org.apache.activemq.command.ActiveMQQueue weiXinMessageDestination;
    @Autowired
    private ProducerServiceImpl weiXinMessageSendProducerService;
    public WeiXinSender() {

    }

    //    /**
    //     * 微信公共账号发送给账号
    //     * @param content 文本内容
    //     * @param toUser 微信用户
    //     * @return
    //     */
    //
    //    public void sendTextMessageToUser(String content, String toUser) throws Exception {
    //        String json = "{\"touser\": \"" + toUser + "\",\"msgtype\": \"text\", \"text\": {\"content\": \"" + content + "\"}}";
    //        //获取access_token
    //        //GetExistAccessToken getExistAccessToken = GetExistAccessToken.getInstance();
    //        //String accessToken = getExistAccessToken.getExistAccessToken();
    //
    //        //获取请求路径
    //        //String action = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+accessToken;
    //        LOGGER.info("json:" + json);
    //        String requestURL = replaceChar(SEND_MESSAGE);
    //        try {
    ////            connectWeiXinInterface(requestURL, json);
    //            HttpClientUtil.doPost(requestURL, json, CHARSET);
    //        } catch (Exception e) {
    //            LOGGER.error(e.getMessage(), e);
    //        }
    //    }

    //    /**
    //     * 微信公共账号发送给账号(本方法限制使用的消息类型是语音或者图片)
    //     * @param mediaId 图片或者语音内容
    //     * @param toUser 微信用户
    //     * @param messageType 消息类型
    //     * @return
    //     */
    //
    //    public void sendPicOrVoiceMessageToUser(String mediaId, String toUser, String msgType) {
    //        String json = null;
    //        if (msgType.equals("image")) {
    //            json = "{\"touser\": \"" + toUser + "\",\"msgtype\": \"image\", \"image\": {\"media_id\": \"" + mediaId + "\"}}";
    //        } else if (msgType.equals("voice")) {
    //            json = "{\"touser\": \"" + toUser + "\",\"msgtype\": \"voice\", \"voice\": {\"media_id\": \"" + mediaId + "\"}}";
    //        }
    //        //获取access_token
    //        //          GetExistAccessToken getExistAccessToken = GetExistAccessToken.getInstance();
    //        //          String accessToken = getExistAccessToken.getExistAccessToken();
    //        //获取请求路径
    //        //          String action = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+accessToken;
    //        try {
    //            String requestURL = replaceChar(SEND_MESSAGE);
    //            //connectWeiXinInterface(requestURL, json);
    //            String result = new String(HttpClientUtil.doPost(requestURL, json, CHARSET));
    //            LOGGER.info(result);
    //        } catch (Exception e) {
    //            LOGGER.error(e.getMessage(), e);
    //        }
    //    }

    /**
     *  发送图文给所有的用户
     * @param openId 用户的id
     */
    //    public void sendNewsToUser(String openId) {
    //        MediaUtil mediaUtil = MediaUtil.getInstance();
    //        ArrayList<Object> articles = new ArrayList<Object>();
    //        Article a = new Article();
    //        articles.add(a);
    //        String str = JsonUtil.getJsonStrFromList(articles);
    //        String json = "{\"touser\":\"" + openId + "\",\"msgtype\":\"news\",\"news\":" + "{\"articles\":" + str + "}" + "}";
    //        json = json.replace("picUrl", "picurl");
    //        LOGGER.info(json);
    //        //获取access_token
    //        String access_token = mediaUtil.getAccess_token();
    //        //获取请求路径
    //        String action = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + access_token;
    //        try {
    //            connectWeiXinInterface(action, json);
    //        } catch (Exception e) {
    //            LOGGER.error(e.getMessage(), e);
    //        }
    //    }

    //    /**
    //     * 连接请求微信后台接口
    //     * @param action 接口url
    //     * @param json  请求接口传送的json字符串
    //     */
    //    public void connectWeiXinInterface(String action, String json) {
    //        URL url;
    //        try {
    //            url = new URL(action);
    //            HttpURLConnection http = (HttpURLConnection)url.openConnection();
    //            http.setRequestMethod("POST");
    //            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    //            http.setDoOutput(true);
    //            http.setDoInput(true);
    //            System.setProperty("sun.net.client.defaultConnectTimeout", "30000"); // 连接超时30秒
    //            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
    //            http.connect();
    //            OutputStream os = http.getOutputStream();
    //            os.write(json.getBytes("UTF-8")); // 传入参数
    //            InputStream is = http.getInputStream();
    //            int size = is.available();
    //            byte[] jsonBytes = new byte[size];
    //            is.read(jsonBytes);
    //            String result = new String(jsonBytes, "UTF-8");
    //            LOGGER.info("请求返回结果:" + result);
    //            os.flush();
    //            os.close();
    //        } catch (Exception e) {
    //            LOGGER.error(e.getMessage(), e);
    //        }
    //    }
    
    public String sendNewsMessage(NewsMessageBean newsMessage) throws NoSuchAlgorithmException, KeyManagementException, IOException, Exception {
        return sendNewsMessage(newsMessage, false);
    }

    /**
     * 发送图文消息
     * @param newsMessage
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @throws IOException
     * @throws Exception
     */
    public String sendNewsMessage(NewsMessageBean newsMessage, boolean useMQSendFalg) throws NoSuchAlgorithmException, KeyManagementException, IOException, Exception {
        if(useMQSendFalg){
            weiXinMessageSendProducerService.sendMessage(weiXinMessageDestination, JSONObject.toJSONString(newsMessage)); 
            return "信息已经推送到消息队列中";
        }
        String requestURL = replaceChar(SEND_MESSAGE);
        String result = new String(HttpClientUtil.doPost(requestURL, JSONObject.toJSONString(newsMessage), CHARSET));
        MessageSendMarkInfoEntity_HI messageSendMarkInfoEntity_HI = new MessageSendMarkInfoEntity_HI();
        messageSendMarkInfoEntity_HI.setMsmMessageContent(JSONObject.toJSONString(newsMessage));
        messageSendMarkInfoEntity_HI.setMsmReceiverAccount(newsMessage.getTouser());
        messageSendMarkInfoEntity_HI.setMsmSendDateTime(new Date());
        messageSendMarkInfoEntity_HI.setMsmSendWayCode("myWX");
        messageSendMarkInfoEntity_HI.setMsmSendWayName("我的测试公众号");
        messageSendMarkInfoServer.saveMessageSendMarkInfoInfo(messageSendMarkInfoEntity_HI);
        
        return result;
    }

    /**
     * 替换所有的固定的字符串
     * @param requestURL
     * @return
     * @throws Exception
     */
    private String replaceChar(String requestURL) throws Exception {
        if (null == serverDetailInfoInfo) {
            queryServerDetailInfo();
        }
        String appId = serverDetailInfoInfo.get(APP_ID_STR);
        String appSecret = serverDetailInfoInfo.get(APP_SECRET_STR);
        if (null != requestURL) {
            requestURL = requestURL.replaceAll("APPID", appId).replaceAll("APPSECRET", appSecret);
            if (requestURL.indexOf("ACCESS_TOKEN") >= 0) {
                requestURL = requestURL.replaceAll("ACCESS_TOKEN", queryTokenInfo_());
            }
            return requestURL;
        } else {
            throw new Exception("您传入的请求地址为空，请传入正确的请求地址");
        }
    }

    /**
     * 获取微信的TOKEN信息
     * 1.从数据库中查找tokenId，如果存在且没有过期，直接拿出来用
     * 2.如果数据库中不存在token或者token过期重新获取新的token并保存起来
     * @return
     * @throws Exception
     */
    private String queryTokenInfo_() throws Exception {
        if (null == serverDetailInfoInfo) {
            queryServerDetailInfo();
        }
        String appId = serverDetailInfoInfo.get(APP_ID_STR);
        String accessToken = "";
        JSONObject queryParamJSON = new JSONObject();
        queryParamJSON.put("msWxAppId", appId);
        MessageServerInfoEntity_HI messageServerInfoEntity_HI = messageServerInfoServer.findMessageInfoInfo(queryParamJSON);
        if (null == messageServerInfoEntity_HI) {
            messageServerInfoEntity_HI = new MessageServerInfoEntity_HI();
            AccessTokenInfoBean accessTokenInfo = queryAccessTokenInfo(messageServerInfoEntity_HI);
            accessToken = accessTokenInfo.getAccess_token();
        } else {
            if (null == messageServerInfoEntity_HI.getMsTokenStartTime()) {
                return queryAccessTokenInfo(messageServerInfoEntity_HI).getAccess_token();
            }
            Long f = messageServerInfoEntity_HI.getMsTokenStartTime();
            //Long startTime = Long.parseLong(String.valueOf(f));
            Long endTime = f.longValue() + 110 * 60 * 1000;
            if (endTime <= System.currentTimeMillis()) {
                //调用API重新获取Token
                AccessTokenInfoBean accessTokenInfo = queryAccessTokenInfo(messageServerInfoEntity_HI);
                accessToken = accessTokenInfo.getAccess_token();
            } else {
                accessToken = messageServerInfoEntity_HI.getMsTokenCode();
            }
        }
        return accessToken;
    }

    /**
     * 获取微信中的accessToken信息
     * @return
     * @throws Exception
     */
    public synchronized AccessTokenInfoBean queryAccessTokenInfo(MessageServerInfoEntity_HI entity) throws Exception {
        if (null == serverDetailInfoInfo) {
            queryServerDetailInfo();
        }
        String appId = serverDetailInfoInfo.get(APP_ID_STR);
        AccessTokenInfoBean accessTokenBean = null;
        JSONObject queryParamJSON = new JSONObject();
        queryParamJSON.put("msWxAppId", appId);
        MessageServerInfoEntity_HI messageServerInfoEntity_HI = messageServerInfoServer.findMessageInfoInfo(queryParamJSON);
        if (null != messageServerInfoEntity_HI && null != messageServerInfoEntity_HI.getMsTokenStartTime()) {
            Long f = messageServerInfoEntity_HI.getMsTokenStartTime();
            Long endTime = f + 110 * 60 * 1000;
            if (endTime > System.currentTimeMillis()) {
                accessTokenBean = new AccessTokenInfoBean();
                accessTokenBean.setAccess_token(messageServerInfoEntity_HI.getMsTokenCode());
                Long startTime = (messageServerInfoEntity_HI.getMsTokenStartTime() / 1000);
                accessTokenBean.setExpires_in(startTime.intValue());
                return accessTokenBean;
            }
        }

        String requestURL = replaceChar(ACCESS_TOKEN_URL);
        String wxTokenInfo = HttpClientUtil.doGet(requestURL, CHARSET);
        accessTokenBean = JSONObject.parseObject(wxTokenInfo, AccessTokenInfoBean.class);
        if (null == entity) {
            entity = new MessageServerInfoEntity_HI();
            entity.setMsWxAppId(appId);
        }
        entity.setMsTokenCode(accessTokenBean.getAccess_token());
        Long startTime = System.currentTimeMillis();
        entity.setMsTokenStartTime(startTime);
        messageServerInfoServer.saveMessageInfo(JSONObject.parseObject(JSONObject.toJSONString(entity)));
        return accessTokenBean;
    }

    /**
     * 获取微信中的IP地址信息
     * @return
     * @throws Exception
     */
    public List<String> queryWeiXinIpInfo() throws Exception {
        List<String> ipInfos = new ArrayList<String>();
        String requestURL = replaceChar(WX_IP_INFO_URL);
        String wxIPInfo = HttpClientUtil.doGet(requestURL, CHARSET);
        JSONObject jSONObject = JSONObject.parseObject(wxIPInfo);
        JSONArray array = jSONObject.getJSONArray("ip_list");
        Iterator<Object> iterator = array.iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            //LOGGER.info(next);
            ipInfos.add(String.valueOf(next));
        }
        return ipInfos;
    }

    /**
     * 查询关注公众好的用户基本信息
     * @return
     * @throws Exception
     */
    public UserOpenIdInfoBean queryAllUserOpenIdInfo() throws Exception {
        String requestURL = replaceChar(GET_USER_OPENID_URL);
        String userOpenId = HttpClientUtil.doGet(requestURL, CHARSET);
        UserOpenIdInfoBean userOpenIdInfo = JSONObject.parseObject(userOpenId, UserOpenIdInfoBean.class);
        return userOpenIdInfo;
    }

    /**
     * 查询所有关注公众号的用户的详细信息
     * @return
     * @throws Exception
     */
    public List<WxMemberInfoEntity_HI> queryAllUserDetailInfo() throws Exception {
        if (null == serverDetailInfoInfo) {
            queryServerDetailInfo();
        }
        String appId = serverDetailInfoInfo.get(APP_ID_STR);
        String appSecret = serverDetailInfoInfo.get(APP_SECRET_STR);

        List<WxMemberInfoEntity_HI> userOpenIdInfos = new ArrayList<WxMemberInfoEntity_HI>();
        UserOpenIdInfoBean userOpenIdInfoBean = queryAllUserOpenIdInfo();
        UserOpenIdDataInfo data = userOpenIdInfoBean.getData();
        List<String> list = data.getOpenid();
        JSONObject queryParamJSON = new JSONObject();
        //queryParamJSON.put("openId", userOpenId);
        Set<String> entitis = wxMemberInfoServer.findWxMemberInfoInfo(queryParamJSON);
        String requestURL = replaceChar(GET_USR_INFO_URL);
        for (int i = 0; i < list.size(); i++) {
            String userOpenId = list.get(i);
            String requestURL_ = requestURL.replaceAll("OPENID", userOpenId);
            if (entitis.contains(userOpenId)) {
                continue;
            }
            String userInfo = HttpClientUtil.doGet(requestURL_, CHARSET);
            WxMemberInfoEntity_HI userOpenIdInfo = JSONObject.parseObject(userInfo, WxMemberInfoEntity_HI.class);
            userOpenIdInfo.setSubscribeTime(new Date(userOpenIdInfo.getSubscribe_time()));
            List<String> tagList = userOpenIdInfo.getTagid_list();
            StringBuffer tagStr = new StringBuffer();
            for (int k = 0; k < tagList.size(); k++) {
                tagStr.append(tagList.get(k) + "#");
            }
            if (tagList.size() > 0) {
                String tagStr_ = tagStr.toString();
                tagStr_ = tagStr_.substring(0, tagStr_.length() - 1);
                userOpenIdInfo.setTagidList(tagStr_);
            }
            wxMemberInfoServer.saveWxMemberInfoInfo(JSONObject.parseObject(JSONObject.toJSONString(userOpenIdInfo)));
            userOpenIdInfos.add(userOpenIdInfo);
        }
        return userOpenIdInfos;
    }

    /**
     * 查询指定openId的用户的详细信息
     * @param userOpenId
     * @return
     * @throws Exception
     */
    public UserDetailInfoBean queryOneUserDetailInfoByOpenId(String userOpenId) throws Exception {
        String requestURL = replaceChar(GET_USR_INFO_URL);
        requestURL = requestURL.replaceAll("OPENID", userOpenId);
        String userInfo = HttpClientUtil.doGet(requestURL, CHARSET);
        UserDetailInfoBean userDetailInfo = JSONObject.parseObject(userInfo, UserDetailInfoBean.class);
        //LOGGER.info(userInfo);
        return userDetailInfo;
    }

    /**
     * 发送消息给指定的用户
     * @param message
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @throws IOException
     * @throws Exception
     */
    public String sendTextMessage2OpenUser(TextMessageBean message) throws NoSuchAlgorithmException, KeyManagementException, IOException, Exception {
        String requestURL = replaceChar(SEND_MESSAGE);
        String result = new String(HttpClientUtil.doPost(requestURL, JSONObject.toJSONString(message), WeiXinSender.CHARSET));
        return result;
    }

    /**
     * 统计现在关注公众号的用户
     * @param json
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @throws IOException
     * @throws Exception
     */
    public ConcernUserCounts countUser(JSONObject json) throws NoSuchAlgorithmException, KeyManagementException, IOException, Exception {
        String requestURL = replaceChar(COUNT_USER); //.replaceAll("ACCESS_TOKEN", ACCESS_TOKEN);
        ConcernUserCounts concernUserCount = null;
        json.put(BEGIN_DATE_STR, "2017-05-04");
        if (json.containsKey(BEGIN_DATE_STR)) {
            String beginDateStr = json.getString(BEGIN_DATE_STR);
            Date endDate = SToolUtils.addDate(6, Calendar.DATE, SToolUtils.string2DateTime(beginDateStr, "yyyy-MM-dd"));
            String endDateStr = SToolUtils.date2String(endDate, "yyyy-MM-dd");
            json.put(END_DATE_STR, endDateStr);
            String concernUserJSON = new String(HttpClientUtil.doPost(requestURL, json.toString(), CHARSET));
            concernUserCount = JSONObject.parseObject(concernUserJSON, ConcernUserCounts.class);
        }
        return concernUserCount;
    }

    /**
     * 创建小程序菜单
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @throws IOException
     */
    public void createMenue() throws NoSuchAlgorithmException, KeyManagementException, IOException, Exception {
        String requestURL = CREATE_MENU_URL.replaceAll("ACCESS_TOKEN", queryTokenInfo_());
        String menuJSONStr = "{\n" +
            "     \"button\":[\n" +
            "     {       \n" +
            "          \"type\":\"click\",\n" +
            "          \"name\":\"今日歌曲\",\n" +
            "          \"key\":\"V1001_TODAY_MUSIC\"\n" +
            "      },\n" +
            "      {\n" +
            "           \"name\":\"菜单\",\n" +
            "           \"sub_button\":[\n" +
            "           { \n" +
            "               \"type\":\"view\",\n" +
            "               \"name\":\"搜索\",\n" +
            "               \"url\":\"http://www.soso.com/\"\n" +
            "            },\n" +
            "            {\n" +
            "                 \"type\":\"miniprogram\",\n" +
            "                 \"name\":\"wxa\",\n" +
            "                 \"url\":\"http://mp.weixin.qq.com\",\n" +
            "                 \"appid\":\"wx286b93c14bbf93aa\",\n" +
            "                 \"pagepath\":\"pages/lunar/index.html\"\n" +
            "             },\n" +
            "            {\n" +
            "               \"type\":\"click\",\n" +
            "               \"name\":\"赞一下我们\",\n" +
            "               \"key\":\"V1001_GOOD\"\n" +
            "            }]\n" +
            "       }]\n" +
            " }";
        String doPost = new String(HttpClientUtil.doPost(requestURL, menuJSONStr, CHARSET));
        //LOGGER.info(doPost);
    }

    public String createTag(TagBeanInfo tagBeanInfo) throws NoSuchAlgorithmException, KeyManagementException, IOException, Exception {
        String requestURL = replaceChar(CREATE_TAG);
        JSONObject json = new JSONObject();
        json.put("tag", JSONObject.parseObject(tagBeanInfo.toString()));
        String resultValue = new String(HttpClientUtil.doPost(requestURL, json.toString(), CHARSET));
        return resultValue;
    }

    public String remarkUser(UserRemarkInfo userRemarkInfo) throws NoSuchAlgorithmException, KeyManagementException, IOException, Exception {
        String requestURL = replaceChar(USER_REMARK);
        String resultValue = new String(HttpClientUtil.doPost(requestURL, JSONObject.toJSONString(userRemarkInfo), CHARSET));
     //   LOGGER.info(resultValue);
        return resultValue;
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext content = SaafToolUtils.context;
        //LOGGER.info(new Date(1493977462000L));
        //LOGGER.info(SToolUtils.date2String(new Date(1495184287000L), "yyyy-MM-dd HH:mm:ss"));
        WeiXinSender accessToken = (WeiXinSender)content.getBean("weiXinSender");

        //new GetAccessToken();
        //accessToken.sendTextMessageToUser("gavin....", "oWyzd1Y1gjNCvX55fmbnYsmfzwbk");
        //accessToken.sendPicOrVoiceMessageToUser("mediaId", "oWyzd1Y1gjNCvX55fmbnYsmfzwbk", "image");
        //获取token
        //AccessTokenInfoBean accessTokenBean = accessToken.queryAccessTokenInfo();
        //LOGGER.info(accessTokenBean.getAccess_token());
        //accessToken.countUser(new JSONObject());

        //发送消息
        TextMessageBean message = new TextMessageBean();
        message.setTouser("oWyzd1Y1gjNCvX55fmbnYsmfzwbk");
        message.setCreateTime(System.currentTimeMillis());
        message.setMsgtype("text");
        message.setText(new ContentMessage("hello java ---"));
        accessToken.sendTextMessage2OpenUser(message);

//        NewsMessageBean newsMessage = new NewsMessageBean();
//        newsMessage.setMsgtype("news");
//        newsMessage.setTouser("oWyzd1Y1gjNCvX55fmbnYsmfzwbk");
//        NewsChildMessageBean news_ = new NewsChildMessageBean();
//
//        List<ArticleBean> articles = new ArrayList<ArticleBean>();
//        for (int i = 0; i < 1; i++) {
//            ArticleBean article = new ArticleBean();
//            article.setTitle("title" + i + 1);
//            article.setDescription("我想秒速");
//            article.setUrl("https://image.baidu.com/search/detail?ct=503316480&z=undefined&tn=baiduimagedetail&ipn=d&word=%7B\"errcode\"%3A85005%2C\"errmsg\"%3A\"appid%20not%20bind%20weapp%20hint%3A%20%5BSIV5VA0777vr19%5D\"%7D&step_word=&ie=utf-8&in=&cl=2&lm=-1&st=undefined&cs=4120507643,3179664321&os=2372104483,2841087290&simid=4254127042,762566717&pn=2&rn=1&di=197819360322&ln=1100&fr=&fmq=1495682551054_R&fm=&ic=undefined&s=undefined&se=&sme=&tab=0&width=undefined&height=undefined&face=undefined&is=0,0&istype=0&ist=&jit=&bdtype=15&spn=0&pi=0&gsm=0&objurl=http%3A%2F%2Fh.hiphotos.baidu.com%2Fzhidao%2Fwh%253D450%252C600%2Fsign%3Db47b2138f71fbe091c0bcb105e502005%2Feac4b74543a98226004c35698f82b9014b90ebeb.jpg&rpstart=0&rpnum=0&adpicid=0&ctd=1495682560776^3_1583X697%1");
//            article.setPicurl("https://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=宠物&step_word=&hs=0&pn=2&spn=0&di=70437016100&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&istype=2&ie=utf-8&oe=utf-8&in=&cl=2&lm=-1&st=-1&cs=3673633480%2C3539052624&os=993722498%2C1010233782&simid=4212927700%2C508965651&adpicid=0&lpn=0&ln=1979&fr=&fmq=1495682661219_R&fm=&ic=0&s=undefined&se=&sme=&tab=0&width=&height=&face=undefined&ist=&jit=&cg=&bdtype=0&oriquery=&objurl=http%3A%2F%2Fmvimg2.meitudata.com%2F5619e503938789482.jpg&fromurl=ippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3B4jtrwt_z%26e3Bv54AzdH3F4j1twAzdH3F9dd8lbn88&gsm=0&rpstart=0&rpnum=0");
//            articles.add(article);
//        }
//        news_.setArticles(articles);
//        newsMessage.setNews(news_);
//        accessToken.sendNewsMessage(newsMessage);
        
        //LOGGER.info(accessToken.sendNewsMessage(newsMessage));

        //获取用户的openId 用户详细信息
        //        List<WxMemberInfoEntity_HI> allUserDetailInfoByOpenId = accessToken.queryAllUserDetailInfo();
        //        LOGGER.info(JSONArray.toJSONString(allUserDetailInfoByOpenId));

        //        net.sf.json.JSONObject gjson = new net.sf.json.JSONObject();
        //        gjson.put("touser", "oWyzd1Y1gjNCvX55fmbnYsmfzwbk");
        //        gjson.put("msgtype", "news");
        //        net.sf.json.JSONObject news = new net.sf.json.JSONObject();
        //        net.sf.json.JSONArray articles = new net.sf.json.JSONArray();
        //        net.sf.json.JSONObject list = new net.sf.json.JSONObject();
        //        list.put("title", String.valueOf("title")); //标题
        //        list.put("description", String.valueOf("description")); //描述
        //        list.put("url", String.valueOf("http://file.jycinema.com:9680/group1/M00/00/72/wKgoaVkRZXmAej6gAABmtpDmHrk858.jpg")); //点击图文链接跳转的地址
        //        list.put("picurl", String.valueOf("http://file.jycinema.com:9680/group1/M00/00/72/wKgoaVkRZXmAej6gAABmtpDmHrk858.jpg")); //图文链接的图片
        //        articles.add(list);
        //        news.put("articles", articles);
        //        //        LOGGER.info(news);
        //        String requestURL = replaceChar(SEND_MESSAGE);
        //        HttpClientUtil.doPost(requestURL, JSONObject.toJSONString(news), GetAccessToken.CHARSET);

        //创建吧标签
        //        createTag(new TagBeanInfo("江西"));

        //备注用户
        //        remarkUser(new UserRemarkInfo("oWyzd1Y1gjNCvX55fmbnYsmfzwbk", "gavin"));


    }

    public static final String BEGIN_DATE_STR = "begin_date";
    public static final String END_DATE_STR = "end_date";

    private synchronized void queryServerDetailInfo() {
        if (null == serverDetailInfoInfo) {
            JSONObject queryParamJSON_ = new JSONObject();
            queryParamJSON_.put("msdWayCode", "myWX"); //TODO
            serverDetailInfoInfo = messageServerDetailInfoServer.findMessageDetailInfoInfo(queryParamJSON_);
        }
    }
}
