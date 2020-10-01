package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Service_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public static String status = "";
	public String StrSql = "";
	public static String msg = "";
	public String course_id = "0";
	public String comp_id = "0";
	public String course_name = "";
	public String course_code = "";
	public String course_desc = "";
	public String course_active = "";
	public String course_variable = "";
	public String course_notes = "";
	public String course_entry_id = "";
	public String course_entry_date = "";
	public String course_modified_id = "";
	public String course_modified_date = "";
	public String entry_date = "";
	public String modified_date = "";
	public String emp_id = "", branch_id = "";
	public String course_entry_by = "";
	public String course_modified_by = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_course_access", request, response);
			HttpSession session = request.getSession(true);
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));

				add = PadQuotes(request.getParameter("Add"));
				update = PadQuotes(request.getParameter("Update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				course_id = CNumeric(PadQuotes(request.getParameter("course_id")));

				if (course_id.equals("0") || !isNumeric(course_id)) {
					course_id = "0";
				}
				// if (update.equals("yes")) {
				// if (course_id == null || course_id.equals("") || !isNumeric(course_id)) {
				// response.sendRedirect(response.encodeRedirectURL("index.jsp"));
				// }
				// }

				if (add == null) {
					add = "";
				}
				if (update == null) {
					update = "";
				}
				if (addB == null) {
					addB = "";
				}
				if (updateB == null) {
					updateB = "";
				}
				if (deleteB == null) {
					deleteB = "";
				}
				if (msg == null) {
					msg = "";
				}

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"Add Service".equals(addB)) {
						course_name = "";
						course_code = "";
						course_notes = "";
						course_desc = "";
						course_active = "1";
						course_variable = "";
						course_entry_id = "0";
						course_entry_date = "";
						course_modified_id = "";
						course_modified_date = "";
					} else {
						CheckPerm(comp_id, "emp_service_add", request, response);
						GetValues(request, response);
						course_entry_id = CNumeric(GetSession("emp_id", request));
						course_entry_date = ToLongDate(kknow());
						course_modified_date = "";
						course_modified_id = "0";
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;

						} else {
							response.sendRedirect(response.encodeRedirectURL("service-list.jsp?course_id=" + course_id + "&msg=Service added successfully!" + msg + ""));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"Update Service".equals(updateB) && !"Delete Service".equals(deleteB)) {
						PopulateFields(response);
					} else if ("Update Service".equals(updateB) && !"Delete Service".equals(deleteB)) {
						CheckPerm(comp_id, "emp_service_edit", request, response);
						GetValues(request, response);
						course_modified_id = CNumeric(GetSession("emp_id", request));
						course_modified_date = ToLongDate(kknow());
						UpdateFields();

						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("service-list.jsp?course_id=" + course_id + "&msg=Service updated successfully!" + msg + ""));
						}
					} else if ("Delete Service".equals(deleteB)) {
						CheckPerm(comp_id, "emp_service_delete", request, response);
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("service-list.jsp?msg=Service deleted successfully!"));
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

		course_name = PadQuotes(request.getParameter("txt_course_name"));
		course_code = PadQuotes(request.getParameter("txt_course_code"));
		course_notes = PadQuotes(request.getParameter("txt_course_notes"));
		course_desc = PadQuotes(request.getParameter("txt_course_desc"));
		course_active = PadQuotes(request.getParameter("ch_course_active"));
		course_variable = PadQuotes(request.getParameter("ch_course_variable"));
		CheckedFields();
		course_entry_by = PadQuotes(request.getParameter("course_entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		course_modified_by = PadQuotes(request.getParameter("course_modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
		if (course_id.equals("")) {
			course_id = "0";
		}
	}

	protected void CheckedFields() {
		if (course_active.equals("on")) {
			course_active = "1";
		} else {
			course_active = "0";
		}
		if (course_variable.equals("on")) {
			course_variable = "1";
		} else {
			course_variable = "0";
		}
	}

	protected void CheckForm() {
		msg = "";
		String Msg1 = "";

		if (course_name.equals("")) {
			msg = msg + "<br>Enter Service!";
		}
		if (!course_name.equals("")) {
			try {
				StrSql = "select course_name from " + compdb(comp_id) + "axela_course where course_name = '" + course_name + "'";
				if (update.equals("yes")) {
					StrSql = StrSql + " and course_id!=" + course_id + "";
				}
				CachedRowSet crs = processQuery(StrSql, 0);

				if (crs.isBeforeFirst()) {
					Msg1 = Msg1 + "<br>Similar Service found!";
				}
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
		if (course_code.equals("")) {
			msg = msg + "<br>Enter Code!";
		}
		if (!course_code.equals("")) {
			try {
				StrSql = "select course_code from " + compdb(comp_id) + "axela_course where course_code = '" + course_code + "'";
				if (update.equals("yes")) {
					StrSql = StrSql + " and course_id!=" + course_id + "";
				}
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					Msg1 = Msg1 + "<br>Similar Service Code found!";
				}
				crs.close();

			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
		if (course_desc.equals("")) {
			msg = msg + "<br>Enter Description!";
		}
		if (course_modified_id.equals("")) {
			course_modified_id = "0";
		}
		msg = msg + Msg1;
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {

				course_id = ExecuteQuery("Select max(course_id) as ID from " + compdb(comp_id) + "axela_course");
				if (course_id == null || course_id.equals("")) {
					course_id = "0";
				}
				int course_idi = Integer.parseInt(course_id) + 1;
				course_id = "" + course_idi;

				StrSql = "insert into " + compdb(comp_id) + "axela_course"
						+ "(course_id , "
						+ "course_cat_id,"
						+ "course_name,"
						+ "course_code,"
						+ "course_desc,"
						+ "course_active,"
						+ "course_variable,"
						+ "course_notes,"
						+ "course_entry_id,"
						+ "course_entry_date,"
						+ "course_modified_id,"
						+ "course_modified_date) "
						+ "values "
						+ "('" + course_id + "',"
						+ "2,"
						+ "'" + course_name + "',"
						+ "'" + course_code + "',"
						+ "'" + course_desc + "',"
						+ "'" + course_active + "',"
						+ "'" + course_variable + "',"
						+ "'" + course_notes + "',"
						+ "'" + course_entry_id + "',"
						+ "'" + course_entry_date + "',"
						+ "'0',"
						+ "'')";
				// SOP("StrSql=========="+StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			int course_idi = Integer.parseInt(course_id);
			StrSql = "select * from " + compdb(comp_id) + "axela_course where  course_id=" + course_idi + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					course_id = crs.getString("course_id");
					course_name = crs.getString("course_name");
					course_code = crs.getString("course_code");
					course_desc = crs.getString("course_desc");
					course_active = crs.getString("course_active");
					course_variable = crs.getString("course_variable");
					course_notes = crs.getString("course_notes");

					entry_date = strToLongDate(crs.getString("course_entry_date"));
					modified_date = strToLongDate(crs.getString("course_modified_date"));

					course_entry_id = crs.getString("course_entry_id");
					if (!course_entry_id.equals("")) {
						course_entry_by = Exename(comp_id, Integer.parseInt(course_entry_id));
					}
					entry_date = strToLongDate(crs.getString("course_entry_date"));

					course_modified_id = crs.getString("course_modified_id");
					if (!course_modified_id.equals("0")) {
						course_modified_by = Exename(comp_id, Integer.parseInt(course_modified_id));
					}
					modified_date = strToLongDate(crs.getString("course_modified_date"));

				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Service!"));
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
				StrSql = " UPDATE " + compdb(comp_id) + "axela_course SET "
						+ " course_name  ='" + course_name + "',"
						+ " course_code  ='" + course_code + "',"
						+ " course_desc  ='" + course_desc + "',"
						+ " course_active  ='" + course_active + "',"
						+ " course_variable  ='" + course_variable + "',"
						+ " course_notes  ='" + course_notes + "',"
						+ " course_modified_id  ='" + course_modified_id + "',"
						+ " course_modified_date  ='" + course_modified_date + "' "
						+ " where course_id = " + course_id + " ";
				// SOP("StrSql...."+StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}

		}
	}

	protected void DeleteFields() {
		StrSql = "SELECT fee_course_id from " + compdb(comp_id) + "axela_course_fee where fee_course_id = " + course_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Service Fee is associated with Service!";
		}

		StrSql = "SELECT invcourse_course_id from " + compdb(comp_id) + "axela_invoice_course where 1=1 and invcourse_course_id = " + course_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Invoice is associated with Service!";
		}

		StrSql = "SELECT studcourse_course_id from " + compdb(comp_id) + "axela_student_course where 1=1 and studcourse_course_id = " + course_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Service is associated with Student Enquiry!";
		}

		StrSql = "SELECT courseweblink_course_id from " + compdb(comp_id) + "axela_course_weblink where 1=1 and courseweblink_course_id = " + course_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Web Link is associated with Service!";
		}

		if (msg.equals("")) {
			try {
				StrSql = "Delete from " + compdb(comp_id) + "axela_course_subject where  course_course_id =" + course_id + "";
				updateQuery(StrSql);
				StrSql = "Delete from " + compdb(comp_id) + "axela_course where  course_id =" + course_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
}
// Modified by Dhanesh on 08-08-08

