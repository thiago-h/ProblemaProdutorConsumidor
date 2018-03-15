package thiagohjr.problemaProdutorConsumidor.logica;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Mercado extends Armazem implements Runnable{
	private final int maxDistancia = 1000;
	private final int minDistancia = 100;

	private final int maxVelocidadeTransporte = 120;
	private final int minVelocidadeTransporte = 60;

	private final int maxCapacidadeTransporte = 120;
	private final int minCapacidadeTransporte = 60;
	
	private final double estoqueCritico = 0.2;
	private final double reabastecimento = 0.3;
	
	
	
	private int distanciaArmazem;
	private int numTransportes;
	private ArrayList<Transporte> transportes = new ArrayList<Transporte>();
	
	
	Armazem armazem;
	
	ExecutorService e;

	public Mercado(int capacidade, int numTransportes, Armazem armazem) {
		super(capacidade);
		this.numTransportes = numTransportes;
		this.armazem = armazem;
		this.distanciaArmazem = Main.geradorInteiros(minDistancia, maxDistancia);
		int velocidade;
		int cargaMaxima;
		Transporte t;
		for(int i = 0; i < numTransportes; i++) {
			velocidade = Main.geradorInteiros(minVelocidadeTransporte, maxVelocidadeTransporte);
			cargaMaxima = Main.geradorInteiros(minCapacidadeTransporte, maxCapacidadeTransporte);
			t = new Transporte(velocidade, distanciaArmazem, cargaMaxima, armazem, this);
			transportes.add(t);			
		}
		transportes.sort(null);
	}

	@Override
	public void run() {
		e = Executors.newFixedThreadPool(numTransportes);
		Transporte transporte;
		while(true) {
			if(this.getEstoque() < getCapacidade() * estoqueCritico) {
				transporte = escolherTransporte();
				if(transporte != null) {
					transporte.setQuantidade((Double.valueOf(getCapacidade() * reabastecimento).intValue()));
					e.execute(transporte);
				}
			}
		}
	}
	
	private Transporte escolherTransporte() {
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
