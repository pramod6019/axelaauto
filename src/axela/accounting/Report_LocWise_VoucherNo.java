package axela.accounting;
//@shivaprasad 16 Aug 2014

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_LocWise_VoucherNo extends Connect {

	public String StrSql = "";
	public String comp_id = "0";
	public String msg = "";
	public String emp_id = "", branch_id = "0";
	public String StrHTML = "";
	public String go = "";
	public String export = "", StrSearch = "";
	public String filter_brand_id = "", filter_region_id = "", filter_branch_id = "";
	public String[] brand_ids, region_ids, jc_branch_ids;
	public String vouchertype_id = "0", voucher_customer_id = "0", vouchertrans_customer_id = "0";
	public String vouchertype_idarr[] = null;
	public axela.service.MIS_Check1 mischeck = new axela.service.MIS_Check1();
	public axela.accounting.Acc_Check acccheck = new axela.accounting.Acc_Check();
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_mis_access,emp_report_access", request, response);
			if (!comp_id.equals("0")) {
				msg = PadQuotes(request.getParameter("msg"));
				go = PadQuotes(request.getParameter("submit_button"));

				if (go.equals("Go")) {
					GetValues(request, response);
					CheckForm();
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = Listdata(request, response);
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
	}

	protected void CheckForm() {
		msg = "";
		if (branch_id.equals("0")) {
			msg = msg + "<br>Select Branch!";
		}
	}
	public String Listdata(HttpServletRequest request, HttpServletResponse response) {
		String locationid = "0";
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT location_id, location_name, vouchertype_id, vouchertype_name,"
				+ " CONCAT(vouchertype_prefix, max(voucher_no), vouchertype_suffix) as voucherno"
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher_type"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher ON voucher_vouchertype_id = vouchertype_id "
				+ " INNER JOIN  " + compdb(comp_id) + "axela_inventory_location ON location_id = voucher_location_id"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_branch ON branch_id = location_branch_id"
				+ " WHERE 1=1"
				+ " AND location_branch_id =" + branch_id
				+ " AND vouchertype_active = 1"
				+ " GROUP BY vouchertype_id"
				+ " ORDER BY location_name, vouchertype_rank";

		if (!ExecuteQuery(StrSql).equals("")) {
			try {
				CachedRowSet crs = processQuery(StrSql, 0);
				CachedRowSet rscopy = crs.createCopy();
				int count = 0;
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				// Str.append("");
				// Str.append("<th data-toggle=\"true\">#</th>\n");
				// Str.append("<th>").append("Voucher").append("</th>\n");
				// Str.append("</tr>\n");
				while (crs.next()) {
					if (!crs.getString("location_id").equals(locationid)) {
						count++;
						Str.append("<thead><tr>\n");

						Str.append("<th valign=top align=center data-toggle=\"true\">").append(count).append("</th>\n");
						Str.append("<th valign=top align=left>").append(crs.getString("location_name")).append("</th>\n");
						Str.append("<th valign=top align=center data-hide=\"phone\">").append("Current Running No.").append("</th>\n");
						Str.append("</tr>\n");
						Str.append("</thead>\n");
						Str.append("<tbody>\n");
						Str.append(AllocatedVoucherDetails(rscopy, crs.getString("location_id")));
						locationid = crs.getString("location_id");
					}
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
				rscopy.close();
				crs.close();
			} catch (Exception ex) {
				SOP("Report Locationwise Currentrunning No.===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		} else {
			Str.append("<br/><br/><br/><center><font color=red><b>No records found!</b></font></center><br/><br/>");
		}
		return Str.toString();
	}

	public String AllocatedVoucherDetails(CachedRowSet crs, String locationid) {

		int count = 0;
		StringBuilder Str = new StringBuilder();
		try {
			while (crs.next()) {

				if (locationid.equals(crs.getString("location_id"))) {
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center>").append("").append("</td>\n");
					Str.append("<td valign=top align=left>").append(crs.getString("vouchertype_name")).append("</td>\n");
					Str.append("<td valign=top align=left>").append(crs.getString("voucherno")).append("</td>\n");
					Str.append("</tr>\n");
				}
			}
			crs.beforeFirst();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new
					Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}

	}
	public String PopulateBranch() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\">Select</option>\n");
		try {
			StrSql = "SELECT branch_id, branch_name"
					+ " FROM  " + compdb(comp_id) + "axela_branch"
					+ " WHERE 1=1"
					+ " GROUP BY branch_id"
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(
						crs.getString("branch_id"));
				Str.append(StrSelectdrop(crs.getString("branch_id"),
						branch_id));
				Str.append(">").append(crs.getString("branch_name"))
						.append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return Str.toString();
	}

}
