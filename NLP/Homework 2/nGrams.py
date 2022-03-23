import pandas as pd
import numpy as np


def bigrams(corpus: list[str]) -> pd.DataFrame:
    counts = None
    totalWords = 0
    
    for line in corpus:
        lineWords = line.lower().replace(",","").split()

        totalWords += 1
        if counts is None:
            counts = pd.DataFrame(data=[1], columns=["<s>"])
        else:
            counts["<s>"] += 1
        
        for word in lineWords:
            totalWords += 1
            if word in counts.columns:
                counts[word] += 1
            else:
                counts[word] = 1
        
        totalWords += 1
        if "</s>" in counts.columns:
            counts["</s>"] += 1
        else:
            counts["</s>"] = 1
    
    uni = counts/totalWords
    print("Unigrams:")
    print(uni)
    
    bg = pd.DataFrame(np.zeros((len(counts.columns), len(counts.columns))), columns=counts.columns)
    for row in range(len(counts.columns)):
        startWord = counts.columns[row]
        for line in corpus:
            lineWords = line.lower().replace(",","").split()
            if startWord == "<s>":
                bg.iloc[row][lineWords[0]] += 1
            else:
                for i in range(len(lineWords)):
                    if lineWords[i] == startWord:
                        if i == len(lineWords) - 1:
                            bg.iloc[row]["</s>"] += 1
                        else:
                            bg.iloc[row][lineWords[i+1]] += 1
    
    for row in range(len(bg)):
        word = counts.columns[row]
        count = counts[word][0]
        bg.loc[row,] = bg.loc[row,]/count
    
    return bg


def generateSentence(bigrams: pd.DataFrame) -> str:
    words = bigrams.columns
    sentence = "<s> "
    nextWord = bigrams.loc[0, bigrams.loc[0,] > 0].sample().index.values[0]
    while nextWord != "</s>":
        sentence += nextWord + " "
        row = words.get_loc(nextWord)
        nextWord = bigrams.loc[row, bigrams.loc[row,] > 0].sample().index.values[0]
    sentence += "</s>"
    return sentence


def main():
    # with open("rickroll.txt") as f:
    with open("allstar.txt") as f:
        lines = f.readlines()
    
    bg = bigrams(lines)
    print("Bigrams:")
    print(bg)
    print("Generated Sentence:")
    print(generateSentence(bg))


if __name__ == "__main__":
    main()