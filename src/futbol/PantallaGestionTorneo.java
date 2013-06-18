//Esta actividad es para la gestión del torneo, donde podemos gestionar el calendario, y añadir o eliminar equipos

package futbol;

import java.util.ArrayList;
import java.util.Random;

import tipos.Equipo;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gestliga.PantallaInicial;
import com.example.gestliga.R;

public class PantallaGestionTorneo extends Activity {
	
	private TextView txtTitulo, txtJornada;
	private Button btnAnadirEquipo, btnEliminarEquipo, btnGenerarCalendario, btnActualizarClasificacion;
	private ArrayList<Equipo> lista_equipos = new ArrayList<Equipo>();
	private ArrayList<Integer> lista_jornada = new ArrayList<Integer>();
	private ImageButton btnFlechaDerecha, btnFlechaIzquierda;
	private Spinner spnJornada;
	private LinearLayout llJornada;
	private boolean soloIda = true;
	private RadioGroup radioGroup;
	private boolean existeCalendario = false;
	private int id_torneo_intent, numJornadas, jornada_actual=1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.futbol_activity_pantalla_gestion_torneo);
		
		id_torneo_intent = getIntent().getIntExtra("id_torneo",-1);
		
		//Se asignan las referencias de las vistas en el layout
		llJornada = (LinearLayout) findViewById(R.id.llJornada);
		btnFlechaDerecha = (ImageButton) findViewById(R.id.btnFlechaDerJornada);
		btnFlechaDerecha.setVisibility(View.GONE);
		btnFlechaDerecha.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(jornada_actual < numJornadas){
					jornada_actual++;
					txtJornada.setText(getString(R.string.jornada) + " " + jornada_actual);
					spnJornada.setSelection(jornada_actual-1);
				}
			}
		});
		btnFlechaIzquierda = (ImageButton) findViewById(R.id.btnFlechaIzqJornada);
		btnFlechaIzquierda.setVisibility(View.GONE);
		btnFlechaIzquierda.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(jornada_actual > 1){
					jornada_actual--;
					txtJornada.setText(getString(R.string.jornada) + " " + jornada_actual);
					spnJornada.setSelection(jornada_actual-1);
				}
			}
		});
		txtJornada = (TextView) findViewById(R.id.txtJornadaX);
		txtJornada.setVisibility(View.GONE);
		spnJornada = (Spinner) findViewById(R.id.spnGestionTorneoJornada);
		spnJornada.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
				cargarCalendario(Integer.parseInt(parent.getItemAtPosition(position).toString()));
				jornada_actual = Integer.parseInt(parent.getItemAtPosition(position).toString());
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		
		btnActualizarClasificacion = (Button) findViewById(R.id.btnActualizarClasificacion);
		btnActualizarClasificacion.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(PantallaInicial.bd.actualizarPartidosClasificacion(id_torneo_intent, jornada_actual, 1)){
					Toast.makeText(getApplicationContext(), getString(R.string.datosActualizados), Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(getApplicationContext(), getString(R.string.resultadoNoCorrecto), Toast.LENGTH_SHORT).show();
				}
			}
		});
		btnActualizarClasificacion.setVisibility(View.GONE);
		
		txtTitulo = (TextView) findViewById(R.id.txtGestionTorneoFutbolNombre);
		txtTitulo.setText(getIntent().getStringExtra("nombre"));
		
		btnAnadirEquipo = (Button) findViewById(R.id.btnGestionTorneoFutbolAnadirEquipo);
		btnAnadirEquipo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), PantallaAnadirEquipo.class);
				intent.putExtra("id_torneo", id_torneo_intent);
				startActivity(intent);
			}
		});
		
		btnEliminarEquipo = (Button) findViewById(R.id.btnGestionTorneoFutbolEliminarEquipo);
		btnEliminarEquipo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), PantallaEliminarEquipos.class);
				intent.putExtra("id_torneo", id_torneo_intent);
				startActivity(intent);
			}
		});
		
		btnGenerarCalendario = (Button) findViewById(R.id.btnGenerarCalendario);
		btnGenerarCalendario.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Compruebo si ya existe un calendario para este torneo
				existeCalendario = false;
				if(PantallaInicial.bd.comprobarCalendarioTorneo(id_torneo_intent)==true){ //Ya existe el calendario, entonces pregunta si lo quiere reemplazar
					existeCalendario = true;
					AlertDialog.Builder builder = new AlertDialog.Builder(PantallaGestionTorneo.this);
					builder.setTitle(getString(R.string.atencion));
					builder.setMessage(getString(R.string.reemplazarCalendario));
					
					builder.setPositiveButton(getString(R.string.reemplazar), new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									PantallaInicial.bd.eliminarPartidosTorneo(id_torneo_intent);
									existeCalendario = false;
									DialogoModoCalendario();
								}
							});
					
					builder.setNegativeButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
					builder.create().show();
				}
				if(existeCalendario==false){ //No existe calendario
					DialogoModoCalendario();
				}
			}
		});
		if(PantallaInicial.bd.comprobarCalendarioTorneo(id_torneo_intent)==true){ //Cargo los datos del calendario
			cargarCalendario(1);
		}
	}
	
	public void onResume(){
		super.onResume();
		preCargarCalendario(1);
	}
	
	//Este método crea un nuevo calendario
	public void generarCalendario(){
		lista_equipos = PantallaInicial.bd.obtenerEquiposDeTorneo(id_torneo_intent, 1);
		desordenarListaEquipos();
		int num_equipos = lista_equipos.size();
		if(num_equipos > 1){
			if(num_equipos%2!=0){
				lista_equipos.add(new Equipo(-1, "Descansa", 0, 0, "", 1, "", 0));
				num_equipos++;
			}
			ArrayList<Equipo> lista_ida = (ArrayList<Equipo>) lista_equipos.clone();
			ArrayList<Equipo> lista_vuelta = (ArrayList<Equipo>) lista_equipos.clone();
			//Este es el algoritmo Round Robin
			Equipo buffer = new Equipo();
			for(int i=0; i<(num_equipos-1); i++){
				boolean local = true;//Con esta variable voy intercambiando los equipos para que vayan apareciendo como local y visitante
				if(i%2==0) local = true;
				else local = false;
				
				PantallaInicial.bd.insertarPartidosTorneo(id_torneo_intent, lista_ida, 1/*Fútbol*/, "Jornada " + (i+1), 1/*Ida*/, local/*Para saber si el equipo juega de local o no*/);
				buffer = lista_ida.get(num_equipos-1);
				for(int j=(num_equipos-1); j>1; j--){
					lista_ida.set(j, lista_ida.get(j-1));
				}
				lista_ida.set(1, buffer);
			}
			//Fin algoritmo Round Robin
			if(soloIda == false){ //Repito el proceso si ha elegido Ida y Vuelta.
				buffer = new Equipo();
				for(int i=0; i<(num_equipos-1); i++){
					boolean local = true;
					if(i%2==0) local = true;
					else local = false;
					PantallaInicial.bd.insertarPartidosTorneo(id_torneo_intent, lista_vuelta, 1/*Fútbol*/, "Jornada " + (i+num_equipos), 2/*Vuelta*/, local);
					buffer = lista_vuelta.get(num_equipos-1);
					for(int j=(num_equipos-1); j>1; j--){
						lista_vuelta.set(j, lista_vuelta.get(j-1));
					}
					lista_vuelta.set(1, buffer);
				}
			}
			Toast.makeText(getApplicationContext(), "Calendario generado con éxito", Toast.LENGTH_SHORT).show();
			btnAnadirEquipo.setEnabled(false);
			btnEliminarEquipo.setEnabled(false);
			preCargarCalendario(1);
		}else{
			Toast.makeText(getApplicationContext(), "No hay equipos suficientes", Toast.LENGTH_SHORT).show();
		}
	}
	//Este método es para que la primera jornada del calendario sea aleatoria, antes de comenzar el desarrollo de Round Robin
	private void desordenarListaEquipos() {
		int num_equipos = lista_equipos.size();
		ArrayList<Equipo> aux = new ArrayList<Equipo>();
		Random rnd;
		while(!lista_equipos.isEmpty()){
			rnd = new Random();
			int num = (int)(rnd.nextDouble()*num_equipos);
			if(num!=num_equipos){
				aux.add(lista_equipos.get(num));
				lista_equipos.remove(num);
				num_equipos--;
			}
		}
		lista_equipos = (ArrayList<Equipo>) aux.clone();
	}

	//Este método comprueba si ya existe un calendario, para cargarlo en pantalla
	public void preCargarCalendario(int num_jornada){
		if(PantallaInicial.bd.comprobarCalendarioTorneo(id_torneo_intent)==true){
			calcularNumJornadas();
			ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item);
			for(int i=1; i<= numJornadas;i++){
				adapter.add(i);
			}
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spnJornada.setAdapter(adapter);
			spnJornada.setEnabled(true);
			cargarCalendario(num_jornada);
			btnAnadirEquipo.setEnabled(false);
			btnEliminarEquipo.setEnabled(false);
		}else{
			llJornada.removeAllViews();
			btnFlechaDerecha.setVisibility(View.GONE);
			btnFlechaIzquierda.setVisibility(View.GONE);
			txtJornada.setVisibility(View.GONE);
			btnActualizarClasificacion.setVisibility(View.GONE);
			spnJornada.setEnabled(false);
		}
	}
	//Con esté método se muestra el calendario en pantalla
	public void cargarCalendario(int num_jornada){
		if(llJornada.getChildCount()>0) llJornada.removeAllViews();
		btnFlechaDerecha.setVisibility(View.VISIBLE);
		btnFlechaIzquierda.setVisibility(View.VISIBLE);
		txtJornada.setVisibility(View.VISIBLE);
		btnActualizarClasificacion.setVisibility(View.VISIBLE);
		
		int num_equipos = PantallaInicial.bd.obtenerNumEquiposTorneo(id_torneo_intent);
		if(num_equipos%2!=0){
			lista_equipos.add(new Equipo(-1, "Descansa", 0, 0, "", 1, "", 0));
			num_equipos++;
		}
		txtJornada.setText(getString(R.string.jornada) + " " + num_jornada);
		lista_jornada = PantallaInicial.bd.obtenerEquiposJornada(id_torneo_intent, num_jornada);
		int position=0;
		for(int i=0; i<num_equipos;i=i+2){
			Equipo local = PantallaInicial.bd.obtenerEquipo(lista_jornada.get(i));
			Equipo visit = new Equipo(-1, "Descansa", 0, 0, "", 1, "", 0);
			if(i+1 < num_equipos){
				visit = PantallaInicial.bd.obtenerEquipo(lista_jornada.get(i+1));
			}
			LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
			LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.futbol_fila_jornada, null);
			TextView txtLocal = (TextView) ll.findViewById(R.id.txtJornadaEquipoLocal);
			txtLocal.setText(local.getNom());
			TextView txtLocalRes = (TextView) ll.findViewById(R.id.txtJornadaEquipoLocalRes);
			txtLocalRes.setText(PantallaInicial.bd.obtenerResultado(id_torneo_intent, local.getId(), num_jornada)+"");
			TextView txtVisit = (TextView) ll.findViewById(R.id.txtJornadaEquipoVisit);
			txtVisit.setText(visit.getNom());
			TextView txtVisitRes = (TextView) ll.findViewById(R.id.txtJornadaEquipoVisitRes);
			txtVisitRes.setText(PantallaInicial.bd.obtenerResultado(id_torneo_intent, visit.getId(), num_jornada)+"");
			//Con este ImageButton podemos acceder a la actividad donde se introducen los datos del partido
			ImageButton btnEditar = (ImageButton) ll.findViewById(R.id.btnJornadaEditar);
			btnEditar.setTag(position);
			btnEditar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int tag = Integer.parseInt(v.getTag()+"");
					int indice_equipoLocal = lista_jornada.get(tag*2);
					int indice_equipoVisit = lista_jornada.get((tag*2)+1);
					Intent intent = new Intent(getApplicationContext(), PantallaGestionarPartido.class);
					intent.putExtra("id_torneo", id_torneo_intent);
					intent.putExtra("local", indice_equipoLocal);
					intent.putExtra("visit", indice_equipoVisit);
					intent.putExtra("jornada", jornada_actual);
					startActivity(intent);
				}
			});
			if(txtVisit.getText().toString().equals("")){
				txtVisit.setText(getString(R.string.descansa));
				txtLocalRes.setText("");
				txtVisitRes.setText("");
				btnEditar.setVisibility(View.INVISIBLE);
			}
			if(txtLocal.getText().toString().equals("")){
				txtLocal.setText(getString(R.string.descansa));
				txtVisitRes.setText("");
				txtLocalRes.setText("");
				btnEditar.setVisibility(View.INVISIBLE);
			}
			llJornada.addView(ll);
			position++;
		}
	}
	
	//Este método calcula el número de jornadas que tendrá un torneo en base al número de equipos
	public void calcularNumJornadas(){
		int num_equipos = PantallaInicial.bd.obtenerNumEquiposTorneo(id_torneo_intent);
		if(num_equipos%2!=0) num_equipos++;
		int num_filas = PantallaInicial.bd.obtenerFilasTorneo(id_torneo_intent);
		if(num_equipos != 0)numJornadas = num_filas/num_equipos;
	}
	
	//Este método se ejecuta cuando se muestra el diálogo para elegir si queremos un calendario de sólo ida, o de ida y vuelta
	public void DialogoModoCalendario(){
		AlertDialog.Builder builder = new AlertDialog.Builder(PantallaGestionTorneo.this);
		builder.setTitle(getString(R.string.tituloSeleccione));
		radioGroup = new RadioGroup(PantallaGestionTorneo.this);
		radioGroup.setBackgroundColor(Color.parseColor(getString(R.color.negro)));
		RadioButton rb1 = new RadioButton(PantallaGestionTorneo.this);
		rb1.setTextColor(Color.parseColor(getString(R.color.blanco)));
		rb1.setText(getString(R.string.ida));
		rb1.setId(0);
		radioGroup.addView(rb1);
		rb1 = new RadioButton(PantallaGestionTorneo.this);
		rb1.setTextColor(Color.parseColor(getString(R.color.blanco)));
		rb1.setText(getString(R.string.idaVuelta));
		rb1.setId(1);
		radioGroup.addView(rb1);
		radioGroup.check(0);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId == 0){
					soloIda = true;
				}
				else{
					soloIda = false;
				}
			}
		});
		builder.setView(radioGroup);
		
		builder.setPositiveButton(getString(R.string.crear), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						generarCalendario();
						dialog.dismiss();
					}
				});
		
		builder.setNegativeButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.create().show();
	}
}
