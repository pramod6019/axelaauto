<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Add_Service_Booking" scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<link rel="shortcut icon" type="image/x-icon"
	href="../admin-ifx/axela.ico">

<link href="../assets/css/font-awesome.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/components-rounded.css" rel="stylesheet"
	id="style_components" type="text/css" />
<link rel="shortcut icon" href="../test/favicon.ico" />
<link href="../assets/css/bootstrap-datepicker3.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/plugins.css" rel="stylesheet" type="text/css" />
<link href='../Library/jquery.qtip.css' rel='stylesheet' type='text/css' />
<link href="../assets/css/multi-select.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/select2.min.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/select2-bootstrap.min.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />
<script language="JavaScript" type="text/javascript">
 $(function() {
    $( "#txt_enquiry_date" ).datepicker({
      showButtonPanel: true,
      dateFormat: "dd/mm/yy"
    });
    $( "#txt_enquiry_close_date" ).datepicker({
      showButtonPanel: true,
      dateFormat: "dd/mm/yy"
    });  
  });
</script>

<script language="JavaScript" type="text/javascript">
	
	function FormSubmit()
        {
            document.frmenquiry.submit();
        }
        function FormFocus() { 
			document.frmenquiry.txt_customer_name.focus();   			
        }
				      
        function PopulateExecutive(){	
            var enquiry_branch_id = document.getElementById('txt_branch_id').value;	
			var team_id = document.getElementById('dr_enquiry_team').value;
			//alert(team_id);
			showHint('../sales/enquiry-check.jsp?executive=yes&active=1&team_id=' + team_id+'&enquiry_branch_id='+enquiry_branch_id,'teamexe');
		  }	  
		  		  
		  function populateItem()
		  {
			  var enquiry_model_id = document.getElementById('dr_enquiry_model_id').value;
			  showHint('../sales/enquiry-check.jsp?enquiry=yes&item=yes&enquiry_model_id='+enquiry_model_id, 'modelitem');
		  }
		  function populateSob(){
				 //alert(soe_id);
				 var enquiry_soe_id = document.getElementById('dr_enquiry_soe_id').value;
// 				 alert('../sales/enquiry-check.jsp?dr_enquiry_sob_id=yes&enquiry_soe_id='+enquiry_soe_id);
			  showHint('../sales/enquiry-check.jsp?dr_enquiry_sob_id=yes&enquiry_soe_id='+enquiry_soe_id, 'dr_enquiry_sob_id');
			  
			}
	
	function DisplayModel() {
	var str1=document.getElementById('dr_enquiry_enquirytype_id').value;	
	
	if(str1=="1")
		{
			displayRow('model');
			displayRow('package');
		} else{
			hideRow('model');
			hideRow('package');
		}
	}
	
    </script>
<style>
@media ( max-width : 1024px) {
}
</style>
</HEAD>
<body
	FormFocus();"
	class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>Add Enquiry</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!--- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp?all=yes">Home</a> &gt;</li>
						<li><a href="../service/index.jsp">Service</a> &gt;</li>
						<li><a href="enquiry.jsp?all=yes">Enquiry</a>&gt;</li>
						<li><a href="enquiry-list.jsp?all=yes">List Enquiry</a> &gt;</li>
						<li><a href="enquiry-list.jsp?all=yes"> <a
								href="enquiry-quickadd.jsp">Add Enquiry</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<form name="frmenquiry" id="frmenquiry" class="form-horizontal" method="post">
								<center>
									<font color="#ff0000"><b><%=mybean.msg%></b></font>
								</center>


								<!-- 	PORTLET customner details-->
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Customer
											Details</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<!--     start customer details -->

											<center>
												<font>Form fields marked with a red asterisk <b><font
														color="#ff0000">*</font></b> are required.
												</font>
											</center>
											<div class="form-group">
												<label class="control-label col-md-4"></label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<input class="form-control" name="contact_id" type="hidden"
														id="contact_id" value="<%=mybean.enquiry_contact_id%>" />
												</div>
											</div>
											
											<div class="form-group">
													<label class="control-label col-md-4 col-sm-2">
														Branch<font color=red>*</font>:
													</label>
													<div class="col-md-6 col-xs-12 col-sm-10" id="emprows">
														<select name="dr_branch_id" id="dr_branch_id"
															class="dropdown form-control"
															onchange="submit()">
															<%=mybean.PopulateBranches(mybean.enquiry_branch_id, mybean.comp_id)%>
														</select>
													</div>
												</div>
											
											<%
												if (mybean.enquiry_contact_id.equals("0")) {
											%>
											<div class="form-group">
												<label class="control-label col-md-4 col-xs-12 col-sm-1">Customer:</label>
												<div class="col-md-6 col-xs-11 col-sm-10" id="emprows">
													<input class="form-control" name="txt_customer_name"
														type="text" id="txt_customer_name"
														value="<%=mybean.customer_name%>" size="50"
														maxlength="255" /> (Name as on Invoice)
												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4"> Contact<font
													color=red>*</font>:
												</label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<table>
														<tr>
															<td><select name="dr_title" class="form-control"
																id="dr_title">
																	<%=mybean.PopulateTitle(mybean.contact_title_id,mybean.comp_id)%>
															</select> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>Title</span></td>
															<td><input name="txt_contact_fname" type="text"
																class="form-control " id="txt_contact_fname"
																value="<%=mybean.contact_fname%>" size="30"
																maxlength="255" onkeyup="ShowNameHint()" />&nbsp;&nbsp;&nbsp;&nbsp;
																&nbsp;&nbsp;&nbsp;&nbsp; <span>First Name</span></td>
															<td><input name="txt_contact_lname" type="text"
																class="form-control " id="txt_contact_lname"
																value="<%=mybean.contact_lname%>" size="30"
																maxlength="255" onkeyup="ShowNameHint()" />
																&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp; <span>Last
																	Name</span></td>
														</tr>
													</table>

												</div>
											</div>

											<div class="form-group">
												<label class="control-label col-md-4 col-sm-2  col-xs-12">Job
													Title:</label>
												<div class="col-md-6 col-sm-10 col-xs-12" id="emprows">
													<input class="form-control" name="txt_contact_jobtitle"
														type="text" id="txt_contact_jobtitle"
														value="<%=mybean.contact_jobtitle%>" size="32"
														maxlength="255">
												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4 col-sm-2 col-xs-12">Mobile
													1<font color="#ff0000">*</font>:
												</label>
												<div class="col-md-6 col-sm-10 col-xs-12" id="emprows">
													<input name="txt_contact_mobile1" type="text"
														class="form-control" id="txt_contact_mobile1"
														onKeyUp="toPhone('txt_contact_mobile1','Contact Mobile1');
													    showHint('../sales/enquiry-check.jsp?enquiry_branch_id='+<%=mybean.enquiry_branch_id%>+'&contact_mobile=' + GetReplace(this.value),'showcontacts');" onChange= "toPhone('txt_contact_mobile1','Contact Mobile1');showHint('../sales/enquiry-check.jsp?enquiry_branch_id='+<%=mybean.enquiry_branch_id%>+'&contact_mobile=' + GetReplace(this.value),'showcontacts');"
														value="<%=mybean.contact_mobile1%>" size="32"
														maxlength="13" /> (91-9999999999)
<!-- 														<br> -->
														<span id="showcontacts"></span>
												</div>
												
											</div>

<!-- 											<div class="form-group"> -->
<!-- 												<label class="control-label col-md-4 col-xs-12 col-sm-2">Mobile -->
<!-- 													2:</label> -->
<!-- 												<div class="col-md-6 col-xs-12 col-sm-10" id="emprows"> -->
<!-- 													<input name="txt_contact_mobile2" type="text" -->
<!-- 														class="form-control" id="txt_contact_mobile2" -->
<%-- 														onKeyUp="toPhone('txt_contact_mobile2','Contact Mobile2'); --%>
<%-- 													    showHint('../sales/enquiry-check.jsp?enquiry_branch_id='+<%=mybean.enquiry_branch_id%>+'&contact_mobile=' + GetReplace(this.value),'showcontacts');" onChange= "toPhone('txt_contact_mobile2','Contact Mobile2');showHint('../sales/enquiry-check.jsp?enquiry_branch_id='+<%=mybean.enquiry_branch_id%>+'&contact_mobile=' + GetReplace(this.value),'showcontacts');" --%>
<%-- 														value="<%=mybean.contact_mobile2%>" size="32" --%>
<!-- 														maxlength="13" /> (91-9999999999) -->
<!-- 														<br></br> -->
<!-- 														<span id="showcontacts"></span> -->
<!-- 												</div> -->
<!-- 											</div> -->

											<div class="form-group">
												<label class="control-label col-md-4 col-sm-2 col-xs-12">Phone
													1<font color="#ff0000">*</font>:
												</label>
												<div class="col-md-6 col-sm-10 col-xs-12" id="emprows">
													<input name="txt_contact_phone1" type="text"
														class="form-control" id="txt_contact_phone1"
														onKeyUp="showHint('../sales/enquiry-check.jsp?contact_id='+<%=mybean.contact_id%>+'&contact_phone=' + GetReplace(this.value),'showcontacts'); toPhone('txt_contact_phone1','Contact Phone1');"
														onChange="toPhone('txt_contact_phone1','Contact Phone1');showHint('../sales/enquiry-check.jsp?contact_id='+<%=mybean.contact_id%>+'&contact_phone=' + GetReplace(this.value),'showcontacts');"
														value="<%=mybean.contact_phone1%>" size="32"
														maxlength="14" /> (91-80-33333333)
												</div>
											</div>

<!-- 											<div class="form-group"> -->
<!-- 												<label class="control-label col-md-4 col-sm-2 col-xs-12">Phone -->
<!-- 													2:</label> -->
<!-- 												<div class="col-md-6 col-xs-12 col-sm-10" id="emprows"> -->
<!-- 													<input name="txt_contact_phone2" type="text" -->
<!-- 														class="form-control" id="txt_contact_phone2" -->
<%-- 														onKeyUp="showHint('../sales/enquiry-check.jsp?contact_id='+<%=mybean.contact_id%>+'&contact_phone=' + GetReplace(this.value),'showcontacts'); toPhone('txt_contact_phone2','Contact Phone2');" --%>
<%-- 														onChange="toPhone('txt_contact_phone2','Contact Phone2');showHint('../sales/enquiry-check.jsp?contact_id='+<%=mybean.contact_id%>+'&contact_phone=' + GetReplace(this.value),'showcontacts');" --%>
<%-- 														value="<%=mybean.contact_phone2%>" size="32" --%>
<!-- 														maxlength="14" /> <br>(91-80-33333333) -->
<!-- 												</div> -->
<!-- 											</div> -->

											<div class="form-group">
												<label class="control-label col-md-4 col-xs-12 col-sm-2">Email
													1:</label>
												<div class="col-md-6 col-xs-12 col-sm-10" id="emprows">
													<input name="txt_contact_email1" type="text"
														class="form-control" id="txt_contact_email1"
														onkeyup="showHint('../sales/enquiry-check.jsp?contact_id='+<%=mybean.contact_id%>+'&enquiry_branch_id='+<%=mybean.enquiry_branch_id%>+'&contact_email=' + GetReplace(this.value),'showcontacts');"
														value="<%=mybean.contact_email1%>" size="32"
														maxlength="100">
												</div>
											</div>

<!-- 											<div class="form-group"> -->
<!-- 												<label class="control-label col-md-4 col-xs-12 col-sm-2">Email -->
<!-- 													2:</label> -->
<!-- 												<div class="col-md-6 col-xs-12 col-sm-10" id="emprows"> -->
<!-- 													<input name="txt_contact_email2" type="text" -->
<!-- 														class="form-control" id="txt_contact_email2" -->
<%-- 														onKeyUp="showHint('../sales/enquiry-check.jsp?contact_id='+<%=mybean.contact_id%>+'&contact_email=' + GetReplace(this.value),'showcontacts');" --%>
<%-- 														value="<%=mybean.contact_email2%>" size="32" --%>
<!-- 														maxlength="100"> -->
<!-- 												</div> -->
<!-- 											</div> -->

											<div class="form-group">
												<label class="control-label col-md-4 col-xs-12 col-sm-2">Address:</label>
												<div class="col-md-6 col-xs-12 col-sm-10" id="emprows">
													<textarea name="txt_contact_address" cols="40" rows="4"
														class="form-control" id="txt_contact_address"
														onKeyUp="charcount('txt_contact_address', 'span_txt_contact_address','<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.contact_address%></textarea>
													<span id="span_txt_contact_address"> (255
														Characters)</span>
												</div>
											</div>

											<div class="form-group">
												<label class="control-label col-md-4 col-sm-2">City<font
													color="#ff0000">*</font>:
												</label>
												<div class="col-md-6 col-xs-12 col-sm-10">
													<select class="form-control select2" id="maincity"
														name="maincity">
														<%=mybean.citycheck.PopulateCities(
						mybean.contact_city_id, mybean.comp_id)%>
													</select>
												</div>
											</div>

											<div class="form-group">
												<label class="control-label col-md-4 col-xs-12 col-sm-2">Pin/Zip:</label>
												<div class="col-md-6 col-xs-12 col-sm-10" id="emprows">
													<input name="txt_contact_pin" type="text"
														class="form-control" id="txt_contact_pin"
														onKeyUp="toInteger('txt_contact_pin','Pin')"
														value="<%=mybean.contact_pin%>" size="10" maxlength="6" />
												</div>
											</div>
											<%
												}
											%>

											<%
												if (!mybean.enquiry_contact_id.equals("0")) {
											%>
											<div class="form-group">
												<label class="control-label col-md-4">Customer:</label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<%=mybean.customer_info%>
												</div>
											</div>

											<div class="form-group">
												<label class="control-label col-md-4">Contact:</label>
												<div class="col-md-6 col-xs-12" id="emprows">
													<%=mybean.contact_info%>
												</div>
											</div>

											<%
												}
											%>

										</div>
									</div>
								</div>



								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Enquiry Details
										</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->

											<div class="form-group">
												<label class="control-label col-md-4 col-sm-2"> Date<font
													color="#ff0000">*</font>:
												</label>
												<div class="col-md-6 col-xs-12 col-sm-10" id="emprows">
													<%
														if (mybean.emp_enquiry_edit.equals("1")) {
													%>
													<input name="txt_enquiry_date" id="txt_enquiry_date"
														value="<%=mybean.enquiry_date%>"
														class="form-control date-picker"
														data-date-format="dd/mm/yyyy" type="text" maxlength="10" />
													<%
														} else {
													%>
													<%=mybean.enquiry_date%>
													<%
														}
													%>

												</div>
											</div>

											<div class="form-group">
												<label class="control-label col-md-4 col-xs-12 col-sm-2">
													Closing Date<font color="#ff0000">*</font>:
												</label>
												<div class="col-md-6 col-xs-12 col-sm-10" id="emprows">
													<input name="txt_enquiry_close_date"
														id="txt_enquiry_close_date"
														value="<%=mybean.enquiry_close_date%>"
														class="form-control date-picker"
														data-date-format="dd/mm/yyyy" type="text" maxlength="10" />
												</div>
											</div>

											<div class="form-group">
												<label class="control-label col-md-4 col-xs-12 col-sm-2">
													Description: </label>
												<div class="col-md-6 col-xs-12 col-sm-10" id="emprows">
													<textarea name="txt_enquiry_desc" cols="40" rows="4"
														class="form-control" id="txt_enquiry_desc"
														onKeyUp="charcount('txt_enquiry_desc', 'span_txt_enquiry_desc','<font color=red>({CHAR} characters left)</font>', '8000')"><%=mybean.enquiry_desc%></textarea>
													<span id="span_txt_enquiry_desc"> (8000 Characters)</span>
												</div>
											</div>

											
											<%
												if (mybean.branchtype_id.equals("1")) {
											%>
											<div class="form-group">
												<label class="control-label col-md-4 col-sm-2">
													Model<font color="#ff0000">*</font>:
												</label>
												<div class="col-md-6 col-xs-12 col-sm-9" id="emprows">
													<select name="dr_enquiry_model_id" id="dr_enquiry_model_id"
														class="dropdown form-control" onChange="populateItem();">
														<%=mybean.PopulateModel(mybean.enquiry_model_id,mybean.comp_id)%>
													</select>
												</div>
												<div class="col-md-2" style="top: 8px"></div>
											</div>


											<div class="form-group">
												<label class="control-label col-md-4 col-xs-12 col-sm-2">Variant<font
													color="#ff0000">*</font>:
												</label>
												<div class="col-md-6 col-xs-12 col-sm-10" id="emprows">
													<!--  <select name="dr_enquiry_item_id" id="dr_enquiry_item_id" class="form-control"> -->
													<span id="modelitem"> <%=mybean.PopulateItem(mybean.enquiry_model_id,
						mybean.comp_id)%></span>
													<!--------- </select> -->

												</div>
											</div>
											<%
												}
											%>

											<%
												if (!mybean.branch_brand_id.equals("1")) {
											%>
											<div class="form-group">
												<label class="control-label col-md-4 col-xs-12 col-sm-2">Type of Buyer<font color="#ff0000">*</font>:
												</label>
												<div class="col-md-6 col-xs-12 col-sm-10" id="emprows">
													<select name="dr_enquiry_buyertype_id"
														id="dr_enquiry_buyertype_id" class="form-control">
														<%=mybean.PopulateBuyerType(mybean.comp_id)%>
													</select>
												</div>
											</div>
											<% } %>
										</div>
										<% if (mybean.branch_brand_id.equals("55")) { %>
										<div class="form-group">
												<label class="control-label col-md-4 col-xs-12 col-sm-2">Category
												<font color="#ff0000">*</font> :
												</label>
												<div class="col-md-6 col-xs-12 col-sm-10" id="emprows">
													<select name="dr_enquiry_enquirycat_id"
														id="dr_enquiry_enquirycat_id" class="form-control">
														<%=mybean.PopulateCategory(mybean.comp_id)%>
													</select>
												</div>
											</div>
													<%} %>
									</div>
								</div>



								<%
									if (mybean.branchtype_id.equals("2")) {
								%>
								<div>
									<div class="portlet box  " style="position: relative">
										<div class="portlet-title" style="text-align: center">
											<div class="caption" style="float: none">Pre Owned</div>
										</div>
										<div class="portlet-body portlet-empty">
											<div class="tab-pane" id="">
												<!-- 											START PORTLET BODY -->
												<!-- <div class="row"> -->
												<div class="form-group">
													<label class="control-label col-md-4 col-xs-12 col-sm-2">
														Pre Owned Model<font color="#ff0000">*</font>:
													</label>
													<div class="col-md-6 col-xs-12 col-sm-10" id="emprows">
														<select class="form-control select2" id="preownedvariant"
															name="preownedvariant">
															<%=mybean.variantcheck
						.PopulateVariant(mybean.enquiry_preownedvariant_id)%>
														</select>
													</div>
												</div>

												<div class="form-group" id="fueltype">
													<label class="control-label col-md-4 col-xs-12 col-sm-2">
														Fuel Type<font color="#ff0000">*</font>:
													</label>
													<div class="col-md-6 col-xs-12 col-sm-10" id="emprows">
														<select name="dr_enquiry_fueltype_id"
															id="dr_enquiry_fueltype_id" class="form-control">
															<%=mybean.PopulateFuelType(mybean.comp_id)%>
														</select>
													</div>
												</div>

												<div class="form-group" id="fueltype">
													<label class="control-label col-md-4 col-xs-12 col-sm-2">
														Pref. Reg.: </label>
													<div class="col-md-6 col-xs-12 col-sm-10" id="emprows">
														<select name="dr_enquiry_prefreg_id"
															id="dr_enquiry_prefreg_id" class="form-control">
															<%=mybean.PopulatePrefReg(mybean.comp_id)%>
														</select>
													</div>
												</div>

												<div class="form-group" id="presentcar">
													<label class="control-label col-md-4 col-xs-12 col-sm-2">
														Present Car: </label>
													<div class="col-md-6 col-xs-12 col-sm-10" id="emprows">
														<input name="txt_enquiry_presentcar" type="text"
															class="form-control" id="txt_enquiry_presentcar"
															value="<%=mybean.enquiry_presentcar%>" size="32"
															maxlength="255" />
													</div>
												</div>
												<div class="form-group" id="presentcar">
													<label class="control-label col-md-4 col-xs-12 col-sm-2">
														Finance<font color="#ff0000">*</font>:
													</label>
													<div class="col-md-6 col-xs-12 col-sm-10" id="emprows">
														<select name="dr_enquiry_finance" id="dr_enquiry_finance"
															class="form-control">
															<%=mybean.PopulateFinance(mybean.comp_id)%>
														</select>

													</div>
												</div>
												<div class="form-group" id="budget">
													<label class="control-label col-md-4 col-xs-12 col-sm-2">
														Budget<font color="#ff0000">*</font>:
													</label>
													<div class="col-md-6 col-xs-12 col-sm-10" id="emprows">
														<input name="txt_enquiry_budget" type="text"
															class="form-control" id="txt_enquiry_budget"
															value="<%=mybean.enquiry_budget%>" size="14"
															maxlength="10" />
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
								<%
									}
								%>


								<!-- enquiry status -->
								<div class="portlet box  " style="position: relative">
									<div class="portlet-title" style="text-align: center;">
										<div class="caption" style="float: none">Enquiry Status
										</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<div class="container-fluid">

												<div class="form-group">
													<label class="control-label col-md-4 col-sm-2">
														Team:<font color=red>*</font>:
													</label>
													<div class="col-md-6 col-xs-12 col-sm-10" id="emprows">
<!-- 														<select name="dr_enquiry_team" id="dr_enquiry_team" -->
<!-- 															class="dropdown form-control" -->
<!-- 															onchange="PopulateExecutive();"> -->
															<%=mybean.PopulateTeam(mybean.enquiry_branch_id, mybean.enquiry_team_id, mybean.comp_id)%>
<!-- 														</select> -->
													</div>
												</div>
												<div class="form-group">
													<label class="control-label col-md-4 col-xs-12 col-sm-2">
														Executive<font color="#ff0000">*</font>:
													</label>
													<div class="col-md-6 col-sm-9">
														<span id="teamexe">
<!-- 														 <select -->
<!-- 															name="dr_enquiry_emp_id" id="dr_enquiry_emp_id" -->
<!-- 															class="dropdown form-control"> -->
																<%=mybean.PopulateSalesExecutives(mybean.enquiry_branch_id, mybean.enquiry_team_id, mybean.enquiry_emp_id, "1", mybean.comp_id, request)%>
<!-- 														</select>  -->
														</span>
													</div>
													<div class="col-md-2" style="top: 8px"></div>
												</div>

												<%
													if (mybean.config_sales_soe.equals("1")) {
												%>

												<div class="form-group">
													<label class="control-label col-md-4 col-xs-12 col-sm-2">Source
														of Enquiry<font color="#ff0000">*</font>:
													</label>
													<div class="col-md-6 col-xs-12 col-sm-9" id="emprows">
														<select name="dr_enquiry_soe_id" id="dr_enquiry_soe_id"
															class="dropdown form-control" onchange=" populateSob();">
															<%=mybean.PopulateSoe(mybean.comp_id)%>
														</select>
													</div>
													<div class="col-md-2" style="top: 8px"></div>
												</div>
												<%
													}
												%>

												<%
													if (mybean.config_sales_sob.equals("1")) {
												%>

												<div class="form-group">
													<label class="control-label col-md-4 col-xs-12 col-sm-2">
														Source of Business<font color="#ff0000">*</font>:
													</label>
													<div class="col-md-6 col-xs-12 col-sm-9" id="emprows">
														<select name="dr_enquiry_sob_id" id="dr_enquiry_sob_id"
															class="dropdown form-control">
															<%=mybean.PopulateSOB()%>
														</select>
													</div>
													<div class="col-md-2" style="top: 8px"></div>
												</div>
												<%
													}
												%>
												<%
													if (mybean.config_sales_campaign.equals("1")) {
												%>

												<div class="form-group">
													<label class="control-label col-md-4 col-xs-12 col-sm-2">
														Campaign<font color="#ff0000">*</font>:
													</label>
													<div class="col-md-6 col-xs-12 col-sm-9" id="emprows">
														<span id="campaign"> <select
															name="dr_enquiry_campaign_id" id="dr_enquiry_campaign_id"
															class="dropdown form-control">
																<%=mybean.PopulateCampaign(mybean.comp_id)%>
														</select>
														</span>
													</div>
													<div class="col-md-2" style="top: 8px">
														<div class="admin-master">
															<a href="../sales/campaign-list.jsp?all=yes"
																title="Manage Campaign"></a>
														</div>
													</div>
												</div>
												<%
													}
												%>

<%-- 												<% --%>
<!-- // 													if (!mybean.team_preownedbranch_id.equals("0")) { -->
<%-- 												%> --%>
												<div class="form-group">
													<label class="control-label col-md-4 col-xs-12 col-sm-2">
														Trade-In Model: </label>
													<div class="col-md-6 col-xs-12 col-sm-9" id="emprows">
														<select class="form-control select2"
															id="enquiry_tradein_preownedvariant_id"
															name="enquiry_tradein_preownedvariant_id">
															<!-- 														onchange="SecurityCheck('enquiry_tradein_preownedvariant_id',this,'hint_preownedvariant')" -->

															<%=mybean.variantcheck
						.PopulateVariant(mybean.enquiry_tradein_preownedvariant_id)%>
														</select>
													</div>
												</div>
<%-- 												<% --%>
<!-- // 													} -->
<%-- 												%> --%>
												<div class="form-group">
													<label class="control-label col-md-4 col-xs-12"> </label>
													<div class="col-md-6 col-xs-12" id="emprows">
														<center>
															<input name="addbutton" type="submit"
																class="button btn btn-success" id="addbutton"
																value="Add Enquiry" />
<!-- <button type="button" class="btn1" name="addbutton" id="addbutton" >Add Enquiry</button> -->
															<input type="hidden" name="add_button" id="add_button" value="">
															<input type="hidden" id="branchtype_id"
																value="<%=mybean.branchtype_id%>"></input>
																<input class="form-control" type="hidden"
														name="txt_branch_id" id="txt_branch_id"
														value="<%=mybean.enquiry_branch_id%>" /> 
														</center>
													</div>
												</div>


											</div>
										</div>
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- END CONTAINER -->
	<%@include file="../Library/admin-footer.jsp"%>
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="../assets/js/components-date-time-pickers.js"
		type="text/javascript"></script>
	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript"
		src="../Library/dynacheck.js"></script>
	<script src="../assets/js/select2.full.min.js" type="text/javascript"></script>
	<script src="../assets/js/components-select2.min.js"
		type="text/javascript"></script>
	<script src="../assets/js/bootstrap-datepicker.js"
		type="text/javascript"></script>
	<script src="../assets/js/jquery.multi-select.js"
		type="text/javascript"></script>
	<script>

	</script>
	  <script>

$(document).ready(function() {
	
	$("#addbutton").click(function() {
		document.getElementById('add_button').value = "yes";
		document.getElementById('frmenquiry').submit();
	});
	
// 	$("#txt_contact_email1").focusout(function(){
<%-- 		showHint('../sales/enquiry-check.jsp?contact_id='+<%=mybean.contact_id%>+'&enquiry_branch_id='+<%=mybean.enquiry_branch_id%>+'&contact_email=' + GetReplace(this.value),'showcontacts'); --%>
// 	});
});

</script>
</body>
</HTML>

