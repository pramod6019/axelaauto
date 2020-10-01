package axela.ws.sales;
//divya 26th march 2014

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import axela.sales.Veh_Quote_Print_PDF;
import cloudify.connect.ConnectWS;

import com.google.gson.Gson;

public class WS_Veh_Quote_Email extends ConnectWS {

    public String StrSql = "";
    public String CountSql = "";
    public String SqlJoin = "";
    public int TotalRecords = 0;
    public String pagecurrent = "";
    public String emp_uuid = "0";
    public String branch_id = "0";
    public String quote_id = "0";
    public String comp_email_enable = "", attachment = "", insurcompattachment = "";
    public String branch_quote_email_enable = "";
    public String branch_quote_email_format = "";
    public String branch_quote_email_sub = "";
    public String branch_quote_email_exe_sub = "";
    public String branch_quote_email_exe_format = "";
    public String config_admin_email = "";
    public String config_email_enable = "";
    public String quote_contact_id = "0";
    public String contact_name = "";
    public String contact_email1 = "";
    public String inscomp_value = "";
    public String inscomp_title = "";
    public String msg = "";
    public String keyword_search = "", keyword = "";
    public String emp_id = "0";
    public String comp_id = "0";
    public String config_sales_enquiry_refno = "";
    DecimalFormat df = new DecimalFormat("0.00");
    Gson gson = new Gson();
    JSONObject output = new JSONObject();
    ArrayList<String> list = new ArrayList<>();
    Map<String, String> map = new HashMap<>();

    public JSONObject QuoteEmail(JSONObject input) throws Exception {
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
        if (!input.isNull("quote_id")) {
            quote_id = PadQuotes((String) input.get("quote_id"));
        }
        PopulateFields();

        try {
            if (!quote_id.equals("0")) {
                if (comp_email_enable.equals("1")
                        && config_email_enable.equals("1")
                        && !config_admin_email.equals("")
                        && branch_quote_email_enable.equals("1")
                        && !contact_email1.equals("")
                        && !branch_quote_email_format.equals("")
                        && !branch_quote_email_sub.equals("")) {
                    attachment = CachePath(comp_id) + "Quote_" + quote_id + ".pdf,Quote_" + quote_id + ".pdf";
                    if (!inscomp_value.equals("") && !inscomp_title.equals("")) {
                        insurcompattachment = InsurCompDocPath(comp_id) + inscomp_value + "," + inscomp_title + "" + fileext(inscomp_value);
                        attachment = attachment + ";" + insurcompattachment;
                    }
                    new Veh_Quote_Print_PDF().QuoteDetails(comp_id, null, null, quote_id, "", "", "file");
                    SendEmail();
                    msg= "Email sent successfully!";
                    output.put("msg", msg);
                } else {
                    if (!comp_email_enable.equals("1")) {
                        msg = msg + "<br>Email Option is Disabled!";
                    }
                    if (!config_email_enable.equals("1")) {
                        msg = msg + "<br>Email Gateway is Disabled!";
                    }
                    if (config_admin_email.equals("")) {
                        msg = msg + "<br>Admin Email is Blank!";
                    }
                    if (contact_email1.equals("")) {
                        msg = msg + "<br>Contact Email is Blank!";
                    }
                    if (!branch_quote_email_enable.equals("1")) {
                        msg = msg + "<br>Quote Email Option is Disabled!";
                    }
                    if (branch_quote_email_format.equals("")) {
                        msg = msg + "<br>Quote Email Format is Blank";
                    }
                    if (branch_quote_email_sub.equals("")) {
                        msg = msg + "<br>Quote Email Subject is Blank";
                    }
//                    response.sendRedirect("../portal/error.jsp?msg=" + msg + "");
                    output.put("msg", msg);
                }
            } else {
                output.put("msg", "Error");
//                response.sendRedirect("../portal/error.jsp?msg=" + msg + "");
            }
            SOP("output = " + output);

        } catch (Exception ex) {
            SOPError("Axelaauto ==" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
//        }
//        }
        return output;
    }

    protected void SendEmail() throws SQLException {
        String msg, sub;
        msg = (branch_quote_email_format);
        sub = (branch_quote_email_sub);

        sub = "REPLACE('" + sub + "','[QUOTEID]', quote_id)";
        sub = "REPLACE(" + sub + ",'[CUSTOMERID]', customer_id)";
        sub = "REPLACE(" + sub + ",'[CUSTOMERNAME]', customer_name)";
        sub = "REPLACE(" + sub + ",'[CONTACTNAME]', CONCAT(title_desc, ' ', contact_fname,' ', contact_lname))";
        sub = "REPLACE(" + sub + ",'[CONTACTJOBTITLE]', contact_jobtitle)";
        sub = "REPLACE(" + sub + ",'[CONTACTMOBILE1]', contact_mobile1)";
        sub = "REPLACE(" + sub + ",'[CONTACTPHONE1]', contact_phone1)";
        sub = "REPLACE(" + sub + ",'[CONTACTEMAIL1]', contact_email1)";
        sub = "REPLACE(" + sub + ",'[EXENAME]', emp_name)";
        sub = "REPLACE(" + sub + ",'[EXEJOBTITLE]', jobtitle_desc)";
        sub = "REPLACE(" + sub + ",'[EXEMOBILE1]', emp_mobile1)";
        sub = "REPLACE(" + sub + ",'[EXEPHONE1]', emp_phone1)";
        sub = "REPLACE(" + sub + ",'[EXEEMAIL1]', emp_email1)";
        sub = "REPLACE(" + sub + ",'[MODELNAME]', model_name)";
        sub = "REPLACE(" + sub + ",'[ITEMNAME]', item_name)";

        msg = "REPLACE('" + msg + "','[QUOTEID]', quote_id)";
        msg = "REPLACE(" + msg + ",'[CUSTOMERID]', customer_id)";
        msg = "REPLACE(" + msg + ",'[CUSTOMERNAME]', customer_name)";
        msg = "REPLACE(" + msg + ",'[CONTACTNAME]', CONCAT(title_desc, ' ', contact_fname,' ', contact_lname))";
        msg = "REPLACE(" + msg + ",'[CONTACTJOBTITLE]', contact_jobtitle)";
        msg = "REPLACE(" + msg + ",'[CONTACTMOBILE1]', contact_mobile1)";
        msg = "REPLACE(" + msg + ",'[CONTACTPHONE1]', contact_phone1)";
        msg = "REPLACE(" + msg + ",'[CONTACTEMAIL1]', contact_email1)";
        msg = "REPLACE(" + msg + ",'[EXENAME]', emp_name)";
        msg = "REPLACE(" + msg + ",'[EXEJOBTITLE]', jobtitle_desc)";
        msg = "REPLACE(" + msg + ",'[EXEMOBILE1]', emp_mobile1)";
        msg = "REPLACE(" + msg + ",'[EXEPHONE1]', emp_phone1)";
        msg = "REPLACE(" + msg + ",'[EXEEMAIL1]', emp_email1)";
        msg = "REPLACE(" + msg + ",'[MODELNAME]', model_name)";
        msg = "REPLACE(" + msg + ",'[ITEMNAME]', item_name)";

        try {

//            postMail(contact_email1, config_admin_email, "info@emax.in", config_admin_email, sub, msg, attachment);
            StrSql = "SELECT"
                    + " '" + quote_contact_id + "',"
                    + " '" + contact_name + "',"
                    + " '" + config_admin_email + "',"
                    + " '" + contact_email1 + "',"
                    + " " + unescapehtml(sub) + ","
                    + " " + unescapehtml(msg) + ","
                    + " '" + attachment.replace("\\", "/") + "',"
                    + " '" + ToLongDate(kknow()) + "',"
                    + " " + emp_id + ","
                    + " 0"
                    + " FROM " + compdb(comp_id) + "axela_sales_quote"
                    + " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = quote_customer_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = quote_contact_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_emp ON quote_emp_id = emp_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_title ON contact_title_id = title_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON emp_jobtitle_id = jobtitle_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_sales_quote_item ON quoteitem_quote_id = quote_id"
                    + " AND quoteitem_rowcount != 0"
                    + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = quoteitem_item_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = quote_enquiry_id "
                    + " WHERE quote_id = " + quote_id + "";

            StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email"
                    + " (email_contact_id,"
                    + " email_contact,"
                    + " email_from,"
                    + " email_to,"
                    + " email_subject,"
                    + " email_msg,"
                    + " email_attach1,"
                    + " email_date,"
                    + " email_entry_id,"
                    + " email_sent)"
                    + " " + StrSql + "";
            updateQuery(StrSql);
        } catch (Exception e) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
        }
    }

    public void PopulateFields() {
        StrSql = "SELECT " + compdb(comp_id) + "axela_sales_quote.*, contact_lname, contact_fname, title_desc, contact_email1,"
                + " COALESCE(branch_quote_email_enable, '') AS branch_quote_email_enable,"
                + " COALESCE(branch_quote_sms_format, '') AS branch_quote_sms_format,"
                + " COALESCE(branch_quote_sms_enable, '') AS branch_quote_sms_enable,"
                + " COALESCE(branch_quote_email_sub, '') AS branch_quote_email_sub,"
                + " COALESCE(branch_quote_email_format, '') AS branch_quote_email_format,"
                + " config_admin_email, config_email_enable, config_sms_enable,"
                + " config_sales_quote_refno, comp_email_enable, comp_sms_enable,"
                + " emp_quote_priceupdate, emp_quote_discountupdate, COALESCE(inscomp_value,'') inscomp_value,"
                + " COALESCE(inscomp_title,'') inscomp_title"
                + " FROM " + compdb(comp_id) + "axela_sales_quote"
                + " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = quote_contact_id"
                + " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
                + " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
                + " LEFT JOIN " + compdb(comp_id) + "axela_insurance_comp ON inscomp_id = quote_inscomp_id"
                + " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = quote_branch_id"
                + " LEFT JOIN " + compdb(comp_id) + "axela_emp ON emp_id = " + emp_id + ","
                + " " + compdb(comp_id) + "axela_config,"
                + " " + compdb(comp_id) + "axela_comp"
                + " WHERE quote_id = " + quote_id;
        CachedRowSet crs =processQuery(StrSql, 0);
//        SOP("strsql==config=="+StrSql);
        try {
            while (crs.next()) {
                branch_id = crs.getString("quote_branch_id");
                quote_contact_id = crs.getString("quote_contact_id");
                contact_name = crs.getString("title_desc") + " " + crs.getString("contact_fname") + " " + crs.getString("contact_lname");
                contact_email1 = crs.getString("contact_email1");
                branch_quote_email_enable = crs.getString("branch_quote_email_enable");
                branch_quote_email_format = crs.getString("branch_quote_email_format");
                branch_quote_email_sub = crs.getString("branch_quote_email_sub");
                config_admin_email = crs.getString("config_admin_email");
                config_email_enable = crs.getString("config_email_enable");
                comp_email_enable = crs.getString("comp_email_enable");
                inscomp_value = crs.getString("inscomp_value");
                inscomp_title = crs.getString("inscomp_title");
            }
            crs.close();
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }
}
