// smitha nag june 6 2013
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

public class Report_VoucherNo extends Connect {

	public String StrHTML = "";

	public String msg = "";
	public String StrSql = "";
	public String LinkExportPage = "";
	public String ExportPerm = "";
	public String company_id = "0";
	public String year_id = "0";
	public String start_date = "";
	public String end_date = "";
	public String from_no = "";
	public String to_no = "";
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

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_mis_access,emp_report_access", request, response);
			HttpSession session = request.getSession(true);
			company_id = CNumeric(GetSession("company_id", request) + "");
			year_id = CNumeric(GetSession("year_id", request) + "");
			ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
			go = PadQuotes(request.getParameter("submit_button"));
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
		from_no = PadQuotes(request.getParameter("txt_from_no"));
		to_no = PadQuotes(request.getParameter("txt_to_no"));
		voucher_branch_id = PadQuotes(request.getParameter("voucher_branch_id"));
		vouchertype_id = CNumeric(PadQuotes(request
				.getParameter("dr_voucher_type")));
		orderby = CNumeric(PadQuotes(request
				.getParameter("dr_order_by")));

	}
	protected void CheckForm() {
		msg = "";
		if (from_no.equals("")) {
			msg += "<br> Enter From No.!";
		}
		if (to_no.equals("")) {
			msg += "<br> Enter To No.!";
		}
		if (vouchertype_id.equals("0")) {
			msg += "<br>Select Voucher TYpe!";
		}
		if (voucher_branch_id.equals("0")) {
			msg += "<br>Select Branch!";
		}

	}

	public String Listdata() {
		StringBuilder Str = new StringBuilder();
		StrSql = " SELECT vouchertype_prefix, vouchertype_suffix"
				+ " FROM " + compdb(comp_id) + " axela_acc_voucher_type"
				+ " WHERE vouchertype_id =" + vouchertype_id;
		CachedRowSet rs1 = processQuery(StrSql, 0);
		try {
			while (rs1.next()) {
				prefix = rs1.getString("vouchertype_prefix");
				suffix = rs1.getString("vouchertype_suffix");
			}
			rs1.close();

			StrSql = "SELECT series_id"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher_series"
					+ " WHERE"
					+ " series_id NOT IN ("
					+ " SELECT voucher_no"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
					+ " WHERE"
					+ " voucher_no BETWEEN " + from_no + " AND " + to_no + ""
					+ " AND voucher_branch_id = " + voucher_branch_id
					+ " AND voucher_vouchertype_id = " + vouchertype_id
					+ " )"
					+ " AND series_id > " + from_no + ""
					+ " AND series_id < " + to_no;
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
				// if (orderby.equals("1")) {
				Str.append("<th align=left>Missing Voucher No</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				Str.append("</div></div>");
				rs.beforeFirst();
				while (rs.next()) {
					count++;
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center>").append(count)
							.append("</td>\n");
					if (orderby.equals("1")) {
						Str.append("<td valign=top align=left>").append(prefix
								+ rs.getString("series_id") + suffix).append("</td>\n");
					} else {
						Str.append("<td valign=top align=left>").append(
								rs.getString("series_id")).append("</td>\n");
					}

				}
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

	public String PopulateVoucherType() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\">Select</option>\n");
		try {
			StrSql = "SELECT vouchertype_id, vouchertype_name"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher_type"
					+ " ORDER BY vouchertype_name";
			CachedRowSet rs = processQuery(StrSql, 0);

			while (rs.next()) {
				Str.append("<option value=").append(
						rs.getString("vouchertype_id"));
				Str.append(StrSelectdrop(rs.getString("vouchertype_id"),
						vouchertype_id));
				Str.append(">").append(rs.getString("vouchertype_name"))
						.append("</option>\n");
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
	public String PopulateBranch() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\">Select</option>\n");
		try {
			StrSql = "SELECT DISTINCT voucher_branch_id, branch_name "
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher "
					+ "INNER JOIN " + compdb(comp_id) + " axela_branch On voucher_branch_id=branch_id"
					+ " ORDER BY branch_name";
			CachedRowSet rs = processQuery(StrSql, 0);
			while (rs.next()) {
				Str.append("<option value=").append(rs.getString("voucher_branch_id"));
				Str.append(StrSelectdrop(rs.getString("voucher_branch_id"), voucher_branch_id));
				Str.append(">").append(rs.getString("branch_name")).append("</option>\n");
			}
			rs.close();
		} catch (Exception ex) {
			System.out.println("Axala Auto======" + this.getClass().getName());
			System.out.println("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}

		return Str.toString();
	}
	public String PopulateOrderBy() {
		StringBuilder Str = new StringBuilder();
		// Str.append("<option value=\"0\">Select</option>\n");
		try {
			// StrSql = "SELECT vouchertype_id, vouchertype_name"
			// + " FROM  axela_acc_voucher_type"
			// + " ORDER BY vouchertype_name";
			// CachedRowSet rs = processQuery(StrSql, 0);

			// while (rs.next()) {
			Str.append("<option value=").append("1");
			Str.append(StrSelectdrop("1",
					orderby));
			Str.append(">").append("Voucher No.")
					.append("</option>\n");

			Str.append("<option value=").append("2");
			Str.append(StrSelectdrop("2",
					orderby));
			Str.append(">").append("Ref. No.")
					.append("</option>\n");
			// }
			// rs.close();
		} catch (Exception ex) {
			System.out.println("Axala Auto===" + this.getClass().getName());
			System.out.println("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return Str.toString();
	}

}
