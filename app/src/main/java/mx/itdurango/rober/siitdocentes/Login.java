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
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import mx.itdurango.rober.siitdocentes.asynctasks.LoginTask;
import mx.itdurango.rober.siitdocentes.estaticos.Estaticos;

/**
 * Muestra una interfáz con un formulario de acceso, hace uso de SharedPreferences para permitir guardar los datos
 * del usuario y contraseña si así lo desea el usuario.
 */
public class Login extends Activity {
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //se genera un apartado privado en las preferencias compartidas del sistema para guardar los datos
        sharedpreferences = getSharedPreferences(Estaticos.SharedPreferencesName, Context.MODE_PRIVATE);

        // recuperación de los objetos XML del layout
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        final TextView etUsuario = (TextView) findViewById(R.id.etUsuario);
        final TextView etContrasena = (TextView) findViewById(R.id.etContrasena);
        final CheckBox chkRecuerda = (CheckBox) findViewById(R.id.chkRecuerda);

        //Verifica las preferencias de usuario para determinar si se encuentra almacenado, de ser así muestra la informacion en su
        //correspondiente cuadro de texto,
        recuperaLogin(etUsuario, etContrasena, chkRecuerda);
        //establece la acción a realizar cuando se presione el botón accesar
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //si el checkbox está habilitado, se guardan los parámetros en las preferencias del compartidas
                if (chkRecuerda.isChecked()) {
                    guardaLogin(etUsuario.getText().toString(), etContrasena.getText().toString(), true);
                } else { //se eliminan los datos almacenados.
                    guardaLogin("", "", false);
                }
                //crea una tarea asíncrona para comunicarse con el sistema y validar los datos de acceso.
                LoginTask lt = new LoginTask(Login.this);
                //ejecuta la tarea asíncrona enviando los parametros de usuario, contrasena y tipo
                //que son los parametros que pide el sistema para realizar un acceso.
                lt.execute(etUsuario.getText().toString(), etContrasena.getText().toString(), "p");
            }
        });

    }

    /**
     * @param usuario
     * @param contrasena
     * @param almacenar
     */
    private void guardaLogin(String usuario, String contrasena, Boolean almacenar) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        String usr = usuario;
        String pass = contrasena;


        editor.putString(Estaticos.usrKey, usr);
        editor.putString(Estaticos.passKey, pass);
        editor.putString(Estaticos.storeKey, almacenar.toString());

        editor.commit();
    }

    /**
     * Permite recuperar los datos almacenados en las preferencias compartidas y asignarlos en el objeto correspondiente
     *
     * @param etUsuario    EditText perteneciente al usuario
     * @param etContrasena EditText perteneciente a la contraseña
     * @param chkRecuerda  CheckBox perteneciente a recordar contraseña
     */
    private void recuperaLogin(TextView etUsuario, TextView etContrasena, CheckBox chkRecuerda) {
        if (sharedpreferences.contains(Estaticos.usrKey)) {
            etUsuario.setText(sharedpreferences.getString(Estaticos.usrKey, ""));

        }
        if (sharedpreferences.contains(Estaticos.passKey)) {
            etContrasena.setText(sharedpreferences.getString(Estaticos.passKey, ""));

        }
        if (sharedpreferences.contains(Estaticos.storeKey)) {
            if (sharedpreferences.getString(Estaticos.storeKey, "").equals("true")) {
                chkRecuerda.setChecked(true);
            } else {
                chkRecuerda.setChecked(true);
            }
        }

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
