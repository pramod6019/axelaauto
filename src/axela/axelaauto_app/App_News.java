package axela.axelaauto_app;
//shilpashree 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class App_News extends Connect {

	public String StrSql = "";
	public String CountSql = "";
	public String emp_uuid = "0";
	public String SqlJoin = "";
	public int TotalRecords = 0;
	public String pagecurrent = "";
	public String emp_id = "";
	public String StrHTML = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {

			StrHTML = NewsList(response);

		} catch (Exception ex) {
			SOP(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String NewsList(HttpServletResponse response) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "Select coalesce(concat(branch_name,' (', branch_code, ')'),'All Branch') as branchname, coalesce(branch_name, '') as branch_name, "
					+ " news_id, news_branch_id,news_topic,news_desc,news_date, news_featured, news_active";

			CountSql = " SELECT Count(distinct news_id)";

			SqlJoin = " from axela_news_branch "
					+ " left join axela_branch on branch_id = news_branch_id"
					+ " where 1=1 ";
			StrSql += SqlJoin;

			StrSql = StrSql + " GROUP BY news_id"
					+ " ORDER BY news_id DESC ";
			CountSql += SqlJoin;
			// SOP("StrSql=====" + StrSqlBreaker(StrSql));
			CachedRowSet crs =processQuery(StrSql, 0);
			String Img = "";
			if (crs.isBeforeFirst()) {
				while (crs.next()) {

					Str.append("<div class=\"container\">")
							.append("&nbsp;")
							.append("<div class=\"panel col-md-12\">")
							.append("<b>").append(crs.getString("news_topic")).append("</b><br>")
							.append(strToShortDate(crs.getString("news_date"))).append("<br>")
							.append(crs.getString("news_desc"))
							.append("</div></div>");
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}

		return Str.toString();
	}

	// public void PopulateConfigDetails() {
	// StrSql = "SELECT config_sales_soe, config_sales_sob,"
	// + " config_sales_campaign, config_sales_enquiry_refno"
	// + " from axela_config, axela_emp"
	// + " where emp_id = " + emp_id + "";
	// CachedRowSet crs =processQuery(StrSql, 0);
	// // SOP("strsql==config=="+StrSql);
	// try {
	// while (crs.next()) {
	// config_sales_enquiry_refno = crs.getString("config_sales_enquiry_refno");
	// }
	// crs.close();
	// } catch (Exception ex) {
	// SOPError("Axelaauto== " + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// }
	// }

	// comp_id=1009&emp_uuid=8e29be8b-e4b8-11e3-b497-000c2937aeae
}
