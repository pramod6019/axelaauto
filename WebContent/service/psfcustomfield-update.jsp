<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.PSFCustomField_Update" scope="request" />
<%mybean.doGet(request, response);%>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>

<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<%@include file="../Library/css.jsp"%>
</head>
<body onLoad="DisplayCustomfields();" class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>
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
							Custom Field
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
							<li><a href="../portal/manager.jsp">Business Manager</a> <%=mybean.LinkHeader%>
								<a href="psfcustomfield-list.jsp?psfdays_id=<%=mybean.psfdays_id%>"
								value="<%=mybean.psfdays_brand_id%>">List Custom Fields</a> &gt;
								<a href="psfcustomfield-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Custom
									Field</a><b>:</b></li>
						</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="#ff0000"><b> <%=mybean.msg%> <br></b></font>
					</center>
				
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%>
										Custom Field
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<center>
											<font size="1">Form fields marked with a red asterisk
												<b><font color="#ff0000">*</font></b> are required.<br>
											</font><br>
										</center>
										<form name="form1" runat="server" method="post" class="form-horizontal">
											<div class="row">
											<div class="form-element6">
												<label>Title<font color=red>*</font>:</label>
													<textarea name="txt_jcpsfcf_title" id="txt_jcpsfcf_title"
														cols="70" rows="6" class="form-control"
														onKeyUp="charcount('txt_jcpsfcf_title', 'span_txt_jcpsfcf_title','<font color=red>({CHAR} characters left)</font>', '1000')"><%=mybean.jcpsfcf_title%></textarea>
													<br><span id=span_txt_jcpsfcf_title> (1000
															characters) </span>
											</div>
											
											<div class="form-element6">
												<label>Type<font color=red>*</font>:
												</label>
													<select name="dr_jcpsfcf_cftype_id"
														id="dr_jcpsfcf_cftype_id" class="form-control"
														onChange="DisplayCustomfields(); "><%=mybean.PopulateCfType()%></select>

											</div>
											
											<div class="form-element6" id="numeric">
												<label>Numeric: </label>
													<input type="checkbox" name="chk_jcpsfcf_numeric"
														id="chk_jcpsfcf_numeric"
														<%=mybean.PopulateCheck(mybean.jcpsfcf_numeric)%> />
											</div>
											</div>
											
											<div class="form-element6" id="minlength">
												<label>Minimum Length: </label>
													<input name="txt_jcpsfcf_length_min"
														id="txt_jcpsfcf_length_min" type="text"
														class="form-control"
														value="<%=mybean.jcpsfcf_length_min%>"
														onKeyUp="toInteger('txt_jcpsfcf_length_min')" 
														maxlength="10" />
											</div>
											
											<div class="form-element6" id="maxlength">
												<label>Maximun Length<font color="#ff0000">*</font>:</label>
													<input name="txt_jcpsfcf_length_max"
														id="txt_jcpsfcf_length_max" type="text"
														class="form-control"
														value="<%=mybean.jcpsfcf_length_max%>"
														onKeyUp="toInteger('txt_jcpsfcf_length_max')" 
														maxlength="10" />
											</div>
											
											<div class="form-element6" id="maxlength">
												<label>Reference Field: </label>
													<input name="txt_jcpsfcf_fieldref"
														id="txt_jcpsfcf_fieldref" type="text" class="form-control"
														value="<%=mybean.jcpsfcf_fieldref%>" 
														maxlength="255" />
											</div>
											
											<div class="form-element6" id="option">
												<label>Option: </label>
													<textarea name="txt_jcpsfcf_option" id="txt_jcpsfcf_option"
														cols="70" rows="4" class="form-control"
														onKeyUp="charcount('txt_jcpsfcf_option', 'span_txt_jcpsfcf_option','<font color=red>({CHAR} characters left)</font>', '5000')"><%=mybean.jcpsfcf_option%></textarea>
													<br><span id=span_txt_jcpsfcf_option> (5000
															characters) </span>
											</div>
											
											<div class="form-element6 form-element-margin" id="unique">
												<label>Unique: </label>
													<input name="chk_jcpsfcf_unique" id="chk_jcpsfcf_unique"
														type="checkbox"
														<%=mybean.PopulateCheck(mybean.jcpsfcf_unique)%> />
											</div>
											
											<div class='row'></div>
											
											
											
											<div class="form-element6">
												<label>Instruction: </label>
													<textarea name="txt_jcpsfcf_instruction"
														id="txt_jcpsfcf_instruction" cols="70" rows="4"
														class="form-control"
														onKeyUp="charcount('txt_jcpsfcf_instruction', 'span_txt_jcpsfcf_instruction','<font color=red>({CHAR} characters left)</font>', '1000')"><%=mybean.jcpsfcf_instruction%></textarea>
													<br><span id=span_txt_jcpsfcf_instruction>
															(1000 characters) </span>
											</div>
											
											<div class="form-element6 form-element-margin" id="mandatory">
												<label>Mandatory: </label>
													<input type="checkbox" name="chk_jcpsfcf_mandatory"
														id="chk_jcpsfcf_mandatory"
														<%=mybean.PopulateCheck(mybean.jcpsfcf_mandatory)%> />
											</div>
											
											<div class="form-element6 form-element-margin ">
												<label>VOC: </label>
													<input type="checkbox" name="chk_jcpsfcf_voc"
														id="chk_jcpsfcf_voc"
														<%=mybean.PopulateCheck(mybean.jcpsfcf_voc)%> />
											</div>
											
											<div class='row'></div>
											
											<div class="form-element6">
												<label>Print: </label>
													<input type="checkbox" name="chk_jcpsfcf_print"
														id="chk_jcpsfcf_print"
														<%=mybean.PopulateCheck(mybean.jcpsfcf_print)%> />
											</div>
											
											<div class="form-element6">
												<label>Active: </label>
													<input type="checkbox" name="chk_jcpsfcf_active"
														id="chk_jcpsfcf_active"
														<%=mybean.PopulateCheck(mybean.jcpsfcf_active)%> />
											</div>
											
											<%if (mybean.status.equals("Update") && !(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) {%>
											
											<div class="form-element6">
												<label>Entry By: </label>
													<%=mybean.unescapehtml(mybean.entry_by)%>
													<input name="entry_by" type="hidden" id="entry_by"
														value="<%=mybean.unescapehtml(mybean.entry_by)%>">
											</div>
											
											<%}%>
											<%if (mybean.status.equals("Update") && !(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) {%>
											
											<div class="form-element6">
												<label>Entry Date: </label>
													<%=mybean.entry_date%>
													<input type="hidden" name="entry_date"
														value="<%=mybean.entry_date%>">
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
												<label>Modified Date:</label>
													<%=mybean.modified_date%>
													<input type="hidden" name="modified_date"
														value="<%=mybean.modified_date%>">
											</div>
											<%}%>
											
											<div class="row"></div>
											
											<center>
												<%if (mybean.status.equals("Add")) {%>
												<input name="addbutton" type="submit"
													class="btn btn-success" id="addbutton"
													value="Add Custom Field"
													onClick="return SubmitFormOnce(document.form1, this);" /> <input
													type="hidden" name="add_button" value="yes"> <%
 														} else if (mybean.status.equals("Update")) { %>
													<input name="updatebutton" type="submit"
													class="btn btn-success" id="updatebutton"
													value="Update Custom Field"
													onClick="return SubmitFormOnce(document.form1, this);" /> <input
													name="update_button" type="hidden" value="yes" /> <input
													name="delete_button" type="submit" class="btn btn-success"
													id="delete_button" OnClick="return confirmdelete(this)"
													value="Delete Custom Field" /> <%}%>
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
	<script>
	 function DisplayCustomfields() {
		var str=document.getElementById('dr_jcpsfcf_cftype_id').value;
		//alert("str333----"+str);
		
		if(str=="1")
		{
			$('#numeric').show();
			$('#minlength').show();
			$('#maxlength').show();
			$('#mandatory').show();
			$('#unique').show();
			$('#option').hide();
		} 
		else if(str=="2")
		{
			$('#minlength').show();
			$('#maxlength').show();
			$('#mandatory').show();
			$('#numeric').hide();
			$('#option').hide();
			$('#unique').hide();
			
		} 
		else if(str=="3")
		{
			$('#numeric').hide();
			$('#minlength').hide();
			$('#maxlength').hide();
			$('#unique').hide();
			$('#option').hide();
			$('#mandatory').hide();
			document.getElementById("chk_jcpsfcf_mandatory").value='';
		}
		else if(str=="4")
		{
		 	//alert("str"+str);
			$('#option').show();
			$('#mandatory').show();
			$('#unique').show();
			$('#minlength').hide();
			$('#maxlength').hide();
			$('#numeric').hide();
			document.getElementById("txt_jcpsfcf_length_min").value='';
			document.getElementById("txt_jcpsfcf_length_max").value='';
			document.getElementById("chk_jcpsfcf_numeric").value='';
		} 
		else if(str=="5")
		{
		 	//alert("str"+str);
			$('#mandatory').show();
			$('#unique').show();
			$('#option').hide();
			$('#numeric').hide();
			$('#minlength').hide();
			$('#maxlength').hide();
			document.getElementById("chk_jcpsfcf_numeric").value='';
		}
		else if(str=="6")
		{
			$('#mandatory').show();
			$('#unique').show();
			$('#option').hide();
			$('#numeric').hide();
			$('#minlength').hide();
			$('#maxlength').hide();
			document.getElementById("chk_jcpsfcf_numeric").value='';
		}
		else if(str=="7")
		{
			$('#mandatory').show();
			$('#unique').show();
			$('#option').hide();
			$('#numeric').hide();
			$('#minlength').hide();
			$('#maxlength').hide();
			document.getElementById("chk_jcpsfcf_numeric").value='';
		}
	} 
	</script>

</body>
</html>
