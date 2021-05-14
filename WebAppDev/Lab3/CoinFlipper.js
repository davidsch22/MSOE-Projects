// SE2840 Winter 2021 - Lab 3 JS Coin Flipper DOM
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

		}
	}

	onGoClicked = () => {
		this.#numberOfCoins = document.getElementById("ncoins").value;
		this.#numberOfRepetitions = document.getElementById("nflips").value;

		this.#frequency = [];  // allocate the array per user-specified input (note: Java initializes the contents to 0).

		if (!this.#checkInputs(this.#numberOfCoins, this.#numberOfRepetitions)) {
			return;
		}

		let executionTime = performance.now(); // current system time snapshot
		this.#flipCoins(); // flip NUM_OF_COINS coins NUM_OF_REPS times
		executionTime = performance.now() - executionTime; // compute elapsed time

		// NOTE: Do not include histogram generation in execution time calculation - console I/O takes a LONG time compared to internal math computations
		this.#printHistogram(); // display the resulting histogram

		document.getElementById("time").textContent = "Elapsed Time: " + executionTime + "ms";
		document.getElementById("time").hidden = false;
	}

	#checkInputs = (nCoins, nFlips) => {
		let nCoinsError = "";
		let nFlipsError = "";

		if (nCoins === "") {
			nCoinsError = "Input value \'" + nCoins + "\' is not valid. No input given.";
		}
		else if (isNaN(nCoins) || !Number.isInteger(parseFloat(nCoins))) {
			nCoinsError = "Input value \'" + nCoins + "\' is not valid. Input must be an integer.";
		}
		else if (nCoins < 1 || nCoins > 10) {
			nCoinsError = "Input value \'" + nCoins + "\' is not valid. Input must be >= 1 and <= 10.";
		}

		if (nFlips === "") {
			nFlipsError = "Input value \'" + nFlips + "\' is not valid. No input given.";
		}
		else if (isNaN(nFlips) || !Number.isInteger(parseFloat(nFlips))) {
			nFlipsError = "Input value \'" + nFlips + "\' is not valid. Input must be an integer.";
		}
		else if (nFlips < 1 || nFlips > 1000000) {
			nFlipsError = "Input value \'" + nFlips + "\' is not valid. Input must be >= 1 and <= 1,000,000.";
		}

		if (nCoinsError !== "") {
			document.getElementById("coinsError").textContent = nCoinsError;
			document.getElementById("coinsError").hidden = false;
		} else {
			document.getElementById("coinsError").hidden = true;
		}
		if (nFlipsError !== "") {
			document.getElementById("flipsError").textContent = nFlipsError;
			document.getElementById("flipsError").hidden = false;
		} else {
			document.getElementById("flipsError").hidden = true;
		}

		return nCoinsError === "" && nFlipsError === "";
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
		// This loop prints the histogram. Each iteration prints one histogram bar.
		let highestFrequency = Math.max.apply(Math, this.#frequency);
		for (let heads = 0; heads <= this.#numberOfCoins; heads++) {
			document.getElementById(heads+"s").textContent = this.#frequency[heads];
			document.getElementById(heads+"meter").max = highestFrequency;
			document.getElementById(heads+"meter").value = this.#frequency[heads];
		}
		document.getElementById("table").hidden = false;
	}
}