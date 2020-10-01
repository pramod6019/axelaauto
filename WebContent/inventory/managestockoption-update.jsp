<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.inventory.ManageStockOption_Update" scope="request"/>
<%mybean.doGet(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
        <HEAD>
        <title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="shortcut icon"  type="image/x-icon" href="../admin-ifx/axela.ico">
        <link rel="shortcut icon" type="image/x-icon"
	href="../admin-ifx/axela.ico">
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


	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript"
		src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
        
        <script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
        <script type="text/javascript" src="../Library/Validate.js"></script>
        <SCRIPT language=JavaScript src="../Library/dynacheck.js" type="text/javascript"></SCRIPT>
        <script language="JavaScript" type="text/javascript">

function FormFocus() { //v1.0
  document.form1.txt_option_code.focus()
}
        </script>
        <link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
        <body  onLoad="FormFocus()" leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
        <%@include file="../portal/header.jsp" %>
        <div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>Update Stock Option </h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt; </li>
						<li><a href="../portal/manager.jsp">Business Manager</a> &gt; </li>
						<li><a href="../inventory/managestockoption.jsp?all=yes">Stock Option</a> &gt; </li>
						<li><a href="managestockoption-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Stock Option</a><b>:</b></li>
						
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					
					<div class="page-content-inner">
					<div class="tab-pane" id="">
<!-- 					BODY START -->
					<center><font color="#ff0000" ><b><%=mybean.msg%>
            </b></font></center>
						 <div class="portlet box  ">
				<div class="portlet-title" style="text-align: center">
					<div class="caption" style="float: none">
						<%=mybean.status%>&nbsp;Stock Option
					</div>
				</div>
				<div class="portlet-body portlet-empty">
					<div class="tab-pane" id="">
						<!-- START PORTLET BODY -->
						<center><font>Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b> are required. </font></center>
						<form name="form1" class="form-horizontal" method="post">
						
<!-- 	Input form-control -->
		<div class="form-body">
			<div class="form-group">
				<label class="col-md-3 control-label">Brand<b><font color="#ff0000">*</font></b>:</label>
				<div class="col-md-6">
					<select name="dr_option_brand_id" class="form-control" id="dr_option_brand_id">
                    		    <%=mybean.PopulatePrincipal(mybean.option_brand_id, mybean.comp_id)%>        
                    	 		 </select>

				</div>
			</div>
		</div>
		<div class="form-body">
			<div class="form-group">
				<label class="col-md-3 control-label">Name<font color="#ff0000">*</font>:</label>
				<div class="col-md-6">
					<input name ="txt_option_name" type="text" class="form-control" id="txt_option_name" value="<%=mybean.option_name %>" size="40"  maxlength="255"/></td>
                       	    

				</div>
			</div>
		</div>
		<div class="form-body">
			<div class="form-group">
				<label class="col-md-3 control-label">Code<font color="#ff0000">*</font>:</label>
				<div class="col-md-6">
					<input name ="txt_option_code" type="text" class="form-control" id="txt_option_code" value="<%=mybean.option_code %>" size="40" maxlength="255"/></td>
                                    	    

				</div>
			</div>
		</div>
		<div class="form-body">
			<div class="form-group">
				<label class="col-md-3 control-label">Type<font color="#ff0000">*</font>:</label>
				<div class="col-md-6">
					<select name="dr_option_optiontype_id" class="form-control" id="dr_option_optiontype_id">
                                <%=mybean.PopulateOptionType()%>
                              </select>
				</div>
			</div>
		</div>
		<%if(mybean.status.equals("Add")){%>
                                <center><input name="button" type="submit" class="btn btn-success" id="button" value="Add Stock Option"  onClick="return SubmitFormOnce(document.form1, this);" />
                                <input type="hidden" name="add_button" value="yes"></input></center>
                                <%}else if (mybean.status.equals("Update")){%>
                                <center><input type="hidden" name="update_button" value="yes">
                                <input name="button" type="submit" class="btn btn-success" id="button" value="Update Stock Option"  onClick="return SubmitFormOnce(document.form1, this);" />
                                <input name="delete_button" type="submit" class="btn btn-success" id="delete_button" OnClick="return confirmdelete(this)" value="Delete Stock Option"/></center>
                                <%}%>
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
     <%@include file="../Library/admin-footer.jsp" %></body>
</HTML>
