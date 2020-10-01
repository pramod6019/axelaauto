package axela.sales;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Campaign_Image_List extends Connect {

	public String img_campaign_id = "0";
	public String img_id = "0";
	public String emp_id = "0";
	public String PageCurrents = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public String Img = "";
	public int recperpage = 0;
	public int PageCurrent = 0;
	public int PageCount = 10;
	public String QueryString = "";
	public String StrHTML = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String msg = "";
	public String comp_id = "0";
	public String all = "";
	public String StrSearch = "";
	public String StrSql = "";
	public String campaign_name = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_campaign_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				all = PadQuotes(request.getParameter("all"));
				msg = PadQuotes(request.getParameter("msg"));
				PageCurrents = PadQuotes(request.getParameter("PageCurrent"));
				QueryString = PadQuotes(request.getQueryString());
				img_campaign_id = CNumeric(PadQuotes(request.getParameter("campaign_id")));
				img_id = CNumeric(PadQuotes(request.getParameter("img_id")));

				if ("yes".equals(all)) {
					msg = "Results for all Images";
					StrSearch = StrSearch + " AND img_id > 0";
				}
				if (!(img_id.equals("0"))) {
					StrSearch = StrSearch + " AND img_id =" + img_id + "";
				}
				if (!(img_campaign_id.equals("0"))) {
					// SOP("==="+img_campaign_id);
					StrSearch = StrSearch + " AND img_campaign_id =" + img_campaign_id + "";
				}
				StrSql = "SELECT campaign_name"
						+ " FROM " + compdb(comp_id) + "axela_sales_campaign"
						+ " WHERE campaign_id = " + img_campaign_id + ""
						+ " GROUP BY campaign_id"
						+ " ORDER BY campaign_id DESC";
				// SOP("StrSql==" + StrSql);
				CachedRowSet rs = processQuery(StrSql, 0);
				if (rs.isBeforeFirst()) {
					while (rs.next()) {
						campaign_name = rs.getString("campaign_name");
					}

					StrHTML = ListImages();

				} else {
					response.sendRedirect("../portal/error.jsp?msg=Invalid Campaign!");
				}
				rs.close();
			}
		} catch (Exception ex) {
			System.out.println("AxelaCRM===" + this.getClass().getName());
			System.out.println("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public String ListImages() {

		StringBuilder Str = new StringBuilder();
		String CountSql = "";
		String Sqljoin = "";
		int TotalRecords = 0;
		int StartRec = 0;
		int EndRec = 0;
		String PageURL = "";
		int PageListSize = 10;

		if ((PageCurrents == null) || (PageCurrents.length() < 1) || isNumeric(PageCurrents) == false) {
			PageCurrents = "1";
		}
		PageCurrent = Integer.parseInt(PageCurrents);

		// to know no of records depending on search
		StrSql = "Select img_id, img_value, img_title, img_campaign_id";

		CountSql = "SELECT Count(img_id)";

		Sqljoin = " FROM " + compdb(comp_id) + "axela_sales_campaign_img "
				+ " WHERE 1 = 1";
		StrSql = StrSql + Sqljoin;
		CountSql = CountSql + Sqljoin;
		if (!(StrSearch.equals(""))) {
			StrSql = StrSql + StrSearch;
			CountSql = CountSql + StrSearch;
			// SOP("StrSql--" + StrSql);
		}

		TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

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
			PageURL = "campaign-image-list.jsp?" + QueryString + "&PageCurrent=";
			PageCount = (TotalRecords / recperpage);
			if ((TotalRecords % recperpage) > 0) {
				PageCount = PageCount + 1;
			}
			PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
			StrSql = StrSql + " ORDER BY img_id desc";
			StrSql = StrSql + " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			try {
				int count = StartRec - 1;
				Str.append("<div class=\"table-responsive\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th data-toggle=\"true\"><span class=\"footable-toggle\"></span>").append("Image").append("s</th>\n");
				Str.append("<th>Title</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					if (crs.getString("img_value").equals("")) {
						Img = "";
					} else {
						Img = "<a href=../Thumbnail.do?campaignimg=" + crs.getString("img_value") + " target=_blank><img src=../Thumbnail.do?campaignimg=" + crs.getString("img_value")
								+ "&width=200 border=0 alt=" + "></a>";
					}
					// SOP("Img===" + Img);
					count = count + 1;
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center >").append(count).append("</td>\n");
					Str.append("<td valign=top align=left >").append(Img).append("</td>\n");
					// Str.append("<td valign=top align=left >").append("<img src=\"../Thumbnail.do?projectimg=").append(rs.getString("img_value")).append("&width=200\"/></b>").append("</td>\n");
					Str.append("<td valign=top align=left >").append(crs.getString("img_title")).append("</td>\n");
					Str.append("<td valign=top align=left nowrap>" + "<a href=campaign-image-update.jsp?update=yes&img_id=").append(crs.getInt("img_id")).append("&campaign_id=")
							.append(crs.getInt("img_campaign_id")).append(">Update Image</a></td>");
					Str.append("</tr>\n");
				}
				crs.close();
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		} else {
			msg = "";
			Str.append("<br><br><br><br><center><font color=red><b>No Image(s) found!</b></font></center><br><br>");
		}

		// SOP("Str/.toString()="+Str.toString());
		return Str.toString();
	}
}
