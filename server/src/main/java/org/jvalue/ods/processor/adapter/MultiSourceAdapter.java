package org.jvalue.ods.processor.adapter;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.jvalue.ods.api.processors.ProcessorReference;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.processor.specification.Argument;
import org.jvalue.ods.processor.specification.CreationMethod;

import java.io.IOException;
import java.lang.annotation.Annotation;
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


	@SuppressWarnings("Duplicates")
	private void createAdapterInstances() {
		for (HashMap hashMap : sourceObjects) {
			Set outerKeySet = hashMap.keySet();
			if (outerKeySet.size() != 2 || !outerKeySet.contains("source") || !outerKeySet.contains("alias"))
				throw new SourceAdapterException("Only fields 'source' and 'alias' need to be defined.");

			String alias;
			HashMap<String, Object> sourceMap;

			try {
				alias = (String) hashMap.get("alias");
			}catch (ClassCastException e){
				throw new SourceAdapterException("Field 'alias' needs to be a String.");
			}

			try {
				sourceMap = (HashMap<String, Object>) hashMap.get("source");
			}catch (ClassCastException e){
				throw new SourceAdapterException("Field 'source' needs to be a json object.");
			}

			if (!sourceMap.keySet().contains("name"))
				throw new SourceAdapterException("Field 'source.name' does not exist.");

			String name = (String) sourceMap.get("name");
			for (Method method : SourceAdapterFactory.class.getDeclaredMethods()) {
				CreationMethod creationAnnotation = method.getAnnotation(CreationMethod.class);
				if (creationAnnotation == null) throw new IllegalArgumentException("creation annotation not found");
				if (!name.equals(creationAnnotation.name())) continue;

				List<Object> arguments = new LinkedList<>();

				// add source argument
				for (Class<?> parameterType : method.getParameterTypes()) {
					if (parameterType.equals(DataSource.class)) arguments.add(dataSource);
				}

				sourceMap.remove("name");
				ProcessorReference reference = new ProcessorReference(name, sourceMap);

				// add custom arguments
				Annotation[][] allParamAnnotations = method.getParameterAnnotations();
				for (Annotation[] paramAnnotations : allParamAnnotations) {
					for (Annotation  annotation : paramAnnotations) {
						if (annotation instanceof Argument) {
							Argument arg = (Argument) annotation;
							Object o = reference.getArguments().get(arg.value());
							if(o == null){
								throw new SourceAdapterException("Required field " + arg.value() + " does not exist.");
							}
							arguments.add(o);
						}
					}
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
				}else if (result.size() > 1){
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
