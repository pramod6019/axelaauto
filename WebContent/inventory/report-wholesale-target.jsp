<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.inventory.Report_Wholesale_Target"
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
						<h1>Wholesale Target</h1>
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
						<li><a href="../inventory/report-wholesale-target.jsp">Wholesale
								Target</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<!-- 					BODY START -->

							<center>
								<font color="red"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Wholesale Target</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="container-fluid">
										<form method="post" name="frm1" id="frm1"
											class="form-horizontal">
											<!-- START PORTLET BODY -->
											<div class="form-element6">
												<label>Start Date<font color=#ff0000><b> *</b></font>: </label>
												<input name="txt_starttime" id="txt_starttime"
													value="<%=mybean.start_time%>"
													class="form-control datepicker" type="text" maxlength="10" />
											</div>
											<div class="form-element6">
												<label>End Date<font color=#ff0000><b> *</b></font>: </label>
												<input name="txt_endtime" id="txt_endtime"
													value="<%=mybean.end_time%>"
													class="form-control datepicker" type="text" maxlength="10" />
											</div>
											<div class="form-element2"  id="multiprincipal">
												<label> Brands:</label>
												<div>	<select name="dr_principal" size="10" multiple="multiple"
														class="form-control multiselect-dropdown" id="dr_principal"
														style="padding: 10px"
														onChange="PopulateBranches();PopulateModels();PopulateRegion();">
														<%=mybean.mischeck.PopulatePrincipal(mybean.brand_ids, mybean.comp_id, request)%>
													</select></div>
											</div>

											<div class="form-element2">
												<label>Regions:</label> <div id="regionHint"> <%=mybean.mischeck.PopulateRegion(mybean.brand_id, mybean.region_ids, mybean.comp_id, request)%>
												</div>
											</div>

											<div class="form-element2">
												<label>Branches:</label> <div id="branchHint"> <%=mybean.mischeck.PopulateBranches(mybean.brand_id, mybean.region_id, mybean.branch_ids, mybean.comp_id, request)%>
												</div>
											</div>
											<div class="form-element2">
												<label> Model: </label><div id="modelHint"> <%=mybean.mischeck.PopulateModels(mybean.brand_id, mybean.model_ids, mybean.comp_id, request)%>
												</div>
											</div>
											<div class="form-element2">
												<label>Variant:</label> <div id="itemHint"> <%=mybean.mischeck.PopulateVariants(mybean.model_id, mybean.item_ids, mybean.comp_id, request)%>
												</div>
											</div>
											<div class="form-element2">
												<label> Fuel Type:</label>
												<div><%=mybean.PopulateFuelType(mybean.comp_id, request)%></div>
											</div>
												<div>
												<center>
													<input type="submit" name="submit_button"
														id="submit_button" class="btn btn-success" value="Go">
													<input type="hidden" name="submit_button" value="Submit">
													<!-- 															   <input type="submit" name="submit_button" id="submit_button" class="button" value="Go"> -->
													<!--                   <input type="hidden" name="submit_button" value="Submit"> -->
												</center>
											</div>
									</div>
								</div>
								</form>
							</div>
						</div>
					</div>
				</div>
				<div class="container-fluid">
					<center><%=mybean.WholesaleHTML%></center>
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
		function Subjectlist(){
                    document.form1.submit();
                }
		function PopulateRegion() { //v1.0
			var principal_id =outputSelected(document.getElementById("dr_principal").options);
			 //alert("111111------"+principal_id);
			showHint('../sales/mis-check1.jsp?multiple=yes&brand_id='+principal_id+'&region=yes','regionHint');
		    } 
		
		function PopulateBranches() { //v1.0
			var principal_id=outputSelected(document.getElementById("dr_principal").options);
			var region_id=outputSelected(document.getElementById("dr_region").options);
			 //alert("222------"+region_id);
			showHint('../sales/mis-check1.jsp?multiple=yes&brand_id='+principal_id+ '&region_id=' + region_id + '&branch=yes','branchHint');
		    }
		
			function PopulateModels() { //v1.0
				
				var principal_id=outputSelected(document.getElementById("dr_principal").options);
				 //salert("333------"+principal_id);
				showHint('../sales/mis-check1.jsp?brand_id='+principal_id+'&model=yes','modelHint');
		    }
			
			function PopulateVariants(){
				var model_id=outputSelected(document.getElementById("dr_model").options);
				 //salert("333------"+principal_id);
				showHint('../sales/mis-check1.jsp?model_id='+model_id+'&item=yes','itemHint');
			}
        </script>
</body>
</HTML>
