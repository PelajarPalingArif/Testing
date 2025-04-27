import uk.org.okapibarcode.backend.QrCode;
import uk.org.okapibarcode.graphics.Color;
import uk.org.okapibarcode.output.Java2DRenderer;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;



public class Main {
    public static void main(String[] args) throws IOException {
        String contentPadding = "FINE SHYT FINE SHYT FINE SHYT FINE SHYT FINE SHYT FINE SHYT FINE SHYT FINE SHYT FINE SHYT FINE SHYT";
        String logoImagePath = "download.png";
        int offset = 20;
        int magnitude = 10;
        QrCode qrCode = new QrCode();
        qrCode.setContent("HAHAHAHHAHAHAHAHHAHHSAHASDA" + contentPadding);
        qrCode.setPreferredEccLevel(QrCode.EccLevel.H);

        int qrRealWidth = qrCode.getWidth() * magnitude;
        int qrRealHeight = qrCode.getHeight() * magnitude;
        BufferedImage image = new BufferedImage(qrRealWidth, qrRealHeight,
                                                BufferedImage.TYPE_INT_RGB
        );
        Graphics2D g2d = image.createGraphics();
        Java2DRenderer renderer = new Java2DRenderer(g2d, magnitude, Color.WHITE, Color.BLACK);
        renderer.render(qrCode);

        int padding = 30;
        BufferedImage resultImage = new BufferedImage(qrRealWidth + padding, qrRealHeight + padding,
                                                      BufferedImage.TYPE_INT_RGB
        );
        Graphics2D resultG2D = resultImage.createGraphics();
        resultG2D.setBackground(java.awt.Color.white);
        resultG2D.clearRect(0, 0, resultImage.getWidth(), resultImage.getHeight());
        resultG2D.drawImage(image, padding / 2, padding / 2, null);

        int allowedLogoWidth;
        int allowedLogoHeight;
        int percent;
        switch (qrCode.getPreferredEccLevel()) {
            case H: {
                percent = 25;
                allowedLogoWidth = (int) (percent / 100.0 * qrRealWidth);
                allowedLogoHeight = (int) (percent / 100.0 * qrRealHeight);
                break;
            }
            case Q: {
                percent = 20;
                allowedLogoWidth = (int) (percent / 100.0 * qrRealWidth);
                allowedLogoHeight = (int) (percent / 100.0 * qrRealHeight);
                break;
            }
            case M: {
                percent = 10;
                allowedLogoWidth = (int) (percent / 100.0 * qrRealWidth);
                allowedLogoHeight = (int) (percent / 100.0 * qrRealHeight);
                break;
            }
            case L: {
                percent = 7;
                allowedLogoWidth = (int) (percent / 100.0 * qrRealWidth);
                allowedLogoHeight = (int) (percent / 100.0 * qrRealHeight);
                break;
            }
            default: {
                percent = 0;
                allowedLogoWidth = 0;
                allowedLogoHeight = 0;
            }
        }
        int logoOffset = (int) (percent / 100.0 * allowedLogoWidth);
        System.out.println("alW : " + allowedLogoWidth);
        System.out.println("alH : " + allowedLogoHeight);
        System.out.println("qrRW : " + qrRealWidth);
        System.out.println("qrRH : " + qrRealHeight);

        try {
            if (allowedLogoWidth > 10 && allowedLogoHeight > 10) {
                BufferedImage logoImageCanvas = new BufferedImage(allowedLogoWidth,
                                                                  allowedLogoHeight,
                                                                  BufferedImage.TYPE_INT_RGB
                );
                Graphics2D logoG2D = logoImageCanvas.createGraphics();
                logoG2D.setColor(java.awt.Color.WHITE);
                logoG2D.fillRect(0, 0, allowedLogoWidth, allowedLogoHeight);
                File logoFile = new File(logoImagePath);
                BufferedImage logoImage = ImageIO.read(logoFile);
                logoG2D.drawImage(logoImage, logoOffset / 2, logoOffset / 2,
                                  allowedLogoWidth - logoOffset, allowedLogoHeight - logoOffset,
                                  null);
                resultG2D.drawImage(logoImageCanvas,
                                    resultImage.getWidth() / 2 - logoImageCanvas.getWidth() / 2,
                                    resultImage.getHeight() / 2 - logoImageCanvas.getHeight() / 2,
                                    null);
            }
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
        }
        ImageIO.write(resultImage, "png", new File("QRCode8.png"));
    }
}
