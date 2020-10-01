<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Veh_Quote_Discount_Update" scope="request" />
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
<body onLoad="FormFocus()" class="page-container-bg-solid page-header-menu-fixed">
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
						<h1><%=mybean.status%>
							Discount Authorization
						</h1>
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
						<li><a href="../sales/index.jsp">Sales</a> &gt;</li>
						<li><a href="../sales/veh-quote-list.jsp?all=yes">List Quote</a> &gt;</li>
						<li><a href="../sales/veh-quote-discount-list.jsp?all=yes&quote_id=<%=mybean.quotediscount_quote_id%>">List Discount</a> &gt;</li>
						<li><a href="../sales/veh-quote-discount-update.jsp?<%=mybean.status.toLowerCase()%>=yes&quote_id=<%=mybean.quotediscount_quote_id%>&quotediscount_id=<%=mybean.quotediscount_id%>">Discount Authorization</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="#ff0000"><b><%=mybean.msg%></b></font>
					</center>
					
					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<center>
											<%=mybean.status%> Discount Authorization
										</center>
									</div>
								</div>
								
								<div class="portlet-body portlet-empty conatiner-fluid">
									<div class="tab-pane" id="">
										<form name="form1" method="post" class="form-horizontal">
											
											<center>
												<small></>Form fields marked with a red asterisk <font color="red">*</font> are required.</small>
											</center>
											<div class="row"></div>
											<div class="form-element6">
												<label>Quote ID:</label>
												<a href="veh-quote-list.jsp?quote_id=<%=mybean.quotediscount_quote_id%>"><%=mybean.quotediscount_quote_id%></a>
											</div>
											
											<div class="form-element6">
												<label>Quote Date:</label>
												<%=mybean.quote_date%>
											</div>
											
											<div class="row"></div>
											
											<% if(!mybean.so_id.equals("0") && !mybean.so_active.equals("0")) { %>
											
											<div class="form-element6">
												<label>SO ID:</label>
												<a href="veh-salesorder-list.jsp?so_id=<%=mybean.so_id%>"><%=mybean.so_id%></a>
											</div>
											
											<div class="form-element6">
												<label>SO Date:</label>
												<%=mybean.so_date%>
											</div>
											
											<% } %>	
											
											<div class="row"></div>
											
											<div class="form-element6">
												<label>Branch:</label>
												<a href="../portal/branch-summary.jsp?branch_id=<%=mybean.branch_id%>"/><%=mybean.branch_name%></a>
											</div>
											
											<div class="form-element6">
												<label>Customer:</label>
												<%=mybean.customer_name%>
											</div>
											
											<div class="row"></div>
											
											<div class="form-element6">
												<label>Contact:</label>
												<%=mybean.contact_name%>
											</div>
											
											<div class="form-element6">
												<label>Quote Amount:</label>
												<%=mybean.quote_grandtotal%>
											</div>
											
											<div class="row"></div>
											
											<div class="form-element6">
												<label>Model:</label>
												<%=mybean.model_name%>
											</div>
											
											<div class="form-element6">
												<label>Item:</label>
												<%=mybean.item_name%>
											</div>
											
											<div class="row"></div>
											
											<div class="form-element6">
												<label>Sales Consultant:</label>
												<%=mybean.ExeDetailsPopover(mybean.quote_emp_id, mybean.quote_emp_name, "")%>
											</div>
											
											<div class="row"></div>
											
											<div class="form-element6 form-element-margin">
												<label >Authorize Executive<font color="#ff0000">*</font>: </label>
												<%=mybean.ExeDetailsPopover(Integer.parseInt(mybean.quotediscount_authorize_emp_id), mybean.quotediscount_authorize_emp_name, "")%>
											</div>
											
											<div class="form-element6">
												<label >Discount Amount<font color="#ff0000">*</font>: </label>
												<input name="txt_quotediscount_requestedamount" type="text" class="form-control" id="txt_quotediscount_requestedamount"
													onKeyUp="toFloat('txt_quotediscount_requestedamount','Amount')" value="<%=mybean.quotediscount_requestedamount%>" size="50" maxlength="255" />
											</div>
											
											
											
											
											
											<div class="row"></div>
											<%
												if (mybean.status.equals("Update") && (mybean.entry_by != null) && !(mybean.entry_by.equals(""))) {
											%>
											
											<div class="form-element6">
												<label >Entry By:&nbsp;</label>
												<span >
													<%=mybean.unescapehtml(mybean.entry_by)%>
													<input name="entry_by" type="hidden" id="entry_by" value="<%=mybean.entry_by%>" />
												</span>
											</div>
											
											<div class="form-element6">
												<label >Entry Date:&nbsp;</label>
												<span >
													<%=mybean.entry_date%>
													<input type="hidden" name="entry_date" value="<%=mybean.entry_date%>" />
												</span>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && (mybean.modified_by != null) && !(mybean.modified_by.equals(""))) {
											%>
											<div class="form-element6">
												<label >Modified By:&nbsp;</label>
												<span >
													<%=mybean.unescapehtml(mybean.modified_by)%>
													<input name="modified_by" type="hidden" id="modified_by" value="<%=mybean.modified_by%>" />
												</span>
											</div>
											<div class="form-element6">
												<label >Modified Date:&nbsp;</label>
												<span >
													<%=mybean.modified_date%>
													<input type="hidden" name="modified_date" value="<%=mybean.modified_date%>" />
												</span>
											</div>
											<%
												}
											%>
											<div class="row"></div>
											<center>
												<%
													if (mybean.status.equals("Add")) {
												%>
												
												<input name="button" type="submit" class="btn btn-success" id="button" value="Add Discount"
													onClick="onPress(); return SubmitFormOnce(document.form1, this);" />
												
												<input type="hidden" name="add_button" value="yes">
												
												<%
													} else if (mybean.status.equals("Update")) {
												%>
												
												<input name="update_button" type="hidden" value="yes" />
												
												<input name="button" type="submit" class="btn btn-success" id="button" value="Update Discount"
													onClick="onPress(); return SubmitFormOnce(document.form1, this);" />
												
												<input name="delete_button" type="submit" class="btn btn-success" id="delete_button"
													OnClick="return confirmdelete(this)" value="Delete Discount" />
												<%
													}
												%>

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

