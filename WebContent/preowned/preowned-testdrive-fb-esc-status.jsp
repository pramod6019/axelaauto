<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.preowned.Preowned_TestDrive_FB_Esc_Status" scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />

<%@include file="../Library/css.jsp"%>
</head>

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
						<h1><%=mybean.ReturnPreOwnedName(request)%>
							Test Drive Feedback Escalation Status
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
						<li><a href="../portal/mis.jsp">MIS</a> &gt;</li>
						<li><a href="preowned-testdrive-fb-esc-status.jsp"><%=mybean.ReturnPreOwnedName(request)%>
								Test Drive Feedback Escalation Status</a>:</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="red"><b> <%=mybean.msg%></b></font>
					</center>
					
						<div class="tab-pane" id="">

							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none"><%=mybean.ReturnPreOwnedName(request)%>
										Test Drive Feedback Escalation Status
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form method="post" name="formemp" id="formemp"
											class="form-horizontal">
											
											<div class="container-fluid">
													<div class="form-element3">
														<div> Brands:</div>
																<select name="dr_principal" size="10"
																	multiple="multiple" class="form-control multiselect-dropdown"
																	id="dr_principal"
																	onChange="PopulateBranches();PopulateRegion();">
																	<%=mybean.mischeck.PopulatePrincipal(mybean.brand_ids, mybean.comp_id, request)%>
																</select>
													</div>

													<div class="form-element3">
														<div>Regions: </div>
															<span id="regionHint"> 
															<%=mybean.mischeck.PopulateRegion(mybean.brand_id, mybean.region_ids, mybean.comp_id, request)%>
															</span>
													</div>

													<div class="form-element3">
														<div> Branches: </div>
															<span id="branchHint"> 
															<%=mybean.mischeck.PopulateBranches(mybean.brand_id, mybean.region_id, mybean.branch_ids, mybean.comp_id, request)%>
															</span>
													</div>
													
													<div class="form-element3">
														<div>Teams: </div>
														<span id="teamHint"> <%=mybean.mischeck.PopulatePreownedTeams(mybean.branch_id,mybean.team_ids, mybean.comp_id,request)%>
															</span>
													</div>
													
													<div class="form-element3">
														<div>Pre-Owned Consultant:</div>
															<div id="exeHint"> 
															<%=mybean.mischeck.PopulatePreownedExecutives(mybean.branch_id, mybean.team_id, mybean.comp_id)%>
															</div>
													</div>
											</div>
											
											<div class="container-fluid">
													<div class="form-element12">
															<center>
																<input type="submit" name="submit_button"
															id="submit_button" class="btn btn-success" value="Go">
															<input type="hidden" name="submit_button" value="Submit">
															</center>
													</div>
											</div>
										</form>
										<div>
											<%=mybean.StrHTML%>
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
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>

	<script language="JavaScript" type="text/javascript">
		function FormFocus() { //v1.0
			//document.formcontact.txt_customer_name.focus(); 
		}
		function frmSubmit() {
			document.formemp.submit();
		}
		function PopulateRegion() { //v1.0
			var brand_id = outputSelected(document
					.getElementById("dr_principal").options);
			//alert("111111------" + brand_id);
			showHint('../preowned/mis-check.jsp?multiple=yes&brand_id='
					+ brand_id + '&region=yes', 'regionHint');
		}

		function PopulateBranches() { //v1.0
			var brand_id = outputSelected(document
					.getElementById("dr_principal").options);
			var region_id = outputSelected(document.getElementById("dr_region").options);
			//alert("brand_id------------" + brand_id);
			//alert("region_id------------" + region_id);
			showHint('../preowned/mis-check.jsp?multiple=yes&brand_id='
					+ brand_id + '&region_id=' + region_id + '&branch=yes',
					'branchHint');
		}
		function PopulatePreownedTeams() {
			var branch_id = $('#dr_branch').val();
			showHint('../preowned/mis-check.jsp?branch_id=' + branch_id
					+ '&preownedteam=yes', 'teamHint');
		}

	
		function PopulateExecutives() { //v1.0
			var team_id = $('#dr_preownedteam').val();
			showHint('../preowned/mis-check.jsp?team_id=' + team_id
					+ '&executives=yes', 'exeHint');
		}
		
		function PopulateAdviser(){
		}
		function PopulateTech(){
		}
		function Populatepsfdays(){
		}
		function PopulateCRMDays(){
		}
		function PopulateExecutives() {
		}
	</script>
</body>