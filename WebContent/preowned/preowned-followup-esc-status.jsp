<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.preowned.Preowned_Followup_Esc_Status" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@ include file="../Library/css.jsp"%>

</head>

<body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
 <%@include file="../portal/header.jsp" %>

	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
			<div class="page-content-inner">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1><%=mybean.ReturnPreOwnedName(request)%> Follow-up Escalation Status</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="index.jsp"><%=mybean.ReturnPreOwnedName(request)%></a> &gt;</li>
						<li><a href="preowned-followup-esc-status.jsp"><%=mybean.ReturnPreOwnedName(request)%>
								Follow-up Escalation Status</a><b>:</b></li>
					</ul>
					<form name="form1" class="form-horizontal">
						
							<div class="tab-pane" id="">
								<!-- 					BODY START -->
								<center>
									<font color="#ff0000"><b><%=mybean.msg%></b></font>
								</center>
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none"><%=mybean.ReturnPreOwnedName(request)%>
											Follow-up Escalation Status</div>
									</div>

									<div class="portlet-body portlet-empty container-fluid">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<form name="form1" method="post" class="form-horizontal">
											
													<div class="form-element4">
														<label >
															Brands:</label>
															<div id="multiprincipal">
																<select name="dr_principal" size="10"
																	multiple="multiple" class="form-control multiselect-dropdown"
																	style="padding: 10px" id="dr_principal"
																	onChange="PopulateBranches();PopulateRegion();">
																	<%=mybean.mischeck.PopulatePrincipal(mybean.brand_ids, mybean.comp_id, request)%>
																</select>
															</div>
														</div>

													<div class="form-element4">
														<label> Regions: </label>
															<div id="regionHint"> 
															<%=mybean.mischeck.PopulateRegion(mybean.brand_id, mybean.region_ids, mybean.comp_id, request)%>
														</div>
													</div>

													<div class="form-element4">
														<label> Branches<font color="red">*</font>: </label>
															<div id="branchHint"> 
															<%=mybean.mischeck.PopulateBranches(mybean.brand_id, mybean.region_id, mybean.branch_ids, mybean.comp_id, request)%>
															</div>
														</div>
													
													<div class="form-element4">
														<label > Teams:</label> 
														<div id="teamHint"> <%=mybean.mischeck.PopulatePreownedTeams(mybean.branch_id,mybean.team_ids, mybean.comp_id,request)%>
															</div>
														</div>
													</div>
													
													<div class="form-element4">
													<label>
														Pre-Owned Consultant:
														</label>
															<div id="exeHint"> 
															<%=mybean.mischeck.PopulatePreownedExecutives(mybean.branch_id, mybean.team_id, mybean.comp_id)%>
															</div>
														</div>
															<center>
															<div class="form-element12">
																<input name="submit_button" type="submit"
															class="btn btn-success" id="submit_button" value="Go" />
														<input type="hidden" name="submit_button" value="Submit">
														</div>
															</center>
															</div>
															</div>
															
											</form>
										</div>
									</div>
								</div>
								<%if(!mybean.StrHTML.equals("")) {%>
								<%=mybean.StrHTML%>
								<%} %>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>


	<%@ include file="../Library/admin-footer.jsp"%>
<%@ include file="../Library/js.jsp"%>

	<script language="JavaScript" type="text/javascript">
		function FormFocus() { //v1.0
			//document.formcontact.txt_customer_name.focus(); 
		}
		function frmSubmit() {
			document.formemp.submit();
		}
		function PopulateRegion() { //v1.0
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			showHint('../preowned/mis-check.jsp?multiple=yes&brand_id=' + brand_id + '&region=yes', 'regionHint');
		}

		function PopulateBranches() { //v1.0
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			var region_id = outputSelected(document.getElementById("dr_region").options);
			showHint('../preowned/mis-check.jsp?multiple=yes&brand_id=' + brand_id + '&region_id=' + region_id + '&branch=yes', 'branchHint');
		}
		function PopulatePreownedTeams() {
			var branch_id = $('#dr_branch').val();
			showHint('../preowned/mis-check.jsp?branch_id=' + branch_id + '&preownedteam=yes', 'teamHint');
		}

		function PopulateExecutives() {
			var team_id = $('#dr_preownedteam').val();
			showHint('../preowned/mis-check.jsp?team_id=' + team_id + ',&executives=yes', 'exeHint');
		}
		//Dont remove this methods
		function PopulateAdviser(){
			
		}function  PopulateTech(){
			
		}
		function  Populatepsfdays(){
			
		} function PopulateCRMDays(){
			
		}
		///
	</script>
</body>
</html>
