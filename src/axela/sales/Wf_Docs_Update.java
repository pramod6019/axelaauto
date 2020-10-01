package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Wf_Docs_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public static String status = "";
	public String StrSql = "";
	public static String msg = "";
	public String prod_name = "";
	public String doc_id = "";
	public String doc_wf_title = "";
	public String doc_effective = "";
	public String doc_daynos = "";
	public String doc_entry_id = "0";
	public String doc_entry_date = "";
	public String doc_modified_id = "0";
	public String doc_modified_date = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String QueryString = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				doc_id = CNumeric(PadQuotes(request.getParameter("doc_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						doc_wf_title = "";
						doc_effective = "0";
						doc_daynos = "0";
					} else {
						GetValues(request, response);
						doc_entry_id = emp_id;
						doc_entry_date = ToLongDate(kknow());
						AddFields(request);
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("wf-docs-list.jsp?doc_id=" + doc_id + "&msg=Document Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Document".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Document".equals(deleteB)) {
						GetValues(request, response);
						doc_modified_id = emp_id;
						doc_modified_date = ToLongDate(kknow());
						UpdateFields(request);
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("wf-docs-list.jsp?doc_id=" + doc_id + "&msg=Document Updated Successfully!"));
						}
					} else if ("Delete Document".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("wf-docs-list.jsp?msg=Document Deleted Successfully!"));
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

		doc_id = CNumeric(PadQuotes(request.getParameter("doc_id")));
		doc_wf_title = PadQuotes(request.getParameter("txt_doc_wf_title"));
		doc_effective = CNumeric(PadQuotes(request.getParameter("dr_effective")));
		doc_daynos = CNumeric(PadQuotes(request.getParameter("txt_doc_daynos")));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (doc_wf_title.equals("")) {
			msg = msg + "<br>Enter Title!";
		}
		if (doc_daynos.equals("0")) {
			msg = msg + "<br>Enter No. of Days!";
		}
		if (doc_effective.equals("0")) {
			msg = msg + "<br>Select Effective From!";
		}
	}

	protected void AddFields(HttpServletRequest request) {
		CheckForm();
		if (msg.equals("")) {
			try {
				doc_id = ExecuteQuery("Select max(doc_id) from " + compdb(comp_id) + "axela_sales_so_wf_docs");
				if (doc_id == null || doc_id.equals("")) {
					doc_id = "0";
				}
				int doc_idi = Integer.parseInt(doc_id) + 1;
				doc_id = "" + doc_idi;
				StrSql = "Insert into " + compdb(comp_id) + "axela_sales_so_wf_docs"
						+ " (doc_id,"
						+ " doc_wf_title,"
						+ " doc_daynos,"
						+ " doc_effective,"
						+ " doc_entry_id,"
						+ " doc_entry_date)"
						+ " values"
						+ " (" + doc_id + ","
						+ " '" + doc_wf_title + "',"
						+ " " + doc_daynos + ","
						+ " " + doc_effective + ","
						+ " " + doc_entry_id + ","
						+ " '" + doc_entry_date + "')";
				updateQuery(StrSql);
				// SOP("StrSql in addfields==" + StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "select *"
					+ " from " + compdb(comp_id) + "axela_sales_so_wf_docs"
					+ " where doc_id = " + doc_id + "";
			// SOP("StrSql in PopulateFields======" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					doc_id = crs.getString("doc_id");
					doc_wf_title = crs.getString("doc_wf_title");
					doc_effective = crs.getString("doc_effective");
					doc_daynos = crs.getString("doc_daynos");
					doc_entry_id = crs.getString("doc_entry_id");
					doc_entry_date = crs.getString("doc_entry_date");
					doc_modified_id = crs.getString("doc_modified_id");
					if (!crs.getString("doc_entry_id").equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(doc_entry_id));
						entry_date = strToLongDate(crs.getString("doc_entry_date"));
					}
					if (!crs.getString("doc_modified_id").equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(doc_modified_id));
						modified_date = strToLongDate(crs.getString("doc_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Document!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields(HttpServletRequest request) throws Exception {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so_wf_docs"
						+ " SET"
						+ " doc_wf_title = '" + doc_wf_title + "',"
						+ " doc_effective = " + doc_effective + ","
						+ " doc_daynos = " + doc_daynos + ", "
						+ " doc_modified_id = " + doc_modified_id + ","
						+ " doc_modified_date = '" + doc_modified_date + "'"
						+ " where doc_id = " + doc_id + "";
				// SOP("StrSql--"+StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		if (msg.equals("")) {
			try {
				StrSql = "Delete from " + compdb(comp_id) + "axela_sales_so_wf_docs where doc_id = " + doc_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateEffective() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		Str.append("<option value=1 ").append(StrSelectdrop("1", doc_effective)).append(">Sales Order Date</option>");
		Str.append("<option value=2 ").append(StrSelectdrop("2", doc_effective)).append(">Delivery Date</option>");
		return Str.toString();
	}
}
