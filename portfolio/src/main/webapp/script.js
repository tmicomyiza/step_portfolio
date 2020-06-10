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
 * Display correct answers to the page.
 */
function showSolutions(){
  const correctAnswers = ["2", "8", "12", "16"];
  var answerlable, answerradio;
  
  // highlight solutions to the page.
  for (var i = 0; i < correctAnswers.length; i ++){
    answerlable = document.getElementById(correctAnswers[i]);
    answerradio = document.getElementById("rad" + correctAnswers[i]);
    
    answerlable.style.color = "green";
    answerradio.checked = true;
  }
}

/**
 * Fetches a random fact from the server.
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
 * Creates an element that represents a comment.
 */
function createCommentElement(comment) {
  const commentElement = document.createElement('li');
  commentElement.className = 'Comment';

  const textElement = document.createElement('span');
  textElement.innerText = comment;

  commentElement.appendChild(textElement);
  return commentElement;
}

/** 
 * Creates a map and adds it to the page. 
 */
function initMap() {
  const map = new google.maps.Map(
    document.getElementById('map'),
    {center: {lat: 13.4487378, lng: -16.720855}, zoom: 2});

  addMarkers(map);
}

/**
 * Creates markers and add them to the map.
 */
function addMarkers(map) {
  const seattleMarker = new google.maps.Marker({
    position: {lat: 47.6129432, lng: -122.4821475},
    map: map,
    title: 'Seattle, WA'
  });

  const rwandaMarker = new google.maps.Marker({
    position: {lat: -1.9432848, lng: 28.7590534},
    map: map,
    title: 'Rwanda'
  });

  const bostonMarker = new google.maps.Marker({
    position: {lat: 42.3140089, lng: -71.2504676},
    map: map,
    title: 'Boston, MA'
  });

  const portlandMarker = new google.maps.Marker({
    position: {lat: 45.5428532, lng: -122.9338262},
    map: map,
    title: 'Portland, OR'
  });

  const sfMarker = new google.maps.Marker({
    position: {lat: 37.757889, lng: -122.577342},
    map: map,
    title: 'San Francisco, CA'
  });

  const nycMarker = new google.maps.Marker({
    position: {lat: 40.6971811, lng: -74.5387578},
    map: map,
    title: 'New York City, NY'
  });

  const phoenixMarker = new google.maps.Marker({
    position: {lat: 33.6056726, lng: -112.4045564},
    map: map,
    title: 'Phoenix, AZ'
  });

  const ugandaMarker = new google.maps.Marker({
    position: {lat: 1.3749742, lng: 27.8152225},
    map: map,
    title: 'Uganda'
  });

  const burundiMarker = new google.maps.Marker({
    position: {lat: -3.3861413, lng: 28.8065842},
    map: map,
    title: 'Burundi'
  });

  const mexicoMarker = new google.maps.Marker({
    position: {lat: 21.1216059, lng: -86.9889769},
    map: map,
    title: 'Cancún, Quintana Roo, Mexico'
  });

  const phillyMarker = new google.maps.Marker({
    position: {lat: 40.0026884, lng: -75.3975305},
    map: map,
    title: 'Philadelphia, PA'
  });

  const amstMarker = new google.maps.Marker({
    position: {lat: 52.3547554, lng: 4.6244656},
    map: map,
    title: 'Amsterdam, Netherlands'
  });
}

/**
 * Translates the poem to a selected language.
 */
function requestTranslation() {
  // Unhide the result section.
  document.getElementById('result').style.display = "initial";

  const numParagraph = 6;
  for(var i = 1; i <= numParagraph; i++) {
    const text = document.getElementById('p' + i.toString()).innerText;
    const languageCode = document.getElementById('language').value;

    // Display loading.. while the user waits for the translation.
    const resultContainer = document.getElementById('t' + i.toString());
    resultContainer.innerText = 'Loading...';

    const params = new URLSearchParams();
    params.append('text', text);
    params.append('languageCode', languageCode);

    fetch('/translates', {
      method: 'POST',
      body: params
    }).then(response => response.text())
    .then((translatedMessage) => {
      resultContainer.innerText = translatedMessage;
    });
  }
}