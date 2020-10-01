package axela.ws.sales;

//package axela.mobile.sales;
////divya 26th march 2014
//
//import java.util.Date;
//import cloudify.connect.ConnectWS;
//import com.google.gson.Gson;
//import java.math.BigDecimal;
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//import org.codehaus.jettison.json.JSONException;
//import org.codehaus.jettison.json.JSONObject;
//
//public class WSVeh_Quote_Add extends ConnectWS {
//
//    public String add = "";
//    public String addB = "";
//    public String status = "";
//    public String msg = "";
//    public String chkpermmsg = "";
//    public String StrSql = "";
//    public String emp_id = "0";
//    public String comp_id = "0";
//    public String emp_role_id = "0";
//    public String entry_by = "", entry_date = "", modified_by = "", modified_date = "";
//    public String BranchAccess = "", branch_id = "";
//    public Connection conntx = null;
//    Statement stmttx = null;
//    public String QueryString = "";
//    public String testdrive_id = "";
//    public String testdrive_veh_id = "";
//    public String testdrive_emp_id = "";
//    public String testdrive_location_id = "";
//    public String testdrive_type_id = "";
//    public String testdrive_time = "", testdrive_time_from = "", testdrive_time_to = "";
//    public String testdrive_confirmed = "", unconfirm = "";
//    public String testdrive_notes = "";
//    public String testdrive_entry_id = "";
//    public String testdrive_entry_date = "";
//    public String enquiry_id = "";
//    public String testdrive_modified_id = "";
//    public String testdrive_modified_date = "";
//    public String item_id = "";
//    public String model_id = "";
//    public String quote_enquiry_id = "";
//    public String customer_name = "", customer_email1 = "", customer_pin = "", customer_address = "";
//    public String model_name = "";
//    public String executive_name = "";
//    public String strHTML = "";
//    public String enquiry_no = "", enquiry_date = "", enquiry_name = "";
//    public String pop = "";
////    public String testdrivedate = "";
//    // ws
//    Gson gson = new Gson();
//    JSONObject output = new JSONObject();
//    ArrayList<String> list = new ArrayList<String>();
//    Map<String, String> map = new HashMap<String, String>();
//
//    public JSONObject QuoteAdd(JSONObject input) throws Exception {
//        if (AppRun().equals("0")) {
//            SOP("input = " + input);
//        }
//        if (!input.isNull("emp_id")) {
//            emp_id = CNumeric(PadQuotes((String) input.get("emp_id")));
//        }
//        if (!input.isNull("add")) {
//            add = PadQuotes((String) input.get("add"));
//        }
//        if (!input.isNull("addB")) {
//            addB = PadQuotes((String) input.get("addB"));
//        }
//
//        if (!emp_id.equals("0")) {
//            if ("yes".equals(add)) {
//                if (!"yes".equals(addB)) {
//                    if (!input.isNull("quote_enquiry_id")) {
//                        quote_enquiry_id = PadQuotes((String) input.get("quote_enquiry_id"));
//                    }
//                    EnquiryDetails();
//                    PopulateModel();
//                    PopulateItem(model_id);
//                } else {
//                    GetValues(input);
//                    testdrive_entry_id = emp_id;
//                    testdrive_entry_date = ToLongDate(kknow());
//                    AddFields();
//                }
//            }
//            try {
//
//            } catch (Exception ex) {
//                SOPError("Axelaauto ==" + this.getClass().getName());
//                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
//            }
//        }
//        if (AppRun().equals("0")) {
//            SOP("output = " + output);
//        }
//        return output;
//    }
//
////    public void doGet(HttpServletRequest request, HttpServletResponse response)
////            throws ServletException, IOException {
////        doPost(request, response);
////    }
//
//    protected void AddFields() throws SQLException, JSONException {
//        try {
//
//        } catch (Exception e) {
//            SOPError("Axelaauto ==" + this.getClass().getName());
//            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
//        }
//    }
//
//    public void EnquiryDetails() {
//        try {
//            StrSql = "SELECT enquiry_model_id, enquiry_item_id"
//                    + " FROM " + compdb(comp_id) + "axela_sales_enquiry"
//                    + " WHERE enquiry_id = " + quote_enquiry_id + ""
//                    //                    + BranchAccess.replace("branch_id", "enquiry_branch_id") + ""
//                    + WSCheckExeAccess(emp_id).replace("emp_id", "enquiry_emp_id") + "";
//            CachedRowSet crs =processQuery(StrSql, 0);
//
//            if (crs.isBeforeFirst()) {
//                while (crs.next()) {
//                    item_id = crs.getString("enquiry_item_id");
//                    model_id = crs.getString("enquiry_model_id");
//                }
//            } else {
//                output.put("msg", "Invalid Enquiry!");
//            }
//            crs.close();
//        } catch (Exception ex) {
//            SOPError("Axelaauto== " + this.getClass().getName());
//            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
//        }
//
//    }
//
//    public JSONObject PopulateModel() {
//        try {
//            StrSql = "SELECT model_id, model_name"
//                    + " FROM " + compdb(comp_id) + "axela_inventory_item_model"
//                    + " WHERE model_active = 1"
//                    + " ORDER BY model_name";
//            CachedRowSet crs =processQuery(StrSql, 0);
//            if (crs.isBeforeFirst()) {
//                map.put("model_id", "0");
//                map.put("model_name", "Select");
//                list.add(gson.toJson(map));
//                while (crs.next()) {
//                    map.put("model_id", crs.getString("model_id"));
//                    map.put("model_name", unescapehtml(crs.getString("model_name")));
//                    list.add(gson.toJson(map));
//                }
//            } else {
//                map.put("model_id", "0");
//                map.put("model_name", "Select");
//                list.add(gson.toJson(map));
//            }
//            map.clear();
//            output.put("populatemodel", list);
//            list.clear();
//            crs.close();
//        } catch (Exception ex) {
//            SOPError("Axelaauto== " + this.getClass().getName());
//            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
//        }
//        return output;
//    }
//
//    public JSONObject PopulateItem(String model_id) {
//        try {
//            StrSql = "SELECT item_id, IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS item_name"
//                    + " FROM " + compdb(comp_id) + "axela_inventory_item"
//                    + " WHERE item_model_id = " + model_id + ""
//                    + " AND item_model_id != 0"
//                    + " AND item_type_id = 1"
//                    + " AND item_active = 1"
//                    + " ORDER BY item_name";
//            CachedRowSet crs =processQuery(StrSql, 0);
//            if (crs.isBeforeFirst()) {
//                map.put("item_id", "0");
//                map.put("item_name", "Select");
//                list.add(gson.toJson(map));
//                while (crs.next()) {
//                    map.put("item_id", crs.getString("item_id"));
//                    map.put("item_name", unescapehtml(crs.getString("item_name")));
//                    list.add(gson.toJson(map));
//                }
//            } else {
//                map.put("item_id", "0");
//                map.put("item_name", "Select");
//                list.add(gson.toJson(map));
//            }
//            map.clear();
//            output.put("populateitem", list);
//            list.clear();
//        } catch (Exception ex) {
//            SOPError("Axelaauto== " + this.getClass().getName());
//            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
//        }
//        return output;
//    }
//
//    public void GetConfigurationDetails(String item_id, String branch_id, String vehstock_id, String emp_quote_discountupdate) {
//        String quote_id = "0";
//        String item_price = "";
//        String item_netdisc = "";
//        double price_amount = 0, so_total_disc = 0;
//        try {
//            // main items
//            StrSql = "SELECT IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS item_name,"
//                    + " price_amt, item_small_desc, price_disc, COALESCE(tax_id, 0) AS tax_id,"
//                    + " COALESCE(quoteitem_disc, 0) AS quoteitem_disc,"
//                    + " COALESCE(quoteitem_id, 0) AS quoteitem_id,"
//                    + " COALESCE(tax_value, 0) AS tax_value, model_id"
//                    + " FROM " + compdb(comp_id) + "axela_inventory_item"
//                    + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
//                    + " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = " + branch_id + ""
//                    + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = item_id"
//                    + " AND price_rateclass_id	 = branch_rateclass_id	"
//                    + " AND price_effective_from <= " + ToLongDate(kknow()) + ""
//                    + " AND price_active = '1'"
//                    + " LEFT JOIN " + compdb(comp_id) + "axela_sales_quote_item ON quoteitem_item_id = item_id"
//                    + " AND quoteitem_rowcount != 0"
//                    + " AND quoteitem_quote_id = " + quote_id + ""
//                    + " LEFT JOIN " + compdb(comp_id) + "axela_acc_tax ON tax_id = price_tax_id"
//                    + " WHERE item_id = " + CNumeric(item_id) + ""
//                    + " ORDER BY price_effective_from DESC"
//                    + " LIMIT 1";
//            CachedRowSet crs1 =processQuery(StrSql, 0);
//            if (crs1.isBeforeFirst()) {
//                while (crs1.next()) {
//
//                    map.put("model_id", crs1.getString("model_id"));
//                    map.put("item_name", crs1.getString("item_name"));
//                    map.put("item_small_desc", crs1.getString("item_small_desc"));
//                    item_price = crs1.getString("price_amt");
//                    price_amount = Double.parseDouble(CNumeric(item_price));
//                    map.put("item_price", crs1.getString("item_price"));
//                    map.put("price_amount", price_amount + "");
//                    map.put("item_netprice", new BigDecimal((price_amount * crs1.getDouble("tax_value") / 100) + price_amount).toString());
//                    map.put("item_tax_id", crs1.getString("tax_id"));
//                    map.put("item_tax_rate", crs1.getString("tax_value"));
//                    if (!crs1.getString("quoteitem_id").equals("0")) {
//                        map.put("item_netdisc", crs1.getString("quoteitem_disc"));
//                    } else {
//                        map.put("item_netdisc", Integer.toString(crs1.getInt("price_disc")));
//                    }
//
////                    if (!PadQuotes(request.getParameter("txt_item_disc")).equals("")) {
////                        map.put("item_netdisc", CNumeric(PadQuotes(request.getParameter("txt_item_disc"))));
////                    }
////
////                    if (!PadQuotes(request.getParameter("div_main_item_amount")).equals("")) {
////                        map.put("item_netamount", CNumeric(PadQuotes(request.getParameter("div_main_item_amount"))));
////                    }
////
////                    if (!PadQuotes(request.getParameter("div_main_item_amountwod")).equals("")) {
////                        map.put("item_netamountwod", CNumeric(PadQuotes(request.getParameter("div_main_item_amountwod"))));
////                    }
//                    so_total_disc = Double.parseDouble(item_netdisc);
//                    map.put("so_total_disc", so_total_disc + "");
//                    map.put("item_tax", new BigDecimal((price_amount - so_total_disc) * crs1.getDouble("tax_value") / 100).toString());
//                    map.put("item_netamount", new BigDecimal(((price_amount - so_total_disc) * crs1.getDouble("tax_value") / 100)
//                            + (price_amount - so_total_disc)).toString());
//                    map.put("item_netamountwod ", new BigDecimal((price_amount * crs1.getDouble("tax_value") / 100)
//                            + price_amount).toString());
//                }
//            }
//            crs1.close();
//
//            // ex-showroom
//        } catch (Exception ex) {
//            SOPError("Axelaauto== " + this.getClass().getName());
//            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
//        }
//
//    }
//    
//    protected void GetValues(JSONObject input) throws JSONException {
//        if (!input.isNull("item_id")) {
//            item_id = CNumeric(PadQuotes((String) input.get("item_id")));
//        }
//        if (!input.isNull("item_small_desc")) {
//            item_small_desc = CNumeric(PadQuotes((String) input.get("item_small_desc")));
//        }
//        if (!input.isNull("model_id")) {
//            model_id = CNumeric(PadQuotes((String) input.get("model_id")));
//        }
//        if (!input.isNull("item_price")) {
//            item_price = CNumeric(PadQuotes((String) input.get("item_price")));
//        }
//        if (!input.isNull("item_netdisc")) {
//            item_netdisc = CNumeric(PadQuotes((String) input.get("item_netdisc")));
//        }
//        
//        if (!input.isNull("item_netprice")) {
//            item_netprice = CNumeric(PadQuotes((String) input.get("item_netprice")));
//        }
//        if (!input.isNull("item_tax_id")) {
//            item_tax_id = CNumeric(PadQuotes((String) input.get("item_tax_id")));
//        }
//        if (!input.isNull("item_tax_rate")) {
//            item_tax_rate = CNumeric(PadQuotes((String) input.get("item_tax_rate")));
//        }
//        if (!input.isNull("quote_auth")) {
//            quote_auth = CNumeric(PadQuotes((String) input.get("quote_auth")));
//        }
//        if (!input.isNull("quote_vehstock_id")) {
//            quote_vehstock_id = CNumeric(PadQuotes((String) input.get("quote_vehstock_id")));
//        }
//        if (!input.isNull("quote_desc")) {
//            quote_desc = CNumeric(PadQuotes((String) input.get("quote_desc")));
//        }
//        if (!input.isNull("quote_terms")) {
//            quote_terms = CNumeric(PadQuotes((String) input.get("quote_terms")));
//        }
//        if (!input.isNull("quotedate")) {
//            quotedate = CNumeric(PadQuotes((String) input.get("quotedate")));
//        }
//        if (!input.isNull("quote_netqty")) {
//            quote_netqty = CNumeric(PadQuotes((String) input.get("quote_netqty")));
//        }
//        if (!input.isNull("quote_netamt")) {
//            quote_netamt = CNumeric(PadQuotes((String) input.get("quote_netamt")));
//        }
//        if (quote_discamt.equals("0")) {
//            quote_discamt = CNumeric(PadQuotes(request.getParameter("quote_discamt")));
//        }
//        if (!input.isNull("quote_totaltax")) {
//            quote_totaltax = CNumeric(PadQuotes((String) input.get("quote_totaltax")));
//        }
//        if (!input.isNull("quote_grandtotal")) {
//            quote_grandtotal = CNumeric(PadQuotes((String) input.get("quote_grandtotal")));
//        }
//        if (!input.isNull("quote_fin_loan1")) {
//            quote_fin_loan1 = CNumeric(PadQuotes((String) input.get("quote_fin_loan1")));
//        }
//        if (!input.isNull("quote_fin_loan2")) {
//            quote_fin_loan2 = CNumeric(PadQuotes((String) input.get("quote_fin_loan2")));
//        }
//        if (!input.isNull("quote_fin_loan3")) {
//            quote_fin_loan3 = CNumeric(PadQuotes((String) input.get("quote_fin_loan3")));
//        }
//        if (!input.isNull("quote_fin_tenure1")) {
//            quote_fin_tenure1 = CNumeric(PadQuotes((String) input.get("quote_fin_tenure1")));
//        }
//        if (!input.isNull("quote_fin_tenure2")) {
//            quote_fin_tenure2 = CNumeric(PadQuotes((String) input.get("quote_fin_tenure2")));
//        }
//        if (!input.isNull("quote_fin_tenure3")) {
//            quote_fin_tenure3 = CNumeric(PadQuotes((String) input.get("quote_fin_tenure3")));
//        }
//        if (!input.isNull("quote_fin_adv_emi1")) {
//            quote_fin_adv_emi1 = CNumeric(PadQuotes((String) input.get("quote_fin_adv_emi1")));
//        }
//        if (!input.isNull("quote_fin_adv_emi2")) {
//            quote_fin_adv_emi2 = CNumeric(PadQuotes((String) input.get("quote_fin_adv_emi2")));
//        }
//        if (!input.isNull("quote_fin_adv_emi3")) {
//            quote_fin_adv_emi3 = CNumeric(PadQuotes((String) input.get("quote_fin_adv_emi3")));
//        }
//        if (!input.isNull("quote_fin_emi1")) {
//            quote_fin_emi1 = CNumeric(PadQuotes((String) input.get("quote_fin_emi1")));
//        }
//        if (!input.isNull("quote_fin_emi2")) {
//            quote_fin_emi2 = CNumeric(PadQuotes((String) input.get("quote_fin_emi2")));
//        }
//        if (!input.isNull("quote_fin_emi3")) {
//            quote_fin_emi3 = CNumeric(PadQuotes((String) input.get("quote_fin_emi3")));
//        }
//        if (!input.isNull("quote_fin_baloonemi1")) {
//            quote_fin_baloonemi1 = CNumeric(PadQuotes((String) input.get("quote_fin_baloonemi1")));
//        }
//        if (!input.isNull("quote_fin_baloonemi2")) {
//            quote_fin_baloonemi2 = CNumeric(PadQuotes((String) input.get("quote_fin_baloonemi2")));
//        }
//        if (!input.isNull("quote_fin_baloonemi3")) {
//            quote_fin_baloonemi3 = CNumeric(PadQuotes((String) input.get("quote_fin_baloonemi3")));
//        }
//        if (!input.isNull("quote_fin_fee1")) {
//            quote_fin_fee1 = CNumeric(PadQuotes((String) input.get("quote_fin_fee1")));
//        }
//        if (!input.isNull("quote_fin_fee2")) {
//            quote_fin_fee2 = CNumeric(PadQuotes((String) input.get("quote_fin_fee2")));
//        }
//        if (!input.isNull("quote_fin_fee3")) {
//            quote_fin_fee3 = CNumeric(PadQuotes((String) input.get("quote_fin_fee3")));
//        }
//        
//        quote_fin_downpayment1 = PadQuotes(request.getParameter("txt_quote_fin_downpayment1"));
//        quote_fin_downpayment2 = PadQuotes(request.getParameter("txt_quote_fin_downpayment2"));
//        quote_fin_downpayment3 = PadQuotes(request.getParameter("txt_quote_fin_downpayment3"));
//        quotedate = PadQuotes(request.getParameter("txt_quote_date"));
//        quote_netqty = CNumeric(PadQuotes(request.getParameter("txt_quote_qty")));
//        quote_netamt = CNumeric(PadQuotes(request.getParameter("txt_quote_netamt")));
//        quote_discamt = CNumeric(PadQuotes(request.getParameter("txt_quote_discamt")));
//        
//        
//        if (!input.isNull("quote_fin_downpayment1")) {
//            quote_fin_downpayment1 = CNumeric(PadQuotes((String) input.get("quote_fin_downpayment1")));
//        }
//        if (!input.isNull("quote_fin_downpayment2")) {
//            quote_fin_downpayment2 = CNumeric(PadQuotes((String) input.get("quote_fin_downpayment2")));
//        }
//        if (!input.isNull("quote_fin_downpayment3")) {
//            quote_fin_downpayment3 = CNumeric(PadQuotes((String) input.get("quote_fin_downpayment3")));
//        }
//        if (!input.isNull("quotedate")) {
//            quotedate = CNumeric(PadQuotes((String) input.get("quotedate")));
//        }
//        if (!input.isNull("quote_netqty")) {
//            quote_netqty = CNumeric(PadQuotes((String) input.get("quote_netqty")));
//        }
//        if (!input.isNull("quote_netamt")) {
//            quote_netamt = CNumeric(PadQuotes((String) input.get("quote_netamt")));
//        }
//        
//        quote_totaltax = CNumeric(PadQuotes(request.getParameter("txt_quote_totaltax")));
//        quote_grandtotal = CNumeric(PadQuotes(request.getParameter("txt_quote_grandtotal")));
//        quote_desc = PadQuotes(request.getParameter("txt_quote_desc"));
//        quote_terms = PadQuotes(request.getParameter("txt_quote_terms"));
//        quote_refno = PadQuotes(request.getParameter("txt_quote_refno"));
//        quote_active = CheckBoxValue(PadQuotes(request.getParameter("chk_quote_active")));
//
//        quote_fin_option1 = PadQuotes(request.getParameter("dr_quote_fin_option1"));
//        quote_fin_option2 = PadQuotes(request.getParameter("dr_quote_fin_option2"));
//        quote_fin_option3 = PadQuotes(request.getParameter("dr_quote_fin_option3"));
//
//        quote_desc = PadQuotes(request.getParameter("txt_quote_desc"));
//        quote_terms = PadQuotes(request.getParameter("txt_quote_terms"));
//        quote_refno = PadQuotes(request.getParameter("txt_quote_refno"));
//        quote_active = CheckBoxValue(PadQuotes(request.getParameter("chk_quote_active")));
//        quote_emp_id = CNumeric(PadQuotes(request.getParameter("dr_executive")));
//        quote_notes = PadQuotes(request.getParameter("txt_quote_notes"));
//        quote_entry_by = PadQuotes(request.getParameter("entry_by"));
//        quote_modified_by = PadQuotes(request.getParameter("modified_by"));
//        entry_date = PadQuotes(request.getParameter("entry_date"));
//        modified_date = PadQuotes(request.getParameter("modified_date"));
//        beforetax_gp_count = PadQuotes(request.getParameter("txt_bt_group_count"));
//        aftertax_gp_count = PadQuotes(request.getParameter("txt_at_group_count"));
//    }
//}
