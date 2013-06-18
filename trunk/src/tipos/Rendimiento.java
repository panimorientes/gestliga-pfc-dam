package tipos;

import java.util.ArrayList;

public class Rendimiento {
	int equ;
	ArrayList<String> pro;
	public Rendimiento() {
	}
	public Rendimiento(int equ, ArrayList<String> pro) {
		this.equ = equ;
		this.pro = (ArrayList<String>) pro.clone();
	}
	public int getEqu() {
		return equ;
	}
	public void setEqu(int equ) {
		this.equ = equ;
	}
	public ArrayList<String> getPro() {
		return pro;
	}
	public void setPro(ArrayList<String> pro) {
		this.pro = pro;
	}
	
	
}
