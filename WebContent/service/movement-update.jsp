<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Movement_Update"
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
<body onLoad="FormFocus()"
	class="page-container-bg-solid page-header-menu-fixed">

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
						<h1><%=mybean.status%> Vehicle Movement </h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
				<div class="page-content-inner">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../portal/manager.jsp">Business Manager</a> &gt;</li>
						<li><a href="movement-list.jsp?all=yes">List Vehicle
								Movements</a>&gt;</li>
						<li><a href="movement-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Vehicle
								Movement:</a></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="#ff0000"><b><%=mybean.msg%></b></font>
					</center>
					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<center><%=mybean.status%>Vehicle Movement
										</center>
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<form name="form1" method="post" class="form-horizontal">
											<center>
												<font size="">Form fields marked with a red asterisk
													<font color="red">*</font> are required.
												</font>
											</center>
											<div class="form-element6">
												<label >Branch<b><font color="#ff0000">*</font></b>: &nbsp; </label>
													<select name="dr_vehmove_branch_id" class="form-control"
														id="dr_vehmove_branch_id">
														<%=mybean.PopulateBranch()%>
													</select>
											</div>
											<div class="form-element6">
												<label >JC Type<b><font color="#ff0000">*</font></b>:&nbsp; </label>
													<select name="dr_vehmove_jctype_id" class="form-control"
														id="dr_vehmove_jctype_id">
														<%=mybean.PopulateJCType()%>
													</select>
											</div>
											<div class="form-element6">
												<label >Reg. No.<font color="#ff0000">*</font><b></b>:&nbsp; </label>
													<input name="txt_vehmove_reg_no" type="text"
														class="form-control" id="txt_vehmove_reg_no"
														value="<%=mybean.vehmove_reg_no%>" size="20"
														maxlength="20">
											</div>
											<div class="form-element6">
												<label >Time In<b><font color="#ff0000">*</font></b>:&nbsp; </label>
														<input type="text" size="16" name="txt_vehmove_timein"
															id="txt_vehmove_timein"
															value="<%=mybean.vehmove_timein%>" class="form-control datetimepicker">
											</div>
											<div class="form-element6">
												<label >Time Out:&nbsp;</label>
														<input type="text" size="16" name="txt_vehmove_timeout"
															id="txt_vehmove_timeout"
															value="<%=mybean.vehmove_timeout%>" class="form-control datetimepicker">
											</div>
											<div class="row"></div>
											<%
												if (mybean.status.equals("Update") && !(mybean.timein_entry_by == null) && !(mybean.timein_entry_by.equals(""))) {
											%>
											<div class="form-element6">
												<label >Time In Entry By: &nbsp;</label>
													<%=mybean.unescapehtml(mybean.timein_entry_by)%>
													<input name="timein_entry_by" type="hidden"
														id="timein_entry_by"
														value="<%=mybean.unescapehtml(mybean.timein_entry_by)%>">
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.timeout_entry_by == null) && !(mybean.timeout_entry_by.equals(""))) {
											%>
											<div class="form-element6">
												<label >Time Out Entry By: &nbsp;</label>
													<%=mybean.unescapehtml(mybean.timeout_entry_by)%>
													<input name="timeout_entry_by" type="hidden"
														id="timeout_entry_by"
														value="<%=mybean.unescapehtml(mybean.timeout_entry_by)%>">
											</div>
											<%
												}
											%>
											<div class="row"></div>
											<%
												if (mybean.status.equals("Update") && !(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) {
											%>
											<div class="form-element6">
												<label>Modified By: &nbsp;</label>
													<%=mybean.unescapehtml(mybean.modified_by)%>
													<input name="modified_by" type="hidden" id="modified_by"
														value="<%=mybean.unescapehtml(mybean.modified_by)%>">
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) {
											%>
											<div class="form-element6">
												<label >Modified Date: &nbsp;</label>
													<%=mybean.modified_date%>
													<input type="hidden" name="modified_date"
														value="<%=mybean.modified_date%>">
											</div>
											<%
												}
											%>

											<center>
												<%
													if (mybean.status.equals("Update")) {
												%>
												<input type="hidden" name="update_button" value="yes">
												<input name="button" type="submit" class="btn btn-success"
													id="button" value="Update Vehicle Movement"
													onclick="return SubmitFormOnce(document.form1,this);" /> <input
													name="delete_button" type="submit" class="btn btn-success"
													id="delete_button" OnClick="return confirmdelete(this)"
													value="Delete Vehicle Movement" />
												<%
													}
												%>
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
	</div>
	<!-- END CONTAINER -->

	<%@include file="../Library/admin-footer.jsp"%>
<%@include file="../Library/js.jsp"%>
<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0  
		document.form1.txt_location_name.focus()
	}
</script>
 <!-- <script language="JavaScript" type="text/javascript">
	$(function() {
		$("#txt_vehmove_timein").datetimepicker({
			dateFormat : 'dd/mm/yy',
			stepMinute : 5
		});
		$("#txt_vehmove_timeout").datetimepicker({
			dateFormat : 'dd/mm/yy',
			stepMinute : 5
		});
	});
</script> -->
</body>
</HTML>
