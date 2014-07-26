package org.jvalue.ods.notifications.sender;



final class GcmApiKeyHelper {

	private  GcmApiKeyHelper() { }


	private static final String 
		RESOURCE_NAME = "/googleApi.key",
		DUMMY_RESOURCE_NAME = "/googleApi.key.template";


	public static String getResourceName() {
		if (isApiKeyPresent()) return RESOURCE_NAME;
		else return DUMMY_RESOURCE_NAME;
	}


	public static boolean isApiKeyPresent() {
		return GcmApiKeyHelper.class.getResource(RESOURCE_NAME) != null;
	}

}
