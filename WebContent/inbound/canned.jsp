<jsp:useBean id="mybean" class="axela.inbound.Canned" scope="request" />
<% mybean.doPost(request, response); %>
<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
			<div class="page-content-inner">
				<div class="container-fluid">
					<!-- END PAGE BREADCRUMBS -->
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
									Canned Messages
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
			
										<center>
											<div><span id="sentmsg"></span></div>
											<div><font color="#ff0000"><b><%=mybean.msg%></b></font></div>
										</center>
										<br>
										<div class="container-fluid">
<%-- 										<% if (mybean.home.equals("yes")) { %> --%>
										<div class="row">
												<div class="col-md-1 text-right">
														<label>Brand: </label>
												</div>
												<div class="col-md-3" style="margin-top: -20px;">
												<select name="dr_brand" id="dr_brand" class="form-element"
													onchange="PopulateCannedEmail();PopulateCannedSMS();">
													<%=mybean.PopulatePrincipal( mybean.brand_id, mybean.comp_id, request)%>
												</select>
												</div>
												
												<div class="col-md-1 text-right">
														<label>Type: </label>
												</div>
												<div class="col-md-3" style="margin-top: -20px;">
												<select name="dr_branchtype" id="dr_branchtype" class="form-element"
													onchange="PopulateCannedEmail();PopulateCannedSMS();">
													<%=mybean.PopulateBranchType(mybean.branchtype_id, request)%>
												</select>
												</div>
												
							<!-- 					<div class="col-md-2 text-right"> -->
							<!-- 							<label> Search: </label> -->
							<!-- 					</div> -->
												<div class="col-md-3" style="margin-top: -10px;">
													<input name="txt_search" type="txt_search" maxlength="255"
													class="form-control" id="txt_search" 
													value="<%=mybean.txt_search%>"
													onKeyUp="getEmail('txt_search',this,'listEmail');getSMS('txt_search',this,'listSMS');"/>
												</div>
											</div>	
										<div class="row"></div>			
<%-- 										<%} %> --%>
										<div class="col-md-6">
													<div class="portlet box" style="height: auto;">
														<div class="portlet-title" style="text-align: center">
															<div class="caption" style="float: none">Email Messages</div>
														</div>
							
														<div class="portlet-body portlet-empty" id="listEmail">
																<%=mybean.StrHTML %>
														</div>
													</div>
										</div>
											
										<div class="col-md-6">
											<div class="portlet box" style="height: auto;">
												<div class="portlet-title" style="text-align: center">
													<div class="caption" style="float: none">SMS Messages</div>
												</div>
							
												<div class="portlet-body portlet-empty" id="listSMS">
													<%=mybean.StrHTML1 %>
												</div>
											</div>
										</div>
										
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script language="JavaScript" type="text/javascript">

// PopulateCannedEmail();PopulateCannedSMS();
</script>


