package br.ufscar.sorocaba.appia.leader.eventual.executor;

import static br.ufscar.sorocaba.appia.util.AppiaUtils.creanteSocketAddress;
import static br.ufscar.sorocaba.appia.util.AppiaUtils.createQoS;
import static br.ufscar.sorocaba.appia.util.AppiaUtils.start;

import java.net.InetSocketAddress;

import net.sf.appia.core.Appia;
import net.sf.appia.core.AppiaCursorException;
import net.sf.appia.core.Channel;
import net.sf.appia.core.ChannelCursor;
import net.sf.appia.core.Layer;
import net.sf.appia.core.QoS;
import net.sf.appia.protocols.udpsimple.UdpSimpleLayer;
import br.ufscar.sorocaba.appia.datalink.perfect.ClockLayer;
import br.ufscar.sorocaba.appia.leader.eventual.ProcessLayer;
import br.ufscar.sorocaba.appia.leader.eventual.ProcessSession;
import br.ufscar.sorocaba.appia.leader.eventual.model.Identificator;

public class ProcessExecutor2 {

	public static void main(String[] args) {
		Layer[] queue = { new UdpSimpleLayer(), new ClockLayer(), new ProcessLayer() };

		QoS myQoS = createQoS("talk_stack", queue);
		Channel channel = myQoS.createUnboundChannel("talk_channel");
		
	    bind((ProcessLayer) queue[2], channel);

		start(channel);

		System.out.println("Starting Appia...");
		Appia.run();
	}

	private static void bind(ProcessLayer layer, Channel channel) {
		ChannelCursor channelCursor = channel.getCursor();
	    channelCursor.top();
	    
	    ProcessSession session = (ProcessSession) layer.createSession();
	    session.init(new Identificator(2), "localhost:9889", 
	    		new InetSocketAddress[] { creanteSocketAddress("localhost:9888"), creanteSocketAddress("localhost:9890") });
	    
	    try {
	    	channelCursor.setSession(session);
	    } catch (AppiaCursorException e) { throw new RuntimeException(e); }
	}
}
