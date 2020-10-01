<%@ page errorPage="../portal/error-page.jsp"%>
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
			<center><b>Discount</b></center>
		</h4>
	</div>
	<div class="modal-body">
			<div class="container-fluid">
				<div class="form-body">
					<div class="form-group col-md-12">
<!-- 					============This is to store parrent data=========== -->
					<input id='dis_row_no' name='dis_row_no' value='<%=request.getParameter("row_no") %>' hidden/>
					<input id='item_qty' name='item_qty' value='0' hidden/>
					<input id='item_unitprice' name='item_unitprice' value='0' hidden />
<!-- 			=================end==========		 -->

<table class='table table-bordered table-hover' data-filter='#filter'>
<thead><tr>
<th><b><u>Net Amount</u></b></th>
<th><b><u>Discount</u></b></th>
<th><b><u>Total</u></b></th>
</tr></thead>
<tbody>
<td><input id='net_amount' name='net_amount' class='form-control text-right' readonly='readonly' value='0' /></td>
<td>
	<div class='col-md-10'>
		<span class='col-md-5'><label>Amount:</label></span>
		<span class='col-md-7'>
			<input id='discount_amount' name='discount_amount' class='form-control text-right' value='0' onKeyUp="toNumber('discount_amount','Discount');" />
		</span>
	</div>
	<div class='row'></div>
	<div class='col-md-10'>
		<span class='col-md-5'><label>Percentage:</label></span>
		<span class='col-md-7'>
			<input id='discount_per' name='discount_per' class='form-control text-right' value='0' onKeyUp="toNumber('discount_per','Discount Per.');" />
		</span>
	</div>
</td>
<td><input id='total' name='total' class='form-control text-right' value='0'  readonly='readonly' /></td>
</tbody>
</table>
					 </div>
				</div>
			</div>
	</div>
	<div class="modal-footer">
	</div>
	</div>
	
	
<script type="application/javascript">
	
	$('#discount_amount').on('change',function(){
		var dis_per = (eval($('#discount_amount').val()) / eval($('#net_amount').val())) * 100;
		$('#discount_per').val(dis_per.toFixed(4));
		$('#total').val((eval($('#net_amount').val()) - eval($('#discount_amount').val())).toFixed(4));
	});
	
	$('#discount_per').on('change',function(){
		
		var dis_amt = (eval($('#discount_per').val()) /100 ) * eval($('#net_amount').val());
		$('#discount_amount').val(dis_amt.toFixed(4));
		$('#total').val((eval($('#net_amount').val()) - dis_amt).toFixed(4));
		
	});
</script>
