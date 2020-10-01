<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Vehicle_Dash"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<%
	if (!mybean.modal.equals("yes")) {
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
<!-- tag CSS -->
<link href="../assets/css/bootstrap-tagsinput.css" rel="stylesheet" type="text/css" />
<!-- tag CSS -->

<%
	}
%>
<!-- <style>
.p-update{
 right:610px;
 top: 17px;
}

</style> -->
</HEAD>

<body onLoad="Contactableband();Serviceactionband();Pickupband();wrong_no();competitor();lostCaseReason();"
	class="page-container-bg-solid page-header-menu-fixed">
	<%
		if (!mybean.modal.equals("yes")) {
	%>
	<%@include file="../portal/header.jsp"%>
	<%
		}
	%>

	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1> Vehicle Dashboard &gt; Vehicle ID:&nbsp;<%=mybean.veh_id%></h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<%
						if (!mybean.modal.equals("yes")) {
					%>
						<ul class="page-breadcrumb breadcrumb">
							<li><a href="../portal/home.jsp">Home</a> &gt;</li>
							<li><a href="../service/index.jsp">Service</a> &gt;</li>
							<li><a href="../service/vehicle.jsp">Vehicles</a> &gt;</li>
							<li><a href="../service/vehicle-list.jsp?all=yes">List Vehicles</a> &gt;</li>
							<li><a href="../service/vehicle-list.jsp?veh_id=<%=mybean.veh_id%>">Vehicle ID:&nbsp;<%=mybean.veh_id%></a>&nbsp;</li> 
						</ul>
					<%
						}
					%>
					<!-- END PAGE BREADCRUMBS -->

					<div class="page-content-inner">

						<!-- 			TAGS START -->
						<CENTER>
							<div class="tag_class" id="customer_tagclass">
								<div class="bs-docs-example" id="bs-docs-example">
									<input type="text" class="form-control" id="enquiry_tags" name="enquiry_tags" />
									<a href="#" id="popover" data-placement="bottom" class="btn btn-success btn-md">
									<span  style="font-size: 20px;margin-top: 5px" class="fa fa-angle-down"></span></a>
<%-- 										<%if(mybean.comp_id.equals("1000") && mybean.AppRun().equals("1")){ %> --%>
									<a href="../portal/canned.jsp?canned=yes&service=yes&veh_id=<%=mybean.veh_id%>" class="btn btn-success btn-lg"  
										data-target="#Hintclicktocall" data-toggle="modal" style="margin-top: 1px;">
									<large><span  style="font-size: 20px;margin-top: 5px" class="glyphicon glyphicon-envelope"></span></large>&nbsp; Messages</a>
									<a href="../service/coupon-issue.jsp?couponissue=yes&dept=3&brand_id=<%=mybean.branch_brand_id %>"
										class="btn btn-success btn-lg" data-target="#Hintclicktocall" data-toggle="modal" style="margin-top: 1px;">
									<large><span  style="font-size: 20px;margin-top: 5px" class="fa fa-ticket"></span></large>&nbsp;Issue Coupon</a>
<%-- 								<%} %> --%>
								</div>
							</div>
							<div class="hint" id="hint_enquiry_tags"></div>

							<div id="popover-head" class="hide">
								<center>Tag List</center>
							</div>
							<div id="popover-content" class="hide">
								<%=mybean.tagcheck.PopulateTagsPopover( mybean.veh_customer_id, mybean.comp_id)%>
							</div>
						</CENTER>

						<!-- 			TAGS END -->


						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<input type="hidden" name="txt_veh_id" id="txt_veh_id"
								value="<%=mybean.veh_id%>"> <input type="hidden"
								name="txt_customer_id" id="txt_customer_id"
								value="<%=mybean.veh_customer_id%>">
							<div >
								<ul class="nav nav-tabs">
									<li class="active"><a href="#tabs-1" data-toggle="tab">Vehicle Info</a></li>
									<li><a href="#tabs-2" data-toggle="tab">Customer</a></li>
									<li><a href="#tabs-3" data-toggle="tab">Ownership</a></li>
									<li><a href="#tabs-4" data-toggle="tab">Follow-up</a></li>
									<li><a href="#tabs-5" data-toggle="tab">Job Cards</a></li>
									<li><a href="#tabs-6" data-toggle="tab">Invoices</a></li>
									<li><a href="#tabs-7" data-toggle="tab">Receipts</a></li>
									<li><a href="#tabs-8" data-toggle="tab">Stock Status</a></li>
									<li><a href="#tabs-9" data-toggle="tab">Documents</a></li>
									<li><a href="#tabs-10" data-toggle="tab">Tickets</a></li>
									<li><a href="#tabs-11" data-toggle="tab">PSF</a></li>
									<li><a href="#tabs-12" data-toggle="tab">History</a></li>

								</ul>
								<div class="tab-content">
									<div class="tab-pane active" id="tabs-1">
										<div class="portlet box  ">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">Vehicle
													Details</div>
											</div>
											<div class="portlet-body portlet-empty container-fluid">
												<!-- START PORTLET BODY -->
												<form class="form-horizontal" action="">
													<div class="form-element6 form-align">
														<label >Vehicle ID:&nbsp;</label>
															<a href="../service/vehicle-list.jsp?veh_id=<%=mybean.veh_id%>"><%=mybean.veh_id%></a>
													</div>
													<div class="form-element6 form-align">
														<label >Branch:&nbsp;</label>
															<a href="../portal/branch-summary.jsp?branch_id=<%=mybean.veh_branch_id%>"><%=mybean.branch_name%></a>
															<input type="hidden" id="dr_branch" value="<%=mybean.veh_branch_id %>" />
													</div>
													<div class="row">
														<div class="form-element6 ">
															<label> Variant <font color="#ff0000">*</font>: </label>
															<select class="form-control select2" id="servicepreownedvariant" name="servicepreownedvariant"
															onchange="SecurityCheck('servicepreownedvariant',this,'hint_servicepreownedvariant');">
																<%=mybean.variantcheck.PopulateVariant(mybean.veh_variant_id,mybean.comp_id)%>
															</select>
															<div class="hint" id="hint_servicepreownedvariant"></div>
														</div>
													
														<div class="form-element6">
															<label >Exterior<font color="#ff0000">*</font>:&nbsp; </label>
																<select name="dr_exterior_id" class="form-control"
																	id="dr_exterior_id"
																	onchange="SecurityCheck('dr_exterior_id', this, 'hint_dr_exterior_id');">
																	<%=mybean.PopulateExterior(mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_dr_exterior_id"></div>
														</div>

													</div>
													<div class="row">
														<div class="form-element6">
															<label >Interior<font color="#ff0000">*</font>:&nbsp; </label>
																<select name="dr_interior_id" class="form-control"
																	id="dr_interior_id"
																	onchange="SecurityCheck('dr_interior_id', this, 'hint_dr_interior_id');">
																	<%=mybean.PopulateInterior(mybean.comp_id)%>
																</select>
																<div class="hint" id="hint_dr_interior_id"></div>
														</div>
														<div class="form-element6">
														<label >Reg. No.<font color="#ff0000">*</font>:&nbsp; </label>
															<input name="txt_veh_reg_no" id="txt_veh_reg_no"
																type="text" class="form-control"
																value="<%=mybean.veh_reg_no%>" size="25" maxlength="20"
																onchange="SecurityCheck('txt_veh_reg_no',this,'hint_txt_veh_reg_no')" />
															<div class="hint" id="hint_txt_veh_reg_no"></div>
														</div>
													</div>
													<div class="row">
														<div class="form-element6">
															<label >Model Year<font color="#ff0000">*</font>:&nbsp; </label>
																<input name="txt_veh_modelyear" id="txt_veh_modelyear"
																	type="text" class="form-control yearpicker"
																	value="<%=mybean.veh_modelyear%>" size="10"
																	maxlength="4"
																	onchange="SecurityCheck('txt_veh_modelyear',this,'hint_txt_veh_modelyear')" />
																<div class="hint" id="hint_txt_veh_modelyear"></div>
														</div>
													
														<div class="form-element6">
															<label >Sale Date<font color="#ff0000">*</font>:&nbsp; </label>
																<input name="txt_veh_sale_date" id="txt_veh_sale_date"
																	value="<%=mybean.veh_sale_date%>" size="12"
																	maxlength="10" class="form-control datepicker"
																	 type="text" 
																onChange="SecurityCheck('txt_veh_sale_date',this,'hint_txt_veh_sale_date')" />
	
																<div class="hint" id="hint_txt_veh_sale_date"></div>
														</div>
													</div>
													
													<div class="row"> 

													<div class="form-element6">
														<label >Sale Amount:&nbsp; </label> 
															<input name="txt_veh_sale_amount"
																id="txt_veh_sale_amount" type="text"
																class="form-control" value="<%=mybean.veh_sale_amount%>"
																onkeyup="toInteger('txt_veh_sale_amount','Sale Amount')"
																size="20" maxlength="11"
																onchange="SecurityCheck('txt_veh_sale_amount',this,'hint_txt_veh_sale_amount')" />
															<div class="hint" id="hint_txt_veh_sale_amount"></div>
													</div>
												

													<div class="form-element6">
														<label>Veh. Source:&nbsp; </label>
															<select class="form-control" name="dr_veh_vehsource_id"
																id="dr_veh_vehsource_id"
																onChange="SecurityCheck('dr_veh_vehsource_id',this,'hint_dr_veh_vehsource_id');">
																<%=mybean.PopulateVehSource(mybean.comp_id)%>
															</select>
															<div class="hint" id="hint_dr_veh_vehsource_id"></div>
													</div>
													</div>
													<div class="row">
														<div class="form-element6">
															<label >Chassis Number<font color="#ff0000">*</font>:&nbsp;
															</label>
																<input name="txt_veh_chassis_no" id="txt_veh_chassis_no"
																	type="text" class="form-control"
																	value="<%=mybean.veh_chassis_no%>" size="25"
																	maxlength="17"
																	onChange="SecurityCheck('txt_veh_chassis_no',this,'hint_txt_veh_chassis_no')" />
																<div class="hint" id="hint_txt_veh_chassis_no"></div>
														</div>
													
														<div class="form-element3">
															<label>Engine Number<font color="#ff0000">*</font>:&nbsp; </label>
																<input name="txt_veh_engine_no" id="txt_veh_engine_no"
																	type="text" class="form-control"
																	value="<%=mybean.veh_engine_no%>" size="20"
																	maxlength="14"
																	onchange="SecurityCheck('txt_veh_engine_no',this,'hint_txt_veh_engine_no')" />
																<div class="hint" id="hint_txt_veh_engine_no"></div>
														</div>
														<div class="form-element3">
															<label>FASTag ID:&nbsp; </label>
															<input name="txt_veh_fastag" id="txt_veh_fastag" type="text" class="form-control"
																value="<%=mybean.veh_fastag%>" size="25" maxlength="20"
																onchange="SecurityCheck('txt_veh_fastag',this,'hint_txt_veh_fastag')" />
															<div class="hint" id="hint_txt_veh_fastag"></div>
														</div>
													</div>
													<div class="row">
														<div class="form-element6">
															<label >Last Sevice Date:&nbsp; </label> 
															<input name="txt_veh_lastservice" id="txt_veh_lastservice" type="text" maxlength="10"
																class="form-control datepicker" value="<%=mybean.veh_lastservice%>" size="12" 
																onchange="SecurityCheck('txt_veh_lastservice',this,'hint_txt_veh_lastservice')" />
															<div class="hint" id="hint_txt_veh_lastservice"></div>
														</div>
														
														<div class="form-element6">
															<label >Last Sevice Kms:&nbsp; </label>
																<input name="txt_veh_lastservice_kms"
																	id="txt_veh_lastservice_kms" type="text"
																	class="form-control"
																	value="<%=mybean.veh_lastservice_kms%>" size="12"
																	maxlength="10"
																	onchange="SecurityCheck('txt_veh_lastservice_kms',this,'hint_txt_veh_lastservice_kms')" />
																<div class="hint" id="hint_txt_veh_lastservice_kms"></div>
														</div>
													</div>
													<div class="row">
														<div class="form-element6">
															<label >Service Due Kms:&nbsp; </label>
															<input name="txt_veh_service_duekms" id="txt_veh_service_duekms" type="text"
																class="form-control" value="<%=mybean.veh_service_duekms%>" size="25"
																maxlength="17" onkeyup="toInteger(this.id);" 
															onChange="SecurityCheck('txt_veh_service_duekms',this,'hint_txt_veh_service_duekms')"/>
															<div class="hint" id="hint_txt_veh_service_duekms"></div>
														</div>
													
														<div class="form-element3">
															<label >Service Due Date:&nbsp;</label>
															<input name="txt_veh_service_duedate" id="txt_veh_service_duedate" type="text" size="12"
																class="form-control datepicker" value="<%=mybean.veh_service_duedate%>" maxlength="10"
																onchange="SecurityCheck('txt_veh_service_duedate',this,'hint_txt_veh_service_duedate')" />
															<div class="hint" id="hint_txt_veh_service_duedate"></div>
														</div>
														
														<div class="form-element3">
															<label >Warranty Expiry Date:&nbsp;</label>
															<input name="txt_veh_warranty_expirydate" type="text" class="form-control datepicker" maxlength="10" 
																id="txt_veh_warranty_expirydate" value="<%=mybean.vehwarrantyexpirydate%>" size="12"
																onchange="SecurityCheck('txt_veh_warranty_expirydate',this,'hint_veh_warranty_expirydate')" />
															<div class="hint" id="hint_veh_warranty_expirydate"></div>
														</div>
														
													</div>
													<div class="row">
														<div class="form-element6">
															<label >Service Advisor<font color="#ff0000">*</font>:&nbsp; </label>
															<select id="dr_veh_emp_id" name="dr_veh_emp_id" class="form-control"
																onChange="SecurityCheck('dr_veh_emp_id',this,'hint_dr_veh_emp_id')">
																<%=mybean.PopulateServiceExecutive(mybean.comp_id)%>
															</select>
															<div class="hint" id="hint_dr_veh_emp_id"></div>
														</div>
														
														<div class="form-element3 form-element-margin">
															<label>Classified: </label>
															<input id="chk_veh_classified" type="checkbox" name="chk_veh_classified"
																onChange="SecurityCheck('chk_veh_classified',this,'hint_chk_veh_classified')"
																<%=mybean.PopulateCheck(mybean.veh_classified)%> />
															<div class="hint" id="hint_chk_veh_classified"></div>	
														</div>
													</div>
												</form>
											</div>
										</div>


										<div class="portlet box  ">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">Customer Details</div>
											</div>
											<div class="portlet-body portlet-empty container-fluid">
												<form class="form-horizontal">
												<div class="row">

													<div class="form-element6">
														<label >Customer<font color="#ff0000">*</font>:&nbsp; </label>
															<input name="txt_veh_customer_name" type="text"
																class="form-control" id="txt_veh_customer_name"
																value="<%=mybean.veh_customer_name%>" size="70"
																maxlength="255"
																onChange="SecurityCheck('txt_veh_customer_name',this,'hint_txt_veh_customer_name')" />
															<div class="hint" id="hint_txt_veh_customer_name"></div>
													</div>
													<div class="form-element6">
														<div class="form-element2 form-element" id="emprows">
														<label > Contact<font color=red>*</font>:&nbsp; </label>
																<select name="dr_title" class="form-control"
																	id="dr_title"
																	onchange="SecurityCheck('dr_title',this,'hint_dr_title')">
																	<%=mybean.PopulateTitle(mybean.comp_id, mybean.contact_title_id)%>
																</select> Title
																<div class="hint" id="hint_dr_title"></div>
																</div>
															<div class="form-element5 form-element-margin element-padding">
																<input name="txt_contact_fname" type="text"
																	class="form-control" id="txt_contact_fname"
																	value="<%=mybean.contact_fname%>" size="32"
																	maxlength="255"
																	onchange="SecurityCheck('txt_contact_fname',this,'hint_txt_contact_fname')" />First
																Name
																<div class="hint" id="hint_txt_contact_fname"></div>
															</div>
															<div class="form-element5 form-element-margin form-element">
																<input name="txt_contact_lname" type="text"
																	class="form-control" id="txt_contact_lname"
																	value="<%=mybean.contact_lname%>" size="32"
																	maxlength="255"
																	onchange="SecurityCheck('txt_contact_lname',this,'hint_txt_contact_lname')" />Last
																Name
																<div class="hint" id="hint_txt_contact_lname"></div>
															</div>
													</div>
													</div><div class="row">
<%-- 													<% --%>
<!-- // 														if (!mybean.contact_mobile1.equals("")) { -->
<%-- 													%> --%>
<!-- 													<div class="form-element6 form-element"> -->
<!-- 															<div class="form-element8 form-element-margin"> -->
<!-- 															<label >Mobile 1:&nbsp; </label> -->
<%-- 																<%=mybean.contact_mobile1%><%=mybean.ClickToCall(mybean.contact_mobile1,mybean.comp_id)%> --%>
<!-- 															</div> -->

<!-- 															<div class="form-element4  form-element-margin"> -->
<!-- 																<select name="dr_new_phonetype_id1" -->
<!-- 																	class="form-control " id="dr_new_phonetype_id1" style="width: 155px;" -->
<!-- 																	onchange="SecurityCheck('dr_new_phonetype_id1',this,'hint_dr_new_phonetype_id1')"> -->
<%-- 																	<%=mybean.PopulatePhoneType(mybean.comp_id,mybean.contact_mobile1_phonetype_id)%> --%>
<!-- 																</select> -->
<!-- 																<div class="hint" id="hint_dr_new_phonetype_id1"></div> -->
<!-- 															</div> -->
<!-- 													</div> -->
<%-- 													<% --%>
<!-- // 														} -->
<%-- 													%> --%>
<%-- 													<% --%>
<!-- // 														if (mybean.contact_mobile1.equals("")) { -->
<%-- 													%> --%>
													<div class="form-element6 form-element">
													<div class="form-element8">
														<label >Mobile 1 :&nbsp;</label>
															<input name="txt_contact_mobile1" type="text"
																class="form-control" id="txt_contact_mobile1"
																value="<%=mybean.contact_mobile1%>" size="32"
																maxlength="13"
																onKeyUp="toPhone('txt_contact_mobile1','Mobile 1')"
																onchange="SecurityCheck('txt_contact_mobile1',this,'hint_txt_contact_mobile1')" />
															<span>(91-9999999999)</span>
														<%
															if (!mybean.contact_mobile1.equals("")) {
														%>
														<%=mybean.ClickToCall(mybean.contact_mobile1,mybean.comp_id)%>
														<%
															} 
														%>
															<div class="hint p-update " id="hint_txt_contact_mobile1"></div>
													</div>
														
														<div class="form-element4 form-element form-element-margin" >
															<select name="dr_new_phonetype_id1"
																class="form-control "
																id="dr_new_phonetype_id1" 
																onchange="SecurityCheck('dr_new_phonetype_id1',this,'hint_dr_new_phonetype_id1')">
																<%=mybean.PopulatePhoneType(mybean.comp_id,mybean.contact_mobile1_phonetype_id)%>
															</select>
															<div class="hint" id="hint_dr_new_phonetype_id1"></div>
														</div>
													</div>
<%-- 													<% --%>
<!-- // 														} -->
<%-- 													%> --%>
<%-- 													<% --%>
<!-- // 														if (!mybean.contact_mobile2.equals("")) { -->
<%-- 													%> --%>
<!-- 													<div class="form-element6 form-element"> -->
<!-- 															<div class="form-element8 form-element-margin"> -->
<!-- 															<label >Mobile 2:&nbsp; </label> -->
<%-- 																<%=mybean.contact_mobile2%><%=mybean.ClickToCall(mybean.contact_mobile2, mybean.comp_id)%> --%>
<!-- 															</div> -->
<!-- 															<div class="form-element4  form-element-margin"> -->
<!-- 																<select name="dr_new_phonetype_id2" style="width: 155px;" -->
<!-- 																	class="form-control " id="dr_new_phonetype_id2" -->
<!-- 																	onchange="SecurityCheck('dr_new_phonetype_id2',this,'hint_dr_new_phonetype_id2')"> -->
<%-- 																	<%=mybean.PopulatePhoneType(mybean.comp_id, mybean.contact_mobile2_phonetype_id)%> --%>
<!-- 																</select> -->
<!-- 																<div class="hint" id="hint_dr_new_phonetype_id2"></div> -->
<!-- 															</div> -->
<!-- 													</div> -->
<%-- 													<% --%>
<!-- // 														} -->
<%-- 													%> --%>

<%-- 													<% --%>
<!-- // 														if (mybean.contact_mobile2.equals("")) { -->
<%-- 													%> --%>
													<div class="form-element6 form-element">
														<div class="form-element8">
														<label >Mobile 2 :&nbsp;</label>
															<input name="txt_contact_mobile2" type="text"
																class="form-control" id="txt_contact_mobile2"
																value="<%=mybean.contact_mobile2%>" size="32"
																maxlength="13"
																onKeyUp="toPhone('txt_contact_mobile2','Mobile 2')"
																onchange="SecurityCheck('txt_contact_mobile2',this,'hint_txt_contact_mobile2')" />
															<span>(91-9999999999)</span>

														<%
															if (!mybean.contact_mobile2.equals("")) {
														%>
														<%=mybean.ClickToCall(mybean.contact_mobile2, mybean.comp_id)%>
														<%
															} 
														%>
														<div class="hint p-update " id="hint_txt_contact_mobile2"></div>
														</div>
														<div class="form-element4 form-element-margin">
															<select name="dr_new_phonetype_id2"
																class="form-control "
																id="dr_new_phonetype_id2"
																onchange="SecurityCheck('dr_new_phonetype_id2',this,'hint_dr_new_phonetype_id2')">
																<%=mybean.PopulatePhoneType(mybean.comp_id, mybean.contact_mobile2_phonetype_id)%>
															</select>
															<div class="hint " id="hint_dr_new_phonetype_id2"></div>
														</div>

													</div>
<%-- 													<% --%>
<!-- // 														} -->
<%-- 													%> --%>
													</div>
													
													
													<div class="row">
													<div class="form-element6 form-element">
														<div class="form-element8">
														<label >Mobile 3 :&nbsp;</label>
															<input name="txt_contact_mobile3" type="text"
																class="form-control" id="txt_contact_mobile3"
																value="<%=mybean.contact_mobile3%>" size="32"
																maxlength="13"
																onKeyUp="toPhone('txt_contact_mobile3','Mobile 3')"
																onchange="SecurityCheck('txt_contact_mobile3',this,'hint_txt_contact_mobile3')" />
															<span>(91-9999999999)</span>
															<%
															if (!mybean.contact_mobile3.equals("")) {
														%>
														<%=mybean.ClickToCall(mybean.contact_mobile3, mybean.comp_id)%>
														<%
															} 
														%>
														<div class="hint p-update" id="hint_txt_contact_mobile3"></div>
														</div>
														<div class="form-element3 form-element-margin" >
															<select name="dr_new_phonetype_id3" 
																class="form-control "
																id="dr_new_phonetype_id3"
																onchange="SecurityCheck('dr_new_phonetype_id3',this,'hint_dr_new_phonetype_id3')">
																<%=mybean.PopulatePhoneType(mybean.comp_id, mybean.contact_mobile3_phonetype_id)%>
															</select>
															<div class="hint" id="hint_dr_new_phonetype_id3"></div>
														</div>
													</div>

													<div class="form-element6 form-element">
														<div class="form-element8">
														<label >Mobile 4 :&nbsp; </label>
															<input name="txt_contact_mobile4" type="text"
																class="form-control" id="txt_contact_mobile4"
																value="<%=mybean.contact_mobile4%>" size="32"
																maxlength="13"
																onKeyUp="toPhone('txt_contact_mobile4','Mobile 4')"
																onchange="SecurityCheck('txt_contact_mobile4',this,'hint_txt_contact_mobile4')" />
															<span>(91-9999999999)</span>
														<%
															if (!mybean.contact_mobile4.equals("")) {
														%>
														<%=mybean.ClickToCall(mybean.contact_mobile4, mybean.comp_id)%>
														<%
															}
														%>
														<div class="hint p-update " id="hint_txt_contact_mobile4"></div>
														</div>
														<div class="form-element4 form-element-margin" >
															<select name="dr_new_phonetype_id4"
																class="form-control "
																id="dr_new_phonetype_id4"
																onchange="SecurityCheck('dr_new_phonetype_id4',this,'hint_dr_new_phonetype_id4')">
																<%=mybean.PopulatePhoneType(mybean.comp_id, mybean.contact_mobile4_phonetype_id)%>
															</select>
															<div class="hint " id="hint_dr_new_phonetype_id4"></div>
														</div>
														
													</div></div>
													<div class="row">
													<div class="form-element6 form-element">
														<div class="form-element8">
														<label >Mobile 5 :&nbsp;</label>
															<input name="txt_contact_mobile5" type="text"
																class="form-control" id="txt_contact_mobile5"
																value="<%=mybean.contact_mobile5%>" size="32"
																maxlength="13"
																onKeyUp="toPhone('txt_contact_mobile5','Mobile 5')"
																onchange="SecurityCheck('txt_contact_mobile5',this,'hint_txt_contact_mobile5')" />
															<span>(91-9999999999)</span>
														<%
															if (!mybean.contact_mobile5.equals("")) {
														%>
														<%=mybean.ClickToCall(mybean.contact_mobile5, mybean.comp_id)%>
														<%
															} 
														%>
														<div class="hint p-update " id="hint_txt_contact_mobile5"></div>
														</div>
														<div class="form-element4 form-element-margin" >
															<select name="dr_new_phonetype_id5" 
																class="form-control "
																id="dr_new_phonetype_id5" 
																onchange="SecurityCheck('dr_new_phonetype_id5',this,'hint_dr_new_phonetype_id5')">
																<%=mybean.PopulatePhoneType(mybean.comp_id, mybean.contact_mobile5_phonetype_id)%>
															</select>
															<div class="hint " id="hint_dr_new_phonetype_id5"></div>
														</div>
													</div>
													<div class="form-element6 form-element">
														<div class="form-element8">
															<label >Mobile 6 :&nbsp;</label>
															<input name="txt_contact_mobile6" type="text"
																class="form-control" id="txt_contact_mobile6"
																value="<%=mybean.contact_mobile6%>" size="32"
																maxlength="13"
																onKeyUp="toPhone('txt_contact_mobile6','Mobile 6')"
																onchange="SecurityCheck('txt_contact_mobile6',this,'hint_txt_contact_mobile6')" />
															<span>(91-9999999999)</span>
														</div>

														<%
															if (!mybean.contact_mobile6.equals("")) {
														%>
														<div class="txt-align col-xs-1" id="picon">
														<%=mybean.ClickToCall(mybean.contact_mobile6, mybean.comp_id)%></div>
														<%
															} 
														%>
														<div class="form-element4 form-element-margin" >
															<select name="dr_new_phonetype_id6" 
																class="form-control "
																id="dr_new_phonetype_id6"
																onchange="SecurityCheck('dr_new_phonetype_id6',this,'hint_dr_new_phonetype_id6')">
																<%=mybean.PopulatePhoneType(mybean.comp_id, mybean.contact_mobile6_phonetype_id)%>
															</select>
															<div class="hint " id="hint_dr_new_phonetype_id6"></div>
														</div>
														<div class="hint p-update "
															id="hint_txt_contact_mobile6"></div>

													</div></div>
													<div class="row">
													<div class="form-element6">
														<label >Phone 1<font color="#ff0000">*</font>:&nbsp;
														</label>
															<input name="txt_contact_phone1" type="text"
																class="form-control" id="txt_contact_phone1"
																value="<%=mybean.contact_phone1%>" size="35"
																maxlength="14"
																onchange="SecurityCheck('txt_contact_phone1',this,'hint_txt_contact_phone1')" />
															(91-80-33333333)
															<div class="hint" id="hint_txt_contact_phone1"></div>
													</div>


													<div class="form-element6">
														<label >Email 1<font color="#ff0000">*</font>:&nbsp;
														</label>
															<input name="txt_contact_email1" type="text"
																class="form-control" id="txt_contact_email1"
																value="<%=mybean.contact_email1%>" size="35"
																maxlength="100"
																onchange="SecurityCheck('txt_contact_email1',this,'hint_txt_contact_email1')" />
															<div class="hint" id="hint_txt_contact_email1"></div>
													</div>
												</div><div class="row">
													<div class="form-element6">
														<label >Address:&nbsp; </label>
															<textarea name="txt_contact_address" cols="40" rows="4"
																class="form-control" id="txt_contact_address"
																onchange="SecurityCheck('txt_contact_address',this,'hint_txt_contact_address')"
																onkeyup="charcount('txt_contact_address', 'span_txt_contact_address','&lt;font color=red&gt;({CHAR} characters left)&lt;/font&gt;', '255')"><%=mybean.contact_address%></textarea>
															<span id="span_txt_contact_address"> (255
																Characters)</span>
															<div class="hint" id="hint_txt_contact_address"></div>
													</div>

													<div class="form-element6">
														<label >City<font color="#ff0000">*</font>:&nbsp;
														</label>
															<select name="dr_city_id" id="dr_city_id"
																class="form-control"
																onchange="SecurityCheck('dr_city_id',this,'hint_dr_city_id')">
																<%=mybean.PopulateCity(mybean.comp_id)%>
															</select>
															<div class="hint" id="hint_dr_city_id"></div>
													</div>

													</div><div class="row">

													<div class="form-element6">
														<label >Pin/Zip<font
															color="#ff0000">*</font>:&nbsp;
														</label>
															<input name="txt_contact_pin" type="text"
																class="form-control" id="txt_contact_pin"
																onkeyup="toInteger('txt_contact_pin','Pin')"
																onchange="SecurityCheck('txt_contact_pin',this,'hint_txt_contact_pin')"
																value="<%=mybean.contact_pin%>" size="10" maxlength="6" />
															<div class="hint" id="hint_txt_contact_pin"></div>
															<input name="veh_id" type="hidden" id="veh_id"
																value="<%=mybean.veh_id%>" />
													</div>

													<div class="form-element6">
														<label >Notes:&nbsp;
														</label>
															<textarea name="txt_veh_notes" cols="50" rows="5"
																class="form-control" id="txt_veh_notes"
																onChange="SecurityCheck('txt_veh_notes',this,'hint_txt_veh_notes')"><%=mybean.veh_notes%></textarea>
															<div class="hint" id="hint_txt_veh_notes"></div>
															<%
																if (!mybean.option.equals("")) {
															%>
															Options:&nbsp;
															<%
																}
															%>
															<%=mybean.option%>
													</div>
														</div>
													<div class="form-element6">
														<label >Entry
															By:&nbsp; </label>
															<%=mybean.veh_entry_by%>
															<input type="hidden" name="entry_by"
																value="<%=mybean.veh_entry_by%>">
													</div>

													<div class="form-element6">
														<label >Entry Date: </label>
															<%=mybean.veh_entry_date%>
															<input type="hidden" name="entry_by"
																value="<%=mybean.veh_entry_date%>">
													</div>

													<%
														if (mybean.veh_modified_by != null
																&& !mybean.veh_modified_by.equals("")) {
													%>

													<div class="form-element6">
														<label >Modified
															By:</label>
															<%=mybean.veh_modified_by%>
															<input type="hidden" name="modified_by"
																value="<%=mybean.veh_modified_by%>">
													</div>
													<div class="form-element6">
														<label >Modified
															Date:</label>
															<%=mybean.veh_entry_date%>
															<input type="hidden" name="veh_entry_date"
																value="<%=mybean.veh_entry_date%>">
													</div>

													<%
														}
													%>
												</form>
											</div>

										</div>
									</div>

									<div class="tab-pane" id="tabs-2">
										<form class="form-horizontal" name="Frmcontact" method="post"
											action="vehicle-dash.jsp?veh_id=<%=mybean.veh_id%>#tabs-2">


											<div class="portlet box  ">
												<div class="portlet-title" style="text-align: center">
													<div class="caption" style="float: none">Customer
														Details</div>
												</div>
												<div class="portlet-body portlet-empty">

													<%=mybean.StrCustomerDetails%>
													<%=mybean.customerdetail%>

												</div>
											</div>


											<div class="portlet box  ">
												<div class="portlet-title" style="text-align: center">
													<div class="caption" style="float: none">Add Contact
														Person</div>
												</div>
												<div class="portlet-body portlet-empty container-fluid">
													<center>
														<font color="red"><%=mybean.contact_msg%></font>
													</center>
												<div class="row">
													<div class="form-element12">
													<div class="form-element2 form-element">
														<label > Contact<font color=red>*</font>:&nbsp; </label>
																<select name="dr_new_contact_title_id"
																	class="form-control " id="dr_new_contact_title_id">
																	<%=mybean.PopulateTitle(mybean.comp_id, mybean.new_contact_title_id)%>
																</select> Title
															</div>
															<div class="form-element5 form-element-margin ">
																<input name="txt_new_contact_fname" type="text"
																	class="form-control" id="txt_new_contact_fname"
																	value="<%=mybean.new_contact_fname%>" size="32"
																	maxlength="255" />First Name
															</div>
															<div class="form-element5 form-element-margin form-element">
																<input name="txt_new_contact_lname" type="text"
																	class="form-control" id="txt_new_contact_lname"
																	value="<%=mybean.new_contact_lname%>" size="32"
																	maxlength="255" />Last Name
															</div>
														</div>

													</div>
													<div class="form-element6 form-element">
													<div class="form-element8">
														<label >Mobile 1:&nbsp; </label>
															<input name="txt_new_contact_mobile1" type="text"
																class="form-control" id="txt_new_contact_mobile1"
																value="<%=mybean.new_contact_mobile1%>" size="32"
																maxlength="13"
																onKeyUp="toPhone('txt_new_contact_mobile1','Mobile 1')" />
															(91-9999999999)
														</div>
														<div class="form-element4 form-element-margin" >
															<select name="dr_new_phonetype_id1"
																class="form-control "
																id="dr_new_phonetype_id1"
																onchange="SecurityCheck('dr_new_phonetype_id1',this,'hint_dr_new_phonetype_id1')">
																<%=mybean.PopulatePhoneType(mybean.comp_id, mybean.contact_mobile1_phonetype_id)%>
															</select>
															<div class="hint " id="hint_dr_new_phonetype_id1"></div>
														</div>
														<div class="col-md-2" id="hint_txt_contact_mobile1"></div>
													</div>

													<div class="form-element6 form-element">
													<div class="form-element8">
														<label >Mobile 2:&nbsp; </label>
															<input name="txt_new_contact_mobile2" type="text"
																class="form-control" id="txt_new_contact_mobile2"
																value="<%=mybean.new_contact_mobile2%>" size="32"
																maxlength="13"
																onKeyUp="toPhone('txt_new_contact_mobile2','Mobile 2')" />
															(91-9999999999)
														</div>
														<div class="form-element4 form-element-margin" >
															<select name="dr_new_phonetype_id2"
																class="form-control "
																id="dr_new_phonetype_id2"
																onchange="SecurityCheck('dr_new_phonetype_id2',this,'hint_dr_new_phonetype_id2')">
																<%=mybean.PopulatePhoneType(mybean.comp_id, mybean.contact_mobile2_phonetype_id)%>
															</select>
															<div class="hint " id="hint_dr_new_phonetype_id2"></div>
														</div>
														<div class="col-md-2" id="hint_txt_contact_mobile2"></div>
													</div>
													<div class="form-element6 form-element">
													<div class="form-element8">
														<label >Mobile 3:&nbsp; </label>
															<input name="txt_new_contact_mobile3" type="text"
																class="form-control" id="txt_new_contact_mobile3"
																value="<%=mybean.new_contact_mobile3%>" size="32"
																maxlength="13"
																onKeyUp="toPhone('txt_new_contact_mobile3','Mobile 3')" />
															(91-9999999999)
															</div>
														<div class="form-element4 form-element-margin">
															<select name="dr_new_phonetype_id3"
																class="form-control "
																id="dr_new_phonetype_id3"
																onchange="SecurityCheck('dr_new_phonetype_id3',this,'hint_dr_new_phonetype_id3')">
																<%=mybean.PopulatePhoneType(mybean.comp_id, mybean.contact_mobile3_phonetype_id)%>
															</select>
															<div class="hint " id="hint_dr_new_phonetype_id3"></div>
														</div>
														<div class="col-md-2" id="hint_txt_contact_mobile3"></div>
													</div>

													<div class="form-element6 form-element">
													<div class="form-element8 ">
														<label >Mobile 4:&nbsp; </label>
															<input name="txt_new_contact_mobile4" type="text"
																class="form-control" id="txt_new_contact_mobile4"
																value="<%=mybean.new_contact_mobile4%>" size="32"
																maxlength="13"
																onKeyUp="toPhone('txt_new_contact_mobile4','Mobile 4')" />
															(91-9999999999)
														</div>
														<div class="form-element4 form-element-margin">
															<select name="dr_new_phonetype_id4"
																class="form-control "
																id="dr_new_phonetype_id4"
																onchange="SecurityCheck('dr_new_phonetype_id4',this,'hint_dr_new_phonetype_id4')">
																<%=mybean.PopulatePhoneType(mybean.comp_id, mybean.contact_mobile4_phonetype_id)%>
															</select>
															<div class="hint " id="hint_dr_new_phonetype_id4"></div>
														</div>
														<div class="col-md-2" id="hint_txt_contact_mobile4"></div>
													</div>

													<div class="form-element6 form-element">
													<div class="form-element8 ">
														<label >Mobile 5:&nbsp; </label>
															<input name="txt_new_contact_mobile5" type="text"
																class="form-control" id="txt_new_contact_mobile5"
																value="<%=mybean.new_contact_mobile5%>" size="32"
																maxlength="13"
																onKeyUp="toPhone('txt_new_contact_mobile5','Mobile 5')" />
															(91-9999999999)
														</div>
														<div class="form-element4 form-element-margin">
															<select name="dr_new_phonetype_id5"
																class="form-control "
																id="dr_new_phonetype_id5"
																onchange="SecurityCheck('dr_new_phonetype_id5',this,'hint_dr_new_phonetype_id5')">
																<%=mybean.PopulatePhoneType(mybean.comp_id, mybean.contact_mobile5_phonetype_id)%>
															</select>
															<div class="hint " id="hint_dr_new_phonetype_id5"></div>
														</div>
														<div class="col-md-2" id="hint_txt_contact_mobile5"></div>
													</div>
													<div class="form-element6 form-element">
													<div class="form-element8">
														<label>Mobile 6:&nbsp; </label>
															<input name="txt_new_contact_mobile6" type="text"
																class="form-control" id="txt_new_contact_mobile6"
																value="<%=mybean.new_contact_mobile6%>" size="32"
																maxlength="13"
																onKeyUp="toPhone('txt_new_contact_mobile6','Mobile 6')" />
															(91-9999999999)
														</div>
														<div class="form-element4 form-element-margin">
															<select name="dr_new_phonetype_id6"
																class="form-control "
																id="dr_new_phonetype_id6"
																onchange="SecurityCheck('dr_new_phonetype_id6',this,'hint_dr_new_phonetype_id6')">
																<%=mybean.PopulatePhoneType(mybean.comp_id, mybean.contact_mobile6_phonetype_id)%>
															</select>
															<div class="hint " id="hint_dr_new_phonetype_id6"></div>
														</div>
														<div class="col-md-2" id="hint_txt_contact_mobile6"></div>
													</div>

													<div class="form-element6">
														<label >Email 1:&nbsp; </label>
															<input name="txt_new_contact_email1" type="text"
																class="form-control" id="txt_new_contact_email1"
																value="<%=mybean.new_contact_email1%>" size="32"
																maxlength="50" />
													</div>
													<div class="form-element6">
														<label >Notes:&nbsp; </label>
															<textarea name="txt_new_contact_notes" cols="70" rows="4"
																class="form-control" id="txt_new_contact_notes"><%=mybean.new_contact_notes%></textarea>
													</div>

													<div class="form-element6">
														<label >Type:&nbsp; </label>
															<select id="dr_new_contact_contacttype_id"
																name="dr_new_contact_contacttype_id"
																class="form-control">
																<%=mybean.PopulateContactType(mybean.comp_id)%>
															</select>
													</div>

													<center>
													<div class="form-element12">
														<input name="add_contact_button" type="submit"
															class="btn btn-success" id="add_contact_button"
															value="Add Contact" />
														</div>
													</center>
												</div>
											</div>
										</form>
									</div>
									
									<div class="tab-pane" id="tabs-3">
										<div class="portlet box  ">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">Ownership</div>
											</div>
											<div class="portlet-body portlet-empty">
												<div class="tab-pane" id="">
													<%=mybean.ListOwnershipData(mybean.comp_id, mybean.veh_id)%>
												</div>
											</div>
										</div>
									</div>

									<div class="tab-pane" id="tabs-4">
										<form class="form-horizontal" name="Frmtasks" id="Frmtasks"
											method="post"
											action="vehicle-dash.jsp?veh_id=<%=mybean.veh_id%>#tabs-4"
											onsubmit="return validation();">
											<div class="portlet box  ">
												<div class="portlet-title" style="text-align: center">
													<div class="caption" style="float: none"><%=mybean.status%>&nbsp;
														Follow-up
													</div>
												</div>
												<div class="portlet-body portlet-empty container-fluid">
													<center>
														<font color="red"><%=mybean.listfollowup_msg%></font>
													</center>
													<center><%=mybean.customerdetail%></center>
													<%--    <%if(mybean.status.equals("Update")){%> --%>
													<div class="form-element6">
														<label >CRM Executive: </label>
															<select name="dr_vehfollowup_crmexe_id"
																class="form-control" id="dr_vehfollowup_crmexe_id"
																onchange="SecurityCheck('dr_vehfollowup_crmexe_id',this,'hint_dr_vehfollowup_crmexe_id')">
																<option value=0>Select</option>
																<%=mybean.PopulateCRMExecutive(mybean.comp_id)%>
															</select>
															<div class="hint" id="hint_dr_vehfollowup_crmexe_id"></div>
														</div><div class="form-element6 form-element-margin">		
														<label >DMS Status: </label>
															<%=mybean.dmsstatus_name%>
															<input type="hidden" name="dmsstatus_name"
																value="<%=mybean.dmsstatus_name%>" /> 
														</div>
														<div class="row"></div>
														<div class="form-element3"></div>
													<div class="form-element6">
														<label >Contactable<font color="red">*</font>:&nbsp; </label>
															<select name="dr_vehfollowup_contactable_id"
																class="form-control" id="dr_vehfollowup_contactable_id"
																onChange="Contactableband();Serviceactionband();wrong_no();competitor();"
																visible="true">
																<option value=0>Select</option>
																<%=mybean.PopulateServiceContactable(mybean.comp_id, mybean.vehfollowup_contactable_id)%>
															</select>
													</div>
													<span id="errormsg" style="margin-left: 26%"></span>

													<!-- Start Contactable Showing Yes  -->
													<div id="contactableyesband">
														<div class="form-group">
															<label class="control-label col-md-3">Action<font
																color="red">*</font>:&nbsp;
															</label>
															<div class="col-md-6 " style="top: 8px">
																<select name="dr_vehfollowup_vehaction_id"
																	class="form-control" id="dr_vehfollowup_vehaction_id"
																	onchange="Serviceactionband()" visible="true">
																	<%=mybean.PopulateServiceAction(mybean.comp_id, mybean.vehfollowup_vehaction_id)%>
																</select>
															</div>
														</div>
														<span id="actionerrormsg" style="margin-left: 26%"></span>

														<div class="form-group">
															<label class="control-label col-md-3">Current
																Mileage :&nbsp;</label>
															<div class="col-md-6 " style="top: 8px">
																<input type="text" name="txt_vehfollowup_kms" size="20"
																	maxlength="9" class="form-control"
																	id="txt_vehfollowup_kms"
																	value="<%=mybean.vehfollowup_kms%>"
																	onkeyup="toPhone('txt_vehfollowup_kms','Vehicle Kms')" />
															</div>
														</div>
														<span style="margin-left: 26%"></span>
														<!-- Start Action Band -->
														<div id="actionband">
															<!-- Start Book A Service Band -->
															<div id="bookaserviceband">
																<div class="form-group">
																	<label class="control-label col-md-3">Workshop<font
																		color="red">*</font>:&nbsp;
																	</label>
																	<div class="col-md-6 " style="top: 8px">
																		<select name="dr_vehfollowup_workshop_branch_id"
																			class="form-control"
																			id="dr_vehfollowup_workshop_branch_id" visible="true">
																			<%=mybean.PopulateServiceWorkshopBranch(mybean.comp_id, mybean.vehfollowup_workshop_branch_id)%>
																		</select>
																	</div>
																</div>
																<span id="workshoperrormsg" style="margin-left: 26%"></span>

																<div class="form-group">
																	<label class="control-label col-md-3">Booking
																		Time<font color="red">*</font>:&nbsp;
																	</label>
																	<div class="col-md-6 " style="top: 8px">
																		<input name="txt_vehfollowup_appt_time" type="text"
																			class="form-control datetimepicker"
																			id="txt_vehfollowup_appt_time"
																			value="<%=mybean.vehfollowup_appt_time1%>" size="20"
																			maxlength="16">
																	</div>
																</div>
																<span id="datetimeerrormsg" style="margin-left: 26%"></span>

																<div class="form-group">
																	<label class="control-label col-md-3">Booking
																		Type<font color="red">*</font>:&nbsp;
																	</label>
																	<div class="col-md-6 " style="top: 8px">
																		<select name="dr_vehfollowup_bookingtype_id"
																			class="form-control" id="dr_vehfollowup_bookingtype_id"
																			Onchange="Pickupband();PopulateDriver()" visible="true">
																			<%=mybean.PopulateServiceBookingType(mybean.comp_id, mybean.vehfollowup_bookingtype_id)%>
																		</select>
																	</div>
																</div>
																<span id="bookingerrormsg" style="margin-left: 26%"></span>
																<!-- Start PickupBand  -->
																<div id="pickupband">
																	<div class="form-group">
																		<label class="control-label col-md-3">Driver<font
																			color="red">*</font>:&nbsp;
																		</label>
																		<div class="col-md-6 " style="top: 8px">
																			<select name="dr_vehfollowup_pickupdriver_emp_id"
																				class="form-control"
																				id="dr_vehfollowup_pickupdriver_emp_id"
																				visible="true">
																				<span id="HintDriver"> <%=mybean.vehicle.PopulateServicePickUp(mybean.comp_id)%>
																			</span>
																			</select>
																		</div>
																	</div>
																	<span id="drivererrormsg" style="margin-left: 26%"></span>

																	<div class="form-group">
																		<label class="control-label col-md-3">Other
																			Address<font color="red">*</font>:&nbsp;
																		</label>
																		<div class="col-md-6">
																			<textarea name="txt_vehfollowup_pickuplocation"
																				cols="40" rows="4" class="form-control"
																				id="txt_vehfollowup_pickuplocation"
																				onchange="SecurityCheck('txt_vehfollowup_pickuplocation',this,'hint_txt_vehfollowup_pickuplocation')"
																				onkeyup="charcount('txt_vehfollowup_pickuplocation', 'span_txt_vehfollowup_pickuplocation','&lt;font color=red&gt;({CHAR} characters left)&lt;/font&gt;', '255')"><%=mybean.vehfollowup_pickuplocation%></textarea>
																			<span id="span_txt_vehfollowup_pickuplocation">
																				(255 Characters)</span>
																			<div class="hint"
																				id="hint_txt_vehfollowup_pickuplocation"></div>
																		</div>
																	</div>
																</div>
																<!-- End PickupBand  -->
															</div>
															<!-- End Book A Service Band -->


															<div class="form-group" id="lostcaseband">
																<label class="control-label col-md-3">Lost Case<font
																	color="red">*</font>:&nbsp;
																</label>
																<div class="col-md-6 " style="top: 8px">
																	<select name="dr_vehlostcase1_id" class="form-control"
																		id="dr_vehlostcase1_id" visible="true"
																		onchange="Serviceactionband();">
																		<%=mybean.PopulateVehLostcase1(mybean.comp_id, mybean.vehlostcase1_id)%>
																	</select>
																</div>
															</div>
															<span id="lostcaseerrormsg" style="margin-left: 26%"></span>
														</div>
														<!-- End Action Band -->
														<div id="competitorband">
															<div class="form-group">
																<label class="control-label col-md-3">Competitor<font
																	color="red">*</font>:&nbsp;
																</label>
																<div class="col-md-6 " style="top: 8px">
																	<select name="dr_competitor_id" class="form-control"
																		id="dr_competitor_id" visible="true"
																		onchange="Followupvalidation();">
																		<%=mybean.PopulateCompetitor(mybean.comp_id, mybean.competitor_id)%>
																	</select>
																</div>
															</div>
															<span id="competitorerrormsg" style="margin-left: 26%"></span>

															<div class="form-group" id="lcReason1">
																<label class="control-label col-md-3">Lost Case
																	Reason<font color="red">*</font>:&nbsp;
																</label>
																<div class="col-md-6 " style="top: 8px">
																	<select name="dr_lostcase_reason1" class="form-control"
																		id="dr_lostcase_reason1" visible="true"
																		onchange="Followupvalidation();lostCaseReason();">
																		<%=mybean.PopulateCompetitorReason(mybean.comp_id, mybean.competitor_id)%>
																	</select>
																</div>
															</div>
															<span id="lc_reason_errormsg1" style="margin-left: 26%"></span>
														</div>
													</div>
													<!-- End Contactable Showing Yes  -->

													<div class="form-group" id="callatterband">
														<label class="control-label col-md-3" id="reason">Next
															Follow-up Time<font color="red">*</font>:&nbsp;
														</label> <label class="control-label col-md-3" id="wrong_no">Next
															Follow-up Time:&nbsp; </label>
														<div class="col-md-6 " style="top: 8px">
															<input name="txt_vehfollowup_time" type="text"
																class="form-control datetimepicker"
																id="txt_vehfollowup_time"
																value="<%=mybean.nextfollowup_time%>" size="20"
																maxlength="16">
														</div>
													</div>
													<span id="nextfollowerrormsg" style="margin-left: 26%"></span>

													<div class="form-group" id="reasonband">
														<label class="control-label col-md-3">Reason<font
															color="red">*</font>:&nbsp;
														</label>
														<div class="col-md-6 " style="top: 8px">
															<select name="dr_vehfollowup_notcontactable_id"
																class="form-control"
																id="dr_vehfollowup_notcontactable_id" visible="true"
																onchange="wrong_no();competitor();">
																<%=mybean.PopulateServiceNotContactable(mybean.comp_id, mybean.vehfollowup_notcontactable_id)%>
															</select>
														</div>
													</div>
													<span id="reasonerrormsg" style="margin-left: 26%"></span>

													<!-- For Wrong no. -->
													<div class="form-group" id="lostcaseband1">
														<label class="control-label col-md-3">Lost Case<font
															color="red">*</font>:&nbsp;
														</label>
														<div class="col-md-6 " style="top: 8px">
															<select name="dr_vehlostcase2_id" class="form-control"
																id="dr_vehlostcase2_id" visible="true"
																onchange="Serviceactionband();competitor();">
																<%=mybean.PopulateVehLostcase1(mybean.comp_id, mybean.vehlostcase1_id)%>
															</select>
														</div>
													</div>
													<span id="lostcaseerrormsg2" style="margin-left: 26%"></span>

													<div id="competitorband1">
														<div class="form-group">
															<label class="control-label col-md-3">Competitor<font
																color="red">*</font>:&nbsp;
															</label>
															<div class="col-md-6 " style="top: 8px">
																<select name="dr_competitor_id1" class="form-control"
																	id="dr_competitor_id1" visible="true"
																	onchange="Followupvalidation();lostCaseReason();">
																	<%=mybean.PopulateCompetitor(mybean.comp_id, mybean.competitor_id)%>
																</select>
															</div>
														</div>
														<span id="competitorerrormsg1" style="margin-left: 26%"></span>

														<div class="form-group" id="lcReason">
															<label class="control-label col-md-3">Lost Case
																Reason<font color="red">*</font>:&nbsp;
															</label>
															<div class="col-md-6 " style="top: 8px">
																<select name="dr_lostcase_reason" class="form-control"
																	id="dr_lostcase_reason" visible="true"
																	onchange="Followupvalidation();lostCaseReason();">
																	<%=mybean.PopulateCompetitorReason(mybean.comp_id, mybean.competitor_id)%>
																</select>
															</div>
														</div>
														<span id="lc_reason_errormsg" style="margin-left: 26%"></span>
													</div>

													<div class="form-group" id="feedbackband">
														<label class="control-label col-md-3">Feedback<font
															color="red">*</font>:&nbsp;
														</label>
														<div class="col-md-6 " style="top: 8px">
															<textarea name="txt_vehfollowup_desc" cols="50" rows="4"
																class="form-control" id="txt_vehfollowup_desc"
																onKeyUp="charcount('txt_vehfollowup_desc', 'span_txt_vehfollowup_desc','<font color=red>({CHAR} characters left)</font>', '1000')"><%=mybean.vehfollowup_desc%></textarea>
															<span id="span_txt_vehfollowup_desc">(1000
																characters)</span> <span id="feedbackerrormsg"
																style="margin-left: 26%"></span>
														</div>
													</div>

													<center>
														<input name="add_followup_button" type="submit"
															class="btn btn-success" id="add_followup_button"
															value="Add" /> <input type="hidden" name="veh_id"
															id="veh_id" value="<%=mybean.veh_id%>"/>
													</center>

												</div>


												<center><%=mybean.listfollowup_info%></center>

											</div>
										</form>
									</div>
									<div class="tab-pane" id="tabs-5">
										<div class="portlet box  ">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">Job Card(s)</div>
											</div>
											<div class="portlet-body portlet-empty">
												<div class="tab-pane" id="">
													<%=mybean.ListJobCardData(mybean.comp_id, mybean.veh_id)%>

												</div>
											</div>
										</div>
									</div>

									<div class="tab-pane" id="tabs-6">
										<div class="portlet box  ">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">Invoice(s)</div>
											</div>
											<div class="portlet-body portlet-empty">
												<div class="tab-pane" id="">
													<center><div><font color=red><b>No Invoice(s) found!</b></font></div></center>
													

												</div>
											</div>
										</div>
									</div>
									
									<div class="tab-pane" id="tabs-7">
										<div class="portlet box  ">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">Receipt(s)</div>
											</div>
											<div class="portlet-body portlet-empty">
												<div class="tab-pane" id="">
													<center><div><font color=red><b>No Receipt(s) found!</b></font></div></center>
												</div>
											</div>
										</div>
									</div>


									<div class="tab-pane" id="tabs-8">

										<div class="portlet box  ">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">Stock Status</div>
											</div>
											<div class="portlet-body portlet-empty container-fluid">
												<form class="form-horizontal" id="" name="">

													<div class="form-element6">
														<label >Branch:&nbsp; </label>
														<select name="dr_branch_id" id="dr_branch_id" class="form-control">
															<%=mybean.PopulateBranch(mybean.branch_id, "", "1,3", "", request)%>
														</select>
													</div>

													<div class="form-element6">
														<label >Location<font color=red>*</font>:&nbsp; </label>
															<%=mybean.PopulateLocation(mybean.branch_id)%>
													</div>

													<div class="form-element6">
														<label >Enter Search Text:&nbsp; </label>
															<input type="text" id="txt_item" name="txt_item" class="form-control" size="42"
																onkeyup="SearchStockStatus(this.value);" />
													</div>
												</form>
											</div>
										</div>
									</div>
									
									<div class="tab-pane" id="tabs-9">
										<div class="portlet box  ">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">Document(s)</div>
											</div>
											<div class="portlet-body portlet-empty">
												<div class="tab-pane" id="">
													<%=mybean.ListDocs(mybean.veh_id, "1", "", mybean.recperpage, mybean.comp_id)%>
												</div>
											</div>
										</div>
									</div>
									
									
									
									<div class="tab-pane" id="tabs-10">
										<div class="portlet box  ">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">Ticket(s)</div>
											</div>
											<div class="portlet-body portlet-empty">
												<div class="tab-pane" id="">
												<div class="container-fluid">
													<a href="ticket-add.jsp?add=yes&veh_id=<%=mybean.veh_id%>&contact_id=<%=mybean.veh_contact_id%>
																&branch_id=<%=mybean.veh_branch_id%>" style="float:right">Add New Ticket...</a>
												</div>
													<%=mybean.ListTicketData(mybean.comp_id, mybean.veh_id)%>

												</div>
											</div>
										</div>
									</div>
									
									
									<div class="tab-pane" id="tabs-11">
										<div class="portlet box  ">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">PSF Follow-up</div>
											</div>
											<div class="portlet-body portlet-empty">
												<div class="tab-pane" id="">
													<%=mybean.ListPSFFollowup(mybean.veh_id, mybean.comp_id)%>
													<%if(!mybean.veh_so_id.equals("0")){ %>
													<%=mybean.enquirydash.ListCRMFollowup("0", mybean.veh_so_id, mybean.comp_id) %>
													<%} %>
												</div>
											</div>
										</div>
									</div>
									
									<div class="tab-pane" id="tabs-12">
										<div class="portlet box ">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">History</div>
											</div>
											<div class="portlet-body portlet-empty">
												<div class="tab-pane" id="">
													<%=mybean.ListHistory(mybean.comp_id, mybean.veh_id)%>

												</div>
											</div>
										</div>
									</div>
								</div>
							</div>

						</div>

					</div>
					<!-- 				page-content-inner END ----->
				</div>
			</div>
		</div>

	</div>
	<%@include file="../Library/admin-footer.jsp"%>
		<%@include file="../Library/js.jsp"%>
<%
	if (!mybean.modal.equals("yes")) {
%>
<script language="JavaScript" type="text/javascript">
	function Followupvalidation() {
					//alert("coming in veh dash");
		var contactable = $("#dr_vehfollowup_contactable_id").val();
		var action = $("#dr_vehfollowup_vehaction_id").val();
		var workshop = $("#dr_vehfollowup_workshop_branch_id").val();
		var datetime = $("#txt_vehfollowup_appt_time").val();
		var booktype = $("#dr_vehfollowup_bookingtype_id").val();
		var nextfollowup = $("#txt_vehfollowup_time").val();
		var lostcase = $("#dr_vehlostcase1_id").val();
		var pickup = $("#dr_vehfollowup_pickupdriver_emp_id").val();
		var competitor = $("#dr_competitor_id").val();
		var lostcasereason = $("#dr_lostcase_reason1").val();
		var notcantactable = $("#dr_vehfollowup_notcontactable_id").val();
		var remarks = $("#txt_vehfollowup_desc").val();
		remarks = remarks.trim();

		// 		if (contactable == 0) {
		// // 			//alert("contactable==="+contactable);
		// 			$("#reasonerrormsg").hide();
		// 			$("#errormsg").html("<b> Select Contactable!").css("color", "red");
		// 			$("#errormsg").show();
		// 			return false;
		// 		}
		if (contactable == 1) {
			$("#errormsg").hide();
			if (action == 0) {
				// 				//alert("action====="+action);
				$("#reasonerrormsg").hide();
				$("#errormsg").hide();
				$("#nextfollowerrormsg").hide();
				$("#lostcaseerrormsg").hide();
				$("#actionerrormsg").html("<b>Select Action!").css("color",
						"red");
				$("#actionerrormsg").show();
				return false;
			}
			if (workshop == 0 && action == 1) {
				$("#errormsg").hide();
				$("#actionerrormsg").hide();
				$("#nextfollowerrormsg").hide();
				$("#lostcaseerrormsg").hide();
				$("#reasonerrormsg").hide();
				$("#workshoperrormsg").html("<b> Select Workshop!").css( "color", "red");
				$("#workshoperrormsg").show();
				return false;
			}
			if (datetime == '' && action == 1) {
				$("#errormsg").hide();
				$("#actionerrormsg").hide();
				$("#nextfollowerrormsg").hide();
				$("#lostcaseerrormsg").hide();
				$("#workshoperrormsg").hide();
				$("#datetimeerrormsg").html("<b>Select DateTime!").css("color", "red");
				$("#datetimeerrormsg").show();
				return false;
			}
			if (booktype == 0 && action == 1) {
				$("#errormsg").hide();
				$("#actionerrormsg").hide();
				$("#datetimeerrormsg").hide();
				$("#nextfollowerrormsg").hide();
				$("#lostcaseerrormsg").hide();
				$("#bookingerrormsg").html("<b>Select Booking Type!").css( "color", "red");
				$("#bookingerrormsg").show();
				return false;
			}
			if (pickup == 0 && booktype == 2) {
				$("#bookingerrormsg").hide();
				$("#drivererrormsg").html("<b>Select Driver!").css("color", "red");
				$("#drivererrormsg").show();
				return false;
			} else {
				$("#drivererrormsg").hide();
			}

			if (nextfollowup == '' && action == 2) {
				$("#errormsg").hide();
				$("#actionerrormsg").hide();
				$("#lostcaseerrormsg").hide();
				$("#reasonerrormsg").hide();
				$("#nextfollowerrormsg").html("<b>Select Next Followup Time!").css("color", "red");
				$("#nextfollowerrormsg").show();
				return false;
			}
			// 	//alert("lostcase===="+lostcase);
			if (lostcase == 0 && action == 3) {
				// 				//alert("inside lostcase==="+lostcase);
				$("#errormsg").hide();
				$("#actionerrormsg").hide();
				$("#reasonerrormsg").hide();
				$("#nextfollowerrormsg").hide();
				$("#lostcaseerrormsg").html("<b>Select Lost Case!").css( "color", "red");
				$("#lostcaseerrormsg").show();
				return false;
			}
			// 			//alert("competitor==="+competitor);
			if (lostcase == 1 && competitor == 0) {
				// 				//alert(" Inside competitor==="+competitor);
				$("#competitorerrormsg").html("<b>Select Competitor!").css( "color", "red");
				$("#competitorerrormsg").show();
				// 				//alert(lostcase);
				// 				//alert(competitor);
				return false;
			} else if (lostcase == 1 && competitor != 0 && lostcasereason == 0) {
				// 				//alert("Inside lostcasereason == "+lostcasereason);
				$("#competitorerrormsg").hide();
				$("#lc_reason_errormsg1").html("<b>Select Lost Case Reason!").css("color", "red");
				$("#lc_reason_errormsg1").show();
				return false;
			} else {
				// 				//alert("inside else");
				$("#competitorerrormsg").hide();
				$("#lc_reason_errormsg1").hide();
				// 		$("#lostcaseerrormsg").hide();
			}

			if (remarks == "") {
				// 				//alert("1234");
				$("#drivererrormsg").hide();
				$("#feedbackerrormsg").html("<br><b>Feedback cannot be empty!").css("color", "red");
				$("#feedbackerrormsg").show();
				return false;
			} else {
				// 				//alert("remarks-hide");
				$("#feedbackerrormsg").hide();
				return true;
			}

		} else {
			if (notcantactable == 0 && contactable == 2) {
				$("#nextfollowerrormsg").hide();
				$("#errormsg").hide();
				// 				//alert("123344545");
				$("#reasonerrormsg").html("<b>Select Reason!").css("color", "red");
				$("#reasonerrormsg").show();
				return false;
			}
			// 			//alert("last else");
			return true;
		}
	}
</script>

<script language="JavaScript" type="text/javascript">
	function Contactableband() {
		// 		//alert("Inside Contactableband() ");
		$("#errormsg").hide();
		var contactable = $("#dr_vehfollowup_contactable_id").val();
		var action = $("#dr_vehfollowup_vehaction_id").val();
		// 		 //alert(contactable_status);
		if (contactable == 0) {
			// 			 //alert("come");
			$("#contactableyesband").hide();
			$("#callatterband").hide();
			$("#reasonband").hide();
			$("#feedbackband").hide();
		} else if (contactable == 1) {
			$("#contactableyesband").show();
			$("#actionband").hide();
			$("#callatterband").hide();
			$("#reasonband").hide();
			$("#feedbackband").show();
		} else if (contactable == 2) {
			$("#contactableyesband").hide();
			$("#callatterband").show();
			$("#reasonband").show();
			$("#feedbackband").show();
		}

		if (contactable == 1 && action == 3) {
			$("#lostcaseband").show();
		}
		if (contactable == 2) {
			$("#reasonband").show();
		}
	}

	function Serviceactionband() {
		// 		//alert("Inside Serviceactionband()");
		$("#actionerrormsg").hide();
		var contactable = $("#dr_vehfollowup_contactable_id").val();
		var serviceAction = $("#dr_vehfollowup_vehaction_id").val();
		var lostcase = $("#dr_vehlostcase1_id").val();
		var action = $("#dr_vehfollowup_vehaction_id").val();
		if (serviceAction == 0) {
			$("#bookaserviceband").hide();
			$("#callatterband").hide();
			$("#lostcaseband").hide();
		} else if (serviceAction == 1) {
			$("#actionband").show();
			$("#bookaserviceband").show();
			$("#pickupband").hide();
			$("#callatterband").hide();
			$("#lostcaseband").hide();
		} else if (serviceAction == 2) {
			$("#actionband").show();
			$("#bookaserviceband").hide();
			$("#callatterband").show();
			$("#reasonband").hide();
			$("#lostcaseband").hide();
		} else if (serviceAction == 3) {
			$("#actionband").show();
			$("#bookaserviceband").hide();
			$("#callatterband").hide();
			$("#lostcaseband").show();
		}
		if (contactable == 2) {
			$("#callatterband").show();
			$("#reasonband").show();
		}
		// 		 //alert("acrtion=="+action);

		if (lostcase == 1) {
			$("#competitorband").show();
		} else {
			$("#competitorband").hide();
			$("#competitorerrormsg").hide();
		}
		if (action != 3) {
			// 			 //alert("inside");
			$("#competitorband").hide();
			$("#competitorerrormsg").hide();
		}
		if (contactable == 1 && action == 3) {
			$("#lostcaseband").show();
		}
		if (contactable == 1) {
			$("#reasonerrormsg").hide();
		}
		if (lostcase != 3) {
			$("#lostcaseerrormsg").hide();
		}
		if (contactable == 1 && action != 2) {
			$("#nextfollowerrormsg").hide();
			$("#callatterband").hide();
		}
		if (contactable == 1 && action == 2) {
			$("#lostcaseband").hide();
			$("#callatterband").show();
			$("#wrong_no").hide();
			$("#reason").show();
		}

	}

	function Pickupband() {
		// 		//alert("Inside Pickupband()");
		var pickup = $("#dr_vehfollowup_bookingtype_id").val();
		if (pickup == 0) {
			$("#pickupband").hide();
		} else if (pickup == 1) {
			$("#pickupband").hide();

		} else if (pickup == 2) {
			$("#pickupband").show();
		} else if (pickup == 3) {
			$("#pickupband").hide();
		}
	}

	function wrong_no() {
		// 		//alert("Inside wrong_no()");
		competitor();
		var reason = $("#dr_vehfollowup_notcontactable_id").val();
		var lostcase = $("#dr_vehlostcase2_id").val();
		var contactable = $("#dr_vehfollowup_contactable_id").val();

		if (contactable == 0) {
			$("#lostcaseband1").hide();
			$("#competitorband1").hide();
			$("#callatterband").hide();
		} else if (contactable == 1) {
			$("#lostcaseband1").hide();
			$("#competitorband1").hide();
		} else if (contactable == 2) {
			if (reason == 0) {
				$("#lostcaseband1").hide();
				$("#reason").show();
				$("#wrong_no").hide();
				$("#competitorband1").hide();
			} else if (reason == 1) {
				$("#lostcaseband1").hide();
				$("#reason").show();
				$("#wrong_no").hide();
				$("#competitorband1").hide();
				$("#nextfollowerrormsg").hide();
				$("#reasonerrormsg").hide();
			} else if (reason == 2) {
				$("#lostcaseband1").hide();
				$("#reason").show();
				$("#wrong_no").hide();
				$("#competitorband1").hide();
				$("#nextfollowerrormsg").hide();
				$("#reasonerrormsg").hide();
			} else if (reason == 3) {
				$("#reason").hide();
				$("#wrong_no").show();
				$("#lostcaseband1").show();
				$("#competitorband1").show();
				$("#nextfollowerrormsg").hide();
				$("#reasonerrormsg").hide();
			} else if (reason == 4) {
				$("#lostcaseband1").hide();
				$("#reason").show();
				$("#wrong_no").hide();
				$("#competitorband1").hide();
				$("#nextfollowerrormsg").hide();
				$("#reasonerrormsg").hide();
			}
		}

	}

	function competitor() {

		// 		//alert("Inside competitor()");
		var lostcase = $("#dr_vehlostcase2_id").val();
		var contactable = $("#dr_vehfollowup_contactable_id").val();
		var reason = $("#dr_vehfollowup_notcontactable_id").val();

		if (reason != 0 && lostcase != 0) {
			$("#reasonerrormsg").hide();
			$("#lostcaseerrormsg2").hide();
			if (lostcase == 1 && contactable == 2 && reason == 3) {
				$("#competitorband1").show();
			} else if (contactable != 2) {
				$("#competitorband1").hide();
			} else if (reson = !3) {
				$("#competitorband1").hide();
			} else if (lostcase != 1) {
				$("#competitorband1").hide();
			}
		} else {
			$("#competitorband1").hide();
		}

	}

	function lostCaseReason() {
		// 		//alert("Inside lostCaseReason() ");
		var competitor = $("#dr_competitor_id1").val();
		if (competitor == 0) {
			$("#lcReason").hide();
		} else {
			$("#lcReason").show();
			$("#competitorerrormsg1").hide();
			$("#lc_reason_errormsg").hide();

		}
	}

	function notcontactablevalidation() {
		$("#errormsg").hide();
		// 		//alert('second one.....');
		var contactable = $("#dr_vehfollowup_contactable_id").val();
		var nextFollowUpTime = $("#txt_vehfollowup_time").val();
		var reason = $("#dr_vehfollowup_notcontactable_id").val();
		var noncontactlostCase = $("#dr_vehlostcase2_id").val();
		// 		var lostCasereason = $("#dr_lostcase_reason").val();
		var noncontactCompetitor = $("#dr_competitor_id1").val();
		var lostcaseReason = $("#dr_lostcase_reason").val();
		var remarks = $("#txt_vehfollowup_desc").val();
		remarks = remarks.trim();

		if (contactable == 2) {
			// 			//alert("contactable == 2");
			if (nextFollowUpTime == "" && reason != 3) {
				// 				//alert("nextfollowerrormsg");
				$("#nextfollowerrormsg").html( "<b>Select Next Follwup time!</b>").css("color", "red");
				$("#nextfollowerrormsg").show();
				return false;
			} else if (nextFollowUpTime == "" && reason == 3) {
				$("#nextfollowerrormsg").hide();
				return true;
			} else if (reason == 0) {
				$("#reasonerrormsg").html("<b>Select Reason!</b>").css("color", "red");
				$("#reasonerrormsg").show();
				return false;
			}
			// 			return true;

			if (reason == 3) {
				// 				//alert("coming in reason==3");
				if (noncontactlostCase == 0) {
					// 					//alert('coming...0.00');
					$("#lostcaseerrormsg2").html("<b>Select Lost Case!</b>") .css("color", "red");
					$("#lostcaseerrormsg2").show();
					return false;
				} else if (noncontactlostCase == 1 && noncontactCompetitor == 0) {
					// 					//alert('coming...0.01');
					$("#lostcaseerrormsg2").hide();
					$("#lc_reason_errormsg").hide();
					$("#competitorerrormsg1").html("<b>Select Competitor!</b>") .css("color", "red");
					$("#competitorerrormsg1").show();
					return false;
				} else if (noncontactlostCase == 1 && lostcaseReason == 0) {
					// 					//alert('coming...0.02');
					$("#competitorerrormsg1").hide();
					$("#lc_reason_errormsg").html( "<b>Select Lost Case Reason!</b>").css("color", "red");
					$("#lc_reason_errormsg").show();
					return false;
				} else {
					// 					//alert('coming...0.03');
					$("#lc_reason_errormsg").hide();
					return true;
				}
				// 				//alert('noncontactCompetitor=='+noncontactCompetitor);
				// 				 if(noncontactCompetitor == 0){
				// 					$("#lc_reason_errormsg").hide();
				// 				}

			}
			if (remarks == "") {
				// 				//alert("1234");
				$("#feedbackerrormsg").html("<br><b>Feedback cannot be empty!") .css("color", "red");
				$("#feedbackerrormsg").show();
				return false;
			} else {
				$("#feedbackerrormsg").hide();
				return true;
			}
		}
	}
</script>

<script type="text/javascript">
	function validation() {
				//alert("coming123");
		var contactable = $("#dr_vehfollowup_contactable_id").val();
		var temp = true;

		if (contactable == 0) {
			// 			//alert("contactable==="+contactable);
			$("#errormsg").html("<b> Select Contactable!").css("color", "red");
			$("#errormsg").show();
			temp = false;
			return false;
			
		} else if (contactable == 1) {
			// 				//alert("contactable==="+contactable);
			if (Followupvalidation()) {
				// 	 			//alert('1==' + temp);
				temp = true;
			} else {
				// 	 			//alert('1.1==' + temp);
				temp = false;
			}
		} else if (contactable == 2) {
			if (notcontactablevalidation()) {
				// 				//alert("contactable==="+contactable);
				// 				//alert('2==' + temp);
				temp = true;
			} else {
				// 	 			//alert('2.1==' + temp);
				temp = false;
			}
		}
		// 		//alert("temp==="+temp);
		return temp;
	}
</script>

<!-- <script type="text/javascript"> -->
<!-- 	$(function() { -->
<!-- 		$("#tabs").tabs({ -->
<!-- 			event : "mouseover" -->
<!-- 		}); -->

<!-- 		$("#txt_veh_modelyear").datepicker({ -->
<!-- 			dateFormat : 'yy', -->
<!-- 			stepMinute : 5 -->
<!-- 		}); -->
<!-- 	}); -->
<!-- </script> -->
<%
	}
%>
<script language="JavaScript" type="text/javascript">
	$("#txt_veh_fastag").bind('keyup',function(e){
		$("#txt_veh_fastag").val(($("#txt_veh_fastag").val()).toUpperCase());
	});

	function PopulateDriver() {
		var bookingid = document.getElementById("dr_vehfollowup_bookingtype_id").value;
		showHint('vehicle-check.jsp?listdriver=yes&booking_id=' + bookingid, 'HintDriver');
	}

	function checknextfollowuptime() {
		var nextfollowuptime = document.getElementById("dr_insurlostcase1_id").value;
		if (nextfollowuptime == "0") {
			$("#nextfollowuptime").show();
		} else
			(nextfollowuptime != "0")
		{
			$("#nextfollowuptime").hide();
		}
	}
	function ClearMsg() {
		document.getElementById("hint_dr_item_model_id").innerHTML = "";
	}

	function UpdateCustomer() {
		document.getElementById("customer_details").style.display = "";
		document.getElementById("customer_display").style.display = "none";
	}

	function HideCustomerDetails() {
		var customer_id = document.getElementById("txt_customer_id").value;
		document.getElementById("customer_details").style.display = "none";
		document.getElementById("customer_display").style.display = "";
		showHint( 'vehicle-dash-check.jsp?display_customer_details=yes&customer_id=' + customer_id, 'div_customer_details');
	}

	function PopulateModelItem() {
		var item_model_id = document.getElementById("dr_item_model_id").value;
		document.getElementById("hint_dr_item_id").innerHTML = "";
		showHint('vehicle-dash-check.jsp?list_model_item=yes&item_model_id=' + item_model_id, 'model-item');
	}


	function SearchStockStatus(itemName) {
		var location_id = document.getElementById("dr_location_id").value;
		showHint('vehicle-dash-check.jsp?stock_status=yes&location_id=' + location_id + '&item_name=' + itemName, 'div_stock_status');
	}

// 	function PopulateItem(branch_id) {
// 		showHint('vehicle-dash-check.jsp?location=yes&branch_id=' + branch_id,  'span_location');
// 	}

	function SecurityCheck(name, obj, hint) {
// 				alert("name-------"+name);
// 				alert("obj-------"+obj);
// 				alert("hint-------"+hint);
		var value;
		var veh_id = GetReplace(document.getElementById("veh_id").value);
		var url = "../service/vehicle-dash-check.jsp?";
		var dat = document.getElementById("txt_veh_sale_date").value;
		var param;
		var str = "123";
		if (name != "chk_veh_classified" && name != "chk_enquiry_avpresent" && name != "chk_enquiry_manager_assist") {
			value = GetReplace(obj.value);
		} else {
			if (obj.checked == true) {
				value = "1";
			} else {
				value = "0";
			}
		}
		if (name == "dr_item_model_id") {
			value = GetReplace(obj.value);
			var stage_id;
			if (value == 0) {
				stage_id = 1;
			} else {
				stage_id = 2;
			}

			param = "name=" + name + "&value=" + value + "&enquiry_dat=" + dat + "&veh_id=" + veh_id + "&stage_id=" + stage_id;
		}

		var fromdate = "";
		if (fromdate != "") {
			param = "name=" + name + "&value=" + value + "&enquiry_dat=" + dat + "&from_date=" + fromdate + "&enquiry_id=" + enquiry_id;
		} else {
			param = "name=" + name + "&value=" + value + "&enquiry_dat=" + dat + "&veh_id=" + veh_id;
		}
		showHint(url + param,  hint);
		setTimeout('RefreshHistory()', 1000);
	}
	//////////////////// eof security check /////////////////////

	function AddNewContact(customer_id) {
		$('#dialog-modal-contact').dialog({
			autoOpen : false,
			width : 900,
			height : 500,
			zIndex : 200,
			modal : true,
			title : "Add New Contact"
		});

		//$('#new_contact_link').click(function(){
		var customer_id = document.getElementById("txt_customer_id").value;
			$.ajax({
				success : function(data) {
				$('#dialog-modal-contact').html( '<iframe src="../customer/customer-contact-update.jsp?Add=yes&customer_id=' + customer_id
												+ '&modal=yes" width="100%" height="100%" frameborder=0></iframe>');
					}
			});

		$('#dialog-modal-contact').dialog('open');
		//return true;
		//});
		////alert("1");
	}

	function CloseModal(customer_id) {
		showHint('vehicle-dash-check.jsp?customer_details=yes&customer_id=' + customer_id, 'tabs-2');
		$('#dialog-modal-contact').dialog('close');
	}

	function RefreshHistory() {
		var veh_id = document.getElementById("veh_id").value;
	}
</script>
	<!-- START OF TAGS CONFIGURATION -->
	<script src="../assets/js/bootstrap-tagsinput.js" type="text/javascript"></script>
	<script type="text/javascript">
		//	THIS IS TO POPULATE VALUE IN TAG-CONTAINER AT THE TIME OF PAGE LOAD
	<%=mybean.tagcheck.PopulateTags(mybean.veh_customer_id, mybean.comp_id)%>
		//	THIS IS TO ADD TAGS IN TAG-CONTAINER ON CLICK FROM POPOVER
	<%=mybean.tagcheck.PopulateTagsJS(mybean.veh_customer_id, mybean.comp_id)%>
		function deleteTag() {
			$('#customer_tagclass > > input').tagsinput('remove', {
				'value' : 0,
				'text' : 'No Tag Selected',
				'continent' : '#ff0000'
			});
		}

		function addNoTag() {
			$('#customer_tagclass > > input').tagsinput('add', {
				'value' : 0,
				'text' : 'No Tag Selected',
				'continent' : '#ff0000'
			});
		}

		$(function() {
			$("#enquiry_tags") .on( 'itemRemoved',
					function() {
					var url = "../customer/customer-tags-check.jsp?";
					var param = "update=yes&tags=" + $("#enquiry_tags").val() + "&customer_id=" + <%=mybean.veh_customer_id%> + "&vehicle=yes&enquiry_id=" + <%=mybean.veh_enquiry_id%> + "&veh_id=" + <%=mybean.veh_id%> ;
					var hint = "hint_enquiry_tags";

					var param2 = "tags_content=yes&customer_id=" + <%=mybean.veh_customer_id%> ;
					var hint2 = "popover-content";

					setTimeout('showHint("' + url + param2 + '", "' + hint2 + '")', 100);

					setTimeout( 'if($("#enquiry_tags").val()==""){ addNoTag(); }', 150);

					showHint(url + param, hint);
			});
		});

		function tagcall(idname) {

			var url = "../customer/customer-tags-check.jsp?";
			var param1 = "add=yes&name=enquiry&tags=" + idname + "&customer_id=" + <%=mybean.veh_customer_id%> + "&vehicle=yes&enquiry_id=" + <%=mybean.veh_enquiry_id%> + "&veh_id=" + <%=mybean.veh_id%> ;
			var hint1 = "hint_enquiry_tags";

			var param2 = "tags_content=yes&id=" + <%=mybean.veh_customer_id%> ;
			var hint2 = "popover-content";

			setTimeout('showHint("' + url + param2 + '", "' + hint2 + '")', 100);

			setTimeout('addTag(' + idname + ')', 150);

			setTimeout('showHint("' + url + param1 + '", "' + hint1 + '")', 50);

			deleteTag();

		}

		//this is provide property to the TAG POPOVER 
		var browser_name = "<%=request.getHeader("User-Agent")%>";
// 		alert(tempname);
		
		if(browser_name.includes("Safari")){
// 			alert("Safari");
			$('#popover').popover({ 
			    html : true,
			    title: function() {
			      return $("#popover-head").html();
			    },
//	 		    trigger:'onclick',
			    content: function() {
			      return $("#popover-content").html();
			    }
			});
			
		}else{
// 			alert("outher");
			$('#popover').popover({ 
			    html : true,
			    title: function() {
			      return $("#popover-head").html();
			    },
	 		    trigger:'onclick',
			    content: function() {
			      return $("#popover-content").html();
			    }
			});
		}
	</script>

	<!-- END OF TAGS CONFIGURATION -->
	
	<!-- For Canned Message -->
	<script type="text/javascript">
		function SendEmail(email_id){
			var veh_id = <%=mybean.veh_id%>;
			showHint('../portal/canned-message-check.jsp?type=3&email=yes&email_id=' + email_id + '&value=' + veh_id, 'sentmsg');
		}
		
		function SendSMS(sms_id){
			var veh_id = <%=mybean.veh_id%>;
			showHint('../portal/canned-message-check.jsp?type=3&sms=yes&sms_id=' + sms_id + '&value=' + veh_id, 'sentmsg');
		}
	</script>
	
	<script type="text/javascript">
		function PopulateCouponCampaign() {
			var brand_id = document.getElementById('dr_principal_id').value;
			var dept_id = document.getElementById('dr_dept_id').value;
			var type_id = document.getElementById('dr_couponcampaign_type_id').value;
			showHint("../service/coupon-issue-check.jsp?couponcampaign=yes&dr_brand_id=" + brand_id + "&dr_dept_id="+ dept_id + "&dr_type_id="+ type_id, "hintcouponcampaign");
		}
		
		function PopulateCouponDetails() {
			var brand_id = document.getElementById('dr_principal_id').value;
			var dept_id = document.getElementById('dr_dept_id').value;
			var type_id = document.getElementById('dr_couponcampaign_type_id').value;
			var campaign_id = document.getElementById('dr_couponcampaign_id').value;
			var url = "../service/coupon-issue-check.jsp?dr_brand_id=" + brand_id + "&dr_dept_id="+ dept_id + "&dr_type_id="+ type_id+ "&dr_campaign_id="+ campaign_id;
			showHint(url+'&couponoffer=yes', 'hintcoupondetail');
			
		}
		
		function CouponIssue(){
			var balancecount = document.getElementById('balance_count').innerHTML;
			var couponid = document.getElementById('coupon_id').value;
			var branchid = <%=mybean.veh_branch_id%>;
			var contact_id = <%=mybean.veh_contact_id%>;
			showHint("../service/coupon-issue-check.jsp?couponoffer=update&coupon_id=" + couponid + "&coupon_contact_id=" + contact_id + "&coupon_branch_id="+ branchid+ "&balance_count="+ balancecount, "issuemsg");
			
			setTimeout('disablebuttuon();', 300);
		}
		
		function disablebuttuon() {
			var issuemsg = $("#issuemsg").text();

			if (issuemsg == 'Coupon Issued!') {
				$('#add_button').hide();
				$('#form1').hide();
			}
		}
	</script>

</body>
</HTML>
