<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Team_Update" scope="request" />
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
						<h1><%=mybean.status%>
							Team
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
						<li><a href="team.jsp">Teams</a>&gt;</li>
						<li><a href="team-list.jsp?all=yes">List Teams</a> &gt;</li>
						<li><% if (!mybean.team_branch_id.equals("0")) { %>
							<a href="team-list.jsp?dr_branch=<%=mybean.team_branch_id%>"><%=mybean.branch_name%></a> </li> &gt;
						<li> <% } %> <a href="team-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Team</a> </li>:
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
										<center>
											<%=mybean.status%> Team
										</center>
									</div>
								</div>
								
								<div class="portlet-body portlet-empty conatiner-fluid">
									<div class="tab-pane" id="">
										<form name="form1" method="post" class="form-horizontal">
											
											<center>
												Form fields marked with a red asterisk <font color="red">*</font> are required.
											</center>
											
											<div class="form-element6">
												<label >Name<font color="#ff0000">*</font>: </label>
												<input name="txt_team_name" type="text" class="form-control" id="txt_team_name"
													value="<%=mybean.team_name%>" size="50" maxlength="255" />
											</div>
											
											<div class="form-element6">
												<label >Branch<font color="#ff0000">*</font>: </label>
												<div >
													<select name="dr_branch_id" class="form-control"
														id="dr_branch_id" onChange="document.form1.submit()">
														<%=mybean.PopulateBranch(mybean.team_branch_id, "", "1,2", "", request)%>
													</select>
												</div>
											</div>
											
											<div class="form-element6">
												<label >Manager<font color="#ff0000">*</font>: </label>
												<div >
													<select name="dr_executive" id="dr_executive" class="form-control">
														<option value=0>Select</option>
														<%=mybean.PopulateManager()%>
													</select>
												</div>
											</div>
											
											<div class="form-element6">
												<label >CRM Executive:</label>
												<div >
													<select name="dr_team_crm_emp" id="dr_team_crm_emp" class="form-control">
														<%=mybean.PopulateCRMExecutive()%>
													</select>
												</div>
											</div>
											
											<div class="form-element6">
												<label >PBF Executive:</label>
												<div >
													<select name="dr_team_pbf_emp_id" id="dr_team_pbf_emp_id" class="form-control">
														<%=mybean.PopulatePBFExecutive()%>
													</select>
												</div>
											</div>
											
											<div class="form-element6">
												<label >PSF Executive:</label>
												<div >
													<select name="dr_team_psf_emp_id" id="team_psf_emp_id" class="form-control">
														<%=mybean.PopulatePSFExecutive()%>
													</select>
												</div>
											</div>
											
											<div class="form-element6">
												<label >Service PSF Executive:</label>
												<div >
													<select name="dr_team_servicepsf_emp_id" id="dr_team_servicepsf_emp_id" class="form-control">
														<%=mybean.PopulateServicePSFExecutive()%>
													</select>
												</div>
											</div>
											
											<div class="form-element6">
												<label >Pre-Owned Branch:</label>
												<div >
													<select name="dr_team_preownedbranch_id" class="form-control"
														id="dr_team_preownedbranch_id" onchange="getBranch();PopulatePreownedEmp();">
														<%=mybean.PopulatePreOwnedBranch()%>
													</select>

												</div>
											</div>

											<div class="form-element6">
												<label >Pre-Owned Consultant:</label>
												<div id="preownedEmpHint">
														<%=mybean.PopulatePreownedExecutives(mybean.comp_id, mybean.team_preownedbranch_id)%>
												</div>
											</div>
											
											<div class="row">
												<label > Active: </label>
												<input id="team_active" type="checkbox" name="team_active"
													<%=mybean.PopulateCheck(mybean.team_active)%> /> 
											</div>

											<div class="form-element12 form-element">
												<div class="form-element5">
													<label >Manage Sales Consultant for this Team:</label>
													<div>
														<select name="list_exe" size="20" multiple="multiple" class="form-control" id="list_exe">
															<%=mybean.PopulateExecutives()%>
														</select>
													</div>
												</div>
												
												<div class="form-element2">
													<center>
														<br></br>
														<br></br>
														
														<input name="Input" type="button" class="btn btn-success"
															onClick="JavaScript:AddItem('list_exe','list_exe_trans', '')" value=" Add >>" />
														
														<br></br>
														
														<input name="Input" type="button" class="btn btn-success"
															onClick="JavaScript:DeleteItem('list_exe_trans')" value="<< Delete" />
														
														</br>
													</center>
												</div>
												
												<div  class="form-element5">
													<label>&nbsp;</label>
													<select name="list_exe_trans" size="20" multiple="multiple" class="form-control" id="list_exe_trans">
														<%=mybean.PopulateExecutivesTrans()%>
													</select>
												</div>
											</div>

											<!--<div class="container-fluid"> -->
											<!--<center> -->
											<!--<b>Manage Consultants for this Team:</b> -->
											<!--</center> -->
											<!--<div class="form-element6"> -->
											<!--<label ></label>  -->
											<!--<div class="col-md-2"> -->
											<!--	<select name="list_exe" size="20" multiple="multiple" class="form-control" id="list_exe" style="width:300px;"> -->
											<%--      <%=mybean.PopulateExecutives()%> --%>
											<!--    </select>    -->
											<!--</div> -->
											<!--<center> -->
											<!--<div class="col-md-3 col-xs-12"> -->

											<!--<input name="Input" type="button" class="btn btn-success" onClick="JavaScript:AddItem('list_exe','list_exe_trans', '')" value=" Add>>"/> -->
											<!--<br> -->
											<%--<input name="Input" type="button" class="btn btn-success" onClick="JavaScript:DeleteItem('list_exe_trans')" value="<< Delete"/> --%>
											<!--</br> -->

											<!--</div> -->
											<!--</center> -->
											<!--<div class="col-md-2"> -->
											<!--<select name="list_exe_trans" size="20" multiple="multiple" class="form-control" id="list_exe_trans" style="width:300px;"> -->
											<%--      <%=mybean.PopulateExecutivesTrans()%> --%>
											<!--</select> -->
											<!--</div>										 -->
											<!--</div> -->
											<!--</div> -->
											
											<%
												if (mybean.status.equals("Update") && (mybean.entry_by != null) && !(mybean.entry_by.equals(""))) {
											%>
											
											<div class="form-element6">
												<label >Entry By:&nbsp;</label>
												<span >
													<%=mybean.unescapehtml(mybean.entry_by)%>
													<input name="entry_by" type="hidden" id="entry_by" value="<%=mybean.entry_by%>" />
												</span>
											</div>
											
											<div class="form-element6">
												<label >Entry Date:&nbsp;</label>
												<span >
													<%=mybean.entry_date%>
													<input type="hidden" name="entry_date" value="<%=mybean.entry_date%>" />
												</span>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && (mybean.modified_by != null) && !(mybean.modified_by.equals(""))) {
											%>
											<div class="form-element6">
												<label >Modified By:&nbsp;</label>
												<span >
													<%=mybean.unescapehtml(mybean.modified_by)%>
													<input name="modified_by" type="hidden" id="modified_by" value="<%=mybean.modified_by%>" />
												</span>
											</div>
											<div class="form-element6">
												<label >Modified Date:&nbsp;</label>
												<span >
													<%=mybean.modified_date%>
													<input type="hidden" name="modified_date" value="<%=mybean.modified_date%>" />
												</span>
											</div>
											<%
												}
											%>
											<br>
											<center>
												<%
													if (mybean.status.equals("Add")) {
												%>
												
												<input name="button" type="submit" class="btn btn-success" id="button" value="Add Team"
													onClick="onPress(); return SubmitFormOnce(document.form1, this);" />
												
												<input type="hidden" name="add_button" value="yes">
												
												<%
													} else if (mybean.status.equals("Update")) {
												%>
												
												<input name="update_button" type="hidden" value="yes" />
												
												<input name="button" type="submit" class="btn btn-success" id="button" value="Update Team"
													onClick="onPress(); return SubmitFormOnce(document.form1, this);" />
												
												<input name="delete_button" type="submit" class="btn btn-success" id="delete_button"
													OnClick="return confirmdelete(this)" value="Delete Team" />
												<%
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
	<!-- END CONTAINER -->

	<%@include file="../Library/admin-footer.jsp"%>

	<%@include file="../Library/js.jsp"%>

	<script language="JavaScript" type="text/javascript">
		function FormFocus() {
			document.form1.txt_team_name.focus()
		}
		
		function getBranch(){
			var branch_id = document.getElementById("dr_team_preownedbranch_id").value;
			branch_id=branch_id + ',';
			showHint('../portal/executive-check.jsp?preownedteam=yes&branch_id='+ branch_id, 'dr_team_preownedemp_id');
		}
		
		function PopulatePreownedEmp() {
			var branch_id = document.getElementById("dr_team_preownedbranch_id").value;
			showHint('../sales/mis-check1.jsp?preownedemp=yes&branch_id='
					+ branch_id, 'preownedEmpHint');
		}
		
	</script>
	<script>
		function onPress() {
			for (i = 0; i < document.form1.list_exe_trans.options.length; i++) {
				document.form1.list_exe_trans.options[i].selected = true;
			}
		}
	</script>



</body>
</HTML>

