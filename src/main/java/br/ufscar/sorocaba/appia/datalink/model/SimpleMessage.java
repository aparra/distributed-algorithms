package br.ufscar.sorocaba.appia.datalink.model;

import java.io.Serializable;

import lombok.Getter;

public @Getter class SimpleMessage implements Serializable {

	private static final long serialVersionUID = 1153199386527053883L;
	
	private int id;
	private String content;

	public SimpleMessage(int id, String content) {
		this.id = id;
		this.content = content;
	}
	
	@Override
	public int hashCode() {
		return 31 * 1 + id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		
		SimpleMessage other = (SimpleMessage) obj;
		if (id != other.id) return false;
		
		return true;
	}
}
