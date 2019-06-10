package com.adht.android.medicontrol.infra;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;

import com.adht.android.medicontrol.infra.ui.MediControlApp;
import com.adht.android.medicontrol.usuario.dominio.Usuario;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Sessao {
    public static final Sessao INSTANCE = new Sessao();
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private final Map<String,Object> values = new HashMap<>();

    public void setUsuario(Usuario usuario) {
        setValue("sessao.Usuario", usuario);
    }

    public Usuario getUsuario() {
        return (Usuario)values.get("sessao.Usuario");
    }

    public String getUltimoAcesso() {
        String result = (String)values.get("sessao.ultimoAcesso");
        return result != null ? result : "-";
    }

    @SuppressWarnings("WeakerAccess")
    public void setValue(String key, Object value) {
        values.put(key, value);
    }

    public Object getValue(String key) {
        return values.get(key);
    }

    private void updateUltimoAcesso() {
        SharedPreferences prefs = MediControlApp.getContext().getSharedPreferences("sessao", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String date = DATE_FORMAT.format(new Date());
        String key = "sessao.ultimoAcesso";
        setValue(key, date);
        editor.putString(key, date);
        editor.apply();
    }

    public void reset() {
        this.values.clear();
        updateUltimoAcesso();
    }
}
