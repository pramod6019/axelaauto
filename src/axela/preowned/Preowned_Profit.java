// Ved Prakash (9th July 2013)
package axela.preowned;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Preowned_Profit extends Connect {

	public String msg = "";
	public String StrSql = "";
	public String StrHTML = "";
	public String StrSearch = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String branch_id = "0";
	public String ExeAccess = "";
	public String BranchAccess = "";
	public String dr_branch_id = "";
	public String so_emp_id = "0";
	public String brand_id = "", region_id = "", team_id = "", exe_id = "";
	public String[] brand_ids, region_ids, branch_ids, team_ids, exe_ids;
	public String preowned_variant_id = "";
	public String starttime = "";
	public String start_time = "";
	public String endtime = "";
	public String end_time = "";
	public String go = "";
	public String groupby = "0";
	StringBuilder strSOID = new StringBuilder();
	public axela.preowned.MIS_Check mischeck = new axela.preowned.MIS_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0"))
		{
			CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
			emp_id = CNumeric(GetSession("emp_id", request));
			ExeAccess = GetSession("ExeAccess", request);
			BranchAccess = GetSession("BranchAccess", request);
			branch_id = CNumeric(GetSession("emp_branch_id", request));
			go = PadQuotes(request.getParameter("submit_button"));
			preowned_variant_id = PadQuotes(request.getParameter("modelvariant"));
			groupby = PadQuotes(request.getParameter("dr_groupby"));
			// SOP("groupby-------------" + groupby);
			try {
				GetValues(request, response);
				CheckForm();
				if (!brand_id.equals("")) {
					StrSearch = " AND branch_brand_id in (" + brand_id + ") ";
				}
				if (!region_id.equals("")) {
					StrSearch += " AND branch_region_id in (" + region_id + ") ";
				}
				if (!branch_id.equals("")) {
					StrSearch = StrSearch + " AND preowned_branch_id IN (" + branch_id + ")";
				}

				if (!preowned_variant_id.equals("0")) {
					StrSearch += " AND preowned_variant_id = " + preowned_variant_id;
				}

				if (!exe_id.equals("")) {
					StrSearch += " AND so_emp_id IN (" + exe_id + ")";
				}

				if (!starttime.equals("")) {
					StrSearch += " AND SUBSTR(so_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)";
				}

				if (!endtime.equals("")) {
					StrSearch += " AND SUBSTR(so_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)";
				}

				if (go.equals("Go")) {
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = PreownedProfitLoss(request);
						if (!strSOID.toString().equals("")) {
							SetSession("sostrsql", " and (" + strSOID.toString() + ")", request);
						}
					}
				}
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}

	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));
		if (branch_id.equals("0")) {
			dr_branch_id = RetrunSelectArrVal(request, "dr_branch");
		} else {
			dr_branch_id = branch_id;
		}
		preowned_variant_id = PadQuotes(request.getParameter("modelvariant"));
		so_emp_id = CNumeric(PadQuotes(request.getParameter("dr_executive")));
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		team_id = RetrunSelectArrVal(request, "dr_preownedteam");
		team_ids = request.getParameterValues("dr_preownedteam");
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
		if (starttime.equals("")) {
			starttime = strToShortDate(ToShortDate(kknow()));
		}
		if (endtime.equals("")) {
			endtime = strToShortDate(ToShortDate(kknow()));
		}
	}
	protected void CheckForm() {
		msg = "";
		if (starttime.equals("")) {
			msg += "<br>SELECT Start Date!";
		}
		if (!starttime.equals("")) {
			if (isValidDateFormatShort(starttime)) {
				starttime = ConvertShortDateToStr(starttime);
				start_time = strToShortDate(starttime);
			} else {
				msg += "<br>Enter Valid Start Date!";
				starttime = "";
			}
		}

		if (endtime.equals("")) {
			msg += "<br>SELECT End Date!";
		}

		if (!endtime.equals("")) {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg += "<br>End Date should be greater than Start date!";
				}
				end_time = strToShortDate(endtime);
				// endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1,
				// 0, 0));
			} else {
				msg += "<br>Enter Valid End Date!";
				endtime = "";
			}
		}
	}

	public String PreownedProfitLoss(HttpServletRequest request) {
		int count = 0;
		double purchasedamt = 0.0;
		double refurbishamt = 0.0;
		double soamount = 0.0;
		double profitloss = 0.0;
		double totalpurchasedamt = 0.0;
		double totalrefurbishamt = 0.0;
		double totalsoamount = 0.0;
		double totprofitloss = 0.0;
		StringBuilder Str = new StringBuilder();

		try {
			StrSql = "SELECT branch_id, CONCAT(branch_name, ' (', branch_code, ')') AS branch_name";
			if (groupby.equals("brand_id")) {
				StrSql += ", brand_name";
			}
			if (groupby.equals("branch_id")) {
				StrSql += " , branch_name";
			}
			if (groupby.equals("region_id")) {
				StrSql += ", region_name";
			}
			StrSql += " FROM " + compdb(comp_id) + "axela_branch";
			if (groupby.equals("brand_id")) {
				StrSql += " INNER JOIN axela_brand ON brand_id = branch_brand_id";
			}
			if (groupby.equals("region_id")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch_region ON region_id = branch_region_id";
			}
			StrSql += " WHERE branch_active = 1 " + BranchAccess + ""
					+ " AND branch_branchtype_id IN (2)";

			if (!dr_branch_id.equals("")) {
				StrSql += " AND branch_id IN (" + dr_branch_id + ")";
			}
			StrSql += " GROUP BY " + groupby
					+ " ORDER BY";
			if (groupby.equals("brand_id")) {
				StrSql += " brand_name";
			}
			else if (groupby.equals("region_id")) {
				StrSql += " region_name";
			} else if (groupby.equals("branch_id")) {
				StrSql += " branch_name";
			}

			// SOP("StrSql--------" + StrSqlBreaker(StrSql));
			CachedRowSet crs1 = processQuery(StrSql, 0);
			if (crs1.isBeforeFirst()) {
				while (crs1.next()) {
					StrSql = "SELECT so_id, so_enquiry_id, so_quote_id, customer_id, customer_name, contact_id, preowned_sub_variant,"
							+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name, carmanuf_id, carmanuf_name, variant_name,"
							+ " CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name, emp_id, so_date, preownedmodel_id, preownedmodel_name,"
							+ " preownedstock_purchase_amt, preownedstock_refurbish_amt, so_grandtotal,"
							+ " COALESCE(preowned_id, 0) AS preowned_id,"
							+ " COALESCE(preownedstock_id, 0) AS preownedstock_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_so"
							+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_stock ON preownedstock_id = so_preownedstock_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = preownedstock_preowned_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = so_contact_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id";
					if (!team_id.equals("")) {
						StrSql += " INNER JOIN " + compdb(comp_id) + "axela_preowned_team_exe ON preownedteamtrans_emp_id = emp_id ";
					}
					StrSql += " LEFT JOIN axela_preowned_variant ON variant_id = preowned_variant_id"
							+ " LEFT JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
							+ " LEFT JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
							+ " WHERE so_active = '1'"
							+ ExeAccess
							+ BranchAccess
							+ StrSearch;
					// StrSql += " ORDER BY " + groupby;
					CachedRowSet crs = processQuery(StrSql, 0);
					// SOP("StrSql-------profit---------" + StrSqlBreaker(StrSql));
					Str.append("<div class=\"table-responsive table-bordered table-hover\">\n");
					Str.append("<center><b>").append(crs1.getString("branch_name")).append("</b></center>\n");
					Str.append("<table class=\"table table-responsive\" data-filter=\"#filter\">\n");

					if (crs.isBeforeFirst()) {
						Str.append("<thead>\n");
						Str.append("<tr align=center>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>#</b></th>\n");
						Str.append("<th data-toggle=\"true\"><b>SO ID</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>Quote ID</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>Enquiry ID</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>Stock ID</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>Manufacturer</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>Model</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>Varient</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>Customer</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>Sales Consultant</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>SO Date</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>Purchased Amount</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>Refurbishment Amount</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>SO Amount</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>Profit</b></th>\n");
						Str.append("</tr>\n");
						Str.append("</thead>\n");
						Str.append("<tbody>\n");
						while (crs.next()) {
							if (count == 0) {
								strSOID.append(" so_id = ").append(crs.getString("so_id"));
							} else {
								strSOID.append(" OR so_id = ").append(crs.getString("so_id"));
							}
							count++;
							purchasedamt = crs.getDouble("preownedstock_purchase_amt");
							refurbishamt = crs.getDouble("preownedstock_refurbish_amt");
							soamount = crs.getDouble("so_grandtotal");
							profitloss = soamount - purchasedamt - refurbishamt;
							totalpurchasedamt += crs.getDouble("preownedstock_purchase_amt");
							totalrefurbishamt += crs.getDouble("preownedstock_refurbish_amt");
							totalsoamount += crs.getDouble("so_grandtotal");
							totprofitloss += profitloss;

							Str.append("<tr valign=\"top\">\n").append("<td align=\"center\">").append(count).append("</td>\n");
							Str.append("<td align=\"center\" nowrap>");
							if (!crs.getString("so_id").equals("")) {
								Str.append("<a href=\"../sales/veh-salesorder-list.jsp?so_id=");
								Str.append(crs.getString("so_id")).append("\">");
								Str.append(crs.getString("so_id")).append("</a>");
							}
							Str.append("</td>\n<td align=\"center\" nowrap><a href=../sales/veh-quote-list.jsp?quote_id=").append(crs.getString("so_quote_id"));
							Str.append(">").append(crs.getString("so_quote_id")).append("</a>");
							Str.append("</td>\n<td align=\"center\" nowrap>");
							Str.append("<a href=../sales/enquiry-list.jsp?enquiry_id=");
							Str.append(crs.getString("so_enquiry_id")).append(">");
							Str.append(crs.getString("so_enquiry_id")).append("</a>").append("</td>\n");
							Str.append("<td align=\"center\">");
							if (!crs.getString("preownedstock_id").equals("0")) {
								Str.append("<a href=\"../preowned/preowned-stock-list.jsp?preownedstock_id=");
								Str.append(crs.getString("preownedstock_id")).append("\">");
								Str.append(crs.getString("preownedstock_id")).append("</a><br/>");
							}
							Str.append("</td>\n");
							Str.append("<td align=\"left\">");
							Str.append(crs.getString("carmanuf_name")).append("<br/>");
							Str.append("</td>\n");
							Str.append("<td align=\"left\">");
							Str.append(crs.getString("preownedmodel_name")).append("<br/>");
							Str.append("</td>\n");
							Str.append("<td align=\"left\">");
							Str.append("<a href=\"../preowned/preowned-list.jsp?preowned_id=");
							Str.append(crs.getString("preowned_id")).append("\">");
							Str.append(crs.getString("variant_name")).append("</a>");
							Str.append("<br/>").append(crs.getString("preowned_sub_variant"));
							Str.append("</td>\n<td valign=top align=left><a href=../customer/customer-list.jsp?customer_id=");
							Str.append(crs.getString("customer_id")).append(">");
							Str.append(crs.getString("customer_name")).append("</a><br/>");
							Str.append("<a href=\"../customer/customer-contact-list.jsp?contact_id=");
							Str.append(crs.getString("contact_id")).append("\">");
							Str.append(crs.getString("contact_name")).append("</a>");
							Str.append("</td>\n<td valign=top align=left><a href=../portal/executive-summary.jsp?emp_id=");
							Str.append(crs.getString("emp_id")).append(">").append(crs.getString("emp_name")).append("</a>");
							Str.append("</td>\n<td valign=top align=center>");
							if (!crs.getString("so_date").equals("")) {
								Str.append(strToShortDate(crs.getString("so_date")));
							}
							Str.append("</td>\n<td valign=top align=right>");
							if (!crs.getString("preownedstock_purchase_amt").equals("")) {
								Str.append(IndFormat(crs.getString("preownedstock_purchase_amt") + ""));
							}
							Str.append("</td>\n<td valign=top align=right>");
							if (!crs.getString("preownedstock_refurbish_amt").equals("0")) {
								Str.append(IndFormat(crs.getString("preownedstock_refurbish_amt") + ""));
							}
							Str.append("</td>\n").append("<td valign=top align=right>");
							if (!crs.getString("so_grandtotal").equals("")) {
								Str.append(IndFormat(crs.getString("so_grandtotal") + ""));
							}
							Str.append("</td>\n<td valign=top align=right>");
							Str.append(IndFormat(profitloss + ""));
							Str.append("</td>\n</tr>\n");
						}
						Str.append("<tr align=center>\n");
						Str.append("<td>&nbsp;</td>\n");
						Str.append("<td>&nbsp;</td>\n");
						Str.append("<td>&nbsp;</td>\n");
						Str.append("<td>&nbsp;</td>\n");
						Str.append("<td>&nbsp;</td>\n");
						Str.append("<td>&nbsp;</td>\n");
						Str.append("<td>&nbsp;</td>\n");
						Str.append("<td>&nbsp;</td>\n");
						Str.append("<td>&nbsp;</td>\n");
						Str.append("<td>&nbsp;</td>\n");
						Str.append("<td align=\"right\"><b>Total:</b></td>\n");
						Str.append("<td align=\"right\"><b>").append(IndFormat(totalpurchasedamt + "")).append("</b></td>\n");
						Str.append("<td align=\"right\"><b>").append(IndFormat(totalrefurbishamt + "")).append("</b></td>\n");
						Str.append("<td align=\"right\"><b>").append(IndFormat(totalsoamount + "")).append("</b></td>\n");
						Str.append("<td align=\"right\"><b>").append(IndFormat(totprofitloss + "")).append("</b></td>\n");
						Str.append("</tr>\n");
					} else {
						Str.append("<tr align=center>");
						Str.append("<td><br><br><br><b><center><font color=red>No " + ReturnPreOwnedName(request) + " Sale found!</font></center></b><br><br><br><br></td>\n").append("</td>\n");
						Str.append("</tr>\n");
					}
					crs.close();
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
					Str.append("</div>\n");
				}
				// Str.append("</td>\n</tr>\n");
				// Str.append("</table>\n");
				// Str.append("</body>\n");
				// Str.append("</html>\n");
				crs1.close();
			} else {
				Str.append("<center><font color=red><b>No Branch(s) found!</b></font></center>");
			}
			crs1.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String PopulateVariant(String variant_id) {

		StringBuilder Str = new StringBuilder();
		try {
			if (!variant_id.equals("")) {
				StrSql = "SELECT variant_id, variant_name"
						+ " FROM axela_preowned_variant"
						+ " WHERE 1=1"
						+ " AND variant_id = " + variant_id;;
				// if (!preowned_model_id.equals("0")) {
				// StrSql = StrSql + " and variant_preownedmodel_id = " +
				// preowned_preownedmodel_id;
				// }
				StrSql = StrSql + " ORDER BY variant_name";
				// SOP("StrSql==" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);
				Str.append("<option value=0>SELECT</option>");
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("variant_id")).append("");
					Str.append(StrSelectdrop(crs.getString("variant_id"), preowned_variant_id));
					Str.append(">").append(crs.getString("variant_name") + "(" + crs.getString("variant_id") + ")").append("</option>\n");
				}
				crs.close();
			}

			StrSql = "SELECT variant_id, variant_name"
					+ " FROM axela_preowned_variant"
					+ " WHERE 1=1";
			// + " AND variant_id = " + variant_id;
			// if (!preowned_model_id.equals("0")) {
			// StrSql = StrSql + " and variant_preownedmodel_id = " +
			// preowned_preownedmodel_id;
			// }
			if (!variant_id.equals("")) {
				StrSql += " AND variant_id != " + variant_id;
			}
			StrSql = StrSql + " ORDER BY variant_name";
			// SOP("StrSql==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>SELECT</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("variant_id")).append("");
				Str.append(StrSelectdrop(crs.getString("variant_id"), preowned_variant_id));
				Str.append(">").append(crs.getString("variant_name") + "(" + crs.getString("variant_id") + ")").append("</option>\n");
			}
			crs.close();

			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateGroupBy(String comp_id) {
		StringBuilder Str = new StringBuilder();
		// Str.append("<option value=0>SELECT</option>");
		Str.append("<option value=brand_id").append(StrSelectdrop("brand_id", groupby)).append(">Brands</option>\n");
		Str.append("<option value=region_id").append(StrSelectdrop("region_id", groupby)).append(">Regions</option>\n");
		Str.append("<option value=branch_id").append(StrSelectdrop("branch_id", groupby)).append(">Branches</option>\n");
		// Str.append("<option value=brand_id").append(StrSelectdrop("brand_id", groupby )).append(">Models</option>\n");

		return Str.toString();
	}

}
