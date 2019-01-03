package org.bio2schema.recognizers.stanfordner;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import com.google.common.collect.Lists;

public final class XmlResultWrapper {

  private final String text;

  public XmlResultWrapper(@Nonnull String text) {
    this.text = checkNotNull(text);
  }

  public Collection<String> getAllStringsEnclosedBy(String tagName) {
    List<String> strings = Lists.newArrayList();
    final Pattern pattern = getRegexPatternFor(tagName);
    final Matcher matcher = pattern.matcher(text);
    while (matcher.find()) {
      strings.add(matcher.group(1));
    }
    return strings;
  }

  private Pattern getRegexPatternFor(String tagName) {
    String regex = String.format("(?<=<%s>)(.*?)(?=</%s>)", tagName, tagName);
    return Pattern.compile(regex);
  }
}
