package tipos;

public class Partido {
	int id, dep, tor, cla;
	String fec, hor, des;
	public Partido() {}
	public Partido(int id, int dep, String fec, String hor, String des, int tor, int cla) {
		this.id = id;
		this.dep = dep;
		this.tor = tor;
		this.fec = fec;
		this.hor = hor;
		this.des = des;
		this.cla = cla;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getDep() {
		return dep;
	}
	public void setDep(int dep) {
		this.dep = dep;
	}
	public int getTor() {
		return tor;
	}
	public void setTor(int tor) {
		this.tor = tor;
	}
	public String getFec() {
		return fec;
	}
	public void setFec(String fec) {
		this.fec = fec;
	}
	public String getHor() {
		return hor;
	}
	public void setHor(String hor) {
		this.hor = hor;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public int getCla() {
		return cla;
	}
	public void setCla(int cla) {
		this.cla = cla;
	}
	
	
}
