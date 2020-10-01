<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.preowned.Preowned_Eval_Update" scope="request"/>
<%mybean.doGet(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
	<HEAD>
	<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>   
	<meta http-equiv="pragma" content="no-cache"/>
	<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt"/>
    <%@include file="../Library/css.jsp"%>
	
</HEAD>
	<body class="page-container-bg-solid page-header-menu-fixed">
    <%@include file="../portal/header.jsp" %>
    <!-- 	BODY -->
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
						<h1><%=mybean.ReturnPreOwnedName(request)%> Evaluation</h1>
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
						<li><a href="index.jsp"><%=mybean.ReturnPreOwnedName(request)%></a> &gt;</li>
						<li><a href="preowned-eval.jsp">Evaluation</a>&gt;</li>
						<li><a href="preowned-eval-list.jsp?all=yes">List Evaluation</a>&gt;</li>
						<li><a href="preowned-eval-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%> Evaluation</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					
				
					<div class="tab-pane" id="">
<!-- 					BODY START -->
						<form name="form1"  method="post" class="form-horizontal">
						<center> <font color="#ff0000"><b><%=mybean.msg%></b></font> </center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Add <%=mybean.ReturnPreOwnedName(request)%> Evaluation</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<center>Form fields marked with a red asterisk * are required.</center>
										<!-- START PORTLET BODY -->

											<div class="container-fluid ">
												<div class="form-body col-md-6 col-sm-6">
													<div class="form-group">
														<label class="col-md-3 control-label"><%=mybean.ReturnPreOwnedName(request)%>: </label>
														<div class="col-md-6" style="top: 9px">
															<input type="hidden" id="txt_preowned_title" name="txt_preowned_title"
																value="<%=mybean.preowned_title%>" />
															<a href="preowned-list.jsp?preowned_id=<%=mybean.preowned_id%>"><%=mybean.preowned_title%>
																(<%=mybean.preowned_id%>)</a>
														</div>
													</div>
												</div>

												<div class="form-body col-md-6 col-sm-6">
													<div class="form-group">
														<label class="col-md-3 control-label"> Date<b><font color="#ff0000">*</font></b>: </label>
														<div class="col-md-6">

															<% if (mybean.return_perm.equals("1")) { %>
															<input name="txt_eval_date" type="text" class="form-control datepicker"
																 id="txt_eval_date"
																value="<%=mybean.evaldate%>" size="11" maxlength="10" />
															<% } else { %>
															<%=mybean.evaldate%>
															<input name="txt_eval_date" type="hidden" class="form-control datepicker"
																 id="txt_eval_date" value="<%=mybean.evaldate%>" />
															<% } %>
														</div>
													</div>

												</div>
											</div>
											<div class="container-fluid ">
												<div class="form-body col-md-6 col-sm-6">
													<div class="form-group">
														<label class="col-md-3 control-label">Model:</label>
														<div class="col-md-6" style="top: 9px">
															<input type="hidden" id="txt_preownedmodel_name" name="txt_preownedmodel_name"
																value="<%=mybean.preownedmodel_name%>" />
															<input type="hidden" id="txt_preownedmodel_id" name="txt_preownedmodel_id"
																value="<%=mybean.preownedmodel_id%>" />
															<a href="managepreownedmodel.jsp?preownedmodel_id=<%=mybean.preownedmodel_id%>"><%=mybean.preownedmodel_name%></a>
														</div>
													</div>
												</div>

												<div class="form-body col-md-6 col-sm-6">
													<div class="form-group">
														<label class="col-md-3 control-label">Variant: </label>
														<div class="col-md-6" style="top: 9px">
															<input type="hidden" id="txt_variant_name"
																name="txt_variant_name" value="<%=mybean.variant_name%>" />
															<input type="hidden" id="txt_variant_id"
																name="txt_variant_id" value="<%=mybean.variant_id%>" />
															<a href="managepreownedvariant.jsp?variant_id=<%=mybean.variant_id%>"><%=mybean.variant_name%> </a>
														</div>
													</div>
												</div> 
											</div>
 
										</div>
	
										<%-- 
										<div class="container-fluid form-group">
											<div class="col-md-3">
												Pre Owned:
											</div>
											<div class="col-md-3">
												<input type="hidden" id="txt_preowned_title" name="txt_preowned_title" value="<%=mybean.preowned_title%>" /><a href="preowned-list.jsp?preowned_id=<%=mybean.preowned_id%>"><%=mybean.preowned_title%> (<%=mybean.preowned_id%>)</a>
											</div>
											<div class="col-md-3">
												Date<b><font color="#ff0000">*</font></b>:
											</div>
											<div class="col-md-3">

												<%
													if (mybean.return_perm.equals("1")) {
												%>
												<input name="txt_eval_date" type="text" class="form-control date-picker"
														data-date-format="dd/mm/yyyy"
													id="txt_eval_date" value="<%=mybean.evaldate%>" size="11"
													maxlength="10" />
												<%
													} else {
												%>
												<%=mybean.evaldate%><input name="txt_eval_date"
													type="hidden" class="form-control date-picker"
														data-date-format="dd/mm/yyyy" id="txt_eval_date"
													value="<%=mybean.evaldate%>" />
												<%}%>
											</div>
											
											<div class="col-md-3">
												Model:
											</div>
											<div class="col-md-3">
												<input type="hidden" id="txt_preownedmodel_name" name="txt_preownedmodel_name" value="<%=mybean.preownedmodel_name%>" />
                         <input type="hidden" id="txt_preownedmodel_id" name="txt_preownedmodel_id" value="<%=mybean.preownedmodel_id%>" />
                         <a href="managepreownedmodel.jsp?preownedmodel_id=<%=mybean.preownedmodel_id%>"><%=mybean.preownedmodel_name%></a>
											</div>
											<div class="col-md-3">
												Variant:
											</div>
											<div class="txt-align" style="top:10px">
												<input type="hidden" id="txt_variant_name" name="txt_variant_name" value="<%=mybean.variant_name%>" />
                         <input type="hidden" id="txt_variant_id" name="txt_variant_id" value="<%=mybean.variant_id%>" /><a href="managepreownedvariant.jsp?variant_id=<%=mybean.variant_id%>"><%=mybean.variant_name%> </a>
											</div>
										</div>
												
									</div>
								</div>
							</div> --%>


							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Accessories / Standard Feature Available</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
<!-- 										<div class="container-fluid"> -->
<!-- 											<div class="col-md-3"> -->
<!-- 												Stereo / Radio: -->
<!-- 											</div> -->
<!-- 											<div class="col-md-3"> -->
<%-- 												<input id="chk_eval_acc_stereo" type="checkbox" name="chk_eval_acc_stereo" <%=mybean.PopulateCheck(mybean.eval_acc_stereo)%> /> --%>
<!-- 											</div> -->
<!-- 											<div class="col-md-3"> -->
<!-- 												Make/ Condition: -->
<!-- 											</div> -->
<!-- 											<div class="col-md-3"> -->
<%-- 												<textarea name="txt_eval_acc_stereo_make"  cols="40" rows="2" class="form-control" id="txt_eval_acc_stereo_make"><%=mybean.eval_acc_stereo_make%></textarea> --%>
<!-- 											</div> -->
<!-- 											</div> -->
											<br>
											<div class="container-fluid">
											<div class="col-md-1"></div>
											<div class="col-md-1">
												Power Steering:
											</div>
											<div class="col-md-3">
												<input id="chk_eval_acc_powersteering" type="checkbox" name="chk_eval_acc_powersteering" <%=mybean.PopulateCheck(mybean.eval_acc_powersteering)%> />
											</div>
											<div class="col-md-1">
												Make/ Condition:
											</div>
											<div class="col-md-3">
												<textarea name="txt_eval_acc_powersteering_make"  cols="40" rows="2" class="form-control" id="txt_eval_acc_powersteering_make" ><%=mybean.eval_acc_powersteering_make%></textarea>
											</div>
											</div><br><div class="container-fluid">
											<div class="col-md-1"></div>
											<div class="col-md-1">
												Power Windows:
											</div>
											<div class="col-md-3">
												<input id="chk_eval_acc_powerwindows" type="checkbox" name="chk_eval_acc_powerwindows" <%=mybean.PopulateCheck(mybean.eval_acc_powerwindows)%> />
											</div>
											<div class="col-md-1">
												Make/ Condition:
											</div>
											<div class="col-md-3">
												<textarea name="txt_eval_acc_powerwindows_make"  cols="40" rows="2" class="form-control" id="txt_eval_acc_powerwindows_make" ><%=mybean.eval_acc_powerwindows_make%></textarea>
											</div></div><br><div class="container-fluid">
											<div class="col-md-1"></div>
											<div class="col-md-1">
												Central Locking (Auto/Manual):
											</div>
											<div class="col-md-3">
												<input id="chk_eval_acc_centrallocking" type="checkbox" name="chk_eval_acc_centrallocking" <%=mybean.PopulateCheck(mybean.eval_acc_centrallocking)%> />
											</div>
											<div class="col-md-1">
												Make/ Condition:
											</div>
											<div class="col-md-3">
												<textarea name="txt_eval_acc_centrallocking_make"  cols="40" rows="2" class="form-control" id="txt_eval_acc_centrallocking_make" ><%=mybean.eval_acc_centrallocking_make%></textarea>
											</div></div><br><div class="container-fluid">
											<div class="col-md-1"></div>
											<div class="col-md-1">
												Alloy Wheels:
											</div>
											<div class="col-md-3">
												<input id="chk_eval_acc_alloywheels" type="checkbox" name="chk_eval_acc_alloywheels" <%=mybean.PopulateCheck(mybean.eval_acc_alloywheels)%> />
											</div>
											<div class="col-md-1">
												Make/ Condition:
											</div>
											<div class="col-md-3">
												<textarea name="txt_eval_acc_alloywheels_make"  cols="40" rows="2" class="form-control" id="txt_eval_acc_alloywheels_make" ><%=mybean.eval_acc_alloywheels_make%></textarea>
											</div></div><br><div class="container-fluid">
											<div class="col-md-1"></div>
											<div class="col-md-1">
												No. of Keys (Original):
											</div>
											<div class="col-md-3">
												<input id="chk_eval_acc_keys" type="checkbox" name="chk_eval_acc_keys" <%=mybean.PopulateCheck(mybean.eval_acc_keys)%> />
											</div>
											<div class="col-md-1">
												Make/ Condition:
											</div>
											<div class="col-md-3">
												<textarea name="txt_eval_acc_keys_make"  cols="40" rows="2" class="form-control" id="txt_eval_acc_keys_make" ><%=mybean.eval_acc_keys_make%></textarea>
											</div></div><br><div class="container-fluid">
											<div class="col-md-1"></div>
											<div class="col-md-1">
												Tool Kit/ Jack:
											</div>
											<div class="col-md-3">
												<input id="chk_eval_acc_toolkit" type="checkbox" name="chk_eval_acc_toolkit" <%=mybean.PopulateCheck(mybean.eval_acc_toolkit)%> />
											</div>
											<div class="col-md-1">
												Make/ Condition:
											</div>
											<div class="col-md-3">
												<textarea name="txt_eval_acc_toolkit_make"  cols="40" rows="2" class="form-control" id="txt_eval_acc_toolkit_make" ><%=mybean.eval_acc_toolkit_make%></textarea>
											</div></div><br><div class="container-fluid">
											<div class="col-md-1"></div>
											<div class="col-md-1">
												Reverse Parking Sensor:
											</div>
											<div class="col-md-3">
												<input id="chk_eval_acc_parkingsensor" type="checkbox" name="chk_eval_acc_parkingsensor" <%=mybean.PopulateCheck(mybean.eval_acc_parkingsensor)%> />
											</div>
											<div class="col-md-1">
												Make/ Condition:
											</div>
											<div class="col-md-3">
												<textarea name="txt_eval_acc_parkingsensor_make"  cols="40" rows="2" class="form-control" id="txt_eval_acc_parkingsensor_make" ><%=mybean.eval_acc_parkingsensor_make%></textarea>
											</div></div><br><div class="container-fluid">
											<div class="col-md-1"></div>
											<div class="col-md-1">
												Others (If any):
											</div>
											<div class="col-md-3">
												<input id="chk_eval_acc_others" type="checkbox" name="chk_eval_acc_others" <%=mybean.PopulateCheck(mybean.eval_acc_others)%> />
											</div>
											<div class="col-md-1">
												Make/ Condition:
											</div>
											<div class="col-md-3">
												<textarea name="txt_eval_acc_others_make"  cols="40" rows="2" class="form-control" id="txt_eval_acc_others_make" ><%=mybean.eval_acc_others_make%></textarea>
											</div>
										</div><br>
										
										<div class="container-fluid">
											<div class="col-md-12">
												<%=mybean.StrHTML%>
											</div>
										</div><br>

										<div class="row">
											<div class="form-element6">
												<label > Expected Price: </label>
												<input name="txt_preowned_expectedprice" type="text"
													class="form-control" id="txt_preowned_expectedprice"
													onkeyup="toInteger('txt_preowned_expectedprice');"
													value="<%=mybean.preowned_expectedprice%>" size="15"
													maxlength="10"   />
											</div>
											
											<div class="form-element6">
												<lable> Offered Price<font color="#ff0000">*</font>: </lable>
												<input type="text" name="txt_eval_offered_price" id="txt_eval_offered_price"
													onkeyup="toInteger('txt_eval_offered_price')"
													value="<%=mybean.eval_offered_price%>" class="form-control" />
											</div>
										</div>
										
										<div class="form-element6">
											<div class="form-element12 form-element">
												<lable>Evaluation Type <b><font color="#ff0000">*</font></b>:</lable>
													<%=mybean.PopulateEvalType() %>
											</div>
											<div class="form-element12 form-element">
												<lable> Pre-Owned Consultant<b><font color="#ff0000">*</font></b>:</lable>
													<select name="dr_eval_executive" class="form-control" id="dr_eval_executive">
	                                            <%=mybean.PopulateEvalExecutive()%>
	                                            </select>
											</div>
										</div>
										
										<div class="form-element6">
											<lable> Notes:</lable>
												<textarea name="txt_eval_notes"  cols="70" rows="5" class="form-control" id="txt_eval_notes" ><%=mybean.eval_notes%></textarea>
										</div>
										
										<div class="row"></div>
										 <% if (mybean.status.equals("Update") &&!(mybean.eval_entry_by == null) && !(mybean.eval_entry_by.equals(""))) { %>
										<div class="row">
										<div class="form-element6">
											<lable>Entry By:</lable>
												<%=mybean.unescapehtml(mybean.eval_entry_by)%>
										</div><%}%>
										 <% if (mybean.status.equals("Update") &&!(mybean.eval_entry_date == null) && !(mybean.eval_entry_date.equals(""))) { %>
										 	<div class="form-element6">
											<lable> Entry Date:</lable>
											
												<%=mybean.eval_entry_date%>
											
										</div>
										</div>
										 <%}%>
										  <% if (mybean.status.equals("Update") &&!(mybean.eval_modified_by == null) && !(mybean.eval_modified_by.equals(""))) { %>
										  <div class="row">
										  	<div class="form-element6">
											<lable> Modified By:</lable>
												<%=mybean.unescapehtml(mybean.eval_modified_by)%>
										</div>
										
										  	
										  <%} %>
										   <% if (mybean.status.equals("Update") &&!(mybean.eval_modified_date == null) && !(mybean.eval_modified_date.equals(""))) { %>
										   	<div class="form-element6">
											<lable> Modified Date:</lable>
												<%=mybean.eval_modified_date%>
										</div>
										</div>
										
										   <%} %>
										 
										   <div class="form-element12">
										   <center>
										   <%if(mybean.status.equals("Add")){%>
										   		<input name="addbutton" type="submit" class="btn btn-success" id="addbutton" value="Add Evaluation" onclick="return SubmitFormOnce(document.form1,this);"/>
                        						<input type="hidden" name="add_button" value="yes"/>
										   <%} else if (mybean.status.equals("Update")){%>
										   		 <input type="hidden" name="update_button" value="yes"/>
							                     <input name="updatebutton" type="submit" class="btn btn-success" id="updatebutton" value="Update Evaluation" onclick="return SubmitFormOnce(document.form1,this);"/>
							                     <input name="delete_button" type="submit" class="btn btn-success" id="delete_button" OnClick="return confirmdelete(this)" value="Delete Evaluation"/>
										   <%} %></center>
										   <input type="hidden" name="eval_entry_by" value="<%=mybean.eval_entry_by%>"/>
					                       <input type="hidden" name="eval_entry_date" value="<%=mybean.eval_entry_date%>"/>
					                       <input type="hidden" name="eval_modified_by" value="<%=mybean.eval_modified_by%>"/>
					                       <input type="hidden" name="eval_modified_date" value="<%=mybean.eval_modified_date%>"/>
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
   <%@ include file="../Library/admin-footer.jsp" %>
    <%@include file="../Library/js.jsp"%>
    <script type="text/javascript">
			function statusallok(i){
			//var headcount = document.getElementById("txt_totalcount").value;
			//alert(i);
			var statuscount  = 0;
			//for(var i=1;i<=headcount;i++){
				statuscount = document.getElementById('txt_evalhead_statuscount_'+i).value;
				for(var j=1;j<=statuscount;j++){
				document.getElementById('dr_evalstatus_'+i+'_'+j).value=1;
			}
			//}
			}
			function GetTotal(){
				var count=document.getElementById("txt_totalcount").value;
				var total=0;
				var amt=0;
				for(var i=1;i<=count;i++){
					//alert(i);
					amt=CheckNumeric(document.getElementById("txt_evalamt"+i).value);
					total=parseInt(total)+parseInt(amt);
				}
				document.getElementById("div_totalamt").innerHTML=	parseInt(total);
				document.getElementById("txt_eval_rf_total").value=	parseInt(total);
			}
			
			function CheckNumeric(num){
		    if(isNaN(num) || num=='' || num==null)
		    {
		        num=0;
		    }
		    return num;
			} 
	</script>
    </body>
</HTML>
