package cl.usach.compiladores.lab1.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.List;

import org.apache.log4j.Logger;

import cl.usach.compiladores.lab1.estructuras.Afnd;

public class AfndToAfd {
	
	final static Logger logger = Logger.getLogger(AfndToAfd.class);
	private Afnd afnd;
	
	public void construirAfd(List<String> G){
		afnd = new Afnd();
		afnd.setEstados(Integer.parseInt(G.get(1)));//Obtener la cantidad de estados
		afnd.setLenguaje(Arrays.asList(G.get(2).split(",")));
		logger.debug("Cantidad Estados: "+ afnd.getEstados());
		logger.debug("Lenguaje; " + afnd.getLenguaje());
		List<String> produccionesTxt = Arrays.asList(G.get(3).split("q(\\d)->"));
		produccionesTxt= produccionesTxt.subList(1, produccionesTxt.size());//eliminar misteriosa produccion inicial vacia
		for (int j = 0; j < produccionesTxt.size(); j++) {
			logger.debug("produccionesTxt q"+j+": " + produccionesTxt.get(j));
		}
		afnd.setProducciones(construirMatrizIncidencia(produccionesTxt, afnd.getLenguaje().size()+1));
		afnd.setClausuras(construirClausuras(afnd.getProducciones(), afnd.getLenguaje().size()+1));

		construirDelta(afnd.getProducciones(),afnd.getLenguaje(),afnd.getClausuras(), afnd.getEstados());
	}
	
	private List<List<String>> construirMatrizIncidencia(List<String> produccionesTxt, int cntElemLenguaje){
		List<List<String>> matrizIncidencia = new ArrayList<List<String>>();	
		logger.debug("Matriz de incicendia: ");
		for (int i = 0; i < produccionesTxt.size(); i++) {
			List<String> fila = Arrays.asList(produccionesTxt.get(i).split(","));
			for (int j = 0; j < cntElemLenguaje; j++) {
				fila.set(j, fila.get(j).replaceAll("[\\[,\\]]", ""));
				logger.debug("i"
						+ ":"+i+", j:"+j+"=> "+ fila.get(j));
			}
			matrizIncidencia.add(fila);
		}
		return matrizIncidencia;
	}
	
	private List<String> construirClausuras(List<List<String>> matrizIncidencia, int cntElemLenguaje){
		//encontrar clausuras de todos los elementos
		List<String> clausuras = new ArrayList<String>();
		for (int i = 0; i < matrizIncidencia.size(); i++) {
			String elementoActual = "q"+i;
			String clausura = matrizIncidencia.get(i).get(matrizIncidencia.get(i).size()-1);
			if(clausura.equals("&")){
				clausuras.add(elementoActual);
			}else{
				if(clausura.contains(elementoActual)){
					clausuras.add(clausura);
				}else{
					clausuras.add(clausura+" "+elementoActual);
				}
			}
			logger.info("Clausuras q"+i+": "+clausuras.get(i));
		}
		return clausuras;
	}
	
	private List<List<String>> limpiarMatrizIncidencia(List<List<String>> mat){
		List<List<String>> newM = new ArrayList<List<String>>();
		
		for (int i = 0; i < mat.size(); i++) {
			List<String> x = new ArrayList<String>();
			for (int j = 0; j < mat.get(i).size()-1; j++) {
				x.add(mat.get(i).get(j));
			}
			newM.add(x);
		}
		return newM;
	}
	
	private List<List<String>> construirDelta(List<List<String>> matrizIncidencia, List<String> alfabeto, List<String> clausuras, List<String> estados){
		List<String> estadosNuevos = new ArrayList<String>();
		estadosNuevos.add(estados.get(0));//añadir por defecto el primer estado.
		List<List<String>> matriz = limpiarMatrizIncidencia(matrizIncidencia);
		
		List<List<String>> delta;
		printMatriz(matriz);
		
		logger.info("Estados: " + estados);
		boolean elementosNuevos;
		do {
			delta = new ArrayList<List<String>>();
			
			logger.warn("Buscando Elementos Nuevos...");
			elementosNuevos=false;
			logger.warn("clausuras: " + clausuras);
			logger.warn("estados: "+estados);
			
			for (int i = 0; i < matriz.size(); i++) {
				
				List<String> dFila = new ArrayList<String>();
				logger.warn("i: "+i);
				
				for (int j = 0; j < matriz.get(i).size(); j++) {
					
					String elemActual = matriz.get(i).get(j);
					logger.warn(estados.get(i)+"->"+alfabeto.get(j)+"->"+elemActual);
					dFila.add(elemActual);
					
					if(estadosNuevos.contains(elemActual)==false){
						elementosNuevos=true;
						logger.warn("Estado Nuevo: "+elemActual);
						estadosNuevos.add(matriz.get(i).get(j));
						logger.info("Obtener producciones del nuevo elemento");
					}
				}
				delta.add(dFila);
			}
			elementosNuevos=false;//TODO QUITAR
			if(elementosNuevos==true){
				matriz= delta;
				estados = estadosNuevos;
				printMatriz(matriz);
			}
			
		} while (elementosNuevos);
		logger.info("Estados: " + estadosNuevos);
		return delta;
	}
	
	private void printMatriz(List<List<String>> mat){
		logger.warn("PRINT MATRIZ");
		for (int i = 0; i < mat.size(); i++) {
			for (int j = 0; j < mat.get(i).size(); j++) {
				logger.warn("i:"+i+", j:"+j+"=>"+mat.get(i).get(j));
			}
		}
		logger.warn("<<FIN>>");
	}
	
}

