import math
import pandas as pd


def train_naive_bayes(D: pd.DataFrame, C: list, lex: pd.DataFrame=pd.DataFrame):
    # returns log P(c) and log P(w|c)

    print("|__Compiling all unique words...")
    D_split = D['Review'].apply(lambda s: s.split())
    if not lex.empty:
        new_vocab = pd.Series([lex['Word'].tolist()], name='Review')
        D_new_vocab = pd.concat([D_split, new_vocab], ignore_index=True)
    V = D_new_vocab.explode('Review').drop_duplicates()

    logprior = {}
    C_split = pd.DataFrame(columns=C)
    loglikelihood = pd.DataFrame(index=V.values, columns=C)

    print("|__Calculating loglikelihood of each word in every class...")
    for c in C: # Calculate P(c) terms
        Ndoc = D.shape[0]
        Nc = D[D['Label'] == c].shape[0]
        logprior[c] = math.log(Nc/Ndoc)
        C_split[c] = D_split[D['Label'] == c].reset_index(drop=True) # all documents in D with class c
        
        # Count total number of words with add-1 smoothing
        print("   |__Counting total number of words in class", c, "...")
        total = 0
        if not lex.empty:
            total += lex[lex['Label'] == c].shape[0]
        C_words = C_split[c].explode()
        total += C_words.shape[0]
        total += C_words.unique().shape[0] # Add-1 smoothingz
        
        # Calculate P(w|c) terms
        print("   |__Calculating probability of each word being in class", c, "...")
        all_counts = C_words.value_counts()
        C_lex = lex[lex['Label'] == c]['Word'].values

        C_word_probs = V.map(lambda w: word_likelihood(all_counts, C_lex, w, total))
        C_word_probs.index = V.values
        loglikelihood[c] = C_word_probs
        print(loglikelihood)
    return logprior, loglikelihood, V


def word_likelihood(all_counts: pd.Series, C_lex, w, total) -> float:
    count = 0
    if w in all_counts.index:
        count += all_counts[w]
    if w in C_lex:
        count += 1
    return math.log((count+1)/total)


def test_naive_bayes(testdoc: list, logprior: dict, loglikelihood: pd.DataFrame, C):
    # returns best c
    testdoc = pd.Series(testdoc)
    sum = {}

    for c in C:
        sum[c] = logprior[c] + testdoc.map(lambda w: word_score(w, loglikelihood, c)).sum()
    return max(sum, key=sum.get)


def word_score(w, loglikelihood: pd.DataFrame, c) -> float:
    if w in loglikelihood.index:
        return loglikelihood.loc[w][c]
    else:
        return 0


def main():
    lex_map = {'positive':'POS', 'negative':'NEG', 'neutral':'NEU', 'both':'both'}
    sent_map = {'__label__2':'POS', '__label__1':'NEG'}
    C = ['POS', 'NEG']

    print("Reading lexicon...")
    # Some editing needed to be done to the file to read properly
    lexicon = pd.read_csv("lexicon/subjclues.tff", sep=' ', header=None, names=['Type', 'Len', 'Word', 'Pos', 'Stemmed', 'PriorPolarity'])
    print("|__Parsing lexicon...")
    lexicon['Word'] = lexicon.apply(lambda row: row['Word'].replace('word1=',''), axis=1)
    lexicon['Label'] = lexicon.apply(lambda row: lex_map[row['PriorPolarity'].replace('priorpolarity=','')], axis=1)
    lexicon.drop(['Type', 'Len', 'Pos', 'Stemmed', 'PriorPolarity'], axis=1, inplace=True)
    print(lexicon)

    print("Reading training file...")
    train = pd.read_csv("Amazon Reviews/train.ft.txt", sep='\t', header=None, names=['Line'])
    print("|__Parsing training data...")
    train['Label'] = train.apply(lambda row: sent_map[row['Line'][:10]], axis=1)
    train['Review'] = train.apply(lambda row: row['Line'][11:].strip().replace('!','').replace('?','').replace(':','').replace('.','').replace(',','').lower(), axis=1)
    train.drop('Line', axis=1, inplace=True)
    print(train)

    print("Training...")
    (logprior, loglikelihood, V) = train_naive_bayes(train, C, lexicon)

    print("Reading testing file...")
    test = pd.read_csv("Amazon Reviews/test.ft.txt", sep='\t', header=None, names=['Line'])
    print("|__Parsing testing data...")
    test['Label'] = test.apply(lambda row: sent_map[row['Line'][:10]], axis=1)
    test['Review'] = test.apply(lambda row: row['Line'][11:].strip().replace('!','').replace('?','').replace(':','').replace('.','').replace(',','').lower(), axis=1)
    test.drop('Line', axis=1, inplace=True)
    print(test)
    
    print("Testing...")
    test['Review'] = test['Review'].apply(lambda s: s.split())
    test['Predict'] = test['Review'].apply(lambda row: test_naive_bayes(row, logprior, loglikelihood, C))

    actual_pos = test[test['Label'] == 'POS']
    actual_neg = test[test['Label'] == 'NEG']

    conf_mat = [[float(actual_pos[actual_pos['Predict'] == 'POS'].shape[0]),
                 float(actual_neg[actual_neg['Predict'] == 'POS'].shape[0])],
                [float(actual_pos[actual_pos['Predict'] == 'NEG'].shape[0]),
                 float(actual_neg[actual_neg['Predict'] == 'NEG'].shape[0])]]

    for row in conf_mat:
        print(row[0], " ", row[1])

    total = float(test.shape[0])
    accuracy = (conf_mat[0][0] + conf_mat[1][1]) / total
    precision = conf_mat[0][0] / (conf_mat[0][0] + conf_mat[0][1])
    recall = conf_mat[0][0] / (conf_mat[0][0] + conf_mat[1][0])
    f_measure = (2 * precision * recall) / (precision + recall)
    print("Accuracy:", accuracy)
    print("Precision:", precision)
    print("Recall:", recall)
    print("F-measure:", f_measure)

    print()


if __name__ == "__main__":
    main()
