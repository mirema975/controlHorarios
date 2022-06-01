package com.example.horarioslaborales;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;


public class ActivityAgregar extends AppCompatActivity {

    public EditText inputIngresoHH, inputIngresoMM, inputSalidaHH, inputSalidaMM;
    public EditText inputIngresoHH2, inputIngresoMM2, inputSalidaHH2, inputSalidaMM2;
    public Spinner spinner_anio, spinner_mes, spinner_dia;

    Calendario calendario = Calendario.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);
        findView();
        setContentSpinners(calendario.aniosAL,spinner_anio,calendario.anioActual);
        setContentSpinners(calendario.mesesAL,spinner_mes,calendario.mesActual);
        setDiasSegunMes(spinner_mes);
    }

    public void findView() {
        inputIngresoHH = findViewById(R.id.input_hs_ingreso);
        inputIngresoHH.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "23")});
        inputIngresoMM = findViewById(R.id.input_min_ingreso);
        inputIngresoMM.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "59")});
        inputSalidaHH = findViewById(R.id.input_hs_salida);
        inputSalidaHH.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "23")});
        inputSalidaMM =  findViewById(R.id.input_min_salida);
        inputSalidaMM.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "59")});

        inputIngresoHH2 = findViewById(R.id.input_hs_ingreso2);
        inputIngresoHH2.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "23")});
        inputIngresoMM2 = findViewById(R.id.input_min_ingreso2);
        inputIngresoMM2.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "59")});
        inputSalidaHH2 = findViewById(R.id.input_hs_salida2);
        inputSalidaHH2.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "23")});
        inputSalidaMM2 = findViewById(R.id.input_min_salida2);
        inputSalidaMM2.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "59")});

        spinner_anio = findViewById(R.id.spinner_anio);
        spinner_mes = findViewById(R.id.spinner_mes);
        spinner_dia = findViewById(R.id.spinner_dia);
    }

    public void setIngresoActual(View view){
        inputIngresoHH.setText(calendario.horaActualStr());
        inputIngresoMM.setText(calendario.minActualStr());
    }

    public void setSalidaActual(View view){
        inputSalidaHH.setText(calendario.horaActualStr());
        inputSalidaMM.setText(calendario.minActualStr());
    }
    public void setIngresoActual2(View view){
        inputIngresoHH2.setText(calendario.horaActualStr());
        inputIngresoMM2.setText(calendario.minActualStr());
    }

    public void setSalidaActual2(View view){
        inputSalidaHH2.setText(calendario.horaActualStr());
        inputSalidaMM2.setText(calendario.minActualStr());
    }


    public void setDiasSegunMes(Spinner sp) {
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (sp.getSelectedItem().equals(2)) {
                    setContentSpinners(calendario.dias28AL,spinner_dia,calendario.diaActual);
                } else if (sp.getSelectedItem().equals(4)
                        || sp.getSelectedItem().equals(6)
                        || sp.getSelectedItem().equals(9)
                        || sp.getSelectedItem().equals(11)) {
                    setContentSpinners(calendario.dias30AL,spinner_dia,calendario.diaActual);
                } else {
                    setContentSpinners(calendario.dias31AL,spinner_dia,calendario.diaActual);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    public void setContentSpinners(ArrayList<Integer> arrl,Spinner sp,Integer valueDefault){
        ArrayAdapter <Integer> arrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, arrl);
        sp.setAdapter(arrayAdapter);
        int spinnerPosition = arrayAdapter.getPosition(valueDefault);
        sp.setSelection(spinnerPosition);
    }

    public void agregar(View view){
        if(inputIngresoHH.getText().toString().isEmpty()){
            Toast.makeText(this,"Debes colocar el ingreso para agregar una fecha",Toast.LENGTH_LONG).show();
            return;
        }
        if(!ingresoMayorSalida(inputIngresoHH2,inputSalidaHH)) {
            Toast.makeText(this,"El ingreso del doble turno debe ser menor que la salida",Toast.LENGTH_LONG).show();
            return;
        }
        if(!inputIngresoHH2.getText().toString().isEmpty()){
            if (Integer.parseInt(inputIngresoHH2.getText().toString()) == 0){
                Toast.makeText(this,"El ingreso del doble turno debe ser menor que 00",Toast.LENGTH_LONG).show();
                return;
            }
        }
        int anio_ingresado_int = Integer.parseInt(spinner_anio.getSelectedItem().toString());
        int mes_ingresado_int = Integer.parseInt(spinner_mes.getSelectedItem().toString());
        int dia_ingresado_int = Integer.parseInt(spinner_dia.getSelectedItem().toString());

        ContentValues registro = new ContentValues();

        registro.put("anio", anio_ingresado_int);
        registro.put("mes", mes_ingresado_int);
        registro.put("dia", dia_ingresado_int);

        addRegistro(inputIngresoHH,"ingreso1_HH", registro);
        addRegistro(inputIngresoMM,"ingreso1_MM", registro);
        addRegistro(inputSalidaHH,"salida1_HH", registro);
        addRegistro(inputSalidaMM,"salida1_MM", registro);
        addRegistro(inputIngresoHH2,"ingreso2_HH", registro);
        addRegistro(inputIngresoMM2,"ingreso2_MM", registro);
        addRegistro(inputSalidaHH2,"salida2_HH", registro);
        addRegistro(inputSalidaMM2,"salida2_MM", registro);

        if(!existeEnBaseDatos(dia_ingresado_int,mes_ingresado_int,anio_ingresado_int)){
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion",null,1);
            SQLiteDatabase baseDatos = admin.getWritableDatabase();
            baseDatos.insert("horarios",null,registro);
            baseDatos.close();
            Toast.makeText(this, "Horario Agregado Correctamente", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        } else {
            Toast.makeText(this, "El dia ingresado ya existe", Toast.LENGTH_SHORT).show();
        }
    }

    private void addRegistro(EditText et, String t, ContentValues r){
        if(!campoVacio(et)){
            int input = Integer.parseInt(et.getText().toString());
            r.put(t, input);
        }
    }

    public boolean ingresoMayorSalida(EditText i, EditText s){
        if(!campoVacio(i) && !campoVacio(s)){
            return Integer.parseInt(i.getText().toString()) >= Integer.parseInt(s.getText().toString()) || Integer.parseInt(i.getText().toString()) == 0;
        }
        return true;
    }

    public boolean existeEnBaseDatos(int d, int m, int a){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion",null,1);
        SQLiteDatabase baseDatos = admin.getReadableDatabase();
        Cursor fila = baseDatos.rawQuery("select anio, mes, dia from horarios where dia = " + d + " and mes = " + m + " and anio = " + a,null);
        return fila.moveToFirst();
    }

    public boolean campoVacio(EditText et){
        return et.getText().toString().length() == 0;
    }
}