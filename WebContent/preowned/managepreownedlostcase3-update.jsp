<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.preowned.ManagePreownedLostCase3_Update" scope="request"/>
<%mybean.doGet(request,response); %>    
<!DOCTYPE html>
<html>
        <head>
        <title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
        <meta http-equiv="pragma" content="no-cache">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
        
	<%@include file="../Library/css.jsp" %>
</head>
<body  onLoad="FormFocus()" class="page-container-bg-solid page-header-menu-fixed">
        
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
								<h1><%=mybean.status%> Pre-Owned Lost Case 3</h1>
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
									<li><a href="managepreownedlostcase3.jsp?all=yes">List Pre-Owned Lost Case3</a> &gt;</li>
									<li><a href="managepreownedlostcase3-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Pre-Owned Lost Case 3</a><b>:</b></li>
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
												<center><%=mybean.status%> Pre-Owned Lost Case 3</center>
											</div>
										</div>
										<div class="portlet-body portlet-empty">
											<div class="tab-pane" id="">
												<form name="form1" method="post" class="form-horizontal">
													<center>
														<font size="1">Form fields marked with a red asterisk
															<font color="red">*</font> are required.
														</font>
													</center><br>
													
													<div class="form-element6 form-element-center">
														<label>Pre Owned Pre Owned Lost Case2<font color="#ff0000">*</font>:</label>
															<select name="dr_preownedlostcase3_lostcase2_id" class="form-control" id="dr_preownedlostcase3_lostcase2_id">      
		                             						<%=mybean.PopulateLostCase2()%>
		                           							</select>
													</div>
													
													<div class="form-element6 form-element-center">
														<label>Name<font color="#ff0000">*</font>:</label>
															<input name ="txt_preownedlostcase3_name" type="txt_preownedlostcase3_name" class="form-control" 
															value="<%=mybean.preownedlostcase3_name%>" maxlength="255"/>
													</div>
												
							        				<div class="form-element6 form-element-center">
							                             <%if (mybean.status.equals("Update") && !(mybean.preownedlostcase3_entry_date == null) && !(mybean.preownedlostcase3_entry_date.equals(""))) {%>
							                            
							                            <div class="form-element6">
															<label>Entry By:</label>
															<%=mybean.unescapehtml(mybean.preownedlostcase3_entry_by)%>
							                      			<input name="entry_by" type="hidden" id="entry_by" value="<%=mybean.preownedlostcase3_entry_by%>">
														</div>
														
														<div class="form-element6">
															<label>Entry Date:</label>
															<%=mybean.preownedlostcase3_entry_date%>
							                      			<input type="hidden" id="entry_date" name="entry_date" value="<%=mybean.preownedlostcase3_entry_date%>">
														</div>	
																
							                            <%}%>
							                            
							                            <% if (mybean.status.equals("Update") && !(mybean.preownedlostcase3_modified_date == null) && !(mybean.preownedlostcase3_modified_date.equals(""))) {%>  
							                            <div class="form-element6">
															<label>Modified By:</label>
															<%=mybean.unescapehtml(mybean.preownedlostcase3_modified_by)%>
							                      			<input type="hidden" id="modified_by" name="modified_by" value="<%=mybean.preownedlostcase3_modified_by%>">
														</div>
														
														<div class="form-element6">
															<label>Modified Date:</label>
															<%=mybean.preownedlostcase3_modified_date%>
							                      			<input type="hidden" id="modified_date" name="modified_date" value="<%=mybean.preownedlostcase3_modified_date%>">
														</div>
														
							                            <%}%>
							                          
							                          </div>
							                          <div class="row"></div>
							                             <%if(mybean.status.equals("Add")){%>
							                  <center>
							                  <input name="addbutton" type="submit" class="btn btn-success" id="addbutton" value="Add Pre Owned Lost Case 3" onClick="return SubmitFormOnce(document.form1, this);" />
							                           <input type="hidden" name="add_button" value="yes">
							                            </center>
							                                <%}else if (mybean.status.equals("Update")){%>
							                                <center>
							                                <input type="hidden" name="update_button" value="yes">
							                                <input name="updatebutton" type="submit" class="btn btn-success" id="updatebutton" value="Update Pre Owned Lost Case 3" onclick="return SubmitFormOnce(document.form1,this);" />
							                                <input name="delete_button" type="submit" class="btn btn-success" id="delete_button" OnClick="return confirmdelete(this)" value="Delete Pre Owned Lost Case 3"/>
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

</body>
</html> 
                 
