package thiagohjr.problemaProdutorConsumidor.logica;

public class Transporte implements Runnable, Comparable<Transporte>{

	private int velocidade;
	private int distancia;
	private int cargaMaxima;
	
	private int quantidade;
	
	private volatile boolean disponivel;
	
	Armazem armazem;
	Mercado mercado;

	public Transporte(int velocidade, int distancia, int cargaMaxima, Armazem armazem, Mercado mercado) {
		this.velocidade = velocidade;
		this.distancia = distancia;
		this.cargaMaxima = cargaMaxima;
		this.armazem = armazem;
		this.mercado = mercado;
		this.disponivel = true;
	}
	
	@Override
	public void run() {
		try {
			disponivel = false;
			Thread.sleep(distancia/velocidade);
			while(!armazem.fornecer(quantidade)){
				Thread.sleep(1000);
			}
			Thread.sleep(distancia/velocidade);
			while(!mercado.armazenar(quantidade)){
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			disponivel = true;
		}
	}

	public final int getVelocidade() {
		return velocidade;
	}

	public final int getDistancia() {
		return distancia;
	}

	public final int getCargaMaxima() {
		return cargaMaxima;
	}

	public final int getQuantidade() {
		return quantidade;
	}

	public final boolean isDisponivel() {
		return disponivel;
	}

	public final Armazem getArmazem() {
		return armazem;
	}

	public final Mercado getMercado() {
		return mercado;
	}

	public final void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	@Override
	public int compareTo(Transporte t) {
		Integer velocidade = this.velocidade;
		return velocidade.compareTo(t.velocidade);
	}

}
