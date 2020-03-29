package ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.api.builder;

import ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.api.LuaObject;
import ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.api.conversion.Converter;
import ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.api.transformer.DefaultTransformers;
import ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.api.transformer.Transformer;
import ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.api.validation.DefaultLuaValidator;
import ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.api.validation.ILuaValidator;

/**
 * Settings for the builder
 */
public class BuilderSettings {
	/**
	 * A string to suffix every class with
	 */
	public String suffix = "_GenAPI";

	/**
	 * The class every generated API will inherit from
	 */
	public Class<? extends LuaObject> parentClass;

	/**
	 * Should verify sources
	 */
	public boolean verify = false;

	/**
	 * Variable converter
	 */
	public Converter converter = Converter.getDefault();

	/**
	 * Class/method transformers
	 */
	public Transformer transformer = new DefaultTransformers();

	/**
	 * Default validator
	 */
	public Class<? extends ILuaValidator> validator = DefaultLuaValidator.class;
}
