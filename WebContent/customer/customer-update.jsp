<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.customer.Customer_Update" scope="request" />
<% mybean.doGet(request, response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp"%>
<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		document.formcontact.txt_customer_name.focus();
	}
</script>
<!-- <script> -->
<!-- // $("#txt_customer_since")({ // todayHighlight:true; // }) -->
<!-- </script> -->
</HEAD>
<body onLoad="FormFocus();HideAsterisk();" class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>
							<%=mybean.status%>
							<%=mybean.tag%></h1>
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
							<li><a href="customer.jsp">Customers</a> &gt;</li>
							<li><a href="customer-list.jsp?all=recent">List <%=mybean.tag%></a> &gt;</li>
							<li><a href="customer-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>
									<%=mybean.tag%></a><b>:</b></li>
						</ul>
						<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%>&nbsp;<%=mybean.tag%>
									</div>
								</div>

								<div class="portlet-body portlet-empty">
									<div class="container-fluid">
										<!-- START PORTLET BODY -->
										<form name="formcontact" method="post" class="form-horizontal">

											<div class="form-element6">
												<label> Name<font color=red>*</font>:&nbsp; </label>
												<input name="txt_customer_name" type="text"
													class="form-control" id="txt_customer_name"
													value="<%=mybean.customer_name%>" size="50" maxlength="255"><font
													color="#ff0000"><span id="txtHint"></span></font>
											</div>
											<div class="form-element6">
												<label>Alias:&nbsp; </label>
												<input name="txt_customer_alias" type="text"
													class="form-control" id="txt_customer_alias"
													value="<%=mybean.customer_alias%>" size="50" maxlength="255"><font
													color="#ff0000"></font>
											</div>

											<div class="form-element6">
												<label> <%=mybean.tag%> Code:&nbsp; </label>
												<input name="txt_customer_code" type="text"
													class="form-control" id="txt_customer_code"
													value="<%=mybean.customer_code%>" size="32" maxlength="20">
											</div>

											<div class="form-element6">
												<label>Branch :&nbsp; </label> 
												<select name="dr_customer_branch_id" class="form-control"
													id="dr_customer_branch_id" onChange="Displaypaymode();">
													<%=mybean.PopulateBranch(mybean.customer_branch_id, "", "", "", request)%>
												</select>
											</div>

											<div class="form-element3">
												<label> Mobile 1<font color=red>*</font>:&nbsp; </label>
												<input name="txt_customer_mobile1" type="text"
													class="form-control" id="txt_customer_mobile1"
													value="<%=mybean.customer_mobile1%>" size="32"
													maxlength="13"
													onKeyUp="toPhone('txt_customer_mobile1','Mobile 1')">
												(91-9999999999)
											</div>

											<div class="form-element3">
												<label> Mobile 2:&nbsp;</label> 
												<input name="txt_customer_mobile2" type="text"
													class="form-control" id="txt_customer_mobile2"
													value="<%=mybean.customer_mobile2%>" size="32"
													maxlength="13"
													onKeyUp="toPhone('txt_customer_mobile2','Mobile 2')">
												(91-9999999999)
											</div>

											<div class="form-element3">
												<label> Mobile 3:&nbsp;</label> 
												<input name="txt_customer_mobile3" type="text"
													class="form-control" id="txt_customer_mobile3"
													value="<%=mybean.customer_mobile3%>" size="32"
													maxlength="13"
													onKeyUp="toPhone('txt_customer_mobile3','Mobile 3')">
												(91-9999999999)
											</div>

											<div class="form-element3">
												<label> Mobile 4:&nbsp;</label> 
												<input name="txt_customer_mobile4" type="text"
													class="form-control" id="txt_customer_mobile4"
													value="<%=mybean.customer_mobile4%>" size="32"
													maxlength="13"
													onKeyUp="toPhone('txt_customer_mobile4','Mobile 4')">
												(91-9999999999)
											</div>

											<div class="form-element3">
												<label> Mobile 5:&nbsp;</label> 
												<input name="txt_customer_mobile5" type="text"
													class="form-control" id="txt_customer_mobile5"
													value="<%=mybean.customer_mobile5%>" size="32"
													maxlength="13"
													onKeyUp="toPhone('txt_customer_mobile5','Mobile 5')">
												(91-9999999999)
											</div>

											<div class="form-element3">
												<label> Mobile 6:&nbsp;</label> 
												<input name="txt_customer_mobile6" type="text"
													class="form-control" id="txt_customer_mobile6"
													value="<%=mybean.customer_mobile6%>" size="32"
													maxlength="13"
													onKeyUp="toPhone('txt_customer_mobile6','Mobile 6')">
												(91-9999999999)
											</div>


											<div class="form-element3">
												<label> Phone 1:&nbsp;</label> 
												<input name="txt_customer_phone1" type="text" class="form-control"
													id="txt_customer_phone1"
													value="<%=mybean.customer_phone1%>" size="32"
													maxlength="15"
													onKeyUp="toPhone('txt_customer_phone1','Phone 1')">
												(91-80-33333333)
											</div>

											<div class="form-element3">
												<label> Phone 2:&nbsp;</label> 
												<input name="txt_customer_phone2" type="text" class="form-control"
													id="txt_customer_phone2"
													value="<%=mybean.customer_phone2%>" size="32"
													maxlength="15"
													onKeyUp="toPhone('txt_customer_phone2','Phone 2')">
												(91-80-33333333)
											</div>

											<div class="form-element3">
												<label> Phone 3:&nbsp;</label> 
												<input name="txt_customer_phone3" type="text" class="form-control"
													id="txt_customer_phone3"
													value="<%=mybean.customer_phone3%>" size="32"
													maxlength="15"
													onKeyUp="toPhone('txt_customer_phone3','Phone 3')">
												(91-80-33333333)
											</div>

											<div class="form-element3">
												<label> Phone 4:&nbsp;</label> 
												<input name="txt_customer_phone4" type="text" class="form-control"
													id="txt_customer_phone4"
													value="<%=mybean.customer_phone4%>" size="32"
													maxlength="15"
													onKeyUp="toPhone('txt_customer_phone4','Phone 4')">
												(91-80-33333333)
											</div>

											<div class="form-element3">
												<label> Fax 1:&nbsp;</label> 
												<input name="txt_customer_fax1"
													type="text" class="form-control" id="txt_customer_fax1"
													value="<%=mybean.customer_fax1%>" size="32" maxlength="50">
											</div>

											<div class="form-element3">
												<label> Fax 2:&nbsp;</label> 
												<input name="txt_customer_fax2"
													type="text" class="form-control" id="txt_customer_fax2"
													value="<%=mybean.customer_fax2%>" size="32" maxlength="50">
											</div>
                          <div class="row"></div>
											<div class="form-element6">
												<label> Email 1:&nbsp;</label> 
												<input name="txt_customer_email1" type="text" class="form-control"
													id="txt_customer_email1"
													value="<%=mybean.customer_email1%>" size="40"
													MaxLength="100">
											</div>
											<div class="form-element6">
												<label> Email 2:&nbsp;</label> 
												<input name="txt_customer_email2" type="text" class="form-control"
													id="txt_customer_email2"
													value="<%=mybean.customer_email2%>" size="40"
													MaxLength="100">
											</div>

											<div class="form-element6">
												<label>Website 1:&nbsp;</label> 
												<input name="txt_customer_website1" type="text"
													class="form-control" id="txt_customer_website1"
													value="<%=mybean.customer_website1%>" size="40"
													MaxLength="100">
											</div>

											<div class="form-element6">
												<label>Website 2:&nbsp;</label> 
												<input name="txt_customer_website2" type="text"
													class="form-control" id="txt_customer_website2"
													value="<%=mybean.customer_website2%>" size="40"
													MaxLength="100">
											</div>

											<div class="form-element6">
												<label>GSTIN: </label>
												<b>
												<input name="txt_customer_gst_no" type="text"
													class="form-control" id="txt_customer_gst_no"
													value="<%=mybean.customer_gst_no%>" size="50"
													maxlength="15"></input></b><b>Example: 22AAAAA0000A1Z5</b> 
												<span id="gst"> </span>
											</div>

											<div class="form-element6">
												<label>GSTIN Date:</label>
												<input name="txt_customer_gst_regdate"
													id="txt_customer_gst_regdate"
													value="<%=mybean.gst_regdate%>"
													class="form-control datepicker" type="text" maxlength="10" />
											</div>

											<div class="row"></div>
											<div class="form-element6">
												<label>ARN:&nbsp; </label> <b>
												<input name="txt_customer_arn_no" type="text" class="form-control"
													id="txt_customer_arn_no"
													value="<%=mybean.customer_arn_no%>" size="10"
													maxlength="15"></input></b><b>Example: AA0707160000001</b>
													<span id="arn"></span>
											</div>

											<div class="form-element6">
												<label>Status<font color=red>*</font>:&nbsp;
												</label> </label> 
												<select name="dr_customer_itstatus_id"
													class="form-control" id="dr_customer_itstatus_id"
													onchange="HideAsterisk();">
													<%=mybean.PopulateItStatus()%>
												</select>
											</div>

											<div class="row"></div>
											
											<div class="form-element6">
												<label> Address<font color=red>*</font>:&nbsp; </label>
												<textarea name="txt_customer_address" cols="40" rows="4"
													class="form-control" id="txt_customer_address"
													onKeyUp="charcount('txt_customer_address', 'span_txt_customer_address','<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.customer_address%></textarea>
												<span id="span_txt_customer_address"> (255 Characters)</span>
											</div>
											
											<div class="form-element6">
												<label> Landmark:&nbsp;</label>
												<textarea name="txt_customer_landmark" cols="40" rows="4"
													class="form-control" id="txt_customer_landmark"
													onKeyUp="charcount('txt_customer_landmark', 'span_txt_customer_landmark','<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.customer_landmark%></textarea>
												<span id="span_txt_customer_landmark"> (255 Characters)</span>
											</div>
											<div class="form-element6">
												<label> City<font color=red>*</font>:&nbsp; </label>
												<select class="form-control-select select2" id="maincity"
													name="maincity">
													<%=mybean.citycheck.PopulateCities(mybean.customer_city_id, mybean.comp_id)%>
												</select>
											</div>
											
											<div class="form-element6">
												<label> Pin/Zip<font color=red>*</font>:&nbsp; </label>
												<input name="txt_customer_pin" type="text"
													class="form-control" id="txt_customer_pin"
													onKeyUp="toInteger('txt_customer_pin','Pin')"
													value="<%=mybean.customer_pin%>" size="10" maxlength="6" />
											</div>
											
											<div class="form-element1 form-element-margin">
												<label> TDS:&nbsp;</label> 
												<input id="chk_customer_tds"
													type="checkbox" name="chk_customer_tds"
													<%=mybean.PopulateCheck(mybean.customer_tds)%> />
											</div>
											
											<div class="form-element5">
												<label>PAN:&nbsp;</label> 
												<input name="txt_customer_pan_no" type="text" class="form-control"
													id="txt_customer_pan_no"
													value="<%=mybean.customer_pan_no%>" size="10"
													maxlength="10"></input><b>Format: ABCDF1234F</b>
													<span id="gst"></span>
											</div>
											
											<%
												if (mybean.config_customer_soe.equals("1")) {
											%>

											<div class="form-element6">
												<label> Source Of Enquiry<font color=red>*</font>:&nbsp; </label>
												<select name="drop_customer_soe_id" class="form-control"
													id="drop_customer_soe_id">
													<%=mybean.PopulateSoe()%>
												</select>

											</div>
											<%
												}
											%>
											<div class="row"></div>
											<%
												if (mybean.config_customer_sob.equals("1")) {
											%>
											<div class="form-element6">
												<label> Source of Business<font color=red>*</font>:&nbsp; </label>
												<select name="drop_customer_sob_id" class="form-control"
													id="drop_customer_sob_id">
													<%=mybean.PopulateSob()%>
												</select>
											</div>
											<%
													}
												%>
											<div class="form-element6">
												<label>Customer Since :&nbsp; </label>
												 <input name="txt_customer_since" id="txt_customer_since"
													value="<%=mybean.customersince%>"
													class="form-control datepicker" type="text" value="" />
											</div>
											
											<div class="row"></div>
											
											<div class="form-element6">
												<label> Type<font color=red>*</font>:&nbsp; </label>
												<select name="dr_customer_type" class="form-control"
													id="dr_customer_type">
													<%=mybean.PopulateType()%>
												</select>
											</div>

											<div class="form-element6">
												<label> Executive:&nbsp; </label> 
												<select name="dr_customer_emp_id" class="form-control"
													id="dr_customer_emp_id">
													<%=mybean.PopulateEmp()%>
												</select>
											</div>

											<div class="form-element4">
												<label> Select Groups:&nbsp;</label> 
												<select name="fo_group" size="10" multiple="multiple" class="form-control" id="fo_group">
													<%=mybean.PopulateGroup()%>
												</select>
											</div>

											<div class="form-element4">
												<center>
													<br> <br> 
													<input name="Input4" type="button"
														class="btn btn-success"
														onClick="JavaScript:AddItem('fo_group','fo_group_trans', '');test();"
														value="   Add >>"> <br> <br> 
													<input
														name="Input" type="button" class="btn btn-success"
														onClick="JavaScript:DeleteItem('fo_group_trans')"
														value="<< Delete"><br></br>
												</center>
											</div>
											<div class="form-element4">
												<select name="fo_group_trans" size="10" multiple="multiple"
													class="form-control" id="fo_group_trans">
													<%=mybean.PopulateGroupTrans()%>
												</select> 
												<input id="fo_group_trans_temp" name="fo_group_trans_temp"
													value="" hidden />

											</div>

											<input type="text" id="select_list" name="select_list" hidden />
											<!-- 													</div> -->
											<!-- 												</div> -->
											<script language="JavaScript">
													function retrive() {

														var select_list = $(
																'.ms-selected.ms-elem-selection')
																.text();
														/* alert("select_list==== "+select_list); */
														$("#select_list").val(
																select_list);
													}
													function onPress() {
														for (i = 0; i < document.formcontact.fo_group_trans.options.length; i++) {
															document.formcontact.fo_group_trans.options[i].selected = true;
														}
													}
												</script>
											<div class="row"></div>
											
											<div class="form-element6">
												<label> Notes:&nbsp;</label>
												<textarea name="txt_customer_notes" cols="70" rows="4"
													class="form-control" id="txt_customer_notes"><%=mybean.customer_notes%></textarea>
											</div>
											
											<div class="form-element6 form-element-margin">
												<label> Active:&nbsp;</label>
												 <input id="chk_customer_active" type="checkbox"
													name="chk_customer_active"
													<%=mybean.PopulateCheck(mybean.customer_active)%> />
											</div>

											<div class="row"></div>
											<%
													if (mybean.status.equals("Update") && !(mybean.entry_by == null)
															&& !(mybean.entry_by.equals(""))) {
												%>
											<div class="form-element6">
												<label>Entry By: <%=mybean.unescapehtml(mybean.entry_by)%></label>
												<input name="entry_by" type="hidden" id="entry_by"
													value="<%=mybean.entry_by%>">
											</div>
											
											<div class="form-element6">
												<label>Entry Date: <%=mybean.entry_date%></label> 
												<input type="hidden" name="entry_date"
													value="<%=mybean.entry_date%>">
											</div>
									</div>

									<% } %>
									
									<% if (mybean.status.equals("Update") && !(mybean.modified_by == null)
															&& !(mybean.modified_by.equals(""))) {
												%>
												
									<div class="form-element6">
										<label>Modified By: <%=mybean.unescapehtml(mybean.modified_by)%></label>
										<input name="modified_by" type="hidden" id="modified_by"
											value="<%=mybean.modified_by%>">
									</div>
									
									<div class="form-element6">
										<label>Modified Date: <%=mybean.modified_date%></label>
										 <input type="hidden" name="modified_date"
											value="<%=mybean.modified_date%>">
									</div>
									
									<% } %>
												
									<center>
									
										<% if (mybean.status.equals("Add")) { %>
										<input name="addbutton" type="submit" class="btn btn-success"
											id="addbutton" value="Add <%=mybean.tag%>"
											onClick="retrive(); onPress(); return SubmitFormOnce(document.formcontact, this);" />
										<input type="hidden" name="add_button" value="yes">
										<%
														} else if (mybean.status.equals("Update")) {
													%>
										<input type="hidden" name="update_button" value="yes">
										<input name="update_button" type="submit"
											class="btn btn-success" id="update_button"
											value="Update <%=mybean.tag%>"
											onClick="onPress(); return SubmitFormOnce(document.formcontact, this);" />
										<input name="delete_button" type="submit"
											class="btn btn-success" id="delete_button"
											onClick="return confirmdelete(this)"
											value="Delete <%=mybean.tag%>" />
										<% } %>
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
	$(function() {
		$("#txt_customer_gst_no").focusout(function(){
			var gst_no=$("#txt_customer_gst_no").val();
			var regex=/^([0-9]{2})([a-zA-Z]{5})([0-9]{4})([a-zA-Z]{1})([a-zA-Z0-9]{3})?$/;
			if(gst_no.length!=0 && gst_no.length < 15){
				$("#gst").html("<br><b><font color='red'>GSTIN is invalid</font></b>");
			}else if(gst_no.length!=0 && regex.test(gst_no) == false){
				$("#gst").html("<br><b><font color='red'>GSTIN is invalid</font></b>");
			}else{
				$("#gst").html("");
			}
		});
		
 	});
	</script>
	<script>
	  $("#txt_customer_gst_no,#txt_customer_arn_no,#txt_customer_pan_no").bind('keyup',function(e){
		  if(e.which >= 97 && e.which <= 122 ){
			  var newKey=e.which-32;
			  e.keyCode=newKey;
			  e.charCode=newKey;
		  }
		  $("#txt_customer_gst_no").val(($("#txt_customer_gst_no").val()).toUpperCase());
		  $("#txt_customer_arn_no").val(($("#txt_customer_arn_no").val()).toUpperCase());
		  $("#txt_customer_pan_no").val(($("#txt_customer_pan_no").val()).toUpperCase());
	  });
	</script>


	<script>
	$(function() {
		$("#txt_customer_arn_no").focusout(function(){
			var arn_no=$("#txt_customer_arn_no").val();
			var regex=/^([a-zA-Z]{2})([0-9]{6})([0-9]{7})([0-9]{1})?$/;
// 			alert("regex.test(arn_no)===="+regex.test(arn_no));
            if(arn_no.length!=0 && regex.test(arn_no)==false){
            	$("#arn").html("<br><b><font color='red'>ARN No. is invalid</font></b>");
            }else{
            	$("#arn").html("");
            }			
		});
		
 	});
	
	function HideAsterisk(){
		var itstatus = $("#dr_customer_itstatus_id").val();
		console.log(itstatus);
		if(itstatus == 2){
			document.getElementById("gstin").innerHTML = '';
			document.getElementById("gstdate").innerHTML = '';
		} else {
			document.getElementById("gstin").innerHTML = '*';
			document.getElementById("gstdate").innerHTML = '*';
		}
	}
	</script>
</body>
</HTML>
