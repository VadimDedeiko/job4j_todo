package ru.job4j.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.TaskService;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Controller
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("task") Task task) {
        task.setCreated(LocalDateTime.now());
        taskService.create(task);
        return "redirect:/";
    }

    @GetMapping("/newtask")
    public String addTask(@ModelAttribute("task") Task task) {
        return "newtask";
    }

    @GetMapping({"/", "/index"})
    public String allTasks(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setEmail("Гость");
        }
        model.addAttribute("user", user);
        model.addAttribute("tasks", taskService.findAll());
        return "index";
    }

    @GetMapping("/done")
    public String allTasksDone(Model model) {
        model.addAttribute("tasks", taskService.findAllDone());
        return "done";
    }

    @GetMapping("/new")
    public String allTasksNew(Model model) {
        model.addAttribute("tasks", taskService.findAllNew());
        return "new";
    }

    @GetMapping("/description/{id}")
    public String allTasksNew(Model model, @PathVariable("id") int id) {
        model.addAttribute("task", taskService.findById(id));
        return "description";
    }

    @PostMapping("/check_done/{id}")
    public String checkDone(@PathVariable("id") int id) {
        Task taskbyId = taskService.findById(id);
        taskbyId.setDone(true);
        taskService.update(id, taskbyId);
        return "redirect:/index";
    }

    @PostMapping("/updatedtask/{id}")
    public String toUpdatePage(@ModelAttribute("task") Task task, @PathVariable("id") int id) {
        task.setCreated(LocalDateTime.now());
        taskService.update(id, task);
        return "redirect:/index";
    }

    @GetMapping("/update/{id}")
    public String update(Model model, @PathVariable("id") int id) {
        model.addAttribute("task", taskService.findById(id));
        return "update";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        taskService.delete(id);
        return "redirect:/index";
    }
}
