<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.preowned.ManagePreownedCRMCustomField_Update" scope="request"/>
<%mybean.doGet(request,response); %>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="pragma" content="no-cache">

<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp"%>

</head>
<body  onLoad="DisplayCustomfields();" leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
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
						<h1><%=mybean.status%> Custom Field</h1>
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
							<li><a href="../portal/manager.jsp">Business Manager</a>
	                            <%=mybean.LinkHeader%>
	                  <a href="managepreownedcrmcustomfield-list.jsp?precrmfollowupdays_id=<%=mybean.precrmfollowupdays_id%>"
	                   value="<%=mybean.brand_id%>">List Custom Fields</a> &gt;</li>
							<li><a href="managepreownedcrmcustomfield-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Custom Field</a><b>:</b></li>
						
						</ul>
					<!-- END PAGE BREADCRUMBS -->
					
					
					<div class="tab-pane" id="">
<!-- 					BODY START -->
                          <center><font color="#ff0000"><b> <%=mybean.msg%> <br></b></font></center>
							<div class="portlet box">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none"><%=mybean.status%> Custom Field</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<center><font size="1">Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b> are required.
                                         </font></center><br>
                                        <form name="form1" class="form-horizontal" runat="server" method="post">
												<div class="row">
												<div class="form-element6">
													<label>Title<font color="#ff0000">*</font>:</label>
														<textarea name="txt_precrmcf_title" id="txt_precrmcf_title"
															cols="70" rows="6" class="form-control"
															onKeyUp="charcount('txt_precrmcf_title', 'span_txt_precrmcf_title','<font color=red>({CHAR} characters left)</font>', '1000')"><%=mybean.precrmcf_title%></textarea>
														<br><span id=span_txt_precrmcf_title> (1000 characters) </span>
												</div>
											
												<div class="form-element6">
													<label>Type<font color="#ff0000">*</font>:</label>
													<select name="dr_precrmcf_cftype_id"
															id="dr_precrmcf_cftype_id" class="form-control"
															onChange="DisplayCustomfields(); "><%=mybean.PopulateCfType()%></select>

												</div>
											
												<div class="form-element6" id="numeric">
													<label>Numeric: </label>

														<input type="checkbox" name="chk_precrmcf_numeric" id="chk_precrmcf_numeric"
															<%=mybean.PopulateCheck(mybean.precrmcf_numeric)%> />
												</div>
												</div>
												
												<div class="form-element6" id="minlength">
													<label>Minimum Length:</label>
														<input name="txt_precrmcf_length_min" id="txt_precrmcf_length_min" type="text" class="form-control"
															value="<%=mybean.precrmcf_length_min%>"
															onKeyUp="toInteger('txt_precrmcf_length_min')" 
															maxlength="10" />
												</div>
												
												<div class="form-element6" id="maxlength">
													<label>Maximum Length<font color="#ff0000">*</font>:</label>
														<input name="txt_precrmcf_length_max"
															id="txt_precrmcf_length_max" type="text" class="form-control"
															value="<%=mybean.precrmcf_length_max%>"
															onKeyUp="toInteger('txt_precrmcf_length_max')"
															maxlength="10" />
												</div>
												
												<div class="form-element6" id="maxlength">
													<label>Reference Field:</label>
													 <input name ="txt_precrmcf_fieldref" id="txt_precrmcf_fieldref" type="text" class="form-control" value="<%=mybean.precrmcf_fieldref%>" maxlength="255"/>
												</div>
												
												<div class="form-element6" id="option">
													<label>Option:</label>
                     									 <textarea name="txt_precrmcf_option" id="txt_precrmcf_option" cols="70" rows="4" class="form-control" onKeyUp="charcount('txt_precrmcf_option', 'span_txt_precrmcf_option','<font color=red>({CHAR} characters left)</font>', '5000')"><%=mybean.precrmcf_option%></textarea><br><span id=span_txt_precrmcf_option> (5000 characters) </span></td> 
												</div>

												<div class="form-element6 form-element-margin" id="unique">
													<label>Unique: </label>
                   										 <input name ="chk_precrmcf_unique" id="chk_precrmcf_unique" type="checkbox" <%=mybean.PopulateCheck(mybean.precrmcf_unique)%> />
												</div>
												
												<div class='row'></div>
												
												<div class="form-element6">
													<label>Instruction:</label>
            											<textarea name="txt_precrmcf_instruction" id="txt_precrmcf_instruction" cols="70" rows="4" class="form-control" onKeyUp="charcount('txt_precrmcf_instruction', 'span_txt_precrmcf_instruction','<font color=red>({CHAR} characters left)</font>', '1000')"><%=mybean.precrmcf_instruction%></textarea>
                            							<br><span id=span_txt_precrmcf_instruction> (1000 characters) </span>                     			
												</div>
												
												
												<div class="form-element6 form-element-margin" id="mandatory">
													<label>Mandatory:</label>
             											<input type="checkbox" name="chk_precrmcf_mandatory" id="chk_precrmcf_mandatory" <%=mybean.PopulateCheck(mybean.precrmcf_mandatory)%> /></td>
												</div>
											
												
												
											
												<div class="form-element6 form-element-margin" id="mandatory">
													<label>VOC:</label>
            											 <input type="checkbox" name="chk_precrmcf_voc" id="chk_precrmcf_voc" <%=mybean.PopulateCheck(mybean.precrmcf_voc)%> /></td>
												</div>
												
												<div class='row'></div>
												<div class="form-element6">
													<label>Print:</label>
                  										<input type="checkbox" name="chk_precrmcf_print" id="chk_precrmcf_print" <%=mybean.PopulateCheck(mybean.precrmcf_print)%> />
												</div>
												
												<div class="form-element6">
													<label>Active:</label>
        												<input type="checkbox" name="chk_precrmcf_active" id="chk_precrmcf_active" <%=mybean.PopulateCheck(mybean.precrmcf_active)%>/>
												</div>
												
                                      <% if (mybean.status.equals("Update")&& !(mybean.entry_by == null) && !(mybean.entry_by.equals("")) ) { %>
												<div class="form-element6">
													<label>Entry By: </label>
														<%=mybean.unescapehtml(mybean.entry_by)%>
                                						<input name="entry_by" type="hidden" id="entry_by" value="<%=mybean.unescapehtml(mybean.entry_by)%>">
												</div>
                            		<%}%>
				                            <% if (mybean.status.equals("Update") && !(mybean.entry_date == null) && !(mybean.entry_date.equals("")) ) { %>
																<div class="form-element6">
																	<label>Entry Date:</label>
																	<%=mybean.entry_date%>
				                                					<input type="hidden" name="entry_date" value="<%=mybean.entry_date%>">
																</div>
				                           
				                            <%}%>
				                            <% if (mybean.status.equals("Update") && !(mybean.modified_by == null) && !(mybean.modified_by.equals("")) ) { %>
																<div class="form-element6">
																	<label>Modified By:</label>
																	<%=mybean.unescapehtml(mybean.modified_by)%>
				                                					<input name="modified_by" type="hidden" id="modified_by" value="<%=mybean.unescapehtml(mybean.modified_by)%>">
																</div>
				                            <%}%>
				                            <% if (mybean.status.equals("Update") && !(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) { %>
																<div class="form-element6">
																	<label>Modified Date:</label>
																	<%=mybean.modified_date%>
				                                					<input type="hidden" name="modified_date" value="<%=mybean.modified_date%>">
																</div>
				                            
				                            <%}%>
				                            <%if(mybean.status.equals("Add")){%>
				                                <center>
				                                	<input name="addbutton" type="submit" class="btn btn-success" id="addbutton" value="Add Custom Field" onClick="return SubmitFormOnce(document.form1, this);"/>     
				                                	<input type="hidden" name="add_button" value="yes"/>
				                                </center>   
				                                	<%}else if (mybean.status.equals("Update")){%>
				                                <center>
					                                <input name="updatebutton" type="submit" class="btn btn-success" id="updatebutton" value="Update Custom Field" onClick="return SubmitFormOnce(document.form1, this);"/>             
					                                <input name="update_button" type="hidden" value="yes" />
					                                <input name="delete_button" type="submit" class="btn btn-success" id="delete_button" OnClick="return confirmdelete(this)" value="Delete Custom Field" />
					                            </center>
				                                <%}%>
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
 
</script>

<script type="text/javascript">

	function DisplayCustomfields() {
		var str=document.getElementById('dr_precrmcf_cftype_id').value;
		//alert("str333"+str);
		
		if(str=="1")
		{
			$('#chequeno').hide();
			$("#numeric").show();
			$("#minlength").show();
			$("#maxlength").show();
			$("#mandatory").show();
			$("#unique").show();
			$("#option").hide();
// 			displayRow('numeric');
// 			displayRow('minlength');
// 			displayRow('maxlength');
// 			displayRow('mandatory');
// 			displayRow('unique');
// 			hideRow('option');
		} 
		else if(str=="2")
		{
			$("#minlength").show();
			$("#maxlength").show();
			$("#mandatory").show();
// 			displayRow('minlength');
// 			displayRow('maxlength');
// 			displayRow('mandatory');
            $("#numeric").hide();
            $("#option").hide();
            $("#unique").hide();
// 			hideRow('numeric');
// 			hideRow('option');
// 			hideRow('unique');
		} 
		else if(str=="3")
		{
			$("#numeric").hide();
            $("#minlength").hide();
            $("#maxlength").hide();
            $("#unique").hide();
            $("#option").hide();
            ("#mandatory").hide();
// 			hideRow('numeric');
// 			hideRow('minlength');
// 			hideRow('maxlength');
// 			hideRow('unique');
// 			hideRow('option');
// 			hideRow('mandatory');
			document.getElementById("chk_precrmcf_mandatory").value='';
		}
		else if(str=="4")
		{
		 	//alert("str"+str);
		 	$("#option").show();
		 	$("#mandatory").show();
			$("#unique").show();
			$("#minlength").hide();
			$("#maxlength").hide();
			$("#numeric").hide();
// 			displayRow('option');
// 			displayRow('mandatory');
// 			displayRow('unique');
// 			hideRow('minlength');
// 			hideRow('maxlength');
// 			hideRow('numeric');
			document.getElementById("txt_precrmcf_length_min").value='';
			document.getElementById("txt_precrmcf_length_max").value='';
			document.getElementById("chk_precrmcf_numeric").value='';
		} 
		else if(str=="5")
		{
		 	//alert("str"+str);
		 	$("#mandatory").show();
			$("#unique").show();
			$("#option").hide();
			$("#numeric").hide();
			$("#minlength").hide();
			$("#maxlength").hide();
// 			displayRow('mandatory');
// 			displayRow('unique');
// 			hideRow('option');
// 			hideRow('numeric');
// 			hideRow('minlength');
// 			hideRow('maxlength');
			document.getElementById("chk_precrmcf_numeric").value='';
		}
		else if(str=="6")
		{
			$("#mandatory").show();
			$("#unique").show();
// 			displayRow('mandatory');
// 			displayRow('unique');
$("#option").hide();
$("#numeric").hide();
$("#minlength").hide();
$("#maxlength").hide();
// 			hideRow('option');
// 			hideRow('numeric');
// 			hideRow('minlength');
// 			hideRow('maxlength');
			document.getElementById("chk_precrmcf_numeric").value='';
		}
		else if(str=="7")
		{
			("#mandatory").show();
			("#unique").show();
// 			displayRow('mandatory');
// 			displayRow('unique');
("#option").hide();
("#numeric").hide();
("#minlength").hide();
("#maxlength").hide();
// 			hideRow('option');
// 			hideRow('numeric');
// 			hideRow('minlength');
// 			hideRow('maxlength');
			document.getElementById("chk_precrmcf_numeric").value='';
		}
	}
	</script>
	</body>
</html>
