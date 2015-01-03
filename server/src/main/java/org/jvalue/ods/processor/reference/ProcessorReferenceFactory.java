package org.jvalue.ods.processor.reference;


import com.google.inject.Inject;

import org.apache.commons.lang3.ClassUtils;
import org.jvalue.ods.processor.specification.Specification;
import org.jvalue.ods.processor.specification.SpecificationManager;
import org.jvalue.ods.processor.specification.ProcessorType;
import org.jvalue.ods.utils.Assert;

import java.util.List;
import java.util.Map;

public final class ProcessorReferenceFactory {

	private final SpecificationManager descriptionManager;

	@Inject
	public ProcessorReferenceFactory(SpecificationManager descriptionManager) {
		this.descriptionManager = descriptionManager;
	}


	public ProcessorReference createProcessorReference(String name, Map<String, Object> arguments) throws InvalidProcessorException {
		Assert.assertNotNull(name, arguments);

		Specification description = descriptionManager.getByName(name);
		if (description == null) throw new InvalidProcessorException("no processor found for id \"" + name + "\"");
		if (arguments.size() != description.getArgumentTypes().size()) throw new InvalidProcessorException("expected " + description.getArgumentTypes().size() + " arguments but got " + arguments.size());

		for (Map.Entry<String, Class<?>> entry : description.getArgumentTypes().entrySet()) {
			Object arg = arguments.get(entry.getKey());
			if (arg == null) throw new InvalidProcessorException("no argument named \"" + entry.getKey() + "\" found");
			if (!ClassUtils.isAssignable(entry.getValue(), arg.getClass(), true)) throw new InvalidProcessorException("invalid argument type for \"" + entry.getKey() + "\"");
		}

		return new ProcessorReference(name, arguments);
	}


	public ProcessorChainReference createProcessorChainReference(
			String processorChainId,
			List<ProcessorReference> processorReferences,
			ExecutionInterval executionInterval)
			throws InvalidProcessorException {

		Assert.assertNotNull(processorChainId, processorReferences);
		assertIsValidProcessorReferenceList(processorReferences);
		return new ProcessorChainReference(processorChainId, processorReferences, executionInterval);
	}


	private void assertIsValidProcessorReferenceList(List<ProcessorReference> references) throws InvalidProcessorException {
		if (references.isEmpty())
			throw new InvalidProcessorException("processor chain cannot be empty");
		if (!descriptionManager.getByName(references.get(0).getName()).getType().equals(ProcessorType.SOURCE_ADAPTER))
			throw new InvalidProcessorException("processor chain must start with " + ProcessorType.SOURCE_ADAPTER);

		ProcessorReference lastReference =  null;
		for (ProcessorReference reference : references) {
			if (lastReference != null) {
				ProcessorType lastType = descriptionManager.getByName(lastReference.getName()).getType();
				ProcessorType nextType = descriptionManager.getByName(reference.getName()).getType();
				if (!lastType.isValidNextFilter(nextType)) throw new InvalidProcessorException(lastType + " cannot be followed by " + nextType);
			}
			lastReference = reference;
		}
	}


	public static class InvalidProcessorException extends IllegalArgumentException {

		InvalidProcessorException(String msg) {
			super(msg);
		}

	}

}
