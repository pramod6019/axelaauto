package axela.ws;

import java.io.File;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONObject;

import axela.ws.axelaautoapp.WS_Activity_List;
import axela.ws.axelaautoapp.WS_CRM_Followup;
import axela.ws.axelaautoapp.WS_Driving_Licence_Upload;
import axela.ws.axelaautoapp.WS_Enquiry_List;
import axela.ws.axelaautoapp.WS_Enquiry_List_Filter;
import axela.ws.axelaautoapp.WS_Esc_Enquiry_Followup;
import axela.ws.axelaautoapp.WS_Esc_Preowned_Followup;
import axela.ws.axelaautoapp.WS_Executive_Uploadimage;
import axela.ws.axelaautoapp.WS_ForgotPassword;
import axela.ws.axelaautoapp.WS_GPSData;
import axela.ws.axelaautoapp.WS_HomeData;
import axela.ws.axelaautoapp.WS_Incentive_HomeData;
import axela.ws.axelaautoapp.WS_Notification_List;
import axela.ws.axelaautoapp.WS_Preowned_Eval_List;
import axela.ws.axelaautoapp.WS_Preowned_List;
import axela.ws.axelaautoapp.WS_Preowned_List_Filter;
import axela.ws.axelaautoapp.WS_Preowned_Stock_List;
import axela.ws.axelaautoapp.WS_Preowned_Testdrive_List;
import axela.ws.axelaautoapp.WS_Preowned_Uploadimage;
import axela.ws.axelaautoapp.WS_Print_QuotePDF;
import axela.ws.axelaautoapp.WS_Quote_List_Filter;
import axela.ws.axelaautoapp.WS_Receipt_List;
import axela.ws.axelaautoapp.WS_Salesorder_List_Filter;
import axela.ws.axelaautoapp.WS_Service_HomeData;
import axela.ws.axelaautoapp.WS_SignInData;
import axela.ws.axelaautoapp.WS_Status_Update;
import axela.ws.axelaautoapp.WS_System_PasswordData;
import axela.ws.axelaautoapp.WS_Testdrive_List;
import axela.ws.axelaautoapp.WS_Thumbnail;
import axela.ws.axelaautoapp.WS_Ticket_List;
import axela.ws.axelaautoapp.WS_Veh_Quote_List;
import axela.ws.axelaautoapp.WS_Veh_Salesorder_List;
import axela.ws.runner.WS_Ecover_Exe_Update;
import axela.ws.runner.WS_Ecover_Executive_Dash_Check;
import axela.ws.runner.WS_Executive_Update_Password;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
//////
@Path("/axelaauto")
public class Controller_AxelaautoApp {

	public Controller_AxelaautoApp() {
	}

	@Path("/test")
	@GET
	@Produces("text/html")
	public String test() throws Exception {
		return "AxelaautoApp WS Test Successful. ";
	}

	@Path("/signin")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response createsignin(JSONObject input) throws Exception {
		WS_SignInData signin = new WS_SignInData();
		JSONObject output = signin.signindata(input);
		return Response.status(201).entity(output.toString()).build();
	}

	@Path("/gps")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response creategps(JSONObject input) throws Exception {
		WS_GPSData gps = new WS_GPSData();
		JSONObject output = gps.gps(input);
		return Response.status(201).entity(output.toString()).build();
	}

	@Path("/quote-print")
	@POST
	@Consumes("application/json")
	@Produces("application/pdf")
	public Response sendquotepdf(JSONObject input, @Context HttpServletRequest request) throws Exception {
		WS_Print_QuotePDF printquote = new WS_Print_QuotePDF();
		String loc = printquote.sendquotepdf(input, request);
		File file = new File(loc);
		Response.ResponseBuilder response = Response.ok((Object) file);
		response.header("Content-Disposition", "attachment; filename=new-android.pdf");
		return response.status(201).build();
	}

	@Path("/home")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response createhome(JSONObject input, @Context HttpServletRequest request) throws Exception {
		WS_HomeData homeObj = new WS_HomeData();
		JSONObject output = homeObj.home(input, request);
		return Response.status(201).entity(output.toString()).build();
	}

	@Path("/activity-list")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response activities(JSONObject input, @Context HttpServletRequest request) throws Exception {
		WS_Activity_List activityObj = new WS_Activity_List();
		JSONObject output = activityObj.ActivityList(input, request);
		return Response.status(201).entity(output.toString()).build();
	}

	@Path("/incentive-home")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response incentivehome(JSONObject input, @Context HttpServletRequest request) throws Exception {
		WS_Incentive_HomeData incentivehomeobj = new WS_Incentive_HomeData();
		JSONObject output = incentivehomeobj.IncentiveHome(input, request);
		return Response.status(201).entity(output.toString()).build();
	}

	@Path("/service-home")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response servicehome(JSONObject input, @Context HttpServletRequest request) throws Exception {
		WS_Service_HomeData servicehomeobj = new WS_Service_HomeData();
		JSONObject output = servicehomeobj.servicehome(input, request);
		return Response.status(201).entity(output.toString()).build();
	}

	@Path("/forgotpassword")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response remind(JSONObject input) throws Exception {
		WS_ForgotPassword forgotpwd = new WS_ForgotPassword();
		JSONObject output = forgotpwd.ForgotPassword(input);
		return Response.status(201).entity(output.toString()).build();
	}

	@Path("/thumbnail")
	@GET
	@Produces("image/jpeg")
	public Response displayimage(
			@QueryParam("emp_id") String emp_id,
			@QueryParam("comp_id") String comp_id,
			@QueryParam("image") String image,
			@QueryParam("path") String path,
			@QueryParam("width") String width) throws Exception {
		WS_Thumbnail thumbnail = new WS_Thumbnail();
		Response.ResponseBuilder response = Response.ok((Object) thumbnail.imagedata(emp_id, comp_id, image, path, width));
		return response.build();
	}

	@Path("/esc-enquiry-followup")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response createesc(JSONObject input) throws Exception {
		WS_Esc_Enquiry_Followup esc_enq_followup = new WS_Esc_Enquiry_Followup();
		JSONObject output = esc_enq_followup.EscList(input);
		return Response.status(201).entity(output.toString()).build();
	}

	@Path("/status-update")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response statusUpdate(JSONObject input, @Context HttpServletRequest request) throws Exception {
		WS_Status_Update statusupdate = new WS_Status_Update();
		JSONObject output = statusupdate.Status_Update(input, request);
		return Response.status(201).entity(output.toString()).build();
	}

	@Path("/changepassword")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response systempassword(JSONObject input, @Context HttpServletRequest request) throws Exception {
		WS_System_PasswordData changepwd = new WS_System_PasswordData();
		JSONObject output = changepwd.changepassword(input, request);
		return Response.status(201).entity(output.toString()).build();
	}

	@Path("/upload-exeimage")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	// @Produces("text/plain")
	public Response ImageUpload(
			@Encoded @FormDataParam("emp_id") String emp_id,
			@Encoded @FormDataParam("comp_id") String comp_id,
			@FormDataParam("exeimage") InputStream is,
			@FormDataParam("exeimage") FormDataContentDisposition formData,
			@Context HttpServletRequest request
			) {
		WS_Executive_Uploadimage upload = new WS_Executive_Uploadimage();
		JSONObject output = upload.UploadImage(emp_id, comp_id, is, formData, request);
		return Response.status(200).entity(output).build();
	}

	@Path("/enquiry-list")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response enquirylist(JSONObject input, @Context HttpServletRequest request) throws Exception {
		WS_Enquiry_List enquiryObj = new WS_Enquiry_List();
		JSONObject output = enquiryObj.EnquiryList(input, request);
		return Response.status(201).entity(output.toString()).build();
	}

	@Path("/enquiry-list-filter")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response enquirylistfilter(JSONObject input, @Context HttpServletRequest request) throws Exception {
		WS_Enquiry_List_Filter enquiryfilterObj = new WS_Enquiry_List_Filter();
		JSONObject output = enquiryfilterObj.EnquiryListFilter(input, request);
		return Response.status(201).entity(output.toString()).build();
	}

	@Path("/quote-list")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response quotelist(JSONObject input, @Context HttpServletRequest request) throws Exception {
		WS_Veh_Quote_List quoteObj = new WS_Veh_Quote_List();
		JSONObject output = quoteObj.QuoteList(input, request);
		return Response.status(201).entity(output.toString()).build();
	}

	@Path("/quote-list-filter")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response quotelistfilter(JSONObject input, @Context HttpServletRequest request) throws Exception {
		WS_Quote_List_Filter quotefilterObj = new WS_Quote_List_Filter();
		JSONObject output = quotefilterObj.QuoteListFilter(input, request);
		return Response.status(201).entity(output.toString()).build();
	}

	@Path("/salesorder-list")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response salesorderlist(JSONObject input, @Context HttpServletRequest request) throws Exception {
		WS_Veh_Salesorder_List salesorderObj = new WS_Veh_Salesorder_List();
		JSONObject output = salesorderObj.SalesorderList(input, request);
		return Response.status(201).entity(output.toString()).build();
	}

	@Path("/salesorder-list-filter")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response salesorderlistfilter(JSONObject input, @Context HttpServletRequest request) throws Exception {
		WS_Salesorder_List_Filter salesorderfilterObj = new WS_Salesorder_List_Filter();
		JSONObject output = salesorderfilterObj.SalesorderListFilter(input, request);
		return Response.status(201).entity(output.toString()).build();
	}

	@Path("/testdrive-list")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response testdrivelist(JSONObject input, @Context HttpServletRequest request) throws Exception {
		WS_Testdrive_List salesorderObj = new WS_Testdrive_List();
		JSONObject output = salesorderObj.TestdriveList(input, request);
		return Response.status(201).entity(output.toString()).build();
	}

	@Path("/testdrive-driving-licence")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	// @Produces("text/plain")
	public Response UploadDrivingLicence(
			@Encoded @FormDataParam("emp_id") String emp_id,
			@Encoded @FormDataParam("testdrive_id") String testdrive_id,
			@Encoded @FormDataParam("comp_id") String comp_id,
			@FormDataParam("licenceimg") InputStream is,
			@FormDataParam("licenceimg") FormDataContentDisposition formData,
			@Context HttpServletRequest request
			) {
		WS_Driving_Licence_Upload drivinglicenceObj = new WS_Driving_Licence_Upload();
		JSONObject output = drivinglicenceObj.UploadDrivingLicence(emp_id, testdrive_id, comp_id, is, formData, request);
		return Response.status(200).entity(output).build();
	}

	@Path("/preowned-list")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response preownedlist(JSONObject input, @Context HttpServletRequest request) throws Exception {
		WS_Preowned_List preownedObj = new WS_Preowned_List();
		JSONObject output = preownedObj.PreownedList(input, request);
		return Response.status(201).entity(output.toString()).build();
	}

	@Path("/crm-followup")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response crmfollowup(JSONObject input, @Context HttpServletRequest request) throws Exception {
		WS_CRM_Followup crmObj = new WS_CRM_Followup();
		JSONObject output = crmObj.CRMFollowup(input, request);
		return Response.status(201).entity(output.toString()).build();
	}

	@Path("/preowned-list-filter")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response preownedlistfilter(JSONObject input, @Context HttpServletRequest request) throws Exception {
		WS_Preowned_List_Filter preownedfilterObj = new WS_Preowned_List_Filter();
		JSONObject output = preownedfilterObj.PreownedListFilter(input, request);
		return Response.status(201).entity(output.toString()).build();
	}

	@Path("/preowned-eval-list")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response preownedevallist(JSONObject input, @Context HttpServletRequest request) throws Exception {
		WS_Preowned_Eval_List preownedfilterObj = new WS_Preowned_Eval_List();
		JSONObject output = preownedfilterObj.PreownedEvalList(input, request);
		return Response.status(201).entity(output.toString()).build();
	}

	@Path("/preowned-stock-list")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response preownedstocklist(JSONObject input, @Context HttpServletRequest request) throws Exception {
		WS_Preowned_Stock_List preownedfilterObj = new WS_Preowned_Stock_List();
		JSONObject output = preownedfilterObj.PreownedStockList(input, request);
		return Response.status(201).entity(output.toString()).build();
	}

	@Path("/preowned-testdrive-list")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response preownedtesdrivelist(JSONObject input, @Context HttpServletRequest request) throws Exception {
		WS_Preowned_Testdrive_List preownedtestdrivelistobj = new WS_Preowned_Testdrive_List();
		JSONObject output = preownedtestdrivelistobj.PreownedTestdriveList(input, request);
		return Response.status(201).entity(output.toString()).build();
	}

	@Path("/esc-preowned-followup")
	@POST
	// @Consumes("application/json")
	@Produces("application/json")
	public Response createpreownedesc(JSONObject input) throws Exception {
		WS_Esc_Preowned_Followup esc_preowned_followup = new WS_Esc_Preowned_Followup();
		JSONObject output = esc_preowned_followup.PreownedEscalation(input);
		return Response.status(201).entity(output.toString()).build();
	}

	@Path("/receipt-list")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response receiptlist(JSONObject input, @Context HttpServletRequest request) throws Exception {
		WS_Receipt_List receiptlistobj = new WS_Receipt_List();
		JSONObject output = receiptlistobj.ReceiptList(input, request);
		return Response.status(201).entity(output.toString()).build();
	}

	@Path("/upload-preownedimage")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response preownedImageUpload(
			@Encoded @FormDataParam("emp_id") String emp_id,
			@Encoded @FormDataParam("preowned_id") String preowned_id,
			@Encoded @FormDataParam("img_title") String img_title,
			@Encoded @FormDataParam("comp_id") String comp_id,
			@FormDataParam("preownedimage") InputStream is,
			@FormDataParam("preownedimage") FormDataContentDisposition formData,
			@Context HttpServletRequest request
			) {
		WS_Preowned_Uploadimage upload = new WS_Preowned_Uploadimage();
		JSONObject output = upload.UploadPreownedImage(emp_id, preowned_id, img_title, comp_id, is, formData, request);
		return Response.status(200).entity(output).build();
	}

	@Path("/notification-list")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response notificationlist(JSONObject input, @Context HttpServletRequest request) throws Exception {
		WS_Notification_List notificationlistobj = new WS_Notification_List();
		JSONObject output = notificationlistobj.NotificationList(input, request);
		return Response.status(201).entity(output.toString()).build();
	}

	@Path("/ticket-list")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response ticketlist(JSONObject input, @Context HttpServletRequest request) throws Exception {
		WS_Ticket_List ticketlistobj = new WS_Ticket_List();
		JSONObject output = ticketlistobj.TicketList(input, request);
		return Response.status(201).entity(output.toString()).build();
	}

	// ecover - AxelaAuto Update executive
	@Path("/ecover-exe-update")
	@POST
	@Consumes("application/json")
	@Produces("text/html")
	public String ExeUpdate(JSONObject input, @Context HttpServletRequest request) throws Exception {
		WS_Ecover_Exe_Update exeupdate = new WS_Ecover_Exe_Update();
		return exeupdate.ExeUpdate(input, request);
	}

	// ecover - AxelaAuto Update emp Password
	@Path("/ecover-emppass")
	@POST
	@Consumes("application/json")
	@Produces("text/html")
	public String Updatepassword(JSONObject input, @Context HttpServletRequest request) throws Exception {
		WS_Executive_Update_Password emppass = new WS_Executive_Update_Password();
		return emppass.Executive_Update_Password(input, request);
	}

	@Path("/ecover-exedash-check")
	@POST
	@Consumes("application/json")
	@Produces("text/html")
	public String dashcheck(JSONObject input, @Context HttpServletRequest request) throws Exception {
		WS_Ecover_Executive_Dash_Check emppass = new WS_Ecover_Executive_Dash_Check();
		return emppass.ExecutiveDashCheck(input, request);
	}

	// @Path("/ecover-exeimage")
	// @POST
	// @Consumes(MediaType.MULTIPART_FORM_DATA)
	// // @Produces("text/plain")
	// public Response ExeImageUpload(
	// @Encoded @FormDataParam("emp_id") String emp_id,
	// @Encoded @FormDataParam("comp_id") String comp_id,
	// @FormDataParam("exeimage") InputStream is,
	// @FormDataParam("exeimage") FormDataContentDisposition formData,
	// @Context HttpServletRequest request
	// ) {
	// try {
	// System.out.println("is size ========= " + is.available());
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// WS_ExecutiveImage_Upload imgupload = new WS_ExecutiveImage_Upload();
	// JSONObject output = imgupload.ExecutiveImageUpload(emp_id, comp_id, is, formData);
	// return Response.status(200).entity(output).build();
	// }

	// @Path("/test-executives")
	// @POST
	// @Consumes("application/plain")
	// @Produces("text/html")
	// public String test(String input, @Context HttpServletRequest request) throws Exception {
	// System.out.println("coming======");
	// WS_Test emppass = new WS_Test();
	// return emppass.test(input, request);
	// }
	//
	// @Path("/test-images")
	// @POST
	// @Consumes(MediaType.MULTIPART_FORM_DATA)
	// public Response testSample(FormDataMultiPart formData, @Context HttpServletRequest request) {
	// System.out.println("coming--------");
	// WS_Test test = new WS_Test();
	// String output = test.testSample(formData, request);
	// // String output = test.testSample(is, formData, request);
	// return Response.status(200).entity(output).build();
	// }
	//
	// @Path("/test-images")
	// @POST
	// @Consumes(MediaType.MULTIPART_FORM_DATA)
	// public Response testSample(@FormDataParam("3") InputStream is,
	// @FormDataParam("3") FormDataContentDisposition formData, @Context HttpServletRequest request) {
	// System.out.println("coming--------");
	// WS_Test test = new WS_Test();
	// String output = test.testSample(is, formData, request);
	// return Response.status(200).entity(output).build();
	// }
}
