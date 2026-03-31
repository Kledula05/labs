package com.ustenko.repository;

import com.ustenko.model.Character;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.net.URL;


public class CharacterRepository {

    private String filePath;
    private LinkedList<Character> characters;

    public CharacterRepository(String filePath) {
        this.filePath = filePath;
        this.characters = new LinkedList<>();
    }

    // ─────────────────────────────────────────────
    //  ЧТЕНИЕ ИЗ ФАЙЛА
    // ─────────────────────────────────────────────

    public void loadFromFile() {
        characters.clear();

        try {
            // Сначала пробуем загрузить из classpath (src/main/resources/)
            InputStream is = getClass().getClassLoader().getResourceAsStream(filePath);

            // Если не нашли в classpath — пробуем как обычный файл
            if (is == null) {
                is = new FileInputStream(filePath);
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            String headerLine = br.readLine(); // читаем заголовок, чтобы определить формат
            if (headerLine == null) {
                System.err.println("Файл пустой: " + filePath);
                br.close();
                return;
            }

            // Определяем формат CSV по заголовку
            // Полный формат (9 колонок): id,name,status,species,type,gender,origin/name,location/name,created
            // Сокращённый формат (6 колонок): id,name,status,species,gender,origin
            String[] headers = headerLine.split(",", -1);
            boolean fullFormat = headers.length >= 7; // есть колонка type

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",", -1);

                try {
                    Character c;
                    if (fullFormat) {
                        // id,name,status,species,type,gender,origin/name,...
                        if (parts.length < 7) continue;
                        c = new Character(
                                Integer.parseInt(parts[0].trim()), // id
                                parts[1].trim(),                   // name
                                parts[2].trim(),                   // status
                                parts[3].trim(),                   // species
                                parts[5].trim(),                   // gender (parts[4] = type — пропускаем)
                                parts[6].trim()                    // origin/name
                        );
                    } else {
                        // id,name,status,species,gender,origin
                        if (parts.length < 6) continue;
                        c = new Character(
                                Integer.parseInt(parts[0].trim()), // id
                                parts[1].trim(),                   // name
                                parts[2].trim(),                   // status
                                parts[3].trim(),                   // species
                                parts[4].trim(),                   // gender
                                parts[5].trim()                    // origin
                        );
                    }
                    characters.add(c);
                } catch (NumberFormatException e) {
                    System.err.println("Пропущена строка (некорректный id): " + line);
                }
            }

            br.close();

            System.out.println("Загружено " + characters.size() + " персонажей из " + filePath);

        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────
    //  ВСТАВКА В СЕРЕДИНУ СПИСКА
    // ─────────────────────────────────────────────



    public void insertAtMiddle(List<Character> newCharacters) {
        int middleIndex = characters.size() / 2;

        // Добавляем в обратном порядке, чтобы порядок сохранился
        // (если добавлять по порядку, каждый следующий сдвинет предыдущий)
        for (int i = newCharacters.size() - 1; i >= 0; i--) {
            characters.add(middleIndex, newCharacters.get(i));
        }

        System.out.println("Вставлено " + newCharacters.size() +
                " персонажей в позицию " + middleIndex);
    }
    // ─────────────────────────────────────────────
    //  ЗАПИСЬ В ФАЙЛ
    // ─────────────────────────────────────────────

    public void saveToFile(String outputPath) {
        try {
            FileWriter fw = new FileWriter(outputPath);
            BufferedWriter writer = new BufferedWriter(fw);
            writer.write("id,name,status,species,gender,origin");
            writer.newLine();

            for (int i = 0; i < characters.size(); i++) {
                Character c = characters.get(i);
                writer.write(c.toCsv());
                writer.newLine();
            }


            writer.close();
            fw.close();

            System.out.println("Сохранено " + characters.size() + " персонажей в " + outputPath);

        } catch (IOException e) {
            System.err.println("Ошибка при записи файла: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────
    //  CRUD — CREATE
    // ─────────────────────────────────────────────


    public void create(Character character) {
        int maxId = 0;
        for (int i = 0; i < characters.size(); i++) {
            Character c = characters.get(i);
            if (c.getId() > maxId) {
                maxId = c.getId();
            }
        }
        int nextId = maxId + 1;

        character.setId(nextId);
        characters.add(character);


        saveToFile("characters_modified.csv");

        System.out.println("Создан персонаж: " + character.getName());
    }

    // ─────────────────────────────────────────────
    //  CRUD — READ
    // ─────────────────────────────────────────────

    public Optional<Character> findById(int id) {
        for (int i = 0; i < characters.size(); i++) {
            Character c = characters.get(i);
            if (c.getId() == id) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    public boolean update(Character updated) {
        for (int i = 0; i < characters.size(); i++) {
            Character c = characters.get(i);
            if (c.getId() == updated.getId()) {
                characters.set(i, updated);
                saveToFile("characters_modified.csv");
                System.out.println("Обновлён: " + updated.getName());
                return true;
            }
        }
        System.out.println("Не найден персонаж с id=" + updated.getId());
        return false;
    }

    // ─────────────────────────────────────────────
    //  CRUD — DELETE
    // ─────────────────────────────────────────────

    public boolean deleteById(int id) {
        boolean removed = false;

        for (int i = 0; i < characters.size(); i++) {
            if (characters.get(i).getId() == id) {
                characters.remove(i);
                removed = true;
                break;
            }
        }

        if (removed) {
            saveToFile(filePath);
            System.out.println("Удален персонаж с id=" + id);
        } else {
            System.out.println(" Персонаж с id=" + id + " не найден.");
        }
        return removed;
    }


    public void printAll() {
        System.out.println("\n--- Список персонажей в LinkedList ---");
        for (int i = 0; i < characters.size(); i++) {
            System.out.println("[" + i + "] " + characters.get(i));
        }

        System.out.println("--------------------------------------\n");
    }

    /**
     * Возвращает текущий размер списка.
     */
    public int size() {
        return characters.size();
    }
}