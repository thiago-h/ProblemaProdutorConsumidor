import java.util.Random;

public class Produtor implements Runnable {

	private int maxProducao;
	private int minProducao;
	private int tempoProducao;
	
	private Armazem armazem;

	public Produtor(int maxProducao, int minProducao, int tempoProducao, Armazem armazem) {
		this.maxProducao = maxProducao;
		this.minProducao = minProducao;
		this.tempoProducao = tempoProducao;
		this.armazem = armazem;
	}


	@Override
	public void run() {
		int producao;
		Random r = new Random();
		while(true) {
			producao = r.nextInt(maxProducao - minProducao) + this.minProducao + 1;
			while(!this.armazem.armazenar(producao)) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(tempoProducao);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}


	public final int getMaxProducao() {
		return maxProducao;
	}

	public final int getMinProducao() {
		return minProducao;
	}

}
