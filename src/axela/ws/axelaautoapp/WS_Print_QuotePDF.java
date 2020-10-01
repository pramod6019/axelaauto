package axela.ws.axelaautoapp;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.codehaus.jettison.json.JSONObject;

import axela.sales.Veh_Quote_Print_PDF;
import cloudify.connect.Connect;

public class WS_Print_QuotePDF extends Connect {

	public HttpServletRequest request;
	public HttpServletResponse response;
	public static String quote_id = "0";
	public String comp_id = "0";
	public String emp_uuid = "0";
	public String addB = "", fileLocation = "";
	// // File f = null;
	File file = null;

	public String sendquotepdf(JSONObject input, @Context HttpServletRequest request) throws Exception {
		if (!input.isNull("quote_id")) {
			quote_id = CNumeric(PadQuotes((String) input.get("quote_id")));
		}
		Veh_Quote_Print_PDF track = new Veh_Quote_Print_PDF();
		track.BranchAccess = "";
		track.ExeAccess = "";
		track.QuoteDetails(comp_id, request, response, quote_id, "", "", "file");
		fileLocation = CachePath(comp_id) + "Quote_" + quote_id + ".pdf";
		return fileLocation;
	}
}
