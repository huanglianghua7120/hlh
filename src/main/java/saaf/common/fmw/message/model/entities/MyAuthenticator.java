package saaf.common.fmw.message.model.entities;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
/*===========================================================+
  |   Copyright (c) 2012 赛意信息科技有限公司                                         |
+===========================================================+
  |  HISTORY                                                                        |
  | ============ ====== ============  ===========================                   |
  |  Date                     Ver.        Administrator                   Content          |
  | ============ ====== ============  ===========================                   |
  |  Jun 23, 2015            1.0           XXX                      Creation        |
 +===========================================================*/
public class MyAuthenticator extends Authenticator {
    String userName = null;
    String password = null;

    public MyAuthenticator() {
    }

    public MyAuthenticator(String username, String password) {
        this.userName = username;
        this.password = password;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, password);
    }
}

