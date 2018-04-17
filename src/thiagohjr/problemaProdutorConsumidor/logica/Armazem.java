package thiagohjr.problemaProdutorConsumidor.logica;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;


public class Armazem {
	private int capacidade;
	private int estoque;
	
	private Map<String, Integer> produtos = new TreeMap<>();
	
	private int armazenado;
	private int entregue;
	
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private ReadLock lockLeitura = lock.readLock();
	private WriteLock lockEscrita = lock.writeLock();


	public Armazem(int capacidade, String[] produtos) {
		this.capacidade = capacidade;
		for (String string : produtos) {
			getProdutos().put(string, 0);
		}
	}
	
	boolean armazenar(int quantidade, String produto){
		lockEscrita.lock();
		try {
			if(estoque + quantidade < capacidade) {
				estoque += quantidade;
				armazenado += quantidade;
				produtos.compute(produto, (k, v) -> v + quantidade);
			}else {
				return false;
			}
		}finally {
			lockEscrita.unlock();
		}
		return true;
	}
	
	boolean fornecer(int quantidade, String produto){
		lockEscrita.lock();
		try {
			if(produtos.get(produto) - quantidade >= 0) {
				estoque -= quantidade;
				entregue += quantidade;
				produtos.compute(produto, (k, v) -> v - quantidade);
				return true;
			}
		}finally {
			lockEscrita.unlock();
		}
		return false;
	}
	
	
	public final int getArmazenado() {
		return armazenado;
	}

	public final int getEntregue() {
		return entregue;
	}

	public final int getCapacidade() {
		return capacidade;
	}
	
	public final int getEstoque() {
		lockLeitura.lock();
		try {
			return estoque;
		}finally {
			lockLeitura.unlock();
		}
	}

	public final Map<String, Integer> getProdutos() {
		return produtos;
	}
	
	@Override
	public String toString() {
		return "\n\nArmazem: " + produtos + "\tTotal: " + getEstoque() 
		+ "\t\t Entregas: " + getEntregue() + "\t\t Recebimentos: " + getArmazenado() 
		+ "\t\t Saldo: " + (getArmazenado() - getEstoque() - getEntregue()) + "\n";
	}
}