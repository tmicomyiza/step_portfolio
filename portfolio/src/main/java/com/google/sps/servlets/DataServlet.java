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

  private int MaxComments;
  private int UserChoice;
  
  @Override
  public void init() {
      MaxComments = 50;
      UserChoice = 1;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("Comment");

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    List<String> comments = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
      
      if (UserChoice > 0){
        String text = (String) entity.getProperty("text");
        comments.add(text);
      }
      else{
        break;
      }
      UserChoice --;
    }

    Gson gson = new Gson();
    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(comments));
  }


  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // get input from the form.
    String text = getParameter(request, "text-input","");

    // Get number of comments user wants to see.
    UserChoice = getUserChoice(request);

    if (!text.isEmpty()){
      Entity commentEntity = new Entity("Comment");
      commentEntity.setProperty("text", text);

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
    String UserChoiceString = getParameter(request, "user-choice","");

    if (UserChoiceString.isEmpty()){
        System.err.println("Could not convert empty string ");
    }

    // Convert the input to an int.
    int UserChoice;
    try {
      UserChoice = Integer.parseInt(UserChoiceString);
    } catch (NumberFormatException e) {
      System.err.println("Could not convert to int: " + UserChoiceString);
      return -1;
    }

    // Check that the input is between 1 and 50.
    if (UserChoice < 1 || UserChoice > MaxComments ) {
      System.err.println("User choice is out of range: " + UserChoiceString);
      return -1;
    }

    return UserChoice;
  }
}




