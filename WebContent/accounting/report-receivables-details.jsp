<%@ page errorPage="error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.accounting.Report_Receivables_Details" scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
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
<LINK REL="STYLESHEET" TYPE="text/css"
	HREF="../Library/theme<%=mybean.GetTheme(request)%>/style.css">
<link href="../Library/theme<%=mybean.GetTheme(request)%>/menu.css"
	rel="stylesheet" media="screen" type="text/css" />
<link
	href="../Library/theme<%=mybean.GetTheme(request)%>/font-awesome.css"
	rel="stylesheet" media="screen" type="text/css" />
<script type="text/javascript"
	src="../Library/Validate.js?target=<%=mybean.jsver%>"></script>
<script type="text/javascript" src="../Library/jquery.js"></script>
<script type="text/javascript"
	src="../Library/jquery-ui.js?target=<%=mybean.jsver%>"></script>
<script type="text/javascript"
	src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
<script type="text/javascript"
	src="../Library/Validate.js?target=<%=mybean.jsver%>"></script>
<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		//document.form1.dr_branch_id.focus()  
	}
	$("#maincity").change(function(){
		  showHint('../sales/enquiry-check.jsp?city=yes&city_id='+$(this).val(),'zone');
	    });
</script>
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
<body onLoad="FormFocus()" leftmargin="0" rightmargin="0" topmargin="0"
	bottommargin="0">
	<%@include file="../portal/header.jsp"%>
	<table width="98%" border="0" align="center" cellpadding="0"
		cellspacing="0">
		<tr>
			<TD><a href="../portal/home.jsp">Home</a> &gt; <a
				href=../accounting/index.jsp>Accounting</a> &gt; <a
				href=report-receivables.jsp>Receivables</a>:</TD>
		</tr>
	</table>
	<TABLE width="98%" border="0" align="center" cellPadding="0"
		cellSpacing="0">
		<!-- <%//if(!mybean.go.equals("")){%>
<tr>
  <td align="right"><input name="PrintButton" type="button" class="button" id="PrintButton" value="Export" onClick="remote=window.open('<%//=mybean.LinkExportPage%>','print','');remote.focus();"></td>
  </tr>
  <%//}%>-->
		<TR>
			<TD align="center" vAlign="top" bgColor="white"><font
				color="#ff0000"><b><%=mybean.msg%></b></font> <br></TD>
		</TR>
		<TR>
			<TD align="center" vAlign="top"><form name="form1" method="post">
					<table width="100%" border="0" align="center" cellpadding="0"
						cellspacing="0" class="tableborder">
						<tr>
							<td><table width="100%" border="0" align="center"
									cellpadding="0" cellspacing="0" class="listtable formdata">
									<tr align="center">
										<th colspan="11">Ledger Statement</th>
									</tr>

									<tr>
										<td align="right" nowrap>Ledger<font color=red>*</font>:
										</td>
										<td><input tabindex="-1"
											class="bigdrop select2-offscreen" id="ledger" name="ledger"
											style="width: 250px" value="<%=mybean.customer_id%>"
											type="hidden"></td>
										<td align="right">City<font color="#ff0000">*</font></td>
										<td align="left"><input tabindex="-1"
											class="bigdrop select2-offscreen" id="maincity"
											name="maincity" style="width: 250px" value="<%=mybean.customer_city_id%>" type="hidden"></td>
										<td align="right" valign="top">Zone<font color="#ff0000">*</font>:
										</td>
										<td align=left colspan="3"><span id="zone"> <%=mybean.PopulateZone(mybean.customer_city_id)%>
										</span></td>
										 <%-- <td align="right" nowrap>Start Date:</td>
										<td><input name="txt_startdate" id="txt_startdate"
											type="text" class="textbox" value="<%=mybean.startdate%>"
											size="12" maxlength="10" /></td>
										<td align="right" nowrap>End Date:</td>
										<td><input name="txt_enddate" id="txt_enddate"
											type="text" class="textbox" value="<%=mybean.enddate%>"
											size="12" maxlength="10" /></td> --%>
										<%-- <td align="right" nowrap>Type:</td>
										<td><select name="dr_voucher_type" class="selectbox"
											id="dr_voucher_type">
												<%=mybean.PopulateVoucherType()%>
										</select></td> --%>

										<td align="right"><input name="submit_button"
											type="submit" class="button" id="submit_button" value="Go" />
											<input type="hidden" name="submit_button" value="Submit"></td>
										<%
											if (!mybean.StrHTML.equals("") && mybean.ExportPerm.equals("1")) {
										%>
										<td align="right"><input name="export_button"
											type="submit" class="button" id="export_button"
											value="Export" /></td>
										<%
											}
										%>
									</tr>
								</table></td>
						</tr>
					</table>
				</form></td>
		</tr>
		<!--<tr align="center">
    <td  align="center">&nbsp;</td>
  </tr>  -->

		<tr align="center">
			<td height="300" align="center" valign="top"><%=mybean.StrHTML%></td>
		</tr>
		<tr align="center">
			<td align="center">&nbsp;</td>
		</tr>
	</TABLE>
	<%@include file="../Library/admin-footer.jsp"%></body>
</HTML>
