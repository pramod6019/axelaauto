package axela.ws.sales;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONObject;

import axela.sales.Enquiry_TrackingCard;
import cloudify.connect.Connect;

public class WS_Enquiry_Print extends Connect {

    public HttpServletRequest request;
    public HttpServletResponse response;
    public static String enquiry_id = "0";
    public String emp_uuid = "0";
    public String comp_id = "0";
    public String addB = "";
    File f = null;

    public File sendpdf(JSONObject input) throws Exception {

        SOP("Enquiry_SendPdf input = " + input);
        if (!input.isNull("enquiry_id")) {
            enquiry_id = CNumeric(PadQuotes((String) input.get("enquiry_id")));
        }
        if (!input.isNull("comp_id")) {
        	comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
        }
        if (!input.isNull("emp_uuid")) {
        	emp_uuid = CNumeric(PadQuotes((String) input.get("emp_uuid")));
        }
        //SOP("enquiry_id=====" + enquiry_id);      
        Enquiry_TrackingCard track = new Enquiry_TrackingCard();
        track.EnquiryDetails(request, response, enquiry_id, "", "", "file");
        f = track.f1;
        return f;
    }
}
