/**
 Copyright (c) <2014> José Roberto López Quiñones <rlopez@itdurango.edu.mx>

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */
/**
 * Autor: Roberto Lopez
 * Fecha: 21/11/2014
 */
package mx.itdurango.rober.siitdocentes.asynctasks;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

import java.util.List;

import mx.itdurango.rober.siitdocentes.ActivityAlumnos;
import mx.itdurango.rober.siitdocentes.ActivityGrupos;
import mx.itdurango.rober.siitdocentes.R;
import mx.itdurango.rober.siitdocentes.estaticos.Estaticos;

/**
 * Tarea asincrona que permite cargar al sistema las calificaciones que se han modificado desde la aplicacion.
 */
public class GuardaParcialesTask extends AsyncTask<String, String, String> {
    private final ActivityAlumnos context;
    private List<NameValuePair> nameValuePairs;

    /**
     * Constructor
     *
     * @param context        contexto de la aplicacion
     * @param nameValuePairs lista de parametros que el servidor recibirá para procesarlos y guardarlos en la base de datos
     */
    public GuardaParcialesTask(ActivityAlumnos context, List<NameValuePair> nameValuePairs) {
        this.context = context;
        this.nameValuePairs = nameValuePairs;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Estaticos.launchRingDialog(context.getString(R.string.load_guardarCalificaciones), context);
    }

    @Override
    protected void onPostExecute(String resultado) {
        super.onPostExecute(resultado);


        Estaticos.ringProgressDialog.dismiss();
        if (resultado.contains("window.location")) {
            Toast.makeText(context, context.getString(R.string.guardaParcialesCorrecto),
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(context, ActivityGrupos.class);
            context.startActivity(intent);

        } else {
            Toast.makeText(context, context.getString(R.string.errorGuardaParciales),
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
