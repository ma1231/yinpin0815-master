package com.example.yinpin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class SongAdapter extends ArrayAdapter<Songs> {
    private int resourceId;

    SongAdapter(Context context, int textViewResourceId, List<Songs> objects){ //ArrayList or List
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        Songs song = getItem(position);//获取当前项song的实例
        View view;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent,false);
        }
        else{
            view = convertView;
        }

        ImageView songImage =(ImageView) view.findViewById(R.id.song_image);
        TextView songTitle = (TextView) view.findViewById(R.id.song_title);
        TextView songSinger = (TextView) view.findViewById(R.id.song_singer);
        TextView songDuration = (TextView) view.findViewById(R.id.song_duration);
        assert song != null;
        songImage.setImageResource(song.getImageId());
        songTitle.setText(song.getTitle());
        songSinger.setText(song.getSinger());
        songDuration.setText(song.getDuration());
        return view;
    }

}

