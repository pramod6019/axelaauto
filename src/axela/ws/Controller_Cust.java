package axela.ws;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONObject;

import axela.ws.cust.WS_AboutUs;
import axela.ws.cust.WS_AppVersion;
//import axela.ws.cust.WS_Book_A_Car;
import axela.ws.cust.WS_Enquiry_Model_ItemData;
import axela.ws.cust.WS_FeatureDescription;
import axela.ws.cust.WS_Feature_List;
import axela.ws.cust.WS_Model_Colour_List;
import axela.ws.cust.WS_Model_List;
import axela.ws.cust.WS_Offers_List;
import axela.ws.cust.WS_Register_User;
import axela.ws.cust.WS_ServiceCenter_List;
import axela.ws.cust.WS_Service_Add;
import axela.ws.cust.WS_Showroom_List;
import axela.ws.cust.WS_Testdrive_Add;

@Path("/cust")
public class Controller_Cust {

	public Controller_Cust() {
	}

	@Path("/test")
	@GET
	@Produces("text/html")
	public String test() throws Exception {
		return "WS Customer Test Successful.";
	}

	@Path("/app-version")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response version(JSONObject input) throws Exception {
		WS_AppVersion home = new WS_AppVersion();
		JSONObject output = home.appversion(input);
		return Response.status(201).entity(output.toString()).build();
	}

	@Path("/register-user")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response createregisteruser(JSONObject input) throws Exception {
		WS_Register_User user = new WS_Register_User();
		JSONObject output = user.registeruserdata(input);
		return Response.status(201).entity(output.toString()).build();
	}

	@Path("/testdrive-add")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response oppradd(JSONObject input) throws Exception {
		WS_Testdrive_Add td = new WS_Testdrive_Add();
		JSONObject output = td.testdriveadd(input);
		return Response.status(201).entity(output.toString()).build();
	}

	@Path("/model-item")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response createmodelitem(JSONObject input) throws Exception {
		WS_Enquiry_Model_ItemData model = new WS_Enquiry_Model_ItemData();
		JSONObject output = model.Enquiry_Model_Item(input);
		return Response.status(201).entity(output.toString()).build();
	}

	@Path("/service-add")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response serviceadd(JSONObject input) throws Exception {
		WS_Service_Add service = new WS_Service_Add();
		JSONObject output = service.serviceadd(input);
		return Response.status(201).entity(output.toString()).build();
	}

	@Path("/model-list")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response createmodeldata(JSONObject input) throws Exception {
		WS_Model_List model = new WS_Model_List();
		JSONObject output = model.modeldata(input);
		return Response.status(201).entity(output.toString()).build();
	}
	@Path("/model-colour-list")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response listcolourdata(JSONObject input) throws Exception {
		WS_Model_Colour_List desc = new WS_Model_Colour_List();
		JSONObject output = desc.listcolourdata(input);
		return Response.status(201).entity(output.toString()).build();
	}

	@Path("/feature-list")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response createfeaturedata(JSONObject input) throws Exception {
		WS_Feature_List feature = new WS_Feature_List();
		JSONObject output = feature.featuredata(input);
		return Response.status(201).entity(output.toString()).build();
	}

	@Path("/offers-list")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response createofferslist(JSONObject input) throws Exception {
		WS_Offers_List list = new WS_Offers_List();
		JSONObject output = list.listoffers(input);
		return Response.status(201).entity(output.toString()).build();
	}

	@Path("/showroom-list")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response showroomlist(JSONObject input) throws Exception {
		WS_Showroom_List list = new WS_Showroom_List();
		JSONObject output = list.listshowrooms(input);
		return Response.status(201).entity(output.toString()).build();
	}

	@Path("/servicecenter-list")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response servicecentrelist(JSONObject input) throws Exception {
		WS_ServiceCenter_List list = new WS_ServiceCenter_List();
		JSONObject output = list.listservicecentres(input);
		return Response.status(201).entity(output.toString()).build();
	}

	@Path("/feature-desc")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response createfeaturedesc(JSONObject input) throws Exception {
		WS_FeatureDescription desc = new WS_FeatureDescription();
		JSONObject output = desc.featuredescdata(input);
		return Response.status(201).entity(output.toString()).build();
	}
	// @Path("/book-a-car")
	// @POST
	// @Consumes("application/json")
	// @Produces("application/json")
	// public Response bookacar(JSONObject input) throws Exception {
	// WS_Book_A_Car desc = new WS_Book_A_Car();
	// JSONObject output = desc.bookacar(input);
	// return Response.status(201).entity(output.toString()).build();
	// }

	@Path("/aboutus")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response createaboutusdata(JSONObject input) throws Exception {
		WS_AboutUs abtus = new WS_AboutUs();
		JSONObject output = abtus.aboutusdata(input);
		return Response.status(201).entity(output.toString()).build();
	}

}
