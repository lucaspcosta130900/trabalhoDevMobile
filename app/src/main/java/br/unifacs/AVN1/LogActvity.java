package br.unifacs.AVN1;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class LogActvity extends AppCompatActivity {

    Banco db = new Banco(this);
    private ListView listLog;
    ArrayAdapter<String> adapter;
    ArrayList<String> aux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_actvity);
        getSupportActionBar().hide();
        listLog = (ListView) findViewById(R.id.listlog);
        clickBtn();
        listar();
    }

    public void listar(){
        List<Local> locais = new ArrayList<Local>();
        locais =  db.lerDados();
        aux =  new ArrayList<String>();
        adapter = new ArrayAdapter<String>(LogActvity.this, android.R.layout.simple_list_item_1, aux);
        listLog.setAdapter(adapter);
        for(Local l : locais){
            String imprime = "LATITUDE: " + l.getLatitude() + "\nLONGITUDE: " + l.getLongitude() + "\nDATA: " + l.getData().replaceAll("-","/");
            aux.add(imprime);
            adapter.notifyDataSetChanged();
        }
    }

    public void clickBtn(){
        FloatingActionButton fablog = findViewById(R.id.fabLog);
        fablog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.close();
                db.deletaBD();
                aux =  new ArrayList<String>();
                adapter = new ArrayAdapter<String>(LogActvity.this, android.R.layout.simple_list_item_1, aux);
                listLog.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();


    }
}