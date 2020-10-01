package axela.portal;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Thumbnail1 extends HttpServlet {

	private String Image = "";
	private String empphoto = "";
	private String itemimg = "";
	private String ImgWidth = "";
	private String noimg = "";
	public String complogo = "";
	public String jcphoto = "";
	public String comp_id = "0";
	public String gallery = "";
	private static final long serialVersionUID = -8285774993751841288L;

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		Connect ct = new Connect();
		try {
			// ct.CheckSession(req, res);
			HttpSession session = req.getSession(true);
			empphoto = ct.PadQuotes(req.getParameter("empphoto"));
			itemimg = ct.PadQuotes(req.getParameter("itemimg"));
			complogo = ct.PadQuotes(req.getParameter("complogo"));
			jcphoto = ct.PadQuotes(req.getParameter("jcphoto"));
			noimg = ct.PadQuotes(req.getParameter("noimg"));
			ImgWidth = ct.PadQuotes(req.getParameter("width"));
			gallery = ct.PadQuotes(req.getParameter("gallery"));
			String imageOutput = "png";

			if (!empphoto.equals("")) {
				Image = ct.ExeImgPath(comp_id) + empphoto;
			}

			if (!jcphoto.equals("")) {
				Image = ct.JobCardImgPath(comp_id) + jcphoto;
			}
			// if (!socialimg.equals("")) {
			// Image = ct.SocialImgPath() + socialimg;
			// }
			if (!itemimg.equals("")) {
				Image = ct.ItemImgPath(comp_id) + itemimg;
			}
			// if (!gallery.equals("")) {
			// Image = ct.GalleryImgPath() + gallery;
			// }

			if (!complogo.equals("")) {
				Image = ct.MediaPath() + "/template/logo.jpg";
			}
			if (noimg.equals("yes")) {
				Image = ct.MediaPath() + "/noimage/noimage.gif";
			}

			// if(catImg == null) catImg = "";
			// if(!catImg.equals(""))
			// {
			// Image = ct.CatImgPath()+catImg;
			// // SOP("Image=="+Image);
			// }

			// Set the mime type of the image
			if ("png".equals(imageOutput)) {
				res.setContentType("image/png");
			} else {
				res.setContentType("image/jpeg");
			}

			ImageIO.setUseCache(false);
			// Read the original image from the Server Location
			BufferedImage bufferedImage = ImageIO.read(new File(Image));

			// Calculate the target width and height
			float scale = 1;
			int targetWidth = 0;
			int targetHeight = 0;
			// String ImgWidth = "";
			ImgWidth = ct.PadQuotes(req.getParameter("width"));
			targetWidth = (int) (bufferedImage.getWidth(null) * scale);
			targetHeight = (int) (bufferedImage.getHeight(null) * scale);
			if (ImgWidth == null || ImgWidth.equals("")) {
				ImgWidth = "0";
			}
			if (targetWidth > Integer.parseInt(ImgWidth) && !ImgWidth.equals("0")) {
				targetHeight = Integer.parseInt(ImgWidth) * targetHeight / targetWidth;
				targetWidth = Integer.parseInt(ImgWidth);
			}
			// BufferedImage bufferedImage1=ImageIO.read(new File(Image));
			// SOP("patientlogo" + patientlogo);
			// ImageIO.write(createResizedCopy(bufferedImage, targetWidth, targetHeight), imageOutput, new File("D:\\exe\\ashish\\newImg\\"+patientlogo) );
			ImageIO.write(createResizedCopy(bufferedImage, targetWidth, targetHeight), imageOutput, res.getOutputStream());

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
		g.dispose();
		return scaledBI;
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doPost(req, res);
	}
}
