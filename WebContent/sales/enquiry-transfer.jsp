<jsp:useBean id="mybean" class="axela.sales.Enquiry_Transfer_Check" scope="request" />
<% mybean.doPost(request, response); %>

<div id="reload" class="modelstyle">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal">&times;</button>
		<h4 class="modal-title">
			<center>
				<b>Enquiry&nbsp;Transfer</b>
			</center>
		</h4>
	</div>

	<div class="modal-body container-fluid">
		<div id="enqtransfer1">
			<center>
				<font color="#ff0000">
					<div id="errormsg"></div>
				</font>
			</center>

			<form method="post" name="frm1" id="frm1" class="form-horizontal">
				<div class="form-element12" style="color:#ff0000" id="enquirytransfer"></div>
					
					<div class="row">
						<div class="form-element6">
							<label > Brand<font color="#ff0000">*</font>: </label>
							<div id="dr_enquiry_brand">
								<select name="dr_enquiry_brand_id" id="dr_enquiry_brand_id"
									class="form-control" onChange="PopulateBranch();PopulateModel();">
									<%=mybean.PopulateBrand(mybean.comp_id)%>
								</select>
							</div>
						</div>
					

						<div class="form-element6 ">
							<label > Branch<font color="#ff0000">*</font>: </label>
							<div id='dr_enquiry_branch'>
								<select name="dr_enquiry_branch_id" id="dr_enquiry_branch_id"
									class="form-control" onChange="PopulateTeam();">
									<option value="0">Select</option>
								</select>
							</div>
						</div>
					</div>
					
					
					
					
					<div class="row">
						<div class="form-element6 ">
							<label > Team<font color="#ff0000">*</font>: </label>
							<div id='dr_enquiry_team'>
								<select name="dr_enquiry_team_id" id="dr_enquiry_team_id"
									class="form-control" onChange="PopulateExecutive();">
									<option value="0">Select</option>
								</select>
							</div>
						</div>
						
						<div class="form-element6 ">
							<label > Sales Consultant<font color="#ff0000">*</font>: </label>
							<div id='dr_enquiry_emp'>
								<select name="dr_enquiry_emp_id" id="dr_enquiry_emp_id" class="form-control">
									<option value="0">Select</option>
								</select>
							</div>
						</div>
					</div>
					
					
					<div class="row">
						<div class="form-element6 ">
							<label > Model<font color="#ff0000">*</font>: </label>
							<div id="dr_enquiry_model">
								<select name="dr_enquiry_model_id" id="dr_enquiry_model_id"
									class="form-control" onChange="PopulateItem();">
									<option value="0">Select</option>
								</select>
							</div>
						</div>
	
						<div class="form-element6 ">
							<label > Variant<font color="#ff0000">*</font>: </label>
							<div id="dr_enquiry_item">
								<select name="dr_enquiry_item_id" id="dr_enquiry_item_id" class="form-control">
									<option value="0">Select</option>
								</select>
							</div>
						</div>
					</div>

				<input type="hidden" id="enquiry_id" name="enquiry_id" value="<%=request.getParameter("enquiry_id")%>" />
				<div class="form-element12 ">
					<center>
						<input name="transfer" class="btn btn-success" id="transfer" type='button' value="Transfer" onclick="enquiryTransfer();" />
					</center>
				</div>
			</form>
		</div>
		<div class="form-element6" id="enquirytransfer"></div>
	</div>
</div>
<!-- This is to provide UI property -->
<script>FormElements();</script>