package mx.itdurango.rober.siitdocentes.asynctasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import mx.itdurango.rober.siitdocentes.ActivityGrupos;
import mx.itdurango.rober.siitdocentes.R;
import mx.itdurango.rober.siitdocentes.adapters.GruposAdapter;
import mx.itdurango.rober.siitdocentes.estaticos.Estaticos;
import mx.itdurango.rober.siitdocentes.estructuras.Grupos;

public class GruposTask extends AsyncTask<String, String, String> {
    private final ActivityGrupos context;
    ProgressDialog ringProgressDialog;

    public GruposTask(ActivityGrupos login) {
        this.context = login;
    }

    public void launchRingDialog(String mensaje) {
        ringProgressDialog = ProgressDialog.show(context,
                "Espera por favor ...", mensaje, true);
        ringProgressDialog.setCancelable(true);
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        launchRingDialog("Cargando item_grupos...");
    }

    @Override
    protected void onPostExecute(String resultado) {
        super.onPostExecute(resultado);

        procesa(resultado);

        ringProgressDialog.dismiss();

    }

    @Override
    protected String doInBackground(String... valores) {

        HttpGet request = new HttpGet(Estaticos.LISTADO_GRUPOS);
        HttpResponse response;
        String responseBody = "";
        try {
            response = Estaticos.BROWSER.execute(request);
            StatusLine status = response.getStatusLine();
            Log.d("main", "estatus:" + status.getStatusCode());

            HttpEntity entity = response.getEntity();

            if (entity != null) {
                responseBody = EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseBody;
    }

    public void procesa(String html) {
        Document doc = Jsoup.parse(html);
        Element tabla = doc.getElementsByTag("table").get(0);
        Elements renglones = tabla.getElementsByTag("tr");

        ArrayList<Grupos> gcs = new ArrayList<Grupos>();
        for (Element tr : renglones) {
            Elements tds = tr.getElementsByTag("td");
            int l = 1;
            Grupos gc = new Grupos();
            for (Element td : tds) {
                if (l == 1) {

                    //<td> <b> IT8851  </b> <br> BASE DE DATOS PARA SOL.WEB  </td>
                    String datos = td.html();
                    datos = datos.replaceAll("<b>", "");
                    String m[] = datos.split("</b> <br />");
                    gc.setClave(m[0]);
                    gc.setNombre(m[1]);

                } else if (l == 2) {
                    gc.setGrupo(td.html());
                } else if (l == 3) {
                    gc.setAlumnos(td.html());

                } else if (l == 4) {
                    // <img
                    // src="http://siit.itdurango.edu.mx/img/iconos/captura_calif.gif"
                    // onclick="window.location = &quot;calificaciones_parciales.php?periodo=20141&amp;materia=IT8851&amp;grupo=8TA&quot;"
                    // alt="Captura de Información" style="cursor:pointer">
                    String params = td.html();
                    String separado[] = params.split("&quot;");
                    params = separado[1]; // solo los parametros
                    params = params.replaceAll("&amp;", "&");
                    // params = params.replace("?", "%3f");
                    gc.setUrl(params);
                }
                l++;
            }
            if (gc.getClave() != null)
                gcs.add(gc);
        }

        context.lvGrupos.setAdapter(new GruposAdapter(context, R.layout.item_grupos, gcs));
    }
}
