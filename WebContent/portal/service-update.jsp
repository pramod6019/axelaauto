<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Service_Update"
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
<link href="../assets/css/bootstrap-wysihtml5.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/summernote.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />

<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		document.form1.txt_course_name.focus()
	}
</script>

<link href="../assets/css/style.css" rel="stylesheet" type="text/css" />
</HEAD>

<body onLoad="FormFocus()"
	class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="header.jsp"%>

	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>Service</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="home.jsp">Home</a> &gt;</li>
						<li><a href="service.jsp">Services</a> &gt;</li>
						<li><a href="service-list.jsp?all=yes">List Services</a> &gt;</li>
						<li><a href="#"><%=mybean.status%>&nbsp;Service</a>:</li>
					</ul>
					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="container-fluid portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none"><%=mybean.status%>&nbsp;Service
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
												<label class="control-label col-md-4">Service <font
													color=red>*</font>:
												</label>
												<div class="col-md-6 col-xs-12">
													<input class="form-control" type="text"
														name="txt_course_name" id="txt_course_name"
														value="<%=mybean.course_name%>" size="50" maxlength="255">
												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4">Code <font
													color=red>*</font>:
												</label>
												<div class="col-md-6 col-xs-12">
													<input class="form-control" type="text"
														name="txt_course_code" id="txt_course_code"
														value="<%=mybean.course_code%>" size="50" maxlength="255">
												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4">Description <font
													color=red>*</font>:
												</label>
												<div class="col-md-6 col-xs-12">
													<textarea class="form-control summernote_1" type="text"
														cols="50" rows="5" name="txt_course_desc"
														id="txt_course_desc"><%=mybean.course_desc%></textarea>
													<script type="text/javascript">
														var editor = CKEDITOR
																.replace(
																		'txt_course_desc',
																		{
																			toolbar : 'MyToolbar',
																			//customConfig: '',
																			width : '95%'
																		});
													</script>
												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4">Active: </label>
												<div class="col-md-6 col-xs-12">
													<input class="" type="checkbox" name="ch_course_active"
														id="ch_course_active"
														value="<%=mybean.PopulateCheck(mybean.course_active)%>">
												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4">Variable: </label>
												<div class="col-md-6 col-xs-12">
													<input class="" type="checkbox" name="ch_course_variable"
														id="ch_course_variable"
														value="<%=mybean.PopulateCheck(mybean.course_variable)%>">
												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4">Notes: </label>
												<div class="col-md-6 col-xs-12">
													<textarea class="form-control" type="text" cols="100"
														rows="4" name="txt_course_notes" id="txt_course_notes"><%=mybean.course_notes%></textarea>
												</div>
											</div>
											<%
												if (mybean.status.equals("Update")
														&& !(mybean.course_entry_by == null)
														&& !(mybean.course_entry_by.equals(""))) {
											%>
											<div class="form-group">
												<label class="control-label col-md-4">Entry By: </label>
												<div class="col-md-6 col-xs-12">
													<%=mybean.course_entry_by%>
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
												<label class="control-label col-md-4">Entry Date: </label>
												<div class="col-md-6 col-xs-12">
													<%=mybean.entry_date%>
												</div>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update")
														&& !(mybean.course_modified_by == null)
														&& !(mybean.course_modified_by.equals(""))) {
											%>
											<div class="form-group">
												<label class="control-label col-md-4">Modified By: </label>
												<div class="col-md-6 col-xs-12">
													<%=mybean.course_modified_by%>
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
												<label class="control-label col-md-4">Modified Date:
												</label>
												<div class="col-md-6 col-xs-12">
													<%=mybean.modified_date%>
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
													<input name="button2" type="submit" class="btn btn-success"
														id="button2" value="Add Service" /> <input type="hidden"
														name="add_button" value="Add Service">
													<%
														} else if (mybean.status.equals("Update")) {
													%>
													<input type="hidden" name="update_button"
														value="Update Service"> <input name="button"
														type="submit" class="btn btn-success" id="button"
														value="Update Service" /> <input name="delete_button"
														type="submit" class="btn btn-success" id="delete_button"
														onClick="return confirmdelete(this)"
														value="Delete Service" />
													<%
														}
													%>
													<input type="hidden" name="course_entry_by"
														value="<%=mybean.course_entry_by%>"> <input
														type="hidden" name="entry_date"
														value="<%=mybean.entry_date%>"> <input
														type="hidden" name="course_modified_by"
														value="<%=mybean.course_modified_by%>"> <input
														type="hidden" name="modified_date"
														value="<%=mybean.modified_date%>">
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

	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript"
		src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
	<script src="../assets/js/summernote.min.js" type="text/javascript"></script>
	<script src="../assets/js/components-editors.min.js"
		type="text/javascript"></script>
</body>
</HTML>
