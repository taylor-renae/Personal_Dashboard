package com.acm.dashboard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

// These tests are the spec. Run them, read what fails, make it pass.
// No database or Spring needed: the domain rules can be tested on their own.
class TodoTest {

    private final LocalDate today = LocalDate.now();
    private final LocalDate tomorrow = today.plusDays(1);
    private final LocalDate yesterday = today.minusDays(1);

    @Nested
    @DisplayName("Task 1: complete() and reopen()")
    class CompleteAndReopen {

        @Test
        void newTodoStartsNotCompleted() {
            Todo todo = Todo.forToday("Study for quiz");
            assertFalse(todo.isCompleted());
        }

        @Test
        void completeMarksTodoAsDone() {
            Todo todo = Todo.forToday("Study for quiz");
            todo.complete();
            assertTrue(todo.isCompleted());
        }

        @Test
        void reopenMarksTodoAsNotDone() {
            Todo todo = Todo.forToday("Study for quiz");
            todo.complete();
            todo.reopen();
            assertFalse(todo.isCompleted());
        }
    }

    @Nested
    @DisplayName("Task 2: title rules")
    class TitleRules {

        @Test
        void rejectsNullTitle() {
            assertThrows(IllegalArgumentException.class, () -> Todo.forToday(null));
        }

        @Test
        void rejectsBlankTitle() {
            assertThrows(IllegalArgumentException.class, () -> Todo.forToday("   "));
        }

        @Test
        void trimsExtraSpaces() {
            Todo todo = Todo.forToday("  Study for quiz  ");
            assertEquals("Study for quiz", todo.getTitle());
        }

        @Test
        void rejectsTitleOver200Characters() {
            String tooLongTitle = "a".repeat(201);
            assertThrows(IllegalArgumentException.class, () -> Todo.forToday(tooLongTitle));
        }

        @Test
        void allowsTitleOfExactly200Characters() {
            String maxLengthTitle = "a".repeat(200);
            Todo todo = Todo.forToday(maxLengthTitle);
            assertEquals(200, todo.getTitle().length());
        }
    }

    @Nested
    @DisplayName("Task 3: rename()")
    class Rename {

        @Test
        void renameChangesTitle() {
            Todo todo = Todo.forToday("Study for quiz");
            todo.rename("Study for final");
            assertEquals("Study for final", todo.getTitle());
        }

        @Test
        void renameFollowsTheSameTitleRules() {
            Todo todo = Todo.forToday("Study for quiz");
            assertThrows(IllegalArgumentException.class, () -> todo.rename(""));
        }
    }

    @Nested
    @DisplayName("Task 4: isCarryover()")
    class Carryover {

        @Test
        void unfinishedTodoFromEarlierDayIsCarryover() {
            Todo todo = Todo.forToday("Read chapter 6");
            // Pretend it's tomorrow: today's unfinished to-do should carry over
            assertTrue(todo.isCarryover(tomorrow));
        }

        @Test
        void completedTodoIsNotCarryover() {
            Todo todo = Todo.forToday("Read chapter 6");
            todo.complete();
            assertFalse(todo.isCarryover(tomorrow));
        }

        @Test
        void todaysTodoIsNotCarryoverToday() {
            Todo todo = Todo.forToday("Read chapter 6");
            assertFalse(todo.isCarryover(today));
        }
    }

    @Nested
    @DisplayName("Task 5: moveTo() and past dates")
    class MoveTo {

        @Test
        void moveToChangesTheDate() {
            Todo todo = Todo.forToday("Gym");
            todo.moveTo(tomorrow);
            assertEquals(tomorrow, todo.getTodoDate());
        }

        @Test
        void cannotMoveIntoThePast() {
            Todo todo = Todo.forToday("Gym");
            assertThrows(IllegalArgumentException.class, () -> todo.moveTo(yesterday));
        }

        @Test
        void cannotMoveACompletedTodo() {
            Todo todo = Todo.forToday("Gym");
            todo.complete();
            assertThrows(IllegalStateException.class, () -> todo.moveTo(tomorrow));
        }

        @Test
        void cannotPlanATodoInThePast() {
            assertThrows(IllegalArgumentException.class, () -> Todo.forDate("Gym", yesterday));
        }
    }

    @Nested
    @DisplayName("Task 6 (stretch): fromCanvasAssignment()")
    class FromCanvas {

        @Test
        void buildsTitleFromCourseAndAssignment() {
            Todo todo = Todo.fromCanvasAssignment("CSCI 350", "Lab 6", 1234L);
            assertEquals("CSCI 350: Lab 6", todo.getTitle());
        }

        @Test
        void remembersTheCanvasAssignment() {
            Todo todo = Todo.fromCanvasAssignment("CSCI 350", "Lab 6", 1234L);
            assertTrue(todo.isFromCanvas());
            assertEquals(1234L, todo.getCanvasAssignmentId());
        }

        @Test
        void requiresAnAssignmentId() {
            assertThrows(IllegalArgumentException.class,
                    () -> Todo.fromCanvasAssignment("CSCI 350", "Lab 6", null));
        }
    }
}
