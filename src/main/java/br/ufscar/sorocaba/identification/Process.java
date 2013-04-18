package br.ufscar.sorocaba.identification;

import lombok.Getter;
import br.com.bfgex.RegexGen;

public class Process {

	private Long messageCount = 0L;
	
	@Getter private String id;

	public Process() {
		id = RegexGen.of("\\d{20}");
	}
	
	public synchronized Message sendMessage() {
		return new Message(id, String.valueOf(++messageCount));
	}
}
