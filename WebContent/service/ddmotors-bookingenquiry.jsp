<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Booking_Enquiry" scope="request" />
<%mybean.doPost(request, response); %>
<%response.setHeader("Access-Control-Allow-Origin","*"); %>
<body>
	<form name="frmenquiry" id="frmenquiry" class="form-horizontal" method="post">
	<center>
		<font color="#ff0000" id="errorMsg"></font>
	</center>

	<br/>
		<center>
				<font>Form fields marked with a red asterisk <b><font
						color="#ff0000">*</font></b> are required. </font>
		</center><br/>
		
				<label>Preferred Location<font color="#ff0000">*</font>:&nbsp; </label>
				<select class="form-control dd" name="dr_branch" id="dr_branch">
					<%=mybean.PopulateBranches(mybean.comp_id, mybean.booking_veh_branch_id)%>
				</select><br/>

				<label>Customer:&nbsp;</label>
				<input class="form-control txbx" name="txt_booking_customer_name" type="text" id="txt_booking_customer_name" 
				onkeypress="javascript: return isAlpha(event);" value="<%=mybean.customer_name%>" size="50" maxlength="255" />
				<div class="text-left">(Name as on Invoice)</div><br/>
					
				<label>Contact<font color=red>*</font>:&nbsp; </label>
				<select name="dr_title" class="form-control dd" id="dr_title">
					<%=mybean.PopulateTitle(mybean.contact_title_id, mybean.comp_id)%>
				</select>
				<div class="text-left">Title</div>
				<br>
				<input name="txt_contact_fname" type="text" class="form-control txbx"
					id="txt_contact_fname" value="<%=mybean.contact_fname%>"
					size="30" maxlength="255" onkeypress="javascript: return isAlpha(event);" />
				<div class="text-left">First Name</div>
				<br>
				<input name="txt_contact_lname" type="text" class="form-control txbx"
					id="txt_contact_lname" onkeypress="javascript: return isAlpha(event);" value="<%=mybean.contact_lname%>"
					size="30" maxlength="255" /> 
				<div class="text-left">Last Name</div>
				<br/>
		
				<label>Mobile<font color="#ff0000">*</font>:&nbsp; </label>
				<input name="txt_contact_mobile1" type="text" class="form-control txbx text-left" id="txt_contact_mobile1" onkeypress="javascript: return isNumber(event);"
					value="<%=mybean.contact_mobile1%>" size="32" maxlength="13" /> <div class="text-left">(91-9999999999)</div> <div class="text-left" id="mobileError"></div>
					<br/>
		
				<label>Email:&nbsp;</label>
				<input name="txt_contact_email1" type="text" class="form-control txbx" id="txt_contact_email1"
					value="<%=mybean.contact_email1%>" size="32" maxlength="100" />
				<div class="text-left" id="emailError"></div><br/>
				

				<label>Maruti Suzuki Models<font color="#ff0000">*</font>: </label>
					<select class="form-control dd" id="dr_booking_veh_model_id" name="dr_booking_veh_model_id">
						<%=mybean.PopulateModel(mybean.booking_veh_model_id, mybean.comp_id)%>
					</select><br/>
					
				<!-- ddwebsite check -->					
				<input name="ddwebsite_check" type="hidden" class="button btn btn-success" id="ddwebsite_check" value="yes" />		
		
				<label>Reg.No<font color="#ff0000">*</font>:&nbsp; </label>
					<input class="form-control txbx" name="txt_booking_veh_reg_no" type="text" id="txt_booking_veh_reg_no" size="50" maxlength="20"
					value="<%=mybean.booking_veh_reg_no%>" /><br/>
				
				<!-- CRM Executive: JYOTHI : Emp ID:681 -->
				<input name="dr_booking_veh_executive" type="hidden" class="button btn btn-success" id="dr_booking_veh_executive" value="681" />
						
				<!-- Booking Type: Select -->
				<input name="dr_booking_type" type="hidden" class="button btn btn-success" id="dr_booking_type" value="0" />
			
				<label>Best time to contact you:&nbsp; </label>
					<input name="txt_nextfollowup_time" onmouseover="initdatepicker();" type="text" class="form-control datetimepicker txbx" id="txt_nextfollowup_time" 
					value="<%=mybean.followup_nextfollowup_time%>" size="20" maxlength="16"/><br/>
			
				<!-- Source of Enquiry: Digital : ID:7 -->
				<input name="dr_booking_soe_id" type="hidden" class="button btn btn-success" id="dr_booking_soe_id" value="7" />
				
				<!-- Source of Business: Website : ID:8 -->
				<input name="dr_booking_sob_id" type="hidden" class="button btn btn-success" id="dr_booking_sob_id" value="8" />
				
				<!-- Campaign: Others : ID:2 -->
				<input name="dr_booking_campaign_id" type="hidden" class="button btn btn-success" id="dr_booking_campaign_id" value="2" />
			
				<label>Additional Requests Or Comments:&nbsp;</label>
				<textarea name="txt_vehfollowup_voc" cols="70" rows="4" class="form-control dd" id="txt_vehfollowup_voc">
					<%=mybean.vehfollowup_voc%>
				</textarea><br/>
				
			<center>
				<input name="addbutton" type="hidden" class="button btn btn-success" id="addbutton" value="Add Enquiry" />
				<input name="add_button" type="button" class="button btn btn-success" id="add_button" onclick="mysubmit();" value="Add Enquiry"/>
			</center>
	</form>
</body>