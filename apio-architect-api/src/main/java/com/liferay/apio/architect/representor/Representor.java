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
import com.liferay.apio.architect.related.RelatedCollection;
import com.liferay.apio.architect.related.RelatedModel;
import com.liferay.apio.architect.unsafe.Unsafe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Holds information about the metadata supported for a resource.
 *
 * <p>
 * Instances of this interface should always be created by using a {@link
 * Representor.Builder}.
 * </p>
 *
 * @author Alejandro Hernández
 * @param  <T> the model's type
 * @see    Representor.Builder
 */
public class Representor<T> {

	/**
	 * Returns the binary resources linked to a model.
	 *
	 * @return the binary resources linked to a model
	 */
	public Optional<BinaryFunction<T>> getBinaryFunction(String binaryId) {
		return Optional.ofNullable(_binaryFunctions.get(binaryId));
	}

	/**
	 * Returns the binary resources linked to a model.
	 *
	 * @return the binary resources linked to a model
	 */
	public List<FieldFunction<T, BinaryFile>> getBinaryFunctions() {
		return Optional.ofNullable(
			_fieldFunctions.get("BINARY")
		).<List<FieldFunction<T, BinaryFile>>>map(
			Unsafe::unsafeCast
		).orElseGet(
			Collections::emptyList
		);
	}

	/**
	 * Returns the map containing the boolean field names and the functions to
	 * get those fields.
	 *
	 * @return the map containing the boolean field names and functions
	 */
	public List<FieldFunction<T, Boolean>> getBooleanFunctions() {
		return Optional.ofNullable(
			_fieldFunctions.get("BOOLEAN")
		).<List<FieldFunction<T, Boolean>>>map(
			Unsafe::unsafeCast
		).orElseGet(
			Collections::emptyList
		);
	}

	/**
	 * Returns the map containing the boolean list field names and the functions
	 * to get those fields.
	 *
	 * @return the map containing the boolean list field names and functions
	 */
	public List<FieldFunction<T, List<Boolean>>> getBooleanListFunctions() {
		return Optional.ofNullable(
			_fieldFunctions.get("BOOLEAN_LIST")
		).<List<FieldFunction<T, List<Boolean>>>>map(
			Unsafe::unsafeCast
		).orElseGet(
			Collections::emptyList
		);
	}

	/**
	 * Returns the model's identifier.
	 *
	 * @param  model the model instance
	 * @return the model's identifier
	 */
	public Object getIdentifier(T model) {
		return _identifierFunction.apply(model);
	}

	/**
	 * Returns the links.
	 *
	 * @return the links
	 */
	public List<FieldFunction<T, String>> getLinkFunctions() {
		return Optional.ofNullable(
			_fieldFunctions.get("LINK")
		).<List<FieldFunction<T, String>>>map(
			Unsafe::unsafeCast
		).orElseGet(
			Collections::emptyList
		);
	}

	/**
	 * Returns a map containing the localized string field names and the
	 * functions to get those fields.
	 *
	 * @return the map containing the localized string field names and functions
	 */
	public List<FieldFunction<T, Function<Language, String>>>
		getLocalizedStringFunctions() {

		return Optional.ofNullable(
			_fieldFunctions.get("LOCALIZED")
		).<List<FieldFunction<T, Function<Language, String>>>>map(
			Unsafe::unsafeCast
		).orElseGet(
			Collections::emptyList
		);
	}

	/**
	 * Returns the list of nested field functions.
	 *
	 * @return the list of nested field functions.
	 */
	public List<NestedFieldFunction<T, ?>> getNestedFieldFunctions() {
		return _nestedFieldFunctions;
	}

	/**
	 * Returns the map containing the number field names and the functions to
	 * get those fields.
	 *
	 * @return the map containing the number field names and functions
	 */
	public List<FieldFunction<T, Number>> getNumberFunctions() {
		return Optional.ofNullable(
			_fieldFunctions.get("NUMBER")
		).<List<FieldFunction<T, Number>>>map(
			Unsafe::unsafeCast
		).orElseGet(
			Collections::emptyList
		);
	}

	/**
	 * Returns the map containing the number list field names and the functions
	 * to get those fields.
	 *
	 * @return the map containing the number list field names and functions
	 */
	public List<FieldFunction<T, List<Number>>> getNumberListFunctions() {
		return Optional.ofNullable(
			_fieldFunctions.get("NUMBER_LIST")
		).<List<FieldFunction<T, List<Number>>>>map(
			Unsafe::unsafeCast
		).orElseGet(
			Collections::emptyList
		);
	}

	/**
	 * Returns the related collections.
	 *
	 * @return the related collections
	 */
	public Stream<RelatedCollection<? extends Identifier>>
		getRelatedCollections() {

		Stream<List<RelatedCollection<? extends Identifier>>> stream =
			Stream.of(_relatedCollections, _supplier.get());

		return stream.filter(
			Objects::nonNull
		).flatMap(
			Collection::stream
		);
	}

	/**
	 * Returns the related models.
	 *
	 * @return the related models
	 */
	public List<RelatedModel<T, ?>> getRelatedModels() {
		return _relatedModels;
	}

	/**
	 * Returns the map containing the string field names and the functions to
	 * get those fields.
	 *
	 * @return the map containing the string field names and functions
	 */
	public List<FieldFunction<T, String>> getStringFunctions() {
		return Optional.ofNullable(
			_fieldFunctions.get("STRING")
		).<List<FieldFunction<T, String>>>map(
			Unsafe::unsafeCast
		).orElseGet(
			Collections::emptyList
		);
	}

	/**
	 * Returns the map containing the string list field names and the functions
	 * to get those fields.
	 *
	 * @return the map containing the string list field names and functions
	 */
	public List<FieldFunction<T, List<String>>> getStringListFunctions() {
		return Optional.ofNullable(
			_fieldFunctions.get("STRING_LIST")
		).<List<FieldFunction<T, List<String>>>>map(
			Unsafe::unsafeCast
		).orElseGet(
			Collections::emptyList
		);
	}

	/**
	 * Returns the types.
	 *
	 * @return the types
	 */
	public List<String> getTypes() {
		return _types;
	}

	public boolean isNested() {
		if (_identifierFunction == null) {
			return true;
		}

		return false;
	}

	/**
	 * Creates generic representations of your domain models that Apio
	 * hypermedia writers can understand.
	 */
	public static class Builder<T, U> {

		public Builder() {
			this(null);
		}

		public Builder(Class<? extends Identifier<U>> identifierClass) {
			_identifierClass = identifierClass;

			Supplier<List<RelatedCollection<?>>> supplier =
				Collections::emptyList;

			_biConsumer = (clazz, relatedCollection) -> {
			};
			_representor = new Representor<>(identifierClass, supplier);
		}

		public Builder(
			Class<? extends Identifier<U>> identifierClass,
			BiConsumer<Class<?>, RelatedCollection<?>> biConsumer,
			Supplier<List<RelatedCollection<?>>> supplier) {

			_identifierClass = identifierClass;
			_biConsumer = biConsumer;

			_representor = new Representor<>(identifierClass, supplier);
		}

		/**
		 * Adds a type for a nested resource, skipping the identifier function
		 * because nested resources don't have an ID.
		 *
		 * @param  type the type name
		 * @return the builder's step
		 */
		public FirstStep<T> nestedTypes(String type, String... types) {
			Representor<T> representor = _withTypes(_representor, type, types);

			return new FirstStep<>(representor, _biConsumer, _identifierClass);
		}

		/**
		 * Adds a type for the model.
		 *
		 * @param  type the type name
		 * @return the builder's step
		 */
		public IdentifierStep<T, U> types(String type, String... types) {
			Representor<T> representor = _withTypes(_representor, type, types);

			return new IdentifierStep<>(
				representor, _biConsumer, _identifierClass);
		}

		private final BiConsumer<Class<?>, RelatedCollection<?>> _biConsumer;
		private Class<? extends Identifier> _identifierClass;
		private final Representor<T> _representor;

	}

	public static class FirstStep<T> {

		/**
		 * Adds information about the bidirectional relation of a linked
		 * resource in the actual resource and a collection of items in the
		 * related resource.
		 *
		 * @param  key the relation's name in the resource
		 * @param  relatedKey the relation's name in the related resource
		 * @param  identifierClass the related resource identifier's class
		 * @param  identifierFunction the function used to get the related
		 *         resource's identifier
		 * @return the builder's step
		 */
		public <S> FirstStep<T> addBidirectionalModel(
			String key, String relatedKey,
			Class<? extends Identifier<S>> identifierClass,
			Function<T, S> identifierFunction) {

			RelatedCollection<?> relatedCollection = new RelatedCollection<>(
				relatedKey, _identifierClass);

			_biConsumer.accept(identifierClass, relatedCollection);

			RelatedModel<T, S> relatedModel = new RelatedModel<>(
				key, identifierClass, identifierFunction);

			Representor<T> representor = _withRelatedModel(
				_representor, relatedModel);

			return new FirstStep<>(representor, _biConsumer, identifierClass);
		}

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

			Representor<T> representor = _withBinaryFunction(
				_representor, fieldFunction);

			return new FirstStep<>(representor, _biConsumer, _identifierClass);
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

			Representor<T> representor = _withBooleanFunction(
				_representor, fieldFunction);

			return new FirstStep<>(representor, _biConsumer, _identifierClass);
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

			Representor<T> representor = _withBooleanListFunction(
				_representor, fieldFunction);

			return new FirstStep<>(representor, _biConsumer, _identifierClass);
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

			Representor<T> representor = _withStringFunction(
				_representor, fieldFunction);

			return new FirstStep<>(representor, _biConsumer, _identifierClass);
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

			Representor<T> representor = _withLinkFunction(
				_representor, fieldFunction);

			return new FirstStep<>(representor, _biConsumer, _identifierClass);
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

			Representor<T> representor = _withRelatedModel(
				_representor, relatedModel);

			return new FirstStep<>(representor, _biConsumer, _identifierClass);
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

			Representor<T> representor = _withLanguageFunction(
				_representor, fieldFunction);

			return new FirstStep<>(representor, _biConsumer, _identifierClass);
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
		 * @param  transformFunction the function that transforms the model
		 *         into the model used inside the nested representor
		 * @param  representorFunction the function that creates the nested
		 *         representor
		 * @return the builder's step
		 */
		public <W> FirstStep<T> addNested(
			String key, Function<T, W> transformFunction,
			Function<Builder<W, ?>, Representor<W>> representorFunction) {

			NestedFieldFunction<T, W> nestedFieldFunction =
				new NestedFieldFunction<>(
					key, transformFunction,
					representorFunction.apply(new Builder<>()));

			Representor<T> representor = _withNestedFieldFunction(
				_representor, nestedFieldFunction);

			return new FirstStep<>(representor, _biConsumer, _identifierClass);
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

			Representor<T> representor = _withNumberFunction(
				_representor, fieldFunction);

			return new FirstStep<>(representor, _biConsumer, _identifierClass);
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

			Representor<T> representor = _withNumberListFunction(
				_representor, fieldFunction);

			return new FirstStep<>(representor, _biConsumer, _identifierClass);
		}

		/**
		 * Adds information about a related collection.
		 *
		 * @param  key the relation's name
		 * @param  itemIdentifierClass the class of the collection items'
		 *         identifier
		 * @return the builder's step
		 */
		public <S extends Identifier> FirstStep<T> addRelatedCollection(
			String key, Class<S> itemIdentifierClass) {

			RelatedCollection<S> relatedCollection = new RelatedCollection<>(
				key, itemIdentifierClass);

			Representor<T> representor = _withRelatedCollection(
				_representor, relatedCollection);

			return new FirstStep<>(representor, _biConsumer, _identifierClass);
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

			Representor<T> representor = _withStringFunction(
				_representor, fieldFunction);

			return new FirstStep<>(representor, _biConsumer, _identifierClass);
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

			Representor<T> representor = _withStringListFunction(
				_representor, fieldFunction);

			return new FirstStep<>(representor, _biConsumer, _identifierClass);
		}

		/**
		 * Constructs and returns a {@link Representor} instance _with the
		 * information provided to the builder.
		 *
		 * @return the {@code Representor} instance
		 */
		public Representor<T> build() {
			return _representor;
		}

		private FirstStep(
			Representor<T> representor,
			BiConsumer<Class<?>, RelatedCollection<?>> biConsumer,
			Class<? extends Identifier> identifierClass) {

			_representor = representor;
			_biConsumer = biConsumer;
			_identifierClass = identifierClass;
		}

		private final BiConsumer<Class<?>, RelatedCollection<?>> _biConsumer;
		private Class<? extends Identifier> _identifierClass;
		private final Representor<T> _representor;

	}

	public static class IdentifierStep<T, U> {

		/**
		 * Provides a lambda function that can be used to obtain a model's
		 * identifier.
		 *
		 * @param  identifierFunction lambda function used to obtain a
		 *         model's identifier
		 * @return the builder's next step
		 */
		public FirstStep<T> identifier(Function<T, U> identifierFunction) {
			Representor<T> representor = _withIdentifierFunction(
				_representor, identifierFunction);

			return new FirstStep<>(representor, _biConsumer, _identifierClass);
		}

		private IdentifierStep(
			Representor<T> representor,
			BiConsumer<Class<?>, RelatedCollection<?>> biConsumer,
			Class<? extends Identifier> identifierClass) {

			_representor = representor;
			_biConsumer = biConsumer;
			_identifierClass = identifierClass;
		}

		private final BiConsumer<Class<?>, RelatedCollection<?>> _biConsumer;
		private final Class<? extends Identifier> _identifierClass;
		private final Representor<T> _representor;

	}

	private static <T> Representor<T> _withBinaryFunction(
		Representor<T> representor,
		FieldFunction<T, BinaryFile> fieldFunction) {

		representor._binaryFunctions.put(
			fieldFunction.key, (BinaryFunction<T>)fieldFunction.function);

		return _withFieldFunction(representor, fieldFunction, "BINARY");
	}

	private static <T> Representor<T> _withBooleanFunction(
		Representor<T> representor, FieldFunction<T, Boolean> fieldFunction) {

		return _withFieldFunction(representor, fieldFunction, "BOOLEAN");
	}

	private static <T> Representor<T> _withBooleanListFunction(
		Representor<T> representor,
		FieldFunction<T, List<Boolean>> fieldFunction) {

		return _withFieldFunction(representor, fieldFunction, "BOOLEAN_LIST");
	}

	private static <T, S> Representor<T> _withFieldFunction(
		Representor<T> representor, FieldFunction<T, S> fieldFunction,
		String key) {

		Representor<T> newRepresentor = new Representor<>(representor);

		List<FieldFunction<T, ?>> list =
			newRepresentor._fieldFunctions.computeIfAbsent(
				key, __ -> new ArrayList<>());

		list.add(fieldFunction);

		return newRepresentor;
	}

	private static <T> Representor<T> _withIdentifierFunction(
		Representor<T> representor, Function<T, ?> identifierFunction) {

		Representor<T> newRepresentor = new Representor<>(representor);

		newRepresentor._identifierFunction = identifierFunction;

		return newRepresentor;
	}

	private static <T> Representor<T> _withLanguageFunction(
		Representor<T> representor,
		FieldFunction<T, Function<Language, String>> fieldFunction) {

		return _withFieldFunction(representor, fieldFunction, "LOCALIZED");
	}

	private static <T> Representor<T> _withLinkFunction(
		Representor<T> representor, FieldFunction<T, String> fieldFunction) {

		return _withFieldFunction(representor, fieldFunction, "LINK");
	}

	private static <T> Representor<T> _withNestedFieldFunction(
		Representor<T> representor,
		NestedFieldFunction<T, ?> nestedFieldFunction) {

		Representor<T> newRepresentor = new Representor<>(representor);

		newRepresentor._nestedFieldFunctions.add(nestedFieldFunction);

		return newRepresentor;
	}

	private static <T> Representor<T> _withNumberFunction(
		Representor<T> representor, FieldFunction<T, Number> fieldFunction) {

		return _withFieldFunction(representor, fieldFunction, "NUMBER");
	}

	private static <T> Representor<T> _withNumberListFunction(
		Representor<T> representor,
		FieldFunction<T, List<Number>> fieldFunction) {

		return _withFieldFunction(representor, fieldFunction, "NUMBER_LIST");
	}

	private static <T> Representor<T> _withRelatedCollection(
		Representor<T> representor, RelatedCollection<?> relatedCollection) {

		Representor<T> newRepresentor = new Representor<>(representor);

		newRepresentor._relatedCollections.add(relatedCollection);

		return newRepresentor;
	}

	private static <T> Representor<T> _withRelatedModel(
		Representor<T> representor, RelatedModel<T, ?> relatedModel) {

		Representor<T> newRepresentor = new Representor<>(representor);

		newRepresentor._relatedModels.add(relatedModel);

		return newRepresentor;
	}

	private static <T> Representor<T> _withStringFunction(
		Representor<T> representor, FieldFunction<T, String> fieldFunction) {

		return _withFieldFunction(representor, fieldFunction, "STRING");
	}

	private static <T> Representor<T> _withStringListFunction(
		Representor<T> representor,
		FieldFunction<T, List<String>> fieldFunction) {

		return _withFieldFunction(representor, fieldFunction, "STRING_LIST");
	}

	private static <T> Representor<T> _withTypes(
		Representor<T> representor, String type, String... types) {

		Representor<T> newRepresentor = new Representor<>(representor);

		newRepresentor._types.add(type);
		Collections.addAll(newRepresentor._types, types);

		return newRepresentor;
	}

	private Representor(
		Class<? extends Identifier<?>> identifierClass,
		Supplier<List<RelatedCollection<?>>> supplier) {

		_binaryFunctions = new LinkedHashMap<>();
		_fieldFunctions = new LinkedHashMap<>();
		_identifierClass = identifierClass;
		_nestedFieldFunctions = new ArrayList<>();
		_relatedCollections = new ArrayList<>();
		_supplier = supplier;
		_relatedModels = new ArrayList<>();
		_types = new ArrayList<>();
	}

	private Representor(
		Map<String, BinaryFunction<T>> binaryFunctions,
		Map<String, List<FieldFunction<T, ?>>> fieldFunctions,
		Class<? extends Identifier<?>> identifierClass,
		Function<T, ?> identifierFunction,
		List<NestedFieldFunction<T, ?>> nestedFieldFunctions,
		List<RelatedCollection<?>> relatedCollections,
		List<RelatedModel<T, ?>> relatedModels,
		Supplier<List<RelatedCollection<?>>> supplier, List<String> types) {

		_binaryFunctions = binaryFunctions;
		_fieldFunctions = fieldFunctions;
		_identifierClass = identifierClass;
		_identifierFunction = identifierFunction;
		_nestedFieldFunctions = nestedFieldFunctions;
		_relatedCollections = relatedCollections;
		_relatedModels = relatedModels;
		_supplier = supplier;
		_types = types;
	}

	private Representor(Representor<T> representor) {
		this(
			representor._binaryFunctions, representor._fieldFunctions,
			representor._identifierClass, representor._identifierFunction,
			representor._nestedFieldFunctions, representor._relatedCollections,
			representor._relatedModels, representor._supplier,
			representor._types);
	}

	private final Map<String, BinaryFunction<T>> _binaryFunctions;
	private final Map<String, List<FieldFunction<T, ?>>> _fieldFunctions;
	private final Class<? extends Identifier<?>> _identifierClass;
	private Function<T, ?> _identifierFunction;
	private final List<NestedFieldFunction<T, ?>> _nestedFieldFunctions;
	private final List<RelatedCollection<?>> _relatedCollections;
	private final List<RelatedModel<T, ?>> _relatedModels;
	private final Supplier<List<RelatedCollection<?>>> _supplier;
	private final List<String> _types;

}