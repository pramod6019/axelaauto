package axela.ws.sales;
//divya 26th march 2014

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.ConnectWS;

import com.google.gson.Gson;

public class WS_News_Branch_List extends ConnectWS {

    public String StrSql = "";
    public String CountSql = "";
    public String emp_uuid = "0";
    public String SqlJoin = "";
    public int TotalRecords = 0;
    public String pagecurrent = "";
    public String emp_id = "";
    public String comp_id = "0";
    Gson gson = new Gson();
    JSONObject output = new JSONObject();
    ArrayList<String> list = new ArrayList<>();
    Map<String, String> map = new HashMap<>();

    public JSONObject NewsList(JSONObject input) throws Exception {
        String confirmed = "";
        if (AppRun().equals("0")) {
            SOP("input = " + input);
        }
        if (!input.isNull("comp_id")) {
        	comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
        }
        if (!input.isNull("emp_uuid")) {
        	emp_uuid = CNumeric(PadQuotes((String) input.get("emp_uuid")));
        }
        pagecurrent = PadQuotes((Integer) input.get("pagecurrent") + "");

//        if (!input.isNull("emp_id")) {
//            emp_id = CNumeric(PadQuotes((String) input.get("emp_id")));
//            if (!emp_id.equals("0")) {
//                if (!input.isNull("keyword")) {
//                    keyword = PadQuotes((String) input.get("keyword"));
//                    if (!keyword.equals("")) {
//                        keyword_search = " and (quote_id like '%" + keyword + "%' or quote_no like '%" + keyword + "%'"
//                                + " or title_id like '%" + keyword + "%' or title_desc like '%" + keyword + "%'"
//                                //                                + " or enquiry_refno like '%" + keyword + "%'"
//                                + " or quote_date like '%" + keyword + "%'"
//                                //+ " or enquiry_close_date like '%" + keyword + "%' or enquiry_lead_id like '%" + keyword + "%'"
//                                + " or customer_id like '%" + keyword + "%' or customer_name like '%" + keyword + "%'"
//                                + " or contact_id like '%" + keyword + "%' or contact_fname like '%" + keyword + "%'"
//                                + " or contact_lname like '%" + keyword + "%'"
//                                + " or contact_mobile1 like '%" + keyword + "%' or contact_mobile2 like '%" + keyword + "%'"
//                                + " or contact_email1 like '%" + keyword + "%'"
//                                //+ " or so_dob like '%" + keyword + "%'"
//                                //                                + " or soe_name like '%" + keyword 
//                                //+ " or so_pan like '%" + keyword + "%'"
//                                + " or quote_lead_id like '%" + keyword + "%'"
//                                + " or quote_enquiry_id like '%" + keyword + "%'"
//                                //                                + " or so_quote_id like '%" + keyword + "%'"
//                                + " or quote_netamt like '%" + keyword + "%'"
//                                + " or quote_discamt like '%" + keyword + "%' or quote_totaltax like '%" + keyword + "%'"
//                                + " or quote_grandtotal like '%" + keyword + "%' "
//                                + " or quote_bill_address like '%" + keyword + "%' or quote_ship_address like '%" + keyword + "%'"
//                                + " or quote_desc like '%" + keyword + "%'"
//                                + " or quote_terms like '%" + keyword + "%'"
//                                + " or quote_auth_id like '%" + keyword + "%' or quote_auth_date like '%" + keyword + "%'"
//                                //+ " or so_mga_amount like '%" + keyword + "%'"
//                                //+ " or so_po like '%" + keyword + "%'"
//                                + " or quote_refno like '%" + keyword + "%'"
//                                + " or quote_notes like '%" + keyword + "%'"
//                                //+ " or so_promise_date like '%" + keyword + "%'"
//                                + " or quote_entry_date like '%" + keyword + "%' or quote_modified_date like '%" + keyword + "%'"
//                                //                                + " or delstatus_name like '%" + keyword + "%' or so_open like '%" + keyword + "%'"
//                                //                                + " or so_active like '%" + keyword + "%' or so_notes like '%" + keyword + "%'"
//                                //                                + " or so_entry_date like '%" + keyword + "%' or so_modified_date like '%" + keyword + "%'"
//                                //                                + " or sob_name like '%" + keyword + "%' or campaign_id like '%" + keyword + "%'"
//                                //                                + " or campaign_name like '%" + keyword + "%'"
//                                + " or emp_id like '%" + keyword + "%' or emp_name like '%" + keyword + "%'"
//                                //                                + " or stage_name like '%" + keyword + "%' "
//                                //+ " or status_name like '%" + keyword + "%'"
//                                + " or branch_id like '%" + keyword + "%' or branch_code like '%" + keyword + "%'"
//                                + " or branch_name like '%" + keyword + "%')";  //or followup_enquiry_id like '%" + keyword + "%'"
////                                + " or item_name like '%" + keyword + "%' or enquiry_priorityenquiry_id like '%" + keyword + "%')";
//                    } else {
//                        keyword_search = "";
//                    }
//                    SOP("keyword==" + keyword);
//                }
//                PopulateConfigDetails();
//                output.put("config_sales_enquiry_refno", config_sales_enquiry_refno);
        try {
            StrSql = "Select coalesce(concat(branch_name,' (', branch_code, ')'),'All Branch') as branchname, coalesce(branch_name, '') as branch_name, "
                    + " news_id, news_branch_id,news_topic,news_desc,news_date, news_featured, news_active";

            CountSql = " SELECT Count(distinct news_id)";

            SqlJoin = " from " + compdb(comp_id) + "axela_news_branch "
                    + " left join " + compdb(comp_id) + "axela_branch on branch_id = news_branch_id"
                    + " where 1=1 ";
            StrSql += SqlJoin;

            StrSql = StrSql + " GROUP BY news_id"
                    + " ORDER BY news_id DESC " + LimitRecords(TotalRecords, pagecurrent);
            CountSql += SqlJoin;
            CachedRowSet crs =processQuery(StrSql, 0);
            TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
            output.put("totalrecords", TotalRecords);
            if (crs.isBeforeFirst()) {
                while (crs.next()) {
                    map.put("branchname", crs.getString("branchname"));
                    map.put("news_branch_id", crs.getString("news_branch_id"));
                    map.put("news_id", crs.getString("news_id"));
                    map.put("news_topic", crs.getString("news_topic").replace("\"", ""));
                    map.put("news_desc", crs.getString("news_desc").replace("\"", ""));
//                    map.put("news_topic", crs.getString("news_topic"));
//                    map.put("news_desc", crs.getString("news_desc")); 
                    map.put("news_date", strToShortDate(crs.getString("news_date")));
                    map.put("news_featured", crs.getString("news_featured"));
                    map.put("news_active", crs.getString("news_active"));

                    list.add(gson.toJson(map)); // Converting String to Json
                }
                map.clear();
                output.put("listdata", list);
                list.clear();
            } else {
                output.put("msg", "No Records Found!");
            }
            if (AppRun().equals("0")) {
                SOP("output = " + output);
            }
            crs.close();
        } catch (Exception ex) {
            SOPError("Axelaauto ==" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
//            }
//        }
        return output;
    }

//    public void PopulateConfigDetails() {
//        StrSql = "SELECT config_sales_soe, config_sales_sob,"
//                + " config_sales_campaign, config_sales_enquiry_refno"
//                + " from " + compdb(comp_id) + "axela_config, " + compdb(comp_id) + "axela_emp"
//                + " where emp_id = " + emp_id + "";
//        CachedRowSet crs =processQuery(StrSql, 0);
////        SOP("strsql==config=="+StrSql);
//        try {
//            while (crs.next()) {
//                config_sales_enquiry_refno = crs.getString("config_sales_enquiry_refno");
//            }
//            crs.close();
//        } catch (Exception ex) {
//            SOPError("Axelaauto== " + this.getClass().getName());
//            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
//        }
//    }
}
