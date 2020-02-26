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

package ru.swayfarer.swl2.json.gsonformatters;

import java.io.PrintWriter;

import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.z.dependencies.com.google.gson.JsonArray;
import ru.swayfarer.swl2.z.dependencies.com.google.gson.JsonElement;
import ru.swayfarer.swl2.z.dependencies.com.google.gson.JsonElementVisitor;
import ru.swayfarer.swl2.z.dependencies.com.google.gson.JsonFormatter;
import ru.swayfarer.swl2.z.dependencies.com.google.gson.JsonObject;
import ru.swayfarer.swl2.z.dependencies.com.google.gson.JsonPrimitive;
import ru.swayfarer.swl2.z.dependencies.com.google.gson.JsonTreeNavigator;

/**
 * Formats Json in a compact way eliminating all unnecessary whitespace.
 * 
 * @author Inderjeet Singh
 */
public class PrettyGsonFormatter implements JsonFormatter {

	public String offsetText;
	
	public PrettyGsonFormatter()
	{
		this(StringUtils.TAB);
	}
	
	public PrettyGsonFormatter(String offsetText)
	{
		this.offsetText = offsetText;
	}
	
	private static class FormattingVisitor implements JsonElementVisitor {
		
		public int offsetCount = 0;
		public String offsetText;
		
		private final PrintWriter writer;

		FormattingVisitor(PrintWriter writer)
		{
			this.writer = writer;
		}

		public void visitPrimitive(JsonPrimitive primitive)
		{
			writer.append(primitive.toString());
		}

		public void startArray(JsonArray array)
		{
			writer.append('[');
			offsetCount ++;

			appendOffset();
		}

		public void visitArrayMember(JsonArray parent, JsonPrimitive member, boolean isFirst)
		{
			if (!isFirst)
			{
				writer.append(',');
				appendOffset();
			}
			writer.append(member.toString());
		}

		public void visitArrayMember(JsonArray parent, JsonArray member, boolean isFirst)
		{
			if (!isFirst)
			{
				writer.append(',');
				appendOffset();
			}
		}

		public void visitArrayMember(JsonArray parent, JsonObject member, boolean isFirst)
		{
			if (!isFirst)
			{
				writer.append(',');
				appendOffset();
			}
		}

		public void endArray(JsonArray array)
		{
			offsetCount --;
			appendOffset();
			writer.append(']');
		}

		public void startObject(JsonObject object)
		{
			writer.append('{');
			offsetCount ++;
			appendOffset();
		}

		public void visitObjectMember(JsonObject parent, String memberName, JsonPrimitive member, boolean isFirst)
		{
			if (!isFirst)
			{
				writer.append(',');
				appendOffset();
			}
			writer.append('"');
			writer.append(memberName);
			writer.append("\":");
			writer.append(member.toString());
		}

		public void visitObjectMember(JsonObject parent, String memberName, JsonArray member, boolean isFirst)
		{
			if (!isFirst)
			{
				writer.append(',');
				appendOffset();
			}
			writer.append('"');
			writer.append(memberName);
			writer.append("\":");
		}

		public void visitObjectMember(JsonObject parent, String memberName, JsonObject member, boolean isFirst)
		{
			if (!isFirst)
			{
				writer.append(',');
				appendOffset();
			}
			writer.append('"');
			writer.append(memberName);
			writer.append("\":");
		}

		public void endObject(JsonObject object)
		{
			offsetCount --;
			appendOffset();
			writer.append('}');
		}
		
		public void appendOffset()
		{
			writer.append("\n");
			
			if (offsetCount <= 0)
				return;
			
			StringBuilder builder = new StringBuilder();
			for (int i1 = 0; i1 < offsetCount; i1 ++)
				builder.append(offsetText);

			
			writer.append(builder.toString());
		}
	}

	public void format(JsonElement root, PrintWriter writer)
	{
		if (root == null)
		{
			return;
		}
		FormattingVisitor visitor = new FormattingVisitor(writer);
		visitor.offsetText = offsetText;
		JsonTreeNavigator navigator = new JsonTreeNavigator(visitor);
		navigator.navigate(root);
	}
}
