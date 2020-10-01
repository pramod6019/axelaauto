package axela.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class CouponCampaign_Update extends Connect {

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
	public String couponcampaign_id = "0";
	public String brand_name = "";
	public String comp_id = "0", emp_id = "0";
	public String starttime = "", endtime = "";
	public String start_time = "", end_time = "";

	public String couponcampaign_brand_id = "0";
	public String couponcampaign_campaigntype_id = "0";
	public String couponcampaign_department_id = "0";
	public String couponcampaign_name = "";
	public String couponcampaign_desc = "";
	public String couponcampaign_startdate = "";
	public String couponcampaign_enddate = "";
	public String couponcampaign_couponoffer = "";
	public long couponcampaign_budget = 0;
	public String couponcampaign_couponcount = "", prev_couponcount = "0";
	public String couponcampaign_couponvalue = "";
	public String couponcampaign_notes = "";
	public String couponcampaign_active = "1";
	public String couponcampaign_entry_id = "0";
	public String couponcampaign_entry_date = "";
	public String couponcampaign_modified_id = "0";
	public String couponcampaign_modified_date = "";
	public String entry_date = "";
	public String entry_by = "";
	public String modified_by = "";
	public String modified_date = "";
	public String end_date = "";

	public Connection conntx = null;
	public Statement stmttx = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_service_coupon_campaign_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());
				couponcampaign_id = CNumeric(PadQuotes(request.getParameter("couponcampaign_id")));
				// brand_id = CNumeric(PadQuotes(request.getParameter("hidden_competitor_brand_id")));
				if (starttime.equals("")) {
					starttime = strToShortDate(ToShortDate(kknow()));
					start_time = strToShortDate(ToShortDate(kknow()));
				}
				if (endtime.equals("")) {
					endtime = strToShortDate(ToShortDate(kknow()));
					end_time = strToShortDate(ToShortDate(kknow()));
				}

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						couponcampaign_brand_id = "0";
						couponcampaign_campaigntype_id = "0";
						couponcampaign_department_id = "0";
						couponcampaign_name = "";
						couponcampaign_desc = "";
						couponcampaign_startdate = "";
						couponcampaign_enddate = "";
						couponcampaign_couponoffer = "";
						couponcampaign_couponcount = "";
						couponcampaign_couponvalue = "";
						couponcampaign_notes = "";
						couponcampaign_budget = 0;

					} else {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_service_coupon_campaign_add", request).equals("1")) {
							couponcampaign_entry_id = CNumeric(GetSession("emp_id", request));
							couponcampaign_entry_date = ToLongDate(kknow());
							AddFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("couponcampaign-list.jsp?couponcampaign_id=" + couponcampaign_id + "&msg=Coupon Campaign Added Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Coupon Campaign".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Coupon Campaign".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_service_coupon_campaign_edit", request).equals("1")) {
							couponcampaign_modified_id = CNumeric(GetSession("emp_id", request));
							couponcampaign_modified_date = ToLongDate(kknow());
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("couponcampaign-list.jsp?couponcampaign_id=" + couponcampaign_id + "&msg=Coupon Campaign Updated Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if ("Delete Coupon Campaign".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_service_coupon_campaign_delete", request).equals("1")) {
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("couponcampaign-list-list.jsp?msg=Coupon Campaign Deleted Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
			}
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		starttime = PadQuotes(request.getParameter("txt_start_date"));
		endtime = PadQuotes(request.getParameter("txt_end_date"));
		if (starttime.equals("")) {
			starttime = strToShortDate(ToShortDate(kknow()));
		}

		if (endtime.equals("")) {
			endtime = strToShortDate(ToShortDate(kknow()));
		}

		couponcampaign_brand_id = CNumeric(PadQuotes(request.getParameter("dr_brand_id")));
		couponcampaign_campaigntype_id = CNumeric(PadQuotes(request.getParameter("dr_campaign_id")));
		couponcampaign_department_id = CNumeric(PadQuotes(request.getParameter("dr_dept_id")));
		couponcampaign_name = PadQuotes(request.getParameter("txt_campaign_name"));
		couponcampaign_desc = PadQuotes(request.getParameter("txt_campaign_desc"));
		couponcampaign_startdate = PadQuotes(request.getParameter("txt_start_date"));
		couponcampaign_enddate = PadQuotes(request.getParameter("txt_end_date"));
		couponcampaign_couponoffer = PadQuotes(request.getParameter("txt_coupon_offer"));
		couponcampaign_couponcount = CNumeric(PadQuotes(request.getParameter("txt_coupon_count")));
		couponcampaign_couponvalue = CNumeric(PadQuotes(request.getParameter("txt_coupon_value")));
		couponcampaign_budget = Long.parseLong(couponcampaign_couponcount) * Long.parseLong(couponcampaign_couponvalue);

		couponcampaign_notes = PadQuotes(request.getParameter("txt_campaign_notes"));
		couponcampaign_active = PadQuotes(request.getParameter("chk_customer_active"));
		// couponcampaign_active = CheckBoxValue(PadQuotes(request.getParameter("chk_customer_active")));
		if (couponcampaign_active.equals("on")) {
			couponcampaign_active = "1";
		} else {
			couponcampaign_active = "0";
		}
		couponcampaign_entry_id = PadQuotes(request.getParameter("couponcampaign_entry_id"));
		couponcampaign_entry_date = PadQuotes(request.getParameter("couponcampaign_entry_date"));
		couponcampaign_modified_id = PadQuotes(request.getParameter("couponcampaign_modified_id"));
		couponcampaign_modified_date = PadQuotes(request.getParameter("mybean.couponcampaign_modified_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));

	}

	protected void CheckForm() {
		msg = "";
		try {
			if (couponcampaign_brand_id.equals("0")) {
				msg += "<br>Select Brand!";
			}
			if (couponcampaign_campaigntype_id.equals("0")) {
				msg += "<br>Select Campaign Type!";
			}
			if (couponcampaign_department_id.equals("0")) {
				msg += "<br>Select Department!";
			}
			if (couponcampaign_name.equals("")) {
				msg += "<br>Enter Campaign Name!";
			}
			if (starttime.equals("")) {
				msg = msg + "<br>Select Start Date!";
			}

			if (!starttime.equals("")) {
				if (isValidDateFormatShort(starttime)) {
					starttime = ConvertShortDateToStr(starttime);
				} else {
					msg = msg + "<br>Enter Valid Start Date!";
					starttime = "";
				}
			}
			if (endtime.equals("")) {
				msg = msg + "<br>Select End Date!<br>";
			}
			if (!endtime.equals("")) {
				if (isValidDateFormatShort(endtime)) {
					endtime = ConvertShortDateToStr(endtime);
					if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
						msg = msg + "<br>Start Date should be less than End date!";
					}

				} else {
					msg = msg + "<br>Enter Valid End Date!";
					endtime = "";
				}
			}
			if (couponcampaign_couponcount.equals("0")) {
				msg += "<br>Enter Coupon Count!";
			}
			if (couponcampaign_couponvalue.equals("0")) {
				msg += "<br>Enter Coupon Value!";
			}
			// validate date
			// else {
			// StrSql = "SELECT couponcampaign_name "
			// + " FROM " + compdb(comp_id) + "axela_service_coupon_campaign"
			// + " WHERE couponcampaign_name = '" + couponcampaign_name + "'";
			// if (update.equals("yes")) {
			// StrSql += " AND couponcampaign_id != " + couponcampaign_id + "";
			// }
			// if (ExecuteQuery(StrSql).equalsIgnoreCase(couponcampaign_name)) {
			// msg += "<br>Similar Coupon Campaign Name found!";
			// }
			// }

			StrSql = " SELECT COUNT(coupon_id)"
					+ " FROM " + compdb(comp_id) + "axela_service_coupon"
					+ " WHERE coupon_couponcampaign_id = " + couponcampaign_id;

			prev_couponcount = CNumeric(ExecuteQuery(StrSql));

			if (Integer.parseInt(prev_couponcount) > Integer.parseInt(couponcampaign_couponcount)) {
				msg += "<br>New Coupon Count Can't be Less than Previous Count !";
			}

		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {

			StrSql = " SELECT couponcampaign_brand_id, couponcampaign_campaigntype_id,"
					+ " couponcampaign_department_id, couponcampaign_name, couponcampaign_desc,"
					+ " couponcampaign_startdate, couponcampaign_enddate, couponcampaign_couponoffer,"
					+ " couponcampaign_couponcount, couponcampaign_couponvalue, "
					+ " couponcampaign_budget, couponcampaign_couponissuecount,"
					+ " couponcampaign_active, couponcampaign_notes,"
					+ " couponcampaign_entry_id, couponcampaign_entry_date,"
					+ " couponcampaign_modified_id, couponcampaign_modified_date "
					+ " FROM " + compdb(comp_id) + "axela_service_coupon_campaign"
					+ " WHERE couponcampaign_id = " + couponcampaign_id;

			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("StrSql---PopulateFields-------" + StrSql);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					couponcampaign_brand_id = crs.getString("couponcampaign_brand_id");
					couponcampaign_campaigntype_id = crs.getString("couponcampaign_campaigntype_id");
					couponcampaign_department_id = crs.getString("couponcampaign_department_id");
					couponcampaign_name = crs.getString("couponcampaign_name");
					couponcampaign_startdate = strToShortDate(crs.getString("couponcampaign_startdate"));
					couponcampaign_enddate = strToShortDate(crs.getString("couponcampaign_enddate"));
					couponcampaign_couponoffer = crs.getString("couponcampaign_couponoffer");
					couponcampaign_budget = crs.getLong("couponcampaign_budget");
					couponcampaign_couponcount = crs.getString("couponcampaign_couponcount");
					couponcampaign_couponvalue = crs.getString("couponcampaign_couponvalue");
					couponcampaign_notes = crs.getString("couponcampaign_notes");
					couponcampaign_active = crs.getString("couponcampaign_active");
					couponcampaign_entry_id = crs.getString("couponcampaign_entry_id");
					if (!couponcampaign_entry_id.equals("")) {
						entry_by = Exename(comp_id, Integer.parseInt(couponcampaign_entry_id));
						entry_date = strToLongDate(crs.getString("couponcampaign_entry_date"));
					}
					couponcampaign_modified_id = crs.getString("couponcampaign_modified_id");
					if (!couponcampaign_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(couponcampaign_modified_id));
						modified_date = strToLongDate(crs.getString("couponcampaign_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Coupon Campaign!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void AddFields() throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			try {

				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_coupon_campaign"
						+ " ( "
						+ " couponcampaign_brand_id,"
						+ " couponcampaign_campaigntype_id,"
						+ " couponcampaign_department_id,"
						+ " couponcampaign_name,"
						+ " couponcampaign_desc,"
						+ " couponcampaign_startdate,"
						+ " couponcampaign_enddate,"
						+ " couponcampaign_couponoffer,"
						+ " couponcampaign_couponcount,"
						+ " couponcampaign_couponvalue,"
						+ " couponcampaign_budget,"
						+ " couponcampaign_notes,"
						+ " couponcampaign_active,"
						+ " couponcampaign_entry_id,"
						+ " couponcampaign_entry_date"
						+ " )"
						+ " VALUES ("
						+ " " + couponcampaign_brand_id + ","
						+ " " + couponcampaign_campaigntype_id + ","
						+ " " + couponcampaign_department_id + ","
						+ " '" + couponcampaign_name + "',"
						+ " '" + couponcampaign_desc + "',"
						+ " '" + ConvertShortDateToStr(couponcampaign_startdate) + "',"
						+ " '" + ConvertShortDateToStr(couponcampaign_enddate) + "',"
						+ " '" + couponcampaign_couponoffer + "',"
						+ " " + couponcampaign_couponcount + ","
						+ " " + couponcampaign_couponvalue + ","
						+ " " + couponcampaign_budget + ","
						+ " '" + couponcampaign_notes + "',"
						+ " '" + couponcampaign_active + "',"
						+ " " + couponcampaign_entry_id + ","
						+ " '" + couponcampaign_entry_date + "'"
						+ ")";
				// SOP("AddFields====" + StrSql);
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
				ResultSet rs = stmttx.getGeneratedKeys();
				while (rs.next()) {
					couponcampaign_id = rs.getString(1);
				}
				rs.close();
				for (int i = 0; i < Integer.parseInt(couponcampaign_couponcount); i++) {

					if (!couponcampaign_id.equals("0")) {
						StrSql = " INSERT INTO " + compdb(comp_id) + "axela_service_coupon "
								+ " (coupon_couponcampaign_id) "
								+ " VALUES "
								+ " ( " + couponcampaign_id + " ) ";
						stmttx.execute(StrSql);
					}
				}

				conntx.commit();
			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("conn is closed.....");
				}
				if (!conntx.isClosed() && conntx != null) {
					SOP("Transaction Error==");
					conntx.rollback();
					SOPError(this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
				}
				msg = "<br>Transaction Error!";
			} finally {
				conntx.setAutoCommit(true);
				if (stmttx != null && !stmttx.isClosed()) {
					stmttx.close();
				}
				if (conntx != null && !conntx.isClosed()) {
					conntx.close();
				}
			}
		}
	}

	protected void UpdateFields() throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			try {

				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_coupon_campaign"
						+ " SET"
						+ " couponcampaign_brand_id = " + couponcampaign_brand_id + ","
						+ " couponcampaign_campaigntype_id = " + couponcampaign_campaigntype_id + ","
						+ " couponcampaign_department_id = " + couponcampaign_department_id + ","
						+ " couponcampaign_name = '" + couponcampaign_name + "',"
						+ " couponcampaign_desc = '" + couponcampaign_desc + "',"
						+ " couponcampaign_startdate = '" + ConvertShortDateToStr(couponcampaign_startdate) + "',"
						+ " couponcampaign_enddate = '" + ConvertShortDateToStr(couponcampaign_enddate) + "',"
						+ " couponcampaign_couponoffer = '" + couponcampaign_couponoffer + "',"
						+ " couponcampaign_budget = '" + couponcampaign_budget + "',"
						+ " couponcampaign_couponcount = " + couponcampaign_couponcount + ","
						+ " couponcampaign_couponvalue = " + couponcampaign_couponvalue + ","
						+ " couponcampaign_notes = '" + couponcampaign_notes + "',"
						+ " couponcampaign_active = " + couponcampaign_active + ","
						+ " couponcampaign_modified_id = " + couponcampaign_modified_id + ","
						+ " couponcampaign_modified_date = '" + couponcampaign_modified_date + "'"
						+ " WHERE couponcampaign_id = " + couponcampaign_id + "";
				// SOP("StrSql----------update-------" + StrSql);
				stmttx.execute(StrSql);

				for (int i = 0; i < (Integer.parseInt(couponcampaign_couponcount) - Integer.parseInt(prev_couponcount)); i++) {
					if (!couponcampaign_id.equals("0")) {
						StrSql = " INSERT INTO " + compdb(comp_id) + "axela_service_coupon "
								+ " (coupon_couponcampaign_id) VALUES ( " + couponcampaign_id + " ) ";
						stmttx.execute(StrSql);
					}
				}

				conntx.commit();
			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("conn is closed.....");
				}
				if (!conntx.isClosed() && conntx != null) {
					SOP("Transaction Error==");
					conntx.rollback();
					SOPError(this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
				}
				msg = "<br>Transaction Error!";
			} finally {
				conntx.setAutoCommit(true);
				if (stmttx != null && !stmttx.isClosed()) {
					stmttx.close();
				}
				if (conntx != null && !conntx.isClosed()) {
					conntx.close();
				}
			}
		}
	}

	protected void DeleteFields() {
		try {
			// association with Vehicle Follow-up
			StrSql = "SELECT COUNT(coupon_id)"
					+ " FROM " + compdb(comp_id) + "axela_service_coupon"
					+ " WHERE coupon_couponcampaign_id = " + couponcampaign_id
					+ " AND coupon_customer_id != 0";
			if (!CNumeric(ExecuteQuery(StrSql)).equals("0")) {
				msg += "<br>Campaign Coupon is associated with Customer!";
			}
			if (msg.equals("")) {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_coupon_campaign"
						+ " WHERE couponcampaign_id = " + couponcampaign_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateBrand(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT brand_id, brand_name"
					+ " FROM axela_brand"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE 1 = 1"
					+ " AND branch_branchtype_id = 3"
					+ " GROUP BY brand_id"
					+ " ORDER BY brand_name";
			// SOP("StrSql--------------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("brand_id"));
				Str.append(StrSelectdrop(crs.getString("brand_id"), couponcampaign_brand_id));
				Str.append(">").append(crs.getString("brand_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateDepartment(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT department_id, department_name"
					+ " FROM  " + compdb(comp_id) + "axela_emp_department"
					+ " WHERE 1 = 1"
					+ " GROUP BY department_id"
					+ " ORDER BY department_name";
			// SOP("StrSql--------------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("department_id"));
				Str.append(StrSelectdrop(crs.getString("department_id"), couponcampaign_department_id));
				Str.append(">").append(crs.getString("department_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateType(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT couponcampaintype_id, couponcampaintype_name"
					+ " FROM " + compdb(comp_id) + "axela_service_couponcampain_type"
					+ " WHERE 1 = 1"
					+ " GROUP BY couponcampaintype_id"
					+ " ORDER BY couponcampaintype_name";
			// SOP("StrSql--------------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("couponcampaintype_id"));
				Str.append(StrSelectdrop(crs.getString("couponcampaintype_id"), couponcampaign_campaigntype_id));
				Str.append(">").append(crs.getString("couponcampaintype_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

}
