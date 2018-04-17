package thiagohjr.problemaProdutorConsumidor.aplicacao;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import thiagohjr.problemaProdutorConsumidor.logica.Armazem;
import thiagohjr.problemaProdutorConsumidor.logica.Comprador;
import thiagohjr.problemaProdutorConsumidor.logica.Mercado;
import thiagohjr.problemaProdutorConsumidor.logica.Produtor;

public class Main {

	static private Random r = new Random();
	
	static private String produtos[];
	static private int tempoProducao[];
	static private int producaoMin[];
	static private int producaoMax[];
	static private int capacidadeMercado[];
	static private int distanciaArmazem[];
	static private double estoqueCritico[];
	static private double reabastecimento[];
	static private int numTransportes[];
	static private ArrayList<Integer[]> velocidadeTransporte = new ArrayList<Integer[]>();
	static private ArrayList<Integer[]> capacidadeTransporte = new ArrayList<Integer[]>();
	static private int intervaloComprasMin[];
	static private int intervaloComprasMax[];
	
	static private int numMercados;
	static private int numCompradores;
	static private int numProdutores;
	
	static private int capacidadeArmazem;
	
	static private ArrayList<Mercado> mercados = new ArrayList<Mercado>();
	static private ArrayList<Comprador> compradores = new ArrayList<Comprador>();
	static private ArrayList<Produtor> produtores = new ArrayList<Produtor>();
	static private Armazem armazem;
	
	private static ExecutorService e;

	public static void main(String[] args) {
		String nomeArquivo = "config.txt";
		try(InputStreamReader arquivoConfiguracoes = new InputStreamReader(new FileInputStream(nomeArquivo), "UTF-8")){
			importarConfiguracoes(arquivoConfiguracoes);
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		e = Executors.newFixedThreadPool(numMercados + numCompradores + numProdutores);
		gerarObjetos();
		
		
		while(true) {
			System.out.println(armazem);
			for(int i = 0; i < numMercados; i++) {
				System.out.println(i + ". " + mercados.get(i));
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public static int geradorInteiros() {
		return r.nextInt();
	}
	
	public static int geradorInteiros(int valor) {
		return r.nextInt(valor);
	}
	
	public static int geradorInteiros(int valorMin, int valorMax) {
		return r.nextInt(valorMax - valorMin) + valorMin + 1;
	}

	public static final String[] getProdutos() {
		return produtos;
	}

	public static final int getNumMercados() {
		return numMercados;
	}

	public static final int getNumCompradores() {
		return numCompradores;
	}

	public static final int getNumProdutores() {
		return numProdutores;
	}
	
	private static void gerarObjetos() {
		armazem = new Armazem(capacidadeArmazem, produtos);
		
		for(int i = 0; i < numProdutores; i++) {
			produtores.add(new Produtor(producaoMin[i], producaoMax[i], tempoProducao[i], armazem, produtos[i]));
			e.execute(produtores.get(i));
		}
		for(int i = 0; i < numMercados; i++) {
			mercados.add(new Mercado(estoqueCritico[i], reabastecimento[i], capacidadeMercado[i], numTransportes[i], armazem, 
					produtos, distanciaArmazem[i], velocidadeTransporte.get(i), capacidadeTransporte.get(i)));
			e.execute(mercados.get(i));
		}
		for(int i = 0; i < numCompradores; i++) {
			compradores.add(new Comprador(intervaloComprasMin[i], intervaloComprasMax[i],mercados));
			e.execute(compradores.get(i));
		}
	}
	
	private static void importarConfiguracoes(InputStreamReader arquivoConfiguracoes) throws IOException {
		String aux[] = null;
		int qtdTransportes = 0;
		Properties configuracoes = new Properties();
		configuracoes.load(arquivoConfiguracoes);		
		
		numMercados = Integer.parseInt(configuracoes.getProperty("numMercados"));
		
		aux = new String[numMercados];
		capacidadeMercado = new int[numMercados];
		aux = configuracoes.getProperty("capacidadeMercado").split(",");
		for(int i = 0; i < numMercados; i++) {
			capacidadeMercado[i] = Integer.parseInt(aux[i]);
		}
		aux = configuracoes.getProperty("distanciaArmazem").split(",");
		distanciaArmazem = new int[numMercados];
		for(int i = 0; i < numMercados; i++) {
			distanciaArmazem[i] = Integer.parseInt(aux[i]);
		}
		estoqueCritico = new double[numMercados];
		aux = configuracoes.getProperty("estoqueCritico").split(",");
		for(int i = 0; i < numMercados; i++) {
			estoqueCritico[i] = Double.parseDouble(aux[i]);
		}
		reabastecimento = new double[numMercados];
		aux = configuracoes.getProperty("reabastecimento").split(",");
		for(int i = 0; i < numMercados; i++) {
			reabastecimento[i] = Double.parseDouble(aux[i]);
		}
		numTransportes = new int[numMercados];
		aux = configuracoes.getProperty("numTransportes").split(",");
		for(int i = 0; i < numMercados; i++) {
			numTransportes[i] = Integer.parseInt(aux[i]);
			qtdTransportes += numTransportes[i];
		}
		aux = new String[qtdTransportes];
		aux = configuracoes.getProperty("velocidadeTransporte").split(",");
		int indice = 0;
		for(int i = 0; i < numMercados; i++) {
			Integer[] valores = new Integer[numTransportes[i]];
			for(int j = 0; j < numTransportes[i]; j++) {
				valores[j] = Integer.valueOf(aux[indice]);
				indice++;
			}
			velocidadeTransporte.add(valores);
		}
		aux = configuracoes.getProperty("capacidadeTransporte").split(",");
		indice = 0;
		for(int i = 0; i < numMercados; i++) {
			Integer[] valores = new Integer[numTransportes[i]];
			for(int j = 0; j < numTransportes[i]; j++) {
				valores[j] = Integer.valueOf(aux[indice]);
			}
			capacidadeTransporte.add(valores);
		}
		
		
		numCompradores = Integer.parseInt(configuracoes.getProperty("numCompradores"));

		aux = new String[numCompradores];
		intervaloComprasMin = new int[numCompradores];
		aux = configuracoes.getProperty("intervaloComprasMin").split(",");
		for(int i = 0; i < numCompradores; i++) {
			intervaloComprasMin[i] = Integer.parseInt(aux[i]);
		}
		intervaloComprasMax = new int[numCompradores];
		aux = configuracoes.getProperty("intervaloComprasMax").split(",");
		for(int i = 0; i < numCompradores; i++) {
			intervaloComprasMax[i] = Integer.parseInt(aux[i]);
		}
		
		
		numProdutores = Integer.parseInt(configuracoes.getProperty("numProdutores"));

		aux = new String[numProdutores];
		tempoProducao = new int[numProdutores];
		aux = configuracoes.getProperty("tempoProducao").split(",");
		for(int i = 0; i < numProdutores; i++) {
			tempoProducao[i] = Integer.parseInt(aux[i]);
		}
		producaoMin = new int[numProdutores];
		aux = configuracoes.getProperty("producaoMin").split(",");
		for(int i = 0; i < numProdutores; i++) {
			producaoMin[i] = Integer.parseInt(aux[i]);
		}
		producaoMax = new int[numProdutores];
		aux = configuracoes.getProperty("producaoMax").split(",");
		for(int i = 0; i < numProdutores; i++) {
			producaoMax[i] = Integer.parseInt(aux[i]);
		}
		
		
		capacidadeArmazem = Integer.parseInt(configuracoes.getProperty("capacidadeArmazem"));
		

		produtos = configuracoes.getProperty("produtos").split(",");
		int numProdutos = produtos.length;
		if(numProdutos < numProdutores) {
			aux = new String[numProdutores];
			for(int i = 0; i < numProdutores; i++) {
				aux[i] = produtos[i % numProdutos];
			}
			produtos = aux;
		}
		if(numProdutos > numProdutores) {
			aux = new String[numProdutores];
			for(int i = 0; i < numProdutores; i++) {
				aux[i] = produtos[i];
			}
			produtos = aux;
		}
	}

}