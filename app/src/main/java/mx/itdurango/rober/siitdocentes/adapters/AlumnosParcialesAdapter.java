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
package mx.itdurango.rober.siitdocentes.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import mx.itdurango.rober.siitdocentes.ActivityAlumnos;
import mx.itdurango.rober.siitdocentes.R;
import mx.itdurango.rober.siitdocentes.estructuras.AlumnosParciales;

/**
 * Adapter para llenar la vista del listado de alumnos
 * debe contemplarse que se tiene un elemento editable en tiempo de ejecución por lo tanto debe controlarse.
 */
public class AlumnosParcialesAdapter extends BaseAdapter {

    public ArrayList<AlumnosParciales> myItems = new ArrayList<AlumnosParciales>();
    ActivityAlumnos context;
    int width;
    int unidad;
    private LayoutInflater mInflater;

    /**
     * Constructor
     *
     * @param context contexto de la aplicación
     * @param datos   listado de alumnos y calificaciones
     * @param unidad  numero de unidad a mostrar
     */
    public AlumnosParcialesAdapter(ActivityAlumnos context, ArrayList<AlumnosParciales> datos, int unidad) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.myItems = datos;
        this.unidad = unidad;
        notifyDataSetChanged();//permite informar cuando se han cambiado los datos en el adapter

        //Obtiene el tamaño horizontal de la pantalla.
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
    }

    public int getCount() {
        return myItems.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_alumno_calif, null);

            holder.tvNombreAlu = (TextView) convertView.findViewById(R.id.tvNombreAlu);
            holder.tvControl = (TextView) convertView.findViewById(R.id.tvControl);
            holder.item_alumno = (LinearLayout) convertView.findViewById(R.id.item_alumno);

            holder.etCalif = (EditText) convertView
                    .findViewById(R.id.etCalif);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvControl.setText(myItems.get(position).getControl());
        holder.tvNombreAlu.setText(myItems.get(position).getNombre());
        holder.tvNombreAlu.setTextColor(Color.BLACK);
        holder.tvNombreAlu.setMaxWidth(width - holder.etCalif.getWidth());


        //llena el EditText con el valor que se tiene en el arreglo
        holder.etCalif.setText(myItems.get(position).getCalificaciones().get(unidad));
        //asigna el id al elemento que corresponde con la posicion del arreglo
        holder.etCalif.setId(position);

        //se agrega un updateListener para determinar cuando ha cambiado el valor
        holder.etCalif.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    final int position = v.getId();
                    final EditText Caption = (EditText) v;
                    //hacer efectivos los cambios en el arreglo para evitar que cuando se haga scroll el repintado de elementos regrese a su valor original
                    //y tome en consideracion el nuevo valor que el usuario escribio.
                    ArrayList<String> califs = myItems.get(position).getCalificaciones();
                    califs.set(unidad, Caption.getText().toString());
                    myItems.get(position).setCalificaciones(califs);
                }
            }
        });

        if (position % 2 == 0) {
            holder.item_alumno.setBackgroundColor(Color.LTGRAY);
        }

        return convertView;
    }


    static class ViewHolder {
        TextView tvControl;
        TextView tvNombreAlu;
        EditText etCalif;
        LinearLayout item_alumno;
    }

}