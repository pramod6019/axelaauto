package axela.insurance;
//Shivaprasad 7July2014

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageInsuranceGift_Update extends Connect {

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
	public String insurgift_id = "0";
	public String insurgift_name = "";
	public String insurgift_active = "";
	public String QueryString = "";
	public String insurgift_entry_id = "0";
	public String insurgift_entry_date = "";
	public String insurgift_modified_id = "0";
	public String insurgift_modified_date = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				insurgift_id = CNumeric(PadQuotes(request.getParameter("insurgift_id")));
				QueryString = PadQuotes(request.getQueryString());
				// SOP("deleteB = " + deleteB);

				if (add.equals("yes")) {

				} else if (update.equals("yes")) {

				}

				if ("yes".equals(add)) {
					status = "Add";
					if (!"yes".equals(addB)) {
						insurgift_name = "";
						insurgift_active = "1";
					} else {
						GetValues(request, response);
						insurgift_entry_id = emp_id;
						insurgift_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manageinsurancegift.jsp?msg=Gift added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					status = "Update";
					if (!"yes".equals(updateB) && !"Delete Gift".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Gift".equals(deleteB)) {
						GetValues(request, response);
						insurgift_modified_id = emp_id;
						insurgift_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manageinsurancegift.jsp?insurgift_id=" + insurgift_id + "&msg=Gift updated Successfully!"));
						}
					} else if ("Delete Gift".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manageinsurancegift.jsp?insurgift_id=" + insurgift_id + "&msg=Gift deleted Successfully!"));
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
		insurgift_name = PadQuotes(request.getParameter("txt_insurgift_name"));
		insurgift_active = CheckBoxValue(PadQuotes(request.getParameter("chk_insurgift_active")));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		if (insurgift_name.equals("")) {
			msg = msg + "<br>Enter Gift!";
		}
		try {
			if (!insurgift_name.equals("")) {
				StrSql = "SELECT insurgift_name FROM " + compdb(comp_id) + "axela_insurance_gift"
						+ " WHERE insurgift_name = '" + insurgift_name + "'";
				if (update.equals("yes")) {
					StrSql = StrSql + " and insurgift_id != " + insurgift_id + "";
				}
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					msg = msg + "<br>Similar Gift Found! ";
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
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_gift"
						+ " (insurgift_name ,"
						+ " insurgift_active,"
						+ " insurgift_entry_id,"
						+ " insurgift_entry_date)"
						+ " VALUES"
						+ " ('" + insurgift_name + "',"
						+ " '" + insurgift_active + "',"
						+ " " + insurgift_entry_id + ","
						+ " '" + insurgift_entry_date + "')";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT insurgift_name, insurgift_active,"
					+ " insurgift_entry_id, insurgift_entry_date,"
					+ " insurgift_modified_id, insurgift_modified_date "
					+ " FROM " + compdb(comp_id) + "axela_insurance_gift "
					+ " WHERE insurgift_id = " + insurgift_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					insurgift_name = crs.getString("insurgift_name");
					insurgift_active = crs.getString("insurgift_active");
					insurgift_entry_id = crs.getString("insurgift_entry_id");
					if (!insurgift_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(insurgift_entry_id));
						entry_date = strToLongDate(crs.getString("insurgift_entry_date"));
					}
					insurgift_modified_id = crs.getString("insurgift_modified_id");
					if (!insurgift_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(insurgift_modified_id));
						modified_date = strToLongDate(crs.getString("insurgift_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Gift!"));
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

				StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_gift"
						+ " SET"
						+ " insurgift_name = '" + insurgift_name + "',"
						+ " insurgift_active = '" + insurgift_active + "',"
						+ " insurgift_modified_id = " + insurgift_modified_id + ","
						+ " insurgift_modified_date = '" + insurgift_modified_date + "'"
						+ " WHERE insurgift_id = " + insurgift_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
	protected void DeleteFields() {
		StrSql = "SELECT insurenquiry_insurgift_id FROM " + compdb(comp_id) + "axela_insurance_enquiry"
				+ " WHERE insurenquiry_insurgift_id = " + insurgift_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Gift is Associated with Enquiry!";
		}
		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_insurance_gift"
						+ " WHERE insurgift_id = " + insurgift_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
}
