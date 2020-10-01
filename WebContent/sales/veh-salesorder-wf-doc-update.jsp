<%@ page errorPage="../portal/error-page.jsp" %>  
<jsp:useBean id="mybean" class="axela.sales.Veh_Salesorder_Wf_Doc_Update" scope="request"/>
<%mybean.doPost(request,response); %>
<jsp:setProperty name="mybean" property="*" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<link href="../assets/css/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/components-rounded.css" rel="stylesheet" id="style_components" type="text/css" />
<link rel="shortcut icon"  type="image/x-icon" href="../admin-ifx/axela.ico">
<link href='../Library/jquery.qtip.css' rel='stylesheet' type='text/css' />
<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css" rel="stylesheet" type="text/css" />

 <script language="JavaScript" type="text/javascript">
function FormFocus() { //v1.0
  document.formcontact.txt_doc_title.focus();
}
function frmSubmit()
{
	document.formcontact.submit();
}
</script>
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
	
</HEAD>
    <body onLoad="FormFocus();"  class="page-container-bg-solid page-header-menu-fixed">
    <%@include file="../portal/header.jsp" %>
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
						<h1><%=mybean.status%>
							&nbsp;Document
						</h1>
					</div>
					<!-- END PAGE TITLE -->
					</div>
			</div>
			<!-- END PAGE HEAD-->
            <!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a>&gt;</li>
						<li><a href="../sales/index.jsp">Sales</a>&gt;</li>
						<li><a href="../sales/veh-salesorder.jsp">Sales Order</a>&gt;</li>
						<li><a href="veh-salesorder-list.jsp?all=yes">List Sales Order</a> &gt;</li>
						<li><a href="veh-salesorder-list.jsp?so_id=<%=mybean.doc_so_id%>"><%=mybean.so_no%></a> &gt;</li> 
						<li><a href="veh-salesorder-wf-doc-list.jsp?so_id=<%=mybean.doc_so_id%>">List Work Flow Documents</a> &gt; <a href="salesorder-wf-doc-update.jsp?<%=mybean.QueryString%>">
						<%=mybean.status%> Document</a>:</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="#ff0000"><b><%=mybean.msg%></b></font>
					</center>
					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<center><%=mybean.status%>
											Document
										</center>
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
									<form name="formcontact" method="post" class="form-horizontal">
											<center>
												<font size="">Form fields marked with a red asterisk
													<font color=#ff0000>*</font> are required.
												</font>
												</center>
												<div class="form-group">
			<label class="control-label col-md-4">SO No:</label>
					<div class="txt-align">
						<b><%=mybean.so_no%></b>
                       <b>
                       <input type="hidden" name="so_id" id="so_id" value="<%=mybean.doc_so_id%>">
                       </b>
					</div>
				</div>
				<div class="form-group">
			<label class="control-label col-md-4">Title<font color="#ff0000">*</font>:</label>
					<div class="col-md-6">
						<input name="txt_doc_title" id="txt_doc_title" type="text" class="form-control" value="<%=mybean.doc_title%>" maxlength="255"  size="50">
					</div>
				</div>
				<div class="form-group">
			<label class="control-label col-md-4">No of Days<font color="#ff0000">*</font>:</label>
					<div class="col-md-6">
						<input name="txt_doc_duedays" id="txt_doc_duedays" type="text" class="form-control" value="<%=mybean.doc_duedays%>" maxlength="3"  size="10">
					</div>
				</div>
    <div class="form-group">
			<label class="control-label col-md-4">Effective From<font color="#ff0000">*</font>:</label>
					<div class="col-md-6">
						<select id="dr_effective" name="dr_effective" class="form-control">                  
						<%= mybean.PopulateEffective()%>
              </select>
					</div>
				</div>
           <%if(mybean.status.equals("Update") && !mybean.entry_by.equals("")){ %>
            <div class="form-group">
			<label class="control-label col-md-4">Entry By:</label>
					<div class="txt-align">
						<%=mybean.entry_by%>
                <input name="entry_by" type="hidden" id="entry_by" value="<%=mybean.entry_by%>">
					</div>
				</div>
				<div class="form-group">
			<label class="control-label col-md-4">Entry Time:</label>
					<div class="txt-align">
						<%=mybean.entry_date%>
                <input name="entry_date" type="hidden" id="entry_date" value="<%=mybean.entry_date%>">
					</div>
				</div>
        
            <%}%>
            <%if(mybean.status.equals("Update") && !mybean.modified_by.equals("")){ %>
            <div class="form-group">
			<label class="control-label col-md-4">Modified By:</label>
					<div class="txt-align">
						<%=mybean.modified_by%>
                <input name="modified_by" type="hidden" id="modified_by" value="<%=mybean.modified_by%>">
					</div>
				</div>
             <div class="form-group">
			<label class="control-label col-md-4">Modified Time:</label>
					<div class="txt-align">
						<%=mybean.modified_date%>
                <input name="modified_date" type="hidden" id="modified_date" value="<%=mybean.modified_date%>">
					</div>
				</div>
            <%}%>
                <%if(mybean.status.equals("Add")) {%>
                <center>
                <input type="submit" name="addbutton" id="addbutton" class="btn btn-success" value="Add Document" onClick="return SubmitFormOnce(document.formcontact, this);"/>
                <input type="hidden" name="add_button" value="yes"/>
                 </center>
                <% } if(mybean.status.equals("Update")) {%>
                 <center>
                <input type="submit" name="updatebutton" id="updatebutton" class="btn btn-success" value="Update Document" onClick="return SubmitFormOnce(document.formcontact, this);"/>
                 <input type="hidden" name="update_button" value="yes">
                <input type="submit" name="delete_button" id="delete_button" class="btn btn-success" OnClick="return confirmdelete(this)" value="Delete Document"/>
               </center>
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
	<!-- END CONTAINER -->

	<%@include file="../Library/admin-footer.jsp"%>
	
	<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript" src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
<script type="text/javascript" src="../ckeditor/ckeditor.js"></script>
<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript" src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
<script type="text/javascript" src="../ckeditor/ckeditor.js"></script>
</body>
    </HTML>
  