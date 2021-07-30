# Variable Byte (VB) encoding


Inverted Lists (also referred to as postings file or inverted file) is an index data structure associated with a key word(term) w , 
```storing a set of document identifiers```, which contain w . Its purpose is to allow fast full text searches, at a cost of increased
processing when a document is added to the database.

An exaple of a inverted lists containing the terms BRUTUS, CAESAR, CALPURNIA is given below.

```BRUTUS``` &rarr; ```1``` ,```2```,```4```,```11```,```31```,```45```,```173```,```174```

```CAESAR``` &rarr; ```1```,```2```,```4```,```5```,```6```,```16```,```57```,```132```,```...```


```CALPURNIA``` &rarr; ```2```,```31```,```54```, ```101```

In order to have  a more efficient representation of the postings file, we store the gaps between postings because in general they are short and require a lot less space 
than 20 bits to store.

For example, if we have the docID ```824``` and the next one is ```829```, we can store the gap between those postings which is ```5```. So instead of storing ```1100111000```
(824) and ```1100111101``` (829), we store ```1100111000``` (824) and the gap ```101``` (5).

Gaps for the most frequent terms such as ```the``` ```and``` ```for``` are mostly equal to 1. But the gaps for a rare term that occurs only once or twice in a collection 
have the same order of magnitude as the docIDs. For an economical representation of this distribution of gaps, we need a variable encoding method that uses fewer bits 
for short gaps.

It’s a very simple idea: use the last 7 bits in every 8 bit byte to store the integer, and use the first bit of the byte (continuation bit) to indicate whether this 
is the last byte that stores the integer or whether another byte follows.It is set to 1 for the last byte of the encoded gap and to 0 otherwise.To decode a variable
byte code, we read a sequence of bytes with continuation bit 0 terminated by a byte with continuation bit 1. We then extract and concatenate the 7-bit parts.

Suppose we want to store the number (gap) ```214577```. In binary, it’d be represented as ```110100011000110001```. With the variable byte scheme, we take the first 
(lowest) seven bits (```0110001```) and add the coninuation bit 1 in the beginning, and we get ```10110001```. We then take the next 7 bits (```0001100```) and add the
coninuation bit 0 in the beginning, and we get ```00001100```. We then take the remaining four bits of the binary value (1101), pad it out to be seven bits, and add the 
coninuation bit 0 in the beginning.So, all up, we’ve got ```000011010000110010110001```
