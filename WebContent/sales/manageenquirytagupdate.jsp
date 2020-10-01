<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Manage_Enquiry_Tag_Update" scope="request" />
<% mybean.doGet(request, response); %>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp"%>
<link href="../assets/css/jquery.minicolors.css" rel="stylesheet" type="text/css" />
<!-- <link href="../assets/css/colorpicker.css" rel="stylesheet" type="text/css" /> -->
</head>

<body onload="FormFocus()" class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>

	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1><%=mybean.status%>
							Enquiry tag
						</h1>
					</div>
					<!-- END PAGE TITLE -->

				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="page-content-inner">
					<div class="container-fluid">
						<!-- BEGIN PAGE BREADCRUMBS -->
						<ul class="page-breadcrumb breadcrumb">
							<li><a href="home.jsp">Home</a> &gt;</li>
							<li><a href="manager.jsp">Business Manager</a> &gt;</li>
							<li><a href="manageenquirytaglist.jsp?all=yes">List Enquiry Tags</a> &gt;</li>
							<li><a href="manageenquirytagupdate.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Enquiry tag</a><b>:</b></li>
						</ul>
						<!-- END PAGE BREADCRUMBS -->
						<div class="tab-pane" id="">

							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>

							<div class="container-fluid portlet box ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none"><%=mybean.status%> Enquiry Tag </div>
								</div>
								<div class="container-fluid portlet-body">
									<div class="tab-pane" id="">
										<center>
											<font size="1">Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b> are required. </font>
										</center>
										<br>

										<form method="post" name="form1" class="form-horizontal">

											<div class="form-element6">
												<label>Title<font color=#ff0000>*</font>: </label>
												<input name="txt_tag_name" type="txt_tag_name" class="form-control" value="<%=mybean.tag_name%>" size="50"
													maxlength="255" />
											</div>
											<!-- 											<div class="form-group"> -->
											<!-- 												<label class="control-label col-md-4 col-xs-12 col-sm-2"> -->
											<!-- 													Color<font color="#ff0000">*</font>: -->
											<!-- 												</label> -->
											<!-- 												<div class="col-md-6 col-xs-12 col-sm-10"> -->
											<!-- 													<select name="tag_id_color" -->
											<!-- 														id="tag_id_color" class="form-control"> -->
											<%-- 															<%=mybean.PopulateColor(mybean.tag_id, mybean.comp_id) %> --%>
											<!-- 													</select> -->

											<!-- 												</div> -->
											<!-- 											</div> -->

											<div class="form-element6">
												<label> Color<font color="#ff0000">*</font>: </label>
												<input type="text" class="form-control colorpicker-rgba"
													name="tag_id_color" id="tag_id_color" data-color-format="rgba"
													value="<%=mybean.tag_colour%>"/>
												<!-- 													

<select name="tag_id_color" id="tag_id_color" class="form-control"> -->
												 <!-- 														<option value="0">Select</option>	 -->
												 <!-- 														<option value="danger">Red</option>	 -->
												<!-- 														<option value="info">Purple</option>	 -->
												 <!-- 														<option value="success">Green</option>	 -->
												 <!-- 														<option value="primary">Blue</option> -->
												 <!-- 														<option value="default">Grey</option> -->
												<!-- 		 -->
												<%-- 		 <%=mybean.PopulateColor(mybean.comp_id)%> --%>
												<!-- 													</select> -->


											</div>

											<% if (mybean.status.equals("Update") && !(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) { %>

											<div class="form-element6">
												<label>Entry By:</label>
												<%=mybean.unescapehtml(mybean.entry_by)%>
												<input name="entry_by" type="hidden" id="entry_by"
													value="<%=mybean.entry_by%>" />
											</div>

											<% } %>

											<% if (mybean.status.equals("Update") && !(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) { %>

											<div class="form-element6">
												<label>Entry Date:</label>
												<%=mybean.entry_date%>
												<input type="hidden" name="entry_date"
													value="<%=mybean.entry_date%>" />
											</div>

											<% } %>

											<% if (mybean.status.equals("Update") && !(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) { %>

											<div class="form-element6">
												<label>Modified By:</label>
												<%=mybean.unescapehtml(mybean.modified_by)%>
												<input name="modified_by" type="hidden" id="modified_by"
													value="<%=mybean.modified_by%>" />
											</div>

											<% } %>

											<% if (mybean.status.equals("Update") && !(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) { %>

											<div class="form-element6">
												<label>Modified Date:</label>
												<%=mybean.modified_date%>
												<input type="hidden" name="modified_date"
													value="<%=mybean.modified_date%>" />
											</div>

											<% } %>


											<center>

												<% if (mybean.status.equals("Add")) { %>

												<input name="add_button" type="submit"
													class="btn btn-success" id="add_button" value="Add tag"
													onClick="return SubmitFormOnce(document.form1, this);" />
												<input type="hidden" name="add_button" value="yes">

												<% } else if (mybean.status.equals("Update")) { %>

												<input type="hidden" name="update_button" value="yes" /> 
												<input name="update_button" type="submit" class="btn btn-success"
													id="update_button" value="Update tag"
													onClick="return SubmitFormOnce(document.form1, this);" />
												<input name="delete_button" type="submit"
													class="btn btn-success" id="delete_button"
													OnClick="return confirmdelete(this)" value="Delete tag" />

												<% } %>

											</center>

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
		<%@include file="../Library/js.jsp"%>

	
<!-- 	<script src="../assets/js/bootstrap-colorpicker.js" type="text/javascript"></script> -->
	<script src="../assets/js/jquery.minicolors.min.js" type="text/javascript"></script>
	
	<script language="JavaScript" type="text/javascript">
	function FormFocus() {
		document.form1.txt_tag_name.focus()
	}
	
	$("#tag_id_color").val('#49bd59');
	
</script>
</body>
</html>