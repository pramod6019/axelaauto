<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.invoice.Report_Top_Invoiced_Products" scope="request" />
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">


<link rel="shortcut icon" href="../test/favicon.ico" />

<link href="../assets/css/font-awesome.css" rel="stylesheet"
	type="text/css" />

<link href="../assets/css/components-rounded.css" rel="stylesheet"
	id="style_components" type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/plugins.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/bootstrap-datepicker3.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/bootstrap-timepicker.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/select2.min.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/select2-bootstrap.min.css" rel="stylesheet"
	type="text/css" />



<LINK REL="STYLESHEET" TYPE="text/css"
	HREF="../assets/css/footable.core.css">

	<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
		rel="stylesheet" type="text/css" />
	<LINK REL="STYLESHEET" TYPE="text/css"
		HREF="../assets/css/footable.core.css">

		<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
		<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
		<script src="../assets/js/bootstrap-datepicker.js"
			type="text/javascript"></script>
		<script src="../assets/js/components-date-time-pickers.js"
			type="text/javascript"></script>

		<script src="../assets/js/select2.full.min.js" type="text/javascript"></script>
		<script src="../assets/js/components-select2.min.js"
			type="text/javascript"></script>

		<script type="text/javascript" src="../Library/dynacheck.js"></script>
		<script type="text/javascript" src="../Library/Validate.js"></script>
		<script type="text/javascript" src="../ckeditor/ckeditor.js"></script>


		<script src="../assets/js/footable.js" type="text/javascript"></script>

		<script type="text/javascript">
		$(function() {
			$('table')
					.footable(
							{
								toggleHTMLElement : '<span><div class="footable-toggle footable-expand" border="0"></div>'
										+ '<div class="footable-toggle footable-contract" border="0"></div></span>'
							});
		});
	</script>
<script language="JavaScript" type="text/javascript">
$(function() {
    $( "#txt_starttime" ).datepicker({
      showButtonPanel: true,
	  dateFormat: "dd/mm/yy"
    });
	
	 $( "#txt_endtime" ).datepicker({  
      showButtonPanel: true,
	  dateFormat: "dd/mm/yy"
    });

  });
  
function PopulateBranches() { //v1.0
var brand_id=outputSelected(document.getElementById("dr_principal").options);
showHint('../sales/mis-check1.jsp?multiple=yes&brand_id='+brand_id+'&branch=yes','branchHint');
}

function PopulateModels() { //v1.0
	var brand_id=outputSelected(document.getElementById("dr_principal").options);
	showHint('../sales/mis-check1.jsp?brand_id='+brand_id+'&model=yes','modelHint');
}
function PopulateVariants() { //v1.0
	var model_id =outputSelected(document.getElementById("dr_model").options);
   showHint('../sales/mis-check1.jsp?model_id='+model_id+'&item=yes','itemHint');
}
function PopulateTeams(){		
	var branch_id=outputSelected(document.getElementById("dr_branch").options);	
   showHint('../sales/mis-check1.jsp?branch_id='+branch_id+'&team=yes', 'teamHint');
}
	
function PopulateExecutives() { //v1.0
	var team_id=outputSelected(document.getElementById("dr_team").options);
	showHint('../sales/mis-check1.jsp?team_id='+team_id+'&executives=yes', 'exeHint');
} 
</script>

<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
<body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
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
						<h1>Report Top Invoiced Products</h1>
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
						<li><a href="../portal/mis.jsp">MIS</a> &gt;</li>
						<li><a href="report-top-invoiced-products.jsp">Top
								Invoiced Products</a><b>:</b></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<center>
								<font color="red"><b><%=mybean.msg%></b></font><br></br>
								<center>
									<div class="portlet box  ">
										<div class="portlet-title" style="text-align: center">
											<div class="caption" style="float: none"></div>
										</div>
										<div class="portlet-body portlet-empty">
											<div class="tab-pane" id="">
												<!-- START PORTLET BODY -->
												<form method="post" class="form-horizontal" name="frm1"
													id="frm1">
													<div class="container-fluid ">
														<div class="form-body col-md-3 col-sm-6">
															<div class="form-group">
																<label class="col-md-3 control-label">Branch:</label>
																<div class="col-md-9">
																	<select name="dr_branch" id="dr_branch"
																		class="bs-select form-control bs-select"
																		onChange="ExeCheck();PopulateTeam();">
																		<%=mybean.PopulateBranch(mybean.branch_id, "all", "", "", request)%>
																	</select>
																</div>
															</div>
														</div>
														<div class="form-body col-md-3 col-sm-6">
															<div class="form-group">
																<label class="col-md-4 control-label">Start
																	Date<font color=red>*</font>:
																</label>
																<div class="col-md-8">
																	<input name="txt_starttime" id="txt_starttime"
																		type="text" class="form-control date-picker"
																		data-date-format="dd/mm/yyyy"
																		value="<%=mybean.starttime %>" size="12"
																		maxlength="10" />

																</div>
															</div>
														</div>

														<div class="form-body col-md-3 col-sm-6">
															<div class="form-group">
																<label class="col-md-4 control-label">End Date<font
																	color=red>*</font>:
																</label>
																<div class="col-md-8">
																	<input name="txt_endtime" id="txt_endtime" type="text"
																		class="form-control date-picker"
																		data-date-format="dd/mm/yyyy"
																		value="<%=mybean.endtime %>" size="12" maxlength="10" />
																</div>
															</div>
														</div>
														<div class="form-body col-md-3 col-sm-6">
															<div class="form-group">
																<label class="col-md-3 control-label"> </label>
																<div class="col-md-9" style="top:-9px">
																	<center>
																		<input name="submit_button" type="submit"
																			class="btn btn-success" id="submit_button" value="Go" />
																		<input type="hidden" name="submit_button"
																			value="Submit">
																	</center>
																</div>
															</div>
														</div>




													</div>

													<div class="container-fluid ">
														<div class="form-body col-md-3 col-sm-6">
															<div class="form-group">
																<label class="col-md-3 control-label"> Brands: </label>
																<div class="col-md-9">
																	<div id="multiprincipal">
																		<select name="dr_principal" size="10"
																			multiple="multiple" class="form-control"
																			id="dr_principal"
																			onChange="PopulateBranches();PopulateModels();">
																			<%=mybean.mischeck.PopulatePrincipal(mybean.brand_ids, mybean.comp_id, request)%>
																		</select>
																	</div>
																</div>
															</div>
														</div>
														<div class="form-body col-md-3 col-sm-6">
															<div class="form-group">
																<label class="col-md-3 control-label"> Branches:
																</label>
																<div class="col-md-9">
																	<span id="branchHint"> <%=mybean.mischeck.PopulateBranches(mybean.brand_id,"" ,mybean.branch_ids, mybean.comp_id, request)%>
																	</span>

																</div>
															</div>
														</div>

														<div class="form-body col-md-3 col-sm-6">
															<div class="form-group">
																<label class="col-md-3 control-label"> Teams: </label>
																<div class="col-md-9">
																	<div id="teamHint">
																		<%=mybean.mischeck.PopulateTeams(mybean.branch_id, mybean.team_ids, mybean.comp_id, request)%>
																	</div>
																</div>
															</div>
														</div>
														<div class="form-body col-md-3 col-sm-6">
															<div class="form-group">
																<label class="col-md-3 control-label">
																	Executive: </label>
																<div class="col-md-9">
																	<span id="exeHint"><%=mybean.mischeck.PopulateSalesExecutives(mybean.team_id, mybean.exe_ids, mybean.comp_id, request)%></span>
																</div>
															</div>
														</div>
													</div>

													<div class="container-fluid ">
														<div class="form-body col-md-3 col-sm-6">
															<div class="form-group">
																<label class="col-md-3 control-label"> Model: </label>
																<div class="col-md-9">
																	<span id="modelHint"> <%=mybean.mischeck.PopulateModels(mybean.brand_id, mybean.model_ids, mybean.comp_id, request)%>
																	</span>
																</div>
															</div>
														</div>
														<div class="form-body col-md-3 col-sm-6">
															<div class="form-group">
																<label class="col-md-3 control-label"> Variants:
																	</td>
																</label>
																<div class="col-md-9">
																	<span id="itemHint"> <%=mybean.mischeck.PopulateVariants(mybean.model_id, mybean.item_ids, mybean.comp_id, request)%>
																	</span>

																</div>
															</div>
														</div>
													</div>
												</form>

											</div>
										</div>
									</div>
									<center><%=mybean.StrHTML%>
									</center>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%@include file="../Library/admin-footer.jsp"%>
	<!-- <script src="../assets/js/jquery.min.js" type="text/javascript"></script>  -->
	<!-- <script src="../assets/js/bootstrap.min.js" type="text/javascript"></script> -->
	<!-- <script type="text/javascript" src="../Library/Validate.js"></script> -->
	<%-- <script type="text/javascript" src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script> --%>
	<!-- <script type="text/javascript" src="../Library/jquery-ui.js"></script> -->
</body>
</HTML>

