package dev.minoua.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public abstract class Model<T extends Model<T>> {
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    @JsonIgnore
    protected String csvFileName;

    public Model(String fileName) {
        this.createdAt = LocalDateTime.now();
        this.csvFileName = fileName;
        this.updatedAt = LocalDateTime.now();
    }

    // CRUD BASICO

    public List<T> list() {
        List<T> result = new ArrayList<>();
        try {
            if (!Files.exists(Paths.get(csvFileName))) return result;

            List<String> lines = Files.readAllLines(Paths.get(csvFileName));
            if (lines.size() < 2) return result;

            for (int i = 1; i < lines.size(); i++) {
                String csvLine = lines.get(i).trim();
                if (csvLine.isEmpty()) continue;
                T obj = fromCSV(csvLine);
                if (obj != null) {
                    result.add(obj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public T find(Long id) {
        for (T registro : list()) {
            try {
                Field idField = getField(registro.getClass(), "id"); // usando getField
                if (idField == null) continue;

                idField.setAccessible(true);
                Long registroId = (Long) idField.get(registro);
                if (registroId != null && registroId.equals(id)) return registro;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void save(T obj) {
        try {
            Path path = Paths.get(csvFileName);
            boolean arquivoExiste = Files.exists(path);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFileName, true))) {
                if (!arquivoExiste) {
                    writer.write(csvHeader((Class<T>) obj.getClass()));
                    writer.newLine();
                }

                long maxId = 0L;
                for (T record : list()) {
                    Field idField = getField(record.getClass(), "id");
                    if (idField != null) {
                        idField.setAccessible(true);
                        Object value = idField.get(record);
                        if (value instanceof Long recordId && recordId > maxId) maxId = recordId;
                    }
                }

                Field idField = getField(obj.getClass(), "id");
                if (idField != null) {
                    idField.setAccessible(true);
                    idField.set(obj, maxId + 1);
                }

                writer.write(obj.toCSV());
                writer.newLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean update(Long id, T object) {
        try {
            List<T> allRecords = list();
            boolean encontrado = false;

            for (int i = 0; i < allRecords.size(); i++) {
                Field idField = getField(allRecords.get(i).getClass(), "id");
                if (idField != null) {
                    idField.setAccessible(true);
                    Long registroId = (Long) idField.get(allRecords.get(i));
                    if (registroId != null && registroId.equals(id)) {
                        T registroAntigo = allRecords.get(i);

                        object.setId(id);
                        object.setCreatedAt(registroAntigo.getCreatedAt());
                        object.setUpdatedAt(LocalDateTime.now());

                        allRecords.set(i, object);
                        encontrado = true;
                        break;
                    }
                }
            }

            if (encontrado) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFileName))) {
                    writer.write(csvHeader((Class<T>) this.getClass()));
                    writer.newLine();
                    for (T record : allRecords) {
                        writer.write(record.toCSV());
                        writer.newLine();
                    }
                }
            }

            return encontrado;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(Long id) {
        try {
            List<T> allRecords = list();
            List<T> registrosAtualizados = new ArrayList<>();
            boolean removido = false;

            for (T record : allRecords) {
                Field idField = getField(record.getClass(), "id");
                if (idField != null) {
                    idField.setAccessible(true);
                    Long registroId = (Long) idField.get(record);
                    if (registroId != null && registroId.equals(id)) {
                        removido = true;
                    } else {
                        registrosAtualizados.add(record);
                    }
                }
            }

            if (removido) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFileName))) {
                    writer.write(csvHeader((Class<T>) this.getClass()));
                    writer.newLine();
                    for (T record : registrosAtualizados) {
                        writer.write(record.toCSV());
                        writer.newLine();
                    }
                }
            }

            return removido;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ATUALIZACAO PARCIAL
    public boolean partialUpdate(Long id, T object) {
        try {
            T existing = find(id);
            if (existing == null) return false;

            List<String> ignoredFields = Arrays.asList("id", "createdAt");

            for (String fieldName : getCsvFieldOrder()) {
                if (ignoredFields.contains(fieldName)) continue;

                Field sourceField = getField(object.getClass(), fieldName);
                if (sourceField == null) continue;

                sourceField.setAccessible(true);
                Object novoValor = sourceField.get(object);

                if (novoValor != null) {
                    Field targetField = getField(existing.getClass(), fieldName);
                    if (targetField == null) continue;

                    targetField.setAccessible(true);
                    if (targetField.getType().isEnum() && novoValor instanceof String) {
                        Object enumValue = Enum.valueOf((Class<Enum>) targetField.getType(), (String) novoValor);
                        targetField.set(existing, enumValue);
                    } else {
                        targetField.set(existing, novoValor);
                    }
                }
            }

            Field updatedAtField = getField(existing.getClass(), "updatedAt");
            if (updatedAtField != null) {
                updatedAtField.setAccessible(true);
                updatedAtField.set(existing, LocalDateTime.now());
            }

            Path path = Paths.get(csvFileName);
            List<String> lines = Files.readAllLines(path);
            for (int i = 1; i < lines.size(); i++) {
                T linhaObj = fromCSV(lines.get(i));
                if (linhaObj.getId().equals(id)) {
                    lines.set(i, existing.toCSV());
                    break;
                }
            }
            Files.write(path, lines);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    // METODO AUXILIAR PARA BUSCAR UM CAMPO NA CLASSE OU SUPERCLASSE
    private Field getField(Class<?> classe, String fieldName) {
        while (classe != null) {
            try {
                return classe.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                classe = classe.getSuperclass();
            }
        }
        return null;
    }


    // METODOS DE MANIPULACAO DE CSV

    public String csvHeader(Class<?> clazz) {
        return String.join(",", getCsvFieldOrder());
    }

    public String toCSV() {
        List<String> values = new ArrayList<>();
        for (String fieldName : getCsvFieldOrder()) {
            try {
                Field field;
                try {
                    field = this.getClass().getDeclaredField(fieldName);
                } catch (NoSuchFieldException e) {
                    field = this.getClass().getSuperclass().getDeclaredField(fieldName);
                }
                field.setAccessible(true);
                Object value = field.get(this);
                if (value instanceof LocalDateTime dateTime) {
                    values.add(dateTime.toLocalDate().toString() + " " + dateTime.toLocalTime().withNano(0).toString());
                } else {
                    values.add(value != null ? value.toString() : "null");
                }
            } catch (Exception e) {
                values.add("null");
            }
        }
        return String.join(",", values);
    }


    // METODOS ABSTRATOS QUE DEVEM SER IMPLEMENTADOS NAS CLASSES FILHAS
    public abstract T fromCSV(String csvLine);

    @JsonIgnore
    public abstract List<String> getCsvFieldOrder();

}
