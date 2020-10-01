<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Enquiry_Dash_Customer"
	scope="request" />
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<link rel="shortcut icon" type="image/x-icon"
	href="../admin-ifx/axela.ico">
<link rel="shortcut icon" type="image/x-icon"
	href="../admin-ifx/axela.ico">
<link href='../Library/jquery.qtip.css' rel='stylesheet' type='text/css' />
<link href="../assets/css/font-awesome.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/components-rounded.css" rel="stylesheet"
	id="style_components" type="text/css" />
<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />
<link href="../assets/css/style.css" rel="stylesheet" type="text/css" />
</HEAD>

<body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
	<%@include file="../portal/header.jsp"%>
	<table width="98%" border="0" align="center" cellpadding="0"
		cellspacing="0">
		<tr>
			<TD><a href="../portal/home.jsp">Home</a> &gt; <a
				href="index.jsp">Sales</a> &gt; <a href="enquiry.jsp">Enquiry</a>
				&gt; <a href="enquiry-list.jsp?all=yes">List Enquiry</a> &gt; <a
				href="enquiry-dash.jsp?enquiry_id=<%=mybean.enquiry_id%>"><b><%=mybean.enquiry_title%>
						(<%=mybean.enquiry_id%>)</b></a>:</TD>
		</tr>
		<tr>
			<TD>
				<ul id="tabnav">
					<li
						<% if(request.getServletPath().equals("/sales/enquiry-dash.jsp")){%>
						class="selecttab" <% } %>><a
						href="enquiry-dash.jsp?pop=yes&amp;enquiry_id=<%=mybean.enquiry_id%>">Enquiry
							Details</a></li>

					<li
						<% if(request.getServletPath().equals("/sales/enquiry-dash-followup.jsp")) {%>
						class="selecttab" <% } %>><a
						href="enquiry-dash-followup.jsp?enquiry_id=<%=mybean.enquiry_id%>">Follow-up</a>
					</li>
					<li
						<% if(request.getServletPath().equals("/sales/enquiry-dash-crmfollowup.jsp")) {%>
						class="selecttab" <% } %>><a
						href="enquiry-dash-crmfollowup.jsp?enquiry_id=<%=mybean.enquiry_id%>">CRM
							Follow-up</a></li>
					<li
						<% if(request.getServletPath().equals("/sales/enquiry-dash-customer.jsp")){%>
						class="selecttab" <% } %>><a
						href="enquiry-dash-customer.jsp?all=yes&amp;enquiry_id=<%=mybean.enquiry_id%>">Customer</a>
					</li>
					<li
						<% if(request.getServletPath().equals("/sales/enquiry-dash-docs.jsp")){%>
						class="selecttab" <% } %>><a
						href="enquiry-dash-docs.jsp?enquiry_id=<%=mybean.enquiry_id%>">Documents</a>
					</li>
					<li
						<% if(request.getServletPath().equals("/sales/enquiry-dash-testdrive.jsp")) {%>
						class="selecttab" <% } %>><a
						href="enquiry-dash-testdrive.jsp?enquiry_id=<%=mybean.enquiry_id%>">Test
							Drives</a></li>
					<li
						<% if(request.getServletPath().equals("/sales/enquiry-dash-quote.jsp")){%>
						class="selecttab" <% } %>><a
						href="enquiry-dash-quote.jsp?enquiry_id=<%=mybean.enquiry_id%>">Quotes</a>
					</li>
					<li
						<% if(request.getServletPath().equals("/sales/enquiry-dash-salesorder.jsp")){%>
						class="selecttab" <% } %>><a
						href="enquiry-dash-salesorder.jsp?enquiry_id=<%=mybean.enquiry_id%>">Sales
							Orders</a></li>
					<li
						<% if(request.getServletPath().equals("/sales/enquiry-dash-invoice.jsp")){%>
						class="selecttab" <% } %>><a
						href="enquiry-dash-invoice.jsp?enquiry_id=<%=mybean.enquiry_id%>">Invoices</a>
					</li>
					<li
						<% if(request.getServletPath().equals("/sales/enquiry-dash-receipt.jsp")){%>
						class="selecttab" <% } %>><a
						href="enquiry-dash-receipt.jsp?enquiry_id=<%=mybean.enquiry_id%>">Receipts</a>
					</li>
					<li
						<% if(request.getServletPath().equals("/sales/enquiry-dash-history.jsp")){%>
						class="selecttab" <% } %>><a
						href="enquiry-dash-history.jsp?enquiry_id=<%=mybean.enquiry_id%>">History</a>
					</li>
				</ul>
			</TD>
		</tr>
	</table>
	<!-- #EndLibraryItem -->
	<table width="98%" border="0" align="center" cellpadding="0"
		cellspacing="0">
		<TR>
			<TD align="center" vAlign="top" colspan="2"><font
				color="#ff0000"><b><%=mybean.msg%></b></font></TD>
		</TR>
		<tr>
			<td colspan="2" align="center" height="300">
				<%if(mybean.msg.equals("")){%>
				<table width="100%" border="1" cellspacing="0" cellpadding="0"
					class="listtable">
					<tr>
						<th colspan="2">Customer</th>
					</tr>
					<tr>
						<td colspan="2" align="center"><a
							href="../customer/customer-list.jsp?customer_id=<%=mybean.customer_id%>">
								<b><%=mybean.customer_name%>&nbsp;(<%=mybean.customer_id%>)</b>
						</a></td>
					</tr>
					<tr>
						<td valign="top">Communication:</td>
						<td><%=mybean.customer_communication%></td>
					</tr>
					<tr>
						<td valign="top">Address:</td>
						<td><%=mybean.customer_address%></td>
					</tr>
					<tr>
						<td valign="top">Landmark:</td>
						<td><%=mybean.customer_landmark%></td>
					</tr>
					<tr>
						<td valign="top">Sales Consultant:</td>
						<td><%=mybean.customer_exe%></td>
					</tr>
					<tr>
						<td valign="top">Active:</td>
						<td><%=mybean.customer_active%></td>
					</tr>
					<tr>
						<td valign="top">Notes:</td>
						<td><%=mybean.customer_notes%></td>
					</tr>
					<tr>
						<td colspan="2" align="center"><%=mybean.ListContact(mybean.customer_id, mybean.comp_id)%></td>
					</tr>
				</table> <%}%>
			</td>
		</tr>
	</table>
	<%@include file="../Library/admin-footer.jsp"%>
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript"
		src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
</body>
</html>