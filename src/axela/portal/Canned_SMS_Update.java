// Ved Prakash (11, 12 Feb 2013)
package axela.portal;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Canned_SMS_Update extends Connect {

	public String updateB = "";
	public String update = "";
	public String StrSql = "";
	public String add = "";
	public String deleteB = "";
	public String addB = "";
	public static String status = "";
	public String msg = "";
	public static String Msg1 = "";
	public String branch_id = "";
	public String brand_id = "0";
	public String QueryString = "";
	public String cannedsms_id = "0";
	public String brand_name = "";
	public String comp_id = "0", emp_id = "0";

	public String cannedsms_brand_id = "0";
	public String cannedsms_branchtype_id = "0";

	public String cannedsms_name = "";
	public String cannedsms_format = "";
	public String cannedsms_active = "0";
	public String cannedsms_rank = "0";
	public String brandconfig_vehfollowup_notcont = "";

	public String cannedsms_entry_id = "0";
	public String cannedsms_entry_date = "0";
	public String cannedsms_modified_id = "0";
	public String cannedsms_modified_date = "";
	public String entry_date = "";
	public String modified_date = "";
	public String cannedsms_entry_by = "";
	public String cannedsms_modified_by = "";
	public Connection conntx = null;
	public Statement stmttx = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{

				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_role_id", request, response);
				// branch_id = CNumeric(GetSession("emp_branch_id", request));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());
				cannedsms_id = CNumeric(PadQuotes(request.getParameter("cannedsms_id")));
				if (cannedsms_brand_id.equals("0")) {
					cannedsms_brand_id = CNumeric(PadQuotes(request.getParameter("brand_id")));
				}
				if (add.equals("yes")) {
					status = "Add";
					cannedsms_active = "1";
				} else if (update.equals("yes")) {
					status = "Update";
				}
				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
					} else {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_role_id", request).equals("1")) {
							cannedsms_entry_id = CNumeric(GetSession("emp_id", request));
							cannedsms_entry_date = ToLongDate(kknow());
							cannedsms_modified_date = "";
							AddFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("canned-sms-list.jsp?cannedsms_id=" + cannedsms_id + "&msg=SMS Added Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Canned SMS".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Canned SMS".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_role_id", request).equals("1")) {
							cannedsms_modified_id = CNumeric(GetSession("emp_id", request));
							cannedsms_modified_date = ToLongDate(kknow());
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("canned-sms-list.jsp?cannedsms_id=" + cannedsms_id + "&msg=Canned SMS Updated Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if ("Delete Canned SMS".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_role_id", request).equals("1")) {
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("canned-sms-list.jsp?msg=Canned SMS Deleted Successfully!"));
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

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		cannedsms_brand_id = CNumeric(PadQuotes(request.getParameter("cannedsms_brand_id")));
		cannedsms_branchtype_id = CNumeric(PadQuotes(request.getParameter("cannedsms_branchtype_id")));
		cannedsms_format = PadQuotes(request.getParameter("cannedsms_format"));
		cannedsms_name = PadQuotes(request.getParameter("cannedsms_name"));
		cannedsms_active = CheckBoxValue(PadQuotes(request.getParameter("cannedsms_active")));
		cannedsms_modified_by = PadQuotes(request.getParameter("modified_by"));

	}
	protected void CheckForm() {
		msg = "";
		if (cannedsms_brand_id.equals("0") || cannedsms_brand_id == null) {
			msg += "<br> Select Brand !";
		}

		if (cannedsms_branchtype_id.equals("0") || cannedsms_branchtype_id == null) {
			msg += "<br> Select Branch Type !";
		}
		if (cannedsms_name.equals("")) {
			msg += "<br>Name cannot be empty!";
		}

		if (msg.equals("")) {
			StrSql = " SELECT cannedsms_id FROM " + compdb(comp_id) + "axela_canned_sms "
					+ " WHERE 1=1"
					+ " AND cannedsms_name = '" + cannedsms_name + "'"
					+ " AND cannedsms_brand_id = " + cannedsms_brand_id
					+ " AND cannedsms_branchtype_id = " + cannedsms_branchtype_id;
			if (updateB.equals("yes")) {
				StrSql += " AND cannedsms_id != " + cannedsms_id;
			}
			// SOP("strsql==" + StrSql);
			if (!CNumeric(ExecuteQuery(StrSql)).equals("0"))
				msg += "<br> Canned SMS Already Exist!";
		}

	}

	protected void AddFields() throws Exception {
		CheckForm();
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				StrSql = "SELECT COALESCE(MAX(cannedsms_rank),0)+1 AS ID "
						+ " FROM " + compdb(comp_id) + "axela_canned_sms "
						+ " WHERE cannedsms_brand_id = " + cannedsms_brand_id
						+ " AND cannedsms_branchtype_id = " + cannedsms_branchtype_id;

				// SOP("StrSql===" + StrSql);
				cannedsms_rank = CNumeric(ExecuteQuery(StrSql));
				// SOP("cannedsms_rank===" + cannedsms_rank);
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_canned_sms"
						+ " ( cannedsms_brand_id,"
						+ "	cannedsms_branchtype_id,"
						+ "	cannedsms_name,"
						+ " cannedsms_format,"
						+ " cannedsms_active,"
						+ " cannedsms_rank,"
						+ " cannedsms_entry_id,"
						+ " cannedsms_entry_date)"

						+ " VALUES"
						+ " (" + " " + cannedsms_brand_id + ","
						+ " " + cannedsms_branchtype_id + ","
						+ " '" + cannedsms_name + "',"
						+ " '" + cannedsms_format + "',"
						+ " " + cannedsms_active + ","
						+ " " + cannedsms_rank + ","
						+ " " + cannedsms_entry_id + ","
						+ " '" + cannedsms_entry_date + "')";
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
				ResultSet rs = stmttx.getGeneratedKeys();
				while (rs.next()) {
					cannedsms_id = rs.getString(1);
				}
				rs.close();
				conntx.commit();

			} catch (Exception ex) {
				if (conntx.isClosed()) {
					SOPError("connection is closed...");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					SOPError("connection rollback...");
				}
				msg = "<br>Transaction Error!";
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			} finally {
				conntx.setAutoCommit(true);
				stmttx.close();
				if (conntx != null && !conntx.isClosed()) {
					conntx.close();
				}
			}
		}
	}

	protected void UpdateFields() throws Exception {
		CheckForm();
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				StrSql = "UPDATE " + compdb(comp_id) + "axela_canned_sms"
						+ " SET "
						+ " cannedsms_brand_id = " + cannedsms_brand_id + " ,"
						+ "	cannedsms_branchtype_id = " + cannedsms_branchtype_id + " ,"
						+ "	cannedsms_name = '" + cannedsms_name + "' ,"
						+ " cannedsms_format = '" + cannedsms_format + "' ,"
						+ " cannedsms_active = " + cannedsms_active + " ,"
						+ " cannedsms_modified_id = " + cannedsms_modified_id + " ,"
						+ " cannedsms_modified_date = '" + ToLongDate(kknow()) + "'"
						+ " WHERE cannedsms_id = " + cannedsms_id + "";
				// SOP("Str--update===" + StrSql);
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
				ResultSet rs = stmttx.getGeneratedKeys();
				while (rs.next()) {
					cannedsms_id = rs.getString(1);
				}
				rs.close();
				conntx.commit();

			} catch (Exception ex) {
				if (conntx.isClosed()) {
					SOPError("connection is closed...");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					SOPError("connection rollback...");
				}
				msg = "<br>Transaction Error!";
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			} finally {
				conntx.setAutoCommit(true);
				stmttx.close();
				if (conntx != null && !conntx.isClosed()) {
					conntx.close();
				}
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = " SELECT cannedsms_id,"
					+ " cannedsms_brand_id,"
					+ "	cannedsms_branchtype_id,"
					+ "	cannedsms_name,"
					+ " cannedsms_format,"
					+ " cannedsms_active,"
					+ " cannedsms_entry_id,"
					+ " cannedsms_entry_date,"
					+ " cannedsms_modified_id,"
					+ " cannedsms_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_canned_sms"
					+ " INNER JOIN axela_brand ON brand_id = cannedsms_brand_id "
					+ " INNER JOIN axela_branch_type ON branchtype_id = cannedsms_branchtype_id "
					+ " WHERE cannedsms_id = " + cannedsms_id;

			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					cannedsms_id = crs.getString("cannedsms_id");
					cannedsms_brand_id = crs.getString("cannedsms_brand_id");
					cannedsms_branchtype_id = crs.getString("cannedsms_branchtype_id");
					cannedsms_name = crs.getString("cannedsms_name");
					cannedsms_format = crs.getString("cannedsms_format");
					cannedsms_active = crs.getString("cannedsms_active");
					cannedsms_entry_date = crs.getString("cannedsms_entry_date");
					cannedsms_entry_id = crs.getString("cannedsms_entry_id");
					cannedsms_modified_id = crs.getString("cannedsms_modified_id");
					if (!cannedsms_entry_id.equals("0")) {
						cannedsms_entry_by = Exename(comp_id, Integer.parseInt(cannedsms_entry_id));

						cannedsms_entry_date = strToShortDate(crs.getString("cannedsms_entry_date"));
					}

					if (!cannedsms_modified_id.equals("0")) {
						cannedsms_modified_by = Exename(comp_id, Integer.parseInt(cannedsms_modified_id));
						cannedsms_modified_date = strToShortDate(crs.getString("cannedsms_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Canned SMS!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void DeleteFields() {
		try {
			if (msg.equals("")) {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_canned_sms"
						+ " WHERE cannedsms_id = " + cannedsms_id + "";
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
					+ "	INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE branch_active = 1"
					+ " GROUP BY brand_id"
					+ " ORDER BY brand_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("brand_id"));
				Str.append(StrSelectdrop(crs.getString("brand_id"), cannedsms_brand_id));
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

	public String PopulateBranchType(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT branchtype_id, branchtype_name"
					+ " FROM axela_branch_type"
					+ " GROUP BY branchtype_id"
					+ " ORDER BY branchtype_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("branchtype_id"));
				Str.append(StrSelectdrop(crs.getString("branchtype_id"), cannedsms_branchtype_id));
				Str.append(">").append(crs.getString("branchtype_name")).append("</option>\n");
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
