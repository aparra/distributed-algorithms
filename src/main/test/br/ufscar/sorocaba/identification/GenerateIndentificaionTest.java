package br.ufscar.sorocaba.identification;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class GenerateIndentificaionTest {

	@Test
	public void generateUniqueIdentification() throws InterruptedException {
		final Set<String> processes = new HashSet<String>();
		final Set<String> messages = new HashSet<String>();

		ExecutorService service = Executors.newFixedThreadPool(10);
		
		for (int i = 0; i < 10; i++) {
			service.execute(new Runnable() {
				@Override
				public void run() {
					Process process = new Process();
					processes.add(process.getId());
					
					for (int j = 0; j < 30; j++) messages.add(process.sendMessage().getId());
				}
			});
		}
		
		service.awaitTermination(10, TimeUnit.SECONDS);

		for (String message : messages) {
			System.out.println(message);
		}
		
		assertEquals(10, processes.size());
		assertEquals(300, messages.size());
	}
}
