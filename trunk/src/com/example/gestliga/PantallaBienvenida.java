//Esta es la pantalla de bienvenida, que simula un proceso de carga y desaparece tras unos segundos

package com.example.gestliga;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class PantallaBienvenida extends Activity {

	private final int SPLASH_DISPLAY_LENGTH = 700; //Tiempo que dura la pantalla 1000 = 1 segundos.
	private ImageView im1, im2, im3, im4, im5;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // Quita el titulo de la aplicación
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // Pone la pantalla completa
		setContentView(R.layout.activity_pantalla_bienvenida);
		
		im1 = (ImageView) findViewById(R.id.img1On);
		im2 = (ImageView) findViewById(R.id.img2On);
		im3 = (ImageView) findViewById(R.id.img3On);
		im4 = (ImageView) findViewById(R.id.img4On);
		im5 = (ImageView) findViewById(R.id.img5On);
		
		//Se programan los hilos para crear el efecto de que la pantalla se está cargando
		new Handler().postDelayed(new Runnable() {
			public void run() {
				im1.setVisibility(View.VISIBLE);
				new Handler().postDelayed(new Runnable() {
					public void run() {
						im2.setVisibility(View.VISIBLE);
						new Handler().postDelayed(new Runnable() {
							public void run() {
								im3.setVisibility(View.VISIBLE);
								new Handler().postDelayed(new Runnable() {
									public void run() {
										im4.setVisibility(View.VISIBLE);
										new Handler().postDelayed(new Runnable() {
											public void run() {
												im5.setVisibility(View.VISIBLE);
												Intent mainIntent = new Intent(PantallaBienvenida.this, PantallaInicial.class);
												mainIntent.putExtra("MAIN", true);
												startActivity(mainIntent);
												finish();
											}
										}, SPLASH_DISPLAY_LENGTH);
									}
								}, SPLASH_DISPLAY_LENGTH);
							}
						}, SPLASH_DISPLAY_LENGTH);
					}
				}, SPLASH_DISPLAY_LENGTH);
			}
		}, SPLASH_DISPLAY_LENGTH);
	}
}
