/* Divya 26th march 2014 */
package axela.ws.sales;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.ConnectWS;

import com.google.gson.Gson;

public class WS_TestDrive_Check extends ConnectWS {

    public String msg = "";
    public String StrSql = "";
    public String update = "";
    public String branch_id = "";
    public String comp_id = "0";
    public String testdrive_veh_id = "";
    public String emp_uuid = "0";
    public String testdrive_time = "";
    public String starttime = "";
    public String endtime = "";
    public String cal = "";
    public Object team_id = "";
    Gson gson = new Gson();
    JSONObject output = new JSONObject();
    ArrayList<String> list = new ArrayList<String>();
    Map<String, String> map = new HashMap<String, String>();

    public JSONObject TestDrive_CheckData(JSONObject input) {
        if (AppRun().equals("0")) {
            SOP("input = " + input);
        }
       
        try {
        	if (!input.isNull("comp_id")) {
                comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
        	}
            if (!input.isNull("branch_id") || !input.isNull("testdrive_veh_id") || !input.isNull("testdrive_time")) {
                branch_id = CNumeric(PadQuotes((String) input.get("branch_id")));
//                SOP("branch_id = " + branch_id);
                testdrive_veh_id = CNumeric(PadQuotes((String) input.get("testdrive_veh_id")));
                testdrive_time = PadQuotes(((String) input.get("testdrive_time")));
                if (!testdrive_time.equals("") && isValidDateFormatLong(testdrive_time)) {
                    starttime = ConvertLongDateToStr(testdrive_time);
                    endtime = ToLongDate(AddHoursDate(StringToDate(starttime), 1, 0, 0));
                }
//                SOP("starttime = " + starttime);
//                SOP("endtime = " + endtime);
                TestDriveCalendar();
            }
        } catch (Exception ex) {
            SOPError("Axelaauto ==" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            return output;
        }
        if (AppRun().equals("0")) {
            SOP("output = " + output);
        }
        return output;
    }

    protected JSONObject TestDriveCalendar() throws JSONException {
        DecimalFormat deci = new DecimalFormat("#");
        String search = "";
        if (testdrive_time.equals("")) {
            msg = "Select Date!";
//            output.put("msg", "Select Date!");
        }
        if (testdrive_veh_id.equals("0")) {
            msg = "Select Vehicle!";
//            output.put("msg", "Select Vehicle!");
        }
        if (msg.equals("")) {
            if (!testdrive_veh_id.equals("0") && !testdrive_time.equals("")) {
                StrSql = "select outage_fromtime, outage_totime, outage_desc "
                        + " from " + compdb(comp_id) + "axela_sales_testdrive_vehicle_outage"
                        + "  where outage_veh_id=" + testdrive_veh_id + " and "
                        + " ((outage_fromtime >= " + starttime + " and outage_fromtime < " + endtime + ") "
                        + " or (outage_totime > " + starttime + " and outage_totime <= " + endtime + ") "
                        + " or (outage_fromtime < " + starttime + " and outage_totime > " + endtime + "))"
                        + " order by outage_fromtime";
//            SOP("StrSql = " + StrSql);
                String outage = "";
                try {
                    CachedRowSet crs =processQuery(StrSql, 0);
                    if (crs.isBeforeFirst()) {
                        while (crs.next()) {
                            outage = strToLongDate(crs.getString("outage_fromtime")) + " - " + strToLongDate(crs.getString("outage_totime"));
                        }
                    }
//                SOP("outage = " + outage);
                    output.put("outage", outage);
                    crs.close();
                } catch (Exception ex) {
                    SOPError("Axelaauto ==" + this.getClass().getName());
                    SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
                }
            }
            if (!testdrive_veh_id.equals("0")) {
                search = " and testdrive_veh_id=" + testdrive_veh_id + "";
            }
            search = search + " and enquiry_branch_id = " + branch_id + "";
            if (!testdrive_time.equals("")) {
                search = search + " and date_format(testdrive_time,'%d/%m/%Y') = '" + strToShortDate(ConvertLongDateToStr(testdrive_time)) + "'";
            }
            try {
                StrSql = " select testdrive_id, veh_name, veh_regno, branch_code, customer_name, testdrive_time_to, testdrive_time_from, testdrive_confirmed,"
                        + " concat('ENQ',branch_code,enquiry_no) as enquiry_no, testdrive_doc_value, customer_id, enquiry_id, emp_id, concat(emp_name,' (', emp_ref_no, ')') as emp_name, "
                        + " testdrive_time, location_name, testdrive_in_time, testdrive_in_kms, testdrive_out_time, testdrive_out_kms"
                        + " from " + compdb(comp_id) + "axela_sales_testdrive "
                        + " inner Join " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = testdrive_enquiry_id "
                        + " inner Join " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id "
                        + " inner join " + compdb(comp_id) + "axela_sales_testdrive_location on location_id= testdrive_location_id"
                        + " inner join " + compdb(comp_id) + "axela_sales_testdrive_vehicle on veh_id = testdrive_veh_id "
                        + " inner join " + compdb(comp_id) + "axela_inventory_item on item_id=veh_item_id"
                        + " inner join " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id "
                        + " inner join " + compdb(comp_id) + "axela_emp on emp_id = testdrive_emp_id "
                        + " where 1=1  ";
                StrSql = StrSql + search + " group by testdrive_id order by testdrive_time_from";
//            SOP("strqq..outage" + StrSqlBreaker(StrSql));
                CachedRowSet crs =processQuery(StrSql, 0);
                if (crs.isBeforeFirst()) {
                    output.put("testdrive_date", strToShortDate(ConvertLongDateToStr(testdrive_time)));
                    while (crs.next()) {
//                    Str.append("<br>");
                        if (!crs.getString("testdrive_time_from").equals("")) {
                            map.put("dur", "" + PeriodTime(crs.getString("testdrive_time_from"), crs.getString("testdrive_time_to"), "2"));
                        } else {
                            map.put("dur", "Not Confirmed");
                        }
                        map.put("testdrive_time", "Test Drive Time: " + SplitHourMin(crs.getString("testdrive_time")));
                        if (crs.getString("testdrive_confirmed").equals("0")) {
                            map.put("confirmed", "0");
                        } else {
                            map.put("confirmed", "1");
                        }
                        map.put("testdrive_id", crs.getString("testdrive_id"));
                        map.put("veh_name", crs.getString("veh_name"));
                        map.put("veh_regno", crs.getString("veh_regno"));
                        map.put("location_name", crs.getString("location_name"));
                        map.put("emp_name", crs.getString("emp_name"));
                        map.put("enquiry_no", crs.getString("enquiry_no"));
                        map.put("customer_name", crs.getString("customer_name") + " (" + crs.getString("customer_id") + ")");

                        if (!crs.getString("testdrive_out_time").equals("")) {
                            map.put("testdrive_out_time", SplitHourMin(crs.getString("testdrive_out_time")));
                            if (!crs.getString("testdrive_in_time").equals("")) {
                                map.put("testdrive_in_time", SplitHourMin(crs.getString("testdrive_in_time")));
                            } else {
                                map.put("testdrive_in_time", "");
                            }
                            if (!crs.getString("testdrive_out_time").equals("") && !crs.getString("testdrive_in_time").equals("")) {
                                String Hours = deci.format(getHoursBetween(StringToDate(crs.getString("testdrive_out_time")), StringToDate(crs.getString("testdrive_in_time"))));
                                String Mins = deci.format(getMinBetween(StringToDate(crs.getString("testdrive_out_time")), StringToDate(crs.getString("testdrive_in_time"))));
                                Hours = doublenum(Integer.parseInt(Hours));
                                Mins = doublenum(Integer.parseInt(Mins));
                                map.put("duration", Hours + ":" + Mins + "");
                            } else {
                                map.put("duration", "");
                            }
                            if (!crs.getString("testdrive_out_kms").equals("")) {
                                map.put("testdrive_out_kms", crs.getString("testdrive_out_kms") + " kms.");
                            } else {
                                map.put("testdrive_out_kms", "");
                            }
                            if (!crs.getString("testdrive_in_time").equals("")) {
                                map.put("testdrive_in_kms", crs.getString("testdrive_in_kms") + " kms.");
                            } else {
                                map.put("testdrive_in_kms", "");
                            }
                        } else {
                            map.put("testdrive_out_time", "");
                        }
                        if (!crs.getString("testdrive_out_time").equals("") && !crs.getString("testdrive_in_time").equals("")) {
                            if (!crs.getString("testdrive_in_kms").equals(crs.getString("testdrive_out_kms"))) {
                                map.put("mileage", (crs.getDouble("testdrive_in_kms") - crs.getDouble("testdrive_out_kms")) + " kms.");
                            } else {
                                map.put("mileage", "");
                            }
                        }
                        list.add(gson.toJson(map));
                    }
                    map.clear();
                    output.put("calendardata", list);
                    list.clear();
                } else {
                    output.put("msg", "No Test Drive(s) Found!");
                }
                SOP("output = " + output);
                crs.close();
            } catch (Exception ex) {
                SOPError("Axelaauto ==" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        } else {
            output.put("msg", msg);
        }
        return output;
    }
}
