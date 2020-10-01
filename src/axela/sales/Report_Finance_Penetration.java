package axela.sales;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Finance_Penetration extends Connect {

	public String StrSql = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String comp_id = "0";
	public static String msg = "";
	public String emp_id = "", branch_id = "";
	public String[] brand_ids, region_ids, branch_ids, team_ids, exe_ids, model_ids;
	public String brand_id = "", region_id = "", team_id = "", exe_id = "", model_id = "";
	public String StrHTML = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String go = "";
	public String ExeAccess = "";
	public String StrSearch = "";
	public String dr_totalby = "0";
	static DecimalFormat deci = new DecimalFormat("0.00");
	public String SearchURL = "<a href=../sales/report-finance-penetration.jsp?filter=yes", idname = "", SearchURLType = "";
	public String filter = "", StrFilter = "";
	// public String SearchURL = "report-monitoring-board-search.jsp";
	public MIS_Check1 mischeck = new MIS_Check1();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_enquiry_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				filter = PadQuotes(request.getParameter("filter"));
				go = PadQuotes(request.getParameter("submit_button"));

				if (go.equals("")) {
					start_time = DateToShortDate(kknow());
					end_time = DateToShortDate(kknow());
					msg = "";
				}

				if (filter.equals("yes")) {
					SalesOrderRedirect(request, response);
				}

				if (go.equals("Go")) {
					GetValues(request, response);
					CheckForm();

					StrSearch = BranchAccess.replace("branch_id", "team_branch_id");
					StrSearch += ExeAccess;
					if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ") ";
					}
					if (!branch_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						StrSearch += "AND branch_id IN (" + branch_id + ")";
					}
					if (!team_id.equals("")) {
						StrSearch += " AND team_id IN (" + team_id + ") ";
					}
					if (!exe_id.equals("")) {
						StrSearch += " AND emp_id IN (" + exe_id + ")";
					}
					if (!model_id.equals("")) {
						StrSearch += " AND model_id IN (" + model_id + ")";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = ListFinacePenetration();
						// if(dr_totalby.equals("0"))
						// {
						//
						// }
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));
		if (starttime.equals("")) {
			starttime = strToShortDate(ToShortDate(kknow()));
		}

		if (endtime.equals("")) {
			endtime = strToShortDate(ToShortDate(kknow()));
		}

		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}

		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		team_id = RetrunSelectArrVal(request, "dr_team");
		team_ids = request.getParameterValues("dr_team");
		model_id = RetrunSelectArrVal(request, "dr_model");
		model_ids = request.getParameterValues("dr_model");
		dr_totalby = request.getParameter("dr_totalby");
	}

	protected void CheckForm() {
		msg = "";
		if (starttime.equals("")) {
			msg += "<br>Select Start Date!";
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
			msg += "<br>Select End Date!<br>";
		}
		if (!endtime.equals("")) {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg += "<br>Start Date should be less than End date!";
				}
				end_time = strToShortDate(endtime);

			} else {
				msg += "<br>Enter Valid End Date!";
				endtime = "";
			}
		}

	}

	public String ListFinacePenetration() {

		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT emp_id,"
				+ " CONCAT(emp_name,' (', emp_ref_no,')') AS emp_name,"
				+ " emp_active,";
		switch (dr_totalby) {
			case "so_emp_id" :
				StrSql += "emp_name,";
				break;
			case "team_id" :
				StrSql += "team_id, team_name,";
				break;
			case "so_branch_id" :
				StrSql += "branch_id, branch_name, branch_code,";
				break;
			case "branch_brand_id" :
				StrSql += "brand_id, brand_name,";
				break;
			case "branch_region_id" :
				StrSql += "region_id, region_name,";
				break;
			case "model_id" :
				StrSql += " model_id, model_name,";
				break;
			case "bank_id" :
				StrSql += "bank_id, bank_name,";
				break;
			case "finstatus_id" :
				StrSql += "finstatus_id, finstatus_name,";
				break;
		}
		StrSql += " COUNT(so_id) AS socount,"
				+ " SUM(IF(so_finstatus_id != 0, 1, 0)) AS fingivencount,"
				+ " SUM(IF(so_finance_amt != 0, so_finance_amt, 0)) AS fingivenamt,"
				+ " SUM(IF(so_finstatus_id = 0, 1, 0)) AS finnotgivencount, so_finstatus_id"
				+ " FROM " + compdb(comp_id) + "axela_sales_so"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id";
		if (!team_id.equals("") || dr_totalby.equals("team_id")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = so_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id ";
		}
		if (!model_id.equals("") || dr_totalby.equals("model_id")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = so_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id";
		}
		if (dr_totalby.equals("bank_id")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_so_bank ON bank_id = so_finstatus_bank_id";
		}
		if (dr_totalby.equals("branch_region_id")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch_region ON region_id = branch_region_id";
		}
		if (dr_totalby.equals("branch_brand_id")) {
			StrSql += " INNER JOIN axelaauto.axela_brand ON brand_id = branch_brand_id";
		}
		if (dr_totalby.equals("finstatus_id")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_so_finance_status ON finstatus_id = so_finstatus_id";
		}

		StrSql += " WHERE 1 = 1"
				+ " AND so_active = 1"
				+ " AND emp_active = 1"
				+ " AND emp_id != 1"
				+ " AND SUBSTR(so_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(so_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)"
				+ StrSearch;

		if (dr_totalby.equals("so_emp_id")) {
			StrSql += " GROUP BY " + dr_totalby
					+ " ORDER BY emp_name";
		} else if (dr_totalby.equals("team_id")) {
			StrSql += " GROUP BY " + dr_totalby
					+ " ORDER BY team_name";
		} else if (dr_totalby.equals("so_branch_id")) {
			StrSql += " GROUP BY " + dr_totalby
					+ " ORDER BY branch_name";
		} else if (dr_totalby.equals("branch_region_id")) {
			StrSql += " GROUP BY " + dr_totalby;
			StrSql += " ORDER BY region_name";
		} else if (dr_totalby.equals("branch_brand_id")) {
			StrSql += " GROUP BY " + dr_totalby;
			StrSql += " ORDER BY brand_name";
		} else if (dr_totalby.equals("model_id")) {
			StrSql += " GROUP BY " + dr_totalby;
			StrSql += " ORDER BY model_name";
		} else if (dr_totalby.equals("bank_id")) {
			StrSql += " GROUP BY " + dr_totalby;
			StrSql += " ORDER BY bank_name";
		}
		else if (dr_totalby.equals("finstatus_id")) {
			StrSql += " GROUP BY " + dr_totalby;
			StrSql += " ORDER BY finstatus_name";
		}

		// SOP("ListFinacePenetration-----------" + StrSql);

		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {

				SearchURL += "&starttime=" + starttime + "&endtime=" + endtime
						+ "&brand_id=" + brand_id
						+ "&region_id=" + region_id
						+ "&dr_branch_id=" + branch_id
						+ "&team_id=" + team_id
						+ "&exe_id=" + exe_id
						+ "&model_id=" + model_id;

				if (dr_totalby.equals("so_emp_id")) {
					SearchURLType += "&type=emp";
					idname = "emp_id";
				} else if (dr_totalby.equals("team_id")) {
					SearchURLType += "&type=team";
					idname = "team_id";
				} else if (dr_totalby.equals("branch_brand_id")) {
					SearchURLType += "&type=brand";
					idname = "brand_id";
				} else if (dr_totalby.equals("so_branch_id")) {
					SearchURLType += "&type=branch";
					idname = "branch_id";
				} else if (dr_totalby.equals("branch_region_id")) {
					SearchURLType += "&type=region";
					idname = "region_id";
				} else if (dr_totalby.equals("model_id")) {
					SearchURLType += "&type=model";
					idname = "model_id";
				} else if (dr_totalby.equals("bank_id")) {
					SearchURLType += "&type=bank";
					idname = "bank_id";
				} else if (dr_totalby.equals("finstatus_id")) {
					SearchURLType += "&type=finstatus";
					idname = "finstatus_id";
				}

				// Str.append("<div class=\" table-bordered\">\n");
				Str.append("<table class=\"table table-hover table-bordered\" data-filter=\"#filter\" id='table'>\n");
				Str.append("<thead>");
				Str.append("<tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				if (dr_totalby.equals("so_emp_id")) {
					Str.append("<th data-toggle=\"true\">Executive</th>\n");
				} else if (dr_totalby.equals("team_id")) {
					Str.append("<th data-toggle=\"true\">Team</th>\n");
				} else if (dr_totalby.equals("so_branch_id")) {
					Str.append("<th data-toggle=\"true\">Branch</th>\n");
				} else if (dr_totalby.equals("branch_region_id")) {
					Str.append("<th data-toggle=\"true\">Region</th>\n");
				} else if (dr_totalby.equals("branch_brand_id")) {
					Str.append("<th data-toggle=\"true\">Brand</th>\n");
				} else if (dr_totalby.equals("model_id")) {
					Str.append("<th data-toggle=\"true\">Model</th>\n");
				} else if (dr_totalby.equals("bank_id")) {
					Str.append("<th data-toggle=\"true\">Bank</th>\n");
				} else if (dr_totalby.equals("finstatus_id")) {
					Str.append("<th data-toggle=\"true\">Finance Status</th>\n");
				}
				Str.append("<th >SO Count</th>\n");
				Str.append("<th data-hide=\"phone\">Finance Given</th>\n");
				Str.append("<th data-hide=\"phone\">Finance Amount</th>\n");
				Str.append("<th data-hide=\"phone\">Finance Not Given</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				int count = 0;
				int total_so_count = 0, total_fingiven_count = 0, total_finnotgiven_count = 0;
				long total_fingiven_amt = 0;
				while (crs.next()) {
					count++;
					total_so_count += crs.getInt("socount");
					total_fingiven_count += crs.getInt("fingivencount");
					total_fingiven_amt += crs.getDouble("fingivenamt");
					total_finnotgiven_count += crs.getInt("finnotgivencount");

					Str.append("<tr>");
					Str.append("<td align='center'>").append(count).append("</td>");
					switch (dr_totalby) {
						case "so_emp_id" :
							Str.append("<td><a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getString("emp_id"))
									.append("\">").append(crs.getString("emp_name")).append("</a></td>");
							break;
						case "team_id" :
							Str.append("<td align='left'>").append(crs.getString("team_name")).append("</td>");
							break;

						case "so_branch_id" :
							Str.append("<td align='left'>").append(crs.getString("branch_name") + " (" + crs.getString("branch_code") + " )").append("</td>");
							break;

						case "branch_region_id" :
							Str.append("<td align='left'>").append(crs.getString("region_name")).append("</td>");
							break;

						case "branch_brand_id" :
							Str.append("<td align='left'>").append(crs.getString("brand_name")).append("</td>");
							break;

						case "model_id" :
							Str.append("<td align='left'>").append(crs.getString("model_name")).append("</td>");
							break;

						case "bank_id" :
							Str.append("<td align='left'>").append(crs.getString("bank_name")).append("</td>");
							break;
						case "finstatus_id" :
							Str.append("<td align='left'>").append(crs.getString("finstatus_name")).append("</td>");
							break;
					}

					Str.append("<td valign=top align=right>").append(SearchURL).append(SearchURLType)
							.append("&typeid=" + crs.getString(idname)).append(" target=_blank>")
							.append(crs.getString("socount")).append("</a></td>");
					Str.append("<td align='right'>").append(SearchURL).append(SearchURLType)
							.append("&fingiven=yes&typeid=" + crs.getString(idname)).append(" target=_blank>")
							.append(crs.getString("fingivencount")).append("</td>");
					Str.append("<td align='right'>").append(IndFormat(deci.parse(crs.getString("fingivenamt")) + "")).append("</td>");
					Str.append("<td align='right'>").append(SearchURL).append(SearchURLType + "&finnotgiven=yes")
							.append("&typeid=" + crs.getString(idname)).append(" target=_blank>")
							.append(crs.getString("finnotgivencount")).append("</td>");
					Str.append("</tr>");
				}
				Str.append("<tr>");
				Str.append("<td></td>");
				Str.append("<td align='right'><b>Total:</b></td>");

				if (dr_totalby.equals("finstatus_id")) {
					Str.append("<td align='right'><b>").append(SearchURL + "&type=finstatus target=_blank>").append(total_so_count).append("</a></b></td>");
				} else {
					Str.append("<td align='right'><b>").append(SearchURL + " target=_blank>").append(total_so_count).append("</a></b></td>");
				}
				Str.append("<td align='right'><b>").append(SearchURL + "&type=finstatus&fingiven=yes target=_blank>").append(total_fingiven_count).append("</a></b></td>");
				Str.append("<td align='right'><b>").append(IndFormat(deci.format(total_fingiven_amt) + "")).append("</b></td>");
				Str.append("<td align='right'><b>").append(SearchURL + "&finnotgiven=yes target=_blank>").append(total_finnotgiven_count).append("</a></b></td>");
				Str.append("</tr>");
				Str.append("</table>");
				// Str.append("</div>");
			} else {
				Str.append("<font color=red><br><br><br><b>No Details Found!</b></font>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
	public String PopulateTeam() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT team_id, team_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_team "
					+ " WHERE team_branch_id = " + dr_branch_id + " "
					+ " AND team_active = 1"
					+ " GROUP BY team_id "
					+ " ORDER BY team_name ";
			// SOP("PopulateTeam query ==== " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("team_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("team_id"), team_ids));
				Str.append(">").append(crs.getString("team_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateTotalBy(String comp_id) {
		StringBuilder Str = new StringBuilder();
		// Str.append("<option value=0>Select</option>");
		Str.append("<option value=so_emp_id").append(StrSelectdrop("so_emp_id", dr_totalby)).append(">Consultants</option>\n");
		Str.append("<option value=team_id").append(StrSelectdrop("team_id", dr_totalby)).append(">Teams</option>\n");
		Str.append("<option value=so_branch_id").append(StrSelectdrop("so_branch_id", dr_totalby)).append(">Branches</option>\n");
		Str.append("<option value=branch_region_id").append(StrSelectdrop("branch_region_id", dr_totalby)).append(">Regions</option>\n");
		Str.append("<option value=branch_brand_id").append(StrSelectdrop("branch_brand_id", dr_totalby)).append(">Brands</option>\n");
		Str.append("<option value=model_id").append(StrSelectdrop("model_id", dr_totalby)).append(">Models</option>\n");
		Str.append("<option value=bank_id").append(StrSelectdrop("bank_id", dr_totalby)).append(">Banks</option>\n");
		Str.append("<option value=finstatus_id").append(StrSelectdrop("finstatus_id", dr_totalby)).append(">Finance Status</option>\n");
		return Str.toString();
	}

	private void SalesOrderRedirect(HttpServletRequest request, HttpServletResponse response) {

		try {
			HttpSession session = request.getSession(true);
			String brand_id = PadQuotes(RetrunSelectArrVal(request, "brand_id"));
			String region_id = PadQuotes(RetrunSelectArrVal(request, "branch_region_id"));
			String branch_id = PadQuotes(RetrunSelectArrVal(request, "dr_branch_id"));
			String model_id = PadQuotes(RetrunSelectArrVal(request, "model_id"));
			String team_id = PadQuotes(RetrunSelectArrVal(request, "team_id"));
			String emp_id = PadQuotes(RetrunSelectArrVal(request, "exe_id"));
			String bank_id = PadQuotes(RetrunSelectArrVal(request, "bank_id"));
			// String so_finstatus_id = PadQuotes(RetrunSelectArrVal(request, "so_finstatus_id"));
			String type = PadQuotes(request.getParameter("type"));
			String type_id = PadQuotes(RetrunSelectArrVal(request, "typeid"));
			String starttime = PadQuotes(request.getParameter("starttime"));
			String endtime = PadQuotes(request.getParameter("endtime"));
			String finnotgiven = PadQuotes(request.getParameter("finnotgiven"));
			String fingiven = PadQuotes(request.getParameter("fingiven"));
			// SOP("brand_id== " + brand_id);
			// SOP("region_id== " + region_id);
			// SOP("branch_id== " + branch_id);
			// SOP("model_id== " + model_id);
			// SOP("team_id== " + team_id);
			// SOP("emp_id== " + emp_id);
			// SOP("bank_id== " + bank_id);
			// SOP("type== " + type);
			// SOP("type_id== " + type_id);
			// SOP("starttime== " + starttime);
			// SOP("endtime== " + endtime);

			StrFilter += " AND emp_active = 1"
					+ " AND emp_id != 1";

			if (finnotgiven.equals("yes")) {
				StrFilter += " AND so_finstatus_id = 0";
			}
			if (fingiven.equals("yes")) {
				StrFilter += " AND so_finstatus_id != 0";
			}

			if (type.equals("emp")) {
				// SOP("emp");
				StrFilter += " AND emp_id = " + type_id;
				// SOP("StrFilter---" + StrFilter);
			} else if (type.equals("team")) {
				StrFilter += " AND emp_id IN (SELECT teamtrans_emp_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_team_exe WHERE teamtrans_team_id = " + type_id + ") ";
			} else if (type.equals("brand")) {
				StrFilter += " AND branch_brand_id = " + type_id;
			} else if (type.equals("branch")) {
				StrFilter += " AND so_branch_id = " + type_id;
			} else if (type.equals("region")) {
				StrFilter += " AND branch_region_id = " + type_id;
			} else if (type.equals("model")) {
				StrFilter += " AND item_model_id = " + type_id;
			} else if (type.equals("bank")) {
				StrFilter += " AND bank_id = " + type_id;
			} else if (type.equals("finstatus")) {
				SOP("type==1111===" + type);
				StrFilter += " AND so_finstatus_id != 0";
				if (!type_id.equals("")) {
					StrFilter += " AND so_finstatus_id = " + type_id;
				}
			}

			// SOP("Brand_id----" + brand_id);

			// Brand
			if (!brand_id.equals("")) {
				StrFilter += " AND branch_brand_id IN (" + brand_id + ") ";
			}
			// Regions
			if (!region_id.equals("")) {
				StrFilter += " AND branch_region_id IN (" + region_id + ") ";
			}
			// Branch
			if (!branch_id.equals("")) {
				StrFilter += " AND so_branch_id IN (" + branch_id + ") ";
			}
			// Models
			if (!branch_id.equals("")) {
				StrFilter += " AND item_model_id IN (" + model_id + ") ";
			}
			// bank
			if (!bank_id.equals("")) {
				StrFilter += " AND bank_id IN (" + bank_id + ") ";
			}
			// Teams
			if (!team_id.equals("")) {
				SOP("team_id not empty");
				StrFilter += " AND emp_id IN (SELECT teamtrans_emp_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_team_exe WHERE teamtrans_team_id IN (" + team_id + ") )";
			}
			// Sales Consultant
			if (!emp_id.equals("")) {
				StrFilter += " AND emp_id IN (" + emp_id + ") ";
			}
			// start date
			if (!starttime.equals("")) {
				StrFilter += " AND SUBSTR(so_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)";
			}
			// end date
			if (!endtime.equals("")) {
				StrFilter += " AND SUBSTR(so_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)";
			}

			// Fin Status
			// if (!so_finstatus_id.equals("")) {
			// StrFilter += " AND so_finstatus_id IN (" + so_finstatus_id + ") ";
			// } else {
			// StrFilter += " AND so_finstatus_id > 1 ";
			// }
			// StrFilter += " AND so_finstatus_id > 1 ";
			StrFilter += " AND so_active = 1 ";

			// SOP("StrSearch====" + StrFilter);
			SetSession("sostrsql", StrFilter, request);
			response.sendRedirect(response.encodeRedirectURL("../sales/veh-salesorder-list.jsp?smart=yes"));
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
