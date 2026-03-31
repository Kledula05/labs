package com.ustenko;

import com.ustenko.model.Character;
import com.ustenko.repository.CharacterRepository;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        String inputFile  = "characters.csv";
        String outputFile = "characters_modified.csv";

        CharacterRepository repo = new CharacterRepository(inputFile);

        System.out.println("════════════════════════════════════════════════");
        System.out.println("  ШАГ 1 — Загрузка персонажей из CSV");
        System.out.println("════════════════════════════════════════════════");
        repo.loadFromFile();
        repo.printAll();

        System.out.println("════════════════════════════════════════════════");
        System.out.println("  ШАГ 2 — Вставка новых персонажей в середину");
        System.out.println("════════════════════════════════════════════════");


        List<Character> newCharacters = List.of(
                new Character(100, "Галактический Президент Закссон", "Жив",   "Инопланетянин",   "Мужской",    "Штаб-квартира Галактической Федерации"),
                new Character(101, "Доктор Вортекс Макбрейн",        "Неизвестно", "Киборг",  "Мужской",    "Измерение X-42"),
                new Character(102, "Принцессa Зара из Нексуса-9",    "Жив",   "Инопланетянка",   "Женский",   "Кластер Нексус-9")
        );

        repo.insertAtMiddle(newCharacters);
        repo.printAll();


        System.out.println("════════════════════════════════════════════════");
        System.out.println("  ШАГ 3 — Сохранение обновленного списка в файл");
        System.out.println("════════════════════════════════════════════════");
        repo.saveToFile(outputFile);


        System.out.println("\n════════════════════════════════════════════════");
        System.out.println("   Демонстрация CRUD операций");
        System.out.println("════════════════════════════════════════════════");

        System.out.println("\n--- СОЗДАНИЕ ---");
        Character newHero = new Character(0, "Омега Бот-7", "Жив", "Робот", "Неизвестно", "Измерение Схем");
        repo.create(newHero);


        System.out.println("\n--- ЧТЕНИЕ по id=2 ---");
        repo.findById(2).ifPresentOrElse(
                c -> System.out.println("Найден: " + c),
                () -> System.out.println("Не найден")
        );


        System.out.println("\n--- ОБНОВЛЕНИЕ id=2 (Статус Морти - Мёртв) ---");
        repo.update(new Character(2, "Морти Смит", "Мёртв", "Человек", "Мужской", "Земля (C-137)"));


        repo.findById(2).ifPresent(c -> System.out.println("После обновления: " + c));


        System.out.println("\n--- УДАЛЕНИЕ id=5  ---");
        repo.deleteById(5);

        System.out.println("\nВсего персонажей в списке после всех операций: " + repo.size());

    }
}