package thiagohjr.problemaProdutorConsumidor.logica;

import thiagohjr.problemaProdutorConsumidor.aplicacao.Main;

public class Produtor implements Runnable {

	private int maxProducao;
	private int minProducao;
	private int tempoProducao;

	private String produto;
	
	private Armazem armazem;

	public Produtor(int minProducao, int maxProducao, int tempoProducao, Armazem armazem, String produto) {
		this.maxProducao = maxProducao;
		this.minProducao = minProducao;
		this.tempoProducao = tempoProducao;
		this.armazem = armazem;
		this.produto = produto;
	}


	@Override
	public void run() {
		int producao;
		while(true) {
			producao = Main.geradorInteiros(minProducao, maxProducao);
			while(!this.armazem.armazenar(producao, produto)) {
				if(this.armazem.getProdutos().get(produto) == 0) {
					int disponivel = armazem.getCapacidade() - armazem.getEstoque() - 1;
					if(this.armazem.armazenar(disponivel, produto)) {
						producao -= disponivel;
					}
				}
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
