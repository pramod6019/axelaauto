<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Service_Report_Bay_WallBoard" scope="request"/>
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
<body  leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
<%@include file="../portal/header.jsp" %>
<TABLE width="98%" border="0" align="center" cellPadding="0" cellSpacing="0">
<form name="formemp"  method="post">
  <TR>
    <TD align="left" ><a href="../portal/home.jsp">Home</a> &gt; <a href="../portal/mis.jsp">MIS</a> &gt; <a href="service_report_bay_wallboard.jsp">Bay Wall Board</a>:</TD>
  </TR>
  <TR>
    <TD align="left" >&nbsp;</TD>
  </TR>
  <TR>
    <TD align="center" vAlign="top" height="300">
    <table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="tableborder">
        <tr>
          <td>
          <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="listtable">
              <tr align="center">
                <th>Bay Wall Board</th>
              </tr>
              <tr>
                <td align="center" valign="middle">Branch<font color=red>*</font>:
                  <%if(mybean.branch_id.equals("0")){%>
                  <select name="dr_bay_branch_id" id="dr_bay_branch_id" class="selectbox" onChange="document.formemp.submit();">
                    <%=mybean.PopulateBranch(mybean.bay_branch_id, "", "1,3", "", request)%>
                  </select>
                  <%}else{%>
                        <input type="hidden" name="dr_branch" id="dr_branch" value="<%=mybean.branch_id%>" />
                        <%=mybean.getBranchName(mybean.bay_branch_id)%>
                        <%}%>
                  </td>
              </tr>
            </table></td>
        </tr>
              <tr align="center">
                <td  align="center" valign="top" bgcolor="#FFFFFF"><%=mybean.StrHTML%></td>
              </tr>
      </table></td>
  </tr>
  </form>
</TABLE> <%@include file="../Library/admin-footer.jsp" %></body>
</HTML>
