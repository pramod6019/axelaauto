<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.axelaauto_app.App_Quote_List_Filter" scope="request" />
<jsp:useBean id="mybeancheck" class="axela.axelaauto_app.App_MIS_Check" scope="request" />
<%
	mybean.doGet(request, response);
%>

<!DOCTYPE html>

<html lang="en">
<!--<![endif]-->
<!-- BEGIN HEAD -->

<head>
<meta charset="utf-8" />
<title>AxelaAuto-App</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1" name="viewport" />
<meta content="" name="description" />
<meta content="" name="author" />
<!-- BEGIN GLOBAL MANDATORY STYLES -->
<link
	href="http://fonts.googleapis.com/css?family=Open

+Sans:400,300,600,700&subset=all"
	rel="stylesheet" type="text/css" />
<link href="css/simple-line-icons.min.css" rel="stylesheet"
	type="text/css" />
<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<!-- <link href="css/uniform.default.css" rel="stylesheet" type="text/css" /> -->
<!-- <link href="css/bootstrap-switch.css" rel="stylesheet" type="text/css" /> -->
<link href="css/components-rounded.css" rel="stylesheet"
	id="style_components" type="text/css" />
<link href="css/plugins-md.css" rel="stylesheet" type="text/css" />
<link rel="shortcut icon" href="favicon.ico" />

<script src="js/jquery.min.js" type="text/javascript"></script>
<script src="js/jquery-ui.js" type="text/javascript"></script>	
<script type="text/javascript" src="js/Validate.js"></script>
<!-- <link href="css/all.css" rel="stylesheet" type="text/css" /> -->
<style>
.col-xs-3 {
	width: 25%;
	position: relative;
	min-height: 1px;
	padding-right: 0px;
	padding-left: 0px;
	position: relative;
	color: #fff;
}

.col-xs-9 {
	width: 75%;
}

ul li {
	text-align: center;
}

.nav-tabs>li>a {
	line-height: 1.42857143;
	border-radius: 0px 0px 0 0
}

label {
	color: #8E44AD;
}

.container {
	padding-right: 0px;
	padding-left: 0px;
	margin-right: auto;
	margin-left: auto;
	margin-top: 45px;
}

a {
	color: #fff;
}

.panel-heading {
	margin-bottom: 20px;
	background-color: #8E44AD;
	border: 1px solid transparent;
	border-radius: 0px;
	box-shadow: 0px 1px 1px rgba(0, 0, 0, 0.05);
}

strong {
	color: #fff;
}

.header-wrap {
	position: fixed;
	width: 100%;
	top: 0;
	z-index: 1;
}
</style>
<script>
	function formsubmit() {
		document.getElementById('addfollowup').submit();
	}
	
	 function PopulateRegion() { //v1.0
	        var name='chk_brand_id';
		 var brand_id=filterCheck(name);
			   showHint('app-mis-check.jsp?brand_id='+brand_id+'&region=yes','tab_6_3');
		} 	
			function PopulateBranches() {
			 var name='chk_brand_id';
			var name2='chk_region_id';
			var brand_id=filterCheck(name);
			var region_id=filterCheck(name2);
			showHint('app-mis-check.jsp?brand_id='+brand_id+ '&region_id=' + region_id +'&branch=yes','tab_6_4');
		}

		function PopulateModels() { //v1.0
		var name='chk_brand_id';
		var brand_id=filterCheck(name);
		showHint('app-mis-check.jsp?brand_id='+brand_id+'&model=yes','tab_6_1');
		}
				
		function PopulateExecutives() { //v1.0
		var name='chk_branch_id';
		var branch_id=filterCheck(name);
	    showHint('app-mis-check.jsp?exe_branch_id='+branch_id+'&executive=yes', 'tab_6_5');
			   }
	
	
	
</script>
</head>
<body>
	<div class="header-wrap">
		<div class="panel-heading">
			<span class="panel-title"> <strong>Quote Filter By</strong> 
			<span style="float: right"> 
			 <!-- <button class="btn btn-sm" name="clearbutton" id="clearbutton"> <b style="color: #8E44AD;">CLEAR</b> </button>  -->
			<a href="javascript:;" style="border:0" type="button" class="btn btn-default btn-sm" name="clearbutton" id="clearbutton"><b style="color: #8E44AD;">CLEAR</b></a> 
<!-- <button type="button" name="apply_btn" id="apply_btn" class="btn btn-sm" value="Apply" onclick="formsubmit();"> <b style="color: #8E44AD;">APPLY</b> </button> -->
			<a href="javascript:;" style="border:0" type="button" class="btn btn-default btn-sm" name="apply_btn" id="apply_btn" onclick="formsubmit();"><b style="color: #8E44AD;">APPLY</b></a>
			 <!-- <input type="button" name="apply_btn" id="apply_btn" class="btn" value="Apply" onclick="formsubmit();">  -->
			</span>
			</span>
		</div>
	</div>

	<div class="container">
		<div class="col-md-3 col-sm-3 col-xs-3">
			<ul class="nav nav-tabs nav-stacked">
				
				<li><a href="#tab_6_2" data-toggle="tab"> <b>Brand</b> </a></li>
				<li><a href="#tab_6_3" data-toggle="tab"> <b>Region</b>
				</a></li>
				<li><a href="#tab_6_4" data-toggle="tab"> <b>Branch</b>
				<li><a href="#tab_6_1" data-toggle="tab"> <b>Model</b>
				</a></li>
			
				
				
				<!-- </a></li>
				<li><a href="#tab_6_6" data-toggle="tab"> <b>SOE</b>
				</a></li> -->
				<li><a href="#tab_6_5" data-toggle="tab"> <b>Consultant</b>
				</a></li>
				
				<li class="active"><a href="#tab_6_7" class="active" data-toggle="tab"> <b>More</b>
				</a></li>
				<li><a href="" data-toggle=""> <b></b></a></li>
				<li><a href="" data-toggle=""> <b></b></a></li>
				<li><a href="" data-toggle=""> <b></b></a></li>
				<li><a href="" data-toggle=""> <b></b></a></li>
				<li><a href="" data-toggle=""> <b></b></a></li>
				<li><a href="" data-toggle=""> <b></b></a></li>
				<li><a href="" data-toggle=""> <b></b></a></li>
				<li><a href="" data-toggle=""> <b></b></a></li>
				<li><a href="" data-toggle=""> <b></b></a></li>


			</ul>
		</div>

		<div class="portlet-body">
			<!--BEGIN TABS-->
			<div class="col-md-9 col-sm-9 col-xs-9">
				<form role="form" class="form-horizontal" name="addfollowup"
					id="addfollowup" method="get"
					action="callurlapp-veh-quote-list.jsp?comp_id=<%=mybean.comp_id%>&emp_uuid=<%=mybean.emp_uuid%>&filter=yes&enquirystatus=filter">
					<div class="tab-content">
						<div class="tab-pane fade" id="tab_6_1">
							<%=mybeancheck.PopulateModel("",mybean.comp_id)%>
						</div>

						<div class="tab-pane fade" id="tab_6_2">

								<%=mybeancheck.PopulateBrand(mybean.comp_id, mybean.BranchAccess)%>

						</div>


						<div class="tab-pane fade" id="tab_6_3">

								<%=mybeancheck.PopulateRegion("", mybean.comp_id, mybean.BranchAccess)%>

						</div>


						<div class="tab-pane fade" id="tab_6_4">

							<%=mybeancheck.Populatebranch1("", "", mybean.comp_id,mybean.BranchAccess)%>

						</div>


						<div class="tab-pane fade" id="tab_6_5">

						<%=mybeancheck.PopulateExecutive("", mybean.comp_id, mybean.BranchAccess, mybean.ExeAccess)%>

						</div>


						<%-- <div class="tab-pane fade" id="tab_6_6">

							<%=mybean.PopulateSOE  (mybean.comp_id)%>

						</div> --%>

						<div class="tab-pane active" id="tab_6_7">
							<div class="scroller" style="height:1500px;s" data-always-
								visible="1" data-rail-visible="0">
								<div class="col-md-12  col-sm-12" style="padding-right: 0px;">
									<div class="form-body">
										<div class="form-group form-md-line-input">
											<label for="form_control_1" value=""> Quote ID:</label> <input
												type="text" class="form-control" name="txt_quote_id"
												id="txt_quote_id">
										     </div>
										
										
										<div class="form-group form-md-line-input">
											<label for="form_control_1" value=""> Branch Name:</label> 
						                   <select class="form-control" name="dr_branch_name" id="dr_branch_name">
							                  <%=mybean.PopulateBranch(mybean.branch_id, "", request)%> 
						                    </select>
										</div>
										
										<div class="form-group form-md-line-input">
											<label for="form_control_1" value=""> Customer ID:</label> <input
												type="text" class="form-control" name="txt_customer_id"
												id="txt_customer_id">
										</div>
										
										<div class="form-group form-md-line-input">
											<label for="form_control_1" value=""> Customer Name:</label>
											<input type="text" class="form-control"
												name="txt_customer_name" id="txt_customer_name">
										</div>
										
										
										<div class="form-group form-md-line-input">
											<label for="form_control_1" value=""> Contact ID:</label> <input
												type="text" class="form-control" name="txt_contact_id"
												id="txt_contact_id">
										</div>
										
										<div class="form-group form-md-line-input">
											<label for="form_control_1" value=""> Contact Name:</label> <input
												type="text" class="form-control" name="txt_contact_name"
												id="txt_contact_name">
										</div>
										
										<div class="form-group form-md-line-input">
											<label for="form_control_1" value=""> Contact Mobile:</label>
											<input type="text" class="form-control"
												name="txt_contact_mobile" id="txt_contact_mobile">
										</div>
										
										<div class="form-group form-md-line-input">
											<label for="form_control_1" value=""> Contact Email:</label>
											<input type="text" class="form-control"
												name="txt_contact_email" id="txt_contact_email">
										</div>
										
										<!-- <div class="form-group form-md-line-input">
											<label for="form_control_1" value=""> Pan No:</label>
											<input type="text" class="form-control"
												name="txt_pan_no" id="txt_pan_no">
										</div> -->
										
										<div class="form-group form-md-line-input">
											<label for="form_control_1" value=""> Enquiry ID:</label>
											<input type="text" class="form-control"
												name="txt_enquiry_id" id="txt_enquiry_id">
										</div>
										
										 <div class="form-group form-md-line-input">
											<label for="form_control_1" value=""> Quote Date:</label>
										     </div>
										
										<div class="form-group form-md-line-input">
											<label for="form_control_1" value=""> From:</label> <input
												type="date" class="form-control" name="txt_from_date"
												id="txt_From_date">
										</div>
										
										<div class="form-group form-md-line-input">
											<label for="form_control_1" value=""> To:</label> <input
												type="date" class="form-control" name="txt_to_date"
												id="txt_to_date">
										</div>
										
										<div class="form-group form-md-line-input">
											<label for="form_control_1" value=""> Quote No:</label> <input
												type="text" class="form-control" name="txt_quote_no"
												id="txt_quote_no">
										     </div>
										
										 
										
										<!-- <div class="form-group form-md-line-input">
											<label for="form_control_1" value=""> Quote ID:</label>
											<input type="text" class="form-control"
												name="txt_quote_id" id="txt_quote_id">
										</div> -->
										
										<!-- <div class="form-group form-md-line-input">
											<label for="form_control_1" value=""> Stock ID:</label>
											<input type="text" class="form-control"
												name="txt_vehstock_id" id="txt_vehstock_id">
										</div> -->
										
										<div class="form-group form-md-line-input">
											<label for="form_control_1" value=""> Active:</label> 
						                   <select class="form-control" name="dr_action" id="dr_action">
							                 <%=mybean.PopulateActive()%>
						                    </select>
										</div>
										
										<!-- Quote Date start -->
<!-- 										<div class="form-group form-md-line-input"> -->
<!-- 										<label for="form_control_1">Quote Date:</label> -->
<!-- 										</div> -->
										
<!-- 										<div class="form-group form-md-line-input"> -->
<!-- 											<label for="form_control_1" value=""> From:</label> <input -->
<!-- 												type="date" class="form-control" name="txt_quote_from_payment_date" -->
<!-- 												id="txt_quote_from_payment_date"> -->
<!-- 										</div> -->
										
<!-- 										<div class="form-group form-md-line-input"> -->
<!-- 											<label for="form_control_1" value=""> To:</label> <input -->
<!-- 												type="date" class="form-control" name="txt_quote_to_payment_date" -->
<!-- 												id="txt_quote_to_payment_date"> -->
<!-- 										</div> -->
										<!-- Quote Date end -->
										
<!-- 										<div class="form-group form-md-line-input"> -->
<!-- 											<label for="form_control_1" value=""> Authorized:</label> -->
<!-- 											<input type="text" class="form-control" -->
<!-- 												name="txt_authorized_id" id="txt_authorized_id"> -->
<!-- 										</div> -->
										
<!-- 										<div class="form-group form-md-line-input"> -->
<!-- 											<label for="form_control_1" value=""> Authorized By:</label> -->
<!-- 											<input type="text" class="form-control" -->
<!-- 												name="txt_authorized_id" id="txt_authorized_id"> -->
<!-- 										</div> -->
										
										 <div class="form-group form-md-line-input">
											<label for="form_control_1" value=""> Authorized Date:</label>
										     </div>
										
										<div class="form-group form-md-line-input">
											<label for="form_control_1" value=""> From:</label> <input
												type="date" class="form-control" name="txt_from_authorized_date"
												id="txt_from_authorized_date">
										</div>
										
										<div class="form-group form-md-line-input">
											<label for="form_control_1" value=""> To:</label> <input
												type="date" class="form-control" name="txt_to_authorized_date"
												id="txt_to_authorized_date">
										</div>
										<br><br>
<!-- 										<div class="form-group form-md-line-input"> -->
<!-- 											<label for="form_control_1" value=""> Active:</label>  -->
<!-- 						                   <select class="form-control" name="dr_action" id="dr_action"> -->
<%-- 							                 <%=mybean.PopulateActive()%> --%>
<!-- 						                    </select> -->
<!-- 										</div> -->
										
										<input type="hidden" name="filter" id="filter" value="yes">
											<input type="hidden" name="quotestatus" id="quotestatus" value="filter">
										
										<%-- <!-- Tentative Delivery Date start -->
										<div class="form-group form-md-line-input">
										<label for="form_control_1">Tentative Delivery Date:</label>
										</div>
										
										<div class="form-group form-md-line-input">
											<label for="form_control_1" value=""> From:</label> <input
												type="date" class="form-control" name="txt_so_from_promise_date"
												id="txt_so_from_promise_date">
										</div>
										
										<div class="form-group form-md-line-input">
											<label for="form_control_1" value=""> To:</label> <input
												type="date" class="form-control" name="txt_so_to_promise_date"
												id="txt_so_to_promise_date">
										</div>
										<!-- Tentative Delivery Date start -->
										
										<!-- Delivery Date start -->
								      <div class="form-group form-md-line-input">
										<label for="form_control_1">Delivery Date:</label> 
			   						 </div>
			   						 
			   						 <div class="form-group form-md-line-input">
											<label for="form_control_1" value=""> From:</label> <input
												type="date" class="form-control" name="txt_so_from_delivery_date"
												id="txt_so_from_delivery_date">
									 </div>
										
										<div class="form-group form-md-line-input">
											<label for="form_control_1" value=""> To:</label> <input
												type="date" class="form-control" name="txt_so_to_delivery_date"
												id="txt_so_to_delivery_date">
										</div>
										<!-- Delivery Date end -->
										
										<!-- Cancel Date start -->
			   						 <div class="form-group form-md-line-input">
										<label for="form_control_1"> Cancel Date:</label> 
			   						 </div>
			   						 
			   						 <div class="form-group form-md-line-input">
											<label for="form_control_1" value=""> From:</label> <input
												type="date" class="form-control" name="txt_so_from_cancel_date"
												id="txt_so_from_cancel_date">
									 </div>
										
										<div class="form-group form-md-line-input">
											<label for="form_control_1" value=""> To:</label> <input
												type="date" class="form-control" name="txt_so_to_cancel_date"
												id="txt_so_to_cancel_date">
										<input type="hidden" name="comp_id" id="comp_id" value="<%=mybean.comp_id%>">
											<input type="hidden" name="filter" id="filter" value="yes">
											<input type="hidden" name="enquirystatus" id="enquirystatus" value="filter">
												<input type="hidden" name="emp_uuid" id="emp_uuid" value="<%=mybean.emp_uuid%>">
											
											
										</div>
										<!-- Cancel Date end --> --%>
									
									</div>

								</div>
							</div>
						</div>
					</div>
					<!--END TABS-->
				</form>
			</div>
		</div>
	</div>

	<!-- BEGIN CORE PLUGINS -->
	<script src="js/jquery.min.js" type="text/javascript"></script>
	<script src="js/bootstrap.min.js" type="text/javascript"></script>
<!-- 	<script src="js/jquery.slimscroll.min.js" type="text/javascript"></script> -->
	<script src="js/jquery.uniform.min.js" type="text/javascript"></script>
<!-- 	<script src="js/app.js" type="text/javascript"></script> -->
	<script>
		$(function() {
			$("#selectallbrand_id").click(function() {
				$('.casebrand_id').prop('checked', this.checked);
			});

			$(".casebrand_id").click(function() {
				if ($(".casebrand_id").length ==

				$(".casebrand_id:checked").length) {
					$('#selectallbrand_id').prop

					("checked", "checked");
				} else {
					$('#selectallbrand_id').removeAttr("checked");
				}
			});

			$("#selectallregion_id").click(function() {
				$('.caseregion_id').prop('checked', this.checked);
			});

			$(".caseregion_id").click(function() {
				if ($(".caseregion_id").length ==

				$(".caseregion_id:checked").length) {
					$('#selectallregion_id').prop

					("checked", "checked");
				} else {
					$('#selectallregion_id').removeAttr("checked");
				}
			});

			$("#selectallbranch_id").click(function() {
				$('.casebranch_id').prop('checked', this.checked);
			});

			$(".casebranch_id").click(function() {
				if ($(".casebranch_id").length ==

				$(".casebranch_id:checked").length) {
					$('#selectallbranch_id').prop

					("checked", "checked");
				} else {
					$('#selectallbranch_id').removeAttr("checked");
				}
			});

			$("#selectallmodel_id").click(function() {
				$('.casemodel_id').prop('checked', this.checked);
			});

			$(".casemodel_id").click(function() {
				if ($(".casemodel_id").length ==

				$(".casemodel_id:checked").length) {
					$('#selectallmodel_id').prop

					("checked", "checked");
				} else {
					$('#selectallmodel_id').removeAttr("checked");
				}
			});

			$("#selectallexec_id").click(function() {
				$('.caseexec_id').prop('checked', this.checked);
			});

			$(".caseexec_id").click(function() {
				if ($(".caseexec_id").length ==

				$(".caseexec_id:checked").length) {
					$('#selectallexec_id').prop

					("checked", "checked");
				} else {
					$('#selectallexec_id').removeAttr("checked");
				}
			});

			$("#selectallsoe_id").click(function() {
				$('.casesoe_id').prop('checked', this.checked);
			});

			$(".casesoe_id").click(function() {
				if ($(".casesoe_id").length ==

				$(".casesoe_id:checked").length) {
					$('#selectallsoe_id').prop

					("checked", "checked");
				} else {
					$('#selectallsoe_id').removeAttr("checked");
				}
			});

		});

		$(function() {
			$('#clearbutton').click(function() {
				$('#addfollowup')[0].reset();

			});
		});
	</script>
</body>

</html>