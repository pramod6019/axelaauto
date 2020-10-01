package axela.ws.axelaautoapp;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.imageio.ImageIO;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.Connect;

public class WS_Thumbnail extends Connect {

	public String StrSql = "";
	public String msg = "";
	public String comp_id = "";
	public String propprojectimg = "";
	public String width = "";
	public String Image = "";
	private String ImgWidth = "";
	public String emp_photo = "";
	JSONObject output = new JSONObject();
	ByteArrayOutputStream bao;
	// BufferedImage img = null;

	public byte[] imagedata(String emp_id, String comp_id, String image, String path, String width) throws Exception {
		try {
			ImgWidth = PadQuotes(width);
			String imageOutput = "png";
			bao = new ByteArrayOutputStream();
			if (!image.equals("")) {
				if (path.equals("complogo")) {
					Image = CompLogoPath() + image;
				}
				if (path.equals("branchlogo")) {
					Image = BranchLogoPath(comp_id) + image;
				}
				if (path.equals("userimg")) {
					Image = ExeImgPath(comp_id) + image;
				}
				if (path.equals("modelphoto")) {
					Image = ModelImgPath(comp_id) + image;
				}
				if (path.equals("featureimg")) {
					Image = FeatureImgPath(comp_id) + image;
				}
				if (path.equals("modelcoloursimg")) {
					Image = ModelColourImgPath(comp_id) + image;
				}
				if (path.equals("preownedimg")) {
					Image = PreownedImgPath(comp_id) + image;
				}
			}
			// SOP("Image====1111======" + Image);
			// Set the mime type of the image
			if ("png".equals(imageOutput)) {
				imageOutput = "png";
			} else {
				imageOutput = "jpeg";
			}
			// Read the original image from the Server Location
			if (new File(Image).exists()) {
				BufferedImage bufferedImage = ImageIO.read(new File(Image));
				// Calculate the target width and height
				float scale = 1;
				int targetWidth = 0;
				int targetHeight = 0;
				targetWidth = (int) (bufferedImage.getWidth(null) * scale);
				targetHeight = (int) (bufferedImage.getHeight(null) * scale);
				if (ImgWidth == null || ImgWidth.equals("")) {
					ImgWidth = targetWidth + "";
				}
				if (targetWidth > Integer.parseInt(ImgWidth) && !ImgWidth.equals("0")) {
					targetHeight = Integer.parseInt(ImgWidth) * targetHeight / targetWidth;
					targetWidth = Integer.parseInt(ImgWidth);
				}

				bao = new ByteArrayOutputStream();
				ImageIO.write(createResizedCopy(bufferedImage, targetWidth, targetHeight), imageOutput, bao);
				// ImageIO.write(createResizedCopy(bufferedImage, targetWidth,
				// targetHeight), imageOutput, res.getOutputStream());
			}
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return bao.toByteArray();
	}
	BufferedImage createResizedCopy(Image originalImage, int scaledWidth, int scaledHeight) {
		BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = scaledBI.createGraphics();
		g.setComposite(AlphaComposite.Src);
		g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
		g.dispose();
		return scaledBI;
	}
}
