//package axela.sales;
//
//import java.io.IOException;
//import java.sql.SQLException;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import javax.sql.rowset.CachedRowSet;
//
//import cloudify.connect.Connect;
//
//public class Enquiry_Transfer extends Connect {
//
//	public String StrSql = "";
//	public String StrHTML = "", msg = "";
//	public String enquiry_id = "0";
//	public String comp_id = "0";
//	public String emp_id = "0";
//	public String preowned_branch_id = "0";
//	public String preowned_id = "0";
//	public String brand_id = "0", branch_id = "0 ", model_id = "0", executive_id = "0";
//	String BranchAccess = "";
//	CachedRowSet crs = null;
//	axela.sales.Enquiry_Check check = new axela.sales.Enquiry_Check();
//	public void doPost(HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		try {
//			CheckSession(request, response);
//			HttpSession session = request.getSession(true);
//			comp_id = CNumeric(GetSession("comp_id", request));
//			if (!comp_id.equals("0")) {
//				enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
//				SOP("enquiry_id----11111---" + enquiry_id);
//				emp_id = CNumeric(GetSession("emp_id", request));
//				BranchAccess = GetSession("BranchAccess", request);
//				SOP("1111");
//				GetValues(request, response);
//				SOP("22");
//				CheckForm();
//				SOP("33");
//				if (!enquiry_id.equals("0")) {
//					PopulateFields(response);
//					SOP("44");
//				}
//			}
//		} catch (Exception ex) {
//			SOPError("Axelaauto===" + this.getClass().getName());
//			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
//		}
//	}
//
//	public void GetValues(HttpServletRequest request,
//			HttpServletResponse response) throws ServletException, IOException {
//
//		brand_id = CNumeric(PadQuotes(request.getParameter("dr_brand_id")));
//		SOP("brand_id----------" + brand_id);
//		branch_id = PadQuotes(request.getParameter("dr_enquiry_branch_id"));
//		SOP("branch_id----------" + branch_id);
//		model_id = PadQuotes(request.getParameter("dr_enquiry_model_id"));
//		SOP("model_id----------" + model_id);
//		executive_id = PadQuotes(request.getParameter("dr_enquiry_emp_id"));
//		SOP("executive_id----------" + executive_id);
//
//	}
//
//	public void CheckForm() throws SQLException {
//		msg = "";
//
//		if (brand_id.equals("0")) {
//			msg = msg + "<br>Select Brand!";
//		}
//	}
//	public void doGet(HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		doPost(request, response);
//	}
//
//	protected void PopulateFields(HttpServletResponse response) {
//
//		try {
//			if (!comp_id.equals("0"))
//			{
//				StrSql = "SELECT enquiry_branch_id, enquiry_customer_id, enquiry_contact_id,"
//						+ "enquiry_enquirytype_id, enquiry_title, enquiry_desc, enquiry_date, "
//						+ " enquiry_model_id, enquiry_item_id, enquiry_close_date, "
//						+ " enquiry_value_syscal, enquiry_avpresent, enquiry_manager_assist, "
//						+ " enquiry_preownedvariant_id, enquiry_tradein_preownedvariant_id,"
//						+ " enquiry_fueltype_id, enquiry_prefreg_id, enquiry_presentcar,"
//						+ " enquiry_finance, enquiry_value, enquiry_emp_id, enquiry_team_id,"
//						+ " enquiry_stage_id, enquiry_soe_id, enquiry_sob_id, enquiry_campaign_id,"
//						+ " enquiry_status_id, enquiry_status_date, enquiry_status_desc,"
//						+ " enquiry_priorityenquiry_id, enquiry_notes, enquiry_qcsno,"
//						+ " enquiry_dmsno, enquiry_buyertype_id, enquiry_enquirycat_id, "
//						+ " enquiry_custtype_id, enquiry_entry_id, enquiry_entry_date"
//						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
//						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id "
//						+ " WHERE enquiry_id=" + enquiry_id;
//				StrSql += " GROUP BY enquiry_id ";
//				SOP("Dash-----PopulateFields-------" + StrSqlBreaker(StrSql));
//				CachedRowSet crs = processQuery(StrSql, 0);
//				Enquiry_Quickadd enqadd = new Enquiry_Quickadd();
//				if (crs.isBeforeFirst()) {
//					while (crs.next()) {
//						enqadd.comp_id = comp_id;
//						enqadd.emp_id = emp_id;
//						enqadd.enquiry_branch_id = crs.getString("enquiry_branch_id");
//						enqadd.enquiry_customer_id = crs.getString("enquiry_customer_id");
//						enqadd.enquiry_contact_id = crs.getString("enquiry_contact_id");
//						enqadd.enquiry_enquirytype_id = crs.getString("enquiry_enquirytype_id");
//						enqadd.enquiry_title = crs.getString("enquiry_title");
//						enqadd.enquiry_desc = crs.getString("enquiry_desc");
//						enqadd.enquiry_date = crs.getString("enquiry_date");
//						enqadd.enquiry_model_id = crs.getString("enquiry_model_id");
//						enqadd.enquiry_item_id = crs.getString("enquiry_item_id");
//						enqadd.enquiry_close_date = crs.getString("enquiry_close_date");
//						enqadd.enquiry_value_syscal = crs.getString("enquiry_value_syscal");
//						enqadd.enquiry_avpresent = crs.getString("enquiry_avpresent");
//						enqadd.enquiry_manager_assist = crs.getString("enquiry_manager_assist");
//						enqadd.enquiry_preownedvariant_id = crs.getString("enquiry_preownedvariant_id");
//						enqadd.enquiry_tradein_preownedvariant_id = crs.getString("enquiry_tradein_preownedvariant_id");
//						enqadd.enquiry_fueltype_id = crs.getString("enquiry_fueltype_id");
//						enqadd.enquiry_prefreg_id = crs.getString("enquiry_prefreg_id");
//						enqadd.enquiry_presentcar = crs.getString("enquiry_presentcar");
//						enqadd.enquiry_finance = crs.getString("enquiry_finance");
//						enqadd.enquiry_value = crs.getString("enquiry_value");
//						enqadd.enquiry_emp_id = crs.getString("enquiry_emp_id");
//						enqadd.enquiry_team_id = crs.getString("enquiry_team_id");
//						enqadd.enquiry_stage_id = crs.getString("enquiry_stage_id");
//						enqadd.enquiry_soe_id = crs.getString("enquiry_soe_id");
//						enqadd.enquiry_sob_id = crs.getString("enquiry_sob_id");
//						enqadd.enquiry_campaign_id = crs.getString("enquiry_campaign_id");
//						enqadd.enquiry_status_id = crs.getString("enquiry_status_id");
//						enqadd.enquiry_status_date = crs.getString("enquiry_status_date");
//						enqadd.enquiry_status_desc = crs.getString("enquiry_status_desc");
//						enqadd.enquiry_priorityenquiry_id = crs.getString("enquiry_priorityenquiry_id");
//						enqadd.enquiry_notes = crs.getString("enquiry_notes");
//						enqadd.enquiry_qcsno = crs.getString("enquiry_qcsno");
//						enqadd.enquiry_dmsno = crs.getString("enquiry_dmsno");
//						enqadd.enquiry_buyertype_id = crs.getString("enquiry_buyertype_id");
//						enqadd.enquiry_enquirycat_id = crs.getString("enquiry_enquirycat_id");
//						enqadd.enquiry_custtype_id = crs.getString("enquiry_custtype_id");
//
//						enqadd.enquiry_entry_id = crs.getString("enquiry_entry_id");
//						enqadd.enquiry_entry_date = crs.getString("enquiry_entry_date");
//						enqadd.PopulateConfigDetails();
//						enqadd.PopulateContactCustomerDetails(response);
//						enqadd.AddEnquiryFields();
//						StrHTML = enqadd.msg;
//						enquiry_id = enqadd.enquiry_id;
//						if (!enquiry_id.equals("0")) {
//							StrHTML += "<br><font color=red><a href=../sales/enquiry-list.jsp?enquiry_id=" + enquiry_id + ">Enquiry added successfully!</a></font>";
//						}
//					}
//				} else {
//					// //msg = "<br><br>No Enquiry found!";
//					response.sendRedirect("../portal/error.jsp?msg=Invalid Enquiry!");
//				}
//				crs.close();
//			}
//		} catch (Exception ex) {
//			SOPError("Axelaauto== " + this.getClass().getName());
//			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
//		}
//	}
//
//	public String PopulateBrand(String comp_id) {
//
//		StringBuilder Str = new StringBuilder();
//		try {
//			StrSql = "SELECT brand_id, brand_name "
//					+ " FROM axela_brand "
//					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
//					+ " WHERE branch_active = 1" + BranchAccess
//					+ " AND branch_branchtype_id IN (1,2)"
//					+ " GROUP BY brand_id "
//					+ " ORDER BY brand_name ";
//			CachedRowSet crs = processQuery(StrSql, 0);
//			Str.append("<option value=0>Select</option>");
//			while (crs.next()) {
//				Str.append("<option value=").append(crs.getString("brand_id")).append("");
//				Str.append(Selectdrop(crs.getInt("brand_id"), brand_id));
//				Str.append(">").append(crs.getString("brand_name")).append("</option> \n");
//			}
//			crs.close();
//			return Str.toString();
//		} catch (Exception ex) {
//			SOPError("Axelaauto== " + this.getClass().getName());
//			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
//			return "";
//		}
//	}
// }
