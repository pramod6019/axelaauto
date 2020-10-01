<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Executives_Access_Log"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="../assets/css/font-awesome.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/components-rounded.css" rel="stylesheet"
	id="style_components" type="text/css" />
<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />

<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		document.form1.txt_search.focus();
	}
	function disp() { //v1.0
		alert("gdfh");
	}
</script>
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
<body>
	<%@include file="header.jsp"%>
	<%@include file="../Library/list-body.jsp"%>
	<%@include file="../Library/admin-footer.jsp"%>
	<script type="text/javascript" src="../Library/Validate.js"></script>
</body>
</HTML>
