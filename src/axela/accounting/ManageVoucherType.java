package axela.accounting;

/**
 * JEET 11 NOV 2014
 */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class ManageVoucherType extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt;&nbsp <a href=../portal/manager.jsp>Business Manager</a> &gt;&nbsp <a href=managevouchertype.jsp?all=yes>List Voucher Type</a>:";
	public String LinkExportPage = "";
	public String LinkAddPage = "<a href=managevouchertype-update.jsp?add=yes>Add Voucher Type...</a>";
	public String ExportPerm = "";

	public String StrHTML = "";
	public String comp_id = "";
	public String msg = "";
	public String Up = "";
	public String Down = "";
	public String StrSql = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String StrSearch = "";
	public String RecCountDisplay = "";
	public String all = "";
	public String vouchertype_id = "0";
	public String voucherclass_ai = "1";

	public String emp_id = "0";
	public Smart SmartSearch = new Smart();
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Type ID", "numeric", "vouchertype_id"},
			{"Name", "text", "vouchertype_name"},
			{"Base Type", "numeric", "vouchertype_base_type"},
			{"Rank", "numeric", "vouchertype_rank"},
			{"Prefix", "text", "vouchertype_prefix"},
			{"Suffix", "text", "vouchertype_suffix"},
			{"Reference No.", "numeric", "vouchertype_ref_no_enable"},
			{"Entry By", "text", "vouchertype_entry_id in (select emp_id from compdb.axela_emp where emp_name"},
			{"Entry Date", "date", "vouchertype_entry_date"},
			{"Modified By", "text", "vouchertype_modified_id in (select emp_id from compdb.axela_emp where emp_name"},
			{"Modified Date", "date", "vouchertype_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			// CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request) + "");
			CheckPerm(comp_id, "emp_role_id", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id ", request));
				all = PadQuotes(request.getParameter("all"));
				msg = PadQuotes(request.getParameter("msg"));
				Up = PadQuotes(request.getParameter("Up"));
				Down = PadQuotes(request.getParameter("Down"));
				vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch += " AND vouchertype_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all Voucher Type(s)!";
				}

				if (Up.equals("yes")) {
					moveup();
					response.sendRedirect(response.encodeRedirectURL("managevouchertype.jsp?all=yes"));
				}
				if (Down.equals("yes")) {
					movedown();
					response.sendRedirect(response.encodeRedirectURL("managevouchertype.jsp?all=yes"));
				}
				if (!(vouchertype_id.equals("0"))) {
					msg = msg + "<br>Results for Type ID = " + vouchertype_id + "!";
					StrSearch = StrSearch + " AND vouchertype_id =" + vouchertype_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch = StrSearch + " and vouchertype_id = 0";
					} else {
						msg = "Results for Search!";
					}
				}
				StrHTML = Listdata();
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

	public String Listdata() {
		int TotalRecords = 0;
		StringBuilder Str = new StringBuilder();
		if (!msg.equals("")) {
			StrSql = "SELECT vouchertype_id, vouchertype_name, voucherclass_name";
			CountSql = "SELECT COUNT(distinct vouchertype_id) ";
			SqlJoin = " FROM  " + compdb(comp_id) + " axela_acc_voucher_type "
					+ " INNER JOIN axela_acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id"
					+ " where 1=1";
			// + " AND voucherclass_ai =" + voucherclass_ai;

			StrSql = StrSql + SqlJoin;
			CountSql = CountSql + SqlJoin;

			if (!(StrSearch.equals(""))) {
				StrSql = StrSql + StrSearch;
				CountSql = CountSql + StrSearch;
			}
			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
			if (TotalRecords != 0) {
				StrSql = StrSql + " order by vouchertype_rank";
				try {

					// SOP("StrSql======" + StrSql);
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = 0;
					Str.append("<div class=\"table-responsive table-bordered\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-hide=\"phone\">#</th>\n");
					Str.append("<th data-toggle=\"true\">Voucher Type</th>\n");
					Str.append("<th>Voucher Class</th>\n");
					Str.append("<th data-hide=\"phone\" width=20%>Order</th>\n");
					Str.append("<th data-hide=\"phone\" width=20%>Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>\n");
						Str.append("<td valign=top align=center>").append(count).append("</td>\n");
						Str.append("<td valign=top>").append(crs.getString("vouchertype_name")).append("</td>\n");
						Str.append("<td valign=top>").append(crs.getString("voucherclass_name")).append("</td>\n");
						Str.append("<td valign=top align=center><a href=\"managevouchertype.jsp?Up=yes&vouchertype_id=").append(crs.getString("vouchertype_id"))
								.append(" \">Up</a> - <a href=\"managevouchertype.jsp?Down=yes&vouchertype_id=").append(crs.getString("vouchertype_id")).append(" \">Down</a></td>\n");
						Str.append("<td valign=top nowrap><a href=\"managevouchertype-update.jsp?update=yes&vouchertype_id=").append(crs.getString("vouchertype_id"))
								.append(" \">Update Voucher Type</a></td>\n");
						Str.append("</tr>\n");
					}
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
					crs.close();
				} catch (Exception ex) {
					SOPError("Axelaauto===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
					return "";
				}
			} else {
				RecCountDisplay = "<br><br><font color=red><b>No Voucher Type found!</b></font>";
			}
		}
		return Str.toString();
	}

	public void moveup() {
		int tempRank;
		int vouchertype_rank;
		try {
			vouchertype_rank = Integer.parseInt(ExecuteQuery("SELECT vouchertype_rank FROM  " + compdb(comp_id) + "axela_acc_voucher_type  where  vouchertype_id=" + vouchertype_id + ""));
			tempRank = Integer.parseInt(ExecuteQuery("select min(vouchertype_rank) as min1 FROM  " + compdb(comp_id) + "axela_acc_voucher_type  where 1=1"));
			if (vouchertype_rank != tempRank) {
				tempRank = vouchertype_rank - 1;
				StrSql = "UPDATE  " + compdb(comp_id) + " axela_acc_voucher_type  set vouchertype_rank=" + vouchertype_rank + " where vouchertype_rank=" + tempRank + " ";
				updateQuery(StrSql);
				StrSql = "UPDATE  " + compdb(comp_id) + " axela_acc_voucher_type  set vouchertype_rank=" + tempRank + " where vouchertype_rank=" + vouchertype_rank + " and vouchertype_id="
						+ vouchertype_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void movedown() {
		int tempRank;
		int vouchertype_rank;
		try {
			vouchertype_rank = Integer.parseInt(ExecuteQuery("SELECT vouchertype_rank FROM  " + compdb(comp_id) + "axela_acc_voucher_type  where vouchertype_id=" + vouchertype_id + ""));
			tempRank = Integer.parseInt(ExecuteQuery("select max(vouchertype_rank) as max1 FROM  " + compdb(comp_id) + "axela_acc_voucher_type  where 1=1"));
			if (vouchertype_rank != tempRank) {
				tempRank = vouchertype_rank + 1;
				StrSql = "UPDATE  " + compdb(comp_id) + " axela_acc_voucher_type  set vouchertype_rank=" + vouchertype_rank + " where vouchertype_rank=" + tempRank + " ";
				updateQuery(StrSql);
				StrSql = "UPDATE  " + compdb(comp_id) + " axela_acc_voucher_type  set vouchertype_rank=" + tempRank + " where vouchertype_rank=" + vouchertype_rank + " and vouchertype_id="
						+ vouchertype_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
