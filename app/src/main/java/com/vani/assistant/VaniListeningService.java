package com.vani.assistant;
import android.app.*;import android.content.*;import android.os.*;import android.speech.*;import java.util.*;
public class VaniListeningService extends Service {
 SpeechRecognizer sr;
 public void onCreate(){super.onCreate(); if(Build.VERSION.SDK_INT>=26){NotificationChannel c=new NotificationChannel("vani","Vani Voice",NotificationManager.IMPORTANCE_LOW);getSystemService(NotificationManager.class).createNotificationChannel(c);}
 Notification.Builder b=Build.VERSION.SDK_INT>=26?new Notification.Builder(this,"vani"):new Notification.Builder(this);
 startForeground(7,b.setContentTitle("वाणी सक्रिय है").setContentText("Voice session चल रहा है").setSmallIcon(android.R.drawable.ic_btn_speak_now).build());
 sr=SpeechRecognizer.createSpeechRecognizer(this); sr.setRecognitionListener(new RecognitionListener(){
 public void onReadyForSpeech(Bundle b){} public void onBeginningOfSpeech(){} public void onRmsChanged(float f){} public void onBufferReceived(byte[] b){} public void onEndOfSpeech(){}
 public void onError(int e){new Handler(Looper.getMainLooper()).postDelayed(()->listen(),900);}
 public void onResults(Bundle b){ArrayList<String> x=b.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION); if(x!=null&&!x.isEmpty()){Intent i=new Intent("com.vani.COMMAND");i.setPackage(getPackageName());i.putExtra("text",x.get(0));sendBroadcast(i);} new Handler(Looper.getMainLooper()).postDelayed(()->listen(),400);}
 public void onPartialResults(Bundle b){} public void onEvent(int a,Bundle b){} }); listen();}
 void listen(){try{Intent i=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);i.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"hi-IN");i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);sr.startListening(i);}catch(Exception e){}}
 public android.os.IBinder onBind(Intent i){return null;} public void onDestroy(){if(sr!=null)sr.destroy();super.onDestroy();}
}
