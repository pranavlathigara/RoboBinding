package org.robobinding.codegen;

import java.util.Set;

import org.robobinding.annotation.DependsOnStateOf;

/**
 * @since 1.0
 * @author Cheng Wei
 *
 */
public class ItemPresentationModelInfoBuilder extends AbstractPresentationModelInfoBuilder {
	public ItemPresentationModelInfoBuilder(Class<?> presentationModelClass, String presentationModelObjectTypeName) {
		super(presentationModelClass, presentationModelObjectTypeName);
	}
	
	@Override
	public void buildProperties() {
		Set<PropertyDescriptor> descriptors = PropertyDescriptorUtils.getPropertyDescriptors(presentationModelClass);
		for(PropertyDescriptor descriptor : descriptors) {
			properties.add(new PropertyInfo(descriptor));
			
			DependsOnStateOf dependsOnStateOfAnnotation = descriptor.getAnnotation(DependsOnStateOf.class);
			Set<String> dependentProperties = dependentProperties(dependsOnStateOfAnnotation);
			if(!dependentProperties.isEmpty()) {
				validation.validate(descriptor, dependentProperties);
				PropertyDependencyInfo dependencyInfo = new PropertyDependencyInfo(descriptor.getName(), dependentProperties);
				propertyDependencies.add(dependencyInfo);
			}
		}
	}

}
