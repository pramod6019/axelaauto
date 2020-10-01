<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Send_Executive_SMS"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<HEAD>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<style>
@media ( min-width : 200px) {
	.control-label {
		text-align: right;
	}
}

.modelstyle {
	height: 200px;
}

.modal-dialog{
	overflow-y: initial;
}

.modal-body{
	height: 200px;
	overflow-y: auto;
}


</style>
</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">&times;</button>
			<h4 class="modal-title">
				<center> <b>Send SMS</b> </center>
			</h4>
		</div>

		<div class="modal-body">
			
			<center>
				<span id="sentmsg"></span>
				<font color="#ff0000"><b><%=mybean.msg%></b></font>
			</center>
			
			<div id="dialog-modal"></div>
			<div class="container-fluid">
			<div class="form-element6 form-element-center">
				<input class="form-control" onKeyUp="toPhone('txt_mobile','Mobile')" maxlength="13" size="32"
					name="customer_mobile_no" type="text" id="customer_mobile_no" value="91-"></input>
			</div><br></br>
			<input type="hidden" id="txt_emp_id" name="txt_emp_id" value=<%=mybean.exe_id %>></input>
			
			<center>
				<button class="btn btn-success" onclick="SendSms();" id="sendSms" name="sendSms" value="Send_SMS">Send SMS </button>
			</center>
			</div>
				
			</div>
			
		</div>
		<script>FormElements();</script>
</body>
</html>