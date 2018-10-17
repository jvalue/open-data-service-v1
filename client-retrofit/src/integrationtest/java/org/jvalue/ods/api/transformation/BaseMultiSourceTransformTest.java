package org.jvalue.ods.api.transformation;

import org.jvalue.ods.api.AbstractApiTest;
import org.jvalue.ods.api.DataTransformationApi;
import org.jvalue.ods.api.ProcessorChainApi;
import org.jvalue.ods.api.views.generic.TransformationFunctionDescription;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

abstract class BaseMultiSourceTransformTest extends AbstractApiTest {

	static String transformationView;
	static String transformFunctionBeforeStore;

	ProcessorChainApi processorChainApi;
	DataTransformationApi transformationApi;

	final String MULTI_FILTER = "multiFilter";
	final String transformationViewId = "transformationViewTest";
	TransformationFunctionDescription queryTransformation;


	static String resourceFileToString(String fileName) throws URISyntaxException, IOException {
		Path path = Paths.get(MultiSourceAndTransformTest.class.getClassLoader()
			.getResource("transformation/" + fileName).toURI());

		return new String(Files.readAllBytes(path));
	}


	ArrayList getViewResult(DataTransformationApi transformationApi, String transformationViewId) throws TimeoutException, InterruptedException {
		ArrayList viewSynchronously = new ArrayList();
		int tries = 0;
		while (viewSynchronously.isEmpty()) {
			if (tries == 4) {
				throw new TimeoutException("Timed out waiting on the processor chain execution.");
			}
			Thread.sleep(3000);
			viewSynchronously = (ArrayList) transformationApi.getViewSynchronously(sourceId, transformationViewId, true, null);
			tries++;
		}
		return viewSynchronously;
	}


}
