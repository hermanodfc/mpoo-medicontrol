package com.adht.android.medicontrol.usuario.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.adht.android.medicontrol.R;
import androidx.fragment.app.Fragment;

public class UsuarioFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.usuario, container, false);
    }
}

