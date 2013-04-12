package br.ufscar.sorocaba.appia.services.job;

import static br.ufscar.sorocaba.appia.util.EventUtils.go;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import net.sf.appia.core.AppiaEventException;
import net.sf.appia.core.Channel;
import net.sf.appia.core.Direction;
import net.sf.appia.core.Event;
import net.sf.appia.core.Layer;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.channel.ChannelInit;
import br.ufscar.sorocaba.appia.services.job.events.JobConfirmEvent;
import br.ufscar.sorocaba.appia.services.job.events.JobRequestEvent;
import br.ufscar.sorocaba.appia.services.job.events.TransformationFaultEvent;

public class JobApplicationSession extends Session {

	public JobApplicationSession(Layer layer) {
		super(layer);
	}

	public void handle(Event event) {
		if (event instanceof ChannelInit) {
			handleChannelInit((ChannelInit) event);
		} else if (event instanceof TransformationFaultEvent) {
			handleTransformationFault((TransformationFaultEvent) event);
		} else if (event instanceof JobConfirmEvent) {
			handleJobConfirm((JobConfirmEvent) event);
		}
	}

	private PrintReader reader = null;

	private void handleChannelInit(ChannelInit init) {
		go(init);
		if (reader == null) reader = new PrintReader(init.getChannel());
	}

	private void handleTransformationFault(TransformationFaultEvent alarm) {
		System.out.println("[JobApplication: received FAULT]");
		go(alarm);
	}
	
	private void handleJobConfirm(JobConfirmEvent confirm) {
		System.out.println(String.format("[JobApplication: received confirmation of request %d]", confirm.getRequestId()));
		go(confirm);
	}

	private class PrintReader extends Thread {

		public boolean ready = false;
		public Channel channel;
		private BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));

		public PrintReader(Channel channel) {
			ready = true;
			if (this.channel == null) this.channel = channel;
			this.start();
		}

		public void run() {
			int requestCount = 0;
			
			boolean running = true;
			
			while (running) {
				++requestCount;
				System.out.print(String.format("[JobApplication](%d) >", requestCount));
				
				try {
					JobRequestEvent request = new JobRequestEvent();
					request.setId(requestCount);
					request.setContent(buffer.readLine());
					
					request.asyncGo(channel, Direction.DOWN);
					
				} catch (AppiaEventException | IOException e) {
					e.printStackTrace();
				}

				try {
					Thread.sleep(1500);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				synchronized (this) {
					if (!ready) running = false;
				}
			}
		}
	}
}
