package axela.sales;

//Shilpashree 06 oct 2015
import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageBrochure extends Connect {

	public String LinkExportPage = "";
	public String StrHTML = "";
	public String msg = "";
	public String StrSql = "";
	public String comp_id = "0";
	// public String QueryString = "";
	public String StrSearch = "";
	public String brochure_id = "0";
	public String emp_id = "0";
	public String BranchAccess = "";
	public String branch_brand_id = "0";
	public String brand_id = "0", rateclass_id = "0";
	public String model_id = "0";
	public String item_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_executive_access,emp_executive_add,emp_executive_edit", request, response);
			if (!comp_id.equals("0")) {
				emp_id = (GetSession("emp_id", request)).toString();
				BranchAccess = GetSession("BranchAccess", request);
				brand_id = CNumeric(PadQuotes(request.getParameter("dr_brand_id")));
				rateclass_id = CNumeric(PadQuotes(request.getParameter("dr_rateclass_id")));
				model_id = CNumeric(PadQuotes(request.getParameter("dr_model")));
				item_id = CNumeric(PadQuotes(request.getParameter("dr_item")));
				msg = PadQuotes(request.getParameter("msg"));
				brochure_id = CNumeric(PadQuotes(request.getParameter("brochure_id")));
				if (!brand_id.equals("0") && !rateclass_id.equals("0")) {
					StrHTML = Listdata();
				} else {
					if (brand_id.equals("0")) {
						StrHTML = "<font color=\"red\"><b>Select Principal!</b></font>";
					}
					if (rateclass_id.equals("0")) {
						StrHTML += "<br><font color=\"red\"><b>Select Rate Class!</b></font>";
					}
				}
			}
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String Listdata() {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT brochure_id, brochure_title, brochure_rateclass_id, brochure_value,"
				+ " brochure_item_id, COALESCE(item_model_id,'0') AS item_model_id,"
				+ " COALESCE(brand_id,'0') AS brand_id,"
				+ " COALESCE(brand_name,'') AS brand_name,"
				+ " COALESCE(rateclass_name,'') AS rateclass_name,"
				+ " COALESCE(item_name,'General') AS item_name,"
				+ " COALESCE(model_name,'General') AS model_name, "
				+ " COALESCE(model_name,'') AS modelorder"
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_brochure"
				+ " LEFT JOIN axela_brand ON brand_id = brochure_brand_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_rate_class ON rateclass_id = brochure_rateclass_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id= brochure_item_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id= item_model_id"
				+ " WHERE brochure_brand_id = " + brand_id + ""
				+ " AND brochure_rateclass_id = " + rateclass_id;

		if (model_id.equals("0") && item_id.equals("0")) {
			StrSql = StrSql + " AND (brochure_item_id=0)";
		} else if (!model_id.equals("0") && item_id.equals("0")) {
			StrSql = StrSql + " AND (model_id = " + model_id + ")";
		} else if (!model_id.equals("0") && !item_id.equals("0")) {
			StrSql = StrSql + " AND (brochure_item_id = " + item_id + ") ";
		} else if (model_id.equals("0") && !item_id.equals("0")) {
			StrSql = StrSql + " AND (brochure_item_id =0)";
		}

		if (!(StrSearch.equals(""))) {
			StrSql = StrSql + StrSearch;
		}
		StrSql = StrSql + " GROUP BY brochure_id"
				+ " ORDER BY modelorder, item_name, brochure_title";
		try {
			// SOP("StrSql----------list----" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("<div class=\"table-responsive\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\" width=5%>#</th>\n");
				Str.append("<th>Principal</th>\n");
				Str.append("<th>Rate Class</th>\n");
				Str.append("<th data-hide=\"phone\">Model</th>\n");
				Str.append("<th data-hide=\"phone\">Variant</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Brochure</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" width=20%>Actions</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count = count + 1;
					Str.append("<tr>\n<td valign=top align=center>").append(count).append("</td>\n");
					Str.append("<td valign=top>").append(crs.getString("brand_name")).append("</td>\n");
					Str.append("<td valign=top>").append(crs.getString("rateclass_name")).append("</td>\n");
					Str.append("<td valign=top>").append(crs.getString("model_name")).append("</td>\n");
					Str.append("<td valign=top>").append(crs.getString("item_name")).append("</td>\n");
					if (!crs.getString("brochure_value").equals("")) {
						if (!new File(EnquiryBrochurePath(comp_id)).exists()) {
							new File(EnquiryBrochurePath(comp_id)).mkdirs();
						}
						File f = new File(EnquiryBrochurePath(comp_id) + crs.getString("brochure_value"));
						// SOP("file==="+f);
						Str.append("<td valign=top align=left ><a href=../Fetchdocs.do?" + "enquiry_brochure_id=");
						Str.append(crs.getString("brochure_id")).append("><b>").append(crs.getString("brochure_title"));
						Str.append("</a></b> (").append(ConvertFileSizeToBytes(FileSize(f))).append(")<br> ");
						Str.append("</td>\n");
					}

					Str.append("<td valign=top nowrap><a href=\"../portal/docs-update.jsp?update=yes&brand_id=").append(crs.getString("brand_id")).append("&brochure_id=")
							.append(crs.getString("brochure_id"));
					Str.append("&brochure_rateclass_id=").append(crs.getString("brochure_rateclass_id"));
					Str.append("&dr_model=").append(crs.getString("item_model_id"));
					Str.append("&dr_item=").append(crs.getString("brochure_item_id")).append(" \">Update Brochure</a></td>\n");
				}
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<br><br><font color=red><b>No Brochure Found!</b></font>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulatePrincipal() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT brand_id, brand_name"
					+ " FROM axela_brand"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE 1=1 " + BranchAccess
					+ " GROUP BY brand_id"
					+ " ORDER BY brand_name ";

			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=\"0\" ").append(StrSelectdrop("0", brand_id)).append(">Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("brand_id")).append("");
				Str.append(StrSelectdrop(crs.getString("brand_id"), brand_id));
				Str.append(">").append(crs.getString("brand_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}

	public String PopulateRateClass() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT rateclass_id, rateclass_name"
					+ " FROM " + compdb(comp_id) + "axela_rate_class"
					+ " WHERE 1=1 "
					+ " ORDER BY rateclass_name ";

			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=\"0\" ").append(StrSelectdrop("0", rateclass_id)).append(">Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("rateclass_id")).append("");
				Str.append(StrSelectdrop(crs.getString("rateclass_id"), rateclass_id));
				Str.append(">").append(crs.getString("rateclass_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}

	public String PopulateModel() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT model_id, CONCAT(brand_name,' - ',model_name) as model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = model_brand_id"
					+ " INNER JOIN axela_brand ON brand_id = model_brand_id"
					+ " WHERE 1=1 "
					+ " AND model_active = '1'"
					+ " AND model_brand_id = " + brand_id
					+ " GROUP BY model_id"
					+ " ORDER BY brand_name, model_name";

			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=\"0\" ").append(StrSelectdrop("0", model_id)).append(">Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id")).append("");
				Str.append(StrSelectdrop(crs.getString("model_id"), model_id));
				Str.append(">").append(crs.getString("model_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}

	public String PopulateItem() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT item_id, item_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " WHERE 1=1 AND item_active = '1' AND item_type_id = 1"
					+ " AND item_model_id = " + model_id + ""
					+ " GROUP BY item_id"
					+ " ORDER BY item_name";
			// SOP("StrSql==="+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=\"0\">Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("item_id")).append("");
				Str.append(StrSelectdrop(crs.getString("item_id"), item_id));
				Str.append(">").append(crs.getString("item_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
