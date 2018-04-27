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
import com.liferay.apio.architect.representor.function.FieldFunction;
import com.liferay.apio.architect.representor.function.NestedFieldFunction;

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
	public static class Builder<T>
		extends BaseBuilder<T, NestedRepresentor<T>> {

		/**
		 * Adds a type for the model.
		 *
		 * @param  type the type name
		 * @param  types the rest of the types
		 * @return the builder's step
		 */
		public FirstStep types(String type, String... types) {
			baseRepresentor.addTypes(type, types);

			return new FirstStep();
		}

		public class FirstStep {

			/**
			 * Adds binary files to a resource.
			 *
			 * @param  key the binary resource's name
			 * @param  binaryFunction the function used to get the binaries
			 * @return the builder's step
			 */
			public FirstStep addBinary(
				String key, BinaryFunction<T> binaryFunction) {

				FieldFunction<T, BinaryFile> fieldFunction =
					new FieldFunction<>(key, binaryFunction);

				baseRepresentor.addBinaryFunction(fieldFunction);

				return this;
			}

			/**
			 * Adds information about a resource's boolean field.
			 *
			 * @param  key the field's name
			 * @param  function the function used to get the boolean value
			 * @return the builder's step
			 */
			public FirstStep addBoolean(
				String key, Function<T, Boolean> function) {

				FieldFunction<T, Boolean> fieldFunction = new FieldFunction<>(
					key, function);

				baseRepresentor.addBooleanFunction(fieldFunction);

				return this;
			}

			/**
			 * Adds information about a resource's boolean list field.
			 *
			 * @param  key the field's name
			 * @param  function the function used to get the boolean list
			 * @return the builder's step
			 */
			public FirstStep addBooleanList(
				String key, Function<T, List<Boolean>> function) {

				FieldFunction<T, List<Boolean>> fieldFunction =
					new FieldFunction<>(key, function);

				baseRepresentor.addBooleanListFunction(fieldFunction);

				return this;
			}

			/**
			 * Adds information about a resource's date field.
			 *
			 * @param  key the field's name
			 * @param  function the function used to get the date value
			 * @return the builder's step
			 */
			public FirstStep addDate(String key, Function<T, Date> function) {
				Function<Date, String> formatFunction = date -> {
					if (date == null) {
						return null;
					}

					return asString(date);
				};

				FieldFunction<T, String> fieldFunction = new FieldFunction<>(
					key, function.andThen(formatFunction));

				baseRepresentor.addStringFunction(fieldFunction);

				return this;
			}

			/**
			 * Adds information about a resource link.
			 *
			 * @param  key the field's name
			 * @param  url the link's URL
			 * @return the builder's step
			 */
			public FirstStep addLink(String key, String url) {
				FieldFunction<T, String> fieldFunction = new FieldFunction<>(
					key, __ -> url);

				baseRepresentor.addLinkFunction(fieldFunction);

				return this;
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
			public <S> FirstStep addLinkedModel(
				String key, Class<? extends Identifier<S>> identifierClass,
				Function<T, S> identifierFunction) {

				RelatedModel<T, S> relatedModel = new RelatedModel<>(
					key, identifierClass, identifierFunction);

				baseRepresentor.addRelatedModel(relatedModel);

				return this;
			}

			/**
			 * Provides information about a resource localized string field.
			 *
			 * @param  key the field's name
			 * @param  stringFunction the function used to get the string value
			 * @return builder's step
			 */
			public FirstStep addLocalizedStringByLanguage(
				String key, BiFunction<T, Language, String> stringFunction) {

				FieldFunction<T, Function<Language, String>> fieldFunction =
					new FieldFunction<>(
						key,
						t -> language -> stringFunction.apply(t, language));

				baseRepresentor.addLanguageFunction(fieldFunction);

				return this;
			}

			/**
			 * Provides information about a resource localized string field.
			 *
			 * @param  key the field's name
			 * @param  stringFunction the function used to get the string value
			 * @return builder's step
			 */
			public FirstStep addLocalizedStringByLocale(
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
			 * @param  transformFunction the function that transforms the model
			 *         into the model used inside the nested nestedRepresentor
			 * @param  representorFunction the function that creates the nested
			 *         nestedRepresentor
			 * @return the builder's step
			 */
			public <W> FirstStep addNested(
				String key, Function<T, W> transformFunction,
				Function<NestedRepresentor.Builder<W>, NestedRepresentor<W>>
					representorFunction) {

				NestedFieldFunction<T, W> nestedFieldFunction =
					new NestedFieldFunction<>(
						key, transformFunction,
						representorFunction.apply(
							new NestedRepresentor.Builder<>()));

				baseRepresentor.addNestedFieldFunction(nestedFieldFunction);

				return this;
			}

			/**
			 * Adds information about a resource's number field.
			 *
			 * @param  key the field's name
			 * @param  function the function used to get the number's value
			 * @return the builder's step
			 */
			public FirstStep addNumber(
				String key, Function<T, Number> function) {

				FieldFunction<T, Number> fieldFunction = new FieldFunction<>(
					key, function);

				baseRepresentor.addNumberFunction(fieldFunction);

				return this;
			}

			/**
			 * Adds information about a resource's number list field.
			 *
			 * @param  key the field's name
			 * @param  function the function used to get the number list
			 * @return the builder's step
			 */
			public FirstStep addNumberList(
				String key, Function<T, List<Number>> function) {

				FieldFunction<T, List<Number>> fieldFunction =
					new FieldFunction<>(key, function);

				baseRepresentor.addNumberListFunction(fieldFunction);

				return this;
			}

			/**
			 * Adds information about a resource's string field.
			 *
			 * @param  key the field's name
			 * @param  function the function used to get the string's value
			 * @return the builder's step
			 */
			public FirstStep addString(
				String key, Function<T, String> function) {

				FieldFunction<T, String> fieldFunction = new FieldFunction<>(
					key, function);

				baseRepresentor.addStringFunction(fieldFunction);

				return this;
			}

			/**
			 * Adds information about a resource's string list field.
			 *
			 * @param  key the field's name
			 * @param  function the function used to get the string list
			 * @return the builder's step
			 */
			public FirstStep addStringList(
				String key, Function<T, List<String>> function) {

				FieldFunction<T, List<String>> fieldFunction =
					new FieldFunction<>(key, function);

				baseRepresentor.addStringListFunction(fieldFunction);

				return this;
			}

			/**
			 * Constructs and returns a {@link NestedRepresentor} instance _with
			 * the information provided to the builder.
			 *
			 * @return the {@code Representor} instance
			 */
			public NestedRepresentor<T> build() {
				return baseRepresentor;
			}

			private FirstStep() {
			}

		}

		protected Builder() {
			super(new NestedRepresentor<>());
		}

	}

	private NestedRepresentor() {
	}

}