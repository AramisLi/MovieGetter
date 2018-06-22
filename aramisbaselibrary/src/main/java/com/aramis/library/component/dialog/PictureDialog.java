package com.aramis.library.component.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.aramis.library.R;
import com.aramis.library.component.camera.ArCameraHelper;

import java.io.File;


/**
 * PictureDialog
 * Created by Aramis on 2017/5/3.
 */

public class PictureDialog implements BunnyDialog {
    private Dialog dialog;
    private ArCameraHelper arCameraHelper;
    private String cameraSaveDir;
    private OnPreCameraListener onPreCameraListener;
    private String cameraFileName;
    private boolean useRandom = false;

    //1=相机，2=相册，3=相机+相册
    private int buttonStatus = 1;

    public PictureDialog(Activity activity) {
        init(activity);
    }

    public PictureDialog(Activity activity, String cameraSaveDir) {
        this.cameraSaveDir = cameraSaveDir;
        init(activity);
    }

    public PictureDialog(Activity activity, String cameraSaveDir, boolean useRandom) {
        this.cameraSaveDir = cameraSaveDir;
        this.useRandom = useRandom;
        init(activity);
    }
    public PictureDialog(Activity activity, String cameraSaveDir, boolean useRandom,int buttonStatus) {
        this.cameraSaveDir = cameraSaveDir;
        this.useRandom = useRandom;
        this.buttonStatus = buttonStatus;
        init(activity);
    }

    public PictureDialog(Activity activity, String cameraSaveDir, String cameraFileName) {
        this.cameraSaveDir = cameraSaveDir;
        this.cameraFileName = cameraFileName;
        init(activity);
    }

    private void init(Activity activity) {
        arCameraHelper = new ArCameraHelper(activity);
        arCameraHelper.useRandom(useRandom);
        dialog = new Dialog(activity, R.style.new_custom_dialog);
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.view_dialog_picture, null);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setWindowAnimations(R.style.DialogAnimation_Bottom);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(attributes);
        initView(dialogView);
        dialog.setContentView(dialogView);

        if (cameraSaveDir != null) {
            File dir = new File(cameraSaveDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
    }


    public void setCameraSaveDir(String cameraSaveDir) {
        this.cameraSaveDir = cameraSaveDir;
    }

    private void initView(View dialogView) {
        //拍照
        TextView text_dialog_camera = (TextView) dialogView.findViewById(R.id.text_dialog_camera);
        text_dialog_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPreCameraListener != null) onPreCameraListener.onPreCamera();
                arCameraHelper.openCamera(cameraSaveDir, cameraFileName);
                dismiss();
            }
        });
        //相册
        TextView text_dialog_gallery = (TextView) dialogView.findViewById(R.id.text_dialog_gallery);
        text_dialog_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arCameraHelper.openGallery();
                dismiss();
            }
        });
        //取消
        TextView text_dialog_cancel = (TextView) dialogView.findViewById(R.id.text_dialog_cancel);
        text_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        switch (buttonStatus){
            case 1:
                text_dialog_camera.setVisibility(View.VISIBLE);
                text_dialog_gallery.setVisibility(View.GONE);
                break;
            case 2:
                text_dialog_camera.setVisibility(View.GONE);
                text_dialog_gallery.setVisibility(View.VISIBLE);
                break;
            case 3:
                text_dialog_camera.setVisibility(View.VISIBLE);
                text_dialog_gallery.setVisibility(View.VISIBLE);
                break;
        }
    }

    public interface OnPreCameraListener {
        void onPreCamera();
    }

    public void setOnPreCameraListener(OnPreCameraListener onPreCameraListener) {
        this.onPreCameraListener = onPreCameraListener;
    }

    public ArCameraHelper getArCameraHelper() {
        return arCameraHelper;
    }


    @Override
    public void show() {
        dialog.show();
    }

    @Override
    public void dismiss() {
        dialog.dismiss();
    }

    @Override
    public boolean isShowing() {
        return dialog.isShowing();
    }
}
