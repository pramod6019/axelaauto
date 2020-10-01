<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.insurance.Insurance_Enquiry"
	scope="request" />
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

<%@include file="../Library/css.jsp"%>
<style>
@media ( max-width : 1024px) {
}
</style>
</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>Insurance Enquiry</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!--- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
				<div class="page-content-inner">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="index.jsp">Insurance Dashboard</a> &gt;</li>
						<li><a href="insurance-enquiry-list.jsp?all=yes">Insurance Enquiry List</a> &gt;</li>
						<li><a href="insurance-enquiry.jsp?">Add Insurance Enquiry</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<form name="frmenquiry" id="frmenquiry" class="form-horizontal" method="post">
								<center>
									<font color="#ff0000"><b><%=mybean.msg%></b></font>
								</center>


								<!-- 	PORTLET customner details-->
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Customer Details</div>
									</div>
									<div class="portlet-body portlet-empty  container-fluid">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<!--     start customer details -->

											<center>
												<font size="1">Form fields marked with a red asterisk <b><font
														color="#ff0000">*</font></b> are required.
												</font>
											</center>

											<div class="form-element6">
												<label> Branch<font color=red>*</font>: </label>
													<select class="dropdown form-control" name="dr_vehbranch_id" id="dr_vehbranch_id"
														onchange="PopulateCampaign(this.value);">
														<%=mybean.PopulateBranches(mybean.comp_id, mybean.insurenquiry_branch_id)%>
													</select>
											</div>

											<div class="form-element6">
												<label>Customer: &nbsp;</label>
													<input class="form-control" name="txt_insurance_customer_name"
														type="text" id="txt_insurance_customer_name"
														value="<%=mybean.insurenquiry_customer_name%>" size="50"
														maxlength="255" />
														<span>(Name as on Invoice)</span>
											</div>

											
											<div class="row">
												<div class="form-element2 ">
													<label>Contact<font color=red>*</font>:&nbsp; </label>
													<select name="dr_title" class="form-control" id="dr_title">
														<%=mybean.PopulateTitle(mybean.insurenquiry_contact_title_id, mybean.comp_id)%>
													</select>
													<span>Title</span>
												</div> 
												
												<div class="form-element5 form-element-margin">
													<input name="txt_contact_fname" type="text" class="form-control"
														id="txt_contact_fname" value="<%=mybean.insurenquiry_contact_fname%>"
														size="30" maxlength="255" onkeyup="ShowNameHint();" />
													<span>First Name</span>
												</div> 
														
												<div class="form-element5 form-element-margin">
													<input name="txt_contact_lname" type="text" class="form-control"
														id="txt_contact_lname" value="<%=mybean.insurenquiry_contact_lname%>"
														size="30" maxlength="255" onkeyup="ShowNameHint();" /> 
													<span>Last Name</span>
												</div>
											</div>
											
											<div class="form-element6">
												<label>Job Title:</label>
												<input class="form-control" name="txt_contact_jobtitle" type="text"
													id="txt_contact_jobtitle" value="<%=mybean.insurenquiry_contact_jobtitle%>"
													size="32" maxlength="255"/>
											</div>

											<div class="form-element6">
												<label>Mobile <font color="#ff0000">*</font>: &nbsp; </label>
													<input name="txt_contact_mobile1" type="text"
														class="form-control" id="txt_contact_mobile1"
														onKeyUp="showHint('../service/report-check.jsp?contact_mobile1='+GetReplace(this.value)+'&search=yes','showcontacts');toPhone('txt_contact_mobile1','Mobile1');"
														value="<%=mybean.insurenquiry_contact_mobile1%>" size="32"
														maxlength="13" /> (91-9999999999) <span id="showcontacts"></span>

											</div>

											<div class="form-element6">
												<label">Email : &nbsp;</label>
													<input name="txt_contact_email1" type="text"
														class="form-control" id="txt_contact_email1"
														onKeyUp="showHint('../service/report-check.jsp?contact_email1='+GetReplace(this.value)+'&search=yes','search-contact');"
														value="<%=mybean.insurenquiry_contact_email1%>" size="32"
														maxlength="100"><span id="search-contact"></span>
											</div>
											
											<div class="form-element6">
												<label> Date<font color=red>*</font>: </label>
													<%-- <%
														if (mybean.emp_insurenquiry_edit.equals("1")) {
													%> --%>
													<input name="txt_insurenquiry_date" id="txt_insurenquiry_date"
														value="<%=mybean.insurenquiry_date%>"
														class="form-control datepicker"
														data-date-format="dd/mm/yyyy" type="text" maxlength="10" />
													<%-- <%
														} else {
													%>
													<%=mybean.insurenquiry_date%>
													<%
														}
													%> --%>

											</div>

										</div>
									</div>
								</div>
								
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Vechile Details</div>
									</div>
									<div class="portlet-body portlet-empty container-fluid">
										<div class="tab-pane" id="">
											<div class="form-element6">
												<label>Model<font color="#ff0000">*</font>: &nbsp; </label>
													<select class="form-control select2" id="preownedvariant"
														name="preownedvariant">
														<%=mybean.modelcheck.PopulateVariant(mybean.insurenquiry_variant_id)%>
													</select>
											</div>

											<div class="form-element6">
												<label>Reg.No: &nbsp; </label>
													<input class="form-control" name="txt_insurance_veh_reg_no"
														type="text" id="txt_insurance_veh_reg_no"
														value="<%=mybean.insurenquiry_reg_no%>" size="50"
														maxlength="255" />
											</div>
											
											<div class="form-element6 ">
														<label>Insurance Type<font color="#ff0000">*</font>:&nbsp;</label>
															<select id="dr_insurenquiry_insurtype_id" name="dr_insurenquiry_insurtype_id" class="form-control">
																<%=mybean.PopulateInsurType(mybean.comp_id)%>
															</select> 
											</div>
											
											
												<div class="form-element6">
													<label> Insurance Executive<font color="#ff0000">*</font>: &nbsp; </label>
														<span id="insurexe"><%=mybean.PopulateInsurExecutive(mybean.comp_id, request)%>
														</span>
												</div>
	
												<div class="form-element6">
													<label>Source of Enquiry<font color="#ff0000">*</font>: &nbsp;</label>
														<select name="dr_insur_soe_id" id="dr_insur_soe_id"
															class="dropdown form-control" onchange="populateSob();">
															<%=mybean.PopulateSoe(mybean.comp_id)%>
														</select>
												</div>
											

											<div class="form-element6">
												<label> Source of Business<font color="#ff0000">*</font> :&nbsp; </label>
													<span id="HintSob"> 
														<%=mybean.PopulateSOB(mybean.insurenquiry_soe_id, mybean.comp_id)%>
													</span>
											</div>
											
											<div class="row"></div>

											<div class="form-element6">
												<label> Campaign<font color="#ff0000">*</font>: &nbsp; </label>
													<span id="insurcampaign">
														<%=mybean.PopulateCampaign(mybean.comp_id, mybean.insurenquiry_branch_id)%>
													</span>
											</div>
											
											<div class="form-element12">
													<center>
														<input name="addbutton" type="submit"
															class="button btn btn-success" id="addbutton" value="Add Enquiry" /> 
													</center>
											</div>

										</div>
									</div>
								</div>

								<!-- 	PORTLET end details-->

 							</form>
						</div>
					</div>
				</div>
			</div>
			<!--contain end  -->

		</div>
		<!--contain end  -->
	</div>


	<!-- END CONTAINER -->
	<%@include file="../Library/js.jsp"%>
	<%@include file="../Library/admin-footer.jsp"%>
	<script type="text/javascript">
         function PopulateItem(model_id){
        	showHint("../service/booking-enquiry-check.jsp?item_model_id="+model_id+"&insurlist_model_item=yes", "model-item");
        }

         function populateSob(){
    		var insur_soe_id = document.getElementById('dr_insur_soe_id').value;
    		showHint('../service/booking-enquiry-check.jsp?dr_insur_sob_id=insuranceenquiry&insur_soe_id='+insur_soe_id, 'HintSob');
    	  
    	}
 		function PopulateCampaign(branch_id) {
 			showHint("../insurance/mis-check.jsp?insurenquiry_branch_id=" + branch_id + "&insurcampaign=yes", "HintSob");
 		}
	
    </script>


</body>
</HTML>



