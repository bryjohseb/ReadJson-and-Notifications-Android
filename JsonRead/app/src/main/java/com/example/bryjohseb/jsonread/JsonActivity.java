package com.example.bryjohseb.jsonread;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.app.RemoteInput;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.R.attr.data;
import static android.os.Build.ID;
import static com.example.bryjohseb.jsonread.R.layout.dialog;

public class JsonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        /*NotificationManager notmngr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Cancels the notification
        int not = getIntent().getIntExtra("notification",-1);
        notmngr.cancel(not);
        if(not != -1)
            finish();*/
        setContentView(R.layout.activity_json);
        Button salvar = (Button) findViewById(R.id.btSalvar);
        Button cargar = (Button) findViewById(R.id.btCargar);
        Button borrar = (Button) findViewById(R.id.btBorrar);
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nombre = (EditText) findViewById(R.id.etNombre);
                EditText edad = (EditText) findViewById(R.id.etEdad);
                EditText correo = (EditText) findViewById(R.id.etCorreo);
                String name = nombre.getText().toString();
                String age = edad.getText().toString();
                String email = correo.getText().toString();
                try {
                    JSONObject jobject = new JSONObject();
                    jobject.put("nombre", name);
                    jobject.put("edad", age);
                    jobject.put("correo", email);
                    JSONArray jarray = readFromFile();
                    jarray.put(jobject);
                    writeToFile(jarray.toString());
                    Mensaje("Objeto Salvado con Éxito!");
                    notification1(1,R.drawable.ecology,"Testing","This is my first notification");
                } catch (JSONException e) {
                    Log.e("Exception", "Unable to create JSONArray: " + e.toString());
                }
            }
        });
        cargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificacion2();
                //notifation5();
                TextView data = (TextView) findViewById(R.id.tvData);
                String dataFile = null;
                String [] d = {};
                try {
                    dataFile = readFromFile().toString();
                } catch (JSONException e) {
                    Log.e("Exception", "Unable to Load JSON from FILE: " + e.toString());
                }
                data.setText(dataFile);
            }
        });
        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*borrarArchivo();
                Mensaje("Data Borrada de Archivo!");*/
                Confirmacion();
            }
        });
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    private void writeToFile(String data) {
        try {
            if(isExternalStorageWritable()){
                File tarjeta = Environment.getExternalStorageDirectory();
                File file = new File(tarjeta.getAbsolutePath(), "dataExter.txt");
                OutputStreamWriter osw = new OutputStreamWriter(
                        new FileOutputStream(file));
                osw.write(data);
                osw.close();
            }
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("data.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private JSONArray readFromFile() throws JSONException {

        String ret = "";
        JSONArray jarray;

        try {
            InputStream inputStream = getApplicationContext().openFileInput("data.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        if (ret == null || ret.isEmpty()) {
            jarray = new JSONArray();
        } else {
            jarray = new JSONArray(ret);
        }
        //JSONObject test;
        //int tam = jarray.length();
        //test = jarray.getJSONObject(0);
        //String nombre = String.valueOf(test.get("nombre"));
        //int edad = Integer.parseInt(String.valueOf(test.get("edad")));
        //String email = String.valueOf(test.get("correo"));
        //final List<JSONObject> objs = asList(jarray);
        //objs.remove(1);
        //ArrayList<String> test = getVector(jarray);
        //String lala = test.toString().substring(1,test.toString().length()-1);
        //String another = lala.substring(1,lala.length()-1);
        //String [] lalala = lala.split(",");
        return jarray;
    }

    public static JSONObject getObject(final JSONArray ja) {
        final int len = ja.length();
        final JSONObject result = null;
        for (int i = 0; i < len; i++) {
            final JSONObject obj = ja.optJSONObject(i);
            try {
                String nombre = String.valueOf(obj.get("nombre"));
            }
            catch(JSONException exc){
            }
        }
        return result;
    }

    public static ArrayList<String> getVector(final JSONArray ja) {
        final int len = ja.length();
        final ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < len; i++) {
            final JSONObject obj = ja.optJSONObject(i);
            try {
                String nombre = String.valueOf(obj.get("nombre"));
                result.add(nombre);
            }
            catch(JSONException exc){
            }
        }
        return result;
    }

    private boolean borrarArchivo() {
        File dir = getFilesDir();
        File file = new File(dir, "data.txt");
        boolean deleted = file.delete();
        return deleted;
    }

    public void Mensaje(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void Confirmacion() {
        /*AlertDialog.Builder builder = new AlertDialog.Builder(JsonActivity.this);


        builder.setTitle("AlertDialog Example");
        builder.setCancelable(true);

        builder.setMessage("This is an Example of Android AlertDialog with 3 Buttons!!");


        //Button One : Yes
        /*builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(JsonActivity.this, "Yes button Clicked!", Toast.LENGTH_SHORT).show();
            }
        });


        //Button Two : No
        /*builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(JsonActivity.this, "No button Clicked!", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

           */
        //Button Three : Neutral
        /*builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(JsonActivity.this, "Neutral button Clicked!", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        AlertDialog diag = builder.create();
        diag.show();*/
        /*LayoutInflater inflater=LayoutInflater.from(getApplicationContext());
        View customTitle=inflater.inflate(R.layout.dialog, null);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(JsonActivity.this);
        builder1.setCustomTitle(customTitle);
        builder1.setMessage("Datos Guardados Correctamente");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Si",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {Mensaje("positivo"); } });
        builder1.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {Mensaje("negativo"); } });
        AlertDialog alert11 = builder1.create();
        alert11.show();*/

        LayoutInflater inflater=LayoutInflater.from(getApplicationContext());
        View customTitle=inflater.inflate(dialog, null);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(JsonActivity.this);
        builder1.setCustomTitle(customTitle);
        builder1.setTitle("Something");
        TextView title = (TextView) customTitle.findViewById(R.id.customtitlebar);
        title.setText("New Title");
        builder1.setMessage("No Posees Tratamientos todavia");
        builder1.setIcon(R.drawable.warning);
        builder1.setCancelable(true);
        builder1.setPositiveButton("Close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {Mensaje("positivo"); } });
        /*builder1.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {Mensaje("negativo"); } });*/
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void notification1(int id, int iconId, String titulo, String contenido) {

        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this).setSmallIcon(iconId)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ecology))
                        .setContentTitle(titulo)
                        .setContentText(contenido)
                        .setColor(getResources().getColor(R.color.colorPrimary));


        // Construir la notificación y emitirla
        NotificationManager notificationManager =
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, builder.build());
    }

    public void notificacion(int id, int iconId, String titulo, String contenido){
        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this).setSmallIcon(iconId)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ecology))
                .setContentTitle(titulo)
                .setContentText(contenido)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        Intent intent = new Intent(this, JsonActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Crear pending intent
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

    // Asignar intent y establecer true para notificar como aviso
                builder.setFullScreenIntent(fullScreenPendingIntent, true);
        NotificationManager notificationManager =
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, builder.build());
    }

    public void notificacion2(){

        Intent dismissIntent = new Intent("com.example.cancel");
        //getBaseContext().sendBroadcast(dismissIntent);
        Bundle extras = new Bundle();
        extras.putInt("notification",1);
        dismissIntent.putExtras(extras);
        //dismissIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent dismissIntent2 = PendingIntent.getBroadcast(this,0, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action actions[] = new NotificationCompat.Action[1];
        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder)
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.warning)
                        .setContentTitle("My notification")
                        .addAction(R.drawable.error, "Dismiss", dismissIntent2)
                        .setContentText("Hello World!");

        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();
        String[] events = new String[6];
// Sets a title for the Inbox in expanded layout
        inboxStyle.setBigContentTitle("My notification");
// Moves events into the expanded layout
        for (int i=0; i < events.length; i++) {

            inboxStyle.addLine("Hola Mundo!");
        }
// Moves the expanded layout object into the notification object.
        mBuilder.setStyle(inboxStyle);

        Intent resultIntent = new Intent(this, JsonActivity.class);
// Because clicking the notification opens a new ("special") activity, there's
// no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setAutoCancel(true);
        // Sets an ID for the notification
        int mNotificationId = 001;
// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    public void notification3(final int id, int iconId, String titulo, String contenido) {
        int numero = 0;
        final NotificationCompat.Builder builder = (NotificationCompat.Builder)
                new NotificationCompat.Builder(this)
                        .setSmallIcon(iconId)
                        .setContentTitle(titulo)
                        .setContentText(contenido).setNumber(numero++);


        final NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        int i;

                        // Ciclo para la simulación de progreso
                        for (i = 0; i <= 100; i += 5) {
                            // Setear 100% como medida máxima
                            builder.setProgress(100, i, false);
                            // Emitir la notificación
                            mNotifyMgr.notify(id, builder.build());
                            // Retardar la ejecución del hilo
                            try {
                                // Retardo de 1s
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                Log.d("", "Falló sleep(1000) ");
                            }
                        }

                    /*
                    ACTUALIZACIÓN DE LA NOTIFICACION
                     */

                        // Desplegar mensaje de éxito al terminar
                        builder.setContentText("Sincronización Completa")
                                // Quitar la barra de progreso
                                .setProgress(0, 0, false);
                        mNotifyMgr.notify(id, builder.build());
                    }
                }

        ).start();

    }

    public void notificacion4(){

        final NotificationCompat.Builder mBuilder;
        final NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("Picture Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.ecology);
// Start a lengthy operation in a background thread
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        int incr;
                        // Do the "lengthy" operation 20 times
                        for (incr = 0; incr <= 100; incr+=5) {
                            // Sets the progress indicator to a max value, the
                            // current completion percentage, and "determinate"
                            // state
                            mBuilder.setProgress(100, incr, false);
                            // Displays the progress bar for the first time.
                            mNotifyMgr.notify(0, mBuilder.build());
                            // Sleeps the thread, simulating an operation
                            // that takes time
                            try {
                                // Sleep for 5 seconds
                                Thread.sleep(5*1000);
                            } catch (InterruptedException e) {
                                Log.d("", "sleep failure");
                            }
                        }
                        // When the loop is finished, updates the notification
                        mBuilder.setContentText("Download complete")
                                // Removes the progress bar
                                .setProgress(0,0,false);
                        mNotifyMgr.notify(5, mBuilder.build());
                    }
                }
// Starts the thread by calling the run() method in its Runnable
        ).start();
    }

    public void notifation5(){
        // Constructs the Builder object.
        NotificationCompat.Builder builder = (NotificationCompat.Builder)
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ecology)
                        .setContentTitle("Titulo")
                        .setContentText("Content")
                        .setDefaults(Notification.DEFAULT_ALL); // requires VIBRATE permission
        /*
         * Sets the big view "big text" style and supplies the
         * text (the user's reminder message) that will be displayed
         * in the detail area of the expanded notification.
         * These calls are ignored by the support library for
         * pre-4.1 devices.
         */

        Intent resultIntent = new Intent(this, JsonActivity.class);
        resultIntent.putExtra("Here", "MSG");
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);

// Because clicking the notification launches a new ("special") activity,
// there's no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        Intent dismissIntent = new Intent(this, JsonActivity.class);
        PendingIntent piDismiss = PendingIntent.getService(this, 0, dismissIntent, 0);

        Intent snoozeIntent = new Intent(this, JsonActivity.class);
        PendingIntent piSnooze = PendingIntent.getService(this, 0, snoozeIntent, 0);

// This sets the pending intent that should be fired when the user clicks the
// notification. Clicking the notification launches a new activity.
        builder.setContentIntent(resultPendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("MSG"))
                .addAction (R.drawable.ecology,
                        "Dismiss", piDismiss)
                .addAction (R.drawable.ecology,
                        "Snooze", piSnooze);
    }
}

