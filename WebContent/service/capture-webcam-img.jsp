
<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Capture_Webcam_Img" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="shortcut icon"  type="image/x-icon" href="../admin-ifx/axela.ico">     
<LINK REL="STYLESHEET" TYPE="text/css" HREF="../Library/theme<%=mybean.GetTheme(request)%>/jquery-ui.css">
<LINK REL="STYLESHEET" TYPE="text/css" HREF="D:\phproot1\photobooth\assets\css\style.css">

<link href="../Library/theme<%=mybean.GetTheme(request)%>/menu.css" rel="stylesheet" media="screen" type="text/css" />
<link href="../Library/theme<%=mybean.GetTheme(request)%>/menu-mobile.css" rel="stylesheet" media="screen" type="text/css" />
<script type="text/javascript" src="../Library/Validate.js"></script>
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
<script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
<script type = "text/javaScript" src="D:\phproot1\photobooth\assets\webcam\webcam.js"></script>
<script type = "text/javaScript" src="D:\phproot1\photobooth\assets\js\script.js"></script>
<script language="JavaScript">
webcam.set_api_url('/capture');
webcam.set_quality(90);
webcam.set_shutter_sound(true);
</script>
<script language="JavaScript">
document.write(webcam.get_html(320, 240));
</script>
<script language="JavaScript">
webcam.set_hook('onComplete', 'my_completion_handler');
function_take_snapshot(){
document.getElementById('upload_results').inneeHTML = '<h1>Uploading...</h>';
webcam.snap();
}
function my_completion_handler(msg){
	if(msg.match(/(http\:\/\/\S+)/)){
		var image_url = RegExp.$1;
		document.getElementById('upload_result').innerHTML =
		'<h1>Upload Successfully</h1>' +
		'<h3>JPEG URL: '+image_url+'</h3>' +
		'<ing src="'+image_url+'">';
		webcam.reset();
	}
	else alert ("Java Error: "+msg);
	}
	</script>
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>

<body  leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
<%@include file="../portal/header.jsp" %>
	<table width="98%"  border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
     <TD><a href="../portal/home.jsp">Home</a> &gt; <a href="index.jsp">Service</a> &gt; <a href="capture-webcam-img.jsp">Web Cams</a>:</TD>
  </tr>
	</table> <%@include file="../Library/admin-footer.jsp" %></body>
</html>
