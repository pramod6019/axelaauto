package axela.inventory;

import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToHTML;
import cloudify.connect.ExportToPDF;
import cloudify.connect.ExportToXLSX;

public class Inventory_Report_Pricebook_Export extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String rateclass_id = "0";
	public String printoption = "";
	public String report = "";
	public String exporttype = "";
	public String exportB = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String exportcount = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String exportpage = "inventory-report-pricebook-export.jsp";
	DecimalFormat df = new DecimalFormat("0.00");
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_export_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				exportcount = ExecuteQuery("SELECT comp_export_count FROM " + compdb(comp_id) + "axela_comp");
				printoption = PadQuotes(request.getParameter("report"));
				exporttype = PadQuotes(request.getParameter("exporttype"));
				exportB = PadQuotes(request.getParameter("btn_export"));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				report = PadQuotes(request.getParameter("report"));

				if (!GetSession("pricebookstrsql", request).equals("")) {
					StrSearch = GetSession("pricebookstrsql", request);
					StrSearch = StrSearch.replace(" emp_id ", " e.emp_id ");
					if (StrSearch.contains("CONCAT(emp_name, emp_ref_no)")) {
						StrSearch = StrSearch.replace("CONCAT(emp_name, emp_ref_no)", "CONCAT(e.emp_name, e.emp_ref_no)");
					}
				}
				if (!GetSession("rateclass_id", request).equals("")) {
					rateclass_id = GetSession("rateclass_id", request);
				}
				if (report.equals("PriceBookDetails") && exportB.equals("Export")) {
					PriceBookDetails(request, response);
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PriceBookDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			String options = "";
			String optionHeader = "", itemHeader = "";
			int ch = 1;

			optionHeader += " ( SELECT '', '', '', '','', '', '', '', '', '', '', ''";
			itemHeader += " UNION ( SELECT 'Model', 'Item', 'Item ID','Item Code','Service Code','HSN', 'Price', 'SGST', 'CGST', 'IGST', 'CESS', 'Ex-showroom Price'";
			StrSql = "SELECT COALESCE(itemoptions.item_id, '0') AS itemid, COALESCE(itemoptions.item_name, '') AS itemname"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item main"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = main.item_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_option ON option_itemmaster_id = main.item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item itemoptions ON itemoptions.item_id = option_item_id"
					+ " WHERE main.item_active = '1'"
					+ "	AND model_active = '1'"
					+ StrSearch.replace("item_id", "main.item_id") + ""
					+ " GROUP BY itemoptions.item_id";
			SOP("StrSql==options==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			int count = 1;
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					options += " COALESCE ((SELECT COALESCE (pricetrans_amt, 0) AS pricetrans_amt"
							+ " FROM " + compdb(comp_id) + "axela_inventory_item_price_trans"
							+ " WHERE 1 = 1"
							+ " AND pricetrans_price_id = exshowroom.price_id"
							+ " AND pricetrans_item_id = " + crs.getString("itemid")
							+ " AND option_itemmaster_id = mainitem.item_id"
							+ " AND itemoptions.item_active = '1'"
							+ " AND group_active = '1'"
							+ " GROUP BY group_name, group_type, itemoptions.item_id"
							+ " ORDER BY group_rank, group_name DESC, itemoptions.item_id ), 0 ) AS '" + crs.getString("itemname") + "',";
					count++;
					optionHeader += ", " + crs.getString("itemid") + " AS 'Option " + ch++ + "'";
					itemHeader += ", '" + crs.getString("itemname") + "'";
				}
				options = options.substring(0, options.length() - 1);
			}
			optionHeader += " )";
			itemHeader += " )";

			crs.close();

			StrSql = "";
			StrSql += optionHeader + itemHeader + " UNION ( SELECT model_name AS 'Model',"
					+ " mainitem.item_name AS 'Item',"
					+ " mainitem.item_id AS 'Item ID',"
					+ " mainitem.item_code AS 'Item Code',"
					+ " mainitem.item_service_code AS 'Service Code',"
					+ " mainitem.item_hsn AS 'HSN',"
					+ " exshowroom.price_amt AS 'Price',"
					// + " @sgst := COALESCE(ROUND(exshowroom.price_amt * sgst.customer_rate / 100, 2), 0) AS 'SGST',"
					// + " @cgst := COALESCE(ROUND(exshowroom.price_amt * cgst.customer_rate / 100, 2), 0) AS 'CGST',"
					// + " @igst := COALESCE(ROUND(exshowroom.price_amt * igst.customer_rate / 100, 2), 0) AS 'IGST',"
					// + " @cess := COALESCE(ROUND(exshowroom.price_amt * cess.customer_rate / 100, 2), 0) AS 'CESS',"
					+ " COALESCE(CONCAT(ROUND(sgst.customer_rate), '%'), '') AS 'SGST',"
					+ " COALESCE(CONCAT(ROUND(cgst.customer_rate), '%'), '') AS 'CGST',"
					+ " COALESCE(CONCAT(ROUND(igst.customer_rate), '%'), '') AS 'IGST',"
					+ " COALESCE(CONCAT(ROUND(cess.customer_rate), '%'), '') AS 'CESS',"
					+ " ROUND((exshowroom.price_amt - exshowroom.price_disc) "
					+ "	+ ROUND(exshowroom.price_amt * sgst.customer_rate / 100, 2)"
					+ " + ROUND(exshowroom.price_amt * cgst.customer_rate / 100, 2)"
					+ " + ROUND(exshowroom.price_amt * cess.customer_rate / 100, 2), 2) AS 'Ex-showroom Price',"
					+ options
					+ " FROM " + compdb(comp_id) + "axela_inventory_item mainitem"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer sgst ON sgst.customer_id = mainitem.item_salestax1_ledger_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer cgst ON cgst.customer_id = mainitem.item_salestax2_ledger_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer igst ON igst.customer_id = mainitem.item_salestax3_ledger_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer cess ON cess.customer_id = mainitem.item_salestax4_ledger_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price exshowroom ON exshowroom.price_item_id = mainitem.item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_rate_class ON rateclass_id = exshowroom.price_rateclass_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = mainitem.item_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_option ON option_itemmaster_id = mainitem.item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_group ON group_id = option_group_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item itemoptions ON itemoptions.item_id = option_item_id"
					+ " LEFT JOIN axela_brand ON brand_id = model_brand_id"
					+ " WHERE mainitem.item_active = '1'"
					+ "	AND model_active = '1'"
					+ " AND exshowroom.price_rateclass_id = " + rateclass_id + ""
					+ " AND exshowroom.price_id = (SELECT price_id"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_price"
					+ " WHERE price_item_id = mainitem.item_id"
					+ " AND price_rateclass_id	 = " + rateclass_id + ""
					+ " AND price_effective_from <= " + ToLongDate(kknow()) + ""
					+ " AND price_amt != 0"
					+ " AND price_active = '1'"
					+ " ORDER BY price_effective_from"
					+ " DESC LIMIT 1) " + StrSearch.replace("item_id", "mainitem.item_id")
					+ " GROUP BY mainitem.item_id"
					+ " ORDER BY mainitem.item_name"
					+ " LIMIT " + exportcount + " )";
			SOP("StrSql==PriceBookDetails==" + StrSql);
			crs = processQuery(StrSql, 0);

			if (exporttype.equals("xlsx")) {
				ExportToXLSX exportToXLSX = new ExportToXLSX();
				exportToXLSX.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("html")) {
				ExportToHTML exportToHTML = new ExportToHTML();
				exportToHTML.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("pdf")) {
				ExportToPDF exportToPDF = new ExportToPDF();
				exportToPDF.Export(request, response, crs, printoption, "A1", comp_id);
			} else if (exporttype.equals("csv")) {
				ExportToXLSX exportToXLSX = new ExportToXLSX();
				exportToXLSX.Export(request, response, crs, printoption, comp_id);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
	}
	public String PopulatePrintOption() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value =PriceBookDetails").append(StrSelectdrop("Price Book Details", printoption)).append(">Price Book Details</option>\n");
		return Str.toString();
	}
}
