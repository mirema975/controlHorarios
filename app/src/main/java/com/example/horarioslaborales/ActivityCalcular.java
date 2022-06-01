package com.example.horarioslaborales;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityCalcular extends AppCompatActivity {

    private TextView tv_horasTotales, tv_aCobrar;
    private Spinner spinner_mesSeleccionado;
    private EditText montoXHora;
    Calendario calendario = Calendario.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calcular);
        montoXHora = (EditText) findViewById(R.id.et_montoHora);
        tv_horasTotales = (TextView) findViewById(R.id.tv_horasTotalesMes);
        tv_aCobrar = (TextView) findViewById(R.id.tv_montoACobrar);
        spinner_mesSeleccionado = (Spinner) findViewById(R.id.spinner_mesSeleccionado);
        setContentSpinners(calendario.meses,spinner_mesSeleccionado);
    }

    public void calcular(View view) {
        if(montoXHora.getText().toString().isEmpty()){
            Toast.makeText(this, "Debes ingresar un monto", Toast.LENGTH_SHORT).show();
            return;
        }
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion",null,1);
        SQLiteDatabase baseDatos = admin.getReadableDatabase();
        Cursor fila = baseDatos.rawQuery("select ingreso1_HH, ingreso1_MM,salida1_HH,salida1_MM,ingreso2_HH,ingreso2_MM,salida2_HH,salida2_MM from horarios where mes =  "
                        + (spinner_mesSeleccionado.getSelectedItemPosition() + 1),
                null);
        double horasTotales = 0;
        while(fila.moveToNext()) {
            if(fila.getString(2) != null) {
                horasTotales += difHoras(fila.getInt(0), fila.getInt(1), fila.getInt(2), fila.getInt(3));
                horasTotales += difMin(fila.getInt(1), fila.getInt(3));
            }
            if(fila.getString(6) != null) {
                horasTotales += difHoras(fila.getInt(4), fila.getInt(5), fila.getInt(6), fila.getInt(7));
                horasTotales += difMin(fila.getInt(5), fila.getInt(7));
            }
        }
        tv_horasTotales.setText("Cantidad de horas: " + Math.round(horasTotales * 100.0) / 100.0);
        double montoACobrar = (Math.round(horasTotales * 100.0) / 100.0) * Integer.parseInt(montoXHora.getText().toString());
        tv_aCobrar.setText("Monto a cobrar: $ " + montoACobrar);
    }

    public double difMin(int im, int sm){
        if(sm < im){
            return (double) (sm - im + 60) / 60;
        }else {
            return (double) (sm - im) / 60;
        }
    }

    public double difHoras(int ih,int im, int sh, int sm){
        double diferenciaHoras = 0;
        if(sh < ih){
            diferenciaHoras += sh + 24 - ih;
        } else {
            diferenciaHoras += sh - ih;
        }
        if(sh == ih && sm < im){
            diferenciaHoras = 24;
        }
        if(sm < im){
            diferenciaHoras -= 1;
        }
        return diferenciaHoras;
    }

    public void setContentSpinners(Integer[] arrl,Spinner sp){
        ArrayAdapter <Integer> arrayAdapter = new ArrayAdapter<Integer>(this, R.layout.spinner_item, arrl);
        sp.setAdapter(arrayAdapter);
        sp.setSelection(calendario.mesActual - 1);
    }
}