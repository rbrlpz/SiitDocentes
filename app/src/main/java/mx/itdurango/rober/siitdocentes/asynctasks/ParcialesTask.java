package mx.itdurango.rober.siitdocentes.asynctasks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import mx.itdurango.rober.siitdocentes.ActivityAlumnos;
import mx.itdurango.rober.siitdocentes.ActivityGrupos;
import mx.itdurango.rober.siitdocentes.estaticos.Estaticos;

public class ParcialesTask extends AsyncTask<String, String, String> {
    private final ActivityGrupos context;
    ProgressDialog ringProgressDialog;

    public ParcialesTask(ActivityGrupos login) {
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

        launchRingDialog("Cargando calificaciones");
    }

    @Override
    protected void onPostExecute(String resultado) {
        super.onPostExecute(resultado);
        //llenaAlumnos(resultado);
        Intent intent = new Intent(context, ActivityAlumnos.class);
        Bundle bundle = new Bundle();
        bundle.putString("resultado", resultado);
        intent.putExtras(bundle);
        context.startActivity(intent);
        ringProgressDialog.dismiss();
    }

    @Override
    protected String doInBackground(String... valores) {
//        String periodo = valores[0], materia = valores[1], grupo = valores[2];
        String pag = valores[0];
        HttpGet request = new HttpGet(Estaticos.REGISTRAR_PARCIALES + pag);//"?periodo="+periodo+"&materia="+materia+"&grupo="+grupo);

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


}
