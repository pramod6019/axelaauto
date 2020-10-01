
<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.accounting.ManageTax_Update" scope="request" />
<% mybean.doPost(request, response); %>
<jsp:setProperty name="mybean" property="*" />
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp"%>
</head>
<body onLoad="FormFocus();"
	class="page-container-bg-solid page-header-menu-fixed">
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
							Tax
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
							<li><a href="../accounting/index.jsp">Accounting</a> &gt;</li>
							<li><a href="managetax.jsp?all=yes"> List Taxes</a> &gt;</li>
							<li><a href="managetax-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>
									Tax</a><b>:</b></li>

						</ul>
						<!-- END PAGE BREADCRUMBS -->
						<center>
							<font color="#ff0000"><b> <%=mybean.msg%>
							</b></font>
						</center>
						<!-- 					BODY START -->
						<div class="portlet box  ">
							<div class="portlet-title" style="text-align: center">
								<div class="caption" style="float: none">
									<%=mybean.status%>
									Tax
								</div>
							</div>
							<div class="portlet-body portlet-empty">
								<div class="tab-pane" id="">
									<!-- START PORTLET BODY -->
									<form name="form1" class="form-horizontal" method="post">
										<center>
											<font size="1">Form fields marked with a red asterisk
												<font color=#ff0000>*</font> are required.
											</font>
										</center>
										<br>
										<div class="form-element6">
											<label>Tax Name<font color="#ff0000">*</font>:
											</label> <input name="txt_customer_name" id="txt_customer_name"
												type="text" class="form-control"
												value="<%=mybean.customer_name%>" maxlength="255" size="38">
										</div>
										<div class="form-element6">
											<label>Tax Rate<font color="#ff0000">*</font>:
											</label> <input name="txt_customer_rate" id="txt_customer_rate"
												type="text" class="form-control"
												value="<%=mybean.customer_rate%>" maxlength="10" size="12">
										</div>
										<div class="form-element6">
											<label>Category<font color="#ff0000">*</font>:
											</label>
											<select id="dr_customer_taxcat_id" name="dr_customer_taxcat_id" class="form-control">
												<%=mybean.PopulateTaxCategory()%>
											</select>
										</div>
										<div class="form-element6">
											<label>Rounding Method<font color="#ff0000">*</font>:
											</label> <select id="dr_customer_round_id"
												name="dr_customer_round_id" class="form-control">
												<%=mybean.PopulateRounding()%>
											</select>
										</div>
										<div class="form-element6">
											<label>Type<font color="#ff0000">*</font>:
											</label> <select id="dr_customer_taxtype_id"
												name="dr_customer_taxtype_id" class="form-control"
												onchange="populategroup();">
												<%=mybean.PopulateTaxType()%>
											</select>
										</div>
										<div class="form-element6">
											<label>Ledger Group<font color="#ff0000">*</font>:
											</label> <select id="dr_accgroup_id" name="dr_accgroup_id"
												class="form-control">
												<%=mybean.PopulateGroup(mybean.comp_id, mybean.accgroup_id)%>
											</select>
										</div>
										<div class="row">
											<div class="form-element2 form-element-margin">
												<label>Active:</label> <input type="checkbox"
													name="chk_customer_active" id="chk_customer_active"
													<%=mybean.PopulateCheck(mybean.customer_active)%>>
											</div>
										</div>
										<%
											if (mybean.status.equals("Update") && !(mybean.entry_by == null)
													&& !(mybean.entry_by.equals(""))) {
										%>
										<div class="form-element12">
											<div class="form-element6">
												<label>Entry By : </label>
												<%=mybean.unescapehtml(mybean.entry_by)%>
												<input name="entry_by" type="hidden" id="entry_by"
													value="<%=mybean.unescapehtml(mybean.entry_by)%>">
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_date == null)
														&& !(mybean.entry_date.equals(""))) {
											%>
											<div class="form-element6">
												<label>Entry Date : </label>
												<%=mybean.entry_date%>
												<input type="hidden" name="entry_date"
													value="<%=mybean.entry_date%>">
											</div>
										</div>
										<%
											}
										%>
										<%
											if (mybean.status.equals("Update") && !(mybean.modified_by == null)
													&& !(mybean.modified_by.equals(""))) {
										%>
										<div class="form-element12">
											<div class="form-element6">
												<label>Modified By : </label>
												<%=mybean.unescapehtml(mybean.modified_by)%>
												<input name="modified_by" type="hidden" id="modified_by"
													value="<%=mybean.unescapehtml(mybean.modified_by)%>">
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update")
														&& !(mybean.modified_date == null)
														&& !(mybean.modified_date.equals(""))) {
											%>
											<div class="form-element6">
												<label>Modified date : </label>
												<%=mybean.modified_date%>
												<input type="hidden" name="modified_date"
													value="<%=mybean.modified_date%>">
											</div>
										</div>
										<div class="row"></div>
										<%
											}
										%>
										<%
											if (mybean.status.equals("Add")) {
										%>
										<center>
											<input name="addbutton" type="submit" class="btn btn-success"
												id="addbutton" value="Add Tax"
												onClick="return SubmitFormOnce(document.form1, this);" /> <input
												type="hidden" id="add_button" name="add_button" value="yes" />
										</center>
										<%
											} else if (mybean.status.equals("Update")) {
										%>
										<center>
											<input type="hidden" id="update_button" name="update_button"
												value="yes" /> <input name="updatebutton" type="submit"
												class="btn btn-success" id="updatebutton" value="Update Tax"
												onClick="return SubmitFormOnce(document.form1, this);" /> <input
												name="delete_button" type="submit" class="btn btn-success"
												id="delete_button" OnClick="return confirmdelete(this)"
												value="Delete Tax" />
										</center>
										<%
											}
										%>
										<input type="hidden" name="customer_id"
											value="<%=mybean.customer_id%>">
									</form>
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
		}
		function populategroup() {
			var taxtype_id = document.getElementById("dr_customer_taxtype_id").value;
			showHint('../accounting/managetax-check.jsp?group=yes&taxtype_id='
					+ taxtype_id, 'dr_accgroup_id');
		}
	</script>
</body>
</HTML>
