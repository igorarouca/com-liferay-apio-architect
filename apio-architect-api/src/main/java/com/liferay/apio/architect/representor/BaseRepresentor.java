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

import com.liferay.apio.architect.alias.BinaryFunction;
import com.liferay.apio.architect.file.BinaryFile;
import com.liferay.apio.architect.language.Language;
import com.liferay.apio.architect.related.RelatedModel;
import com.liferay.apio.architect.unsafe.Unsafe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Base class for {@code Representors}.
 *
 * <p>
 * Descendants of this class holds information about the metadata supported for
 * a resource.
 * </p>
 *
 * <p>
 * Only two descendants are allowed: {@link Representor} and {@link
 * NestedRepresentor}.
 * </p>
 *
 * @author Alejandro Hernández
 * @param  <T> the model's type
 */
public abstract class BaseRepresentor<T> {

	/**
	 * Returns a binary resource linked to a model, if present. Returns {@code
	 * Optional#empty} otherwise.
	 *
	 * @param  binaryId the ID of the binary resource
	 * @return a binary resource linked to a model if present; {@code
	 *         Optional#empty()} otherwise
	 * @review
	 */
	public Optional<BinaryFunction<T>> getBinaryFunction(String binaryId) {
		return Optional.ofNullable(binaryFunctions.get(binaryId));
	}

	/**
	 * Returns the binary functions linked to a model.
	 *
	 * @return the binary functions linked to a model
	 */
	public List<FieldFunction<T, BinaryFile>> getBinaryFunctions() {
		return Optional.ofNullable(
			fieldFunctions.get("BINARY")
		).<List<FieldFunction<T, BinaryFile>>>map(
			Unsafe::unsafeCast
		).orElseGet(
			Collections::emptyList
		);
	}

	/**
	 * Returns the list containing the boolean field names and the functions to
	 * get those fields.
	 *
	 * @return the list containing the boolean field names and functions
	 */
	public List<FieldFunction<T, Boolean>> getBooleanFunctions() {
		return Optional.ofNullable(
			fieldFunctions.get("BOOLEAN")
		).<List<FieldFunction<T, Boolean>>>map(
			Unsafe::unsafeCast
		).orElseGet(
			Collections::emptyList
		);
	}

	/**
	 * Returns the list containing the boolean list field names and the
	 * functions to get those fields.
	 *
	 * @return the list containing the boolean list field names and functions
	 */
	public List<FieldFunction<T, List<Boolean>>> getBooleanListFunctions() {
		return Optional.ofNullable(
			fieldFunctions.get("BOOLEAN_LIST")
		).<List<FieldFunction<T, List<Boolean>>>>map(
			Unsafe::unsafeCast
		).orElseGet(
			Collections::emptyList
		);
	}

	/**
	 * Returns the list containing the links field names and the functions to
	 * get those links.
	 *
	 * @return the list containing the links field names and functions
	 */
	public List<FieldFunction<T, String>> getLinkFunctions() {
		return Optional.ofNullable(
			fieldFunctions.get("LINK")
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
	 * @return the list containing the localized string field names and
	 *         functions
	 */
	public List<FieldFunction<T, Function<Language, String>>>
		getLocalizedStringFunctions() {

		return Optional.ofNullable(
			fieldFunctions.get("LOCALIZED")
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
		return nestedFieldFunctions;
	}

	/**
	 * Returns the list containing the number field names and the functions to
	 * get those fields.
	 *
	 * @return the list containing the number field names and functions
	 */
	public List<FieldFunction<T, Number>> getNumberFunctions() {
		return Optional.ofNullable(
			fieldFunctions.get("NUMBER")
		).<List<FieldFunction<T, Number>>>map(
			Unsafe::unsafeCast
		).orElseGet(
			Collections::emptyList
		);
	}

	/**
	 * Returns the list containing the number list field names and the functions
	 * to get those fields.
	 *
	 * @return the list containing the number list field names and functions
	 */
	public List<FieldFunction<T, List<Number>>> getNumberListFunctions() {
		return Optional.ofNullable(
			fieldFunctions.get("NUMBER_LIST")
		).<List<FieldFunction<T, List<Number>>>>map(
			Unsafe::unsafeCast
		).orElseGet(
			Collections::emptyList
		);
	}

	/**
	 * Returns the related models.
	 *
	 * @return the related models
	 */
	public List<RelatedModel<T, ?>> getRelatedModels() {
		return relatedModels;
	}

	/**
	 * Returns the list containing the string field names and the functions to
	 * get those fields.
	 *
	 * @return the list containing the string field names and functions
	 */
	public List<FieldFunction<T, String>> getStringFunctions() {
		return Optional.ofNullable(
			fieldFunctions.get("STRING")
		).<List<FieldFunction<T, String>>>map(
			Unsafe::unsafeCast
		).orElseGet(
			Collections::emptyList
		);
	}

	/**
	 * Returns the list containing the string list field names and the functions
	 * to get those fields.
	 *
	 * @return the list containing the string list field names and functions
	 */
	public List<FieldFunction<T, List<String>>> getStringListFunctions() {
		return Optional.ofNullable(
			fieldFunctions.get("STRING_LIST")
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
		return types;
	}

	public abstract boolean isNested();

	public abstract static class BaseBuilder<T, S extends BaseRepresentor<T>> {

		protected BaseBuilder(S baseRepresentor) {
			this.baseRepresentor = baseRepresentor;
		}

		protected final S baseRepresentor;

	}

	protected BaseRepresentor() {
		binaryFunctions = new LinkedHashMap<>();
		fieldFunctions = new LinkedHashMap<>();
		nestedFieldFunctions = new ArrayList<>();
		relatedModels = new ArrayList<>();
		types = new ArrayList<>();
	}

	protected void addBinaryFunction(
		FieldFunction<T, BinaryFile> fieldFunction) {

		binaryFunctions.put(
			fieldFunction.key, (BinaryFunction<T>)fieldFunction.function);

		addFieldFunction(fieldFunction, "BINARY");
	}

	protected void addBooleanFunction(FieldFunction<T, Boolean> fieldFunction) {
		addFieldFunction(fieldFunction, "BOOLEAN");
	}

	protected void addBooleanListFunction(
		FieldFunction<T, List<Boolean>> fieldFunction) {

		addFieldFunction(fieldFunction, "BOOLEAN_LIST");
	}

	protected <S> void addFieldFunction(
		FieldFunction<T, S> fieldFunction, String key) {

		List<FieldFunction<T, ?>> list = fieldFunctions.computeIfAbsent(
			key, __ -> new ArrayList<>());

		list.add(fieldFunction);
	}

	protected void addLanguageFunction(
		FieldFunction<T, Function<Language, String>> fieldFunction) {

		addFieldFunction(fieldFunction, "LOCALIZED");
	}

	protected void addLinkFunction(FieldFunction<T, String> fieldFunction) {
		addFieldFunction(fieldFunction, "LINK");
	}

	protected void addNestedFieldFunction(
		NestedFieldFunction<T, ?> nestedFieldFunction) {

		nestedFieldFunctions.add(nestedFieldFunction);
	}

	protected void addNumberFunction(FieldFunction<T, Number> fieldFunction) {
		addFieldFunction(fieldFunction, "NUMBER");
	}

	protected void addNumberListFunction(
		FieldFunction<T, List<Number>> fieldFunction) {

		addFieldFunction(fieldFunction, "NUMBER_LIST");
	}

	protected void addRelatedModel(RelatedModel<T, ?> relatedModel) {
		relatedModels.add(relatedModel);
	}

	protected void addStringFunction(FieldFunction<T, String> fieldFunction) {
		addFieldFunction(fieldFunction, "STRING");
	}

	protected void addStringListFunction(
		FieldFunction<T, List<String>> fieldFunction) {

		addFieldFunction(fieldFunction, "STRING_LIST");
	}

	protected void addTypes(String type, String... typesArray) {
		types.add(type);
		Collections.addAll(types, typesArray);
	}

	protected final Map<String, BinaryFunction<T>> binaryFunctions;
	protected final Map<String, List<FieldFunction<T, ?>>> fieldFunctions;
	protected final List<NestedFieldFunction<T, ?>> nestedFieldFunctions;
	protected final List<RelatedModel<T, ?>> relatedModels;
	protected final List<String> types;

}