package org.example.ttidmodulo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class helpes {

    public static boolean VerificarConexionInternet(Context context) {
        boolean connected = false;
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // Recupera todas las redes (tanto móviles como wifi)
        NetworkInfo[] redes = connec.getAllNetworkInfo();
        for (int i = 0; i < redes.length; i++) {
            // Si alguna red tiene conexión, se devuelve true
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                connected = true;
            }
        }
        return connected;
    }

    public String ConvierteTAGEPC(String EPC) {

        String cadena = "";

        for (int i = 0; i < 20; i=i+2) {
            String hex=EPC.substring(i,i+2);
            String decimal= String.valueOf(Integer.parseInt(hex,16));
            cadena += get6BitChar(decimal);
        }
        return cadena;
    }

    public char get6BitChar(String str){
        switch (str){
            case "48":
                return '0';
            case "49":
                return '1';
            case "50":
                return '2';
            case "51":
                return '3';
            case "52":
                return '4';
            case "53":
                return '5';
            case "54":
                return '6';
            case "55":
                return '7';
            case "56":
                return '8';
            case "57":
                return '9';
            case "65":
                return 'A';
            case "66":
                return 'B';
            case "67":
                return 'C';
            case "68":
                return 'D';
            case "69":
                return 'E';
            case "70":
                return 'F';
            case "71":
                return 'G';
            case "72":
                return 'H';
            case "73":
                return 'I';
            case "74":
                return 'J';
            case "75":
                return 'K';
            case "76":
                return 'L';
            case "77":
                return 'M';
            case "78":
                return 'N';
            case "79":
                return 'O';
            case "80":
                return 'P';
            case "81":
                return 'Q';
            case "82":
                return 'R';
            case "83":
                return 'S';
            case "84":
                return 'T';
            case "85":
                return 'U';
            case "86":
                return 'V';
            case "87":
                return 'W';
            case "88":
                return 'X';
            case "89":
                return 'Y';
            case "90":
                return 'Z';

        }
        return '\0';
    }
}
