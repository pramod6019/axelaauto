package axela.inventory;
//Murali 2nd july

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToHTML;
import cloudify.connect.ExportToPDF;
import cloudify.connect.ExportToXLSX;

public class Item_Export extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String printoption = "";
	public String exporttype = "";
	public String exportB = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String exportcount = "";
	public String BranchAccess = "", ExeAccess = "";
	public String exportpage = "item-export.jsp";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			// HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_export_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				exportcount = ExecuteQuery("SELECT comp_export_count FROM " + compdb(comp_id) + "axela_comp");
				printoption = PadQuotes(request.getParameter("report"));
				exporttype = PadQuotes(request.getParameter("exporttype"));
				exportB = PadQuotes(request.getParameter("btn_export"));
				if (!GetSession("itemsstrsql", request).equals("")) {
					StrSearch = GetSession("itemsstrsql", request);
				}
				if (exportB.equals("Export")) {
					ItemDetails(request, response);

				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void ItemDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "SELECT item_id AS 'ID',"
					+ " item_name AS 'Name',"
					+ " item_code AS 'Code',"
					+ " item_service_code AS 'ServiceCode',"
					+ "	item_hsn AS 'HSN',"
					+ " COALESCE (model_name, '') AS Model,"
					+ " item_small_desc AS 'Description',"
					+ " type_name AS 'Type',"
					+ " COALESCE (fueltype_name, '') AS 'Fuel',"
					+ " item_uom_id AS 'UOM',"
					+ " item_eoq AS 'EOQ',"
					+ " COALESCE (price_disc, 0) AS 'Discount',"
					+ " cat_name AS 'Category'"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_cat ON cat_id = item_cat_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_type ON type_id = item_type_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_uom ON uom_id = item_uom_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_fueltype ON fueltype_id = item_fueltype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON item_model_id = model_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = item_id"
					+ " WHERE 1=1 " + StrSearch + ""
					+ " GROUP BY item_id"
					+ " ORDER BY item_id DESC" + " "
					+ " LIMIT " + exportcount;
			// SOP("StrSql--------enq export----" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);
			if (exporttype.equals("xlsx")) {
				ExportToXLSX exportToXLSX = new ExportToXLSX();
				exportToXLSX.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("html")) {
				ExportToHTML exportToHTML = new ExportToHTML();
				exportToHTML.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("pdf")) {
				ExportToPDF exportToPDF = new ExportToPDF();
				exportToPDF.Export(request, response, crs, printoption, "A1", comp_id);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
	}
	public String PopulatePrintOption() {
		String print = "";
		print = print + "<option value = ItemDetails" + StrSelectdrop("ItemDetails", printoption) + ">Item Details</option>\n";
		return print;
	}
}
