package ui_console;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import model.Schedule;

public class Main {
    private static final String[] CATEGORIES = {"観光", "食事", "移動", "宿泊"};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Schedule> schedules = new ArrayList<>();

        System.out.println("==============================");
        System.out.println("        旅行予定アプリ        ");
        System.out.println("==============================");

        while (true) {
            System.out.println("\n--- メニュー ---");
            System.out.println("1: 予定追加");
            System.out.println("2: 予定編集");
            System.out.println("3: 予定削除");
            System.out.println("4: 予定一覧");
            System.out.println("0: 終了");
            System.out.print("選択してください: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    Schedule newSchedule = inputSchedule(scanner);
                    schedules.add(newSchedule);
                    System.out.println("予定を追加しました！");
                    break;
                case "2":
                    editSchedule(scanner, schedules);
                    break;
                case "3":
                    deleteSchedule(scanner, schedules);
                    break;
                case "4":
                    displayAllSchedules(schedules);
                    break;
                case "0":
                    System.out.println("アプリを終了します。");
                    scanner.close();
                    return;
                default:
                    System.out.println("無効な選択です。再度入力してください。");
            }
        }
    }

    // --- 予定入力 ---
    private static Schedule inputSchedule(Scanner scanner) {
        System.out.println("\n予定のタイトルを入力してください:");
        String title = scanner.nextLine();

        LocalDateTime dateTime = null;
        while (dateTime == null) {
            System.out.println("予定の日時を入力してください:");
            System.out.println(" - 年あり: 2025-09-08 または 2025-09-08T15:00");
            System.out.println(" - 年なし: 09-08 または 09-08T15:00");
            System.out.println(" - 相対形式: +30m, +2h, +1d, +1w");
            String input = scanner.nextLine();
            try {
                dateTime = parseInput(input);
            } catch (Exception e) {
                System.out.println("入力形式が不正です。再入力してください。");
            }
        }

        // カテゴリ入力
        System.out.println("カテゴリを選択してください:");
        for (int i = 0; i < CATEGORIES.length; i++) {
            System.out.println((i + 1) + ": " + CATEGORIES[i]);
        }
        String category = CATEGORIES[0]; // デフォルト
        try {
            int catIndex = Integer.parseInt(scanner.nextLine()) - 1;
            if (catIndex >= 0 && catIndex < CATEGORIES.length) {
                category = CATEGORIES[catIndex];
            } else {
                System.out.println("無効な選択。デフォルト: " + category);
            }
        } catch (Exception e) {
            System.out.println("無効入力。デフォルト: " + category);
        }

        return new Schedule(title, dateTime, category);
    }

    // --- 予定編集 ---
    private static void editSchedule(Scanner scanner, ArrayList<Schedule> schedules) {
        if (schedules.isEmpty()) {
            System.out.println("予定がありません。");
            return;
        }

        displayAllSchedules(schedules);

        System.out.print("編集する予定の番号を入力してください: ");
        int index = -1;
        try {
            index = Integer.parseInt(scanner.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("番号入力不正です。編集を中止します。");
            return;
        }

        if (index < 0 || index >= schedules.size()) {
            System.out.println("無効な番号です。編集を中止します。");
            return;
        }

        Schedule s = schedules.get(index);

        System.out.println("現在のタイトル: " + s.getTitle());
        System.out.print("新しいタイトル（Enterでそのまま）: ");
        String newTitle = scanner.nextLine();
        if (!newTitle.isBlank()) s.setTitle(newTitle);

        System.out.println("現在の日時: " + s.getDateTime());
        System.out.print("新しい日時（Enterでそのまま）: ");
        String newDateStr = scanner.nextLine();
        if (!newDateStr.isBlank()) {
            try {
                LocalDateTime newDate = parseInput(newDateStr);
                s.setDateTime(newDate);
            } catch (Exception e) {
                System.out.println("日時入力不正。日時は変更されません。");
            }
        }

        System.out.println("現在のカテゴリ: " + s.getCategory());
        System.out.println("カテゴリを選択してください:");
        for (int i = 0; i < CATEGORIES.length; i++) {
            System.out.println((i + 1) + ": " + CATEGORIES[i]);
        }
        String newCat = scanner.nextLine();
        try {
            int catIndex = Integer.parseInt(newCat) - 1;
            if (catIndex >= 0 && catIndex < CATEGORIES.length) {
                s.setCategory(CATEGORIES[catIndex]);
            }
        } catch (Exception e) {
            // 変更なし
        }

        System.out.println("予定を更新しました！");
    }

    // --- 予定削除 ---
    private static void deleteSchedule(Scanner scanner, ArrayList<Schedule> schedules) {
        if (schedules.isEmpty()) {
            System.out.println("予定がありません。");
            return;
        }
        displayAllSchedules(schedules);
        System.out.print("削除する予定の番号を入力してください: ");
        int index = -1;
        try {
            index = Integer.parseInt(scanner.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("番号入力不正です。削除を中止します。");
            return;
        }
        if (index < 0 || index >= schedules.size()) {
            System.out.println("無効な番号です。削除を中止します。");
            return;
        }
        schedules.remove(index);
        System.out.println("予定を削除しました！");
    }

    // --- 一覧表示 ---
    private static void displayAllSchedules(ArrayList<Schedule> schedules) {
        if (schedules.isEmpty()) {
            System.out.println("予定がありません。");
            return;
        }

        schedules.sort((s1, s2) -> s1.getDateTime().compareTo(s2.getDateTime()));

        LocalDateTime now = LocalDateTime.now();
        System.out.println("\n=== 予定一覧 ===");
        for (int i = 0; i < schedules.size(); i++) {
            System.out.print((i + 1) + ": ");
            displaySchedule(schedules.get(i), now);	// nowでLocalDateTime.now()を渡す
        }
    }

    // --- 個別予定表示 ---
    private static void displaySchedule(Schedule s, LocalDateTime now) {
        LocalDateTime dt = s.getDateTime();
        boolean isFuture = dt.isAfter(now);

        // 日付表示（今日なら省略）
        String dateStr = dt.toLocalDate().equals(now.toLocalDate()) ? "" :
                         dt.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " ";

        // 時間差を Duration で取得
        Duration duration = Duration.between(now, dt);
        long totalMinutes = Math.abs(duration.toMinutes());

        long days = totalMinutes / (24 * 60);
        long hours = (totalMinutes % (24 * 60)) / 60;
        long minutes = totalMinutes % 60;

        String diffStr = String.format("%02d日%02d時間%02d分", days, hours, minutes);

        String displayText = dateStr + s.getTitle() + " [" + s.getCategory() + "] → "
                            + (isFuture ? "予定まで " : "予定から ") + diffStr
                            + (isFuture ? "" : " 経過しています");

        // 色分け
        if (isFuture) System.out.println("\u001B[32m" + displayText + "\u001B[0m");
        else System.out.println("\u001B[31m" + displayText + "\u001B[0m");
    }


    // --- 入力文字列解析 ---
    private static LocalDateTime parseInput(String input) {
        LocalDateTime dateTime;
        if (input.startsWith("+")) {
            LocalDateTime now = LocalDateTime.now();
            int value = Integer.parseInt(input.substring(1, input.length() - 1));
            char unit = input.charAt(input.length() - 1);
            switch (unit) {
                case 'm': dateTime = now.plusMinutes(value); break;
                case 'h': dateTime = now.plusHours(value); break;
                case 'd': dateTime = now.plusDays(value); break;
                case 'w': dateTime = now.plusWeeks(value); break;
                default: throw new IllegalArgumentException("不明な単位: " + unit);
            }
        } else if (input.matches("\\d{2}-\\d{2}")) {
            String withYear = LocalDate.now().getYear() + "-" + input;
            dateTime = LocalDate.parse(withYear, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                                .atStartOfDay();
        } else if (input.matches("\\d{2}-\\d{2}T\\d{2}:\\d{2}")) {
            String withYear = LocalDate.now().getYear() + "-" + input;
            dateTime = LocalDateTime.parse(withYear, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        } else if (input.contains("T")) {
            dateTime = LocalDateTime.parse(input);
        } else {
            LocalDate date = LocalDate.parse(input);
            dateTime = LocalDateTime.of(date, LocalTime.MIDNIGHT);
        }
        return dateTime;
    }
}
