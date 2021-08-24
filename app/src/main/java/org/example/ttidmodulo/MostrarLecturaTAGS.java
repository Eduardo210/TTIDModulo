package org.example.ttidmodulo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
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
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static org.example.ttidmodulo.R.color.*;

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
        ListAdapter adapterTAGS = new ListAdapter(MostrarLecturaTAGS.this, CodeTAG, TipoISO,ExaCodeTag);
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapterTAGS);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tag = ExaCodeTag.get(position);
                helpes h = new helpes();

                if (h.VerificarConexionInternet(MostrarLecturaTAGS.this)){
                    ejecutarCLase();
                }else{
                    Toast.makeText(MostrarLecturaTAGS.this, "Verifica tu conexión a Internet", Toast.LENGTH_SHORT).show();
                }

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
        String MensajeId, Mensaje, UsuarioId, listaTag,
                Tag, Color, Domicilio, Estatus, Holograma, Marca,
                Modelo, Placas, Tipo, Usuario;


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
                request.addProperty("usuarioNombreWCF", Global.usuarioWCF);
                request.addProperty("passwordWCF", Global.passwordWCF + formattedDate);
                request.addProperty("tag_id", tag);

                //Version Soap
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                envelope.setOutputSoapObject(request);
                envelope.dotNet = true;
                HttpTransportSE androidHttpTransport = new HttpTransportSE(Global.URL_DOMAIN);

                //Call the webservice
                androidHttpTransport.call(Global.SOAP_ACTION + Global.METHOD_QUERY_TAG, envelope);

                // Get the result
                if (envelope.bodyIn instanceof SoapFault)
                {
                    final SoapFault sf = (SoapFault) envelope.bodyIn;

                }else {
                    result = (SoapObject) envelope.bodyIn;
                    SoapObject body = (SoapObject) result.getProperty(0);
                     MensajeId = body.getProperty("_1_MensajeId").toString();
                     Mensaje = body.getProperty("_2_Mensaje").toString();
                     UsuarioId = body.getProperty("_3_UsuarioId").toString();
                     SoapObject listag = (SoapObject) body.getProperty("listag");
                    for (int i = 0; i < listag.getPropertyCount(); i++) {
                        SoapObject elementoRespuesta = (SoapObject) listag.getProperty(i);
                        Color = elementoRespuesta.getProperty("Color").toString();
                        Domicilio = elementoRespuesta.getProperty("Domicilio").toString();
                        Estatus = elementoRespuesta.getProperty("Estatus").toString();
                        Holograma = elementoRespuesta.getProperty("Holograma").toString();
                        Marca = elementoRespuesta.getProperty("Marca").toString();
                        Modelo = elementoRespuesta.getProperty("Modelo").toString();
                        Placas = elementoRespuesta.getProperty("Placas").toString();
                        Tipo = elementoRespuesta.getProperty("Tipo").toString();
                        Usuario = elementoRespuesta.getProperty("Usuario").toString();

                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return result.toString();
        }

        @SuppressLint("ResourceAsColor")
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            int id2;
            if (result != null) {
                if (MensajeId.equals("OK") || MensajeId == "OK") {
                    id2 = 128;
                }else{
                    id2 = 127;
                }

                if (id2 <=127) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MostrarLecturaTAGS.this);
                    builder.setTitle("Numero de TAG " + tag);
                    builder.setMessage("El TAG seleccionado no tiene registros en la base de datos. Favor de verificar.");
                    builder.setPositiveButton("Aceptar", null);

                    AlertDialog dialog = builder.create();
                    dialog.show();

                } else {
//                    LinearLayout layout = new LinearLayout(MostrarLecturaTAGS.this);
//                    layout.setOrientation(LinearLayout.VERTICAL);

//                    final TextView tagId = new TextView(MostrarLecturaTAGS.this);
//                    tagId.setTextSize(28);
//                    tagId.setTextColor(Color.BLUE);
//                    tagId.setPadding(115, 0, 15, 0);
//                    tagId.setText(Tag_Id );
//                    layout.addView(tagId);
//
//                    final TextView estatus1 = new TextView(MostrarLecturaTAGS.this);
//                    estatus1.setTextSize(18);
//                    estatus1.setTextColor(Color.BLACK);
//                    estatus1.setPadding(250, 30, 15, 0);
//                    estatus1.setText("Estatus: ");
//                    layout.addView(estatus1);
//
//                    final TextView estatus = new TextView(MostrarLecturaTAGS.this);
//                    estatus.setTextSize(28);
//                    estatus.setPadding(200, 0, 15, 0);
//                    estatus.setText(Estatus);
//                    if (Estatus.equals("ACTIVO") || Estatus == "ACTIVO"){
//                        estatus.setTextColor(Color.GREEN);
//                    }else{
//                        estatus.setTextColor(Color.RED);
//                    }
//                    layout.addView(estatus);
//
//
//                    final TextView Tagtipo = new TextView(MostrarLecturaTAGS.this);
//                    Tagtipo.setTextSize(18);
//                    Tagtipo.setTextColor(Color.BLACK);
//                    Tagtipo.setPadding(250, 30, 15, 0);
//                    Tagtipo.setText("TAG Tipo: ");
//                    layout.addView(Tagtipo);
//
//                    final TextView Tagtipo1 = new TextView(MostrarLecturaTAGS.this);
//                    Tagtipo1.setTextSize(28);
//                    Tagtipo1.setTextColor(Color.BLACK);
//                    Tagtipo1.setPadding(200, 0, 15, 0);
//                    Tagtipo1.setText(Tipo);
//                    layout.addView(Tagtipo1);
//
//                    final TextView TagSaldo = new TextView(MostrarLecturaTAGS.this);
//                    TagSaldo.setTextSize(18);
//                    TagSaldo.setTextColor(Color.BLACK);
//                    TagSaldo.setPadding(270, 30, 15, 0);
//                    TagSaldo.setText("Saldo");
//                    layout.addView(TagSaldo);
//
//                    final TextView TagSaldo1 = new TextView(MostrarLecturaTAGS.this);
//                    TagSaldo1.setTextSize(32);
//                    TagSaldo1.setTextColor(Color.BLACK);
//                    TagSaldo1.setPadding(200, 0, 15, 0);
//                    TagSaldo1.setText("$ " + Tag_Saldo);
//                    layout.addView(TagSaldo1);

                    final Dialog dialogPersonalizado = new Dialog(MostrarLecturaTAGS.this);
                    dialogPersonalizado.setContentView(R.layout.dialogo_password_ajustes);
                    TextView es = dialogPersonalizado.findViewById(R.id.txtEst);
                    if (Estatus.equals("Activo") || Estatus == "Activo"){
                        es.setText("ACEPTADO");

                    }else{
                        es.setText("DENEGADO");
                        es.setBackgroundColor(errorRed);
                    }

                    TextView Placa = dialogPersonalizado.findViewById(R.id.txtPlaca);
                    if (Placas.equals("anyType{}") || Placas=="anyType{}"){
                        Placas = "SIN ESPECIFICAR";
                    }
                    Placa.setText(Placas);

                    TextView domi = dialogPersonalizado.findViewById(R.id.txtDomicilio);
                    if (Domicilio.equals("anyType{}") || Domicilio=="anyType{}"){
                        Domicilio = "SIN ESPECIFICAR";
                    }
                    domi.setText(Domicilio);

                    TextView col = dialogPersonalizado.findViewById(R.id.txtColor);
                    if (Color.equals("anyType{}") || Color=="anyType{}"){
                        Color = "SIN ESPECIFICAR";
                    }
                    col.setText(Color);

                    TextView esta = dialogPersonalizado.findViewById(R.id.txtEstatus);
                    if (Estatus.equals("anyType{}") || Estatus=="anyType{}"){
                        Estatus = "SIN ESPECIFICAR";
                    }
                    esta.setText(Estatus);


                    TextView holo = dialogPersonalizado.findViewById(R.id.txtHolograma);
                    if (Holograma.equals("anyType{}") || Holograma=="anyType{}"){
                        Holograma = "SIN ESPECIFICAR";
                    }
                    holo.setText(Holograma);

                    TextView mar = dialogPersonalizado.findViewById(R.id.txtMarca);
                    if (Marca.equals("anyType{}") || Marca=="anyType{}"){
                        Marca = "SIN ESPECIFICAR";
                    }
                    mar.setText(Marca);

                    TextView model = dialogPersonalizado.findViewById(R.id.txtModelo);
                    if (Modelo.equals("anyType{}") || Modelo=="anyType{}"){
                        Modelo = "SIN ESPECIFICAR";
                    }
                    model.setText(Modelo);

                    TextView tip = dialogPersonalizado.findViewById(R.id.txtTipo);
                    if (Tipo.equals("anyType{}") || Tipo=="anyType{}"){
                        Tipo = "SIN ESPECIFICAR";
                    }
                    tip.setText(Tipo);

                    dialogPersonalizado.show();

                }
            }
        }

    }


}