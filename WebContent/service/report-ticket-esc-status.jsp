<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Report_Ticket_Esc_Status"
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
<style>
.multiselect-container > li > a > label.checkbox {
    margin-left: -10px;
}
</style>
<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		//document.formcontact.txt_customer_name.focus(); 
	}
	function frmSubmit() {
		document.formemp.submit();
	}
</script>
</HEAD>

<body class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>Ticket Escalation Status</h1>
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
							<li><a href="../portal/mis.jsp">MIS</a> &gt;</li>
							<li><a href="report-ticket-esc-status.jsp">Ticket
									Escalation Status</a><b>: </b></li>

						</ul>
						<!-- END PAGE BREADCRUMBS -->
						<center>
							<font color="#ff0000"><b><%=mybean.msg%></b></font>
						</center>

						<div class="portlet box  ">
							<div class="portlet-title" style="text-align: center">
								<div class="caption" style="float: none">Ticket Escalation
									Status</div>
							</div>
							<div class="portlet-body portlet-empty container-fluid">
								<div class="tab-pane" id="">

									<form name="formemp" id="formemp" method="post">
										<div class="form-element3">
											<div>Brands:</div>
											<select name="dr_principal" size="10" multiple="multiple"
												class="form-control multiselect-dropdown" id="dr_principal"
												onChange="PopulateBranches();PopulateRegion();">
												<%=mybean.mischeck.PopulatePrincipal(mybean.brand_ids, mybean.comp_id, request)%>
											</select>
										</div>

										<div class="form-element3">
											<div>Regions:</div>
											<span id="regionHint">
												<%=mybean.mischeck.PopulateRegion(mybean.brand_id, mybean.region_ids, mybean.comp_id, request)%>
											</span>
										</div>

										<div class="form-element3">
											<div>Branches:</div>
											<span id="branchHint">
												<%=mybean.mischeck.PopulateBranches(mybean.brand_id, mybean.region_id, mybean.branch_ids, mybean.comp_id, request)%>
											</span>
										</div>

										<div class="form-element3">
											<div>Ticket Owner:</div>
											<span id="exeHint">
												<%=mybean.mischeck.PopulateTicketOwner(mybean.owner_ids, mybean.branch_id, mybean.comp_id)%>
											</span>
										</div>
										
										<div class="form-element3">
											<label>Type: </label>
											<select name="dr_tickettype_id" class='form-control' id="dr_tickettype_id" onchange="PopulateDays();">
												<%=mybean.PopulateType()%>
											</select>
										</div>
		
										<div class="form-element3">
											<label>Days: </label>
											<div  id='daysHint'>
											<%=mybean.PopulateDays(mybean.comp_id, mybean.tickettype_id, mybean.branch_id)%>
											</div>
										</div>

										<div class="row"></div>
										<div class="form-element12">
											<center>
												<input type="submit" name="submit_button" id="submit_button"
													class="btn btn-success" value="Go" />
											</center>
											<input type="hidden" name="submit_button" value="Submit" />
										</div>

									</form>

								</div>
							</div>
						</div>


						<%
							if (!mybean.StrHTML.equals("")) {
						%>
						<div class="portlet box  ">
							<div class="portlet-title" style="text-align: center">
								<div class="caption" style="float: none">Ticket Escalation
									Status</div>
							</div>
							<div class="portlet-body portlet-empty container-fluid">

								<div class="page-content-inner">
									<div class="tab-pane" id="">
										<!-- 					BODY START -->

										<center>
											<div>
												<%=mybean.StrHTML%></div>
										</center>
									</div>
								</div>


							</div>
						</div>
						<%
							}
						%>

					</div>
				</div>
			</div>
		</div>
	</div>

	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
	<!-- 	FOOTABLE START -->
	<script type="text/javascript">
		function PopulateRegion() {
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			showHint('../service/mis-check1.jsp?region=yes&brand_id=' + brand_id, 'regionHint');
		}

		function PopulateBranches() {
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			var region_id = outputSelected(document.getElementById("dr_region").options);
			showHint('../service/mis-check1.jsp?branch=yes&brand_id=' + brand_id + '&region_id=' + region_id, 'branchHint');
		}
		
		function PopulateAdviser() {
			var branch_id = outputSelected(document.getElementById("dr_branch_id").options);
			showHint('../service/mis-check1.jsp?ticketOwner=yes&exe_branch_id=' + branch_id , 'exeHint');
		}

		function PopulateBranch() {
		}
		function PopulateZone() {
		}
		function PopulateTech() {
		}
		function Populatepsfdays() {
		}
		function PopulateModels() {
		}
		function PopulateCRMDays() {
		}
		function PopulateExecutives() {
		}
		
		function PopulateDays() {
			var brand_id=$("#dr_principal").val();
			var tickettype_id=$("#dr_tickettype_id").val();
			showHint('../service/mis-check1.jsp?ticketdays=yes&tickettype_id='+tickettype_id+'&ticket_brand_id='+brand_id, 'daysHint');
		    }
		
	</script>

	<!-- 	FOOTABLE END -->
</body>
</html>
