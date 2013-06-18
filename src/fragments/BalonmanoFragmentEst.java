//Esta pestaña representa las estadísticas de balonmano

package fragments;

import java.util.ArrayList;
import java.util.Iterator;

import tipos.Clasificacion;
import tipos.Equipo;
import tipos.GoleadorEquipos;
import tipos.Recuento;
import tipos.Rendimiento;
import tipos.Tarjetas;
import tipos.Torneo;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TableRow.LayoutParams;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.gestliga.PantallaInicial;
import com.example.gestliga.R;

public class BalonmanoFragmentEst extends SherlockFragment {

	private Spinner spnTorneo;
	private TableLayout tablaClasificacion, tablaGoleadores, tablaRendimientoEquipos;
	private Button btnClasificacion, btnGoleadores, btnRendimiento;
	ArrayList<Torneo> lista_torneos = new ArrayList<Torneo>();
	ArrayList<Equipo> lista_equipos = new ArrayList<Equipo>();
	ArrayList<Clasificacion> lista_clasificacion = new ArrayList<Clasificacion>();
	ArrayList<Recuento> lista_goleadores = new ArrayList<Recuento>();
	ArrayList<Rendimiento> lista_rendimiento = new ArrayList<Rendimiento>();
	
	private int id_torneo_seleccionado = -1;
	
	private View v;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.balonmano_fragment_est, container, false);
		
		//Se asignan las referencias de las vistas en el layout
		lista_torneos = PantallaInicial.bd.obtenerTorneos("tor_id", 5);
		
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
					tablaGoleadores.setVisibility(View.INVISIBLE);
					tablaRendimientoEquipos.setVisibility(View.INVISIBLE);
					
					lista_clasificacion = PantallaInicial.bd.obtenerClasificacion(id_torneo_seleccionado, 5);
					ordenarClasificacion();
					Iterator<Clasificacion> it = lista_clasificacion.iterator();
					int pos = 1;
					while(it.hasNext()){
						Clasificacion c = it.next();
						LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			    		TableRow fila = (TableRow) inflater.inflate(R.layout.balonmano_estadisticas_clasificacion_table_row, null);
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
			    		puntos.setText((c.getGan()*2 + c.getEmp())+"");
			    		
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
				//Se construye la tabla de goleadores
				if(id_torneo_seleccionado == -1) Toast.makeText(getActivity(), getString(R.string.seleccioneTorneo), Toast.LENGTH_SHORT).show();
				else{
					tablaClasificacion.setVisibility(View.INVISIBLE);
					tablaGoleadores.setVisibility(View.VISIBLE);
					if(tablaGoleadores.getChildCount()>1) tablaGoleadores.removeViews(1, tablaGoleadores.getChildCount()-1);
					tablaRendimientoEquipos.setVisibility(View.INVISIBLE);
					
					lista_goleadores = PantallaInicial.bd.obtenerGoleadoresTorneoBalonmano(id_torneo_seleccionado); //Cambiar para balonmano
					Iterator<Recuento> it = lista_goleadores.iterator();
					int pos = 1;
					while(it.hasNext()){
						Recuento g = it.next();
						LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			    		TableRow fila = (TableRow) inflater.inflate(R.layout.balonmano_estadisticas_goleadores_table_row, null);
			    		TextView posicion = (TextView) fila.findViewById(R.id.txtPosicion);
			    		posicion.setText(pos+".");
			    		TextView jugador = (TextView) fila.findViewById(R.id.txtJugador);
			    		jugador.setText(PantallaInicial.bd.obtenerJugador(g.getJug(),5).getApo());
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
		
		btnRendimiento = (Button) v.findViewById(R.id.btnRendimientoEquipos);
		btnRendimiento.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Se construye la tabla donde se mostrará la serie de partidos ganados/perdidos de los equipos
				if(id_torneo_seleccionado == -1) Toast.makeText(getActivity(), getString(R.string.seleccioneTorneo), Toast.LENGTH_SHORT).show();
				else{
					tablaClasificacion.setVisibility(View.INVISIBLE);
					tablaGoleadores.setVisibility(View.INVISIBLE);
					tablaRendimientoEquipos.setVisibility(View.VISIBLE);
					if(tablaRendimientoEquipos.getChildCount()>1) tablaRendimientoEquipos.removeViews(1, tablaRendimientoEquipos.getChildCount()-1);
					
					lista_rendimiento = PantallaInicial.bd.obtenerRendimientoEquiposBalonmano(id_torneo_seleccionado);
					Iterator<Rendimiento> it = lista_rendimiento.iterator();
					int pos = 1;
					while(it.hasNext()){
						Rendimiento g = it.next();
						LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			    		TableRow fila = (TableRow) inflater.inflate(R.layout.balonmano_estadisticas_rendimiento_table_row, null);
			    		TextView equipo = (TextView) fila.findViewById(R.id.txtEquipo);
			    		equipo.setText(PantallaInicial.bd.obtenerNombreEquipo(g.getEqu()));
			    		TextView progresion = (TextView) fila.findViewById(R.id.txtProgresion);
			    		Iterator<String> is = lista_rendimiento.get(pos-1).getPro().iterator();
			    		String r = "";
			    		while(is.hasNext()){
			    			String aux="";
			    			aux = is.next();
			    			if(aux.equals("G")){
			    				r = r + " " + Html.fromHtml("<![CDATA[<font color=#04B431>G</font>]]>");
			    			}else if(aux.equals("P")){
			    				r = r + " " + Html.fromHtml("<![CDATA[<font color=#8A0808>P</font>]]>");
			    			}else{
			    				r = r + " " + Html.fromHtml("<![CDATA[<font color=#DF7401>E</font>]]>");
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
	
	//Este método es para ordenar la clasificación. Primero se ordena por puntos, y después por diferencia entre goles a favor y en contra
	private void ordenarClasificacion(){
		ArrayList<Clasificacion> lista_aux = new ArrayList<Clasificacion>();
		while(!lista_clasificacion.isEmpty()){
			Clasificacion aux = new Clasificacion();
			int puntos_max = lista_clasificacion.get(0).getGan()*2 + lista_clasificacion.get(0).getEmp();
			int gf_max = lista_clasificacion.get(0).getGf();
			int dif_max = gf_max - lista_clasificacion.get(0).getGc();
			
			Iterator<Clasificacion> it = lista_clasificacion.iterator();
			while(it.hasNext()){
				Clasificacion c = it.next();
				if((c.getGan()*2 + c.getEmp()) >= puntos_max){
					if((c.getGf()-c.getGc()) >= dif_max){
						puntos_max = c.getGan()*2 + c.getEmp();
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
