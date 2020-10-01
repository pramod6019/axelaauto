<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Incentive_By_Target_Add" scope="request" />
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
/* .modelstyle { */
/* 	height: 490px; */
/* 	width: 500px; */
/* } */
/* .modal-dialog{ */
/* 	width: 502px; */
/* } */
</style>
</HEAD>

<body class="page-container-bg-solid page-header-menu-fixed">
	<div id="reload" class="modelstyle">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">&times;</button>
			<h4 class="modal-title">
				<center> <b>Add <%=mybean.addtype %> Incentive</b> </center>
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
					
										<% if(mybean.type.equals("overallwise")) { %>
											<div class="form-element6 form-element-center">
												<label>Band From <font color="#ff0000">*</font>:
												</label>
													<input name="txt_incentivetargetband_from" id="txt_incentivetargetband_from" type="text" class="form-control" 
														value="<%=mybean.band_from%>" size="3" maxlength="3" /> 
											</div>
											
											<div class="form-element6 form-element-center">
												<label>Band To <font color="#ff0000">*</font>:
												</label>
													<input name="txt_incentivetargetband_to" id="txt_incentivetargetband_to" type="text" class="form-control" 
														value="<%=mybean.band_to%>" size="3" maxlength="3" /> 
											</div>
											
											<div class="form-element6 form-element-center">
												<label>Amount <font color="#ff0000">*</font>:
												</label>
													<input name="txt_incentivetargetband_amount" id="txt_incentivetargetband_amount" type="text" class="form-control" 
														value="<%=mybean.amount%>" size="10" maxlength="10" /> 
											</div>
										<%} %>	
										
										<% if(mybean.type.equals("slabwise")) { %>
											<div class="form-element6">
												<label>Slab From <font color="#ff0000">*</font>:
												</label>
													<input name="txt_incentiveslab_from" id="txt_incentiveslab_from" type="text" class="form-control" 
														value="<%=mybean.slab_from%>" size="3" maxlength="3" /> 
											</div>
											
											<div class="form-element6">
												<label>Slab To <font color="#ff0000">*</font>:
												</label>
													<input name="txt_incentiveslab_to" id="txt_incentiveslab_to" type="text" class="form-control" 
														value="<%=mybean.slab_to%>" size="3" maxlength="3" /> 
											</div>
											
											<div class="form-element6">
												<label>SO Amount : </label>
													<input name="txt_incentiveslab_soamount" id="txt_incentiveslab_soamount" type="text" class="form-control" 
														value="<%=mybean.so_amount%>" size="10" maxlength="10" /> 
											</div>
											
											<div class="form-element6">
												<label>Finance Amount : </label>
													<input name="txt_incentiveslab_finamount" id="txt_incentiveslab_finamount" type="text" class="form-control" 
														value="<%=mybean.finance_amount%>" size="10" maxlength="10" /> 
											</div>
											
											<div class="form-element6">
												<label>Insurance Amount : </label>
													<input name="txt_incentiveslab_insuramount" id="txt_incentiveslab_insuramount" type="text" class="form-control" 
														value="<%=mybean.insurance_amount%>" size="10" maxlength="10" /> 
											</div>
											
											<div class="form-element6">
												<label>Ext. Warranty Amount : </label>
													<input name="txt_incentiveslab_ewamount" id="txt_incentiveslab_ewamount" type="text" class="form-control" 
														value="<%=mybean.ew_amount%>" size="10" maxlength="10" /> 
											</div>
											
											<div class="form-element6">
												<label>Accessories Minimum Amount : </label>
													<input name="txt_incentiveslab_accessminamount" id="txt_incentiveslab_accessminamount" type="text" class="form-control" 
														value="<%=mybean.accessmin_amount%>" size="10" maxlength="10" /> 
											</div>
											
											<div class="form-element6">
												<label>Accessories Amount : </label>
													<input name="txt_incentiveslab_accessamount" id="txt_incentiveslab_accessamount" type="text" class="form-control" 
														value="<%=mybean.access_amount%>" size="10" maxlength="10" /> 
											</div>
											
											<div class="form-element6">
												<label>Exchange Amount : </label>
													<input name="txt_incentiveslab_exchangeamount" id="txt_incentiveslab_exchangeamount" type="text" class="form-control" 
														value="<%=mybean.exchange_amount%>" size="10" maxlength="10" /> 
											</div>
										<%} %>	
										
										<% if(mybean.type.equals("insurancewise") || mybean.type.equals("ewwise") || mybean.type.equals("exchangewise")) { %>
											<div class="form-element6 form-element-center">
												<label>Slab From <font color="#ff0000">*</font>:
												</label>
													<input name="txt_<%=mybean.type %>_from" id="txt_<%=mybean.type %>_from" type="text" class="form-control" 
														value="<%=mybean.slab_from%>" size="3" maxlength="3" /> 
											</div>
											
											<div class="form-element6 form-element-center">
												<label>Slab To <font color="#ff0000">*</font>:
												</label>
													<input name="txt_<%=mybean.type %>_to" id="txt_<%=mybean.type %>_to" type="text" class="form-control" 
														value="<%=mybean.slab_to%>" size="3" maxlength="3" /> 
											</div>
											
											<div class="form-element6 form-element-center">
												<label>Amount <font color="#ff0000">*</font>:
												</label>
													<input name="txt_<%=mybean.type %>_amount" id="txt_<%=mybean.type %>_amount" type="text" class="form-control" 
														value="<%=mybean.amount%>" size="10" maxlength="10" /> 
											</div>
											<%} %>	
											
											<% if(mybean.type.equals("financewise")) { %>
											<div class="form-element6 form-element-center">
												<label>From <font color="#ff0000">*</font>:
												</label>
													<input name="txt_<%=mybean.type %>_from" id="txt_<%=mybean.type %>_from" type="text" class="form-control" 
														value="<%=mybean.slab_from%>" size="10" maxlength="10" /> 
											</div>
											
											<div class="form-element6 form-element-center">
												<label>To <font color="#ff0000">*</font>:
												</label>
													<input name="txt_<%=mybean.type %>_to" id="txt_<%=mybean.type %>_to" type="text" class="form-control" 
														value="<%=mybean.slab_to%>" size="10" maxlength="10" /> 
											</div>
											
											<div class="form-element6 form-element-center">
												<label>Amount <font color="#ff0000">*</font>:
												</label>
													<input name="txt_<%=mybean.type %>_amount" id="txt_<%=mybean.type %>_amount" type="text" class="form-control" 
														value="<%=mybean.amount%>" size="10" maxlength="10" /> 
											</div>
											<%} %>
											
											<% if(mybean.type.equals("accessorieswise")) { %>
											<div class="form-element6 form-element-center">
												<label>From <font color="#ff0000">*</font>:
												</label>
													<input name="txt_<%=mybean.type %>_from" id="txt_<%=mybean.type %>_from" type="text" class="form-control" 
														value="<%=mybean.slab_from%>" size="10" maxlength="10" /> 
											</div>
											
											<div class="form-element6 form-element-center">
												<label>To <font color="#ff0000">*</font>:
												</label>
													<input name="txt_<%=mybean.type %>_to" id="txt_<%=mybean.type %>_to" type="text" class="form-control" 
														value="<%=mybean.slab_to%>" size="10" maxlength="10" /> 
											</div>
											
											<div class="form-element6 form-element-center">
												<label>Percentage <font color="#ff0000">*</font>:
												</label>
													<input name="txt_<%=mybean.type %>_percentage" id="txt_<%=mybean.type %>_percentage" type="text" class="form-control" 
														value="<%=mybean.percentage%>" size="10" maxlength="10" /> 
											</div>
											<%} %>	
													
											<div class="row"></div>
													<center>
														<input name="add_button" class="btn btn-success" id="add_button"
															type='button' value="Add Incentive" 
															onclick="return addIncentive();" />
														<input type="hidden" name="add_button" id="add_button" value="yes" />
														<input type="hidden" name="band_id" value="<%=mybean.band_id%>">
														<input type="hidden" name="brand_id" id="brand_id" value="<%=mybean.brand_id%>">
														<input type="hidden" name="type" id="type" value="<%=mybean.type%>">
													</center>
												</form>
												</center>
											</div>
								</div>



							</div>


						</div>
					</div>
				</div>
			</div>
</body>
</HTML>
