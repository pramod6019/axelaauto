<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.JobCard_Click_Image" scope="request"/>
<%mybean.doPost(request, response);%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
    <meta charset="utf-8" />
    <title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
    <link rel="stylesheet" type="text/css" href="../Library/clickphoto/css/styles.css" />
    <link rel="stylesheet" type="text/css" href="../Library/clickphoto/fancybox/jquery.fancybox-1.3.4.css" />
    <script language="JavaScript" type="text/javascript">
		var jc_id = <%=mybean.jc_id%>;
		var urlupload = 'jobcard-click-upload.jsp?jc_id='+jc_id;
		function GetImages(){
			setTimeout(function() {
				showHint("jobcard-click-browse.jsp?jc_id=" + jc_id,  "photos");
			}, 2000);
		}
    </script>
    <script src="../Library/clickphoto/jquery.min.js"></script>
    <script src="../Library/clickphoto/fancybox/jquery.easing-1.3.pack.js"></script>
    <script src="../Library/clickphoto/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
    <script src="../Library/clickphoto/webcam/webcam.js"></script>
    <script src="../Library/clickphoto/js/script.js"></script>
    <script type="text/javascript" src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
    <link rel="shortcut icon" type="image/x-icon"
	href="../admin-ifx/axela.ico">
	<link href='../Library/jquery.qtip.css' rel='stylesheet'
		type='text/css' />

	<link href="../assets/css/font-awesome.css" rel="stylesheet"
		type="text/css" />
	<link href="../assets/css/bootstrap.css" rel="stylesheet"
		type="text/css" />
	<link href="../assets/css/components-rounded.css" rel="stylesheet"
		id="style_components" type="text/css" />
	<link href="../assets/css/font-awesome.css" rel="stylesheet"
		type="text/css" />
		<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />


	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript"
		src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
    
    <link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>

    <body onload="showHint('jobcard-click-browse.jsp?jc_id=' + jc_id,  'photos');" leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0" border="0">
<div><%=mybean.LinkHeader%></div>
<div id="photos" align="center"></div>
<div id="camera"> <span class="tooltip"></span> <span class="camTop"></span>
      <div id="screen"></div>
      <div id="buttons">
    <div class="buttonPane"> <a id="shootButton" href="" class="blueButton">Shoot!</a> </div>
    <div class="buttonPane hidden"> <a id="cancelButton" href="" class="blueButton">Cancel</a> <a id="uploadButton" onClick="GetImages();" href="" class="greenButton">Upload!</a> </div>
  </div>
      <span class="settings"></span></div>
</body>
</html>
