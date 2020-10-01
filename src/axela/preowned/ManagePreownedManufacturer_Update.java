package axela.preowned;
/*
 * @author Bhagwan Singh (10th July 2013)
 */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManagePreownedManufacturer_Update extends Connect {

	public String add = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String manuf_id = "0";
	public String carmanuf_id = "0";
	public String carmanuf_name = "";
	public String carmanuf_entry_id = "0";
	public String carmanuf_entry_by = "";
	public String carmanuf_entry_date = "";
	public String carmanuf_modified_id = "0";
	public String carmanuf_modified_by = "";
	public String carmanuf_modified_date = "";
	public String entry_by = "", modified_by = "";
	public String QueryString = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_role_id, emp_preowned_stock_add", request, response);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				carmanuf_id = CNumeric(PadQuotes(request.getParameter("carmanuf_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
					} else {
						GetValues(request, response);
						carmanuf_entry_id = emp_id;
						carmanuf_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managepreownedmanufacturer.jsp?carmanuf_id=" + carmanuf_id + "&msg=Pre-Owned Manufacturer Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Pre-Owned Manufacturer".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Pre-Owned Manufacturer".equals(deleteB)) {
						GetValues(request, response);

						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managepreownedmanufacturer.jsp?carmanuf_id=" + carmanuf_id + "&msg=Pre-Owned Manufacturer Updated Successfully!"));
						}
					}

					if ("Delete Pre-Owned Manufacturer".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managepreownedmanufacturer.jsp?msg=Pre-Owned Manufacturer Deleted Successfully!"));
						}
					}
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		carmanuf_name = PadQuotes(request.getParameter("txt_carmanuf_name"));
		carmanuf_entry_by = PadQuotes(request.getParameter("entry_by"));
		carmanuf_modified_by = PadQuotes(request.getParameter("modified_by"));
		carmanuf_entry_date = PadQuotes(request.getParameter("entry_date"));
		carmanuf_modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (carmanuf_name.equals("")) {
			msg = "<br>Enter Manufacturer!";
		}
		try {
			if (!carmanuf_name.equals("")) {
				StrSql = "SELECT carmanuf_name"
						+ " FROM axela_preowned_manuf"
						+ " WHERE carmanuf_name = '" + carmanuf_name + "'";
				if (update.equals("yes")) {
					StrSql = StrSql + " AND carmanuf_id != " + carmanuf_id + "";
				}
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					msg = msg + "<br>Similar Manufacturer Found! ";
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				carmanuf_id = ExecuteQuery("SELECT COALESCE(MAX(carmanuf_id),0)+1 AS carmanuf_id FROM axela_preowned_manuf");
				// SOP("carmanuf_id==AddFields==" + carmanuf_id);
				StrSql = "INSERT INTO axela_preowned_manuf"
						+ "(carmanuf_id,"
						+ " carmanuf_name,"
						+ " carmanuf_entry_id,"
						+ " carmanuf_entry_date)"
						+ " values"
						+ " (" + carmanuf_id + ","
						+ " '" + carmanuf_name + "',"
						+ " " + carmanuf_entry_id + ","
						+ " '" + carmanuf_entry_date + "')";
				// SOP("StrSql==AddFields" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT carmanuf_id, carmanuf_name, carmanuf_entry_id, carmanuf_entry_date, carmanuf_modified_id,"
					+ " carmanuf_modified_date"
					+ " FROM axela_preowned_manuf"
					+ " WHERE carmanuf_id = " + carmanuf_id + "";

			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					carmanuf_id = crs.getString("carmanuf_id");
					carmanuf_name = crs.getString("carmanuf_name");
					carmanuf_entry_id = crs.getString("carmanuf_entry_id");
					if (!carmanuf_entry_id.equals("")) {
						carmanuf_entry_by = Exename(comp_id, Integer.parseInt(carmanuf_entry_id));
					}
					carmanuf_entry_date = strToLongDate(crs.getString("carmanuf_entry_date"));
					carmanuf_modified_id = crs.getString("carmanuf_modified_id");
					if (!carmanuf_modified_id.equals("")) {
						carmanuf_modified_by = Exename(comp_id, Integer.parseInt(carmanuf_modified_id));
					}
					carmanuf_modified_date = strToLongDate(crs.getString("carmanuf_modified_date"));
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Used Manufacturer!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				carmanuf_modified_id = emp_id;
				carmanuf_modified_date = ToLongDate(kknow());
				StrSql = "UPDATE axela_preowned_manuf"
						+ " SET"
						+ " carmanuf_name = '" + carmanuf_name + "',"
						+ " carmanuf_modified_id = " + carmanuf_modified_id + ","
						+ " carmanuf_modified_date = '" + carmanuf_modified_date + "'"
						+ " WHERE carmanuf_id = " + carmanuf_id + "";
				updateQuery(StrSql);

			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {

		StrSql = "SELECT preownedmodel_id"
				+ " FROM axela_preowned_model"
				+ " WHERE preownedmodel_carmanuf_id = " + carmanuf_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Pre-Owned Manufacturer is associated with Model!";
		}
		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM axela_preowned_manuf"
						+ " WHERE carmanuf_id = " + carmanuf_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

}
