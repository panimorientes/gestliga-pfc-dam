package tipos;

public class DatosJugadorPartido {
	int jug, par, st1, st2, st3;
	String ron;
	public DatosJugadorPartido() {
	}
	public DatosJugadorPartido(int jug, int par, int st1, int st2, int st3, String ron) {
		this.jug = jug;
		this.par = par;
		this.st1 = st1;
		this.st2 = st2;
		this.st3 = st3;
		this.ron = ron;
	}
	public int getJug() {
		return jug;
	}
	public void setJug(int jug) {
		this.jug = jug;
	}
	public int getPar() {
		return par;
	}
	public void setPar(int par) {
		this.par = par;
	}
	public int getSt1() {
		return st1;
	}
	public void setSt1(int st1) {
		this.st1 = st1;
	}
	public int getSt2() {
		return st2;
	}
	public void setSt2(int st2) {
		this.st2 = st2;
	}
	public int getSt3() {
		return st3;
	}
	public void setSt3(int st3) {
		this.st3 = st3;
	}
	public String getRon() {
		return ron;
	}
	public void setRon(String ron) {
		this.ron = ron;
	}
}
