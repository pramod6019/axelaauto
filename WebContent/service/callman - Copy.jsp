<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Callman" scope="request"/>
<%mybean.doGet(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"/>
        <HEAD>
        <title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
        <meta http-equiv="pragma" content="no-cache"/>
        <meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt"/>
        <link rel="shortcut icon"  type="image/x-icon" href="../admin-ifx/axela.ico">     
        
        <script type="text/javascript" src="../Library/Validate.js"></script>
        <script type="text/javascript" src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
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
        
        
<script language="JavaScript" type="text/javascript">
		
		function OnFocus(id){
			if(id.value=='Search') { 
			id.value = ''; 
			}
			}
			
		function OnBlur(id){
			if(id.value=='') { 
			id.value = 'Search'; 
			}
			}
			
//			function PopulateVehicleDash(veh_id){
//			showHint('../service/vehicle-dash - Copy.jsp?veh_id='+veh_id+'&modal=yes','div_callman');
//				}
		
		function CallSearch(){
			var search_param = document.getElementById("dr_search_param").value;
			var branch_id = document.getElementById("dr_branch_id").value;
			var emp_id = document.getElementById("dr_emp_id").value;
			var txt_search = '';
			var para = '';
			var search_id = document.getElementById("dr_search_id").value;
			//alert("search_id=="+search_id);
			if(search_id=='1'){
					document.getElementById("span_branch").style.display = '';
					document.getElementById("span_emp").style.display = '';
					document.getElementById("span_dr_search").style.display = '';
				txt_search = '';
				para = 'overdue';
				}else if(search_id=='2'){
					document.getElementById("span_branch").style.display = '';
					document.getElementById("span_emp").style.display = '';
					document.getElementById("span_dr_search").style.display = '';
					txt_search=document.getElementById("txt_search").value;
					para='date';
					}else if(search_id=='3'){
					document.getElementById("span_branch").style.display = 'none';
					document.getElementById("span_emp").style.display = 'none';
					document.getElementById("span_dr_search").style.display = 'none';
				    document.getElementById("txt_search").focus();
					txt_search=document.getElementById("txt_search").value;
					para='vehicle';
					}else if(search_id=='4'){
					document.getElementById("span_branch").style.display = 'none';
					document.getElementById("span_emp").style.display = 'none';
					document.getElementById("span_dr_search").style.display = 'none';
				    document.getElementById("txt_search").focus();
					txt_search=document.getElementById("txt_search").value;
					para='contact';
					}else if(search_id=='5'){
					document.getElementById("span_branch").style.display = '';
					document.getElementById("span_emp").style.display = 'none';
					document.getElementById("span_dr_search").style.display = 'none';
				    document.getElementById("txt_search").focus();
					txt_search='';
					para='service_due';
					}
			showHint("callman-check.jsp?branch_id="+branch_id+"&emp_id="+emp_id+"&txt_search="+txt_search+"&para="+para+"&search_param="+search_param,"div_callman");
			}
			
			
		function PopulateSearchBox(){
			var branch_id=document.getElementById("dr_branch_id").value;
			var emp_id = document.getElementById("dr_emp_id").value;
			var dr_search = document.getElementById("dr_search_id").value;
			//alert("dr_search=="+dr_search);
			if(dr_search==1){
				document.getElementById("span_search").innerHTML = "";
			}else if(dr_search==2){
				document.getElementById("span_search").innerHTML = "<input name='txt_search' type='text' class='textbox'  id ='txt_search' size='20' maxlength='16' onchange='CallSearch();' value='<%=mybean.strToShortDate(mybean.ToLongDate(mybean.kknow()))%>'/>";
				
				$(function() {
                              $( "#txt_search" ).datepicker({
                              showButtonPanel: true,
                              dateFormat: "dd/mm/yy"
                             });
	            });
				
				}else if(dr_search==3 || dr_search==4 || dr_search==5){
				document.getElementById("span_search").innerHTML = "<input name ='txt_search' type='text' class='textbox' id='txt_search' size='25' maxlength='255' onKeyUp='CallSearch();' value='' onfocus='OnFocus(this.id);' onBlur='OnBlur(this.id);'/>";
				//document.getElementById("txt_search").focus();
				}
				CallSearch();
                }
				
				function GetVehicleDetail(veh_id){
					showHint("callman-check.jsp?veh_id="+veh_id+"&veh_details=yes","div_callman");
					}
					
					
</script>

<script type="text/javascript">
$(function() {
$( "#tabs" ).tabs({
event: "mouseover"
});
});
</script>

<script language="JavaScript" type="text/javascript">
    function PopulateVehicleDash(veh_id){
        showHint('../service/vehicle-dash - Copy.jsp?veh_id='+veh_id+'&modal=yes','div_callman');
    }

</script>
                                
<script type="text/javascript">
    $(document.ready(function() {
        $( "#tabs" ).tabs();
    });
</script>
        <link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
        <body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
        <%@include file="../portal/header.jsp" %>
        <TABLE width="98%" height="300" border="0" align="center" cellPadding="0" cellSpacing="0">
          <TR>
            <TD align="left"><a href="../portal/home.jsp">Home</a> &gt; <a href="../service/index.jsp">Service</a> &gt; <a href="callman.jsp">Call Manager</a>:</TD>
          </TR>
          <TR>
            <TD align="center" vAlign="top"><font color="#ff0000" ><b><%=mybean.msg%></b></font> <br/></TD>
          </TR>
<tr>
            <td align="right" valign="top"><form name="form1"  method="post">
                    <table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="listtable">
                    <tr>
                        <th align="center">Call Manager</th>
                      </tr>
                    <tr>
                        <td align="center"><span id="span_branch" style="display:none">
                        <select name="dr_branch_id" class="selectbox" id="dr_branch_id" onChange="CallSearch();">
                            <%=mybean.PopulateBranch(mybean.branch_id, "", request)%>
                          </select>
                        &nbsp;&nbsp;&nbsp;
                        </span>
                  <span id="span_emp" style="display:none">
                  <select name="dr_emp_id" class="selectbox" id="dr_emp_id" onchange="CallSearch();"   >
                    <%=mybean.PopulateExecutive()%>
                  </select>
                   &nbsp;&nbsp;&nbsp;
                   </span>
                   <span id="span_search" name="span_search"><input name ="txt_search" type="text" class="textbox" id="txt_search" size="25" maxlength="255" onKeyUp="CallSearch();" value="" onfocus="OnFocus(this.id);" onBlur="OnBlur(this.id);"/></span>
<select name="dr_search_id" class="selectbox" id="dr_search_id" onchange="PopulateSearchBox();">
                    <%=mybean.PopulateSearch()%>
                          </select>
                          &nbsp;&nbsp;&nbsp;
                          <span id="span_dr_search" style="display:none">
<select name="dr_search_param" class="selectbox" id="dr_search_param" onchange="CallSearch();"  >
                    <%=mybean.PopulateSearchParam()%>
                          </select>
                          </span>
                          </td>
                      </tr>
                  </table>
                  </form></td>
                  </tr>
                   <tr>
                      <td align="center" valign="top">&nbsp;</td>
                    </tr>
                    <tr>
                      <td align="center" valign="top" bgcolor="#FFFFFF" height="300">
                      <div id="div_callman"><%=mybean.StrHTML%></div></td>
                    </tr>
        </TABLE> <%@include file="../Library/admin-footer.jsp" %></body>
</HTML>
