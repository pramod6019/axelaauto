<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.sales.Report_CRM_Details" scope="request"/>
<jsp:useBean id="export" class="axela.sales.Report_CRM_Details" scope="request"/>
<%export.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD> 
<title><%=export.AppName%> - <%=export.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<%@include file="../Library/css.jsp"%> 

</HEAD>		
		
<body class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>CRM Follow-up Details</h1>
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
						<li><a href="../portal/mis.jsp">MIS</a> &gt;</li>
						<li><a href="report-crm-details.jsp">CRM Follow-up Details</a><b>:</b></li> 
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="red"><b> <%=export.msg%></b></font>
					</center>
						<div class="tab-pane" id="">

							<div class="portlet box ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">CRM Follow-up Details</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form method="post" name="frm1" id="frm1" class="form-horizontal">

											<div class="form-element6">
												<label>From Date:&nbsp;</label> <input name="txt_starttime"
													id="txt_starttime" value="<%=export.from_date%>"
													class="form-control datepicker" type="text" value="" />
											</div>

											<div class="form-element6">
												<label>To Date:&nbsp;</label> <input name="txt_endtime"
													id="txt_endtime" value="<%=export.to_date%>"
													class="form-control datepicker" type="text" value="" />
											</div>

											<div class="form-element4">
													<label >Branch<font color="red">*</font>:&nbsp; </label>
													<div> 
														<%--<%if(export.branch_id.equals("0")){%> --%>
														<select name="dr_branch" id="dr_branch" class="form-control" onchange="PopulateCRMDays();">
															<%=export.PopulateBranch(export.dr_branch_id, "", "1,2", "", request)%>
														</select>
														<%--<%}else{%> --%>
														<!--<input type="hidden" name="dr_branch" id="dr_branch" -->
														<%--value="<%=export.branch_id%>"/> --%>
														<%--<%=export.getBranchName(export.dr_branch_id, export.comp_id)%> --%>
														<%--<%}%> --%>
													</div>
												</div>
												
												<div class="form-element4">
													<label >Type<font color="red">*</font>:&nbsp; </label>
													<div >
														<select name="dr_crmdays_crmtype_id" class="form-control" id="dr_crmdays_crmtype_id" onchange="PopulateCRMDays();">
															<%=export.PopulateCRMType()%>
														</select> 
													</div>
												</div>
												
												<div class="form-element4">
													<label >Follow-up Days<font color="red">*</font>:&nbsp; </label>
													<div>
														<span id="crmdaysHint">
															<% export.rep.crmdays_id = export.crmfollowupdays_id; %>
															<%=export.rep.PopulateCRMDays(export.comp_id, export.crmdays_crmtype_id, export.crmfollowupdays_id, export.dr_branch_id, "")%>
														</span>
														<input type="text" id="txt_dr_crmdays_id" name="txt_dr_crmdays_id" hidden>
													</div>
												</div>

												<div class="form-element12" align="center">
													<input name="btn_export" id="btn_export" type="submit" class="btn btn-success" value="Export" />
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
	
	<%@include file="../Library/js.jsp"%>

	<script type="text/javascript">
		function coming() {
			var temp = $("#dr_crmdays_id").val();
			// alert("temp-----------"+temp);
			document.getElementById('txt_dr_crmdays_id').value = temp;
		}
		
		function PopulateCRMDays() {
			var branch_id = $("#dr_branch").val();
			var crmtype_id = document.getElementById("dr_crmdays_crmtype_id").value;
			showHint( '../sales/mis-check1.jsp?crmdaysfollowupsingle=yes&esc=yes&crmtype_id=' + crmtype_id + '&branch_id=' + branch_id, 'crmdaysHint');
		}
		///
	</script>

</body>
</html>
