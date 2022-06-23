package it.polito.tdp.PremierLeague.model;

public class TestModel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Model m = new Model();
		m.creaGrafo(0.5);
		Player p = m.trovaTopPlayer();
		System.out.println(p);
		System.out.println(m.listaBattuti(p));
		System.out.println("Dream team: " + m.trovaDreamTeam(3));
	}

}
