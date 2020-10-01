<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="mybean" class="axela.home.Enquiry_Add" scope="request"/>
<%mybean.doPost(request,response); %>
<%response.setHeader("Access-Control-Allow-Origin","*"); %>
<html>
<head>


</head> 
<body>
<!--"http://ddmotors.axelaauto.com/axelaauto-ddmotors/home/enquiry-add.jsp"-->
<!--"http://localhost:7040/axelaauto-ddmotors/home/enquiry-add.jsp" -->
<!--"http://localhost:6061/axelaauto-ddmotors/home/enquiry-add.jsp" (JEET)-->
<%
	String url = "";
    if(mybean.AppRun().equals("1")) {
		url = "http://ddmotors.axelaauto.com/axelaauto/home/enquiry-add.jsp";
	} else {
		url = "http://localhost:8030/axelaauto/home/enquiry-add.jsp";
	}
%>
<font color="#ff0000" ><b><%=mybean.msg%></b></font>
<form  name="buycarfrm" novalidate="novalidate"  method="get"  id="buycarfrm" action="<%=url%>">
			<input name="cmd" id="cmd" value="52a857df4fe2638c234387793eef5e31" type="hidden">
			<h5>Title<span style="color:red">*</span>:</h5><select name="dr_contact_title_id" size="1" class="dd" id="title" value="<%=mybean.contact_title_id%>"><%=mybean.enquiry.PopulateTitle(mybean.contact_title_id,mybean.comp_id)%>  
                    </select><span style="color:red;font-weight: bold;" id="err0"></span><br>
            <h5>First Name<span style="color:red">*</span>:</h5><!--placeholder="First Name" -->
           <input name="txt_contact_fname" class="txbx " id="fname" type="text" value="<%=mybean.contact_fname%>" onKeyPress="return onlyAlphabets(event);"/><span style="color:red;font-weight: bold;" id="err1"></span><br>
            <h5>Last Name:</h5><input name="txt_contact_lname" class="txbx " id="lname" type="text" value="<%=mybean.contact_lname%>" onKeyPress="return onlyAlphabets(event);" /><br>
		       <h5>Email<span style="color:red">*</span>:</h5>
				<input name="txt_contact_email1" class="txbx " id="email" type="text" value="<%=mybean.contact_email1%>"
                /> <span style="color:red;font-weight: bold;" id="err2"></span><br>
			   <h5>Mobile No<span style="color:red">*</span>:</h5>
               <input name="txt_contact_mobile1" class="txbx" id="mobile" type="text" value="<%=mybean.contact_mobile1%>"  maxlength = "10" onKeyPress="return valMobile(event);" ><span style="color:red;font-weight: bold;" id="err3"></span><br>
                
                <%-- <h5>Phone No:</h5>
             <input name="txt_contact_phone1" class="txbx" id="phone" type="text" value="<%=mybean.contact_phone1%>" onKeyPress="return valPhone(event);" maxlength = "12"><span style="color:red;font-weight: bold;" id="err-phone"></span><br>				 --%>
				<h5>Model:<span style="color:red">*</span></h5>
				<select name="dr_enquiry_model_id" size="1" class="dd" id="model" value="<%=mybean.enquiry_model_id%>">
					<%=mybean.enquiry.PopulateModel(mybean.enquiry_model_id, mybean.comp_id)%>              
                     </select><span style="color:red;font-weight: bold;" id="err4"></span><br>
<!-- 				<h5>Choose Location<span style="color:red">*</span>:</h5> -->
<!-- 				 <select name="dr_enquiry_branch_id" id="txt_location" class="dd" > -->
<%-- 				      <%=mybean.PopulateBranch(mybean.enquiry_branch_id, "", request)%> --%>
<!-- 				     </select><span style="color:red;font-weight: bold;" id="err5"></span><br>	 -->
				<h5>Note:</h5>
		        <textarea name="txt_enquiry_desc" cols="30" rows="3" class="txbx" id="note" value="<%=mybean.enquiry_desc%>" ></textarea>			
<h2>Verify your Entry</h2>
			<ul>
				<li>Please fill in the result of below numbers to verify your entry.</li>
				<li class="verify-field"><h5>Verification Code<span style="color:red">*</span>:</h5></li>
                <table><tr>
                      <td>
                       <img src="<%=mybean.urlpath%>/Captcha.do" ></td>
                    </tr>
                    <tr>
                      <td ><input name="code" type="text" class="txbx" id="code"  value="" maxlength="10" width="150">
                      </td>
                    </tr>
                    <tr>
                      <td align="left">Letters are not case-sensitive<br>
                      <span style="color:red;font-weight: bold;" id="err6"></span></td>
                    </tr>
                    <tr></table>
                    
                    
                </ul>
                
                
<ul>
				<li class="bt-row"><br><label>&nbsp;</label>
					<span class="note-text ">
		<!--					<a href=""><img src="../images/submit.jpg" width="97" height="30" alt="Submit" title="Submit" value="submit" type="image"></a> -->
        <input type="hidden" name="submitb" value="yes">
        <input src="../images/submit.jpg" alt="Submit" title="Submit" name="submitb" type="image" id="submitb" value="Submit" onClick="return validateform();"/>
         
                         <!-- <input src="../images/submit.jpg" alt="Submit" title="Submit" id="submit" name="submit" value="submit" type="image" onClick="return validateform();" >  &nbsp; &nbsp; -->
							<a href="javascript:document.buycarfrm.reset()"><img src="../images/reset.jpg" alt="Reset"></a>
					</span> 
				</li>
			</ul>
            
            </form>
            </body>
</html>
