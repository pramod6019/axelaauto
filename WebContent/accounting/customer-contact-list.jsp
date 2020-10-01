<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.customer.Customer_Contact_List" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
                <HEAD>
                <title><%=mybean.AppName%></title>
                <meta name="viewport" content="width=device-width, initial-scale=1">
                <meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
                <meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
                <LINK REL="STYLESHEET" TYPE="text/css" HREF="../Library/theme<%=mybean.GetTheme(request)%>/jquery-ui.css">
                <LINK REL="STYLESHEET" TYPE="text/css" HREF="../Library/theme<%=mybean.GetTheme(request)%>/style.css">
                <link href="../Library/theme<%=mybean.GetTheme(request)%>/menu.css" rel="stylesheet" media="screen" type="text/css" />
                <link href="../Library/theme<%=mybean.GetTheme(request)%>/font-awesome.css" rel="stylesheet" media="screen" type="text/css" />
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
                <script type="text/javascript" src="../Library/Validate.js?target=<%=mybean.jsver%>"></script>
                <script type="text/javascript" src="../Library/smart.js"></script>
                <script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
                <script type="text/javascript">

function SelectQuoteContact(contact_id)
{
	
	window.parent.document.getElementById("span_cont_id").value=contact_id;
	window.parent.GetContactLocationDetails(contact_id);
}

function SelectSOContact(contact_id)
{
	window.parent.document.getElementById("span_cont_id").value=contact_id;
	window.parent.GetContactLocationDetails(contact_id);
}

function SelectInvoiceContact(contact_id)
{
	window.parent.document.getElementById("span_cont_id").value=contact_id;
	window.parent.GetContactLocationDetails(contact_id);
}

function SelectBillContact(contact_id, contact_name, customer_id, customer_name)
{
	window.parent.document.getElementById("span_cont_id").value=contact_id;
	window.parent.document.getElementById("span_acct_id").value=customer_id;
	window.parent.document.getElementById("span_bill_customer_id").innerHTML = "<a href=../customer/customer-list.jsp?customer_id=" + customer_id + " target = _blank><b>" + customer_name + "</b></a>";
	window.parent.document.getElementById("span_bill_contact_id").innerHTML = "<a href=../customer/customer-contact-list.jsp?contact_id=" + contact_id + " target = _blank><b>" + contact_name + "</b></a>";
}

function SelectSoContact(contact_id)
{
    window.parent.document.getElementById("hid_contact_id").value=contact_id;
    window.parent.GetContactLocationDetails(contact_id);
}
        </script>
                <SCRIPT language="JavaScript" type="text/javascript">
                 function onPress(){
					 }        
                </SCRIPT>
                <link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
                <body  <%if(mybean.advSearch.equals(null)  || mybean.advSearch.equals("")){%>onLoad="LoadRows();FormFocus();" <%}%>leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
                <%if(mybean.group.equals("")){%>
                <%@include file="../portal/header.jsp" %>
                <%}%><%@include file="../Library/list-body.jsp" %><%if(mybean.group.equals("")){%> <%@include file="../Library/admin-footer.jsp" %><%}%>
</body>
</HTML>
