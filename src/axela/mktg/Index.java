package axela.mktg;
//Murali 21st jun
//Saiman 10th dec 2012
/*Smitha Nag 2,3 april 2013 */

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Index extends Connect {

	public String StrSql = "";
	public String msg = "";
	public String emp_id = "", branch_id = "";
	public String dr_branch_id = "";
	public String StrSearch = "";
	public String comp_id = "0";
	public String smart = "";
	public String ExeAccess = "";
	public String BranchAccess = "";
	public String NoSalesPipeline = "";
	public String ListCampaign = "";
	public String startdate = new SimpleDateFormat("01/MM/yyyy").format(kknow());
	public String enddate = "";
	public String exe_id = "";
	public String TargetSearch = "";
	public String DateSearch = "";
	public String ExeSearch = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			emp_id = (session.getAttribute("emp_id")).toString();
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_lead_access, emp_opportunity_access, emp_quote_access, emp_sales_order_access", request, response);
			branch_id = (session.getAttribute("emp_branch_id")).toString();
			BranchAccess = CheckNull(session.getAttribute("BranchAccess"));
			ExeAccess = CheckNull(session.getAttribute("ExeAccess"));
			smart = PadQuotes(request.getParameter("smart"));
			exe_id = CNumeric(PadQuotes(request.getParameter("dr_executive")));

			enddate = AddMonth(startdate, 1);
			enddate = ConvertShortDateToStr(enddate);
			if (!PadQuotes(request.getParameter("dr_month")).equals("")) {
				startdate = PadQuotes(request.getParameter("dr_month"));
				enddate = AddMonth(startdate, 1);
				enddate = ConvertShortDateToStr(enddate);
				// SOP("Start=="+startdate);
				// SOP("emd==="+enddate);
			}

			TargetSearch = ExeAccess.replace("emp_id", "target_emp_id") + "";
			TargetSearch = TargetSearch + " and target_startdate>= '" + ConvertShortDateToStr(startdate) + "' and target_startdate< '" + enddate + "'";
			DateSearch = " and startdate>= '" + ConvertShortDateToStr(startdate) + "' and startdate< '" + enddate + "'";

			if (!exe_id.equals("0")) {
				TargetSearch = TargetSearch + " and target_emp_id in (" + exe_id + ")";
				ExeSearch = " and emp_id in (" + exe_id + ")";
			}

			if (msg.equals("")) {
				if (smart.equals("yes")) {
				} else {
					session.setAttribute("ItemIssuedstrsql", StrSearch);
				}
				ListCampaign = CampaignSummary(request);
				SalesPipeline();
			}
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String CampaignSummary(HttpServletRequest request) {
		int opprcount = 0;
		StringBuilder Str = new StringBuilder();
		int count = 0;

		try {
			StrSql = " SELECT campaign_id, campaign_name, campaign_startdate, campaign_enddate, "
					+ " coalesce((select count(oppr_id) from axela_sales_oppr where  oppr_campaign_id = campaign_id"
					+ " ),0)  as enqcount"
					+ " from axela_sales_campaign"
					+ " where campaign_active='1' "
					+ " and campaign_startdate<" + ToLongDate(kknow()) + " and campaign_enddate>" + ToLongDate(kknow());
			StrSql = StrSql + " group by campaign_id order by campaign_name";
			// SOP("strsql in LmsSummary---" + StrSql);
			CachedRowSet crs = processQuery(StrSql);
			Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
			Str.append("<tr align=center>\n");
			Str.append("<th>#</th>\n");
			Str.append("<th>Campaign</th>\n");
			Str.append("<th>Date</th>\n");
			Str.append("<th>Enquiry</th>\n");
			Str.append("</tr>\n");
			while (crs.next()) {
				count = count + 1;
				opprcount = opprcount + crs.getInt("enqcount");
				Str.append("<tr>\n");
				Str.append("<td valign=top align=center><b>").append(count).append(".</b></td>\n");
				Str.append("<td valign=top align=left><a href=campaign-list.jsp?campaign_id=").append(crs.getString("campaign_id")).append(">").append(crs.getString("campaign_name"))
						.append("</a></td>\n");
				Str.append("<td valign=top align=center>").append(strToShortDate(crs.getString("campaign_startdate"))).append(" - ").append(strToShortDate(crs.getString("campaign_enddate")))
						.append("</td>");
				Str.append("<td valign=top align=right>").append(crs.getString("enqcount")).append("</td>");
				Str.append("</tr>");
			}
			crs.close();
			Str.append("<tr>\n");
			Str.append("<td align=right>&nbsp;</td>\n");
			Str.append("<td align=right>&nbsp;</td>\n");
			Str.append("<td align=right><b>Total:</b></td>\n");
			Str.append("<td align=right><b>").append(opprcount).append("</b></td>\n");
			Str.append("</tr></table>");
			return Str.toString();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String FollowupEscStatus() {
		try {
			int totalopprcount = 0;
			StringBuilder Str = new StringBuilder();
			StrSql = " Select gr.group_id as group_id, count(oppr_id) as triggercount "
					+ " from ( "
					+ " select 1 as group_id "
					+ " UNION "
					+ " select 2 as group_id "
					+ " UNION "
					+ " select 3 as group_id "
					+ " UNION "
					+ " select 4 as group_id "
					+ " UNION "
					+ " select 5 as group_id "
					+ " ) as gr "
					+ " left join axela_sales_oppr_followup on followup_trigger=gr.group_id "
					+ " and followup_desc = '' "
					+ " left join axela_sales_oppr on oppr_id = followup_oppr_id "
					+ " and followup_trigger > 0 "
					+ " and oppr_status_id=1 "
					+ BranchAccess.replace("branch_id", "oppr_branch_id") + ExeAccess.replace("emp_id", "oppr_emp_id")
					+ " where 1=1 "
					+ " group by group_id "
					+ " order by group_id";
			// SOP("StrSql==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql);
			while (crs.next()) {
				totalopprcount = totalopprcount + crs.getInt("triggercount");
				Str.append("<tr align=center>\n");
				Str.append("<td align=right> Level ").append(crs.getString("group_id")).append(":</td>\n");
				Str.append("<td align=right> ").append(crs.getString("triggercount")).append("</td>\n");
				Str.append("</tr>");
			}
			crs.close();
			Str.append("<tr align=center>\n");
			Str.append("<td align=right><b><a href=\"report-followup-esc-status.jsp \" target=_blank>Total</a>:</b></td>\n");
			Str.append("<td align=right><b><a href=\"report-followup-esc-status.jsp \" target=_blank>").append(totalopprcount).append("</a></b></td>\n");
			Str.append("</tr>");
			return Str.toString();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String OpprEscStatus() {
		try {
			int totalopprcount = 0;
			StringBuilder Str = new StringBuilder();
			StrSql = " Select gr.group_id as group_id, count(oppr_id) as triggercount "
					+ " from ( "
					+ " select 1 as group_id "
					+ " UNION "
					+ " select 2 as group_id "
					+ " UNION "
					+ " select 3 as group_id "
					+ " UNION "
					+ " select 4 as group_id "
					+ " UNION "
					+ " select 5 as group_id "
					+ " ) as gr "
					+ " left join axela_sales_oppr on oppr_trigger=gr.group_id "
					+ " and oppr_status_id=1  and oppr_trigger > 0 "
					+ BranchAccess.replace("branch_id", "oppr_branch_id") + ExeAccess.replace("emp_id", "oppr_emp_id")
					+ " where 1=1 "
					+ " group by group_id "
					+ " order by group_id";
			// SOP("StrSql==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql);
			while (crs.next()) {
				totalopprcount = totalopprcount + crs.getInt("triggercount");
				Str.append("<tr align=center>\n");
				Str.append("<td align=right> Level ").append(crs.getString("group_id")).append(":</td>\n");
				Str.append("<td align=right> ").append(crs.getString("triggercount")).append("</td>\n");
				Str.append("</tr>");
			}
			crs.close();
			Str.append("<tr align=center>\n");
			Str.append("<td align=right><b><a href=\"report-oppr-esc-status.jsp \" target=_blank>Total</a>: </b></td>\n");
			Str.append("<td align=right><b><a href=\"report-oppr-esc-status.jsp \" target=_blank>").append(totalopprcount).append("</a></b></td>\n");
			Str.append("</tr>");
			return Str.toString();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String CRMFollowupEscStatus() {
		try {
			int totalopprcount = 0;
			StringBuilder Str = new StringBuilder();
			StrSql = " Select gr.group_id as group_id, count(oppr_id) as triggercount "
					+ " from ( "
					+ " select 1 as group_id "
					+ " UNION "
					+ " select 2 as group_id "
					+ " UNION "
					+ " select 3 as group_id "
					+ " UNION "
					+ " select 4 as group_id "
					+ " UNION "
					+ " select 5 as group_id "
					+ " ) as gr "
					+ " left join axela_sales_oppr_crmfollowup on crmfollowup_trigger=gr.group_id "
					+ " and crmfollowup_desc = '' "
					+ " left join axela_sales_oppr on oppr_id = crmfollowup_oppr_id "
					+ " and crmfollowup_trigger > 0 "
					+ " and oppr_status_id=1 "
					+ BranchAccess.replace("branch_id", "oppr_branch_id") + ExeAccess.replace("emp_id", "oppr_emp_id")
					+ " where 1=1 "
					+ " group by group_id "
					+ " order by group_id";
			// SOP("StrSql==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql);
			while (crs.next()) {
				totalopprcount = totalopprcount + crs.getInt("triggercount");
				Str.append("<tr align=center>\n");
				Str.append("<td align=right> Level ").append(crs.getString("group_id")).append(":</td>\n");
				Str.append("<td align=right> ").append(crs.getString("triggercount")).append("</td>\n");
				Str.append("</tr>");
			}
			crs.close();
			Str.append("<tr align=center>\n");
			Str.append("<td align=right><b><a href=\"report-crmfollowup-esc-status.jsp \" target=_blank>Total</a>:</b></td>\n");
			Str.append("<td align=right><b><a href=\"report-crmfollowup-esc-status.jsp \" target=_blank>").append(totalopprcount).append("</a></b></td>\n");
			Str.append("</tr>");
			return Str.toString();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String SalesPipeline() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "select stage_name, count(oppr_id) as stage_count"
					+ " from axela_sales_oppr "
					+ " INNER JOIN axela_sales_oppr_stage on stage_id =oppr_stage_id "
					+ " where 1=1 and oppr_status_id =1 "
					+ BranchAccess.replace("branch_id", "oppr_branch_id") + "" + ExeAccess.replace("emp_id", "oppr_emp_id")
					+ " group by stage_id "
					+ " order by stage_count desc";
			// SOP("strsql in LmsSummarySalespipe-- -" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql);
			if (crs.isBeforeFirst()) {
				Str.append("[");
				while (crs.next()) {
					Str.append("{'title': '" + crs.getString("stage_name") + "', 'total':" + crs.getString("stage_count") + "},");
					// Str.append("['").append(crs.getString("stage_name")).append(" (").append(crs.getString("stage_count")).append(")").append("',").append(crs.getString("stage_count")).append("], ");
				}
				Str.append("]");
			} else {
				NoSalesPipeline = "No open opportunities!";
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateSalesExecutives() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') as emp_name"
					+ " FROM axela_emp"
					+ " INNER JOIN axela_sales_team_exe on teamtrans_emp_id=emp_id"
					+ " WHERE emp_active = '1'  and emp_sales='1' " + ExeAccess + "";
			StrSql = StrSql + " group by emp_id order by emp_name";
			CachedRowSet crs = processQuery(StrSql);
			Str.append("<select name=dr_executive id=dr_executive class=selectbox onchange=\"EmpCheck();\" >");
			Str.append("<option value=0 >All Executives</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateMonth() {
		String year = "<select name=dr_month  id=dr_month class=selectbox onchange=\"EmpCheck();\">";
		year = year + "";
		String currdate = new SimpleDateFormat("01/MM/yyyy").format(kknow());
		currdate = AddMonth(currdate, -6);
		for (int i = 0; i <= 12; i++) {
			String str = AddMonth(currdate, i);
			year = year + "<option value = " + str + "" + StrSelectdrop(str, startdate) + ">" + StrShorttoYearMonth(str) + "</option>\n";
		}
		year = year + "</select>";
		return year;
	}

	public String ListTarget() {
		StringBuilder Str = new StringBuilder();
		int oppr_count_t = 0, oppr_count_a = 0, oppr_count_perc = 0;
		int oppr_calls_t = 0, oppr_calls_a = 0, oppr_calls_perc = 0;
		int oppr_meetings_t = 0, oppr_meetings_a = 0, oppr_meetings_perc = 0;
		int oppr_demos_t = 0, oppr_demos_a = 0, oppr_demos_perc = 0;
		int oppr_hot_t = 0, oppr_hot_a = 0, oppr_hot_perc = 0;
		int oppr_so_t = 0, oppr_so_a = 0, oppr_so_perc = 0;
		int oppr_so_amt_t = 0, oppr_so_amt_a = 0, oppr_so_amt_perc = 0;

		try {
			StrSql = "SELECT model_id, model_name,  "
					+ " @oppr_count_t:=coalesce((select sum(modeltarget_oppr_count) "
					+ " from axela_sales_target_model "
					+ " inner join axela_sales_target on target_id=modeltarget_target_id "
					+ " where modeltarget_model_id=model_id "
					+ " " + TargetSearch + " ),0) as oppr_count_t ,  "
					+ " @oppr_count_a:=(select coalesce(count(oppr_id),0)  "
					+ " from axela_sales_oppr "
					+ " where oppr_model_id = model_id "
					+ DateSearch.replace("startdate", "oppr_date")
					+ ExeSearch.replace("emp_id", "oppr_emp_id") + ") as oppr_count_a, "
					+ " coalesce((100*@oppr_count_a/@oppr_count_t),0) as oppr_count_perc , "
					+ " @oppr_calls_t:=coalesce((select sum(modeltarget_oppr_calls_count) "
					+ " from axela_sales_target_model "
					+ " inner join axela_sales_target on target_id=modeltarget_target_id "
					+ " where modeltarget_model_id=model_id "
					+ " " + TargetSearch + " ),0) as oppr_calls_t,  "
					+ " @oppr_calls_a:=(select coalesce(count(followup_id),0)  "
					+ " from axela_sales_oppr "
					+ " inner join axela_sales_oppr_followup on followup_oppr_id=oppr_id "
					+ " where oppr_model_id = model_id and followup_followuptype_id=1"
					+ DateSearch.replace("startdate", "followup_followup_time")
					+ ExeSearch.replace("emp_id", "oppr_emp_id") + ") as oppr_calls_a, "
					+ " coalesce((100*@oppr_calls_a/@oppr_calls_t),0) as oppr_calls_perc , "
					+ " @oppr_meetings_t:=coalesce((select sum(modeltarget_oppr_meetings_count) "
					+ " from axela_sales_target_model "
					+ " inner join axela_sales_target on target_id=modeltarget_target_id "
					+ " where modeltarget_model_id=model_id "
					+ " " + TargetSearch + " ),0) as oppr_meetings_t ,  "
					+ " @oppr_meetings_a:=(select coalesce(count(oppr_id),0)  "
					+ " from axela_sales_oppr "
					+ " inner join axela_sales_oppr_followup on followup_oppr_id=oppr_id "
					+ " where oppr_model_id = model_id and followup_followuptype_id=2"
					+ DateSearch.replace("startdate", "followup_followup_time")
					+ ExeSearch.replace("emp_id", "oppr_emp_id") + ") as oppr_meetings_a, "
					+ " coalesce((100*@oppr_meetings_a/@oppr_meetings_t),0) as oppr_meetings_perc , "
					+ " @oppr_demos_t:=coalesce((select sum(modeltarget_oppr_demos_count) "
					+ " from axela_sales_target_model "
					+ " inner join axela_sales_target on target_id=modeltarget_target_id "
					+ " where modeltarget_model_id=model_id "
					+ " " + TargetSearch + " ),0) as oppr_demos_t, "
					+ " @oppr_demos_a:=(select coalesce(count(demo_id),0)  "
					+ " from axela_sales_oppr "
					+ " inner join axela_sales_demo on demo_oppr_id=oppr_id "
					+ " where oppr_model_id = model_id and demo_fb_taken=1"
					+ DateSearch.replace("startdate", "demo_time")
					+ ExeSearch.replace("emp_id", "oppr_emp_id") + ") as oppr_demos_a, "
					+ " coalesce((100*@oppr_demos_a/@oppr_demos_t),0) as oppr_demos_perc , "
					+ " @oppr_hot_t:=coalesce((select sum(modeltarget_oppr_hot_count) "
					+ " from axela_sales_target_model "
					+ " inner join axela_sales_target on target_id=modeltarget_target_id "
					+ " where modeltarget_model_id=model_id "
					+ " " + TargetSearch + " ),0) as oppr_hot_t, "
					+ " @oppr_hot_a:=(select coalesce(count(oppr_id),0) "
					+ " from axela_sales_oppr "
					+ " where oppr_model_id = model_id and oppr_priorityoppr_id=1"
					+ DateSearch.replace("startdate", "oppr_date")
					+ ExeSearch.replace("emp_id", "oppr_emp_id") + ") as oppr_hot_a, "
					+ " coalesce((100*@oppr_hot_a/@oppr_hot_t),0) as oppr_hot_perc , "
					+ " @oppr_so_t:=coalesce((select sum(modeltarget_so_count) "
					+ " from axela_sales_target_model "
					+ " inner join axela_sales_target on target_id=modeltarget_target_id "
					+ " where modeltarget_model_id=model_id "
					+ " " + TargetSearch + " ),0) as oppr_so_t ,  "
					+ " @oppr_so_a:=(select coalesce(count(so_id),0) "
					+ " from axela_sales_oppr "
					+ " inner join axela_sales_so on so_oppr_id = oppr_id  "
					+ " where oppr_model_id = model_id and so_active='1'"
					+ DateSearch.replace("startdate", "so_date")
					+ ExeSearch.replace("emp_id", "oppr_emp_id") + ") as oppr_so_a, "
					+ " coalesce((100*@oppr_so_a/@oppr_so_t),0) as oppr_so_perc , "
					+ " @oppr_so_amt_t:=coalesce((select sum(modeltarget_so_amount) "
					+ " from axela_sales_target_model "
					+ " inner join axela_sales_target on target_id=modeltarget_target_id "
					+ " where modeltarget_model_id=model_id "
					+ " " + TargetSearch + " ),0) as oppr_so_amt_t, "
					+ " @oppr_so_amt_a:=(select coalesce(sum(so_grandtotal),0) "
					+ " from axela_sales_oppr "
					+ " inner join axela_sales_so on so_oppr_id = oppr_id  "
					+ " where oppr_model_id = model_id and so_active='1'"
					+ DateSearch.replace("startdate", "so_date")
					+ ExeSearch.replace("emp_id", "oppr_emp_id") + ") as oppr_so_amt_a,  "
					+ " coalesce((100*@oppr_so_amt_a/@oppr_so_amt_t),0) as oppr_so_amt_perc "
					+ " FROM axela_inventory_item_model  "
					+ " where 1=1 "
					+ " group by model_name"
					+ " order by model_name";
			// SOP("query list target..." + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql);
			if (crs.isBeforeFirst()) {
				Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				Str.append("<tr align=center>\n");
				Str.append("<th>Model</th>\n");
				Str.append("<th colspan=3>Oppr Count</th>\n");
				Str.append("<th colspan=3>Oppr Calls</th>\n");
				Str.append("<th colspan=3>Oppr Meeting</th>\n");
				Str.append("<th colspan=3>Oppr Demos</th>\n");
				Str.append("<th colspan=3>Oppr Hot</th>\n");
				Str.append("<th colspan=3>SO Count</th>\n");
				Str.append("<th colspan=3>SO Amount</th>\n");
				Str.append("</tr>\n");
				Str.append("<tr>\n");
				Str.append("<th>&nbsp;</th>");
				Str.append("<th>T</th>\n");
				Str.append("<th>A</th>\n");
				Str.append("<th>%</th>\n");
				Str.append("<th>T</th>\n");
				Str.append("<th>A</th>\n");
				Str.append("<th>%</th>\n");
				Str.append("<th>T</th>\n");
				Str.append("<th>A</th>\n");
				Str.append("<th>%</th>\n");
				Str.append("<th>T</th>\n");
				Str.append("<th>A</th>\n");
				Str.append("<th>%</th>\n");
				Str.append("<th>T</th>\n");
				Str.append("<th>A</th>\n");
				Str.append("<th>%</th>\n");
				Str.append("<th>T</th>\n");
				Str.append("<th>A</th>\n");
				Str.append("<th>%</th>\n");
				Str.append("<th>T</th>\n");
				Str.append("<th>A</th>\n");
				Str.append("<th>%</th>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					oppr_count_t = oppr_count_t + crs.getInt("oppr_count_t");
					oppr_count_a = oppr_count_a + crs.getInt("oppr_count_a");
					oppr_count_perc = oppr_count_perc + (int) crs.getDouble("oppr_count_perc");
					oppr_calls_t = oppr_calls_t + crs.getInt("oppr_calls_t");
					oppr_calls_a = oppr_calls_a + crs.getInt("oppr_calls_a");
					oppr_calls_perc = oppr_calls_perc + (int) crs.getDouble("oppr_calls_perc");
					oppr_meetings_t = oppr_meetings_t + crs.getInt("oppr_meetings_t");
					oppr_meetings_a = oppr_meetings_a + crs.getInt("oppr_meetings_a");
					oppr_meetings_perc = oppr_meetings_perc + (int) crs.getDouble("oppr_meetings_perc");
					oppr_demos_t = oppr_demos_t + crs.getInt("oppr_demos_t");
					oppr_demos_a = oppr_demos_a + crs.getInt("oppr_demos_a");
					oppr_demos_perc = oppr_demos_perc + (int) crs.getDouble("oppr_demos_perc");
					oppr_hot_t = oppr_hot_t + crs.getInt("oppr_hot_t");
					oppr_hot_a = oppr_hot_a + crs.getInt("oppr_hot_a");
					oppr_hot_perc = oppr_hot_perc + (int) crs.getDouble("oppr_hot_perc");
					oppr_so_t = oppr_so_t + crs.getInt("oppr_so_t");
					oppr_so_a = oppr_so_a + crs.getInt("oppr_so_a");
					oppr_so_perc = oppr_so_perc + (int) crs.getDouble("oppr_so_perc");
					oppr_so_amt_t = oppr_so_amt_t + (int) crs.getDouble("oppr_so_amt_t");
					oppr_so_amt_a = oppr_so_amt_a + (int) crs.getDouble("oppr_so_amt_a");
					oppr_so_amt_perc = oppr_so_amt_perc + (int) crs.getDouble("oppr_so_amt_perc");

					Str.append("<tr>\n");
					Str.append("<td valign=top align=left>").append(crs.getString("model_name")).append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getInt("oppr_count_t")).append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getInt("oppr_count_a")).append("</td>");
					Str.append("<td valign=top align=right>").append((int) crs.getDouble("oppr_count_perc")).append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getInt("oppr_calls_t")).append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getInt("oppr_calls_a")).append("</td>");
					Str.append("<td valign=top align=right>").append((int) crs.getDouble("oppr_calls_perc")).append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getInt("oppr_meetings_t")).append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getInt("oppr_meetings_a")).append("</td>");
					Str.append("<td valign=top align=right>").append((int) crs.getDouble("oppr_meetings_perc")).append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getInt("oppr_demos_t")).append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getInt("oppr_demos_a")).append("</td>");
					Str.append("<td valign=top align=right>").append((int) crs.getDouble("oppr_demos_perc")).append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getInt("oppr_hot_t")).append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getInt("oppr_hot_a")).append("</td>");
					Str.append("<td valign=top align=right>").append((int) crs.getDouble("oppr_hot_perc")).append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getInt("oppr_so_t")).append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getInt("oppr_so_a")).append("</td>");
					Str.append("<td valign=top align=right>").append((int) crs.getDouble("oppr_so_perc")).append("</td>");
					Str.append("<td valign=top align=right>").append((int) crs.getDouble("oppr_so_amt_t")).append("</td>");
					Str.append("<td valign=top align=right>").append((int) crs.getDouble("oppr_so_amt_a")).append("</td>");
					Str.append("<td valign=top align=right>").append((int) crs.getDouble("oppr_so_amt_perc")).append("</td>");
					Str.append("</tr>\n");
				}
				Str.append("<tr>\n");
				Str.append("<td valign=top align=right><b>Total:</b></td>");
				Str.append("<td valign=top align=right><b>").append(oppr_count_t).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(oppr_count_a).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(getPercentage(oppr_count_a, oppr_count_t)).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(oppr_calls_t).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(oppr_calls_a).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(getPercentage(oppr_calls_a, oppr_calls_t)).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(oppr_meetings_t).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(oppr_meetings_a).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(getPercentage(oppr_meetings_a, oppr_meetings_t)).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(oppr_demos_t).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(oppr_demos_a).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(getPercentage(oppr_demos_a, oppr_demos_t)).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(oppr_hot_t).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(oppr_hot_a).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(getPercentage(oppr_hot_a, oppr_hot_t)).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(oppr_so_t).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(oppr_so_a).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(getPercentage(oppr_so_a, oppr_so_t)).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(oppr_so_amt_t).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(oppr_so_amt_a).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(getPercentage(oppr_so_amt_a, oppr_so_amt_t)).append("</b></td>");
				Str.append("</tr>\n");
				Str.append("</table>\n");
			} else {
				Str.append("<br><br><br><br><font color=red>No Target(s) found!</font><br><br>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
