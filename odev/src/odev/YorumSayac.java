/**
*
* @author Nihad Asgarov  nihad.asgarov@ogr.sakarya.edu.tr
* @since 10.04.2023
* <p>
*  Verilen Java dosyasındaki yorum satırlarının sayısını ve türlerini sayan ve ayrı ayrı dosyalara kaydeden sınıfs
* </p>
*/

package odev;
import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Scanner;

public class YorumSayac {
	
	public static void main(String[] args) {
		
		boolean bayrak = false;
		String dosyaYolu;
		String javadocYorumString = null;
		
		if (args.length>0) {
			
			dosyaYolu = args[0];
			
		} else {
			
			System.out.println("Dosya yolu giriniz: ");
			
			try (Scanner scanner = new Scanner (System.in)) {
				
				dosyaYolu = scanner.nextLine();
				
			}
		}
		
		try (BufferedReader br = new BufferedReader(new FileReader(dosyaYolu))) {
			
			String satir;
			String mevcutFonksiyon = null;
			int tekSatirYorum = 0;
			int cokSatirliYorum = 0;
			int javadocYorum = 0;
			
			BufferedWriter tekSatirYazici = new BufferedWriter(new FileWriter("teksatir.txt"));
			BufferedWriter cokSatirliYazici = new BufferedWriter(new FileWriter("coksatir.txt"));
			BufferedWriter javadocYazici = new BufferedWriter(new FileWriter("javadoc.txt"));
			
			while ((satir = br.readLine()) != null) {
				
				satir = satir.trim();
				
				Pattern tekSatir = Pattern.compile("//.*");
				Matcher eslesmeTekSatir = tekSatir.matcher(satir);
				
				Pattern sinif = Pattern.compile("class\\s+(\\w+)");
				Matcher eslesmeSinif = sinif.matcher(satir);
				
				if (eslesmeSinif.find()) {
					
					System.out.println("Sinif: " + eslesmeSinif.group(1));
					
				}
				
				if (satir.contains("/**")) {
					
					javadocYorum++;
					StringBuilder javadocYorumMetni = new StringBuilder();
					
					while (!satir.contains("*/")) {
						
						javadocYorumMetni.append(satir).append("\n");
						satir = br.readLine().trim();
						
					}
					
					javadocYorumMetni.append(satir);
					javadocYorumString = javadocYorumMetni.toString() + "\n";
					
				}
				
				if ((satir.contains("public") || satir.contains("protected") || satir.contains("private") || !satir.contains("class")) && satir.contains("{") && satir.contains("(") && satir.contains(")")) {
					
				    bayrak = true;
									
				    int start = satir.indexOf("(");
				    int end = satir.indexOf(")", start);
				                    
				    if(start != -1 && end != -1 && !satir.contains("if") && !satir.contains("else") && !satir.contains("else if") && !satir.contains("do") && !satir.contains("while") && !satir.contains("for") && !satir.contains("foreach") && !satir.contains("switch") && !satir.contains("try") && !satir.contains("catch")) {
				        mevcutFonksiyon = satir.substring(0, start).replaceAll("(\\bpublic\\b|\\bprotected\\b|\\bprivate\\b|\\bvoid\\b|\\bstatic\\b|\\bfinal\\b|\\bString\\b|[(){}])","").trim();
				        if (mevcutFonksiyon.equals(""))
				            mevcutFonksiyon = "Anonymous function";
				        System.out.println("       Fonksiyon: " + mevcutFonksiyon);
				    }
                    
                    if (javadocYorumString != null) {
                    	
                    	javadocYazici.write("Fonksiyon: " + mevcutFonksiyon + "\n" + javadocYorumString + "\n");
                    	javadocYazici.write("-------------------------------------------" + "\n");
                    	
                    }
				}
				
				if (bayrak) {
					
					if (eslesmeTekSatir.find()) {
						
						tekSatirYorum++;
						tekSatirYazici.write("Fonksiyon: " + mevcutFonksiyon + "\n" + eslesmeTekSatir.group() + "\n");
						tekSatirYazici.write("-------------------------------------------" + "\n");
						
					} else if (satir.contains("/*") && !satir.contains("/**")) {
						
						cokSatirliYorum++;
						StringBuilder cokSatirliYorumMetni = new StringBuilder();
						int baslangicIndex = satir.indexOf("/*");
						int bitisIndex = satir.indexOf("*/");
						
						if (bitisIndex != -1) {
							
							cokSatirliYorumMetni.append(satir.substring(baslangicIndex, bitisIndex + 2));
							
						} else {
							
							cokSatirliYorumMetni.append(satir.substring(baslangicIndex)).append("\n");
							satir = br.readLine().trim();
							
							while (!satir.contains("*/")) {
								
								cokSatirliYorumMetni.append(satir).append("\n");
								satir = br.readLine().trim();
								
							}
							
							bitisIndex = satir.indexOf("*/");
							cokSatirliYorumMetni.append(satir.substring(0, bitisIndex + 2));
							
						}
						
						cokSatirliYazici.write("Fonksiyon: " + mevcutFonksiyon + "\n" + cokSatirliYorumMetni.toString() + "\n");
						cokSatirliYazici.write("-------------------------------------------" + "\n");
						
					} else if (satir.contains("/**")) {
						
						javadocYorum++;
						StringBuilder javadocYorumMetni = new StringBuilder();
						int baslangicIndex = satir.indexOf("/**");
						int bitisIndex = satir.indexOf("*/");
						
						if (bitisIndex != -1) {
							
							javadocYorumMetni.append(satir.substring(baslangicIndex, bitisIndex + 2));
							
						} else {
							
							javadocYorumMetni.append(satir.substring(baslangicIndex)).append("\n");
							satir = br.readLine().trim();
							
							while (!satir.contains("*/")) {
								
								javadocYorumMetni.append(satir).append("\n");
								satir = br.readLine().trim();
								
							}
							
							bitisIndex = satir.indexOf("*/");
							javadocYorumMetni.append(satir.substring(0, bitisIndex + 2));
							
						}
						
						javadocYazici.write("Fonksiyon: " + mevcutFonksiyon + "\n" + javadocYorumMetni.toString() + "\n");
						javadocYazici.write("-------------------------------------------" + "\n");
						
					} else if (satir.contains("}")) {
						
						bayrak = false;
						
						System.out.println("                Tek Satir Yorum Sayisi:   " + tekSatirYorum);
						System.out.println("                Çok Satirli Yorum Sayisi: " + cokSatirliYorum);
						System.out.println("                Javadoc Yorum Sayisi:     " + javadocYorum);
						System.out.println("-------------------------------------------");
						
						tekSatirYorum = 0;
						cokSatirliYorum = 0;
						javadocYorum = 0;
						
					}
				}
			}
			
			tekSatirYazici.close();
			cokSatirliYazici.close();
			javadocYazici.close();
			
		} catch (IOException e) {
			
			System.out.println("Error reading file: " + e.getMessage());
			
		}
	}
}