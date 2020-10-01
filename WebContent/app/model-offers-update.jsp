<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.app.Model_Offers_Update" scope="request"/>
<%mybean.doGet(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.ClientName%></title>
<meta http-equiv="pragma" content="no-cache"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt"/>

<%@include file="../Library/css.jsp"%>

<link href="../assets/css/bootstrap-wysihtml5.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/summernote.css" rel="stylesheet" type="text/css" />

</HEAD>

<body onLoad="FormFocus();" class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1><%=mybean.status%>&nbsp;Offer
						</h1>
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
						<li><a href="../portal/home.jsp">Home</a> &gt; Offer &gt;</li>
						<li><a href="model-offers-list.jsp?all=yes">List Offers</a> &gt;</li>
						<li><a href="model-offers-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%> Offers</a>:</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>

							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%>&nbsp;Offer
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="form1" method="post" class="form-horizontal">
										
										
											<div class="form-element6 form-element">
											
												<div class="form-element12">
													<label>Topic<font color="#ff0000">*</font>: </label>
													<input name="txt_offers_topic" type="text" class="form-control" id="txt_offers_topic"
														value="<%=mybean.offers_topic%>" size="70" maxlength="255">
												</div>
												
												<div class="form-element12">
													<label>Date<font color="#ff0000">*</font>: </label>
													<input name="txt_offers_date" type="text" class="form-control datepicker" id="txt_offers_date"
														value="<%=mybean.offers_date%>" size="14" maxlength="255" />
													<br><font color="#ff0000"><span id="txtHint"></span></font>
												</div>
	
												<div class="form-element12">
													<label>Type<font color="#ff0000">*</font>: </label>
													<div >
														<select name="dr_offers_offertype_id" id="dr_offers_offertype_id" class="form-control">
															<%=mybean.PopulateType()%>
														</select>
													</div>
	
												</div>
	
												<div class="form-element12">
													<label>Active:</label>
													<input id="chk_offers_active" type="checkbox" name="chk_offers_active"
														<%=mybean.PopulateCheck(mybean.offers_active)%> />
												</div>
											</div>
											
											
											<div class="form-element6">
												<label>Description<font color="#ff0000">*</font>: </label>
												<textarea name="txt_offers_desc" cols="70" rows="4" class="form-control summernote_1"
													id="txt_offers_desc"><%=mybean.offers_desc%></textarea>
											</div> 

											<%
												if (mybean.status.equals("Update") && !(mybean.entry_by == null) 
														&& !(mybean.entry_by.equals(""))) {
											%>
											<div class="form-element3">
												<label>Entry by:</label>
												<span><%=mybean.unescapehtml(mybean.entry_by)%>
													<input name="entry_by" type="hidden" id="entry_by" value="<%=mybean.entry_by%>"/>
												</span>
											</div>

											<div class="form-element3">
												<label>Entry Date:</label>
												<span><%=mybean.entry_date%>
													<input type="hidden" name="entry_date" value="<%=mybean.entry_date%>"/>
												</span>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.modified_by == null) 
														&& !(mybean.modified_by.equals(""))) {
											%>
											<div class="form-element3">
												<label>Modified by:</label>
												<span><%=mybean.unescapehtml(mybean.modified_by)%>
													<input name="modified_by" type="hidden" id="modified_by" value="<%=mybean.modified_by%>"/>
												</span>
											</div>
											<div class="form-element3">
												<label>Modified date:</label>
												<span><%=mybean.modified_date%>
													<input type="hidden" name="modified_date" value="<%=mybean.modified_date%>"/>
												</span>
											</div>
											<%
												}
											%>
											<div class="form-element12">
												<center>
													<%
														if (mybean.status.equals("Add")) {
													%>
													<input name="addbutton" type="submit" class="btn btn-success" id="addbutton" 
														value="Add Offer" onClick="onPress();return SubmitFormOnce(document.form1,this);" />
													<input type="hidden" name="add_button" value="yes"/>
													<%}else if (mybean.status.equals("Update")){%>
													<input type="hidden" name="update_button" value="yes"/>
													<input name="updatebutton" type="submit" class="btn btn-success" id="updatebutton"
														value="Update Offer" onClick="onPress();return SubmitFormOnce(document.form1,this);" />
													<input name="delete_button" type="submit" class="btn btn-success" id="delete_button"
														onClick="return confirmdelete(this)" value="Delete Offer" />
													<%}%>
												</center>
											</div>
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
	<%@ include file="../Library/admin-footer.jsp"%>

	<%@include file="../Library/js.jsp"%>

	<script src="../assets/js/summernote.min.js" type="text/javascript"></script>
	<script src="../assets/js/components-editors.min.js" type="text/javascript"></script>
	
	<script type="text/javascript">
		function FormFocus() { //v1.0
			document.formcontact.txt_customer_name.focus();
		}
	</script>

</body>
</HTML>
