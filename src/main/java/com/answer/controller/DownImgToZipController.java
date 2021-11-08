package com.answer.controller;

import org.assertj.core.util.Lists;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * created by liufeng
 * 2020/12/3
 * 图片下载打包压缩
 */
@Controller
public class DownImgToZipController {

    @RequestMapping("downImg")
    public void downImg(HttpServletRequest request, HttpServletResponse response) {
        List<String> filePaths= Lists.newArrayList("http://p8.maiyaole.com/img/201411/10/201411101748304.jpg","http://p8.maiyaole.com/img/201502/01/20150201023837777.jpg");
        try {
            //文件的名称
            String downloadFilename = "中文.zip";
            //转换中文否则可能会产生乱码
            downloadFilename = URLEncoder.encode(downloadFilename, "UTF-8");
            // 指明response的返回对象是文件流
            response.setContentType("application/octet-stream");
            // 设置在下载框默认显示的文件名
            response.setHeader("Content-Disposition", "attachment;filename=" + downloadFilename);
            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
            String[] files = new String[filePaths.size()];
            filePaths.toArray(files);
            for (int i = 0; i < files.length; i++) {
                String url = files[i];
                zos.putNextEntry(new ZipEntry("temp_download" + File.separator + i + ".jpg"));
                InputStream fis = getInputStreamByGet(url);
                byte[] buffer = new byte[1024];
                int r = 0;
                while ((r = fis.read(buffer)) != -1) {
                    zos.write(buffer, 0, r);
                }
                fis.close();
            }
            zos.flush();
            zos.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static InputStream getInputStreamByGet(String url) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return conn.getInputStream();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
