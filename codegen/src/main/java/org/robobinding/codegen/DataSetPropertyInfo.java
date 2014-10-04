package org.robobinding.codegen;

import java.text.MessageFormat;
import java.util.List;

import javax.lang.model.util.Types;

import org.apache.commons.lang3.StringUtils;
import org.robobinding.annotation.ItemPresentationModel;
import org.robobinding.property.AbstractDataSet;
import org.robobinding.property.ListDataSet;
import org.robobinding.property.TypedCursorDataSet;


/**
 * @since 1.0
 * @author Cheng Wei
 *
 */
public class DataSetPropertyInfo {
	private final Types types;
	private final MethodElementWrapper getter;
	private final ItemPresentationModel itemPresentationModelAnnotation;
	private final String itemPresentationModelObjectTypeName;
	
	public DataSetPropertyInfo(PropertyDescriptor descriptor, ItemPresentationModel itemPresentationModelAnnotation,
			String itemPresentationModelObjectTypeName) {
		this.descriptor = descriptor;
		this.itemPresentationModelAnnotation = itemPresentationModelAnnotation;
		this.itemPresentationModelObjectTypeName = itemPresentationModelObjectTypeName;
	}

	public String name() {
		return GetterUtils.propertyNameFromGetter(getter);
	}
	
	public String type() {
		return getter.returnTypeName();
	}

	public String getter() {
		return getter.methodName();
	}

	public Class<? extends AbstractDataSet> dataSetImplementationType() {
		if(getter.isReturnTypeSubTypeOf(List.class)) {
			return ListDataSet.class;
		} else if(getter.isReturnTypeSubTypeOf(type())) {
			return TypedCursorDataSet.class;
		} else {
			throw new RuntimeException(MessageFormat.format("Property {0} has an unsupported dataSet type", getter.toString()));
		}
	}

	public Class<?> itemPresentationModelType() {
		return itemPresentationModelAnnotation.value();
	}

	public boolean isCreatedByFactoryMethod() {
		return !StringUtils.isBlank(itemPresentationModelAnnotation.factoryMethod());
	}

	public String factoryMethod() {
		return StringUtils.trim(itemPresentationModelAnnotation.factoryMethod());
	}

	public String itemPresentationModelObjectTypeName() {
		return itemPresentationModelObjectTypeName;
	}
}
