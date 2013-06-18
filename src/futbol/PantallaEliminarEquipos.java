//Esta actividad es para eliminar equipos a un torneo de fútbol

package futbol;

import java.util.ArrayList;
import java.util.Iterator;

import tipos.Equipo;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.gestliga.PantallaInicial;
import com.example.gestliga.R;

public class PantallaEliminarEquipos extends Activity {

	ArrayList<Equipo> lista_equipos = new ArrayList<Equipo>();
	ArrayList<Integer> lista_equipos_checkeds = new ArrayList<Integer>();
	private Button btnEliminar;
	private LinearLayout llEliminar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.futbol_activity_pantalla_eliminar_equipos);
		//Se asignan las referencias de las vistas en el layout	
		llEliminar = (LinearLayout) findViewById(R.id.llFutbolGestionTorneoEquiposEliminar);
		//Se cargan los equipos que están en el torneo
		lista_equipos = PantallaInicial.bd.obtenerEquiposDeTorneo(getIntent().getIntExtra("id_torneo", -1), 1);
		Iterator<Equipo> it = lista_equipos.iterator();
		int position = 0;
		while(it.hasNext()){
			Equipo equipo = it.next();
			CheckBox ch = new CheckBox(this);
			ch.setText(equipo.getNom());
			ch.setTextColor(Color.parseColor(getString(R.color.negro)));
			ch.setTag(position);
			ch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					int tag = (Integer) buttonView.getTag();
					if(isChecked){
						lista_equipos_checkeds.add(lista_equipos.get(tag).getId());
					}else{
						lista_equipos_checkeds.remove(lista_equipos_checkeds.indexOf(lista_equipos.get(tag).getId()));
					}
				}
			});
			llEliminar.addView(ch);
			position++;
		}
		
		btnEliminar = (Button) findViewById(R.id.btnEliminarGestionTorneoEliminarEquipo);
		btnEliminar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!lista_equipos_checkeds.isEmpty()){
					if(PantallaInicial.bd.eliminarEquiposTorneo(lista_equipos_checkeds, getIntent().getIntExtra("id_torneo", -1)) == 1){
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
