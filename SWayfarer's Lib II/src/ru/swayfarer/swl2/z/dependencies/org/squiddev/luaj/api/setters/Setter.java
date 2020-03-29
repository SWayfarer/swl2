package ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.api.setters;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import java.util.WeakHashMap;

import ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.api.builder.BuilderException;
import ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.api.builder.IInjector;
import ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.api.builder.tree.LuaField;

/**
 * Set a particular instance field to a related value
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Setter {
	/**
	 * The rule used to set this value
	 *
	 * {@code this} will be an instance of {@link ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.api.builder.BuilderSettings#parentClass}
	 * It should leave one value on the stack when used to set the field
	 *
	 * @return The rule used
	 */
	Class<? extends IInjector<LuaField>> value();

	/**
	 * A cache of rule instances to use
	 */
	final class SetterCache {
		private static Map<Class<? extends IInjector<LuaField>>, IInjector<LuaField>> cache = new WeakHashMap<>();

		@SuppressWarnings("unchecked")
		public static <T extends IInjector<LuaField>> T getInstance(Class<T> setterClass) {
			IInjector<LuaField> setter = cache.get(setterClass);
			if (setter == null) {
				try {
					setter = setterClass.newInstance();
				} catch (ReflectiveOperationException e) {
					throw new BuilderException("Cannot create " + setterClass.getName(), e);
				}

				cache.put(setterClass, setter);
			}

			return (T) setter;
		}
	}
}
