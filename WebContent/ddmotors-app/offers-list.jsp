<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.ddmotors_app.Offers_List"
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
b {
	color: #000000;
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
				<center>Offers</center>
			</h3>
		</div>
	</div>
	
				<%=mybean.StrHTML%>


  <!-- <div class="table table-responsive">
	<table class="table table-responsive table-bordered" align="center"
						cellpadding="0" cellspacing="0">
					 <thead>
						 <tr>
							 <th width="20%" align="center" valign="top">MODEL</th>
							 <th align="center" valign="top">Consumer Offer</th>
							 <th align="center" valign="top"><div data-canvas-width="178.8159023823059">Exchange Bonus  (Less </div>
						     <div data-canvas-width="160.6510696542921">than 7 years old car)</div></th>
							 <th align="center" valign="top"><div data-canvas-width="139.68773328470752">Wheels Of India / </div>
						     <div data-canvas-width="115.85805076102928">Journey Of Life</div></th>
                              <th align="center" valign="top"><div data-canvas-width="126.67893150843621">Savings on MGA </div>
                              <div data-canvas-width="26.763871190294868">Kit </div></th>
                              <th align="center" valign="top">Total Savings</th>
						 </tr>
						 </thead>
 <tbody>
						 <tr>
							 <td align="left" valign="top">Alto 800 MC (All Variants)</td>
							 <td align="right" valign="top">20,000</td>
							 <td align="right" valign="top">20,000</td>
							 <td align="right" valign="top">5,000</td>
							 <td align="left" valign="top">0</td>
                  <td align="left" valign="top">45,000</td>
                   </tr>
						 <tr>
							 <td align="left" valign="top">Alto K10 (P) / (CNG) MT</td>
							 <td align="right" valign="top">15,000</td>
							 <td align="right" valign="top">20,000</td>
							 <td align="right" valign="top">5,000</td>
							 <td align="left" valign="top">0</td>
                  <td align="left" valign="top">40,000</td>
                   </tr>
						 <tr>
							 <td align="left" valign="top">Alto K10 AMT</td>
							 <td align="right" valign="top">20,000</td>
							 <td align="right" valign="top">20,000</td>
							 <td align="right" valign="top">5,000</td>
							 <td align="left" valign="top">0</td>
                  <td align="left" valign="top">45,000</td>
                   </tr>
						 <tr>
							 <td align="left" valign="top">Celerio (D) MT</td>
							 <td align="right" valign="top">50,000</td>
							 <td align="right" valign="top">25,000</td>
							 <td align="right" valign="top">5,000</td>
							 <td align="left" valign="top">0</td>
                   <td align="left" valign="top">80,000</td>
                   </tr>
						 <tr>
							 <td align="left" valign="top">Celerio (P) MT</td>
							 <td align="right" valign="top">15,000</td>
							 <td align="right" valign="top">25,000</td>
							 <td align="right" valign="top">5,000</td>
							 <td align="left" valign="top">0</td>
                   <td align="left" valign="top">45,000</td>
                   </tr>
						 <tr>
							 <td align="left" valign="top">Celerio AMT</td>
							 <td align="right" valign="top">20,000</td>
							 <td align="right" valign="top">25,000</td>
							 <td align="right" valign="top">5,000</td>
							 <td align="left" valign="top">0</td>
                   <td align="left" valign="top">50,000</td>
                   </tr>
						 <tr>
							 <td align="left" valign="top">Celerio CNG MT</td>
							 <td align="right" valign="top">15,000</td>
							 <td align="right" valign="top">25,000</td>
							 <td align="right" valign="top">5,000</td>
					
                  		 <td align="left" valign="top">0</td>
                  <td align="left" valign="top">45,000</td>
                    </tr>
						 <tr>
							 <td align="left" valign="top">CIAZ (All Variants)</td>
							 <td align="right" valign="top">10,000</td>
							 <td align="right" valign="top">40,000</td>
							 <td align="right" valign="top">10,000</td>
							 <td align="left" valign="top">0</td>
                   <td align="left" valign="top">60,000</td>
                   </tr>
						 <tr>
						  <td align="left" valign="top">Dzire (D)</td>
							 <td align="right" valign="top">15,000</td>
							 <td align="right" valign="top">25,000</td>
							 <td align="right" valign="top">5,000</td>
							 <td align="left" valign="top">0</td>
                   <td align="left" valign="top">45,000</td>
                   </tr>
						 <tr>
							 <td align="left" valign="top">Dzire Zdi AMT</td>
							 <td align="right" valign="top">20,000</td>
							 <td align="right" valign="top">25,000</td>
							 <td align="right" valign="top">5,000</td>
							 <td align="left" valign="top">0</td>
                   <td align="left" valign="top">50,000</td>
                   </tr>
						 <tr>
							 <td align="left" valign="top">Dzire (P)</td>
							 <td align="right" valign="top">10,000</td>
							 <td align="right" valign="top">20,000</td>
							 <td align="right" valign="top">5,000</td>
							 <td align="left" valign="top">0</td>
                   <td align="left" valign="top">35,000</td>
                   </tr>
						 <tr>
							 <td align="left" valign="top">Dzire Tour</td>
							 <td align="right" valign="top">20,000</td>
							 <td align="right" valign="top">25,000</td>
							 <td align="right" valign="top">5,000</td>
							 <td align="left" valign="top">0</td>
                   <td align="left" valign="top">50,000</td>
                   </tr>
						 <tr>
							 <td align="left" valign="top">Eeco (P) (except Cargo)</td>
							 <td align="right" valign="top">15,000</td>
							 <td align="right" valign="top">20,000</td>
							 <td align="right" valign="top">5,000</td>
							 <td align="left" valign="top">0</td>
                   <td align="left" valign="top">40,000</td>
                   </tr>
						 <tr>
							 <td align="left" valign="top">Eeco Ambulance</td>
							 <td align="right" valign="top">0</td>
							 <td align="right" valign="top">20,000</td>
							 <td align="right" valign="top">5,000</td>
							 <td align="left" valign="top">0</td>
                  <td align="left" valign="top">25,000</td>
                    </tr>
						 <tr>
							 <td align="left" valign="top">Eeco Cargo CNG</td>
							 <td align="right" valign="top">30,000</td>
							 <td align="right" valign="top">20,000</td>
							 <td align="right" valign="top">5,000</td>
							 <td align="left" valign="top">0</td>
                   <td align="left" valign="top">55,000</td>
                   </tr>
						 <tr>
							 <td align="left" valign="top">Eeco CNG (except Cargo CNG)</td>
							 <td align="right" valign="top">10,000</td>
							 <td align="right" valign="top">20,000</td>
							 <td align="right" valign="top">5,000</td>
							 <td align="left" valign="top">0</td> 
							    <td align="left" valign="top">35,000</td>
                       </tr>

						 <tr>
							 <td align="left" valign="top">Omni (except Cargo &amp; Amb)</td>
							 <td align="right" valign="top">10,000</td>
							 <td align="right" valign="top">20,000</td>
							 <td align="right" valign="top">5,000</td>							 
                   <td align="left" valign="top">0</td>
                      <td align="left" valign="top">35,000</td>

                   </tr>
						 <tr>
							 <td align="left" valign="top">Omni Cargo</td>
							 <td align="right" valign="top">10,000</td>
							 <td align="right" valign="top">20,000</td>
							 <td align="right" valign="top">5,000</td>							 <td align="left" valign="top">0</td>

                      <td align="left" valign="top">35,000</td>
                   </tr>
						 <tr>
							 <td align="left" valign="top">Omni Ambulance</td>
							 <td align="right" valign="top">0</td>
							 <td align="right" valign="top">20,000</td>
							 <td align="right" valign="top">0</td>
							 <td align="left" valign="top">0</td>
                      <td align="left" valign="top">20,000</td>
                   </tr>
						 <tr>
							 <td align="left" valign="top">Ertiga (All Variants)</td>
							 <td align="right" valign="top">10,000</td>
							 <td align="right" valign="top">20,000</td>
							 <td align="right" valign="top">5,000</td>
							 <td align="left" valign="top">0</td>
                      <td align="left" valign="top">35,000</td>
                   </tr>
						 <tr>
							 <td align="left" valign="top">Ritz (D)</td>
							 <td align="right" valign="top">20,000</td>
							 <td align="right" valign="top">30,000</td>
							 <td align="right" valign="top">0</td>
							 <td align="left" valign="top">0</td>
                      <td align="left" valign="top">50,000</td>
                   </tr>
						 <tr>
							 <td align="left" valign="top">Ritz Lxi</td>
							 <td align="right" valign="top">15,000</td>
							 <td align="right" valign="top">30,000</td>
							 <td align="right" valign="top">0</td>
							 <td align="left" valign="top">0</td>
                      <td align="left" valign="top">45,000</td>
                   </tr>
						 <tr>
							 <td align="left" valign="top">Ritz Vxi/ Zxi</td>
							 <td align="right" valign="top">20,000</td>
							 <td align="right" valign="top">30,000</td>
							 <td align="right" valign="top">0</td>
							 <td align="left" valign="top">0</td>
                      <td align="left" valign="top">50,000</td>
                   </tr>
						 <tr>
							 <td align="left" valign="top">Stingray MT</td>
							 <td align="right" valign="top">30,000</td>
                              <td align="right" valign="top">25,000</td>
							 <td align="right" valign="top">5,000</td>
							 <td align="right" valign="top">0</td>
						    <td align="left" valign="top">60,000</td>
						 </tr>
						 <tr>
							 <td align="left" valign="top">Swift (D)</td>
							 <td align="right" valign="top">15,000</td>
                              <td align="right" valign="top">20,000</td>
							 <td align="right" valign="top">5,000</td>
							 <td align="right" valign="top">0</td>
						    <td align="left" valign="top">40,000</td>
                         </tr>
                          <tr>
								<td align="left" valign="top">Swift Lxi(O)/ Lxi (O) Plus</td>
								<td align="right" valign="top">18,000</td>
                              <td align="right" valign="top">20,000</td>
								<td align="right" valign="top">5,000</td>
								<td align="right" valign="top">0</td>
						    <td align="left" valign="top">43,000</td>
						 </tr>
						 <tr>
							 <td align="left" valign="top">Swift Vxi/ Vxi Ltd. and Zxi</td>
							 <td align="right" valign="top">10,000</td>
                              <td align="left" valign="top">20,000</td>
							 <td align="right" valign="top">5,000</td>
						 	 <td align="right" valign="top">0</td>
						    <td align="left" valign="top">35,000</td>
						 </tr>
						 <tr>
							 <td align="left" valign="top">Wagon R (P) and (CNG)</td>
							 <td align="right" valign="top">20,000</td>
                              <td align="left" valign="top">25,000</td>
							 <td align="right" valign="top">5,000</td>
							 <td align="right" valign="top">0</td>
						     <td align="left" valign="top">50,000</td>
                         </tr>
                          <tr>
								<td align="left" valign="top">Wagon R &amp; Stingray AMT</td>
								<td align="right" valign="top">25,000</td>
                              <td align="left" valign="top">25,000</td>
								<td align="right" valign="top">5,000</td>
								<td align="right" valign="top">0</td>
						     <td align="left" valign="top">55,000</td>
                         </tr>
                          <tr>
								<td align="left" valign="top">Wagon R Felicity (Lxi &amp; CNG)</td>
								<td align="right" valign="top">20,000</td>
                              <td align="left" valign="top">25,000</td>
								<td align="right" valign="top">5,000</td>
								<td align="right" valign="top">13,000</td>
						    <td align="left" valign="top">63,000</td>
                         </tr>
                          <tr>
								<td align="left" valign="top">Wagon R Felicity (VXi)</td>
								<td align="right" valign="top">20,000</td>
                              <td align="left" valign="top">25,000</td>
								<td align="right" valign="top">5,000</td>
								<td align="right" valign="top">9,000</td>
						    <td align="left" valign="top">59,000</td>
                          </tr>
                          <tr>
								<td align="left" valign="top">Wagon-R (LPG)</td>
								<td align="right" valign="top">0</td>
                              <td align="left" valign="top">25,000</td>
								<td align="right" valign="top">5,000</td>
								<td align="right" valign="top">0</td>
						     <td align="left" valign="top">30,000</td>
                         </tr>
                          
                          
                </tbody>
			    </table>
			    </div> -->

	
</body>
<!-- END BODY -->
</html>