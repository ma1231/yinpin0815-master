package com.example.yinpin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.example.yinpin.SongsUtil.getAllSongs;

public class MainActivity extends AppCompatActivity {
    public static boolean ispause = true;
    public static boolean pause = true;
    private List<Songs> songList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取本地音乐
        songList = getAllSongs(MainActivity.this);
        //自定义适配器
        SongAdapter adapter = new SongAdapter(MainActivity.this, R.layout.song_item, songList);
        //创建ListView
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Songs songSelected = songList.get(position);
                Toast.makeText(MainActivity.this,songSelected.getTitle(),Toast.LENGTH_SHORT).show();//提醒方式
                //调用MusicActivity
                Intent intent = new Intent(MainActivity.this, MusicActivity.class);
                intent.putExtra("Song_position",new Gson().toJson(position));//传递这首歌的位置
                intent.putExtra("Song",new Gson().toJson(songSelected));//传递这首歌的具体信息
                intent.putExtra("Song_list",new Gson().toJson(songList));//传递整个歌曲列表
                //启动活动
                startActivity(intent);
            }
        });
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, "拒绝权限将无法应用程序", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }
}