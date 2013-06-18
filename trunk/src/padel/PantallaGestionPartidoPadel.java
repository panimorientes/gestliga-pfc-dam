//Esta actividad sirve para introducir los datos de un partido de padel


package padel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import tipos.Partido;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gestliga.PantallaInicial;
import com.example.gestliga.R;

public class PantallaGestionPartidoPadel extends Activity {

	private TextView txtFecha, txtJ1, txtJ2;
	private Spinner spnHora;
	private EditText edts11, edts12, edts13, edts21, edts22, edts23, edtDuracion;
	private ImageButton btnSeleccionarFecha;
	private Partido partido;
	private int[] id_jugadores = new int[2];
	private int[] resJ1 = {0, 0, 0};
	private int[] resJ2 = {0, 0, 0};
	private int id_partido, duracion=0;
	private int _anio; private int _mes; private int _dia;
	private String hora="";
	private boolean cargando = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.padel_activity_pantalla_gestion_partido);
		
		id_partido = getIntent().getIntExtra("id_partido", -1);
		
		//Se asignan las referencias de las vistas en el layout
		txtFecha = (TextView) findViewById(R.id.txtFechaPartido);
		txtJ1 = (TextView) findViewById(R.id.txtPartidoJugador1);
		txtJ2 = (TextView) findViewById(R.id.txtPartidoJugador2);
		spnHora = (Spinner) findViewById(R.id.spnHoraPartido);
		spnHora.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
				hora = parent.getItemAtPosition(position).toString();
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		
		edts11 = (EditText) findViewById(R.id.edtset11);
		edts11.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				edts11.setText("");
				edts12.setText("");
				edts13.setText("");
				return false;
			}
		});
		edts11.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				if(edts11.getText().toString().length()>0)resJ1[0] = Integer.parseInt(edts11.getText().toString());
				else resJ1[0] = 0;
				comprobarResultadosIntroducidos();
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
		});
		edts12 = (EditText) findViewById(R.id.edtset12);
		edts12.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				edts12.setText("");
				edts13.setText("");
				return false;
			}
		});
		edts12.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				if(edts12.getText().toString().length()>0)resJ1[1] = Integer.parseInt(edts12.getText().toString());
				else resJ1[1] = 0;
				comprobarResultadosIntroducidos();
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
		});
		edts13 = (EditText) findViewById(R.id.edtset13);
		edts13.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				edts13.setText("");
				return false;
			}
		});
		edts13.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				if(edts13.getText().toString().length()>0)resJ1[2] = Integer.parseInt(edts13.getText().toString());
				else resJ1[2] = 0;
				comprobarResultadosIntroducidos();
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
		});
		edts21 = (EditText) findViewById(R.id.edtset21);
		edts21.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				edts21.setText("");
				edts22.setText("");
				edts23.setText("");
				return false;
			}
		});
		edts21.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				if(edts21.getText().toString().length()>0)resJ2[0] = Integer.parseInt(edts21.getText().toString());
				else resJ2[0] = 0;
				comprobarResultadosIntroducidos();
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
		});
		edts22 = (EditText) findViewById(R.id.edtset22);
		edts22.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				edts22.setText("");
				edts23.setText("");
				return false;
			}
		});
		edts22.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				if(edts22.getText().toString().length()>0)resJ2[1] = Integer.parseInt(edts22.getText().toString());
				else resJ2[1] = 0;
				comprobarResultadosIntroducidos();
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
		});
		edts23 = (EditText) findViewById(R.id.edtset23);
		edts23.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				edts23.setText("");
				return false;
			}
		});
		edts23.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				if(edts23.getText().toString().length()>0)resJ2[2] = Integer.parseInt(edts23.getText().toString());
				else{
					resJ2[2] = 0;
				}
				comprobarResultadosIntroducidos();
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
		});
		
		btnSeleccionarFecha = (ImageButton) findViewById(R.id.btnSeleccionarFechaPartido);
		btnSeleccionarFecha.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(0);
			}
		});
		edtDuracion = (EditText) findViewById(R.id.edtDuracionPartido);
		
		final Calendar _cal = Calendar.getInstance();
		_anio = _cal.get(Calendar.YEAR);
		_mes = _cal.get(Calendar.MONTH);
		_dia = _cal.get(Calendar.DAY_OF_MONTH);
		updateDisplay();
		
		try{
			id_jugadores = PantallaInicial.bd.obtenerJugadoresPartido(id_partido);
			txtJ1.setText(PantallaInicial.bd.obtenerNombreJugador(id_jugadores[0]));
			txtJ2.setText(PantallaInicial.bd.obtenerNombreJugador(id_jugadores[1]));
			
			resJ1 = PantallaInicial.bd.obtenerResultadoTenis(id_partido, id_jugadores[0]);
			resJ2 = PantallaInicial.bd.obtenerResultadoTenis(id_partido, id_jugadores[1]);
			
			edts11.setText(resJ1[0]+"");
			edts12.setText(resJ1[1]+"");
			edts13.setText(resJ1[2]+"");
			
			edts21.setText(resJ2[0]+"");
			edts22.setText(resJ2[1]+"");
			edts23.setText(resJ2[2]+"");
			cargando = false;
			
			partido = PantallaInicial.bd.obtenerPartido(id_partido);
			
			//Se comprueba para cada control, si existen datos en la BD para cargarlos al iniciar la actividad
			if(partido.getFec().length()>0){
				txtFecha.setText(partido.getFec());
			}
			if(partido.getHor().length()>0){
				for(int i=0; i<spnHora.getCount(); i++){
					if(partido.getHor().equals(spnHora.getItemAtPosition(i))) spnHora.setSelection(i);
				}
			}
			
			edtDuracion.setText(PantallaInicial.bd.obtenerDuracionPartidoTenis(id_partido)+"");
		}catch(Exception e){
			Toast.makeText(this, getString(R.string.BDNoAbierta), Toast.LENGTH_SHORT).show();
		}
	}
	
	public void onResume(){
		super.onResume();
	}
	
	//Este método almacena los datos del partido
	public void btnGuardar_Click(View v){
		if(comprobarResultadosGuardar()){
			if(edts13.getVisibility() == View.INVISIBLE) resJ1[2] = 0;
			if(edts23.getVisibility() == View.INVISIBLE) resJ2[2] = 0;
			PantallaInicial.bd.guardarResultadoTenis(id_partido, id_jugadores[0], resJ1[0], resJ1[1], resJ1[2]);
			PantallaInicial.bd.guardarResultadoTenis(id_partido, id_jugadores[1], resJ2[0], resJ2[1], resJ2[2]);
			PantallaInicial.bd.guardarDatosPartidoTenis(id_partido, txtFecha.getText().toString(), hora);
			if(edtDuracion.getText().toString().length()==0) duracion = 0;
			else duracion = Integer.parseInt(edtDuracion.getText().toString());
			
			PantallaInicial.bd.insertarMinutosJugadorTenis(id_partido, id_jugadores[0], duracion);
			PantallaInicial.bd.insertarMinutosJugadorTenis(id_partido, id_jugadores[1], duracion);
			
			Toast.makeText(this, getString(R.string.BDInserta), Toast.LENGTH_SHORT).show();
			finish();
		}else{
			Toast.makeText(this, getString(R.string.resultadoNoCorrecto), Toast.LENGTH_SHORT).show();
		}
	}
	
	//Este método comprueba los resultados que se van introduciendo para habilitar o deshabilitar el tercer set
	private void comprobarResultadosIntroducidos(){
		int comprobar1 = 0, comprobar2 = 0;
		if((resJ1[0] == 6 && (resJ2[0] <= resJ1[0]-2))||
		   (resJ1[0] == 7 && ((resJ2[0] == 6)||(resJ2[0] == 5)))){
			comprobar1++;
		}
		if((resJ1[1] == 6 && (resJ2[1] <= resJ1[1]-2))||
		   (resJ1[1] == 7 && ((resJ2[1] == 6)||(resJ2[1] == 5)))){
			comprobar1++;
		}
		if((resJ2[0] == 6 && (resJ1[0] <= resJ2[0]-2))||
		   (resJ2[0] == 7 && ((resJ1[0] == 6)||(resJ1[0] == 5)))){
			comprobar2++;
		}
		if((resJ2[1] == 6 && (resJ1[1] <= resJ2[1]-2))||
		   (resJ2[1] == 7 && ((resJ1[1] == 6)||(resJ1[1] == 5)))){
			comprobar2++;
		}
		if(comprobar1==2 || comprobar2==2){
			edts13.setVisibility(View.INVISIBLE);
			edts23.setVisibility(View.INVISIBLE);
			if(!cargando){
				resJ1[2] = 0;
				resJ2[2] = 0;
			}
		}else{
			edts13.setVisibility(View.VISIBLE);
			edts23.setVisibility(View.VISIBLE);
			if(!cargando){
				resJ1[2] = 0;
				resJ2[2] = 0;
			}
		}
	}
	
	//Este método comprueba que los resultados introducidos tengan la forma correcta antes de guardarlos
	private boolean comprobarResultadosGuardar(){
		int comprobar=0; //Tiene que llegar a 2 o 3, pues se comprueban los tres sets.
		if(edts11.getText().toString().length()>0)resJ1[0] = Integer.parseInt(edts11.getText().toString());
		else resJ1[0] = 0;
		if(edts12.getText().toString().length()>0)resJ1[1] = Integer.parseInt(edts12.getText().toString());
		else resJ1[1] = 0;
		if(edts13.getText().toString().length()>0)resJ1[2] = Integer.parseInt(edts13.getText().toString());
		else resJ1[2] = 0;
		if(edts21.getText().toString().length()>0)resJ2[0] = Integer.parseInt(edts21.getText().toString());
		else resJ2[0] = 0;
		if(edts22.getText().toString().length()>0)resJ2[1] = Integer.parseInt(edts22.getText().toString());
		else resJ2[1] = 0;
		if(edts23.getText().toString().length()>0)resJ2[2] = Integer.parseInt(edts23.getText().toString());
		else resJ2[2] = 0;
		
		//Se comprueban los Sets
		if(resJ1[0]>7 || resJ1[1]>7 || resJ1[2]>7 || resJ2[0]>7 || resJ2[1]>7 || resJ2[2]>7) return false;
		if((resJ1[0]==7 && resJ2[0]<5) || (resJ1[1]==7 && resJ2[1]<5) || (resJ1[2]==7 && resJ2[2]<5) || 
		   (resJ2[0]==7 && resJ1[0]<5) || (resJ2[1]==7 && resJ1[1]<5) || (resJ2[2]==7 && resJ1[2]<5) ||
		   (resJ1[0]<=6 && resJ2[0]==resJ1[0]-1) || (resJ1[1]<=6 && resJ2[1]==resJ1[1]-1) || (resJ1[2]<=6 && resJ2[2]==resJ1[2]-1)) return false;
		if((resJ1[0]==7 && (resJ2[0]==6 ||(resJ2[0]<=resJ1[0]-2))) || (resJ1[0]==6 && resJ2[0]<=4)) comprobar++;
		if((resJ1[1]==7 && (resJ2[1]==6 ||(resJ2[1]<=resJ1[1]-2))) || (resJ1[1]==6 && resJ2[1]<=4)) comprobar++;
		if(comprobar == 2) comprobar++;
		else if((resJ1[2]==7 && (resJ2[2]==6 ||(resJ2[2]<=resJ1[2]-2))) || (resJ1[2]==6 && resJ2[2]<=4)) comprobar++;
		if(comprobar>=2) return true;
		
		comprobar = 0;
		if(resJ2[0]>7 || resJ2[1]>7 || resJ2[2]>7 || resJ1[0]>7 || resJ1[1]>7 || resJ1[2]>7) return false;
		if((resJ2[0]==7 && resJ1[0]<5) || (resJ2[1]==7 && resJ1[1]<5) || (resJ2[2]==7 && resJ1[2]<5) || 
		   (resJ1[0]==7 && resJ2[0]<5) || (resJ1[1]==7 && resJ2[1]<5) || (resJ1[2]==7 && resJ2[2]<5) ||
		   (resJ2[0]<=6 && resJ1[0]==resJ2[0]-1) || (resJ2[1]<=6 && resJ1[1]==resJ2[1]-1) || (resJ2[2]<=6 && resJ1[2]==resJ2[2]-1)) return false;
		if((resJ2[0]==7 && (resJ1[0]==6 ||(resJ1[0]<=resJ2[0]-2))) || (resJ2[0]==6 && resJ1[0]<=4)) comprobar++;
		if((resJ2[1]==7 && (resJ1[1]==6 ||(resJ1[1]<=resJ2[1]-2))) || (resJ2[1]==6 && resJ1[1]<=4)) comprobar++;
		if(comprobar == 2) comprobar++;
		else if((resJ2[2]==7 && (resJ1[2]==6 ||(resJ1[2]<=resJ2[2]-2))) || (resJ2[2]==6 && resJ1[2]<=4)) comprobar++;
		if(comprobar>=2) return true;
				
		return false;
	}
	
	private void updateDisplay(){
		SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			txtFecha.setText(spf.format(spf.parse(new StringBuilder().append(_dia).append("/").append(_mes + 1).append("/").append(_anio).toString())));
		} catch (ParseException e) {e.printStackTrace();}
	}
	
	private DatePickerDialog.OnDateSetListener _dateListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			_anio = year;
			_mes = monthOfYear;
			_dia = dayOfMonth;
			updateDisplay();
		}
	};
	
	protected Dialog onCreateDialog(int id){
		switch(id){
			case 0:
				return new DatePickerDialog(this, _dateListener, _anio, _mes, _dia);
		}
		return null;
	}
}
