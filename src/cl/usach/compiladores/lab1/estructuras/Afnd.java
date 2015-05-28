package cl.usach.compiladores.lab1.estructuras;

import java.util.ArrayList;
import java.util.List;

public class Afnd {
	
	private List<String> lenguaje;
	private List<String> estados;
	private List<List<String>> producciones;
	private List<List<String>> clausuras;
	private String estadoInicial;
	private List<String> estadosFinales;

	public List<String> getLenguaje() {
		return lenguaje;
	}
	public void setLenguaje(List<String> lenguaje) {
		this.lenguaje = lenguaje;
	}	
	public List<String> getEstados() {
		return estados;
	}
	public void setEstados(int cantidadEstados) {
		estados = new ArrayList<String>();
		for (int i = 0; i < cantidadEstados; i++) {
			estados.add("q"+i);
		}
	}
	public void setEstados(List<String> estados) {
		this.estados = estados;
	}
	public List<List<String>> getProducciones() {
		return producciones;
	}
	public void setProducciones(List<List<String>> list) {
		this.producciones = list;
	}
	public String getEstadoInicial() {
		return estadoInicial;
	}
	public void setEstadoInicial(String estadoInicial) {
		this.estadoInicial = estadoInicial;
	}
	public List<String> getEstadosFinales() {
		return estadosFinales;
	}
	public void setEstadosFinales(List<String> estadosFinales) {
		this.estadosFinales = estadosFinales;
	}
	public List<List<String>> getClausuras() {
		return clausuras;
	}
	public void setClausuras(List<List<String>> clausuras) {
		this.clausuras = clausuras;
	}
	
}
