package cloudify.connect;

import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Email {

	Connect ct = new Connect();

	public void postMailCustom(final String branch_id, final String emp_email_server, final String emp_email_username, final String emp_email_password,
			final String emp_email_port, final String emp_email_ssl, final String emp_email_tls, final String to, final String cc,
			final String bcc, final String from, final String subject, final String msg,
			final String attach, final String comp_id) throws MessagingException {
		Thread t = new Thread(new Runnable()
		{
			public void run()
			{

				try {// Get system properties
						// SOPError("executed....");
					if (!emp_email_server.equals("") && !emp_email_username.equals("") && !emp_email_password.equals("")
							&& !emp_email_port.equals("")) {
						Session mailSession = null;
						Properties props = System.getProperties();
						if (ct.AppRun().equals("1")) {

							// Setup mail server
							props.put("mail.transport.protocol", "smtp");
							props.put("mail.smtp.port", emp_email_port);
							props.put("mail.smtp.host", emp_email_server);
							props.put("mail.smtp.auth", "true");

							if (emp_email_ssl.equals("1")) {
								props.put("mail.smtp.ssl.enable", "true");
								props.put("mail.smtp.socketFactory.port", emp_email_port);
								props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
							} else if (emp_email_tls.equals("1")) {
								props.put("mail.smtp.starttls.enable", "true");
							}

							mailSession = Session.getInstance(props,
									new Authenticator() {
										public PasswordAuthentication getPasswordAuthentication() {
											return new PasswordAuthentication(emp_email_username, emp_email_password);
										}
									});

						} else {
							props.put("mail.smtp.host", "192.168.0.10");
							mailSession = Session.getInstance(props, null);

							// props.put("mail.transport.protocol", "smtp");
							// props.put("mail.smtp.port", "25");
							// props.put("mail.smtp.host", "mail.emax.in");
							// props.put("mail.smtp.auth", "true");
							// // Get session
							// mailSession = Session.getInstance(props,
							// new Authenticator() {
							// public PasswordAuthentication getPasswordAuthentication() {
							// return new PasswordAuthentication("vijay@emax.in",
							// "Vanilla");}
							// });
						}
						// SOPError("session===="+session);
						// Define message
						MimeMessage message = new MimeMessage(mailSession);
						message.setFrom(new InternetAddress(from));
						// SOPError("from==="+from);

						if (!to.equals("")) {
							// message.addRecipient(Message.RecipientType.CC,new
							// InternetAddress(cc));
							ArrayList recipientsArray = new ArrayList();
							StringTokenizer st = new StringTokenizer(to, ",");
							while (st.hasMoreTokens()) {
								recipientsArray.add(st.nextToken());
							}
							int sizeTo = recipientsArray.size();
							InternetAddress[] addressTo = new InternetAddress[sizeTo];
							for (int i = 0; i < sizeTo; i++) {
								addressTo[i] = new InternetAddress(recipientsArray.get(i).toString());
							}
							message.setRecipients(Message.RecipientType.TO, addressTo);
						}
						if (!cc.equals("")) {
							// message.addRecipient(Message.RecipientType.CC,new
							// InternetAddress(cc));
							ArrayList recipientsArray = new ArrayList();
							StringTokenizer st = new StringTokenizer(cc, ",");
							while (st.hasMoreTokens()) {
								recipientsArray.add(st.nextToken());
							}
							int sizeTo = recipientsArray.size();
							InternetAddress[] addressTo = new InternetAddress[sizeTo];
							for (int i = 0; i < sizeTo; i++) {
								addressTo[i] = new InternetAddress(recipientsArray.get(i).toString());
							}
							message.setRecipients(Message.RecipientType.CC, addressTo);
						}

						if (!bcc.equals("")) {
							// message.addRecipient(Message.RecipientType.CC,new
							// InternetAddress(cc));
							ArrayList recipientsArray = new ArrayList();
							StringTokenizer st = new StringTokenizer(bcc, ",");
							while (st.hasMoreTokens()) {
								recipientsArray.add(st.nextToken());
							}
							int sizeTo = recipientsArray.size();
							InternetAddress[] addressTo = new InternetAddress[sizeTo];
							for (int i = 0; i < sizeTo; i++) {
								addressTo[i] = new InternetAddress(recipientsArray.get(i).toString());
							}
							message.setRecipients(Message.RecipientType.BCC, addressTo);
							// message.addRecipient(Message.RecipientType.BCC,new
							// InternetAddress(bcc));
						}
						message.setSubject(subject, "UTF-8");

						// create the message part
						Multipart multipart = new MimeMultipart();
						// System.out.println("multipart===="+multipart);
						// fill message
						MimeBodyPart messageBodyPart = new MimeBodyPart();
						messageBodyPart.setContent(msg, "text/html; charset=UTF-8");
						multipart.addBodyPart(messageBodyPart);

						// Attachment
						if (!attach.equals("")) {
							// attach = attach.replace("/", "\\");
							String[] filepath = attach.split(";");
							for (int j = 0; j < filepath.length; j++) {
								String[] filename = filepath[j].split(",");
								messageBodyPart = new MimeBodyPart();
								javax.activation.DataSource source = new FileDataSource(filename[0]);
								messageBodyPart.setDataHandler(new DataHandler(source));
								messageBodyPart.setFileName(filename[1]);
								multipart.addBodyPart(messageBodyPart);
							}
						}

						// Fetch Embeded Image
						// System.out.println("multipart===="+msg);
						if (msg.contains("<img src=\"cid:sa-logo.jpg\" />")) {
							messageBodyPart = new MimeBodyPart();
							FileDataSource fds = new FileDataSource(ct.TemplatePath(comp_id) + "sa-logo.jpg");
							messageBodyPart.setDataHandler(new DataHandler(fds));
							messageBodyPart.setHeader("Content-ID", "<sa-logo.jpg>");
							multipart.addBodyPart(messageBodyPart);
						}
						if (msg.contains("<img src=\"cid:emailbanner1.jpg\" />")) {
							messageBodyPart = new MimeBodyPart();
							FileDataSource fds = new FileDataSource(ct.TemplatePath(comp_id) + "emailbanner1.jpg");
							messageBodyPart.setDataHandler(new DataHandler(fds));
							messageBodyPart.setDisposition(MimeBodyPart.INLINE);
							messageBodyPart.setHeader("Content-ID", "<emailbanner1.jpg>");
							multipart.addBodyPart(messageBodyPart);
						}

						// Put parts in message
						message.setContent(multipart);
						message.setSentDate(ct.kknow());
						// Send the message
						Transport.send(message);
						ct.SOPInfo("AxelaAuto-" + comp_id + "-" + branch_id + ": " + to);
					}
				} catch (Exception ex) {
					ct.SOPError(ct.ClientName + "===" + this.getClass().getName());
					ct.SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
				}
			}
		});
		t.start();
	}
}
