package br.ufscar.sorocaba.appia.leader.eventual;

import static br.ufscar.sorocaba.appia.util.AppiaUtils.creanteSocketAddress;
import static br.ufscar.sorocaba.appia.util.AppiaUtils.go;
import static br.ufscar.sorocaba.appia.util.AppiaUtils.initAndGo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import lombok.Getter;
import net.sf.appia.core.AppiaEventException;
import net.sf.appia.core.Channel;
import net.sf.appia.core.Direction;
import net.sf.appia.core.Event;
import net.sf.appia.core.Layer;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.channel.ChannelInit;
import net.sf.appia.core.message.Message;
import net.sf.appia.protocols.common.RegisterSocketEvent;
import br.ufscar.sorocaba.appia.datalink.events.TicTacEvent;
import br.ufscar.sorocaba.appia.datalink.model.SimpleMessage;
import br.ufscar.sorocaba.appia.leader.eventual.events.HeartbeatEvent;
import br.ufscar.sorocaba.appia.leader.eventual.events.HeartbeatResponseEvent;
import br.ufscar.sorocaba.appia.leader.eventual.events.RequestEvent;
import br.ufscar.sorocaba.appia.leader.eventual.model.Identificator;
import br.ufscar.sorocaba.appia.leader.eventual.model.IdentificatorMessage;

public class ProcessSession extends Session {

	@Getter private Identificator identificator;
	private String leaderId;	
	
	private MessageReader reader = null;
	
	@Getter private InetSocketAddress home;
	private InetSocketAddress[] addresses;
	
	private ElectLowerEpoch leaderDetector;
	
	public ProcessSession(Layer layer) {
		super(layer);
	}

	@Override
	public void handle(Event event) {
		if (event instanceof ChannelInit) {
			handleChannelInit((ChannelInit) event);
		} else if (event instanceof RequestEvent) {
			handleRequest((RequestEvent) event);
		} else if (event instanceof HeartbeatEvent) {
			handleHeartbeat((HeartbeatEvent) event);
		}
		
		leaderDetector.handle(event);
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
		
		if (reader == null) reader = new MessageReader(init.getChannel());
		
		System.out.println(String.format("\nProcess %s is up and running\n", identificator));
	}

	private void handleRequest(RequestEvent request) {
		if (request.getTo().getPort() != home.getPort()) {
			go(request);
			return;
		}
		
		SimpleMessage simpleMessage = (SimpleMessage) request.getMessage().popObject();
		System.out.println(String.format("[Process %s] received message: %s", identificator, simpleMessage.getContent()));
		go(request);
	}

	private void handleHeartbeat(HeartbeatEvent heartbeat) {
		if (heartbeat.getTo().getPort() != home.getPort()) {
			go(heartbeat);
			return;
		}
		
		Message message = new Message();
		message.pushObject(new IdentificatorMessage(identificator));
		
		HeartbeatResponseEvent heartbeatResponse = new HeartbeatResponseEvent();
		heartbeatResponse.setChannel(heartbeat.getChannel());
		heartbeatResponse.setDir(Direction.DOWN);
		heartbeatResponse.setSourceSession(this);

		heartbeatResponse.to(heartbeat.getTargetAddress()).from(home).setMessage(message);
		
		initAndGo(heartbeatResponse);
	}
	
	public void init(Identificator identificator, String address, InetSocketAddress[] addresses) {
		this.identificator = identificator;
		home = creanteSocketAddress(address);
		this.addresses = addresses;  
		leaderDetector = new ElectLowerEpoch();
	}
	
	public class ElectLowerEpoch {
		
		private Set<Identificator> candidates = new HashSet<Identificator>();
		
		public void handle(Event event) {
			if (event instanceof ChannelInit) {
				handleChannelInit((ChannelInit) event);
			} else if (event instanceof TicTacEvent) {
				handleTicTac((TicTacEvent) event);
			} else if (event instanceof HeartbeatResponseEvent) {
				handleHeartbeatResponse((HeartbeatResponseEvent) event);
			}
		}

		private void handleChannelInit(ChannelInit init) {
			go(init);
			
			long epoch = retrieve();
			store(++epoch);
			
			identificator.changeEpoch(epoch);
			
			sendHeartbeat(init.getChannel());
		}
		
		private void handleTicTac(TicTacEvent event) {
			String newLeaderId = electLeader();
			if (newLeaderId != null && !newLeaderId.equals(leaderId)) {
				leaderId = newLeaderId;
				System.out.println(String.format("[Process %s] new leader: %s", identificator, leaderId));
			}
			sendHeartbeat(event.getChannel());
			go(event);
		}

		public String electLeader() {
			if (candidates.isEmpty()) return String.valueOf(identificator.getValue());
			
			Identificator leader = identificator;
			
			for (Identificator identificator : candidates) {
				if (leader.getEpoch() > identificator.getEpoch()) {
					leader = identificator;
				} else if (leader.getEpoch() == identificator.getEpoch() && leader.getValue() < identificator.getValue()) {
					leader = identificator;
				}
			}
			
			return String.valueOf(leader.getValue());
		}

		
		private void sendHeartbeat(Channel channel) {
			for (InetSocketAddress address : addresses) {
				HeartbeatEvent heartbeat = new HeartbeatEvent();
				heartbeat.setChannel(channel);
				heartbeat.setDir(Direction.DOWN);
				heartbeat.setSourceSession(ProcessSession.this);

				heartbeat.to(address).from(home);
				
				initAndGo(heartbeat);
			}
			candidates.clear();
		}
		
		private void handleHeartbeatResponse(HeartbeatResponseEvent heartbeatResponse) {
			IdentificatorMessage message = (IdentificatorMessage) heartbeatResponse.getMessage().popObject();
			candidates.add(message.getIdentificator());
		}
		
		private void store(Long epoch) {
			PrintWriter printer = null;
			try {
				printer = new PrintWriter(new File(String.format("epoch-%s", identificator)));
				printer.print(epoch);
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			} finally {
				if (printer != null) {
					printer.flush();
					printer.close();
				}
			}
		}
		
		private long retrieve() {
			Scanner scanner = null;
			try {
				scanner = new Scanner(new File(String.format("epoch-%s", identificator)));
				return Long.valueOf(scanner.next());
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			} finally {
				scanner.close();
			}
		}
	}
	
	private class MessageReader extends Thread {

		private boolean ready = false;
		private Channel channel;
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
				System.out.print(String.format("[Process %s](%d) >", identificator, requestCount));
				
				try {
					String token[] = buffer.readLine().split(" to ");
					
					Message message = new Message();
					message.pushObject(new SimpleMessage(requestCount, token[0]));
					
					RequestEvent requestEvent = new RequestEvent();
					requestEvent.to(token[1]).from(home).setMessage(message);
					
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
