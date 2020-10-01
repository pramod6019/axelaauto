package axela.insurance;
//satish  5-Apr-2013

import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Vehicle_Dash_Jobcard extends Connect {

	public String veh_id = "0";
	public String veh_reg_no = "";
	public String StrSearch = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String StrHTML = "";
	public String StrSql = "";
	public String msg = "";
	public String comp_id = "0";
	public String CountSql = "";
	public String SqlJoin = "";
	public String QueryString = "";
	public String orderby = "";
	public String ordertype = "";
	public String ordernavi = "";
	DecimalFormat df = new DecimalFormat("0.00");

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				CheckPerm(comp_id, "emp_service_vehicle_access", request, response);
				veh_id = CNumeric(PadQuotes(request.getParameter("veh_id")));
				StrSql = "SELECT veh_reg_no FROM " + compdb(comp_id) + "axela_service_veh WHERE veh_id = " + veh_id + "";
				veh_reg_no = ExecuteQuery(StrSql);
				if (veh_reg_no.equals("")) {
					response.sendRedirect("../portal/error.jsp?msg=Invalid Vehicle ID!");
				}

				StrHTML = ListData(comp_id, veh_id);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String ListData(String comp_id, String veh_id) {
		int TotalRecords = 0;
		StringBuilder Str = new StringBuilder();

		StrSql = "SELECT jc_id, jc_branch_id, CONCAT('JC', branch_code, jc_no) AS jc_no, jc_time_in,"
				+ " jc_contact_id, jc_title, jc_cust_voice, jc_time_promised, jc_time_ready,"
				+ " jc_netamt, jc_totaltax, jc_grandtotal, jc_ro_no, jc_auth, jc_active, jc_time_out,"
				+ " jcstage_name, model_name, item_name, veh_id, veh_reg_no, priorityjc_name, jccat_name,"
				+ " jctype_name, jc_priority_trigger, customer_id, customer_name, contact_id,"
				+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name,"
				+ " branch_id, CONCAT(branch_name, ' (', branch_code, ')') AS branch_name,"
				+ " CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name, emp_id, item_id, model_id";

		CountSql = "SELECT COUNT(DISTINCT(jc_id))";

		SqlJoin = " FROM " + compdb(comp_id) + "axela_service_jc"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jc_customer_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = jc_emp_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_item_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_stage ON jcstage_id = jc_jcstage_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_priority ON priorityjc_id = jc_priorityjc_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_cat ON jccat_id = jc_jccat_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_type ON jctype_id = jc_jctype_id"
				+ " WHERE jc_veh_id = " + veh_id + BranchAccess + ExeAccess + "";

		StrSql += SqlJoin + StrSearch;
		CountSql += SqlJoin + StrSearch;

		TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
		if (TotalRecords != 0) {
			// display on jsp page
			StrSql += " GROUP BY jc_id";
			if (orderby.equals("")) {
				StrSql += " ORDER BY jc_id DESC";
			} else {
				StrSql += " ORDER BY " + orderby + " " + ordertype + " ";
			}
			CachedRowSet crs = processQuery(StrSql, 0);
			try {
				int count = 0;
				QueryString = QueryString.replaceAll("&orderby=" + orderby, "");
				QueryString = QueryString.replaceAll("&ordertype=" + ordertype, "");
				ordernavi = "jobcard-list.jsp?" + QueryString;
				if (ordertype.equals("asc")) {
					ordertype = "desc";
				} else {
					ordertype = "asc";
				}
				Str.append("<table border=\"1\" cellspacing=\"0\" cellpadding=\"0\" class=\"listtable\">");
				Str.append("<tr align=\"center\">\n");
				Str.append("<th>#</th>\n");
				Str.append("<th>").append(GridLink("ID", "jc_id", ordernavi, ordertype)).append("</th>\n");
				Str.append("<th>").append(GridLink("No.", "jc_no", ordernavi, ordertype)).append("</th>\n");
				Str.append("<th>").append(GridLink("Job Card", "jc_title", ordernavi, ordertype)).append("</th>\n");
				Str.append("<th>").append(GridLink("Contact", "jc_contact_id", ordernavi, ordertype)).append("</th>\n");
				Str.append("<th>").append(GridLink("Time", "jc_time_in", ordernavi, ordertype)).append("</th>\n");
				Str.append("<th>").append(GridLink("Voice", "jc_cust_voice", ordernavi, ordertype)).append("</th>\n");
				Str.append("<th>").append(GridLink("Item", "item_name", ordernavi, ordertype)).append("</th>\n");
				Str.append("<th>").append(GridLink("Reg. No.", "veh_reg_no", ordernavi, ordertype)).append("</th>\n");
				Str.append("<th>").append(GridLink("Stage", "jcstage_name", ordernavi, ordertype)).append("</th>\n");
				Str.append("<th>").append(GridLink("Priority", "priorityjc_name", ordernavi, ordertype)).append("</th>\n");
				Str.append("<th>").append(GridLink("Level", "jc_priority_trigger", ordernavi, ordertype)).append("</th>\n");
				Str.append("<th>").append(GridLink("Type", "jctype_name", ordernavi, ordertype)).append("</th>\n");
				Str.append("<th>").append(GridLink("Category", "jccat_name", ordernavi, ordertype)).append("</th>\n");
				Str.append("<th>").append(GridLink("Amount", "jc_grandtotal", ordernavi, ordertype)).append("</th>\n");
				Str.append("<th>").append(GridLink("Executive", "emp_id", ordernavi, ordertype)).append("</th>\n");
				Str.append("<th>").append(GridLink("Branch", "branch_name", ordernavi, ordertype)).append("</th>\n");
				Str.append("<th>Actions</th>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr>\n<td align=\"center\" valign=\"top\">").append(count);
					Str.append("</td>\n<td valign=\"top\" align=\"center\">").append(crs.getString("jc_id"));
					Str.append("</td>\n<td valign=\"top\" align=\"center\">").append(crs.getString("jc_no"));
					Str.append("</td>\n<td valign=\"top\" align=\"left\">");
					Str.append("<a href=\"jobcard-dash.jsp?jc_id=").append(crs.getString("jc_id")).append("\">").append(crs.getString("jc_title")).append("</a>");
					if (!crs.getString("jc_ro_no").equals("")) {
						Str.append("<br>RO. No: ").append(crs.getString("jc_ro_no"));
					}

					if (crs.getString("jc_active").equals("0")) {
						Str.append("<br><font color=\"red\"><b>[Inactive]</b></font>");
					}

					if (crs.getString("jc_auth").equals("1")) {
						Str.append("<br><font color=\"red\">[Authorized]</font>");
					}
					Str.append("</td>\n<td valign=\"top\" align=\"left\">");
					Str.append("<a href=\"../customer/customer-list.jsp?customer_id=");
					Str.append(crs.getString("customer_id")).append("\">");
					Str.append(crs.getString("customer_name")).append("</a>");
					Str.append("<br><a href=\"../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("contact_id")).append("\">");
					Str.append(crs.getString("contact_name")).append("</a>");
					Str.append("</td>\n<td valign=\"top\" align=\"left\" nowrap>Time In: ").append(strToLongDate(crs.getString("jc_time_in")));
					Str.append("<br>Promised: ").append(strToLongDate(crs.getString("jc_time_promised")));
					if (!crs.getString("jc_time_ready").equals("")) {
						Str.append("<br>Ready: ").append(strToLongDate(crs.getString("jc_time_ready")));
					}

					if (!crs.getString("jc_time_out").equals("")) {
						Str.append("<br>Time Out: ").append(strToLongDate(crs.getString("jc_time_out")));
					}
					Str.append("</td>\n<td align=\"left\" valign=\"top\">").append(crs.getString("jc_cust_voice"));
					Str.append("</td>\n<td align=\"left\" valign=\"top\"><a href=\"../inventory/inventory-item-list.jsp?item_id=").append(crs.getString("item_id")).append("\">");
					Str.append(crs.getString("item_name"));
					Str.append("</a></td>\n<td valign=\"top\" align=\"left\" nowrap><a href=\"../service/vehicle-list.jsp?veh_id=");
					Str.append(crs.getString("veh_id")).append("\">").append(SplitRegNo(crs.getString("veh_reg_no"), 2)).append("</a>");
					Str.append("</td>\n<td valign=\"top\" align=\"left\">").append(crs.getString("jcstage_name"));
					Str.append("</td>\n<td valign=\"top\" align=\"left\">").append(crs.getString("priorityjc_name"));
					Str.append("</td>\n<td valign=\"top\" align=\"center\">").append(crs.getString("jc_priority_trigger"));
					Str.append("</td>\n<td valign=\"top\" align=\"left\">").append(crs.getString("jctype_name"));
					Str.append("</td>\n<td valign=\"top\" align=\"left\">").append(crs.getString("jccat_name"));

					Str.append("</td>\n<td valign=\"top\" align=\"right\" nowrap>Net Total: ").append(IndDecimalFormat(df.format(crs.getDouble("jc_netamt"))));
					if (!crs.getString("jc_totaltax").equals("0")) {
						Str.append("<br>Service Tax: ").append(IndDecimalFormat(df.format(crs.getDouble("jc_totaltax"))));
					}
					Str.append("<br><b>Total: ").append(IndDecimalFormat(df.format(crs.getDouble("jc_grandtotal")))).append("</b>");
					Str.append("<br><br></td>\n<td valign=\"top\">");
					Str.append("<a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getInt("emp_id")).append("\">").append(crs.getString("emp_name")).append("</a>");
					Str.append("</td>\n<td valign=\"top\"><a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append("\">");
					Str.append(crs.getString("branch_name")).append("</a></td>\n");
					Str.append("<td valign=\"top\" align=\"left\" nowrap>");
					Str.append("<a href=\"jobcard-update.jsp?update=yes&jc_id=").append(crs.getString("jc_id")).append("\">Update Job Card</a>");
					Str.append("<br/><a href=\"jobcard-authorize.jsp?jc_id=").append(crs.getString("jc_id")).append(" \">Authorize</a>");
					Str.append("<br/><a href=\"../service/ticket-add.jsp?add=yes&jc_id=").append(crs.getString("jc_id")).append("\">Add Ticket</a>");
					Str.append("<br/><a href=\"../invoice/invoice-update.jsp?add=yes&jc_id=").append(crs.getString("jc_id")).append("\">Add Invoice</a>");
					if (!crs.getString("jc_time_out").equals("")) {
						if (Long.parseLong(crs.getString("jc_time_out")) <= Long.parseLong(ToLongDate(kknow()))) {
							Str.append("<br/><a href=\"../service/jobcard-cust-feedback.jsp?add=yes&jc_id=").append(crs.getString("jc_id")).append("\">Customer Feedback</a>");
						}
					}
					Str.append("<br/><a href=\"jobcard-print-pdf.jsp?jc_id=").append(crs.getString("jc_id")).append("&target=").append(Math.random()).append("\" target=_blank>Print Job Card</a>");
					Str.append("<br/><a href=\"jobcard-email.jsp?jc_id=").append(crs.getString("jc_id")).append("\">Email Job Card</a>");
					Str.append("<br/><a href=\"gate-pass-print-pdf.jsp?jc_id=").append(crs.getString("jc_id")).append("&target=").append(Math.random()).append("\" target=_blank>Print Gate Pass</a>");
					Str.append("</td>\n</tr>\n");
				}
				Str.append("</table>\n");
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		} else {
			Str.append("<br><br><br><br><font color=\"red\"><b>No Job Card(s) found!</b></font><br><br><br><br>");
		}
		return Str.toString();
	}
}
