package br.ufscar.sorocaba.appia.leader.eventual.model;

import java.io.Serializable;

import lombok.Getter;

public @Getter class Identificator implements Serializable {

	private static final long serialVersionUID = -8023022763205760427L;
	
	private long value;
	private long epoch = 0L;
	
	public Identificator(long value) {
		this.value = value;
	}

	public void changeEpoch(long epoch) {
		this.epoch = epoch;
	}
	
	@Override
	public String toString() {
		return String.valueOf(value);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (value ^ (value >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Identificator other = (Identificator) obj;
		if (value != other.value)
			return false;
		return true;
	}
}
