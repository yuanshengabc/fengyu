package cn.deepclue.datamaster.streamer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by xuzb on 21/06/2017.
 */
public class HttpExecutor {
    private static final Logger logger = LoggerFactory.getLogger(HttpExecutor.class);

    public static class StringResp {
        private static final int UNKNOWN_ERROR_CODE = -1;
        private int respCode = UNKNOWN_ERROR_CODE;
        private String respMsg;

        public int getCode() {
            return respCode;
        }

        public boolean isSuccessCode() {
            return respCode == 200;
        }

        public boolean isErrorCode() {
            return !isSuccessCode();
        }

        public String getMessage() {
            return respMsg;
        }

        public void setResponseCode(int respCode) {
            this.respCode = respCode;
        }

        public void setResponseMessage(String respMsg) {
            this.respMsg = respMsg;
        }

        public String toString() {
            return "ResponseCode: " + respCode + "Message:\n" + respMsg;
        }
    }

    public static StringResp doHttpPostRequest(String urlstr, String content, Map<String, String> properties) {
        return doHttpPostRequest(urlstr, content, properties, "UTF8", 4000);
    }

    public static StringResp doHttpPostRequest(String urlstr, String content, Map<String, String> properties, String charset, int timeout) {
        HttpURLConnection conn = null;
        OutputStream out = null;
        InputStream is = null;
        BufferedReader br = null;
        StringResp respMsg = new StringResp();
        try {
            URL url = new URL(urlstr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(timeout);
            conn.setConnectTimeout(timeout / 2);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(false);

            // set properties
            if (properties != null) {
                for (Map.Entry<String, String> propertyEntries : properties.entrySet()) {
                    String key = propertyEntries.getKey();
                    conn.setRequestProperty(key, propertyEntries.getValue());
                }
            }

            conn.connect();

            out = conn.getOutputStream();

            out.write(content.getBytes(charset));
            out.flush();

            is = conn.getInputStream();
            br = new BufferedReader(new InputStreamReader(is, charset));
            StringBuilder response = new StringBuilder();
            String readLine;
            while ((readLine = br.readLine()) != null) {
                response.append(readLine);
            }

            respMsg.setResponseMessage(response.toString());
        } catch (IOException e) {
            logger.info("Failed to do http post request {}", e);
            respMsg.setResponseMessage(e.toString());
        } finally {
            try {
                if (conn != null)
                    respMsg.setResponseCode(conn.getResponseCode());
                if (out != null)
                    out.close();
                if (is != null)
                    is.close();
                if (br != null)
                    br.close();
            } catch (IOException e) {
                logger.info("Failed to close connection streamer {}", e);
            }

            if (conn != null)
                conn.disconnect();
        }
        return respMsg;
    }

    public static StringResp doHttpGetRequest(String urlstr) {
        return doHttpGetRequest(urlstr, null, "UTF8", 4000);
    }

    public static StringResp doHttpGetRequest(String urlstr, Map<String, String> properties, String charset, int timeout) {
        StringResp respMsg = new StringResp();
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            URL url = new URL(urlstr);

            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(timeout / 2);
            conn.setReadTimeout(timeout);

            // set properties
            if (properties != null) {
                for (Map.Entry<String, String> propertyEntries : properties.entrySet()) {
                    String key = propertyEntries.getKey();
                    conn.setRequestProperty(key, propertyEntries.getValue());
                }
            }

            is = conn.getInputStream();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            byte[] buffer = new byte[8064];

            while (true) {
                int rd = is.read(buffer);
                if (rd == -1) break;
                stream.write(buffer, 0, rd);
            }

            stream.flush();
            buffer = stream.toByteArray();
            String response = new String(buffer);
            respMsg.setResponseMessage(response);
        } catch (IOException e) {
            logger.info("Failed to do http get request {}", e);
            respMsg.setResponseMessage(e.toString());
        } finally {
            try {
                if (conn != null)
                    respMsg.setResponseCode(conn.getResponseCode());
                if (is != null)
                    is.close();
            } catch (IOException e) {
                logger.info("Failed to close input streamer {}", e);
            }
            if (conn != null)
                conn.disconnect();
        }
        return respMsg;
    }
}
