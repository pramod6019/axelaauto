package cloudify.connect;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Captcha extends HttpServlet {

	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int width = 200;
		int height = 60;
		int fontSize = 35;
		int xGap = 30;
		int yGap = 25;
		String fontName = "Monotype Corsiva"; // test
		Color gradiantStartColor = new Color(60, 60, 60); // dark grey
		Color gradiantEndColor = new Color(140, 140, 140); // light grey
		Color textColor = new Color(255, 153, 0); // orange

		// String[] newData = {"hiworld", "orlando", "global", "publish", "looky"}; // you add more words or read them from db or something...

		BufferedImage bufferedImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		Graphics2D g2d = bufferedImage.createGraphics();

		RenderingHints rh = new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		rh.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);

		g2d.setRenderingHints(rh);

		GradientPaint gp = new GradientPaint(0, 0, gradiantStartColor, 0, height / 2, gradiantEndColor, true);

		g2d.setPaint(gp);
		g2d.fillRect(0, 0, width, height);

		Random r = new Random();

		// for (int i = 0; i < width - 10; i = i + 25){
		// int q = Math.abs(r.nextInt()) % width;
		// int colorIndex = Math.abs(r.nextInt()) % 200;
		// g2d.setColor(new Color(colorIndex, colorIndex, colorIndex));
		// g2d.drawLine(i, q, width, height);
		// g2d.drawLine(q, i, i, height);
		// }

		g2d.setColor(textColor);

		String captcha = generateCaptchaString();
		request.getSession().setAttribute("captcha", captcha);

		int x = 0;
		int y = 0;

		for (int i = 0; i < captcha.length(); i++) {
			Font font = new Font(fontName, Font.BOLD, fontSize);
			g2d.setFont(font);
			x += xGap + (Math.abs(r.nextInt()) % 15);
			y = yGap + Math.abs(r.nextInt()) % 20;

			g2d.drawChars(captcha.toCharArray(), i, 1, x, y);
		}

		for (int i = 0; i < width - 10; i = i + 25) {
			int p = Math.abs(r.nextInt()) % width;
			int q = Math.abs(r.nextInt()) % width;
			int colorIndex = Math.abs(r.nextInt()) % 200;
			g2d.setColor(new Color(colorIndex, colorIndex, colorIndex));
		}

		g2d.dispose();

		response.setContentType("image/png");
		OutputStream os = response.getOutputStream();
		ImageIO.write(bufferedImage, "png", os);
		os.close();
	}

	// Generate a CAPTCHA String consisting of random lowercase & uppercase letters, and numbecrs.
	public String generateCaptchaString() {
		Random generator = new Random();

		int length = 4;
		// 5 + (Math.abs(generator.nextInt()) % 3);

		StringBuilder captchaStringBuilder = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int baseCharNumber = Math.abs(generator.nextInt()) % 62;
			int charNumber = 0;
			if (baseCharNumber < 26) {
				charNumber = 65 + baseCharNumber;
			} else if (baseCharNumber < 52) {
				charNumber = 97 + (baseCharNumber - 26);
			} else {
				charNumber = 48 + (baseCharNumber - 52);
			}
			captchaStringBuilder.append((char) charNumber);
		}

		return captchaStringBuilder.toString();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}
}
