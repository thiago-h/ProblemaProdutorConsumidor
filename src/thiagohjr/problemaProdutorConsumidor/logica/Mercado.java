package thiagohjr.problemaProdutorConsumidor.logica;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Mercado extends Armazem implements Runnable{
	
	private double estoqueCritico;
	private double reabastecimento;
	
	private int distanciaArmazem;
	private int numTransportes;
	private ArrayList<Transporte> transportes = new ArrayList<Transporte>();
	
	Transporte transporte;
	
	Armazem armazem;
	
	ExecutorService e;

	public Mercado(double estoqueCritico, double reabastecimento, int capacidade, int numTransportes, Armazem armazem, 
			String[] produtos, int distanciaArmazem, Integer[] velocidadeTransporte, Integer[] capacidadeTransporte) {
		super(capacidade,produtos);
		this.estoqueCritico = estoqueCritico;
		this.reabastecimento = reabastecimento;
		this.numTransportes = numTransportes;
		this.armazem = armazem;
		this.distanciaArmazem = distanciaArmazem;
		Transporte t;
		for(int i = 0; i < numTransportes; i++) {
			t = new Transporte(velocidadeTransporte[i], capacidadeTransporte[i], distanciaArmazem, armazem, this);
			transportes.add(t);
		}
		transportes.sort(null);
	}

	@Override
	public void run() {
		e = Executors.newFixedThreadPool(numTransportes);
		//Transporte transporte;
		while(true) {
			Set<Map.Entry<String, Integer>> produtos = this.getProdutos().entrySet();
			for (Entry<String, Integer> entry : produtos) {
				if(entry.getValue() < getCapacidade() * estoqueCritico) {
					transporte = escolherTransporte();
					if(transporte != null) {
						int quantidade = (Double.valueOf(getCapacidade() * reabastecimento).intValue());
						transporte.setQuantidade((quantidade <= transporte.getCargaMaxima() ? quantidade : transporte.getCargaMaxima()));
						transporte.setProduto(entry.getKey());
						e.execute(transporte);
					}
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
			//System.out.println(t);
			t.setCarregado(false);
			t.setDisponivel(false);
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
	
	@Override
	public String toString() {
		return "Mercado: " + getProdutos() + "\tTotal: " + getEstoque() 
				+ "\t\t Vendas: " + getEntregue() + "\t\t Abastecimento: " + getArmazenado() 
				+ "\t\t Saldo: " + (getArmazenado() - getEntregue() - getEstoque()) + "\tTransportes: " + transportes.toString();
	}
}