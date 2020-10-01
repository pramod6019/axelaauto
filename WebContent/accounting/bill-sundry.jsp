<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.accounting.Bill_Sundry"
	scope="request" />
<% mybean.doPost(request, response); %>
<style type="">
@media (min-width: 992px) {
.control-label {
	text-align: right;
}
}
</style>
<div id="reload">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal">&times;</button>
		<h4 class="modal-title">
			<b>Bill&nbsp;Sundry</b>
		</h4>
	</div>
	<div class="modal-body">
<!-- 		<form name="form1"> -->
			<center>
				<font color="#ff0000"><%=mybean.msg%></font></b></font>
			</center>
			<center>
			
				<input name="txt_cart_total" type="hidden" class="form-control"
					id="txt_cart_total" value="<%=mybean.cart_total%>" /> 
					
				<input name="txt_cart_bsamt_carttotal" type="hidden" class="form-control" 
					id="txt_cart_bsamt_carttotal" value="<%=mybean.cart_bsamt_carttotal%>" />
					
			</center>
			<div class="container-fluid">
				<div class="form-body">
					<div class="form-group col-md-12">
						<label class="control-label col-md-2 col-xs-12" style="top:8px;">Bill Sundry:</label>
						<div class="col-md-4 col-xs-12">
							<select id="dr_bill_sundry" name="dr_bill_sundry"
								onchange="getBillSundryAmt();" class="form-control">
								<%=mybean.PopulateBillSundry()%>
							</select>
						</div>
						<label class="control-label col-md-2 col-xs-12" style="top:8px;">Amount:</label>
						<div class="col-md-2 col-xs-12" >
							<input name="txt_billsundry_amt" type="text" class="form-control" id="txt_billsundry_amt" value="" size="10" maxlength="10" 
							 onKeyUp="toNumber('txt_billsundry_amt','Bill Sundry');" />
						</div>
					<div class="col-md-2 col-xs-12">
						<%if(mybean.status.equals("Add")){%>
							<center>
							<input name="addbutton"  class="btn btn-success" id="addbutton" type='button' value="Add" style="margin-top: 0px;"/>
							</center>
							<input type="hidden" name="add_button" value="yes" >
							<input type='hidden' name='po' id='po' value='<%= mybean.po %>' >
							<input type='hidden' name='so' id='so' value='<%= mybean.so %>' >
							<input type='hidden' name='vouchertype_id' id='vouchertype_id' value='<%= mybean.vouchertype_id %>' >
							<input type='hidden' name='cart_session_id' id='cart_session_id' value='<%= mybean.cart_session_id %>' >
							<input type='hidden' name='voucher_id' id='voucher_id' value='<%= mybean.voucher_id %>' >
						<%} %>
					</div>
				</div>
			</div>
		</div>
<!-- 		</form> -->
		<center><div id='tax_list' name='tax_list'><%=mybean.StrHTML%></div></center>
		
	</div>
	<div class="modal-footer">
	</div>
	</div>
	
	
	<script type="application/javascript">
	
	
	
	$(document).ready(function(){
		
		
		$('#addbutton').click(function(){
			var vouchertype_id = document.getElementById('vouchertype_id').value;
			var cart_session_id = document.getElementById('cart_session_id').value;
			var po = document.getElementById('po').value;
			var so = document.getElementById('so').value;
			var dr_bill_sundry = document.getElementById('dr_bill_sundry').value
			var txt_billsundry_amt = document.getElementById('txt_billsundry_amt').value
			if(po == 'yes'){
				showHintFootable('../accounting/bill-sundry-call.jsp?add_button=yes&po=yes&call=yes&vouchertype_id='
						+vouchertype_id+'&cart_session_id='+cart_session_id+'&txt_billsundry_amt='
						+txt_billsundry_amt+'&dr_bill_sundry='+dr_bill_sundry,'tax_list');
			}else{
				showHintFootable('../accounting/bill-sundry-call.jsp?add_button=yes&so=yes&call=yes&vouchertype_id='
						+vouchertype_id+'&cart_session_id='+cart_session_id+'&txt_billsundry_amt='
						+txt_billsundry_amt+'&dr_bill_sundry='+dr_bill_sundry,'tax_list');
			}
		});
	});
	</script>
	<script type="text/javascript">
	
	function DeleteCartTax(cart_id){
		var vouchertype_id = document.getElementById('vouchertype_id').value;
		var cart_session_id = document.getElementById('cart_session_id').value;
		var po = document.getElementById('po').value;
		var so = document.getElementById('so').value;
		var dr_bill_sundry = document.getElementById('dr_bill_sundry').value
		var txt_billsundry_amt = document.getElementById('txt_billsundry_amt').value
		
		if(po == 'yes'){
			showHintFootable('../accounting/bill-sundry-call.jsp?delete_button=Delete&po=yes&call=yes&vouchertype_id='
					+vouchertype_id+'&cart_session_id='+cart_session_id+'&txt_billsundry_amt='
					+txt_billsundry_amt+'&dr_bill_sundry='+dr_bill_sundry+'&cart_id='+cart_id,'tax_list');
		}else{
			showHintFootable('../accounting/bill-sundry-call.jsp?delete_button=Delete&so=yes&call=yes&vouchertype_id='
					+vouchertype_id+'&cart_session_id='+cart_session_id+'&txt_billsundry_amt='
					+txt_billsundry_amt+'&dr_bill_sundry='+dr_bill_sundry+'&cart_id='+cart_id,'tax_list');
		}
	}
	
	function AddCartTax(vouchertype_id, voucher_id, cart_total){   
	var tax_bill_sundry = document.getElementById('dr_bill_sundry').value; 
	var tax_bill_sundryarr = tax_bill_sundry.split("-");  
	var cart_bs_tax_id = tax_bill_sundryarr[0]; 
	var cart_bs_tax_rate = tax_bill_sundryarr[1];       
	showHintFootable('../accounting/purchase-details.jsp?cart_vouchertype_id='+vouchertype_id+'&cart_voucher_id='+voucher_id+'&cart_bs_tax_id='+cart_bs_tax_id+'&cart_bs_tax_rate='+cart_bs_tax_rate+'&cart_bs_total='+cart_total+'&addcart_billsundry=yes','billsundry-details');    
	}
	
	function getBillSundryAmt() {
		var cart_bs_tax_id = 0;
		var cart_bs_tax_rate = 0.0;
		var tax_bill_sundry = document.getElementById('dr_bill_sundry').value;
		var tax_bill_sundryarr = tax_bill_sundry.split("-");
		cart_bs_tax_id = tax_bill_sundryarr[0];
		cart_bs_tax_rate = tax_bill_sundryarr[1];
		var cart_total = parseFloat( document.getElementById('txt_cart_total').value).toFixed(2);
		var cart_bsamt_carttotal = parseFloat( document.getElementById('txt_cart_bsamt_carttotal').value).toFixed(2);
			if (cart_bsamt_carttotal != 0 && !isNaN(cart_bsamt_carttotal)) {
				var bs_amt = (eval(cart_bsamt_carttotal) * (eval(cart_bs_tax_rate) / 100)).toFixed(2);
				document.getElementById('txt_billsundry_amt').value = eval(bs_amt);
			} else {
				var bs_amt = (eval(cart_total) * (eval(cart_bs_tax_rate) / 100)).toFixed(2);
				document.getElementById('txt_billsundry_amt').value = eval(bs_amt);
			}
	}
</script>
