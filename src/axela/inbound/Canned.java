package axela.inbound;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Canned extends Connect {

	public String StrHTML = "", StrHTML1 = "", StrPostponed = "";
	public String StrSearch = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String txt_search_email = "";
	public String txt_search_sms = "";
	public String txt_search = "";
	public String branch_id = "0", branchtype_id = "", brand_id = "0";
	public String branch_trans = "";
	public String branch_trans_id = "0";
	public String branchtype = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String emp_id = "";
	public String msg = "", canned = "", sales = "", service = "", preowned = "", email_id = "", contact_email_id = "",
			contact_mobile1 = "", contact_mobile2 = "", sms_id = "", type = "", value = "";
	public String email = "", sms = "", listemail = "", listsms = "";
	public String enquiry_id = "", preowned_id = "", veh_id = "", jc_id = "", home = "", customer_email_to = "", customer_mobile_no = "", call_no = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				branchtype_id = PadQuotes(request.getParameter("dr_branchtype"));
				brand_id = CNumeric(PadQuotes(request.getParameter("dr_brand")));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				canned = PadQuotes(request.getParameter("canned"));
				sales = PadQuotes(request.getParameter("sales"));
				preowned = PadQuotes(request.getParameter("preowned"));
				service = PadQuotes(request.getParameter("service"));
				type = CNumeric(PadQuotes(request.getParameter("type")));
				enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
				call_no = PadQuotes(request.getParameter("call_no"));

				if (branch_id.equals("0")) {
					branch_id = ExecuteQuery("SELECT branch_id "
							+ " FROM " + compdb(comp_id) + "axela_branch"
							+ " WHERE branch_active = 1"
							+ BranchAccess
							+ " AND branch_branchtype_id"
							+ " IN (1, 2) LIMIT 1");
				}
				// SOP("branch_id======" + branch_id);

				preowned_id = CNumeric(PadQuotes(request.getParameter("preowned_id")));
				veh_id = CNumeric(PadQuotes(request.getParameter("veh_id")));
				jc_id = CNumeric(PadQuotes(request.getParameter("jc_id")));
				value = CNumeric(PadQuotes(request.getParameter("value")));
				email = PadQuotes(request.getParameter("email"));
				sms = PadQuotes(request.getParameter("sms"));
				email_id = PadQuotes(request.getParameter("email_id"));
				sms_id = PadQuotes(request.getParameter("sms_id"));
				home = PadQuotes(request.getParameter("home"));
				customer_email_to = PadQuotes(request.getParameter("customer_email_to"));
				customer_mobile_no = PadQuotes(request.getParameter("customer_mobile_no"));
				listemail = PadQuotes(request.getParameter("listemail"));
				listsms = PadQuotes(request.getParameter("listsms"));
				txt_search_email = PadQuotes(request.getParameter("txt_search_email"));
				txt_search_sms = PadQuotes(request.getParameter("txt_search_sms"));
				// //SOP("type-===" + type);
				// SOP("email====" + email);
				// SOP("sms====" + sms);
				// SOP("home-===" + home);
				// SOP("customer_mobile_no-===" + customer_mobile_no);
				// SOP("value-===" + value);
				// SOP("email_id-===" + email_id);

				if (canned.equals("yes")) {
					if (sales.equals("yes")) {
						branchtype = "1";
					} else if (preowned.equals("yes")) {
						branchtype = "2";
					} else if (service.equals("yes")) {
						branchtype = "3";
					} else {
						branchtype = "0";
					}

					if (listemail.equals("yes")) {
						brand_id = CNumeric(PadQuotes(request.getParameter("brand_id")));
						branchtype_id = CNumeric(PadQuotes(request.getParameter("branchtype_id")));
						StrHTML = getEmailList(branchtype_id);
					}
					if (listsms.equals("yes")) {
						brand_id = CNumeric(PadQuotes(request.getParameter("brand_id")));
						branchtype_id = CNumeric(PadQuotes(request.getParameter("branchtype_id")));
						StrHTML = getSMSList(branchtype_id);
					}

					if (!listemail.equals("yes") && !listsms.equals("yes")) {
						StrHTML = getEmailList(branchtype_id);
						StrHTML1 = getSMSList(branchtype_id);
					}

				}

				if (!type.equals("0")) {
					if (email.equals("yes")) {
						StrHTML = SendEmail(type, value, email_id);
					}
					if (sms.equals("yes")) {
						StrHTML = SendSMS(type, value, sms_id);
					}
				}

				if (home.equals("yes")) {
					if (!customer_email_to.equals("")) {
						if (!IsValidEmail(customer_email_to)) {
							msg = "Error!" + msg;
							StrHTML = msg + "<b><br>Enter Valid Contact Email!</b>";
						} else if (msg.equals("")) {
							if (email.equals("yes")) {
								StrHTML = SendEmail(type, customer_email_to, email_id);
							}
						}

					}

					if (!customer_mobile_no.equals("")) {
						if (!IsValidMobileNo11(customer_mobile_no)) {
							msg = "Error!" + msg;
							StrHTML = msg + "<b><br>Enter Valid Contact Mobile!</b>";
						} else if (msg.equals("")) {
							if (sms.equals("yes")) {
								StrHTML = SendSMS(type, customer_mobile_no, sms_id);
							}
						}
					}
					// SOP("msg===" + msg);
				}

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

	public String getEmailList(String branchtype) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT cannedemail_id, cannedemail_name"
					+ " FROM " + compdb(comp_id) + "axela_canned_email"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = cannedemail_brand_id";

			if (branchtype.equals("1")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_branch_id = branch_id"
						+ " AND enquiry_id = " + enquiry_id;
			} else if (branchtype.equals("2")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_branch_id = branch_id"
						+ " AND preowned_id = " + preowned_id;
			} else if (branchtype.equals("3")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_branch_id = branch_id"
						+ " AND veh_id = " + veh_id;
			}

			StrSql += " WHERE 1= 1";
			if (branchtype.equals("0")) {
				StrSql += " AND cannedemail_sub NOT LIKE '%[%' "
						+ " AND cannedemail_format NOT LIKE '%[%' ";

			} else {
				if (!branchtype.equals("")) {
					StrSql += " AND cannedemail_branchtype_id = " + branchtype;
				}
			}
			if (!brand_id.equals("0")) {
				StrSql += " AND branch_brand_id = " + brand_id + "";
			}
			StrSql += " AND cannedemail_active = 1"
					+ " AND cannedemail_sub != ''"
					+ " AND cannedemail_format != ''";
			if (!txt_search_email.equals("")) {
				StrSql += " AND (cannedemail_name LIKE '%" + txt_search_email + "%'"
						+ " OR cannedemail_sub LIKE '%" + txt_search_email + "%'"
						+ " OR cannedemail_format LIKE '%" + txt_search_email + "%')";
			}

			StrSql += " GROUP BY cannedemail_id"
					+ " ORDER BY cannedemail_rank ";

			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("StrSql---1====" + StrSql);
			if (crs.next()) {
				crs.beforeFirst();
				// Str.append("<div class='modal-dialog'>");
				Str.append("<center>");
				if (home.equals("yes")) {
					Str.append("<div class=\"col-md-6 col-md-offset-3\">"
							+ "<input class=\"form-control\" name=\"txt_email\" type=\"text\" id=\"txt_email\""
							+ " size=\"35\" maxlength=\"100\"></div><br></br>");
				}
				while (crs.next()) {
					Str.append("<div>"
							+ "<button class=\"btn btn-success\" onclick='SendEmail(this.value);'"
							+ " id='" + crs.getString("cannedemail_id") + "'"
							+ " name='" + crs.getString("cannedemail_id") + "'"
							+ " value='" + crs.getString("cannedemail_id") + "'")
							.append(">")
							.append(crs.getString("cannedemail_name"))
							.append("</button></div>\n");
				}
				Str.append("</center>");
				// Str.append("</div>");
				crs.close();
			}
			else {
				Str.append("<center><font color=red><b>No Email Message's Found</b></font></center>");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}

		return Str.toString();

	}

	public String getSMSList(String branchtype) {

		StringBuilder Str = new StringBuilder();
		try {

			StrSql = "SELECT cannedsms_id, cannedsms_name"
					+ " FROM " + compdb(comp_id) + "axela_canned_sms"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = cannedsms_brand_id";

			if (branchtype.equals("1")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_branch_id = branch_id";
				if (!enquiry_id.equals("0")) {
					StrSql += " AND enquiry_id = " + enquiry_id;
				}
			} else if (branchtype.equals("2")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_branch_id = branch_id";
				if (!preowned_id.equals("0")) {
					StrSql += " AND preowned_id = " + preowned_id;
				}
			} else if (branchtype.equals("3")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_branch_id = branch_id";
				if (!veh_id.equals("0")) {
					StrSql += " AND veh_id = " + veh_id;
				}
			}
			StrSql += " WHERE 1= 1";
			if (branchtype.equals("0")) {
				StrSql += " AND cannedsms_format NOT LIKE '%[%'";

			} else {
				if (!branchtype.equals("")) {
					StrSql += " AND cannedsms_branchtype_id = " + branchtype;
				}
			}
			if (!brand_id.equals("0")) {
				StrSql += " AND branch_brand_id = " + brand_id + "";
			}
			StrSql += " AND cannedsms_active = 1"
					+ " AND cannedsms_format != ''";
			if (!txt_search_sms.equals("")) {
				StrSql += " AND (cannedsms_name LIKE '%" + txt_search_sms + "%'"
						+ " OR cannedsms_format LIKE '%" + txt_search_sms + "%')";
			}
			StrSql += " GROUP BY cannedsms_id "
					+ " ORDER BY cannedsms_rank ";

			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("StrSql---2==sms==" + StrSql);
			if (crs.next()) {
				crs.beforeFirst();
				Str.append("<center>");
				if (home.equals("yes")) {
					Str.append("<div class=\"col-md-6 col-md-offset-3\">"
							+ "<input class=\"form-control\" onKeyUp=\"toPhone('txt_mobile','Mobile')\" maxlength=\"13\" size=\"32\""
							+ "name=\"txt_mobile\" type=\"text\" id=\"txt_mobile\" value=" + call_no + "></input></div><br></br>");
				}
				while (crs.next()) {
					Str.append("<div>"
							+ "<button class=\"btn btn-success\" onclick='SendSMSC(this.value);'"
							+ " id='" + crs.getString("cannedsms_id") + "'"
							+ " name='" + crs.getString("cannedsms_id") + "'"
							+ " value='" + crs.getString("cannedsms_id") + "'")
							.append(">")
							.append(crs.getString("cannedsms_name"))
							.append("</button></div>\n");
				}
				Str.append("</center>");
				crs.close();
			}
			else {
				Str.append("<center><font color=red><b>No SMS Message's Found</b></font></center>");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto=====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}

		return Str.toString();

	}

	protected String SendEmail(String type, String value, String email_id) throws SQLException {
		// SOP("coming");
		String emailmsg = "";
		String sub = "";
		// SOP("Email value===" + value);

		// getting email sub and format
		StrSql = " SELECT "
				+ " cannedemail_sub,"
				+ " cannedemail_format"
				+ " FROM " + compdb(comp_id) + "axela_canned_email"
				+ " WHERE cannedemail_id = " + email_id;

		CachedRowSet crs = processQuery(StrSql, 0);

		while (crs.next()) {
			sub = crs.getString("cannedemail_sub");
			emailmsg = crs.getString("cannedemail_format");

		}
		crs.close();
		// SOP("sub==email===" + sub);
		// SOP("format==email===" + emailmsg);
		// getting contact email id
		if (!type.equals("0")) {
			StrSql = "SELECT "
					+ " contact_email1,"
					+ " contact_email2 ";
			if (type.equals("1")) {
				StrSql += " FROM " + compdb(comp_id) + "axela_sales_enquiry "
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id "
						+ " WHERE enquiry_id = " + value;
			} else if (type.equals("2")) {
				StrSql += " FROM " + compdb(comp_id) + "axela_preowned "
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = preowned_contact_id "
						+ " WHERE preowned_id = " + value;
			} else if (type.equals("3")) {
				StrSql += " FROM " + compdb(comp_id) + "axela_service_veh "
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id "
						+ " WHERE veh_id = " + value;
			}

			// SOPInfo("StrSql==email====" + StrSql);

			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				contact_email_id = crs.getString("contact_email1");
				if (!contact_email_id.equals("") && !crs.getString("contact_email2").equals("")) {
					contact_email_id = contact_email_id + "," + crs.getString("contact_email2");
				}
				else if (contact_email_id.equals("") && !crs.getString("contact_email2").equals("")) {
					contact_email_id = crs.getString("contact_email2");
				}
			}
		}

		if (home.equals("yes")) {
			contact_email_id = value;
		}

		if (type.equals("1")) {
			sub = "REPLACE('" + sub + "','[ENQUIRYID]', enquiry_id)";
			sub = "REPLACE(" + sub + ",'[ENQUIRYNAME]', enquiry_title)";
			sub = "REPLACE(" + sub + ",'[CUSTOMERID]', customer_id)";
			sub = "REPLACE(" + sub + ",'[CUSTOMERNAME]', customer_name)";
			sub = "REPLACE(" + sub + ",'[CONTACTNAME]', concat(title_desc, ' ', contact_fname,' ', contact_lname))";
			sub = "REPLACE(" + sub + ",'[CONTACTJOBTITLE]', contact_jobtitle)";
			sub = "REPLACE(" + sub + ",'[CONTACTMOBILE1]', contact_mobile1)";
			sub = "REPLACE(" + sub + ",'[CONTACTPHONE1]', contact_phone1)";
			sub = "REPLACE(" + sub + ",'[CONTACTEMAIL1]', contact_email1)";
			sub = "REPLACE(" + sub + ",'[EXENAME]', enq.emp_name)";
			sub = "REPLACE(" + sub + ",'[EXEJOBTITLE]', enqjob.jobtitle_desc)";
			sub = "REPLACE(" + sub + ",'[EXEMOBILE1]', enq.emp_mobile1)";
			sub = "REPLACE(" + sub + ",'[EXEPHONE1]', enq.emp_phone1)";
			sub = "REPLACE(" + sub + ",'[EXEEMAIL1]', enq.emp_email1)";
			sub = "REPLACE(" + sub + ",'[CRMEXENAME]', crm.emp_name)";
			sub = "REPLACE(" + sub + ",'[CRMEXEJOBTITLE]', crmjob.jobtitle_desc)";
			sub = "REPLACE(" + sub + ",'[CRMEXEMOBILE1]', crm.emp_mobile1)";
			sub = "REPLACE(" + sub + ",'[CRMEXEPHONE1]', crm.emp_phone1)";
			sub = "REPLACE(" + sub + ",'[CRMEXEEMAIL1]', crm.emp_email1)";
			sub = "REPLACE(" + sub + ",'[BRANCHEMAIL1]', branch_email1)";
			sub = "REPLACE(" + sub + ",'[BRANCHNAME]', branch_name)";
			sub = "REPLACE(" + sub + ",'[MODELNAME]', model_name)";
			sub = "REPLACE(" + sub + ",'[ITEMNAME]', item_name)";

			emailmsg = "REPLACE('" + emailmsg + "','[ENQUIRYID]', enquiry_id)";
			emailmsg = "REPLACE(" + emailmsg + ",'[ENQUIRYNAME]', enquiry_title)";
			emailmsg = "REPLACE(" + emailmsg + ",'[CUSTOMERID]', customer_id)";
			emailmsg = "REPLACE(" + emailmsg + ",'[CUSTOMERNAME]', customer_name)";
			emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTNAME]', CONCAT(title_desc, ' ', contact_fname,' ', contact_lname))";
			emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTJOBTITLE]', contact_jobtitle)";
			emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTMOBILE1]', contact_mobile1)";
			emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTPHONE1]', contact_phone1)";
			emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTEMAIL1]', contact_email1)";
			emailmsg = "REPLACE(" + emailmsg + ",'[EXENAME]', enq.emp_name)";
			emailmsg = "REPLACE(" + emailmsg + ",'[EXEJOBTITLE]', enqjob.jobtitle_desc)";
			emailmsg = "REPLACE(" + emailmsg + ",'[EXEMOBILE1]', enq.emp_mobile1)";
			emailmsg = "REPLACE(" + emailmsg + ",'[EXEPHONE1]', enq.emp_phone1)";
			emailmsg = "REPLACE(" + emailmsg + ",'[EXEEMAIL1]', enq.emp_email1)";
			emailmsg = "REPLACE(" + emailmsg + ",'[CRMEXENAME]', crm.emp_name)";
			emailmsg = "REPLACE(" + emailmsg + ",'[CRMEXEJOBTITLE]', crmjob.jobtitle_desc)";
			emailmsg = "REPLACE(" + emailmsg + ",'[CRMEXEMOBILE1]', crm.emp_mobile1)";
			emailmsg = "REPLACE(" + emailmsg + ",'[CRMEXEPHONE1]', crm.emp_phone1)";
			emailmsg = "REPLACE(" + emailmsg + ",'[CRMEXEEMAIL1]', crm.emp_email1)";
			emailmsg = "REPLACE(" + emailmsg + ",'[BRANCHNAME]', branch_name)";
			emailmsg = "REPLACE(" + emailmsg + ",'[BRANCHEMAIL1]', branch_email1)";
			emailmsg = "REPLACE(" + emailmsg + ",'[MODELNAME]', model_name)";
			emailmsg = "REPLACE(" + emailmsg + ",'[ITEMNAME]', item_name)";
		} else if (type.equals("2")) {
			sub = "replace('" + sub + "', '[PREOWNEDID]',preowned_id)";
			sub = "replace(" + sub + ",'[PREOWNEDNAME]',preowned_title)";
			sub = "replace(" + sub + ",'[CUSTOMERID]',customer_id)";
			sub = "replace(" + sub + ",'[CUSTOMERNAME]',customer_name)";
			sub = "replace(" + sub + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
			sub = "replace(" + sub + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
			sub = "replace(" + sub + ",'[CONTACTMOBILE1]',contact_mobile1)";
			sub = "replace(" + sub + ",'[CONTACTPHONE1]',contact_phone1)";
			sub = "replace(" + sub + ",'[CONTACTEMAIL1]',contact_email1)";
			sub = "replace(" + sub + ",'[EXENAME]',emp_name)";
			sub = "replace(" + sub + ",'[EXEJOBTITLE]',jobtitle_desc)";
			sub = "replace(" + sub + ",'[EXEMOBILE1]',emp_mobile1)";
			sub = "replace(" + sub + ",'[EXEPHONE1]',emp_phone1)";
			sub = "replace(" + sub + ",'[EXEEMAIL1]',emp_email1)";
			sub = "replace(" + sub + ",'[BRANCHNAME]',branch_name)";
			sub = "replace(" + sub + ",'[MODELNAME]',preownedmodel_name)";
			sub = "replace(" + sub + ",'[ITEMNAME]',variant_name)";

			emailmsg = "replace('" + emailmsg + "', '[PREOWNEDID]',preowned_id)";
			emailmsg = "replace(" + emailmsg + ",'[PREOWNEDNAME]',preowned_title)";
			emailmsg = "replace(" + emailmsg + ",'[CUSTOMERID]',customer_id)";
			emailmsg = "replace(" + emailmsg + ",'[CUSTOMERNAME]',customer_name)";
			emailmsg = "replace(" + emailmsg + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
			emailmsg = "replace(" + emailmsg + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
			emailmsg = "replace(" + emailmsg + ",'[CONTACTMOBILE1]',contact_mobile1)";
			emailmsg = "replace(" + emailmsg + ",'[CONTACTPHONE1]',contact_phone1)";
			emailmsg = "replace(" + emailmsg + ",'[CONTACTEMAIL1]',contact_email1)";
			emailmsg = "replace(" + emailmsg + ",'[EXENAME]',emp_name)";
			emailmsg = "replace(" + emailmsg + ",'[EXEJOBTITLE]',jobtitle_desc)";
			emailmsg = "replace(" + emailmsg + ",'[EXEMOBILE1]',emp_mobile1)";
			emailmsg = "replace(" + emailmsg + ",'[EXEPHONE1]',emp_phone1)";
			emailmsg = "replace(" + emailmsg + ",'[EXEEMAIL1]',emp_email1)";
			emailmsg = "replace(" + emailmsg + ",'[BRANCHNAME]',branch_name)";
			emailmsg = "replace(" + emailmsg + ",'[MODELNAME]',preownedmodel_name)";
			emailmsg = "replace(" + emailmsg + ",'[ITEMNAME]',variant_name)";

		} else if (type.equals("3")) {
			sub = "REPLACE('" + sub + "','[VEHID]',veh_id)";
			sub = "REPLACE(" + sub + ",'[CUSTOMERID]', customer_id)";
			sub = "REPLACE(" + sub + ",'[CUSTOMERNAME]', customer_name)";
			sub = "REPLACE(" + sub + ",'[CONTACTNAME]', CONCAT(title_desc, ' ', contact_fname,' ', contact_lname))";
			sub = "REPLACE(" + sub + ",'[CONTACTJOBTITLE]', contact_jobtitle)";
			sub = "REPLACE(" + sub + ",'[CONTACTMOBILE1]', contact_mobile1)";
			sub = "REPLACE(" + sub + ",'[CONTACTPHONE1]', contact_phone1)";
			sub = "REPLACE(" + sub + ",'[CONTACTEMAIL1]', contact_email1)";
			sub = "REPLACE(" + sub + ",'[EXENAME]', emp_name)";
			sub = "REPLACE(" + sub + ",'[EXEJOBTITLE]', jobtitle_desc)";
			sub = "REPLACE(" + sub + ",'[EXEMOBILE1]', emp_mobile1)";
			sub = "REPLACE(" + sub + ",'[EXEPHONE1]', emp_phone1)";
			sub = "REPLACE(" + sub + ",'[EXEEMAIL1]', emp_email1)";
			sub = "REPLACE(" + sub + ",'[MODELNAME]', COALESCE(model_name,''))";
			sub = "REPLACE(" + sub + ",'[ITEMNAME]', COALESCE(item_name,''))";
			sub = "REPLACE(" + sub + ",'[BRANCHEMAIL1]', branch_email1)";
			sub = "REPLACE(" + sub + ",'[BRANCHNAME]', branch_name)";
			sub = "REPLACE(" + sub + ",'[SERVICEDUEDATE]', DATE_FORMAT(veh_service_duedate,'%d/%m/%Y'))";

			emailmsg = "REPLACE('" + emailmsg + "','[VEHID]',veh_id)";
			emailmsg = "REPLACE(" + emailmsg + ",'[CUSTOMERID]', customer_id)";
			emailmsg = "REPLACE(" + emailmsg + ",'[CUSTOMERNAME]', customer_name)";
			emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTNAME]', CONCAT(title_desc, ' ', contact_fname,' ', contact_lname))";
			emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTJOBTITLE]', contact_jobtitle)";
			emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTMOBILE1]', contact_mobile1)";
			emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTPHONE1]', contact_phone1)";
			emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTEMAIL1]', contact_email1)";
			emailmsg = "REPLACE(" + emailmsg + ",'[EXENAME]', emp_name)";
			emailmsg = "REPLACE(" + emailmsg + ",'[EXEJOBTITLE]', jobtitle_desc)";
			emailmsg = "REPLACE(" + emailmsg + ",'[EXEMOBILE1]', emp_mobile1)";
			emailmsg = "REPLACE(" + emailmsg + ",'[EXEPHONE1]', emp_phone1)";
			emailmsg = "REPLACE(" + emailmsg + ",'[EXEEMAIL1]', emp_email1)";
			emailmsg = "REPLACE(" + emailmsg + ",'[MODELNAME]', COALESCE(model_name,''))";
			emailmsg = "REPLACE(" + emailmsg + ",'[ITEMNAME]', COALESCE(item_name,''))";
			emailmsg = "REPLACE(" + emailmsg + ",'[BRANCHNAME]', branch_name)";
			emailmsg = "REPLACE(" + emailmsg + ",'[BRANCHEMAIL1]', branch_email1)";
			emailmsg = "REPLACE(" + emailmsg + ",'[SERVICEDUEDATE]', DATE_FORMAT(veh_service_duedate,'%d/%m/%Y'))";

		}

		if (!contact_email_id.equals("")) {
			try {
				if (!type.equals("0")) {
					StrSql = "SELECT"
							+ "	branch_id,"
							+ " contact_id,"
							+ " CONCAT(title_desc, ' ', contact_fname,' ', contact_lname),"
							+ " branch_email1 ,"
							+ " '" + contact_email_id + "',"
							+ " " + unescapehtml(sub) + ","
							+ " " + unescapehtml(emailmsg) + ","
							+ " '" + ToLongDate(kknow()) + "',"
							+ " " + emp_id + ","
							+ " 0";
					if (type.equals("1")) {

						StrSql += " FROM " + compdb(comp_id) + "axela_sales_enquiry"
								+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_emp enq ON enq.emp_id = enquiry_emp_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle enqjob ON enqjob.jobtitle_id = enq.emp_jobtitle_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crm ON crm_enquiry_id = enquiry_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_emp crm ON crm.emp_id = crm_emp_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle crmjob ON crmjob.jobtitle_id = crm.emp_jobtitle_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id  "
								+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id  "
								+ " WHERE enquiry_id = " + value;
						// SOP("StrSql====enq===" + StrSql);

					} else if (type.equals("2")) {

						StrSql += " FROM " + compdb(comp_id) + "axela_preowned"
								+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = preowned_customer_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = preowned_contact_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = preowned_emp_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
								+ " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id  "
								+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id  "
								+ " WHERE preowned_id = " + value;
						// SOP("StrSql====enq===" + StrSql);

					} else if (type.equals("3")) {

						StrSql += " FROM " + compdb(comp_id) + "axela_service_veh"
								// + " LEFT JOIN " + compdb(comp_id) + "axela_service_jc ON jc_veh_id = veh_id "
								+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = veh_branch_id "
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id "
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id "
								+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = veh_entry_id "
								+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id "
								+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id "
								+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_item_id "
								+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id "
								+ " WHERE veh_id = " + value;
						// SOP("StrSql---->>" + StrSql);
					}
				}

				if (home.equals("yes")) {
					StrSql = "SELECT"
							+ "	IF(emp_branch_id = 0, 1 ,emp_branch_id),"
							+ " 0,"
							+ " '',"
							+ " branch_email1 ,"
							+ " '" + contact_email_id + "',"
							+ " '" + unescapehtml(sub) + "',"
							+ " '" + unescapehtml(emailmsg) + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " " + emp_id + ","
							+ " 0"
							+ " FROM " + compdb(comp_id) + "axela_emp";
					if (!branch_id.equals("0")) {
						StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id";
					} else {
						StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = 1";
					}

					StrSql += " WHERE emp_id = " + emp_id;
					// SOP("StrSql---->>" + StrSql);
				}

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email"
						+ " (email_branch_id,"
						+ " email_contact_id,"
						+ " email_contact,"
						+ " email_from,"
						+ " email_to,"
						+ " email_subject,"
						+ " email_msg,"
						+ " email_date,"
						+ " email_entry_id,"
						+ " email_sent)"
						+ " "
						+ StrSql + " LIMIT 1" + "";
				updateQuery(StrSql);
				msg = "<b>Email sent Successfully! </b><br></br>";
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		} else {
			msg = "<b>Contact Email is Blank!</b><br></br>";
		}

		return msg;
	}

	protected String SendSMS(String type, String value, String sms_id) throws SQLException {
		String smsmsg = "";

		// getting cannedsms_format
		StrSql = " SELECT "
				+ " cannedsms_format"
				+ " FROM  " + compdb(comp_id) + "axela_canned_sms"
				+ " WHERE cannedsms_id = " + sms_id;

		CachedRowSet crs = processQuery(StrSql, 0);
		// SOP("StrSql==111==" + StrSql);
		while (crs.next()) {
			smsmsg = crs.getString("cannedsms_format");
		}
		crs.close();

		// getting contact mobile1
		if (!type.equals("0")) {
			StrSql = "SELECT "
					+ " contact_mobile1,"
					+ " contact_mobile2 ";
			if (type.equals("1")) {
				StrSql += " FROM " + compdb(comp_id) + "axela_sales_enquiry "
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id "
						+ " WHERE enquiry_id = " + value;
			} else if (type.equals("2")) {
				StrSql += " FROM " + compdb(comp_id) + "axela_preowned "
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = preowned_contact_id "
						+ " WHERE preowned_id = " + value;
			} else if (type.equals("3")) {
				StrSql += " FROM " + compdb(comp_id) + "axela_service_veh "
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id "
						+ " WHERE veh_id = " + value;
			}
			// SOPInfo("StrSql==Sms====" + StrSql);
			crs = processQuery(StrSql, 0);

			while (crs.next()) {
				contact_mobile1 = crs.getString("contact_mobile1");
				contact_mobile2 = crs.getString("contact_mobile2");

			}
		}

		if (home.equals("yes")) {
			contact_mobile1 = value;
		}

		if (type.equals("1")) {
			smsmsg = "REPLACE('" + smsmsg + "','[ENQUIRYID]',enquiry_id)";
			smsmsg = "REPLACE(" + smsmsg + ",'[ENQUIRYNAME]',enquiry_title)";
			smsmsg = "REPLACE(" + smsmsg + ",'[CUSTOMERID]',customer_id)";
			smsmsg = "REPLACE(" + smsmsg + ",'[CUSTOMERNAME]',customer_name)";
			smsmsg = "REPLACE(" + smsmsg + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
			smsmsg = "REPLACE(" + smsmsg + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
			smsmsg = "REPLACE(" + smsmsg + ",'[CONTACTMOBILE1]',contact_mobile1)";
			smsmsg = "REPLACE(" + smsmsg + ",'[CONTACTPHONE1]',contact_phone1)";
			smsmsg = "REPLACE(" + smsmsg + ",'[CONTACTEMAIL1]',contact_email1)";
			smsmsg = "REPLACE(" + smsmsg + ",'[EXENAME]', enq.emp_name)";
			smsmsg = "REPLACE(" + smsmsg + ",'[EXEJOBTITLE]', enqjob.jobtitle_desc)";
			smsmsg = "REPLACE(" + smsmsg + ",'[EXEMOBILE1]', enq.emp_mobile1)";
			smsmsg = "REPLACE(" + smsmsg + ",'[EXEPHONE1]', enq.emp_phone1)";
			smsmsg = "REPLACE(" + smsmsg + ",'[EXEEMAIL1]', enq.emp_email1)";
			smsmsg = "REPLACE(" + smsmsg + ", '[CRMEXENAME]', crm.emp_name)";
			smsmsg = "REPLACE(" + smsmsg + ", '[CRMEXEJOBTITLE]', crmjob.jobtitle_desc)";
			smsmsg = "REPLACE(" + smsmsg + ", '[CRMEXEMOBILE1]', crm.emp_mobile1)";
			smsmsg = "REPLACE(" + smsmsg + ", '[CRMEXEPHONE1]', crm.emp_phone1)";
			smsmsg = "REPLACE(" + smsmsg + ", '[CRMEXEEMAIL1]', crm.emp_email1)";
			smsmsg = "replace(" + smsmsg + ",'[BRANCHNAME]',branch_name)";
			smsmsg = "replace(" + smsmsg + ",'[BRANCHEMAIL1]',branch_email1)";
			smsmsg = "replace(" + smsmsg + ",'[MODELNAME]',model_name)";
			smsmsg = "replace(" + smsmsg + ",'[ITEMNAME]',item_name)";
		} else if (type.equals("2")) {

			smsmsg = "replace('" + smsmsg + "', '[PREOWNEDID]',preowned_id)";
			smsmsg = "replace(" + smsmsg + ",'[PREOWNEDNAME]',preowned_title)";
			smsmsg = "replace(" + smsmsg + ",'[CUSTOMERID]',customer_id)";
			smsmsg = "replace(" + smsmsg + ",'[CUSTOMERNAME]',customer_name)";
			smsmsg = "replace(" + smsmsg + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
			smsmsg = "replace(" + smsmsg + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
			smsmsg = "replace(" + smsmsg + ",'[CONTACTMOBILE1]',contact_mobile1)";
			smsmsg = "replace(" + smsmsg + ",'[CONTACTPHONE1]',contact_phone1)";
			smsmsg = "replace(" + smsmsg + ",'[CONTACTEMAIL1]',contact_email1)";
			smsmsg = "replace(" + smsmsg + ",'[EXENAME]',emp_name)";
			smsmsg = "replace(" + smsmsg + ",'[EXEJOBTITLE]',jobtitle_desc)";
			smsmsg = "replace(" + smsmsg + ",'[EXEMOBILE1]',emp_mobile1)";
			smsmsg = "replace(" + smsmsg + ",'[EXEPHONE1]',emp_phone1)";
			smsmsg = "replace(" + smsmsg + ",'[EXEEMAIL1]',emp_email1)";
			smsmsg = "replace(" + smsmsg + ",'[BRANCHNAME]',branch_name)";
			smsmsg = "replace(" + smsmsg + ",'[MODELNAME]',preownedmodel_name)";
			smsmsg = "replace(" + smsmsg + ",'[ITEMNAME]',variant_name)";

		} else if (type.equals("3")) {
			smsmsg = "REPLACE('" + smsmsg + "','[VEHID]',veh_id)";
			smsmsg = "REPLACE(" + smsmsg + ", '[CUSTOMERID]', customer_id)";
			smsmsg = "REPLACE(" + smsmsg + ", '[CUSTOMERNAME]', customer_name)";
			smsmsg = "REPLACE(" + smsmsg + ", '[CONTACTNAME]', CONCAT(contact_fname, ' ', contact_lname))";
			smsmsg = "REPLACE(" + smsmsg + ", '[CONTACTJOBTITLE]', contact_jobtitle)";
			smsmsg = "REPLACE(" + smsmsg + ", '[CONTACTMOBILE1]', contact_mobile1)";
			smsmsg = "REPLACE(" + smsmsg + ", '[CONTACTPHONE1]', contact_phone1)";
			smsmsg = "REPLACE(" + smsmsg + ", '[CONTACTEMAIL1]', contact_email1)";
			smsmsg = "REPLACE(" + smsmsg + ", '[EXENAME]', emp_name)";
			smsmsg = "REPLACE(" + smsmsg + ", '[EXEJOBTITLE]', jobtitle_desc)";
			smsmsg = "REPLACE(" + smsmsg + ", '[EXEMOBILE1]', emp_mobile1)";
			smsmsg = "REPLACE(" + smsmsg + ", '[EXEPHONE1]', emp_phone1)";
			smsmsg = "REPLACE(" + smsmsg + ", '[EXEEMAIL1]', emp_email1)";
			smsmsg = "REPLACE(" + smsmsg + ", '[MODELNAME]', COALESCE(model_name,''))";
			smsmsg = "REPLACE(" + smsmsg + ", '[ITEMNAME]', COALESCE(item_name,''))";
			smsmsg = "REPLACE(" + smsmsg + ", '[REGNO]', COALESCE(veh_reg_no,''))";
			smsmsg = "REPLACE(" + smsmsg + ", '[SERVICEDUEDATE]', DATE_FORMAT(veh_service_duedate,'%d/%m/%Y'))";
		}

		if (!contact_mobile1.equals("")) {
			try {
				if (!type.equals("0")) {
					StrSql = "SELECT"
							+ " branch_id ,"
							+ " contact_id ,"
							+ " CONCAT(title_desc, ' ', contact_fname,' ', contact_lname),"
							+ " '" + contact_mobile1 + "' ,"
							+ " " + unescapehtml(smsmsg) + ","
							+ " '" + ToLongDate(kknow()) + "',"
							+ " 0,"
							+ " " + emp_id + "";

					if (type.equals("1")) {

						StrSql += " FROM " + compdb(comp_id) + "axela_sales_enquiry"
								+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_emp enq ON enq.emp_id = enquiry_emp_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle enqjob ON enqjob.jobtitle_id = enq.emp_jobtitle_id"

								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crm ON crm_enquiry_id = enquiry_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_emp crm ON crm.emp_id = crm_emp_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle crmjob ON crmjob.jobtitle_id = crm.emp_jobtitle_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id  "
								+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id  "
								+ " WHERE enquiry_id = " + value;
						// SOP("StrSql==1====3===" + StrSql);
					} else if (type.equals("2")) {

						StrSql += " FROM " + compdb(comp_id) + "axela_preowned"
								+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = preowned_customer_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = preowned_contact_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = preowned_emp_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
								+ " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id  "
								+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id  "
								+ " WHERE preowned_id = " + value;
						// SOP("StrSql====enq===" + StrSql);

					} else if (type.equals("3")) {

						StrSql += " FROM " + compdb(comp_id) + "axela_service_veh"
								+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = veh_branch_id "
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id "
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id "
								+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = veh_entry_id "
								+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id "
								+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id "
								+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_item_id "
								+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id "
								+ " WHERE veh_id = " + value;
						// SOP("StrSql=3==" + StrSql);

					}
				}

				if (home.equals("yes")) {
					StrSql = "SELECT"
							+ " if(emp_branch_id=0, 1 ,emp_branch_id) ,"
							+ " 0 ,"
							+ " '',"
							+ " '" + contact_mobile1 + "' ,"
							+ " '" + unescapehtml(smsmsg) + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " 0,"
							+ " " + emp_id + ""
							+ " FROM " + compdb(comp_id) + "axela_emp"
							+ " WHERE emp_id =" + emp_id;
					// SOP("StrSql---->>" + StrSql);
				}

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sms"
						+ " (sms_branch_id,"
						+ " sms_contact_id,"
						+ " sms_contact,"
						+ " sms_mobileno,"
						+ " sms_msg,"
						+ " sms_date,"
						+ " sms_sent,"
						+ " sms_entry_id)"
						+ " " + StrSql + " LIMIT 1" + "";
				// SOP("Sendsms-----------" + StrSql);
				updateQuery(StrSql);
				msg = "<b> SMS sent Successfully! </b><br></br>";
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}

		if (!contact_mobile2.equals("")) {
			try {
				StrSql = "SELECT"
						+ " branch_id ,"
						+ " contact_id ,"
						+ " CONCAT(title_desc, ' ', contact_fname,' ', contact_lname),"
						+ " " + contact_mobile2 + ","
						+ " " + unescapehtml(smsmsg) + ","
						+ " '" + ToLongDate(kknow()) + "',"
						+ " 0,"
						+ " " + emp_id + "";

				if (type.equals("1")) {

					StrSql += " FROM " + compdb(comp_id) + "axela_sales_enquiry"
							+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id  "
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id  "
							+ " WHERE enquiry_id = " + value;
				} else if (type.equals("2")) {

					StrSql += " FROM " + compdb(comp_id) + "axela_preowned"
							+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = preowned_customer_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = preowned_contact_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = preowned_emp_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
							+ " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id  "
							+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id  "
							+ " WHERE preowned_id = " + value;
					// SOP("StrSql====enq===" + StrSql);

				} else if (type.equals("3")) {

					if (!veh_id.equals("0")) {
						StrSql += " FROM axela_service_veh"
								+ " INNER JOIN axela_branch ON branch_id = veh_branch_id "
								+ " INNER JOIN axela_customer ON customer_id = veh_customer_id "
								+ " INNER JOIN axela_customer_contact ON contact_id = veh_contact_id "
								+ " INNER JOIN axela_emp ON emp_id = veh_entry_id "
								+ " INNER JOIN axela_title ON title_id = contact_title_id "
								+ " INNER JOIN axela_jobtitle ON jobtitle_id = emp_jobtitle_id "
								+ " INNER JOIN axela_inventory_item ON item_id = veh_item_id "
								+ " INNER JOIN axela_inventory_item_model ON model_id = item_model_id "
								+ " WHERE veh_id = " + value;
					}

				}
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sms"
						+ " (sms_branch_id,"
						+ " sms_contact_id,"
						+ " sms_contact,"
						+ " sms_mobileno,"
						+ " sms_msg,"
						+ " sms_date,"
						+ " sms_sent,"
						+ " sms_entry_id)"
						+ " " + StrSql + " LIMIT 1" + "";
				// SOP("-------Sendsms2-----------" + StrSql);
				updateQuery(StrSql);
				msg = "<b> SMS sent Successfully! </b><br></br>";
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}

		}
		if (contact_mobile1.equals("") && contact_mobile2.equals("")) {
			msg = "<b>Contact Mobile is Blank!</b><br></br>";
		}
		return msg;

	}

	public String PopulatePrincipal(String brand_id, String comp_id, HttpServletRequest request) {

		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT brand_id, brand_name "
					+ " FROM axela_brand "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE branch_active = 1" + BranchAccess
					+ " AND branch_branchtype_id = 3"
					+ " GROUP BY brand_id "
					+ " ORDER BY brand_name ";
			// // SOP("PopulateTeam query =====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value =0>Select Brand</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("brand_id")).append("");
				Str.append(Selectdrop(crs.getInt("brand_id"), brand_id));
				Str.append(">").append(crs.getString("brand_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateBranchType(String branch_type, HttpServletRequest request) {
		String BranchAccess = "";
		StringBuilder stringval = new StringBuilder();
		HttpSession session = request.getSession(true);
		BranchAccess = GetSession("BranchAccess", request);
		String comp_id = CNumeric(GetSession("comp_id", request));
		try {
			String SqlStr = "SELECT branchtype_id, branchtype_name"
					+ " FROM axela_branch_type"
					+ " WHERE 1 = 1";
			if (!branch_type.equals("")) {
				SqlStr += "	AND branchtype_id IN (" + branch_type + ")";
			}
			SqlStr += " ORDER BY branchtype_name";
			// SOP("SqlStr======" + SqlStr);
			CachedRowSet crs = processQuery(SqlStr, 0);
			stringval.append("<option value =0>Select Type</option>");
			while (crs.next()) {
				stringval.append("<option value='").append(crs.getString("branchtype_id")).append("' ");
				stringval.append(StrSelectdrop(crs.getString("branchtype_id"), branch_type));
				stringval.append(">").append(crs.getString("branchtype_name") + "</option>\n");
			}
			crs.close();
			return stringval.toString();
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}

}
