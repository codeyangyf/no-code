package com.lc.project.validation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lc.project.dto.config.ConfigDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConfigValidator {

    private final ObjectMapper objectMapper;

    private static final Set<String> VALID_COMPONENT_TYPES = Set.of(
            "Text", "TextArea", "Input", "Number", "Select", "Radio", "Checkbox",
            "DatePicker", "DateTimePicker", "FileUpload", "Button", "Card",
            "Table", "Form", "Layout", "Grid", "Divider", "Icon"
    );

    private static final Set<String> VALID_FIELD_TYPES = Set.of(
            "TEXT", "TEXTAREA", "NUMBER", "DATE", "DATETIME", "BOOLEAN",
            "SELECT", "RADIO", "CHECKBOX", "FILE", "REFERENCE"
    );

    private static final Set<String> VALID_DATA_SOURCE_TYPES = Set.of(
            "MYSQL", "SQLITE", "API", "POSTGRESQL"
    );

    public ConfigDTO.ValidationResult validate(ConfigDTO.ProjectConfig config) {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        validateSchemaVersion(config, errors);
        validateProjectMeta(config.getProject(), errors);
        validatePages(config.getPages(), errors, warnings);
        validateDataModels(config.getDataModels(), errors, warnings);
        validateDataSources(config.getDataSources(), errors, warnings);

        return ConfigDTO.ValidationResult.builder()
                .valid(errors.isEmpty())
                .errors(errors)
                .warnings(warnings)
                .build();
    }

    private void validateSchemaVersion(ConfigDTO.ProjectConfig config, List<String> errors) {
        if (config.getSchemaVersion() == null || config.getSchemaVersion().isEmpty()) {
            errors.add("schemaVersion is required");
        }
    }

    private void validateProjectMeta(ConfigDTO.ProjectMeta project, List<String> errors) {
        if (project == null) {
            errors.add("project meta is required");
            return;
        }
        if (project.getId() == null || project.getId().isEmpty()) {
            errors.add("project.id is required");
        }
        if (project.getCode() == null || project.getCode().isEmpty()) {
            errors.add("project.code is required");
        }
        if (project.getName() == null || project.getName().isEmpty()) {
            errors.add("project.name is required");
        }
    }

    private void validatePages(List<ConfigDTO.PageConfig> pages, List<String> errors, List<String> warnings) {
        if (pages == null) return;

        Set<String> pageCodes = new HashSet<>();
        for (int i = 0; i < pages.size(); i++) {
            ConfigDTO.PageConfig page = pages.get(i);
            String prefix = "pages[" + i + "]";

            if (page.getCode() == null || page.getCode().isEmpty()) {
                errors.add(prefix + ".code is required");
            } else if (!pageCodes.add(page.getCode())) {
                errors.add(prefix + ".code is duplicated: " + page.getCode());
            }

            if (page.getName() == null || page.getName().isEmpty()) {
                errors.add(prefix + ".name is required");
            }

            validateComponents(page.getComponents(), prefix + ".components", errors, warnings);
        }
    }

    private void validateComponents(List<ConfigDTO.ComponentConfig> components, String prefix,
                                    List<String> errors, List<String> warnings) {
        if (components == null) return;

        for (int i = 0; i < components.size(); i++) {
            ConfigDTO.ComponentConfig comp = components.get(i);
            String compPrefix = prefix + "[" + i + "]";

            if (comp.getType() == null || comp.getType().isEmpty()) {
                errors.add(compPrefix + ".type is required");
            } else if (!VALID_COMPONENT_TYPES.contains(comp.getType())) {
                errors.add(compPrefix + ".type is invalid: " + comp.getType());
            }

            if (comp.getName() == null || comp.getName().isEmpty()) {
                warnings.add(compPrefix + ".name is recommended");
            }
        }
    }

    private void validateDataModels(List<ConfigDTO.DataModelConfig> models, List<String> errors, List<String> warnings) {
        if (models == null) return;

        Set<String> modelCodes = new HashSet<>();
        for (int i = 0; i < models.size(); i++) {
            ConfigDTO.DataModelConfig model = models.get(i);
            String prefix = "dataModels[" + i + "]";

            if (model.getCode() == null || model.getCode().isEmpty()) {
                errors.add(prefix + ".code is required");
            } else if (!modelCodes.add(model.getCode())) {
                errors.add(prefix + ".code is duplicated: " + model.getCode());
            }

            if (model.getName() == null || model.getName().isEmpty()) {
                errors.add(prefix + ".name is required");
            }

            validateModelFields(model.getFields(), prefix + ".fields", errors, warnings);
        }
    }

    private void validateModelFields(List<ConfigDTO.ModelFieldConfig> fields, String prefix,
                                     List<String> errors, List<String> warnings) {
        if (fields == null) return;

        Set<String> fieldCodes = new HashSet<>();
        for (int i = 0; i < fields.size(); i++) {
            ConfigDTO.ModelFieldConfig field = fields.get(i);
            String fieldPrefix = prefix + "[" + i + "]";

            if (field.getCode() == null || field.getCode().isEmpty()) {
                errors.add(fieldPrefix + ".code is required");
            } else if (!fieldCodes.add(field.getCode())) {
                errors.add(fieldPrefix + ".code is duplicated: " + field.getCode());
            }

            if (field.getName() == null || field.getName().isEmpty()) {
                errors.add(fieldPrefix + ".name is required");
            }

            if (field.getType() == null || field.getType().isEmpty()) {
                errors.add(fieldPrefix + ".type is required");
            } else if (!VALID_FIELD_TYPES.contains(field.getType())) {
                errors.add(fieldPrefix + ".type is invalid: " + field.getType());
            }
        }
    }

    private void validateDataSources(List<ConfigDTO.DataSourceConfig> sources, List<String> errors, List<String> warnings) {
        if (sources == null) return;

        for (int i = 0; i < sources.size(); i++) {
            ConfigDTO.DataSourceConfig source = sources.get(i);
            String prefix = "dataSources[" + i + "]";

            if (source.getName() == null || source.getName().isEmpty()) {
                errors.add(prefix + ".name is required");
            }

            if (source.getType() == null || source.getType().isEmpty()) {
                errors.add(prefix + ".type is required");
            } else if (!VALID_DATA_SOURCE_TYPES.contains(source.getType())) {
                errors.add(prefix + ".type is invalid: " + source.getType());
            }
        }
    }

    public ConfigDTO.ValidationResult validateJson(String json) {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        try {
            JsonNode root = objectMapper.readTree(json);
            if (!root.has("schemaVersion")) {
                errors.add("schemaVersion is required");
            }
            if (!root.has("project")) {
                errors.add("project is required");
            }
        } catch (JsonProcessingException e) {
            errors.add("Invalid JSON: " + e.getMessage());
        }

        return ConfigDTO.ValidationResult.builder()
                .valid(errors.isEmpty())
                .errors(errors)
                .warnings(warnings)
                .build();
    }
}
