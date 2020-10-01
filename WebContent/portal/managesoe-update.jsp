<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Managesoe_Update"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
<link href='../Library/jquery.qtip.css' rel='stylesheet' type='text/css' />
</head>

<body onLoad="FormFocus()" class="page-container-bg-solid page-header-menu-fixed">
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
						<h1><%=mybean.status%>&nbsp;SOE
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
						<!-- BEGIN PAGE BREADCRUMBS -->
						<ul class="page-breadcrumb breadcrumb">
							<li><a href="home.jsp">Home</a> &gt;</li>
							<li><a href="manager.jsp">Business Manager</a> &gt;</li>
							<li><a href="managesoe.jsp?all=yes">List SOE</a> &gt;</li>
							<li><a href="managesoe-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;SOE</a>:</li>
						</ul>
						<!-- END PAGE BREADCRUMBS -->
						<center>
							<font color="#ff0000"><b><%=mybean.msg%></b></font>
						</center>

						<div class="tab-pane" id="">

							<div class="portlet box">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none"><%=mybean.status%>&nbsp;SOE
									</div>
								</div>
								
								<div class="container-fluid portlet-body">
									<div class="tab-pane" id="">
										<center>
											<font>Form fields marked with a red asterisk <b><font
													color="#ff0000">*</font></b> are required.
											</font>
										</center>
										<br />
										
										<form method="post" name="form1" class="form-horizontal">
										
											<div class="form-element6 form-element-center">
												<label> SOE<font color=red>*</font>: </label>
												<input name="txt_soe_name" id="txt_soe_name" type="text"
													class="form-control" value="<%=mybean.soe_name%>"
													maxlength="255" size="50" />
											</div>


											<div class="form-element6 form-element-center">
												<li class="list-group-item bg-font-blue"><center>SOB</center>
												</li> <br>
												<!-- 												<label class="control-label col-md-4 col-sm-4"></label> -->
												<div class="form-element12 form-element">
													<div class="form-element5">
														<select name="soe_sob" size="15" multiple="multiple"
															type="text" class="form-control" id="soe_sob">
															<%=mybean.PopulateSob()%>
														</select>
													</div>
													
													<div class="form-element2">
														<center>
															<br/> 
															<input name="Input5" type="button" class="btn btn-success"
																onclick="JavaScript:AddItem('soe_sob','soe_sob_trans', '')"
																value="Add &gt;&gt;" /> <br>
															<input name="Input" type="button" class="btn btn-success"
																onclick="JavaScript:DeleteItem('soe_sob_trans')"
																value="&lt;&lt;Delete" /> 
														</center>
													</div>
													
													<div class="form-element5">
														<select name="soe_sob_trans" size="15" multiple="multiple"
															type="text" class="form-control" id="soe_sob_trans">
															<%=mybean.PopulateSobTrans()%>
														</select>
													</div>
												</div>
											</div>

											<div class="form-element6 form-element-center">
												<div class="form-element12 form-element">
													<div class="form-element7">
														<label>CRM Enable:</label>
														<!-- 													<div class="col-md-6" style="top: 8px"> -->
														<input type="checkbox" name="chk_crm_enable"
															id="chk_crm_enable"
															<%=mybean.PopulateCheck(mybean.soe_crm_enable)%> />
														<!-- 													</div> -->
													</div>

													<div class="form-element5">
														<label>Active:</label>
														<!-- 													<div class="col-md-6" style="top: 8px"> -->
														<input type="checkbox" name="chk_soe_active" id="chk_soe_active"
															<%=mybean.PopulateCheck(mybean.soe_active)%> />
														<!-- 													</div> -->
													</div>
												</div>
											</div>
											
											<% if (mybean.status.equals("Update") && !(mybean.entry_by == null)
														&& !(mybean.entry_by.equals(""))) {
											%>
											
											<div class="form-element6 form-element-center">
												<div class="form-element12 form-element">
													<div class="form-element7">
														<label>Entry By:&nbsp;</label>
														<%=mybean.unescapehtml(mybean.entry_by)%>
														<input name="entry_by" type="hidden" id="entry_by"
															value="<%=mybean.unescapehtml(mybean.entry_by)%>" />
													</div>
													
													<% } %>
													
													<% if (mybean.status.equals("Update") && !(mybean.entry_date == null)
																&& !(mybean.entry_date.equals(""))) {
													%>
													
													<div class="form-element5">
														<label>Entry Date:&nbsp;</label>
														<%=mybean.entry_date%>
														<input type="hidden" name="entry_date"
															value="<%=mybean.entry_date%>" />
													</div>
												</div>
											</div>
											
											<% } %>
											
											<% if (mybean.status.equals("Update") && !(mybean.modified_by == null)
														&& !(mybean.modified_by.equals(""))) {
											%>
											<div class="form-element6 form-element-center">
												<div class="form-element12 form-element">
													<div class="form-element7">
														<label>Modified By:&nbsp;</label>
														<%=mybean.unescapehtml(mybean.modified_by)%>
														<input name="modified_by" type="hidden" id="modified_by"
															value="<%=mybean.unescapehtml(mybean.modified_by)%>" />
													</div>
													
													<% } %>
													
													<% if (mybean.status.equals("Update") && !(mybean.modified_date == null)
																&& !(mybean.modified_date.equals(""))) {
													%>
													
													<div class="form-element5">
														<label>Modified Date:&nbsp;</label>
														<%=mybean.modified_date%>
														<input type="hidden" name="modified_date"
															value="<%=mybean.modified_date%>" />
													</div>
												</div>
											</div>
											
											<% } %>

											<div class="form-element12">
												<div class="form-element-center">
													<center>
														<% if (mybean.status.equals("Add")) { %>
														<input name="button" type="submit" class="btn btn-success"
															id="button" value="Add SOE"
															onClick="onPress();return SubmitFormOnce(document.form1, this);" />
														<input type="hidden" name="add_button" value="yes">
														<% } else if (mybean.status.equals("Update")) { %>
														<input type="hidden" name="update_button" value="yes">
														<input name="button" type="submit" class="btn btn-success"
															id="button" value="Update SOE"
															onClick="onPress();return SubmitFormOnce(document.form1, this);" />
														<input name="delete_button" type="submit"
															class="btn btn-success" id="delete_button"
															OnClick="return confirmdelete(this)" value="Delete SOE" />
														<% } %>
													</center>
												</div>
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


	<%@include file="../Library/admin-footer.jsp"%>

	<%@include file="../Library/js.jsp"%>

	<script language="JavaScript">
		function onPress() {
			for (i = 0; i < document.form1.soe_sob_trans.options.length; i++) {
				document.form1.soe_sob_trans.options[i].selected = true;
			}
		}
	</script>

	<script language="JavaScript" type="text/javascript">
		function FormFocus() { //v1.0
			document.form1.txt_soe_name.focus()
		}
	</script>
</body>
</html>
