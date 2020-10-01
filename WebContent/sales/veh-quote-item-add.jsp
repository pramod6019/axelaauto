<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Veh_Quote_Item_Add"
	scope="request" />
<% mybean.doPost(request, response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<style type="text/css">
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
	<div class="modal-header">
		<span style="font-size: 22px; float: right; cursor: pointer;" data-dismiss="modal">&times;</span>
		<h4 class="modal-title">
			<center> <b>Item Add</b> </center>
		</h4>
	</div>

	<div class="modal-body">
		<div class="page-content-inner container-fluid">
			<!-- END PAGE BREADCRUMBS -->

			<div class="tab-pane" id="">
				<!-- 					BODY START -->
				<form name="frmitemadd" id="frmitemadd" class="form-horizontal" method="post">
				
					<center> <font color="#ff0000"><b><div id="errorMsg"></div></b></font> </center>

					<!-- 	START PORTLET BODY-->
						<center>
							<font size="1">Form fields marked with a red asterisk <b>
							<font color="#ff0000">*</font></b> are required. </font>
						</center>

						<div class="form-element6">
							<label>Group<font color="#ff0000">*</font>: </label>
							<select id="dr_option_group_id" name="dr_option_group_id" class="form-control" >
								<%=mybean.PopulateGroups()%>
							</select>
						</div>

						<div class="form-element6">
							<label>Item Name:&nbsp;</label>
							<input type="text" class="form-control" id="txt_option_name" name="txt_option_name" onkeyup="Itemdetails(this.value);"/>
						</div>
						
						<div class="form-element12">
							<div id='Itemdetails'></div>
						</div>

					<!-- 	PORTLET end details-->
				</form>
			</div>
		</div>
	</div>

	<!-- this is to to provide UI property -->
	<script>FormElements();</script>
	
	<script type="text/javascript">
		function Itemdetails(name){
			var group_id = document.getElementById("dr_option_group_id").value;
			var url = "../sales/veh-quote-item-add-check.jsp?itemdetails=yes";
			var param = "&group_id=" + group_id + "&item_name=" + name + "&itemmaster_id=" + <%=mybean.itemmaster_id %>;
			showHint(url + param, 'Itemdetails');
		}
		
		function AddItem(item_id, item_code, aftertax, item_name, option_id, group_name){
			var group_id = document.getElementById("dr_option_group_id").value;
			var item_qty = document.getElementById("item_qty_"+item_id).value;
			
			var configitems = $("#config_details_json").html();
			console.log("item_name=="+item_name);
			var url = '../sales/veh-quote-item-add-check.jsp?itemadd=yes'
					+'&item_id='+item_id
					+'&aftertax='+aftertax
					+'&item_code='+item_code
					+'&item_name='+item_name
					+'&option_id='+option_id
					+'&group_id='+group_id
					+'&group_name='+group_name
					+'&item_qty='+item_qty
// 			console.log(url);
			$.ajax({
	 			type: "POST",
				url: url,
				data: JSON.stringify(configitems),
				contentType: "application/json",
	 			success: function (data){
	 				if(data.trim() != 'SignIn'){
	 					if(data.trim().includes("Error!")){
	 						$("#errorMsg").html(data.trim());
	 					}else {
	 						getItemDetails(data.trim());
	 					}
	 				
	 				} else{
	 					window.location.href = "../portal/";
	 				}
	 			}
	 		})
		}
	</script>
</body>
</html>