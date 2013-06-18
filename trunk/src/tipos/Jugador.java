package tipos;

public class Jugador {
	private int id, pos, dep, equ;
	private String nom, apo, fna;
	public Jugador() {
	}
	public Jugador(int id, String nom, String apo, String fna, int pos, int dep, int equ) {
		this.id = id;
		this.pos = pos;
		this.dep = dep;
		this.equ = equ;
		this.nom = nom;
		this.apo = apo;
		this.fna = fna;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPos() {
		return pos;
	}
	public void setPos(int pos) {
		this.pos = pos;
	}
	public int getDep() {
		return dep;
	}
	public void setDep(int dep) {
		this.dep = dep;
	}
	public int getEqu() {
		return equ;
	}
	public void setEqu(int equ) {
		this.equ = equ;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getApo() {
		return apo;
	}
	public void setApo(String apo) {
		this.apo = apo;
	}
	public String getFna() {
		String f = fna;
		f = convertirFecha(f);
		return f;
	}
	public void setFna(String fna) {
		fna = convertirFecha(fna);
		this.fna = fna;
	}
	
	private String convertirFecha(String fecha){
		String fec[] = fecha.split("/");
		String f = fec[2] + "/" + fec[1] + "/" + fec[0];
		return f;
	}
}
