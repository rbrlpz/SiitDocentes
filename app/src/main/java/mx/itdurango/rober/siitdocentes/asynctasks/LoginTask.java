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
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

import mx.itdurango.rober.siitdocentes.ActivityGrupos;
import mx.itdurango.rober.siitdocentes.Login;
import mx.itdurango.rober.siitdocentes.R;
import mx.itdurango.rober.siitdocentes.estaticos.Estaticos;

/**
 * Tarea asíncrona que envía los datos de acceso a una página del sistema, la cual los evalua, procesa y valida
 */
public class LoginTask extends AsyncTask<String, String, String> {
    private final Login context;

    /**
     * Constructor recibe la actividad de la cual se manda llamar el objeto generado a partir de la clase LoginTask
     *
     * @param login Actividad de la que se genera el objeto
     */
    public LoginTask(Login login) {
        this.context = login;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //muestra un dialogo de espera con el mensaje de validación de acceso
        Estaticos.launchRingDialog(context.getString(R.string.valida_login), context);
    }

    @Override
    protected void onPostExecute(String resultado) {
        super.onPostExecute(resultado);


        Estaticos.ringProgressDialog.dismiss();
        if (resultado.contains("window.top.location")) {
            Toast.makeText(context, context.getString(R.string.login_correcto), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(context, ActivityGrupos.class);
            context.startActivity(intent);

        } else {
            Toast.makeText(context, context.getString(R.string.login_incorrecto), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected String doInBackground(String... valores) {

        HttpPost request = new HttpPost(Estaticos.LOGIN_PAGE);
        String usr = valores[0], pass = valores[1], tipo = valores[2];
        HttpResponse response;
        String responseBody = "";
        try {

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                    2);
            nameValuePairs.add(new BasicNameValuePair("usuario", usr));
            nameValuePairs.add(new BasicNameValuePair("contrasena", pass));
            nameValuePairs.add(new BasicNameValuePair("tipo", tipo));
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
        return responseBody;
    }
}
