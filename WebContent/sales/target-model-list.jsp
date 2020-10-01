<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.sales.Target_Model_List" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp"%>
</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>List Target</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
		<div class="page-content">
			<div class="page-content-inner">
				<div class="container-fluid">
					<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
						<form name="form1" method="post">
							<tr>
								<td colspan="2">
									<a href="../portal/home.jsp">Home</a> &gt;
									<a href="index.jsp">Sales</a> &gt; <a href="target.jsp">Target</a> &gt;
									<a href="target-list.jsp?dr_executives=<%=mybean.emp_id%>&dr_year=<%=mybean.year%>">List Targets</a> &gt;
									<a href="target-list.jsp?dr_executives=<%=mybean.emp_id%>&dr_year=<%=mybean.year%>"><%=mybean.month_name%>-<%=mybean.year%></a> &gt;
									<a href="../portal/executive-list.jsp?emp_id=<%=mybean.emp_id%>"><%=mybean.emp_name%></a> &gt;
									<a href="target-model-list.jsp?<%=mybean.QueryString%>">Update Target</a><b>:</b>
								</td>
							</tr>
							<tr>
								<td colspan="2" align="center"><font color="#ff0000"><b><%=mybean.msg%></b></font><br></td>
							</tr>
							<tr>
								<td align="center">&nbsp;</td>
							</tr>
							<tr height="300px">
								<td valign="top" colspan="2" align="center"><%=mybean.StrHTML%></td>
							</tr>
							<tr>
								<td colspan="2" align="center">&nbsp;</td>
							</tr>
				 		</form>
					 </table>
				 </div>
	 		</div>
		</div>
	</div>
</div>
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
	<script type="text/javascript" >
	  function CalItemTotal(count){
		  var cnt=count;
		  var mdl_count = document.getElementById('model_count').value;  
		  var field_id="";
		  var sum_field_id="";
		  
		  if(cnt==1){				  
			  field_id='txt_count_'; 
			  sum_field_id='total_enquiry_count';
		  }
	      else if(cnt==2){
			  field_id='txt_call_'; 
			  sum_field_id='total_enquiry_calls_count';
		  }
		  else if(cnt==3){
			  field_id='txt_meeting_'; 
			  sum_field_id='total_enquiry_meetings_count';
		  }
		  else if(cnt==4){
			  field_id='txt_testdrive_'; 
			  sum_field_id='total_enquiry_testdrives_count';
		  }
		  else  if(cnt==5){
			  field_id='txt_hot_'; 
			  sum_field_id='total_enquiry_hot_count';
		  }
		  else  if(cnt==6){
			  field_id='txt_so_'; 
			  sum_field_id='total_so_count';
		  }
		  else  if(cnt==7){
			  field_id='txt_amt_'; 
			  sum_field_id='total_so_amount';
		  }
			
		  var sum=0;
		  for(var i=1;i<=mdl_count;i++){
			var xr=document.getElementById(field_id+i).value;
		    if(xr == 0 || xr == '' || isNaN(xr) == true || xr == null) {
            xr = 0;
            }
			sum=sum+parseInt(xr);
			  }
			   document.getElementById(sum_field_id).innerHTML =sum;
			}	  
</script>
</body>
</HTML>
