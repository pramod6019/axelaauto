<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.preowned.Target_Update" scope="request"/>
<%mybean.doPost(request,response); %>
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
						<h1>Target Update</h1>
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
						<li><a href="../preowned/index.jsp">Pre-Owned</a> &gt;</li>
						<li><a href="target.jsp">Target</a> &gt;</li>
						<li><a href="target-list.jsp?dr_executives=<%=mybean.preownedtarget_exe_id%>&dr_year=<%=mybean.year%>"> <%=mybean.month_name%>-<%=mybean.year%></a> &gt;</li> 
						<li><a href="target-list.jsp?dr_executives=<%=mybean.preownedtarget_exe_id%>&dr_year=<%=mybean.year%>">List Targets</a> &gt;</li>
						<li><a href="../portal/executive-list.jsp?emp_id=<%=mybean.preownedtarget_exe_id%>"><%=mybean.preownedtarget_exe_name%></a> &gt;</li>
						<li><a href="target-update.jsp?<%=mybean.QueryString%>">Update Target</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="#ff0000"><b><%=mybean.msg%></b></font>
					</center>
			
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<center>Target Update</center>
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
											<br></br>
											
											<div class="form-element6">
												<label>Pre-Owned Count<font color="#ff0000">*</font>: </label>
												<input name="txt_preownedtarget_enquiry_count" type="text" class="form-control"
													id="txt_preownedtarget_enquiry_count" onkeyup="isNumber(this)"
													value="<%=mybean.preownedtarget_enquiry_count%>" size="10" maxlength="10" />
											</div>

											<div class="form-element6">
												<label>Evaluation Count<font color="#ff0000">*</font>: </label>
												<input name="txt_preownedtarget_eval_count" type="text" onkeyup="isNumber(this)"
													class="form-control" id="txt_preownedtarget_eval_count"
													value="<%=mybean.preownedtarget_enquiry_eval_count%>" size="10" maxlength="10" />
											</div>
											
											<div class="form-element6">
												<label>Purchase Count<font color="#ff0000">*</font>: </label>
												<input name="txt_preownedtarget_purchase_count" type="text" onkeyup="isNumber(this)"
													class="form-control" id="txt_preownedtarget_purchase_count"
													value="<%=mybean.preownedtarget_purchase_count%>" size="10" maxlength="10" />
											</div>

											<%
												if (mybean.status.equals("Update") && !(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) {
											%>
											<div class="row"></div>
											<div class="form-element6">
												<label>Entry By:&nbsp;</label>
												<%=mybean.unescapehtml(mybean.entry_by)%>
												<input name="entry_by" type="hidden" id="entry_by" value="<%=mybean.entry_by%>" />
											</div>

											<div class="form-element6">
												<label>Entry Date:&nbsp;</label>
												<%=mybean.entry_date%>
												<input type="hidden" name="entry_date" value="<%=mybean.entry_date%>" />
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) {
											%>

											<div class="form-element6">
												<label>Modified By:&nbsp;</label>
												<%=mybean.unescapehtml(mybean.modified_by)%>
												<input name="modified_by" type="hidden" id="modified_by" value="<%=mybean.modified_by%>"></input>
											</div>

											<div class="form-element6">
												<label>Modified Date:&nbsp;</label>
												<%=mybean.modified_date%>
												<input type="hidden" name="modified_date" value="<%=mybean.modified_date%>" />
												<%
													}
												%>
											</div>

											<div class="form-element12">
												<center>
													<input name="update_button" type="submit"
														class="btn btn-success" id="" value="Update" />
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

	// 	function toDecimal(ob) {
	// 		var invalidChar = /[^0-9]/gi;
	// 		if (invalidChar.test(ob.value)) {
	// 			ob.value = ob.value.replace(invalidChar, "");
	// 		}
	// 	}
</script>

</body>
</HTML>
