/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package axela.sales;

import java.io.IOException;

import cloudify.connect.Connect;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RadioCheckField;

/**
 *
 * @author Ajit
 */
public class CellField implements PdfPCellEvent {
	Connect ct = new Connect();
	public PdfFormField parent;
	public PdfWriter writer;
	public boolean checked;
	public String onValue;
	public int left = 0, bottom = 0, right = 0, top = 0;

	public CellField(PdfWriter writer, PdfFormField parent, boolean checked, String onValue, int left, int bottom, int right, int top) {
		this.writer = writer;
		this.parent = parent;
		this.checked = checked;
		this.onValue = onValue;
		this.left = left;
		this.bottom = bottom;
		this.right = right;
		this.top = top;
	}

	public void cellLayout(PdfPCell cell, Rectangle rect, PdfContentByte[] cb) {
		try {
			this.createCheckboxField(rect);
		} catch (Exception ex) {
			ct.SOPError("Axelaauto==" + this.getClass().getName());
			ct.SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void createCheckboxField(Rectangle rect) throws IOException, DocumentException {

		RadioCheckField bt = new RadioCheckField(this.writer, new RectangleReadOnly(rect.getLeft(this.left), rect.getBottom(this.bottom), rect.getRight(this.right), rect.getTop(this.top)), null,
				this.onValue);
		bt.setChecked(this.checked);
		bt.setBorderColor(GrayColor.GRAYBLACK);
		bt.setBackgroundColor(BaseColor.WHITE);
		bt.setCheckType(RadioCheckField.TYPE_CHECK);
		this.parent.addKid(bt.getCheckField());
	}
}
