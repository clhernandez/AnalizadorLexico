package cl.usach.compiladores.lab1.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

public class Init {
	final static Logger logger = Logger.getLogger(Init.class);
	
	public static void main(String[] args) throws IOException {
		try {
			//snnipet lectura de archivo: http://goo.gl/03JBkb
			BufferedReader br = new BufferedReader(new FileReader("C:/Users/clandesta/Documents/workspace/AnalizadorLexico/ej.txt"));
	        String line="init";
			StringBuilder sb = new StringBuilder();
//			int cntLine=0;
			while (line != null) {
				line = br.readLine();
				
				if(line != null && line.contains("#")==false){
//					System.out.println(cntLine+": "+line);
					sb.append(line);
//					cntLine++;
				}
			}
//			System.out.println(sb.toString());
			List<String> entrada = Arrays.asList(sb.toString().split(";"));
//			System.out.println(Arrays.toString(entrada.toArray()));
//			System.out.println(entrada.get(0));
			if(entrada.size()>0){
				
				switch (entrada.get(0).trim()) {
				case "1":
					AfndToAfd builder = new AfndToAfd();
					builder.construirAfd(entrada);
					break;

				default:
					break;
				}
			}
	        
		} catch (IOException e) {
			e.printStackTrace();
		}
	 	
	}

}
