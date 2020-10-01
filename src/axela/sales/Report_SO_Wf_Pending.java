package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_SO_Wf_Pending extends Connect {

	public String StrHTML = "";
	public String StrTitle = "Sales Order Work Flow Pending Delivery";
	public String emp_id = "0";
	public String comp_id = "0";
	public String dr_branch_id = "0";
	public String StrSearch = "";
	public String StrSql = "";
	public String branch_id = "0", BranchAccess = "";
	public String msg = "", ExeAccess = "";
	public String emp_all_exe = "";
	StringBuilder strSOID = new StringBuilder();

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		CheckPerm(comp_id, "emp_mis_access", request, response);
		if (!comp_id.equals("0")) {
			emp_id = CNumeric(GetSession("emp_id", request));
			ExeAccess = GetSession("ExeAccess", request);
			BranchAccess = GetSession("BranchAccess", request);
			branch_id = CNumeric(GetSession("emp_branch_id", request));
			emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
			if (branch_id.equals("0")) {
				dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
			} else {
				dr_branch_id = branch_id;
			}

			try {
				if (!dr_branch_id.equals("0")) {
					StrSearch = StrSearch + " and so_branch_id=" + dr_branch_id;
				} else {
					msg = msg + "<br>Select Branch!";
				}

				if (!msg.equals("")) {
					msg = "Error!" + msg;
				}
				if (msg.equals("")) {
					StrHTML = SalesOrderDetail();
					SetSession("sostrsql", " and (" + strSOID.toString() + ")", request);
					// SetSession("Campaignstrsql", StrSearch, request);
				}
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String SalesOrderDetail() {
		StringBuilder Str = new StringBuilder();
		String effective = "";
		String duedate = "";
		try {
			StrSql = "select so_id, so_date, so_delivered_date, so_emp_id, doc_effective, doc_duedays, "
					+ " doc_wf_title, emp_id, CONCAT(emp_name,' (',emp_ref_no,')') as emp_name  "
					+ " from " + compdb(comp_id) + "axela_sales_so "
					+ " inner join " + compdb(comp_id) + "axela_sales_so_docs on doc_so_id = so_id "
					+ " inner join " + compdb(comp_id) + "axela_branch on branch_id= so_branch_id "
					+ " inner join " + compdb(comp_id) + "axela_emp on emp_id= so_emp_id "
					+ " where 1=1 and so_active = '1' and "
					+ " doc_title='' and doc_effective!=0 and doc_duedays!=0 and "
					+ " (if(doc_effective=1,(DATE_FORMAT(date_add(so_date, interval doc_duedays day), '%Y%m%d000000') <= '" + ToShortDate(kknow()) + "'),'') "
					+ " OR "
					+ " if(doc_effective=2"
					+ " and so_delivered_date!='',"
					+ " (DATE_FORMAT(date_add(so_delivered_date, interval doc_duedays day), '%Y%m%d000000') <= '" + ToShortDate(kknow()) + "'),'') )"
					+ BranchAccess;
			if (!dr_branch_id.equals("0")) {
				StrSql = StrSql + " and branch_id = " + dr_branch_id;
			}

			StrSql += ExeAccess;

			StrSql = StrSql + " order by so_id desc ";
			// SOP("StrSql==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			int count = 0;
			if (crs.isBeforeFirst()) {

				Str.append("<div class=\"  table-bordered\">");
				Str.append("<table class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\" width=5%>#</th>");
				Str.append("<th data-hide=\"phone\">Sales Consultant</th>");
				Str.append("<th data-hide=\"phone\">SO ID</th>\n");
				Str.append("<th data-hide=\"phone\">SO Date</th>\n");
				// Str.append("<th>Delivery Date</th>\n");
				Str.append("<th data-hide=\"phone\">WF Title</th>\n");
				Str.append("<th data-hide=\"phone\">Effective From</th>\n");
				Str.append("<th data-hide=\"phone\">Due Date</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count++;
					if (crs.getString("doc_effective").equals("1")) {
						effective = "Sales Order Date";
					} else if (crs.getString("doc_effective").equals("2")) {
						effective = "Delivery Date";
					}
					Str.append("<tr>");
					Str.append("<td valign=top align=center>").append(count).append("</td>");
					Str.append("<td valign=top align=left>");

					Str.append(ExeDetailsPopover(crs.getInt("emp_id"), crs.getString("emp_name"), "") + "</td>");
					Str.append("<td valign=top align=center>").append(crs.getString("so_id")).append("</td>\n");
					Str.append("<td valign=top align=center>").append(strToShortDate(crs.getString("so_date"))).append("</td>\n");
					// Str.append("<td valign=top align=center>").append(strToShortDate(crs.getString("so_delivered_date"))).append("</td>\n");
					Str.append("<td valign=top align=left>").append(crs.getString("doc_wf_title")).append("</td>\n");
					Str.append("<td valign=top align=left>").append(effective).append("</td>\n");
					Str.append("<td valign=top align=center>");
					if (crs.getString("doc_effective").equals("0")) {
						Str.append("-");
					} else {
						if (crs.getString("doc_effective").equals("1")) {
							duedate = ToShortDate(AddHoursDate(StringToDate(crs.getString("so_date")), crs.getDouble("doc_duedays"), 0, 0));
							Str.append("").append(strToShortDate(duedate)).append("");
						}
						if (crs.getString("doc_effective").equals("2") && !crs.getString("so_delivered_date").equals("")) {
							duedate = ToShortDate(AddHoursDate(StringToDate(crs.getString("so_delivered_date")), crs.getDouble("doc_duedays"), 0, 0));
							Str.append("").append(strToShortDate(duedate)).append("");
						}
						if (crs.getString("doc_effective").equals("2") && crs.getString("so_delivered_date").equals("")) {
							Str.append("-");
						}
					}
					Str.append("</td>");
					Str.append("</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<br><br><br><br><font color=red><b>No Document(s) found!</b></font>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
}
