//Esta es la pantalla principal, desde la que se tiene acceso a las dos áreas: gestión y estadísticas

package com.example.gestliga;

import java.text.ParseException;

import tipos.Torneo;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class PantallaInicial extends Activity {
	
	private ImageView btnGestion;
	private ImageView btnEstadisticas;
	private Button btnCreditos;
	public static DBHelper bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_inicial);
        bd = new DBHelper(this);
        
        btnGestion = (ImageView) findViewById(R.id.btnGestion);
        btnGestion.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					btnGestion.setBackgroundColor(Color.argb(30, 0, 0, 0));
				}
				if(event.getAction() == MotionEvent.ACTION_UP){
					btnGestion.setBackgroundColor(Color.TRANSPARENT);
				}
				return false;
			}
		});
        
        btnEstadisticas = (ImageView) findViewById(R.id.btnEstadisticas);
        btnEstadisticas.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					btnEstadisticas.setBackgroundColor(Color.argb(30, 0, 0, 0));
				}
				if(event.getAction() == MotionEvent.ACTION_UP){
					btnEstadisticas.setBackgroundColor(Color.TRANSPARENT);
				}
				return false;
			}
		});
        
        btnCreditos = (Button) findViewById(R.id.btnCreditos);
        btnCreditos.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
				builder.setTitle(getString(R.string.creditos));
				builder.setMessage(getString(R.string.creditosExp));
				
				builder.setNegativeButton(getString(R.string.volver), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						});
				builder.create().show();
			}
		});
        
        try{
        	//Se declara el objeto DBHelper y se abre la conexión con la base de datos
        	bd = new DBHelper(this);
        	bd.open();
        }catch(Exception e){
        	Toast.makeText(this, getString(R.string.BDNoAbierta), Toast.LENGTH_SHORT).show();
        }
    }
    
    //Métodos para el evento onClick de los botones
    public void btnGestion_Click(View v){
    	try{
    		Intent intent = new Intent(this, PantallaGestionPrincipal.class);
    		startActivity(intent);
    	}catch(Exception e){}
    }
    
    public void btnEstadisticas_Click(View v){
    	try{
    		Intent intent = new Intent(this, PantallaEstadisticasPrincipal.class);
    		startActivity(intent);
    	}catch(Exception e){}
    }
}
