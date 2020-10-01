<%@ page errorPage="../portal/error-page.jsp"%>
<%-- <jsp:useBean id="mybean" class="axela.axelaauto_app.App_Share_Link" --%>
<%-- 	scope="request" /> --%>
<%-- <% --%>
// 	mybean.doPost(request, response);
<%-- %> --%>
<!DOCTYPE html>

<html lang="en">
<head>

<meta content="width=device-width, initial-scale=1" name="viewport">

<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="css/components-rounded.css" id="style_components"
	rel="stylesheet" type="text/css">

<style>

.container {
	padding-right: 0px;
	padding-left: 0px;
	margin-right: auto;
	margin-left: auto;
	margin-top: 45px;
}
.panel-heading {
	margin-bottom: 20px;
	background-color: #8E44AD;
	border: 1px solid transparent;
	border-radius: 0px;
	box-shadow: 0px 1px 1px rgba(0, 0, 0, 0.05);
}

.header-wrap {
	position: fixed;
	width: 100%;
	top: 0;
	z-index: 1;
}
strong{
	color: #fff;
}
b{
color: #8E44AD;
}
</style>
</head>

<script>
function cal1()
{
	$.ajax({
		url : "app-veh-quote-list.jsp?dd1=no",
		type : 'post',
		data : '',
		success : function(html) {
			hideloader();
			$("#con").append(html);
		}
	})
	}
	function cal2()
	{
		$.ajax({
			url : "app-veh-quote-list.jsp?dd1=yes",
			type : 'post',
			data : '',
			success : function(html) {
				hideloader();
				$("#con").append(html);
			}
		})
		
	}
	</script>

<body>
<div class="header-wrap">
		<div class="panel-heading">
			<span class="panel-title"> <center><strong>Silverarrows</strong></center> 
			</span>
		</div>
	</div>
	<div class="container">
		<input type="text" name="uname" id="uname"  onclick="cal1();" onchange="cal2();">
 <!-- 		<center>			 -->
<!-- 			<table class="table"> -->
<!-- 				<tr> -->
<!-- 					<td><label for="form_control_1" style="float: right">Model:</label></td>  -->
<%-- 					<td><label for="form_control_1"><b><%=mybean.model_name%></b></label></td> --%>
<!-- 				</tr> -->
<!-- 				<tr> -->
<!-- 					<td><label for="form_control_1" style="float: right">Type:</label></td> -->
<%-- 					<td><label for="form_control_1"><b><%=mybean.type%></b></label></td> --%>
<!-- 				</tr> -->
<!-- 				<tr> -->
<!-- 					<td><label for="form_control_1" style="float: right">Colour:</label></td> -->
<%-- 					<td><label for="form_control_1"><b><%=mybean.imgdatacat_name %></b></label></td> --%>
<!-- 				</tr> -->
<!-- 			</table> -->
<!-- 		</center> -->


<%-- 		<%=mybean.StrHTML%> --%>

	</div>


<script src="js/jquery.min.js" type="text/javascript"></script>
<script src="js/bootstrap.min.js" type="text/javascript"></script>
</body>
<!-- END BODY- -->
</html>