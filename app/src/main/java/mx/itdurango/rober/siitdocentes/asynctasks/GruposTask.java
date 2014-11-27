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

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
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

/**
 * Tarea asincrona que carga y procesa los grupos asignados al docente que inició sesión.
 */
public class GruposTask extends AsyncTask<String, String, String> {
    private final ActivityGrupos context;

    public GruposTask(ActivityGrupos login) {
        this.context = login;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //muestra un cuadro de dialogo de proceso para permitir la carga de grupos
        Estaticos.launchRingDialog(context.getString(R.string.loading_grupos), context);
    }

    @Override
    protected void onPostExecute(String resultado) {
        super.onPostExecute(resultado);
        //procesa el html resultante de la petición httpGet
        procesa(resultado);
        Estaticos.ringProgressDialog.dismiss();

    }

    @Override
    protected String doInBackground(String... valores) {
        //Genera una petición httpGet hacia la pagina que muestra el listado de grupos
        HttpGet request = new HttpGet(Estaticos.LISTADO_GRUPOS);
        HttpResponse response;
        String responseBody = "";
        try {
            //ejecuta la petición en el "navegador" estático para preservar la sesion.
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

    /**
     * Procesa el html resultante de la petición del listado de grupos descomponiendolo y asignandolo a un ArrayList
     *
     * @param html cuerpo html del resultado de la petición
     */
    public void procesa(String html) {
        //se genera un documento donde se almacena el contenido html listo para ser procesado.
        Document doc = Jsoup.parse(html);
        //se obtiene la tabla donde se encuentra el contenido que interesa
        Element tabla = doc.getElementsByTag("table").get(0);
        //se obtienen todos los renglones de la tabla
        Elements renglones = tabla.getElementsByTag("tr");
        //arraylist que almacenará la información de los grupos
        ArrayList<Grupos> gcs = new ArrayList<Grupos>();
        //se recorre cada renglon almacenandolo en un objeto
        for (Element tr : renglones) {
            //se obtienen todos los elementos td de cada renglon.
            Elements tds = tr.getElementsByTag("td");
            //lleva el control de la columna que se está evaluando
            int l = 1;
            //objeto para lmacenar la informacion de cada grupo
            Grupos gc = new Grupos();
            //se recorren todos los elementos td del renglon actual
            for (Element td : tds) {
                //en el renglon 1 se encuentra la informacion del grupo con el siguiente formato
                //<b> CLAVE_MATERIA  </b> <br> NOMBRE DE LA MATERIA
                if (l == 1) {
                    //se obtiene el contenido de la celda
                    String datos = td.html();
                    //eliminar las etiquetas de inicio de negritas
                    datos = datos.replaceAll("<b>", "");
                    //separar la cadena para tener en la posición 0 la clave de la materia y en la posicion 1 el nombre de la misma.
                    String m[] = datos.split("</b> <br />");
                    gc.setClave(m[0]); //se asigna la clave de la materia al campo correspondiente
                    gc.setNombre(m[1]);//se asigna el nombre de la materia al campo correspondiente
                } else if (l == 2) { //en la columna 2 se encuentra el grupo
                    gc.setGrupo(td.html());
                } else if (l == 3) { //en la columna 3 se encuentra el numero de alumnos inscritos
                    gc.setAlumnos(td.html());
                } else if (l == 4) { //en la columna 4 se encuentran los vinculos para asignar calificaciones parciales con el siguiente formato
                    // <img src="http://siit.itdurango.edu.mx/img/iconos/captura_calif.gif"
                    // onclick="window.location = &quot;calificaciones_parciales.php?periodo=20141&amp;materia=IT8851&amp;grupo=8TA&quot;"
                    // alt="Captura de Información" style="cursor:pointer">

                    //tomamos el contenido de la celda
                    String params = td.html();
                    //si separamos mediante la cadena "&quot;" podemos obtener solamente la url con parámetros que se tiene que mandar llamar
                    String separado[] = params.split("&quot;");
                    params = separado[1]; // solo los parametros
                    params = params.replaceAll("&amp;", "&");
                    //asignar la url a su campo correspondiente
                    gc.setUrl(params);
                }
                //incrementar el numero de columna
                l++;
            }
            //si la clave es nula significa que no es una materia, probablemente sea el encabezado de la tabla
            if (gc.getClave() != null)
                gcs.add(gc);
        }
        //se genera un adapter nuevo con la información obtenida para ser asignado al listview de grupos.
        context.lvGrupos.setAdapter(new GruposAdapter(context, R.layout.item_grupos, gcs));
    }
}
