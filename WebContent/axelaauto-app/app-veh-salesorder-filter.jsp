<!DOCTYPE html>

<html lang="en">
<!--<![endif]-->
<!--- BEGIN HEAD -->

<head>
<meta charset="utf-8" />
<title>AxelaAuto</title>

<meta content="width=device-width, initial-scale=1" name="viewport" />

<link
	href="http://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700&subset=all"
	rel="stylesheet" type="text/css" />
<link href="css/simple-line-icons.min.css" rel="stylesheet"
	type="text/css" />
<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="css/uniform.default.css" rel="stylesheet" type="text/css" />
<link href="css/bootstrap-switch.css" rel="stylesheet" type="text/css" />
<link href="css/components-rounded.css" rel="stylesheet"
	id="style_components" type="text/css" />
<link href="css/plugins-md.css" rel="stylesheet" type="text/css" />
<link rel="shortcut icon" href="favicon.ico" />
<link href="css/all.css" rel="stylesheet" type="text/css" />
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
</head>
<body>
	<div class="header-wrap">
		<div class="panel-heading">
			<span class="panel-title"> <strong>Filter By</strong>
			
				<span style="float: right">
							
					<!-- <a href="javascript:;" class="btn btn-default btn-sm" name="clearbutton"
						id="clearbutton" style="background-color: #e3e3e3; color: #000;">
						Clear</a> <a href="callurlapp-veh-salesorder-list.jsp?emp_id=1"
						class="btn btn-default btn-sm" name="applybutton" id="applybutton"
						style="background-color: #e3e3e3; color: #000;"> Apply </a> -->
						
			<a href="javascript:;" style="border:0" type="button" class="btn btn-default btn-sm" name="clearbutton" id="clearbutton"><b style="color: #8E44AD;">CLEAR</b></a> 
			<a href="callurlapp-veh-salesorder-list.jsp?emp_id=1" style="border:0" type="button" class="btn btn-default btn-sm" name="applybutton" id="applybutton" onclick="formsubmit();"><b style="color: #8E44AD;">APPLY</b></a>
				
				</span>
			</span>
		</div>
		</div>

	<div class="container">
		<div class="col-md-3 col-sm-3 col-xs-3">
			<ul class="nav nav-tabs nav-stacked">
				<li class="active"><a href="#tab_6_1" class="active"
					data-toggle="tab"> <b>Model</b>
				</a></li>
				<li><a href="#tab_6_2" data-toggle="tab"> <b>Status</b>
				</a></li>
				<li><a href="#tab_6_3" data-toggle="tab"> <b>Stage</b>
				</a></li>
				<li><a href="#tab_6_4" data-toggle="tab"> <b>Priority</b>
				</a></li>
				<li><a href="#tab_6_5" data-toggle="tab"> <b>Consultant</b>
				</a></li>
				<li><a href="#tab_6_6" data-toggle="tab"> <b>SOE</b>
				</a></li>
				<li><a href="#tab_6_7" data-toggle="tab"> <b>More</b>
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
					id="addfollowup" method="post">
					<div class="tab-content">
						<div class="tab-pane active" id="tab_6_1">
							<div class="row">
								<table class="table" id="one">
									<tr style="background-color: #2f353b; color: #fff;">
										<td class="pull-left">Model</td>
										<td class="pull-right" style="position:relaive; right:50px;"><label style="color: white; position:relative; right:20px;">Select All</label> <input type="checkbox"
											style="position:relaive; right:10px; top:6px;" class="icheck" id="selectallmodel_id">
										</td>
									</tr>
									<tr>
										<td class="pull-left">Model</td>
										<td class="pull-right"><input type="checkbox"
										style="position:relaive; right:10px;"	class="casemodel_id icheck" name="model_id" value="1"></td>
									</tr>
									<tr>
										<td class="pull-left">Model</td>
										<td class="pull-right"><input type="checkbox"
										style="position:relaive; right:10px;"	class="casemodel_id icheck" name="model_id" value="2"></td>
									</tr>
								</table>
							</div>
						</div>
						<div class="tab-pane fade" id="tab_6_2">
							<div class="row">
								<table class="table">
									<tr style="background-color: #2f353b; color: #fff;">
										<td class="pull-left">Status</td>

										<td class="pull-right" style="position:relaive; right:50px;"><label style="color: white; position:relative; right:20px;">Select All </label> <input type="checkbox"
											style="position:relaive; right:10px; top:6px;" class="icheck" id="selectallstatus_id">
										</td>
									</tr>
									<tr>
										<td class="pull-left">Model</td>
										<td class="pull-right"><input type="checkbox" style="position:relaive; right:10px;"
											class="casestatus_id icheck" name="status_id" value="1"></td>
									</tr>
									<tr>
										<td class="pull-left">Model</td>
										<td class="pull-right"><input type="checkbox" style="position:relaive; right:10px;"
											class="casestatus_id icheck" name="status_id" value="2"></td>
									</tr>
								</table>
							</div>
						</div>
						<div class="tab-pane fade" id="tab_6_3">

							<div class="row">
								<table class="table">
									<tr style="background-color: #2f353b; color: #fff;">
										<td class="pull-left">Stage</td>
										<td class="pull-right" style="position:relaive; right:50px;"><label style="color: white; position:relative; right:20px;">Select All </label> <input type="checkbox"
											style="position:relaive; right:10px; top:6px;" class="icheck" id="selectallstage_id">
										</td>
									</tr>
									<tr>
										<td class="pull-left">Model</td>
										<td class="pull-right"><input type="checkbox" style="position:relaive; right:10px;"
											class="casestage_id icheck" name="stage_id"></td>
									</tr>
									<tr>
										<td class="pull-left">Model</td>
										<td class="pull-right"><input type="checkbox" style="position:relaive; right:10px;"
											class="casestage_id icheck" name="stage_id"></td>
									</tr>
								</table>
							</div>
						</div>
						<div class="tab-pane fade" id="tab_6_4">

							<div class="row">
								<table class="table">
									<tr style="background-color: #2f353b; color: #fff;">
										<td class="pull-left">Priority</td>
										<td class="pull-right" style="position:relaive; right:50px;"><label style="color: white; position:relative; right:20px;">Select All </label> <input type="checkbox"
											style="position:relaive; right:10px; top:6px;" class="icheck" id="selectallpriority_id">
										</td>
									</tr>
									<tr>
										<td class="pull-left">Model</td>
										<td class="pull-right"><input type="checkbox" style="position:relaive; right:10px;"
											class="casepriority_id icheck" name="priority_id"></td>
									</tr>
									<tr>
										<td class="pull-left">Model</td>
										<td class="pull-right"><input type="checkbox" style="position:relaive; right:10px;"
											class="casepriority_id icheck" name="priority_id"></td>
									</tr>
								</table>
							</div>
						</div>
						<div class="tab-pane fade" id="tab_6_5">

							<div class="row">
								<table class="table">
									<tr style="background-color: #2f353b; color: #fff;">
										<td class="pull-left">Consultant</td>
										<td class="pull-right" style="position:relaive; right:50px;"><label style="color: white; position:relative; right:20px;">Select All </label> <input type="checkbox"
											style="position:relaive; right:10px; top:6px;" class="icheck" id="selectallexec_id">
										</td>
									</tr>
									<tr>
										<td class="pull-left">Model</td>
										<td class="pull-right"><input type="checkbox" style="position:relaive; right:10px;"
											class="caseexec_id icheck" name="exec_id"></td>
									</tr>
									<tr>
										<td class="pull-left">Model</td>
										<td class="pull-right"><input type="checkbox" style="position:relaive; right:10px;"
											class="caseexec_id icheck" name="exec_id"></td>
									</tr>
								</table>
							</div>
						</div>
						<div class="tab-pane fade" id="tab_6_6">
							<div class="row">
								<table class="table">
									<tr style="background-color: #2f353b; color: #fff;">
										<td class="pull-left">SOE</td>
										<td class="pull-right" style="position:relaive; right:50px;"><label style="color: white; position:relative; right:20px;">Select All </label>  <input type="checkbox"
											style="position:relaive; right:10px; top:6px;" class="icheck" id="selectallsoe_id">
										</td>
									</tr>
									<tr>
										<td class="pull-left">Model</td>
										<td class="pull-right"><input type="checkbox" style="position:relaive; right:10px;"
											class="casesoe_id icheck" name="soe_id"></td>
									</tr>
									<tr>
										<td class="pull-left">Model</td>
										<td class="pull-right"><input type="checkbox" style="position:relaive; right:10px;"
											class="casesoe_id icheck" name="soe_id"></td>
									</tr>
								</table>
							</div>
						</div>
						<div class="tab-pane fade" id="tab_6_7">
							<div class="scroller" style="height: 500px;"
								data-always-visible="1" data-rail-visible="0">

								<div class="col-md-12 col-sm-12">

									<div class="form-body">
										<div class="form-group form-md-line-input">
								<label for="form_control_1" value=""> Sales Order ID:</label> <input
									type="text" class="form-control" name="" id="">
							</div>
							<div class="form-group form-md-line-input">
								<label for="form_control_1" value=""> Sales Order No.:</label> <input
									type="text" class="form-control" name="" id="">
							</div>
							<div class="form-group form-md-line-input">
								<label for="form_control_1" value=""> Sales Order Date:</label>
								<input type="text" class="form-control" name="" id="">
							</div>
							<div class="form-group form-md-line-input">
								<label for="form_control_1" value=""> Branch ID:</label> <input
									type="text" class="form-control" name="" id="">
							</div>
							<div class="form-group form-md-line-input">
								<label for="form_control_1" value=""> Branch Name:</label> <input
									type="text" class="form-control" name="" id="">
							</div>
							<div class="form-group form-md-line-input">
								<label for="form_control_1" value=""> Customer ID:</label> <input
									type="text" class="form-control" name="" id="">
							</div>
							<div class="form-group form-md-line-input">
								<label for="form_control_1" value=""> Contact ID:</label> <input
									type="text" class="form-control" name="" id="">
							</div>
							<div class="form-group form-md-line-input">
								<label for="form_control_1" value=""> Customer Name:</label> <input
									type="text" class="form-control" name="" id="">
							</div>
							<div class="form-group form-md-line-input">
								<label for="form_control_1" value=""> Customer DOB:</label> <input
									type="text" class="form-control" name="" id="">
							</div>
							<div class="form-group form-md-line-input">
								<label for="form_control_1" value=""> PAN No.:</label> <input
									type="text" class="form-control" name="" id="">
							</div>
							<div class="form-group form-md-line-input">
								<label for="form_control_1" value=""> Contact Name:</label> <input
									type="text" class="form-control" name="" id="">
							</div>
							<div class="form-group form-md-line-input">
								<label for="form_control_1" value=""> Contact Mobile:</label> <input
									type="text" class="form-control" name="" id="">
							</div>
							<div class="form-group form-md-line-input">
								<label for="form_control_1" value=""> Contact Email:</label> <input
									type="text" class="form-control" name="" id="">
							</div>
							<div class="form-group form-md-line-input">
								<label for="form_control_1" value=""> Lead ID:</label> <input
									type="text" class="form-control" name="" id="">
							</div>
							<div class="form-group form-md-line-input">
								<label for="form_control_1" value=""> Enquiry ID:</label> <input
									type="text" class="form-control" name="" id="">
							</div>
							<div class="form-group form-md-line-input">
								<label for="form_control_1" value="">Quote ID:</label> <input
									type="text" class="form-control" name="" id="">
							</div>
							<div class="form-group form-md-line-input">
								<label for="form_control_1" value="">Stock ID:</label> <input
									type="text" class="form-control" name="" id="">
							</div>
							<div class="form-group form-md-line-input">
								<label for="form_control_1" value="">Net Amount:</label> <input
									type="text" class="form-control" name="" id="">
							</div>
							<div class="form-group form-md-line-input">
								<label for="form_control_1" value="">Discount:</label> <input
									type="text" class="form-control" name="" id="">
							</div>
							<div class="form-group form-md-line-input">
								<label for="form_control_1" value="">Tax:</label> <input
									type="text" class="form-control" name="" id="">
							</div>
							<div class="form-group form-md-line-input">
								<label for="form_control_1" value=""> Total:</label> <input
									type="text" class="form-control" name="" id="">
							</div>
							<div class="form-group form-md-line-input">
								<label for="form_control_1" value="">Description:</label> <input
									type="text" class="form-control" name="" id="">
							</div>
							<div class="form-group form-md-line-input">
								<label for="form_control_1" value="">Terms:</label> <input
									type="text" class="form-control" name="" id="">
							</div>
							<div class="form-group form-md-line-input">
								<label for="form_control_1" value="">Accessories Amount:</label>
								<input type="text" class="form-control" name="" id="">
							</div>
							<div class="form-group form-md-line-input">
								<label for="form_control_1" value="">Purchase Order:</label> <input
									type="text" class="form-control" name="" id="">
							</div>
							<div class="form-group form-md-line-input">
								<label for="form_control_1" value=""> Ref. No.:</label> <input
									type="text" class="form-control" name="" id="">
							</div>
							<div class="form-group form-md-line-input">
								<label for="form_control_1" value="">Payment Date:</label> <input
									type="text" class="form-control" name="" id="">
							</div>
							<div class="form-group form-md-line-input">
								<label for="form_control_1" value="">Tentative Delivary
									Date:</label> <input type="text" class="form-control" name="" id="">
							</div>
							<div class="form-group form-md-line-input">
								<label for="form_control_1" value="">Retail Date:</label> <input
									type="text" class="form-control" name="" id="">
							</div>
							<div class="form-group form-md-line-input">
								<label for="form_control_1" value="">Delivered Date:</label> <input
									type="text" class="form-control" name="" id="">
							</div>
							<div class="form-group form-md-line-input">
								<label for="form_control_1" value="">Delivered Status:</label> <input
									type="text" class="form-control" name="" id="">
							</div>
							<div class="form-group form-md-line-input">
								<label for="form_control_1" value="">Open:</label> <input
									type="text" class="form-control" name="" id="">
							</div>
							<div class="form-group form-md-line-input">
								<label for="form_control_1" value=""> Consultant:</label> <input
									type="text" class="form-control" name="" id="">
							</div>
							<div class="form-group form-md-line-input">
								<label for="form_control_1" value=""> Active:</label> <input
									type="text" class="form-control" name="" id="">
							</div>
							<div class="form-group form-md-line-input">
								<label for="form_control_1" value=""> Notes:</label> <input
									type="text" class="form-control" name="" id="">
							</div>
							<div class="form-group form-md-line-input">
								<label for="form_control_1" value=""> Entry By:</label> <input
									type="text" class="form-control" name="" id="">
							</div>
							<div class="form-group form-md-line-input">
								<label for="form_control_1" value=""> Enter Date:</label> <input
									type="text" class="form-control" name="" id="">
							</div>
							<div class="form-group form-md-line-input">
								<label for="form_control_1" value=""> Modified By:</label> <input
									type="text" class="form-control" name="" id="">
							</div>
							<div class="form-group form-md-line-input">
								<label for="form_control_1" value=""> Modified Date:</label> <input
									type="text" class="form-control" name="" id="">
							</div><br><br>
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
	<script src="js/jquery.slimscroll.min.js" type="text/javascript"></script>
	<script src="js/jquery.uniform.min.js" type="text/javascript"></script>
	<script src="js/app.min.js" type="text/javascript"></script>
	<script> 
		$(function(){
			$("#selectallmodel_id").click(function(){
				$('.casemodel_id').prop('checked',this.checked);
			});
			
			$(".casemodel_id").click(function(){
				if($(".casemodel_id").length == $(".casemodel_id:checked").length){
					$('#selectallmodel_id').prop("checked","checked");
				}else{
				$('#selectallmodel_id').removeAttr("checked");
				}
			});
			
			$("#selectallstatus_id").click(function(){
				$('.casestatus_id').prop('checked',this.checked);
			});
			
			$(".casestatus_id").click(function(){
				if($(".casestatus_id").length == $(".casestatus_id:checked").length){
					$('#selectallstatus_id').prop("checked","checked");
				}else{
				$('#selectallstatus_id').removeAttr("checked");
				}
			});	
			
			$("#selectallstage_id").click(function(){
				$('.casestage_id').prop('checked',this.checked);
			});
			
			$(".casestage_id").click(function(){
				if($(".casestage_id").length == $(".casestage_id:checked").length){
					$('#selectallstage_id').prop("checked","checked");
				}else{
				$('#selectallstage_id').removeAttr("checked");
				}
			});
			
			$("#selectallpriority_id").click(function(){
				$('.casepriority_id').prop('checked',this.checked);
			});
			
			$(".casepriority_id").click(function(){
				if($(".casepriority_id").length == $(".casepriority_id:checked").length){
					$('#selectallpriority_id').prop("checked","checked");
				}else{
				$('#selectallpriority_id').removeAttr("checked");
				}
			});
			
			$("#selectallexec_id").click(function(){
				$('.caseexec_id').prop('checked',this.checked);
			});
			
			$(".caseexec_id").click(function(){
				if($(".caseexec_id").length == $(".caseexec_id:checked").length){
					$('#selectallexec_id').prop("checked","checked");
				}else{
				$('#selectallexec_id').removeAttr("checked");
				}
			});
			
			$("#selectallsoe_id").click(function(){
				$('.casesoe_id').prop('checked',this.checked);
			});
			
			$(".casesoe_id").click(function(){
				if($(".casesoe_id").length == $(".casesoe_id:checked").length){
					$('#selectallsoe_id').prop("checked","checked");
				}else{
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