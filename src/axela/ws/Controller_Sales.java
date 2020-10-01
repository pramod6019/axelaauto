//package axela.ws;
//
//import java.io.File;
//import java.io.InputStream;
//
//import javax.ws.rs.Consumes;
//import javax.ws.rs.Encoded;
//import javax.ws.rs.GET;
//import javax.ws.rs.POST;
//import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//import javax.ws.rs.QueryParam;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//
//import org.codehaus.jettison.json.JSONObject;
//
//import axela.ws.sales.WS_ActivityData;
//import axela.ws.sales.WS_Add_Quote;
//import axela.ws.sales.WS_AppVersion;
//import axela.ws.sales.WS_Branch_Dashboard;
//import axela.ws.sales.WS_CRMFollowup_Update;
//import axela.ws.sales.WS_Enquiry_AddData;
//import axela.ws.sales.WS_Enquiry_Brochure_EmailData;
//import axela.ws.sales.WS_Enquiry_Dash;
//import axela.ws.sales.WS_Enquiry_DashCRMFollowup;
//import axela.ws.sales.WS_Enquiry_DashFollowUpData;
//import axela.ws.sales.WS_Enquiry_Dash_CheckData;
//import axela.ws.sales.WS_Enquiry_List;
//import axela.ws.sales.WS_Enquiry_Model_ItemData;
//import axela.ws.sales.WS_Enquiry_QuickAdd;
//import axela.ws.sales.WS_Enquiry_SearchData;
//import axela.ws.sales.WS_Esc_Enquiry_Followup;
//import axela.ws.sales.WS_Executive_Uploadimage;
//import axela.ws.sales.WS_Executives_List;
//import axela.ws.sales.WS_ForgotPassword;
//import axela.ws.sales.WS_GPSData;
//import axela.ws.sales.WS_HomeData;
//import axela.ws.sales.WS_Monitoring_Board;
//import axela.ws.sales.WS_News_Branch_List;
//import axela.ws.sales.WS_PopulateMainCityData;
//import axela.ws.sales.WS_PrintPDF;
//import axela.ws.sales.WS_Print_QuotePDF;
//import axela.ws.sales.WS_Quote;
//import axela.ws.sales.WS_Salesorder_Dash;
//import axela.ws.sales.WS_Salesorder_Dash_Customer;
//import axela.ws.sales.WS_SignInData;
//import axela.ws.sales.WS_Status_Update; 
//import axela.ws.sales.WS_System_PasswordData;
//import axela.ws.sales.WS_TestDrive_Check;
//import axela.ws.sales.WS_TestDrive_Feedback;
//import axela.ws.sales.WS_TestDrive_List;
//import axela.ws.sales.WS_TestDrive_Print_GatePass;
//import axela.ws.sales.WS_TestDrive_Update;
//import axela.ws.sales.WS_Thumbnail;
//import axela.ws.sales.WS_Veh_Quote_Email;
//import axela.ws.sales.WS_Veh_Quote_List;
//import axela.ws.sales.WS_Veh_Quote_Update;
//import axela.ws.sales.WS_Veh_Salesorder_List;
//import axela.ws.sales.WS_Veh_Salesorder_Update;
//
//import com.sun.jersey.core.header.FormDataContentDisposition;
//import com.sun.jersey.multipart.FormDataParam;
//
//@Path("/sales")
//public class Controller_Sales {
//
//	public Controller_Sales() {
//	}
//
//	@Path("/test")
//	@GET
//	@Produces("text/html")
//	public String test() throws Exception {
//		return "WS Sales Test Successful.";
//	}
//
//	@Path("/signin")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	public Response createsignin(JSONObject input) throws Exception {
//		WS_SignInData signin = new WS_SignInData();
//		JSONObject output = signin.signindata(input);
//		return Response.status(201).entity(output.toString()).build();
//	}
//
//	@Path("/home")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	public Response createhome(JSONObject input) throws Exception {
//		WS_HomeData homeObj = new WS_HomeData();
//		JSONObject output = homeObj.home(input);
//		return Response.status(201).entity(output.toString()).build();
//	}
//
//	@Path("/app-version")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	public Response version(JSONObject input) throws Exception {
//		WS_AppVersion home = new WS_AppVersion();
//		JSONObject output = home.appversion(input);
//		return Response.status(201).entity(output.toString()).build();
//	}
//
//	@Path("/cities")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	public Response createcities(JSONObject input) throws Exception {
//		WS_PopulateMainCityData city = new WS_PopulateMainCityData();
//		JSONObject output = city.citiesdata(input);
//		return Response.status(201).entity(output.toString()).build();
//	}
//
//	@Path("/activity-list")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	public Response createactivitylist(JSONObject input) throws Exception {
//		WS_ActivityData activity = new WS_ActivityData();
//		JSONObject output = activity.ActivityList(input);
//		return Response.status(201).entity(output.toString()).build();
//	}
//
//	@Path("/enquiry-list")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	public Response createenquirylistesc(JSONObject input) throws Exception {
//		WS_Enquiry_List enquirylistobj = new WS_Enquiry_List();
//		JSONObject output = enquirylistobj.EnquiryList(input);
//		return Response.status(201).entity(output.toString()).build();
//	}
//
//	@Path("/executives-list")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	public Response createexecutiveslist(JSONObject input) throws Exception {
//		WS_Executives_List exe = new WS_Executives_List();
//		JSONObject output = exe.ExecutivesList(input);
//		return Response.status(201).entity(output.toString()).build();
//	}
//
//	@Path("/thumbnail")
//	@GET
//	@Produces("image/jpeg")
//	public Response displayimage(
//			@QueryParam("emp_id") String emp_id,
//			@QueryParam("comp_id") String comp_id,
//			@QueryParam("image") String image,
//			@QueryParam("path") String path,
//			@QueryParam("width") String width) throws Exception {
//		WS_Thumbnail thumbnail = new WS_Thumbnail();
//		Response.ResponseBuilder response = Response.ok((Object) thumbnail.imagedata(emp_id, comp_id, image, path, width));
//		return response.build();
//	}
//
//	@Path("/enquiry-sendbrochure")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	public Response sendbrochure(JSONObject input) throws Exception {
//		WS_Enquiry_Brochure_EmailData enquirysendbrochure = new WS_Enquiry_Brochure_EmailData();
//		JSONObject output = enquirysendbrochure.Enquiry_SendBrochure(input);
//		return Response.status(201).entity(output.toString()).build();
//	}
//
//	@Path("/enquiry-search")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	public Response enquirysearch(JSONObject input) throws Exception {
//		WS_Enquiry_SearchData search = new WS_Enquiry_SearchData();
//		JSONObject output = search.Enquiry_Search(input);
//		return Response.status(201).entity(output.toString()).build();
//	}
//
//	@Path("/forgotpassword")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	public Response remind(JSONObject input) throws Exception {
//		WS_ForgotPassword forgotpwd = new WS_ForgotPassword();
//		JSONObject output = forgotpwd.ForgotPassword(input);
//		return Response.status(201).entity(output.toString()).build();
//	}
//
//	@Path("/changepassword")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	public Response systempassword(JSONObject input) throws Exception {
//		WS_System_PasswordData changepwd = new WS_System_PasswordData();
//		JSONObject output = changepwd.changepassword(input);
//		return Response.status(201).entity(output.toString()).build();
//	}
//
//	@Path("/enquiry-add")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	public Response createenquiryadd(JSONObject input) throws Exception {
//		WS_Enquiry_AddData enquiryaddobj = new WS_Enquiry_AddData();
//		JSONObject output = enquiryaddobj.Enquiry_Add(input);
//		return Response.status(201).entity(output.toString()).build();
//	}
//
//	@Path("/enquiry-quickadd")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	public Response createenquiryquickadd(JSONObject input) throws Exception {
//		WS_Enquiry_QuickAdd enquiryaddobj = new WS_Enquiry_QuickAdd();
//		JSONObject output = enquiryaddobj.Enquiry_QuickAdd(input);
//		return Response.status(201).entity(output.toString()).build();
//	}
//
//	@Path("/enquiry-dash")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	public Response createenquirydash(JSONObject input) throws Exception {
//		WS_Enquiry_Dash enquirydash = new WS_Enquiry_Dash();
//		JSONObject output = enquirydash.Enquiry_Dash(input);
//		return Response.status(201).entity(output.toString()).build();
//	}
//
//	@Path("/enquiry-dash-check")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	public Response createenquirydashcheck(JSONObject input) throws Exception {
//		WS_Enquiry_Dash_CheckData dashcheck = new WS_Enquiry_Dash_CheckData();
//		JSONObject output = dashcheck.dashcheck(input);
//		return Response.status(201).entity(output.toString()).build();
//	}
//
//	@Path("/enquiry-dash-followup")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	public Response createopprdashfollowup(JSONObject input) throws Exception {
//		WS_Enquiry_DashFollowUpData enquirydashfollowup = new WS_Enquiry_DashFollowUpData();
//		JSONObject output = enquirydashfollowup.Enquiry_Dash_FollowUp(input);
//		return Response.status(201).entity(output.toString()).build();
//	}
//
//	@Path("/enquiry-dash-crmfollowup")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	public Response createcrmdashfollowup(JSONObject input) throws Exception {
//		WS_Enquiry_DashCRMFollowup enquirydashcrmfollowup = new WS_Enquiry_DashCRMFollowup();
//		JSONObject output = enquirydashcrmfollowup.Enquiry_Dash_CRMFollowUp(input);
//		return Response.status(201).entity(output.toString()).build();
//	}
//	@Path("/crmfollowup-update")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	public Response createcrmfollowup(JSONObject input) throws Exception {
//		WS_CRMFollowup_Update enquirycrmfollowupupdate = new WS_CRMFollowup_Update();
//		JSONObject output = enquirycrmfollowupupdate.CRMFollowUp_Update(input);
//		return Response.status(201).entity(output.toString()).build();
//	}
//
//	@Path("/model-item")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	public Response createmodelitem(JSONObject input) throws Exception {
//		WS_Enquiry_Model_ItemData model = new WS_Enquiry_Model_ItemData();
//		JSONObject output = model.Enquiry_Model_Item(input);
//		return Response.status(201).entity(output.toString()).build();
//	}
//
//	@Path("/esc-enquiry-followup")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	public Response createesc(JSONObject input) throws Exception {
//		WS_Esc_Enquiry_Followup esc_enq_followup = new WS_Esc_Enquiry_Followup();
//		JSONObject output = esc_enq_followup.EscList(input);
//		return Response.status(201).entity(output.toString()).build();
//	}
//
//	@Path("/testdrive-list")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	public Response createtestdrivelist(JSONObject input) throws Exception {
//		WS_TestDrive_List testdrive = new WS_TestDrive_List();
//		JSONObject output = testdrive.TestDriveList(input);
//		return Response.status(201).entity(output.toString()).build();
//	}
//
//	@Path("/testdrive-update")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	public Response createtestdriveupdate(JSONObject input) throws Exception {
//		WS_TestDrive_Update testdrive_update = new WS_TestDrive_Update();
//		JSONObject output = testdrive_update.TestDrive_Update(input);
//		return Response.status(201).entity(output.toString()).build();
//	}
//
//	@Path("/testdrive-feedback")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	public Response createtestdrivefeedback(JSONObject input) throws Exception {
//		WS_TestDrive_Feedback fb = new WS_TestDrive_Feedback();
//		JSONObject output = fb.TestDrive_Feedback(input);
//		return Response.status(201).entity(output.toString()).build();
//	}
//
//	@Path("/testdrive-check")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	public Response createtestdrivecheck(JSONObject input) throws Exception {
//		WS_TestDrive_Check testdrive_check = new WS_TestDrive_Check();
//		JSONObject output = testdrive_check.TestDrive_CheckData(input);
//		return Response.status(201).entity(output.toString()).build();
//	}
//
//	@Path("/quote-add")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	public Response createquoteadd(JSONObject input) throws Exception {
//		WS_Add_Quote quoteaddobj = new WS_Add_Quote();
//		JSONObject output = quoteaddobj.AddQuote(input);
//		return Response.status(201).entity(output.toString()).build();
//	}
//
//	@Path("/quote-update")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	public Response createquoteupdate(JSONObject input) throws Exception {
//		WS_Veh_Quote_Update quoteupdateobj = new WS_Veh_Quote_Update();
//		JSONObject output = quoteupdateobj.QuoteUpdate(input);
//		return Response.status(201).entity(output.toString()).build();
//	}
//
//	@Path("/salesorder-update")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	public Response createsalesorderupdate(JSONObject input) throws Exception {
//		WS_Veh_Salesorder_Update salesorderupdateobj = new WS_Veh_Salesorder_Update();
//		JSONObject output = salesorderupdateobj.SalesorderUpdate(input);
//		return Response.status(201).entity(output.toString()).build();
//	}
//
//	@Path("/status-update")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	public Response statusUpdate(JSONObject input) throws Exception {
//		WS_Status_Update statusupdate = new WS_Status_Update();
//		JSONObject output = statusupdate.Status_Update(input);
//		return Response.status(201).entity(output.toString()).build();
//	}
//
//	@Path("/news")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	public Response createnewslistesc(JSONObject input) throws Exception {
//		WS_News_Branch_List newslistobj = new WS_News_Branch_List();
//		JSONObject output = newslistobj.NewsList(input);
//		return Response.status(201).entity(output.toString()).build();
//	}
//
//	@Path("/quote-url")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	public Response createquote(JSONObject input) throws Exception {
//		WS_Quote quoteobj = new WS_Quote();
//		JSONObject output = quoteobj.Quote(input);
//		return Response.status(201).entity(output.toString()).build();
//	}
//
//	@Path("/quote-email")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	public Response createquoteemail(JSONObject input) throws Exception {
//		WS_Veh_Quote_Email quoteemailobj = new WS_Veh_Quote_Email();
//		JSONObject output = quoteemailobj.QuoteEmail(input);
//		return Response.status(201).entity(output.toString()).build();
//	}
//
//	@Path("/veh-quote-list")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	public Response quotelist(JSONObject input) throws Exception {
//		WS_Veh_Quote_List vehlist = new WS_Veh_Quote_List();
//		JSONObject output = vehlist.QuoteList(input);
//		return Response.status(201).entity(output.toString()).build();
//	}
//
//	@Path("/veh-salesorder-list")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	public Response salesorderlist(JSONObject input) throws Exception {
//		WS_Veh_Salesorder_List vehlist = new WS_Veh_Salesorder_List();
//		JSONObject output = vehlist.SalesorderList(input);
//		return Response.status(201).entity(output.toString()).build();
//	}
//
//	@Path("/salesorder-dash")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	public Response createsalesorderdash(JSONObject input) throws Exception {
//		WS_Salesorder_Dash salesorderdash = new WS_Salesorder_Dash();
//		JSONObject output = salesorderdash.Salesorder_Dash(input);
//		return Response.status(201).entity(output.toString()).build();
//	}
//
//	@Path("/salesorder-dash-customer")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	public Response salesorderdashcustomer(JSONObject input) throws Exception {
//		WS_Salesorder_Dash_Customer salesorderdashcust = new WS_Salesorder_Dash_Customer();
//		JSONObject output = salesorderdashcust.SalesOrder_Dash_Customer(input);
//		return Response.status(201).entity(output.toString()).build();
//	}
//
//	// @Path("/quote-add")
//	// @POST
//	// @Consumes("application/json")
//	// @Produces("application/json")
//	// public Response quoteadd(JSONObject input) throws Exception {
//	// WSVeh_Quote_Add quoteadd = new WSVeh_Quote_Add();
//	// JSONObject output = quoteadd.QuoteAdd(input);
//	// return Response.status(201).entity(output.toString()).build();
//	// }
//	@Path("/gps")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	public Response creategps(JSONObject input) throws Exception {
//		WS_GPSData gps = new WS_GPSData();
//		JSONObject output = gps.gpsdata(input);
//		return Response.status(201).entity(output.toString()).build();
//	}
//
//	@Path("/list-dashboard")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	public Response dashboard(JSONObject input) throws Exception {
//		WS_Branch_Dashboard dash = new WS_Branch_Dashboard();
//		JSONObject output = dash.dashboard(input);
//		return Response.status(201).entity(output.toString()).build();
//	}
//
//	@Path("/monitoring-board")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	public Response monitoringboard(JSONObject input) throws Exception {
//		WS_Monitoring_Board mb = new WS_Monitoring_Board();
//		JSONObject output = mb.monitoringboard(input);
//		return Response.status(201).entity(output.toString()).build();
//	}
//
//	// @Path("/enquiry-print-old")
//	// @POST
//	// @Consumes("application/json")
//	// @Produces("application/pdf")
//	// public Response sendpdf(JSONObject input) throws Exception {
//	// Enquiry_Print print = new Enquiry_Print();
//	// ResponseBuilder response = Response.ok((Object)print.sendpdf(input));
//	// response.header("Content-Disposition",
//	// "attachment; filename=Enquiry_" + Enquiry_Print.enquiry_id + ".pdf");
//	// return response.build();
//	// }
//	@Path("/enquiry-print")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/pdf")
//	public Response sendenquirypdf(JSONObject input) throws Exception {
//		WS_PrintPDF print = new WS_PrintPDF();
//		String loc = print.sendpdf(input);
//		File file = new File(loc);
//		Response.ResponseBuilder response = Response.ok((Object) file);
//		response.header("Content-Disposition",
//				"attachment; filename=new-android.pdf");
//		return response.status(201).build();
//	}
//
//	@Path("/quote-print")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/pdf")
//	public Response sendquotepdf(JSONObject input) throws Exception {
//
//		WS_Print_QuotePDF printquote = new WS_Print_QuotePDF();
//		String loc = printquote.sendquotepdf(input);
//		File file = new File(loc);
//		Response.ResponseBuilder response = Response.ok((Object) file);
//		response.header("Content-Disposition",
//				"attachment; filename=new-android.pdf");
//		return response.status(201).build();
//	}
//
//	@Path("/gatepass-print")
//	@POST
//	@Consumes("application/json")
//	@Produces("application/pdf")
//	public Response sendgatepasspdf(JSONObject input) throws Exception {
//
//		WS_TestDrive_Print_GatePass printgatepass = new WS_TestDrive_Print_GatePass();
//		String loc = printgatepass.sendgatepasspdf(input);
//		File file = new File(loc);
//		Response.ResponseBuilder response = Response.ok((Object) file);
//		response.header("Content-Disposition",
//				"attachment; filename=new-android.pdf");
//		return response.status(201).build();
//	}
//
//	//
//	@Path("/upload-exeimage")
//	@POST
//	@Consumes(MediaType.MULTIPART_FORM_DATA)
//	// @Produces("text/plain")
//	public Response exeImageUpload(
//			@Encoded @FormDataParam("emp_id") String emp_id,
//			@Encoded @FormDataParam("comp_id") String comp_id,
//			@FormDataParam("exeimage") InputStream is,
//			@FormDataParam("exeimage") FormDataContentDisposition formData
//			) {
//		WS_Executive_Uploadimage upload = new WS_Executive_Uploadimage();
//		JSONObject output = upload.UploadImage(emp_id, comp_id, is, formData);
//		return Response.status(200).entity(output).build();
//	}
//
// }
