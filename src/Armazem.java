import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class Armazem {
	private int capacidade;
	private int estoque;
	
	private int armazenado;
	private int entregue;
	
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private ReadLock lockLeitura = lock.readLock();
	private WriteLock lockEscrita = lock.writeLock();

	public Armazem(int capacidade) {
		this.capacidade = capacidade;
		this.estoque = 0;
	}
	
	boolean armazenar(int quantidade){
		lockEscrita.lock();
		try {
			if(estoque + quantidade <= capacidade) {
				estoque += quantidade;
				armazenado += quantidade;
			}else {
				return false;
			}
		}finally {
			lockEscrita.unlock();
		}
		return true;
	}
	
	boolean fornecer(int quantidade){
		lockEscrita.lock();
		try {
			if(estoque - quantidade >=0) {
				estoque -= quantidade;
				entregue += quantidade;
			}else {
				return false;
			}
		}finally {
			lockEscrita.unlock();
		}
		return true;
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
}
