package org.bio2schema.reconcilers.dbpedia;

import static org.bio2schema.util.JsonPreconditions.checkIfObjectNode;
import java.io.IOException;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bio2schema.api.reconciliation.Database;
import org.bio2schema.api.reconciliation.StandardEntityReconciler;
import org.bio2schema.api.reconciliation.entitytype.GenericEntity;
import org.bio2schema.util.JacksonUtils;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;

public class DbpediaLookup extends StandardEntityReconciler<GenericEntity> {

  private static final double DEFAULT_SIMILARITY_THRESHOLD = 0.42;

  private double similarityThreshold = DEFAULT_SIMILARITY_THRESHOLD;

  private final List<String> acceptedTypes = Lists.newArrayList();

  public DbpediaLookup(@Nonnull Database database) {
    super("DBpedia Lookup", database);
  }

  /**
   * The similarity threshold is the desired lower limit for two strings having a same notion by
   * measuring their syntactic similarity. The threshold value is between 0 and 1 (where 0 = no
   * string matching and 1 = an exact match).
   * 
   * @param threshold A value between 0 and 1
   */
  public DbpediaLookup setSimilarityThreshold(double threshold) {
    if (threshold >= 0 && threshold <= 1) {
      similarityThreshold = threshold;
    }
    return this;
  }

  /**
   * Method for specifying a schema.org type that will be used to filter the results from DBpedia.
   * Use a simple naming, such as "Organization", "Person", "City", as the input for the method.
   *
   * @param schemaOrgType A schema.org type name (see {@link https://schema.org/docs/full.html})
   */
  public DbpediaLookup addFilter(String schemaOrgType) {
    acceptedTypes.add(schemaOrgType);
    return this;
  }

  @Nullable
  @Override
  public DbpediaArticle handleResult(String inputString, Object outputResult) throws IOException {
    ObjectNode objectNode = checkIfObjectNode(outputResult);
    DbpediaArticle[] results = JacksonUtils.mapper.treeToValue(objectNode.get("results"), DbpediaArticle[].class);
    return filterResult(inputString, results);
  }

  @Nullable
  private DbpediaArticle filterResult(String inputString, DbpediaArticle[] results) {
    for (int i = 0; i < results.length; i++) {
      DbpediaArticle article = results[i];
      if (acceptedTypes.isEmpty() || acceptedTypes.contains(article.getType())) {
        if (NameSimilarity.evaluate(inputString, article.getName(), similarityThreshold)) {
          return article;
        }
      }
    }
    return null;
  }
}
