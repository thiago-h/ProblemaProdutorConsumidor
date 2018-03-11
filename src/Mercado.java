import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class Mercado implements Runnable{
	private final int maxDistancia = 1000;
	private final int minDistancia = 100;

	private final int maxVelocidadeTransporte = 120;
	private final int minVelocidadeTransporte = 60;

	private final int maxCapacidadeTransporte = 120;
	private final int minCapacidadeTransporte = 60;
	
	private final double estoqueCritico = 0.2;
	private final double reabastecimento = 0.3;
	
	
	private int vendas;
	private int abastecimento;
		
	private int capacidade;
	private int estoque;
	private int distanciaArmazem;
	private int numTransportes;
	private ArrayList<Transporte> transportes = new ArrayList<Transporte>();
	
	
	Armazem armazem;
	
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private ReadLock lockLeitura = lock.readLock();
	private WriteLock lockEscrita = lock.writeLock();
	
	ExecutorService e;

	public Mercado(int capacidade, int numTransportes, Armazem armazem) {
		Random r = new Random();
		this.vendas = 0;
		this.abastecimento = 0;
		this.capacidade = capacidade;
		this.estoque = 0;
		this.numTransportes = numTransportes;
		this.armazem = armazem;
		this.distanciaArmazem = r.nextInt(maxDistancia - minDistancia) + minDistancia + 1;
		int velocidade;
		int cargaMaxima;
		Transporte t;
		for(int i = 0; i < numTransportes; i++) {
			velocidade = r.nextInt(maxVelocidadeTransporte - minVelocidadeTransporte) + minVelocidadeTransporte + 1;
			cargaMaxima = r.nextInt(maxCapacidadeTransporte - minCapacidadeTransporte) + minCapacidadeTransporte + 1;
			t = new Transporte(velocidade, distanciaArmazem, cargaMaxima, armazem, this);
			transportes.add(t);			
		}
	}

	@Override
	public void run() {
		e = Executors.newFixedThreadPool(numTransportes);
		Transporte transporte;
		while(true) {
			if(estoque < capacidade * estoqueCritico) {
				transporte = escolherTransporte();
				if(transporte != null) {
					transporte.setQuantidade((Double.valueOf(capacidade * reabastecimento).intValue()));
					e.execute(transporte);
				}
			}
		}
	}
	
	private Transporte escolherTransporte() {
		transportes.sort(null);
		int i = 0;
		Transporte t = null;
		while(i < numTransportes && !transportes.get(i).isDisponivel()) {
			i++;
		}
		if(i < numTransportes) {
			t = transportes.get(i);
		}
		return t;
	}

	boolean armazenar(int quantidade){
		lockEscrita.lock();
		try {
			if(estoque + quantidade <= capacidade) {
				estoque += quantidade;
				abastecimento += quantidade;
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
				vendas += quantidade;
			}else {
				return false;
			}
		}finally {
			lockEscrita.unlock();
		}
		return true;
	}
	
	public final int getVendas() {
		return vendas;
	}
	
	public final int getAbastecimento() {
		return abastecimento;
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

	public final int getDistanciaArmazem() {
		return distanciaArmazem;
	}

	public final int getNumTransportes() {
		return numTransportes;
	}

	public final ArrayList<Transporte> getTransportes() {
		return transportes;
	}

	public final Armazem getArmazem() {
		return armazem;
	}
}
