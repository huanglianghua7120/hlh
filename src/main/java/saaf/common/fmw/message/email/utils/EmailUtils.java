package saaf.common.fmw.message.email.utils;


import com.base.adf.common.utils.SToolUtils;
import com.base.sie.common.exception.email.EmailException;

import java.io.UnsupportedEncodingException;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/*===========================================================+
  |   Copyright (c) 2012 赛意信息科技有限公司                                         |
+===========================================================+
  |  HISTORY                                                                        |
  | ============ ====== ============  ===========================                   |
  |  Date                     Ver.        Administrator                   Content          |
  | ============ ====== ============  ===========================                   |
  |  Jun 23, 2015            1.0           XXX                      Creation        |
  |  邮件发送的工具类，里面包含验证邮件地址、获取附件等方法
 +===========================================================*/
public class EmailUtils {
    //    private static final ADFLogger LOGGER = ADFLogger.createADFLogger(EmailUtils.class);
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailUtils.class);

    public EmailUtils() {
        super();
    }

    public static void main(String[] args) throws EmailException {
        LOGGER.info(EmailUtils.validateEmailList(new String[] { "test@mai.com", "justTest", "hello@mai.com" }));
    }

    //组合验证邮箱地址

    public static String validateEmailList(String[] mailArray) {
        StringBuffer toList = new StringBuffer();
        int length = mailArray.length;
        if (mailArray != null && length == 1) {
            boolean validateResult = false;
            validateResult = SToolUtils.validateEmail(mailArray[0]);
            if (validateResult) {
                toList.append(mailArray[0]);
            }
        } else {
            for (int i = 0; i < length; i++) {
                boolean validateResult = false;
                validateResult = SToolUtils.validateEmail(mailArray[i]);
                if (validateResult) {
                    toList.append(mailArray[i]);
                    toList.append(",");
                }
            }
        }
        String resutlValue = toList.toString();
        if (resutlValue.endsWith(",")) {
            resutlValue = resutlValue.substring(0, (resutlValue.length() - 1));
        }
        return resutlValue;
    }

    //添加多个附件
    public static void addTach(String[] fileList, Multipart multipart) throws MessagingException, UnsupportedEncodingException {
        for (int index = 0; index < fileList.length; index++) {
            MimeBodyPart mailArchieve = new MimeBodyPart();
            FileDataSource fds = new FileDataSource(fileList[index]);
            mailArchieve.setDataHandler(new DataHandler(fds));
            mailArchieve.setFileName(MimeUtility.encodeText(fds.getName(), "GBK", "B"));
            multipart.addBodyPart(mailArchieve);
        }
    }
}