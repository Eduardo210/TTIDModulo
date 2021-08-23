package org.example.ttidmodulo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ttid.LectorPeaje;
import com.ttid.PeajeListener;
import com.ttid.UHFApplication;

import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener, PeajeListener {

    //Widget
    public Button btn_read;
    Button btnmenu;
    public TextView tv_resul, tv_log, tv_log2;

    //Controlador del lector
    private LectorPeaje lector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Inicializar();

        InitReader(this);
        ((UHFApplication) getApplication()).addActivity(this);

        btnmenu = (Button) this.findViewById(R.id.btnBack);
        this.registerForContextMenu(btnmenu);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Configuración");
        menu.setHeaderIcon(R.drawable.setings);
        menu.add(0, 0, Menu.NONE, "Potencia y Protocolo");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                Intent intent = new Intent(this, PotenciaActivity.class);
                startActivity(intent);
                break;
            case 1:
                btnmenu.setText(item.getTitle().toString());
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    private void ClearText() {
        tv_resul.setText("");
        tv_log.setText("");
        tv_log2.setText("");
    }

    private void IniciarLectura() {
        ClearText();
        lector.LeerTagTelepeaje();
    }

    private void InitReader(Context context) {
        //Se crea instancia de objeto LectorPeaje
        lector = new LectorPeaje(context);
        //Se llama a la funcion para conectar internamente con el lector
        lector.Conectar();

        Cargarpreferencias();
        lector.ConfigurarLector(Variables_globales.Power6B, Variables_globales.Power6C, Variables_globales.Time6B, Variables_globales.Time6C, Variables_globales.seleccion, Variables_globales.Formato);

    }


    public void Cargarpreferencias() {
        SharedPreferences preferences1 = getSharedPreferences("configuracion", Context.MODE_PRIVATE);
        String pot6b = preferences1.getString("pwr6b", "30");
        String pot6c = preferences1.getString("pwr6c", "27");
        String time6b = preferences1.getString("time6b", "600");
        String time6c = preferences1.getString("time6c", "600");
        int seleccion1 = preferences1.getInt("seleccion", Variables_globales.seleccion);
        int formato1 = preferences1.getInt("formato", Variables_globales.Formato);

        Variables_globales.Power6B = Integer.valueOf(pot6b);
        Variables_globales.Power6C = Integer.valueOf(pot6c);
        Variables_globales.Time6B = Integer.valueOf(time6b);
        Variables_globales.Time6C = Integer.valueOf(time6c);
        Variables_globales.seleccion = seleccion1;
        Variables_globales.Formato = formato1;
    }

    @Override
    public void OnReadTagPeaje(String s) {
//        tv_resul.setText(s);
//        Intent intent = new Intent(getApplicationContext(), MostrarLecturaTAGS.class);
//        intent.putExtra("datos",s);
//        startActivity(intent);


        Log.e("FINAAAAL", s);
//            txtTag.setText(Tag.split("\\|")[0]+"  -  "+Tag.split("\\|")[1]);

//        txtTag.setText(Tag.replace("|","\r\n"));
        String[] elementos;
        elementos = s.split("\r\n");
//        txtTag.setText(elementos.toString());

        ArrayList<String> TipoISO = new ArrayList();
        ArrayList<String> Errores = new ArrayList();
        ArrayList<String> CodeTAG = new ArrayList();
        ArrayList<String> ExaCodeTag = new ArrayList();

        for (int i = 0; i < elementos.length; i++) {
            String elem = elementos[i];
            String[] partes;
            String parte;
            partes = elem.split("\\|");
            if (partes[1].equals("01") || partes[1] == "01") {
//                Toast.makeText(this, "Error en la lectura de algun TAG", Toast.LENGTH_SHORT).show();
            } else {
                if (partes.length < 4) {
//                    Toast.makeText(this, "Error en la lectura de algun TAG", Toast.LENGTH_SHORT).show();
                } else {
                    TipoISO.add(partes[0]);
                    Errores.add(partes[1]);
                    CodeTAG.add(partes[2]);
                    parte = partes[3].substring(8,24);

                    ExaCodeTag.add(parte);
                }
            }
        }
        if (TipoISO.isEmpty() || Errores.isEmpty() || CodeTAG.isEmpty() || ExaCodeTag.isEmpty()) {
//            Helpers helpers = new Helpers();
//            helpers.Aviso(MainActivity.this, "Datos Obtenidos",Tag);
        } else {
            Intent intent = new Intent(MainActivity.this, MostrarLecturaTAGS.class);
            intent.putStringArrayListExtra("codetag", CodeTAG);
            intent.putStringArrayListExtra("tipoiso", TipoISO);
            intent.putStringArrayListExtra("exacodetag", ExaCodeTag);
            startActivity(intent);
            tv_resul.setText("");

        }
    }

    @Override
    public void OnPeajeLogInformation(int i, String s) {
        if (i >= 21 && i <= 24) {
            tv_log.setText(s);
        } else {
            tv_log2.setText(s);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.potencia_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.config:
                Intent intent = new Intent(MainActivity.this, PotenciaActivity.class);
                startActivityForResult(intent, 1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            ClearText();
            lector.ConfigurarLector(Variables_globales.Power6B, Variables_globales.Power6C, Variables_globales.Time6B, Variables_globales.Time6C, Variables_globales.seleccion, Variables_globales.Formato);
        }
    }

    private void Inicializar() {
        tv_log = findViewById(R.id.tv_log);
        tv_log2 = findViewById(R.id.tv_log2);
        tv_resul = findViewById(R.id.tv_resul);
        btn_read = findViewById(R.id.btnrd);
        btn_read.setOnClickListener(this);

        tv_resul.setText("");
        tv_log.setText("");
        tv_log2.setText("");
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e("onKeyDown", "Code: " + keyCode + " Event: " + event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_F1) {

        } else if (keyCode == KeyEvent.KEYCODE_F4) {
            if (event.getRepeatCount() == 0) {
                tv_resul.setText("");
                IniciarLectura();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_F1) {

        } else if (keyCode == KeyEvent.KEYCODE_F4) {
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnrd:
                IniciarLectura();
                break;
        }
    }
}
