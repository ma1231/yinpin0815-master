package com.example.yinpin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaCodecList;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import static android.media.MediaExtractor.SEEK_TO_PREVIOUS_SYNC;

public class MusicActivity<path> extends AppCompatActivity implements View.OnClickListener  {
    private static Songs songSelected;
    private static Songs songPrevious;
    private static int songPosition;
    private static int isHaveConnect=0;
    private static List<Songs> songList ;
    private boolean isMusicPlaying;
    private boolean isMusicSwitch;
    public static boolean ispause = true;
    public static final String SAMPLE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/music4.aac";
    public static boolean pause = true;
    public static int i=1;
    public static int j=1;
    public static int m=1;
    int mSampleRate;
    int channel;
    int samplerate=44100;//采样率44100
    int changelConfig= AudioFormat.CHANNEL_OUT_STEREO;//信道数

    private MediaCodec mediaDecode;
    private MediaFormat format;
    private MediaExtractor Extractor;
    private MediaCodec.Callback mCallback;
    private AudioTrack mPlayer;

    private Button play ;
    private Button stop ;
    private Button pause1 ;
    private Button continuer ;
    private Button back ;
    private Button left ;
    private Button right ;

    private TextView title ;
    private TextView  author ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        Intent intent = getIntent();
        String JsonData = intent.getStringExtra("Song");
        songSelected = new Gson().fromJson(JsonData, Songs.class); //接收选中的歌曲信息
        String JsonData2 = intent.getStringExtra("Song_position");
        songPosition = new Gson().fromJson(JsonData2, int.class); //接收选中的歌曲在列表中的位置
        String JsonData3 = intent.getStringExtra("Song_list");
        songList = new Gson().fromJson(JsonData3, new TypeToken<List<Songs>>(){}.getType()); //接收整个歌曲列表



         pause1 = (Button) findViewById(R.id.pause1);
         //continuer = (Button) findViewById(R.id.continuer);
         back = (Button) findViewById(R.id.button_back);
        left = (Button) findViewById(R.id.button_left);
         right = (Button) findViewById(R.id.button_right);
        title = (TextView) findViewById(R.id.textTitle);
          author = (TextView) findViewById(R.id.textAuthor);
        //title.setText(songSelected.getTitle());
        //author.setText(songSelected.getSinger());
        pause1.setOnClickListener(this);
       // continuer.setOnClickListener(this);
        back.setOnClickListener(this);
        left.setOnClickListener(this);
        right.setOnClickListener(this);

       if(isHaveConnect==0)
       {
               isHaveConnect=1;
               ispause=false;
               excute(songSelected.getFileUrl());
       }else{
               if (songPrevious.getTitle()!=songSelected.getTitle())
               {
                   isMusicSwitch=true;
               }else{
                   isMusicSwitch=false;
               }
           /*if(!ispause) {
               ispause=true;
               excute(songSelected.getFileUrl());

              *//* if ( isMusicSwitch) {
                   //切歌+正在播放
                   ispause = false;
                   excute(songSelected.getFileUrl());

               } else if ( !isMusicSwitch) {
                   //相同+正在播放
                   ispause = false;
                   excute(songSelected.getFileUrl());
               }*//*
           }*/
            if(ispause&&isMusicSwitch){
               //切歌+已经停止播放
                ispause=false;
               excute(songSelected.getFileUrl());

           }
           else if(ispause&&!isMusicSwitch){
               ispause=false;
               excute(songSelected.getFileUrl());
           }
        }
        songPrevious = songSelected;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pause1:
               if(i%2==1){
                if(pause) {
                    pause = false;
                    i++;
                   pause1.setBackground(getDrawable(R.drawable.play));
                }
               }
              else if(i%2==0){
                   if(!pause) {
                       pause = true;
                       pause1.setBackground(getDrawable(R.drawable.pause));
                   }
                   i++;
               }
                break;

            /*case R.id.continuer:
                if(!pause) {
                    pause = true;
                    //decoder.setrunning();

                }
                Log.d("xn-continuerA", String.valueOf(pause));
                break;*/
            case R.id.button_back:
                ispause=true;
                finish();
                break;
            case R.id.button_left:
                pause=true;
                if(j%2==1){
                    if (!ispause) {
                        ispause = true;
                    }
                   if(songPosition > 0) {
                        songPosition = songPosition - 1;
                    }
                    songSelected = songList.get(songPosition);


                    j++;
                }
               else if(j%2==0){
                    if (ispause) {
                        ispause = false;
                        title.setText(songSelected.getTitle());
                        author.setText(songSelected.getSinger());
                         excute( songSelected.getFileUrl());
                        //Log.d("xnxn无", String.valueOf(ispause)+"abc");
                        if(pause){
                            pause1.setBackground(getDrawable(R.drawable.pause));
                            j++;
                            i++;
                        }
                    }
                }


                /*ispause=false;
                excute(songSelected.getFileUrl());*/
                break;
            case R.id.button_right:
               if(m%2==1) {
                   pause = true;
                   if (!ispause) {
                       ispause = true;
                   }
                   Log.e("R.id.button_rights:", String.valueOf(songPosition));
                   if (songPosition < songList.size() - 1) {
                       songPosition = songPosition + 1;
                   }
                   songSelected = songList.get(songPosition);

                   Log.e("R.id.button_right:", songSelected.getTitle());
                   m++;
               }else if(m%2==0){
                   if (ispause) {
                       ispause = false;
                       title.setText(songSelected.getTitle());
                       author.setText(songSelected.getSinger());
                       excute( songSelected.getFileUrl());
                       //Log.d("xnxn无", String.valueOf(ispause)+"abc");
                       if(pause){
                           pause1.setBackground(getDrawable(R.drawable.pause));
                           m++;
                           i++;
                       }
                   }

               }
                break;
            default:
                break;
        }
    }
    public  void excute(String path){
        {
            try {
               title.setText(songSelected.getTitle());
                author.setText(songSelected.getSinger());
               Extractor = new MediaExtractor();
                Log.e("xnxn无",songSelected.getFileUrl()+"a");
                   Extractor.setDataSource(path);
                Log.e("xnxn无123",path);
                for (int i = 0; i < Extractor.getTrackCount(); i++) {
                    format = Extractor.getTrackFormat(i);//获取指定（index）的通道格式
                    float time = format.getLong(MediaFormat.KEY_DURATION) / 1000000;
                    Log.e("xnxn无123", String.valueOf(time));
                    String mime = format.getString(MediaFormat.KEY_MIME);//媒体数据格式
                    if (mime.startsWith("audio/")) {

                        Extractor.selectTrack(i);//选择此音频轨道
                        mSampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);//获取当前音频的采样率
                        Log.d("xn", String.valueOf(mSampleRate));
                        channel = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT); //获取当前帧的通道数
                        int minBufferSize = AudioTrack.getMinBufferSize(samplerate, changelConfig, AudioFormat.ENCODING_PCM_16BIT);

                        mPlayer = new AudioTrack(AudioManager.STREAM_MUSIC, samplerate, changelConfig, AudioFormat.ENCODING_PCM_16BIT, minBufferSize, AudioTrack.MODE_STREAM);
                        {
                            MediaCodecList codecList = new MediaCodecList(MediaCodecList.ALL_CODECS);//8.2
                            String decoderName = codecList.findDecoderForFormat(format);//8.2
                            mediaDecode = MediaCodec.createByCodecName(decoderName);//8.2
                            MediaMetadataRetriever mmr = new MediaMetadataRetriever();//8.11
                            mmr.setDataSource(path);//8.11
                            String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);//播放时长//8.11
                            Log.d("许诺666", duration);//8.11
                            {
                                mediaDecode.setCallback(new MediaCodec.Callback() {
                                    @Override
                                    public void onInputBufferAvailable(MediaCodec mediaCodec, int id) {
                                        if (!ispause) //8.7
                                        {
                                            Log.e("xn1234", "onInputBufferAvailable");

                                            ByteBuffer inputBuffer = mediaDecode.getInputBuffer(id);
                                            {
                                                {
                                                    Extractor.seekTo(10, SEEK_TO_PREVIOUS_SYNC);
                                                    int readresult = Extractor.readSampleData(inputBuffer, 0);//从MediaExtractor中读取一帧待解的数据

                                                    //小于0 代表所有数据已读取完成
                                                    if (readresult < 0) {
                                                        mediaDecode.queueInputBuffer(id, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                                                    } else {

                                                        mediaDecode.queueInputBuffer(id, 0, readresult, Extractor.getSampleTime(), 0);//插入一帧待解码的数据
                                                        Log.d("xn-readresult", String.valueOf(readresult));
                                                        if(pause) {

                                                            Extractor.advance();//MediaExtractor移动到下一取样处
                                                            //Extractor.seekTo(0, SEEK_TO_PREVIOUS_SYNC);
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                    }

                                    @Override
                                    public void onOutputBufferAvailable(MediaCodec mediaCodec, int id, MediaCodec.BufferInfo bufferInfo) {

                                        {
                                            ByteBuffer outputBuffer = mediaDecode.getOutputBuffer(id);
                                            Log.d("xn-outputBuffer", "ByteBuffer outputBuffer");
                                            MediaFormat outputFormat = mediaDecode.getOutputFormat(id);
                                            Log.d("xn-format", "format == outputFormat");
                                            {
                                                if (/*format == outputFormat &&*/ outputBuffer != null && bufferInfo.size > 0) {
                                                    byte[] mbuffer = new byte[bufferInfo.size];//BufferInfo内定义了此数据块的大小
                                                    Log.d("xn-mbuffer", String.valueOf(mbuffer));
                                                    outputBuffer.get(mbuffer);//将Buffer内的数据取出到字节数组中
                                                    mPlayer.play();

                                                    // mPlayer.pause();
                                                        if (pause) {
                                                            mPlayer.write(mbuffer, 0, bufferInfo.size);
                                                        }

                                                }
                                                mediaDecode.releaseOutputBuffer(id, false);
                                            }

                                        }
                                    }

                                    @Override
                                    public void onError(MediaCodec mediaCodec, MediaCodec.CodecException e) {
                                    }

                                    @Override
                                    public void onOutputFormatChanged(MediaCodec mediaCodec, MediaFormat mediaFormat) {
                                            format = mediaFormat;
                                    }
                                });
                            }
                        }
                        mediaDecode.configure(format, null, null, 0);//8.2
                    }
                }
                format =  mediaDecode.getOutputFormat();
                mediaDecode.start();//8.7
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
