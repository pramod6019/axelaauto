package axela.sales;
//Bhagwan Singh
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageCorporate_Update extends Connect {

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
	public String corporate_id = "0";
	public String corporate_name = "";
	public String corporate_code = "";
	public String corporate_cat = "";
	public String corporate_brand_id = "0";
	public String corporate_active = "1";
	public String QueryString = "";
	public String corporate_entry_id = "0";
	public String corporate_entry_date = "";
	public String corporate_modified_id = "0";
	public String corporate_modified_date = "";
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
				emp_id = (session.getAttribute("emp_id")).toString();
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				corporate_id = CNumeric(PadQuotes(request.getParameter("corporate_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
					if (addB.equals("yes")) {
						GetValues(request, response);
						corporate_entry_id = emp_id;
						corporate_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managecorporate-list.jsp?corporate_id=" + corporate_id + "&msg=Corporate Added Successfully!"));
						}
					}
				} else if (update.equals("yes")) {
					status = "Update";
					if (!updateB.equals("yes") && !deleteB.equals("Delete Corporate")) {
						PopulateFields(response);
					} else if (updateB.equals("yes") && !deleteB.equals("Delete Corporate")) {
						GetValues(request, response);
						corporate_modified_id = emp_id;
						corporate_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managecorporate-list.jsp?corporate_id=" + corporate_id + "&msg=Corporate Updated Successfully!"));
						}
					} else if (deleteB.equals("Delete Corporate")) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managecorporate-list.jsp?msg=Corporate Deleted Successfully!"));
						}
					}
				}
			}
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		corporate_name = PadQuotes(request.getParameter("txt_corporate_name"));
		corporate_brand_id = CNumeric(PadQuotes(request.getParameter("dr_corporate_brand_id")));
		corporate_code = PadQuotes(request.getParameter("txt_corporate_code"));
		corporate_cat = PadQuotes(request.getParameter("txt_corporate_cat"));
		corporate_active = PadQuotes(request.getParameter("chk_corporate_active"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
		if (corporate_active.equals("on")) {
			corporate_active = "1";
		} else {
			corporate_active = "0";
		}
	}

	protected void CheckForm() {
		msg = "";
		if (corporate_brand_id.equals("0")) {
			msg = msg + "<br>Select Brand!";
		}
		if (corporate_name.equals("")) {
			msg = msg + "<br>Enter Corporate Name!";
		} else {
			StrSql = "SELECT corporate_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_corporate"
					+ " WHERE corporate_name = '" + corporate_name + "'"
					+ " AND corporate_brand_id =" + corporate_brand_id;
			if (update.equals("yes")) {
				StrSql += " AND corporate_id != " + corporate_id + "";
			}

			if (ExecuteQuery(StrSql).equals(corporate_name)) {
				msg = msg + "<br>Similar Corporate Name found for this Brand!";
			}
		}
		if (!corporate_code.equals("")) {
			StrSql = "SELECT corporate_code "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_corporate"
					+ " WHERE corporate_code = '" + corporate_code + "'"
					+ " AND corporate_brand_id =" + corporate_brand_id;
			if (update.equals("yes")) {
				StrSql += " AND corporate_id != " + corporate_id + "";
			}

			if (ExecuteQuery(StrSql).equals(corporate_code)) {
				msg = msg + "<br>Similar Corporate Code found for this Brand!";
			}
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			corporate_id = ExecuteQuery("SELECT (COALESCE(MAX(corporate_id), 0) + 1) FROM " + compdb(comp_id) + "axela_sales_enquiry_corporate");
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_corporate"
					+ " (corporate_id,"
					+ " corporate_brand_id,"
					+ " corporate_code,"
					+ " corporate_cat,"
					+ " corporate_name,"
					+ " corporate_active,"
					+ " corporate_entry_id,"
					+ " corporate_entry_date)"
					+ " values"
					+ " (" + corporate_id + ","
					+ " " + corporate_brand_id + ","
					+ " '" + corporate_code + "',"
					+ " '" + corporate_cat + "',"
					+ " '" + corporate_name + "',"
					+ " " + corporate_active + ","
					+ " " + corporate_entry_id + ","
					+ " '" + corporate_entry_date + "')";
			// SOP("AddFields=====" + StrSqlBreaker(StrSql));
			updateQuery(StrSql);
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT * FROM " + compdb(comp_id) + "axela_sales_enquiry_corporate WHERE corporate_id = " + corporate_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					corporate_name = crs.getString("corporate_name");
					corporate_brand_id = crs.getString("corporate_brand_id");
					corporate_code = crs.getString("corporate_code");
					corporate_cat = crs.getString("corporate_cat");
					corporate_active = crs.getString("corporate_active");
					corporate_entry_id = crs.getString("corporate_entry_id");
					if (!corporate_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(corporate_entry_id));
						entry_date = strToLongDate(crs.getString("corporate_entry_date"));
					}
					corporate_modified_id = crs.getString("corporate_modified_id");
					if (!corporate_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(corporate_modified_id));
						modified_date = strToLongDate(crs.getString("corporate_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Corporate!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields() {
		CheckForm();
		if (msg.equals("")) {
			StrSql = " UPDATE " + compdb(comp_id) + " axela_sales_enquiry_corporate"
					+ " SET"
					+ " corporate_brand_id = '" + corporate_brand_id + "',"
					+ " corporate_code = '" + corporate_code + "',"
					+ " corporate_cat = '" + corporate_cat + "',"
					+ " corporate_name = '" + corporate_name + "',"
					+ " corporate_active = '" + corporate_active + "',"
					+ " corporate_modified_id = " + corporate_modified_id + ","
					+ " corporate_modified_date = '" + corporate_modified_date + "' "
					+ " WHERE corporate_id = " + corporate_id + "";
			updateQuery(StrSql);
		}
	}

	protected void DeleteFields() {
		StrSql = "SELECT enquiry_corporate_id FROM " + compdb(comp_id) + "axela_sales_enquiry"
				+ " WHERE enquiry_corporate_id = " + corporate_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg += "<br>Corporate is Associated with an Enquiry!";
		}
		if (msg.equals("")) {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_enquiry_corporate"
					+ " WHERE corporate_id =" + corporate_id + "";
			updateQuery(StrSql);
		}
	}
	public String PopulateBrand(String corporate_brand_id, HttpServletRequest request) {

		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();
		try {
			// SOP(Str);
			StrSql = "SELECT brand_id, brand_name "
					+ " FROM axela_brand "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE branch_active = 1" + BranchAccess
					+ " AND branch_branchtype_id = 1"
					+ " GROUP BY brand_id "
					+ " ORDER BY brand_name ";
			// SOP("PopulateBrand======" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("brand_id")).append("");
				Str.append(StrSelectdrop(crs.getString("brand_id"), corporate_brand_id));
				Str.append(">").append(crs.getString("brand_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

}
