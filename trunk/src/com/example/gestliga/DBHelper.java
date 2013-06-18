//Esta clase maneja todas las operaciones contra la base de datos

package com.example.gestliga;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import tipos.Clasificacion;
import tipos.ClasificacionBaloncesto;
import tipos.DatosJugadorPartido;
import tipos.DatosJugadorPartidoBC;
import tipos.Deporte;
import tipos.Equipo;
import tipos.Recuento;
import tipos.GoleadorEquipos;
import tipos.IncidenciaBalonmano;
import tipos.IncidenciaFutbol;
import tipos.Jugador;
import tipos.MinutosJugador;
import tipos.Partido;
import tipos.PartidoMejorVictoria;
import tipos.PosBaloncesto;
import tipos.PosBalonmano;
import tipos.PosFutbol;
import tipos.Rendimiento;
import tipos.Tarjetas;
import tipos.Torneo;
import tipos.TorneoTenis;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	private static String ruta_bd = "/data/data/com.example.gestliga/databases/bd_gestliga"; // Esta es la ruta donde Android guardará la BD
	private static String nombre_bd = "bd_gestliga";
	private SQLiteDatabase bd;
	private final Context context;
	final private static int BBDD_VERSION = 1;

	//Constructor
	public DBHelper(Context context) {
		super(context, nombre_bd, null, BBDD_VERSION);
		this.context = context;
	}

	//Este método crea la BD en caso de que no exista ya en el dispositivo
	public void crearBD() throws IOException {
		boolean existeBD = comprobarBD();
		if (existeBD == true) {
		} else {
			try {
				this.getReadableDatabase();
				copiarBD();
			} catch (Exception e) {
			}
		}
	}

	//Este método comprueba si ya existe la BD
	public boolean comprobarBD() {
		File dbFile = new File(ruta_bd);
		return dbFile.exists();
	}

	// Este método se encarga de copiar la BD desde el directorio externo a la
	// aplicación al directorio específico
	// que Android dispone para la aplicación.
	private void copiarBD() throws IOException {
		InputStream input = context.getAssets().open(nombre_bd); // Obtiene la BD de la carpeta Assets del proyecto
		OutputStream output = new FileOutputStream(ruta_bd); // Creamos la dirección para la BD

		byte[] buffer = new byte[1024];
		int length;

		while ((length = input.read(buffer)) > 0) {
			output.write(buffer, 0, length);
		}

		output.flush();
		output.close();
		input.close();
	}

	// Este método se encarga de abrir la conexión con la BD
	public void open() throws IOException {
		crearBD();
		bd = SQLiteDatabase.openDatabase(ruta_bd, null,
				SQLiteDatabase.OPEN_READWRITE);

		// Espacio reservado para actualizaciones de la BD
	}

	// Con este método se cierra la conexión con la BD
	public synchronized void close() {
		if (bd != null) {
			super.close();
		}
	}

	// Estos dos métodos no son utilizados, ya que no estamos creando la BD
	// desde dentro de la aplicación
	@Override
	public void onCreate(SQLiteDatabase db) {
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	// TABLAS
	final private String OBJ_JUGADOR = "obj_jugador";
	final private String OBJ_EQUIPO = "obj_equipo";
	final private String OBJ_TORNEO = "obj_torneo";
	final private String OBJ_PARTIDO = "obj_partido";
	final private String OBJ_INCIDENCIA_FUTBOL = "obj_incidencia_futbol";
	final private String OBJ_INCIDENCIA_BALONMANO = "obj_incidencia_balonmano";
	final private String REL_JUGADOR_TORNEO = "rel_jugador_torneo";
	final private String REL_JUGADOR_PARTIDO = "rel_jugador_partido";
	final private String REL_EQUIPO_TORNEO = "rel_equipo_torneo";
	final private String REL_EQUIPO_PARTIDO = "rel_equipo_partido";
	final private String REL_JUG_BALONCESTO_PART = "rel_jug_baloncesto_part";
	final private String REL_JUG_MINUTOS_PART = "rel_jug_minutos_part";
	final private String REL_JUG_FUTBOL_PART = "rel_jug_futbol_part";
	final private String REL_JUG_BALONMANO_PART = "rel_jug_balonmano_part";
	final private String TIP_POSICION_FUTBOL = "tip_posicion_futbol";
	final private String TIP_POSICION_BALONCESTO = "tip_posicion_baloncesto";
	final private String TIP_POSICION_BALONMANO = "tip_posicion_balonmano";
	final private String TIP_DEPORTE = "tip_deporte";
	final private String TIP_TIPO_INCIDENCIA = "tip_tipo_incidencia";
	final private String TIP_PIE = "tip_pie";
	final private String TIP_COLOR = "tip_color";

	// OPERACIONES
	private String consulta;

	//Este método devuelve el próximo ID de un Torneo
	public int obtenerIDTorneoMAX() {
		int id;
		consulta = "SELECT MAX(tor_id) FROM " + OBJ_TORNEO;
		Cursor c = bd.rawQuery(consulta, null);
		if (c.moveToFirst())
			id = c.getInt(0) + 1;
		else
			id = 0;

		c.close();
		return id;
	}

	//Este método insertar un nuevo registro Torneo y devuelve 1 en caso de éxito o 0 si ocurre algún fallo
	public int crearTorneo(Torneo t) {
		try {
			consulta = "INSERT INTO " + OBJ_TORNEO + " VALUES(" + t.getId()
					+ ", '" + t.getNom() + "', '" + t.getLug() + "', '"
					+ t.getFin() + "', '" + t.getFfi() + "', " + t.getDep()
					+ ")";

			bd.execSQL(consulta);
			return 1;
		} catch (Exception e) {
			return 0;
		}
	}

	//Este método devuelve una lista con todos los torneos
	//Recibe: el orden por el que se filtrará la búsqueda y el deporte al que pertenece el torneo
	public ArrayList<Torneo> obtenerTorneos(String orden, int dep) {
		if (orden.length() > 0)
			orden = " ORDER BY REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(LOWER("+orden+"),'á','a'), 'é','e'),'í','i'),'ó','o'),'ú','u'),'Á','A'),'É','E'),'Í','I'),'Ó','O'),'Ú','U')";
		ArrayList<Torneo> lista = new ArrayList<Torneo>();
		consulta = "SELECT *, (SELECT COUNT(*) FROM " + REL_EQUIPO_TORNEO
				+ " AS eqt WHERE eqt.eit_tor = tor.tor_id) AS numEquipos FROM "
				+ OBJ_TORNEO + " AS tor WHERE tor.tor_dep = " + dep
				+ " GROUP BY tor.tor_id" + orden;
		Cursor c = bd.rawQuery(consulta, null);
		if (c.moveToFirst()) {
			do {
				lista.add(new Torneo(c.getInt(0), c.getString(1), c
						.getString(2), c.getString(3), c.getString(4), c
						.getInt(5), c.getInt(6)));
			} while (c.moveToNext());
		}
		c.close();
		return lista;
	}

	//Este método devuelve el Torneo cuya ID es la que se pasa por parámetro
	public Torneo obtenerTorneo(int id_torneo) {
		Torneo t = new Torneo();
		String consulta = "SELECT *, (SELECT COUNT(*) FROM "
				+ REL_EQUIPO_TORNEO
				+ " AS eqt WHERE eqt.eit_tor = tor.tor_id) AS numEquipos FROM "
				+ OBJ_TORNEO + " AS tor WHERE tor.tor_id = " + id_torneo
				+ " GROUP BY tor.tor_id";
		Cursor c = bd.rawQuery(consulta, null);
		if (c.moveToFirst()) {
			t = new Torneo(c.getInt(0), c.getString(1), c.getString(2),
					c.getString(3), c.getString(4), c.getInt(5), c.getInt(6));
		}
		c.close();
		return t;
	}

	//Este método devuelve el número de equipos que pertenecen al torneo que recibe como parámetro
	public int obtenerNumEquiposTorneo(int id_torneo) {
		consulta = "SELECT COUNT(*) FROM " + REL_EQUIPO_TORNEO
				+ " WHERE eit_tor = " + id_torneo;
		Cursor c = bd.rawQuery(consulta, null);
		int num;
		if (c.moveToFirst())
			num = c.getInt(0);
		else
			num = 0;
		c.close();
		return num;
	}

	//Este método realiza una modificación en el registro Torneo que recibe como parámetro
	//Devuelve 1 en caso de éxito y 0 si ocurre algún fallo
	public int modificarTorneo(Torneo t) {
		try {
			String consulta = "UPDATE " + OBJ_TORNEO + " SET tor_nom = '"
					+ t.getNom() + "', tor_lug = '" + t.getLug()
					+ "', tor_fin = '" + t.getFin() + "', tor_ffi = '"
					+ t.getFfi() + "' WHERE tor_id = " + t.getId();
			bd.execSQL(consulta);
			return 1;
		} catch (Exception e) {
			return 0;
		}
	}

	//Este método elimina el torneo cuya ID recibe como parámetro
	//Devuelve 1 en caso de éxito y 0 si ocurre algún fallo
	public int eliminarTorneo(int id_torneo) {
		bd.beginTransaction();
		try {
			bd.execSQL("DELETE FROM " + OBJ_TORNEO + " WHERE tor_id = "
					+ id_torneo);
			bd.execSQL("DELETE FROM " + REL_EQUIPO_TORNEO + " WHERE eit_tor = "
					+ id_torneo);
			bd.execSQL("DELETE FROM " + REL_JUGADOR_TORNEO
					+ " WHERE jit_tor = " + id_torneo);
			eliminarPartidosTorneo(id_torneo);
			bd.setTransactionSuccessful();
			bd.endTransaction();
			return 1;
		} catch (Exception e) {
			bd.endTransaction();
			return 0;
		}

	}

	//Este método devuelve una cadena de texto con el nombre del deporte cuya ID es recibida por parámetro
	public String obtenerDeporte(int dep) {
		String consulta = "SELECT tde_den FROM " + TIP_DEPORTE
				+ " WHERE tde_id = " + dep;
		Cursor c = bd.rawQuery(consulta, null);
		if (c.moveToFirst()) {
			String den = c.getString(0);
			c.close();
			return den;
		} else {
			c.close();
			return "";
		}
	}

	//Este método devuelve una lista con todos los deportes que existen en la BD
	public ArrayList<Deporte> obtenerDeportes() {
		ArrayList<Deporte> lista = new ArrayList<Deporte>();
		String consulta = "SELECT * FROM " + TIP_DEPORTE;
		Cursor c = bd.rawQuery(consulta, null);
		if (c.moveToFirst()) {
			do {
				lista.add(new Deporte(c.getInt(0), c.getString(1)));
			} while (c.moveToNext());
		}
		c.close();
		return lista;
	}

	// EQUIPOS
	//Este método devuelve una lista con los Equipos filtrados por deporte y ordenados según los parámetros de entrada
	public ArrayList<Equipo> obtenerEquipos(String orden, int dep) {
		if (orden.length() > 0)
			orden = " ORDER BY REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(LOWER("+orden+"),'á','a'), 'é','e'),'í','i'),'ó','o'),'ú','u'),'Á','A'),'É','E'),'Í','I'),'Ó','O'),'Ú','U')";
		ArrayList<Equipo> lista = new ArrayList<Equipo>();
		consulta = "SELECT *, (SELECT COUNT(*) FROM "
				+ OBJ_JUGADOR
				+ " AS jug WHERE jug.jug_equ = equ.equ_id) AS numJugadores FROM "
				+ OBJ_EQUIPO + " AS equ WHERE equ_dep = " + dep
				+ " GROUP BY equ.equ_id" + orden;
		Cursor c = bd.rawQuery(consulta, null);
		if (c.moveToFirst()) {
			do {
				lista.add(new Equipo(c.getInt(0), c.getString(1), c.getInt(2),
						c.getInt(3), c.getString(4), c.getInt(5), c
								.getString(6), c.getInt(7)));
			} while (c.moveToNext());
		}
		c.close();
		return lista;
	}

	//Este método devuelve una lista de Equipos filtrados por nombre, deporte y torneo
	public ArrayList<Equipo> obtenerEquiposPorNombre(String buscar, int dep, int torneo) {
		ArrayList<Equipo> lista = new ArrayList<Equipo>();
		consulta = "SELECT *, 0 FROM " + OBJ_EQUIPO
				+ " WHERE equ_id NOT IN(SELECT eit_equ FROM "
				+ REL_EQUIPO_TORNEO + " WHERE eit_tor = " + torneo
				+ ") AND equ_nom LIKE '%" + buscar + "%' AND equ_dep = " + dep
				+ " ORDER BY equ_id";
		Cursor c = bd.rawQuery(consulta, null);
		if (c.moveToFirst()) {
			do {
				lista.add(new Equipo(c.getInt(0), c.getString(1), c.getInt(2),
						c.getInt(3), c.getString(4), c.getInt(5), c
								.getString(6), c.getInt(7)));
			} while (c.moveToNext());
		}
		c.close();
		return lista;
	}

	//Este método devuelve el Equipo correspondiente al ID que recibe como parámetro
	public Equipo obtenerEquipo(int id_equipo) {
		Equipo e = new Equipo();
		String consulta = "SELECT *, (SELECT COUNT(*) FROM "
				+ OBJ_JUGADOR
				+ " AS jug WHERE jug.jug_equ = equ.equ_id) AS numJugadores FROM "
				+ OBJ_EQUIPO + " AS equ WHERE equ.equ_id = " + id_equipo
				+ " GROUP BY equ.equ_id";
		Cursor c = bd.rawQuery(consulta, null);
		if (c.moveToFirst()) {
			e = new Equipo(c.getInt(0), c.getString(1), c.getInt(2),
					c.getInt(3), c.getString(4), c.getInt(5), c.getString(6),
					c.getInt(7));
		}
		c.close();
		return e;
	}

	//Este método devuelve el siguiente ID disponible para un equipo
	//Devuelve 1 en caso de éxito y 0 si ocurre algún fallo
	public int obtenerIDEquipoMAX() {
		int id;
		consulta = "SELECT MAX(equ_id) FROM " + OBJ_EQUIPO;
		Cursor c = bd.rawQuery(consulta, null);
		if (c.moveToFirst())
			id = c.getInt(0) + 1;
		else
			id = 0;

		c.close();
		return id;
	}

	//Este métedo inserta un nuevo nuevo equipo en la BD que es recibido por parámetro
	//Devuelve 1 en caso de éxito y 0 si ocurre algún fallo
	public int crearEquipo(Equipo e) {
		try {
			consulta = "INSERT INTO " + OBJ_EQUIPO + " VALUES(" + e.getId()
					+ ", '" + e.getNom() + "', " + e.getFfu() + ", "
					+ e.getSoc() + ", '" + e.getEst() + "', " + e.getDep()
					+ ", '" + e.getCiu() + "')";

			bd.execSQL(consulta);
			return 1;
		} catch (Exception ex) {
			return 0;
		}
	}

	//Este método elimina el Equipo de la BD que se pasa por parámetro
	//Devuelve 1 en caso de éxito y 0 si ocurre algún fallo
	public int eliminarEquipo(int id_equipo) {
		bd.beginTransaction();
		try {
			bd.execSQL("DELETE FROM " + OBJ_EQUIPO + " WHERE equ_id = "
					+ id_equipo);
			bd.execSQL("DELETE FROM " + REL_EQUIPO_TORNEO + " WHERE eit_equ = "
					+ id_equipo);
			bd.execSQL("UPDATE " + OBJ_JUGADOR
					+ " SET jug_equ = 0 WHERE jug_equ = " + id_equipo);
			bd.execSQL("UPDATE " + REL_EQUIPO_PARTIDO
					+ " SET ejp_equ = 0 WHERE ejp_equ = " + id_equipo);
			bd.setTransactionSuccessful();
			bd.endTransaction();
			return 1;
		} catch (Exception e) {
			bd.endTransaction();
			return 0;
		}
	}

	//Este método modifica un equipo con los datos que se pasan por parámetro
	//Devuelve 1 en caso de éxito y 0 si ocurre algún fallo
	public int modificarEquipo(Equipo e) {
		try {
			String consulta = "UPDATE " + OBJ_EQUIPO + " SET equ_nom = '"
					+ e.getNom() + "', equ_ffu = " + e.getFfu()
					+ ", equ_soc = " + e.getSoc() + ", equ_est = '"
					+ e.getEst() + "', equ_ciu = '" + e.getCiu()
					+ "' WHERE equ_id = " + e.getId();
			bd.execSQL(consulta);
			return 1;
		} catch (Exception ex) {
			return 0;
		}
	}

	// JUGADORES
	//Este método devuelve una lista de Jugadores filtrados por el deporte y ordenados según los parámetros de entrada
	public ArrayList<Jugador> obtenerJugadores(String orden, int dep) {
		ArrayList<Jugador> lista = new ArrayList<Jugador>();
		if (orden.length() > 0) {
			if (orden.equals("jug_equ")) {
				orden = " ORDER BY REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(LOWER(equipo),'á','a'), 'é','e'),'í','i'),'ó','o'),'ú','u'),'Á','A'),'É','E'),'Í','I'),'Ó','O'),'Ú','U')";
			} else {
				orden = " ORDER BY REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(LOWER(" + orden + "),'á','a'), 'é','e'),'í','i'),'ó','o'),'ú','u'),'Á','A'),'É','E'),'Í','I'),'Ó','O'),'Ú','U')";
			}
		}

		consulta = "SELECT jug.*, equ.equ_nom AS equipo FROM " + OBJ_JUGADOR
				+ " AS jug, " + OBJ_EQUIPO + " AS equ "
				+ "WHERE jug.jug_equ = equ.equ_id AND jug.jug_dep = " + dep
				+ orden;
		Cursor c = bd.rawQuery(consulta, null);
		if (c.moveToFirst()) {
			do {
				lista.add(new Jugador(c.getInt(0), c.getString(1), c
						.getString(2), c.getString(3), c.getInt(4),
						c.getInt(5), c.getInt(6)));
			} while (c.moveToNext());
		}
		c.close();
		return lista;
	}

	//Este método devuelve los datos de la posición de fútbol cuyo ID se recibe por parámetro
	public PosFutbol obtenerPosicionFutbol(int pos) {
		PosFutbol posicion = new PosFutbol();
		consulta = "SELECT * FROM " + TIP_POSICION_FUTBOL + " WHERE tpf_id = "
				+ pos;
		Cursor c = bd.rawQuery(consulta, null);
		if (c.moveToFirst())
			posicion = new PosFutbol(c.getInt(0), c.getString(1),
					c.getString(2));
		c.close();
		return posicion;
	}

	//Este método elimina el Jugador de la BD cuyo ID es recibido por parámetro
	//Devuelve 1 en caso de éxito y 0 si ocurre algún fallo
	public int eliminarJugador(int id_jug) {
		bd.beginTransaction();
		try {
			bd.execSQL("DELETE FROM " + OBJ_JUGADOR + " WHERE jug_id = "
					+ id_jug);
			bd.execSQL("DELETE FROM " + REL_JUG_BALONCESTO_PART
					+ " WHERE jbp_jug = " + id_jug);
			bd.execSQL("DELETE FROM " + REL_JUG_MINUTOS_PART
					+ " WHERE jmp_jug = " + id_jug);
			bd.execSQL("DELETE FROM " + REL_JUG_FUTBOL_PART
					+ " WHERE jfp_jug = " + id_jug);
			bd.execSQL("DELETE FROM " + REL_JUG_BALONMANO_PART
					+ " WHERE jbmp_jug = " + id_jug);
			bd.execSQL("DELETE FROM " + REL_JUGADOR_TORNEO
					+ " WHERE jit_jug = " + id_jug);
			bd.execSQL("DELETE FROM " + REL_JUGADOR_PARTIDO
					+ " WHERE jjp_jug = " + id_jug);
			bd.setTransactionSuccessful();
			bd.endTransaction();
			return 1;
		} catch (Exception e) {
			bd.endTransaction();
			return 0;
		}
	}

	//Este método devuelve una lista con todas las posiciones de fútbol de la BD
	public ArrayList<PosFutbol> obtenerPosicionesFutbol() {
		ArrayList<PosFutbol> lista = new ArrayList<PosFutbol>();
		String consulta = "SELECT * FROM " + TIP_POSICION_FUTBOL;
		Cursor c = bd.rawQuery(consulta, null);
		if (c.moveToFirst()) {
			do {
				lista.add(new PosFutbol(c.getInt(0), c.getString(1), c
						.getString(2)));
			} while (c.moveToNext());
		}
		c.close();
		return lista;
	}

	//Este método devuelve el siguiente ID disponible para un Jugador
	public int obtenerIDJugadorMAX() {
		String consulta = "SELECT MAX(jug_id) FROM " + OBJ_JUGADOR;
		Cursor c = bd.rawQuery(consulta, null);
		if (c.moveToFirst()) {
			int num = c.getInt(0);
			c.close();
			return num + 1;
		} else {
			c.close();
			return 0;
		}
	}

	//Este método inserta un nuevo registro Jugador con los parámetros de entrada.
	//Devuelve 1 en caso de éxito y 0 si ocurre algún fallo
	public int crearJugador(Jugador j) {
		try {
			consulta = "INSERT INTO " + OBJ_JUGADOR + " VALUES(" + j.getId()
					+ ", '" + j.getNom() + "', '" + j.getApo() + "', '"
					+ j.getFna() + "', " + j.getPos() + ", " + j.getDep()
					+ ", " + j.getEqu() + ")";

			bd.execSQL(consulta);
			return 1;
		} catch (Exception ex) {
			return 0;
		}
	}

	//Este método modifica el registro para el Jugador que se le pasa como parámetro de entrada
	//Devuelve 1 en caso de éxito y 0 si ocurre algún fallo
	public int modificarJugador(Jugador j) {
		try {
			String consulta = "UPDATE " + OBJ_JUGADOR + " SET " + "jug_nom = '"
					+ j.getNom() + "', " + "jug_apo = '" + j.getApo() + "', "
					+ "jug_fna = '" + j.getFna() + "', " + "jug_pos = "
					+ j.getPos() + ", " + "jug_equ = " + j.getEqu() + " "
					+ "WHERE jug_id = " + j.getId();
			bd.execSQL(consulta);
			return 1;
		} catch (Exception e) {
			return 0;
		}
	}

	//Este método registra una nueva relación entre un equipo y un torneo
	//Devuelve 1 en caso de éxito y 0 si ocurre algún fallo
	public int insertarEquiposEnTorneo(ArrayList<Integer> lista_equipos, int id_torneo) {
		try {
			bd.beginTransaction();
			Iterator<Integer> it = lista_equipos.iterator();
			while (it.hasNext()) {
				String consulta = "INSERT INTO " + REL_EQUIPO_TORNEO
						+ " VALUES (" + it.next() + ", " + id_torneo + ")";
				bd.execSQL(consulta);
			}
			bd.setTransactionSuccessful();
			bd.endTransaction();
			return 1;
		} catch (Exception e) {
			bd.endTransaction();
			return 0;
		}
	}

	//Este método devuelve una lista de IDs de los equipos que pertenecen al torneo que recibe como parámetro
	public ArrayList<Integer> obtenerIDEquiposDeTorneo(int id_torneo) {
		ArrayList<Integer> lista = new ArrayList<Integer>();
		Cursor c = bd.rawQuery("SELECT eit_equ FROM " + REL_EQUIPO_TORNEO
				+ " WHERE eit_tor = " + id_torneo, null);
		if (c.moveToFirst()) {
			do {
				lista.add(c.getInt(0));
			} while (c.moveToNext());
		}
		c.close();
		return lista;
	}

	//Este método devuelve una cadena con el nombre del equipo cuyo ID corresponde al parámetro de entrada
	public CharSequence obtenerNombreEquipo(int id_equ) {
		String nombre = "";
		Cursor c = bd.rawQuery("SELECT equ_nom FROM " + OBJ_EQUIPO
				+ " WHERE equ_id = " + id_equ, null);
		if (c.moveToFirst())
			nombre = c.getString(0);
		c.close();
		return nombre;
	}

	//Este método devuelve una lista con los Equipos filtrados por deporte y pertenecientes al Torneo pasado por parámetro
	public ArrayList<Equipo> obtenerEquiposDeTorneo(int id_torneo, int dep) {
		ArrayList<Equipo> lista = new ArrayList<Equipo>();
		consulta = "SELECT *, (SELECT COUNT(*) FROM "
				+ OBJ_JUGADOR
				+ " AS jug WHERE jug.jug_equ = equ.equ_id) AS numJugadores FROM "
				+ OBJ_EQUIPO + " AS equ, " + REL_EQUIPO_TORNEO
				+ " WHERE equ_id = eit_equ AND equ_dep = " + dep
				+ " AND eit_tor = " + id_torneo + " GROUP BY equ_id";
		Cursor c = bd.rawQuery(consulta, null);
		if (c.moveToFirst()) {
			do {
				lista.add(new Equipo(c.getInt(0), c.getString(1), c.getInt(2),
						c.getInt(3), c.getString(4), c.getInt(5), c
								.getString(6), c.getInt(7)));
			} while (c.moveToNext());
		}
		c.close();
		return lista;
	}

	//Este método elimina los equipos que recibe por parámetro de un torneo
	//Devuelve 1 en caso de éxito y 0 si ocurre algún fallo
	public int eliminarEquiposTorneo(ArrayList<Integer> lista_equipos, int id_torneo) {
		bd.beginTransaction();
		try {
			Iterator<Integer> it = lista_equipos.iterator();
			while (it.hasNext()) {
				bd.execSQL("DELETE FROM " + REL_EQUIPO_TORNEO
						+ " WHERE eit_equ = " + it.next() + " AND eit_tor = " + id_torneo);
			}
			bd.setTransactionSuccessful();
			bd.endTransaction();
			return 1;
		} catch (Exception e) {
			bd.endTransaction();
			return 0;
		}
	}

	//Este método devuelve el siguiente ID disponible de un Partido
	public int obtenerIDPartidoMax() {
		int num;
		Cursor c = bd.rawQuery("SELECT MAX(par_id) FROM " + OBJ_PARTIDO, null);
		if (c.moveToFirst())
			num = c.getInt(0) + 1;
		else
			num = 0;
		c.close();
		return num;
	}

	//Este método crea los partidos de un torneo
	//Recibe:
	/* id_torneo: ID correspondiente al torneo
	 * lista_equipos: lista de los equipos que pertenecen al torneo
	 * id_dep: ID correspondiente al deporte
	 * descripcion: cadena que indica la jornada del torneo
	 * modo: indica si los partidos van a ser sólo de ida o de ida y vuelta
	 * local: booleano que indica el método de insertar los equipos en el partido
	 */
	public void insertarPartidosTorneo(int id_torneo, ArrayList<Equipo> lista_equipos, int id_dep, String descripcion, int modo, boolean local) {
		int num_equipos = lista_equipos.size();

		for (int i = 0, j = (num_equipos - 1); i < j; i++, j--) {
			int id_partido = obtenerIDPartidoMax();
			Partido part = new Partido(id_partido, id_dep, "", "", descripcion, id_torneo, 0);
			crearPartido(part);
			if (modo == 1) { /* Ida */
				if (local) {
					insertarEquipoEnPartido(id_partido, lista_equipos.get(i).getId(), 0);
					insertarEquipoEnPartido(id_partido, lista_equipos.get(j).getId(), 0);
				} else {
					insertarEquipoEnPartido(id_partido, lista_equipos.get(j).getId(), 0);
					insertarEquipoEnPartido(id_partido, lista_equipos.get(i).getId(), 0);
				}
			} else {
				if (local) {
					insertarEquipoEnPartido(id_partido, lista_equipos.get(j).getId(), 0);
					insertarEquipoEnPartido(id_partido, lista_equipos.get(i).getId(), 0);
				} else {
					insertarEquipoEnPartido(id_partido, lista_equipos.get(i).getId(), 0);
					insertarEquipoEnPartido(id_partido, lista_equipos.get(j).getId(), 0);
				}
			}
		}
	}

	//Este método crea un nuevo registro en la tabla Partidos
	public void crearPartido(Partido p) {
		bd.execSQL("INSERT INTO " + OBJ_PARTIDO + " VALUES(" + p.getId() + ", "
				+ p.getDep() + ", '" + p.getFec() + "', '" + p.getHor()
				+ "', '" + p.getDes() + "', " + p.getTor() + ", 0)");
	}

	//Este método inserta un nuevo registro en la relación de equipo con partido, con los parámetros de entrada
	public void insertarEquipoEnPartido(int id_partido, int id_equipo, int resultado) {
		bd.execSQL("INSERT INTO " + REL_EQUIPO_PARTIDO + " VALUES(" + id_equipo + "," + id_partido + "," + resultado + ")");
	}

	//Este método devuelve un booleano que será true cuando exista un calendario para el torneo que recibe como parámetro de entrada
	public boolean comprobarCalendarioTorneo(int id_torneo) {
		Cursor c = bd.rawQuery("SELECT * FROM OBJ_PARTIDO WHERE par_tor = "
				+ id_torneo, null);
		if (c.moveToFirst()) {
			c.close();
			return true;
		} else {
			c.close();
			return false;
		}
	}

	//Este método elimina los partidos y todos sus datos del torneo que se pasa como parámetro 
	public void eliminarPartidosTorneo(int id_torneo) {
		bd.beginTransaction();
		try {
			bd.execSQL("DELETE FROM " + REL_JUG_MINUTOS_PART
					+ " WHERE jmp_par IN (SELECT par_id FROM " + OBJ_PARTIDO
					+ " WHERE par_tor = " + id_torneo + ")");
			bd.execSQL("DELETE FROM " + OBJ_INCIDENCIA_FUTBOL
					+ " WHERE inf_id IN (SELECT jfp_inf FROM "
					+ REL_JUG_FUTBOL_PART
					+ " WHERE jfp_par IN (SELECT par_id FROM " + OBJ_PARTIDO
					+ " WHERE par_tor = " + id_torneo + "))");
			bd.execSQL("DELETE FROM " + REL_JUG_FUTBOL_PART
					+ " WHERE jfp_par IN (SELECT par_id FROM " + OBJ_PARTIDO
					+ " WHERE par_tor = " + id_torneo + ")");
			bd.execSQL("DELETE FROM " + REL_EQUIPO_PARTIDO
					+ " WHERE ejp_par IN (SELECT par_id FROM " + OBJ_PARTIDO
					+ " WHERE par_tor = " + id_torneo + ")");
			bd.execSQL("DELETE FROM " + REL_JUGADOR_PARTIDO
					+ " WHERE jjp_par IN (SELECT par_id FROM " + OBJ_PARTIDO
					+ " WHERE par_tor = " + id_torneo + ")");
			bd.execSQL("DELETE FROM " + OBJ_PARTIDO + " WHERE par_tor = "
					+ id_torneo);
			bd.setTransactionSuccessful();
			bd.endTransaction();
		} catch (Exception e) {
			bd.endTransaction();
		}
	}

	//Este método devuelve una lista con los equipos que disputan la jornada y el torneo que se pasan ocmo parámetros
	public ArrayList<Integer> obtenerEquiposJornada(int id_torneo, int jor) {
		ArrayList<Integer> lista = new ArrayList<Integer>();
		Cursor c = bd.rawQuery("SELECT ejp_equ FROM " + REL_EQUIPO_PARTIDO
				+ ", " + OBJ_PARTIDO + " WHERE par_tor = " + id_torneo
				+ " AND par_id = ejp_par AND par_des LIKE '%" + jor
				+ "%' ORDER BY ejp_par", null);
		if (c.moveToFirst()) {
			do {
				lista.add(c.getInt(0));
			} while (c.moveToNext());
		}
		c.close();
		return lista;
	}

	//Este método devuelve el resultado de un equipo para la jornada del torneo pasado como parámetro
	public int obtenerResultado(int id_torneo, int id_equ, int num_jornada) {
		int res = 0;
		Cursor c = bd.rawQuery("SELECT ejp_res FROM " + REL_EQUIPO_PARTIDO
				+ ", " + OBJ_PARTIDO + " WHERE par_des LIKE '%" + num_jornada
				+ "%' AND par_tor = " + id_torneo
				+ " AND par_id = ejp_par AND ejp_equ = " + id_equ, null);
		if (c.moveToFirst()) {
			res = c.getInt(0);
		}
		c.close();
		return res;
	}

	//Este método el número de partidos que tiene el torneo pasado por parámetro
	public int obtenerFilasTorneo(int id_torneo) {
		int filas = 0;
		Cursor c = bd.rawQuery("SELECT COUNT(*) FROM " + OBJ_PARTIDO + ", "
				+ REL_EQUIPO_PARTIDO + " WHERE par_id = ejp_par AND par_tor = "
				+ id_torneo, null);
		if (c.moveToFirst()) {
			filas = c.getInt(0);
		}
		c.close();
		return filas;
	}

	//Este método devuelve el ID del partido que disputan los equipos pasados por parámetro
	public int obtenerIDPartido(int id_torneo, int equipoLocal, int equipoVisit) {
		int id_part = 0;
		Cursor c = bd
				.rawQuery(
						"SELECT ejp1.ejp_par FROM "
								+ REL_EQUIPO_PARTIDO
								+ " AS ejp1, "
								+ REL_EQUIPO_PARTIDO
								+ " AS ejp2, "
								+ OBJ_PARTIDO
								+ " WHERE ejp1.ejp_equ = "
								+ equipoLocal
								+ " AND ejp2.ejp_equ = "
								+ equipoVisit
								+ " AND ejp1.ejp_par = ejp2.ejp_par AND ejp1.ejp_par = par_id AND ejp2.ejp_par = par_id AND par_tor = "
								+ id_torneo, null);
		if (c.moveToFirst()) {
			id_part = c.getInt(0);
		}
		c.close();
		return id_part;
	}

	//Este método devuelve una lista de Jugadores pertenecientes al equipo pasado como parámetro
	public ArrayList<Jugador> obtenerJugadoresEquipo(int id_equipo) {
		ArrayList<Jugador> lista = new ArrayList<Jugador>();
		Cursor c = bd.rawQuery("SELECT jug.*, equ.equ_nom AS equipo FROM "
				+ OBJ_JUGADOR + " AS jug, " + OBJ_EQUIPO + " AS equ "
				+ "WHERE jug.jug_equ = equ.equ_id AND jug.jug_equ = "
				+ id_equipo + " ORDER BY jug.jug_equ", null);
		if (c.moveToFirst()) {
			do {
				lista.add(new Jugador(c.getInt(0), c.getString(1), c
						.getString(2), c.getString(3), c.getInt(4),
						c.getInt(5), c.getInt(6)));
			} while (c.moveToNext());
		}
		c.close();
		return lista;
	}

	//Este método inserta los minutos jugados por los jugadores en un partido
	//Recibe
	/* id_partido: ID del partido sobre el que se van a guardar los minutos
	 * datosJugador: vector que contiene los IDs de los jugadores para los que se va a guardar información
	 * datosJugadorMinuto: vector similar a datosJugador que contiene los minutos jugados por cada jugador
	 * lista_jugadores: lista con todos los jugadores pertenecientes al equipo
	 */
	public void insertarMinutosJugador(int id_partido, int[] datosJugador, int[] datosJugadorMinuto, ArrayList<Jugador> lista_jugadores) {
		bd.beginTransaction();
		try {
			String ids = "";
			Iterator<Jugador> it = lista_jugadores.iterator();
			while (it.hasNext()) {
				Jugador j = it.next();
				ids = ids + j.getId();
				if (it.hasNext())
					ids = ids + ",";
			}
			
			bd.execSQL("DELETE FROM " + REL_JUG_MINUTOS_PART
					+ " WHERE jmp_par = " + id_partido + " AND jmp_jug IN ("
					+ ids + ")");
			for (int i = 0; i < datosJugador.length; i++) {
				if (datosJugador[i] != -1) {
					bd.execSQL("INSERT INTO " + REL_JUG_MINUTOS_PART
							+ " VALUES(" + datosJugador[i] + ", " + id_partido
							+ ", " + datosJugadorMinuto[i] + ")");
				}
			}
			bd.setTransactionSuccessful();
			bd.endTransaction();
		} catch (Exception e) {
			bd.endTransaction();
		}
	}

	//Este método registra todas las incidencias de un partido para los jugadores pertenecientes a un equipo
	public void insertarDatosPartidoJugador(int id_partido, ArrayList<Integer> datosGolJugador, ArrayList<Integer> datosMinutoGolJugador, ArrayList<Integer> datosTarjetaJugador, ArrayList<Integer> datosMinutoTarjetaJugador, ArrayList<Integer> datosTarjetaColorJugador, ArrayList<Jugador> lista_jugadores) {
		bd.beginTransaction();
		try {
			String ids = "";
			Iterator<Jugador> it = lista_jugadores.iterator();
			while (it.hasNext()) {
				Jugador j = it.next();
				ids = ids + j.getId();
				if (it.hasNext())
					ids = ids + ",";
			}
			bd.execSQL("DELETE FROM " + OBJ_INCIDENCIA_FUTBOL
					+ " WHERE inf_id IN (SELECT jfp_inf FROM "
					+ REL_JUG_FUTBOL_PART + " WHERE jfp_par = " + id_partido
					+ " AND jfp_jug IN (" + ids + "))");
			bd.execSQL("DELETE FROM " + REL_JUG_FUTBOL_PART
					+ " WHERE jfp_par = " + id_partido + " AND jfp_jug IN ("
					+ ids + ")");
			for (int i = 0; i < datosGolJugador.size(); i++) {
				if (datosGolJugador.get(i) != -1) {
					int id_inf = obtenerIDIncidenciaFutbolMAX();
					bd.execSQL("INSERT INTO " + OBJ_INCIDENCIA_FUTBOL
							+ " VALUES(" + id_inf + ", 1, "
							+ datosMinutoGolJugador.get(i) + ", 0)");
					bd.execSQL("INSERT INTO " + REL_JUG_FUTBOL_PART
							+ " VALUES(" + datosGolJugador.get(i) + ", "
							+ id_partido + ", " + id_inf + ")");
				}
			}

			for (int i = 0; i < datosTarjetaJugador.size(); i++) {
				if (datosTarjetaJugador.get(i) != -1) {
					int id_inf = obtenerIDIncidenciaFutbolMAX();
					bd.execSQL("INSERT INTO " + OBJ_INCIDENCIA_FUTBOL
							+ " VALUES(" + id_inf + ", 2, "
							+ datosMinutoTarjetaJugador.get(i) + ", "
							+ datosTarjetaColorJugador.get(i) + ")");
					bd.execSQL("INSERT INTO " + REL_JUG_FUTBOL_PART
							+ " VALUES(" + datosTarjetaJugador.get(i) + ", "
							+ id_partido + ", " + id_inf + ")");
				}
			}

			bd.setTransactionSuccessful();
			bd.endTransaction();
		} catch (Exception e) {
			bd.endTransaction();
		}
	}

	//Este método devuelve el siguiente ID disponible para una incidencia de fútbol
	public int obtenerIDIncidenciaFutbolMAX() {
		String consulta = "SELECT MAX(inf_id) FROM " + OBJ_INCIDENCIA_FUTBOL;
		Cursor c = bd.rawQuery(consulta, null);
		if (c.moveToFirst()) {
			int num = c.getInt(0);
			c.close();
			return num + 1;
		} else {
			c.close();
			return 0;
		}
	}

	//Este método almacena el resultado para el equipo de un partido
	public void insertarResultado(int id_partido, int equipo, int res) {
		bd.beginTransaction();
		try {
			bd.execSQL("DELETE FROM " + REL_EQUIPO_PARTIDO
					+ " WHERE ejp_equ = " + equipo + " AND ejp_par = "
					+ id_partido);
			bd.execSQL("INSERT INTO " + REL_EQUIPO_PARTIDO + " VALUES("
					+ equipo + ", " + id_partido + ", " + res + ")");
			bd.setTransactionSuccessful();
			bd.endTransaction();
		} catch (Exception e) {
			bd.endTransaction();
		}
	}

	//Este método de una lista con los minutos disputados por jugadores de un equipo en un partido
	public ArrayList<MinutosJugador> obtenerMinutosJugadores(int equipo, int id_partido) {
		ArrayList<Jugador> lista_jugador = obtenerJugadoresEquipo(equipo);
		String ids = "";
		Iterator<Jugador> it = lista_jugador.iterator();
		while (it.hasNext()) {
			Jugador j = it.next();
			ids = ids + j.getId();
			if (it.hasNext())
				ids = ids + ",";
		}

		ArrayList<MinutosJugador> lista_minutos = new ArrayList<MinutosJugador>();
		Cursor c = bd.rawQuery(
				"SELECT * FROM " + REL_JUG_MINUTOS_PART + " WHERE jmp_jug IN ("
						+ ids + ") AND jmp_par = " + id_partido, null);
		if (c.moveToFirst()) {
			do {
				lista_minutos.add(new MinutosJugador(c.getInt(0), c.getInt(1),
						c.getInt(2)));
			} while (c.moveToNext());
		}
		c.close();
		return lista_minutos;
	}

	//Este método devuelve una lista de IncidenciaFutbol con los goles marcados por los jugadores de un equipo en un partido
	public ArrayList<IncidenciaFutbol> obtenerGolesJugador(int id_equipo, int id_partido) {
		ArrayList<Jugador> lista_jugadores = obtenerJugadoresEquipo(id_equipo);
		ArrayList<IncidenciaFutbol> lista = new ArrayList<IncidenciaFutbol>();
		String ids = "";
		Iterator<Jugador> it = lista_jugadores.iterator();
		while (it.hasNext()) {
			ids = ids + it.next().getId();
			if (it.hasNext())
				ids = ids + ",";
		}
		Cursor c = bd.rawQuery("SELECT inf.*, jfp.jfp_jug FROM "
				+ REL_JUG_FUTBOL_PART + " AS jfp, " + OBJ_INCIDENCIA_FUTBOL
				+ " AS inf "
				+ "WHERE jfp.jfp_inf = inf.inf_id AND jfp.jfp_jug IN (" + ids
				+ ") " + "AND jfp.jfp_par = " + id_partido
				+ " AND inf.inf_tip = 1 ORDER BY inf.inf_min ASC", null);
		if (c.moveToFirst()) {
			do {
				lista.add(new IncidenciaFutbol(c.getInt(0), c.getInt(1), c
						.getInt(2), c.getInt(3), c.getInt(4)));
			} while (c.moveToNext());
		}
		c.close();
		return lista;
	}

	//Este método devuelve una lista de IncidenciaFutbol con las tarjetas mostradas a los jugadores de un equipo en un partido
	public ArrayList<IncidenciaFutbol> obtenerTarjetasJugador(int id_equipo, int id_partido) {
		ArrayList<Jugador> lista_jugadores = obtenerJugadoresEquipo(id_equipo);
		ArrayList<IncidenciaFutbol> lista = new ArrayList<IncidenciaFutbol>();
		String ids = "";
		Iterator<Jugador> it = lista_jugadores.iterator();
		while (it.hasNext()) {
			ids = ids + it.next().getId();
			if (it.hasNext())
				ids = ids + ",";
		}

		Cursor c = bd.rawQuery("SELECT inf.*, jfp.jfp_jug FROM "
				+ REL_JUG_FUTBOL_PART + " AS jfp, " + OBJ_INCIDENCIA_FUTBOL
				+ " AS inf "
				+ "WHERE jfp.jfp_inf = inf.inf_id AND jfp.jfp_jug IN (" + ids
				+ ") " + "AND jfp.jfp_par = " + id_partido
				+ " AND inf.inf_tip = 2 ORDER BY inf.inf_min", null);
		if (c.moveToFirst()) {
			do {
				lista.add(new IncidenciaFutbol(c.getInt(0), c.getInt(1), c
						.getInt(2), c.getInt(3), c.getInt(4)));
			} while (c.moveToNext());
		}
		c.close();
		return lista;
	}

	// TENIS
	// TORNEOS
	
	//Este método devuelve una lista con los torneos de tenis ordenados por el parámetro de entrada
	public ArrayList<TorneoTenis> obtenerTorneosTenis(String orden) {
		if (orden.length() > 0)
			orden = " ORDER BY REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(LOWER("+orden+"),'á','a'), 'é','e'),'í','i'),'ó','o'),'ú','u'),'Á','A'),'É','E'),'Í','I'),'Ó','O'),'Ú','U')";
		ArrayList<TorneoTenis> lista = new ArrayList<TorneoTenis>();
		consulta = "SELECT *, (SELECT COUNT(*) FROM "
				+ REL_JUGADOR_TORNEO
				+ " AS jit WHERE jit.jit_tor = tor.tor_id) AS numJugadores FROM "
				+ OBJ_TORNEO
				+ " AS tor WHERE tor.tor_dep = 2 GROUP BY tor.tor_id" + orden;
		Cursor c = bd.rawQuery(consulta, null);
		if (c.moveToFirst()) {
			do {
				lista.add(new TorneoTenis(c.getInt(0), c.getString(1), c
						.getString(2), c.getString(3), c.getString(4), c
						.getInt(5), c.getInt(6)));
			} while (c.moveToNext());
		}
		c.close();
		return lista;
	}

	//Este método devuelve el torneo de tenis cuyo ID corresponde con el parámetro de entrada
	public TorneoTenis obtenerTorneoTenis(int id_torneo) {
		TorneoTenis t = new TorneoTenis();
		consulta = "SELECT *, (SELECT COUNT(*) FROM "
				+ REL_JUGADOR_TORNEO
				+ " AS jit WHERE jit.jit_tor = tor.tor_id) AS numJugadores FROM "
				+ OBJ_TORNEO + " AS tor WHERE tor.tor_id = " + id_torneo;
		Cursor c = bd.rawQuery(consulta, null);
		if (c.moveToFirst())
			t = new TorneoTenis(c.getInt(0), c.getString(1), c.getString(2),
					c.getString(3), c.getString(4), c.getInt(5), c.getInt(6));
		c.close();
		return t;
	}

	//Este método devuelve los datos de un torneo correspondiente al partido recibido como parámetro
	public TorneoTenis obtenerTorneoTenisPorPartido(int id_partido) {
		TorneoTenis t = new TorneoTenis();
		consulta = "SELECT *, (SELECT COUNT(*) FROM "
				+ REL_JUGADOR_TORNEO
				+ " AS jit WHERE jit.jit_tor = tor.tor_id) AS numJugadores FROM "
				+ OBJ_TORNEO + " AS tor, " + OBJ_PARTIDO
				+ " AS par WHERE tor.tor_id = par.par_tor AND par_id = "
				+ id_partido;
		Cursor c = bd.rawQuery(consulta, null);
		if (c.moveToFirst())
			t = new TorneoTenis(c.getInt(0), c.getString(1), c.getString(2),
					c.getString(3), c.getString(4), c.getInt(5), c.getInt(6));
		c.close();
		return t;
	}

	//Este método modifica los datos del torneo que recibe como parámetro
	//Devuelve 1 en caso de éxito y 0 si ocurre algún fallo
	public int modificarTorneoTenis(TorneoTenis t) {
		try {
			String consulta = "UPDATE " + OBJ_TORNEO + " SET tor_nom = '"
					+ t.getNom() + "', tor_lug = '" + t.getLug()
					+ "', tor_fin = '" + t.getFin() + "', tor_ffi = '"
					+ t.getFfi() + "' WHERE tor_id = " + t.getId();
			bd.execSQL(consulta);
			return 1;
		} catch (Exception e) {
			return 0;
		}
	}

	// JUGADORES
	//Este método devuelve una lista con los IDs de los jugadores qu disputan el torneo pasado como parámetro
	public ArrayList<Integer> obtenerIDJugadoresDeTorneo(int id_torneo) {
		ArrayList<Integer> lista = new ArrayList<Integer>();
		Cursor c = bd.rawQuery("SELECT jit_jug FROM " + REL_JUGADOR_TORNEO
				+ " WHERE jit_tor = " + id_torneo, null);
		if (c.moveToFirst()) {
			do {
				lista.add(c.getInt(0));
			} while (c.moveToNext());
		}
		c.close();
		return lista;
	}

	//Este método devuelve una cadena con el apodo del jugador que recibe como parámetro
	public String obtenerApodoJugador(int id_jugador) {
		String apodo = "";
		Cursor c = bd.rawQuery("SELECT jug_apo FROM " + OBJ_JUGADOR
				+ " WHERE jug_id = " + id_jugador, null);
		if (c.moveToFirst()) {
			apodo = c.getString(0);
			if (apodo.equals("")) {
				c = bd.rawQuery("SELECT jug_nom FROM " + OBJ_JUGADOR
						+ " WHERE jug_id = " + id_jugador, null);
				if (c.moveToFirst())
					apodo = c.getString(0);
			}
		}
		c.close();
		return apodo;
	}

	//Este método devuelve una lista con todos los jugadores que disputan un torneo
	public ArrayList<Jugador> obtenerJugadoresDeTorneo(int id_torneo, String orden, int dep) {
		ArrayList<Jugador> lista = new ArrayList<Jugador>();
		orden = "jug_nom";
		if (orden.length() > 0) {
			if (orden.equals("jug_equ")) {
				orden = " ORDER BY  REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(LOWER(equipo),'á','a'), 'é','e'),'í','i'),'ó','o'),'ú','u'),'Á','A'),'É','E'),'Í','I'),'Ó','O'),'Ú','U')";
			} else {
				orden = " ORDER BY  REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(LOWER("+orden+"),'á','a'), 'é','e'),'í','i'),'ó','o'),'ú','u'),'Á','A'),'É','E'),'Í','I'),'Ó','O'),'Ú','U')";
			}
		}

		consulta = "SELECT jug.* FROM " + OBJ_JUGADOR + " AS jug, "
				+ REL_JUGADOR_TORNEO + " AS jit "
				+ "WHERE jug.jug_id = jit.jit_jug AND jit.jit_tor = "
				+ id_torneo + " AND jug.jug_dep = " + dep + orden;
		Cursor c = bd.rawQuery(consulta, null);
		if (c.moveToFirst()) {
			do {
				lista.add(new Jugador(c.getInt(0), c.getString(1), c
						.getString(2), c.getString(3), c.getInt(4),
						c.getInt(5), c.getInt(6)));
			} while (c.moveToNext());
		}
		c.close();
		return lista;
	}

	//Este método insertar una lista de jugadores en un torneo
	//Devuelve 1 en caso de éxito y 0 si ocurre algún fallo
	public int insertarJugadoresEnTorneo(ArrayList<Integer> lista_jugadores, int id_torneo) {
		try {
			bd.beginTransaction();
			Iterator<Integer> it = lista_jugadores.iterator();
			while (it.hasNext()) {
				String consulta = "INSERT INTO " + REL_JUGADOR_TORNEO
						+ " VALUES (" + it.next() + ", " + id_torneo + ")";
				bd.execSQL(consulta);
			}
			bd.setTransactionSuccessful();
			bd.endTransaction();
			return 1;
		} catch (Exception e) {
			bd.endTransaction();
			return 0;
		}
	}

	//Este método devuelve una lista con jugadores filtrados por nombre y pertenecientes a un torneo
	public ArrayList<Jugador> obtenerJugadoresPorNombre(String buscar, int dep, int torneo) {
		ArrayList<Jugador> lista = new ArrayList<Jugador>();
		consulta = "SELECT * FROM " + OBJ_JUGADOR + " WHERE jug_nom LIKE '%"
				+ buscar + "%' AND jug_dep = " + dep
				+ " AND jug_id NOT IN(SELECT jit_jug FROM "
				+ REL_JUGADOR_TORNEO + " WHERE jit_tor = " + torneo + ")"
				+ " ORDER BY REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(LOWER(jug_nom),'á','a'), 'é','e'),'í','i'),'ó','o'),'ú','u'),'Á','A'),'É','E'),'Í','I'),'Ó','O'),'Ú','U')";
		Cursor c = bd.rawQuery(consulta, null);
		if (c.moveToFirst()) {
			do {
				lista.add(new Jugador(c.getInt(0), c.getString(1), c
						.getString(2), c.getString(3), c.getInt(4),
						c.getInt(5), c.getInt(6)));
			} while (c.moveToNext());
		}
		c.close();
		return lista;
	}

	//Este método elimina todos los jugadores de un torneo
	//Devuelve 1 en caso de éxito y 0 si ocurre algún fallo
	public int eliminarJugadoresTorneo(ArrayList<Integer> lista_jugadores, int id_torneo) {
		bd.beginTransaction();
		try {
			Iterator<Integer> it = lista_jugadores.iterator();
			while (it.hasNext()) {
				bd.execSQL("DELETE FROM " + REL_JUGADOR_TORNEO
						+ " WHERE jit_jug = " + it.next() + " AND jit_tor = " + id_torneo);
			}
			bd.setTransactionSuccessful();
			bd.endTransaction();
			return 1;
		} catch (Exception e) {
			bd.endTransaction();
			return 0;
		}
	}

	//Este método registra los partidos de la primera ronda de un torneo de tenis o pádel
	public void insertarPartidosTorneoTenis(int id_torneo, ArrayList<Jugador> lista_jugadores, int dep, int ronda) {
		int num_jugadores = lista_jugadores.size();
		int num_rondas = num_jugadores / 2;
		Random rnd = new Random();
		for (int i = 0; i < num_rondas; i++) {
			int posicion = (int) (rnd.nextDouble() * num_jugadores);
			if (posicion != num_jugadores) {
				int id_partido = obtenerIDPartidoMax();
				Partido part = new Partido(id_partido, dep, "", "", "Ronda "
						+ (ronda) + "", id_torneo, 0);
				crearPartido(part);
				Jugador j1 = new Jugador();
				Jugador j2 = new Jugador();
				j1 = lista_jugadores.get(posicion);
				lista_jugadores.remove(posicion);
				num_jugadores--;
				posicion = (int) (rnd.nextDouble() * num_jugadores);
				j2 = lista_jugadores.get(posicion);
				lista_jugadores.remove(posicion);
				num_jugadores--;
				insertarJugadorEnPartido(j1.getId(), id_partido, ronda);
				insertarJugadorEnPartido(j2.getId(), id_partido, ronda);
			}
		}
	}

	//Este método insertar los partidos de una ronda distinta a la primera de un torneo de tenis o pádel
	public void insertarPartidosSiguienteRondaTenis(int id_torneo, ArrayList<Jugador> lista_jugadores, int dep, int ronda) {
		while (!(lista_jugadores.isEmpty())) {
			int id_partido = obtenerIDPartidoMax();
			Partido part = new Partido(id_partido, dep, "", "", "Ronda "
					+ (ronda) + "", id_torneo, 0);
			crearPartido(part);
			Jugador j1 = new Jugador();
			Jugador j2 = new Jugador();
			j1 = lista_jugadores.get(0);
			lista_jugadores.remove(0);
			j2 = lista_jugadores.get(0);
			lista_jugadores.remove(0);
			insertarJugadorEnPartido(j1.getId(), id_partido, ronda);
			insertarJugadorEnPartido(j2.getId(), id_partido, ronda);
		}
	}

	//Este método registra los jugadores que juegan un partido de una ronda
	private void insertarJugadorEnPartido(int id_jug, int id_partido, int ronda) {
		bd.execSQL("INSERT INTO " + REL_JUGADOR_PARTIDO + " VALUES(" + id_jug
				+ ", " + id_partido + ", 0, 0, 0, '" + obtenerRonda(ronda)
				+ "')");
		bd.execSQL("INSERT INTO " + REL_JUG_MINUTOS_PART + " VALUES(" + id_jug
				+ ", " + id_partido + ", 0)");
	}

	//Este método devuelve una cadena con la ronda correspondiente al ID que recibe como parámetro
	private String obtenerRonda(int ronda) {
		switch (ronda) {
		case 1:
			return "Final";
		case 2:
			return "Semifinales";
		case 3:
			return "1/4 Final";
		case 4:
			return "1/8 Final";
		case 5:
			return "1/16 Final";
		case 6:
			return "1/32 Final";
		case 7:
			return "Ronda 1";
		default:
			return "Ronda -";
		}
	}

	//Este método devuelve el número de rondas que tendrá un torneo de tenis o pádel en función del nº de jugadores que recibe como parámetro
	private int calcularRonda(int num_jugadores) {
		switch (num_jugadores) {
		case 2:
			return 1;
		case 4:
			return 2;
		case 8:
			return 3;
		case 16:
			return 4;
		case 32:
			return 5;
		case 64:
			return 6;
		case 128:
			return 7;
		default:
			return 0;
		}
	}

	//Este método devuelve una lista con los jugadores que disputan la ronda de un torneo
	public ArrayList<Integer> obtenerJugadoresRonda(int id_torneo, int num_ronda) {
		ArrayList<Integer> lista = new ArrayList<Integer>();
		Cursor c = bd.rawQuery("SELECT jjp_jug FROM " + REL_JUGADOR_PARTIDO
				+ ", " + OBJ_PARTIDO + " WHERE par_tor = " + id_torneo
				+ " AND par_id = jjp_par AND par_des LIKE 'Ronda " + num_ronda
				+ "' ORDER BY jjp_par", null);
		if (c.moveToFirst()) {
			do {
				lista.add(c.getInt(0));
			} while (c.moveToNext());
		}
		c.close();
		return lista;
	}

	//Este método devuelve los datos de un jugador cuyo ID y deporte corresponden a los parámetros de entrada
	public Jugador obtenerJugador(int jug, int dep) {
		Jugador j = new Jugador();
		consulta = "SELECT jug.*, equ.equ_nom AS equipo FROM " + OBJ_JUGADOR
				+ " AS jug, " + OBJ_EQUIPO + " AS equ "
				+ "WHERE jug.jug_equ = equ.equ_id AND jug.jug_dep = " + dep
				+ " AND jug.jug_id = " + jug;
		Cursor c = bd.rawQuery(consulta, null);
		if (c.moveToFirst())
			j = new Jugador(c.getInt(0), c.getString(1), c.getString(2),
					c.getString(3), c.getInt(4), c.getInt(5), c.getInt(6));
		c.close();
		return j;
	}

	//Este método devuelve el ID del partido de tenis o pádel correpondiente a un torneo y en el que juegan los dos jugadores que recibe como parámetro de entrada
	public int obtenerIDPartidoTenis(int id_torneo, int jug1, int jug2) {
		int id_part = 0;
		Cursor c = bd
				.rawQuery(
						"SELECT jjp1.jjp_par FROM "
								+ REL_JUGADOR_PARTIDO
								+ " AS jjp1, "
								+ REL_JUGADOR_PARTIDO
								+ " AS jjp2, "
								+ OBJ_PARTIDO
								+ " WHERE jjp1.jjp_jug = "
								+ jug1
								+ " AND jjp2.jjp_jug = "
								+ jug2
								+ " AND jjp1.jjp_par = jjp2.jjp_par AND jjp1.jjp_par = par_id AND jjp2.jjp_par = par_id AND par_tor = "
								+ id_torneo, null);
		if (c.moveToFirst()) {
			id_part = c.getInt(0);
		}
		c.close();
		return id_part;
	}

	//Este método devuelve los datos de un jugador en un partido de tenis o pádel
	public DatosJugadorPartido obtenerDatosJugadorPartido(int id_jug, int id_partido) {
		DatosJugadorPartido djp = new DatosJugadorPartido();
		Cursor c = bd.rawQuery(
				"SELECT * FROM " + REL_JUGADOR_PARTIDO + " WHERE jjp_jug = "
						+ id_jug + " AND jjp_par = " + id_partido, null);
		if (c.moveToFirst())
			djp = new DatosJugadorPartido(c.getInt(0), c.getInt(1),
					c.getInt(2), c.getInt(3), c.getInt(4), c.getString(5));
		c.close();
		return djp;
	}

	//Este método devuelve las IDs de los  jugadores que disputan el partido pasado como parámetro
	public int[] obtenerJugadoresPartido(int id_partido) {
		int[] id_jugadores = new int[2];
		Cursor c = bd.rawQuery("SELECT jjp_jug FROM " + REL_JUGADOR_PARTIDO
				+ " WHERE jjp_par = " + id_partido, null);
		if (c.moveToFirst()) {
			id_jugadores[0] = c.getInt(0);
			c.moveToNext();
			id_jugadores[1] = c.getInt(0);
		}
		c.close();
		return id_jugadores;
	}

	//Este método devuelve el nombre del jugador cuyo ID corresponde con el parámetro de entrada
	public CharSequence obtenerNombreJugador(int jug) {
		String nombre = "";
		Cursor c = bd.rawQuery("SELECT jug_nom FROM " + OBJ_JUGADOR
				+ " WHERE jug_id = " + jug, null);
		if (c.moveToFirst()) {
			nombre = c.getString(0);
		}
		c.close();
		return nombre;
	}

	//Este método devuelve el resultado de los 3 sets del jugador y partido pasado como parámetros
	public int[] obtenerResultadoTenis(int id_partido, int jug) {
		int[] res = { 0, 0, 0 };
		Cursor c = bd.rawQuery("SELECT jjp_st1,  jjp_st2, jjp_st3 FROM "
				+ REL_JUGADOR_PARTIDO + " WHERE jjp_par = " + id_partido
				+ " AND jjp_jug = " + jug, null);
		if (c.moveToFirst()) {
			res[0] = c.getInt(0);
			res[1] = c.getInt(1);
			res[2] = c.getInt(2);
		}
		c.close();
		return res;
	}

	//Este método devuelve los datos de un partido cuyo ID corresponde con el dato de entrada
	public Partido obtenerPartido(int id_par) {
		Partido p = new Partido();
		Cursor c = bd.rawQuery("SELECT * FROM " + OBJ_PARTIDO
				+ " WHERE par_id = " + id_par, null);
		if (c.moveToFirst())
			p = new Partido(c.getInt(0), c.getInt(1), c.getString(2),
					c.getString(3), c.getString(4), c.getInt(5), c.getInt(6));
		c.close();
		return p;
	}

	//Este método actualiza los datos de un partido, para el jugador pasado como parámetros de entrada
	public void guardarResultadoTenis(int id_partido, int jug, int s1, int s2, int s3) {
		bd.execSQL("UPDATE " + REL_JUGADOR_PARTIDO + " SET jjp_st1 = " + s1
				+ ", jjp_st2 = " + s2 + ", jjp_st3 = " + s3
				+ " WHERE jjp_par = " + id_partido + " AND jjp_jug = " + jug);
	}

	//Este método guarda los datos de un partido de tenis o pádel
	public void guardarDatosPartidoTenis(int id_partido, String fecha, String hora) {
		bd.execSQL("UPDATE " + OBJ_PARTIDO + " SET par_fec = '" + fecha
				+ "', par_hor = '" + hora + "' WHERE par_id = " + id_partido);
	}

	//Este método inserta los minutos que un jugador ha disputado en un partido
	public void insertarMinutosJugadorTenis(int id_partido, int jug, int duracion) {
		bd.beginTransaction();
		try {
			bd.execSQL("UPDATE " + REL_JUG_MINUTOS_PART + " SET jmp_min = "
					+ duracion + " WHERE jmp_jug = " + jug + " AND jmp_par = "
					+ id_partido);
			bd.setTransactionSuccessful();
			bd.endTransaction();
		} catch (Exception e) {
			bd.endTransaction();
		}
	}

	//Este método devuelve la duración del partido cuyo ID corresponde con el parámetro de entrada
	public String obtenerDuracionPartidoTenis(int id_partido) {
		String duracion = "";
		Cursor c = bd.rawQuery("SELECT jmp_min FROM " + REL_JUG_MINUTOS_PART
				+ " WHERE jmp_par = " + id_partido, null);
		if (c.moveToFirst())
			duracion = c.getInt(0) + "";
		c.close();
		return duracion;
	}

	//Este método devuelve una lista con los IDs de los partidos de una ronda en un torneo
	public ArrayList<Integer> obtenerPartidosRonda(int id_torneo, int ronda) {
		ArrayList<Integer> lista = new ArrayList<Integer>();
		Cursor c = bd.rawQuery("SELECT par_id FROM " + OBJ_PARTIDO
				+ " WHERE par_tor = " + id_torneo + " AND par_des LIKE 'Ronda "
				+ ronda + "'", null);
		if (c.moveToFirst()) {
			do {
				lista.add(c.getInt(0));
			} while (c.moveToNext());
		}
		c.close();
		return lista;
	}

	//Este método devuelve los jugadores que han ganado su partido en la ronda del torneo pasados como parámetros de entrada
	public ArrayList<Jugador> obtenerJugadoresGanadoresDeRonda(int id_torneo,
			int ronda, int dep) {
		ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
		ArrayList<Integer> partidos = new ArrayList<Integer>();
		DatosJugadorPartido djp1 = new DatosJugadorPartido();
		DatosJugadorPartido djp2 = new DatosJugadorPartido();
		// Primero se necesitan los partidos de ese torneo y de la ronda en concreto
		Cursor c = bd.rawQuery("SELECT par_id FROM " + OBJ_PARTIDO
				+ " WHERE par_tor = " + id_torneo + " AND par_des LIKE 'Ronda "
				+ ronda + "'", null);
		if (c.moveToFirst()) {
			do {
				partidos.add(c.getInt(0));
			} while (c.moveToNext());
		}
		c.close();
		// Para cada partido se comprueba quien es el ganador
		Iterator<Integer> it = partidos.iterator();
		while (it.hasNext()) {
			int par = it.next();
			c = bd.rawQuery("SELECT * FROM " + REL_JUGADOR_PARTIDO
					+ " WHERE jjp_par = " + par, null);
			int comprobarGanador = 0; // Si al terminar de comprobar los resultados es positivo, significará que el ganados es el primer jugador. Si es negativo, al contrario.
			if (c.moveToFirst()) {
				djp1 = new DatosJugadorPartido(c.getInt(0), c.getInt(1), c.getInt(2), c.getInt(3), c.getInt(4), c.getString(5));
				c.moveToNext();
				djp2 = new DatosJugadorPartido(c.getInt(0), c.getInt(1), c.getInt(2), c.getInt(3), c.getInt(4), c.getString(5));
				
				if ((djp1.getSt1() > djp2.getSt1()))
					comprobarGanador++;
				else
					comprobarGanador--;
				
				if ((djp1.getSt2() > djp2.getSt2()))
					comprobarGanador++;
				else
					comprobarGanador--;
				
				if ((djp1.getSt3() > djp2.getSt3()))
					comprobarGanador++;
				else
					comprobarGanador--;

				if (comprobarGanador > 0) {
					Jugador j = obtenerJugador(djp1.getJug(), dep);
					jugadores.add(j);
				} else {
					Jugador j = obtenerJugador(djp2.getJug(), dep);
					jugadores.add(j);
				}
			}
			c.close();
		}
		return jugadores;
	}

	//Este método elimina los registros correspondientes a los partidos de un torneo en una ronda concreta
	public void eliminarPartidosRonda(int id_torneo, int ronda) {
		bd.beginTransaction();
		try {
			bd.execSQL("DELETE FROM " + REL_JUG_MINUTOS_PART
					+ " WHERE jmp_par IN (SELECT par_id FROM " + OBJ_PARTIDO
					+ " WHERE par_tor = " + id_torneo
					+ " AND par_des LIKE 'Ronda " + ronda + "')");
			bd.execSQL("DELETE FROM " + REL_JUGADOR_PARTIDO
					+ " WHERE jjp_par IN (SELECT par_id FROM " + OBJ_PARTIDO
					+ " WHERE par_tor = " + id_torneo
					+ " AND par_des LIKE 'Ronda " + ronda + "')");
			bd.execSQL("DELETE FROM " + OBJ_PARTIDO + " WHERE par_tor = "
					+ id_torneo + " AND par_des LIKE 'Ronda " + ronda + "'");
			bd.setTransactionSuccessful();
			bd.endTransaction();
		} catch (Exception e) {
			bd.endTransaction();
		}
	}

	// BALONCESTO
	// TORNEOS
	
	//Este método devuelve una lista con las posiciones de baloncesto
	public ArrayList<PosBaloncesto> obtenerPosicionesBaloncesto() {
		ArrayList<PosBaloncesto> lista = new ArrayList<PosBaloncesto>();
		String consulta = "SELECT * FROM " + TIP_POSICION_BALONCESTO;
		Cursor c = bd.rawQuery(consulta, null);
		if (c.moveToFirst()) {
			do {
				lista.add(new PosBaloncesto(c.getInt(0), c.getString(1), c
						.getString(2)));
			} while (c.moveToNext());
		}
		c.close();
		return lista;
	}

	//Este método devuelve los datos de una posición concreta de baloncesto
	public PosBaloncesto obtenerPosicionBaloncesto(int id) {
		PosBaloncesto pos = new PosBaloncesto();
		String consulta = "SELECT * FROM " + TIP_POSICION_BALONCESTO
				+ " WHERE tpbc_id = " + id;
		Cursor c = bd.rawQuery(consulta, null);
		if (c.moveToFirst())
			pos = new PosBaloncesto(c.getInt(0), c.getString(1), c.getString(2));
		c.close();
		return pos;
	}

	//Este método devuelve una lista con los datos de un jugador en el partido pasado como parámetro de entrada
	public ArrayList<DatosJugadorPartidoBC> obtenerDatosJugadorPartidoBC(int id_partido, ArrayList<Jugador> lista_jugadores) {
		ArrayList<DatosJugadorPartidoBC> lista = new ArrayList<DatosJugadorPartidoBC>();
		Iterator<Jugador> it = lista_jugadores.iterator();
		while (it.hasNext()) {
			Cursor c = bd.rawQuery("SELECT * FROM " + REL_JUG_BALONCESTO_PART
					+ " WHERE jbp_jug = " + it.next().getId()
					+ " AND jbp_par = " + id_partido, null);
			if (c.moveToFirst())
				lista.add(new DatosJugadorPartidoBC(c.getInt(0), c.getInt(1), c
						.getInt(2), c.getInt(3), c.getInt(4), c.getInt(5), c
						.getInt(6)));
			c.close();
		}
		return lista;
	}

	//Este método devuelve los minutos que un jugador ha disputado en un partido
	public int obtenerMinutosJugador(int id_partido, int id_jug) {
		int min = 0;
		Cursor c = bd.rawQuery(
				"SELECT jmp_min FROM " + REL_JUG_MINUTOS_PART
						+ " WHERE jmp_jug = " + id_jug + " AND jmp_par = "
						+ id_partido, null);
		if (c.moveToFirst())
			min = c.getInt(0);
		c.close();
		return min;
	}

	//Este método devuelve si un jugador ha sido MVP en un partido
	public boolean obtenerMVP(int id_partido, int id_jug) {
		Cursor c = bd.rawQuery("SELECT jbp_mvp FROM " + REL_JUG_BALONCESTO_PART
				+ " WHERE jbp_jug = " + id_jug + " AND jbp_par = " + id_partido,
				null);
		if (c.moveToFirst()) {
			if (c.getInt(0) == 1) {
				c.close();
				return true;
			}
		}
		c.close();
		return false;
	}

	//Este método inserta los datos de los jugadores que disputan un partido
	/* Recibe:
	 * id_partido: ID del partido para el cual se van a guardar los datos
	 * jugadores: vector que contiene los ID de los jugadores para los cuales se van a guardar datos
	 * id_jug_MVP: ID del jugador que se ha declarado MVP
	 * puntos: vector que contiene los puntos anotados por cada jugador de los que se van a guardar datos
	 * rebotes: vector que contiene los rebotes efectuados por cada jugador de los que se van a guardar datos
	 * tapones: vector que contiene los tapones efectuados por cada jugador de los que se van a guardar datos
	 * faltas: vector que contiene las faltas cometidas por cada jugador de los que se van a guardar datos
	 * lista_jugadores: lista que contiene todos los jugadores de un equipo
	 */
	public void insertarDatosPartidoJugadorBaloncesto(int id_partido, int[] jugadores, int id_jug_MVP, int[] puntos, int[] rebotes, int[] tapones, int[] faltas, ArrayList<Jugador> lista_jugadores) {
		bd.beginTransaction();
		try {
			String ids = "";
			Iterator<Jugador> it = lista_jugadores.iterator();
			while (it.hasNext()) {
				Jugador j = it.next();
				ids = ids + j.getId();
				if (it.hasNext())
					ids = ids + ",";
			}
			bd.execSQL("DELETE FROM " + REL_JUG_BALONCESTO_PART
					+ " WHERE jbp_jug IN (" + ids + ") AND jbp_par = "
					+ id_partido);
			for (int i = 0; i < 12; i++) {
				if (jugadores[i] != -1) {
					if (jugadores[i] == id_jug_MVP) {
						bd.execSQL("INSERT INTO " + REL_JUG_BALONCESTO_PART
								+ " VALUES(" + jugadores[i] + ","
								+ id_partido + ", 1," + puntos[i] + ","
								+ rebotes[i] + "," + tapones[i] + ","
								+ faltas[i] + ")");
					} else
						bd.execSQL("INSERT INTO " + REL_JUG_BALONCESTO_PART
								+ " VALUES(" + jugadores[i] + ","
								+ id_partido + ", 0," + puntos[i] + ","
								+ rebotes[i] + "," + tapones[i] + ","
								+ faltas[i] + ")");
				}
			}
			bd.setTransactionSuccessful();
			bd.endTransaction();
		} catch (Exception e) {
			bd.endTransaction();
		}
	}

	//Este método registra los datos de un partido
	public void insertarDatosPartido(int id_partido, String fecha, String hora) {
		bd.beginTransaction();
		try {
			bd.execSQL("UPDATE " + OBJ_PARTIDO + " SET par_fec = '" + fecha
					+ "', par_hor = '" + hora + "' WHERE par_id = "
					+ id_partido);
			bd.setTransactionSuccessful();
			bd.endTransaction();
		} catch (Exception e) {
			bd.endTransaction();
		}
	}

	// PÁDEL
	// TORNEOS
	
	//Este método devuelve una lista con los torneo de pádel ordenados por el parámetro de entrada
	public ArrayList<TorneoTenis> obtenerTorneosPadel(String orden) {
		if (orden.length() > 0)
			orden = " ORDER BY REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(LOWER("+orden+"),'á','a'), 'é','e'),'í','i'),'ó','o'),'ú','u'),'Á','A'),'É','E'),'Í','I'),'Ó','O'),'Ú','U')";
		ArrayList<TorneoTenis> lista = new ArrayList<TorneoTenis>();
		consulta = "SELECT *, (SELECT COUNT(*) FROM "
				+ REL_JUGADOR_TORNEO
				+ " AS jit WHERE jit.jit_tor = tor.tor_id) AS numJugadores FROM "
				+ OBJ_TORNEO
				+ " AS tor WHERE tor.tor_dep = 4 GROUP BY tor.tor_id" + orden;
		Cursor c = bd.rawQuery(consulta, null);
		if (c.moveToFirst()) {
			do {
				lista.add(new TorneoTenis(c.getInt(0), c.getString(1), c
						.getString(2), c.getString(3), c.getString(4), c
						.getInt(5), c.getInt(6)));
			} while (c.moveToNext());
		}
		c.close();
		return lista;
	}

	//Este método devuelve el torneo de pádel cuyo ID corresponde con el parámetro de entrada
	public TorneoTenis obtenerTorneoPadel(int id_torneo) {
		TorneoTenis t = new TorneoTenis();
		consulta = "SELECT *, (SELECT COUNT(*) FROM "
				+ REL_JUGADOR_TORNEO
				+ " AS jit WHERE jit.jit_tor = tor.tor_id) AS numJugadores FROM "
				+ OBJ_TORNEO + " AS tor WHERE tor.tor_id = " + id_torneo;
		Cursor c = bd.rawQuery(consulta, null);
		if (c.moveToFirst())
			t = new TorneoTenis(c.getInt(0), c.getString(1), c.getString(2),
					c.getString(3), c.getString(4), c.getInt(5), c.getInt(6));
		c.close();
		return t;
	}

	//Este método devuelve una lista de objetos IncidenciaBalonmano con los goles marcados por los jugadores de un equipo en un partido
	public ArrayList<IncidenciaBalonmano> obtenerGolesJugadorBalonmano(int id_equipo, int id_partido) {
		ArrayList<Jugador> lista_jugadores = obtenerJugadoresEquipo(id_equipo);
		ArrayList<IncidenciaBalonmano> lista = new ArrayList<IncidenciaBalonmano>();
		String ids = "";
		Iterator<Jugador> it = lista_jugadores.iterator();
		while (it.hasNext()) {
			ids = ids + it.next().getId();
			if (it.hasNext())
				ids = ids + ",";
		}

		Cursor c = bd.rawQuery("SELECT inb.*, jbmp.jbmp_jug FROM "
				+ REL_JUG_BALONMANO_PART + " AS jbmp, "
				+ OBJ_INCIDENCIA_BALONMANO + " AS inb "
				+ "WHERE jbmp.jbmp_inb = inb.inb_id AND jbmp.jbmp_jug IN ("
				+ ids + ") " + "AND jbmp.jbmp_par = " + id_partido
				+ " ORDER BY inb.inb_gol ASC", null);
		if (c.moveToFirst()) {
			do {
				lista.add(new IncidenciaBalonmano(c.getInt(0), c.getInt(1), c
						.getInt(2)));
			} while (c.moveToNext());
		}
		c.close();
		return lista;
	}

	//Este método almacena los datos de un partido de balonmano
	/* Recibe:
	 * id_partido: ID del partido sobre el que se van a guardar los datos
	 * datosGolJugador: lista que contiene los IDs de los jugadores han marcado
	 * datosGolMinutoJugador: lista que contiene los minutos en los que han marcado los jugadores
	 * lista_jugadores: lista que contiene todos los jugadores del equipo
	 */
	public void insertarDatosPartidoJugadorBalonmano(int id_partido, ArrayList<Integer> datosGolJugador, ArrayList<Integer> datosMinutoGolJugador, ArrayList<Jugador> lista_jugadores) {
		bd.beginTransaction();
		try {
			String ids = "";
			Iterator<Jugador> it = lista_jugadores.iterator();
			while (it.hasNext()) {
				Jugador j = it.next();
				ids = ids + j.getId();
				if (it.hasNext())
					ids = ids + ",";
			}
			bd.execSQL("DELETE FROM " + OBJ_INCIDENCIA_BALONMANO
					+ " WHERE inb_id IN (SELECT jbmp_inb FROM "
					+ REL_JUG_BALONMANO_PART + " WHERE jbmp_par = "
					+ id_partido + " AND jbmp_jug IN (" + ids + "))");
			bd.execSQL("DELETE FROM " + REL_JUG_BALONMANO_PART
					+ " WHERE jbmp_par = " + id_partido + " AND jbmp_jug IN ("
					+ ids + ")");
			for (int i = 0; i < datosGolJugador.size(); i++) {
				if (datosGolJugador.get(i) != -1) {
					int id_inb = obtenerIDIncidenciaBalonmanoMAX();
					bd.execSQL("INSERT INTO " + OBJ_INCIDENCIA_BALONMANO
							+ " VALUES(" + id_inb + ", "
							+ datosMinutoGolJugador.get(i) + ")");
					bd.execSQL("INSERT INTO " + REL_JUG_BALONMANO_PART
							+ " VALUES(" + datosGolJugador.get(i) + ", "
							+ id_partido + ", " + id_inb + ")");
				}
			}

			bd.setTransactionSuccessful();
			bd.endTransaction();
		} catch (Exception e) {
			bd.endTransaction();
		}
	}

	//Este método devuelve el siguiente ID disponible para IncidenciaBalonmano
	private int obtenerIDIncidenciaBalonmanoMAX() {
		String consulta = "SELECT MAX(inb_id) FROM " + OBJ_INCIDENCIA_BALONMANO;
		Cursor c = bd.rawQuery(consulta, null);
		if (c.moveToFirst()) {
			int num = c.getInt(0);
			c.close();
			return num + 1;
		} else {
			c.close();
			return 0;
		}
	}

	//Este método devuelve las posiciones de balonmano
	public ArrayList<PosBalonmano> obtenerPosicionesBalonmano() {
		ArrayList<PosBalonmano> lista = new ArrayList<PosBalonmano>();
		String consulta = "SELECT * FROM " + TIP_POSICION_BALONMANO;
		Cursor c = bd.rawQuery(consulta, null);
		if (c.moveToFirst()) {
			do {
				lista.add(new PosBalonmano(c.getInt(0), c.getString(1), c
						.getString(2)));
			} while (c.moveToNext());
		}
		c.close();
		return lista;
	}

	//Este método devuelve una lista con objetos Clasificacion, que formarán la clasificación de un torneo
	public ArrayList<Clasificacion> obtenerClasificacion(int id_torneo, int dep) {
		ArrayList<Clasificacion> lista = new ArrayList<Clasificacion>();
		// Para saber el número de jornadas disputadas
		int num_jugados = 0;
		Cursor c = bd.rawQuery("SELECT COUNT(DISTINCT par_des) FROM "
				+ OBJ_PARTIDO + " WHERE par_tor = " + id_torneo
				+ " AND par_cla = 1", null);
		if (c.moveToFirst())
			num_jugados = c.getInt(0);
		c.close();
		// Para saber los partidos que se han jugado
		ArrayList<Integer> lista_partidos = new ArrayList<Integer>();
		c = bd.rawQuery("SELECT par_id FROM " + OBJ_PARTIDO
				+ " WHERE par_tor = " + id_torneo + " AND par_cla = 1", null);
		if (c.moveToFirst()) {
			do {
				lista_partidos.add(c.getInt(0));
			} while (c.moveToNext());
		}
		c.close();
		// Obtengo los equipos del torneo
		ArrayList<Equipo> lista_equipos = obtenerEquiposDeTorneo(id_torneo, dep);
		Iterator<Equipo> it = lista_equipos.iterator();
		while (it.hasNext()) {
			Equipo e = it.next();
			int g = 0, em = 0, p = 0, gf = 0, gc = 0, j = 0;
			if (e.getId() != -1) {
				Iterator<Integer> ip = lista_partidos.iterator();
				while (ip.hasNext()) {
					int partido = ip.next();
					// Ahora de aquí saco con el getCount el numero de partidos ganados y recorriendo el cursor los goles a favor
					c = bd.rawQuery(
							"SELECT ejp1.ejp_res, (SELECT ejp2.ejp_res FROM "
									+ REL_EQUIPO_PARTIDO
									+ " AS ejp2 WHERE ejp2.ejp_equ <> ejp1.ejp_equ AND ejp2.ejp_par = ejp1.ejp_par) FROM "
									+ REL_EQUIPO_PARTIDO
									+ " AS ejp1 WHERE ejp1.ejp_equ = "
									+ e.getId()
									+ " AND ejp1.ejp_res > (SELECT ejp2.ejp_res FROM "
									+ REL_EQUIPO_PARTIDO
									+ " AS ejp2 WHERE ejp2.ejp_equ <> "
									+ e.getId()
									+ " AND ejp1.ejp_par = ejp2.ejp_par AND ejp2.ejp_equ <> -1)"
									+ " AND ejp1.ejp_par = " + partido, null);
					j = j + c.getCount();
					g = g + c.getCount();
					if (c.moveToFirst()) {
						do {
							gf = gf + c.getInt(0);
							gc = gc + c.getInt(1);
						} while (c.moveToNext());
					}
					c.close();
					// Ahora de aquí saco con el getCount el numero de partidos perdidos y recorriendo el cursor los goles en contra
					c = bd.rawQuery(
							"SELECT ejp1.ejp_res, (SELECT ejp2.ejp_res FROM "
									+ REL_EQUIPO_PARTIDO
									+ " AS ejp2 WHERE ejp2.ejp_equ <> ejp1.ejp_equ AND ejp2.ejp_par = ejp1.ejp_par) FROM "
									+ REL_EQUIPO_PARTIDO
									+ " AS ejp1 WHERE ejp1.ejp_equ <> "
									+ e.getId()
									+ " AND ejp1.ejp_res > (SELECT ejp2.ejp_res FROM "
									+ REL_EQUIPO_PARTIDO
									+ " AS ejp2 WHERE ejp2.ejp_equ = "
									+ e.getId()
									+ " AND ejp1.ejp_par = ejp2.ejp_par AND ejp2.ejp_equ <> -1)"
									+ " AND ejp1.ejp_par = " + partido, null);
					j = j + c.getCount();
					p = p + c.getCount();
					if (c.moveToFirst()) {
						do {
							gf = gf + c.getInt(1);
							gc = gc + c.getInt(0);
						} while (c.moveToNext());
					}
					c.close();
					// Ahora de aquí saco con el getCount el numero de partidos empatados
					c = bd.rawQuery(
							"SELECT ejp1.ejp_res, (SELECT ejp2.ejp_res FROM "
									+ REL_EQUIPO_PARTIDO
									+ " AS ejp2 WHERE ejp2.ejp_equ <> ejp1.ejp_equ AND ejp2.ejp_par = ejp1.ejp_par) FROM "
									+ REL_EQUIPO_PARTIDO
									+ " AS ejp1 WHERE ejp1.ejp_equ = "
									+ e.getId()
									+ " AND ejp1.ejp_res = (SELECT ejp2.ejp_res FROM "
									+ REL_EQUIPO_PARTIDO
									+ " AS ejp2 WHERE ejp2.ejp_equ <> "
									+ e.getId()
									+ " AND ejp1.ejp_par = ejp2.ejp_par AND ejp2.ejp_equ <> -1)"
									+ " AND ejp1.ejp_par = " + partido, null);
					j = j + c.getCount();
					em = em + c.getCount();
					if (c.moveToFirst()) {
						do {
							gf = gf + c.getInt(0);
							gc = gc + c.getInt(0);
						} while (c.moveToNext());
					}
					c.close();
				}
				// Almaceno los datos
				Clasificacion cla = new Clasificacion(e.getId(), j, g, em, p, gf, gc);
				lista.add(cla);
			}
		}
		return lista;
	}

	//Este método devolverá true si todos los partidos se han podido actualizar en la clasficación de una jornada en un torneo
	//Devolverá false si alguno de los resultado no es correcto
	public boolean actualizarPartidosClasificacion(int id_torneo, int jornada, int dep) {
		ArrayList<Integer> lista_equipos = obtenerEquiposJornada(id_torneo, jornada);
		Iterator<Integer> it = lista_equipos.iterator();
		while (it.hasNext()) {
			Equipo e1 = obtenerEquipo(it.next());
			Equipo e2 = obtenerEquipo(it.next());
			if (e1.getId() == 0 || e2.getId() == 0) {
			} else {
				if ((obtenerResultado(id_torneo, e1.getId(), jornada) == 0 && obtenerResultado(
						id_torneo, e2.getId(), jornada) == 0) && dep == 3) {
					return false;
				}
			}
		}
		bd.execSQL("UPDATE " + OBJ_PARTIDO
				+ " SET par_cla = 1 WHERE par_tor = " + id_torneo
				+ " AND par_des LIKE 'Jornada " + jornada + "'");
		return true;
	}

	//Este método devuelve una lista con la clasficación de goleadores de un torneo
	public ArrayList<Recuento> obtenerGoleadoresTorneo(int id_torneo) {
		ArrayList<Recuento> lista = new ArrayList<Recuento>();
		Cursor c = bd.rawQuery(
						"SELECT jfp_jug, COUNT(inf_tip) As Goles, (SELECT jug_equ FROM "
								+ OBJ_JUGADOR
								+ " WHERE jug_id = jfp_jug) AS Equipo FROM "
								+ REL_JUG_FUTBOL_PART
								+ ", "
								+ OBJ_INCIDENCIA_FUTBOL
								+ ", "
								+ OBJ_PARTIDO
								+ " WHERE inf_id = jfp_inf "
								+ "AND jfp_par = par_id AND par_tor = "
								+ id_torneo
								+ " AND inf_tip = 1"
								+ " GROUP BY jfp_jug, Equipo ORDER BY Goles DESC", null);
		if (c.moveToFirst()) {
			do {
				lista.add(new Recuento(c.getInt(0), c.getInt(2), c.getInt(1)));
			} while (c.moveToNext());
		}
		c.close();
		return lista;
	}

	//Este método devuelve una lista con la clasificación de los goleadores de balonmano en un torneo
	public ArrayList<Recuento> obtenerGoleadoresTorneoBalonmano(int id_torneo) {
		ArrayList<Recuento> lista = new ArrayList<Recuento>();
		Cursor c = bd.rawQuery("SELECT jbmp_jug, COUNT(jbmp_inb) As Goles, (SELECT jug_equ FROM "
						+ OBJ_JUGADOR
						+ " WHERE jug_id = jbmp_jug) AS Equipo FROM "
						+ REL_JUG_BALONMANO_PART + ", " + OBJ_PARTIDO
						+ " WHERE jbmp_par = par_id AND par_tor = " + id_torneo
						+ " GROUP BY jbmp_jug, Equipo ORDER BY Goles DESC", null);
		if (c.moveToFirst()) {
			do {
				lista.add(new Recuento(c.getInt(0), c.getInt(2), c.getInt(1)));
			} while (c.moveToNext());
		}
		c.close();
		return lista;
	}

	//Este método devuelve una lista con la clasificación de los jugadores que han recibido tarjetas en un torneo
	public ArrayList<Tarjetas> obtenerTarjetasTorneo(int id_torneo) {
		ArrayList<Tarjetas> lista = new ArrayList<Tarjetas>();
		Cursor c = bd.rawQuery("SELECT jfp_jug, (SELECT jug_equ FROM "
								+ OBJ_JUGADOR
								+ " WHERE jug_id = jfp_jug) AS Equipo, "
								+ "(SELECT COUNT(*) FROM "
								+ OBJ_INCIDENCIA_FUTBOL
								+ " WHERE inf_id IN (SELECT jfp2.jfp_inf FROM "
								+ REL_JUG_FUTBOL_PART
								+ " AS jfp2 WHERE jfp2.jfp_jug = jfp1.jfp_jug) AND inf_tip = 2 AND inf_col = 2) AS Rojas, "
								+ "(SELECT COUNT(*) FROM "
								+ OBJ_INCIDENCIA_FUTBOL
								+ " WHERE inf_id IN (SELECT jfp2.jfp_inf FROM "
								+ REL_JUG_FUTBOL_PART
								+ " AS jfp2 WHERE jfp2.jfp_jug = jfp1.jfp_jug) AND inf_tip = 2 AND inf_col = 1) AS Amarillas FROM "
								+ REL_JUG_FUTBOL_PART
								+ " AS jfp1, "
								+ OBJ_INCIDENCIA_FUTBOL
								+ ", "
								+ OBJ_PARTIDO
								+ " WHERE inf_id = jfp_inf "
								+ "AND jfp_par = par_id AND par_tor = "
								+ id_torneo
								+ " AND inf_tip = 2"
								+ " GROUP BY jfp_jug, Equipo ORDER BY (Rojas + Amarillas) DESC, Rojas DESC, Amarillas DESC", null);
		if (c.moveToFirst()) {
			do {
				lista.add(new Tarjetas(c.getInt(0), c.getInt(1), c.getInt(3), c.getInt(2)));
			} while (c.moveToNext());
		}
		c.close();
		return lista;
	}

	//Este método devuelve el jugador que más ha marcado a un equipo durante un torneo
	public ArrayList<GoleadorEquipos> obtenerJugadorGolesEquipo(int id_torneo) {
		ArrayList<GoleadorEquipos> lista = new ArrayList<GoleadorEquipos>();
		Cursor c = bd.rawQuery("SELECT MAX((SELECT COUNT(*) FROM "
								+ OBJ_INCIDENCIA_FUTBOL
								+ ", "
								+ REL_JUG_FUTBOL_PART
								+ " AS B WHERE inf_tip = 1 AND inf_id = B.jfp_inf AND "
								+ "B.jfp_jug = A.jfp_jug AND jfp_par IN(SELECT ejp_par FROM "
								+ REL_EQUIPO_PARTIDO
								+ " WHERE ejp_equ = equ_id))) AS Jug, jfp_jug, equ_id FROM "
								+ REL_JUG_FUTBOL_PART
								+ " AS A, "
								+ OBJ_INCIDENCIA_FUTBOL
								+ ", "
								+ REL_EQUIPO_PARTIDO
								+ ", "
								+ OBJ_EQUIPO
								+ ", "
								+ OBJ_PARTIDO
								+ " WHERE equ_id = ejp_equ AND ejp_par = jfp_par AND jfp_par IN "
								+ "(select ejp_par FROM "
								+ OBJ_EQUIPO
								+ " AS B, "
								+ REL_EQUIPO_PARTIDO
								+ " WHERE equ_id = ejp_equ AND B.equ_id = equ_id) "
								+ "AND jfp_inf = inf_id AND inf_tip = 1 AND jfp_jug NOT IN "
								+ "(SELECT jug_id FROM " + OBJ_JUGADOR
								+ " WHERE jug_equ = equ_id) "
								+" AND jfp_par = par_id AND ejp_par = par_id AND par_tor = " + id_torneo
								+ " GROUP BY equ_id, jfp_jug ORDER BY Jug DESC LIMIT 1", null);
		if (c.moveToFirst()) {
			do {
				lista.add(new GoleadorEquipos(c.getInt(1), c.getInt(2), c.getInt(0)));
			} while (c.moveToNext());
		}
		c.close();
		return lista;
	}

	//Este método devuelve el partido de tenis o pádel cuya diferencia es mayor entre sets a favor y en contra para el jugador pasado como parámetro 
	public PartidoMejorVictoria obtenerMejorVictoria(int id_jugador) {
		PartidoMejorVictoria p = new PartidoMejorVictoria();
		Cursor c = bd.rawQuery("SELECT (jjp_st1+jjp_st2+jjp_st3)-(SELECT B.jjp_st1 + B.jjp_st2 + B.jjp_st3 FROM "
								+ REL_JUGADOR_PARTIDO
								+ " AS B WHERE B.jjp_jug <> A.jjp_jug AND B.jjp_par = A.jjp_par) AS dif, jjp_par, jjp_jug, jjp_ron FROM "
								+ REL_JUGADOR_PARTIDO
								+ " AS A WHERE jjp_jug = "
								+ id_jugador
								+ " ORDER BY dif DESC LIMIT 1", null);
		if (c.moveToFirst()){
			//Se comprueba que ha ganado
			DatosJugadorPartido djp1 = new DatosJugadorPartido();
			DatosJugadorPartido djp2 = new DatosJugadorPartido();
			Cursor c2 = bd.rawQuery("SELECT * FROM " + REL_JUGADOR_PARTIDO
					+ " WHERE jjp_par = " + c.getInt(1) + " AND jjp_jug = "
					+ id_jugador, null);
			Cursor c3 = bd.rawQuery("SELECT * FROM " + REL_JUGADOR_PARTIDO
					+ " WHERE jjp_par = " + c.getInt(1) + " AND jjp_jug <> "
					+ id_jugador, null);
			int comprobarGanador = 0; // Si al terminar de comprobar los resultados es positivo, significará que el ganados es el primer jugador. Si es negativo, al contrario.
			if (c2.moveToFirst() && c3.moveToFirst()) {
				djp1 = new DatosJugadorPartido(c2.getInt(0), c2.getInt(1),
						c2.getInt(2), c2.getInt(3), c2.getInt(4),
						c2.getString(5));
				c2.moveToNext();
				djp2 = new DatosJugadorPartido(c3.getInt(0), c3.getInt(1),
						c3.getInt(2), c3.getInt(3), c3.getInt(4),
						c3.getString(5));

				if ((djp1.getSt1() > djp2.getSt1()))
					comprobarGanador++;
				else
					comprobarGanador--;
				
				if ((djp1.getSt2() > djp2.getSt2()))
					comprobarGanador++;
				else
					comprobarGanador--;
				
				if ((djp1.getSt3() > djp2.getSt3()))
					comprobarGanador++;
				else
					comprobarGanador--;

				if (comprobarGanador > 0)
					p = new PartidoMejorVictoria(c.getInt(2), c.getInt(1), c.getInt(0), c.getString(3));
			}
			c2.close();
			c3.close();
		}
		c.close();
		return p;
	}

	//Este método devuelve un vector con los sets a favor y en contra totales de un jugador
	public int[] obtenerSetsFC(int id_jugador) {
		int[] sets = { 0, 0 };
		Cursor c = bd.rawQuery("SELECT SUM((SELECT jjp_st1+jjp_st2+jjp_st3 FROM "
						+ REL_JUGADOR_PARTIDO
						+ " AS jjp1 WHERE jjp1.jjp_jug = " + id_jugador
						+ " AND jjp1.jjp_par = jjp3.jjp_par)),"
						+ "SUM((SELECT jjp_st1+jjp_st2+jjp_st3 FROM "
						+ REL_JUGADOR_PARTIDO
						+ " AS jjp2 WHERE jjp2.jjp_jug <> " + id_jugador
						+ " AND jjp2.jjp_par = jjp3.jjp_par)) "
						+ "FROM REL_JUGADOR_PARTIDO jjp3 WHERE jjp3.jjp_jug = "
						+ id_jugador, null);
		if (c.moveToFirst()) {
			sets[0] = c.getInt(0);
			sets[1] = c.getInt(1);
		}
		c.close();
		return sets;
	}

	//Este método devuelve un vector con los partidos ganados y perdidos totales de un jugador
	public int[] obtenerPartidosGP(int id_jugador) {
		int[] partidosGP = { 0, 0 };
		ArrayList<Integer> partidos = new ArrayList<Integer>();
		DatosJugadorPartido djp1 = new DatosJugadorPartido();
		DatosJugadorPartido djp2 = new DatosJugadorPartido();
		// Primero se necesitan los partidos del jugador
		Cursor c = bd.rawQuery("SELECT DISTINCT jjp_par FROM "
				+ REL_JUGADOR_PARTIDO + " WHERE jjp_jug = " + id_jugador, null);
		if (c.moveToFirst()) {
			do {
				partidos.add(c.getInt(0));
			} while (c.moveToNext());
		}
		c.close();
		// Para cada partido se comprueba quien es el ganador
		Iterator<Integer> it = partidos.iterator();
		while (it.hasNext()) {
			int par = it.next();
			Cursor c2 = bd.rawQuery("SELECT * FROM " + REL_JUGADOR_PARTIDO
					+ " WHERE jjp_par = " + par + " AND jjp_jug = "
					+ id_jugador, null);
			Cursor c3 = bd.rawQuery("SELECT * FROM " + REL_JUGADOR_PARTIDO
					+ " WHERE jjp_par = " + par + " AND jjp_jug <> "
					+ id_jugador, null);
			int comprobarGanador = 0; // Si al terminar de comprobar los resultados es positivo, significará que el ganados es el primer jugador. Si es negativo, al contrario.
			if (c2.moveToFirst() && c3.moveToFirst()) {
				djp1 = new DatosJugadorPartido(c2.getInt(0), c2.getInt(1),
						c2.getInt(2), c2.getInt(3), c2.getInt(4),
						c2.getString(5));
				c2.moveToNext();
				djp2 = new DatosJugadorPartido(c3.getInt(0), c3.getInt(1),
						c3.getInt(2), c3.getInt(3), c3.getInt(4),
						c3.getString(5));

				if ((djp1.getSt1() > djp2.getSt1()))
					comprobarGanador++;
				else
					comprobarGanador--;
				
				if ((djp1.getSt2() > djp2.getSt2()))
					comprobarGanador++;
				else
					comprobarGanador--;
				
				if ((djp1.getSt3() > djp2.getSt3()))
					comprobarGanador++;
				else
					comprobarGanador--;

				if (comprobarGanador > 0)
					partidosGP[0]++;
				else
					partidosGP[1]++;
			}
			c2.close();
			c3.close();
		}
		c.close();
		return partidosGP;
	}

	//Este método devuelve una lista con obtejos ClasificacionBaloncesto que forman la clasficicación de los equipos en un torneo de baloncesto
	public ArrayList<ClasificacionBaloncesto> obtenerClasificacionBaloncesto(int id_torneo) {
		ArrayList<ClasificacionBaloncesto> lista = new ArrayList<ClasificacionBaloncesto>();
		// Para saber el número de jornadas disputadas
		int num_jugados = 0;
		Cursor c = bd.rawQuery("SELECT COUNT(DISTINCT par_des) FROM "
				+ OBJ_PARTIDO + " WHERE par_tor = " + id_torneo
				+ " AND par_cla = 1", null);
		if (c.moveToFirst()) num_jugados = c.getInt(0);
		c.close();
		// Para saber los partidos que se han jugado
		ArrayList<Integer> lista_partidos = new ArrayList<Integer>();
		c = bd.rawQuery("SELECT par_id FROM " + OBJ_PARTIDO + " WHERE par_tor = " + id_torneo + " AND par_cla = 1", null);
		if (c.moveToFirst()) {
			do {
				lista_partidos.add(c.getInt(0));
			} while (c.moveToNext());
		}
		c.close();
		// Obtengo los equipos del torneo
		ArrayList<Equipo> lista_equipos = obtenerEquiposDeTorneo(id_torneo, 3);
		Iterator<Equipo> it = lista_equipos.iterator();
		while (it.hasNext()) {
			Equipo e = it.next();
			int g = 0, p = 0, pf = 0, pc = 0, j = 0;
			if (e.getId() != -1) {
				Iterator<Integer> ip = lista_partidos.iterator();
				while (ip.hasNext()) {
					int partido = ip.next();
					// Ahora de aquí saco con el getCount el numero de partidos ganados y recorriendo el cursor los puntos a favor
					c = bd.rawQuery(
							"SELECT ejp1.ejp_res, (SELECT ejp2.ejp_res FROM "
									+ REL_EQUIPO_PARTIDO
									+ " AS ejp2 WHERE ejp2.ejp_equ <> ejp1.ejp_equ AND ejp2.ejp_par = ejp1.ejp_par) FROM "
									+ REL_EQUIPO_PARTIDO
									+ " AS ejp1 WHERE ejp1.ejp_equ = "
									+ e.getId()
									+ " AND ejp1.ejp_res > (SELECT ejp2.ejp_res FROM "
									+ REL_EQUIPO_PARTIDO
									+ " AS ejp2 WHERE ejp2.ejp_equ <> "
									+ e.getId()
									+ " AND ejp1.ejp_par = ejp2.ejp_par AND ejp2.ejp_equ <> -1)"
									+ " AND ejp1.ejp_par = " + partido, null);
					j = j + c.getCount();
					g = g + c.getCount();
					if (c.moveToFirst()) {
						do {
							pf = pf + c.getInt(0);
							pc = pc + c.getInt(1);
						} while (c.moveToNext());
					}
					c.close();
					
					// Ahora de aquí saco con el getCount el numero de partidos perdidos y recorriendo el cursor los puntos en contra
					c = bd.rawQuery(
							"SELECT ejp1.ejp_res, (SELECT ejp2.ejp_res FROM "
									+ REL_EQUIPO_PARTIDO
									+ " AS ejp2 WHERE ejp2.ejp_equ <> ejp1.ejp_equ AND ejp2.ejp_par = ejp1.ejp_par) FROM "
									+ REL_EQUIPO_PARTIDO
									+ " AS ejp1 WHERE ejp1.ejp_equ <> "
									+ e.getId()
									+ " AND ejp1.ejp_res > (SELECT ejp2.ejp_res FROM "
									+ REL_EQUIPO_PARTIDO
									+ " AS ejp2 WHERE ejp2.ejp_equ = "
									+ e.getId()
									+ " AND ejp1.ejp_par = ejp2.ejp_par AND ejp2.ejp_equ <> -1)"
									+ " AND ejp1.ejp_par = " + partido, null);
					j = j + c.getCount();
					p = p + c.getCount();
					if (c.moveToFirst()) {
						do {
							pf = pf + c.getInt(1);
							pc = pc + c.getInt(0);
						} while (c.moveToNext());
					}
					c.close();
				}
				// Almaceno los datos
				ClasificacionBaloncesto cla = new ClasificacionBaloncesto(e.getId(), j, g, p, pf, pc);
				lista.add(cla);
			}
		}
		return lista;
	}

	//Este método devuelve una lista con la clasificación de máximos anotadores de baloncesto de un torneo
	public ArrayList<Recuento> obtenerAnotadoresTorneo(int id_torneo) {
		ArrayList<Recuento> lista = new ArrayList<Recuento>();
		String consulta = "SELECT jbp_jug, SUM(jbp_pun) As Puntos, (SELECT jug_equ FROM "
				+ OBJ_JUGADOR
				+ " WHERE jug_id = jbp_jug) AS Equipo FROM "
				+ REL_JUG_BALONCESTO_PART
				+ ", "
				+ OBJ_PARTIDO
				+ " WHERE jbp_par = par_id AND par_tor = "
				+ id_torneo
				+ " AND par_cla = 1"
				+ " GROUP BY jbp_jug, Equipo HAVING Puntos > 0 ORDER BY Puntos DESC";
		Cursor c = bd.rawQuery(consulta, null);
		if (c.moveToFirst()) {
			do {
				lista.add(new Recuento(c.getInt(0), c.getInt(2), c.getInt(1)));
			} while (c.moveToNext());
		}
		c.close();
		return lista;
	}

	//Este método devuelve una lista con la clasificación de jugadores que han sido nombrados MVP en un torneo
	public ArrayList<Recuento> obtenerJugadoresMVP(int id_torneo) {
		ArrayList<Recuento> lista = new ArrayList<Recuento>();
		Cursor c = bd.rawQuery("SELECT jbp_jug, COUNT(jbp_jug) AS MVP, jug_equ FROM "
								+ REL_JUG_BALONCESTO_PART
								+ ", "
								+ OBJ_PARTIDO
								+ ", "
								+ OBJ_JUGADOR
								+ " WHERE jbp_mvp = 1 AND jbp_par = par_id AND par_tor = " + id_torneo
								+ " AND par_cla = 1 AND jbp_jug = jug_id GROUP BY jbp_jug ORDER BY MVP DESC", null);
		if (c.moveToFirst()) {
			do {
				lista.add(new Recuento(c.getInt(0), c.getInt(2), c.getInt(1)));
			} while (c.moveToNext());
		}
		c.close();
		return lista;
	}

	//Este método devuelve una lista de objetos Rendimiento que representan la serie de partidos ganados o perdidos de los equipos de un torneo
	public ArrayList<Rendimiento> obtenerRendimientoEquipos(int id_torneo) {
		ArrayList<Rendimiento> lista = new ArrayList<Rendimiento>();
		ArrayList<String> progresion = new ArrayList<String>();
		// Para saber el número de jornadas disputadas
		int num_jugados = 0;
		Cursor c = bd.rawQuery("SELECT COUNT(DISTINCT par_des) FROM "
				+ OBJ_PARTIDO + " WHERE par_tor = " + id_torneo
				+ " AND par_cla = 1", null);
		if (c.moveToFirst())
			num_jugados = c.getInt(0);
		c.close();
		// Para saber los partidos que se han jugado
		ArrayList<Integer> lista_partidos = new ArrayList<Integer>();
		c = bd.rawQuery("SELECT par_id FROM " + OBJ_PARTIDO + " WHERE par_tor = " + id_torneo + " AND par_cla = 1", null);
		if (c.moveToFirst()) {
			do {
				lista_partidos.add(c.getInt(0));
			} while (c.moveToNext());
		}
		c.close();
		// Obtengo los equipos del torneo
		ArrayList<Equipo> lista_equipos = obtenerEquiposDeTorneo(id_torneo, 3);
		Iterator<Equipo> it = lista_equipos.iterator();
		while (it.hasNext()) {
			Equipo e = it.next();
			progresion = new ArrayList<String>();
			if (e.getId() != -1) {
				Iterator<Integer> ip = lista_partidos.iterator();
				while (ip.hasNext()) {
					int partido = ip.next();
					// Con esta consulta saco los partidos ganados
					c = bd.rawQuery(
							"SELECT ejp1.ejp_res, (SELECT ejp2.ejp_res FROM "
									+ REL_EQUIPO_PARTIDO
									+ " AS ejp2 WHERE ejp2.ejp_equ <> ejp1.ejp_equ AND ejp2.ejp_par = ejp1.ejp_par) FROM "
									+ REL_EQUIPO_PARTIDO
									+ " AS ejp1 WHERE ejp1.ejp_equ = "
									+ e.getId()
									+ " AND ejp1.ejp_res > (SELECT ejp2.ejp_res FROM "
									+ REL_EQUIPO_PARTIDO
									+ " AS ejp2 WHERE ejp2.ejp_equ <> "
									+ e.getId()
									+ " AND ejp1.ejp_par = ejp2.ejp_par AND ejp2.ejp_equ <> -1)"
									+ " AND ejp1.ejp_par = " + partido, null);
					if (c.moveToFirst()) {
						do {
							progresion.add("G");
						} while (c.moveToNext());
					}
					c.close();
					// Con esta consulta obtengo los partidos perdidos
					c = bd.rawQuery(
							"SELECT ejp1.ejp_res, (SELECT ejp2.ejp_res FROM "
									+ REL_EQUIPO_PARTIDO
									+ " AS ejp2 WHERE ejp2.ejp_equ <> ejp1.ejp_equ AND ejp2.ejp_par = ejp1.ejp_par) FROM "
									+ REL_EQUIPO_PARTIDO
									+ " AS ejp1 WHERE ejp1.ejp_equ <> "
									+ e.getId()
									+ " AND ejp1.ejp_res > (SELECT ejp2.ejp_res FROM "
									+ REL_EQUIPO_PARTIDO
									+ " AS ejp2 WHERE ejp2.ejp_equ = "
									+ e.getId()
									+ " AND ejp1.ejp_par = ejp2.ejp_par AND ejp2.ejp_equ <> -1)"
									+ " AND ejp1.ejp_par = " + partido, null);
					if (c.moveToFirst()) {
						do {
							progresion.add("P");
						} while (c.moveToNext());
					}
					c.close();
				}
				// Almaceno los datos
				lista.add(new Rendimiento(e.getId(), progresion));
			}
		}
		return lista;
	}

	//Este método nos devuelve los puntos de la clasificación de un jugador de tenis o pádel
	public int obtenerPuntosClasificacionJugador(int id_jug, int dep) {
		int puntos = 0;
		//Con esta consulta obtengo la ronda a la que ha llegado más lejos el jugador en cada torneo que haya disputado
		Cursor c = bd.rawQuery("SELECT MIN(par_des), par_id FROM "
				+ OBJ_PARTIDO + ", " + REL_JUGADOR_PARTIDO
				+ " WHERE jjp_par = par_id AND par_dep = " + dep
				+ " AND jjp_jug = " + id_jug
				+ " GROUP BY par_tor ORDER BY par_des", null);
		if (c.moveToFirst()) {
			do {
				//Compruebo la ronda. En caso de haber llegado a la ronda 1 (Final), habrá que comprobar si ha ganado o fue finalista
				String ronda = c.getString(0);
				if (ronda.equals("Ronda 7")) puntos = puntos + 1;
				else if (ronda.equals("Ronda 6")) puntos = puntos + 2;
				else if (ronda.equals("Ronda 5")) puntos = puntos + 3;
				else if (ronda.equals("Ronda 4")) puntos = puntos + 4;
				else if (ronda.equals("Ronda 3")) puntos = puntos + 5;
				else if (ronda.equals("Ronda 2")) puntos = puntos + 6;
				else if (ronda.equals("Ronda 1")) {
					int comprobar = 0;
					int[] jugadores = obtenerJugadoresPartido(c.getInt(1));
					int[] res1 = obtenerResultadoTenis(c.getInt(1), jugadores[0]);
					int[] res2 = obtenerResultadoTenis(c.getInt(1), jugadores[1]);
					if (jugadores[0] == id_jug) {
						if (res1[0] > res2[0]) comprobar++;
						else comprobar--;
						
						if (res1[1] > res2[1]) comprobar++;
						else comprobar--;
						
						if (res1[2] > res2[2]) comprobar++;
						else comprobar--;

						if (comprobar > 0) puntos = puntos + 10;
						else puntos = puntos + 7;
					} else {
						if (res1[0] > res2[0]) comprobar--;
						else comprobar++;
						
						if (res1[1] > res2[1]) comprobar--;
						else comprobar++;
						
						if (res1[2] > res2[2]) comprobar--;
						else comprobar++;

						if (comprobar > 0) puntos = puntos + 10;
						else puntos = puntos + 7;
					}
				}
			} while (c.moveToNext());
		}
		return puntos;
	}

	//Este método devuelve una lista de objetos Rendimiento que representan la serie de partidos ganados, empatados o perdidos de los equipos de un torneo de balonmano
	public ArrayList<Rendimiento> obtenerRendimientoEquiposBalonmano(
			int id_torneo) {
		ArrayList<Rendimiento> lista = new ArrayList<Rendimiento>();
		ArrayList<String> progresion = new ArrayList<String>();
		// Para saber el número de jornadas disputadas
		int num_jugados = 0;
		Cursor c = bd.rawQuery("SELECT COUNT(DISTINCT par_des) FROM "
				+ OBJ_PARTIDO + " WHERE par_tor = " + id_torneo
				+ " AND par_cla = 1", null);
		if (c.moveToFirst())
			num_jugados = c.getInt(0);
		c.close();
		// Para saber los partidos que se han jugado
		ArrayList<Integer> lista_partidos = new ArrayList<Integer>();
		c = bd.rawQuery("SELECT par_id FROM " + OBJ_PARTIDO
				+ " WHERE par_tor = " + id_torneo + " AND par_cla = 1", null);
		if (c.moveToFirst()) {
			do {
				lista_partidos.add(c.getInt(0));
			} while (c.moveToNext());
		}
		c.close();
		// Obtengo los equipos del torneo
		ArrayList<Equipo> lista_equipos = obtenerEquiposDeTorneo(id_torneo, 5);
		Iterator<Equipo> it = lista_equipos.iterator();
		while (it.hasNext()) {
			Equipo e = it.next();
			progresion = new ArrayList<String>();
			if (e.getId() != -1) {
				Iterator<Integer> ip = lista_partidos.iterator();
				while (ip.hasNext()) {
					int partido = ip.next();
					// Con esta consulta saco los partidos ganados
					c = bd.rawQuery(
							"SELECT ejp1.ejp_res, (SELECT ejp2.ejp_res FROM "
									+ REL_EQUIPO_PARTIDO
									+ " AS ejp2 WHERE ejp2.ejp_equ <> ejp1.ejp_equ AND ejp2.ejp_par = ejp1.ejp_par) FROM "
									+ REL_EQUIPO_PARTIDO
									+ " AS ejp1 WHERE ejp1.ejp_equ = "
									+ e.getId()
									+ " AND ejp1.ejp_res > (SELECT ejp2.ejp_res FROM "
									+ REL_EQUIPO_PARTIDO
									+ " AS ejp2 WHERE ejp2.ejp_equ <> "
									+ e.getId()
									+ " AND ejp1.ejp_par = ejp2.ejp_par AND ejp2.ejp_equ <> -1)"
									+ " AND ejp1.ejp_par = " + partido, null);
					if (c.moveToFirst()) {
						do {
							progresion.add("G");
						} while (c.moveToNext());
					}
					c.close();
					// Con esta los empatados
					c = bd.rawQuery(
							"SELECT ejp1.ejp_res, (SELECT ejp2.ejp_res FROM "
									+ REL_EQUIPO_PARTIDO
									+ " AS ejp2 WHERE ejp2.ejp_equ <> ejp1.ejp_equ AND ejp2.ejp_par = ejp1.ejp_par) FROM "
									+ REL_EQUIPO_PARTIDO
									+ " AS ejp1 WHERE ejp1.ejp_equ = "
									+ e.getId()
									+ " AND ejp1.ejp_res = (SELECT ejp2.ejp_res FROM "
									+ REL_EQUIPO_PARTIDO
									+ " AS ejp2 WHERE ejp2.ejp_equ <> "
									+ e.getId()
									+ " AND ejp1.ejp_par = ejp2.ejp_par AND ejp2.ejp_equ <> -1)"
									+ " AND ejp1.ejp_par = " + partido, null);
					if (c.moveToFirst()) {
						do {
							progresion.add("E");
						} while (c.moveToNext());
					}
					c.close();
					// Con esta los perdidos
					c = bd.rawQuery(
							"SELECT ejp1.ejp_res, (SELECT ejp2.ejp_res FROM "
									+ REL_EQUIPO_PARTIDO
									+ " AS ejp2 WHERE ejp2.ejp_equ <> ejp1.ejp_equ AND ejp2.ejp_par = ejp1.ejp_par) FROM "
									+ REL_EQUIPO_PARTIDO
									+ " AS ejp1 WHERE ejp1.ejp_equ <> "
									+ e.getId()
									+ " AND ejp1.ejp_res > (SELECT ejp2.ejp_res FROM "
									+ REL_EQUIPO_PARTIDO
									+ " AS ejp2 WHERE ejp2.ejp_equ = "
									+ e.getId()
									+ " AND ejp1.ejp_par = ejp2.ejp_par AND ejp2.ejp_equ <> -1)"
									+ " AND ejp1.ejp_par = " + partido, null);
					if (c.moveToFirst()) {
						do {
							progresion.add("P");
						} while (c.moveToNext());
					}
					c.close();
				}
				// Almaceno los datos
				lista.add(new Rendimiento(e.getId(), progresion));
			}
		}
		return lista;
	}
}
