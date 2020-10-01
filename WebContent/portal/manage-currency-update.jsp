<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Manage_Currency_Update"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />

<link href="../assets/css/font-awesome.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/components-rounded.css" rel="stylesheet"
	id="style_components" type="text/css" />
<link rel="shortcut icon" href="../test/favicon.ico" />
<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />
<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript"
	src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		document.form1.txt_currency_name.focus()
	}
</script>
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
<body onLoad="FormFocus()"
	class="page-container-bg-solid page-header-menu-fixed">

	<%@include file="header.jsp"%>

	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1><%=mybean.status%>&nbsp;Currency
						</h1>
					</div>
					<!-- END PAGE TITLE -->

				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<!-- BEGIN PAGE BREADCRUMBS -->
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="home.jsp">Home</a> &gt;</li>
						<li><a href="manager.jsp">Business Manager</a> &gt;</li>
						<li><a href="manage-currency.jsp">List Currency</a> &gt;</li>
						<li><a
							href="manage-currency-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Currency</a>:</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<div class="page-content-inner">
						<div class="tab-pane" id="">

							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>

							<div class="container-fluid portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none"><%=mybean.status%>&nbsp;Currency
									</div>
								</div>
								<div class="portlet-body">
									<div class="tab-pane" id="">
										<center>
											<font>Form fields marked with a red asterisk <b><font
													color="#ff0000">*</font></b> are required.
											</font>
										</center>
										<br />
										<form method="post" name="form1" class="form-horizontal">
											<div class="form-group">
												<label class="control-label col-md-4">Name <font
													color="#ff0000">*</font>:
												</label>
												<div class="col-md-6 col-xs-12">
													<input name="txt_currency_name" type="text"
														class="form-control" id="txt_currency_name"
														value="<%=mybean.currency_name%>" size="50"
														maxlength="255" />
												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4">Symbol <b>:</b></label>

												<div class="col-md-6 col-xs-12">
													<input name="txt_currency_symbol" type="text"
														class="form-control" id="txt_currency_symbol"
														value="<%=mybean.currency_symbol%>" size="50"
														maxlength="255" />
												</div>
											</div>
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_by == null)
														&& !(mybean.entry_by.equals(""))) {
											%>
											<div class="form-group">
												<label class="control-label col-md-4">Entry By:</label>
												<div class="col-md-6 col-xs-12"><%=mybean.entry_by%>
													<input name="entry_by" type="hidden" id="entry_by"
														value="<%=mybean.entry_by%>">
												</div>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_date == null)
														&& !(mybean.entry_date.equals(""))) {
											%>
											<div class="form-group">
												<label class="control-label col-md-4">Entry Date:</label>
												<div class="col-md-6 col-xs-12"><%=mybean.entry_date%>
													<input type="hidden" name="entry_date"
														value="<%=mybean.entry_date%>">
												</div>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.modified_by == null)
														&& !(mybean.modified_by.equals(""))) {
											%>
											<div class="form-group">
												<label class="control-label col-md-4">Modified By:</label>
												<div class="col-md-6 col-xs-12"><%=mybean.modified_by%>
													<input name="modified_by" type="hidden" id="modified_by"
														value="<%=mybean.modified_by%>">
												</div>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update")
														&& !(mybean.modified_date == null)
														&& !(mybean.modified_date.equals(""))) {
											%>
											<div class="form-group">
												<label class="control-label col-md-4">Modified Date:</label>
												<div class="col-md-6 col-xs-12"><%=mybean.modified_date%>
													<input type="hidden" name="modified_date"
														value="<%=mybean.modified_date%>">
												</div>
											</div>
											<%
												}
											%>

											<div class="form-group">
												<center>
													<%
														if (mybean.status.equals("Add")) {
													%>
													<input name="button" type="submit" class="btn btn-success"
														id="button" value="Add Currency" /> <input type="hidden"
														name="add_button" value="Add Currency"> <% 	} else if (mybean.status.equals("Update")) { %> <input type="hidden" name="update_button" value="Update Currency">
															<input name="button" type="submit"
															class="btn btn-success" id="button"
															value="Update Currency" /> <input name="delete_button"
															type="submit" class="btn btn-success" id="delete_button"
															OnClick="return confirmdelete(this)"
															value="Delete Currency" /> <% } %>
													
												</center>
											</div>
										</form>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>


	<%@include file="../Library/admin-footer.jsp"%>

</body>
</HTML>
