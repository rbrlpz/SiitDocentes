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
package mx.itdurango.rober.siitdocentes;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import mx.itdurango.rober.siitdocentes.adapters.GruposAdapter;
import mx.itdurango.rober.siitdocentes.asynctasks.GruposTask;
import mx.itdurango.rober.siitdocentes.asynctasks.ParcialesTask;
import mx.itdurango.rober.siitdocentes.estaticos.Estaticos;

/**
 * Permite mostrar un listado de los grupos asignados al docente que ha iniciado sesión
 * Al presionar un objeto de la lista envia la petición de mostrar los alumnos y calificaciones para la materia seleccionada
 */
public class ActivityGrupos extends Activity {
    public GruposAdapter gruposAdapter;
    public ListView lvGrupos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupos);

        //se asigna el objeto XML al listview

        lvGrupos = (ListView) findViewById(R.id.lvGrupos);
        //se genera una nueva tarea asincrona que usará la sesion para obtener los grupos asignados al docente
        GruposTask gt = new GruposTask(this);
        gt.execute();

        //cuando se presiona una materia se genera una nueva tarea asincrona para cargar la información de los alumnos/calificaciones.
        lvGrupos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ParcialesTask pt = new ParcialesTask(ActivityGrupos.this);
                String url = ((TextView) view.findViewById(R.id.tv_url)).getText().toString();
                pt.execute(url);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menAcercade) {
            Estaticos.AcercaDe(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
