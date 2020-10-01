<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Report_JobCard_ControlBoard1" scope="request"/>
<%mybean.doPost(request,response);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="shortcut icon"  type="image/x-icon" href="../admin-ifx/axela.ico">       

<link rel="stylesheet" type="text/css" href="../Library/qtip/jquery.qtip.css"/>
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
<script type="text/javascript" src="../Library/qtip/jquery.qtip.js"></script>
    <link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>

<script type="text/javascript">

$(document).ready(function() {
	$('.manhours').each(function(){
		$(this).qtip({
			content: {
				text: '<div id=loading align=center><img align=center src=\"../admin-ifx/loading.gif\" /></div>',
				ajax: {
                    url: '../service/report-check.jsp?manhours=yes&jc_id='+$(this).attr('id'),
                    type: 'POST',
                    data: 'jc_id='+$(this).attr('id'),
					success: function(data, status){
							return data;
					}
			  },
			},
			position: {
				target: 'mouse'
					  }
	 
		});
	});
	
});

</script> 
    
<script language="JavaScript" type="text/javascript">

    function BlinkFont(){
		document.getElementById("div_promised_time").style.color = "red";
		setTimeout("SetBlinkFont()", 1000);
		}
		
	function SetBlinkFont(){
		document.getElementById("div_promised_time").style.color = "white";
		setTimeout("BlinkFont()", 1000); 
		}
  
    function ExeTechnicianCheck() { //v1.0
	var branch_id=document.getElementById("dr_branch").value;
	showHint('../service/report-check.jsp?multiple=yes&status=yes&technician=yes&branch_id=' + GetReplace(branch_id),'technicianHint');
    }
  
    function ExeAdvisorCheck() { //v1.0
	var branch_id=document.getElementById("dr_branch").value;
	showHint('../service/report-check.jsp?multiple=yes&advisor=yes&branch_id=' + GetReplace(branch_id),'advisorHint');
    }	
	
	 function manhourdetails(jc_id){
		 //alert("gfgfggg");
                        //var jcontent = $('#summary');
                        //jcontent.html("");
                        $.ajax({
                            url: '../service/jc-check.jsp',
                            type: 'POST',
                            data: 'jc_id='+jc_id,
                            success: function (ajaxdata){
                                //jcontent.html(ajaxdata);
                                $('.tooltip').each(function(){
                                    $(this).qtip({
                                        content: $(this).next('.tip-content').html(),
                                        show:{
											solo: true,
											event: "mouseover",
                                            delay: 100
                                        },
                                        hide:{
                                            fixed: false
                                        },
                                        position: {
                                            my: 'top center',
                                            at: 'bottom center'
                                        }
                                    });
                                });
                            }
                        });
                    }
    </script>
    <body   leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0" <%if(mybean.msg.equals("")){%> onload="BlinkFont();"<%}%>>
<%@include file="../portal/header.jsp" %>
        <TABLE width="98%" border="0" align="center" cellPadding="0" cellSpacing="0" >
        <TR>
    <TD align="left" ><a href="../portal/home.jsp">Home</a> &gt; <a href="../portal/mis.jsp">MIS</a> &gt; <a href="report-jobcard-controlboard.jsp">Job Card Control Board</a>:</TD>
  </TR>
            <TR>
                <TD align="center" vAlign="top"><font color="#ff0000" ><b><%=mybean.msg%></b></font><br>
                </TD>
            </TR>
            
            
            
             <tr>
          <td align="center" valign="top"><form method="post" name="frm1"  id="frm1">
              <table width="100%" border="1" align="center" cellpadding="0" cellspacing="0"  class="tableborder">
                <tr>
                  <td>
                  <!--<div class="tooltip">
Unit12cvcvcv
</div> -->
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="listtable" >
                   <tr>
                        <th colspan="10" align="center">Job Card Control Board</th>
                      </tr>
                      <tr>
                        
                        <td align="right" valign="top">Branch<font color=red>*</font>:</td>
                        <td colspan="7" align="left" valign="top"><%if(mybean.branch_id.equals("0")){%>
                        <select name="dr_branch" id="dr_branch" class="selectbox" onChange="ExeTechnicianCheck(); ExeAdvisorCheck();">
                            <%=mybean.PopulateBranch(mybean.dr_branch_id,"",request)%>
                          </select>
                        <%}else{%>
                        <input type="hidden" name="dr_branch" id="dr_branch" value="<%=mybean.branch_id%>" />
                        <%=mybean.getBranchName(mybean.dr_branch_id, mybean.comp_id)%>
                        <%}%></td>
                        <td align="center" valign="top">
                         <%if(!mybean.jc_data.equals("")){%>
                         <a href="../service/jobcard-list.jsp?smart=yes">Export</a>
                         <%}%>&nbsp;
                         </td>
                         <td align="center" valign="top">
                         
                          <input name="submit_button" type="submit" class="button" id="submit_button" value="Go" />
                        <input type="hidden" name="submit_button" value="Submit"/></td>
                        </tr>  
                        <tr>
                         
                         <td align="right" valign="top">Stage:</td>
                        <td valign="top"><%=mybean.PopulateJobCardStage()%></td>
                        <td align="right" valign="top">Type:</td>
                        <td valign="top"><%=mybean.PopulateJobCardType()%></td>
                          <td align="right" valign="top">Advisor:</td>
                        <td valign="top"><span id="advisorHint"><%=mybean.reportexe.PopulateServiceAdvisors(mybean.dr_branch_id, mybean.advisorexe_ids, mybean.ExeAccess)%></span></td>
                        <td align="right" valign="top">Technician:</td>
                        <td valign="top"><span id="technicianHint"><%=mybean.reportexe.PopulateTechnicians(mybean.dr_branch_id, mybean.technicianexe_ids, mybean.ExeAccess)%></span></td>
                         <td align="right" valign="top">Model:</td>
                         <td valign="top"><select name="dr_model" size="10" multiple="multiple" class="selectbox" id="dr_model" style="width:130px">
                            <%=mybean.PopulateModel()%>
                          </select></td> 
                        </tr>
                         </table></td>
                </tr>
              </table>
            </form></td>
        </tr>
        <tr>
          <td>&nbsp;</td>  
        </tr>
        
        <tr>
          <td align="center" valign="top" height="200"><%=mybean.jc_data%></td>  
        </tr>
        </TABLE> <%@include file="../Library/admin-footer.jsp" %></body>
</HTML>
