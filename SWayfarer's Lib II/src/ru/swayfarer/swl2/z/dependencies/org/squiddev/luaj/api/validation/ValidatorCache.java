package ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.api.validation;

import java.util.HashMap;
import java.util.Map;

import ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.api.builder.BuilderException;

/**
 * Cache of validator instances
 */
public final class ValidatorCache {
	/**
	 * Cache of validator instances
	 */
	private static final Map<Class<? extends ILuaValidator>, ILuaValidator> VALIDATORS = new HashMap<>();

	public static ILuaValidator getValidator(Class<? extends ILuaValidator> validator) {
		ILuaValidator val = VALIDATORS.get(validator);

		if (val == null) {
			try {
				val = validator.newInstance();
			} catch (ReflectiveOperationException e) {
				throw new BuilderException("Cannot create new instance of " + validator.getName(), e);
			}

			VALIDATORS.put(validator, val);
		}

		return val;
	}
}
