package axela.axelaauto_app;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class App_Preowned_Dash_Image extends Connect {
	public int i = 0;
	public String StrSql = "";
	public String SqlJoin = "";
	public String CountSql = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String StrSearch = "";

	public String emp_uuid = "0";
	public String emp_id = "0";
	public String preowned_id = "0";
	public String branch_id = "";
	public String enquiry_id = "0";
	public String StrHTML = "", msg = "";
	public String comp_id = "0";
	public String emp_all_exe = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(PadQuotes(request.getParameter("comp_id")));
		emp_uuid = PadQuotes(request.getParameter("emp_uuid"));
		if (!CNumeric(GetSession("emp_id", request) + "").equals("0") && !emp_uuid.equals("")) {
			if (ExecuteQuery("SELECT emp_id"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_id=" + CNumeric(GetSession("emp_id", request) + "") + ""
					+ " AND emp_uuid='" + emp_uuid + "' ").equals(""))
			{
				session.setAttribute("emp_id", "0");
				session.setAttribute("sessionMap", null);
			}
		}
		SetSession("comp_id", comp_id, request);
		CheckAppSession(emp_uuid, comp_id, request);
		emp_id = CNumeric(session.getAttribute("emp_id") + "");
		if (!emp_id.equals("0")) {
			try {
				preowned_id = CNumeric(PadQuotes(request.getParameter("preowned_id")));
				StrHTML = ListImg(comp_id);
			} catch (Exception ex) {
				SOPError("Axelaauto-App===" + this.getClass().getName());
				SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String ListImg(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT img_id, img_value, img_title"
					+ " FROM " + compdb(comp_id) + "axela_preowned_img"
					+ " WHERE img_preowned_id = " + preowned_id + "";
			// SOP("StrSql--------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<center>");
					if (!crs.getString("img_value").equals("")) {
						File f = new File(PreownedImgPath(comp_id) + crs.getString("img_value"));
						Str.append("<a href=\"imgpopup" + crs.getString("img_value") + "\"><img class=\"img-rounded img-responsive\" src=\"../Thumbnail.do?preownedimg=")
								.append(crs.getString("img_value"))
								.append("&width=250\" height=\"200\" width=\"250\" style=\"border:3px solid black\"/></a></b>");
						Str.append("<b>").append(crs.getString("img_title")).append("</b>&nbsp&nbsp");
						Str.append("(").append(ConvertFileSizeToBytes(FileSize(f))).append(")<br><br><br>");
					} else {
						Str.append("<b>").append(crs.getString("img_title")).append("(").append(crs.getString("img_id")).append(")(0 Bytes)</b><br>");
					}
					Str.append("</div>\n");
					Str.append("</center>");
				}
			} else {
				Str.append("<div class=\"container\" align=\"center\"><b><h4>&nbsp;</h4>\n").append("No Image(s) Found!").append("</b></div>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
