package cn.deepclue.datamaster.fusion.stdin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Created by xuzb on 22/05/2017.
 */
public class Shell {

    private static final Logger logger = LoggerFactory.getLogger(Shell.class);

    static class StreamWorker extends Thread {
        InputStream is;
        String type;

        StreamWorker(InputStream is, String type) {
            this.is = is;
            this.type = type;
        }

        public void run() {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line=null;
                if ("error".equals(type)) {
                    while ((line = br.readLine()) != null) {
                        logger.error(line);
                    }
                } else if ("debug".equals(type)) {
                    while ((line = br.readLine()) != null) {
                        logger.debug(line);
                    }
                } else if ("stdout".equals(type)) {
                    while ((line = br.readLine()) != null) {
                        System.out.println(line);
                    }
                } else {
                    while ((line = br.readLine()) != null) {
                        logger.info(line);
                    }
                }
            } catch (IOException e) {
                logger.error("", e);
            }
        }
    }

    public static int run(String cmd) {
        return run(cmd, "info");
    }

    public static Process runAsync(String cmd) {
        try {
            // http://docs.oracle.com/javase/7/docs/api/java/lang/Process.html
            // Because some native platforms only provide limited
            // buffer size for standard input and output streams,
            // failure to promptly write the input stream or read the
            // output stream of the subprocess may cause the
            // subprocess to block, or even deadlock.

            // return 0 indicates normal termination
            return Runtime.getRuntime().exec(cmd);

        } catch (IOException e) {
            logger.error("{}", e);
        }
        return null;
    }

    public static int run(String cmd, String logType) {
        try {
            // http://docs.oracle.com/javase/7/docs/api/java/lang/Process.html
            // Because some native platforms only provide limited
            // buffer size for standard input and output streams,
            // failure to promptly write the input stream or read the
            // output stream of the subprocess may cause the
            // subprocess to block, or even deadlock.
            Process proc = Runtime.getRuntime().exec(cmd);

            StreamWorker output = new StreamWorker(proc.getInputStream(), logType);
            StreamWorker error = new StreamWorker(proc.getErrorStream(), "error");

            output.start();
            error.start();

            // return 0 indicates normal termination
            return proc.waitFor();

        } catch (IOException | InterruptedException e) {
            logger.error("{}", e);
        }
        return -1;
    }

    public static String runWithStdout(String cmd) {
        try {
            Process proc = Runtime.getRuntime().exec(cmd);

            StreamWorker err = new StreamWorker(proc.getErrorStream(), "error");
            err.start();

            BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            StringBuilder stdout = new StringBuilder();
            while ((line = br.readLine()) != null) {
                stdout.append(line).append("\n");
            }
            proc.waitFor();
            return stdout.toString();
        } catch (IOException | InterruptedException e) {
            logger.error("{}", e);
        }
        return "";
    }

    public static String runWithStdinStdOut(String cmd, String input) {
        try {
            Process proc = Runtime.getRuntime().exec(cmd);

            OutputStream stdinWriter = proc.getOutputStream();
            stdinWriter.write(input.getBytes(StandardCharsets.UTF_8));
            stdinWriter.close();

            StreamWorker err = new StreamWorker(proc.getErrorStream(), "error");
            err.start();

            BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            StringBuilder stdout = new StringBuilder();
            while ((line = br.readLine()) != null) {
                stdout.append(line).append("\n");
            }
            if (0 == proc.waitFor())
                return stdout.toString();
        } catch (Exception  e) {
            logger.error("{}", e);
        }
        return "";
    }
    public static int runWithStdin(String cmd, String input) {
        return runWithStdin(cmd, input, "info");
    }

    public static int runWithStdin(String cmd, String input, String logType) {
        try {
            logger.info("Running '{}' '{}' '{}'", cmd, input, logType);
            Process proc = Runtime.getRuntime().exec(cmd);

            OutputStream stdinWriter = proc.getOutputStream();
            PrintWriter pWriter = new PrintWriter(stdinWriter);
            pWriter.println(input);
            pWriter.flush();
            pWriter.close();

            StreamWorker output = new StreamWorker(proc.getInputStream(), logType);
            StreamWorker error = new StreamWorker(proc.getErrorStream(), "error");
            output.start();
            error.start();

            return proc.waitFor();

        } catch (IOException | InterruptedException e) {
            logger.error("{}", e);
        }

        return -1;
    }
}
