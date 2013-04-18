package br.ufscar.sorocaba.identification;

import lombok.Getter;

public class Message {

	@Getter private String id;

	public Message(String processId, String id) {
		this.id = String.format("%s:%s", processId, id);
	}
}
