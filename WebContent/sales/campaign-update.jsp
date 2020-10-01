<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Campaign_Update"
	scope="request" />
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
 
<script language="JavaScript" type="text/javascript">
 
function frmSubmit()
{
	document.form1.submit();
}

function FormFocus()
{
	document.form1.txt_campaign_name.focus();	
}
    </script>

<body onLoad="FormFocus();"
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
						<h1><%=mybean.status%>
							Campaign
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
						<li><a href="campaign.jsp">Campaigns</a>&gt;:</li>
						<li><a href="campaign-list.jsp?all=recent"> List
								Campaigns</a> &gt;</li>
						<li><a href="campaign-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>
								Campaign</a><b>:</b></li>
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
										<center><%=mybean.status%>
											Campaign
										</center>
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="container-fluid">
										<!-- START PORTLET BODY -->
										<form name="form1" method="post" class="form-horizontal">
											<center>
												<font size="1">Form fields marked with a red asterisk
													<font color=#ff0000>*</font> are required.
												</font><br></br>
											</center>
											<div class="form-element6">
												<label> Campaign Name<font color=red>*</font>:&nbsp; </label>
													<input name="txt_campaign_name" id="txt_campaign_name"
														type="text" class="form-control"
														value="<%=mybean.campaign_name%>" s size="60">
											</div>
											<div class="form-element6">
												<label>Campaign Type<font color=red>*</font>:&nbsp; </label>
													<select id="dr_camptype_id" name="dr_camptype_id"
														class="form-control">
														<%=mybean.PopulateCampType()%>
													</select>
											</div>
											<div class="form-element6">
												<label>Description<font color=red>*</font>:&nbsp; </label>
													<textarea name="txt_campaign_desc" cols="40" rows="6"
														class="form-control" id="txt_campaign_desc"><%=mybean.campaign_desc%></textarea>
											</div>

											<div class="form-element3">
												<label>Start Date<font color=#ff0000><b>*</b></font>:&nbsp;</label>
													<input name="txt_campaign_startdate"
														id="txt_campaign_startdate"
														value="<%=mybean.campaign_startdate%>"
														class="form-control datepicker"
														 type="text" value="" />
											</div>
											<div class="form-element3">
												<label>End Date<font color=#ff0000><b>*</b></font>:&nbsp;</label>
													<input name="txt_campaign_enddate"
														id="txt_campaign_enddate"
														value="<%=mybean.campaign_enddate%>"
														class="form-control datepicker"
														type="text" value="" />
											</div>
										
											<div class="form-element6">
												<label>Campaign Budget<font color=red>*</font>:&nbsp; </label>
													<input name="txt_campaign_budget"
														value="<%=mybean.campaign_budget%>" class="form-control"
														id="txt_campaign_budget"
														onKeyUp="toNumber('txt_campaign_budget','Campaign Budget')"
														maxlength="10"> </input>
											</div>
												<div class="row"></div>
											<SCRIPT language="JavaScript">
function onPress()
{
	for (i=0; i < document.form1.list_branch_trans.options.length; i++ )
        {
	document.form1.list_branch_trans.options[i].selected =true;
	}
  
}
 
</SCRIPT>
											<div class="form-element5">
												<label>Select Branch:&nbsp; </label>
													<select name="list_branch" size="10" multiple="multiple"
														class="form-control" id="list_branch">
														<%=mybean.PopulateBranch()%>
													</select>
												</div>
												<center>
													<div class="form-element2">
													<label>&nbsp;</label>
														<br /> <br /> <INPUT name="Input" type="button"
															class="button btn btn-success"
															onClick="JavaScript:AddItem('list_branch','list_branch_trans', '')"
															value=" Add >>"> <BR> <INPUT name="Input"
															type="button" class="button btn btn-success"
															onClick="JavaScript:DeleteItem('list_branch_trans')"
															value="<< Delete">
													</div>
												</center>
												<div class="form-element5">
												<label>&nbsp;</label>
													<select name="list_branch_trans" size="10"
														multiple="multiple" class="form-control"
														id="list_branch_trans">
														<%=mybean.PopulateBranchTrans(request)%>
													</select>
												</div>

											</div>
											<div class="form-element6">
												<label>Notes:&nbsp; </label>
													<textarea name="txt_campaign_notes" cols="70" rows="4"
														class="form-control" id="txt_campaign_notes"><%=mybean.campaign_notes%></textarea>
											</div>
											<div class="form-element6">
												<label> Active:&nbsp;</label>
													<input type="checkbox" name="chk_campaign_active"
														id="chk_campaign_active" style="margin-top: 11px;"
														<%=mybean.PopulateCheck(mybean.campaign_active)%>>
											</div>
											<div class="row"></div>
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_by.equals(""))) {
											%>
											<div class="form-element6">
												<label> Entry By: <%=mybean.unescapehtml(mybean.entry_by)%></label>
												</div>
											<div class="form-element6">
												<label> Entry Time: <%=mybean.entry_date%></label>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.modified_by.equals(""))) {
											%>
											<div class="form-element6">
												<label>Modified By: <%=mybean.unescapehtml(mybean.modified_by)%></label>
												</div>
											<div class="form-element6">
												<label>Modified Time: <%=mybean.modified_date%></label>
											</div>
											<%
												}
											%>
											<center>
												<%
													if (mybean.status.equals("Add")) {
												%>
												<input type="submit" name="add_button" id="add_button"
													class="button btn btn-success" value="Add Campaign"
													onClick="onPress(); return SubmitFormOnce(document.form1, this);">
												<input type="hidden" name="add_button1" value="yes">
												<!--   <input type="submit" name="add_button" id="add_button" class="button" value="Add Campaign" onClick="onPress(); return SubmitFormOnce(document.form1, this);">
                      <input type="hidden" name="add_button" value="yes"> -->
												<%
													}
													if (mybean.status.equals("Update")) {
												%>
												<input type="submit" name="update_button" id="update_button"
													class="button btn btn-success"
													onClick="onPress();return SubmitFormOnce(document.form1, this);"
													value="Update Campaign"> <input type="hidden"
													name="update_button1" id="update_button1" value="yes">
												<input type="submit" name="delete_button" id="delete_button"
													class="button btn btn-success"
													OnClick="return confirmdelete(this)"
													value="Delete Campaign">
												<%
													}
												%>
											</center>
											<center>
												<input type="hidden" name="campaign_id"
													value="<%=mybean.campaign_id%>"> <input
													type="hidden" name="entry_by" value="<%=mybean.entry_by%>">
												<input type="hidden" name="entry_date"
													value="<%=mybean.entry_date%>"> <input
													type="hidden" name="modified_by"
													value="<%=mybean.modified_by%>"> <input
													type="hidden" name="modified_date"
													value="<%=mybean.modified_date%>">
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
