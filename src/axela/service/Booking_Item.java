package axela.service;
//@Bhagwan Singh 30 Oct 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Booking_Item extends Connect {

	public String update = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String BranchAccess = "";
	public String branch_id = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String QueryString = "";
	public String booking_id = "0";
	public String booking_branch_id = "0";
	public String link_branch = "";
	public String veh_id = "0";
	public String link_item = "";
	public String veh_reg_no = "";
	public String item_name = "";
	public String booking_time = "";
	public String StrHTML = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_service_booking_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));

				msg = PadQuotes(request.getParameter("msg"));
				update = PadQuotes(request.getParameter("update"));
				booking_id = CNumeric(PadQuotes(request.getParameter("booking_id")));
				QueryString = PadQuotes(request.getQueryString());
				if (update.equals("yes")) {
					status = "Update";
				}
				if (!branch_id.equals("0")) {
					booking_branch_id = branch_id;
				}
				if (!booking_id.equals("0")) {
					PopulateBookingDetails();
					StrHTML = new Booking_Check().ListApptItems(booking_id, comp_id);
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

	public String PopulateLocation(String booking_branch_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT location_id, location_name, location_code"
					+ " FROM " + compdb(comp_id) + "axela_inventory_location"
					+ " WHERE location_branch_id = " + booking_branch_id + ""
					+ " GROUP BY location_id"
					+ " ORDER BY location_name";
			// SOP("StrSql = " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select Location</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("location_id"));
				Str.append(">").append(crs.getString("location_name")).append(" (");
				Str.append(crs.getString("location_code")).append(")");
				Str.append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public void PopulateBookingDetails() {
		try {
			StrSql = "SELECT booking_branch_id, CONCAT(branch_name, ' (', branch_code,')' ) AS branchname,"
					+ " booking_time, veh_id, veh_reg_no, item_id,"
					+ " IF(item_code != '', CONCAT(item_name,' (', item_code,')'), item_name) AS itemname"
					+ " FROM " + compdb(comp_id) + "axela_service_booking"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = booking_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = booking_veh_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_variant_id"
					+ " WHERE booking_id = " + booking_id + "";
			// SOP("StrSql====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				booking_branch_id = crs.getString("booking_branch_id");
				booking_time = strToLongDate(crs.getString("booking_time"));
				link_branch = "<a href=../portal/branch-list.jsp?branch_id=" + crs.getString("booking_branch_id") + ">"
						+ crs.getString("branchname") + "</a>";
				veh_id = "<a href=../service/vehicle-list.jsp?veh_id=" + crs.getString("veh_id") + ">"
						+ crs.getString("veh_id") + "</a>";
				link_item = "<a href=../inventory/inventory-item-list.jsp?item_id=" + crs.getString("item_id") + ">"
						+ crs.getString("itemname") + "</a>";
				veh_reg_no = "<a href=../service/vehicle-list.jsp?veh_id=" + crs.getString("veh_id") + ">"
						+ SplitRegNo(crs.getString("veh_reg_no"), 2) + "</a>";
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
