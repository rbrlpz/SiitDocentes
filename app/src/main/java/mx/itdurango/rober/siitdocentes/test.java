package mx.itdurango.rober.siitdocentes;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rober on 21/11/14.
 */
public class test extends ArrayAdapter<Model>

{
    private final List<Model> list;
    private final Activity context;

    public test(Activity context, List<Model> list) {
        super(context, R.layout.activity_alumnos, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();

            //view = inflator.inflate(R.layout.rowbuttonlayout, null);

            final ViewHolder viewHolder = new ViewHolder();
            // viewHolder.text = (TextView) view.findViewById(R.id.label);
            //viewHolder.scores = (EditText) view.findViewById(R.id.score);
            viewHolder.scores.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Model element = (Model) viewHolder.scores.getTag();
                    element.setScore(s.toString());
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void afterTextChanged(Editable s) {
                }
            });
            //viewHolder.checkbox = (CheckBox)view.findViewById(R.id.check);
            viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Model element = (Model) viewHolder.checkbox.getTag();
                    element.setSelected(buttonView.isChecked());
                }
            });
            viewHolder.checkbox.setTag(list.get(position));
            viewHolder.scores.setTag(list.get(position));
            view.setTag(viewHolder);
        } else {
            view = convertView;
            ((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
            ((ViewHolder) view.getTag()).scores.setTag(list.get(position));
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.text.setText(list.get(position).getName());
        holder.scores.setText(list.get(position).getScore());
        holder.checkbox.setChecked(list.get(position).isSelected());
        return view;

    }

    static class ViewHolder {
        protected TextView text;
        protected CheckBox checkbox;
        protected EditText scores;
    }


}
