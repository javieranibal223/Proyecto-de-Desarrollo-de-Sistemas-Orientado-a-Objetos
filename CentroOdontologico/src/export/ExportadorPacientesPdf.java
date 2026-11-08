package export;

import exception.ExportacionException;
import model.Paciente;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Genera un archivo PDF con el listado de pacientes en formato A4.
 * <p>
 * Usa Apache PDFBox 3.x. Si hay más filas que las que entran en una página,
 * continúa en páginas nuevas.
 */
public class ExportadorPacientesPdf {

    private static final String TITULO = "Listado de pacientes - Centro Odontológico";
    private static final float MARGEN = 50f;
    private static final float ALTO_FILA = 18f;
    private static final int FILAS_POR_PAGINA = 35;
    private static final float[] X_COLUMNAS = {MARGEN, 95f, 255f, 395f, 470f};

    /**
     * Exporta los pacientes indicados al archivo de destino.
     *
     * @param pacientes listado a exportar
     * @param destino   archivo PDF a crear (se sobrescribe si ya existe)
     * @throws ExportacionException si el archivo no puede escribirse
     */
    public void exportar(List<Paciente> pacientes, File destino) throws ExportacionException {
        try (PDDocument documento = new PDDocument()) {
            PDType1Font normal = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            PDType1Font negrita = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

            for (int desde = 0; desde == 0 || desde < pacientes.size(); desde += FILAS_POR_PAGINA) {
                int hasta = Math.min(desde + FILAS_POR_PAGINA, pacientes.size());
                PDPage pagina = new PDPage(PDRectangle.A4);
                documento.addPage(pagina);
                try (PDPageContentStream cs = new PDPageContentStream(documento, pagina)) {
                    escribirPagina(cs, pacientes.subList(desde, hasta), normal, negrita);
                }
            }
            documento.save(destino);
        } catch (IOException e) {
            throw new ExportacionException("No se pudo generar el PDF en " + destino.getAbsolutePath()
                    + ". Verifique que el archivo no esté abierto en otro programa.", e);
        }
    }

    private void escribirPagina(PDPageContentStream cs, List<Paciente> filas,
                                PDType1Font normal, PDType1Font negrita) throws IOException {
        float y = PDRectangle.A4.getHeight() - MARGEN;
        escribirTexto(cs, negrita, 14, MARGEN, y, TITULO);
        y -= 35;
        escribirFila(cs, negrita, y, "ID", "Apellido", "Nombre", "DNI", "Fecha de ingreso");
        for (Paciente p : filas) {
            y -= ALTO_FILA;
            escribirFila(cs, normal, y, String.valueOf(p.getId()), p.getApellido(),
                    p.getNombre(), p.getDni(), p.getFechaIngreso().format(Paciente.FORMATO_FECHA));
        }
    }

    private void escribirFila(PDPageContentStream cs, PDType1Font fuente, float y, String... celdas)
            throws IOException {
        for (int i = 0; i < celdas.length; i++) {
            escribirTexto(cs, fuente, 10, X_COLUMNAS[i], y, celdas[i]);
        }
    }

    private void escribirTexto(PDPageContentStream cs, PDType1Font fuente, float tamanio,
                               float x, float y, String texto) throws IOException {
        cs.beginText();
        cs.setFont(fuente, tamanio);
        cs.newLineAtOffset(x, y);
        cs.showText(texto);
        cs.endText();
    }
}
