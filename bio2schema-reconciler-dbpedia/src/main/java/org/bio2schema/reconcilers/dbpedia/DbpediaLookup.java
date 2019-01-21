package org.bio2schema.reconcilers.dbpedia;

import static org.bio2schema.util.JsonPreconditions.checkIfObjectNode;
import java.io.IOException;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bio2schema.api.reconciliation.CachedEntityReconciler;
import org.bio2schema.api.reconciliation.Database;
import org.bio2schema.api.reconciliation.entitytype.GenericEntity;
import org.bio2schema.util.JacksonUtils;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;

public class DbpediaLookup extends CachedEntityReconciler<GenericEntity> {

  private final List<String> typeFilters = Lists.newArrayList();

  public DbpediaLookup(@Nonnull Database database) {
    super("DBpedia Lookup", database);
  }

  public DbpediaLookup filterType(@Nonnull String typeName) {
    typeFilters.add(typeName);
    return this;
  }

  public DbpediaLookup unfilterType(@Nonnull String typeName) {
    typeFilters.remove(typeName);
    return this;
  }

  @Nullable
  @Override
  public DbpediaArticle handleResult(String inputString, Object outputResult) throws IOException {
    ObjectNode objectNode = checkIfObjectNode(outputResult);
    DbpediaArticle[] results = JacksonUtils.mapper.treeToValue(objectNode.get("results"), DbpediaArticle[].class);
    return getImmediateTopResult(results);
  }

  @Nullable
  private DbpediaArticle getImmediateTopResult(DbpediaArticle[] results) {
    for (int i = 0; i < results.length; i++) {
      DbpediaArticle article = results[i];
      if (isFilterEmpty() || typeFilters.contains(article.getType())) {
        return article;
      }
    }
    return null;
  }

  private boolean isFilterEmpty() {
    return typeFilters.isEmpty();
  }
}
