<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Coupon_Issue" scope="request" />
<% mybean.doPost(request, response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<style type="">
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
				<center>
					<b>Coupon Issue</b>
				</center>
			</h4>
		</div>

		<div class="modal-body">
			<div id="timefield">

				<center>
					<font color='red'><b><div class="form-element12" id="issuemsg"></div></b></font>
				</center>
				<%
					if (mybean.ReturnPerm(mybean.comp_id, "emp_service_coupon_issue", request).equals("1")) {
				%>
				<form name="form1" id="form1" method="post" class="form-horizontal">
					<div class="row"></div>
					<div class="form-element6">
						<label>Brand<font color=red>*</font>: </label>
						<div>
							<%=mybean.PopulatePrincipal(mybean.comp_id, request)%>
						</div>
					</div>

					<div class="form-element6">
						<label>Department<font color=red>*</font></label>
						<div>
							<%=mybean.PopulateDepartment(mybean.comp_id, request)%>
						</div>
					</div>

					<div class="form-element6">
						<label>Coupon Campaign Type<font color=red>*</font></label>
						<div>
							<%=mybean.PopulateCouponCampaignType(mybean.comp_id, request)%>
						</div>
					</div>

					<div class="form-element6">
						<label>Coupon Campaign<font color=red>*</font></label>
						<div id="hintcouponcampaign">
							<select name="dr_couponcampaign_id" id="dr_couponcampaign_id" class="form-control">
							</select>
						</div>
					</div>

					<span name="hintcoupondetail" id="hintcoupondetail">
						<input type="hidden" name="coupon_id" id="coupon_id" value="0" />
					</span>

				</form>
				<div class="form-element12">
					<center>
						<input name="add_button" class="btn btn-success" id="add_button"
							type='button' value="Issue Coupon" onclick='return CouponIssue();' />
						<input type="hidden" name="add_button" id="add_button" value="yes" />
						<input type="hidden" name="customer_id" id="customer_id"
							value="<%=mybean.customer_id%>" /> 
					</center>
				</div>
				</form>
				<%
					} else {
				%>
				<div class="form-group">
					<center>
						<font color=red>Access Denied!</font>
					</center>
				</div>
				<%
					}
				%>
			</div>
		</div>
	</div>
</body>
<!-- this is to to provide UI property -->
<script>FormElements();</script>
</html>