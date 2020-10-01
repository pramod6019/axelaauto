<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.mktg.Campaign_Update" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<link rel="shortcut icon" type="image/x-icon"
	href="../admin-ifx/axela.ico">

<link href='../Library/jquery.qtip.css' rel='stylesheet' type='text/css' />
<link href="../assets/css/font-awesome.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/components-rounded.css" rel="stylesheet"
	id="style_components" type="text/css" />
<link href="../assets/css/font-awesome.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/select2.min.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/select2-bootstrap.min.css" rel="stylesheet"
	type="text/css" />


<link href="../assets/css/bootstrap-wysihtml5.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/summernote.css" rel="stylesheet"
	type="text/css" />

<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />

<link href="../assets/css/style.css" rel="stylesheet" type="text/css" />
		</HEAD>
<body  class="page-container-bg-solid page-header-menu-fixed">
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
						<h1><%=mybean.status%> Campaign</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../mktg/campaign.jsp">Campaign</a> &gt;</li>
						<li><a href="../mktg/campaign-list.jsp?all=yes">List Campaign</a> &gt;</li>
						<li><a href="campaign-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Campaign</a>: </li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%>&nbsp;Campaign
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="formcontact" method="post" class="form-horizontal">
										
                                           <div class="form-body">
												<div class="form-group">
													<label class="col-md-4 control-label">Branch<font
														color=red>*</font>:
													</label>
													<div class="col-md-6">
														<select name="dr_campaign_branch_id" class="form-control"
															id="dr_lead_branch_id">
															<%=mybean.PopulateBranch()%>
														</select>
													</div>
												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4 col-sm-2  col-xs-12">Subject<font color="red">*</font>:</label>
												<div class="col-md-6 col-sm-10 col-xs-12" id="emprows">
													<input class="form-control" name="txt_campaign_subject"
														type="text" id="txt_campaign_subject"
														value="<%=mybean.campaign_subject%>" size="32"
														maxlength="255">
												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4 col-xs-12">
													Message<font color="red">*</font>:</label>
												<div class="col-md-6 col-xs-12">
													<textarea name="txt_campaign_msg" cols="70" rows="4"
														class="form-control summernote_1" id="txt_campaign_msg"><%=mybean.campaign_msg%></textarea>
													<script type="text/javascript">
														CKEDITOR
																.replace(
																		'txt_campaign_msg',
																		{
																			uiColor : hexc($(
																					"a:link")
																					.css(
																							"color")),

																		});
													</script>
												</div>
											</div>
											    <%
													if (mybean.status.equals("Update") && !(mybean.entry_by == null)
															&& !(mybean.entry_by.equals(""))) {
												%>
												<div class="form-group">
													<label class="control-label col-md-4">Entry By:</label>
													<div class="txt-align">
														<%=mybean.unescapehtml(mybean.entry_by)%>
														<input name="entry_by" type="hidden" id="entry_by"
															value="<%=mybean.entry_by%>">
													</div>
												</div>
												<div class="form-group">
													<label class="control-label col-md-4">Entry Date:</label>
													<div class="txt-align">
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
												<div class="form-group">
													<label class="control-label col-md-4">Modified By:</label>
													<div class="txt-align">
														<%=mybean.unescapehtml(mybean.modified_by)%>
														<input name="modified_by" type="hidden" id="modified_by"
															value="<%=mybean.modified_by%>">
													</div>
												</div>
												<div class="form-group">
													<label class="control-label col-md-4">Modified
														Date:</label>
													<div class="txt-align">
														<%=mybean.modified_date%>
														<input type="hidden" name="modified_date"
															value="<%=mybean.modified_date%>">
													</div>
												</div>
												<%
													}
												%>
												<center>
													<%
														if (mybean.status.equals("Add")) {
													%>
													<input name="addbutton" type="submit"
														class="btn btn-success" id="addbutton"
														value="Add Campaign"
														onClick="retrive(); onPress(); return SubmitFormOnce(document.formcontact, this);" />
													<input type="hidden" name="add_button" value="yes">
													<%
														} else if (mybean.status.equals("Update")) {
													%>
													<input type="hidden" name="update_button" value="yes">
													<input name="updatebutton" type="submit"
														class="btn btn-success" id="updatebutton"
														value="Update Campaign"
														onClick="onPress(); return SubmitFormOnce(document.formcontact, this);" />
													<input name="delete_button" type="submit"
														class="btn btn-success" id="delete_button"
														onClick="return confirmdelete(this)"
														value="Delete Campaign" />
													<%
														}
													%>
												</center>
												
								    <table class="table table-hover table-bordered">
												<thead>
													<tr>
														<th colspan="2" align="center">Substitution Variables</th>
													</tr>
												</thead>
												<tr>
													<td align="right">Lead Name:</td>
													<td align="left">[LEADNAME]</td>
												</tr>
												<tr>
													<td align="right">Lead ID:</td>
													<td align="left">[LEADID]</td>
												</tr>
												<tr>
													<td align="right">Lead Job Title:</td>
													<td align="left">[LEADJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Lead Mobile:</td>
													<td align="left">[LEADMOBILE]</td>
												</tr>
												<tr>
													<td align="right">Lead Phone:</td>
													<td align="left">[LEADPHONE]</td>
												</tr>
												<tr>
													<td align="right">Lead Email:</td>
													<td align="left">[LEADEMAIL]</td>
												</tr>
										 </table>
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
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="../assets/js/summernote.min.js" type="text/javascript"></script>
	<script src="../assets/js/components-editors.min.js"
		type="text/javascript"></script>
	<script src="../assets/js/bootstrap-datepicker.js"
		type="text/javascript"></script>
	<!-- <script type="text/javascript" src="../ckeditor/ckeditor.js"></script> -->
	<script type="text/javascript" src="../Library/dynacheck.js"></script>
	<script src="../assets/js/select2.full.min.js" type="text/javascript"></script>
	<script src="../assets/js/components-select2.min.js"
		type="text/javascript"></script>
	<script type="text/javascript" src="../Library/Validate.js"></script>
</body>
</HTML>