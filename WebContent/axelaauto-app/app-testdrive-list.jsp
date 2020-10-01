<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.axelaauto_app.App_TestDrive_List"
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
<!-- <LINK REL="STYLESHEET" TYPE="text/css" -->
<%-- 	HREF="../Library/theme<%=mybean.GetTheme(request)%>/jquery-ui.css"> --%>

<style>
a {
	color: #fff;
}

.container {
    padding-right: 10px;
    padding-left: 0px;
    margin-right: auto;
    margin-left: auto;
/*      margin-left: 50px; */
}
.panel-heading {
	margin-bottom: 0px;
	background-color: #8E44AD;
	border: 1px solid transparent;
	border-radius: 0px;
	box-shadow: 0px 1px 1px rgba(0, 0, 0, 0.05);
}

.heading {
	position: fixed;
	width: 100%;
	top: 40;
	z-index: 1;
	background-color: #fff;
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

span {
	color: red;
}

b {
	color: #8E44AD;
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

.con {
	margin-top: 100px;
}
</style>

<script>
	var emp_id = <%=mybean.emp_id%>;
	var comp_id = <%=mybean.comp_id%>;
	var total = <%=mybean.totalcount%>;
	var count = <%=mybean.pagecurrent%>;
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
									count++;
									temp_count = count;
									loadArticle(count);

								}
								//count++;
								/* alert("count after method--------"+count); */
							}
						})
			});

	function loadArticle(pagenumber) {
		//$('#loader').html('<div id=loading align=center><img align=center src=\"../admin-ifx/loading.gif\" /></div>');
		showloader();
		$.ajax({
			url : "app-testdrive-list.jsp?pagenumber=" + pagenumber
					+ "&emp_id=" + emp_id + "&comp_id=" + comp_id + "",
			type : 'post',
			data : '',
			success : function(html) {
				hideloader();
				$("#con").append(html);
			}
		})
		return false;
	}

	function showloader() {
		$('#loader').css('display', 'block');
		$('#loader').show();

	}

	function hideloader() {
		$('#loader').css('display', 'none');
		$('#loader').hide();

	}
</script>
</head>

<body <%if (!mybean.msg.equals("")) {%>
	onload="showToast('<%=mybean.msg%>')" <%}%>>
	
	<div class="header-wrap">

		<%
			if (mybean.pagecurrent == 1) {
		%>

		<div class="heading">
			<div class="container" style="margin-top: 5px">
				<form role="form" class="form-horizontal" name="frmquote"
					id="frmquote" method="post">
					<div class="form-body">
						<!-- 	<div class="form-group"> -->
						<div class="col-md-12 col-xs-12">

							<div class="form-group form-md-line-input">
								<label for="form_control_1">Date<span>*</span>:
								</label> <input type="" class="form-control" id="txt_testdrive_date"
									name="txt_testdrive_date" value="<%=mybean.startdate1%>"
									onfocusout="this.form.submit();" onclick="datePicker('txt_testdrive_date');" readonly />
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
		<%
			}
		%>
	</div>
	<div class="con" id="con" name="con">
	<%=mybean.StrHTML%> 

<!-- 		<div class="container"> -->
<!-- 			<div class="col-md-12"> -->
<!-- 				<div class="row" style="background-color: #fff"> -->
<!-- 					<br> -->

<!-- 					<div class="col-md-8 col-xs-8"> -->
<!-- 					hnvghnjvmjbvmnbvmb  -->
					
<!-- 					</div> -->
<!-- 					<div class="col-md-4 col-xs-4"> -->
<!-- 						<div class="row"> -->
<!-- 							<img src="ifx/icon-call.png" class="img-responsive" -->
<!-- 								data-toggle="modal" data-target="myModal1" style="float: right"> -->

<!-- 							<div class="modal fade" id="myModal1" role="dialog"> -->
<!-- 								<div class="modal-header"> -->
<!-- 									<button type="button" class="close" data-dismiss="modal">&times;</button> -->
<!-- 									<center> -->
<!-- 										<h4 class=modal-title> -->
<!-- 											<b style="color: white">Contact</b> -->
<!-- 										</h4> -->
<!-- 									</center> -->

<!-- 								</div> -->
<!-- 								<div class="modal-body"> -->
<!-- 								chchchc bh cvbcvbcv -->
								
<!-- 								f</div> -->


<!-- 							</div> -->




<!-- 							<img src="ifx/icon-call.png" class="img-responsive" data-toggle="modal" data-target="#myModal1" -->
<!-- 										style="float:right"> -->

<!-- 							<div class="modal fade" id="myModal1"  role="dialog"> -->
<!-- 								<div class="modal-dialog"> -->

<!-- 									<div class="modal-content"> -->

<!-- 										<div class="modal-header"> -->
<!-- 											<button type="button" class="close" data-dismiss="modal">&times;</button> -->
<!-- 											<center> -->
<!-- 												<h4 class="modal-title"> -->
<!-- 													<b style=color:white">Contact</b> -->
<!-- 												</h4> -->
<!-- 											</center> -->

<!-- 										</div> -->

<!-- 										<div class="modal-body" id="myModal1"> -->

<!-- 											<p> -->
<!-- 												<b style="color:black"> </b> -->
<!-- 											</p> -->
<!-- 										</div> -->
										
										
										
<!-- 									</div> -->
<!-- 								</div> -->
<!-- 							</div> -->

<!-- 						</div> -->

<!-- 					</div> -->
<!-- 				</div> -->

<!-- 			</div> -->
<!-- 		</div> -->






	</div>
	<br>
	<div id="loader" class="loader"></div>
<script src="js/jquery.min.js" type="text/javascript"></script>
<script src="js/bootstrap.min.js" type="text/javascript"></script>
<script src="js/axelamobilecall.js" type="text/javascript"></script>
</body>

<!-- END BODY -->
</html>