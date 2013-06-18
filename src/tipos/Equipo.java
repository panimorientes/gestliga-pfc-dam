package tipos;

public class Equipo {
	private int id, ffu, soc, dep, numJugadores;
	private String nom, est, ciu;
	public Equipo() {
	}
	public Equipo(int id, String nom, int ffu, int soc, String est, int dep, String ciu, int numJugadores) {
		this.id = id;
		this.ffu = ffu;
		this.soc = soc;
		this.dep = dep;
		this.nom = nom;
		this.est = est;
		this.ciu = ciu;
		this.numJugadores = numJugadores;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getFfu() {
		return ffu;
	}
	public void setFfu(int ffu) {
		this.ffu = ffu;
	}
	public int getSoc() {
		return soc;
	}
	public void setSoc(int soc) {
		this.soc = soc;
	}
	public int getDep() {
		return dep;
	}
	public void setDep(int dep) {
		this.dep = dep;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getEst() {
		return est;
	}
	public void setEst(String est) {
		this.est = est;
	}
	public String getCiu() {
		return ciu;
	}
	public void setCiu(String ciu) {
		this.ciu = ciu;
	}
	public int getNumJugadores() {
		return numJugadores;
	}
	public void setNumJugadores(int numJugadores) {
		this.numJugadores = numJugadores;
	}
	
}
