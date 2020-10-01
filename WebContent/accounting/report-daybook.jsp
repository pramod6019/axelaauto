<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.accounting.Report_Day_Book" scope="request" />
<%mybean.doPost(request,response);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%= mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>   
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<%@include file="../Library/css.jsp"%>
</HEAD>

<body class="page-container-bg-solid page-header-menu-fixed" onload="fun();">
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
						<h1>Report Day Book</h1>
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
							<li><a href=../accounting/index.jsp>Accounting</a></li>
							<li>&gt; <a href="report-cashbook.jsp">Cash Book</a><b>:</b></li>

						</ul>
						<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<center>
								<font color="red"><b><%=mybean.msg%></b></font><br />
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Day Book</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="container-fluid">
										<!-- START PORTLET BODY -->
										<form method="post" class="form-horizontal" name="frm1"
											id="frm1">
											<div class="container-fluid " style="margin-bottom: 15px">
												<div class="form-element4">
													<label>Voucher Type:</label> <div><select multiple="multiple"
														name="dr_voucher_type" class="form-control"
														id="dr_voucher_type" size=10><%=mybean.PopulateVoucherType(mybean.vouchertype_idarr)%>
													</select></div>
												</div>
												
												<div class="form-element4">
												<label>Brand</label></br>
													<span id="multiprincipal">
													<select
														name="dr_principal"
														class="form-control multiselect-dropdown"
														multiple='multiple' size=10
														id="dr_principal" onChange="PopulateBranches();">
															<%=mybean.acccheck.PopulatePrincipal(mybean.brand_ids, mybean.comp_id, request)%>
													</select>
													</span>
											</div>
											<div class="form-element4">
														<label>Region:</label>
															<div id="regionHint">
														<%=mybean.acccheck.PopulateRegion(mybean.brand_id, mybean.region_ids, mybean.comp_id, request)%>
														</div>
											</div>

												<div class="form-element4">
												<label>Branch<font color="#ff0000">*</font>:</label></br>
														<span id="branchHint">
														<%=mybean.acccheck.PopulateBranchesld( mybean.brand_id, mybean.region_id, mybean.branch_ids, mybean.comp_id, request)%>
														</span>
											</div>
											
											<div class="form-element4">
													<label>Ledger<font color=red>*</font>:</label> 
													<div>
													<select class="form-control select2" id="ledger" name="ledger">
															<%=mybean.ledgercheck.PopulateLedgers("0", mybean.ledger_id, mybean.comp_id)%>
													</select>
													</div>
												</div>
												
												
<!-- 											<div class="form-element3"> -->
<!-- 													<label>Financial Year<font color=red>*</font>:</label> -->
<!-- 														<select name="dr_finyear_id" id="dr_finyear_id" class="form-control" /> -->
<%-- 														<%=mybean.PopulareYear(mybean.finyear_id)%> --%>
<!-- 														</select> -->
<!-- 													</div> -->
													
<!-- 													<div class="form-element3"> -->
<!-- 													<label>Type<font color=red>*</font>:</label> -->
<!-- 														<select name="dr_type_id" id="dr_type_id" class="form-control" /> -->
<%-- 														<%=mybean.PopulateType(mybean.type_id)%> --%>
<!-- 														</select> -->
<!-- 													</div> -->
<!-- 													<div id="date_show_hide"> -->
												<div class="form-element4">
													<label>Date<font color="red">*</font>:
													</label> <input name="txt_startdate" id="txt_startdate" type="text"
														class="form-control datepicker"
														value="<%=mybean.startdate%>" size="12" maxlength="10" />

												</div>
												
<!-- 												<div class="form-element3" > -->
<!-- 													<label>End Date<font color="red">*</font>: -->
<!-- 													</label> <input name="txt_enddate" id="txt_enddate" type="text" -->
<!-- 														class="form-control datepicker" -->
<%-- 														value="<%=mybean.enddate%>" size="12" maxlength="10" /> --%>

<!-- 												</div> -->
<!-- 												</div> -->
													
													
<!-- 													<div class="form-element2" id="monthshow"> -->
<!-- 												<label>Month<font color="#ff0000">*</font>: -->
<!-- 												</label> <select name="dr_month" class="form-control" -->
<!-- 													id="dr_month"> -->
<%-- 													<%=mybean.PopulateMonth(mybean.month)%> --%>
<!-- 												</select> -->
<!-- 											</div> -->
													
<!-- 													<div class="form-element3"> -->
<!-- 													<label>Ledger Type<font color=red>*</font>:</label> -->
<!-- 														<select name="dr_type_id" id="dr_type_id" class="form-control" /> -->
<%-- 														<%=mybean.PopulateType(mybean.type_id)%> --%>
<!-- 														</select> -->
<!-- 													</div> -->
											<div class="row"></div>
											<center>
												<div class="form-element12">
												
													<input name="submit_button" type="submit"
														class="btn btn-success" id="submit_button" value="Go" />
													<input type="hidden" name="submit_button" value="Submit" />
												</div>
												</center>
											</div>
									</div>
								</div>
							</div>
							<div><%=mybean.StrHTML%></div>

						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%@include file="../Library/js.jsp"%>
	<script language="JavaScript" type="text/javascript">
	
	$(document).ready(function() {
		$("#dr_voucher_type").multiselect();
		$("#dr_type_id").change(function(){
			var type=$("#dr_type_id").val();
			if(type==2){
				$("#date_show_hide").hide();
				$("#monthshow").show();
				
			}else{
				$("#date_show_hide").show();
				$("#monthshow").hide();
			}
		});
	});
		
	function PopulateBranches() {
		var brand_id = outputSelected(document.getElementById("dr_principal").options);
		showHintMultySelect( '../accounting/acc-check.jsp?multiplecheck=yes&brand_id=' + brand_id + '&mulbranch=yes', 'branchHint');
	}
		
	function showHintMultySelect(url, Hint) {
		$('#' + Hint).html('<div id=loading align=center><img align=center src=\"../admin-ifx/loading.gif\" /></div>');
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
		
	 function ListshowHint(id, Hint) {
		 var a = document.getElementById("listbody_row_"+Hint);
		 if(a.style.display==='none'){
// 			 	$(".listbody_row_td").html('');
// 				$(".listbody_row").hide();
				$('#list_'+Hint).html("<div id=loading align=center><img align=center src=../admin-ifx/loading.gif /></div>");
				$("#listbody_row_"+Hint).show();
				url = "../accounting/day-book-check.jsp?all=yes&voucher_id="+id;
					$.ajax({
						url: url,
						type: 'GET',
						success: function (data){
								//alert("data=="+data);
								if(data.trim() != 'SignIn'){
									$('#list_'+Hint).html('' + data.trim() + '');
								} else{
									window.location.href = "../portal/";
								}
						}
					});
		 }
// 		 else{
// 				$(".listbody_row_td").html('');
// 				$(".listbody_row").hide();
// 		 }
	}
		
</script>
</body>
</HTML>
