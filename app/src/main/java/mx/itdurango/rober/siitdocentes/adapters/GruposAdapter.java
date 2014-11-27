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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import mx.itdurango.rober.siitdocentes.R;
import mx.itdurango.rober.siitdocentes.estructuras.Grupos;

/**
 * Crea un adaptador para poder desplegar la información de los grupos en el listview correspondiente
 */
public class GruposAdapter extends ArrayAdapter<Grupos> {
    Context context;
    ArrayList<Grupos> materias;

    /**
     * Constructor
     *
     * @param context  contexto de donde se crea el adapter
     * @param view     vista que va as er usada para mostrar la informacion
     * @param materias lista de Grupos que se tomarán encuenta para mostrar la información
     */
    public GruposAdapter(Context context, int view, ArrayList<Grupos> materias) {
        super(context, view, materias);
        this.context = context;
        this.materias = materias;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_grupos, parent, false);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.alumnos = (TextView) convertView
                    .findViewById(R.id.tv_alumnos);
            viewHolder.materia = (TextView) convertView
                    .findViewById(R.id.tv_materia);
            viewHolder.grupo = (TextView) convertView
                    .findViewById(R.id.tv_grupo);
            viewHolder.item = (LinearLayout) convertView.findViewById(R.id.item_grupo);
            viewHolder.url = (TextView) convertView.findViewById(R.id.tv_url);
            convertView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.alumnos.setText("Alumnos: " + materias.get(position).getAlumnos());
        holder.grupo.setText("Grupo:" + materias.get(position).getGrupo());
        holder.materia.setText(materias.get(position).getNombre());
        holder.url.setVisibility(View.INVISIBLE); //ocultar el dato de la url
        holder.url.setText(materias.get(position).getUrl());
        if (position % 2 == 0)
            holder.item.setBackgroundColor(Color.LTGRAY);

        return convertView;
    }

    static class ViewHolder {
        TextView alumnos;
        TextView materia;
        TextView grupo;
        TextView url;
        LinearLayout item;
    }

}
