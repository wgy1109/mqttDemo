package com.sound.util.mail;

/**   
 * 简单邮件（不带附件的邮件）发送器   
 */
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.io.File;
import javax.mail.*;
import javax.mail.internet.*;

import com.sound.util.Const;

import javax.activation.*;

public class SimpleMailSender {
	Transport transport;
	
	public static String toAddress = "gywang@163.com";  // 邮箱

	public static void sendTextMail(String title, String content, String toAddress, Map<String, Object> map) {
		MailSenderInfo mailSenderInfo = new MailSenderInfo();
		mailSenderInfo.setToAddress(toAddress);
		mailSenderInfo.setSubject(title);
		mailSenderInfo.setContent(content);
		mailSenderInfo.setMailServerHost(Const.mailServerHost);
		mailSenderInfo.setMailServerPort(Const.MailServerPort);
		mailSenderInfo.setValidate(true);
		mailSenderInfo.setUserName(map.get("address").toString());
		mailSenderInfo.setPassword(map.get("password").toString());
		mailSenderInfo.setFromAddress(map.get("address").toString());
		try {
			sendTextMail(mailSenderInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String sendEmailCode(String email,Map<String, Object> map) {
		String msg = "您的验证码为:";
		String code = "";
		Random r = new Random();
		for (int i = 0; i < 4; i++) {
			code += r.nextInt(9);
		}
		if(validateEmail(email)){
			sendTextMail("验证码", msg+code, email, map);
			return code;
		}else{
			return null;
		}
		
	}

	/**
	 * 以文本格式发送邮件
	 * 
	 * @param mailInfo
	 *            待发送的邮件的信息
	 */
	public static boolean sendTextMail(MailSenderInfo mailInfo) throws Exception {
		// 判断是否需要身份认证
		MyAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		if (mailInfo.isValidate()) {
			// 如果需要身份认证，则创建一个密码验证器
			authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session.getDefaultInstance(pro, authenticator);

		// 根据session创建一个邮件消息
		Message mailMessage = new MimeMessage(sendMailSession);
		// 创建邮件发送者地址
		Address from = new InternetAddress(mailInfo.getFromAddress());
		// 设置邮件消息的发送者
		mailMessage.setFrom(from);
		// 创建邮件的接收者地址，并设置到邮件消息中
		Address to = new InternetAddress(mailInfo.getToAddress());
		mailMessage.setRecipient(Message.RecipientType.TO, to);
		// 设置邮件消息的主题
		mailMessage.setSubject(mailInfo.getSubject());
		// 设置邮件消息发送的时间
		mailMessage.setSentDate(new Date());
		// 设置邮件消息的主要内容
		String mailContent = mailInfo.getContent();
		mailMessage.setText(mailContent);
		// 发送邮件
		Transport.send(mailMessage);
		return true;
	}

	/**
	 * 以HTML格式发送邮件
	 * 
	 * @param mailInfo
	 *            待发送的邮件信息
	 */
	public boolean sendHtmlMail(MailSenderInfo mailInfo) throws Exception {
		// 判断是否需要身份认证
		MyAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		// 如果需要身份认证，则创建一个密码验证器
		if (mailInfo.isValidate()) {
			authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session.getDefaultInstance(pro, authenticator);

		// 根据session创建一个邮件消息
		Message mailMessage = new MimeMessage(sendMailSession);
		// 创建邮件发送者地址
		Address from = new InternetAddress(mailInfo.getFromAddress());
		// 设置邮件消息的发送者
		mailMessage.setFrom(from);
		// 创建邮件的接收者地址，并设置到邮件消息中
		Address to = new InternetAddress(mailInfo.getToAddress());
		// Message.RecipientType.TO属性表示接收者的类型为TO
		mailMessage.setRecipient(Message.RecipientType.TO, to);
		// 设置邮件消息的主题
		mailMessage.setSubject(mailInfo.getSubject());
		// 设置邮件消息发送的时间
		mailMessage.setSentDate(new Date());
		// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
		Multipart mainPart = new MimeMultipart();
		// 创建一个包含HTML内容的MimeBodyPart
		BodyPart html = new MimeBodyPart();
		// 设置HTML内容
		html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
		mainPart.addBodyPart(html);
		// 将MiniMultipart对象设置为邮件内容
		mailMessage.setContent(mainPart);
		// 发送邮件
		Transport.send(mailMessage);
		return true;
	}

	/*
	 * @title:标题
	 * 
	 * @content:内容
	 * 
	 * @type:类型,1:文本格式;2:html格式
	 * 
	 * @tomail:接收的邮箱
	 */
	public boolean sendMail(String title, String content, String type, String tomail) throws Exception {

		// 这个类主要是设置邮件
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost("smtp.qq.com");
		mailInfo.setMailServerPort("25");
		mailInfo.setValidate(true);
		mailInfo.setUserName("itfather@1b23.com");
		mailInfo.setPassword("tttt");// 您的邮箱密码
		mailInfo.setFromAddress("itfather@1b23.com");
		mailInfo.setToAddress(tomail);
		mailInfo.setSubject(title);
		mailInfo.setContent(content);
		// 这个类主要来发送邮件

		SimpleMailSender sms = new SimpleMailSender();

		if ("1".equals(type)) {
			return sms.sendTextMail(mailInfo);// 发送文体格式
		} else if ("2".equals(type)) {
			return sms.sendHtmlMail(mailInfo);// 发送html格式
		}
		return false;
	}

	/**
	 * @param SMTP
	 *            邮件服务器
	 * @param PORT
	 *            端口
	 * @param EMAIL
	 *            本邮箱账号
	 * @param PAW
	 *            本邮箱密码
	 * @param toEMAIL
	 *            对方箱账号
	 * @param TITLE
	 *            标题
	 * @param CONTENT
	 *            内容
	 * @param TYPE
	 *            1：文本格式;2：HTML格式
	 */
	public static void sendEmail(String SMTP, String PORT, String EMAIL, String PAW, String toEMAIL, String TITLE,
			String CONTENT, String TYPE) throws Exception {

		// 这个类主要是设置邮件
		MailSenderInfo mailInfo = new MailSenderInfo();

		mailInfo.setMailServerHost(SMTP);
		mailInfo.setMailServerPort(PORT);
		mailInfo.setValidate(true);
		mailInfo.setUserName(EMAIL);
		mailInfo.setPassword(PAW);
		mailInfo.setFromAddress(EMAIL);
		mailInfo.setToAddress(toEMAIL);
		mailInfo.setSubject(TITLE);
		mailInfo.setContent(CONTENT);
		// 这个类主要来发送邮件

		SimpleMailSender sms = new SimpleMailSender();

		if ("1".equals(TYPE)) {
			sms.sendTextMail(mailInfo);
		} else {
			sms.sendHtmlMail(mailInfo);
		}

	}
	
	/**
     * 发送邮件
     * 
     * @param mailInfo
     *            邮件信息
     * @param attachment
     *            附件
     */
    public void doSendHtmlEmail(MailSenderInfo mailInfo, File attachment, File attachment2, File attachment3) {
        try {
        	MyAuthenticator authenticator = null;
    		Properties pro = mailInfo.getProperties();
    		Session sendMailSession = Session.getDefaultInstance(pro, authenticator);
    		Message message = new MimeMessage(sendMailSession);
    		Address from1 = new InternetAddress(mailInfo.getFromAddress());
    		message.setFrom(from1);
    		Address to = new InternetAddress(mailInfo.getToAddress());
    		message.setRecipient(Message.RecipientType.TO, to);
    		message.setSubject(mailInfo.getSubject());
            // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();
            // 添加邮件正文
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(mailInfo.getContent(), "text/html;charset=UTF-8");
            multipart.addBodyPart(contentPart);
            // 添加附件的内容
            if (attachment != null) {
                BodyPart attachmentBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachment);
                attachmentBodyPart.setDataHandler(new DataHandler(source));
                // 网上流传的解决文件名乱码的方法，其实用MimeUtility.encodeWord就可以很方便的搞定
                // 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
                sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
                attachmentBodyPart.setFileName("=?utf-8?B?" + enc.encode(attachment.getName().getBytes()) + "?=");
                multipart.addBodyPart(attachmentBodyPart);
            }
            // 添加附件的内容2
            if (attachment2 != null) {
                BodyPart attachmentBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachment2);
                attachmentBodyPart.setDataHandler(new DataHandler(source));
                sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
                attachmentBodyPart.setFileName("=?utf-8?B?" + enc.encode(attachment2.getName().getBytes()) + "?=");
                multipart.addBodyPart(attachmentBodyPart);
            }
            // 添加附件的内容3
            if (attachment3 != null) {
                BodyPart attachmentBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachment3);
                attachmentBodyPart.setDataHandler(new DataHandler(source));
                sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
                attachmentBodyPart.setFileName("=?utf-8?B?" + enc.encode(attachment3.getName().getBytes()) + "?=");
                multipart.addBodyPart(attachmentBodyPart);
            }
            // 将multipart对象放到message中
            message.setContent(multipart);
            // 保存邮件
            message.saveChanges();
            
            transport = sendMailSession.getTransport();
            // smtp验证，就是你用来发邮件的邮箱用户名密码
            transport.connect(mailInfo.getMailServerHost(), mailInfo.getUserName(), mailInfo.getPassword());
            // 发送
            transport.sendMessage(message, message.getAllRecipients());

            System.out.println("send success!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

	public static boolean validateEmail (String email){
		if(email == null) return false;
		if(email.length()>50) return false;
		return email.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	}

}
