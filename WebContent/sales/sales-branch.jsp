<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Sales_Branch"
	scope="request" />
<%mybean.doGet(request, response);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
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
<script language="JavaScript" type="text/javascript">
            function FormFocus() { //v1.0
                document.form1.dr_branch_id.focus()
            }
        </script>
 <link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"

	rel="stylesheet" type="text/css" />
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
<body onLoad="FormFocus()"
	class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>	
	<TABLE width="98%" border="0" align="center" cellPadding="0"
		cellSpacing="0">
		<TR>
			<TD align="left"><a href="../portal/home.jsp">Home</a> &gt; <a
				href="index.jsp">Sales</a> &gt; <%=mybean.heading%>:</TD>
		</TR>
		<TR>
			<TD align="center" vAlign="top"><font color="#ff0000"><b><%=mybean.msg%></b></font>
				<br></TD>
		</TR>
		<TR>
			<TD height="300" align="center" vAlign="top"><table width="100%"
					height="157" border="0" cellpadding="1" cellspacing="0">
					<tr>
						<td valign="top"><form name="form1" method="post">
								<table width="100%" border="1" align="center" cellpadding="0"
									cellspacing="0" class="tableborder">
									<tr valign="middle">
										<td><table width="100%" border="0" cellpadding="0"
												cellspacing="0" class="listtable">
												<tbody>
													<tr align="center">
														<th colspan="2"><%=mybean.heading%></th>
													</tr>
													<tr>
														<td>&nbsp;</td>
														<td align=left><font size="1">Form fields
																marked with a red asterisk <b><font color="#ff0000">*</font></b>
																are required.
														</font></td>
													</tr>
													<tr valign="middle">
														<td align="right" width="45%">Branch<font
															color="#ff0000">*</font>:
														</td>
														<td width="55%" align=left><select
															name="dr_branch_id" class="selectbox" id="dr_branch_id">
																<%=mybean.PopulateBranch(mybean.branch_id, "", "1,2", "",  request)%>
														</select> </td>
													</tr>
													<tr align="center">
														<td colspan="2" valign="middle"><input
															name="go_button" type="submit" class="button"
															id="go_button" value="GO" /></td>
													</tr>
													<tbody>
											</table></td>
									</tr>
								</table>
							</form></td>
					</tr>
				</table></TD>
		</TR>
	</TABLE>
	<%@include file="../Library/admin-footer.jsp"%>
	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript"
		src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
</body>
</HTML>
