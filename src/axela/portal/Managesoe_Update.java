package axela.portal;
//Murali 21st jun

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Managesoe_Update extends Connect {

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
	public String branch_id = "0";
	public String soe_id = "0";
	public String soe_name = "";
	public String soe_active = "";
	public String soe_crm_enable = "";
	public String QueryString = "";
	public String soe_entry_id = "0";
	public String soe_entry_date = "";
	public String soe_modified_id = "0";
	public String soe_modified_date = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";
	public String[] soe_sob_trans = new String[10];
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				CheckPerm(comp_id, "emp_role_id", request, response);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				soe_id = CNumeric(PadQuotes(request.getParameter("soe_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						soe_name = "";
						soe_active = "1";
					} else {
						GetValues(request, response);
						soe_entry_id = emp_id;
						soe_entry_date = ToLongDate(kknow());
						AddFields();
						UpdateSob();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managesoe.jsp?soe_id=" + soe_id + "&msg= SOE Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete SOE".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete SOE".equals(deleteB)) {
						GetValues(request, response);
						soe_modified_id = emp_id;
						soe_modified_date = ToLongDate(kknow());
						UpdateFields();
						UpdateSob();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managesoe.jsp?soe_id=" + soe_id + "&msg=SOE Updated Successfully!"));
						}
					} else if ("Delete SOE".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						UpdateSob();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managesoe.jsp?msg=SOE Deleted Successfully!"));
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
		soe_name = PadQuotes(request.getParameter("txt_soe_name"));
		soe_sob_trans = request.getParameterValues("soe_sob_trans");
		soe_active = PadQuotes(request.getParameter("chk_soe_active"));
		if (soe_active.equals("on")) {
			soe_active = "1";
		} else {
			soe_active = "0";
		}

		soe_crm_enable = PadQuotes(request.getParameter("chk_crm_enable"));
		if (soe_crm_enable.equals("on")) {
			soe_crm_enable = "1";
		} else {
			soe_crm_enable = "0";
		}
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (soe_name.equals("")) {
			msg = msg + "<br>Enter SOE!";
		}
		try {
			if (!soe_name.equals("")) {
				StrSql = "Select soe_name from " + compdb(comp_id) + "axela_soe where soe_name = '" + soe_name + "'";
				if (update.equals("yes")) {
					StrSql = StrSql + " and soe_id != " + soe_id + "";
				}
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					msg = msg + "<br>Similar SOE Found! ";
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				soe_id = ExecuteQuery("Select (coalesce(max(soe_id),0)+1) from " + compdb(comp_id) + "axela_soe");
				StrSql = "Insert into " + compdb(comp_id) + "axela_soe"
						+ " (soe_id,"
						+ " soe_name,"
						+ " soe_crm_enable,"
						+ " soe_active,"
						+ " soe_entry_id,"
						+ " soe_entry_date)"
						+ " values"
						+ " (" + soe_id + ","
						+ " '" + soe_name + "',"
						+ " " + soe_crm_enable + ","
						+ " " + soe_active + ","
						+ " " + soe_entry_id + ","
						+ " '" + soe_entry_date + "')";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public void UpdateSob() {
		if (msg.equals("")) {
			try {
				StrSql = "Delete from " + compdb(comp_id) + "axela_soe_trans"
						+ " where soetrans_soe_id = " + soe_id;
				updateQuery(StrSql);
				if (soe_sob_trans != null) {
					for (int i = 0; i < soe_sob_trans.length; i++) {
						StrSql = "Insert into " + compdb(comp_id) + "axela_soe_trans"
								+ " (soetrans_soe_id,"
								+ " soetrans_sob_id)"
								+ " values"
								+ " (" + soe_id + ","
								+ " " + soe_sob_trans[i] + ")";
						updateQuery(StrSql);
					}
				}
			} catch (Exception ex) {
				SOPError(this.getClass().getName());
				SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "Select * from " + compdb(comp_id) + "axela_soe where soe_id = " + soe_id + "";
			// SOP("StrSql==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					soe_name = crs.getString("soe_name");
					soe_crm_enable = crs.getString("soe_crm_enable");
					soe_active = crs.getString("soe_active");
					soe_entry_id = crs.getString("soe_entry_id");
					if (!soe_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(soe_entry_id));
						entry_date = strToLongDate(crs.getString("soe_entry_date"));
					}
					soe_modified_id = crs.getString("soe_modified_id");
					if (!soe_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(soe_modified_id));
						modified_date = strToLongDate(crs.getString("soe_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid SOE!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = " UPDATE " + compdb(comp_id) + "axela_soe"
						+ " SET"
						+ " soe_name = '" + soe_name + "',"
						+ " soe_crm_enable= " + soe_crm_enable + ","
						+ " soe_active= " + soe_active + ","
						+ " soe_modified_id = " + soe_modified_id + ","
						+ " soe_modified_date = '" + soe_modified_date + "' "
						+ " where soe_id = " + soe_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		StrSql = "SELECT enquiry_soe_id FROM " + compdb(comp_id) + "axela_sales_enquiry where enquiry_soe_id = " + soe_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>SOE is Associated with Enquiry!";
		}
		StrSql = "SELECT lead_soe_id FROM " + compdb(comp_id) + "axela_sales_lead where lead_soe_id = " + soe_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>SOE is Associated with Lead!";
		}
		StrSql = "SELECT customer_soe_id FROM " + compdb(comp_id) + "axela_customer where customer_soe_id = " + soe_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>SOE is Associated with Customer!";
		}
		if (msg.equals("")) {
			try {
				StrSql = "Delete from " + compdb(comp_id) + "axela_soe where soe_id =" + soe_id + "";
				updateQuery(StrSql);

			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateSob() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "select sob_id, sob_name "
					+ " from " + compdb(comp_id) + "axela_sob "
					+ " where 1 = 1 "
					+ " group by sob_id"
					+ " order by sob_name ";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("sob_id")).append(">");
				Str.append(crs.getString("sob_name"));
				Str.append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateSobTrans() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT sob_id, sob_name"
					+ " FROM " + compdb(comp_id) + "axela_sob"
					+ " INNER JOIN " + compdb(comp_id) + "axela_soe_trans ON soetrans_sob_id = sob_id"
					+ " AND soetrans_soe_id = " + soe_id + ""
					// + " group by sob_id"
					+ " ORDER BY sob_name";
			// SOP("StrSql----------" + StrSql);

			if ((add.equals("yes") || updateB.equals("yes")) && soe_sob_trans != null) {
				StrSql = "SELECT sob_id, sob_name"
						+ " FROM " + compdb(comp_id) + "axela_sob"
						+ " WHERE 1=1"
						+ " ORDER BY sob_name";
				// SOP("StrSql-----trans------" + StrSqlBreaker(StrSql));
			}
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				if ((add.equals("yes") || updateB.equals("yes")) && soe_sob_trans != null) {
					for (int i = 0; i < soe_sob_trans.length; i++) {
						if (crs.getString("sob_id").equals(soe_sob_trans[i])) {
							Str.append("<option value=").append(crs.getString("sob_id"));
							Str.append("selected>").append(crs.getString("sob_name"));
							Str.append("</option>\n");
						}
					}
				} else if (update.equals("yes") && !updateB.equals("yes")) {
					Str.append("<option value=").append(crs.getString("sob_id"));
					Str.append(" selected>").append(crs.getString("sob_name"));
					Str.append("</option> \n");
				}
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
