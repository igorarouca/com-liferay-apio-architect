/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.apio.architect.representor;

import static com.liferay.apio.architect.date.DateTransformer.asString;

import com.liferay.apio.architect.alias.BinaryFunction;
import com.liferay.apio.architect.file.BinaryFile;
import com.liferay.apio.architect.identifier.Identifier;
import com.liferay.apio.architect.language.Language;
import com.liferay.apio.architect.related.RelatedModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Holds information about the metadata supported for a nested resource.
 *
 * <p>
 * Instances of this interface should always be created by using a {@link
 * NestedRepresentor.Builder}.
 * </p>
 *
 * @author Alejandro Hernández
 * @param  <T> the model's type
 * @see    NestedRepresentor.Builder
 */
public class NestedRepresentor<T> extends BaseRepresentor<T> {

	@Override
	public boolean isNested() {
		return true;
	}

	/**
	 * Creates generic representations of your domain models that Apio
	 * hypermedia writers can understand.
	 */
	public static class Builder<T> {

		/**
		 * Adds a type for the model.
		 *
		 * @param  type the type name
		 * @param  types the rest of the types
		 * @return the builder's step
		 */
		public FirstStep<T> types(String type, String... types) {
			NestedRepresentor<T> representor = _withTypes(type, types);

			return new FirstStep<>(representor);
		}

	}

	public static class FirstStep<T> {

		/**
		 * Adds binary files to a resource.
		 *
		 * @param  key the binary resource's name
		 * @param  binaryFunction the function used to get the binaries
		 * @return the builder's step
		 */
		public FirstStep<T> addBinary(
			String key, BinaryFunction<T> binaryFunction) {

			FieldFunction<T, BinaryFile> fieldFunction = new FieldFunction<>(
				key, binaryFunction);

			NestedRepresentor<T> representor = _withBinaryFunction(
				_nestedRepresentor, fieldFunction);

			return new FirstStep<>(representor);
		}

		/**
		 * Adds information about a resource's boolean field.
		 *
		 * @param  key the field's name
		 * @param  function the function used to get the boolean value
		 * @return the builder's step
		 */
		public FirstStep<T> addBoolean(
			String key, Function<T, Boolean> function) {

			FieldFunction<T, Boolean> fieldFunction = new FieldFunction<>(
				key, function);

			NestedRepresentor<T> representor = _withBooleanFunction(
				_nestedRepresentor, fieldFunction);

			return new FirstStep<>(representor);
		}

		/**
		 * Adds information about a resource's boolean list field.
		 *
		 * @param  key the field's name
		 * @param  function the function used to get the boolean list
		 * @return the builder's step
		 */
		public FirstStep<T> addBooleanList(
			String key, Function<T, List<Boolean>> function) {

			FieldFunction<T, List<Boolean>> fieldFunction = new FieldFunction<>(
				key, function);

			NestedRepresentor<T> representor = _withBooleanListFunction(
				_nestedRepresentor, fieldFunction);

			return new FirstStep<>(representor);
		}

		/**
		 * Adds information about a resource's date field.
		 *
		 * @param  key the field's name
		 * @param  function the function used to get the date value
		 * @return the builder's step
		 */
		public FirstStep<T> addDate(String key, Function<T, Date> function) {
			Function<Date, String> formatFunction = date -> {
				if (date == null) {
					return null;
				}

				return asString(date);
			};

			FieldFunction<T, String> fieldFunction = new FieldFunction<>(
				key, function.andThen(formatFunction));

			NestedRepresentor<T> representor = _withStringFunction(
				_nestedRepresentor, fieldFunction);

			return new FirstStep<>(representor);
		}

		/**
		 * Adds information about a resource link.
		 *
		 * @param  key the field's name
		 * @param  url the link's URL
		 * @return the builder's step
		 */
		public FirstStep<T> addLink(String key, String url) {
			FieldFunction<T, String> fieldFunction = new FieldFunction<>(
				key, __ -> url);

			NestedRepresentor<T> representor = _withLinkFunction(
				_nestedRepresentor, fieldFunction);

			return new FirstStep<>(representor);
		}

		/**
		 * Adds information about an embeddable related resource.
		 *
		 * @param  key the relation's name
		 * @param  identifierClass the related resource identifier's class
		 * @param  identifierFunction the function used to get the related
		 *         resource's identifier
		 * @return the builder's step
		 */
		public <S> FirstStep<T> addLinkedModel(
			String key, Class<? extends Identifier<S>> identifierClass,
			Function<T, S> identifierFunction) {

			RelatedModel<T, S> relatedModel = new RelatedModel<>(
				key, identifierClass, identifierFunction);

			NestedRepresentor<T> representor = _withRelatedModel(
				_nestedRepresentor, relatedModel);

			return new FirstStep<>(representor);
		}

		/**
		 * Provides information about a resource localized string field.
		 *
		 * @param  key the field's name
		 * @param  stringFunction the function used to get the string value
		 * @return builder's step
		 */
		public FirstStep<T> addLocalizedStringByLanguage(
			String key, BiFunction<T, Language, String> stringFunction) {

			FieldFunction<T, Function<Language, String>> fieldFunction =
				new FieldFunction<>(
					key, t -> language -> stringFunction.apply(t, language));

			NestedRepresentor<T> representor = _withLanguageFunction(
				_nestedRepresentor, fieldFunction);

			return new FirstStep<>(representor);
		}

		/**
		 * Provides information about a resource localized string field.
		 *
		 * @param  key the field's name
		 * @param  stringFunction the function used to get the string value
		 * @return builder's step
		 */
		public FirstStep<T> addLocalizedStringByLocale(
			String key, BiFunction<T, Locale, String> stringFunction) {

			return addLocalizedStringByLanguage(
				key,
				(t, language) -> stringFunction.apply(
					t, language.getPreferredLocale()));
		}

		/**
		 * Provides information about a nested field.
		 *
		 * @param  key the field's name
		 * @param  transformFunction the function that transforms the model into
		 *         the model used inside the nested nestedRepresentor
		 * @param  representorFunction the function that creates the nested
		 *         nestedRepresentor
		 * @return the builder's step
		 */
		public <W> FirstStep<T> addNested(
			String key, Function<T, W> transformFunction,
			Function<Builder<W>, NestedRepresentor<W>> representorFunction) {

			NestedFieldFunction<T, W> nestedFieldFunction =
				new NestedFieldFunction<>(
					key, transformFunction,
					representorFunction.apply(new Builder<>()));

			NestedRepresentor<T> representor = _withNestedFieldFunction(
				_nestedRepresentor, nestedFieldFunction);

			return new FirstStep<>(representor);
		}

		/**
		 * Adds information about a resource's number field.
		 *
		 * @param  key the field's name
		 * @param  function the function used to get the number's value
		 * @return the builder's step
		 */
		public FirstStep<T> addNumber(
			String key, Function<T, Number> function) {

			FieldFunction<T, Number> fieldFunction = new FieldFunction<>(
				key, function);

			NestedRepresentor<T> representor = _withNumberFunction(
				_nestedRepresentor, fieldFunction);

			return new FirstStep<>(representor);
		}

		/**
		 * Adds information about a resource's number list field.
		 *
		 * @param  key the field's name
		 * @param  function the function used to get the number list
		 * @return the builder's step
		 */
		public FirstStep<T> addNumberList(
			String key, Function<T, List<Number>> function) {

			FieldFunction<T, List<Number>> fieldFunction = new FieldFunction<>(
				key, function);

			NestedRepresentor<T> representor = _withNumberListFunction(
				_nestedRepresentor, fieldFunction);

			return new FirstStep<>(representor);
		}

		/**
		 * Adds information about a resource's string field.
		 *
		 * @param  key the field's name
		 * @param  function the function used to get the string's value
		 * @return the builder's step
		 */
		public FirstStep<T> addString(
			String key, Function<T, String> function) {

			FieldFunction<T, String> fieldFunction = new FieldFunction<>(
				key, function);

			NestedRepresentor<T> representor = _withStringFunction(
				_nestedRepresentor, fieldFunction);

			return new FirstStep<>(representor);
		}

		/**
		 * Adds information about a resource's string list field.
		 *
		 * @param  key the field's name
		 * @param  function the function used to get the string list
		 * @return the builder's step
		 */
		public FirstStep<T> addStringList(
			String key, Function<T, List<String>> function) {

			FieldFunction<T, List<String>> fieldFunction = new FieldFunction<>(
				key, function);

			NestedRepresentor<T> representor = _withStringListFunction(
				_nestedRepresentor, fieldFunction);

			return new FirstStep<>(representor);
		}

		/**
		 * Constructs and returns a {@link NestedRepresentor} instance _with the
		 * information provided to the builder.
		 *
		 * @return the {@code Representor} instance
		 */
		public NestedRepresentor<T> build() {
			return _nestedRepresentor;
		}

		private FirstStep(NestedRepresentor<T> nestedRepresentor) {
			_nestedRepresentor = nestedRepresentor;
		}

		private final NestedRepresentor<T> _nestedRepresentor;

	}

	private static <T> NestedRepresentor<T> _withBinaryFunction(
		NestedRepresentor<T> representor,
		FieldFunction<T, BinaryFile> fieldFunction) {

		representor.binaryFunctions.put(
			fieldFunction.key, (BinaryFunction<T>)fieldFunction.function);

		return _withFieldFunction(representor, fieldFunction, "BINARY");
	}

	private static <T> NestedRepresentor<T> _withBooleanFunction(
		NestedRepresentor<T> representor,
		FieldFunction<T, Boolean> fieldFunction) {

		return _withFieldFunction(representor, fieldFunction, "BOOLEAN");
	}

	private static <T> NestedRepresentor<T> _withBooleanListFunction(
		NestedRepresentor<T> representor,
		FieldFunction<T, List<Boolean>> fieldFunction) {

		return _withFieldFunction(representor, fieldFunction, "BOOLEAN_LIST");
	}

	private static <T, S> NestedRepresentor<T> _withFieldFunction(
		NestedRepresentor<T> representor, FieldFunction<T, S> fieldFunction,
		String key) {

		NestedRepresentor<T> newRepresentor = new NestedRepresentor<>(
			representor);

		List<FieldFunction<T, ?>> list =
			newRepresentor.fieldFunctions.computeIfAbsent(
				key, __ -> new ArrayList<>());

		list.add(fieldFunction);

		return newRepresentor;
	}

	private static <T> NestedRepresentor<T> _withLanguageFunction(
		NestedRepresentor<T> representor,
		FieldFunction<T, Function<Language, String>> fieldFunction) {

		return _withFieldFunction(representor, fieldFunction, "LOCALIZED");
	}

	private static <T> NestedRepresentor<T> _withLinkFunction(
		NestedRepresentor<T> representor,
		FieldFunction<T, String> fieldFunction) {

		return _withFieldFunction(representor, fieldFunction, "LINK");
	}

	private static <T> NestedRepresentor<T> _withNestedFieldFunction(
		NestedRepresentor<T> representor,
		NestedFieldFunction<T, ?> nestedFieldFunction) {

		NestedRepresentor<T> newRepresentor = new NestedRepresentor<>(
			representor);

		newRepresentor.nestedFieldFunctions.add(nestedFieldFunction);

		return newRepresentor;
	}

	private static <T> NestedRepresentor<T> _withNumberFunction(
		NestedRepresentor<T> representor,
		FieldFunction<T, Number> fieldFunction) {

		return _withFieldFunction(representor, fieldFunction, "NUMBER");
	}

	private static <T> NestedRepresentor<T> _withNumberListFunction(
		NestedRepresentor<T> representor,
		FieldFunction<T, List<Number>> fieldFunction) {

		return _withFieldFunction(representor, fieldFunction, "NUMBER_LIST");
	}

	private static <T> NestedRepresentor<T> _withRelatedModel(
		NestedRepresentor<T> representor, RelatedModel<T, ?> relatedModel) {

		NestedRepresentor<T> newRepresentor = new NestedRepresentor<>(
			representor);

		newRepresentor.relatedModels.add(relatedModel);

		return newRepresentor;
	}

	private static <T> NestedRepresentor<T> _withStringFunction(
		NestedRepresentor<T> representor,
		FieldFunction<T, String> fieldFunction) {

		return _withFieldFunction(representor, fieldFunction, "STRING");
	}

	private static <T> NestedRepresentor<T> _withStringListFunction(
		NestedRepresentor<T> representor,
		FieldFunction<T, List<String>> fieldFunction) {

		return _withFieldFunction(representor, fieldFunction, "STRING_LIST");
	}

	private static <T> NestedRepresentor<T> _withTypes(
		String type, String... types) {

		NestedRepresentor<T> newRepresentor = new NestedRepresentor<>();

		newRepresentor.types.add(type);
		Collections.addAll(newRepresentor.types, types);

		return newRepresentor;
	}

	private NestedRepresentor() {
	}

	private NestedRepresentor(NestedRepresentor<T> representor) {
		super(representor);
	}

}