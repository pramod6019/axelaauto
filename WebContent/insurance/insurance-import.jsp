<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.insurance.Insurance_Import" scope="request"/>             
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title>Vehicle import New</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="refresh" content="2" >            
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>  

<body  leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
<%= mybean.msg %>     
<%= mybean.importmsg %>      
</body>
</html>
