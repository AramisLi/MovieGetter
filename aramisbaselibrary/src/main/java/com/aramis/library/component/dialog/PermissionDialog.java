package com.aramis.library.component.dialog;

import android.content.Context;

/**
 * PermissionDialog
 * Created by Aramis on 2017/5/4.
 */

public class PermissionDialog implements BunnyDialog {
    private DefaultHintDialog hintDialog;

    public PermissionDialog(Context context) {
        hintDialog = new DefaultHintDialog(context);
    }


    @Override
    public void show() {
        
    }

    @Override
    public void dismiss() {

    }

    @Override
    public boolean isShowing() {
        return false;
    }
}
