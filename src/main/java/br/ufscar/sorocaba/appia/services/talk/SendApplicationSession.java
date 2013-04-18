package br.ufscar.sorocaba.appia.services.talk;

import static br.ufscar.sorocaba.appia.util.AppiaUtils.creanteSocketAddress;
import static br.ufscar.sorocaba.appia.util.AppiaUtils.go;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

import net.sf.appia.core.AppiaEventException;
import net.sf.appia.core.Channel;
import net.sf.appia.core.Direction;
import net.sf.appia.core.Event;
import net.sf.appia.core.Layer;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.channel.ChannelInit;
import net.sf.appia.core.message.Message;
import net.sf.appia.protocols.common.RegisterSocketEvent;
import br.ufscar.sorocaba.appia.services.talk.events.ReceiveConfirmEvent;
import br.ufscar.sorocaba.appia.services.talk.events.SendRequestEvent;
import br.ufscar.sorocaba.appia.services.talk.model.SimpleMessage;

public class SendApplicationSession extends Session {

	private MessageReader reader = null;
	private InetSocketAddress home = creanteSocketAddress("localhost", 9988);

	public SendApplicationSession(Layer layer) {
		super(layer);
	}

	public void handle(Event event) {
		if (event instanceof ChannelInit) {
			handleChannelInit((ChannelInit) event);
		} else if (event instanceof ReceiveConfirmEvent) {
			handleReceiveConfirm((ReceiveConfirmEvent) event);
		}
	}

	private void handleChannelInit(ChannelInit init) {
		go(init);
		
		try {
			RegisterSocketEvent registerSocket = new RegisterSocketEvent(init.getChannel(), Direction.DOWN, this);
			registerSocket.port = home.getPort();
			registerSocket.localHost = home.getAddress();
			
			go(registerSocket);
		} catch (AppiaEventException e) {
			throw new RuntimeException(e);
		}
		
		System.out.println("\nSend channel is up and running\n");
		
		if (reader == null) reader = new MessageReader(init.getChannel());
	}

	private void handleReceiveConfirm(ReceiveConfirmEvent event) {
		SimpleMessage message = (SimpleMessage) event.getMessage().popObject();
		System.out.println(String.format("[SendApplication] received request confirmation %d\n", message.getId()));
		go(event);
	}

	private class MessageReader extends Thread {

		public boolean ready = false;
		public Channel channel;
		private BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));

		public MessageReader(Channel channel) {
			ready = true;
			if (this.channel == null) this.channel = channel;
			this.start();
		}

		public void run() {
			int requestCount = 0;
			
			boolean running = true;

			while (running) {
				++requestCount;
				System.out.print(String.format("[SendApplication](%d) >", requestCount));
				
				try {
					Message message = new Message();
					message.pushObject(new SimpleMessage(requestCount, buffer.readLine()));

					SendRequestEvent requestEvent = new SendRequestEvent();
					requestEvent.to("localhost:8899").from(home).setMessage(message);
					
					System.out.println("\nSending message...");
					
					requestEvent.asyncGo(channel, Direction.DOWN);
					
				} catch (AppiaEventException | IOException e) {
					e.printStackTrace();
				}

				try {
					Thread.sleep(1500);
				} catch (Exception e) {
					e.printStackTrace();
				}

				synchronized (this) {
					if (!ready) running = false;
				}
			}
		}
	}
}
