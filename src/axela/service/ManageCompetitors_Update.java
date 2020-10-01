package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageCompetitors_Update extends Connect {

	public String updateB = "";
	public String update = "";
	public String StrSql = "";
	public String add = "";
	public String deleteB = "";
	public String addB = "";
	public static String status = "";
	public String msg = "";
	public String branch_id = "";
	public String brand_id = "0";
	public String QueryString = "";
	public String competitor_id = "0";
	public String brand_name = "";
	public String comp_id = "0", emp_id = "0";
	public String competitor_brand_id = "0";
	public String competitor_name = "";

	public String competitor_entry_id = "0";
	public String competitor_entry_date = "";
	public String competitor_modified_id = "0";
	public String competitor_modified_date = "";
	public String entry_date = "";
	public String modified_date = "";
	public String entry_by = "";
	public String modified_by = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id", request, response);
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());
				competitor_id = CNumeric(PadQuotes(request.getParameter("competitor_id")));
				brand_id = CNumeric(PadQuotes(request.getParameter("hidden_competitor_brand_id")));

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
					} else {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_role_id", request).equals("1")) {
							competitor_entry_id = CNumeric(GetSession("emp_id", request));
							competitor_entry_date = ToLongDate(kknow());
							AddFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("managecompetitors-list.jsp?competitor_id=" + competitor_id + "&msg=Competitor Added Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Competitor".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Competitor".equals(deleteB)) {
						SOP("Update");
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_role_id", request).equals("1")) {
							competitor_modified_id = CNumeric(GetSession("emp_id", request));
							competitor_modified_date = ToLongDate(kknow());
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("managecompetitors-list.jsp?competitor_id=" + competitor_id + "&msg=Competitor Updated Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if ("Delete Competitor".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_role_id", request).equals("1")) {
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("managecompetitors-list.jsp?msg=Competitor Deleted Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
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
		competitor_brand_id = CNumeric(PadQuotes(request.getParameter("dr_brand_id")));
		brand_id = CNumeric(PadQuotes(request.getParameter("hidden_competitor_brand_id")));
		competitor_name = PadQuotes(request.getParameter("txt_competitor_name"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));

	}
	protected void CheckForm() {
		msg = "";
		try {
			if (competitor_brand_id.equals("0")) {
				msg = msg + "<br>Select Brand!";
			}
			if (competitor_name.equals("")) {
				msg = msg + "<br>Enter Competitor Name!";
			} else {
				StrSql = "SELECT competitor_name "
						+ " FROM " + compdb(comp_id) + "axela_service_competitor"
						+ " WHERE competitor_brand_id =" + competitor_brand_id
						+ " AND competitor_name = '" + competitor_name + "'";
				if (update.equals("yes")) {
					StrSql += " AND competitor_id != " + competitor_id + "";

				}
				if (ExecuteQuery(StrSql).equalsIgnoreCase(competitor_name)) {
					msg = msg + "<br>Similar Competitor Name found!";
				}
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
				competitor_id = ExecuteQuery("SELECT COALESCE(max(competitor_id), 0)+1 as competitor_id FROM " + compdb(comp_id) + "axela_service_competitor");
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_competitor"
						+ " (competitor_id,"
						+ " competitor_brand_id,"
						+ " competitor_name,"
						+ " competitor_entry_id,"
						+ " competitor_entry_date)"
						+ " values"
						+ " (" + competitor_id + ","
						+ " " + competitor_brand_id + ","
						+ " '" + competitor_name + "',"
						+ " " + competitor_entry_id + ","
						+ " '" + ToLongDate(kknow()) + "')";
				// SOP("strsql==========Add Competitor====" + // StrSqlBreaker(StrSql));
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = " SELECT competitor_id, brand_name, brand_id, "
					+ " competitor_brand_id,"
					+ " competitor_name,"
					+ " competitor_entry_id,"
					+ " competitor_entry_date,"
					+ " competitor_modified_id, "
					+ " competitor_modified_date "
					+ " FROM " + compdb(comp_id) + "axela_service_competitor"
					+ " INNER JOIN axela_brand ON brand_id = competitor_brand_id "
					+ " WHERE competitor_id = " + competitor_id;

			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("StrSql---PopulateFields-------" + StrSql);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					competitor_id = crs.getString("competitor_id");
					competitor_brand_id = crs.getString("competitor_brand_id");
					competitor_name = crs.getString("competitor_name");
					brand_name = crs.getString("brand_name");
					competitor_entry_id = crs.getString("competitor_entry_id");
					if (!competitor_entry_id.equals("")) {
						entry_by = Exename(comp_id, Integer.parseInt(competitor_entry_id));
					}
					entry_date = strToLongDate(crs.getString("competitor_entry_date"));
					competitor_modified_id = crs.getString("competitor_modified_id");
					if (!competitor_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(competitor_modified_id));
						modified_date = strToLongDate(crs.getString("competitor_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Competitor!"));
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
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_competitor"
						+ " SET"
						+ " competitor_brand_id = " + competitor_brand_id + ","
						+ " competitor_name = '" + competitor_name + "',"
						+ " competitor_modified_id = " + competitor_modified_id + ","
						+ " competitor_modified_date = '" + competitor_modified_date + "'"
						+ " where competitor_id = " + competitor_id + "";
				// SOP("StrSql----------update-------" + StrSqlBreaker(StrSql));
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		try {
			// association with Vehicle Follow-up
			StrSql = "SELECT vehfollowup_competitor_id"
					+ " FROM " + compdb(comp_id) + "axela_service_followup"
					+ " where vehfollowup_competitor_id = " + competitor_id + "";
			if (!ExecuteQuery(StrSql).equals("")) {
				msg = msg + "<br>Competitor is associated with Vehicle Follow-up!";
			}
			if (msg.equals("")) {
				StrSql = "Delete FROM " + compdb(comp_id) + "axela_service_competitor"
						+ " where competitor_id = " + competitor_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateBrand(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT brand_id, brand_name"
					+ " FROM axela_brand"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE 1=1"
					+ " AND branch_branchtype_id = 3"
					+ " GROUP BY brand_id"
					+ " ORDER BY brand_name";
			// SOP("StrSql--------------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("brand_id"));
				Str.append(StrSelectdrop(crs.getString("brand_id"), competitor_brand_id));
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
}
