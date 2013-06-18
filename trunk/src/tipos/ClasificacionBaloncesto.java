package tipos;

public class ClasificacionBaloncesto {
	int id_equipo, jug, gan, per, pf, pc;

	public ClasificacionBaloncesto() {
	}

	public ClasificacionBaloncesto(int id_equipo, int jug, int gan, int per,
			int pf, int pc) {
		this.id_equipo = id_equipo;
		this.jug = jug;
		this.gan = gan;
		this.per = per;
		this.pf = pf;
		this.pc = pc;
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

	public int getPer() {
		return per;
	}

	public void setPer(int per) {
		this.per = per;
	}

	public int getPf() {
		return pf;
	}

	public void setPf(int pf) {
		this.pf = pf;
	}

	public int getPc() {
		return pc;
	}

	public void setPc(int pc) {
		this.pc = pc;
	}
	
	
}
