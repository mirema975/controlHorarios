package com.example.horarioslaborales;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private ListView lv_horarios;
    private Spinner spinner_mes;
    private Calendario calendario = Calendario.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner_mes = (Spinner) findViewById(R.id.spinnerMesMain);
        setContentSpinners(calendario.meses, spinner_mes);
        lv_horarios = (ListView) findViewById(R.id.listview_horarios_guardados);
        filtrado(spinner_mes);
        clickListView(lv_horarios);
    }

    public void setContentSpinners(Integer[] arrl,Spinner sp){
        ArrayAdapter <Integer> arrayAdapter = new ArrayAdapter<Integer>(this, R.layout.spinner_item, arrl);
        sp.setAdapter(arrayAdapter);
        sp.setSelection(calendario.mesActual - 1);
    }

    public void agregar(View view){
        Intent i = new Intent(this, ActivityAgregar.class);
        startActivity(i);
    }

    public void filtrado(Spinner sp) {
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (sp.getSelectedItem().equals(Integer.parseInt(spinner_mes.getSelectedItem().toString()))) {
                listarHorarios(Integer.parseInt(spinner_mes.getSelectedItem().toString()));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    public void listarHorarios(int m){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase dataBase = admin.getReadableDatabase();
        ArrayList<Calendar> fechas = new ArrayList<>();
        Cursor fila = dataBase.rawQuery("select anio, mes, dia from horarios where mes = " + m ,null);
        while (fila.moveToNext()){
            int anio = fila.getInt(0);
            int mes = fila.getInt(1);
            int dia = fila.getInt(2);
            Calendar fecha = Calendar.getInstance();
            fecha.set(anio,mes - 1,dia);
            fechas.add(fecha);
        }
        Collections.sort(fechas);
        ArrayList<String> fechasSort = new ArrayList<>();
        fechas.forEach(a -> fechasSort.add(""
                + a.get(Calendar.DATE)
                + " - "
                + (a.get(Calendar.MONTH) + 1)
                + " - " + a.get(Calendar.YEAR)));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.list_item_ema, fechasSort);
        lv_horarios.setAdapter(arrayAdapter);
        dataBase.close();
    }

    public void clickListView(ListView lv){
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String fechaSeleccionada = lv_horarios.getItemAtPosition(i).toString();
                navegar(fechaSeleccionada);
            }
        });
    }

    public void navegar(String f){
        Bundle extras = new Bundle();
        extras.putString("fecha", f);
        Intent intent = new Intent(this, DetallesHorario.class);
        intent.putExtras(extras);
        startActivity(intent);
    }

    public void activityCalcular(View view){
        Intent intent = new Intent(this, ActivityCalcular.class);
        startActivity(intent);
    }

}