<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Update_Kms" scope="request" />
<% mybean.doPost(request, response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<style type="">
@media ( min-width : 992px) {
	.control-label {
		text-align: right;
	}
}

.modelstyle {
	height: 250px;
}
</style>
</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed">
	<div id="reload" class="modelstyle">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">&times;</button>
			<h4 class="modal-title">
				<center> <b>Update Kms</b> </center>
			</h4>
		</div>

		<div class="modal-body">
			<div id="timefield">
			
			<center>
				<b><div class="colo-md-4" id="msg"></div></b>
			</center>
				<%if(mybean.ReturnPerm(mybean.comp_id, "emp_testdrive_edit", request).equals("1")){ %>
				<form name="form1" id="form1" method="post" class="form-horizontal">
				<center>
					<div class="form-element6 form-element-center">
							<label> In Kms<font color=red>*</font>: &nbsp;</label>
								<input name="txt_salesgatepass_in_kms" type="text"
									class="form-control" id="txt_salesgatepass_in_kms"
									value="<%=mybean.salesgatepass_in_kms%>" size="18"
									maxlength="10"
									onKeyUp="toFloat('txt_salesgatepass_in_kms','In Kms')">

					</div>
				</center>
				</form>

				<center>
					<input name="add_button" class="btn btn-success" id="add_button"
						type='button' value="Update Kms" style="margin-top: 0px;"
						onclick="return UpdateKms();" />
					<input type="hidden" name="add_button" id="add_button" value="yes" />
					<input type="hidden" name="salesgatepass_testdriveveh_id" id="salesgatepass_testdriveveh_id"
					 value="<%=mybean.salesgatepass_testdriveveh_id%>" />
					 <input type="hidden" name="salesgatepass_id" id="salesgatepass_id"
					 value="<%=mybean.salesgatepass_id%>" />
				</center>
			</form>
				<%}else { %>
					<div class="form-group">
						<center><font color=red>Access Denied!</font></center>
					</div>
				<% }%>
			</div>
		</div>
	</div>
</body>
<!-- this is to to provide UI property -->
<script>FormElements();
	function closeModal(){
		var msg =  $("#msg").text();
		if(msg == 'In Kms updated'){
			$("#Hintclicktocall").modal('toggle');
		}
	}
</script>
</html>