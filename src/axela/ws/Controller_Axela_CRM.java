package axela.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

public class Controller_Axela_CRM {

	@Path("/test")
	@GET
	@Produces("text/html")
	public String test() throws Exception {
		return "Axela-CRM WS Test Successful. ";
	}
}
