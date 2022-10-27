package org.opengis.cite.sta10.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * List of entity types in SensorThings API.
 */
public enum EntityType {
    THING("Thing", "Things"),
    LOCATION("Location", "Locations"),
    SENSOR("Sensor", "Sensors"),
    OBSERVED_PROPERTY("ObservedProperty", "ObservedProperties"),
    OBSERVATION("Observation", "Observations"),
    DATASTREAM("Datastream", "Datastreams"),
    FEATURE_OF_INTEREST("FeatureOfInterest", "FeaturesOfInterest"),
    HISTORICAL_LOCATION("HistoricalLocation", "HistoricalLocations");
    public final String singular;
    public final String plural;
    private final List<String> properties = new ArrayList<>();
    private final List<String> relations = new ArrayList<>();

    private static final Map<String, EntityType> NAMES_MAP = new HashMap<>();
    private static final Set<String> NAMES_PLURAL = new HashSet<>();

    static {
        THING.addProperties("name", "description");
        THING.addRelations(DATASTREAM.plural, HISTORICAL_LOCATION.plural, LOCATION.plural);

        LOCATION.addProperties("name", "description", "encodingType", "location");
        LOCATION.addRelations(HISTORICAL_LOCATION.plural, THING.plural);

        SENSOR.addProperties("name", "description", "encodingType", "metadata");
        SENSOR.addRelations(DATASTREAM.plural);

        OBSERVED_PROPERTY.addProperties("name", "definition", "description");
        OBSERVED_PROPERTY.addRelations(DATASTREAM.plural);

        OBSERVATION.addProperties("phenomenonTime", "result", "resultTime");
        OBSERVATION.addRelations(DATASTREAM.singular, FEATURE_OF_INTEREST.singular);

        DATASTREAM.addProperties("name", "description", "unitOfMeasurement", "observationType");
        DATASTREAM.addRelations(THING.singular, SENSOR.singular, OBSERVED_PROPERTY.singular, OBSERVATION.plural);

        FEATURE_OF_INTEREST.addProperties("name", "description", "encodingType", "feature");
        FEATURE_OF_INTEREST.addRelations(OBSERVATION.plural);

        HISTORICAL_LOCATION.addProperties("time");
        HISTORICAL_LOCATION.addRelations(THING.singular, LOCATION.plural);

        for (EntityType entityType : EntityType.values()) {
            NAMES_MAP.put(entityType.singular, entityType);
            NAMES_MAP.put(entityType.plural, entityType);
            NAMES_PLURAL.add(entityType.plural);
        }
    }

    public static EntityType getForRelation(String relation) {
        EntityType entityType = NAMES_MAP.get(relation);
        if (entityType == null) {
            throw new IllegalArgumentException("Unknown relation: " + relation);
        }
        return entityType;
    }

    public static boolean isPlural(String relation) {
        return NAMES_PLURAL.contains(relation);
    }

    private EntityType(String singular, String plural) {
        this.singular = singular;
        this.plural = plural;
    }

    public String getRootEntitySet() {
        return plural;
    }

    public List<String> getRelations() {
        return Collections.unmodifiableList(relations);
    }

    public List<String> getProperties() {
        return Collections.unmodifiableList(properties);
    }

    private void addProperties(String... properties) {
        this.properties.addAll(Arrays.asList(properties));
    }

    private void addRelations(String... relations) {
        this.relations.addAll(Arrays.asList(relations));
    }

}
