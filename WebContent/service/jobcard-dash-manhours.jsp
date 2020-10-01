<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.JobCard_Dash_ManHours" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
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
		<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />


	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript"
		src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
<body   leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
<%@include file="../portal/header.jsp" %> <%@include file="../Library/jobcard-dash.jsp" %><TABLE cellSpacing="0" cellPadding="0" width="98%" height="300" border="0" align="center">
<form name="form1"  method="post">
<tr align="center">
          <td align="center" valign="middle"><table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="listtable">
          <tbody>
          <%if(mybean.flag.equals("1")) {%>
           <tr valign="left">
                <td align="center">                
                <table width="100%" border="0" cellpadding="0" cellspacing="0" >
                
<tr>
                    <td align="center">User:&nbsp;<select name="dr_emp_id" class="selectbox" id="dr_emp_id" onChange="document.form1.submit()">
                        <%=mybean.PopulateUser()%>
                    </select>                    </td>
                  </tr>
                 
                   </table>
                    </td>
              </tr>
              <%}%>
            </tbody>
          </table></td>
        </tr>
  <TR>
    <TD height="300" align="center" vAlign="top"><%=mybean.StrHTML%></TD>
  </TR>
  </form>
</TABLE> <%@include file="../Library/admin-footer.jsp" %></body>
</HTML>