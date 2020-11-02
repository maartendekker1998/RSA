package cyber.rsa;

import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

class RSA {

    private static int bitLength = 2048;

    //de priemgetallen
    private static BigInteger p;
    private static BigInteger q;
    //de modulus
    private static BigInteger n;
    //phi
    private static BigInteger phi;
    //encryption exponent
    private static BigInteger e;
    //decryption exponent
    private static BigInteger d;

    public static void main(String[] args) {

        Scanner userInput = new Scanner(System.in);

        // 1. Zoek twee priemgetallen
        p = generateLargePrime(bitLength);
        q = generateLargePrime(bitLength);

        // 2. n = (p*q)
        n = p.multiply(q);

        // 3. Phi(n) = (p-1)*(q-1)
        phi = getPhi(p, q);

        // 4. Zoek een e die voldoet aan  1 < e < Phi(n)    en      gcd(e,Phi) = 1
        e = genE(phi);

        // 5. Bereken d where  d ≡ e^(-1) (mod Phi(n))
        // makkelijke oplossing door middel van BigInteger functionaliteit.
        d = e.modInverse(phi);

        // Print RSA variabelen
        System.out.println();
        System.out.println("RSA variabelen : ");
        System.out.println();
        System.out.println("p: " + p);
        System.out.println();
        System.out.println("q: " + q);
        System.out.println();
        System.out.println("n: " + n);
        System.out.println();
        System.out.println("Phi: " + phi);
        System.out.println();
        System.out.println("e : " + e);
        System.out.println();
        System.out.println("d : " + d);
        System.out.println();

        // encryption / decryption example
        System.out.println("what message do you want to encrypt with RSA-2048");
        String message = userInput.nextLine();

        BigInteger encoded = encodeMessage(message);
        BigInteger encrypted = encrypt(encoded, e, n);
        BigInteger decrypted = decrypt(encrypted, d, n);
        String decoded = decodeMessage(decrypted);

        System.out.println();
        System.out.println("Original message: " + message);
        System.out.println();
        System.out.println("Encoded: " + encoded);
        System.out.println();
        System.out.println("Encrypted: " + encrypted);
        System.out.println();
        System.out.println("Decrypted: " + decrypted);
        System.out.println();
        System.out.println("Decoded: " + decoded);
    }


    public static BigInteger encodeMessage(String message) {
        byte[] bytearray = message.getBytes(UTF_8);
        BigInteger val = new BigInteger(bytearray);
        return val;
    }


    public static String decodeMessage(BigInteger message) {
        return new String( message.toByteArray(), UTF_8);
    }


    public static BigInteger getPhi(BigInteger p, BigInteger q) {
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        return phi;
    }

    //Genereer een random groot priemgetal gebaseerd op bitlengte

    public static BigInteger generateLargePrime(int bits) {
        Random randomInteger = new Random();
        BigInteger largePrime = BigInteger.probablePrime(bits, randomInteger);
        return largePrime;
    }


    ///Recursieve implementatie van Euclidian algoritme om gcd te vinden

    public static BigInteger gcd(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO)) {
            return a;
        } else {
            return gcd(b, a.mod(b));
        }
    }


    //Zoek e die groter is dan 1 en kleiner is dan phi, deze e moet ook reatief priem zijn aan phi
    public static BigInteger genE(BigInteger phi) {
        Random rand = new Random();
        BigInteger e;
        do {
            e = new BigInteger(2048, rand);
            //als e groter is dan phi, zoek een nieuwe e
            while (e.min(phi).equals(phi)) {
                e = new BigInteger(2048, rand);
            }
        }
        // check of e en phi relatief priem zijn
        while (!gcd(e, phi).equals(BigInteger.ONE));
        return e;
    }

    public static BigInteger encrypt(BigInteger message, BigInteger e, BigInteger n) {
        return message.modPow(e, n);
    }

    public static BigInteger decrypt(BigInteger message, BigInteger d, BigInteger n) {
        return message.modPow(d, n);
    }
}
