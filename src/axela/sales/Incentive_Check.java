package axela.sales;
///divya 4th dec

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Incentive_Check extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String StrHTML = "";
	public String StrSql = "";
	public String name = "";
	public String value = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String band_id = "0", brand_id = "0", deletetarget = "", listincentivetarget = "", brand_name = "", item_id = "";
	public String band_from = "", band_to = "", slab_from = "", slab_to = "", amount = "", add = "", incentivetarget_id = "", incentivevariant_id = "0", msg = "";
	public String month = "", year = "";
	public String from_date = "", to_date = "", deletevariant = "", listincentivevariant = "", model_id = "";
	public String type = "", listtype = "";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			BranchAccess = GetSession("BranchAccess", request);
			ExeAccess = GetSession("ExeAccess", request);
			band_id = CNumeric(PadQuotes(request.getParameter("band_id")));
			brand_id = CNumeric(PadQuotes(request.getParameter("brand_id")));
			emp_id = CNumeric(GetSession("emp_id", request));
			name = PadQuotes(request.getParameter("name"));
			value = PadQuotes(request.getParameter("value"));
			band_from = PadQuotes(request.getParameter("band_from"));
			band_to = PadQuotes(request.getParameter("band_to"));
			slab_from = PadQuotes(request.getParameter("slab_from"));
			slab_to = PadQuotes(request.getParameter("slab_to"));
			from_date = PadQuotes(request.getParameter("from_date"));
			to_date = PadQuotes(request.getParameter("to_date"));
			deletetarget = PadQuotes(request.getParameter("deletetarget"));
			deletevariant = PadQuotes(request.getParameter("deletevariant"));
			listincentivetarget = PadQuotes(request.getParameter("listincentivetarget"));
			listincentivevariant = PadQuotes(request.getParameter("listincentivevariant"));
			type = PadQuotes(request.getParameter("type"));
			listtype = PadQuotes(request.getParameter("listtype"));
			month = CNumeric(PadQuotes(request.getParameter("month")));

			// SOP("slab_from==" + slab_from);
			// SOP("slab_to==" + slab_to);
			// SOP("band_from==" + band_from);
			// SOP("band_to==" + band_to);

			if (month.length() == 1) {
				month = "0" + month;
			}
			year = CNumeric(PadQuotes(request.getParameter("year")));
			incentivevariant_id = CNumeric(PadQuotes(request.getParameter("incentivevariant_id")));
			item_id = CNumeric(PadQuotes(request.getParameter("item_id")));
			model_id = CNumeric(PadQuotes(request.getParameter("model_id")));
			// SOP("brand_id===" + brand_id + "==year===" + year + "==month===" + month);
			// SOP("type==" + type);
			// SOP("listtype==" + listtype);
			if (deletetarget.equals("yes")) {
				StrHTML = new Incentive_By_Target().DeleteIncentive(band_id, brand_id, comp_id);
			}

			if (listincentivetarget.equals("yes")) {
				StrHTML = new Incentive_By_Target().Listdata(brand_id, comp_id, year, month);
			}

			if (type.equals("slabwise")) {
				StrHTML = new Incentive_By_Target().DeleteSlab(band_id, brand_id, comp_id);
			}

			if (listtype.equals("slabwise")) {
				StrHTML = new Incentive_By_Target().SlabWise(brand_id, comp_id, year, month);
			}

			if (type.equals("insurancewise")) {
				StrHTML = new Incentive_By_Target().DeleteInsurance(band_id, brand_id, comp_id);
			}

			if (listtype.equals("insurancewise")) {
				StrHTML = new Incentive_By_Target().InsuranceSlab(brand_id, comp_id, year, month);
			}

			if (type.equals("Finance-Wise")) {
				StrHTML = new Incentive_By_Target().DeleteFinance(band_id, brand_id, comp_id);
			}

			if (listtype.equals("financewise")) {
				StrHTML = new Incentive_By_Target().FinanceSlab(brand_id, comp_id, year, month);
			}

			if (type.equals("accessorieswise")) {
				StrHTML = new Incentive_By_Target().DeleteAccessories(band_id, brand_id, comp_id);
			}

			if (listtype.equals("accessorieswise")) {
				StrHTML = new Incentive_By_Target().AccessoriesSlab(brand_id, comp_id, year, month);
			}

			if (type.equals("ewwise")) {
				StrHTML = new Incentive_By_Target().DeleteEW(band_id, brand_id, comp_id);
			}

			if (listtype.equals("ewwise")) {
				StrHTML = new Incentive_By_Target().EWSlab(brand_id, comp_id, year, month);
			}

			if (type.equals("exchangewise")) {
				StrHTML = new Incentive_By_Target().DeleteExchange(band_id, brand_id, comp_id);
			}

			if (listtype.equals("exchangewise")) {
				StrHTML = new Incentive_By_Target().ExchangeSlab(brand_id, comp_id, year, month);
			}

			if (deletevariant.equals("yes")) {
				// SOP("delete variant");
				StrHTML = new Incentive_By_Variant().DeleteIncentive(incentivevariant_id, comp_id);
			}

			if (listincentivevariant.equals("yes")) {
				StrHTML = new Incentive_By_Variant().Listdata(brand_id, model_id, comp_id, year, month);
			}
			// SOP("name==" + name);
			if (name.contains("txt_incentivetargetband_from_")) {

				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");

					StrSql = "SELECT "
							+ " incentivetargetband_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_incentive_target_band"
							+ " INNER JOIN " + compdb(comp_id) + "axela_sales_incentive_target ON  incentivetarget_id = incentivetargetband_incentivetarget_id"
							+ " WHERE 1 = 1"
							+ " AND (incentivetargetband_from <= " + band_from
							+ " AND incentivetargetband_to >= " + band_from + ")"
							+ " OR ( incentivetargetband_from <= " + band_to
							+ " AND incentivetargetband_to >= " + band_to + ")"
							+ " AND incentivetargetband_id !=" + band_id
							+ " AND SUBSTR(incentivetarget_startdate,1,6) =" + (year + month)
							+ " AND incentivetarget_brand_id =" + brand_id;
					// SOP("already==" + StrSql);
					if (ExecuteQuery(StrSql).equals("") || ExecuteQuery(StrSql).equals(band_id)) {
						StrSql = "SELECT "
								+ " incentivetargetband_to"
								+ " FROM " + compdb(comp_id) + "axela_sales_incentive_target_band"
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_incentive_target ON  incentivetarget_id = incentivetargetband_incentivetarget_id"
								+ " WHERE 1 = 1"
								+ " AND incentivetargetband_to > " + value
								+ " AND incentivetargetband_id = " + band_id;
						// SOP("cannot==" + StrSql);
						if (ExecuteQuery(StrSql).equals("")) {
							StrHTML = "Band From cannot be greater than Band To!";
						} else {
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_incentive_target_band"
									+ " SET"
									+ " incentivetargetband_from = '" + value + "'"
									+ " WHERE incentivetargetband_id = " + band_id + "";
							// SOP("StrSql==" + StrSql);
							updateQuery(StrSql);
							StrHTML = StrHTML + "<br>Band From updated!";
						}
					} else {
						StrHTML = "Band Range already present!";
					}
				} else {
					StrHTML = "Enter Band From!";
				}
			} else if (name.contains("txt_incentivetargetband_to_")) {
				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					value = value.replaceAll("%", "");

					StrSql = "SELECT "
							+ " incentivetargetband_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_incentive_target_band"
							+ " INNER JOIN " + compdb(comp_id) + "axela_sales_incentive_target ON  incentivetarget_id = incentivetargetband_incentivetarget_id"
							+ " WHERE 1 = 1"
							+ " AND (incentivetargetband_from <= " + band_from
							+ " AND incentivetargetband_to >= " + band_from + ")"
							+ " OR ( incentivetargetband_from <= " + band_to
							+ " AND incentivetargetband_to >= " + band_to + ")"
							+ " AND incentivetargetband_id !=" + band_id
							+ " AND SUBSTR(incentivetarget_startdate,1,6) =" + (year + month)
							+ " AND incentivetarget_brand_id =" + brand_id;
					// SOP("already==" + StrSql);
					if (ExecuteQuery(StrSql).equals("") || ExecuteQuery(StrSql).equals(band_id)) {
						StrSql = "SELECT "
								+ " incentivetargetband_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_incentive_target_band"
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_incentive_target ON  incentivetarget_id = incentivetargetband_incentivetarget_id"
								+ " WHERE 1 = 1"
								+ " AND incentivetargetband_from < " + value
								+ " AND incentivetargetband_id = " + band_id;
						// SOP("cannot==" + StrSql);
						if (ExecuteQuery(StrSql).equals("")) {
							StrHTML = "Band From cannot be less than Band To!";
						} else {
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_incentive_target_band"
									+ " SET"
									+ " incentivetargetband_to = '" + value + "'"
									+ " WHERE incentivetargetband_id = " + band_id + "";
							// SOP("StrSql==" + StrSql);
							updateQuery(StrSql);
							StrHTML = StrHTML + "<br>Band From updated!";
						}
					} else {
						StrHTML = "Band Range already present!";
					}
				} else {
					StrHTML = "Enter Band To!";
				}
			} else if (name.contains("txt_incentivetargetband_amount_")) {
				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					value = value.replaceAll(",", "");
					StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_incentive_target_band"
							+ " SET"
							+ " incentivetargetband_amount = '" + value + "'"
							+ " WHERE incentivetargetband_id = " + band_id + "";
					// SOP("StrSql==" + StrSql);
					updateQuery(StrSql);
					StrHTML = StrHTML + "<br>Amount updated!";
				} else {
					StrHTML = "Enter Amount!";
				}
			}
			// starting
			// slab from
			else if (name.contains("txt_incentiveslab_from_")) {

				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");

					StrSql = "SELECT "
							+ " incentiveslab_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab"
							+ " WHERE 1 = 1"
							+ " AND (incentiveslab_from <= " + slab_from
							+ " AND incentiveslab_to >= " + slab_from + ")"
							+ " OR ( incentiveslab_from <= " + slab_to
							+ " AND incentiveslab_to >= " + slab_to + ")"
							+ " AND incentiveslab_id !=" + band_id
							+ " AND SUBSTR(incentiveslab_date,1,6) =" + (year + month)
							+ " AND incentiveslab_brand_id =" + brand_id;
					// SOP("already=slab=" + StrSql);
					if (ExecuteQuery(StrSql).equals("") || ExecuteQuery(StrSql).equals(band_id)) {
						StrSql = "SELECT "
								+ " incentiveslab_to"
								+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab"
								+ " WHERE 1 = 1"
								+ " AND incentiveslab_to > " + value
								+ " AND incentiveslab_id = " + band_id;
						// SOP("cannot=slab=" + StrSql);
						if (ExecuteQuery(StrSql).equals("")) {
							StrHTML = "Slab From cannot be greater than Slab To!";
						} else {
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_incentive_slab"
									+ " SET"
									+ " incentiveslab_from = '" + value + "'"
									+ " WHERE incentiveslab_id = " + band_id + "";
							// SOP("StrSql==" + StrSql);
							updateQuery(StrSql);
							StrHTML = StrHTML + "<br>Slab From updated!";
						}
					} else {
						StrHTML = "Slab Range already present!";
					}
				} else {
					StrHTML = "Enter Slab From!";
				}

			}
			// slab to
			else if (name.contains("txt_incentiveslab_to_")) {
				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");

					StrSql = "SELECT "
							+ " incentiveslab_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab"
							+ " WHERE 1 = 1"
							+ " AND (incentiveslab_from <= " + slab_from
							+ " AND incentiveslab_to >= " + slab_from + ")"
							+ " OR ( incentiveslab_from <= " + slab_to
							+ " AND incentiveslab_to >= " + slab_to + ")"
							+ " AND incentiveslab_id !=" + band_id
							+ " AND SUBSTR(incentiveslab_date,1,6) =" + (year + month)
							+ " AND incentiveslab_brand_id =" + brand_id;
					// SOP("already=slab=" + StrSql);
					if (ExecuteQuery(StrSql).equals("") || ExecuteQuery(StrSql).equals(band_id)) {
						StrSql = "SELECT "
								+ " incentiveslab_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab"
								+ " WHERE 1 = 1"
								+ " AND incentiveslab_from < " + value
								+ " AND incentiveslab_id = " + band_id;
						// SOP("cannot=slab=" + StrSql);
						if (ExecuteQuery(StrSql).equals("")) {
							StrHTML = "Slab From cannot be less than Slab To!";
						} else {
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_incentive_slab"
									+ " SET"
									+ " incentiveslab_to = '" + value + "'"
									+ " WHERE incentiveslab_id = " + band_id + "";
							// SOP("StrSql==" + StrSql);
							updateQuery(StrSql);
							StrHTML = StrHTML + "<br>Slab To updated!";
						}
					} else {
						StrHTML = "Slab Range already present!";
					}
				} else {
					StrHTML = "Enter Slab To!";
				}
			}
			// so amount
			else if (name.contains("txt_incentiveslab_soamount_")) {
				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					value = value.replaceAll(",", "");
					StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_incentive_slab"
							+ " SET"
							+ " incentiveslab_soamt = '" + value + "'"
							+ " WHERE incentiveslab_id = " + band_id + "";
					// SOP("StrSql==" + StrSql);
					updateQuery(StrSql);
					StrHTML = StrHTML + "<br>SO Amount updated!";
				} else {
					StrHTML = "Enter SO Amount!";
				}
			}
			// finance amount
			else if (name.contains("txt_incentiveslab_finamount_")) {
				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					value = value.replaceAll(",", "");
					StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_incentive_slab"
							+ " SET"
							+ " incentiveslab_financeamt = '" + value + "'"
							+ " WHERE incentiveslab_id = " + band_id + "";
					// SOP("StrSql==" + StrSql);
					updateQuery(StrSql);
					StrHTML = StrHTML + "<br>Finance Amount updated!";
				} else {
					StrHTML = "Enter Finance Amount!";
				}
			}
			// insur amount
			else if (name.contains("txt_incentiveslab_insuramount_")) {
				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					value = value.replaceAll(",", "");
					StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_incentive_slab"
							+ " SET"
							+ " incentiveslab_insuramt = '" + value + "'"
							+ " WHERE incentiveslab_id = " + band_id + "";
					// SOP("StrSql==" + StrSql);
					updateQuery(StrSql);
					StrHTML = StrHTML + "<br>Insurance Amount updated!";
				} else {
					StrHTML = "Enter Insurance Amount!";
				}
			}
			// Ext. Warranty amount
			else if (name.contains("txt_incentiveslab_ewamount_")) {
				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					value = value.replaceAll(",", "");
					StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_incentive_slab"
							+ " SET"
							+ " incentiveslab_ewamt = '" + value + "'"
							+ " WHERE incentiveslab_id = " + band_id + "";
					// SOP("StrSql==" + StrSql);
					updateQuery(StrSql);
					StrHTML = StrHTML + "<br>Ext. Warranty Amount updated!";
				} else {
					StrHTML = "Enter Ext. Warranty Amount!";
				}
			}
			// Accessories min amount
			else if (name.contains("txt_incentiveslab_accessminamount_")) {
				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					value = value.replaceAll(",", "");
					StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_incentive_slab"
							+ " SET"
							+ " incentiveslab_accessmin = '" + value + "'"
							+ " WHERE incentiveslab_brand_id = " + brand_id
							+ " AND SUBSTR(incentiveslab_date,1,6) = " + (year + month);
					// SOP("StrSql==" + StrSql);
					updateQuery(StrSql);
					StrHTML = StrHTML + "<br>Accessories Minimum Amount updated!";
				} else {
					StrHTML = "Enter Accessories Minimum Amount!";
				}
			}
			// Accessories amount
			else if (name.contains("txt_incentiveslab_accessamount_")) {
				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					value = value.replaceAll(",", "");
					StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_incentive_slab"
							+ " SET"
							+ " incentiveslab_accessamt = '" + value + "'"
							+ " WHERE incentiveslab_id = " + band_id + "";
					// SOP("StrSql==" + StrSql);
					updateQuery(StrSql);
					StrHTML = StrHTML + "<br>Accessories Amount updated!";
				} else {
					StrHTML = "Enter Accessories Amount!";
				}
			}
			// Exchange amount
			else if (name.contains("txt_incentiveslab_exchangeamount_")) {
				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					value = value.replaceAll(",", "");
					StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_incentive_slab"
							+ " SET"
							+ " incentiveslab_exchangeamt = '" + value + "'"
							+ " WHERE incentiveslab_id = " + band_id + "";
					// SOP("StrSql==" + StrSql);
					updateQuery(StrSql);
					StrHTML = StrHTML + "<br>Exchange Amount updated!";
				} else {
					StrHTML = "Enter Exchange Amount!";
				}
			}
			// insur slab from
			else if (name.contains("txt_incentiveinsurslab_from_")) {

				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					value = value.replaceAll("%", "");

					StrSql = "SELECT "
							+ " incentiveinsurslab_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_insur"
							+ " WHERE 1 = 1"
							+ " AND (incentiveinsurslab_from <= " + slab_from
							+ " AND incentiveinsurslab_to >= " + slab_from + ")"
							+ " OR ( incentiveinsurslab_from <= " + slab_to
							+ " AND incentiveinsurslab_to >= " + slab_to + ")"
							+ " AND incentiveinsurslab_id !=" + band_id
							+ " AND SUBSTR(incentiveinsurslab_date,1,6) =" + (year + month)
							+ " AND incentiveinsurslab_brand_id =" + brand_id;
					SOP("already=slab=" + StrSql);
					if (ExecuteQuery(StrSql).equals("") || ExecuteQuery(StrSql).equals(band_id)) {
						StrSql = "SELECT "
								+ " incentiveinsurslab_to"
								+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_insur"
								+ " WHERE 1 = 1"
								+ " AND incentiveinsurslab_to > " + value
								+ " AND incentiveinsurslab_id = " + band_id;
						// SOP("cannot=slab=" + StrSql);
						if (ExecuteQuery(StrSql).equals("")) {
							StrHTML = "Slab From cannot be greater than Slab To!";
						} else {
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_incentive_slab_insur"
									+ " SET"
									+ " incentiveinsurslab_from = '" + value + "'"
									+ " WHERE incentiveinsurslab_id = " + band_id + "";
							// SOP("StrSql==" + StrSql);
							updateQuery(StrSql);
							StrHTML = StrHTML + "<br>Slab From updated!";
						}
					} else {
						StrHTML = "Slab Range already present!";
					}
				} else {
					StrHTML = "Enter Slab From!";
				}
			}
			// insur slab to
			else if (name.contains("txt_incentiveinsurslab_to_")) {
				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					value = value.replaceAll("%", "");

					StrSql = "SELECT "
							+ " incentiveinsurslab_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_insur"
							+ " WHERE 1 = 1"
							+ " AND (incentiveinsurslab_from <= " + slab_from
							+ " AND incentiveinsurslab_to >= " + slab_from + ")"
							+ " OR ( incentiveinsurslab_from <= " + slab_to
							+ " AND incentiveinsurslab_to >= " + slab_to + ")"
							+ " AND incentiveinsurslab_id !=" + band_id
							+ " AND SUBSTR(incentiveinsurslab_date,1,6) =" + (year + month)
							+ " AND incentiveinsurslab_brand_id =" + brand_id;
					SOP("already=slab=" + StrSql);
					if (ExecuteQuery(StrSql).equals("") || ExecuteQuery(StrSql).equals(band_id)) {
						StrSql = "SELECT "
								+ " incentiveinsurslab_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_insur"
								+ " WHERE 1 = 1"
								+ " AND incentiveinsurslab_from < " + value
								+ " AND incentiveinsurslab_id = " + band_id;
						// SOP("cannot=slab=" + StrSql);
						if (ExecuteQuery(StrSql).equals("")) {
							StrHTML = "Slab From cannot be less than Slab To!";
						} else {
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_incentive_slab_insur"
									+ " SET"
									+ " incentiveinsurslab_to = '" + value + "'"
									+ " WHERE incentiveinsurslab_id = " + band_id + "";
							// SOP("StrSql==" + StrSql);
							updateQuery(StrSql);
							StrHTML = StrHTML + "<br>Slab To updated!";
						}
					} else {
						StrHTML = "Slab Range already present!";
					}
				} else {
					StrHTML = "Enter Slab To!";
				}
			}
			// Insurance amount
			else if (name.contains("txt_incentiveinsurslab_amount_")) {
				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					value = value.replaceAll(",", "");

					StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_incentive_slab_insur"
							+ " SET"
							+ " incentiveinsurslab_amt = '" + value + "'"
							+ " WHERE incentiveinsurslab_id = " + band_id + "";
					// SOP("StrSql==" + StrSql);
					updateQuery(StrSql);
					StrHTML = StrHTML + "<br>Insurance Amount updated!";
				} else {
					StrHTML = "Enter Insurance Amount!";
				}
			}
			// Finance slab from
			else if (name.contains("txt_incentivefinanceslab_from_")) {

				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					value = value.replaceAll(",", "");

					StrSql = "SELECT "
							+ " incentivefinanceslab_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_finance"
							+ " WHERE 1 = 1"
							+ " AND (incentivefinanceslab_from <= " + slab_from
							+ " AND incentivefinanceslab_to >= " + slab_from + ")"
							+ " OR ( incentivefinanceslab_from <= " + slab_to
							+ " AND incentivefinanceslab_to >= " + slab_to + ")"
							+ " AND incentivefinanceslab_id !=" + band_id
							+ " AND SUBSTR(incentivefinanceslab_date,1,6) =" + (year + month)
							+ " AND incentivefinanceslab_brand_id =" + brand_id;
					// SOP("already=slab=" + StrSql);
					if (ExecuteQuery(StrSql).equals("") || ExecuteQuery(StrSql).equals(band_id)) {
						StrSql = "SELECT "
								+ " incentivefinanceslab_to"
								+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_finance"
								+ " WHERE 1 = 1"
								+ " AND incentivefinanceslab_to > " + value
								+ " AND incentivefinanceslab_id = " + band_id;
						// SOP("cannot=slab=" + StrSql);
						if (ExecuteQuery(StrSql).equals("")) {
							StrHTML = "Slab From cannot be greater than Slab To!";
						} else {
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_incentive_slab_finance"
									+ " SET"
									+ " incentivefinanceslab_from = '" + value + "'"
									+ " WHERE incentivefinanceslab_id = " + band_id + "";
							// SOP("StrSql==" + StrSql);
							updateQuery(StrSql);
							StrHTML = StrHTML + "<br>Slab From updated!";
						}
					} else {
						StrHTML = "Slab Range already present!";
					}
				} else {
					StrHTML = "Enter Slab From!";
				}
			}
			// Finance slab to
			else if (name.contains("txt_incentivefinanceslab_to_")) {
				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					value = value.replaceAll(",", "");

					StrSql = "SELECT "
							+ " incentivefinanceslab_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_finance"
							+ " WHERE 1 = 1"
							+ " AND (incentivefinanceslab_from <= " + slab_from
							+ " AND incentivefinanceslab_to >= " + slab_from + ")"
							+ " OR ( incentivefinanceslab_from <= " + slab_to
							+ " AND incentivefinanceslab_to >= " + slab_to + ")"
							+ " AND incentivefinanceslab_id !=" + band_id
							+ " AND SUBSTR(incentivefinanceslab_date,1,6) =" + (year + month)
							+ " AND incentivefinanceslab_brand_id =" + brand_id;
					// SOP("already=slab=" + StrSql);
					if (ExecuteQuery(StrSql).equals("") || ExecuteQuery(StrSql).equals(band_id)) {
						StrSql = "SELECT "
								+ " incentivefinanceslab_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_finance"
								+ " WHERE 1 = 1"
								+ " AND incentivefinanceslab_from < " + value
								+ " AND incentivefinanceslab_id = " + band_id;
						// SOP("cannot=slab=" + StrSql);
						if (ExecuteQuery(StrSql).equals("")) {
							StrHTML = "Slab From cannot be less than Slab To!";
						} else {
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_incentive_slab_finance"
									+ " SET"
									+ " incentivefinanceslab_to = '" + value + "'"
									+ " WHERE incentivefinanceslab_id = " + band_id + "";
							// SOP("StrSql==" + StrSql);
							updateQuery(StrSql);
							StrHTML = StrHTML + "<br>Slab To updated!";
						}
					} else {
						StrHTML = "Slab Range already present!";
					}
				} else {
					StrHTML = "Enter Slab To!";
				}
			}
			// Finance Amount
			else if (name.contains("txt_incentivefinanceslab_amt_")) {
				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					value = value.replaceAll(",", "");

					StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_incentive_slab_finance"
							+ " SET"
							+ " incentivefinanceslab_amt = '" + value + "'"
							+ " WHERE incentivefinanceslab_id = " + band_id + "";
					// SOP("StrSql==" + StrSql);
					updateQuery(StrSql);
					StrHTML = StrHTML + "<br>Finance Amount updated!";
				} else {
					StrHTML = "Enter Finance Amount!";
				}
			}
			// Accessories slab from
			else if (name.contains("txt_incentiveaccesslab_from_")) {

				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					value = value.replaceAll(",", "");

					StrSql = "SELECT "
							+ " incentiveaccesslab_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_accessories"
							+ " WHERE 1 = 1"
							+ " AND (incentiveaccesslab_from <= " + slab_from
							+ " AND incentiveaccesslab_to >= " + slab_from + ")"
							+ " OR ( incentiveaccesslab_from <= " + slab_to
							+ " AND incentiveaccesslab_to >= " + slab_to + ")"
							+ " AND incentiveaccesslab_id !=" + band_id
							+ " AND SUBSTR(incentiveaccesslab_date,1,6) =" + (year + month)
							+ " AND incentiveaccesslab_brand_id =" + brand_id;
					// SOP("already=slab=" + StrSql);
					if (ExecuteQuery(StrSql).equals("") || ExecuteQuery(StrSql).equals(band_id)) {
						StrSql = "SELECT "
								+ " incentiveaccesslab_to"
								+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_accessories"
								+ " WHERE 1 = 1"
								+ " AND incentiveaccesslab_to > " + value
								+ " AND incentiveaccesslab_id = " + band_id;
						// SOP("cannot=slab=" + StrSql);
						if (ExecuteQuery(StrSql).equals("")) {
							StrHTML = "Slab From cannot be greater than Slab To!";
						} else {
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_incentive_slab_accessories"
									+ " SET"
									+ " incentiveaccesslab_from = '" + value + "'"
									+ " WHERE incentiveaccesslab_id = " + band_id + "";
							// SOP("StrSql==" + StrSql);
							updateQuery(StrSql);
							StrHTML = StrHTML + "<br>Slab From updated!";
						}
					} else {
						StrHTML = "Slab Range already present!";
					}
				} else {
					StrHTML = "Enter Slab From!";
				}
			}
			// Accessories slab to
			else if (name.contains("txt_incentiveaccesslab_to_")) {
				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					value = value.replaceAll(",", "");

					StrSql = "SELECT "
							+ " incentiveaccesslab_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_accessories"
							+ " WHERE 1 = 1"
							+ " AND (incentiveaccesslab_from <= " + slab_from
							+ " AND incentiveaccesslab_to >= " + slab_from + ")"
							+ " OR ( incentiveaccesslab_from <= " + slab_to
							+ " AND incentiveaccesslab_to >= " + slab_to + ")"
							+ " AND incentiveaccesslab_id !=" + band_id
							+ " AND SUBSTR(incentiveaccesslab_date,1,6) =" + (year + month)
							+ " AND incentiveaccesslab_brand_id =" + brand_id;
					// SOP("already=slab=" + StrSql);
					if (ExecuteQuery(StrSql).equals("") || ExecuteQuery(StrSql).equals(band_id)) {
						StrSql = "SELECT "
								+ " incentiveaccesslab_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_accessories"
								+ " WHERE 1 = 1"
								+ " AND incentiveaccesslab_from < " + value
								+ " AND incentiveaccesslab_id = " + band_id;
						// SOP("cannot=slab=" + StrSql);
						if (ExecuteQuery(StrSql).equals("")) {
							StrHTML = "Slab From cannot be less than Slab To!";
						} else {
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_incentive_slab_accessories"
									+ " SET"
									+ " incentiveaccesslab_to = '" + value + "'"
									+ " WHERE incentiveaccesslab_id = " + band_id + "";
							// SOP("StrSql==" + StrSql);
							updateQuery(StrSql);
							StrHTML = StrHTML + "<br>Slab To updated!";
						}
					} else {
						StrHTML = "Slab Range already present!";
					}
				} else {
					StrHTML = "Enter Slab To!";
				}
			}
			// Accessories percentage
			else if (name.contains("txt_incentiveaccesslab_perc_")) {
				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					value = value.replaceAll("%", "");
					StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_incentive_slab_accessories"
							+ " SET"
							+ " incentiveaccesslab_perc = '" + value + "'"
							+ " WHERE incentiveaccesslab_id = " + band_id + "";
					// SOP("StrSql==" + StrSql);
					updateQuery(StrSql);
					StrHTML = StrHTML + "<br>Accessories Percentage updated!";
				} else {
					StrHTML = "Enter Accessories Percentage!";
				}
			}
			// Extended Warranty slab from
			else if (name.contains("txt_incentiveewslab_from_")) {

				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					value = value.replaceAll("%", "");

					StrSql = "SELECT "
							+ " incentiveewslab_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_ew"
							+ " WHERE 1 = 1"
							+ " AND (incentiveewslab_from <= " + slab_from
							+ " AND incentiveewslab_to >= " + slab_from + ")"
							+ " OR ( incentiveewslab_from <= " + slab_to
							+ " AND incentiveewslab_to >= " + slab_to + ")"
							+ " AND incentiveewslab_id !=" + band_id
							+ " AND SUBSTR(incentiveewslab_date,1,6) =" + (year + month)
							+ " AND incentiveewslab_brand_id =" + brand_id;
					// SOP("already=slab=" + StrSql);
					if (ExecuteQuery(StrSql).equals("") || ExecuteQuery(StrSql).equals(band_id)) {
						StrSql = "SELECT "
								+ " incentiveewslab_to"
								+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_ew"
								+ " WHERE 1 = 1"
								+ " AND incentiveewslab_to > " + value
								+ " AND incentiveewslab_id = " + band_id;
						// SOP("cannot=slab=" + StrSql);
						if (ExecuteQuery(StrSql).equals("")) {
							StrHTML = "Slab From cannot be greater than Slab To!";
						} else {
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_incentive_slab_ew"
									+ " SET"
									+ " incentiveewslab_from = '" + value + "'"
									+ " WHERE incentiveewslab_id = " + band_id + "";
							// SOP("StrSql==" + StrSql);
							updateQuery(StrSql);
							StrHTML = StrHTML + "<br>Slab From updated!";
						}
					} else {
						StrHTML = "Slab Range already present!";
					}
				} else {
					StrHTML = "Enter Slab From!";
				}
			}
			// Extended Warranty slab to
			else if (name.contains("txt_incentiveewslab_to_")) {
				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					value = value.replaceAll("%", "");

					StrSql = "SELECT "
							+ " incentiveewslab_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_ew"
							+ " WHERE 1 = 1"
							+ " AND (incentiveewslab_from <= " + slab_from
							+ " AND incentiveewslab_to >= " + slab_from + ")"
							+ " OR ( incentiveewslab_from <= " + slab_to
							+ " AND incentiveewslab_to >= " + slab_to + ")"
							+ " AND incentiveewslab_id !=" + band_id
							+ " AND SUBSTR(incentiveewslab_date,1,6) =" + (year + month)
							+ " AND incentiveewslab_brand_id =" + brand_id;
					// SOP("already=slab=" + StrSql);
					if (ExecuteQuery(StrSql).equals("") || ExecuteQuery(StrSql).equals(band_id)) {
						StrSql = "SELECT "
								+ " incentiveewslab_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_ew"
								+ " WHERE 1 = 1"
								+ " AND incentiveewslab_from < " + value
								+ " AND incentiveewslab_id = " + band_id;
						// SOP("cannot=slab=" + StrSql);
						if (ExecuteQuery(StrSql).equals("")) {
							StrHTML = "Slab From cannot be less than Slab To!";
						} else {
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_incentive_slab_ew"
									+ " SET"
									+ " incentiveewslab_to = '" + value + "'"
									+ " WHERE incentiveewslab_id = " + band_id + "";
							// SOP("StrSql==" + StrSql);
							updateQuery(StrSql);
							StrHTML = StrHTML + "<br>Slab To updated!";
						}
					} else {
						StrHTML = "Slab Range already present!";
					}
				} else {
					StrHTML = "Enter Slab To!";
				}
			}
			// Extended Warranty amount
			else if (name.contains("txt_incentiveewslab_amount_")) {
				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					value = value.replaceAll(",", "");
					StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_incentive_slab_ew"
							+ " SET"
							+ " incentiveewslab_amt = '" + value + "'"
							+ " WHERE incentiveewslab_id = " + band_id + "";
					// SOP("StrSql==" + StrSql);
					updateQuery(StrSql);
					StrHTML = StrHTML + "<br>Extended Warranty Amount updated!";
				} else {
					StrHTML = "Enter Extended Warranty Amount!";
				}
			}
			// Exchange slab from
			else if (name.contains("txt_incentiveexchangeslab_from_")) {

				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					value = value.replaceAll("%", "");

					StrSql = "SELECT "
							+ " incentiveexchangeslab_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_exchange"
							+ " WHERE 1 = 1"
							+ " AND (incentiveexchangeslab_from <= " + slab_from
							+ " AND incentiveexchangeslab_to >= " + slab_from + ")"
							+ " OR ( incentiveexchangeslab_from <= " + slab_to
							+ " AND incentiveexchangeslab_to >= " + slab_to + ")"
							+ " AND incentiveexchangeslab_id !=" + band_id
							+ " AND SUBSTR(incentiveexchangeslab_date,1,6) =" + (year + month)
							+ " AND incentiveexchangeslab_brand_id =" + brand_id;
					// SOP("already=slab=" + StrSql);
					if (ExecuteQuery(StrSql).equals("") || ExecuteQuery(StrSql).equals(band_id)) {
						StrSql = "SELECT "
								+ " incentiveexchangeslab_to"
								+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_exchange"
								+ " WHERE 1 = 1"
								+ " AND incentiveexchangeslab_to > " + value
								+ " AND incentiveexchangeslab_id = " + band_id;
						// SOP("cannot=slab=" + StrSql);
						if (ExecuteQuery(StrSql).equals("")) {
							StrHTML = "Slab From cannot be greater than Slab To!";
						} else {
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_incentive_slab_exchange"
									+ " SET"
									+ " incentiveexchangeslab_from = '" + value + "'"
									+ " WHERE incentiveexchangeslab_id = " + band_id + "";
							// SOP("StrSql==" + StrSql);
							updateQuery(StrSql);
							StrHTML = StrHTML + "<br>Slab From updated!";
						}
					} else {
						StrHTML = "Slab Range already present!";
					}
				} else {
					StrHTML = "Enter Slab From!";
				}
			}
			// Exchange slab to
			else if (name.contains("txt_incentiveexchangeslab_to_")) {
				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					value = value.replaceAll("%", "");

					StrSql = "SELECT "
							+ " incentiveexchangeslab_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_exchange"
							+ " WHERE 1 = 1"
							+ " AND (incentiveexchangeslab_from <= " + slab_from
							+ " AND incentiveexchangeslab_to >= " + slab_from + ")"
							+ " OR ( incentiveexchangeslab_from <= " + slab_to
							+ " AND incentiveexchangeslab_to >= " + slab_to + ")"
							+ " AND incentiveexchangeslab_id !=" + band_id
							+ " AND SUBSTR(incentiveexchangeslab_date,1,6) =" + (year + month)
							+ " AND incentiveexchangeslab_brand_id =" + brand_id;
					// SOP("already=slab=" + StrSql);
					if (ExecuteQuery(StrSql).equals("") || ExecuteQuery(StrSql).equals(band_id)) {
						StrSql = "SELECT "
								+ " incentiveexchangeslab_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_exchange"
								+ " WHERE 1 = 1"
								+ " AND incentiveexchangeslab_from < " + value
								+ " AND incentiveexchangeslab_id = " + band_id;
						// SOP("cannot=slab=" + StrSql);
						if (ExecuteQuery(StrSql).equals("")) {
							StrHTML = "Slab From cannot be less than Slab To!";
						} else {
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_incentive_slab_exchange"
									+ " SET"
									+ " incentiveexchangeslab_to = '" + value + "'"
									+ " WHERE incentiveexchangeslab_id = " + band_id + "";
							// SOP("StrSql==" + StrSql);
							updateQuery(StrSql);
							StrHTML = StrHTML + "<br>Slab To updated!";
						}
					} else {
						StrHTML = "Slab Range already present!";
					}
				} else {
					StrHTML = "Enter Slab To!";
				}
			}
			// Exchange amount
			else if (name.contains("txt_incentiveexchangeslab_amount_")) {
				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					value = value.replaceAll(",", "");
					StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_incentive_slab_exchange"
							+ " SET"
							+ " incentiveexchangeslab_amt = '" + value + "'"
							+ " WHERE incentiveexchangeslab_id = " + band_id + "";
					// SOP("StrSql==" + StrSql);
					updateQuery(StrSql);
					StrHTML = StrHTML + "<br>Exchange Amount updated!";
				} else {
					StrHTML = "Enter Exchange Amount!";
				}
			}

			// ending
			else if (name.contains("txt_incentivevariant_startdate_")) {

				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");

					StrSql = "SELECT "
							+ " incentivevariant_item_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_incentive_variant"
							+ " WHERE 1 = 1"
							+ " AND ((incentivevariant_startdate <= " + Long.parseLong(ConvertShortDateToStr(from_date))
							+ " AND incentivevariant_enddate >= " + Long.parseLong(ConvertShortDateToStr(from_date)) + ")"
							+ " OR ( incentivevariant_startdate <= " + Long.parseLong(ConvertShortDateToStr(to_date))
							+ " AND incentivevariant_enddate >= " + Long.parseLong(ConvertShortDateToStr(to_date)) + "))"
							+ " AND incentivevariant_item_id = " + item_id;
					// SOP("already==" + StrSql);
					// SOP("item_id==" + ExecuteQuery(StrSql).contains(item_id));
					if (ExecuteQuery(StrSql).equals("") || ExecuteQuery(StrSql).equals(item_id)) {
						if (Long.parseLong(ConvertShortDateToStr(to_date)) < Long.parseLong(ConvertShortDateToStr(from_date))) {
							StrHTML = "From Date cannot be greater than To Date!";
						} else {
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_incentive_variant"
									+ " SET"
									+ " incentivevariant_startdate = '" + Long.parseLong(ConvertShortDateToStr(value)) + "'"
									+ " WHERE incentivevariant_id = " + incentivevariant_id + "";
							// SOP("StrSql==" + StrSql);
							updateQuery(StrSql);
							StrHTML = StrHTML + "<br>From Date updated!";
						}
					} else {
						StrHTML = "Date Range already present!";
					}
				} else {
					StrHTML = "Enter From Date!";
				}
			} else if (name.contains("txt_incentivevariant_enddate_")) {

				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");

					StrSql = "SELECT "
							+ " incentivevariant_item_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_incentive_variant"
							+ " WHERE 1 = 1"
							+ " AND ((incentivevariant_startdate <= " + Long.parseLong(ConvertShortDateToStr(from_date))
							+ " AND incentivevariant_enddate >= " + Long.parseLong(ConvertShortDateToStr(from_date)) + ")"
							+ " OR ( incentivevariant_startdate <= " + Long.parseLong(ConvertShortDateToStr(to_date))
							+ " AND incentivevariant_enddate >= " + Long.parseLong(ConvertShortDateToStr(to_date)) + "))"
							+ " AND incentivevariant_item_id = " + item_id;
					// SOP("already==" + StrSql);
					// SOP("item_id==" + ExecuteQuery(StrSql).contains(item_id));
					if (ExecuteQuery(StrSql).equals("") || ExecuteQuery(StrSql).equals(item_id)) {
						if (Long.parseLong(ConvertShortDateToStr(to_date)) < Long.parseLong(ConvertShortDateToStr(from_date))) {
							StrHTML = "To Date cannot be less than From Date!";
						} else {
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_incentive_variant"
									+ " SET"
									+ " incentivevariant_enddate = '" + Long.parseLong(ConvertShortDateToStr(value)) + "'"
									+ " WHERE incentivevariant_id = " + incentivevariant_id + "";
							// SOP("StrSql==" + StrSql);
							updateQuery(StrSql);
							StrHTML = StrHTML + "<br>To Date updated!";
						}
					} else {
						StrHTML = "Date Range already present!";
					}
				} else {
					StrHTML = "Enter To Date!";
				}
			} else if (name.contains("txt_incentivevariant_amount_")) {
				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					value = value.replaceAll(",", "");
					StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_incentive_variant"
							+ " SET"
							+ " incentivevariant_amount = '" + value + "'"
							+ " WHERE incentivevariant_id = " + incentivevariant_id + "";
					// SOP("StrSql==" + StrSql);
					updateQuery(StrSql);
					StrHTML = StrHTML + "<br>Amount updated!";
				} else {
					StrHTML = "Enter Amount!";
				}
			}
		}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
