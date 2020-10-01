<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Executive_Send_Sms"
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
	height: 350px;
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
			<div id="dialog-modal"></div>
			<div class="container-fluid">
			<center>
				<font color="red"><b><span id="sentmsg"></span></b></font>
			</center>
			<form name="form1" id="form1" method="post" class="form-horizontal">
				<div class="form-element9 form-element-center">
					<label>Executive<font color="#ff0000">*</font>:
					</label> <select class="form-control select2" id="allexecutives"
						name="allexecutives">
						<%=mybean.crecheck.PopulateAllExecutives(mybean.comp_id, mybean.exe_id)%>
					</select>
				</div>

				<div class="form-element9 form-element-center">
					<label> Contact Name<font color=red>*</font>:&nbsp;
					</label> <input name="contact_name" type="text" class="form-control"
						id="contact_name" value="<%=mybean.contact_name%>" size="255"
						maxlength="255" />
						<input type="hidden" id="contact_id" name="contact_id" value="<%=mybean.contact_id %>" />
				</div>
				
				

				<div class="form-element9 form-element-center">
					<label> Contact Mobile<font color=red>*</font>:&nbsp;
					</label> <input name="contact_mobile" type="text" class="form-control"
						id="contact_mobile" value="<%=mybean.contact_mobile%>" size="255"
						maxlength="13" onKeyUp="toPhone('contact_mobile','Mobile')" />
				</div>
			</form>


			<center>
				<button class="btn btn-success" onclick="SendSms();"
				id="sendSms" name="sendSms" value="Send SMS">Send SMS </button>
			</center>
			
			</div>
				
			</div>
			
		</div>
		<script src="../assets/js/select2.full.min.js" type="text/javascript"></script>
<script src="../assets/js/components-select2.min.js" type="text/javascript"></script>

			<script> FormElements();</script>
</body>
</html>