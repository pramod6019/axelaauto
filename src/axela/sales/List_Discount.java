package axela.sales;
// Dilip Kumar 09 APR 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class List_Discount extends Connect {
	// ///// List page links

	public String LinkHeader = "<a href=\"../sales/home.jsp\">Home</a>"
			+ " &gt; <a href=\"../sales/index.jsp\">Sales</a>"
			+ " &gt; <a href=\"../sales/list-discount.jsp?all=yes\">List Discount</a><b>:</b>";
	public String LinkAddPage = "<a href=\"../sales/update-discount.jsp?add=yes\">Add New Discount</a>";
	public String LinkExportPage = "";
	public String LinkListPage = "../sales/list-discount.jsp?all=yes";
	public String ExportPerm = "";
	public String msg = "";
	public String StrHTML = "";
	public String all = "";
	public String smart = "";
	public String StrSql = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String StrSearch = "";
	public String QueryString = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String BranchAccess = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String discount_id = "0";
	public Smart SmartSearch = new Smart();
	public String advSearch = "";
	public String filter = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Discount ID", "numeric", "discount_id"},
			{"Job Titile", "text", "jobtitle_id IN (SELECT jobtitle_id FROM compdb.axela_jobtitle WHERE jobtitle_desc"},
			{"Model", "text", "model_id IN (SELECT model_id FROM compdb.axela_inventory_item_model WHERE jobtitle_desc"},
			{"Entry By", "text", "discount_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "discount_entry_date"},
			{"Modified By", "text", "discount_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "discount_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_discount_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				smart = PadQuotes(request.getParameter("smart"));
				msg = PadQuotes(request.getParameter("msg"));
				discount_id = CNumeric(PadQuotes(request.getParameter("discount_id")));
				all = PadQuotes(request.getParameter("all"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				filter = PadQuotes(request.getParameter("filter"));
				SOP("filter==" + filter);
				if (filter.equals("yes")) {
					SearchData(request, response);
					// TotalStockDetails();
				}

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND discount_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all Discount!";
				} else if (!discount_id.equals("0")) {
					msg += "<br>Results for Discount ID = " + discount_id + "!";
					StrSearch += " AND discount_id = " + discount_id + "";
				}

				else if ("yes".equals(smart)) {
					msg += "<br>Results of Search!";
					if (!GetSession("Discountstrsql", request).equals("")) {
						StrSearch += GetSession("discountstrsql", request);
					}
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch += " AND discount_id = 0";
					} else {
						msg = "Results for Search!";
					}
				}

				StrSearch += BranchAccess;
				SetSession("Discountstrsql", StrSearch, request);
				StrHTML = ListData();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String ListData() {
		CachedRowSet crs = null;
		StringBuilder Str = new StringBuilder();
		int count = 0;
		try {
			if (!msg.equals("")) {

				StrSql = "SELECT discount_id,"
						+ " jobtitle_id, jobtitle_desc,"
						+ " IF(model_name IS NULL,'All Models',model_name) AS model_name ,"
						+ " brand_id, brand_name,"
						+ " discount_amount,"
						+ " discount_month"
						+ " FROM " + compdb(comp_id) + "axela_sales_discount"
						+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = discount_jobtitle_id"
						+ " INNER JOIN axelaauto.axela_brand ON brand_id = discount_brand_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = discount_model_id"
						+ " WHERE 1=1 "
						+ StrSearch
						+ " GROUP BY discount_id"
						+ " ORDER BY discount_id DESC";
				crs = processQuery(StrSql, 0);
				if (crs.next()) {
					crs.beforeFirst();
					Str.append("<div class=\"table-bordered\">\n");
					Str.append("\n<table class=\"table table-bordered table-responsive table-hover\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<tr align=\"center\">\n");
					Str.append("<th data-hide=\"phone, tablet\">#</th>\n");
					Str.append("<th data-toggle=\"true\">ID</th>\n");
					Str.append("<th data-hide=\"phone\">Job Title</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Brand</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Model</th>\n");
					Str.append("<th data-hide=\"phone\">Discount</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Month</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Action</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead><tr>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count++;
						Str.append("<tr><td align=center>\n").append(count);
						Str.append("</td>\n");
						Str.append("<td align=center>\n").append(crs.getString("discount_id"));
						Str.append("</td>\n");

						Str.append("<td align=left> <a href=\"../sales/list-discount.jsp?filter=yes"
								+ "&brand_id=" + crs.getString("brand_id")
								+ "&emp_jobtitle_id=" + crs.getString("jobtitle_id") + "\">")
								.append(crs.getString("jobtitle_desc"))
								.append("</a>\n");
						Str.append("</td>\n");
						Str.append("<td align=left>\n").append(crs.getString("brand_name"));
						Str.append("</td>\n");
						Str.append("<td align=left>\n").append(crs.getString("model_name"));
						Str.append("</td>\n");
						Str.append("<td align=right>\n").append(crs.getDouble("discount_amount"));
						Str.append("</td>\n");
						Str.append("<td align=center>\n").append(TextMonth(Integer.parseInt(crs.getString("discount_month").substring(4, 6)) - 1)
								+ " " + crs.getString("discount_month").substring(0, 4));
						Str.append("</td>");
						Str.append("<td align=center>\n").append("<a href=\"../sales/update-discount.jsp?update=yes&discount_id=" + crs.getString("discount_id") + "\">Update Discount</a>");
						Str.append("</td></tr>\n");
					}
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");

				}
				else {
					Str.append("<center><div><br><br><br><br><font color=\"red\"><b>No Discount found!</b></font><br><br></div></center>\n");
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public void SearchData(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String brand_id = CNumeric(PadQuotes(request.getParameter("brand_id")));
		String emp_jobtitle_id = CNumeric(PadQuotes(request.getParameter("emp_jobtitle_id")));

		StrSql += " AND (branch_brand_id = " + brand_id + " OR emp_branch_id IN (SELECT emp_branch_id"
				+ " FROM " + compdb(comp_id) + "axela_emp_branch"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id"
				+ " WHERE 1=1"
				+ " AND branch_brand_id = " + brand_id
				+ " ) OR emp_all_branches = 1 )"
				+ " AND emp_jobtitle_id = " + emp_jobtitle_id
				+ " AND emp_active = 1"
				+ " AND branch_branchtype_id = 1";

		SetSession("exestrsql", StrSql, request);
		response.sendRedirect(response.encodeRedirectURL("../portal/exe-list.jsp?smart=yes"));

	}
}
