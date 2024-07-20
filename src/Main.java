import java.util.Scanner;

public class Main {

    public static TaskManager taskManager;
    public static Scanner scanner;


    public static void main(String[] args) {

        taskManager = new TaskManager();
        scanner = new Scanner(System.in);


        String command;
        System.out.println("Вас приветствует трекер задач");
        while (true) {
        printMenu();

        command = scanner.nextLine();

            switch (command) {
                case "1":
                    System.out.println("Выберите тип задачи, которую хотите добавить");
                    System.out.println("1 - Простая");
                    System.out.println("2 - Составная");
                    System.out.println("3 - Подзадача");
                    String taskType = scanner.nextLine();
                    if (taskType.equals("1")) {
                        taskPutter("None");
                       // System.out.println(taskManager.getTasks());
                    } else if (taskType.equals("2")) {
                        epicPutter("None");
                        //System.out.println(taskManager.getEpics());

                    } else if (taskType.equals("3")) {
                        System.out.println("Введите идентификационный номер задачи к которой нужно добавить подзадачу");
                        String idToAdd=scanner.nextLine();
                        if (taskManager.scanId(idToAdd).getCount()==2) {
                            System.out.println("Введите название подзадачи");
                            String taskName = scanner.nextLine();
                            System.out.println("Введите краткое описание подзадачи");
                            String taskDescription = scanner.nextLine();
                            Status taskStatus=Status.NEW;
                            try {
                                System.out.println("Введите cтатус задачи (NEW, IN_PROGRESS,DONE)");
                                taskStatus = Status.valueOf(scanner.nextLine());
                            } catch (IllegalArgumentException e) {
                                System.out.println("Вы ввели недопустимый вариант, поэтому выбран статус по умолчанию (NEW)");
                            }
                            taskManager.subTaskPut(taskManager.getEpics().get(idToAdd), new SubTask(taskName,taskDescription,taskStatus));
                            taskManager.checkEpicStatus(taskManager.getEpics().get(idToAdd));
                        } else {
                            System.out.println("Такого индикатора нет");
                        }
                    }

                    break;
                case "2":
                    System.out.println("Простые задачи:");
                    for (String s:taskManager.getTasks().keySet() ){
                        System.out.println(s);
                        System.out.println(taskManager.getTasks().get(s));
                    }
                    if (taskManager.getTasks().isEmpty()) {
                        System.out.println("Список пуст");
                    }
                    System.out.println();
                    System.out.println("Составные задачи:");
                    for (String s:taskManager.getEpics().keySet() ){
                        System.out.println(s);
                        System.out.println(taskManager.getEpics().get(s));
                    }
                    if (taskManager.getEpics().isEmpty()) {
                        System.out.println("Список пуст");
                    }
                    System.out.println();
                    break;
                case "3":
                    System.out.println("Введите номер идентификатора задачи для ее удаления");
                    String idToDelete = scanner.nextLine();
                    taskManager.taskDelete(idToDelete);
                    break;
                case "4":
                    taskManager.Clear();
                    System.out.println("Списки очищены");
                    break;
                case "5":
                    System.out.println("Введите номер идентификатора задачи для замены");
                    String idToReplace = scanner.nextLine();
                    switch (taskManager.scanId(idToReplace).getCount()) {
                        case 1:
                            System.out.println("Вы можете заменить простую задачу");
                            taskPutter(idToReplace);
                            break;
                        case 2:
                            System.out.println("Вы можете заменить составную задачу");
                            epicPutter(idToReplace);
                            break;
                     /*   case 3:
                            System.out.println("Вы можете заменить и простую и составную задачу");
                            System.out.println("Выберите задачу для замены");
                            System.out.println("1 - Простая");
                            System.out.println("2 - Составная");
                            String typeOfTask = scanner.nextLine();
                            if (typeOfTask.equals("1")) {
                                taskPutter(idToReplace);
                            } else if (typeOfTask.equals("2")) {
                                epicPutter(idToReplace);
                            } else {
                                System.out.println("Такого варианта нет");
                            }
                            break;*/
                        case 3:
                            Epic newEpic = taskManager.scanId(idToReplace).getEpic();
                            System.out.println("Вы можете заменить подзадачу");
                            System.out.println("Введите название подзадачи");
                            String taskName = scanner.nextLine();
                            System.out.println("Введите краткое описание подзадачи");
                            String taskDescription = scanner.nextLine();
                            Status status=Status.NEW;
                            try {
                                System.out.println("Введите cтатус задачи (NEW, IN_PROGRESS,DONE)");
                                status = Status.valueOf(scanner.nextLine());
                            } catch (IllegalArgumentException e) {
                                System.out.println("Вы ввели недопустимый вариант, поэтому выбран статус по умолчанию (NEW)");
                            }
                            taskManager.subTaskReplace(idToReplace,newEpic,new SubTask(taskName,taskDescription,status));
                            taskManager.checkEpicStatus(newEpic);

                            break;

                        default:
                            System.out.println("Такого id нет");

                    }
                    break;
                case "6":
                    System.out.println("Введите номер идентификатора задачи для просмотра");
                    String idToShow = scanner.nextLine();
                    if (taskManager.showTask(idToShow)!=null) {
                        System.out.println(taskManager.showTask(idToShow));
                    } else if (taskManager.showEpic(idToShow)!=null) {
                        System.out.println(taskManager.showEpic(idToShow));
                    } else {
                        System.out.println("Такого номера нет");
                    }
                    break;
                case "7":
                    System.out.println("Введите номер идентификатора задачи для просмотра списка подзадач");
                    String id = scanner.nextLine();
                    if (taskManager.showSubTasks(id)!=null) {
                        System.out.println(taskManager.showSubTasks(id));
                    } else {
                        System.out.println("Такого номера нет");
                    }
                    break;
                default:
                    return;

            }
        }


    }

    public static void taskPutter(String id) {
        System.out.println("Введите название задачи");
        String taskName = scanner.nextLine();
        System.out.println("Введите краткое описание задачи");
        String taskDescription = scanner.nextLine();
        Status status=Status.NEW;
        try {
            System.out.println("Введите cтатус задачи (NEW, IN_PROGRESS,DONE)");
            status = Status.valueOf(scanner.nextLine());
        } catch (IllegalArgumentException e) {
            System.out.println("Вы ввели недопустимый вариант, поэтому выбран статус по умолчанию (NEW)");
        }
        if (id.equals("None")) {
            taskManager.taskPut(new Task(taskName, taskDescription,status));
        } else {
            taskManager.taskReplace(id,new Task(taskName, taskDescription,status));
        }
    }



    public static void epicPutter(String id) {
        System.out.println("Введите название составной задачи");
        String epicName = scanner.nextLine();
        System.out.println("Введите краткое описание составной задачи");
        String epicDescription = scanner.nextLine();
        Status epicStatus=Status.NEW;
        try {
            System.out.println("Введите cтатус задачи (NEW, IN_PROGRESS,DONE)");
            epicStatus = Status.valueOf(scanner.nextLine());
        } catch (IllegalArgumentException e) {
            System.out.println("Вы ввели недопустимый вариант, поэтому выбран статус по умолчанию (NEW)");
        }
        Epic newEpic = new Epic(epicName,epicDescription,epicStatus);
        if (id.equals("None")) {
            taskManager.epicsPut(newEpic);
        } else {
            taskManager.epicReplace(id,newEpic);
        }
        System.out.println("Введите количество подзадач");
        int amountOfSubtasks=-1;
        do {
            try {
                amountOfSubtasks = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Вы ввели не число");
                continue;
            }
            if (amountOfSubtasks<0) {
                System.out.println("Вы ввели число меньше 0");
            }
        } while (amountOfSubtasks<0);
        for (int i=0; i<amountOfSubtasks;i++) {
            System.out.println("Введите название подзадачи");
            String taskName = scanner.nextLine();
            System.out.println("Введите краткое описание подзадачи");
            String taskDescription = scanner.nextLine();
            Status taskStatus=Status.NEW;
            try {
                System.out.println("Введите cтатус задачи (NEW, IN_PROGRESS,DONE)");
                taskStatus = Status.valueOf(scanner.nextLine());
            } catch (IllegalArgumentException e) {
                System.out.println("Вы ввели недопустимый вариант, поэтому выбран статус по умолчанию (NEW)");
            }
            taskManager.subTaskPut(newEpic, new SubTask(taskName,taskDescription,taskStatus));

        }
        if (amountOfSubtasks>0) {
            taskManager.checkEpicStatus(newEpic);
        } else {
            newEpic.setStatus(Status.NEW);
        }
    }

    public static void printMenu() {

        System.out.println("Выберите действие");
        System.out.println("1 - Добавить задачу");
        System.out.println("2 - Получить список всех задач");
        System.out.println("3 - Удаление по идентификатору");
        System.out.println("4 - Удалить все");
        System.out.println("5 - Обновить задачу по идентификатору");
        System.out.println("6 - Получение по идентификатору");
        System.out.println("7 - Просмотр всех подзадач составной задачи");


    }
}
