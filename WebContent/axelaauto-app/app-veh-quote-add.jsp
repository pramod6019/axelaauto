<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.axelaauto_app.App_Veh_Quote_Add"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html>

<html lang="en">
<head>
<meta content="width=device-width, initial-scale=1" name="viewport">

<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="css/components-rounded.css" id="style_components"
	rel="stylesheet" type="text/css">
	


<!-- <script> -->
<!-- // 	$(document).ready(function() { -->
<!-- // 		$("#addbutton").click(function() { -->
<!-- // 			checkForm(); -->
<!-- // 		}); -->
<!-- // 	}); -->

<!-- // 	var msg = ""; -->

<!-- // 	$(function(){ -->
<%-- 		var quote_by = '<%=mybean.quoteby%>'; --%>
<!-- // 		if(quote_by == "Stock ID"){ -->
<!-- // 			$("#stockid").addClass('active'); -->
<!-- // 		}else if(quote_by == "Commission No."){ -->
<!-- // 			$("#commno").addClass('active'); -->
<!-- // 		} -->
<!-- // 	}); -->
<!-- // 	$("#stockid").click(function(){ -->
<!-- // 		alert("111111111111"); -->
<!-- // 		$('#txt_quote_by').val('Stock ID'); -->
<!-- // 	}); -->
<!-- // 	$("#commno").click(function(){ -->
<!-- // 		$('#txt_quote_by').val('Commission No.'); -->
<!-- // 	}); -->
<!-- </script> -->

<style>
b {
	color: #8E44AD;
}

span {
	color: red;
}

.container {
	padding-right: 15px;
	padding-left: 15px;
	margin-right: auto;
	margin-left: auto;
	margin-top: 45px;
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
<body <%if(!mybean.msg.equals("")) {%>
	onload="showToast('<%=mybean.msg%>')" <%} %>>
<!-- 	<div class="header-wrap"> -->
<!-- 		<div class="panel-heading"> -->
<!-- 			<span class="panel-title"> <strong><center>Add -->
<!-- 						Quote</center></strong></span> -->
<!-- 		</div> -->
<!-- 	</div> -->
	<div class="container-fluid">

		
			<div class="form-body">

				<div class="alert alert-danger display-hide">
					<button class="close" data-close="alert"></button>
					You have some form errors. Please check below.
				</div>
				<div class="alert alert-success display-hide">
					<button class="close" data-close="alert"></button>
					Your form validation is successful!
				</div>
				
<%-- 					<%if(mybean.enquiry_enquirytype_id.equals("1")){%> --%>
							
<%-- 							<%if(!mybean.quote_enquiry_id.equals("0") --%>
<%-- 									&& !mybean.next_button.equals("ADD By Enquiry") ){%> --%>
									<% if (mybean.enquiry_enquirytype_id.equals("1")) { %>
									<%if(!mybean.quote_enquiry_id.equals("0")
									&& !mybean.next_button.equals("ADD By Enquiry") ){%>
				
				<form role="form" class="form-horizontal" id="form1"
			name="form1" method="post">
			
			<div class="row panel-heading"
									style="background-color: #8E44AD; border: 1px solid transparent; border-radius: 0px; padding:0px;">
									<span class="panel-title">
										<center>
											<h4>
												<strong>Add Quote By Stock ID</strong>
											</h4>
										</center>
									</span>
								</div>
			
			<center>
								<div class="btn-group" data-toggle="buttons">
										<label class="btn btn-default active" id="stockid" >
										<input type="radio" class="toggle"  id="txt_quote_by_stock" value="Stock ID">Stock ID</label>
										
										<label class="btn btn-default" id="commno">
										<input type="radio" class="toggle" id="txt_quote_by_comm" value="Commission No.">Commission No.</label>
									</div>
									<input type='text' id='txt_quote_by' name='txt_quote_by' value='<%=mybean.quoteby%>' hidden/>
				</center>
				
				
				
				<br>	
					<center><input name="txt_vehstock_details" type="number" class="form-control" id="txt_vehstock_details"
														value="<%=mybean.txt_vehstock_details%>" onkeyup="toInteger(this.id);" />
														</center>
				<br>
				<center><input name="next_button" type="submit" class="btn1" id="next_button" value="Next" />
													<input name="button_addby" type="hidden" id="button_addby" value="ADD By ID" /></center><br>
				
				</form>
				
				
				<div class="row panel-heading"
									style="background-color: #8E44AD; border: 1px solid transparent; border-radius: 0px; padding:0px;">
									<span class="panel-title">
										<center>
											<h4>
												<strong>Add Quote By Variant</strong>
											</h4>
										</center>
									</span>
								</div>
				
				<form role="form" class="form-horizontal" id="form1"
			name="form1" method="post">
				<div class="form-group form-md-line-input">
					<label for="form_control_1">Model<span>*</span>:
					</label> <select class="form-control" name="dr_item_model_id"
						id="dr_item_model_id" onChange="populateitem(this.value);">
						<%=mybean.PopulateModel()%>
					</select>
				</div>
				<div class="form-group form-md-line-input">
					<label for="form_control_1">Item<span>*</span>:
					</label> <span id="itemHint"> <%=mybean.PopulateItem(mybean.item_model_id, mybean.comp_id)%></span>
				</div>
				
<!-- 				<div class="form-group form-md-line-input"> -->
<!-- 					<label for="form_control_1">By: -->
<%-- 					</label> <span id="itemHint"> <%=mybean.PopulateQuoteBy(mybean.quoteby)%></span> --%>
<!-- 				</div> -->
				
<!-- 				<div class="form-group form-md-line-input"> -->
<!-- 					<label for="form_control_1">ID.: -->
<!-- 					</label> <input name="txt_vehstock_id" type="text" class="form-control" id="txt_vehstock_id" -->
<%-- 														value="<%=mybean.vehstock_id%>" onkeyup="toInteger(this.id);" /> --%>
<!-- 				</div> -->

<div class="form-actions noborder">
					<center>
					
					
					<br><input name="next_button" type="submit" class="btn1" id="next_button" value="Next" />
													<input name="button_addby" type="hidden" id="button_addby" value="ADD By Variant" />
					
<!-- 						<button type="submit" class="btn1" name="addbutton" id="addbutton">Next</button> -->
<!-- 						<input type="hidden" name="add_button" id="add_button" value="yes" /><br> -->
						<br>
					</center>
				</div>
				</form>
				
				<%} %>
				
					<%if(!mybean.quote_stock_id.equals("0") 
								&& !mybean.next_button.equals("ADD By ID")
								&& !mybean.next_button.equals("ADD By Variant")){%>
								
								<div class="row panel-heading"
									style="background-color: #8E44AD; border: 1px solid transparent; border-radius: 0px; padding:0px;">
									<span class="panel-title">
										<center>
											<h4>
												<strong>Add Quote By Enquiry</strong>
											</h4>
										</center>
									</span>
								</div>
				
				<div class="col-md-12" style="margin-top: 40px; margin-left: 9px;">
					<form role="form" class="form-horizontal" name="form3" id="form3"
						method="post">
						<div class="form-body">
							<div class="form-group form-md-line-input">
								<label for="form_control_1">Model:<span
									style="color: #000000; margin-left: 102px;"><%=mybean.model_name%></span>
								</label>
							</div>
							<!-- 				</div> -->
							<div class="form-group form-md-line-input">
								<label for="form_control_1">Variant:<span
									style="color: #000000; margin-left: 96px;"><%=mybean.item_name%></span>
								</label>
							</div>
							
							
							<div class="form-group form-md-line-input">
								<label for="form_control_1">Enquiry ID<font color="#ff0000">*</font>:
								<input name="txt_quote_enquiry_id" type="number"
														class="form-control" style="top: 8px"
														id="txt_quote_enquiry_id"
														value="<%=mybean.quote_enquiry_id%>"
														onkeyup="toInteger(this.id);" />
								</label>
							</div>
							<center>
					
					
					<br><input name="next_button" type="submit" class="btn1" id="next_button" value="Next" />
													<input name="button_addby" type="hidden" id="button_addby" value="ADD By Enquiry" />
						</div>
						<!-- 				</div> -->
					</form>
				</div>
				
				
				
				
				<% } %>
				<% } %>
				
				
			

				<!-- 				Start Pre-Owned -->
				<% if (mybean.enquiry_enquirytype_id.equals("2")) { %>
				<div class="row panel-heading"
									style="background-color: #8E44AD; border: 1px solid transparent; border-radius: 0px; padding:0px;">
									<span class="panel-title">
										<center>
											<h4>
												<strong>Add Quote By Pre-Owned Stock</strong>
											</h4>
										</center>
									</span>
								</div>
				<div class="col-md-12" style="margin-top: 40px; margin-left: 9px;">
					<form role="form" class="form-horizontal" name="form4" id="form4"
						method="post">
						<div class="form-body">
							<div class="form-group form-md-line-input">
								<label for="form_control_1">Model:<span
									style="color: #000000; margin-left: 102px;"><%=mybean.model_name%></span>
								</label>
							</div>
							<!-- 				</div> -->
							<div class="form-group form-md-line-input">
								<label for="form_control_1">Variant:<span 
									style="color: #000000; margin-left: 96px;"><%=mybean.item_name%></span>
								</label>
							</div>
							<div class="form-group form-md-line-input">
								<label for="form_control_1">Pre-Owned Stock ID.<span>*</span>:<input
									name="txt_preowned_stock_id" type="text" class="textbox"
									style="color: #000000; margin-left: 10px;"
									id="txt_preowned_stock_id" value="<%=mybean.preownedstock_id%>"
									size="10" maxlength="10" onkeyup="toInteger(this.id);" />
								</label>
							</div>
							<br>
							<center>
													<input name="next_button" type="submit" class="btn1" id="next_button" value="Next" />
													<input name="button_addby" type="hidden" id="button_addby" value="ADD By PreownedStock" />
												</center>
							<div>
								<input type="hidden" name="dr_item_model_id" id="dr_item_model_id" value="<%=mybean.item_model_id%>">
								<input type="hidden" name="dr_item_id" id="dr_item_id" value="<%=mybean.item_id%>">
								<input type="hidden" name="txt_item_name" id="txt_item_name" value="<%=mybean.item_name%>">
							    <input type="hidden" name="txt_model_name" id="txt_model_name" value="<%=mybean.model_name%>">
							</div>
						</div>
						<!-- 				</div> -->
					</form>
				</div>
				<% } %>
				<!-- End Pre-Owned -->
				<br>
				

			</div>
<!-- 		</form> -->
	</div>

	<script src="js/jquery.min.js" type="text/javascript"></script>
	<script src="js/bootstrap.min.js" type="text/javascript"></script>
	<script src="../library/dynacheck.js" type="text/javascript"></script>
	<script type="text/javascript" src="js/jquery-ui.js"></script>
	<script src="js/axelamobilecall.js" type="text/javascript"></script>
	<script type="text/javascript" src="js/Validate.js"></script>
	
		<script language="JavaScript" type="text/javascript">
		function populateitem(model_id) {
			showHint( '../axelaauto-app/app-enquiry-check.jsp?vehquote=yes&quote_model_id='
							+ model_id, 'itemHint');
		}
	 
	$(function(){
		var quote_by = '<%=mybean.quoteby%>';
		if(quote_by == "Stock ID"){
			$("#stockid").addClass('active');
		}else if(quote_by == "Commission No."){
			$("#commno").addClass('active');
		}
	});
	
	$('#txt_quote_by').val('Stock ID');
	
	$("#stockid").click(function(){
		$('#txt_quote_by').val('Stock ID');
	});
	
	$("#commno").click(function(){
		$('#txt_quote_by').val('Commission No.');
	});
	
		
</script>
</body>
<!-- END BODY -->
</html>