<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Report_Ticket_Followup" scope="request"/>
<jsp:useBean id="export" class="axela.customer.Customer_Export" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp"%>
<script language="JavaScript" type="text/javascript">
	function PopulateRegion() {
		var brand_id = outputSelected(document.getElementById("dr_principal").options);
		showHint('../accessories/mis-check.jsp?multiplecheckregion=yes&brand_id=' + brand_id , 'regionHint');
	}

	function PopulateBranches() {
		var brand_id = outputSelected(document.getElementById("dr_principal").options);
		var region_id = outputSelected(document.getElementById("dr_region").options);
		showHint('../accessories/mis-check.jsp?multiplecheckbranch=yes&brand_id=' + brand_id + '&region_id=' + region_id , 'branchHint');
	}
</script>

</head>
<body  class="page-container-bg-solid page-header-menu-fixed">
<%@include file="../portal/header.jsp" %>
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
						<h1>Report Ticket Follow-up</h1>
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
						<li><a href="index.jsp">Service</a> &gt;</li>
						<li><a href="ticket.jsp">Ticket</a> &gt;</li>
						<li><a href="report-ticket-followup.jsp">Report Ticket Follow-up</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					
					<div class="page-content-inner">
						
								<center>
									<font color="#ff0000"><b><%=mybean.msg%></b></font>
								</center>
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Report Ticket Follow-up </div>
									</div>
									<div class="portlet-body portlet-empty container-fluid">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<form method="post" name="frm1" id="frm1"
												class="form-horizontal">
												
												<div class="form-element4">
														<label>Start Date<font color="red">*</font>: </label>
															<input name="txt_starttime" id="txt_starttime"
																	value="<%=mybean.start_time%>"
																	class="form-control datepicker"
																	type="text" maxlength="10" />
												</div>
												
												
												<div class="form-element4">	
														<label>End Date<font color="red">*</font>: </label>
															<input name="txt_endtime" id="txt_endtime"
																	value="<%=mybean.end_time%>"
																	class="form-control datepicker"
																	type="text" maxlength="10" />
													
												</div>
												
												<div class="form-element4">
														<label>Type: </label>
															<select id="dr_type" name="dr_type" class="form-control">
															<%=mybean.PopulateType() %>
															</select>
												</div>
												
														<div class="form-element3">
															<div> Brands:</div>
																	<select name="dr_principal" size="10"
																		multiple="multiple" class="form-control multiselect-dropdown"
																		id="dr_principal" onChange="PopulateBranches();PopulateRegion();">
																		<%=mybean.mischeck.PopulatePrincipal(mybean.brand_ids,
																				mybean.comp_id, request)%>
																	</select>
														</div>
	
														<div class="form-element3">
															<div> Regions:</div> 
															<span id="regionHint"> 
																<%=mybean.mischeck.PopulateRegion(mybean.brand_id,
																		mybean.region_ids, mybean.comp_id, request)%>
																</span>
														</div>
	
														<div class="form-element3">
															<div> Branches:</div> 
															<span id="branchHint">
																<%=mybean.mischeck.PopulateBranches(mybean.brand_id,
																		mybean.region_id, mybean.branch_ids, mybean.comp_id,request)%>
																</span>
														</div>
														
														<div class="form-element3">
															<div> Ticket Owner: </div>
															<span id="exeHint"> 
																	<%=mybean.PopulateTicketOwner(mybean.owner_ids, mybean.comp_id, request)%>
																	</span>
														</div>
														
														<div class="form-element12">
														<center>
															<input type="submit" name="submit_button"
																id="submit_button" class="btn btn-success" style="margin-top:3px;" value="Go" />
																</center>
															<input type="hidden" name="submit_button" value="Submit" />
												</div>
	
											</form>
										</div>
									</div>
								</div></div></div>
								
								<%
									if (!mybean.StrHTML.equals("")) {
								%>
								<center><%=mybean.StrHTML%></center>
								<%
									}
								%>
					
					</div>
				</div>
			</div>
		</div>

	</div>
	<!-- END CONTAINER -->
 <%@include file="../Library/admin-footer.jsp" %>
<%@include file="../Library/js.jsp"%>
</body>
</html>
