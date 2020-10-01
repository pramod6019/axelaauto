package axela.sales;
//aJIt 28th december, 2012

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Enquiry_Generate extends Connect {

	public String StrSql = "";
	public static String msg = "";
	public String branch_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public String lead_sob_id = "0";
	public String lead_soe_id = "0";
	public String generateB = "";
	public String config_sales_soe = "";
	public String config_sales_sob = "";
	public String FormHTML = "";
	public String title_id = "0";
	public String empcount_id = "0";
	public String comp_subdomain = "";
	public String channel_domain = "";
	public String config_sales_enquiry_domain = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_lead_access", request, response);
			if (!comp_id.equals("0")) {
				PopulateConfigDetails();
				generateB = PadQuotes(request.getParameter("generate_form"));
				msg = PadQuotes(request.getParameter("msg"));
				if (generateB.equals("Generate Form")) {
					GetValues(request, response);
					CheckForm();
					if (msg.equals("")) {
						FormHTML = BuildForm();
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		branch_id = PadQuotes(request.getParameter("dr_branch_id"));
		emp_id = PadQuotes(request.getParameter("dr_emp_id"));
		lead_sob_id = PadQuotes(request.getParameter("dr_lead_sob_id"));
		lead_soe_id = PadQuotes(request.getParameter("dr_lead_soe_id"));
	}

	protected void CheckForm() {
		msg = "";
		if (branch_id.equals("0")) {
			msg = msg + "<br>Select Branch!";
		}
		if (emp_id.equals("0")) {
			msg = msg + "<br>Select Sales Consultant!";
		}
		if (config_sales_sob.equals("1")) {
			if (lead_sob_id.equals("0")) {
				msg = msg + "<br>Select Source Of Business!";
			}
		}
		if (config_sales_soe.equals("1")) {
			if (lead_soe_id.equals("0")) {
				msg = msg + "<br>Select Source Of Enquiry!";
			}
		}
	}

	public String PopulateExecutive() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " WHERE 1 = 1"
					+ " AND emp_sales=1"
					+ " AND emp_active='1'"
					+ " AND (emp_branch_id = " + branch_id + ""
					+ " OR emp_id = 1 "
					+ " OR emp_id IN (SELECT empbr.emp_id"
					+ " FROM " + compdb(comp_id) + "axela_emp_branch empbr "
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id=empbr.emp_id"
					+ " AND empbr.emp_branch_id=" + branch_id + "))"
					+ " GROUP BY emp_id "
					+ " ORDER BY emp_name ";

			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(StrSelectdrop(crs.getString("emp_id"), emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
		return Str.toString();
	}

	public String PopulateSoe() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>");
		try {
			StrSql = "SELECT soe_id, soe_name"
					+ " FROM " + compdb(comp_id) + "axela_soe"
					+ " GROUP BY soe_id"
					+ " ORDER BY soe_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("soe_id")).append("");
				Str.append(StrSelectdrop(crs.getString("soe_id"), lead_soe_id));
				Str.append(">").append(crs.getString("soe_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateSob() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>");
		try {
			StrSql = "SELECT sob_id, sob_name"
					+ " FROM " + compdb(comp_id) + "axela_sob"
					+ " GROUP BY sob_id"
					+ " ORDER BY sob_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("sob_id")).append("");
				Str.append(StrSelectdrop(crs.getString("sob_id"), lead_sob_id));
				Str.append(">").append(crs.getString("sob_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public void PopulateConfigDetails() {
		StrSql = "SELECT config_sales_soe, config_sales_sob,"
				+ " config_sales_enquiry_domain,"
				+ " comp_subdomain, channel_domain"
				+ " FROM axela." + compdb(comp_id) + "axela_config," + compdb(comp_id) + "axela_comp"
				+ " INNER JOIN axela." + compdb(comp_id) + "axela_channel ON channel_id = comp_channel_id";
		System.out.println(StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		// SOP("PopulateConfigDetails======" + StrSqlBreaker(StrSql));
		try {
			while (crs.next()) {
				// SOP("11111111");
				config_sales_enquiry_domain = crs.getString("config_sales_enquiry_domain");
				config_sales_soe = crs.getString("config_sales_soe");
				config_sales_sob = crs.getString("config_sales_sob");
				comp_subdomain = crs.getString("comp_subdomain");
				channel_domain = crs.getString("channel_domain");
				// SOP("222222");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String BuildForm() {
		String Str = "";
		try {
			Str = new Scanner(new File(TemplatePath(comp_id) + "enquiry.html")).useDelimiter("\\A").next();
			Str = Str.replace("[URL]", "http://" + comp_subdomain + "." + channel_domain + "/axela/sales/enquiry.jsp");
			Str = Str.replace("[TITLE]", PopulateTitle());
			Str = Str.replace("[BRANCH_ID]", branch_id);
			Str = Str.replace("[EMP_ID]", emp_id);
			Str = Str.replace("[SOE_ID]", lead_soe_id);
			Str = Str.replace("[SOB_ID]", lead_sob_id);
			Str = Str.replace("<", "&lt;");
			Str = Str.replace(">", "&gt;");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateTitle() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT title_id, title_desc"
					+ " FROM " + compdb(comp_id) + "axela_title"
					+ " WHERE 1 = 1"
					+ " ORDER BY title_desc ";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("title_id")).append("");
				Str.append(StrSelectdrop(crs.getString("title_id"), title_id));
				Str.append(">").append(crs.getString("title_desc")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
