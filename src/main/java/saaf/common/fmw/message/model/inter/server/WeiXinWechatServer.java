package saaf.common.fmw.message.model.inter.server;

import saaf.common.fmw.message.model.entities.ArticleBean;
import saaf.common.fmw.message.model.entities.NewsChildMessageBean;
import saaf.common.fmw.message.model.entities.NewsMessageBean;
import saaf.common.fmw.message.model.entities.TextMessageBean;
import saaf.common.fmw.message.weixin.server.utils.HttpClientUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 核心服务类
 *
 * @author liufeng
 * @date 2013-07-25
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeiXinWechatServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeiXinWechatServer.class);
    public static void main(String[] args) {
        WeiXinWechatServer servlet = new WeiXinWechatServer();
        servlet.testsendTextByOpenids();
    }

    //获取微信返回的access_token

    private String getAccess_token() {
        String access_token = "6fIMwq3pUTSPZtuePT1Tf0fZgchl_KeZz8kZlZf7xnAeR7-pzsbgynbRjFYTyNu72neNYbJqZLeB00DVv3E1ze9uxDL5PfnGU0BjatI-6_UZXHbACARPZ";
        return access_token;
        //        StringBuffer action = new StringBuffer();
        //        action.append("https://api.weixin.QQ.com/cgi-bin/token?grant_type=client_credential").append("&appid=wx9610d16958bcd356"). //设置服务号的appid
        //            append("&secret=ab1733dd38286c51183dc3b045860850"); //设置服务号的密匙
        //        URL url;
        //        try {
        //            url = new URL(action.toString());
        //            HttpURLConnection http = (HttpURLConnection)url.openConnection();
        //            http.setRequestMethod("GET");
        //            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        //            http.setDoInput(true);
        //            InputStream is = http.getInputStream();
        //            int size = is.available();
        //            byte[] buf = new byte[size];
        //            is.read(buf);
        //            String resp = new String(buf, "UTF-8");
        //            JSONObject json = JSONObject.fromObject(resp);
        //            Object object = json.get("access_token");
        //            if (object != null) {
        //                access_token = String.valueOf(object);
        //            }
        //            LOGGER.info("getAccess_token:" + access_token);
        //            return access_token;
        //        } catch (MalformedURLException e) {
        //            LOGGER.error(e.getMessage(), e);
        //            return access_token;
        //        } catch (IOException e) {
        //            LOGGER.error(e.getMessage(), e);
        //            return access_token;
        //
        //        }
    }
    //获取该服务号下的用户组

    public JSONArray getOpenids() {
        JSONArray array = null;
        String urlstr = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID";
        urlstr = urlstr.replace("ACCESS_TOKEN", getAccess_token());
        urlstr = urlstr.replace("NEXT_OPENID", "");
        return array;
        //        URL url;
        //        try {
        //            url = new URL(urlstr);
        //            HttpURLConnection http = (HttpURLConnection)url.openConnection();
        //            http.setRequestMethod("GET");
        //            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        //            http.setDoInput(true);
        //            InputStream is = http.getInputStream();
        //            int size = is.available();
        //            byte[] buf = new byte[size];
        //            is.read(buf);
        //            String resp = new String(buf, "UTF-8");
        //            JSONObject jsonObject = JSONObject.fromObject(resp);
        //            LOGGER.info("getOpenids:" + jsonObject.toString());
        //            array = jsonObject.getJSONObject("data").getJSONArray("openid");
        //            return array;
        //        } catch (MalformedURLException e) {
        //            LOGGER.error(e.getMessage(), e);
        //            return array;
        //
        //        } catch (IOException e) {
        //            LOGGER.error(e.getMessage(), e);
        //            return array;
        //        }
    }
    //根据用户组的openId获取用户详细数据

    public JSONObject getUserOpenids(String openId) {
        String urlstr = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
        urlstr = urlstr.replace("ACCESS_TOKEN", getAccess_token());
        urlstr = urlstr.replace("OPENID", openId);
        urlstr = urlstr.replace("NEXT_OPENID", "");
        URL url;
        try {
            url = new URL(urlstr);
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http.setDoInput(true);
            InputStream is = http.getInputStream();
            int size = is.available();
            byte[] buf = new byte[size];
            is.read(buf);
            String resp = new String(buf, "UTF-8");
            JSONObject jsonObject = JSONObject.fromObject(resp);
            LOGGER.info("getUserOpenids:" + jsonObject.toString());
            return jsonObject;
        } catch (MalformedURLException e) {
            LOGGER.error(e.getMessage(), e);
            return null;

        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }
    //主方法,只有服务号才能使用客服图文消息,实现点击图文链接,直接跳转到指定URL

    public void testsendTextByOpenids() {
        //String urlstr ="https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=ACCESS_TOKEN"; //群发图文消息
        String urlstr = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN"; //发送客服图文消息
        urlstr = urlstr.replace("ACCESS_TOKEN", getAccess_token());
        String reqjson = createGroupText(getOpenids());
        try {
            String result = new String(HttpClientUtil.doPost(urlstr, reqjson, "utf-8"));
            LOGGER.info(result);
        } catch (NoSuchAlgorithmException e) {
        } catch (KeyManagementException e) {
        } catch (IOException e) {
        }
        //        try {
        //            URL httpclient = new URL(urlstr);
        //            HttpURLConnection conn = (HttpURLConnection)httpclient.openConnection();
        //            conn.setConnectTimeout(5000);
        //            conn.setReadTimeout(2000);
        //            conn.setRequestMethod("POST");
        //            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        //            conn.setDoOutput(true);
        //            conn.setDoInput(true);
        //            conn.connect();
        //            OutputStream os = conn.getOutputStream();
        //            LOGGER.info("ccccc:" + reqjson);
        //            os.write(reqjson.getBytes("UTF-8")); //传入参数
        //            os.flush();
        //            os.close();
        //
        //            InputStream is = conn.getInputStream();
        //            int size = is.available();
        //            byte[] jsonBytes = new byte[size];
        //            is.read(jsonBytes);
        //            String message = new String(jsonBytes, "UTF-8");
        //            LOGGER.info("testsendTextByOpenids:" + message);
        //
        //        } catch (MalformedURLException e) {
        //            LOGGER.error(e.getMessage(), e);
        //        } catch (IOException e) {
        //            LOGGER.error(e.getMessage(), e);
        //        }

    }
    //创建发送的数据

    private String createGroupText(JSONArray array) {
        TextMessageBean messageBean = new TextMessageBean();
        messageBean.setTouser("oWyzd1Y1gjNCvX55fmbnYsmfzwbk");
        messageBean.setMsgtype("news");
        //        messageBean.setCreateTime(null);
        //        messageBean.setText(null);
        //        messageBean.setCreateTime(System.currentTimeMillis());
        NewsMessageBean newsMessage = new NewsMessageBean();
        newsMessage.setMsgtype("news");
        newsMessage.setTouser("oWyzd1Y1gjNCvX55fmbnYsmfzwbk");
        NewsChildMessageBean news_ = new NewsChildMessageBean();

        List<ArticleBean> articles = new ArrayList<ArticleBean>();
        for (int i = 0; i < 1; i++) {
            ArticleBean article = new ArticleBean();
            article.setTitle("title" + i + 1);
            article.setDescription("我想秒速");
            article.setUrl("https://image.baidu.com/search/detail?ct=503316480&z=undefined&tn=baiduimagedetail&ipn=d&word=%7B\"errcode\"%3A85005%2C\"errmsg\"%3A\"appid%20not%20bind%20weapp%20hint%3A%20%5BSIV5VA0777vr19%5D\"%7D&step_word=&ie=utf-8&in=&cl=2&lm=-1&st=undefined&cs=4120507643,3179664321&os=2372104483,2841087290&simid=4254127042,762566717&pn=2&rn=1&di=197819360322&ln=1100&fr=&fmq=1495682551054_R&fm=&ic=undefined&s=undefined&se=&sme=&tab=0&width=undefined&height=undefined&face=undefined&is=0,0&istype=0&ist=&jit=&bdtype=15&spn=0&pi=0&gsm=0&objurl=http%3A%2F%2Fh.hiphotos.baidu.com%2Fzhidao%2Fwh%253D450%252C600%2Fsign%3Db47b2138f71fbe091c0bcb105e502005%2Feac4b74543a98226004c35698f82b9014b90ebeb.jpg&rpstart=0&rpnum=0&adpicid=0&ctd=1495682560776^3_1583X697%1");
            article.setPicurl("https://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=宠物&step_word=&hs=0&pn=2&spn=0&di=70437016100&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&istype=2&ie=utf-8&oe=utf-8&in=&cl=2&lm=-1&st=-1&cs=3673633480%2C3539052624&os=993722498%2C1010233782&simid=4212927700%2C508965651&adpicid=0&lpn=0&ln=1979&fr=&fmq=1495682661219_R&fm=&ic=0&s=undefined&se=&sme=&tab=0&width=&height=&face=undefined&ist=&jit=&cg=&bdtype=0&oriquery=&objurl=http%3A%2F%2Fmvimg2.meitudata.com%2F5619e503938789482.jpg&fromurl=ippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3B4jtrwt_z%26e3Bv54AzdH3F4j1twAzdH3F9dd8lbn88&gsm=0&rpstart=0&rpnum=0");
            articles.add(article);
        }
        news_.setArticles(articles);
        newsMessage.setNews(news_);
        //messageBean.setArticles(articles);

        JSONObject gjson = new JSONObject();
        //JSONObject json = getUserOpenids(array.get(3).toString()); //array参数是用户组所有的用户,该方法打印array其中一个用户的详细信息
        gjson.put("touser", "oWyzd1Y1gjNCvX55fmbnYsmfzwbk"); //array.get(3));
        gjson.put("msgtype", "news");
        JSONObject news = new JSONObject();
        JSONArray articles1 = new JSONArray();
        JSONObject list = new JSONObject();
        list.put("title", "title"); //标题
        list.put("description", "description"); //描述
        list.put("url",
                 "https://image.baidu.com/search/detail?ct=503316480&z=undefined&tn=baiduimagedetail&ipn=d&word=%7B\"errcode\"%3A85005%2C\"errmsg\"%3A\"appid%20not%20bind%20weapp%20hint%3A%20%5BSIV5VA0777vr19%5D\"%7D&step_word=&ie=utf-8&in=&cl=2&lm=-1&st=undefined&cs=4120507643,3179664321&os=2372104483,2841087290&simid=4254127042,762566717&pn=2&rn=1&di=197819360322&ln=1100&fr=&fmq=1495682551054_R&fm=&ic=undefined&s=undefined&se=&sme=&tab=0&width=undefined&height=undefined&face=undefined&is=0,0&istype=0&ist=&jit=&bdtype=15&spn=0&pi=0&gsm=0&objurl=http%3A%2F%2Fh.hiphotos.baidu.com%2Fzhidao%2Fwh%253D450%252C600%2Fsign%3Db47b2138f71fbe091c0bcb105e502005%2Feac4b74543a98226004c35698f82b9014b90ebeb.jpg&rpstart=0&rpnum=0&adpicid=0&ctd=1495682560776^3_1583X697%1"); //点击图文链接跳转的地址
        list.put("picurl",
                 "https://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=宠物&step_word=&hs=0&pn=2&spn=0&di=70437016100&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&istype=2&ie=utf-8&oe=utf-8&in=&cl=2&lm=-1&st=-1&cs=3673633480%2C3539052624&os=993722498%2C1010233782&simid=4212927700%2C508965651&adpicid=0&lpn=0&ln=1979&fr=&fmq=1495682661219_R&fm=&ic=0&s=undefined&se=&sme=&tab=0&width=&height=&face=undefined&ist=&jit=&cg=&bdtype=0&oriquery=&objurl=http%3A%2F%2Fmvimg2.meitudata.com%2F5619e503938789482.jpg&fromurl=ippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3B4jtrwt_z%26e3Bv54AzdH3F4j1twAzdH3F9dd8lbn88&gsm=0&rpstart=0&rpnum=0"); //图文链接的图片
        articles1.add(list);
        news.put("articles", articles1);
        JSONObject text = new JSONObject();
        text.put("name", "======================");
        //        gjson.put("text", text);
        gjson.put("news", news);
 //       LOGGER.info(gjson);
        JSONObject gjson1 = new JSONObject();
        gjson1.put("news", messageBean);
        String result = com.alibaba.fastjson.JSONObject.toJSON(newsMessage).toString();
        //        LOGGER.info(result);
        //        LOGGER.info(gjson);
        //        LOGGER.info(gjson1);
        return result;
    }
}
