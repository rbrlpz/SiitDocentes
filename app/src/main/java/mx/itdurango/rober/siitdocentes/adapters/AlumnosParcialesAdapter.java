package mx.itdurango.rober.siitdocentes.adapters;

/**
 * Created by rober on 21/11/14.
 */

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

public class AlumnosParcialesAdapter extends BaseAdapter {

    public ArrayList<AlumnosParciales> myItems = new ArrayList<AlumnosParciales>();
    ActivityAlumnos context;
    int width;
    int unidad;
    private LayoutInflater mInflater;

    public AlumnosParcialesAdapter(ActivityAlumnos context, ArrayList<AlumnosParciales> datos, int unidad) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.myItems = datos;
        this.unidad = unidad;
        notifyDataSetChanged();

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


        //Fill EditText with the value you have in data source
        holder.etCalif.setText(myItems.get(position).getCalificaciones().get(unidad));
        holder.etCalif.setId(position);

        //we need to update adapter once we finish with editing
        holder.etCalif.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    final int position = v.getId();
                    final EditText Caption = (EditText) v;
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