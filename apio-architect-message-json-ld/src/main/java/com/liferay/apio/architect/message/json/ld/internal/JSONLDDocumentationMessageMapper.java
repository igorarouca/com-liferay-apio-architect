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

package com.liferay.apio.architect.message.json.ld.internal;

import static com.liferay.apio.architect.message.json.ld.internal.JSONLDConstants.FIELD_NAME_CONTEXT;
import static com.liferay.apio.architect.message.json.ld.internal.JSONLDConstants.FIELD_NAME_DESCRIPTION;
import static com.liferay.apio.architect.message.json.ld.internal.JSONLDConstants.FIELD_NAME_ID;
import static com.liferay.apio.architect.message.json.ld.internal.JSONLDConstants.FIELD_NAME_TITLE;
import static com.liferay.apio.architect.message.json.ld.internal.JSONLDConstants.FIELD_NAME_TYPE;
import static com.liferay.apio.architect.message.json.ld.internal.JSONLDConstants.MEDIA_TYPE;
import static com.liferay.apio.architect.message.json.ld.internal.JSONLDConstants.TYPE_API_DOCUMENTATION;
import static com.liferay.apio.architect.message.json.ld.internal.JSONLDConstants.URL_HYDRA_PROFILE;

import com.liferay.apio.architect.alias.RequestFunction;
import com.liferay.apio.architect.documentation.Documentation;
import com.liferay.apio.architect.form.FormField;
import com.liferay.apio.architect.message.json.DocumentationMessageMapper;
import com.liferay.apio.architect.message.json.JSONObjectBuilder;

import java.util.Optional;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;

import org.osgi.service.component.annotations.Component;

/**
 * Represents documentation in JSON-LD + Hydra format.
 *
 * <p>
 * For more information, see <a href="https://json-ld.org/">JSON-LD </a> and <a
 * href="https://www.hydra-cg.com/">Hydra </a> .
 * </p>
 *
 * @author Alejandro Hernández
 */
@Component(immediate = true)
public class JSONLDDocumentationMessageMapper
	implements DocumentationMessageMapper {

	@Override
	public String getMediaType() {
		return MEDIA_TYPE;
	}

	@Override
	public void mapDescription(
		JSONObjectBuilder jsonObjectBuilder, String description) {

		jsonObjectBuilder.field(
			FIELD_NAME_DESCRIPTION
		).stringValue(
			description
		);
	}

	@Override
	public void mapFormField(
		JSONObjectBuilder jsonObjectBuilder, Object name,
		Optional<FormField> formFieldOptional) {

		jsonObjectBuilder.field(
			JSONLDConstants.FIELD_NAME_TITLE).stringValue(String.valueOf(name));

		Boolean required =
			formFieldOptional.map(formField -> formField.required).orElse(
				false);

		jsonObjectBuilder.field(
			JSONLDConstants.FIELD_NAME_REQUIRED
		).booleanValue(
			required
		);

		jsonObjectBuilder.field(
			"readonly"
		).booleanValue(
			!formFieldOptional.isPresent()
		);

		jsonObjectBuilder.field(
			"writeonly"
		).booleanValue(
			false
		);
	}

	@Override
	public void mapOperation(
		JSONObjectBuilder jsonObjectBuilder, String entity,
		RequestFunction requestFunction) {

		String httpMethod = requestFunction.getHttpMethod();

		jsonObjectBuilder.field(
			FIELD_NAME_ID
		).stringValue(
			"_:" + entity
		);

		jsonObjectBuilder.field(
			FIELD_NAME_TYPE
		).stringValue(
			JSONLDConstants.TYPE_OPERATION
		);
		jsonObjectBuilder.field(
			"method"
		).stringValue(
			httpMethod
		);

		String value = _getReturnValue(entity, requestFunction);

		jsonObjectBuilder.field(
			"returns"
		).stringValue(
			value
		);
	}

	@Override
	public void mapResource(
		JSONObjectBuilder jsonObjectBuilder, String entity) {

		jsonObjectBuilder.field(
			FIELD_NAME_ID
		).stringValue(
			"http://schema.org/" + entity
		);
		jsonObjectBuilder.field(
			FIELD_NAME_TYPE
		).stringValue(
			JSONLDConstants.TYPE_CLASS
		);
		jsonObjectBuilder.field(
			JSONLDConstants.FIELD_NAME_TITLE
		).stringValue(
			entity
		);
	}

	@Override
	public void mapTitle(JSONObjectBuilder jsonObjectBuilder, String title) {
		jsonObjectBuilder.field(
			FIELD_NAME_TITLE
		).stringValue(
			title
		);
	}

	@Override
	public void onFinishOperation(
		JSONObjectBuilder jsonObjectBuilder,
		JSONObjectBuilder propertyJsonObjectBuilder) {

		jsonObjectBuilder.field(
			"supportedOperation"
		).arrayValue(
		).add(
			propertyJsonObjectBuilder
		);
	}

	@Override
	public void onFinishProperty(
		JSONObjectBuilder jsonObjectBuilder,
		JSONObjectBuilder propertyJsonObjectBuilder) {

		jsonObjectBuilder.field(
			"supportedProperty"
		).arrayValue(
		).add(
			propertyJsonObjectBuilder
		);
	}

	@Override
	public void onFinishResource(
		JSONObjectBuilder jsonObjectBuilder,
		JSONObjectBuilder entityJsonObjectBuilder) {

		jsonObjectBuilder.field(
			"supportedClass"
		).arrayValue(
		).add(
			entityJsonObjectBuilder
		);
	}

	@Override
	public void onStart(
		JSONObjectBuilder jsonObjectBuilder, Documentation documentation,
		HttpHeaders httpHeaders) {

		JSONObjectBuilder.FieldStep contextBuilder =
			jsonObjectBuilder.nestedField(FIELD_NAME_CONTEXT);

		JSONObjectBuilder.FieldStep hydra = contextBuilder.field("hydra");

		hydra.stringValue(URL_HYDRA_PROFILE);

		jsonObjectBuilder.field(
			FIELD_NAME_ID
		).stringValue(
			"http://api.example.com/doc/"
		);

		jsonObjectBuilder.field(
			FIELD_NAME_TYPE
		).stringValue(
			TYPE_API_DOCUMENTATION
		);

		JSONObjectBuilder.FieldStep propertyBuilder =
			contextBuilder.nestedField("property");

		propertyBuilder.field(
			FIELD_NAME_ID
		).stringValue(
			JSONLDConstants.FIELD_NAME_PROPERTY
		);

		propertyBuilder.field(
			FIELD_NAME_TYPE
		).stringValue(
			FIELD_NAME_ID
		);

		contextBuilder.field(
			"readonly"
		).stringValue(
			"hydra:readonly"
		);

		contextBuilder.field(
			"writeonly"
		).stringValue(
			"hydra:writeonly"
		);

		contextBuilder.field(
			"supportedClass"
		).stringValue(
			"hydra:supportedClass"
		);

		contextBuilder.field(
			"supportedProperty"
		).stringValue(
			"hydra:supportedProperty"
		);

		contextBuilder.field(
			"supportedOperation"
		).stringValue(
			"hydra:supportedOperation"
		);

		contextBuilder.field(
			"method"
		).stringValue(
			"hydra:method"
		);

		JSONObjectBuilder.FieldStep expectBuilder = contextBuilder.nestedField(
			"expect");

		expectBuilder.field(
			FIELD_NAME_ID
		).stringValue(
			"hydra:expect"
		);

		expectBuilder.field(
			FIELD_NAME_TYPE
		).stringValue(
			FIELD_NAME_ID
		);

		JSONObjectBuilder.FieldStep returnsBuilder = contextBuilder.nestedField(
			"returns");

		returnsBuilder.field(
			FIELD_NAME_ID
		).stringValue(
			"hydra:returns"
		);

		returnsBuilder.field(
			FIELD_NAME_TYPE
		).stringValue(
			FIELD_NAME_ID
		);

		contextBuilder.field(
			"statusCodes"
		).stringValue(
			"hydra:statusCodes"
		);

		contextBuilder.field(
			"code"
		).stringValue(
			"hydra:statusCodes"
		);
	}

	private String _getReturnValue(
		String entity, RequestFunction requestFunction) {

		String value = null;

		if (HttpMethod.DELETE.equals(requestFunction.getHttpMethod())) {
			value = "http://www.w3.org/2002/07/owl#Nothing";
		}
		else if (requestFunction.getCollection()) {
			value = "http://www.w3.org/ns/hydra/core#Collection";
		}
		else {
			value = "http://schema.org/" + entity;
		}

		return value;
	}

}