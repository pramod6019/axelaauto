<%@ page errorPage="error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.accounting.Reconcile"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
	<%@include file="../Library/css.jsp"%>
</HEAD>
<body onLoad="FormFocus();"
	class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>Reconciliation</h1>
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
						<li><a href="index.jsp">Accounting</a> &gt;</li>
						<li><a href="reconcile.jsp">Reconciliation</a>:</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<center>
								<font color="#ff0000"><b><span id="error"> <%-- 									<%=mybean.msg%> --%>
									</span></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Reconciliation</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="container-fluid">
										<!-- START PORTLET BODY -->
										<form name="form1" class="form-horizontal" method="post">
											<div class="form-element6">
												<label>Ledger<font color="#FF0000">*</font>:
												</label> <select name="dr_customer" id="dr_customer"
													value="<%=mybean.customer_id%>" class="form-control"
													onchange="ReconcileList(1);ReconcileData(0,'');">
													<%=mybean.PopulateLedger()%>
												</select>
											</div>
											<div class="form-element3">
												<label>From Date<font color="#FF0000">*</font>:
												</label> <input name="txt_fromdate" id="txt_fromdate"
													class="form-control datepicker"
													onchange="ReconcileList(1);ReconcileData(0,'');"
													type="text" value="<%=mybean.fromdate%>" size="12"
													maxlength="10" />
											</div>
											<div class="form-element3">
												<label>To Date<font color="#FF0000">*</font>:
												</label> <input name="txt_todate" id="txt_todate"
													class="form-control datepicker"
													onchange="ReconcileList(1);ReconcileData(0,'');"
													type="text" class="textbox" value="<%=mybean.todate%>"
													size="12" maxlength="10" />
											</div>

											<div class="form-element12">
												<div id="getreconciledata"></div>
											</div>
											<div class="form-element12">
												<div id="listreconciledata"></div>
											</div>

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
	<script>
		function FormFocus() { //v1.0

			ReconcileList(1);
			ReconcileData(0);

		}

		function ReconcileData(vouchertrans_id) {
			var customer_id = document.getElementById('dr_customer').value;
			var fromdate = document.getElementById('txt_fromdate').value;
			var todate = document.getElementById('txt_todate').value;
			var reconcile = '';
			var check = '';
			if (vouchertrans_id != 0) {
				check = document.getElementById('check' + vouchertrans_id).checked;
				if (check == true)
					reconcile = 'yes';
				else
					reconcile = 'no';
			}
			showHint(
					'../accounting/ledger-check.jsp?getreconciledata=yes&customer_id='
							+ customer_id + '&fromdate=' + fromdate
							+ '&todate=' + todate + '&vouchertrans_id='
							+ vouchertrans_id + '&reconcile=' + reconcile,
					'getreconciledata');
		}

		function ReconcileList(pagecurrent) {
			var customer_id = document.getElementById('dr_customer').value;
			var fromdate = document.getElementById('txt_fromdate').value;
			var todate = document.getElementById('txt_todate').value;
			showHint(
					'../accounting/ledger-check.jsp?listreconciledata=yes&customer_id='
							+ customer_id + '&fromdate=' + fromdate
							+ '&todate=' + todate + '&PageCurrent='
							+ pagecurrent, 'listreconciledata');
		}
	</script>
</body>
</HTML>
