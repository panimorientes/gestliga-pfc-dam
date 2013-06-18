package tipos;

public class Deporte {
	private int id;
	private String den;
	
	public Deporte() {}

	public Deporte(int id, String den) {
		this.id = id;
		this.den = den;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDen() {
		return den;
	}

	public void setDen(String den) {
		this.den = den;
	}
	
}
