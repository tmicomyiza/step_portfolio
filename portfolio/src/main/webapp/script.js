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
 * 
 */

(function (){
    var quizContainer = document.getElementById('quiz');
    //var resultsContainer = document.getElementById('results');
    var submitButton = document.getElementById('submit');
    const Questions = [
        {
            question: "what is true about me?",
            answers: {
                a: 'I am from Ghana',
                b: 'I love to dance',
                c: 'I am a good singer',
                d: 'I have a cat'
            },
            correctAnswer: 'b'
        },
        {
            question: "what is NOT true about me?",
            answers: {
                a: 'I am from Rwanda',
                b: 'I love to paint',
                c: 'I write poems',
                d: 'I study applied mathematics'
            },
            correctAnswer: 'd'
        },
        {
            question: "what is my least favorite genre of music",
            answers: {
                a: 'Afrobeat',
                b: 'Hip Hop',
                c: 'Dancehall',
                d: 'Country',
                e: 'House',
                f: 'Jazz'
            },
            correctAnswer: 'e'
        },
    ];

    DisplayQuestions(Questions, quizContainer);

    //show results
    submitButton.onclick = function (){
        DisplayAnswers(quizContainer, questions)
    }
})();

/**
 * Adds questions to the page
 */
function DisplayQuestions(Quizcontainer, questions){
    var outputs = [];
    var answers;

    for(Aquestion in questions){
        answers = [];

        for (option in Aquestion.answers){
            
            //add an html button
            answers.push(
            	'<label>'
					+ '<input type="radio" name="question'+i+'" value="'+option+'">'
					+ option + ': '
					+ Aquestion.answers[option]
				+ '</label>'
			);
	
        }

        // add the question to outputs
        outputs.push(	'<div class="question">' + Aquestion.question + '</div>'
			+ '<div class="answers">' + answers.join('') + '</div>'
		);
    }

    // add quetions 
    QuizContainer.innerHTML = outputs.join('');
}

/**
 * Adds solutions to the page
 */

function DisplayAnswers(QuizContainer, questions){
    var solutionContainer = QuizContainer.querySelectorAll('.answers');
    
    var userSelection = '';
    
    for(var i = 0; i < questions.length; i++){
        userSelection = (solutionContainer[i].querySelector('input[name=quetion'+i+']:checked')||{}).value;

        if(userSelection===questions[i].correctAnswer){
            solutionContainer[i].style.color = 'lightgreen';
        }
        else{
            solutionContainer[i].style.color = 'red';
        }
    }
}


