package thiagohjr.problemaProdutorConsumidor.aplicacao;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import thiagohjr.problemaProdutorConsumidor.logica.Armazem;
import thiagohjr.problemaProdutorConsumidor.logica.Comprador;
import thiagohjr.problemaProdutorConsumidor.logica.Mercado;
import thiagohjr.problemaProdutorConsumidor.logica.Produtor;

public class Main {

	static private Random r = new Random();
	//static private String produtos[] = {"manga", "maçã", "pera", "morango", "melancia", "melão", "abacaxi", "laranja", "banana", "limão"};
	static private String produtos[] = {"manga", "maçã"};
	static private int numMercados = 3;
	static private int numCompradores = 10;
	static private int numProdutores = 2;

	public static void main(String[] args) {
		
		
		
		
		
		ArrayList<Mercado> mercados = new ArrayList<Mercado>();
		ArrayList<Comprador> compradores = new ArrayList<Comprador>();
		ArrayList<Produtor> produtores = new ArrayList<Produtor>();
		
		ExecutorService e = Executors.newFixedThreadPool(numMercados + numCompradores + numProdutores);
		
		Armazem armazem = new Armazem(1500, produtos);
		
		for(int i = 0; i < numProdutores; i++) {
			produtores.add(new Produtor(300, 200, geradorInteiros(3000, 5000), armazem, produtos[i]));
			e.execute(produtores.get(i));
		}
		for(int i = 0; i < numMercados; i++) {
			mercados.add(new Mercado(500, r.nextInt(2) + 1, armazem, produtos));
			e.execute(mercados.get(i));
		}
		for(int i = 0; i < numCompradores; i++) {
			compradores.add(new Comprador(mercados));
			e.execute(compradores.get(i));
		}
		
		while(true) {
			/*System.out.println("\n\nArmazem: " + armazem.getEstoque() 
						+ "\t\t Entregas: " + armazem.getEntregue() + "\t\t Recebimentos: " + armazem.getArmazenado() 
						+ "\t\t Saldo: " + (armazem.getArmazenado() - armazem.getEstoque() - armazem.getEntregue()) + "\n");*/
			System.out.println(armazem);
			for(int i = 0; i < numMercados; i++) {
				/*System.out.println("Mercado " + i + ": " + mercados.get(i).getEstoque() 
						+ "\t\t Vendas: " + mercados.get(i).getEntregue() + "\t\t Abastecimento: " + mercados.get(i).getArmazenado() 
						+ "\t\t Saldo: " + (mercados.get(i).getArmazenado() - mercados.get(i).getEntregue() - mercados.get(i).getEstoque()));*/
				System.out.println(i + ". " + mercados.get(i));
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	public static int geradorInteiros() {
		return r.nextInt();
	}
	
	public static int geradorInteiros(int valor) {
		return r.nextInt(valor);
	}
	
	public static int geradorInteiros(int valorMin, int valorMax) {
		return r.nextInt(valorMax - valorMin) + valorMin + 1;
	}

	public static final String[] getProdutos() {
		return produtos;
	}

	public static final int getNumMercados() {
		return numMercados;
	}

	public static final int getNumCompradores() {
		return numCompradores;
	}

	public static final int getNumProdutores() {
		return numProdutores;
	}

}
