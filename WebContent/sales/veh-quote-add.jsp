<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Veh_Quote_Add" scope="request" />
<%mybean.doGet(request, response);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" />
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<%@include file="../Library/css.jsp" %>
<style>
#stockid{
  border-bottom-left-radius: 15px;
  border-top-left-radius: 15px;
}
#commno{
margin-left: -1em;
border-bottom-right-radius: 15px;
border-top-right-radius: 15px;
}
</style>
</HEAD>
<body onLoad="FormFocus()" class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>
	<!--         START -->

	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD -->
			<div class="page-head">
				<div class="container-fluid">
					<div class="page-title">
						<h1>Add Quote</h1>
					</div>
				</div>
			</div>
			<!-- END PAGE HEAD -->
			<!-- BEGIN PAGE CONTENT BODY ------------->
			<div class="page-content">
			<div class="page-content-inner">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../sales/index.jsp">Sales</a> &gt;</li>
						<li><a href="veh-quote-add.jsp">Add Quote</a><b>:</b></li>

					</ul>

						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="caption" style="float: none">
								<center>
									<font color="#ff0000"><b><%=mybean.msg%></b></font
								</center>
							</div>
							<%if(mybean.enquiry_enquirytype_id.equals("1")){%>
							
							<%if(!mybean.quote_enquiry_id.equals("0")
									&& !mybean.next_button.equals("ADD By Enquiry") ){%>
							<form name="form1" id="form1" method="post" class="form-horizontal">
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Add Quote By Stock ID</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="container-fluid">
											<!-- START PORTLET BODY -->
												<center>
													Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b> are required.
												</center>
											
											<center>
												<div class="clearfix">
													<div class="btn-group" data-toggle="buttons">
														<label class="btn btn-default" id="stockid">
														<input type="radio" class="toggle" id="txt_quote_by_stock" value="Stock ID">Stock ID</label>
														
														<label class="btn btn-default" id="commno">
														<input type="radio" class="toggle" id="txt_quote_by_comm" value="Commission No.">Commission No.</label>
													</div>
													<input type='text' id='txt_quote_by' name='txt_quote_by' value='<%=mybean.quoteby%>' hidden/>
												</div>
											</center>
											 <center>
												<div class="form-element5">
												</div>
												<div class="form-element2">
													<input name="txt_vehstock_details" type="text" class="form-control" id="txt_vehstock_details"
														value="<%=mybean.txt_vehstock_details%>" onkeyup="toInteger(this.id);" />
												</div>
											 </center>
											
											<div class="form-element12">
												<center>
													<input name="next_button" type="submit" class="btn btn-success" id="next_button" value="Next" />
													<input name="button_addby" type="hidden" id="button_addby" value="ADD By ID" />
												</center>
											</div>

										</div>
									</div>
								</div>
							</form>
						
						
						
<!-- 						------------------------------------------------------------------------- -->
						
						
						<form name="form2" method="post" class="form-horizontal">
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Add Quote By Variant</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="container-fluid">
											<!-- START PORTLET BODY -->
												<center>
													Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b> are required.
												</center>
											
											<div class="form-element3"></div>
											<div class="form-element3">
													<label>Model<font color="#ff0000">*</font>: </label>
														<select name="dr_item_model_id" id="dr_item_model_id"
															class="form-control" onchange="PopulateItem(this.value);">
															<%=mybean.PopulateModel(mybean.comp_id)%>
														</select>
													</div>

											<div class="form-element3">
												<label>Variant<font color="#ff0000">*</font>: </label>
													<span id="model-item"><%=mybean.PopulateItem(mybean.item_model_id, mybean.comp_id)%>
													</span>
											</div>
											
											<div class="form-element12">
												<center>
													<input name="next_button" type="submit" class="btn btn-success" id="next_button" value="Next" />
													<input name="button_addby" type="hidden" id="button_addby" value="ADD By Variant" />
												</center>
											</div>

										</div>
									</div>
								</div>
							</form>
						<%} %>
						
						<%if(!mybean.quote_stock_id.equals("0") 
								&& !mybean.next_button.equals("ADD By ID")
								&& !mybean.next_button.equals("ADD By Variant")){%>
						
						<form name="form3" method="post" class="form-horizontal">
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Add Quote By Enquiry</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="container-fluid">
											<!-- START PORTLET BODY -->
												<center>
													Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b> are required.
												</center>
											</br>
											<div class="form-element5"></div>
											<div class="form-element3">
												<label>Model: </label> 
													<a href="../inventory/item-model.jsp?model_id=<%=mybean.item_model_id%>"><%=mybean.model_name%> </a>
											</div>
                                            <div class="row"></div>
                                            <div class="form-element5"></div>
											<div class="form-element3">
												<label>Variant: </label>
												<a href="../inventory/inventory-item-list.jsp?item_id=<%=mybean.enquiry_item_id%>"><%=mybean.item_name%> </a>
											</div>
											<div class="row"></div>
											<div class="form-element5"></div>
											<div class="form-element3">
											
												<label>Enquiry ID<font color="#ff0000">*</font>: </label>
													<input name="txt_quote_enquiry_id" type="text"
														class="form-control" style="top: 8px"
														id="txt_quote_enquiry_id"
														value="<%=mybean.quote_enquiry_id%>"
														onkeyup="toInteger(this.id);" />
														</div>
											<div class="form-element12">
												<center>
													<input name="next_button" type="submit" class="btn btn-success" id="next_button" value="Next" />
													<input name="button_addby" type="hidden" id="button_addby" value="ADD By Enquiry" />
												</center>
											</div>
														
											

										</div>
									</div>
								</div>
							</form>
						
						
						<%} %>
						
						<%} %>
						
						<%if(mybean.enquiry_enquirytype_id.equals("2")){%>
						<form name="form4" method="post" class="form-horizontal">
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Add Quote By Pre-Owned Stock</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="container-fluid">
											<!-- START PORTLET BODY -->
												<center>
													Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b> are required.
												</center>
											</br>
											<div class="form-element5"></div>
											<div class="form-element3">
												<label>Model: </label> 
												<input type="hidden" name="dr_item_model_id" id="dr_item_model_id" value="<%=mybean.item_model_id%>">
												<input type="hidden" name="dr_item_id" id="dr_item_id" value="<%=mybean.enquiry_item_id%>">
												<input type="hidden" name="txt_item_name" id="txt_item_name" value="<%=mybean.item_name%>">
												<input type="hidden" name="txt_model_name" id="txt_model_name" value="<%=mybean.model_name%>">

													<a href="../preowned/managepreownedmodel.jsp?preownedmodel_id=<%=mybean.item_model_id%>"><%=mybean.model_name%> </a>
											</div>
                                            <div class="row"></div>
                                            <div class="form-element5"></div>
											<div class="form-element3">
												<label>Variant: </label>
													<a href="../preowned/managepreownedvariant.jsp?variant_id=<%=mybean.enquiry_item_id%>"><%=mybean.item_name%> </a>
											</div>
											<div class="row"></div>
											<div class="form-element5"></div>
											<div class="form-element3">
											
												<label>Pre-Owned Stock ID.<font color="#ff0000">*</font>: </label>
													<input name="txt_preowned_stock_id" type="text"
														class="form-control" style="top: 8px"
														id="txt_preowned_stock_id"
														value="<%=mybean.preownedstock_id%>"
														onkeyup="toInteger(this.id);" />
														</div>
											<div class="form-element12">
												<center>
													<input name="next_button" type="submit" class="btn btn-success" id="next_button" value="Next" />
													<input name="button_addby" type="hidden" id="button_addby" value="ADD By PreownedStock" />
												</center>
											</div>
														
											

										</div>
									</div>
								</div>
							</form>
						
						<%} %>
						
						
						
						
						
						
						
						
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>

	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp" %>
	
	<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
// 		document.form1.dr_branch_id.focus()
	}

	function PopulateItem(model_id) {
		showHint("veh-quote-check.jsp?model_id=" + model_id + "&list_model_item=yes", "model-item");
	}
	
	 
	$(function(){
		var quote_by = '<%=mybean.quoteby%>';
		if(quote_by == "Stock ID"){
			$("#stockid").addClass('active');
		}else if(quote_by == "Commission No."){
			$("#commno").addClass('active');
		}
	});
	$("#stockid").click(function(){
		$('#txt_quote_by').val('Stock ID');
	});
	$("#commno").click(function(){
		$('#txt_quote_by').val('Commission No.');
	});
	
		
</script>
	</body>
</HTML>