package dev.expensewise.backend.category;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author arpan
 * @since 9/5/25
 */
@Service
public class EmojiService {
    private final Map<String, String> emojiMap = new HashMap<>();
    private final Map<String, Set<String>> keywordMap = new HashMap<>();
    private final String DEFAULT_CATEGORY_EMOJI = "\uD83D\uDCC2";

    public EmojiService() {
        emojiMap.put("food", "🍔");
        emojiMap.put("groceries", "🛒");
        emojiMap.put("transport", "🚌");
        emojiMap.put("fuel", "⛽");
        emojiMap.put("shopping", "🛍️");
        emojiMap.put("rent", "🏠");
        emojiMap.put("entertainment", "🎬");
        emojiMap.put("utilities", "💡");
        emojiMap.put("internet", "🌐");
        emojiMap.put("phone", "📱");
        emojiMap.put("health", "💊");
        emojiMap.put("fitness", "🏋️");
        emojiMap.put("salary", "💰");
        emojiMap.put("gift", "🎁");
        emojiMap.put("travel", "✈️");
        emojiMap.put("education", "📚");
        emojiMap.put("insurance", "🛡️");
        emojiMap.put("tax", "💸");
        emojiMap.put("charity", "🙏");
        emojiMap.put("pets", "🐾");
        emojiMap.put("kids", "🧒");
        emojiMap.put("loan", "🏦");
        emojiMap.put("clothing", "👕");
        emojiMap.put("electronics", "💻");
        emojiMap.put("subscription", "🔔");
        emojiMap.put("alcohol", "🍺");
        emojiMap.put("coffee", "☕");
        emojiMap.put("savings", "🏦");
        emojiMap.put("party", "🎉");
        emojiMap.put("others", "📂");
        keywordMap.put("food", Set.of("restaurant", "meal", "dinner", "lunch", "breakfast", "snack", "eating"));
        keywordMap.put("groceries", Set.of("market", "supermarket", "grocery", "vegetables", "fruits", "essentials"));
        keywordMap.put("transport", Set.of("bus", "train", "metro", "cab", "taxi", "ride", "rickshaw", "car"));
        keywordMap.put("fuel", Set.of("petrol", "diesel", "gas", "fuel", "cng"));
        keywordMap.put("shopping", Set.of("mall", "buy", "purchase", "shop", "retail"));
        keywordMap.put("rent", Set.of("house", "flat", "apartment", "room", "pg", "lease"));
        keywordMap.put("entertainment", Set.of("movie", "cinema", "concert", "show", "netflix", "spotify", "game", "theatre"));
        keywordMap.put("utilities", Set.of("electricity", "power", "bill", "water", "gas", "utility"));
        keywordMap.put("internet", Set.of("wifi", "broadband", "data", "recharge"));
        keywordMap.put("phone", Set.of("mobile", "cell", "telephone", "sim", "recharge", "call"));
        keywordMap.put("health", Set.of("doctor", "medicine", "hospital", "clinic", "pharmacy", "medical"));
        keywordMap.put("fitness", Set.of("gym", "exercise", "workout", "yoga", "trainer"));
        keywordMap.put("salary", Set.of("income", "pay", "bonus", "wages", "stipend"));
        keywordMap.put("gift", Set.of("present", "giftcard", "birthday", "festival"));
        keywordMap.put("travel", Set.of("trip", "flight", "journey", "vacation", "holiday", "tour"));
        keywordMap.put("education", Set.of("school", "college", "university", "books", "fees", "course"));
        keywordMap.put("insurance", Set.of("policy", "premium", "life insurance", "health insurance"));
        keywordMap.put("tax", Set.of("gst", "income tax", "tds", "government tax"));
        keywordMap.put("charity", Set.of("donation", "ngo", "fundraiser", "help"));
        keywordMap.put("pets", Set.of("dog", "cat", "animal", "pet food", "vet"));
        keywordMap.put("kids", Set.of("child", "baby", "school fee", "toy"));
        keywordMap.put("loan", Set.of("emi", "debt", "mortgage", "borrow", "credit"));
        keywordMap.put("clothing", Set.of("dress", "shirt", "pant", "shoes", "wear", "fashion"));
        keywordMap.put("electronics", Set.of("laptop", "phone", "computer", "tv", "gadget"));
        keywordMap.put("subscription", Set.of("membership", "prime", "netflix", "spotify", "disney", "hotstar"));
        keywordMap.put("alcohol", Set.of("beer", "whiskey", "wine", "vodka", "rum"));
        keywordMap.put("coffee", Set.of("tea", "latte", "espresso", "cappuccino", "brew", "café"));
        keywordMap.put("savings", Set.of("investment", "deposit", "bank", "fund", "fd", "rd"));
        keywordMap.put("party", Set.of("celebration", "birthday", "club", "bar"));
    }


    public String getCategory(String categoryName) {
        if (categoryName == null || categoryName.isBlank()) {
            return this.DEFAULT_CATEGORY_EMOJI;
        }
        String[] splitKeywords = categoryName.trim().toLowerCase().split("\\s+");
        for (String keyword : splitKeywords) {
            if (emojiMap.containsKey(keyword)) {
                return emojiMap.get(keyword);
            }
            String suggestedEmoji = getEmojiFromKeyword(keyword);
            if (!suggestedEmoji.equals(this.DEFAULT_CATEGORY_EMOJI)) {
                return suggestedEmoji;
            }
        }
        return "\uD83D\uDCC2";
    }

    private String getEmojiFromKeyword(String keyword) {
        for (Map.Entry<String, Set<String>> entry : keywordMap.entrySet()) {
            if (entry.getValue().contains(keyword)) {
                return emojiMap.get(entry.getKey());
            }
        }
        return this.DEFAULT_CATEGORY_EMOJI;
    }
}
