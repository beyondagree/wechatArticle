package com.robl.wechatarticle.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class PythonUtil {

    private static final String pyEntry = "entry.py";

    private static String PYTHON_BASE_DIR = "pythonSrc";

    public static boolean execPythonFunc(String... args) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            sb.append(arg);
            sb.append(" ");
        }
        StringBuilder cmd = new StringBuilder("python ");
        cmd.append(PYTHON_BASE_DIR);
        cmd.append(File.separator);
        cmd.append(pyEntry);
        cmd.append(" ");
        cmd.append(sb);
        System.out.println("cmd=" + cmd);

        Process process = Runtime.getRuntime().exec(cmd.toString());
        InputStream is = process.getInputStream();
        DataInputStream dis = new DataInputStream(is);
        String str = "";
        while ((str = dis.readLine()) != null) {
            System.out.println(str);
        }
        process.waitFor();
        is.close();
        process.exitValue();

        return true;
    }

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println(
                PYTHON_BASE_DIR
        );
    }

}
