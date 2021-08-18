package org.example.ttidmodulo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MostrarLecturaTAGS extends Activity {

    private ListView list;
    ArrayList<String> TipoISO = new ArrayList();
    ArrayList<String> Errores = new ArrayList();
    ArrayList<String> CodeTAG = new ArrayList();
    ArrayList<String> ExaCodeTag = new ArrayList();
    ArrayList<String> saldo = new ArrayList();
    Button btnback;
    SoapObject result;
    String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_lectura_t_a_g_s);

        CodeTAG = (ArrayList<String>) getIntent().getStringArrayListExtra("codetag");
        TipoISO = (ArrayList<String>) getIntent().getStringArrayListExtra("tipoiso");
        ExaCodeTag = (ArrayList<String>) getIntent().getStringArrayListExtra("exacodetag");
        ListAdapter adapterTAGS = new ListAdapter(MostrarLecturaTAGS.this, CodeTAG, TipoISO);
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapterTAGS);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tag = CodeTAG.get(position);
                ejecutarCLase();
            }
        });
        btnback = findViewById(R.id.btnBack);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void ejecutarCLase() {
        new SetTreasureBoxAsyncTask().execute();
    }


    private class SetTreasureBoxAsyncTask extends AsyncTask<String, String, String> {
        String Id, Mensaje, Clase_Id, Estatus, Operador_Id,
                Tag_Clase, TagFAlta, Tag_FID, Tag_FModificacion,
                Tag_Id, Tag_Saldo, Tipo;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {


            try {
                SoapObject request = new SoapObject(Global.NAMESPACE, Global.METHOD_QUERY_TAG);

                //Formato de Fecha
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
                String formattedDate = df.format(c.getTime());

                //Parameters
                request.addProperty("usuarioWCF", Global.usuarioWCF);
                request.addProperty("passwordWCF", Global.passwordWCF + formattedDate);
                request.addProperty("NumTag", tag);

                //Version Soap
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                envelope.setOutputSoapObject(request);
                envelope.dotNet = true;
                HttpTransportSE androidHttpTransport = new HttpTransportSE(Global.URL_DOMAIN);

                //Call the webservice
                androidHttpTransport.call(Global.SOAP_ACTION + Global.METHOD_QUERY_TAG, envelope);

                // Get the result
                result = (SoapObject) envelope.bodyIn;
                SoapObject body = (SoapObject) result.getProperty(0);
                Id = body.getProperty("Id").toString();
                Mensaje = body.getProperty("Mensaje").toString();
                SoapObject bodyTag = (SoapObject) body.getProperty("Tag");
                Clase_Id = bodyTag.getProperty("Clase_Id").toString();
                Estatus = bodyTag.getProperty("Estatus").toString();
                Operador_Id = bodyTag.getProperty("Operador_Id").toString();
                Tag_Clase = bodyTag.getProperty("Tag_Clase").toString();
                TagFAlta = bodyTag.getProperty("Tag_FAlta").toString();
                Tag_FID = bodyTag.getProperty("Tag_FID").toString();
                Tag_FModificacion = bodyTag.getProperty("Tag_FModificacion").toString();
                Tag_Id = bodyTag.getProperty("Tag_Id").toString();
                Tag_Saldo = bodyTag.getProperty("Tag_Saldo").toString();
                Tipo = bodyTag.getProperty("Tipo").toString();
                if (Tag_Saldo.isEmpty() || Tag_Saldo == null) {
                    saldo.add("0.00");
                } else {
                    saldo.add(Tag_Saldo);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                int id2 = Integer.parseInt(Id);
                if (id2 <=127) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MostrarLecturaTAGS.this);
                    builder.setTitle("Numero de TAG " + tag);
                    builder.setMessage("El TAG seleccionado no tiene registros en la base de datos. Favor de verificar.");
                    builder.setPositiveButton("Aceptar", null);

                    AlertDialog dialog = builder.create();
                    dialog.show();

                } else {
                    LinearLayout layout = new LinearLayout(MostrarLecturaTAGS.this);
                    layout.setOrientation(LinearLayout.VERTICAL);

                    final TextView tagId = new TextView(MostrarLecturaTAGS.this);
                    tagId.setTextSize(28);
                    tagId.setTextColor(Color.BLUE);
                    tagId.setPadding(115, 0, 15, 0);
                    tagId.setText(Tag_Id );
                    layout.addView(tagId);

                    final TextView estatus1 = new TextView(MostrarLecturaTAGS.this);
                    estatus1.setTextSize(18);
                    estatus1.setTextColor(Color.BLACK);
                    estatus1.setPadding(250, 30, 15, 0);
                    estatus1.setText("Estatus: ");
                    layout.addView(estatus1);

                    final TextView estatus = new TextView(MostrarLecturaTAGS.this);
                    estatus.setTextSize(28);
                    estatus.setPadding(200, 0, 15, 0);
                    estatus.setText(Estatus);
                    if (Estatus.equals("ACTIVO") || Estatus == "ACTIVO"){
                        estatus.setTextColor(Color.GREEN);
                    }else{
                        estatus.setTextColor(Color.RED);
                    }
                    layout.addView(estatus);


                    final TextView Tagtipo = new TextView(MostrarLecturaTAGS.this);
                    Tagtipo.setTextSize(18);
                    Tagtipo.setTextColor(Color.BLACK);
                    Tagtipo.setPadding(250, 30, 15, 0);
                    Tagtipo.setText("TAG Tipo: ");
                    layout.addView(Tagtipo);

                    final TextView Tagtipo1 = new TextView(MostrarLecturaTAGS.this);
                    Tagtipo1.setTextSize(28);
                    Tagtipo1.setTextColor(Color.BLACK);
                    Tagtipo1.setPadding(200, 0, 15, 0);
                    Tagtipo1.setText(Tipo);
                    layout.addView(Tagtipo1);

                    final TextView TagSaldo = new TextView(MostrarLecturaTAGS.this);
                    TagSaldo.setTextSize(18);
                    TagSaldo.setTextColor(Color.BLACK);
                    TagSaldo.setPadding(270, 30, 15, 0);
                    TagSaldo.setText("Saldo");
                    layout.addView(TagSaldo);

                    final TextView TagSaldo1 = new TextView(MostrarLecturaTAGS.this);
                    TagSaldo1.setTextSize(32);
                    TagSaldo1.setTextColor(Color.BLACK);
                    TagSaldo1.setPadding(200, 0, 15, 0);
                    TagSaldo1.setText("$ " + Tag_Saldo);
                    layout.addView(TagSaldo1);

                    final AlertDialog.Builder builder = new AlertDialog.Builder(MostrarLecturaTAGS.this);
                    builder
                            .setTitle("           Numero de TAG: ")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    final AlertDialog alertDialogPersonalizado = builder.create();
                    alertDialogPersonalizado.setView(layout);
// después mostrarla:
                    alertDialogPersonalizado.show();

                }
            }
        }

    }


}