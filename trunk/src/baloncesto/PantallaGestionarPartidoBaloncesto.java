//Esta actividad sirve para introducir los datos de un partido de baloncesto

package baloncesto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import tipos.DatosJugadorPartidoBC;
import tipos.Jugador;
import tipos.Partido;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gestliga.PantallaInicial;
import com.example.gestliga.R;

public class PantallaGestionarPartidoBaloncesto extends Activity {

	private TextView txtEquipoLocal, txtEquipoVisit, txtEquipoLocalResumen, txtEquipoVisitResumen, txtFecha;
	private EditText edtEquipoLocalRes, edtEquipoVisitRes;
	private Spinner spnMVPLocal, spnMVPVisit, spnHora;
	private ImageButton btnSeleccionarFecha;
	private LinearLayout llResumenLocal, llResumenVisit;
	private ArrayList<Jugador> lista_jugadores_local = new ArrayList<Jugador>();
	private ArrayList<Jugador> lista_jugadores_visit = new ArrayList<Jugador>();
	private ArrayList<DatosJugadorPartidoBC> lista_datos_local = new ArrayList<DatosJugadorPartidoBC>();
	private ArrayList<DatosJugadorPartidoBC> lista_datos_visit = new ArrayList<DatosJugadorPartidoBC>();
	private Partido partido;
	private int[] jugadoresLocal = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	private int[] jugadoresVisit = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	private int[] minutosLocal = {0,0,0,0,0,0,0,0,0,0,0,0};
	private int[] minutosVisit = {0,0,0,0,0,0,0,0,0,0,0,0};
	private int[] puntosLocal = {0,0,0,0,0,0,0,0,0,0,0,0};
	private int[] puntosVisit = {0,0,0,0,0,0,0,0,0,0,0,0};
	private int[] rebotesLocal = {0,0,0,0,0,0,0,0,0,0,0,0};
	private int[] rebotesVisit = {0,0,0,0,0,0,0,0,0,0,0,0};
	private int[] taponesLocal = {0,0,0,0,0,0,0,0,0,0,0,0};
	private int[] taponesVisit = {0,0,0,0,0,0,0,0,0,0,0,0};
	private int[] faltasLocal = {0,0,0,0,0,0,0,0,0,0,0,0};
	private int[] faltasVisit = {0,0,0,0,0,0,0,0,0,0,0,0};
	
	private int id_torneo, local, visit, jornada, id_partido, resLocal=0, resVisit=0;
	private int posicionMVP = -1, id_jug_MVPLocal = -1, id_jug_MVPVisit = -1, id_jug_MVP = -1, posicion_local = 0, posicion_visit = 0,
				posicion_faltas_local = 0, posicion_faltas_visit = 0,
				posicion_mj_local = 0, posicion_mj_visit = 0,
				posicion_pts_local = 0, posicion_pts_visit = 0,
				posicion_tap_local = 0, posicion_tap_visit = 0,
				posicion_reb_local = 0, posicion_reb_visit = 0;
	
	private int minutoLocal = 0, minutoVisit = 0, puntoLocal = 0, puntoVisit = 0, rebLocal = 0, rebVisit = 0,
				tapLocal = 0, tapVisit = 0;
	
	private int _anio; private int _mes; private int _dia;
	private String hora="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baloncesto_activity_pantalla_gestionar_partido);
		
		//Se asignan las referencias de las vistas en el layout
		txtEquipoLocal = (TextView) findViewById(R.id.txtPartidoEquipoLocalBC);
		txtEquipoVisit = (TextView) findViewById(R.id.txtPartidoEquipoVisitBC);
		txtEquipoLocalResumen = (TextView) findViewById(R.id.txtEquipoLocalResumenBC);
		txtEquipoVisitResumen = (TextView) findViewById(R.id.txtEquipoVisitResumenBC);
		edtEquipoLocalRes = (EditText) findViewById(R.id.edtPartidoEquipoLocalResBC);
		edtEquipoLocalRes.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void afterTextChanged(Editable s) {
				if(edtEquipoLocalRes.getText().toString().length()>0) resLocal = Integer.parseInt(edtEquipoLocalRes.getText().toString());
				else resLocal = 0;
				txtEquipoLocalResumen.setText(txtEquipoLocal.getText().toString() + " - " + resLocal);
			}
		});
		edtEquipoLocalRes.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				edtEquipoLocalRes.setText("");
				return false;
			}
		});
		edtEquipoVisitRes = (EditText) findViewById(R.id.edtPartidoEquipoVisitResBC);
		edtEquipoVisitRes.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void afterTextChanged(Editable s) {
				if(edtEquipoVisitRes.getText().toString().length()>0) resVisit = Integer.parseInt(edtEquipoVisitRes.getText().toString());
				else resVisit = 0;
				txtEquipoVisitResumen.setText(txtEquipoVisit.getText().toString() + " - " + resVisit);
			}
		});
		edtEquipoVisitRes.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				edtEquipoVisitRes.setText("");
				return false;
			}
		});
		spnMVPLocal = (Spinner) findViewById(R.id.spnMVPLocal);
		spnMVPVisit = (Spinner) findViewById(R.id.spnMVPVisit);
		llResumenLocal = (LinearLayout) findViewById(R.id.llResumenBCLocal);
		llResumenVisit = (LinearLayout) findViewById(R.id.llResumenBCVisit);
		
		//Se recogen los datos del intent
		id_torneo = getIntent().getIntExtra("id_torneo", -1);
		local = getIntent().getIntExtra("local", -1);
		visit = getIntent().getIntExtra("visit", -1);
		jornada = getIntent().getIntExtra("jornada", -1);
		
		txtEquipoLocal.setText(PantallaInicial.bd.obtenerNombreEquipo(local));
		txtEquipoVisit.setText(PantallaInicial.bd.obtenerNombreEquipo(visit));
		edtEquipoLocalRes.setText(PantallaInicial.bd.obtenerResultado(id_torneo, local, jornada)+"");
		edtEquipoVisitRes.setText(PantallaInicial.bd.obtenerResultado(id_torneo, visit, jornada)+"");
		
		spnHora = (Spinner) findViewById(R.id.spnHoraPartidoBC);
		spnHora.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
				hora = parent.getItemAtPosition(position).toString();
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		txtFecha = (TextView) findViewById(R.id.txtFechaPartidoBC);
		btnSeleccionarFecha = (ImageButton) findViewById(R.id.btnSeleccionarFechaPartidoBC);
		btnSeleccionarFecha.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(0);
			}
		});
		final Calendar _cal = Calendar.getInstance();
		_anio = _cal.get(Calendar.YEAR);
		_mes = _cal.get(Calendar.MONTH);
		_dia = _cal.get(Calendar.DAY_OF_MONTH);
		updateDisplay();
		
		txtEquipoLocalResumen.setText(txtEquipoLocal.getText().toString() + " - " + resLocal);
		txtEquipoVisitResumen.setText(txtEquipoVisit.getText().toString() + " - " + resVisit);
		
		//Se comprueba para cada control, si existen datos en la BD para cargarlos al iniciar la actividad
		id_partido = PantallaInicial.bd.obtenerIDPartido(id_torneo, local, visit);
		partido = PantallaInicial.bd.obtenerPartido(id_partido);
		if(partido.getFec().length()>0){
			txtFecha.setText(partido.getFec());
		}
		if(partido.getHor().length()>0){
			for(int i=0; i<spnHora.getCount(); i++){
				if(partido.getHor().equals(spnHora.getItemAtPosition(i))) spnHora.setSelection(i);
			}
		}
		
		lista_jugadores_local = PantallaInicial.bd.obtenerJugadoresEquipo(local);
		lista_jugadores_visit = PantallaInicial.bd.obtenerJugadoresEquipo(visit);
		lista_datos_local = PantallaInicial.bd.obtenerDatosJugadorPartidoBC(id_partido, lista_jugadores_local);
		lista_datos_visit = PantallaInicial.bd.obtenerDatosJugadorPartidoBC(id_partido, lista_jugadores_visit);
		//Equipo Local
		//En baloncesto, como mucho jugarán 12
		for(int i = 0; i<12; i++){
			posicion_local = -1;
			posicion_faltas_local = 0;
			minutoLocal = 0;
			puntoLocal = 0;
			rebLocal = 0;
			tapLocal = 0;
			boolean encontrado = false;
			int posicion_falta = 0;
			LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
			LinearLayout llDatos = (LinearLayout) inflater.inflate(R.layout.baloncesto_fila_resumen, null);
			Spinner spnNombreJugador = (Spinner) llDatos.findViewById(R.id.spnJugador);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			Iterator<Jugador> it = lista_jugadores_local.iterator();
			adapter.add("");
			DatosJugadorPartidoBC d = new DatosJugadorPartidoBC();
			while(it.hasNext()){
				Jugador j = it.next();
				adapter.add(j.getApo());
				Iterator<DatosJugadorPartidoBC> il = lista_datos_local.iterator();
				//Comprobamos si hay jugadores con datos de este partido, para pre-cargarlos
				while(il.hasNext() && encontrado == false){
					DatosJugadorPartidoBC da = il.next();
					if(j.getId() == da.getJug()){
						posicion_local = lista_jugadores_local.indexOf(j);
						posicion_falta = da.getFal();
						encontrado = true;
						d = da;
						minutoLocal = PantallaInicial.bd.obtenerMinutosJugador(id_partido, j.getId());
						minutosLocal[i] = minutoLocal;
						puntoLocal = da.getPun();
						puntosLocal[i] = puntoLocal;
						rebLocal = da.getReb();
						rebotesLocal[i] = rebLocal;
						tapLocal = da.getTap();
						taponesLocal[i] = tapLocal;
						if(da.getMvp()==1){
							id_jug_MVPLocal = j.getId();
						}
					}
				}
				if(posicion_local > -1) lista_datos_local.remove(d);
			}
			spnNombreJugador.setAdapter(adapter);
			spnNombreJugador.setTag(i); //Para recuperar después los valores
			spnNombreJugador.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
					int tag = Integer.parseInt(parent.getTag()+"");
					if(parent.getItemAtPosition(position).toString().equals("")){
						jugadoresLocal[tag] = -1;
					}
					else{
						jugadoresLocal[tag] = lista_jugadores_local.get(position-1).getId();
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {}
			});
			if(posicion_local>=0){
				spnNombreJugador.setSelection(posicion_local+1);
			}
			
			EditText mj = (EditText) llDatos.findViewById(R.id.edtMJ);
			mj.setText(minutoLocal+"");
			mj.setTag(i);
			mj.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if(hasFocus) posicion_mj_local = Integer.parseInt(v.getTag()+"");
				}
			});
			mj.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {}
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
				@Override
				public void afterTextChanged(Editable s) {
					if(s.toString().length()>0) minutosLocal[posicion_mj_local] = Integer.parseInt(s.toString());
					else minutosLocal[posicion_mj_local] = 0;
				}
			});
			EditText pts = (EditText) llDatos.findViewById(R.id.edtPts);
			pts.setText(puntoLocal+"");
			pts.setTag(i);
			pts.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if(hasFocus) posicion_pts_local = Integer.parseInt(v.getTag()+"");
				}
			});
			pts.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {}
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
				@Override
				public void afterTextChanged(Editable s) {
					if(s.toString().length()>0) puntosLocal[posicion_pts_local] = Integer.parseInt(s.toString());
					else puntosLocal[posicion_pts_local] = 0;
				}
			});
			EditText reb = (EditText) llDatos.findViewById(R.id.edtReb);
			reb.setText(rebLocal+"");
			reb.setTag(i);
			reb.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if(hasFocus) posicion_reb_local = Integer.parseInt(v.getTag()+"");
				}
			});
			reb.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {}
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
				@Override
				public void afterTextChanged(Editable s) {
					if(s.toString().length()>0) rebotesLocal[posicion_reb_local] = Integer.parseInt(s.toString());
					else rebotesLocal[posicion_reb_local] = 0;
				}
			});
			EditText tap = (EditText) llDatos.findViewById(R.id.edtTap);
			tap.setText(tapLocal+"");
			tap.setTag(i);
			tap.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if(hasFocus) posicion_tap_local = Integer.parseInt(v.getTag()+"");
				}
			});
			tap.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {}
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
				@Override
				public void afterTextChanged(Editable s) {
					if(s.toString().length()>0) taponesLocal[posicion_tap_local] = Integer.parseInt(s.toString());
					else taponesLocal[posicion_tap_local] = 0;
				}
			});
			
			Spinner spnFalta = (Spinner) llDatos.findViewById(R.id.spnFal);
			spnFalta.setTag(i); //Para recuperar después los valores
			spnFalta.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
					int tag = Integer.parseInt(parent.getTag()+"");
					faltasLocal[tag] = Integer.parseInt(parent.getItemAtPosition(position).toString());
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {}
			});
			ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.faltas, android.R.layout.simple_spinner_item);
			adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spnFalta.setAdapter(adapter2);
			spnFalta.setSelection(posicion_falta);
			
			llResumenLocal.addView(llDatos);
		}
		//Equipo Visitante
		//Se realizan las mismas operaciones que con el equipo local
		for(int i = 0; i<12; i++){
			posicion_visit = -1;
			posicion_faltas_visit = 0;
			minutoVisit = 0;
			puntoVisit = 0;
			rebVisit = 0;
			tapVisit = 0;
			boolean encontrado = false;
			int posicion_falta = 0;
			LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
			LinearLayout llDatos = (LinearLayout) inflater.inflate(R.layout.baloncesto_fila_resumen, null);
			Spinner spnNombreJugador = (Spinner) llDatos.findViewById(R.id.spnJugador);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			Iterator<Jugador> it = lista_jugadores_visit.iterator();
			adapter.add("");
			DatosJugadorPartidoBC d = new DatosJugadorPartidoBC();
			while(it.hasNext()){
				Jugador j = it.next();
				adapter.add(j.getApo());
				Iterator<DatosJugadorPartidoBC> il = lista_datos_visit.iterator();
				while(il.hasNext() && encontrado == false){
					DatosJugadorPartidoBC da = il.next();
					if(j.getId() == da.getJug()){
						posicion_visit = lista_jugadores_visit.indexOf(j);
						posicion_falta = da.getFal();
						encontrado = true;
						d = da;
						minutoVisit = PantallaInicial.bd.obtenerMinutosJugador(id_partido, j.getId());
						minutosVisit[i] = minutoVisit;
						puntoVisit = da.getPun();
						puntosVisit[i] = puntoVisit;
						rebVisit = da.getReb();
						rebotesVisit[i] = rebVisit;
						tapVisit = da.getTap();
						taponesVisit[i] = tapVisit;
						if(da.getMvp()==1){
							id_jug_MVPVisit = j.getId();
						}
					}
				}
				if(posicion_visit > -1) lista_datos_visit.remove(d);
			}
			spnNombreJugador.setAdapter(adapter);
			spnNombreJugador.setTag(i); //Para recuperar después los valores
			spnNombreJugador.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
					int tag = Integer.parseInt(parent.getTag()+"");
					if(parent.getItemAtPosition(position).toString().equals("")){
						jugadoresVisit[tag] = -1;
					}
					else{
						jugadoresVisit[tag] = lista_jugadores_visit.get(position-1).getId();
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {}
			});
			if(posicion_visit>=0){
				spnNombreJugador.setSelection(posicion_visit+1);
			}
			
			EditText mj = (EditText) llDatos.findViewById(R.id.edtMJ);
			mj.setText(minutoVisit+"");
			mj.setTag(i);
			mj.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if(hasFocus) posicion_mj_visit = Integer.parseInt(v.getTag()+"");
				}
			});
			mj.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {}
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
				@Override
				public void afterTextChanged(Editable s) {
					Log.d("MJ", String.valueOf(s.toString()));
					if(s.toString().length()>0) minutosVisit[posicion_mj_visit] = Integer.parseInt(s.toString());
					else minutosVisit[posicion_mj_visit] = 0;
				}
			});
			EditText pts = (EditText) llDatos.findViewById(R.id.edtPts);
			pts.setText(puntoVisit+"");
			pts.setTag(i);
			pts.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if(hasFocus) posicion_pts_visit = Integer.parseInt(v.getTag()+"");
				}
			});
			pts.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {}
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
				@Override
				public void afterTextChanged(Editable s) {
					if(s.toString().length()>0) puntosVisit[posicion_pts_visit] = Integer.parseInt(s.toString());
					else puntosVisit[posicion_pts_visit] = 0;
				}
			});
			EditText reb = (EditText) llDatos.findViewById(R.id.edtReb);
			reb.setText(rebVisit+"");
			reb.setTag(i);
			reb.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if(hasFocus) posicion_reb_visit = Integer.parseInt(v.getTag()+"");
				}
			});
			reb.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {}
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
				@Override
				public void afterTextChanged(Editable s) {
					if(s.toString().length()>0) rebotesVisit[posicion_reb_visit] = Integer.parseInt(s.toString());
					else rebotesVisit[posicion_reb_visit] = 0;
				}
			});
			EditText tap = (EditText) llDatos.findViewById(R.id.edtTap);
			tap.setText(tapVisit+"");
			tap.setTag(i);
			tap.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if(hasFocus) posicion_tap_visit = Integer.parseInt(v.getTag()+"");
				}
			});
			tap.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {}
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
				@Override
				public void afterTextChanged(Editable s) {
					if(s.toString().length()>0) taponesVisit[posicion_tap_visit] = Integer.parseInt(s.toString());
					else taponesVisit[posicion_tap_visit] = 0;
				}
			});
			
			Spinner spnFalta = (Spinner) llDatos.findViewById(R.id.spnFal);
			spnFalta.setTag(i); //Para recuperar después los valores
			spnFalta.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
					int tag = Integer.parseInt(parent.getTag()+"");
					faltasVisit[tag] = Integer.parseInt(parent.getItemAtPosition(position).toString());
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {}
			});
			ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.faltas, android.R.layout.simple_spinner_item);
			adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spnFalta.setAdapter(adapter2);
			spnFalta.setSelection(posicion_falta);
			
			llResumenVisit.addView(llDatos);
		}
		//MVP
		//Se cargan dos spinner con los datos de los jugadores local y visitante. Sólo puede haber un MVP por partido, por ello, sólo será válido el último jugador escogido
		ArrayAdapter<String> adapterMVP = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		adapterMVP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Iterator<Jugador> itMVP = lista_jugadores_local.iterator();
		adapterMVP.add("");
		while(itMVP.hasNext()){
			Jugador j = itMVP.next();
			adapterMVP.add(j.getNom());
			if(PantallaInicial.bd.obtenerMVP(id_partido, j.getId())){
				posicionMVP = lista_jugadores_local.indexOf(j);
				id_jug_MVPLocal = j.getId();
			}
		}
		spnMVPLocal.setAdapter(adapterMVP);
		spnMVPLocal.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
				if(parent.getItemAtPosition(position).toString().equals("")) id_jug_MVPLocal = -1;
				else id_jug_MVPLocal = lista_jugadores_local.get(position-1).getId();
				if(id_jug_MVPLocal!=-1) id_jug_MVP = id_jug_MVPLocal;
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		if(posicionMVP>-1) spnMVPLocal.setSelection(posicionMVP+1);
		
		posicionMVP=-1;
		adapterMVP = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		adapterMVP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		itMVP = lista_jugadores_visit.iterator();
		adapterMVP.add("");
		while(itMVP.hasNext()){
			Jugador j = itMVP.next();
			adapterMVP.add(j.getNom());
			if(PantallaInicial.bd.obtenerMVP(id_partido, j.getId())){
				posicionMVP = lista_jugadores_visit.indexOf(j);
				id_jug_MVPVisit = j.getId();
			}
		}
		spnMVPVisit.setAdapter(adapterMVP);
		spnMVPVisit.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
				if(parent.getItemAtPosition(position).toString().equals("")) id_jug_MVPVisit = -1;
				else id_jug_MVPVisit = lista_jugadores_visit.get(position-1).getId();
				if(id_jug_MVPVisit!=-1) id_jug_MVP = id_jug_MVPVisit;
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		if(posicionMVP>-1) spnMVPVisit.setSelection(posicionMVP+1);
	}
	
	private void updateDisplay(){
		SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			txtFecha.setText(spf.format(spf.parse(new StringBuilder().append(_dia).append("/").append(_mes + 1).append("/").append(_anio).toString())));
		} catch (ParseException e) {e.printStackTrace();}
	}
	
	private DatePickerDialog.OnDateSetListener _dateListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			_anio = year;
			_mes = monthOfYear;
			_dia = dayOfMonth;
			updateDisplay();
		}
	};
	
	protected Dialog onCreateDialog(int id){
		switch(id){
			case 0:
				return new DatePickerDialog(this, _dateListener, _anio, _mes, _dia);
		}
		return null;
	}
	
	public void btnGuardar_Click(View v){
		try {
			if(resLocal!=resVisit){
				//Primero, introducimos el resultado
				PantallaInicial.bd.insertarResultado(id_partido, local, resLocal);
				PantallaInicial.bd.insertarResultado(id_partido, visit, resVisit);
				
				//Segundo, introducimos los minutos disputados por cada jugador. Antes borro los que ya hubiese
				PantallaInicial.bd.insertarMinutosJugador(id_partido, jugadoresLocal, minutosLocal, lista_jugadores_local);
				PantallaInicial.bd.insertarMinutosJugador(id_partido, jugadoresVisit, minutosVisit, lista_jugadores_visit);
	
				//Tercero, introducimos los goles, si los hubiere, de cada jugador
				PantallaInicial.bd.insertarDatosPartidoJugadorBaloncesto(id_partido, jugadoresLocal, id_jug_MVP, puntosLocal, rebotesLocal, taponesLocal, faltasLocal, lista_jugadores_local);
				PantallaInicial.bd.insertarDatosPartidoJugadorBaloncesto(id_partido, jugadoresVisit, id_jug_MVP, puntosVisit, rebotesVisit, taponesVisit, faltasVisit, lista_jugadores_visit);
				
				//Insertamos la fecha y hora del partido
				PantallaInicial.bd.insertarDatosPartido(id_partido, txtFecha.getText().toString(), hora);
				
				Toast.makeText(this, getString(R.string.BDInserta), Toast.LENGTH_SHORT).show();
				finish();
			}else{
				Toast.makeText(this, getString(R.string.resultadoNoCorrecto), Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			Toast.makeText(this, getString(R.string.BDNoInserta), Toast.LENGTH_SHORT).show();
		}
	}
	
	public void onResume(){
		super.onResume();
		id_jug_MVPLocal = -1; id_jug_MVPVisit = -1; id_jug_MVP = -1;
	}
}
