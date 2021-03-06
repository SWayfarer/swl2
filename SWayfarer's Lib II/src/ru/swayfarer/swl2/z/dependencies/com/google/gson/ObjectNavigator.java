/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.swayfarer.swl2.z.dependencies.com.google.gson;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;

import lombok.AllArgsConstructor;
import ru.swayfarer.swl2.json.JsonStage;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.observable.SimpleObservable;
import ru.swayfarer.swl2.observable.events.AbstractCancelableEvent;

/**
 * Provides ability to apply a visitor to an object and all of its fields
 * recursively.
 *
 * @author Inderjeet Singh
 * @author Joel Leitch
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ObjectNavigator {

	public IObservable<JsonEvent> eventSerialization = new SimpleObservable<>();
	public IObservable<JsonFieldEvent> eventFieldSerialization = new SimpleObservable<>();
	
	@AllArgsConstructor(staticName = "of")
	public static class JsonEvent extends AbstractCancelableEvent {
		public JsonStage stage;
		public Object obj;
		public Class<?> cl;
	}
	
	@AllArgsConstructor(staticName = "of")
	public static class JsonFieldEvent extends AbstractCancelableEvent {
		
		public JsonStage stage;
		public Field field;
		public Class<?> cl;
		public Class<?> fieldClass;
		public Object obj;
		
	}
	
	
	
	public interface Visitor {
		/**
		 * This is called before the object navigator starts visiting the
		 * current object
		 */
		void startVisitingObject(Object node);

		/**
		 * This is called after the object navigator finishes visiting the
		 * current object
		 */
		void endVisitingObject(Object node);

		/**
		 * This is called to visit the current object if it is an iterable
		 *
		 * @param componentType
		 *            the type of each element of the component
		 */
		void visitCollection(Collection collection, Type componentType);

		/**
		 * This is called to visit the current object if it is an array
		 */
		void visitArray(Object array, Type componentType);

		/**
		 * This is called to visit the current object if it is a primitive
		 */
		void visitPrimitiveValue(Object obj);

		/**
		 * This is called to visit an object field of the current object
		 */
		void visitObjectField(Field f, Object obj);

		/**
		 * This is called to visit a field of type Collection of the current
		 * object
		 */
		void visitCollectionField(Field f, Object obj);

		/**
		 * This is called to visit an array field of the current object
		 */
		void visitArrayField(Field f, Object obj);

		/**
		 * This is called to visit a primitive field of the current object
		 */
		void visitPrimitiveField(Field f, Object obj);

		/**
		 * This is called to visit an enum object
		 */
		public void visitEnum(Object obj, Type objType);

		/**
		 * This is called to visit an object using a custom handler
		 * 
		 * @return true if a custom handler exists, false otherwise
		 */
		public boolean visitUsingCustomHandler(Object obj, Type objType);
	}

	private final ExclusionStrategy exclusionStrategy;
	private final MemoryRefStack<Object> ancestors;
	private final Object obj;
	private final Type objType;

	/**
	 * @param obj
	 *            The object being navigated
	 * @param objType
	 *            The (fully genericized) type of the object being navigated
	 * @param exclusionStrategy
	 *            the concrete strategy object to be used to filter out fields
	 *            of an object.
	 */
	ObjectNavigator(Object obj, Type objType, ExclusionStrategy exclusionStrategy, MemoryRefStack<Object> ancestors)
	{
		Preconditions.checkNotNull(exclusionStrategy);
		Preconditions.checkNotNull(ancestors);

		this.obj = obj;
		this.objType = objType;
		this.exclusionStrategy = exclusionStrategy;
		this.ancestors = ancestors;
	}

	/**
	 * Navigate all the fields of the specified object. If a field is null, it
	 * does not get visited.
	 */
	public void accept(Visitor visitor)
	{
		if (obj == null)
		{
			return;
		}
		
		TypeInfo<?> objTypeInfo = new TypeInfo<Object>(objType);
		
		if (exclusionStrategy.shouldSkipClass(objTypeInfo.getTopLevelClass()))
		{
			return;
		}

		if (ancestors.contains(obj))
		{
			throw new IllegalStateException("Circular reference found: " + obj);
		}
		
		ancestors.push(obj);

		try
		{
			if (isCollectionOrArray(objTypeInfo))
			{
				if (objTypeInfo.isArray())
				{
					visitor.visitArray(obj, objType);
				}
				else
				{ // must be a collection
					visitor.visitCollection((Collection<?>) obj, objType);
				}
			}
			else if (objTypeInfo.getTopLevelClass().isEnum())
			{
				visitor.visitEnum(obj, objType);
			}
			else if (objTypeInfo.isPrimitiveOrStringAndNotAnArray())
			{
				visitor.visitPrimitiveValue(obj);
			}
			else
			{
				if (!visitor.visitUsingCustomHandler(obj, objType))
				{
					visitor.startVisitingObject(obj);
					// For all classes in the inheritance hierarchy (including
					// the current class),
					// visit all fields
					for (Class<?> curr = objTypeInfo.getTopLevelClass(); curr != null && !curr.equals(Object.class); curr = curr.getSuperclass())
					{
						if (!curr.isSynthetic())
						{
							navigateClassFields(obj, curr, visitor);
						}
					}
					visitor.endVisitingObject(obj);
				}
			}
		}
		finally
		{
			ancestors.pop();
		}
	}

	private void navigateClassFields(Object obj, Class<?> clazz, Visitor visitor)
	{
		JsonEvent jsonEvent = null;
		JsonFieldEvent jsonFieldEvent = null;
		JsonStage currentStage = null;
		
		if (!eventSerialization.isEmpty())
		{
			currentStage = getStage(true, visitor);
			jsonEvent = JsonEvent.of(currentStage, obj, clazz);
			eventSerialization.next(jsonEvent);
			
			if (jsonEvent.isCanceled())
				return;
			
			clazz = jsonEvent.cl;
		}
		
		
		Field[] fields = clazz.getDeclaredFields();
		AccessibleObject.setAccessible(fields, true);
		
		for (Field f : fields)
		{
			if (!eventFieldSerialization.isEmpty())
			{
				currentStage = getStage(true, visitor);
				jsonFieldEvent = JsonFieldEvent.of(currentStage, f, clazz, f.getType(), obj);
				eventFieldSerialization.next(jsonFieldEvent);
				
				if (jsonFieldEvent.isCanceled())
					continue;
			}
			
			TypeInfo<?> fieldTypeInfo = new TypeInfo<Object>(jsonFieldEvent == null ? f.getType() : jsonFieldEvent.fieldClass);
			
			if (exclusionStrategy.shouldSkipField(f))
			{
				continue; // skip
			}
			else if (isCollectionOrArray(fieldTypeInfo))
			{
				if (fieldTypeInfo.isArray())
				{
					visitor.visitArrayField(f, obj);
				}
				else
				{ // must be Collection
					visitor.visitCollectionField(f, obj);
				}
			}
			else if (fieldTypeInfo.isPrimitiveOrStringAndNotAnArray())
			{
				visitor.visitPrimitiveField(f, obj);
			}
			else
			{
				visitor.visitObjectField(f, obj);
			}
			
			if (!eventFieldSerialization.isEmpty())
			{
				currentStage = getStage(false, visitor);
				jsonFieldEvent = JsonFieldEvent.of(currentStage, f, clazz, jsonFieldEvent.fieldClass, obj);
				eventFieldSerialization.next(jsonFieldEvent);
			}
		}
		
		if (!eventSerialization.isEmpty())
		{
			currentStage = getStage(false, visitor);
			jsonEvent = JsonEvent.of(currentStage, obj, clazz);
			eventSerialization.next(jsonEvent);
		}
	}
	
	public static JsonStage getStage(boolean isStart, Visitor visitor)
	{
		return visitor instanceof JsonDeserializationVisitor ? isStart ? JsonStage.Load_Started : JsonStage.Load_Completed : isStart ? JsonStage.Save_Started : JsonStage.Save_Completed;
	}

	private static boolean isCollectionOrArray(TypeInfo<?> typeInfo)
	{
		return Collection.class.isAssignableFrom(typeInfo.getTopLevelClass()) || typeInfo.isArray();
	}

	private static final Class[] PRIMITIVE_TYPES =
	{ int.class, long.class, short.class, float.class, double.class, byte.class, boolean.class, Integer.class, Long.class, Short.class, Float.class, Double.class, Byte.class, Boolean.class };

	static boolean isPrimitiveOrString(Object target)
	{
		if (target instanceof String)
		{
			return true;
		}
		Class<?> classOfPrimitive = target.getClass();
		for (Class standardPrimitive : PRIMITIVE_TYPES)
		{
			if (standardPrimitive.isAssignableFrom(classOfPrimitive))
			{
				return true;
			}
		}
		return false;
	}
}
