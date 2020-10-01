<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.inventory.Stock_Options"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<%@include file="../Library/css.jsp"%>
<script type="text/javascript">
function ItemSearch()
{  
    //document.getElementById("hint_item_qty").innerHTML = "";
    var value = document.getElementById("txt_search").value;
    var url = "stock-item-check.jsp?";
    var param="q="+ value+"&vehstock_id=<%=mybean.trans_vehstock_id%>";
		showHint(url + param, 'hint_search_item');
	}
	function AddOptionItem() {
		document.form1.submit();
	}
</script>

</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed" onload="">
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
						<h1>Stock Option</h1>
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
						<li><a href="../inventory/index.jsp">Inventory</a> &gt;</li>
						<li><a href="../inventory/stock.jsp">Stock</a> &gt;</li>
						<li><a href="stock-list.jsp?all=yes">List Stock</a> &gt;</li>
						<li><a href="stock-list.jsp?vehstock_id=<%=mybean.trans_vehstock_id%>">Stock
								ID: <%=mybean.trans_vehstock_id%></a> &gt;</li>
						<li><a href="stock-options.jsp?vehstock_id=<%=mybean.trans_vehstock_id%>">Stock
								Option</a>:</li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="container-fluid">
							<!-- 					BODY START -->
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<form name="form1" id="form1" method="post" class="form-horizontal"
								action="item-options.jsp?add=yes&item_id=<%=mybean.trans_vehstock_id%>">
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">
											Options for <%=mybean.itemmaster_name%>
											<input type="hidden" id="txt_itemmaster_id"
												name="txt_itemmaster_id" value="<%=mybean.trans_vehstock_id%>"
										</div>
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<div class="form-element6 form-element-center">
											<label>Search</label>
												<input name="txt_search" type="text" class="form-control"
													id="txt_search" value="" size="30" maxlength="255"
													onKeyUp="ItemSearch();" />
												<div class="hint" id="hint_search_item">Enter your
													search parameter!</div>
										</div>
										<center><%=mybean.StrHTML%></center>
									</div>
								</div>
						    </div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
<%@include file="../Library/admin-footer.jsp"%>
	</div>
	<!-- END CONTAINER -->
	<%@include file="../Library/js.jsp"%>
	<script type="text/javascript" src="../Library/quote.js"></script>

</body>

</HTML>
