package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	private Graph<Player, DefaultWeightedEdge> grafo;
	private Map<Integer, Player> idMap;
	private PremierLeagueDAO dao;
	private List<Player> dreamTeam;
	
	public Model() {
		this.idMap = new HashMap<>();
		this.dao = new PremierLeagueDAO();
		
		this.dao.listAllPlayers(idMap);
	}
	
	public void creaGrafo(double x) {
		// creo la struttura del grafo
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		// aggiungo i vertici
		Graphs.addAllVertices(this.grafo, this.dao.getVertici(x, idMap));
		
		// aggiungo gli archi
		for(Adiacenza a : this.dao.getArchi(x)) {
			Graphs.addEdge(this.grafo, this.idMap.get(a.getP1()), this.idMap.get(a.getP2()), 
					a.getPeso());
		}
		
		// console
		System.out.printf("Vertici: %d\n", this.grafo.vertexSet().size());
		System.out.printf("Archi: %d\n", this.grafo.edgeSet().size());
		
	}
	
	public Player trovaTopPlayer() {
		int max = 0;
		Player topPlayer = null;
		
		// ciclo su tutti i vertici del grafo, quello con più archi uscenti sarà il top player
		for(Player p : this.grafo.vertexSet()) {
			if(this.grafo.outDegreeOf(p) > max) {
				max = this.grafo.outDegreeOf(p);
				topPlayer = p;
			}
		}
		
		return topPlayer;
	}
	
	public List<Player> listaBattuti(Player p) {
		List<Player> battuti = new ArrayList<>();
		List<Adiacenza> adiacenze = new ArrayList<>();

		// ciclo su tutti gli archi uscenti dal vertice
		for(DefaultWeightedEdge e : this.grafo.outgoingEdgesOf(p)) {
			Adiacenza a = new Adiacenza(this.grafo.getEdgeSource(e).getPlayerID(), 
					this.grafo.getEdgeTarget(e).getPlayerID(), this.grafo.getEdgeWeight(e));
			adiacenze.add(a);
		}
		
		// ordino la lista di adiacenze in modo decrescente per peso
		Collections.sort(adiacenze);
		
		// itero sulla lista di adiacenze per salvare i giocatori battuti
		for(Adiacenza a : adiacenze) {
			battuti.add(this.idMap.get(a.getP2()));
		}
		
		return battuti;
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public boolean isGrafoCreato() {
		if(this.grafo == null) {
			return false;
		}
		return true;
	}
	
	/*
	 * dato un numero di giocatori k, trova il team con il maggior grado di titolarità
	 */
	public List<Player> trovaDreamTeam(int k) {
		this.dreamTeam = new ArrayList<>();
		List<Player> parziale = new ArrayList<>();
		this.cerca(parziale, k);
		return this.dreamTeam;
	}
	
	private void cerca(List<Player> parziale, int k) {
		// condizione di terminazione
		if(parziale.size() == k) {
			// soluzione totale
			
			// controllo soluzione ottima
			if(this.calcolaTitolarita(parziale) > this.calcolaTitolarita(this.dreamTeam)) {
				this.dreamTeam = new ArrayList<Player>(parziale);
			}
			return;
		}
		
		for(Player p : this.grafo.vertexSet()) {
			// se non è già presente, provo ad aggiungere il giocatore
			if(!parziale.contains(p)) {
				parziale.add(p);
				
				// controllo la soluzione parziale
				if(this.isIdoneo(p, parziale)) {
					// soluzione valida
					this.cerca(parziale, k);
				}
				
				// backtracking
				parziale.remove(p);
			}
		}
	}

	private boolean isIdoneo(Player p, List<Player> parziale) {
		for(Player pi : parziale) {
			for(Player battuto : this.listaBattuti(pi)) {
				if(p.equals(battuto)) {
					return false;
				}
			}
		}
		return true;
	}

	public int calcolaTitolarita(List<Player> team) {
		int titolarita = 0;
		for(Player p : team) {
			int pesoUscenti = 0;
			int pesoEntranti = 0;
			
			for(DefaultWeightedEdge e : this.grafo.outgoingEdgesOf(p)) {
				pesoUscenti += this.grafo.getEdgeWeight(e);
			}
			for(DefaultWeightedEdge e : this.grafo.incomingEdgesOf(p)) {
				pesoEntranti += this.grafo.getEdgeWeight(e);
			}
			titolarita += pesoUscenti-pesoEntranti;
		}
		return titolarita;
	}
}
