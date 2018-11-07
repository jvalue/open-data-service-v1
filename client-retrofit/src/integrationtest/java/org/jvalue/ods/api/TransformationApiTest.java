package org.jvalue.ods.api;

import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.api.views.generic.TransformationFunction;
import org.jvalue.ods.api.views.generic.TransformationFunctionDescription;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

import java.util.List;

public class TransformationApiTest extends AbstractApiTest {

	protected DataTransformationApi transformationApi;


	protected static final String EXTENSION_FUNCTION = "function transform(doc) {" +
										"	if(doc != null) {" +
										"		doc.extension = \"This is an extension\";"+
										"	}"+
										"	return doc;" +
										"}";

	protected final String transformationViewId = "transformationView";

	protected TransformationFunctionDescription transformationFunctionDescription;


	@Override
	protected void initApi(RestAdapter restAdapter) {
		transformationApi = restAdapter.create(DataTransformationApi.class);
		transformationFunctionDescription = new TransformationFunctionDescription(EXTENSION_FUNCTION, null);
	}

	@Test
	public void testCrd(){
		// create
		transformationApi.addViewSynchronously(sourceId, transformationViewId, transformationFunctionDescription);

		//read
		List<TransformationFunction> allViewsSynchronously = transformationApi.getAllViewsSynchronously(sourceId);

		Assert.assertEquals(1,allViewsSynchronously.size());


		//delete
		transformationApi.deleteViewSynchronously(sourceId,transformationViewId);

		List<TransformationFunction> deleted = transformationApi.getAllViewsSynchronously(sourceId);

		Assert.assertEquals(0,deleted.size());
	}

	@Test(expected = RetrofitError.class)
	public void testUpdate(){
		// create
		transformationApi.addViewSynchronously(sourceId, transformationViewId, transformationFunctionDescription);
		transformationApi.addViewSynchronously(sourceId, transformationViewId, transformationFunctionDescription);
	}
}
