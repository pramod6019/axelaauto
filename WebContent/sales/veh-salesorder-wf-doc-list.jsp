<%@ page errorPage="../portal/error-page.jsp" %> 
<jsp:useBean id="mybean" class="axela.sales.Veh_Salesorder_Wf_Doc_List" scope="request"/>
<%mybean.doPost(request,response); %>
<jsp:setProperty name="mybean" property="*" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<link rel="shortcut icon"  type="image/x-icon" href="../admin-ifx/axela.ico">   

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
<script type="text/javascript" src="../ckeditor/ckeditor.js"></script>

	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript"
		src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
       
     <link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"

	rel="stylesheet" type="text/css" />
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD> 
    <body bgColor="#ffffff" onLoad="FormFocus();" leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0"><%@include file="../portal/header.jsp" %>
    <TABLE width="98%" border="0" align="center" cellPadding="0" cellSpacing="0">
    <TR>
    <TD align="left" bgColor="white"><a href="../portal/home.jsp">Home</a> &gt; <a href="../sales/index.jsp">Sales</a> &gt; <a href="../sales/veh-salesorder.jsp">Sales Order</a> &gt; <a href="veh-salesorder-list.jsp?so_id=<%=mybean.doc_so_id%>"><%=mybean.so_no%></a> &gt; <a href="veh-salesorder-wf-doc-list.jsp?so_id=<%=mybean.doc_so_id%>">List Work Flow Documents</a>: </TD>
    </TR>
    <TR>
    <TD align="center" vAlign="top" bgColor="white"><font color="#ff0000" ><b><%=mybean.msg%></b></font>
      <br></TD>
    </TR>
    
      <tr>
        <td align="right"><a href="veh-salesorder-wf-doc-update.jsp?add=yes&so_id=<%=mybean.doc_so_id%>">Add New Work Flow Document...</a></td>
      </tr>
      <tr>
        <td align="right">&nbsp;</td>
      </tr>
      <tr>
        <td height="200" align="center" valign="top"><%=mybean.StrHTML%></td>
      </tr>
    
    <TR>
      <TD align="center" vAlign="top">&nbsp;</TD>
    </TR>
    </TABLE> <%@include file="../Library/admin-footer.jsp" %></body>
    </HTML>
  