package com.example.mywordsbook;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RightFragment extends Fragment {

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.right_fragment,container,false);
        return view;
    }

    public void refresh(String CHN,String SEN){
        TextView CHN_txt =(TextView) getView().findViewById(R.id.CHN_txt);
        TextView SEN_txt =(TextView) getView().findViewById(R.id.SEN_txt);
        CHN_txt.setText(CHN);
        SEN_txt.setText(SEN);
    }

}
