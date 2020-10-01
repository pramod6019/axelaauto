<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.customer.Customer_Dash"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<%@include file="../Library/css.jsp"%>
<!-- tag CSS -->
<link href="../assets/css/bootstrap-tagsinput.css" rel="stylesheet" type="text/css" />
<!-- tag CSS -->

<style>
.pop {
	top: 280px;
	left: 740px;
}
.font-size{
	font-size: 17px;
}
.margin-h4{
margin :0px;
}
.left-right-border{
border-right: 1px solid #ff0000;
border-left: 1px solid #ff0000;
}
.right-border{
border-right: 1px solid #ff0000;
}
</style>
</head>
<!-- onload="FormFocus();" -->
<body  class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>Customer Dashboard</h1>
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
							<li><a href="customer.jsp">Customers</a> &gt;</li>
							<li><a href="customer-list.jsp?all=yes">List Customers</a>
								&gt;</li>
							<li><a
								href="customer-dash.jsp?customer_id=<%=mybean.customer_id%>"><%=mybean.customer_name%>&nbsp;(<%=mybean.customer_id%>)</a><b>:</b></li>
						</ul>
						<!-- END PAGE BREADCRUMBS -->
						<div class="form-element12" style="background: white;padding:0px;">
							<div class="form-element5 form-element-padding" style="padding:0px;">
							<div class="form-element6 left-right-border">
								<p class="font-size"><%=mybean.customer_so_amount%></p>
								<h4 class="margin-h4">Sales Order</h4>
								<span >Count <b>: </b><%=mybean.customer_so_count%></span>
							</div>
							
							<div class="form-element6 right-border">
								<p class="font-size"><%=mybean.customer_jc_amount%></p>
								<h4 class="margin-h4">Job Cards</h4>
								 <span>Count <b>: </b><%=mybean.customer_jc_count%></span>
							</div>
<!-- 							<div class="form-element4 right-border"> -->
<%-- 								<p class="font-size"><%=mybean.customer_insurance_amount%></p> --%>
<!-- 								<h4 class="margin-h4">Insurance</h4>  -->
<%-- 								<span>Count <b>: </b><%=mybean.customer_insurance_count%></span> --%>
<!-- 							</div> -->
							</div>
							<div class="form-element5 form-element">
							<div class="form-element4 right-border ">
								<p class="font-size"><%=mybean.customer_coupons_amount%></p>
								<h4 class="margin-h4">Coupons</h4>
								 <span>Count <b>: </b><%=mybean.customer_coupons_count%></span>
							</div>
							<div class="form-element4 right-border">
								<p class="font-size"><%=mybean.customer_invoice_amount%></p>
								<h4 class="margin-h4">Invoice</h4>
								<span>Count <b>: </b><%=mybean.customer_invoice_count%></span>
							</div>
							<div class="form-element4 right-border">
								<p class="font-size"><%=mybean.customer_receipt_amount%></p>
								<h4 class="margin-h4">Receipts</h4>
								 <span>Count <b>:</b> <%=mybean.customer_receipt_count %></span>
							</div>
							</div>
							<div class="form-element2">
								<p class="font-size">0</p>
								<h4 class="margin-h4">Profit</h4>
								 <span ></span>
							</div>
						</div>
						<!-- 			TAGS START -->
						<CENTER>
							<div class="tag_class" id="customer_tagclass">
								<div class="bs-docs-example" id="bs-docs-example">
									<input type="text" class="form-control" id="enquiry_tags"
										name="enquiry_tags" /> <a href="#" id="popover" data-placement="bottom"
										class="btn btn-success btn-md"><span  style="font-size: 20px;margin-top: 5px"
										class="fa fa-angle-down"></span></a>
<%-- 										<%if(mybean.comp_id.equals("1000") && mybean.AppRun().equals("1")){ %> --%>
<%-- 								<%} %> --%>
								</div>
							</div>
							<div class="hint" id="hint_enquiry_tags"></div>

							<div id="popover-head" class="hide">
								<center>Tag List</center>
							</div>
							<div id="popover-content" class="hide">
								<%=mybean.tagcheck.PopulateTagsPopover( mybean.customer_id, mybean.comp_id)%>
							</div>
						</CENTER>

							<!-- 					BODY START -->
							<div>
								<ul class="nav nav-tabs">
									<li class="active"><a href="#tabs-1" data-toggle="tab">Customer Overview</a></li>
									<li onclick="LoadCustomerDash('2');"><a href="#tabs-2" data-toggle="tab" >Contacts</a></li>
									<li onclick="LoadCustomerDash('3');"><a href="#tabs-3" data-toggle="tab">Enquiries</a></li>
									<li onclick="LoadCustomerDash('4');"><a href="#tabs-4" data-toggle="tab">Sales Order</a></li>
									<li onclick="LoadCustomerDash('5');"><a href="#tabs-5" data-toggle="tab">Vehicles</a></li>
									<li onclick="LoadCustomerDash('6');"><a href="#tabs-6" data-toggle="tab">Job Cards</a></li>
<!-- 									<li onclick="LoadCustomerDash('7');"><a href="#tabs-7" data-toggle="tab">Insurance</a></li> -->
									<li onclick="LoadCustomerDash('8');"><a href="#tabs-8" data-toggle="tab">Tickets</a></li>
									<li onclick="LoadCustomerDash('9');"><a href="#tabs-9" data-toggle="tab">Coupon</a></li>
									<li onclick="LoadCustomerDash('10');"><a href="#tabs-10" data-toggle="tab">Invoice</a></li>
									<li onclick="LoadCustomerDash('11');"><a href="#tabs-11" data-toggle="tab">Receipts</a></li>
									<li onclick="LoadCustomerDash('12');"><a href="#tabs-12" data-toggle="tab">History</a></li>
								</ul>
								<div class="tab-content">
								<div id="dialog-modal"></div>
								<div class="tab-pane in active" id="tabs-1">
									<center>
										<font color="#ff0000"><b><%=mybean.msg%></b></font>
									</center>
									<div class="portlet box">
<!-- 										<div class="portlet-title" style="text-align: center"> -->
<!-- 											<div class="caption" style="float: none"> -->
<!-- 												<a -->
<%-- 													href="customer-list.jsp?customer_id=<%=mybean.customer_id%>"><%=mybean.customer_name%>&nbsp;(<%=mybean.customer_id%>)</a> --%>
<!-- 											</div> -->
<!-- 										</div> -->
										<div class="portlet-title" style="text-align: center">
											<div class="caption" style="float: none">Customer Overview</div>
										</div>
										<div class="portlet-body portlet-empty container-fluid">
											<div class="tab-pane" id="">
												<!-- START PORTLET BODY -->
												<form name="form1" id="form1" class="form-horizontal"
													method="post">
												<div class="form-element6">
												<label> Name<font color=red>*</font>:&nbsp; </label>
												<input name="txt_customer_name" type="text"
													class="form-control" id="txt_customer_name"
													value="<%=mybean.customer_name%>" size="50" maxlength="255" 
													onchange="SecurityCheck('txt_customer_name',this,'hint_txt_customer_name')"
													/><font color="#ff0000"></font>
													<input class="form-control" name="customer_id" type="hidden" id="customer_id" value="<%=mybean.customer_id%>" />
													<div class="hint" id="hint_txt_customer_name">
											</div>
											</div>
											<div class="form-element6">
												<label>Alias:&nbsp; </label>
												<input name="txt_customer_alias" type="text"
													class="form-control" id="txt_customer_alias"
													value="<%=mybean.customer_alias%>" size="50" maxlength="255"
													 onchange="SecurityCheck('txt_customer_alias',this,'hint_txt_customer_alias')" /><font color="#ff0000"></font>
													<div class="hint" id="hint_txt_customer_alias"></div>
											</div>
											<div class="row"></div>
											<div class="form-element6">
												<label>Customer Code:&nbsp; </label>
												<input name="txt_customer_code" type="text"
													class="form-control" id="txt_customer_code"
													value="<%=mybean.customer_code%>" size="32" maxlength="20"
													 onchange="SecurityCheck('txt_customer_code',this,'hint_txt_customer_code')" />
													<div class="hint" id="hint_txt_customer_code"></div> 
											</div>
											
											<div class="form-element6">
												<label>Branch :&nbsp; </label> 
												<select name="dr_customer_branch_id" class="form-control"
													id="dr_customer_branch_id" 
													onchange="SecurityCheck('dr_customer_branch_id',this,'hint_dr_customer_branch_id')">
													<%=mybean.PopulateBranch(mybean.customer_branch_id, "", "", "", request)%>
												</select>
												<div class="hint" id="hint_dr_customer_branch_id"></div>
											</div>
											<div class="row"></div>
											<div class="form-element3 form-element-padding">
												<label> Mobile 1<font color=red>*</font>:&nbsp; </label>
												<div class="col-md-11">
														<input name="txt_customer_mobile1" type="text"
													class="form-control" id="txt_customer_mobile1"
													value="<%=mybean.customer_mobile1%>"
													 size="32"
													maxlength="13" onKeyUp="toPhone('txt_customer_mobile1','Mobile 1')"
													onchange="SecurityCheck('txt_customer_mobile1',this,'hint_txt_customer_mobile1')" />
												</div>
												<div class="col-md-1">
												<% if (!mybean.customer_mobile1.equals("")) { %>
													<span><%=mybean.ClickToCall(mybean.customer_mobile1,mybean.comp_id)%></span>
													<% } %>
												</div>
												<div></div>
													(91-9999999999)
													<div class="hint" id="hint_txt_customer_mobile1"></div> 
											</div>

											<div class="form-element3 form-element-padding">
												<label> Mobile 2:&nbsp;</label> 
												<div class="col-md-11">
												<input name="txt_customer_mobile2" type="text"
													class="form-control" id="txt_customer_mobile2"
													value="<%=mybean.customer_mobile2%>"
													 size="32" maxlength="13"
													onKeyUp="toPhone('txt_customer_mobile2','Mobile 2')" 
													onchange="SecurityCheck('txt_customer_mobile2',this,'hint_txt_customer_mobile2')" />
												</div>
												<div class="col-md-1">
												<% if (!mybean.customer_mobile2.equals("")) { %>
													<span><%=mybean.ClickToCall(mybean.customer_mobile2,mybean.comp_id)%></span>
													<% } %>
												</div>
												<div></div>
													(91-9999999999)
													<div class="hint" id="hint_txt_customer_mobile2"></div> 
											</div>

											<div class="form-element3">
												<label> Mobile 3:&nbsp;</label>
												<div class="col-md-11">
												<input name="txt_customer_mobile3" type="text"
													class="form-control" id="txt_customer_mobile3"
													value="<%=mybean.customer_mobile3%>" 
													size="32" maxlength="13"
													onKeyUp="toPhone('txt_customer_mobile3','Mobile 3')"
													onchange="SecurityCheck('txt_customer_mobile3',this,'hint_txt_customer_mobile3')" />
												</div>
												<div class="col-md-1">
												<% if (!mybean.customer_mobile3.equals("")) { %>
													<span><%=mybean.ClickToCall(mybean.customer_mobile3,mybean.comp_id)%></span>
													<% } %>
												</div>
												<div></div>
													(91-9999999999)
												<div class="hint" id="hint_txt_customer_mobile3"></div> 
											</div>

											<div class="form-element3">
												<label> Mobile 4:&nbsp;</label> 
												<div class="col-md-11">												<input name="txt_customer_mobile4" type="text"
													class="form-control" id="txt_customer_mobile4"
													value="<%=mybean.customer_mobile4%>" 
													size="32" maxlength="13"
													onKeyUp="toPhone('txt_customer_mobile4','Mobile 4')" 
													onchange="SecurityCheck('txt_customer_mobile4',this,'hint_txt_customer_mobile4')" />
													</div>
													<div class="col-md-1">
												<% if (!mybean.customer_mobile4.equals("")) { %>
													<span><%=mybean.ClickToCall(mybean.customer_mobile4,mybean.comp_id)%></span>
													<% } %>
												</div>
												<div></div>
													(91-9999999999)
												<div class="hint" id="hint_txt_customer_mobile4"></div>
											</div>
											<div class="row"></div>
											<div class="form-element3">
												<label> Mobile 5:&nbsp;</label>
												<div class="col-md-11"> 
												<input name="txt_customer_mobile5" type="text"
													class="form-control" id="txt_customer_mobile5"
													value="<%=mybean.customer_mobile5%>"
													 size="32" maxlength="13"
													onKeyUp="toPhone('txt_customer_mobile5','Mobile 5')"
													onchange="SecurityCheck('txt_customer_mobile5',this,'hint_txt_customer_mobile5')" />
												</div>
												<div class="col-md-1">
												<% if (!mybean.customer_mobile5.equals("")) { %>
													<span><%=mybean.ClickToCall(mybean.customer_mobile5,mybean.comp_id)%></span>
													<% } %>
												</div>
												<div></div>
													(91-9999999999)
												<div class="hint" id="hint_txt_customer_mobile5"></div>
											</div>

											<div class="form-element3">
												<label> Mobile 6:&nbsp;</label>
												<div class="col-md-11"> 
												<input name="txt_customer_mobile6" type="text"
													class="form-control" id="txt_customer_mobile6"
													value="<%=mybean.customer_mobile6%>"
													 size="32" maxlength="13"
													onKeyUp="toPhone('txt_customer_mobile6','Mobile 6')" 
													onchange="SecurityCheck('txt_customer_mobile6',this,'hint_txt_customer_mobile6')" />
													</div>
													<div class="col-md-1">
												<% if (!mybean.customer_mobile6.equals("")) { %>
													<span><%=mybean.ClickToCall(mybean.customer_mobile6,mybean.comp_id)%></span>
													<% } %>
												</div>
												<div></div>
													(91-9999999999)
												<div class="hint" id="hint_txt_customer_mobile6"></div>
											</div>


											<div class="form-element3">
												<label> Phone 1:&nbsp;</label> 
												<input name="txt_customer_phone1" type="text" class="form-control"
													id="txt_customer_phone1"
													value="<%=mybean.customer_phone1%>" 
													size="32" maxlength="15"
													onKeyUp="toPhone('txt_customer_phone1','Phone 1')"
														onchange="SecurityCheck('txt_customer_phone1',this,'hint_txt_customer_phone1')" />
												(91-80-33333333)
												<div class="hint" id="hint_txt_customer_phone1"></div>
											</div>

											<div class="form-element3">
												<label> Phone 2:&nbsp;</label> 
												<input name="txt_customer_phone2" type="text" class="form-control"
													id="txt_customer_phone2"
													value="<%=mybean.customer_phone2%>" 
													size="32" maxlength="15"
													onKeyUp="toPhone('txt_customer_phone2','Phone 2')" 
													onchange="SecurityCheck('txt_customer_phone2',this,'hint_txt_customer_phone2')" />
												(91-80-33333333)
												<div class="hint" id="hint_txt_customer_phone2"></div>
											</div>
											<div class="row"></div>
											<div class="form-element3">
												<label> Phone 3:&nbsp;</label> 
												<input name="txt_customer_phone3" type="text" class="form-control"
													id="txt_customer_phone3"
													value="<%=mybean.customer_phone3%>" 
													size="32" maxlength="15"
													onKeyUp="toPhone('txt_customer_phone3','Phone 3')" 
													onchange="SecurityCheck('txt_customer_phone3',this,'hint_txt_customer_phone3')" />
												(91-80-33333333)
												<div class="hint" id="hint_txt_customer_phone3"></div>
											</div>

											<div class="form-element3">
												<label> Phone 4:&nbsp;</label> 
												<input name="txt_customer_phone4" type="text" class="form-control"
													id="txt_customer_phone4"
													value="<%=mybean.customer_phone4%>" 
													size="32" maxlength="15"
													onKeyUp="toPhone('txt_customer_phone4','Phone 4')" 
													onchange="SecurityCheck('txt_customer_phone4',this,'hint_txt_customer_phone4')" />
												(91-80-33333333)
												<div class="hint" id="hint_txt_customer_phone4"></div>
											</div>

											<div class="form-element3">
												<label> Fax 1:&nbsp;</label> 
												<input name="txt_customer_fax1"
													type="text" class="form-control" id="txt_customer_fax1"
													value="<%=mybean.customer_fax1%>"
													 size="32" maxlength="50" 
													 onchange="SecurityCheck('txt_customer_fax1',this,'hint_txt_customer_fax1')" />
												<div class="hint" id="hint_txt_customer_fax1"></div>	 
											</div>

											<div class="form-element3">
												<label> Fax 2:&nbsp;</label> 
												<input name="txt_customer_fax2"
													type="text" class="form-control" id="txt_customer_fax2"
													value="<%=mybean.customer_fax2%>"
													 size="32" maxlength="50" 
													 onchange="SecurityCheck('txt_customer_fax2',this,'hint_txt_customer_fax2')" />
												<div class="hint" id="hint_txt_customer_fax2"></div>	 
											</div>
                         					 <div class="row"></div>
											<div class="form-element6">
												<label> Email 1:&nbsp;</label> 
												<input name="txt_customer_email1" type="text" class="form-control"
													id="txt_customer_email1"
													value="<%=mybean.customer_email1%>" 
													size="40" MaxLength="100"
													onchange="SecurityCheck('txt_customer_email1',this,'hint_txt_customer_email1')" />
												<div class="hint" id="hint_txt_customer_email1"></div>
											</div>
											<div class="form-element6">
												<label> Email 2:&nbsp;</label> 
												<input name="txt_customer_email2" type="text" class="form-control"
													id="txt_customer_email2"
													value="<%=mybean.customer_email2%>"
													 size="40" MaxLength="100" 
													 onchange="SecurityCheck('txt_customer_email2',this,'hint_txt_customer_email2')" />
												<div class="hint" id="hint_txt_customer_email2"></div>
											</div>
												<div class="row"></div>	
												<div class="form-element6">
												<label>Website 1:&nbsp;</label> 
												<input name="txt_customer_website1" type="text"
													class="form-control" id="txt_customer_website1"
													value="<%=mybean.customer_website1%>
													" size="40" MaxLength="100" 
													onchange="SecurityCheck('txt_customer_website1',this,'hint_txt_customer_website1')" />
													<div class="hint" id="hint_txt_customer_website1"></div>
											</div>

											<div class="form-element6">
												<label>Website 2:&nbsp;</label> 
												<input name="txt_customer_website2" type="text"
													class="form-control" id="txt_customer_website2"
													value="<%=mybean.customer_website2%>" 
													size="40" MaxLength="100" onchange="SecurityCheck('txt_customer_website2',this,'hint_txt_customer_website2')" />
													<div class="hint" id="hint_txt_customer_website2"> </div>
											</div>
											<div class="row"></div>
											<div class="form-element6">
												<label>GSTIN<font color=red>*</font>:&nbsp;
												</label> <b> 
												<input name="txt_customer_gst_no" type="text"
													class="form-control" id="txt_customer_gst_no"
													value="<%=mybean.customer_gst_no%>" size="50"
													maxlength="15"  /></b><b>Example: 22AAAAA0000A1Z5</b> 
												<span id="gst"> </span>
												<div class="hint" id="hint_txt_customer_gst_no">
											</div>
											</div>

											<div class="form-element6">
												<label>GSTIN Date<font color=red>*</font>:&nbsp; </label>
												<input name="txt_customer_gst_regdate"
													id="txt_customer_gst_regdate"
													value="<%=mybean.gst_regdate%>"
													class="form-control datepicker" type="text" maxlength="10" onchange="SecurityCheck('txt_customer_gst_regdate',this,'hint_txt_customer_gst_regdate')" />
													<div class="hint" id="hint_txt_customer_gst_regdate">
											</div>
											</div>

											<div class="row"></div>
											<div class="form-element6">
												<label>ARN:&nbsp; </label> <b>
												<input name="txt_customer_arn_no" type="text" class="form-control"
													id="txt_customer_arn_no"
													value="<%=mybean.customer_arn_no%>" size="10"
													maxlength="15"  /></b>
													<b>Example: AA0707160000001</b>
													<span id="arn"></span>
													<div class="hint" id="hint_txt_customer_arn_no"> </div>
											</div>

											<div class="form-element6">
												<label>Status<font color=red>*</font>:&nbsp;</label>
												<select name="dr_customer_itstatus_id"
													class="form-control" id="dr_customer_itstatus_id" 
													onchange="SecurityCheck('dr_customer_itstatus_id',this,'hint_dr_customer_itstatus_id')">
													<%=mybean.PopulateItStatus()%>
												</select>
												<div class="hint" id="hint_dr_customer_itstatus_id"> </div>
											</div>

											<div class="row"></div>
											
											<div class="form-element6">
												<label> Address<font color=red>*</font>:&nbsp; </label>
												<textarea name="txt_customer_address" cols="40" rows="4"
													class="form-control" id="txt_customer_address"
													onchange="SecurityCheck('txt_customer_address',this,'hint_txt_customer_address')"
													onKeyUp="charcount('txt_customer_address', 'span_txt_customer_address','<font color=red>({CHAR} characters left)</font>', '255')"
													><%=mybean.customer_address%></textarea>
												<span id="span_txt_customer_address"> (255 Characters)</span>
												<div class="hint" id="hint_txt_customer_address"> </div>
											</div>
											
											<div class="form-element6">
												<label> Landmark:&nbsp;</label>
												<textarea name="txt_customer_landmark" cols="40" rows="4"
													class="form-control" id="txt_customer_landmark"
													onchange="SecurityCheck('txt_customer_landmark',this,'hint_txt_customer_landmark')"
													onKeyUp="charcount('txt_customer_landmark', 'span_txt_customer_landmark','<font color=red>({CHAR} characters left)</font>', '255')"
													><%=mybean.customer_landmark%></textarea>
												<span id="span_txt_customer_landmark"> (255 Characters)</span>
												<div class="hint" id="hint_txt_customer_landmark"> </div>
											</div>
											<div class="row"></div>
											<div class="form-element6">
												<label> City<font color=red>*</font>:&nbsp; </label>
												<select class="form-control-select select2" id="maincity"
													name="maincity"
													onchange="SecurityCheck('maincity',this,'hint_maincity')">
													<%=mybean.citycheck.PopulateCities(mybean.customer_city_id, mybean.comp_id)%>
												</select>
												<div class="hint" id="hint_maincity"> </div>
											</div>
											
											<div class="form-element6">
												<label> Pin/Zip<font color=red>*</font>:&nbsp; </label>
												<input name="txt_customer_pin" type="text"
													class="form-control" id="txt_customer_pin"
													onchange="SecurityCheck('txt_customer_pin',this,'hint_txt_customer_pin')"
													value="<%=mybean.customer_pin%>" size="10" maxlength="6" />
					<!-- 							onKeyUp="toInteger('txt_customer_pin','Pin')" -->
											<div class="hint" id="hint_txt_customer_pin"> </div>
											</div>
											<div class="row"></div>
											<div class="form-element1 form-element-margin">
												<label> TDS:&nbsp;</label> 
												<input id="chk_customer_tds"
													type="checkbox" name="chk_customer_tds"
													onchange="SecurityCheck('chk_customer_tds',this,'hint_chk_customer_tds')"
													<%=mybean.PopulateCheck(mybean.customer_tds)%> />
													<div class="hint" id="hint_chk_customer_tds"> </div>
											</div>
											
											<div class="form-element5">
												<label>PAN:&nbsp;</label> 
												<input name="txt_customer_pan_no" type="text" class="form-control"
													id="txt_customer_pan_no"
													onchange="SecurityCheck('txt_customer_pan_no',this,'hint_txt_customer_pan_no')"
													value="<%=mybean.customer_pan_no%>" size="10"
													maxlength="10"></input><b>Format: ABCDF1234F</b>
													<span id="gst"></span>
													<div class="hint" id="hint_txt_customer_pan_no"> </div>
											</div>
											<div class="form-element6">
												<label>Customer Since :&nbsp; </label>
												 <input name="txt_customer_since" id="txt_customer_since"
													value="<%=mybean.customersince%>"
													onchange="SecurityCheck('txt_customer_since',this,'hint_txt_customer_since')"
													class="form-control datepicker" type="text" value="" />
													<div class="hint" id="hint_txt_customer_since"> </div>
											</div>
											
											<div class="row"></div>
											<%
												if (mybean.config_customer_sob.equals("1")) {
											%>
											<div class="form-element6">
												<label> Source of Business<font color=red>*</font>:&nbsp; </label>
												<select name="drop_customer_sob_id" class="form-control"
												onchange="SecurityCheck('drop_customer_sob_id',this,'hint_drop_customer_sob_id')"
													id="drop_customer_sob_id">
													<%=mybean.PopulateSob()%>
												</select>
												<div class="hint" id="hint_drop_customer_sob_id"> </div>
											</div>
											<%
													}
												%>
											<%
												if (mybean.config_customer_soe.equals("1")) {
											%>

											<div class="form-element6">
												<label> Source Of Enquiry<font color=red>*</font>:&nbsp; </label>
												<select name="drop_customer_soe_id" class="form-control"
													id="drop_customer_soe_id"
													onchange="SecurityCheck('drop_customer_soe_id',this,'hint_drop_customer_soe_id')" >
													<%=mybean.PopulateSoe()%>
												</select>
												<div class="hint" id="hint_drop_customer_soe_id"> </div>
											</div>
											<%
												}
											%>
											
											<div class="row"></div>
											
											<div class="form-element6">
												<label> Type<font color=red>*</font>:&nbsp; </label>
												<select name="dr_customer_type" class="form-control"
												onchange="SecurityCheck('dr_customer_type',this,'hint_dr_customer_type')"
													id="dr_customer_type">
													<%=mybean.PopulateType()%>
												</select>
												<div class="hint" id="hint_dr_customer_type"> </div>
											</div>

											<div class="form-element6">
												<label> Executive:&nbsp; </label> 
												<select name="dr_customer_emp_id" class="form-control"
													id="dr_customer_emp_id"
													onchange="SecurityCheck('dr_customer_emp_id',this,'hint_dr_customer_emp_id')" >
													<%=mybean.PopulateEmp()%>
												</select>
												<div class="hint" id="hint_dr_customer_emp_id"> </div>
											</div>
											<div class="row"></div>
											
											<div class="form-element6">
												<label> Notes:&nbsp;</label>
												<textarea name="txt_customer_address" cols="40" rows="4"
													class="form-control" id="txt_customer_address"
													onchange="SecurityCheck('txt_customer_notes',this,'hint_txt_customer_notes')"
													><%=mybean.customer_notes%></textarea>
													<div class="hint" id="hint_txt_customer_notes"> </div>
											</div>
											
											<div class="form-element6 form-element-margin">
												<label> Active:&nbsp;</label>
												 <input id="chk_customer_active" type="checkbox"
													name="chk_customer_active"
													onchange="SecurityCheck('chk_customer_active',this,'hint_chk_customer_active')"
													<%=mybean.PopulateCheck(mybean.customer_active)%> />
													<div class="hint" id="hint_chk_customer_active"> </div>
											</div>
												</form>
											</div>
										</div>
									</div>
								</div>
								<div class="tab-pane" id="tabs-2"></div>
								<div class="tab-pane " id="tabs-3"></div>
								<div class="tab-pane " id="tabs-4"></div>	
								<div class="tab-pane " id="tabs-5"></div>	
								<div class="tab-pane " id="tabs-6"></div>	
<!-- 								<div class="tab-pane " id="tabs-7"></div>	 -->
							    <div class="tab-pane " id="tabs-8"></div>	
								<div class="tab-pane " id="tabs-9"></div>	
								<div class="tab-pane " id="tabs-10"></div>	
								<div class="tab-pane " id="tabs-11"></div>	
								<div class="tab-pane " id="tabs-12"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
	</div>
	</div>
	<!-- END CONTAINER -->
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
	<script src="../assets/js/bootstrap-tagsinput.js" type="text/javascript"></script>
<!-- 	<script src="../assets/js/footable.js" type="text/javascript"></script> -->
	<script>
	$(function() {
		$("#txt_customer_gst_no").change(function(){
			var gst_no=$("#txt_customer_gst_no").val();
			var regex=/^([0-9]{2})([a-zA-Z]{5})([0-9]{4})([a-zA-Z]{1})([a-zA-Z0-9]{3})?$/;
			if(gst_no.length!=0 && gst_no.length < 15){
				$("#gst").html("<br><b><font color='red'>GSTIN is invalid</font></b>");
			}else if(gst_no.length!=0 && regex.test(gst_no) == false){
				$("#gst").html("<br><b><font color='red'>GSTIN is invalid</font></b>");
			}else{
				$("#gst").html("");
				SecurityCheck('txt_customer_gst_no',this,'hint_txt_customer_gst_no');
			}
		});
 	});
	</script>
	<script>
	  $("#txt_customer_gst_no,#txt_customer_arn_no,#txt_customer_pan_no").bind('keyup',function(e){
		  if(e.which >= 97 && e.which <= 122 ){
			  var newKey=e.which-32;
			  e.keyCode=newKey;
			  e.charCode=newKey;
		  }
		  $("#txt_customer_gst_no").val(($("#txt_customer_gst_no").val()).toUpperCase());
		  $("#txt_customer_arn_no").val(($("#txt_customer_arn_no").val()).toUpperCase());
		  $("#txt_customer_pan_no").val(($("#txt_customer_pan_no").val()).toUpperCase());
	  });
	</script>
	<script>
	$(function() {
		$("#txt_customer_arn_no").change(function(){
			var arn_no=$("#txt_customer_arn_no").val();
			var regex=/^([a-zA-Z]{2})([0-9]{6})([0-9]{7})([0-9]{1})?$/;
// 			alert("regex.test(arn_no)===="+regex.test(arn_no));
            if(arn_no.length!=0 && regex.test(arn_no)==false){
            	$("#arn").html("<br><b><font color='red'>ARN No. is invalid</font></b>");
            }else{
            	$("#arn").html("");
            	SecurityCheck('txt_customer_arn_no',this,'hint_txt_customer_arn_no');
            }			
		});
 	});
	</script>
	
	<script type="text/javascript">
// 		$(function() {
// 			$('table')
// 					.footable(
// 							{
// 								toggleHTMLElement : '<span><div class="footable-toggle footable-expand" border="0"></div>'
// 										+ '<div class="footable-toggle footable-contract" border="0"></div></span>'
// 							});
// 		});
		function SecurityCheck(name, obj, hint) {
			var customer_id = GetReplace(document.form1.customer_id.value);
			var url = "../customer/customer-dash-check.jsp?";
// 			var dat = document.getElementById("txt_enquiry_date").value;
			var str = "123";
// 			var fromdate = "";
			if (name != "chk_customer_active" && name!="chk_customer_tds") {
				var value = GetReplace(obj.value);
			} else {
				if (obj.checked == true) {
					var value = "1";
				} else {
					var value = "0";
				}
			}
			var param = "name=" + name + "&value=" + value + "&customer_id=" + customer_id ;
// 			alert(name+" "+value+" "+customer_id +" "+param);
 			showHint(url + param, hint);
		}
		function LoadCustomerDash(tab) {
			var customer_id = GetReplace(document.form1.customer_id.value);
			if (tab == '2') {
				if (document.getElementById("tabs-2").innerHTML == '') {
				showHint( 'customer-dash-check.jsp?contacts=yes&customer_id='
								+ customer_id, 'tabs-2');
				}
			} else if (tab == '3') {
				if (document.getElementById("tabs-3").innerHTML == '') {
					showHint(
							'customer-dash-check.jsp?enquiries=yes&customer_id='
									+ customer_id, 'tabs-3');
				}
			} 
			else if (tab == '4') {
				if (document.getElementById("tabs-4").innerHTML == '') {
				showHint(
						'customer-dash-check.jsp?salesorder=yes&customer_id='
								+ customer_id, 'tabs-4');
				}
		} 
			else if (tab == '5') {
				if (document.getElementById("tabs-5").innerHTML == '') {
				showHint(
						'customer-dash-check.jsp?vehicles=yes&customer_id='
								+ customer_id, 'tabs-5');
				}
		} 
			else if (tab == '6') {
				if (document.getElementById("tabs-6").innerHTML == '') {
				showHint(
						'customer-dash-check.jsp?jobcards=yes&customer_id='
								+ customer_id, 'tabs-6');
				}
		} 
// 			else if (tab == '7') {
// 				if (document.getElementById("tabs-7").innerHTML == '') {
// 				showHint(
// 						'customer-dash-check.jsp?insurance=yes&customer_id='
// 								+ customer_id, 'tabs-7');
// 				}
// 		} 
			else if (tab == '8') {
				if (document.getElementById("tabs-8").innerHTML == '') {
				showHint(
						'customer-dash-check.jsp?tickets=yes&customer_id='
								+ customer_id, 'tabs-8');
				}
		} 
			else if (tab == '9') {
				if (document.getElementById("tabs-9").innerHTML == '') {
				showHint(
						'customer-dash-check.jsp?coupon=yes&customer_id='
								+ customer_id, 'tabs-9');
				}
		} 
			else if (tab == '10') {
				if (document.getElementById("tabs-10").innerHTML == '') {
				showHint(
						'customer-dash-check.jsp?invoice=yes&customer_id='
								+ customer_id, 'tabs-10');
				}
		} 
			else if (tab == '11') {
				if (document.getElementById("tabs-11").innerHTML == '') {
				showHint(
						'customer-dash-check.jsp?receipts=yes&customer_id='
								+ customer_id, 'tabs-11');
				}
		} 
			else if (tab == '12') {
				if (document.getElementById("tabs-12").innerHTML == '') {
				showHint(
						'customer-dash-check.jsp?history=yes&customer_id='
								+ customer_id, 'tabs-12');
				}
		} 
		}
	
	</script>
		<!-- START OF TAGS CONFIGURATION -->
	<script src="../assets/js/bootstrap-tagsinput.js"
		type="text/javascript"></script>
	<script type="text/javascript">
		//	THIS IS TO POPULATE VALUE IN TAG-CONTAINER AT THE TIME OF PAGE LOAD
	<%=mybean.tagcheck.PopulateTags(mybean.customer_id,
					mybean.comp_id)%>
		//	THIS IS TO ADD TAGS IN TAG-CONTAINER ON CLICK FROM POPOVER
	<%=mybean.tagcheck.PopulateTagsJS(mybean.customer_id,
					mybean.comp_id)%>
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
			$("#enquiry_tags").on('itemRemoved',
							function() {

								var url = "../customer/customer-tags-check.jsp?";

								var param = "update=yes&tags=" + $("#enquiry_tags").val() + "&customer=yes&customer_id=" + <%=mybean.customer_id%>+"&enquiry_id=0";
								var hint = "hint_enquiry_tags";
								var param2 = "tags_content=yes&customer=yes&customer_id="+ <%=mybean.customer_id%>;
								var hint2 = "popover-content";

								setTimeout('showHint("' + url + param2 + '", "'
										+ hint2 + '")', 100);

								setTimeout(
										'if($("#enquiry_tags").val()==""){ addNoTag(); }',
										150);

								showHint(url + param, hint);

							});
		});

		function tagcall(idname) {
			var url = "../customer/customer-tags-check.jsp?";
			var param1 = "add=yes&tags=" + idname + "&customer=yes&enquiry_id=0&customer_id=" + <%=mybean.customer_id%> ;
			var hint1 = "hint_enquiry_tags";

			var param2 = "tags_content=yes&customer_id="+ <%=mybean.customer_id%>;
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
	
</body>
</html>