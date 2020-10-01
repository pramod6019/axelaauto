package axela.sales;
////aJIt 25th july 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Veh_Quote_Add extends Connect {

	public String emp_id = "";
	public String comp_id = "0";
	public String chkPermMsg = "";
	public String next_button = "";
	public static String msg = "";
	public String StrSql = "";
	public String item_name = "", model_name = "", vehstock_ref_no = "";
	public String oppr_branch_id = "0", preownedstock_id = "0";
	public String emp_role_id = "0", enquiry_enquirytype_id = "0";
	public String quote_enquiry_id = "0", quote_stock_id = "0", enquiry_item_id = "0", vehstock_id = "0", vehstock_comm_no = "0";
	public String item_model_id = "0", item_id = "0", enquiry_customer_id = "0", enquiry_branch_id = "0";
	public String customer_address = "", customer_pin = "";
	public String ExeAccess = "", BranchAccess = "", branch_brand_id = "0";
	public String quoteby = "";
	public String txt_vehstock_details = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_sales_quote_add", request, response);
			if (!comp_id.equals("0")) {
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				emp_id = CNumeric(GetSession("emp_id", request));
				ExeAccess = GetSession("ExeAccess", request);
				BranchAccess = GetSession("BranchAccess", request);
				next_button = PadQuotes(request.getParameter("next_button"));
				msg = PadQuotes(request.getParameter("msg"));
				quote_enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
				quote_stock_id = CNumeric(PadQuotes(request.getParameter("quote_stock_id")));

				if (!quote_enquiry_id.equals("0")) {
					EnquiryDetails(response);
				}
				if (!quote_stock_id.equals("0")) {
					VehstockDetails(response);
				}

				if (!enquiry_customer_id.equals("0") && !branch_brand_id.equals("60")) {
					StrSql = "SELECT customer_address, customer_pin"
							+ " FROM " + compdb(comp_id) + "axela_customer"
							+ " WHERE customer_id = " + enquiry_customer_id;
					CachedRowSet crs = processQuery(StrSql, 0);
					while (crs.next()) {
						customer_address = crs.getString("customer_address");
						customer_pin = crs.getString("customer_pin");
					}
					if (customer_address.equals("") || customer_pin.equals("")) {
						if (customer_address.equals("")) {
							msg += "<br> Update Customer Address! <br>";
						}
						if (customer_pin.equals("")) {
							msg += "<br> Update Customer Pin!";
						}
						response.sendRedirect("../portal/error.jsp?msg=" + msg);
					}
					crs.close();
				}

				if (next_button.equals("Next")) {
					GetValues(request, response);
					CheckForm();
					if (msg.equals("")) {
						if (enquiry_enquirytype_id.equals("1")) {
							if (branch_brand_id.equals("56")) {
								response.sendRedirect("veh-quote-update-new.jsp?add=yes&item_id=" + item_id + "&enquiry_id=" + quote_enquiry_id
										+ "&vehstock_id=" + vehstock_id + "&vehstock_comm_no=" + vehstock_comm_no);
							} else {
								response.sendRedirect("veh-quote-update.jsp?add=yes&item_id=" + item_id + "&enquiry_id=" + quote_enquiry_id
										+ "&vehstock_id=" + vehstock_id + "&vehstock_comm_no=" + vehstock_comm_no);
							}

						} else {
							response.sendRedirect("veh-quote-update.jsp?add=yes&item_id=" + item_id + "&enquiry_id=" + quote_enquiry_id + "&preownedstock_id=" + preownedstock_id + "");
						}
					} else {
						msg = "Error!" + msg;
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		next_button = PadQuotes(request.getParameter("button_addby"));
		if (next_button.equals("ADD By ID")) {
			quoteby = PadQuotes(request.getParameter("txt_quote_by"));
			txt_vehstock_details = CNumeric(PadQuotes(request.getParameter("txt_vehstock_details")));

		} else if (next_button.equals("ADD By Variant")) {
			item_model_id = CNumeric(PadQuotes(request.getParameter("dr_item_model_id")));
			item_id = CNumeric(PadQuotes(request.getParameter("dr_item_id")));

		} else if (next_button.equals("ADD By Enquiry")) {
			quote_enquiry_id = CNumeric(PadQuotes(request.getParameter("txt_quote_enquiry_id")));
			vehstock_id = CNumeric(PadQuotes(request.getParameter("quote_stock_id")));

		} else if (next_button.equals("ADD By PreownedStock")) {
			preownedstock_id = CNumeric(PadQuotes(request.getParameter("txt_preowned_stock_id")));
			item_model_id = CNumeric(PadQuotes(request.getParameter("dr_item_model_id")));
			item_id = CNumeric(PadQuotes(request.getParameter("dr_item_id")));
			item_name = PadQuotes(request.getParameter("txt_item_name"));
			model_name = PadQuotes(request.getParameter("txt_model_name"));
		}
	}

	protected void CheckForm() {
		msg = "";
		CachedRowSet crs = null;
		try {
			if (enquiry_enquirytype_id.equals("1")) {
				if (next_button.equals("ADD By ID")) {
					if (!quoteby.equals("")) {
						if (quoteby.equals("Stock ID")) {
							if (!txt_vehstock_details.equals("0")) {
								StrSql = "SELECT vehstock_id, vehstock_comm_no,"
										+ " vehstock_item_id, item_model_id"
										+ " FROM " + compdb(comp_id) + "axela_vehstock"
										+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = vehstock_item_id"
										+ " WHERE 1=1"
										+ " AND vehstock_delstatus_id != 6"
										+ " AND vehstock_id = " + txt_vehstock_details;

								crs = processQuery(StrSql, 0);
								if (crs.isBeforeFirst()) {
									while (crs.next()) {
										vehstock_id = CNumeric(crs.getString("vehstock_id"));
										vehstock_comm_no = CNumeric(crs.getString("vehstock_comm_no"));
										item_id = CNumeric(crs.getString("vehstock_item_id"));
										item_model_id = CNumeric(crs.getString("item_model_id"));

									}
								} else {
									msg += "<br>Invalid Stock ID!";
								}
								crs.close();

							} else {
								msg += "<br>Enter Stock ID!";
							}

						} else if (quoteby.equals("Commission No.")) {
							if (!txt_vehstock_details.equals("0")) {
								StrSql = "SELECT vehstock_id, vehstock_comm_no,"
										+ " vehstock_item_id, item_model_id"
										+ " FROM " + compdb(comp_id) + "axela_vehstock"
										+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = vehstock_item_id"
										+ " WHERE 1=1"
										+ " AND vehstock_delstatus_id != 6"
										+ " AND vehstock_comm_no = " + txt_vehstock_details;
								crs = processQuery(StrSql, 0);
								if (crs.isBeforeFirst()) {
									while (crs.next()) {
										vehstock_id = CNumeric(crs.getString("vehstock_id"));
										vehstock_comm_no = CNumeric(crs.getString("vehstock_comm_no"));
										item_id = CNumeric(crs.getString("vehstock_item_id"));
										item_model_id = CNumeric(crs.getString("item_model_id"));

									}
								} else {
									msg += "<br>Invalid Commission No.!";
								}
								crs.close();
							} else {
								msg += "<br>Enter Commission No.!";
							}

						}
					} else {
						// msg += "<br>Select Stock ID or Commission No.!";
					}
				}

				if (next_button.equals("ADD By Variant")) {
					if (item_model_id.equals("0")) {
						msg += "<br>Select Model!";
					}

					if (item_id.equals("0")) {
						msg += "<br>Select Item!";
					}
				}

				if (next_button.equals("ADD By Enquiry")) {
					if (quote_enquiry_id.equals("0")) {
						msg += "<br>Enter Enqiry ID!";
					} else {
						StrSql = "SELECT enquiry_branch_id, enquiry_status_id, customer_address, customer_pin"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
								+ " WHERE enquiry_id = " + quote_enquiry_id + ""
								+ " AND enquiry_enquirytype_id = 1"
								// + " AND enquiry_status_id != 2"
								+ BranchAccess.replace("branch_id", "enquiry_branch_id") + ""
								+ ExeAccess.replace("emp_id", "enquiry_emp_id") + "";
						// SOP("StrSql----------" + StrSql);
						crs = processQuery(StrSql, 0);

						if (crs.isBeforeFirst()) {
							while (crs.next()) {
								if (!enquiry_branch_id.equals(crs.getString("enquiry_branch_id"))) {
									msg += "<br>Stock branch and Enquiry branch can't be different!";
								}
								if (crs.getString("enquiry_status_id").equals("2")) {
									msg += "<br>SO already present!";
								}
								if (crs.getString("customer_address").equals("") && !branch_brand_id.equals("60")) {
									msg += "<br>Update Customer Address!";
								}
								if (crs.getString("customer_pin").equals("") && !branch_brand_id.equals("60")) {
									msg += "<br>Update Customer Pin!";
								}
							}
						} else {
							msg += "<br>Invalid Enquiry ID!";
						}
						crs.close();
					}

				}
			}

			if ((enquiry_enquirytype_id.equals("2"))) {
				if (!preownedstock_id.equals("0")) {
					StrSql = "SELECT preownedstock_id FROM " + compdb(comp_id) + "axela_preowned_stock"
							+ " WHERE preownedstock_id = " + preownedstock_id + "";
					if (CNumeric(ExecuteQuery(StrSql)).equals("0")) {
						msg += "<br>Invalid Stock ID.!";
					}
				} else {
					msg += "<br>Enter Pre-Owned Stock ID!";
				}

			}

			// if (branch_brand_id.equals("56") && vehstock_id.equals("0") && msg.equals("")) {
			// msg += "<br>Enter Stock ID or Commission No.!";
			// }
			if (!branch_brand_id.equals("60")) {
				msg = msg + new Enquiry_Dash_Methods().CheckEnquiryFields(quote_enquiry_id, branch_brand_id, comp_id);
			}

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void EnquiryDetails(HttpServletResponse response) {
		try {
			StrSql = "SELECT enquiry_enquirytype_id, enquiry_model_id, enquiry_item_id, enquiry_customer_id, "
					+ " enquiry_branch_id, branch_brand_id, COALESCE(item_name,'') AS item_name, model_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id"
					+ " WHERE enquiry_id = " + quote_enquiry_id + ""
					+ BranchAccess.replace("branch_id", "enquiry_branch_id") + ""
					+ ExeAccess.replace("emp_id", "enquiry_emp_id") + "";
			// SOP("StrSql----------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					enquiry_enquirytype_id = crs.getString("enquiry_enquirytype_id");
					enquiry_item_id = crs.getString("enquiry_item_id");
					item_model_id = crs.getString("enquiry_model_id");
					item_name = crs.getString("item_name");
					model_name = crs.getString("model_name");
					enquiry_customer_id = crs.getString("enquiry_customer_id");
					enquiry_branch_id = crs.getString("enquiry_branch_id");
					branch_brand_id = crs.getString("branch_brand_id");
				}
			} else {
				response.sendRedirect("../portal/error.jsp?msg=Invalid Enquiry!");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}

	public void VehstockDetails(HttpServletResponse response) {
		try {
			StrSql = "SELECT item_model_id, item_id,"
					+ " vehstock_comm_no, vehstock_branch_id,"
					+ " branch_brand_id,"
					+ " COALESCE(item_name,'') AS item_name, model_name"
					+ " FROM " + compdb(comp_id) + "axela_vehstock"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = vehstock_branch_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = vehstock_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " WHERE vehstock_id = " + quote_stock_id + "";
			// + BranchAccess.replace("branch_id", "vehstock_branch_id") + "";
			// + ExeAccess.replace("emp_id", "enquiry_emp_id") + "";
			// SOP("StrSql------stock----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					enquiry_enquirytype_id = "1";
					vehstock_comm_no = crs.getString("vehstock_comm_no");
					item_id = crs.getString("item_id");
					item_model_id = crs.getString("item_model_id");
					item_name = crs.getString("item_name");
					model_name = crs.getString("model_name");
					enquiry_branch_id = crs.getString("vehstock_branch_id");
					branch_brand_id = crs.getString("branch_brand_id");
				}
			} else {
				response.sendRedirect("../portal/error.jsp?msg=Invalid Enquiry!");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}
	public String PopulateModel(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " INNER JOIN axela_brand ON brand_id = model_brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = model_brand_id"
					+ " WHERE model_active = 1"
					+ " AND branch_id = " + enquiry_branch_id
					+ " GROUP BY model_id"
					+ " ORDER BY model_name";
			// SOP("Model===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id"));
				Str.append(StrSelectdrop(crs.getString("model_id"), item_model_id));
				Str.append(">").append(crs.getString("model_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateItem(String model_id, String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT item_id, IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS item_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " WHERE item_model_id = " + model_id + ""
					+ " AND item_model_id != 0"
					+ " AND item_type_id = 1"
					+ " AND item_active = 1"
					+ " ORDER BY item_name";
			// SOP("popolate item--------=" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=\"dr_item_id\" class=\"form-control\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("item_id"));
				Str.append(StrSelectdrop(crs.getString("item_id"), enquiry_item_id));
				Str.append(">").append(crs.getString("item_name")).append("</option>\n");
			}
			crs.close();
			Str.append("</select>\n");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateQuoteBy(String quoteby) {

		StringBuilder Str = new StringBuilder();
		Str.append("<select name=\"dr_selectby\" class=\"form-control\">\n");
		Str.append("<option value=\"StockID\" " + StrSelectdrop(quoteby, "StockID") + ">Stock ID</option>\n");
		Str.append("<option value=\"Comm.No\" " + StrSelectdrop(quoteby, "Comm.No") + ">Commission No.</option>\n");
		Str.append("</select>");
		return Str.toString();
	}
}
