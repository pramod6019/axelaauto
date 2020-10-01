<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.CouponCampaign_Update"
	scope="request" />
<%mybean.doGet(request, response);%>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>

</head>
<body onload='FormFocus();calBudget();' class="page-container-bg-solid page-header-menu-fixed">

	<%@include file="../portal/header.jsp"%>
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1><%=mybean.status%>
							Coupon Campaign
						</h1>
					</div>
					<!-- END PAGE TITLE -->

				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<div class="page-content-inner">
						<!-- BEGIN PAGE BREADCRUMBS -->
						<ul class="page-breadcrumb breadcrumb">
							<li><a href="../portal/home.jsp">Home</a> &gt;</li>
							<li><a href="../service/index.jsp">Service</a> &gt;</li>
							<li><a href="couponcampaign-list.jsp?all=yes">List Coupon Campaign</a> &gt;</li>
							<li><a href="couponcampaign-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Coupon
									Camapign</a><b>:</b></li>

						</ul>
						<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">

							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>

							<div class="container-fluid portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%> Coupon Campaign
									</div>
								</div>
								<div class="portlet-body">
									<div class="tab-pane" id="">
										<center>
											<font size="1">Form fields marked with a red asterisk
												<b><font color="#ff0000">*</font></b> are required.
											</font>
										</center>
										<br />
										<form method="post" name="form1" class="form-horizontal">

											<div class="form-element6">
												<!--campaign name text  -->
												<label>Coupon Campaign Name<font color="#ff0000">*</font>: </label>
												<input name="txt_campaign_name" type="text" id="txt_campaign_name"
													class="form-control" value="<%=mybean.couponcampaign_name%>" />
											</div>
											
											<div class="form-element6 ">
												<!--brand id dropdown  -->
												<label>Brand<font color="#ff0000">*</font>: </label>
												<select name="dr_brand_id" class="form-control" id="dr_brand_id">
													<%=mybean.PopulateBrand(mybean.comp_id)%>
												</select>
											</div>

											<div class="form-element6 ">
												<!--campaigntype dropdown  -->
												<label> Coupon Campaign Type<font color="#ff0000">*</font>: </label>
												<select name="dr_campaign_id" class="form-control" id="dr_campaign_id">
													<%=mybean.PopulateType(mybean.comp_id)%>
												</select>
											</div>
											
											<div class="form-element6 ">
												<!--dept name dropdown  -->
												<label>Department<font color="#ff0000">*</font>: </label>
												<select name="dr_dept_id" class="form-control" id="dr_dept_id">
													<%=mybean.PopulateDepartment(mybean.comp_id)%>
												</select>
											</div>

											<div class="row">
												<div class="form-element6">
													<!-- coupon campaign description -->
													<label> Coupon Campaign Description:&nbsp; </label>
													<textarea name="txt_campaign_desc" cols="40" rows="5"
														class="form-control" id="txt_campaign_desc"
														onKeyUp="charcount('txt_customer_address', 'span_txt_customer_address','<font color=red>({CHAR} characters left)</font>', '255')"> <%=mybean.couponcampaign_desc%></textarea>
													<span id="span_txt_customer_address">(255 Characters)</span>
												</div>
	
												<div class="form-element6 form-element">
													<!--date picker  -->
													<div class="form-element6">
														<label>Start Date<font color="#ff0000">*</font>: </label>
														<input name="txt_start_date" id="txt_start_date"
															value="<%=mybean.start_time%>"
															class="form-control datepicker" type="text" />
													</div>
	
													<div class="form-element6">
														<label>End Date<font color="#ff0000">*</font>:
														</label> <input name="txt_end_date" id="txt_end_date"
															value="<%= mybean.end_time%>"
															class="form-control datepicker" type="text" /> 
													</div>
												</div>
											

												<div class="form-element6">
													<!--coupon offer text  -->
													<label> Coupon Offer:</label>
													<input name="txt_coupon_offer" type="text" class="form-control"
														id="txt_coupon_offer" value="<%=mybean.couponcampaign_couponoffer%>"/>
												</div>
											
											</div>

											<div class="form-element6">
												<!--coupon count text  -->
												<label> Coupon Count<font color="#ff0000">*</font>: </label>
												<input name="txt_coupon_count" type="text" id="txt_coupon_count"
													onKeyUp="toInteger('txt_coupon_count','Coupon Count');calBudget();" size="20" maxlength="10"
													class="form-control" value="<%=mybean.couponcampaign_couponcount%>"/>
											</div>

											<div class="form-element6">
												<!--coupon value text  -->
												<label> Coupon Value<font color="#ff0000">*</font>: </label>
												<input name="txt_coupon_value" type="text" id="txt_coupon_value"
													onKeyUp="toInteger('txt_coupon_value','Coupon Value');calBudget();" size="20" maxlength="10"
													class="form-control" value="<%=mybean.couponcampaign_couponvalue%>"/>
											</div>
											
											<div class="form-element6 form-element form-element-margin">	
												<div class="form-element12">		<!--budget  -->
													<label>Budget<font color="#ff0000">*</font>:&nbsp;</label>
													<span id="hintbudget"><%=mybean.couponcampaign_budget%></span>
												</div>
												
												<div class="form-element12 form-element-margin">
													<!--active checkbox  -->
													<label>Active:&nbsp;</label>
													<input id="chk_customer_active" type="checkbox" name="chk_customer_active"
														<%=mybean.PopulateCheck(mybean.couponcampaign_active)%> />
												</div>
											</div>
											
											<div class="form-element6">
												<!-- coupon campaign notes -->
												<label>Campaign Notes: </label>
												<textarea name="txt_campaign_notes" cols="40" rows="4"
													class="form-control" id="txt_customer_address"
													onKeyUp="charcount('txt_customer_address', 'span_txt_customer_address','<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.couponcampaign_notes%></textarea>
												<span id="txt_campaign_notes">(255 Characters)</span>
											</div>
											

											
											<div class="row"></div>
											<%if (mybean.status.equals("Update") && !(mybean.entry_by == null)
														&& !(mybean.entry_by.equals(""))) {%>

											<div class="form-element6 form-element-center">
												<div class="form-element6">
													<label>Entry By:</label>
													<%=mybean.unescapehtml(mybean.entry_by)%>
													<input name="entry_by" type="hidden" id="entry_by" value="<%=mybean.entry_by%>" />
												</div>

												<div class="form-element6">
													<label>Entry Date:</label>
													<%=mybean.entry_date%>
													<input type="hidden" name="entry_date" value="<%=mybean.entry_date%>" />
												</div>
												<%}%>
												<%if (mybean.status.equals("Update") && !(mybean.modified_by == null)
															&& !(mybean.modified_by.equals(""))) {%>

												<div class="form-element6">
													<label>Modified By:</label>
													<%=mybean.unescapehtml(mybean.modified_by)%>
													<input name="modified_by" type="hidden" id="modified_by"
														value="<%=mybean.modified_by%>" />
												</div>
												<div class="form-element6">
													<label>Modified Date:</label>
													<%=mybean.modified_date%>
													<input type="hidden" name="modified_date"
														value="<%=mybean.modified_date%>" />
												</div>
											</div>
											<%}%>

											<div class="row"></div>

											<center>
												<%if (mybean.status.equals("Add")) {%>
												<input name="button" type="submit" class="btn btn-success"
													id="button" value="Add Coupon Campaign"
													onClick="return SubmitFormOnce(document.form1, this);" />
												<input type="hidden" name="add_button" value="yes"/>
												<%} else if (mybean.status.equals("Update")) {%>
												<input type="hidden" name="update_button" value="yes"/>
												<input name="button" type="submit" class="btn btn-success"
													id="button" value="Update Coupon Campaign"
													onClick="return SubmitFormOnce(document.form1, this);" />
												<input name="delete_button" type="submit"
													class="btn btn-success" id="delete_button"
													OnClick="return confirmdelete(this)"
													value="Delete Coupon Campaign" />
												<%}%>

											</center>

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

	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
	<script language="JavaScript" type="text/javascript">
		function FormFocus() {
			document.form1.txt_campaign_name.focus()
		}
		
		function calBudget(){
			var couponcount = document.getElementById('txt_coupon_count').value;
			var couponvalue = document.getElementById('txt_coupon_value').value;
			
			document.getElementById('hintbudget').innerHTML = couponcount * couponvalue;
		}
	</script>
</body>
</html>