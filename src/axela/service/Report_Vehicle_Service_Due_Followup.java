package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToXLSX;
///
public class Report_Vehicle_Service_Due_Followup extends Connect {

	public String StrHTML = "";
	public String StrSearch = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String branch_id = "0", empcrm_id = "0", vehcalltype_id = "0";
	public String dr_branch_id = "0", veh_branch_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String pending_followup = "";
	public String go = "";
	public String veh_kms = "", start_time = "", starttime = "";
	public String months = "", end_time = "", endtime = "", vehfollowup_contactable_id = "",
			vehfollowup_searchby = "", vehfollowup_vehaction_id = "", vehfollowup_vehlostcase1_id = "";
	public String veh_emp_id = "";
	public String veh_id = "";
	public String exportB = "";
	public String exportcount = "";
	public String msg = "", veh_last = "", vlkms = "0", vkms = "0";
	public Report_Check reportexe = new Report_Check();
	public String[] brand_ids, region_ids, zone_ids, branch_ids, model_ids, advisor_ids, tech_ids, jccat_ids;
	public String brand_id = "", region_id = "", zone_id, model_id = "", advisor_id = "", tech_id = "", jccat_id = "";
	public String vehfollowup_dueservice = "";

	public axela.service.MIS_Check1 mischeck = new axela.service.MIS_Check1();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_service_vehicle_access, emp_service_insurance_access", request, response);
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				exportcount = ExecuteQuery("select comp_export_count from " + compdb(comp_id) + "axela_comp");
				go = PadQuotes(request.getParameter("submit_button"));
				exportB = PadQuotes(request.getParameter("btn_export"));
				GetValues(request, response);
				CheckForm();
				StrSearch = ExeAccess.replace("emp_id", "crm.emp_id");
				if (!brand_id.equals("")) {
					StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
				}
				if (!region_id.equals("")) {
					StrSearch += " AND branch_region_id IN (" + region_id + ") ";
				}
				if (!zone_id.equals("")) {
					StrSearch += " AND branch_zone_id IN (" + zone_id + ") ";
				}
				if (!branch_id.equals("")) {
					StrSearch += " AND veh_branch_id IN (" + branch_id + ")";
				}
				if (!empcrm_id.equals("") && !empcrm_id.equals("0")) {
					StrSearch += " AND vehfollowup_emp_id = " + empcrm_id + "";
				}
				if (!vehcalltype_id.equals("") && !vehcalltype_id.equals("0")) {
					StrSearch += " AND vehfollowup_vehcalltype_id = " + vehcalltype_id + "";
				}

				if (!vehfollowup_contactable_id.equals("") && !vehfollowup_contactable_id.equals("0")) {
					StrSearch += " AND veh_contactable_id = " + vehfollowup_contactable_id + "";
					StrSearch += " AND vehfollowup_contactable_id = " + vehfollowup_contactable_id + "";
				}
				if (!vehfollowup_vehaction_id.equals("") && !vehfollowup_vehaction_id.equals("0")) {
					StrSearch += " AND vehfollowup_vehaction_id = " + vehfollowup_vehaction_id + "";
				}
				if (!vehfollowup_vehlostcase1_id.equals("") && !vehfollowup_vehlostcase1_id.equals("0")) {
					StrSearch += " AND vehfollowup_vehlostcase1_id = " + vehfollowup_vehlostcase1_id + "";
				}
				if (!vehfollowup_dueservice.equals("") && !vehfollowup_dueservice.equals("0")) {
					StrSearch += " AND vehfollowup_dueservice = '" + vehfollowup_dueservice + "'";
				}

				StrSearch += " AND SUBSTR(vehfollowup_followup_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
						+ " AND SUBSTR(vehfollowup_followup_time, 1, 8)  <= SUBSTR('" + endtime + "', 1, 8) ";

				if (pending_followup.equals("1")) {
					StrSearch += " AND SUBSTR(vehfollowup_followup_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
							+ " AND SUBSTR(vehfollowup_followup_time, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) "
							+ " AND vehfollowup_contactable_id = 0"
							+ " AND SUBSTR(vehfollowup_followup_time, 1, 8) < SUBSTR('" + (ToLongDate(kknow())) + "', 1, 8) ";
				}
				if (go.equals("Go")) {

					// SOP("msg=" + msg);
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = ServiceDetails();
					}
				}
				if (exportB.equals("Export")) {
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						ServiceDetailsExport(request, response);
					}
				}

			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		zone_id = RetrunSelectArrVal(request, "dr_zone");
		zone_ids = request.getParameterValues("dr_zone");
		branch_id = RetrunSelectArrVal(request, "dr_branch_id");
		branch_ids = request.getParameterValues("dr_branch_id");

		empcrm_id = CNumeric(PadQuotes(request.getParameter("dr_emp_id")));
		vehcalltype_id = CNumeric(PadQuotes(request.getParameter("dr_vehcalltype_id")));
		start_time = PadQuotes(request.getParameter("txt_start_time"));
		end_time = PadQuotes(request.getParameter("txt_end_time"));

		vehfollowup_contactable_id = CNumeric(PadQuotes(request.getParameter("dr_vehfollowup_contactable_id")));
		vehfollowup_vehaction_id = CNumeric(PadQuotes(request.getParameter("dr_vehfollowup_vehaction_id")));
		vehfollowup_vehlostcase1_id = CNumeric(PadQuotes(request.getParameter("dr_vehfollowup_vehlostcase1_id")));
		vehfollowup_searchby = CNumeric(PadQuotes(request.getParameter("dr_vehfollowup_searchby")));
		vehfollowup_dueservice = PadQuotes(request.getParameter("dr_vehfollowup_dueservice"));
		// SOP("vehfollowup_searchby====>" + vehfollowup_searchby);
		if (start_time.equals("")) {
			start_time = strToShortDate(ToShortDate(kknow()));
		}

		if (end_time.equals("")) {
			end_time = strToShortDate(ToShortDate(kknow()));
		}

		starttime = ConvertShortDateToStr(start_time);
		endtime = ConvertShortDateToStr(end_time);

		pending_followup = PadQuotes(request.getParameter("chk_pending_followup"));
		// SOP("pending_followup====>" + pending_followup);
		if (pending_followup.equals("on")) {
			pending_followup = "1";
		} else {
			pending_followup = "0";
		}
	}
	protected void CheckForm() {
		msg = "";

		if (!end_time.equals("")) {
			if (isValidDateFormatShort(end_time)) {
				if (Long.parseLong(ConvertShortDateToStr(start_time)) > Long.parseLong(ConvertShortDateToStr(end_time))) {
					msg = msg + " <br>End Date cannot be less than Start Date!";
				}
			}
		} else {
			msg = msg + "<br>Enter valid End Date!";
		}
	}

	public String ServiceDetails() {
		try {
			int count = 0;
			StringBuilder Str = new StringBuilder();
			// String curryear = ToLongDate(kknow()).substring(0, 4);
			// String duedate = "CONCAT(DATE_FORMAT(DATE_SUB(concat('" + ToLongDate(kknow()).substring(0, 4) + "',substr(veh_sale_date, 5, 4),'000000'),INTERVAL 1 DAY),'%Y%m%d'),'000000')";
			StrSql = " SELECT veh_id, variant_name, vehfollowup_contactable_id, "
					+ " vehfollowup_vehaction_id, vehfollowup_booking_main, vehfollowup_vehlostcase1_id,"
					+ " COALESCE(crm.emp_id, 0) AS veh_crmemp_id,vehfollowup_dueservice,"
					+ " COALESCE(CONCAT(crm.emp_name,' (', crm.emp_ref_no, ')'), '') AS crmemp_name, "
					+ " veh_id, veh_reg_no, customer_id, customer_name, contact_id, veh_iacs,"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name,"
					+ " contact_mobile1, contact_mobile2, contact_email1, contact_email2,"
					+ " veh_chassis_no,veh_engine_no,veh_modelyear, branch_id, branch_name,"
					+ " COALESCE(veh_kms, 0) AS veh_kms, COALESCE(veh_cal_kms, 0) AS veh_cal_kms,"
					+ " COALESCE(veh_lastservice, '') AS veh_lastservice, COALESCE(veh_lastservice_kms, 0) AS veh_lastservice_kms,"
					+ " COALESCE(veh_calservicedate, '') AS veh_calservicedate,"
					+ " COALESCE(veh_service_duedate, '') AS duedate, veh_sale_date, COALESCE(veh_service_duekms, 0) AS duekms,"
					+ " COALESCE(vehfollowup_followup_time, '') AS vehfollowup_followup_time,"
					+ " COALESCE(bookingtype_name, '') AS bookingtype_name,"
					+ " vehfollowup_appt_time, vehfollowup_enquiry_time,"
					+ " COALESCE(dr.emp_name, '') AS dr_emp_name,"
					+ " COALESCE(vehfollowup_pickuplocation, '') AS vehfollowup_pickuplocation,"
					+ " COALESCE(vehlostcase1_name, '') AS vehlostcase1_name,"
					+ " COALESCE(vehfollowup_desc, '') AS vehfollowup_desc,"
					+ " COALESCE(notcontactable_name, '') AS notcontactable_name,"// reason
					+ " COALESCE(vehfollowup_kms, 0) AS vehfollowup_kms,"
					+ "	COALESCE(dmsstatus_name, '') AS dmsstatus_name,"
					+ " COALESCE(vehcalltype_name, '') vehcalltype_name"
					+ " FROM " + compdb(comp_id) + "axela_service_followup"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = vehfollowup_veh_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"

					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
					+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = veh_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp crm ON crm.emp_id = vehfollowup_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_followup_bookingtype ON bookingtype_id = vehfollowup_bookingtype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp dr ON dr.emp_id = vehfollowup_pickupdriver_emp_id "
					+ " LEFT JOIN axela_notcontactable ON notcontactable_id = vehfollowup_notcontactable_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_followup_lostcase1 ON vehlostcase1_id = vehfollowup_vehlostcase1_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_followup_action ON vehaction_id = vehfollowup_vehaction_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_followup_calltype ON vehcalltype_id = vehfollowup_vehcalltype_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_dms_status ON dmsstatus_id = veh_dmsstatus_id"
					+ " WHERE 1=1"
					+ " " + StrSearch;
			if (!vehfollowup_contactable_id.equals("") && !vehfollowup_contactable_id.equals("0")) {
				StrSql += " GROUP BY vehfollowup_id";
			} else {
				StrSql += " GROUP BY veh_id";
			}
			StrSql += " ORDER BY vehfollowup_id DESC"
					+ " LIMIT 3000";

			// SOP("StrSql====service due followup========" + StrSql);
			CachedRowSet crs1 = processQuery(StrSql, 0);
			if (crs1.isBeforeFirst()) {
				Str.append("<div class=\"table-responsive \">\n");
				Str.append("<table class=\"table table-bordered table-responsive table-hover\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\" align=center>#</th>\n");
				Str.append("<th align=center>Vehicle ID</th>\n");
				Str.append("<th style=\"width:200px;\" align=center>Customer</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Variant</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Reg. No.</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Chassis Number</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Engine No.</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Year</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Sale Date</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Last Service</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Call Type</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Due Service</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Contactable</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Action</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Branch</th>\n");
				Str.append("<th data-hide=\"phone\">Booking Time</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Booking Type</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Driver</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Address</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Follow-up Time</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Lost Case</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Reason</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>KMS</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Feedback</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>CRM Executive</th>\n");
				// Str.append("<th data-hide=\"phone, tablet\" align=center>Kms</th>\n");
				// Str.append("<th align=center>Cal.Service</th>\n");
				// Str.append("<th align=center>Service Due</th>\n");

				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");

				while (crs1.next()) {
					count++;
					Str.append("<tr onmouseover='ShowCustomerInfo(" + crs1.getString("veh_id") + ")' onmouseout='HideCustomerInfo(" + crs1.getString("veh_id") + ");'");
					Str.append(" style='height:200px'>\n");
					Str.append("<td align=center>").append(count).append("</td>");
					Str.append("<td align=center><a href=\"javascript:remote=window.open('vehicle-dash.jsp?veh_id=").append(crs1.getString("veh_id") + "#tabs-4")
							.append("','vehicledash','');remote.focus();\">");
					Str.append(crs1.getString("veh_id")).append("</a></td>\n");
					Str.append("<td valign=\"top\" align=\"left\" nowrap>");
					Str.append("<a href=\"../customer/customer-list.jsp?customer_id=");
					Str.append(crs1.getString("customer_id")).append("\">");
					Str.append(crs1.getString("customer_name")).append("</a>");
					Str.append("<br><a href=\"../customer/customer-contact-list.jsp?contact_id=");
					Str.append(crs1.getString("contact_id")).append("\">");
					Str.append(crs1.getString("contact_name")).append("</a>");
					if (!crs1.getString("contact_mobile1").equals("")) {
						Str.append("<br>").append(SplitPhoneNoSpan(crs1.getString("contact_mobile1"), 5, "M", crs1.getString("veh_id")))
								.append(ClickToCall(crs1.getString("contact_mobile1"), comp_id));
					}

					if (!crs1.getString("contact_mobile2").equals("")) {
						Str.append("<br>").append(SplitPhoneNoSpan(crs1.getString("contact_mobile2"), 5, "M", crs1.getString("veh_id")))
								.append(ClickToCall(crs1.getString("contact_mobile2"), comp_id));
					}

					if (!crs1.getString("contact_email1").equals("")) {
						Str.append("<br><span class='customer_info customer_" + crs1.getString("veh_id") + "'  style='display: none;'><a href=\"mailto:")
								.append(crs1.getString("contact_email1")).append("\">");
						Str.append(crs1.getString("contact_email1")).append("</a></span>");
					}

					if (!crs1.getString("contact_email2").equals("")) {
						Str.append("<br><span class='customer_info customer_" + crs1.getString("veh_id") + "'  style='display: none;'><a href=\"mailto:")
								.append(crs1.getString("contact_email2")).append("\">");
						Str.append(crs1.getString("contact_email2")).append("</a></span>");
					}
					Str.append("</td>");
					Str.append("<td align=left>").append(crs1.getString("variant_name")).append("</td>");
					Str.append("\n<td valign=\"top\" align=\"left\" nowrap><a href=\"../service/vehicle-list.jsp?veh_id=");
					Str.append(crs1.getString("veh_id")).append("\">").append(SplitRegNo(crs1.getString("veh_reg_no"), 2)).append("</a><br>");
					if (!crs1.getString("dmsstatus_name").equals("")) {
						Str.append("DMS Status:").append(crs1.getString("dmsstatus_name"));
					}
					Str.append("</td>");
					Str.append("<td align=\"center\" valign=\"top\">").append(crs1.getString("veh_chassis_no"));
					Str.append("</td>");
					Str.append("<td align=\"center\" valign=\"top\">").append(crs1.getString("veh_engine_no"));
					if (crs1.getString("veh_iacs").equals("1")) {
						Str.append("<br/><font color=\"red\"><b>IACS</b></font>");
					}
					Str.append("</td>");
					Str.append("<td align=\"center\" valign=\"top\">").append(crs1.getString("veh_modelyear"));
					Str.append("</td>");
					Str.append("<td align=center>").append(strToShortDate(crs1.getString("veh_sale_date"))).append("</td>");
					Str.append("<td align=center>");
					if (!strToShortDate(crs1.getString("veh_sale_date")).equals(crs1.getString("veh_lastservice")) && !crs1.getString("veh_lastservice").equals("")) {
						Str.append(strToShortDate(crs1.getString("veh_lastservice")));
						Str.append("<br>").append(IndFormat(crs1.getString("veh_lastservice_kms"))).append(" Kms");
						// Str.append(IndFormat(crs1.getString("veh_kms")));
					}
					Str.append("</td>\n");
					Str.append("<td valign=top align=left >").append(crs1.getString("vehcalltype_name")).append("</td>");
					Str.append("<td valign=top align=left >").append(crs1.getString("vehfollowup_dueservice")).append("</td>");
					Str.append("<td align=left>");
					if ((crs1.getString("vehfollowup_contactable_id")).equals("1")) {
						Str.append("Yes").append("</td>");
					} else if ((crs1.getString("vehfollowup_contactable_id")).equals("2")) {
						Str.append("No").append("</td>");
					}
					else if ((crs1.getString("vehfollowup_contactable_id")).equals("0")) {
						Str.append("").append("</td>");
					}
					Str.append("</td>");
					Str.append("<td align=left>");
					if (crs1.getString("vehfollowup_vehaction_id").equals("1")) {
						Str.append("Book A Service");
					} else if (crs1.getString("vehfollowup_vehaction_id").equals("2")) {
						Str.append("Call Latter");
					} else if (crs1.getString("vehfollowup_vehaction_id").equals("3")) {
						Str.append("No Service");
					}
					if (!crs1.getString("vehfollowup_enquiry_time").equals("")) {
						Str.append("<br>(Enquiry)");
					} else {
						Str.append("<br>(DMS)");
					}
					Str.append("</td>");

					Str.append("<td align=left><a href=../portal/branch-summary.jsp?branch_id=").append(crs1.getString("branch_id")).append(">")
							.append(crs1.getString("branch_name")).append("</a></td>");
					Str.append("<td valign=top align=center >").append(strToLongDate(crs1.getString("vehfollowup_appt_time"))).append("</td>");
					Str.append("<td align=center>").append(crs1.getString("bookingtype_name"));
					if (crs1.getString("vehfollowup_vehaction_id").equals("1") && crs1.getString("vehfollowup_booking_main").equals("1")) {
						Str.append("</br>(Main)");
					}
					else if (crs1.getString("vehfollowup_vehaction_id").equals("1") && crs1.getString("vehfollowup_booking_main").equals("0")) {
						Str.append("</br>(Postponed)");
					}
					Str.append("<td align=center>").append(crs1.getString("dr_emp_name")).append("</td>");
					Str.append("<td align=center>").append(crs1.getString("vehfollowup_pickuplocation")).append("</td>");
					Str.append("<td align=center><a href=\"javascript:remote=window.open('vehicle-dash.jsp?veh_id=").append(crs1.getString("veh_id") + "#tabs-4")
							.append("','vehdash','');remote.focus();\">").append(strToLongDate(crs1.getString("vehfollowup_followup_time"))).append("</a></td>\n");

					Str.append("<td align=center>").append(crs1.getString("vehlostcase1_name")).append("</td>");
					Str.append("<td align=center>").append(crs1.getString("notcontactable_name")).append("</td>");
					Str.append("<td align=center>").append(crs1.getString("vehfollowup_kms")).append("</td>");
					Str.append("<td align=center>").append(crs1.getString("vehfollowup_desc")).append("</td>");

					Str.append("<td align=left><a href=../portal/executive-summary.jsp?emp_id=").append(crs1.getString("veh_crmemp_id")).append(">")
							.append(crs1.getString("crmemp_name")).append("</a></td>");

					Str.append("</tr>");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<font color=red><b>No Service Due found!</b></font>");
			}
			crs1.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public void ServiceDetailsExport(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = " SELECT veh_id AS 'Vehicle ID',"
					+ " branch_name AS 'Branch',"
					+ " veh_reg_no AS 'Registration No.',"
					+ " veh_engine_no AS 'Engine No.',"
					+ " veh_chassis_no AS 'Chassis No.',"
					+ " customer_name AS 'Customer Name',"
					+ " CONCAT(title_desc,' ', contact_fname,' ', contact_lname) AS 'Contact Name',"
					+ " contact_mobile1 AS 'Contact Mobile1',"
					+ " contact_mobile2 AS 'Contact Mobile2',"
					+ " contact_phone1 AS 'Contact Phone1',"
					+ " contact_phone2 AS 'Contact Phone2',"
					+ " contact_email1 AS 'Contact Email1',"
					+ " contact_email2 AS 'Contact Email2',"
					+ " variant_name AS 'Item ',"
					+ " veh_modelyear AS 'Model Year',"
					+ " COALESCE(DATE_FORMAT(veh_sale_date, '%d/%m/%Y'),'') AS 'Sale Date',"
					+ " COALESCE(DATE_FORMAT(veh_lastservice, '%d/%m/%Y'),'') AS 'Last Service',"
					+ " COALESCE(veh_lastservice_kms, 0) AS 'Last Service Kms',"
					+ " COALESCE(vehcalltype_name, '') AS 'Call Type',"
					+ " COALESCE(vehfollowup_dueservice, '') AS 'Due Service',"
					+ " IF(vehfollowup_contactable_id = 1, 'Yes','No') AS 'Contactable',"
					+ " CASE WHEN vehfollowup_vehaction_id = 1 THEN 'Book A Service'"
					+ " WHEN vehfollowup_vehaction_id = 2 THEN 'Call Letter'"
					+ " WHEN vehfollowup_vehaction_id = 3 THEN 'No Service'"
					+ " END AS 'Action',"
					+ " COALESCE(DATE_FORMAT(vehfollowup_appt_time, '%d/%m/%Y %H:%i'),'') AS 'Booking Time',"
					+ " COALESCE(bookingtype_name, '') AS 'Booking Type',"
					+ " COALESCE(CONCAT(crm.emp_name,' (', crm.emp_ref_no, ')'), '') AS 'CRM Executive', "
					+ " COALESCE(dr.emp_name, '') AS 'Driver',"
					+ " COALESCE(vehfollowup_pickuplocation, '') AS 'Address',"
					+ " COALESCE(DATE_FORMAT(vehfollowup_followup_time, '%d/%m/%Y %H:%i'),'') AS 'Follow-up Time',"
					+ " COALESCE(vehlostcase1_name, '') AS 'Lost Case',"
					+ " COALESCE(notcontactable_name, '') AS 'Reason',"// reason
					+ " COALESCE(vehfollowup_kms, 0) AS 'KMS',"
					+ " COALESCE(vehfollowup_desc, '') AS 'Feedback'"
					+ " FROM " + compdb(comp_id) + "axela_service_followup"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = vehfollowup_veh_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
					+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = veh_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp crm ON crm.emp_id = vehfollowup_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_followup_bookingtype ON bookingtype_id = vehfollowup_bookingtype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp dr ON dr.emp_id = vehfollowup_pickupdriver_emp_id "
					+ " LEFT JOIN axela_notcontactable ON notcontactable_id = vehfollowup_notcontactable_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_followup_lostcase1 ON vehlostcase1_id = vehfollowup_vehlostcase1_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_followup_action ON vehaction_id = vehfollowup_vehaction_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_followup_calltype ON vehcalltype_id = vehfollowup_vehcalltype_id "
					+ " WHERE 1=1"
					+ StrSearch
					+ " GROUP BY veh_id"
					+ " ORDER BY vehfollowup_id DESC "
					+ " LIMIT 10000"; // + exportcount;

			// SOP("StrSql====service due Export========" + StrSqlBreaker(StrSql));
			CachedRowSet crs1 = processQuery(StrSql, 0);
			if (crs1.isBeforeFirst()) {
				new ExportToXLSX().Export(request, response, crs1, "VechicleFollowupDetails", comp_id);
			} else {
				msg = "No Records To Export!";
			}
			crs1.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateCRMExecutives(String comp_id, String branch_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = 1"
					+ " AND emp_crm = 1";
			if (!branch_id.equals("")) {
				StrSql += " AND (emp_branch_id IN ( " + branch_id + ")"
						+ " OR emp_branch_id = 0)";
			}
			StrSql += " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			// SOP("StrSql===" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_emp_id\" class=\"form-control\" id=\"dr_emp_id\" >");
			Str.append("<option value=\"0\">Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop(crs.getInt("emp_id"), empcrm_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateBranch() {
		StringBuilder Str = new StringBuilder();
		try {

			StrSql = "SELECT branch_id, branch_name"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_active = 1"
					+ " AND branch_branchtype_id IN (1,3)"
					+ " GROUP BY branch_id"
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// Str.append("<select name=\"dr_branch_id\" id=\"dr_branch_id\" class=\"selectbox\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("branch_id"));
				Str.append(Selectdrop(crs.getInt("branch_id"), branch_id));
				Str.append(">").append(crs.getString("branch_name")).append("</option>\n");
			}
			// Str.append("</select>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateCallTypeDays() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT vehcalltype_id, vehcalltype_name"
					+ " FROM " + compdb(comp_id) + "axela_service_followup_calltype"
					+ " WHERE 1 = 1"
					+ " GROUP BY vehcalltype_id"
					+ " ORDER BY vehcalltype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// Str.append("<select name=\"dr_branch_id\" id=\"dr_branch_id\" class=\"selectbox\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("vehcalltype_id"));
				Str.append(Selectdrop(crs.getInt("vehcalltype_id"), vehcalltype_id));
				Str.append(">").append(crs.getString("vehcalltype_name")).append("</option>\n");
			}
			Str.append("</select>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateServiceContactable(String comp_id, String vehfollowup_contactable_id) {
		StringBuilder Str = new StringBuilder();
		try {
			// Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"1\" " + Selectdrop(1, vehfollowup_contactable_id) + ">Yes</option>");
			Str.append("<option value=\"2\" " + Selectdrop(2, vehfollowup_contactable_id) + ">No</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateServiceAction(String comp_id, String vehfollowup_vehaction_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT vehaction_id, vehaction_name"
					+ " FROM " + compdb(comp_id) + "axela_service_followup_action"
					+ " WHERE 1=1"
					+ " GROUP BY vehaction_id"
					+ " ORDER BY vehaction_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			// Str.append("<select name=\"dr_item_id\" id=\"dr_item_id\" class=\"selectbox\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("vehaction_id"));
				Str.append(StrSelectdrop(crs.getString("vehaction_id"), vehfollowup_vehaction_id));
				Str.append(">").append(crs.getString("vehaction_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateLostcase(String comp_id, String vehfollowup_vehlostcase1_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT vehlostcase1_id, vehlostcase1_name"
					+ " FROM " + compdb(comp_id) + "axela_service_followup_lostcase1"
					+ " WHERE 1=1"
					+ " AND vehlostcase1_name LIKE 'Dissatisfied%'"
					+ " GROUP BY vehlostcase1_id"
					+ " ORDER BY vehlostcase1_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			// Str.append("<select name=\"dr_item_id\" id=\"dr_item_id\" class=\"selectbox\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("vehlostcase1_id"));
				Str.append(StrSelectdrop(crs.getString("vehlostcase1_id"), vehfollowup_vehlostcase1_id));
				Str.append(">").append(crs.getString("vehlostcase1_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateServiceSearchBy(String comp_id, String vehfollowup_contactable_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"1\" " + Selectdrop(1, vehfollowup_contactable_id) + ">Follow-up Date</option>");
			Str.append("<option value=\"2\" " + Selectdrop(2, vehfollowup_contactable_id) + ">Booking Date</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateDueService(String comp_id, String vehfollowup_dueservice) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT vehfollowup_dueservice"
					+ " FROM " + compdb(comp_id) + "axela_service_followup"
					+ " WHERE 1=1"
					+ " AND vehfollowup_dueservice != ''"
					+ " GROUP BY vehfollowup_dueservice"
					+ " ORDER BY vehfollowup_dueservice";
			// SOP("StrSql==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=\"").append(crs.getString("vehfollowup_dueservice") + "\"");
				Str.append(StrSelectdrop(crs.getString("vehfollowup_dueservice"), vehfollowup_dueservice));
				Str.append(">").append(crs.getString("vehfollowup_dueservice")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
}
