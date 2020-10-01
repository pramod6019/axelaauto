<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Vehicle_Update"
	scope="request" />
<%mybean.doGet(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" />
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<%@include file="../Library/css.jsp"%>
</HEAD>
<body onLoad="FormFocus()"
	class="page-container-bg-solid page-header-menu-fixed">
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
						<h1><%=mybean.status%> Vehicle </h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<div class="page-content-inner">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../service/vehicle.jsp">Vehicles</a> &gt;</li>
						<li><a href="vehicle-list.jsp?all=yes">List Vehicles</a>&gt;</li>
						<li><a href="vehicle-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Vehicle</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%>&nbsp;Vehicle
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="form1" class="form-horizontal" method="post">
											<input type="hidden" id="txt_prev_contact_id"
												name="txt_prev_contact_id" value="<%=mybean.prev_contact_id%>">
											<center>
												<font size="1">Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b> are required.
												</font>
											</center>
											<br>
											<%if(mybean.status.equals("Add") && mybean.contact_id.equals("0")){%>
												<div class="row " id="customer_name">
													<div class="form-element6">
														<label> Customer: </label>
														<input name="txt_customer_name" type="text" class="form-control" id="txt_customer_name"
															value="<%=mybean.customer_name%>" size="50" maxlength="255" />
													</div>
	
													<div class="form-element6">
														<label> Branch<font color="#ff0000">*</font>: </label>
														<select name="dr_branch" class="form-control" id="dr_branch" onchange="PopulateModel(this.value)">
															<%=mybean.PopulateBranch(mybean.customer_branch_id,"", "3", "", request)%>
														</select>
														<%if(mybean.emp_role_id.equals("1")) {%>
															<div class="admin-master">
																<a href="../portal/branch-list.jsp?all=yes" title="Manage Branch"></a>
															</div>
														<%}%>
													</div> 
												</div>

											<div class="form-element12" id="contact_name" >
												<div class="form-element2 form-element">
													<label >Contact Name<font color="#ff0000">*</font>: </label>
													 <select name="dr_title" class="form-control" id="dr_title">
															<%=mybean.PopulateTitle(mybean.comp_id)%>
													</select> Title
												</div>
												<div class="form-element5 form-element-margin ">
													<input name="txt_contact_fname" type="text" class="form-control" id="txt_contact_fname"
														value="<%=mybean.contact_fname%>" size="32" maxlength="255" onkeyup="ShowNameHint()" />First Name
													<div class="hint" id="hint_txt_contact_fname"></div>
												</div>
												<div class="form-element5 form-element-margin form-element">
													<input name="txt_contact_lname" type="text" class="form-control" id="txt_contact_lname"
														value="<%=mybean.contact_lname%>" size="32" maxlength="255" onkeyup="ShowNameHint()" />Last Name
												</div>
											</div>
											<div class="form-element6" id="contact_mobile">
												<label > Mobile 1<font color="#ff0000">*</font>: </label>
												<input name="txt_contact_mobile1" type="text" class="form-control" id="txt_contact_mobile1"
													value="<%=mybean.contact_mobile1 %>" size="30" maxlength="13"
													onKeyUp="showHint('report-check.jsp?contact_mobile1='+GetReplace(this.value)+'&search=yes','search-contact');toPhone('txt_contact_mobile1','Mobile1');" />
													(91-9999999999)
											</div>
											<div class="form-element6">
												<label> Mobile 2: </label>
												<input name="txt_contact_mobile2" type="text" class="form-control" id="txt_contact_mobile2"
													value="<%=mybean.contact_mobile2 %>" size="30" maxlength="13"
													onKeyUp="showHint('report-check.jsp?contact_mobile2='+GetReplace(this.value)+'&search=yes','search-contact');toPhone('txt_contact_mobile2','Mobile2');" />
													(91-9999999999)
											</div>
											<div class="form-element6" id="contact_email">
												<label > Email 1: </label>
												<input name="txt_contact_email1" type="text" class="form-control" id="txt_contact_email1"
													value="<%=mybean.contact_email1%>" size="35" maxlength="255"
													onKeyUp="showHint('report-check.jsp?contact_email1='+GetReplace(this.value)+'&search=yes','search-contact');" />
											</div>
											<div class="form-element6">
												<label > Email 2: </label>
												<input name="txt_contact_email2" type="text" class="form-control" id="txt_contact_email2"
													value="<%=mybean.contact_email2%>" size="35" maxlength="255"
													onKeyUp="showHint('report-check.jsp?contact_email2='+GetReplace(this.value)+'&search=yes','search-contact');" /> 
											</div>
												<div class="row">
												<div class="form-element6" id="contact_address">
														<label > Address<font color="#ff0000">*</font>: </label>
															<textarea name="txt_contact_address" rows="5"
																class="form-control" id="txt_contact_address"
																onKeyUp="charcount('txt_contact_address', 'span_txt_contact_address','&lt;font color=red&gt;({CHAR} characters left)&lt;/font&gt;', '255')"><%=mybean.contact_address%></textarea>
															<span id="span_txt_contact_address"> (255
																Characters)</span>
															<span id="search-contact"></span>
												</div>
											<div class="form-element6 " id="contact_state">
														<label > State<font color="#ff0000">*</font>: </label>
															<span id="contact_state_id"><%=mybean.PopulateState(mybean.state_id,"contact_city_id","dr_contact_state_id", mybean.comp_id)%></span>
															<%if(mybean.emp_role_id.equals("1")) {%>
															<div class="admin-master">
																<a href="../portal/managestate.jsp?all=yes"
																	title="Manage State"></a>
															</div>
															<%}%>
											</div>
											
											<div class="form-element6 " id="contact_city">
														<label > City<font color="#ff0000">*</font>: </label>
															<span id="contact_city_id">
															<%=mybean.PopulateCity(mybean.state_id, mybean.contact_city_id, "dr_contact_city_id", mybean.comp_id)%></span>
															<%
																if (mybean.emp_role_id.equals("1")) {
															%>
															<div class="admin-master">
																<a href="../portal/managecity.jsp?all=yes"
																	title="Manage City"></a>
															</div>
															<%
																}
															%>
												</div>
</div>
											<div class="row">
												<div class="form-element6">
														<label > Pin/Zip<font color="#ff0000">*</font>: </label>
															<input name="txt_contact_pin" type="text"
																class="form-control" id="txt_contact_pin"
																onKeyUp="toInteger('txt_contact_pin','Pin')"
																value="<%=mybean.contact_pin%>" size="10" maxlength="6" />
												</div>
												
												<div class="form-element6  ">
											<input name="veh_so_id" type="hidden" value="<%=mybean.so_id %>"/>
											<input name="veh_so_no" type="hidden" value="<%=mybean.veh_so_no %>"/> 
														<label  > Sales Order: </label>
														<%if(mybean.link_so_name.equals("")){ %>
															<input name="txt_veh_so_id" id="txt_veh_so_id"
																type="text" class="form-control"
																 size="20" value="<%=mybean.veh_so_id %>"
																maxlength="25" />
														<%} else{ %>
														<span  id="link_so_name" name="link_so_name">
																<%=mybean.link_so_name%>
														</span>
														<%} %>
											</div>
												
												</div>
											<!-- <div class="container-fluid " id=selected_contact
												style="display: none">
												<div class="form-body col-md-6 col-sm-6">
													<div class="form-group">
														<label class="col-md-3 control-label">
															Customer: </label>
														<div class="col-md-6" style="top: 9px">
															<b><span id="selected_customer_id"
																name="selected_customer_id"></span></b>
															
															
														</div>
													</div>
												</div>

												<div class="form-body col-md-6 col-sm-6">
													<div class="form-group">
														<label class="col-md-3 control-label">
															Contact: </label>
														<div class="col-md-6" style="top: 9px">
															<b><span id="selected_contact_id"
																name="selected_contact_id"></span></b> <a href="#"
																id="dialog_link">(Select Contact)</a>
															<div id="dialog-modal"></div>
														</div>
													</div>
												</div>


											</div> -->

											<%
												} else {
												
											%>
											<!-- to get the branch id in update mode for populating variant -->
												<input type="hidden" id="dr_branch" value="<%=mybean.customer_branch_id%>"/>
												<div class="form-element6 " id="contact_link">
													<label> Customer: </label>
													<b><span id="span_veh_customer_id" name="span_veh_customer_id"><%=mybean.link_customer_name%></span></b>&nbsp;
												</div>

												<div class="form-element6">
													<label > Contact: </label>
													<b><span id="span_veh_contact_id" name="span_veh_contact_id"><%=mybean.link_contact_name%></span></b>&nbsp;
														<!-- <a href="#" id="dialog_link">(Select Contact)</a> -->
													<div id="dialog-modal"></div>
												</div>
											<%}%>
											
											
											
											<div class="form-element6 ">
<!-- 														<label > Model<font color="#ff0000">*</font>: </label> -->
<!-- 															<span id="Hintmodel"> -->
<%-- 															 <%=mybean.PopulateModel(mybean.comp_id, mybean.customer_branch_id)%> --%>
<!-- 															</span> -->
<!-- 															<div class="admin-master"> -->
<!-- 																<a href="../inventory/item-model.jsp?all=yes" -->
<!-- 																	title="Manage Model"></a> -->
<!-- 															</div> -->
													<input name="span_acct_id" type="hidden" id="span_acct_id" value="<%=mybean.customer_id%>" />
													<input name="acct_id" type="hidden" id="acct_id" value="<%=mybean.veh_customer_id%>" />
													<input name="span_cont_id" type="hidden" id="span_cont_id" value="<%=mybean.contact_id%>" />
													<input name="cont_id" type="hidden" id="cont_id" value="<%=mybean.veh_contact_id%>" />
													<input type="hidden" name="txt_status" id="txt_status" value="<%=mybean.status%>" />
											</div>
											
											<div class="row"></div>
											
											<div class="form-element6 ">
												<label> Variant <font color="#ff0000">*</font>: </label>
												<select class="form-control select2" id="servicepreownedvariant" name="servicepreownedvariant">
													<%=mybean.variantcheck.PopulateVariant(mybean.veh_variant_id,mybean.comp_id)%>
												</select>
											</div>
											
											<div class="form-element6 ">
												<label > Model Year<font color="#ff0000">*</font>: </label>
												<input name="txt_veh_modelyear" id="txt_veh_modelyear" type="text" class="form-control yearpicker"
													value="<%=mybean.veh_modelyear%>" size="10" />
											</div>
											
											<div class="form-element3 ">
												<label > Chassis Number<font color="#ff0000">*</font>: </label>
													<input name="txt_veh_chassis_no" id="txt_veh_chassis_no" maxlength="25"
														type="text" class="form-control" value="<%=mybean.veh_chassis_no%>" size="20" />
											</div>
											
											<div class="form-element3 ">
												<label>Engine Number: </label>
												<input name="txt_veh_engine_no" id="txt_veh_engine_no" type="text" class="form-control"
													value="<%=mybean.veh_engine_no%>" size="20" maxlength="25" />
											</div>
											
											<div class="form-element3 ">
												<label>FASTag ID<font color="#ff0000">*</font>: </label>
												<input name="txt_veh_fastag" id="txt_veh_fastag" type="text" class="form-control"
													value="<%=mybean.veh_fastag%>" size="25" maxlength="20" />
											</div>
											
											<div class="form-element3">
												<label>Reg. Number<font color="#ff0000">*</font>: </label>
												<input name="txt_veh_reg_no" id="txt_veh_reg_no" type="text" class="form-control"
													value="<%=mybean.veh_reg_no%>" size="20" maxlength="20" />
											</div>

											<div class="form-element6">
												<label>Sale Date<b><font color="#ff0000">*</font></b>: </label>
												<input name="txt_veh_sale_date" type="text" class="form-control datepicker"
													 id="txt_veh_sale_date" value="<%=mybean.vehsaledate%>" size="12" maxlength="10" />
											</div>
											
											<div class="form-element6 ">
												<label>Sale Amount: </label>
												<input name="txt_veh_sale_amount" type="text" class="form-control"
													onkeyup="toInteger('txt_veh_sale_amount')" id="txt_veh_sale_amount"
													value="<%=mybean.veh_sale_amount%>" size="12" maxlength="10" />
											</div>
											
											<div class="form-element6 ">
												<label> Veh. Source<b><font color="#ff0000">*</font></b>: </label>
												<select name="dr_veh_vehsource_id" type="text" class="form-control" id="dr_veh_vehsource_id">
													<%=mybean.PopulateVehSource(mybean.comp_id)%>
												</select>
											</div>
											
											<div class="form-element6 ">
												<label > Kms<b><font color="#ff0000">*</font></b>: </label>
												<input name="txt_veh_kms" id="txt_veh_kms" type="text" class="form-control" value="<%=mybean.veh_kms%>"
													size="20" maxlength="10" onkeyup="toInteger(this.id);" />
											</div>
											<div class="row"></div>
											<div class="form-element6 ">
												<label > Last Service Date: </label>
												<input name="txt_veh_lastservice" id="txt_veh_lastservice" type="text"
													class="form-control datepicker" value="<%=mybean.veh_lastservice%>" size="10" maxlength="10" />
											</div>
											
											<div class="form-element6 ">
												<label > Last Service Kms: </label>
												<input name="txt_veh_lastservice_kms" id="txt_veh_lastservice_kms" type="text"
													class="form-control" value="<%=mybean.veh_lastservice_kms%>" size="10" maxlength="10" />
											</div>
											
											<div class="form-element6 ">
												<label> Service Due Kms: </label>
												<input name="txt_veh_service_duekms" id="txt_veh_service_duekms" type="text" size="20" maxlength="11"
													class="form-control" value="<%=mybean.veh_service_duekms%>"  onkeyup="toInteger(this.id);" />

											</div>
											
											<div class="form-element6 ">
												<label > Service Due Date: </label>
												<input name="txt_veh_service_duedate" id="txt_veh_service_duedate" type="text"
													class="form-control datepicker" value="<%=mybean.service_duedate%>" size="12" maxlength="10" />
											</div>
											
											<div class="form-element6 ">
												<label > Warranty Expiry Date: </label>
												<input name="txt_veh_warranty_expirydate" id="txt_veh_warranty_expirydate" type="text"
													class="form-control datepicker" value="<%=mybean.vehwarrantyexpirydate%>" size="12" maxlength="10" />
											</div>
											
											<div class="form-element6 ">
												<label > CRM Executive<font color="#ff0000">*</font>: </label>
												<select id="dr_veh_crmemp_id" name="dr_veh_crmemp_id" class="form-control">
														<%=mybean.PopulateCRMExecutive(mybean.comp_id)%>
												</select>
											</div>
											
											<div class="form-element6 ">
												<label > Service Advisor<font color="#ff0000">*</font>: </label>
												<select id="dr_veh_emp_id" name="dr_veh_emp_id" class="form-control">
													<%=mybean.PopulateServiceExecutive(mybean.comp_id)%>
												</select>
											</div>
											
											<div class="row">
											
											<div class="form-element6 ">
												<label > Notes: </label>
												<textarea name="txt_veh_notes" cols="70" rows="5"
													class="form-control" id="txt_veh_notes"><%=mybean.veh_notes%></textarea> 
											</div>
											
											</div>
											<% if (mybean.status.equals("Update") &&!(mybean.veh_entry_by == null) && !(mybean.veh_entry_by.equals(""))) { %>
												<div class="form-element6 ">
													<label > Entry By: </label>
														<%=mybean.unescapehtml(mybean.veh_entry_by)%>
												</div>
											<%} %>
											<% if (mybean.status.equals("Update") &&!(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) { %>
												<div class="form-element6 ">
													<label > Entry Date: </label>
														<%=mybean.entry_date%>
												</div>
											<%} %>
											<% if (mybean.status.equals("Update") &&!(mybean.veh_modified_by == null) && !(mybean.veh_modified_by.equals(""))) { %>
												<div class="form-element6 ">
													<label > Modified By: </label>
														<%=mybean.unescapehtml(mybean.veh_modified_by)%>
												</div>
											<%} %>
											<% if (mybean.status.equals("Update") &&!(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) { %>
												<div class="form-element6 ">
													<label > Modified Date: </label>
														<%=mybean.modified_date%>
												</div>
											<%} %>
											<%if(mybean.status.equals("Add")){%>
												<center>
													<div class="form-element12 form-center">
														<input name="addbutton" type="submit" class="btn btn-success" id="addbutton" value="Add Vehicle"
															onclick="return SubmitFormOnce(document.form1,this);" />
														<input type="hidden" id="add_button" name="add_button" value="yes" />
													</div>
												</center>
											<%}else if (mybean.status.equals("Update")){%>
												<center>
												<div class="form-element12">
													<input type="hidden" id="update_button" name="update_button" value="yes" />
													<input name="updatebutton" type="submit" class="btn btn-success" id="updatebutton"
														value="Update Vehicle" onclick="return SubmitFormOnce(document.form1,this);" />
													<input name="delete_button" type="submit" class="btn btn-success"
														id="delete_button" onclick="return confirmdelete(this)" value="Delete Vehicle" />
												</div>
												</center>
											<%}%>
												<input type="hidden" name="veh_entry_by" value="<%=mybean.veh_entry_by%>" />
												<input type="hidden" name="entry_date" value="<%=mybean.entry_date%>" />
												<input type="hidden" name="veh_modified_by" value="<%=mybean.veh_modified_by%>" />
												<input type="hidden" name="modified_date" value="<%=mybean.modified_date%>" />
										</form>
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
	 <script>  
	// JavaScript Document
// $(function(){
//     // Dialog
//     $('#dialog-modal').dialog({
//         autoOpen: false,
//         width: 900,
//         height: 500,
//         zIndex: 200,
//         modal: true,
//         title: "Select Sales Order"
//     });
//     $('#dialog_link').click(function(){

//         $.ajax({
//             //url: "home.jsp",
//             success: function(data){
//                 $('#dialog-modal').html('<iframe src="../customer/customer-contact-list.jsp?group=select_veh_contact" width="100%" height="100%" frameborder=0></iframe>');
//             }
//         });
// 		$('#dialog-modal').dialog('open');
//         return true;
//     });
// });
 
// $(function(){
//     // Dialog
//     $('#dialog-modal').dialog({
//         autoOpen: false,
//         width: 900,
//         height: 500,
//         zIndex: 200,
//         modal: true,
//         title: "Select Sales Order"
//     });
//     $('#veh_so_link').click(function(){

//         $.ajax({
//             //url: "home.jsp",
//             success: function(data){
//                 $('#dialog-modal').html('<iframe src="../sales/veh-salesorder-list.jsp?group=select_veh_so" width="100%" height="100%" frameborder=0></iframe>');
//             }  
//         }); 
// 		$('#dialog-modal').dialog('open');
//         return true;
//     });
// });
	     
  /*  $(function() { 
    $( "#txt_veh_modelyear" ).datepicker({
		dateFormat: 'yy',
		stepMinute: 5    
		});	
	 	$( "#txt_veh_sale_date" ).datepicker({
		dateFormat: 'dd/mm/yy',
		stepMinute: 5
		});
		
		$( "#txt_veh_warranty_expirydate" ).datepicker({
		dateFormat: 'dd/mm/yy',
		stepMinute: 5
		});
		
		$( "#txt_veh_service_duedate" ).datepicker({
		dateFormat: 'dd/mm/yy',
		});
		
		txt_veh_lastservice
		$( "#txt_veh_lastservice" ).datepicker({
			dateFormat: 'dd/mm/yy',
		});
		
		$( "#txt_veh_renewal_date" ).datepicker({
		dateFormat: 'dd/mm/yy',
		});
	}); */
</script>
    <script language="JavaScript" type="text/javascript">
    
    $("#txt_veh_fastag").bind('keyup',function(e){
		$("#txt_veh_fastag").val(($("#txt_veh_fastag").val()).toUpperCase());
	});

function FormFocus() { //v1.0
  //document.form1.txt_stock_name.focus();
}
function PopulateModel(branch_id){
	
	showHint("vehicle-dash-check.jsp?veh_branch_id="+branch_id+"&model=yes", "Hintmodel");
	//alert("branch_id-----"+branch_id);
}
 function PopulateItem(model_id){
				showHint("vehicle-dash-check.jsp?item_model_id="+model_id+"&list_model_item=yes", "model-item");
				}
				
			function ShowNameHint()
				{
					var contact_fname = document.getElementById("txt_contact_fname").value;
					var contact_lname = document.getElementById("txt_contact_lname").value;
					showHint("../service/report-check.jsp?contact_fname="+contact_fname+"&contact_lname="+contact_lname+"&search=yes", "search-contact");
					}    
					
					//For selecting existing contact
function SelectContact(contact_id, contact_name, customer_id, customer_name){
	var contact_link = '<a href="../customer/customer-contact-list.jsp?contact_id='+contact_id+'"> '+contact_name+ '</a>';
    var customer_link = '<a href="../customer/customer-list.jsp?customer_id='+customer_id+'">' +customer_name+ '</a>';
	var status = document.getElementById("txt_status").value;
	var selected_customer = document.getElementById("span_acct_id").value;
    var quote_customer = document.getElementById("acct_id").value;
//        alert("selected_customer=="+selected_customer);
	if(status!='Update' && selected_customer==0){
	document.getElementById("customer_name").style.display = 'none';
    document.getElementById("contact_name").style.display = 'none';
    document.getElementById("contact_mobile").style.display = 'none';
//    document.getElementById("contact_phone").style.display = 'none';
    document.getElementById("contact_email").style.display = 'none';
	document.getElementById("contact_address").style.display = 'none';
    document.getElementById("contact_city").style.display = 'none';
    document.getElementById("contact_state").style.display = 'none';
	document.getElementById("selected_contact").style.display = '';
	}
    document.getElementById("span_cont_id").value = contact_id;
    document.getElementById("span_acct_id").value = customer_id;
	if(status=='Add' && quote_customer==0){
	document.getElementById("selected_contact_id").innerHTML = contact_link;
    document.getElementById("selected_customer_id").innerHTML = customer_link;
	}else if(status=='Update' || selected_customer!=0){
    document.getElementById("span_veh_contact_id").innerHTML = contact_link;
    document.getElementById("span_veh_customer_id").innerHTML = customer_link;
	}
	if(so_id=='0'){
	document.getElementById("search-contact").innerHTML = '';
	}
	$('#dialog-modal').dialog('close'); 
}

function SelectVehSO(so_id, so_no)
{
	document.getElementById("span_so_id").value = so_id;
	document.getElementById("span_veh_so_id").innerHTML = "<a href=../sales/veh-salesorder-list.jsp?so_id="+so_id+">" +so_no+ "</a>";
	$('#dialog-modal').dialog('close'); 
}

function RemoveVehSO(){
	document.getElementById("span_so_id").value = "0";
	document.getElementById("span_veh_so_id").innerHTML = "";
	}
	</script>
</body>
</HTML>