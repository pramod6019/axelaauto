package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Location extends Connect {

	public String StrSql = "";
	public String param = "";
	public String subparam = "";
	public String subparamspan = "";
	public String country_id = "";
	public String state_id = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String city_id = "";
	public String zone_id = "";
	public String locality_id = "";
	public String dr_lead_branch_id = "";
	public String dr1 = "";
	public String dr2 = "";
	public String dr3 = "";
	public String lead_branch_id = "";
	public String enquiry_branch_id = "";
	public String lead_emp_id = "";
	public String dr_lead_emp_id = "";
	public String dr_state_id = "";
	public String dr_city_id = "";
	public String dr_zone_id = "";
	public String dr_locality_id = "";
	public String StrHTML = "";
	public String span_state_id = "";
	public String span_city_id = "";
	public String span_zone_id = "";
	public String span_locality_id = "";
	public String country_city_iddrop = "";
	public String customer_country_id = "";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Connect ct = new Connect();
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {

			if (!GetSession("comp_id", request).equals("")) {
			}
			// param = ct.PadQuotes(request.getParameter("param")).trim();
			// subparam = ct.PadQuotes(request.getParameter("subparam")).trim();
			// subparamspan =
			// ct.PadQuotes(request.getParameter("subparamspan")).trim();
			country_city_iddrop = ct.PadQuotes(request.getParameter("country_city_iddrop")).trim();
			customer_country_id = ct.PadQuotes(request.getParameter("dr_country_id")).trim();
			country_id = ct.PadQuotes(request.getParameter("country_id")).trim();
			lead_branch_id = ct.PadQuotes(request.getParameter("lead_branch_id")).trim();
			enquiry_branch_id = ct.PadQuotes(request.getParameter("enquiry_branch_id")).trim();
			lead_emp_id = ct.PadQuotes(request.getParameter("lead_emp_id")).trim();
			state_id = ct.PadQuotes(request.getParameter("state_id")).trim();
			emp_id = ct.PadQuotes(request.getParameter("emp_id")).trim();
			city_id = ct.PadQuotes(request.getParameter("city_id")).trim();
			zone_id = ct.PadQuotes(request.getParameter("zone_id")).trim();
			locality_id = ct.PadQuotes(request.getParameter("locality_id")).trim();
			dr_lead_branch_id = ct.PadQuotes(request.getParameter("lead_branch_id")).trim();
			dr_lead_emp_id = ct.PadQuotes(request.getParameter("lead_emp_id")).trim();
			dr_state_id = ct.PadQuotes(request.getParameter("dr_state_id")).trim();
			dr_city_id = ct.PadQuotes(request.getParameter("dr_city_id")).trim();
			dr_zone_id = ct.PadQuotes(request.getParameter("dr_zone_id")).trim();
			dr_locality_id = ct.PadQuotes(request.getParameter("dr_locality_id")).trim();
			span_state_id = ct.PadQuotes(request.getParameter("span_state_id")).trim();
			span_city_id = ct.PadQuotes(request.getParameter("span_city_id")).trim();
			span_zone_id = ct.PadQuotes(request.getParameter("span_zone_id")).trim();
			span_locality_id = ct.PadQuotes(request.getParameter("span_locality_id")).trim();
			dr1 = ct.PadQuotes(request.getParameter("dr1")).trim();
			dr2 = ct.PadQuotes(request.getParameter("dr2")).trim();
			dr3 = ct.PadQuotes(request.getParameter("dr3")).trim();

			// response.setContentType("text/html");
			// PrintWriter out = response.getWriter();
			// StrSql = " Select country_id, state_id, city_id "
			// + " from " + ct.compdb(comp_id) + "" + compdb(comp_id) +
			// "axela_branch "
			// + " inner join " + ct.compdb(comp_id) + "" + compdb(comp_id) +
			// "axela_city on city_id = branch_city_id "
			// + " inner join " + ct.compdb(comp_id) + "" + compdb(comp_id) +
			// "axela_state on state_id = city_state_id "
			// + " inner join " + ct.compdb(comp_id) + "" + compdb(comp_id) +
			// "axela_country on country_id = state_country_id "
			// + " where  branch_id=" + enquiry_branch_id + "";
			// CachedRowSet crs =ct.processQuery(StrSql, 0);
			// try {
			// while (crs.next()) {
			// country_id = crs.getString("country_id");
			// state_id = crs.getString("state_id");
			// city_id = crs.getString("city_id");
			// }
			// // ct.crs.close();
			// } catch (Exception e) {
			// SOPError("Axelaauto===" + this.getClass().getName());
			// SOPError("Error in " + new
			// Exception().getStackTrace()[0].getMethodName() + ": " + e);
			// }

			if (!lead_branch_id.equals("")) {
				String str = "";
				StrSql = "Select emp_id, emp_name from " + compdb(comp_id) + "axela_emp"
						+ " inner join " + compdb(comp_id) + "axela_1." + compdb(comp_id) + "axela_branch on branch_id=emp_branch_id"
						+ " where emp_branch_id=" + dr_lead_branch_id + ""
						+ "  order by emp_name";
				try {
					CachedRowSet crs2 = ct.processQuery(StrSql, 0);
					// str = "<select name=" + dr_lead_emp_id + " id=" +
					// dr_lead_emp_id + " class=selectbox ";
					// if (!dr_lead_emp_id.equals("")) {
					// str = str +
					// " onchange=\"showHint('../portal/location.jsp?lead_branch_id=' + GetReplace(this.value)+'&dr_lead_emp_id="
					// + dr_lead_emp_id + "','" + dr_lead_emp_id + "'); \"";
					// }
					str = str + " ><option value = 0>Select</option>";
					while (crs2.next()) {
						str = str + "<option value=" + crs2.getString("emp_id") + "";
						str = str + ">" + crs2.getString("emp_name") + "</option> \n";
					}
					crs2.close();
					// out.println(str);
					StrHTML = str;
				} catch (Exception ex) {
					SOPError("Axelaauto===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				}
			}
			// if(){
			// SOP("dr1==>>"+dr1+"and enquiry_branch_id=="+enquiry_branch_id);
			// SOP("dr2==>>"+dr2+"and enquiry_branch_id=="+enquiry_branch_id);
			// SOP("dr3==>>"+dr3+"and enquiry_branch_id=="+enquiry_branch_id);
			//
			//
			// if (!enquiry_branch_id.equals("") && dr1.equals("dr_country_id"))
			// {
			// out.flush();
			//
			// //
			// SOP("country_id====here" + country_id);
			// String str = "";
			// StrSql = "Select country_id, country_name from " +
			// ct.compdb(comp_id) + "" + compdb(comp_id) +
			// "axela_country order by country_name";
			// try {
			// CachedRowSet crs1 =ct.processQuery(StrSql, 0);
			// str = str +
			// "<select name=\"dr_country_id\" id=\"dr_country_id\" class=selectbox ";
			// // if (!dr_city_id.equals("")) {
			// str = str +
			// " onchange=\"showHint('../portal/location.jsp?country_id=' + GetReplace(this.value) + '&dr2=dr_state_id','state_id'); \"";
			// // }
			// str = str + " ><option value = 0>Select</option>";
			//
			// while (crs1.next()) {
			// str = str + "<option value=" + crs1.getString("country_id") +
			// " ";
			// str = str + ct.StrSelectdrop(crs1.getString("country_id"),
			// country_id);
			// str = str + ">" + crs1.getString("country_name") +
			// "</option> \n";
			// }
			// ct.crs1.close();
			// str = str + "</select>";
			// out.println(str);
			// } catch (Exception ex) {
			// SOPError("Axelaauto===" + this.getClass().getName());
			// SOPError("Error in " + new
			// Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			// }
			// }
			//
			//
			//
			// else if(!country_id.equals("") && dr2.equals("dr_state_id")) {
			// out.flush();
			// SOP("enquiry_branch_id==2==" + enquiry_branch_id);
			// String str = "";
			// SOP("StrSql==???"+StrSql);
			// StrSql = "Select state_id, state_name from " + ct.compdb(comp_id)
			// + "" + compdb(comp_id) + "axela_state where state_country_id=" +
			// ct.CNumeric(country_id) + " order by state_name";
			// SOP("StrSql==???"+StrSql);
			// try {
			// CachedRowSet crs1 =ct.processQuery(StrSql, 0);
			// str =
			// "<select name=\"dr_state_id\" id=\"dr_state_id\" class=selectbox ";
			// // if (!dr_city_id.equals("")) {
			// str = str +
			// " onchange=showHint('../portal/location.jsp?state_id=' + GetReplace(this.value)+'&dr3=dr_city_id','city_id');";
			// // }
			// str = str + " ><option value = 0>Select</option>";
			//
			// while (crs1.next()) {
			// str = str + "<option value=" + crs1.getString("state_id") + " ";
			// str = str + ct.StrSelectdrop(crs1.getString("state_id"),
			// state_id);
			// str = str + ">" + crs1.getString("state_name") + "</option> \n";
			// }
			// ct.crs1.close();
			// str = str + "</select>";
			// out.println(str);
			// } catch (Exception ex) {
			// SOPError("Axelaauto===" + this.getClass().getName());
			// SOPError("Error in " + new
			// Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			// }
			// }
			// ////////////////////////////////////////////////////////////////////////////////////////////////////
			//
			//
			// else if(!state_id.equals("") && dr3.equals("dr_city_id")) {
			// out.flush();
			// SOP("enquiry_branch_id==3==" + enquiry_branch_id);
			// String str = "";
			// StrSql = "Select city_id, city_name, city_state_id from " +
			// ct.compdb(comp_id) + "" + compdb(comp_id) +
			// "axela_city where city_state_id=" + ct.CNumeric(state_id) +
			// " order by city_name";
			// str =
			// "<select name=\"dr_city_id\" id=\"dr_city_id\" class=selectbox ";
			// // if (!dr_zone_id.equals("")) {
			// // str = str +
			// " onchange=\"showHint('../portal/location.jsp?city_id=' + GetReplace(this.value)+'&dr_zone_id="
			// + dr_zone_id + "&span_zone_id=" + span_zone_id +
			// "&dr_locality_id=" +
			// dr_locality_id + "&span_locality_id=" + span_locality_id + "','"
			// + span_zone_id + "'); \"";
			// // }
			// str = str + " ><option value = 0>Select</option>";
			// try {
			// CachedRowSet crs1 =ct.processQuery(StrSql, 0);
			// while (crs1.next()) {
			// str = str + "<option value=" + crs1.getString("city_id") + " ";
			// str = str + ct.StrSelectdrop(crs1.getString("city_id"), city_id);
			// str = str + ">" + crs1.getString("city_name") + "</option> \n";
			// }
			// str = str + "</select>";
			// ct.crs1.close();
			// out.println(str);
			// } catch (Exception ex) {
			// SOPError("Axelaauto===" + this.getClass().getName());
			// SOPError("Error in " + new
			// Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			// }
			// }
			// }
			// }
			// }
			if (!country_id.equals("")) {
				String str = "";
				StrSql = "Select state_id, state_name"
						+ " from " + compdb(comp_id) + "axela_state"
						+ " where state_country_id = " + ct.CNumeric(country_id) + ""
						+ " order by state_name";
				try {
					CachedRowSet crs1 = ct.processQuery(StrSql, 0);
					str = "<select name=" + dr_state_id + " id=" + dr_state_id + " class=selectbox ";
					if (!dr_city_id.equals("")) {
						str = str + " onchange=\"showHint('../portal/location.jsp?state_id=' + GetReplace(this.value)+'&dr_city_id=" + dr_city_id + "&dr_zone_id=" + dr_zone_id + "&span_zone_id="
								+ span_zone_id + "&dr_locality_id=" + dr_locality_id + "&span_locality_id=" + span_locality_id + "','" + span_city_id + "'); \"";
					}
					str = str + " ><option value = 0>Select</option>";

					while (crs1.next()) {
						str = str + "<option value=" + crs1.getString("state_id") + "";
						str = str + ">" + crs1.getString("state_name") + "</option> \n";
					}
					crs1.close();
					// out.println(str);
					StrHTML = str;
				} catch (Exception ex) {
					SOPError("Axelaauto===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				}
			}

			// //////////////////////////////////////////////////////////////////////////////////////////////////

			if (!state_id.equals("")) {
				String str = "";
				StrSql = "Select city_id, city_name, city_state_id"
						+ " from " + compdb(comp_id) + "axela_city"
						+ " where city_state_id = " + ct.CNumeric(state_id) + ""
						+ " order by city_name";
				// SOP("dr_city_id=="+dr_city_id);
				str = "<select name=" + dr_city_id + " id=" + dr_city_id + " class=form-control";
				if (!dr_zone_id.equals("")) {
					str = str + " onchange=\"showHint('../portal/location.jsp?city_id=' + GetReplace(this.value)+'&dr_zone_id=" + dr_zone_id + "&span_zone_id=" + span_zone_id + "&dr_locality_id="
							+ dr_locality_id + "&span_locality_id=" + span_locality_id + "','" + span_zone_id + "'); \"";
				}
				str = str + "><option value = 0>Select</option>";
				try {
					CachedRowSet crs1 = ct.processQuery(StrSql, 0);
					while (crs1.next()) {
						str = str + "<option value=" + crs1.getString("city_id") + "";
						str = str + ">" + crs1.getString("city_name") + "</option> \n";
					}
					str = str + "</select>";
					crs1.close();
					// out.println(str);
					StrHTML = str;
				} catch (Exception ex) {
					SOPError("Axelaauto===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				}
			}

			if (!city_id.equals("")) {
				String str = "";
				try {
					StrSql = "Select zone_id,zone_name,zone_city_id from " + compdb(comp_id) + "axela_zone where zone_city_id=" + ct.CNumeric(city_id) + " order by zone_name";
					SOP(StrSql);
					str = "<select name=" + dr_zone_id + " id=" + dr_zone_id + " class=selectbox ";
					if (!dr_locality_id.equals("")) {
						str = str + " onchange=\"showHint('../portal/location.jsp?zone_id=' + GetReplace(this.value)+'&dr_locality_id=" + dr_locality_id + "'&span_locality_id=" + span_locality_id
								+ "','" + span_locality_id + "'); \"";
					}
					str = str + " ><option value = 0>Select</option>";
					CachedRowSet crs = ct.processQuery(StrSql, 0);
					while (crs.next()) {
						str = str + "<option value=" + crs.getString("zone_id") + "";
						str = str + ">" + crs.getString("zone_name") + "</option> \n";
					}
					str = str + "</select>";
					crs.close();
					// out.println(str);
					StrHTML = str;
				} catch (Exception ex) {
					SOPError("Axelaauto===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				}
			}

			if (!zone_id.equals("")) {
				String str = "";
				try {
					StrSql = "Select locality_id, locality_name from " + compdb(comp_id) + "axela_locality where locality_zone_id=" + ct.CNumeric(zone_id) + " order by locality_name";

					str = "<select name=" + dr_locality_id + " id=" + dr_locality_id + " class='form-control' onchange='PopulateLocation();' ";
					str = str + " ><option value = 0>Select</option>";
					CachedRowSet crs = ct.processQuery(StrSql, 0);
					while (crs.next()) {
						str = str + "<option value=" + crs.getString("locality_id") + "";
						str = str + ">" + crs.getString("locality_name") + "</option>\n";
					}
					str = str + "</select>";
					crs.close();

					StrHTML = str;
				} catch (Exception ex) {
					SOPError("Axelaauto===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				}
			}

			if (!country_city_iddrop.equals("")) {
				String str = "";
				StrSql = "SELECT city_id, city_name from " + compdb(comp_id) + "axela_city "
						+ " inner join " + compdb(comp_id) + "axela_state on state_id=city_state_id "
						+ " inner join " + compdb(comp_id) + "axela_country on country_id=state_country_id "
						+ " where state_country_id=" + country_city_iddrop + " order by city_name";
				// " inner join " + ct.compdb(comp_id) + "" + compdb(comp_id) +
				// "axela_country on country_id=city_country_id " +
				// " where city_country_id=" + country_city_iddrop +
				// " order by city_name";

				// SOP(StrSql);
				try {
					CachedRowSet crs1 = ct.processQuery(StrSql, 0);
					str = "<select name=dr_city_id id=dr_city_id class=selectbox";

					str = str + " ><option value = 0>Select</option>";

					while (crs1.next()) {
						str = str + "<option value=" + crs1.getString("city_id") + "";
						str = str + ">" + crs1.getString("city_name") + "</option> \n";
					}
					crs1.close();
					// out.println(str);
					StrHTML = str;
				} catch (Exception ex) {
					SOPError("Axelaauto===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				}
			}
		}

		// out.flush();
		// out.close();

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
// Manthu

