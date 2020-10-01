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

public class Model_Colours_Image_List extends Connect {

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
	public String model_id = "0";
	public String feature_name = "";
	public String comp_id = "0";
	public String colours_id = "0";
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
			model_id = CNumeric(PadQuotes(request.getParameter("model_id")));
			colours_id = CNumeric(PadQuotes(request.getParameter("colours_id")));
			feature_name = ExecuteQuery("select concat(model_name,' (',model_id,')') from " + compdb(comp_id) + "axela_inventory_item_model where model_id=" + model_id + "");
			SOP("model_id----l---" + model_id);
			all = PadQuotes(request.getParameter("all"));

			if ("yes".equals(all)) {
				msg = "Results for all Images";
				StrSearch = StrSearch + " and colours_id > 0";
			}
			// if (!feature_id.equals("0")) {
			// msg += "<br>Results for Feature ID = " + feature_id + "!";
			// StrSearch += " AND img_feature_id = " + feature_id + "";
			// }

			if (!(colours_id.equals("0"))) {
				StrSearch = StrSearch + " and colours_id = " + colours_id + "";
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
		StrSql = "Select colours_id, colours_model_id, colours_title, colours_value ";

		CountSql = "SELECT Count(colours_id)";

		Sqljoin = " from " + compdb(comp_id) + "axela_app_model_colours "
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
			PageURL = "model-colours-image-list.jsp?" + QueryString + "&PageCurrent=";
			PageCount = (TotalRecords / recperpage);
			if ((TotalRecords % recperpage) > 0) {
				PageCount = PageCount + 1;
			}
			PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
			StrSql = StrSql + " order by colours_id desc";
			StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage + "";
			// SOP("StrSql------List-----" + StrSql);
			CachedRowSet crs = processPrepQuery(StrSql, map, 0);
			try {
				int count = StartRec - 1;
				Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				Str.append("<tr align=center>\n");
				Str.append("<th width=5%>#</th>\n");
				Str.append("<th>Images</th>\n");
				Str.append("<th>Actions</th>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					SOP("colours_value ====" + crs.getString("colours_value"));
					if (crs.getString("colours_value").equals("")) {
						SOP("colours_value-------" + crs.getString("colours_value"));
						Img = "";
					} else {
						// Img = "<img src=../Thumbnail.do?featureimg=" + crs.getString("colours_value") + "&width=150&target=" + Math.random() + " border=0 alt=" + crs.getString("img_title") + ">";
						Img = "<img src=../Thumbnail.do?modelcoloursimg=" + crs.getString("colours_value")
								+ "&width=150&target=" + Math.random()
								+ "&alt=" + crs.getString("colours_title") + ">";
					}
					count = count + 1;
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center >").append(count).append("</td>\n");
					Str.append("<td valign=top align=left >").append(Img);
					Str.append("<br> ").append(crs.getString("colours_title")).append("</td>");
					Str.append("<td valign=top align=left nowrap>" + "<a href=model-colours-image-update.jsp?update=yes&colours_id=").append(crs.getInt("colours_id"))
							.append("&model_id=").append(crs.getInt("colours_model_id")).append(">Update Image</a></td>");
					Str.append("</tr>");
					Str.append("</tr>\n");
				}
				crs.close();
				map.clear();
				// stmt.close();
				// conn.close();
				Str.append("</table>\n");
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
