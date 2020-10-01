<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Service_Target_Update"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp" %>
<script type="text/javascript">
	function isNumber(ob) {
		var invalidChar = /[^0-9]/gi
		if (invalidChar.test(ob.value)) {
			ob.value = ob.value.replace(invalidChar, "");
		}
	}
</script>
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
						<h1>Service Target Update</h1>
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
						<li><a href="../service/index.jsp">Service</a> &gt;</li>
						<li><a href="target.jsp">Target</a>&gt;</li>
						<li><a href="service-target-list.jsp?dr_executives=<%=mybean.Service_target_emp_id%>&dr_year=<%=mybean.year%>">
						<%=mybean.month_name%>-<%=mybean.year%></a> &gt; 
						<a href="service-target-list.jsp?dr_executives=<%=mybean.Service_target_emp_id%>&dr_year=<%=mybean.year%>">List Targets</a> &gt; 
						<a href="../portal/executive-list.jsp?emp_id=<%=mybean.Service_target_emp_id%>"><%=mybean.emp_name%></a> &gt; 
						<a href="service-target-update.jsp?<%=mybean.QueryString%>">Update Target</a><b> :</b>
						</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="#ff0000"><b><%=mybean.msg%></b></font>
					</center>
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<center>Service Target Update</center>
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<form name="formcontact" method="post" class="form-horizontal">
											<center>
												<font size="1">Form fields marked with a red asterisk
													<font color=#ff0000>*</font> are required.
												</font>
											</center>
											</br>
											<div class="form-element3">
												<label >Job Card Count :</label>
													<input name="txt_jc_count" type="text"
														class="form-control" id="txt_jc_count"
														onkeyup="isNumber(this)"
														value="<%=mybean.service_target_jc_count%>" size="10"
														maxlength="9" />
											</div>
											
											<div class="form-element3">
												<label >PMS Count :</label>
													<input name="txt_pms_count" type="text"
														class="form-control" id="txt_pms_count"
														onkeyup="toFloat('txt_pms_count','')""
														value="<%=mybean.service_target_pms_count%>" size="10"
														maxlength="9" />
											</div>
											
											<div class="form-element3">
												<label >Labour Amount :</label>
													<input name="txt_labour_amt" type="text"
														class="form-control" id="txt_labour_amt"
														onkeyup="toFloat('txt_labour_amt','')"
														value="<%=mybean.service_target_labour_amount%>" size="10"
														maxlength="9" />
											</div>
											<div class="form-element3">
												<label >Parts Amount:</label>
													<input name="txt_parts_amt" type="text"
														class="form-control" id="txt_parts_amt"
														onkeyup="toFloat('txt_parts_amt','')"
														value="<%=mybean.service_target_parts_amount%>" size="10"
														maxlength="9" />
											</div>
											<div class="form-element3">
												<label >Oil Amount:</label>
													<input name="txt_oil_amt" type="text"
														class="form-control" id="txt_oil_amt"
														onkeyup="toFloat('txt_oil_amt','')"
														value="<%=mybean.service_target_oil_amount%>"
														size="10" maxlength="9" />
											</div>
											<div class="form-element3">
												<label >CNG Amount:</label>
													<input name="txt_cng_amt" type="text"
														class="form-control" id="txt_cng_amt"
														onkeyup="toFloat('txt_cng_amt','')"
														value="<%=mybean.service_target_cng_amount%>" size="10"
														maxlength="9" />
											</div>
											<div class="form-element3">
												<label >Tyre Count:</label>
													<input name="txt_tyre_count" type="text"
														class="form-control" id="txt_tyre_count"
														onkeyup="toFloat('txt_tyre_count','')"
														value="<%=mybean.service_target_tyre_count%>" size="10"
														maxlength="9" />
											</div>
											<div class="form-element3">
												<label >Tyre Amount:</label>
													<input name="txt_tyre_amt" type="text"
														class="form-control" id="txt_tyre_amt"
														onkeyup="toFloat('txt_tyre_amt','')"
														value="<%=mybean.service_target_tyre_amount%>" size="10"
														maxlength="9" />
											</div>
											
											<div class="form-element3">
												<label >Brake Count:</label>
													<input name="txt_break_count" type="text"
														class="form-control" id="txt_break_count"
														onkeyup="toFloat('txt_break_count','')"
														value="<%=mybean.service_target_break_count%>" size="10"
														maxlength="9" />
											</div>
											
											<div class="form-element3">
												<label >Brake Amount:</label>
													<input name="txt_break_amt" type="text"
														class="form-control" id="txt_break_amt"
														onkeyup="toFloat('txt_break_amt','')"
														value="<%=mybean.service_target_break_amount%>" size="10"
														maxlength="9" />
											</div>
											
											<div class="form-element3">
												<label >Battery Count:</label>
													<input name="txt_battery_count" type="text"
														class="form-control" id="txt_battery_count"
														onkeyup="toFloat('txt_battery_count','')"
														value="<%=mybean.service_target_battery_count%>" size="10"
														maxlength="9" />
											</div>
											<div class="form-element3">
												<label >Battery Amount:</label>
													<input name="txt_battery_amt" type="text"
														class="form-control" id="txt_battery_amt"
														onkeyup="toFloat('txt_battery_amt','')"
														value="<%=mybean.service_target_battery_amount%>" size="10"
														maxlength="9" />
											</div>
											
											<div class="form-element3">
												<label >Accessories Amount:</label>
													<input name="txt_accessories_amt" type="text"
														class="form-control" id="txt_accessories_amt"
														onkeyup="toFloat('txt_accessories_amt','')"
														value="<%=mybean.service_target_accessories_amount%>"
														size="10" maxlength="9" />
											</div>
											
											<div class="form-element3">
												<label >VAS Amount:</label>
													<input name="txt_vas_amt" type="text"
														class="form-control" id="txt_vas_amt"
														onkeyup="toFloat('txt_vas_amt','')"
														value="<%=mybean.service_target_vas_amount%>" size="10"
														maxlength="9" />
											</div>
											<div class="form-element3">
												<label >Extended Warranty Count:</label>
													<input name="txt_extwarranty_count" type="text"
														class="form-control" id="txt_extwarranty_count"
														onkeyup="toFloat('txt_extwarranty_count','')"
														value="<%=mybean.service_target_extwarranty_count%>" size="10"
														maxlength="9" />
											</div> 
											<div class="form-element3">
												<label >Extended Warranty Amount:</label>
													<input name="txt_extwarranty_amt" type="text"
														class="form-control" id="txt_extwarranty_amt"
														onkeyup="toFloat('txt_extwarranty_amt','')"
														value="<%=mybean.service_target_extwarranty_amount%>" size="10"
														maxlength="9" />
											</div>
											
											<div class="form-element3">
												<label >Wheel Alignment Amount:</label>
													<input name="txt_wheelalignment_amt" type="text"
														class="form-control" id="txt_wheelalignment_amt"
														onkeyup="toFloat('txt_wheelalignment_amt','')"
														value="<%=mybean.service_target_wheelalignment_amount%>" size="10"
														maxlength="9" />
											</div>
											
											<div class="row"></div>
											
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) {
											%>
                                        <div class="row">
											<div class="form-element6 ">
												<label >Entry By:&nbsp;</label>
													<%=mybean.unescapehtml(mybean.entry_by)%>
													<input name="entry_by" type="hidden" id="entry_by"
														value="<%=mybean.entry_by%>" />
											</div>

											<div class="form-element6 ">
												<label >Entry Date:&nbsp;</label>
													<%=mybean.entry_date%>
													<input type="hidden" name="entry_date"
														value="<%=mybean.entry_date%>" />
											</div>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) {
											%>
<div class="row">
											<div class="form-element6 ">
												<label >Modified By:&nbsp;</label>
													<%=mybean.unescapehtml(mybean.modified_by)%>
													<input name="modified_by" type="hidden" id="modified_by"
														value="<%=mybean.modified_by%>"></input>
											</div>

											<div class="form-element6 ">
												<label >Modified Date:&nbsp;</label>
													<%=mybean.modified_date%>
													<input type="hidden" name="modified_date"
														value="<%=mybean.modified_date%>" />
														</div>
												<%
													}
												%>
												<center>
											<div class="form-element12 form-center">
													<input name="update_button" type="submit" class="btn btn-success" id="" value="Update" />
														</div>
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

	</div>
	<!-- END CONTAINER -->

	<%@include file="../Library/admin-footer.jsp"%>
<%@include file="../Library/js.jsp" %>

</body>
</HTML>
