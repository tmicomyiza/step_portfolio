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

/**
 * Adds a random fact to the page.
 */
function addRandomFact() {
  const facts =
      ['I am black belt in Karate, JKA style', 'I love stargazing', 'I am big dancer',
                                             'I have a dog and her name is Violet'];

  // Pick a random greeting.
  const fact = facts[Math.floor(Math.random() * facts.length)];

  // Add it to the page.
  const factcontainer = document.getElementById('fact-container');
  factcontainer.innerText = fact;
}

/**
 * Display correct answers to the page
 */
function Solutions(){
    const correctAnswers = ["2", "8", "12", "16"];
    var answerlable, answerradio;
    //highlight solutions to the page
    for (var i = 0; i < correctAnswers.length; i ++){
        answerlable = document.getElementById(correctAnswers[i]);
        answerradio = document.getElementById("rad" + correctAnswers[i]);
        
        answerlable.style.color = "green";
        answerradio.checked = true;

    }
}

/**
 * Fetches a message from the server
 */
async function randomFact(){
    const response = await fetch('/data');
    const message = await response.text();
    document.getElementById('fact-container').innerText = message;
}