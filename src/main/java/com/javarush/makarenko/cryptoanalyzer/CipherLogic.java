package com.javarush.makarenko.cryptoanalyzer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class CipherLogic {

    private static final char[] ALPHABET = {'а', 'б', 'в', 'г', 'д', 'е', 'ж', 'з',
            'и','к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ',
            'ъ', 'ы', 'ь', 'э', 'я', '.', ',', '«', '»', '"', '\'', ':', '!', '?', ' '};
    public static final int shiftRange = ALPHABET.length - 1;

    private static final HashMap<Character, Integer> alphabetKeyCharValueInt = new HashMap<>();
    private static final HashMap<Integer, Character> alphabetKeyIntValueChar = new HashMap<>();
    private static final Set<Character> alphabetPunctuation = new HashSet<>();

    static {
        for (int i = 0; i < ALPHABET.length; i++) {
            char character = ALPHABET[i];
            alphabetKeyCharValueInt.put(character, i);
            alphabetKeyCharValueInt.put(Character.toUpperCase(character), i);

            alphabetKeyIntValueChar.put(i, character);
            if (Character.isLetter(character)) {
                alphabetKeyIntValueChar.put(i + ALPHABET.length, Character.toUpperCase(character));
            }
            else {
                alphabetPunctuation.add(character);
            }
        }
    }

    private static final Set<Character> punctuationBeforeSpace = new HashSet<>();
    private static final Set<Character> punctuationAfterSpace = new HashSet<>();

    static {
        for (char character : ".,!?:;)]}»'\"".toCharArray()) {
            //if (alphabetPunctuation.contains(character)) punctuationBeforeSpace.add(character);
            punctuationBeforeSpace.add(character);
        }
        for (char character : "([{«'\"".toCharArray()) {
            //if (alphabetPunctuation.contains(character)) punctuationAfterSpace.add(character);
            punctuationAfterSpace.add(character);
        }
    }



    private String inputText;
    private int shift;
    private String[] bruteForceTextResult;

    public CipherLogic(String inputText, int shift) {
        this.inputText = inputText;
        this.shift = shift;
    }

    public CipherLogic(String inputText, String[] bruteForceTextResult) {
        this.inputText = inputText;
        this.bruteForceTextResult = bruteForceTextResult;
    }




    public String encrypt() {
        int index;
        int indexShifted;
        StringBuilder outputText = new StringBuilder();

        for (char character : inputText.toCharArray()) {
            if (alphabetKeyCharValueInt.containsKey(character)) {
                index = alphabetKeyCharValueInt.get(character);

                if (Character.isUpperCase(character)) {
                    indexShifted = ALPHABET.length + (index + shift) % ALPHABET.length;
                    outputText.append(alphabetKeyIntValueChar.get(indexShifted));
                } else {
                    indexShifted = (index + shift) % ALPHABET.length;
                    outputText.append(alphabetKeyIntValueChar.get(indexShifted));
                }
            } else {
                outputText.append(character);
            }
        }
        return outputText.toString();
    }





    public String decrypt() {
        int indexShifted;
        int index;
        StringBuilder outputText = new StringBuilder();

        for (char character : inputText.toCharArray()) {
            if (alphabetKeyCharValueInt.containsKey(character)) {
                indexShifted = alphabetKeyCharValueInt.get(character);

                if (Character.isUpperCase(character)) {
                    index = ALPHABET.length + (indexShifted - shift + ALPHABET.length) % ALPHABET.length;
                    outputText.append(alphabetKeyIntValueChar.get(index));
                } else {
                    index = (indexShifted - shift + ALPHABET.length) % ALPHABET.length;
                    outputText.append(alphabetKeyIntValueChar.get(index));
                }
            } else {
                outputText.append(character);
            }
        }
        return outputText.toString();
    }





    public int bruteForce() {
        int indexShifted;
        int index;

        StringBuilder outputText;
        char[] inputTextArray = inputText.toCharArray();
        String[] outputTextArray = new String[shiftRange];
        int[] score = new int[shiftRange];

        int[] shiftArray = new int[shiftRange];
        // заполнение массива возможных сдвигов
        for (int i = 0; i < shiftRange; i++) {
            shiftArray[i] = i + 1;
        }

        for (int i = 0; i < shiftRange; i++) {
            shift = shiftArray[i];
            score[i] = 0;
            outputText = new StringBuilder();

            for (int j = 0; j < inputTextArray.length; j++) {
                char character = inputTextArray[j];

                if (alphabetKeyCharValueInt.containsKey(character)) {
                    indexShifted = alphabetKeyCharValueInt.get(character);

                    if (Character.isUpperCase(character)) {
                        index = ALPHABET.length + (indexShifted - shift + ALPHABET.length) % ALPHABET.length;
                        outputText.append(alphabetKeyIntValueChar.get(index));
                    } else {
                        index = (indexShifted - shift + ALPHABET.length) % ALPHABET.length;
                        outputText.append(alphabetKeyIntValueChar.get(index));
                    }
                } else {
                    outputText.append(character);
                }
            }

            // запись варианта расшифрованного текста со сдвигом shift[i] в массив output[i]
            outputTextArray[i] = outputText.toString();


            for (int j = 1; j < outputTextArray[i].length() - 1; j++) {
                char character = outputTextArray[i].charAt(j);
                // проходимся по тексту и проверяем символы после пробела и перед пробелом
                if (character == ' ') {
                    // проверяем символ перед пробелом
                    if (punctuationBeforeSpace.contains(outputTextArray[i].charAt(j - 1))) {
                        score[i]++;
                    }
                    // проверяем символ после пробела
                    if (punctuationAfterSpace.contains(outputTextArray[i].charAt(j + 1))) {
                        score[i]++;
                    }
                }
                // проходимся по тексту и проверяем пробел после !?., и если нет делаем --
                if (character == '!' || character == '?' || character == '.' || character == ',' || character == ':' || character == ';') {
                    if (!(outputTextArray[i].charAt(j+1) == ' ')) {
                        score[i]--;
                    }
                }
            }
        }

        int scoreMaxIndex = 0;
        for (int i = 1; i < score.length; i++) {
            if (score[i] > score[scoreMaxIndex]) {
                scoreMaxIndex = i;
            }
        }

        for (int i = 0; i < outputTextArray.length; i++) {
            bruteForceTextResult[i] = outputTextArray[i];
        }

        return scoreMaxIndex;
    }












    //char[] analysisChar = new char[] {'о','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','',''};
    //double[] analysisFreqency = new double[] {10.97,8.45,8.01,7.35,6.7,6.26,5.47,};
//    о	1	10,97%
//    е	2	8,45%
//    а	3	8,01%
//    и	4	7,35%
//    н	5	6,70%
//    т	6	6,26%
//    с	7	5,47%
//    р	8	4,73%
//    в	9	4,54%
//    л	10	4,40%
//    к	11	3,49%
//    м	12	3,21%
//    д	13	2,98%
//    п	14	2,81%
//    у	15	2,62%
//    я	16	2,01%
//    ы	17	1,90%
//    ь	18	1,74%
//    г	19	1,70%
//    з	20	1,65%
//    б	21	1,59%
//    ч	22	1,44%
//    й	23	1,21%
//    х	24	0,97%
//    ж	25	0,94%
//    ш	26	0,73%
//    ю	27	0,64%
//    ц	28	0,48%
//    щ	29	0,36%
//    э	30	0,32%
//    ф	31	0,26%
//    ъ	32	0,04%
//    ё	33	 0,04%



}
