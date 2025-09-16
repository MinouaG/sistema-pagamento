package dev.minoua.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class Model<T extends Model<T>> {
    private Long id;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    @JsonIgnore
    protected String csvFileName;

    public Model(String fileName) {
        this.createdAt = LocalDateTime.now();
        this.csvFileName = fileName;
        this.updatedAt = LocalDateTime.now();
    }

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
        List<T> registros = this.list();
        for (T registro : registros) {
            try {
                Field idField = registro.getClass().getSuperclass().getDeclaredField("id");
                idField.setAccessible(true);
                Long registroId = (Long) idField.get(registro);
                if (registroId != null && registroId.equals(id)) {
                    return registro;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void save(T obj) {
        boolean arquivoExiste = Files.exists(Paths.get(csvFileName));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFileName, true))) {
            if (!arquivoExiste) {
                writer.write(this.csvHeader((Class<T>) obj.getClass()));
                writer.newLine();
            }

            long maxId = 0L;
            List<T> allRecords = this.list();
            for (T record : allRecords) {
                try {
                    Field idField = record.getClass().getSuperclass().getDeclaredField("id");
                    idField.setAccessible(true);
                    Object value = idField.get(record);
                    if (value instanceof Long) {
                        long recordId = (Long) value;
                        if (recordId > maxId) maxId = recordId;
                    }
                } catch (Exception ignore) {}
            }
            try {
                Field idField = obj.getClass().getSuperclass().getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(obj, maxId + 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            writer.write(obj.toCSV());
            writer.newLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean update(Long id, T object) {
        try {
            List<T> allRecords = list();
            boolean encontrado = false;

            for (int i = 0; i < allRecords.size(); i++) {
                if (allRecords.get(i).getId().equals(id)) {
                    object.setId(id); // garante que o id permaneça igual
                    object.setUpdatedAt(LocalDateTime.now()); // atualiza timestamp
                    allRecords.set(i, object);
                    encontrado = true;
                    break;
                }
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFileName))) {
                writer.write(this.csvHeader((Class<T>) this.getClass()));
                writer.newLine();

                for (T record : allRecords) {
                    writer.write(record.toCSV());
                    writer.newLine();
                }
            }
            return encontrado;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    // Metodos para manipulacao do CSV

    // Metodo abstrato para gerar o header do CSV a ser implementado nas subclasses
    @JsonIgnore
    public abstract List<String> getCsvFieldOrder();

    public String csvHeader(Class<?> clazz) {
        return String.join(",", getCsvFieldOrder());
    }

    // Metodo para converter o objeto numa linha CSV
    public String toCSV() {
        List<String> values = new ArrayList<>();
        for (String fieldName : getCsvFieldOrder()) {
            try {
                Field field;
                try {
                    field = this.getClass().getDeclaredField(fieldName);
                    System.out.println("ESCREVENDO NO ARQUIVO" );
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

    // Delete
    public boolean delete(Long id) {
        try {
            List<T> allRecords = list();
            boolean removido = false;

            List<T> registrosAtualizados = new ArrayList<>();
            for (T record : allRecords) {
                Field idField = record.getClass().getSuperclass().getDeclaredField("id");
                idField.setAccessible(true);
                Long recordId = (Long) idField.get(record);

                if (recordId != null && recordId.equals(id)) {
                    removido = true;
                } else {
                    registrosAtualizados.add(record);
                }
            }

            if (removido) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFileName))) {
                    writer.write(this.csvHeader((Class<T>) this.getClass()));
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

    // ATUALIZACAO PARCIAL PARA PATCH
    public boolean partialUpdate(Long id, T object) {
        try {
            List<T> allRecords = list();
            boolean atualizado = false;

            for (int i = 0; i < allRecords.size(); i++) {
                T registro = allRecords.get(i);
                if (registro.getId().equals(id)) {
                    // Atualiza apenas os campos não nulos
                    for (String fieldName : getCsvFieldOrder()) {
                        try {
                            Field field = object.getClass().getDeclaredField(fieldName);
                            field.setAccessible(true);
                            Object novoValor = field.get(object);

                            if (novoValor != null) {
                                Field targetField;
                                try {
                                    targetField = registro.getClass().getDeclaredField(fieldName);
                                } catch (NoSuchFieldException e) {
                                    targetField = registro.getClass().getSuperclass().getDeclaredField(fieldName);
                                }
                                targetField.setAccessible(true);
                                targetField.set(registro, novoValor);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    // Atualiza timestamp
                    registro.setUpdatedAt(LocalDateTime.now());
                    allRecords.set(i, registro);
                    atualizado = true;
                    break;
                }
            }

            if (atualizado) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFileName))) {
                    writer.write(this.csvHeader((Class<T>) this.getClass()));
                    writer.newLine();
                    for (T record : allRecords) {
                        writer.write(record.toCSV());
                        writer.newLine();
                    }
                }
            }

            return atualizado;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    // Metodo abstrato para converter uma linha CSV em objeto
    public abstract T fromCSV(String csvLine);
}
