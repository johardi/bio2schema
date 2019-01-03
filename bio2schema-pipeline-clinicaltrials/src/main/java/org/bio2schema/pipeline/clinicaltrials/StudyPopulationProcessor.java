package org.bio2schema.pipeline.clinicaltrials;

import static org.bio2schema.util.JsonMutators.set;
import static org.bio2schema.util.JsonMutators.with;
import static org.bio2schema.util.JsonPreconditions.checkIfObjectNode;
import static org.bio2schema.vocab.SchemaOrg.PROPERTY_POPULATION;
import org.bio2schema.api.pipeline.Processor;
import org.bio2schema.util.JacksonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.TextNode;

public final class StudyPopulationProcessor implements Processor {

  private static final String INCLUSION_CRITERIA = "Inclusion Criteria";
  private static final String EXCLUSION_CRITERIA = "Exclusion Criteria";

  @Override
  public JsonNode process(JsonNode input) {
    checkIfObjectNode(input);
    JsonNode studyPopulation = input.path(PROPERTY_POPULATION);
    if (studyPopulation.isTextual()) {
      ArrayNode inclusionExclusion = processInclusionExclusionCriteria((TextNode) studyPopulation);
      if (notEmpty(inclusionExclusion)) {
        set(input, with(PROPERTY_POPULATION, inclusionExclusion));
      }
    }
    return input;
  }

  private ArrayNode processInclusionExclusionCriteria(TextNode studyPopulation) {
    ArrayNode populationList = JacksonUtils.createEmptyArrayNode();
    String populationParagraph = studyPopulation.asText();
    if (hasInclusionExclusionStructure(populationParagraph)) {
      extractAndAddInclusionExclusionCriteria(populationParagraph, populationList);
    } else if (hasInclusionOnly(populationParagraph)) {
      extractAndAddInclusionCriteriaOnly(populationParagraph, populationList);
    } else if (hasExclusionOnly(populationParagraph)) {
      extractAndAddExclusionCriteriaOnly(populationParagraph, populationList);
    }
    return populationList;
  }

  private void extractAndAddInclusionExclusionCriteria(String paragraph, ArrayNode collector) {
    String inclusionCriteria = paragraph.substring(
        StringUtils.indexOfIgnoreCase(paragraph, INCLUSION_CRITERIA),
        StringUtils.indexOfIgnoreCase(paragraph, EXCLUSION_CRITERIA));
    String exclusionCriteria = paragraph.substring(
        StringUtils.indexOfIgnoreCase(paragraph, EXCLUSION_CRITERIA),
        paragraph.length());
    collector.add(inclusionCriteria.trim());
    collector.add(exclusionCriteria.trim());
  }

  private void extractAndAddInclusionCriteriaOnly(String paragraph, ArrayNode collector) {
    String inclusionCriteria = paragraph.substring(
        StringUtils.indexOfIgnoreCase(paragraph, INCLUSION_CRITERIA),
        paragraph.length());
    String exclusionCriteria = "";
    collector.add(inclusionCriteria.trim());
    collector.add(exclusionCriteria);
  }

  private void extractAndAddExclusionCriteriaOnly(String paragraph, ArrayNode collector) {
    String inclusionCriteria = "";
    String exclusionCriteria = paragraph.substring(
        StringUtils.indexOfIgnoreCase(paragraph, EXCLUSION_CRITERIA),
        paragraph.length());
    collector.add(inclusionCriteria);
    collector.add(exclusionCriteria.trim());
  }

  private static boolean hasInclusionExclusionStructure(String population) {
    return StringUtils.containsIgnoreCase(population, INCLUSION_CRITERIA)
        && StringUtils.containsIgnoreCase(population, EXCLUSION_CRITERIA);
  }

  private static boolean hasInclusionOnly(String population) {
    return StringUtils.containsIgnoreCase(population, INCLUSION_CRITERIA)
        && !StringUtils.containsIgnoreCase(population, EXCLUSION_CRITERIA);
  }

  private static boolean hasExclusionOnly(String population) {
    return !StringUtils.containsIgnoreCase(population, INCLUSION_CRITERIA)
        && StringUtils.containsIgnoreCase(population, EXCLUSION_CRITERIA);
  }

  private static boolean notEmpty(ArrayNode arrayNode) {
    return arrayNode.size() != 0;
  }
}
