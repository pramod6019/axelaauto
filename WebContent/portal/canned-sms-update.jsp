<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Canned_SMS_Update"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<%@include file="../Library/css.jsp"%>
<link href="../assets/css/bootstrap-wysihtml5.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/summernote.css" rel="stylesheet"
	type="text/css" />
<!-- <script type="text/javascript" src="../Library/Validate.js"></script> -->
<!-- <script type="text/javascript" -->
<%-- 	src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script> --%>
<!-- <script type="text/javascript" src="../Library/jquery.js"></script> -->
<!-- <script type="text/javascript" src="../Library/jquery-ui.js"></script> -->


<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		document.form1.txt_branch_name.focus();
	}
	/* 
	 function selectpreowned() {
	 var temp = document.getElementById('dr_branch_branchtype_id').value;
	 if (temp == 1) {
	 $("#preowned_div").show();
	 } else {
	 $("#preowned_div").hide();
	 }
	 }
	 */
</script>
<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />

<link href="../assets/css/style.css" rel="stylesheet" type="text/css" />
</head>
<body onLoad="selectpreowned();FormFocus();"
	class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="header.jsp"%>
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1><%=mybean.status%>&nbsp;Canned SMS
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
						<ul class="page-breadcrumb breadcrumb">
							<li><a href="../portal/home.jsp">Home</a> &gt;</li>
							<li><a href="manager.jsp">Business Manager</a> &gt;</li>
							<li><a href="canned-sms-list.jsp?all=yes">List Canned
									SMS</a> &gt;</li>
							<li><a href="canned-sms-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Canned
									SMS</a><b>:</b></li>
						</ul>
						<!-- END PAGE BREADCRUMBS -->
						<div class="tab-pane" id="">
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%>
										Canned SMS
									</div>
								</div>
							<div class="portlet-body portlet-empty">
								<div class="container-fluid">
										<div class="tab-pane" id="">
											<form name="form1" method="post" class="form-horizontal ">
												<center>
													<font size="1">Form fields marked with a red asterisk <b><font
															color="#ff0000">*</font></b> are required.</font> 
												</center>
												<br />
												<!-- START PORTLET BODY -->
												<div class="form-element6">
													<label> Brand<font color="#ff0000">*</font>:&nbsp;
													</label> <select name="cannedsms_brand_id" class="form-control"
														<%if (mybean.status.equals("Update")) {%> disabled <%}%>
														id="cannedsms_brand_id">
														<%=mybean.PopulateBrand(mybean.comp_id)%>
													</select> <input hidden id="cannedsms_brand_id"
														name="cannedsms_brand_id"
														value="<%=mybean.cannedsms_brand_id%>" />
												</div>
												<div class="form-element6">
													<label> Branch Type<font color="#ff0000">*</font>:&nbsp;
													</label> <select name="cannedsms_branchtype_id"
														class="form-control" id="cannedsms_branchtype_id">
														<%=mybean.PopulateBranchType(mybean.comp_id)%>
													</select>
												</div>

												<div class="form-element6">
													<label>Name <font color="#ff0000">*</font>:&nbsp;
													</label> <input name="cannedsms_name" type="text"
														class="form-control" value="<%=mybean.cannedsms_name%>"
														size="50" maxlength="255" />
												</div>
												<div class="row"></div>
												<div class="form-element6">
													<label> Format:&nbsp; <font color=red></font>
													</label>
													<textarea name="cannedsms_format" cols="40" rows="10"
														class="form-control" id="cannedsms_format"
														maxlength="1000"><%=mybean.cannedsms_format%></textarea>
													<br> <span id="span_sms_msg"></span>
												</div>
												<div class="form-element6" style="margin-top: 20px;">
													<%
														if (mybean.status.equals("Update")
																&& !(mybean.cannedsms_entry_date == null)
																&& !(mybean.cannedsms_entry_date.equals(""))) {
													%>
													
													<table class="table table-hover table-bordered">
														<thead>
															<tr>
																<th colspan="2" align="center">Substitution
																	Variables</th>
															</tr>
														</thead>

														<%
															if (mybean.cannedsms_branchtype_id.equals("1")
																		|| mybean.cannedsms_branchtype_id.equals("2")) {
														%>

														<tr>
															<td align="right">Enquiry ID:</td>
															<td align="left">[ENQUIRYID]</td>
														</tr>
														<tr>
															<td align="right">Enquiry Name:</td>
															<td align="left">[ENQUIRYNAME]</td>
														</tr>
														<tr>
															<td align="right">Customer ID:</td>
															<td align="left">[CUSTOMERID]</td>
														</tr>
														<tr>
															<td align="right">Customer Name:</td>
															<td align="left">[CUSTOMERNAME]</td>
														</tr>
														<tr>
															<td align="right">Contact Name:</td>
															<td align="left">[CONTACTNAME]</td>
														</tr>
														<tr>
															<td align="right">Contact Job Title:</td>
															<td align="left">[CONTACTJOBTITLE]</td>
														</tr>
														<tr>
															<td align="right">Contact Mobile1:</td>
															<td align="left">[CONTACTMOBILE1]</td>
														</tr>
														<tr>
															<td align="right">Contact Phone1:</td>
															<td align="left">[CONTACTPHONE1]</td>
														</tr>
														<tr>
															<td align="right">Contact Email1:</td>
															<td align="left">[CONTACTEMAIL1]</td>
														</tr>
														<tr>
															<td align="right">Executive Name:</td>
															<td align="left">[EXENAME]</td>
														</tr>
														<tr>
															<td align="right">Executive Job Title:</td>
															<td align="left">[EXEJOBTITLE]</td>
														</tr>
														<tr>
															<td align="right">Executive Mobile1:</td>
															<td align="left">[EXEMOBILE1]</td>
														</tr>
														<tr>
															<td align="right">Executive Phone1:</td>
															<td align="left">[EXEPHONE1]</td>
														</tr>
														<tr>
															<td align="right">Executive Email1:</td>
															<td align="left">[EXEEMAIL1]</td>
														</tr>
														<tr>
															<td align="right">CRM Executive Name:</td>
															<td align="left">[CRMEXENAME]</td>
														</tr>
														<tr>
															<td align="right">CRM Executive Job Title:</td>
															<td align="left">[CRMEXEJOBTITLE]</td>
														</tr>
														<tr>
															<td align="right">CRM Executive Mobile1:</td>
															<td align="left">[CRMEXEMOBILE1]</td>
														</tr>
														<tr>
															<td align="right">CRM Executive Phone1:</td>
															<td align="left">[CRMEXEPHONE1]</td>
														</tr>
														<tr>
															<td align="right">CRM Executive Email1:</td>
															<td align="left">[CRMEXEEMAIL1]</td>
														</tr>
														<tr>
															<td align="right">Branch Name:</td>
															<td align="left">[BRANCHNAME]</td>
														</tr>

														<tr>
															<td align="right">Branch Email1:</td>
															<td align="left">[BRANCHEMAIL1]</td>
														</tr>

													<%
														}
													%>

													<%
														if (mybean.cannedsms_branchtype_id.equals("3")) {
													%>
													<tr>
														<td align="right">Veh ID:</td>
														<td align="left">[VEHID]</td>
													</tr>
													<tr>
														<td align="right">Job Card ID:</td>
														<td align="left">[JOBCARDID]</td>
													</tr>
													<tr>
														<td align="right">Customer ID:</td>
														<td align="left">[CUSTOMERID]</td>
													</tr>
													<tr>
														<td align="right">Customer Name:</td>
														<td align="left">[CUSTOMERNAME]</td>
													</tr>
													<tr>
														<td align="right">Contact Name:</td>
														<td align="left">[CONTACTNAME]</td>
													</tr>
													<tr>
														<td align="right">Contact Job Title:</td>
														<td align="left">[CONTACTJOBTITLE]</td>
													</tr>
													<tr>
														<td align="right">Contact Mobile1:</td>
														<td align="left">[CONTACTMOBILE1]</td>
													</tr>
													<tr>
														<td align="right">Contact Phone1:</td>
														<td align="left">[CONTACTPHONE1]</td>
													</tr>
													<tr>
														<td align="right">Contact Email1:</td>
														<td align="left">[CONTACTEMAIL1]</td>
													</tr>
													<tr>
														<td align="right">Executive Name:</td>
														<td align="left">[EXENAME]</td>
													</tr>
													<tr>
														<td align="right">Executive Job Title:</td>
														<td align="left">[EXEJOBTITLE]</td>
													</tr>
													<tr>
														<td align="right">Executive Mobile1:</td>
														<td align="left">[EXEMOBILE1]</td>
													</tr>
													<tr>
														<td align="right">Executive Phone1:</td>
														<td align="left">[EXEPHONE1]</td>
													</tr>
													<tr>
														<td align="right">Executive Email1:</td>
														<td align="left">[EXEEMAIL1]</td>
													</tr>
													<tr>
														<td align="right">Item Name:</td>
														<td align="left">[ITEMNAME]</td>
													</tr>
													<tr>
														<td align="right">Model Name:</td>
														<td align="left">[MODELNAME]</td>
													</tr>
													<tr>
														<td align="right">Reg. No.:</td>
														<td align="left">[REGNO]</td>
													</tr>
													<tr>
														<td align="right">Service Due Date:</td>
														<td align="left">[SERVICEDUEDATE]</td>
													</tr>
													<!-- 												<tr> -->
													<!-- 													<td align="right">Service Type:</td> -->
													<!-- 													<td align="left">[SERVICETYPE]</td> -->
													<!-- 												</tr> -->

													
													
													<% } %>
													</table>
													<% } %>
												</div>	
												<div class="row"></div>
												<div class="form-element6">
													<label>Active:&nbsp;</label> <input id="cannedsms_active"
														type="checkbox" name="cannedsms_active"
														<%=mybean.PopulateCheck(mybean.cannedsms_active)%> />
												</div>
												
												<div class="row"></div>
												<%
													if (mybean.status.equals("Update")
															&& !(mybean.cannedsms_entry_date == null)
															&& !(mybean.cannedsms_entry_date.equals(""))) {
												%>
												<div class="form-element6">
													<label>Entry By:&nbsp;</label>
													<%=mybean.unescapehtml(mybean.cannedsms_entry_by)%>
													<input name="entry_by" type="hidden" id="entry_by"
														value="<%=mybean.unescapehtml(mybean.cannedsms_entry_by)%>" />
												</div>
												<div class="form-element6">
													<label>Entry Date:&nbsp;</label>
													<%=mybean.cannedsms_entry_date%>
													<input type="hidden" id="entry_date" name="entry_date"
														value="<%=mybean.cannedsms_entry_date%>" />
												</div>
												<% } %>
												<%
													if (mybean.status.equals("Update")
															&& !(mybean.cannedsms_modified_date == null)
															&& !(mybean.cannedsms_modified_date.equals(""))) {
												%>
												<div class="form-element6">
													<label>Modified By:&nbsp;</label>
													<%=mybean.unescapehtml(mybean.cannedsms_modified_by)%>
													<input type="hidden" id="modified_by" name="modified_by"
														value="<%=mybean.unescapehtml(mybean.cannedsms_modified_by)%>" />
												</div>
												<div class="form-element6">
													<label>Modified Date:&nbsp;</label>
													<%=mybean.cannedsms_modified_date%><input type="hidden"
														id="modified_date" name="modified_date"
														value="<%=mybean.cannedsms_modified_date%>" />
												</div>
												<%
													}
												%>
												<div class="row"></div>
												<center>
													<%
														if (mybean.status.equals("Add")) {
													%>
													<input name="button" type="submit" class="btn btn-success"
														id="button" value="Add Canned SMS"
														onClick="return SubmitFormOnce(document.form1, this);" />
													<input type="hidden" name="add_button" value="yes">
														<%
															} else if (mybean.status.equals("Update")) {
														%> <input type="hidden" name="update_button" value="yes">
															<input name="button" type="submit"
															class="btn btn-success" id="button"
															value="Update Canned SMS"
															onClick="return SubmitFormOnce(document.form1, this);" />
															<input name="delete_button" type="submit"
															class="btn btn-success" id="delete_button"
															onClick="return confirmdelete(this)"
															value="Delete Canned SMS" /> <%
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
		</div>
		<%@include file="../Library/js.jsp"%>
	<script src="../assets/js/summernote.min.js" type="text/javascript"></script>
	<script src="../assets/js/components-editors.min.js"></script>
	<%@include file="../Library/admin-footer.jsp"%>
</body>
</HTML>
