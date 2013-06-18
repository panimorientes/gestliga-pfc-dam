//Esta pestaña representa las estadísticas de baloncesto

package fragments;

import java.util.ArrayList;
import java.util.Iterator;

import tipos.Clasificacion;
import tipos.ClasificacionBaloncesto;
import tipos.Equipo;
import tipos.Recuento;
import tipos.Rendimiento;
import tipos.Torneo;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
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
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.gestliga.PantallaInicial;
import com.example.gestliga.R;

public class BaloncestoFragmentEst extends SherlockFragment {

	private Spinner spnTorneo;
	private TableLayout tablaClasificacion, tablaAnotadores, tablaMVP, tablaRendimientoEquipos;
	private Button btnClasificacion, btnAnotadores, btnMVP, btnRendimientoEquipos;
	ArrayList<Torneo> lista_torneos = new ArrayList<Torneo>();
	ArrayList<Equipo> lista_equipos = new ArrayList<Equipo>();
	ArrayList<ClasificacionBaloncesto> lista_clasificacion = new ArrayList<ClasificacionBaloncesto>();
	ArrayList<Recuento> lista_anotadores = new ArrayList<Recuento>();
	ArrayList<Recuento> lista_mvp = new ArrayList<Recuento>();
	ArrayList<Rendimiento> lista_rendimiento = new ArrayList<Rendimiento>();
	
	private int id_torneo_seleccionado = -1;
	
	private View v;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.baloncesto_fragment_est, container, false);

		//Se asignan las referencias de las vistas en el layout
		lista_torneos = PantallaInicial.bd.obtenerTorneos("tor_nom", 3);
		
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
		tablaAnotadores = (TableLayout) v.findViewById(R.id.tablaAnotadores);
		tablaMVP = (TableLayout) v.findViewById(R.id.tablaMVP);
		tablaRendimientoEquipos = (TableLayout) v.findViewById(R.id.tablaRendmientoEquipos);
		
		btnClasificacion = (Button) v.findViewById(R.id.btnClasificacion);
		btnClasificacion.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Se construye la tabla de clasificación
				if(id_torneo_seleccionado == -1) Toast.makeText(getActivity(), getString(R.string.seleccioneTorneo), Toast.LENGTH_SHORT).show();
				else{
					tablaClasificacion.setVisibility(View.VISIBLE);
					if(tablaClasificacion.getChildCount()>1) tablaClasificacion.removeViews(1, tablaClasificacion.getChildCount()-1);
					tablaAnotadores.setVisibility(View.INVISIBLE);
					tablaMVP.setVisibility(View.INVISIBLE);
					tablaRendimientoEquipos.setVisibility(View.INVISIBLE);
					
					lista_clasificacion = PantallaInicial.bd.obtenerClasificacionBaloncesto(id_torneo_seleccionado);
					ordenarClasificacion();
					Iterator<ClasificacionBaloncesto> it = lista_clasificacion.iterator();
					int pos = 1;
					while(it.hasNext()){
						ClasificacionBaloncesto c = it.next();
						LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			    		TableRow fila = (TableRow) inflater.inflate(R.layout.baloncesto_estadisticas_clasificacion_table_row, null);
			    		TextView posicion = (TextView) fila.findViewById(R.id.txtPosicion);
			    		posicion.setText(pos+".");
			    		TextView equipo = (TextView) fila.findViewById(R.id.txtEquipo);
			    		equipo.setText(PantallaInicial.bd.obtenerNombreEquipo(c.getId_equipo()));
			    		TextView jugados = (TextView) fila.findViewById(R.id.txtJugados);
			    		jugados.setText(c.getJug()+"");
			    		TextView ganados = (TextView) fila.findViewById(R.id.txtGanados);
			    		ganados.setText(c.getGan()+"");
			    		TextView perdidos = (TextView) fila.findViewById(R.id.txtPerdidos);
			    		perdidos.setText(c.getPer()+"");
			    		TextView puntosF = (TextView) fila.findViewById(R.id.txtPuntosFavor);
			    		puntosF.setText(c.getPf()+"");
			    		TextView puntosC = (TextView) fila.findViewById(R.id.txtPuntosContra);
			    		puntosC.setText(c.getPc()+"");
			    		TextView dif = (TextView) fila.findViewById(R.id.txtDiferencia);
			    		dif.setText((c.getPf()-c.getPc())+"");
			    		TextView puntos = (TextView) fila.findViewById(R.id.txtPuntos);
			    		puntos.setText(c.getGan()+"");
			    		
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
		
		btnAnotadores = (Button) v.findViewById(R.id.btnMaxAnotadores);
		btnAnotadores.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Se construye la tabla de máximos anotadores
				if(id_torneo_seleccionado == -1) Toast.makeText(getActivity(), getString(R.string.seleccioneTorneo), Toast.LENGTH_SHORT).show();
				else{
					tablaClasificacion.setVisibility(View.INVISIBLE);
					tablaAnotadores.setVisibility(View.VISIBLE);
					if(tablaAnotadores.getChildCount()>1) tablaAnotadores.removeViews(1, tablaAnotadores.getChildCount()-1);
					tablaMVP.setVisibility(View.INVISIBLE);
					tablaRendimientoEquipos.setVisibility(View.INVISIBLE);
					
					lista_anotadores = PantallaInicial.bd.obtenerAnotadoresTorneo(id_torneo_seleccionado);
					Iterator<Recuento> it = lista_anotadores.iterator();
					int pos = 1;
					while(it.hasNext()){
						Recuento g = it.next();
						LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			    		TableRow fila = (TableRow) inflater.inflate(R.layout.baloncesto_estadisticas_anotadores_table_row, null);
			    		TextView posicion = (TextView) fila.findViewById(R.id.txtPosicion);
			    		posicion.setText(pos+".");
			    		TextView jugador = (TextView) fila.findViewById(R.id.txtJugador);
			    		jugador.setText(PantallaInicial.bd.obtenerJugador(g.getJug(),3).getApo());
			    		TextView equipo = (TextView) fila.findViewById(R.id.txtEquipo);
			    		equipo.setText(PantallaInicial.bd.obtenerNombreEquipo(g.getEqu()));
			    		TextView puntos = (TextView) fila.findViewById(R.id.txtPuntos);
			    		puntos.setText(g.getGoles()+"");
			    		
			    		FrameLayout frm = new FrameLayout(getActivity());
						frm.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1));
						frm.setBackgroundColor(Color.parseColor("#222222"));
						
						tablaAnotadores.addView(fila);
						if(it.hasNext()) tablaAnotadores.addView(frm);
			    		pos++;
					}
				}
			}
		});
		
		btnMVP = (Button) v.findViewById(R.id.btnMVP);
		btnMVP.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Se construye la clasificación de los jugadores que han sido nombrados MVP
				if(id_torneo_seleccionado == -1) Toast.makeText(getActivity(), getString(R.string.seleccioneTorneo), Toast.LENGTH_SHORT).show();
				else{
					tablaClasificacion.setVisibility(View.INVISIBLE);
					tablaAnotadores.setVisibility(View.INVISIBLE);
					tablaMVP.setVisibility(View.VISIBLE);
					if(tablaMVP.getChildCount()>1) tablaMVP.removeViews(1, tablaMVP.getChildCount()-1);
					tablaRendimientoEquipos.setVisibility(View.INVISIBLE);
					
					lista_mvp = PantallaInicial.bd.obtenerJugadoresMVP(id_torneo_seleccionado);
					Iterator<Recuento> it = lista_mvp.iterator();
					int pos = 1;
					while(it.hasNext()){
						Recuento g = it.next();
						LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			    		TableRow fila = (TableRow) inflater.inflate(R.layout.baloncesto_estadisticas_mvp_table_row, null);
			    		TextView posicion = (TextView) fila.findViewById(R.id.txtPosicion);
			    		posicion.setText(pos+".");
			    		TextView jugador = (TextView) fila.findViewById(R.id.txtJugador);
			    		jugador.setText(PantallaInicial.bd.obtenerJugador(g.getJug(),3).getApo());
			    		TextView equipo = (TextView) fila.findViewById(R.id.txtEquipo);
			    		equipo.setText(PantallaInicial.bd.obtenerNombreEquipo(g.getEqu()));
			    		TextView mvp = (TextView) fila.findViewById(R.id.txtMVP);
			    		mvp.setText(g.getGoles()+"");
			    		
			    		FrameLayout frm = new FrameLayout(getActivity());
						frm.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1));
						frm.setBackgroundColor(Color.parseColor("#222222"));
						
						tablaMVP.addView(fila);
						if(it.hasNext()) tablaMVP.addView(frm);
			    		pos++;
					}
				}
			}
		});
		
		btnRendimientoEquipos = (Button) v.findViewById(R.id.btnRendimientoEquipo);
		btnRendimientoEquipos.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Se construye la tabla donde se mostrará la serie de partidos ganados/perdidos de los equipos	
				if(id_torneo_seleccionado == -1) Toast.makeText(getActivity(), getString(R.string.seleccioneTorneo), Toast.LENGTH_SHORT).show();
				else{
					tablaClasificacion.setVisibility(View.INVISIBLE);
					tablaAnotadores.setVisibility(View.INVISIBLE);
					tablaMVP.setVisibility(View.INVISIBLE);
					tablaRendimientoEquipos.setVisibility(View.VISIBLE);
					if(tablaRendimientoEquipos.getChildCount()>1) tablaRendimientoEquipos.removeViews(1, tablaRendimientoEquipos.getChildCount()-1);
					
					lista_rendimiento = PantallaInicial.bd.obtenerRendimientoEquipos(id_torneo_seleccionado);
					Iterator<Rendimiento> it = lista_rendimiento.iterator();
					int pos = 1;
					while(it.hasNext()){
						Rendimiento g = it.next();
						LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			    		TableRow fila = (TableRow) inflater.inflate(R.layout.baloncesto_estadisticas_rendimiento_table_row, null);
			    		TextView equipo = (TextView) fila.findViewById(R.id.txtEquipo);
			    		equipo.setText(PantallaInicial.bd.obtenerNombreEquipo(g.getEqu()));
			    		TextView progresion = (TextView) fila.findViewById(R.id.txtProgresion);
			    		Iterator<String> is = lista_rendimiento.get(pos-1).getPro().iterator();
			    		String r = "";
			    		while(is.hasNext()){
			    			if(is.next().equals("G")){
			    				r = r + " " + Html.fromHtml("<![CDATA[<font color=#04B431>G</font>]]>");
			    			}else{
			    				r = r + " " + Html.fromHtml("<![CDATA[<font color=#8A0808>P</font>]]>");
			    			}
			    		}
			    		progresion.setText(Html.fromHtml(r));
			    		
			    		FrameLayout frm = new FrameLayout(getActivity());
						frm.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1));
						frm.setBackgroundColor(Color.parseColor("#222222"));
						
						tablaRendimientoEquipos.addView(fila);
						if(it.hasNext()) tablaRendimientoEquipos.addView(frm);
			    		pos++;
					}
				}
			}
		});
		
		return v;
	}
	
	//Este método es para ordenar la clasificación. Primero se ordena por puntos, y después por diferencia entre puntos a favor y en contra
	private void ordenarClasificacion(){
		ArrayList<ClasificacionBaloncesto> lista_aux = new ArrayList<ClasificacionBaloncesto>();
		while(!lista_clasificacion.isEmpty()){
			ClasificacionBaloncesto aux = new ClasificacionBaloncesto();
			int puntos_max = lista_clasificacion.get(0).getGan();
			int pf_max = lista_clasificacion.get(0).getPf();
			int dif_max = pf_max - lista_clasificacion.get(0).getPc();
			Iterator<ClasificacionBaloncesto> it = lista_clasificacion.iterator();
			while(it.hasNext()){
				ClasificacionBaloncesto c = it.next();
				if(c.getGan() >= puntos_max){
					if((c.getPf()-c.getPc()) >= dif_max){
						puntos_max = c.getGan();
						dif_max = c.getPf() - c.getPc();
						aux = c;
					}
				}
			}
			lista_clasificacion.remove(aux);
			lista_aux.add(aux);
		}
		lista_clasificacion = (ArrayList<ClasificacionBaloncesto>) lista_aux.clone();
	}

	@Override
	public void onStart() {
		super.onStart();
	}
}
