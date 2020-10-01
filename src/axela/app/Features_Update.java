package axela.app;
//aJIt

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Features_Update extends Connect {

	public String add = "";
	public String update = "";
	public String addB = "";
	public String updateB = "";
	public String deleteB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String feature_id = "0";
	public String feature_name = "";
	public String feature_desc = "";
	public String feature_active = "0";
	public String feature_entry_date = "";
	public String feature_modified_date = "";
	public String feature_entry_id = "0";
	public String entry_date = "";
	public String feature_modified_id = "0";
	public String modified_date = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_role_id = "0";
	public String feature_entry_by = "";
	public String feature_modified_by = "";
	public String QueryString = "";
	public String feature_model_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				CheckPerm(comp_id, "emp_role_id", request, response);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				feature_id = CNumeric(PadQuotes(request.getParameter("feature_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
					if (!addB.equals("yes")) {
						feature_active = "1";
					} else {
						if (ReturnPerm(comp_id, "emp_role_id", request).equals("1")) {
							GetValues(request, response);
							feature_entry_id = emp_id;
							feature_entry_date = ToLongDate(kknow());
							AddFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("features-list.jsp?feature_id=" + feature_id + "&msg=Features added successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
				if ("yes".equals(update)) {
					status = "Update";
					if (!"yes".equals(updateB) && !"Delete Features".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Features".equals(deleteB)) {
						if (ReturnPerm(comp_id, "emp_role_id", request).equals("1")) {
							GetValues(request, response);
							feature_modified_id = emp_id;
							feature_modified_date = ToLongDate(kknow());
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("../app/features-list.jsp?feature_id=" + feature_id + "&msg=Features Updated Successfully!" + msg + ""));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}

					} else if ("Delete Features".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("features-list.jsp?msg=Features Deleted Successfully!"));
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
		feature_name = PadQuotes(request.getParameter("txt_feature_name"));
		feature_model_id = CNumeric(PadQuotes(request.getParameter("drop_feature_model_id")));
		feature_desc = PadQuotes(request.getParameter("txt_feature_desc"));
		feature_active = CheckBoxValue(PadQuotes(request.getParameter("chk_feature_active")));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
		feature_entry_by = unescapehtml(PadQuotes(request.getParameter("feature_entry_by")));
		feature_modified_by = PadQuotes(request.getParameter("feature_modified_by"));
	}

	protected void CheckForm() {
		msg = "";
		if (feature_name.equals("")) {
			msg += "<br>Enter features Name!";
		} else {
			StrSql = "SELECT feature_name FROM " + compdb(comp_id) + "axela_app_model_feature"
					+ " WHERE feature_name = '" + feature_name + "'"
					+ " AND feature_model_id = " + feature_model_id + "";
			if (update.equals("yes")) {
				StrSql += " AND feature_id != " + feature_id + "";
			}

			if (ExecuteQuery(StrSql).equals(feature_name)) {
				msg += "<br>Similar features found!";
			}
		}
		if (feature_model_id.equals("0")) {
			msg += "<br>Select Model!";
		}
	}
	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			feature_id = ExecuteQuery("SELECT COALESCE(MAX(feature_id), 0) + 1 FROM " + compdb(comp_id) + "axela_app_model_feature");
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_app_model_feature"
					+ " (feature_id,"
					+ " feature_name,"
					+ " feature_model_id,"
					+ " feature_desc,"
					+ " feature_active,"
					+ " feature_entry_id,"
					+ " feature_entry_date)"
					+ " VALUES"
					+ " (" + feature_id + ","
					+ " '" + feature_name + "',"
					+ " " + feature_model_id + ","
					+ " '" + feature_desc.replace("&nbsp;", "").replace("br /", "") + "',"
					+ " '" + feature_active + "',"
					+ " '" + feature_entry_id + "',"
					+ " '" + feature_entry_date + "')";
			// SOP("StrSql-------" + StrSql);
			updateQuery(StrSql);
		}
	}
	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT feature_id, feature_name, feature_model_id,"
					+ " feature_desc, feature_active,"
					+ " feature_entry_id, feature_entry_date, feature_modified_id, feature_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_app_model_feature"
					+ " WHERE feature_id = " + feature_id + "";
			CachedRowSet crs =processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					feature_name = crs.getString("feature_name");
					feature_model_id = crs.getString("feature_model_id");
					feature_desc = crs.getString("feature_desc");
					feature_active = crs.getString("feature_active");
					feature_entry_id = crs.getString("feature_entry_id");
					if (!feature_entry_id.equals("0")) {
						feature_entry_by = Exename(comp_id, Integer.parseInt(feature_entry_id));
					}
					entry_date = strToLongDate(crs.getString("feature_entry_date"));
					feature_modified_id = crs.getString("feature_modified_id");
					if (!feature_modified_id.equals("0")) {
						feature_modified_by = Exename(comp_id, Integer.parseInt(feature_modified_id));
						modified_date = strToLongDate(crs.getString("feature_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Item!"));
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
			StrSql = "UPDATE " + compdb(comp_id) + "axela_app_model_feature"
					+ " SET"
					+ " feature_name = '" + feature_name + "',"
					+ " feature_model_id = " + feature_model_id + ","
					+ " feature_desc = '" + feature_desc + "',"
					+ " feature_active = '" + feature_active + "',"
					+ " feature_modified_id = '" + feature_modified_id + "',"
					+ " feature_modified_date = '" + feature_modified_date + "'"
					+ " WHERE feature_id = " + feature_id + "";
			updateQuery(StrSql);
		}
	}
	protected void DeleteFields() {

		if (msg.equals("")) {
			try {

				StrSql = "Delete FROM " + compdb(comp_id) + "axela_app_model_feature WHERE feature_id = " + feature_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
	public String PopulateModel() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " WHERE model_active = 1"
					+ " GROUP BY model_id"
					+ " ORDER BY model_name";
			CachedRowSet crs =processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id"));
				Str.append(StrSelectdrop(crs.getString("model_id"), feature_model_id));
				Str.append(">").append(crs.getString("model_name")).append("</option>\n");
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
