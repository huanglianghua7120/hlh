package saaf.common.fmw.message.utils;

import com.alibaba.fastjson.JSONObject;
import com.base.adf.common.utils.FileUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map.Entry;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public final class SaafToolUtils {
private static final Logger LOGGER = LoggerFactory.getLogger(SaafToolUtils.class);
public static final String STATUS = "status";
public static final String MSG = "msg";
public static final String COUNT = "count";
public static final String DATA = "data";
public static final ApplicationContext context = new FileSystemXmlApplicationContext("classpath:saaf/common/fmw/config/spring.hibernate.cfg.xml");
private static ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();
	public SaafToolUtils() {
		super();
	}

        public static Connection getConnection(){
            SessionFactory sf  = (SessionFactory)SaafToolUtils.context.getBean("sessionFactory");
            Session session = threadLocal.get();
            if(null == session){
                session = sf.openSession();
                threadLocal.set(session);
            }
            LOGGER.info("getConnection session hash code is : " + session.hashCode() + "======================");
            return sf.openSession().connection();
        }
        public static void releaseConnection(Connection connection){
            if(null != connection){
                try {
                    connection.close();
                } catch (SQLException e) {
                    LOGGER.error(e.getMessage(), e);
                }
                Session session = threadLocal.get();
                LOGGER.info("releaseConnection session hash code is : " + session.hashCode() + "**************");
                if(null != session && session.isOpen()){
                    session.close();
                }
            }
        }
}
