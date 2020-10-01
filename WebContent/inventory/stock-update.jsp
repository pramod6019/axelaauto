<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.inventory.Stock_Update"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>

<style>
#brand_id {
	display: none;
}
#displayCommission {
	display: none;
}
</style>

</HEAD>
<body onLoad="FormFocus();getBrandID();"
	class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>

	<!-- BEGIN CONTAINER
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1><%=mybean.status%>&nbsp;Stock </h1>
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
						<li><a href="../inventory/index.jsp">Inventory</a> &gt;</li>
						<li><a href="../inventory/stock.jsp">Stock</a> &gt;</li>
						<li><a href="stock-list.jsp?all=recent">List Stock</a> &gt;</li>
						<li><a href="stock-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Stock</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<form name="form1" method="post" class="form-horizontal">
								<center>
									<font color="#ff0000"><b><%=mybean.msg%></b></font> <br>
								</center>
								<div class="portlet box">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">
											<%=mybean.status%>&nbsp;Stock
										</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="container-fluid">
											<!-- START PORTLET BODY -->
											 <center>											
													<font>Form fields marked with a red asterisk <b><font
															color="#ff0000">*</font></b> are required.<br>
											 </center>	

											<div class="form-element6">
												<label>Branch<font color="#ff0000">*</font>: </label>
													<select name="dr_vehstock_branch_id" class="form-control"
														id="dr_vehstock_branch_id"
														onChange="PopulateModel();PopulatePaintWork();getBrandID();">
														<%if(mybean.add.equals("yes")){ %>
														<%=mybean.PopulateBranch(mybean.vehstock_branch_id, "", "1", "", request)%>
													<%}else{ %>
													<%=mybean.PopulateBranch(mybean.vehstock_branch_id, "", "1", mybean.branch_brand_id, request)%>
													<%} %>
													</select>
											</div>

											<div class="form-element6">
												<label> Location<font color="#ff0000">*</font>: </label>
													<span id="branchexe"> <%=mybean.PopulateLocation()%>
													</span>
											</div>
											
											<div class="form-element6">
												<label>Model<font color="#ff0000">*</font>: </label>
													<span id="branchmodel">                   
														<%=mybean.PopulateModel(mybean.comp_id, mybean.vehstock_branch_id)%>
													</span>
											</div>

											<div class="form-element6">
												<label>Variant<font color="#ff0000">*</font>: </label>
													<span id="branchitem">                   
														<%=mybean.PopulateItem(mybean.comp_id, mybean.vehstock_model_id, mybean.vehstock_branch_id)%>
													</span>
											</div>
<!-- // -->
											<div class="form-element6">
												<label>Model Year<font color="#ff0000">*</font>: </label>
													<input name="txt_vehstock_modelyear" id="txt_vehstock_modelyear"
														class="form-control yearpicker" 
														type="text" value="<%=mybean.vehstock_modelyear%>" size="10"
														maxlength="4" />
											</div>
											
											<div hidden>
											<div name="brand_id" id="brand_id" hidden></div>
											</div>
<%-- 											<% if (mybean.branch_brand_id.equals("52") || mybean.branch_brand_id.equals("55") ||  mybean.branch_brand_id.equals("56")) { %> --%>
											
											<div id="displayCommission">
											<div class="row"></div>
											<div class="form-element4">
												<label>Manufactured Year:</label>
													<input name="txt_vehstock_mfgyear" id="txt_vehstock_mfgyear"
														type="text" class="form-control yearpicker"
														value="<%=mybean.vehstock_mfgyear%>"
														onKeyUp="toInteger('txt_vehstock_mfgyear','Manufactured Year')"
														size="10" maxlength="4" />
											</div>

											<div class="form-element4">
												<label>Reference Number:</label>
													<input name="txt_vehstock_ref_no" id="txt_vehstock_ref_no"
														type="text" class="form-control"
														value="<%=mybean.vehstock_ref_no%>" size="20" maxlength="15" />
											</div>

											<div class="form-element4">
												<label>Commission Number<span id="commission"><font color="#ff0000">*</font></span>: </label>
													<input name="txt_vehstock_comm_no" id="txt_vehstock_comm_no"
														type="text" class="form-control"
														value="<%=mybean.vehstock_comm_no%>" size="20" maxlength="15" />
											</div>
											<div class="row"></div>
											</div>
<%-- 											<% } %> --%>
				
											<div class="form-element6">
												<label>Chassis Prefix:</label>
													<input name="txt_vehstock_chassis_prefix"
														id="txt_vehstock_chassis_prefix" type="text"
														class="form-control"
														value="<%=mybean.vehstock_chassis_prefix%>" size="20"
														maxlength="15" />
											</div>

											<div class="form-element3">
												<label>Chassis Number<span id="chassis"><font color="#ff0000">*</font></span>:</label>
													<input name="txt_vehstock_chassis_no"
														id="txt_vehstock_chassis_no" type="text" class="form-control"
														value="<%=mybean.vehstock_chassis_no%>" size="22"
														maxlength="25" />
											</div>
											
											<div class="form-element3">
												<label>FASTag ID: </label>
												<input name="txt_vehstock_fastag" id="txt_vehstock_fastag" type="text"
													class="form-control" value="<%=mybean.vehstock_fastag%>" size="25" maxlength="20" />
											</div>

											<div class="form-element6">
												<label> Paintwork: </label>
													<span id="paintwork"> <!--                     <select name="dr_vehstock_paintwork_id" class="form-control" id="dr_vehstock_paintwork_id" > -->
														<%=mybean.PopulatePaintWork(mybean.comp_id, mybean.vehstock_branch_id)%>
														<!--                       </select> -->
													</span>
											</div>

											<div class="form-element3">
												<label>Engine Number<span id="engine"><font color="#ff0000">*</font></span>: </label>
													<input name="txt_vehstock_engine_no" id="txt_vehstock_engine_no"
														type="text" class="form-control"
														value="<%=mybean.vehstock_engine_no%>" size="22"
														maxlength="25" />
											</div>
											<div class="form-element3">
												<label>Parking Number:</label>
													<input name="txt_vehstock_parking_no"
														id="txt_vehstock_parking_no" type="text" class="form-control"
														value="<%=mybean.vehstock_parking_no%>"
														onKeyUp="toInteger('txt_vehstock_parking_no','Parking Number')"
														size="10" maxlength="10" />
											</div>
											<div class="row"></div>
											<%
												if (mybean.branch_brand_id.equals("52")
														|| mybean.branch_brand_id.equals("55")) {
											%>
											<div class="form-element6">
												<label>Confirmed SGW:</label>
												<input name="txt_vehstock_confirmed_sgw"
													id="txt_vehstock_confirmed_sgw" type="text" class="form-control datepicker"
													value="<%=mybean.confirmed_sgw%>" size="11" maxlength="10" />
											</div>
											<%
												}
											%>
											<div class="form-element6">
												<label>Ordered Date:</label>
												<input name="txt_vehstock_ordered_date" id="txt_vehstock_ordered_date"
													class="form-control datepicker" type="text"
													value="<%=mybean.ordered_date%>" size="11" maxlength="10" />
											</div>
											
											<div class="form-element6">
												<label>Invoice Number: </label>
												<input name="txt_vehstock_invoice_no"
													id="txt_vehstock_invoice_no" type="text" class="form-control"
													value="<%=mybean.vehstock_invoice_no%>" size="20" maxlength="30" />
											</div>

											<div class="form-element6">
												<label>Invoice Date:</label>
												<input name="txt_vehstock_invoice_date" id="txt_vehstock_invoice_date"
													class="form-control datepicker" type="text"
													value="<%=mybean.invoice_date%>" size="11" maxlength="10" />
											</div>

											<div class="form-element6">
												<label> Invoice	Amount:</label>
												<input name="txt_vehstock_invoice_amount" type="text"
													class="form-control" id="txt_vehstock_invoice_amount"
													onKeyUp="toNumber('txt_vehstock_invoice_amount','Car Cost')"
													value="<%=mybean.vehstock_invoice_amount%>" size="11" maxlength="10" />
											</div>

											<div class="form-element6">
												<label>Arrival Date:</label>
												<input name="txt_vehstock_arrival_date" id="txt_vehstock_arrival_date"
													class="form-control datepicker" type="text"
													value="<%=mybean.arrival_date%>" size="11" maxlength="10" />
											</div>

											<div class="form-element6">
												<label>PDI Date:</label>
												<input name="txt_vehstock_pdi_date" id="txt_vehstock_pdi_date"
													class="form-control datepicker" type="text"
													value="<%=mybean.vehstock_pdi_date%>" size="11" maxlength="10" />
											</div>
											
											<div class="form-element6">
												<label>DMS Date:</label>
												<input name="txt_vehstock_dms_date" id="txt_vehstock_dms_date"
													class="form-control datepicker" type="text"
													value="<%=mybean.vehstock_dms_date%>" size="11" maxlength="10" />
											</div>

											<div class="form-element6">
												<label>NSC:</label>
												<input name="txt_vehstock_nsc" id="txt_vehstock_nsc" type="text"
													class="form-control" value="<%=mybean.vehstock_nsc%>" size="20" maxlength="25" />
											</div>

											<div class="form-element6">
												<label>Delivery	Status<font color="#ff0000">*</font>:</label>
												<select name="dr_vehstock_delstatus_id" class="form-control" id="dr_vehstock_delstatus_id">
													<%=mybean.PopulateDeliveryStatus()%>
												</select>
											</div>

											<div class="form-element5">
												<label> Status<font	color="#ff0000">*</font>:</label>
												<select name="dr_vehstock_status_id" class="form-control" id="dr_vehstock_status_id">
													<%=mybean.PopulateStatus()%>
												</select>
											</div>

											<div class="form-element1 form-element-margin">
												<label>Blocked: </label>
												<input id="chk_vehstock_blocked" type="checkbox" name="chk_vehstock_blocked"
													<%=mybean.PopulateCheck(mybean.vehstock_blocked)%> />
											</div>
											
											<div class="form-element6">
												<label>Incentive:</label>
												<input name="txt_vehstock_incentive" id="txt_vehstock_incentive" type="text"
													onKeyUp="toNumber('txt_vehstock_incentive', 'Incentive')"
													class="form-control" value="<%=mybean.vehstock_incentive%>" size="20" maxlength="25" />
											</div>	
											<div class="form-element6 form-element-margin">
												<label> Invoice Amount After tax:</label>
												<label> <%=mybean.vehstock_invoiceamountaftertax %></label>
											</div>											
										</div>
									</div>
								</div>
								<div class="portlet box ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">In Transit
											Damage</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<br>
											<div class="form-element6">
												<label>Rectification Date:</label>
													<input name="txt_vehstock_rectification_date"
														id="txt_vehstock_rectification_date"
														class="form-control datepicker"	type="text"
														value="<%=mybean.rectification_date%>" size="11"
														maxlength="10" />
											</div>
											<div class="row"></div>
											<div class="form-element6">
												<label>Nature of Damage:</label>
													<textarea name="txt_vehstock_intransit_damage" cols="70"
														rows="5" class="form-control"
														id="txt_vehstock_intransit_damage"
														onKeyUp="charcount('txt_vehstock_intransit_damage', 'span_vehstock_intransit_damage','<font color=red>({CHAR} characters left)</font>', '8000')"><%=mybean.vehstock_intransit_damage%></textarea>
													<br> <span id="span_vehstock_intransit_damage">(8000
														Characters)</span>
											</div>

											<div class="form-element6">
												<label>Notes:</label>
													<textarea name="txt_vehstock_notes" cols="70" rows="5"
														class="form-control" id="txt_vehstock_notes"><%=mybean.vehstock_notes%></textarea>
											</div>
											<div class="row"></div>
											<%
												if (mybean.status.equals("Update")
														&& !(mybean.vehstock_entry_by == null)
														&& !(mybean.vehstock_entry_by.equals(""))) {
											%>
											<div class="form-element6">
												<label>Entry By: <%=mybean.unescapehtml(mybean.vehstock_entry_by)%></label>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_date == null)
														&& !(mybean.entry_date.equals(""))) {
											%>
											<div class="form-element6">
												<label> Entry Date: <%=mybean.entry_date%></label>
											</div>
											<%
												}
											%>

											<%
												if (mybean.status.equals("Update")
														&& !(mybean.vehstock_modified_by == null)
														&& !(mybean.vehstock_modified_by.equals(""))) {
											%>
											<div class="form-element6">
												<label>Modified By: <%=mybean.unescapehtml(mybean.vehstock_modified_by)%></label>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update")
														&& !(mybean.modified_date == null)
														&& !(mybean.modified_date.equals(""))) {
											%>
											<div class="form-element6">
												<label>Modified Date: <%=mybean.modified_date%></label>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Add")) {
											%>
											<center>
												<input name="addbutton" type="submit"
													class="button btn btn-success" id="addbutton"
													value="Add Stock"
													onclick="return SubmitFormOnce(document.form1,this);" />
											</center>
											<input type="hidden" name="add_button" value="yes" />

											<%
												} else if (mybean.status.equals("Update")) {
											%>
											<input type="hidden" name="update_button" value="yes" />
											<center>
												<input name="updatebutton" type="submit"
													class="button btn btn-success" id="updatebutton"
													value="Update Stock"
													onclick="return SubmitFormOnce(document.form1,this);" /> 
													<input name="delete_button" type="submit"
													class="button btn btn-success" id="delete_button"
													OnClick="return confirmdelete(this)" value="Delete Stock" />
											</center>

											<%
												}
											%>
											<input type="hidden" name="vehstock_entry_by"
												value="<%=mybean.vehstock_entry_by%>"/> 
												<input type="hidden" name="entry_date"
												value="<%=mybean.entry_date%>"/> 
												<input type="hidden" name="vehstock_modified_by"
												value="<%=mybean.vehstock_modified_by%>"> 
												<input type="hidden" name="modified_date"
												value="<%=mybean.modified_date%>"/>
										</div>
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>

	<%@include file="../Library/admin-footer.jsp"%>
<%@include file="../Library/js.jsp"%>

	<script src="../assets/js/footable.js" type="text/javascript"></script>
	<script>
	function FormFocus() { //v1.0
		//document.form1.txt_vehstock_name.focus();
// 		$("#displayCommission").hide();
// 		$("#brand_id").hide();
// 		getBrandID(); 
	}
	/* function PopulateBranchLocation(){	
	var vehstock_branch_id = document.getElementById('dr_vehstock_branch_id').value;
		if(vehstock_branch_id!="" && vehstock_branch_id!="0")
	   {
	       showHint('../inventory/inventory-check.jsp?vehstock_branch_id='+vehstock_branch_id,'branchexe');
	}  
	} */
	
	$("#txt_vehstock_fastag").bind('keyup',function(e){
		$("#txt_vehstock_fastag").val(($("#txt_vehstock_fastag").val()).toUpperCase());
	});
 
	function PopulateModel() {
		var vehstock_branch_id = document.getElementById('dr_vehstock_branch_id').value;
		if (vehstock_branch_id != "" && vehstock_branch_id != "0") {
			showHint('../inventory/inventory-check.jsp?stockbranchmodel=yes&vehstock_branch_id='
							+ vehstock_branch_id, 'branchmodel');
		}
	}
	
	function PopulateItem() {
		var vehstock_branch_id = document.getElementById('dr_vehstock_branch_id').value;
		var vehstock_model_id = document.getElementById('dr_vehstock_model_id').value;
		if (vehstock_branch_id != "" && vehstock_branch_id != "0") {
			showHint('../inventory/inventory-check.jsp?stockbranchitem=yes&vehstock_branch_id='
							+ vehstock_branch_id+'&vehstock_model_id='+vehstock_model_id, 'branchitem');
		}
	}
	
	function getBrandID() {
// 		alert("111");
		$("#brand_id").hide();
// 		console.log("brand_id=11=");
		var vehstock_branch_id = document.getElementById('dr_vehstock_branch_id').value;
		if (vehstock_branch_id != "" && vehstock_branch_id != "0") {
			showHint('../inventory/inventory-check.jsp?displayCommission=yes&vehstock_branch_id='
							+ vehstock_branch_id, 'brand_id');
			
			setTimeout('displayCommission()', 200);
// 			console.log("brand_id==");
		}
		
	}
	
	function displayCommission() {
		var brand_id = $("#brand_id").html();
// 		console.log("brand_id=="+brand_id);
		if(brand_id == 52 || brand_id == 55 || brand_id == 56){
			$("#displayCommission").show();
			$("#brand_id").hide();
			$("#commission").show();
			$("#chassis").hide();
			$("#engine").hide();
		}else if(brand_id == 2 || brand_id == 10){
			$("#brand_id").hide();
			$("#commission").hide();
			$("#chassis").show();
			$("#engine").show();
		}else{
			$("#brand_id").hide();
			$("#chassis").show();
			$("#engine").hide();
		}
		
		
	}

	function PopulatePaintWork() {
		var vehstock_branch_id = document.getElementById('dr_vehstock_branch_id').value;
		if (vehstock_branch_id != "" && vehstock_branch_id != "0") {
			showHint( '../inventory/inventory-check.jsp?stockbranchpaintwork=yes&vehstock_branch_id='
							+ vehstock_branch_id, 'paintwork');
		}
	}
	
</script>
	<script type="text/javascript">

	$(function() {
	 $('table').footable({
		 toggleHTMLElement : '<span> <div class="footable-toggle footable-expand" border="0"></div>' + '<div class="footable-toggle footable-contract" border="0"></div></span>'
	 });
		
	});
</script>
</body>
</HTML>
