<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.accounting.Voucher" scope="request"/>
<jsp:useBean id="export" class="axela.accounting.Voucher_Export" scope="request"/> 
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">  
<HEAD>       
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp"%>
</HEAD>
    
<body class="page-container-bg-solid page-header-menu-fixed">
<%@include file="../portal/header.jsp" %>
<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
					  <%if(mybean.param1.equals("yes")){%>
						<h1><%=mybean.vouchertype_name%></h1>
						<%}%>
						<%if(mybean.param2.equals("yes")){%>
						<h1><%=mybean.vouchertype_name%></h1>
						<%}%>
						<%if(mybean.param3.equals("9")){%>
						<h1><%=mybean.vouchertype_name%></h1>
						<%}%>
						<%if(mybean.param3.equals("18")){%>
						<h1>Journal</h1>
						<%}%>
						<%if(mybean.param3.equals("16")){%>
						<h1><%=mybean.vouchertype_name%></h1>
						<%}%>
						<%if(mybean.param3.equals("15")){%>
						<h1><%=mybean.vouchertype_name%></h1>
						<%}%>
						<%if(mybean.param3.equals("7")){%>
						<h1><%=mybean.vouchertype_name%></h1>
						<%}%>
						<%if(mybean.param3.equals("19")){%>
						<h1><%=mybean.vouchertype_name%></h1>
						<%}%>
						<%if(mybean.param3.equals("5")){%>
						<h1><%=mybean.vouchertype_name%></h1>
						<%}%>
						<%if(mybean.param3.equals("11")){%>
						<h1><%=mybean.vouchertype_name%></h1>
						<%}%>
						<%if(mybean.param3.equals("6")){%>
						<h1><%=mybean.vouchertype_name%></h1>
						<%}%>
						<%if(mybean.param3.equals("12")){%>
						<h1><%=mybean.vouchertype_name%></h1>
						<%}%>
						<%if(mybean.param3.equals("10")){%>
						<h1><%=mybean.vouchertype_name%></h1>
						<%}%>
						<%if(mybean.param3.equals("21")){%>
						<h1><%=mybean.vouchertype_name%></h1>
						<%}%>
						<%if(mybean.param3.equals("27")){%>
						<h1><%=mybean.vouchertype_name%></h1>
						<%}%>
						<%if(mybean.param3.equals("28")){%>
						<h1><%=mybean.vouchertype_name%></h1>
						<%}%>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
			<div class="page-content-inner">
				<div class="container-fluid">
					<!-- END PAGE BREADCRUMBS -->
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt; </li>
						<%if(mybean.accessories.equals("yes")){%>
						<li><a href="../accessories/index.jsp">Accessories</a> &gt; </li>
						<%}else{%>
						<li><a href="../accounting/index.jsp">Accounting</a> &gt; </li>
						<%}%>
						<li><a href="../accounting/voucher.jsp?param1=<%=mybean.param1%>&param2=<%=mybean.param2%>&param3=<%=mybean.param3%>"><%=mybean.vouchertype_name%></a><b>:</b></li>
						
					</ul>
					<div class="tab-pane" id="">
<!-- 					BODY START -->
					 <%@include file="../accounting/landing-branch.jsp" %>
					</div>
				</div>
				</div>
			</div>
		</div>

	</div>
  <%@include file="../Library/admin-footer.jsp" %>
  <%@include file="../Library/js.jsp"%>
  </body>
</html>
