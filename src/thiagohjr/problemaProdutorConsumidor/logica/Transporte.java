package thiagohjr.problemaProdutorConsumidor.logica;


public class Transporte implements Runnable, Comparable<Transporte>{

	private int velocidade;
	private int distancia;
	private int cargaMaxima;
	
	private int quantidade;
	private String produto;
	
	private volatile boolean disponivel;
	
	private Armazem armazem;
	private Mercado mercado;
	
	private volatile boolean carregado;

	public Transporte(int velocidade, int distancia, int cargaMaxima, Armazem armazem, Mercado mercado) {
		this.velocidade = velocidade;
		this.distancia = distancia;
		this.cargaMaxima = cargaMaxima;
		this.armazem = armazem;
		this.mercado = mercado;
		this.disponivel = true;
		this.carregado = false;
	}
	
	@Override
	public void run() {
		try {
			//System.out.println("Transporte" + this.toString() + "\tbuscando " + produto + ": " + quantidade);
			Thread.sleep(distancia/velocidade);
			while(!armazem.fornecer(quantidade,produto)){
				int disponivel = armazem.getProdutos().get(produto);
				if(armazem.fornecer(disponivel, produto)) {
					quantidade -= disponivel;
				}
				Thread.sleep(1000);
			}
			carregado = true;
			Thread.sleep(distancia/velocidade);
			while(!mercado.armazenar(quantidade,produto)){
				Thread.sleep(1000);
			}
			carregado = false;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			disponivel = true;
			//System.out.println("Transporte" + this.toString() + "\tentregou " + produto + ": " + quantidade);
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
	
	public final void setProduto(String produto) {
		this.produto = produto;
	}

	public final void setDisponivel(boolean disponivel) {
		this.disponivel = disponivel;
	}

	@Override
	public int compareTo(Transporte t) {
		Integer velocidade = this.velocidade;
		return velocidade.compareTo(t.velocidade);
	}
	
	@Override
	public String toString() {
		return "Disponível: " + disponivel + " " + (disponivel ? "" : (carregado ? "Carregado com: " : "Buscando: ") + quantidade + " " + produto);
	}

}
