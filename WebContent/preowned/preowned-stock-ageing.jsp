<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.preowned.Preowned_Stock_Ageing" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<link rel="shortcut icon"  type="image/x-icon" href="../admin-ifx/axela.ico">     

<link rel="shortcut icon" type="image/x-icon"
	href="../admin-ifx/axela.ico">
	<link href='../Library/jquery.qtip.css' rel='stylesheet'
		type='text/css' />

	<link href="../assets/css/font-awesome.css" rel="stylesheet"
		type="text/css" />
	<link href="../assets/css/bootstrap.css" rel="stylesheet"
		type="text/css" />
		 
	<link href="../assets/css/components-rounded.css" rel="stylesheet"
		id="style_components" type="text/css" />
	<link href="../assets/css/font-awesome.css" rel="stylesheet"
		type="text/css" />
		
		
	<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />	 
	<LINK REL="STYLESHEET" TYPE="text/css"
	HREF="../assets/css/footable.core.css">
<script type="text/javascript" src="../Library/Validate.js"></script>
 <script type="text/javascript" src="../Library/jquery.js"></script>
     <script type="text/javascript" src="../Library/jquery-ui.js"></script>
<script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
<script type="text/javascript" src="../Library/smart.js"></script>
<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript"
		src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
	
<script type="text/javascript" src="../ckeditor/ckeditor.js"></script>	
<script src="../assets/js/footable.js" type="text/javascript"></script>
<script type="text/javascript">
		$(function() {
			$('table')
					.footable(
							{
								toggleHTMLElement : '<span><div class="footable-toggle footable-expand" border="0"></div>'
										+ '<div class="footable-toggle footable-contract" border="0"></div></span>'
							});
		});
	</script>



<script type="text/javascript" >
function populatefollowup(preowned_id){
	if(document.getElementById("followup_"+preowned_id).innerHTML==""){
		showHint('../preowned/preowned-followup.jsp?preowned_id='+preowned_id,  'followup_'+preowned_id+'');
		}
	}
</script>
    <link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed">
  <%@include file="../portal/header.jsp" %>
<!--   <table width="98%" border="0" align="center" cellpadding="0" cellspacing="0"> -->
<!--     <tr> -->
<%--       <td><%=mybean.LinkHeader%>&nbsp;<br><br></td> --%>
<!--     </tr> -->
<!--     <tr> -->
<%--       <td align="center"><font color="#ff0000"><b><%=mybean.msg%></b></font><br></td> --%>
<!--     </tr> -->
<!--     <tr> -->
<%--       <td valign="top" height="200" align="center"><%=mybean.StrHTML%></td> --%>
<!--     </tr> -->
<!--   </table> -->
  
  
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
						<h1>Stock Ageing Status</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href=../portal/home.jsp>Home</a>&gt;</li>
						<li><a href=../preowned/index.jsp>Pre Owned</a> &gt;</li>
						<li><a href=../preowned/preowned-stock.jsp>Stock</a>&gt;</li>
						<li><a href=preowned-stock-list.jsp?preownedstock_id=" + preownedstock_id + ">List Stocks</a>&gt;</li>
						<li><a href=preowned-stock-ageing.jsp?preownedstock_id=" + preownedstock_id + ">Stock Ageing Status</a>:</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					
					<div class="page-content-inner">
					<div class="tab-pane" id="">
<!-- 					BODY START -->
				<center><font color="#ff0000"><b><%=mybean.msg%></b></font><br></center>	
					<center><%=mybean.StrHTML%></center>	 

					</div>
				</div>
				</div>
			</div>
		</div>

	</div>
	<!-- END CONTAINER -->
  
  
  
  
 <%@ include file="../Library/admin-footer.jsp" %></body>
</HTML>
