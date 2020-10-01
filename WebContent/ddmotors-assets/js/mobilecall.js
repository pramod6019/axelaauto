

function showToast(msg) {
	if (msg != '') {
//	   alert('showToast===' + msg)
		location.href = 'showtoast' + msg;
	}
	return;
}

function callURL(url) {
	//			alert(url);
	location.href = 'callurl' + url;
}

function callNo(number) {
	//			alert(number)
	location.href = 'callno' + number;
}

function sendMail(mail_id) {
	//			alert(mail_id);
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

//function showDialog(test){
////	alert(result);
//	
//}