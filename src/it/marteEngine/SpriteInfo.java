package it.marteEngine;

public class SpriteInfo implements Comparable<SpriteInfo> {

	private String id;
	private String type;

	public SpriteInfo(String id, String type) {
		this.id = id;
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int compareTo(SpriteInfo o) {
		Integer id = new Integer(o.getId());
		Integer myId = new Integer(getId());
		if (myId < id) {
			return -1;
		} else if (myId > id) {
			return 1;
		}
		return 0;
	}

}