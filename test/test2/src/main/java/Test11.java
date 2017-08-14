//Hashkill
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class Test11 {
    public static void main(String args[])  {
        for (int i = 0; i <= 1000; i++) {
            StringBuilder str = new StringBuilder();
            str.append("ctf{");
            str.append(String.valueOf(i));
            str.append('_');
            for (int j = 1; j < 6; j ++) {
                String str1 = "";
                switch (j) {
                    case 1:
                        str1 = str+"manhattan";
                        break;
                    case 2:
                        str1 = str+"thebronx";
                        break;
                    case 3:
                        str1 = str+"brooklyn";
                        break;
                    case 4:
                        str1 = str+"queens";
                        break;
                    case 5:
                        str1 = str+"statenisland";
                }
                str1 = str1 + '_';
                for (int k = 10000; k < 15000; k++) {
                    String str2;
                    str2 = str1 + String.valueOf(k) + '}';
                    if (Objects.equals(SHA(str2,"MD5"), "6ac66ed89ef9654cf25eb88c21f4ecd0")) {
                        System.out.println(str2);
                    }
                }
            }
        }
    }

    public String SHA256(final String strText) {
        return SHA(strText, "SHA-256");
    }

    public String SHA512(final String strText) {
        return SHA(strText, "SHA-512");
    }

    private static String SHA(final String strText, final String strType) {
        // 返回值
        String strResult = null;

        // 是否是有效字符串
        if (strText != null && strText.length() > 0)
        {
            try
            {
                // SHA 加密开始
                // 创建加密对象 并傳入加密類型
                MessageDigest messageDigest = MessageDigest.getInstance(strType);
                // 传入要加密的字符串
                messageDigest.update(strText.getBytes());
                // 得到 byte 類型结果
                byte byteBuffer[] = messageDigest.digest();

                // 將 byte 轉換爲 string
                StringBuffer strHexString = new StringBuffer();
                // 遍歷 byte buffer
                for (int i = 0; i < byteBuffer.length; i++)
                {
                    String hex = Integer.toHexString(0xff & byteBuffer[i]);
                    if (hex.length() == 1)
                    {
                        strHexString.append('0');
                    }
                    strHexString.append(hex);
                }
                // 得到返回結果
                strResult = strHexString.toString();
            }
            catch (NoSuchAlgorithmException e)
            {
                e.printStackTrace();
            }
        }

        return strResult;
    }
}
