<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.portal.Email_Contact_Send" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href='../Library/jquery.qtip.css' rel='stylesheet'
		type='text/css' />

	<link href="../assets/css/font-awesome.css" rel="stylesheet"
		type="text/css" />
	<link href="../assets/css/bootstrap.css" rel="stylesheet"
		type="text/css" />
	<link href="../assets/css/components-rounded.css" rel="stylesheet"
		id="style_components" type="text/css" />
	<link href="../assets/css/font-awesome.css" rel="stylesheet"
		type="text/css" />
		<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />

<link href="../assets/css/bootstrap-wysihtml5.css" rel="stylesheet" type="text/css" />
        <link href="../assets/css/summernote.css" rel="stylesheet" type="text/css" />	
<script language="JavaScript" type="text/javascript">
            function hideRow(element)
{
//element refer to row id where u want to hide a row.
var myObj = document.getElementById(element);
//u need to a give id for a row to which u want to hide
var cels = myObj.getElementsByTagName('td');
	for(var col_no=0; col_no < cels.length; col_no++)
		{
		cels[col_no].style.display='none';
		}
		myObj.style.visibility = 'hidden';
 }

 function displayRow(element)
{
	var myObj = document.getElementById(element);
	//u need to a give id for a row to which u want to hide
	var cels = myObj.getElementsByTagName('td')
		for(var col_no=0; col_no < cels.length; col_no++)
			{
				cels[col_no].style.display='';
			}
		myObj.style.visibility = 'visible';
		//myObj.style.height = '0px';
 }
 function Displaypaymode()
        {
       var group=document.form1.chk_email_allgroup.checked;
        if(group==true) group="1";
        else zone="0";
//        alert("str======="+zone);
        if(group=="1")
        {
            hideRow('group');
        }
         if(group=="0")
        {
            displayRow('group');
        }
 }
		
		</script>
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
<body  onLoad="Displaypaymode();" class="page-container-bg-solid page-header-menu-fixed">
<%@include file="header.jsp" %>
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
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="home.jsp">Home</a> &gt;</li>
						<li><a href="email.jsp">Email</a> &gt;</li>
						<li><a href="email-contact-send.jsp">Send Email<%=mybean.status%></a>:</li>
	
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					
					<div class="page-content-inner">
					<div class="tab-pane" id="">
						<div class="portlet box  ">
				<div class="portlet-title" style="text-align: center">
					<div class="caption" style="float: none">
						Send Email <%=mybean.status%>
					</div>
				</div>
				<div class="portlet-body portlet-empty">
					<div class="tab-pane" id="">
						<!-- START PORTLET BODY -->
						<form name="form1" method="post" action="email-contact-send.jsp" class="form-horizontal">
						 <%if(mybean.cont.equals("yes")) {%>
							<div class="form-group">
										<label class="control-label col-md-4">Contact ID: 
										</label>
										<div class="col-md-6 col-xs-12" id="emprows">
											<input name= "contact_id" type="text" class="form-control" onKeyUp="toNumber('contact_id','Contact ID');" value="<%=mybean.customer_id%>" size="60" maxlength="60"/>
                           <input type="hidden" name="cont" value="yes">

										</div>
									</div>
									 <%}if(mybean.contkey.equals("yes")) {%>
									<div class="form-group">
										<label class="control-label col-md-4">Contact Key ID:
										</label>
										<div class="col-md-6 col-xs-12" id="emprows">
										<input name= "contact_key_id" type="text" class="form-control" id="contact_key_id" onKeyUp="toNumber('contact_id','Contact ID');" value="<%=mybean.customer_contact_id%>" size="60" maxlength="60"/>
                         <input type="hidden" name="contkey" value="yes">	

										</div>
									</div>
									 <%}if(mybean.allconn.equals("yes")) {%>
									<div class="form-group">
										<label class="control-label col-md-4">All:
										</label>
										<div class="col-md-6 col-xs-12" id="emprows">
											<input type="checkbox" name="chk_email_allgroup" id="chk_email_allgroup" onClick="Displaypaymode();" <%=mybean.PopulateCheck(mybean.email_allgroup) %>>
	                     <input type="hidden" name="allconn" value="yes">

										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-md-4">Groups: 
										</label>
										<div class="col-md-6 col-xs-12" id="emprows">
											<select name="dr_group" size="20" multiple="multiple" class="form-control" id="fo_group" style="width:300px;">
		               <%=mybean.PopulateGroup() %>
	                 </select>

										</div>
									</div>
									<%}%>
									<div class="form-group">
										<label class="control-label col-md-4">Subject: <font
											color=red>*</font>
										</label>
										<div class="col-md-6 col-xs-12" id="emprows">
											<input name= "txt_email_subject" type="text" class="form-control"   value="<%=mybean.email_subject%>" size="60" maxlength="160">

										</div>
									</div>
									<div class="form-group">
										<label class="control-label col-md-4">Message: <font
											color=red>*</font>
										</label>
										<div class="col-md-6 col-xs-12" id="emprows">
											<textarea name="txt_email_msg" cols="60" rows="12" 
											class="form-control summernote_1" id="txt_email_msg" maxlength="10000">
											<%=mybean.email_msg%></textarea> <%if(mybean.smartcont.equals("yes")) {%>
                            <input type="hidden" name="smartcont" value="yes">
                            <input type="hidden" name="tag" value="<%=mybean.tag%>">
                             <%}%>
                              <%if(mybean.smartcontkey.equals("yes")) {%>
                            <input type="hidden" name="smartcontkey" value="yes">
                             <%}%>
                       <script type="text/javascript">
									   var editor = CKEDITOR.replace('txt_email_msg', 
									    {
										    toolbar: 'MyToolbar',
											//customConfig: '',
										    width: '95%'
									    });		
								     </script>
                       <br>
                       <span id="span_email_msg"></span>

										</div>
									</div>
									<center>
									<input name="send_button" type="submit" class="btn btn-success" Value="Send" size="30" maxlength="255">
									</center><br></br>
	
						<table class="table table-bordered">
                           <tr>
                             <th  style="text-align:center" colspan="2"><b>Substitution Variables</b></th>
                           </tr>
                           <tr>
                             <td align="right">Contact Name:</td>
                             <td>[NAME]</td>
                           </tr>
                           <tr>
                             <td align="right">Contact ID:</td>
                             <td>[CONTACTID]</td>
                           </tr>
                         </table>
                     

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

    <%@include file="../Library/admin-footer.jsp" %>
    <script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="../Library/Validate.js"></script>
	
	<script src="../assets/js/summernote.min.js" type="text/javascript"></script>
   <script src="../assets/js/components-editors.min.js" type="text/javascript"></script>
    </body>
</HTML>
