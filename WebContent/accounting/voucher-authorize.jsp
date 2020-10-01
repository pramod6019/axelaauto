<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.accounting.Voucher_Authorize"
	scope="request" />
<%mybean.doGet(request, response);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%= mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>   
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp"%>
</HEAD>
<body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
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
						<h1>Authorize Voucher</h1>
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
						<li><a href="../accounting/home.jsp">Home</a> &gt; </li>
						<li><a href="voucher-list.jsp?all=yes">List Voucher</a> &gt; </li>
						<li><a href="voucher-authorize.jsp?voucher_id=<%=mybean.voucher_id%>&vouchertype_id=<%=mybean.vouchertype_id%>&voucherclass_id=<%=mybean.voucherclass_id%>">Authorize Voucher</a>:</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
						
						<div class="tab-pane" id="">
						<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Authorize Voucher</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="form1" id="form1" method="post" class="form-horizontal">
											<input type="hidden" name="txt_voucher_branch_id" id="txt_voucher_branch_id" value="<%=mybean.voucher_branch_id %>"/>
											<input type="hidden" name="txt_voucher_no_check" id="txt_voucher_no_check" value="<%=mybean.voucher_no_check %>"/> 
											<input type="hidden" name="txt_voucher_authorizetime" id="txt_voucher_authorizetime" value="<%=mybean.voucher_authorizetime %>"/>
											<input type="hidden" name="txt_voucher_authorize_id" id="txt_voucher_authorize_id" value="<%=mybean.voucher_authorize_id %>"/>		
											<div class="form-element6 form-element-center">
													<label>Voucher ID: &nbsp </label>
														<a href="voucher-list.jsp?voucherclass_id=<%=mybean.voucherclass_id%>&vouchertype_id=<%=mybean.vouchertype_id%> 
														&voucher_id= <%=mybean.voucher_id%>"><%=mybean.voucher_id%></a>
											</div>
											<% if(!mybean.voucher_no.equals("")){%>
											<div class="form-element6 form-element-center">
													<label>Voucher No:&nbsp </label>
													<a href="voucher-list.jsp?voucherclass_id=<%=mybean.voucherclass_id%>&vouchertype_id=<%=mybean.vouchertype_id%> 
														&voucher_id= <%=mybean.voucher_id%>"><%=mybean.voucher_no%></a>
											</div>
											<% }%>
											<div class="form-element6 form-element-center">
													<label>Authorize: &nbsp </label>
														<input id="chk_voucher_authorize"
											type="checkbox" name="chk_voucher_authorize"
											<%=mybean.PopulateCheck(mybean.voucher_authorize)%> />
											</div>
	<% if (!(mybean.voucher_authorize_time == null) && !(mybean.voucher_authorize_time.equals(""))) { %>
											<div class="form-element6 form-element-center">
													<label>Authorized By: &nbsp; </label>
														<%=mybean.unescapehtml(mybean.voucher_authorized_by)%>
											<input name="quote_authorized_by" type="hidden"
											id="quote_authorized_by"
											value="<%=mybean.unescapehtml(mybean.voucher_authorized_by)%>" />
											</div>
											<div class="form-element6 form-element-center">
													<label>Authorize Date: &nbsp; </label>
														<%=mybean.voucher_authorizetime%> <input
														type="hidden" id="quote_authdate" name="quote_authdate"
														value="<%=mybean.voucher_authorizetime%>" />
											</div>
											<% } %>
													<center>
													
														<input type="hidden" name="update_button" value="yes"/> 
														<input type="hidden" id="Update" name="Update" value="yes"/>
														<input name="updatebutton" id="updatebutton" type="submit"
															class="btn btn-success" value="Update Voucher"
															onClick="return SubmitFormOnce(document.form1, this);" /></center>
													
											<input type="text" value="<%=mybean.old_voucher_authorize%>" id="old_voucher_authorize" name="old_voucher_authorize" hidden />
											<input type="text" value="<%=mybean.voucher_active%>" id="txt_voucher_active" name="txt_voucher_active" hidden />
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

	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
	</body>
</HTML>
