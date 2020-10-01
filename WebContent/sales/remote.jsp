<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.sales.Remote" scope="request"/>
<%mybean.doPost(request, response);%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<HTML style="overflow: hidden; display: block;" xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<link href='../Library/jquery.qtip.css' rel='stylesheet' type='text/css' />

<link href="../assets/css/font-awesome.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet"
		type="text/css" />
		 
<link href="../assets/css/components-rounded.css" rel="stylesheet"
	id="style_components" type="text/css" />
<link href="../assets/css/font-awesome.css" rel="stylesheet"
	type="text/css" />
<script type="text/javascript" src="../Library/smart.js"></script>
<LINK REL="STYLESHEET" TYPE="text/css"
		HREF="../assets/css/footable.core.css">
<script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
<script>
		document.getElementsByTagName("body")[0].style.overflow = "hidden";
		document.getElementsByTagName("html")[0].style.overflow = "hidden";
	
		window.onresize = resize_iframe;
		function resize_iframe() {
	
		  var viewportwidth;
		  var viewportheight;
		  var header = document.getElementById("iframe_id").offsetTop;
		  // the more standards compliant browsers (mozilla/netscape/opera) use window.innerWidth and window.innerHeight
		  if (typeof window.innerWidth != 'undefined') {
		    viewportwidth = window.innerWidth,
		    viewportheight = window.innerHeight
		    viewportheight = viewportheight - header;
		  }
	
		  // IE6,IE7 in standards compliant mode (i.e. with a valid doctype as the first line in the document)
		  else if (typeof document.documentElement != 'undefined' && typeof document.documentElement.clientWidth != 'undefined' && document.documentElement.clientWidth != 0) {
		    viewportwidth = document.documentElement.clientWidth,
		    viewportheight = document.documentElement.clientHeight
		    viewportheight = viewportheight - header;
		  }
	
		  // older versions of IE
		  else {
		    viewportwidth = document.getElementsByTagName('body')[0].clientWidth,
		    viewportheight = document.getElementsByTagName('body')[0].clientHeight
		    viewportheight = viewportheight - header;
		  }
	
		  //document.write('<p>Your viewport width is '+viewportwidth+'x'+viewportheight+'</p>');
		  var header = document.getElementById("iframe_id").offsetTop;
		  document.getElementById("iframe_id").style.height=viewportheight+"px";
		}
	</script>

 <link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"

	rel="stylesheet" type="text/css" />
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
<body data-twttr-rendered="true" style="overflow: hidden;">
<%@include file="../portal/header.jsp" %>
<p align="center">
<iframe id="iframe_id" name="iframe_id" scrolling="auto" src="<%=mybean.url%>" style="position: relative; bottom: 0px; left: 0px; top: 0px; height: 509px;" frameborder="0" height="200px" align="middle" width="100%" onload="resize_iframe()">
</iframe></p>
<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript"
		src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
		<script src="../assets/js/footable.js" type="text/javascript"></script>
</body>
</HTML>