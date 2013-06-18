package tipos;

public class TorneoTenis {
	private int id, dep, numJugadores;
	private String nom, lug, fin, ffi;
	public TorneoTenis() {
	}
	public TorneoTenis(int id, String nom, String lug, String fin, String ffi, int dep, int numJugadores) {
		this.id = id;
		this.dep = dep;
		this.numJugadores = numJugadores;
		this.nom = nom;
		this.lug = lug;
		this.fin = fin;
		this.ffi = ffi;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getDep() {
		return dep;
	}
	public void setDep(int dep) {
		this.dep = dep;
	}
	public int getNumJugadores() {
		return numJugadores;
	}
	public void setNumJugadores(int numJugadores) {
		this.numJugadores = numJugadores;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getLug() {
		return lug;
	}
	public void setLug(String lug) {
		this.lug = lug;
	}
	public String getFin() {
		String f = fin;
		f = convertirFecha(f);
		return f;
	}
	public void setFin(String fin) {
		fin = convertirFecha(fin);
		this.fin = fin;
	}
	public String getFfi() {
		String f = ffi;
		f = convertirFecha(f);
		return f;
	}
	public void setFfi(String ffi) {
		ffi = convertirFecha(ffi);
		this.ffi = ffi;
	}
	
	private String convertirFecha(String fecha){
		String fec[] = fecha.split("/");
		String f = fec[2] + "/" + fec[1] + "/" + fec[0];
		return f;
	}
}
