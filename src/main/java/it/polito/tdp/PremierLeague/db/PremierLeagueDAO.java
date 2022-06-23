package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Player;

public class PremierLeagueDAO {
	
	public void listAllPlayers(Map<Integer, Player> idMap){
		String sql = "SELECT * FROM Players";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				idMap.put(res.getInt("PlayerID"), player);
			}
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Player> getVertici(double x, Map<Integer, Player> idMap){
		String sql = "select playerid, avg(`Goals`) as avg "
				+ "from actions "
				+ "group by `PlayerID` "
				+ "having avg(`Goals`) > ?";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDouble(1, x);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				result.add(idMap.get(res.getInt("playerid")));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Adiacenza> getArchi(double x){
		String sql = "select a1.playerid as p1, a2.playerid as p2, (sum(a1.timeplayed)-sum(a2.timeplayed)) "
				+ "as delta "
				+ "from actions a1, actions a2 "
				+ "where a1.playerid in (select playerid "
				+ "	from actions "
				+ "	group by `PlayerID` "
				+ "	having avg(`Goals`) > ?) "
				+ "and a2.playerid in (select playerid "
				+ "	from actions "
				+ "	group by `PlayerID` "
				+ "	having avg(`Goals`) > ?) "
				+ "and a1.matchid=a2.matchid "
				+ "and a1.playerid<>a2.playerid "
				+ "and a1.teamid<>a2.`TeamID` "
				+ "and a1.starts=1 "
				+ "and a1.starts=a2.starts "
				+ "group by a1.playerid, a2.playerid "
				+ "having delta>0";
		List<Adiacenza> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDouble(1, x);
			st.setDouble(2, x);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				result.add(new Adiacenza(res.getInt("P1"), res.getInt("p2"), res.getDouble("delta")));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
