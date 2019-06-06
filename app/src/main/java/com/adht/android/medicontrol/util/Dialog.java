package com.adht.android.medicontrol.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Dialog {

    public static AlertDialog alertDialogOkButton(String titulo, String mensagem, Context context,
                                                  DialogInterface.OnClickListener listener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(titulo);
        builder.setMessage(mensagem);

        builder.setPositiveButton("OK", listener);
        return builder.create();
    }

    public static AlertDialog alertDialoOkCancelButton(String titulo, String mensagem,
                                                       Context context,
                                                       DialogInterface.OnClickListener okListener,
                                                       DialogInterface.OnClickListener cancelListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(titulo);
        builder.setMessage(mensagem);

        builder.setPositiveButton("OK", okListener);
        builder.setNegativeButton("Cancelar", cancelListener);

        return builder.create();

    }

}
