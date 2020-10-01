<jsp:useBean id="mybean" class="axela.inbound.Executive_Send_Sms"
	scope="request" />
<% mybean.doPost(request, response); %>
<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<!-- END PAGE HEAD-->
			<!--- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
			<div class="page-content-inner">
				<div class="container-fluid">
					<!-- END PAGE BREADCRUMBS -->
					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<form name="form1" id="form1" method="post" class="form-horizontal">
								<center>
									<font color="#ff0000"><b><%=mybean.msg%><div id="errorMsg"></div></b></font>
								</center>


								<!-- 	PORTLET customner details-->
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Customer Details</div>
									</div>
									<div class="portlet-body portlet-empty container-fluid">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<!--     start customer details -->
											
										<div class="form-element9 form-element-center">
											<label>Executive<font color="#ff0000">*</font>:
											</label> <select class="form-control select2" id="allexecutives"
												name="allexecutives">
												<%=mybean.crecheck.PopulateAllExecutives(mybean.comp_id, mybean.exe_id)%>
											</select>
										</div>
						
										<div class="form-element9 form-element-center">
											<label> Contact Name<font color=red>*</font>:&nbsp;
											</label> <input name="contact_name" type="text" class="form-control"
												id="contact_name" value="<%=mybean.contact_name%>" size="255"
												maxlength="255" />
												<input type="hidden" id="contact_id" name="contact_id" value="<%=mybean.contact_id %>" />
												<input type="hidden" id="customer_id" name="customer_id" value="<%=mybean.customer_id %>" />
												<input type="hidden" id="call_id" name="call_id" value="<%=mybean.call_id %>" />
										</div>

										<div class="form-element9 form-element-center">
											<label> Contact Mobile<font color=red>*</font>:&nbsp;
											</label> <input name="contact_mobile" type="text" class="form-control"
												id="contact_mobile" value="<%=mybean.contact_mobile%>" size="255"
												maxlength="13" onKeyUp="toPhone('contact_mobile','Mobile')" />
										</div>
										
										<center>
											<button class="btn btn-success" onclick="SendSms();"
											id="sendSms" name="sendSms" value="Send SMS">Send SMS </button>
										</center>
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
<script src="../assets/js/select2.full.min.js" type="text/javascript"></script>
<script src="../assets/js/components-select2.min.js" type="text/javascript"></script>
<script> FormElements();</script>
