//Esta pestaña representa las estadísticas de tenis

package fragments;

import java.util.ArrayList;
import java.util.Iterator;

import tipos.Clasificacion;
import tipos.Jugador;
import tipos.PartidoMejorVictoria;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TableRow.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.gestliga.PantallaInicial;
import com.example.gestliga.R;

public class TenisFragmentEst extends SherlockFragment{

	private LinearLayout llMejorVictoria, llSetsFC, llPartidosGP;
	private TableLayout tablaClasificacion;
	private Button btnMejorVictoria, btnSetsFC, btnPartidosGP, btnClasificacion;
	private Spinner spnJugador;
	private int id_jugador_seleccionado = -1;
	private ArrayList<Jugador> lista_jugadores = new ArrayList<Jugador>();
	private ArrayList<int[]> lista_pares_clasificacion = new ArrayList<int[]>();
	private int[] sets = {0,0};
	private int[] partidos ={0,0};
	private TextView txtTorneo, txtRonda, txtJugador1, txtJugador2, txtSet11, txtSet12, txtSet13, txtSet21, txtSet22, txtSet23, txtDuracion; 
	private TextView txtJugador, setF, setC;
	private TextView txtJugadorGP, txtPartidosG, txtPartidosP;
	
	private View v;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.tenis_fragment_est, container, false);

		//Se asignan las referencias de las vistas en el layout
		lista_jugadores = PantallaInicial.bd.obtenerJugadores("jug_apo", 2);
		
		llMejorVictoria = (LinearLayout) v.findViewById(R.id.llMejorVictoria);
		llSetsFC = (LinearLayout) v.findViewById(R.id.llSetsFC);
		llPartidosGP = (LinearLayout) v.findViewById(R.id.llPartidosGP);
		tablaClasificacion = (TableLayout) v.findViewById(R.id.tablaClasificacion);
		
		//Botón 1
		txtTorneo = (TextView) v.findViewById(R.id.txtTorneo);
		txtRonda = (TextView) v.findViewById(R.id.txtRonda);
		txtJugador1 = (TextView) v.findViewById(R.id.txtJugador1);
		txtJugador2 = (TextView) v.findViewById(R.id.txtJugador2);
		txtSet11 = (TextView) v.findViewById(R.id.txtset11);
		txtSet12 = (TextView) v.findViewById(R.id.txtset12);
		txtSet13 = (TextView) v.findViewById(R.id.txtset13);
		txtSet21 = (TextView) v.findViewById(R.id.txtset21);
		txtSet22 = (TextView) v.findViewById(R.id.txtset22);
		txtSet23 = (TextView) v.findViewById(R.id.txtset23);
		txtDuracion = (TextView) v.findViewById(R.id.txtDuracion);
		
		//Botón 2
		txtJugador = (TextView) v.findViewById(R.id.txtJugador);
		setF = (TextView) v.findViewById(R.id.txtsetsF);
		setC = (TextView) v.findViewById(R.id.txtsetsC);
		
		//Botón 3
		txtJugadorGP = (TextView) v.findViewById(R.id.txtJugadorGP);
		txtPartidosG = (TextView) v.findViewById(R.id.txtPartidosG);
		txtPartidosP = (TextView) v.findViewById(R.id.txtPartidosP);
		
		spnJugador = (Spinner) v.findViewById(R.id.spnJugador);
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(v.getContext(), android.R.layout.simple_spinner_item);
		Iterator<Jugador> it = lista_jugadores.iterator();
		adapter.add("Elija jugador");
		while(it.hasNext()){
			adapter.add(it.next().getApo());
		}
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnJugador.setAdapter(adapter);
		spnJugador.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
				if(parent.getItemAtPosition(position).toString().equals("Elija jugador")) id_jugador_seleccionado = -1;
				else id_jugador_seleccionado = lista_jugadores.get(position-1).getId();
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		
		btnMejorVictoria = (Button) v.findViewById(R.id.btnMejorVictoria);
		btnMejorVictoria.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Se construye la vista que muestra la mejor victoria(mayor diferencia entre sets a favor y sets en contra en un partido)
				if(id_jugador_seleccionado == -1) Toast.makeText(getActivity(), getString(R.string.seleccioneJugador), Toast.LENGTH_SHORT).show();
				else{
					llMejorVictoria.setVisibility(View.VISIBLE);
					llSetsFC.setVisibility(View.INVISIBLE);
					llPartidosGP.setVisibility(View.INVISIBLE);
					tablaClasificacion.setVisibility(View.INVISIBLE);
					
					PartidoMejorVictoria p = PantallaInicial.bd.obtenerMejorVictoria(id_jugador_seleccionado);
					if(p.getJug() != -1){
						txtTorneo.setText(getString(R.string.torneo) + ": " + PantallaInicial.bd.obtenerTorneoTenisPorPartido(p.getPar()).getNom());
						txtRonda.setText(p.getRon());
						
						txtJugador1.setText(PantallaInicial.bd.obtenerJugador(id_jugador_seleccionado, 2).getApo());
						int[] res = PantallaInicial.bd.obtenerResultadoTenis(p.getPar(), id_jugador_seleccionado);
						txtSet11.setText(res[0]+"");
						txtSet12.setText(res[1]+"");
						txtSet13.setText(res[2]+"");
						
						int[] jugs = PantallaInicial.bd.obtenerJugadoresPartido(p.getPar());
						if(jugs[0] == p.getJug()){
							res = PantallaInicial.bd.obtenerResultadoTenis(p.getPar(), jugs[1]);
							txtJugador2.setText(PantallaInicial.bd.obtenerJugador(jugs[1], 2).getApo());
						}else{
							res = PantallaInicial.bd.obtenerResultadoTenis(p.getPar(), jugs[0]);
							txtJugador2.setText(PantallaInicial.bd.obtenerJugador(jugs[0], 2).getApo());
						}
						txtSet21.setText(res[0]+"");
						txtSet22.setText(res[1]+"");
						txtSet23.setText(res[2]+"");
						
						txtDuracion.setText(getString(R.string.duracionPartido) + " " + PantallaInicial.bd.obtenerDuracionPartidoTenis(p.getPar()));
					}else{
						Toast.makeText(getActivity(), getString(R.string.noResultados), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		
		btnSetsFC = (Button) v.findViewById(R.id.btnSetsFC);
		btnSetsFC.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Se construye la vista que muestra los sets a favor y en contra de un jugador
				if(id_jugador_seleccionado == -1) Toast.makeText(getActivity(), getString(R.string.seleccioneJugador), Toast.LENGTH_SHORT).show();
				else{
					llMejorVictoria.setVisibility(View.INVISIBLE);
					llSetsFC.setVisibility(View.VISIBLE);
					llPartidosGP.setVisibility(View.INVISIBLE);
					tablaClasificacion.setVisibility(View.INVISIBLE);
					
					sets = PantallaInicial.bd.obtenerSetsFC(id_jugador_seleccionado);
					
		    		txtJugador.setText(PantallaInicial.bd.obtenerJugador(id_jugador_seleccionado, 2).getApo());
		    		setF.setText(sets[0]+"");
		    		setC.setText(sets[1]+"");
				}
			}
		});
		
		btnPartidosGP = (Button) v.findViewById(R.id.btnPartidosGP);
		btnPartidosGP.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Se construye la vista que muestra el número total de partidos ganados y perdidos de un jugador
				if(id_jugador_seleccionado == -1) Toast.makeText(getActivity(), getString(R.string.seleccioneJugador), Toast.LENGTH_SHORT).show();
				else{
					llMejorVictoria.setVisibility(View.INVISIBLE);
					llSetsFC.setVisibility(View.INVISIBLE);
					llPartidosGP.setVisibility(View.VISIBLE);
					tablaClasificacion.setVisibility(View.INVISIBLE);
					
					partidos = PantallaInicial.bd.obtenerPartidosGP(id_jugador_seleccionado);
					
					txtJugadorGP.setText(PantallaInicial.bd.obtenerJugador(id_jugador_seleccionado, 2).getApo());
					txtPartidosG.setText(partidos[0]+"");
					txtPartidosP.setText(partidos[1]+"");
				}
			}
		});
		
		btnClasificacion = (Button) v.findViewById(R.id.btnClasificacion);
		btnClasificacion.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Se construye la tabla de clasificación
				llMejorVictoria.setVisibility(View.INVISIBLE);
				llSetsFC.setVisibility(View.INVISIBLE);
				llPartidosGP.setVisibility(View.INVISIBLE);
				tablaClasificacion.setVisibility(View.VISIBLE);
				if(tablaClasificacion.getChildCount()>1) tablaClasificacion.removeViews(1, tablaClasificacion.getChildCount()-1);
				
				lista_pares_clasificacion.clear();
				ArrayList<Jugador> lista_jugadores = PantallaInicial.bd.obtenerJugadores("", 2);
				Iterator<Jugador> it = lista_jugadores.iterator();
				while(it.hasNext()){
					Jugador j = it.next();
					int[] par = new int[2];
					par[0] = j.getId();
					par[1] = PantallaInicial.bd.obtenerPuntosClasificacionJugador(j.getId(), 2);
					lista_pares_clasificacion.add(par);
				}
				ordenarClasificacionTenis();
				Iterator<int[]> ic = lista_pares_clasificacion.iterator();
				int pos = 1;
				while(ic.hasNext()){
					int[] c = ic.next();
					LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    		TableRow fila = (TableRow) inflater.inflate(R.layout.tenis_estadisticas_clasificacion_table_row, null);
		    		TextView posicion = (TextView) fila.findViewById(R.id.txtPosicion);
		    		posicion.setText(pos+".");
		    		TextView jugador = (TextView) fila.findViewById(R.id.txtJugador);
		    		jugador.setText(PantallaInicial.bd.obtenerNombreJugador(c[0]));
		    		TextView puntos = (TextView) fila.findViewById(R.id.txtPuntos);
		    		puntos.setText(c[1]+"");
		    		
		    		FrameLayout frm = new FrameLayout(getActivity());
					frm.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1));
					frm.setBackgroundColor(Color.parseColor("#222222"));
					
					tablaClasificacion.addView(fila);
					if(it.hasNext()) tablaClasificacion.addView(frm);
		    		pos++;
				}
			}
		});
		
		return v;
	}
	
	//Este método es para ordenar la clasificación. Se ordena por puntos
	private void ordenarClasificacionTenis() {
		ArrayList<int[]> lista_aux = new ArrayList<int[]>();
		while(!lista_pares_clasificacion.isEmpty()){
			int[] aux = new int[2];
			int puntos_max = 0;
			Iterator<int[]> it = lista_pares_clasificacion.iterator();
			while(it.hasNext()){
				int[] c = it.next();
				if(c[1] >= puntos_max){
					puntos_max = c[1];
					aux = c;
				}
				
			}
			lista_pares_clasificacion.remove(aux);
			lista_aux.add(aux);
			
		}
		lista_pares_clasificacion = (ArrayList<int[]>) lista_aux.clone();
	}

	@Override
	public void onStart() {
		super.onStart();

	}
}