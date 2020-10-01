<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.accounting.Group_Update" scope="request" />
<% mybean.doGet(request, response); %>

<div id="reload">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal">&times;</button>
		<h4 class="modal-title">
			<b><%=mybean.status%>&nbsp;Group </b>
		</h4>
	</div>
	<div class="modal-body">
<!-- 		<form name="form1"> -->
			<center>
				<font color="#ff0000"><%=mybean.msg%></font></b></font>
			</center>
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
			<div class="page-content-inner">
				<div class="container-fluid">

						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<center>
								<font color="#ff0000"><b><span id="error"><%=mybean.msg%></span></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none"> <%=mybean.status%>&nbsp;Group
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="container-fluid">
										<!-- START PORTLET BODY -->
										<form name="form1" method="post" class="form-horizontal">
											<center>
												<font>Form fields marked with a red asterisk <font
													color=#ff0000>*</font> are required.
												</font>
											</center>
												<div class="form-element12">
													<label>Parent Group<font color="#ff0000">*</font>:
													</label> <select id="dr_accgroup_parent_id"
														name="dr_accgroup_parent_id" class="form-control">
														<%=mybean.PopulateParentGroup(mybean.comp_id, "1", mybean.accgroup_parent_id, mybean.accgroup_type)%>
													</select>
												</div>
												<div class="form-element12">
														<label>Group<font color="#ff0000">*</font>: </label>
															<input name="txt_group_name" id="txt_group_name" type="text" class="form-control"
																value="<%=mybean.accgroup_name %>" size="50" maxlength="255" />
												</div>
												
												<div class="form-element12">
														<label>Description: </label>
															<textarea name="txt_descreption" rows="4" class="form-control" id="txt_descreption" onKeyUp="charcount('txt_descreption', 'span_txt_descreption', '<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.descreption%></textarea>
															<span id="span_txt_descreption"> (255 Characters)</span>
												</div>
												<div class="form-element12">

												<% if (mybean.status.equals("Update")&& !(mybean.entry_by == null) && !(mybean.entry_by.equals("")) ) { %>

												<div class="form-element12">
														<label>Entry By: <%=mybean.unescapehtml(mybean.entry_by)%></label>
															<input name="entry_by" type="hidden" id="entry_by"
																value="<%=mybean.entry_by%>">
														</div>
												<%}%>
												<% if (mybean.status.equals("Update") && !(mybean.entry_date == null) && !(mybean.entry_date.equals("")) ) { %>
												<div class="form-element12">
														<label>Entry Date : <%=mybean.entry_date%></label>
															<input type="hidden" name="entry_date"
																value="<%=mybean.entry_date%>">
														</div>
												<%}%>
												<% if (mybean.status.equals("Update") && !(mybean.modified_by == null) && !(mybean.modified_by.equals("")) ) { %>
												<div class="form-element12">
														<label>Modified By : </label>
															<%=mybean.unescapehtml(mybean.modified_by)%>
															<input name="modified_by" type="hidden" id="modified_by"
																value="<%=mybean.modified_by%>">
														</div>
												<%}%>
												<% if (mybean.status.equals("Update") && !(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) { %>
												<div class="form-element12">
														<label>Modified date: </label>
															<%=mybean.modified_date%>
															<input type="hidden" name="modified_date"
																value="<%=mybean.modified_date%>">
												</div>
												<%}%>
												<div class="form-element12">
															<center>
																<%if(mybean.status.equals("Add")){%>

																	<input name="addbutton" type="button" class="btn btn-success" id="addbutton" value="Add Group" />
																	
																	<input type="hidden" name="add_button" value="yes">
																
																<%}else if (mybean.status.equals("Update")){%>
																	
																	<input type="hidden" name="update_button" value="yes">
																	
																	<input id='accgroup_id' name='accgroup_id' value='<%=mybean.accgroup_id%>' hidden />
																	<input name="updatebutton" type="button" class="btn btn-success" id="updatebutton" value="Update Group" />
																		
																	<input name="delete_button" type="button" class="btn btn-success" id="delete_button" value="Delete Group" />
																	
																<%}%>
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
</div>
</div>
<script type="application/javascript">
	
	$(document).ready(function(){
		
		$('#addbutton').click(function(){
			var txt_group_name = document.getElementById('txt_group_name').value;
			var dr_accgroup_parent_id = document.getElementById('dr_accgroup_parent_id').value;
			var txt_descreption = document.getElementById('txt_descreption').value;
			
			showHintAcc('../accounting/group-check.jsp?add_button=yes&add=yes'
					+'&txt_group_name='+txt_group_name
					+'&dr_accgroup_parent_id=' +dr_accgroup_parent_id
					+'&txt_descreption='+txt_descreption,'error');
		});
		
		$('#updatebutton').click(function(){
			var txt_group_name = document.getElementById('txt_group_name').value;
			var dr_accgroup_parent_id = document.getElementById('dr_accgroup_parent_id').value;
			var accgroup_id= document.getElementById('accgroup_id').value;
			var txt_descreption = document.getElementById('txt_descreption').value;
			
			showHintAcc('../accounting/group-check.jsp?update_button=yes&update=yes&accgroup_id='+accgroup_id
					+'&txt_group_name='+txt_group_name
					+'&dr_accgroup_parent_id='+dr_accgroup_parent_id
					+'&txt_descreption='+txt_descreption ,'error');
		});
		
		$('#delete_button').click(function(){
			var accgroup_id= document.getElementById('accgroup_id').value;
			showHintAcc("../accounting/group-check.jsp?delete_button=Delete Group&update=yes&accgroup_id="+accgroup_id,'error');
		});
		
	});
	
	</script>

</body>
</HTML>
