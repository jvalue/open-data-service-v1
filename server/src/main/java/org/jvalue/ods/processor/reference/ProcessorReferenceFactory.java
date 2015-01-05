package org.jvalue.ods.processor.reference;


import com.google.inject.Inject;

import org.apache.commons.lang3.ClassUtils;
import org.jvalue.ods.api.processors.ProcessorReference;
import org.jvalue.ods.api.processors.ProcessorReferenceChain;
import org.jvalue.ods.processor.specification.ProcessorType;
import org.jvalue.ods.processor.specification.Specification;
import org.jvalue.ods.processor.specification.SpecificationManager;
import org.jvalue.ods.utils.Assert;

import java.util.List;
import java.util.Map;


public final class ProcessorReferenceFactory {

	private final SpecificationManager descriptionManager;

	@Inject
	public ProcessorReferenceFactory(SpecificationManager descriptionManager) {
		this.descriptionManager = descriptionManager;
	}


	public ProcessorReference assertIsValidProcessorReference(ProcessorReference reference) throws InvalidProcessorException {
		Specification description = descriptionManager.getByName(reference.getName());
		if (description == null) throw new InvalidProcessorException("no processor found for id \"" + reference.getName() + "\"");
		if (reference.getArguments().size() != description.getArgumentTypes().size()) throw new InvalidProcessorException("expected " + description.getArgumentTypes().size() + " arguments but got " + reference.getArguments().size());

		for (Map.Entry<String, Class<?>> entry : description.getArgumentTypes().entrySet()) {
			Object arg = reference.getArguments().get(entry.getKey());
			if (arg == null) throw new InvalidProcessorException("no argument named \"" + entry.getKey() + "\" found");
			if (!ClassUtils.isAssignable(entry.getValue(), arg.getClass(), true)) throw new InvalidProcessorException("invalid argument type for \"" + entry.getKey() + "\"");
		}
		return reference;
	}


	public ProcessorReferenceChain assertIsValidReferenceChain(
			ProcessorReferenceChain referenceChain)
			throws InvalidProcessorException {

		Assert.assertNotNull(referenceChain);
		assertIsValidProcessorReferenceList(referenceChain.getProcessors());
		return referenceChain;
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
