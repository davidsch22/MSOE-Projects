import math
import pandas as pd


def train_naive_bayes(D: pd.DataFrame, C: list, lex: pd.DataFrame=pd.DataFrame):
    # returns log P(c) and log P(w|c)

    D_words = D['Sentence'].apply(lambda s: s.split())
    if not lex.empty:
        new_vocab = pd.Series([lex['Word'].tolist()], name='Sentence')
        D_new_vocab = pd.concat([D_words, new_vocab], ignore_index=True)
    V = list(set().union(*D_new_vocab.tolist()))

    logprior = {}
    bigdoc = {}
    loglikelihood = pd.DataFrame(index=V, columns=C)

    for c in C: # Calculate P(c) terms
        Ndoc = len(D)
        Nc = len(D[D['Label'] == c])
        logprior[c] = math.log(Nc/Ndoc)
        bigdoc[c] = D_words[D['Label'] == c].tolist() # all d in D with class c
        total = 0
        if not lex.empty:
            total += lex.shape[0]
        for w in V: # Count total number of words with add-1 smoothing
            total += sum(x.count(w) for x in bigdoc[c]) + 1
        for w in V: # Calculate P(w|c) terms
            count = sum(x.count(w) for x in bigdoc[c]) # number of occurrences of w in bigdoc[c]
            if not lex.empty and w in lex[lex['Label'] == c]['Word'].unique():
                count += 1
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
    lexicon = pd.DataFrame(columns=['Word', 'Label'])
    train = pd.DataFrame(columns=['Sentence', 'Label'])
    test = []

    with open('lexicon/subjclues.tff') as f:
        line = f.readline()
        while line:
            details = line.split()
            word = details[2].replace('word1=','')
            label = details[5].replace('priorpolarity=','')
            sent_map = {'positive':'POS', 'negative':'NEG', 'neutral':'NEU', 'both':'both', 'polarity=negative':'NEG'}
            label = sent_map[label]
            new_word = pd.DataFrame({'Word': [word], 'Label': [label]})
            lexicon = pd.concat([lexicon, new_word], ignore_index=True)
            line = f.readline()

    with open('newTrainSet.txt') as f:
        line = f.readline()
        while line:
            label = line[:3]
            sentence = line[4:].strip().strip("!?").replace('.','').replace(',','').lower()
            new_row = pd.DataFrame({'Sentence': [sentence], 'Label': [label]})
            train = pd.concat([train, new_row], ignore_index=True)
            line = f.readline()

    with open('testSet.txt') as f:
        line = f.readline()
        while line:
            line = line.strip().strip("!?").replace('.','').replace(',','').lower()
            test.append(line)
            line = f.readline()
    
    C = ['POS', 'NEG', 'NEU']

    (logprior, loglikelihood, V) = train_naive_bayes(train, C, lexicon)
    print(logprior, '\n')
    print(loglikelihood.loc['great'][:], '\n')
    print(loglikelihood.loc['software'][:], '\n')
    print(loglikelihood.loc['sucks'][:], '\n')
    for line in test:
        print(line, ":", test_naive_bayes(line.split(), logprior, loglikelihood, C, V))
    print()


if __name__ == "__main__":
    main()
