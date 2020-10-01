<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.accessories.Index" scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<meta name="viewport" content="width=device-width, initial-scale=1" />

<%@include file="../Library/css.jsp"%>
<style>
.btn.btn-outline.blue-oleo.active, .btn.btn-outline.blue-oleo:active,
	.btn.btn-outline.blue-oleo:active:focus, .btn.btn-outline.blue-oleo:active:hover,
	.btn.btn-outline.blue-oleo:focus, .btn.btn-outline.blue-oleo:hover {
	border-color: #94A0B2;
	color: #FFF;
	background-color: #94A0B2;
}

.icon-btn {
	top: -13px;
	left: 17px;
}

.dashboard-stat2 {
	box-shadow: 2px 2px 2px #888888;
	width: 200px;
}

@media screen and (min-width:992px) {
	#portlet {
		margin-left: 7px;
	}
	.portlet.light {
		width: 97%;
		margin-left: 23px;
	}
}
</style>

</HEAD>

<body onload="allFilter();"
	class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>Accessories</h1>
					</div>
					<!-- END PAGE TITLE -->
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
				<div class="page-content-inner">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../accessories/index.jsp">Accessories</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					</div>
					</div>
						
						<div class="tab-pane" id="">
							<!-- 					BODY START -->

								<!-- 							=====================start card================ -->

								<div class="col-md-12 col-sm-12">

									<div class="portlet light ">
											<div class="portlet-title">
												<div class="caption caption-md">
													<i class="icon-bar-chart font-dark hide"></i> <span
														class="caption-subject font-green-steel uppercase bold">Service Summary</span>
														 <span class="caption-helper hide">weekly stats...</span>
												</div>
												<div class="actions form-element3" style="text-align: right;margin-top: -23px;">
													<div class="btn-group btn-group-devided" data-toggle="buttons">
														<label
															class="btn btn-transparent btn-no-border blue-oleo btn-outline btn-circle btn-sm active" id='today'>
															<input type="radio" name="options"  onchange="count(this.value);getvalue(this.value);"
															id="option1" value="today">Today
														</label> <label
															class="btn btn-transparent btn-no-border blue-oleo btn-outline btn-circle btn-sm" id='month'>
															<input type="radio" name="options"  onchange="count(this.value);getvalue(this.value);" 
															id="option2" value="month">Month
														</label> <label
															class="btn btn-transparent btn-no-border blue-oleo btn-outline btn-circle btn-sm" id='quarter'>
															<input type="radio" name="options"  onchange="count(this.value);getvalue(this.value);"
															id="option3" value="quarter">Quarter
														</label>
													</div>
												</div>
											</div>
 
										<div class="actions col-md-12">

											<form name="form1" class="form-horizontal">
													<div class="form-element3">
														<div>Brand:</div>
															<select name="dr_principal" size="10" multiple="multiple"
																class="form-control multiselect-dropdown service_element hidden" id="dr_principal"
																onChange="PopulateBranches();PopulateRegion();">
																	<%=mybean.mischeck.PopulatePrincipal(mybean.brand_ids, mybean.comp_id, request)%>
															</select>
													</div>

													<div class="form-element3">
														<label>Region:</label>
															<div id="regionHint">
																<%=mybean.mischeck.PopulateRegion(mybean.filter_brand_id, mybean.region_ids, mybean.comp_id, request)%>
															</div>
													</div>
													
												<div class="form-element3">
														<label>Branch:</label>
														<div id="branchHint">
																<%=mybean.mischeck.PopulateBranches(mybean.filter_brand_id, mybean.filter_region_id, mybean.branch_ids, mybean.comp_id, request)%>
														</div>
												</div>
											</form>

										</div>
										<input type='text' value='today' id='cardvalue' name='cardvalue' hidden />

										<div class="portlet-body" id="portlet">
											<div id="dashbox" style="margin-left: -29px;" class="row">
												<center>
													<div class="form-element3">
														<div class="dashboard-stat2 ">
															<div class="display" id='hint'>
																<div class="number col-md-7 col-xs-7">
																	<h3 class="font-green-sharp">
																		<span data-counter="counterup" id='preorder_count'></span>
																	</h3>
																	<small>Preorder Count</small>
																</div>
																<div class="col-md-5 col-xs-5">
																	<a class='btn-sm bg-green-sharp icon-btn col-md-5'>
																		<i class="fa fa-database"></i>
	<!-- 																	<div>Users</div> -->

																	</a>
																</div>
															</div>
															<div class="progress-info">
																<div class="progress">
																	<!-- 																style="width: 200%;" -->
																	<span class="progress-bar progress-bar-success green-sharp"
																		id='preorder_count_bar'> </span>
																</div>
																<div class="status">
																	<div class="status-title">Growth</div>
																	<div class="status-number" id='preorder_count_label'></div>
																</div>
															</div>
														</div>
													</div>
													<div class="form-element3">
														<div class="dashboard-stat2 ">
															<div class="display">
																<div class="number col-md-7 col-xs-7">
																	<h3 class="font-red-haze">
																		<span data-counter="counterup" id='preorder_amt'></span>
																	</h3>
																	<small>Preorder Amount</small>
																</div>
																<div class="col-md-5">
																	<a class='btn-sm bg-red-haze icon-btn '>
																		<i class="fa fa-filter"></i> <!-- 																		<div>Users</div> -->
																	</a>
																</div>
															</div>
															<div class="progress-info">
																<div class="progress">
																	<span class="progress-bar progress-bar-success red-haze"
																		id='preorder_amt_bar'> </span>
																</div>
																<div class="status">
																	<div class="status-title">Growth</div>
																	<div class="status-number" id='preorder_amt_label'></div>
																</div>
															</div>
														</div>
													</div>
													<div class="form-element3">
														<div class="dashboard-stat2 ">
															<div class="display">
																<div class="number col-md-7 col-xs-7">
																	<h3 class="font-blue-sharp">
																		<span data-counter="counterup" id='invoice_count'></span>
																	</h3>
																	<small>Invoice Count</small>
																</div>
																<div class="col-md-5">
																	<a class='btn-sm bg-blue-sharp icon-btn col-md-5'>
																		<i class="fa fa-feed"></i> 
																	</a>
																</div>
															</div>
															<div class="progress-info">
																<div class="progress">
																	<span class="progress-bar progress-bar-success blue-sharp"
																		id='invoice_count_bar'></span>
																</div>
																<div class="status">
																	<div class="status-title">Growth</div>
																	<div class="status-number" id='invoice_count_label'></div>
																</div>
															</div>
														</div>
													</div>
													<div class="form-element3">
														<div class="dashboard-stat2 ">
															<div class="display">
																<div class="number col-md-7 col-xs-7">
																	<h3 class="font-purple-soft">
																		<span data-counter="counterup" id='invoice_amt'></span>
																	</h3>
																	<small>Invoice Amount</small>
																</div>
																<div class="col-md-5">
																	<a class='btn-sm bg-purple-soft icon-btn col-md-7'>
																		<i class="fa fa-film"></i>
																	</a>
																</div>
															</div>
															<div class="progress-info">
																<div class="progress">
																	<span class="progress-bar progress-bar-success purple-soft"
																		id='invoice_amt_bar'> </span>
																</div>
																<div class="status">
																	<div class="status-title">Growth</div>
																	<div class="status-number" id='invoice_amt_label'></div>
																</div>
															</div>
														</div>
													</div>
												</center>
											</div>
										</div>
									</div>
								</div>

								<!--  =====================end card================ -->
								<div class="page-content">
										<div class="page-content-inner">
											<div class="tab-pane" id="">
													<div class="form-element4 pull-right"
														style="padding: 5px">
														<div class="portlet box">
															<div class="portlet-title" style="text-align: center">
																<div class="caption" style="float: none">Accessories
																	Reports</div>
															</div>
															<div class="portlet-body portlet-empty container-fluid">
																<div class="tab-pane" id="">
																	<%=mybean.ListReports()%>
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
	
		var carddata;
		var bardata;
		var periodType = "today";
		
		function count(type) {
			
			periodType = type;
			$('#cardvalue').val(type);

			switch(type){
			case 'today':
				count_type_loop(carddata[0],"preorder_count");
				count_type_loop(carddata[3], "preorder_amt");
				count_type_loop(carddata[6], "invoice_count");
				count_type_loop(carddata[9], "invoice_amt");
				
				bar_type_loop(bardata[0],carddata[0],"preorder_count");
				bar_type_loop(bardata[3],carddata[3],"preorder_amt");
				bar_type_loop(bardata[6],carddata[6],"invoice_count");
				bar_type_loop(bardata[9],carddata[9],"invoice_amt");
				
				break;
			
			case 'month':
				count_type_loop(carddata[1],"preorder_count");
				count_type_loop(carddata[4], "preorder_amt");
				count_type_loop(carddata[7], "invoice_count");
				count_type_loop(carddata[10], "invoice_amt");
				
				bar_type_loop(bardata[1],carddata[1],"preorder_count");
				bar_type_loop(bardata[4],carddata[4],"preorder_amt");
				bar_type_loop(bardata[7],carddata[7],"invoice_count");
				bar_type_loop(bardata[10],carddata[10],"invoice_amt");
				
				break;
			
			case 'quarter':
				count_type_loop(carddata[2],"preorder_count");
				count_type_loop(carddata[5], "preorder_amt");
				count_type_loop(carddata[8], "invoice_count");
				count_type_loop(carddata[11], "invoice_amt");
				
				bar_type_loop(bardata[2],carddata[2],"preorder_count");
				bar_type_loop(bardata[5],carddata[5],"preorder_amt");
				bar_type_loop(bardata[8],carddata[8],"invoice_count");
				bar_type_loop(bardata[11],carddata[11],"invoice_amt");
				
				break;
			}
		}

		function count_type_loop(mybean_value, Hint){
			var t= 100;
			if(mybean_value>999999){
				mybean_value = eval(mybean_value/100000).toFixed(3);
				for(i = 0, j = eval(mybean_value/10), t=100 ; Math.round(i) <= mybean_value ; i=i+j, t=t+100){
					setTimeout('document.getElementById("' + Hint + '").innerHTML=' + Math.round(i) +'+"L"', t) ;
					if(mybean_value==0 || mybean_value=="" || mybean_value==null){break;}
				}
			}else{
				for(i = 0, j = eval(mybean_value/10), t=100 ; Math.round(i) <= mybean_value ; i=i+j, t=t+100){
					setTimeout('document.getElementById("' + Hint + '").innerHTML=' + Math.round(i), t) ;
					if(mybean_value==0 || mybean_value=="" || mybean_value==null){break;}
				}
			}
		}
		
		function bar_type_loop(mybean_old_value, mybean_new_value, Hint){
			
			if(mybean_old_value > 0){
				var per = Math.round(eval((mybean_new_value * 100)/mybean_old_value));
					for(i = 0, j = eval(per/10), t=100 ; Math.round(i) <= per ; i=i+j, t=t+100){
						setTimeout('document.getElementById("' + Hint + '_label").innerHTML=' + Math.round(i)+'+"%";', t) ;
						setTimeout('document.getElementById("' + Hint + '_bar").style.width="' + Math.round(i)+'%";', t) ;
						if(per==0 || per=="" || per==null){break;}
					}
			}else {
				var t=100;
				if(mybean_old_value == 0 && mybean_new_value > 0){
					var per = Math.round(100);
					for(i = 0, j = eval(per/10) ; Math.round(i) <= per ; i=i+j, t=t+100){
						setTimeout('document.getElementById("' + Hint + '_label").innerHTML=' + Math.round(i)+'+"%";', t) ;
						setTimeout('document.getElementById("' + Hint + '_bar").style.width="' + Math.round(i)+'%";', t) ;
					}
				}else{
					setTimeout('document.getElementById("' + Hint + '_label").innerHTML="0%";', t) ;
					setTimeout('document.getElementById("' + Hint + '_bar").style.width="0%";', t) ;
				}
			}
		}
//		end populate cards


		function PopulateRegion() { //v1.0
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			showHint( '../accessories/mis-check.jsp?multiplecheckregion=yes&brand_id=' + brand_id + '&region=yes', 'regionHint');
		}

		function PopulateBranches() { //v1.0
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			var region_id = outputSelected(document.getElementById("dr_region").options);
			showHint( '../accessories/mis-check.jsp?multiplecheckbranch=yes&brand_id=' + brand_id + '&region_id=' + region_id + '&branch=yes', 'branchHint');

		}

		function allFilter(){
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			var region_id = outputSelected(document.getElementById("dr_region").options);
			var dr_branch = outputSelected(document.getElementById("dr_branch").options);

			var url="../accessories/index-check.jsp?refreshAll=yes&brand_id=" + brand_id + "&region_id="+region_id+"&dr_branch_id="+dr_branch;

			// for Cards
			setTimeout('showHintCards("'+url+'&cards=yes", "preorder_count,preorder_amt,invoice_count,invoice_amt");',20);
			
		}

		function showHintCards(url, Hint) {
			var card_ids = Hint.split(',');
			
			$('#'+card_ids[0]).html('<div id=loading align=center><img align=center src=\"../admin-ifx/loading.gif\" /></div>');
			$('#'+card_ids[1]).html('<div id=loading align=center><img align=center src=\"../admin-ifx/loading.gif\" /></div>');
			$('#'+card_ids[2]).html('<div id=loading align=center><img align=center src=\"../admin-ifx/loading.gif\" /></div>');
			$('#'+card_ids[3]).html('<div id=loading align=center><img align=center src=\"../admin-ifx/loading.gif\" /></div>');
			
				$.ajax({
					url: url,
					type: 'GET',
					success: function (data){
						if(data.trim() != 'SignIn'){
							carddata=data.trim().substring(0,data.trim().indexOf(":")).split(",");
							bardata=data.trim().substring(data.trim().indexOf(":")+1,data.trim().length).split(",");
							count(periodType);
						} else {
							window.location.href = "../portal/";
						}
					}
				});
		}
	</script>
</body>
</html>
