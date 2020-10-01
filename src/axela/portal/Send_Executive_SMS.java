package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Send_Executive_SMS extends Connect {

	public String StrHTML = "", StrHTML1 = "", StrPostponed = "";
	public String StrSearch = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String branch_id = "0";
	public String branchtype = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String emp_id = "", exe_id = "";
	public String msg = "", customer_mobile_no = "";
	public String email = "", sms = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				customer_mobile_no = PadQuotes(request.getParameter("customer_mobile_no"));
				exe_id = PadQuotes(request.getParameter("exe_id"));

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

	/*
	 * protected String SendSMS(String type, String value, String sms_id) throws SQLException { String smsmsg = "";
	 * 
	 * // getting cannedsms_format StrSql = " SELECT " + " cannedsms_format" + " FROM  " + compdb(comp_id) + "axela_canned_sms" + " WHERE cannedsms_id = " + sms_id;
	 * 
	 * CachedRowSet crs = processQuery(StrSql, 0); // SOP("StrSql==111==" + StrSql); while (crs.next()) { smsmsg = crs.getString("cannedsms_format"); } crs.close();
	 * 
	 * // getting contact mobile1 if (!type.equals("0")) { StrSql = "SELECT " + " contact_mobile1," + " contact_mobile2 "; if (type.equals("1")) { StrSql += " FROM " + compdb(comp_id) +
	 * "axela_sales_enquiry " + " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id " + " WHERE enquiry_id = " + value; } else if (type.equals("2")) { StrSql
	 * += " FROM " + compdb(comp_id) + "axela_preowned " + " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = preowned_contact_id " + " WHERE preowned_id = " + value; } else if
	 * (type.equals("3")) { StrSql += " FROM " + compdb(comp_id) + "axela_service_veh " + " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id " +
	 * " WHERE veh_id = " + value; } // SOPInfo("StrSql==Sms====" + StrSql); crs = processQuery(StrSql, 0);
	 * 
	 * while (crs.next()) { contact_mobile1 = crs.getString("contact_mobile1"); contact_mobile2 = crs.getString("contact_mobile2");
	 * 
	 * } }
	 * 
	 * if (home.equals("yes")) { contact_mobile1 = value; }
	 * 
	 * if (type.equals("1")) { smsmsg = "REPLACE('" + smsmsg + "','[ENQUIRYID]',enquiry_id)"; smsmsg = "REPLACE(" + smsmsg + ",'[ENQUIRYNAME]',enquiry_title)"; smsmsg = "REPLACE(" + smsmsg +
	 * ",'[CUSTOMERID]',customer_id)"; smsmsg = "REPLACE(" + smsmsg + ",'[CUSTOMERNAME]',customer_name)"; smsmsg = "REPLACE(" + smsmsg +
	 * ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))"; smsmsg = "REPLACE(" + smsmsg + ",'[CONTACTJOBTITLE]',contact_jobtitle)"; smsmsg = "REPLACE(" + smsmsg +
	 * ",'[CONTACTMOBILE1]',contact_mobile1)"; smsmsg = "REPLACE(" + smsmsg + ",'[CONTACTPHONE1]',contact_phone1)"; smsmsg = "REPLACE(" + smsmsg + ",'[CONTACTEMAIL1]',contact_email1)"; smsmsg =
	 * "REPLACE(" + smsmsg + ",'[EXENAME]', enq.emp_name)"; smsmsg = "REPLACE(" + smsmsg + ",'[EXEJOBTITLE]', enqjob.jobtitle_desc)"; smsmsg = "REPLACE(" + smsmsg +
	 * ",'[EXEMOBILE1]', enq.emp_mobile1)"; smsmsg = "REPLACE(" + smsmsg + ",'[EXEPHONE1]', enq.emp_phone1)"; smsmsg = "REPLACE(" + smsmsg + ",'[EXEEMAIL1]', enq.emp_email1)"; smsmsg = "REPLACE(" +
	 * smsmsg + ", '[CRMEXENAME]', crm.emp_name)"; smsmsg = "REPLACE(" + smsmsg + ", '[CRMEXEJOBTITLE]', crmjob.jobtitle_desc)"; smsmsg = "REPLACE(" + smsmsg + ", '[CRMEXEMOBILE1]', crm.emp_mobile1)";
	 * smsmsg = "REPLACE(" + smsmsg + ", '[CRMEXEPHONE1]', crm.emp_phone1)"; smsmsg = "REPLACE(" + smsmsg + ", '[CRMEXEEMAIL1]', crm.emp_email1)"; smsmsg = "replace(" + smsmsg +
	 * ",'[BRANCHNAME]',branch_name)"; smsmsg = "replace(" + smsmsg + ",'[BRANCHEMAIL1]',branch_email1)"; smsmsg = "replace(" + smsmsg + ",'[MODELNAME]',model_name)"; smsmsg = "replace(" + smsmsg +
	 * ",'[ITEMNAME]',item_name)"; } else if (type.equals("2")) {
	 * 
	 * smsmsg = "replace('" + smsmsg + "', '[PREOWNEDID]',preowned_id)"; smsmsg = "replace(" + smsmsg + ",'[PREOWNEDNAME]',preowned_title)"; smsmsg = "replace(" + smsmsg +
	 * ",'[CUSTOMERID]',customer_id)"; smsmsg = "replace(" + smsmsg + ",'[CUSTOMERNAME]',customer_name)"; smsmsg = "replace(" + smsmsg +
	 * ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))"; smsmsg = "replace(" + smsmsg + ",'[CONTACTJOBTITLE]',contact_jobtitle)"; smsmsg = "replace(" + smsmsg +
	 * ",'[CONTACTMOBILE1]',contact_mobile1)"; smsmsg = "replace(" + smsmsg + ",'[CONTACTPHONE1]',contact_phone1)"; smsmsg = "replace(" + smsmsg + ",'[CONTACTEMAIL1]',contact_email1)"; smsmsg =
	 * "replace(" + smsmsg + ",'[EXENAME]',emp_name)"; smsmsg = "replace(" + smsmsg + ",'[EXEJOBTITLE]',jobtitle_desc)"; smsmsg = "replace(" + smsmsg + ",'[EXEMOBILE1]',emp_mobile1)"; smsmsg =
	 * "replace(" + smsmsg + ",'[EXEPHONE1]',emp_phone1)"; smsmsg = "replace(" + smsmsg + ",'[EXEEMAIL1]',emp_email1)"; smsmsg = "replace(" + smsmsg + ",'[BRANCHNAME]',branch_name)"; smsmsg =
	 * "replace(" + smsmsg + ",'[MODELNAME]',preownedmodel_name)"; smsmsg = "replace(" + smsmsg + ",'[ITEMNAME]',variant_name)";
	 * 
	 * } else if (type.equals("3")) { smsmsg = "REPLACE('" + smsmsg + "','[VEHID]',veh_id)"; smsmsg = "REPLACE(" + smsmsg + ", '[CUSTOMERID]', customer_id)"; smsmsg = "REPLACE(" + smsmsg +
	 * ", '[CUSTOMERNAME]', customer_name)"; smsmsg = "REPLACE(" + smsmsg + ", '[CONTACTNAME]', CONCAT(contact_fname, ' ', contact_lname))"; smsmsg = "REPLACE(" + smsmsg +
	 * ", '[CONTACTJOBTITLE]', contact_jobtitle)"; smsmsg = "REPLACE(" + smsmsg + ", '[CONTACTMOBILE1]', contact_mobile1)"; smsmsg = "REPLACE(" + smsmsg + ", '[CONTACTPHONE1]', contact_phone1)";
	 * smsmsg = "REPLACE(" + smsmsg + ", '[CONTACTEMAIL1]', contact_email1)"; smsmsg = "REPLACE(" + smsmsg + ", '[EXENAME]', emp_name)"; smsmsg = "REPLACE(" + smsmsg +
	 * ", '[EXEJOBTITLE]', jobtitle_desc)"; smsmsg = "REPLACE(" + smsmsg + ", '[EXEMOBILE1]', emp_mobile1)"; smsmsg = "REPLACE(" + smsmsg + ", '[EXEPHONE1]', emp_phone1)"; smsmsg = "REPLACE(" + smsmsg
	 * + ", '[EXEEMAIL1]', emp_email1)"; smsmsg = "REPLACE(" + smsmsg + ", '[MODELNAME]', COALESCE(model_name,''))"; smsmsg = "REPLACE(" + smsmsg + ", '[ITEMNAME]', COALESCE(item_name,''))"; smsmsg =
	 * "REPLACE(" + smsmsg + ", '[REGNO]', COALESCE(veh_reg_no,''))"; smsmsg = "REPLACE(" + smsmsg + ", '[SERVICEDUEDATE]', DATE_FORMAT(veh_service_duedate,'%d/%m/%Y'))"; }
	 * 
	 * if (!contact_mobile1.equals("")) { try { if (!type.equals("0")) { StrSql = "SELECT" + " branch_id ," + " contact_id ," + " CONCAT(title_desc, ' ', contact_fname,' ', contact_lname)," + " '" +
	 * contact_mobile1 + "' ," + " " + unescapehtml(smsmsg) + "," + " '" + ToLongDate(kknow()) + "'," + " 0," + " " + emp_id + "";
	 * 
	 * if (type.equals("1")) {
	 * 
	 * StrSql += " FROM " + compdb(comp_id) + "axela_sales_enquiry" + " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id" + " INNER JOIN " + compdb(comp_id) +
	 * "axela_customer ON customer_id = enquiry_customer_id" + " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id" + " INNER JOIN " + compdb(comp_id) +
	 * "axela_title ON title_id = contact_title_id" + " INNER JOIN " + compdb(comp_id) + "axela_emp enq ON enq.emp_id = enquiry_emp_id" + " INNER JOIN " + compdb(comp_id) +
	 * "axela_jobtitle enqjob ON enqjob.jobtitle_id = enq.emp_jobtitle_id"
	 * 
	 * + " INNER JOIN " + compdb(comp_id) + "axela_sales_crm ON crm_enquiry_id = enquiry_id" + " INNER JOIN " + compdb(comp_id) + "axela_emp crm ON crm.emp_id = crm_emp_id" + " INNER JOIN " +
	 * compdb(comp_id) + "axela_jobtitle crmjob ON crmjob.jobtitle_id = crm.emp_jobtitle_id" + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id  " +
	 * " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id  " + " WHERE enquiry_id = " + value; // SOP("StrSql==1====3===" + StrSql); } else if (type.equals("2")) {
	 * 
	 * StrSql += " FROM " + compdb(comp_id) + "axela_preowned" + " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id" + " INNER JOIN " + compdb(comp_id) +
	 * "axela_customer ON customer_id = preowned_customer_id" + " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = preowned_contact_id" + " INNER JOIN " + compdb(comp_id) +
	 * "axela_title ON title_id = contact_title_id" + " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = preowned_emp_id" + " INNER JOIN " + compdb(comp_id) +
	 * "axela_jobtitle ON jobtitle_id = emp_jobtitle_id" + " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id  " +
	 * " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id  " + " WHERE preowned_id = " + value; // SOP("StrSql====enq===" + StrSql);
	 * 
	 * } else if (type.equals("3")) {
	 * 
	 * StrSql += " FROM " + compdb(comp_id) + "axela_service_veh" + " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = veh_branch_id " + " INNER JOIN " + compdb(comp_id) +
	 * "axela_customer ON customer_id = veh_customer_id " + " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id " + " INNER JOIN " + compdb(comp_id) +
	 * "axela_emp ON emp_id = veh_entry_id " + " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id " + " INNER JOIN " + compdb(comp_id) +
	 * "axela_jobtitle ON jobtitle_id = emp_jobtitle_id " + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_item_id " + " INNER JOIN " + compdb(comp_id) +
	 * "axela_inventory_item_model ON model_id = item_model_id " + " WHERE veh_id = " + value; // SOP("StrSql=3==" + StrSql);
	 * 
	 * } }
	 * 
	 * if (home.equals("yes")) { StrSql = "SELECT" + " if(emp_branch_id=0, 1 ,emp_branch_id) ," + " 0 ," + " ''," + " '" + contact_mobile1 + "' ," + " '" + unescapehtml(smsmsg) + "'," + " '" +
	 * ToLongDate(kknow()) + "'," + " 0," + " " + emp_id + "" + " FROM " + compdb(comp_id) + "axela_emp" + " WHERE emp_id =" + emp_id; // SOP("StrSql---->>" + StrSql); }
	 * 
	 * StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sms" + " (sms_branch_id," + " sms_contact_id," + " sms_contact," + " sms_mobileno," + " sms_msg," + " sms_date," + " sms_sent," +
	 * " sms_entry_id)" + " " + StrSql + " LIMIT 1" + ""; // SOP("Sendsms-----------" + StrSql); updateQuery(StrSql); msg = "<b> SMS sent Successfully! </b><br></br>"; } catch (Exception ex) {
	 * SOPError("Axelaauto== " + this.getClass().getName()); SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex); } }
	 * 
	 * if (!contact_mobile2.equals("")) { try { StrSql = "SELECT" + " branch_id ," + " contact_id ," + " CONCAT(title_desc, ' ', contact_fname,' ', contact_lname)," + " " + contact_mobile2 + "," + " "
	 * + unescapehtml(smsmsg) + "," + " '" + ToLongDate(kknow()) + "'," + " 0," + " " + emp_id + "";
	 * 
	 * if (type.equals("1")) {
	 * 
	 * StrSql += " FROM " + compdb(comp_id) + "axela_sales_enquiry" + " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id" + " INNER JOIN " + compdb(comp_id) +
	 * "axela_customer ON customer_id = enquiry_customer_id" + " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id" + " INNER JOIN " + compdb(comp_id) +
	 * "axela_title ON title_id = contact_title_id" + " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id" + " INNER JOIN " + compdb(comp_id) +
	 * "axela_jobtitle ON jobtitle_id = emp_jobtitle_id" + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id  " + " INNER JOIN " + compdb(comp_id) +
	 * "axela_inventory_item ON item_id = enquiry_item_id  " + " WHERE enquiry_id = " + value; } else if (type.equals("2")) {
	 * 
	 * StrSql += " FROM " + compdb(comp_id) + "axela_preowned" + " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id" + " INNER JOIN " + compdb(comp_id) +
	 * "axela_customer ON customer_id = preowned_customer_id" + " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = preowned_contact_id" + " INNER JOIN " + compdb(comp_id) +
	 * "axela_title ON title_id = contact_title_id" + " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = preowned_emp_id" + " INNER JOIN " + compdb(comp_id) +
	 * "axela_jobtitle ON jobtitle_id = emp_jobtitle_id" + " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id  " +
	 * " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id  " + " WHERE preowned_id = " + value; // SOP("StrSql====enq===" + StrSql);
	 * 
	 * } else if (type.equals("3")) {
	 * 
	 * if (!veh_id.equals("0")) { StrSql += " FROM axela_service_veh" + " INNER JOIN axela_branch ON branch_id = veh_branch_id " + " INNER JOIN axela_customer ON customer_id = veh_customer_id " +
	 * " INNER JOIN axela_customer_contact ON contact_id = veh_contact_id " + " INNER JOIN axela_emp ON emp_id = veh_entry_id " + " INNER JOIN axela_title ON title_id = contact_title_id " +
	 * " INNER JOIN axela_jobtitle ON jobtitle_id = emp_jobtitle_id " + " INNER JOIN axela_inventory_item ON item_id = veh_item_id " +
	 * " INNER JOIN axela_inventory_item_model ON model_id = item_model_id " + " WHERE veh_id = " + value; }
	 * 
	 * } StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sms" + " (sms_branch_id," + " sms_contact_id," + " sms_contact," + " sms_mobileno," + " sms_msg," + " sms_date," + " sms_sent," +
	 * " sms_entry_id)" + " " + StrSql + " LIMIT 1" + ""; // SOP("-------Sendsms2-----------" + StrSql); updateQuery(StrSql); msg = "<b> SMS sent Successfully! </b><br></br>"; } catch (Exception ex) {
	 * SOPError("Axelaauto== " + this.getClass().getName()); SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex); }
	 * 
	 * } if (contact_mobile1.equals("") && contact_mobile2.equals("")) { msg = "<b>Contact Mobile is Blank!</b><br></br>"; } return msg;
	 * 
	 * }
	 */
}
