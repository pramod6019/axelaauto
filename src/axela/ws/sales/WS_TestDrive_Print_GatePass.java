package axela.ws.sales;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONObject;

import axela.sales.TestDrive_Print_GatePass;
import cloudify.connect.Connect;

public class WS_TestDrive_Print_GatePass extends Connect {

    public HttpServletRequest request;
    public HttpServletResponse response;
    public String comp_id = "0";
    public String addB = "", fileLocation = "";
    public static String testdrive_id = "0"; 
    File f = null;
//    File file = null;

    public String sendgatepasspdf(JSONObject input) throws Exception {
        SOP("TestDrive_SendPdf input = " + input);
        if (!input.isNull("testdrive_id")) {
        	testdrive_id = CNumeric(PadQuotes((String) input.get("testdrive_id")));
        }
        if (!input.isNull("comp_id")) {
        	comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
        }
        TestDrive_Print_GatePass gatepass = new TestDrive_Print_GatePass(); 
        gatepass.BranchAccess = "";
        gatepass.ExeAccess = "";
        gatepass.comp_id = comp_id;
        gatepass.StrSearch =  " AND testdrive_id = " + testdrive_id + "";
        SOP("testdriveid==="+testdrive_id);
        gatepass.testdrive_id = testdrive_id;
        SOP("1");
        gatepass.InvoiceDetails(request, response, "file");
        SOP("output");
        fileLocation = CachePath(comp_id) + "gatepass_" + testdrive_id + ".pdf";
        return fileLocation;   
    }
}
 