package com.yufei.util;

import java.io.*;

/**
 * Created by XinYufei on 2017/2/27.
 */
public class FileOperation {

    public static void readToBuffer(StringBuffer buffer, String filePath)
            throws IOException {
        String line; // 用来保存每行读取的内容
        // 用流和流的读写
//		InputStream is = new FileInputStream(filePath);
//		BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        // 用文件和文件的读写
        File is = new File(filePath);
        BufferedReader reader = new BufferedReader(new FileReader(is));

        line = reader.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
            buffer.append(line); // 将读到的内容添加到 buffer 中
            buffer.append("\n"); // 添加换行符
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
//		is.close();
    }

    public static void writeFile(String content, String filePath)
            throws IOException {
        // 用OutputStream和OutputStreamWriter
        // 用流和流的读写
        OutputStream is = new FileOutputStream(filePath);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(is));

        // 用File和BufferedWriter
        // 用文件和文件的读写
//		File is = new File(filePath);
//		BufferedWriter writer = new BufferedWriter(new FileWriter(is));

        writer.write(content);
        writer.flush();
        writer.close();
        // is.close();
    }
}
