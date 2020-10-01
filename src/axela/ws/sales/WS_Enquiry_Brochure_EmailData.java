package axela.ws.sales;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.ConnectWS;

import com.google.gson.Gson;

public class WS_Enquiry_Brochure_EmailData extends ConnectWS {

	public String emp_id = "0";
	public String comp_id = "0";
	public String StrSql = "";
	public String attachment = "";
	public String enquiry_id = "";
	public String emp_uuid = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String contact_email1 = "", contact_email2 = "";
	public String StrHTML = "";
	public String branch_email1 = "";
	public String config_email_enable = "";
	public String comp_email_enable = "";
	public String branch_enquiry_brochure_email_enable = "";
	public String branch_enquiry_brochure_email_format = "";
	public String branch_enquiry_brochure_email_sub = "";
	public int TotalRecords = 0;
	public String enquiry_contact_id = "", contact_name = "";
	public String sendB = "";
	public String StrSearch = "", brochure_id = "";
	public String[] chk_brochure = null;
	public String enquiry_model_id = "0", enquiry_branch_id = "0", brochure_model_id = "0", SrcModel = "";
	public String msg = "", first = "";
	// ws
	JSONObject output = new JSONObject();
	Gson gson = new Gson();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();

	public JSONObject Enquiry_SendBrochure(JSONObject input) {
		if (AppRun().equals("0")) {
			SOP("input = " + input);
		}
		try {
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

			if (!input.isNull("first")) {
				first = PadQuotes((String) input.get("first"));
			}
			if (!input.isNull("brochure_id")) {
				brochure_id = PadQuotes((String) input.get("brochure_id"));
			}
			ExeAccess = WSCheckExeAccess(emp_id);

			if (!enquiry_id.equals("0")) {
				StrSql = "select enquiry_branch_id, enquiry_model_id"
						+ " from " + compdb(comp_id) + "axela_sales_enquiry"
						+ " where enquiry_id = " + enquiry_id
						+ BranchAccess.replace("branch_id", "enquiry_branch_id")
						+ ExeAccess.replace("emp_id", "enquiry_emp_id") + ""
						+ " group by enquiry_id "
						+ " order by enquiry_id desc ";
				CachedRowSet crs = processQuery(StrSql, 0);
				// SOP("StrSql = " + StrSql);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						enquiry_branch_id = crs.getString("enquiry_branch_id");
						enquiry_model_id = crs.getString("enquiry_model_id");
					}
				} else {
					output.put("invalidenquiry", "Invalid Enquiry!");
				}
				crs.close();
			}

			if (!input.isNull("brochure_model_id")) {
				brochure_model_id = CNumeric(PadQuotes((String) input.get("brochure_model_id")));
			}

			if (!brochure_model_id.equals("0")) {
				if (!enquiry_model_id.equals(brochure_model_id)) {
					enquiry_model_id = brochure_model_id;
				}
			}
			if (!input.isNull("sendB")) {
				sendB = PadQuotes((String) input.get("sendB"));
			}
			output.put("enquiry_model_id", enquiry_model_id);
			if (!sendB.equals("yes")) {
				output.put("brochure_model_id", brochure_model_id);
				ListBrochure();
				PopulateModel();
			} else {
				msg = "";
				CheckForm();
				if (!msg.equals("")) {
					msg = "Error!" + msg;
				} else {
					if (!attachment.equals("")) {
						PopulateFields();
						if (comp_email_enable.equals("1")
								&& config_email_enable.equals("1")
								&& !branch_email1.equals("")
								&& branch_enquiry_brochure_email_enable.equals("1") && !contact_email1.equals("")
								&& !branch_enquiry_brochure_email_format.equals("")
								&& !branch_enquiry_brochure_email_sub.equals("")) {
							SendEmail();
							output.put("emailstatus", "Brochure Sent Successfully!");
						} else {
							if (!comp_email_enable.equals("1")) {
								msg = msg + "<br>Email Option is Disabled!";
							}
							if (!config_email_enable.equals("1")) {
								msg = msg + "<br>Email Gateway is Disabled!";
							}
							if (branch_email1.equals("")) {
								msg = msg + "<br>Branch Email is Blank!";
							}
							if (contact_email1.equals("")) {
								msg = msg + "<br>Contact Email is Blank!";
							}
							if (!branch_enquiry_brochure_email_enable.equals("1")) {
								msg = msg + "<br>Brochure Email Option is Disabled!";
							}
							if (!branch_enquiry_brochure_email_format.equals("")) {
								msg = msg + "<br>Brochure Email Format is Blank";
							}
							if (!branch_enquiry_brochure_email_sub.equals("")) {
								msg = msg + "<br>Brochure Email Subject is Blank";
							}
							output.put("emailstatus", "Error!" + msg);
						}
					}
				}
			}
			if (AppRun().equals("0")) {
				SOP("output = " + output);
			}
			return output;
		} catch (Exception ex) {
			SOP(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
	}

	protected void CheckForm() throws ServletException, IOException, SQLException {
		int count = 0;
		msg = "";
		chk_brochure = new String[TotalRecords];
		if (!brochure_id.equals("0")) {
			StrSearch = "  and brochure_id in (" + brochure_id.substring(0, brochure_id.lastIndexOf(",")) + ")";
			StrSql = "SELECT brochure_value, brochure_title"
					+ " from " + compdb(comp_id) + "axela_sales_enquiry_brochure"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = " + enquiry_branch_id
					+ " WHERE enquiry_id = " + enquiry_id + StrSearch + ""
					+ " AND brochure_rateclass_id = branch_rateclass_id AND brochure_brand_id = branch_brand_id"
					+ " GROUP BY brochure_id";
			// SOP("StrSql checkform = " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				attachment = "";
				while (crs.next()) {
					attachment = attachment + EnquiryBrochurePath(comp_id) + crs.getString("brochure_value") + ","
							+ crs.getString("brochure_title") + fileext(crs.getString("brochure_value"));
					attachment = attachment + ";";
				}
			}
			attachment = attachment.substring(0, attachment.lastIndexOf(";"));
			crs.close();
		} else {
			msg = msg + "<br>Select Atleast 1 Brochure!";
		}
	}

	protected void SendEmail() {
		String msg = "", sub = "", contact_email_foremail = "";
		if (!contact_email2.equals("")) {
			contact_email_foremail = contact_email1 + "," + contact_email2;
		} else {
			contact_email_foremail = contact_email1;
		}
		msg = (branch_enquiry_brochure_email_format);
		sub = (branch_enquiry_brochure_email_sub);

		sub = "replace('" + sub + "','[ENQUIRYID]', enquiry_id)";
		sub = "replace(" + sub + ",'[ENQUIRYNAME]',enquiry_title)";
		sub = "replace(" + sub + ",'[CUSTOMERID]',customer_id)";
		sub = "replace(" + sub + ",'[CUSTOMERNAME]',customer_name)";
		sub = "replace(" + sub + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		sub = "replace(" + sub + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
		sub = "replace(" + sub + ",'[CONTACTMOBILE1]',contact_mobile1)";
		sub = "replace(" + sub + ",'[CONTACTPHONE1]',contact_phone1)";
		sub = "replace(" + sub + ",'[CONTACTEMAIL1]',contact_email1)";
		sub = "replace(" + sub + ",'[EXENAME]',emp_name)";
		sub = "replace(" + sub + ",'[EXEJOBTITLE]',jobtitle_desc)";
		sub = "replace(" + sub + ",'[EXEMOBILE1]',emp_mobile1)";
		sub = "replace(" + sub + ",'[EXEPHONE1]',emp_phone1)";
		sub = "replace(" + sub + ",'[EXEEMAIL1]',emp_email1)";
		sub = "replace(" + sub + ",'[MODELNAME]',model_name)";
		sub = "replace(" + sub + ",'[ITEMNAME]',item_name)";

		msg = "replace('" + msg + "','[ENQUIRYID]',enquiry_id)";
		msg = "replace(" + msg + ",'[ENQUIRYNAME]',enquiry_title)";
		msg = "replace(" + msg + ",'[CUSTOMERID]',customer_id)";
		msg = "replace(" + msg + ",'[CUSTOMERNAME]',customer_name)";
		msg = "replace(" + msg + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		msg = "replace(" + msg + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
		msg = "replace(" + msg + ",'[CONTACTMOBILE1]',contact_mobile1)";
		msg = "replace(" + msg + ",'[CONTACTPHONE1]',contact_phone1)";
		msg = "replace(" + msg + ",'[CONTACTEMAIL1]',contact_email1)";
		msg = "replace(" + msg + ",'[EXENAME]',emp_name)";
		msg = "replace(" + msg + ",'[EXEJOBTITLE]',jobtitle_desc)";
		msg = "replace(" + msg + ",'[EXEMOBILE1]',emp_mobile1)";
		msg = "replace(" + msg + ",'[EXEPHONE1]',emp_phone1)";
		msg = "replace(" + msg + ",'[EXEEMAIL1]',emp_email1)";
		msg = "replace(" + msg + ",'[MODELNAME]',model_name)";
		msg = "replace(" + msg + ",'[ITEMNAME]',item_name)";

		try {
			StrSql = "SELECT"
					+ " '" + enquiry_contact_id + "',"
					+ " '" + contact_name + "',"
					+ " '" + branch_email1 + "',"
					+ " '" + contact_email_foremail + "',"
					+ " " + unescapehtml(sub) + ","
					+ " " + unescapehtml(msg) + ","
					+ " '" + attachment.replace("\\", "/") + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " " + emp_id + ","
					+ " 0 "
					+ " from " + compdb(comp_id) + "axela_sales_enquiry   "
					+ " inner join " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id  "
					+ " inner join " + compdb(comp_id) + "axela_customer on customer_id = enquiry_customer_id  "
					+ " inner join " + compdb(comp_id) + "axela_customer_contact on contact_id = enquiry_contact_id  "
					+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = enquiry_entry_id  "
					+ " inner join " + compdb(comp_id) + "axela_title on contact_title_id = title_id  "
					+ " inner join " + compdb(comp_id) + "axela_jobtitle on emp_jobtitle_id = jobtitle_id  "
					+ " inner join " + compdb(comp_id) + "axela_inventory_item_model on model_id = enquiry_model_id  "
					+ " inner join " + compdb(comp_id) + "axela_inventory_item on item_id = enquiry_item_id  "
					+ " where enquiry_id = " + enquiry_id + "";
			// SOP("StrSql = " + StrSql);
			StrSql = "INSERT into " + compdb(comp_id) + "axela_email"
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
			// SOP("insert  " + StrSql);
			updateQuery(StrSql);
		} catch (Exception e) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	public void ListBrochure() {
		CachedRowSet crs = null;
		int count = 0;
		try {
			StrSql = "SELECT brochure_id, brochure_title, brochure_value,"
					+ " COALESCE(item_id,0) as item_id, item_url,"
					+ " COALESCE(item_name, 'General') as item_name, "
					+ " COALESCE(model_name, 'General') as model_name,  "
					+ " COALESCE(model_name, '') as modelorder "
					+ " from " + compdb(comp_id) + "axela_sales_enquiry_brochure "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON brochure_rateclass_id = branch_rateclass_id AND brochure_brand_id = branch_brand_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = brochure_item_id  "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id   "
					+ " WHERE branch_id=" + enquiry_branch_id + " "
					+ " and (model_id =" + enquiry_model_id + " or brochure_item_id=0) "
					+ " GROUP BY brochure_id "
					+ " ORDER BY modelorder, item_name, brochure_title";
			// SOP("ListBrochure query == " + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					count++;
					map.put("brochure_id", crs.getString("brochure_id"));
					map.put("model_name", crs.getString("model_name"));
					map.put("item_id", crs.getString("item_id"));
					map.put("item_name", unescapehtml(crs.getString("item_name")));
					map.put("item_url", crs.getString("item_url"));
					map.put("brochure_value", crs.getString("brochure_value"));
					map.put("brochure_title", crs.getString("brochure_title"));
					list.add(gson.toJson(map));
				}
				map.clear();
				output.put("listbrochure", list);
				list.clear();
				TotalRecords = count;
				crs.close();
			} else {
				output.put("listbrochure", "No Brochures Found!");
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void PopulateFields() {
		try {
			StrSql = "SELECT enquiry_branch_id, enquiry_contact_id, contact_lname, contact_fname, "
					+ " title_desc, contact_email1, contact_email2, "
					+ " coalesce(branch_enquiry_brochure_email_enable,'') as branch_enquiry_brochure_email_enable, "
					+ " coalesce(branch_enquiry_brochure_email_sub,'') as branch_enquiry_brochure_email_sub, "
					+ " coalesce(branch_enquiry_brochure_email_format,'') as branch_enquiry_brochure_email_format, "
					+ " coalesce(branch_email1,'') as branch_email1, config_email_enable, comp_email_enable "
					+ " from " + compdb(comp_id) + "axela_sales_enquiry  "
					+ " inner join " + compdb(comp_id) + "axela_customer_contact on contact_id = enquiry_contact_id  "
					+ " inner join " + compdb(comp_id) + "axela_customer on customer_id = contact_customer_id "
					+ " inner join " + compdb(comp_id) + "axela_title on title_id = contact_title_id  "
					+ " inner JOIN " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id  "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp on emp_id =" + emp_id + " ,"
					+ " " + compdb(comp_id) + "axela_config, " + compdb(comp_id) + "axela_comp  "
					+ " where enquiry_id = " + enquiry_id
					+ ExeAccess.replace("emp_id", "enquiry_emp_id") + BranchAccess;
			// SOP("PopulateFields " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				enquiry_contact_id = crs.getString("enquiry_contact_id");
				contact_name = crs.getString("title_desc") + " " + crs.getString("contact_fname") + " " + crs.getString("contact_lname");
				contact_email1 = crs.getString("contact_email1");
				contact_email2 = crs.getString("contact_email2");
				branch_enquiry_brochure_email_enable = crs.getString("branch_enquiry_brochure_email_enable");
				branch_enquiry_brochure_email_format = crs.getString("branch_enquiry_brochure_email_format");
				branch_enquiry_brochure_email_sub = crs.getString("branch_enquiry_brochure_email_sub");
				branch_email1 = crs.getString("branch_email1");
				config_email_enable = crs.getString("config_email_enable");
				comp_email_enable = crs.getString("comp_email_enable");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PopulateModel() {
		try {
			StrSql = "Select model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " INNER JOIN axela_brand ON brand_id = model_brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = model_brand_id"
					+ " where 1=1 "
					+ " AND branch_id=" + enquiry_branch_id + ""
					+ " AND model_active = '1'"
					+ " AND model_sales = '1'"
					+ " GROUP BY model_id"
					+ " ORDER BY model_name";
			SOP("StrSql-----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			map.put("model_id", "0");
			map.put("model_name", "Select");
			list.add(gson.toJson(map));
			while (crs.next()) {
				map.put("model_id", crs.getString("model_id"));
				map.put("model_name", crs.getString("model_name"));
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatemodel", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
