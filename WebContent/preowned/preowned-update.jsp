<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.preowned.Preowned_Update"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<jsp:setProperty name="mybean" property="*" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp"%>
</HEAD>

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
						<h1>
							Update
							<%=mybean.ReturnPreOwnedName(request)%></h1>
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
						<li><a href="../portal/home.jsp?all=yes">Home</a> &gt;</li>
						<li><a href="index.jsp"><%=mybean.ReturnPreOwnedName(request)%></a>
							&gt;</li>
						<!-- 						<li><a href="preowned.jsp?all=yes">Pre Owned</a>&gt;</li> -->
						<li><a href="preowned-list.jsp?all=yes">List <%=mybean.ReturnPreOwnedName(request)%></a>&gt;</li>
						<li><a href="preowned-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>
								<%=mybean.ReturnPreOwnedName(request)%></a>:</li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->

							<form name="form1" method="post" class="form-horizontal">
								<center>
									<font color="#ff0000"><b><%=mybean.msg%></b></font>
								</center>
								<%-- 								<% --%>
								<!-- // 									if (mybean.preowned_contact_id.equals("0")) { -->
								<%-- 								%> --%>
								<div class="portlet box">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">
											Update
											<%=mybean.ReturnPreOwnedName(request)%></div>
									</div>
									<div class="portlet-body portlet-empty container-fluid">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<center>
												<font size="1">Form fields marked with a red asterisk <b><font
														color="#ff0000">*</font></b> are required.
												</font>
											</center>

											<div class="form-element6">
												<label > Customer: </label>
												
													<b><span id="span_bill_customer_id"
														name="span_bill_customer_id"> <%
 			if (!mybean.preowned_customer_id.equals("0") || !mybean.preowned_customer_id.equals("0")) {
 									%> <%=mybean.link_customer_name%> <%
 																	}
 																	%>
													</span></b>&nbsp; <input name="span_acct_id" type="hidden"
														id="span_acct_id" value="<%=mybean.customer_id%>">
													<input name="acct_id" type="hidden" id="acct_id"
														value="<%=mybean.preowned_customer_id%>"/>
												</div>
												<div class="form-element6">
												<label > Contact: </label>
												
													<b><span id="span_bill_contact_id"
														name="span_bill_contact_id"> <%
 				if (!mybean.preowned_contact_id.equals("0") || !mybean.preowned_contact_id.equals("0")) {
 														%> <%=mybean.link_contact_name%> <%
													 								}
 																						%>
													</span></b>&nbsp;
													<%
														//if(mybean.status.equals("Update")) {
													%>
<!-- 													<a href="#" id="dialog_link">(Select Contact)</a> -->
													<%
														//}
													%>
													<input name="span_cont_id" type="hidden" id="span_cont_id"
														value="<%=mybean.contact_id%>"> <input
														name="cont_id" type="hidden" id="cont_id"
														value="<%=mybean.preowned_contact_id%>">
													<div id="dialog-modal"></div>
												
											</div>

											<div class="form-element6">
												<label > Date:</label>
												
													<%
														//if (mybean.emp_opportunity_edit.equals("1") && !mybean.lead_id.equals("0")) {
													%>
													<input name="txt_preowned_date" type="text"
														class="form-control datepicker"
														 id="txt_enquiry_date"
														value="<%=mybean.preowneddate%>" size="12" maxlength="10">
													<%
														// } else {
													%>
													<%
														//=mybean.DisplayDate()
													%>
													<%
														//}
													%>

												</div>
												<div class="form-element6">
												<label >Closing Date:</label>
													<input name="txt_preowned_close_date" type="text"
														class="form-control datepicker"
														id="txt_preowned_close_date"
														value="<%=mybean.closedate%>" size="12" maxlength="10">
												</div>
											

											<div class="form-element6">
												<label >Pre-Owned
													Title<font color="#ff0000">*</font>:
												</label>
													<input name="txt_preowned_title" type="text"
														class="form-control" id="txt_preowned_title"
														value="<%=mybean.preowned_title%>" size="32"
														maxlength="255" />
											</div>	
											<div class="form-element6">		
												<label >Branch<font
													color="#ff0000">*</font>:
												</label>
													<select name="dr_branch" id="dr_branch"
														class="form-control"
														onChange="PopulateCampaign();PopulatePreExecutive();PopulatePreTeam();">
														<%=mybean.PopulateBranch(mybean.preowned_branch_id, "all", "2", "", request)%>
													</select>
											</div>

											<div class="form-element6">
												<label >Description:</label>
												<div >
													<textarea name="txt_preowned_desc" cols="70" rows="4"
														class="form-control" id="txt_preowned_desc"
														onKeyUp="charcount('txt_preowned_desc', 'span_txt_preowned_desc','<font color=red>({CHAR} characters left)</font>', '8000')"><%=mybean.preowned_desc%></textarea>
													<span id="span_txt_preowned_desc"> (8000
														Characters)</span>
												</div>
												
											</div>
											<div class="form-element6">
												<label >Model:</label>
												
													<!-- 													<input tabindex="-1" class="form-control" -->
													<!-- 														id="modelvariant" name="modelvariant" style="width: 300px" -->
													<%-- 														value="<%=mybean.preowned_variant_id%>" type="hidden" /> --%>
													<select class="form-control select2" id="preownedvariant"
														name="preownedvariant">
														<%=mybean.modelcheck.PopulateVariant(mybean.preowned_variant_id)%>
													</select>
											</div>
										</div>
									</div>
									</div>
									<%-- 									<% --%>
									<!-- // 										} -->
									<%-- 									%> --%>
									<div class="portlet box">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none"><%=mybean.ReturnPreOwnedName(request)%>
											Status
										</div>
									</div>
									
									<div class="portlet-body portlet-empty container-fluid">
										<div class="tab-pane" id="">
											<!-- 											START PORTLET BODY -->
											
											<div class="form-element6">
												<label >Status<font
													color="#ff0000">*</font>:
												</label>
											
													<select name="dr_preowned_preownedstatus_id"
														class="form-control" id="dr_preowned_preownedstatus_id">
														<%=mybean.PopulateStatus()%>
													</select>
												
											</div>
												<div class="form-element6">
												<label >Priority<font
													color="#ff0000">*</font>:
												</label>
												
													<select name="dr_preowned_prioritypreowned_id"
														class="form-control" id="dr_preowned_prioritypreowned_id">
														<%=mybean.PopulatePreownedPriority()%>
													</select>
												
											</div>
											
											<div class="form-element6">
												<label >Status
													Comments<font color="#ff0000">*</font>:
												</label>
												
													<textarea name="txt_preowned_preownedstatus_desc" cols="50"
														rows="4" class="form-control"
														id="txt_preowned_preownedstatus_desc"
														onKeyUp="charcount('txt_preowned_preownedstatus_desc', 'span_txt_preowned_preownedstatus_desc','<font color=red>({CHAR} characters left)</font>', '1000')"><%=mybean.preowned_preownedstatus_desc%></textarea>
													 <span id="span_txt_preowned_preownedstatus_desc">
														(1000 Characters)</span> <br>
												
												</div>

											<div class="form-element6">
												<label >Sales Team: </label>
												<div >
													<select name="dr_sales_team" id="dr_sales_team"
														class="form-control" onchange="PopulateSalesExecutive();">
														<%=mybean.PopulateSalesTeam(mybean.sales_team_id, mybean.comp_id)%></select>
												</div>
												
												<label >Pre-Owned
													Team<font color="#ff0000">*</font>:
												</label>
												<div  id="preownedteam">
														<%=mybean.PopulatePreownedTeam(mybean.preowned_branch_id, mybean.preowned_team_id, mybean.comp_id)%>
													
												</div>
											</div>
<div class="row"></div>
											<div class="form-element6">
												<label >Sales Consultant: </label>
												<div id="salesteamexe">
													<%=mybean.PopulateSalesExecutives(mybean.preowned_sales_emp_id, "0", mybean.comp_id)%>
												</div>
											
												</div>
											<div class="form-element6">
												<label >Pre-Owned Consultant<font color="#ff0000">*</font>: </label>
												<div id="teamexe">
													<%=mybean.PopulatePreownedExecutives(mybean.preowned_branch_id, mybean.preowned_team_id, mybean.preowned_emp_id, mybean.comp_id, request)%> 
												</div>
												
												
											</div>

											<%
												if (mybean.config_preowned_soe.equals("1")) {
											%>
											<div class="form-element6">
												<label >Source of Enquiry<font color="#ff0000">*</font>:
												</label>
											
													<select name="dr_preowned_soe_id" id="dr_preowned_soe_id"
														class="form-control">
														<%=mybean.PopulateSoe()%>
													</select>

												
											</div>
											<%
												}
											%>

											<%
												if (mybean.config_preowned_sob.equals("1")) {
											%>
											<div class="form-element6">
												<label >Source of
													Business<font color="#ff0000">*</font>:
												</label>
													<select name="dr_preowned_soe_id" id="dr_preowned_soe_id"
														class="form-control">
														<%=mybean.PopulateSob()%>
													</select>
											</div>
											<%
												}
											%>


											<%
												if (mybean.config_preowned_campaign.equals("1")) {
											%>
											<div class="form-element6">
												<label >Campaign<font
													color="#ff0000">*</font>:
												</label>
												
													<span id="campaign"> <select
														name="dr_preowned_campaign_id"
														id="dr_preowned_campaign_id" class="form-control">
															<%=mybean.PopulateCampaign(mybean.preowned_date)%>
													</select>
													</span>
													<div class="admin-master">
														<a href="../sales/campaign-list.jsp?all=yes"
															title="Manage Campaign"></a>
													</div>
												
											</div>
											<%
												}
											%>


											<%
												if (mybean.config_preowned_refno.equals("1")) {
											%>
											<div class="form-element6">
												<label >Ref. No.<font
													color="#ff0000">*</font>:
												</label>
												
													<input name="txt_preowned_refno" type="text"
														class="form-control" id="txt_preowned_refno"
														value="<%=mybean.preowned_refno%>" size="32"
														maxlength="50" />
												
											</div>
											<%
												}
											%>

											<div class="form-element6">
												<label >Notes: </label>
													<textarea name="txt_preowned_notes" id="txt_preowned_notes"
														cols="70" rows="4" class="form-control"><%=mybean.preowned_notes%></textarea>
											</div>
											
											<div class="row"></div>
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) {
											%>
												<div class="form-element6 form-element-margin">
													<label > Entry By: </label>
													<%=mybean.unescapehtml(mybean.entry_by)%>
													<input name="entry_by" type="hidden" id="entry_by"
															value="<%=mybean.unescapehtml(mybean.entry_by)%>">
												</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) {
											%>
												<div class="form-element6 form-element-margin">
													<label > Entry Date: </label>
													<%=mybean.entry_date%>
													<input type="hidden" name="entry_date" value="<%=mybean.entry_date%>">
												</div>	
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) {
											%>
												<div class="form-element6 form-element-margin">
													<label > Modified By: </label>
													<%=mybean.unescapehtml(mybean.modified_by)%>
													<input name="modified_by" type="hidden" id="modified_by"
															value="<%=mybean.unescapehtml(mybean.modified_by)%>">
												</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) {
											%>
												<div class="form-element6 form-element-margin">
													<label > Modified Date: </label>
														<%=mybean.modified_date%>
													<input type="hidden" name="modified_date" value="<%=mybean.modified_date%>">
												</div>
											<%
												}
											%>
											<div class="form-element12">
																						<center>
												<%
													if (mybean.status.equals("Add")) {
												%>
												<input name="addbutton" type="submit"
													class="btn btn-success" id="addbutton"
													value="Add <%=mybean.ReturnPreOwnedName(request)%>"
													onClick="return SubmitFormOnce(document.form1, this);" />
												<input type="hidden" name="add_button" value="yes">
												<%
													} else if (mybean.status.equals("Update")) {
												%>

												<input type="hidden" name="update_button" value="yes">
												<input name="updatebutton" type="submit"
													class="btn btn-success" id="updatebutton"
													value="Update <%=mybean.ReturnPreOwnedName(request)%>"
													onClick="return SubmitFormOnce(document.form1, this);" />

												<input name="delete_button" type="submit"
													class="btn btn-success" id="delete_button"
													OnClick="return confirmdelete(this)"
													value="Delete <%=mybean.ReturnPreOwnedName(request)%>" />


												<%-- <input type="hidden" name="update_button" value="yes"/>
													<input name="updatebutton" type="submit"
													class="btn btn-success" id="updatebutton"
													value="Update <%=mybean.ReturnPreOwnedName(request)%>"
													onClick="return SubmitFormOnce(document.form1, this);" /> 
													<input type="hidden" name="delete_button" value="Delete Pre Owned">
													<input
													name="deletebutton" type="submit" class="btn btn-success"
													id="deletebutton" OnClick="return confirmdelete(this)"
													value="Delete <%=mybean.ReturnPreOwnedName(request)%>" /> --%>
												<%
													}
												%>
											</center>
											</div>
											</div>
											</div>
										</div>
									
								</div>
							</form>


						</div>
					</div>
				</div>
		</div>

	</div>
	<!-- END CONTAINER -->
	<%@ include file="../Library/admin-footer.jsp"%>
	<%@ include file="../Library/js.jsp" %>

<script>
	function selectvariant() {
		var temp2 = document.getElementById('select2-modelvariant-container').innerHTML;
		$("#txt_modelvariant").val(temp2);
		// 	alert(temp2);
	}
</script>
<script type="text/javascript">
// 	$(function() {
// 		// Dialog
// 		$('#dialog-modal').dialog({
// 			autoOpen : false,
// 			width : 900,
// 			height : 500,
// 			zIndex : 200,
// 			modal : true,
// 			title : "Select Contact"
// 		});
// 		$('#dialog_link')
// 				.click(
// 						function() {

// 							$
// 									.ajax({
// 										//url: "home.jsp",
// 										success : function(data) {
// 											$('#dialog-modal')
// 													.html(
// 															'<iframe src="../customer/customer-contact-list.jsp?group=selectcontact" width="100%" height="100%" frameborder=0></iframe>');
// 										}
// 									});
// 							$('#dialog-modal').dialog('open');
// 							return true;
// 						});
// 	});
</script>
<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		document.form1.modelvariant.focus();
	}
	function frmSubmit() {
		document.formcontact.submit();
	}
	
	 function PopulateSalesExecutive(){
		var team_id = document.getElementById('dr_sales_team').value;
		showHint('../preowned/preowned-check.jsp?salesexecutive=yes&team_id=' + team_id,'salesteamexe');
	  } 
	
	function PopulatePreTeam() {
		var branch_id = document.getElementById('dr_branch').value;
		if (branch_id != "" && branch_id != "0") {
			showHint('../preowned/preowned-check.jsp?preownedteam=yes&preowned_branch_id=' + branch_id, 'preownedteam');
		}
		
	}
	
	function PopulatePreExecutive(){
		var preowned_branch_id = document.getElementById('dr_branch').value;
		var team_id = document.getElementById('dr_preowned_team').value;
		showHint('../preowned/preowned-check.jsp?preexecutive=yes&team_id=' + team_id+'&preowned_branch_id='+preowned_branch_id,'teamexe');
	  }
	
	function PopulateCampaign() {
		var branch_id = document.getElementById('dr_branch').value;
		var enquiry_date = document.getElementById('txt_enquiry_date').value;
		if (branch_id != "" && branch_id != "0") {
			showHint('../sales/campaign-check.jsp?enquiry_branch_id=' + branch_id + '&enquiry_date=' + enquiry_date, 'campaign');
		}
	}

	function CampaignCheck(name, obj, hint) {
		var date = document.getElementById('txt_preowned_date').value;
		var branch_id = document.getElementById('dr_branch').value;
		var url = "../sales/preowned-check.jsp?";
		var param = "name=" + name + "&value=" + date + "&branch_id=" + branch_id;
		var str = "123";
		showHintPost(url + param, str, param, hint);
		setTimeout('RefreshHistory()', 1000);
	}
</script>
	
</body>
</HTML>
