<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.ManageCallType_Update" scope="request"/>
<%mybean.doGet(request,response); %>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
       
<%@include file="../Library/css.jsp" %>     
</head>
<body  onLoad="FormFocus()" leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
   <%@include file="../portal/header.jsp" %>
        <div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1><%=mybean.status%> Call Type</h1>
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
							<li><a href="managecalltype.jsp?all=yes">List  Call Type</a> &gt;</li>
							<li><a href="managecalltype-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%> Call Type</a><b>:</b></li>
						</ul>
				         <!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="#ff0000"><b> <%=mybean.msg%> <br></b></font>
					</center>
					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none"><%=mybean.status%> 
										Call Type
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<form name="form1" class="form-horizontal" runat="server" method="post">
											<!-- START PORTLET BODY -->
											<center>
												<font size="1">Form fields marked with a red asterisk <b><font
														color="#ff0000">*</font></b> are required.<br></font>
											</center><br>
											
												<div class="form-element6 form-element-center">
													<label>Name<font color="#ff0000">*</font>:</label>
														<input name="txt_ticketstatus_name" id="txt_ticketstatus_name" type="text"
															class="form-control" value="<%=mybean.type_name%>" maxlength="255"/>
												</div>
				                        <div class="form-element6 form-element-center">	
				                        <%if (mybean.status.equals("Update") && !(mybean.type_entry_date == null) && !(mybean.type_entry_date.equals(""))) {%>
				                      <div class="form-element6">
														<label>Entry By:</label><%=mybean.unescapehtml(mybean.type_entry_by)%>
				                            <input name="entry_by" type="hidden" id="entry_by" value="<%=mybean.type_entry_by%>">
				                          	</div>
	
													<div class="form-element6">
														<label>Entry Date:</label><%=mybean.type_entry_date%>
				                            <input type="hidden" id="entry_date" name="entry_date" value="<%=mybean.type_entry_date%>">
				                        </div>
				                        <%}%>
				                        <% if (mybean.status.equals("Update") && !(mybean.type_modified_date == null) && !(mybean.type_modified_date.equals(""))) {%>
				                      	<div class="form-element6">
														<label>Modified By:</label><%=mybean.unescapehtml(mybean.type_modified_by)%>
				                            <input type="hidden" id="modified_by" name="modified_by" value="<%=mybean.type_modified_by%>">
				                          </div>
	
													<div class="form-element6">
														<label>Modified Date:</label><%=mybean.type_modified_date%>
				                            <input type="hidden" id="modified_date" name="modified_date" value="<%=mybean.type_modified_date%>">
				                        		
													</div>
												<%}%>
												</div>
												<div class="row"></div>
											<center>
											<%if(mybean.status.equals("Add")){%>
				                            <input name="addbutton" type="submit" class="btn btn-success" id="addbutton" value="Add Call Type" onclick="return SumbitFormOnce(document.form1,this);"/>
				                            <input type="hidden" name="add_button" value="yes"/>
				                            <%}else if (mybean.status.equals("Update")){%>
				                            <input type="hidden" name="update_button" value="yes">
				                            <input name="updatebutton" type="submit" class="btn btn-success" id="updatebutton" value="Update Call Type" onclick="return SubmitFormOnce(document.form1,this);"/>
				                            <input name="delete_button" type="submit" class="btn btn-success" id="delete_button" OnClick="return confirmdelete(this)" value="Delete Call Type" >
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
<%@include file="../Library/admin-footer.jsp" %>
<%@include file="../Library/js.jsp" %>
         
<script>
   function FormFocus() { //v1.0
  	document.form1.txt_type_name.focus()
	}
</script>
</body>
</html>
