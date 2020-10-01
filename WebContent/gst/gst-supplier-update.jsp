<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.gst.Gst_Supplier_Update" scope="page" />
<% mybean.doGet(request, response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">

<%@include file="../Library/css.jsp"%>

<style>
.page-header
{
	height: 0px;
}
</style>
</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed">
	
	<%@include file="../gst/header.jsp"%>
	
	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN CONTENT -->
				<div class="container-fluid">
				
					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<center>
								<font color="#FF0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										Vendor Info
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form class="form-horizontal" name="frmdoc"
											enctype="MULTIPART/FORM-DATA" method="post">

											<input type="hidden" name="hid-customer_branch_id" id="hid-customer_branch_id" value=<%=mybean.customer_branch_id %>/>
											<div class="form-element6">
													<label >Taxable Person Name<font color=red>*</font>:&nbsp;
													</label>
													<input name="txt_customer_name" type="text"
														class="form-control" id="txt_customer_name"
														value="<%=mybean.customer_name%>" size="50"
														maxlength="255"><font color="#ff0000"><span
														id="txtHint"></span></font>
											</div>
											
											<div class="form-element6">	
											 <div class="form-element3 form-element">
											<label> Authorised Person Name<font color=red>*</font>: </label>
															<select name="dr_title" class="form-control"
																id="dr_title">
																	<%=mybean.PopulateTitle()%>
															</select>Title<font color=red>*</font>:
											</div>

											<div class='form-element5  form-element-margin'>

															<input name="txt_contact_fname" type="text"
																class="form-control" id="txt_contact_fname"
																value="<%=mybean.contact_fname%>" size="30"
																maxlength="255" onkeyup="ShowNameHint()" />
																<span>First Name</span>
											</div>
											

											<div class='form-element4  form-element-margin'>

															<input name="txt_contact_lname" type="text"
																class="form-control" id="txt_contact_lname"
																value="<%=mybean.contact_lname%>" size="30"
																maxlength="255" onkeyup="ShowNameHint()" />
																<span>Last Name</span>
                                      		</div>
                                      </div>
											
												<div class="form-element6">
												<label>GSTIN<font color=red>*</font>:&nbsp;
												</label>
													<b><input name="txt_customer_gst_no" type="text"
														class="form-control" id="txt_customer_gst_no"
														value="<%=mybean.customer_gst_no%>" size="50"
														maxlength="15"></input></b><b>Example: 22AAAAA0000A1Z5</b><span id="gst"></span>
											</div>
											
											<div class="form-element6">
												<label>GSTIN Date<font color=red>*</font>:&nbsp;</label>
													<input name="txt_customer_gst_regdate" id="txt_customer_gst_regdate"
														value="<%=mybean.gst_regdate%>"
														class="form-control datepicker"
														 type="text" maxlength="10" />
											</div>
											<div class="row"></div>
											<div class="form-element6">
												<label>Select Document :&nbsp;
												</label>
													<input NAME="filename" Type="file" class="btn btn-success" id="filename" value="<%=mybean.gst_doc_value%>" size="34">
														<span>Click the Browse button to select the document from your computer</span>
											</div>
											
										<div class="form-element6">
												<label>ARN:&nbsp;
												</label>
														<input name="txt_customer_arn_no" type="text"
															class="form-control" id="txt_customer_arn_no"
															value="<%=mybean.customer_arn_no%>" size="10"
															maxlength="15"></input><b>Example: AA0707160000001</b><span id="arn"></span>
											</div>	
											<div class="row"></div>
											<div class="form-element6">
												<label>PAN<font
													color=red>*</font>:&nbsp;
												</label>
														<input name="txt_customer_pan_no" type="text"
															class="form-control" id="txt_customer_pan_no"
															value="<%=mybean.customer_pan_no%>" size="10"
															maxlength="10"></input><b>Example: ABCDF1234F</b>
											</div>
											
											<div class="form-element6">
												<label>Status<font color=red>*</font>:&nbsp;</label>
												</label>
													<select name="dr_customer_itstatus_id" class="form-control"
															id="dr_customer_itstatus_id">
														<%=mybean.PopulateItStatus()%>
													</select>

											</div>
											<div class="row"></div>
											<div class="form-element6">
												<label> Mobile <font color=red>*</font>:&nbsp;</label>
													<input name="txt_contact_mobile1" type="text"
														class="form-control" id="txt_contact_mobile1"
														value="<%=mybean.contact_mobile1%>" size="32"
														maxlength="13"
														onKeyUp="toPhone('txt_contact_mobile1','Mobile 1')">
													(91-9999999999)
											</div>
											
											
											<div class="form-element6">
												<label>Office Landline :&nbsp;</label>
													<input name="txt_contact_phone1" type="text"
														class="form-control" id="txt_contact_phone1"
														value="<%=mybean.contact_phone1%>" size="32"
														maxlength="14"
														onKeyUp="toPhone('txt_contact_phone1','Mobile 1')">
													(91-80-33333333)
											</div>
											
											
											<div class="form-element6">
												<label> Email <font color=red>*</font>:&nbsp;</label>
													<input name="txt_contact_email1" type="text"
														class="form-control" id="txt_contact_email1"
														value="<%=mybean.contact_email1%>" size="40"
														MaxLength="100"/>
														</br><label>City<font
													color=red>*</font>:&nbsp;
												</label>
													<select class="form-control-select select2" id="maincity"
														name="maincity">
														<%=mybean.citycheck.PopulateCities(mybean.contact_city_id,mybean.comp_id)%>
													</select>
											</div>
											
											<div class="form-element6">
												<label>Address<font color=red>*</font>:&nbsp;
												</label>
													<textarea name="txt_contact_address" cols="40" rows="4"
														class="form-control" id="txt_contact_address"
														onKeyUp="charcount('txt_contact_address', 'span_txt_contact_address','<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.contact_address%></textarea>
													<span id="span_txt_contact_address"> (255
														Characters)</span>
											</div>
											
											<div class="form-element6">
												<label> Pin/Zip<font
													color=red>*</font>:&nbsp;
												</label>
													<input name="txt_contact_pin" type="text"
														class="form-control" id="txt_contact_pin"
														onKeyUp="toInteger('txt_contact_pin','Pin')"
														value="<%=mybean.contact_pin%>" size="10" maxlength="6" />

											</div>
											<div class="form-element12">
											<center>
                                               <input name="add_button" type="submit" class="btn btn-success" value="Add Vendor" />
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
	<!-- END CONTAINER ------>
<%-- 	<%@include file="../Library/admin-footer.jsp"%> --%>
<%@include file="../Library/js.jsp"%>
	<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		document.frmdoc.txt_doc_name.focus()
	}
</script>

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
            	$("#arn").html("<br><b><font color='red'>ARN. is invalid</font></b>");
            }else{
            	$("#arn").html("");
            }			
		});
		
 	});
	</script>
	
</body>
</HTML>
