// SE2840 Winter 2021 - Lab 2 JS Coin Flipper
// Name: David Schulz
// Class Section: 041

// Tell the code inspection tool that we're writing ES6 compliant code:
// jshint esversion: 6
// Tell the code inspection tool that we're using "developer" classes (console, alert, etc)
// jshint devel:true
// See https://jshint.com/docs/ for more JSHint directives
// jshint unused:false

class CoinFlipper {
	#numberOfCoins;
	#numberOfRepetitions;
	#frequency;

	constructor() {
		this.init = () => {
			console.clear();

			this.#numberOfCoins = prompt("Enter the number of coins to be flipped: ", "0");
			this.#numberOfRepetitions = prompt("Enter the number of flips: ", "0");
			this.#frequency = [];  // allocate the array per user-specified input (note: Java initializes the contents to 0).

			if (isNaN(this.#numberOfCoins) || isNaN(this.#numberOfRepetitions)) {
				alert("ERROR: Values cannot be empty.")
				return;
			}
			else if (!Number.isInteger(parseFloat(this.#numberOfCoins)) || !Number.isInteger(parseFloat(this.#numberOfRepetitions))) {
				alert("ERROR: Values must be integers.");
				return;
			}
			else if (this.#numberOfCoins <= 0 || this.#numberOfRepetitions <= 0) {
				alert("ERROR: Values cannot be less than 1.");
				return;
			}
			else if (this.#numberOfCoins > 10 || this.#numberOfRepetitions > 1000000) {
				alert("ERROR: Number of coins can't be greater than 10 and number of repetitions can't be greater than 1 million.");
				return;
			}

			let executionTime = performance.now(); // current system time snapshot
			this.#flipCoins(); // flip NUM_OF_COINS coins NUM_OF_REPS times
			executionTime = performance.now() - executionTime; // compute elapsed time

			// NOTE: Do not include histogram generation in execution time calculation - console I/O takes a LONG time compared to internal math computations
			this.#printHistogram(); // display the resulting histogram

			console.log("Coin Flipper Time: " + executionTime + "ms");
		}
	}

	#flipCoins() {
		// This loop fills up the frequency bins. Each iteration simulates one group of numCoins coin flips.
		// For example, with a group of flips of 3 coins, heads may come up 0, 1, 2, or 3 times.
		for (let rep = 0; rep < this.#numberOfRepetitions; rep++) {
			// perform a single flip of NUM_OF_COINS coins
			let heads = this.#doSingleFlip();
			if (this.#frequency[heads] == null) {
				this.#frequency[heads] = 1;
			} else {
				this.#frequency[heads]++; // update appropriate bin
			}
		}
	}

	#doSingleFlip() {
		let heads = 0;
		for (let i = 0; i < this.#numberOfCoins; i++) { // flip each coin
			heads += Math.floor(Math.random() * 2); // computed random int value is either 0 or 1 (tails or heads)
		}
		return heads; // number of heads that came up
	}

	#printHistogram() {
		console.log("Number of times each head count occurred in " + this.#numberOfRepetitions + " flips of " + this.#numberOfCoins + " coins:");

		// This loop prints the histogram. Each iteration prints one histogram bar.
		for (let heads = 0; heads <= this.#numberOfCoins; heads++) {
			let barString = " " + heads + "  " + this.#frequency[heads] + "  ";
			let fractionOfReps = this.#frequency[heads] / this.#numberOfRepetitions;
			let numOfAsterisks = Math.floor(Math.round(fractionOfReps * 100));

			for (let i = 0; i < numOfAsterisks; i++) {
				barString += "*";
			}
			console.log(barString);
		}
	}
}