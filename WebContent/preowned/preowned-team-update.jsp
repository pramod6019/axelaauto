<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.preowned.Preowned_Team_Update" scope="request" />
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

<script language="JavaScript" type="text/javascript">
	function FormFocus() {
		document.form1.txt_team_name.focus()
	}
	
	function getBranch(){
		var branch_id = document.getElementById("dr_team_preownedbranch_id").value;
		branch_id=branch_id + ',';
		showHint('../portal/executive-check.jsp?preownedteam=yes&branch_id='+ branch_id, 'dr_team_preownedemp_id');
	}
</script>
<script>
	function onPress() {
		for (i = 0; i < document.form1.list_exe_trans.options.length; i++) {
			document.form1.list_exe_trans.options[i].selected = true;
		}
	}
</script>

<link href="../assets/css/style.css" rel="stylesheet" type="text/css" />
</HEAD>


<body onLoad="FormFocus()"
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
						<h1><%=mybean.status%> Pre-Owned Team </h1>
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
						<li><a href="preowned-team.jsp">Pre-Owned Teams</a>&gt;</li>
						<li><a href="preowned-team-list.jsp?all=yes">Pre-Owned List Teams</a> &gt;</li>
						<li>
							<%
								if (!mybean.preownedteam_branch_id.equals("0")) {
							%> <a href="preowned-team-list.jsp?dr_branch=<%=mybean.preownedteam_branch_id%>"><%=mybean.branch_name%></a>
						</li> &gt;
						<li>
							<%
								}
							%> <a href="preowned-team-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%> Pre-Owned Team</a>
						</li>:
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
											Pre-Owned Team
										</center>
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<form name="form1" method="post" class="form-horizontal">
											<center>
												<font size="">Form fields marked with a red asterisk
													<font color="red">*</font> are required.
												</font>
											</center>
											<div class="form-element6">
												<label>Name<font
													color="#ff0000">*</font>:
												</label>
													<input name="txt_team_name" type="text"
														class="form-control" id="txt_team_name"
														value="<%=mybean.preownedteam_name%>" size="50" maxlength="255" />
											</div>
											<div class="form-element6">
												<label>Branch<font
													color="#ff0000">*</font>:
												</label>
													<select name="dr_branch_id" class="form-control"
														id="dr_branch_id" onChange="document.form1.submit()">
														<%=mybean.PopulateBranch(mybean.preownedteam_branch_id, "", "2", "", request)%>
													</select>
											</div>
											<div class="form-element6">
												<label>Manager<font
													color="#ff0000">*</font>:
												</label>
													<select name="dr_executive" id="dr_executive"
														class="form-control">
														<option value=0>Select</option>
														<%=mybean.PopulateManager()%>
													</select>
											</div>
											<div class="form-element6">
												<label>CRM Executive:</label>
													<select name="dr_team_crm_emp" id="dr_team_crm_emp"
														class="form-control">
														<%=mybean.PopulateCRMExecutive()%>
													</select>
											</div>
<!-- 											<div class="form-group"> -->
<!-- 												<label class="control-label col-md-4">PBF Executive:</label> -->
<!-- 												<div class="col-md-6"> -->
<!-- 													<select name="dr_team_pbf_emp_id" id="dr_team_pbf_emp_id" -->
<!-- 														class="form-control"> -->
<%-- 														<%=mybean.PopulatePBFExecutive()%> --%>
<!-- 													</select> -->
<!-- 												</div> -->
<!-- 											</div> -->
											<div class="form-element6">
												<label>PSF Executive:</label>
													<select name="dr_team_psf_emp_id" id="team_psf_emp_id"
														class="form-control">
														<%=mybean.PopulatePSFExecutive()%>
													</select>
											</div>
<div class="row"></div>
											<div class="form-element5">
												<label>Manage Pre-Owned Consultant for this Team:</label>
														<select name="list_exe" size="20" multiple="multiple"
															class="form-control" id="list_exe">
															<%=mybean.PopulateExecutives()%>
														</select>
												</div>	
													<div class="form-element2">
														<center>
															<br></br>
															<input name="Input" type="button" class="btn btn-success"
																onClick="JavaScript:AddItem('list_exe','list_exe_trans', '')"
																value=" Add >>" /> <br></br> <input name="Input"
																type="button" class="btn btn-success"
																onClick="JavaScript:DeleteItem('list_exe_trans')"
																value="<< Delete" /> </br>
														</center>
													</div>
													<div class="form-element5 form-element-margin">
														<select name="list_exe_trans" size="20"
															multiple="multiple" class="form-control"
															id="list_exe_trans">
															<%=mybean.PopulateExecutivesTrans()%>
														</select>
													</div>

												

											<!-- 					<div class="container-fluid"> -->
											<!-- 					<center> -->
											<!-- 					<b>Manage Pre-Owned Consultant for this Team:</b> -->
											<!-- 					</center> -->
											<!-- 					<div class="form-group"> -->
											<!-- 					<label class="control-label col-md-4"></label>  -->
											<!-- 					<div class="col-md-2"> -->
											<!-- 						<select name="list_exe" size="20" multiple="multiple" class="form-control" id="list_exe" style="width:300px;"> -->
											<%--                                         <%=mybean.PopulateExecutives()%> --%>
											<!--                                       </select>    -->
											<!-- 					</div> -->
											<!-- 					<center> -->
											<!-- 					<div class="col-md-3 col-xs-12"> -->

											<!-- 					<input name="Input" type="button" class="btn btn-success" onClick="JavaScript:AddItem('list_exe','list_exe_trans', '')" value=" Add>>"/> -->
											<!-- 					<br> -->
											<%--                     <input name="Input" type="button" class="btn btn-success" onClick="JavaScript:DeleteItem('list_exe_trans')" value="<< Delete"/> --%>
											<!--                     </br> -->

											<!--                     </div> -->
											<!--                     </center> -->
											<!-- 					<div class="col-md-2"> -->
											<!-- 					<select name="list_exe_trans" size="20" multiple="multiple" class="form-control" id="list_exe_trans" style="width:300px;"> -->
											<%--                                         <%=mybean.PopulateExecutivesTrans()%> --%>
											<!--                                   </select> -->
											<!--                     </div>										 -->
											<!-- 					</div> -->
											<!-- 					</div> -->
											<div class="form-element12">
												<label> Active: </label>
													<input id="team_active" type="checkbox" name="team_active"
														<%=mybean.PopulateCheck(mybean.team_active)%> />
											</div>
											<%
												if (mybean.status.equals("Update") && (mybean.entry_by != null) && !(mybean.entry_by.equals(""))) {
											%>
											<div class="form-element6">
												<label>Entry By:</label>
													<%=mybean.unescapehtml(mybean.entry_by)%>
													<input name="entry_by" type="hidden" id="entry_by"
														value="<%=mybean.entry_by%>" />
											</div>
											<div class="form-element6">
												<label>Entry Date:</label>
													<%=mybean.entry_date%>
													<input type="hidden" name="entry_date"
														value="<%=mybean.entry_date%>" />
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && (mybean.modified_by != null) && !(mybean.modified_by.equals(""))) {
											%>
											<div class="form-element6">
												<label>Modified By:</label>
													<%=mybean.unescapehtml(mybean.modified_by)%>
													<input name="modified_by" type="hidden" id="modified_by"
														value="<%=mybean.modified_by%>" />
											</div>
											<div class="form-element6">
												<label>Modified Date:</label>
													<%=mybean.modified_date%>
													<input type="hidden" name="modified_date"
														value="<%=mybean.modified_date%>" />
											</div>
											<%
												}
											%>
											<br>
											<div class="form-element12">
											<center>
												<%
													if (mybean.status.equals("Add")) {
												%>
												<input name="button" type="submit" class="btn btn-success"
													id="button" value="Add Team"
													onClick="onPress(); return SubmitFormOnce(document.form1, this);" />
												<input type="hidden" name="add_button" value="yes">
												<%
													} else if (mybean.status.equals("Update")) {
												%>
												<input name="update_button" type="hidden" value="yes" /> <input
													name="button" type="submit" class="btn btn-success"
													id="button" value="Update Team"
													onClick="onPress(); return SubmitFormOnce(document.form1, this);" />
												<input name="delete_button" type="submit"
													class="btn btn-success" id="delete_button"
													OnClick="return confirmdelete(this)" value="Delete Team" />
												<%
													}
												%>

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

	<%@include file="../Library/js.jsp" %>

</body>
</HTML>

