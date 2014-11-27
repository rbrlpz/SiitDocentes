package mx.itdurango.rober.siitdocentes.asynctasks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

import java.util.List;

import mx.itdurango.rober.siitdocentes.ActivityAlumnos;
import mx.itdurango.rober.siitdocentes.ActivityGrupos;
import mx.itdurango.rober.siitdocentes.estaticos.Estaticos;

public class GuardaParcialesTask extends AsyncTask<String, String, String> {
    private final ActivityAlumnos context;
    ProgressDialog ringProgressDialog;
    private List<NameValuePair> nameValuePairs;

    public GuardaParcialesTask(ActivityAlumnos context, List<NameValuePair> nameValuePairs) {
        this.context = context;
        this.nameValuePairs = nameValuePairs;
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

        launchRingDialog("Cargando información al sistema.");
    }

    @Override
    protected void onPostExecute(String resultado) {
        super.onPostExecute(resultado);


        ringProgressDialog.dismiss();
        if (resultado.contains("window.location")) {
            Toast.makeText(context, "Proceso terminado correctamente",
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(context, ActivityGrupos.class);
            context.startActivity(intent);

        } else {
            Toast.makeText(context, "Ha ocurrido un error, probablemente hay que reiniciar sesión",
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected String doInBackground(String... valores) {

        HttpPost request = new HttpPost(Estaticos.GUARDAR_PARCIALES);

        HttpResponse response;
        String responseBody = "";
        try {

            request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
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
        Log.d("POST_CALIF", responseBody);
        return responseBody;
    }
}
