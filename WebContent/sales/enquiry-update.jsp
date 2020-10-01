<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Enquiry_Update" scope="request" />
<jsp:useBean id="mybeanquickadd" class="axela.sales.Enquiry_Quickadd" scope="request" />
<%
	mybean.doPost(request, response);
%>
<jsp:setProperty name="mybean" property="*" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<%@include file="../Library/css.jsp"%>

</HEAD>
<body onLoad="FormFocus();" class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>
							<%=mybean.status%> Enquiry
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
						<li><a href="enquiry.jsp">Enquiry</a> &gt;</li>
						<li><a href="enquiry-list.jsp?all=yes"> List Enquiry</a> &gt;</li>
						<li><a href="enquiry-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%> Enquiry</a><b>:</b></li> 
					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<input type="hidden" id="txt_config_campaign" name="txt_config_campaign" value="<%=mybean.config_sales_campaign%>">
							<!-- 					BODY START -->
							<form name="form1" method="post" class="form-horizontal">
								<center>
									<font color="#ff0000"><b><%=mybean.msg%></b></font>
								</center>
								<div class="portlet box">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">
											<%=mybean.status%> Enquiry
										</div>
									</div>
									<div class="portlet-body portlet-empty container-fluid">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<div>
												<center>
													Form fields marked with a red asterisk <font color=#ff0000>*</font>
													are required.
												</center>
											</div>
											<br/>
											
											<div class="form-element6">
												<label>Customer: </label>
													<b>
														<span id="span_bill_customer_id" name="span_bill_customer_id">
															<%
	 															if (!mybean.enquiry_customer_id.equals("0") 
	 																	|| !mybean.enquiry_customer_id.equals("0")) {
	 														%>
	 															<%=mybean.link_customer_name%>
	 														<% 
	 															}
															%>
														</span>
													</b>
												<input name="span_acct_id" type="hidden" id="span_acct_id" value="<%=mybean.customer_id%>" />
												<input name="acct_id" type="hidden" id="acct_id" value="<%=mybean.enquiry_customer_id%>" />
												<input type="hidden" name="txt_status" id="txt_status" value="<%=mybean.status%>" />
											</div>
												
											<div class="form-element6">
												<label> Contact: </label>
												<b><span id="span_bill_contact_id" name="span_bill_contact_id">
														<%
	 														if (!mybean.enquiry_contact_id.equals("0") 
	 																|| !mybean.enquiry_contact_id.equals("0")) {
	 													%>
	 														<%=mybean.link_contact_name%>
	 													<%
	 														}
	 													%>
													</span>
												</b>
												<!--  <a href="#" id="dialog_link">(Select Contact)</a>  -->
												<input name="span_cont_id" type="hidden" id="span_cont_id" value="<%=mybean.contact_id%>" />
												<input name="cont_id" type="hidden" id="cont_id" value="<%=mybean.enquiry_contact_id%>" />
												<div id="dialog-modal"></div>
											</div>
											
											<div class="form-element6">
												<label>Date:</label>
												<input name="txt_enquiry_date" id="txt_enquiry_date" value="<%=mybean.enquirydate%>"
													class="form-control datepicker" type="text" maxlength="10" />
											</div>
											<div class="form-element6">
												<label>Closing Date:</label>
												<input name="txt_enquiry_close_date" id="txt_enquiry_close_date"
													value="<%=mybean.closedate%>" class="form-control datepicker"
													type="text" maxlength="10" />
											</div>
											<div class="form-element6">
												<label> Title<font color=red>*</font>: </label>
												<input name="txt_enquiry_title" type="text" class="form-control" id="txt_enquiry_title"
													value="<%=mybean.enquiry_title%>" size="32" maxlength="255" />
											</div>

											<div class="form-element6">
												<label> Branch<font color=red>*</font>: </label>
												<select name="dr_branch" id="dr_branch" class="form-control"
													onChange="PopulateTeam(this.value);PopulateCampaign();">
													<%=mybean.PopulateBranch(mybean.enquiry_branch_id, "all", "1,2", "", request)%>
												</select>
											</div>
											
											<div class="row">
											
											<div class="form-element6">
												<label> Description: </label>
												<textarea name="txt_enquiry_desc" cols="70" rows="4" class="form-control" id="txt_enquiry_desc"
													onKeyUp="charcount('txt_enquiry_desc', 'span_txt_enquiry_desc','<font color=red>({CHAR} characters left)</font>', '8000')"><%=mybean.enquiry_desc%></textarea>
												<span id="span_txt_enquiry_desc"> (8000 Characters)</span>
											</div>
											<input name="enquiry_enquirytype_id" type="hidden" id="enquiry_enquirytype_id" value="<%=mybean.enquiry_enquirytype_id%>" />
											<%
												if (mybean.enquiry_enquirytype_id.equals("1")) {
											%>
											<div class="form-element3">
												<label> Model<font color=red>*</font>: </label>
												<select name="dr_enquiry_model_id" id="dr_enquiry_model_id"
													class="form-control" onChange="populateItem();">
													<%=mybean.PopulateModel()%>
												</select>
											</div>

											<div class="form-element3">
												<label> Variant: </label>
												<span id="modelitem">
													<select name="dr_enquiry_item_id" id="dr_enquiry_item_id" class="form-control">
														<%=mybean.PopulateItem()%>
													</select>
												</span> 
												
											</div>
											
											<%
												}
											%>
											<%
												if (mybean.enquiry_enquirytype_id.equals("2")) {
											%>
											<!-- 											SELECT2 --------->
											<div class="form-element6">
												<label> Pre Owned Model<font color=red>*</font>: </label>
												<select class="form-control select2" id="preownedvariant" name="preownedvariant">
													<%=mybean.VariantCheck.PopulateVariant(mybean.preowned_variant_id)%>
												</select>
												<!--<input tabindex="-1" class="bigdrop select2-offscreen"  -->
												<!--id="modelvariant" name="modelvariant" style="width:300px"   -->
												<%--value="<%=mybean.enquiry_item_id%>" type="hidden" /> --%>
												<%--value="<%=mybean.enquiry_item_id%>" type="hidden" /> --%>
											</div>
											<%
												}
											%>
											<div class="form-element6">
												<label > Value: </label>
												<input name="txt_enquiry_value" type="text" class="form-control"
													id="txt_enquiry_value" onKeyUp="toInteger('txt_enquiry_value','Value')"
													value="<%=mybean.enquiry_value%>" size="20" maxlength="10" /> 
											</div>
											
											</div>
											
											<div class="form-element3"> 
												<label > AV Presentation:&nbsp; </label>
												<input id="chk_enquiry_avpresent" name="chk_enquiry_avpresent" type="checkbox"
													<%=mybean.PopulateCheck(mybean.enquiry_avpresent)%> /> 
											</div>
											<div class="form-element3"> 
												<label > Manager Assistance:&nbsp; </label>
												<input id="chk_enquiry_manager_assist" type="checkbox"
													name="chk_enquiry_manager_assist"
													<%=mybean.PopulateCheck(mybean.enquiry_manager_assist)%> /> 
											</div>

										</div>
									</div>
								</div>
								

								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">
											Enquiry Status
										</div>
									</div>
									<div class="portlet-body portlet-empty container-fluid">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<div class="form-element6">
												<label > Stage<font color=red>*</font>: </label>
												<select name="dr_enquiry_stage_id" id="dr_enquiry_stage_id" class="form-control">
													<%=mybean.PopulateStage()%>
												</select>
											</div>
											
											<div class="form-element6">
												<label> Status<font color=red>*</font>: </label>
												<select name="dr_enquiry_status_id" class="form-control" id="dr_enquiry_status_id">
													<%=mybean.PopulateStatus()%>
												</select>

											</div>
											
											<div class="row">
												<div class="form-element6">
													<label > Status Comments<font color=red>*</font>: </label>
													<textarea name="txt_enquiry_status_desc" cols="50" rows="4"
														class="form-control" id="txt_enquiry_status_desc"
														onKeyUp="charcount('txt_enquiry_status_desc', 'span_txt_enquiry_status_desc','<font color=red>({CHAR} characters left)</font>', '1000')"><%=mybean.enquiry_status_desc%></textarea>
													<span id="span_txt_enquiry_status_desc"> (1000 Characters)</span>
												</div>
												
												<div class="form-element6 form-element-margin">
													<label > Priority<font color=red>*</font>: </label>
													</label> <span> <b><%=mybean.priorityenquiry_desc%> (<%=mybean.priorityenquiry_duehrs%>)</b></span>
												</div>
												
												<div class="form-element6">
													<label > Reference Consultant : </label>
													<%=mybean.PopulateReferenceExecutives()%>
												</div>
											</div>			
																			
											<div class="row">
												
												<div class="form-element6">
													<label > Team<font color="red">*</font>: </label>
													<!--<select name="dr_enquiry_team" id="dr_enquiry_team" class="dropdown form-control" -->
													<!--onchange="PopulateExecutive();"> -->
													<span id="team">
														<%=mybean.PopulateTeam(mybean.enquiry_branch_id, mybean.comp_id, mybean.team_id)%>
													</span>
													<!--	</select> --> 
												</div>
	
												<div class="form-element6">
													<label > Sales Consultant<font color=red>*</font>: </label>
													<span id="teamexe">
														<%=mybean.PopulateTeamExecutives(mybean.enquiry_branch_id, mybean.team_id, mybean.comp_id)%>
													</span>
												</div>

											</div>
											
											<%
												if (mybean.config_sales_soe.equals("1")) {
											%>
												<div class="form-element6">
													<label > Source of Enquiry<font color=red>*</font>: </label>
													<select name="dr_enquiry_soe_id" id="dr_enquiry_soe_id" class="form-control">
															<%=mybean.PopulateSoe()%>
													</select>
												</div>
											<%
												}
											%>
											<%
												if (mybean.config_sales_sob.equals("1")) {
											%>
												<div class="form-element6">
													<label > Source of Business<font color=red>*</font>: </label>
													<select name="dr_enquiry_sob_id" id="dr_enquiry_sob_id" class="form-control">
														<%=mybean.PopulateSob()%>
													</select>
												</div>
											<%
												}
											%>
											<%
												if (mybean.config_sales_campaign.equals("1")) {
											%>
												<div class="form-element6">
													<label > Campaign<font color=red>*</font>: </label>
													<span id="campaign">
														<select name="dr_enquiry_campaign_id" id="dr_enquiry_campaign_id" class="form-control">
															<%=mybean.PopulateCampaign(mybean.enquiry_date)%>
														</select>
													</span>
													<div class="admin-master">
														<a href="../sales/campaign-list.jsp?all=yes" title="Manage Campaign"></a>
													</div>
												</div>
											<%
												}
											%>
											<%
												if (mybean.config_sales_enquiry_refno.equals("1")) {
											%>
												<div class="form-element6">
													<label > QCS No.<font color=red>*</font>: </label>
													<input name="txt_enquiry_qcsno" type="text" class="form-control" id="txt_enquiry_qcsno"
														value="<%=mybean.enquiry_qcsno%>" size="32" maxlength="50" />
	
												</div>
											<%
												}
											%>
											<div class="form-element6">
												<label> Notes: </label>
												<textarea name="txt_enquiry_notes" id="txt_enquiry_notes"
													cols="70" rows="4" class="form-control"><%=mybean.enquiry_notes%></textarea> 
											</div>
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) {
											%>
												<div class="form-element3 form-element-margin">
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
												<div class="form-element3 form-element-margin">
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
												<div class="form-element3 form-element-margin">
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
												<div class="form-element3 form-element-margin">
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
														if (mybean.status.equals("Update")) {
													%>
	
													<input type="hidden" name="update_button" value="yes">
													<input name="button" type="submit" class="btn btn-success" id="button"
														onClick="selectVariant();return SubmitFormOnce(document.form1, this);" value="Update Enquiry" />
													<input name="delete_button" type="submit" class="btn btn-success" id="delete_button"
														OnClick=" return confirmdelete(this);" value="Delete Enquiry" />
													<%
														}
													%>
	
												</center>
												<center>
													<input type="hidden" name="enquiry_id" value="<%=mybean.enquiry_id%>"/>
													<input type="hidden" name="brand_id" value="<%=mybean.branch_brand_id%>"/>
													<input class="form-control" type="hidden" name="txt_branch_id"
														id="txt_branch_id" value="<%=mybean.enquiry_branch_id%>" />
												</center>
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
	</div>
	<!-- END CONTAINER -->

	<%@include file="../Library/admin-footer.jsp"%>

	<%@include file="../Library/js.jsp"%>

	<script type="text/javascript">
// 		$(function() {
// 			// Dialog
// 			$('#dialog-modal').dialog({
// 				autoOpen : false,
// 				width : 900,
// 				height : 500,
// 				zIndex : 200,
// 				modal : true,
// 				title : "Select Contact"
// 			});
// 			$('#dialog_link').click( function() {
// 				$.ajax({
// 				// 										url: "home. jsp",
// 					success : function(data) {
// 								$('#dialog-modal').html( '<iframe src="../customer/customer-contact-list.jsp?group=selectcontact" width="100%" height="100%" frameborder=0></iframe>');
// 							}
// 				});
// 				$('#dialog-modal').dialog('open');
// 					return true;
// 				});
// 		});
	</script>
	<script language="JavaScript" type="text/javascript">
		function FormFocus() { //v1.0
			//document.form1.txt_title_name.focus();
		}
		function frmSubmit() {
			document.formcontact.submit();
		}

		function selectVariant() {
			// 		alert("comming");
			var temp = document.getElementById('modelvariant').innerHTML;
			$("#txt_modelvariant").val(temp);
			// 		alert("txt_modelvariant=="+temp);
		}

		function PopulateTeam(branch_id) {
			var config_campaign = document.getElementById('txt_config_campaign').value;
			showHint( '../sales/enquiry-check.jsp?team=yes&active=0&enquiry_branch_id=' + branch_id, 'team');
			if (config_campaign == '1') {
				PopulateCampaign(branch_id);
			}
			setTimeout('PopulateTeamExc()', 200);
		}

		function PopulateTeamExc() {
			var branch_id = document.getElementById('dr_branch').value;
			var team_id = document.getElementById('dr_enquiry_team').value;
			if (branch_id != "" && branch_id != "0" && team_id == "0") {
				showHint( '../sales/enquiry-check.jsp?executive=yes&enquiry_branch_id=' + branch_id, 'teamexe');
			}
			if (team_id != "" && team_id != "0" && branch_id != "" && branch_id != "0") {
				showHint('../sales/enquiry-check.jsp?executive=yes&team_id=' + team_id + '&enquiry_branch_id=' + branch_id, 'teamexe');
			}
		}

		function PopulateExecutive() {
			var enquiry_branch_id = document.getElementById('dr_branch').value;
			var team_id = document.getElementById('dr_enquiry_team').value;
			// 		alert("enquiry_branch_id=="+enquiry_branch_id);
			// 		alert("team_id=="+team_id);
			showHint( '../sales/enquiry-check.jsp?executive=yes&active=0&team_id=' + team_id + '&enquiry_branch_id=' + enquiry_branch_id, 'teamexe');
		}

		function PopulateCampaign() {
			var branch_id = document.getElementById('dr_branch').value;
			var enquiry_date = document.getElementById('txt_enquiry_date').value;
			if (branch_id != "" && branch_id != "0") {
				showHint('../sales/campaign-check.jsp?enquiry_branch_id=' + branch_id + '&enquiry_date=' + enquiry_date, 'campaign');
			}
		}

		function CampaignCheck(name, obj, hint) {
			var date = document.getElementById('txt_enquiry_date').value;
			var branch_id = document.getElementById('dr_branch').value;
			var url = "../sales/enquiry-check.jsp?";
			var param = "name=" + name + "&value=" + date + "&branch_id=" + branch_id;
			var str = "123";
			showHintPost(url + param, str, param, hint);
			setTimeout('RefreshHistory()', 1000);
		}

		function populateItem() {
			//alert("itemmm");
			var model_id = document.getElementById('dr_enquiry_model_id').value;
			//alert(model_id);
			showHint( '../sales/enquiry-check.jsp?item=yes&update=yes&enquiry_model_id=' + model_id, 'modelitem');
		}
	</script>

</body>
</HTML>
