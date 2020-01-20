package com.example.blarasig.projet_942;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;



public class PrendrePhoto extends AppCompatActivity implements SurfaceHolder.Callback {
    private Button monBouton;
    private TextView helloworld;
    private Camera camera;
    private SurfaceView surfaceCamera;
    private Boolean isPreview;
    private String dataImage;


    //POUR ENVOYER AU SERV
    URL connectURL;
    String responseString;
    String Title;
    String Description;
    byte[ ] dataToServer;
    String iFileName;
    FileInputStream fileInputStream = null;






    private FileOutputStream stream;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Nous mettons l'application en plein écran et sans barre de titre
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        isPreview = false;
        System.out.println("debut prog");




        // Nous appliquons notre layout
        setContentView(R.layout.activity_prendre_photo);
        surfaceCamera = (SurfaceView) findViewById(R.id.surfaceViewCamera);
        // Méthode d'initialisation de la caméra
        InitializeCamera();

        monBouton = (Button) findViewById(R.id.button1);
        helloworld = (TextView)findViewById(R.id.tv1);

        monBouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO action à faire

                int visibility = helloworld.getVisibility();
                if(visibility == View.VISIBLE) {
                    helloworld.setVisibility(View.INVISIBLE);
                }
                else{
                    helloworld.setVisibility(View.VISIBLE);
                }
            }
        });

        surfaceCamera.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Nous prenons une photo
                if (camera != null) {

                    SavePicture();

                }

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_prendre_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void InitializeCamera() {
        // Nous attachons nos retours du holder à notre activité
        surfaceCamera.getHolder().addCallback(this);
        // Nous spécifiions le type du holder en mode SURFACE_TYPE_PUSH_BUFFERS
        surfaceCamera.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // Nous arrêtons la camera et nous rendons la main
        if (camera != null) {
            camera.stopPreview();
            isPreview = false;
            camera.release();
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // Nous prenons le contrôle de la camera
        if (camera == null)
            camera = Camera.open();
    }

    // Quand la surface change
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

        // Si le mode preview est lancé alors nous le stoppons
        if (isPreview) {
            camera.stopPreview();
        }
        // Nous récupérons les paramètres de la caméra
        Camera.Parameters parameters = camera.getParameters();

        // Nous changeons la taille
        parameters.setPreviewSize(width, height);

        // Nous appliquons nos nouveaux paramètres
        //camera.setParameters(parameters);

        try {
            // Nous attachons notre prévisualisation de la caméra au holder de la
            // surface
            camera.setPreviewDisplay(surfaceCamera.getHolder());
        } catch (IOException e) {
        }

        // Nous lançons la preview
        camera.startPreview();
        isPreview = true;
    }


    // Retour sur l'application
    @Override
    public void onResume() {
        super.onResume();
        camera = Camera.open();
    }

    // Mise en pause de l'application
    @Override
    public void onPause() {
        super.onPause();

        if (camera != null) {
            camera.release();
            camera = null;
        }
    }



    Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {

        public void onPictureTaken(byte[] data, Camera camera) {
            if (data != null) {
                // Enregistrement de votre image
                try {
                    if (stream != null) {
                        stream.write(data);
                        stream.flush();
                        stream.close();

                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }

                // Nous redémarrons la prévisualisation
                camera.startPreview();
            }
        }
    };


    public void EnvoiWeb() throws IOException, JSONException {


        Thread monThread = new Thread() {
            public void run() {
                try {

                    URL url = new URL("http://192.168.118.106/API.php");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("Cache-Control", "no-cache");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    dataImage="prout";
                    String urlParameters  = "fichier="+dataImage;


                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(urlParameters);

                    System.out.flush();
                    writer.flush();
                    writer.close();
                    os.close();

                    //conn.connect();
                    int responseCode = conn.getResponseCode();
                    System.out.println(responseCode);System.out.flush();
                    String response = "";
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        String line;
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        /*while ((line = br.readLine()) != null) {
                            response += line;
                        }*/
                    } else {
                        response = "";

                    }

                    //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG);
                    System.out.println(response);
                } catch (Exception e) {
                    Thread t = currentThread();
                    t.setUncaughtExceptionHandler((UncaughtExceptionHandler) e);
                }




            }};
        monThread.start();

    }


    void Sending(){


        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        String Tag="fSnd";
        try
        {
            connectURL = new URL("http://192.168.118.106/API.php");
            Log.e(Tag,"Starting Http File Sending to URL");

            // Open a HTTP connection to the URL
            HttpURLConnection conn = (HttpURLConnection)connectURL.openConnection();

            // Allow Inputs
            conn.setDoInput(true);

            // Allow Outputs
            conn.setDoOutput(true);

            // Don't use a cached copy.
            conn.setUseCaches(false);

            // Use a post method.
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Connection", "Keep-Alive");

            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"title\""+ lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(Title);
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"description\""+ lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(Description);
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + iFileName +"\"" + lineEnd);
            dos.writeBytes(lineEnd);

            Log.e(Tag,"Headers are written");

            // create a buffer of maximum size
            int bytesAvailable = fileInputStream.available();

            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[ ] buffer = new byte[bufferSize];

            // read file and write it into form...
            int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0)
            {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable,maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0,bufferSize);
            }
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // close streams
            fileInputStream.close();

            dos.flush();

            Log.e(Tag,"File Sent, Response: "+String.valueOf(conn.getResponseCode()));

            InputStream is = conn.getInputStream();

            // retrieve the response from server
            int ch;

            StringBuffer b =new StringBuffer();
            while( ( ch = is.read() ) != -1 ){ b.append( (char)ch ); }
            String s=b.toString();
            Log.i("Response",s);
            dos.close();
        }
        catch (MalformedURLException ex)
        {
            Log.e(Tag, "URL error: " + ex.getMessage(), ex);
        }

        catch (IOException ioe)
        {
            Log.e(Tag, "IO error: " + ioe.getMessage(), ioe);
        }
    }



    private void SavePicture() {
        try {
            SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss");
            String fileName = "photo_" + timeStampFormat.format(new Date())
                    + ".jpg";

            // Metadata pour la photo
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, fileName);
            values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
            values.put(MediaStore.Images.Media.DESCRIPTION, "Image prise par FormationCamera");
            values.put(MediaStore.Images.Media.DATE_TAKEN, new Date().getTime());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

            // Support de stockage
            Uri taken = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values);

            // Ouverture du flux pour la sauvegarde
            stream = (FileOutputStream) getContentResolver().openOutputStream(
                    taken);

            camera.takePicture(null, pictureCallback, pictureCallback);


            // L'image est enregistrée dans la galerie
            // Find the last picture
            String[] projection = new String[]{
                    MediaStore.Images.ImageColumns._ID,
                    MediaStore.Images.ImageColumns.DATA,
                    MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.ImageColumns.DATE_TAKEN,
                    MediaStore.Images.ImageColumns.MIME_TYPE
            };

            final Cursor cursor = getContentResolver()
                    .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
                            null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

// Put it in the image view
            if (cursor.moveToFirst()) {
                String imageLocation = cursor.getString(1);
                iFileName = imageLocation;
                File imageFile = new File(imageLocation);
                if (imageFile.exists()) {   // TODO: is there a better way to do this?
                    /*Bitmap bm = BitmapFactory.decodeFile(imageLocation);

                    ByteArrayOutputStream baos=new  ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG,100, baos);
                    byte [] b=baos.toByteArray();
                    String dataImage = Base64.encodeToString(b, Base64.DEFAULT);

                */



                }

            }
            //Sending();


            // Code d'envoi serveur
            try {
                EnvoiWeb();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            System.out.println(" MAD CHEH");
            Log.d("CREATION","lemessage");
        }
    }

}
