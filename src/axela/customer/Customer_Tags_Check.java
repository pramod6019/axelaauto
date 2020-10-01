package axela.customer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Customer_Tags_Check extends Connect {

	public String StrSql = "";
	public String comp_id = "0";
	public String StrHTML = "";
	public String id = "";
	public String emp_id = "0";
	public String vehicle = "0";
	public String veh_id = "0";
	public String update = "";
	public String add = "";
	public String enquiry_id = "0";
	public String customer_id = "0";
	public String tags = "0";
	public String tags_content = "";
	public String hexCode = "";
	public String customer = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
				// customer_id = CNumeric(PadQuotes(request.getParameter("id")));
				vehicle = PadQuotes(request.getParameter("vehicle"));
				veh_id = CNumeric(PadQuotes(request.getParameter("veh_id")));
				customer_id = CNumeric(PadQuotes(request.getParameter("customer_id")));
				tags_content = PadQuotes(request.getParameter("tags_content"));
				update = PadQuotes(request.getParameter("update"));
				add = PadQuotes(request.getParameter("add"));
				tags = PadQuotes(request.getParameter("tags"));
				customer = PadQuotes(request.getParameter("customer"));
				// SOP("customer===========" + customer + " customer_id==========" + customer_id + "enquiry_id" + id);
				if (add.equals("yes")) {
					StrHTML = addTags(id, comp_id, customer_id);
				}

				if (update.equals("yes")) {
					StrHTML = updateTags(id, comp_id, customer_id);
				}

				if (!customer_id.equals("0") && tags_content.equals("yes")) {
					StrHTML = PopulateTagsPopover(customer_id, comp_id);
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	// to populate tags in popup
	public String PopulateTags(String customer_id, String comp_id) {
		try {

			StrSql = "SELECT tagtrans_tag_id, tag_colour, tag_name "
					+ " FROM " + compdb(comp_id) + "axela_customer_tag_trans "
					+ " INNER JOIN  " + compdb(comp_id) + "axela_customer_tag "
					+ " ON tag_id = tagtrans_tag_id "
					+ " WHERE tagtrans_customer_id =" + customer_id
					+ " ORDER BY tagtrans_tag_id";
			// SOP("PopulateTags==StrSql===" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);
			StrSql = "";
			if (!customer_id.equals("0")) {
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						StrSql += "$('#customer_tagclass > > input').tagsinput('add', { 'value': " + crs.getString("tagtrans_tag_id")
								+ " , 'text': '" + crs.getString("tag_name")
								+ "' , 'continent': '" + crs.getString("tag_colour") + "' });";
					}
				} else {
					StrSql += "$('#customer_tagclass > > input').tagsinput('add', { 'value':  0, 'text': 'No Tag Selected' , 'continent': '#ff0000' });";
					// StrSql = "No Tags Found";
				}
			}

			crs.close();
			// SOP("PopulateTags===" + StrSql.toString());
			return StrSql.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public String PopulateTagsJS(String customer_id, String comp_id) {
		try {

			StrSql = "SELECT tag_id, tag_colour, tag_name"
					+ " FROM " + compdb(comp_id) + "axela_customer_tag"
					+ " ORDER BY tag_id";

			// SOP("PopulateTags==StrSql===" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);
			StrSql = "function addTag(idname){"
					+ " switch(idname){ ";
			if (!customer_id.equals("0")) {
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						StrSql += " case " + crs.getString("tag_id") + ": ";
						StrSql += "$('#customer_tagclass > > input').tagsinput('add', { 'value': " + crs.getString("tag_id")
								+ " , 'text': '" + crs.getString("tag_name")
								+ "' , 'continent': '" + crs.getString("tag_colour") + "' });";
						StrSql += " break;";
					}
					StrSql += " default: $('#customer_tagclass > > input').tagsinput('add', { 'value':  0, 'text': 'No Tag Selected' , 'continent': '#ff0000' }); break;";
				}
			}

			StrSql += " } }";

			crs.close();
			// SOP("PopulateTagsJS===" + StrSql.toString());
			return StrSql.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	// to populate tags in popup box
	public String PopulateTagsPopover(String customer_id, String comp_id) {
		try {

			StrSql = "SELECT tag_id, tag_colour, tag_name"
					+ " FROM " + compdb(comp_id) + "axela_customer_tag WHERE tag_id NOT IN (1,2)"
					+ " AND tag_id NOT IN ("
					+ " SELECT COALESCE(tagtrans_tag_id,'')"
					+ " FROM " + compdb(comp_id) + "axela_customer_tag_trans"
					+ " WHERE tagtrans_customer_id = " + customer_id + " )";

			// SOP("tags PopulateTagsPopover=====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);;
			StrSql = "";
			if (!customer_id.equals("0")) {
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						StrSql += "<button class='btn' style='background:" + crs.getString("tag_colour") + " ; color:white'"
								+ " onclick=\"tagcall(" + crs.getString("tag_id") + ");$('#popover').trigger('click');\" id=tag" + crs.getString("tag_id")
								+ " value='" + crs.getString("tag_colour") + "' >" + crs.getString("tag_name")
								+ "</button>";
					}
				} else {
					StrSql += "<font color='#ff0000'>No Tags</font>";
				}
			}
			crs.close();
			// SOP("tags PopulateTagsPopover=====toString" + StrSql.toString());
			return StrSql.toString();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String addTags(String id, String comp_id, String customer_id) {
		try {
			StringBuilder Str = new StringBuilder();
			tags = tags.replace("tag", "");

			String tag_check = CNumeric(PadQuotes(ExecuteQuery("SELECT tagtrans_tag_id"
					+ " FROM " + compdb(comp_id) + "axela_customer_tag_trans"
					+ " WHERE tagtrans_tag_id = " + tags
					+ " AND tagtrans_customer_id = " + customer_id)));
			// SOP("tags" + tag_check);
			if (!tags.equals("0") && !tags.equals("") && tag_check.equals("0")) {

				updateQuery("INSERT INTO " + compdb(comp_id) + "axela_customer_tag_trans VALUE(" + tags + "," + customer_id + ")");

				if (!id.equals("0") && !vehicle.equals("yes")) {
					String tagname = ExecuteQuery("SELECT tag_name FROM " + compdb(comp_id) + "axela_customer_tag WHERE tag_id = " + tags);
					String history_actiontype = "Tag Added";
					StrSql = "INSERT into " + compdb(comp_id) + "axela_sales_enquiry_history"
							+ " (history_enquiry_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_newvalue,"
							+ " history_oldvalue)"
							+ " values ("
							+ " '" + id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + tagname + "',"
							+ "'')";// history_oldvalue
					updateQuery(StrSql);
				} else if (vehicle.equals("yes")) {

					String tagname = ExecuteQuery("SELECT tag_name FROM " + compdb(comp_id) + "axela_customer_tag WHERE tag_id = " + tags);
					String history_actiontype = "Tag Added";
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
							+ " (history_veh_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_newvalue,"
							+ " history_oldvalue)"
							+ " VALUES ("
							+ " '" + veh_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + tagname + "',"
							+ "'')";// history_oldvalue
					updateQuery(StrSql);
				}
				if (customer.equals("yes")) {
					String tagname = ExecuteQuery("SELECT tag_name FROM " + compdb(comp_id) + "axela_customer_tag WHERE tag_id = " + tags);
					String history_actiontype = "Tag Added";
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_history"
							+ " (history_customer_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_newvalue,"
							+ " history_oldvalue)"
							+ " VALUES ("
							+ " '" + customer_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + tagname + "',"
							+ "'')";// history_oldvalue
					updateQuery(StrSql);
				}

			}

			// SOP("tags check=====" + StrSql);
			return "<font color='#ff0000' >Tag Updated Successfully!</font>";

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String updateTags(String id, String comp_id, String customer_id) {
		try {
			StringBuilder Str = new StringBuilder();

			SOP("ID-----" + id);
			SOP("veh_id-----" + veh_id);
			SOP("customer-----" + customer);

			if (!id.equals("0") || !veh_id.equals("0") || customer.equals("yes")) {
				String oldtagname = "SELECT tag_name"
						+ " FROM " + compdb(comp_id) + "axela_customer_tag"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_tag_trans ON tagtrans_tag_id = tag_id"
						+ " WHERE 1 = 1"
						+ " AND tag_id NOT IN (1, 2)";

				if (!tags.equals("0") && !tags.equals("")) {
					oldtagname += " AND tagtrans_tag_id NOT IN (" + tags + ")";
				}
				oldtagname += " AND tagtrans_customer_id =" + customer_id;

				oldtagname = ExecuteQuery(oldtagname);

				updateQuery("DELETE FROM " + compdb(comp_id) + "axela_customer_tag_trans"
						+ " WHERE tagtrans_tag_id NOT IN (1, 2)"
						+ " AND tagtrans_customer_id = " + customer_id);

				String tagsArr[] = tags.split(",");
				for (String value : tagsArr) {
					if (!value.equals("0") && !value.equals("") && !value.equals("1") && !value.equals("2")) {
						updateQuery("INSERT INTO " + compdb(comp_id) + "axela_customer_tag_trans VALUE('" + value + "','" + customer_id + "')");
					}
				}

				String history_actiontype = "Tag Removed";

				if (!oldtagname.equals("")) {
					if (!id.equals("0") && !vehicle.equals("yes")) {
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " (history_enquiry_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_newvalue,"
								+ " history_oldvalue)"
								+ " VALUES ("
								+ " '" + id + "',"
								+ " '" + emp_id + "',"
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',"
								+ " ''," // history_newvalue
								+ "'" + oldtagname + "')";
						updateQuery(StrSql);
					} else if (vehicle.equals("yes")) {
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
								+ " (history_veh_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_newvalue,"
								+ " history_oldvalue)"
								+ " VALUES ("
								+ " '" + veh_id + "',"
								+ " '" + emp_id + "',"
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',"
								+ " ''," // history_newvalue
								+ "'" + oldtagname + "')";
						updateQuery(StrSql);
					}
					if (customer.equals("yes")) {
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_history"
								+ " (history_customer_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_newvalue,"
								+ " history_oldvalue)"
								+ " VALUES ("
								+ " '" + customer_id + "',"
								+ " '" + emp_id + "',"
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',"
								+ " ''," // history_newvalue
								+ "'" + oldtagname + "')";
						updateQuery(StrSql);
					}
				}

			}

			// SOP("tags check=====" + StrSql);
			return "<font color='#ff0000' >Tag Updated Successfully!</font>";
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		doPost(request, response);
	}
}
