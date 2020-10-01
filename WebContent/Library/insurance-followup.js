function InsurContactableBand(){
 //alert("ContactableBand");
	var contactable = $("#dr_insurfollowup_contactable_id").val();
	var disposition = $("#dr_disposition_id").val();
	if(contactable == 0){
		$("#disposition").hide();
		$("#inspection").hide();
		$("#inspection_result").hide();
		$("#offer").hide();
		$("#appointment").hide();
		$("#insurnextfollowuptime").hide();
		$("#insurnextfollowuptype").hide();
		$("#fieldexe").hide();
		//$("#insurremarks").hide();
		$("#notinserestedreason").hide();
		$("#dr_disposition_id").val("0");
		$("#dr_inspection_id").val("0");
		$("#dr_inspection_result_id").val("0");
		////$("#dr_offer_id").val("0");
		$("#dr_appoint_verification_id").val("0");
		$("#txt_insurfollowup_time").val("");
		$("#dr_insurfollowup_type").val("0");
		$("#dr_field_emp_id").val("0");
		$("#txt_insurfollowup_desc").val("");
	}else{
		$("#disposition").show();
	}
}

function DispositionBand(){
 	//alert("DispositionBand");
	var contactable = $("#dr_insurfollowup_contactable_id").val();
	var disposition = $("#dr_disposition_id").val();
// 	//alert("disposition111=="+disposition);
	if(disposition == 0){
		$("#notinserestedreason").hide();
		$("#inspection").hide();
		$("#inspection_result").hide();
		$("#offer").hide();
		$("#appointment").hide();
		$("#insurnextfollowuptime").hide();
		$("#insurnextfollowuptype").hide();
		$("#fieldexe").hide();
		//$("#insurremarks").hide();
		$("#dr_inspection_id").val("0");
		$("#dr_inspection_result_id").val("0");
		////$("#dr_offer_id").val("0");
		$("#dr_appoint_verification_id").val("0");
		$("#txt_insurfollowup_time").val("");
		$("#dr_insurfollowup_type").val("0");
		$("#dr_field_emp_id").val("0");
		$("#txt_insurfollowup_desc").val("");
	}else if(contactable==1 && disposition==4){
		$("#appointment").hide();
		$("#fieldexe").hide();
		$("#inspection").show();
		$("#offer").show();
		$("#insurnextfollowuptime").show();
		$("#insurnextfollowuptype").show();
		$("#insurremarks").show();
	}else if(contactable==1 && disposition==5){
		$("#fieldexe").hide();
		$("#notinserestedreason").show();
		$("#insurnextfollowuptime").show();
		$("#insurnextfollowuptype").show();
		$("#insurremarks").show();
	}else if(contactable==2 && disposition==11){
		$("#offer").hide();
		$("#inspection").hide();
		$("#inspection_result").hide();
		$("#appointment").hide();
		$("#insurnextfollowuptime").hide();
		$("#insurnextfollowuptype").hide();
		$("#fieldexe").hide();
		$("#notinserestedreason").hide();
		$("#insurremarks").show();
	}else if(contactable==1 && disposition == 10){
// 		//alert("disposition=="+disposition);
		$("#inspection").hide();
		$("#notinserestedreason").hide();
		$("#inspection_result").hide();
		$("#fieldexe").hide();
		$("#appointment").show();
		$("#insurnextfollowuptime").show();
		$("#insurnextfollowuptype").show();
		$("#insurremarks").show();
	}else if(contactable==1 && disposition==12){
		$("#insurnextfollowuptime").show();
		$("#insurnextfollowuptype").show();
		$("#fieldexe").show();
		$("#insurremarks").show();
	}else{
		$("#inspection").hide();
		$("#notinserestedreason").hide();
		$("#inspection_result").hide();
		$("#appointment").hide();
		$("#fieldexe").hide();
		$("#insurnextfollowuptime").show();
		$("#insurnextfollowuptype").show();
		$("#insurremarks").show();
	}
	
	if(disposition == 5){
		$("#type").hide();
		$("#reason2").hide();
		$("#reason1").show();
		$("#type1").show();
	}else{
		$("#reason2").show();
		$("#reason1").hide();
		$("#type").show();
		$("#type1").hide();
	}
}

function InspectionBand(){
 	//alert("InspectionBand");
	var inspection = $("#dr_inspection_id").val();
	if(inspection == 0){
		$("#offer").hide();
		$("#fieldexe").hide();
		$("#inspection_result").hide();
		$("#appointment").hide();
		$("#insurnextfollowuptime").hide();
		$("#insurnextfollowuptype").hide();
		//$("#insurremarks").hide();
		$("#dr_inspection_result_id").val("0");
		//$("#dr_offer_id").val("0");
		$("#dr_appoint_verification_id").val("0");
		$("#txt_insurfollowup_time").val("");
		$("#dr_insurfollowup_type").val("0");
		$("#dr_field_emp_id").val("0");
		$("#txt_insurfollowup_desc").val("");
	}else if(inspection == 1){
		$("#inspection_result").show();
		$("#offer").show();
		$("#insurnextfollowuptime").show();
		$("#insurnextfollowuptype").show();
		$("#insurremarks").show();
	}else{
		$("#offer").show();
		$("#fieldexe").hide();
		$("#notinserestedreason").hide();
		$("#inspection_result").hide();
		$("#appointment").hide();
		$("#insurnextfollowuptime").show();
		$("#insurnextfollowuptype").show();
		//$("#insurremarks").hide();
		$("#dr_inspection_result_id").val("0");
		//$("#dr_offer_id").val("0");
		$("#dr_appoint_verification_id").val("0");
		$("#txt_insurfollowup_time").val("");
		$("#dr_insurfollowup_type").val("0");
		$("#dr_field_emp_id").val("0");
		$("#txt_insurfollowup_desc").val("");
	}
}

function InspectionResultBand(){
 	//alert("InspectionResultBand");
	var inspectionResult = $("#dr_inspection_result_id").val();
	if(inspectionResult == 0){
		$("#offer").hide();
		$("#notinserestedreason").hide();
		$("#fieldexe").hide();
		$("#appointment").hide();
		$("#insurnextfollowuptime").hide();
		$("#insurnextfollowuptype").hide();
		//$("#insurremarks").hide();
		//$("#dr_offer_id").val("0");
		$("#dr_appoint_verification_id").val("0");
		$("#txt_insurfollowup_time").val("");
		$("#dr_insurfollowup_type").val("0");
		$("#dr_field_emp_id").val("0");
		$("#txt_insurfollowup_desc").val("");
	}else{
		$("#offer").show();
	}
}

function OfferBand(){
/// 	//alert("OfferBand");
	var offer = $("#dr_offer_id").val();
	var disposition = $("#dr_disposition_id").val();
	if(offer == 0){
		$("#fieldexe").hide();
		$("#notinserestedreason").hide();
		$("#appointment").hide();
		$("#insurnextfollowuptime").hide();
		$("#insurnextfollowuptype").hide();
		//$("#insurremarks").hide();
		$("#dr_appoint_verification_id").val("0");
		$("#txt_insurfollowup_time").val("");
		$("#dr_insurfollowup_type").val("0");
		$("#dr_field_emp_id").val("0");
		$("#txt_insurfollowup_desc").val("");
	}else if(disposition == 4){
		$("#appointment").hide();
	}else{
		$("#appointment").hide();
	}
}

function AppVerificationBand(){
 	//alert("AppVerificationBand");
	var appVerification = $("#dr_appoint_verification_id").val();
	if(appVerification == 0){
		$("#fieldexe").hide();
		$("#insurnextfollowuptime").hide();
		$("#insurnextfollowuptype").hide();
		//$("#insurremarks").hide();
		$("#txt_insurfollowup_time").val("");
		$("#dr_insurfollowup_type").val("0");
		$("#dr_field_emp_id").val("0");
		$("#txt_insurfollowup_desc").val("");
	}else{
		$("#insurnextfollowuptime").show();
	}
}

function NextFollowupTimeBand(){
 	//alert("NextFollowupTimeBand");
	var insurnextfollowuptime = $("#txt_insurfollowup_time").val();
	var disposition = $("#dr_disposition_id").val();
	
	if(insurnextfollowuptime == 0){
		$("#fieldexe").hide();
		$("#insurnextfollowuptype").hide();
		//$("#insurremarks").hide();
		$("#dr_insurfollowup_type").val("0");
		$("#dr_field_emp_id").val("0");
		$("#txt_insurfollowup_desc").val("");
	}else{
		$("#insurnextfollowuptype").show();
	}
}

function NextFollowupTypeBand(){
 	//alert("NextFollowupTypeBand");
var disposition = $("#dr_disposition_id").val();
	var insurnextfollowuptype = $("#dr_insurfollowup_type").val();
	if(insurnextfollowuptype == 0){
		$("#fieldexe").hide();
//		$("#insurremarks").hide();
		$("#dr_field_emp_id").val("0");
		$("#txt_insurfollowup_desc").val("");
	}else if(insurnextfollowuptype == 3 && disposition == 12){
		$("#fieldexe").show();
		$("#insurremarks").show();
	}else{
		$("#fieldexe").hide();
		$("#dr_field_emp_id").val("0");
		$("#insurremarks").show();
	}
}

function FieldExeBand(){
 	//alert("FieldExeBand");
	var fieldExe = $("#dr_field_emp_id").val();
	if(fieldExe == 0){
		//$("#insurremarks").hide();
		$("#txt_insurfollowup_desc").val("");
	}else{
		$("#insurremarks").show();
	}
}

	function PopulateDisposition() {
		var contactable = $("#dr_insurfollowup_contactable_id").val();
		showHint('../insurance/mis-check.jsp?listdisposition=yes&contactable_id=' + contactable, 'HintDisposition');
	}
	
	function InsurContactableValidation(){
// 		//alert("ContactableValidation");
		var contactable = $("#dr_insurfollowup_contactable_id").val();
		
		if(contactable == 0){
			////alert("Contactable==0");
			$("#insurerrormsg").html("<b> Select Contactable!").css("color", "red");
			$("#insurerrormsg").show();
			$("#dispositionerrormsg").hide();
			$("#inspectionerrormsg").hide();
			$("#insurnextfollowerrormsg").hide();
			$("#insurnextfollowtypeerrormsg").hide();
			return false;
		}else{
			////alert("else");
			$("#insurerrormsg").hide();
			return true;
		}
	}
	
	function DispositionValidation(){
 		//alert("DispositionValidation");
		var contactable = $("#dr_insurfollowup_contactable_id").val();
		if(contactable==0){
			return false;
		}else{
			var disposition = $("#dr_disposition_id").val();
			
			if(disposition == 0){
	// 			//////alert("Disposition==0");
				$("#dispositionerrormsg").html("<b> Select Disposition!").css("color", "red");
				if(contactable != 0){
					$("#dispositionerrormsg").show();
				}
				$("#inspectionerrormsg").hide();
				$("#insurnextfollowerrormsg").hide();
				$("#insurnextfollowtypeerrormsg").hide();
				return false;
			}else{
	// 			////////alert("Disposition!=0");
				$("#dispositionerrormsg").hide();
				return true;
			}
		}
	}
	
	function InspectionValidation(){
 		//alert("InspectionValidation");
		var disposition = $("#dr_disposition_id").val();
		if(disposition==0){
			return false;
		}else{
		var inspection = $("#dr_inspection_id").val();
// 		//////alert("disposition =="+disposition);
		if(disposition == 4){
			if(inspection==0){
// 				//////alert("Inspection==0");
				$("#inspectionerrormsg").html("<b> Select Inspection!").css("color", "red");
				if(disposition != 0){
					$("#inspectionerrormsg").show();
				}
				$("#insurnextfollowerrormsg").hide();
				$("#insurnextfollowtypeerrormsg").hide();
				return false;
			}else{
				$("#inspectionerrormsg").hide();
				return true;
			}
		}
		else{
			return true;
		}}
	}
	
	function NextFollowupTimeValidation() {
 		//alert("NextFollowupTimeValidation");
		var inspection = $("#dr_inspection_id").val();
		var disposition = $("#dr_disposition_id").val();
		var insurnextfollowuptime = $("#txt_insurfollowup_time").val();
		var currtime = $("#currenttime").val();
		var tempfollwuptime = insurnextfollowuptime.replace("/","").replace("/","").replace(":","").replace(" ","");
		currtime  = parseInt(currtime);
		tempfollwuptime = tempfollwuptime.substr(4,4)+tempfollwuptime.substr(2,2)+tempfollwuptime.substr(0,2)+tempfollwuptime.substr(8,4)+"00";
//		alert('tempfollwuptime=='+tempfollwuptime);
//		alert('currtime=='+currtime);
		tempfollwuptime = parseInt(tempfollwuptime);
		if(inspection==0 && disposition == 4){
// 			//alert("Inside");
			return false;
		}else{
			var inspection = $("#dr_inspection_id").val();
			var insurnextfollowuptime = $("#txt_insurfollowup_time").val();
// 	        //alert("disposition==121===="+disposition);

	        if(disposition !=5 && disposition !=11 && insurnextfollowuptime == ''){
// 	         	//alert("disposition1==="+disposition);
	        	$("#insurnextfollowerrormsg").html("<b> Select Next Follow-Up Time!").css("color", "red");
// 	        	//alert('inspection=='+inspection);
				if(disposition != 0){
					$("#insurnextfollowuptime").show();
					$("#insurnextfollowerrormsg").show();
				}
				$("#insurnextfollowtypeerrormsg").hide();
				return false;
	        }else if(insurnextfollowuptime != ''){
// 	        	//alert(111);
				if(tempfollwuptime < currtime){
//					alert('coming..');
					$("#insurnextfollowerrormsg").html("<b> Next Follow-up Time should be greater than Today's Date and Time!").css("color", "red");
					$("#insurnextfollowerrormsg").show();
					return false;
				}
				else return true;
	        }else if(disposition == 5  && insurnextfollowuptime == ''){
// 	        	//alert(222);
	        	$("#insurnextfollowerrormsg").hide();
				return true;
	        }
	        else{
// 	        	//alert(333);
	        	$("#insurnextfollowerrormsg").hide();
				return true;
	        }
		}
	}
	
	function NextFollowupTypeValidation() {
 		//alert("NextFollowupTypeValidation");
		var insurnextfollowuptime = $("#txt_insurfollowup_time").val();
		var disposition = $("#dr_disposition_id").val();
		var insurnextfollowuptime = $("#txt_insurfollowup_time").val();
		var currtime = $("#currenttime").val();
		var tempfollwuptime = insurnextfollowuptime.replace("/","").replace("/","").replace(":","").replace(" ","");
		tempfollwuptime = tempfollwuptime.substr(4,4)+tempfollwuptime.substr(2,2)+tempfollwuptime.substr(0,2)+tempfollwuptime.substr(8,4)+"00";
		currtime  = parseInt(currtime);
		tempfollwuptime = parseInt(tempfollwuptime);
		if(insurnextfollowuptime=='' && disposition != 5 && disposition !=11){
// 			//alert("insurnextfollowuptime---->>>"+insurnextfollowuptime);
			return false;
		}else if(tempfollwuptime < currtime){
// 			alert("zzzz");
			return false;
		}else{
		var disposition = $("#dr_disposition_id").val();
		var inspection = $("#dr_inspection_id").val();
		var insurnextfollowuptime = $("#txt_insurfollowup_time").val();
		var insurnextfollowuptype = $("#dr_insurfollowup_type").val();
//         //////alert(insurnextfollowuptime);
//         //////alert("disposition==121===="+disposition);
		 if(disposition !=5 && disposition !=11 && insurnextfollowuptype == 0){
//         	 //////alert("disposition==="+disposition);
        	$("#insurnextfollowtypeerrormsg").html("<b> Select Next Follow-Up Type!").css("color", "red");
// 			if(inspection != 0){
				$("#insurnextfollowuptype").show();
				$("#insurnextfollowtypeerrormsg").show();
// 			}
			return false;
        }
        else if(disposition == 5  && insurnextfollowuptype == 0){
//         	 //////alert("disposition==123==="+disposition);
        	$("#insurnextfollowtypeerrormsg").hide();
			return true;
        } else{
//         	 //////alert("disposition=124===="+disposition);
        	$("#insurnextfollowtypeerrormsg").hide();
			return true;
        }
       }
	}
	
	function ReasonValidation() {
		//alert("ReasonValidation()");
		var disposition = $("#dr_disposition_id").val();
		var reason = $("#dr_reason_id").val();
		if(disposition == 5 && reason == "0"){
			$("#notinserestedreasonerrormsg").html("<b> Select Reason!").css("color", "red");
			$("#notinserestedreasonerrormsg").show();
			return false;
		}else{
			$("#notinserestedreasonerrormsg").hide();
			return true;
		}
	}
	
	function RemarksValidation() {
		//alert("Remarksvalidation()");
		var insurremarks = $("#txt_insurfollowup_desc").val();
		insurremarks = insurremarks.trim();
		if(insurremarks == ""){
			$("#remarkserrormsg").html("</br><b> Remarks cannot be empty!").css("color", "red");
			$("#remarkserrormsg").show();
			return false;
		}else{
			$("#remarkserrormsg").hide();
			return true;
		}
	}
	
	function insurvalidation() {
		var temp = true;
		if (InsurContactableValidation() && temp) {
// 			alert('1==' + temp);
			temp = true;
		} else {
// 			alert('1.1==' + temp);
			temp = false;
		}

		if (DispositionValidation() && temp) {
// 			alert('2==' + temp); 
			temp = true;
		} else {
//			alert('2.1==' + temp);
			temp = false;
		}
		
		if (ReasonValidation() && temp) {
//			alert('92==' + temp);
			temp = true;
		} else {
//			alert('92.1==' + temp);
			temp = false;
		}

		if (InspectionValidation() && temp) {
//			alert('3==' + temp);
			temp = true;
		} else {
//			alert('3.1==' + temp);
			temp = false;
		}
		
		if (NextFollowupTimeValidation() && temp) {
//			alert('4==' + temp);
			temp = true;
		} else {
//			alert('4.1==' + temp);
			temp = false;
		}
		
		if (NextFollowupTypeValidation() && temp) {
//			alert('5==' + temp);
			temp = true;
		} else {
//			alert('5.1==' + temp);
			temp = false;
		}
		
		if (RemarksValidation() && temp) {
//			alert('6==' + temp);
			temp = true;
		} else {
//			alert('6.1==' + temp);
			temp = false;
		}
		
		return temp;
	}
	
