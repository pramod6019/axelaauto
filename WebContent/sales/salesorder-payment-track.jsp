<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Salesorder_Payment_Track"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta name="viewport" content="width=device-width, initial-scale=1">
	<%@include file="../Library/css.jsp"%>
	<link href="../assets/css/multi-select.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
	$(function() {
		$("#txt_payment_from").datepicker({
			showButtonPanel : true,
			dateFormat : "dd/mm/yy"
		});
	});
	function payment_chk_calculate() {
		document.getElementById("txt_calculate").value = "Calculate";
	}
	function track_results() {
		var result = 0;
		var track_so_amt = document.getElementById('txt_so_amt').value;

		for (var i = 1; i <= document.getElementById('txt_days').value; i++) {
			var track_payment_id = 'txt_payment_amt' + i;

			var newresult = document.getElementById(track_payment_id).value;
			if (newresult == "") {
				newresult = 0;
			}
			result = parseInt(result) + parseInt(newresult);

		}

		document.getElementById('track_total').innerHTML = result;
		document.getElementById('track_balance').innerHTML = parseInt(track_so_amt)
				- parseInt(result);
	}

	function AddFinanceStatus() {
		document.getElementById("txt_add_status").value = "Add";
	}
</script>

<link href="../assets/css/style.css" rel="stylesheet" type="text/css" />
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
						<h1><%=mybean.status%>
							Payment Track
						</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../sales/index.jsp">Sales</a> &gt;</li>
						<li><a href="veh-salesorder-list.jsp?all=recent">List
								Sales Order</a>&gt;</li>
						<li><a
							href="salesorder-payment-track.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Payment
								Track</a>:</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="#ff0000"><b><%=mybean.msg%></b></font>
					</center>
					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<center><%=mybean.status%>
											Payment Track
										</center>
									</div>
								</div>

								<div class="portlet-body portlet-empty">
									<div class="container-fluid">
										<form name="form1" method="post" class="form-horizontal">
											<center>
												<font size="">Form fields marked with a red asterisk
													<font color="red">*</font> are required.
												</font>
											</center>
											</br>
											<div class="form-element6">
												<input type="hidden" id="txt_so_id" name="txt_so_id"
													value="<%=mybean.so_id%>" /> <input type="hidden"
													id="txt_so_amt" name="txt_so_amt"
													value="<%=mybean.so_amt%>" /> 
													<label>Sales Order ID:</label>
													<a href="../sales/veh-salesorder-list.jsp?so_id=<%=mybean.so_id%>"><b><%=mybean.so_id%></b></a>
											</div>

											<div class="form-element6">
												<label>Sales Order Date:</label>
													<b><%=mybean.sodate%></b> <input name="txt_so_date"
														type="hidden" class="textbox" id="txt_so_date"
														value="<%=mybean.sodate%>" />
											</div>
											<div class="form-element6" id="contact_link">
												<label>Customer:</label>
													<b><span id="span_quote_customer_id"
														name="span_quote_customer_id"><%=mybean.customer_name%></span></b>&nbsp;
													<input name="acct_id" type="hidden" id="acct_id"
														value="<%=mybean.customer_id%>" />
											</div>

											<div class="form-element6" id="contact_link">
												<label>Contact:</label>
													<b><span id="span_quote_contact_id"
														name="span_quote_contact_id"><%=mybean.contact_name%></span></b>&nbsp;
													<input name="span_cont_id" type="hidden" id="span_cont_id"
														value="<%=mybean.contact_id%>" /> <input name="cont_id"
														type="hidden" id="cont_id" value="<%=mybean.contact_id%>" />
													<div id="dialog-modal"></div>
											</div>

											<div class="form-element6" id="contact_link">
												<label>Sales Order
													Amount:</label>
													<b><%=mybean.IndFormat(Integer.toString(mybean.so_amt))%></b>
													<input type="hidden" value="<%=mybean.so_amt%>"
														id="txt_so_amt" name="txt_so_amt" />
											</div>
<div class="row"></div>
											<div class="form-element5">
												<label>Calculate
													Payment from:</label>
													<input name="txt_payment_from" type="text"
														class="form-control" id="txt_payment_from"
														value="<%=mybean.so_date%>" size="11" maxlength="10" />
												</div>
												<div class="form-element1">
												<label>for</label>
													<select name="dr_days" id="dr_days" class="form-control">
														<%=mybean.PopulateDays()%>
													</select>
												</div>
												<div class="form-element1">
												<label>Parts:</label>
													<input input type="submit" class="btn btn-success"
														name="calculate_button" id="calculate_button"
														value="Calculate"
														onClick="payment_chk_calculate();return SubmitFormOnce(document.form1, this);" />
													<input id="txt_days" type="hidden"
														value="<%=mybean.no_days%>" /> <input type="hidden"
														name="txt_calculate" id="txt_calculate" />
												</div>

<div class="row"></div>
											<div class="form-element12">
												<span id="track_div"> <%=mybean.GetPaymentTrackDetails(mybean.so_id, Integer.toString(mybean.no_days), request)%></span>
											</div>
											<%
												if (mybean.status.equals("Add")) {
											%>
											<center>
												<input name="addbutton" id="addbutton" type="button"
													onClick="return SubmitFormOnce(document.form1, this);"
													class="btn btn-success" value="Add Payment Track" /> <input
													type="hidden" name="add_button" value="yes" />
											</center>
											<%
												}
											%>

											<br>
											<%=mybean.ListReceiptData()%>
											<br>


											<div class="portlet box">
												<div class="portlet-title" style="text-align: center">
													<div class="caption" style="float: none">Finance
														Status</div>
												</div>
												<div class="portlet-body portlet-empty">
													<div class="tab-pane" id="">
														<!-- START PORTLET BODY -->
														<div class="form-element6 form-element-center">
															<label>Finance Status:</label>
																<select name="dr_so_finstatus_id"
																	id="dr_so_finstatus_id" class="form-control">
																	<%=mybean.PopulateFinanceStatus()%>
																</select>
														</div>
														<div class="form-element6 form-element-center">
															<label>Description<font color="#ff0000">*</font>: </label>
																<textarea id="txt_finnancetrans_desc"
																	name="txt_finnancetrans_desc" cols="70" rows="4"
																	class="form-control"><%=mybean.finnancetrans_desc%></textarea>
														</div>
														<%
															if (mybean.status.equals("Add")) {
														%>
														<center>
															<input name="addbutton" id="addbutton" type="button"
																onClick="AddFinanceStatus();return SubmitFormOnce(document.form1, this);"
																class="btn btn-success" value="Finance Status" /> <input
																type="hidden" name="txt_add_status" id="txt_add_status" />
														</center>
														<%}%>
														<%if(!mybean.so_finstatus_date.equals("")){%>
														<center>
															<%=mybean.PopulateFinanceTrans()%>
														</center>
														<%}%>
													</div>
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
	<!-- END CONTAINER -->

	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
	<script type="text/javascript" src="../ckeditor/ckeditor.js"></script>
	<script type="text/javascript" src="../Library/veh-quote.js"></script>
</body>
</HTML>





