package axela.ws.sales;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONObject;

import axela.sales.Enquiry_TrackingCard;
import cloudify.connect.Connect;

public class WS_PrintPDF extends Connect {

    public HttpServletRequest request;
    public HttpServletResponse response;
    public static String enquiry_id = "0";
    public String comp_id = "0";
    public String emp_uuid = "0";
    public String addB = "", fileLocation = "";
//    File f = null;
    File file = null;

    public String sendpdf(JSONObject input) throws Exception {
        SOP("Enquiry_SendPdf input = " + input);
        if (!input.isNull("comp_id")) {
        	comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
        }
        if (!input.isNull("enquiry_id")) {
            enquiry_id = CNumeric(PadQuotes((String) input.get("enquiry_id")));
        }
        Enquiry_TrackingCard track = new Enquiry_TrackingCard();
        track.BranchAccess = "";
        track.ExeAccess = "";
        track.comp_id = comp_id;
        track.EnquiryDetails(request, response, enquiry_id, "", "", "file");
        fileLocation = CachePath(comp_id) + "Enquiry_" + enquiry_id + ".pdf";
        return fileLocation; 
    }
}
