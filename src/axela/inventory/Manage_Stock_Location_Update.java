package axela.inventory;

import java.io.IOException;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Manage_Stock_Location_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public static String status = "";
	public String StrSql = "";
	public static String msg = "";
	public String vehstocklocation_id = "";
	public String vehstocklocation_branch_id = "";
	public String vehstocklocation_name = "";
	public String BranchAccess;
	public String emp_role_id = "";
	public String emp_id = "";
	public String comp_id = "0", branch_id = "0";
	public String QueryString = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_stock_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				add = PadQuotes(request.getParameter("Add"));
				update = PadQuotes(request.getParameter("Update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				vehstocklocation_id = CNumeric(PadQuotes(request.getParameter("vehstocklocation_id")));
				QueryString = PadQuotes(request.getQueryString());
				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						vehstocklocation_branch_id = "";
						vehstocklocation_name = "";
					} else {
						CheckPerm(comp_id, "emp_item_add", request, response);
						GetValues(request, response);
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manage-stock-location.jsp?vehstocklocation_id=" + vehstocklocation_id + "&msg=Stock Location Added Successfully!"));
						}
					}
				}

				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Location".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Location".equals(deleteB)) {
						CheckPerm(comp_id, "emp_item_edit", request, response);
						GetValues(request, response);
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manage-stock-location.jsp?vehstocklocation_id=" + vehstocklocation_id + "&msg=Stock Location Updated Successfully!"));
						}
					} else if ("Delete Location".equals(deleteB)) {
						CheckPerm(comp_id, "emp_item_delete", request, response);
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manage-stock-location.jsp?msg=Stock Location Deleted Successfully!"));
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
		vehstocklocation_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
		vehstocklocation_name = PadQuotes(request.getParameter("txt_vehstocklocation_name"));
	}

	protected void CheckForm() {
		msg = "";
		try {
			// if (vehstocklocation_branch_id.equals("0")) {
			// msg = msg + "<br>Select Branch!";
			// }
			if (vehstocklocation_name.equals("")) {
				msg = msg + "<br>Enter Name!";
			}
			if (!vehstocklocation_name.equals("")) {
				StrSql = "SELECT vehstocklocation_name FROM " + compdb(comp_id) + "axela_vehstock_location"
						+ " WHERE vehstocklocation_name = '" + vehstocklocation_name + "'"
						// + " and vehstocklocation_branch_id = " + vehstocklocation_branch_id +
						// ""
						+ " AND vehstocklocation_id != " + vehstocklocation_id + "";
				ResultSet rsname = processQuery(StrSql, 0);
				if (rsname.isBeforeFirst()) {
					msg = msg + "<br>Similar Location Name Found!";
				}
				rsname.close();
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
				vehstocklocation_id = ExecuteQuery("SELECT (COALESCE(MAX(vehstocklocation_id),0)+1) FROM " + compdb(comp_id) + "axela_vehstock_location");
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_location"
						+ " (vehstocklocation_id,"
						+ " vehstocklocation_branch_id,"
						+ " vehstocklocation_name)"
						+ " VALUES"
						+ " (" + vehstocklocation_id + ","
						+ " " + vehstocklocation_branch_id + ","
						+ " '" + vehstocklocation_name + "')";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT * FROM " + compdb(comp_id) + "axela_vehstock_location"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = vehstocklocation_branch_id"
					+ " WHERE vehstocklocation_id = " + vehstocklocation_id;
			// + BranchAccess;
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					vehstocklocation_branch_id = crs.getString("vehstocklocation_branch_id");
					vehstocklocation_name = crs.getString("vehstocklocation_name");
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Location!"));
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
				StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock_location"
						+ " SET"
						+ " vehstocklocation_branch_id = " + vehstocklocation_branch_id + ","
						+ " vehstocklocation_name = '" + vehstocklocation_name + "'"
						+ " WHERE vehstocklocation_id = " + vehstocklocation_id + "";
				// SOP("upda===========" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		// Association with Stock
		StrSql = "SELECT vehstock_vehstocklocation_id FROM " + compdb(comp_id) + "axela_vehstock"
				+ " WHERE vehstock_vehstocklocation_id = " + vehstocklocation_id + "";
		// SOP("StrSql===" + StrSql);
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Location is Associated with Stock!";
		}

		StrSql = "SELECT vehstockgatepass_id FROM " + compdb(comp_id) + "axela_vehstock_gatepass"
				+ " WHERE vehstockgatepass_from_location_id = " + vehstocklocation_branch_id
				+ " OR vehstockgatepass_to_location_id = " + vehstocklocation_branch_id;
		// SOP("StrSql-------dele---" + StrSql);
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Location is Associated with Gate Pass!";
		}
		// Delete records
		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_vehstock_location"
						+ " WHERE vehstocklocation_id = " + vehstocklocation_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
	public String PopulateBranch(String comp_id) {
		StringBuilder Str = new StringBuilder();

		try {
			StrSql = "SELECT branch_id, branch_name "
					+ " FROM " + compdb(comp_id) + "axela_branch "
					+ " WHERE branch_active = 1"
					+ " GROUP BY branch_name"
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";

			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select Branch</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("branch_id")).append("");
				Str.append(StrSelectdrop(crs.getString("branch_id"), vehstocklocation_branch_id));
				Str.append(">").append(crs.getString("branch_name")).append("</option>\n");
			}
			// SOP("str============" + Str);
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
