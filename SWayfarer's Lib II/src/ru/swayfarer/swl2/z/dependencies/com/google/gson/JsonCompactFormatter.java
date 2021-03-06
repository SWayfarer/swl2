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


import java.io.PrintWriter;


/**
 * Formats Json in a compact way eliminating all unnecessary whitespace.
 * 
 * @author Inderjeet Singh
 */
public class JsonCompactFormatter implements JsonFormatter {

  private static class FormattingVisitor implements JsonElementVisitor {
    private final PrintWriter writer;
    
    FormattingVisitor(PrintWriter writer) {
      this.writer = writer;
    }
    
    public void visitPrimitive(JsonPrimitive primitive) {
      writer.append(primitive.toString());
    }
    
    public void startArray(JsonArray array) {      
      writer.append('[');
    }

    public void visitArrayMember(JsonArray parent, JsonPrimitive member, boolean isFirst) {
      if (!isFirst) {
        writer.append(',');
      }      
      writer.append(member.toString());
    }
    
    public void visitArrayMember(JsonArray parent, JsonArray member, boolean isFirst) {
      if (!isFirst) {
        writer.append(',');
      }      
    }
    
    public void visitArrayMember(JsonArray parent, JsonObject member, boolean isFirst) {
      if (!isFirst) {
        writer.append(',');
      }      
    }
    
    public void endArray(JsonArray array) {
      writer.append(']');
    }
    
    public void startObject(JsonObject object) {
      writer.append('{');
    }
    
    public void visitObjectMember(JsonObject parent, String memberName, JsonPrimitive member, 
        boolean isFirst) {
      if (!isFirst) {
        writer.append(',');
      }      
      writer.append('"');
      writer.append(memberName);
      writer.append("\":");
      writer.append(member.toString());
    }
    
    public void visitObjectMember(JsonObject parent, String memberName, JsonArray member, 
        boolean isFirst) {
      if (!isFirst) {
        writer.append(',');
      }      
      writer.append('"');
      writer.append(memberName);
      writer.append("\":");
    }

    public void visitObjectMember(JsonObject parent, String memberName, JsonObject member, 
        boolean isFirst) {
      if (!isFirst) {
        writer.append(',');
      }      
      writer.append('"');
      writer.append(memberName);
      writer.append("\":");      
    }
    
    public void endObject(JsonObject object) {
      writer.append('}');
    }
  }
  
  public void format(JsonElement root, PrintWriter writer) {
    if (root == null) {
      return;
    }
    FormattingVisitor visitor = new FormattingVisitor(writer);
    JsonTreeNavigator navigator = new JsonTreeNavigator(visitor);
    navigator.navigate(root);
  }  
}
