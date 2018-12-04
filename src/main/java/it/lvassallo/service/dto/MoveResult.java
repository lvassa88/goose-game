package it.lvassallo.service.dto;

public class MoveResult {

	private int position;

	private String message;
	
	public MoveResult(int position, String message) {
		this.position = position;
		this.message = message;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "MoveResult [position=" + position + ", message=" + message + "]";
	}

}
