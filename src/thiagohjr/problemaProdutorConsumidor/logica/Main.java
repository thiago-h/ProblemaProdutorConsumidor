package thiagohjr.problemaProdutorConsumidor.logica;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

	static Random r = new Random();

	public static void main(String[] args) {
		int numMercados = 3;
		int numCompradores = 50;
		int numProdutores = 10;
		
		
		
		ArrayList<Mercado> mercados = new ArrayList<Mercado>();
		ArrayList<Comprador> compradores = new ArrayList<Comprador>();
		ArrayList<Produtor> produtores = new ArrayList<Produtor>();
		
		ExecutorService e = Executors.newFixedThreadPool(numMercados + numCompradores + numProdutores);
		
		Armazem armazem = new Armazem(1000);
		for(int i = 0; i < numProdutores; i++) {
			produtores.add(new Produtor(300, 200, r.nextInt(5000), armazem));
			e.execute(produtores.get(i));
		}
		for(int i = 0; i < numMercados; i++) {
			mercados.add(new Mercado(500, r.nextInt(2) + 1, armazem));
			e.execute(mercados.get(i));
		}
		for(int i = 0; i < numCompradores; i++) {
			compradores.add(new Comprador(mercados));
			e.execute(compradores.get(i));
		}
		
		while(true) {
			System.out.println("\n\nArmazem: " + armazem.getEstoque() 
						+ "\t\t Entregas: " + armazem.getEntregue() + "\t\t Recebimentos: " + armazem.getArmazenado() 
						+ "\t\t Saldo: " + (armazem.getArmazenado() - armazem.getEstoque() - armazem.getEntregue()) + "\n");
			for(int i = 0; i < numMercados; i++) {
				System.out.println("Mercado " + i + ": " + mercados.get(i).getEstoque() 
						+ "\t\t Vendas: " + mercados.get(i).getEntregue() + "\t\t Abastecimento: " + mercados.get(i).getArmazenado() 
						+ "\t\t Saldo: " + (mercados.get(i).getArmazenado() - mercados.get(i).getEntregue() - mercados.get(i).getEstoque()));
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

}
