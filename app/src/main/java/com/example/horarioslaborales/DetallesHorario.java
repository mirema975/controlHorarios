package com.example.horarioslaborales;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DetallesHorario extends AppCompatActivity {

    private EditText inputIngresoHH, inputIngresoMM, inputSalidaHH, inputSalidaMM;
    private EditText inputIngresoHH2, inputIngresoMM2, inputSalidaHH2, inputSalidaMM2;
    private TextView tv_ht, tv_fecha;
    Calendario calendario = Calendario.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_horario);
        findView();
        setDatosInput();
        tv_fecha.setText(recibirDatos());
    }

    private void findView() {
        inputIngresoHH = findViewById(R.id.input_edit_ingreso_hs);
        inputIngresoHH.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "23")});
        inputIngresoMM = findViewById(R.id.input_edit_ingreso_min);
        inputIngresoMM.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "59")});
        inputSalidaHH = findViewById(R.id.input_edit_salida_hs);
        inputSalidaHH.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "23")});
        inputSalidaMM =  findViewById(R.id.input_edit_salida_min);
        inputSalidaMM.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "59")});

        inputIngresoHH2 = findViewById(R.id.input_edit_ingreso2_hs);
        inputIngresoHH2.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "23")});
        inputIngresoMM2 = findViewById(R.id.input_edit_ingreso2_min);
        inputIngresoMM2.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "59")});
        inputSalidaHH2 = findViewById(R.id.input_edit_salida2_hs);
        inputSalidaHH2.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "23")});
        inputSalidaMM2 = findViewById(R.id.input_edit_salida2_min);
        inputSalidaMM2.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "59")});

        tv_fecha = findViewById(R.id.label_fecha);
        tv_ht = findViewById(R.id.tv_horas_totales);
    }

    public String recibirDatos(){
        Intent intent = this.getIntent();
        Bundle extra = intent.getExtras();
        return extra.getString("fecha");
    }

    public void setDatosInput() {
        String valorRecibido = recibirDatos();
        String[] fechaArray = valorRecibido.split(" - ");
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion",null,1);
        SQLiteDatabase baseDatos = admin.getReadableDatabase();
        Cursor fila = baseDatos.rawQuery("select ingreso1_HH, ingreso1_MM,salida1_HH,salida1_MM,ingreso2_HH,ingreso2_MM,salida2_HH,salida2_MM from horarios where anio = "
                        + Integer.parseInt(fechaArray[2])
                        + " and mes = " + Integer.parseInt(fechaArray[1])
                        + " and dia = " + Integer.parseInt(fechaArray[0]),
                null);
        double horasTotales = 0;
        if(fila.moveToFirst()) {
            inputIngresoHH.setText(fila.getString(0));
            inputIngresoMM.setText(fila.getString(1));
            inputSalidaHH.setText(fila.getString(2));
            inputSalidaMM.setText(fila.getString(3));
            inputIngresoHH2.setText(fila.getString(4));
            inputIngresoMM2.setText(fila.getString(5));
            inputSalidaHH2.setText(fila.getString(6));
            inputSalidaMM2.setText(fila.getString(7));
            if(fila.getString(2) != null) {
                horasTotales += difHoras(fila.getInt(0), fila.getInt(1), fila.getInt(2), fila.getInt(3));
                horasTotales += difMin(fila.getInt(1), fila.getInt(3));
            }
            if(fila.getString(6) != null) {
                horasTotales += difHoras(fila.getInt(4), fila.getInt(5), fila.getInt(6), fila.getInt(7));
                horasTotales += difMin(fila.getInt(5), fila.getInt(7));
            }
        }
        tv_ht.setText("Cantidad de horas: " + Math.round(horasTotales * 100.0) / 100.0);
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

    public void setIngresoActual(View view){
        inputIngresoHH.setText(calendario.horaActualStr());
        inputIngresoMM.setText(calendario.minActualStr());
    }

    public void setSalidaActual(View view){
        inputSalidaHH.setText(calendario.horaActualStr());
        inputSalidaHH.requestFocus();
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

    public void guardar(View view){
        if(!ingresoMayorSalida(inputIngresoHH2,inputSalidaHH)) {
            Toast.makeText(this,"El ingreso del doble turno debe ser mayor que la salida",Toast.LENGTH_LONG).show();
            return;
        }
        if(!inputIngresoHH2.getText().toString().isEmpty()){
            if (Integer.parseInt(inputIngresoHH2.getText().toString()) == 0){
            Toast.makeText(this,"El ingreso del doble turno debe ser menor que 00",Toast.LENGTH_LONG).show();
            return;
            }
        }
        String valorRecibido = recibirDatos();
        String[] fechaArray = valorRecibido.split(" - ");
        ContentValues registro = new ContentValues();
        addRegistro(inputIngresoHH,"ingreso1_HH", registro);
        addRegistro(inputIngresoMM,"ingreso1_MM", registro);
        addRegistro(inputSalidaHH,"salida1_HH", registro);
        addRegistro(inputSalidaMM,"salida1_MM", registro);
        addRegistro(inputIngresoHH2,"ingreso2_HH", registro);
        addRegistro(inputIngresoMM2,"ingreso2_MM", registro);
        addRegistro(inputSalidaHH2,"salida2_HH", registro);
        addRegistro(inputSalidaMM2,"salida2_MM", registro);
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion",null,1);
        SQLiteDatabase baseDatos = admin.getWritableDatabase();
        int cantidad = baseDatos.update("horarios", registro,
                "anio = "
                + Integer.parseInt(fechaArray[2])
                + " and mes = "
                + Integer.parseInt(fechaArray[1])
                + " and dia = "
                + Integer.parseInt(fechaArray[0]),
                null);
        baseDatos.close();
        if (cantidad != 0){
            Toast.makeText(this, "Fecha modificada correctamente", Toast.LENGTH_SHORT).show();
            setDatosInput();
        }
    }

    private void addRegistro(EditText et, String key, ContentValues r){
        if(!campoVacio(et)) {
            int input = Integer.parseInt(et.getText().toString());
            r.put(key, input);
        }
    }

    public boolean campoVacio(EditText et){
        return et.getText().toString().length() == 0;
    }

    public void eliminar(View view){
        String valorRecibido = recibirDatos();
        String[] fechaArray = valorRecibido.split(" - ");
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion",null,1);
        SQLiteDatabase baseDatos = admin.getWritableDatabase();
        baseDatos.delete("horarios",
                "anio = "
                + Integer.parseInt(fechaArray[2])
                + " and mes = "
                + Integer.parseInt(fechaArray[1])
                + " and dia = "
                + Integer.parseInt(fechaArray[0]),
                null);
        baseDatos.close();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public boolean ingresoMayorSalida(EditText i, EditText s){
        if(!campoVacio(i) && !campoVacio(s)){
            return Integer.parseInt(i.getText().toString()) >= Integer.parseInt(s.getText().toString()) || Integer.parseInt(i.getText().toString()) == 0;
        }
        return true;
    }
}

