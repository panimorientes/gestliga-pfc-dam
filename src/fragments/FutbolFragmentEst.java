//Esta pestaña representa las estadísticas de fútbol

package fragments;

import java.util.ArrayList;
import java.util.Iterator;

import tipos.Clasificacion;
import tipos.Equipo;
import tipos.Recuento;
import tipos.GoleadorEquipos;
import tipos.Tarjetas;
import tipos.Torneo;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.gestliga.PantallaInicial;
import com.example.gestliga.R;

public class FutbolFragmentEst extends SherlockFragment {

	private Spinner spnTorneo;
	private TableLayout tablaClasificacion, tablaGoleadores, tablaTarjetas, tablaJugadorGolesEquipo;
	private Button btnClasificacion, btnGoleadores, btnTarjetas, btnJugadorGolesEquipo;
	ArrayList<Torneo> lista_torneos = new ArrayList<Torneo>();
	ArrayList<Equipo> lista_equipos = new ArrayList<Equipo>();
	ArrayList<Clasificacion> lista_clasificacion = new ArrayList<Clasificacion>();
	ArrayList<Recuento> lista_goleadores = new ArrayList<Recuento>();
	ArrayList<Tarjetas> lista_tarjetas = new ArrayList<Tarjetas>();
	ArrayList<GoleadorEquipos> lista_goleadores_a_equipo = new ArrayList<GoleadorEquipos>();
	
	private int id_torneo_seleccionado = -1;
	
 	private View v;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.futbol_fragment_est, container, false);

		//Se asignan las referencias de las vistas en el layout
		lista_torneos = PantallaInicial.bd.obtenerTorneos("tor_id", 1);
		
		spnTorneo = (Spinner) v.findViewById(R.id.spnTorneo);
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(v.getContext(), android.R.layout.simple_spinner_item);
		Iterator<Torneo> it = lista_torneos.iterator();
		adapter.add("Elija torneo");
		while(it.hasNext()){
			adapter.add(it.next().getNom());
		}
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnTorneo.setAdapter(adapter);
		spnTorneo.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
				if(parent.getItemAtPosition(position).toString().equals("Elija torneo")) id_torneo_seleccionado = -1;
				else id_torneo_seleccionado = lista_torneos.get(position-1).getId();
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		
		tablaClasificacion = (TableLayout) v.findViewById(R.id.tablaClasificacion);
		tablaGoleadores = (TableLayout) v.findViewById(R.id.tablaGoleadores);
		tablaTarjetas = (TableLayout) v.findViewById(R.id.tablaTarjetas);
		tablaJugadorGolesEquipo = (TableLayout) v.findViewById(R.id.tablaGolesJugadorEquipo);
		
		btnClasificacion = (Button) v.findViewById(R.id.btnClasificacion);
		btnClasificacion.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Se construye la tabla de clasificación
				if(id_torneo_seleccionado == -1) Toast.makeText(getActivity(), getString(R.string.seleccioneTorneo), Toast.LENGTH_SHORT).show();
				else{
					tablaClasificacion.setVisibility(View.VISIBLE);
					if(tablaClasificacion.getChildCount()>1) tablaClasificacion.removeViews(1, tablaClasificacion.getChildCount()-1);
					tablaGoleadores.setVisibility(View.INVISIBLE);
					tablaTarjetas.setVisibility(View.INVISIBLE);
					tablaJugadorGolesEquipo.setVisibility(View.INVISIBLE);
					
					lista_clasificacion = PantallaInicial.bd.obtenerClasificacion(id_torneo_seleccionado, 1);
					ordenarClasificacion();
					Iterator<Clasificacion> it = lista_clasificacion.iterator();
					int pos = 1;
					while(it.hasNext()){
						Clasificacion c = it.next();
						LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			    		TableRow fila = (TableRow) inflater.inflate(R.layout.futbol_estadisticas_clasificacion_table_row, null);
			    		TextView posicion = (TextView) fila.findViewById(R.id.txtPosicion);
			    		posicion.setText(pos+".");
			    		TextView equipo = (TextView) fila.findViewById(R.id.txtEquipo);
			    		equipo.setText(PantallaInicial.bd.obtenerNombreEquipo(c.getId_equipo()));
			    		TextView jugados = (TextView) fila.findViewById(R.id.txtJugados);
			    		jugados.setText(c.getJug()+"");
			    		TextView ganados = (TextView) fila.findViewById(R.id.txtGanados);
			    		ganados.setText(c.getGan()+"");
			    		TextView empatados = (TextView) fila.findViewById(R.id.txtEmpatados);
			    		empatados.setText(c.getEmp()+"");
			    		TextView perdidos = (TextView) fila.findViewById(R.id.txtPerdidos);
			    		perdidos.setText(c.getPer()+"");
			    		TextView golesF = (TextView) fila.findViewById(R.id.txtGolesFavor);
			    		golesF.setText(c.getGf()+"");
			    		TextView golesC = (TextView) fila.findViewById(R.id.txtGolesContra);
			    		golesC.setText(c.getGc()+"");
			    		TextView dif = (TextView) fila.findViewById(R.id.txtDiferencia);
			    		dif.setText((c.getGf()-c.getGc())+"");
			    		TextView puntos = (TextView) fila.findViewById(R.id.txtPuntos);
			    		puntos.setText((c.getGan()*3 + c.getEmp())+"");
			    		
			    		FrameLayout frm = new FrameLayout(getActivity());
						frm.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1));
						frm.setBackgroundColor(Color.parseColor("#222222"));
						
						tablaClasificacion.addView(fila);
						if(it.hasNext()) tablaClasificacion.addView(frm);
			    		pos++;
					}
				}
			}
		});
		
		btnGoleadores = (Button) v.findViewById(R.id.btnMaxGoleadores);
		btnGoleadores.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Se construye la tabla de máximos goleadores
				if(id_torneo_seleccionado == -1) Toast.makeText(getActivity(), getString(R.string.seleccioneTorneo), Toast.LENGTH_SHORT).show();
				else{
					tablaClasificacion.setVisibility(View.INVISIBLE);
					tablaGoleadores.setVisibility(View.VISIBLE);
					if(tablaGoleadores.getChildCount()>1) tablaGoleadores.removeViews(1, tablaGoleadores.getChildCount()-1);
					tablaTarjetas.setVisibility(View.INVISIBLE);
					tablaJugadorGolesEquipo.setVisibility(View.INVISIBLE);
					
					lista_goleadores = PantallaInicial.bd.obtenerGoleadoresTorneo(id_torneo_seleccionado);
					Iterator<Recuento> it = lista_goleadores.iterator();
					int pos = 1;
					while(it.hasNext()){
						Recuento g = it.next();
						LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			    		TableRow fila = (TableRow) inflater.inflate(R.layout.futbol_estadisticas_goleadores_table_row, null);
			    		TextView posicion = (TextView) fila.findViewById(R.id.txtPosicion);
			    		posicion.setText(pos+".");
			    		TextView jugador = (TextView) fila.findViewById(R.id.txtJugador);
			    		jugador.setText(PantallaInicial.bd.obtenerJugador(g.getJug(),1).getApo());
			    		TextView equipo = (TextView) fila.findViewById(R.id.txtEquipo);
			    		equipo.setText(PantallaInicial.bd.obtenerNombreEquipo(g.getEqu()));
			    		TextView goles = (TextView) fila.findViewById(R.id.txtGoles);
			    		goles.setText(g.getGoles()+"");
			    		
			    		FrameLayout frm = new FrameLayout(getActivity());
						frm.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1));
						frm.setBackgroundColor(Color.parseColor("#222222"));
						
						tablaGoleadores.addView(fila);
						if(it.hasNext()) tablaGoleadores.addView(frm);
			    		pos++;
					}
				}
			}
		});
		
		btnTarjetas = (Button) v.findViewById(R.id.btnTarjetas);
		btnTarjetas.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Se construye la tabla de jugadores que han recibido tarjetas
				if(id_torneo_seleccionado == -1) Toast.makeText(getActivity(), getString(R.string.seleccioneTorneo), Toast.LENGTH_SHORT).show();
				else{
					tablaClasificacion.setVisibility(View.INVISIBLE);
					tablaGoleadores.setVisibility(View.INVISIBLE);
					tablaTarjetas.setVisibility(View.VISIBLE);
					if(tablaTarjetas.getChildCount()>1) tablaTarjetas.removeViews(1, tablaTarjetas.getChildCount()-1);
					tablaJugadorGolesEquipo.setVisibility(View.INVISIBLE);
					
					lista_tarjetas = PantallaInicial.bd.obtenerTarjetasTorneo(id_torneo_seleccionado);
					Iterator<Tarjetas> it = lista_tarjetas.iterator();
					int pos = 1;
					while(it.hasNext()){
						Tarjetas t = it.next();
						LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			    		TableRow fila = (TableRow) inflater.inflate(R.layout.futbol_estadisticas_tarjetas_table_row, null);
			    		TextView posicion = (TextView) fila.findViewById(R.id.txtPosicion);
			    		posicion.setText(pos+".");
			    		TextView jugador = (TextView) fila.findViewById(R.id.txtJugador);
			    		jugador.setText(PantallaInicial.bd.obtenerJugador(t.getJug(),1).getApo());
			    		TextView equipo = (TextView) fila.findViewById(R.id.txtEquipo);
			    		equipo.setText(PantallaInicial.bd.obtenerNombreEquipo(t.getEqu()));
			    		TextView rojas = (TextView) fila.findViewById(R.id.txtRojas);
			    		rojas.setText(t.getRoj()+"");
			    		TextView amarillas = (TextView) fila.findViewById(R.id.txtAmarillas);
			    		amarillas.setText(t.getAma()+"");
			    		
			    		FrameLayout frm = new FrameLayout(getActivity());
						frm.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1));
						frm.setBackgroundColor(Color.parseColor("#222222"));
						
						tablaTarjetas.addView(fila);
						if(it.hasNext()) tablaTarjetas.addView(frm);
			    		pos++;
					}
				}
			}
		});
		
		btnJugadorGolesEquipo = (Button) v.findViewById(R.id.btnGolesJugadorEquipo);
		btnJugadorGolesEquipo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Se construye la tabla donde se mostrará los jugadores que más goles han marcado a un equipo
				if(id_torneo_seleccionado == -1) Toast.makeText(getActivity(), getString(R.string.seleccioneTorneo), Toast.LENGTH_SHORT).show();
				else{
					tablaClasificacion.setVisibility(View.INVISIBLE);
					tablaGoleadores.setVisibility(View.INVISIBLE);
					tablaTarjetas.setVisibility(View.INVISIBLE);
					tablaJugadorGolesEquipo.setVisibility(View.VISIBLE);
					if(tablaJugadorGolesEquipo.getChildCount()>1) tablaJugadorGolesEquipo.removeViews(1, tablaJugadorGolesEquipo.getChildCount()-1);
					
					lista_goleadores_a_equipo = PantallaInicial.bd.obtenerJugadorGolesEquipo(id_torneo_seleccionado);
					Iterator<GoleadorEquipos> it = lista_goleadores_a_equipo.iterator();
					int pos = 1;
					while(it.hasNext()){
						GoleadorEquipos t = it.next();
						LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			    		TableRow fila = (TableRow) inflater.inflate(R.layout.futbol_estadisticas_jugador_goles_equipo_table_row, null);
			    		TextView posicion = (TextView) fila.findViewById(R.id.txtPosicion);
			    		posicion.setText(pos+".");
			    		TextView equipo = (TextView) fila.findViewById(R.id.txtEquipo);
			    		equipo.setText(PantallaInicial.bd.obtenerNombreEquipo(t.getEqu()));
			    		TextView jugador = (TextView) fila.findViewById(R.id.txtJugador);
			    		jugador.setText(PantallaInicial.bd.obtenerJugador(t.getJug(),1).getApo());
			    		TextView goles = (TextView) fila.findViewById(R.id.txtGoles);
			    		goles.setText(t.getGol()+"");
			    		
			    		FrameLayout frm = new FrameLayout(getActivity());
						frm.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1));
						frm.setBackgroundColor(Color.parseColor("#222222"));
						
						tablaJugadorGolesEquipo.addView(fila);
						if(it.hasNext()) tablaJugadorGolesEquipo.addView(frm);
			    		pos++;
					}
				}
			}
		});
		
		return v;
	}
	
	//Este método es para ordenar la clasificación. Primero se ordena por puntos, y después por diferencia entre goles a favor y en contra
	private void ordenarClasificacion(){
		ArrayList<Clasificacion> lista_aux = new ArrayList<Clasificacion>();
		while(!lista_clasificacion.isEmpty()){
			Clasificacion aux = new Clasificacion();
			int puntos_max = lista_clasificacion.get(0).getGan()*3 + lista_clasificacion.get(0).getEmp();
			int gf_max = lista_clasificacion.get(0).getGf();
			int dif_max = gf_max - lista_clasificacion.get(0).getGc();
			
			Iterator<Clasificacion> it = lista_clasificacion.iterator();
			while(it.hasNext()){
				Clasificacion c = it.next();
				if((c.getGan()*3 + c.getEmp()) >= puntos_max){
					if((c.getGf()-c.getGc()) >= dif_max){
						puntos_max = c.getGan()*3 + c.getEmp();
						dif_max = c.getGf() - c.getGc(); 
						aux = c;
					}
				}	
			}
			lista_clasificacion.remove(aux);
			lista_aux.add(aux);
		}
		lista_clasificacion = (ArrayList<Clasificacion>) lista_aux.clone();
	}

	@Override
	public void onStart() {
		super.onStart();
	}
}
