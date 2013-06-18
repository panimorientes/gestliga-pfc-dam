//Esta es la pestaña de Baloncesto de la parte de gestión

package fragments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import tipos.Deporte;
import tipos.Equipo;
import tipos.Jugador;
import tipos.PosBaloncesto;
import tipos.Torneo;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import baloncesto.PantallaGestionTorneoBaloncesto;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.gestliga.PantallaInicial;
import com.example.gestliga.R;

public class BaloncestoFragment extends SherlockFragment implements OnItemSelectedListener, OnDateSetListener {

	private Spinner spnMenu, spnOrdenar, spnPosicionJugador, spnEquipoJugador,
			spnModPosicionJugador, spnModEquipoJugador;
	private TableLayout tablaTorneos, tablaEquipos, tablaJugadores,
			tablaTorneosContent, tablaEquiposContent, tablaJugadoresContent;
	private Button btnCrear;
	private EditText edtNombreTorneo, edtLugarTorneo, edtModNombreTorneo,
			edtModLugarTorneo, edtNombreEquipo, edtCiudadEquipo,
			edtEstadioEquipo, edtFechaFundacionEquipo, edtNumSociosEquipo,
			edtModNombreEquipo, edtModCiudadEquipo, edtModEstadioEquipo,
			edtModFechaFundacionEquipo, edtModNumSociosEquipo,
			edtNombreJugador, edtApodoJugador, edtModNombreJugador,
			edtModApodoJugador;

	private TextView txtFechaInicioTorneo, txtFechaFinTorneo,
			txtVerNombreTorneo, txtVerLugarTorneo, txtVerNumEquipos,
			txtVerFechaI, txtVerFechaF, txtVerDeporte, txtModFechaITorneo,
			txtModFechaFTorneo, txtVerNombreEquipo, txtVerCiudadEquipo,
			txtVerEstadioEquipo, txtVerFechaFundacionEquipo,
			txtVerNumSociosEquipo, txtVerDeporteEquipo, txtVerNombreJugador,
			txtVerApodoJugador, txtVerFechaNacJugador, txtVerEquipoJugador,
			txtVerPosicionJugador, txtFechaNacJugador, txtModFechaNacJugador;

	private ImageButton btnSeleccionarFechaInicio, btnSeleccionarFechaFin,
			btnSeleccionarFechaInicioMod, btnSeleccionarFechaFinMod,
			btnSeleccionarFechaNacJugador, btnSeleccionarFechaNacJugadorMod;

	private int fecha;
	private String tituloDialogo;

	private String nombreTorneo, lugarTorneo, fechaInicio, fechaFin,
			orden = "", nombreEquipo, ciudadEquipo, estadioEquipo,
			nombreJugador, apodoJugador, fechaNacJugador;
	private int fechaFundacionEquipo, numSociosEquipo, posicionJugador,
			equipoJugador;
	int id_torneo, deporte, id_torneo_modificar, id_equipo,
			id_equipo_modificar, id_jugador, id_jugador_modificar;
	ArrayList<Torneo> lista_torneos = new ArrayList<Torneo>();
	ArrayList<Deporte> lista_deportes = new ArrayList<Deporte>();
	ArrayList<Equipo> lista_equipos = new ArrayList<Equipo>();
	ArrayList<Jugador> lista_jugadores = new ArrayList<Jugador>();
	ArrayList<PosBaloncesto> lista_posiciones = new ArrayList<PosBaloncesto>();
	Torneo torneo;
	Equipo equipo;
	Jugador jugador;
	LinearLayout ll;
	Bundle b = new Bundle();
	private View v;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		try{
			v = inflater.inflate(R.layout.baloncesto_fragment, container, false);
			//Se asignan las referencias de las vistas en el layout
			spnOrdenar = (Spinner) v.findViewById(R.id.spnOrdenarPor_BC);
			spnMenu = (Spinner) v.findViewById(R.id.spnMenuBC);
			ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(v.getContext(), android.R.layout.simple_spinner_item) {
				public View getView(int position, View convertView, ViewGroup parent) {
					View v = super.getView(position, convertView, parent);
	
					((TextView) v).setTextSize(25);
					((TextView) v).setTextColor(Color.parseColor(getString(R.color.negro)));
					return v;
				}
	
				public View getDropDownView(int position, View convertView, ViewGroup parent) {
					View v = super.getDropDownView(position, convertView, parent);
	
					((TextView) v).setTextSize(25);
					((TextView) v).setTextColor(Color.parseColor(getString(R.color.negro)));
	
					return v;
				}
			};
			adapter.add(getString(R.string.torneos));
			adapter.add(getString(R.string.equipos));
			adapter.add(getString(R.string.jugadores));
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spnMenu.setAdapter(adapter);
			spnMenu.setOnItemSelectedListener(this);
	
			tablaTorneos = (TableLayout) v.findViewById(R.id.tablaTorneosBC);
			tablaEquipos = (TableLayout) v.findViewById(R.id.tablaEquiposBC);
			tablaJugadores = (TableLayout) v.findViewById(R.id.tablaJugadoresBC);
			tablaTorneosContent = (TableLayout) v.findViewById(R.id.tablaTorneosContentBC);
			tablaEquiposContent = (TableLayout) v.findViewById(R.id.tablaEquiposContentBC);
			tablaJugadoresContent = (TableLayout) v.findViewById(R.id.tablaJugadoresContentBC);
			
			btnCrear = (Button) v.findViewById(R.id.btnCrearBC);
			btnCrear.setOnTouchListener(new OnTouchListener() {
	
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						btnCrear.setBackgroundColor(Color.parseColor(getString(R.color.fondo_botones_pressed)));
					} else {
						btnCrear.setBackgroundColor(Color.parseColor(getString(R.color.fondo_botones)));
					}
					return false;
				}
			});
			btnCrear.setOnClickListener(new OnClickListener() {
	
				@Override
				public void onClick(View v) {
					if (v != null) {
						if(spnMenu.getSelectedItem().toString().equals(getString(R.string.torneos))) crearTorneo(v);
						else if(spnMenu.getSelectedItem().toString().equals(getString(R.string.equipos))) crearEquipo(v);
						else if(spnMenu.getSelectedItem().toString().equals(getString(R.string.jugadores))) crearJugador(v);
					}
				}
			});
		}catch(Exception e){
			Toast.makeText(getActivity(), getString(R.string.BDNoAbierta), Toast.LENGTH_SHORT).show();
		}
		return v;
	}

	//Clase privada para el manejo de fechas
	public static class DatePickerDialogFragment extends DialogFragment {
        private OnDateSetListener mFragment;

        public DatePickerDialogFragment(OnDateSetListener callback) {
            mFragment = callback;
        }

        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int cmonth = c.get(Calendar.MONTH);
            int cday = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), mFragment, year, cmonth, cday);
        }
    }
	
	//Método para mostrar la fecha en los TextViews
	private void setDateEditText(Date d) throws ParseException {
        java.text.DateFormat df = DateFormat.getDateFormat(getActivity());
        if(fecha == 1){
        	txtFechaInicioTorneo.setText(df.format(d));
        	fechaInicio = txtFechaInicioTorneo.getText().toString();
        }
        else if(fecha == 2){
        	txtFechaFinTorneo.setText(df.format(d));
        	fechaFin = txtFechaFinTorneo.getText().toString();
        }
        else if(fecha == 3){
        	txtModFechaITorneo.setText(df.format(d));
        	fechaInicio = txtModFechaITorneo.getText().toString();
        	if(comprobarFechas(fechaInicio, fechaFin)==false){
				Toast.makeText(getActivity(), getString(R.string.fechasNoCorrectas), Toast.LENGTH_SHORT).show();
			}
        }
        else if(fecha == 4){
        	txtModFechaFTorneo.setText(df.format(d));
        	fechaFin = txtModFechaFTorneo.getText().toString();
        	if(comprobarFechas(fechaInicio, fechaFin)==false){
				Toast.makeText(getActivity(), getString(R.string.fechasNoCorrectas), Toast.LENGTH_SHORT).show();
			}
        }
        else if(fecha == 5){
        	txtFechaNacJugador.setText(df.format(d));
        	fechaNacJugador = txtFechaNacJugador.getText().toString();
        }
        else if(fecha == 6){
        	txtModFechaNacJugador.setText(df.format(d));
        	fechaNacJugador = txtModFechaNacJugador.getText().toString();
        }
    }
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Date d = new Date(year - 1900, month, day);
        try {
			setDateEditText(d);
		} catch (ParseException e) {}
    }
    private boolean comprobarFechas(String fechaI, String fechaF) throws ParseException{
    	SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
    	Date dI = spf.parse(fechaI);
    	Date dF = spf.parse(fechaF);
    	if(dI.after(dF)) return false;
    	else return true;
    }
    
    //Método para el evento Click del Spinner principal
    @Override
	public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
		if (parent.getItemAtPosition(position).toString().equals(getString(R.string.torneos))) {
			orden = "";
			tablaTorneos.setVisibility(View.VISIBLE);
			tablaTorneosContent.setVisibility(View.VISIBLE);
			tablaEquipos.setVisibility(View.GONE);
			tablaEquiposContent.setVisibility(View.GONE);
			tablaJugadores.setVisibility(View.GONE);
			tablaJugadoresContent.setVisibility(View.GONE);
			tituloDialogo = getString(R.string.tituloTorneo);
			//Se define en el adaptador el tamaño y color del texto del Spinner
			ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getSherlockActivity(), android.R.layout.simple_spinner_item) {
				public View getView(int position, View convertView, ViewGroup parent) {
					View v = super.getView(position, convertView, parent);
					((TextView) v).setTextSize(20);
					((TextView) v).setTextColor(Color.parseColor(getString(R.color.negro)));
					return v;
				}
				public View getDropDownView(int position, View convertView, ViewGroup parent) {
					View v = super.getDropDownView(position, convertView, parent);
					((TextView) v).setTextSize(20);
					((TextView) v).setTextColor(Color.parseColor(getString(R.color.negro)));
					return v;
				}
			};
			adapter.add(getString(R.string.elijaFiltro));
			adapter.add(getString(R.string.nombre));
			adapter.add(getString(R.string.numEquipos));
			adapter.add(getString(R.string.fechaInicioLinea));
			adapter.add(getString(R.string.fechaFinLinea));
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spnOrdenar.setAdapter(adapter);
			spnOrdenar.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
					if(parent.getItemAtPosition(position).toString().equals(getString(R.string.nombre))) orden = "tor_nom";
					else if(parent.getItemAtPosition(position).toString().equals(getString(R.string.numEquipos))) orden = "numEquipos";
					else if(parent.getItemAtPosition(position).toString().equals(getString(R.string.fechaInicioLinea))) orden = "tor_fin";
					else if(parent.getItemAtPosition(position).toString().equals(getString(R.string.fechaFinLinea))) orden = "tor_ffi";
					else orden = "";
					construirTablaTorneos();
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {}
			});
			construirTablaTorneos();
		} else if (parent.getItemAtPosition(position).toString().equals(getString(R.string.equipos))) {
			orden = "";
			tablaTorneos.setVisibility(View.GONE);
			tablaTorneosContent.setVisibility(View.GONE);
			tablaEquipos.setVisibility(View.VISIBLE);
			tablaEquiposContent.setVisibility(View.VISIBLE);
			tablaJugadores.setVisibility(View.GONE);
			tablaJugadoresContent.setVisibility(View.GONE);
			tituloDialogo = getString(R.string.tituloEquipo);
			//Se define en el adaptador el tamaño y color del texto del Spinner
			ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getSherlockActivity(), android.R.layout.simple_spinner_item) {
				public View getView(int position, View convertView, ViewGroup parent) {
					View v = super.getView(position, convertView, parent);
								((TextView) v).setTextSize(20);
					((TextView) v).setTextColor(Color.parseColor(getString(R.color.negro)));
					return v;
				}
							public View getDropDownView(int position, View convertView,
						ViewGroup parent) {
					View v = super.getDropDownView(position, convertView, parent);
								((TextView) v).setTextSize(20);
					((TextView) v).setTextColor(Color.parseColor(getString(R.color.negro)));
								return v;
				}
			};
			adapter.add(getString(R.string.elijaFiltro));
			adapter.add(getString(R.string.nombre));
			adapter.add(getString(R.string.numJugadores));
			adapter.add(getString(R.string.numSocios));
			adapter.add(getString(R.string.ciudad));
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spnOrdenar.setAdapter(adapter);
			spnOrdenar.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
					if(parent.getItemAtPosition(position).toString().equals(getString(R.string.nombre))) orden = "equ_nom";
					else if(parent.getItemAtPosition(position).toString().equals(getString(R.string.numJugadores))) orden = "numJugadores DESC";
					else if(parent.getItemAtPosition(position).toString().equals(getString(R.string.numSocios))) orden = "equ_soc DESC";
					else if(parent.getItemAtPosition(position).toString().equals(getString(R.string.ciudad))) orden = "equ_ciu";
					else orden = "";
					construirTablaEquipos();
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {}
			});
			construirTablaEquipos();
		} else if (parent.getItemAtPosition(position).toString().equals(getString(R.string.jugadores))) {
			orden = "";
			tablaTorneos.setVisibility(View.GONE);
			tablaTorneosContent.setVisibility(View.GONE);
			tablaEquipos.setVisibility(View.GONE);
			tablaEquiposContent.setVisibility(View.GONE);
			tablaJugadores.setVisibility(View.VISIBLE);
			tablaJugadoresContent.setVisibility(View.VISIBLE);
			tituloDialogo = getString(R.string.tituloJugador);
			//Se define en el adaptador el tamaño y color del texto del Spinner
			ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getSherlockActivity(), android.R.layout.simple_spinner_item) {
				public View getView(int position, View convertView, ViewGroup parent) {
					View v = super.getView(position, convertView, parent);
								((TextView) v).setTextSize(20);
					((TextView) v).setTextColor(Color.parseColor(getString(R.color.negro)));
					return v;
				}
							public View getDropDownView(int position, View convertView,
						ViewGroup parent) {
					View v = super.getDropDownView(position, convertView, parent);
								((TextView) v).setTextSize(20);
					((TextView) v).setTextColor(Color.parseColor(getString(R.color.negro)));
								return v;
				}
			};
			adapter.add(getString(R.string.elijaFiltro));
			adapter.add(getString(R.string.nombre));
			adapter.add(getString(R.string.apodo));
			adapter.add(getString(R.string.edad));
			adapter.add(getString(R.string.posicionSinPuntos));
			adapter.add(getString(R.string.equipo));
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spnOrdenar.setAdapter(adapter);
			spnOrdenar.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
					if(parent.getItemAtPosition(position).toString().equals(getString(R.string.nombre))) orden = "jug_nom";
					else if(parent.getItemAtPosition(position).toString().equals(getString(R.string.apodo))) orden = "jug_apo";
					else if(parent.getItemAtPosition(position).toString().equals(getString(R.string.edad))) orden = "jug_fna";
					else if(parent.getItemAtPosition(position).toString().equals(getString(R.string.posicionSinPuntos))) orden = "jug_pos";
					else if(parent.getItemAtPosition(position).toString().equals(getString(R.string.equipo))) orden = "jug_equ";
					else orden = "";
					construirTablaJugadores();
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {}
			});
			construirTablaJugadores();
		}
		
	}    
    
    @Override
	public void onNothingSelected(AdapterView<?> arg0) {}
    
    //Método para crear un nuevo torneo
    private void crearTorneo(View v){
    	//Mostraremos el formulario para crear el torneo dentro de un diálogo, por ello, primero "inflamos" el layout
    	//creado para la creación de un torneo de baloncesto, y agregamos las referencias de las vistas con el layout
    	LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.baloncesto_torneo_crear, null);
		edtNombreTorneo = (EditText) ll.findViewById(R.id.edtBCNombreTorneo);
		edtNombreTorneo.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(edtNombreTorneo.getText().toString().equals(getString(R.string.nombreTorneo)))
					edtNombreTorneo.setText("");
				return false;
			}
		});
		nombreTorneo = edtNombreTorneo.getText().toString();
		edtNombreTorneo.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void afterTextChanged(Editable s) {
				nombreTorneo = edtNombreTorneo.getText().toString();
			}
		});
		
		edtLugarTorneo = (EditText) ll.findViewById(R.id.edtBCLugarTorneo);
		edtLugarTorneo.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(edtLugarTorneo.getText().toString().equals(getString(R.string.lugarTorneo)))
					edtLugarTorneo.setText("");
				return false;
			}
		});
		lugarTorneo = edtLugarTorneo.getText().toString();
		edtLugarTorneo.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void afterTextChanged(Editable s) {
				lugarTorneo = edtLugarTorneo.getText().toString();
			}
		});
		
		txtFechaInicioTorneo = (TextView) ll.findViewById(R.id.txtBCFechaInicioTorneo);
		txtFechaFinTorneo = (TextView) ll.findViewById(R.id.txtBCFechaFinTorneo);
		btnSeleccionarFechaInicio = (ImageButton) ll.findViewById(R.id.btnBCFechaInicio);
		btnSeleccionarFechaInicio.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	fecha = 1; //Para saber donde cargarlo en setDateEditText
	        	FragmentTransaction ft = getFragmentManager().beginTransaction();
                DialogFragment newFragment = new DatePickerDialogFragment(BaloncestoFragment.this);
                newFragment.show(ft, "date_picker_dialog");
	        }

	    });
		btnSeleccionarFechaFin = (ImageButton) ll.findViewById(R.id.btnBCFechaFin);
		btnSeleccionarFechaFin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fecha = 2; //Para saber donde cargarlo en setDateEditText
				FragmentTransaction ft = getFragmentManager().beginTransaction();
                DialogFragment newFragment = new DatePickerDialogFragment(BaloncestoFragment.this);
                newFragment.show(ft, "date_picker_dialog");
			}
		});
		
		AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
		builder.setTitle(tituloDialogo);
		builder.setView(ll);
		
		//Una vez que se ha asignado el layout al diálogo, establecemos el comportamiento de los botones
		builder.setPositiveButton(getString(R.string.crear),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//Se comprueba que están todos los datos rellenos, y que las fechas son correctas
						boolean guardar = true;
						String rellena = getString(R.string.rellena);
						if(nombreTorneo.trim().equals("") || nombreTorneo.trim().equals(getString(R.string.nombreTorneo))){
							guardar = false;
							rellena = rellena + getString(R.string.nombreTorneoRellena);
						}
						if(lugarTorneo.trim().equals("") || lugarTorneo.trim().equals(getString(R.string.lugarTorneo))){
							guardar = false;
							rellena = rellena + getString(R.string.lugarTorneoRellena);
						}
						if(txtFechaInicioTorneo.getText().toString().equals(getString(R.string.fechaInicioLinea))){
							guardar = false;
							rellena = rellena + getString(R.string.fechaInicioRellena);
						}
						if(txtFechaFinTorneo.getText().toString().equals(getString(R.string.fechaFinLinea))){
							guardar = false;
							rellena = rellena + getString(R.string.fechaFinRellena);
						}
						if(guardar == true){
							try {
								if(comprobarFechas(fechaInicio, fechaFin)==false){
									guardar = false;
									rellena = rellena + getString(R.string.fechasNoCorrectas);
								}
							} catch (ParseException e) {e.printStackTrace();}
						}
						
						if(guardar == false){
							Toast.makeText(getActivity(), rellena, Toast.LENGTH_LONG).show();
							guardar = true;
						}else{
							id_torneo = PantallaInicial.bd.obtenerIDTorneoMAX();
							Torneo t = new Torneo(id_torneo, nombreTorneo, lugarTorneo, fechaInicio, fechaFin, 3, 0);
							if(PantallaInicial.bd.crearTorneo(t)==1){
								Toast.makeText(getActivity(), getString(R.string.torneoCreado), Toast.LENGTH_SHORT).show();
								construirTablaTorneos();
								dialog.dismiss();
							}else{
								Toast.makeText(getActivity(), getString(R.string.torneoNoCreado), Toast.LENGTH_SHORT).show();
							}
						}
					}
				});
		builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		builder.create().show();
    }
    
    //Este método sirve para mostrar la tabla de todos los torneos de baloncesto existentes en la BD
    private void construirTablaTorneos(){
    	tablaTorneosContent.removeAllViews();
    	int position = 0;
    	lista_torneos = PantallaInicial.bd.obtenerTorneos(orden, 3);
    	Iterator<Torneo> it = lista_torneos.iterator();
    	while(it.hasNext()){
    		//Para cada torneo se van creando filas. Para ello se "infla" el layout creado para tal fin
    		Torneo t = it.next();
    		LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		TableRow fila = (TableRow) inflater.inflate(R.layout.baloncesto_torneos_table_row, null);
    		TextView nombre = (TextView) fila.findViewById(R.id.txtNombreBCTorneo);
    		nombre.setText(t.getNom());
    		TextView numEquipos = (TextView) fila.findViewById(R.id.txtEquiposBCTorneo);
    		numEquipos.setText(t.getNumEquipos()+"");
    		TextView fechaI = (TextView) fila.findViewById(R.id.txtFechaInicioBCTorneos);
    		fechaI.setText(t.getFin());
    		TextView fechaF = (TextView) fila.findViewById(R.id.txtFechaFinBCTorneos);
    		fechaF.setText(t.getFfi());
    		//Este ImageButton sirve para ver los datos del torneo
    		ImageButton btnVer = (ImageButton) fila.findViewById(R.id.trowImgVer);
    		btnVer.setTag(position);
    		btnVer.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View vi) {
					if (vi != null) {
						//Los datos se verán en un diálogo, por lo que se "infla" el layout creado para tal fin
						int tag = Integer.parseInt(vi.getTag()+"");
						Torneo t = lista_torneos.get(tag);
						LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.baloncesto_torneo_ver, null);
						txtVerNombreTorneo = (TextView) ll.findViewById(R.id.txtVerTorneoNombreBC);
						txtVerNombreTorneo.setText(t.getNom());
						
						txtVerLugarTorneo = (TextView) ll.findViewById(R.id.txtVerTorneoLugarBC);
						txtVerLugarTorneo.setText(t.getLug());
						
						txtVerNumEquipos = (TextView) ll.findViewById(R.id.txtVerTorneoNumEquiposBC);
						txtVerNumEquipos.setText(t.getNumEquipos()+"");
						
						txtVerFechaI = (TextView) ll.findViewById(R.id.txtVerTorneoFechaIBC);
						txtVerFechaI.setText(t.getFin());
						
						txtVerFechaF = (TextView) ll.findViewById(R.id.txtVerTorneoFechaFBC);
						txtVerFechaF.setText(t.getFfi());
						
						txtVerDeporte = (TextView) ll.findViewById(R.id.txtVerTorneoDeporteBC);
						txtVerDeporte.setText(PantallaInicial.bd.obtenerDeporte(t.getDep()));
						
						ArrayList<Integer> lista = PantallaInicial.bd.obtenerIDEquiposDeTorneo(t.getId());
						Iterator<Integer> aux = lista.iterator();
						LinearLayout llEquipos = (LinearLayout) ll.findViewById(R.id.llBCEquiposInscritosTorneo);
						int n = 1;
						while(aux.hasNext()){
							TextView txt = new TextView(getActivity().getBaseContext());
							txt.setText(n + ". " + PantallaInicial.bd.obtenerNombreEquipo(aux.next()));
							txt.setTextSize(18);
							txt.setTextColor(Color.parseColor(getString(R.color.blanco)));
							llEquipos.addView(txt);
							n++;
						}
						
						//Se asigna al diálogo la vista
						AlertDialog.Builder builder = new AlertDialog.Builder(vi.getContext());
						builder.setTitle(t.getNom());
						builder.setView(ll);
						
						//Se establece el comportamiento de los botones
						builder.setNegativeButton(getString(R.string.volver), new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,int which) {
										dialog.cancel();
									}
								});
						builder.create().show();
					}
				}
			});
    		btnVer.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if(event.getAction() == MotionEvent.ACTION_DOWN) v.setBackgroundResource(R.color.fondo_boton_pressed);
					else v.setBackgroundResource(R.color.blanco);
					return false;
				}
			});
    		//Con este ImageButton podemos modificar los datos de un partido. La forma de proceder es igual a btnVer
    		ImageButton btnModificar = (ImageButton) fila.findViewById(R.id.trowImgEditar);
    		btnModificar.setTag(position);
    		btnModificar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (v != null) {
						int tag = Integer.parseInt(v.getTag()+"");
						torneo = lista_torneos.get(tag);
						id_torneo_modificar = torneo.getId(); //Para pasarle a la función de modificar más abajo
						LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						ll = (LinearLayout) inflater.inflate(R.layout.baloncesto_torneo_modificar, null);
						edtModNombreTorneo = (EditText) ll.findViewById(R.id.edtModTorneoNombreBC);
						edtModNombreTorneo.setText(torneo.getNom());
						nombreTorneo = edtModNombreTorneo.getText().toString();
						edtModNombreTorneo.addTextChangedListener(new TextWatcher() {
							@Override
							public void onTextChanged(CharSequence s, int start, int before, int count) {}
							@Override
							public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
							@Override
							public void afterTextChanged(Editable s) {
								nombreTorneo = edtModNombreTorneo.getText().toString();
							}
						});
						
						edtModLugarTorneo = (EditText) ll.findViewById(R.id.edtModTorneoLugarBC);
						edtModLugarTorneo.setText(torneo.getLug());
						lugarTorneo = edtModLugarTorneo.getText().toString();
						edtModLugarTorneo.addTextChangedListener(new TextWatcher() {
							@Override
							public void onTextChanged(CharSequence s, int start, int before, int count) {}
							@Override
							public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
							@Override
							public void afterTextChanged(Editable s) {
								lugarTorneo = edtModLugarTorneo.getText().toString();
							}
						});
						
						txtModFechaITorneo = (TextView) ll.findViewById(R.id.txtModTorneoFechaIBC);
						txtModFechaITorneo.setText(torneo.getFin());
						fechaInicio = torneo.getFin();
						txtModFechaFTorneo = (TextView) ll.findViewById(R.id.txtModTorneoFechaFBC);
						txtModFechaFTorneo.setText(torneo.getFfi());
						fechaFin = torneo.getFfi();
						btnSeleccionarFechaInicioMod = (ImageButton) ll.findViewById(R.id.btnModBCFechaInicio);
						btnSeleccionarFechaInicioMod.setOnClickListener(new View.OnClickListener() {
					        @Override
					        public void onClick(View v) {
					        	fecha = 3; //Para saber donde cargarlo en setDateEditText
					        	FragmentTransaction ft = getFragmentManager().beginTransaction();
				                DialogFragment newFragment = new DatePickerDialogFragment(BaloncestoFragment.this);
				                newFragment.show(ft, "date_picker_dialog");
					        }
					    });
						btnSeleccionarFechaFinMod = (ImageButton) ll.findViewById(R.id.btnModBCFechaFin);
						btnSeleccionarFechaFinMod.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								fecha = 4; //Para saber donde cargarlo en setDateEditText
								FragmentTransaction ft = getFragmentManager().beginTransaction();
				                DialogFragment newFragment = new DatePickerDialogFragment(BaloncestoFragment.this);
				                newFragment.show(ft, "date_picker_dialog");
							}
						});

						AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
						//builder.setTitle(torneo.getNom());
						builder.setView(ll);
						
						builder.setPositiveButton(getString(R.string.modificar), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								boolean guardar = true;
								String rellena = getString(R.string.rellena);
								if(nombreTorneo.trim().equals("") || nombreTorneo.trim().equals(getString(R.string.nombreTorneo))){
									guardar = false;
									rellena = rellena + getString(R.string.nombreTorneoRellena);
								}
								if(lugarTorneo.trim().equals("") || lugarTorneo.trim().equals(getString(R.string.lugarTorneo))){
									guardar = false;
									rellena = rellena + getString(R.string.lugarTorneoRellena);
								}
								if(txtModFechaITorneo.getText().toString().equals(getString(R.string.fechaInicioLinea))){
									guardar = false;
									rellena = rellena + getString(R.string.fechaInicioRellena);
								}
								if(txtModFechaFTorneo.getText().toString().equals(getString(R.string.fechaFinLinea))){
									guardar = false;
									rellena = rellena + getString(R.string.fechaFinRellena);
								}
								if(guardar == true){
									try {
										if(comprobarFechas(fechaInicio, fechaFin)==false){
											guardar = false;
											rellena = rellena + getString(R.string.fechasNoCorrectas);
										}
									} catch (ParseException e) {e.printStackTrace();}
								}
								
								if(guardar == false){
									Toast.makeText(getActivity(), rellena, Toast.LENGTH_LONG).show();
									guardar = true;
								}else{
									Torneo original = PantallaInicial.bd.obtenerTorneo(id_torneo_modificar);
									Torneo t = new Torneo(id_torneo_modificar, nombreTorneo, lugarTorneo, fechaInicio, fechaFin, original.getDep(), original.getNumEquipos());
									if(PantallaInicial.bd.modificarTorneo(t)==1){
										Toast.makeText(getActivity(), getString(R.string.BDModificado), Toast.LENGTH_SHORT).show();
										construirTablaTorneos();
										dialog.dismiss();
									}else{
										Toast.makeText(getActivity(), getString(R.string.BDNoModificado), Toast.LENGTH_SHORT).show();
									}
								}
							}
						});
						builder.setNegativeButton(getString(R.string.volver), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,int which) {
								dialog.cancel();
							}
						});
						AlertDialog alertDialog = builder.create();
						alertDialog.show();
						alertDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
						//builder.create().show();
					}
				}
			});
    		btnModificar.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if(event.getAction() == MotionEvent.ACTION_DOWN) v.setBackgroundResource(R.color.fondo_boton_pressed);
					else v.setBackgroundResource(R.color.blanco);
					return false;
				}
			});
    		//Presionando este ImageButton, eliminaremos de forma permanente el torneo de la BD (previa confirmación)
    		ImageButton btnEliminar = (ImageButton) fila.findViewById(R.id.trowImgBorrar);
    		btnEliminar.setTag(position);
    		btnEliminar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int tag = Integer.parseInt(v.getTag()+"");
					b.putInt("tag", tag);
					AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
					builder.setTitle(getString(R.string.eliminarDialogo) + " (" + lista_torneos.get(tag).getNom() + ")");
					builder.setMessage(getString(R.string.eliminarTorneoPregunta));
					builder.setPositiveButton(getString(R.string.si), new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									if(PantallaInicial.bd.eliminarTorneo(lista_torneos.get(b.getInt("tag")).getId())==1){
										Toast.makeText(getActivity(), getString(R.string.BDEliminado), Toast.LENGTH_SHORT).show();
										construirTablaTorneos();
										dialog.dismiss();
									}else{
										Toast.makeText(getActivity(), getString(R.string.BDNoEliminado), Toast.LENGTH_SHORT).show();
									}
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
			});
    		btnEliminar.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if(event.getAction() == MotionEvent.ACTION_DOWN) v.setBackgroundResource(R.color.fondo_boton_pressed);
					else v.setBackgroundResource(R.color.blanco);
					return false;
				}
			});
    		
    		//Con este ImageButton accederemos a la actividad que nos permite gestionar un torneo (equipos y calendario)
    		ImageButton btnGestionar = (ImageButton) fila.findViewById(R.id.trowImgGestionar);
    		btnGestionar.setTag(position);
    		btnGestionar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int tag = Integer.parseInt(v.getTag()+"");
					Intent intent = new Intent(getActivity(), PantallaGestionTorneoBaloncesto.class);
					intent.putExtra("id_torneo", lista_torneos.get(tag).getId());
					intent.putExtra("nombre", lista_torneos.get(tag).getNom());
					startActivity(intent);
				}
			});
    		btnGestionar.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if(event.getAction() == MotionEvent.ACTION_DOWN) v.setBackgroundResource(R.color.fondo_boton_pressed);
					else v.setBackgroundResource(R.color.blanco);
					return false;
				}
			});
    		
    		FrameLayout frm = new FrameLayout(getActivity());
			frm.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1));
			frm.setBackgroundColor(Color.parseColor("#222222"));
			
			tablaTorneosContent.addView(fila);
			if(it.hasNext()) tablaTorneosContent.addView(frm);
    		position++;
    	}
    }

    //Este método nos permite crear un nuevo equipo. La forma de proceder es similar al método crearTorneo
    private void crearEquipo(View v){
    	LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.baloncesto_equipo_crear, null);
		edtNombreEquipo = (EditText) ll.findViewById(R.id.edtBCNombreEquipo);
		edtNombreEquipo.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(edtNombreEquipo.getText().toString().equals(getString(R.string.nombreEquipoPuntos)))
					edtNombreEquipo.setText("");
				return false;
			}
		});
		nombreEquipo = edtNombreEquipo.getText().toString();
		edtNombreEquipo.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void afterTextChanged(Editable s) {
				nombreEquipo = edtNombreEquipo.getText().toString();
			}
		});
		
		edtCiudadEquipo = (EditText) ll.findViewById(R.id.edtBCCiudadEquipo);
		edtCiudadEquipo.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(edtCiudadEquipo.getText().toString().equals(getString(R.string.ciudadPuntos)))
					edtCiudadEquipo.setText("");
				return false;
			}
		});
		ciudadEquipo = edtCiudadEquipo.getText().toString();
		edtCiudadEquipo.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void afterTextChanged(Editable s) {
				ciudadEquipo = edtCiudadEquipo.getText().toString();
			}
		});
		
		edtEstadioEquipo = (EditText) ll.findViewById(R.id.edtBCEstadioEquipo);
		edtEstadioEquipo.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(edtEstadioEquipo.getText().toString().equals(getString(R.string.estadioPuntos)))
					edtEstadioEquipo.setText("");
				return false;
			}
		});
		estadioEquipo = edtEstadioEquipo.getText().toString();
		edtEstadioEquipo.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void afterTextChanged(Editable s) {
				estadioEquipo = edtEstadioEquipo.getText().toString();
			}
		});
		
		edtFechaFundacionEquipo = (EditText) ll.findViewById(R.id.edtBCFechaFundacionEquipo);
		edtFechaFundacionEquipo.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(edtFechaFundacionEquipo.getText().toString().equals(getString(R.string.fechaFundacionPuntos)))
					edtFechaFundacionEquipo.setText("");
				return false;
			}
		});
		
		fechaFundacionEquipo = 0;
		edtFechaFundacionEquipo.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void afterTextChanged(Editable s) {
				if(edtFechaFundacionEquipo.getText().toString().trim().length() == 0){
					fechaFundacionEquipo = 0;
				}
				else fechaFundacionEquipo = Integer.parseInt(edtFechaFundacionEquipo.getText().toString());
			}
		});
		
		edtNumSociosEquipo = (EditText) ll.findViewById(R.id.edtBCNumSociosEquipo);
		edtNumSociosEquipo.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(edtNumSociosEquipo.getText().toString().equals(getString(R.string.numSociosPuntos)))
					edtNumSociosEquipo.setText("");
				return false;
			}
		});
		
		numSociosEquipo = 0;
		edtNumSociosEquipo.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void afterTextChanged(Editable s) {
				if(edtNumSociosEquipo.getText().toString().trim().length() == 0) numSociosEquipo = 0;
				else numSociosEquipo = Integer.parseInt(edtNumSociosEquipo.getText().toString());
			}
		});
		
		AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
		builder.setTitle(tituloDialogo);
		builder.setView(ll);

		builder.setPositiveButton(getString(R.string.crear),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						boolean guardar = true;
						String rellena = getString(R.string.rellena);
						if(nombreEquipo.trim().equals("") || nombreEquipo.trim().equals(getString(R.string.nombreEquipoPuntos))){
							guardar = false;
							rellena = rellena + getString(R.string.nombreEquipoRellene);
						}
						if(ciudadEquipo.trim().equals("") || ciudadEquipo.trim().equals(getString(R.string.ciudadPuntos))){
							guardar = false;
							rellena = rellena + getString(R.string.ciudadEquipoRellene);
						}
						if(estadioEquipo.trim().equals("") || estadioEquipo.trim().equals(getString(R.string.estadioPuntos))){
							guardar = false;
							rellena = rellena + getString(R.string.estadioEquipoRellene);
						}
						if(edtFechaFundacionEquipo.getText().toString().trim().equals("") || edtFechaFundacionEquipo.getText().toString().equals(getString(R.string.fechaFundacionPuntos))){
							guardar = false;
							rellena = rellena + getString(R.string.fechaFundacionEquipoRellene);
						}else{
							if(edtFechaFundacionEquipo.getText().toString().trim().length()!=4){
								guardar = false;
								rellena = rellena + getString(R.string.fechaFundacionDigitosEquipoRellene);
							}
						}
						if(edtNumSociosEquipo.getText().toString().trim().equals("") || edtNumSociosEquipo.getText().toString().equals(getString(R.string.numSociosPuntos))){
							guardar = false;
							rellena = rellena + getString(R.string.numSociosEquipoRellene);
						}
						
						if(guardar == false){
							Toast.makeText(getActivity(), rellena, Toast.LENGTH_LONG).show();
							guardar = true;
						}else{
							id_equipo = PantallaInicial.bd.obtenerIDEquipoMAX();
							Equipo e = new Equipo(id_equipo, nombreEquipo, fechaFundacionEquipo, numSociosEquipo, estadioEquipo, 3, ciudadEquipo, 0);
							if(PantallaInicial.bd.crearEquipo(e)==1){
								Toast.makeText(getActivity(), getString(R.string.equipoCreado), Toast.LENGTH_SHORT).show();
								construirTablaEquipos();
								dialog.dismiss();
							}else{
								Toast.makeText(getActivity(), getString(R.string.equipoNoCreado), Toast.LENGTH_SHORT).show();
							}
						}
					}
				});
		builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						dialog.cancel();
					}
				});
		builder.create().show();
    }
    
    //Este método nos permite mostrar la tabla con todos los equipos existentes en la BD. La forma de proceder es similar a construirTablaTorneos
    private void construirTablaEquipos(){
    	tablaEquiposContent.removeAllViews();
    	int position = 0;
    	lista_equipos = PantallaInicial.bd.obtenerEquipos(orden,3);
    	Iterator<Equipo> it = lista_equipos.iterator();
    	while(it.hasNext()){
    		Equipo e = it.next();
    		if(e.getId()!=0){
	    		LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    		TableRow fila = (TableRow) inflater.inflate(R.layout.baloncesto_equipos_table_row, null);
	    		TextView nombre = (TextView) fila.findViewById(R.id.txtNombreBCEquipos);
	    		nombre.setText(e.getNom());
	    		TextView numJugadores = (TextView) fila.findViewById(R.id.txtNumJugadoresBCEquipos);
	    		numJugadores.setText(e.getNumJugadores()+"");
	    		TextView numSocios = (TextView) fila.findViewById(R.id.txtSociosBCEquipos);
	    		numSocios.setText(e.getSoc()+"");
	    		TextView ciudad = (TextView) fila.findViewById(R.id.txtCiudadBCEquipos);
	    		ciudad.setText(e.getCiu());
	    		ImageButton btnVer = (ImageButton) fila.findViewById(R.id.trowImgVerEquipos);
	    		btnVer.setTag(position);
	    		btnVer.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (v != null) {
							int tag = Integer.parseInt(v.getTag()+"");
							Equipo e = lista_equipos.get(tag);
							LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
							LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.baloncesto_equipos_ver, null);
							txtVerNombreEquipo = (TextView) ll.findViewById(R.id.txtVerEquipoNombreBC);
							txtVerNombreEquipo.setText(e.getNom());
							
							txtVerCiudadEquipo = (TextView) ll.findViewById(R.id.txtVerEquipoCiudadBC);
							txtVerCiudadEquipo.setText(e.getCiu());
							
							txtVerEstadioEquipo = (TextView) ll.findViewById(R.id.txtVerEquipoEstadioBC);
							txtVerEstadioEquipo.setText(e.getEst());
							
							txtVerFechaFundacionEquipo = (TextView) ll.findViewById(R.id.txtVerEquipoFechaFundacionBC);
							txtVerFechaFundacionEquipo.setText(e.getFfu()+"");
							
							txtVerNumSociosEquipo = (TextView) ll.findViewById(R.id.txtVerEquipoSociosBC);
							txtVerNumSociosEquipo.setText(e.getSoc()+"");
							
							txtVerDeporteEquipo = (TextView) ll.findViewById(R.id.txtVerEquipoDeporteBC);
							txtVerDeporteEquipo.setText(PantallaInicial.bd.obtenerDeporte(e.getDep()));
							
							AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
							//builder.setTitle(e.getNom());
							builder.setView(ll);
							
							builder.setNegativeButton(getString(R.string.volver), new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog,int which) {
											dialog.cancel();
										}
									});
							AlertDialog alertDialog = builder.create();
							alertDialog.show();
							alertDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
							//builder.create().show();
						}
					}
				});
	    		btnVer.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if(event.getAction() == MotionEvent.ACTION_DOWN) v.setBackgroundResource(R.color.fondo_boton_pressed);
						else v.setBackgroundResource(R.color.blanco);
						return false;
					}
				});
	    		
	    		ImageButton btnModificar = (ImageButton) fila.findViewById(R.id.trowImgEditarEquipos);
	    		btnModificar.setTag(position);
	    		btnModificar.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (v != null) {
							int tag = Integer.parseInt(v.getTag()+"");
							equipo = lista_equipos.get(tag);
							id_equipo_modificar = equipo.getId(); //Para pasarle a la función de modificar más abajo
							LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
							ll = (LinearLayout) inflater.inflate(R.layout.baloncesto_equipos_modificar, null);
							edtModNombreEquipo = (EditText) ll.findViewById(R.id.edtModEquipoNombreBC);
							edtModNombreEquipo.setText(equipo.getNom());
							nombreEquipo = edtModNombreEquipo.getText().toString();
							edtModNombreEquipo.addTextChangedListener(new TextWatcher() {
								@Override
								public void onTextChanged(CharSequence s, int start, int before, int count) {}
								@Override
								public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
								@Override
								public void afterTextChanged(Editable s) {
									nombreEquipo = edtModNombreEquipo.getText().toString();
								}
							});
							
							edtModCiudadEquipo = (EditText) ll.findViewById(R.id.edtModEquipoCiudadBC);
							edtModCiudadEquipo.setText(equipo.getCiu());
							ciudadEquipo = edtModCiudadEquipo.getText().toString();
							edtModCiudadEquipo.addTextChangedListener(new TextWatcher() {
								@Override
								public void onTextChanged(CharSequence s, int start, int before, int count) {}
								@Override
								public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
								@Override
								public void afterTextChanged(Editable s) {
									ciudadEquipo = edtModCiudadEquipo.getText().toString();
								}
							});
							
							edtModEstadioEquipo = (EditText) ll.findViewById(R.id.edtModEquipoEstadioBC);
							edtModEstadioEquipo.setText(equipo.getEst());
							estadioEquipo = edtModEstadioEquipo.getText().toString();
							edtModEstadioEquipo.addTextChangedListener(new TextWatcher() {
								@Override
								public void onTextChanged(CharSequence s, int start, int before, int count) {}
								@Override
								public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
								@Override
								public void afterTextChanged(Editable s) {
									estadioEquipo = edtModEstadioEquipo.getText().toString();
								}
							});
							
							edtModFechaFundacionEquipo = (EditText) ll.findViewById(R.id.edtModEquipoFechaFundacionBC);
							edtModFechaFundacionEquipo.setText(equipo.getFfu()+"");
							fechaFundacionEquipo = Integer.parseInt(edtModFechaFundacionEquipo.getText().toString());
							edtModFechaFundacionEquipo.addTextChangedListener(new TextWatcher() {
								@Override
								public void onTextChanged(CharSequence s, int start, int before, int count) {}
								@Override
								public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
								@Override
								public void afterTextChanged(Editable s) {
									if(edtModFechaFundacionEquipo.getText().toString().trim().length()==0) fechaFundacionEquipo = 0;
									fechaFundacionEquipo = Integer.parseInt(edtModFechaFundacionEquipo.getText().toString());
								}
							});
							
							edtModNumSociosEquipo = (EditText) ll.findViewById(R.id.edtModEquipoNumSociosBC);
							edtModNumSociosEquipo.setText(equipo.getSoc()+"");
							numSociosEquipo = Integer.parseInt(edtModNumSociosEquipo.getText().toString());
							edtModNumSociosEquipo.addTextChangedListener(new TextWatcher() {
								@Override
								public void onTextChanged(CharSequence s, int start, int before, int count) {}
								@Override
								public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
								@Override
								public void afterTextChanged(Editable s) {
									if(edtModNumSociosEquipo.getText().toString().trim().length()==0) numSociosEquipo = 0;
									else numSociosEquipo = Integer.parseInt(edtModNumSociosEquipo.getText().toString());
								}
							});
							
							AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
							//builder.setTitle(equipo.getNom());
							builder.setView(ll);
							
							builder.setPositiveButton(getString(R.string.modificar), new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									boolean guardar = true;
									String rellena = getString(R.string.rellena);
									if(nombreEquipo.trim().equals("") || nombreEquipo.trim().equals(getString(R.string.nombreEquipoPuntos))){
										guardar = false;
										rellena = rellena + getString(R.string.nombreEquipoRellene);
									}
									if(ciudadEquipo.trim().equals("") || ciudadEquipo.trim().equals(getString(R.string.ciudadPuntos))){
										guardar = false;
										rellena = rellena + getString(R.string.ciudadEquipoRellene);
									}
									if(estadioEquipo.trim().equals("") || estadioEquipo.trim().equals(getString(R.string.estadioPuntos))){
										guardar = false;
										rellena = rellena + getString(R.string.estadioEquipoRellene);
									}
									if(edtModFechaFundacionEquipo.getText().toString().trim().equals("")){
										guardar = false;
										rellena = rellena + getString(R.string.fechaFundacionEquipoRellene);
									}
									if(edtModNumSociosEquipo.getText().toString().trim().equals("")){
										guardar = false;
										rellena = rellena + getString(R.string.numSociosEquipoRellene);
									}
									
									if(guardar == false){
										Toast.makeText(getActivity(), rellena, Toast.LENGTH_LONG).show();
										guardar = true;
									}else{
										Equipo original = PantallaInicial.bd.obtenerEquipo(id_equipo_modificar);
										Equipo e = new Equipo(id_equipo_modificar, nombreEquipo, fechaFundacionEquipo, numSociosEquipo, estadioEquipo, original.getDep(), ciudadEquipo, original.getNumJugadores());
										if(PantallaInicial.bd.modificarEquipo(e)==1){
											Toast.makeText(getActivity(), getString(R.string.BDModificado), Toast.LENGTH_SHORT).show();
											construirTablaEquipos();
											dialog.dismiss();
										}else{
											Toast.makeText(getActivity(), getString(R.string.BDNoModificado), Toast.LENGTH_SHORT).show();
										}
									}
								}
							});
							builder.setNegativeButton(getString(R.string.volver), new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog,int which) {
											dialog.cancel();
										}
									});
							AlertDialog alertDialog = builder.create();
							alertDialog.show();
							alertDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
							//builder.create().show();
						}
					}
				});
	    		btnModificar.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if(event.getAction() == MotionEvent.ACTION_DOWN) v.setBackgroundResource(R.color.fondo_boton_pressed);
						else v.setBackgroundResource(R.color.blanco);
						return false;
					}
				});
	    		
	    		ImageButton btnEliminar = (ImageButton) fila.findViewById(R.id.trowImgBorrarEquipos);
	    		btnEliminar.setTag(position);
	    		btnEliminar.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						int tag = Integer.parseInt(v.getTag()+"");
						b.putInt("tag", tag);
						AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
						builder.setTitle(getString(R.string.eliminarDialogo) + " (" + lista_equipos.get(tag).getNom() + ")");
						builder.setMessage(getString(R.string.eliminarEquipoPregunta));
						builder.setPositiveButton(getString(R.string.si), new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										if(PantallaInicial.bd.eliminarEquipo(lista_equipos.get(b.getInt("tag")).getId())==1){
											Toast.makeText(getActivity(), getString(R.string.BDEliminado), Toast.LENGTH_SHORT).show();
											construirTablaEquipos();
											dialog.dismiss();
										}else{
											Toast.makeText(getActivity(), getString(R.string.BDNoEliminado), Toast.LENGTH_SHORT).show();
										}
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
				});
	    		btnEliminar.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if(event.getAction() == MotionEvent.ACTION_DOWN) v.setBackgroundResource(R.color.fondo_boton_pressed);
						else v.setBackgroundResource(R.color.blanco);
						return false;
					}
				});
	    		
	    		FrameLayout frm = new FrameLayout(getActivity());
				frm.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1));
				frm.setBackgroundColor(Color.parseColor("#222222"));
				
				tablaEquiposContent.addView(fila);
				if(it.hasNext()) tablaEquiposContent.addView(frm);
	    		position++;
    		}
    	}
    }
	
    //Este método permite crear un nuevo jugador en la BD. La forma de proceder es similar a crearTorneo y crearEquipo
	private void crearJugador(View v){
    	LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.baloncesto_jugadores_crear, null);
		edtNombreJugador = (EditText) ll.findViewById(R.id.edtBCNombreJugador);
		edtNombreJugador.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(edtNombreJugador.getText().toString().equals(getString(R.string.nombreDelJugador)))
					edtNombreJugador.setText("");
				return false;
			}
		});
		nombreJugador = edtNombreJugador.getText().toString();
		edtNombreJugador.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void afterTextChanged(Editable s) {
				nombreJugador = edtNombreJugador.getText().toString();
			}
		});
		
		edtApodoJugador = (EditText) ll.findViewById(R.id.edtBCApodoJugador);
		edtApodoJugador.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(edtApodoJugador.getText().toString().equals(getString(R.string.apodo)))
					edtApodoJugador.setText("");
				return false;
			}
		});
		apodoJugador = edtApodoJugador.getText().toString();
		edtApodoJugador.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void afterTextChanged(Editable s) {
				apodoJugador = edtApodoJugador.getText().toString();
			}
		});
		
		txtFechaNacJugador = (TextView) ll.findViewById(R.id.txtBCFechaNacJugadorSeleccionada);
		btnSeleccionarFechaNacJugador = (ImageButton) ll.findViewById(R.id.btnBCFechaNacJugador);
		btnSeleccionarFechaNacJugador.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	fecha = 5; //Para saber donde cargarlo en setDateEditText
	        	FragmentTransaction ft = getFragmentManager().beginTransaction();
                DialogFragment newFragment = new DatePickerDialogFragment(BaloncestoFragment.this);
                newFragment.show(ft, "date_picker_dialog");
	        }

	    });
		
		spnPosicionJugador = (Spinner) ll.findViewById(R.id.spnBCPosicionJugador);
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(v.getContext(), android.R.layout.simple_spinner_item) {
			public View getView(int position, View convertView, ViewGroup parent) {
				View v = super.getView(position, convertView, parent);

				((TextView) v).setTextSize(20);
				((TextView) v).setTextColor(Color.parseColor(getString(R.color.negro)));
				return v;
			}

			public View getDropDownView(int position, View convertView, ViewGroup parent) {
				View v = super.getDropDownView(position, convertView, parent);

				((TextView) v).setTextSize(20);
				((TextView) v).setTextColor(Color.parseColor(getString(R.color.negro)));

				return v;
			}
		};
		
		lista_posiciones = PantallaInicial.bd.obtenerPosicionesBaloncesto();
		Iterator<PosBaloncesto> ip = lista_posiciones.iterator();
		while(ip.hasNext()){
			adapter.add(ip.next().getDen());
		}
		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnPosicionJugador.setAdapter(adapter);
		spnPosicionJugador.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
				posicionJugador = lista_posiciones.get(position).getId();
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		
		spnEquipoJugador = (Spinner) ll.findViewById(R.id.spnBCEquipoJugador);
		adapter = new ArrayAdapter<CharSequence>(v.getContext(), android.R.layout.simple_spinner_item) {
			public View getView(int position, View convertView, ViewGroup parent) {
				View v = super.getView(position, convertView, parent);

				((TextView) v).setTextSize(20);
				((TextView) v).setTextColor(Color.parseColor(getString(R.color.negro)));
				return v;
			}

			public View getDropDownView(int position, View convertView, ViewGroup parent) {
				View v = super.getDropDownView(position, convertView, parent);

				((TextView) v).setTextSize(20);
				((TextView) v).setTextColor(Color.parseColor(getString(R.color.negro)));

				return v;
			}
		};
		lista_equipos = PantallaInicial.bd.obtenerEquipos("equ_nom",3);
		Iterator<Equipo> iq = lista_equipos.iterator();
		adapter.add("");
		while(iq.hasNext()){
			Equipo e = iq.next();
			adapter.add(e.getNom());
		}
		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnEquipoJugador.setAdapter(adapter);
		spnEquipoJugador.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
				if(position != 0) equipoJugador = lista_equipos.get(position-1).getId();
				else equipoJugador = 0;
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		
		AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
		builder.setTitle(tituloDialogo);
		builder.setView(ll);
		

		builder.setPositiveButton(getString(R.string.crear),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						boolean guardar = true;
						String rellena = getString(R.string.rellena);
						if(nombreJugador.trim().equals("") || nombreJugador.trim().equals(getString(R.string.nombreDelJugador))){
							guardar = false;
							rellena = rellena + getString(R.string.nombreJugadorRellena);
						}
						if(apodoJugador.trim().equals("") || apodoJugador.trim().equals(getString(R.string.apodo))){
							guardar = false;
							rellena = rellena + getString(R.string.apodoJugadorRellena);
						}
						if(txtFechaNacJugador.getText().toString().equals("") || txtFechaNacJugador.getText().toString().equals(getString(R.string.fechaNacimiento))){
							guardar = false;
							rellena = rellena + getString(R.string.fechaNacJugadorRellena);
						}
						
						if(guardar == true){
							try {
								long d = System.currentTimeMillis();
								SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
								if(comprobarFechas(fechaNacJugador, spf.format(d))==false){
									guardar = false;
									rellena = rellena + getString(R.string.fechasNoCorrectas);
								}
							} catch (ParseException e) {e.printStackTrace();}
						}
						
						if(guardar == false){
							Toast.makeText(getActivity(), rellena, Toast.LENGTH_LONG).show();
							guardar = true;
						}else{
							id_jugador = PantallaInicial.bd.obtenerIDJugadorMAX();
							Jugador j = new Jugador(id_jugador, nombreJugador, apodoJugador, fechaNacJugador, posicionJugador, 3, equipoJugador);
							if(PantallaInicial.bd.crearJugador(j)==1){
								Toast.makeText(getActivity(), getString(R.string.jugadorCreado), Toast.LENGTH_SHORT).show();
								construirTablaJugadores();
								dialog.dismiss();
							}else{
								Toast.makeText(getActivity(), getString(R.string.jugadorNoCreado), Toast.LENGTH_SHORT).show();
							}
						}
					}
				});
		builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						dialog.cancel();
					}
				});
		builder.create().show();
    }
	
	//Este método nos permite mostrar la tabla con todos los jugadores existentes en la BD. La forma de proceder es similar a construirTablaTorneos y construirTablaEquipos
	private void construirTablaJugadores(){
    	tablaJugadoresContent.removeAllViews();
    	int position = 0;
    	lista_jugadores = PantallaInicial.bd.obtenerJugadores(orden, 3);
    	Iterator<Jugador> it = lista_jugadores.iterator();
    	while(it.hasNext()){
    		Jugador j = it.next();
    		LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		TableRow fila = (TableRow) inflater.inflate(R.layout.baloncesto_jugadores_table_row, null);
    		TextView nombre = (TextView) fila.findViewById(R.id.txtNombreBCJugadores);
    		nombre.setText(j.getNom());
    		TextView apodo = (TextView) fila.findViewById(R.id.txtApodoBCJugadores);
    		apodo.setText(j.getApo());
    		
    		TextView edad = (TextView) fila.findViewById(R.id.txtEdadBCJugadores);
    		String fechaNac = j.getFna();
    		//A continuación calculamos la edad del jugador
    		SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
    		Date dHoy;
    		Date dNac;
			try {
				dHoy = spf.parse(spf.format(System.currentTimeMillis()));
				dNac = spf.parse(fechaNac);
				long fechaNacMs = dNac.getTime();
	    		long fechaHoyMs = dHoy.getTime();
	    		long diferencia = fechaHoyMs - fechaNacMs;
	    		double anios = Math.floor(diferencia/(1000*60*60*24));
	    		edad.setText(((int)anios/365)+"");
			} catch (ParseException e1) {e1.printStackTrace();}
    		
    		TextView posicion = (TextView) fila.findViewById(R.id.txtPosicionBCJugadores);
    		posicion.setText(PantallaInicial.bd.obtenerPosicionBaloncesto(j.getPos()).getAbr());
    		TextView equipo_jug = (TextView) fila.findViewById(R.id.txtEquipoBCJugadores);
    		if(j.getEqu() == -1) equipo_jug.setText("-");
    		else equipo_jug.setText(PantallaInicial.bd.obtenerEquipo(j.getEqu()).getNom());
    		
    		ImageButton btnVer = (ImageButton) fila.findViewById(R.id.trowImgVerJugador);
    		btnVer.setTag(position);
    		btnVer.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (v != null) {
						int tag = Integer.parseInt(v.getTag()+"");
						Jugador j = lista_jugadores.get(tag);
						LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.baloncesto_jugadores_ver, null);
						txtVerNombreJugador = (TextView) ll.findViewById(R.id.txtVerJugadorNombreBC);
						txtVerNombreJugador.setText(j.getNom());
						
						txtVerApodoJugador = (TextView) ll.findViewById(R.id.txtVerJugadorApodoBC);
						txtVerApodoJugador.setText(j.getApo());
						
						txtVerFechaNacJugador = (TextView) ll.findViewById(R.id.txtVerJugadorFechaNacBC);
						txtVerFechaNacJugador.setText(j.getFna());
						
						txtVerEquipoJugador = (TextView) ll.findViewById(R.id.txtVerJugadorEquipoBC);
						txtVerEquipoJugador.setText(PantallaInicial.bd.obtenerEquipo(j.getEqu()).getNom());
						
						txtVerPosicionJugador = (TextView) ll.findViewById(R.id.txtVerJugadorPosicionBC);
						txtVerPosicionJugador.setText(PantallaInicial.bd.obtenerPosicionBaloncesto(j.getPos()).getDen());
						
						AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
						//builder.setTitle(j.getNom());
						builder.setView(ll);
						
						builder.setNegativeButton(getString(R.string.volver), new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,int which) {
										dialog.cancel();
									}
								});
						AlertDialog alertDialog = builder.create();
						alertDialog.show();
						alertDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
						//builder.create().show();
					}
				}
			});
    		btnVer.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if(event.getAction() == MotionEvent.ACTION_DOWN) v.setBackgroundResource(R.color.fondo_boton_pressed);
					else v.setBackgroundResource(R.color.blanco);
					return false;
				}
			});
    		
    		ImageButton btnModificar = (ImageButton) fila.findViewById(R.id.trowImgEditarJugador);
    		btnModificar.setTag(position);
    		btnModificar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (v != null) {
						int tag = Integer.parseInt(v.getTag()+"");
						jugador = lista_jugadores.get(tag);
						id_jugador_modificar = jugador.getId(); //Para pasarle a la función de modificar más abajo
						LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						ll = (LinearLayout) inflater.inflate(R.layout.baloncesto_jugadores_modificar, null);
						edtModNombreJugador = (EditText) ll.findViewById(R.id.edtModJugadorNombreBC);
						edtModNombreJugador.setText(jugador.getNom());
						nombreJugador = edtModNombreJugador.getText().toString();
						edtModNombreJugador.addTextChangedListener(new TextWatcher() {
							@Override
							public void onTextChanged(CharSequence s, int start, int before, int count) {}
							@Override
							public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
							@Override
							public void afterTextChanged(Editable s) {
								nombreJugador = edtModNombreJugador.getText().toString();
							}
						});
						
						edtModApodoJugador = (EditText) ll.findViewById(R.id.edtModJugadorApodoBC);
						edtModApodoJugador.setText(jugador.getApo());
						apodoJugador = edtModApodoJugador.getText().toString();
						edtModApodoJugador.addTextChangedListener(new TextWatcher() {
							@Override
							public void onTextChanged(CharSequence s, int start, int before, int count) {}
							@Override
							public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
							@Override
							public void afterTextChanged(Editable s) {
								apodoJugador = edtModApodoJugador.getText().toString();
							}
						});
						
						txtModFechaNacJugador = (TextView) ll.findViewById(R.id.txtModJugadorFechaNacBC);
						txtModFechaNacJugador.setText(jugador.getFna());
						fechaNacJugador = jugador.getFna();
						btnSeleccionarFechaNacJugadorMod = (ImageButton) ll.findViewById(R.id.btnModBCFechaNac);
						btnSeleccionarFechaNacJugadorMod.setOnClickListener(new View.OnClickListener() {
					        @Override
					        public void onClick(View v) {
					        	fecha = 6; //Para saber donde cargarlo en setDateEditText
					        	FragmentTransaction ft = getFragmentManager().beginTransaction();
				                DialogFragment newFragment = new DatePickerDialogFragment(BaloncestoFragment.this);
				                newFragment.show(ft, "date_picker_dialog");
					        }
					    });
						
						spnModPosicionJugador = (Spinner) ll.findViewById(R.id.spnModJugadorPosicionBC);
						ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(v.getContext(), android.R.layout.simple_spinner_item) {
							public View getView(int position, View convertView, ViewGroup parent) {
								View v = super.getView(position, convertView, parent);

								((TextView) v).setTextSize(20);
								((TextView) v).setTextColor(Color.parseColor(getString(R.color.negro)));
								return v;
							}

							public View getDropDownView(int position, View convertView,
									ViewGroup parent) {
								View v = super.getDropDownView(position, convertView, parent);

								((TextView) v).setTextSize(20);
								((TextView) v).setTextColor(Color.parseColor(getString(R.color.negro)));

								return v;
							}
						};
						
						int pos_selected = 0;
						int cont = 0;
						lista_posiciones = PantallaInicial.bd.obtenerPosicionesBaloncesto();
						Iterator<PosBaloncesto> ip = lista_posiciones.iterator();
						while(ip.hasNext()){
							PosBaloncesto p = ip.next();
							adapter.add(p.getDen());
							if(p.getId() == jugador.getPos()) pos_selected = cont;
							cont++;
						}
						
						adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						spnModPosicionJugador.setAdapter(adapter);
						spnModPosicionJugador.setOnItemSelectedListener(new OnItemSelectedListener() {
							@Override
							public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
								posicionJugador = lista_posiciones.get(position).getId();
							}
							@Override
							public void onNothingSelected(AdapterView<?> arg0) {}
						});
						spnModPosicionJugador.setSelection(pos_selected);
						
						spnModEquipoJugador = (Spinner) ll.findViewById(R.id.spnModBCEquipoJugador);
						adapter = new ArrayAdapter<CharSequence>(v.getContext(), android.R.layout.simple_spinner_item) {
							public View getView(int position, View convertView, ViewGroup parent) {
								View v = super.getView(position, convertView, parent);

								((TextView) v).setTextSize(20);
								((TextView) v).setTextColor(Color.parseColor(getString(R.color.negro)));
								return v;
							}

							public View getDropDownView(int position, View convertView,
									ViewGroup parent) {
								View v = super.getDropDownView(position, convertView, parent);

								((TextView) v).setTextSize(20);
								((TextView) v).setTextColor(Color.parseColor(getString(R.color.negro)));

								return v;
							}
						};
						
						pos_selected = -1;
						cont = 0;
						lista_equipos = PantallaInicial.bd.obtenerEquipos("equ_nom",3);
						Iterator<Equipo> iq = lista_equipos.iterator();
						adapter.add("");
						while(iq.hasNext()){
							Equipo e = iq.next();
							adapter.add(e.getNom());
							if(e.getId() == jugador.getEqu()) pos_selected = cont;
							cont++;
						}
						
						adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						spnModEquipoJugador.setAdapter(adapter);
						spnModEquipoJugador.setOnItemSelectedListener(new OnItemSelectedListener() {
							@Override
							public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
								if(position != 0) equipoJugador = lista_equipos.get(position-1).getId();
								else equipoJugador = 0;
							}
							@Override
							public void onNothingSelected(AdapterView<?> arg0) {}
						});
						spnModEquipoJugador.setSelection(pos_selected+1);

						AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
						//builder.setTitle(jugador.getNom());
						builder.setView(ll);
						
						builder.setPositiveButton(getString(R.string.modificar), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								boolean guardar = true;
								String rellena = getString(R.string.rellena);
								if(nombreJugador.trim().equals("") || nombreJugador.trim().equals(getString(R.string.nombreDelJugador))){
									guardar = false;
									rellena = rellena + getString(R.string.nombreJugadorRellena);
								}
								if(apodoJugador.trim().equals("") || apodoJugador.trim().equals(getString(R.string.apodo))){
									guardar = false;
									rellena = rellena + getString(R.string.apodoJugadorRellena);
								}
								if(txtModFechaNacJugador.getText().toString().equals("")){
									guardar = false;
									rellena = rellena + getString(R.string.fechaNacJugadorRellena);
								}
								
								if(guardar == true){
									try {
										long d = System.currentTimeMillis();
										SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
										if(comprobarFechas(fechaNacJugador, spf.format(d))==false){
											guardar = false;
											rellena = rellena + getString(R.string.fechasNoCorrectas);
										}
									} catch (ParseException e) {e.printStackTrace();}
								}
								
								if(guardar == false){
									Toast.makeText(getActivity(), rellena, Toast.LENGTH_LONG).show();
									guardar = true;
								}else{
									Jugador j = new Jugador(jugador.getId(), nombreJugador, apodoJugador, fechaNacJugador, posicionJugador, jugador.getDep(), equipoJugador);
									if(PantallaInicial.bd.modificarJugador(j)==1){
										Toast.makeText(getActivity(), getString(R.string.BDModificado), Toast.LENGTH_SHORT).show();
										construirTablaJugadores();
										dialog.dismiss();
									}else{
										Toast.makeText(getActivity(), getString(R.string.BDNoModificado), Toast.LENGTH_SHORT).show();
									}
								}
							}
						});
						builder.setNegativeButton(getString(R.string.volver), new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,int which) {
										dialog.cancel();
									}
								});
						AlertDialog alertDialog = builder.create();
						alertDialog.show();
						alertDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
						//builder.create().show();
					}
				}
			});
    		btnModificar.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if(event.getAction() == MotionEvent.ACTION_DOWN) v.setBackgroundResource(R.color.fondo_boton_pressed);
					else v.setBackgroundResource(R.color.blanco);
					return false;
				}
			});
    		
    		ImageButton btnEliminar = (ImageButton) fila.findViewById(R.id.trowImgBorrarJugador);
    		btnEliminar.setTag(position);
    		btnEliminar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int tag = Integer.parseInt(v.getTag()+"");
					b.putInt("tag", tag);
					AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
					builder.setTitle(getString(R.string.eliminarDialogo) + " (" + lista_jugadores.get(tag).getNom() + ")");
					builder.setMessage(getString(R.string.eliminarJugadorPregunta));
					builder.setPositiveButton(getString(R.string.si), new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									if(PantallaInicial.bd.eliminarJugador(lista_jugadores.get(b.getInt("tag")).getId())==1){
										Toast.makeText(getActivity(), getString(R.string.BDEliminado), Toast.LENGTH_SHORT).show();
										construirTablaJugadores();
										dialog.dismiss();
									}else{
										Toast.makeText(getActivity(), getString(R.string.BDNoEliminado), Toast.LENGTH_SHORT).show();
									}
								}
							});
					builder.setNegativeButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							});
					builder.create().show();
				}
			});
    		btnEliminar.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if(event.getAction() == MotionEvent.ACTION_DOWN) v.setBackgroundResource(R.color.fondo_boton_pressed);
					else v.setBackgroundResource(R.color.blanco);
					return false;
				}
			});
    		
    		FrameLayout frm = new FrameLayout(getActivity());
			frm.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1));
			frm.setBackgroundColor(Color.parseColor("#222222"));
			
			tablaJugadoresContent.addView(fila);
			if(it.hasNext()) tablaJugadoresContent.addView(frm);
    		position++;
    	}
    }
	
	@Override
	public void onStart() {
		super.onStart();
		construirTablaTorneos();
	}
	
	public void onPause(){
		super.onPause();
		orden="";
	}
}
