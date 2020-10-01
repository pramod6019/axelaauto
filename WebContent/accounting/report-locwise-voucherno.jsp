<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.accounting.Report_LocWise_VoucherNo" scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp"%>
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
						<h1>Report Location Wise</h1>
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
						<li><a href=../accounting/index.jsp>Accounting</a> &gt;</li>
						<li><a href="report-locwise-voucherno.jsp">Location Wise
								Current Running No.</a>:</li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<center>
								<font color="red"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Location Wise
										Current Running No</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="container-fluid">
										<!-- START PORTLET BODY -->
										<form method="post" name="frm1" class="form-horizontal"
											id="frm1">
											
													<div class="form-element3">
														<label>Brand:</label>
															<div id="multiprincipal">
																		<select name="dr_principal" size="10"
																			multiple="multiple" class="form-control service_element hidden"
																			id="dr_principal"
																			onChange="PopulateBranches();"> 
																			<%=mybean.mischeck.PopulatePrincipal(mybean.brand_ids, mybean.comp_id, request)%>
																		</select>
																	</div>
													</div>
												
												<div class="form-element3">
														<label>Branch:</label>
 															<div id="branchHint"> 
															<%=mybean.acccheck.PopulateBranchesld(mybean.filter_brand_id, mybean.filter_region_id, mybean.jc_branch_ids, mybean.comp_id, request)%>
															</div> 
												</div>
											
<center>
														<input name="submit_button" type="submit"
															class="btn btn-success" id="submit_button" value="Go" />
														<input type="hidden" name="submit_button" value="Submit" />
														<center>

										</form>

									</div>
								</div>
							</div>
							<%=mybean.StrHTML%>

						</div>
					</div>
				</div>
			</div>
		</div>

	</div>

	<%@include file="../Library/admin-footer.jsp"%>
<%@include file="../Library/js.jsp"%>
	<script type="text/javascript" src="../assets/js/bootstrap-multiselect.js" ></script>
	<script>
		$(function() {
			$('#dr_principal').multiselect({
	       			 enableClickableOptGroups: true,
	       			 includeSelectAllOption: true,
	   		});
		});
		$(function() {
			$('#dr_branch').multiselect({
	       			 enableClickableOptGroups: true,
	       			 includeSelectAllOption: true,
	   		});
		});
	</script>
	<script>
	function PopulateBranches() {
		var brand_id = outputSelected(document.getElementById("dr_principal").options);
		showHintMultySelect('../accounting/acc-check.jsp?multiplecheck=yes&brand_id=' + brand_id + '&mulbranch=yes', 'branchHint');
	}
	function showHintMultySelect(url, Hint) {
		$('#'+Hint).html('<div id=loading align=center><img align=center src=\"../admin-ifx/loading.gif\" /></div>');
			$.ajax({
				url: url,
				type: 'GET',
				success: function (data){
					if(data.trim() != 'SignIn'){
						$('#'+Hint).fadeIn(500).html('' + data.trim() + '');
						convertmultiselect();
						}
					else{
						window.location.href = "../portal/";
						}
				}
			});
	
}
	function convertmultiselect(){
		$("#dr_principal").multiselect({ nonSelectedText :'Brand'});
		$("#dr_branch").multiselect({ nonSelectedText :'Branch'});
	}
	</script>
</body>
</HTML>
