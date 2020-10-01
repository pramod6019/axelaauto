<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Report_Coupon_Campaign"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>

<!-- <style>
.table-bordered > thead > tr > th
{
border: 2.5px solid #e7ecf1;
}
.table-bordered > tbody > tr > td
{
border: 2.5px solid #e7ecf1;
}
</style> -->

</HEAD>

<body onload="ToggleDate();" class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>Report Coupon Campaign</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY ----->
			<div class="page-content">
			<div class="page-content-inner">
				<div class="container-fluid">
				
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../portal/mis.jsp">MIS</a> &gt;</li>
						<li><a href="../service/report-coupon-campaign.jsp">Report Coupon Campaign</a><b>:</b></li> 
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center><font color="red"><b><%=mybean.msg%></b></font></center>
					
						<div class="tab-pane" id="">

							<div class="portlet box">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										Report Coupon Campaign
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form method="post" name="frm1" id="frm1" class="form-horizontal">
											
											<div class="form-element6 ">
												<label>Start Date<font color="red">*</font>:</label>
												<input name="txt_starttime" value="<%=mybean.start_time%>"
													id="txt_starttime" class="form-control datepicker" type="text" />
											</div>
												
											<div class="form-element6">
												<label>End Date<font color="red">*</font>:</label>
												<input name="txt_endtime" value="<%=mybean.end_time%>"
													id="txt_endtime" class="form-control datepicker" type="text" />
											</div>
												
											<div class="form-element4">
												<label>Brand<font color="#ff0000">*</font>: </label>
												<div>
													<select name="dr_principal" id="dr_principal" class="form-control multiselect-dropdown" multiple="multiple">
														<%=mybean.PopulateBrand(mybean.comp_id)%>
													</select>
												</div>
											</div>

											<div class="form-element4">
												<!--campaigntype dropdown  -->
												<label> Coupon Campaign Type<font color="#ff0000">*</font>: </label>
												<div>
													<select name="dr_campaign_id" id="dr_campaign_id" class="form-control multiselect-dropdown" multiple="multiple">
														<%=mybean.PopulateType(mybean.comp_id)%>
													</select>
												</div>
											</div>

											<div class="form-element4">
												<!--dept name dropdown  -->
												<label>Department<font color="#ff0000">*</font>: </label>
												<div>
													<select name="dr_dept_id" id="dr_dept_id" class="form-control multiselect-dropdown" multiple="multiple">
														<%=mybean.PopulateDepartment(mybean.comp_id)%>
													</select>
												</div>
											</div>

											<div class="form-element12">
												<center>
													<input type="submit" name="submit_button" id="submit_button" 
														class="btn btn-success" value="Go" />
													<input type="hidden" name="submit_button" value="Submit" />
												</center>
											</div>
										</form>
									</div>
								</div>
							</div>

							<%
								if (!mybean.StrHTML.equals("")) {
							%>
								<!-- START PORTLET BODY -->
								<center><%=mybean.StrHTML%></center>
							<%
								}
							%>
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
		
		function populateCatogery() {
			var department_id = $('#dr_ticket_dept_id').val();
			if (department_id == null) {
				department_id = "";
			}
			showHint('../service/report-check.jsp?ticket_category=ticket_addmulti&ticket_dept_id=' + department_id, 'categoryHint');
		}
	</script>
</body>
</HTML>
