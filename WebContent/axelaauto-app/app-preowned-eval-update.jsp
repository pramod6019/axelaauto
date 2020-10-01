<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.axelaauto_app.App_Preowned_Eval_Update" scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html>

<html lang="en">
<head>

<meta content="width=device-width, initial-scale=1" name="viewport">

<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="css/components-rounded.css" id="style_components"
	rel="stylesheet" type="text/css">

<script src="js/jquery.min.js" type="text/javascript"></script>
<script src="js/bootstrap.js" type="text/javascript"></script>

<script src="js/axelamobilecall.js" type="text/javascript"></script>
<script src="js/jquery-ui.js" type="text/javascript"></script>
<script type="text/javascript" src="js/Validate.js"></script>

<script>
	$(document).ready(function() {
		$("#addbutton").click(function() {
			checkForm();
		});
	});
	var msg = "";
	function checkForm() {
		msg = "";
// 		var financetype = document.getElementById("dr_quote_inscomp_id").value;
// 		if (financetype == '0') {
// 			msg += '<br>Select Insurance Company!';
// 		}
// 		var executive = document.getElementById("dr_executive").value;
// 		if (executive == '0') {
// 			msg += '<br>Select Consultant!';
// 		}
var offered_price = document.getElementById("txt_eval_offered_price").value;
		if (offered_price <= '0') {
			msg += '<br>Select Offered Price!';
		}
		var exe = document.getElementById("txt_eval_offered_price").value;
		if (exe <= '0') {
		msg += '<br>Select Pre-Owned Consultant!';
		}
	if (msg != '') {
		showToast(msg);
	} 
	else {
		document.getElementById('add_button').value = "yes";
		document.getElementById('frmaddquote').submit();
	}
	}
	</script>
	<script>
	
	

	function FormSubmit() {
		document.frmaddquote.submit();
	}
	
	function GetTotal(){
		var count=document.getElementById("txt_totalcount").value;
		var total=0;
		var amt=0;
		for(var i=1;i<=count;i++){
			//alert(i);
			amt=CheckNumeric(document.getElementById("txt_evalamt"+i).value);
			total=parseInt(total)+parseInt(amt);
		}
		document.getElementById("div_totalamt").innerHTML=	parseInt(total);
		document.getElementById("txt_eval_rf_total").value=	parseInt(total);
	}
	
	function CheckNumeric(num){
	    if(isNaN(num) || num=='' || num==null)
	    {
	        num=0;
	    }
	    return num;
	} 
	
</script>
<style>
/* table td { */
/* 	color: #8E44AD; */
	
/* } */


/* #status{ */
/* width: 10px; */
/* } */

td {
width: auto;
}

b {
	color: #8E44AD;
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
label{
color: #8E44AD;
}
</style>
</head>

<body <%if (!mybean.msg.equals("")) {%>
	onload="showToast('<%=mybean.msg%>')" <%}%>>
	<div class="header-wrap">
		<div class="panel-heading">
			<span class="panel-title"> <strong><center>Add Evaluation</center></strong></span>
		</div>
	</div>
	<div class="container" style="margin-top: 25px;">
		<form role="form" id="frmaddquote" name="frmaddquote"
			class="form-horizontal" method="post">
			<div class="form-body">

				<div class="alert alert-danger display-hide">
					<button class="close" data-close="alert"></button>
					You have some form errors. Please check below.
				</div>
				<div class="alert alert-success display-hide">
					<button class="close" data-close="alert"></button>
					Your form validation is successful!
				</div>

				<div class="form-group">
					<div class="form-group form-md-line-input">
						<label class="col-md-4 col-xs-4 control-label"
							for="form_control_1"><%=mybean.ReturnPreOwnedName(request)%>: </label>
						<div class="col-md-8 col-xs-8">
						<input type="text" class="form-control" id="txt_preowned_title" name="txt_preowned_title" value="<%=mybean.preowned_id%>" />
							<input type="hidden" id="txt_preowned_title" name="txt_preowned_title" value="<%=mybean.preowned_title%>" />
							
						</div>
					</div>

					<div class="form-group form-md-line-input">
						<label class="col-md-4 col-xs-4 control-label"
							for="form_control_1">Date: </label>
						<div class="col-md-8 col-xs-8">
							<%-- 							<label for="date" class="form-control"><%=mybean.quotedate%></label> --%>
							<% if (mybean.return_perm.equals("1")) { %>
												<input name="txt_eval_date" type="text" class="form-control date-picker"
														data-date-format="dd/mm/yyyy"
													id="txt_eval_date" value="<%=mybean.evaldate%>" size="11"
													maxlength="10" />
												<%
													} else {
												%>
												<%=mybean.evaldate%><input name="txt_eval_date"
													type="hidden" class="form-control date-picker"
														data-date-format="dd/mm/yyyy" id="txt_eval_date"
													value="<%=mybean.evaldate%>" />
												<%}%>
						</div>
					</div>

					<div class="form-group form-md-line-input">
						<label class="col-md-4 col-xs-4 control-label"
							for="form_control_1">Model:</label>
						<div class="col-md-8 col-xs-8">
							<input type="text" class="form-control" id="txt_preownedmodel_name" name="txt_preownedmodel_name" value="<%=mybean.preownedmodel_name%>" />
                         <input type="hidden" id="txt_preownedmodel_id" name="txt_preownedmodel_id" value="<%=mybean.preownedmodel_id%>" />
                         <input type="hidden" id="txt_preownedmodel_name" name="txt_preownedmodel_name" value="<%=mybean.preownedmodel_name%>" />
						</div>
					</div>
					
					<div class="form-group form-md-line-input">
						<label class="col-md-4 col-xs-4 control-label"
							for="form_control_1">Variant: </label>
						<div class="col-md-8 col-xs-8">
							<%-- <input type="text" class="form-control" value="<%=mybean.link_contact_name %>"> --%>
							<input type="text" class="form-control" id="txt_variant_name" name="txt_variant_name" value="<%=mybean.variant_name%>" />
							<input type="hidden" id="txt_variant_name" name="txt_variant_name" value="<%=mybean.variant_name%>" />
                         <input type="hidden" id="txt_variant_id" name="txt_variant_id" value="<%=mybean.variant_id%>" />
						</div>
					</div><br>
					
					<div class="  panel-heading"
						style="background-color: #8E44AD; border: 1px solid transparent; border-radius: 0px; margin-left: 0px">
						<span class="panel-title">
							<center>
								<h4>
									<strong>Accessories / Standard Feature Available</strong>
								</h4>
							</center>
						</span>
					</div>
					<br>


<!-- 					<div class="col-md-12"> -->

<!-- 						<div class="form-group form-md-line-input"> -->
<!-- 							<label for="form_control_1">Power Steering:</label>  -->
<%-- 							<input id="chk_eval_acc_powersteering" type="checkbox" name="chk_eval_acc_powersteering" <%=mybean.PopulateCheck(mybean.eval_acc_powersteering)%> /> --%>
<%-- <%-- 								<%=mybean.PopulateFinOption(mybean.quote_fin_option1)%> --%> 
<!-- 							</select> -->
<!-- 						</div> -->
						
						
											
											
											
											
											
											<div class="col-md-12">
											<div class="form-group form-md-line-input">
							<label for="form_control_1">Power Steering:</label>
							<input id="chk_eval_acc_powersteering"  style="top:10px" type="checkbox" name="chk_eval_acc_powersteering" <%=mybean.PopulateCheck(mybean.eval_acc_powersteering)%> />
						</div>
						</div>
						
						<div class="col-md-12">
											<div class="form-group form-md-line-input">
							<label for="form_control_1">Make/ Condition:</label>
								<textarea name="txt_eval_acc_powersteering_make"  cols="40" rows="2" class="form-control" id="txt_eval_acc_powersteering_make" ><%=mybean.eval_acc_powersteering_make%></textarea>
						</div>
						</div>
						
						<div class="col-md-12">
											<div class="form-group form-md-line-input">
							<label for="form_control_1">Power Windows:</label>
							<input id="chk_eval_acc_powerwindows" type="checkbox" name="chk_eval_acc_powerwindows" <%=mybean.PopulateCheck(mybean.eval_acc_powerwindows)%> />
						</div>
						</div>
						
						<div class="col-md-12">
											<div class="form-group form-md-line-input">
							<label for="form_control_1">Make/ Condition:</label>
								<textarea name="txt_eval_acc_powerwindows_make"  cols="40" rows="2" class="form-control" id="txt_eval_acc_powerwindows_make" ><%=mybean.eval_acc_powerwindows_make%></textarea>
						</div>
						</div>
						
						<div class="col-md-12">
											<div class="form-group form-md-line-input">
							<label for="form_control_1">Central Locking (Auto/Manual):</label>
							<input id="chk_eval_acc_centrallocking" type="checkbox" name="chk_eval_acc_centrallocking" <%=mybean.PopulateCheck(mybean.eval_acc_centrallocking)%> />
						</div>
						</div>
						
						<div class="col-md-12">
											<div class="form-group form-md-line-input">
							<label for="form_control_1">Make/ Condition:</label>
									<textarea name="txt_eval_acc_centrallocking_make"  cols="40" rows="2" class="form-control" id="txt_eval_acc_centrallocking_make" ><%=mybean.eval_acc_centrallocking_make%></textarea>
						</div>
						</div>
						
						<div class="col-md-12">
											<div class="form-group form-md-line-input">
							<label for="form_control_1">Alloy Wheels:</label>
							<input id="chk_eval_acc_alloywheels" type="checkbox" name="chk_eval_acc_alloywheels" <%=mybean.PopulateCheck(mybean.eval_acc_alloywheels)%> />
						</div>
						</div>
						
						<div class="col-md-12">
											<div class="form-group form-md-line-input">
							<label for="form_control_1">Make/ Condition:</label>
									<textarea name="txt_eval_acc_alloywheels_make"  cols="40" rows="2" class="form-control" id="txt_eval_acc_alloywheels_make" ><%=mybean.eval_acc_alloywheels_make%></textarea>
						</div>
						</div>
						
						<div class="col-md-12">
											<div class="form-group form-md-line-input">
							<label for="form_control_1">No. of Keys (Original):</label>
							<input id="chk_eval_acc_keys" type="checkbox" name="chk_eval_acc_keys" <%=mybean.PopulateCheck(mybean.eval_acc_keys)%> />
						</div>
						</div>
						
						<div class="col-md-12">
											<div class="form-group form-md-line-input">
							<label for="form_control_1">Make/ Condition:</label>
									<textarea name="txt_eval_acc_keys_make"  cols="40" rows="2" class="form-control" id="txt_eval_acc_keys_make" ><%=mybean.eval_acc_keys_make%></textarea>
						</div>
						</div>
						
						<div class="col-md-12">
											<div class="form-group form-md-line-input">
							<label for="form_control_1">Tool Kit/ Jack:</label>
							<input id="chk_eval_acc_toolkit" type="checkbox" name="chk_eval_acc_toolkit" <%=mybean.PopulateCheck(mybean.eval_acc_toolkit)%> />
						</div>
						</div>
						
						<div class="col-md-12">
											<div class="form-group form-md-line-input">
							<label for="form_control_1">Make/ Condition:</label>
									<textarea name="txt_eval_acc_toolkit_make"  cols="40" rows="2" class="form-control" id="txt_eval_acc_toolkit_make" ><%=mybean.eval_acc_toolkit_make%></textarea>
						</div>
						</div>
						
						<div class="col-md-12">
											<div class="form-group form-md-line-input">
							<label for="form_control_1">Reverse Parking Sensor:</label>
							<input id="chk_eval_acc_parkingsensor" type="checkbox" name="chk_eval_acc_parkingsensor" <%=mybean.PopulateCheck(mybean.eval_acc_parkingsensor)%> />
						</div>
						</div>
						
						<div class="col-md-12">
											<div class="form-group form-md-line-input">
							<label for="form_control_1">Make/ Condition:</label>
									<textarea name="txt_eval_acc_parkingsensor_make"  cols="40" rows="2" class="form-control" id="txt_eval_acc_parkingsensor_make" ><%=mybean.eval_acc_parkingsensor_make%></textarea>
						</div>
						</div>
						
						<div class="col-md-12">
											<div class="form-group form-md-line-input">
							<label for="form_control_1">Others (If any):</label>
							<input id="chk_eval_acc_others" type="checkbox" name="chk_eval_acc_others" <%=mybean.PopulateCheck(mybean.eval_acc_others)%> />
						</div>
						</div>
						
						<div class="col-md-12">
											<div class="form-group form-md-line-input">
							<label for="form_control_1">Make/ Condition:</label>
								<textarea name="txt_eval_acc_others_make"  cols="40" rows="2" class="form-control" id="txt_eval_acc_others_make" ><%=mybean.eval_acc_others_make%></textarea>
						</div>
						</div>
						<br><br>
											
<!-- 					<div class="container-fluid"> -->
<!-- 											<div class="col-md-12"> -->
												<%=mybean.StrHTML%>
<!-- 											</div> -->
<!-- 										</div> -->
										<br>						
																						
											

						
<!-- 					</div> -->
<div class="col-md-12">
											<div class="form-group form-md-line-input">
							<label for="form_control_1">Offered Price<font color="#ff0000">*</font>:</label>
								<input type="tel" name="txt_eval_offered_price" id="txt_eval_offered_price" value="<%=mybean.eval_offered_price%>" class="form-control" />
						</div>
						</div>
						
						<div class="col-md-12">
											<div class="form-group form-md-line-input">
							<label for="form_control_1">Pre-Owned Consultant<b><font color="#ff0000">*</font></b>:</label>
							<select name="dr_eval_executive" class="form-control" id="dr_eval_executive">
                                            <%=mybean.PopulateEvalExecutive()%>
                                            </select>
						</div>
						</div>
						<br><br>
						<div class="form-actions noborder">
							<center>
								<button type="button" class="btn1" name="addbutton"
									id="addbutton">Add Evaluation</button>
									<input type="hidden" name="add_button" id="add_button" value="yes"/>
							</center>
						</div>
<div class="container-fluid"><center>
										   <%if(mybean.status.equals("Add")){%>
<!-- 										   		<input name="addbutton" type="submit" class="btn btn-success" id="addbutton" value="Add Evaluation" onclick="return SubmitFormOnce(document.form1,this);"/> -->
<!--                         <input type="hidden" name="add_button" id="add_button" value="yes"/> -->
										   <%} else if (mybean.status.equals("Update")){%>
										   		 <input type="hidden" name="update_button" value="yes"/>
                        <input name="updatebutton" type="submit" class="btn btn-success" id="updatebutton" value="Update Evaluation" onclick="return SubmitFormOnce(document.form1,this);"/>
                        <input name="delete_button" type="submit" class="btn btn-success" id="delete_button" OnClick="return confirmdelete(this)" value="Delete Evaluation"/>
										   <%} %></center>
										   <input type="hidden" name="eval_entry_by" value="<%=mybean.eval_entry_by%>"/>
                        <input type="hidden" name="eval_entry_date" value="<%=mybean.eval_entry_date%>"/>
                        <input type="hidden" name="eval_modified_by" value="<%=mybean.eval_modified_by%>"/>
                        <input type="hidden" name="eval_modified_date" value="<%=mybean.eval_modified_date%>"/>
                         <input type="hidden" name="add" value="yes"/>
										   </div>


				</div>
			</div>
		</form>
	</div>

	<table>
		<tr>
			<td>
			</td>
			<td align="right" valign="top"></td>
		</tr>
	</table>




</body>


</html>