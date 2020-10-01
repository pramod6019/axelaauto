function signinbanner() {
	/////I. Array of pictures that will randomly appear

var randompic = new Array ( );
randompic[0] = "../admin-ifx/signin-banner1.jpg";
randompic[1] = "../admin-ifx/signin-banner2.jpg";
randompic[2] = "../admin-ifx/signin-banner3.jpg";
//randompic[3] = "../admin-ifx/signin-banner4.jpg";
//randompic[4] = "../admin-ifx/signin-banner5.jpg";
//randompic[5] = "../admin-ifx/signin-banner6.jpg";
//randompic[6] = "../admin-ifx/signin-banner7.jpg";
//randompic[7] = "../admin-ifx/signin-banner8.jpg";
//randompic[8] = "../admin-ifx/signin-banner9.jpg";
//randompic[9] = "../admin-ifx/signin-banner10.jpg";
//randompic[10] = "../admin-ifx/signin-banner11.jpg";

//II. function to generate number from 0 to n

function randomzero (n)
{
  return ( Math.floor ( Math.random ( )*0.9999999999999999* (n + 1)) );
}

//III. assign any random number from 0 to 2 to x.

x = randomzero(2);

//IV. display the image 

document.write('<img alt="random image" src="' + randompic[x] + '"/>');
	
}

///////////////////////////////////////////////////////////////
////// Email validation Adopted from another website //////////
/////////////////////////////////////////////////////////////// 

function emailCheck(emailStr) {

    /* The following pattern is used to check if the entered e-mail address
   fits the user@domain format.  It also is used to separate the username
   from the domain. */
    var emailPat=/^(.+)@(.+)$/
    /* The following string represents the pattern for matching all special
   characters.  We don't want to allow special characters in the address. 
   These characters include ( ) < > @ , ;: \ " . [ ]    */
    var specialChars="\\(\\)<>@,;:\\\\\\\"\\.\\[\\]"
    /* The following string represents the range of characters allowed in a
   username or domainname.  It really states which chars aren't allowed. */
    var validChars="\[^\\s" + specialChars + "\]"
    /* The following pattern applies if the "user" is a quoted string (in
   which case, there are no rules about which characters are allowed
   and which aren't; anything goes).  E.g. "jiminy cricket"@disney.com
   is a legal e-mail address. */
    var quotedUser="(\"[^\"]*\")"
    /* The following pattern applies for domains that are IP addresses,
   rather than symbolic names.  E.g. joe@[123.124.233.4] is a legal
   e-mail address. NOTE: The square brackets are required. */
    var ipDomainPat=/^\[(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3})\]$/
    /* The following string represents an atom (basically a series of
   non-special characters.) */
    var atom=validChars + '+'
    /* The following string represents one word in the typical username.
   For example, in john.doe@somewhere.com, john and doe are words.
   Basically, a word is either an atom or quoted string. */
    var word="(" + atom + "|" + quotedUser + ")"
    // The following pattern describes the structure of the user
    var userPat=new RegExp("^" + word + "(\\." + word + ")*$")
    /* The following pattern describes the structure of a normal symbolic
   domain, as opposed to ipDomainPat, shown above. */
    var domainPat=new RegExp("^" + atom + "(\\." + atom +")*$")


    /* Finally, let's start trying to figure out if the supplied address is
   valid. */

    /* Begin with the coarse pattern to simply break up user@domain into
   different pieces that are easy to analyze. */
    var matchArray=emailStr.match(emailPat)
    if (matchArray==null) {
        /* Too many/few @'s or something; basically, this address doesn't
     even fit the general mould of a valid e-mail address. */
        //alert("Your Email address doesn't seems to be in proper format \""  +emailStr+ '" .')
        return false
    }
    var user=matchArray[1]
    var domain=matchArray[2]

    // See if "user" is valid
    if (user.match(userPat)==null) {
        // user is not valid
        //alert("The username doesn't seem to be valid. \""  +emailStr+ '"')
        return false
    }

    /* if the e-mail address is at an IP address (as opposed to a symbolic
   host name) make sure the IP address is valid. */
    var IPArray=domain.match(ipDomainPat)
    if (IPArray!=null) {
        // this is an IP address
        for (var i=1;i<=4;i++) {
            if (IPArray[i]>255) {
                //alert("Destination IP address is invalid! \""  +emailStr+ '"')
                return false
            }
        }
        return true
    }

    // Domain is symbolic name
    var domainArray=domain.match(domainPat)
    if (domainArray==null) {
        //alert("The domain name doesn't seem to be valid. \""  +emailStr+ '"')
        return false
    }

    /* domain name seems valid, but now make sure that it ends in a
   three-letter word (like com, edu, gov) or a two-letter word,
   representing country (uk, nl), and that there's a hostname preceding 
   the domain or country. */

    /* Now we need to break up the domain to get a count of how many atoms
   it consists of. */
    var atomPat=new RegExp(atom,"g")
    var domArr=domain.match(atomPat)
    var len=domArr.length
    if (domArr[domArr.length-1].length<2 ||
        domArr[domArr.length-1].length>4) {
        // the address must end in a two letter or three letter word.
        //alert("The address must end in a maximum of four-letter domain, or two letter country. \""  +emailStr+ '"')
        return false
    }

    // Make sure there's a host name preceding the domain.
    if (len<2) {
        var errStr="This address is missing a hostname! \""  +emailStr+ '"'
        //alert(errStr)
        return false
    }

    // If we've gotten this far, everything's valid!
    return true;
}
///////////////////////////////////////////////////////////////
////// TextArea characters count validation          //////////
///////////////////////////////////////////////////////////////
function GetObjID(ObjName)
{
    for (var ObjID=0; ObjID < document.all.length; ObjID++)
        if ( document.all[ObjID].name == ObjName )
        {
            return(ObjID);
            break;
        }
    return(-1);
}
function toPhone(textid, textname)
{	
    var textidObj=getObject(textid);
    var newString = "";		 // REVISED/CORRECTED STRING
    var count = 0;			 // COUNTER FOR LOOPING THROUGH STRING
    // LOOP THROUGH STRING
    for (i = 0; i < textidObj.value.length; i++) 
    {
        ch = textidObj.value.substring(i, i+1);  // CHECK EACH CHARACTER
        if ((ch >= "0" && ch <= "9") || (ch == "-")) 
        {
            newString += ch;
        }
    }
    textidObj.value = newString;
    return (false);
}

function SelectCompanyContact(id, name, indexid, compid, spanid)
{
    var contid=window.parent.document.getElementById("cont_id").value;	
    var spanidObj=window.parent.document.getElementById("span_prop_contact_id");
    var spancontid=window.parent.document.getElementById("span_cont_id");
    var compstr;
	
    if(contid==id)
    {
        spancontid.value=contid;
        id=contid;
    }
    else
    {
        spancontid.value=id;
        id=id;
    }
	
    compstr = "<a href=../account/account-contact-list.jsp?contact_id=" + id + " target = _blank><b>" + name + " ("+ id +")</b></a>";
		
    spanidObj.innerHTML = compstr;
    //alert(compid + " selected.");
	window.parent.$('#dialog-modal').dialog('close');
}

function SelectCompanyContact1(id, accid, accname, name, indexid, compid, spanid)
{
	
	var contid=window.parent.document.getElementById("cont_id").value;
	var acctid=window.parent.document.getElementById("acct_id").value;	
    var spanidObj=window.parent.document.getElementById("span_bill_contact_id");
    var spancontid=window.parent.document.getElementById("span_cont_id");
	var spanidObj1=window.parent.document.getElementById("span_bill_account_id");
    var spancontid1=window.parent.document.getElementById("span_acct_id");
    var compstr;
	
    if(contid==id)
    {
        spancontid.value=contid;
        id=contid;
    }
    else
    {
        spancontid.value=id;
        id=id;
    }
	if(acctid==accid)
    {
        spancontid1.value=acctid;
        accid=acctid;
    }
    else
    {
        spancontid1.value=accid;
        accid=accid;
    }
	
    compstr = "<a href=../account/account-contact-list.jsp?contact_id=" + id + " target = _blank><b>" + name + " ("+ id +")</b></a>";
	compstr1 = "<a href=../account/account-list.jsp?account_id=" + accid + " target = _blank><b>" + accname + " ("+ accid +")</b></a>";
		
    spanidObj.innerHTML = compstr;
	spanidObj1.innerHTML = compstr1;
    //alert(compid + " selected.");
	window.parent.$('#dialog-modal').dialog('close');
	 //window.close();
	
}

function SelectCompanyProperty(id, name, indexid, compid, spanid)
{  
   //alert("hiiiiiii");
    var accid=window.parent.document.getElementById("acc_id").value; 
    var spanidObj=window.parent.document.getElementById("span_prop_client_id");	
    var spanaccid=window.parent.document.getElementById("span_acc_id");
    var compstr;
    if(accid==id)
    {
        spanaccid.value=accid;
        id=accid;
    }
    else
    {
        spanaccid.value=id;
        id=id;
    }	
    compstr = "<a href=../account/account-list.jsp?account_id=" + id + " target = _blank><b>" + name + " (" + id + ")</b></a>";		
    spanidObj.innerHTML = compstr;
    //alert(compid + " selected.");
	window.parent.$('#dialog-modal').dialog('close');
}
//

function SelectAsset(id, name, indexid, compid, spanid)
{  
    var assid=window.parent.document.getElementById("asset_id").value;
    var spanidObj=window.parent.document.getElementById("span_issue_asset_id");	
    var spanassid=window.parent.document.getElementById("span_asset_id");
    var compstr;
   
    if(assid==id)
    {
        spanassid.value=assid;
        id=assid;
    }
    else
    {
        spanassid.value=id;
        id=id;
    }  
    compstr = "<a href=../asset/asset-list.jsp?asset_id=" + id + " target = _blank><b>" + name + "</b></a>";
		
    spanidObj.innerHTML = compstr;
    window.parent.$('#dialog-modal').dialog('close');
   // alert(compid + " selected.");
}

function SelectContractAsset(id, name, indexid, compid, spanid)
{
    var assid=window.parent.document.getElementById("asset_id").value; 
    var spanidObj=window.parent.document.getElementById("span_issue_asset_id");	
    var spanassid=window.parent.document.getElementById("span_asset_id");
	var contract_id=window.parent.document.getElementById("contract_id").value;	
    var compstr;
   
    if(assid==id)
    {
        spanassid.value=assid;
        id=assid;
    }
    else
    {
        spanassid.value=id;
        id=id;
    }
  
    compstr = "<a href=../asset/asset-list.jsp?asset_id=" + id + " target = _blank><b>" + name + "</b></a>";
		
    spanidObj.innerHTML = compstr;
    
  //  alert(compid + " selected.");
	
	//window.parent.location = "../asset/contract-asset.jsp?add=yes&asset_id="+id+"&contract_id="+contract_id+""
	window.parent.$('#dialog-modal').dialog('close');
}

function SelectInsuranceAsset(id, name, indexid, compid, spanid)
{
    var assid=window.parent.document.getElementById("asset_id").value; 
    var spanidObj=window.parent.document.getElementById("span_insurance_asset_id");	
    var spanassid=window.parent.document.getElementById("span_asset_id");
	var insurpolicy_id=window.parent.document.getElementById("insurpolicy_id").value;	
    var compstr;
	
   
    if(assid==id)
    {
        spanassid.value=assid;
        id=assid;
    }
    else
    {
        spanassid.value=id;
        id=id;
    }

    compstr = "<a href=../asset/asset-list.jsp?asset_id=" + id + " target = _blank><b>" + name + "</b></a>";
		
    spanidObj.innerHTML = compstr;
    
    //alert(compid + " selected.");
	window.parent.$('#dialog-modal').dialog('close');
	//window.parent.location = "../asset/insurance-asset.jsp?add=yes&asset_id="+id+"&insurpolicy_id="+insurpolicy_id+""
}

function SelectPOAccount(id, name, indexid, compid, spanid)
{
    var accid=window.parent.document.getElementById("acc_id").value;
    var spanidObj=window.parent.document.getElementById("span_po_account_id");
    var spanaccid=window.parent.document.getElementById("span_acc_id");
    var compstr;
    if(accid==id)
    {
        spanaccid.value=accid;
        id=accid;
    }
    else
    {
        spanaccid.value=id;
        id=id;
    }
    compstr = "<a href=../account/account-list.jsp?account_id=" + id + " target = _blank><b>" + name + " (" + id + ")</b></a>";
    spanidObj.innerHTML = compstr;
    alert(compid + " selected.");
    window.parent.GetSupplierLocationDetails();
}

function SelectCompanyPropertya(id, name, indexid, compid, spanid)
{
    var indexidObj=getObjectOpener(indexid);
    var compidObj=getObjectOpener(compid);
    var spanidObj=getObjectOpener(spanid);
	
    var compstr
    compstr = "<a href=../account/account-list.jsp?account_id=" + id + " target = _blank><b>" + name + " (" + id + ")</b></a>";
    indexidObj.value = id;
    compidObj.value = compstr;
    spanidObj.innerHTML = compstr;
	
    alert(compid + " selected.")
    window.close();
}

function getObject(obj)
{
    var theObj;
    if(document.all)
    {
        if(typeof obj=="string")
        {
            return document.all(obj);
        }
        else
        {
            return obj.style;
        }
    }
    if(document.getElementById)
    {
        if(typeof obj=="string")
        {
            return document.getElementById(obj);
        }
        else
        {
            return obj.style;
        }
    }
    return null;
}
function charcount(entrada,salida,texto,length)
{
    var entradaObj=getObject(entrada);
    var salidaObj=getObject(salida);
    salidaObj.value=entradaObj.value.length;
    var longitud=length - salidaObj.value;
    
    if(longitud <= 0)
    {
        longitud=0;
        texto='<span class="disable">&nbsp;'+texto+'&nbsp;</span>';
        entradaObj.value=entradaObj.value.substr(0,length);
    }
    salidaObj.innerHTML = texto.replace("{CHAR}",longitud);
}

function getObjectOpener(obj)
{
    var theObj;
    if(window.opener.document.all)
    {
        if(typeof obj=="string")
        {
            return window.opener.document.all(obj);
        }
        else
        {
            return obj.style;
        }
    }
    if(window.opener.document.getElementById)
    {
        if(typeof obj=="string")
        {
            return window.opener.document.getElementById(obj);
        }
        else
        {
            return obj.style;
        }
    }
    return null;
}

function SelectCompany(id, name, indexid, companyid, spanid)
{
//    alert("hjhj");
    var indexidObj=getObjectOpener(indexid);
    var companyidObj=getObjectOpener(companyid);
    var spanidObj=getObjectOpener(spanid);
    var companystr
    companystr = "<a href=account-list.jsp?account_id=" + id + " target = _blank><b>" + name + " (" + id + ")</b></a>";
    indexidObj.value = id;
    companyidObj.value = companystr;
    spanidObj.innerHTML = companystr;
    alert(companyid + " selected.")
//window.close();
}

function SelectStudentActivity(id, name, indexid, compid, spanid)//, ptcid, ptc, ph1id, ph1, ph2id, ph2
{
    var indexidObj=getObjectOpener(indexid);
    //alert(spanid + " spanid.")
    var compidObj=getObjectOpener(compid);
    var spanidObj=getObjectOpener(spanid);
    //var ptcidObj=getObjectOpener(ptcid);
    //var ph1idObj=getObjectOpener(ph1id);
    //var ph2idObj=getObjectOpener(ph2id);
    var compstr
    compstr = "<a href=student-list.jsp?student_id=" + id + " target = _blank><b>" + name + " (" + id + ")</b></a>";
    //alert('e'+compstr);
    indexidObj.value = id;
    //compidObj.value = compstr;
    spanidObj.innerHTML = compstr;
    //ptcidObj.value = ptc;
    //ph1idObj.value = ph1;
    //ph2idObj.value = ph2;
    alert(name + " selected.")
    window.close();
}
////////////////////////////////////////////////////////////////
function SelectStudentActivity1(id, name,indexid, compid, spanid,spancontact)//, ptcid, ptc, ph1id, ph1, ph2id, ph2
{
    alert("hell========="+name);
    var indexidObj=getObjectOpener(indexid);
    //alert(spanid + " spanid.")
    var compidObj=getObjectOpener(compid);
    var spanidObj=getObjectOpener(spanid);
    var spanphObj=getObjectOpener(spancontact);
    //var ptcidObj=getObjectOpener(ptcid);
    //var ph1idObj=getObjectOpener(ph1);
    //var ph2idObj=getObjectOpener(ph2id);
    var compstr
    compstr = "<a href=student-list.jsp?student_id=" + id + " target = _blank><b>" + name +" (" + id + ")</b></a>";
    //alert('e'+compstr);
    indexidObj.value = id;
    //compidObj.value = compstr;
    spanidObj.innerHTML = compstr;
    //var space=" ";
    spanphObj.innerHTML = "<input type=\"text\" name=\"txt_sch_contact_person\" class=\"textbox\" value=\""+name+"\" size=\"40\" maxlength=\"255\"/>";
    //ptcidObj.value = ptc;
    //ph1idObj.value = ph1;
    //ph2idObj.value = ph2;
    //alert(name + " selected.")
    window.close();
	
}
////////////////////////////////////////////////////////////////


function SelectMemberProp(id, name, indexid, compid, spanid )
{
       
    var indexidObj=getObjectOpener(indexid);
    var compidObj=getObjectOpener(compid);
    var spanidObj=getObjectOpener(spanid);
    var compstr
    compstr = "<a href=member-list.jsp?member_id=" + id + " target = _blank><b>" + name + " (" + id + ")</b></a>";
    indexidObj.value = id;
    compidObj.value = compstr;
    spanidObj.innerHTML = compstr;
    alert(compid + " selected.")
    window.close();
}

function SelectProp(id, name, indexid, compid, spanid )
{
    var indexidObj=getObjectOpener(indexid);
    var compidObj=getObjectOpener(compid);
    var spanidObj=getObjectOpener(spanid);
    var compstr
    compstr = "<a href=property-list.jsp?prop_id=" + id + " target = _blank><b>" + name + " (" + id + ")</b></a>";
    indexidObj.value = id;
    compidObj.value = compstr;
    spanidObj.innerHTML = compstr;
    alert(compid + " selected.")
    window.close();
}
function SelectProj(id, name, indexid, compid, spanid )
{
    var indexidObj=getObjectOpener(indexid);
    var compidObj=getObjectOpener(compid);
    var spanidObj=getObjectOpener(spanid);
    var compstr
    compstr = "<a href=project-list.jsp?proj_id=" + id + " target = _blank><b>" + name + " (" + id + ")</b></a>";
    indexidObj.value = id;
    compidObj.value = compstr;
    spanidObj.innerHTML = compstr;
    alert(compid + " selected.")
    window.close();
}

function SelectCompanyTicket(id, name, indexid, compid, spanid)
{
    var indexidObj=getObjectOpener(indexid);
    var compidObj=getObjectOpener(compid);
    var spanidObj=getObjectOpener(spanid);
    var compstr
    compstr = "<a href=contacts-list.jsp?client_id=" + id + " target = _blank><b>" + name + " (" + id + ")</b></a>";
    indexidObj.value = id;
    compidObj.value = compstr;
    spanidObj.innerHTML = compstr;
    alert(compid + " selected.")
    window.close();
}

function SelectCity(id, name, indexid, indexname, cityname, spanid, countryname, countryid)
{
    var indexidObj=getObjectOpener(indexid);
    var indexnameObj=getObjectOpener(indexname);
    var spanidObj=getObjectOpener(spanid);
    var citystr
    alert(" selected.")
    citystr = name;
    indexidObj.value = id;
    indexnameObj.value = name;
    spanidObj.innerHTML = citystr;
    if(countryname != "")
    {
        var countryidObj=getObjectOpener(countryname);
        countryidObj.selectedIndex.value == countryid;
    }
    alert(cityname + " selected.")
    window.close();
}


function SelectCategory(id, name, indexid, indexname, Category, spanid)
{
    var indexidObj=getObjectOpener(indexid);
    var indexnameObj=getObjectOpener(indexname);
    var spanidObj=getObjectOpener(spanid);
    var catstr
    catstr = "<B>"+name+"</B>";
    indexidObj.value = id;
    indexnameObj.value = name;
    spanidObj.innerHTML = catstr;
	
    alert(Category + " selected.")
    window.close();
}
function WinStudentSelector(group)
{
    var Id = "";
    remote=window.open('student-list.jsp?all=yes&group='+ group,'Student', 'top=50,width=700,height=500,title=no,resizable=yes,scrollbars=yes,status=yes');
    remote.focus()
    if (remote != null)
    {
        if (remote.opener == null)
            remote.opener = self;
    }
}

function WinPopActivityStatus(doc)
{
    var name = "document";
    var top = "50";
    var left = "50";
    var width = "500";
    var height = "400";
    remote=window.open(doc,name,'top='+top+',left='+left+',width='+width+',height='+height+',title=no,resizable=no,scrollbars=yes,status=yes');
    remote.focus()
    if (remote != null)
    {
        if (remote.opener == null)
            remote.opener = self;
    }
}

function ContactDeSelector(indexid, contactid, spanid)
{
    var indexidObj=getObject(indexid);
    var contactidObj=getObject(contactid);
    var spanidObj=getObject(spanid);
    indexidObj.value = "";
    contactidObj.value = "";
    spanidObj.innerHTML = "";
    alert(contactid + " cleared.");
}

function CityDeSelector(indexid, cityid, spanid)
{
    var indexidObj=getObject(indexid);
    var cityidObj=getObject(cityid);
    var spanidObj=getObject(spanid);
    indexidObj.value = "";
    //cityidObj.value = "";
    spanidObj.innerHTML = "";
    alert(cityid + " cleared.");
}

function WinPop(doc, name, top, left, width, height)
{
    remote=window.open(doc,name, 'top='+top+',left='+left+',width='+width+',height='+height+',title=no,resizable=no,scrollbars=yes,status=no');
    remote.focus()
    if (remote != null)
    {
        if (remote.opener == null)
            remote.opener = self;
    }
}



function fullscreenpopup(url, name) 
{
    params  = 'width='+screen.width;
    params += ', height='+screen.height;
    params += ', top=0, left=0'
    params += ', fullscreen=yes,resizable=no,scrollbars=yes,status=no';

    newwin=window.open(url, name, params);
    if (window.focus) {
        newwin.focus()
    }
    return false;
}

function AddItem(ObjName, DesName, CatName)
{
    //GET OBJECT ID AND DESTINATION OBJECT
	//alert(ObjName + '----' + document.getElementById(ObjName));
    ObjID    = getObject(ObjName);
	DesObjID = getObject(DesName);
//	alert('----' + ObjID);
    //  window.alert(DesObjID.length);
    k=0;
    i = ObjID.options.length;
    if (i==0)
        return;
    maxselected=0
    for (h=0; h<i; h++)
        if (ObjID.options[h].selected ) {
            k=k+1;
            maxselected=h+1;
        }
    if (maxselected>=i)
        maxselected=0;
//    if ( DesObjID.length + k >100 ) {
//     window.alert("You can choose 10 items at most");
//     return;
//     }

    if (CatName != "")
        CatObjID = getObject(CatName);
    else
        CatObjID = 0;
    if ( ObjID != -1 && DesObjID != -1 && CatObjID != -1 )
    {
        jj = CatObjID.selectedIndex;
        if ( CatName != "")
        {
            CatValue = CatObjID.options[jj].text;
            CatCode  = CatObjID.options[jj].value;
        }
        else
            CatValue = "";
        i = ObjID.options.length;
        j = DesObjID.options.length;    
        for (h=0; h<i; h++)
        {
            if (ObjID.options[h].selected )

            {
                Code = ObjID.options[h].value;
                Text = ObjID.options[h].text;
                j = DesObjID.options.length;
//                if (Text.indexOf('&')!=-1) {
//                    for (k=j-1; k>=0; k-- ) {
//                        DesObjID.options[k]=null;
//                    }
//                    j=0;
//                }
                if (Text.substring(0,1)=='-' && Text.substring(1,2)!='-') {
                    for (k=j-1; k>=0; k-- ) {
                        if (((DesObjID.options[k].value).substring(0,2))==(Code.substring(0,2)))
                            DesObjID.options[k]=null;
                    }
                    j= DesObjID.options.length;
                }
                HasSelected = false;
         
                for (k=0; k<j; k++ ) {
                    //     if ((DesObjID.options[k].text).indexOf('&')!=-1){
                    //        HasSelected = true;
                    //        window.alert('You have selected all countries & territories, so no more items can be added.');
                    //        break;
                    //    }

                    //if (ObjID.options[h].selected ==true && (ObjID.options[h].text).indexOf('-')!=-1){
                    //      HasSelected = true;
                    //      window.alert('Can not select separator.');
                    //     break;
                    //   }
	   
                    //   if ((DesObjID.options[k].text).indexOf('[')!=-1){
                    //          if((ObjID.options[h].text).indexOf('[')==-1)
                    //              {HasSelected = true;
                    //               window.alert('You have selected one or more continents, so no more specific country or territory can be added. You may delete selected continents then to add countries & territories.');
                    //	   break;
                    //             }
                    //       }else{
                    //          if((ObjID.options[h].text).indexOf('[')!=-1)
                    //              {HasSelected = true;
                    //               window.alert('You have selected one or more countries or territories, so no more specific continent can be added. You may delete selected countries or territories, then to add continents.');
                    //           break;
                    //              }
                    //  }
           
                    if (DesObjID.options[k].value == Code)
                    {
                        HasSelected = true;
                        break;
                    }
                }
         
         
                if ( HasSelected == false)
                {
                    if (CatValue !="")

                    {
                        Location = GetLocation(DesObjID, CatValue);
                        if ( Location == -1 )
                        {
                            DesObjID.options[j] =  new Option("---"+CatValue+"---",CatCode);
                            DesObjID.options[j+1] = new Option(Text, Code);
                        }//if
                        else
                        {
                            InsertItem(DesObjID, Location+1);
                            DesObjID.options[Location+1] = new Option(Text, Code);
                        }//else
                    }
                    else
                        DesObjID.options[j] = new Option(Text, Code);
                }//if
                ObjID.options[h].selected =false;
            }//if
        }//for
        ObjID.options[maxselected].selected =true;
    }//if
}//end of function

function DeleteItem(ObjName)
{
    ObjID = getObject(ObjName);
	//alert("ObjName inside Delete===" + ObjID);
    minselected=0;
    if ( ObjID != -1 )
    {
        for (i=ObjID.length-1; i>=0; i--)
        {
            if (ObjID.options[i].selected)

            { // window.alert(i);
                if (minselected==0 || i<minselected)
                    minselected=i;
                ObjID.options[i] = null;
            }
        }
        i=ObjID.length;

        if (i>0)  {
            if (minselected>=i)
                minselected=i-1;
            ObjID.options[minselected].selected=true;
        }
    }
}

//Validate float on lost focus &&
function ValidateFloat(textid, textname)
{
    var textidObj=getObject(textid);
    var field_val = textidObj.value;
    //alert(textidObj.value.length);
    if (textidObj.value.length != 0) {
        pat=/^[0-9.,]+$/
        if(field_val.match(pat)){
            return (true);
        }
        else{
            alert(textname + " is not numeric. \nShould consist numbers from 0 - 9 and . or ,!");
            textidObj.focus();
            return (false);
        }
    }
}
function roundNumber(num, dec)
{
    //  alert(num);
    var result = Math.round(num * Math.pow(10, dec))/Math.pow(10, dec);
    // alert(result);
    return result;
}
function toFloat(textid, textname)
{	
    var textidObj=getObject(textid);
    var newString = "";		 // REVISED/CORRECTED STRING
    var count = 0;			 // COUNTER FOR LOOPING THROUGH STRING
    // LOOP THROUGH STRING
    for (i = 0; i < textidObj.value.length; i++) 
    {
        ch = textidObj.value.substring(i, i+1);  // CHECK EACH CHARACTER
        if ((ch >= "0" && ch <= "9") || (ch == ".")) 
        //if (ch >= "0" && ch <= "9")
        {
            newString += ch;
        }
    }
    textidObj.value = newString;
    return (false);
}

function toNumber(textid, textname)
{	
    var textidObj=getObject(textid);
    var newString = "";		 // REVISED/CORRECTED STRING
    var count = 0;			 // COUNTER FOR LOOPING THROUGH STRING
    // LOOP THROUGH STRING
    for (i = 0; i < textidObj.value.length; i++) 
    {
        ch = textidObj.value.substring(i, i+1);  // CHECK EACH CHARACTER
        if ((ch >= "0" && ch <= "9") || (ch == ".") || (ch == ",")) 
        {
            newString += ch;
        }
    }
    textidObj.value = newString;
    return (false);
}

function toInteger(textid)
{
     // alert("hi");
    var textidObj=getObject(textid);
    var newString = "";		 // REVISED/CORRECTED STRING
    var count = 0;
    //      alert("hi"+textidObj);// COUNTER FOR LOOPING THROUGH STRING
    // LOOP THROUGH STRING
    for (i = 0; i < textidObj.value.length; i++) 
    {
        ch = textidObj.value.substring(i, i+1);  // CHECK EACH CHARACTER
        if (ch >= "0" && ch <= "9") 
        {
            newString += ch;
        }
    }
    textidObj.value = newString;
    return (false);
}
//round to next number
function toRound(textid)
{	
    var textidObj=getObject(textid);
    var newString = "0";		 // REVISED/CORRECTED STRING
    newString = textidObj.value;
    if(!isNaN(newString))
        newString=Math.ceil(newString);
    else newString = "0";
    textidObj.value = newString;
}
//Validate date on lost focus &&
function ValidateDate(textid, textname)
{
    var textidObj = getObject(textid);
    var field_val = textidObj.value;
	
    if (textidObj.value.length!=0)
    {
        pat=/^[0-9]+$/
		
        if(field_val.match(pat))
        {
            return (true);
        }
        else
        {
            alert(textname + " is not a valid date. \nShould be in the format dd/mm/yyyy!");
            textidObj.focus();
            return(false);
        }
    }
}

function charcount(entrada,salida,texto,length)
{
    var entradaObj=getObject(entrada);
    var salidaObj=getObject(salida);
    salidaObj.value=entradaObj.value.length;
    var longitud=length - salidaObj.value;
    
    if(longitud <= 0)
    {
        longitud=0;
        texto='<span class="disable">&nbsp;'+texto+'&nbsp;</span>';
        entradaObj.value=entradaObj.value.substr(0,length);
    }
    salidaObj.innerHTML = texto.replace("{CHAR}",longitud);
}

function confirmdelete(form)
{
    var ans;
    ans=window.confirm('Sure you want to delete!');
    //alert (ans);
    if (ans==true)
    {
    //alert('Yes');
    //document.Form1.hdnbox.value='Yes';
    }
    else
    {
        //alert('No');
        return(false);
    //document.Form1.hdnbox.value='No';
    }
}
 
var submitadd = false;
function SubmitFormOnce(myForm, button) {
   //alert("exe");
    if (!submitadd) {
        submitadd = true;
        button.value = 'Please Wait!';
        button.disabled = true;
        myForm.submit();
    }
    else {
        alert ("Already submitted, please wait!");
    }
	//alert("comming==111");
	//Android.showToast("added","1234");
	//alert("comming");
    return true;
}


function getSelected(opt) {
    var selected = new Array();
    var index = 0;
    for (var intLoop = 0; intLoop < opt.length; intLoop++) {
        if ((opt[intLoop].selected) ||
            (opt[intLoop].checked)) {
            index = selected.length;
            selected[index] = new Object;
            selected[index].value = opt[intLoop].value;
            selected[index].index = intLoop;
        }
    }
    return selected;
}
		 
function outputSelected(opt) {
    var sel = getSelected(opt);
    var strSel = "";
    for (var item in sel)
        strSel += sel[item].value + ",";
    //alert("Selected Items:\n" + strSel);
    return strSel;
}

function getoutputSelected(opt) {
    var sel = opt;
    var strSel = "";
    for (var item in sel)
        strSel += sel[item].value + ",";
//    alert("Selected Items:\n" + strSel);
    return strSel;
}
		 
		 
function CheckAll(frm)
{
    var field_val=eval('document.'+frm+'.elements');
    count = field_val.length;
    for (i=0; i < count; i++) 
    {
        field_val[i].checked = 1;
    }
}
function UncheckAll(frm){
    var field_val=eval('document.'+frm+'.elements');
    count = field_val.length;
    for (i=0; i < count; i++) 
    {
        field_val[i].checked = 0;
    }
}
///////////////////////////////////////////AttMarkAllPresent
function AttMarkAllPresent(frm)
{
    var field_val=eval('document.'+frm+'.elements');
    count = field_val.length;
    j=1;
    for (i=0; i < count; i++) 
    {
        if(field_val[i].name=='dr_att_attstatus_id'+j)
        {
            field_val[i].value ="1";
            j=j+1;
        }
        if(field_val[i].name=='dr_testtrans_teststatus'+j)
        {
            field_val[i].value ="1";
            j=j+1;
        }
        if(field_val[i].name=='dr_teststatus_id'+j)
        {
            field_val[i].value ="1";
            j=j+1;
        }
    }
}
function AttMarkAllAbsent(frm)
{
    var field_val=eval('document.'+frm+'.elements');
    count = field_val.length;
    j=1;
    for (i=0; i < count; i++) 
    {
        if(field_val[i].name=='dr_att_attstatus_id'+j)
        {
            // alert(field_val[i].name);
            field_val[i].value ="2";
            j=j+1;
        }
        if(field_val[i].name=='dr_testtrans_teststatus'+j)
        {
            field_val[i].value ="2";
            j=j+1;
        }
        if(field_val[i].name=='dr_teststatus_id'+j)
        {
            field_val[i].value ="2";
            j=j+1;
        }
    }
}
function AttUncheckAll(frm){
    var field_val=eval('document.'+frm+'.elements');
    count = field_val.length;
    for (i=0; i < count; i++) 
    {
        field_val[i].checked = 0;
    }
}
////////////////////////////////////////////////

function check_property(val)
{
    var base=document.navisearch;
    if(val==1)
    {
        base.prop_all.checked=true;
        base.prop_owner.checked=false;
        base.prop_builder.checked=false;
        base.prop_realtor.checked=false;
    }
    if(val==2)
    {
        base.prop_all.checked=false;
    }
}

function SelectStudentLMS(id, student_id)
{
    var indexidObj=parent.document.getElementById(id);
    //    alert(student_id + " selected.");
    indexidObj.value = student_id;
}

function DeleteGuardian(guardian_id, student_id, relation_id)
{
    var ans;
    ans=window.confirm('Sure you want to delete!');
    //alert (ans);
    if (ans==true)
    {
        //alert('Yes');
        window.location= "student-guardian-update.jsp?guardian_id="+guardian_id+"&student_id="+student_id+"&relation_id="+relation_id+"&guardiandelete=yes";
    }
    else
    {
//alert('No');
//		return(false);
}
}

function CheckNumeric(num) {
	if (isNaN(num) || num == '' || num == null) {
		num = 0;
	}
	return num;
}
 
function WinPopTakeTicket(doc)
{
    var name = "newticket";
    var top = "0";
    var left = "0";
    var width = "1000";
    var height = "680";
    remote=window.open(doc,'','top='+top+',left='+left+',width='+width+',height='+height+',title=no,resizable=no,scrollbars=yes,status=yes');
    remote.focus()
    if (remote != null)
    {
        if (remote.opener == null)
            remote.opener = self;
    }
}

function ToggleDisplay(id,spanid)
{
    //    alert("hi=====");
    var elem = document.getElementById(id);
    var elemsign = document.getElementById(spanid);

    if (elem)
    {
        if (elem.style.display != 'block')
        {
            elem.style.display = 'block';
            elem.style.visibility = 'visible';
            elemsign.innerHTML = "[-]";
        }
        else
        {
            elem.style.display = 'none';
            elem.style.visibility = 'hidden';
            elemsign.innerHTML = "[+]";
        }
    }
}

function hideRow(element)
{
    //element refer to row id where u want to hide a row.
    var myObj = document.getElementById(element);
    //u need to a give id for a row to which u want to hide
    var cels = myObj.getElementsByTagName('td');
    for(var col_no=0; col_no < cels.length; col_no++)
    {
        cels[col_no].style.display='none';
    }
    myObj.style.visibility = 'hidden';
    myObj.style.height = '0px';
}

function displayRow(element)
{
    var myObj = document.getElementById(element);
    //u need to a give id for a row to which u want to hide
    var cels = myObj.getElementsByTagName('td')

    for(var col_no=0; col_no < cels.length; col_no++)
    {
        cels[col_no].style.display='';
    }
    myObj.style.visibility = 'visible';
    myObj.style.height = '0px';
}

function  CalServiceTaxComp(Amt, Tax)
{
		        
    var DiscountedTax=0;
    if(Amt=="0" || Tax=="0") return "0";
    DiscountedTax = ((Amt * Tax)/100);
    DiscountedAmt = parseFloat(Amt) + parseFloat(DiscountedTax);
    var Taxout= ((Amt*DiscountedTax)/DiscountedAmt);
    Taxout=roundNumber(Taxout, 2);
    return Taxout;
}

function hexc(colorval) {
    var parts = colorval.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
    delete(parts[0]);
    for (var i = 1; i <= 3; ++i) {
        parts[i] = parseInt(parts[i]).toString(16);
        if (parts[i].length == 1) parts[i] = '0' + parts[i];
    }
    color = '#' + parts.join('');
	return color;
} 

function Click2CallDDMotors(agentName, Password, phoneNumber, campaign){
//alert("agentName-------"+agentName);
var ref_id = "";
	$.ajax({
	       type: 'POST',
               url: 'http://182.71.188.123/Click2Call/click2callinitiation.php',
               dataType:'json',
               crossDomain: true,
               data:{action:"Call",customer_phone:phoneNumber,camp_name:campaign,agent_uname:agentName, agent_password: md5(Password),authstring:'DDclick2call'},
               success: function(response) {
		    if(response['ref_id'] == ""){
		    $("#Hintclicktocall").dialog('close');
			$("#error_msg").text(response['status_message']);
			$(".error").show();
			$(".error").fadeOut(2500);
		    }else{
		    	$("#Hintclicktocall").dialog('close');
			ref_id = response['ref_id'];
			$("#id_agentname").attr('disabled',true);
			$("#id_password").attr('disabled',true);
			$("#id_phoneNumber").attr('disabled',true);
			$("#id_campaign").attr('disabled',true);
			$("#step_1").hide();
			$("#step_2").show();
			$("#success_msg").text(response['status_message']);
			$(".success").show();
			$(".success").fadeOut(2500);
		    }
               },
               error: function(error){
            	   $("#Hintclicktocall").dialog('close');
		  var error = JSON.parse(error['responseText']);
		  $("#error_msg").text(error['status_message']);
		  $(".error").show();
		  $(".error").fadeOut(2500);
               }
               
	});
    }

//=========================================== This is copied from MD5.JS==========================

function md5 (str) {
	  var xl

	  var rotateLeft = function (lValue, iShiftBits) {
	    return (lValue << iShiftBits) | (lValue >>> (32 - iShiftBits))
	  }

	  var addUnsigned = function (lX, lY) {
	    var lX4, lY4, lX8, lY8, lResult
	    lX8 = (lX & 0x80000000)
	    lY8 = (lY & 0x80000000)
	    lX4 = (lX & 0x40000000)
	    lY4 = (lY & 0x40000000)
	    lResult = (lX & 0x3FFFFFFF) + (lY & 0x3FFFFFFF)
	    if (lX4 & lY4) {
	      return (lResult ^ 0x80000000 ^ lX8 ^ lY8)
	    }
	    if (lX4 | lY4) {
	      if (lResult & 0x40000000) {
	        return (lResult ^ 0xC0000000 ^ lX8 ^ lY8)
	      } else {
	        return (lResult ^ 0x40000000 ^ lX8 ^ lY8)
	      }
	    } else {
	      return (lResult ^ lX8 ^ lY8)
	    }
	  }

	  var _F = function (x, y, z) {
	    return (x & y) | ((~x) & z)
	  }
	  var _G = function (x, y, z) {
	    return (x & z) | (y & (~z))
	  }
	  var _H = function (x, y, z) {
	    return (x ^ y ^ z)
	  }
	  var _I = function (x, y, z) {
	    return (y ^ (x | (~z)))
	  }

	  var _FF = function (a, b, c, d, x, s, ac) {
	    a = addUnsigned(a, addUnsigned(addUnsigned(_F(b, c, d), x), ac))
	    return addUnsigned(rotateLeft(a, s), b)
	  }

	  var _GG = function (a, b, c, d, x, s, ac) {
	    a = addUnsigned(a, addUnsigned(addUnsigned(_G(b, c, d), x), ac))
	    return addUnsigned(rotateLeft(a, s), b)
	  }

	  var _HH = function (a, b, c, d, x, s, ac) {
	    a = addUnsigned(a, addUnsigned(addUnsigned(_H(b, c, d), x), ac))
	    return addUnsigned(rotateLeft(a, s), b)
	  }

	  var _II = function (a, b, c, d, x, s, ac) {
	    a = addUnsigned(a, addUnsigned(addUnsigned(_I(b, c, d), x), ac))
	    return addUnsigned(rotateLeft(a, s), b)
	  }

	  var convertToWordArray = function (str) {
	    var lWordCount
	    var lMessageLength = str.length
	    var lNumberOfWords_temp1 = lMessageLength + 8
	    var lNumberOfWords_temp2 = (lNumberOfWords_temp1 - (lNumberOfWords_temp1 % 64)) / 64
	    var lNumberOfWords = (lNumberOfWords_temp2 + 1) * 16
	    var lWordArray = new Array(lNumberOfWords - 1)
	    var lBytePosition = 0
	    var lByteCount = 0
	    while (lByteCount < lMessageLength) {
	      lWordCount = (lByteCount - (lByteCount % 4)) / 4
	      lBytePosition = (lByteCount % 4) * 8
	      lWordArray[lWordCount] = (lWordArray[lWordCount] | (str.charCodeAt(lByteCount) << lBytePosition))
	      lByteCount++
	    }
	    lWordCount = (lByteCount - (lByteCount % 4)) / 4
	    lBytePosition = (lByteCount % 4) * 8
	    lWordArray[lWordCount] = lWordArray[lWordCount] | (0x80 << lBytePosition)
	    lWordArray[lNumberOfWords - 2] = lMessageLength << 3
	    lWordArray[lNumberOfWords - 1] = lMessageLength >>> 29
	    return lWordArray
	  }

	  var wordToHex = function (lValue) {
	    var wordToHexValue = '',
	      wordToHexValue_temp = '',
	      lByte, lCount
	    for (lCount = 0; lCount <= 3; lCount++) {
	      lByte = (lValue >>> (lCount * 8)) & 255
	      wordToHexValue_temp = '0' + lByte.toString(16)
	      wordToHexValue = wordToHexValue + wordToHexValue_temp.substr(wordToHexValue_temp.length - 2, 2)
	    }
	    return wordToHexValue
	  }

	  var x = [],
	    k, AA, BB, CC, DD, a, b, c, d, S11 = 7,
	    S12 = 12,
	    S13 = 17,
	    S14 = 22,
	    S21 = 5,
	    S22 = 9,
	    S23 = 14,
	    S24 = 20,
	    S31 = 4,
	    S32 = 11,
	    S33 = 16,
	    S34 = 23,
	    S41 = 6,
	    S42 = 10,
	    S43 = 15,
	    S44 = 21

	  str = this.utf8_encode(str)
	  x = convertToWordArray(str)
	  a = 0x67452301
	  b = 0xEFCDAB89
	  c = 0x98BADCFE
	  d = 0x10325476

	  xl = x.length
	  for (k = 0; k < xl; k += 16) {
	    AA = a
	    BB = b
	    CC = c
	    DD = d
	    a = _FF(a, b, c, d, x[k + 0], S11, 0xD76AA478)
	    d = _FF(d, a, b, c, x[k + 1], S12, 0xE8C7B756)
	    c = _FF(c, d, a, b, x[k + 2], S13, 0x242070DB)
	    b = _FF(b, c, d, a, x[k + 3], S14, 0xC1BDCEEE)
	    a = _FF(a, b, c, d, x[k + 4], S11, 0xF57C0FAF)
	    d = _FF(d, a, b, c, x[k + 5], S12, 0x4787C62A)
	    c = _FF(c, d, a, b, x[k + 6], S13, 0xA8304613)
	    b = _FF(b, c, d, a, x[k + 7], S14, 0xFD469501)
	    a = _FF(a, b, c, d, x[k + 8], S11, 0x698098D8)
	    d = _FF(d, a, b, c, x[k + 9], S12, 0x8B44F7AF)
	    c = _FF(c, d, a, b, x[k + 10], S13, 0xFFFF5BB1)
	    b = _FF(b, c, d, a, x[k + 11], S14, 0x895CD7BE)
	    a = _FF(a, b, c, d, x[k + 12], S11, 0x6B901122)
	    d = _FF(d, a, b, c, x[k + 13], S12, 0xFD987193)
	    c = _FF(c, d, a, b, x[k + 14], S13, 0xA679438E)
	    b = _FF(b, c, d, a, x[k + 15], S14, 0x49B40821)
	    a = _GG(a, b, c, d, x[k + 1], S21, 0xF61E2562)
	    d = _GG(d, a, b, c, x[k + 6], S22, 0xC040B340)
	    c = _GG(c, d, a, b, x[k + 11], S23, 0x265E5A51)
	    b = _GG(b, c, d, a, x[k + 0], S24, 0xE9B6C7AA)
	    a = _GG(a, b, c, d, x[k + 5], S21, 0xD62F105D)
	    d = _GG(d, a, b, c, x[k + 10], S22, 0x2441453)
	    c = _GG(c, d, a, b, x[k + 15], S23, 0xD8A1E681)
	    b = _GG(b, c, d, a, x[k + 4], S24, 0xE7D3FBC8)
	    a = _GG(a, b, c, d, x[k + 9], S21, 0x21E1CDE6)
	    d = _GG(d, a, b, c, x[k + 14], S22, 0xC33707D6)
	    c = _GG(c, d, a, b, x[k + 3], S23, 0xF4D50D87)
	    b = _GG(b, c, d, a, x[k + 8], S24, 0x455A14ED)
	    a = _GG(a, b, c, d, x[k + 13], S21, 0xA9E3E905)
	    d = _GG(d, a, b, c, x[k + 2], S22, 0xFCEFA3F8)
	    c = _GG(c, d, a, b, x[k + 7], S23, 0x676F02D9)
	    b = _GG(b, c, d, a, x[k + 12], S24, 0x8D2A4C8A)
	    a = _HH(a, b, c, d, x[k + 5], S31, 0xFFFA3942)
	    d = _HH(d, a, b, c, x[k + 8], S32, 0x8771F681)
	    c = _HH(c, d, a, b, x[k + 11], S33, 0x6D9D6122)
	    b = _HH(b, c, d, a, x[k + 14], S34, 0xFDE5380C)
	    a = _HH(a, b, c, d, x[k + 1], S31, 0xA4BEEA44)
	    d = _HH(d, a, b, c, x[k + 4], S32, 0x4BDECFA9)
	    c = _HH(c, d, a, b, x[k + 7], S33, 0xF6BB4B60)
	    b = _HH(b, c, d, a, x[k + 10], S34, 0xBEBFBC70)
	    a = _HH(a, b, c, d, x[k + 13], S31, 0x289B7EC6)
	    d = _HH(d, a, b, c, x[k + 0], S32, 0xEAA127FA)
	    c = _HH(c, d, a, b, x[k + 3], S33, 0xD4EF3085)
	    b = _HH(b, c, d, a, x[k + 6], S34, 0x4881D05)
	    a = _HH(a, b, c, d, x[k + 9], S31, 0xD9D4D039)
	    d = _HH(d, a, b, c, x[k + 12], S32, 0xE6DB99E5)
	    c = _HH(c, d, a, b, x[k + 15], S33, 0x1FA27CF8)
	    b = _HH(b, c, d, a, x[k + 2], S34, 0xC4AC5665)
	    a = _II(a, b, c, d, x[k + 0], S41, 0xF4292244)
	    d = _II(d, a, b, c, x[k + 7], S42, 0x432AFF97)
	    c = _II(c, d, a, b, x[k + 14], S43, 0xAB9423A7)
	    b = _II(b, c, d, a, x[k + 5], S44, 0xFC93A039)
	    a = _II(a, b, c, d, x[k + 12], S41, 0x655B59C3)
	    d = _II(d, a, b, c, x[k + 3], S42, 0x8F0CCC92)
	    c = _II(c, d, a, b, x[k + 10], S43, 0xFFEFF47D)
	    b = _II(b, c, d, a, x[k + 1], S44, 0x85845DD1)
	    a = _II(a, b, c, d, x[k + 8], S41, 0x6FA87E4F)
	    d = _II(d, a, b, c, x[k + 15], S42, 0xFE2CE6E0)
	    c = _II(c, d, a, b, x[k + 6], S43, 0xA3014314)
	    b = _II(b, c, d, a, x[k + 13], S44, 0x4E0811A1)
	    a = _II(a, b, c, d, x[k + 4], S41, 0xF7537E82)
	    d = _II(d, a, b, c, x[k + 11], S42, 0xBD3AF235)
	    c = _II(c, d, a, b, x[k + 2], S43, 0x2AD7D2BB)
	    b = _II(b, c, d, a, x[k + 9], S44, 0xEB86D391)
	    a = addUnsigned(a, AA)
	    b = addUnsigned(b, BB)
	    c = addUnsigned(c, CC)
	    d = addUnsigned(d, DD)
	  }

	  var temp = wordToHex(a) + wordToHex(b) + wordToHex(c) + wordToHex(d)

	  return temp.toLowerCase()
	}

	function utf8_encode (argString) {
	  //  discuss at: http://phpjs.org/functions/utf8_encode/
	  // original by: Webtoolkit.info (http://www.webtoolkit.info/)
	  // improved by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
	  // improved by: sowberry
	  // improved by: Jack
	  // improved by: Yves Sucaet
	  // improved by: kirilloid
	  // bugfixed by: Onno Marsman
	  // bugfixed by: Onno Marsman
	  // bugfixed by: Ulrich
	  // bugfixed by: Rafał Kukawski (http://blog.kukawski.pl)
	  // bugfixed by: kirilloid
	  //   example 1: utf8_encode('Kevin van Zonneveld');
	  //   returns 1: 'Kevin van Zonneveld'

	  if (argString === null || typeof argString === 'undefined') {
	    return ''
	  }

	  // .replace(/\r\n/g, "\n").replace(/\r/g, "\n");
	  var string = (argString + '')
	  var utftext = '',
	    start, end, stringl = 0

	  start = end = 0
	  stringl = string.length
	  for (var n = 0; n < stringl; n++) {
	    var c1 = string.charCodeAt(n)
	    var enc = null

	    if (c1 < 128) {
	      end++
	    } else if (c1 > 127 && c1 < 2048) {
	      enc = String.fromCharCode(
	        (c1 >> 6) | 192, (c1 & 63) | 128
	      )
	    } else if ((c1 & 0xF800) != 0xD800) {
	      enc = String.fromCharCode(
	        (c1 >> 12) | 224, ((c1 >> 6) & 63) | 128, (c1 & 63) | 128
	      )
	    } else {
	      // surrogate pairs
	      if ((c1 & 0xFC00) != 0xD800) {
	        throw new RangeError('Unmatched trail surrogate at ' + n)
	      }
	      var c2 = string.charCodeAt(++n)
	      if ((c2 & 0xFC00) != 0xDC00) {
	        throw new RangeError('Unmatched lead surrogate at ' + (n - 1))
	      }
	      c1 = ((c1 & 0x3FF) << 10) + (c2 & 0x3FF) + 0x10000
	      enc = String.fromCharCode(
	        (c1 >> 18) | 240, ((c1 >> 12) & 63) | 128, ((c1 >> 6) & 63) | 128, (c1 & 63) | 128
	      )
	    }
	    if (enc !== null) {
	      if (end > start) {
	        utftext += string.slice(start, end)
	      }
	      utftext += enc
	      start = end = n + 1
	    }
	  }

	  if (end > start) {
	    utftext += string.slice(start, stringl)
	  }

	  return utftext
	}
	
	
	function indDecimalFormat(val) {
		var deci = "";
		var negative = "";
		if (val.includes("-")) {
			val = val.replace("-", "");
			negative = "-";
		}
		if (val.includes(".")) {
			deci = val.substring(val.indexOf("."));
		}
		var d = parseFloat(val);
		d = Math.floor(d);
		var val1 = Math.round(d) + "";
		var sb = val1 + "";
		var len = sb.length;
		if (len < 4) {
			return negative + sb.toString() + deci;
		}
		var cp = len - 3;
		sb = sb.slice(0, cp) +  ',' + sb.slice(cp);
		if (len < 6) {
			return negative + sb.toString() + deci;
		}
		while (cp > 2) {
			cp -= 2;
			sb = sb.slice(0, cp) +  ',' + sb.slice(cp);
		}
		return negative + sb.toString() + deci;
	}
	
	function removeIndDecimalFormat(val) {
		return  val.replace(/,/g, '');
	}


	
// =======================end of MD5.js===================================================