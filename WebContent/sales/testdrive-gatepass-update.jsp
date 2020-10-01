<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.TestDrive_Gatepass_Update" scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<%@include file="../Library/css.jsp"%>

</HEAD>
<body onLoad="FormFocus()" class="page-container-bg-solid page-header-menu-fixed">
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
						<h1><%=mybean.status%>
							Test Drive Gatepass
						</h1>
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
						<li><a href="../sales/index.jsp">Sales</a> &gt;</li>
						<li><a href="../sales/managetestdrivevehicle.jsp?testdriveveh_id=<%=mybean.salesgatepass_testdriveveh_id%>">List Vehicles</a> &gt;</li>
						<li><a href="../sales/testdrive-gatepass-list.jsp?testdriveveh_id=<%=mybean.salesgatepass_testdriveveh_id%>">List Test Drive Gate Pass</a> &gt;</li>
						<li><a href="../sales/testdrive-gatepass-update.jsp?<%=mybean.status.toLowerCase()%>=yes&salesgatepass_id=<%=mybean.salesgatepass_id%>&testdriveveh_id=<%=mybean.salesgatepass_testdriveveh_id%>">TestDrive Gatepass</a><b>:</b></li>
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
										<center>
											<%=mybean.status%> Test Drive Gatepass
										</center>
									</div>
								</div>
								
								<div class="portlet-body portlet-empty conatiner-fluid">
									<div class="tab-pane" id="">
										<form name="form1" method="post" class="form-horizontal">
											
											<center>
												<small></>Form fields marked with a red asterisk <font color="red">*</font> are required.</small>
											</center>
											<div class="row"></div>
											<div class="row"></div>
											<div class="form-element6">
												<label>Test Drive Vehicle ID:</label>
												<a href= "../sales/managetestdrivevehicle.jsp?testdriveveh_id=<%=mybean.salesgatepass_testdriveveh_id%>">
													<b><%=mybean.salesgatepass_testdriveveh_id%></b>
												</a>
											</div>
											
											<div class="form-element6">
												<label>From Location:</label>
												<a href="../portal/branch-summary.jsp?branch_id=<%=mybean.salesgatepass_from_branch_id%>">
														<b><%=mybean.salesgatepass_from_branch%></b></a>
											</div>
											
											<div class="form-element6">
												<label>To Location<font color="#ff0000">*</font>:</label>
												<%=mybean.PopulateBranch(mybean.salesgatepass_to_branch_id, mybean.comp_id, request)%>
											</div>
											
											<div class="form-element6">
												<label>Gate Pass Type<font color="#ff0000">*</font>:</label>
												<%=mybean.PopulateGatepassType(mybean.salesgatepass_gatepasstype_id, mybean.comp_id, request)%>
											</div>
											
											<div class="form-element6">
												<label> From Time<font color="#ff0000">*</font>: </label>
												<input name="txt_salesgatepass_fromtime" id="txt_salesgatepass_fromtime"
													value="<%=mybean.salesgatepassfromtime%>" class="form-control datetimepicker" type="text" />
											</div>
											
											
											<div class="form-element6">
												<label> To Time<font color="#ff0000">*</font>: </label>
												<input name="txt_salesgatepass_totime" id="txt_salesgatepass_totime"
													value="<%=mybean.salesgatepasstotime%>" class="form-control datetimepicker" type="text" />
											</div>
											
											<div class="row">
											<div class="form-element3">
													<label> Out Kms<font color="#ff0000">*</font>: &nbsp;</label>
														<input name="txt_salesgatepass_out_kms" type="text"
															class="form-control" id="txt_salesgatepass_out_kms"
															value="<%=mybean.salesgatepass_out_kms%>" size="18"
															maxlength="10"
															onKeyUp="toFloat('txt_salesgatepass_out_kms','Out Kms')">

											</div>
											
											<div class="form-element3">
												<label>Driver: </label>
													<select class="form-control select2"  id="allexecutives" name="allexecutives" >
														<%=mybean.execheck.PopulateDriver(mybean.comp_id, mybean.salesgatepass_driver_id)%>
													</select>
											</div>
											
											<div class="form-element6">
											<label>Notes:</label>
												<textarea name="txt_salesgatepass_notes" cols="40" rows="4"
													class="form-control" id="txt_salesgatepass_notes"
													onkeyup="charcount('txt_salesgatepass_notes', 'span_txt_salesgatepass_notes','<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.salesgatepass_notes%></textarea>
												<span id="span_txt_salesgatepass_notes"> (255 Characters)</span>
											</div>
											
											
											
											</div>
											
											<div class="row"></div>
											<%
												if (mybean.status.equals("Update") && (mybean.entry_by != null) && !(mybean.entry_by.equals(""))) {
											%>
											
											<div class="form-element6">
												<label >Entry By:&nbsp;</label>
												<span >
													<%=mybean.unescapehtml(mybean.entry_by)%>
													<input name="entry_by" type="hidden" id="entry_by" value="<%=mybean.entry_by%>" />
												</span>
											</div>
											
											<div class="form-element6">
												<label >Entry Date:&nbsp;</label>
												<span >
													<%=mybean.entry_date%>
													<input type="hidden" name="entry_date" value="<%=mybean.entry_date%>" />
												</span>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && (mybean.modified_by != null) && !(mybean.modified_by.equals(""))) {
											%>
											<div class="form-element6">
												<label >Modified By:&nbsp;</label>
												<span >
													<%=mybean.unescapehtml(mybean.modified_by)%>
													<input name="modified_by" type="hidden" id="modified_by" value="<%=mybean.modified_by%>" />
												</span>
											</div>
											<div class="form-element6">
												<label >Modified Date:&nbsp;</label>
												<span >
													<%=mybean.modified_date%>
													<input type="hidden" name="modified_date" value="<%=mybean.modified_date%>" />
												</span>
											</div>
											<%
												}
											%>
											<div class="row"></div>
											<center>
												<%
													if (mybean.status.equals("Add")) {
												%>
												
												<input name="button" type="submit" class="btn btn-success" id="button" value="Add Gate Pass"
													onClick="onPress(); return SubmitFormOnce(document.form1, this);" />
												
												<input type="hidden" name="add_button" value="yes">
												
												<%
													} else if (mybean.status.equals("Update")) {
												%>
												
												<input name="update_button" type="hidden" value="yes" />
												
												<input name="button" type="submit" class="btn btn-success" id="button" value="Update Gate Pass"
													onClick="onPress(); return SubmitFormOnce(document.form1, this);" />
												
												<input name="delete_button" type="submit" class="btn btn-success" id="delete_button"
													OnClick="return confirmdelete(this)" value="Delete Gate Pass" />
												<%
													}
												%>

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
	<!-- END CONTAINER -->
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
</body>
</HTML>

