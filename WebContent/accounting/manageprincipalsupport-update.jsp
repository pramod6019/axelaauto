<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.accounting.ManagePrincipalSupport_Update" scope="request" />
<% mybean.doPost(request, response); %>
<jsp:setProperty name="mybean" property="*" />
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt"/>
<%@include file="../Library/css.jsp"%>
</head>

<body onLoad="FormFocus();" class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>

	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1><%=mybean.status%> Principal Support </h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<div class="page-content-inner">
						<ul class="page-breadcrumb breadcrumb">
							<li><a href="../portal/home.jsp">Home</a> &gt;</li>
							<li><a href="../accounting/index.jsp">Accounting</a> &gt;</li>
							<li><a href="manageprincipalsupport.jsp?all=yes"> List Principal Support</a> &gt;</li>
							<li><a href="manageprincipalsupport-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%> Principal Support</a><b>:</b></li>
						</ul>
						<!-- END PAGE BREADCRUMBS -->
						<center> <font color="#ff0000"><b> <%=mybean.msg%> </b></font> </center>
					
						
										<form name="form1" class="form-horizontal" method="post">
											
											
											<div class="portlet box  ">
												<div class="portlet-title" style="text-align: center">
													<div class="caption" style="float: none">
														<%=mybean.status%> Principal Support
													</div>
												</div>
												<center>
												<font size="1">Form fields marked with a red asterisk
												<font color=#ff0000>*</font> are required.
												</font>
											</center><br>
												
												<div class="portlet-body portlet-empty container-fluid">
													<div class="form-element6">
														<label>Brand<font color=#ff0000>*</font>:</label>
															<select name="dr_brand" class="form-control" id="dr_brand"
																 onChange="PopulateModels();">
																<%=mybean.PopulatePrincipal(mybean.brand_id, mybean.comp_id, request)%>
															</select>
													</div>
											
											
													<div class="form-element6">
														<label>Model<font color=#ff0000>*</font>: </label>
															<span id="modelHint">
															<%=mybean.PopulateModel(mybean.principalsupport_model_id, mybean.brand_id, mybean.comp_id)%>
															</span>
													</div>
											
											
													<div class="form-element6">
														<label>Fuel Type<font color=#ff0000>*</font>: </label>
															<select id="dr_fuel_type_id" name="dr_fuel_type_id" class="form-control" >
<%-- 															<%if(mybean.status.equals("Update")){ %>disabled<%} %> --%>
															<%=mybean.PopulateFuelType(mybean.principalsupport_fueltype_id, request)%>
														</select>
													</div>
											
											
													<div class="form-element6">
														<label>Month<font color=#ff0000>*</font>: </label>
															<select name="dr_month" class="form-control" id="dr_month" >
<%-- 															<%if(mybean.status.equals("Update")){ %>disabled<%} %> --%>
																<%=mybean.PopulateMonth(mybean.principalsupport_month)%>
															</select>
													</div>
												</div>
											</div>
											<div class="row"></div>
											<div class="portlet box  ">
												<div class="portlet-title" style="text-align: center">
													<div class="caption" style="float: none">
														Customer Offer
													</div>
												</div>
												<div class="portlet-body portlet-empty container-fluid">
													<div class="form-element6">
														<label>Customer 3rd Year Ext Wty:</label>
															<input class="form-control" type="text" name="txt_customer3rdyearextwty" maxlength="10"
																 id="txt_customer3rdyearextwty" value="<%=mybean.principalsupport_customer3rdyearextwty%>" onkeyup="toInteger(this.id);"></input>
													</div>
													<div class="form-element6">
														<label>Customer RSA:</label>
															<input class="form-control" type="text" name="txt_customerrsa" maxlength="10"
															 id="txt_customerrsa" value="<%=mybean.principalsupport_customerrsa%>" onkeyup="toInteger(this.id);"></input>
													</div>
												
													<div class="form-element6">
														<label>Customer Insurance:</label>
														<input class="form-control" type="text" name="txt_customerinsurance" maxlength="10"
															 id="txt_customerinsurance" value="<%=mybean.principalsupport_customerinsurance%>" onkeyup="toInteger(this.id);"></input>
													</div>
											
													<div class="form-element6">
														<label>Customer Cash Discount:</label>
															<input class="form-control" type="text" name="txt_customercashdiscount" maxlength="10"
															 id="txt_customercashdiscount" value="<%=mybean.principalsupport_customercashdiscount%>" onkeyup="toInteger(this.id);"></input>
													</div>
											
													<div class="form-element6">
														<label>Customer Exchange:</label>
														<input class="form-control" type="text" name="txt_customerexchange" maxlength="10"
															 id="txt_customerexchange" value="<%=mybean.principalsupport_customerexchange%>" onkeyup="toInteger(this.id);"></input>
													</div>
												
													<div class="form-element6">
														<label>Customer Loyalty:</label>
															<input class="form-control" type="text" name="txt_customerloyalty" maxlength="10"
															 id="txt_customerloyalty" value="<%=mybean.principalsupport_customerloyalty%>" onkeyup="toInteger(this.id);"></input>
													</div>
												
													<div class="form-element6">
														<label>Customer Govt Emp Scheme:</label>
														<input class="form-control" type="text" name="txt_customergovtempscheme" maxlength="10"
															 id="txt_customergovtempscheme" value="<%=mybean.principalsupport_customergovtempscheme%>" onkeyup="toInteger(this.id);"></input>
													</div>
											
													<div class="form-element6">
														<label>Customer Other Benefit:</label>
														<input class="form-control" type="text" name="txt_customerotherbenefit" maxlength="10"
															 id="txt_customerotherbenefit" value="<%=mybean.principalsupport_customerotherbenefit%>" onkeyup="toInteger(this.id);"></input>
													</div>
												
													<div class="form-element6">
														<label>Customer Corporate:</label>
														<input class="form-control" type="text" name="txt_customercorporate" maxlength="10"
															 id="txt_customercorporate" value="<%=mybean.principalsupport_customercorporate%>" onkeyup="toInteger(this.id);"></input>
													</div>
											
												</div>
											</div>	
												
											<div class="row"></div>	
											<div class="portlet box  ">
												<div class="portlet-title" style="text-align: center">
													<div class="caption" style="float: none">
														Dealer Support
													</div>
												</div>	
												
												<div class="portlet-body portlet-empty container-fluid">
													<div class="form-element6">
														<label>Extended Warranty:</label>
															<input class="form-control" type="text" name="txt_extwty" maxlength="10"
															 id="txt_extwty" value="<%=mybean.principalsupport_extwty%>" onkeyup="toInteger(this.id);"></input>
													</div>
											
													<div class="form-element6">
														<label>RSA:</label>
														<input class="form-control" type="text" name="txt_rsa" maxlength="10"
															 id="txt_rsa" value="<%=mybean.principalsupport_rsa%>" onkeyup="toInteger(this.id);"></input>
													</div>
												
								<!-- ::::: -->
												
												
													<div class="form-element6">
														<label>Insurance: </label>
														<input class="form-control" type="text" name="txt_insurance" id="txt_insurance" maxlength="10"
														 value= "<%=mybean.principalsupport_insurance%>" onkeyup="toInteger(this.id);"> </input>
													</div>
		
													<div class="form-element6">
														<label>Cash Discount: </label>
														<input class="form-control" type="text" name="txt_cash_discount" id="txt_cash_discount"
														 maxlength="10" value= "<%=mybean.principalsupport_cashdiscount%>" onkeyup="toInteger(this.id);"></input>
													</div>
											
													<div class="form-element6">
														<label>Exchange: </label>
														<input class="form-control" type="text" name="txt_exchange" id="txt_exchange"
														maxlength="10" value= "<%=mybean.principalsupport_exchange%>" onkeyup="toInteger(this.id);"></input>
													</div>
											
													<div class="form-element6">
														<label>Loyalty: </label>
														<input class="form-control" type="text" name="txt_loyalty" id="txt_loyalty"
														maxlength="10" value= "<%=mybean.principalsupport_loyalty%>" onkeyup="toInteger(this.id);"></input>
													</div>
											
					<!-- .......... -->
												
													<div class="form-element6">
														<label>Govt Emp Scheme: </label>
														<input class="form-control" type="text" name="txt_govtempscheme" id="txt_govtempscheme"
														maxlength="10" value= "<%=mybean.principalsupport_govtempscheme%>" onkeyup="toInteger(this.id);"></input>
													</div>
												
													<div class="form-element6">
														<label>Monthly Additional Benefit: </label>
														<input class="form-control" type="text" name="txt_monthlyadnbenefit" id="txt_monthlyadnbenefit"
														maxlength="10" value= "<%=mybean.principalsupport_monthlyadnbenefit%>" onkeyup="toInteger(this.id);"></input>
													</div>
												
											
									<!-- ....... -->
									
													<div class="form-element6">
														<label>Corporate: </label>
														<input class="form-control" type="text" name="txt_corporate" id="txt_corporate"
														maxlength="10"	 value= "<%=mybean.principalsupport_corporate%>" onkeyup="toInteger(this.id);"></input>
													</div>
				<!-- start -->							
												</div>
											</div>
											
											<div class="row"></div>
											<div class="portlet box  ">
												<div class="portlet-title" style="text-align: center">
													<div class="caption" style="float: none">
														Wholesale Target Support
													</div>
												</div>
												<div class="portlet-body portlet-empty container-fluid">
													<div class="form-element6">
														<label>EB1 From Date: </label>
														<input class="form-control datepicker" type="text" name="txt_eb1_fromdate" id="txt_eb1_fromdate"
														maxlength="14"  value= "<%=mybean.principalsupport_eb1_fromdate%>" onkeyup="toInteger(this.id);"></input>
													</div>
												
													<div class="form-element6">
														<label>EB1 To Date: </label>
														<input class="form-control datepicker" type="text" name="txt_eb1_todate" id="txt_eb1_todate"
														maxlength="14"  value= "<%=mybean.principalsupport_eb1_todate%>" onkeyup="toInteger(this.id);"></input>
													</div>
											
													<div class="form-element6">
														<label>EB1 Wholesale Target Count:</label>
														<input class="form-control" type="text" name="txt_eb1_wholesaletargetcount" id="txt_eb1_wholesaletargetcount"
														maxlength="10"  value= "<%=mybean.principalsupport_eb1_wholesaletargetcount%>" onkeyup="toInteger(this.id);"></input>
													</div>
												
													<div class="form-element6">
														<label>EB1 Wholesale Target Perc:</label>
														<input class="form-control" type="text" name="txt_eb1_wholesaletargetperc" id="txt_eb1_wholesaletargetperc"
														maxlength="14"  value= "<%=mybean.principalsupport_eb1_wholesaletargetperc%>" onkeyup="toInteger(this.id);"></input>
													</div>
												
												
													<div class="form-element6">
														<label>EB1 Retail Target Count:</label>
														<input class="form-control" type="text" name="txt_eb1_retailtargetcount" id="txt_eb1_retailtargetcount"
														maxlength="10"  value= "<%=mybean.principalsupport_eb1_retailtargetcount%>" onkeyup="toInteger(this.id);"></input>
													</div>
												
													<div class="form-element6">
														<label>EB1 Retail Target Perc:</label>
														<input class="form-control" type="text" name="txt_eb1_retailtargetperc" id="txt_eb1_retailtargetperc"
														maxlength="10"  value= "<%=mybean.principalsupport_eb1_retailtargetperc%>" onkeyup="toInteger(this.id);"></input>
													</div>
											
											
											
													<div class="form-element6">
														<label>EB2 From Date: </label>
														<input class="form-control datepicker" type="text" name="txt_eb2_fromdate" id="txt_eb2_fromdate"
														maxlength="14"  value= "<%=mybean.principalsupport_eb2_fromdate%>" onkeyup="toInteger(this.id);"></input>
													</div>
												
												
													<div class="form-element6">
														<label>EB2 To Date: </label>
														<input class="form-control datepicker" type="text" name="txt_eb2_todate" id="txt_eb2_todate"
														maxlength="14"  value= "<%=mybean.principalsupport_eb2_todate%>" onkeyup="toInteger(this.id);"></input>
													</div>
											
													<div class="form-element6">
														<label>EB2 Wholesale Target Count:</label>
														<input class="form-control" type="text" name="txt_eb2_wholesaletargetcount" id="txt_eb2_wholesaletargetcount"
														maxlength="10"  value= "<%=mybean.principalsupport_eb2_wholesaletargetcount%>" onkeyup="toInteger(this.id);"></input>
													</div>
												
													<div class="form-element6">
														<label>EB2 Wholesale Target Perc:</label>
														<input class="form-control" type="text" name="txt_eb2_wholesaletargetperc" id="txt_eb2_wholesaletargetperc"
														maxlength="14"  value= "<%=mybean.principalsupport_eb2_wholesaletargetperc%>" onkeyup="toInteger(this.id);"></input>
													</div>
													
												
												<div class="form-element6">
													<label>EB2 Retail Target Count:</label>
														<input class="form-control" type="text" name="txt_eb2_retailtargetcount" id="txt_eb2_retailtargetcount"
														maxlength="10"  value= "<%=mybean.principalsupport_eb2_retailtargetcount%>" onkeyup="toInteger(this.id);"></input>
												</div>
												
												<div class="form-element6">
													<label>EB2 Retail Target Perc:</label>
														<input class="form-control" type="text" name="txt_eb2_retailtargetperc" id="txt_eb2_retailtargetperc"
														maxlength="10"  value= "<%=mybean.principalsupport_eb2_retailtargetperc%>" onkeyup="toInteger(this.id);"></input>
												</div>
<!-- 												buttons -->
												<% if (mybean.status.equals("Update")&& !(mybean.entry_by == null) 
													&& !(mybean.entry_by.equals(""))) { %>
											
												<div class="form-element6">
													<label>Entry By : </label>
														<%=mybean.unescapehtml(mybean.entry_by)%>
														<input name="entry_by" type="hidden" id="entry_by"
															value="<%=mybean.unescapehtml(mybean.principalsupport_entry_id)%>">
														</input>
												</div>
											
												<div class="form-element6">
													<label>Entry Date : </label>
														<%=mybean.entry_date%>
														<input type="hidden" name="entry_date" value="<%=mybean.principalsupport_entry_date%>"/>
												</div>
											
											<% } %>
											<% if (mybean.status.equals("Update")&& !(mybean.modified_by.equals("No Executive found!")) 
													&& !(mybean.modified_by.equals(""))) {  %>
											
												<div class="form-element6">
													<label>Modified By : </label>
														<%=mybean.unescapehtml(mybean.modified_by)%>
														<input name="modified_by" type="hidden" id="modified_by"
															value="<%=mybean.unescapehtml(mybean.principalsupport_modified_id)%>"/>
												</div>
											
											
												<div class="form-element6">
													<label> Modified Date:</label>
												<%=mybean.modified_date%>
												<input type="hidden" name="modified_date"
												value="<%=mybean.modified_date%>">
												</div>

											<div class="row"></div>
											
											<% } %>
											<% if (mybean.status.equals("Add")) { %>
											
											<center>
												<input name="addbutton" type="submit"
													class="btn btn-success" id="addbutton" value="Add Principal Support"
													onClick="return SubmitFormOnce(document.form1, this);" />
													<input type="hidden" id="add_button" name="add_button" value="yes" />
											</center>
											<% } else if (mybean.status.equals("Update")) { %>
											<center>
												<input type="hidden" id="update_button" name="update_button" value="yes" />
													<input name="updatebutton" type="submit"
													class="btn btn-success" id="updatebutton" value="Update Principal Support"
													onClick="return SubmitFormOnce(document.form1, this);" />
													
													<input name="delete_button" type="submit" class="btn btn-success"
													id="delete_button" OnClick="return confirmdelete(this)" value="Delete Principal Support" />
											</center>
											
											<% } %>
												
												
												
											</div>
											</div>
											<div class="row"></div>
											
											
						<!-- end -->
											
											<input type="hidden" name="principalsupport_id" value="<%=mybean.principalsupport_id%>">
										</form>
									</div><!-- //tab-pane -->

				</div>
			</div>
		</div>
	</div>
		<%@include file="../Library/js.jsp"%>
	<%@include file="../Library/admin-footer.jsp"%>
	<script language="JavaScript" type="text/javascript">
// 		function FormFocus() { //v1.0
// 			document.form1.txt_tax_name.focus();
// 		}
		
		function PopulateModels() { //v1.0
			
			var brand_id=document.getElementById("dr_brand").value;
			showHint('../accounting/acc-check.jsp?brand_id='+brand_id+'&model=manageprincipalsupport','modelHint');
	    }
	</script>
</body>
</html>
