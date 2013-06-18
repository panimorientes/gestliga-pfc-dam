package tipos;

public class DatosJugadorPartidoBC {
	int jug, par, mvp, pun, reb, tap, fal;

	public DatosJugadorPartidoBC() {
	}

	public DatosJugadorPartidoBC(int jug, int par, int mvp, int pun, int reb,
			int tap, int fal) {
		this.jug = jug;
		this.par = par;
		this.mvp = mvp;
		this.pun = pun;
		this.reb = reb;
		this.tap = tap;
		this.fal = fal;
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

	public int getMvp() {
		return mvp;
	}

	public void setMvp(int mvp) {
		this.mvp = mvp;
	}

	public int getPun() {
		return pun;
	}

	public void setPun(int pun) {
		this.pun = pun;
	}

	public int getReb() {
		return reb;
	}

	public void setReb(int reb) {
		this.reb = reb;
	}

	public int getTap() {
		return tap;
	}

	public void setTap(int tap) {
		this.tap = tap;
	}

	public int getFal() {
		return fal;
	}

	public void setFal(int fal) {
		this.fal = fal;
	}
	
}
