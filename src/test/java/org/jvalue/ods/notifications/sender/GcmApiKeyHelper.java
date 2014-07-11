package org.jvalue.ods.notifications.sender;



final class GcmApiKeyHelper {

	private  GcmApiKeyHelper() { }


	private static boolean keySet = false;

	public static void setupKeyResource() {
		if (keySet) return;

		if (!isApiKeyPresent()) GcmApiKey.setKeyResourceName("/googleApi.key.template");

		keySet = true;
	}


	public static boolean isApiKeyPresent() {
		return GcmApiKeyHelper.class.getResource("/googleApi.key") != null;
	}

}
