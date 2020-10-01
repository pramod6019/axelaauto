<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.axelaauto_app.App_Veh_Quote_List"
	scope="request" />
<%
	mybean.doPost(request, response);
%>

<!DOCTYPE html>

<html lang="en">
<head>
<meta content="width=device-width, initial-scale=1" name="viewport">
<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="css/components-rounded.css" id="style_components" rel="stylesheet" type="text/css">
<link href="css/default.css" rel="stylesheet" type="text/css" id="style_color">
<script src="js/jquery.min.js" type="text/javascript"></script>
<script src="js/bootstrap.min.js" type="text/javascript"></script>
<script src="js/axelamobilecall.js" type="text/javascript"></script>


<script>
	var emp_id = <%=mybean.emp_id%> ;
	var comp_id = <%=mybean.comp_id%> ;
	var total = <%=mybean.totalcount%> ;
	var count = <%=mybean.pagecurrent%> ;
	var status='<%=mybean.quotestatus%>';
	var temp_count = 0;
	$(document).ready(
			function($) {
				$(window).scroll(
						function() {
							if ($(window).scrollTop() == $(document).height()
									- $(window).height()) {
								if (count > total) {
									return false;
								} else if (eval(temp_count) != eval(count)) {
									//alert("2222");
									count++;
									temp_count = count;
									loadArticle(count);

								}
								//count++;
								//alert("count after method---------"+count);
							}
						})
			});

	function loadArticle(pagenumber) {
		//$('#loader').html('<div id=loading align=center><img align=center src=\"../admin-ifx/loading.gif\" /></div>');
		showloader();
		if (status == 'filter') {
				var printurl=$(location).attr('href');
				$.ajax({
					 url :printurl+"&pagenumber="+pagenumber,
// 				url : "app-veh-salesorder-list.jsp?pagenumber=" + pagenumber
// 						+ "&enquirystatus=monthbooking&emp_id=" + emp_id + "&comp_id="+comp_id+"",
				type : 'post',
				data : '',
				success : function(html) {
					hideloader();
					$("#con").append(html);
				}
			})
		}
		else{
		$.ajax({
			url : "app-veh-quote-list.jsp?pagenumber=" + pagenumber + "&emp_id="
					+ emp_id + "&comp_id="+comp_id+"",
			type : 'post',
			data : '',
			success : function(html) {
				hideloader();
				$("#con").append(html);
			}
		})
		
	}
		return false;
	}

	function mail(quote) {
		var emp_id =
<%=mybean.emp_id%>
	;
		$.ajax({
			url : "app-email-quote.jsp?quote_id=" + quote + "&emp_id=" + emp_id,
			type : 'post',
			data : '',
			success : function(msg) {
				showToast(msg);
			}

		})
	}
	
	function showloader() {
		$('#loader').css('display', 'block');
		$('#loader').show();
		;

	}

	function hideloader() {
		$('#loader').css('display', 'none');
		$('#loader').hide();

	}
</script>
<style>
b {
	color: #8E44AD;
}

.container {
	padding-right: 15px;
	padding-left: 0px;
	margin-right: auto;
	margin-left: auto;
}

.con {
	margin-top: 40px;
}

.panel-heading {
	margin-bottom: 20px;
	background-color: #8E44AD;
	border: 1px solid transparent;
	border-radius: 0px;
	box-shadow: 0px 1px 1px rgba(0, 0, 0, 0.05);
}

strong {
	color: #fff;
}

.header-wrap {
	position: fixed;
	width: 100%;
	top: 0;
	z-index: 1;
}

.loader {
	display: none;
	position: fixed;
	left: 0px;
	top: 0px;
	width: 100%;
  	height: 100%;  
 	bottom: 3px; 
	background: url('../admin-ifx/loading.gif') 50% 100% no-repeat;
}
.btn{
	background-color: #fff;
}
</style>


</head>

<body <%if (!mybean.msg.equals("")) {%>
	onload="showToast('<%=mybean.msg%>')" <%}%>>
	<%
		if (mybean.pagecurrent == 1) {
	%>
	<div class="header-wrap">
		<div class="panel-heading">
			<span class="panel-title">
					<strong>List Quotes</strong>
					<a href="callurlapp-quote-list-filter.jsp" type="button" class="btn btn-sm" style="float: right"> <b>FILTER</b> </a>
<!-- 					<a href="callurlapp-quote-list-filter.jsp"><button type="button" class="btn btn-sm btn-purple" style="float: right">FILTER</button></a> -->
			</span>
		</div>
	</div>
	<%
		}
	%>
	<h4>&nbsp;</h4>
	<div id="con">
		<%=mybean.StrHTML%>
		
	</div><br>
	<div id="loader" class="loader"></div>

</body>
</html>