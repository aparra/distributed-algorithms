package br.ufscar.sorocaba.appia.leader.eventual.model;

import java.io.Serializable;

import lombok.Getter;

public class IdentificatorMessage implements Serializable {

	private static final long serialVersionUID = 2486183201726810373L;

	@Getter private Identificator identificator;

	public IdentificatorMessage(Identificator identificator) {
		this.identificator = identificator;
	}
}
