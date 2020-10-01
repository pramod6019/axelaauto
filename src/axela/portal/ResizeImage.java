package axela.portal;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import org.apache.commons.io.output.ByteArrayOutputStream;

import cloudify.connect.Connect;

public class ResizeImage extends Connect {

	private String testdrive_doc_value;
	public String LinkHeader = "";
	public String add = "";
	public String update = "";
	public String addB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	private String ImgWidth = "";
	public String comp_id = "0";
	public BufferedImage bufferedImage = null;
	public String testdrive_id = "0";
	public InputStream inputStream = null;
	public java.sql.PreparedStatement psmt = null;
	public ResultSet rs = null;
	public Statement stmt = null;
	public byte[] data = null;
	public Connection conn = null;
	HttpServletRequest request;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				msg = PadQuotes(request.getParameter("msg"));
				if (addB.equals("Add Resize")) {
					PopulateblobData(response);
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, Exception {
		doPost(request, response);
	}
	BufferedImage createResizedCopy(Image originalImage, int scaledWidth, int scaledHeight) {
		BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = scaledBI.createGraphics();
		g.setComposite(AlphaComposite.Src);
		g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
		// g.createHeadlessSmoothBufferedImage(originalImage, 0, scaledWidth, scaledHeight, null);
		g.dispose();
		return scaledBI;
	}

	protected void updateData(String testdrive_id, InputStream inputstream) throws Exception {

		// int count = 0;
		if (msg.equals("")) {
			try {
				int count = 0;
				if (!testdrive_id.equals("0")) {
					StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_testdrive"
							+ " SET";
					if (inputStream.available() != 0) {
						StrSql += " testdrive_doc_data = ?";

					}
					StrSql += " WHERE testdrive_id = ?";
					conn = null;
					conn = connectDB();
					psmt = conn.prepareStatement(StrSql);
					if (inputStream.available() != 0) {
						psmt.setBlob(1, inputStream);
					}
					psmt.setString(2, testdrive_id);
					// SOP("StrSql====" + StrSql);
					psmt.executeUpdate();
				}

			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			} finally {
				try {
					if (psmt != null) {
						psmt.close();
					}
					if (stmt != null) {
						stmt.close();
					}
					if (conn != null) {
						conn.close();
					}

				} catch (Exception ex) {
					SOPError(this.getClass().getName());
					SOPError(new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
				}

			}
		}
	}

	protected void PopulateblobData(HttpServletResponse response) {
		try {
			StrSql = "SELECT testdrive_id, testdrive_doc_data, testdrive_doc_value"
					+ " FROM " + compdb(comp_id) + "axela_sales_testdrive"
					+ " WHERE testdrive_doc_data IS NOT NULL"
					+ " ORDER BY"
					+ " OCTET_LENGTH(testdrive_doc_data) DESC";
			// SOP("StrSql-PopulateConfigDetails--" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {

					testdrive_id = crs.getString("testdrive_id");
					testdrive_doc_value = crs.getString("testdrive_doc_value");
					data = crs.getBytes("testdrive_doc_data");
					if (data != null) {
						// SOP("data====before======" + data.length);
						bufferedImage = ImageIO.read(new ByteArrayInputStream(data));
						if (bufferedImage != null) {
							// Calculate the target width and height
							float scale = 1;
							int targetWidth = 0;
							int targetHeight = 0;
							ImgWidth = PadQuotes("1024");
							targetWidth = (int) (bufferedImage.getWidth(null) * scale);
							targetHeight = (int) (bufferedImage.getHeight(null) * scale);
							if (ImgWidth == null || ImgWidth.equals("")) {
								ImgWidth = targetWidth + "";
							}
							if (targetWidth > Integer.parseInt(ImgWidth) && !ImgWidth.equals("0")) {
								targetHeight = Integer.parseInt(ImgWidth) * targetHeight / targetWidth;
								targetWidth = Integer.parseInt(ImgWidth);
							}
							bufferedImage = createResizedCopy(bufferedImage, targetWidth, targetHeight);
							ByteArrayOutputStream bios = new ByteArrayOutputStream();
							ImageIO.write(bufferedImage, fileext(testdrive_doc_value).substring(1), bios);
							inputStream = new ByteArrayInputStream(bios.toByteArray());
							// SOP("inputStream===after===" + inputStream.available());
							updateData(testdrive_id, inputStream);
						}

					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Resize" + msg));
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in: " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
