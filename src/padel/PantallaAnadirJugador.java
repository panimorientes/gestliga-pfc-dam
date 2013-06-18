//Esta actividad es para añadir jugadores a un torneo de pádel

package padel;

import java.util.ArrayList;
import java.util.Iterator;

import tipos.Jugador;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class PantallaAnadirJugador extends Activity {

	private String buscar="";
	private LinearLayout llAnadir,llBuscar;
	private EditText edtBuscar;
	private Button btnAnadir;
	private ArrayList<Jugador> lista_jugadores = new ArrayList<Jugador>();
	private ArrayList<Integer> lista_jugadores_seleccionados = new ArrayList<Integer>();
	private Jugador jugador;
	int posicion_buscar = 0, posicion_anadir = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.padel_activity_pantalla_anadir_jugador);
		
		//Se asignan las referencias de las vistas en el layout
		btnAnadir = (Button) findViewById(R.id.btnGuardarGestionTorneoAnadirJugador);
		btnAnadir.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(PantallaInicial.bd.insertarJugadoresEnTorneo(lista_jugadores_seleccionados, getIntent().getIntExtra("id_torneo",-1)) == 1){
					PantallaInicial.bd.eliminarPartidosTorneo(getIntent().getIntExtra("id_torneo", -1));
					Toast.makeText(getApplicationContext(), getString(R.string.jugadoresAnadidos), Toast.LENGTH_SHORT).show();
					finish();
				}else{
					Toast.makeText(getApplicationContext(), getString(R.string.jugadoresNoAnadidos), Toast.LENGTH_SHORT).show();
				}
			}
		});
		llAnadir = (LinearLayout) findViewById(R.id.llPadelGestionTorneoJugadoresSeleccionados);
		llBuscar = (LinearLayout) findViewById(R.id.llPadelGestionTorneoJugadoresBuscar);
		edtBuscar = (EditText) findViewById(R.id.edtGestionTorneoBuscarPadel);
		edtBuscar.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
			@Override
			public void afterTextChanged(Editable s) {
				buscar = edtBuscar.getText().toString();
				cargarJugadores();
			}
		});
		posicion_buscar = 0;
		posicion_anadir = 0;
		cargarJugadores();
	}
	
	//Método para mostrar los jugadores
	private void cargarJugadores(){
		llBuscar.removeAllViews();
		lista_jugadores.clear();
		lista_jugadores = PantallaInicial.bd.obtenerJugadoresPorNombre(buscar, 4, getIntent().getIntExtra("id_torneo", -1));
		Iterator<Jugador> it = lista_jugadores.iterator();
		posicion_buscar = 0;
		while(it.hasNext()){
			jugador = it.next();
			if(lista_jugadores_seleccionados.indexOf(jugador.getId())<0){
				LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
				LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.padel_fila_buscar_jugador, null);
				TextView txtEquipo = (TextView) ll.findViewById(R.id.txtGestionTorneoAnadirJugadorFilaJugador);
				txtEquipo.setText(jugador.getNom());
				ImageButton img = (ImageButton) ll.findViewById(R.id.imgGestionTorneoAnadirJugadorFilaJugador);
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
	
	//Método para cargar la lista de jugadores seleccionados
	private void cargarListaSeleccionada(View v){
		lista_jugadores_seleccionados.add(lista_jugadores.get(Integer.parseInt(v.getTag()+"")).getId());
		LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.padel_fila_anadir_jugador, null);
		TextView txtEquipo = (TextView) ll.findViewById(R.id.txtGestionTorneoAnadirJugadorFilaJugadorEliminar);
		txtEquipo.setText(lista_jugadores.get(Integer.parseInt(v.getTag()+"")).getNom());
		ImageButton img = (ImageButton) ll.findViewById(R.id.imgGestionTorneoAnadirJugadorFilaJugadorEliminar);
		img.setTag(posicion_anadir);
		img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				lista_jugadores_seleccionados.remove(Integer.parseInt(v.getTag()+""));
				llAnadir.removeViewAt(Integer.parseInt(v.getTag()+"")); 
				posicion_anadir--;
				cargarJugadores();
				for(int i = 0; i<llAnadir.getChildCount();i++){
					View vista = llAnadir.getChildAt(i);
					View boton = ((ViewGroup)vista).getChildAt(0);
					boton.setTag(i);
				}
			}
		});
		llAnadir.addView(ll);
		posicion_anadir++;
		cargarJugadores();
	}
}
