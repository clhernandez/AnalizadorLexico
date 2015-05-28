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
		afnd.setEstadosFinales(Arrays.asList(G.get(5)));//Obtener la lista de estados finales
		logger.debug("Cantidad Estados: "+ afnd.getEstados());
		logger.debug("Lenguaje; " + afnd.getLenguaje());
		
		List<String> produccionesTxt = Arrays.asList(G.get(3).split("q(\\d)->"));
		produccionesTxt= produccionesTxt.subList(1, produccionesTxt.size());//eliminar misteriosa produccion inicial vacia
		for (int j = 0; j < produccionesTxt.size(); j++) {
			logger.debug("produccionesTxt q"+j+": " + produccionesTxt.get(j));
		}
		afnd.setProducciones(parseProduccionesTxt(produccionesTxt, afnd.getLenguaje().size()+1));
		//obtener clausuras de cada produccion
		List<List<String>> listaClausuras = new ArrayList<List<String>>();
		for (int i = 0; i < afnd.getProducciones().size(); i++) {
			//buscar las clausuras para cada nodo
			List<String> clausuras = buscarClausuras(afnd, afnd.getEstados().get(i), new ArrayList<String>());
			logger.debug("C "+afnd.getEstados().get(i)+"- e:" + clausuras);
			listaClausuras.add(clausuras);
		}
		afnd.setClausuras(listaClausuras);
		
		for (int i = 0; i < afnd.getClausuras().size(); i++) {
			logger.info("Clausura q"+i+": "+afnd.getClausuras().get(i));
		}
		
		List<List<String>> afd = new ArrayList<List<String>>();
		construirAfd(afnd, new Afd());
		
		afnd.setClausuras(listaClausuras);
		//construirDelta(afnd.getProducciones(),afnd.getLenguaje(),afnd.getClausuras(), afnd.getEstados());
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
	
	private List<String> buscarClausuras(Afnd afnd, String nodo, List<String> clausuras){
//		#	a	b	c	e
//		q0->[q1],[&],[&],[&]
//		q1->[q2 q3],[q1],[&],[&]
//		q2->[&],[&],[&],[&]
//		q3->[&],[&],[q3],[q1]
//		estados: [q0, q1, q2, q3]
//		nodo: q0
		logger.info("Buscando clausuras para: "+nodo);
		int indexNodo = afnd.getEstados().indexOf(nodo);
		logger.debug("Index de "+nodo+": "+indexNodo);
		logger.debug(afnd.getProducciones().get(indexNodo));

		String mVacios = afnd.getProducciones().get(indexNodo).get(afnd.getLenguaje().size());
		logger.debug("Movimientos vacios desde nodo "+nodo+": "+mVacios);
		if(mVacios.equals("&") || mVacios.equals(nodo)){
			//Si es igual a conjunto vacio
			if(clausuras.contains(nodo)==false){
				//si el nodo que estamos buscando no se encuentra en las clausuras.
				clausuras.add(nodo);
			}
			logger.debug("Clausuras: "+clausuras);
			return clausuras;
		}else{
			//contiene uno o mas movimientos vacios
			clausuras.add(nodo);//se agrega el nodo principal.
			String[] nodoVacio = mVacios.split(" ");//obtener un vector con todos los nodos de movimiento vacio
			for (int i = 0; i < nodoVacio.length; i++) {
				logger.debug("Rec>>Buscar clausuras para nodo "+nodoVacio[i]);
				//por cada nodo con movimiento vacio ir a buscar sus clausuras
				List<String> tempClosure = buscarClausuras(afnd, nodoVacio[i], clausuras);
				for (String string : tempClosure) {
					//se agrega cada elemento que no este en la lista de clausuras actuales.
					if(clausuras.contains(string)==false){
						clausuras.add(string);
						logger.debug("RE>>Clausuras: "+clausuras);
					}
				}
			}
			logger.debug("REC<<");
			
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
	
	private List<String> buscarMovimientos(Afnd afnd, String estado, List<String> producciones){
		if(estado.split(" ").length>1){
			String[] estados = estado.split(" ");
			List<List<String>> tempProducciones = new ArrayList<List<String>>();
			for (int i = 0; i < estados.length; i++) {
				tempProducciones.add(buscarMovimientos(afnd, estados[i], new ArrayList<String>()));
			}
			for (int i = 0; i < tempProducciones.size(); i++) {
				for (int j = 0; j < tempProducciones.get(i).size()-1; j++) {
					String eActual = tempProducciones.get(i).get(j);
					if(i==0){
						producciones.add(eActual);
					}else{
						  
					}
				}
			}
			
			return producciones;
		}else{
			int indexNodo = afnd.getEstados().indexOf(estado);
			logger.debug("Buscar movimientos del estado "+estado);
			if(afnd.getClausuras().get(indexNodo).size()==1){
				//no tiene movimientos vacios a otro nodo
				for (int i = 0; i < afnd.getProducciones().get(indexNodo).size()-1; i++) {
					logger.debug("NODO: "+afnd.getProducciones().get(indexNodo).get(i));
					producciones.add(afnd.getProducciones().get(indexNodo).get(i));
				}
				return producciones;
			}else{
				List<String> produccionesNodo = afnd.getProducciones().get(indexNodo);
				
				for (int i = 0; i < afnd.getClausuras().get(indexNodo).size(); i++) {
					String clausura = afnd.getClausuras().get(indexNodo).get(i);
					if(clausura.equals(estado)==false){
						logger.debug("clausura: "+clausura);
						List<String> tempProducciones = buscarMovimientos(afnd, clausura, producciones);
						logger.debug("producciones clausura "+clausura+":"  + tempProducciones);
						
						for (int j = 0; j <produccionesNodo.size()-1; j++) {
							logger.debug("temp["+j+"]: "+produccionesNodo.get(j));
							logger.debug("tempProducciones.get("+j+"):" + tempProducciones.get(j));
							
							if(produccionesNodo.get(j).equals("&")==true || tempProducciones.get(j).contains(produccionesNodo.get(j))){
								produccionesNodo.set(j, tempProducciones.get(j));
							}if(tempProducciones.get(j).equals("&")==false && tempProducciones.get(j).contains(produccionesNodo.get(j))==false){
								produccionesNodo.set(j, produccionesNodo.get(j) + " " + tempProducciones.get(j));
							}
							logger.debug("R>>" + produccionesNodo.get(j));
						}
						
					}
				}
				return produccionesNodo;
			}
		}
	}
	
	private Afd construirAfd(Afnd afnd, Afd afd){
//		#	a	b	c	e
//		q0->[q1],[&],[&],[&]
//		q1->[q2 q3],[q1],[&],[&]
//		q2->[&],[&],[&],[&]
//		q3->[&],[&],[q3],[q1]
//		estados: [q0, q1, q2, q3]
//		nodoInicial: q0
//		Clausura q0: [q0]
//		Clausura q1: [q1]
//		Clausura q2: [q2]
//		Clausura q3: [q3, q1]
		logger.info("<<Construir AFD>>");
		List<List<String>> mIncicencia = new ArrayList<List<String>>();
		List<String> estados = new ArrayList<String>();
		
		
		boolean elementosNuevos;
		String estadoActual = afnd.getEstadoInicial();
		estados.add(estadoActual);
		
		do {
			elementosNuevos=false;
			
			List<String> produccionesIniciales= buscarMovimientos(afnd, estadoActual, new ArrayList<String>());
			for (String string : produccionesIniciales) {
				if(estados.contains(string)==false){
					estados.add(string);
					elementosNuevos=true;
				}
			}
			logger.debug("Producciones nodo "+estadoActual+": " + produccionesIniciales);
			logger.debug("estados: " + estados);
			logger.debug("estadoActual: " + estadoActual);
			
			
			if(elementosNuevos==true){
				estadoActual = estados.get(estados.indexOf(estadoActual)+1);
				mIncicencia.add(produccionesIniciales);
				
				if(estadoActual.equals("&")){
					//Estado conjunto vacio
					List<String> conjuntoVacio = new ArrayList<String>();
					for (int i = 0; i < afnd.getEstados().size()-1; i++) {
						conjuntoVacio.add("&");
					}
					logger.debug("estadoActual: " + estadoActual);
					logger.debug("conjunto vacio: " + conjuntoVacio);
					estadoActual = estados.get(estados.indexOf(estadoActual)+1);
					mIncicencia.add(conjuntoVacio);

					
				}

			}
			logger.debug("estadoNuevo: " + estadoActual);
			
			
			
			
		} while (elementosNuevos);

		
		
		
		
		
		return null;
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

