package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Managestage_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String stage_id = "0";
	public String stage_name = "";
	public String stage_probability = "";
	public String stage_rank = "";
	public String QueryString = "";
	public String stage_entry_id = "0";
	public String stage_entry_date = "";
	public String stage_modified_id = "0";
	public String stage_modified_date = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				stage_id = CNumeric(PadQuotes(request.getParameter("stage_id")));
				QueryString = PadQuotes(request.getQueryString());

				if ("yes".equals(add)) {
					status = "Add";
					if (!"yes".equals(addB)) {
						stage_name = "";
					} else {
						GetValues(request, response);
						stage_entry_id = emp_id;
						stage_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managestage.jsp?stage_id=" + stage_id + "&msg=Stage Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					status = "Update";
					if (!"yes".equals(updateB) && !"Delete Stage".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Stage".equals(deleteB)) {
						GetValues(request, response);
						stage_modified_id = emp_id;
						stage_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managestage.jsp?stage_id=" + stage_id + "&msg=Stage Updated Successfully!"));
						}
					} else if ("Delete Stage".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managestage.jsp?msg=Stage Deleted Successfully!"));
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
		stage_id = CNumeric(PadQuotes(request.getParameter("stage_id")));
		stage_name = PadQuotes(request.getParameter("txt_stage_name"));
		stage_probability = PadQuotes(request.getParameter("txt_stage_probability"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		try {
			if (stage_name.equals("")) {
				msg = msg + "<br>Enter the Stage Name!";
			}
			if (stage_probability.equals("")) {
				msg = msg + "<br>Enter the Stage Probability!";
			}
			if (!stage_probability.equals("")) {
				if (Integer.parseInt(stage_probability) < 0) {
					msg = msg + "<br>Stage Probability cannot be less than Zero!";
				}
				if (Integer.parseInt(stage_probability) > 100) {
					msg = msg + "<br>Stage Probability cannot be greater than Hundred!";
				}
			}
			if (!stage_name.equals("")) {
				StrSql = "Select stage_name from " + compdb(comp_id) + "axela_sales_enquiry_stage where stage_name = '" + stage_name + "'";
				if (update.equals("yes")) {
					StrSql = StrSql + " and stage_id != " + stage_id + "";
				}
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					msg = msg + "<br>Similar Stage Found! ";
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				stage_id = ExecuteQuery("Select (coalesce(max(stage_id),0)+1) from " + compdb(comp_id) + "axela_sales_enquiry_stage");
				StrSql = "Insert into " + compdb(comp_id) + "axela_sales_enquiry_stage"
						+ " (stage_id,"
						+ " stage_name,"
						+ " stage_rank,"
						+ " stage_probability,"
						+ " stage_entry_id,"
						+ " stage_entry_date)"
						+ " values"
						+ " (" + stage_id + ","
						+ " '" + stage_name + "',"
						+ " (Select (coalesce(max(stage_rank),0)+1) from " + compdb(comp_id) + "axela_sales_enquiry_stage as Rank where 1=1),"
						+ " " + stage_probability + ","
						+ " " + stage_entry_id + ","
						+ " '" + stage_entry_date + "')";
				// SOP("StrSql="+StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "Select * from " + compdb(comp_id) + "axela_sales_enquiry_stage"
					+ " where stage_id = " + stage_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					stage_name = crs.getString("stage_name");
					stage_probability = crs.getString("stage_probability");
					stage_entry_id = crs.getString("stage_entry_id");
					if (!stage_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(stage_entry_id));
						entry_date = strToLongDate(crs.getString("stage_entry_date"));
					}
					stage_modified_id = crs.getString("stage_modified_id");
					if (!stage_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(stage_modified_id));
						modified_date = strToLongDate(crs.getString("stage_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Stage!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry_stage"
						+ " SET"
						+ " stage_probability = '" + stage_probability + "',"
						+ " stage_modified_id = " + stage_modified_id + ","
						+ " stage_modified_date = '" + stage_modified_date + "'"
						+ " where stage_id = " + stage_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		StrSql = "SELECT enquiry_stage_id FROM " + compdb(comp_id) + "axela_sales_enquiry where enquiry_stage_id = " + stage_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Stage is Associated with Enquiry!";
		}
		if (stage_id.equals("1")) {
			msg = msg + "<br>Stage Cannot Be Deleted!";
		}
		if (msg.equals("")) {
			try {
				StrSql = "Delete from " + compdb(comp_id) + "axela_sales_enquiry_stage where stage_id =" + stage_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
}
