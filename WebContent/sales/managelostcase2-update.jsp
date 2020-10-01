<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.ManageLostCase2_Update"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp"%>
</HEAD>
<body onLoad="FormFocus()" class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>
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
						<h1><%=mybean.status%>&nbsp;Lost Case 2
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
						<ul class="page-breadcrumb breadcrumb">
							<li><a href="../portal/home.jsp">Home</a> &gt;</li>
							<li><a href="../portal/manager.jsp">Business Manager</a> &gt;</li>
							<li><a href="managelostcase1.jsp?all=yes">List Lost Case 1</a> &gt;</li>
							<li><a href="managelostcase2-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Lost Case 2</a><b>:</b></li>
						</ul>
						<!-- END PAGE BREADCRUMBS -->
						<center>
							<font color="#ff0000"><b><%=mybean.msg%></b></font>
						</center>

						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%>&nbsp;Lost Case 2
									</div>
								</div>
								
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<center>
											<font size="1">Form fields marked with a red asterisk
												<b><font color="#ff0000">*</font></b> are required.
											</font>
										</center>
										
										<form name="form1" method="post" class="form-horizontal">

											<div class="form-element6">
												<label> Name<font color=red>*</font>: </label>
												<input name="txt_lostcase2_name" type="text"
												class="form-control" id="txt_lostcase2_name"
												value="<%=mybean.lostcase2_name%>" size="50"
												maxlength="255" />
											</div>

											<div class="form-element6">
												<label> Lost Case1<font color=red>*</font>: </label>
												<select name="dr_lostcase2_lostcase1_id"
												class="form-control" id="dr_lostcase2_lostcase1_id">
												<%=mybean.PopulateLostCase1()%>
												</select>
											</div>

											<%
												if (mybean.status.equals("Update") && !(mybean.lostcase2_entry_date == null) && !(mybean.lostcase2_entry_date.equals(""))) {
											%>
											
											<div class="form-element6">
												<label> Entry By:</label>
												<%=mybean.unescapehtml(mybean.lostcase2_entry_by)%>
												<input name="entry_by" type="hidden" id="entry_by"
												value="<%=mybean.lostcase2_entry_by%>" />
											</div>
											
											<div class="form-element6">
												<label> Entry Date:</label>
												<%=mybean.lostcase2_entry_date%>
												<input type="hidden" name="entry_date"
												value="<%=mybean.lostcase2_entry_date%>" />
											</div>
											
											<%
												}
											%>
											
											<%
												if (mybean.status.equals("Update") && !(mybean.lostcase2_modified_date == null) && !(mybean.lostcase2_modified_date.equals(""))) {
											%>
											
											<div class="form-element6">
												<label> Modified By:</label>
												<%=mybean.unescapehtml(mybean.lostcase2_modified_by)%>
												<input name="modified_by" type="hidden" id="modified_by"
												value="<%=mybean.lostcase2_modified_by%>" />
											</div>
											
											<div class="form-element6">
												<label> Modified Date:</label>
												<%=mybean.lostcase2_modified_date%>
												<input type="hidden" name="modified_date"
												value="<%=mybean.lostcase2_modified_date%>" />
											</div>
											
											<%
												}
											%>
											
											<center>
												<%
													if (mybean.status.equals("Add")) {
												%>
												<input name="addbutton" type="submit"
												class="btn btn-success" id="addbutton"
												value="Add Lost Case 2"
												onClick="return SubmitFormOnce(document.form1, this);" />
												<input type="hidden" name="add_button" id="add_button"
													value="yes">
												<%
													} else if (mybean.status.equals("Update")) {
												%>
												<input type="hidden" name="update_button" id="update_button"
												value="yes"> <input name="updatebutton"
												type="submit" class="btn btn-success" id="updatebutton"
												value="Update Lost Case 2"
												onClick="return SubmitFormOnce(document.form1, this);" />
												<input name="delete_button" type="submit"
												class="btn btn-success" id="delete_button"
												OnClick="return confirmdelete(this)"
												value="Delete Lost Case 2" />
												<%}%>
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
	<!-- END CONTAINER -->
	<%@include file="../Library/admin-footer.jsp"%>

	<%@include file="../Library/js.jsp"%>

	<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		document.form1.txt_lostcase2_name.focus()
	}
</script>
</body>
</HTML>
