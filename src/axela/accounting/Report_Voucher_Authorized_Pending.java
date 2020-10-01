package axela.accounting;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Voucher_Authorized_Pending extends Connect {

	public String StrHTML = "";

	public String msg = "";
	public String StrSql = "";
	public String LinkExportPage = "";
	public String ExportPerm = "";
	public String company_id = "0";
	public String prefix = "";
	public String suffix = "";
	public String orderby = "";
	public String vouchertype_id = "102";
	public String go = "";
	public String comp_id = "";
	public String voucher_branch_id = "0";
	DecimalFormat df = new DecimalFormat("0.00");
	Map<Integer, Object> prepmap = new HashMap<>();
	public int prepkey = 1;
	public String BranchAccess = "";
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_mis_access,emp_report_access", request, response);
			HttpSession session = request.getSession(true);
			company_id = CNumeric(GetSession("company_id", request) + "");
			go = PadQuotes(request.getParameter("submit_button"));
			BranchAccess = GetSession("BranchAccess", request) + "";
			if (go.equals("Go")) {
				GetValues(request, response);
				CheckForm();
				if (msg.equals("")) {
					StrHTML = Listdata();
				}
			}
		} catch (Exception ex) {
			System.out.println(" AxA Pro===" + this.getClass().getName());
			System.out.println("Error in "
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

		voucher_branch_id = PadQuotes(request.getParameter("voucher_branch_id"));

	}
	protected void CheckForm() {
		msg = "";
		if (voucher_branch_id.equals("0")) {
			msg += "<br>Select Branch!";
		}

	}

	public String Listdata() {
		StringBuilder Str = new StringBuilder();
		try {

			StrSql = "SELECT"
					+ " vouchertype_name, vouchertype_id, voucherclass_id,"
					+ " SUM(IF(DATEDIFF( CURRENT_DATE(),voucher_date) = 1, 1, 0)) AS '1days',"
					+ " SUM(IF(DATEDIFF( CURRENT_DATE(),voucher_date) = 2, 1, 0)) AS '2days',"
					+ " SUM(IF(DATEDIFF( CURRENT_DATE(),voucher_date) = 3, 1, 0)) AS '3days',"
					+ " SUM(IF(DATEDIFF( CURRENT_DATE(),voucher_date) = 4, 1, 0)) AS '4days',"
					+ " SUM(IF(DATEDIFF( CURRENT_DATE(),voucher_date) = 5, 1, 0)) AS '5days',"
					+ " SUM(IF(DATEDIFF( CURRENT_DATE(),voucher_date) >= 6 AND DATEDIFF( CURRENT_DATE(),voucher_date) <= 15, 1, 0)) AS '6-15days',"
					+ " SUM(IF(DATEDIFF( CURRENT_DATE(),voucher_date) >= 16 AND DATEDIFF( CURRENT_DATE(),voucher_date) <= 30, 1, 0)) AS '16-30days',"
					+ " SUM(IF(DATEDIFF( CURRENT_DATE(),voucher_date) > 30, 1, 0)) AS '>30days'"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
					+ " INNER JOIN axela_acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
					+ " WHERE"
					+ " 1 = 1"
					+ " AND voucher_active = 1"
					+ " AND voucher_authorize = 0"
					+ " AND voucher_branch_id = " + voucher_branch_id
					+ BranchAccess
					+ " AND (vouchertype_defaultauthorize =1 OR vouchertype_authorize = 1)"
					+ " GROUP BY voucher_vouchertype_id"
					+ " ORDER BY voucher_vouchertype_id,DATEDIFF( CURRENT_DATE(),voucher_date)";

			CachedRowSet rs = processQuery(StrSql, 0);

			int count = 0;
			if (rs.isBeforeFirst()) {
				Str.append("<div class=\"container-fluid portlet box\">");
				Str.append("<div class=\"container-fluid\">\n");
				Str.append("<div class=\"table-responsive\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<tr>\n");
				Str.append("<th>#</th>\n");
				Str.append("<th align=left>vouchertype_name</th>\n");
				Str.append("<th align=left>1days</th>\n");
				Str.append("<th align=left>2days</th>\n");
				Str.append("<th align=left>3days</th>\n");
				Str.append("<th align=left>4days</th>\n");
				Str.append("<th align=left>5days</th>\n");
				Str.append("<th align=left>6-15days</th>\n");
				Str.append("<th align=left>16-30days</th>\n");
				Str.append("<th align=left>>30days</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				Str.append("</div></div>");
				rs.beforeFirst();
				while (rs.next()) {
					count++;
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center>").append(count).append("</td>\n");
					Str.append("<td valign=top align=left>").append(rs.getString("vouchertype_name")).append("</td>\n");

					Str.append("<td valign=top align=right><a href=../accounting/voucher-list.jsp?"
							+ "all=yes&authorizepending_branch_id=" + voucher_branch_id
							+ "&voucherclass_id=" + rs.getString("voucherclass_id")
							+ "&vouchertype_id=" + rs.getString("vouchertype_id")
							+ "&authorized_pending=1 target=_blank>").append(rs.getString("1days")).append("</a></td>\n");
					Str.append("<td valign=top align=right><a href=../accounting/voucher-list.jsp?"
							+ "all=yes&authorizepending_branch_id=" + voucher_branch_id
							+ "&voucherclass_id=" + rs.getString("voucherclass_id")
							+ "&vouchertype_id=" + rs.getString("vouchertype_id")
							+ "&authorized_pending=2 target=_blank>").append(rs.getString("2days")).append("</a></td>\n");
					Str.append("<td valign=top align=right><a href=../accounting/voucher-list.jsp?"
							+ "all=yes&authorizepending_branch_id=" + voucher_branch_id
							+ "&voucherclass_id=" + rs.getString("voucherclass_id")
							+ "&vouchertype_id=" + rs.getString("vouchertype_id")
							+ "&authorized_pending=3 target=_blank>").append(rs.getString("3days")).append("</a></td>\n");
					Str.append("<td valign=top align=right><a href=../accounting/voucher-list.jsp?"
							+ "all=yes&authorizepending_branch_id=" + voucher_branch_id
							+ "&voucherclass_id=" + rs.getString("voucherclass_id")
							+ "&vouchertype_id=" + rs.getString("vouchertype_id")
							+ "&authorized_pending=4 target=_blank>").append(rs.getString("4days")).append("</a></td>\n");
					Str.append("<td valign=top align=right><a href=../accounting/voucher-list.jsp?"
							+ "all=yes&authorizepending_branch_id=" + voucher_branch_id
							+ "&voucherclass_id=" + rs.getString("voucherclass_id")
							+ "&vouchertype_id=" + rs.getString("vouchertype_id")
							+ "&authorized_pending=5 target=_blank>").append(rs.getString("5days")).append("</a></td>\n");
					Str.append("<td valign=top align=right><a href=../accounting/voucher-list.jsp?"
							+ "all=yes&authorizepending_branch_id=" + voucher_branch_id
							+ "&voucherclass_id=" + rs.getString("voucherclass_id")
							+ "&vouchertype_id=" + rs.getString("vouchertype_id")
							+ "&authorized_pending=6-15 target=_blank>").append(rs.getString("6-15days")).append("</a></td>\n");
					Str.append("<td valign=top align=right><a href=../accounting/voucher-list.jsp?"
							+ "all=yes&authorizepending_branch_id=" + voucher_branch_id
							+ "&voucherclass_id=" + rs.getString("voucherclass_id")
							+ "&vouchertype_id=" + rs.getString("vouchertype_id")
							+ "&authorized_pending=16-30 target=_blank>").append(rs.getString("16-30days")).append("</a></td>\n");
					Str.append("<td valign=top align=right><a href=../accounting/voucher-list.jsp?"
							+ "all=yes&authorizepending_branch_id=" + voucher_branch_id
							+ "&voucherclass_id=" + rs.getString("voucherclass_id")
							+ "&vouchertype_id=" + rs.getString("vouchertype_id")
							+ "&authorized_pending=30 target=_blank>").append(rs.getString(">30days")).append("</a></td>\n");
				}
				Str.append("</tr>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<br><br><br><font color=red><b><center>No Record(s) Found!</center></b></font><br><br>");
			}
		} catch (Exception ex) {
			System.out.println(" Axala Auto===" + this.getClass().getName());
			System.out.println("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateBranch() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\">Select</option>\n");
		try {
			StrSql = "SELECT DISTINCT voucher_branch_id, branch_name"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ "	INNER JOIN " + compdb(comp_id) + " axela_branch On voucher_branch_id=branch_id"
					+ " ORDER BY branch_name";
			CachedRowSet rs = processQuery(StrSql, 0);
			while (rs.next()) {
				Str.append("<option value=").append(rs.getString("voucher_branch_id"));
				Str.append(StrSelectdrop(rs.getString("voucher_branch_id"), voucher_branch_id));
				Str.append(">").append(rs.getString("branch_name")).append("</option>\n");
			}
			rs.close();
		} catch (Exception ex) {
			System.out.println("Axala Auto===" + this.getClass().getName());
			System.out.println("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}

		return Str.toString();
	}

}
