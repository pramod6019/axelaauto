package axela.app;

////////////Divya 5 oct
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Features_Image_List extends Connect {

	public String emp_id = "0";
	public String PageCurrents = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public String Img = "";
	public int recperpage = 0;
	public int PageCurrent = 0;
	public int PageCount = 10;
	public String QueryString = "";
	public String msg = "";
	public String feature_id = "0";
	public String feature_name = "";
	public String comp_id = "0";
	public String img_id = "0";
	public String all = "";
	public String StrSearch = "";
	public String StrHTML = "";
	public String StrSql = "";
	Map<Integer, Object> map = new HashMap<Integer, Object>();
	public int mapkey = 0;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			emp_id = GetSession("emp_id", request) + "";
			recperpage = Integer.parseInt(CNumeric(GetSession("emp_recperpage", request) + ""));
			CheckPerm(comp_id, "emp_role_id", request, response);
			PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
			QueryString = PadQuotes(request.getQueryString());
			msg = PadQuotes(request.getParameter("msg"));
			feature_id = CNumeric(PadQuotes(request.getParameter("feature_id")));
			feature_name = ExecuteQuery("select concat(feature_name,' (',feature_id,')') from " + compdb(comp_id) + "axela_app_model_feature where feature_id=" + feature_id + "");
			img_id = CNumeric(PadQuotes(request.getParameter("img_id")));
			all = PadQuotes(request.getParameter("all"));

			if ("yes".equals(all)) {
				msg = "Results for all Images";
				StrSearch = StrSearch + " and img_id > 0";
			}
			if (!feature_id.equals("0")) {
				msg += "<br>Results for Feature ID = " + feature_id + "!";
				StrSearch += " AND img_feature_id = " + feature_id + "";
			}

			if (!(img_id.equals("0"))) {
				StrSearch = StrSearch + " and img_id = " + img_id + "";
			}
			StrHTML = Listdata();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String Listdata() throws PropertyVetoException, SQLException, IOException {
		StringBuilder Str = new StringBuilder();
		String CountSql = "";
		String Sqljoin = "";
		int TotalRecords = 0;
		int StartRec = 0;
		int EndRec = 0;
		String PageURL = "";
		int PageListSize = 10;

		if ((PageCurrents.equals("0"))) {
			PageCurrents = "1";
		}
		PageCurrent = Integer.parseInt(PageCurrents);
		// to know no of records depending on search
		StrSql = "Select img_id, img_feature_id, img_value, img_title ";

		CountSql = "SELECT Count(img_id)";

		Sqljoin = " from " + compdb(comp_id) + "axela_app_model_feature_img "
				+ " where 1=1 ";

		StrSql = StrSql + Sqljoin;
		CountSql = CountSql + Sqljoin;

		if (!(StrSearch.equals(""))) {
			StrSql = StrSql + StrSearch;
			CountSql = CountSql + StrSearch;
		}
		TotalRecords = Integer.parseInt(ExecutePrepQuery(CountSql, map, 0));
		if (TotalRecords != 0) {
			StartRec = ((PageCurrent - 1) * recperpage) + 1;
			EndRec = ((PageCurrent - 1) * recperpage) + recperpage;

			if (EndRec > TotalRecords) {
				EndRec = TotalRecords;
			}
			RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Image(s)";
			if (QueryString.contains("PageCurrent") == true) {
				QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
			}
			PageURL = "feature-image-list.jsp?" + QueryString + "&PageCurrent=";
			PageCount = (TotalRecords / recperpage);
			if ((TotalRecords % recperpage) > 0) {
				PageCount = PageCount + 1;
			}
			PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
			StrSql = StrSql + " order by img_id desc";
			StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage + "";

			CachedRowSet crs = processPrepQuery(StrSql, map, 0);
			try {
				int count = StartRec - 1;
				Str.append("<div class=\"  table-responsive\">\n");
				Str.append("<table class=\"table table-bordered table-hover   \" data-filter=\"#filter\">\n");
				Str.append("<thead>\n");
				Str.append("<tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>Images</th>\n");
				Str.append("<th>Actions</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					if (crs.getString("img_value").equals("")) {
						Img = "";
					} else {
						// Img = "<img src=../Thumbnail.do?featureimg=" + crs.getString("img_value") + "&width=150&target=" + Math.random() + " border=0 alt=" + crs.getString("img_title") + ">";
						Img = "<img src=../Thumbnail.do?featureimg=" + crs.getString("img_value")
								+ "&width=150"
								+ "&alt=" + crs.getString("img_title") + ">";
					}
					count = count + 1;
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center >").append(count).append("</td>\n");
					Str.append("<td valign=top align=left >").append(Img);
					Str.append("<br> ").append(crs.getString("img_title")).append("</td>");
					Str.append("<td valign=top align=left nowrap>" + "<a href=features-image.jsp?update=yes&img_id=").append(crs.getInt("img_id"))
							.append("&feature_id=").append(crs.getInt("img_feature_id")).append(">Update Image</a></td>");
					Str.append("</tr>");
					Str.append("</tr>\n");
				}
				crs.close();
				map.clear();
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		} else {
			msg = "";
			Str.append("<br><br><br><br><font color=red><b>No Image(s) found!</b></font><br><br>");
		}
		return Str.toString();
	}
}
