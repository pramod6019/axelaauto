<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Booking_Enquiry"
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
</HEAD>
<body Onload="PopulateBookingTime();"
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
						<h1>Add Booking Enquiry</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!--- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
			<div class="page-content-inner">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp?all=yes">Home</a> &gt;</li>
						<li><a href="../service/index.jsp">Service</a> &gt;</li>
						<li><a href="booking.jsp?all=yes">Booking Enquiry</a> &gt;</li>
						<!-- <li><a href="booking-list.jsp?all=yes">Booking </a> &gt;</li>
						<li><a href="booking-list.jsp?all=yes">-->
						<a href="booking-enquiry.jsp">Add Booking Enquiry</a><b>:</b>
						</li>
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
										<div class="caption" style="float: none">Customer
											Details</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="container-fluid">
											<!-- START PORTLET BODY -->
											<!--     start customer details -->
											<center>
													<font>Form fields marked with a red asterisk <b><font
															color="#ff0000">*</font></b> are required. </font>
											</center>
											<div class="form-element6">
												<label> Branch<font color="#ff0000">*</font>:&nbsp; </label>
													<select class="form-control" name="dr_branch"
															id="dr_branch" onchange="PopulateCampaign(this.value);">
													<%=mybean.PopulateBranches(mybean.comp_id, mybean.booking_veh_branch_id)%>
													</select>
											</div>


											<div class="form-element6">
												<label>Customer:&nbsp;</label>
													<input class="form-control"
														name="txt_booking_customer_name" type="text"
														id="txt_booking_customer_name"
														value="<%=mybean.customer_name%>" size="50"
														maxlength="255" /> (Name as on Invoice)
											</div>
											
											<div class="row">
												<div class="form-element2 ">
													<label>Contact<font color=red>*</font>:&nbsp; </label>
													<select name="dr_title" class="form-control" id="dr_title">
														<%=mybean.PopulateTitle(mybean.contact_title_id, mybean.comp_id)%>
													</select>
													<span>Title</span>
												</div> 
												
												<div class="form-element5 form-element-margin">
													<input name="txt_contact_fname" type="text" class="form-control"
														id="txt_contact_fname" value="<%=mybean.contact_fname%>"
														size="30" maxlength="255" onkeyup="ShowNameHint();" />
													<span>First Name</span>
												</div> 
														
												<div class="form-element5 form-element-margin">
													<input name="txt_contact_lname" type="text" class="form-control"
														id="txt_contact_lname" value="<%=mybean.contact_lname%>"
														size="30" maxlength="255" /> 
													<span>Last Name</span>
												</div>
											</div>
											
											
											
											
											<div class="form-element6">
												<label>Mobile <font color="#ff0000">*</font>:&nbsp; </label>
													<input name="txt_contact_mobile1" type="text"
														class="form-control" id="txt_contact_mobile1"
														onKeyUp="showHint('report-check.jsp?contact_mobile1='+GetReplace(this.value)+'&search=yes','showcontacts');toPhone('txt_contact_mobile1','Mobile1');"
														value="<%=mybean.contact_mobile1%>" size="32"
														maxlength="13" /> (91-9999999999) <span id="showcontacts"></span>
											</div>
											
											<div class="form-element6">
												<label>Email :&nbsp;</label>
													<input name="txt_contact_email1" type="text"
														class="form-control" id="txt_contact_email1"
<%-- 														onkeyup="showHint('../service/booking-enquiry-check.jsp?contact_id='+<%=mybean.customer_id%>+'&booking_veh_branch_id='+<%=mybean.booking_veh_branch_id%>+'&contact_email=' + GetReplace(this.value),'showcontacts');" --%>
														onKeyUp="showHint('report-check.jsp?contact_email1='+GetReplace(this.value)+'&search=yes','search-contact');"
														value="<%=mybean.contact_email1%>" size="32"
														maxlength="100"><span id="search-contact"></span>
											</div>
										</div>
									</div>
								</div>
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Vechile Details</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="container-fluid">
											<%-- <%
												if (mybean.branchtype_id.equals("1")) {
											%> --%>
											<div class="form-element6 ">
														<label> Variant <font color="#ff0000">*</font>: </label>
														<select class="form-control select2" id="servicepreownedvariant" name="servicepreownedvariant">
															<%=mybean.variantcheck.PopulateVariant(mybean.booking_veh_variant_id, mybean.comp_id)%>
														</select>
											</div>
											<div class="form-element6">
												<label>Reg.No <font color="#ff0000">*</font>:&nbsp; </label>
													<input class="form-control" name="txt_booking_veh_reg_no"
														type="text" id="txt_booking_veh_reg_no"
														value="<%=mybean.booking_veh_reg_no%>" size="50"
														maxlength="20" />
											</div>
											<div class="row"></div>
											<div class="form-element6">
												<label> CRM Executive<font color="#ff0000">*</font>:&nbsp; </label>
													<span id="crmexe">
															<%=mybean.PopulateCRMExecutive(mybean.comp_id,request)%>
													</span>
											</div>
											
											<div class="form-element6">
												<label>Booking Type:&nbsp; </label>
													<select name="dr_booking_type" class="form-control" id="dr_booking_type"
														Onchange="PopulateBookingTime();" visible="true">
														<%=mybean.PopulateServiceBookingType(mybean.comp_id, mybean.booking_type)%>
													</select>
											</div>
											
										<div class="row"></div>	
											<div class="form-element6" id="followuptime">
												<label>Follow-up Time:&nbsp; </label>
													<input name="txt_nextfollowup_time" type="text" class="form-control datetimepicker"
														id="txt_nextfollowup_time" value="<%=mybean.followup_nextfollowup_time%>"
														size="20" maxlength="16"/>
											</div>
											
											<div class="form-element6" id="bookingtimeband">
												<label>Booking Time<font color="red">*</font>:&nbsp; </label>
													<input name="txt_booking_time" type="text" class="form-control datetimepicker"
														id="txt_booking_time" value="<%=mybean.booking_time%>"/>
											</div>
											
												<div class="form-element6">
													<label>Source of Enquiry <font color="#ff0000">*</font>:&nbsp; </label>
														<select name="dr_booking_soe_id" id="dr_booking_soe_id"
															class="dropdown form-control" onchange="populateSob();">
															<%=mybean.PopulateSoe(mybean.comp_id)%>
														</select>
												</div>
												<div class="row"></div>
												<div class="form-element6">
													<label> Source of Business<font color="#ff0000">*</font> :&nbsp; </label>
														<span id="HintSob"> 
															<%=mybean.PopulateSOB(mybean.booking_soe_id, mybean.comp_id)%>
														</span>
												</div>
											<!-- End PickupBand  -->
											<div class="form-element6">
													<label> Campaign<font color="#ff0000">*</font>:&nbsp; </label>
														<span id="campaign">
															<%=mybean.PopulateCampaign(mybean.booking_veh_branch_id, mybean.comp_id)%>
														</span>
											</div>
											
											<div class="form-element6">
												<label> VOC:&nbsp;</label>
												<textarea name="txt_vehfollowup_voc" cols="70" rows="4"
													class="form-control" id="txt_vehfollowup_voc"><%=mybean.vehfollowup_voc%></textarea>
											</div>	
												
												<div class="row"></div>
													<center>
														<input name="addbutton" type="submit" class="button btn btn-success" id="addbutton" value="Add Enquiry" />
													</center>
										</div>
									</div>
								</div>
								<!-- 	PORTLET end details-->
							</form>
						</div>
					</div>
					</div>
				</div>
			</div>
			<!--contain end  -->
		</div>

	<!-- END CONTAINER -->
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
<script>
$(document).ready(function() {
	
	$("#addbutton").click(function() {
		document.getElementById('add_button').value = "yes";
		document.getElementById('frmenquiry').submit();
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
				      
//         function PopulateExecutive(branch_id){	
// 			showHint("../service/booking-enquiry-check.jsp?executive=yes&active=1&booking_enquiry_branch_id="+branch_id,'crmexe');
// 		  }
        
		  		  
        function PopulateModel(branch_id){
        	showHint("../service/booking-enquiry-check.jsp?veh_branch_id="+branch_id+"&model=yes", "Hintmodel");
        }
         function PopulateItem(model_id){
        	showHint("../service/booking-enquiry-check.jsp?item_model_id="+model_id+"&list_model_item=yes","model-item");
        }
         function PopulateCampaign(branch_id){
 			showHint("../service/booking-enquiry-check.jsp?veh_branch_id="+branch_id+"&campaign=yes", "campaign");
 		  }
	function populateSob(){
		 var booking_soe_id = document.getElementById('dr_booking_soe_id').value;
		 showHint('../service/booking-enquiry-check.jsp?dr_booking_sob_id=yes&booking_soe_id='+booking_soe_id, 'HintSob');
	}
	
    </script>
    
    <script>
    function PopulateBookingTime(){
    	var pickup=$("#dr_booking_type").val();
    	if(pickup!='0' ){
    		$("#followuptime").hide();
    		$("#bookingtimeband").show();
    	}
    	else{
    		$("#followuptime").show();
    		$("#bookingtimeband").hide();
    	}
    }
    </script>

</body>
</HTML>



