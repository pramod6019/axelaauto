<jsp:useBean id="mybeanheader" class="axela.gst.Header" scope="request" />
<meta content="width=device-width, initial-scale=1" name="viewport" />
<% mybeanheader.doPost(request, response); %>


<link href="../assets/css/simple-line-icons.css" rel="stylesheet"	type="text/css" />
<link rel="shortcut icon" type="image/x-icon"	href="../admin-ifx/axela.ico">

<script>

//hide and show of contact info in list pages======
function ShowCustomerInfo(id){
	$(".customer_"+id).show();
}
function HideCustomerInfo(id){
	$(".customer_"+id).hide();
}
//=========================================

</script>


<!-- BEGIN HEADER  -->
<div class="page-header">
	<!-- BEGIN HEADER TOP --->
		<div class="container-fluid">
			<!-- BEGIN LOGO -->
			<div class="col-md-12 col-xs-12">
			<center>
				<% if (!mybeanheader.comp_logo.equals("")) { %>
				<div style="height: 75px;margin-top: 7px;">
					<%-- 					<%=mybeanheader.comp_logo%> --%>
					<img src="<%=mybeanheader.comp_logo%>" width="180" height="120%">
				<% } %>
				</div>
			<div style="margin-top: 15px;">
				<% if (mybeanheader.comp_id.equals("1009")) { %>
					<b>D.D.Industries Limited<br>Delhi GSTIN: 07AAACD0148D1ZA<br>Dehradun GSTIN: 05AAACD0148D1ZE<BR></b>
					
				<%} %>
<!-- 					<img src="../admin-ifx/logo.jpg" class="img img-responsive" width="140" height="43" alt="Axela" /> -->
					<b><%=mybeanheader.branch_address%><%=mybeanheader.branch_pin%><%=mybeanheader.city_name%></b>
				
			</div>
			</center>
			</div>
			</div>



			<a href="javascript:;" class="menu-toggler"></a>

	</div>

