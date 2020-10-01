<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.accounting.Voucher_List" scope="request" />
<% mybean.doPost(request, response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta name="viewport" content="width=device-width, initial-scale=1"> 
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp" %>	
<style>
.ui-menu {
	width: 150px;
}

.ui-menu-item a {
	text-align: left;
	line-height: 150%;
	font-size: 12px;
	FONT-FAMILY: Verdana, Arial, Helvetica, sans-serif;
}
</style>
</HEAD>
<body
	<%if (mybean.advSearch.equals(null) || mybean.advSearch.equals("")) {%>
	onLoad="LoadRows();FormFocus();" <%}%> class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>
	<!-- <div onmouseover="populatefollowup();">click</div> -->
	 <div id="hero"></div> 
	 <div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>List <%= mybean.vouchertype_name  %></h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<!-- END PAGE BREADCRUMBS -->
					<div class="page-content-inner">
					<div class="container-fluid">
<!-- 					BODY START -->
					<%@include file="../Library/list-body.jsp"%>
					</div>
				</div>
				</div>
			</div>
		</div>
	</div>
	
<%@include file="../Library/admin-footer.jsp"%>
<%@include file="../Library/js.jsp" %>
<script src="../Library/smart.js" type="text/javascript"></script>
<script type="text/javascript">
	function populatefollowup(customer_id, voucher_id) {
		//alert(customer_id+"-----"+voucher_id);	    
		if (document.getElementById("followup_" + customer_id).innerHTML == "") {
			showHint('../accounting/customer-followup.jsp?customerfollowup=yes&customer_id='
							+ customer_id + "&voucher_id=" + voucher_id, 'followup_' + customer_id);
		}
	}

	// for dispaying voucher status
	function populatevoucherstatus(vouchertype_id, voucher_id) {
		//alert(vouchertype_id+"  "+voucher_id);
		if(vouchertype_id==108 ||vouchertype_id==10||vouchertype_id==1){
			if (document.getElementById('followup_' + voucher_id).innerHTML == "") {       
				showHint('../accounting/customer-followup.jsp?purchasevouchertstaus=yes&voucher_id='
							+ voucher_id + "&vouchertype_id=" + vouchertype_id,
							'followup_' + voucher_id);                       
				}
		}
		if(vouchertype_id==4 ||vouchertype_id==3){
			if (document.getElementById('followup_' + voucher_id).innerHTML == "") {       
				showHint('../accounting/customer-followup.jsp?salesvouchertstaus=yes&voucher_id='
							+ voucher_id + "&vouchertype_id=" + vouchertype_id,
							'followup_' + voucher_id);                       
				}
		}
		
	}
</script>
<!-- <script type="text/javascript" src="../ckeditor/ckeditor.js"></script> -->
		
</body>
</HTML>
