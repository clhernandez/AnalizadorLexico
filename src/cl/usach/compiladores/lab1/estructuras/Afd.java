package cl.usach.compiladores.lab1.estructuras;

import java.util.ArrayList;
import java.util.List;

public class Afd {
	private List<String> estados;
	private List<String> lenguaje;
	private List<List<String>> matrizIncidencia;
	private String estadoInicial;
	private List<String> estadosFinales;
	
	public Afd(){
		this.estados = new ArrayList<String>();
		this.lenguaje = new ArrayList<String>();
		this.matrizIncidencia = new ArrayList<List<String>>();
		this.estadosFinales = new ArrayList<String>();
	}
	
	public List<String> getEstados() {
		return estados;
	}
	public void setEstados(List<String> estados) {
		this.estados = estados;
	}
	public List<String> getLenguaje() {
		return lenguaje;
	}
	public void setLenguaje(List<String> lenguaje) {
		this.lenguaje = lenguaje;
	}
	public List<List<String>> getMatrizIncidencia() {
		return matrizIncidencia;
	}
	public void setMatrizIncidencia(List<List<String>> matrizIncidencia) {
		this.matrizIncidencia = matrizIncidencia;
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

}
