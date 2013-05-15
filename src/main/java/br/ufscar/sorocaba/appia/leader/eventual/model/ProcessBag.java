package br.ufscar.sorocaba.appia.leader.eventual.model;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;

public class ProcessBag {

	@SuppressWarnings("serial")
	@Getter private Set<Identificator> processes = new HashSet<Identificator>() {{
		add(new Identificator(1));
		add(new Identificator(2));
		add(new Identificator(3));
	}};
	
	public void update(Identificator identificator) {
		processes.add(identificator);
	}
	
	public String electLeader() {
		Identificator leader = processes.iterator().next();
		
		for (Identificator identificator : processes) {
			if (leader.getEpoch() <= identificator.getEpoch() && leader.getValue() < identificator.getValue()) {
				leader = identificator;
			}
		}
		
		return String.valueOf(leader.getValue());
	}
}
