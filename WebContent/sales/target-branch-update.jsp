<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Target_Branch_Update" scope="request" />
<% mybean.doPost(request, response); %>
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
						<h1>Branch Target Update</h1>
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
							<li><a href="index.jsp">Sales</a>&gt;</li>
							<li><a href="target.jsp">Target</a>&gt;</li>
							<li><a href="target-branch-list.jsp?dr_branch=<%=mybean.branch_id%>&dr_year=<%=mybean.year%>"> <%=mybean.month_name%>-<%=mybean.year%></a>&gt;</li>
							<li><a href="../portal/branch-summary.jsp?branch_id=<%=mybean.branch_id%>"><%=mybean.branch_name%></a> &gt;</li>
							<li><a href="target-branch-update.jsp?<%=mybean.QueryString%>">Update Target</a><b>:</b></li>
						</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="#ff0000"><b><%=mybean.msg%></b></font>
					</center>
					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<center>Branch Target Update</center>
									</div>
								</div>
								
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<form name="formcontact" method="post" class="form-horizontal">
											<center>
												<font size="">Form fields marked with a red asterisk
													<font color=#ff0000>*</font> are required.
												</font>
											</center>
											
											<br />
											
											<div class="form-element6">
												<label>Enquiry Count :</label>
												<input name="txt_Enquiry_count" type="text"
													class="form-control" id="txt_Enquiry_count" onkeyup="isNumber(this)"
													value="<%=mybean.branchtarget_enquiry_count%>" size="10" maxlength="9" />
											</div>
											
											<div class="form-element6">
												<label>Home Visit Count:</label>
												<input name="txt_home_count" type="text" class="form-control"
													id="txt_home_count" onkeyup="isNumber(this)" maxlength="9" 
													value="<%=mybean.branchtarget_homevisit_count%>" size="10" />
											</div>
											
											<div class="form-element6">
												<label>Test Drive Count:</label>
												<input name="txt_drive_count" type="text" class="form-control" id="txt_drive_count"
													onkeyup="isNumber(this)" value="<%=mybean.branchtarget_testdrives_count%>"
													size="10" maxlength="9" />
											</div>
											
											<div class="form-element6">
												<label>Booking Count:</label>
												<input name="txt_book_count" type="text" class="form-control" id="txt_book_count"
													onkeyup="isNumber(this)" value="<%=mybean.branchtarget_bookings_count%>" size="10"
													maxlength="9" />
											</div>
											
											<div class="form-element6">
												<label>Delivery Count:</label>
												<input name="txt_delivery_count" type="text" class="form-control" id="txt_delivery_count"
													onkeyup="isNumber(this)" value="<%=mybean.branchtarget_delivery_count%>" size="10"
													maxlength="9" />
											</div>
											
											<div class="form-element6">
												<label>Accessories Amount:</label>
												<input name="txt_accessories_count" type="text" class="form-control" id="txt_accessories_count"
													onkeyup="isNumber(this)" value="<%=mybean.branchtarget_accessories_amount%>"
													size="10" maxlength="9" />
											</div>
											
											<div class="form-element6">
												<label>Insurance Count:</label>
												<input name="txt_insurance_count" type="text" class="form-control" id="txt_insurance_count"
													onkeyup="isNumber(this)" value="<%=mybean.branchtarget_insurance_count%>" size="10"
													maxlength="9" />
											</div>
											
											<div class="form-element6">
												<label>EW Count:</label>
												<input name="txt_ew_count" type="text" class="form-control" id="txt_ew_count" onkeyup="isNumber(this)"
													value="<%=mybean.branchtarget_ew_count%>" size="10" maxlength="9" />
											</div>
											
											<div class="form-element6">
												<label>Fincases Count:</label>
												<input name="txt_fincases_count" type="text" class="form-control" id="txt_fincases_count"
													onkeyup="isNumber(this)" value="<%=mybean.branchtarget_fincases_count%>" size="10"
													maxlength="9" />
											</div>
											
											<div class="form-element6">
												<label>Exchange Count:</label>
												<input name="txt_exchange_count" type="text" class="form-control" id="txt_exchange_count"
													onkeyup="isNumber(this)" value="<%=mybean.branchtarget_exchange_count%>" size="10"
													maxlength="9" />
											</div>
											<div class="form-element6">
												<label>Evaluation Count:</label>
												<input name="txt_evaluation_count" type="text" class="form-control" id="txt_evaluation_count"
													onkeyup="isNumber(this)" value="<%=mybean.branchtarget_evaluation_count%>" size="10" maxlength="9" />
											</div>
											<div class="row"> </div>
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) {
											%>
										
											<div class="form-element6">
												<label>Entry By:&nbsp;</label>
												<span>
													<%=mybean.unescapehtml(mybean.entry_by)%>
													<input name="entry_by" type="hidden" id="entry_by" value="<%=mybean.entry_by%>" />
												</span>
											</div>

											<div class="form-element6">
												<label>Entry Date:&nbsp;</label>
												<span>
													<%=mybean.entry_date%>
													<input type="hidden" name="entry_date" value="<%=mybean.entry_date%>" />
												</span>
											</div>
											
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) {
											%>

											<div class="form-element6">
												<label>Modified By:&nbsp;</label>
												<span>
													<%=mybean.unescapehtml(mybean.modified_by)%>
													<input name="modified_by" type="hidden" id="modified_by"
														value="<%=mybean.modified_by%>"></input>
												</span>
											</div>

											<div class="form-element6">
												<label>Modified Date:&nbsp;</label>
												<span>
													<%=mybean.modified_date%>
													<input type="hidden" name="modified_date" value="<%=mybean.modified_date%>" />
												</span>
											</div>
												<%
													}
												%>
											<div class="form-element12">
												<center>
													<input name="update_button" type="submit" class="btn btn-success" id="" value="Update" />
												</center>
											</div>
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
	
	<script type="text/javascript">
		function isNumber(ob) {
			var invalidChar = /[^0-9]/gi
			if (invalidChar.test(ob.value)) {
				ob.value = ob.value.replace(invalidChar, "");
			}
		}
	</script>
</body>
</HTML>
