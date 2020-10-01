<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.accounting.ManagePayDays" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<LINK REL="STYLESHEET" TYPE="text/css" HREF="../Library/theme<%=mybean.GetTheme(request)%>/jquery-ui.css">
<LINK REL="STYLESHEET" TYPE="text/css" HREF="../Library/theme<%=mybean.GetTheme(request)%>/style.css">
<link href="../Library/theme<%=mybean.GetTheme(request)%>/menu.css" rel="stylesheet" media="screen" type="text/css" />
<link href="../Library/theme<%=mybean.GetTheme(request)%>/font-awesome.css" rel="stylesheet" media="screen" type="text/css" />
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
<script type="text/javascript" src="../Library/Validate.js?target=<%=mybean.jsver%>"></script>
<script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>

<script type="text/javascript" src="../Library/smart.js"></script>
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
<body <%if(mybean.advSearch.equals(null)  || mybean.advSearch.equals("")){%>onLoad="LoadRows();FormFocus();" <%}%>leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
<%@include file="../portal/header.jsp" %>

   <TABLE width="98%" border="0" align="center" cellPadding="0" cellSpacing="0">
    <%if(request.getParameter("group")==null) {%>
  <tr>
    <td width="80" colspan="2" align="left" valign="middle"><%=mybean.LinkHeader%>&nbsp;<br></td>
  </tr>
 <%}%>
    <tr>
    <td colspan="2" align="right" valign="middle"><font color="#ff0000"><b><%=mybean.LinkAddPage%></b></font><br></td>
  </tr>
  <TR>         
    <TD align="center" vAlign="top" ><b><font color="#FF0000"><%=mybean.msg%></font></b><br/>
     </Td>    
  </TR>
  <tr>
  <td height="300" align="center" valign="top"><%=mybean.StrHTML%></td>
  </tr>
  </TABLE>
 
  <%@include file="../Library/admin-footer.jsp" %></body>
</HTML>
