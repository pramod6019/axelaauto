<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.accounting.Report_Order_Status"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%></title>
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
<link
	href="../Library/theme<%=mybean.GetTheme(request)%>/font-awesome.css"
	rel="stylesheet" media="screen" type="text/css" />
<script type="text/javascript" src="../Library/jquery.js"></script>
<script type="text/javascript" src="../Library/jquery-ui.js?target=<%=mybean.jsver%>"></script>
<script type="text/javascript" src="../Library/Validate.js?target=<%=mybean.jsver%>"></script>
<link rel="stylesheet" type="text/css"
	href="../Library/jquery.multiselect.css" />
<script type="text/javascript" src="../Library/jquery.multiselect.js"></script>
<script type="text/javascript">
</script>

<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
<body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
	<%@include file="../portal/header.jsp"%>
	<TABLE width="98%" border="0" align="center" cellPadding="0"
		cellSpacing="0">
		<TR>
			<TD align="left" bgColor="white"><a href="../portal/home.jsp">Home</a>&nbsp;&gt;&nbsp;<a
				href="index.jsp">Inventory</a>&nbsp;&gt;&nbsp;<a
				href="report-order-status.jsp?add=yes">Order Status</a>:</TD>
		</TR>
		<TR>
			<TD align="center" vAlign="top" bgColor="white"><font
				color="#ff0000"><b><%=mybean.msg%></b></font> <br></TD>
		</TR>

		<TR>
			<TD height="300" align="center" vAlign="top" bgColor="white">
				<form name="form1" action="report-order-status.jsp?add=yes"
					method="post">
					<table width="100%" border="1" align="center" cellpadding="0"
						cellspacing="0" class="tableborder">
						<tr>
							<td>
                                <table width="100%" border="0" align="center" cellpadding="0"
									cellspacing="0" class="listtable formdata">
									<tr align="center">
										<th colspan="2">Order Status</th>
									</tr>
									<tr>
										<td>&nbsp;</td>
										<td><font size="1">Form fields marked with a red
												asterisk <b><font color="#ff0000">*</font></b> are required.
										</font></td>
									</tr>
									<tr align="right">
										<td colspan="1" valign="middle">Voucher Type<font color="#ff0000">*</font>:
										</td>
										<td align="left"><select name="dr_voucher_type" id="dr_voucher_type"
											class="selectbox" >
												<%=mybean.PopulateVoucherType()%>
										</select></td>
									</tr>   
									<tr align="right">
										<td colspan="1" valign="middle">Voucher No.<font
											color="#ff0000">*</font>:&nbsp;
										</td>
										<td align="left"><input type="text" name="txt_voucher_no" class="textbox"
											id="txt_voucher_no" value="<%=mybean.voucher_no%>">
										</td>
									</tr>
									<tr align="center">
										<td colspan="2" valign="middle">
										
									<input name="add_button" type="submit" class="button"
											id="add_button" value="Go" /></td>
									</tr>
									<tr>
									<td colspan="2"><%=mybean.StrHTML %>  
									</td>
									</tr>
									
								</table>
							</td>
						</tr>
						
					</table>
				</form>
			</TD>
		</TR>
	</TABLE>
	<%@include file="../Library/admin-footer.jsp"%></body>
</HTML>
