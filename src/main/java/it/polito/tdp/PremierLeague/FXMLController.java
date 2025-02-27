/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Model;
import it.polito.tdp.PremierLeague.model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnTopPlayer"
    private Button btnTopPlayer; // Value injected by FXMLLoader

    @FXML // fx:id="btnDreamTeam"
    private Button btnDreamTeam; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="txtGoals"
    private TextField txtGoals; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	// pulisco l'area di testo
    	this.txtResult.clear();
    	
    	// controllo x
    	double x = 0;
    	try {
    		x = Double.parseDouble(this.txtGoals.getText());
    	}
    	catch(NumberFormatException e) {
    		e.printStackTrace();
    		this.txtResult.setText("Errore: devi interire un valore numerico per x.");
    		return;
    	}
    	
    	// creo il grafo
    	this.model.creaGrafo(x);
    	
    	// stampo il risultato
    	this.txtResult.setText(String.format("Grafo creato\n"
    									   + "# VERTICI: %d\n"
    									   + "# ARCHI: %d", this.model.nVertici(), 
    									   this.model.nArchi()));
    }

    @FXML
    void doDreamTeam(ActionEvent event) {
    	// pulisco l'area di testo
    	this.txtResult.clear();
    	
    	// controllo il grafo
    	if(!this.model.isGrafoCreato()) {
    		this.txtResult.setText("Errore: devi prima creare il grafo.");
    		return;
    	}
    	
    	// controllo k
    	int k = 0;
    	try {
    		k = Integer.parseInt(this.txtK.getText());
    	}
    	catch(NumberFormatException e) {
    		e.printStackTrace();
    		this.txtResult.setText("Errore: devi inserire un valore intero per k.");
    		return;
    	}
    	
    	// trovo il dream team
    	List<Player> team = this.model.trovaDreamTeam(k);
    	
    	// stampo il risultato
    	this.txtResult.setText("DREAM TEAM:\n");
    	for(Player p : team) {
    		this.txtResult.appendText(p.toString() + "\n");
    	}
    	this.txtResult.appendText(String.format("Grado titolarità: %d", this.model.calcolaTitolarita(team)));
    }

    @FXML
    void doTopPlayer(ActionEvent event) {
    	// pulisco l'area di testo
    	this.txtResult.clear();
    	
    	// controllo il grafo
    	if(!this.model.isGrafoCreato()) {
    		this.txtResult.setText("Errore: devi prima creare il grafo.");
    		return;
    	}
    	
    	// trovo il top player
    	Player p = this.model.trovaTopPlayer();
    	
    	// trovo i giocatori battuti
    	List<Player> battuti = this.model.listaBattuti(p);
    	
    	// stampo il risultato
    	this.txtResult.setText(String.format("TOP PLAYER: %s\n\n", p.toString()));
    	this.txtResult.appendText("AVVERSARI BATTUTI:\n");
    	for(Player pi : battuti) {
    		this.txtResult.appendText(pi.toString() + "\n");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnTopPlayer != null : "fx:id=\"btnTopPlayer\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnDreamTeam != null : "fx:id=\"btnDreamTeam\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGoals != null : "fx:id=\"txtGoals\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
