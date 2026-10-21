package com.vani.assistant;
import android.*;import android.app.*;import android.content.*;import android.content.pm.PackageManager;import android.graphics.*;import android.net.*;import android.os.*;import android.provider.*;import android.speech.*;import android.speech.tts.*;import android.view.*;import android.widget.*;import java.text.*;import java.util.*;
public class MainActivity extends Activity implements TextToSpeech.OnInitListener{
 TextView state,heard; TextToSpeech tts; BroadcastReceiver rx; boolean session=false;
 public void onCreate(Bundle b){super.onCreate(b);ui();tts=new TextToSpeech(this,this);perm();rx=new BroadcastReceiver(){public void onReceive(Context c,Intent i){String q=i.getStringExtra("text");if(q!=null){heard.setText(q);command(q);}}};registerReceiver(rx,new IntentFilter("com.vani.COMMAND"),RECEIVER_NOT_EXPORTED);}
 void ui(){LinearLayout r=new LinearLayout(this);r.setOrientation(LinearLayout.VERTICAL);r.setGravity(Gravity.CENTER);r.setPadding(28,40,28,30);r.setBackgroundColor(Color.rgb(32,33,35));
 TextView h=t("वाणी",32,Color.WHITE);h.setTypeface(null,1);r.addView(h);r.addView(t("आपका व्यक्तिगत फोन सहायक",13,Color.LTGRAY));
 Space s=new Space(this);r.addView(s,new LinearLayout.LayoutParams(1,0,1));TextView o=t("व",76,Color.WHITE);android.graphics.drawable.GradientDrawable d=new android.graphics.drawable.GradientDrawable();d.setShape(1);d.setColor(Color.rgb(16,163,127));o.setBackground(d);r.addView(o,new LinearLayout.LayoutParams(200,200));
 state=t("वाणी शुरू करें",20,Color.WHITE);r.addView(state);heard=t("हिंदी / Hinglish • Voice only",15,Color.LTGRAY);r.addView(heard);
 Button start=new Button(this);start.setText("🎙  Vani Voice Session शुरू करें");start.setTextColor(Color.WHITE);start.setBackgroundColor(Color.rgb(16,163,127));r.addView(start,new LinearLayout.LayoutParams(-1,150));
 Button assistant=new Button(this);assistant.setText("Default Assistant सेटिंग खोलें");r.addView(assistant,new LinearLayout.LayoutParams(-1,-2));
 r.addView(t("बोलें: समय बताओ • कैमरा खोलो • YouTube खोलो • Google पर … खोजो • Settings खोलो • वाणी बंद हो जाओ",13,Color.GRAY));
 Space e=new Space(this);r.addView(e,new LinearLayout.LayoutParams(1,0,1));r.addView(t("🔒 कोई in-app chat history नहीं",12,Color.GRAY));setContentView(r);
 start.setOnClickListener(v->startSession());assistant.setOnClickListener(v->{try{startActivity(new Intent(Settings.ACTION_VOICE_INPUT_SETTINGS));}catch(Exception x){startActivity(new Intent(Settings.ACTION_SETTINGS));}});}
 TextView t(String x,int z,int c){TextView v=new TextView(this);v.setText(x);v.setTextSize(z);v.setTextColor(c);v.setGravity(17);v.setPadding(10,14,10,14);return v;}
 void perm(){ArrayList<String>a=new ArrayList<>();if(checkSelfPermission(Manifest.permission.RECORD_AUDIO)!=PackageManager.PERMISSION_GRANTED)a.add(Manifest.permission.RECORD_AUDIO);if(Build.VERSION.SDK_INT>=33&&checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)!=PackageManager.PERMISSION_GRANTED)a.add(Manifest.permission.POST_NOTIFICATIONS);if(!a.isEmpty())requestPermissions(a.toArray(new String[0]),1);}
 void startSession(){if(checkSelfPermission(Manifest.permission.RECORD_AUDIO)!=PackageManager.PERMISSION_GRANTED){perm();return;}session=true;Intent i=new Intent(this,VaniListeningService.class);startForegroundService(i);state.setText("वाणी सुन रही है…");speak("वाणी तैयार है। बोलिए।");}
 void stopSession(){session=false;stopService(new Intent(this,VaniListeningService.class));state.setText("वाणी बंद है");}
 void command(String raw){String q=raw.toLowerCase(Locale.ROOT);
 if(q.contains("बंद हो जाओ")||q.contains("stop vani")){speak("ठीक है।");stopSession();return;}
 if(q.contains("समय")||q.contains("time")){speak("अभी "+new SimpleDateFormat("h:mm a",new Locale("hi","IN")).format(new Date())+" है");return;}
 if(q.contains("तारीख")||q.contains("date")){speak("आज "+new SimpleDateFormat("d MMMM yyyy",new Locale("hi","IN")).format(new Date())+" है");return;}
 if(q.contains("कैमरा")||q.contains("camera")){try{startActivity(new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA));speak("कैमरा खोल रही हूँ");}catch(Exception e){speak("कैमरा नहीं खुल पाया");}return;}
 if(q.contains("settings")||q.contains("सेटिंग")){startActivity(new Intent(Settings.ACTION_SETTINGS));speak("सेटिंग्स खोल रही हूँ");return;}
 if(q.contains("youtube")){open("https://www.youtube.com");speak("यूट्यूब खोल रही हूँ");return;}
 if(q.contains("google")||q.contains("खोजो")||q.contains("search")){String x=q.replace("google","").replace("गूगल","").replace("पर","").replace("खोजो","").replace("search","").trim();open("https://www.google.com/search?q="+Uri.encode(x));speak("खोज रही हूँ");return;}
 speak("यह command अभी local Vani में उपलब्ध नहीं है। AI brain जोड़ने पर मैं प्राकृतिक भाषा में और ज्यादा commands समझ सकूँगी।");}
 void open(String u){try{startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(u)));}catch(Exception e){speak("नहीं खुल पाया");}}
 void speak(String x){state.setText(x);if(tts!=null)tts.speak(x,TextToSpeech.QUEUE_FLUSH,null,"vani");}
 public void onInit(int s){if(s==TextToSpeech.SUCCESS){tts.setLanguage(new Locale("hi","IN"));tts.setSpeechRate(.9f);}}
 protected void onDestroy(){try{unregisterReceiver(rx);}catch(Exception e){}if(tts!=null){tts.stop();tts.shutdown();}super.onDestroy();}
}
