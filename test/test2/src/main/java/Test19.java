import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

public class Test19 {
    public static void main(String args[]) throws Exception {
        long sum = 0;
        for (int i = 1; i < 10000; i++) {
            String filename = "bmp/".concat(String.valueOf(i).concat(".bmp"));
            String s = Bmp2Num.recognize(ImageIO.read(new File(filename)));
            System.out.println(s);
            sum = sum + i * Long.parseLong(s);
        }
        System.out.println("sum=" + sum);
    }
}

class Bmp2Num {
    // 数字模板 0-9
    private static int[][] value = {
            // num 0;
            {0, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0},
            // num 1
            {0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0},
            // num2
            {0, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1},
            // num3
            {0, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0},
            // num4
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1},
            // num5
            {1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0},
            // num6
            {0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0},
            // num7
            {1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
            // num8
            {0, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0},
            // num9
            {0, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 0, 0}
    };

    public static String recognize(byte[] byteArray) throws Exception {
        InputStream is = new ByteArrayInputStream(byteArray);
        BufferedImage image = ImageIO.read(is);
        return recognize(image);
    }

    /*识别图像*/
    static String recognize(BufferedImage image) throws Exception {
        StringBuilder sb = new StringBuilder("");
        BufferedImage newim[] = new BufferedImage[4];
        if (null == image) {
            throw new RuntimeException();
        }
        // 图像分成四块
        newim[0] = image.getSubimage(0, 0, 6, 10);
        newim[1] = image.getSubimage(10, 0, 6, 10);
        newim[2] = image.getSubimage(20, 0, 6, 10);
        newim[3] = image.getSubimage(30, 0, 6, 10);
        for (int k = 0; k < 4; k++) {
            int iw = newim[k].getWidth(null);
            int ih = newim[k].getHeight(null);
            int[] pix = new int[iw * ih];
            // 转换为0，1的图像数组。扫描图像数据，像素为白色的取值为0，否则取值1；
            for (int i = 0; i < ih; i++) {
                for (int j = 0; j < iw; j++) {
                    pix[i * (iw) + j] = newim[k].getRGB(j, i);
                    //System.out.print(pix[i * (iw) + j]);
                    if (pix[i * (iw) + j] == -1118482)//-1118482是空白像素的值
                        pix[i * (iw) + j] = 0;
                    else
                        pix[i * (iw) + j] = 1;
                    //System.out.print(pix[i * (iw) + j]);
                }
                //System.out.println();
            }
            int r = getMatchNum(pix);
            sb.append(r);
        }
        return sb.toString();
    }

    private static int getMatchNum(int[] pix) {
        int result = -1;
        int temp = 100;
        int x;
        for (int k = 0; k <= 9; k++) {
            x = 0;
            for (int i = 0; i < pix.length; i++) {
                x = x + Math.abs(pix[i] - value[k][i]);
            }
            if (x == 0) {
                result = k;
                break;
            } else if (x < temp) {
                temp = x;
                result = k;
            }
        }
        return result;
    }
}
