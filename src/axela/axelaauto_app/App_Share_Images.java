package axela.axelaauto_app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class App_Share_Images extends Connect {

	public String addB = "";
	public String StrSql = "";
	public String msg = "";
	public String enquiry_id = "0";
	public String model_id = "0";
	public String imgdatacat_model_id = "0";
	public String imgdatacat_type_id = "0";
	public String imgdatacat_id = "0";
	public String modelname = "";
	public String img = "";
	public String StrHTML = "";
	public String emp_id = "0";
	public String imgdata = "", share = "";
	public String emp_uuid = "";
	public String comp_id="0";
	CachedRowSet crs =null;
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {            

		HttpSession session = request.getSession(true);
		comp_id = CNumeric(PadQuotes(request.getParameter("comp_id")));
		emp_uuid = PadQuotes(request.getParameter("emp_uuid"));
		SOP("emp_uuid----"+emp_uuid);
		CheckAppSession(emp_uuid, comp_id, request);
		emp_id = CNumeric(session.getAttribute("emp_id") + "");  
			if (!emp_id.equals("0")) {
				addB = PadQuotes(request.getParameter("add_button"));
				msg = PadQuotes(request.getParameter("msg"));
				enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
				imgdatacat_model_id = CNumeric(PadQuotes(request.getParameter("imgdatacat_model_id")));
				imgdatacat_type_id = CNumeric(PadQuotes(request.getParameter("imgdatacat_type_id")));
				imgdatacat_id = CNumeric(PadQuotes(request.getParameter("imgdatacat_id")));
				share = PadQuotes(request.getParameter("share"));
			}
		} catch (Exception ex) {
			SOPError("Silverarrows-App=== " + this.getClass().getName());
			SOPError("Silverarrows-App=== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	
	public String ListData(String imgdatacat_model_id, String imgdatacat_type_id, String imgdatacat_id) {
		try {
			StrSql = "SELECT CONCAT(model_name,'-',if(imgdatacat_type_id=1,'Exterior','Interior'),'-',"
					+ " imgdatacat_name,'-',imgdata_title) as imgtitle,"
					+ " imgdata_img_value"
					+ " FROM axela_imgdata"
					+ " INNER JOIN axela_imgdata_cat on imgdatacat_id = imgdata_imgdatacat_id "
					+ " INNER JOIN axela_inventory_item_model on model_id = imgdatacat_model_id"
					+ " WHERE 1=1"
					+ " AND imgdatacat_model_id = " + imgdatacat_model_id
					+ " AND imgdatacat_type_id = " + imgdatacat_type_id
					+ " AND imgdatacat_id = " + imgdatacat_id
					+ " AND model_active = '1' "
					+ " AND model_sales = '1'"
					+ " AND imgdatacat_active = 1"
					+ " AND imgdata_img_value !=''"
				//	+ " AND imgdata_active = 1"
					+ " GROUP BY imgdata_id"
					+ " ORDER BY imgtitle";
//			 SOP("STrSql==ListData-------------=" + StrSqlBreaker(StrSql));
			CachedRowSet crs =processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					if (!crs.getString("imgdata_img_value").equals("")) {
						imgdata += AppURL() + "ThumbnailAxelaApp.do?categoryimg=" + crs.getString("imgdata_img_value") + "&width=800" + "$";
					}
				}
			}
			else {
				imgdata = "No Images Found!";
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Silverarrows-App===" + this.getClass().getName());
			SOPError("Silverarrows-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return imgdata;
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String PopulateModel(String imgdatacat_model_id) {
		StringBuilder Str = new StringBuilder();
SOP("imgdatacat_model_id----"+imgdatacat_model_id);
		try {
			StrSql = "SELECT model_id, model_name"
					+ " FROM axela_inventory_item_model"
					+ " INNER JOIN axela_imgdata_cat on imgdatacat_model_id = model_id"
					+ " INNER JOIN axela_imgdata on imgdata_imgdatacat_id = imgdatacat_id"
					+ " WHERE model_active = '1' AND model_sales = '1'"
					+ " GROUP BY model_id"
					+ " ORDER BY model_name";
		SOPError("PopulateModel SQL------" + StrSql);
			CachedRowSet crs =processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id")).append("");
				Str.append(StrSelectdrop(crs.getString("model_id"), imgdatacat_model_id));
				Str.append(">").append(crs.getString("model_name")).append("</option>\n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Silverarrows-App=== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();

	}

	public String PopulateType() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>\n");
		Str.append("<option value=1").append(StrSelectdrop("1", imgdatacat_type_id)).append(">Exterior</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", imgdatacat_type_id)).append(">Interior</option>\n");

		return Str.toString();
	}

	public String PopulateCategory(String imgdatacat_model_id, String imgdatacat_type_id) {
		StringBuilder Str = new StringBuilder();

		try {
			StrSql = " SELECT  imgdatacat_id, imgdatacat_name "
					+ " FROM axela_imgdata_cat"
					+ " INNER JOIN axela_inventory_item_model ON model_id = imgdatacat_model_id"
				    + " INNER JOIN axela_imgdata ON imgdata_imgdatacat_id = imgdatacat_id"
					+ " WHERE imgdatacat_model_id = " + imgdatacat_model_id
					+ " AND imgdatacat_type_id = " + imgdatacat_type_id
					+ " AND model_active = '1' "
					+ " AND model_sales = '1'"
					+ " AND imgdatacat_active = 1"
					+ " GROUP BY imgdatacat_id"
					+ " ORDER BY imgdatacat_name";
			//SOP("PopulateCategory----------" + StrSqlBreaker(StrSql));
			CachedRowSet crs =processQuery(StrSql, 0);
			Str.append("<select name=\"dr_cat_id\" id=\"dr_cat_id\" class=\"form-control\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("imgdatacat_id")).append("");
				Str.append(StrSelectdrop(crs.getString("imgdatacat_id"), imgdatacat_id));
				Str.append(">").append(crs.getString("imgdatacat_name")).append("</option>\n");
			}
			Str.append("</select>");
			crs.close();

		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Silverarrows-App====" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();

	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		imgdatacat_model_id = CNumeric(PadQuotes(request.getParameter("dr_imgdatacat_model_id")));
		imgdatacat_type_id = CNumeric(PadQuotes(request.getParameter("dr_imgdatacat_type_id")));
		imgdatacat_id = CNumeric(PadQuotes(request.getParameter("dr_cat_id")));

	}

}
