<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Email_Exe_Send"
	scope="request" />
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
<link href="../assets/css/bootstrap-wysihtml5.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/summernote.css" rel="stylesheet" type="text/css" />
</HEAD>
<body onLoad="Displaypaymode();"
	class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="header.jsp"%>
	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>Send Email</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
			<div class="page-content-inner">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="home.jsp">Home</a> &gt;</li>
						<li><a href="email.jsp">Email</a> &gt;</li>
						<li><a href="email-exe-send.jsp">Send Executive Email<%=mybean.status%></a> :</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

					
						<div class="tab-pane" id="">
							<center>
								<font color="#FF0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										Send Executive Email<%=mybean.status%>
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="form1" method="post" action="email-exe-send.jsp" class="form-horizontal">
											<%if(mybean.branch_id.equals("0")) {%>
												<div class="form-element6 form-element-center" id="emprows">
												<label > Branch: <font color=red>*</font> </label>
													<select name="dr_branch" class="form-control"
														onChange="FormSubmit();">
														<%=mybean.PopulateBranch()%>
													</select>
												</div>
											
											<%}%>
											
<!-- 											<div class="form-group" id="email_exe"> -->
<!-- 										<label class="control-label col-md-4"></label> -->
<!-- 										<div class="col-md-6 col-xs-12" > -->
<!-- 											<select name="my_multi_select1" multiple="multiple" -->
<!-- 												class="multi-select" id="my_multi_select1"> -->
<%-- 												<%=mybean.PopulateExecutives()%> --%>
<!-- 											</select> -->
<!-- 										</div> -->
<!-- 									</div> -->
											<div class="form-element6 form-element-center">
												<label >All Executives: </label>
													<input type="checkbox" name="chk_email_allexe" 
														id="chk_email_allexe" onClick="Displaypaymode();"
														<%=mybean.PopulateCheck(mybean.email_allemail)%>>
											</div>
											<div class="form-element6 form-element-center" style="display: none;" id="email_exe">
													<label >Select Executives:</label>
														<select name="email_allexe" multiple="multiple"
														 class="form-control multiselect-dropdown" id="email_allexe">
													<%=mybean.PopulateExecutives()%>
													</select>
											</div>
											<div class="form-element6 form-element-center" >
												<label > Subject: <font color=red>*</font>
												</label>
													<input name="txt_email_subject" type="text" class="form-control"
														value="<%=mybean.email_subject%>" size="60"
														maxlength="160">
											</div>
											<div class="form-element6 form-element-center">
												<label > Message: <font
													color=red>*</font>
												</label>
													<textarea name="txt_email_msg" cols="60" rows="12"
														class="form-control summernote_1" id="txt_email_msg" maxlength="10000"><%=mybean.email_msg%></textarea>
													<script type="text/javascript">
									   CKEDITOR.replace('txt_email_msg', 
									    {
										  uiColor: hexc($("a:link").css("color")),
									    });		
								     </script>
													<br> <span id="span_email_msg"></span>
											</div>
											<center>
												<input name="send_button" type="submit" class="btn btn-success"
													Value="Send" size="30" maxlength="255" onClick="onPress()"></input>
													 
											</center><br></br>
											<center>
												<table class="table table-bordered">
													<tr>
														<th style="text-align:center" colspan="2"><b>Substitution Variables </b></th>
													</tr>
													<tr>
														<td align="right">Executive Name:</td>
														<td>[NAME]</td>
													</tr>
													<tr>
														<td align="right">Executive ID:</td>
														<td>[EXECUTIVEID]</td>
													</tr>
													<tr>
														<td align="right">Executive Ref. No.:</td>
														<td>[EXECUTIVEREFNO]</td>
													</tr>
												</table>
											</center>
										</form>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
	<!-- END CONTAINER -->

	<%@include file="../Library/admin-footer.jsp"%>
		<%@include file="../Library/js.jsp"%>
	<script src="../assets/js/summernote.min.js" type="text/javascript"></script>
   <script src="../assets/js/components-editors.min.js" type="text/javascript"></script>
</body>
<script >
// function FormSubmit() {
// 	document.form1.submit();
// }
// function onPress() {
// 	for (i = 0; i < document.form1.exe_team_trans.options.length; i++) {
// 		document.form1.exe_team_trans.options[i].selected = true;
// 	}
// }
// function hideRow(element)
// {
// //element refer to row id where u want to hide a row.
// var myObj = document.getElementById(element);
// //u need to a give id for a row to which u want to hide
// var cels = myObj.getElementsByTagName('select');
// 	for(var col_no=0; col_no < cels.length; col_no++)
// 		{
// 		cels[col_no].style.display='none';
// 		}
// 		myObj.style.visibility = 'hidden';
//  }

//  function displayRow(element)
// { 
// 	var myObj = document.getElementById(element);
// 	//u need to a give id for a row to which u want to hide
// 	var cels = myObj.getElementsByTagName('select')
// 		for(var col_no=0; col_no < cels.length; col_no++)
// 			{
// 				cels[col_no].style.display='';
// 			}
// 		myObj.style.visibility = 'visible';
// 		//myObj.style.height = '0px';
//  }
 /* function Displaypaymode()
        {
       var zone=document.form1.chk_email_allexe.checked;
        if(zone==true) zone="1";
        else zone="0";
//        alert("str======="+zone);
        if(zone=="1")
        {
        	$("#email_exe").hide(1000)
//             hideRow('email_exe');
//             document.getElementById("add_space").style.margin = 0 + "px";
        }
         if(zone=="0")
        {
        	 $("#email_exe").show(1000)
//             displayRow('email_exe');
//             document.getElementById("add_space").style.marginTop = 200 + "px";
        }
 } */
 function FormSubmit() {
		document.form1.submit();
	}
	
	function Displaypaymode() {
		if($('#chk_email_allexe').prop('checked') == true)
			$('#email_exe').hide();
		else
			$('#email_exe').show();
	}
 </script>
 
 
  
</body>
</HTML>
