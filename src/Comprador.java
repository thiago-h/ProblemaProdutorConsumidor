import java.util.ArrayList;
import java.util.Random;

public class Comprador implements Runnable {

	private final int minEspera = 1000;
	private final int maxEspera = 5000;
	
	ArrayList<Mercado> mercados = new ArrayList<Mercado>();
	
	public Comprador(ArrayList<Mercado> mercados) {
		this.mercados = mercados;
	}

	@Override
	public void run() {
		Random r = new Random();
		Mercado mercado;
		while(true) {
			try {
				mercado = mercados.get(r.nextInt(mercados.size()));
				while(!mercado.fornecer(r.nextInt(mercado.getEstoque() + 1) + 1));
				Thread.sleep(r.nextInt(maxEspera - minEspera) + minEspera + 1);
			}catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
