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

public class GruposAdapter extends ArrayAdapter<Grupos> {
    Context context;
    ArrayList<Grupos> materias;

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
        holder.alumnos.setTextColor(Color.BLACK);
        holder.grupo.setText("Grupo:" + materias.get(position).getGrupo());
        holder.grupo.setTextColor(Color.BLACK);
        holder.materia.setText(materias.get(position).getNombre());
        holder.materia.setTextColor(Color.BLACK);
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
