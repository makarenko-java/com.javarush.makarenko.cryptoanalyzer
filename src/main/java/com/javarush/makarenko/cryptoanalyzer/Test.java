package com.javarush.makarenko.cryptoanalyzer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Test {

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

    private static final Set<Character> punctuationSpaceAfter = new HashSet<>();
    private static final Set<Character> punctuationSpaceBefore = new HashSet<>();

    static {
        for (char character : ".,!?:;)]}»'\"".toCharArray()) {
            if (alphabetPunctuation.contains(character)) punctuationSpaceAfter.add(character);
        }
        for (char character : "([{«'\"".toCharArray()) {
            if (alphabetPunctuation.contains(character)) punctuationSpaceBefore.add(character);
        }
    }


    public static void main(String[] args) {
        System.out.println(punctuationSpaceAfter.toString());
        System.out.println(punctuationSpaceBefore.toString());



        int[] shiftArray = new int[shiftRange];
        // заполнение массива возможных сдвигов
        for (int i = 0; i < shiftRange; i++) {
            shiftArray[i] = i + 1;
        }

        System.out.println(Arrays.toString(shiftArray));
//        char[] ALPHABET = {'а', 'б', 'в', 'г', 'д', 'е', 'ж', 'з',
//                'и','к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ',
//                'ъ', 'ы', 'ь', 'э', 'я', '.', ',', '«', '»', '"', '\'', ':', '!', '?', ' '};
//        System.out.println(ALPHABET.length);
//        System.out.println(ALPHABET.length-1);
//        System.out.println(Character.isUpperCase('.'));
//
//
//
//        Set<Character> punctuation = new HashSet<>();
//
//        for (char character : ALPHABET) {
//            if (!Character.isLetter(character) && character != ' ') punctuation.add(character);
//        }
//        for (Character punctuationMark : punctuation) {
//            System.out.print(punctuationMark);
//        }

    }
}
