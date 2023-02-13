import java.util.*;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static void main(String[] args) {
        int cardSize = 100;

        for (int i = 0; i < cardSize; i++) {
            new Thread(() -> {

                int count = 0;
                String direction = generateRoute("RLRFR", 100);
                for (int j = 0; j < direction.length(); j++) {
                    if ('R' == direction.charAt(j)) {
                        count++;
                    }
                }
                synchronized (sizeToFreq) {
                    if (sizeToFreq.containsKey(count)) {
                        int numberOfAppearances = sizeToFreq.get(count) + 1;
                        sizeToFreq.put(count, numberOfAppearances);
                    } else {
                        sizeToFreq.put(count, 1);
                    }
                    sizeToFreq.notify();
                }
            }).start();
        }

        new Thread(() -> {
            synchronized (sizeToFreq) {
                int sumSizeToFreqValues = 0;
                for (float sizeToFreqValues : sizeToFreq.values()) {
                    sumSizeToFreqValues += sizeToFreqValues;
                }
                if (sumSizeToFreqValues == cardSize) {
                    Map.Entry<Integer, Integer> maxEntry = sizeToFreq.entrySet().stream()
                            .max(Map.Entry.comparingByValue())
                            .orElse(null);

                    assert maxEntry != null;
                    System.out.printf("Самое частое количество повторений %d (встретилось %d раз)\n", maxEntry.getKey(), maxEntry.getValue());
                    System.out.println("Другие размеры:");
                    for (int keySet : sizeToFreq.keySet()) {
                        if (keySet != maxEntry.getKey()) {
                            System.out.printf("- %d (%d раз)\n", keySet, sizeToFreq.get(keySet));
                        }
                    }
                }
            }
        }).start();
    }
}
