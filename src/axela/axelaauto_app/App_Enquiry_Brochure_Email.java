package axela.axelaauto_app;
//smitha nag 10 & 12,13 april, 2013
// modified by sn 6, 7 may 2013, 13,14 may
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.portal.Header;
import cloudify.connect.Connect;

public class App_Enquiry_Brochure_Email extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String StrSql = "";
	public String attachment = "";
	public String enquiry_id = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String contact_email1 = "", contact_email2 = "";
	public String StrHTML = "";
	public String branch_email1 = "";
	public String config_email_enable = "";
	public String comp_email_enable = "";
	public String brandconfig_enquiry_brochure_email_enable = "";
	public String brandconfig_enquiry_brochure_email_format = "";
	public String brandconfig_enquiry_brochure_email_sub = "";
	public String TotalRecords = "";
	public String enquiry_contact_id = "", contact_name = "";
	public String sendB = "";
	public String StrSearch = "";
	public String[] chk_brochure = null;
	public String enquiry_model_id = "", branch_brand_id = "", branch_rateclass_id = "", enquiry_branch_id = "", brochure_model_id = "", SrcModel = "", model_name = "";
	public String msg = "";
	public String emp_uuid = "", access = "", emp_all_exe = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(PadQuotes(request.getParameter("comp_id")));
			emp_uuid = PadQuotes(request.getParameter("emp_uuid"));
			CheckAppSession(emp_uuid, comp_id, request);
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				access = ReturnPerm(comp_id, "emp_enquiry_access", request);
				if (access.equals("0"))
				{
					response.sendRedirect("callurl" + "app-error.jsp?msg=Access denied. Please contact system administrator!");
				}

				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				// emp_all_exe = GetSession("emp_all_exe", request);
				// if (emp_all_exe.equals("1"))
				// {
				// ExeAccess = "";
				// }
				enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
				StrSql = "SELECT enquiry_branch_id, enquiry_model_id, branch_brand_id, branch_rateclass_id "
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch" + " ON branch_id	= enquiry_branch_id"
						+ " WHERE enquiry_id = " + enquiry_id
						+ BranchAccess.replace("branch_id", "enquiry_branch_id")
						+ ExeAccess.replace("emp_id", "enquiry_emp_id") + "";
				StrSql += " GROUP BY enquiry_id "
						+ " ORDER BY enquiry_id desc ";
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						enquiry_branch_id = crs.getString("enquiry_branch_id");
						enquiry_model_id = crs.getString("enquiry_model_id");
						branch_brand_id = crs.getString("branch_brand_id");
						branch_rateclass_id = crs.getString("branch_rateclass_id");
					}
				} else {
					response.sendRedirect("app-error.jsp?msg=Invalid Enquiry!");
				}
				crs.close();
				brochure_model_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_model_id")));
				if (!brochure_model_id.equals("0")) {
					if (!enquiry_model_id.equals(brochure_model_id)) {
						enquiry_model_id = brochure_model_id;
					}
				}
				sendB = PadQuotes(request.getParameter("send_button"));
				StrHTML = ListBrochure();
				if ("Send Email".equals(sendB)) {
					msg = "";
					GetValues(request, response);
					CheckForm(request, response);
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					} else {
						if (!attachment.equals("")) {
							PopulateFields();
							if (comp_email_enable.equals("1") && config_email_enable.equals("1")
									&& !branch_email1.equals("") && brandconfig_enquiry_brochure_email_enable.equals("1")
									&& !contact_email1.equals("") && !brandconfig_enquiry_brochure_email_format.equals("")
									&& !brandconfig_enquiry_brochure_email_sub.equals("")) {
								SendEmail();
								response.sendRedirect("callurlapp-enquiry-list.jsp?enquiry_id=" + enquiry_id + "&msg=Email sent successfully!");
							} else {
								if (!comp_email_enable.equals("1")) {
									msg = msg + "<br>Email Option is Disabled!";
								}
								if (!config_email_enable.equals("1")) {
									msg = msg + "<br>Email Gateway is Disabled!";
								}
								if (branch_email1.equals("")) {
									msg = msg + "<br>Branch Email is Blank!";
								}
								if (contact_email1.equals("")) {
									msg = msg + "<br>Contact Email is Blank!";
								}
								if (!brandconfig_enquiry_brochure_email_enable.equals("1")) {
									msg = msg + "<br>Brochure Email Option is Disabled!";
								}
								if (brandconfig_enquiry_brochure_email_format.equals("")) {
									msg = msg + "<br>Brochure Email Format is Blank!";
								}
								if (brandconfig_enquiry_brochure_email_sub.equals("")) {
									msg = msg + "<br>Brochure Email Subject is Blank!";
								}
								// response.sendRedirect("../portal/app-error.jsp?msg="
								// + msg + "");
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException, SQLException {
		TotalRecords = CNumeric(PadQuotes(request.getParameter("txt_count")));
	}

	protected void CheckForm(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException, SQLException {
		int count = 0;
		msg = "";
		chk_brochure = new String[Integer.parseInt(TotalRecords)];
		for (int i = 0; i < Integer.parseInt(TotalRecords); i++) {
			count++;
			chk_brochure[i] = PadQuotes(request.getParameter("chk_brochure" + count));
			if (chk_brochure[i].equals("on")) {
				StrSearch = StrSearch + PadQuotes(request.getParameter("hid_brochure" + count)) + "";
				StrSearch = StrSearch + ",";
				chk_brochure[i] = "1";
			} else {
				chk_brochure[i] = "0";
			}
		}
		if (!StrSearch.equals("")) {
			StrSearch = StrSearch.substring(0, StrSearch.lastIndexOf(","));
			StrSearch = "  AND brochure_id in (" + StrSearch + ")";

			StrSql = "SELECT brochure_value, brochure_title"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id	 = enquiry_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_brochure ON brochure_rateclass_id = branch_rateclass_id AND brochure_brand_id = branch_brand_id"
					+ " WHERE enquiry_id = " + enquiry_id + StrSearch + ""
					+ " GROUP BY brochure_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				attachment = "";
				while (crs.next()) {
					attachment = attachment + EnquiryBrochurePath(comp_id) + crs.getString("brochure_value") + ","
							+ crs.getString("brochure_title") + fileext(crs.getString("brochure_value"));
					attachment = attachment + ";";
				}
			}
			// SOP("attachment isssss " + attachment);
			attachment = attachment.substring(0, attachment.lastIndexOf(";"));
			crs.close();
		} else {
			msg = msg + "<br>Select Atleast 1 Brochure!";
		}
	}
	protected void SendEmail() {
		String msg = "", sub = "", contact_email_foremail = "";
		model_name = ExecuteQuery("SELECT model_name"
				+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
				+ " WHERE model_id = " + enquiry_model_id + "");
		if (!contact_email2.equals("")) {
			contact_email_foremail = contact_email1 + "," + contact_email2;
		} else {
			contact_email_foremail = contact_email1;
		}
		msg = (brandconfig_enquiry_brochure_email_format);
		sub = (brandconfig_enquiry_brochure_email_sub);

		sub = "REPLACE('" + sub + "','[ENQUIRYID]', enquiry_id)";
		sub = "REPLACE(" + sub + ",'[ENQUIRYNAME]',enquiry_title)";
		sub = "REPLACE(" + sub + ",'[CUSTOMERID]',customer_id)";
		sub = "REPLACE(" + sub + ",'[CUSTOMERNAME]',customer_name)";
		sub = "REPLACE(" + sub + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		sub = "REPLACE(" + sub + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
		sub = "REPLACE(" + sub + ",'[CONTACTMOBILE1]',contact_mobile1)";
		sub = "REPLACE(" + sub + ",'[CONTACTPHONE1]',contact_phone1)";
		sub = "REPLACE(" + sub + ",'[CONTACTEMAIL1]',contact_email1)";
		sub = "REPLACE(" + sub + ",'[EXENAME]',emp_name)";
		sub = "REPLACE(" + sub + ",'[EXEJOBTITLE]',jobtitle_desc)";
		sub = "REPLACE(" + sub + ",'[EXEMOBILE1]',emp_mobile1)";
		sub = "REPLACE(" + sub + ",'[EXEPHONE1]',emp_phone1)";
		sub = "REPLACE(" + sub + ",'[EXEEMAIL1]',emp_email1)";
		sub = "REPLACE(" + sub + ",'[BRANCHEMAIL1]',branch_email1)";
		sub = "REPLACE(" + sub + ",'[MODELNAME]','" + model_name + "')";
		sub = "REPLACE(" + sub + ",'[ITEMNAME]',item_name)";
		sub = "REPLACE(" + sub + ", '[BRANCHADDRESS]',branch_add)";

		msg = "REPLACE('" + msg + "','[ENQUIRYID]',enquiry_id)";
		msg = "REPLACE(" + msg + ",'[ENQUIRYNAME]',enquiry_title)";
		msg = "REPLACE(" + msg + ",'[CUSTOMERID]',customer_id)";
		msg = "REPLACE(" + msg + ",'[CUSTOMERNAME]',customer_name)";
		msg = "REPLACE(" + msg + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		msg = "REPLACE(" + msg + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
		msg = "REPLACE(" + msg + ",'[CONTACTMOBILE1]',contact_mobile1)";
		msg = "REPLACE(" + msg + ",'[CONTACTPHONE1]',contact_phone1)";
		msg = "REPLACE(" + msg + ",'[CONTACTEMAIL1]',contact_email1)";
		msg = "REPLACE(" + msg + ",'[EXENAME]',emp_name)";
		msg = "REPLACE(" + msg + ",'[EXEJOBTITLE]',jobtitle_desc)";
		msg = "REPLACE(" + msg + ",'[EXEMOBILE1]',emp_mobile1)";
		msg = "REPLACE(" + msg + ",'[EXEPHONE1]',emp_phone1)";
		msg = "REPLACE(" + msg + ",'[EXEEMAIL1]',emp_email1)";
		msg = "REPLACE(" + msg + ",'[BRANCHEMAIL1]',branch_email1)";
		msg = "REPLACE(" + msg + ",'[MODELNAME]',model_name)";
		msg = "REPLACE(" + msg + ",'[ITEMNAME]',item_name)";
		msg = "REPLACE(" + msg + ", '[BRANCHADDRESS]',branch_add)";

		try {
			StrSql = "SELECT"
					+ "	branch_id,"
					+ " '" + enquiry_contact_id + "',"
					+ " '" + contact_name + "',"
					+ " '" + branch_email1 + "',"
					+ " '" + contact_email_foremail + "',"
					+ " " + unescapehtml(sub) + ","
					+ " " + unescapehtml(msg) + ","
					+ " '" + attachment.replace("\\", "/") + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " " + emp_id + ","
					+ " 0 "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry   "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_entry_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON contact_title_id = title_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON emp_jobtitle_id = jobtitle_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id  "
					+ " WHERE enquiry_id = " + enquiry_id + "";
			StrSql = "INSERT into " + compdb(comp_id) + "axela_email"
					+ " ("
					+ "	email_branch_id,"
					+ "	email_contact_id,"
					+ " email_contact,"
					+ " email_from,"
					+ " email_to,"
					+ " email_subject,"
					+ " email_msg,"
					+ " email_attach1,"
					+ " email_date,"
					+ " email_entry_id,"
					+ " email_sent)"
					+ " " + StrSql + "";
			// SOP("StrSql==in brochure=email==" + StrSql);
			updateQuery(StrSql);
		} catch (Exception e) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	public String ListBrochure() {
		int count = 0;
		CachedRowSet crs = null;
		StringBuilder Str = new StringBuilder();
		String StrModel = "";
		try {
			StrSql = "SELECT brochure_id, brochure_title, brochure_value,"
					+ " COALESCE(item_id,0) as item_id, item_url,"
					+ " COALESCE(item_name, 'General') as item_name, "
					+ " COALESCE(model_name, 'General') as model_name,  "
					+ " COALESCE(model_name, '') as modelorder "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_brochure "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch " + " ON brochure_rateclass_id = branch_rateclass_id "
					+ " AND brochure_brand_id = branch_brand_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item " + " ON item_id = brochure_item_id  "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model " + " ON model_id = item_model_id   "
					+ " WHERE 1=1"
					+ " AND branch_id=" + enquiry_branch_id + " "
					+ " AND (model_id =" + enquiry_model_id + " or brochure_item_id=0) "
					+ " ORDER BY modelorder, item_name, brochure_title";
			// SOPInfo("StrSql--brocher--app-----" + StrSqlBreaker(StrSql));
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				// Str.append("<table  border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				Str.append("<table class=table  table-hover table-bordered><br><br><br>");
				Str.append(" <thead>");
				Str.append("<tr align=center><td colspan=5>");
				Str.append("<b>Model : ");
				Str.append(PopulateModel());
				Str.append("</td></tr>\n");
				Str.append("<tr align=center>\n");
				Str.append("<th width=5%>#</th>\n");
				Str.append("<th width=5%>Select</th>\n");
				Str.append("<th>Model</th>\n");
				Str.append("<th>Item</th>\n");
				Str.append("<th align=center>Brochure</th>\n");
				Str.append("</tr>\n");
				Str.append(" /<thead>\n");
				while (crs.next()) {
					count++;
					Str.append("<tbody>");
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center>").append(count).append("</td>\n");

					Str.append("<td>\n");
					Str.append("<input type=\"checkbox\" id=\"chk_brochure\" class=\"icheck\"").append(count);
					Str.append(" name=chk_brochure").append(count).append(" />");
					Str.append("<input type=\"hidden\" id=\"hid_brochure\" class=\"icheck\"").append(count);
					Str.append(" name=hid_brochure").append(count);
					Str.append(" value=").append(crs.getString("brochure_id")).append(" />");
					Str.append("</td>\n");
					/*
					 * Str.append("<td align=center>\n"); Str.append("<input type=\"checkbox\" id=\"chk_brochure\"" ).append(count); Str.append(" name=chk_brochure").append(count ).append(" />");
					 * Str.append("<input type=\"hidden\" id=\"hid_brochure\"" ).append(count); Str.append(" name=hid_brochure").append(count); Str.append
					 * (" value=").append(crs.getString("brochure_id")). append(" />"); Str.append("</td>\n");
					 */
					Str.append("<td valign=top align=left >").append(crs.getString("model_name")).append("</td>\n");
					// Str.append("<td valign=top align=left >").append(crs.getString("item_name")).append("</td>\n");
					Str.append("<td valign=top align=left >");
					if (!crs.getString("item_id").equals("0") && !crs.getString("item_url").equals("")) {
						Str.append("<a href=app-remote.jsp?item_id=" + crs.getString("item_id") + " target=_blank>");
						Str.append(crs.getString("item_name"));
						Str.append("</a>");
					} else {
						Str.append(crs.getString("item_name"));
					}
					Str.append("</td>\n");
					if (!crs.getString("brochure_value").equals("")) {
						if (!new File(EnquiryBrochurePath(comp_id)).exists()) {
							new File(EnquiryBrochurePath(comp_id)).mkdirs();
						}
						File f = new File(EnquiryBrochurePath(comp_id) + crs.getString("brochure_value"));
						// dnt delete... (uncomment for document download)
						Str.append("<td valign=top align=left ><a href=../Fetchdocs.do?" + "enquiry_brochure_id=");
						Str.append(crs.getString("brochure_id")).append("><b>").append(crs.getString("brochure_title"));
						Str.append("</a></b> ").append(ConvertFileSizeToBytes(FileSize(f))).append(")<br> ");
						//
						// if u dnt need doc download use the below code...
						// Str.append("<td valign=top align=left >").append(crs.getString("brochure_title"));
						// Str.append(" (").append(ConvertFileSizeToBytes(FileSize(f))).append(")<br> ");
						// Str.append("</td>\n");
						//
					}
					Str.append("</tr>\n");
					Str.append("</tbody>");
				}

				Str.append("<tr>");
				Str.append("<td align=center colspan='5' valign='middle'>\n");
				Str.append("<input name='txt_count' type='hidden' id='txt_count' value='").append(count);
				Str.append("'/><input name='send_button' type='submit' class='button' id='send_button' value='Send Email'/>");
				Str.append("<input name=txt_brochure_count id=txt_brochure_count type=hidden  value=");
				Str.append(count).append(" />");
				Str.append("</td>");
				Str.append("</tr>\n");
				Str.append("</table>\n");
			} else {
				msg = "No Brochures Found";
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	protected void PopulateFields() {
		try {
			StrSql = "SELECT enquiry_branch_id, enquiry_contact_id, contact_lname, contact_fname, "
					+ " title_desc, contact_email1, contact_email2, "
					+ " COALESCE(brandconfig_enquiry_brochure_email_enable,'') AS brandconfig_enquiry_brochure_email_enable, "
					+ " COALESCE(brandconfig_enquiry_brochure_email_sub,'') AS brandconfig_enquiry_brochure_email_sub, "
					+ " COALESCE(brandconfig_enquiry_brochure_email_format,'') AS brandconfig_enquiry_brochure_email_format, "
					+ " COALESCE(branch_email1,'') AS branch_email1, config_email_enable, comp_email_enable "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_brand_config ON brandconfig_brand_id = branch_brand_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id,"
					+ " " + compdb(comp_id) + "axela_config, " + compdb(comp_id) + "axela_comp  "
					+ " WHERE enquiry_id = " + enquiry_id + ExeAccess + BranchAccess;
			// SOP("PopulateFields " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				enquiry_contact_id = crs.getString("enquiry_contact_id");
				contact_name = crs.getString("title_desc") + " " + crs.getString("contact_fname") + " " + crs.getString("contact_lname");
				contact_email1 = crs.getString("contact_email1");
				contact_email2 = crs.getString("contact_email2");
				brandconfig_enquiry_brochure_email_enable = crs.getString("brandconfig_enquiry_brochure_email_enable");
				brandconfig_enquiry_brochure_email_format = crs.getString("brandconfig_enquiry_brochure_email_format");
				brandconfig_enquiry_brochure_email_sub = crs.getString("brandconfig_enquiry_brochure_email_sub");
				branch_email1 = crs.getString("branch_email1");
				config_email_enable = crs.getString("config_email_enable");
				comp_email_enable = crs.getString("comp_email_enable");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateModel() {
		StringBuilder Str = new StringBuilder();
		try {

			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " INNER JOIN axela_brand " + " ON brand_id = model_brand_id"
					+ " WHERE 1=1 and model_active = '1' "
					+ " AND model_sales=1 "
					+ " AND model_brand_id =" + branch_brand_id
					+ " GROUP BY model_id"
					+ " ORDER BY model_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_enquiry_model_id\" id=\"dr_enquiry_model_id\" class=\"form-control\" onChange=\"document.form1.submit()\" >");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id")).append("");
				Str.append(StrSelectdrop(crs.getString("model_id"), enquiry_model_id));
				Str.append(">").append(crs.getString("model_name")).append("</option>\n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App=== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
