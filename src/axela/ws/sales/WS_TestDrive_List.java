package axela.ws.sales;
//divya 26th march 2014

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.ConnectWS;

import com.google.gson.Gson;

public class WS_TestDrive_List extends ConnectWS {

    public String StrSql = "";
    public String emp_id = "";
    public String comp_id = "0";
    public String startdate = "";
    public String ExeAccess = "";
    public String emp_uuid = "0";
    Gson gson = new Gson();
    JSONObject output = new JSONObject();
    ArrayList<String> list = new ArrayList<String>();
    Map<String, String> map = new HashMap<String, String>();

    public JSONObject TestDriveList(JSONObject input) throws Exception {
        String confirmed = "";
        if (AppRun().equals("0")) {
            SOP("input = " + input);
        }
        if (!input.isNull("emp_id")) {
            emp_id = CNumeric(PadQuotes((String) input.get("emp_id")));
            if (!input.isNull("comp_id")) {
            	comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
            }
            if (!input.isNull("emp_uuid")) {
            	emp_uuid = CNumeric(PadQuotes((String) input.get("emp_uuid")));
            }
            if (!emp_id.equals("0")) {
                if (!input.isNull("startdate")) {
                    startdate = PadQuotes((String) input.get("startdate"));
                    startdate = ConvertShortDateToStr(startdate);
                }
                try {
//                    ExeAccess = WSCheckExeAccess(emp_id);
                    StrSql = "select testdrive_id, veh_id, veh_name, branch_code, customer_name, contact_id, concat(title_desc, ' ', contact_fname,' ',"
                            + " contact_lname) as contact_name, contact_mobile1, contact_mobile2, contact_email1, contact_email2, contact_phone1, contact_phone2,"
                            + " coalesce(model_name, '') as  model_name,"
                            + " testdrive_time_to, testdrive_time_from, testdrive_type, testdrive_confirmed, coalesce(testdrive_notes, '') as testdrive_notes, "
                            + " testdrive_doc_value, customer_id, enquiry_id, branch_id, CONCAT(branch_name,' (',branch_code,')') as branchname, "
                            + " exeemp.emp_id, concat(exeemp.emp_name,' (', exeemp.emp_ref_no, ')') as emp_name, "
                            + " testdrive_time, location_name, testdrive_in_time, testdrive_in_kms, testdrive_out_time, testdrive_out_kms, "
                            + " testdrive_fb_taken, coalesce(testdrive_fb_status_id,'0') as testdrive_fb_status_id, coalesce(status_name, '') as status_name,"
                            + " testdrive_fb_status_comments,"
                            + " testdrive_fb_budget, testdrive_fb_finance, testdrive_fb_finance_amount, testdrive_fb_finance_comments,"
                            + " testdrive_fb_insurance, testdrive_fb_insurance_comments  "
                            + " from " + compdb(comp_id) + "axela_sales_testdrive "
                            + " inner join " + compdb(comp_id) + "axela_sales_testdrive_location on location_id= testdrive_location_id"
                            + " inner join " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = testdrive_enquiry_id "
                            + " inner Join " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id "
                            + " inner join " + compdb(comp_id) + "axela_customer_contact on contact_id = enquiry_contact_id "
                            + " inner join " + compdb(comp_id) + "axela_title on title_id = contact_title_id "
                            + " inner join " + compdb(comp_id) + "axela_sales_testdrive_vehicle on veh_id = testdrive_veh_id "
                            + " inner join " + compdb(comp_id) + "axela_inventory_item on item_id = veh_item_id"
                            + " inner join " + compdb(comp_id) + "axela_inventory_item_model on model_id = item_model_id "
                            + " inner join " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id "
                            + " inner join " + compdb(comp_id) + "axela_emp exeemp on exeemp.emp_id = testdrive_emp_id "
                            + " left join " + compdb(comp_id) + "axela_sales_testdrive_status on status_id= testdrive_fb_status_id,"
                            + compdb(comp_id) + "axela_emp emp"
                            + " where 1=1"
                            + " AND emp.emp_id = " + emp_id
							+ " AND IF (emp.emp_exeaccess != '', FIND_IN_SET(testdrive_emp_id, emp.emp_exeaccess), 1=1)"
                            + " and substring(testdrive_time,1,8) = substring(" + startdate + " ,1,8)"
                            + " GROUP BY testdrive_id"
                            + " ORDER BY testdrive_id DESC ";
//            SOP("list testdrive query === " + StrSqlBreaker(StrSql));
                    CachedRowSet crs =processQuery(StrSql, 0);

                    if (crs.isBeforeFirst()) {
                        while (crs.next()) {
                            if (crs.getString("testdrive_confirmed").equals("0")) {
                                confirmed = "[Not Confirmed]";
                            } else {
                                confirmed = "";
                            }
                            map.put("emp_name", crs.getString("emp_name"));
                            map.put("testdrive_id", crs.getString("testdrive_id"));
                            map.put("enquiry_id", crs.getString("enquiry_id"));
                            map.put("customer_name", crs.getString("customer_name"));
                            map.put("contactname", crs.getString("contact_name"));
                            if (!crs.getString("contact_mobile1").equals("")) {
                                map.put("contact_mobile1", SplitPhoneNo(crs.getString("contact_mobile1"), 5, "M"));
                            } else {
                                map.put("contact_mobile1", "");
                            }
                            if (!crs.getString("contact_mobile2").equals("")) {
                                map.put("contact_mobile2", SplitPhoneNo(crs.getString("contact_mobile2"), 5, "M"));
                            } else {
                                map.put("contact_mobile2", "");
                            }
                            if (!crs.getString("contact_phone1").equals("")) {
                                map.put("contact_phone1", SplitPhoneNo(crs.getString("contact_phone1"), 4, "T"));
                            } else {
                                map.put("contact_phone1", "");
                            }

                            if (!crs.getString("contact_phone2").equals("")) {
                                map.put("contact_phone2", SplitPhoneNo(crs.getString("contact_phone2"), 4, "T"));
                            } else {
                                map.put("contact_phone2", "");
                            }

                            map.put("contact_email1", crs.getString("contact_email1"));
                            map.put("contact_email2", crs.getString("contact_email2"));
                            map.put("veh_name", crs.getString("veh_name"));
                            map.put("testdrive_time", strToLongDate(crs.getString("testdrive_time")));
                            if (!crs.getString("testdrive_time_from").equals("")) {
                                map.put("duration", PeriodTime(crs.getString("testdrive_time_from"), crs.getString("testdrive_time_to"), "1"));
                            }
                            map.put("location", crs.getString("location_name"));
                            if (!crs.getString("testdrive_notes").equals("")) {
                                map.put("notes", crs.getString("testdrive_notes"));
                            } else {
                                map.put("notes", "");
                            }
                            map.put("confirmed", confirmed);
                            String feedback = "";
                            if (crs.getString("testdrive_fb_taken").equals("1")) {
                                feedback = "Test Drive Taken";
                                if (!crs.getString("status_name").equals("")) {
                                    feedback += "<br>" + crs.getString("status_name") + "<br>" + crs.getString("testdrive_fb_status_comments");
                                } else {
                                    if (crs.getDouble("testdrive_fb_budget") != 0) {
                                        feedback += "<br>Budget: " + crs.getString("testdrive_fb_budget") + "<br>";
                                    }
                                    if (crs.getString("testdrive_fb_finance").equals("1")) {
                                        feedback += "<br>Finance Required<br>%age: " + crs.getString("testdrive_fb_finance_amount");
                                    } else {
                                        feedback += "<br>Finance Not Required<br>Comments: " + crs.getString("testdrive_fb_finance_comments");
                                    }
                                    if (crs.getString("testdrive_fb_insurance").equals("1")) {
                                        feedback += "<br>Insurance Required";
                                    } else {
                                        feedback += "<br>Insurance Not Required<br>Comments: " + crs.getString("testdrive_fb_insurance_comments");
                                    }
                                }
                            }
                            if (crs.getString("testdrive_fb_taken").equals("2")) {
                                feedback += "Test Drive not taken";
                            }
                            map.put("feedback", feedback);
                            map.put("msg", "");
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
            }
        }
        return output;
    }
}
