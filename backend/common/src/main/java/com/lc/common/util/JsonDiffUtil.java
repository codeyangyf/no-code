package com.lc.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class JsonDiffUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiffEntry {
        private String path;
        private String operation;
        private Object oldValue;
        private Object newValue;

        @Override
        public String toString() {
            return String.format("[%s] %s: %s -> %s", operation, path, oldValue, newValue);
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiffResult {
        private List<DiffEntry> changes;
        private boolean hasChanges;

        public static DiffResult empty() {
            return new DiffResult(Collections.emptyList(), false);
        }

        public static DiffResult of(List<DiffEntry> changes) {
            return new DiffResult(changes, !changes.isEmpty());
        }

        public String toSummary() {
            if (!hasChanges) {
                return "无变更";
            }
            StringBuilder sb = new StringBuilder();
            sb.append("检测到").append(changes.size()).append("处变更：\n");
            for (DiffEntry entry : changes) {
                sb.append("  - ").append(entry.toString()).append("\n");
            }
            return sb.toString();
        }
    }

    public static DiffResult compare(Object source, Object target) {
        if (source == null && target == null) {
            return DiffResult.empty();
        }

        try {
            JsonNode sourceNode = objectMapper.valueToTree(source);
            JsonNode targetNode = objectMapper.valueToTree(target);
            List<DiffEntry> changes = new ArrayList<>();
            compareNodes("", sourceNode, targetNode, changes);
            return DiffResult.of(changes);
        } catch (Exception e) {
            log.warn("Failed to compare objects: {}", e.getMessage());
            return DiffResult.empty();
        }
    }

    public static DiffResult compareJson(String sourceJson, String targetJson) {
        if (sourceJson == null && targetJson == null) {
            return DiffResult.empty();
        }

        try {
            JsonNode sourceNode = objectMapper.readTree(sourceJson);
            JsonNode targetNode = objectMapper.readTree(targetJson);
            List<DiffEntry> changes = new ArrayList<>();
            compareNodes("", sourceNode, targetNode, changes);
            return DiffResult.of(changes);
        } catch (Exception e) {
            log.warn("Failed to compare JSON strings: {}", e.getMessage());
            return DiffResult.empty();
        }
    }

    public static String generateDiffSummary(Object source, Object target) {
        return compare(source, target).toSummary();
    }

    public static String generateDiffSummary(String sourceJson, String targetJson) {
        return compareJson(sourceJson, targetJson).toSummary();
    }

    private static void compareNodes(String path, JsonNode source, JsonNode target, List<DiffEntry> changes) {
        if (source == null && target == null) {
            return;
        }

        if (source == null) {
            changes.add(new DiffEntry(path, "ADDED", null, convertNodeValue(target)));
            return;
        }

        if (target == null) {
            changes.add(new DiffEntry(path, "DELETED", convertNodeValue(source), null));
            return;
        }

        if (!source.isContainerNode() && !target.isContainerNode()) {
            if (!source.equals(target)) {
                changes.add(new DiffEntry(path, "MODIFIED", convertNodeValue(source), convertNodeValue(target)));
            }
            return;
        }

        if (source.isObject() && target.isObject()) {
            Set<String> allFields = new HashSet<>();
            Iterator<String> sourceFields = source.fieldNames();
            sourceFields.forEachRemaining(allFields::add);
            Iterator<String> targetFields = target.fieldNames();
            targetFields.forEachRemaining(allFields::add);

            for (String field : allFields) {
                String fieldPath = path.isEmpty() ? field : path + "." + field;
                compareNodes(fieldPath, source.get(field), target.get(field), changes);
            }
            return;
        }

        if (source.isArray() && target.isArray()) {
            int maxSize = Math.max(source.size(), target.size());
            for (int i = 0; i < maxSize; i++) {
                String indexPath = path + "[" + i + "]";
                JsonNode sourceItem = i < source.size() ? source.get(i) : null;
                JsonNode targetItem = i < target.size() ? target.get(i) : null;
                compareNodes(indexPath, sourceItem, targetItem, changes);
            }
            return;
        }

        changes.add(new DiffEntry(path, "MODIFIED", convertNodeValue(source), convertNodeValue(target)));
    }

    private static Object convertNodeValue(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }

        if (node.isTextual()) {
            return node.asText();
        }

        if (node.isNumber()) {
            if (node.isInt()) {
                return node.asInt();
            }
            if (node.isLong()) {
                return node.asLong();
            }
            if (node.isDouble()) {
                return node.asDouble();
            }
            return node.decimalValue();
        }

        if (node.isBoolean()) {
            return node.asBoolean();
        }

        if (node.isArray()) {
            List<Object> list = new ArrayList<>();
            for (JsonNode item : node) {
                list.add(convertNodeValue(item));
            }
            return list;
        }

        if (node.isObject()) {
            Map<String, Object> map = new LinkedHashMap<>();
            Iterator<String> fields = node.fieldNames();
            while (fields.hasNext()) {
                String field = fields.next();
                map.put(field, convertNodeValue(node.get(field)));
            }
            return map;
        }

        return node.asText();
    }
}