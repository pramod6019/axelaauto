package axela.service;
//divya 31st may 2013

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class PSFDays_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String psfdays_brand_id = "0";
	// public String psfdays_psftype_id = "0";
	public String psfdays_exe_type = "0";
	public String psfdays_jccat_id = "0";
	public String psfdays_emp_id = "0";
	public String dr_brand_id = "0";
	public String psfdays_id = "0";
	public String psfdays_daycount = "0";
	public String psfdays_desc = "";
	public String psfdays_script = "";
	public String psfdays_active = "";
	// public String psfdays_lostfollowup = "";
	// psf
	public String psf_exe_ids[] = new String[10];
	public String psf_exe_id = "";
	public String[] psf_cat_trans = null;
	public String[] psf_type_trans = null;
	public String psfdays_contactable_email_enable = "";
	public String psfdays_contactable_email_sub = "";
	public String psfdays_contactable_email_format = "";
	public String psfdays_contactable_sms_format = "";
	public String psfdays_contactable_sms_enable = "";

	public String psfdays_noncontactable_email_sub = "";
	public String psfdays_noncontactable_email_format = "";
	public String psfdays_noncontactable_sms_format = "";
	public String psfdays_noncontactable_email_enable = "";
	public String psfdays_noncontactable_sms_enable = "";

	public String psfdays_satisfied_email_enable = "";
	public String psfdays_satisfied_email_sub = "";
	public String psfdays_satisfied_email_format = "";
	public String psfdays_satisfied_sms_enable = "";
	public String psfdays_satisfied_sms_format = "";
	public String psfdays_dissatisfied_email_enable = "";
	public String psfdays_dissatisfied_email_sub = "";
	public String psfdays_dissatisfied_email_format = "";
	public String psfdays_dissatisfied_sms_enable = "";
	public String psfdays_dissatisfied_sms_format = "";

	public String QueryString = "";
	public String psfdays_entry_id = "0";
	public String psfdays_entry_date = "";
	public String psfdays_modified_id = "0";
	public String psfdays_modified_date = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";
	public String jcpsf_id = "0";
	public String crmdays_crmtype_id = "0";
	public String psfexe_id[] = new String[20];
	public Connection conntx = null;
	public Statement stmttx = null;
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_role_id", request, response);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				psfdays_id = CNumeric(PadQuotes(request.getParameter("psfdays_id")));
				QueryString = PadQuotes(request.getQueryString());
				psfdays_brand_id = CNumeric(PadQuotes(request.getParameter("dr_brand")));
				dr_brand_id = CNumeric(PadQuotes(request.getParameter("dr_brand_id")));
				if (!dr_brand_id.equals("0")) {
					psfdays_brand_id = dr_brand_id;
				}
				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						psfdays_daycount = "0";
					} else {
						GetValues(request, response);
						psfdays_entry_id = emp_id;
						psfdays_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("psfdays.jsp?dr_brand=" + psfdays_brand_id + "&msg=PSF Day Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Followup Days".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Followup Days".equals(deleteB)) {
						GetValues(request, response);
						psfdays_modified_id = emp_id;
						psfdays_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("psfdays.jsp?dr_brand=" + psfdays_brand_id + "&msg=PSF Day Updated Successfully!"));
						}
					} else if ("Delete Followup Days".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("psfdays.jsp?dr_brand=" + psfdays_brand_id + "&msg=PSF Day Deleted Successfully!"));
						}
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
		// psfdays_psftype_id = CNumeric(PadQuotes(request.getParameter("dr_psfdays_psftype_id")));
		psfdays_exe_type = CNumeric(PadQuotes(request.getParameter("dr_psfdays_exe_type")));
		psfdays_jccat_id = CNumeric(PadQuotes(request.getParameter("dr_psfdays_jccat_id")));
		psfdays_emp_id = CNumeric(PadQuotes(request.getParameter("dr_psfdays_emp_id")));

		psf_exe_ids = request.getParameterValues("psf_exe");
		if (psf_exe_ids != null) {
			for (int i = 0; i < psf_exe_ids.length; i++) {
				psf_exe_id += psf_exe_ids[i] + ",";

			}
			psf_exe_id = psf_exe_id.substring(0, psf_exe_id.length() - 1);
		}
		psfdays_daycount = CNumeric(PadQuotes(request.getParameter("txt_psfdays_daycount")));
		psfdays_desc = PadQuotes(request.getParameter("txt_psfdays_desc"));
		psfdays_script = PadQuotes(request.getParameter("txt_psfdays_script"));
		psfdays_active = PadQuotes(request.getParameter("chk_psfdays_active"));
		psfdays_contactable_email_enable = PadQuotes(request.getParameter("chk_psfdays_contactable_email_enable"));
		psfdays_contactable_sms_enable = PadQuotes(request.getParameter("chk_psfdays_contactable_sms_enable"));
		psfdays_noncontactable_email_enable = PadQuotes(request.getParameter("chk_psfdays_noncontactable_email_enable"));
		psfdays_noncontactable_sms_enable = PadQuotes(request.getParameter("chk_psfdays_noncontactable_sms_enable"));

		psfdays_satisfied_email_enable = PadQuotes(request.getParameter("chk_psfdays_satisfied_email_enable"));
		psfdays_satisfied_sms_enable = PadQuotes(request.getParameter("chk_psfdays_satisfied_sms_enable"));
		psfdays_dissatisfied_email_enable = PadQuotes(request.getParameter("chk_psfdays_dissatisfied_email_enable"));
		psfdays_dissatisfied_sms_enable = PadQuotes(request.getParameter("chk_psfdays_dissatisfied_sms_enable"));
		if (psfdays_active.equals("on")) {
			psfdays_active = "1";
		} else {
			psfdays_active = "0";
		}

		if (psfdays_contactable_email_enable.equals("on")) {
			psfdays_contactable_email_enable = "1";
		} else {
			psfdays_contactable_email_enable = "0";
		}
		if (psfdays_contactable_sms_enable.equals("on")) {
			psfdays_contactable_sms_enable = "1";
		} else {
			psfdays_contactable_sms_enable = "0";
		}

		if (psfdays_noncontactable_email_enable.equals("on")) {
			psfdays_noncontactable_email_enable = "1";
		} else {
			psfdays_noncontactable_email_enable = "0";
		}
		if (psfdays_noncontactable_sms_enable.equals("on")) {
			psfdays_noncontactable_sms_enable = "1";
		} else {
			psfdays_noncontactable_sms_enable = "0";
		}
		if (psfdays_satisfied_email_enable.equals("on")) {
			psfdays_satisfied_email_enable = "1";
		} else {
			psfdays_satisfied_email_enable = "0";
		}
		if (psfdays_satisfied_sms_enable.equals("on")) {
			psfdays_satisfied_sms_enable = "1";
		} else {
			psfdays_satisfied_sms_enable = "0";
		}
		if (psfdays_dissatisfied_email_enable.equals("on")) {
			psfdays_dissatisfied_email_enable = "1";
		} else {
			psfdays_dissatisfied_email_enable = "0";
		}
		if (psfdays_dissatisfied_sms_enable.equals("on")) {
			psfdays_dissatisfied_sms_enable = "1";
		} else {
			psfdays_dissatisfied_sms_enable = "0";
		}
		psf_cat_trans = request.getParameterValues("psf_cat_trans");
		psf_type_trans = request.getParameterValues("psf_type_trans");
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}
	protected void CheckForm() {
		msg = "";
		if (psfdays_brand_id.equals("0")) {
			msg = msg + "<br>Select Brand!";
		}
		// if (psfdays_psftype_id.equals("0")) {
		// msg = msg + "<br>Select CRM Type!";
		// }
		if (psfdays_exe_type.equals("0")) {
			msg = msg + "<br>Service Advisor!";
		}
		if (psfdays_daycount.equals("0")) {
			msg = msg + "<br>Enter Days!";
		} else {
			try {
				if (!psfdays_daycount.equals("0")) {
					StrSql = "SELECT psfdays_daycount FROM "
							+ compdb(comp_id) + "axela_service_jc_psfdays"
							+ " WHERE psfdays_daycount = " + psfdays_daycount + ""
							+ " AND psfdays_brand_id = " + psfdays_brand_id + "";
					// + " AND psfdays_crmtype_id = " + psfdays_psftype_id + "";
					if (!psfdays_jccat_id.equals("0")) {
						StrSql = StrSql + " AND psfdays_jccat_id = " + psfdays_jccat_id + "";
					}
					if (update.equals("yes")) {
						StrSql = StrSql + " AND psfdays_id!= " + psfdays_id + "";
					}
					// SOP("StrSql=lllll===" + StrSqlBreaker(StrSql));
					CachedRowSet crs = processQuery(StrSql, 0);
					if (crs.isBeforeFirst()) {
						msg = msg + "<br>Similar Days Found!";
					}
					crs.close();
				}
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
		if (psfdays_desc.equals("")) {
			msg = msg + "<br>Enter Description!";
		}
		if (psfdays_script.equals("")) {
			msg = msg + "<br>Enter Script!";
		}

	}

	protected void AddFields() {
		CheckForm();
		try {
			if (msg.equals("")) {
				try {
					conntx = connectDB();
					conntx.setAutoCommit(false);
					stmttx = conntx.createStatement();
					psfdays_id = ExecuteQuery("SELECT (coalesce(max(psfdays_id),0)+1) FROM " + compdb(comp_id) + "axela_service_jc_psfdays");
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_psfdays"
							+ " (psfdays_id,"
							+ " psfdays_brand_id,"
							// + " psfdays_psftype_id,"
							+ " psfdays_exe_type,"
							+ " psfdays_jccat_id,"
							+ " psfdays_emp_id,"
							+ " psfdays_daycount,"
							+ " psfdays_desc,"
							+ " psfdays_script,"
							// + " psfdays_lostfollowup,"
							+ " psfdays_active,"
							+ " psfdays_contactable_email_sub,"
							+ " psfdays_contactable_email_format,"
							+ " psfdays_contactable_email_exe_format,"
							+ " psfdays_contactable_sms_format,"
							+ " psfdays_noncontactable_email_sub,"
							+ " psfdays_noncontactable_email_format,"
							+ " psfdays_noncontactable_email_exe_format,"
							+ " psfdays_noncontactable_sms_format,"

							+ " psfdays_satisfied_email_sub,"
							+ " psfdays_satisfied_email_format,"
							+ " psfdays_satisfied_email_exe_format,"
							+ " psfdays_satisfied_sms_format,"
							+ " psfdays_dissatisfied_email_sub,"
							+ " psfdays_dissatisfied_email_format,"
							+ " psfdays_dissatisfied_email_exe_format,"
							+ " psfdays_dissatisfied_sms_format,"
							+ " psfdays_entry_id,"
							+ " psfdays_entry_date)"
							+ " VALUES"
							+ " (" + psfdays_id + ","
							+ " " + psfdays_brand_id + ","
							// + " " + psfdays_psftype_id + ","
							+ " " + psfdays_exe_type + ","
							+ " " + psfdays_jccat_id + ","
							+ " '" + psf_exe_id + "'," // psfdays_emp_id
							+ " " + psfdays_daycount + ","
							+ " '" + psfdays_desc + "',"
							+ " '" + psfdays_script + "',"
							// + " '" + psfdays_lostfollowup + "',"
							+ " " + psfdays_active + ","
							+ " '" + psfdays_contactable_email_sub + "',"
							+ " '" + psfdays_contactable_email_format + "',"
							+ " '', " // psfdays_contactable_email_exe_format
							+ " '" + psfdays_contactable_sms_format + "',"
							+ " '" + psfdays_noncontactable_email_sub + "',"
							+ " '" + psfdays_noncontactable_email_format + "',"
							+ " '', " // psfdays_noncontactable_email_exe_format
							+ " '" + psfdays_noncontactable_sms_format + "',"
							+ " '" + psfdays_satisfied_email_sub + "',"
							+ " '" + psfdays_satisfied_email_format + "',"
							+ " '', " // psfdays_satisfied_email_exe_format
							+ " '" + psfdays_satisfied_sms_format + "',"
							+ " '" + psfdays_dissatisfied_email_sub + "',"
							+ " '" + psfdays_dissatisfied_email_format + "',"
							+ " '', " // psfdays_dissatisfied_email_exe_format
							+ " '" + psfdays_dissatisfied_sms_format + "',"
							+ " " + psfdays_entry_id + ","
							+ " '" + psfdays_entry_date + "')";
					// SOP("StrSql===" + StrSql);

					stmttx.addBatch(StrSql);

					UpdateCategory();
					UpdateType();
					stmttx.executeBatch();
					conntx.commit();
				} catch (Exception e) {
					if (conntx.isClosed()) {
						SOPError("conn is closed.....");
					}
					if (!conntx.isClosed() && conntx != null) {
						SOPError("connection rollback...\n sql--"
								+ StrSql);
					}
					msg = msg + "<br>Transaction Error!";
					SOPError("Axelaauto===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
				} finally {
					conntx.setAutoCommit(true);
					if (stmttx != null && !stmttx.isClosed()) {
						stmttx.close();
					}
					if (conntx != null && !conntx.isClosed()) {
						conntx.close();
					}

				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT psfdays_brand_id, psfdays_exe_type,psfdays_jccat_id, psfdays_emp_id,"
					+ " psfdays_daycount, psfdays_desc, psfdays_script, psfdays_active,"
					+ " psfdays_contactable_email_enable,"
					+ " psfdays_contactable_email_sub,"
					+ " psfdays_contactable_email_format,"
					+ " psfdays_contactable_sms_enable,"
					+ " psfdays_contactable_sms_format,"
					+ " psfdays_noncontactable_email_enable,"
					+ " psfdays_noncontactable_email_sub,"
					+ " psfdays_noncontactable_email_format,"
					+ " psfdays_noncontactable_sms_enable,"
					+ " psfdays_noncontactable_sms_format,"

					+ " psfdays_satisfied_email_enable,"
					+ " psfdays_satisfied_email_sub,"
					+ " psfdays_satisfied_email_format,"
					+ " psfdays_satisfied_sms_enable,"
					+ " psfdays_satisfied_sms_format,"
					+ " psfdays_dissatisfied_email_enable,"
					+ " psfdays_dissatisfied_email_sub,"
					+ " psfdays_dissatisfied_email_format,"
					+ " psfdays_dissatisfied_sms_enable, "
					+ " psfdays_dissatisfied_sms_format,"
					+ " psfdays_entry_id, psfdays_entry_date,"
					+ " psfdays_modified_id, psfdays_modified_date FROM " + compdb(comp_id) + "axela_service_jc_psfdays"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_cat ON jccat_id = psfdays_jccat_id"
					+ " WHERE psfdays_id = " + psfdays_id + "";
			// SOP("StrSql==pop==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					psfdays_brand_id = crs.getString("psfdays_brand_id");
					psfdays_exe_type = crs.getString("psfdays_exe_type");
					psfdays_jccat_id = crs.getString("psfdays_jccat_id");
					psfdays_emp_id = crs.getString("psfdays_emp_id");
					psfdays_daycount = crs.getString("psfdays_daycount");
					psfdays_desc = crs.getString("psfdays_desc");
					psfdays_script = crs.getString("psfdays_script");
					psfdays_active = crs.getString("psfdays_active");
					psfdays_contactable_email_enable = crs.getString("psfdays_contactable_email_enable");
					psfdays_contactable_email_sub = crs.getString("psfdays_contactable_email_sub");
					psfdays_contactable_email_format = crs.getString("psfdays_contactable_email_format");
					psfdays_contactable_sms_enable = crs.getString("psfdays_contactable_sms_enable");
					psfdays_contactable_sms_format = crs.getString("psfdays_contactable_sms_format");
					psfdays_noncontactable_email_enable = crs.getString("psfdays_noncontactable_email_enable");
					psfdays_noncontactable_email_sub = crs.getString("psfdays_noncontactable_email_sub");
					psfdays_noncontactable_email_format = crs.getString("psfdays_noncontactable_email_format");
					psfdays_noncontactable_sms_enable = crs.getString("psfdays_noncontactable_sms_enable");
					psfdays_noncontactable_sms_format = crs.getString("psfdays_noncontactable_sms_format");
					psfdays_satisfied_email_enable = crs.getString("psfdays_satisfied_email_enable");
					psfdays_satisfied_email_sub = crs.getString("psfdays_satisfied_email_sub");
					psfdays_satisfied_email_format = crs.getString("psfdays_satisfied_email_format");
					psfdays_satisfied_sms_enable = crs.getString("psfdays_satisfied_sms_enable");
					psfdays_satisfied_sms_format = crs.getString("psfdays_satisfied_sms_format");
					psfdays_dissatisfied_email_enable = crs.getString("psfdays_dissatisfied_email_enable");
					psfdays_dissatisfied_email_sub = crs.getString("psfdays_dissatisfied_email_sub");
					psfdays_dissatisfied_email_format = crs.getString("psfdays_dissatisfied_email_format");
					psfdays_dissatisfied_sms_enable = crs.getString("psfdays_dissatisfied_sms_enable");
					psfdays_dissatisfied_sms_format = crs.getString("psfdays_dissatisfied_sms_format");
					// psfdays_lostfollowup = crs.getString("psfdays_lostfollowup");
					psfdays_entry_id = crs.getString("psfdays_entry_id");
					if (!psfdays_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(psfdays_entry_id));
						entry_date = strToLongDate(crs.getString("psfdays_entry_date"));
					}
					psfdays_modified_id = crs.getString("psfdays_modified_id");
					if (!psfdays_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(psfdays_modified_id));
						modified_date = strToLongDate(crs.getString("psfdays_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid CRM Follow-up Day!"));
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields() {
		CheckForm();
		try {
			if (msg.equals("")) {
				try {
					conntx = connectDB();
					conntx.setAutoCommit(false);
					stmttx = conntx.createStatement();
					StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc_psfdays"
							+ " SET"
							+ " psfdays_brand_id = '" + psfdays_brand_id + "',"
							// + " crmdays_crmtype_id = '" + crmdays_crmtype_id + "',"
							+ " psfdays_exe_type = '" + psfdays_exe_type + "',"
							+ " psfdays_jccat_id = '" + psfdays_jccat_id + "',"
							+ " psfdays_emp_id = '" + psf_exe_id + "',"
							+ " psfdays_daycount = " + psfdays_daycount + ","
							+ " psfdays_desc = '" + psfdays_desc + "',"
							+ " psfdays_script = '" + psfdays_script + "',"
							// + " psfdays_lostfollowup = '" + psfdays_lostfollowup + "',"
							+ " psfdays_active = '" + psfdays_active + "',"
							+ " psfdays_contactable_email_enable = '" + psfdays_contactable_email_enable + "',"
							+ " psfdays_contactable_sms_enable = '" + psfdays_contactable_sms_enable + "',"
							+ " psfdays_noncontactable_email_enable = '" + psfdays_noncontactable_email_enable + "',"
							+ " psfdays_noncontactable_sms_enable = '" + psfdays_noncontactable_sms_enable + "',"
							+ " psfdays_satisfied_email_enable = '" + psfdays_satisfied_email_enable + "',"
							+ " psfdays_satisfied_sms_enable = '" + psfdays_satisfied_sms_enable + "',"
							+ " psfdays_dissatisfied_email_enable = '" + psfdays_dissatisfied_email_enable + "',"
							+ " psfdays_dissatisfied_sms_enable = '" + psfdays_dissatisfied_sms_enable + "',"
							+ " psfdays_modified_id = " + psfdays_modified_id + ","
							+ " psfdays_modified_date = '" + psfdays_modified_date + "'"
							+ " WHERE psfdays_id = " + psfdays_id + "";
					stmttx.addBatch(StrSql);

					// if (psf_cat_trans != null) {
					UpdateCategory();
					// }
					// if (psf_type_trans != null) {
					UpdateType();
					// }
					stmttx.executeBatch();
					conntx.commit();
				} catch (Exception e) {
					if (conntx.isClosed()) {
						SOPError("conn is closed.....");
					}
					if (!conntx.isClosed() && conntx != null) {
						SOPError("connection rollback...\n sql--"
								+ StrSql);
					}
					msg = msg + "<br>Transaction Error!";
					SOPError("Axelaauto===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
				} finally {
					conntx.setAutoCommit(true);
					if (stmttx != null && !stmttx.isClosed()) {
						stmttx.close();
					}
					if (conntx != null && !conntx.isClosed()) {
						conntx.close();
					}

				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void DeleteFields() {

		StrSql = "SELECT jcpsf_psfdays_id FROM " + compdb(comp_id) + "axela_service_jc_psf WHERE jcpsf_psfdays_id= " + psfdays_id;
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>CRM days is associated with CRM Follow-up!";
		}
		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_jc_psfdays WHERE psfdays_id =" + psfdays_id;
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public void UpdateCategory() throws SQLException {
		if (msg.equals("")) {
			try {
				stmttx.addBatch("DELETE FROM " + compdb(comp_id)
						+ "axela_psfdayscattrans" + " WHERE psfdayscattrans_psfdays_id = " + psfdays_id);
				if (psf_cat_trans != null) {
					for (int i = 0; i < psf_cat_trans.length; i++) {
						stmttx.addBatch("INSERT INTO " + compdb(comp_id) + "axela_psfdayscattrans"
								+ " (psfdayscattrans_psfdays_id," + " psfdayscattrans_jccat_id)" + " VALUES" + " (" + psfdays_id
								+ "," + " " + psf_cat_trans[i] + ")");
						// //SOP("StrSql=====444444444============" + StrSql);
					}
				}
			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("conn is closed.....");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					SOPError("Connection rollback...");
				}
				msg = "<br>Transaction Error!";
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
			}
		}
	}

	public void UpdateType() throws SQLException {
		if (msg.equals("")) {
			try {
				stmttx.addBatch("DELETE FROM " + compdb(comp_id)
						+ "axela_psfdaystypetrans" + " WHERE psfdaytypestrans_psfdays_id = " + psfdays_id);
				if (psf_type_trans != null) {
					for (int i = 0; i < psf_type_trans.length; i++) {
						stmttx.addBatch("INSERT INTO " + compdb(comp_id) + "axela_psfdaystypetrans"
								+ " (psfdaytypestrans_psfdays_id," + " psfdaytypestrans_jctype_id)" + " VALUES" + " (" + psfdays_id
								+ "," + " " + psf_type_trans[i] + ")");
						// //SOP("StrSql=====444444444============" + StrSql);
					}
				}
			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("conn is closed.....");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					SOPError("Connection rollback...");
				}
				msg = "<br>Transaction Error!";
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
			}
		}
	}

	public String PopulateBrand(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT brand_id, brand_name"
					+ " FROM axela_brand"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE branch_active = 1"
					+ " AND branch_branchtype_id IN (1,3)"
					+ " GROUP BY brand_id"
					+ " ORDER BY brand_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("brand_id"));
				Str.append(StrSelectdrop(crs.getString("brand_id"), psfdays_brand_id));
				Str.append(">").append(crs.getString("brand_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateCRMType(String crmdays_crmtype_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT crmtype_id, crmtype_name"
					+ " FROM axela_sales_crm_type ORDER BY crmtype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("crmtype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("crmtype_id"), crmdays_crmtype_id));
				Str.append(">").append(crs.getString("crmtype_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateExecutive(String psfdays_emp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_psfdays ON emp_id = psfdays_emp_id"
					+ " WHERE 1 = 1 "
					+ " AND emp_service_psf = 1"
					+ " AND emp_active = 1"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(StrSelectdrop(crs.getString("emp_id"), psfdays_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateSelectedExecutives() {

		StringBuilder Str = new StringBuilder();
		try {
			if (update.equals("yes") && !updateB.equals("yes") && !psfdays_emp_id.equals("")) {
				StrSql = "SELECT emp_id, emp_name"
						+ " FROM " + compdb(comp_id) + "axela_emp "
						+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_psfdays ON emp_id = psfdays_emp_id"
						+ " WHERE emp_id IN  (" + psfdays_emp_id + ")"
						+ " AND emp_service_psf = 1"
						+ " AND emp_active = 1"
						+ " GROUP BY emp_id"
						+ " ORDER BY emp_name";
				// SOP("StrSql===" + StrSql);

			}
			CachedRowSet crs = processQuery(StrSql, 0);
			// Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				if (update.equals("yes") && !updateB.equals("yes") && !psfdays_emp_id.equals("")) {
					Str.append("<option value=").append(crs.getString("emp_id"));
					Str.append(" selected>").append(crs.getString("emp_name"));
					Str.append("</option> \n");
				}
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateCRMExecutiveType(String crmdays_exe_type) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>");
		Str.append("<option value=1").append(StrSelectdrop("1", crmdays_exe_type)).append(">PSF Executive</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", crmdays_exe_type)).append(">Service Advisior</option>\n");
		return Str.toString();
	}

	public String PopulateCategory(String psfdays_jccat_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT jccat_id, jccat_name"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_cat "
					+ " GROUP BY jccat_id"
					+ " ORDER BY jccat_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("jccat_id")).append("");
				Str.append(StrSelectdrop(crs.getString("jccat_id"), psfdays_jccat_id));
				Str.append(">").append(crs.getString("jccat_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateTransCategory() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT jccat_id, jccat_name"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_cat "
					+ " GROUP BY jccat_id"
					+ " ORDER BY jccat_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("jccat_id"));
				Str.append(">").append(crs.getString("jccat_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateSelectedCategory() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT jccat_id, jccat_name"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_cat"
					+ " INNER JOIN " + compdb(comp_id) + "axela_psfdayscattrans ON psfdayscattrans_jccat_id = jccat_id"
					+ " WHERE psfdayscattrans_psfdays_id = " + psfdays_id + ""
					+ " ORDER BY jccat_name";

			if ((add.equals("yes") || updateB.equals("yes"))
					&& psf_cat_trans != null) {
				StrSql = "SELECT jccat_id, jccat_name"
						+ " FROM " + compdb(comp_id) + "axela_service_jc_cat"
						+ " WHERE 1 = 1"
						+ " ORDER BY jccat_name";
			}
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				if ((add.equals("yes") || updateB.equals("yes"))
						&& psf_cat_trans != null) {
					for (int i = 0; i < psf_cat_trans.length; i++) {
						if (crs.getString("jccat_id").equals(psf_cat_trans[i])) {
							Str.append("<option value=").append(crs.getString("jccat_id"));
							Str.append(" selected>").append(crs.getString("jccat_name"));
							Str.append("</option>\n");
						}
					}
				} else if (update.equals("yes") && !updateB.equals("yes")) {
					Str.append("<option value=").append(crs.getString("jccat_id"));
					Str.append(" selected>").append(crs.getString("jccat_name"));
					Str.append("</option> \n");
				}
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateTransType() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT jctype_id, jctype_name"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_type "
					+ " GROUP BY jctype_id"
					+ " ORDER BY jctype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("jctype_id"));
				Str.append(">").append(crs.getString("jctype_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateSelectedType() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT jctype_id, jctype_name"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_type"
					+ " INNER JOIN " + compdb(comp_id) + "axela_psfdaystypetrans ON psfdaytypestrans_jctype_id = jctype_id"
					+ " WHERE psfdaytypestrans_psfdays_id = " + psfdays_id + ""
					+ " ORDER BY jctype_name";

			if ((add.equals("yes") || updateB.equals("yes"))
					&& psf_cat_trans != null) {
				StrSql = "SELECT jctype_id, jctype_name"
						+ " FROM " + compdb(comp_id) + "axela_service_jc_type"
						+ " WHERE 1 = 1"
						+ " ORDER BY jctype_name";
			}
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				if ((add.equals("yes") || updateB.equals("yes"))
						&& psf_type_trans != null) {
					for (int i = 0; i < psf_type_trans.length; i++) {
						if (crs.getString("jctype_id").equals(psf_type_trans[i])) {
							Str.append("<option value=").append(crs.getString("jctype_id"));
							Str.append(" selected>").append(crs.getString("jctype_name"));
							Str.append("</option>\n");
						}
					}
				} else if (update.equals("yes") && !updateB.equals("yes")) {
					Str.append("<option value=").append(crs.getString("jctype_id"));
					Str.append(" selected>").append(crs.getString("jctype_name"));
					Str.append("</option> \n");
				}
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
