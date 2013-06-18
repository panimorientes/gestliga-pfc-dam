//Esta es la pestaña de Pádel de la parte de gestión

package fragments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import padel.PantallaGestionTorneoPadel;

import tenis.PantallaGestionTorneoTenis;
import tipos.Deporte;
import tipos.Jugador;
import tipos.Torneo;
import tipos.TorneoTenis;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TableRow.LayoutParams;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.gestliga.PantallaInicial;
import com.example.gestliga.R;

import fragments.TenisFragment.DatePickerDialogFragment;

public class PadelFragment extends SherlockFragment implements OnItemSelectedListener, OnDateSetListener{
 
	private Spinner spnMenu, spnOrdenar;
	private TableLayout tablaTorneos, tablaJugadores,
			tablaTorneosContent, tablaJugadoresContent;
	private Button btnCrear;
	private EditText edtNombreTorneo, edtLugarTorneo, 
			edtModNombreTorneo, edtModLugarTorneo, 
			edtNombreJugador, edtApodoJugador, 
			edtModNombreJugador, edtModApodoJugador;

	private TextView txtFechaInicioTorneo, txtFechaFinTorneo,
			txtVerNombreTorneo, txtVerLugarTorneo, txtVerNumJugadores,
			txtVerFechaI, txtVerFechaF, txtVerDeporte, txtModFechaITorneo,
			txtModFechaFTorneo, txtVerNombreJugador, txtVerApodoJugador, 
			txtVerFechaNacJugador, txtVerEquipoJugador, txtVerPosicionJugador, 
			txtFechaNacJugador, txtModFechaNacJugador;

	private ImageButton btnSeleccionarFechaInicio, btnSeleccionarFechaFin,
			btnSeleccionarFechaInicioMod, btnSeleccionarFechaFinMod,
			btnSeleccionarFechaNacJugador, btnSeleccionarFechaNacJugadorMod;

	private int _dia, _mes, _anio, fecha;
	private String tituloDialogo;

	private String nombreTorneo, lugarTorneo, fechaInicio, fechaFin,
			orden = "", nombreJugador, apodoJugador, fechaNacJugador;
	int id_torneo, deporte, id_torneo_modificar, id_jugador, id_jugador_modificar;
	ArrayList<TorneoTenis> lista_torneos = new ArrayList<TorneoTenis>();
	ArrayList<Deporte> lista_deportes = new ArrayList<Deporte>();
	ArrayList<Jugador> lista_jugadores = new ArrayList<Jugador>();
	TorneoTenis torneo;
	Jugador jugador;
	LinearLayout ll;
	Bundle b = new Bundle();
	private View v;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	try {
			v = inflater.inflate(R.layout.padel_fragment, container, false);
			//Se asignan las referencias de las vistas en el layout
			spnOrdenar = (Spinner) v.findViewById(R.id.spnOrdenarPor_Padel);
			spnMenu = (Spinner) v.findViewById(R.id.spnMenuPadel);
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
			adapter.add(getString(R.string.jugadores));
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spnMenu.setAdapter(adapter);
			spnMenu.setOnItemSelectedListener(this);

			tablaTorneos = (TableLayout) v.findViewById(R.id.tablaTorneosPadel);
			tablaJugadores = (TableLayout) v.findViewById(R.id.tablaJugadoresPadel);
			tablaTorneosContent = (TableLayout) v.findViewById(R.id.tablaTorneosContentPadel);
			tablaJugadoresContent = (TableLayout) v.findViewById(R.id.tablaJugadoresContentPadel);

			final Calendar _cal = Calendar.getInstance();
			_anio = _cal.get(Calendar.YEAR);
			_mes = _cal.get(Calendar.MONTH);
			_dia = _cal.get(Calendar.DAY_OF_MONTH);

			btnCrear = (Button) v.findViewById(R.id.btnCrearPadel);
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
						if (spnMenu.getSelectedItem().toString().equals(getString(R.string.torneos)))
							crearTorneo(v);
						else if (spnMenu.getSelectedItem().toString().equals(getString(R.string.jugadores)))
							crearJugador(v);
					}
				}
			});
		} catch (Exception e) {
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
		if (fecha == 1) {
			txtFechaInicioTorneo.setText(df.format(d));
			fechaInicio = txtFechaInicioTorneo.getText().toString();
		} else if (fecha == 2) {
			txtFechaFinTorneo.setText(df.format(d));
			fechaFin = txtFechaFinTorneo.getText().toString();
		} else if (fecha == 3) {
			txtModFechaITorneo.setText(df.format(d));
			fechaInicio = txtModFechaITorneo.getText().toString();
			if (comprobarFechas(fechaInicio, fechaFin) == false) {
				Toast.makeText(getActivity(), getString(R.string.fechasNoCorrectas), Toast.LENGTH_SHORT).show();
			}
		} else if (fecha == 4) {
			txtModFechaFTorneo.setText(df.format(d));
			fechaFin = txtModFechaFTorneo.getText().toString();
			if (comprobarFechas(fechaInicio, fechaFin) == false) {
				Toast.makeText(getActivity(), getString(R.string.fechasNoCorrectas), Toast.LENGTH_SHORT).show();
			}
		} else if (fecha == 5) {
			txtFechaNacJugador.setText(df.format(d));
			fechaNacJugador = txtFechaNacJugador.getText().toString();
		} else if (fecha == 6) {
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

	private boolean comprobarFechas(String fechaI, String fechaF) throws ParseException {
		SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
		Date dI = spf.parse(fechaI);
		Date dF = spf.parse(fechaF);
		if (dI.after(dF))
			return false;
		else
			return true;
	}

	//Método para el evento Click del Spinner principal
	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
		if (parent.getItemAtPosition(position).toString().equals(getString(R.string.torneos))) {
			orden = "";
			tablaTorneos.setVisibility(View.VISIBLE);
			tablaTorneosContent.setVisibility(View.VISIBLE);
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
			adapter.add(getString(R.string.numJugadores));
			adapter.add(getString(R.string.fechaInicioLinea));
			adapter.add(getString(R.string.fechaFinLinea));
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spnOrdenar.setAdapter(adapter);
			spnOrdenar.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
					if (parent.getItemAtPosition(position).toString().equals(getString(R.string.nombre)))
						orden = "tor_nom";
					else if (parent.getItemAtPosition(position).toString().equals(getString(R.string.numJugadores)))
						orden = "numJugadores";
					else if (parent.getItemAtPosition(position).toString().equals(getString(R.string.fechaInicioLinea)))
						orden = "tor_fin";
					else if (parent.getItemAtPosition(position).toString().equals(getString(R.string.fechaFinLinea)))
						orden = "tor_ffi";
					else
						orden = "";
					construirTablaTorneos();
				}
					@Override
				public void onNothingSelected(AdapterView<?> arg0) {}
			});
			construirTablaTorneos();
		} else if (parent.getItemAtPosition(position).toString().equals(getString(R.string.jugadores))) {
			orden = "";
			tablaTorneos.setVisibility(View.GONE);
			tablaTorneosContent.setVisibility(View.GONE);
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
				public View getDropDownView(int position, View convertView, ViewGroup parent) {
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
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spnOrdenar.setAdapter(adapter);
			spnOrdenar.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
					if (parent.getItemAtPosition(position).toString().equals(getString(R.string.nombre)))
						orden = "jug_nom";
					else if (parent.getItemAtPosition(position).toString().equals(getString(R.string.apodo)))
						orden = "jug_apo";
					else if (parent.getItemAtPosition(position).toString().equals(getString(R.string.edad)))
						orden = "jug_fna";
					else
						orden = "";
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
	private void crearTorneo(View v) {
		//Mostraremos el formulario para crear el torneo dentro de un diálogo, por ello, primero "inflamos" el layout
    	//creado para la creación de un torneo de pádel, y agregamos las referencias de las vistas con el layout
		LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.padel_torneo_crear, null);
		edtNombreTorneo = (EditText) ll.findViewById(R.id.edtPadelNombreTorneo);
		edtNombreTorneo.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (edtNombreTorneo.getText().toString().equals(getString(R.string.nombreTorneo)))
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

		edtLugarTorneo = (EditText) ll.findViewById(R.id.edtPadelLugarTorneo);
		edtLugarTorneo.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (edtLugarTorneo.getText().toString().equals(getString(R.string.lugarTorneo)))
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

		txtFechaInicioTorneo = (TextView) ll.findViewById(R.id.txtPadelFechaInicioTorneo);
		txtFechaFinTorneo = (TextView) ll.findViewById(R.id.txtPadelFechaFinTorneo);
		btnSeleccionarFechaInicio = (ImageButton) ll.findViewById(R.id.btnPadelFechaInicio);
		btnSeleccionarFechaInicio.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fecha = 1; // Para saber donde cargarlo en setDateEditText
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				DialogFragment newFragment = new DatePickerDialogFragment(PadelFragment.this);
				newFragment.show(ft, "date_picker_dialog");
			}

		});
		btnSeleccionarFechaFin = (ImageButton) ll.findViewById(R.id.btnPadelFechaFin);
		btnSeleccionarFechaFin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fecha = 2; // Para saber donde cargarlo en setDateEditText
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				DialogFragment newFragment = new DatePickerDialogFragment(PadelFragment.this);
				newFragment.show(ft, "date_picker_dialog");
			}
		});
		
		AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
		builder.setTitle(tituloDialogo);
		builder.setView(ll);
		//Una vez que se ha asignado el layout al diálogo, establecemos el comportamiento de los botones
		builder.setPositiveButton(getString(R.string.crear), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//Se comprueba que están todos los datos rellenos, y que las fechas son correctas
				boolean guardar = true;
				String rellena = getString(R.string.rellena);
				if (nombreTorneo.trim().equals("") || nombreTorneo.trim().equals(getString(R.string.nombreTorneo))) {
					guardar = false;
					rellena = rellena + getString(R.string.nombreTorneoRellena);
				}
				if (lugarTorneo.trim().equals("") || lugarTorneo.trim().equals(getString(R.string.lugarTorneo))) {
					guardar = false;
					rellena = rellena + getString(R.string.lugarTorneoRellena);
				}
				if (txtFechaInicioTorneo.getText().toString().equals(getString(R.string.fechaInicioLinea))) {
					guardar = false;
					rellena = rellena + getString(R.string.fechaInicioRellena);
				}
				if (txtFechaFinTorneo.getText().toString().equals(getString(R.string.fechaFinLinea))) {
					guardar = false;
					rellena = rellena + getString(R.string.fechaFinRellena);
				}
				if (guardar == true) {
					try {
						if (comprobarFechas(fechaInicio, fechaFin) == false) {
							guardar = false;
							rellena = rellena + getString(R.string.fechasNoCorrectas);
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}

				if (guardar == false) {
					Toast.makeText(getActivity(), rellena, Toast.LENGTH_LONG).show();
					guardar = true;
				} else {
					id_torneo = PantallaInicial.bd.obtenerIDTorneoMAX();
					Torneo t = new Torneo(id_torneo, nombreTorneo, lugarTorneo, fechaInicio, fechaFin, 4, 0);
					if (PantallaInicial.bd.crearTorneo(t) == 1) {
						Toast.makeText(getActivity(), getString(R.string.torneoCreado), Toast.LENGTH_SHORT).show();
						construirTablaTorneos();
						dialog.dismiss();
					} else {
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

	//Este método sirve para mostrar la tabla de todos los torneos de pádel existentes en la BD
	private void construirTablaTorneos() {
		tablaTorneosContent.removeAllViews();
		int position = 0;
		lista_torneos = PantallaInicial.bd.obtenerTorneosPadel(orden);
		Iterator<TorneoTenis> it = lista_torneos.iterator();
		while (it.hasNext()) {
			//Para cada torneo se van creando filas. Para ello se "infla" el layout creado para tal fin
			TorneoTenis t = it.next();
			LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			TableRow fila = (TableRow) inflater.inflate(R.layout.padel_torneos_table_row, null);
			TextView nombre = (TextView) fila.findViewById(R.id.txtNombrePadelTorneo);
			nombre.setText(t.getNom());
			TextView numJugadores = (TextView) fila.findViewById(R.id.txtJugadoresPadelTorneo);
			numJugadores.setText(t.getNumJugadores() + "");
			TextView fechaI = (TextView) fila.findViewById(R.id.txtFechaInicioPadelTorneos);
			fechaI.setText(t.getFin());
			TextView fechaF = (TextView) fila.findViewById(R.id.txtFechaFinPadelTorneos);
			fechaF.setText(t.getFfi());
			//Este ImageButton sirve para ver los datos del torneo
			ImageButton btnVer = (ImageButton) fila.findViewById(R.id.trowImgVer);
			btnVer.setTag(position);
			btnVer.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View vi) {
					if (vi != null) {
						//Los datos se verán en un diálogo, por lo que se "infla" el layout creado para tal fin
						int tag = Integer.parseInt(vi.getTag() + "");
						TorneoTenis t = lista_torneos.get(tag);
						LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.padel_torneo_ver, null);
						txtVerNombreTorneo = (TextView) ll.findViewById(R.id.txtVerTorneoNombrePadel);
						txtVerNombreTorneo.setText(t.getNom());

						txtVerLugarTorneo = (TextView) ll.findViewById(R.id.txtVerTorneoLugarPadel);
						txtVerLugarTorneo.setText(t.getLug());

						txtVerNumJugadores = (TextView) ll.findViewById(R.id.txtVerTorneoNumJugadoresPadel);
						txtVerNumJugadores.setText(t.getNumJugadores() + "");

						txtVerFechaI = (TextView) ll.findViewById(R.id.txtVerTorneoFechaIPadel);
						txtVerFechaI.setText(t.getFin());

						txtVerFechaF = (TextView) ll.findViewById(R.id.txtVerTorneoFechaFPadel);
						txtVerFechaF.setText(t.getFfi());

						txtVerDeporte = (TextView) ll.findViewById(R.id.txtVerTorneoDeportePadel);
						txtVerDeporte.setText(PantallaInicial.bd.obtenerDeporte(t.getDep()));

						ArrayList<Integer> lista = PantallaInicial.bd.obtenerIDJugadoresDeTorneo(t.getId());
						Iterator<Integer> aux = lista.iterator();
						LinearLayout llJugadores = (LinearLayout) ll.findViewById(R.id.llPadelJugadoresInscritosTorneo);
						int n = 1;
						while (aux.hasNext()) {
							TextView txt = new TextView(getActivity().getBaseContext());
							txt.setText(n + ". " + PantallaInicial.bd.obtenerApodoJugador(aux.next()));
							txt.setTextSize(18);
							txt.setTextColor(Color.parseColor(getString(R.color.blanco)));
							llJugadores.addView(txt);
							n++;
						}
						//Se asigna al diálogo la vista
						AlertDialog.Builder builder = new AlertDialog.Builder(vi.getContext());
						builder.setTitle(t.getNom());
						builder.setView(ll);
						//Se establece el comportamiento de los botones
						builder.setNegativeButton(getString(R.string.volver), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
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
					if (event.getAction() == MotionEvent.ACTION_DOWN)
						v.setBackgroundResource(R.color.fondo_boton_pressed);
					else
						v.setBackgroundResource(R.color.blanco);
					return false;
				}
			});
			
			//Con este ImageButton podemos modificar los datos de un torneo. La forma de proceder es igual a btnVer
			ImageButton btnModificar = (ImageButton) fila.findViewById(R.id.trowImgEditar);
			btnModificar.setTag(position);
			btnModificar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (v != null) {
						int tag = Integer.parseInt(v.getTag() + "");
						torneo = lista_torneos.get(tag);
						id_torneo_modificar = torneo.getId(); // Para pasarle a la función de modificar más abajo
						LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						ll = (LinearLayout) inflater.inflate(R.layout.padel_torneo_modificar, null);
						edtModNombreTorneo = (EditText) ll.findViewById(R.id.edtModTorneoNombrePadel);
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

						edtModLugarTorneo = (EditText) ll.findViewById(R.id.edtModTorneoLugarPadel);
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

						txtModFechaITorneo = (TextView) ll.findViewById(R.id.txtModTorneoFechaIPadel);
						txtModFechaITorneo.setText(torneo.getFin());
						fechaInicio = torneo.getFin();
						txtModFechaFTorneo = (TextView) ll.findViewById(R.id.txtModTorneoFechaFPadel);
						txtModFechaFTorneo.setText(torneo.getFfi());
						fechaFin = torneo.getFfi();
						btnSeleccionarFechaInicioMod = (ImageButton) ll.findViewById(R.id.btnModFechaInicioPadel);
						btnSeleccionarFechaInicioMod.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								fecha = 3; // Para saber donde cargarlo en setDateEditText
								FragmentTransaction ft = getFragmentManager().beginTransaction();
								DialogFragment newFragment = new DatePickerDialogFragment(PadelFragment.this);
								newFragment.show(ft,"date_picker_dialog");
							}
						});
						btnSeleccionarFechaFinMod = (ImageButton) ll.findViewById(R.id.btnModFechaFinPadel);
						btnSeleccionarFechaFinMod.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								fecha = 4; // Para saber donde cargarlo en setDateEditText
								FragmentTransaction ft = getFragmentManager().beginTransaction();
								DialogFragment newFragment = new DatePickerDialogFragment(PadelFragment.this);
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
								if (nombreTorneo.trim().equals("")|| nombreTorneo.trim().equals(getString(R.string.nombreTorneo))) {
									guardar = false;
									rellena = rellena + getString(R.string.nombreTorneoRellena);
								}
								if (lugarTorneo.trim().equals("") || lugarTorneo.trim().equals(getString(R.string.lugarTorneo))) {
									guardar = false;
									rellena = rellena + getString(R.string.lugarTorneoRellena);
								}
								if (txtModFechaITorneo.getText().toString().equals(getString(R.string.fechaInicioLinea))) {
									guardar = false;
									rellena = rellena + getString(R.string.fechaInicioRellena);
								}
								if (txtModFechaFTorneo.getText().toString().equals(getString(R.string.fechaFinLinea))) {
									guardar = false;
									rellena = rellena + getString(R.string.fechaFinRellena);
								}
								if (guardar == true) {
									try {
										if (comprobarFechas(fechaInicio, fechaFin) == false) {
											guardar = false;
											rellena = rellena + getString(R.string.fechasNoCorrectas);
										}
									} catch (ParseException e) {
										e.printStackTrace();
									}
								}

								if (guardar == false) {
									Toast.makeText(getActivity(), rellena, Toast.LENGTH_LONG).show();
									guardar = true;
								} else {
									TorneoTenis original = PantallaInicial.bd.obtenerTorneoPadel(id_torneo_modificar);
									TorneoTenis t = new TorneoTenis(id_torneo_modificar, nombreTorneo, lugarTorneo, fechaInicio, fechaFin, original.getDep(), original.getNumJugadores());
									if (PantallaInicial.bd.modificarTorneoTenis(t) == 1) {
										Toast.makeText(getActivity(), getString(R.string.BDModificado), Toast.LENGTH_SHORT).show();
										construirTablaTorneos();
										dialog.dismiss();
									} else {
										Toast.makeText(getActivity(), getString(R.string.BDNoModificado), Toast.LENGTH_SHORT).show();
									}
								}
							}
						});
						builder.setNegativeButton(getString(R.string.volver),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
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
					if (event.getAction() == MotionEvent.ACTION_DOWN)
						v.setBackgroundResource(R.color.fondo_boton_pressed);
					else
						v.setBackgroundResource(R.color.blanco);
					return false;
				}
			});
			//Presionando este ImageButton, eliminaremos de forma permanente el torneo de la BD (previa confirmación)
			ImageButton btnEliminar = (ImageButton) fila.findViewById(R.id.trowImgBorrar);
			btnEliminar.setTag(position);
			btnEliminar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int tag = Integer.parseInt(v.getTag() + "");
					b.putInt("tag", tag);
					AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
					builder.setTitle(getString(R.string.eliminarDialogo) + " (" + lista_torneos.get(tag).getNom() + ")");
					builder.setMessage(getString(R.string.eliminarTorneoPregunta));
					builder.setPositiveButton(getString(R.string.si), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (PantallaInicial.bd.eliminarTorneo(lista_torneos.get(b.getInt("tag")).getId()) == 1) {
								Toast.makeText(getActivity(), getString(R.string.BDEliminado), Toast.LENGTH_SHORT).show();
								construirTablaTorneos();
								dialog.dismiss();
							} else {
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
					if (event.getAction() == MotionEvent.ACTION_DOWN)
						v.setBackgroundResource(R.color.fondo_boton_pressed);
					else
						v.setBackgroundResource(R.color.blanco);
					return false;
				}
			});
			//Con este ImageButton accederemos a la actividad que nos permite gestionar un torneo (jugadores y calendario)
			ImageButton btnGestionar = (ImageButton) fila.findViewById(R.id.trowImgGestionar);
			btnGestionar.setTag(position);
			btnGestionar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int tag = Integer.parseInt(v.getTag()+"");
					Intent intent = new Intent(getActivity(), PantallaGestionTorneoPadel.class);
					intent.putExtra("id_torneo", lista_torneos.get(tag).getId());
					intent.putExtra("nombre", lista_torneos.get(tag).getNom());
					startActivity(intent);
				}
			});
			btnGestionar.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_DOWN)
						v.setBackgroundResource(R.color.fondo_boton_pressed);
					else
						v.setBackgroundResource(R.color.blanco);
					return false;
				}
			});

			FrameLayout frm = new FrameLayout(getActivity());
			frm.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1));
			frm.setBackgroundColor(Color.parseColor("#222222"));

			tablaTorneosContent.addView(fila);
			if (it.hasNext())
				tablaTorneosContent.addView(frm);
			position++;
		}
	}

	//Este método permite crear un nuevo jugador en la BD. La forma de proceder es similar a crearTorneo
	private void crearJugador(View v) {
		LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.padel_jugadores_crear, null);
		edtNombreJugador = (EditText) ll.findViewById(R.id.edtPadelNombreJugador);
		edtNombreJugador.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (edtNombreJugador.getText().toString().equals(getString(R.string.nombreDelJugador))) edtNombreJugador.setText("");
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

		edtApodoJugador = (EditText) ll.findViewById(R.id.edtPadelApodoJugador);
		edtApodoJugador.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (edtApodoJugador.getText().toString().equals(getString(R.string.apodo))) edtApodoJugador.setText("");
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
		txtFechaNacJugador = (TextView) ll.findViewById(R.id.txtPadelFechaNacJugadorSeleccionada);
		btnSeleccionarFechaNacJugador = (ImageButton) ll.findViewById(R.id.btnPadelFechaNacJugador);
		btnSeleccionarFechaNacJugador.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fecha = 5; // Para saber donde cargarlo en setDateEditText
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				DialogFragment newFragment = new DatePickerDialogFragment(PadelFragment.this);
				newFragment.show(ft, "date_picker_dialog");
			}
		});
		
		AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
		builder.setTitle(tituloDialogo);
		builder.setView(ll);

		builder.setPositiveButton(getString(R.string.crear), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				boolean guardar = true;
				String rellena = getString(R.string.rellena);
				if (nombreJugador.trim().equals("") || nombreJugador.trim().equals(getString(R.string.nombreDelJugador))) {
					guardar = false;
					rellena = rellena + getString(R.string.nombreJugadorRellena);
				}
				if (apodoJugador.trim().equals("") || apodoJugador.trim().equals(getString(R.string.apodo))) {
					guardar = false;
					rellena = rellena + getString(R.string.apodoJugadorRellena);
				}
				if (txtFechaNacJugador.getText().toString().equals("") || txtFechaNacJugador.getText().toString().equals(getString(R.string.fechaNacimiento))) {
					guardar = false;
					rellena = rellena + getString(R.string.fechaNacJugadorRellena);
				}

				if (guardar == true) {
					try {
						long d = System.currentTimeMillis();
						SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
						if (comprobarFechas(fechaNacJugador, spf.format(d)) == false) {
							guardar = false;
							rellena = rellena + getString(R.string.fechasNoCorrectas);
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}

				if (guardar == false) {
					Toast.makeText(getActivity(), rellena, Toast.LENGTH_LONG).show();
					guardar = true;
				} else {
					id_jugador = PantallaInicial.bd.obtenerIDJugadorMAX();
					Jugador j = new Jugador(id_jugador, nombreJugador, apodoJugador, fechaNacJugador, -1, 4, 0);
					if (PantallaInicial.bd.crearJugador(j) == 1) {
						Toast.makeText(getActivity(), getString(R.string.jugadorCreado), Toast.LENGTH_SHORT).show();
						construirTablaJugadores();
						dialog.dismiss();
					} else {
						Toast.makeText(getActivity(), getString(R.string.jugadorNoCreado), Toast.LENGTH_SHORT).show();
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

	//Este método nos permite mostrar la tabla con todos los jugadores existentes en la BD. La forma de proceder es similar a construirTablaTorneos
	private void construirTablaJugadores() {
		tablaJugadoresContent.removeAllViews();
		int position = 0;
		lista_jugadores = PantallaInicial.bd.obtenerJugadores(orden, 4);
		Log.d("NumJugadores",lista_jugadores.size()+"");
		Iterator<Jugador> it = lista_jugadores.iterator();
		while (it.hasNext()) {
			Jugador j = it.next();
			LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			TableRow fila = (TableRow) inflater.inflate(R.layout.padel_jugadores_table_row, null);
			TextView nombre = (TextView) fila.findViewById(R.id.txtNombrePadelJugadores);
			nombre.setText(j.getNom());
			TextView apodo = (TextView) fila.findViewById(R.id.txtApodoPadelJugadores);
			apodo.setText(j.getApo());
			TextView edad = (TextView) fila.findViewById(R.id.txtEdadPadelJugadores);
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
				double anios = Math.floor(diferencia / (1000 * 60 * 60 * 24));
				edad.setText(((int) anios / 365) + "");
			} catch (ParseException e1) {
				e1.printStackTrace();
			}

			ImageButton btnVer = (ImageButton) fila.findViewById(R.id.trowImgVerJugador);
			btnVer.setTag(position);
			btnVer.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (v != null) {
						int tag = Integer.parseInt(v.getTag() + "");
						Jugador j = lista_jugadores.get(tag);
						LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.padel_jugadores_ver, null);
						txtVerNombreJugador = (TextView) ll.findViewById(R.id.txtVerJugadorNombrePadel);
						txtVerNombreJugador.setText(j.getNom());

						txtVerApodoJugador = (TextView) ll.findViewById(R.id.txtVerJugadorApodoPadel);
						txtVerApodoJugador.setText(j.getApo());

						txtVerFechaNacJugador = (TextView) ll.findViewById(R.id.txtVerJugadorFechaNacPadel);
						txtVerFechaNacJugador.setText(j.getFna());

						AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
						//builder.setTitle(j.getNom());
						builder.setView(ll);

						builder.setNegativeButton(getString(R.string.volver), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
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
					if (event.getAction() == MotionEvent.ACTION_DOWN)
						v.setBackgroundResource(R.color.fondo_boton_pressed);
					else
						v.setBackgroundResource(R.color.blanco);
					return false;
				}
			});

			ImageButton btnModificar = (ImageButton) fila.findViewById(R.id.trowImgEditarJugador);
			btnModificar.setTag(position);
			btnModificar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (v != null) {
						int tag = Integer.parseInt(v.getTag() + "");
						jugador = lista_jugadores.get(tag);
						id_jugador_modificar = jugador.getId(); // Para pasarle a la función de modificar más abajo
						LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						ll = (LinearLayout) inflater.inflate(R.layout.padel_jugadores_modificar, null);
						edtModNombreJugador = (EditText) ll.findViewById(R.id.edtModJugadorNombrePadel);
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

						edtModApodoJugador = (EditText) ll.findViewById(R.id.edtModJugadorApodoPadel);
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

						txtModFechaNacJugador = (TextView) ll.findViewById(R.id.txtModJugadorFechaNacPadel);
						txtModFechaNacJugador.setText(jugador.getFna());
						fechaNacJugador = jugador.getFna();
						btnSeleccionarFechaNacJugadorMod = (ImageButton) ll.findViewById(R.id.btnModPadelFechaNac);
						btnSeleccionarFechaNacJugadorMod.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								fecha = 6; // Para saber donde cargarlo en setDateEditText
								FragmentTransaction ft = getFragmentManager().beginTransaction();
								DialogFragment newFragment = new DatePickerDialogFragment(PadelFragment.this);
								newFragment.show(ft, "date_picker_dialog");
							}
						});

						AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
						//builder.setTitle(jugador.getNom());
						builder.setView(ll);

						builder.setPositiveButton(getString(R.string.modificar), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								boolean guardar = true;
								String rellena = getString(R.string.rellena);
								if (nombreJugador.trim().equals("") || nombreJugador.trim().equals(getString(R.string.nombreDelJugador))) {
									guardar = false;
									rellena = rellena + getString(R.string.nombreJugadorRellena);
								}
								if (apodoJugador.trim().equals("") || apodoJugador.trim().equals(getString(R.string.apodo))) {
									guardar = false;
									rellena = rellena + getString(R.string.apodoJugadorRellena);
								}
								if (txtModFechaNacJugador.getText().toString().equals("")) {
									guardar = false;
									rellena = rellena + getString(R.string.fechaNacJugadorRellena);
								}

								if (guardar == true) {
									try {
										long d = System.currentTimeMillis();
										SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
										if (comprobarFechas(fechaNacJugador, spf.format(d)) == false) {
											guardar = false;
											rellena = rellena + getString(R.string.fechasNoCorrectas);
										}
									} catch (ParseException e) {
										e.printStackTrace();
									}
								}

								if (guardar == false) {
									Toast.makeText(getActivity(), rellena, Toast.LENGTH_LONG).show();
									guardar = true;
								} else {
									Jugador j = new Jugador(jugador.getId(), nombreJugador, apodoJugador, fechaNacJugador, -1, jugador.getDep(), 0);
									if (PantallaInicial.bd.modificarJugador(j) == 1) {
										Toast.makeText(getActivity(), getString(R.string.BDModificado), Toast.LENGTH_SHORT).show();
										construirTablaJugadores();
										dialog.dismiss();
									} else {
										Toast.makeText(getActivity(), getString(R.string.BDNoModificado), Toast.LENGTH_SHORT).show();
									}
								}
							}
						});
						builder.setNegativeButton(getString(R.string.volver), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
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
					if (event.getAction() == MotionEvent.ACTION_DOWN)
						v.setBackgroundResource(R.color.fondo_boton_pressed);
					else
						v.setBackgroundResource(R.color.blanco);
					return false;
				}
			});

			ImageButton btnEliminar = (ImageButton) fila.findViewById(R.id.trowImgBorrarJugador);
			btnEliminar.setTag(position);
			btnEliminar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int tag = Integer.parseInt(v.getTag() + "");
					b.putInt("tag", tag);
					AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
					builder.setTitle(getString(R.string.eliminarDialogo) + " (" + lista_jugadores.get(tag).getNom() + ")");
					builder.setMessage(getString(R.string.eliminarJugadorPregunta));
					builder.setPositiveButton(getString(R.string.si), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (PantallaInicial.bd.eliminarJugador(lista_jugadores.get(b.getInt("tag")).getId()) == 1) {
								Toast.makeText(getActivity(), getString(R.string.BDEliminado), Toast.LENGTH_SHORT).show();
								construirTablaJugadores();
								dialog.dismiss();
							} else {
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
					if (event.getAction() == MotionEvent.ACTION_DOWN)
						v.setBackgroundResource(R.color.fondo_boton_pressed);
					else
						v.setBackgroundResource(R.color.blanco);
					return false;
				}
			});

			FrameLayout frm = new FrameLayout(getActivity());
			frm.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1));
			frm.setBackgroundColor(Color.parseColor("#222222"));

			tablaJugadoresContent.addView(fila);
			if (it.hasNext()) tablaJugadoresContent.addView(frm);
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
