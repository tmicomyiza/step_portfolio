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

import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/** Servlet that returns a random fact*/
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private List<String> facts;
  private ArrayList<String> comments;

  @Override
  public void init() {
    facts = new ArrayList<>();
    facts.add("black belt in Karate, JKA style");
    facts.add("I love stargazing");
    facts.add("I love to dance");
    facts.add("I have a dog and her name is Violet");
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    //String fact = facts.get((int) (Math.random() * facts.size()));
    Gson gson = new Gson();
    String fact = gson.toJson(facts); 
    //response.setContentType("text/html;");
    response.setContentType("application/json;");
    response.getWriter().println(fact);
  }
}

