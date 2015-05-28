package cl.usach.compiladores.lab1.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import cl.usach.compiladores.lab1.estructuras.Afd;
import cl.usach.compiladores.lab1.estructuras.Afnd;

public class AfndToAfd {
	
	final static Logger logger = Logger.getLogger(AfndToAfd.class);
	private Afnd afnd;
	
	public void construirAfd(List<String> G){
		afnd = new Afnd();
		
		afnd.setEstados(Integer.parseInt(G.get(1)));//Obtener la cantidad de estados
		afnd.setLenguaje(Arrays.asList(G.get(2).split(",")));//obtener el lenguaje
		afnd.setEstadoInicial(G.get(4));//Obtener el estado inicial
		afnd.setEstadosFinales(Arrays.asList(G.get(5).replaceAll("\\[(.*)\\]", "$1").split(",")));//Obtener la lista de estados finales
		logger.debug("Cantidad Estados: "+ afnd.getEstados());
		logger.debug("Lenguaje; " + afnd.getLenguaje());
		logger.debug("Estado inicial : " + afnd.getEstadoInicial());
		logger.debug("Estados Finales: " + afnd.getEstadosFinales());
		List<String> produccionesTxt = Arrays.asList(G.get(3).split("q(\\d)->"));
		produccionesTxt= produccionesTxt.subList(1, produccionesTxt.size());//eliminar misteriosa produccion inicial vacia
		afnd.setProducciones(parseProduccionesTxt(produccionesTxt, afnd.getLenguaje().size()+1));
		
		afndToAfd(afnd, afnd.getEstadoInicial(), new Afd());
		
	}
	
	private void afndToAfd(Afnd afnd, String estadoActual, Afd afd) {
		if(afd.getEstados().contains(estadoActual)){
			logger.debug("Estado antiguo");
		}else{
			logger.debug("Agregar Estado nuevo: "+estadoActual);
			afd.getEstados().add(estadoActual);
			logger.debug("Buscar clausuras...");
			List<String> clausuras = new ArrayList<String>();
			String[] estadosActuales = estadoActual.split(" ");
			if(estadosActuales.length==1){
				//movimiento unico
				int indexNodo = afnd.getEstados().indexOf(estadosActuales[0]);
				String[] clausurasTemp = afnd.getProducciones().get(indexNodo).get(afnd.getProducciones().get(indexNodo).size()-1).split(" ");
				clausuras.add(estadoActual);
				for (int i = 0; i < clausurasTemp.length; i++) {
					if(!clausurasTemp[i].equals("&")){
						clausuras.add(clausurasTemp[i]);
					}
				}
				logger.debug("clausuras " + clausuras);
			}else{
				//movimiento multiple
				for (int i = 0; i < estadosActuales.length; i++) {
					//recorrer los movimientos actuales en busqueda de sus clausuras
				}
			}
		}
	}

	private List<List<String>> parseProduccionesTxt(List<String> produccionesTxt, int cntElemLenguaje){
		List<List<String>> matrizIncidencia = new ArrayList<List<String>>();	
		logger.debug("Construir Matriz de incicendia: ");
		for (int i = 0; i < produccionesTxt.size(); i++) {
			List<String> fila = Arrays.asList(produccionesTxt.get(i).split(","));
			for (int j = 0; j < cntElemLenguaje; j++) {
				fila.set(j, fila.get(j).replaceAll("[\\[,\\]]", ""));
				//logger.debug("i:"+i+", j:"+j+"=> "+ fila.get(j));
			}
			logger.info("Producciones nodo q"+i+": "+fila);
			matrizIncidencia.add(fila);
		}
		return matrizIncidencia;
	}
		
}