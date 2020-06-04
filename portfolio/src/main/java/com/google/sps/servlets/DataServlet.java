// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

import java.util.Arrays;
import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/** 
 * Servlet that returns a random fact.
 */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private int maxComments;
  private int userChoice;
  public static String propertyKey = "text";
  public static String entityKey = "Comment";
  
  @Override
  public void init() {
    maxComments = 50;
    userChoice = 10;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query(entityKey);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    List<String> comments = new ArrayList<>();
    int count = userChoice;
    for (Entity entity : results.asIterable()) {
      
      if (count > 0){
        String text = (String) entity.getProperty(propertyKey);
        comments.add(text);
      }
      else{
        break;
      }
      count --;
    }

    Gson gson = new Gson();
    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(comments));
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // get input from the form.
    String text = getParameter(request, "text-input", "");

    // Get number of comments user wants to see.
    userChoice = getUserChoice(request);

    if (!text.isEmpty()){
      Entity commentEntity = new Entity(entityKey);
      commentEntity.setProperty(propertyKey, text);

      // store the input to datastore.
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      datastore.put(commentEntity);
    }

    // respond with the result.
    response.sendRedirect("/images.html");
  }

  /**
   * @return the request parameter, or the default value if the parameter
   *         was not specified by the client.
   */
  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }

  /** 
   * Returns the choice entered by the user, or -1 if the choice was invalid. 
   */
  private int getUserChoice(HttpServletRequest request) {
    // Get the input from the form.
    String userChoiceString = getParameter(request, "user-choice", "");

    // if the user doesn't provide number of comments they want to see,
    // we use the default value that is stored in UserChoice attribute.
    if (userChoiceString.isEmpty()){
      return userChoice;
    }

    // Convert the input to an int.
    int inputUserChoice;
    try {
      inputUserChoice = Integer.parseInt(userChoiceString);
    } catch (NumberFormatException e) {
      System.err.println("Could not convert to int: " + userChoiceString);
      return -1;
    }

    // Check that the input is between 1 and 50.
    if (inputUserChoice < 1 || inputUserChoice > maxComments ) {
      System.err.println("User choice is out of range: " + userChoiceString);
      return -1;
    }
    return inputUserChoice;
  }
}