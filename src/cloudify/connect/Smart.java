package cloudify.connect;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

public class Smart extends Connect {

	public String count = "";
	public int counti;
	public String row = "";
	public String queryrow = "";
	public String param = "";
	public String value_1 = "";
	public String filter = "";
	private StringBuilder str_txt = new StringBuilder();
	public String[] split = new String[50];
	public String field_txt = "";
	public String search = "";
	public String txt_value = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
	}

	public String PopulateSmartSearch(String smartarr[][], HttpServletRequest request) {
		count = CNumeric(PadQuotes(request.getParameter("dr_searchcount_var")));
		counti = Integer.parseInt(count);
		StringBuilder Str = new StringBuilder();
		for (int i = 1; i <= counti; i++) {
			row = request.getParameter(i + "_dr_field");
			// checking whether the row is there or not
			if (row != null) {
				Str.append("<tr><td><select name=")
						.append(i).append("_dr_field class=form-control onchange = \"SmartPopulateCriteria(this,")
						.append(i).append(");SmartPopulateParam1(this,").append(i).append("); BootstrapSelect();\">\n");

				for (int j = 0; j < smartarr.length; j++) {
					Str.append("<option value=\"").append(j).append("-").append(smartarr[j][1]).append("\"").append(StrSelectdrop(j + "-" + smartarr[j][1],
							PadQuotes(request.getParameter(i + "_dr_field")))).append(">").append(smartarr[j][0]).append("</option>");
				}

				Str.append("</select></td>\n");
				Str.append("<td><div id=dr").append(i).append(">\n");
				split = row.split("-");
				if (split[1].equals("text")) {
					Str.append("<select name=").append(i).append("_dr_param class=form-control>");

					Str.append("<option value=0-text ").append(StrSelectdrop("0-text", PadQuotes(request.getParameter(i + "_dr_param")))).append(">contains</option>"
							+ "<option value=1-text ").append(StrSelectdrop("1-text", PadQuotes(request.getParameter(i + "_dr_param")))).append(">not contains</option>"
							+ "<option value=2-text ").append(StrSelectdrop("2-text", PadQuotes(request.getParameter(i + "_dr_param")))).append(">is equal to</option>"
							+ "<option value=3-text ").append(StrSelectdrop("3-text", PadQuotes(request.getParameter(i + "_dr_param")))).append(">is not equal to</option>"
							+ "<option value=4-text ").append(StrSelectdrop("4-text", PadQuotes(request.getParameter(i + "_dr_param")))).append(">starts with</option>"
							+ "<option value=5-text ").append(StrSelectdrop("5-text", PadQuotes(request.getParameter(i + "_dr_param")))).append(">does not start with</option>"
							+ "<option value=6-text ").append(StrSelectdrop("6-text", PadQuotes(request.getParameter(i + "_dr_param")))).append(">ends with</option>"
							+ "<option value=7-text ").append(StrSelectdrop("7-text", PadQuotes(request.getParameter(i + "_dr_param")))).append(">does not end with</option>"
							+ "<option value=8-text ").append(StrSelectdrop("8-text", PadQuotes(request.getParameter(i + "_dr_param")))).append(">is blank</option>"
							+ "<option value=9-text ").append(StrSelectdrop("9-text", PadQuotes(request.getParameter(i + "_dr_param")))).append(">is not blank</option>"
							+ "</select>");

				} else if (split[1].equals("numeric")) {
					Str.append("<select name=" + i + "_dr_param  class=form-control>"
							+ "<option value=0-numeric " + StrSelectdrop("0-numeric", PadQuotes(request.getParameter(i + "_dr_param"))) + ">is equal to</option>"
							+ "<option value=1-numeric " + StrSelectdrop("1-numeric", PadQuotes(request.getParameter(i + "_dr_param"))) + ">is not equal to</option>"
							+ "<option value=2-numeric " + StrSelectdrop("2-numeric", PadQuotes(request.getParameter(i + "_dr_param"))) + ">is less than</option>"
							+ "<option value=3-numeric " + StrSelectdrop("3-numeric", PadQuotes(request.getParameter(i + "_dr_param"))) + ">is less than or equal to</option>"
							+ "<option value=4-numeric " + StrSelectdrop("4-numeric", PadQuotes(request.getParameter(i + "_dr_param"))) + ">is greater than</option>"
							+ "<option value=5-numeric " + StrSelectdrop("5-numeric", PadQuotes(request.getParameter(i + "_dr_param"))) + ">is greater than or equal to</option>"
							+ "<option value=6-numeric " + StrSelectdrop("6-numeric", PadQuotes(request.getParameter(i + "_dr_param"))) + ">is between</option>"
							+ "<option value=7-numeric " + StrSelectdrop("7-numeric", PadQuotes(request.getParameter(i + "_dr_param"))) + ">is not between</option>" + "</select>");
				} else if (split[1].equals("date")) {
					Str.append("<select name=" + i + "_dr_param id=" + i + "_dr_param class=form-control onchange = SmartPopulateParam1(this," + i + ");SmartPopulateParam2(this," + i + ");>"
							+ "<option value=0-date " + StrSelectdrop("0-date", PadQuotes(request.getParameter(i + "_dr_param"))) + ">is equal to</option>"
							+ "<option value=1-date " + StrSelectdrop("1-date", PadQuotes(request.getParameter(i + "_dr_param"))) + ">is not equal to</option>"
							+ "<option value=2-date " + StrSelectdrop("2-date", PadQuotes(request.getParameter(i + "_dr_param"))) + ">is less than</option>"
							+ "<option value=3-date " + StrSelectdrop("3-date", PadQuotes(request.getParameter(i + "_dr_param"))) + ">is less than or equal to</option>"
							+ "<option value=4-date " + StrSelectdrop("4-date", PadQuotes(request.getParameter(i + "_dr_param"))) + ">is greater than</option>"
							+ "<option value=5-date " + StrSelectdrop("5-date", PadQuotes(request.getParameter(i + "_dr_param"))) + ">is greater than or equal to</option>"
							+ "<option value=6-date " + StrSelectdrop("6-date", PadQuotes(request.getParameter(i + "_dr_param"))) + ">is between</option>"
							+ "<option value=7-date " + StrSelectdrop("7-date", PadQuotes(request.getParameter(i + "_dr_param"))) + ">is not between</option>"
							+ "<option value=8-date " + StrSelectdrop("8-date", PadQuotes(request.getParameter(i + "_dr_param"))) + ">is blank</option>"
							+ "<option value=9-date " + StrSelectdrop("9-date", PadQuotes(request.getParameter(i + "_dr_param"))) + ">is not blank</option>"
							+ "</select>");
				} else if (split[1].equals("boolean")) {
					Str.append("<select name=").append(i).append("_dr_param  class=form-control>"
							+ "<option value=0-boolean ").append(StrSelectdrop("0-boolean", PadQuotes(request.getParameter(i + "_dr_param")))).append(">is equal to</option>" + "</select>");
				}
				Str.append("</td>\n");
				Str.append("<td><div id=booltxt").append(i).append(">");
				param = PadQuotes(request.getParameter(i + "_dr_param"));
				value_1 = PadQuotes(request.getParameter(i + "_txt_value_1"));
				if (split[1].equals("text")) {
					Str.append("<input name=").append(i).append("_txt_value_1 type=text class=form-control size=30  value=\"")
							.append(value_1).append("\" onchange='GetValues();' ></td>\n");
				} else if (split[1].equals("numeric")) {
					if (param.equals("6-numeric") || param.equals("7-numeric")) {
						Str.append("<input name=").append(i).append("_txt_value_1  type=text class=form-control size=30 value=").append(value_1).append(">&nbsp");
						Str.append("<input name=").append(i).append("_txt_value_2 type=text class=form-control size=30 value=")
								.append(PadQuotes(request.getParameter(i + "_txt_value_2"))).append("></td>\n");
					} else {
						Str.append("<input name=").append(i).append("_txt_value_1 type=text class=form-control size=30 value=\"").append(value_1).append("\"></td>\n");
					}
				} else if (split[1].equals("date")) {
					/* modification on 14th march 2013 */
					if (param.equals("6-date") || param.equals("7-date")) {
						Str.append("<input  name=")
								.append(i)
								.append("_txt_value_1 id=")
								.append(i)
								.append("_txt_value_1 type=text class='form-control datetimepicker' onmouseover=\"$('.datetimepicker').bootstrapMaterialDatePicker({ format : 'DD/MM/YYYY HH:mm',switchOnClick : true, clearButton: true });\" size=16 maxlength=16 "
										+ "value=\"")
								.append(value_1).append("\">&nbsp");
						Str.append("<input  name=")
								.append(i)
								.append("_txt_value_2 id=")
								.append(i)
								.append("_txt_value_2 type=text class='form-control datetimepicker' onmouseover=\"$('.datetimepicker').bootstrapMaterialDatePicker({ format : 'DD/MM/YYYY HH:mm',switchOnClick : true, clearButton: true });\" size=16 maxlength=16"
										+ " value=\"")
								.append(PadQuotes(request.getParameter(i + "_txt_value_2"))).append("\"></td>\n ");
					} else {
						Str.append("<input  name=")
								.append(i)
								.append("_txt_value_1 id=")
								.append(i)
								.append("_txt_value_1 type=text class='form-control datetimepicker' onmouseover=\"$('.datetimepicker').bootstrapMaterialDatePicker({ format : 'DD/MM/YYYY HH:mm',switchOnClick : true, clearButton: true });\" size=16 maxlength=16 "
										+ "value='")
								.append(value_1).append("'>");
					}
					/* end of modification on 14th march 2013 */
				} else if (split[1].equals("boolean")) {
					Str.append("<select name=").append(i).append("_dr_value_1 class=form-control>" + "<option value=0 ")
							.append(StrSelectdrop("0", PadQuotes(request.getParameter(i + "_dr_value_1"))))
							.append(">NO</option>" + "<option value=1 ").append(StrSelectdrop("1", PadQuotes(request.getParameter(i + "_dr_value_1"))))
							.append(">YES</option>" + "</select>");
				}
				Str.append("</div></td>\n");
				Str.append("<td><select  class=form-control name=")
						.append(i).append("_dr_filter  >\n" + "<option value=and ")
						.append(StrSelectdrop("and", PadQuotes(request.getParameter(i + "_dr_filter"))))
						.append(">AND</option>\n" + "<option value=or ")
						.append(StrSelectdrop("or", PadQuotes(request.getParameter(i + "_dr_filter"))))
						.append(">OR</option>\n");
				Str.append("</select></td>\n");
				Str.append("<td><center><div class=\"btn btn-group\"><i class=\"fa fa-minus-square\" style=\"font-size:30px\" id=\"sample_editable_1_new\" onclick=\'this.parentNode.parentNode.parentNode.parentNode.remove();FormFocus();\' onmouseover=\"this.title='Remove Criteria'\"></i> <i class=\"fa fa-plus-square\" style=\"font-size:30px\" id=\"sample_editable_1_new\" onclick=\'LoadRows();BootstrapSelect();\' onmouseover=\"this.title='Add Criteria'\"></i></div></center>");
				Str.append("</td>\n");
				Str.append("</tr>");

			}
		}
		return Str.toString();
	}

	public String BuildSmartSql(String smartarr[][], HttpServletRequest request) {
		count = request.getParameter("dr_searchcount_var");
		counti = Integer.parseInt(count);
		StringBuilder Str = new StringBuilder();
		int j = 0;
		String dr_and_or = "";
		String fieldname = "";
		HttpSession session = request.getSession(true);
		String comp_id = CNumeric(GetSession("comp_id", request));

		for (int i = 1; i <= counti; i++) {
			queryrow = request.getParameter(i + "_dr_field");
			if (queryrow != null) {
				// checking wheter the row is there or not

				for (j = 0; j < smartarr.length; j++) {
					txt_value = PadQuotes(request.getParameter(i + "_txt_value_1"));
					fieldname = smartarr[j][2];
					if (fieldname.contains("compdb.")) {
						fieldname = fieldname.replace("compdb.",
								compdb(comp_id));
					} else if (fieldname.contains("maindb.")) {
						fieldname = fieldname.replace("maindb.", maindb());
					}
					// SOPError("smartarr===" + smartarr[j][2]);
					if ((!txt_value.equals("") && txt_value != null)
							|| (PadQuotes(request.getParameter(i + "_dr_field")).equals(j + "-boolean"))
							|| (PadQuotes(request.getParameter(i + "_dr_param")).equals("8-text"))
							|| (PadQuotes(request.getParameter(i + "_dr_param")).equals("9-text"))
							|| (PadQuotes(request.getParameter(i + "_dr_param")).equals("8-date"))
							|| (PadQuotes(request.getParameter(i + "_dr_param")).equals("9-date"))) {

						// SOPError("fieldname===" + fieldname);
						if (str_txt.toString().equals("")) {
							str_txt.append(" AND ");
						}

						if (PadQuotes(request.getParameter(i + "_dr_field")).equals(j + "-text")) {
							// ////// Start Keyword Search
							if (PadQuotes(request.getParameter(i + "_dr_field")).equals("0-text") && smartarr[0][2].equals("keyword_arr")) {
								str_txt.append(" ").append(dr_and_or).append(" ");
								str_txt.append("( ");
								// //// Loop keyword search for Text and Numeric
								// Fields
								for (j = 1; j < smartarr.length; j++) {

									fieldname = smartarr[j][2];

									if (!fieldname.toLowerCase().contains("select ") && (smartarr[j][1].contains("numeric") || smartarr[j][1].contains("text"))) {
										if (j == 1) {
											str_txt.append(" ");
										} else {
											str_txt.append(" or ");
										}
										if (PadQuotes(request.getParameter(i + "_dr_param")).equals("8-text")) {
											str_txt.append(fieldname).append(" ='' ");
										} else if (PadQuotes(request.getParameter(i + "_dr_param")).equals("9-text")) {
											str_txt.append(fieldname).append(" !='' ");
										} else if (PadQuotes(request.getParameter(i + "_dr_param")).equals("2-text")) {
											str_txt.append(fieldname).append(" ='" + txt_value + "' ");
										} else {
											str_txt.append(fieldname).append(" like '%").append(txt_value).append("%'");
										}
									}
								}
								str_txt.append(" )");
								dr_and_or = PadQuotes(request.getParameter(i
										+ "_dr_filter"));
								// SOPError("array:123" + str_txt);
								// ////// End Keyword Search

							} else {
								str_txt.append(" ").append(dr_and_or).append(" ");
								str_txt.append(" ").append(fieldname).append(" ");

								if (PadQuotes(request.getParameter(i + "_dr_param")).equals("0-text")) {

									str_txt.append(" LiKE '%").append(txt_value).append("%'");

								} else if (PadQuotes(request.getParameter(i + "_dr_param")).equals("1-text")) {

									str_txt.append(" NOT LiKE '%").append(txt_value).append("%'");

								} else if (PadQuotes(request.getParameter(i + "_dr_param")).equals("2-text")) {

									str_txt.append(" = '").append(txt_value).append("'");

								} else if (PadQuotes(request.getParameter(i + "_dr_param")).equals("3-text")) {

									str_txt.append(" <> '").append(txt_value).append("'");

								} else if (PadQuotes(request.getParameter(i + "_dr_param")).equals("4-text")) {

									str_txt.append(" LiKE '").append(txt_value).append("%'");

								} else if (PadQuotes(request.getParameter(i + "_dr_param")).equals("5-text")) {

									str_txt.append(" NOT LiKE '").append(txt_value).append("%'");

								} else if (PadQuotes(request.getParameter(i + "_dr_param")).equals("6-text")) {

									str_txt.append(" LiKE '%").append(txt_value).append("'");

								} else if (PadQuotes(request.getParameter(i + "_dr_param")).equals("7-text")) {

									str_txt.append(" NOT LiKE '%").append(txt_value).append("'");

								} else if (PadQuotes(request.getParameter(i + "_dr_param")).equals("8-text")) {

									str_txt.append(" ='' ");

								} else if (PadQuotes(request.getParameter(i + "_dr_param")).equals("9-text")) {

									str_txt.append(" !='' ");
								}

								dr_and_or = PadQuotes(request.getParameter(i + "_dr_filter"));
								if (fieldname.toLowerCase().contains("select ")) {
									str_txt.append(")");
								}
								break;
							}
						} else if (PadQuotes(request.getParameter(i + "_dr_field")).equals(j + "-numeric")) {
							str_txt.append(" ").append(dr_and_or).append(" ");
							str_txt.append(" ").append(fieldname).append(" ");
							if (PadQuotes(request.getParameter(i + "_dr_param")).equals("0-numeric")) {
								str_txt.append("  =  ").append(CNumeric(txt_value)).append("");
							} else if (PadQuotes(request.getParameter(i + "_dr_param")).equals("1-numeric")) {
								str_txt.append(" <>  ").append(CNumeric(txt_value)).append("");
							} else if (PadQuotes(request.getParameter(i + "_dr_param")).equals("2-numeric")) {
								str_txt.append(" < ").append(CNumeric(txt_value)).append("");
							} else if (PadQuotes(request.getParameter(i + "_dr_param")).equals("3-numeric")) {
								str_txt.append(" <= '").append(CNumeric(txt_value)).append("'");
							} else if (PadQuotes(request.getParameter(i + "_dr_param")).equals("4-numeric")) {
								str_txt.append(" > ").append(CNumeric(txt_value)).append("");
							} else if (PadQuotes(request.getParameter(i + "_dr_param")).equals("5-numeric")) {
								str_txt.append(" >= ").append(CNumeric(txt_value)).append("");
							} else if (PadQuotes(request.getParameter(i + "_dr_param")).equals("6-numeric")) {
								str_txt.append(" between ").append(CNumeric(txt_value)).append(" and ")
										.append(PadQuotes(request.getParameter(i + "_txt_value_2"))).append("");
							} else if (PadQuotes(request.getParameter(i + "_dr_param")).equals("7-numeric")) {
								str_txt.append(" not between ").append(CNumeric(txt_value)).append(" and ")
										.append(PadQuotes(request.getParameter(i + "_txt_value_2"))).append("");
							}
							dr_and_or = PadQuotes(request.getParameter(i + "_dr_filter"));
							if (fieldname.toLowerCase().contains("select ")) {
								str_txt.append(")");
							}
							break;
						} else if (PadQuotes(request.getParameter(i + "_dr_field")).equals(j + "-date")) {

							str_txt.append(" ").append(dr_and_or).append(" ");
							str_txt.append(" ").append(fieldname).append(" ");
							txt_value = ConvertLongDateToStr(txt_value);
							if (PadQuotes(request.getParameter(i + "_dr_param")).equals("0-date")) {
								str_txt.append(" = '").append(txt_value).append("'");
							} else if (PadQuotes(request.getParameter(i + "_dr_param")).equals("1-date")) {
								str_txt.append(" <> '").append(txt_value).append("'");
							} else if (PadQuotes(request.getParameter(i + "_dr_param")).equals("2-date")) {
								str_txt.append(" < '").append(txt_value).append("'");
							} else if (PadQuotes(request.getParameter(i + "_dr_param")).equals("3-date")) {
								str_txt.append(" <= '").append(txt_value).append("'");
							} else if (PadQuotes(request.getParameter(i + "_dr_param")).equals("4-date")) {
								str_txt.append(" > '").append(txt_value).append("'");
							} else if (PadQuotes(request.getParameter(i + "_dr_param")).equals("5-date")) {
								str_txt.append(" >= '").append(txt_value).append("'");
							} else if (PadQuotes(request.getParameter(i + "_dr_param")).equals("6-date")) {
								str_txt.append(" between ").append(txt_value).append(" and ")
										.append(ConvertLongDateToStr(PadQuotes(request.getParameter(i + "_txt_value_2")))).append("");
							} else if (PadQuotes(request.getParameter(i + "_dr_param")).equals("7-date")) {
								str_txt.append(" not between '").append(txt_value).append(" and ")
										.append(ConvertLongDateToStr(PadQuotes(request.getParameter(i + "_txt_value_2")))).append("");
							} else if (PadQuotes(request.getParameter(i + "_dr_param")).equals("8-date")) {
								str_txt.append(" ='' ");
							} else if (PadQuotes(request.getParameter(i + "_dr_param")).equals("9-date")) {
								str_txt.append(" !='' ");
							}
							dr_and_or = PadQuotes(request.getParameter(i + "_dr_filter"));
							if (fieldname.toLowerCase().contains("select ")) {
								str_txt.append(")");
							}
							break;
						} else if (PadQuotes(request.getParameter(i + "_dr_field")).equals(j + "-boolean")) {
							str_txt.append(" ").append(dr_and_or).append(" ");
							str_txt.append(" ").append(fieldname).append(" ");
							if (PadQuotes(request.getParameter(i + "_dr_param")).equals("0-boolean")) {
								str_txt.append(" = '").append(PadQuotes(request.getParameter(i + "_dr_value_1"))).append("'");
							}
							dr_and_or = PadQuotes(request.getParameter(i + "_dr_filter"));
							if (fieldname.toLowerCase().contains("select ")) {
								str_txt.append(")");
							}
							break;
						}
						if (i != counti) {
							// str_txt.append(" " + PadQuotes(request.getParameter(i + "_dr_filter")) + " ");
						}
					}
				}
			}
		}
		str_txt.append("");
		return str_txt.toString();
	}
	public String PopulateList(String Sql, String Fieldid, String Fieldname,
			String[] Selectid) {
		try {
			// SOPError("StrSql in PopulateList for "+Fieldid+" ===="+Sql);
			CachedRowSet crs = processQuery(Sql, 0);
			StringBuilder str = new StringBuilder();
			str.append("<select>");
			while (crs.next()) {
				str.append("<option value=").append(crs.getString(Fieldid)).append("");
				str.append(ArrSelectdrop(crs.getInt(Fieldid), Selectid));
				str.append(">").append(crs.getString(Fieldname)).append("</option> \n");
			}
			str.append("</select>");
			crs.close();
			return str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
}
