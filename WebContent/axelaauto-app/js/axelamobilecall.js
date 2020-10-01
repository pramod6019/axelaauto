

function showToast(msg) {
	if (msg != '') {
		location.href = 'showtoast' + msg;
	}
	return;
}
///function showToast(StrHTML) {
//	if (value != '') {
//	   alert('showToast==========' + StrHTML)
//		location.href = 'showtoast' + StrHTML;
/////	}
//	return;
//}

function callURL(url) {
//			alert(url);
	location.href = 'callurl' + url;
}

function printPDF(url) {
location.href = 'printpdf' + url;
}

function imageURL(url) {
location.href = 'imageurl' + url;
}

function imageLINK(url) {
//	alert("url==1="+url);
location.href = 'imagelink' + url;
}


function emailURL(url) {
//	alert("url==1="+url);
location.href = 'emailurl' + url;
}

function callNo(number) {
//				alert(number)
	location.href = 'callno' + number;
}

function sendMail(mail_id) {
	location.href = 'sendmail' + mail_id;
}

function openURL(url) {
	//			alert(url);
	location.href = 'openurl' + url;
}

function openMap(showroom_latitude, showroom_longitude, showroom_name,
		showroom_address) {
	//			alert(showroom_latitude + ", " + showroom_longitude + ", "
	//					+ showroom_name + ", " + showroom_address);
	location.href = 'openmap' + showroom_latitude + "&$" + showroom_longitude
			+ "&$" +"\n"+ showroom_name + "" + showroom_address;
}

function showDialog(jsonarray, arrayname, arrayid, arrayvalue, title, inputid, callfunction){
//	alert(jsonarray);  
	Android.showDialog(jsonarray, arrayname, arrayid, arrayvalue, title, inputid, callfunction);
}
//function datePicker(url, date) {
//	alert("111111111111");
//		location.href = 'datepicker' + date + url;
//		}

function datePicker(url) {
		location.href = 'datepicker' + url;
		}
function timePicker(url) {
location.href = 'timepicker' + url;
}
