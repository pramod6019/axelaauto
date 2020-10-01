<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.accounting.Report_Sales_Profitability" scope="request" />
<%
	mybean.doPost(request, response);
%>
<%
	if (!mybean.header.equals("no")) {
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
<!-- <link href="../assets/dropjones/fixedHeader.dataTables.min.css" rel="stylesheet" type="text/css" /> -->
<!-- <link href="../assets/dropjones/jquery.dataTables.min.css" rel="stylesheet" type="text/css" /> -->
</HEAD>

<body class="page-container-bg-solid page-header-menu-fixed" onload="populateAsterisk();">
	<%@include file="../portal/header.jsp"%>

	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>Report Sales Profitability</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY ----->
			<div class="page-content">
				<div class="page-content-inner">
					<div class="container-fluid">
						<ul class="page-breadcrumb breadcrumb">
							<li><a href="../portal/home.jsp">Home</a> &gt;</li>
							<li><a href="../portal/mis.jsp">MIS</a> &gt;</li>
							<li><a href="../accounting/report-sales-profitability.jsp">
									Report Sales Profitability</a><b>:</b></li>
						</ul>
						<!-- END PAGE BREADCRUMBS -->
						<center>
							<font color="red"><b> <%=mybean.msg%></b></font>
						</center>

						<div class="tab-pane" id="">

							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										Report Sales Profitability
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<!-- START PORTLET BODY -->
									<form method="post" name="frm1" id="frm1" class="form-horizontal">

										<div class="form-element4">
											<label>Start Date<font color="red">*</font>: </label>
											<input name="txt_starttime" id="txt_starttime" type="text"
												value="<%=mybean.start_time%>" class="form-control datepicker"  />
										</div>

										<div class="form-element4">
											<label>End Date<font color="red">*</font>: </label>
											<input name="txt_endtime" id="txt_endtime" type="text" 
												value="<%=mybean.end_time%>" class="form-control datepicker" />
										</div>

										<div class="form-element4">
											<label>Total By: </label>
											<select name="dr_totalby" class="form-control" id="dr_totalby" onchange="populateAsterisk();">
												<%=mybean.PopulateTotalBy(mybean.comp_id)%>
											</select>
										</div>

										<!--end -->

										<div class="form-element2">
											<label>Brands<span id="star"><font color="#ff0000">*</font></span>:</label>
											<div>
												<select name="dr_principal" size="10" multiple="multiple"
													class="form-control multiselect-dropdown" id="dr_principal"
													onChange="PopulateBranches();PopulateModels();PopulateRegion();">
													<%=mybean.mischeck.PopulatePrincipal(mybean.brand_ids, mybean.comp_id, request)%>
												</select>
											</div>
										</div>

										<div class="form-element2">
											<label>Regions:</label>
											<div id="regionHint">
												<%=mybean.mischeck.PopulateRegion(mybean.brand_id, mybean.region_ids, mybean.comp_id, request)%>
											</div>
										</div>

										<div class="form-element2">
											<label>Branches:</label>
											<div id="branchHint">
												<%=mybean.mischeck.PopulateBranches(mybean.brand_id, mybean.region_id, mybean.branch_ids, mybean.comp_id, request)%>
											</div>
										</div>


										<div class="form-element2">
											<label>Model:</label>
											<div id="modelHint">
												<%=mybean.mischeck.PopulateModels(mybean.brand_id, mybean.model_ids, mybean.comp_id, request)%>
											</div>
										</div>

										<div class="form-element2">
											<label>Variant:</label>
											<div id="itemHint">
												<%=mybean.mischeck.PopulateVariants(mybean.model_id, mybean.item_ids, mybean.comp_id, request)%>
											</div>
										</div>

										<div class="form-element2">
											<label>Fuel Type:</label>
											<div>
												<%=mybean.PopulateFuelType(mybean.comp_id, request)%>
											</div>
										</div>

										<div class="form-element2">
											<label>Teams:</label>
											<div id="teamHint">
												<%=mybean.mischeck.PopulateTeams(mybean.branch_id, mybean.team_ids, mybean.comp_id, request)%>
											</div>
										</div>

										<div class="form-element2">
											<label>Sales Consultant:</label>
											<div id="exeHint">
												<%=mybean.mischeck.PopulateSalesExecutives(mybean.team_id, mybean.exe_ids, mybean.comp_id, request)%>
											</div>
										</div>
										
										<div class="form-element2">
											<label>Data Fields:</label>
											<div id="">
												<%=mybean.PopulateDataFields(mybean.dr_datafields)%>
												</div>
										</div>
										
											<div class="form-element2" style="top: 27px;">
												<label>Include Inactive Executive: </label>
												<input type="checkbox" id="chk_include_inactive_exe" name="chk_include_inactive_exe"  <%=mybean.PopulateCheck(mybean.include_inactive_exe) %> "/>
											</div>
<div class="row"></div>
										<div class="form-element12" align="center">
											<input type="submit" name="submit_button" id="submit_button"
												class="btn btn-success" value="Go" />
											<input type="hidden" name="submit_button" value="Submit" />
										</div>

									</form>
								</div>
							</div>
						</div>
						<% if (!mybean.StrHTML.equals("")) { %>
<%-- 						<%if(mybean.emp_copy_access.equals("1")){ %> --%>
						<center><button id="btnExport" >Export</button></center>
<%-- 						<%} %> --%>
<div id="tableWrap">
							<center><%=mybean.StrHTML%></center>
							</div>
						<% } %>
					</div>
				</div>
			</div>
		</div>
	</div>

	</div>
	<!-- END CONTAINER -->
	<%
		}
	%>
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
	<script language="JavaScript" type="text/javascript">
		function PopulateRegion() { //v1.0
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			//alert("111111------"+brand_id);
			showHint('../sales/mis-check1.jsp?multiple=yes&brand_id=' + brand_id + '&region=yes', 'regionHint');
		}

		function PopulateBranches() { //v1.0
			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			var region_id = outputSelected(document.getElementById("dr_region").options);
			//alert("222------------"+region_id);
			showHint('../sales/mis-check1.jsp?multiple=yes&brand_id='
					+ brand_id + '&region_id=' + region_id + '&branch=yes', 'branchHint');
		}

		function PopulateModels() { //v1.0

			var brand_id = outputSelected(document.getElementById("dr_principal").options);
			//alert("333------"+brand_id);
			showHint('../sales/mis-check1.jsp?brand_id=' + brand_id + '&model=yes', 'modelHint');
		}

		function PopulateTeams() {
			var branch_id = outputSelected(document.getElementById("dr_branch").options);
			showHint('../sales/mis-check1.jsp?branch_id=' + branch_id + '&team=yes', 'teamHint');
		}

		function PopulateExecutives() { //v1.0
			var branch_id = outputSelected(document.getElementById("dr_branch").options);
			var team_id = outputSelected(document.getElementById("dr_team").options);
			showHint('../sales/mis-check1.jsp?exe_branch_id=' + branch_id
					+ '&team_id=' + team_id + '&executives=yes', 'exeHint');
		}

		function PopulateVariants() { //v1.0
			var model_id = outputSelected(document.getElementById("dr_model").options);
			//alert("333------"+brand_id);
			showHint('../sales/mis-check1.jsp?model_id=' + model_id + '&item=yes', 'itemHint');
		}
		function PopulateCRMDays(){
		}
		// if feedback type contactable sataisfied or 
		// dissatisfied is mandatory
		function populateAsterisk() {
			var totalby = document.getElementById("dr_totalby").value;
			if (totalby == 7) {
				$('#star').show();
			} else {
				$('#star').hide();
			}
		}
		
// 		$(document).ready(function(){
// 			$('#table').DataTable({
// 				fixedHeader:true
// 			});
// 		});
		
		
	</script>
	
	<script type="text/javascript">
$(function(){
    $('#btnExport').click(function(){
//     	alert(1);
        var url='data:application/vnd.ms-excel,' + encodeURIComponent($('#tableWrap').html()) 
        location.href=url
        return false
    })
})
</script>
	<script>
	/*
	*  jQuery table2excel - v1.1.1
	*  jQuery plugin to export an .xls file in browser from an HTML table
	*  https://github.com/rainabba/jquery-table2excel
	*
	*  Made by rainabba
	*  Under MIT License
	*/
	//table2excel.js
	; (function ($, window, document, undefined) {
	    var pluginName = "table2excel",

	    defaults = {
	        exclude: ".noExl",
	        name: "Table2Excel",
	        filename: "table2excel",
	        fileext: ".xls",
	        exclude_img: true,
	        exclude_links: true,
	        exclude_inputs: true
	    };

	    // The actual plugin constructor
	    function Plugin(element, options) {
	        this.element = element;
	        // jQuery has an extend method which merges the contents of two or
	        // more objects, storing the result in the first object. The first object
	        // is generally empty as we don't want to alter the default options for
	        // future instances of the plugin
	        //
	        this.settings = $.extend({}, defaults, options);
	        this._defaults = defaults;
	        this._name = pluginName;
	        this.init();
	    }

	    Plugin.prototype = {
	        init: function () {
	            var e = this;

	            var utf8Heading = "<meta http-equiv=\"content-type\" content=\"application/vnd.ms-excel; charset=UTF-8\">";
	            e.template = {
	                head: "<html xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:x=\"urn:schemas-microsoft-com:office:excel\" xmlns=\"http://www.w3.org/TR/REC-html40\">" + utf8Heading + "<head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets>",
	                sheet: {
	                    head: "<x:ExcelWorksheet><x:Name>",
	                    tail: "</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet>"
	                },
	                mid: "</x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head><body>",
	                table: {
	                    head: "<table>",
	                    tail: "</table>"
	                },
	                foot: "</body></html>"
	            };

	            e.tableRows = [];

	            // get contents of table except for exclude
	            $(e.element).each(function (i, o) {
	                var tempRows = "";
	                $(o).find("tr").not(e.settings.exclude).each(function (i, p) {

	                    tempRows += "<tr>";
	                    $(p).find("td,th").not(e.settings.exclude).each(function (i, q) { // p did not exist, I corrected

	                        var rc = {
	                            rows: $(this).attr("rowspan"),
	                            cols: $(this).attr("colspan"),
	                            flag: $(q).find(e.settings.exclude)
	                        };

	                        if (rc.flag.length > 0) {
	                            tempRows += "<td> </td>"; // exclude it!!
	                        } else {
	                            if (rc.rows & rc.cols) {
	                                tempRows += "<td>" + $(q).html() + "</td>";
	                            } else {
	                                tempRows += "<td";
	                                if (rc.rows > 0) {
	                                    tempRows += " rowspan=\'" + rc.rows + "\' ";
	                                }
	                                if (rc.cols > 0) {
	                                    tempRows += " colspan=\'" + rc.cols + "\' ";
	                                }
	                                tempRows += "/>" + $(q).html() + "</td>";
	                            }
	                        }
	                    });

	                    tempRows += "</tr>";
	                    console.log(tempRows);

	                });
	                // exclude img tags
	                if (e.settings.exclude_img) {
	                    tempRows = exclude_img(tempRows);
	                }

	                // exclude link tags
	                if (e.settings.exclude_links) {
	                    tempRows = exclude_links(tempRows);
	                }

	                // exclude input tags
	                if (e.settings.exclude_inputs) {
	                    tempRows = exclude_inputs(tempRows);
	                }
	                e.tableRows.push(tempRows);
	            });

	            e.tableToExcel(e.tableRows, e.settings.name, e.settings.sheetName);
	        },

	        tableToExcel: function (table, name, sheetName) {
	            var e = this, fullTemplate = "", i, link, a;

	            e.format = function (s, c) {
	                return s.replace(/{(\w+)}/g, function (m, p) {
	                    return c[p];
	                });
	            };

	            sheetName = typeof sheetName === "undefined" ? "Sheet" : sheetName;

	            e.ctx = {
	                worksheet: name || "Worksheet",
	                table: table,
	                sheetName: sheetName
	            };

	            fullTemplate = e.template.head;

	            if ($.isArray(table)) {
	                for (i in table) {
	                    //fullTemplate += e.template.sheet.head + "{worksheet" + i + "}" + e.template.sheet.tail;
	                    fullTemplate += e.template.sheet.head + sheetName + i + e.template.sheet.tail;
	                }
	            }

	            fullTemplate += e.template.mid;

	            if ($.isArray(table)) {
	                for (i in table) {
	                    fullTemplate += e.template.table.head + "{table" + i + "}" + e.template.table.tail;
	                }
	            }

	            fullTemplate += e.template.foot;

	            for (i in table) {
	                e.ctx["table" + i] = table[i];
	            }
	            delete e.ctx.table;

	            var isIE = /*@cc_on!@*/false || !!document.documentMode; // this works with IE10 and IE11 both :)            
	            //if (typeof msie !== "undefined" && msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./))      // this works ONLY with IE 11!!!
	            if (isIE) {
	                if (typeof Blob !== "undefined") {
	                    //use blobs if we can
	                    fullTemplate = e.format(fullTemplate, e.ctx); // with this, works with IE
	                    fullTemplate = [fullTemplate];
	                    //convert to array
	                    var blob1 = new Blob(fullTemplate, { type: "text/html" });
	                    window.navigator.msSaveBlob(blob1, getFileName(e.settings));
	                } else {
	                    //otherwise use the iframe and save
	                    //requires a blank iframe on page called txtArea1
	                    txtArea1.document.open("text/html", "replace");
	                    txtArea1.document.write(e.format(fullTemplate, e.ctx));
	                    txtArea1.document.close();
	                    txtArea1.focus();
	                    sa = txtArea1.document.execCommand("SaveAs", true, getFileName(e.settings));
	                }

	            } else {
	                var blob = new Blob([e.format(fullTemplate, e.ctx)], { type: "application/vnd.ms-excel" });
	                window.URL = window.URL || window.webkitURL;
	                link = window.URL.createObjectURL(blob);
	                a = document.createElement("a");
	                a.download = getFileName(e.settings);
	                a.href = link;

	                document.body.appendChild(a);

	                a.click();

	                document.body.removeChild(a);
	            }

	            return true;
	        }
	    };

	    function getFileName(settings) {
	        return (settings.filename ? settings.filename : "table2excel");
	    }

	    // Removes all img tags
	    function exclude_img(string) {
	        var _patt = /(\s+alt\s*=\s*"([^"]*)"|\s+alt\s*=\s*'([^']*)')/i;
	        return string.replace(/<img[^>]*>/gi, function myFunction(x) {
	            var res = _patt.exec(x);
	            if (res !== null && res.length >= 2) {
	                return res[2];
	            } else {
	                return "";
	            }
	        });
	    }

	    // Removes all link tags
	    function exclude_links(string) {
	        return string.replace(/<a[^>]*>|<\/a>/gi, "");
	    }

	    // Removes input params
	    function exclude_inputs(string) {
	        var _patt = /(\s+value\s*=\s*"([^"]*)"|\s+value\s*=\s*'([^']*)')/i;
	        return string.replace(/<input[^>]*>|<\/input>/gi, function myFunction(x) {
	            var res = _patt.exec(x);
	            if (res !== null && res.length >= 2) {
	                return res[2];
	            } else {
	                return "";
	            }
	        });
	    }

	    $.fn[pluginName] = function (options) {
	        var e = this;
	        e.each(function () {
	            if (!$.data(e, "plugin_" + pluginName)) {
	                $.data(e, "plugin_" + pluginName, new Plugin(this, options));
	            }
	        });

	        // chain jQuery functions
	        return e;
	    };

	})(jQuery, window, document);

</script>
	
	
	
</body>
</HTML>
