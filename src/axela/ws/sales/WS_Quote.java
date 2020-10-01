package axela.ws.sales;
//divya 26th march 2014

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.ConnectWS;
import cloudify.connect.Encrypt;

import com.google.gson.Gson;

public class WS_Quote extends ConnectWS {

    public String StrSql = "";
    public String CountSql = "";
    public String SqlJoin = "";
    public int TotalRecords = 0;
    public String pagecurrent = "";
    public String emp_id = "0";
    public String comp_id = "0";
    public String model_id = "0";
    public String item_id = "0";
    public String enquiry_id = "0";
    public String quote_url = "";
    public String emp_uuid = "0", timeout = "";
    Encrypt encrypt = new Encrypt();

    Gson gson = new Gson();
    JSONObject output = new JSONObject();
    ArrayList<String> list = new ArrayList<>();
    Map<String, String> map = new HashMap<>();

    public JSONObject Quote(JSONObject input) throws Exception {
        String confirmed = "";
        SOP("input = " + input);
        if (!input.isNull("emp_id")) {
            emp_id = CNumeric(PadQuotes((String) input.get("emp_id")));
        }
        if (!input.isNull("comp_id")) {
        	comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
        }
        if (!input.isNull("emp_uuid")) {
        	emp_uuid = CNumeric(PadQuotes((String) input.get("emp_uuid")));
        }
        if (!input.isNull("enquiry_id")) {
            enquiry_id = CNumeric(PadQuotes((String) input.get("enquiry_id")));
        }
        timeout = ToLongDate(AddHoursDate(kknow(), 0, 48, 0));

        try {
            StrSql = "Select emp_uuid FROM " + compdb(comp_id) + "axela_emp"
                    + " WHERE emp_id= " + emp_id;
            emp_uuid = ExecuteQuery(StrSql);
            quote_url = AppURL() + "portal/user-authentication.jsp?data="
                    + URLEncoder.encode(encrypt.encrypt("timeout=" + timeout + "&user=" + emp_uuid + "&redirect=sales/veh-quote-add.jsp?enquiry_id@#$" + enquiry_id), "UTF-8");
            output.put("quote_url", quote_url);
            SOP("output = " + output);

        } catch (Exception ex) {
            SOPError("Axelaauto ==" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }

        return output;
    }

}
