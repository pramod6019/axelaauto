<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Salesorder_Cancel" scope="request" />
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
	height: 450px;
}
</style>
</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed">
	<div id="reload" class="modelstyle">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">&times;</button>
			<h4 class="modal-title">
				<center> <b>Sales Order Cancel</b> </center>
			</h4>
		</div>

		<div class="modal-body">
			<div id="timefield">
			
			<center>
				<div class="colo-md-4" id="cancelmsg"></div>
			</center>
				<%if(mybean.ReturnPerm(mybean.comp_id, "emp_sales_order_cancel", request).equals("1")){ %>
				<%if(mybean.so_active.equals("1")){ %>
				<form name="form1" id="form1" method="post" class="form-horizontal">
				<center>
					<div class="form-group">
						<label class="control-label col-md-4 col-xs-12">Cancel
							Date :<font color=red>*</font></label>
						<div class="col-md-6 col-xs-12" >
							<input name="txt_so_cancel_date" type="text"
								class="form-control datepicker" 
								onmouseover="$('.datepicker').bootstrapMaterialDatePicker({ format : 'DD/MM/YYYY',switchOnClick : true, time: false });"
								data-date-container='#Hintclicktocall'
								id="txt_so_cancel_date" value="" size="12" maxlength="10" />
							<div class="hint" id="hint_txt_so_cancel_date"></div>
						</div>
					</div>
					
					
					<div class="form-group">
						<label class="control-label col-md-4 col-xs-12">Cancel Reason:<font color=red>*</font></label>
						<div class="col-md-6 col-xs-12" >
						<%=mybean.PopulateCancelReason(mybean.comp_id) %>
						<div class="hint" id="hint_dr_cancel_reason"></div>
						</div>
					</div>
					
					
					<div class="form-group">
						<label class="control-label col-md-4 col-xs-12">Notes:<font color=red>*</font></label>
						<div class="col-md-6 col-xs-12" >
							<textarea name="txt_so_notes" cols="70" rows="4" class="form-control" id="txt_so_notes"><%=mybean.so_notes%></textarea>
							<div class="hint" id="hint_txt_so_notes"></div>
						</div>
						
					</div>
					
					
<!-- 					<div class="form-group"> -->
<!-- 						<label class="control-label col-md-4 col-xs-12">CIN Status:<font color=red>*</font> -->
<!-- 						</label> -->
<!-- 						<div class="col-md-6 col-xs-12" > -->
<!-- 							<select name="dr_so_cinstatus_id" class="form-control" id="dr_so_cinstatus_id"> -->
<!-- 							</select> -->
<!-- 							<div class="hint" id="hint_dr_so_cinstatus_id"></div> -->
<!-- 						</div> -->
<!-- 					</div> -->
					
				</center>
				</form>
				<% } %>

				<center>
					<input name="add_button" class="btn btn-success" id="add_button"
						type='button' value="Cancel Sales Order" style="margin-top: 0px;"
						onclick="return CancelSalesOrder();" />
					<input type="hidden" name="add_button" id="add_button" value="yes" />
					<input type="hidden" name="so_id" id="so_id" value="<%=mybean.so_id%>" />
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
<script>FormElements();</script>
</html>