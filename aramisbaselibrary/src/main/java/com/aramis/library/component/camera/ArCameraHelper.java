package com.aramis.library.component.camera;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 相机帮助类
 * Created by Aramis on 2016/10/29.
 */
public class ArCameraHelper {
    //requestCode
    public static final int requestCode_camera = 8001;
    public static final int requestCode_gallery = 8101;
    public static final int requestCode_scan = 8201;
    public static final int requestCode_scan_et = 8301;

    private String cameraPachePath = "bunnyCamera.jpg";
    private Activity activity;
    //相机拍摄图片的名称
    private final String defaultCameraName = "bunnyCamera.jpg";

    private boolean useRandom = false;

    //使用周期短，无需单例
    public ArCameraHelper(Activity activity) {
        this.activity = activity;

    }

    public void useRandom(boolean useRandom) {
        this.useRandom = useRandom;
    }

    public void openCamera(String fileDir, String fileName) {
        String ccFileName = useRandom ? (System.currentTimeMillis() + ".jpg") : (TextUtils.isEmpty(fileName) ? defaultCameraName : fileName);
        Intent intentCamera = new Intent("android.media.action.IMAGE_CAPTURE");

        File file = new File(fileDir);
        if (!file.exists()) file.mkdirs();
        File pcacheFile = new File(cameraPachePath = fileDir + File.separator + ccFileName);
//
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {  //针对Android7.0，需要通过FileProvider封装过的路径，提供给外部调用
            Uri photoURI = FileProvider.getUriForFile(activity, "com.bosh.FileProvider", pcacheFile);
            intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        } else {
            intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(pcacheFile));
        }


        intentCamera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            activity.startActivityForResult(intentCamera, requestCode_camera);
        } else {
            Toast.makeText(activity, "no permission", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 打开照相机
     */
    public void openCamera(String fileDir) {
        openCamera(fileDir, defaultCameraName);
    }

    /**
     * 打开系统画廊（单选图片）
     */
    public void openGallery() {
        Intent intentGallery = new Intent(Intent.ACTION_PICK);
        intentGallery.setType("image/*");//相片类型
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            activity.startActivityForResult(intentGallery, requestCode_gallery);
        }else {
            Toast.makeText(activity, "no permission", Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * 获取文件路径（照相机和系统画廊）
     */
    public String getFilePath(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case requestCode_camera:
                    if (!TextUtils.isEmpty(cameraPachePath)) {
                        File file = new File(cameraPachePath);
                        if (file.exists()) {
                            return cameraPachePath;
                        } else {
                            Toast.makeText(activity, "文件获取失败，请重新拍照", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                case requestCode_gallery:
                    if (data != null) {
                        return getPathFormUri(activity, data.getData());
                    } else {
                        Toast.makeText(activity, "文件获取失败，请重新选择", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
        return null;
    }


    /**
     * 重新计算bitmap尺寸，以降低bitmap大小
     *
     * @param path 文件路径
     * @return 重新计算后的bitmap
     * @throws IOException
     */
    public Bitmap revitionImageSize(String path) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(
                new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            if ((options.outWidth >> i <= 256)
                    && (options.outHeight >> i <= 256)) {
                in = new BufferedInputStream(
                        new FileInputStream(new File(path)));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }

    /**
     * 通过uri获取文件路径
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getPathFormUri(final Context context, final Uri uri) {
//        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {// ExternalStorageProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                //  handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {
                // DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) { // MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // MediaStore (and general)
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // File
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
