package org.jvalue.ods.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.jvalue.commons.utils.Assert;
import org.jvalue.commons.utils.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;


public class BsonToJsonUtils {

	private BsonToJsonUtils () { }

	public static JsonNode BsonToJson(FindIterable<Document> documents) {
		Assert.assertNotNull(documents);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode result = null;
		for (Document doc : documents) {
			try {
				result = mapper.readTree(doc.toJson());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
