package org.bio2schema.reconcilers.dbpedia;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.bio2schema.util.JsonPreconditions.checkIfObjectNode;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.apache.commons.text.similarity.SimilarityScore;
import org.bio2schema.api.reconciliation.CachedEntityReconciler;
import org.bio2schema.api.reconciliation.Database;
import org.bio2schema.api.reconciliation.entitytype.GenericEntity;
import org.bio2schema.util.JacksonUtils;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;

public class DbpediaSimilarityLookup extends CachedEntityReconciler<GenericEntity> {

  private static final double DEFAULT_SIMILARITY_THRESHOLD = 0.88;

  private final SimilarityScore<Double> similarityAlgorithm = new JaroWinklerDistance();

  private double similarityThreshold;

  private final List<String> typeFilters = Lists.newArrayList();

  public DbpediaSimilarityLookup(@Nonnull Database database) {
    this(database, DEFAULT_SIMILARITY_THRESHOLD);
  }

  public DbpediaSimilarityLookup(@Nonnull Database database, @Nonnull double similarityThreshold) {
    super("DBpedia Lookup", database);
    this.similarityThreshold = checkNotNull(similarityThreshold);
  }

  public void searchOnly(@Nonnull String... typeName) {
    typeFilters.addAll(Arrays.asList(typeName));
  }

  @Nullable
  @Override
  public DbpediaArticle handleResult(String inputString, Object outputResult) throws IOException {
    ObjectNode objectNode = checkIfObjectNode(outputResult);
    DbpediaArticle[] results = JacksonUtils.mapper.treeToValue(objectNode.get("results"), DbpediaArticle[].class);
    DbpediaArticle result = null;
    double minThreshold = similarityThreshold;
    for (int i = 0; i < results.length; i++) {
      DbpediaArticle article = results[i];
      if (isFilterEmpty() || typeFilters.contains(article.getType())) {
        double score = similarityAlgorithm.apply(inputString, article.getName());
        if (isSingleWord(inputString)) {
          if (score == 1) {
            result = article;
            minThreshold = 1;
          }
        } else {
          if (score > minThreshold) {
            result = article;
            minThreshold = score;
          }
        }
      }
    }
    return result;
  }

  private boolean isFilterEmpty() {
    return typeFilters.isEmpty();
  }

  private static boolean isSingleWord(String s) {
    return s.trim().split(" ").length == 1;
  }
}
