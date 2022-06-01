package com.example.horarioslaborales;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class Calendario extends GregorianCalendar {

    static Calendario calendario;

    public static Calendario getInstance(){
        if (calendario == null){
            calendario = new Calendario();
        }
        return calendario;
    }
    private Calendario() {
    }

    public Integer[] anios = {2021, 2022, 2023, 2024, 2025, 2026, 2027};
    public ArrayList<Integer> aniosAL = new ArrayList<Integer>(Arrays.asList(anios));
    public Integer[] meses = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    public ArrayList<Integer> mesesAL = new ArrayList<Integer>(Arrays.asList(meses));

    public Integer[] dias28 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28};
    public ArrayList<Integer> dias28AL = new ArrayList<Integer>(Arrays.asList(dias28));
    public Integer[] dias30 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31};
    public ArrayList<Integer> dias30AL = new ArrayList<Integer>(Arrays.asList(dias30));
    public Integer[] dias31 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31};
    public ArrayList<Integer> dias31AL = new ArrayList<Integer>(Arrays.asList(dias31));



    public Integer anioActual = Integer.parseInt(new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime()));
    public Integer mesActual = Integer.parseInt(new SimpleDateFormat("MM").format(Calendar.getInstance().getTime()));
    public Integer diaActual = Integer.parseInt(new SimpleDateFormat("dd").format(Calendar.getInstance().getTime()));

    public String horaActualStr(){
        return new SimpleDateFormat("HH").format(Calendar.getInstance().getTime());
    }

    public String minActualStr(){
        return new SimpleDateFormat("mm").format(Calendar.getInstance().getTime());
    }
}
