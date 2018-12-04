package it.lvassallo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "player")
public class Player {

	@Id
	@Column(name = "name")
	private String name;

	@Column(name = "position")
	private int position;

	public String getName() {
		return name;
	}
	
	public Player name(String name) {
		this.name = name;
		return this;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPosition() {
		return position;
	}
	
	public Player position(int position) {
		this.position = position;
		return this;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return "Player [name=" + name + ", position=" + position + "]";
	}

}