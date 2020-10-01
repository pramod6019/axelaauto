package axela.inventory;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cloudify.connect.Connect;

public class Item extends Connect {

	public String msg = "";
	public String starttime = "";
	public String start_time = "";
	public String endtime = "";
	public String end_time = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String StrHTML = "";
	public String StrSearch = "";
	public String smart = "";
	public String StrSql = "";
	public String ExportPerm = "";
	public String EnableSearch = "1";
	public String ListLink = "<a href=../inventory/inventory-item-list.jsp?smart=yes>Click here to List Items</a>";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {

			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_item_access, emp_sales_item_access, emp_pos_item_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				smart = PadQuotes(request.getParameter("smart"));
				// ExportPerm = ReturnPerm(comp_id, "emp_export_access", request, response);
				if (!smart.equals("yes")) {
					GetValues(request, response);
					CheckForm();
					if (!starttime.equals("")) {
						StrSearch = " and substr(item_entry_date,1,8) >= substr('" + starttime + "',1,8) ";
					}
					if (!endtime.equals("")) {
						StrSearch = StrSearch + " and substr(item_entry_date,1,8) <= substr('" + endtime + "',1,8) ";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						SetSession("itemstrsql", StrSearch, request);
						StrHTML = ItemsSummary(request);
					}
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in: " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));
		if (starttime.equals("")) {
			starttime = ReportStartdate();
		}
		if (endtime.equals("")) {
			endtime = strToShortDate(ToShortDate(kknow()));
		}
	}

	protected void CheckForm() {
		msg = "";
		if (starttime.equals("")) {
			msg = msg + "<br>Select Start Date!";
		}
		if (!starttime.equals("")) {
			if (isValidDateFormatShort(starttime)) {
				starttime = ConvertShortDateToStr(starttime);
				start_time = strToShortDate(starttime);
			} else {
				msg = msg + "<br>Enter Valid Start Date!";
				starttime = "";
			}
		}
		if (endtime.equals("")) {
			msg = msg + "<br>Select End Date!";
		}
		if (!endtime.equals("")) {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg = msg + "<br>Start Date should be less than End date!";
				}
				end_time = strToShortDate(endtime);
			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}
	}

	public String ItemsSummary(HttpServletRequest request) {
		String count = "";
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<b>Item Summary</b><br>");
			Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
			Str.append("<tr align=center>\n");
			Str.append("<th>&nbsp;</th>\n");
			Str.append("<th>Total</th>\n");
			Str.append("<th>Stock Items</th>\n");
			Str.append("<th>Active</th>\n");
			Str.append("<th>Inactive</th>\n");
			Str.append("</tr>");
			Str.append("<tr align=center>\n");
			Str.append("<td align=center>Items</td>\n");
			count = ExecuteQuery("select count(item_id) from " + compdb(comp_id) + "axela_inventory_item where 1 = 1");
			Str.append("<td align=center><b>").append(count).append("</b></td>\n");
			count = ExecuteQuery("select count(item_id) from " + compdb(comp_id) + "axela_inventory_item where item_nonstock = '1'");
			Str.append("<td align=center><b>").append(count).append("</b></td>\n");
			count = ExecuteQuery("select count(item_id) from " + compdb(comp_id) + "axela_inventory_item where item_active = '1'");
			Str.append("<td align=center><b>").append(count).append("</b></td>\n");
			count = ExecuteQuery("select count(item_id) from " + compdb(comp_id) + "axela_inventory_item where  item_active = '0'");
			Str.append("<td align=center><b>").append(count).append("</b></td>\n");
			Str.append("</tr>");
			Str.append("</table>");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in: " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
