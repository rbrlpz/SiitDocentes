package mx.itdurango.rober.siitdocentes.estaticos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.apache.http.impl.client.DefaultHttpClient;

import mx.itdurango.rober.siitdocentes.R;

/**
 * Created by rober on 21/11/14.
 */
public class Estaticos {
    public static final DefaultHttpClient BROWSER = new DefaultHttpClient();
    public static final String LOGIN_PAGE = "http://siit.itdurango.edu.mx/acceso.php";
    public static final String LISTADO_GRUPOS = "http://siit.itdurango.edu.mx/modulos/doc/certificacion/grupos.php?accion=calificaciones_parciales";
    public static final String GUARDAR_PARCIALES = "http://siit.itdurango.edu.mx/modulos/doc/certificacion/calificaciones_parciales_bd.php";//?periodo=20143&materia=IT8851&grupo=8A";
    public static final String REGISTRAR_PARCIALES = "http://siit.itdurango.edu.mx/modulos/doc/certificacion/";//calificaciones_parciales.php";//?periodo=20143&materia=IT8851&grupo=8A";
    //    public static final String LOGIN_PAGE = "http://itdurango.mx/siit/index.php";
//    public static final String GUARDAR_PARCIALES = "http://itdurango.mx/siit/calificaciones_parciales_bd.php";//?periodo=20143&materia=IT8851&grupo=8A";
//    public static final String REGISTRAR_PARCIALES = "http://itdurango.mx/siit/";//calificaciones_parciales.php";//?periodo=20143&materia=IT8851&grupo=8A";
//    public static final String LISTADO_GRUPOS = "http://itdurango.mx/siit/grupos.php?accion=calificaciones_parciales";
    public static final String SharedPreferencesName = "LoginPrefsSIIT";
    public static String usrKey = "UsrKey";
    public static String passKey = "PassKey";
    public static String storeKey = "RecuerdaKey";


    public static ProgressDialog ringProgressDialog;


    public static void launchRingDialog(String mensaje, Activity context) {
        Estaticos.ringProgressDialog = ProgressDialog.show(context,
                "Espera por favor ...", mensaje, true);
        Estaticos.ringProgressDialog.setCancelable(false);
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }

    public static String sanitize(String valor) {
        valor = valor.replace("&aacute;", "á");
        valor = valor.replace("&eacute;", "é");
        valor = valor.replace("&iacute;", "í");
        valor = valor.replace("&oacute;", "ó");
        valor = valor.replace("&uacute;", "ú");
        valor = valor.replace("&Aacute;", "Á");
        valor = valor.replace("&Eacute;", "É");
        valor = valor.replace("&Iacute;", "Í");
        valor = valor.replace("&Oacute;", "Ó");
        valor = valor.replace("&Uacute;", "Ú");
        valor = valor.replace("&ntilde;", "ñ");
        valor = valor.replace("&Ntilde;", "Ñ");
        valor = valor.replace("&Atilde;", "Ñ");


        return valor;
    }

    public static void AcercaDe(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.acerca_de, null);
        TextView disclaimer = (TextView) view.findViewById(R.id.tvDisclaimer);
        disclaimer.setMovementMethod(LinkMovementMethod.getInstance());
        disclaimer.setText(Html.fromHtml(activity.getString(R.string.Disclaimer)));

        builder.setView(view)
                .setTitle("Atención!!")
                .setCancelable(false)
                .setNeutralButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
