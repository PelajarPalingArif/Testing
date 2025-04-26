import uk.org.okapibarcode.backend.QrCode;
import uk.org.okapibarcode.graphics.Color;
import uk.org.okapibarcode.output.Java2DRenderer;


import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class QRTesting {
    public static void main(String[] args) throws IOException {
        int offset = 20;
        int magnitude = 4;
        QrCode qrCode = new QrCode();
        qrCode.setFontName("Monospaced");
        qrCode.setContent("HAHAHAHHAHAHAHAHHAHHSAHASDA");

        int qrRealWidth = qrCode.getWidth() * magnitude;
        int qrRealHeight = qrCode.getHeight() * magnitude;
        BufferedImage image = new BufferedImage(qrRealWidth,qrRealHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2d = image.createGraphics();
        Java2DRenderer renderer = new Java2DRenderer(g2d, magnitude, Color.WHITE, Color.BLACK);
        renderer.render(qrCode);


        ImageIO.write(image, "png", new File("QRCode6.png"));
    }

}
