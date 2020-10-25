//package saaf.common.fmw.service.mail.utils;
//
//import javax.mail.Message;
//import javax.mail.MessagingException;
//import javax.mail.Part;
//import javax.mail.internet.MimeMessage;
//
//    public static class getMailInfo extends Thread {
//        Message message[] = null;
//        ShowMail re = null;
//
//        public getMailInfo(Message[] message) {
//            this.message = message;
//        }
//
//        @Override
//        public void run() {
//            // TODO Auto-generated method stub
//            super.run();
//            if (null != message) {
//                for (int i = 0; i < message.length; i++) {
//                    try {
//                        re = new ShowMail((MimeMessage)message[i]);
//
//                        LOGGER.info("邮件　" + i + "　主题:　" + re.getSubject());
//                        LOGGER.info("邮件　" + i + "　是否需要回复:　" + re.getReplySign());
//                        LOGGER.info("邮件　" + i + "　是否已读:　" + re.isNew());
//                        LOGGER.info("邮件　" + i + "　是否包含附件:　" + re.isContainAttach((Part)message[i]));
//                        LOGGER.info("邮件　" + i + "　发送时间:　" + re.getSentDate());
//                        LOGGER.info("邮件　" + i + "　发送人地址:　" + re.getFrom());
//                        LOGGER.info("邮件　" + i + "　收信人地址:　" + re.getMailAddress("to"));
//                        LOGGER.info("邮件　" + i + "　抄送:　" + re.getMailAddress("cc"));
//                        LOGGER.info("邮件　" + i + "　暗抄:　" + re.getMailAddress("bcc"));
//                        re.setDateFormat("yyyy年MM月dd日");
//                        LOGGER.info("邮件　" + i + "　发送时间:　" + re.getSentDate());
//                        LOGGER.info("邮件　" + i + "　邮件ID:　" + re.getMessageId());
//                        re.getMailContent((Part)message[i]);
//                        LOGGER.info("邮件　" + i + "　正文内容:　\r\n" +
//                                re.getBodyText());
//                    } catch (MessagingException e) {
//                        // TODO Auto-generated catch block
//                        LOGGER.error(e.getMessage(), e);
//                    } catch (Exception e) {
//                        // TODO Auto-generated catch block
//                        LOGGER.error(e.getMessage(), e);
//                    }
//                }
//            }
//        }
//    }