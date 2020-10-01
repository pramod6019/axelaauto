<%@ page import="java.util.*" %>
<%@ page import="javax.mail.*" %>
<%@ page import="javax.mail.internet.*" %>
<%@ page import="javax.activation.*" %>
<%@ page import="javax.net.*" %>

<%

String from = "vijay@emax.in";
String to = "vijay@unisolonline.com";
String bcc = "";
String cc = "";
String subject = "Test Mail";
String mess = "Test mail from Vijay.";

Session ses = null;
 Properties props = System.getProperties();
//  try {
 props.put("mail.transport.protocol", "smtp");
             props.put("mail.smtp.port", "25");
             props.put("mail.smtp.host", "192.168.1.7");
             props.put("mail.smtp.auth", "true");
			 props.put("mail.smtp.debug", "true");
            // Get session
            ses = Session.getInstance(props,
            new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication("support@mycampus.local", "support");}
            });


        // Define message
        MimeMessage message =new MimeMessage(ses);
        
        message.setFrom(new InternetAddress(from));
        //if(bcc.equals("")) bcc="info@unisolonline.com";
        message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
        if(!cc.equals(""))
        message.addRecipient(Message.RecipientType.CC,new InternetAddress(cc));
        if(!bcc.equals(""))
        message.addRecipient(Message.RecipientType.BCC,new InternetAddress(bcc));
        message.setSubject(subject);
        // create the message part
        Multipart multipart = new MimeMultipart();
        //fill message
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(mess, "text/html");
        multipart.addBodyPart(messageBodyPart);

        
        // Put parts in message
        message.setContent(multipart);
        
        // Send the message
        Transport.send( message );
        System.out.println("email sent");

 //       } catch(Exception ex) {
 //           System.out.println("email error"+ex);
//        }
        


%>