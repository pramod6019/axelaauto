package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageRateClass_Update extends Connect {

	public String add = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public static String status = "";
	public String StrSql = "";
	public String rateclass_type = "1";
	public static String msg = "";
	public String rateclass_id = "0";
	public String rateclass_name = "";
	public String QueryString = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				CheckPerm(comp_id, "emp_role_id", request, response);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				rateclass_id = CNumeric(PadQuotes(request.getParameter("rateclass_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						rateclass_name = "";
					} else {
						GetValues(request, response);
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managerateclass.jsp?rateclass_id=" + rateclass_id + "&msg=Rate Class Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Rate Class".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Rate Class".equals(deleteB)) {
						GetValues(request, response);
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managerateclass.jsp?rateclass_id=" + rateclass_id + "&msg=Rate Class Updated Successfully!"));
						}
					} else if ("Delete Rate Class".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managerateclass.jsp?msg=Rate Class Deleted Successfully!"));
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
		rateclass_name = PadQuotes(request.getParameter("txt_rateclass_name"));
		rateclass_type = CNumeric(PadQuotes(request.getParameter("dr_rateclass_type")));
	}

	protected void CheckForm() {
		msg = "";
		if (rateclass_name.equals("")) {
			msg = msg + "<br>Enter Rate Class!";
		}
		if (rateclass_type.equals("0")) {
			msg += "<br>Select Type!";
		}
		try {
			if (!rateclass_name.equals("")) {
				if (update.equals("yes") && !rateclass_name.equals("")) {
					StrSql = "Select rateclass_name from " + compdb(comp_id) + "axela_rate_class where rateclass_name = '" + rateclass_name + "' and rateclass_id != " + rateclass_id + "";
				}
				if (add.equals("yes") && !rateclass_name.equals("")) {
					StrSql = "Select rateclass_name from " + compdb(comp_id) + "axela_rate_class where rateclass_name = '" + rateclass_name + "'";
				}
				CachedRowSet crs =processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					msg = msg + "<br>Similar Rate Class Found!";
				}
				crs.close();
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
				rateclass_id = ExecuteQuery("Select (coalesce(max(rateclass_id),0)+1) from " + compdb(comp_id) + "axela_rate_class");
				StrSql = "Insert into " + compdb(comp_id) + "axela_rate_class"
						+ "( rateclass_id,"
						+ " rateclass_name,"
						+ " rateclass_type)"
						+ " values"
						+ " (" + rateclass_id + ","
						+ " '" + rateclass_name + "',"
						+ " " + rateclass_type + ")";
				SOP("StrSql------AddFields--" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "Select * from " + compdb(comp_id) + "axela_rate_class where rateclass_id = " + rateclass_id + "";
			CachedRowSet crs =processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					rateclass_id = crs.getString("rateclass_id");
					rateclass_name = crs.getString("rateclass_name");
					rateclass_type = crs.getString("rateclass_type");
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Rate Class!"));
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
				StrSql = " UPDATE " + compdb(comp_id) + "axela_rate_class"
						+ " SET"
						+ " rateclass_name = '" + rateclass_name + "',"
						+ " rateclass_type = " + rateclass_type
						+ " where rateclass_id = " + rateclass_id + "";
				SOP("StrSql------UpdateFields--" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		if (rateclass_id.equals("1")) {
			msg = msg + "<br>Cannot Delete First Record!";
		}
		StrSql = "SELECT branch_rateclass_id	 from " + compdb(comp_id) + "axela_branch where branch_rateclass_id	 = " + rateclass_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Branch Class is Associated with a Branch!";
		}
		StrSql = "SELECT price_rateclass_id	"
				+ " FROM " + compdb(comp_id) + "axela_inventory_item_price"
				+ " WHERE price_rateclass_id	 = " + rateclass_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Branch Class ia Associated with Price!";
		}
		StrSql = "SELECT brochure_rateclass_id	"
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_brochure"
				+ " WHERE brochure_rateclass_id	 = " + rateclass_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Branch Class ia Associated with Brochure!";
		}

		if (msg.equals("")) {
			try {
				StrSql = "Delete from " + compdb(comp_id) + "axela_rate_class where rateclass_id = " + rateclass_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
	public String PopulateType(String rateclass_type) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0>Select</option>");

		Str.append("<option value=1").append(StrSelectdrop("1", rateclass_type))
				.append(">").append("Sales");
		Str.append("</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", rateclass_type))
				.append(">").append("Purchase");
		Str.append("</option>\n");

		return Str.toString();
	}
}
