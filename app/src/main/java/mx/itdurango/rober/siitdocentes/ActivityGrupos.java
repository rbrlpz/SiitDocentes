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
 * Created by rober on 21/11/14.
 */
public class ActivityGrupos extends Activity {
    public GruposAdapter gruposAdapter;
    public ListView lvGrupos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupos);

        lvGrupos = (ListView) findViewById(R.id.lvGrupos);
        GruposTask gt = new GruposTask(this);
        gt.execute();

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
