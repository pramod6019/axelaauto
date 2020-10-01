<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Incentive_By_Variant_Add" scope="request" />
<% mybean.doPost(request, response); %>
<jsp:setProperty name="mybean" property="*" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp"%>
<style>
label
{
	float: left;
}
.modelstyle {
	height: 600px;
	width: 500px;
}
.modal-dialog{
	width: 502px;
}
</style>
</HEAD>

<body class="page-container-bg-solid page-header-menu-fixed">
	<div id="reload" class="modelstyle">
		<div class="modal-header" onclick="listIncentive();">
			<button type="button" class="close" data-dismiss="modal">&times;</button>
			<h4 class="modal-title">
				<center> <b>Add Incentive</b> </center>
			</h4>
		</div>

		<div class="modal-body">
			<div id="timefield">
					<!-- END PAGE BREADCRUMBS -->

				<center><b><font color="#ff0000"><div id="msg"></div></font></b></center>
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="container-fluid">
								<div >
											<div class="tab-pane" id="">
											
												<!-- START PORTLET BODY -->
				<center>
												<form name="formcontact" method="post" class="form-horizontal">
													<center>
														<font size="">Form fields marked with a red
															asterisk <b><font color="#ff0000">*</font></b> are required.
														</font>
													</center><br></br>
					
					
											<div class="form-element6 form-element-center">
												<label>Variant<font color=red>*</font>:&nbsp; </label>
												<div>
													<select name="dr_item" class="form-control"
														id="dr_item" >
														<%=mybean.PopulateVariant(mybean.comp_id, request)%>
													</select>
												</div>
											</div>
											
											<div class="form-element6 form-element-center">
												<label>From Date<font color="#ff0000">*</font>:
												</label>
												<input name="txt_incentivevariant_startdate" id="txt_incentivevariant_startdate"
													 type="text" class="form-control datepicker"
							 						 onmouseover="$('.datepicker').bootstrapMaterialDatePicker({ format : 'DD/MM/YYYY',switchOnClick : true, time: false });"
													 value="<%=mybean.from_date%>" size="10" maxlength="10" />
										   </div>
											
											
											
											<div class="form-element6 form-element-center">
												<label>To Date<font color="#ff0000">*</font>:
												</label>
												<input name="txt_incentivevariant_enddate" id="txt_incentivevariant_enddate"
							 						type="text" class="form-control datepicker"
							 						onmouseover="$('.datepicker').bootstrapMaterialDatePicker({ format : 'DD/MM/YYYY',switchOnClick : true, time: false });"
													value="<%=mybean.to_date%>" size="10" maxlength="10" />
											</div>
											
											<div class="form-element6 form-element-center">
												<label>Amount <font color="#ff0000">*</font>:
												</label>
													<input name="txt_incentivevariant_amount" id="txt_incentivevariant_amount" type="text" class="form-control" 
														onkeyup="toInteger('txt_incentivevariant_amount','Amount')"
														value="<%=mybean.amount%>" size="10" maxlength="10" /> 
											</div>
													
													
											<div class="row"></div>
													<center>
														<input name="add_button" class="btn btn-success" id="add_button"
															type='button' value="Add Incentive" 
															onclick="return addIncentive();" />
														<input type="hidden" name="add_button" id="add_button" value="yes" />
														<input type="hidden" id="item_id" name="item_id" value="<%=mybean.item_id%>">
														<input type="hidden" name="brand_id" id="brand_id" value="<%=mybean.brand_id%>">
													</center>
												</form>
												</center>
											</div>
							</div>


						</div>
					</div>
				</div>
			</div>
</body>
</HTML>
