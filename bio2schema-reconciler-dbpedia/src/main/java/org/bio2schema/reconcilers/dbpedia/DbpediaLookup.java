package org.bio2schema.reconcilers.dbpedia;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.bio2schema.util.JsonPreconditions.checkIfObjectNode;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bio2schema.api.reconciliation.Database;
import org.bio2schema.api.reconciliation.EntityReconciler;
import org.bio2schema.api.reconciliation.entitytype.GenericEntity;
import org.bio2schema.util.JacksonUtils;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;

public class DbpediaLookup implements EntityReconciler<GenericEntity> {

  private final Database database;

  private final List<String> typeFilters = Lists.newArrayList();

  public DbpediaLookup(@Nonnull Database database) {
    this.database = checkNotNull(database);
  }

  public void searchOnly(@Nonnull String... typeName) {
    typeFilters.addAll(Arrays.asList(typeName));
  }

  @Override
  public Optional<GenericEntity> reconcile(String inputString) throws IOException {
    try {
      Object outputResult = database.find(inputString);
      DbpediaArticle article = handleResult(inputString, outputResult);
      return Optional.ofNullable(article);
    } catch (IOException e) {
      String message = String.format(
          "A problem occurred using reconciler '%s'\n> %s",
          database.getName(), e.getMessage());
      throw new IOException(message);
    }
  }

  @Nullable
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
