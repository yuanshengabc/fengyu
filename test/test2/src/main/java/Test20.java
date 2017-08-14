import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Test20 {
    public static void main(String args[]) throws Exception {
        String filename = "bmp/1.bmp";
        BufferedImage image = ImageIO.read(new File(filename));
        int iw = image.getWidth();
        int ih = image.getHeight();
        int[] pix = new int[iw * ih];
        for (int i = 0; i < ih; i++) {
            for (int j = 0; j < iw; j++) {
                pix[i * (iw) + j] = image.getRGB(j, i);
                //System.out.print(pix[i * (iw) + j]);
                if (pix[i * (iw) + j] == -1118482)//-1118482是空白像素的值
                    pix[i * (iw) + j] = 0;
                else
                    pix[i * (iw) + j] = 1;
                System.out.print(pix[i * (iw) + j]);
            }
            System.out.println();
        }
    }
}
