<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.sales.Enquiry_Trigger_Status" scope="request"/>
<%mybean.doPost(request,response); %>
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
 <script language="JavaScript" type="text/javascript">
function PopulateBranch() { //v1.0
  document.form1.submit();
}
</script>
 <link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"

	rel="stylesheet" type="text/css" />
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>

<body  leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0"><%@include file="../portal/header.jsp" %>
<table width="100%" height="157" border="0" cellpadding="1" cellspacing="0">
      <form name="form1"  method="post">
      <%if(mybean.branch_id.equals("0")) {%>
      <tr align="center">
        <td colspan="2" align="center" valign="middle"><b>Enquiry Escalation Status
        </b><br>
        <br></td>
      </tr>
      <tr align="center">
        <td colspan="2" align="center" valign="middle"><font color="#ff0000" ><b><%=mybean.msg%></b></font></td>
      </tr>
      <tr align="center">
          <td colspan="2" align="center" valign="middle">Branch: 
            <select name="dr_branch_id" class="selectbox" id="dr_branch_id" onChange="PopulateBranch();">
            <%=mybean.PopulateBranch() %>
          </select>
          <br></td>
        </tr>
        <%}%>
        <tr align="center">
          <td height="300" colspan="2" align="center" valign="middle"><%=mybean.StrHTML%></td>
        </tr>
        <tr align="center">
          <td colspan="2" align="center" valign="middle">&nbsp;</td>
        </tr>
        <tr align="center">
          <td colspan="2" align="center" valign="middle"></td>
        </tr>
      </form>
    </table> <%@include file="../Library/admin-footer.jsp" %></body>
</html>
