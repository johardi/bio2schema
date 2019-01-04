package org.bio2schema.pipeline.clinicaltrials;

import static org.bio2schema.util.JsonMutators.set;
import static org.bio2schema.util.JsonMutators.with;
import static org.bio2schema.util.JsonPreconditions.checkIfObjectNode;
import static org.bio2schema.vocab.SchemaOrg.PROPERTY_POPULATION;
import org.bio2schema.api.pipeline.Processor;
import org.bio2schema.util.JacksonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public final class StudyPopulationProcessor implements Processor {

  private static final String INCLUSION_CRITERIA = "Inclusion Criteria";
  private static final String EXCLUSION_CRITERIA = "Exclusion Criteria";

  @Override
  public JsonNode process(JsonNode input) {
    checkIfObjectNode(input);
    JsonNode studyPopulation = input.path(PROPERTY_POPULATION);
    if (studyPopulation.isTextual()) {
      JsonNode inclusionExclusion = processInclusionExclusionCriteria(studyPopulation);
      set(input, with(PROPERTY_POPULATION, inclusionExclusion));
    }
    return input;
  }

  private JsonNode processInclusionExclusionCriteria(JsonNode studyPopulation) {
    JsonNode processingResult = studyPopulation;
    try {
      String populationParagraph = studyPopulation.asText();
      if (hasInclusionExclusionStructure(populationParagraph)) {
        processingResult = extractAndAddInclusionExclusionCriteria(populationParagraph);
      } else if (hasInclusionOnly(populationParagraph)) {
        processingResult = extractAndAddInclusionCriteriaOnly(populationParagraph);
      } else if (hasExclusionOnly(populationParagraph)) {
        processingResult = extractAndAddExclusionCriteriaOnly(populationParagraph);
      }
    } catch (Exception e) {
      // XXX: We ignore any parsing error and the met return the original study
      // population text instead
    }
    return processingResult;
  }

  private ArrayNode extractAndAddInclusionExclusionCriteria(String paragraph) {
    String inclusionCriteria = paragraph.substring(
        StringUtils.indexOfIgnoreCase(paragraph, INCLUSION_CRITERIA),
        StringUtils.indexOfIgnoreCase(paragraph, EXCLUSION_CRITERIA));
    String exclusionCriteria = paragraph.substring(
        StringUtils.indexOfIgnoreCase(paragraph, EXCLUSION_CRITERIA),
        paragraph.length());
    ArrayNode populationList = JacksonUtils.createEmptyArrayNode();
    populationList.add(inclusionCriteria.trim());
    populationList.add(exclusionCriteria.trim());
    return populationList;
  }

  private ArrayNode extractAndAddInclusionCriteriaOnly(String paragraph) {
    String inclusionCriteria = paragraph.substring(
        StringUtils.indexOfIgnoreCase(paragraph, INCLUSION_CRITERIA),
        paragraph.length());
    String exclusionCriteria = "";
    ArrayNode populationList = JacksonUtils.createEmptyArrayNode();
    populationList.add(inclusionCriteria.trim());
    populationList.add(exclusionCriteria);
    return populationList;
  }

  private ArrayNode extractAndAddExclusionCriteriaOnly(String paragraph) {
    String inclusionCriteria = "";
    String exclusionCriteria = paragraph.substring(
        StringUtils.indexOfIgnoreCase(paragraph, EXCLUSION_CRITERIA),
        paragraph.length());
    ArrayNode populationList = JacksonUtils.createEmptyArrayNode();
    populationList.add(inclusionCriteria);
    populationList.add(exclusionCriteria.trim());
    return populationList;
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
}
