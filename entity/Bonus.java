package fr.glowning.discordminer.entity;

public class Bonus {

	private String type;
	private long content;

	public Bonus(String type, long content) {
		this.type = type;
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getContent() {
		return content;
	}

	public void setContent(long content) {
		this.content = content;
	}
}
