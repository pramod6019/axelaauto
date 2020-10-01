<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.inventory.Stock_Dash"
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
						<h1>Stock Dashboard &gt; Stock ID<b>: </b>
							<%=mybean.vehstock_id%></h1>
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
							<li><a href="../inventory/stock-list.jsp?all=recent">List Stock</a> &gt;</li>
							<li><a href="../inventory/stock-dash.jsp?vehstock_id=<%=mybean.vehstock_id%>">Stock Dash</a><b>:</b></li>
						</ul>
						<!-- END PAGE BREADCRUMBS -->
						
							<!-- 					BODY START -->
							<div>
								<ul class="nav nav-tabs">
									<li class="active"><a href="#tabs-1" data-toggle="tab">Stock Details</a></li>
									<li onclick="LoadHistory('2');"><a href="#tabs-2" data-toggle="tab">History</a></li>
								</ul>
							<div class="tab-content">
								<div id="dialog-modal"></div>
								<div class="tab-pane in active" id="tabs-1">
									<center>
										<font color="#ff0000"><b><%=mybean.msg%></b></font>
									</center>
									<div class="portlet box">
										<div class="portlet-title" style="text-align: center">
											<div class="caption" style="float: none">Stock Details</div>
										</div>
										<div class="portlet-body portlet-empty container-fluid">
											<div class="tab-pane" id="">
												<form name="form1" id="form1" class="form-horizontal"
														method="post">	
												<!-- START PORTLET BODY -->
													<input type="hidden" name="vehstock_id" id="vehstock_id" value="<%=mybean.vehstock_id%>" />
											<div class="row">
												<div class="form-element6">
													<label>Branch<font color="#ff0000">*</font>: </label>
														<select name="dr_vehstock_branch_id" class="form-control"
															id="dr_vehstock_branch_id"
															onChange="PopulateItem();PopulatePaintWork();SecurityCheck('dr_vehstock_branch_id',this,'hint_dr_vehstock_branch_id');">
														<%=mybean.PopulateBranch(mybean.vehstock_branch_id, "", "1", mybean.branch_brand_id, request)%>
														</select>
														<div class="hint" id="hint_dr_vehstock_branch_id"></div>
												</div>
	
												<div class="form-element6">
													<label> Location<font color="#ff0000">*</font>: </label>
														<div id="branchexe" onchange="SecurityCheck('dr_vehstock_vehstocklocation_id',this,'hint_dr_vehstock_vehstocklocation_id');">
															<%=mybean.PopulateLocation()%>
														</div>
														<div class="hint" id="hint_dr_vehstock_vehstocklocation_id"></div>
												</div>
											</div>
											
											<div class="row">
												<div class="form-element6">
													<label>Item<font color="#ff0000">*</font>: </label>
														<div id="branchitem" onchange="SecurityCheck('dr_vehstock_item_id',this,'hint_dr_vehstock_item_id')"> 
															<%=mybean.PopulateItem(mybean.comp_id, mybean.vehstock_branch_id)%>
														</div>
														<div class="hint" id="hint_dr_vehstock_item_id"></div>
												</div>
												
												<div class="form-element6">
													<label>Model Year<font color="#ff0000">*</font>: </label>
														<input name="txt_vehstock_modelyear" id="txt_vehstock_modelyear"
															class="form-control yearpicker" 
															type="text" value="<%=mybean.vehstock_modelyear%>" size="10"
															maxlength="4" onchange="SecurityCheck('txt_vehstock_modelyear',this,'hint_txt_vehstock_modelyear')"/>
													<div class="hint" id="hint_txt_vehstock_modelyear"></div>
												</div>
											</div>
											
											<% if (mybean.branch_brand_id.equals("52") || mybean.branch_brand_id.equals("55") || mybean.branch_brand_id.equals("56")) { %>
											<div class="row">
												<div class="form-element4">
													<label>Manufactured Year:</label>
														<input name="txt_vehstock_mfgyear" id="txt_vehstock_mfgyear"
															type="text" class="form-control yearpicker"
															value="<%=mybean.vehstock_mfgyear%>"
															onKeyUp="toInteger('txt_vehstock_mfgyear','Manufactured Year')"
															size="10" maxlength="4" onchange="SecurityCheck('txt_vehstock_mfgyear',this,'hint_txt_vehstock_mfgyear')" />
													<div class="hint" id="hint_txt_vehstock_mfgyear"></div>
												</div>
	
												<div class="form-element4">
													<label>Reference Number:</label>
														<input name="txt_vehstock_ref_no" id="txt_vehstock_ref_no"
															type="text" class="form-control"
															value="<%=mybean.vehstock_ref_no%>" size="20" maxlength="15" 
															onchange="SecurityCheck('txt_vehstock_ref_no',this,'hint_txt_vehstock_ref_no')"/>
													<div class="hint" id="hint_txt_vehstock_ref_no"></div>
												</div>
	
												<div class="form-element4">
													<label>Commission Number<font color="#ff0000">*</font>: </label>
														<input name="txt_vehstock_comm_no" id="txt_vehstock_comm_no"
															type="text" class="form-control"
															value="<%=mybean.vehstock_comm_no%>" size="20" maxlength="15" 
															onchange="SecurityCheck('txt_vehstock_comm_no',this,'hint_txt_vehstock_comm_no')"/>
													<div class="hint" id="hint_txt_vehstock_comm_no"></div>
												</div>
											</div>	
											<% } %>
											<div class="row">
												<div class="form-element6">
													<label>Chassis Prefix:</label>
														<input name="txt_vehstock_chassis_prefix"
															id="txt_vehstock_chassis_prefix" type="text"
															class="form-control"
															value="<%=mybean.vehstock_chassis_prefix%>" size="20"
															maxlength="15" onchange="SecurityCheck('txt_vehstock_chassis_prefix',this,'hint_txt_vehstock_chassis_prefix')"/>
													<div class="hint" id="hint_txt_vehstock_chassis_prefix"></div>		
												</div>
	
												<div class="form-element3">
												<% if (mybean.branch_brand_id.equals("52") || mybean.branch_brand_id.equals("55") || mybean.branch_brand_id.equals("56")) { %>
													<label>Chassis Number:</label>
												<% }else{ %>
													<label>Chassis Number<font color="#ff0000">*</font>:</label>
													<% } %>
														<input name="txt_vehstock_chassis_no"
															id="txt_vehstock_chassis_no" type="text" class="form-control"
															value="<%=mybean.vehstock_chassis_no%>" size="22"
															maxlength="25" onchange="SecurityCheck('txt_vehstock_chassis_no',this,'hint_txt_vehstock_chassis_no')"/>
													<div class="hint" id="hint_txt_vehstock_chassis_no"></div>
												</div>
												
												<div class="form-element3">
													<label>FASTag ID: </label>
													<input name="txt_vehstock_fastag" id="txt_vehstock_fastag" type="text"
														onchange="SecurityCheck('txt_vehstock_fastag',this,'hint_txt_vehstock_fastag')"
														class="form-control" value="<%=mybean.vehstock_fastag %>" size="25" maxlength="20" />
													<div class="hint" id="hint_txt_vehstock_fastag"></div>
												</div> 
											</div>	
											<div class="row">
												<div class="form-element6">
													<label> Paintwork: </label>
														<div id="paintwork" >
															<%=mybean.PopulatePaintWork(mybean.comp_id, mybean.vehstock_branch_id)%>
														</div>
													<div class="hint" id="hint_dr_vehstock_paintwork_id"></div>
												</div>
	
												<div class="form-element3">
												<% if (mybean.branch_brand_id.equals("2") || mybean.branch_brand_id.equals("10")){%>
													<label>Engine Number<font color="#ff0000">*</font>: </label>
												<% }else{ %>
													<label>Engine Number: </label>
												<% } %>
														<input name="txt_vehstock_engine_no" id="txt_vehstock_engine_no"
															type="text" class="form-control"
															value="<%=mybean.vehstock_engine_no%>" size="22"
															maxlength="25" onchange="SecurityCheck('txt_vehstock_engine_no',this,'hint_txt_vehstock_engine_no')"/>
													<div class="hint" id="hint_txt_vehstock_engine_no"></div>
												</div>
												<div class="form-element3">
													<label>Parking Number:</label>
														<input name="txt_vehstock_parking_no"
															id="txt_vehstock_parking_no" type="text" class="form-control"
															value="<%=mybean.vehstock_parking_no%>"
															onKeyUp="toInteger('txt_vehstock_parking_no','Parking Number')"
															size="10" maxlength="10" onchange="SecurityCheck('txt_vehstock_parking_no',this,'hint_txt_vehstock_parking_no')"/>
													<div class="hint" id="hint_txt_vehstock_parking_no"></div>
												</div>
											</div>	
											<div class="row">
												<% if (mybean.branch_brand_id.equals("52") || mybean.branch_brand_id.equals("55")) { %>
												<div class="form-element6">
													<label>Confirmed SGW:</label>
														<input name="txt_vehstock_confirmed_sgw"
															id="txt_vehstock_confirmed_sgw" type="text"
															class="form-control datepicker"
															value="<%=mybean.confirmed_sgw%>" size="11" maxlength="10" 
															onchange="SecurityCheck('txt_vehstock_confirmed_sgw',this,'hint_txt_vehstock_confirmed_sgw')"/>
													<div class="hint" id="hint_txt_vehstock_confirmed_sgw"></div>
												</div>
												<% } %>
												<div class="form-element6">
													<label>Ordered Date:</label>
														<input name="txt_vehstock_ordered_date"
															id="txt_vehstock_ordered_date"
															class="form-control datepicker"
															type="text"
															value="<%=mybean.ordered_date%>" size="11" maxlength="10" 
															onchange="SecurityCheck('txt_vehstock_ordered_date',this,'hint_txt_vehstock_ordered_date')"/>
													<div class="hint" id="hint_txt_vehstock_ordered_date"></div>
												</div>
												
												<div class="form-element6">
													<label>Invoice Number: </label>
														<input name="txt_vehstock_invoice_no"
															id="txt_vehstock_invoice_no" type="text" class="form-control"
															value="<%=mybean.vehstock_invoice_no%>" size="20"
															maxlength="30" onchange="SecurityCheck('txt_vehstock_invoice_no',this,'hint_txt_vehstock_invoice_no')"/>
													<div class="hint" id="hint_txt_vehstock_invoice_no"></div>		
												</div>
											</div>
											<div class="row">
												<div class="form-element6">
													<label>Invoice Date:</label>
														<input name="txt_vehstock_invoice_date"
															id="txt_vehstock_invoice_date"
															class="form-control datepicker"
															type="text" onchange="SecurityCheck('txt_vehstock_invoice_date',this,'hint_txt_vehstock_invoice_date')"
															value="<%=mybean.invoice_date%>" size="11" maxlength="10" />
													<div class="hint" id="hint_txt_vehstock_invoice_date"></div>											
												</div>
	
												<div class="form-element2">
													<label> Invoice	Amount:</label>
														<input name="txt_vehstock_invoice_amount" type="text"
															class="form-control" id="txt_vehstock_invoice_amount"
																onKeyUp="toNumber('txt_vehstock_invoice_amount','Car Cost')"
															value="<%=mybean.vehstock_invoice_amount%>" size="11"
															maxlength="10" onchange="SecurityCheck('txt_vehstock_invoice_amount',this,'hint_txt_vehstock_invoice_amount')" />
													<div class="hint" id="hint_txt_vehstock_invoice_amount"></div>
												</div>
												
												<div class="form-element2 form-element-margin">
													<label> Principal Support:</label>
													<label> <%=mybean.vehstock_principalsupport %></label>
												</div>
												<div class="form-element2 form-element-margin">
													<label> Invoice Amount After tax:</label>
													<label> <%=mybean.vehstock_invoiceamountaftertax %></label>
												</div>
												
											</div>
											<div class="row">
												<div class="form-element6">
													<label>Arrival Date:</label>
														<input name="txt_vehstock_arrival_date"
															id="txt_vehstock_arrival_date"
															class="form-control datepicker"
															type="text" onchange="SecurityCheck('txt_vehstock_arrival_date',this,'hint_txt_vehstock_arrival_date')"
															value="<%=mybean.arrival_date%>" size="11" maxlength="10" />
													<div class="hint" id="hint_txt_vehstock_arrival_date"></div>		
												</div>
	
												<div class="form-element6">
													<label>PDI Date:</label>
														<input name="txt_vehstock_pdi_date" id="txt_vehstock_pdi_date"
															class="form-control datepicker"
															type="text" onchange="SecurityCheck('txt_vehstock_pdi_date',this,'hint_txt_vehstock_pdi_date')"
															value="<%=mybean.vehstock_pdi_date%>" size="11"
															maxlength="10" />
													<div class="hint" id="hint_txt_vehstock_pdi_date"></div>		
												</div>
											</div>
											<div class="row">
												<div class="form-element6">
													<label>DMS Date:</label>
														<input name="txt_vehstock_dms_date" id="txt_vehstock_dms_date"
															class="form-control datepicker"
															type="text" onchange="SecurityCheck('txt_vehstock_dms_date',this,'hint_txt_vehstock_dms_date')"
															value="<%=mybean.vehstock_dms_date%>" size="11"
															maxlength="10" />
													<div class="hint" id="hint_txt_vehstock_dms_date"></div>		
												</div>
	
												<div class="form-element6">
													<label>NSC:</label>
														<input name="txt_vehstock_nsc" id="txt_vehstock_nsc" type="text"
															class="form-control" value="<%=mybean.vehstock_nsc%>"
															size="20" maxlength="25" 
															onchange="SecurityCheck('txt_vehstock_nsc',this,'hint_txt_vehstock_nsc')"/>
													<div class="hint" id="hint_txt_vehstock_nsc"></div>		
												</div>
											</div>
											<div class="row">
												<div class="form-element6">
													<label>Delivery	Status<font color="#ff0000">*</font>:</label>
														<select name="dr_vehstock_delstatus_id" class="form-control"
															id="dr_vehstock_delstatus_id" onchange="SecurityCheck('dr_vehstock_delstatus_id',this,'hint_dr_vehstock_delstatus_id')">
															<%=mybean.PopulateDeliveryStatus()%>
															
														</select>
													<div class="hint" id="hint_dr_vehstock_delstatus_id"></div>	
												</div>
	
												<div class="form-element4">
													<label> Status<font	color="#ff0000">*</font>:</label>
														<select name="dr_vehstock_status_id" class="form-control"
															id="dr_vehstock_status_id" onchange="SecurityCheck('dr_vehstock_status_id',this,'hint_dr_vehstock_status_id')">
															<%=mybean.PopulateStatus()%>
														</select>
													<div class="hint" id="hint_dr_vehstock_status_id"></div>	
												</div>
	
												<div class="form-element2 form-element-margin">
													<label>Blocked: </label>
														<input id="chk_vehstock_blocked" type="checkbox"
															name="chk_vehstock_blocked" onchange="SecurityCheck('chk_vehstock_blocked',this,'hint_chk_vehstock_blocked')"
															<%=mybean.PopulateCheck(mybean.vehstock_blocked)%> />
													<div class="hint" id="hint_chk_vehstock_blocked"></div>
												</div>
												
												<div class="form-element6">
													<label>Incentive:</label>
													<input name="txt_vehstock_incentive" id="txt_vehstock_incentive" type="text"
														class="form-control" value="<%=mybean.vehstock_incentive%>" size="20" maxlength="25"
														onKeyUp="toNumber('txt_vehstock_incentive', 'Incentive')"
														onchange="SecurityCheck('txt_vehstock_incentive',this,'hint_txt_vehstock_incentive')"/>
													<div class="hint" id="hint_txt_vehstock_incentive"></div>		
												</div>
											</div>	
										</form>
									</div>
								</div>
						</div>
								<div class="portlet box  container-fluid">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">In Transit
											Damage</div>
									</div>
									<div class="portlet-body portlet-empty container-fluid">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<br>
											<div class="form-element6">
												<label>Rectification Date:</label>
													<input name="txt_vehstock_rectification_date"
														id="txt_vehstock_rectification_date"
														class="form-control datepicker"	type="text"
														value="<%=mybean.rectification_date%>" size="11"
														maxlength="10" onchange="SecurityCheck('txt_vehstock_rectification_date',this,'hint_txt_vehstock_rectification_date')"/>
												<div class="hint" id="hint_txt_vehstock_rectification_date"></div>
											</div>
											<div class="row"></div>
												<div class="form-element6">
													<label>Nature of Damage:</label>
														<textarea name="txt_vehstock_intransit_damage" cols="70"
															id="txt_vehstock_intransit_damage"
															rows="5" class="form-control" onchange="SecurityCheck('txt_vehstock_intransit_damage',this,'hint_txt_vehstock_intransit_damage')"
															onKeyUp="charcount('txt_vehstock_intransit_damage', 'span_vehstock_intransit_damage','<font color=red>({CHAR} characters left)</font>', '8000')"><%=mybean.vehstock_intransit_damage%></textarea>
														<span id="span_vehstock_intransit_damage">(8000
															Characters)</span>
													<div class="hint" id="hint_txt_vehstock_intransit_damage"></div>		
												</div>
	
												<div class="form-element6">
													<label>Notes:</label>
														<textarea name="txt_vehstock_notes" cols="70" rows="5" onchange="SecurityCheck('txt_vehstock_notes',this,'hint_txt_vehstock_notes')"
															class="form-control" id="txt_vehstock_notes"><%=mybean.vehstock_notes%></textarea>
													<div class="hint" id="hint_txt_vehstock_notes"></div>
												</div>
											<div class="row"></div>
												<div class="row">
													<div class="form-element3 form-element-margin">
														<label>Entry By:&nbsp;</label> <span> <%=mybean.vehstock_entry_by%>
															<input type="hidden" name="vehstock_entry_by"
															value="<%=mybean.vehstock_entry_by%>">
														</span>
													</div>

													<div class="form-element3 form-element-margin">
														<label>Entry Date:&nbsp;</label> <span> <%=mybean.vehstock_entry_date%>
															<input type="hidden" name="vehstock_entry_date"
															value="<%=mybean.vehstock_entry_date%>">
														</span>
													</div>

													<% if (mybean.vehstock_modified_by != null && !mybean.vehstock_modified_by.equals("")) { %>

													<div class="form-element3 form-element-margin">
														<label>Modified By:&nbsp;</label> <span> <%=mybean.vehstock_modified_by%>
															<input type="hidden" name="vehstock_modified_by"
															value="<%=mybean.vehstock_modified_by%>">
														</span>
													</div>

													<div class="form-element3 form-element-margin">
														<label>Modified Date:&nbsp;</label> <span> <%=mybean.vehstock_modified_date%>
															<input type="hidden" name="vehstock_modified_date"
															value="<%=mybean.vehstock_modified_date%>">
														</span>
													</div>
													<% } %>
											</div>
										</div>
									</div>
								</div>
							</div>							
							<div class="tab-pane" id="tabs-2"></div>
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
<script>
		function SecurityCheck(name, obj, hint) {
			var vehstock_id = GetReplace(document.form1.vehstock_id.value);
			var url = "../inventory/stock-dash-check.jsp?";
			var str = "123";
			if (name != "chk_vehstock_blocked") {
				var value = GetReplace(obj.value);
			} else {
				if (obj.checked == true) {
					var value = "1";
				} else {
					var value = "0";
				}
			}
			var param = "name=" + name + "&value=" + value + "&vehstock_id=" + vehstock_id ;
 			showHint(url + param, hint);
		}
		
		function LoadHistory(tab) {
			var vehstock_id = GetReplace(document.form1.vehstock_id.value);
			if (tab == '2') {
				if (document.getElementById("tabs-2").innerHTML == '') {
				showHint( 'stock-dash-check.jsp?history=yes&vehstock_id='
								+ vehstock_id, 'tabs-2');
				}
			} 
		}

		function PopulateItem() {
			var vehstock_branch_id = document.getElementById('dr_vehstock_branch_id').value;
			if (vehstock_branch_id != "" && vehstock_branch_id != "0") {
				showHint( '../inventory/inventory-check.jsp?stockbranchitem=yes&vehstock_branch_id='
								+ vehstock_branch_id, 'branchitem');
			}
		}
		
		function PopulatePaintWork() {
			var vehstock_branch_id = document.getElementById('dr_vehstock_branch_id').value;
			if (vehstock_branch_id != "" && vehstock_branch_id != "0") {
				showHint( '../inventory/inventory-check.jsp?stockbranchpaintwork=yes&vehstock_branch_id='
								+ vehstock_branch_id, 'paintwork');
			}
		}
		
		$("#txt_vehstock_fastag").bind('keyup',function(e){
			$("#txt_vehstock_fastag").val(($("#txt_vehstock_fastag").val()).toUpperCase());
		});
</script>	
</body>
</html>