////////////sangita
/* Modifiy by Ved Prakash (22th July 2013) */
package axela.preowned;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
//import cloudify.connect.Connect_Pre Owned;

public class Preowned_Dash_Image extends Connect {

	public String msg = "";
	public String all = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String StrHTML = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String QueryString = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String preowned_id = "0";
	public String preowned_title = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				CheckPerm(comp_id, "emp_preowned_access", request, response);
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				preowned_id = CNumeric(PadQuotes(request.getParameter("preowned_id")));
				all = PadQuotes(request.getParameter("all"));

				if ("yes".equals(all)) {
					msg = "Results for all Images";
					StrSearch = StrSearch + " AND img_id > 0";
				}

				StrSql = "SELECT preowned_title FROM " + compdb(comp_id) + "axela_preowned"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = preowned_emp_id"
						+ " WHERE preowned_id = " + preowned_id + BranchAccess + ExeAccess + ""
						+ " GROUP BY preowned_id"
						+ " ORDER BY preowned_id DESC";
				// SOP("StrSql=====" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						preowned_title = crs.getString("preowned_title");
					}
					StrHTML = ListImages();
				} else {
					response.sendRedirect("../portal/error.jsp?msg=Invalid Pre-owned!");
				}
				crs.close();
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String ListImages() {
		StringBuilder Str = new StringBuilder();
		StrSql = " SELECT img_id, img_value, img_title"
				+ " FROM " + compdb(comp_id) + "axela_preowned_img"
				+ " WHERE img_preowned_id = " + preowned_id + "";

		if (!(StrSearch.equals(""))) {
			StrSql += StrSearch;
		}

		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			int count = 0;
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"container-fluid\">\n");
				while (crs.next()) {
					if (count == 4) {
						Str.append("</div>\n");
						Str.append("<div class=\"container-fluid\">\n");
						count = 0;
					}
					Str.append("<div class=\"col-md-3 col-sm-3\">\n");
					if (!crs.getString("img_value").equals("")) {
						File f = new File(PreownedImgPath(comp_id) + crs.getString("img_value"));
						Str.append("<img src=\"../Thumbnail.do?preownedimg=").append(crs.getString("img_value")).append("&width=175\"/></b>");
						Str.append("<br><b>").append(crs.getString("img_title")).append("</b>");
						Str.append("<br>(").append(ConvertFileSizeToBytes(FileSize(f))).append(")<br>");
					} else {
						Str.append("<b>").append(crs.getString("img_title")).append(" (").append(crs.getString("img_id")).append(") (0 Bytes)</b><br>");
					}

					if (!preowned_id.equals("0")) {
						Str.append("<a href=\"../preowned/preowned-dash-image-update.jsp?update=yes&preowned_id=").append(preowned_id);
						Str.append("&img_id=").append(crs.getString("img_id")).append("\">Update Image</a>");
					}
					Str.append("</div>\n");

					count++;
				}
			} else {

				Str.append("<br><br><br><br><br><center><font color=\"red\"><b>No Image found!</b></font></center><br><br><br><br><br>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
}
