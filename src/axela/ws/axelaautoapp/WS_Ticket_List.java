package axela.ws.axelaautoapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;
import javax.ws.rs.core.Context;

import org.codehaus.jettison.json.JSONObject;

import axela.portal.Header;
import cloudify.connect.Connect;

import com.google.gson.Gson;

public class WS_Ticket_List extends Connect {
	public String StrSql = "";
	public String SqlJoin = "";
	public String StrSearch = "";
	public String pagecurrent = "";
	public String emp_uuid = "0";
	public String emp_id = "";
	public String comp_id = "0";
	public String branch_id = "0";
	public String ticket_id = "0";
	public String previous_ticket_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String emp_all_exe = "";
	public String emp_branch_id = "0";
	public CachedRowSet crs = null;
	public String executive_id = "0";
	Gson gson = new Gson();
	JSONObject obj = new JSONObject();
	JSONObject output = new JSONObject();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();
	private long closetime;

	public JSONObject TicketList(JSONObject input, @Context HttpServletRequest request) throws Exception {
		if (AppRun().equals("0")) {
			SOP("input==WS_Ticket_List===" + input);
		}
		HttpSession session = request.getSession(true);
		emp_id = CNumeric(session.getAttribute("emp_id") + "");
		if (!input.isNull("comp_id")) {
			comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
		}
		if (!input.isNull("pagecurrent")) {
			pagecurrent = CNumeric(PadQuotes((String) input.get("pagecurrent")));
		}
		if (!input.isNull("emp_uuid")) {
			emp_uuid = PadQuotes((String) input.get("emp_uuid"));
		}
		if (!CNumeric(GetSession("emp_id", request) + "").equals("0") && !emp_uuid.equals("")) {
			if (ExecuteQuery("SELECT emp_id FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_id=" + CNumeric(GetSession("emp_id", request) + "") + ""
					+ " AND emp_uuid='" + emp_uuid + "' ").equals(""))
			{
				session.setAttribute("emp_id", "0");
				session.setAttribute("sessionMap", null);
			}
		}
		CheckAppSession(emp_uuid, comp_id, request);
		if (ReturnPerm(comp_id, "emp_ticket_access", request).equals("0")) {
			output.put("errorpage", "Access Denied!");
			return output;
		}
		emp_id = CNumeric(session.getAttribute("emp_id") + "");
		BranchAccess = GetSession("BranchAccess", request);
		ExeAccess = GetSession("ExeAccess", request);
		emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
		emp_branch_id = GetSession("emp_branch_id", request);
		if (pagecurrent.equals("1")) {
			new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
		}
		if (!emp_id.equals("0")) {
			try {
				if (!input.isNull("ticket_id")) {
					ticket_id = CNumeric((String) input.get("ticket_id"));
				}
				if (!input.isNull("previous_ticket_id")) {
					previous_ticket_id = CNumeric((String) input.get("previous_ticket_id"));
				}
				if (!ticket_id.equals("0")) {
					StrSearch += "AND ticket_id = " + ticket_id;
				}
				if (!previous_ticket_id.equals("0")) {
					StrSearch = StrSearch + " AND IF((SELECT ticket_customer_id FROM " + compdb(comp_id) + "axela_service_ticket WHERE ticket_id = " + previous_ticket_id + ")!=0, "
							+ " ticket_customer_id IN (SELECT ticket_customer_id FROM " + compdb(comp_id) + "axela_service_ticket WHERE ticket_id = " + previous_ticket_id + "), "
							+ " ticket_emp_id IN (SELECT ticket_emp_id FROM " + compdb(comp_id) + "axela_service_ticket WHERE ticket_id = " + previous_ticket_id + "))";
				}
				StrSql = "SELECT ticket_id, "
						+ " COALESCE(customer_id, 0) AS customer_id, coalesce(customer_name, '') AS customer_name,"
						+ " ticketstatus_name,"
						+ " COALESCE (branch_name,'') AS ticketbranch_name,"
						+ " COALESCE(branch_brand_id,'') AS branch_brand_id,"
						+ " COALESCE(CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname), '') AS contact_name,"
						+ " COALESCE(contact_mobile1,'') AS contact_mobile1,"
						+ " COALESCE(contact_mobile2,'') AS contact_mobile2,"
						+ " COALESCE(contact_email1, '') AS contact_email1,"
						+ " COALESCE(contact_email2, '') AS contact_email2, "
						+ " CONCAT(emp_name, ' (',emp_ref_no, ')') AS emp_name,"
						+ " ticket_report_time, ticket_due_time, ticket_closed_time";
				SqlJoin = " FROM " + compdb(comp_id) + "axela_service_ticket"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = ticket_emp_id";
				if ((emp_all_exe.equals("1") && !emp_id.equals("1"))) {
					if (!emp_branch_id.equals("0")) {
						SqlJoin += " AND emp_branch_id = " + emp_branch_id;
					}
				}
				SqlJoin += " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_source ON ticketsource_id = ticket_ticketsource_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_status ON ticketstatus_id = ticket_ticketstatus_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_dept ON ticket_dept_id = ticket_ticket_dept_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_priority ON priorityticket_id = ticket_priorityticket_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = ticket_contact_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = ticket_customer_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = ticket_branch_id "
						+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_cat ON ticketcat_id = ticket_ticketcat_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_type ON tickettype_id = ticket_tickettype_id";
				SqlJoin += " WHERE 1 = 1 "
						+ BranchAccess.replace("branch_id", "ticket_branch_id")
						+ ExeAccess.replace("emp_id", "enquiry_emp_id");
				if (!StrSearch.equals("")) {
					SqlJoin += StrSearch;
				}
				StrSql += SqlJoin;
				StrSql += " GROUP BY ticket_id"
						+ " ORDER BY ticket_id DESC "
						+ LimitRecords(0, pagecurrent);
				SOP("StrSql========================" + StrSql);
				crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						map.put("ticket_id", crs.getString("ticket_id"));
						map.put("ticketstatus_name", crs.getString("ticketstatus_name"));
						map.put("ticket_report_time", strToLongDate(crs.getString("ticket_report_time")));
						map.put("ticket_due_time", strToLongDate(crs.getString("ticket_due_time")));
						if (!crs.getString("ticket_closed_time").equals("")) {
							closetime = crs.getLong("ticket_closed_time");
						} else {
							closetime = Long.parseLong(ToLongDate(kknow()));
						}
						if (!crs.getString("ticket_due_time").equals("")) {
							if (closetime >= Long.parseLong(crs.getString("ticket_due_time"))) {
								map.put("ticketdue", "yes");
							}
						}
						map.put("ticket_closed_time", strToLongDate(crs.getString("ticket_closed_time")));
						map.put("contact_name", crs.getString("contact_name"));
						map.put("contact_mobile1", crs.getString("contact_mobile1"));
						map.put("contact_mobile2", crs.getString("contact_mobile2"));
						map.put("contact_email1", crs.getString("contact_email1"));
						map.put("contact_email2", crs.getString("contact_email2"));
						map.put("emp_name", crs.getString("emp_name"));
						list.add(gson.toJson(map));
					}
					map = null;
					output.put("listdata", list);
				} else {
					output.put("msg", "No Records Found!");
				}
				crs.close();
				if (AppRun().equals("0")) {
					SOP("output =========WS_Ticket_list======== " + output.toString(1));
				}
			} catch (Exception ex) {
				SOPError("Axelaauto-App ==" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return output;
			}
		}
		return output;
	}
}
