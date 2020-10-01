<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.axelaauto_app.App_Share_Images"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html>

<html lang="en">
<head>

<meta content="width=device-width, initial-scale=1" name="viewport">

<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="css/components-rounded.css" id="style_components"
	rel="stylesheet" type="text/css">
<link href="css/default.css" rel="stylesheet" type="text/css"
	id="style_color">
<LINK REL="STYLESHEET" TYPE="text/css"
	HREF="../Library/theme<%=mybean.GetTheme(request)%>/jquery-ui.css">

<script>
	function populateCategory() {
		var imgdatacat_model_id = document
				.getElementById('dr_imgdatacat_model_id').value;
		var imgdatacat_type_id = document
				.getElementById('dr_imgdatacat_type_id').value;
		var url = "../axelaauto-app/share-images-check.jsp?image=yes&imgdatacat_model_id="
				+ imgdatacat_model_id
				+ "&imgdatacat_type_id="
				+ imgdatacat_type_id;
		showHint(url, '123', 'modelcategory');
	}
</script>
<style>
b {
	color: #8E44AD;
}
 
a:hover {
	color: #fff;
	text-decoration: underline
}
 span{
 color: red;
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
</style>
</head>

<body <%if (!mybean.msg.equals("")) {%>
	onload="showToast('<%=mybean.msg%>')" <%}%>>
	<div class="header-wrap">
		<div class="panel-heading">
			<span class="panel-title"> <strong><center>Share</center></strong></span>
		</div>
	</div>
	<div class="container" style="margin-top: 25px;">

		<form role="form" id="frmshareimages" name="frmshareimages"
			class="form-horizontal" method="post">
			<div class="form-body">
				<div class="alert alert-danger display-hide">
					<button class="close" data-close="alert"></button>
					You have some form errors. Please check below.
				</div>
				<div class="alert alert-success display-hide">
					<button class="close" data-close="alert"></button>
					Your form validation is successful!
				</div>

				<div class="form-group form-md-line-input text-center">

					<label for="form_control_1"><b><h4><a
							href="callurlenquiry-dash.jsp?oppr_id=
								<%=mybean.oppr_id%>"
							style="color: red; text-decoration: underline;"> Opportunity ID: <%=mybean.oppr_id%></a></h4></b></label>
				</div>
				<div class="form-group form-md-line-input">
					<label for="form_control_1">Model<span>*</span>:</label> <select
						class="form-control" name="dr_imgdatacat_model_id" 
						id="dr_imgdatacat_model_id"
						onChange="populateCategory();">
						<%=mybean.PopulateModel(mybean.imgdatacat_model_id)%>
					</select>
				</div>

				<div class="form-group form-md-line-input">
					<label for="form_control_1">Type<span>*</span>:</label> <select
						class="form-control" name="dr_imgdatacat_type_id"
						id="dr_imgdatacat_type_id" value="<%=mybean.imgdatacat_type_id%>"
						onChange="populateCategory();">
						<%=mybean.PopulateType()%>
					</select>
				</div>

				<div class="form-group form-md-line-input">
					<label for="form_control_1">Category<span>*</span>:</label> <span
						id="modelcategory"> <%=mybean.PopulateCategory(mybean.imgdatacat_model_id,
					mybean.imgdatacat_type_id)%>
					</span>
				</div>
				<input type="hidden" class="form-control" name="txt_oppr_hint"
					id="txt_oppr_hint" />

			</div>
			<br>
			<div class="form-actions noborder">
				<center>
					<button type="button" class="btn1" name="addbutton" id="addbutton"
						onclick='return getSearchResult();'>Share Images</button>
					<button type="button" class="btn1" name="addbutton" id="addbutton"
						onclick='getLink();'>Share Link</button>
				</center>
				<input type="hidden" name="add_button" value="yes"><br>
				<br>
			</div>
		</form>
	</div>


	<script language="JavaScript" type="text/javascript">
		function getLink() {
			var msg = "";
			var model_id = document.getElementById("dr_imgdatacat_model_id").value;

			if (model_id == '0') {
				msg += '<br>Select Model!';
			}

			var type_id = document.getElementById("dr_imgdatacat_type_id").value;
			if (type_id == '0') {
				msg += '<br>Select Type!';
			}

			var cat_id = document.getElementById("dr_cat_id").value;
			if (cat_id == '0') {
				msg += '<br>Select Category!';
			}

			if (msg != '') {
				showToast(msg);
			}
			if (msg == '') {
				window.location.href = "sharelinkshare-link.jsp?model_id=" + model_id
						+ "&type_id=" + type_id + "&cat_id=" + cat_id + "";
			}
			return true;
		}
	</script>

	<script language="JavaScript" type="text/javascript">
		function getSearchResult() {
			var msg = "";
			imgdatacat_model_id = document
					.getElementById("dr_imgdatacat_model_id").value;
			if (imgdatacat_model_id == '0') {
				msg += '<br>Select Model!';
			}

			imgdatacat_type_id = document
					.getElementById("dr_imgdatacat_type_id").value;
			if (imgdatacat_type_id == '0') {
				msg += '<br>Select Type!';
			}

			imgdatacat_id = document.getElementById("dr_cat_id").value;
			if (imgdatacat_id == '0') {
				msg += '<br>Select Category!';
			}

			if (msg != '') {
				showToast(msg);
			} else {
				url = '../axelaauto-app/share-images-check.jsp?share=yes&imgdatacat_model_id='
						+ imgdatacat_model_id
						+ '&imgdatacat_type_id='
						+ imgdatacat_type_id
						+ '&imgdatacat_id='
						+ imgdatacat_id;

				$.get(url, function(data, status) {
					//alert(data.trim());
					if (status == 'success') {
						if (data.trim() == 'No Images Found!')
							showToast('No Images Found!');
						else
							imageURL(data.trim());
					} else {
						showToast('No Images Found12345!');
					}
				})
			}
		}
	</script>
<script src="js/jquery.min.js" type="text/javascript"></script>
<script src="js/bootstrap.min.js" type="text/javascript"></script>
<script src="js/metronic.js" type="text/javascript"></script>
<script src="js/axelamobilecall.js" type="text/javascript"></script>
<script src="../Library/dynacheck.js" type="text/javascript"></script>
</body>
<!-- END BODY ----->
</html>