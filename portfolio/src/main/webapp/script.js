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
 * Fetches a random fact from the server
 */
async function getRandomFact(){
  const response = await fetch('/fact');
  const message = await response.text();
  document.getElementById('fact-container').innerText = message;
}

/** 
 * Fetches comments from the server and adds them to the DOM. 
 */
function loadComments() {
  fetch('/data')
  .then(response => response.json())
  .then((comments) => {
    const commentListElement = document.getElementById('comment-container');
    comments.forEach((comment) => {
      commentListElement.appendChild(createCommentElement(comment));
    })
  });
}

/** 
 * Creates an element that represents a comment
 */
function createCommentElement(comment) {
  const commentElement = document.createElement('li');
  commentElement.className = 'Comment';

  const textElement = document.createElement('span');
  textElement.innerText = comment;

  commentElement.appendChild(textElement);
  return commentElement;
}
