package tipos;

public class IncidenciaBalonmano {
	int id, gol, jug;

	public IncidenciaBalonmano() {
	}

	public IncidenciaBalonmano(int id, int gol, int jug) {
		this.id = id;
		this.gol = gol;
		this.jug = jug;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGol() {
		return gol;
	}

	public void setGol(int gol) {
		this.gol = gol;
	}

	public int getJug() {
		return jug;
	}

	public void setJug(int jug) {
		this.jug = jug;
	}
}
