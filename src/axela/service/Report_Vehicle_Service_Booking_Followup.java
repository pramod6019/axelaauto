package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToHTML;
import cloudify.connect.ExportToXLSX;

public class Report_Vehicle_Service_Booking_Followup extends Connect {

	public String StrHTML = "", StrPostponed = "";
	public String StrSearch = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String branch_id = "0", empcrm_id = "0", vehcalltype_id = "0", vehfollowup_bookingtype_id = "0";
	public String post_veh_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String pending_followup = "", active_booking = "1";
	public String go = "";
	public String veh_kms = "", start_time = "", starttime1 = "", starttime = "";
	public String months = "", end_time = "", endtime = "", endtime1 = "";
	public String veh_emp_id = "", addpostpone = "", postponed_time = "";
	public String veh_id = "";
	public String calltype_id = "0", emp_post_id = "0";
	public String msg = "";
	public String refresh = "";
	public String exportB = "";
	public String printB = "", printoption = "";
	public Report_Check reportexe = new Report_Check();
	public axela.service.MIS_Check1 mischeck = new axela.service.MIS_Check1();

	public String[] brand_ids, region_ids, zone_ids, branch_ids, preownedmodel_ids, advisor_ids, tech_ids, jccat_ids;
	public String brand_id = "", region_id = "", zone_id, preownedmodel_id = "", advisor_id = "", tech_id = "", jccat_id = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_service_vehicle_access, emp_service_insurance_access", request, response);
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				starttime = strToShortDate(ToShortDate(kknow()));
				endtime = strToShortDate(ToShortDate(kknow()));
				exportB = PadQuotes(request.getParameter("btn_export"));
				printB = PadQuotes(request.getParameter("btn_print"));
				go = PadQuotes(request.getParameter("submit_button"));
				refresh = PadQuotes(request.getParameter("refresh"));
				StrSearch = ExeAccess.replace("emp_id", "crm.emp_id");
				GetValues(request, response);

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
					StrSearch += " AND vehfollowup_workshop_branch_id IN (" + branch_id + ")";
				}

				if (!vehcalltype_id.equals("") && !vehcalltype_id.equals("0")) {
					StrSearch += " AND vehfollowup_vehcalltype_id = " + vehcalltype_id + "";
				}
				if (!vehfollowup_bookingtype_id.equals("") && !vehfollowup_bookingtype_id.equals("0")) {
					StrSearch += " AND vehfollowup_bookingtype_id = " + vehfollowup_bookingtype_id + "";
				}
				// SOP("empcrm_id===" + empcrm_id);
				if (!empcrm_id.equals("") && !empcrm_id.equals("0")) {
					StrSearch += " AND vehfollowup_emp_id = " + empcrm_id + "";
				}
				if (active_booking.equals("1")) {
					StrSearch += " AND vehfollowup_postponed = 0"
							+ " AND vehfollowup_veh_id NOT IN ( SELECT followup.vehfollowup_veh_id "
							+ " FROM  " + compdb(comp_id) + "axela_service_followup followup"
							+ " WHERE 1 = 1"
							+ " AND followup.vehfollowup_id > main.vehfollowup_id"
							+ " AND followup.vehfollowup_veh_id = main.vehfollowup_veh_id"
							+ " AND vehfollowup_desc LIKE 'Already serviced at%')";
				}
				if (pending_followup.equals("1")) {
					StrSearch += " AND vehfollowup_postponed = 0"
							+ " AND vehfollowup_desc = ''";
				}

				if (go.equals("Go")) {
					// / GetValues(request, response);
					CheckForm();
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = ServiceDetails();
					}
				}
				if (exportB.equals("Export")) {
					GetValues(request, response);
					CheckForm();
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						ServiceBookingDetailsExport(request, response);
					}
				}
				if (printB.equals("Print")) {
					GetValues(request, response);
					CheckForm();
					// if (vehfollowup_bookingtype_id.equals("2")) {
					printoption = "html";
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						ServiceBookingPickupDropPrint(request, response);
					}
					// } else {
					// msg = "Error!" + "<br>Booking Type Should be Only Pickup & Drop For Print!";
					// }
				}

			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
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
		vehfollowup_bookingtype_id = CNumeric(PadQuotes(request.getParameter("dr_vehfollowup_bookingtype_id")));

		zone_ids = request.getParameterValues("dr_zone");
		branch_id = RetrunSelectArrVal(request, "dr_branch_id");
		branch_ids = request.getParameterValues("dr_branch_id");
		pending_followup = PadQuotes(request.getParameter("chk_pending_followup"));
		if (pending_followup.equals("on")) {
			pending_followup = "1";
		} else {
			pending_followup = "0";
		}

		active_booking = PadQuotes(request.getParameter("chk_active_booking"));
		if (active_booking.equals("on")) {
			active_booking = "1";
		} else {
			active_booking = "0";
		}
		empcrm_id = CNumeric(PadQuotes(request.getParameter("dr_emp_id")));
		vehcalltype_id = CNumeric(PadQuotes(request.getParameter("dr_vehcalltype_id")));
		starttime = PadQuotes(request.getParameter("txt_start_time"));
		endtime = PadQuotes(request.getParameter("txt_end_time"));

		if (refresh.equals("yes") && starttime.equals("") && endtime.equals("")) {
			starttime = PadQuotes(request.getParameter("start_time"));
			endtime = PadQuotes(request.getParameter("end_time"));
			empcrm_id = CNumeric(PadQuotes(request.getParameter("dr1_emp_id")));
			active_booking = PadQuotes(request.getParameter("active_booking"));
			pending_followup = PadQuotes(request.getParameter("pending_followup"));
			vehcalltype_id = CNumeric(PadQuotes(request.getParameter("vehcalltype_id")));
			vehfollowup_bookingtype_id = CNumeric(PadQuotes(request.getParameter("vehfollowup_bookingtype_id")));
			brand_id = RetrunSelectArrVal(request, "dr_principal");
			// brand_ids = request.getParameterValues("dr_principal");
			brand_ids = brand_id.split(",");
			region_id = RetrunSelectArrVal(request, "dr_region");
			// region_ids = request.getParameterValues("dr_region");
			region_ids = region_id.split(",");
			zone_id = RetrunSelectArrVal(request, "dr_zone");
			// zone_ids = request.getParameterValues("dr_zone");
			zone_ids = zone_id.split(",");
			branch_id = RetrunSelectArrVal(request, "dr1_branch_id");
			// branch_ids = request.getParameterValues("dr_branch_id");
			branch_ids = branch_id.split(",");
			starttime = strToShortDate(starttime);
			endtime = strToShortDate(endtime);
		}

		if (starttime.equals("")) {
			starttime = strToShortDate(ToShortDate(kknow()));
		}

		if (endtime.equals("")) {
			endtime = strToShortDate(ToShortDate(kknow()));
		}

		start_time = ConvertShortDateToStr(starttime);
		end_time = ConvertShortDateToStr(endtime);
	}

	protected void CheckForm() {
		msg = "";
		// SOP(zone_id);
		// SOP(booking_branch_id);
		// if (zone_id.equals("")) {
		// msg = msg + "<br>Select Zone!";
		// }
		if (starttime.equals("")) {
			msg = msg + "<br>Select Start Date!";
		} else {
			if (isValidDateFormatShort(starttime)) {
				start_time = ConvertShortDateToStr(starttime);
			} else {
				msg = msg + "<br>Enter Valid Start Date!";
				start_time = "";
			}
		}
		if (endtime.equals("")) {
			msg = msg + "<br>Select End Date!";
		} else {
			if (isValidDateFormatShort(endtime)) {
				end_time = ConvertShortDateToStr(endtime);
				if (!start_time.equals("") && !end_time.equals("") && Long.parseLong(start_time) > Long.parseLong(end_time)) {
					msg = msg + "<br>Start Date should be less than End date!";
				}
			} else {
				msg = msg + "<br>Enter Valid End Date!";
				end_time = "";
			}
		}
	}
	public String ServiceDetails() {
		try {
			int count = 0;
			StringBuilder Str = new StringBuilder();
			// String curryear = ToLongDate(kknow()).substring(0, 4);
			// String duedate = "CONCAT(DATE_FORMAT(DATE_SUB(concat('" + ToLongDate(kknow()).substring(0, 4) + "',substr(veh_sale_date, 5, 4),'000000'),INTERVAL 1 DAY),'%Y%m%d'),'000000')";
			StrSql = " SELECT veh_id, variant_name, vehfollowup_id, vehfollowup_contactable_id, vehfollowup_vehaction_id, vehfollowup_booking_main,"
					+ " COALESCE(crm.emp_id, 0) AS veh_crmemp_id, veh_branch_id, vehfollowup_dueservice,"
					+ " vehfollowup_postponed, vehfollowup_vehlostcase1_id, "
					+ " COALESCE(concat(crm.emp_name,' (', crm.emp_ref_no, ')'), '') AS crmemp_name, "
					+ " veh_id, veh_reg_no, customer_id, customer_name, contact_id, veh_iacs,"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name,"
					+ " contact_mobile1, contact_mobile2, contact_email1, contact_email2,"
					+ " veh_chassis_no,veh_engine_no,veh_modelyear, branch_id, branch_name,"
					+ " COALESCE(veh_kms, 0) AS veh_kms, COALESCE(veh_cal_kms, 0) AS veh_cal_kms,"
					+ " COALESCE(veh_lastservice, '') AS veh_lastservice, COALESCE(veh_lastservice_kms, 0) AS veh_lastservice_kms,"
					+ " COALESCE(veh_calservicedate, '') AS veh_calservicedate,"
					+ " COALESCE(veh_service_duedate, '') AS duedate, veh_sale_date, COALESCE(veh_service_duekms, 0) AS duekms,"
					+ " COALESCE(vehfollowup_followup_time, '') AS vehfollowup_followup_time,"
					+ " COALESCE(bookingtype_name, '') AS bookingtype_name, vehfollowup_appt_time,"
					+ " COALESCE(dr.emp_name, '') AS dr_emp_name,"
					+ " COALESCE(vehfollowup_pickuplocation, '') AS vehfollowup_pickuplocation,"
					+ " COALESCE(vehlostcase1_name, '') AS vehlostcase1_name,"
					+ " COALESCE(vehfollowup_desc, '') AS vehfollowup_desc,"
					+ " COALESCE(notcontactable_name, '') AS notcontactable_name,"// reason
					+ " COALESCE(vehfollowup_kms, 0) AS vehfollowup_kms,"
					+ " COALESCE(vehcalltype_name, '') vehcalltype_name,"

					+ "  COALESCE((SELECT MAX(vehfollowup_appt_time)"
					+ " FROM " + compdb(comp_id) + "axela_service_followup"
					+ " nextfollowup"
					+ " WHERE 1=1"
					+ " AND nextfollowup.vehfollowup_id > main.vehfollowup_id"
					+ " AND nextfollowup.vehfollowup_veh_id = main.vehfollowup_veh_id"
					+ " AND vehfollowup_postponed = 0),'') AS nextappttime,"

					+ " COALESCE((SELECT followup.vehfollowup_id "
					+ " FROM " + compdb(comp_id) + "axela_service_followup followup"
					+ " WHERE followup.vehfollowup_id > main.vehfollowup_id "
					+ " AND followup.vehfollowup_veh_id = main.vehfollowup_veh_id "
					+ " AND followup.vehfollowup_desc LIKE 'Already serviced at%' LIMIT 1),0)"
					+ " AS followupcount"
					+ " FROM " + compdb(comp_id) + "axela_service_followup main"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = vehfollowup_veh_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
					+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = vehfollowup_workshop_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp crm ON crm.emp_id = vehfollowup_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_followup_bookingtype ON bookingtype_id = vehfollowup_bookingtype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp dr ON dr.emp_id = vehfollowup_pickupdriver_emp_id "
					+ " LEFT JOIN axela_notcontactable ON notcontactable_id = vehfollowup_notcontactable_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_followup_lostcase1 ON vehlostcase1_id = vehfollowup_vehlostcase1_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_followup_action ON vehaction_id = vehfollowup_vehaction_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_followup_calltype ON vehcalltype_id = vehfollowup_vehcalltype_id "
					+ " WHERE 1=1"
					+ " AND vehfollowup_vehaction_id= 1";
			if (!start_time.equals("") && pending_followup.equals("0")) {
				StrSql += " AND SUBSTR(vehfollowup_appt_time, 1, 8) >= SUBSTR('" + start_time + "', 1, 8)";
			}
			if (!end_time.equals("")) {
				StrSql += " AND SUBSTR(vehfollowup_appt_time, 1, 8) <= SUBSTR('" + end_time + "', 1, 8) ";
			}
			StrSql += StrSearch + ExeAccess.replace("emp_id", "vehfollowup_emp_id")
					+ " GROUP BY vehfollowup_id"
					+ " ORDER BY vehfollowup_appt_time DESC"
					+ " LIMIT 1000";
			// SOP("StrSql========booking followup==1======" + StrSql);
			CachedRowSet crs1 = processQuery(StrSql, 0);
			if (crs1.isBeforeFirst()) {
				Str.append("<div class=\"table-responsive\">\n");
				Str.append("<table class=\"table table-responsive table-hover table-bordered \" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>#</th>\n");
				Str.append("<th align=center data-toggle=\"true\">Vehicle ID</th>\n");
				Str.append("<th style=\"width:200px;\" align=center>Customer</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Variant</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Reg. No.</th>\n");
				// Str.append("<th data-hide=\"phone, tablet\" align=center>Chassis Number</th>\n");
				// Str.append("<th data-hide=\"phone, tablet\" align=center>Engine No.</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Year</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Sale Date</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Last Service</th>\n");
				// Str.append("<th data-hide=\"phone, tablet\" align=center>Days</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Due Service</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Workshop</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Booking Time</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Booking Type</th>\n");

				Str.append("<th data-hide=\"phone, tablet\" align=center>Driver</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Address</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Feed Back</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>CRM Executive</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Status</th>\n");
				// Str.append("<th data-hide=\"phone, tablet\" align=center>Action</th>\n");
				// Str.append("<th align=center>Follow-up Kms</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs1.next()) {
					count++;
					Str.append("<tr onmouseover='ShowCustomerInfo(" + crs1.getString("veh_id") + ")' onmouseout='HideCustomerInfo(" + crs1.getString("veh_id") + ");'");
					Str.append(" style='height:200px'>\n");
					Str.append("<td align=center>").append(count).append("</td>");
					Str.append("<td align=center><a href=\"javascript:remote=window.open('vehicle-dash.jsp?veh_id=").append(crs1.getString("veh_id"))
							.append("#tabs-4','vehdash','');remote.focus();\">")
							.append(crs1.getString("veh_id")).append("</a></td>\n");
					// Str.append("<td align=center><a href=vehicle-dash.jsp?veh_id=").append(crs1.getString("veh_id")).append(">").append(crs1.getString("veh_id")).append("</a></td>\n");
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
					Str.append(crs1.getString("veh_id")).append("\">").append(SplitRegNo(crs1.getString("veh_reg_no"), 2)).append("</a></td>");
					Str.append("<td align=\"center\" valign=\"top\">").append(crs1.getString("veh_modelyear"));
					Str.append("</td>");
					Str.append("<td align=center>").append(strToShortDate(crs1.getString("veh_sale_date"))).append("</td>");
					Str.append("<td align=center>").append(strToShortDate(crs1.getString("veh_lastservice")));
					Str.append("<br>").append(IndFormat(crs1.getString("veh_lastservice_kms"))).append(" Kms").append("</td>");
					// Str.append("<td align=center>").append(crs1.getString("vehcalltype_name") + "1").append("</td>");
					Str.append("<td align=center>").append(crs1.getString("vehfollowup_dueservice")).append("</td>");
					Str.append("<td align=left><a href=../portal/branch-summary.jsp?branch_id=").append(crs1.getString("branch_id")).append(">")
							.append(crs1.getString("branch_name")).append("</a></td>");

					// Str.append("<td align=center><a href=\"javascript:remote=window.open('vehicle-dash.jsp?veh_id=").append(crs1.getString("veh_id") + "#tabs-4")
					// .append("','vehdash','');remote.focus();\">").append(strToLongDate(crs1.getString("booking_time"))).append("</a></td>\n");
					Str.append("<td valign=top align=center >").append(strToLongDate(crs1.getString("vehfollowup_appt_time"))).append("</td>");
					Str.append("<td align=center>").append(crs1.getString("bookingtype_name"));
					// if (crs1.getString("vehfollowup_vehaction_id").equals("1")) {
					// SOP("coming..1");
					if (crs1.getString("vehfollowup_booking_main").equals("1")) {
						Str.append("</br>(Main)");
					}
					if (crs1.getString("vehfollowup_postponed").equals("1")) {
						Str.append("</br>(Postponed)");
						if (!crs1.getString("nextappttime").equals("")) {
							Str.append("</br>" + strToLongDate(crs1.getString("nextappttime")));
						}
					}

					Str.append("</td>");
					Str.append("<td align=center>").append(crs1.getString("dr_emp_name")).append("</td>");
					Str.append("<td align=center>").append(crs1.getString("vehfollowup_pickuplocation")).append("</td>");
					Str.append("<td align=center>").append(crs1.getString("vehfollowup_desc")).append("</td>");
					Str.append("<td align=left><a href=../portal/executive-summary.jsp?emp_id=").append(crs1.getString("veh_crmemp_id")).append(">")
							.append(crs1.getString("crmemp_name")).append("</a></td>");
					// SOP("2------------" + crs1.getString("vehfollowup_postponed"));
					Str.append("<td align=center>");

					if (crs1.getString("vehfollowup_postponed").equals("0") && crs1.getString("followupcount").equals("0")) {
						Str.append("<a class=\" btn btn-success btn-outline sbold\" href=\"../service/report-vehicle-service-booking-model.jsp?postponemodal=yes&vehfollowup_id="
								+ crs1.getString("vehfollowup_id")
								+ "\" data-target=\"#Hintclicktocall\" data-toggle=\"modal\">Postpone</a>");
						Str.append("<a class=\" btn btn-success btn-outline sbold\" href=\"../service/report-vehicle-service-booking-cancel-model.jsp?branch_id=" + crs1.getString("veh_branch_id")
								+ "&vehfollowup_id="
								+ crs1.getString("vehfollowup_id")
								+ "\" data-target=\"#Hintclicktocall\" data-toggle=\"modal\">Cancel</a>");
					} else if (crs1.getString("vehfollowup_postponed").equals("1")) {
						// if (!crs1.getString("vehfollowup_vehlostcase1_id").equals("0")) {
						// Str.append("</br>Cancelled");
						// }
						// else {
						Str.append("</br>Postponed");
						// }
					}
					else if (!crs1.getString("followupcount").equals("0")) {
						Str.append("Already Serviced");
					}
					Str.append("</td>");
					Str.append("</tr>");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");

			} else {
				Str.append("<font color=red><b>No Service Bookings found!</b></font>");
			}
			crs1.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public void ServiceBookingDetailsExport(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = " SELECT veh_id AS 'Vehicle ID',"
					+ " customer_name AS 'Customer Name',"
					+ " CONCAT(title_desc,' ', contact_fname,' ', contact_lname) AS 'Contact Name',"
					+ " contact_mobile1 AS 'Contact Mobile1',"
					+ " contact_mobile2 AS 'Contact Mobile2',"
					+ " contact_phone1 AS 'Contact Phone1',"
					+ " contact_phone2 AS 'Contact Phone2',"
					+ " contact_email1 AS 'Contact Email1',"
					+ " contact_email2 AS 'Contact Email2',"
					+ " variant_name AS 'Item ',"
					+ " veh_reg_no AS 'Registration No.',"
					+ " veh_modelyear AS 'Model Year',"
					+ " veh_engine_no AS 'Engine No.',"
					+ " veh_chassis_no AS 'Chassis No.',"
					+ " COALESCE(DATE_FORMAT(veh_sale_date, '%d/%m/%Y'),'') AS 'Sale Date',"
					+ " COALESCE(DATE_FORMAT(veh_lastservice, '%d/%m/%Y'),'') AS 'Last Service',"
					+ " COALESCE(veh_lastservice_kms, 0) AS 'Last Service Kms',"
					+ " branch_name AS 'Work Shop',"
					+ " COALESCE(DATE_FORMAT(vehfollowup_appt_time, '%d/%m/%Y %h:%i'),'') AS 'Booking Appt. Time',"
					+ " COALESCE(bookingtype_name, '') AS 'Booking Type',"
					+ " If(vehfollowup_booking_main=1, 'Main', '') AS 'Booking Main',"
					+ " COALESCE(dr.emp_name, '') AS 'Driver Name',"
					+ " COALESCE(vehfollowup_pickuplocation, '') AS 'Address',"
					+ " COALESCE(vehfollowup_desc, '') AS 'Feed Back',"
					+ " COALESCE(crm.emp_name, '') AS 'CRM Executive',"
					+ " COALESCE(DATE_FORMAT(vehfollowup_modified_time, '%d/%m/%Y %h:%i'),'') AS 'Booking Entry Time'"
					+ " FROM " + compdb(comp_id) + "axela_service_followup main"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = vehfollowup_veh_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
					+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = vehfollowup_workshop_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp crm ON crm.emp_id = vehfollowup_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_followup_bookingtype ON bookingtype_id = vehfollowup_bookingtype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp dr ON dr.emp_id = vehfollowup_pickupdriver_emp_id "
					+ " LEFT JOIN axela_notcontactable ON notcontactable_id = vehfollowup_notcontactable_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_followup_lostcase1 ON vehlostcase1_id = vehfollowup_vehlostcase1_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_followup_action ON vehaction_id = vehfollowup_vehaction_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_followup_calltype ON vehcalltype_id = vehfollowup_vehcalltype_id "
					+ " WHERE 1=1"
					+ " AND vehfollowup_vehaction_id= 1";
			if (!start_time.equals("")) {
				StrSql += " AND SUBSTR(vehfollowup_appt_time, 1, 8) >= SUBSTR('" + start_time + "', 1, 8)";
			}
			if (!end_time.equals("")) {
				StrSql += " AND SUBSTR(vehfollowup_appt_time, 1, 8) <= SUBSTR('" + end_time + "', 1, 8) ";
			}
			StrSql += StrSearch
					+ " GROUP BY vehfollowup_id"
					+ " ORDER BY vehfollowup_appt_time DESC"
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

	public void ServiceBookingPickupDropPrint(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = " SELECT "
					+ " COALESCE(DATE_FORMAT(vehfollowup_appt_time, '%d/%m/%Y'),'') AS 'Date',"
					+ " COALESCE(branch_name,'') AS 'Service Centre Name',"
					+ " customer_name AS 'Customer Name',"
					+ " contact_mobile1 AS 'Mobile No',"
					+ " contact_mobile2 AS 'Alternative Mobile No',"
					+ " COALESCE(veh_reg_no,'') AS 'Car No.',"
					+ " COALESCE(preownedmodel_name, '') AS 'Model',"
					+ " COALESCE(vehfollowup_dueservice, '') AS  'Service',"
					+ " COALESCE(vehfollowup_pickuplocation, '') AS 'Address',"
					+ " COALESCE(DATE_FORMAT(vehfollowup_appt_time, '%H:%i:%s'),'') AS 'Pick up Time',"
					+ " COALESCE(crm.emp_name, '') AS 'CRM Executive',"
					+ " '' AS 'Discount Offer'"
					+ " FROM " + compdb(comp_id) + "axela_service_followup"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = vehfollowup_veh_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
					+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = vehfollowup_workshop_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp crm ON crm.emp_id = vehfollowup_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_followup_bookingtype ON bookingtype_id = vehfollowup_bookingtype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp dr ON dr.emp_id = vehfollowup_pickupdriver_emp_id "
					+ " LEFT JOIN axela_notcontactable ON notcontactable_id = vehfollowup_notcontactable_id"
					+ " WHERE 1=1"
					+ " AND vehfollowup_vehaction_id= 1";
			if (!start_time.equals("")) {
				StrSql += " AND SUBSTR(vehfollowup_appt_time, 1, 8) >= SUBSTR('" + start_time + "', 1, 8)";
			}
			if (!end_time.equals("")) {
				StrSql += " AND SUBSTR(vehfollowup_appt_time, 1, 8) <= SUBSTR('" + end_time + "', 1, 8) ";
			}
			StrSql += StrSearch
					+ " GROUP BY vehfollowup_id"
					+ " ORDER BY vehfollowup_id DESC"
					+ " LIMIT 10000"; // + exportcount;

			// SOP("StrSql====service pickup Export========" + StrSqlBreaker(StrSql));
			CachedRowSet crs2 = processQuery(StrSql, 0);
			if (printoption.equals("html")) {
				ExportToHTML exportToHTML = new ExportToHTML();
				exportToHTML.Export(request, response, crs2, "html", comp_id);
			} else {
				msg = "No Records To Print!";
			}
			crs2.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
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

	public String PopulateServiceBookingType(String comp_id, String vehfollowup_bookingtype_id) {
		StringBuilder Str = new StringBuilder();
		try {

			StrSql = "SELECT bookingtype_id, bookingtype_name"
					+ " FROM " + compdb(comp_id) + "axela_service_followup_bookingtype"
					+ " WHERE 1 = 1"
					+ " GROUP BY bookingtype_id"
					+ " ORDER BY bookingtype_id ";
			CachedRowSet crs = processQuery(StrSql, 0);

			// Str.append("<select name=\"dr_item_id\" id=\"dr_item_id\" class=\"selectbox\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("bookingtype_id"));
				Str.append(StrSelectdrop(crs.getString("bookingtype_id"), vehfollowup_bookingtype_id));
				Str.append(">").append(crs.getString("bookingtype_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateCRMExecutives(String crmemp_id, String comp_id, String ExeAccess) {
		StringBuilder Str = new StringBuilder();
		try {

			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = 1"
					+ " AND emp_crm = 1" + ExeAccess + ""
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=\"dr_emp_id\" id=\"dr_emp_id\" class=\"form-control\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop(crs.getInt("emp_id"), crmemp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			Str.append("</select>\n");
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
					+ " AND branch_branchtype_id IN (3)"
					+ " GROUP BY branch_id"
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			// Str.append("<select name=\"dr_branch_id\" id=\"dr_branch_id\" class=\"selectbox\" onChange=\"ExeCheck();\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("branch_id"));
				Str.append(Selectdrop(crs.getInt("branch_id"), branch_id));
				Str.append(">").append(crs.getString("branch_name")).append("</option>\n");
			}
			Str.append("</select>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

}
