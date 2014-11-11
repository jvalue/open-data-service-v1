package org.jvalue.ods.notifications.sender;


import com.google.inject.Inject;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.notifications.clients.ClientVisitor;
import org.jvalue.ods.notifications.clients.GcmClient;
import org.jvalue.ods.notifications.clients.HttpClient;

public final class SenderVisitor implements ClientVisitor<SenderVisitor.DataEntry, SenderResult> {

	private final Sender<GcmClient> gcmSender;
	private final Sender<HttpClient> httpSender;

	@Inject
	public SenderVisitor(
			Sender<GcmClient> gcmSender,
			Sender<HttpClient> httpSender) {

		this.gcmSender = gcmSender;
		this.httpSender = httpSender;
	}



	@Override
	public SenderResult visit(GcmClient client, DataEntry entry) {
		return gcmSender.notifySourceChanged(client, entry.getSource(), entry.getData());
	}


	@Override
	public SenderResult visit(HttpClient client, DataEntry entry) {
		return httpSender.notifySourceChanged(client, entry.getSource(), entry.getData());
	}


	public static class DataEntry {

		private final DataSource source;
		private final Object data;

		public DataEntry(DataSource source, Object data) {
			this.source = source;
			this.data = data;
		}

		public DataSource getSource() {
			return source;
		}

		public Object getData() {
			return data;
		}

	}

}
