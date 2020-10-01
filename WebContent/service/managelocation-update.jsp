<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.ManageLocation_Update" scope="request"/>
<%mybean.doGet(request,response); %>
<!DOCTYPE html>
<html>
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp" %> 
   
</head>
<body  onLoad="FormFocus()" leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
     <%@include file="../portal/header.jsp" %>
        
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
						<h1><%=mybean.status%>&nbsp;Service Location
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
							<li><a href="../portal/manager.jsp">Business Manager</a> &gt;</li>
							<li><a href="managelocation.jsp?all=yes">List Locations</a> &gt;</li>
							<li><a href="managelocation-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Service Location</a><b>:</b></li>
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
										<center><%=mybean.status%>&nbsp;Service Location</center>
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
									<form name="form1" method="post" class="form-horizontal">
											<center>
												<font size="1">Form fields marked with a red asterisk
													<font color=#ff0000>*</font> are required.</font>
												</center><br>
												
												<div class="form-element6">
													<label>Branch<font color="#ff0000">*</font>:&nbsp;</label>
													<select id="dr_location_branch_id" name="dr_location_branch_id" class="form-control" >
								                      <%=mybean.PopulateBranch(mybean.location_branch_id,"", "1,3", "", request)%>
								                    </select>					
												</div>
												
												<div class="form-element6">
													<label>Location<font color="#ff0000">*</font>:&nbsp;</label>
															<input name ="txt_location_name" type="text" class="form-control" id="txt_location_name" value="<%=mybean.location_name %>"  maxlength="255"/>
												</div>
												
												<div class="row"></div>
												
												<div class="form-element6">
													<label>Lead Time<font color="#ff0000">*</font>:&nbsp;</label>
															<input name ="txt_location_leadtime" type="text" class="form-control" id="txt_location_leadtime" 
																onKeyUp="toInteger('txt_location_leadtime','Lead Time')" value="<%=mybean.location_leadtime %>" maxlength="10"/>
																<span>in Minutes (time to reach location from showroom)</span>
												</div>
												
												<div class="form-element6">
													<label>Inspection Duration<font color="#ff0000">*</font>:&nbsp;</label>
															<input name ="txt_location_inspection_dur" type="text" class="form-control" id="txt_location_inspection_dur" 
																onKeyUp="toInteger('txt_location_inspection_dur','Inspection Duration')" value="<%=mybean.location_inspection_dur %>" maxlength="10"/>
																<span>in Minutes (Inspection Duration plus time to reach showroom from location)</span>
												</div>
												
												<div class="form-element6">
													<label>Active:&nbsp;</label>
															<input type="checkbox" name="chk_location_active" id="chk_location_active" <%=mybean.PopulateCheck(mybean.location_active)%>/>
												</div>
												
												<div class="row"></div>
												
											<%if (mybean.status.equals("Update") && !(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) {%>
											
											<div class="form-element6">
												<label>Entry By: &nbsp;</label>
													<%=mybean.unescapehtml(mybean.entry_by)%>
													<input name="entry_by" type="hidden" id="entry_by"
														value="<%=mybean.unescapehtml(mybean.entry_by)%>">
											</div>
											<%}%>
											
											<%if (mybean.status.equals("Update") && !(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) {%>
											<div class="form-element6">
												<label>Entry Date:&nbsp; </label>
													<%=mybean.entry_date%>
													<input type="hidden" name="entry_date" value="<%=mybean.entry_date%>">
											</div>
											<%}%>
											
											<%if (mybean.status.equals("Update") && !(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) {%>
											
											<div class="form-element6">
												<label>Modified By: </label>
													<%=mybean.unescapehtml(mybean.modified_by)%>
													<input name="modified_by" type="hidden" id="modified_by"
														value="<%=mybean.unescapehtml(mybean.modified_by)%>">
											</div>
											<%}%>
											
											<%if (mybean.status.equals("Update") && !(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) {%>
											
											<div class="form-element6">
												<label>Modified Date: </label>
													<%=mybean.modified_date%>
													<input type="hidden" name="modified_date" value="<%=mybean.modified_date%>">
											</div>
											<%}%>
											
											<center>
												<%if(mybean.status.equals("Add")){%>
						                          <input name="addbutton" type="submit" class="btn btn-success" id="addbutton" value="Add Location" 
						                          onclick="return SubmitFormOnce(document.form1,this);"/>
						                          <input type="hidden" name="add_button" value="yes">
						                        <%}%>
											
												<%if (mybean.status.equals("Update")) {%>
												<input type="hidden" name="update_button" value="yes">
												<input name="update_button" type="submit" class="btn btn-success"
													id="button"
													onclick="return SubmitFormOnce(document.form1,this);"
													value="Update Location" /> 
												<input name="delete_button"
													type="submit" class="btn btn-success" id="delete_button"
													OnClick=" return confirmdelete(this);"
													value="Delete Location" />
												<%}%>
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
		function FormFocus() { //v1.0
		  document.form1.txt_location_name.focus()
		}
    </script>
    </body> 
</html>
