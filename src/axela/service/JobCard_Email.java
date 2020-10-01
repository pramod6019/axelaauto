package axela.service;

/**
 * @author GuruMurthy TS, 20 APR 2013
 */
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class JobCard_Email extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String StrSql = "";
	public String branch_id = "0";
	public String jc_id = "0";
	public String comp_email_enable = "", attachment = "";
	public String brandconfig_jc_estimate_email_enable = "";
	public String brandconfig_jc_estimate_email_format = "";
	public String brandconfig_jc_estimate_email_sub = "";
	// public String branch_quote_email_exe_sub = "";
	// public String branch_quote_email_exe_format = "";
	public String config_admin_email = "";
	public String config_email_enable = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String jc_contact_id = "0";
	public String contact_name = "";
	public String contact_email1 = "";
	public String contact_email2 = "";
	JobCard_Print_Pdf JobCard_pdf = new JobCard_Print_Pdf();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_service_jobcard_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				jc_id = CNumeric(PadQuotes(request.getParameter("jc_id")));
				PopulateFields();
				if (!jc_id.equals("0")) {
					if (comp_email_enable.equals("1")
							&& config_email_enable.equals("1")
							&& !config_admin_email.equals("")
							&& brandconfig_jc_estimate_email_enable.equals("1")) {
						if (!contact_email1.equals("")
								&& !brandconfig_jc_estimate_email_format.equals("")
								&& !brandconfig_jc_estimate_email_sub.equals("")) {
							attachment = CachePath(comp_id) + "JobCard_" + jc_id + ".pdf,JobCard_" + jc_id + ".pdf";
							JobCard_pdf.JobCardDetails(request, response, jc_id, BranchAccess, ExeAccess, "file", "estimate");
							SendEmail();
						}
					}
				}
				response.sendRedirect("jobcard-list.jsp?jc_id=" + jc_id + "&msg=Email sent successfully!");
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

	protected void SendEmail() throws SQLException {
		String msg, sub;
		msg = brandconfig_jc_estimate_email_format;
		sub = brandconfig_jc_estimate_email_sub;

		sub = "REPLACE('" + sub + "','[JOBCARDID]',jc_id)";
		sub = "REPLACE(" + sub + ",'[CUSTOMERID]',customer_id)";
		sub = "REPLACE(" + sub + ",'[CUSTOMERNAME]',customer_name)";
		sub = "REPLACE(" + sub + ",'[CONTACTNAME]',concat(contact_fname,' ', contact_lname))";
		sub = "REPLACE(" + sub + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
		sub = "REPLACE(" + sub + ",'[CONTACTMOBILE1]',contact_mobile1)";
		sub = "REPLACE(" + sub + ",'[CONTACTPHONE1]',contact_phone1)";
		sub = "REPLACE(" + sub + ",'[CONTACTEMAIL1]',contact_email1)";
		sub = "REPLACE(" + sub + ",'[EXENAME]',emp_name)";
		sub = "REPLACE(" + sub + ",'[EXEJOBTITLE]',jobtitle_desc)";
		sub = "REPLACE(" + sub + ",'[EXEMOBILE1]',emp_mobile1)";
		sub = "REPLACE(" + sub + ",'[EXEPHONE1]',emp_phone1)";
		sub = "REPLACE(" + sub + ",'[EXEEMAIL1]',emp_email1)";
		sub = "REPLACE(" + sub + ",'[MODELNAME]', COALESCE(preownedmodel_name,''))";
		sub = "REPLACE(" + sub + ",'[ITEMNAME]', COALESCE(variant_name,''))";
		sub = "REPLACE(" + sub + ",'[REGNO]', veh_reg_no)";

		msg = "REPLACE('" + msg + "','[JOBCARDID]',jc_id)";
		msg = "REPLACE(" + msg + ",'[CUSTOMERID]',customer_id)";
		msg = "REPLACE(" + msg + ",'[CUSTOMERNAME]',customer_name)";
		msg = "REPLACE(" + msg + ",'[CONTACTNAME]',concat(contact_fname,' ', contact_lname))";
		msg = "REPLACE(" + msg + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
		msg = "REPLACE(" + msg + ",'[CONTACTMOBILE1]',contact_mobile1)";
		msg = "REPLACE(" + msg + ",'[CONTACTPHONE1]',contact_phone1)";
		msg = "REPLACE(" + msg + ",'[CONTACTEMAIL1]',contact_email1)";
		msg = "REPLACE(" + msg + ",'[EXENAME]',emp_name)";
		msg = "REPLACE(" + msg + ",'[EXEJOBTITLE]',jobtitle_desc)";
		msg = "REPLACE(" + msg + ",'[EXEMOBILE1]',emp_mobile1)";
		msg = "REPLACE(" + msg + ",'[EXEPHONE1]',emp_phone1)";
		msg = "REPLACE(" + msg + ",'[EXEEMAIL1]',emp_email1)";
		msg = "REPLACE(" + msg + ",'[MODELNAME]', COALESCE(preownedmodel_name,''))";
		msg = "REPLACE(" + msg + ",'[ITEMNAME]', COALESCE(variant_name,''))";
		msg = "REPLACE(" + msg + ",'[REGNO]', veh_reg_no)";
		msg = "REPLACE(" + msg + ", '[BRANCHADDRESS]', branch_add)";

		try {
			String email_to = contact_email1;
			if (!contact_email2.equals("")) {
				email_to += "," + contact_email2;
			}

			// postMail(contact_email1, config_admin_email, "info@emax.in", config_admin_email, sub, msg, attachment);
			StrSql = "SELECT"
					+ "	branch_id,"
					+ " '" + jc_contact_id + "',"
					+ " '" + contact_name + "',"
					+ " '" + config_admin_email + "',"
					+ " '" + email_to + "',"
					+ " " + sub + ","
					+ " " + msg + ","
					+ " '" + attachment.replace("\\", "/") + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " " + emp_id + ","
					+ " 0"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
					+ "	INNER JOIN " + compdb(comp_id) + "axela_brand_config ON brandconfig_brand_id = branch_brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jc_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON jc_emp_id = emp_id"
					// + " INNER JOIN " + compdb(comp_id) + "axela_title ON contact_title_id = title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON emp_jobtitle_id = jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_trans ON jctrans_jc_id = jc_id"
					+ " AND jctrans_rowcount != 0"
					+ " LEFT JOIN axelaauto.axela_preowned_variant ON variant_id = jctrans_item_id"
					+ " LEFT JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " WHERE jc_id = " + jc_id + "";

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email"
					+ " ("
					+ "	email_branch_id,"
					+ "	email_contact_id,"
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

	protected void PopulateFields() {
		try {
			StrSql = "SELECT jc_branch_id, jc_contact_id, contact_lname, contact_fname,contact_email1,"
					+ " COALESCE(brandconfig_jc_estimate_email_enable,'') AS brandconfig_jc_estimate_email_enable,"
					+ " COALESCE(brandconfig_jc_estimate_email_sub,'') AS brandconfig_jc_estimate_email_sub, contact_email2,"
					+ " COALESCE(brandconfig_jc_estimate_email_format,'') AS brandconfig_jc_estimate_email_format,"
					+ " config_admin_email, config_email_enable, comp_email_enable "
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id = contact_customer_id"
					// + " inner join " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch on branch_id = jc_branch_id"
					+ "	INNER JOIN " + compdb(comp_id) + "axela_brand_config ON brandconfig_brand_id = branch_brand_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp on emp_id = " + emp_id + ","
					+ " " + compdb(comp_id) + "axela_config,"
					+ " " + compdb(comp_id) + "axela_comp"
					+ " WHERE jc_id = " + jc_id + BranchAccess;
			// SOP("StrSql==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				branch_id = crs.getString("jc_branch_id");
				jc_contact_id = crs.getString("jc_contact_id");
				contact_name = crs.getString("contact_fname") + " " + crs.getString("contact_lname");
				contact_email1 = crs.getString("contact_email1");
				contact_email2 = crs.getString("contact_email2");
				brandconfig_jc_estimate_email_enable = crs.getString("brandconfig_jc_estimate_email_enable");
				brandconfig_jc_estimate_email_format = crs.getString("brandconfig_jc_estimate_email_format");
				brandconfig_jc_estimate_email_sub = crs.getString("brandconfig_jc_estimate_email_sub");
				config_admin_email = crs.getString("config_admin_email");
				config_email_enable = crs.getString("config_email_enable");
				comp_email_enable = crs.getString("comp_email_enable");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
