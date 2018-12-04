package it.lvassallo.service.dto;

import java.io.Serializable;

public class MovePlayerDTO implements Serializable {

	private static final long serialVersionUID = -794082355459339667L;

	private String playerName;
	private int dado1 = 0;
	private int dado2 = 0;

	public MovePlayerDTO(String name) {
		playerName = name;
	}

	public MovePlayerDTO(String name, int _dado1, int _dado2) {
		playerName = name;
		dado1 = _dado1;
		dado2 = _dado2;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String namePlayer) {
		this.playerName = namePlayer;
	}

	public int getDado1() {
		return dado1;
	}

	public void setDado1(int dado1) {
		this.dado1 = dado1;
	}

	public int getDado2() {
		return dado2;
	}

	public void setDado2(int dado2) {
		this.dado2 = dado2;
	}

	@Override
	public String toString() {
		return "MovePlayerDTO [namePlayer=" + playerName + ", dado1=" + dado1 + ", dado2=" + dado2 + "]";
	}

}
