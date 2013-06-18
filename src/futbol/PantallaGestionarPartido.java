//Esta actividad sirve para introducir los datos de un partido de fútbol

package futbol;

import java.util.ArrayList;
import java.util.Iterator;

import tipos.IncidenciaFutbol;
import tipos.Jugador;
import tipos.MinutosJugador;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gestliga.PantallaInicial;
import com.example.gestliga.R;

public class PantallaGestionarPartido extends Activity {

	private TextView txtEquipoLocal, txtEquipoVisit;
	private EditText edtEquipoLocal, edtEquipoVisit;
	private int equipoLocal, equipoVisit, id_torneo, id_partido, resLocal, resVisit, num_jornada, minuto=0;
	private LinearLayout llGolesLocal, llMinutosLocal, llTarjetasLocal, llGolesVisit, llMinutosVisit, llTarjetasVisit;
	private ArrayList<Jugador> lista_jugadores_local = new ArrayList<Jugador>();
	private ArrayList<Jugador> lista_jugadores_visit = new ArrayList<Jugador>();
	private ArrayList<MinutosJugador> lista_minutos_local = new ArrayList<MinutosJugador>();
	private ArrayList<MinutosJugador> lista_minutos_visit = new ArrayList<MinutosJugador>();
	private ArrayList<IncidenciaFutbol> lista_goles_local = new ArrayList<IncidenciaFutbol>();
	private ArrayList<IncidenciaFutbol> lista_goles_visit = new ArrayList<IncidenciaFutbol>();
	private ArrayList<IncidenciaFutbol> lista_tarjetas_local = new ArrayList<IncidenciaFutbol>();
	private ArrayList<IncidenciaFutbol> lista_tarjetas_visit = new ArrayList<IncidenciaFutbol>();
	private ArrayList<IncidenciaFutbol> lista_tarjetas_color_local = new ArrayList<IncidenciaFutbol>();
	private ArrayList<IncidenciaFutbol> lista_tarjetas_color_visit = new ArrayList<IncidenciaFutbol>();
	
	private int[] datosJugadorLocal = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	private int[] datosMinutosJugadorLocal = {0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	private ArrayList<Integer> datosGolJugadorLocal = new ArrayList<Integer>();
	private ArrayList<Integer> datosMinutoGolJugadorLocal = new ArrayList<Integer>();
	private ArrayList<Integer> datosTarjetaJugadorLocal = new ArrayList<Integer>();
	private ArrayList<Integer> datosMinutoTarjetaJugadorLocal = new ArrayList<Integer>();
	private ArrayList<Integer> datosTarjetaColorJugadorLocal = new ArrayList<Integer>();
	
	private int[] datosJugadorVisit = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	private int[] datosMinutosJugadorVisit = {0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	private ArrayList<Integer> datosGolJugadorVisit = new ArrayList<Integer>();
	private ArrayList<Integer> datosMinutoGolJugadorVisit = new ArrayList<Integer>();
	private ArrayList<Integer> datosTarjetaJugadorVisit = new ArrayList<Integer>();
	private ArrayList<Integer> datosMinutoTarjetaJugadorVisit = new ArrayList<Integer>();
	private ArrayList<Integer> datosTarjetaColorJugadorVisit = new ArrayList<Integer>();
	
	private int posicion_jugador_local = 0, posicion_minuto_local = 0, 
				posicion_gol_local = 0, posicion_minuto_gol_local = 0,
				posicion_tarjeta_local = 0, posicion_minuto_tarjeta_local = 0,
				posicion_jugador_visit = 0, posicion_minuto_visit = 0, 
				posicion_gol_visit = 0, posicion_minuto_gol_visit = 0,
				posicion_tarjeta_visit = 0, posicion_minuto_tarjeta_visit = 0;
	
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.futbol_activity_pantalla_gestionar_partido);
		//Se asignan las referencias de las vistas en el layout
		txtEquipoLocal = (TextView) findViewById(R.id.txtPartidoEquipoLocal);
		txtEquipoVisit = (TextView) findViewById(R.id.txtPartidoEquipoVisit);
		
		edtEquipoLocal = (EditText) findViewById(R.id.edtPartidoEquipoLocalRes);
		edtEquipoVisit = (EditText) findViewById(R.id.edtPartidoEquipoVisitRes);
		//Se recogen los datos del intent
		id_torneo = getIntent().getIntExtra("id_torneo", -1);
		equipoLocal = getIntent().getIntExtra("local", -1);
		equipoVisit = getIntent().getIntExtra("visit", -1);
		num_jornada = getIntent().getIntExtra("jornada", -1);
		id_partido = PantallaInicial.bd.obtenerIDPartido(id_torneo, equipoLocal, equipoVisit);
		
		txtEquipoLocal.setText(PantallaInicial.bd.obtenerNombreEquipo(equipoLocal));
		txtEquipoVisit.setText(PantallaInicial.bd.obtenerNombreEquipo(equipoVisit));
		resLocal = PantallaInicial.bd.obtenerResultado(id_torneo, equipoLocal, num_jornada);
		resVisit = PantallaInicial.bd.obtenerResultado(id_torneo, equipoVisit, num_jornada);
		edtEquipoLocal.setText(resLocal+"");
		edtEquipoLocal.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void afterTextChanged(Editable s) {
				if(edtEquipoLocal.getText().toString().equals("")) resLocal = 0;
				else resLocal = Integer.parseInt(edtEquipoLocal.getText().toString());
				generarGolesLocal();
			}
		});
		edtEquipoVisit.setText(resVisit+"");
		edtEquipoVisit.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void afterTextChanged(Editable s) {
				if(edtEquipoVisit.getText().toString().equals("")) resVisit = 0;
				else resVisit = Integer.parseInt(edtEquipoVisit.getText().toString());
				generarGolesVisit();
			}
		});
		
		llGolesLocal = (LinearLayout) findViewById(R.id.llFutbolGolesLocal);
		llMinutosLocal = (LinearLayout) findViewById(R.id.llFutbolMinutosLocal);
		llTarjetasLocal = (LinearLayout) findViewById(R.id.llFutbolTarjetasLocal);
		llGolesVisit = (LinearLayout) findViewById(R.id.llFutbolGolesVisit);
		llMinutosVisit = (LinearLayout) findViewById(R.id.llFutbolMinutosVisit);
		llTarjetasVisit = (LinearLayout) findViewById(R.id.llFutbolTarjetasVisit);
		
		lista_minutos_local = PantallaInicial.bd.obtenerMinutosJugadores(equipoLocal, id_partido);
		lista_minutos_visit = PantallaInicial.bd.obtenerMinutosJugadores(equipoVisit, id_partido);
		
		lista_goles_local = PantallaInicial.bd.obtenerGolesJugador(equipoLocal, id_partido);
		lista_goles_visit = PantallaInicial.bd.obtenerGolesJugador(equipoVisit, id_partido);
		
		lista_tarjetas_local = PantallaInicial.bd.obtenerTarjetasJugador(equipoLocal, id_partido);
		lista_tarjetas_visit = PantallaInicial.bd.obtenerTarjetasJugador(equipoVisit, id_partido);
		
		ArrayList<IncidenciaFutbol> lista_aux = PantallaInicial.bd.obtenerTarjetasJugador(equipoLocal, id_partido);
		Iterator<IncidenciaFutbol> it = lista_aux.iterator();
		while(it.hasNext()){
			it.next();
			btnGenerarTarjetasLocal_Click(null);
		}
		
		lista_aux = PantallaInicial.bd.obtenerTarjetasJugador(equipoVisit, id_partido);
		it = lista_aux.iterator();
		while(it.hasNext()){
			it.next();
			btnGenerarTarjetasVisit_Click(null);
		}
		
		generarMinutosLocal();
		generarGolesLocal();
		generarMinutosVisit();
		generarGolesVisit();
	}
	//Este método genera los controles para introducir los jugadores locales que han disputado el partido junto con sus minutos
	public void generarMinutosLocal(){
		for(int i = 0; i<14; i++){ //Como mucho jugarán 14 jugadores por equipo
			posicion_jugador_local = -1;
			posicion_minuto_local = 0;
			boolean encontrado = false;
			LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
			LinearLayout llMinutos = (LinearLayout) inflater.inflate(R.layout.futbol_fila_gol, null);
			Spinner spnNombreJugador = (Spinner) llMinutos.findViewById(R.id.spnGol);
			lista_jugadores_local = PantallaInicial.bd.obtenerJugadoresEquipo(equipoLocal);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			Iterator<Jugador> it = lista_jugadores_local.iterator();
			adapter.add(""); //Al añadir un campo vacía al adaptador, en OnItemSelectedListener será position -1
			MinutosJugador m = new MinutosJugador();
			while(it.hasNext()){
				Jugador j = it.next();
				adapter.add(j.getApo());
				Iterator<MinutosJugador> il = lista_minutos_local.iterator();
				while(il.hasNext() && encontrado == false){
					MinutosJugador mj = il.next();
					//Comprobamos si existen datos para cada jugador
					if(mj.getJug() == j.getId()){
						posicion_jugador_local = lista_jugadores_local.indexOf(j);
						posicion_minuto_local = mj.getMin();
						m = mj;
						encontrado = true;
					}
				}
				if(posicion_jugador_local>=0) lista_minutos_local.remove(m);
			}
			spnNombreJugador.setAdapter(adapter);
			spnNombreJugador.setTag(i); //Para recuperar después los valores
			spnNombreJugador.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
					int tag = Integer.parseInt(parent.getTag()+"");
					if(parent.getItemAtPosition(position).toString().equals("")){
						datosJugadorLocal[tag] = -1;
					}
					else{
						datosJugadorLocal[tag] = lista_jugadores_local.get(position-1).getId();
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {}
			});
			if(posicion_jugador_local>=0){
				spnNombreJugador.setSelection(posicion_jugador_local+1);
			}
			Spinner spnMinuto = (Spinner) llMinutos.findViewById(R.id.spnMinuto);
			spnMinuto.setTag(i); //Para recuperar después los valores
			spnMinuto.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
					int tag = Integer.parseInt(parent.getTag()+"");
					datosMinutosJugadorLocal[tag] = Integer.parseInt(parent.getItemAtPosition(position).toString());
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {}
			});
			ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.minutos, android.R.layout.simple_spinner_item);
			adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spnMinuto.setAdapter(adapter2);
			spnMinuto.setSelection(posicion_minuto_local);
			
			llMinutosLocal.addView(llMinutos);
		}
	}
	
	//Este método genera los controles para introducir los jugadores que han marcado gol junto con su minuto
	public void generarGolesLocal(){
		if(llGolesLocal.getChildCount()>1) llGolesLocal.removeViews(1, llGolesLocal.getChildCount()-1);
		for(int i=0; i<resLocal; i++){
			posicion_gol_local = -1;
			posicion_minuto_gol_local = 0;
			boolean encontrado = false;
			datosGolJugadorLocal.add(-1); //Quitar si no funcionas
			datosMinutoGolJugadorLocal.add(0); //Quitar si no funciona
			LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
			LinearLayout llGoles = (LinearLayout) inflater.inflate(R.layout.futbol_fila_gol, null);
			Spinner spnNombreJugador = (Spinner) llGoles.findViewById(R.id.spnGol);
			lista_jugadores_local = PantallaInicial.bd.obtenerJugadoresEquipo(equipoLocal);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			Iterator<Jugador> it = lista_jugadores_local.iterator();
			adapter.add("");
			IncidenciaFutbol inf = new IncidenciaFutbol();
			//Compruebo si ya existen datos para cargarlos
			while(it.hasNext()){
				Jugador j = it.next();
				adapter.add(j.getApo());
				Iterator<IncidenciaFutbol> ij = lista_goles_local.iterator();
				while(ij.hasNext() && encontrado == false){
					IncidenciaFutbol inc = ij.next();
					if(inc.getJug() == j.getId()){
						posicion_gol_local = lista_jugadores_local.indexOf(j);
						encontrado = true;
						inf = inc;
						posicion_minuto_gol_local = inc.getMin();
					}
				}
			}
			if(posicion_gol_local>-1) lista_goles_local.remove(inf); 
			spnNombreJugador.setAdapter(adapter);
			spnNombreJugador.setTag(i); ////Para recuperar después los valores
			spnNombreJugador.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
					int tag = Integer.parseInt(parent.getTag()+"");
					if(parent.getItemAtPosition(position).toString().equals("")){
						datosGolJugadorLocal.set(tag, -1);
					}
					else{
						datosGolJugadorLocal.set(tag, lista_jugadores_local.get(position-1).getId());
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {}
			});
			spnNombreJugador.setSelection(posicion_gol_local+1);
			Spinner spnMinuto = (Spinner) llGoles.findViewById(R.id.spnMinuto);
			spnMinuto.setTag(i); //Para recuperar después los valores
			spnMinuto.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
					int tag = Integer.parseInt(parent.getTag()+"");
					datosMinutoGolJugadorLocal.set(tag, Integer.parseInt(parent.getItemAtPosition(position).toString()));
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {}
			});
			ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.minutos, android.R.layout.simple_spinner_item);
			adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spnMinuto.setAdapter(adapter2);
			spnMinuto.setSelection(posicion_minuto_gol_local);
			llGolesLocal.addView(llGoles);
		}
	}
	
	//Método para generar los controles para introducir las tarjetas de los jugadores locales
	public void btnGenerarTarjetasLocal_Click(View v){
		posicion_tarjeta_local = -1;
		posicion_minuto_tarjeta_local = 0;
		boolean encontrado = false;
		int color = 0;
		datosTarjetaJugadorLocal.add(-1);
		datosMinutoTarjetaJugadorLocal.add(0);
		datosTarjetaColorJugadorLocal.add(1);
		LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
		LinearLayout llTarjeta = (LinearLayout) inflater.inflate(R.layout.futbol_fila_tarjeta, null);
		Spinner spnNombreJugador = (Spinner) llTarjeta.findViewById(R.id.spnGol);
		lista_jugadores_local = PantallaInicial.bd.obtenerJugadoresEquipo(equipoLocal);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Iterator<Jugador> it = lista_jugadores_local.iterator();
		adapter.add("");
		IncidenciaFutbol inf = new IncidenciaFutbol();
		while(it.hasNext()){
			Jugador j = it.next();
			adapter.add(j.getApo());
			Iterator<IncidenciaFutbol> ij = lista_tarjetas_local.iterator();
			while(ij.hasNext() && encontrado == false){
				IncidenciaFutbol inc = ij.next();
				if(inc.getJug() == j.getId()){
					posicion_tarjeta_local = lista_jugadores_local.indexOf(j);
					encontrado = true;
					inf = inc;
					posicion_minuto_tarjeta_local = inc.getMin();
					color = inc.getCol();
				}
			}
		}
		if(posicion_tarjeta_local>-1) lista_tarjetas_local.remove(inf); 
		spnNombreJugador.setAdapter(adapter);
		spnNombreJugador.setTag(llTarjetasLocal.getChildCount()-1); //Para recuperar después los valores
		spnNombreJugador.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
				int tag = Integer.parseInt(parent.getTag()+"");
				if(parent.getItemAtPosition(position).toString().equals("")){
					datosTarjetaJugadorLocal.set(tag, -1);
				}
				else{
					datosTarjetaJugadorLocal.set(tag, lista_jugadores_local.get(position-1).getId());
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		spnNombreJugador.setSelection(posicion_tarjeta_local+1);
		Spinner spnMinuto = (Spinner) llTarjeta.findViewById(R.id.spnMinuto);
		spnMinuto.setTag(llTarjetasLocal.getChildCount()-1); //Para recuperar después los valores
		spnMinuto.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
				int tag = Integer.parseInt(parent.getTag()+"");
				datosMinutoTarjetaJugadorLocal.set(tag, Integer.parseInt(parent.getItemAtPosition(position).toString()));
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.minutos, android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnMinuto.setAdapter(adapter2);
		spnMinuto.setSelection(posicion_minuto_tarjeta_local);
		
		RadioButton rbA = (RadioButton) llTarjeta.findViewById(R.id.rbA);
		rbA.setTag(llTarjetasLocal.getChildCount()-1);
		rbA.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				int tag = Integer.parseInt(buttonView.getTag()+"");
				if(isChecked) datosTarjetaColorJugadorLocal.set(tag, 1);
			}
		});
		RadioButton rbR = (RadioButton) llTarjeta.findViewById(R.id.rbR);
		rbR.setTag(llTarjetasLocal.getChildCount()-1);
		rbR.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				int tag = Integer.parseInt(buttonView.getTag()+"");
				if(isChecked) datosTarjetaColorJugadorLocal.set(tag, 2);
			}
		});
		llTarjetasLocal.addView(llTarjeta);
		if(color==1) rbA.setChecked(true);
		else if(color==2) rbR.setChecked(true);
	}
	
	public void btnBorrarTarjetasLocal_Click(View v){
		if(llTarjetasLocal.getChildCount()>1){
			datosTarjetaJugadorLocal.remove(datosTarjetaJugadorLocal.size()-1);
			datosMinutoTarjetaJugadorLocal.remove(datosMinutoTarjetaJugadorLocal.size()-1);
			llTarjetasLocal.removeViewAt(llTarjetasLocal.getChildCount()-1);
		}
	}
	
	public void generarMinutosVisit(){
		for(int i = 0; i<14; i++){ //Como mucho jugarán 14 jugadores por equipo
			posicion_jugador_visit = -1;
			posicion_minuto_visit = 0;
			boolean encontrado = false;
			LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
			LinearLayout llMinutos = (LinearLayout) inflater.inflate(R.layout.futbol_fila_gol, null);
			Spinner spnNombreJugador = (Spinner) llMinutos.findViewById(R.id.spnGol);
			lista_jugadores_visit = PantallaInicial.bd.obtenerJugadoresEquipo(equipoVisit);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			Iterator<Jugador> it = lista_jugadores_visit.iterator();
			adapter.add("");
			MinutosJugador m = new MinutosJugador();
			while(it.hasNext()){
				Jugador j = it.next();
				adapter.add(j.getApo());
				Iterator<MinutosJugador> il = lista_minutos_visit.iterator();
				while(il.hasNext() && encontrado == false){
					MinutosJugador mj = il.next();
					if(mj.getJug() == j.getId()){
						posicion_jugador_visit = lista_jugadores_visit.indexOf(j);
						posicion_minuto_visit = mj.getMin();
						m = mj;
						encontrado = true;
					}
				}
				if(posicion_jugador_visit>=0) lista_minutos_visit.remove(m);
			}
			spnNombreJugador.setAdapter(adapter);
			spnNombreJugador.setTag(i); //Para recuperar después los valores
			spnNombreJugador.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
					int tag = Integer.parseInt(parent.getTag()+"");
					if(parent.getItemAtPosition(position).toString().equals("")){
						datosJugadorVisit[tag] = -1;
					}
					else{
						datosJugadorVisit[tag] = lista_jugadores_visit.get(position-1).getId();
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {}
			});
			if(posicion_jugador_visit>=0){
				spnNombreJugador.setSelection(posicion_jugador_visit+1);
			}
			Spinner spnMinuto = (Spinner) llMinutos.findViewById(R.id.spnMinuto);
			spnMinuto.setTag(i); //Para recuperar después los valores
			spnMinuto.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
					int tag = Integer.parseInt(parent.getTag()+"");
					datosMinutosJugadorVisit[tag] = Integer.parseInt(parent.getItemAtPosition(position).toString());
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {}
			});
			ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.minutos, android.R.layout.simple_spinner_item);
			adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spnMinuto.setAdapter(adapter2);
			spnMinuto.setSelection(posicion_minuto_visit);
			llMinutosVisit.addView(llMinutos);
		}
	}
	
	public void generarGolesVisit(){
		if(llGolesVisit.getChildCount()>1) llGolesVisit.removeViews(1, llGolesVisit.getChildCount()-1);
		for(int i=0; i<resVisit; i++){
			posicion_gol_visit = -1;
			posicion_minuto_gol_visit = 0;
			boolean encontrado = false;
			datosGolJugadorVisit.add(-1);
			datosMinutoGolJugadorVisit.add(0);
			LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
			LinearLayout llGoles = (LinearLayout) inflater.inflate(R.layout.futbol_fila_gol, null);
			Spinner spnNombreJugador = (Spinner) llGoles.findViewById(R.id.spnGol);
			lista_jugadores_visit = PantallaInicial.bd.obtenerJugadoresEquipo(equipoVisit);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			Iterator<Jugador> it = lista_jugadores_visit.iterator();
			adapter.add("");
			IncidenciaFutbol inf = new IncidenciaFutbol();
			while(it.hasNext()){
				Jugador j = it.next();
				adapter.add(j.getApo());
				Iterator<IncidenciaFutbol> ij = lista_goles_visit.iterator();
				while(ij.hasNext() && encontrado == false){
					IncidenciaFutbol inc = ij.next();
					if(inc.getJug() == j.getId()){
						posicion_gol_visit = lista_jugadores_visit.indexOf(j);
						encontrado = true;
						inf = inc;
						posicion_minuto_gol_visit = inc.getMin();
					}
				}
			}
			if(posicion_gol_visit>-1) lista_goles_visit.remove(inf); 
			spnNombreJugador.setAdapter(adapter);
			spnNombreJugador.setTag(i); //Para recuperar después los valores
			spnNombreJugador.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
					int tag = Integer.parseInt(parent.getTag()+"");
					if(parent.getItemAtPosition(position).toString().equals("")){
						datosGolJugadorVisit.set(tag, -1);
					}
					else{
						datosGolJugadorVisit.set(tag, lista_jugadores_visit.get(position-1).getId());
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {}
			});
			spnNombreJugador.setSelection(posicion_gol_visit+1);
			Spinner spnMinuto = (Spinner) llGoles.findViewById(R.id.spnMinuto);
			spnMinuto.setTag(i); //Para recuperar después los valores
			spnMinuto.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
					int tag = Integer.parseInt(parent.getTag()+"");
					datosMinutoGolJugadorVisit.set(tag, Integer.parseInt(parent.getItemAtPosition(position).toString()));
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {}
			});
			ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.minutos, android.R.layout.simple_spinner_item);
			adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spnMinuto.setAdapter(adapter2);
			spnMinuto.setSelection(posicion_minuto_gol_visit);
			llGolesVisit.addView(llGoles);
		}
	}
	
	public void btnGenerarTarjetasVisit_Click(View v){
		posicion_tarjeta_visit = -1;
		posicion_minuto_tarjeta_visit = 0;
		boolean encontrado = false;
		int color = 0;
		datosTarjetaJugadorVisit.add(-1); 
		datosMinutoTarjetaJugadorVisit.add(0);
		datosTarjetaColorJugadorVisit.add(1);
		LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
		LinearLayout llTarjeta = (LinearLayout) inflater.inflate(R.layout.futbol_fila_tarjeta, null);
		Spinner spnNombreJugador = (Spinner) llTarjeta.findViewById(R.id.spnGol);
		lista_jugadores_visit = PantallaInicial.bd.obtenerJugadoresEquipo(equipoVisit);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Iterator<Jugador> it = lista_jugadores_visit.iterator();
		adapter.add("");
		IncidenciaFutbol inf = new IncidenciaFutbol();
		while(it.hasNext()){
			Jugador j = it.next();
			adapter.add(j.getApo());
			Iterator<IncidenciaFutbol> ij = lista_tarjetas_visit.iterator();
			while(ij.hasNext() && encontrado == false){
				IncidenciaFutbol inc = ij.next();
				if(inc.getJug() == j.getId()){
					posicion_tarjeta_visit = lista_jugadores_visit.indexOf(j);
					encontrado = true;
					inf = inc;
					posicion_minuto_tarjeta_visit = inc.getMin();
					color = inc.getCol();
				}
			}
		}
		if(posicion_tarjeta_visit>-1) lista_tarjetas_visit.remove(inf); 
		spnNombreJugador.setAdapter(adapter);
		spnNombreJugador.setTag(llTarjetasVisit.getChildCount()-1); //Para recuperar después los valores
		spnNombreJugador.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
				int tag = Integer.parseInt(parent.getTag()+"");
				if(parent.getItemAtPosition(position).toString().equals("")){
					datosTarjetaJugadorVisit.set(tag, -1);
				}
				else{
					datosTarjetaJugadorVisit.set(tag, lista_jugadores_visit.get(position-1).getId());
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		spnNombreJugador.setSelection(posicion_tarjeta_visit+1);
		Spinner spnMinuto = (Spinner) llTarjeta.findViewById(R.id.spnMinuto);
		spnMinuto.setTag(llTarjetasVisit.getChildCount()-1); //Para recuperar después los valores
		spnMinuto.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
				int tag = Integer.parseInt(parent.getTag()+"");
				datosMinutoTarjetaJugadorVisit.set(tag, Integer.parseInt(parent.getItemAtPosition(position).toString()));
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.minutos, android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnMinuto.setAdapter(adapter2);
		spnMinuto.setSelection(posicion_minuto_tarjeta_visit);
		
		RadioButton rbA = (RadioButton) llTarjeta.findViewById(R.id.rbA);
		rbA.setTag(llTarjetasVisit.getChildCount()-1);
		rbA.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				int tag = Integer.parseInt(buttonView.getTag()+"");
				if(isChecked) datosTarjetaColorJugadorVisit.set(tag, 1);
			}
		});
		
		RadioButton rbR = (RadioButton) llTarjeta.findViewById(R.id.rbR);
		rbR.setTag(llTarjetasVisit.getChildCount()-1);
		rbR.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				int tag = Integer.parseInt(buttonView.getTag()+"");
				if(isChecked) datosTarjetaColorJugadorVisit.set(tag, 2);
			}
		});
		if(color==1) rbA.setChecked(true);
		else if(color==2) rbR.setChecked(true);
		llTarjetasVisit.addView(llTarjeta);
	}
	
	public void btnBorrarTarjetasVisit_Click(View v){
		if(llTarjetasVisit.getChildCount()>1){
			datosTarjetaJugadorVisit.remove(datosTarjetaJugadorVisit.size()-1);
			datosMinutoTarjetaJugadorVisit.remove(datosMinutoTarjetaJugadorVisit.size()-1);
			llTarjetasVisit.removeViewAt(llTarjetasVisit.getChildCount()-1);
		}
	}
	
	public void btnGuardar_Click(View v){
		try {
			//Primero, introducimos el resultado
			PantallaInicial.bd.insertarResultado(id_partido, equipoLocal, resLocal);
			PantallaInicial.bd.insertarResultado(id_partido, equipoVisit, resVisit);
			
			//Segundo, introducimos los minutos disputados por cada jugador. Antes borro los que ya hubiese
			PantallaInicial.bd.insertarMinutosJugador(id_partido, datosJugadorLocal, datosMinutosJugadorLocal, lista_jugadores_local);
			PantallaInicial.bd.insertarMinutosJugador(id_partido, datosJugadorVisit, datosMinutosJugadorVisit, lista_jugadores_visit);
			
			//Tercero, introducimos los goles, si los hubiere, de cada jugador
			PantallaInicial.bd.insertarDatosPartidoJugador(id_partido, datosGolJugadorLocal, datosMinutoGolJugadorLocal, datosTarjetaJugadorLocal, datosMinutoTarjetaJugadorLocal, datosTarjetaColorJugadorLocal, lista_jugadores_local);
			PantallaInicial.bd.insertarDatosPartidoJugador(id_partido, datosGolJugadorVisit, datosMinutoGolJugadorVisit, datosTarjetaJugadorVisit, datosMinutoTarjetaJugadorVisit, datosTarjetaColorJugadorVisit, lista_jugadores_visit);
			
			Toast.makeText(this, getString(R.string.BDInserta), Toast.LENGTH_SHORT).show();
			finish();
		} catch (Exception e) {
			Toast.makeText(this, getString(R.string.BDNoInserta), Toast.LENGTH_SHORT).show();
		}
	}
}
