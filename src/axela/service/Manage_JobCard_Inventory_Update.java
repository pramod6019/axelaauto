//Bhagwan Singh 5 march 2013
package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Manage_JobCard_Inventory_Update extends Connect {

	public String add = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String msg = "";
	public String invent_id = "0";
	public String invent_name = "";
	public String invent_entry_id = "0";
	public String invent_model_id = "0";
	public String invent_entry_by = "";
	public String invent_entry_date = "";
	public String invent_modified_id = "0";
	public String invent_modified_by = "";
	public String invent_modified_date = "";
	public String entry_date = "";
	public String modified_date = "";
	public String QueryString = "";
	public String StrSql = "";
	public String invent_model_name = "";
	public String model_id = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_service_jobcard_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				invent_id = CNumeric(PadQuotes(request.getParameter("invent_id")));
				invent_model_id = CNumeric(PadQuotes(request.getParameter("invent_model_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if ("yes".equals(addB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_service_jobcard_add", request).equals("1")) {
							invent_entry_id = emp_id;
							invent_entry_date = ToLongDate(kknow());
							AddFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("manage-jobcard-inventory-list.jsp?invent_model_id=" + invent_model_id + "&msg=Inventory added Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}

				if ("yes".equals(update)) {
					if (!"yes".equals(updateB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Inventory".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_service_jobcard_edit", request).equals("1")) {
							invent_modified_id = emp_id;
							invent_modified_date = ToLongDate(kknow());
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("manage-jobcard-inventory-list.jsp?invent_model_id=" + invent_model_id + "&msg=Inventory updated Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}

					if ("Delete Inventory".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_service_jobcard_delete", request).equals("1")) {
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("manage-jobcard-inventory-list.jsp?invent_model_id=" + invent_model_id + "&msg=Inventory deleted Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
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
		invent_name = PadQuotes(request.getParameter("txt_invent_name"));
		invent_model_id = CNumeric(PadQuotes(request.getParameter("dr_invent_model_id")));
		invent_entry_by = PadQuotes(request.getParameter("entry_by"));
		invent_modified_by = PadQuotes(request.getParameter("modified_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";

		if (invent_model_id.equals("0") || invent_model_id.equals("")) {
			msg += "<br>Select Model!";
		}

		if (invent_name.equals("")) {
			msg += "<br>Enter Inventory Name!";
		}

		try {
			if (!invent_name.equals("")) {
				StrSql = "SELECT invent_name FROM " + compdb(comp_id) + "axela_service_jc_invent"
						+ " WHERE invent_name = '" + invent_name + "'"
						+ " AND invent_model_id = " + invent_model_id + "";
				if (update.equals("yes")) {
					StrSql += " AND invent_id != " + invent_id + "";
				}

				if (!ExecuteQuery(StrSql).equals("")) {
					msg += "<br>Similar Inventory Name Found!";
				}
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
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_invent"
						+ " (invent_name,"
						+ " invent_model_id,"
						+ " invent_entry_id,"
						+ " invent_entry_date)"
						+ " VALUES"
						+ " ('" + invent_name + "',"
						+ " '" + invent_model_id + "',"
						+ " " + invent_entry_id + ","
						+ " '" + invent_entry_date + "')";
				invent_id = UpdateQueryReturnID(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateModel(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " ORDER BY model_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=0>Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id"));
				Str.append(StrSelectdrop(crs.getString("model_id"), invent_model_id));
				Str.append(">").append(crs.getString("model_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT invent_name, invent_model_id, model_name, invent_entry_id,"
					+ " invent_entry_date, invent_modified_id, invent_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_invent"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model on model_id = invent_model_id"
					+ " WHERE invent_id = " + invent_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					invent_name = crs.getString("invent_name");
					invent_model_id = crs.getString("invent_model_id");
					invent_model_name = crs.getString("model_name");
					invent_entry_id = crs.getString("invent_entry_id");
					if (!invent_entry_id.equals("")) {
						invent_entry_by = Exename(comp_id, Integer.parseInt(invent_entry_id));
					}
					entry_date = strToLongDate(crs.getString("invent_entry_date"));
					invent_modified_id = crs.getString("invent_modified_id");
					if (!invent_modified_id.equals("")) {
						invent_modified_by = Exename(comp_id, Integer.parseInt(invent_modified_id));
					}
					modified_date = strToLongDate(crs.getString("invent_modified_date"));
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Inventory"));
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
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc_invent"
						+ " SET"
						+ " invent_name = '" + invent_name + "',"
						+ " invent_model_id = '" + invent_model_id + "',"
						+ " invent_modified_id = " + invent_modified_id + ","
						+ " invent_modified_date = '" + invent_modified_date + "'"
						+ " WHERE invent_id = " + invent_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		StrSql = "SELECT inventtrans_invent_id"
				+ " FROM " + compdb(comp_id) + "axela_service_jc_invent_trans"
				+ " WHERE inventtrans_invent_id = " + invent_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg += "<br>Inventry is Associated with Vehicle!";
		}

		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_jc_invent"
						+ " WHERE invent_id = " + invent_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
}
