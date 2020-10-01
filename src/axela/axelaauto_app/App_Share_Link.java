package axela.axelaauto_app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class App_Share_Link extends Connect {

	public String StrSql = "";
	public String imgdatacat_model_id = "0";
	public String imgdatacat_type_id = "0";
	public String imgdatacat_id = "0";
	public String StrHTML = "";
	public String model_name="",imgdatacat_name="",type="";
	CachedRowSet crs =null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
				imgdatacat_model_id = CNumeric(PadQuotes(request.getParameter("model_id")));
				imgdatacat_type_id = CNumeric(PadQuotes(request.getParameter("type_id")));
				imgdatacat_id = CNumeric(PadQuotes(request.getParameter("cat_id")));
				if (!imgdatacat_model_id.equals("0") && !imgdatacat_type_id.equals("0") && !imgdatacat_id.equals("0")) {
				if(imgdatacat_type_id.equals("1"))
				{
					type="Exterior";
				}
				else
				{
					type="Interior";
				}
				StrHTML = ListData(imgdatacat_model_id, imgdatacat_type_id, imgdatacat_id,request,response);
			}
		} catch (Exception ex) {
			SOPError("Silverarrows-App===" + this.getClass().getName());
			SOPError("Silverarrows-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String ListData(String imgdatacat_model_id,
			String imgdatacat_type_id, String imgdatacat_id,HttpServletRequest request, HttpServletResponse response) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT imgdata_id, CONCAT(model_name,'-',IF(imgdatacat_type_id=1,'Exterior','Interior'),'-',"
					+ " imgdatacat_name,'-',imgdata_title) AS imgtitle, model_name, imgdatacat_name,"
					+ " imgdata_img_value"
					+ " FROM axela_imgdata"
					+ " INNER JOIN axela_imgdata_cat ON imgdatacat_id = imgdata_imgdatacat_id "
					+ " INNER JOIN axela_inventory_item_model ON model_id = imgdatacat_model_id"
					+ " WHERE 1=1"
					+ " AND imgdatacat_model_id = " + imgdatacat_model_id
					+ " AND imgdatacat_type_id = " + imgdatacat_type_id
					+ " AND imgdatacat_id = " + imgdatacat_id
					+ " AND model_active = '1' "
					+ " AND model_sales = '1'"
					+ " AND imgdatacat_active = 1"
					+ " AND imgdata_img_value !=''"
					//+ " AND imgdata_active = 1"
					+ " GROUP BY imgdata_id" 
					+ " ORDER BY imgtitle";
	//SOP("STrSql==ListData-------------=" + StrSqlBreaker(StrSql));
			CachedRowSet crs =processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					model_name=crs.getString("model_name");
					imgdatacat_name=crs.getString("imgdatacat_name");
					if (!crs.getString("imgdata_img_value").equals("")) {
					   Str.append("<div class=\"row\">")
					   .append("<div class=\"col-md-12\">").append("<br>")
					   .append("<center>")
		               .append("<img src=\"../ThumbnailAxelaApp.do?carimg=" + crs.getString("imgdata_img_value") + "\" class=\"img-responsive\" data-toggle=\"modal\" href=\"#modal-" + crs.getString("imgdata_id") + "\">")
		               .append("</center>");
					    Str.append("<div id=\"modal-" + crs.getString("imgdata_id") + "\" class=\"modal fade\" tabindex=\"-1\" aria-hidden=\"true\">")
					   .append("<div class=\"modal-dialog\">")
					   .append("<div class=\"modal-body\">")
					   .append("<div class=\"scroller\" style=\"height: 300px\" data-always-visible=\"1\" data-rail-visible1=\"1\">")
					   .append("<div class=\"row\">")
					   .append("<div class=\"col-md-12\">")
					   .append("<center>")
					   .append("<img src=\"../ThumbnailAxelaApp.do?carimg=" + crs.getString("imgdata_img_value") +"\" class=\"img-responsive\">")
					   .append("<center>")
					   .append("</div>")
					   .append("</div>")
					   .append("</div>")
					   .append("</div>")
					   .append("</div>")
					   .append("</div>");
					    Str.append("</div>")
					   .append("</div><br>");
					}
				}
			} else {
				  Str.append("NO Image Found");	
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Silverarrows-App===" + this.getClass().getName());
			SOPError("Silverarrows-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		imgdatacat_model_id = CNumeric(PadQuotes(request.getParameter("dr_imgdatacat_model_id")));
		imgdatacat_type_id = CNumeric(PadQuotes(request.getParameter("dr_imgdatacat_type_id")));
		imgdatacat_id = CNumeric(PadQuotes(request.getParameter("dr_cat_id")));

	}

}
