package org.jvalue.ods.notifications.sender;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.generic.GenericEntity;
import org.jvalue.ods.notifications.NotificationException;
import org.jvalue.ods.notifications.NotificationSender;
import org.jvalue.ods.notifications.clients.HttpClient;
import org.jvalue.ods.utils.RestCall;
import org.jvalue.ods.utils.RestException;

import com.fasterxml.jackson.databind.ObjectMapper;


final class HttpSender implements NotificationSender<HttpClient> {

	private static final ObjectMapper mapper = new ObjectMapper();
	

	@Override
	public void notifySourceChanged(
			HttpClient client, 
			DataSource source, 
			GenericEntity data) throws NotificationException {

		try {
			RestCall.Builder builder = new RestCall.Builder(
					RestCall.RequestType.POST, 
					client.getRestUrl())
				.parameter(client.getSourceParam(), source.getId());

			if (client.getSendData()) {
				String jsonString = URLEncoder.encode(
						mapper.valueToTree(data).toString(),
						"UTF-8");
				builder.content("application/json", jsonString.getBytes());
			}

			builder.build().execute();

		} catch (RestException re) {
			throw new NotificationException(re);
		} catch (UnsupportedEncodingException uee) {
			throw new NotificationException(uee);
		}
	}


}
