package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
public class Manage_Enquiry_Tag_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public static String status = "";
	public String StrSql = "";
	public static String msg = "";
	// public String state_id = "0";
	public String tag_id = "0";
	public String tag_name = "";
	public String user_id = "0";
	public String tag_colour = "";
	public String tag_id_colour = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String QueryString = "";
	public String tag_entry_id = "0";
	public String tag_entry_date = "";
	public String tag_modified_id = "0";
	public String tag_modified_date = "";
	public String entry_date = "";
	public String entry_by = "";
	public String modified_by = "";
	public String modified_date = "";
	public String tag_id_list = "0";
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				add = PadQuotes(request.getParameter("Add"));
				update = PadQuotes(request.getParameter("Update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				tag_id = CNumeric(PadQuotes(request.getParameter("tag_id")));
				tag_name = PadQuotes(request.getParameter("txt_tag_name"));
				tag_colour = PadQuotes(request.getParameter("tag_id_color"));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						tag_name = "";
					} else {
						GetValues(request, response);
						tag_entry_id = emp_id;
						tag_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response
									.encodeRedirectURL("manageenquirytaglist.jsp?tag_id="
											+ tag_id
											+ "&tag_name="
											+ tag_name
											+ "&msg=tag Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB)
							&& !"Delete tag".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB)
							&& !"Delete tag".equals(deleteB)) {
						GetValues(request, response);
						tag_modified_id = emp_id;
						tag_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response
									.encodeRedirectURL("manageenquirytaglist.jsp?Update=yes&tag_id="
											+ tag_id
											+ "&msg=tag Updated Successfully!"));
						}
					} else if ("Delete tag".equals(deleteB)) {

						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response
									.encodeRedirectURL("manageenquirytaglist.jsp?msg=tag Deleted Successfully!"));
						}
					}
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		tag_colour = PadQuotes(request.getParameter("tag_id_color"));
		tag_name = PadQuotes(request.getParameter("txt_tag_name"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));

	}

	protected void CheckForm() {
		msg = "";
		try {
			if (tag_colour.equals("0")) {
				msg = msg + "<br>Select Color!";
			}
			if (tag_name.equals("")) {
				msg = msg + "<br>Enter tag!";
			} else {
				StrSql = "SELECT tag_name"
						+ " FROM " + compdb(comp_id) + "axela_customer_tag"
						+ " WHERE tag_id = " + tag_id;
				if (update.equals("yes")) {
					StrSql += " AND tag_id != " + tag_id + "";
				}
				if (ExecuteQuery(StrSql).equals("tag_name")) {
					msg = msg + "<br>Similar Tag Name Found!";
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_tag"
						+ " (tag_colour,"
						+ " tag_name,"
						+ " tag_entry_id,"
						+ " tag_entry_date)"
						+ " values"
						+ " ('" + tag_colour + "',"
						+ "'" + tag_name + "',"
						+ " " + tag_entry_id + ","
						+ " '" + tag_entry_date + "')";
				// SOP("StrSql=====insert=======" + StrSql);

				tag_id = UpdateQueryReturnID(StrSql);
				// updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			// int tag_idi = Integer.parseInt(tag_id);
			StrSql = "SELECT * FROM " + compdb(comp_id) + " axela_customer_tag"
					+ " WHERE tag_id = '" + tag_id + "'";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					tag_id = crs.getString("tag_id");
					tag_name = crs.getString("tag_name");
					// SOP("tag_name===" + tag_name + "id========" + tag_id);
					tag_colour = crs.getString("tag_colour");
					tag_entry_id = crs.getString("tag_entry_id");

					if (!tag_entry_id.equals("0")) {
						entry_by = Exename(comp_id,
								Integer.parseInt(tag_entry_id));
						entry_date = strToLongDate(crs
								.getString("tag_entry_date"));
					}
					tag_modified_id = crs.getString("tag_modified_id");
					if (!tag_modified_id.equals("0")) {
						modified_by = Exename(comp_id,
								Integer.parseInt(tag_modified_id));
						modified_date = strToLongDate(crs
								.getString("tag_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response
						.encodeRedirectURL("../portal/error.jsp?msg=Invalid tag!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	protected void UpdateFields() {
		// SOP("updatafields.........");
		CheckForm();

		if (msg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_customer_tag" + " SET"
						+ " tag_name = '" + tag_name + "',"
						+ " tag_colour = '" + tag_colour + "',"
						+ " tag_modified_id=" + tag_modified_id + ","
						+ " tag_modified_date='" + tag_modified_date + "'"
						+ " WHERE tag_id = " + tag_id + "";
				// SOP("tag_colour update==" + tag_colour);
				updateQuery(StrSql);

			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		StrSql = "SELECT tagtrans_tag_id FROM " + compdb(comp_id)
				+ "axela_customer_tag_trans WHERE tagtrans_tag_id = " + tag_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Tag is Associated with a Enquiry!";
		}

		StrSql = "SELECT tagbooking_tag_id FROM " + compdb(comp_id)
				+ "axela_sales_booking_tags WHERE tagbooking_tag_id = " + tag_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Tag is Associated with a Booking!";
		}

		if (tag_id.equals("1") || tag_id.equals("2")) {
			msg = msg + "<br>Tag 'Satisfied' and 'Dis-Satisfied' can not be Deleted!";
		}

		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_customer_tag where tag_id = " + tag_id + " ";
				// SOP("StrSql===" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + ex);
			}
		}
	}

}
