package axela.sales;
// manjur, May 2016
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.fill.JRAbstractLRUVirtualizer;
import net.sf.jasperreports.engine.fill.JRGzipVirtualizer;
import net.sf.jasperreports.engine.fill.JRSwapFileVirtualizer;
import net.sf.jasperreports.engine.util.JRSwapFile;
import cloudify.connect.Connect;

public class JasperReport extends Connect {
	private static final long serialVersionUID = 1L;
	public String reporttype = "", msg = "";
	public String StrHTML = "";
	public String format = "";
	public Map<String, Object> parameters = new HashMap<String, Object>();
	public List<Map> dataList = new ArrayList<Map>();
	String reportfrom = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JRException {
		HttpSession session = request.getSession(true);
		JasperPrint jasperPrint;
		JRGzipVirtualizer jrGzipVirtualizer = null;
		// reportfrom = PadQuotes(request.getParameter("dr_report"));
		// SOP("reportfrom===" + reportfrom);

		format = "pdf";
		try {

			if (msg.equals("")) {
				if (!dataList.isEmpty()) {
					if (jrGzipVirtualizer == null) {
						jrGzipVirtualizer = new JRGzipVirtualizer(100);
					}
					jrGzipVirtualizer.setReadOnly(true);
					parameters.put(JRParameter.REPORT_VIRTUALIZER, jrGzipVirtualizer);
					// String reportFile = JasperCompileManager.compileReportToFile(request.getServletContext().getRealPath("") + (reportfrom + ".jrxml"));
					String reportFile = request.getServletContext().getRealPath("") + (reportfrom + ".jasper");
					// SOP("reportFile==" + reportFile);
					JRAbstractLRUVirtualizer virtualizer;
					JRSwapFile swapFile = new JRSwapFile(TempPath(), 1024, 1024);
					virtualizer = new JRSwapFileVirtualizer(1, swapFile, true);
					parameters.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
					JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(dataList);
					if (format.equals("pdf")) {
						byte[] bytes = null;
						bytes = JasperRunManager.runReportToPdf(reportFile, parameters, jrBeanCollectionDataSource);
						response.setContentType("APPLICATION/pdf");
						response.setHeader("Content-Disposition", "Attachment; filename=\"" + reportfrom + ".pdf\"");
						response.setContentLength(bytes.length);
						ServletOutputStream servletOutputStream = response.getOutputStream();
						servletOutputStream.write(bytes, 0, bytes.length);
						servletOutputStream.flush();
						servletOutputStream.close();
					}
					// else if (format.equals("xlsx")) {
					// jasperPrint = JasperFillManager.fillReport(reportFile, parameters, jrBeanCollectionDataSource);
					// byte[] bytes = null;
					// ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					// JRXlsxExporter exporter = new JRXlsxExporter();
					// exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
					// exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
					// exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					// exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
					// exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					// exporter.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
					// exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
					// exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.TRUE);
					// exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.FALSE);
					// exporter.exportReport();
					// bytes = byteArrayOutputStream.toByteArray();
					// response.setContentType("application/x-ms-excel");
					// response.setHeader("Content-Disposition", "Attachment; filename=\"" + reportfrom + ".xlsx\"");
					// response.setContentLength(bytes.length);
					// ServletOutputStream servletOutputStream = response.getOutputStream();
					// servletOutputStream.write(bytes, 0, bytes.length);
					// servletOutputStream.flush();
					// servletOutputStream.close(); ///
					// }
				} else {
					StrHTML = "<br><center><font color=red><b>No Record(s) found!</b></font></center>";;
				}
			} else {
				StrHTML = "<br><center><font color=red><b>" + msg + "</b></font></center>";;
			}
			dataList.clear();
		} catch (Exception ex) {
			System.out.println("Axelaauto===" + this.getClass().getName());
			System.out.println("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}
}
