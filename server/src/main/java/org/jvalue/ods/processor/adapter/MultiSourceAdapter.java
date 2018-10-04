package org.jvalue.ods.processor.adapter;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.processor.specification.CreationMethod;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

public class MultiSourceAdapter extends AbstractSourceAdapter {

	private final ArrayList<SourceAdapter> sourceAdapters;
	private final SourceAdapterFactory sourceAdapterFactory;
	private final DataSource dataSource;
	private final ArrayList<LinkedHashMap> sourceObjects;

	@Inject
	protected MultiSourceAdapter(SourceAdapterFactory sourceAdapterFactory,
								 @Assisted DataSource dataSource,
								 @Assisted ArrayList<LinkedHashMap> sourceObjects,
								 MetricRegistry registry) {
		//dummy url
		super(dataSource, "http://invalidurl.org", registry);

		this.sourceAdapters = new ArrayList<>();
		this.sourceAdapterFactory = sourceAdapterFactory;
		this.dataSource = dataSource;
		this.sourceObjects = sourceObjects;

		try {
			createAdapterInstances();
		} catch (Exception e) {
			throw new SourceAdapterException(e);
		}
	}


	private void createAdapterInstances() {
		for (HashMap hashMap : sourceObjects) {
			Set outerKeySet = hashMap.keySet();
			if (outerKeySet.size() != 2 || !outerKeySet.contains("source") || !outerKeySet.contains("alias"))
				throw new SourceAdapterException("fields 'source' and 'alias' need to be declared.");

			Object firstKey = hashMap.keySet().iterator().next();
			HashMap innerHashMap = (HashMap) hashMap.get(firstKey);
			Set innerKeySet = innerHashMap.keySet();

			if (innerKeySet.size() != 2 || !innerKeySet.contains("name") || !innerKeySet.contains("url"))
				throw new SourceAdapterException("fields 'name' and 'url' need to be declared.");

			HashMap sourceMap = (HashMap) hashMap.get("source");
			String name = (String) sourceMap.get("name");
			String url = (String) sourceMap.get("url");
			String alias = (String) hashMap.get("alias");

			for (Method method : SourceAdapterFactory.class.getDeclaredMethods()) {
				CreationMethod creationAnnotation = method.getAnnotation(CreationMethod.class);
				if (creationAnnotation == null) throw new IllegalArgumentException("creation annotation not found");
				if (!name.equals(creationAnnotation.name())) continue;

				List<Object> arguments = new LinkedList<>();

				// add source and url arguments
				for (Class<?> parameterType : method.getParameterTypes()) {
					if (parameterType.equals(DataSource.class)) arguments.add(dataSource);
					if (parameterType.equals(String.class)) arguments.add(url);
				}

				try {
					SourceAdapter adapter = (SourceAdapter) method.invoke(sourceAdapterFactory, arguments.toArray());
					adapter.setAlias(alias);
					sourceAdapters.add(adapter);
				} catch (IllegalAccessException | InvocationTargetException ie) {
					throw new IllegalStateException(ie);
				}
			}
		}
	}


	@Override
	protected SourceIterator doCreateIterator(DataSource source, URL sourceUrl, MetricRegistry registry) {
		return new MultiSourceIterator(source, sourceUrl, registry, sourceAdapters);
	}

	private static final class MultiSourceIterator extends SourceIterator {
		private final ArrayList<SourceAdapter> sourceAdapters;
		private final Iterator<SourceAdapter> iterator;
		private ObjectMapper mapper;


		public MultiSourceIterator(DataSource source, URL sourceUrl, MetricRegistry registry, ArrayList<SourceAdapter> sourceAdapters) {
			super(source, sourceUrl, registry);
			this.sourceAdapters = sourceAdapters;
			this.iterator = this.sourceAdapters.iterator();
			this.mapper = new ObjectMapper();
		}


		@Override
		protected JsonNode doNext() throws IOException {


			ObjectNode resultNode = new ObjectMapper().createObjectNode();
			while (iterator.hasNext()) {
				// traverse multiple adapters
				SourceAdapter adapter = iterator.next();
				//traverse documents in adapter
				//possible to get array data
				List<ObjectNode> result = new ArrayList<>();
				for(ObjectNode node : adapter){
					result.add(node);
				}

				if(result.size() == 1){
					//add object Node
					resultNode.set(adapter.getAlias(), result.get(0));
				}else{
					//add arrayNode
					ArrayNode arrayNode = mapper.valueToTree(result);
					resultNode.set(adapter.getAlias(), arrayNode);
				}
			}
			return resultNode;
		}


		@Override
		protected boolean doHasNext() throws IOException {
			return iterator.hasNext();
		}
	}
}
