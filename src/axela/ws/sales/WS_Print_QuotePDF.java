package axela.ws.sales;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
//    File f = null;
    File file = null;

    public String sendquotepdf(JSONObject input) throws Exception {
        SOP("Enquiry_SendPdf input = " + input);
        if (!input.isNull("quote_id")) {
            quote_id = CNumeric(PadQuotes((String) input.get("quote_id")));
        }
        if (!input.isNull("comp_id")) {
        	comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
        }
        Veh_Quote_Print_PDF track = new Veh_Quote_Print_PDF(); 
        track.BranchAccess = "";
        track.ExeAccess = "";
        track.QuoteDetails(comp_id, request, response, quote_id, "", "", "file");
        fileLocation = CachePath(comp_id) + "Quote_" + quote_id + ".pdf";
        return fileLocation; 
    }
}
