package axela.portal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class News extends Connect {

	public static String msg = "";
	public String start_time = "";
	public String end_time = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String StrHTML = "";
	public String smart = "";
	public String ExportPerm = "";
	public String EnableSearch = "0";
	public String ListLink = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_news_access", request, response);
			HttpSession session = request.getSession(true);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				StrHTML = NewsSummary(request);
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public String NewsSummary(HttpServletRequest request) {
		String count = "";
		StringBuilder Str = new StringBuilder();
		try {
			// Str.append("<b>News Status<br>");
			Str.append("<table class=\"table table-bordered\">");
			Str.append("<tr>\n");
			Str.append("<th>&nbsp;</th>\n");
			Str.append("<th>Total</th>\n");
			Str.append("<th>Featured</th>\n");
			Str.append("<th>Active</th>\n");
			Str.append("<th>Inactive</th>\n");
			Str.append("</tr>");
			Str.append("<tr>\n");
			Str.append("<td>Head Office News</td>\n");
			count = ExecuteQuery("select count(news_id) from "
					+ compdb(comp_id) + "axela_news_ho where 1=1 ");
			Str.append("<td>" + count + "</td>\n");
			count = ExecuteQuery("select count(news_id) from "
					+ compdb(comp_id)
					+ "axela_news_ho where news_featured = '1'");
			Str.append("<td>" + count + "</td>\n");
			count = ExecuteQuery("select count(news_id) from "
					+ compdb(comp_id) + "axela_news_ho where news_active = '1'");
			Str.append("<td>" + count + "</td>\n");
			count = ExecuteQuery("select count(news_id) from "
					+ compdb(comp_id) + "axela_news_ho where news_active = '0'");
			Str.append("<td>" + count + "</td>\n");
			Str.append("</tr>");
			Str.append("<tr>\n");
			Str.append("<td>Branch News</td>\n");

			count = ExecuteQuery("SELECT COUNT(news_id) FROM "
					+ compdb(comp_id) + "axela_news_branch "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = news_branch_id "
					+ " WHERE 1=1 ");
			Str.append("<td>" + count + "</td>\n");

			count = ExecuteQuery("SELECT COUNT(news_id) FROM "
					+ compdb(comp_id) + "axela_news_branch "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = news_branch_id "
					+ " WHERE news_featured = '1'");
			Str.append("<td>" + count + "</td>\n");

			count = ExecuteQuery("SELECT COUNT(news_id) FROM "
					+ compdb(comp_id) + "axela_news_branch "
					+ " LEFT  JOIN " + compdb(comp_id) + "axela_branch ON branch_id = news_branch_id "
					+ " WHERE news_active = '1'");
			Str.append("<td>" + count + "</td>\n");

			count = ExecuteQuery("SELECT COUNT(news_id) FROM "
					+ compdb(comp_id) + "axela_news_branch "
					+ " LEFT  JOIN " + compdb(comp_id) + "axela_branch ON branch_id = news_branch_id "
					+ " WHERE  news_active = '0'");
			Str.append("<td>" + count + "</td>\n");
			Str.append("</tr>");
			Str.append("</table>");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
	}
}
