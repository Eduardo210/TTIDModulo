package org.example.ttidmodulo;

import android.content.SharedPreferences;

public class Variables_globales {

    public static int Power6B =30;
    public static int Power6C =27;
    public static int Time6B =500;
    public static int Time6C =500;


    //Trigger
    // seleccion valores;
//    0 representa el protocolo B
//    1 Representa el protocolo C
//    2 Representar para la lectura de ambos protocolos
    public static int seleccion = 1;

//    Formato es lo que reperesenta el dormato de decodificacion para la lectura de los tag
//    el 0 representa  el formato normal
//    el 1 Formato TelepeajeMX
//    el 2 representa el formato frontera Mx
//    el 3 represental el telepeaje GT
    public static int Formato=0;
}
