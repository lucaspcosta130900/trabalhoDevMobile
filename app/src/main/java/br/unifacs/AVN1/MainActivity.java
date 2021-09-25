package br.unifacs.AVN1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button botaoConfig=(Button) findViewById(R.id.buttonConfig);
        Button botaoSair=(Button) findViewById(R.id.buttonGNSS);
        Button botaoSobre=(Button) findViewById(R.id.buttonSobre);
        Button botaoNavegacao=(Button) findViewById(R.id.buttonNavegacao);
        botaoConfig.setOnClickListener(this);
        botaoSair.setOnClickListener(this);
        botaoSobre.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonConfig:
                startActivity(new Intent(this,ConfigActivity.class));
                break;
            case R.id.buttonGNSS:
                finish();
                break;
            case R.id.buttonNavegacao:
                finish();
                break;
            case R.id.buttonSobre:
                startActivity(new Intent(this,SobreActivity.class));
                break;
        }
    }
}