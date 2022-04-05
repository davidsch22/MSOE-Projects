import math
import pandas as pd


def train_naive_bayes(D: pd.DataFrame, C: list):
    # returns log P(c) and log P(w|c)
    # train['Sentence'] = train['Sentence'].apply(lambda s: )

    D_words = D['Sentence'].apply(lambda s: s.split())
    V = list(set().union(*D_words.tolist()))

    logprior = {}
    bigdoc = {}
    loglikelihood = pd.DataFrame(index=V, columns=C)

    for c in C: # Calculate P(c) terms
        Ndoc = len(D)
        Nc = len(D[D['Label'] == c])
        logprior[c] = math.log(Nc/Ndoc)
        bigdoc[c] = D_words[D['Label'] == c].tolist() # all d in D with class c
        total = 0
        for w in V: # Count total number of words with add-1 smoothing
            total += sum(x.count(w) for x in bigdoc[c]) + 1
        for w in V: # Calculate P(w|c) terms
            count = sum(x.count(w) for x in bigdoc[c]) # number of occurrences of w in bigdoc[c]
            loglikelihood.loc[w][c] = math.log((count + 1)/total)
    return logprior, loglikelihood, V


def test_naive_bayes(testdoc: list, logprior: dict, loglikelihood: pd.DataFrame, C: list, V: list):
    # returns best c
    sum = {}

    for c in C:
        sum[c] = logprior[c]
        for word in testdoc:
            if word in V:
                sum[c] = sum[c] + loglikelihood.loc[word][c]
    return max(sum, key=sum.get)


def main():
    train = pd.DataFrame(columns=['Sentence', 'Label'])
    test = []

    with open('trainingSet.txt') as f:
        line = f.readline()
        while line:
            label = line[:3]
            sentence = line[4:].strip().strip(".!?").replace(',', '').lower()
            new_row = pd.DataFrame({'Sentence': [sentence], 'Label': [label]})
            train = pd.concat([train, new_row], ignore_index=True)
            line = f.readline()

    with open('testSet.txt') as f:
        line = f.readline()
        while line:
            line = line.strip().strip(".!?").replace(',', '').lower()
            test.append(line)
            line = f.readline()
    
    C = ['POS', 'NEG', 'NEU']

    (logprior, loglikelihood, V) = train_naive_bayes(train, C)
    for line in test:
        print(line, ":", test_naive_bayes(line.split(), logprior, loglikelihood, C, V))


if __name__ == "__main__":
    main()
