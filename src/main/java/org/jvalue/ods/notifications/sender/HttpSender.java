package org.jvalue.ods.notifications.sender;

import com.fasterxml.jackson.databind.node.ArrayNode;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.notifications.clients.HttpClient;
import org.jvalue.ods.utils.RestCall;
import org.jvalue.ods.utils.RestException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public final class HttpSender extends Sender<HttpClient> {

	@Override
	public SenderResult notifySourceChanged(
			HttpClient client, 
			DataSource source, 
			ArrayNode data) {

		try {
			RestCall.Builder builder = new RestCall.Builder(
					RestCall.RequestType.POST, 
					client.getRestUrl())
				.parameter(client.getSourceParam(), source.getId());

			if (client.getSendData()) {
				String jsonString = URLEncoder.encode(
						data.toString(),
						"UTF-8");
				builder.content("application/json", jsonString.getBytes());
			}

			builder.build().execute();

		} catch (RestException re) {
			return getErrorResult(re);
		} catch (UnsupportedEncodingException uee) {
			throw new RuntimeException(uee);
		}

		return getSuccessResult();
	}


}
