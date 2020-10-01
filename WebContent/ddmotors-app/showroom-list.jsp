<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.ddmotors_app.Showroom_List"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html>

<html lang="en" class="no-js">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1" name="viewport">
<meta content="" name="description">
<meta content="" name="author">

<link href="../ddmotors-assets/css/bootstrap.min.css" rel="stylesheet"
	type="text/css">
<link href="../ddmotors-assets/css/components-rounded.css"
	id="style_components" rel="stylesheet" type="text/css">
<link href="../ddmotors-assets/css/default.css" rel="stylesheet"
	type="text/css" id="style_color">

<script src="../ddmotors-assets/js/jquery.min.js" type="text/javascript"></script>
<script src="../ddmotors-assets/js/bootstrap.min.js"
	type="text/javascript"></script>
<script src="../ddmotors-assets/js/mobilecall.js" type="text/javascript"></script>
<style>
b {
	color: #0f4c75;
	height: 50px;
}

.container {
	padding-right: 0px;
	padding-left: 0px;
	margin-right: auto;
	margin-left: auto;
}

</style>
</head>
<body>

	<div class="container">


		<center>
			<h3>
				<b>Showrooms</b>
			</h3>
		</center>
					<%=mybean.StrHTML%>
<!-- 								<div class="panel-group accordion scrollable" id="accordion2"> -->
<!-- 									<div class="panel panel-default"> -->
<!-- 										<div class="panel-heading"> -->
<!-- 											<h4 class="panel-title"> -->
<!-- 												<a class="accordion-toggle" data-toggle="collapse" -->
<!-- 													data-parent="#accordion2" href="#collapse_2_1"> -->
<!-- 													Collapsible Group Item #1 </a> -->
<!-- 											</h4> -->
<!-- 										</div> -->
<!-- 										<div id="collapse_2_1" class="panel-collapse in"> -->
<!-- 											<div class="panel-body"> -->
<!-- 												<p>Duis autem vel eum iriure dolor in hendrerit in -->
<!-- 													vulputate. Ut wisi enim ad minim veniam, quis nostrud -->
<!-- 													exerci tation ullamcorper suscipit lobortis nisl ut.</p> -->
<!-- 												<p>Anim pariatur cliche reprehenderit, enim eiusmod high -->
<!-- 													life accusamus terry richardson ad squid. 3 wolf moon -->
<!-- 													officia aute, non cupidatat skateboard dolor brunch. Food -->
<!-- 													truck quinoa nesciunt laborum eiusmod.</p> -->
<!-- 											</div> -->
<!-- 										</div> -->
<!-- 									</div> -->
<!-- 									<div class="panel panel-default"> -->
<!-- 										<div class="panel-heading"> -->
<!-- 											<h4 class="panel-title"> -->
<!-- 												<a class="accordion-toggle" data-toggle="collapse" -->
<!-- 													data-parent="#accordion2" href="#collapse_2_2"> -->
<!-- 													Collapsible Group Item #2 </a> -->
<!-- 											</h4> -->
<!-- 										</div> -->
<!-- 										<div id="collapse_2_2" class="panel-collapse collapse"> -->
<!-- 											<div class="panel-body"> -->
<!-- 												<p>Duis autem vel eum iriure dolor in hendrerit in -->
<!-- 													vulputate. Ut wisi enim ad minim veniam, quis nostrud -->
<!-- 													exerci tation ullamcorper suscipit lobortis nisl ut.</p> -->
<!-- 												<p>Anim pariatur cliche reprehenderit, enim eiusmod high -->
<!-- 													life accusamus terry richardson ad squid. 3 wolf moon -->
<!-- 													officia aute, non cupidatat skateboard dolor brunch. Food -->
<!-- 													truck quinoa nesciunt laborum eiusmod.</p> -->
<!-- 												<p>Duis autem vel eum iriure dolor in hendrerit in -->
<!-- 													vulputate. Ut wisi enim ad minim veniam, quis nostrud -->
<!-- 													exerci tation ullamcorper suscipit lobortis nisl ut.</p> -->
<!-- 												<p> -->
<!-- 													<a class="btn blue" -->
<!-- 														href="ui_tabs_accordions_navs.html#collapse_2_2" -->
<!-- 														target="_blank"> Activate this section via URL </a> -->
<!-- 												</p> -->
<!-- 											</div> -->
<!-- 										</div> -->
<!-- 									</div> -->
<!-- 									<div class="panel panel-default"> -->
<!-- 										<div class="panel-heading"> -->
<!-- 											<h4 class="panel-title"> -->
<!-- 												<a class="accordion-toggle" data-toggle="collapse" -->
<!-- 													data-parent="#accordion2" href="#collapse_2_3"> -->
<!-- 													Collapsible Group Item #3 </a> -->
<!-- 											</h4> -->
<!-- 										</div> -->
<!-- 										<div id="collapse_2_3" class="panel-collapse collapse"> -->
<!-- 											<div class="panel-body"> -->
<!-- 												<p>Anim pariatur cliche reprehenderit, enim eiusmod high -->
<!-- 													life accusamus terry richardson ad squid. 3 wolf moon -->
<!-- 													officia aute, non cupidatat skateboard dolor brunch. Food -->
<!-- 													truck quinoa nesciunt laborum eiusmod. Brunch 3 wolf moon -->
<!-- 													tempor.</p> -->
<!-- 												<p>Duis autem vel eum iriure dolor in hendrerit in -->
<!-- 													vulputate. Ut wisi enim ad minim veniam, quis nostrud -->
<!-- 													exerci tation ullamcorper suscipit lobortis nisl ut.</p> -->
<!-- 												<p> -->
<!-- 													<a class="btn green" -->
<!-- 														href="ui_tabs_accordions_navs.html#collapse_2_3" -->
<!-- 														target="_blank"> Activate this section via URL </a> -->
<!-- 												</p> -->
<!-- 											</div> -->
<!-- 										</div> -->
<!-- 									</div> -->
<!-- 									<div class="panel panel-default"> -->
<!-- 										<div class="panel-heading"> -->
<!-- 											<h4 class="panel-title"> -->
<!-- 												<a class="accordion-toggle" data-toggle="collapse" -->
<!-- 													data-parent="#accordion2" href="#collapse_2_4"> -->
<!-- 													Collapsible Group Item #4 </a> -->
<!-- 											</h4> -->
<!-- 										</div> -->
<!-- 										<div id="collapse_2_4" class="panel-collapse collapse"> -->
<!-- 											<div class="panel-body"> -->
<!-- 												<p>Duis autem vel eum iriure dolor in hendrerit in -->
<!-- 													vulputate. Ut wisi enim ad minim veniam, quis nostrud -->
<!-- 													exerci tation ullamcorper suscipit lobortis nisl ut.</p> -->
<!-- 												<p>Anim pariatur cliche reprehenderit, enim eiusmod high -->
<!-- 													life accusamus terry richardson ad squid. 3 wolf moon -->
<!-- 													officia aute, non cupidatat skateboard dolor brunch. Food -->
<!-- 													truck quinoa nesciunt laborum eiusmod. Brunch 3 wolf moon -->
<!-- 													tempor.</p> -->
<!-- 												<p>Duis autem vel eum iriure dolor in hendrerit in -->
<!-- 													vulputate. Ut wisi enim ad minim veniam, quis nostrud -->
<!-- 													exerci tation ullamcorper suscipit lobortis nisl ut.</p> -->
<!-- 												<p> -->
<!-- 													<a class="btn red" -->
<!-- 														href="ui_tabs_accordions_navs.html#collapse_2_4" -->
<!-- 														target="_blank"> Activate this section via URL </a> -->
<!-- 												</p> -->
<!-- 											</div> -->
<!-- 										</div> -->
<!-- 									</div> -->
<!-- 								</div> -->

<!-- 		<div class="panel-group accordion scrollable" id="accordion2"> -->
<!-- 			<div class="panel panel-default"> -->
<!-- 				<div class="panel-heading"> -->
<!-- 					<h4 class="panel-title"> -->
<!-- 						<a class="accordion-toggle" data-toggle="collapse" -->
<!-- 							data-parent="#accordion2" href="#collapse_2_1">Showroom 1</a> -->
<!-- 					</h4> -->
<!-- 				</div> -->
<!-- 				<div id="collapse_2_1" class="panel-collapse collapse"> -->
<!-- 					<div class="panel-body"> -->
					
<!-- 					<table class="table table-responsive">  -->
<!-- 							<tr>  -->
<!--  								<td class="col-md-4 col-xs-4"><b>Address:</b></td> -->

<!-- 								<td class="col-md-8 col-xs-8"><b>Audi</b></td>  -->
<!--  							</tr>  -->

<!--  						</table>  -->
<!-- 					</div> -->
<!-- 					<div class="panel-body"> -->
					
<!-- 						<table class="table table-responsive">  -->
<!-- 							<tr>  -->
<!--  								<td class="col-md-4 col-xs-4"><b>Address:</b></td> -->

<!-- 								<td class="col-md-8 col-xs-8"><b>Audi</b><br><b>Audi</b></td>  -->
<!--  							</tr>  -->

<!--  						</table> -->
<!-- 					</div> -->
<!-- 					<div class="panel-body"> -->
<!-- 						<table class="table table-responsive">  -->
<!-- 							<tr>  -->
<!--  								<td class="col-md-4 col-xs-4"><b>Address:</b></td> -->

<!-- 								<td class="col-md-8 col-xs-8"><b>Audi</b><br><b>Audi</b></td>  -->
<!--  							</tr>  -->

<!--  						</table> -->
<!-- 					</div> -->
<!-- 					<div class="panel-body"> -->
<!-- 						<table class="table table-responsive">  -->
<!-- 							<tr>  -->
<!--  								<td class="col-md-4 col-xs-4"><b>Address:</b></td> -->

<!-- 								<td class="col-md-8 col-xs-8"><b>Audi</b></td>  -->
<!--  							</tr>  -->

<!--  						</table> -->
<!-- 					</div> -->
<!-- 				</div> -->
<!-- 			</div> -->
			
<!-- 		</div> -->
	</div>

</body>
<!-- END BODY -->
</html>