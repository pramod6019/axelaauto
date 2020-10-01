package axela.app;
//aJIt

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Model_Colours_Update extends Connect {

	public String add = "";
	public String update = "";
	public String addB = "";
	public String updateB = "";
	public String deleteB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String colours_id = "0";
	public String colours_title = "";
	public String colours_colour = "";
	public String colours_value = "0";
	public String colours_entry_date = "";
	public String colours_modified_date = "";
	public String colours_entry_id = "0";
	public String entry_date = "";
	public String colours_modified_id = "0";
	public String modified_date = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_role_id = "0";
	public String colours_entry_by = "";
	public String colours_modified_by = "";
	public String QueryString = "";
	public String colours_model_id = "0";

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
				colours_id = CNumeric(PadQuotes(request.getParameter("colours_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
					if (!addB.equals("yes")) {
						colours_value = "1";
					} else {
						if (ReturnPerm(comp_id, "emp_role_id", request).equals("1")) {
							GetValues(request, response);
							colours_entry_id = emp_id;
							colours_entry_date = ToLongDate(kknow());
							AddFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("model-colours-list.jsp?colours_id=" + colours_id + "&msg=Colour added successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
				if ("yes".equals(update)) {
					status = "Update";
					if (!"yes".equals(updateB) && !"Delete Colours".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Colours".equals(deleteB)) {
						if (ReturnPerm(comp_id, "emp_role_id", request).equals("1")) {
							GetValues(request, response);
							colours_modified_id = emp_id;
							colours_modified_date = ToLongDate(kknow());
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("../app/model-colours-list.jsp?colours_id=" + colours_id + "&msg=Colour Updated Successfully!" + msg + ""));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}

					} else if ("Delete Colours".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("model-colours-list.jsp?msg=Colour Deleted Successfully!"));
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
		colours_title = PadQuotes(request.getParameter("txt_colour_title"));
		colours_model_id = CNumeric(PadQuotes(request.getParameter("dr_colours_model_id")));
		colours_colour = PadQuotes(request.getParameter("txt_colours_colour"));
		// colours_value = CheckBoxValue(PadQuotes(request.getParameter("chk_feature_active")));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
		colours_entry_by = unescapehtml(PadQuotes(request.getParameter("colours_entry_by")));
		colours_modified_by = PadQuotes(request.getParameter("colours_modified_by"));
	}

	protected void CheckForm() {
		msg = "";
		if (colours_title.equals("")) {
			msg += "<br>Enter Colours Name!";
		} else {
			StrSql = "SELECT colours_title FROM " + compdb(comp_id) + "axela_app_model_colours"
					+ " WHERE colours_title = '" + colours_title + "'"
					+ " AND colours_model_id = " + colours_model_id + "";
			if (update.equals("yes")) {
				StrSql += " AND colours_id != " + colours_id + "";
			}

			if (ExecuteQuery(StrSql).equals(colours_title)) {
				msg += "<br>Similar Colour found!";
			}
		}
		if (colours_model_id.equals("0")) {
			msg += "<br>Select Model!";
		}

		if (colours_colour.equals("")) {
			msg += "<br>Enter Colour Code!";
		}
	}
	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			colours_id = ExecuteQuery("SELECT COALESCE(MAX(colours_id), 0) + 1 FROM " + compdb(comp_id) + "axela_app_model_colours");
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_app_model_colours"
					+ " (colours_title,"
					+ " colours_model_id,"
					+ " colours_colour,"
					+ " colours_value,"
					+ " colours_entry_id,"
					+ " colours_entry_date)"
					+ " VALUES"
					+ " ("
					+ " '" + colours_title + "',"
					+ " " + colours_model_id + ","
					+ " '" + colours_colour + "',"
					+ " '" + colours_value + "',"
					+ " '" + colours_entry_id + "',"
					+ " '" + colours_entry_date + "')";
			// SOP("StrSql-------" + StrSql);

			updateQuery(StrSql);
		}
	}
	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT colours_id, colours_title, colours_model_id,"
					+ " colours_colour, colours_value,"
					+ " colours_entry_id, colours_entry_date, colours_modified_id, colours_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_app_model_colours"
					+ " WHERE colours_id = " + colours_id + "";
			CachedRowSet crs =processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					colours_title = crs.getString("colours_title");
					colours_model_id = crs.getString("colours_model_id");
					colours_colour = crs.getString("colours_colour");
					colours_value = crs.getString("colours_value");
					colours_entry_id = crs.getString("colours_entry_id");
					if (!colours_entry_id.equals("0")) {
						colours_entry_by = Exename(comp_id, Integer.parseInt(colours_entry_id));
					}
					entry_date = strToLongDate(crs.getString("colours_entry_date"));
					colours_modified_id = crs.getString("colours_modified_id");
					if (!colours_modified_id.equals("0")) {
						colours_modified_by = Exename(comp_id, Integer.parseInt(colours_modified_id));
						modified_date = strToLongDate(crs.getString("colours_modified_date"));
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
			StrSql = "UPDATE " + compdb(comp_id) + "axela_app_model_colours"
					+ " SET"
					+ " colours_title = '" + colours_title + "',"
					+ " colours_model_id = " + colours_model_id + ","
					+ " colours_colour = '" + colours_colour + "',"
					// + " colours_value = '" + colours_value + "',"
					+ " colours_modified_id = '" + colours_modified_id + "',"
					+ " colours_modified_date = '" + colours_modified_date + "'"
					+ " WHERE colours_id = " + colours_id + "";
			SOP("strsql==UpdateFields===" + StrSql);
			updateQuery(StrSql);
		}
	}
	protected void DeleteFields() {

		if (msg.equals("")) {
			try {

				StrSql = "Delete FROM " + compdb(comp_id) + "axela_app_model_feature WHERE colours_id = " + colours_id + "";
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
				Str.append(StrSelectdrop(crs.getString("model_id"), colours_model_id));
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
