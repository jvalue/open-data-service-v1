package org.jvalue.ods.notifications.sender;


import com.google.common.base.Objects;

import org.jvalue.ods.api.notifications.Client;
import org.jvalue.ods.api.notifications.ClientVisitor;
import org.jvalue.ods.api.notifications.GcmClient;
import org.jvalue.ods.api.notifications.HttpClient;
import org.jvalue.ods.api.sources.DataSource;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * Cache for {@link org.jvalue.ods.notifications.sender.Sender} instance while data
 * is being streamed to clients.
 */
public final class SenderCache {

	private final Map<SenderKey, Sender<?>> senderMap = new HashMap<>();
	private final SenderCreatorVisitor senderCreatorVisitor;

	@Inject
	public SenderCache(SenderFactory senderFactory) {
		this.senderCreatorVisitor = new SenderCreatorVisitor(senderFactory);
	}


	@SuppressWarnings("unchecked")
	public <T extends Client> Sender<T> get(DataSource source, T client) {
		SenderKey key = new SenderKey(source, client);
		if (!senderMap.containsKey(key)) senderMap.put(key, client.accept(senderCreatorVisitor, null));
		return (Sender<T>) senderMap.get(key);
	}


	public void release(DataSource source, Client client) {
		SenderKey key = new SenderKey(source, client);
		senderMap.remove(key);
	}


	private static final class SenderCreatorVisitor implements ClientVisitor<DataSource, Sender<?>> {

		private final SenderFactory senderFactory;

		public SenderCreatorVisitor(SenderFactory senderFactory) {
			this.senderFactory = senderFactory;
		}

		@Override
		public Sender<GcmClient> visit(GcmClient client, DataSource source) {
			return senderFactory.createGcmSender(source, client);
		}

		@Override
		public Sender<HttpClient> visit(HttpClient client, DataSource source) {
			return senderFactory.createHttpSender(source, client);
		}
	}


	private static final class SenderKey {

		private final Client client;
		private final DataSource source;

		public SenderKey(DataSource source, Client client) {
			this.source = source;
			this.client = client;
		}

		@Override
		public boolean equals(Object other) {
			if (other == null || !(other instanceof SenderKey)) return false;
			if (other == this) return true;
			SenderKey key = (SenderKey) other;
			return Objects.equal(client, key.client)
					&& Objects.equal(source, key.source);
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(client, source);
		}

	}

}
