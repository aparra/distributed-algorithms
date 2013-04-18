package br.ufscar.sorocaba.appia.services.talk.model;

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
}
