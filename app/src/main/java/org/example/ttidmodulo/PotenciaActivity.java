package org.example.ttidmodulo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.renderscript.Sampler;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



import com.ttid.VariablesGlobales;

public class PotenciaActivity extends Activity {
    private Spinner sp1,sp2;
    private Button bt1;
    private EditText et_pot6c, et_pot6b, et_tiem6c, et_tiem6b;


    String [] Formatos6B={"Formato Simple","Formato Telepeaje MX","RAW"};
    String [] Formatos6C={"Formato Simple","Formato Telepeaje MX","Formato Frontera MX", "Formato Telepeaje GT"};
    String [] FormatosAuto={"Formato Simple","Formato Telepeaje MX"};
    ArrayAdapter<String> adapterformat6B;
    ArrayAdapter<String> adapterFormat6C;
    ArrayAdapter<String> adapterFormatAuto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_potencia);

        sp1 = findViewById(R.id.spin_1);
        String [] trigop = {"6B", "6C", "AUTO"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item_45, trigop);
        sp1.setAdapter(adapter);

        sp2=findViewById(R.id.spin_2);
        adapterformat6B=new ArrayAdapter<>(this,R.layout.spinner_item_45,Formatos6B);
        adapterFormat6C=new ArrayAdapter<>(this,R.layout.spinner_item_45,Formatos6C);
        adapterFormatAuto=new ArrayAdapter<>(this,R.layout.spinner_item_45,FormatosAuto);

        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position==VariablesGlobales.Mode_6C){//modo de operacion 6C
                    sp2.setAdapter(adapterFormat6C);
                    sp2.setSelection(Variables_globales.Formato);
                }else if(position==VariablesGlobales.Mode_6B){
                    sp2.setAdapter(adapterformat6B);
                    if(Variables_globales.Formato>2){
                        sp2.setSelection(0);
                    }else{
                        sp2.setSelection(Variables_globales.Formato);
                    }
                }else if(position==VariablesGlobales.Mode_Double_Auto){
                    sp2.setAdapter(adapterFormatAuto);
                    if(Variables_globales.Formato<=VariablesGlobales.PEAJE_MX){
                        sp2.setSelection(Variables_globales.Formato);
                    }else{
                        sp2.setSelection(0);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Inicializar();
        Cargarpreferencias();
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validarconfig();
            }
        });
    }

    private void Inicializar(){

        bt1 = findViewById(R.id.btn_save);
        et_pot6b = findViewById(R.id.et_pot6b);
        et_pot6c = findViewById(R.id.et_pot6c);
        et_tiem6b = findViewById(R.id.et_tiem6b);
        et_tiem6c = findViewById(R.id.et_tiem6c);

    }

    private void Validarconfig(){
        String pot6b = et_pot6b.getText().toString();
        String pot6c = et_pot6c.getText().toString();
        String tiemp6b = et_tiem6b.getText().toString();
        String tiemp6c = et_tiem6c.getText().toString();
        if(pot6b.length()==0|| pot6c.length()==0||tiemp6b.length()==0||tiemp6c.length()==0){
            Toast.makeText(this, "Complete todos los datos para continuar", Toast.LENGTH_SHORT).show();
            return;
        }
        if(Integer.valueOf(pot6b)<10 ||Integer.valueOf(pot6b)>=34){
            Toast.makeText(this, "Potencia de 6B debe estar entre 10-33 dBm", Toast.LENGTH_SHORT).show();
            return;
        }
        if(Integer.valueOf(pot6c)<10 ||Integer.valueOf(pot6c)>=34){
            Toast.makeText(this, "Potencia de 6C debe estar entre 10-33 dBm", Toast.LENGTH_SHORT).show();
            return;
        }
        if(Integer.valueOf(tiemp6b)<500||Integer.valueOf(tiemp6b)>3000){
            Toast.makeText(this,"Tiempo de 6B debe estar entre 500-3000 milisegundos",Toast.LENGTH_SHORT).show();
            return;
        }
        if(Integer.valueOf(tiemp6c)<500||Integer.valueOf(tiemp6c)>3000){
            Toast.makeText(this,"Tiempo de 6C debe estar entre 500-3000 milisegundos",Toast.LENGTH_SHORT).show();
            return;
        }
        Guardarpreferencias();
        Cargarpreferencias();



       // Variables_globales.seleccion = sp1.getSelectedItemPosition() + 1;
       // Toast.makeText(this, "Guardado", Toast.LENGTH_LONG).show();
        /*Intent data = new Intent(this, MainActivity.class);
        String seleccion = sp1.getSelectedItem().toString();
        Bundle bundle1 = new Bundle();
        bundle1.putString("seleccionkey", seleccion);
        data.putExtras(bundle1);
        startActivity(data);*/
        PotenciaActivity.this.finish();

    }

    public void Cargarpreferencias(){
        SharedPreferences preferences1 = getSharedPreferences("configuracion", Context.MODE_PRIVATE);
        String pot6b = preferences1.getString("pwr6b", "30");
        String pot6c = preferences1.getString("pwr6c", "27");
        String time6b = preferences1.getString("time6b", "500");
        String time6c = preferences1.getString("time6c", "500");
        int seleccion1 = preferences1.getInt("seleccion",Variables_globales.seleccion);
        int formato1=preferences1.getInt("formato", Variables_globales.Formato);

        Variables_globales.Power6B = Integer.valueOf(pot6b);
        et_pot6b.setText(String.valueOf(Variables_globales.Power6B));
        Variables_globales.Power6C = Integer.valueOf(pot6c);
        et_pot6c.setText(String.valueOf(Variables_globales.Power6C));
        Variables_globales.Time6B = Integer.valueOf(time6b);
        et_tiem6b.setText(String.valueOf(Variables_globales.Time6B));
        Variables_globales.Time6C = Integer.valueOf(time6c);
        et_tiem6c.setText(String.valueOf(Variables_globales.Time6C));

        Variables_globales.seleccion = seleccion1;
        Variables_globales.Formato=formato1;

      /*  if(seleccion1==2){//si el modo de operacion es lectura 6C
            sp2.setAdapter(adapterFormat6C);
        }else{
            sp2.setAdapter(adapterformat6B);
        }*/

       sp1.setSelection(Variables_globales.seleccion);

    }

    public void Guardarpreferencias(){
        SharedPreferences preferences1 = getSharedPreferences("configuracion", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor1 = preferences1.edit();

        editor1.putString("pwr6b",et_pot6b.getText().toString());
        editor1.putString("pwr6c",et_pot6c.getText().toString());
        editor1.putString("time6b",et_tiem6b.getText().toString());
        editor1.putString("time6c",et_tiem6c.getText().toString());
        editor1.putInt("seleccion",sp1.getSelectedItemPosition());
        editor1.putInt("formato",sp2.getSelectedItemPosition());
        editor1.commit();

    }
}
