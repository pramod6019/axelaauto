<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.CRMCustomField_Update"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<meta http-equiv="pragma" content="no-cache"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt"/>
<%@include file="../Library/css.jsp"%>
</HEAD>
<body onLoad="DisplayCustomfields();" leftmargin="0" rightmargin="0"
	topmargin="0" bottommargin="0">
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
				<div class="page-content-inner">
					<div class="container-fluid">
						<ul class="page-breadcrumb breadcrumb">
							<li><a href="../portal/home.jsp">Home</a> &gt;</li>
							<li><a href="../portal/manager.jsp">Business Manager</a> <%=mybean.LinkHeader%>
							<li><a href="crmcustomfield-list.jsp?crmdays_id=<%=mybean.crmdays_id%>" value="<%=mybean.brand_id%>">List Custom Fields</a> &gt;</li>
							<li><a href="crmcustomfield-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Custom Field</a><b>:</b></li>
						</ul>
						<!-- END PAGE BREADCRUMBS -->


						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<center>
								<font color="#ff0000"><b> <%=mybean.msg%> <br></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none"><%=mybean.status%>
										Custom Field
									</div>
								</div>
								
								<div class=" container-fluid portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<center>
											<font>Form fields marked with a red asterisk <b><font
													color="#ff0000">*</font></b> are required.<br>
											</font>
										</center>
										
										<form name="form1" class="form-horizontal" runat="server"
											method="post">

											<div class="form-element6">
												<label>Title<font color="#ff0000">*</font>: </label>
												<textarea name="txt_crmcf_title" id="txt_crmcf_title"
													cols="70" rows="4" class="form-control"
													onKeyUp="charcount('txt_crmcf_title', 'span_txt_crmcf_title','<font color=red>({CHAR} characters left)</font>', '1000')"><%=mybean.crmcf_title%></textarea>
												<span id=span_txt_crmcf_title> (1000 characters) </span>
											</div>



											<div class="form-element6">
												<label>Option: </label>
												<textarea name="txt_crmcf_option" id="txt_crmcf_option"
													cols="70" rows="4" class="form-control"
													onKeyUp="charcount('txt_crmcf_option', 'span_txt_crmcf_option','<font color=red>({CHAR} characters left)</font>', '5000')"><%=mybean.crmcf_option%></textarea>
												<span id=span_txt_crmcf_option> (5000 characters) </span>
											</div>


											<div class="form-element6">
												<label>Minimum Length :</label>
												 <input name="txt_crmcf_length_min" id="txt_crmcf_length_min"
													type="text" class="form-control"
													value="<%=mybean.crmcf_length_min%>"
													onKeyUp="toInteger('txt_crmcf_length_min')" size="10"
													maxlength="10" />
											</div>

											<div class="form-element6">
												<label>Maximum Length <font color="#ff0000">*</font>: </label>
												<input name="txt_crmcf_length_max"
													id="txt_crmcf_length_max" type="text" class="form-control"
													value="<%=mybean.crmcf_length_max%>"
													onKeyUp="toInteger('txt_crmcf_length_max')" size="10"
													maxlength="10" />
											</div>

											<div class="form-element6">
												<label>Reference Field: </label> 
												<input name="txt_crmcf_fieldref" id="txt_crmcf_fieldref"
													type="text" class="form-control"
													value="<%=mybean.crmcf_fieldref%>" size="50"
													maxlength="255" />

											</div>


											<div class="form-element6">
												<label>Type<font color="#ff0000">*</font>: </label>
												 <select name="dr_crmcf_cftype_id" id="dr_crmcf_cftype_id"
													class="form-control" onChange="DisplayCustomfields(); "><%=mybean.PopulateCfType()%>
												</select> 
											</div>



											<div class="form-element6">
												<label> Instruction: </label>
												<textarea name="txt_crmcf_instruction"
													id="txt_crmcf_instruction" cols="70" rows="4" class="form-control"
													onKeyUp="charcount('txt_crmcf_instruction', 'span_txt_crmcf_instruction','<font color=red>({CHAR} characters left)</font>', '1000')"><%=mybean.crmcf_instruction%></textarea>
												<span id=span_txt_crmcf_instruction> (1000 characters) </span>
											</div>



											<div class="form-element12">
												<div class="form-element4 form-element">
													<label>Numeric: </label> 
													<input type="checkbox" name="chk_crmcf_numeric" id="chk_crmcf_numeric"
														<%=mybean.PopulateCheck(mybean.crmcf_numeric)%> />
												</div>

												<div class="form-element4">
													<label>Unique: </label> 
													<input name="chk_crmcf_unique" id="chk_crmcf_unique" type="checkbox"
														<%=mybean.PopulateCheck(mybean.crmcf_unique)%> />
												</div>

												<div class="form-element4">
													<label>Mandatory: </label> 
													<input type="checkbox" name="chk_crmcf_mandatory" id="chk_crmcf_mandatory"
														<%=mybean.PopulateCheck(mybean.crmcf_mandatory)%> />
												</div>

											</div>
											
											<div class="form-element12">
												<div class="form-element4 form-element">
													<label>VOC: </label> 
													<input type="checkbox" name="chk_crmcf_voc" id="chk_crmcf_voc"
														<%=mybean.PopulateCheck(mybean.crmcf_voc)%> />
												</div>


												<div class="form-element4">
													<label>Print: </label> 
													<input type="checkbox" name="chk_crmcf_print" id="chk_crmcf_print"
														<%=mybean.PopulateCheck(mybean.crmcf_print)%> />
												</div>

												<div class="form-element4">
													<label>Active: </label> 
													<input type="checkbox" name="chk_crmcf_active" id="chk_crmcf_active"
														<%=mybean.PopulateCheck(mybean.crmcf_active)%> />
												</div>
											</div>

											

											<%
												if (mybean.status.equals("Update") && !(mybean.entry_by == null)
														&& !(mybean.entry_by.equals(""))) {
											%>
										<div class="form-element12">
											<div class="form-element6 form-element">
												<label>Entry By: </label>
												<%=mybean.unescapehtml(mybean.entry_by)%>
												<input name="entry_by" type="hidden" id="entry_by"
													value="<%=mybean.unescapehtml(mybean.entry_by)%>">
											</div>

											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_date == null)
														&& !(mybean.entry_date.equals(""))) {
											%>

											<div class="form-element6">
												<label>Entry Date:</label>
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
										<div class="form-element12">
											<div class="form-element6 form-element">
												<label>Modified By:</label>
												<%=mybean.unescapehtml(mybean.modified_by)%>
												<input name="modified_by" type="hidden" id="modified_by"
													value="<%=mybean.unescapehtml(mybean.modified_by)%>">
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update")
														&& !(mybean.modified_date == null)
														&& !(mybean.modified_date.equals(""))) {
											%>

											<div class="form-element6">
												<label>Modified Date:</label>
												<%=mybean.modified_date%>
												<input type="hidden" name="modified_date"
													value="<%=mybean.modified_date%>">
											</div>
										</div>
											<%
												}
											%>
											
											<%
												if (mybean.status.equals("Add")) {
											%>
											<div class="form-element12">
												<center>
													<input name="addbutton" type="submit"
														class="btn btn-success" id="addbutton" value="Add Custom Field"
														onClick="return SubmitFormOnce(document.form1, this);" />
													<input type="hidden" name="add_button" value="yes" />
												</center>
											</div>
											<%
												} else if (mybean.status.equals("Update")) {
											%>
											<div class="form-element12">
												<center>
													<input name="updatebutton" type="submit"
														class="btn btn-success" id="updatebutton" value="Update Custom Field"
														onClick="return SubmitFormOnce(document.form1, this);" />
													<input name="update_button" type="hidden" value="yes" />
													<input name="delete_button" type="submit" class="btn btn-success"
														id="delete_button" onClick="return confirmdelete(this)"
														value="Delete Custom Field" />
												</center>
											</div>
											<%
												}
											%>

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
	<%@include file="../Library/admin-footer.jsp"%></body>

	<%@include file="../Library/js.jsp"%>

<script type="text/javascript">
	function DisplayCustomfields() {
		var str = document.getElementById('dr_crmcf_cftype_id').value;
		//alert("str333"+str);

		if (str == "1") {
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
		} else if (str == "2") {
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
		} else if (str == "3") {
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
			document.getElementById("chk_crmcf_mandatory").value = '';
		} else if (str == "4") {
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
			document.getElementById("txt_crmcf_length_min").value = '';
			document.getElementById("txt_crmcf_length_max").value = '';
			document.getElementById("chk_crmcf_numeric").value = '';
		} else if (str == "5") {
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
			document.getElementById("chk_crmcf_numeric").value = '';
		} else if (str == "6") {
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
			document.getElementById("chk_crmcf_numeric").value = '';
		} else if (str == "7") {
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
			document.getElementById("chk_crmcf_numeric").value = '';
		}
	}
</script>
</HTML>
