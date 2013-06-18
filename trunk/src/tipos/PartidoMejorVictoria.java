package tipos;

public class PartidoMejorVictoria {
	int jug, par, dif;
	String ron;

	public PartidoMejorVictoria() {
		jug = -1;
	}

	public PartidoMejorVictoria(int jug, int par, int dif, String ron) {
		this.jug = jug;
		this.par = par;
		this.dif = dif;
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

	public int getDif() {
		return dif;
	}

	public void setDif(int dif) {
		this.dif = dif;
	}

	public String getRon() {
		return ron;
	}

	public void setRon(String ron) {
		this.ron = ron;
	}
	
	
}
