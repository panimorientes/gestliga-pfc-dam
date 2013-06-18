//Esta actividad es para eliminar jugadores a un torneo de tenis
package tenis;

import java.util.ArrayList;
import java.util.Iterator;

import tipos.*;

import com.example.gestliga.PantallaInicial;
import com.example.gestliga.R;
import com.example.gestliga.R.layout;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class PantallaEliminarJugadores extends Activity {

	ArrayList<Jugador> lista_jugadores = new ArrayList<Jugador>();
	ArrayList<Integer> lista_jugadores_checkeds = new ArrayList<Integer>();
	private Button btnEliminar;
	private LinearLayout llEliminar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tenis_activity_pantalla_eliminar_jugadores);
		//Se asignan las referencias de las vistas en el layout
		llEliminar = (LinearLayout) findViewById(R.id.llTenisGestionTorneoJugadoresEliminar);
		//Se cargan los jugadores que están en el torneo
		lista_jugadores = PantallaInicial.bd.obtenerJugadoresDeTorneo(getIntent().getIntExtra("id_torneo", -1), "", 2);
		Iterator<Jugador> it = lista_jugadores.iterator();
		int position = 0;
		while(it.hasNext()){
			Jugador jugador = it.next();
			CheckBox ch = new CheckBox(this);
			ch.setText(jugador.getNom());
			ch.setTextColor(Color.parseColor(getString(R.color.negro)));
			ch.setTag(position);
			ch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					int tag = (Integer) buttonView.getTag();
					if(isChecked){
						lista_jugadores_checkeds.add(lista_jugadores.get(tag).getId());
					}else{
						lista_jugadores_checkeds.remove(lista_jugadores_checkeds.indexOf(lista_jugadores.get(tag).getId()));
					}
				}
			});
			llEliminar.addView(ch);
			position++;
		}
		
		btnEliminar = (Button) findViewById(R.id.btnEliminarGestionTorneoEliminarJugador);
		btnEliminar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!lista_jugadores_checkeds.isEmpty()){
					if(PantallaInicial.bd.eliminarJugadoresTorneo(lista_jugadores_checkeds, getIntent().getIntExtra("id_torneo", -1)) == 1){
						PantallaInicial.bd.eliminarPartidosTorneo(getIntent().getIntExtra("id_torneo", -1));
						Toast.makeText(getApplicationContext(), getString(R.string.BDEliminado), Toast.LENGTH_SHORT).show();
						finish();
					}else{
						Toast.makeText(getApplicationContext(), getString(R.string.BDNoEliminado), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
}
