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
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import mx.itdurango.rober.siitdocentes.adapters.AlumnosParcialesAdapter;
import mx.itdurango.rober.siitdocentes.asynctasks.GuardaParcialesTask;
import mx.itdurango.rober.siitdocentes.estaticos.Estaticos;
import mx.itdurango.rober.siitdocentes.estructuras.AlumnosParciales;
import mx.itdurango.rober.siitdocentes.thirdparty.DirectoryChooserDialog;
import mx.itdurango.rober.siitdocentes.thirdparty.SimpleFileDialog;

/**
 * Class: ActivityAlumnos
 * Es una clase que controla la actividad correspondiente al módulo de alumnos
 * la cual permite leer a través del protocolo HttpGet la dirección correspondiente al grupo seleccionado en la actividad anterior.
 * se genera un parser de elementos html, recupera el número de control, nombre y cada una de las calificaciones asignadas o disponibles
 * para mostrarlas en un listview incorporando un menú que controla las acciones a realizar sobre los elementos de calificación, de
 * igual manera muestra un spinner que permite seleccionar la unidad de la cual se están mostrando/modificando las calificaciones.
 */
public class ActivityAlumnos extends Activity {
    public AlumnosParcialesAdapter alumnosParcialesAdapter;
    public GridView lvAlumnos;
    public Spinner spn_unidad;

    int unidad = 0;
    //variables usadas para guardar las calificaciones
    String periodo, materia, grupo, docente, fecha_captura;

    //Variable que controla la lista de alumnos que pertenecen al grupo así como las calificaciones asignadas
    ArrayList<AlumnosParciales> gcs = new ArrayList<AlumnosParciales>();

    /* variables para elegir directorio */
    String m_chosenDir = "";
    boolean m_newFolderEnabled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumnos);
        /*
        * Se recibe un parametro llamado "resultado" el cual contiene todo el html de la página en cuestión
        * */
        Bundle bundle = getIntent().getExtras();
        String resultado = bundle.getString("resultado");

        //GridView que maneja el listado de alumnos
        lvAlumnos = (GridView) findViewById(R.id.lvAlumnos);
        //Spinner que controla el listado de unidades
        spn_unidad = (Spinner) findViewById(R.id.spn_unidad);

        //establece el evento que se llevará a cabo al seleccionar una unidad
        spn_unidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                unidad = position;
                cambiaCalificaciones(unidad);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Parser que descompone el código que se recibió como parámetro y llena los elementos de la vista
        llenaAlumnos(resultado);
    }

    /**
     * cambiaCalificaciones permite refrescar el listado de alumnos cambiando las calificaciones a la unidad seleccionada
     *
     * @param unidad es la unidad que se desea desplegar información
     */
    private void cambiaCalificaciones(int unidad) {
        alumnosParcialesAdapter = new AlumnosParcialesAdapter(this, gcs, unidad);
        lvAlumnos.setAdapter(alumnosParcialesAdapter);
    }

    /**
     * Permite descomponer el código html que se envía con una estructura especifica para llenar los datos de la vista
     *
     * @param html código html que se recibió de una petición HttpGet, debe tener una estructura similar a la siguiente para que el proceso funcione correctamente
     *             <p/>
     *             <input name="periodo" type="hidden" value="20141" />
     *             <input name="materia" type="hidden" value="SD2424" />
     *             <input name="grupo" type="hidden" value="5VR" />
     *             <input name="docente" type="hidden" value="LOQR841213822" />
     *             <input name="fecha_captura" type="hidden" value="2014/06/12" />
     *             <table>
     *             <tr>
     *             <td>No</td>
     *             <td>Noctrl</td>
     *             <td>Nombre</td>
     *             <td>Unidad 1</td>
     *             <td>Unidad 1</td>
     *             <td>Unidad 3</td>
     *             <td>...</td>
     *             <td>Unidad N</td>
     *             </tr>
     *             <tr>
     *             <td>1</td>
     *             <td>9999999</td>
     *             <td>XXXXXXXXXXXXXXXXXXXXX</td>
     *             <td><input type="text" name="calif[1][1]" value="999"/></td>
     *             <td><input type="text" name="calif[1][2]" value="999"/></td>
     *             <td><input type="text" name="calif[1][3]" value="999"/></td>
     *             <td>...</td>
     *             <td><input type="text" name="calif[1][N]" value="999"/></td>
     *             </tr>
     *             <tr>
     *             <td>2</td>
     *             <td>888888888</td>
     *             <td>YYYYYYYYYYYYYYYYYYYYY</td>
     *             <td><input type="text" name="calif[2][1]" value="999"/></td>
     *             <td><input type="text" name="calif[2][2]" value="999"/></td>
     *             <td><input type="text" name="calif[2][3]" value="999"/></td>
     *             <td>...</td>
     *             <td><input type="text" name="calif[2][N]" value="999"/></td>
     *             </tr>
     *             <tr>
     *             <td>M</td>
     *             <td>000000000</td>
     *             <td>ZZZZZZZZZZZZZZZZZZZZZZ</td>
     *             <td><input type="text" name="calif[M][1]" value="999"/></td>
     *             <td><input type="text" name="calif[M][2]" value="999"/></td>
     *             <td><input type="text" name="calif[M][3]" value="999"/></td>
     *             <td>...</td>
     *             <td><input type="text" name="calif[M][N]" value="999"/></td>
     *             </tr>
     *             </table>
     */
    void llenaAlumnos(String html) {
        //Generar un archivo de documento para almacenar los datos del html de forma que se pueda
        //manipular facilmente usando la librería Jsoup
        Document doc = Jsoup.parse(html);

        try {
            //extraer los valores de los elementos del formulario y almacenarlos en los atributos correspondientes de la clase
            Elements e = doc.getElementsByAttributeValue("name", "periodo");
            periodo = e.get(0).attr("value");
            e = doc.getElementsByAttributeValue("name", "materia");
            materia = e.get(0).attr("value");
            e = doc.getElementsByAttributeValue("name", "grupo");
            grupo = e.get(0).attr("value");
            e = doc.getElementsByAttributeValue("name", "docente");
            docente = e.get(0).attr("value");
            e = doc.getElementsByAttributeValue("name", "fecha_captura");
            fecha_captura = e.get(0).attr("value");

            //extraer la tabla correspondiente al listado de alumnos en el caso del siit.itdurango.edu.mx,
            // corresponde a la tabla numero 2 y ya que la numeración comienza en 0, la tabla que necesitamos está en el indice 1
            Element tabla = doc.getElementsByTag("table").get(1);
            //Extraer todos los elementos de tipo tr que pertenecen a la tabla y almacenarlos en una coleccion de tipo Elements.
            Elements renglones = tabla.getElementsByTag("tr");
            //Recorrer la colección de renglones y almacenar cada uno en un objeto
            for (Element tr : renglones) {
                //para cada objeto tr, extraer sus elementos td y almacenarlos en una coleccion
                Elements tds = tr.getElementsByTag("td");
                //permite llevar el control de la columna que se está leyendo, ya que las columnas no tienen un id o clase, se realiza el proceso a mano.
                int col = 1;
                //contenedor de tipo AlumosParciales para almacenar la información de cada alumno (tr)
                AlumnosParciales c = new AlumnosParciales();
                for (Element td : tds) {
                    if (col == 1) {// la columna 1 corresponde al número consecutivo de la tabla
                        c.setNum(td.html());
                    } else if (col == 2) {// la columna 2 corresponde al número de control del alumno
                        c.setControl(td.html());
                    } else if (col == 3) {// la columna 3 corresponde al nombre del alumno
                        c.setNombre(Estaticos.sanitize(td.html()));
                    } else { //el resto de las columnas pertenecen a las calificaciones parciales
                        //se extrae el elemento <input> de la columna y se obtiene el atributo valor para recuperar la calificación en caso de que ya hubiera sido asignada
                        String cal = td.getElementsByTag("input").get(0)
                                .attr("value");

                        ArrayList<String> calif = c.getCalificaciones();
                        calif.add(cal);
                        //se agrega la nueva calificación al conjunto de calificaciones del alumno
                        c.setCalificaciones(calif);
                    }
                    col++; //incrementa el numero de columa
                }
                if (c.getCalificaciones().size() > 0) { //para evitar agregar al listado de alumnos el encabezado de la tabla, validamos que existan calificaciones.
                    gcs.add(c);
                }
            }

            //Llenamos el spinner de unidades a partir del numero de calificaciones que existen en el arreglo
            List<String> spinnerArray = new ArrayList<String>();
            for (int i = 1; i <= gcs.get(1).getCalificaciones().size() - 1; i++) {
                spinnerArray.add("Unidad " + i);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, spinnerArray);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_unidad.setAdapter(adapter);

            //llenamos el listado de alumnos con la información que se obtuvo del proceso anterior
            alumnosParcialesAdapter = new AlumnosParcialesAdapter(this, gcs, unidad);
            lvAlumnos.setAdapter(alumnosParcialesAdapter);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, getString(R.string.error_parser), Toast.LENGTH_SHORT).show();
            finish(); //finaliza el intent actual para desplegar el anterior
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.alumnos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menGuardar) {
            guardarCambios(gcs);
        } else if (id == R.id.menDescargar) {
            elegirDirectorio();
        }
        if (id == R.id.menCargar) {
            cargarCSV();
        }
        if (id == R.id.menAcercade) {
            Estaticos.AcercaDe(this);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Permite enviar los datos al servidor del sistema para almacenar los cambios correspondientes
     *
     * @param listado_alumnos es la lista de alumnos que pertenecen al grupo actual incluyendo las calificaciones de todas las unidades
     */
    private void guardarCambios(ArrayList<AlumnosParciales> listado_alumnos) {
        //Generación de todos los parámetros que se tienen que enviar al servidor a través del método HttpPost
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("periodo", periodo));
        nameValuePairs.add(new BasicNameValuePair("materia", materia));
        nameValuePairs.add(new BasicNameValuePair("grupo", grupo));
        nameValuePairs.add(new BasicNameValuePair("docente", docente));
        nameValuePairs.add(new BasicNameValuePair("fecha_captura", fecha_captura));


        int no_alumno = 1;
        for (AlumnosParciales alumno : listado_alumnos) {
            nameValuePairs.add(new BasicNameValuePair("clave[" + no_alumno + "]", alumno.getControl()));
            nameValuePairs.add(new BasicNameValuePair("promedio[" + no_alumno + "]", "")); //lo calcula el servidor
            int no_calif = 1;
            for (String calif : alumno.getCalificaciones()) {
                nameValuePairs.add(new BasicNameValuePair("calif[" + no_alumno + "][" + no_calif + "]", alumno.getCalificaciones().get(no_calif - 1)));
                no_calif++;
            }
            no_alumno++;
        }
        nameValuePairs.add(new BasicNameValuePair("i", listado_alumnos.size() + ""));
        nameValuePairs.add(new BasicNameValuePair("no_uni", listado_alumnos.get(0).getCalificaciones().size() + ""));

        //Se genera una tarea asíncrona para llamar a la página correspondiente donde se reciben los datos
        GuardaParcialesTask gp = new GuardaParcialesTask(this, nameValuePairs);
        gp.execute("");

    }

    /**
     * Permite generar un archivo .CSV con la estructura que se necesitaría para poder manipular los datos en la PC
     * y subirlo de nuevo.
     */
    private void descargaPlantilla() {
        //el nombre del archivo corresponde a la materia que se está viendo
        String filename = materia;
        FileOutputStream outputStream;
        //Se genera el archivo (vacío) en la ruta seleccionada previamente "m_chosenDir"
        File file = new File(m_chosenDir, filename + ".csv");
        String dato = "";
        try {
            //preparación del archivo para poder escribir datos en el
            outputStream = new FileOutputStream(file);

            ArrayList<AlumnosParciales> listado_alumnos = gcs;
            //se crea el encabezado, como sigue:
            // NOCTRL, NOMBRE, UNIDAD1, UNIDAD2, UNIDAD3,..., UNIDADN
            dato = "NOCTRL,NOMBRE,";
            String comas = "";
            //generacion de los nombres de undidad en base a la cantidad de calificaciones del primer registro.
            for (int i = 1; i < gcs.get(0).getCalificaciones().size(); i++) {
                dato += "UNIDAD " + i;
                //la variable comas, permite crear la estructura completa para las calificaciones en la impresión de los alumnos
                comas += ",";
            }
            dato += "\n"; //indicador del término de renglón

            //recorrer el listado para extraer los datos correspondientes.
            for (AlumnosParciales alumno : listado_alumnos) {
                dato += alumno.getControl() + "," + alumno.getNombre() + "," + comas;
                dato += "\n";
            }

            //almacenar la información en el archivo
            outputStream.write(dato.getBytes());
            //cerrra el archivo
            outputStream.close();

            Toast.makeText(this, "Archivo guardado con el nombre: " + filename, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.error_descargaPlantilla), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        file = null;
        System.gc(); //llamar al garbage collector para liberar memoria
    }

    /**
     * Muestra un listado de los directorios disponibles en el almacenamiento externo del dispositivo
     *
     * @see mx.itdurango.rober.siitdocentes.thirdparty.DirectoryChooserDialog
     */
    private void elegirDirectorio() {
        // Create DirectoryChooserDialog and register a callback
        DirectoryChooserDialog directoryChooserDialog = new DirectoryChooserDialog(ActivityAlumnos.this,
                new DirectoryChooserDialog.ChosenDirectoryListener() {
                    @Override
                    public void onChosenDir(String chosenDir) {
                        m_chosenDir = chosenDir;
                        descargaPlantilla();
                    }
                });
        /* no tengo idea de para que sirvan las lineas siguientes, hacer pruebas para comprobar que siga funcionando si se eliminan */
        // Toggle new folder button enabling
        directoryChooserDialog.setNewFolderEnabled(m_newFolderEnabled);
        // Load directory chooser dialog for initial 'm_chosenDir' directory.
        // The registered callback will be called upon final directory selection.
        directoryChooserDialog.chooseDirectory(m_chosenDir);
        m_newFolderEnabled = !m_newFolderEnabled;
    }

    /**
     * Permite elegir un archivo .CSV del almacenamiento externo, y cargar la información almacenada en él hacia el listado de calificaciones mostrado en ese momento,
     * se cargarán todas las calificaciónes mas no se guardarán en el sistema, hasta que se elija la opción guardar del menu.
     *
     * @see mx.itdurango.rober.siitdocentes.thirdparty.SimpleFileDialog
     */
    private void cargarCSV() {
        SimpleFileDialog FolderChooseDialog = new SimpleFileDialog(ActivityAlumnos.this, "Elegir archivo",
                new SimpleFileDialog.SimpleFileDialogListener() {
                    @Override
                    public void onChosenDir(String chosenDir) {
                        String m_chosen = chosenDir;
                        LoadFile(m_chosen);
                    }
                });
        FolderChooseDialog.chooseFile_or_Dir();
    }

    /**
     * Lee un archivo, lo procesa y asigna las calificaciones de cada alumno en su correspondiente lugar de la vista y arreglo.
     *
     * @param m_chosen ruta absoluta hacia el archivo
     */
    private void LoadFile(String m_chosen) {
        File file = new File(m_chosen);
        try {
            Reader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = "";

            bufferedReader.readLine(); //la primer línea no interesa ya que es el encabezado.
            int j = 0;
            //Se lee línea por línea el archivo
            while ((line = bufferedReader.readLine()) != null) {
                //Log.i("ARCHIVO", line);
                //separar la línea que se lee mediante la coma
                String datos[] = line.split(",");
                ArrayList<String> calificaciones = gcs.get(j).getCalificaciones();
                //recorrer los datos de la línea ya separada a partir de la columna 2 (la columna 0 es el numero de control y al 1 el nombre)
                for (int i = 2; i < datos.length; i++) {
                    //se reemplaza la calificacion correspondiente
                    calificaciones.set(i - 2, datos[i]);
                    //Log.i("CALIF", "[" + j + "][" + i + "]=" + datos[i]);
                }
                //se almacenan los cambios en el listado principal.
                gcs.get(j).setCalificaciones(calificaciones);
                j++;
            }
            //se recargan las calificaciones en el listado para que se vean reflejados los cambios
            cambiaCalificaciones(0);
        } catch (java.io.IOException e) {
            Toast.makeText(this, getString(R.string.error_cargaCalif), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

}
