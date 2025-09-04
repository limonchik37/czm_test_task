package fit.cvut.cz.measurement;

/**
 * Directed segment between two counters (fromId -> toId).
 */
public record Segment(String fromId, String toId) { }