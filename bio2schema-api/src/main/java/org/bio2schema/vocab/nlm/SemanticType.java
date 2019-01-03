package org.bio2schema.vocab.nlm;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nonnull;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public final class SemanticType {

  //@formatter:off
  public static final SemanticType ACTIVITY = getInstance("T052", "Activity", "Activities & Behaviors", "ACTI");
  public static final SemanticType BEHAVIOR = getInstance("T053", "Behavior", "Activities & Behaviors", "ACTI");
  public static final SemanticType DAILY_OR_RECREATIONAL_ACTIVITY = getInstance("T056", "Daily or Recreational Activity", "Activities & Behaviors", "ACTI");
  public static final SemanticType EVENT = getInstance("T051", "Event", "Activities & Behaviors", "ACTI");
  public static final SemanticType GOVERNMENTAL_OR_REGULATORY_ACTIVITY = getInstance("T064", "Governmental or Regulatory Activity", "Activities & Behaviors", "ACTI");
  public static final SemanticType INDIVIDUAL_BEHAVIOR = getInstance("T055", "Individual Behavior", "Activities & Behaviors", "ACTI");
  public static final SemanticType MACHINE_ACTIVITY = getInstance("T066", "Machine Activity", "Activities & Behaviors", "ACTI");
  public static final SemanticType OCCUPATIONAL_ACTIVITY = getInstance("T057", "Occupational Activity", "Activities & Behaviors", "ACTI");
  public static final SemanticType SOCIAL_BEHAVIOR = getInstance("T054", "Social Behavior", "Activities & Behaviors", "ACTI");
  public static final SemanticType ANATOMICAL_STRUCTURE = getInstance("T017", "Anatomical Structure", "Anatomy", "ANAT");
  public static final SemanticType BODY_LOCATION_OR_REGION = getInstance("T029", "Body Location or Region", "Anatomy", "ANAT");
  public static final SemanticType BODY_PART_ORGAN_OR_ORGAN_COMPONENT = getInstance("T023", "Body Part, Organ, or Organ Component", "Anatomy", "ANAT");
  public static final SemanticType BODY_SPACE_OR_JUNCTION = getInstance("T030", "Body Space or Junction", "Anatomy", "ANAT");
  public static final SemanticType BODY_SUBSTANCE = getInstance("T031", "Body Substance", "Anatomy", "ANAT");
  public static final SemanticType BODY_SYSTEM = getInstance("T022", "Body System", "Anatomy", "ANAT");
  public static final SemanticType CELL = getInstance("T025", "Cell", "Anatomy", "ANAT");
  public static final SemanticType CELL_COMPONENT = getInstance("T026", "Cell Component", "Anatomy", "ANAT");
  public static final SemanticType EMBRYONIC_STRUCTURE = getInstance("T018", "Embryonic Structure", "Anatomy", "ANAT");
  public static final SemanticType FULLY_FORMED_ANATOMICAL_STRUCTURE = getInstance("T021", "Fully Formed Anatomical Structure", "Anatomy", "ANAT");
  public static final SemanticType TISSUE = getInstance("T024", "Tissue", "Anatomy", "ANAT");
  public static final SemanticType AMINO_ACID_PEPTIDE_OR_PROTEIN = getInstance("T116", "Amino Acid, Peptide, or Protein", "Chemicals & Drugs", "CHEM");
  public static final SemanticType ANTIBIOTIC = getInstance("T195", "Antibiotic", "Chemicals & Drugs", "CHEM");
  public static final SemanticType BIOLOGICALLY_ACTIVE_SUBSTANCE = getInstance("T123", "Biologically Active Substance", "Chemicals & Drugs", "CHEM");
  public static final SemanticType BIOMEDICAL_OR_DENTAL_MATERIAL = getInstance("T122", "Biomedical or Dental Material", "Chemicals & Drugs", "CHEM");
  public static final SemanticType CHEMICAL = getInstance("T103", "Chemical", "Chemicals & Drugs", "CHEM");
  public static final SemanticType CHEMICAL_VIEWED_FUNCTIONALLY = getInstance("T120", "Chemical Viewed Functionally", "Chemicals & Drugs", "CHEM");
  public static final SemanticType CHEMICAL_VIEWED_STRUCTURALLY = getInstance("T104", "Chemical Viewed Structurally", "Chemicals & Drugs", "CHEM");
  public static final SemanticType CLINICAL_DRUG = getInstance("T200", "Clinical Drug", "Chemicals & Drugs", "CHEM");
  public static final SemanticType ELEMENT_ION_OR_ISOTOPE = getInstance("T196", "Element, Ion, or Isotope", "Chemicals & Drugs", "CHEM");
  public static final SemanticType ENZYME = getInstance("T126", "Enzyme", "Chemicals & Drugs", "CHEM");
  public static final SemanticType HAZARDOUS_OR_POISONOUS_SUBSTANCE = getInstance("T131", "Hazardous or Poisonous Substance", "Chemicals & Drugs", "CHEM");
  public static final SemanticType HORMONE = getInstance("T125", "Hormone", "Chemicals & Drugs", "CHEM");
  public static final SemanticType IMMUNOLOGIC_FACTOR = getInstance("T129", "Immunologic Factor", "Chemicals & Drugs", "CHEM");
  public static final SemanticType INDICATOR_REAGENT_OR_DIAGNOSTIC_AID = getInstance("T130", "Indicator, Reagent, or Diagnostic Aid", "Chemicals & Drugs", "CHEM");
  public static final SemanticType INORGANIC_CHEMICAL = getInstance("T197", "Inorganic Chemical", "Chemicals & Drugs", "CHEM");
  public static final SemanticType NUCLEIC_ACID_NUCLEOSIDE_OR_NUCLEOTIDE = getInstance("T114", "Nucleic Acid, Nucleoside, or Nucleotide", "Chemicals & Drugs", "CHEM");
  public static final SemanticType ORGANIC_CHEMICAL = getInstance("T109", "Organic Chemical", "Chemicals & Drugs", "CHEM");
  public static final SemanticType PHARMACOLOGIC_SUBSTANCE = getInstance("T121", "Pharmacologic Substance", "Chemicals & Drugs", "CHEM");
  public static final SemanticType RECEPTOR = getInstance("T192", "Receptor", "Chemicals & Drugs", "CHEM");
  public static final SemanticType VITAMIN = getInstance("T127", "Vitamin", "Chemicals & Drugs", "CHEM");
  public static final SemanticType CLASSIFICATION = getInstance("T185", "Classification", "Concepts & Ideas", "CONC");
  public static final SemanticType CONCEPTUAL_ENTITY = getInstance("T077", "Conceptual Entity", "Concepts & Ideas", "CONC");
  public static final SemanticType FUNCTIONAL_CONCEPT = getInstance("T169", "Functional Concept", "Concepts & Ideas", "CONC");
  public static final SemanticType GROUP_ATTRIBUTE = getInstance("T102", "Group Attribute", "Concepts & Ideas", "CONC");
  public static final SemanticType IDEA_OR_CONCEPT = getInstance("T078", "Idea or Concept", "Concepts & Ideas", "CONC");
  public static final SemanticType INTELLECTUAL_PRODUCT = getInstance("T170", "Intellectual Product", "Concepts & Ideas", "CONC");
  public static final SemanticType LANGUAGE = getInstance("T171", "Language", "Concepts & Ideas", "CONC");
  public static final SemanticType QUALITATIVE_CONCEPT = getInstance("T080", "Qualitative Concept", "Concepts & Ideas", "CONC");
  public static final SemanticType QUANTITATIVE_CONCEPT = getInstance("T081", "Quantitative Concept", "Concepts & Ideas", "CONC");
  public static final SemanticType REGULATION_OR_LAW = getInstance("T089", "Regulation or Law", "Concepts & Ideas", "CONC");
  public static final SemanticType SPATIAL_CONCEPT = getInstance("T082", "Spatial Concept", "Concepts & Ideas", "CONC");
  public static final SemanticType TEMPORAL_CONCEPT = getInstance("T079", "Temporal Concept", "Concepts & Ideas", "CONC");
  public static final SemanticType DRUG_DELIVERY_DEVICE = getInstance("T203", "Drug Delivery Device", "Devices", "DEVI");
  public static final SemanticType MEDICAL_DEVICE = getInstance("T074", "Medical Device", "Devices", "DEVI");
  public static final SemanticType RESEARCH_DEVICE = getInstance("T075", "Research Device", "Devices", "DEVI");
  public static final SemanticType ACQUIRED_ABNORMALITY = getInstance("T020", "Acquired Abnormality", "Disorders", "DISO");
  public static final SemanticType ANATOMICAL_ABNORMALITY = getInstance("T190", "Anatomical Abnormality", "Disorders", "DISO");
  public static final SemanticType CELL_OR_MOLECULAR_DYSFUNCTION = getInstance("T049", "Cell or Molecular Dysfunction", "Disorders", "DISO");
  public static final SemanticType CONGENITAL_ABNORMALITY = getInstance("T019", "Congenital Abnormality", "Disorders", "DISO");
  public static final SemanticType DISEASE_OR_SYNDROME = getInstance("T047", "Disease or Syndrome", "Disorders", "DISO");
  public static final SemanticType EXPERIMENTAL_MODEL_OF_DISEASE = getInstance("T050", "Experimental Model of Disease", "Disorders", "DISO");
  public static final SemanticType FINDING = getInstance("T033", "Finding", "Disorders", "DISO");
  public static final SemanticType INJURY_OR_POISONING = getInstance("T037", "Injury or Poisoning", "Disorders", "DISO");
  public static final SemanticType MENTAL_OR_BEHAVIORAL_DYSFUNCTION = getInstance("T048", "Mental or Behavioral Dysfunction", "Disorders", "DISO");
  public static final SemanticType NEOPLASTIC_PROCESS = getInstance("T191", "Neoplastic Process", "Disorders", "DISO");
  public static final SemanticType PATHOLOGIC_FUNCTION = getInstance("T046", "Pathologic Function", "Disorders", "DISO");
  public static final SemanticType SIGN_OR_SYMPTOM = getInstance("T184", "Sign or Symptom", "Disorders", "DISO");
  public static final SemanticType AMINO_ACID_SEQUENCE = getInstance("T087", "Amino Acid Sequence", "Genes & Molecular Sequences", "GENE");
  public static final SemanticType CARBOHYDRATE_SEQUENCE = getInstance("T088", "Carbohydrate Sequence", "Genes & Molecular Sequences", "GENE");
  public static final SemanticType GENE_OR_GENOME = getInstance("T028", "Gene or Genome", "Genes & Molecular Sequences", "GENE");
  public static final SemanticType MOLECULAR_SEQUENCE = getInstance("T085", "Molecular Sequence", "Genes & Molecular Sequences", "GENE");
  public static final SemanticType NUCLEOTIDE_SEQUENCE = getInstance("T086", "Nucleotide Sequence", "Genes & Molecular Sequences", "GENE");
  public static final SemanticType GEOGRAPHIC_AREA = getInstance("T083", "Geographic Area", "Geographic Areas", "GEOG");
  public static final SemanticType AGE_GROUP = getInstance("T100", "Age Group", "Living Beings", "LIVB");
  public static final SemanticType AMPHIBIAN = getInstance("T011", "Amphibian", "Living Beings", "LIVB");
  public static final SemanticType ANIMAL = getInstance("T008", "Animal", "Living Beings", "LIVB");
  public static final SemanticType ARCHAEON = getInstance("T194", "Archaeon", "Living Beings", "LIVB");
  public static final SemanticType BACTERIUM = getInstance("T007", "Bacterium", "Living Beings", "LIVB");
  public static final SemanticType BIRD = getInstance("T012", "Bird", "Living Beings", "LIVB");
  public static final SemanticType EUKARYOTE = getInstance("T204", "Eukaryote", "Living Beings", "LIVB");
  public static final SemanticType FAMILY_GROUP = getInstance("T099", "Family Group", "Living Beings", "LIVB");
  public static final SemanticType FISH = getInstance("T013", "Fish", "Living Beings", "LIVB");
  public static final SemanticType FUNGUS = getInstance("T004", "Fungus", "Living Beings", "LIVB");
  public static final SemanticType GROUP = getInstance("T096", "Group", "Living Beings", "LIVB");
  public static final SemanticType HUMAN = getInstance("T016", "Human", "Living Beings", "LIVB");
  public static final SemanticType MAMMAL = getInstance("T015", "Mammal", "Living Beings", "LIVB");
  public static final SemanticType ORGANISM = getInstance("T001", "Organism", "Living Beings", "LIVB");
  public static final SemanticType PATIENT_OR_DISABLED_GROUP = getInstance("T101", "Patient or Disabled Group", "Living Beings", "LIVB");
  public static final SemanticType PLANT = getInstance("T002", "Plant", "Living Beings", "LIVB");
  public static final SemanticType POPULATION_GROUP = getInstance("T098", "Population Group", "Living Beings", "LIVB");
  public static final SemanticType PROFESSIONAL_OR_OCCUPATIONAL_GROUP = getInstance("T097", "Professional or Occupational Group", "Living Beings", "LIVB");
  public static final SemanticType REPTILE = getInstance("T014", "Reptile", "Living Beings", "LIVB");
  public static final SemanticType VERTEBRATE = getInstance("T010", "Vertebrate", "Living Beings", "LIVB");
  public static final SemanticType VIRUS = getInstance("T005", "Virus", "Living Beings", "LIVB");
  public static final SemanticType ENTITY = getInstance("T071", "Entity", "Objects", "OBJC");
  public static final SemanticType FOOD = getInstance("T168", "Food", "Objects", "OBJC");
  public static final SemanticType MANUFACTURED_OBJECT = getInstance("T073", "Manufactured Object", "Objects", "OBJC");
  public static final SemanticType PHYSICAL_OBJECT = getInstance("T072", "Physical Object", "Objects", "OBJC");
  public static final SemanticType SUBSTANCE = getInstance("T167", "Substance", "Objects", "OBJC");
  public static final SemanticType BIOMEDICAL_OCCUPATION_OR_DISCIPLINE = getInstance("T091", "Biomedical Occupation or Discipline", "Occupations", "OCCU");
  public static final SemanticType OCCUPATION_OR_DISCIPLINE = getInstance("T090", "Occupation or Discipline", "Occupations", "OCCU");
  public static final SemanticType HEALTH_CARE_RELATED_ORGANIZATION = getInstance("T093", "Health Care Related Organization", "Organizations", "ORGA");
  public static final SemanticType ORGANIZATION = getInstance("T092", "Organization", "Organizations", "ORGA");
  public static final SemanticType PROFESSIONAL_SOCIETY = getInstance("T094", "Professional Society", "Organizations", "ORGA");
  public static final SemanticType SELF_HELP_OR_RELIEF_ORGANIZATION = getInstance("T095", "Self-help or Relief Organization", "Organizations", "ORGA");
  public static final SemanticType BIOLOGIC_FUNCTION = getInstance("T038", "Biologic Function", "Phenomena", "PHEN");
  public static final SemanticType ENVIRONMENTAL_EFFECT_OF_HUMANS = getInstance("T069", "Environmental Effect of Humans", "Phenomena", "PHEN");
  public static final SemanticType HUMAN_CAUSED_PHENOMENON_OR_PROCESS = getInstance("T068", "Human-caused Phenomenon or Process", "Phenomena", "PHEN");
  public static final SemanticType LABORATORY_OR_TEST_RESULT = getInstance("T034", "Laboratory or Test Result", "Phenomena", "PHEN");
  public static final SemanticType NATURAL_PHENOMENON_OR_PROCESS = getInstance("T070", "Natural Phenomenon or Process", "Phenomena", "PHEN");
  public static final SemanticType PHENOMENON_OR_PROCESS = getInstance("T067", "Phenomenon or Process", "Phenomena", "PHEN");
  public static final SemanticType CELL_FUNCTION = getInstance("T043", "Cell Function", "Physiology", "PHYS");
  public static final SemanticType CLINICAL_ATTRIBUTE = getInstance("T201", "Clinical Attribute", "Physiology", "PHYS");
  public static final SemanticType GENETIC_FUNCTION = getInstance("T045", "Genetic Function", "Physiology", "PHYS");
  public static final SemanticType MENTAL_PROCESS = getInstance("T041", "Mental Process", "Physiology", "PHYS");
  public static final SemanticType MOLECULAR_FUNCTION = getInstance("T044", "Molecular Function", "Physiology", "PHYS");
  public static final SemanticType ORGANISM_ATTRIBUTE = getInstance("T032", "Organism Attribute", "Physiology", "PHYS");
  public static final SemanticType ORGANISM_FUNCTION = getInstance("T040", "Organism Function", "Physiology", "PHYS");
  public static final SemanticType ORGAN_OR_TISSUE_FUNCTION = getInstance("T042", "Organ or Tissue Function", "Physiology", "PHYS");
  public static final SemanticType PHYSIOLOGIC_FUNCTION = getInstance("T039", "Physiologic Function", "Physiology", "PHYS");
  public static final SemanticType DIAGNOSTIC_PROCEDURE = getInstance("T060", "Diagnostic Procedure", "Procedures", "PROC");
  public static final SemanticType EDUCATIONAL_ACTIVITY = getInstance("T065", "Educational Activity", "Procedures", "PROC");
  public static final SemanticType HEALTH_CARE_ACTIVITY = getInstance("T058", "Health Care Activity", "Procedures", "PROC");
  public static final SemanticType LABORATORY_PROCEDURE = getInstance("T059", "Laboratory Procedure", "Procedures", "PROC");
  public static final SemanticType MOLECULAR_BIOLOGY_RESEARCH_TECHNIQUE = getInstance("T063", "Molecular Biology Research Technique", "Procedures", "PROC");
  public static final SemanticType RESEARCH_ACTIVITY = getInstance("T062", "Research Activity", "Procedures", "PROC");
  public static final SemanticType THERAPEUTIC_OR_PREVENTIVE_PROCEDURE = getInstance("T061", "Therapeutic or Preventive Procedure", "Procedures", "PROC");
  //@formatter:on

  private final String tui;
  private final String name;
  private final String groupName;
  private final String groupAbbrev;

  public SemanticType(@Nonnull String tui, @Nonnull String name, @Nonnull String groupName,
      @Nonnull String groupAbbrev) {
    this.tui = checkNotNull(tui);
    this.name = checkNotNull(name);
    this.groupName = checkNotNull(groupName);
    this.groupAbbrev = checkNotNull(groupAbbrev);
  }

  private static SemanticType getInstance(String tui, String name, String groupName,
      String groupAbbrev) {
    return new SemanticType(tui, name, groupName, groupAbbrev);
  }

  public static SemanticType getSemanticType(@Nonnull String tui) {
    checkNotNull(tui);
    return TUI_TYPE_MAP.get(tui);
  }

  public String getTui() {
    return tui;
  }

  public String getName() {
    return name;
  }

  public String getGroupName() {
    return groupName;
  }

  public String getGroupAbbrev() {
    return groupAbbrev;
  }

  @Override
  public int hashCode() {
    return Objects.hash(tui, name, groupName, groupAbbrev);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof SemanticType)) {
      return false;
    }
    SemanticType other = (SemanticType) obj;
    return tui.equals(other.tui) && name.equals(other.name) && groupName.equals(other.groupName)
        && groupAbbrev.equals(other.groupAbbrev);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("tui", tui).add("name", name)
        .add("groupName", groupName).add("groupAbbrv", groupAbbrev).toString();
  }

  //@formatter:off
  public static final Set<SemanticType> ACTIVITIES_AND_BEHAVIORS_GROUP =
      Sets.newHashSet(ACTIVITY, BEHAVIOR, DAILY_OR_RECREATIONAL_ACTIVITY, EVENT, 
          GOVERNMENTAL_OR_REGULATORY_ACTIVITY, INDIVIDUAL_BEHAVIOR, MACHINE_ACTIVITY,
          OCCUPATIONAL_ACTIVITY, SOCIAL_BEHAVIOR);

  public static final Set<SemanticType> ANATOMY_GROUP =
      Sets.newHashSet(ANATOMICAL_STRUCTURE, BODY_LOCATION_OR_REGION,
          BODY_PART_ORGAN_OR_ORGAN_COMPONENT, BODY_SPACE_OR_JUNCTION, BODY_SUBSTANCE,
          BODY_SYSTEM, CELL, CELL_COMPONENT, EMBRYONIC_STRUCTURE,
          FULLY_FORMED_ANATOMICAL_STRUCTURE, TISSUE);

  public static final Set<SemanticType> CHEMICALS_AND_DRUGS_GROUP =
      Sets.newHashSet(AMINO_ACID_PEPTIDE_OR_PROTEIN, ANTIBIOTIC, BIOLOGICALLY_ACTIVE_SUBSTANCE,
          BIOMEDICAL_OR_DENTAL_MATERIAL, CHEMICAL, CHEMICAL_VIEWED_FUNCTIONALLY,
          CHEMICAL_VIEWED_STRUCTURALLY, CLINICAL_DRUG, ELEMENT_ION_OR_ISOTOPE, ENZYME,
          HAZARDOUS_OR_POISONOUS_SUBSTANCE, HORMONE, IMMUNOLOGIC_FACTOR,
          INDICATOR_REAGENT_OR_DIAGNOSTIC_AID, INORGANIC_CHEMICAL,
          NUCLEIC_ACID_NUCLEOSIDE_OR_NUCLEOTIDE, ORGANIC_CHEMICAL, PHARMACOLOGIC_SUBSTANCE,
          RECEPTOR, VITAMIN);

  public static final Set<SemanticType> CONCEPTS_AND_IDEAS_GROUP =
      Sets.newHashSet(CLASSIFICATION, CONCEPTUAL_ENTITY, FUNCTIONAL_CONCEPT, GROUP_ATTRIBUTE,
          IDEA_OR_CONCEPT, INTELLECTUAL_PRODUCT, LANGUAGE, QUALITATIVE_CONCEPT,
          QUANTITATIVE_CONCEPT, REGULATION_OR_LAW, SPATIAL_CONCEPT, TEMPORAL_CONCEPT);

  public static final Set<SemanticType> DEVICES_GROUP =
      Sets.newHashSet(DRUG_DELIVERY_DEVICE, MEDICAL_DEVICE, RESEARCH_DEVICE);

  public static final Set<SemanticType> DISORDERS_GROUP =
      Sets.newHashSet(ACQUIRED_ABNORMALITY, ANATOMICAL_ABNORMALITY,
          CELL_OR_MOLECULAR_DYSFUNCTION,
          CONGENITAL_ABNORMALITY, DISEASE_OR_SYNDROME, EXPERIMENTAL_MODEL_OF_DISEASE, FINDING,
          INJURY_OR_POISONING, MENTAL_OR_BEHAVIORAL_DYSFUNCTION, NEOPLASTIC_PROCESS,
          PATHOLOGIC_FUNCTION, SIGN_OR_SYMPTOM);

  public static final Set<SemanticType> GENES_AND_MOLECULAR_SEQUENCES_GROUP =
      Sets.newHashSet(AMINO_ACID_SEQUENCE, CARBOHYDRATE_SEQUENCE, GENE_OR_GENOME,
          MOLECULAR_SEQUENCE, NUCLEOTIDE_SEQUENCE);

  public static final Set<SemanticType> GEOGRAPHIC_AREAS_GROUP =
      Sets.newHashSet(GEOGRAPHIC_AREA);

  public static final Set<SemanticType> LIVING_BEINGS_GROUP =
      Sets.newHashSet(AGE_GROUP, AMPHIBIAN, ANIMAL, ARCHAEON, BACTERIUM, BIRD, EUKARYOTE,
          FAMILY_GROUP, FISH, FUNGUS, GROUP, HUMAN, MAMMAL, ORGANISM, PATIENT_OR_DISABLED_GROUP,
          PLANT, POPULATION_GROUP, PROFESSIONAL_OR_OCCUPATIONAL_GROUP, REPTILE, VERTEBRATE,
          VIRUS);

  public static final Set<SemanticType> OBJECTS_GROUP =
      Sets.newHashSet(ENTITY, FOOD, MANUFACTURED_OBJECT, PHYSICAL_OBJECT, SUBSTANCE);

  public static final Set<SemanticType> OCCUPATIONS_GROUP =
      Sets.newHashSet(BIOMEDICAL_OCCUPATION_OR_DISCIPLINE, OCCUPATION_OR_DISCIPLINE);

  public static final Set<SemanticType> ORGANIZATIONS_GROUP =
      Sets.newHashSet(HEALTH_CARE_RELATED_ORGANIZATION, ORGANIZATION, PROFESSIONAL_SOCIETY,
          SELF_HELP_OR_RELIEF_ORGANIZATION);

  public static final Set<SemanticType> PHENOMENA_GROUP =
      Sets.newHashSet(BIOLOGIC_FUNCTION, ENVIRONMENTAL_EFFECT_OF_HUMANS,
          HUMAN_CAUSED_PHENOMENON_OR_PROCESS, LABORATORY_OR_TEST_RESULT,
          NATURAL_PHENOMENON_OR_PROCESS, PHENOMENON_OR_PROCESS);

  public static final Set<SemanticType> PHYSIOLOGY_GROUP =
      Sets.newHashSet(CELL_FUNCTION, CLINICAL_ATTRIBUTE, GENETIC_FUNCTION, MENTAL_PROCESS,
          MOLECULAR_FUNCTION, ORGANISM_ATTRIBUTE, ORGANISM_FUNCTION, ORGAN_OR_TISSUE_FUNCTION,
          PHYSIOLOGIC_FUNCTION);

  public static final Set<SemanticType> PROCEDURES_GROUP =
      Sets.newHashSet(DIAGNOSTIC_PROCEDURE, EDUCATIONAL_ACTIVITY, HEALTH_CARE_ACTIVITY,
          LABORATORY_PROCEDURE, MOLECULAR_BIOLOGY_RESEARCH_TECHNIQUE, RESEARCH_ACTIVITY,
          THERAPEUTIC_OR_PREVENTIVE_PROCEDURE);

  public static final Set<SemanticType> SEMANTIC_TYPES = Sets.newHashSet();
  static {
    SEMANTIC_TYPES.addAll(ACTIVITIES_AND_BEHAVIORS_GROUP);
    SEMANTIC_TYPES.addAll(ANATOMY_GROUP);
    SEMANTIC_TYPES.addAll(CHEMICALS_AND_DRUGS_GROUP);
    SEMANTIC_TYPES.addAll(CONCEPTS_AND_IDEAS_GROUP);
    SEMANTIC_TYPES.addAll(DEVICES_GROUP);
    SEMANTIC_TYPES.addAll(DISORDERS_GROUP);
    SEMANTIC_TYPES.addAll(GENES_AND_MOLECULAR_SEQUENCES_GROUP);
    SEMANTIC_TYPES.addAll(GEOGRAPHIC_AREAS_GROUP);
    SEMANTIC_TYPES.addAll(LIVING_BEINGS_GROUP);
    SEMANTIC_TYPES.addAll(OBJECTS_GROUP);
    SEMANTIC_TYPES.addAll(OCCUPATIONS_GROUP);
    SEMANTIC_TYPES.addAll(ORGANIZATIONS_GROUP);
    SEMANTIC_TYPES.addAll(PHENOMENA_GROUP);
    SEMANTIC_TYPES.addAll(PHYSIOLOGY_GROUP);
    SEMANTIC_TYPES.addAll(PROCEDURES_GROUP);
  }

  private static final Map<String, SemanticType> TUI_TYPE_MAP = Maps.uniqueIndex(SEMANTIC_TYPES, SemanticType::getTui);
  //@formatter:on
}
