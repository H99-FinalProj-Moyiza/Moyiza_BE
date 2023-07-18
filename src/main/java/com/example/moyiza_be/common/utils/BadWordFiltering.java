package com.example.moyiza_be.common.utils;

import com.example.moyiza_be.domain.chat.dto.ChatMessageInput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Component
@Slf4j
public class BadWordFiltering implements BadWords {
    private final Set<String> badWordsSet = new HashSet<>(List.of(badWords));

    public boolean checkBadWord(String input) {
        String patternText = buildPatternText();

        for (String word : badWordsSet) {
            String[] chars = word.split("");
            if (Pattern.compile(String.join(patternText, chars))
                    .matcher(input)
                    .find()) return true;
        }
        return false;
    }

    public ChatMessageInput change(ChatMessageInput chatMessageInput) {
        String text = chatMessageInput.getContent();
        String patternText = buildPatternText();

        for (String word : badWordsSet) {
            if (word.length() == 1) {
                text = text.replace(word, substituteValue);
            }
            String[] chars = word.split("");
            text = Pattern.compile(String.join(patternText, chars))
                    .matcher(text)
                    .replaceAll(matchedWord -> substituteValue.repeat(matchedWord.group().length()));
        }
        chatMessageInput.setContent(text);

        return chatMessageInput;
    }

    private String buildPatternText() {
        StringBuilder delimiterBuilder = new StringBuilder("[");
        for (String delimiter : delimiters) {
            delimiterBuilder.append(Pattern.quote(delimiter));
        }
        delimiterBuilder.append("]*");
        return delimiterBuilder.toString();
    }
}