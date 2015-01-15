package org.jvalue.ods.notifications.sender;


import org.jvalue.ods.api.notifications.Client;
import org.jvalue.ods.api.notifications.ClientVisitor;
import org.jvalue.ods.api.notifications.GcmClient;
import org.jvalue.ods.api.notifications.HttpClient;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * Cache for {@link org.jvalue.ods.notifications.sender.Sender} instance while data
 * is being streamed to clients.
 */
public final class SenderCache {

	private final Map<Client, Sender<?>> senderMap = new HashMap<>();
	private final SenderCreatorVisitor senderCreatorVisitor;

	@Inject
	public SenderCache(SenderFactory senderFactory) {
		this.senderCreatorVisitor = new SenderCreatorVisitor(senderFactory);
	}


	@SuppressWarnings("unchecked")
	public <T extends Client> Sender<T> get(T client) {
		if (!senderMap.containsKey(client)) senderMap.put(client, client.accept(senderCreatorVisitor, null));
		return (Sender<T>) senderMap.get(client);
	}


	public void release(Client client) {
		senderMap.remove(client);
	}


	private static final class SenderCreatorVisitor implements ClientVisitor<Void, Sender<?>> {

		private final SenderFactory senderFactory;

		public SenderCreatorVisitor(SenderFactory senderFactory) {
			this.senderFactory = senderFactory;
		}

		@Override
		public Sender<GcmClient> visit(GcmClient client, Void param) {
			return senderFactory.createGcmSender(client);
		}

		@Override
		public Sender<HttpClient> visit(HttpClient client, Void param) {
			return senderFactory.createHttpSender(client);
		}
	}

}
