package com.example.alphacar.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.alphacar.MainActivity;
import com.example.alphacar.R;
import com.example.alphacar.RegisterActivity;

public class ButtonFragment extends Fragment {
    MainActivity mainActivity;
    Button reg_cancel, reg_btn, reg_up_btn, btn_cp_reg;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.button_in_button, container, false);
        mainActivity = (MainActivity) getActivity();

        btn_cp_reg = mainActivity.findViewById(R.id.btn_cp_reg);

        reg_up_btn = rootView.findViewById(R.id.btn_reg_up);
        reg_btn = rootView.findViewById(R.id.btn_reg_new);
        reg_cancel = rootView.findViewById(R.id.btn_cancel);

        reg_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new MasterStoreFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contain, fragment).addToBackStack(null).commit();
            }
        });

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, RegisterActivity.class);
                startActivity(intent);
            }
        });

        reg_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().remove(ButtonFragment.this).commit();
                btn_cp_reg.setVisibility(View.VISIBLE);

            }
        });


        return rootView;
    }
}
