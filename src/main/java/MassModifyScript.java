import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.*;
import java.nio.charset.StandardCharsets;

public class MassModifyScript {
    public static void main(String[] args) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader("sheet.pdf"), new PdfWriter("modifiedScript.pdf"));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, false);
        String fieldName = "humanity";
        for (int i = 1; i <= 6; i++) {
            PdfFormField field = form.getField(fieldName + i);
            System.out.println("modyfikacja pola " + fieldName + i);
            if (field != null) {
                PdfArray kids = field.getKids();
                PdfDictionary standardAction;
                if (kids == null) {
                    standardAction = field.getPdfObject().getAsDictionary(PdfName.A);
                } else {
                    PdfDictionary widget = kids.getAsDictionary(0);
                    standardAction = widget.getAsDictionary(PdfName.A);
                }
                PdfStream jsStream = (PdfStream) standardAction.get(PdfName.JS);
                String jsCode = new String(jsStream.getBytes(), StandardCharsets.UTF_8);
                if (jsCode.contains("majorDMG.display = display.visible;") && field.getFieldName().toString().startsWith("humanity")) {
                    jsCode = jsCode.replace("majorDMG.display = display.visible;\n} else if (majorDMG.display === display.visible) {\n    majorDMG.display = display.hidden;\n    ", "")
                            .replace("majorDMG.display = display.visible;\r\n} else if (majorDMG.display === display.visible) {\r\n    majorDMG.display = display.hidden;\r\n    ", "")
                            .replace("majorDMG.display = display.visible;\r} else if (majorDMG.display === display.visible) {\r    majorDMG.display = display.hidden;\r    ", "");
                    jsStream.setData(jsCode.getBytes(StandardCharsets.UTF_8));
                }
            }
        }
        pdfDoc.close();
    }
}