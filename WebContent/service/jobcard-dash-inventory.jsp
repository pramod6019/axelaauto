<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.JobCard_Dash_Inventory" scope="request"/>
<%mybean.doGet(request, response);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt"/>
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
<script type="text/javascript" src="../Library/quote.js"></script>
<script type="text/javascript">
function UpdateInventory(chk_id, value, chk_name)
{
    var check = document.getElementById(chk_id).checked;
    var hint = 'hint_jc_invent_id';
    var jc_id =  document.getElementById("txt_jc_id").value;
	var url = "../service/jobcard-dash-check.jsp?jc_id="+jc_id+"&checked="+check+"&value="+    value+"&name=chk_jc_invent_id&chk_invent_name="+chk_name+"";	
    showHint(url,  hint);
}

function UpdateJCInventory(value)
{
	var hint = 'hint_jc_invent_id';
    var jc_id =  document.getElementById("txt_jc_id").value;
	var url = "../service/jobcard-dash-check.jsp?jc_id="+jc_id+"&value="+value+"&name=txt_jc_inventory";	
    showHint(url,  hint);
}
</script>
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
<body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0" onload="">
<%@include file="../portal/header.jsp" %> <%@include file="../Library/jobcard-dash.jsp" %><form name="form1" id="form1" method="post" action="">
<TABLE width="98%" height="300" border="0" align="center" cellPadding="0" cellSpacing="0">
  <TR>
    <TD align="center" vAlign="top"><font color="#ff0000"><b><%=mybean.msg%></b></font></TD>
  </TR>
  <TR>
    <TD align="center" vAlign="top" height="300">
        <%=mybean.StrHTML%>
              <input name="txt_jc_id" type="hidden" id="txt_jc_id" value="<%=mybean.jc_id%>" /></td>
          </tr>
        </table>
      </form> <%@include file="../Library/admin-footer.jsp" %></body>
</HTML>
