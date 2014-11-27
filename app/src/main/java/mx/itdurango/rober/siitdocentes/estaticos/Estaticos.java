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

    /* Sección de parametros almacenados en las preferencias compartidas */
    //nombre de las preferencias compartidas
    public static final String SharedPreferencesName = "LoginPrefsSIIT";
    //valor para usuario
    public static final String usrKey = "UsrKey";
    //valor para contraseña
    public static final String passKey = "PassKey";
    //valor para recordar contraseña
    public static final String storeKey = "RecuerdaKey";


    //objeto para manipular el cuadro de dialogo de progreso
    public static ProgressDialog ringProgressDialog;

    /**
     * Muestra un cuadro de dialogo de progreso con un mensaje personalizado
     *
     * @param mensaje mensaje a desplegar por el cuadro
     * @param context contexto de ejecución
     */
    public static void launchRingDialog(String mensaje, Activity context) {
        Estaticos.ringProgressDialog = ProgressDialog.show(context,
                context.getString(R.string.tituloDialogo), mensaje, true);
        Estaticos.ringProgressDialog.setCancelable(false);
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }

    /**
     * Permite cambiar los valores de caracteres html a caracteres legibles
     * @param valor cadena html
     * @return cadena con caracteres especiales.
     */
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

    /**
     * Muestra un alert con una vista "Acerca de..."
     * @param activity
     */
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
