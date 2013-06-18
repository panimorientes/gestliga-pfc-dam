//Esta actividad es para añadir equipos a un torneo de balonmano

package balonmano;

import java.util.ArrayList;
import java.util.Iterator;

import tipos.Equipo;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gestliga.PantallaInicial;
import com.example.gestliga.R;

public class PantallaAnadirEquipoBalonmano extends Activity {

	private String buscar="";
	private LinearLayout llAnadir,llBuscar;
	private EditText edtBuscar;
	private Button btnAnadir;
	private ArrayList<Equipo> lista_equipos = new ArrayList<Equipo>();
	private ArrayList<Integer> lista_equipos_seleccionados = new ArrayList<Integer>();
	private Equipo equipo;
	int posicion_buscar = 0, posicion_anadir = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.balonmano_activity_pantalla_anadir_equipo);
		
		//Se asignan las referencias de las vistas en el layout
		btnAnadir = (Button) findViewById(R.id.btnGuardarGestionTorneoAnadirEquipo);
		btnAnadir.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(PantallaInicial.bd.insertarEquiposEnTorneo(lista_equipos_seleccionados, getIntent().getIntExtra("id_torneo",-1)) == 1){
					PantallaInicial.bd.eliminarPartidosTorneo(getIntent().getIntExtra("id_torneo", -1));
					Toast.makeText(getApplicationContext(), getString(R.string.equiposAnadidos), Toast.LENGTH_SHORT).show();
					finish();
				}else{
					Toast.makeText(getApplicationContext(), getString(R.string.equiposNoAnadidos), Toast.LENGTH_SHORT).show();
				}
			}
		});
		llAnadir = (LinearLayout) findViewById(R.id.llBMGestionTorneoEquiposSeleccionados);
		llBuscar = (LinearLayout) findViewById(R.id.llBMGestionTorneoEquiposBuscar);
		edtBuscar = (EditText) findViewById(R.id.edtGestionTorneoBuscar);
		edtBuscar.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
			@Override
			public void afterTextChanged(Editable s) {
				buscar = edtBuscar.getText().toString();
				cargarEquipos();
			}
		});
		posicion_buscar = 0;
		posicion_anadir = 0;
		cargarEquipos();
	}
	
	//Método para mostrar los equipos
	private void cargarEquipos(){
		llBuscar.removeAllViews();
		lista_equipos.clear();
		lista_equipos = PantallaInicial.bd.obtenerEquiposPorNombre(buscar, 5, getIntent().getIntExtra("id_torneo",-1));
		Iterator<Equipo> it = lista_equipos.iterator();
		posicion_buscar = 0;
		while(it.hasNext()){
			equipo = it.next();
			if(lista_equipos_seleccionados.indexOf(equipo.getId())<0){
				LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
				LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.balonmano_fila_buscar_equipo, null);
				TextView txtEquipo = (TextView) ll.findViewById(R.id.txtGestionTorneoAnadirEquipoFilaEquipo);
				txtEquipo.setText(equipo.getNom());
				ImageButton img = (ImageButton) ll.findViewById(R.id.imgGestionTorneoAnadirEquipoFilaEquipo);
				img.setTag(posicion_buscar);
				img.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						cargarListaSeleccionada(v);
					}
				});
				llBuscar.addView(ll);
			}
			posicion_buscar++;
		}
	}
	
	//Método para cargar la lista de equipos seleccionados
	private void cargarListaSeleccionada(View v){
		lista_equipos_seleccionados.add(lista_equipos.get(Integer.parseInt(v.getTag()+"")).getId());
		LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.balonmano_fila_anadir_equipo, null);
		TextView txtEquipo = (TextView) ll.findViewById(R.id.txtGestionTorneoAnadirEquipoFilaEquipoEliminar);
		txtEquipo.setText(lista_equipos.get(Integer.parseInt(v.getTag()+"")).getNom());
		ImageButton img = (ImageButton) ll.findViewById(R.id.imgGestionTorneoAnadirEquipoFilaEquipoEliminar);
		img.setTag(posicion_anadir);
		img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				lista_equipos_seleccionados.remove(Integer.parseInt(v.getTag()+""));
				llAnadir.removeViewAt(Integer.parseInt(v.getTag()+"")); 
				posicion_anadir--;
				cargarEquipos();
				for(int i = 0; i<llAnadir.getChildCount();i++){
					View vista = llAnadir.getChildAt(i);
					View boton = ((ViewGroup)vista).getChildAt(0);
					boton.setTag(i);
				}
			}
		});
		llAnadir.addView(ll);
		posicion_anadir++;
		cargarEquipos();
	}
}
