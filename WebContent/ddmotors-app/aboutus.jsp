<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.ddmotors_app.AboutUs"
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

<style>
h4, p {
	color: #0f4c75;
}
p.abtus{
text-align: justify;
font-family: Calibri; 
}
center {
	font-size: 20px;
}
</style>
</head>

<body>
	<div class="panel panel-default">
		<div class="panel-heading">
			<h3 class="panel-title">
				<center>About Us</center>
			</h3>
		</div>
	</div>
	<div class="container">
	
<%-- 					<%=mybean.StrHTML%> --%>
	
		<div class="portlet light">
		<article>
					<h5>
						<strong>DD Motors - The One Stop Shop for all your needs</strong>
					</h5>
					<p>
						D.D. Motors an Authorised Multi-location Maruti Suzuki Dealership
						has been present in India since 1996. Since its inception,
						expansion has been DD Motors vision. Headquartered in New Delhi,
						DDMotors family includes over 1500 employees and has marked 3
						corners West, North and South of New Delhi and also has its
						presence in Dehradun, Uttarakhand with overall <strong>9
							showrooms, 3 True Value outlets, 8 Workshops and 7 bodyshops,
							which include 5 SSS&#8217;s (A one stop Sales, Service, Spares
							centre )</strong>. DD&#8217;s vision is to create an organization
						that would be admired and sought out for its professionalism and
						quality of work. This inspiration is still alive in our work
						ethics and forms the foundation of our success. The Dealership
						also works to deliver a wide range of initiatives and schemes to
						promote the automobile industry and enable the dealership to
						become even more successful.
					</p>
					<p>To derive its strength in its commitment to create architectural
						marvel by using state of the art technology, automobile,
						consulting global architects and by motivating human potential to
						scale new heights.</p>
					<p>
						<strong>Our Services:</strong>
					</p>

					<ul>
						<li>New Cars</li>
						<li>Certified Used Cars (India&#8217;s Largest True
							Value Outlet at Moti nagar, New Delhi)</li>
						<li>Auto Finance</li>
						<li>Auto Insurance</li>
						<li>General Insurance</li>
						<li>Car Service</li>
						<li>Body Repair</li>
						<li>Auto Card Loyalty Programme</li>
					</ul>
					<p>
						<strong>D.D. Motors Built Around You!</strong>
					</p>
					<ul>
						<li>Sales Over 17000 cars in a year</li>
						<li>Services over 120,000 vehicles in a year</li>
						<li>Body repair 18000 vehicles in a year</li>
						<li>50,000 Insurance renewal Policies a year</li>
						<li>All this with top 10 CSI and SSI ratings all
							India</li>
					</ul>
					<p>A customer is the most important visitor on our premises; he is
						not dependent on us, we are dependent on them. He is not an
						interruption in our work, he is the purpose of it. He is not an
						outsider in our business, he is part of it. We are not doing him a
						favour by serving him, he is doing us a favour by giving us an
						opportunity to do so.</p>



				</article>

		</div>
	</div>


</body>
<!-- END BODY -->
</html>
