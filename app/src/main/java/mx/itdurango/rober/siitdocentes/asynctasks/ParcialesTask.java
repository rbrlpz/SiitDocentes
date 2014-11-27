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
import android.os.Bundle;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import mx.itdurango.rober.siitdocentes.ActivityAlumnos;
import mx.itdurango.rober.siitdocentes.ActivityGrupos;
import mx.itdurango.rober.siitdocentes.R;
import mx.itdurango.rober.siitdocentes.estaticos.Estaticos;

/**
 * Tarea asincrona que permite cargar los datos de las calificaciones parciales por alumno del grupo seleccionado
 */
public class ParcialesTask extends AsyncTask<String, String, String> {
    private final ActivityGrupos context;

    public ParcialesTask(ActivityGrupos login) {
        this.context = login;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Estaticos.launchRingDialog(context.getString(R.string.loading_calificaciones), context);
    }

    @Override
    protected void onPostExecute(String resultado) {
        super.onPostExecute(resultado);
        Intent intent = new Intent(context, ActivityAlumnos.class);
        Bundle bundle = new Bundle();
        bundle.putString("resultado", resultado);
        intent.putExtras(bundle);
        context.startActivity(intent);
        Estaticos.ringProgressDialog.dismiss();
    }

    @Override
    protected String doInBackground(String... valores) {
        //se obtiene el dato de la url que se va a accesar
        String pag = valores[0];
        HttpGet request = new HttpGet(Estaticos.REGISTRAR_PARCIALES + pag);//"?periodo="+periodo+"&materia="+materia+"&grupo="+grupo);

        HttpResponse response;
        String responseBody = "";
        try {
            response = Estaticos.BROWSER.execute(request);
            StatusLine status = response.getStatusLine();
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
