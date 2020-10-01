package axela.portal;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ThumbnailBlob extends HttpServlet {

	public String campaignimg = "";
	private String ImgWidth = "";
	public String complogo = "";
	public String img_id = "0";
	public String project_id = "0";
	public String img_type = "";
	public String img_type1 = "";
	public String img_type2 = "";
	public String unit_id = "0";
	public String emp_id = "0";
	public String item_id = "0";
	public String featureimg = "";
	public String modelcoloursimg = "";
	public String comp_id = "0";
	public String profile_id = "0";
	public String testdrive_id = "0";
	public String transblockalbum_id = "0", veh_id = "0";
	public String gallery = "";
	public String StrSql = "";
	public Connection conn = null;
	public Statement stmt = null;
	public ResultSet rs = null;
	CachedRowSet crs = null;
	public byte[] data = null;
	public Connect ct = new Connect();
	private static final long serialVersionUID = -8285774993751841288L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			comp_id = ct.CNumeric(ct.GetSession("comp_id", request));
			if (!comp_id.equals("0")) {

				testdrive_id = ct.CNumeric(ct.PadQuotes(request.getParameter("testdrive_id")));
				veh_id = ct.CNumeric(ct.PadQuotes(request.getParameter("veh_id")));

				// ct.SOP("testdrive_id" + testdrive_id);
				img_type = ct.PadQuotes(request.getParameter("image_type"));
				// ct.SOPInfo("img_type===" + img_type);
				ImgWidth = ct.PadQuotes(request.getParameter("width"));
				gallery = ct.PadQuotes(request.getParameter("gallery"));
				featureimg = ct.PadQuotes(request.getParameter("featureimg"));
				modelcoloursimg = ct.PadQuotes(request.getParameter("modelcoloursimg"));
				String imageOutput = "png";

				if (!img_type.equals("") && img_type.equals("testdrive")) {
					StrSql = "SELECT testdrive_doc_data  FROM " + ct.compdb(comp_id) + "axela_sales_testdrive WHERE testdrive_id = " + testdrive_id;
					// ct.SOPInfo("StrSql======s" + StrSql);
					if (StrSql != "" && !StrSql.equals(null)) {
						crs = ct.processQuery(StrSql, 0);
						if (crs.isBeforeFirst()) {
							while (crs.next()) {
								data = crs.getBytes("testdrive_doc_data");
								// ct.SOP("data==testdrive===" + data);
							}
						}
					}
				}

				// preowned testdrive
				if (!img_type.equals("") && img_type.equals("preownedtestdrive")) {
					StrSql = "SELECT testdrive_doc_data  FROM " + ct.compdb(comp_id) + "axela_preowned_testdrive WHERE testdrive_id = " + testdrive_id;
					// ct.SOPInfo("StrSql======s" + StrSql);
					if (StrSql != "" && !StrSql.equals(null)) {
						crs = ct.processQuery(StrSql, 0);
						if (crs.isBeforeFirst()) {
							while (crs.next()) {
								data = crs.getBytes("testdrive_doc_data");
								// ct.SOP("data==testdrive===" + data);
							}
						}
					}
				}

				// For vehicle in
				if (!img_type.equals("") && img_type.equals("vehiclein")) {
					StrSql = "SELECT insurimg_data  FROM " + ct.compdb(comp_id) + "axela_service_veh_insur_img WHERE insurimg_veh_id = " + veh_id;
					// ct.SOPInfo("StrSql======s" + StrSql);
					if (StrSql != "" && !StrSql.equals(null)) {
						crs = ct.processQuery(StrSql, 0);
						if (crs.isBeforeFirst()) {
							while (crs.next()) {
								data = crs.getBytes("insurimg_data");
								// ct.SOP("data==testdrive===" + data);
							}
						}
					}
				}

				// Set the mime type of the image

				if (data != null) {
					// ct.SOPInfo("before bufferedImage reader===" + data);
					BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(data));
					// ct.SOPInfo("bufferedImage======" + bufferedImage);
					if (bufferedImage != null) {
						// Calculate the target width and height
						float scale = 1;
						int targetWidth = 0;
						int targetHeight = 0;
						ImgWidth = ct.PadQuotes(request.getParameter("width"));
						targetWidth = (int) (bufferedImage.getWidth(null) * scale);
						targetHeight = (int) (bufferedImage.getHeight(null) * scale);
						// ct.SOP("ImgWidth===" + ImgWidth);
						// ct.SOP("targetWidth===" + targetWidth);
						// ct.SOP("targetHeight===" + targetHeight);
						if (ImgWidth == null || ImgWidth.equals("")) {
							ImgWidth = targetWidth + "";
						}
						if (targetWidth > Integer.parseInt(ImgWidth) && !ImgWidth.equals("0")) {
							targetHeight = Integer.parseInt(ImgWidth) * targetHeight / targetWidth;
							targetWidth = Integer.parseInt(ImgWidth);
						}
						// ct.SOP("kskskkk==========" + createResizedCopy(bufferedImage, targetWidth, targetHeight).TYPE_BYTE_BINARY);
						ImageIO.write(createResizedCopy(bufferedImage, targetWidth, targetHeight), imageOutput, response.getOutputStream());
					}

				}
			}
		} catch (Exception ex) {
			ct.SOPError("Axelaauto===" + this.getClass().getName());
			ct.SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
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

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public static DateFormat htmlExpiresDateFormat() {
		DateFormat httpDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
		httpDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		return httpDateFormat;
	}

}
