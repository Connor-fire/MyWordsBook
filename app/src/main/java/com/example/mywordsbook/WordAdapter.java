package com.example.mywordsbook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class WordAdapter extends ArrayAdapter<Word> {
    private int resourceId;
    List<Word> wordList =null;
    public WordAdapter( Context context, int resource,  List<Word> objects) {
        super(context, resource, objects);
        wordList= objects;
        resourceId=resource;
    }

    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        Word word=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView ENG_txt=(TextView) view.findViewById(R.id.ENG_txt);
        ENG_txt.setText(word.ENG);
        return view;
    }

}
