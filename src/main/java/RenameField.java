import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;

public class RenameField {
    public static void main(String[] args) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader("sheet.pdf"), new PdfWriter("renamedSheet.pdf"));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        String from;
        String to;
        from = "adflaw1_";
        to = "adflaw7_";
        for (int i = 1; i <= 5; i++) {
            copyField(form, pdfDoc, from+i+"dot", to+i+"dot");
            copyField(form, pdfDoc, from+i, to+i);
        }
        copyField(form, pdfDoc, from+"clear", to+"clear");
        from = "adflaw2_";
        to = "adflaw8_";
        for (int i = 1; i <= 5; i++) {
            copyField(form, pdfDoc, from+i+"dot", to+i+"dot");
            copyField(form, pdfDoc, from+i, to+i);
        }
        copyField(form, pdfDoc, from+"clear", to+"clear");
        from = "adflaw3_";
        to = "adflaw9_";
        for (int i = 1; i <= 5; i++) {
            copyField(form, pdfDoc, from+i+"dot", to+i+"dot");
            copyField(form, pdfDoc, from+i, to+i);
        }
        copyField(form, pdfDoc, from+"clear", to+"clear");
        from = "adflaw4_";
        to = "adflaw10_";
        for (int i = 1; i <= 5; i++) {
            copyField(form, pdfDoc, from+i+"dot", to+i+"dot");
            copyField(form, pdfDoc, from+i, to+i);
        }
        copyField(form, pdfDoc, from+"clear", to+"clear");
        from = "adflaw5_";
        to = "adflaw11_";
        for (int i = 1; i <= 5; i++) {
            copyField(form, pdfDoc, from+i+"dot", to+i+"dot");
            copyField(form, pdfDoc, from+i, to+i);
        }
        copyField(form, pdfDoc, from+"clear", to+"clear");
        pdfDoc.close();
    }
    private static void copyField(PdfAcroForm form, PdfDocument pdfDoc, String oldName, String newName) {
        PdfFormField originalField = form.getField(oldName);
        PdfDictionary originalDict = originalField.getPdfObject();

        PdfDictionary clonedDict = new PdfDictionary();
        for (PdfName key : originalDict.keySet()) {
            clonedDict.put(key, originalDict.get(key));
        }
        clonedDict.remove(PdfName.Parent);
        clonedDict.remove(PdfName.Kids);
        clonedDict.put(PdfName.T, new PdfString(newName));
        clonedDict.makeIndirect(pdfDoc);

        PdfArray originalKids = originalDict.getAsArray(PdfName.Kids);
        PdfDictionary originalWidget = originalKids.getAsDictionary(1);

        PdfDictionary clonedWidget = new PdfDictionary();
        for (PdfName key : originalWidget.keySet()) {
            clonedWidget.put(key, originalWidget.get(key));
        }
        clonedWidget.put(PdfName.Parent, clonedDict);
        clonedWidget.makeIndirect(pdfDoc);

        PdfArray clonedKids = new PdfArray();
        clonedKids.add(clonedWidget);
        clonedDict.put(PdfName.Kids, clonedKids);

        PdfFormField newField = PdfFormField.makeFormField(clonedDict, pdfDoc);
        form.addField(newField);

        PdfArray kids = originalField.getKids();
        PdfDictionary secondWidget = kids.getAsDictionary(1);
        kids.remove(1);

        PdfDictionary pageDict = secondWidget.getAsDictionary(PdfName.P);
        PdfPage page = pdfDoc.getPage(pageDict);
        page.removeAnnotation(PdfAnnotation.makeAnnotation(secondWidget));
    }
}