<%@ page errorPage="error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.accounting.Report_LedgerStatement" scope="request" />
<% mybean.doGet(request, response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp" %>
</HEAD>
<body onLoad="FormFocus()"
	class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>Report Ledger Statement</h1>
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
						<li><a href=report-ledgerstatement.jsp>Ledger Statement</a>:</li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font> <br>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Ledger Statement</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="form1" class="form-horizontal" method="get">
												<div class="form-element6">
													<label>Group:</label>
														<select name="dr_group" class="form-control" id="dr_group">
															<%=mybean.PopulateGroup()%>
														</select>
												</div>
												<div class="form-element6">
													<label>Type:</label>
														<select name="dr_voucher_type" class="form-control"
															id="dr_voucher_type">
															<%=mybean.PopulateVoucherType()%>
														</select>
											</div>

												<div class="form-element3">
												<label>Brand:</label></br>
													<span id="multiprincipal"> <select
														name="dr_principal" size="10" multiple="multiple"
														class="form-control service_element hidden"
														id="dr_principal" onChange="PopulateBranches();">
															<%=mybean.acccheck.PopulatePrincipal(mybean.brand_ids, mybean.comp_id, request)%>
													</select>
													</span>
												</div>

												<div class="form-element3">
												<label>Branch:</label></br>
														<span id="branchHint">
														<%=mybean.acccheck.PopulateBranchesld( mybean.brand_id, mybean.region_id, mybean.branch_ids, mybean.comp_id, request)%>
														</span>
												</div>

												<div class="form-element6">
													<label>Ledger: </label>
														<select class="form-control select2" id="ledger" name="ledger">
															<%=mybean.ledgercheck.PopulateLedgers("0", mybean.customer_id, mybean.comp_id)%>
														</select>
											</div>
									<div class="row"></div>
												<div class="form-element6">
													<label>Order By:</label>
														<select name="dr_orderby" class="form-control"
															id="dr_orderby">
															<%=mybean.PopulateOrderBy()%>
														</select>
											</div>

												<div class="form-element3">
														<label>Start Date:</label>
															<input name="txt_startdate" id="txt_startdate"
																type="text" class="form-control datepicker"
																value="<%=mybean.startdate%>" size="12" maxlength="10" />
												</div>
													<div class="form-element3">
														<label>End Date:</label>
															<input name="txt_enddate" id="txt_enddate" type="text"
																class="form-control datepicker"
																value="<%=mybean.enddate%>" size="12" maxlength="10" />
												</div>
												<div class="row"></div>
															<center>
																<input name="submit_button" type="submit" class="btn btn-success" id="submit_button" value="Go" />
																<input type="hidden" name="submit_btn" value="Submit" />
															</center>
											<%
												if (!mybean.StrHTML.equals("") && mybean.ExportPerm.equals("1")) {
											%>
											<!-- 											<div class="container-fluid"> -->
											<!-- 												<div class="form-body col-md-12 col-md-12"> -->
											<!-- 													<div class="form-group"> -->
											<!-- 														<div class="col-md-12"> -->
											<!-- 															<center> -->
											<!-- 																<input name="export_button" type="submit" -->
											<!-- 																	class="btn btn-success" id="export_button" -->
											<!-- 																	value="Export" /> -->
											<!-- 															</center> -->
											<!-- 														</div> -->
											<!-- 													</div> -->
											<!-- 												</div> -->
											<!-- 											</div> -->
											<%
												}
											%>

										</form>
									</div>


								</div>
							</div>

							<div class="container-fluid">
								<div class="tab-pane" id="">
									<center>
										<strong><%=mybean.RecCountDisplay%></strong>
									</center>
									<!-- START PORTLET BODY -->
									<br>
									<center>
										<%=mybean.PageNaviStr%>
									</center>
									<br>
									<center>
										<%=mybean.StrHTML%>
									</center>
									<br>
									<center></center>
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
	<%@include file="../Library/js.jsp" %>
	<script>
		$(function() {
			$('#dr_principal').multiselect({
				enableClickableOptGroups : true,
				includeSelectAllOption : true,
			});
		});
		$(function() {
			$('#dr_branch').multiselect({
				enableClickableOptGroups : true,
				includeSelectAllOption : true,
			});
		});
	</script>

	<script language="JavaScript" type="text/javascript">
		function FormFocus() { //v1.0

		}
	</script>
	<script>
		function PopulateBranches() {
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			showHintMultySelect( '../accounting/acc-check.jsp?multiplecheck=yes&brand_id=' + brand_id + '&mulbranch=yes', 'branchHint');
		}
		function showHintMultySelect(url, Hint) {
			$('#' + Hint) .html( '<div id=loading align=center><img align=center src=\"../admin-ifx/loading.gif\" /></div>');
			$.ajax({
				url : url,
				type : 'GET',
				success : function(data) {
					if (data.trim() != 'SignIn') {
						$('#' + Hint).fadeIn(500).html('' + data.trim() + '');
						convertmultiselect();
					} else {
						window.location.href = "../portal/";
					}
				}
			});

		}
		function convertmultiselect() {
			$("#dr_principal").multiselect({
				nonSelectedText : 'Brand'
			});
			$("#dr_branch").multiselect({
				nonSelectedText : 'Branch'
			});
		}
	</script>

</body>
</HTML>
