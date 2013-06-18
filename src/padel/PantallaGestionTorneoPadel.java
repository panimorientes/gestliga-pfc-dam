//Esta actividad es para la gestión del torneo, donde podemos gestionar el calendario, y añadir o eliminar jugadores

package padel;

import java.util.ArrayList;
import java.util.Iterator;

import tipos.*;

import com.example.gestliga.PantallaInicial;
import com.example.gestliga.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PantallaGestionTorneoPadel extends Activity {

	private TextView txtTitulo, txtRonda;
	private Button btnAnadirJugador, btnEliminarJugador, btnGenerarCalendario, btnSiguienteRonda;
	private ArrayList<Jugador> lista_jugadores = new ArrayList<Jugador>();
	private ArrayList<Integer> lista_ronda = new ArrayList<Integer>();
	private ImageButton btnFlechaDerecha, btnFlechaIzquierda;
	private LinearLayout llRonda;
	private boolean existeCalendario = false;
	private int id_torneo_intent, numRondas, ronda_actual=0;
	private ArrayList<Integer> lista_partidos = new ArrayList<Integer>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.padel_activity_pantalla_gestion_torneo);
		
		id_torneo_intent = getIntent().getIntExtra("id_torneo",-1);
		//Se asignan las referencias de las vistas en el layout
		llRonda = (LinearLayout) findViewById(R.id.llRonda);
		btnFlechaDerecha = (ImageButton) findViewById(R.id.btnFlechaDerRondaPadel);
		btnFlechaDerecha.setVisibility(View.GONE);
		btnFlechaDerecha.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(ronda_actual > 1){
					ronda_actual--;
					txtRonda.setText(getString(R.string.rondaConPuntos) + " " + obtenerRonda(ronda_actual));
					cargarCalendario(ronda_actual);
				}
			}
		});
		btnFlechaIzquierda = (ImageButton) findViewById(R.id.btnFlechaIzqRondaPadel);
		btnFlechaIzquierda.setVisibility(View.GONE);
		btnFlechaIzquierda.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(ronda_actual < numRondas){
					ronda_actual++;
					txtRonda.setText(getString(R.string.rondaConPuntos) + " " + obtenerRonda(ronda_actual));
					cargarCalendario(ronda_actual);
				}
			}
		});
		
		//Se comprueba que están todos los resultados introducidos y se genera la nueva ronda
		btnSiguienteRonda = (Button) findViewById(R.id.btnSiguienteRonda);
		btnSiguienteRonda.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(ronda_actual != 1){ //No es la Final
					boolean generar = true;
					ArrayList<Integer> lista_par_ronda = new ArrayList<Integer>();
					lista_par_ronda = PantallaInicial.bd.obtenerPartidosRonda(id_torneo_intent, ronda_actual);
					if(lista_par_ronda.size()==0) generar = false;
					Iterator<Integer> it = lista_par_ronda.iterator();	
					while(it.hasNext()){
						int id_par = it.next();
						int[] res1 = PantallaInicial.bd.obtenerResultadoTenis(id_par, PantallaInicial.bd.obtenerJugadoresPartido(id_par)[0]);
						int[] res2 = PantallaInicial.bd.obtenerResultadoTenis(id_par, PantallaInicial.bd.obtenerJugadoresPartido(id_par)[1]);
						if(res1[0]==0 && res1[1]==0 && res1[2]==0 && res2[0]==0 && res2[1]==0 && res2[2]==0) generar = false;
					}
					
					if(generar){
						PantallaInicial.bd.eliminarPartidosRonda(id_torneo_intent, ronda_actual-1);
						generarCalendario(ronda_actual-1);
						preCargarCalendario(ronda_actual);
					}
				}
			}
		});
		
		txtRonda = (TextView) findViewById(R.id.txtRondaX);
		txtRonda.setVisibility(View.GONE);
		
		txtTitulo = (TextView) findViewById(R.id.txtGestionTorneoPadelNombre);
		txtTitulo.setText(getIntent().getStringExtra("nombre"));
		
		btnAnadirJugador = (Button) findViewById(R.id.btnGestionTorneoPadelAnadirJugadores);
		btnAnadirJugador.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), PantallaAnadirJugador.class);
				intent.putExtra("id_torneo", id_torneo_intent);
				startActivity(intent);
			}
		});
		
		btnEliminarJugador = (Button) findViewById(R.id.btnGestionTorneoPadelEliminarJugadores);
		btnEliminarJugador.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), PantallaEliminarJugadores.class);
				intent.putExtra("id_torneo", id_torneo_intent);
				startActivity(intent);
			}
		});
		
		btnGenerarCalendario = (Button) findViewById(R.id.btnGenerarCalendarioPadel);
		btnGenerarCalendario.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Primero se comprueba si el número de jugadores es correcto
				if(!comprobarNumJugadores()){
					AlertDialog.Builder builder = new AlertDialog.Builder(PantallaGestionTorneoPadel.this);
					builder.setTitle(getString(R.string.atencion));
					builder.setMessage(getString(R.string.jugadoresNoCorrectos));
					
					builder.setNeutralButton(getString(R.string.volver), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
					
					builder.create().show();
				}
				else{
					//Compruebo si ya existe un calendario para este torneo
					existeCalendario = false;
					if(PantallaInicial.bd.comprobarCalendarioTorneo(id_torneo_intent)==true){ //Ya existe el calendario, entonces pregunta si lo quiere reemplazar
						existeCalendario = true;
						AlertDialog.Builder builder = new AlertDialog.Builder(PantallaGestionTorneoPadel.this);
						builder.setTitle(getString(R.string.atencion));
						builder.setMessage(getString(R.string.reemplazarCalendario));
						
						builder.setPositiveButton(getString(R.string.reemplazar), new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										PantallaInicial.bd.eliminarPartidosTorneo(id_torneo_intent);
										existeCalendario = false;
										ArrayList<Jugador> lista = PantallaInicial.bd.obtenerJugadoresDeTorneo(id_torneo_intent, "", 4);
										generarCalendario(calcularRonda(lista.size()));
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
						ArrayList<Jugador> lista = PantallaInicial.bd.obtenerJugadoresDeTorneo(id_torneo_intent, "", 4);
						generarCalendario(calcularRonda(lista.size()));
					}
				}
			}
		});
		if(PantallaInicial.bd.comprobarCalendarioTorneo(id_torneo_intent)==true){ //Cargo los datos del calendario
			ArrayList<Jugador> lista = PantallaInicial.bd.obtenerJugadoresDeTorneo(id_torneo_intent, "", 4);
			cargarCalendario(calcularRonda(lista.size()));
		}
	}
	
	public void onResume(){
		super.onResume();
		ArrayList<Jugador> lista = PantallaInicial.bd.obtenerJugadoresDeTorneo(id_torneo_intent, "", 4);
		ronda_actual = calcularRonda(lista.size());
		preCargarCalendario(ronda_actual);
	}
	
	//Este método devuelve true si el número de jugadores es correcto
	public boolean comprobarNumJugadores(){
		TorneoTenis t = PantallaInicial.bd.obtenerTorneoPadel(id_torneo_intent);
		for(int i=1; i<=7;i++){
			if(t.getNumJugadores() == Math.pow(2, i)) return true;
		}
		return false;
	}
	
	//Este método crea un nuevo calendario
	public void generarCalendario(int ronda){
		//Obtener jugadores de ronda (cojer a los que han ganado, excepto en la primera ronda)
		lista_jugadores = PantallaInicial.bd.obtenerJugadoresDeTorneo(id_torneo_intent, "", 4);
		if(ronda < calcularRonda(lista_jugadores.size())){
			lista_jugadores = PantallaInicial.bd.obtenerJugadoresGanadoresDeRonda(id_torneo_intent, ronda+1, 4); //Lo tengo que calcular de la ronda anterior a la que recibe el método
			PantallaInicial.bd.insertarPartidosSiguienteRondaTenis(id_torneo_intent, lista_jugadores, 4, ronda);
			Toast.makeText(getApplicationContext(), "Ronda generada con éxito", Toast.LENGTH_SHORT).show();
		}else{
			PantallaInicial.bd.insertarPartidosTorneoTenis(id_torneo_intent, lista_jugadores, 4, ronda);
			Toast.makeText(getApplicationContext(), "Calendario generado con éxito", Toast.LENGTH_SHORT).show();
		}
		preCargarCalendario(ronda);
	}
	
	//Este método comprueba si ya existe un calendario, para cargarlo en pantalla
	public void preCargarCalendario(int num_ronda){
		if(PantallaInicial.bd.comprobarCalendarioTorneo(id_torneo_intent)==true){
			numRondas = calcularRonda(PantallaInicial.bd.obtenerJugadoresDeTorneo(id_torneo_intent, "", 4).size());
			cargarCalendario(num_ronda);
			btnAnadirJugador.setEnabled(false);
			btnEliminarJugador.setEnabled(false);
		}else{
			llRonda.removeAllViews();
			btnFlechaDerecha.setVisibility(View.GONE);
			btnFlechaIzquierda.setVisibility(View.GONE);
			txtRonda.setVisibility(View.GONE);
		}
	}
	
	//Con este método se muestra el calendario en pantalla
	public void cargarCalendario(int num_ronda){
		if(llRonda.getChildCount()>0) llRonda.removeAllViews();
		btnFlechaDerecha.setVisibility(View.VISIBLE);
		btnFlechaIzquierda.setVisibility(View.VISIBLE);
		txtRonda.setVisibility(View.VISIBLE);
		
		lista_ronda = PantallaInicial.bd.obtenerJugadoresRonda(id_torneo_intent, num_ronda);
		txtRonda.setText(getString(R.string.rondaConPuntos) + " " + obtenerRonda(lista_ronda.size()));

		for(int i=0; i<lista_ronda.size();i=i+2){
			Jugador j1 = PantallaInicial.bd.obtenerJugador(lista_ronda.get(i), 4);
			Jugador j2 = PantallaInicial.bd.obtenerJugador(lista_ronda.get(i+1), 4);
			int id_partido = PantallaInicial.bd.obtenerIDPartidoTenis(id_torneo_intent, j1.getId(), j2.getId());
			lista_partidos.add(id_partido);
			DatosJugadorPartido djp = PantallaInicial.bd.obtenerDatosJugadorPartido(j1.getId(), id_partido);
			
			LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
			LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.padel_fila_ronda, null);
			TextView txtLocal = (TextView) ll.findViewById(R.id.txtRondaJugador1);
			txtLocal.setText(j1.getNom());
			TextView txts11 = (TextView) ll.findViewById(R.id.txtset11);
			txts11.setText(djp.getSt1()+"");
			TextView txts12 = (TextView) ll.findViewById(R.id.txtset12);
			txts12.setText(djp.getSt2()+"");
			TextView txts13 = (TextView) ll.findViewById(R.id.txtset13);
			txts13.setText(djp.getSt3()+"");
			//Con este ImageButton podemos acceder a la actividad donde se introducen los datos del partido
			ImageButton btnEditar = (ImageButton) ll.findViewById(R.id.btnRondaEditar);
			btnEditar.setTag(lista_partidos.size()-1);
			btnEditar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getBaseContext(), PantallaGestionPartidoPadel.class);
					intent.putExtra("id_partido", lista_partidos.get(Integer.parseInt(v.getTag()+"")));
					startActivity(intent);
				}
			});
			
			djp = PantallaInicial.bd.obtenerDatosJugadorPartido(j2.getId(), id_partido);
			TextView txtVisit = (TextView) ll.findViewById(R.id.txtRondaJugador2);
			txtVisit.setText(j2.getNom());
			TextView txts21 = (TextView) ll.findViewById(R.id.txtset21);
			txts21.setText(djp.getSt1()+"");
			TextView txts22 = (TextView) ll.findViewById(R.id.txtset22);
			txts22.setText(djp.getSt2()+"");
			TextView txts23 = (TextView) ll.findViewById(R.id.txtset23);
			txts23.setText(djp.getSt3()+"");
			
			llRonda.addView(ll);
			
			FrameLayout frm = new FrameLayout(this);
			frm.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
			frm.setBackgroundColor(Color.parseColor(getString(R.color.negro)));
			llRonda.addView(frm);
		}
	}
	
	//Este método devuelve una cadena con la ronda correspondiente al ID que recibe como parámetro
	private String obtenerRonda(int numJugadores){
		int ronda = calcularRonda(numJugadores);
		switch(ronda){
		case 1:
			return "Final";
		case 2:
			return "Semifinales";
		case 3:
			return "1/4 Final";
		case 4:
			return "1/8 Final";
		case 5:
			return "1/16 Final";
		case 6:
			return "1/32 Final";
		case 7:
			return "Ronda 1";
		default:
			return "Ronda -";	
		}
	}
	
	//Este método devuelve el número de rondas que tendrá un torneo de tenis o pádel en función del nº de jugadores que recibe como parámetro
	private int calcularRonda(int num_jugadores){
		switch(num_jugadores){
		case 2:
			return 1;
		case 4:
			return 2;
		case 8:
			return 3;
		case 16:
			return 4;
		case 32:
			return 5;
		case 64:
			return 6;
		case 128:
			return 7;
		default:
			return ronda_actual;	
		}
	}
}
