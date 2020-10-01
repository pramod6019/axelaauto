package axela.portal;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Thumbnail extends HttpServlet {

	private String Image = "";
	private String empphoto = "";
	private String branchlogo = "";
	private String itemimg = "";
	private String ImgWidth = "";
	private String noimg = "";
	public String complogo = "";
	public String jcphoto = "";
	public String jcimg = "";
	public String campaignimg = "";
	public String preownedimg = "";
	public String modelimg = "";
	public String featureimg = "";
	public String modelcoloursimg = "";
	public String comp_id = "0";
	public String gallery = "";
	private static final long serialVersionUID = -8285774993751841288L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connect ct = new Connect();
		try {
			HttpSession session = request.getSession(true);
			comp_id = ct.CNumeric(ct.GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				empphoto = ct.PadQuotes(request.getParameter("empphoto"));
				branchlogo = ct.PadQuotes(request.getParameter("branchlogo"));
				itemimg = ct.PadQuotes(request.getParameter("itemimg"));
				complogo = ct.PadQuotes(request.getParameter("complogo"));
				jcphoto = ct.PadQuotes(request.getParameter("jcphoto"));
				jcimg = ct.PadQuotes(request.getParameter("jcimg"));
				campaignimg = ct.PadQuotes(request.getParameter("campaignimg"));
				preownedimg = ct.PadQuotes(request.getParameter("preownedimg"));
				noimg = ct.PadQuotes(request.getParameter("noimg"));
				ImgWidth = ct.PadQuotes(request.getParameter("width"));
				gallery = ct.PadQuotes(request.getParameter("gallery"));
				modelimg = ct.PadQuotes(request.getParameter("modelimg"));
				featureimg = ct.PadQuotes(request.getParameter("featureimg"));
				modelcoloursimg = ct.PadQuotes(request.getParameter("modelcoloursimg"));
				// ct.SOP("modelcoloursimg====" + modelcoloursimg);
				String imageOutput = "jpg";
				if (!empphoto.equals("")) {
					Image = ct.ExeImgPath(comp_id) + empphoto;
				}
				if (!branchlogo.equals("")) {
					Image = ct.BranchLogoPath(comp_id) + branchlogo;
				}
				if (!jcimg.equals("")) {
					Image = ct.JobCardImgPath(comp_id) + jcimg;
				}
				if (!campaignimg.equals("")) {
					Image = ct.CampaignImgPath(comp_id) + campaignimg;
				}
				if (!preownedimg.equals("")) {
					Image = ct.PreownedImgPath(comp_id) + preownedimg;
				}
				if (!jcphoto.equals("")) {
					Image = ct.JobCardImgPath(comp_id) + jcphoto;
				}
				if (!itemimg.equals("")) {
					Image = ct.ItemImgPath(comp_id) + itemimg;
				}
				if (!modelimg.equals("")) {
					Image = ct.ModelImgPath(comp_id) + modelimg;
				}
				if (!featureimg.equals("")) {
					Image = ct.FeatureImgPath(comp_id) + featureimg;
					// ct.SOP("Image----featureimg-----" + Image);
				}
				if (!modelcoloursimg.equals("")) {
					Image = ct.ModelColourImgPath(comp_id) + modelcoloursimg;
					// ct.SOP("Image----coloursim-----" + Image);
				}
				if (!complogo.equals("")) {
					Image = ct.CompLogoPath() + complogo;
				}
				if (noimg.equals("yes")) {
					Image = ct.MediaPath() + "/noimage/noimage.gif";
				}
				if ("png".equals(imageOutput)) {
					response.setContentType("image/png");
				} else {
					response.setContentType("image/jpeg");
				}
				int seconds = 57600;
				Calendar cal = new GregorianCalendar();
				cal.roll(Calendar.SECOND, seconds);
				response.setHeader("Expires", htmlExpiresDateFormat().format(cal.getTime()));
				response.setHeader("Cache-Control", "PUBLIC, max-age=" + seconds + ", must-revalidate");

				// ImageIO.setUseCache(false);
				// Read the original image from the Server Location
				if (new File(Image).exists()) {
					// ct.SOPInfo("empfile====" + new File(Image));
					BufferedImage bufferedImage = ImageIO.read(new File(Image));
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
					// BufferedImage bufferedImage1=ImageIO.read(new
					// File(Image));
					// bufferedImage=ImageIO.read(new File(Image));
					// SOP("patientlogo" + patientlogo);
					// ImageIO.write(createResizedCopy(bufferedImage,
					// targetWidth, targetHeight), imageOutput, new
					// File("D:\\exe\\ashish\\newImg\\"+patientlogo) );
					ImageIO.write(createResizedCopy(bufferedImage, targetWidth, targetHeight), imageOutput, response.getOutputStream());

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
		// g.createHeadlessSmoothBufferedImage(originalImage, 0, scaledWidth,
		// scaledHeight, null);
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
