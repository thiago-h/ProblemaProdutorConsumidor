package thiagohjr.problemaProdutorConsumidor.logica;
import java.util.ArrayList;

import thiagohjr.problemaProdutorConsumidor.aplicacao.Main;

public class Comprador implements Runnable {

	private int minEspera;
	private int maxEspera;
	
	
	private ArrayList<Mercado> mercados = new ArrayList<Mercado>();
	
	

	public Comprador(int minEspera, int maxEspera, ArrayList<Mercado> mercados) {
		this.minEspera = minEspera;
		this.maxEspera = maxEspera;
		this.mercados = mercados;
	}



	@Override
	public void run() {
		Mercado mercado;
		while(true) {
			try {
				mercado = mercados.get(Main.geradorInteiros(mercados.size()));
				while(!mercado.fornecer(Main.geradorInteiros(mercado.getEstoque()+1), 
						Main.getProdutos()[Main.geradorInteiros(Main.getNumProdutores())]));
				Thread.sleep(Main.geradorInteiros(minEspera, maxEspera));
			}catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}