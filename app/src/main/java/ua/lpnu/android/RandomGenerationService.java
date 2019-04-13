package ua.lpnu.android;

import java.util.Random;

public class RandomGenerationService {
    private Random random;

    public RandomGenerationService(int seed) {
        random = new Random(seed);
    }

    public RandomGenerationService() {
        random = new Random((int) System.currentTimeMillis());
    }

    public String next(int length) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < length; i++) {
            res.append((char) (random.nextInt(26) + 'a'));
        }
        return res.toString();
    }

    public String next() {
        int length = 3 + random.nextInt(12);
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < length; i++) {
            res.append((char) (random.nextInt(26) + 'a'));
        }
        return res.toString();
    }
}
