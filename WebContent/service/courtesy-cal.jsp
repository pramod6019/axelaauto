<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Courtesy_Cal"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
		<meta name="viewport" content="width=device-width, initial-scale=1">
			<%@include file="../Library/css.jsp"%>
</HEAD>
<body>
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
						<h1>Courtesy Calendar</h1>
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
							<li><a href="../service/index.jsp">Service</a> &gt;</li>
							<li><a href="../service/courtesy.jsp">Courtesy</a>&gt;</li>
							<li><a
								href="../courtesy/courtesy-cal.jsp?<%=mybean.QueryString%>">Courtesy
									Calendar</a><b>:</b></li>
						</ul>
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<form name="formemp" class="form-horizontal " method="post">
								<center>
									<font color="#ff0000"><b><%=mybean.msg%></b></font>
								</center>


								<!-- 	PORTLET customner details-->
								<div class="portlet box">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Courtesy
											Calendar</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="container-fluid">
											<!-- START PORTLET BODY -->
											<!--     start customer details -->
											<center>
												<font size="1">Form fields marked with a red asterisk
													<b><font color="#ff0000">*</font></b> are required.
												</font>
											</center>
											<br />
											<div class="form-element3">
												<label>Branch<font color="#ff0000">*</font>:&nbsp;
												</label>
												<%
													if (mybean.branch_id.equals("0")) {
												%><select name="dr_branch"
													id="dr_branch" class="form-control"
													onChange="CourtesyCheck();showHint('../service/courtesy-check.jsp?multiple=yes&branch_id=' + GetReplace(this.value),'vehHint');">
													<%=mybean.PopulateBranch(mybean.courtesyveh_branch_id,
						"", "1,3", "", request)%>
												</select>
												<%
													} else {
												%>
												<input type="hidden" name="dr_branch" id="dr_branch"
													value="<%=mybean.branch_id%>" />
												<%=mybean.getBranchName(mybean.courtesyveh_branch_id,
						mybean.comp_id)%>
												<%
													}
												%>
											</div>
											<div class="form-element3">
												<label>Start Date<font color=#ff0000><b>
															*</b></font></label> <input name="txt_courtesycar_time_from" type="text"
													class="form-control datepicker"
													id="txt_courtesycar_time_from"
													value="<%=mybean.courtesycar_time_from%>" size="12"
													maxlength="10" />
											</div>
											<div class="form-element3">
												<label>End Date<font color=#ff0000><b> *</b></font></label>
												<input name="txt_courtesycar_time_to" type="text"
													class="form-control datepicker"
													id="txt_courtesycar_time_to"
													value="<%=mybean.courtesycar_time_to%>" size="12"
													maxlength="10" />
											</div>
											<div class="form-element3">
												<input name="submit_button" type="submit"
													class="btn btn-success" id="submit_button" value="Submit" />
												<input name="submit_button" type="hidden" id="submit_button"
													value="Submit" />
											</div>
											<div class="row"></div>
											<div class="form-element3">
												<label>Vehicle:<font color=#ff0000><b> *</b></font></label>
												<span id="vehHint"><%=mybean.PopulateVehicle(mybean.courtesyveh_branch_id)%></span>
											</div>
										</div>
									</div>
								</div>
							</form>
						</div>
					</div>
					<div class="portlet-body">
									<div class="tab-pane" id="">
										<%=mybean.StrHTML%>
									</div>
							</div>
				</div>
			</div>
		</div>
	</div>
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>

	<script type="text/javascript" src="../Library/smart.js"></script>
	<script language="JavaScript" type="text/javascript">
		function CourtesyCheck() { //v1.0
			var branch_id = document.getElementById("dr_branch").value;
		}
	</script>
	<script>
		$(function() {
			$("#txt_courtesycar_time_from").datepicker({
				showButtonPanel : true,
				dateFormat : "dd/mm/yy"
			});

			$("#txt_courtesycar_time_to").datepicker({
				showButtonPanel : true,
				dateFormat : "dd/mm/yy"
			});
		});
	</script>
</body>
</HTML>
