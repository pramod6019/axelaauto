package axela.sales;
//divya 26th nov

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import org.apache.tomcat.util.codec.binary.Base64;
import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.Connect;

public class Sales_Ecover_Policy_Add extends Connect {

	public String emp_id = "";
	public String comp_id = "0";
	public String branch_id = "";
	public String StrSql = "";
	public String StrHTML = "";
	public String so_id = "";
	public String enquiry_date = "";
	public StringBuilder Str = new StringBuilder();
	public String policydetails = "";
	public JSONObject details = new JSONObject();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			emp_id = CNumeric(GetSession("emp_id", request));
			branch_id = CNumeric(GetSession("emp_branch_id", request));
			so_id = CNumeric(PadQuotes(request.getParameter("so_id")));
			if (!so_id.equals("0")) {
				StrHTML = PopulateDetails();
				if (!StrHTML.equals("")) {
					response.sendRedirect("../portal/ecover-signin.jsp?policy_add=yes&policydetails=" + StrHTML.toString());
				}
				SOP("StrHTML====" + new String(Base64.decodeBase64(StrHTML.getBytes("ISO-8859-1")).toString()));
			}
		}
	}

	public String PopulateDetails() throws UnsupportedEncodingException {
		try {
			StrSql = "SELECT so_id, customer_name,"
					+ " customer_notes, customer_city_id, customer_soe_id, customer_sob_id,"
					+ " customer_accgroup_id, customer_type,"
					+ " contact_title_id, title_desc, contact_fname, contact_lname, contact_jobtitle,"
					+ " contact_mobile1, contact_mobile2, contact_phone1, contact_phone2,"
					+ " contact_email1, contact_email2, contact_address, contact_pin, contact_notes,"
					+ " so_reg_no, vehstock_chassis_no, vehstock_engine_no, vehstock_modelyear"
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = so_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
					+ " WHERE 1 = 1"
					+ " AND so_id = " + so_id;
			// SOP("StrSql--" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				details.put("policy_so_id", crs.getString("so_id"));
				details.put("policy_customer_name", crs.getString("customer_name"));
				details.put("policy_customer_mobile1", crs.getString("contact_mobile1"));
				details.put("policy_customer_mobile2", crs.getString("contact_mobile2"));
				details.put("policy_customer_email1", crs.getString("contact_email1"));
				details.put("policy_customer_email2", crs.getString("contact_email2"));
				details.put("policy_customer_phone1", crs.getString("contact_phone1"));
				details.put("policy_customer_phone2", crs.getString("contact_phone2"));
				details.put("policy_customer_notes", crs.getString("customer_notes"));
				details.put("policy_customer_city_id", crs.getString("customer_city_id"));
				details.put("policy_customer_soe_id", crs.getString("customer_soe_id"));
				details.put("policy_customer_sob_id", crs.getString("customer_sob_id"));
				details.put("policy_customer_accgroup_id", crs.getString("customer_accgroup_id"));
				details.put("policy_customer_type", crs.getString("customer_type"));
				details.put("policy_contact_title_id", crs.getString("contact_title_id"));
				details.put("policy_title_desc", crs.getString("title_desc"));
				details.put("policy_contact_fname", crs.getString("contact_fname"));
				details.put("policy_contact_lname", crs.getString("contact_lname"));
				details.put("policy_contact_jobtitle", crs.getString("contact_jobtitle"));
				details.put("policy_contact_address", crs.getString("contact_address"));
				details.put("policy_contact_pin", crs.getString("contact_pin"));
				details.put("policy_contact_notes", crs.getString("contact_notes"));
				details.put("policy_so_reg_no", crs.getString("so_reg_no"));
				details.put("policy_chassis_no", crs.getString("vehstock_chassis_no"));
				details.put("policy_engine_no", crs.getString("vehstock_engine_no"));
				details.put("policy_modelyear", crs.getString("vehstock_modelyear"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return new String(Base64.encodeBase64(details.toString().getBytes("ISO-8859-1")));
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
