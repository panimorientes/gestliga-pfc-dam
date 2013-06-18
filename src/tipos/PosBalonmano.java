package tipos;

public class PosBalonmano {
	private int id;
	private String den, abr;
	
	public PosBalonmano() {}

	public PosBalonmano(int id, String den, String abr) {
		this.id = id;
		this.den = den;
		this.abr = abr;
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

	public String getAbr() {
		return abr;
	}

	public void setAbr(String abr) {
		this.abr = abr;
	}
	
}
