package tipos;

public class IncidenciaFutbol {
	private int id, tip, min, col, jug;
	public IncidenciaFutbol() {
	}
	public IncidenciaFutbol(int id, int tip, int min, int col, int jug) {
		this.id = id;
		this.tip = tip;
		this.min = min;
		this.col = col;
		this.jug = jug;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTip() {
		return tip;
	}
	public void setTip(int tip) {
		this.tip = tip;
	}
	public int getMin() {
		return min;
	}
	public void setMin(int min) {
		this.min = min;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public int getJug() {
		return jug;
	}
	public void setJug(int jug) {
		this.jug = jug;
	}

	
}
