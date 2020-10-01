<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Configure_Sales"
	scope="request" />
<%
	mybean.doGet(request, response);
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
						<h1>Configure Sales</h1>
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
						<li><a href="../portal/manager.jsp#sales">Business Manager</a> &gt;</li>
						<li><a href="../portal/manager.jsp#sales">Sales</a> &gt;</li> <li><a href="configure-sales.jsp">Configure Sales</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="#ff0000"><b><%=mybean.msg%></b></font>
					</center>
					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Configure Sales
									</div>
								</div>
								
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										
										<center>
											<font size="1">Form fields marked with a red asterisk
												<b><font color="#ff0000">*</font></b> are required.
											</font><br>
										</center>
										
										<form name="form1" method="post" class="form-horizontal">
										<br />
											<div class="form-element3">
												<label> Enable Leads: </label>
												<input type="checkbox" name="chk_config_sales_leads"
													<%=mybean.PopulateCheck(mybean.config_sales_leads)%> />
												</div>
											
											
											<div class="form-element3">
												<label> Enable Enquiry: </label>
												<input type="checkbox" name="chk_config_sales_enquiry"
													<%=mybean.PopulateCheck(mybean.config_sales_enquiry)%> />
												</div>
											
											
											<div class="form-element3">
												<label> Enable Quotes: </label>
												<input type="checkbox" name="chk_config_sales_quote"
													<%=mybean.PopulateCheck(mybean.config_sales_quote)%> />
												</div>
											
											
											<div class="form-element3">
												<label> Enable Sales Orders: </label>
												<input type="checkbox" name="chk_config_sales_salesorder"
													<%=mybean.PopulateCheck(mybean.config_sales_salesorder)%> />
												</div>
											
											
											<div class="form-element3">
												<label> Enable Invoices: </label>
												<input type="checkbox" name="chk_config_invoice_invoice"
													<%=mybean.PopulateCheck(mybean.config_invoice_invoice)%> />
												</div>
											
											
											<div class="form-element3">
												<label> Enable Balance Payments: </label>
												<input type="checkbox"
													name="chk_config_sales_balancepayments"
													<%=mybean.PopulateCheck(mybean.config_sales_balancepayments)%> />
												</div>
										
											
											<div class="form-element3">
												<label> Enable Campaigns: </label>
													<input type="checkbox" name="chk_config_sales_campaign"
														<%=mybean.PopulateCheck(mybean.config_sales_campaign)%> />
												</div>
										
											
											<div class="form-element3">
												<label> Enable Targets: </label>
												<input type="checkbox" name="chk_config_sales_target"
													<%=mybean.PopulateCheck(mybean.config_sales_target)%> />
												</div>
										
											
											<div class="form-element3">
												<label> Enable Teams: </label>
												<input type="checkbox" name="chk_config_sales_teams"
													<%=mybean.PopulateCheck(mybean.config_sales_teams)%> />
												</div>
											
											
											<div class="form-element3">
												<label> Lead for Enquiry: </label>
												<input type="checkbox"
													name="chk_config_sales_lead_for_enquiry"
													<%=mybean.PopulateCheck(mybean.config_sales_lead_for_enquiry)%> />
												</div>
											
											
											<div class="form-element3">
												<label> Enquiry for Quote: </label>
												<input type="checkbox"
													name="chk_config_sales_enquiry_for_quote"
													<%=mybean.PopulateCheck(mybean.config_sales_enquiry_for_quote)%> />
												</div>
											
											
											<div class="form-element3">
												<label> Quote for Sales Order: </label>
												<input type="checkbox" name="chk_config_sales_quote_for_so"
													<%=mybean.PopulateCheck(mybean.config_sales_quote_for_so)%> />
												</div>
										
											
											<div class="form-element3">
												<label> Lead Ref No.: </label>
												<input type="checkbox" name="chk_config_sales_lead_refno"
													<%=mybean.PopulateCheck(mybean.config_sales_lead_refno)%> />
												</div>
										
											
											<div class="form-element3">
												<label> Enquiry QCS No.: </label>
												<input type="checkbox"
													name="chk_config_sales_enquiry_refno"
													<%=mybean.PopulateCheck(mybean.config_sales_enquiry_refno)%> />
												</div>
											
											
											<div class="form-element3">
												<label>Quote Ref No.: </label>
												<input type="checkbox" name="chk_config_sales_quote_refno"
													<%=mybean.PopulateCheck(mybean.config_sales_quote_refno)%> />
												</div>
										
											
											<div class="form-element3">
												<label> Sales Order Ref No.: </label>
												<input type="checkbox" name="chk_config_sales_so_refno"
													<%=mybean.PopulateCheck(mybean.config_sales_so_refno)%> />
												</div>
											
											
											<div class="form-element3">
												<label>Invoice Ref No.: </label>
												<input type="checkbox"
													name="chk_config_invoice_invoice_refno"
													<%=mybean.PopulateCheck(mybean.config_invoice_invoice_refno)%> />
												</div>
											
											
											<div class="form-element3">
												<label> Receipt Ref No.: </label>
												<input type="checkbox"
													name="chk_config_invoice_receipt_refno"
													<%=mybean.PopulateCheck(mybean.config_invoice_receipt_refno)%> />
												</div>
											
											
											<div class="form-element3">
												<label> Enable Source of Enquiry: </label>
												<input type="checkbox" name="chk_config_sales_soe"
													<%=mybean.PopulateCheck(mybean.config_sales_soe)%>>
												</div>
											
											
											<div class="form-element3">
												<label> Enable Source of Business: </label>
												<input type="checkbox" name="chk_config_sales_sob"
													<%=mybean.PopulateCheck(mybean.config_sales_sob)%>>
											</div>
											
											<div class="form-element6">
												<label> Enquiry Domain: </label>
												<input name="txt_config_sales_enquiry_domain" type="text"
													class="form-control" id="txt_config_sales_enquiry_domain"
													value="<%=mybean.config_sales_enquiry_domain%>" size="50"
													maxlength="255">
												</div>
												
												<div class="form-element6">
													<label> Enquiry Thankyou URL: </label>
													<input name="txt_config_sales_enquiry_thankyou_url"
														type="text" class="form-control"
														id="txt_config_sales_enquiry_thankyou_url"
														value="<%=mybean.config_sales_enquiry_thankyou_url%>"
														size="50" maxlength="255">
													</div>
												
											<center>
												<input name="update_button" type="submit" class="btn btn-success" id="update_button" value="Update" />
											</center>
											
										</form>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
	<!-- END CONTAINER -->
	<%@include file="../Library/admin-footer.jsp"%>
	
	<%@include file="../Library/js.jsp"%>   
</body>
</HTML>
