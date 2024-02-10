package com.tutorial.quizapp


fun getRandomIndices(k: Int, n: Int): List<Int> {
    return (0 until n).shuffled().take(k)
}

val questions = listOf<String>(
    "What is the capital city of France?",
    "Who wrote the play \"Hamlet\"?",
    "What is the chemical symbol for water?",
    "Which planet is known as the \"Red Planet\"?",
    "What is the largest mammal?",
    "Who painted the Mona Lisa?",
    "What is the powerhouse of the cell?",
    "Which of the following is not a primary color?",
    "What is the tallest mountain in the world?",
    "Who is known as the father of computing?",
    "Which gas do plants use for photosynthesis?",
    "What year did the Titanic sink?",
    "What is the chemical symbol for gold?",
    "Which country is famous for the tulip festival?",
    "Who developed the theory of relativity?",
    "What is the currency of Japan?",
    "What is the fastest land animal?",
    "Which ocean is the largest?",
    "What is the hardest natural substance on Earth?",
    "What is the process of a caterpillar turning into a butterfly called?"
)

val choicesA = listOf<String>(
    "London",
    "William Shakespeare",
    "H2O",
    "Jupiter",
    "Elephant",
    "Leonardo da Vinci",
    "Nucleus",
    "Green",
    "Mount Everest",
    "Alan Turing",
    "Oxygen",
    "1912",
    "Au",
    "Netherlands",
    "Isaac Newton",
    "Yen",
    "Cheetah",
    "Pacific Ocean",
    "Diamond",
    "Metamorphosis"
)

val choicesB = listOf<String>(
    "Paris",
    "Charles Dickens",
    "CO2",
    "Mars",
    "Whale",
    "Pablo Picasso",
    "Mitochondria",
    "Red",
    "K2",
    "Charles Babbage",
    "Carbon Dioxide",
    "1915",
    "Ag",
    "Nepal",
    "Albert Einstein",
    "Dollar",
    "Lion",
    "Atlantic Ocean",
    "Graphite",
    "Pollination"
)

val choicesC = listOf<String>(
    "Rome",
    "Jane Austen",
    "NaCl",
    "Saturn",
    "Giraffe",
    "Vincent van Gogh",
    "Ribosome",
    "Blue",
    "Kangchenjunga",
    "Steve Jobs",
    "Nitrogen",
    "1917",
    "Pt",
    "Turkey",
    "Stephen Hawking",
    "Euro",
    "Leopard",
    "Indian Ocean",
    "Quartz",
    "Panspermia"
)

val answers = listOf<Int>(
    2, 1, 1, 2, 2, 1, 2, 1, 1, 2, 2, 1, 1, 1, 2, 1, 1, 1, 2, 1
)

