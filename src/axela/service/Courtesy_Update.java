// Ved Prakash (5th or 6th Apr 2013)
package axela.service;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Courtesy_Update extends Connect {

	public String add = "";
	public String update = "";
	public String addB = "";
	public String updateB = "";
	public String deleteB = "";
	public String status = "";
	public String msg = "";
	public String StrSql = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_role_id = "0";
	public String branch_id = "0";
	public String BranchAccess = "";
	public String QueryString = "";
	public String customer_id = "";
	public String courtesycar_id = "0";
	public String contact_id = "0";
	public String contact_name = "";
	public String courtesycar_courtesyveh_id = "0";
	public String courtesycar_customer_id = "0";
	public String courtesycar_contact_id = "0";
	public String courtesycar_branch_id = "0";
	public String courtesyendtime_time = "";
	public String courtesycar_time_from = "";
	public String courtesycar_time_to = "";
	public String courtesyveh_service_start_date = "";
	public String courtesyveh_service_end_date = "";
	public String courtesycar_start_time = "";
	public String courtesycar_end_time = "";
	public String courtesycar_contact_name = "";
	public String courtesycar_customer_name = "";
	public String ccstartdate = "";
	public String ccenddate = "";
	public String courtesycar_active = "0";
	public String courtesycar_notes = "";
	public String mobile1 = "";
	public String mobile2 = "";
	public String courtesycar_landmark = "";
	public String courtesycar_address = "";
	public String courtesycar_entry_id = "";
	public String courtesycar_entry_date = "";
	public String courtesycar_modified_id = "";
	public String courtesycar_modified_date = "";
	public String customer_display = "none";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";
	public String link_customer_name = "";
	public String link_contact_name = "";
	public String strHTML = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = PadQuotes(CheckNull(GetSession("BranchAccess", request)));
				CheckPerm(comp_id, "emp_service_courtesy_access", request, response);
				QueryString = PadQuotes(request.getQueryString());
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				courtesycar_id = CNumeric(PadQuotes(request.getParameter("courtesycar_id")));
				courtesycar_branch_id = CNumeric(PadQuotes(request.getParameter("courtesyveh_branch_id")));

				if (!branch_id.equals("0")) {
					courtesycar_branch_id = branch_id;
				}

				if (add.equals("yes")) {
					status = "Add";
					if (!addB.equals("yes")) {
						courtesycar_active = "1";
					} else if (addB.equals("yes")) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_service_courtesy_add", request).equals("1")) {
							courtesycar_entry_id = emp_id;
							courtesycar_entry_date = ToLongDate(kknow());
							AddFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("courtesy-list.jsp?courtesycar_id=" + courtesycar_id + "&msg=Courtesy Car Added Successfully"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				} else if (update.equals("yes")) {
					status = "Update";
					if (!updateB.equals("yes") && !deleteB.equals("Delete Courtesy Car")) {
						PopulateFields(response);
					} else if (updateB.equals("yes") && !deleteB.equals("Delete Courtesy Car")) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_service_courtesy_edit", request).equals("1")) {
							courtesycar_modified_id = emp_id;
							courtesycar_modified_date = ToLongDate(kknow());
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("courtesy-list.jsp?courtesycar_id=" + courtesycar_id + "&msg=Courtesy Car updated successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if (deleteB.equals("Delete Courtesy Car")) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_service_courtesy_delete", request).equals("1")) {
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("courtesy-list.jsp?msg=Courtesy Car deleted successfully!"));
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
		customer_id = CNumeric(PadQuotes(request.getParameter("customer_id")));
		contact_name = PadQuotes(request.getParameter("txt_contact_name"));
		mobile1 = PadQuotes(request.getParameter("txt_courtesycar_mobile1"));
		mobile2 = PadQuotes(request.getParameter("txt_courtesycar_mobile2"));
		courtesycar_landmark = PadQuotes(request.getParameter("txt_courtesycar_landmark"));
		courtesycar_address = PadQuotes(request.getParameter("txt_courtesycar_address"));
		courtesycar_id = CNumeric(PadQuotes(request.getParameter("courtesycar_id")));
		courtesycar_contact_id = CNumeric(PadQuotes(request.getParameter("courtesycar_contact_id")));
		contact_id = CNumeric(PadQuotes(request.getParameter("span_contact_id")));

		if (!contact_id.equals("0") && !courtesycar_contact_id.equals(contact_id)) {
			courtesycar_contact_id = contact_id;
		}

		if (!courtesycar_contact_id.equals("0")) {
			PopulateContactDetails();
		}
		courtesycar_courtesyveh_id = CNumeric(PadQuotes(request.getParameter("dr_vehicle")));
		courtesycar_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		courtesycar_time_from = PadQuotes(request.getParameter("txt_courtesycar_startdate"));
		ccstartdate = ConvertLongDateToStr(courtesycar_time_from);
		courtesycar_time_to = PadQuotes(request.getParameter("txt_courtesycar_enddate"));
		ccenddate = ConvertLongDateToStr(courtesycar_time_to);
		courtesycar_active = CheckBoxValue(PadQuotes(request.getParameter("chk_courtesycar_active")));
		courtesycar_notes = PadQuotes(request.getParameter("txt_courtesycar_notes"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		try {
			if (courtesycar_branch_id.equals("0")) {
				msg += "<br>Select Branch!";
			}

			if (courtesycar_contact_id.equals("0")) {
				msg += "<br>Select Contact!";
			}

			if (courtesycar_courtesyveh_id.equals("0")) {
				msg += "<br>Select Vehicle!";
			}

			if (courtesycar_time_from.equals("")) {
				msg += "<br>Select Start Date!";
			} else if (isValidDateFormatLong(courtesycar_time_from)) {
				ccstartdate = ConvertLongDateToStr(courtesycar_time_from);
			} else {
				msg += "<br>Enter valid Start Date!";
			}

			if (!courtesycar_time_from.equals("") && !courtesycar_time_to.equals("")) {
				if (isValidDateFormatLong(courtesycar_time_to) && isValidDateFormatLong(courtesycar_time_to)) {
					if (Long.parseLong(ConvertLongDateToStr(courtesycar_time_to)) < Long.parseLong(ConvertLongDateToStr(courtesycar_time_from))) {
						msg += "<br>Courtesy End Date Should be greater than Start Date!";
					}
				}
			}

			if (courtesycar_time_to.equals("")) {
				msg += "<br>Select End Date!";
			} else if (!courtesycar_time_to.equals("")) {
				if (isValidDateFormatLong(courtesycar_time_to)) {
					ccenddate = ConvertLongDateToStr(courtesycar_time_to);
				} else {
					msg += "<br>Enter valid End Date!";
				}
			}

			// cchecking for occupied Vehicle....
			StrSql = "SELECT courtesycar_id FROM " + compdb(comp_id) + "axela_service_courtesy_car"
					+ " WHERE courtesycar_active = 1"
					+ " AND courtesycar_courtesyveh_id = " + courtesycar_courtesyveh_id + ""
					+ " AND ((courtesycar_time_from >= " + ConvertLongDateToStr(courtesycar_time_from) + ""
					+ " AND courtesycar_time_from < " + ConvertLongDateToStr(courtesycar_time_to) + ")"
					+ " OR (courtesycar_time_to > " + ConvertLongDateToStr(courtesycar_time_from) + ""
					+ " AND courtesycar_time_to <= " + ConvertLongDateToStr(courtesycar_time_to) + ") "
					+ " OR (courtesycar_time_from >= " + ConvertLongDateToStr(courtesycar_time_from) + ""
					+ " AND courtesycar_time_to <= " + ConvertLongDateToStr(courtesycar_time_to) + ") "
					+ " OR (courtesycar_time_from <= " + ConvertLongDateToStr(courtesycar_time_from) + ""
					+ " AND courtesycar_time_to >= " + ConvertLongDateToStr(courtesycar_time_to) + "))";
			if (update.equals("yes")) {
				StrSql += " AND courtesycar_id != " + courtesycar_id + "";
			}

			if (!ExecuteQuery(StrSql).equals("")) {
				msg += "<br>Vehicle is occupied by other Person!";
			}
			// ...............
			if (add.equals("yes")) {
				StrSql = "SELECT courtesyveh_service_start_date, courtesyveh_service_end_date"
						+ " FROM " + compdb(comp_id) + "axela_service_courtesy_vehicle"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = courtesyveh_branch_id"
						+ " WHERE courtesyveh_id = " + courtesycar_courtesyveh_id + BranchAccess + "";
				CachedRowSet crs = processQuery(StrSql, 0);

				while (crs.next()) {
					courtesyveh_service_start_date = crs.getString("courtesyveh_service_start_date");
					courtesyveh_service_end_date = crs.getString("courtesyveh_service_end_date");
				}
				crs.close();
				if (!courtesycar_time_from.equals("") && !courtesyveh_service_start_date.equals("") && !courtesyveh_service_end_date.equals("")) {
					if (isValidDateFormatLong(courtesycar_time_from)) {
						if (Long.parseLong(ConvertLongDateToStr(courtesycar_time_from)) >= Long.parseLong(courtesyveh_service_start_date)
								&& Long.parseLong(ConvertLongDateToStr(courtesycar_time_from)) <= Long.parseLong(courtesyveh_service_end_date)) {
							msg += "<br>Courtesy Vehicle is not free between " + strToShortDate(courtesyveh_service_start_date) + " and " + strToShortDate(courtesyveh_service_end_date)
									+ ", choose another Start Date!";
						}
					}
				}

				if (!courtesycar_time_to.equals("") && !courtesyveh_service_start_date.equals("") && !courtesyveh_service_end_date.equals("")) {
					if (isValidDateFormatLong(courtesycar_time_to)) {
						if (Long.parseLong(ConvertLongDateToStr(courtesycar_time_to)) >= Long.parseLong(courtesyveh_service_start_date)
								&& Long.parseLong(ConvertLongDateToStr(courtesycar_time_to)) <= Long.parseLong(courtesyveh_service_end_date)) {
							msg += "<br>Courtesy Vehicle is not free between " + strToShortDate(courtesyveh_service_start_date) + " and " + strToShortDate(courtesyveh_service_end_date)
									+ ", choose another End Date!";
						}
					}
				}
			}

			if (mobile1.equals("")) {
				msg += "<br>Enter Mobile1 No.!";
			} else if (!mobile1.equals("")) {
				if (!IsValidMobileNo11(mobile1)) {
					msg += "<br>Enter Valid Mobile1 No.!";
				}
			}

			if (!mobile2.equals("")) {
				if (!IsValidMobileNo11(mobile2)) {
					msg += "<br>Enter valid Mobile2 No.!";
				}
			}

			if (courtesycar_landmark.equals("")) {
				msg += "<br>Enter Landmark!";
			}
			if (courtesycar_address.equals("")) {
				msg += "<br>Enter Address!";
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void AddFields() throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_courtesy_car"
					+ " (courtesycar_customer_id,"
					+ " courtesycar_contact_id,"
					+ " courtesycar_branch_id,"
					+ " courtesycar_courtesyveh_id,"
					+ " courtesycar_time_from,"
					+ " courtesycar_time_to,"
					+ " courtesycar_contact_name,"
					+ " courtesycar_landmark,"
					+ " courtesycar_active,"
					+ " courtesycar_notes,"
					+ " courtesycar_mobile1,"
					+ " courtesycar_mobile2,"
					+ " courtesycar_add,"
					+ " courtesycar_entry_id,"
					+ " courtesycar_entry_date)"
					+ " VALUES"
					+ " (" + customer_id + ","
					+ " " + courtesycar_contact_id + ","
					+ " " + courtesycar_branch_id + ","
					+ " " + courtesycar_courtesyveh_id + ","
					+ " '" + ccstartdate + "',"
					+ " '" + ccenddate + "',"
					+ " '" + contact_name + "',"
					+ " '" + courtesycar_landmark + "',"
					+ " '" + courtesycar_active + "',"
					+ " '" + courtesycar_notes + "',"
					+ " '" + mobile1 + "',"
					+ " '" + mobile2 + "',"
					+ " '" + courtesycar_address + "',"
					+ " " + courtesycar_entry_id + ","
					+ " '" + courtesycar_entry_date + "')";
			courtesycar_id = UpdateQueryReturnID(StrSql);
		}
	}

	protected void UpdateFields() {
		CheckForm();
		if (msg.equals("")) {
			StrSql = "UPDATE " + compdb(comp_id) + "axela_service_courtesy_car"
					+ " SET"
					+ " courtesycar_customer_id = " + customer_id + ","
					+ " courtesycar_contact_id = " + courtesycar_contact_id + ","
					+ " courtesycar_branch_id = " + courtesycar_branch_id + ","
					+ " courtesycar_courtesyveh_id = " + courtesycar_courtesyveh_id + ","
					+ " courtesycar_time_from = '" + ccstartdate + "',"
					+ " courtesycar_time_to = '" + ccenddate + "',"
					+ " courtesycar_contact_name = '" + contact_name + "',"
					+ " courtesycar_active = '" + courtesycar_active + "',"
					+ " courtesycar_notes = '" + courtesycar_notes + "',"
					+ " courtesycar_landmark = '" + courtesycar_landmark + "',"
					+ " courtesycar_mobile1 = '" + mobile1 + "',"
					+ " courtesycar_mobile2 = '" + mobile2 + "',"
					+ " courtesycar_add = '" + courtesycar_address + "',"
					+ " courtesycar_modified_id = " + courtesycar_modified_id + ","
					+ " courtesycar_modified_date = '" + courtesycar_modified_date + "'"
					+ " WHERE courtesycar_id = " + courtesycar_id + "";
			updateQuery(StrSql);
		}
	}

	protected void DeleteFields() {
		if (msg.equals("")) {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_courtesy_car"
					+ " WHERE courtesycar_id = " + courtesycar_id + "";
			updateQuery(StrSql);
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		customer_display = "";
		try {
			StrSql = "SELECT courtesycar_customer_id, courtesycar_contact_id, courtesycar_courtesyveh_id,"
					+ " courtesycar_time_from, courtesycar_time_to, courtesycar_contact_name, courtesycar_branch_id,"
					+ " courtesycar_landmark, courtesycar_active, courtesycar_notes, courtesycar_mobile1,"
					+ " courtesycar_mobile2, courtesycar_add, courtesycar_entry_id,courtesycar_entry_date,"
					+ " courtesycar_modified_id, courtesycar_modified_date, courtesyveh_branch_id, customer_name"
					+ " FROM " + compdb(comp_id) + "axela_service_courtesy_car"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_courtesy_vehicle ON courtesyveh_id = courtesycar_courtesyveh_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = courtesyveh_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = courtesycar_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = courtesycar_contact_id"
					+ " WHERE courtesycar_id = " + courtesycar_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					courtesycar_contact_id = crs.getString("courtesycar_contact_id");
					contact_name = crs.getString("courtesycar_contact_name");
					customer_id = crs.getString("courtesycar_customer_id");
					link_contact_name = "<a href=\"../customer/customer-contact-list.jsp?contact_id=" + crs.getString("courtesycar_contact_id") + "\">"
							+ crs.getString("courtesycar_contact_name") + "</a>";
					link_customer_name = "<a href=\"../customer/customer-list.jsp?customer_id=" + crs.getString("courtesycar_customer_id") + "\">"
							+ crs.getString("customer_name") + "</a>";
					courtesycar_courtesyveh_id = crs.getString("courtesycar_courtesyveh_id");
					courtesycar_branch_id = crs.getString("courtesycar_branch_id");
					courtesycar_time_from = strToLongDate(crs.getString("courtesycar_time_from"));
					courtesycar_time_to = strToLongDate(crs.getString("courtesycar_time_to"));
					mobile1 = crs.getString("courtesycar_mobile1");
					mobile2 = crs.getString("courtesycar_mobile2");
					courtesycar_active = crs.getString("courtesycar_active");
					courtesycar_notes = crs.getString("courtesycar_notes");
					courtesycar_landmark = crs.getString("courtesycar_landmark");
					courtesycar_address = crs.getString("courtesycar_add");
					courtesycar_entry_id = crs.getString("courtesycar_entry_id");
					entry_by = Exename(comp_id, crs.getInt("courtesycar_entry_id"));
					entry_date = strToLongDate(crs.getString("courtesycar_entry_date"));
					courtesycar_modified_id = crs.getString("courtesycar_modified_id");

					if (!courtesycar_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(courtesycar_modified_id));
						modified_date = strToLongDate(crs.getString("courtesycar_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Courtesy Car"));
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateVehicle() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT courtesyveh_id, courtesyveh_name, courtesyveh_regno"
					+ " FROM " + compdb(comp_id) + "axela_service_courtesy_vehicle"
					+ " WHERE courtesyveh_active = 1"
					+ " AND courtesyveh_branch_id = " + courtesycar_branch_id + ""
					+ " ORDER BY courtesyveh_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\"> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("courtesyveh_id"));
				Str.append(StrSelectdrop(crs.getString("courtesyveh_id"), courtesycar_courtesyveh_id));
				Str.append(">").append(crs.getString("courtesyveh_name")).append(" - ");
				Str.append(SplitRegNo(crs.getString("courtesyveh_regno"), 2)).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateBranch() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT branch_id, branch_name"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_courtesy_vehicle ON courtesyveh_branch_id = branch_id";
			if (add.equals("yes")) {
				StrSql += " WHERE branch_active = 1"
						+ " AND courtesyveh_active = 1";
			}

			StrSql += " GROUP BY branch_name"
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select Branch</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("branch_id"));
				Str.append(Selectdrop(crs.getInt("branch_id"), courtesycar_branch_id));
				Str.append(">").append(crs.getString("branch_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	protected void PopulateContactDetails() {
		try {
			if (!contact_id.equals("0") || !courtesycar_contact_id.equals("0")) {
				StrSql = "SELECT contact_id, contact_fname, contact_lname, contact_mobile1,"
						+ " contact_mobile1, title_desc, customer_id, customer_name"
						+ " FROM " + compdb(comp_id) + "axela_customer_contact"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id = contact_customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
						+ " WHERE 1 = 1";
				if (!contact_id.equals("0")) {
					StrSql += " AND contact_id = " + contact_id + "";
				} else if (!courtesycar_contact_id.equals("0")) {
					StrSql += " AND contact_id = " + courtesycar_contact_id + "";
				}
				CachedRowSet crs = processQuery(StrSql, 0);

				while (crs.next()) {
					customer_display = "";
					link_contact_name = "<a href=\"../customer/customer-contact-list.jsp?contact_id=" + crs.getString("contact_id") + "\">"
							+ crs.getString("title_desc") + " " + crs.getString("contact_fname") + " " + crs.getString("contact_lname") + "</a>";
					link_customer_name = "<a href=\"../customer/customer-list.jsp?customer_id=" + crs.getString("customer_id") + "\">"
							+ crs.getString("customer_name") + "</a>";
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
