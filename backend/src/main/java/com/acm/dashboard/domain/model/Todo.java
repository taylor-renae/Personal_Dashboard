package com.acm.dashboard.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "todos")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Todo {

    private static final int MAX_TITLE_LENGTH = 200;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private LocalDate todoDate;
    private boolean completed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Links this to-do back to the Canvas assignment it was created from.
    // Null for to-dos the student typed in themselves.
    private Long canvasAssignmentId;

    // Private so the only way to make a Todo is through the factory methods below.
    // That guarantees every to-do starts out valid.
    private Todo(String title, LocalDate todoDate, Long canvasAssignmentId) {
        this.title = validateTitle(title);
        this.todoDate = todoDate;
        this.canvasAssignmentId = canvasAssignmentId;
        this.completed = false;
    }

    // WORKED EXAMPLE: Creates a to-do for today
    public static Todo forToday(String title) {
        return new Todo(title, LocalDate.now(), null);
    }

    // Creates a to-do planned for a specific day
    public static Todo forDate(String title, LocalDate plannedDate) {
        rejectPastDate(plannedDate);
        return new Todo(title, plannedDate, null);
    }

    // TASK 6 (stretch): Create a to-do from a Canvas assignment.
    //   - If canvasAssignmentId is null, throw an IllegalArgumentException
    //   - The title should look like "CSCI 350: Lab 6"
    //   - It should be planned for today and remember the canvasAssignmentId
    //   Hint: look at how forToday() calls the constructor
    public static Todo fromCanvasAssignment(String courseCode, String assignmentName, Long canvasAssignmentId) {
        throw new UnsupportedOperationException("TODO: implement fromCanvasAssignment()");
    }

    // TASK 1: Mark the to-do as done
    public void complete() {
        throw new UnsupportedOperationException("TODO: implement complete()");
    }

    // TASK 1: Mark a finished to-do as not done again
    public void reopen() {
        throw new UnsupportedOperationException("TODO: implement reopen()");
    }

    // TASK 3: Change the title, following the same rules as when it was created
    //   Hint: you already wrote a method that checks titles
    public void rename(String newTitle) {
        throw new UnsupportedOperationException("TODO: implement rename()");
    }

    // TASK 5: Push the to-do to another day.
    //   - A completed to-do can't be moved: throw an IllegalStateException
    //   - It can't be moved into the past: use rejectPastDate()
    //   - Otherwise, update todoDate
    public void moveTo(LocalDate newDate) {
        throw new UnsupportedOperationException("TODO: implement moveTo()");
    }

    // TASK 4: Return true if this to-do is NOT completed AND its date is before today
    //   Hint: LocalDate has an isBefore() method
    public boolean isCarryover(LocalDate today) {
        throw new UnsupportedOperationException("TODO: implement isCarryover()");
    }

    public boolean isFromCanvas() {
        return canvasAssignmentId != null;
    }

    // TASK 2: Make sure a title is valid, then return the cleaned-up version.
    //   - null or blank: throw an IllegalArgumentException
    //   - Remove extra spaces from the start and end
    //   - Longer than MAX_TITLE_LENGTH: throw an IllegalArgumentException
    private static String validateTitle(String title) {
        // TODO: replace this line with the rules above
        return title;
    }

    // TASK 5: Throw an IllegalArgumentException if the date is null or before today
    private static void rejectPastDate(LocalDate requestedDate) {
        // TODO: add the rule
    }

    // Automatically Sets CreatedAt and UpdatedAt on Created
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Automatically sets UpdatedAt only when Updated
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
