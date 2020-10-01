package axela.sales;

import javax.sql.rowset.CachedRowSet;

import org.apache.commons.lang3.StringEscapeUtils;

import cloudify.connect.Connect;

public class Enquiry_Dash_Methods extends Connect {
	CachedRowSet crs = null;
	public String enquiry_ford_customertype = "";
	public String enquiry_ford_intentionpurchase = "";
	public String enquiry_ford_kmsdriven = "";
	public String enquiry_ford_newvehfor = "";
	public String enquiry_ford_investment = "";
	public String enquiry_ford_colourofchoice = "";
	public String enquiry_ford_cashorfinance = "";
	public String enquiry_ford_currentcar = "";
	public String enquiry_ford_exchangeoldcar = "";
	public String enquiry_ford_othercarconcideration = "";
	public String enquiry_ford_ex_make = "";
	public String enquiry_ford_ex_model = "";
	public String enquiry_ford_ex_derivative = "";
	public String enquiry_ford_ex_year = "";
	public String enquiry_ford_ex_odoreading = "";
	public String enquiry_ford_ex_doors = "";
	public String enquiry_ford_ex_bodystyle = "";
	public String enquiry_ford_ex_enginesize = "";
	public String enquiry_ford_ex_fueltype = "";
	public String enquiry_ford_ex_drive = "";
	public String enquiry_ford_ex_transmission = "";
	public String enquiry_ford_ex_colour = "";
	public String enquiry_ford_ex_priceoffered = "";
	public String enquiry_ford_ex_estmtprice = "";
	public String emp_all_exe = "";
	String StrSql = "";
	// Start Hyundai Fields
	public String enquiry_hyundai_chooseoneoption = "";
	public String enquiry_hyundai_kmsinamonth = "";
	public String enquiry_hyundai_membersinthefamily = "";
	public String enquiry_hyundai_topexpectation = "";
	public String enquiry_hyundai_finalizenewcar = "";
	public String enquiry_hyundai_modeofpurchase = "";
	public String enquiry_hyundai_annualincome = "";
	public String enquiry_hyundai_othercars = "";
	// // // End Hyundai Fields

	public String c = "0";

	public String PopulateCorporate(String branch_brand_id, String enquiry_corporate_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT corporate_id, corporate_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_corporate"
					+ " WHERE 1=1"
					+ " AND corporate_brand_id = " + branch_brand_id
					+ " AND corporate_active = 1 "
					+ " GROUP BY corporate_id"
					+ " ORDER BY corporate_name";
			// SOP("StrSql--------PopulateCorporate-------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("corporate_id")).append("");
				Str.append(StrSelectdrop(crs.getString("corporate_id"), enquiry_corporate_id));
				Str.append(">").append(crs.getString("corporate_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	// Start maruti methods
	public String PopulateBuyerType(String comp_id, String enquiry_buyertype_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT buyertype_id, buyertype_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_add_buyertype"
					+ " WHERE 1=1" + " ORDER BY buyertype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("buyertype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("buyertype_id"), enquiry_buyertype_id));
				Str.append(">").append(crs.getString("buyertype_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateEnquiryCorporate(String dr_enquiry_corporate, String comp_id) {
		StringBuilder Str = new StringBuilder();
		String corporate = "CSD, Fleet, GEM(DGS&D), WOI_JOL, ACE CLUB, ROYAL CLUB, CORAL CLUB, LEASING,"
				+ " N2N, PREMIUM_LIC&GIC_B2B, LIC B2C & LIC AGENTS, BANKERS DELITE, NAVRATRNA, INDIAN RAILWAYS,"
				+ " IT COMPANIES, TEACHERS, DOCTORS, ADVOCATES, CHARTERD ACCOUNTENTS";

		String[] a = corporate.split(", ");
		try {
			Str.append("<option value=\"\">Select</option>");
			for (int i = 0; i < a.length; i++) {
				Str.append("<option value=\"" + a[i] + "\" " + StrSelectdrop(dr_enquiry_corporate, a[i]) + ">" + a[i] + "</option>");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	// End maruti methods

	// / Start Nexa Methods
	public String PopulateEnquiryNexaGender(String enquiry_nexa_gender, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"Male\" " + StrSelectdrop(enquiry_nexa_gender, "Male") + ">Male</option>");
			Str.append("<option value=\"Female\" " + StrSelectdrop(enquiry_nexa_gender, "Female") + ">Female</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateEnquiryNexaBeveragechoice(String enquiry_nexa_beveragechoice, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"Tea\" " + StrSelectdrop(enquiry_nexa_beveragechoice, "Tea") + ">Tea</option>");
			Str.append("<option value=\"Coffee\" " + StrSelectdrop(enquiry_nexa_beveragechoice, "Coffee") + ">Coffee</option>");
			Str.append("<option value=\"Cold Beverage\" " + StrSelectdrop(enquiry_nexa_beveragechoice, "Cold Beverage") + ">Cold Beverage</option>");
			Str.append("<option value=\"Juice\" " + StrSelectdrop(enquiry_nexa_beveragechoice, "Juice") + ">Juice</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateEnquiryNexaAutocard(String enquiry_nexa_autocard, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"Yes\" " + StrSelectdrop(enquiry_nexa_autocard, "Yes") + ">Yes</option>");
			Str.append("<option value=\"No\" " + StrSelectdrop(enquiry_nexa_autocard, "No") + ">No</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateEnquiryNexaFueltype(String enquiry_nexa_fueltype, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"Petrol\" " + StrSelectdrop(enquiry_nexa_fueltype, "Petrol") + ">Petrol</option>");
			Str.append("<option value=\"Diesel\" " + StrSelectdrop(enquiry_nexa_fueltype, "Diesel") + ">Diesel</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateEnquiryNexaSpecreq(String enquiry_nexa_specreq, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"Comfort\" " + StrSelectdrop(enquiry_nexa_specreq, "Comfort") + ">Comfort</option>");
			Str.append("<option value=\"Storage Space\" " + StrSelectdrop(enquiry_nexa_specreq, "Storage Space") + ">Storage Space</option>");
			Str.append("<option value=\"High Resale Value\" " + StrSelectdrop(enquiry_nexa_specreq, "High Resale Value") + ">High Resale Value</option>");
			Str.append("<option value=\"Buying Cost\" " + StrSelectdrop(enquiry_nexa_specreq, "Buying Cost") + ">Buying Cost</option>");
			Str.append("<option value=\"Features\" " + StrSelectdrop(enquiry_nexa_specreq, "Features") + ">Features</option>");
			Str.append("<option value=\"Looks\" " + StrSelectdrop(enquiry_nexa_specreq, "Looks") + ">Looks</option>");
			Str.append("<option value=\"Mileage\" " + StrSelectdrop(enquiry_nexa_specreq, "Mileage") + ">Mileage</option>");
			Str.append("<option value=\"Low Maintenance Cost\" " + StrSelectdrop(enquiry_nexa_specreq, "Low Maintenance Cost") + ">Low Maintenance Cost</option>");
			Str.append("<option value=\"Performance\" " + StrSelectdrop(enquiry_nexa_specreq, "Performance") + ">Performance</option>");
			Str.append("<option value=\"Safety\" " + StrSelectdrop(enquiry_nexa_specreq, "Safety") + ">Safety</option>");
			Str.append("<option value=\"Sitting Space\" " + StrSelectdrop(enquiry_nexa_specreq, "Sitting Space") + ">Sitting Space</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateEnquiryNexaTestdrivereq(String enquiry_nexa_testdrivereq, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"0\">Select</option>");
			Str.append("<option value=\"1\" " + StrSelectdrop(enquiry_nexa_testdrivereq, "1") + ">Yes</option>");
			Str.append("<option value=\"2\" " + StrSelectdrop(enquiry_nexa_testdrivereq, "2") + ">No</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	// //End Nexa Methods

	// / Start Hyundai Methods
	public String PopulateHyundaiChooseOneOption(String enquiry_hyundai_chooseoneoption, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"First Time Buyer\" " + StrSelectdrop(enquiry_hyundai_chooseoneoption, "First Time Buyer") + ">First Time Buyer</option>");
			Str.append("<option value=\"Exchange Buyer\" " + StrSelectdrop(enquiry_hyundai_chooseoneoption, "Exchange Buyer") + ">Exchange Buyer</option>");
			Str.append("<option value=\"Additional Car Buyer\" " + StrSelectdrop(enquiry_hyundai_chooseoneoption, "Additional Car Buyer") + ">Additional Car Buyer</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateHyundaiKmsInAMonth(String enquiry_hyundai_kmsinamonth, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"500 to 1000\" " + StrSelectdrop(enquiry_hyundai_kmsinamonth, "500 to 1000") + ">500 to 1000</option>");
			Str.append("<option value=\"1000 to 1500\" " + StrSelectdrop(enquiry_hyundai_kmsinamonth, "1000 to 1500") + ">1000 to 1500</option>");
			Str.append("<option value=\"1500 to 2000\" " + StrSelectdrop(enquiry_hyundai_kmsinamonth, "1500 to 2000") + ">1500 to 2000</option>");
			Str.append("<option value=\">2000\" " + StrSelectdrop(enquiry_hyundai_kmsinamonth, ">2000") + ">>2000</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateHyundaiMembersInTheFamily(String enquiry_hyundai_membersinthefamily, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"2\" " + StrSelectdrop(enquiry_hyundai_membersinthefamily, "2") + ">2</option>");
			Str.append("<option value=\"3 to 5\" " + StrSelectdrop(enquiry_hyundai_membersinthefamily, "3 to 5") + ">3 to 5</option>");
			Str.append("<option value=\"6 to 7\" " + StrSelectdrop(enquiry_hyundai_membersinthefamily, "6 to 7") + ">6 to 7</option>");
			Str.append("<option value=\"More Than 7\" " + StrSelectdrop(enquiry_hyundai_membersinthefamily, "More Than 7") + ">More Than 7</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateHyundaiTopExpectation(String enquiry_hyundai_topexpectation, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"Features\" " + StrSelectdrop(enquiry_hyundai_topexpectation, "Features") + ">Features</option>");
			Str.append("<option value=\"Performance\" " + StrSelectdrop(enquiry_hyundai_topexpectation, "Performance") + ">Performance</option>");
			Str.append("<option value=\"Looks\" " + StrSelectdrop(enquiry_hyundai_topexpectation, "Looks") + ">Looks</option>");
			Str.append("<option value=\"Value for Money\" " + StrSelectdrop(enquiry_hyundai_topexpectation, "Value for Money") + ">Value for Money</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateHyundaiFinalizeNewCar(String enquiry_hyundai_finalizenewcar, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"<15 Days (Hot)\"  " + StrSelectdrop(enquiry_hyundai_finalizenewcar, "<15 Days (Hot)") + "><15 Days (Hot)</option>");
			Str.append("<option value=\"16 to 30 Days (Warm)\" " + StrSelectdrop(enquiry_hyundai_finalizenewcar, "16 to 30 Days (Warm)") + ">16 to 30 Days (Warm)</option>");
			Str.append("<option value=\">30 Days (Cold)\" " + StrSelectdrop(enquiry_hyundai_finalizenewcar, ">30 Days (Cold)") + ">>30 Days (Cold)</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateHyundaiModeOfPurchase(String enquiry_hyundai_modeofpurchase, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"Cash\" " + StrSelectdrop(enquiry_hyundai_modeofpurchase, "Cash") + ">Cash</option>");
			Str.append("<option value=\"Finance\" " + StrSelectdrop(enquiry_hyundai_modeofpurchase, "Finance") + ">Finance</option>");
			Str.append("<option value=\"Self Arranged Finance\" " + StrSelectdrop(enquiry_hyundai_modeofpurchase, "Self Arranged Finance") + ">Self Arranged Finance</option>");
			Str.append("<option value=\"Company Finance\" " + StrSelectdrop(enquiry_hyundai_modeofpurchase, "Company Finance") + ">Company Finance</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateHyundaiAnnualIncome(String enquiry_hyundai_annualincome, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"<2.5 Lakhs\" " + StrSelectdrop(enquiry_hyundai_annualincome, "<2.5 Lakhs") + "><2.5 Lakhs</option>");
			Str.append("<option value=\"2.5 Lakhs to 5 Lakhs\" " + StrSelectdrop(enquiry_hyundai_annualincome, "2.5 Lakhs to 5 Lakhs") + ">2.5 Lakhs to 5 Lakhs</option>");
			Str.append("<option value=\"5 Lakhs to 10 Lakhs\" " + StrSelectdrop(enquiry_hyundai_annualincome, "5 Lakhs to 10 Lakhs") + ">5 Lakhs to 10 Lakhs</option>");
			Str.append("<option value=\">10 Lakhs\" " + StrSelectdrop(enquiry_hyundai_annualincome, ">10 Lakhs") + ">>10 Lakhs</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	// Start Ford Methods
	public String PopulateFordCustomerType(String enquiry_ford_customertype, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"Business\" " + StrSelectdrop(enquiry_ford_customertype, "Business") + ">Business</option>");
			Str.append("<option value=\"Federal Govt\" " + StrSelectdrop(enquiry_ford_customertype, "Federal Govt") + ">Federal Govt</option>");
			Str.append("<option value=\"Fleet\" " + StrSelectdrop(enquiry_ford_customertype, "Fleet") + ">Fleet</option>");
			Str.append("<option value=\"Local Govt\" " + StrSelectdrop(enquiry_ford_customertype, "Local Govt") + ">Local Govt</option>");
			Str.append("<option value=\"Not For Proft\" " + StrSelectdrop(enquiry_ford_customertype, "Not For Proft") + ">Not For Proft</option>");
			Str.append("<option value=\"Private\" " + StrSelectdrop(enquiry_ford_customertype, "Private") + ">Private</option>");
			Str.append("<option value=\"Rental\" " + StrSelectdrop(enquiry_ford_customertype, "Rental") + ">Rental</option>");
			Str.append("<option value=\"State Govt\" " + StrSelectdrop(enquiry_ford_customertype, "State Govt") + ">State Govt</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateFordIntentionPurchase(String enquiry_ford_intentionpurchase, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"Own Use\" " + StrSelectdrop(enquiry_ford_intentionpurchase, "Own Use") + ">Own Use</option>");
			Str.append("<option value=\"To Rent\" " + StrSelectdrop(enquiry_ford_intentionpurchase, "To Rent") + ">To Rent</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateFordNewVehFor(String enquiry_ford_newvehfor, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"Self\" " + StrSelectdrop(enquiry_ford_newvehfor, "Self") + ">Self</option>");
			Str.append("<option value=\"Father\" " + StrSelectdrop(enquiry_ford_newvehfor, "Father") + ">Father</option>");
			Str.append("<option value=\"Mother\" " + StrSelectdrop(enquiry_ford_newvehfor, "Mother") + ">Mother</option>");
			Str.append("<option value=\"Daughter\" " + StrSelectdrop(enquiry_ford_newvehfor, "Daughter") + ">Daughter</option>");
			Str.append("<option value=\"Son\" " + StrSelectdrop(enquiry_ford_newvehfor, "Son") + ">Son</option>");
			Str.append("<option value=\"Others\" " + StrSelectdrop(enquiry_ford_newvehfor, "Others") + ">Others</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateFordColourOfChoice(String enquiry_ford_colourofchoice, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"White\" " + StrSelectdrop(enquiry_ford_colourofchoice, "White") + ">White</option>");
			Str.append("<option value=\"Black\" " + StrSelectdrop(enquiry_ford_colourofchoice, "Black") + ">Black</option>");
			Str.append("<option value=\"Red\" " + StrSelectdrop(enquiry_ford_colourofchoice, "Red") + ">Red</option>");
			Str.append("<option value=\"Grey\" " + StrSelectdrop(enquiry_ford_colourofchoice, "Grey") + ">Grey</option>");
			Str.append("<option value=\"Silver\" " + StrSelectdrop(enquiry_ford_colourofchoice, "Silver") + ">Silver</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateFordCashOrFinance(String enquiry_ford_cashorfinance, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"Cash\" " + StrSelectdrop(enquiry_ford_cashorfinance, "Cash") + ">Cash</option>");
			Str.append("<option value=\"Finance\" " + StrSelectdrop(enquiry_ford_cashorfinance, "Finance") + ">Finance</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateFordExchangeOldCar(String enquiry_ford_exchangeoldcar, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"Yes\" " + StrSelectdrop(enquiry_ford_exchangeoldcar, "Yes") + ">Yes</option>");
			Str.append("<option value=\"No\" " + StrSelectdrop(enquiry_ford_exchangeoldcar, "No") + ">No</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateFordExYear(String enquiry_ford_ex_year, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			String curryear1 = SplitYear(ToLongDate(kknow()));
			int i = (Integer.parseInt(curryear1));
			Str.append("<option value=\"\">Select</option>");
			for (int a = i; a > i - 10; a--)
			{
				Str.append("<option value=" + a + " " + StrSelectdrop("" + enquiry_ford_ex_year + "", "" + a + "") + ">" + a + "</option>");
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateFordExDoors(String enquiry_ford_ex_doors, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"2\" " + StrSelectdrop(enquiry_ford_ex_doors, "2") + ">2</option>");
			Str.append("<option value=\"3\" " + StrSelectdrop(enquiry_ford_ex_doors, "3") + ">3</option>");
			Str.append("<option value=\"4\" " + StrSelectdrop(enquiry_ford_ex_doors, "4") + ">4</option>");
			Str.append("<option value=\"5\" " + StrSelectdrop(enquiry_ford_ex_doors, "5") + ">5</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateFordExBodyStyle(String enquiry_ford_ex_bodystyle, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"Hatch\" " + StrSelectdrop(enquiry_ford_ex_bodystyle, "Hatch") + ">Hatch</option>");
			Str.append("<option value=\"MPV\" " + StrSelectdrop(enquiry_ford_ex_bodystyle, "MPV") + ">MPV</option>");
			Str.append("<option value=\"Sedan\" " + StrSelectdrop(enquiry_ford_ex_bodystyle, "Sedan") + ">Sedan</option>");
			Str.append("<option value=\"SUV\" " + StrSelectdrop(enquiry_ford_ex_bodystyle, "SUV") + ">SUV</option>");
			Str.append("<option value=\"UTE\" " + StrSelectdrop(enquiry_ford_ex_bodystyle, "UTE") + ">UTE</option>");
			Str.append("<option value=\"Wagon\" " + StrSelectdrop(enquiry_ford_ex_bodystyle, "Wagon") + ">Wagon</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateFordExFuelType(String enquiry_ford_ex_fueltype, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"Diesel\" " + StrSelectdrop(enquiry_ford_ex_fueltype, "Diesel") + ">Diesel</option>");
			Str.append("<option value=\"Dual Fuel\" " + StrSelectdrop(enquiry_ford_ex_fueltype, "Dual Fuel") + ">Dual Fuel</option>");
			Str.append("<option value=\"Electric\" " + StrSelectdrop(enquiry_ford_ex_fueltype, "Electric") + ">Electric</option>");
			Str.append("<option value=\"Gas\" " + StrSelectdrop(enquiry_ford_ex_fueltype, "Gas") + ">Gas</option>");
			Str.append("<option value=\"Hybrid\" " + StrSelectdrop(enquiry_ford_ex_fueltype, "Hybrid") + ">Hybrid</option>");
			Str.append("<option value=\"Petrol\" " + StrSelectdrop(enquiry_ford_ex_fueltype, "Petrol") + ">Petrol</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateFordExDrive(String enquiry_ford_ex_drive, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"2WD\" " + StrSelectdrop(enquiry_ford_ex_drive, "2WD") + ">2WD</option>");
			Str.append("<option value=\"4WD\" " + StrSelectdrop(enquiry_ford_ex_drive, "4WD") + ">4WD</option>");
			Str.append("<option value=\"AWD\" " + StrSelectdrop(enquiry_ford_ex_drive, "AWD") + ">AWD</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateFordExTransmition(String enquiry_ford_ex_transmission, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"Auto\" " + StrSelectdrop(enquiry_ford_ex_transmission, "Auto") + ">Auto</option>");
			Str.append("<option value=\"Manual\" " + StrSelectdrop(enquiry_ford_ex_transmission, "Manual") + ">Manual</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateFordExColour(String enquiry_ford_ex_colour, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"Black\" " + StrSelectdrop(enquiry_ford_ex_colour, "Black") + ">Black</option>");
			Str.append("<option value=\"Blue\" " + StrSelectdrop(enquiry_ford_ex_colour, "Blue") + ">Blue</option>");
			Str.append("<option value=\"Grey\" " + StrSelectdrop(enquiry_ford_ex_colour, "Grey") + ">Grey</option>");
			Str.append("<option value=\"Red\" " + StrSelectdrop(enquiry_ford_ex_colour, "Red") + ">Red</option>");
			Str.append("<option value=\"White\" " + StrSelectdrop(enquiry_ford_ex_colour, "White") + ">White</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
	// End Ford Methods

	// //Start of MB Methods////////
	public String PopulateCategory(String enquiry_enquirycat_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT enquirycat_id, enquirycat_name"
					+ " FROM axela_sales_enquiry_cat"
					+ " WHERE 1=1"
					+ " GROUP BY enquirycat_id"
					+ " ORDER BY enquirycat_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("enquirycat_id")).append("");
				Str.append(StrSelectdrop(crs.getString("enquirycat_id"), enquiry_enquirycat_id));
				Str.append(">").append(crs.getString("enquirycat_name")).append("</option>\n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateMBOccupation(String enquiry_mb_occupation, String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"\">Select</option>\n");
		Str.append("<option value=\"Business\"")
				.append(StrSelectdrop("Business", enquiry_mb_occupation)).append(">Business</option>\n");
		Str.append("<option value=\"Professional\"")
				.append(StrSelectdrop("Professional", enquiry_mb_occupation)).append(">Professional</option>\n");
		Str.append("<option value=\"Salaried\"")
				.append(StrSelectdrop("Salaried", enquiry_mb_occupation)).append(">Salaried</option>\n");
		Str.append("<option value=\"Self-employed\"")
				.append(StrSelectdrop("Self-employed", enquiry_mb_occupation)).append(">Self-employed</option>\n");
		return Str.toString();
	}

	public String PopulateMBCarUsageConditions(String enquiry_mb_carusage, String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"\">Select</option>\n");
		Str.append("<option value=\"Fleet\"").append(StrSelectdrop("Fleet", enquiry_mb_carusage)).append(">Fleet</option>\n");
		Str.append("<option value=\"Gift\"").append(StrSelectdrop("Gift", enquiry_mb_carusage)).append(">Gift</option>\n");
		Str.append("<option value=\"Official\"").append(StrSelectdrop("Official", enquiry_mb_carusage)).append(">Official</option>\n");
		Str.append("<option value=\"Others\"").append(StrSelectdrop("Others", enquiry_mb_carusage)).append(">Others</option>\n");
		Str.append("<option value=\"Personal\"").append(StrSelectdrop("Personal", enquiry_mb_carusage)).append(">Personal</option>\n");
		return Str.toString();
	}

	public String PopulateMBType(String enquiry_mb_type, String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"\">Select</option>\n");
		Str.append("<option value=\"Corp.\"").append(StrSelectdrop("Corp.", enquiry_mb_type)).append(">Corp.</option>\n");
		Str.append("<option value=\"Fleet\"").append(StrSelectdrop("Fleet", enquiry_mb_type)).append(">Fleet</option>\n");
		Str.append("<option value=\"Govt.\"").append(StrSelectdrop("Govt.", enquiry_mb_type)).append(">Govt.</option>\n");
		Str.append("<option value=\"Individual\"").append(StrSelectdrop("Individual", enquiry_mb_type)).append(">Individual</option>\n");
		Str.append("<option value=\"Others\"").append(StrSelectdrop("Others", enquiry_mb_type)).append(">Others</option>\n");
		return Str.toString();
	}

	public String PopulateMBDrivingPattern(String enquiry_mb_drivingpattern, String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"\">Select</option>\n");
		Str.append("<option value=\"Chauffeur\"").append(StrSelectdrop("Chauffeur", enquiry_mb_drivingpattern)).append(">Chauffeur</option>\n");
		Str.append("<option value=\"Self\"").append(StrSelectdrop("Self", enquiry_mb_drivingpattern)).append(">Self</option>\n");
		return Str.toString();
	}

	public String PopulateMBIncome(String enquiry_mb_income, String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"\">Select</option>\n");
		Str.append("<option value=\"<50 lakhs\"").append(StrSelectdrop(StringEscapeUtils.escapeHtml4("<50 lakhs"), enquiry_mb_income)).append("><50 lakhs</option>\n");
		Str.append("<option value=\"50-70 lakhs\"").append(StrSelectdrop("50-70 lakhs", enquiry_mb_income)).append(">50-70 lakhs</option>\n");
		Str.append("<option value=\">75 lakhs\"").append(StrSelectdrop(StringEscapeUtils.escapeHtml4(">75 lakhs"), enquiry_mb_income)).append(">>75 lakhs</option>\n");
		return Str.toString();
	}

	public String PopulateMBAverageDriving(String enquiry_mb_avgdriving, String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"\">Select</option>\n");
		Str.append("<option value=\"<1000\"").append(StrSelectdrop(StringEscapeUtils.escapeHtml4("<1000"), enquiry_mb_avgdriving)).append("><1000</option>\n");
		Str.append("<option value=\"1000-2000\"").append(StrSelectdrop("1000-2000", enquiry_mb_avgdriving)).append(">1000-2000</option>\n");
		Str.append("<option value=\"2000-4000\"").append(StrSelectdrop("2000-4000", enquiry_mb_avgdriving)).append(">2000-4000</option>\n");
		Str.append("<option value=\">4000\"").append(StrSelectdrop(StringEscapeUtils.escapeHtml4(">4000"), enquiry_mb_avgdriving)).append(">>4000</option>\n");
		return Str.toString();
	}

	public String PopulateMBCurrentCars(String enquiry_mb_currentcars) {
		StringBuilder Str = new StringBuilder();
		String currentcars = "Audi, BMW, Jaguar, Lexus, Others, VW";
		String[] a = currentcars.split(", ");
		try {
			for (int i = 0; i < a.length; i++) {
				String currentcar = a[i];
				Str.append("<input type=checkbox name='currentcars[]' id='" + currentcar + "' value=" + currentcar
						+ " onclick=\"SecurityCheck('dr_enquiry_mb_currentcars',this,'hint_dr_enquiry_mb_currentcars')\"")
						.append(StrArrSelectCheck(enquiry_mb_currentcars, currentcar))
						.append(">").append("&nbsp&nbsp" + a[i] + "&nbsp&nbsp");
			}
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateFollowupStatus(String comp_id, String followup_followupstatus_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT followupstatus_id, followupstatus_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup_status"
					+ " WHERE 1 = 1"
					+ " ORDER BY followupstatus_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {

				Str.append("<option value=").append(crs.getString("followupstatus_id"));
				Str.append(StrSelectdrop(crs.getString("followupstatus_id"), followup_followupstatus_id));
				Str.append(">").append(crs.getString("followupstatus_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
	// //////End of MB Methods

	public String CheckEnquiryFields(String enquiry_id, String brand_id, String comp_id) {
		String msg = "";
		// SOP("brand_id==" + brand_id);
		try {
			String StrSql = "";
			// start maruthi fields check
			if (brand_id.equals("2")) {
				StrSql = " SELECT"
						+ " enquiry_id,"
						+ " enquiry_age_id,"
						+ " enquiry_occ_id,"
						+ " enquiry_custtype_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " WHERE enquiry_id = " + enquiry_id
						+ " GROUP BY enquiry_id";
				// SOP("brand_id=2=" + StrSql);
				crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						if (crs.getString("enquiry_age_id").equals("0")) {
							msg = msg + "<br>Enter Age!";
						}
						if (crs.getString("enquiry_custtype_id").equals("0")) {
							msg = msg + "<br>Enter Type of Customer!";
						}
						if (crs.getString("enquiry_occ_id").equals("0")) {
							msg = msg + "<br>Enter Occupation!";
						}
					}
				}
				crs.close();
			}

			// start nexa fields check
			if (brand_id.equals("10")) {
				StrSql = " SELECT"
						+ " enquiry_id,"
						+ " enquiry_nexa_gender,"
						+ " enquiry_nexa_beveragechoice, "
						+ " enquiry_nexa_autocard,"
						+ " enquiry_nexa_fueltype,"
						+ " enquiry_nexa_specreq,"
						+ " enquiry_nexa_testdrivereq"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " WHERE enquiry_id = " + enquiry_id
						+ " GROUP BY enquiry_id";
				// SOP("brand_id=2=" + StrSql);
				crs = processQuery(StrSql, 0);

				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						if (crs.getString("enquiry_nexa_gender").equals("")) {
							msg = msg + "<br>Enter Gender!";
						}
						if (crs.getString("enquiry_nexa_beveragechoice").equals("")) {
							msg = msg + "<br>Enter Beverage Choice!";
						}
						if (crs.getString("enquiry_nexa_autocard").equals("")) {
							msg = msg + "<br>Enter Autocard!";
						}
						if (crs.getString("enquiry_nexa_fueltype").equals("")) {
							msg = msg + "<br>Enter Fuel Type!";
						}
						if (crs.getString("enquiry_nexa_specreq").equals("")) {
							msg = msg + "<br>Enter Specific requirement!";
						}
						if (crs.getString("enquiry_nexa_testdrivereq").equals("")) {
							msg = msg + "<br>Enter Test Drive Required!";
						}
					}
				}
				crs.close();
			}
			// start maruthi and nexa common fields check
			if (brand_id.equals("2") || brand_id.equals("10")) {
				StrSql = " SELECT"
						+ " enquiry_id,"
						+ " enquiry_buyertype_id,"
						+ " enquiry_monthkms_id,"
						+ " enquiry_purchasemode_id,"
						+ " enquiry_income_id,"
						+ " enquiry_familymember_count,"
						+ " enquiry_expectation_id,"
						+ " enquiry_othercar"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " WHERE enquiry_id = " + enquiry_id
						+ " GROUP BY enquiry_id";
				// SOP("brand_id=2=" + StrSql);
				crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						if (crs.getString("enquiry_buyertype_id").equals("0")) {
							msg = msg + "<br>Enter Buyer Type!";
						}
						if (crs.getString("enquiry_monthkms_id").equals("0")) {
							msg = msg + "<br>Enter Month Kms!";
						}
						if (crs.getString("enquiry_purchasemode_id").equals("0")) {
							msg = msg + "<br>Enter Mode of Purchase!";
						}
						if (crs.getString("enquiry_income_id").equals("0")) {
							msg = msg + "<br>Enter Household Income!";
						}
						if (crs.getString("enquiry_familymember_count").equals("0")) {
							msg = msg + "<br>Enter Count of family members!";
						}
						if (crs.getString("enquiry_expectation_id").equals("0")) {
							msg = msg + "<br>Enter Priority Expectations From the Car!";
						}
						if (crs.getString("enquiry_othercar").equals("0")) {
							msg = msg + "<br>Enter Other Car!";
						}
						if (!msg.equals("")) {
							msg = "<br>Enter Full Customer information!" + msg;
						}
					}
				}
				crs.close();
			}

			// StrSql = " SELECT enquiry_id " + " FROM " + compdb(comp_id) + "axela_sales_enquiry"
			// // + " WHERE enquiry_buyertype_id=3 AND (enquiry_ownership_id=0 OR enquiry_tradein_preownedvariant_id=0 OR enquiry_purchasemonth=''"
			// + " WHERE enquiry_buyertype_id=3 AND (enquiry_ownership_id=0 OR enquiry_purchasemonth=''"
			// // / + " OR enquiry_loancompletionmonth='' "
			// // + " OR enquiry_loanfinancer=''"
			// // + " OR enquiry_kms=0"
			// + " OR enquiry_expectedprice=0"
			// // + " OR enquiry_quotedprice=0"
			// + " ) AND enquiry_id = " + enquiry_id; // //OR
			// // enquiry_currentemi=0
			// if (!ExecuteQuery(StrSql).equals("")) {
			// msg = msg + "<br>Enter full exchange information!";
			// }

			// -----starts Hyundai fields checking------
			if (brand_id.equals("6")) {
				String hyundaimsg = "";
				StrSql = " SELECT enquiry_hyundai_chooseoneoption, enquiry_hyundai_kmsinamonth, enquiry_hyundai_membersinthefamily, "
						+ " enquiry_hyundai_topexpectation, enquiry_hyundai_finalizenewcar, enquiry_hyundai_modeofpurchase, "
						+ " enquiry_hyundai_annualincome, enquiry_hyundai_othercars"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " WHERE 1=1"
						+ " AND enquiry_id = " + enquiry_id
						+ " GROUP BY enquiry_id";
				crs = processQuery(StrSql, 0);
				while (crs.next()) {
					if (crs.getString("enquiry_hyundai_chooseoneoption").equals("")) {
						hyundaimsg += "<br>Select one option!";
					}
					if (crs.getString("enquiry_hyundai_kmsinamonth").equals("")) {
						hyundaimsg += "<br>Select How many kilometers you drive in a month!";
					}
					if (crs.getString("enquiry_hyundai_membersinthefamily").equals("")) {
						hyundaimsg += "<br>Select How many members are there in your family!";
					}
					if (crs.getString("enquiry_hyundai_topexpectation").equals("")) {
						hyundaimsg += "<br>Select What is your top most priority expectation from your car!";
					}
					if (crs.getString("enquiry_hyundai_finalizenewcar").equals("")) {
						hyundaimsg += "<br>Select By when are you expecting to finalize your new car!";
					}
					if (crs.getString("enquiry_hyundai_modeofpurchase").equals("")) {
						hyundaimsg += "<br>Select What will be your mode of purchase!";
					}
					if (crs.getString("enquiry_hyundai_annualincome").equals("")) {
						hyundaimsg += "<br>Select What is your appropriate annual household income!";
					}
					if (crs.getString("enquiry_hyundai_othercars").equals("")) {
						hyundaimsg += "<br>Enter Which other cars are you considering!";
					}

					if (!hyundaimsg.equals("")) {
						msg = msg + "<br>Enter Full Need Assessment Details For Hyundai!" + hyundaimsg;
					}
				}
				crs.close();
				hyundaimsg = "";

			}
			// -----End Hyundai fields checking-----

			// -----Start Ford fields checking------
			if (brand_id.equals("7")) {
				String fordmsg = "";
				StrSql = " SELECT enquiry_ford_customertype, enquiry_ford_intentionpurchase, enquiry_ford_kmsdriven, "
						+ " enquiry_ford_newvehfor, enquiry_ford_investment, enquiry_ford_colourofchoice, enquiry_ford_cashorfinance, "
						+ " enquiry_ford_currentcar, enquiry_ford_exchangeoldcar, enquiry_ford_othercarconcideration  "
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " WHERE 1=1"
						+ " AND enquiry_id = " + enquiry_id
						+ " GROUP BY enquiry_id";
				crs = processQuery(StrSql, 0);
				while (crs.next()) {
					if (crs.getString("enquiry_ford_customertype").equals("")) {
						fordmsg += "<br>Select Type of Customer!";
					}
					if (crs.getString("enquiry_ford_intentionpurchase").equals("")) {
						fordmsg += "<br>Select Intention to purchase!";
					}
					if (crs.getString("enquiry_ford_kmsdriven").equals("0")) {
						fordmsg += "<br>Enter No. of Kms Driven Every Day!";
					}
					if (crs.getString("enquiry_ford_newvehfor").equals("")) {
						fordmsg += "<br>Select New Vehicle for Self OR Someone else!";
					}
					if (crs.getString("enquiry_ford_investment").equals("0")) {
						fordmsg += "<br>Enter Amount of investment in new car!";
					}
					if (crs.getString("enquiry_ford_colourofchoice").equals("")) {
						fordmsg += "<br>Select Any specific colour choice!";
					}
					if (crs.getString("enquiry_ford_cashorfinance").equals("")) {
						fordmsg += "<br>Select Cash / Finance!";
					}
					if (crs.getString("enquiry_ford_currentcar").equals("")) {
						fordmsg += "<br>Enter Which Car you Driving now!";
					}
					if (crs.getString("enquiry_ford_exchangeoldcar").equals("")) {
						fordmsg += "<br>Select Do you want to Exchange your old car!";
					}
					if (crs.getString("enquiry_ford_othercarconcideration").equals("")) {
						fordmsg += "<br>Enter Which Other cars you considering!";
					}

					if (!fordmsg.equals("")) {
						msg = msg + "<br>Enter Full Need Assessment of Customer for Ford!" + fordmsg;
					}

				}
				crs.close();
				fordmsg = "";
				// StrSql = " SELECT enquiry_ford_ex_derivative, enquiry_ford_ex_year,"
				// + " enquiry_ford_ex_odoreading, enquiry_ford_ex_doors, enquiry_ford_ex_bodystyle,"
				// + " enquiry_ford_ex_enginesize, enquiry_ford_ex_fueltype, enquiry_ford_ex_drive,"
				// + " enquiry_ford_ex_transmission, enquiry_ford_ex_colour, enquiry_ford_ex_priceoffered,"
				// + " enquiry_ford_ex_estmtprice,"
				// // + " IF((enquiry_tradein_preownedvariant_id='0' "
				// + " IF(( enquiry_ford_ex_derivative=''"
				// + " AND enquiry_ford_ex_year=''  AND enquiry_ford_ex_odoreading=''"
				// + " AND enquiry_ford_ex_doors='' AND enquiry_ford_ex_bodystyle=''"
				// + " AND enquiry_ford_ex_enginesize='' AND enquiry_ford_ex_fueltype=''"
				// + " AND enquiry_ford_ex_drive='' AND enquiry_ford_ex_transmission=''"
				// + " AND enquiry_ford_ex_colour='' AND enquiry_ford_ex_priceoffered='0'"
				// + " AND enquiry_ford_ex_estmtprice=0), 1, 0) AS blankfields,"
				// // + " IF((enquiry_tradein_preownedvariant_id!='0' "
				// + " IF(( enquiry_ford_ex_derivative!=''"
				// + " AND enquiry_ford_ex_year!=''  AND enquiry_ford_ex_odoreading!=''"
				// + " AND enquiry_ford_ex_doors!='' AND enquiry_ford_ex_bodystyle!=''"
				// + " AND enquiry_ford_ex_enginesize!='' AND enquiry_ford_ex_fueltype!=''"
				// + " AND enquiry_ford_ex_drive!='' AND enquiry_ford_ex_transmission!=''"
				// + " AND enquiry_ford_ex_colour!='' AND enquiry_ford_ex_priceoffered!='0'"
				// + " AND enquiry_ford_ex_estmtprice!=0), 1, 0) AS nonblankfields "
				// + " FROM " + compdb(comp_id) + "axela_sales_enquiry"
				// + " WHERE 1=1 "
				// + " AND enquiry_id = " + enquiry_id;
				// // SOP("StrSql----ford-------" + StrSqlBreaker(StrSql));
				// crs = processQuery(StrSql, 0);
				// while (crs.next()) {
				// if (!crs.getString("blankfields").equals("1") &&
				// !crs.getString("nonblankfields").equals("1")) {
				// // msg = msg + "<br>Enter full Trade-in Details!";
				//
				// // if (crs.getString("enquiry_ford_ex_make").equals("")) {
				// // fordmsg += "<br>Enter Make!";
				// //
				// // }
				// // if (crs.getString("enquiry_ford_ex_model").equals("")) {
				// // fordmsg += "<br>Enter Model!";
				// //
				// // }
				//
				// // if (crs.getString("enquiry_tradein_preownedvariant_id").equals("0")) {
				// // fordmsg += "<br>Select Trade-In Model!";
				// //
				// // }
				// if (crs.getString("enquiry_ford_ex_derivative").equals("")) {
				// fordmsg += "<br>Enter Derivative!";
				//
				// }
				// if (crs.getString("enquiry_ford_ex_year").equals("")) {
				// fordmsg += "<br>Select Year!";
				//
				// }
				// if (crs.getString("enquiry_ford_ex_odoreading").equals("")) {
				// fordmsg += "<br>Enter Odo KM reading!";
				//
				// }
				// if (crs.getString("enquiry_ford_ex_doors").equals("")) {
				// fordmsg += "<br>Select Doors!";
				//
				// }
				// if (crs.getString("enquiry_ford_ex_bodystyle").equals("")) {
				// fordmsg += "<br>Select Body Style!";
				//
				// }
				// if (crs.getString("enquiry_ford_ex_fueltype").equals("")) {
				// fordmsg += "<br>Enter Engine Size!";
				//
				// }
				// if (crs.getString("enquiry_ford_ex_fueltype").equals("")) {
				// fordmsg += "<br>Select Fuel Type!";
				//
				// }
				// if (crs.getString("enquiry_ford_ex_drive").equals("")) {
				// fordmsg += "<br>Select Drive!";
				//
				// }
				// if (crs.getString("enquiry_ford_ex_transmission").equals("")) {
				// fordmsg += "<br>Select Transmission!";
				//
				// }
				// if (crs.getString("enquiry_ford_ex_colour").equals("")) {
				// fordmsg += "<br>Select Colour!";
				//
				// }
				// if (crs.getString("enquiry_ford_ex_priceoffered").equals("0")) {
				// fordmsg += "<br>Enter Price Offered!";
				//
				// }
				// if (crs.getString("enquiry_ford_ex_estmtprice").equals("0")) {
				// fordmsg += "<br>Enter Estimated Price!";
				//
				// }
				// }
				// if (!fordmsg.equals(""))
				// {
				// msg = msg + "<br>Enter Full Trade-in Details for Ford!" + fordmsg;
				// }
				//
				// }
				// crs.close();

			}
			// -----ends Ford fields checking-----

			// -----Start Nexa Fields Checking------

			// StrSql = " SELECT enquiry_id " + " FROM " + compdb(comp_id) + "axela_sales_enquiry"
			// + " WHERE enquiry_buyertype_id=3 "
			// // + "AND (enquiry_tradein_preownedvariant_id=0 OR enquiry_ownership_id=0 OR enquiry_tradein_preownedvariant_id='0' OR enquiry_purchasemonth=''"
			// + "AND (enquiry_ownership_id=0 OR enquiry_purchasemonth=''"
			//
			// // / + " OR enquiry_loancompletionmonth='' "
			// // + " OR enquiry_loanfinancer=''"
			// // + " OR enquiry_kms=0"
			// + " OR enquiry_expectedprice=0"
			// // + " OR enquiry_quotedprice=0"
			// + " ) AND enquiry_id = " + enquiry_id; // //OR
			// if (!ExecuteQuery(StrSql).equals("")) {
			// msg = msg + "<br>Enter full exchange information!";
			// }

			// -----End Nexa Fields Checking-----

			// -----Start Porsche Fields Checking-----
			if (brand_id.equals("56")) {
				StrSql = " SELECT enquiry_porsche_gender, enquiry_porsche_language, enquiry_porsche_religion,"
						+ " enquiry_porsche_preferredcomm, enquiry_porsche_socialmediapref, enquiry_porsche_maritalstatus,"
						+ " enquiry_porsche_interest, enquiry_porsche_financeoption, enquiry_porsche_insuranceoption,"
						+ " enquiry_porsche_vehicleinhouse, enquiry_porsche_householdcount, contact_dob, "
						+ " (SELECT COUNT(currentcars_enquiry_id) FROM " + compdb(comp_id) + "axela_sales_enquiry_currentcars"
						+ " WHERE currentcars_enquiry_id = " + enquiry_id + " ) AS currentcarscount "
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON enquiry_contact_id = contact_id"
						+ " WHERE enquiry_id = " + enquiry_id
						+ " GROUP BY enquiry_id";
				// SOP("Porsche---mandatory----" + StrSql);
				crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						if (crs.getString("enquiry_porsche_gender").equals("")) {
							msg = msg + "<br>Enter Gender!";
						}
						if (crs.getString("enquiry_porsche_language").equals("")) {
							msg = msg + "<br>Enter Language!";
						}
						if (crs.getString("enquiry_porsche_religion").equals("")) {
							msg = msg + "<br>Enter Religion!";
						}
						if (crs.getString("enquiry_porsche_preferredcomm").equals("")) {
							msg = msg + "<br>Enter Preferred Communication!";
						}
						if (crs.getString("enquiry_porsche_socialmediapref").equals("")) {
							msg = msg + "<br>Enter Social Media Preference!";
						}
						if (crs.getString("enquiry_porsche_maritalstatus").equals("")) {
							msg = msg + "<br>Enter Marital Status!";
						}
						if (crs.getString("enquiry_porsche_interest").equals("")) {
							msg = msg + "<br>Enter Interests!";
						}
						if (crs.getString("enquiry_porsche_financeoption").equals("")) {
							msg = msg + "<br>Enter Interested In Financing!";
						}
						if (crs.getString("enquiry_porsche_insuranceoption").equals("")) {
							msg = msg + "<br>Enter Interested In Insurance!";
						}
						if (crs.getString("enquiry_porsche_vehicleinhouse").equals("")) {
							msg = msg + "<br>Enter Number Of Vehicle In House!";
						}
						if (crs.getString("enquiry_porsche_householdcount").equals("")) {
							msg = msg + "<br>Enter Person's In Household!";
						}
						if (crs.getString("contact_dob").equals("")) {
							msg = msg + "<br>Enter DOB!";
						}
						if (crs.getString("currentcarscount").equals("0")) {
							msg = msg + "<br>Enter Current Cars!";
						}
						if (!msg.equals("")) {
							msg = "<br>Enter Following Customer Need Assessement Details!" + msg;
						}

					}
				}
				crs.close();
			}

			// -----End Porsche Fields Checking-----

			// ------------Skoda Fields-------------
			if (brand_id.equals("11")) {
				StrSql = "SELECT"
						+ " na_skoda_ownbusiness,"
						+ " na_skoda_companyname,"
						+ " na_skoda_financerequired,"
						+ " na_skoda_currentcarappxkmsrun,"
						+ " na_skoda_whatareyoulookingfor,"
						+ " na_skoda_numberoffamilymembers,"
						+ " na_skoda_whowilldrive,"
						+ " na_skoda_whoareyoubuyingfor,"
						+ " na_skoda_newcarappxrun,"
						+ " na_skoda_wherewillbecardriven,"
						+ " contact_jobtitle,"
						+ " (SELECT COUNT(currentcars_enquiry_id) FROM " + compdb(comp_id) + "axela_sales_enquiry_currentcars"
						+ " WHERE currentcars_enquiry_id = " + enquiry_id + " ) AS currentcarscount "
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_na"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = na_enquiry_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = na_enquiry_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_currentcars ON currentcars_enquiry_id = na_enquiry_id"
						+ " WHERE 1=1"
						+ " AND na_enquiry_id = " + enquiry_id
						+ " GROUP BY na_enquiry_id";
				// SOP("StrSql==" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						if (crs.getString("na_skoda_ownbusiness").equals("")) {
							msg = msg + "<br>Select Own a Business!";
						}
						if (crs.getString("na_skoda_companyname").equals("")) {
							msg = msg + "<br>Enter Company Name!";
						}
						if (crs.getString("contact_jobtitle").equals("")) {
							msg = msg + "<br>Enter Job Title!";
						}
						if (crs.getString("na_skoda_financerequired").equals("")) {
							msg = msg + "<br>Select Finance Required!";
						}
						if (crs.getString("na_skoda_currentcarappxkmsrun").equals("")) {
							msg = msg + "<br>Select Approximate kms run!";
						}
						if (crs.getString("currentcarscount").equals("0")) {
							msg = msg + "<br>Select Current Cars!";
						}
						if (crs.getString("na_skoda_whatareyoulookingfor").equals("")) {
							msg = msg + "<br>Select What are you looking for in your car!";
						}
						if (crs.getString("na_skoda_numberoffamilymembers").equals("")) {
							msg = msg + "<br>Select Number of Family Members!";
						}
						if (crs.getString("na_skoda_whowilldrive").equals("")) {
							msg = msg + "<br>Select Who will drive the car!";
						}
						if (crs.getString("na_skoda_whoareyoubuyingfor").equals("")) {
							msg = msg + "<br>Select Who are you buying the car for!";
						}
						if (crs.getString("na_skoda_newcarappxrun").equals("")) {
							msg = msg + "<br>Select Approximately how many kms in a day will the car be run!";
						}
						if (crs.getString("na_skoda_wherewillbecardriven").equals("")) {
							msg = msg + "<br>Select Where will the car be driven mostly!";
						}
					}
				}
				crs.close();
			}
			// -------End of skoda fields

			// AMP Fields
			if (brand_id.equals("60")) {
				StrSql = "SELECT"
						+ " enquiry_jlr_employmentstatus,"
						+ " enquiry_jlr_industry,"
						+ " contact_dob,"
						+ " enquiry_jlr_gender,"
						+ " enquiry_jlr_occupation,"
						+ " COALESCE(currentcars_variant_id,'') AS currentcars_variant_id,"
						+ " COALESCE(othermodels_preownedmodel_id,'') AS othermodels_preownedmodel_id,"
						+ " enquiry_jlr_noofchildren,"
						+ " enquiry_jlr_noofpeopleinhousehold,"
						+ " enquiry_jlr_householdincome,"
						+ " enquiry_jlr_interests,"
						+ " enquiry_jlr_annualrevenue,"
						+ " enquiry_jlr_noofemployees,"
						+ " enquiry_jlr_accounttype"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_currentcars ON currentcars_enquiry_id = enquiry_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_othermodels ON othermodels_enquiry_id = enquiry_id"
						+ " WHERE 1=1"
						+ " AND enquiry_id = " + enquiry_id
						+ " GROUP BY enquiry_id";
				// SOP("StrSql==" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						if (crs.getString("enquiry_jlr_employmentstatus").equals("")) {
							msg = msg + "<br>Select Employment Status!";
						}
						if (crs.getString("enquiry_jlr_industry").equals("")) {
							msg = msg + "<br>Select Industry!";
						}
						// if (crs.getString("contact_dob").equals("")) {
						// msg = msg + "<br>Select Birthday!";
						// }
						// if (crs.getString("enquiry_jlr_gender").equals("")) {
						// msg = msg + "<br>Select Gender!";
						// }
						if (crs.getString("enquiry_jlr_occupation").equals("")) {
							msg = msg + "<br>Select Occupation!";
						}
						if (crs.getString("currentcars_variant_id").equals("")) {
							msg = msg + "<br>Select Current Vehicle!";
						}
						if (crs.getString("othermodels_preownedmodel_id").equals("")) {
							msg = msg + "<br>Select Other Models Interest!";
						}
						// if (crs.getString("enquiry_jlr_noofchildren").equals("0")) {
						// msg = msg + "<br>Enter No. of Children!";
						// }
						// if (crs.getString("enquiry_jlr_noofpeopleinhousehold").equals("0")) {
						// msg = msg + "<br>Enter Person's In Household!";
						// }
						// if (crs.getString("enquiry_jlr_householdincome").equals("0")) {
						// msg = msg + "<br>Enter Household Income!";
						// }
						// if (crs.getString("enquiry_jlr_annualrevenue").equals("0")) {
						// msg = msg + "<br>Enter Annual Revenue!";
						// }
						// if (crs.getString("enquiry_jlr_noofemployees").equals("0")) {
						// msg = msg + "<br>Enter No. of Employees!";
						// }
						if (crs.getString("enquiry_jlr_accounttype").equals("")) {
							msg = msg + "<br>Select Account Type!";
						}
						// if (crs.getString("enquiry_jlr_interests").equals("")) {
						// msg = msg + "<br>Select Interests!";
						// }
					}
				}
				crs.close();
			}

			return msg;
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return msg;
		}
	}
	public String PopulateEnquiryPorscheGender(String dr_enquiry_porsche_gender, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"Male\" " + StrSelectdrop(dr_enquiry_porsche_gender, "Male") + ">Male</option>");
			Str.append("<option value=\"Female\" " + StrSelectdrop(dr_enquiry_porsche_gender, "Female") + ">Female</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateEnquiryPorscheNationality(String dr_enquiry_porsche_nationality, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"Indian\" " + StrSelectdrop(dr_enquiry_porsche_nationality, "Indian") + ">Indian</option>");
			Str.append("<option value=\"Others\" " + StrSelectdrop(dr_enquiry_porsche_nationality, "Others") + ">Others</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateEnquiryPorscheReligion(String dr_enquiry_porsche_religion, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"Hindu\" " + StrSelectdrop(dr_enquiry_porsche_religion, "Hindu") + ">Hindu</option>");
			Str.append("<option value=\"Muslim\" " + StrSelectdrop(dr_enquiry_porsche_religion, "Muslim") + ">Muslim</option>");
			Str.append("<option value=\"Christian\" " + StrSelectdrop(dr_enquiry_porsche_religion, "Christian") + ">Christian</option>");
			Str.append("<option value=\"Sikh\" " + StrSelectdrop(dr_enquiry_porsche_religion, "Sikh") + ">Sikh</option>");
			Str.append("<option value=\"Jain\" " + StrSelectdrop(dr_enquiry_porsche_religion, "Jain") + ">Jain</option>");
			Str.append("<option value=\"Others\" " + StrSelectdrop(dr_enquiry_porsche_religion, "Others") + ">Others</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateEnquiryPorscheMaritalStatus(String dr_enquiry_porsche_maritalstatus, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"Single\" " + StrSelectdrop(dr_enquiry_porsche_maritalstatus, "Single") + ">Single</option>");
			Str.append("<option value=\"Married\" " + StrSelectdrop(dr_enquiry_porsche_maritalstatus, "Married") + ">Married</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateSpouseDriveStatus(String dr_enquiry_porsche_spousedrive, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"Yes\" " + StrSelectdrop(dr_enquiry_porsche_spousedrive, "Yes") + ">Yes</option>");
			Str.append("<option value=\"No\" " + StrSelectdrop(dr_enquiry_porsche_spousedrive, "No") + ">No</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateEnquiryPorscheFinance(String dr_enquiry_porsche_finance, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"Yes\" " + StrSelectdrop(dr_enquiry_porsche_finance, "Yes") + ">Yes</option>");
			Str.append("<option value=\"No\" " + StrSelectdrop(dr_enquiry_porsche_finance, "No") + ">No</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateEnquiryPorscheInsurance(String dr_enquiry_porsche_insurance, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"Yes\" " + StrSelectdrop(dr_enquiry_porsche_insurance, "Yes") + ">Yes</option>");
			Str.append("<option value=\"No\" " + StrSelectdrop(dr_enquiry_porsche_insurance, "No") + ">No</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateEnquiryPorscheLanguage(String chk_enquiry_porsche_language, String comp_id) {
		StringBuilder Str = new StringBuilder();
		String language = "English, Kannada, Tamil, Telugu, Hindi, Others";

		String[] a = language.split(", ");
		try {
			for (int i = 0; i < a.length; i++) {
				String porschelanguage = a[i];
				Str.append("<input type='checkbox' name='porschelanguage[]' id='" + porschelanguage + "' value='" + porschelanguage + "'"
						+ " onclick=\"SecurityCheck('chk_enquiry_porsche_language',this,'hint_chk_enquiry_porsche_language')\"")
						.append(StrArrSelectCheck(chk_enquiry_porsche_language, porschelanguage))
						.append(">").append("&nbsp;&nbsp;" + a[i] + "&nbsp;&nbsp;").append("</input>");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateEnquiryPorschePreferredCommunication(String chk_enquiry_porsche_preferredcomm, String comp_id) {
		StringBuilder Str = new StringBuilder();
		String prefcomm = "Mobile, Email, Landline";

		String[] a = prefcomm.split(", ");
		try {
			for (int i = 0; i < a.length; i++) {
				String porscheprefcomm = a[i];
				Str.append("<input type='checkbox' name='porscheprefcomm[]' id='" + porscheprefcomm + "' value='" + porscheprefcomm + "'"
						+ " onclick=\"SecurityCheck('chk_enquiry_porsche_preferredcomm',this,'hint_chk_enquiry_porsche_preferredcomm')\"")
						.append(StrArrSelectCheck(chk_enquiry_porsche_preferredcomm, porscheprefcomm))
						.append(">").append("&nbsp;&nbsp;" + a[i] + "&nbsp;&nbsp;").append("</input>");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateEnquiryPorscheSocialMediaPref(String chk_enquiry_porsche_socialmediapref, String comp_id) {
		StringBuilder Str = new StringBuilder();
		String socialmediapref = "Facebook, Instagram, Linkedin, Twitter";

		String[] a = socialmediapref.split(", ");
		try {
			for (int i = 0; i < a.length; i++) {
				String porschesocialmediapref = a[i];
				Str.append("<input type='checkbox' name='porschesocialmediapref[]' id='" + porschesocialmediapref + "' value='" + porschesocialmediapref + "'"
						+ " onclick=\"SecurityCheck('chk_enquiry_porsche_socialmediapref',this,'hint_chk_enquiry_porsche_socialmediapref')\"")
						.append(StrArrSelectCheck(chk_enquiry_porsche_socialmediapref, porschesocialmediapref))
						.append(">").append("&nbsp;&nbsp;" + a[i] + "&nbsp;&nbsp;").append("</input>");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateEnquiryPorscheInsterest(String chk_enquiry_porsche_interest, String comp_id) {
		StringBuilder Str = new StringBuilder();
		String instrest = "Horse Riding, Rugby, Driving, Stock Markets, Travelling, Cricket, Skiing, Theatre, Tennis, Polo, Art Collection, Music, Football, Jogging/Running, Watches, High Technology, Golf, Cycling, Interior Design, Sailing, Motorbikes, Cigars, Power Boats, Classic Cars, Fine Dining, Motorsport, Hiking, Photography";

		String[] a = instrest.split(", ");
		try {
			Str.append("<table class='table table-bordered table-hover'>");
			int count = 0;
			for (int i = 0; i < a.length; i++) {
				String porscheinterest = a[i];
				if (count == 0) {
					Str.append("<tr>");
				}
				Str.append("<td style='padding: 2px'>");
				Str.append("<input type='checkbox' name='porscheinterest[]' id='" + porscheinterest + "' value='" + porscheinterest + "'"
						+ " onclick=\"SecurityCheck('chk_enquiry_porsche_interest',this,'hint_chk_enquiry_porsche_interest')\"")
						.append(StrArrSelectCheck(chk_enquiry_porsche_interest, porscheinterest))
						.append(" /></td><td style='padding: 2px'>").append("&nbsp;&nbsp;" + a[i] + "&nbsp;&nbsp;");
				Str.append("</td>");
				count++;
				if (count == 5) {
					Str.append("</tr>");
					count = 0;
				}
			}
			Str.append("</table>");
		} catch (Exception ex) {
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateEnquiryPorscheIndustry(String dr_enquiry_porsche_industry, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<optgroup label=\"\">");
			Str.append("<option value=\"\">Select</option>");
			Str.append("</optgroup>");
			Str.append("<optgroup label=\"Industry\">");
			Str.append("<option value=\"Automotive Industry\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Automotive Industry") + ">Automotive Industry</option>");
			Str.append("<option value=\"Chemical and pharmaceutical industry\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Chemical and pharmaceutical industry")
					+ ">Chemical and pharmaceutical industry</option>");
			Str.append("<option value=\"Consumer goods industry\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Consumer goods industry") + ">Consumer goods industry</option>");
			Str.append("<option value=\"Engine building industry\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Engine building industry") + ">Engine building industry</option>");
			Str.append("<option value=\"IT hardware\" " + StrSelectdrop(dr_enquiry_porsche_industry, "IT hardware") + ">(IT) hardware</option>");
			Str.append("<option value=\"Telecommunications\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Telecommunications") + ">Telecommunications</option>");
			Str.append("<option value=\"Building industry\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Building industry") + ">Building industry</option>");
			Str.append("<option value=\"Food industry/agribusiness\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Food industry/agribusiness") + ">Food industry/agribusiness</option>");
			Str.append("<option value=\"Textile/apparel/leather industry\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Textile/apparel/leather industry")
					+ ">Textile/apparel/leather industry</option>");
			Str.append("<option value=\"Timber/paper industry\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Timber/paper industry") + ">Timber/paper industry</option>");
			Str.append("<option value=\"Extractive/glass/ceramics/block/oil industry\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Extractive/glass/ceramics/block/oil industry")
					+ ">Extractive/glass/ceramics/block/oil industry</option>");
			Str.append("<option value=\"Metal, steel industry\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Metal, steel industry") + ">Metal, steel industry</option>");
			Str.append("<option value=\"Electrotechnology\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Electrotechnology") + ">Electrotechnology</option>");
			Str.append("<option value=\"Medical engineering, optical devices\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Medical engineering, optical devices")
					+ ">Medical engineering, optical devices</option>");
			Str.append("<option value=\"Jewelry, musical instruments, sport equipment\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Jewelry, musical instruments, sport equipment")
					+ ">Jewelry, musical instruments, sport equipment</option>");
			Str.append("<option value=\"Other industries\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Other industries") + ">Other industries</option>");
			Str.append("</optgroup>");
			Str.append("<optgroup label=\"Services\">");
			Str.append("<option value=\"Banks\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Banks") + ">Banks</option>");
			Str.append("<option value=\"Assurances\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Assurances") + ">Assurances</option>");
			Str.append("<option value=\"General financial services\" " + StrSelectdrop(dr_enquiry_porsche_industry, "General financial services") + ">General financial services</option>");
			Str.append("<option value=\"Marketing/PR/ad\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Marketing/PR/ad") + ">Marketing/PR/ad</option>");
			Str.append("<option value=\"Media/film/publishing\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Media/film/publishing") + ">Media/film/publishing</option>");
			Str.append("<option value=\"Graphics and design\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Graphics and design") + ">Graphics and design</option>");
			Str.append("<option value=\"Telecommunications\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Telecommunications") + ">Telecommunications</option>");
			Str.append("<option value=\"IT Software\" " + StrSelectdrop(dr_enquiry_porsche_industry, "IT Software") + ">(IT) Software</option>");
			Str.append("<option value=\"Gastronomy\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Gastronomy") + ">Gastronomy</option>");
			Str.append("<option value=\"Transport, tourism, automobile\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Transport, tourism, automobile") + ">Transport, tourism, automobile</option>");
			Str.append("<option value=\"Real estate\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Real estate") + ">Real estate</option>");
			Str.append("<option value=\"Architect/construction engineer/structural engineer\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Architect/construction engineer/structural engineer")
					+ ">Architect/construction engineer/structural engineer</option>");
			Str.append("<option value=\"Education\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Education") + ">Education</option>");
			Str.append("<option value=\"Other services\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Other services") + ">Other services</option>");
			Str.append("</optgroup>");
			Str.append("<b><optgroup label=\"Consulting\"></b>");
			Str.append("<option value=\"Legal consulting\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Legal consulting") + ">Legal consulting</option>");
			Str.append("<option value=\"Tax consulting\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Tax consulting") + ">Tax consulting</option>");
			Str.append("<option value=\"Consulting\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Consulting") + ">Consulting</option>");
			Str.append("<option value=\"Accounting\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Accounting") + ">Accounting</option>");
			Str.append("<option value=\"Notary\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Notary") + ">Notary</option>");
			Str.append("<option value=\"IT consulting\" " + StrSelectdrop(dr_enquiry_porsche_industry, "IT consulting") + ">IT consulting</option>");
			Str.append("<option value=\"Financial consulting\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Financial consulting") + ">Financial consulting</option>");
			Str.append("<option value=\"Personnel consulting\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Personnel consulting") + ">Personnel consulting</option>");
			Str.append("<option value=\"Environment consulting\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Environment consulting") + ">Environment consulting</option>");
			Str.append("<option value=\"Other consulting\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Other consulting") + ">Other consulting</option>");
			Str.append("</optgroup>");
			Str.append("<optgroup label=\"Health care\">");
			Str.append("<option value=\"Medical doctor\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Medical doctor") + ">Medical doctor</option>");
			Str.append("<option value=\"Dentist/dental technician\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Dentist/dental technician") + ">Dentist/dental technician</option>");
			Str.append("<option value=\"Medical specialist\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Medical specialist") + ">Medical specialist</option>");
			Str.append("<option value=\"Therapeutical occupations\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Therapeutical occupations") + ">Therapeutical occupations</option>");
			Str.append("<option value=\"Health care general\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Health care general") + ">Health care general</option>");
			Str.append("<option value=\"Hospital/clinic/rehabilitation\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Hospital/clinic/rehabilitation") + ">Hospital/clinic/rehabilitation</option>");
			Str.append("<option value=\"Pharmacies\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Pharmacies") + ">Pharmacies</option>");
			Str.append("<option value=\"Fitness/chiropractor\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Fitness/chiropractor") + ">Fitness/chiropractor</option>");
			Str.append("</optgroup>");
			Str.append("<optgroup label=\"Trade\">");
			Str.append("<option value=\"Trade general\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Trade general") + ">Trade general</option>");
			Str.append("<option value=\"Food general\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Food general") + ">Food general</option>");
			Str.append("<option value=\"Textile\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Textile") + ">Textile</option>");
			Str.append("<option value=\"Electro/electronics\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Electro/electronics") + ">Electro/electronics</option>");
			Str.append("<option value=\"Wholesaling general\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Wholesaling general") + ">Wholesaling general</option>");
			Str.append("<option value=\"Retail trade general\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Retail trade general") + ">Retail trade general</option>");
			Str.append("<option value=\"Car dealer\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Car dealer") + ">Car dealer</option>");
			Str.append("<option value=\"Power/energy supply\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Power/energy supply") + ">Power/energy supply</option>");
			Str.append("</optgroup>");
			Str.append("<optgroup label=\"Handcraft\">");
			Str.append("<option value=\"Handcraft general\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Handcraft general") + ">Handcraft general</option>");
			Str.append("<option value=\"Building craft (cabinetmaker, locksmith, etc.)\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Building craft (cabinetmaker, locksmith, etc.)")
					+ ">Building craft (cabinetmaker, locksmith, etc.)</option>");
			Str.append("<option value=\"Food (butcher, baker, etc.)\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Food (butcher, baker, etc.)") + ">Food (butcher, baker, etc.)</option>");
			Str.append("<option value=\"Environment, energy\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Environment, energy") + ">Environment, energy</option>");
			Str.append("<option value=\"Electro/metal/motocar/technique\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Electro/metal/motocar/technique")
					+ ">Electro/metal/motocar/technique</option>");
			Str.append("<option value=\"Health\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Health") + ">Health</option>");
			Str.append("<option value=\"Clothing/textile\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Clothing/textile") + ">Clothing/textile</option>");
			Str.append("<option value=\"Craftwork\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Craftwork") + ">Craftwork</option>");
			Str.append("<option value=\"Agriculture/forestry\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Agriculture/forestry") + ">Agriculture/forestry</option>");
			Str.append("</optgroup>");
			Str.append("<optgroup label=\"Others\">");
			Str.append("<option value=\"Artist, musician\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Artist, musician") + ">Artist, musician</option>");
			Str.append("<option value=\"Athlete\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Athlete") + ">Athlete</option>");
			Str.append("<option value=\"Design\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Design") + ">Design</option>");
			Str.append("<option value=\"Entertainment\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Entertainment") + ">Entertainment</option>");
			Str.append("<option value=\"Military/army/marine\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Military/army/marine") + ">Military/army/marine</option>");
			Str.append("<option value=\"Public administration/government\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Public administration/government")
					+ ">Public administration/government</option>");
			Str.append("<option value=\"Others\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Others") + ">Others</option>");
			Str.append("<option value=\"Pensionary; housewife/househusband, collegian\" " + StrSelectdrop(dr_enquiry_porsche_industry, "Pensionary; housewife/househusband, collegian")
					+ ">Pensionary; housewife/househusband, collegian</option>");
			Str.append("</optgroup>");
		} catch (Exception ex) {
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	// start JLR field methods
	public String PopulateJLREmploymentStatus(String enquiry_jlr_employmentstatus, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"Employed\" " + StrSelectdrop(enquiry_jlr_employmentstatus, "Employed") + ">Employed</option>");
			Str.append("<option value=\"Self Employed\" " + StrSelectdrop(enquiry_jlr_employmentstatus, "Self Employed") + ">Self Employed</option>");
			Str.append("<option value=\"Civil Servant\" " + StrSelectdrop(enquiry_jlr_employmentstatus, "Civil Servant") + ">Civil Servant</option>");
			Str.append("<option value=\"Retired\" " + StrSelectdrop(enquiry_jlr_employmentstatus, "Retired") + ">Retired</option>");
			Str.append("<option value=\"Unemployed\" " + StrSelectdrop(enquiry_jlr_employmentstatus, "Unemployed") + ">Unemployed</option>");
			Str.append("<option value=\"Other\" " + StrSelectdrop(enquiry_jlr_employmentstatus, "Other") + ">Other</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateEnquiryJLRIndustry(String enquiry_jlr_industry, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"Accounting/Consulting/Taxation\" " + StrSelectdrop(enquiry_jlr_industry, "Accounting/Consulting/Taxation") + ">Accounting/Consulting/Taxation</option>");
			Str.append("<option value=\"Advertising/Event Mgmt/Marketing\" " + StrSelectdrop(enquiry_jlr_industry, "Advertising/Event Mgmt/Marketing") + ">Advertising/Event Mgmt/Marketing</option>");
			Str.append("<option value=\"Agriculture\" " + StrSelectdrop(enquiry_jlr_industry, "Agriculture") + ">Agriculture</option>");
			Str.append("<option value=\"Architectural Services/Interior Design\" " + StrSelectdrop(enquiry_jlr_industry, "Architectural Services/Interior Design")
					+ ">Architectural Services/Interior Design</option>");
			Str.append("<option value=\"Automotive Trade\" " + StrSelectdrop(enquiry_jlr_industry, "Automotive Trade") + ">Automotive Trade</option>");
			Str.append("<option value=\"Bank/Finance\" " + StrSelectdrop(enquiry_jlr_industry, "Bank/Finance") + ">Bank/Finance</option>");
			Str.append("<option value=\"BPO/KPO/LPO/Call Center\" " + StrSelectdrop(enquiry_jlr_industry, "BPO/KPO/LPO/Call Center") + ">BPO/KPO/LPO/Call Center</option>");
			Str.append("<option value=\"Capital Goods/Machine Manufacturing\" " + StrSelectdrop(enquiry_jlr_industry, "Capital Goods/Machine Manufacturing")
					+ ">Capital Goods/Machine Manufacturing</option>");
			Str.append("<option value=\"Construction/Real Estate/Infrastructure\" " + StrSelectdrop(enquiry_jlr_industry, "Construction/Real Estate/Infrastructure")
					+ ">Construction/Real Estate/Infrastructure</option>");
			Str.append("<option value=\"Consumer Goods - FMCG\" " + StrSelectdrop(enquiry_jlr_industry, "Consumer Goods - FMCG") + ">Consumer Goods - FMCG</option>");
			Str.append("<option value=\"Courier/Logistics/Warehouse\" " + StrSelectdrop(enquiry_jlr_industry, "Courier/Logistics/Warehouse") + ">Courier/Logistics/Warehouse</option>");
			Str.append("<option value=\"Education & Training\" " + StrSelectdrop(enquiry_jlr_industry, "Education & Training") + ">Education & Training</option>");
			Str.append("<option value=\"Electronics\" " + StrSelectdrop(enquiry_jlr_industry, "Electronics") + ">Electronics</option>");
			Str.append("<option value=\"Energy Electrical/Gas/Oil/Renewables\" " + StrSelectdrop(enquiry_jlr_industry, "Energy Electrical/Gas/Oil/Renewables")
					+ ">Energy (Electrical/Gas/Oil/Renewables)</option>");
			Str.append("<option value=\"Engineering\" " + StrSelectdrop(enquiry_jlr_industry, "Engineering") + ">Engineering</option>");
			Str.append("<option value=\"Environmental\" " + StrSelectdrop(enquiry_jlr_industry, "Environmental") + ">Environmental</option>");
			Str.append("<option value=\"Export Houses\" " + StrSelectdrop(enquiry_jlr_industry, "Export Houses") + ">Export Houses</option>");
			Str.append("<option value=\"Financial Services\" " + StrSelectdrop(enquiry_jlr_industry, "Financial Services") + ">Financial Services</option>");
			Str.append("<option value=\"Fitness/Health/Lifestyle sector\" " + StrSelectdrop(enquiry_jlr_industry, "Fitness/Health/Lifestyle sector") + ">Fitness/Health/Lifestyle sector</option>");
			Str.append("<option value=\"Food Processing/Beverages\" " + StrSelectdrop(enquiry_jlr_industry, "Food Processing/Beverages") + ">Food Processing/Beverages</option>");
			Str.append("<option value=\"GarmentTextiles/Accessories\" " + StrSelectdrop(enquiry_jlr_industry, "GarmentTextiles/Accessories") + ">GarmentTextiles/Accessories</option>");
			Str.append("<option value=\"Gems/Jewelleries/Watches\" " + StrSelectdrop(enquiry_jlr_industry, "Gems/Jewelleries/Watches") + ">Gems/Jewelleries/Watches</option>");
			Str.append("<option value=\"Government\" " + StrSelectdrop(enquiry_jlr_industry, "Government") + ">Government</option>");
			Str.append("<option value=\"Hospitals/Hosp.Equipments/Healthcare\" " + StrSelectdrop(enquiry_jlr_industry, "Hospitals/Hosp.Equipments/Healthcare")
					+ ">Hospitals/Hosp.Equipments/Healthcare</option>");
			Str.append("<option value=\"Hotel/Resort Management\" " + StrSelectdrop(enquiry_jlr_industry, "Hotel/Resort Management") + ">Hotel/Resort Management</option>");
			Str.append("<option value=\"IT-Software Services/E-Commerce\" " + StrSelectdrop(enquiry_jlr_industry, "IT-Software Services/E-Commerce") + ">IT-Software Services/E-Commerce</option>");
			Str.append("<option value=\"Management/Consultancy\" " + StrSelectdrop(enquiry_jlr_industry, "Management/Consultancy") + ">Management/Consultancy</option>");
			Str.append("<option value=\"Media/Entertainment\" " + StrSelectdrop(enquiry_jlr_industry, "Media/Entertainment") + ">Media/Entertainment</option>");
			Str.append("<option value=\"Metals/Mining\" " + StrSelectdrop(enquiry_jlr_industry, "Metals/Mining") + ">Metals/Mining</option>");
			Str.append("<option value=\"Not For Profit\" " + StrSelectdrop(enquiry_jlr_industry, "Not For Profit") + ">Not For Profit</option>");
			Str.append("<option value=\"Other\" " + StrSelectdrop(enquiry_jlr_industry, "Other") + ">Other</option>");
			Str.append("<option value=\"Petrochemicals/Refineries\" " + StrSelectdrop(enquiry_jlr_industry, "Petrochemicals/Refineries") + ">Petrochemicals/Refineries</option>");
			Str.append("<option value=\"Pharmaceuticals/Bio Tech\" " + StrSelectdrop(enquiry_jlr_industry, "Pharmaceuticals/Bio Tech") + ">Pharmaceuticals/Bio Tech</option>");
			Str.append("<option value=\"Retail\" " + StrSelectdrop(enquiry_jlr_industry, "Retail") + ">Retail</option>");
			Str.append("<option value=\"Science/Marine\" " + StrSelectdrop(enquiry_jlr_industry, "Science/Marine") + ">Science/Marine</option>");
			Str.append("<option value=\"Science&Technology\" " + StrSelectdrop(enquiry_jlr_industry, "Science&Technology") + ">Science & Technology</option>");
			Str.append("<option value=\"Shipping/Marine\" " + StrSelectdrop(enquiry_jlr_industry, "Shipping/Marine") + ">Shipping/Marine </option>");
			Str.append("<option value=\"Sports Industry\" " + StrSelectdrop(enquiry_jlr_industry, "Sports Industry") + ">Sports Industry</option>");
			Str.append("<option value=\"Telecommunications/Mobile\" " + StrSelectdrop(enquiry_jlr_industry, "Telecommunications/Mobile") + ">Telecommunications/Mobile</option>");
			Str.append("<option value=\"Transportation\" " + StrSelectdrop(enquiry_jlr_industry, "Transportation") + ">Transportation</option>");
			Str.append("<option value=\"Travel/Tourism\" " + StrSelectdrop(enquiry_jlr_industry, "Travel/Tourism") + ">Travel/Tourism</option>");
			Str.append("<option value=\"Utilities\" " + StrSelectdrop(enquiry_jlr_industry, "Utilities") + ">Utilities</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateEnquiryJLRGender(String dr_enquiry_jlr_gender, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"Male\" " + StrSelectdrop(dr_enquiry_jlr_gender, "Male") + ">Male</option>");
			Str.append("<option value=\"Female\" " + StrSelectdrop(dr_enquiry_jlr_gender, "Female") + ">Female</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateJLROccupation(String enquiry_jlr_occupation, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"Accountants/CPA\" " + StrSelectdrop(enquiry_jlr_occupation, "Accountants/CPA") + ">Accountants/CPA</option>");
			Str.append("<option value=\"Actor/Singer/Dancer/Writer\" " + StrSelectdrop(enquiry_jlr_occupation, "Actor/Singer/Dancer/Writer") + ">Actor/Singer/Dancer/Writer</option>");
			Str.append("<option value=\"Architects\" " + StrSelectdrop(enquiry_jlr_occupation, "Architects") + ">Architects</option>");
			Str.append("<option value=\"Attorneys\" " + StrSelectdrop(enquiry_jlr_occupation, "Attorneys") + ">Attorneys</option>");
			Str.append("<option value=\"Beauty\" " + StrSelectdrop(enquiry_jlr_occupation, "Beauty") + ">Beauty</option>");
			Str.append("<option value=\"Blue Collar\" " + StrSelectdrop(enquiry_jlr_occupation, "Blue Collar") + ">Blue Collar</option>");
			Str.append("<option value=\"Chiropractors\" " + StrSelectdrop(enquiry_jlr_occupation, "Chiropractors") + ">Chiropractors</option>");
			Str.append("<option value=\"Clergy\" " + StrSelectdrop(enquiry_jlr_occupation, "Clergy") + ">Clergy</option>");
			Str.append("<option value=\"Counselors\" " + StrSelectdrop(enquiry_jlr_occupation, "Counselors") + ">Counselors</option>");
			Str.append("<option value=\"Doctor\" " + StrSelectdrop(enquiry_jlr_occupation, "Doctor") + ">Doctor</option>");
			Str.append("<option value=\"Doctors/Physicians/Surgeons\" " + StrSelectdrop(enquiry_jlr_occupation, "Doctors/Physicians/Surgeons") + ">Doctors/Physicians/Surgeons</option>");
			Str.append("<option value=\"Dentist/Dental Hygienist\" " + StrSelectdrop(enquiry_jlr_occupation, "Dentist/Dental Hygienist") + ">Dentist/Dental Hygienist</option>");
			Str.append("<option value=\"Electricians\" " + StrSelectdrop(enquiry_jlr_occupation, "Electricians") + ">Electricians</option>");
			Str.append("<option value=\"Engineers\" " + StrSelectdrop(enquiry_jlr_occupation, "Engineers") + ">Engineers</option>");
			Str.append("<option value=\"Executives/Directors/Entrepreneur\" " + StrSelectdrop(enquiry_jlr_occupation, "Executives/Directors/Entrepreneur")
					+ ">Executives/Directors/Entrepreneur</option>");
			Str.append("<option value=\"Farming/Fish/Forestry\" " + StrSelectdrop(enquiry_jlr_occupation, "Farming/Fish/Forestry") + ">Farming/Fish/Forestry</option>");
			Str.append("<option value=\"Insurance/Underwriters\" " + StrSelectdrop(enquiry_jlr_occupation, "Insurance/Underwriters") + ">Insurance/Underwriters</option>");
			Str.append("<option value=\"Interior Designers\" " + StrSelectdrop(enquiry_jlr_occupation, "Interior Designers") + ">Interior Designers</option>");
			Str.append("<option value=\"Landscape Architects\" " + StrSelectdrop(enquiry_jlr_occupation, "Landscape Architects") + ">Landscape Architects</option>");
			Str.append("<option value=\"Management/Business & Financial Operations\" " + StrSelectdrop(enquiry_jlr_occupation, "Management/Business & Financial Operations")
					+ ">Management/Business & Financial Operations</option>");
			Str.append("<option value=\"Newscaster\" " + StrSelectdrop(enquiry_jlr_occupation, "Newscaster") + ">Newscaster</option>");
			Str.append("<option value=\"Nurse\" " + StrSelectdrop(enquiry_jlr_occupation, "Nurse") + ">Nurse</option>");
			Str.append("<option value=\"Occupational Therapist/Physical Therapist\" " + StrSelectdrop(enquiry_jlr_occupation, "Occupational Therapist/Physical Therapist")
					+ ">Occupational Therapist/Physical Therapist</option>");
			Str.append("<option value=\"Office & Administrative Support\" " + StrSelectdrop(enquiry_jlr_occupation, "Office & Administrative Support")
					+ ">Office & Administrative Support</option>");
			Str.append("<option value=\"Opticians/Optometrist\" " + StrSelectdrop(enquiry_jlr_occupation, "Opticians/Optometrist") + ">Opticians/Optometrist</option>");
			Str.append("<option value=\"Other\" " + StrSelectdrop(enquiry_jlr_occupation, "Other") + ">Other</option>");
			Str.append("<option value=\"Pharmacist\" " + StrSelectdrop(enquiry_jlr_occupation, "Pharmacist") + ">Pharmacist</option>");
			Str.append("<option value=\"Professional: Legal/Education & Health Practitioner/Tech/Support\" "
					+ StrSelectdrop(enquiry_jlr_occupation, "Professional: Legal/Education & Health Practitioner/Tech/Support")
					+ ">Professional: Legal/Education & Health Practitioner/Tech/Support</option>");
			Str.append("<option value=\"Professional Athlete\" " + StrSelectdrop(enquiry_jlr_occupation, "Professional Athlete") + ">Professional Athlete</option>");
			Str.append("<option value=\"Psychologist\" " + StrSelectdrop(enquiry_jlr_occupation, "Psychologist") + ">Psychologist</option>");
			Str.append("<option value=\"Real Estate\" " + StrSelectdrop(enquiry_jlr_occupation, "Real Estate") + ">Real Estate</option>");
			Str.append("<option value=\"Retired\" " + StrSelectdrop(enquiry_jlr_occupation, "Retired") + ">Retired</option>");
			Str.append("<option value=\"Sales\" " + StrSelectdrop(enquiry_jlr_occupation, "Sales") + ">Sales</option>");
			Str.append("<option value=\"Services/Creative\" " + StrSelectdrop(enquiry_jlr_occupation, "Services/Creative") + ">Services/Creative</option>");
			Str.append("<option value=\"Social Worker\" " + StrSelectdrop(enquiry_jlr_occupation, "Social Worker") + ">Social Worker</option>");
			Str.append("<option value=\"Speech Path/Audiologist\" " + StrSelectdrop(enquiry_jlr_occupation, "Speech Path/Audiologist") + ">Speech Path/Audiologist</option>");
			Str.append("<option value=\"Surveyors\" " + StrSelectdrop(enquiry_jlr_occupation, "Surveyors") + ">Surveyors</option>");
			Str.append("<option value=\"Technical: Computers/Math & Architect/Engineering\" " + StrSelectdrop(enquiry_jlr_occupation, "Technical: Computers/Math & Architect/Engineering")
					+ ">Technical: Computers/Math & Architect/Engineering</option>");
			Str.append("<option value=\"Veterinarian\" " + StrSelectdrop(enquiry_jlr_occupation, "Veterinarian") + ">Veterinarian</option>");

		} catch (Exception ex) {
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateEnquiryJLRInsterest(String chk_enquiry_jlr_interest, String comp_id) {
		StringBuilder Str = new StringBuilder();
		String interest = "Horse Riding, Rugby, Driving, Stock Markets, Travelling, Cricket, Skiing, Theatre, Tennis, Polo, Art Collection, Music, Football, Jogging/Running, Watches, High Technology, Golf, Cycling, Interior Design, Sailing, Motorbikes, Cigars, Power Boats, Classic Cars, Fine Dining, Motorsport, Hiking, Photography";
		String[] a = interest.split(", ");
		try {
			Str.append("<table class='table table-bordered table-hover'>");
			int count = 0;
			for (int i = 0; i < a.length; i++) {
				String jlrinterest = a[i];
				if (count == 0) {
					Str.append("<tr>");
				}
				Str.append("<td style='padding: 2px'>");
				Str.append("<input type='checkbox' name='jlrinterest[]' id='" + jlrinterest + "' value='" + jlrinterest + "' "
						+ " onclick=\"SecurityCheck('chk_enquiry_jlr_interest',this,'hint_chk_enquiry_jlr_interest')\" ")
						.append(StrArrSelectCheck(chk_enquiry_jlr_interest, jlrinterest))
						.append(" /></td><td style='padding: 2px'>").append("&nbsp;&nbsp;" + a[i] + "&nbsp;&nbsp;");
				Str.append("</td>");
				count++;
				if (count == 5) {
					Str.append("</tr>");
					count = 0;
				}
			}
			Str.append("</table>");
		} catch (Exception ex) {
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateJLRAccountType(String enquiry_jlr_accounttype, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"Private\" " + StrSelectdrop(enquiry_jlr_accounttype, "Private") + ">Private</option>");
			Str.append("<option value=\"Business\" " + StrSelectdrop(enquiry_jlr_accounttype, "Business") + ">Business</option>");
			Str.append("<option value=\"Fleet\" " + StrSelectdrop(enquiry_jlr_accounttype, "Fleet") + ">Fleet</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateEnquiryStatus(String enquiry_status, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"Open\" " + StrSelectdrop(enquiry_status, "Open") + ">Open</option>");
			Str.append("<option value=\"Contacted\" " + StrSelectdrop(enquiry_status, "Contacted") + ">Contacted</option>");
			Str.append("<option value=\"Unable To Contact\" " + StrSelectdrop(enquiry_status, "Unable To Contact") + ">Unable To Contact</option>");
			Str.append("<option value=\"Need Analysis\" " + StrSelectdrop(enquiry_status, "Need Analysis") + ">Need Analysis</option>");
			Str.append("<option value=\"Showroom Appointment\" " + StrSelectdrop(enquiry_status, "Showroom Appointment") + ">Showroom Appointment</option>");
			Str.append("<option value=\"Qualified\" " + StrSelectdrop(enquiry_status, "Qualified") + ">Qualified</option>");
			Str.append("<option value=\"Continue To Nuture\" " + StrSelectdrop(enquiry_status, "Continue To Nuture") + ">Continue To Nuture</option>");
			Str.append("<option value=\"No Intention To Buy\" " + StrSelectdrop(enquiry_status, "No Intention To Buy") + ">No Intention To Buy</option>");
			Str.append("<option value=\"Bought Competitor\" " + StrSelectdrop(enquiry_status, "Bought Competitor") + ">Bought Competitor</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
	// end of JLR methods

	// Start of Skoda Methods

	public String PopulateSkodaOwnBusiness(String na_skoda_ownbusiness, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"Yes\" " + StrSelectdrop(na_skoda_ownbusiness, "Yes") + ">Yes</option>");
			Str.append("<option value=\"No\" " + StrSelectdrop(na_skoda_ownbusiness, "No") + ">No</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateSkodaFinance(String na_skoda_financerequired, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"Yes\" " + StrSelectdrop(na_skoda_financerequired, "Yes") + ">Yes</option>");
			Str.append("<option value=\"No\" " + StrSelectdrop(na_skoda_financerequired, "No") + ">No</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateSkodaAppxCurrentCarRun(String na_skoda_enquiry_currentcars, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"<10,000\" " + StrSelectdrop(na_skoda_enquiry_currentcars, "<10,000") + "><10,000</option>");
			Str.append("<option value=\"10,000-30,000\" " + StrSelectdrop(na_skoda_enquiry_currentcars, "10,000-30,000") + ">10,000-30,000</option>");
			Str.append("<option value=\"30,000-60,000\" " + StrSelectdrop(na_skoda_enquiry_currentcars, "30,000-60,000") + ">30,000-60,000</option>");
			Str.append("<option value=\">60,000\" " + StrSelectdrop(na_skoda_enquiry_currentcars, ">60,000") + ">>60,000</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateSkodaWhatAreYouLookingFor(String na_skoda_whatareyoulookingfor, String comp_id) {
		StringBuilder Str = new StringBuilder();
		String skodawhatareyoulookingfor = "Comfort, Styling/Appearance, Quality/Reliability, Fuel Economy, Safety, Ease Of Use, Roominess/Storage, Driving Characteristics";

		String[] a = skodawhatareyoulookingfor.split(", ");
		try {
			Str.append("<table class='table table-bordered table-hover'>");
			int count = 0;
			for (int i = 0; i < a.length; i++) {
				String whatareyoulookingfor = a[i];
				if (count == 0) {
					Str.append("<tr>");
				}
				Str.append("<td style='padding: 2px'>");
				Str.append("<input type='checkbox' name='whatareyoulookingfor[]' id='" + whatareyoulookingfor + "' value='" + whatareyoulookingfor + "'"
						+ " onclick=\"SecurityCheckSkoda('chk_na_skoda_whatareyoulookingfor',this,'hint_chk_na_skoda_whatareyoulookingfor')\"")
						.append(StrArrSelectCheck(na_skoda_whatareyoulookingfor, whatareyoulookingfor))
						.append(" /></td><td style='padding: 2px'>").append("&nbsp;&nbsp;" + a[i] + "&nbsp;&nbsp;");
				Str.append("</td>");
				count++;
				if (count == 4) {
					Str.append("</tr>");
					count = 0;
				}
			}
			Str.append("</table>");
		} catch (Exception ex) {
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateSkodaMembersInTheFamily(String na_skoda_numberoffamilymembers, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"1-2\" " + StrSelectdrop(na_skoda_numberoffamilymembers, "1-2") + ">1-2</option>");
			Str.append("<option value=\"2-4\" " + StrSelectdrop(na_skoda_numberoffamilymembers, "2-4") + ">2-4</option>");
			Str.append("<option value=\"4-6\" " + StrSelectdrop(na_skoda_numberoffamilymembers, "4-6") + ">4-6</option>");
			Str.append("<option value=\"6 above\" " + StrSelectdrop(na_skoda_numberoffamilymembers, "6 above") + ">6 above</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateWhoWillDrive(String na_skoda_whowilldrive, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"Self-driven\" " + StrSelectdrop(na_skoda_whowilldrive, "Self-driven") + ">Self-driven</option>");
			Str.append("<option value=\"Chauffer driven\" " + StrSelectdrop(na_skoda_whowilldrive, "Chauffer driven") + ">Chauffer driven</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateWhoAreYouBuyingFor(String na_skoda_whoareyoubuyingfor, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"Self\" " + StrSelectdrop(na_skoda_whoareyoubuyingfor, "Self") + ">Self</option>");
			Str.append("<option value=\"Spouse\" " + StrSelectdrop(na_skoda_whoareyoubuyingfor, "Spouse") + ">Spouse</option>");
			Str.append("<option value=\"Son/Daughter\" " + StrSelectdrop(na_skoda_whoareyoubuyingfor, "Son/Daughter") + ">Son/Daughter</option>");
			Str.append("<option value=\"Parents\" " + StrSelectdrop(na_skoda_whoareyoubuyingfor, "Parents") + ">Parents</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateSkodaAppxNewCarRun(String na_skoda_newcarappxrun, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"<10\" " + StrSelectdrop(na_skoda_newcarappxrun, "<10") + "><10</option>");
			Str.append("<option value=\"<70\" " + StrSelectdrop(na_skoda_newcarappxrun, "<70") + "><70</option>");
			Str.append("<option value=\">70\" " + StrSelectdrop(na_skoda_newcarappxrun, ">70") + ">>70</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateSkodaWhereWillBeDriven(String na_skoda_wherewillbecardriven, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"City\" " + StrSelectdrop(na_skoda_wherewillbecardriven, "City") + ">City</option>");
			Str.append("<option value=\"HighWay\" " + StrSelectdrop(na_skoda_wherewillbecardriven, "HighWay") + ">HighWay</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	// END of Skoda Methods

}
