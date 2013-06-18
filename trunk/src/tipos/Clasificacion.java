package tipos;

public class Clasificacion {
	int id_equipo, jug, gan, emp, per, gf, gc;

	public Clasificacion() {
	}

	public Clasificacion(int id_equipo, int jug, int gan, int emp, int per, int gf, int gc) {
		this.id_equipo = id_equipo;
		this.jug = jug;
		this.gan = gan;
		this.emp = emp;
		this.per = per;
		this.gf = gf;
		this.gc = gc;
	}

	public int getId_equipo() {
		return id_equipo;
	}

	public void setId_equipo(int id_equipo) {
		this.id_equipo = id_equipo;
	}

	public int getJug() {
		return jug;
	}

	public void setJug(int jug) {
		this.jug = jug;
	}

	public int getGan() {
		return gan;
	}

	public void setGan(int gan) {
		this.gan = gan;
	}

	public int getEmp() {
		return emp;
	}

	public void setEmp(int emp) {
		this.emp = emp;
	}

	public int getPer() {
		return per;
	}

	public void setPer(int per) {
		this.per = per;
	}

	public int getGf() {
		return gf;
	}

	public void setGf(int gf) {
		this.gf = gf;
	}

	public int getGc() {
		return gc;
	}

	public void setGc(int gc) {
		this.gc = gc;
	}
	
	
}
