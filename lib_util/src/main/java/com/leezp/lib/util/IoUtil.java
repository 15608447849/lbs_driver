package com.leezp.lib.util;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Leeping on 2018/7/31.
 * email: 793065165@qq.com
 */

public class IoUtil {
    public static void closeIo(Closeable... closeable){
        for (Closeable c: closeable){
            if (c == null) continue;
            try {
                c.close();
            } catch (IOException e) {
            }
        }
    }

    public static File imageUriToFile(Context context, Uri uri, String filePath){
        File file = null;
        final String scheme = uri.getScheme();
        if (ContentResolver.SCHEME_CONTENT.equals(scheme)
                || ContentResolver.SCHEME_FILE.equals(scheme)) {
            InputStream stream = null;
            FileOutputStream fos = null;
            try {
                stream = context.getContentResolver().openInputStream(uri);
                file = new File(filePath);
                fos = new FileOutputStream(file);
                byte[] buf = new byte[1024*8];
                int len;
                while ( (len=stream.read(buf)) >0 ){
                    fos.write(buf,0,len);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                closeIo(stream,fos);
            }
        }
        return file;
    }


    public static String inputStreamToString(InputStream inputStream) {
        String result = null;
        BufferedReader reader = null;
        try {
            reader =  new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer resultBuffer = new StringBuffer();
            String tempLine=null;
            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
            }
            result = resultBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            closeIo(reader);
        }
        return result;
    }
}
