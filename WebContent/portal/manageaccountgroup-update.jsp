<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Managecustomergroup_Update"
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
		document.form1.txt_group_desc.focus()
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
						<h1><%=mybean.status%>&nbsp;Group
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
						<li><a href="managecustomergroup.jsp?all=yes">List Groups</a>
							&gt;</li>
						<li><a
							href="managecustomergroup-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Group</a>:</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<div class="page-content-inner">
						<div class="tab-pane" id="">

							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>

							<div class="container-fluid portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none"><%=mybean.status%>&nbsp;Group
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
												<label class="control-label col-md-4">Group Desc <font
													color="#ff0000">*</font>:
												</label>
												<div class="col-md-6 col-xs-12">
													<input name="txt_group_desc" type="text"
														class="form-control" id="txt_group_desc"
														value="<%=mybean.group_desc%>" size="50" maxlength="255" />
												</div>
											</div>

											<%
												if (mybean.status.equals("Update") && !(mybean.entry_date == null)
														&& !(mybean.entry_date.equals(""))) {
											%>
											<div class="form-group">
												<label class="control-label col-md-4">Entry By:</label>
												<div class="col-md-6 col-xs-12"><%=mybean.unescapehtml(mybean.entry_by)%>
													<input type="hidden" name="entry_by"
														value="<%=mybean.entry_by%>">
												</div>
											</div>
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
												if (mybean.status.equals("Update") && mybean.modified_by != null
														&& !mybean.modified_by.equals("")) {
											%>
											<div class="form-group">
												<label class="control-label col-md-4">Modified By:</label>
												<div class="col-md-6 col-xs-12"><%=mybean.unescapehtml(mybean.modified_by)%>
													<input type="hidden" name="modified_by"
														value="<%=mybean.modified_by%>">
												</div>
											</div>
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
														id="button" value="Add Group"
														onClick="return SubmitFormOnce(document.form1, this);" />
													<input type="hidden" name="add_button" value="yes">
														<%
															} else if (mybean.status.equals("Update")) {
														%> <input type="hidden" name="update_button" value="yes">
															<input name="button" type="submit"
															class="btn btn-success" id="button" value="Update Group"
															onClick="return SubmitFormOnce(document.form1, this);" />
															<input name="delete_button" type="submit"
															class="btn btn-success" id="delete_button"
															OnClick="return confirmdelete(this)" value="Delete Group" />
															<%
																}
															%>
													
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
