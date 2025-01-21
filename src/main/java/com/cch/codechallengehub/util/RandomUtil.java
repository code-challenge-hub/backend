package com.cch.codechallengehub.util;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtil {

    public static int getRandomIntWithinBound(int bound){
        return ThreadLocalRandom.current().nextInt(bound);
    }

    public static String getRandomStringWithLength(int length){
        StringBuilder key = new StringBuilder();
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(3);
            switch (index) {
                case 0 -> key.append((char) (random.nextInt(26) + 65)); // A-Z
                case 1 -> key.append((char) (random.nextInt(26) + 97)); // a-z
                case 2 -> key.append(random.nextInt(10));              // 0-9
            }
        }
        return key.toString();
    }


}
